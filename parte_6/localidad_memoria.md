---
title: "Hardware y Localidad de Memoria"
subtitle: "Por qué O(1) a veces es más lento que O(N)"
subject: Estructuras de Datos
description: El impacto de la jerarquía de memoria, la caché del procesador y la localidad espacial en el rendimiento real de las estructuras de datos.
---

(parte5-localidad-memoria)=
# Hardware y Localidad de Memoria

El análisis asintótico (la notación Big-O) asume un modelo teórico muy simplificado donde todas las lecturas de memoria cuestan exactamente lo mismo: una operación básica, `O(1)`. En el mundo real del hardware moderno, **esa suposición es falsa**.

Leer un dato de la memoria principal (RAM) puede ser cientos de veces más lento que leerlo de la memoria caché interna del procesador (L1/L2/L3). Por eso, cómo se distribuyen los datos físicamente en la memoria afecta dramáticamente el tiempo de ejecución (wall-clock time), incluso si el algoritmo tiene la misma complejidad teórica.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender la jerarquía de memoria y la localidad espacial para tomar decisiones informadas entre estructuras contiguas (arreglos) y dispersas (nodos).

**Prerrequisitos.** Conviene tener fresco el capítulo de [Análisis de algoritmos](algoritmos.md).

**Desarrollo.** Se explica la memoria caché, los *cache misses*, y se compara el rendimiento empírico de arreglos versus listas enlazadas bajo el lente del hardware.
:::

## La jerarquía de memoria y la Caché

Los procesadores modernos son muchísimo más rápidos que la memoria RAM. Si la CPU tuviera que esperar a la RAM en cada instrucción, se la pasaría inactiva. Para evitarlo, los procesadores incluyen memoria **caché**: memorias muy pequeñas, pero ultra rápidas, integradas en el mismo chip.

Cuando la CPU pide leer la dirección de memoria `X`:

1. Primero busca en la caché L1 (la más rápida y pequeña). Si está ahí, es un **Cache Hit** (Toma ~1-2 ciclos de reloj).
2. Si no está, busca en L2 o L3.
3. Si no está en ninguna caché, es un **Cache Miss**. La CPU tiene que ir hasta la RAM a buscar el dato (Toma ~100-300 ciclos de reloj).

¡Un *cache miss* es más de 100 veces más lento que un *cache hit*!

## Localidad Espacial

Acá entra el truco del hardware: cuando ocurre un *cache miss* y la CPU tiene que ir a la RAM a buscar la dirección `X`, **no trae un solo byte**. Trae un bloque contiguo de memoria entero (llamado *Cache Line*, típicamente de 64 bytes).

El hardware asume que si tu programa acaba de pedir el byte en `X`, es altamente probable que en los próximos nanosegundos pida el byte en `X+1`, `X+2`, etc. A este principio se lo llama **Localidad Espacial**.

### Arreglos vs Memoria Fragmentada

¿Qué pasa cuando recorremos un `int[]` (arreglo primitivo de enteros)?

- El primer elemento (`arreglo[0]`) genera un *cache miss*. La CPU va a la RAM y trae un *cache line* completo (ej: 16 enteros contiguos).
- Los siguientes 15 accesos (`arreglo[1]` a `arreglo[15]`) generan **cache hits** inmediatos porque ya están en la L1. Son prácticamente gratis.

¿Qué pasa cuando recorremos una `LinkedList<Integer>`?

- Cada nodo de la lista fue creado con un `new` distinto en momentos distintos, por lo que el *Garbage Collector* los ubicó en direcciones arbitrarias y dispersas en la memoria (heap).
- Leer `nodoActual` genera un *cache miss*. La CPU trae un bloque de 64 bytes de la RAM, pero el único dato útil en ese bloque es ese nodo. El siguiente `nodo.siguiente` apunta a una zona de memoria completamente distinta.
- Al acceder al siguiente nodo, ocurre **otro cache miss**.

## El choque entre Teoría y Realidad

Según la teoría asintótica, recorrer todos los elementos de un arreglo de tamaño N y de una lista enlazada de tamaño N toma exactamente la misma cantidad de operaciones: `O(N)`.

Sin embargo, en un benchmark real moderno, **recorrer el arreglo de enteros puede ser entre 10 y 50 veces más rápido** que recorrer la lista enlazada. La constante oculta en la notación asintótica está dictada por el hardware.

### Por qué un O(N) puede ganar a un O(1)

Imaginá que tenés una colección de 1000 elementos y querés insertar un valor en el medio (posición 500).

- **Lista enlazada:** Teóricamente, insertar un nodo con punteros es `O(1)`. Pero primero tenés que recorrer hasta la mitad (`O(N)`), sufriendo cientos de *cache misses*.
- **Arreglo dinámico (ArrayList):** Insertar en el medio implica desplazar 500 elementos a la derecha, lo cual es teóricamente `O(N)`. ¡Horrible! Sin embargo, la CPU tiene instrucciones vectorizadas ultra-optimizadas (SIMD) para mover bloques contiguos de memoria (`System.arraycopy`), y todo ocurre sobre datos que ya están calientes en la caché.

En la práctica, para colecciones no tan gigantes, el `ArrayList` `O(N)` suele ganarle en tiempo real a la `LinkedList` `O(1)` debido a la aplastante ventaja de la **localidad espacial**.

## Resumen y Criterio de Diseño

1. **La memoria contigua es la reina del rendimiento.** Si el rendimiento absoluto importa, preferí estructuras respaldadas por arreglos (`ArrayList`, `ArrayDeque`).
2. **Los punteros y referencias rompen la caché.** Los árboles, grafos y listas enlazadas sufren de memoria fragmentada. Son estructuras excelentes por sus propiedades lógicas, pero el hardware no las favorece.
3. En Java, un `ArrayList<Objeto>` es contiguo solo respecto a las *referencias* (punteros a memoria). Los objetos reales siguen estando dispersos. Para aprovechar al máximo la localidad espacial en cálculos numéricos pesados, hay que usar arreglos de tipos primitivos (`int[]`, `double[]`).

:::{warning}
La notación `O(...)` es fundamental para entender cómo **escala** un algoritmo cuando `N` tiende a infinito. Pero para `N` chicos o medianos, las constantes físicas del hardware (la caché) dominan el tiempo de ejecución real.
:::
