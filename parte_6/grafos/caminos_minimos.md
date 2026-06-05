---
title: "Caminos mínimos"
subtitle: "Optimizar rutas según un costo"
subject: Estructuras de Datos
description: Cómo cambia el problema de caminos mínimos según haya pesos uniformes, pesos no negativos o pesos negativos, y qué hipótesis justifican BFS, Dijkstra y Bellman-Ford.
---

(parte6-caminos-minimos)=
# Caminos mínimos


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

Cuando el grafo tiene pesos o costos, ya no alcanza con saber si un vértice es alcanzable: hace falta decidir cuál es la **mejor ruta**. Aquí, el concepto de "mejor" suele significar el camino cuya suma de pesos de aristas sea la mínima posible.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender qué cambia entre caminos mínimos en grafos no ponderados, ponderados sin pesos negativos y casos más generales.

**Prerrequisitos.** [Recorridos](recorridos.md) y [Colas de prioridad](../secuencias/colas_prioridad.md).

**Desarrollo.** El capítulo define el concepto de relajación, presenta los algoritmos de BFS, Dijkstra y Bellman-Ford, y explica el problema de los ciclos negativos.
:::

## El alma del algoritmo: La Relajación

Casi todos los algoritmos de caminos mínimos se basan en una operación fundamental llamada **relajación**. Imaginá que mantenés una estimación `dist[v]` del camino más corto desde el origen hasta `v`.

Si descubrís una arista $u \to v$ con peso $w$, y resulta que:
$$\text{dist}[u] + w < \text{dist}[v]$$
...entonces encontraste un atajo. **Relajar** la arista significa actualizar `dist[v]` con este nuevo valor más bajo.

```text
algoritmo relajar(u, v, peso)
    si dist[u] + peso < dist[v] entonces
        dist[v] ← dist[u] + peso
        padre[v] ← u
    fin si
fin algoritmo
```

## 1. Pesos Uniformes: El poder del BFS

Como vimos en el capítulo anterior, si todas las aristas cuestan lo mismo (ej: todos los saltos valen 1), el BFS es imbatible. Su exploración por capas garantiza que la primera vez que llegamos a un nodo, lo hacemos por el camino con menos aristas, que en este caso es el más barato.

- **Costo:** $O(V + E)$
- **Limitación:** Solo sirve si no hay pesos o si son todos iguales.

## 2. Pesos No Negativos: Algoritmo de Dijkstra

Si las aristas tienen pesos variados (ej: kilómetros entre ciudades) pero todos son $\ge 0$, usamos **Dijkstra**. Es un algoritmo *voraz* (greedy) que siempre elige procesar el nodo con la menor distancia acumulada actual.

### Implementación con PriorityQueue
En Java, usamos una `PriorityQueue` para extraer siempre el nodo más cercano de forma eficiente.

```java
public void dijkstra(Grafo<V> g, V origen) {
    Map<V, Double> dist = new HashMap<>();
    PriorityQueue<NodoDistancia<V>> pq = new PriorityQueue<>();

    dist.put(origen, 0.0);
    pq.add(new NodoDistancia<>(origen, 0.0));

    while (!pq.isEmpty()) {
        NodoDistancia<V> actual = pq.poll();
        V u = actual.vertice;

        // Si ya encontramos un camino mejor antes de sacarlo de la cola, ignorar
        if (actual.distancia > dist.getOrDefault(u, Double.POSITIVE_INFINITY)) continue;

        for (Arista<V> arista : g.vecinosDe(u)) {
            double nuevaDist = dist.get(u) + arista.peso;
            if (nuevaDist < dist.getOrDefault(arista.destino, Double.POSITIVE_INFINITY)) {
                dist.put(arista.destino, nuevaDist);
                pq.add(new NodoDistancia<>(arista.destino, nuevaDist));
            }
        }
    }
}
```

- **Costo:** $O((V+E) \log V)$ usando colas de prioridad.
- **Por qué fallan los negativos:** Dijkstra asume que una vez que "cierra" un nodo, su distancia ya no puede mejorar. Un peso negativo podría permitir que un camino largo se vuelva repentinamente más corto que el actual, rompiendo la lógica voraz.

