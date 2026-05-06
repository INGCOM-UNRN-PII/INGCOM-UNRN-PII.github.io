---
title: "Conectividad"
subtitle: "Componentes, partición y robustez"
subject: Estructuras de Datos
description: Esqueleto editorial del capítulo sobre conectividad en grafos.
---

(parte5-conectividad)=
# Conectividad

La conectividad cierra la familia volviendo sobre una de las preguntas más generales de grafos: cómo se parte el problema en componentes, qué tan conectado está el sistema y qué algoritmos permiten detectar esa estructura.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender conectividad en grafos dirigidos y no dirigidos, y vincularla con componentes y partición del problema.

**Prerrequisitos.** Conviene haber leído [Orden topológico](orden_topologico.md) y tener presentes los recorridos y conjuntos disjuntos vistos antes.

**Desarrollo.** El capítulo debería presentar componentes conexas, conectividad fuerte como ampliación y aplicaciones sobre robustez o segmentación de redes.
:::

## Qué preguntas resuelve la conectividad

Este bloque debería cubrir:

- componentes conexas,
- conectividad fuerte en digrafos,
- pertenencia de dos vértices al mismo componente,
- separación del grafo en subproblemas.

## Qué herramientas y aplicaciones aparecen

Este bloque debería cubrir:

- DFS y BFS por componentes,
- Kosaraju o Tarjan como ampliación,
- relación con conjuntos disjuntos,
- análisis de redes y robustez estructural.

## Resumen

El capítulo debería dejar instalada una idea final: la conectividad no es un detalle accesorio de los grafos; muchas veces define la forma misma en que un problema debe particionarse y resolverse.

## Ejercicios

```{exercise}
:label: ex-parte5-conectividad-mini

Describí un problema real donde sea importante detectar componentes separadas de una red. Indicá qué decisión práctica podría tomarse a partir de ese resultado.
```

## Próximo paso

Para seguir, conviene volver a [la portada de la parte](../indice.md) y revisar qué familias ya tienen un esqueleto editorial suficientemente claro como para avanzar hacia contenido pleno.
