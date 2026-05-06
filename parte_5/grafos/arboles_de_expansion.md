---
title: "Árboles de expansión"
subtitle: "Conectar todo con costo mínimo"
subject: Estructuras de Datos
description: Qué problema resuelve un árbol de expansión mínima y cómo contrastar Prim y Kruskal para conectar un grafo ponderado con costo total mínimo.
---

(parte5-arboles-expansion)=
# Árboles de expansión

Los árboles de expansión mínima cambian el foco una vez más: ya no interesa una ruta óptima entre dos vértices, sino una estructura global que conecte todo el grafo con costo total mínimo.

Eso obliga a separar dos preguntas que suelen mezclarse:

- **¿cuál es la mejor ruta entre A y B?**
- **¿cómo conecto todos los vértices pagando lo menos posible en total?**

Aunque ambas usen grafos ponderados, no son el mismo problema. En caminos mínimos se optimiza una ruta. En MST se optimiza una red completa.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender qué es un spanning tree mínimo y por qué Prim y Kruskal resuelven el mismo problema con estrategias distintas.

**Prerrequisitos.** Conviene haber leído [Caminos mínimos](caminos_minimos.md) y recordar [Conjuntos disjuntos](../diccionarios/conjuntos_disjuntos.md).

**Desarrollo.** El capítulo define árbol de expansión y MST, distingue este problema de caminos mínimos, compara Prim y Kruskal y cierra mostrando cuándo conviene cada estrategia y qué estructuras auxiliares reutilizan.
:::

:::{tip} Idea guía
Un MST no busca una ruta óptima. Busca una **infraestructura mínima** que deje conectado todo lo que hace falta conectar.
:::

## Qué define a un árbol de expansión mínima

Supongamos un grafo no dirigido, conexo y ponderado.

Un **árbol de expansión** (*spanning tree*) es un subconjunto de sus aristas tal que:

1. incluye todos los vértices;
2. mantiene el grafo conectado;
3. no tiene ciclos.

Como es un árbol sobre `V` vértices, tendrá exactamente `V - 1` aristas.

Un **árbol de expansión mínima** (*minimum spanning tree*, MST) es el árbol de expansión cuyo **costo total** es mínimo entre todos los posibles.

### Cuándo tiene sentido el problema

El problema clásico de MST se formula sobre:

- grafos no dirigidos,
- ponderados,
- y normalmente conexos.

Si el grafo no es conexo, ya no hay un único árbol que cubra todo. Lo que aparece es un **bosque de expansión mínima**, una por cada componente.

### Ejemplos típicos

| Dominio | Qué representan los pesos |
| :--- | :--- |
| Tendido de fibra o cableado | costo de conexión |
| Red eléctrica | costo de tender líneas |
| Conectar sucursales | distancia o presupuesto |
| Diseño de infraestructura | costo total de unión entre puntos |

El rasgo común es este: no importa una ruta particular entre dos puntos, sino minimizar el costo global de dejar toda la red conectada.

## Por qué no es lo mismo que camino mínimo

Esta distinción es obligatoria.

| Problema | Qué optimiza | Qué devuelve |
| :--- | :--- | :--- |
| Camino mínimo | costo de una ruta entre origen y destino | un camino |
| MST | costo total de conexión de toda la red | un árbol |

Un MST puede contener caminos entre vértices que **no** sean mínimos en forma individual. Y un conjunto de caminos mínimos desde un origen tampoco forma necesariamente un MST.

La pregunta correcta entonces no es "¿qué algoritmo ponderado uso?", sino "¿quiero optimizar una ruta o una red completa?".

## Prim: crecer desde un árbol parcial

**Prim** construye el MST agregando de a una arista que conecta el árbol parcial actual con un vértice todavía no incorporado.

La intuición es:

1. empezás desde un vértice cualquiera;
2. mirás todas las aristas que salen del árbol parcial;
3. elegís la más barata que agregue un vértice nuevo;
4. repetís hasta cubrir todos los vértices.

La estructura natural para esa elección es una **cola de prioridad** sobre aristas candidatas.

```java
public void prim(GrafoPesado<String> grafo, String origen) {
    Set<String> enArbol = new HashSet<>();
    PriorityQueue<AristaPesada<String>> candidatas = new PriorityQueue<>();

    enArbol.add(origen);
    for (AristaPesada<String> arista : grafo.aristasDesde(origen)) {
        candidatas.add(arista);
    }

    while (!candidatas.isEmpty()) {
        AristaPesada<String> mejor = candidatas.remove();
        if (enArbol.contains(mejor.destino())) {
            continue;
        }

        enArbol.add(mejor.destino());

        for (AristaPesada<String> arista : grafo.aristasDesde(mejor.destino())) {
            if (!enArbol.contains(arista.destino())) {
                candidatas.add(arista);
            }
        }
    }
}
```

### Qué caracteriza a Prim

- trabaja "desde adentro" del árbol parcial;
- se apoya muy bien en listas de adyacencia;
- usa una cola de prioridad como estructura clave;
- su lógica recuerda a Dijkstra en la mecánica, pero no resuelve el mismo problema.

