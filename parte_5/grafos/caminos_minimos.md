---
title: "Caminos mínimos"
subtitle: "Optimizar rutas según un costo"
subject: Estructuras de Datos
description: Esqueleto editorial del capítulo sobre caminos mínimos.
---

(parte5-caminos-minimos)=
# Caminos mínimos

Cuando el grafo tiene pesos o costos, ya no alcanza con saber si un vértice es alcanzable: hace falta decidir cuál es la mejor ruta. Ahí aparecen algoritmos que combinan representación, prioridad y relajación.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender qué cambia entre caminos mínimos en grafos no ponderados, ponderados sin pesos negativos y casos más generales.

**Prerrequisitos.** Conviene haber leído [Recorridos](recorridos.md) y recordar [Colas de prioridad](../secuencias/colas_prioridad.md).

**Desarrollo.** El capítulo debería contrastar BFS, Dijkstra y Bellman-Ford, explicitando en qué hipótesis se apoya cada uno.
:::

## Qué preguntas resuelve esta familia de algoritmos

Este bloque debería cubrir:

- costo de un camino,
- mejor ruta desde un origen,
- diferencia entre pesos uniformes y arbitrarios,
- noción de relajación de aristas.

## Qué algoritmos deberían aparecer

Este bloque debería cubrir:

- BFS para pesos uniformes,
- Dijkstra,
- Bellman-Ford como ampliación,
- mención de all-pairs como panorama adicional.

## Resumen

El capítulo debería dejar clara una idea: “camino mínimo” no nombra un único algoritmo, sino una familia de soluciones cuyo criterio cambia según el modelo de pesos del problema.

## Ejercicios

```{exercise}
:label: ex-parte5-caminos-minimos-mini

Indicá en qué contexto BFS alcanza para caminos mínimos y en qué contexto hace falta Dijkstra. Justificá qué dato del problema cambia entre ambos casos.
```

## Próximo paso

Para seguir, conviene pasar a [Árboles de expansión](arboles_de_expansion.md), donde la pregunta deja de ser una ruta y pasa a ser una red completa de conexión mínima.
