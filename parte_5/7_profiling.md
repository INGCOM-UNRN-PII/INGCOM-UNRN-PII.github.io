---
title: "Profiling"
subtitle: "Medir antes de optimizar"
subject: Estructuras de Datos
description: Cómo usar evidencia empírica para validar decisiones de diseño y encontrar cuellos de botella reales usando técnicas modernas de la JVM, telemetría y análisis de memoria.
---

(parte5-profiling)=
# Profiling: El Arte y la Ciencia de la Medición de Rendimiento

Seguramente escuchaste alguna vez la frase de Donald Knuth: *"La optimización prematura es la raíz de todos los males"*. Pero ojo, que muchas veces se malinterpreta: no significa que el rendimiento no importe o que puedas escribir código ineficiente "porque total después se arregla". Significa que intentar optimizar código basándote en la intuición, sin saber exactamente **qué** está andando lento, es la forma más rápida de perder el tiempo, introducir bugs sutiles y arruinar la mantenibilidad de un sistema.

El **profiling** es la disciplina de usar evidencia empírica y herramientas de medición de precisión para encontrar los cuellos de botella reales. En sistemas modernos, y especialmente en entornos gestionados como la JVM, la realidad suele ser contraintuitiva. Un lazo que parece lento puede ser optimizado a nada por el compilador, mientras que una simple asignación de objeto puede estar gatillando pausas de milisegundos que destruyen tu latencia de cola.

En este capítulo, vamos a profundizar en el arte de la medición. No nos vamos a limitar a "correr una herramienta y mirar colores". Vamos a entender la estadística detrás de los percentiles, la física detrás de los cache misses y la magia negra del compilador JIT. El objetivo es que dejes de adivinar y empieces a saber.

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, se espera que estés en condiciones de:

1.  **Dominar el Método Científico de Optimización:** Formular hipótesis basadas en datos y validarlas con experimentos controlados.
2.  **Entender las Leyes de Escalado:** Aplicar Amdahl, Gustafson y la Ley de Little para predecir si una optimización vale el esfuerzo.
3.  **Desmitificar la JVM:** Comprender cómo el JIT (C1, C2, OSR) y el Garbage Collector interactúan con tus mediciones.
4.  **Ejecutar Microbenchmarks Rigurosos:** Usar JMH evitando las trampas de Dead Code Elimination, Constant Folding y calentamiento.
5.  **Interpretar Telemetría Avanzada:** Analizar Flame Graphs, eventos de JFR y contadores de hardware (L1/L3 misses).
6.  **Diagnosticar Memoria a Escala:** Identificar Memory Leaks, Allocation Pressure y el impacto de los TLABs.
:::

## 1. Filosofía y Fundamentos: El método científico en la optimización

Optimizar no es un acto de inspiración divina; es un proceso puramente experimental. Si cambiás una línea de código porque "te parece que así va a ser más rápido", no sos un ingeniero, sos un alquimista. Pero para ser un ingeniero de rendimiento, primero tenés que entender las leyes físicas y matemáticas que gobiernan el escalado.

### Las Leyes del Escalado: ¿Por qué no podés comprar rendimiento infinito?

Cuando te enfrentás a un sistema lento, la primera reacción suele ser "tirarle más hardware". Si un hilo tarda 10 segundos, ponemos 10 hilos y debería tardar 1 segundo, ¿no? La realidad te va a demostrar que esto casi nunca es así.

#### Ley de Amdahl: El techo de cristal de la serialización

La Ley de Amdahl describe el incremento de velocidad esperado de un sistema cuando solo una parte del mismo es mejorada. Es, en esencia, la ley de los rendimientos decrecientes.

**Derivación Matemática:**
Imaginá que un programa tarda un tiempo total $T$ en ejecutarse. Este tiempo se divide en dos partes: una fracción $P$ que puede ser paralelizada (u optimizada) y una fracción secuencial $(1-P)$ que debe ejecutarse de forma estrictamente serial (por ejemplo, escribir en un archivo, esperar un lock o la fase final de reducción de un algoritmo).

Si aplicamos una mejora de factor $N$ (como tener $N$ núcleos) a la parte paralelizable, el nuevo tiempo de ejecución $T(N)$ será:

$$T(N) = T \times \left( (1-P) + \frac{P}{N} \right)$$

El **Speedup** $S(N)$ se define como la relación entre el tiempo original y el tiempo mejorado:

$$S(N) = \frac{T}{T(N)} = \frac{1}{(1-P) + \frac{P}{N}}$$

Si llevamos el número de núcleos al infinito ($N \to \infty$), el término $\frac{P}{N}$ tiende a cero, y nos queda el **límite teórico máximo de speedup**:

$$S_{max} = \frac{1}{1-P}$$

**Análisis de Speedup según la fracción paralelizable ($P$):**

| Núcleos ($N$) | $P = 0.50$ | $P = 0.75$ | $P = 0.90$ | $P = 0.95$ | $P = 0.99$ |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **2** | 1.33x | 1.60x | 1.82x | 1.90x | 1.98x |
| **10** | 1.82x | 3.08x | 5.26x | 6.89x | 9.17x |
| **100** | 1.98x | 3.92x | 9.17x | 16.81x | 50.25x |
| **1000** | 1.99x | 3.99x | 9.91x | 19.64x | 90.99x |
| **$\infty$** | **2.00x** | **4.00x** | **10.00x** | **20.00x** | **100.00x** |

**Conclusión existencial:** Si tu código tiene un 5% de lógica secuencial (un simple `synchronized` global, por ejemplo), no importa si tenés una supercomputadora de la NASA con 1 millón de núcleos; tu programa nunca irá más de 20 veces más rápido que en un solo núcleo. La Ley de Amdahl te obliga a atacar la parte serial del código antes de intentar paralelizar.

#### Ley de Gustafson: El enfoque del optimista (Fixed-Time Speedup)

Mientras Amdahl asume que el tamaño del problema es fijo, la Ley de Gustafson observa que, en la práctica, cuando tenemos más hardware, no queremos resolver el mismo problema más rápido, sino resolver un problema mucho más grande en el mismo tiempo.

$$S_{scaled}(N) = (1-P) + N \times P$$

Acá el speedup es lineal con respecto a $N$. Gustafson es la razón por la que el Big Data existe: si tengo 10 veces más datos, pongo 10 veces más nodos y mantengo la latencia constante. Amdahl mide cuán rápido corrés una carrera de 100 metros; Gustafson mide cuánta carga podés llevar en un camión si tenés una flota más grande.

#### Ley de Little: El equilibrio del sistema

Esta ley es fundamental para el dimensionamiento de servidores. Relaciona el número promedio de elementos en un sistema ($L$), la tasa de llegada ($\lambda$) y el tiempo promedio que cada elemento pasa en el sistema ($W$):

$$L = \lambda \times W$$

Si querés bajar la cantidad de memoria usada por peticiones pendientes ($L$), tenés solo dos caminos: o bajás la latencia de procesamiento ($W$) o limitás la tasa de entrada ($\lambda$). Si tu latencia sube por un microbloqueo, la memoria consumida por peticiones pendientes saldrá de control hasta que el servidor explote (OOM).

### Latencia de Cola: Por qué el promedio es tu peor enemigo

En sistemas a gran escala, el promedio no tiene valor estadístico. Lo que destruye la experiencia del usuario es la **latencia de cola (Tail Latency)**.

#### El Efecto Multiplicativo de las Dependencias

Imaginá que tu microservicio "A" llama a 10 servicios en paralelo para construir una respuesta. Cada uno de esos servicios tiene un **p99** de 1ms (es decir, el 1% de las veces tarda más de 1ms). ¿Cuál es la probabilidad de que tu servicio "A" experimente una demora?

$$P(\text{éxito}) = 0.99^{10} \approx 0.90$$

¡Eso significa que el 10% de tus usuarios experimentarán latencia alta! Si tu arquitectura tiene 100 dependencias, la probabilidad de que una respuesta sea lenta es de casi el 65%, incluso si cada dependencia individual es "rápida" el 99% del tiempo. Por eso, en sistemas críticos, medimos el **p99.9** o el **p99.99**. Un sistema que es consistentemente mediocre es preferible a uno que es rapidísimo el 95% del tiempo pero que tiene "sollozos" (hiccups) aleatorios de 1 segundo.

### Mechanical Sympathy: Entendiendo el "fierro"

El término fue popularizado por Martin Thompson (creador del LMAX Disruptor), inspirado por el piloto de F1 Jackie Stewart, quien decía que para ser un gran piloto no necesitabas ser ingeniero mecánico, pero sí tener "simpatía mecánica": entender cómo funciona el auto para no destruirlo.

En software, esto significa entender que:
1.  **La memoria no es plana:** La diferencia entre leer de la caché L1 y la RAM es de 2 órdenes de magnitud (1ns vs 100ns). Un algoritmo $O(N^2)$ que cabe en L1 puede ser más rápido que uno $O(N)$ que genera cache misses constantes.
2.  **Las CPUs son predictivas:** Si tenés un `if` que cambia de resultado aleatoriamente, el "Branch Predictor" fallará, vaciando el pipeline de ejecución y desperdiciando ciclos.
3.  **Los punteros son caros:** En la JVM, cada puntero a un objeto es un salto potencial a una dirección de memoria aleatoria (Pointer Chasing). Los arreglos de primitivos son los mejores amigos del hardware.

---

## 2. Ejecución en la JVM: El problema del blanco móvil

Medir en Java es una pesadilla comparado con lenguajes estáticos como C o Rust. En la JVM, el código que escribiste no es el código que se ejecuta.

### El Pipeline del Compilador C2 (Server Compiler)

El compilador C2 es el corazón del rendimiento en Java. No es un compilador estático; es un optimizador dinámico que utiliza datos de ejecución en tiempo real (PGO - Profile Guided Optimization).

El proceso de C2 sigue estas fases críticas:

1.  **Bytecode Parsing:** Se traduce el bytecode de Java a una **Representación Intermedia (IR)** llamada "Ideal Graph". Es un grafo de flujo de datos y control (Sea of Nodes).
2.  **Inlining (La madre de todas las optimizaciones):** El JIT decide qué llamadas a métodos "pegar" dentro del llamador. Si el método es pequeño o se llama mucho, se elimina la llamada física. Esto es vital porque permite que el JIT vea "más allá" y optimice el flujo de datos.
3.  **Global Value Numbering (GVN):** Elimina cálculos redundantes. Si hacés `x + y` dos veces y nada cambió en el medio, C2 lo hace una sola vez.
4.  **Escape Analysis (EA):** C2 determina si un objeto "escapa" del método actual. Si el objeto no sale del método, C2 puede:
    *   **Scalar Replacement:** No crear el objeto en el Heap y usar registros de CPU para sus campos.
    *   **Lock Elision:** Eliminar `synchronized` si sabe que nadie más verá ese objeto.
5.  **Loop Optimizations:** Unrolling (repetir el cuerpo del lazo para evitar el salto del `for`), Range Check Elimination (eliminar validaciones de límites de arreglos si puede probar que i siempre es menor que `length`).
6.  **Code Generation:** Traducir el Ideal Graph a instrucciones de máquina (x86_64, ARM64, etc.).

### Inlining y la Contaminación de Sitios de Llamada (Call Site Pollution)

El Inlining depende de la jerarquía de clases y de lo que el JIT haya visto en el "warm-up".

*   **Monomorphic Inlining:** El JIT ve que en un sitio de llamada siempre se usa la clase `A`. Compila una llamada directa. Es rapidísimo.
*   **Bimorphic Inlining:** Se usan dos clases (`A` y `B`). El JIT genera un `if (clase == A) ... else if (clase == B) ...`. Todavía es aceptable.
*   **Megamorphic Call:** Hay 3 o más clases posibles en el mismo sitio. El JIT se rinde, no puede inlinear y tiene que hacer un "Virtual Method Table Lookup" (VMT). Esto es 10 veces más lento que una llamada inlined.

**Call Site Pollution:** Si tenés una interfaz y en un punto del código pasás 10 implementaciones distintas, estás "contaminando" ese sitio de llamada y privando al JIT de optimizarlo. A veces, duplicar un poco de código para que cada sitio de llamada sea monomórfico puede duplicar el throughput.

### Vectorización Automática (SIMD)

Las CPUs modernas tienen instrucciones **SIMD (Single Instruction, Multiple Data)** como AVX2 o AVX-512. Permiten, por ejemplo, sumar 8 números `double` en un solo ciclo de reloj usando registros de 512 bits.

La JVM intenta hacer esto automáticamente mediante un proceso llamado **SuperWord Optimization**.
Si tenés un lazo simple:
```java
for (int i = 0; i < a.length; i++) {
    c[i] = a[i] + b[i];
}
```
El JIT puede transformar esto en código que procesa 4, 8 o 16 elementos por vez. Pero ojo: cualquier complejidad dentro del lazo (un `if`, una llamada a otro método, un acceso a memoria no contiguo) desactivará la vectorización.

### Interpretación de `-XX:+PrintAssembly` y `-XX:+LogCompilation`

Para los cirujanos del rendimiento, las herramientas de alto nivel no bastan. Necesitamos ver qué código nativo generó el JIT.

