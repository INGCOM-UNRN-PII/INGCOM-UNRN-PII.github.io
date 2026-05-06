---
title: "Árboles de expansión"
subtitle: "Conectar todo con costo mínimo"
subject: Estructuras de Datos
description: Esqueleto editorial del capítulo sobre árboles de expansión.
---

(parte5-arboles-expansion)=
# Árboles de expansión

Los árboles de expansión mínima cambian el foco una vez más: ya no interesa una ruta óptima entre dos vértices, sino una estructura global que conecte todo el grafo con costo total mínimo.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender qué es un spanning tree mínimo y por qué Prim y Kruskal resuelven el mismo problema con estrategias distintas.

**Prerrequisitos.** Conviene haber leído [Caminos mínimos](caminos_minimos.md) y recordar [Conjuntos disjuntos](../diccionarios/conjuntos_disjuntos.md).

**Desarrollo.** El capítulo debería presentar el concepto de árbol de expansión, comparar Prim y Kruskal y destacar el papel de colas de prioridad y estructuras de partición.
:::

## Qué problema resuelve un MST

Este bloque debería cubrir:

- spanning tree,
- costo total de conexión,
- diferencia con camino mínimo,
- criterio de optimalidad global.

## Qué algoritmos y apoyos aparecen

Este bloque debería cubrir:

- Prim,
- Kruskal,
- uso de colas de prioridad,
- uso de conjuntos disjuntos.

## Resumen

El capítulo debería dejar instalada una idea: aunque caminos mínimos y MST usan grafos ponderados, resuelven preguntas distintas y por eso necesitan criterios algorítmicos distintos.

## Ejercicios

```{exercise}
:label: ex-parte5-arboles-expansion-mini

Explicá por qué un árbol de expansión mínima no reemplaza a un algoritmo de camino mínimo, aun cuando ambos trabajen sobre el mismo grafo ponderado.
```

## Próximo paso

Para seguir, conviene pasar a [Orden topológico](orden_topologico.md), donde el problema pasa a ser de dependencias y precedencias.
