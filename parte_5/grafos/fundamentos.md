---
title: "Fundamentos de grafos"
subtitle: "Relaciones generales entre vértices"
subject: Estructuras de Datos
description: Esqueleto editorial del capítulo sobre fundamentos de grafos.
---

(parte5-fundamentos-grafos)=
# Fundamentos de grafos

Los grafos cierran la parte con la familia más general: cuando ni la linealidad ni la jerarquía alcanzan para modelar el problema, hace falta pensar en vértices, aristas, caminos y conectividad.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Instalar el vocabulario mínimo de grafos para poder hablar después de representación, recorridos y algoritmos clásicos.

**Prerrequisitos.** Conviene haber trabajado [Árboles](../arboles/indice.md), porque ayuda a contrastar jerarquía estricta con relaciones arbitrarias.

**Desarrollo.** El capítulo debería definir tipos de grafo, caminos, ciclos y conectividad básica, preparando el terreno para la representación y los recorridos.
:::

## Qué define a un grafo

Este bloque debería cubrir:

- vértices y aristas,
- grafos dirigidos y no dirigidos,
- grafos ponderados y no ponderados,
- caminos, ciclos y componentes.

## Qué preguntas abre esta familia

Este bloque debería cubrir:

- cómo representar el grafo,
- cómo recorrerlo,
- cómo optimizar caminos o conexiones,
- cómo detectar dependencias y componentes.

## Resumen

El capítulo debería dejar instalada una idea central: un grafo no es solo “muchos nodos conectados”, sino un modelo general para problemas donde las relaciones ya no pueden imponerse como lista o árbol.

## Ejercicios

```{exercise}
:label: ex-parte5-fundamentos-grafos-mini

Elegí un dominio real, por ejemplo una red de rutas o una red social, e indicá qué representarían sus vértices y aristas. Aclará además si el grafo debería ser dirigido, ponderado o ambos.
```

## Próximo paso

Para seguir, conviene pasar a [Representación de grafos](representacion.md), donde el problema conceptual se transforma en estructura concreta.