*   **`-XX:+PrintAssembly`:** Requiere la librería `hsdis`. Muestra el código ensamblador real que la CPU está ejecutando. Acá podés verificar si el JIT usó registros de 128 bits o si está haciendo chequeos de nulidad innecesarios.
*   **`-XX:+LogCompilation`:** Genera un XML gigante con todas las decisiones del JIT. Herramientas como **JITWatch** permiten visualizar este log y entender por qué un método NO fue inlined (ej: "too big", "already compiled").

### De-optimización: El costo de equivocarse

El JIT es un apostador. Si ve que en 10.000 ejecuciones un puntero nunca fue `null`, compila el código **asumiendo** que no es `null`. Pero si de repente llega un `null`, la CPU no puede seguir.

Se produce una **De-optimización** (Uncommon Trap):
1.  La ejecución se detiene en seco.
2.  Se descarta el código nativo optimizado.
3.  El estado del hilo se "desenrolla" y vuelve al **Intérprete**.
4.  La JVM anota que "la suposición de no-null falló" y, después de un tiempo, volverá a compilar el método con un chequeo de nulidad explícito.

**Uncommon Traps comunes en producción:**
*   `class_check`: Llegó una clase que el JIT no esperaba en un sitio de llamada monomórfico.
*   `unstable_if`: Se tomó una rama del `if` que nunca se había tomado antes.
*   `null_check`: Apareció un nulo donde no solía haberlo.

::: {warning} Moraleja para el Profiling
Un sistema que sufre constantes de-optimizaciones tendrá una latencia errática. Esto suele pasar cuando el entorno de "Staging" (donde se calienta el JIT) tiene datos muy distintos a los de "Producción". **Siempre perfilá con datos que representen la realidad caótica del mundo real.**
:::

---

## 3. INVESTIGACIÓN SISTEMÁTICA (CASO DE ESTUDIO): Crónica de una optimización extrema

Imaginá que sos el responsable de un motor de búsqueda de patrones en logs financieros (FIX Protocol). El sistema actual está sufriendo para procesar el pico de transacciones del mediodía. Vamos a seguir este proceso paso a paso, como si estuviéramos en la trinchera.

### Escena 1: El Diagnóstico del Paciente (Baseline)

El sistema recibe un stream de mensajes de texto y debe extraer el `OrderID` y el `Price`.

**Enzo (Senior):** "Nico, el sistema está al 95% de CPU y el p99 se fue a 2 segundos. Los clientes están gritando. ¿Qué tenemos?"
**Nico (Junior):** "Mirá, el código es simple. Usamos Jackson para el JSON y una Regex para validar el ID. Mirá el baseline:"

```java
// VERSION 0: El "Baseline" Ingenuo
public void processMessage(String rawJson) {
    // 1. Deserialización completa (Muy caro)
    Map<String, Object> map = objectMapper.readValue(rawJson, Map.class);
    
    // 2. Acceso por String key (Genera Hash y búsqueda)
    String orderId = (String) map.get("orderId");
    
    // 3. Validación con Regex (Backtracking potencial)
    if (orderId.matches("ORD-[0-9]{8}")) { 
        // 4. Conversión de tipo (Crea objetos Double)
        double price = Double.parseDouble(map.get("price").toString());
        save(orderId, price);
    }
}
```

Lanzamos el JFR y abrimos el **Java Mission Control (JMC)**.
**JMC nos dice:**
- **Hot Methods:** `java.util.regex.Pattern.matcher()` consume el 40% del tiempo de CPU.
- **Allocation:** `java.util.LinkedHashMap` (creado por Jackson) genera 1.5 GB de basura por segundo.
- **GC:** El recolector G1 está haciendo pausas de 400ms cada 2 segundos.

**Captura del Reporte JMC:**
```text
[EVENT] jdk.ObjectAllocationInNewTLAB
  - Class: java.util.LinkedHashMap$LinkedHashMapEntry
  - Count: 12,450,000 samples
  - Allocation Rate: 1.48 GB/s
[EVENT] jdk.ExecutionSample
  - Method: java.util.regex.Pattern.match(Pattern.java:1234)
  - CPU: 42.5%
```

**Investigación de Nico:** "Che, Enzo, el JFR dice que pasamos el 20% del tiempo solo en `String.hashCode()` cuando buscamos en el Map. ¿Por qué Jackson es tan pesado?"
**Enzo:** "Porque Jackson tiene que crear un mapa de objetos `String` a objetos `Object` por cada mensaje. Si el mensaje tiene 50 campos, creás 100 objetos solo para el mapa. Multiplicá por 50k mensajes... ¡Es una fábrica de basura!"

### Escena 2: La Primera Cirugía (Versión Optimized v1)

**Enzo:** "Vamos a pre-compilar y usar un parser manual minimalista que busque los offsets. Nada de mapas, nada de Jackson."

```java
// VERSION 1: Pre-compilación y Parser manual
private static final Pattern ID_PATTERN = Pattern.compile("ORD-[0-9]{8}");

public void processMessage(String rawJson) {
    // Parser "pobre" pero rápido: buscamos la posición de la clave
    int idStart = rawJson.indexOf("\"orderId\":\"") + 11;
    if (idStart < 11) return;
    int idEnd = rawJson.indexOf("\"", idStart);
    String id = rawJson.substring(idStart, idEnd); 

    if (ID_PATTERN.matcher(id).matches()) {
        int priceStart = rawJson.indexOf("\"price\":") + 8;
        int priceEnd = rawJson.indexOf(",", priceStart);
        if (priceEnd == -1) priceEnd = rawJson.indexOf("}", priceStart);
        double price = Double.parseDouble(rawJson.substring(priceStart, priceEnd));
        save(id, price);
    }
}
```

**Análisis de Nico:** "Subimos a 150k msg/s. Pero mirá el Flame Graph: `Pattern.matcher` sigue siendo una meseta ancha. Y `substring` sigue pidiendo memoria."
**Enzo:** "Exacto. `substring` copia los caracteres a un array nuevo. Y el motor de Regex de Java no es el más rápido del mundo. Vamos a la fase 2: Zero-Copy."

### Escena 3: Rompiendo el Techo (Versión Optimized v2)

**Enzo:** "Vamos a trabajar sobre `byte[]` directamente y validar el ID con un lazo manual. Sin `String`, sin `Pattern`."

```java
// VERSION 2: Zero-Copy y Validación Manual
public void processMessage(byte[] rawBytes, int offset, int length) {
    int idPos = findField(rawBytes, offset, length, "orderId");
    if (isValidOrderId(rawBytes, idPos)) {
        double price = FastDoubleParser.parseDouble(rawBytes, ...);
        save(rawBytes, idPos, 12, price);
    }
}
```

**Resultados Finales:**
- Throughput: 900,000 msg/sec.
- Allocation: Casi cero.
- Latencia p99: 5ms.

---

## 4. Microbenchmarking con JMH: El manual del rigor absoluto

JMH no es solo una herramienta, es un guardián de la integridad científica. Vamos a profundizar en sus capacidades más oscuras.

### Guía de Profilers Internos de JMH (`-prof`)

JMH permite adjuntar profilers específicos que corren durante el benchmark. Estos son los que tenés que saber usar:

1.  **`gc`:** Reporta la tasa de asignación de objetos (bytes/op) y el tiempo de pausa del GC. Es la forma más rápida de saber si tu "algoritmo rápido" va a morir en producción por presión de memoria.
2.  **`stack`:** Genera un perfil de los stacks de ejecución. Si ves que tu código de benchmark gasta mucho tiempo en métodos de JMH, tu benchmark está mal diseñado (overhead de medición).
3.  **`perfasm`:** Solo Linux. Genera el ensamblador generado por el JIT y lo correlaciona con los ciclos de CPU. Es la única forma de saber si el compilador aplicó **Vectorización (SIMD)** usando registros AVX-512.
4.  **`cl` (Classloader):** Útil para detectar leaks de metadatos o carga excesiva de clases dinámicas.
5.  **`jfr`:** Genera un archivo `.jfr` directamente desde el benchmark para analizarlo en JMC.

### Controlando al JIT con `@CompilerControl`

El JIT es inteligente, a veces demasiado. Con esta anotación podés forzar comportamientos:
*   **`Mode.DONT_INLINE`:** Obliga a una llamada de método real. Fundamental si querés medir el costo de polimorfismo o despacho virtual.
*   **`Mode.EXCLUDE`:** Evita que el método sea compilado. Sirve para medir el peor caso (modo interpretado).
*   **`Mode.REQUIRED_INLINE`:** Fuerza al JIT a "pegar" el código.

### La Matemática de la Latencia: El fenómeno de la cola

Cuando JMH mide latencia, usa el **HDRHistogram**. Un error común es mirar el promedio. En sistemas distribuidos, lo que importa es la **Cola de Latencia**.
Si tenés un p99 de 100ms y un promedio de 1ms, significa que uno de cada 100 usuarios experimenta una lentitud 100 veces mayor a la normal. En una arquitectura de microservicios, si tu petición toca 10 servicios con ese p99, la probabilidad de que tu usuario sufra una demora es casi del 10%. Optimizar el p99.9 es lo que garantiza la escalabilidad.

---

## 5. TELEMETRÍA AVANZADA: Arquitectura y Custom Events de JFR

JFR no es un "monitor" externo; es un sistema de buffers circulares integrados en la memoria nativa de la JVM.

### Cómo funciona JFR por dentro

1.  **Buffer por Hilo:** Cada hilo de Java tiene un pequeño buffer local en memoria nativa para anotar eventos sin bloquear a otros hilos.
2.  **Global Buffer:** Cuando el buffer del hilo se llena, se vuelca a un buffer global.
3.  **Disk Storage:** Periódicamente, los buffers globales se escriben a un archivo `.jfr`.
Este diseño "wait-free" es lo que permite que JFR tenga un impacto menor al 1% en el rendimiento.

### Implementando Metadatos Personalizados

Podés decorar tus eventos con anotaciones que Mission Control entiende:
*   **`@DataAmount`:** Si tu campo es `long bytes`, JMC lo mostrará como "KB", "MB" o "GB".
*   **`@Timespan`:** Si es `long nanos`, lo mostrará como "ms", "s".
*   **`@Percentage`:** Muestra el valor como una proporción del total.
*   **`@Relational`:** Permite agrupar eventos distintos por un ID común (ej: un `CorrelationID`).

### Ejemplo de Evento de Ciclo de Vida Complejo
```java
@Label("DB Query")
@Category({"App", "Database"})
public class QueryEvent extends Event {
    @Label("SQL")
    public String sql;
    
    @Label("Rows")
    public int rowCount;
    
    @Label("Success")
    @BooleanFlag
    public boolean success;
}
```

---

## 6. HPC y la Física de la CPU: PMUs y Ejecución Fuera de Orden

Para optimizar al nivel de los nanosegundos, tenemos que entender qué pasa dentro del silicio.

### El Pipeline y la Ejecución Especulativa

Las CPUs modernas no ejecutan una instrucción a la vez. Tienen un **Pipeline** (como una línea de ensamblaje).
1.  **Fetch:** Traer la instrucción de memoria.
2.  **Decode:** Entender qué hay que hacer.
3.  **Execute:** Hacer la cuenta (ALU).
4.  **Writeback:** Guardar el resultado.

Si tenés un `if`, la CPU no espera a saber el resultado. **Adivina** (Branch Prediction) y sigue ejecutando. Si falla, tiene que "limpiar el pipeline", lo que cuesta ~20 ciclos de reloj. `async-profiler -e branch-misses` te dice dónde tu lógica de negocio es demasiado "impredecible" para el hardware.

### Unidades de Monitoreo de Performance (PMU)

Cada núcleo de CPU tiene registros especiales llamados contadores de performance.
*   **L1 Cache Misses:** Indica que tus datos no caben en los 32KB más rápidos.
*   **ITLB Misses:** Fallos en la caché de instrucciones. Ocurre si tu código es una "sopa de métodos" dispersos por toda la memoria.
*   **Cycles per Instruction (CPI):** Si es mayor a 1, tu CPU está perdiendo el tiempo esperando a la memoria RAM.

### Profiling de "Hardware Events" con async-profiler
```bash
# Ver cuántas instrucciones se ejecutan por ciclo de reloj
asprof -e instructions,cycles -f ipc_profile.html <pid>
```
Si ves que tu código tiene un IPC de 0.2, no importa cuánto optimices tu algoritmo de Java: tu problema es la **Localidad de Memoria**. La CPU está el 80% del tiempo parada esperando que los datos lleguen del bus de memoria.

---

## 7. Visualización: Flame Graphs

Un Flame Graph es la mejor forma de entender dónde se va el tiempo en un sistema complejo.

*   **Eje X:** Representa la población total de muestras. El ancho de una caja indica qué porcentaje del tiempo total estuvo ese método en la pila de llamadas. **No es una línea de tiempo**, es una agregación estadística.
*   **Eje Y:** Representa la profundidad del stack trace (quién llamó a quién).
*   **Color:** Suele indicar si el código es Java (naranja), Nativo (rojo) o del Kernel (celeste).

