---
title: "Localidad de memoria"
subtitle: "El impacto del hardware en el rendimiento real"
subject: Estructuras de Datos
description: Por qué la notación asintótica no cuenta toda la historia y cómo la jerarquía de memoria beneficia a las estructuras contiguas frente a las enlazadas.
---

(parte6-localidad-memoria)=
# Localidad de memoria

Cuando analizamos algoritmos en el pizarrón, solemos usar la notación Big-O para estimar el costo. Decimos que acceder a un elemento en un arreglo es $O(1)$ y que recorrer una lista enlazada es $O(N)$. Sin embargo, en una computadora real, no todos los accesos a memoria cuestan lo mismo. El hardware no es una abstracción matemática: tiene una arquitectura física que premia ciertos patrones de acceso y penaliza otros.

Entender la **localidad de memoria** es lo que separa a un programador que solo conoce la teoría de uno que sabe construir sistemas de alto rendimiento. En este capítulo, vamos a ver por qué "estar cerca" en la memoria es tan importante como tener un algoritmo eficiente.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Comprender cómo la jerarquía de memoria y los patrones de acceso influyen en la velocidad de ejecución real de las estructuras de datos.

**Prerrequisitos.** Conocimientos básicos de punteros y arreglos (vistos en la [Parte 1](../parte_1/13_memoria.md)) y nociones de complejidad asintótica.

**Desarrollo.** Exploraremos la jerarquía de memoria y sus latencias, definiremos los principios de localidad, analizaremos el impacto del diseño de objetos en Java (incluyendo su anatomía interna) y el rol del Garbage Collector en la localidad.
:::

## La jerarquía de memoria y el "Muro de la Memoria"

Tu procesador es increíblemente rápido, pero la memoria RAM es, comparativamente, muy lenta. Esta brecha de velocidad se conoce como el **Memory Wall**. Si el procesador tuviera que esperar a la RAM por cada dato que necesita, pasaría el 99% de su tiempo de brazos cruzados.

Para mitigar esto, los procesadores usan niveles de memoria intermedia llamados **caché** (L1, L2 y L3). El costo de acceso se mide en **ciclos de reloj**:

| Nivel | Capacidad típica | Latencia (aprox. en ciclos) | Analogía |
| :--- | :--- | :--- | :--- |
| **Registros CPU** | Bytes | < 1 ciclo | En la mano |
| **Caché L1** | KB | ~4 ciclos | En el bolsillo |
| **Caché L2** | KB - MB | ~12 ciclos | En el cajón del escritorio |
| **Caché L3** | MB | ~40 ciclos | En el estante de la oficina |
| **RAM (Main Memory)** | GB | **~200 ciclos** | En el depósito al fondo del edificio |

```{mermaid}
graph TD
    CPU[Procesador] <--> L1[L1: 4 ciclos]
    L1 <--> L2[L2: 12 ciclos]
    L2 <--> L3[L3: 40 ciclos]
    L3 <--> RAM[RAM: 200 ciclos]
    
    style CPU fill:#f8f9fa,stroke:#333,stroke-width:2px;
    style RAM fill:#e3f2fd,stroke:#1565c0,stroke-width:2px;
    classDef cache fill:#fff3e0,stroke:#f57c00,stroke-width:2px;
    class L1,L2,L3 cache;
```

Un solo *cache miss* (no encontrar el dato en la caché y tener que ir a buscarlo a la RAM) puede costar tanto como ejecutar cientos de instrucciones simples.

## Los dos principios de localidad

Para que la caché sea efectiva, el hardware se apoya en dos comportamientos observados en casi todos los programas:

1.  **Localidad Temporal:** Si accediste a una variable recién, es muy probable que la vuelvas a usar pronto. El hardware mantiene esos datos en L1/L2.
2.  **Localidad Espacial:** Si accediste a un dato, es muy probable que necesites los datos que están **físicamente pegados** en la memoria.

::: {important} La línea de caché (Cache Line)
Cuando el procesador trae un dato desde la RAM, no trae solo los 4 u 8 bytes que pediste. Trae un bloque completo llamado **Cache Line** (típicamente de 64 bytes). Si tu estructura de datos es contigua, al traer el primer elemento, el hardware "te regala" los siguientes elementos que entren en esos 64 bytes gratis en la caché.
:::

## Prefetching y Pipelining
Los procesadores modernos no son pasivos. Tienen una unidad llamada **Prefetcher** que observa los patrones de acceso. Si detecta que estás recorriendo memoria de forma lineal (como en un `for` sobre un arreglo), se anticipa y pide los siguientes bloques a la RAM antes de que el programa los solicite. Esto "oculta" la latencia de la RAM y hace que el código parezca mucho más rápido de lo que físicamente es.

