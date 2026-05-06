---
title: "Colas de prioridad"
subtitle: "Elegir el siguiente elemento por prioridad"
subject: Estructuras de Datos
description: Esqueleto editorial del capítulo sobre colas de prioridad.
---

(parte5-colas-prioridad)=
# Colas de prioridad

Las colas de prioridad cierran la familia de secuencias mostrando un cambio de criterio fuerte: el siguiente elemento ya no sale por orden de llegada ni por última inserción, sino por prioridad relativa.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender la cola de prioridad como TAD y anticipar por qué los heaps se vuelven una implementación dominante.

**Prerrequisitos.** Conviene haber leído [Deques](deques.md) y tener presente el marco de [Análisis de algoritmos](../algoritmos.md).

**Desarrollo.** El capítulo debería definir operaciones principales, comparar implementaciones simples con la solución basada en heaps y conectar la estructura con algoritmos voraces y planificación.
:::

## Qué define a una cola de prioridad

Este bloque debería cubrir:

- operaciones `insert`, `findMin` o `findMax`, `deleteMin` o `deleteMax`,
- criterio de prioridad y empates,
- diferencia con cola FIFO,
- estabilidad y comparadores.

## Cómo se implementa y dónde sirve

Este bloque debería cubrir:

- arreglos ordenados y desordenados,
- listas enlazadas ordenadas,
- heap binario como implementación dominante,
- aplicaciones en planificación, simulación y caminos mínimos.

## Resumen

El capítulo debería dejar clara una idea: una cola de prioridad no ordena “todo” el conjunto como un diccionario ordenado, pero sí mantiene exactamente la información que hace falta para extraer el mejor candidato en cada paso.

## Ejercicios

```{exercise}
:label: ex-parte5-colas-prioridad-mini

Justificá por qué una cola FIFO no alcanza para modelar la atención de pacientes en una guardia cuando la prioridad médica debe alterar el orden de ingreso.
```

## Próximo paso

Para seguir, conviene pasar a [Diccionarios y conjuntos](../diccionarios/indice.md), donde la pregunta central deja de ser la posición y pasa a ser la clave.