::: {tip} Cómo leer un Flame Graph
Buscá "mesetas" anchas en los niveles superiores (las que no tienen nada arriba). Esos son los métodos que están quemando CPU directamente ("On-CPU hot spots"). Si una caja es muy ancha pero tiene muchas cajas arriba, es solo un intermediario (como `run()` o `main()`).
:::

---

## 8. Profiling de Memoria: El fantasma en la máquina

En Java, no liberamos memoria manualmente, pero eso no significa que no podamos tener problemas de rendimiento por culpa de la gestión de memoria.

### El Heap y la Jerga del GC
La memoria se organiza en el **Heap**. El Garbage Collector (GC) intenta liberar objetos que ya no tienen referencias desde los **GC Roots** (hilos, estáticos, JNI).
*   **Young Generation:** Donde nacen los objetos. El GC acá es muy rápido (Minor GC).
*   **Old Generation:** Donde viven los objetos que sobrevivieron a varios Minor GCs. Limpiar esto es caro (Major GC).

### TLABs y Presión de Asignación
Cada hilo tiene su propio **Thread Local Allocation Buffer (TLAB)**. Es un pequeño pedazo del Heap donde puede crear objetos sin bloquear a otros hilos. Si creamos objetos demasiado rápido, los TLABs se saturan y la JVM tiene que sincronizar hilos para pedir más memoria, lo que genera micro-latencias.

---

## Ejercicios Avanzados

Aquí tenés 20 ejercicios diseñados para convertirte en un experto. Cada solución incluye una explicación técnica profunda.

```{exercise}
:label: ex-prof-1
**Ley de Amdahl.** Un algoritmo tiene una parte secuencial que toma el 15% del tiempo total. Si compramos un procesador con infinitos núcleos, ¿cuál es el speedup máximo teórico que podemos alcanzar? Justificá con la fórmula y explicá por qué los núcleos extra dejan de ser útiles después de cierto punto.
```

:::{solution} ex-prof-1
:class: dropdown
Para calcular el speedup máximo teórico, aplicamos la **Ley de Amdahl**: $S(N) = \frac{1}{(1-P) + \frac{P}{N}}$. 
En este caso, la fracción secuencial es $(1-P) = 0.15$ y la paralela es $P = 0.85$. Si llevamos los núcleos al infinito ($N \to \infty$), el término $\frac{0.85}{N}$ desaparece, resultando en $S(\infty) = \frac{1}{0.15} \approx 6.66$.

**Análisis Técnico Profundo:**
Este límite de 6.66x no es solo un capricho matemático, sino que representa el "muro de cristal" de la computación paralela. En la arquitectura de la JVM, la parte secuencial $(1-P)$ suele estar compuesta por la gestión de locks globales (como la contención en el `ClassLoader` o el acceso a regiones sincronizadas de memoria), las fases de "Stop-the-World" del Garbage Collector y el overhead de la gestión de hilos por parte del kernel.

A medida que sumás núcleos, el tiempo que tardan las tareas paralelas disminuye, pero la coordinación entre esos núcleos empieza a pasar factura. Según la **Ley Universal de Escalabilidad (USL)** de Neil Gunther, el escalado no solo se ameseta (Amdahl), sino que eventualmente decae debido a la **coherencia de caché** y la **contención del bus de memoria** (denotado por el parámetro $\beta$ de crosstalk).

En sistemas de producción reales, especialmente en la nube (AWS/GCP), esto significa que pagar por una instancia con 128 vCPUs para un proceso que tiene un 15% secuencial es tirar la plata a la basura. Estarías pagando un 1000% más de hardware para obtener apenas un 5% de mejora real sobre una instancia de 16 núcleos. Antes de escalar horizontalmente, tenés la obligación de reducir ese 15% mediante algoritmos *lock-free* o reduciendo la presión sobre el GC.

```{mermaid}
graph TD
    A[Carga Total] --> B(Parte Secuencial: 15%)
    A --> C(Parte Paralelizable: 85%)
    C --> D[Núcleo 1]
    C --> E[Núcleo 2]
    C --> F[Núcleo N...]
    B --> G[Cuello de Botella Inevitable]
    D & E & F --> H[Sincronización/Merge]
    G & H --> I[Fin de Ejecución]
    style B fill:#f66,stroke:#333
```
:::

```{exercise}
:label: ex-prof-2
**Safepoint Bias.** Explicá detalladamente por qué un profiler basado en sampling tradicional podría atribuir erróneamente el tiempo a un método de loggeo justo después de un lazo matemático intenso.
```

:::{solution} ex-prof-2
:class: dropdown
El **Safepoint Bias** es uno de los errores de medición más insidiosos en el ecosistema Java. Ocurre porque los samplers tradicionales de la JVM (como VisualVM o el profiler de JMC sin JFR habilitado) dependen de que el hilo llegue a un punto de control seguro para poder inspeccionar su stack trace.

**Explicación Arquitectónica:**
El compilador JIT, buscando la máxima eficiencia en la ejecución de lazos (loops), realiza una optimización llamada **Loop Unrolling** y elimina los chequeos de safepoint dentro de iteraciones que considera "cortas" o "simples". Si tenés un algoritmo matemático denso que corre por 500ms sin hacer llamadas a otros métodos ni asignar memoria (lo que dispararía un chequeo de TLAB), ese hilo es "invisible" para el profiler.

Cuando el hilo finalmente sale del lazo y llama, por ejemplo, a `logger.info()`, la JVM inserta un safepoint garantizado. En ese instante, el profiler "despierta", toma la foto y ve que el hilo está en el método de loggeo. 

**Impacto en Producción:**
Esto genera "falsos positivos" de rendimiento. Podés pasar semanas optimizando tu librería de logs pensando que es el cuello de botella, cuando en realidad el problema era el cálculo previo. 
Para mitigar esto en sistemas críticos, debés usar **Async-profiler**, que utiliza la señal `SIGPROF` del sistema operativo para interrumpir al hilo en cualquier instrucción de CPU, o habilitar el flag `-XX:+UseCountedLoopSafepoints` (aunque esto último puede reducir el rendimiento bruto entre un 1% y 5% al agregar overhead de control en los lazos).

```{mermaid}
sequenceDiagram
    participant CPU as Unidad de Ejecución
    participant JIT as Compilador (C2)
    participant Prof as Sampler Tradicional
    Note over CPU: Ejecutando Lazo Math.sin()
    JIT-->>CPU: Remueve Safepoint por optimización
    Prof->>CPU: ¿Dónde estás? (Sample Request)
    CPU-->>Prof: Silencio (No hay Safepoint)
    Note over CPU: Lazo Termina -> llama a Log()
    Note over CPU: Entra a Safepoint en Log()
    CPU-->>Prof: Estoy en Logger.info()
    Note over Prof: "El Loggeo es lento" (ERROR)
```
:::

```{exercise}
:label: ex-prof-3
**DCE en JMH.** Analizá por qué el siguiente código no mide nada y cómo el uso de `Blackhole` soluciona el problema estructuralmente.
```java
@Benchmark
public void benchmark() {
    long suma = 0;
    for (int i = 0; i < 1000; i++) suma += i;
}
```
```

:::{solution} ex-prof-3
:class: dropdown
El compilador JIT (específicamente C2) realiza un análisis de flujo de datos extremadamente agresivo. Al notar que la variable `suma` es puramente local y su valor final no se utiliza en ningún lugar fuera de la función, el JIT aplica **Dead Code Elimination (DCE)**. El resultado es que el lazo completo es removido físicamente del código binario nativo. Tu benchmark no está midiendo el costo de la suma, sino el costo de una instrucción `RET` (retorno de función), que es prácticamente cero.

**La magia negra de Blackhole:**
JMH provee un objeto especial llamado `Blackhole`. Estructuralmente, este objeto contiene métodos que están marcados internamente de forma que la JVM no pueda predecir sus efectos secundarios. Al pasarle el resultado a `bh.consume(suma)`, obligás al compilador a mantener vivos todos los cálculos que llevan a ese valor.

**Impacto en Sistemas Reales:**
Este fenómeno ocurre a menudo en tests unitarios que intentan medir rendimiento. Si el compilador detecta que el resultado de un cálculo pesado no se usa, lo va a borrar. En producción, si tenés código que calcula cosas que "por las dudas" no se usan, el JIT te está salvando la vida borrándolas, pero en un benchmark te está mintiendo. Usar `Blackhole` es la única forma de garantizar que la CPU realice el trabajo que querés medir.

```java
@Benchmark
public void benchmarkCorrecto(Blackhole bh) {
    long suma = 0;
    for (int i = 0; i < 1000; i++) suma += i;
    bh.consume(suma); // Forzamos la ejecución
}
```
:::

```{exercise}
:label: ex-prof-4
**Flame Graphs.** Ves una meseta gigante en un Flame Graph llamada `unix_syscall`. ¿Qué te está diciendo esto sobre la arquitectura de tu aplicación Java?
```

:::{solution} ex-prof-4
:class: dropdown
Una meseta ancha en `unix_syscall` indica que tu aplicación está pasando la mayor parte de su tiempo "cruzando la frontera" entre el **Modo Usuario** (donde corre la JVM) y el **Modo Kernel** (donde corre el sistema operativo). Este cambio de contexto es extremadamente caro en términos de ciclos de CPU.

**Análisis de Arquitectura:**
Si ves esto, tu aplicación es **I/O Bound** o está sufriendo de **Lock Contention** severa. Cada vez que hacés un `write()` a un archivo sin usar un buffer (ej: `FileOutputStream` directo en lugar de `BufferedOutputStream`), generás un syscall. Si escribís 1 millón de veces 1 byte, hacés 1 millón de syscalls. 

**Impacto en Producción:**
En sistemas de alta carga, esto se traduce en una saturación de la CPU que no se refleja en "trabajo útil" de la aplicación. Es "tiempo muerto" esperando a que el kernel mueva datos o gestione locks (futex). 
1. **Latencia:** El p99 se dispara porque el kernel tiene que suspender el hilo de la JVM para realizar la tarea.
2. **Throughput:** CAE drásticamente.
**Solución:** Agrupá las operaciones (batching), usá NIO con buffers directos para evitar copias entre usuario y kernel, o reducé la granularidad de los locks para evitar que el hilo entre en estado `BLOCKED` y tenga que ser despertado por el kernel.

```{mermaid}
graph LR
    JVM[Código Java (User Mode)] -- Syscall --> OS[Kernel Linux]
    OS -- Interrupción --> HW[Hardware / Disco / Red]
    HW --> OS
    OS -- Retorno --> JVM
    style OS fill:#ff9,stroke:#333
    Note over JVM,OS: El "Context Switch" cuesta ~1000-3000 ciclos
```
:::

```{exercise}
:label: ex-prof-5
**GC Roots.** En un dump de memoria, ves 500MB de objetos `UserSession` que deberían haber expirado. ¿Cómo usarías el concepto de "Path to GC Root" para encontrar el bug?
```

:::{solution} ex-prof-5
:class: dropdown
El concepto de **GC Root** es la base del algoritmo de "Mark-and-Sweep" de la JVM. Un objeto solo se libera si ya no es alcanzable desde una raíz (stack traces de hilos, variables estáticas, JNI handles). Si tenés 500MB de sesiones viejas, significa que *alguien* todavía tiene una referencia fuerte hacia ellas.

**Técnica de Investigación:**
En herramientas como MAT o JProfiler, debés usar la función **"Path to GC Root"**. Esto te mostrará la cadena jerárquica de punteros. 
- Si el path termina en una variable `static`, tenés un **Memory Leak** por uso de una caché global que nunca se limpia.
- Si el path termina en un hilo activo (ej: una variable local en un hilo que se bloqueó infinitamente), el bug está en la lógica de concurrencia que impide que el hilo termine.

**Análisis en Producción:**
Es muy común que los desarrolladores usen `ThreadLocal` para guardar datos de sesión y se olviden de llamar a `.remove()`. Dado que en los servidores de aplicaciones (Tomcat/Jetty) los hilos se reutilizan (thread pooling), ese `ThreadLocal` mantiene viva la sesión del usuario anterior para siempre. Multiplicado por miles de peticiones, esto causa un `OutOfMemoryError` en cuestión de horas. La solución es siempre limpiar los `ThreadLocal` en un bloque `finally`.

```{mermaid}
graph BT
    S[Objeto UserSession] --> M[HashMap 'cache']
    M --> C[Clase SessionManager]
    C --> R((GC Root: Variable Estática))
    style R fill:#f96,stroke:#333,stroke-width:4px
```
:::

```{exercise}
:label: ex-prof-6
**JIT Tiers.** Explicá el concepto de "De-optimización" y diseñá un escenario de código donde un cambio sutil en los datos de entrada destruya el rendimiento de un método ya compilado por C2.
```

:::{solution} ex-prof-6
:class: dropdown
La **Tiered Compilation** de la JVM permite que el código pase de ser interpretado a ser compilado por C1 (rápido) y finalmente por C2 (agresivo). C2 es un compilador **especulativo**: toma decisiones basadas en lo que ha visto hasta ahora (Profiling).

