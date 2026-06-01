---
title: "Profiling"
subtitle: "Medir antes de optimizar"
subject: Estructuras de Datos
description: Cómo usar evidencia empírica para validar decisiones de diseño y encontrar cuellos de botella reales usando técnicas modernas de la JVM, telemetría y análisis de memoria.
---

(parte6-profiling)=
# Profiling

Seguramente escuchaste alguna vez la frase de Donald Knuth: *"La optimización prematura es la raíz de todos los males"*. Esto no significa que el rendimiento no importe, sino que intentar optimizar código sin saber exactamente **qué** está andando lento es una pérdida de tiempo. El **profiling** es la disciplina de usar herramientas de medición para encontrar los cuellos de botella reales en tu programa.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Aprender a recolectar y analizar datos empíricos sobre el rendimiento de un programa para tomar decisiones de diseño basadas en evidencia científica.

**Prerrequisitos.** Haber leído [Localidad de memoria](localidad_memoria.md) y tener conocimientos sólidos de Java y complejidad asintótica.

**Desarrollo.** Veremos las técnicas de instrumentación y muestreo, las optimizaciones agresivas de la JVM (JIT), el uso de JMH para microbenchmarking, el análisis de memoria para detectar fugas (Leaks) y la telemetría con Java Flight Recorder (JFR).
:::

## ¿Qué es el profiling?

A diferencia de la depuración (debugging), que busca errores lógicos, el profiling busca ineficiencias. Un *profiler* es una herramienta que monitorea la ejecución del programa y recolecta métricas sobre:

- **Tiempo de CPU:** ¿En qué métodos pasa el procesador la mayor parte del tiempo?
- **Uso de Memoria (Heap):** ¿Cuántos objetos se están creando? ¿Dónde se están acumulando? ¿Qué tan seguido corre el *Garbage Collector*?
- **I/O y Red:** ¿Estamos esperando demasiado por el disco o la red?

## La trampa del cronómetro

La forma más básica de medir tiempo es usar el reloj del sistema. En Java, solemos usar `System.nanoTime()`.

## Técnicas de Profiling: ¿Cómo mide el software?

Un profiler no es mágico; tiene que "meterse" en tu código para ver qué pasa. Existen dos estrategias principales:

### 1. Instrumentación
El profiler modifica el *bytecode* de tus clases (o inyecta código al compilar) para insertar contadores al inicio y al final de cada método.
- **Ventaja:** Precisión absoluta en el conteo de llamadas.
- **Desventaja:** Gran sobrecosto (*overhead*). El acto de medir cambia el rendimiento del programa (Efecto Observador o "Heisenbug"), lo que puede invalidar los resultados.

### 2. Muestreo (Sampling)
El profiler le pregunta a la JVM en intervalos regulares (ej: cada 10ms): "¿En qué método estás ahora?". 
- **Ventaja:** Bajo sobrecosto, ideal para entornos de producción.
- **Desventaja:** Puede perderse métodos que se ejecutan muy rápido entre muestras. Además, los profileres tradicionales sufren de **Safepoint Bias**: solo pueden tomar muestras cuando la JVM llega a un estado seguro, lo que suele distorsionar los datos hacia métodos que tienen *safepoints* (como lazos largos).

## La JVM: Un blanco móvil

Medir en Java es difícil porque el compilador **JIT (Just-In-Time)** es extremadamente inteligente y agresivo. Si no tenés cuidado, podrías estar midiendo algo que ya no existe.

### Eliminación de Código Muerto (DCE)
Si calculás algo pero no usás el resultado, el JIT detectará que no tiene efectos secundarios y eliminará el código. Tu benchmark dirá que tarda 0 ns.

### Constant Folding
Si el JIT detecta que una expresión siempre da el mismo resultado (ej: `2 + 2`), reemplazará el cálculo por la constante `4` durante la compilación, invalidando tu medición de velocidad de suma.

### Inlining
El JIT puede "pegar" un método chico directamente donde se lo llama. Esto es excelente para el rendimiento, pero confuso si querés medir ese método por separado.

## JMH: El Microbenchmark Harness

