---
title: "Parte 5: Estructuras de Datos"
subtitle: "Mapa de aprendizaje"
subject: Estructuras de Datos
description: Recorrido sugerido y mapa estructural de la parte 5, con foco en TAD, costo, implementaciones y algoritmos sobre estructuras ya consolidadas.
---

(parte-5-estructuras-de-datos)=
# Parte 5: Estructuras de Datos

Esta parte ya integra el TOC principal y reúne un mapa de aprendizaje estable para el tramo de estructuras de datos: primero fija el lenguaje de TAD y costo, después recorre familias concretas y finalmente cierra con algoritmos sobre grafos.

:::{important}
**Estado.** Parte publicada. El recorrido completo ya quedó desarrollado en las cuatro familias y puede leerse como tramo estable de la cursada.
:::

## Propósito de la parte

La parte 5 consolida cuatro preguntas que conviene aprender a separar:

1. **qué problema abstracto modela la estructura**;
2. **qué operaciones promete y qué invariantes sostiene**;
3. **cuánto cuestan esas operaciones**;
4. **cómo cambia todo eso según la representación elegida**.

Dicho más directo: no alcanza con saber “qué estructura usar”. Esta parte busca que puedas justificar por qué una implementación conviene más que otra según acceso, memoria, altura, prioridad, colisiones o costo por bloque.

## Estado editorial actual

| Bloque | Estado actual | Qué ya aporta |
| :--- | :--- | :--- |
| Núcleo transversal | Desarrollo completo | Lenguaje común sobre TAD, contratos y complejidad |
| Secuencias | Desarrollo completo | Contraste entre memoria contigua, nodos y restricciones de acceso |
| Diccionarios y conjuntos | Desarrollo completo | Acceso por clave, hashing, orden, prefijos y partición |
| Árboles | Desarrollo completo | Jerarquía, búsqueda, balance, prioridad y almacenamiento externo |
| Grafos | Desarrollo completo | Representación, recorridos, optimización, precedencias y partición |

## Orden sugerido de lectura

| Orden | Página | Rol |
| :--- | :--- | :--- |
| 3 | [Secuencias](secuencias/indice.md) | Entrar por las estructuras lineales y sus restricciones |
| 4 | [Diccionarios y conjuntos](diccionarios/indice.md) | Pasar del acceso posicional al acceso por clave |
| 5 | [Árboles](arboles/indice.md) | Trabajar jerarquía, búsqueda, balance y prioridad |
| 6 | [Grafos](grafos/indice.md) | Cerrar con relaciones generales y algoritmos clásicos |

Si querés una versión todavía más corta del recorrido, el hilo principal hoy es:

3. [fundamentos de secuencias](secuencias/fundamentos.md),
4. [tablas hash](diccionarios/tablas_hash.md),
5. [BST](arboles/arboles_busqueda.md),
6. [recorridos de grafos](grafos/recorridos.md).

## Capítulos nucleares

Estos capítulos conviene dominarlos sí o sí porque fijan ideas que después reaparecen en toda la parte:

| Capítulo | Por qué es nuclear |
| :--- | :--- |
| [Tipos abstractos de datos](adt.md) | Separa contrato abstracto de representación concreta |
| [Análisis de algoritmos](algoritmos.md) | Fija el modelo de costo reutilizado por toda la parte |
| [Fundamentos de secuencias](secuencias/fundamentos.md) | Instala el lenguaje base de operaciones lineales |
| [Tablas hash](diccionarios/tablas_hash.md) | Muestra el trade-off más fuerte entre promedio, colisiones y peor caso |
| [Árboles binarios de búsqueda](arboles/arboles_busqueda.md) | Une forma jerárquica, orden y dependencia de la altura |
| [Recorridos](grafos/recorridos.md) | Conecta pilas, colas, árboles y grafos desde algoritmos concretos |

## Repaso y ampliación

Estas páginas funcionan mejor como profundización o segunda lectura:

| Página | Tipo | Uso sugerido |
| :--- | :--- | :--- |
| [Deques](secuencias/deques.md) | Ampliación | Releer cuando ambos extremos pasan a ser relevantes |
| [Colas de prioridad](secuencias/colas_prioridad.md) | Puente | Usar para enlazar secuencias con heaps y algoritmos voraces |
| [Tries](diccionarios/tries.md) | Ampliación | Consultar cuando el dominio esté guiado por prefijos |
| [Árboles balanceados](arboles/arboles_balanceados.md) | Profundización | Releer cuando la garantía de altura sea una exigencia real |
| [Árboles B](arboles/arboles_b.md) | Profundización | Consultar cuando el costo dominante sea leer páginas o bloques |
| [Orden topológico](grafos/orden_topologico.md) y [Conectividad](grafos/conectividad.md) | Ampliación | Releer frente a dependencias, componentes y partición del grafo |

## Índice estructural exhaustivo

### Núcleo transversal

- [Tipos abstractos de datos](adt.md)
- [Análisis de algoritmos](algoritmos.md)

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

## Criterio de organización

La parte quedó organizada para evitar dos errores frecuentes:

1. enseñar solo la abstracción y nunca bajar a representación;
2. listar implementaciones sueltas sin un problema abstracto que las unifique.

Por eso cada familia intenta sostener este patrón:

- una página de **fundamentos**,
- varias páginas de **estructuras específicas**,
- y un índice de familia que funcione como mapa comparativo.

## Próximo paso

Si ya tenés claro el marco transversal, el siguiente paso natural es entrar por [Secuencias](secuencias/indice.md). Si lo que querés es revisar toda la progresión de la parte antes de decidir su publicación, conviene pasar después por los índices de [diccionarios](diccionarios/indice.md), [árboles](arboles/indice.md) y [grafos](grafos/indice.md).