**Escenario de Desastre:**
Imaginá un método que procesa una lista de interfaces `Validador`. Durante el pre-calentamiento (warm-up), la lista solo contiene objetos del tipo `ValidadorSimple`. C2 aplica una optimización llamada **Monomorphic Inlining**: "pega" el código de `ValidadorSimple` directamente dentro del lazo, eliminando el costo del despacho virtual (VMT).

Si después del warm-up llega un solo objeto `ValidadorComplejo`, la suposición de C2 se rompe. Esto dispara un **Uncommon Trap**. La JVM debe detener el mundo, "de-optimizar" el código (volver al intérprete o a con C1), descartar el código nativo optimizado y eventualmente re-compilar.

**Impacto Real:**
Esto causa los famosos "picos de latencia" en aplicaciones Java que acaban de arrancar o que sufren cambios bruscos en el tráfico. Si tu sistema recibe un tipo de dato nuevo después de horas de estabilidad, podrías ver una caída del 50% en el rendimiento mientras la JVM intenta estabilizarse de nuevo. Es lo que se conoce como **Type Pollution** (Contaminación de Tipos).

```{mermaid}
stateDiagram-v2
    [*] --> Interprete: Inicio
    Interprete --> C1: Hot Method
    C1 --> C2: Very Hot Method (Optimización Especulativa)
    C2 --> Interprete: Uncommon Trap (Suposición fallida)
    Interprete --> C2: Re-optimización (Polimórfica)
```
:::

```{exercise}
:label: ex-prof-7
**Inlining.** ¿Cuál es la relación entre el tamaño del bytecode de un método y la capacidad del JIT para optimizarlo? Mencioná los flags de la JVM involucrados.
```

:::{solution} ex-prof-7
:class: dropdown
El **Inlining** es la madre de todas las optimizaciones en la JVM. Consiste en reemplazar la llamada a un método por el cuerpo del método mismo. Esto no solo ahorra el costo de la llamada (push/pop del stack frame), sino que permite que el compilador vea el flujo de datos completo y aplique optimizaciones como el **Escape Analysis**.

**Límites Físicos:**
El JIT tiene presupuestos estrictos para el inlining para evitar que el binario resultante sea gigantesco (lo que arruinaría la caché de instrucciones L1 de la CPU).
1. **`-XX:MaxFreqInlineSize` (325 bytes):** Si un método se llama muy seguido, este es el tamaño máximo de bytecode que el JIT aceptará para inlinearlo.
2. **`-XX:MaxInlineSize` (35 bytes):** Si el método no es "caliente", el límite es minúsculo.

**Análisis de Impacto:**
Si escribís métodos "bollo" (gigantes), estás bloqueando al JIT. Un método de 500 líneas nunca será inlined. Esto significa que si creás un objeto pequeño adentro, el Escape Analysis no podrá ver que el objeto no sale del método y no podrá eliminar la asignación en el Heap.
**Regla de Oro:** Mantené tus métodos pequeños (menos de 30-50 líneas). No es solo por limpieza de código; es para que el JIT tenga "permiso" de optimizarte.

```{mermaid}
graph TD
    A[Método A] -- Llama a --> B[Método B]
    A -- Llama a --> C[Método C]
    B -- Inlined --> A
    C -- Muy Grande --> NoInlined[Llamada de Función Real]
    style B fill:#dfd
    style C fill:#fdd
```
:::

```{exercise}
:label: ex-prof-8
**TLABs.** Explicá por qué la asignación de objetos en Java es casi tan rápida como mover un puntero en C, y cómo el profiling de "Allocation Pressure" revela problemas de diseño.
```

:::{solution} ex-prof-8
:class: dropdown
Contrario a la creencia popular, `new Object()` en Java no busca memoria en una lista global bloqueada. La JVM utiliza **TLABs (Thread Local Allocation Buffers)**. Cada hilo recibe un "pedazo" exclusivo de la memoria Eden. Asignar un objeto es simplemente incrementar un puntero interno (`bump-the-pointer`). No hay locks, no hay esperas. Es puramente local al núcleo de la CPU.

**El Problema: Allocation Pressure:**
Si tu código crea millones de objetos temporales por segundo (ej: `String.format()` dentro de un lazo), el TLAB se llena en microsegundos. Cuando esto pasa:
1. El hilo debe detenerse y pedir un nuevo TLAB al Heap global.
2. Esto requiere una operación atómica (`CAS`) o un lock, lo que genera contención entre todos los hilos de la aplicación.
3. El Garbage Collector tiene que trabajar mucho más seguido para limpiar la basura acumulada.

**Impacto en Producción:**
En sistemas de alta carga (FinTech, juegos), una alta presión de asignación destruye el rendimiento aunque el GC sea rápido. Cada vez que pedís un TLAB nuevo, estás robándole ciclos de reloj a la lógica de negocio. Usá `async-profiler -e alloc` para ver qué métodos están llenando los TLABs. Si ves que el 90% de tu memoria son objetos `Integer` o `Double` (boxeo), pasate a primitivos y vas a ver cómo el sistema "respira".

```{mermaid}
graph LR
    H1[Hilo 1] --> T1[TLAB 1]
    H2[Hilo 2] --> T2[TLAB 2]
    T1 & T2 --> Eden[Eden Space Global]
    Eden -- Lock/CAS --> NewTLAB[Asignar Nuevo Buffer]
    style NewTLAB fill:#f66
```
:::

```{exercise}
:label: ex-prof-9
**JMH Params.** Creamos un benchmark para comparar `ArrayList` vs `LinkedList` en inserciones al final. ¿Por qué para $N=10^6$ la `ArrayList` sigue siendo superior a pesar de tener que redimensionar el arreglo interno?
```

:::{solution} ex-prof-9
:class: dropdown
Este es el ejemplo clásico de por qué la complejidad algorítmica ($O$) no lo es todo. Aunque ambas tienen inserción $O(1)$ amortizada, la **Localidad de Referencia** le da la victoria total a la `ArrayList`.

**Análisis de Hardware:**
Las CPUs modernas no leen de a 1 byte, leen **Cache Lines** de 64 bytes. 
1. **ArrayList:** Los elementos están contiguos. Cuando la CPU trae el elemento 0, también trae los elementos 1 al 7 a la caché L1. El recorrido es una línea recta para el prefetcher de la CPU.
2. **LinkedList:** Cada nodo es un objeto independiente en una dirección de memoria aleatoria. La CPU tiene que saltar por todo el Heap. Cada salto es un **Cache Miss** potencial que obliga a la CPU a esperar 100-300 ciclos a que lleguen los datos de la RAM.

**Overhead de Memoria:**
Una `LinkedList` de 1 millón de `Integer` consume aproximadamente 48MB (Overhead de objeto `Node` + referencias). Una `ArrayList` consume solo 8MB (el arreglo de referencias). 
En producción, esto significa que la `LinkedList` no solo es más lenta, sino que llena el Heap 6 veces más rápido, disparando el Garbage Collector con mucha más frecuencia. Nunca uses `LinkedList` a menos que necesites insertar en el medio y tengas el iterador en la mano; para todo lo demás, `ArrayList` es la reina.

```{mermaid}
graph LR
    subgraph ArrayList
    A[Elem 0] --- B[Elem 1] --- C[Elem 2]
    end
    subgraph LinkedList
    N1[Nodo 1] -. RAM Jump .-> N2[Nodo 2] -. RAM Jump .-> N3[Nodo 3]
    end
    style ArrayList fill:#dfd
    style LinkedList fill:#fdd
```
:::

```{exercise}
:label: ex-prof-10
**Análisis de Fugas.** Un sistema tiene un uso de memoria que crece linealmente. El Heap Dump muestra muchos `byte[]`. ¿Cómo determinarías si es un leak de un cache de imágenes o un leak de conexiones de red no cerradas?
```

:::{solution} ex-prof-10
:class: dropdown
Los `byte[]` son la causa número uno de OOM (Out Of Memory) porque son la representación base de casi todo: Strings, imágenes, buffers de red, archivos. Para diagnosticar el origen, debés realizar un análisis forense del dump:

1. **Inspección de Contenido (Hex View):** Si abrís el contenido de los arreglos en MAT y ves "JFIF" o "PNG", son imágenes. Si ves "HTTP/1.1 200 OK", son buffers de red.
2. **Dominator Tree:** Esta es la herramienta clave. Te dirá qué objeto es el "dueño" del arreglo. 
   - Si el dueño es un `LruCache` o un `HashMap` de tu código, tenés un leak de caché (te olvidaste de ponerle un tamaño máximo).
   - Si el dueño es un `SocketInputStream` o un objeto de una librería como Netty o Apache HttpClient, tenés un leak de conexiones (estás abriendo sockets y no llamando a `.close()`).

**Impacto en Sistemas Cloud:**
En entornos como AWS Lambda o Kubernetes con límites de memoria estrictos, un leak de este tipo causará que el proceso sea terminado por el **OOM Killer** del sistema operativo. Esto es peor que un error de Java, porque no deja rastro en los logs de la aplicación. El profiling preventivo con JFR y el monitoreo del evento `jdk.ObjectAllocationInNewTLAB` son fundamentales para detectar la tendencia alcista antes de que el pod explote.
:::

```{exercise}
:label: ex-prof-11
**False Sharing.** Diseñá un ejemplo de dos variables `long` que, al ser modificadas por hilos distintos en un procesador multinúcleo, rinden 10 veces peor de lo esperado. Explicá el rol de la L1 Cache Line.
```

:::{solution} ex-prof-11
:class: dropdown
El **False Sharing** es un fenómeno de hardware que ocurre cuando dos hilos modifican variables independientes que, por mala suerte, residen en la misma **Línea de Caché** (típicamente 64 bytes).

**Mecánica del Fallo:**
La CPU mantiene la coherencia entre núcleos mediante un protocolo llamado **MESI**. Si el Núcleo 1 modifica la variable `A`, invalida toda la línea de caché en los demás núcleos. Si el Núcleo 2 quiere modificar la variable `B` (que está en la misma línea), debe esperar a que los datos se sincronicen desde la memoria principal, a pesar de que `A` y `B` no tienen nada que ver lógicamente.

**Ejemplo de Código Letal:**
```java
public class Contadores {
    public long contadorA; // Ocupa 8 bytes
    public long contadorB; // Ocupa 8 bytes, ¡cae en la misma línea!
}
```

**Solución Académica:**
En Java 8+, se introdujo la anotación `@jdk.internal.vm.annotation.Contended`. Esta anotación le indica a la JVM que debe agregar un "relleno" (padding) de bytes vacíos alrededor de la variable para forzarla a estar en su propia línea de caché. En sistemas de trading de alta frecuencia, evitar el False Sharing es la diferencia entre procesar en microsegundos o en milisegundos.

```{mermaid}
graph TD
    subgraph "Línea de Caché (64 bytes)"
    A[Variable A]
    B[Variable B]
    Padding[Espacio Vacío...]
    end
    N1[Núcleo 1 modifica A] -- Invalida --> N2[Núcleo 2 esperando por B]
    style N2 fill:#f66
```
:::

```{exercise}
:label: ex-prof-12
**Async-profiler.** Querés perfilar una aplicación que corre en un ambiente de Cloud restringido donde no podés usar eventos de kernel (`perf_events`). ¿Qué alternativa tenés y qué perdés en el camino?
```

:::{solution} ex-prof-12
:class: dropdown
Si estás en un contenedor Docker sin privilegios `SYS_ADMIN`, `async-profiler` no puede acceder a los contadores de hardware (`perf_event_open`).

**La Alternativa: `-e itimer`:**
Podés usar el modo `itimer` (Interval Timer). Este modo utiliza señales del sistema operativo (`SIGPROF`) que no requieren privilegios especiales. El SO envía una señal al proceso cada $X$ milisegundos, y el profiler captura el stack trace en ese momento.

**Lo que perdés (El costo de la seguridad):**
1. **Visibilidad del Kernel:** No verás qué está haciendo el SO (ej: esperas de disco, manejo de red). Solo verás el tiempo de CPU en modo usuario.
2. **Precisión en Native:** `itimer` a veces tiene problemas para capturar stack traces de código nativo (JNI) con precisión quirúrgica.
3. **Métricas de Hardware:** Te olvidás de medir **Cache Misses**, **Branch Mispredictions** o ciclos de CPU desperdiciados. Solo tenés una medida de "tiempo".

**Análisis de Producción:**
A pesar de estas limitaciones, `itimer` sigue siendo órdenes de magnitud mejor que cualquier profiler basado en safepoints. En un entorno de microservicios en Kubernetes, es tu mejor herramienta para diagnosticar por qué un pod está consumiendo el 100% de CPU sin tener que pedirle permiso al equipo de infraestructura para cambiar las políticas de seguridad del cluster.
:::

```{exercise}
:label: ex-prof-13
**Constant Folding.** En JMH, ¿por qué es fundamental usar campos `@State` en lugar de variables locales constantes? Mostrá un ejemplo de un benchmark fallido por este motivo.
```

:::{solution} ex-prof-13
:class: dropdown
El JIT es un optimizador obsesivo. Si ve algo que puede calcular "en frío", lo hará y borrará el código original. Esto es el **Constant Folding**.

