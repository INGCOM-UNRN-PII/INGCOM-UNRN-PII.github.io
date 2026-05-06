---
title: "Árboles B"
subtitle: "Balance y ramificación para almacenamiento externo"
subject: Estructuras de Datos
description: Esqueleto editorial del capítulo sobre árboles B.
---

(parte5-arboles-b)=
# Árboles B

Los árboles B cierran la familia mostrando que las decisiones de estructura cambian cuando los datos ya no viven solo en memoria principal. En ese contexto, bajar la altura y aprovechar bloques es más importante que mantener una forma binaria.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender por qué los árboles B aparecen cuando la unidad de costo relevante deja de ser la comparación y pasa a ser el acceso a bloque o página.

**Prerrequisitos.** Conviene haber leído [Heaps](heaps.md) y recordar la discusión sobre altura y balance en árboles de búsqueda.

**Desarrollo.** El capítulo debería presentar árboles multicamino balanceados, sus operaciones básicas y su relación con índices de bases de datos y sistemas de archivos.
:::

## Qué problema resuelven

Este bloque debería cubrir:

- almacenamiento externo,
- gran factor de ramificación,
- baja altura,
- acceso por bloques o páginas.

## Qué variantes y usos aparecen

Este bloque debería cubrir:

- B-Tree,
- B+Tree como ampliación,
- división, fusión y redistribución de nodos,
- uso en índices persistentes.

## Resumen

El capítulo debería dejar clara una idea: cuando cambia el modelo de costo y leer un bloque domina el tiempo, los árboles B se vuelven más adecuados que las variantes binarias clásicas.

## Ejercicios

```{exercise}
:label: ex-parte5-arboles-b-mini

Explicá por qué una estructura con alto factor de ramificación puede ser ventajosa en disco aunque resulte menos natural que un árbol binario para explicar en clase.
```

## Próximo paso

Para seguir, conviene pasar a [Grafos](../grafos/indice.md), donde la organización deja de ser jerárquica y pasa a ser completamente general.
