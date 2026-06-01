---
title: "Representación de grafos"
subtitle: "Matriz, lista y costo de acceso"
subject: Estructuras de Datos
description: Cómo comparar matrices, listas de adyacencia y listas de aristas según memoria, consulta de adyacencia e iteración de vecinos.
---

(parte6-representacion-grafos)=
# Representación de grafos

En grafos, la representación influye tanto como el algoritmo. Antes de correr BFS, Dijkstra o Prim, hace falta decidir cómo se almacenan vértices y aristas y qué consultas conviene optimizar.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Comparar las representaciones más comunes de grafos y entender cómo afectan memoria, consulta de adyacencia e iteración de vecinos.

**Prerrequisitos.** Conviene haber leído [Fundamentos de grafos](fundamentos.md).

**Desarrollo.** El capítulo contrasta matriz de adyacencia, lista de adyacencia y lista de aristas, explica cómo codificar dirección y peso, y cierra comparando sus costos según densidad del grafo y tipo de algoritmo.
:::

## 1. Matriz de Adyacencia

La **matriz de adyacencia** es la forma más directa: una tabla cuadrada de $V \times V$ donde el valor en la celda $(i, j)$ indica si hay una arista entre el vértice $i$ y el $j$.

### Implementación en Java
Si los vértices se identifican con enteros de $0$ a $n-1$:

```java
public class GrafoMatriz {
    private final double[][] matriz;
    private final boolean dirigido;

    public GrafoMatriz(int n, boolean dirigido) {
        this.matriz = new double[n][n];
        this.dirigido = dirigido;
        // Inicializar con infinito para denotar "sin conexión"
        for(int i=0; i<n; i++) Arrays.fill(matriz[i], Double.POSITIVE_INFINITY);
    }

    public void conectar(int u, int v, double peso) {
        matriz[u][v] = peso;
        if (!dirigido) matriz[v][u] = peso;
    }

    public boolean sonAdyacentes(int u, int v) {
        return matriz[u][v] != Double.POSITIVE_INFINITY;
    }
}
```

- **Fortaleza:** Consultar si $A$ y $B$ están conectados es instantáneo ($O(1)$).
- **Debilidad:** Consume $O(V^2)$ de memoria siempre, incluso si no hay aristas. Además, listar los vecinos de un nodo exige recorrer toda su fila, costando $O(V)$.

## 2. Lista de Adyacencia

Es la representación estándar para la mayoría de los algoritmos. Para cada vértice, guardamos una lista de sus vecinos.

### Implementación en Java
Es común usar un `Map` para asociar nombres de vértices con sus listas de vecinos, lo que nos da flexibilidad si los IDs no son enteros contiguos.

```java
public class GrafoLista<V> {
    private final Map<V, List<Arista<V>>> adj = new HashMap<>();

    public void conectar(V u, V v, double peso) {
        adj.computeIfAbsent(u, k -> new ArrayList<>()).add(new Arista<>(v, peso));
    }

    public List<Arista<V>> vecinosDe(V u) {
        return adj.getOrDefault(u, Collections.emptyList());
    }
}

record Arista<V>(V destino, double peso) {}
```

- **Fortaleza:** Muy eficiente en memoria para grafos dispersos ($O(V + E)$). Iterar sobre los vecinos es muy rápido: solo recorremos los que realmente existen.
- **Debilidad:** Consultar si $A$ y $B$ son adyacentes requiere buscar en la lista de $A$, lo que puede costar $O(\text{grado}(A))$.

## 3. Lista de Aristas

Simplemente una colección (arreglo o lista) de todas las conexiones. Es útil cuando el algoritmo no necesita navegar de nodo en nodo, sino procesar todos los vínculos globalmente.

- **Uso típico:** Algoritmo de Kruskal (para Árboles de Expansión Mínima), donde necesitamos ordenar todas las aristas por peso al principio.

## 4. El enfoque "Orientado a Objetos" (Red de Nodos)

A veces, por inercia de OOP, se intenta representar el grafo como una red de objetos `Nodo` que contienen punteros a otros `Nodo`.

```java
class Nodo {
    String nombre;
    List<Nodo> vecinos;
}
```

**Cuidado:** Aunque parece intuitivo, este enfoque suele ser **problemático**:
1. **Dificultad de gestión:** ¿Quién es el dueño del grafo? ¿Cómo iteramos sobre "todos los nodos" si alguno quedó desconectado?
2. **Algoritmos:** La mayoría de los algoritmos de grafos (DFS, BFS, Dijkstra) necesitan marcar nodos como "visitados". Si el estado de visitado vive dentro del objeto `Nodo`, no podemos correr dos algoritmos en paralelo sin limpiarlos.
3. **Persistencia:** Es mucho más difícil de serializar y depurar que una simple lista o matriz.

:::{tip} Recomendación de la cátedra
Preferí siempre separar los **datos** de la **estructura**. El grafo debería ser un objeto que gestiona las conexiones, y los algoritmos deberían mantener su propio estado (como un `Set<V> visitados`) por fuera de los nodos.
:::

## Cuadro Comparativo de Complejidad

| Operación | Matriz de Adyacencia | Lista de Adyacencia |
| :--- | :--- | :--- |
| **Espacio en Memoria** | $O(V^2)$ | $O(V + E)$ |
| **Consultar Adyacencia $(u, v)$** | $O(1)$ | $O(\text{grado}(u))$ |
| **Listar Vecinos de $u$** | $O(V)$ | $O(\text{grado}(u))$ |
| **Insertar Arista** | $O(1)$ | $O(1)$ |
| **Insertar Vértice** | $O(V^2)$ (copia de matriz) | $O(1)$ |

## Grafos Implícitos

No siempre hace falta "guardar" el grafo en memoria. A veces el grafo está definido por las reglas de un problema. 

- **Ejemplo:** Un tablero de ajedrez. No guardamos un millón de aristas entre casilleros. La función `obtenerVecinos(casillero)` simplemente calcula los movimientos legales del caballo en ese instante.

Esto se llama **grafo implícito** y es fundamental en inteligencia artificial y resolución de puzzles.

## Resumen

1. Si el grafo es **denso** (muchas aristas) o necesitás consultas de adyacencia constantes: **Matriz**.
2. Si el grafo es **disperso** (pocas aristas) o vas a recorrerlo mucho: **Lista de Adyacencia**.
3. Si el algoritmo trabaja con aristas globales: **Lista de Aristas**.
4. Evitá meter lógica de algoritmos (como "visitado") dentro de los objetos del dominio.

## Ejercicios

```{exercise}
:label: ex-parte6-representacion-memoria

Calculá cuánta memoria ocuparía una matriz de adyacencia de booleanos para un grafo de 50.000 usuarios de una red social. ¿Es viable en una PC estándar? ¿Cambiaría tu respuesta si usás una lista de adyacencia donde cada usuario tiene en promedio 200 amigos?
```

```{exercise}
:label: ex-parte6-representacion-incidencia

Investigá qué es una **Matriz de Incidencia**. ¿En qué se diferencia de la de adyacencia? ¿Para qué tipo de problemas creés que podría ser útil?
```

```{exercise}
:label: ex-parte6-representacion-implícito

Pensá en el juego del Sudoku como un grafo. ¿Cuáles serían los vértices? ¿Cuándo habría una arista entre dos celdas? ¿Convendría guardarlo explícitamente o tratarlo como un grafo implícito?
```

## Próximo paso

Ahora que sabemos cómo guardar el grafo, vamos a aprender a movernos por él: [Recorridos de grafos](recorridos.md), el corazón de la exploración algorítmica.