**Benchmark Fallido:**
```java
@Benchmark
public double calcular() {
    double x = 100.0;
    return Math.sqrt(x); // El JIT ve que sqrt(100) es 10 y borra la llamada
}
```
En este caso, JMH te reportará un tiempo de 0.1ns, porque la CPU no está calculando la raíz cuadrada; simplemente está devolviendo el número 10 que ya estaba pre-calculado en el binario nativo. No estás midiendo `Math.sqrt`, estás midiendo la velocidad de retorno de una constante.

**La Solución Estructural:**
Debés usar objetos `@State`. Al poner `x` en un campo de una clase de estado, el compilador JIT no puede estar 100% seguro de que otro hilo no modificará ese valor (aunque no lo hagas). Esto "rompe" el análisis de constantes y fuerza a la CPU a leer el valor de memoria y realizar el cálculo real en cada iteración.

**Impacto en Producción:**
Este fenómeno explica por qué a veces configuraciones "hardcodeadas" en el código corren mucho más rápido que configuraciones leídas de un archivo `.properties`. El JIT optimiza las constantes de una forma que es imposible para valores dinámicos. Pero en un benchmark, es una trampa mortal que invalida tus resultados.
:::

```{exercise}
:label: ex-prof-14
**Escalabilidad.** Explicá la diferencia entre "Throughput" y "Goodput" usando un ejemplo de un servidor web bajo un ataque de denegación de servicio (DoS).
```

:::{solution} ex-prof-14
:class: dropdown
Esta distinción es vital para entender la salud real de un sistema bajo estrés. 

1. **Throughput (Capacidad Bruta):** Es la cantidad total de trabajo que el sistema está procesando. Si tu servidor recibe 10.000 peticiones por segundo y a todas responde con un error `503 Service Unavailable` de forma instantánea, tu throughput es de 10.000 rps. El sistema parece "rápido" y "eficiente" en los gráficos de red.
2. **Goodput (Capacidad Útil):** Es la fracción de ese trabajo que realmente beneficia al usuario. Si de esas 10.000 peticiones, solo 5 son de usuarios legítimos que reciben un `200 OK`, tu goodput es de solo 5 rps.

**Análisis de Profiling:**
Bajo un ataque DoS o una sobrecarga masiva, el throughput puede mantenerse alto mientras el goodput cae a cero. Esto suele pasar porque el sistema gasta todos sus recursos (CPU, hilos, ancho de banda) en rechazar conexiones o manejar excepciones. 
**Lección de Ingeniería:** Al optimizar, siempre buscá maximizar el goodput. Si hacés que tu sistema maneje errores 10 veces más rápido, estás subiendo el throughput pero no estás ayudando al usuario. Usar técnicas de **Circuit Breaker** o **Backpressure** ayuda a proteger el goodput sacrificando el throughput total de forma controlada.
:::

```{exercise}
:label: ex-prof-15
**Amdahl en End-to-End.** ¿Por qué optimizar un `HashMap` para que sea 10x más rápido puede no tener impacto en un sistema que consulta una base de datos? Justificá usando la Ley de Amdahl.
```

:::{solution} ex-prof-15
:class: dropdown
Este es el error más común del programador junior: optimizar lo que es "fácil" en lugar de lo que es "importante".

**Cálculo de Amdahl:**
Supongamos que procesar una petición tarda 100ms.
- 99ms se van esperando la respuesta de la Base de Datos (I/O).
- 1ms se va procesando el JSON en la JVM usando un `HashMap`.
Si optimizás el `HashMap` un 1000% (ahora tarda 0.1ms), la petición total ahora tarda 99.1ms.
**Speedup Real:** $100 / 99.1 = 1.009x$. ¡Una mejora del 0.9% después de semanas de trabajo!

**Análisis de Sistemas Reales:**
En arquitecturas de microservicios, el tiempo de CPU suele ser una fracción minúscula del tiempo de respuesta (latencia). La mayor parte del tiempo es "tiempo de espera" (red, base de datos, locks). Según Amdahl, el speedup máximo está estrictamente limitado por la parte que **no** podés optimizar. Si no tocás la base de datos, nunca vas a mejorar la percepción del usuario más allá de ese 1%. El profiling "End-to-End" con herramientas como Zipkin o Jaeger te permite ver esta realidad antes de que pierdas tiempo optimizando algoritmos que no son el cuello de botella.
:::

```{exercise}
:label: ex-prof-16
**Escape Analysis.** Explicá cómo la JVM puede eliminar totalmente la asignación de un objeto en el Heap. ¿Qué condiciones deben cumplirse y cómo lo verificás con profiling?
```

:::{solution} ex-prof-16
:class: dropdown
El **Escape Analysis (EA)** es la optimización que permite que Java sea competitivo con C++. El JIT analiza si un objeto "escapa" del ámbito donde fue creado.

**Condición de No-Escape:**
Un objeto no escapa si:
1. No se guarda en un campo de una clase.
2. No se devuelve como resultado del método.
3. No se pasa como argumento a otro método que el JIT no pueda inlinear.

**La Optimización: Scalar Replacement:**
Si el objeto no escapa, la JVM hace algo asombroso: **no crea el objeto**. En lugar de asignar memoria en el Heap, descompone el objeto en sus campos primitivos y los guarda directamente en los registros de la CPU o en el Stack del hilo. Esto elimina el costo de asignación, elimina la presión sobre el Garbage Collector y mejora la velocidad de acceso.

**Cómo verificarlo:**
Usá JMH con el profiler de GC (`-prof gc`). Si tu código hace `new Punto(x, y)` millones de veces pero el profiler reporta `0 bytes/op` de asignación, felicitaciones: el EA está funcionando. Si ves asignaciones, es probable que tus métodos sean demasiado grandes para ser inlined, lo que "rompe" el análisis de escape.
:::

```{exercise}
:label: ex-prof-17
**Wall-clock vs CPU Time.** Un hilo está bloqueado esperando un `ReentrantLock`. ¿Aparecerá este hilo en un perfil de CPU? ¿Y en uno de Wall-clock? Justificá.
```

:::{solution} ex-prof-17
:class: dropdown
Entender esta diferencia es la clave para diagnosticar problemas de escalabilidad.

1. **CPU Time:** No aparecerá. Cuando un hilo está bloqueado esperando un lock, el sistema operativo lo pone en estado `WAITING` y lo quita del núcleo de la CPU. No consume ciclos de reloj. Si solo mirás un perfil de CPU (como el de VisualVM estándar), el hilo será "invisible". Podrías pensar que tu servidor está "relajado" porque el uso de CPU es bajo (10%), cuando en realidad está colapsado.
2. **Wall-clock Time:** Aparecerá con una meseta gigante. El Wall-clock mide el tiempo real que pasa desde el inicio hasta el fin, sin importar qué está haciendo el hilo. Verás que el hilo está "gastando" el 100% de su tiempo en el método `.lock()`.

**Análisis de Producción:**
Si tu aplicación está lenta pero la CPU está baja, tenés un problema de **concurrencia o I/O**. Necesitás un Wall-clock profile para encontrar el lock que está causando el cuello de botella. Si la aplicación está lenta y la CPU está al 100%, tenés un problema de **algoritmos o GC**, y necesitás un CPU profile para ver qué métodos están quemando ciclos.
:::

```{exercise}
:label: ex-prof-18
**JFR Custom Events.** Diseñá un escenario donde un evento personalizado de JFR sea mejor que un simple log de texto para diagnosticar un problema de rendimiento intermitente.
```

:::{solution} ex-prof-18
:class: dropdown
Imaginá un sistema de procesamiento de pagos que, de forma aleatoria, tarda 10 segundos en procesar una transacción. Un log de texto te diría: `[2023-10-27 10:00:01] Pago lento: ID_999`. Pero eso no te sirve para saber **por qué**.

**La Solución con JFR:**
Creamos un evento personalizado `PagoProcesadoEvent` que guarde el ID del cliente y el monto. 
Al analizar el archivo JFR en Mission Control (JMC), podés hacer algo que es imposible con logs: **Correlación Temporal Automática**.
1. Seleccionás el evento de 10 segundos en la línea de tiempo.
2. El JMC te muestra automáticamente qué estaba pasando en la JVM *exactamente en ese hilo y en ese instante*. 
3. Podés ver si hubo una pausa de Garbage Collector justo en ese momento, o si el hilo estuvo bloqueado esperando un pool de conexiones a la base de datos (evento `jdk.ThreadPark`).

**Ventaja Técnica:**
A diferencia de los logs, los eventos JFR son binarios y estructurados. El overhead de grabarlos es casi cero. Podés capturar miles de eventos por segundo sin afectar el rendimiento, lo que te permite tener una "caja negra" de avión para tu software de producción.
:::

```{exercise}
:label: ex-prof-19
**HashMap vs TreeMap.** ¿Por qué un `TreeMap` puede ser más lento en el microbenchmark pero hacer que el resto de la aplicación sea más rápida? (Pista: Pensá en el flujo de datos subsiguiente).
```

:::{solution} ex-prof-19
:class: dropdown
Este es el ejercicio resalta la trampa de los microbenchmarks aislados. En un test de inserción pura, el `HashMap` ($O(1)$) va a destruir al `TreeMap` ($O(\log n)$) porque este último tiene que mantener un árbol rojo-negro balanceado y realizar comparaciones en cada paso.

**El Escenario Global:**
Supongamos que tu aplicación después necesita procesar los datos de forma ordenada (ej: generar un reporte por fecha).
- **Con HashMap:** Tenés que copiar todas las entradas a una lista y ordenarla usando `Collections.sort()`, lo cual cuesta $O(n \log n)$ y genera muchísima basura (objetos temporales) que el Garbage Collector tendrá que limpiar.
- **Con TreeMap:** El recorrido ya es ordenado por definición. El costo es $O(n)$ y **cero asignaciones extras**.

**Impacto en Profiling:**
Si perfilás la aplicación completa, verás que el tiempo "ahorrado" en la inserción del `HashMap` es una fracción de lo que se gasta después ordenando y limpiando la basura generada. El `TreeMap` ofrece un rendimiento más predecible y reduce la presión sobre el GC. Siempre diseñá tus estructuras pensando en el **ciclo de vida completo del dato**, no solo en la operación más rápida.
:::

```{exercise}
:label: ex-prof-20
**Optimización de Bucle.** ¿Cómo detectaría un profiler de CPU que este código tiene un problema de "redundancia de cálculo"?
```java
for (int i = 0; i < lista.size(); i++) {
    double factor = Math.sqrt(Math.pow(Math.PI, 2)); // Constante disfrazada
    lista.get(i).actualizar(factor);
}
```
```

:::{solution} ex-prof-20
:class: dropdown
Un profiler de CPU de alta precisión (como `async-profiler` con `-prof perfasm` o JFR) detectará este problema mostrando una "acumulación de calor" (hotspot) desproporcionada en las instrucciones de punto flotante dentro del lazo.

**Análisis de la JVM:**
Aunque `Math.PI` es una constante, el JIT no siempre puede aplicar **Loop Invariant Code Motion (LICM)** si el cálculo es complejo o si involucra llamadas a métodos que no puede asegurar que sean "puros". Cada vez que el lazo corre, la CPU podría estar calculando la potencia y la raíz cuadrada, desperdiciando ciclos preciosos.

**Cómo se ve en el Profiler:**
En un Flame Graph, verás que el método `actualizar` tiene una caja muy ancha para su "Self Time" (tiempo propio) dedicada a operaciones matemáticas. Si ves que una operación que debería ser instantánea consume el 30% de tu CPU, es una señal clara de que tenés un cálculo que debería estar fuera del lazo. 

**Impacto en Producción:**
En dispositivos móviles o sistemas embebidos, este tipo de ineficiencias no solo ralentizan la app, sino que **agotan la batería** innecesariamente. La optimización manual (sacar el `factor` fuera del lazo) garantiza que la intención del programador sea clara tanto para otros humanos como para el compilador, eliminando cualquier duda sobre la eficiencia del código generado.
:::

## Próximo paso

Con las herramientas de medición en tu cinturón, ya podés aventurarte a comparar representaciones de datos con rigor. Pasemos a estudiar las [Secuencias](secuencias/indice.md), donde aplicaremos todo esto para entender por qué un arreglo es, a veces, invencible.

## 10. PROFILING EN SISTEMAS DISTRIBUIDOS: El desafío de la visibilidad

Cuando pasás de un monolito a una arquitectura de microservicios, el profiling tradicional se queda corto. Ya no te alcanza con saber que el método `procesarPago()` es lento en el Servicio A; necesitás saber si la lentitud viene de la red, de una base de datos en otro datacenter o de un servicio de terceros que te está clavando con un timeout silencioso. En sistemas distribuidos, el rendimiento es una propiedad emergente de la interacción entre componentes, y ahí es donde entra el **Profiling Distribuido**.

### El abismo de los microservicios
En un sistema distribuido, una petición de usuario puede saltar por diez servicios distintos antes de devolver una respuesta. Si el p99 de tu latencia sube, ¿a quién culpás? Sin telemetría correlacionada, vas a estar saltando de servidor en servidor tirando `top` o `jstack` a ver qué encontrás. Es como intentar arreglar un motor mientras el auto va a 100 km/h por la Panamericana.

