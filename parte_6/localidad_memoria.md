---
title: "Localidad de memoria"
subtitle: "El impacto del hardware en el rendimiento real"
subject: Estructuras de Datos
description: Por qué la notación asintótica no cuenta toda la historia y cómo la jerarquía de memoria beneficia a las estructuras contiguas.
---

(parte6-localidad-memoria)=
# Localidad de memoria

Cuando analizamos algoritmos en papel, solemos usar la notación Big-O para estimar el costo. Decimos que acceder a un elemento en un arreglo es $O(1)$ y que recorrer una lista enlazada es $O(N)$. Sin embargo, en una computadora real, no todos los accesos a memoria cuestan lo mismo. El hardware no es una abstracción matemática: tiene una arquitectura física que premia ciertos patrones de acceso y penaliza otros.

Entender la **localidad de memoria** es lo que separa a un programador que solo conoce la teoría de uno que sabe construir sistemas de alto rendimiento.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Comprender cómo la jerarquía de memoria y los patrones de acceso influyen en la velocidad de ejecución de las estructuras de datos.

**Prerrequisitos.** Conocimientos básicos de punteros y arreglos (vistos en la [Parte 1](../parte_1/13_memoria.md)) y nociones de complejidad asintótica.

**Desarrollo.** Exploraremos la jerarquía de memoria, definiremos localidad espacial y temporal, y compararemos el impacto real de usar arreglos frente a estructuras enlazadas.
:::

## La jerarquía de memoria

Tu procesador es increíblemente rápido, pero la memoria RAM es, comparativamente, muy lenta. Si el procesador tuviera que esperar a la RAM por cada dato que necesita, pasaría la mayor parte del tiempo de brazos cruzados. Para evitar esto, los procesadores modernos usan niveles de memoria intermedia llamados **caché** (L1, L2 y L3).

```{mermaid}
graph TD
    CPU[Procesador] <--> L1[Caché L1 - Muy rápida / Muy chica]
    L1 <--> L2[Caché L2 - Rápida / Chica]
    L2 <--> L3[Caché L3 - Lenta / Mediana]
    L3 <--> RAM[Memoria RAM - Muy lenta / Grande]
    
    style CPU fill:#f8f9fa,stroke:#333,stroke-width:2px;
    style RAM fill:#e3f2fd,stroke:#1565c0,stroke-width:2px;
    classDef cache fill:#fff3e0,stroke:#f57c00,stroke-width:2px;
    class L1,L2,L3 cache;
```

Cuando pedís un dato, el procesador lo busca primero en L1. Si no está (**cache miss**), lo busca en L2, y así hasta llegar a la RAM. La diferencia de tiempo es brutal: un acceso a L1 puede tomar 1 nanosegundo, mientras que ir a la RAM puede tomar 100 nanosegundos.

## Localidad temporal y espacial

Para que la caché sea efectiva, el hardware se apoya en dos principios empíricos:

1.  **Localidad Temporal:** Si accediste a un dato hace poco, es muy probable que lo vuelvas a necesitar pronto (pensá en el contador de un lazo).
2.  **Localidad Espacial:** Si accediste a un dato, es muy probable que necesites los datos que están **físicamente cerca** en la memoria.

::: {important} El bloque de caché (Cache Line)
Cuando el procesador trae un dato desde la RAM, no trae solo ese byte. Trae un bloque completo (típicamente de 64 bytes). Si tu estructura de datos aprovecha ese bloque, el rendimiento vuela. Si lo ignora, vas a sufrir constantes *cache misses*.
:::

## Arreglos vs. Listas Enlazadas

Acá es donde la teoría del Big-O se encuentra con la realidad física.

### El arreglo: El rey de la localidad espacial

Como los elementos de un arreglo están contiguos, cuando procesás el primer elemento, el hardware ya cargó los siguientes en la caché. Recorrer un arreglo es una operación "amigable" para el procesador (streaming).

```{mermaid}
block-beta
    columns 8
    block:Arr:8
        A0["[0]"] A1["[1]"] A2["[2]"] A3["[3]"] A4["[4]"] A5["[5]"] A6["[6]"] A7["[7]"]
    end
    style Arr fill:#c8e6c9,stroke:#388e3c
```

### La lista enlazada: El caos de los punteros

En una lista enlazada, cada nodo puede estar en cualquier lugar de la memoria. Para pasar del nodo 1 al nodo 2, el procesador tiene que seguir un puntero. Es muy probable que el nodo 2 no esté en la caché, forzando un viaje costoso a la RAM.

```{mermaid}
block-beta
    columns 8
    block:Heap:8
        N1["Nodo 1"] space:2 N3["Nodo 3"] space:1 N2["Nodo 2"] space:1 N4["Nodo 4"]
    end
    N1 --> N2
    N2 --> N3
    N3 --> N4
    style Heap fill:#ffebee,stroke:#c62828
```

Aun cuando ambas tengan un recorrido de $O(N)$, el arreglo puede ser **órdenes de magnitud** más rápido en la práctica debido a la localidad de memoria.

## ¿Cuándo importa esto?

Si estás manejando unos pocos cientos de elementos, la diferencia es imperceptible. Pero si estás construyendo un motor gráfico, un sistema de base de datos o procesando millones de registros, la localidad de memoria es tu mejor aliada (o tu peor enemiga).

- **Preferí arreglos (ArrayList en Java)** por defecto para recorridos secuenciales.
- **Evitá estructuras "puntero-intensivas"** si el rendimiento es crítico y los datos no cambian de forma errática.
- **Cuidado con las matrices de objetos:** En Java, un arreglo de objetos es en realidad un arreglo de referencias. Tenés localidad para las referencias, pero no necesariamente para los objetos en sí.

## Resumen

La notación asintótica es una guía excelente, pero ignorar cómo funciona el hardware te puede llevar a elegir implementaciones que son teóricamente iguales pero prácticamente muy distintas. La **localidad espacial** es la razón por la cual los arreglos suelen dominar el rendimiento en sistemas reales frente a las listas enlazadas.

## Ejercicios

```{exercise}
:label: ex-parte6-localidad-1

Explicá con tus palabras por qué un `for` que recorre una matriz por filas suele ser más rápido que uno que la recorre por columnas en lenguajes que almacenan matrices en orden de fila (como C o Java).
```

```{exercise}
:label: ex-parte6-localidad-2

Investigá qué es un "Cache Miss" y cómo podés medirlo en Linux usando la herramienta `perf`.
```

```{exercise}
:label: ex-parte6-localidad-3

Si tenés una lista enlazada de 1.000.000 de enteros y un arreglo de 1.000.000 de enteros, ¿cuál creés que ocupará más memoria total y por qué? (Considerá el overhead de los punteros).
```

## Próximo paso

Ahora que sabemos que la realidad puede ser distinta a la teoría, necesitamos aprender a medirla. El siguiente paso es el [Profiling](profiling.md).
