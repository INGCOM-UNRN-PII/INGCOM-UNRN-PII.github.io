---
title: "Recorridos"
subtitle: "DFS, BFS y exploración sistemática"
subject: Estructuras de Datos
description: Esqueleto editorial del capítulo sobre recorridos de grafos.
---

(parte5-recorridos-grafos)=
# Recorridos

Los recorridos son el corazón algorítmico de la familia de grafos. A partir de DFS y BFS se desprenden ideas de alcanzabilidad, detección de ciclos, caminos mínimos no ponderados y análisis de componentes.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender DFS y BFS como patrones de exploración que reutilizan pilas, colas y marcado de visitados.

**Prerrequisitos.** Conviene haber leído [Representación de grafos](representacion.md) y recordar [Pilas](../secuencias/pilas.md) y [Colas](../secuencias/colas.md).

**Desarrollo.** El capítulo debería presentar DFS recursivo e iterativo, BFS, árboles de recorrido y usos típicos para alcanzabilidad y conectividad.
:::

## Qué resuelven DFS y BFS

Este bloque debería cubrir:

- exploración sistemática,
- vértices visitados,
- árboles de recorrido,
- diferencia entre profundidad y anchura.

## Qué aplicaciones se apoyan en ellos

Este bloque debería cubrir:

- alcanzabilidad,
- componentes conexas,
- detección de ciclos,
- caminos mínimos en grafos no ponderados.

## Resumen

El capítulo debería dejar instalada una idea central: muchos algoritmos de grafos no parten de cero; son refinamientos de un buen recorrido con el estado correcto.

## Ejercicios

```{exercise}
:label: ex-parte5-recorridos-grafos-mini

Explicá por qué BFS encuentra caminos mínimos en cantidad de aristas cuando el grafo no tiene pesos, pero DFS no garantiza eso.
```

## Próximo paso

Para seguir, conviene pasar a [Caminos mínimos](caminos_minimos.md), donde el problema deja de ser solo recorrer y pasa a ser optimizar.