El problema principal es la **pérdida de contexto**. Cuando el Servicio A llama al Servicio B por HTTP, la JVM del Servicio B no tiene idea de qué hilo del Servicio A disparó esa petición. Para arreglar esto, usamos **Distributed Tracing**.

### Distributed Tracing y la unión con el Profiling
Herramientas como **Jaeger**, **Zipkin** y, más recientemente, el estándar **OpenTelemetry (OTel)**, nos permiten reconstruir el camino de una petición usando tres conceptos clave:
1. **Trace ID:** Un identificador único para toda la transacción (ej. desde que el usuario hace click hasta que recibe el mail).
2. **Span ID:** Un identificador para una unidad de trabajo dentro de un servicio.
3. **Propagation:** El proceso de pasar estos IDs en los headers (HTTP, Kafka, gRPC) para que el siguiente servicio sepa a qué trace pertenece.

Pero acá está el truco: el tracing te dice *dónde* se perdió el tiempo (ej. "el Servicio B tardó 500ms"), pero no te dice *por qué* (ej. "¿fue por CPU, por GC o por un lock?"). Para eso necesitás **Continuous Profiling** integrado con tracing.

### Correlación por Trace ID: Inyectando metadatos en JFR
El "Santo Grial\" del profiling moderno es poder filtrar un Flame Graph por Trace ID. Imaginate ver solo los ciclos de CPU que consumió la petición del cliente \"VIP\" que se queja de lentitud.

Con **JDK Flight Recorder (JFR)**, podés crear eventos personalizados que incluyan el `trace_id`. Mirá cómo podrías hacerlo:

```java
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;

@Name("com.catedra.TraceContextEvent")
@Label("Trace Context")
class TraceContextEvent extends Event {
    @Label("Trace ID")
    String traceId;
    
    @Label("Span ID")
    String spanId;
}

// En tu filtro o interceptor de peticiones:
TraceContextEvent event = new TraceContextEvent();
event.traceId = OpenTelemetry.getTraceId(); // Sacado del contexto de OTel
event.begin();
try {
    procesarPeticion();
} finally {
    event.commit();
}
```

Al hacer esto, cuando abrís el archivo `.jfr` en Mission Control, podés usar el Trace ID como filtro para ver qué estaba haciendo la CPU exactamente durante esa traza. Herramientas como **Grafana Phlare**, **Pyroscope** o **Datadog** automatizan este proceso, permitiéndote saltar de un \"Span\" de latencia directamente al Flame Graph de ese instante.

### Latencia de red y el costo oculto de la Serialización
En un sistema distribuido, la red es el \"bus\" de tu computadora. Y así como en el capítulo de memoria vimos que los punteros duelen, en la red lo que duele es la **Serialización**.

Muchos desarrolladores usan JSON por defecto porque es fácil de leer para los humanos. Pero para la CPU, JSON es un dolor de cabeza:
*   **Parsing:** Hay que escanear bytes, manejar escapes de texto, convertir strings a números.
*   **Reflection:** Librerías como Jackson suelen usar introspección de clases, lo que destruye el inlining del JIT.
*   **Tamaño:** JSON es redundante. Mandar la palabra `\"nombre_del_cliente\"` un millón de veces es tirar ancho de banda.

**Caso de estudio: JSON vs Protobuf**
Si hacés un profiling de un servicio que maneja 50k peticiones por segundo, vas a ver que el 30% de la CPU se te va en `jackson.databind`. Si te pasás a **Protobuf (Protocol Buffers)** o **Avro**, ese tiempo cae a menos del 5%. ¿Por qué? Porque Protobuf usa un formato binario compacto y genera código Java nativo que no necesita reflexión. La CPU simplemente copia bytes a posiciones fijas de memoria. Si tu servicio es \"CPU Bound\" y hacés mucho I/O, el profiling te va a gritar que dejes de usar JSON.

---

## 11. EL ROL DEL SISTEMA OPERATIVO (KERNEL PROFILING)

A veces, el profiler de Java te dice que todo está bien, pero la aplicación sigue siendo una tortuga. Mirás el Flame Graph y ves una caja gigante que dice `Native Method` o, peor, ves que la CPU está en un 80% de `System Time` (Kernel) y solo un 20% en `User Time` (JVM). Acá es donde tenés que sacarte el sombrero de programador Java y ponerte el de administrador de sistemas: la JVM no es una isla, vive arriba de un Kernel (generalmente Linux).

### ¿Por qué la JVM no es una isla?
Cada vez que tu código hace algo \"interesante\" (leer un archivo, mandar un paquete por red, pedir memoria, crear un hilo), tiene que pedirle permiso al Kernel mediante una **System Call (syscall)**. El problema es que cruzar la frontera entre el modo usuario y el modo kernel es caro.

### Context Switching: El asesino silencioso
Si tenés demasiados hilos (ej. 5000 hilos para un servidor que solo tiene 8 núcleos), el Kernel va a pasar más tiempo haciendo **Context Switching** que ejecutando tu código. 
*   **Involuntary Context Switches:** El Kernel le saca la CPU a tu hilo porque se le terminó el tiempo (time slice). Esto pasa cuando tenés sobrecarga de hilos.
*   **Voluntary Context Switches:** Tu hilo suelta la CPU porque está esperando algo (un lock, un I/O, un `Thread.sleep()`).

Podés ver esto con el comando `pidstat -w`. Si ves miles de context switches por segundo, tu profiling de \"Wall-clock\" te va a mostrar que los hilos pasan la mayor parte del tiempo en estado `WAITING`, esperando a que el Kernel les devuelva el control.

### Paging, Swapping y el terror del GC
Este es el escenario de pesadilla de cualquier Java Dev. El Garbage Collector asume que toda la memoria RAM es igual de rápida. Pero si el sistema operativo se queda sin RAM física y empieza a usar el disco (Swap), estás liquidado.

Cuando el GC hace un \"Stop-The-World\" para escanear el Heap, toca miles de objetos. Si parte de ese Heap fue \"swapeado\" a disco, cada acceso a un objeto dispara un **Major Page Fault**. La CPU tiene que frenar todo, ir al disco (que es 100.000 veces más lento que la RAM), cargar la página y seguir. Una pausa de GC que debería durar 10ms se convierte en una de 10 segundos.

**Consejo de trinchera:** Siempre monitoreá los Page Faults con `vmstat 1` o `sar -B`. Si ves que `pgscank` o `pgscand` suben, tu JVM está a punto de explotar, no importa qué tan bien hayas tuneado el `Xmx`.

### Usando eBPF para ver lo invisible
Hasta hace poco, ver qué pasaba dentro del Kernel era para unos pocos elegidos. Pero ahora tenemos **eBPF (Extended Berkeley Packet Filter)**. eBPF te permite correr programas minúsculos dentro del Kernel sin crashearlo y sin afectar el rendimiento.

Con herramientas como **bpftrace**, podés perfilar el I/O de disco de tu JVM desde afuera. Por ejemplo, si sospechás que los logs te están frenando, podés tirar este script:

```bash
# bpftrace -e 'tracepoint:syscalls:sys_enter_write /pid == 1234/ { @[ustack] = count(); }'
```

Esto te va a dar un stack trace que cruza la frontera: vas a ver el método Java que llamó a `FileOutputStream.write()`, pasando por la librería de C (`libc`), la syscall `write()`, y finalmente el sistema de archivos del Kernel (`vfs_write`). 

eBPF es la herramienta definitiva para detectar:
*   **Micro-latencias de disco:** ¿El disco tarda 1ms o 100ms en confirmar un `write`?
*   **Retransmisiones TCP:** ¿Por qué la red está lenta aunque la CPU esté baja?
*   **Hardware Interrupts:** ¿Hay una placa de red mal configurada que está bombardeando a la CPU con interrupciones?

---

## 12. PROFILING CONTINUO EN CI/CD: El guardián del rendimiento

En el desarrollo de software moderno, el rendimiento no puede ser un pensamiento de último momento o algo que se mide una vez al año antes de un despliegue importante. Debe estar integrado en el corazón del pipeline de Integración Continua y Despliegue Continuo (CI/CD). La filosofía detrás del **Continuous Profiling** y los **Performance Regression Tests** es simple: si un cambio de código rompe el p99, el build debe fallar.

### Filosofía de Performance Regression Testing
A diferencia de los tests funcionales que verifican "qué" hace el software, los tests de rendimiento verifican "cómo" lo hace bajo presión. 
1. **Definición de Umbrales (SLOs):** Debés establecer límites claros. Por ejemplo: "La búsqueda no puede tardar más de 50ms en el p95 con 10.000 elementos".
2. **Ambiente de Medición Estable:** Correr JMH en una laptop mientras usás Chrome es inútil. Los benchmarks de CI deben correr en máquinas dedicadas (bare-metal si es posible) para evitar el ruido de la virtualización (noisy neighbors).

### Automatización con JMH y Jenkins/GitHub Actions
Podés configurar JMH para que exporte los resultados en formato JSON. Luego, un script o un plugin de Jenkins compara esos resultados contra el build anterior. 
- Si el throughput bajó un 5% o más, el build se marca como "Inestable".
- Si bajó un 20%, se "Aborta".
Esto te permite detectar de-optimizaciones accidentales (ej: un desarrollador que agregó un log pesado en un lazo crítico) antes de que lleguen a producción.

### Comparación de Perfiles entre Versiones
El profiling continuo no solo se trata de números, sino de **perfiles**. Herramientas como **Pyroscope** o **Parca** permiten realizar "Diffs" de Flame Graphs. Podés ver visualmente que la "caja" de un método creció entre la versión v1.2 y v1.3, lo que te indica exactamente dónde se introdujo la ineficiencia.

---

## 13. CASO DE ESTUDIO AVANZADO: La Revolución del LMAX Disruptor

Uno de los hitos más importantes en el estudio de la performance en Java fue la creación del **Disruptor** por parte del equipo de LMAX (una plataforma de trading de altísima frecuencia). Su investigación es el ejemplo perfecto de cómo el profiling de hardware redefine el software.

### El Problema de las Colas Tradicionales
Al perfilar sistemas que usaban `LinkedBlockingQueue` para comunicar hilos, el equipo notó que el rendimiento era mediocre a pesar de tener CPUs potentes. El profiling de bajo nivel (contadores de hardware) reveló el culpable: **Cache Invalidation y Lock Contention**.
- En una `LinkedBlockingQueue`, los nodos se crean dinámicamente ($Allocation Pressure$).
- La cola usa locks (`ReentrantLock`) que disparan syscalls caros.
- La cabeza y el fondo de la cola a menudo caen en la misma línea de caché, causando **False Sharing**.

### La Solución: Mechanical Sympathy y RingBuffers
Aplicando lo aprendido mediante profiling de CPU y caché, diseñaron el Disruptor:
1. **Pre-asignación:** Todos los objetos en la cola se crean al arrancar. El Garbage Collector no trabaja durante la ejecución.
2. **RingBuffer Circular:** Se usa un arreglo fijo (potencia de 2) para maximizar la localidad espacial y usar operaciones de bit (`&`) en lugar de módulo (`%`).
3. **Sequence Barrier:** En lugar de locks, usan barreras de memoria y variables `volatile` para coordinar hilos de forma *lock-free*.
4. **Padding de Caché:** Agregan bytes "muertos" para asegurar que los punteros de lectura y escritura vivan en líneas de caché distintas.

**Resultado:** Lograron procesar más de 6 millones de órdenes por segundo con una latencia p99 inferior a 1 microsegundo. El profiling les demostró que, a ese nivel, la abstracción de "objetos" de Java es un estorbo, y hay que pensar en "bits y líneas de caché".

---

## 14. GUÍA DE RESOLUCIÓN DE PROBLEMAS (TROUBLESHOOTING)

Cuando el sistema está ardiendo, necesitás un protocolo de actuación. Aquí tenés los pasos recomendados:

### Escenario A: "Tengo picos de latencia (Hiccups) aleatorios"
1. **Mirar Pausas de GC:** Usá JFR y buscá el evento `jdk.GCPhasePause`. Si la pausa coincide con el pico, tenés un problema de memoria.
2. **Analizar Safepoints:** Si el GC es corto pero el p99 es alto, buscá pausas de safepoint largas (TTSP). Alguien está secuestrando la JVM.
3. **Revisar I/O:** ¿Hay picos de escritura en disco? El evento `jdk.FileWrite` te dirá si el disco está bloqueando hilos.

### Escenario B: "La CPU está al 100% pero el throughput es bajo"
1. **Flame Graph de CPU:** Identificá el método más ancho. ¿Es tu código o es una librería?
2. **Check de Inlining:** Usá JITWatch para ver si los métodos calientes fueron inlined. Si no, descompone tus métodos gigantes.
3. **Contadores de Hardware:** Usá `async-profiler -e cache-misses`. Si los misses son altos, tu algoritmo tiene mala localidad. Rediseñá tus estructuras de datos para que sean más compactas.

### Escenario C: "La memoria sube y sube (Memory Leak)"
1. **Heap Dump Diff:** Tomá un dump ahora y otro en 10 minutos. Compará las instancias. 
2. **Path to GC Root:** Buscá quién retiene los objetos que más crecieron.
3. **Check de ThreadLocals:** Son la causa #1 de leaks en servidores web. Asegurate de que se estén limpiando.