Para evitar estas trampas, usamos **JMH**. Es una herramienta que genera código de soporte para asegurar que tus mediciones sean válidas.

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class MiBenchmark {

    @Param({"100", "1000", "10000"})
    public int N;

    private List<Integer> lista;

    @Setup
    public void setup() {
        lista = new ArrayList<>();
        for (int i = 0; i < N; i++) lista.add(i);
    }

    @Benchmark
    public int medirBusqueda() {
        // JMH asegura que el resultado se consuma para evitar DCE
        return lista.indexOf(N - 1);
    }
}
```

## Análisis de Memoria y Memory Leaks

Optimizar no es solo ir rápido; es no quedarse sin memoria. 

### El Heap Dump
Un **Heap Dump** es una captura instantánea de todos los objetos que viven en la memoria RAM en un momento dado. Se genera con herramientas como `jmap` o desde VisualVM.

Para encontrar un **Memory Leak** (fuga de memoria), la estrategia es:
1.  Tomar un Heap Dump base.
2.  Realizar acciones que deberían liberar memoria.
3.  Tomar un segundo Heap Dump.
4.  Comparar (Diff) para ver qué objetos siguen creciendo y quién los está reteniendo (buscando el "GC Root").

### Allocation Pressure
A veces no tenés una fuga, pero creás demasiados objetos temporales. Esto llena los **TLABs (Thread Local Allocation Buffers)** y obliga al Garbage Collector a correr constantemente, pausando tu programa. Esto se visualiza mejor con profileres de asignación.

## Java Flight Recorder (JFR) y JMC

**JFR** es el estándar de oro para telemetría en producción. Viene integrado en la JVM y tiene un impacto casi nulo (< 1%). 
- Recolecta eventos del sistema, del GC, de los hilos y de la aplicación.
- Los archivos `.jfr` se abren con **JDK Mission Control (JMC)** para un análisis profundo.

## Async-Profiler y Flame Graphs

Para aplicaciones grandes, **Async-Profiler** es imbatible. Evita el *Safepoint Bias* y genera **Flame Graphs**.
- **Eje X:** Representa las muestras (ancho = tiempo de CPU consumido).
- **Eje Y:** Representa la profundidad de la pila de llamadas (*stack depth*).

::: {tip} Interpretación
Si ves una caja muy ancha que no tiene casi nada arriba, encontraste un **Hot Spot**: un método que está haciendo mucho trabajo pesado por sí mismo.
:::

## Estrategia de Optimización: El Ciclo Científico

1.  **Macro-profiling (JFR/Async-Profiler):** Mirá el sistema completo. ¿Es CPU, Memoria o Bloqueo de Hilos?
2.  **Identificar el Hot Path:** Seguí la ruta más ancha del Flame Graph.
3.  **Aislar con JMH:** Llevá ese método a un benchmark controlado.
4.  **Hipótesis de Hardware/Algoritmos:** ¿Es una mala complejidad ($O(N^2)$)? ¿Es falta de localidad de memoria?
5.  **Validar:** El cambio debe mejorar los números de JMH **y** achicar la caja en el profiler sistémico.

## Resumen

El profiling no es una opinión, es una medición científica. 
- No confíes en un cronómetro manual; usá **JMH**.
- No adivines qué está lento; usá **Async-Profiler** o **JFR**.
- Antes de cambiar una línea de código, asegurate de tener una **métrica base** (baseline).
- Recordá: el código más rápido es el que **no se ejecuta**.

## Ejercicios

```{exercise}
:label: ex-parte6-profiling-1

Investigá qué es un **GC Root** y por qué es la clave para encontrar el culpable de un Memory Leak en un Heap Dump.
```

```{exercise}
:label: ex-parte6-profiling-2

Explicá la diferencia entre **CPU Time** (tiempo procesando) y **User Time** (tiempo total percibido). ¿Por qué un método que espera un recurso de red podría tener bajo CPU Time pero mucho User Time?
```

```{exercise}
:label: ex-parte6-profiling-3

Descargá **Async-Profiler** y generá un Flame Graph de un programa que realice muchas concatenaciones de Strings (`+=`) en un lazo largo. ¿Qué método de la biblioteca estándar aparece como el más costoso?
```

## Próximo paso

Con las herramientas de medición dominadas, es momento de ver cómo las decisiones de representación impactan en estos números al estudiar las [Secuencias](secuencias/indice.md).