## Anatomía de un objeto en Java: El costo del "Bloat"

En Java, un objeto no es solo la suma de sus campos. Cada objeto tiene un **Header** (encabezado) que consume memoria y afecta la localidad espacial.

En una JVM típica de 64 bits:
- **Mark Word (8 bytes):** Hash code, bits de bloqueo, estado del Garbage Collector.
- **Class Pointer (4-8 bytes):** Puntero a la metadata de la clase.
- **Padding:** Relleno para que el objeto ocupe un múltiplo de 8 bytes (alineación).

### Ejemplo: El costo de un `Integer`
Un `int` primitivo ocupa **4 bytes**.
Un objeto `Integer` ocupa:
- 12 bytes de header + 4 bytes de dato = **16 bytes**.
- ¡Es 4 veces más grande y requiere un salto de puntero adicional!

Esto explica por qué un `int[]` es masivamente más eficiente que un `ArrayList<Integer>`. El segundo no solo rompe la localidad espacial (los objetos están dispersos), sino que consume mucha más memoria, lo que llena la caché más rápido con datos "inútiles" (headers).

## El rol del Garbage Collector en la localidad

Podrías pensar que las listas enlazadas son siempre un desastre en términos de localidad. Sin embargo, el **Garbage Collector (GC)** de Java puede ayudar. 

Muchos GCs modernos son **compactadores**: cuando limpian la memoria, mueven los objetos que sobrevivieron para que queden contiguos. Si creaste todos los nodos de una lista al mismo tiempo, es probable que el GC los termine ubicando cerca en el *heap*, mejorando un poco la localidad espacial frente a una lista que fue creciendo lentamente a lo largo de horas.

## Matrices y el orden de recorrido

En Java, una matriz `int[filas][columnas]` es un arreglo de arreglos. Esto se llama **Row-Major Order** (aunque técnicamente Java permite "arreglos irregulares", el layout estándar sigue esta lógica).

Si recorrés por **filas**, aprovechás la línea de caché cargada:
```java
for (int i = 0; i < filas; i++) {
    for (int j = 0; j < columnas; j++) {
        suma += matriz[i][j]; // Acceso contiguo
    }
}
```

Si recorrés por **columnas**, en cada paso saltás a una dirección de memoria lejana, forzando un *cache miss* casi garantizado en cada iteración:
```java
for (int j = 0; j < columnas; j++) {
    for (int i = 0; i < filas; i++) {
        suma += matriz[i][j]; // Cache Miss Infierno
    }
}
```

## Sabías que...? False Sharing
Incluso si tus datos están bien organizados, podés tener problemas de rendimiento en entornos multihilo. Si dos hilos modifican variables distintas que casualmente caen en la **misma línea de caché**, el hardware invalidará la caché del otro procesador constantemente. Esto se llama **False Sharing** y es una de las fuentes de bugs de rendimiento más difíciles de detectar.

## El futuro: Proyecto Valhalla
La comunidad de Java está trabajando en el **Proyecto Valhalla**, que permitirá crear **Value Types** (o *Inline Types*). Esto permitirá tener objetos (como un `Complex` o un `Point`) que se comporten como primitivos: sin headers y almacenados contiguamente en arreglos, dándonos lo mejor de la Localidad Espacial y la Orientación a Objetos.

## Resumen

La eficiencia real depende de la **Simpatía Mecánica** (Mechanical Sympathy): diseñar software que trabaje a favor del hardware, no en su contra.
- Los **arreglos de primitivos** minimizan el overhead y maximizan la localidad.
- El **prefetcher** ama el acceso lineal y odia los saltos de punteros.
- Entender el **layout de memoria** de la JVM te permite predecir por qué una estructura será más lenta que otra a pesar de tener la misma complejidad teórica.

## Ejercicios

```{exercise}
:label: ex-parte6-localidad-header

Calculá cuánto espacio ocupa un arreglo de 100 objetos `Point { int x, y; }` en una JVM de 64 bits (con punteros comprimidos, el header ocupa 12 bytes). Comparalo con un `int[200]` donde guardes las coordenadas alternadas.
```

```{exercise}
:label: ex-parte6-localidad-recorrido

¿Por qué se dice que una estructura `List<Integer>` es "puntero-intensiva" y cómo afecta esto a la Localidad Temporal?
```

```{exercise}
:label: ex-parte6-localidad-valhalla

Investigá brevemente el Proyecto Valhalla. ¿Cómo resolvería el problema de la "indirección" en los arreglos de objetos?
```

## Próximo paso

Con las herramientas de medición en mano, es hora de validar estas teorías en la práctica con [Profiling](profiling.md).