---

## 15. CONCLUSIÓN: El futuro del rendimiento en Java

El mundo de la performance en Java está en constante evolución. Dos proyectos van a cambiar las reglas del juego en los próximos años:

### Proyecto Valhalla: El fin del Object Bloat
Como vimos en los ejercicios, el overhead de las cabeceras de objetos y la indirección de punteros son el gran enemigo de la caché. Valhalla introducirá los **Value Types**. Podremos tener "objetos" que se comporten como primitivos, almacenados de forma contigua en memoria sin headers. Esto hará que estructuras como `ArrayList<ComplexNumber>` sean tan rápidas como en C++.

### Proyecto Loom: Hilos Virtuales y Concurrencia Masiva
Loom introduce hilos livianos que no requieren syscalls para el context switch. Esto cambiará el profiling de Wall-clock: ya no tendremos miedo de bloquear miles de hilos esperando I/O, lo que simplificará enormemente el modelo de programación de servidores de alta concurrencia.

---

## GLOSARIO TÉCNICO DE PERFORMANCE

En el mundo de la optimización, hablar con precisión es la mitad de la batalla. Si no podés distinguir entre latencia y throughput, vas a terminar optimizando la métrica equivocada. Acá tenés una guía exhaustiva de los términos que tenés que manejar como un profesional para no quedar como un improvisado cuando te toque discutir con el equipo de infraestructura.

**Throughput (Caudal de Procesamiento)**
Es la medida de cuántas unidades de trabajo puede procesar tu sistema en un intervalo de tiempo determinado. No te confundas con la velocidad individual; el throughput se trata de volumen. Pensalo como una autopista: no importa si un auto va a 200 km/h (eso es latencia), el throughput es cuántos autos pasan por el peaje por hora. En la JVM, lo medimos como transacciones por segundo (TPS) o peticiones por segundo (RPS). Un sistema puede tener un throughput altísimo pero una latencia mediocre si procesa muchas cosas en paralelo pero cada una tarda bastante. Ojo con el "Throughput de Negocio" vs. el "Throughput Técnico": procesar un millón de mensajes de error por segundo es un éxito técnico pero un fracaso rotundo para el usuario.

**Latency (Latencia)**
Es el tiempo transcurrido desde que se inicia una operación hasta que se completa. Es el "tiempo de respuesta" que siente el usuario. Se mide en unidades de tiempo (milisegundos, microsegundos o nanosegundos). La latencia es caprichosa porque nunca es fija; es una distribución estadística. Tenés la latencia de procesamiento (lo que tarda tu código en hacer la cuenta) y la latencia de espera (el tiempo que el pedido pasa en una cola porque todos los hilos están ocupados). En sistemas de alto rendimiento, luchamos por bajar la latencia para mejorar la interactividad, sabiendo que muchas veces bajar la latencia implica sacrificar un poco de throughput por el overhead de coordinación.

**Goodput (Caudal Útil)**
Esta es la métrica de la verdad. Representa la tasa de transferencia de datos o tareas que son realmente útiles para el propósito final, descartando retransmisiones, cabeceras de protocolo o, lo más común en Java, peticiones que terminaron en error. Si tu servidor está bajo un ataque de denegación de servicio (DoS) y responde "403 Forbidden" a 100.000 peticiones por segundo, tu throughput es enorme, pero tu goodput es casi cero. Siempre perfilá buscando maximizar el goodput; optimizar la velocidad con la que tu sistema falla es una pérdida de tiempo total si no arreglás la causa del error.

**Jitter (Variabilidad de Latencia)**
Es la fluctuación o variación temporal en la latencia de una serie de operaciones. Si la primera petición tarda 10ms y la segunda tarda 500ms, tenés un jitter espantoso. Para aplicaciones de streaming de audio/video o sistemas de trading, el jitter es más dañino que una latencia alta pero constante. Un sistema predecible permite armar buffers y estrategias de control; un sistema con jitter te obliga a sobdimensionar todo "por las dudas". En Java, las causas principales del jitter son las pausas del Garbage Collector y la competencia por el tiempo de CPU entre los hilos de tu app y otros procesos del sistema operativo.

**Tail Latency (Latencia de Cola)**
Se refiere a los valores de latencia que caen en los percentiles más altos de la distribución, típicamente el p99, p99.9 o el p99.99. Si el promedio de tu sistema es de 5ms pero el p99 es de 2 segundos, tenés un problema de latencia de cola. Se llama así porque en un histograma representa la "cola" larga hacia la derecha. Es lo que realmente destruye la experiencia del usuario. En arquitecturas de microservicios, la latencia de cola se propaga y se multiplica: si tu servicio llama a otros 10 servicios en paralelo, la probabilidad de que uno de ellos te clave con su latencia de cola es altísima, arruinando toda la respuesta del usuario.

**STW (Stop The World)**
Es el evento donde la JVM suspende por completo la ejecución de todos los hilos de la aplicación para realizar tareas críticas de mantenimiento, generalmente relacionadas con el Garbage Collector. Durante un STW, tu código no corre; el reloj sigue pasando pero tu lógica está congelada. Para un observador externo, el sistema parece haber muerto por unos milisegundos (o segundos). El gran desafío de los recolectores modernos como ZGC o Shenandoah es reducir estos eventos a menos de un milisegundo, permitiendo que la mayoría del trabajo de limpieza se haga de forma concurrente mientras tu aplicación sigue andando.

**TTSP (Time To Safepoint)**
Es el tiempo que transcurre desde que la JVM solicita un Safepoint (para un STW) hasta que efectivamente todos los hilos de la aplicación se detienen en un punto seguro. No es instantáneo. Si tenés un hilo haciendo un lazo matemático muy intenso que el JIT optimizó sacándole los chequeos de safepoint, ese hilo puede tardar cientos de milisegundos en frenar, dejando a todos los demás hilos (y al GC) esperando. Un TTSP largo es un asesino silencioso de la latencia; por más que el GC sea rápido, si tardás 500ms en frenar los hilos para empezar a limpiar, tu pausa real para el usuario fue de 500ms + tiempo de GC.

**TLAB (Thread Local Allocation Buffer)**
Es un pequeño bloque de memoria del "Eden Space" asignado exclusivamente a un hilo para que pueda crear objetos sin tener que sincronizarse con otros hilos. Es la razón por la que crear objetos en Java es absurdamente rápido (casi tanto como en C). Sin TLABs, cada vez que hacés un `new`, el hilo tendría que pedir un lock global para reservar memoria, lo que destruiría el rendimiento en sistemas multinúcleo. Cuando perfilás "Allocation Pressure", lo que ves es qué tan rápido tus hilos están llenando sus TLABs y pidiendo nuevos al Heap global.

**RSS (Resident Set Size)**
Es la cantidad de memoria física real (RAM) que tu proceso Java está ocupando en un momento dado. Ojo, que no es lo mismo que el tamaño del Heap (`-Xmx`). El RSS incluye el Heap, pero también el Metaspace (clases), el Code Cache (código JIT), la memoria usada por los hilos (stacks), y las librerías nativas cargadas por JNI. El OOM Killer de Linux no mira cuánta memoria dice Java que tiene; mira el RSS. Si tu RSS supera el límite del contenedor o del sistema, tu proceso va a morir sin que veas un `OutOfMemoryError` en los logs de Java.

**VSS (Virtual Set Size)**
Es el tamaño total del espacio de direccionamiento virtual que el proceso ha reclamado al sistema operativo. Incluye todo lo que hay en el RSS más la memoria que ha sido reservada pero no necesariamente "mapeada" a RAM física (como el espacio total del Heap que todavía no se usó). En sistemas de 64 bits, es normal que el VSS sea gigante (varios terabytes si usás ZGC), porque el espacio de direcciones es barato. No te asustes si ves un VSS enorme en `top`; lo que importa para la estabilidad del sistema es el RSS y cuánto estás paginando a disco.

---

## APÉNDICE: JVM DIAGNOSTIC FLAGS

Tocar los flags de la JVM sin entenderlos es como meter mano en el motor de un avión en pleno vuelo. Sin embargo, para hacer profiling serio, necesitás conocer las llaves maestras que abren la telemetría profunda. Acá tenés los flags de diagnóstico y ajuste más importantes que te van a ayudar a entender qué carajo está pasando dentro de la caja negra.

**-Xmx (Maximum Heap Size)**
Define el techo máximo de memoria que el Heap de Java puede ocupar. Es el flag más conocido, pero el que peor se usa. Ponerlo muy alto puede darte "aire" para no tener errores de memoria, pero hace que las pausas de GC sean más largas porque hay más basura para revisar. Ponerlo muy bajo causa "GC Thrashing", donde la JVM se la pasa limpiando memoria en lugar de trabajar. El truco es encontrar el punto dulce donde el GC pueda hacer su trabajo eficientemente sin desperdiciar RAM que el sistema operativo podría usar para caché de archivos.

**-Xms (Initial Heap Size)**
Establece el tamaño inicial del Heap al arrancar la aplicación. Un consejo de veterano: en entornos de producción, igualá siempre el `-Xms` con el `-Xmx`. ¿Por qué? Porque si son distintos, la JVM va a estar redimensionando el Heap constantemente según la carga, pidiendo y devolviendo memoria al sistema operativo. Ese redimensionamiento ocurre durante eventos de GC y agrega una latencia innecesaria. Al igualarlos, le decís a la JVM: "Tomá toda esta memoria de entrada y no pierdas tiempo gestionando el tamaño del pool".

**-XX:MaxMetaspaceSize**
Define el límite de memoria para el Metaspace, que es donde la JVM guarda los metadatos de las clases cargadas. A diferencia del Heap, el Metaspace vive en memoria nativa (fuera del control directo del GC del Heap). Si tu aplicación carga clases dinámicamente (típico en frameworks como Spring o Hibernate), este espacio puede crecer. Si no le ponés un límite, puede comerse toda la RAM del servidor. Si se llena, vas a ver un `java.lang.OutOfMemoryError: Metaspace`. Es vital monitorear esto si usás muchas librerías que generan proxies en tiempo de ejecución.

**-XX:+PrintGCDetails**
Es el punto de partida para cualquier análisis de rendimiento. Activa el loggeo detallado de cada evento del Garbage Collector, mostrándote cuánto tiempo duró cada fase, cuánta memoria se liberó y por qué se disparó el evento. En versiones modernas de Java (9+), esto se hace con el sistema de loggeo unificado: `-Xlog:gc*`. Nunca corras una aplicación en producción sin tener los logs de GC activos. El overhead es despreciable y la información que te dan cuando las papas queman es la diferencia entre arreglar el problema en 5 minutos o estar ciego toda la noche.

**-XX:+UnlockDiagnosticVMOptions**
Es la "llave maestra". Muchos de los flags más potentes (como los que siguen abajo) están protegidos porque pueden ser peligrosos o cambiar el comportamiento interno de la JVM de formas sutiles. Este flag no hace nada por sí solo, pero permite que la JVM acepte los flags de diagnóstico. Es como entrar en "Modo Desarrollador" en tu teléfono. Tenés que usarlo con respeto, sabiendo que lo que actives después de esto es para investigación profunda, no para configuración rutinaria de cualquier app.

**-XX:+PrintAssembly**
Es el flag definitivo para los amantes del bajo nivel. Vuelca por consola el código ensamblador (código de máquina) real que el compilador JIT generó para tus métodos más usados. Requiere que instales una librería externa llamada `hsdis` en el directorio de la JRE. Es la única forma de verificar si el JIT está aplicando optimizaciones de hardware como SIMD/Vectorización, o si está metiendo chequeos de nulidad innecesarios que te están frenando. Si querés saber exactamente qué está ejecutando la CPU, este es el camino.

**-XX:+LogCompilation**
Genera un archivo XML detallado con todas las decisiones que tomó el compilador JIT (C1 y C2) durante la vida de la aplicación. Te dice qué métodos fueron inlined, cuáles fueron rechazados por ser demasiado grandes, y dónde ocurrieron las de-optimizaciones (uncommon traps). Leer el XML a mano es imposible, pero herramientas como JITWatch lo procesan y te dan una vista clara de por qué tu código no está volando como debería. Es fundamental para diagnosticar problemas de "warm-up" y contaminación de sitios de llamada.

**-XX:+UseCountedLoopSafepoints**
Este flag es la cura para un problema de latencia muy específico pero común en computación científica. Por defecto, el JIT quita los chequeos de safepoint de los lazos "contados" (los que tienen un límite conocido, como un `for` de 0 a 1000) para ganar velocidad. Pero si el lazo tarda mucho, el hilo no puede frenar para el GC, disparando el tiempo de TTSP. Al activar este flag, obligás al JIT a mantener los safepoints en esos lazos, sacrificando un poquito de throughput a cambio de una latencia mucho más predecible y pausas de GC más cortas.