### Algoritmo de Dijkstra (Pseudocódigo)

```text
algoritmo dijkstra(grafo, origen)
    dist ← inicializarConInfinito()
    dist[origen] ← 0
    pq ← crearColaPrioridad()
    pq.agregar(origen, 0)
    
    mientras pq no este vacia hacer
        u ← pq.extraerMinimo()
        
        para cada vecino v de u con peso w hacer
            si dist[u] + w < dist[v] entonces
                dist[v] ← dist[u] + w
                pq.actualizarOAgregar(v, dist[v])
            fin si
        fin para
    fin mientras
fin algoritmo
```

## 3. Pesos Negativos: Bellman-Ford

Si el grafo tiene pesos negativos (ej: una arista que representa un "crédito" o "bonificación"), Dijkstra ya no es confiable. **Bellman-Ford** es más robusto pero más lento: simplemente relaja **todas** las aristas del grafo $|V|-1$ veces.

### Algoritmo de Bellman-Ford (Pseudocódigo)

```text
algoritmo bellmanFord(grafo, origen)
    dist ← inicializarConInfinito()
    dist[origen] ← 0
    
    repetir |V| - 1 veces hacer
        para cada arista (u, v) con peso w en el grafo hacer
            relajar(u, v, w)
        fin para
    fin repetir
    
    // Verificación de ciclos negativos
    para cada arista (u, v) con peso w hacer
        si dist[u] + w < dist[v] entonces
            error "Existe un ciclo negativo"
        fin si
    fin para
fin algoritmo
```

- **Detección de Ciclos Negativos:** Si después de $|V|-1$ pasadas todavía podemos relajar alguna arista, significa que hay un ciclo cuyo peso total es negativo. En este caso, el camino mínimo es $-\infty$ (podemos dar vueltas infinitas para bajar el costo).

## 4. Un paso más allá: El Algoritmo A*

En aplicaciones de navegación (como Google Maps o videojuegos), Dijkstra puede ser lento porque explora en todas direcciones como un círculo que se expande. 

El algoritmo **A*** mejora esto usando una **heurística**: una función que estima cuánto falta para llegar al destino. Esto "guía" la búsqueda hacia la meta, ignorando caminos que se alejan demasiado. Es, esencialmente, Dijkstra con brújula.

## Resumen comparativo

| Algoritmo | Pesos | Complejidad | Notas |
| :--- | :--- | :--- | :--- |
| **BFS** | Sin pesos / Uniformes | $O(V+E)$ | El más rápido para grafos simples. |
| **Dijkstra** | Solo $\ge 0$ | $O(E \log V)$ | Estándar para la mayoría de los casos. |
| **Bellman-Ford** | Cualquiera | $O(V \cdot E)$ | Detecta ciclos negativos. |
| **Floyd-Warshall** | Cualquiera | $O(V^3)$ | Calcula distancias entre **todos** los pares. |

## Ejercicios

```{exercise}
:label: ex-parte6-caminos-ejemplo

Dado un grafo con aristas $(A, B, 5), (B, C, 2), (A, C, 10)$, ejecutá manualmente Dijkstra desde $A$. ¿Cuál es el camino más corto a $C$? ¿Qué pasaría si la arista $(B, C)$ pesara $-10$?
```

```{exercise}
:label: ex-parte6-caminos-ciclo-negativo

Dibujá un grafo con tres nodos que contenga un ciclo de peso total negativo. Explicá por qué un algoritmo de búsqueda de camino mínimo nunca terminaría o daría un resultado erróneo en esta estructura.
```

```{exercise}
:label: ex-parte6-caminos-java

Investigá la clase `PriorityQueue` de Java. ¿Cómo harías para que ordene los elementos de menor a mayor distancia si guardás objetos personalizados?
```

## Próximo paso

A veces no buscamos el camino entre dos puntos, sino la forma más barata de conectar **todos** los puntos de una red sin ciclos. Para eso, estudiaremos los [Árboles de expansión](arboles_de_expansion.md).
