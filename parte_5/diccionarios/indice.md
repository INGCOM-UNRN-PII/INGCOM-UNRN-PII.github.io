---
title: "Diccionarios y conjuntos"
subtitle: "Índice de familia"
subject: Estructuras de Datos
description: Mapa de la familia de diccionarios y conjuntos para la parte 5.
---

(parte5-diccionarios-conjuntos)=
# Diccionarios y conjuntos

Esta familia reúne las estructuras donde la pertenencia, la búsqueda por clave y la actualización de asociaciones son la preocupación principal. A diferencia de las secuencias, acá el foco deja de estar en la posición y pasa a estar en **cómo identificar elementos** y **cómo recuperarlos con criterio**.

En Java, esta familia aparece enseguida cuando se usan interfaces como `Map` y `Set`. En esta parte conviene mirar primero el problema abstracto y recién después las clases concretas del framework.

:::{note}
Conviene usar este índice para ordenar el problema de acceso por clave antes de elegir entre hashing, orden, prefijos o partición.
:::

## Recorrido sugerido

| Orden | Página | Rol en la familia |
| :--- | :--- | :--- |
| 1 | [Fundamentos de diccionarios y conjuntos](fundamentos.md) | Define claves, pertenencia y operaciones base |
| 2 | [Tablas hash](tablas_hash.md) | Instala acceso promedio eficiente |
| 3 | [Diccionarios ordenados](diccionarios_ordenados.md) | Contrasta hashing con estructuras ordenadas |
| 4 | [Tries](tries.md) | Introduce búsqueda por prefijos |
| 5 | [Conjuntos disjuntos](conjuntos_disjuntos.md) | Cierra con partición dinámica de elementos |

## Comparación rápida

| Estructura | Fuerte principal | Trade-off central | Cuándo conviene |
| :--- | :--- | :--- | :--- |
| [Tablas hash](tablas_hash.md) | Acceso promedio rápido | Peor caso y colisiones | Cuando importa lookup frecuente por clave |
| [Diccionarios ordenados](diccionarios_ordenados.md) | Orden y rangos | Más costo que hash en promedio | Cuando hacen falta consultas por intervalo |
| [Tries](tries.md) | Prefijos | Costo de memoria por estructura | Cuando las claves son cadenas o secuencias de símbolos |
| [Conjuntos disjuntos](conjuntos_disjuntos.md) | Partición dinámica | No reemplaza un mapa general | Cuando importa agrupar y unir componentes |

## Qué preguntas debería ayudar a responder esta familia

Al terminar esta familia, conviene poder responder con criterio:

1. cuándo alcanza con saber si un elemento pertenece o no a un conjunto,
2. cuándo hace falta asociar una clave con un valor,
3. cuándo importa el orden de las claves,
4. cuándo la estructura interna de la clave importa más que su comparación global,
5. cuándo el problema no es “buscar” sino “mantener grupos”.

## Decisiones rápidas

Si el problema dominante es:

- **lookup frecuente por clave**, conviene empezar por [tablas hash](tablas_hash.md),
- **consultas por rango o recorrido ordenado**, conviene mirar [diccionarios ordenados](diccionarios_ordenados.md),
- **búsqueda por prefijo**, conviene mirar [tries](tries.md),
- **unir y consultar componentes**, conviene mirar [conjuntos disjuntos](conjuntos_disjuntos.md).

## Conexiones con el resto de la parte

Esta familia funciona como puente entre varias ideas ya vistas o que aparecen después:

- reutiliza el análisis de costo de [análisis de algoritmos](../algoritmos.md),
- se conecta con `Map` y `Set` de [colecciones en Java](../../parte_2/07_colecciones_genericos.md),
- y prepara el terreno para árboles de búsqueda y algoritmos de grafos.

## Cierre integrador sugerido

Un buen cierre para esta familia sería justificar qué estructura usar para:

- un padrón por DNI,
- autocompletado por prefijo,
- un índice por rango de fechas,
- agrupación de componentes en una red.