**-XX:MaxFreqInlineSize**
Controla el tamaño máximo de bytecode que un método puede tener para ser candidato a inlining si se considera "frecuente" (caliente). El valor por defecto suele ser 325 bytes. Si tenés un método crítico que mide 330 bytes, el JIT no lo va a inlinear, y vas a perder muchísimas optimizaciones derivadas. Subir este valor puede mejorar el rendimiento de aplicaciones con métodos complejos, pero ojo: si lo subís demasiado, el tamaño del binario generado puede saturar la caché de instrucciones de la CPU y terminar haciendo que todo ande más lento.

## GLOSARIO TÉCNICO DE PERFORMANCE

En el mundo de la optimización, hablar con precisión es la mitad de la batalla. Si no podés distinguir entre latencia y throughput, vas a terminar optimizando la métrica equivocada. Acá tenés una guía exhaustiva de los términos que tenés que manejar como un profesional para no quedar como un improvisado cuando te toque discutir con el equipo de infraestructura.

**Throughput (Caudal de Procesamiento)**
Es la medida de cuántas unidades de trabajo puede procesar tu sistema en un intervalo de tiempo determinado. No te confundas con la velocidad individual; el throughput se trata de volumen. Pensalo como una autopista: no importa si un auto va a 200 km/h (eso es latencia), el throughput es cuántos autos pasan por el peaje por hora. En la JVM, lo medimos como transacciones por segundo (TPS) o peticiones por segundo (RPS). Un sistema puede tener un throughput altísimo pero una latencia mediocre si procesa muchas cosas en paralelo pero cada una tarda bastante. Ojo con el "Throughput de Negocio" vs. el "Throughput Técnico": procesar un millón de mensajes de error por segundo es un éxito técnico pero un fracaso rotundo para el usuario.

**Latency (Latencia)**
Es el tiempo transcurrido desde que se inicia una operación hasta que se completa. Es el "tiempo de respuesta" que siente el usuario. Se mide en unidades de tiempo (milisegundos, microsegundos o nanosegundos). La latencia es caprichosa porque nunca es fija; es una distribución estadística. Tenés la latencia de procesamiento (lo que tarda tu código en hacer la cuenta) y la latencia de espera (el tiempo que el pedido pasa en una cola porque todos los hilos están ocupados). En sistemas de alto rendimiento, luchamos por bajar la latencia para mejorar la interactividad, sabiendo que muchas veces bajar la latencia implica sacrificar un poco de throughput por el overhead de coordinación.

**Goodput (Caudal Útil)**
Esta es la métrica de la verdad. Representa la tasa de transferencia de datos o tareas que son realmente útiles para el propósito final, descartando retransmisiones, cabeceras de protocolo o, lo más común en Java, peticiones que terminaron en error. Si tu servidor está bajo un ataque de denegación de servicio (DoS) y responde "403 Forbidden" a 100.000 peticiones por segundo, tu throughput es enorme, pero tu goodput es casi cero. Siempre perfilá buscando maximizar el goodput; optimizar la velocidad con la que tu sistema falla es una pérdida de tiempo total si no arreglás la causa del error.

**Jitter (Variabilidad de Latencia)**
Es la fluctuación o variación temporal en la latencia de una serie de operaciones. Si la primera petición tarda 10ms y la segunda tarda 500ms, tenés un jitter espantoso. Para aplicaciones de streaming de audio/video o sistemas de trading, el jitter es más dañino que una latencia alta pero constante. Un sistema predecible permite armar buffers y estrategias de control; un sistema con jitter te obliga a sobdimensionar todo "por las dudas". En Java, las causas principales del jitter son las pausas del Garbage Collector y la competencia por el tiempo de CPU entre los hilos de tu app y otros procesos del sistema operativo.

**Tail Latency (Latencia de Cola)**
Se refiere a los valores de latencia que caen en los percentiles más altos de la distribución, típicamente el p99, p99.9 o el p99.99. Si el promedio de tu sistema es de 5ms pero el p99 es de 2 segundos, tenés un problema de latencia de cola. Se llama así porque en un histograma representa la "cola" larga hacia la derecha. Es lo que realmente destruye la experiencia del usuario. En arquitecturas de microservicios, la latencia de cola se propaga y se multiplica: si tu servicio llama a otros 10 servicios en paralelo, la probabilidad de que uno de ellos te clave con su latencia de cola es altísima, arruinando toda la respuesta del usuario.

**STW (Stop The World)**
Es el evento donde la JVM suspende por completo la ejecución de todos los hilos de la aplicación para realizar tareas críticas de mantenimiento, generalmente relacionadas con el Garbage Collector. Durante un STW, tu código no corre; el reloj sigue pasando pero tu lógica está congelada. Para un observador externo, el sistema parece haber muerto por unos milisegundos (o segundos). El gran desafío de los recolectores modernos como ZGC o Shenandoah es reducir estos eventos a menos de un milisegundo, permitiendo que la mayoría del trabajo de limpieza se haga de forma concurrente mientras tu aplicación sigue andando.

**TTSP (Time To Safepoint)**
Es el tiempo que transcurre desde que la JVM solicita un Safepoint (para un STW) hasta que efectivamente todos los hilos de la aplicación se detienen en un punto seguro. No es instantáneo. Si tenés un hilo haciendo un lazo matemático muy intenso que el JIT optimizó sacándole los chequeos de safepoint, ese hilo puede tardar cientos de milisegundos en frenar, dejando a todos los demás hilos (y al GC) esperando. Un TTSP largo es un asesino silencioso de la latencia; por más que el GC sea rápido, si tardás 500ms en frenar los hilos para empezar a limpiar, tu pausa real para el usuario fue de 500ms + tiempo de GC.

**TLAB (Thread Local Allocation Buffer)**
Es un pequeño bloque de memoria del "Eden Space" asignado exclusivamente a un hilo para que pueda crear objetos sin tener que sincronizarse con otros hilos. Es la razón por la que crear objetos en Java es absurdamente rápido (casi tanto como en C). Sin TLABs, cada vez que hacés un `new`, el hilo tendría que pedir un lock global para reservar memoria, lo que destruiría el rendimiento en sistemas multinúcleo. Cuando perfilás "Allocation Pressure", lo que ves es qué tan rápido tus hilos están llenando sus TLABs y pidiendo nuevos al Heap global.

**RSS (Resident Set Size)**
Es la cantidad de memoria física real (RAM) que tu proceso Java está ocupando en un momento dado. Ojo, que no es lo mismo que el tamaño del Heap (`-Xmx`). El RSS incluye el Heap, pero también el Metaspace (clases), el Code Cache (código JIT), la memoria usada por los hilos (stacks), y las librerías nativas cargadas por JNI. El OOM Killer de Linux no mira cuánta memoria dice Java que tiene; mira el RSS. Si tu RSS supera el límite del contenedor o del sistema, tu proceso va a morir sin que veas un `OutOfMemoryError` en los logs de Java.

**VSS (Virtual Set Size)**
Es el tamaño total del espacio de direccionamiento virtual que el proceso ha reclamado al sistema operativo. Incluye todo lo que hay en el RSS más la memoria que ha sido reservada pero no necesariamente "mapeada" a RAM física (como el espacio total del Heap que todavía no se usó). En sistemas de 64 bits, es normal que el VSS sea gigante (varios terabytes si usás ZGC), porque el espacio de direcciones es barato. No te asustes si ves un VSS enorme en `top`; lo que importa para la estabilidad del sistema es el RSS y cuánto estás paginando a disco.

---

## APÉNDICE: JVM DIAGNOSTIC FLAGS

Tocar los flags de la JVM sin entenderlos es como meter mano en el motor de un avión en pleno vuelo. Sin embargo, para hacer profiling serio, necesitás conocer las llaves maestras que abren la telemetría profunda. Acá tenés los flags de diagnóstico y ajuste más importantes que te van a ayudar a entender qué carajo está pasando dentro de la caja negra.

**-Xmx (Maximum Heap Size)**
Define el techo máximo de memoria que el Heap de Java puede ocupar. Es el flag más conocido, pero el que peor se usa. Ponerlo muy alto puede darte "aire" para no tener errores de memoria, pero hace que las pausas de GC sean más largas porque hay más basura para revisar. Ponerlo muy bajo causa "GC Thrashing", donde la JVM se la pasa limpiando memoria en lugar de trabajar. El truco es encontrar el punto dulce donde el GC pueda hacer su trabajo eficientemente sin desperdiciar RAM que el sistema operativo podría usar para caché de archivos.

**-Xms (Initial Heap Size)**
Establece el tamaño inicial del Heap al arrancar la aplicación. Un consejo de veterano: en entornos de producción, igualá siempre el `-Xms` con el `-Xmx`. ¿Por qué? Porque si son distintos, la JVM va a estar redimensionando el Heap constantemente según la carga, pidiendo y devolviendo memoria al sistema operativo. Ese redimensionamiento ocurre durante eventos de GC y agrega una latencia necesaria. Al igualarlos, le decís a la JVM: "Tomá toda esta memoria de entrada y no pierdas tiempo gestionando el tamaño del pool".

**-XX:MaxMetaspaceSize**
Define el límite de memoria para el Metaspace, que es donde la JVM guarda los metadatos de las clases cargadas. A diferencia del Heap, el Metaspace vive en memoria nativa (fuera del control directo del GC del Heap). Si tu aplicación carga clases dinámicamente (típico en frameworks como Spring o Hibernate), este espacio puede crecer. Si no le ponés un límite, puede comerse toda la RAM del servidor. Si se llena, vas a ver un `java.lang.OutOfMemoryError: Metaspace`. Es vital monitorear esto si usás muchas librerías que generan proxies en tiempo de ejecución.

**-XX:+PrintGCDetails**
Es el punto de partida para cualquier análisis de rendimiento. Activa el loggeo detallado de cada evento del Garbage Collector, mostrándote cuánto tiempo duró cada fase, cuánta memoria se liberó y por qué se disparó el evento. En versiones modernas de Java (9+), esto se hace con el sistema de loggeo unificado: `-Xlog:gc*`. Nunca corras una aplicación en producción sin tener los logs de GC activos. El overhead es despreciable y la información que te dan cuando las papas queman es la diferencia entre arreglar el problema en 5 minutos o estar ciego toda la noche.

**-XX:+UnlockDiagnosticVMOptions**
Es la "llave maestra". Muchos de los flags más potentes (como los que siguen abajo) están protegidos porque pueden ser peligrosos o cambiar el comportamiento interno de la JVM de formas sutiles. Este flag no hace nada por sí solo, pero permite que la JVM acepte los flags de diagnóstico. Es como entrar en "Modo Desarrollador" en tu teléfono. Tenés que usarlo con respeto, sabiendo que lo que actives después de esto es para investigación profunda, no para configuración rutinaria de cualquier app.

**-XX:+PrintAssembly**
Es el flag definitivo para los amantes del bajo nivel. Vuelca por consola el código ensamblador (código de máquina) real que el compilador JIT generó para tus métodos más usados. Requiere que instales una librería externa llamada `hsdis` en el directorio de la JRE. Es la única forma de verificar si el JIT está aplicando optimizaciones de hardware como SIMD/Vectorización, o si está metiendo chequeos de nulidad innecesarios que te están frenando. Si querés saber exactamente qué está ejecutando la CPU, este es el camino.

**-XX:+LogCompilation**
Genera un archivo XML detallado con todas las decisiones que tomó el compilador JIT (C1 y C2) durante la vida de la aplicación. Te dice qué métodos fueron inlined, cuáles fueron rechazados por ser demasiado grandes, y dónde ocurrieron las de-optimizaciones (uncommon traps). Leer el XML a mano es imposible, pero herramientas como JITWatch lo procesan y te dan una vista clara de por qué tu código no está volando como debería. Es fundamental para diagnosticar problemas de "warm-up" y contaminación de sitios de llamada.

**-XX:+UseCountedLoopSafepoints**
Este flag es la cura para un problema de latencia muy específico pero común en computación científica. Por defecto, el JIT quita los chequeos de safepoint de los lazos "contados" (los que tienen un límite conocido, como un `for` de 0 a 1000) para ganar velocidad. Pero si el lazo tarda mucho, el hilo no puede frenar para el GC, disparando el tiempo de TTSP. Al activar este flag, obligás al JIT a mantener los safepoints en esos lazos, sacrificando un poquito de throughput a cambio de una latencia mucho más predecible y pausas de GC más cortas.

**-XX:MaxFreqInlineSize**
Controla el tamaño máximo de bytecode que un método puede tener para ser candidato a inlining si se considera "frecuente" (caliente). El valor por defecto suele ser 325 bytes. Si tenés un método crítico que mide 330 bytes, el JIT no lo va a inlinear, y vas a perder muchísimas optimizaciones derivadas. Subir este valor puede mejorar el rendimiento de aplicaciones con métodos complejos, pero ojo: si lo subís demasiado, el tamaño del binario generado puede saturar la caché de instrucciones de la CPU y terminar haciendo que todo ande más lento.

---

## Próximo paso

Con las herramientas de medición en tu cinturón y el conocimiento profundo de cómo la JVM interactúa con el hardware, ya podés aventurarte a comparar representaciones de datos con rigor científico. Pasemos a estudiar las [Secuencias](secuencias/indice.md), donde aplicaremos todo este arsenal para entender por qué, a pesar de lo que dicen los libros de teoría, un simple arreglo suele ser la estructura de datos más poderosa del mundo real.
