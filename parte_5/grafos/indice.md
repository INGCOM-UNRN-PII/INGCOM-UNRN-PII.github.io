---
title: "Grafos"
subtitle: "Índice de familia en revisión"
subject: Estructuras de Datos
description: Mapa de la familia de grafos, con estado editorial actual y recorrido sugerido dentro de la parte 5.
---

(parte5-grafos)=
# Grafos

Esta familia cubre estructuras donde las relaciones ya no son lineales ni jerárquicas, sino arbitrarias. El foco pasa a representación, recorridos, conectividad y optimización sobre caminos.

:::{note}
Conviene entrar a esta familia después de árboles, porque reutiliza recorridos, colas, pilas y prioridades, pero ya en un escenario más general.
:::

## Estado editorial actual

La familia sigue **en revisión**, pero ya no está toda en el mismo estado.

| Página | Estado actual | Rol |
| :--- | :--- | :--- |
| [Fundamentos de grafos](fundamentos.md) | Desarrollo base disponible | Fija el vocabulario mínimo de vértices, aristas, caminos y conectividad |
| [Representación de grafos](representacion.md) | Desarrollo base disponible | Compara matrices, listas y aristas |
| [Recorridos](recorridos.md) | Desarrollo base disponible | Instala DFS y BFS |
| [Caminos mínimos](caminos_minimos.md) | Desarrollo base disponible | Introduce optimización sobre caminos |
| [Árboles de expansión](arboles_de_expansion.md) | Desarrollo base disponible | Trabaja conectividad con costo mínimo |
| [Orden topológico](orden_topologico.md) y [Conectividad](conectividad.md) | Esqueleto editorial | Cierra con dependencias, componentes y particiones |

## Recorrido sugerido

| Orden | Página | Rol en la familia |
| :--- | :--- | :--- |
| 1 | [Fundamentos de grafos](fundamentos.md) | Define vértices, aristas y tipos de grafo |
| 2 | [Representación de grafos](representacion.md) | Compara matrices, listas y costo de acceso |
| 3 | [Recorridos](recorridos.md) | Instala DFS y BFS como base algorítmica |
| 4 | [Caminos mínimos](caminos_minimos.md) | Introduce optimización sobre caminos |
| 5 | [Árboles de expansión](arboles_de_expansion.md) | Trabaja conectividad óptima |
| 6 | [Orden topológico](orden_topologico.md) y [Conectividad](conectividad.md) | Cierra con dependencias y componentes |

## Comparación rápida

| Tema | Pregunta central | Herramienta dominante | Cuándo aparece |
| :--- | :--- | :--- | :--- |
| [Representación](representacion.md) | ¿Cómo guardar el grafo? | Matriz o lista de adyacencia | Antes de programar cualquier algoritmo |
| [Recorridos](recorridos.md) | ¿Qué vértices son alcanzables? | DFS y BFS | En exploración y conectividad básica |
| [Caminos mínimos](caminos_minimos.md) | ¿Cuál es la mejor ruta? | BFS, Dijkstra, Bellman-Ford | En navegación y ruteo |
| [Árboles de expansión](arboles_de_expansion.md) | ¿Cómo conectar con costo mínimo? | Prim o Kruskal | En diseño de redes |
| [Orden topológico](orden_topologico.md) | ¿Qué dependencia va antes? | DFS o Kahn | En DAG y planificación |
| [Conectividad](conectividad.md) | ¿Cómo se parte el grafo? | DFS, BFS, SCC | En análisis de componentes |

## Qué ya conviene leer y qué todavía funciona como mapa

Hoy la lectura más útil de esta familia es:

1. leer en bloque [Fundamentos de grafos](fundamentos.md), [Representación de grafos](representacion.md), [Recorridos](recorridos.md), [Caminos mínimos](caminos_minimos.md) y [Árboles de expansión](arboles_de_expansion.md), porque ya fijan el núcleo conceptual, de representación y de optimización básica de la familia;
2. usar el resto del índice como hoja de ruta para dependencias y partición;
3. volver a este mapa a medida que cada capítulo deje de ser esqueleto y pase a contenido pleno.

## Criterios de uso

Esta familia debería ayudar a distinguir:

1. problema de representación vs problema algorítmico,
2. recorridos generales vs algoritmos de optimización,
3. grafos dirigidos vs no dirigidos,
4. pesos uniformes vs pesos arbitrarios.

## Decisión rápida

Si el problema todavía está mal encuadrado, conviene decidir en este orden:

1. si el dominio realmente necesita un **grafo** o si alcanza con una secuencia o un árbol;
2. si las relaciones son **dirigidas** o **no dirigidas**;
3. si el costo importante está en **representar**, **recorrer** u **optimizar**;
4. si el siguiente capítulo a leer debería ser [Representación de grafos](representacion.md) o [Recorridos](recorridos.md).

## Cierre integrador sugerido

Un buen cierre para esta familia sería justificar qué algoritmo usar para:

- recorrer una red social,
- encontrar la ruta más barata,
- planificar materias con correlatividades,
- detectar componentes aisladas.

## Próximo paso

El siguiente paso natural es [Representación de grafos](representacion.md): ahí el lenguaje abstracto de vértices y aristas se convierte en decisiones concretas sobre memoria, adyacencia y recorrido.
