---
title: "Grafos"
subtitle: "Índice de familia en revisión"
subject: Estructuras de Datos
description: Mapa de la familia de grafos para la parte 5.
---

(parte5-grafos)=
# Grafos

Esta familia cubre estructuras donde las relaciones ya no son lineales ni jerárquicas, sino arbitrarias. El foco pasa a representación, recorridos, conectividad y optimización sobre caminos.

:::{note}
Conviene entrar a esta familia después de árboles, porque reutiliza recorridos, colas, pilas y prioridades, pero ya en un escenario más general.
:::

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

## Criterios de uso

Esta familia debería ayudar a distinguir:

1. problema de representación vs problema algorítmico,
2. recorridos generales vs algoritmos de optimización,
3. grafos dirigidos vs no dirigidos,
4. pesos uniformes vs pesos arbitrarios.

## Cierre integrador sugerido

Un buen cierre para esta familia sería justificar qué algoritmo usar para:

- recorrer una red social,
- encontrar la ruta más barata,
- planificar materias con correlatividades,
- detectar componentes aisladas.
