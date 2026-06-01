---
title: "Diccionarios y conjuntos"
subtitle: "Índice de familia"
subject: Estructuras de Datos
description: Mapa de la familia de diccionarios y conjuntos para la parte 6.
---

(parte6-diccionarios-conjuntos)=
# Diccionarios y conjuntos

Esta familia reúne las estructuras donde la **pertenencia**, la **búsqueda por clave** y la **actualización de asociaciones** son la preocupación principal. A diferencia de las secuencias (donde importa el *dónde* está el dato), acá el foco se desplaza hacia el *qué* es el dato: dejamos de buscar por índice posicional y pasamos a buscar por **identidad o valor**.

En Java, esta familia se manifiesta principalmente a través del framework de colecciones con las interfaces `Map` (para diccionarios) y `Set` (para conjuntos). En esta parte del apunte, desarmamos la "magia" de estas clases para entender cómo funcionan por dentro.

## Filosofía del acceso por clave

Pasar de secuencias a diccionarios implica un cambio mental en la organización de la memoria:

1. **De posición a identidad**: En un arreglo, buscás el elemento `i`. En un diccionario, buscás la información de `"Juan"` o el producto con ID `4502`.
2. **Abstracción del almacenamiento**: Al usuario del TAD no le importa si el dato está "al principio" o "al final"; le importa que, dada la clave, el valor aparezca rápido.
3. **El contrato de unicidad**: En general, las claves no se repiten. Esto simplifica la lógica de actualización: asociar un valor a una clave existente pisa el anterior.

:::{tip}
Si venís de C, pensá en un diccionario como una generalización de un arreglo donde el "índice" no tiene por qué ser un entero contiguo de $0$ a $N-1$, sino que puede ser cualquier tipo de dato con capacidad de ser comparado o hasheado.
:::

## Mapa de la familia

| Orden | Página | Concepto central | Aplicación típica |
| :--- | :--- | :--- | :--- |
| 1 | [Fundamentos](fundamentos.md) | Claves, valores y pertenencia | Modelado inicial de problemas |
| 2 | [Tablas hash](tablas_hash.md) | Acceso por cálculo (hashing) | Cachés, bases de datos, lookups $O(1)$ |
| 3 | [Diccionarios ordenados](diccionarios_ordenados.md) | Acceso por comparación | Listas de precios, agendas, rangos |
| 4 | [Tries](tries.md) | Acceso por estructura de clave | Autocompletado, correctores, prefijos |
| 5 | [Conjuntos disjuntos](conjuntos_disjuntos.md) | Partición y agrupamiento | Redes, componentes conexos, Kruskal |

## Criterios de elección

¿Cómo decidir qué estructura usar? No hay una "mejor" en términos absolutos, sino una más adecuada para cada compromiso (*trade-off*).

| Si necesitás... | Probablemente te convenga... | Porque... |
| :--- | :--- | :--- |
| Velocidad pura de búsqueda | [Tablas hash](tablas_hash.md) | Ofrecen tiempo constante $O(1)$ promedio. |
| Mantener los datos ordenados | [Diccionarios ordenados](diccionarios_ordenados.md) | Permiten recorrer en orden y buscar rangos. |
| Búsquedas por prefijo | [Tries](tries.md) | Explotan la estructura compartida de las claves. |
| Agrupar elementos en grupos | [Conjuntos disjuntos](conjuntos_disjuntos.md) | Son imbatibles para la operación `union` y `find`. |

## Relación con Java y la memoria

A lo largo de estos capítulos, vamos a ver cómo estas estructuras se mapean a la realidad:

- **Representación**: Cómo pasamos de un concepto abstracto (un conjunto de personas) a algo que entra en la memoria (nodos, punteros, arreglos de buckets).
- **Contratos**: La importancia de `hashCode()` y `equals()` en Java como base para que estas estructuras funcionen.
- **Eficiencia**: Por qué un `HashMap` es generalmente más rápido que un `TreeMap`, pero por qué a veces preferimos el segundo.

## Qué deberías dominar al finalizar

Un estudiante que recorrió esta familia con éxito debería poder:

1. Justificar la elección de una estructura sobre otra basándose en el análisis de complejidad.
2. Explicar cómo se resuelven las colisiones en una tabla hash.
3. Dibujar la estructura de un Trie dado un conjunto de palabras.
4. Implementar las operaciones básicas de un conjunto disjunto con optimizaciones de ranking y compresión de caminos.
5. Conectar estas estructuras con problemas del mundo real (ej. un sistema de ruteo, un motor de búsqueda, un gestor de archivos).

## Próximo paso

Empezamos por los cimientos: [Fundamentos de diccionarios y conjuntos](fundamentos.md), donde definimos qué es una clave y qué operaciones mínimas esperamos de estas estructuras.
