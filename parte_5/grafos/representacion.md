---
title: "Representación de grafos"
subtitle: "Matriz, lista y costo de acceso"
subject: Estructuras de Datos
description: Cómo comparar matrices, listas de adyacencia y listas de aristas según memoria, consulta de adyacencia e iteración de vecinos.
---

(parte5-representacion-grafos)=
# Representación de grafos

En grafos, la representación influye tanto como el algoritmo. Antes de correr BFS, Dijkstra o Prim, hace falta decidir cómo se almacenan vértices y aristas y qué consultas conviene optimizar.

Ese punto importa más que en otras familias porque, en un grafo, muchas operaciones pueden formularse de maneras muy distintas:

- consultar si dos vértices son adyacentes,
- iterar todos los vecinos de un vértice,
- recorrer todas las aristas,
- agregar o borrar conexiones,
- guardar pesos y direcciones.

No existe una representación universalmente mejor. Existe una representación mejor **para cierto patrón de consultas y cierto tipo de grafo**.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Comparar las representaciones más comunes de grafos y entender cómo afectan memoria, consulta de adyacencia e iteración de vecinos.

**Prerrequisitos.** Conviene haber leído [Fundamentos de grafos](fundamentos.md), porque este capítulo transforma el lenguaje conceptual en decisiones de estructura.

**Desarrollo.** El capítulo contrasta matriz de adyacencia, lista de adyacencia y lista de aristas, explica cómo codificar dirección y peso, y cierra comparando sus costos según densidad del grafo y tipo de algoritmo.
:::

:::{tip} Idea guía
En grafos, elegir representación es decidir **qué consulta querés volver barata** y cuál aceptás pagar más cara.
:::

## Qué preguntas manda la representación

Antes de mirar variantes conviene identificar qué necesita hacer el programa.

Las preguntas típicas son estas:

- **¿Existen aristas entre `u` y `v`?**
- **¿Cuáles son todos los vecinos de `u`?**
- **¿Cuántas aristas hay?**
- **¿El grafo es denso o disperso?**
- **¿Necesito recorrer todas las aristas o consultar adyacencia puntual?**

Eso obliga a distinguir una noción central.

### Grafos densos y dispersos

Un grafo con `|V|` vértices puede tener, en el caso simple no dirigido, hasta `|V| * (|V| - 1) / 2` aristas.

Conviene hablar de:

- **grafo denso**: la cantidad de aristas está relativamente cerca del máximo posible;
- **grafo disperso**: hay muchas menos aristas que las que podrían existir.

La diferencia es decisiva:

- en densos, una matriz puede ser razonable;
- en dispersos, guardar todas las posibles conexiones suele desperdiciar memoria.

## Matriz de adyacencia

La **matriz de adyacencia** organiza el grafo como una tabla cuadrada `n x n`, donde `n` es la cantidad de vértices.

La idea es simple:

- fila `i`, columna `j` representa la relación entre el vértice `i` y el vértice `j`;
- el contenido puede ser booleano, entero, peso o una marca especial.

### Qué guarda

| Tipo de grafo | Qué podría guardarse en `matriz[i][j]` |
| :--- | :--- |
| No ponderado | `true` / `false` |
| Ponderado | peso de la arista o un valor centinela |
| Dirigido | arista de `i` hacia `j` |
| No dirigido | relación simétrica entre `i` y `j` |

Ejemplo:

```java
public final class GrafoMatriz {
    private final boolean[][] adyacencia;

    public GrafoMatriz(int cantidadVertices) {
        this.adyacencia = new boolean[cantidadVertices][cantidadVertices];
    }

    public void agregarArista(int origen, int destino) {
        this.adyacencia[origen][destino] = true;
    }

    public boolean sonAdyacentes(int origen, int destino) {
        return this.adyacencia[origen][destino];
    }
}
```

### Qué gana

- consulta de adyacencia directa;
- implementación conceptual simple;
- buena opción si el grafo es denso;
- útil cuando los vértices ya se indexan naturalmente.

### Qué paga

- memoria `O(V^2)` aunque casi no haya aristas;
- iterar vecinos de un vértice exige revisar toda la fila;
- agregar vértices suele ser incómodo porque cambia el tamaño de la matriz.

### Invariantes típicos

- la matriz debe ser cuadrada;
- si el grafo es no dirigido, debería cumplirse `matriz[i][j] == matriz[j][i]`;
- si no se permiten lazos, `matriz[i][i]` debería quedar en falso o sin peso válido.

## Lista de adyacencia

La **lista de adyacencia** guarda, para cada vértice, la colección de sus vecinos salientes.

Es la representación más habitual cuando el grafo es disperso y los algoritmos recorren vecinos.

```java
public final class GrafoLista {
    private final Map<String, List<String>> vecinos;

    public GrafoLista() {
        this.vecinos = new HashMap<>();
    }

    public void agregarVertice(String vertice) {
        this.vecinos.putIfAbsent(vertice, new ArrayList<>());
    }

    public void agregarArista(String origen, String destino) {
        this.agregarVertice(origen);
        this.agregarVertice(destino);
        this.vecinos.get(origen).add(destino);
    }

    public Iterable<String> vecinosDe(String vertice) {
        return this.vecinos.getOrDefault(vertice, List.of());
    }
}
```

### Qué gana

- memoria más cercana a `O(V + E)`;
- iterar vecinos de un vértice es natural;
- encaja muy bien con DFS, BFS, Dijkstra y casi todos los algoritmos clásicos;
- agregar vértices nuevos suele ser más flexible que en matriz.

### Qué paga

- consultar si `u` y `v` son adyacentes ya no es inmediato si la lista no tiene estructura extra;
- la implementación necesita más objetos o nodos;
- si el grafo es muy denso, puede perder parte de su ventaja.

