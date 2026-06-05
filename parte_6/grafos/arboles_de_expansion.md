---
title: "Árboles de expansión"
subtitle: "Conectar todo con costo mínimo"
subject: Estructuras de Datos
description: Qué problema resuelve un árbol de expansión mínima y cómo contrastar Prim y Kruskal para conectar un grafo ponderado con costo total mínimo.
---

(parte6-arboles-expansion)=
# Árboles de expansión


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

A veces el problema no es llegar rápido de un punto A a un punto B, sino lograr que **todos** los puntos de una red estén comunicados gastando lo menos posible. Esto es lo que llamamos un **Árbol de Expansión Mínima** (MST - Minimum Spanning Tree).

Imaginá que tenés que conectar varias computadoras con cables de fibra óptica. No importa si para ir de la PC 1 a la PC 4 hay que pasar por la 2 y la 3; lo que importa es que el **costo total de cable** sea el mínimo posible.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender qué es un spanning tree mínimo y por qué Prim y Kruskal resuelven el mismo problema con estrategias distintas.

**Prerrequisitos.** [Caminos mínimos](caminos_minimos.md) y [Conjuntos disjuntos](../diccionarios/conjuntos_disjuntos.md).

**Desarrollo.** El capítulo define el MST, lo contrasta con los caminos mínimos y profundiza en los algoritmos de Prim y Kruskal.
:::

## Definición y Propiedades

Un **Árbol de Expansión** de un grafo $G$ es un subgrafo que:
1. Contiene a **todos** los vértices de $G$.
2. Es un **árbol** (conexo y sin ciclos).

Si el grafo tiene $V$ vértices, el árbol de expansión tendrá exactamente $V-1$ aristas. El **MST** es aquel cuya suma de pesos de las aristas sea la mínima de entre todos los árboles de expansión posibles.

:::{important} MST vs. Caminos Mínimos
Es un error común pensar que un MST te da los caminos más cortos entre nodos. 
- **Dijkstra** minimiza la distancia desde **un origen**.
- **MST** minimiza el **peso total** de la estructura de conexión.
Un MST puede tener caminos entre nodos individuales que son mucho más largos que el camino más corto que encontraría Dijkstra.
:::

## La Propiedad del Corte (Cut Property)

Ambos algoritmos que veremos (Prim y Kruskal) se basan en una verdad fundamental de la teoría de grafos:
> Si dividimos los nodos del grafo en dos grupos (un **corte**), la arista más barata que cruza de un grupo al otro **debe** formar parte del MST.

## 1. Algoritmo de Prim: Crecimiento Local

Prim funciona de forma muy parecida a Dijkstra. Empieza en un nodo cualquiera y va "conquistando" al vecino más cercano que todavía no esté en el árbol.

### Algoritmo de Prim (Pseudocódigo)

```text
algoritmo prim(grafo, inicio)
    visitados ← crearConjuntoVacio()
    pq ← crearColaPrioridad()
    mst ← crearListaVacia()
    
    visitados.add(inicio)
    para cada vecino v de inicio con peso w hacer
        pq.agregar(arista(inicio, v), w)
    fin para
    
    mientras pq no este vacia hacer
        mejorArista ← pq.extraerMinimo()
        v ← mejorArista.destino
        
        si v no esta en visitados entonces
            visitados.add(v)
            mst.agregar(mejorArista)
            
            para cada vecino n de v con peso w hacer
                si n no esta en visitados entonces
                    pq.agregar(arista(v, n), w)
                fin si
            fin para
        fin si
    fin mientras
    
    devolver mst
fin algoritmo
```

## 2. Algoritmo de Kruskal: Selección Global

Kruskal ignora la estructura del árbol hasta el final. Mira todas las aristas del grafo, las ordena de menor a mayor peso, y las va agregando siempre y cuando **no formen un ciclo**.

Para detectar ciclos de forma eficiente, Kruskal es el "mejor amigo" de la estructura **Union-Find**.

### Algoritmo de Kruskal (Pseudocódigo)

```text
algoritmo kruskal(grafo)
    mst ← crearListaVacia()
    uf ← crearUnionFind(grafo.vertices)
    aristas ← grafo.todasLasAristas()
    
    aristas.ordenarPorPesoCreciente()
    
    para cada arista (u, v) en aristas hacer
        si uf.find(u) != uf.find(v) entonces
            mst.agregar(arista(u, v))
            uf.union(u, v)
        fin si
    fin para
    
    devolver mst
fin algoritmo
```

- **Estrategia:** Agrega la arista más barata del grafo que conecte dos componentes que antes estaban separadas.
- **Cuándo conviene:** En grafos **dispersos** (pocas aristas) porque el costo dominante es ordenar las aristas.

## Comparativa Final

| Algoritmo | Enfoque | Estructura Auxiliar | Complejidad |
| :--- | :--- | :--- | :--- |
| **Prim** | Nodos (Local) | Priority Queue | $O(E \log V)$ |
| **Kruskal** | Aristas (Global) | Union-Find | $O(E \log E)$ |

## Resumen

1. Un **MST** conecta todos los puntos con el mínimo costo total.
2. **Prim** es como una mancha de aceite que se expande por la arista más barata.
3. **Kruskal** es como unir puntos al azar, empezando por los más cercanos, cuidando no cerrar círculos.
4. El MST es fundamental en el diseño de redes, circuitos y sistemas de transporte.

## Ejercicios

```{exercise}
:label: ex-parte6-expansion-ejemplo

Dado un triángulo de nodos $A, B, C$ con pesos $AB=10, BC=1, AC=10$. ¿Cuál es el MST? ¿Cuál es el camino más corto de $A$ a $C$? ¿Coinciden?
```

```{exercise}
:label: ex-parte6-expansion-ciclos

En el algoritmo de Kruskal, ¿qué pasaría si no usáramos Union-Find? ¿Cómo detectarías si una arista forma un ciclo? ¿Qué complejidad tendría esa detección comparada con Union-Find?
```

```{exercise}
:label: ex-parte6-expansion-unicidad

Si todas las aristas de un grafo tienen pesos distintos, ¿es el MST único? ¿Y si hay aristas con el mismo peso? Justificá con un ejemplo pequeño.
```

## Próximo paso

A veces las aristas no representan distancias, sino **precedencias** (ej: para hacer la tarea B, primero hay que terminar la A). Para organizar estos problemas, estudiaremos el [Orden topológico](orden_topologico.md).