## Kruskal: ordenar aristas globalmente

**Kruskal** cambia completamente la estrategia:

1. ordena todas las aristas por peso;
2. va tomando la más barata disponible;
3. la agrega solo si no forma ciclo con lo ya elegido.

La dificultad ya no está en elegir la mejor frontera desde un árbol parcial, sino en responder rápido:

- "¿esta arista une dos componentes distintas o cerraría un ciclo?"

Ahí aparece con fuerza el TAD de [Conjuntos disjuntos](../diccionarios/conjuntos_disjuntos.md).

```java
public List<AristaPesada<String>> kruskal(List<AristaPesada<String>> aristas, UnionFind<String> uf) {
    List<AristaPesada<String>> resultado = new ArrayList<>();
    aristas.sort(Comparator.comparingInt(AristaPesada::peso));

    for (AristaPesada<String> arista : aristas) {
        String u = arista.origen();
        String v = arista.destino();

        if (!uf.find(u).equals(uf.find(v))) {
            resultado.add(arista);
            uf.union(u, v);
        }
    }

    return resultado;
}
```

### Qué caracteriza a Kruskal

- piensa globalmente sobre el conjunto de aristas;
- necesita detectar componentes o ciclos de forma eficiente;
- encaja muy bien cuando el foco está puesto en la lista total de aristas;
- reutiliza de manera natural union-find.

## Comparación entre Prim y Kruskal

| Algoritmo | Intuición | Estructura clave | Suele encajar mejor cuando... |
| :--- | :--- | :--- | :--- |
| Prim | crecer desde un árbol parcial | cola de prioridad | hay buena estructura de adyacencia |
| Kruskal | elegir aristas globalmente sin formar ciclos | conjuntos disjuntos | es natural trabajar con lista de aristas |

Ambos resuelven el mismo problema, pero organizan la información de manera distinta.

## Costos y relación con la representación

Como ya pasó en otros capítulos, el costo real depende también de cómo esté guardado el grafo.

### Prim

Con lista de adyacencia y cola de prioridad:

- suele analizarse alrededor de `O(E log V)` según implementación.

### Kruskal

Su costo suele quedar dominado por:

- ordenar aristas;
- más el costo casi constante amortizado de union-find.

Por eso se lo suele expresar alrededor de:

- `O(E log E)` o formas equivalentes.

La representación influye en qué información está más disponible:

- si ya tenés lista de adyacencia, Prim suele fluir mejor;
- si ya tenés lista de aristas, Kruskal suele quedar más natural.

## Cortes, ciclos y por qué funcionan

No hace falta demostrar toda la teoría formal, pero sí conviene entender la intuición de corrección.

### Idea detrás de Prim

En cada paso, tomar la arista más barata que cruza desde el árbol parcial hacia afuera es una decisión segura porque existe algún MST que puede incluir esa arista.

### Idea detrás de Kruskal

Tomar la arista global más barata que no forme ciclo también es seguro, porque evita agregar costo innecesario y preserva la posibilidad de completar un árbol válido.

Las dos estrategias explotan propiedades estructurales de cortes y ciclos, aunque las miren desde ángulos distintos.

## Qué errores conviene evitar

Errores frecuentes:

1. usar MST cuando el problema real era una ruta mínima entre dos vértices;
2. olvidar que el problema clásico de MST supone grafo no dirigido;
3. aplicar Kruskal sin una estructura que detecte ciclos eficientemente;
4. creer que el MST garantiza camino mínimo entre todos los pares;
5. no distinguir entre árbol de expansión y bosque de expansión cuando el grafo no es conexo.

## Resumen

Aunque caminos mínimos y MST usan grafos ponderados, resuelven preguntas distintas y por eso necesitan criterios algorítmicos distintos.

Las ideas que deberían quedar instaladas son estas:

1. un MST minimiza costo total de conexión, no rutas individuales;
2. Prim crece un árbol parcial apoyándose en prioridad;
3. Kruskal elige aristas globalmente apoyándose en conjuntos disjuntos;
4. la estructura de representación vuelve a influir sobre qué algoritmo resulta más natural.

## Ejercicios

```{exercise}
:label: ex-parte5-arboles-expansion-mini

Explicá por qué un árbol de expansión mínima no reemplaza a un algoritmo de camino mínimo, aun cuando ambos trabajen sobre el mismo grafo ponderado.
```

```{exercise}
:label: ex-parte5-arboles-expansion-prim-kruskal

Compará Prim y Kruskal para una red de ciudades. Explicá qué estructura auxiliar domina en cada algoritmo y por qué uno piensa en fronteras del árbol mientras el otro piensa en componentes.
```

```{exercise}
:label: ex-parte5-arboles-expansion-bosque

Describí qué pasaría si intentaras calcular un MST sobre un grafo no conexo. Explicá por qué el resultado correcto ya no es un árbol único y cómo debería reinterpretarse el problema.
```

## Próximo paso

Para seguir, conviene pasar a [Orden topológico](orden_topologico.md), donde el problema pasa a ser de dependencias y precedencias.