### Invariantes típicos

- todo vecino listado debe corresponder a un vértice válido;
- si el grafo es no dirigido, cada arista `u-v` debería aparecer reflejada en ambas listas;
- si el grafo es simple, la lista de un vértice no debería repetir el mismo vecino.

## Lista de aristas

La **lista de aristas** guarda el conjunto de conexiones como pares o tuplas independientes.

Ejemplo:

```java
public record Arista(String origen, String destino, int peso) {}
```

y el grafo podría mantener:

```java
private final List<Arista> aristas;
```

### Cuándo sirve

Esta representación es útil cuando:

- interesa recorrer todas las aristas como colección;
- el algoritmo trabaja directamente sobre ellas;
- o el foco no está puesto en consultar vecinos todo el tiempo.

Ejemplos típicos:

- **Kruskal**, que suele ordenar aristas por peso;
- importación/exportación de grafos desde archivos;
- algoritmos donde la arista es la unidad principal de trabajo.

### Qué paga

- consultar vecinos o adyacencia puntual puede ser más caro;
- muchas preguntas del TAD requieren escanear toda la lista;
- suele necesitar estructuras auxiliares si el algoritmo quiere ir más rápido.

## Cómo representar pesos y dirección

La representación no solo decide "dónde guardo la arista", sino también **cómo codifico su información**.

### Dirección

| Tipo | Qué implica en memoria |
| :--- | :--- |
| No dirigido | una sola relación lógica, aunque a veces se guarde duplicada en listas |
| Dirigido | hay que distinguir origen y destino |

En listas de adyacencia, un grafo dirigido suele guardar solo vecinos salientes. Si además se quieren vecinos entrantes rápidos, hace falta estructura adicional.

### Peso

El peso puede guardarse:

- como entero o real dentro de la matriz;
- como parte de una arista;
- como campo del elemento almacenado en la lista de adyacencia.

Ejemplo de lista de adyacencia con peso:

```java
public record VecinoConPeso(String destino, int peso) {}
private final Map<String, List<VecinoConPeso>> vecinos;
```

La clave editorial es no esconder esta decisión: si el grafo es ponderado, la representación debe dejar claro **qué significa el peso** y dónde vive.

## Comparación rápida de representaciones

| Representación | Memoria típica | Consultar adyacencia | Iterar vecinos | Cuándo conviene |
| :--- | :--- | :--- | :--- | :--- |
| Matriz de adyacencia | O(V^2) | Muy fuerte | Más débil | Grafos densos o consultas masivas de adyacencia |
| Lista de adyacencia | O(V + E) | Intermedia | Muy fuerte | Grafos dispersos y algoritmos de recorrido |
| Lista de aristas | O(E) | Débil | Débil para vecinos, fuerte para recorrer aristas | Algoritmos centrados en aristas |

## Qué relación tiene esto con los algoritmos

Una misma idea algorítmica cambia de costo según la representación.

### DFS y BFS

En [Recorridos](recorridos.md), DFS y BFS necesitan visitar vecinos.

- con lista de adyacencia, recorrer todo el grafo suele ser `O(V + E)`;
- con matriz, la exploración suele pagar `O(V^2)` porque cada vértice revisa una fila completa.

### Dijkstra y Bellman-Ford

En [Caminos mínimos](caminos_minimos.md), también importa iterar vecinos o aristas:

- Dijkstra suele encajar mejor con listas de adyacencia;
- Bellman-Ford puede trabajar muy naturalmente con lista de aristas.

### Prim y Kruskal

En [Árboles de expansión](arboles_de_expansion.md):

- Prim suele aprovechar adyacencias por vértice;
- Kruskal suele aprovechar el conjunto total de aristas.

La conclusión no es solo "elegí una estructura". Es "elegí una estructura coherente con el tipo de algoritmo que vas a correr".

## Qué errores conviene evitar

Errores frecuentes:

1. usar matriz por costumbre aunque el grafo sea muy disperso;
2. elegir lista de adyacencia y después necesitar consultas de adyacencia masiva sin estructura extra;
3. no dejar explícito si la representación corresponde a un grafo dirigido o no dirigido;
4. guardar pesos sin aclarar su semántica;
5. exponer directamente la estructura interna y obligar al cliente a conocer la representación.

## Resumen

En grafos no hay una representación universalmente mejor. La elección depende de:

1. si el grafo es denso o disperso;
2. si importa más consultar adyacencia o recorrer vecinos;
3. si el algoritmo trabaja sobre vértices o sobre aristas;
4. y si hace falta guardar dirección, peso o ambos.

La idea fuerte del capítulo es esta: representar un grafo no es un detalle de implementación aislado. Es una decisión que condiciona complejidad, memoria y claridad algorítmica.

## Ejercicios

```{exercise}
:label: ex-parte5-representacion-grafos-mini

Justificá qué representación elegirías para un grafo muy disperso y qué elegirías para uno muy denso. Indicá en cada caso qué operación te interesa optimizar.
```

```{exercise}
:label: ex-parte5-representacion-grafos-direccion

Tomá una red de correlatividades entre materias y una red de amistades. Explicá cómo representarías cada una y qué cambiaría en la estructura al pasar de un grafo dirigido a uno no dirigido.
```

```{exercise}
:label: ex-parte5-representacion-grafos-kernel

Un algoritmo necesita consultar miles de veces si dos vértices son adyacentes, pero casi nunca recorre la lista completa de vecinos. Justificá qué representación conviene y qué costo estarías aceptando en otras operaciones.
```

## Próximo paso

Para seguir, conviene pasar a [Recorridos](recorridos.md), donde esas representaciones se usan para explorar el grafo.
