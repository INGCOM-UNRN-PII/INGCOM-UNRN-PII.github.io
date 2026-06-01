---
title: "Profiling"
subtitle: "Medir antes de optimizar"
subject: Estructuras de Datos
description: Cómo usar evidencia empírica para validar decisiones de diseño y encontrar cuellos de botella reales.
---

(parte6-profiling)=
# Profiling

Seguramente escuchaste alguna vez la frase de Donald Knuth: *"La optimización prematura es la raíz de todos los males"*. Esto no significa que el rendimiento no importe, sino que intentar optimizar código sin saber exactamente **qué** está andando lento es una pérdida de tiempo. El **profiling** es la disciplina de usar herramientas de medición para encontrar los cuellos de botella reales en tu programa.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Aprender a recolectar y analizar datos empíricos sobre el rendimiento de un programa para tomar decisiones de diseño basadas en evidencia.

**Prerrequisitos.** Haber leído [Localidad de memoria](localidad_memoria.md) y tener conocimientos de Java y complejidad asintótica.

**Desarrollo.** Veremos la diferencia entre medir tiempo y medir recursos, cómo realizar mediciones precisas en Java evitando las trampas del JIT y qué herramientas existen para analizar el uso de CPU y memoria.
:::

## ¿Qué es el profiling?

A diferencia de la depuración (debugging), que busca errores lógicos, el profiling busca ineficiencias. Un *profiler* es una herramienta que monitorea la ejecución del programa y recolecta métricas sobre:

- **Tiempo de CPU:** ¿En qué métodos pasa el procesador la mayor parte del tiempo?
- **Uso de Memoria (Heap):** ¿Cuántos objetos se están creando? ¿Dónde se están acumulando? ¿Qué tan seguido corre el *Garbage Collector*?
- **I/O y Red:** ¿Estamos esperando demasiado por el disco o la red?

## La trampa del cronómetro

La forma más básica de medir tiempo es usar el reloj del sistema. En Java, solemos usar `System.nanoTime()`.

```java
long inicio = System.nanoTime();
ejecutarAlgoritmo();
long fin = System.nanoTime();

long duracionMs = (fin - inicio) / 1_000_000;
System.out.println("Duración: " + duracionMs + " ms");
```

::: {warning} Cuidado con el JIT (Just-In-Time Compiler)
Java no interpreta el código de la misma forma todo el tiempo. La JVM analiza qué partes del código se ejecutan más seguido y las compila a código de máquina optimizado "sobre la marcha". Esto significa que la primera vez que ejecutas un método será **mucho más lenta** que la milésima vez. Para medir en serio, necesitás un periodo de **warm-up** (calentamiento).
:::

## Herramientas de Profiling

Existen herramientas profesionales que te permiten visualizar esto sin ensuciar tu código con `nanoTime()`:

1.  **VisualVM:** Viene con el JDK y es excelente para una primera mirada rápida al heap y al uso de CPU.
2.  **JMC (JDK Mission Control):** Una herramienta más avanzada y de bajo impacto para entornos productivos.
3.  **JMH (Java Microbenchmark Harness):** Es el estándar de la industria para escribir microbenchmarks precisos en Java. Se encarga de manejar el warm-up, las optimizaciones del compilador y las mediciones estadísticas por vos.

## Optimización basada en evidencia

Una vez que identificaste el cuello de botella, el proceso es iterativo:

1.  **Medir:** Obtener la métrica base (baseline).
2.  **Hipótesis:** ¿Por qué es lento? (¿Mala complejidad? ¿Mala localidad? ¿Demasiadas asignaciones?).
3.  **Cambiar:** Aplicar una **única** modificación.
4.  **Validar:** Medir de nuevo. Si no mejoró, revertí el cambio.

::: {important} No adivines
Nunca asumas que un cambio "debería ser más rápido". El hardware y los compiladores modernos son tan complejos que a veces una optimización teórica termina haciendo el código más lento. Confiá siempre en los datos.
:::

## Resumen

El profiling es el cable a tierra de la teoría de algoritmos. Nos permite validar si nuestras estructuras de datos se comportan como esperábamos en el mundo real y nos asegura que estamos gastando nuestro esfuerzo de optimización donde realmente importa.

## Ejercicios

```{exercise}
:label: ex-parte6-profiling-1

Escribí un programa simple en Java que compare el tiempo de búsqueda en un `ArrayList` frente a una `LinkedList` de 100.000 elementos. Ejecutá la prueba 10 veces seguidas dentro del mismo programa. ¿Notás alguna diferencia de tiempo entre la primera ejecución y las siguientes? ¿Por qué?
```

```{exercise}
:label: ex-parte6-profiling-2

Investigá qué es el "Escape Analysis" en la JVM y cómo puede afectar el rendimiento de la creación de objetos pequeños en un lazo.
```

```{exercise}
:label: ex-parte6-profiling-3

Descargá y abrí **VisualVM** mientras ejecutás un programa que haga un uso intenso de memoria (por ejemplo, creando millones de strings). Observá el gráfico del "Heap" y tratá de identificar cuándo actúa el Garbage Collector.
```

## Próximo paso

Con las herramientas de medición en mano, estamos listos para explorar la primera gran familia de estructuras: las [Secuencias](secuencias/indice.md).
