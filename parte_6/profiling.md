---
title: "Benchmark y Profiling"
subtitle: "Medir rendimiento en el mundo real"
subject: Estructuras de Datos
description: Separar la teoría de la práctica usando herramientas de medición reales, entendiendo el rol de la JVM, JMH y VisualVM.
---

(parte5-profiling)=
# Benchmark y Profiling

Saber que un algoritmo es `O(n log n)` es el primer paso. El segundo paso es validar que la implementación en código no esté introduciendo cuellos de botella por mala gestión de memoria, creación excesiva de objetos o bloqueos. Para eso se usa **Profiling** y **Benchmarking**.

- **Profiling:** Es mirar *adentro* del programa mientras corre (ej. VisualVM) para encontrar "puntos calientes" (hotspots), fugas de memoria o hilos trabados.
- **Benchmarking:** Es medir *desde afuera* cuánto tarda en ejecutarse una porción de código (ej. JMH) bajo condiciones controladas.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Aprender por qué no alcanza con usar `System.currentTimeMillis()` para medir rendimiento en Java y qué herramientas estándar usa la industria.

**Prerrequisitos.** Conviene entender primero el costo asintótico en [Análisis de algoritmos](algoritmos.md) y el impacto del hardware en [Localidad de memoria](localidad_memoria.md).

**Desarrollo.** El capítulo derriba el mito del cronómetro manual, explica las optimizaciones de la JVM (Warm-up, JIT) e introduce VisualVM y JMH de forma conceptual.
:::

## La trampa de System.currentTimeMillis()

El instinto básico de todo programador para medir rendimiento es este:

```java
long inicio = System.currentTimeMillis();
miAlgoritmo();
long fin = System.currentTimeMillis();
System.out.println("Tardó: " + (fin - inicio) + " ms");
```

En Java (y lenguajes modernos sobre máquinas virtuales), **esta métrica es inútil**.

### Por qué falla el cronómetro ingenuo

1. **JIT Compilation (Just-In-Time):** La JVM arranca interpretando el código (lento). Cuando detecta que un método se ejecuta muchas veces ("se calienta" o *warm-up*), el compilador JIT lo traduce a código de máquina nativo ultra-optimizado. El cronómetro manual suele medir el código frío no optimizado.
2. **Garbage Collection (GC):** Si durante `miAlgoritmo()` la memoria se llena y el GC se dispara, el cronómetro va a sumar el tiempo del GC al algoritmo, ensuciando la medición.
3. **Dead Code Elimination:** Si tu algoritmo calcula un resultado pero nunca lo usás, la JVM (que es muy inteligente) puede simplemente borrar todo tu algoritmo en tiempo de ejecución porque "no hace nada útil". El cronómetro va a medir 0 ms.

## Microbenchmarking correcto: JMH

Para medir milisegundos o nanosegundos de forma confiable, el estándar de la industria en Java es **JMH (Java Microbenchmark Harness)**.

JMH se encarga de aislar el ruido:

- Ejecuta iteraciones de **warm-up** previas a la medición para asegurarse de que el compilador JIT ya optimizó el código.
- Obliga a consumir los resultados (usando los famosos "Blackholes" de JMH) para que el compilador no borre el código asumiendo que es inútil.
- Ejecuta múltiples iteraciones (forks) para dar un promedio estadístico estable.

En esta materia, si tenés que comparar si tu implementación de lista enlazada es más rápida que el `ArrayList` estándar, un test de JMH es la única métrica indiscutible.

## Profiling: Encontrar el Cuello de Botella

Si el benchmark de JMH te dice *cuánto* tarda, el **profiling** te dice *por qué* tarda.

Para esto se usan herramientas gráficas como **VisualVM** (que viene incluida históricamente con el JDK o se descarga gratis).

¿Qué se busca en VisualVM?

1. **CPU Profiling:** Muestra qué métodos específicos están consumiendo más tiempo de CPU. Si tu implementación de un árbol tarda mucho, el CPU profiler te puede indicar que el 90% del tiempo se está gastando en un método `rebalancear()` ineficiente.
2. **Memory Profiling (Heap Walker):** Analiza la memoria RAM en tiempo real. Es vital para detectar:
   - **Object Churn:** Si estás creando miles de objetos temporales dentro de un bucle `while` que obligan al Garbage Collector a trabajar sin parar.
   - **Memory Leaks:** Si estás manteniendo referencias a nodos viejos que ya no se usan, impidiendo que se libere la memoria.

### Perfilando Estructuras de Datos

Cuando implementes tu propia tabla hash o árbol balanceado, una buena práctica es:

1. Escribir un pequeño `main` que inserte un millón de elementos en un bucle infinito (o muy largo).
2. Conectar VisualVM a ese proceso de Java.
3. Observar la pestaña de Memoria. ¿El uso de memoria tiene forma de sierra regular (crea basura, limpia basura, crea basura...) o forma de escalera ascendente (fuga de memoria)?
4. Activar el CPU Sampler. ¿El tiempo está donde esperabas (ej. calculando el Hash) o perdiendo el tiempo redimensionando arreglos innecesariamente?

## Resumen

- Medir código con cronómetros básicos engaña debido a las optimizaciones dinámicas de la JVM.
- Para medir **tiempo exacto**, usá **JMH** que controla el *warm-up* y previene el *dead code elimination*.
- Para entender **causas de lentitud**, conectá **VisualVM** y observá la distribución de tiempo en la CPU y la creación de objetos en la memoria.
- La complejidad teórica (`O(n)`) dice cómo escala el algoritmo en papel; el Profiling y Benchmarking revelan su comportamiento bajo estrés físico real.
