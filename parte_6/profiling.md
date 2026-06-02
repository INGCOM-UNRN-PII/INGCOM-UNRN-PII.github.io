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

**Desarrollo.** Veremos las técnicas de instrumentación y muestreo, las herramientas clave (VisualVM, JMC, JMH), las optimizaciones agresivas de la JVM (JIT) y estrategias para identificar cuellos de botella.
:::

## Técnicas de Profiling: ¿Cómo mide el software?

Un profiler no es mágico; tiene que "meterse" en tu código para ver qué pasa. Existen dos estrategias principales:

### 1. Instrumentación
El profiler modifica el *bytecode* de tus clases para insertar contadores al inicio y al final de cada método.
- **Ventaja:** Precisión absoluta en el conteo de llamadas.
- **Desventaja:** Gran sobrecosto (*overhead*). El acto de medir cambia el rendimiento del programa (Efecto Observador), lo que puede invalidar los resultados.

### 2. Muestreo (Sampling)
El profiler le pregunta a la JVM en intervalos regulares: "¿En qué método estás ahora?". 
- **Ventaja:** Bajo sobrecosto, ideal para entornos de producción.
- **Desventaja:** Puede perderse métodos muy rápidos y sufre de **Safepoint Bias**, lo que puede distorsionar los datos.

## La JVM: Un blanco móvil

Medir en Java es difícil porque el compilador **JIT (Just-In-Time)** es extremadamente inteligente. Si no tenés cuidado, podrías estar midiendo algo que ya no existe debido a:
- **Eliminación de Código Muerto (DCE):** Si calculás algo pero no usás el resultado, el JIT elimina el código.
- **Constant Folding:** Si el resultado es predecible (ej: `2+2`), el JIT lo reemplaza por una constante.
- **Inlining:** El JIT "pega" métodos chicos directamente en el lugar de llamada.

---

## Herramientas Esenciales del Ecosistema

Para no caer en estas trampas y obtener datos útiles, el ecosistema Java ofrece tres herramientas fundamentales que debés conocer:

### 1. VisualVM: Monitoreo en Vivo y Quick-Analysis
**VisualVM** es la herramienta "navaja suiza" para el desarrollo diario. Te permite conectarte a una aplicación en ejecución y observar:
- **Uso de Heap:** Ver en tiempo real cómo crece la memoria y cuándo actúa el GC.
- **Threads:** Identificar hilos bloqueados (*deadlocks*).
- **Sampler:** Obtener un perfil rápido de CPU o Memoria sin configuración compleja.
- **Heap Dumps:** Capturar el estado de la memoria para buscar fugas (*leaks*).

### 2. JDK Mission Control (JMC) y Flight Recorder (JFR)
Esta es la artillería pesada para entornos de producción. **Java Flight Recorder (JFR)** viene integrado en la JVM y recolecta telemetría con un impacto casi nulo (< 1%).
- Los archivos `.jfr` se abren con **JDK Mission Control (JMC)**.
- Permite analizar eventos de latencia del sistema, pausas de GC y bloqueos de red con un detalle que ninguna otra herramienta alcanza.

### 3. Java Microbenchmark Harness (JMH)
Cuando necesitás medir una estructura de datos o un algoritmo pequeño de forma aislada, **JMH** es la única opción científica. Es un framework que genera código de soporte para:
- Manejar el **Warm-up** (calentamiento) necesario para el JIT.
- Evitar la **DCE** mediante el uso de `Blackhole`.
- Proveer estadísticas rigurosas (media, varianza, percentiles).

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class MiBenchmark {
    @Benchmark
    public void medir(Blackhole bh) {
        // bh.consume() evita que el JIT elimine este código
        bh.consume(miAlgoritmo());
    }
}
```

---

## Visualización Avanzada: Flame Graphs

Para aplicaciones grandes, herramientas como **Async-Profiler** generan **Flame Graphs**.
- **Eje X:** Representa las muestras (ancho = tiempo de CPU).
- **Eje Y:** Representa la profundidad de la pila de llamadas (*stack trace*).

::: {tip} Interpretación
Buscá las cajas más anchas que no tengan nada arriba: esos son tus **Hot Spots** (métodos que hacen mucho trabajo por sí mismos).
:::

## Estrategia de Optimización: El Ciclo Científico

1.  **Macro-profiling (VisualVM/JFR):** Mirá el sistema completo. ¿Es CPU o Memoria?
2.  **Identificar el Hot Path:** Seguí la ruta más ancha del Flame Graph.
3.  **Aislar con JMH:** Llevá ese método a un benchmark controlado.
4.  **Hipótesis:** ¿Es mala complejidad ($O$) o mala localidad de memoria?
5.  **Validar:** El cambio debe mejorar los números de JMH **y** la telemetría del sistema completo.

## Resumen

El profiling no es adivinar, es medir científicamente. 
- Usá **VisualVM** para una primera mirada rápida.
- Usá **JMC/JFR** para telemetría profunda en producción.
- Usá **JMH** para validar micro-optimizaciones algorítmicas.
- Recordá: si no tenés una **métrica base**, no estás optimizando, estás probando suerte.

## Ejercicios

```{exercise}
:label: ex-parte6-profiling-1
Investigá cómo abrir **VisualVM** (suele estar en el bin del JDK o como descarga aparte). Conectalo a un programa que esté corriendo y realizá un "Sampler" de CPU. ¿Qué método de tu código aparece al tope?
```

```{exercise}
:label: ex-parte6-profiling-2
Buscá en la documentación de **JMH** la diferencia entre `@Warmup` y `@Measurement`. ¿Por qué es un error medir sin una fase de calentamiento previa?
```

```{exercise}
:label: ex-parte6-profiling-3
Explicá por qué **JDK Mission Control** es preferible a VisualVM para diagnosticar problemas que solo ocurren en servidores de producción bajo mucha carga.
```

## Próximo paso

Con las herramientas dominadas, es momento de ver cómo las decisiones de representación impactan en estos números al estudiar las [Secuencias](secuencias/indice.md).
