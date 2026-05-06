---
title: "Representación de grafos"
subtitle: "Matriz, lista y costo de acceso"
subject: Estructuras de Datos
description: Esqueleto editorial del capítulo sobre representación de grafos.
---

(parte5-representacion-grafos)=
# Representación de grafos

En grafos, la representación influye tanto como el algoritmo. Antes de correr BFS, Dijkstra o Prim, hace falta decidir cómo se almacenan vértices y aristas y qué consultas conviene optimizar.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Comparar las representaciones más comunes de grafos y entender cómo afectan memoria, consulta de adyacencia e iteración de vecinos.

**Prerrequisitos.** Conviene haber leído [Fundamentos de grafos](fundamentos.md), porque este capítulo transforma el lenguaje conceptual en decisiones de estructura.

**Desarrollo.** El capítulo debería contrastar matriz de adyacencia, lista de adyacencia y lista de aristas, vinculando cada una con densidad del grafo y tipo de algoritmo.
:::

## Qué opciones de representación existen

Este bloque debería cubrir:

- matriz de adyacencia,
- lista de adyacencia,
- lista de aristas,
- codificación de pesos y direcciones.

## Qué trade-offs aparecen

Este bloque debería cubrir:

- grafos densos vs dispersos,
- costo de consultar si dos vértices son adyacentes,
- costo de iterar vecinos,
- impacto en memoria y simplicidad de implementación.

## Resumen

El capítulo debería dejar clara una idea: en grafos no hay una representación universalmente mejor; cada una favorece ciertas operaciones y castiga otras.

## Ejercicios

```{exercise}
:label: ex-parte5-representacion-grafos-mini

Justificá qué representación elegirías para un grafo muy disperso y qué elegirías para uno muy denso. Indicá en cada caso qué operación te interesa optimizar.
```

## Próximo paso

Para seguir, conviene pasar a [Recorridos](recorridos.md), donde esas representaciones se usan para explorar el grafo.
