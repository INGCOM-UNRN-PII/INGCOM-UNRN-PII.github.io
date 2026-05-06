---
title: "Orden topológico"
subtitle: "Linearizar dependencias en DAG"
subject: Estructuras de Datos
description: Esqueleto editorial del capítulo sobre orden topológico.
---

(parte5-orden-topologico)=
# Orden topológico

El orden topológico muestra un caso donde el grafo no modela distancias ni costos, sino dependencias parciales. La pregunta ya no es “qué vértices conectan”, sino “qué debe ocurrir antes que qué”.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender cuándo un grafo dirigido acíclico admite una linearización válida y cómo construirla.

**Prerrequisitos.** Conviene haber leído [Árboles de expansión](arboles_de_expansion.md) y recordar [Recorridos](recorridos.md).

**Desarrollo.** El capítulo debería definir DAG, presentar DFS y Kahn como estrategias de orden topológico y conectar la idea con planificación y compilación.
:::

## Qué problema resuelve

Este bloque debería cubrir:

- dependencias parciales,
- grafos dirigidos acíclicos,
- linearización válida,
- detección de ciclos como condición de imposibilidad.

## Qué algoritmos y usos aparecen

Este bloque debería cubrir:

- orden topológico por DFS,
- algoritmo de Kahn,
- cronogramas de tareas,
- correlatividades y compilación.

## Resumen

El capítulo debería dejar clara una idea: el orden topológico no “ordena” cualquier grafo; solo funciona cuando la estructura expresa precedencias sin ciclos.

## Ejercicios

```{exercise}
:label: ex-parte5-orden-topologico-mini

Modelá un conjunto de materias con correlatividades como un grafo y explicá qué significaría encontrar un ciclo en ese contexto.
```

## Próximo paso

Para seguir, conviene pasar a [Conectividad](conectividad.md), donde la atención se concentra en componentes y particiones del grafo.
