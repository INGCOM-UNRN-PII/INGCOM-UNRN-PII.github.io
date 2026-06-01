---
title: "Grafos"
subtitle: "Índice de familia"
subject: Estructuras de Datos
description: Mapa de la familia de grafos, con recorrido sugerido y criterios de comparación dentro de la parte 5.
---

(parte5-grafos)=
# Grafos

Esta familia representa la frontera final de las estructuras de datos clásicas. Aquí, las relaciones ya no son lineales (como en las secuencias) ni jerárquicas (como en los árboles), sino **arbitrarias**. Un grafo permite modelar cualquier sistema de entidades interconectadas: desde neuronas y mapas de ciudades hasta redes sociales y dependencias de software.

:::{note}
Conviene entrar a esta familia después de estudiar árboles, ya que los árboles son, técnicamente, un tipo especial de grafo (conexo y acíclico). Muchos algoritmos que ya conocés, como DFS o BFS, reaparecen aquí en su forma más general.
:::

## El cambio de paradigma: Relaciones arbitrarias

Pasar a grafos implica aceptar que un elemento puede estar conectado con cualquier otro, incluso consigo mismo. Esto introduce nuevos desafíos:

1. **Ciclos**: Podemos volver al punto de partida, lo que obliga a marcar qué nodos ya visitamos para no entrar en lazos infinitos.
2. **Representación**: Ya no hay un "siguiente" o un "padre" único. ¿Cómo guardamos eficientemente quién es vecino de quién?
3. **Optimización**: El problema ya no es solo encontrar algo, sino encontrar el **mejor camino** o la **conexión más barata**.

## Mapa de aprendizaje de la familia

| Orden | Página | Concepto central | Aplicación típica |
| :--- | :--- | :--- | :--- |
| 1 | [Fundamentos](fundamentos.md) | Vértices, aristas y tipos | Vocabulario base y modelado |
| 2 | [Representación](representacion.md) | Matrices vs. Listas | Decisiones de memoria y eficiencia |
| 3 | [Recorridos](recorridos.md) | DFS y BFS | Exploración y alcanzabilidad |
| 4 | [Caminos mínimos](caminos_minimos.md) | Dijkstra y Bellman-Ford | GPS, ruteo de paquetes, logística |
| 5 | [Árboles de expansión](arboles_de_expansion.md) | Prim y Kruskal | Diseño de redes de bajo costo |
| 6 | [Orden topológico](orden_topologico.md) | Dependencias y DAGs | Compiladores, gestión de proyectos |
| 7 | [Conectividad](conectividad.md) | Componentes y SCC | Análisis de comunidades, robustez de redes |

## Criterios de decisión: ¿Qué algoritmo necesito?

El éxito en esta familia depende de saber "traducir" un problema del mundo real al algoritmo correcto.

| Si el problema es... | Probablemente necesites... | ¿Por qué? |
| :--- | :--- | :--- |
| **¿Puedo llegar de A a B?** | [Recorridos (DFS/BFS)](recorridos.md) | Verifican conectividad simple. |
| **¿Cuál es la ruta más corta (sin pesos)?** | [BFS](recorridos.md) | BFS encuentra el camino con menos aristas. |
| **¿Cuál es la ruta más barata?** | [Caminos mínimos](caminos_minimos.md) | Dijkstra optimiza sobre aristas con peso. |
| **¿Cómo conecto todo al menor costo?** | [Árboles de expansión](arboles_de_expansion.md) | MST (Minimum Spanning Tree) evita ciclos y minimiza el peso total. |
| **¿En qué orden debo hacer estas tareas?** | [Orden topológico](orden_topologico.md) | Resuelve dependencias en grafos dirigidos acíclicos (DAG). |
| **¿Qué grupos están fuertemente unidos?** | [Conectividad (SCC)](conectividad.md) | Identifica sub-grafos donde todos llegan a todos. |

## Grafos en Java y el mundo real

A diferencia de las secuencias o los diccionarios, Java no provee una interfaz `Graph` estándar en su SDK básico. Esto se debe a que la implementación óptima depende demasiado del problema. En esta parte, aprenderemos a:

- Construir nuestras propias representaciones usando las colecciones que ya conocemos (`Map`, `List`, `Set`).
- Entender por qué a veces una simple matriz de enteros es mejor que una red de objetos complejos.
- Conectar la teoría con bibliotecas de producción (como JGraphT o Google Guava).

## Qué deberías dominar al finalizar

Un estudiante que recorrió esta familia con éxito debería poder:

1. Elegir entre matriz y lista de adyacencia según la densidad del grafo.
2. Ejecutar manualmente DFS y BFS sobre grafos dirigidos y no dirigidos.
3. Explicar por qué Dijkstra no funciona con pesos negativos y cuándo usar Bellman-Ford.
4. Identificar si un problema admite una solución topológica (presencia de ciclos).
5. Modelar un problema complejo (ej: ruteo de fibra óptica) como un problema de grafos.

## Próximo paso

Empezamos por los cimientos: [Fundamentos de grafos](fundamentos.md), donde definimos qué es un vértice, qué es una arista y por qué un grafo "dirigido" cambia todas las reglas del juego.
