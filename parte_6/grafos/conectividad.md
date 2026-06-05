---
title: "Conectividad"
subtitle: "Componentes, partición y robustez"
subject: Estructuras de Datos
description: Cómo decidir qué vértices pertenecen a la misma componente y qué cambia cuando el grafo es dirigido o exige conectividad fuerte.
---

(parte6-conectividad)=
# Conectividad


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

La conectividad es el estudio de la "cohesión" de un grafo. ¿Está todo el grafo en una sola pieza o está fragmentado en varias islas? ¿Qué tan vulnerable es la red si un nodo falla? Responder estas preguntas es vital para entender la robustez de cualquier sistema, desde Internet hasta una red de transporte.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender conectividad en grafos dirigidos y no dirigidos, y vincularla con componentes y partición del problema.

**Prerrequisitos.** [Recorridos](recorridos.md) y [Conjuntos disjuntos](../diccionarios/conjuntos_disjuntos.md).

**Desarrollo.** El capítulo distingue conectividad débil y fuerte, introduce los conceptos de puentes y puntos de articulación, y explica el algoritmo de Kosaraju para SCC.
:::

## 1. Grafos No Dirigidos: Componentes Conexas

En un grafo no dirigido, el concepto es simple: una **Componente Conexa** es un grupo de nodos donde todos pueden llegar a todos. Si el grafo tiene más de una componente, decimos que está fragmentado.

### Cómo detectarlas
Simplemente corremos un recorrido (DFS o BFS). Todos los nodos alcanzados desde un origen forman una componente. Si quedan nodos sin visitar, elegimos uno y repetimos.

```java
int contarComponentes(Grafo<V> g) {
    Set<V> visitados = new HashSet<>();
    int contador = 0;
    for (V v : g.obtenerVertices()) {
        if (!visitados.contains(v)) {
            contador++;
            explorarDFS(v, visitados); // Marcamos toda la "isla"
        }
    }
    return contador;
}
```

## 2. Grafos Dirigidos: La asimetría del camino

En los digrafos (grafos dirigidos), la conectividad se vuelve más compleja porque las calles pueden ser de una sola mano.

- **Conectividad Débil:** El grafo está conectado si ignoramos la dirección de las flechas.
- **Conectividad Fuerte (SCC):** Un grupo de nodos es una **Componente Fuertemente Conexa** si existe un camino de ida y vuelta entre **cualquier** par de nodos del grupo.

### Algoritmo de Kosaraju (Simplificado)
Para encontrar las SCC, no alcanza con un DFS común. Una técnica elegante es:
1. Hacer un DFS y guardar el orden de finalización de los nodos.
2. Invertir todas las flechas del grafo (Grafo Transpuesto).
3. Hacer un segundo DFS sobre el grafo invertido, siguiendo el orden de finalización del paso 1. Cada vez que iniciamos un DFS en esta fase, descubrimos una SCC completa.

## 3. Robustez: Puentes y Puntos de Articulación

No todos los nodos y aristas son igual de importantes. 

- **Punto de Articulación:** Un nodo cuya eliminación fragmenta el grafo en dos o más piezas. Es un "punto crítico de falla".
- **Puente:** Una arista cuya eliminación fragmenta el grafo.

:::{tip} Aplicación en Redes
En diseño de infraestructura, queremos evitar puntos de articulación y puentes. Una red robusta debería tener múltiples caminos alternativos para que la falla de un solo componente no aísle a nadie.
:::

## 4. Conectividad y Union-Find

Como vimos en el capítulo de [Árboles de Expansión](arboles_de_expansion.md), la estructura **Union-Find** es ideal para gestionar la conectividad de forma dinámica. Si el grafo está cambiando (se agregan aristas), Union-Find nos dice en tiempo casi constante si dos nodos acaban de quedar conectados en la misma componente.

## Resumen

1. **Componentes Conexas:** Las "islas" de un grafo no dirigido.
2. **Fuertemente Conexo (SCC):** Regiones de un digrafo con caminos de ida y vuelta.
3. **Robustez:** Se mide identificando puntos de articulación y puentes.
4. **Kosaraju:** Algoritmo clásico para encontrar comunidades fuertemente unidas en digrafos.

## Ejercicios

```{exercise}
:label: ex-parte6-conectividad-ejemplo

Dibujá un grafo dirigido con 4 nodos $A, B, C, D$ y las aristas $A \to B, B \to C, C \to A, C \to D$. ¿Cuáles son las Componentes Fuertemente Conexas?
```

```{exercise}
:label: ex-parte6-conectividad-puente

En una red de fibra óptica, ¿por qué es peligroso que exista un "Puente"? ¿Cómo podrías modificar el grafo para que esa arista deje de ser un puente?
```

```{exercise}
:label: ex-parte6-conectividad-tarjan

Investigá el **Algoritmo de Tarjan**. ¿En qué se diferencia del de Kosaraju para encontrar SCC? ¿Por qué se dice que es más eficiente en una sola pasada?
```

## Próximo paso

¡Felicitaciones! Has completado el recorrido por la familia de Grafos. Ahora es momento de hacer una [revisión cruzada](p6-revision-cruzada) de toda la Parte 6 para asegurar que los conceptos de ADT, Secuencias, Diccionarios, Árboles y Grafos estén bien integrados.
