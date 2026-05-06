---
title: "Parte 5: Estructuras de Datos"
subtitle: "Esqueleto temático en revisión"
subject: Estructuras de Datos
description: Mapa editorial de la parte 5 en desarrollo.
---

(parte-5-estructuras-de-datos)=
# Parte 5: Estructuras de Datos

Esta parte todavía no está publicada en el TOC principal. Su función actual es fijar una estructura exhaustiva y coherente para el tramo de estructuras de datos, de modo que después se pueda redactar sin volver a discutir el mapa general.

:::{important}
**Estado.** Material en revisión. El objetivo de estas páginas no es cerrar el contenido final, sino definir alcance, secuencia y separación entre teoría abstracta e implementaciones concretas.
:::

## Propósito de la parte

La parte 5 debería consolidar tres capas que en el programa aparecen fuertemente relacionadas y conviene distinguir con claridad:

1. **TAD y contratos**: qué problema modela cada estructura y qué operaciones promete,
2. **análisis algorítmico**: cuánto cuestan las operaciones y qué trade-offs aparecen,
3. **implementaciones concretas**: cómo cambian costo, memoria, invariantes y casos de uso según la representación elegida.

## Orden sugerido de desarrollo

| Orden | Página | Rol |
| :--- | :--- | :--- |
| 1 | [Tipos abstractos de datos](adt.md) | Fijar vocabulario sobre interfaz, representación e invariantes |
| 2 | [Análisis de algoritmos](algoritmos.md) | Instalar el criterio de comparación entre implementaciones |
| 3 | [Hardware y Localidad de Memoria](localidad_memoria.md) | Entender el impacto de la caché en el rendimiento real |
| 4 | [Benchmark y Profiling](profiling.md) | Medir el rendimiento en la práctica aislando el ruido |
| 5 | [Secuencias](secuencias/indice.md) | Empezar por las estructuras lineales |
| 6 | [Diccionarios y conjuntos](diccionarios/indice.md) | Pasar a acceso por clave, hashing y orden |
| 7 | [Árboles](arboles/indice.md) | Trabajar jerarquía, búsqueda y prioridad |
| 8 | [Grafos](grafos/indice.md) | Cerrar con relaciones generales y algoritmos clásicos |

## Capítulos nucleares

Estos capítulos conviene dominarlos sí o sí porque fijan el lenguaje y las comparaciones que el resto de la parte reutiliza:

| Capítulo | Por qué es nuclear |
| :--- | :--- |
| [Tipos abstractos de datos](adt.md) | Define qué se abstrae y qué pertenece a la implementación |
| [Análisis de algoritmos](algoritmos.md) | Fija el modelo de costo de toda la parte |
| [Fundamentos de secuencias](secuencias/fundamentos.md) | Introduce la familia lineal que reaparece en casi todas las implementaciones |
| [Tablas hash](diccionarios/tablas_hash.md) | Instala el trade-off más fuerte entre acceso promedio y peor caso |
| [Árboles binarios de búsqueda](arboles/arboles_busqueda.md) | Une jerarquía, orden y eficiencia |
| [Recorridos](grafos/recorridos.md) | Conecta pilas, colas, árboles y grafos desde algoritmos concretos |

## Repaso y ampliación

Estas páginas sirven mejor como profundización o segunda lectura una vez que ya está claro el mapa general:

| Página | Tipo | Uso sugerido |
| :--- | :--- | :--- |
| [Deques](secuencias/deques.md) | Ampliación | Releer cuando aparezcan operaciones eficientes en ambos extremos |
| [Colas de prioridad](secuencias/colas_prioridad.md) | Puente | Usar para enlazar secuencias con heaps |
| [Tries](diccionarios/tries.md) | Ampliación | Consultar cuando el dominio esté guiado por prefijos |
| [Árboles balanceados](arboles/arboles_balanceados.md) | Profundización | Releer cuando la garantía de altura sea central |
| [Árboles B](arboles/arboles_b.md) | Profundización | Consultar al hablar de almacenamiento externo |
| [Orden topológico](grafos/orden_topologico.md) y [Conectividad](grafos/conectividad.md) | Ampliación | Releer frente a dependencias y partición de grafos |

## Criterio de organización

Cada subdirectorio mezcla dos planos:

1. un capítulo de **fundamentos** para la teoría general del TAD o familia,
2. capítulos de **estructuras específicas** donde aparezcan sus variantes e implementaciones.

Eso evita dos errores frecuentes:

- explicar la abstracción sin bajar nunca a código,
- o listar implementaciones sueltas sin una idea unificadora del TAD.

## Índice estructural exhaustivo

### Núcleo transversal

- [Tipos abstractos de datos](adt.md)
- [Análisis de algoritmos](algoritmos.md)
- [Hardware y Localidad de Memoria](localidad_memoria.md)
- [Benchmark y Profiling](profiling.md)

### Secuencias

- [Índice de secuencias](secuencias/indice.md)
- [Fundamentos de secuencias](secuencias/fundamentos.md)
- [Arreglos](secuencias/arreglos.md)
- [Listas enlazadas](secuencias/listas_enlazadas.md)
- [Pilas](secuencias/pilas.md)
- [Colas](secuencias/colas.md)
- [Deques](secuencias/deques.md)
- [Colas de prioridad](secuencias/colas_prioridad.md)

### Diccionarios y conjuntos

- [Índice de diccionarios y conjuntos](diccionarios/indice.md)
- [Fundamentos de diccionarios y conjuntos](diccionarios/fundamentos.md)
- [Tablas hash](diccionarios/tablas_hash.md)
- [Diccionarios ordenados](diccionarios/diccionarios_ordenados.md)
- [Tries](diccionarios/tries.md)
- [Conjuntos disjuntos](diccionarios/conjuntos_disjuntos.md)

### Árboles

- [Índice de árboles](arboles/indice.md)
- [Fundamentos de árboles](arboles/fundamentos.md)
- [Árboles binarios](arboles/arboles_binarios.md)
- [Árboles binarios de búsqueda](arboles/arboles_busqueda.md)
- [Árboles balanceados](arboles/arboles_balanceados.md)
- [Heaps](arboles/heaps.md)
- [Árboles B](arboles/arboles_b.md)

### Grafos

- [Índice de grafos](grafos/indice.md)
- [Fundamentos de grafos](grafos/fundamentos.md)
- [Representación de grafos](grafos/representacion.md)
- [Recorridos](grafos/recorridos.md)
- [Caminos mínimos](grafos/caminos_minimos.md)
- [Árboles de expansión](grafos/arboles_de_expansion.md)
- [Orden topológico](grafos/orden_topologico.md)
- [Conectividad](grafos/conectividad.md)

## Criterios de exhaustividad

Para considerar cerrada esta parte, cada familia debería cubrir como mínimo:

1. definición abstracta,
2. operaciones básicas,
3. invariantes,
4. implementaciones alternativas,
5. complejidad temporal y espacial,
6. ventajas y desventajas,
7. aplicaciones típicas,
8. errores frecuentes de implementación.

## Próximo paso

Una vez fijado este mapa, conviene completar primero los capítulos transversales ([ADT](adt.md) y [algoritmos](algoritmos.md)) y después avanzar familia por familia.
