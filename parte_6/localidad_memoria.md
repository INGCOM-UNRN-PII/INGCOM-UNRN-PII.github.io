---
title: "Localidad de memoria"
subtitle: "Sinfonía entre el software y el silicio"
subject: Estructuras de Datos
description: Análisis exhaustivo del impacto de la jerarquía de memoria, el layout de objetos en la JVM y las optimizaciones de hardware en el rendimiento real de los algoritmos.
---

(parte6-localidad-memoria)=
# Localidad de memoria

En el análisis teórico de algoritmos, nos movemos en el cómodo mundo de la **Máquina de Acceso Aleatorio (RAM)**, una abstracción donde cada celda de memoria tiene el mismo costo de acceso. Sin embargo, en el hardware real, esta abstracción se rompe violentamente. Un procesador moderno de 3.5 GHz puede ejecutar cientos de instrucciones en el tiempo que le toma a una señal eléctrica viajar a la memoria RAM y volver.

Entender la **localidad de memoria** no es un "extra" para optimización prematura; es una necesidad fundamental. Ignorar cómo el hardware gestiona los datos invalida cualquier análisis asintótico en sistemas de alta performance. Como decía L. Peter Deutsch: "LISP programmers know the value of everything and the cost of nothing". En Programación II, buscamos que sepas ambas cosas.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Dominar los principios físicos y lógicos que gobiernan la transferencia de datos entre la jerarquía de memoria y la CPU, y cómo diseñar estructuras de datos que maximicen el rendimiento aprovechando el diseño del hardware.

**Prerrequisitos.** 
- [Gestión de memoria en C y Java](../parte_1/13_memoria.md).
- [Complejidad Asintótica](../parte_5/3_algoritmos.md).
- Conocimientos de punteros e indirección.

**Desarrollo.** 
1. Arquitectura de Hardware: El Memory Wall y latencias exactas.
2. JVM Internals: Anatomía de objetos (JOL), punteros comprimidos y alineación.
3. Microarquitectura: Prefetching, OoO y Pipeline.
4. Localidad en Estructuras: Arreglos vs. Listas vs. B-Trees.
5. Casos de Estudio: Matrices y Loop Blocking.
6. Problemas Avanzados: False Sharing, TLB y Huge Pages.
7. Futuro: Proyecto Valhalla.
8. Benchmarks y JMH.
9. Ejercicios Técnicos de Alto Nivel.
:::

## 1. Arquitectura de Hardware: La Física Detrás del Cómputo

El rendimiento de un sistema informático está limitado por leyes físicas inmutables: la velocidad de propagación de los electrones en el silicio y la constante dieléctrica de los aislantes. No podés optimizar código si no entendés la topografía del terreno donde corren tus datos.

### 1.1. La Gran Brecha: Historia del "Memory Wall"
Desde finales de los años 70, la industria del hardware ha seguido dos ritmos de evolución asimétricos. Por un lado, la capacidad de procesamiento (CPU) creció siguiendo la Ley de Moore, optimizando el conteo de transistores y la frecuencia de reloj. Por otro lado, la memoria RAM (DRAM) se enfocó casi exclusivamente en la **densidad de almacenamiento** y el costo por bit, dejando de lado la latencia.

En 1980, un procesador Intel 8086 y su memoria RAM operaban a velocidades similares; no había "espera". Para 1995, la CPU ya era 10 veces más rápida. Hoy, un núcleo moderno es órdenes de magnitud más veloz que la DRAM. Esta diferencia se conoce como el **Memory Wall**.

```{mermaid}
graph LR
    subgraph "Evolución Histórica (1980-2024)"
    direction TB
    C[Performance CPU +55% anual] --- M[Performance RAM +7% anual]
    M --> W{The Memory Wall}
    W --> S[Stalls masivos de ejecución]
    end
```

### 1.2. Anatomía Microscópica: L1, L2 y L3
Para mitigar esta brecha, se introdujeron las memorias caché (SRAM). A diferencia de la DRAM, que usa capacitores que deben refrescarse (lentos), la SRAM usa celdas de 6 transistores que mantienen el estado mientras haya energía, permitiendo conmutaciones en picosegundos.

#### División L1: Instrucciones vs Datos
La caché L1 (nivel 1) es la más cercana a la ALU y está dividida físicamente:
- **L1i (Instruction Cache):** Optimizada para lecturas secuenciales y *branch prediction*. Es de solo lectura para la CPU (salvo código auto-modificable).
- **L1d (Data Cache):** Diseñada para soportar lectura y escritura masiva. Tiene múltiples puertos para permitir que la CPU lea y escriba datos en el mismo ciclo.

Esta división (Arquitectura Harvard interna) evita que la búsqueda de instrucciones compita con el acceso a los datos, eliminando cuellos de botella estructurales en el pipeline.

#### Latencias Detalladas (Arquitectura Moderna)
Los siguientes valores son aproximados para una CPU de alto rendimiento a 4.0 GHz:

| Nivel de Memoria | Capacidad Típica | Latencia (Ciclos) | Latencia (ns) | Ancho de Banda (GB/s) |
| :--- | :--- | :--- | :--- | :--- |
| **Registros** | ~1-2 KB | 0 | 0.25 | ~10.000+ |
| **L1 (i+d)** | 32-64 KB | 4 - 5 | 1.0 - 1.2 | ~1.500 |
| **L2** | 256 KB - 2 MB | 12 - 15 | 3.0 - 4.0 | ~800 |
| **L3 (Shared)** | 8 - 64 MB | 40 - 70 | 10.0 - 18.0 | ~300 |
| **RAM (DDR5)** | 16 - 128 GB | 200 - 600 | 50.0 - 150.0 | ~50 - 100 |

### 1.3. Organización Lógica: El Viaje de un Bit
La caché no guarda bytes aislados; guarda **Líneas de Caché (Cache Lines)**, usualmente de 64 bytes. Para la CPU, la memoria no es un continuo de bytes, sino un conjunto de bloques que deben mapearse rígidamente.

#### Cálculo de Direccionamiento de Caché
En una arquitectura de 64 bits con una caché L1 de 32 KB, 8-way set associative, una dirección se descompone así:

1.  **Offset (6 bits):** Como cada línea tiene 64 bytes ($2^6$), los bits 0 a 5 determinan el byte exacto dentro de la línea.
2.  **Index (6 bits):** Calculamos el número de sets. $Sets = \frac{Capacidad}{Línea \times Vías} = \frac{32768}{64 \times 8} = 64$. Necesitamos $\log_2(64) = 6$ bits (bits 6 a 11) para identificar el set.
3.  **Tag (52 bits):** Los bits restantes (12 a 63) sirven para identificar unívocamente si la línea cargada en el set corresponde a la dirección buscada.

**Fórmula de Latencia Promedio (AMAT):**
$AMAT = Hit\_Time + Miss\_Rate \times Miss\_Penalty$
Si el Hit Time es de 1ns y el Miss Penalty (RAM) es de 100ns, un Miss Rate de apenas el 5% duplica el tiempo de acceso promedio ($1 + 0.05 \times 100 = 6ns$). ¡La performance cae un 600%!

```{mermaid}
graph TD
    A[Dirección de 64 bits] --> B{Decodificador}
    B --> C[Tag: bits 12-63]
    B --> D[Index: bits 6-11]
    B --> E[Offset: bits 0-5]
    D --> F[Selecciona Set en SRAM]
    F --> G[Compara Tag con las N-Vías]
    G -- Match --> H[Cache Hit: Envía Byte a CPU]
    G -- No Match --> I[Cache Miss: Pide a L2]
```

### 1.4. Políticas de Escritura: Write-Back vs Write-Through
¿Qué pasa cuando la CPU modifica un dato? No puede enviarlo a la RAM inmediatamente (tardaría 500 ciclos).
- **Write-Through:** La escritura se propaga a todos los niveles de memoria simultáneamente. Es segura pero extremadamente lenta. Solo se usa en sistemas críticos o I/O simple.
- **Write-Back:** El estándar moderno. La CPU modifica el dato en L1 y marca la línea como **Dirty** (sucia). El dato solo se escribe en la L2/RAM cuando la línea es expulsada para dejar lugar a otra. Esto permite "acumular" miles de escrituras en una variable local sin tocar el bus externo.

### 1.5. Inclusión vs Exclusión
- **Cachés Inclusivas:** La L3 contiene una copia de todo lo que hay en L1 y L2. Si un núcleo necesita un dato que tiene otro, lo busca en L3. Gasta espacio pero simplifica la coherencia.
- **Cachés Exclusivas:** El dato solo vive en un nivel. Si se mueve de L2 a L1, se borra de L2. Maximiza la capacidad total pero hace que los fallos sean más caros.

### 1.6. TLB: La Caché de Direcciones
Además de los datos, la CPU debe cachear la traducción de **Direcciones Virtuales** (las que usa tu programa) a **Direcciones Físicas** (las que entiende el IMC).
- El **TLB (Translation Lookaside Buffer)** es una caché ultra-rápida de la tabla de páginas del SO.
- Un **TLB Miss** obliga al hardware a recorrer la tabla de páginas en RAM (*Page Walk*), sumando otros 100ns de latencia antes siquiera de empezar a buscar el dato. Por eso, la localidad espacial no solo ayuda a la caché de datos, sino que evita que el TLB "se olvide" de dónde están tus páginas de memoria.

### 1.7. El Bus de Memoria y el IMC
Antiguamente, la CPU hablaba con la RAM a través de un chip externo (Northbridge). Hoy, el **IMC (Integrated Memory Controller)** está dentro del silicio de la CPU para reducir la latencia.

- **Canales (Channels):** Los sistemas modernos son *Dual-Channel* o *Quad-Channel*. Esto no baja la latencia (el tiempo de acceso es el mismo), pero duplica o cuadruplica el ancho de banda (la cantidad de datos que viajan en paralelo).
- **Ancho del Bus:** Típicamente 64 bits por canal.
- **Cálculo de Ancho de Banda:** $Frecuencia \times Ancho\_Bus \times Canales / 8$. Una DDR5-6000 en Dual Channel ofrece teóricamente 96 GB/s.

:::{important} El Impacto del Salto de Bus
Cruzar el bus de memoria hacia la RAM implica un cambio de dominio de reloj y una espera física por la carga de capacitores. Es un evento "catastrófico" para el pipeline: la CPU puede ejecutar mil instrucciones en el tiempo que tarda un solo viaje de ida y vuelta a la RAM.
:::

## 2. Concurrencia y Coherencia de Caché: El Desafío del Multi-Core

En un mundo multi-core, el problema ya no es solo cuán rápido accedés a la memoria, sino cómo garantizamos que todos los núcleos vean la misma realidad. Si el Núcleo A modifica una variable que el Núcleo B tiene en su caché, el sistema debe invalidar o actualizar la copia de B de forma instantánea.

### 2.1. El Protocolo MESI: El Estándar de Oro
Casi todos los procesadores x86 usan una variante de MESI para mantener la coherencia. Cada línea de caché tiene dos bits adicionales para marcar su estado:

- **Modified (M):** El dato fue modificado localmente y es "sucio" (la RAM está desactualizada). Este núcleo es el único dueño.
- **Exclusive (E):** El dato es idéntico a la RAM y este núcleo es el único que lo tiene.
- **Shared (S):** El dato es idéntico a la RAM pero otros núcleos también tienen una copia.
- **Invalid (I):** El dato en esta línea no es válido y debe recargarse.

#### Máquina de Estados MESI (Mermaid)
```{mermaid}
stateDiagram-v2
    [*] --> Invalid
    Invalid --> Exclusive: Read Miss (No one else has it)
    Invalid --> Shared: Read Miss (Others have it)
    Exclusive --> Modified: Write Hit
    Shared --> Modified: Write Hit (Invalidates others)
    Shared --> Shared: Read Hit
    Modified --> Shared: Read Miss by other (Snoop, Write back to RAM)
    Modified --> Invalid: External Write (Invalidate)
    Shared --> Invalid: External Write (Invalidate)
    Exclusive --> Invalid: External Write (Invalidate)
    Modified --> [*]: Eviction (Write back)
```

### 2.2. Evolución: MOESI y MESIF
- **MOESI (AMD):** Añade el estado **Owned**. Permite compartir una línea modificada entre núcleos sin escribirla a RAM inmediatamente. El "Owner" es responsable de actualizar la RAM cuando la línea se expulse.
- **MESIF (Intel):** Añade el estado **Forward**. Designa a un solo núcleo como el encargado de responder a peticiones de lectura de una línea en estado Shared, evitando que todos los núcleos saturen el bus respondiendo al mismo tiempo.

### 2.3. Estrategias de Comunicación: Snooping vs Directorios
¿Cómo se enteran los núcleos de lo que hacen los demás?
- **Snooping:** Todos los núcleos "escuchan" (husmean) el bus de memoria. Si ven una escritura a una dirección que ellos tienen, invalidan su copia. Escala bien hasta 8-16 núcleos, pero satura el bus en servidores gigantes.
- **Directory-Based:** Un componente central (el Directorio) lleva un registro de qué núcleos tienen qué líneas. Cuando alguien quiere escribir, el Directorio envía mensajes de invalidación solo a los interesados. Es la base de los supercomputadores y sistemas multi-socket.

### 2.4. Optimizando el Caos: Store Buffers e Invalidate Queues
Para no detener la ejecución en cada escritura, la CPU usa trucos sucios:
1. **Store Buffer:** Cuando escribís, el dato se guarda en un buffer rápido y la CPU sigue viaje. El dato se "publica" a la caché más tarde.
2. **Invalidate Queue:** Cuando llega un mensaje de "Invalidate", el núcleo lo mete en una cola y avisa que ya cumplió, pero puede que tarde unos ciclos en borrar realmente el dato de su caché.

**Consecuencia:** Estos buffers son la razón por la cual necesitamos **Barreras de Memoria (Memory Fences)** en programación concurrente. Sin ellas, el orden de las escrituras puede parecer "desordenado" desde la perspectiva de otro núcleo.

### 2.5. Modelos de Consistencia: TSO vs Weak Ordering
- **TSO (Total Store Order) - x86:** Es un modelo "fuerte". Las escrituras no se reordenan con otras escrituras. Esto facilita la vida al programador pero limita un poco al hardware.
- **Weak Ordering - ARM / Apple Silicon:** Es un modelo "relajado". El hardware reordena todo lo que puede para ganar velocidad. Si querés orden, tenés que pedirlo explícitamente con instrucciones `DMB` o `ISB`.
- **Impacto en Localidad:** En ARM, los hilos pueden sufrir más fallos de caché invisibles debido a que la consistencia se delega más al software, forzando al programador a ser más consciente de la topografía del sistema.

## 3. JVM Internals: Anatomía de la Memoria y Layout de Objetos

En Java, no tenemos un operador `sizeof` ni podemos usar aritmética de punteros para recorrer una estructura como lo hacés en C. Esto nos da seguridad, pero nos oculta la realidad física de los datos. La JVM (específicamente HotSpot) gestiona el layout de los objetos siguiendo reglas muy estrictas de alineación y eficiencia que tenés que conocer para no desperdiciar gigabytes de RAM y, sobre todo, para no destruir la localidad de la caché.

### 2.1. Anatomía de un Objeto (The Object Header)
Cada vez que hacés `new MiClase()`, la JVM no solo reserva espacio para tus campos. Existe un "impuesto" de memoria obligatorio llamado **Object Header**. En una arquitectura de 64 bits moderna, este encabezado es la clave de cómo Java gestiona el bloqueo (*locking*), la recolección de basura y el polimorfismo.

El encabezado se divide principalmente en dos partes:

#### 2.1.1. Mark Word (8 bytes - 64 bits)
Es una "navaja suiza" de bits que cambia su significado según el estado del objeto:

| Estado | Bits 0-61 | Bit 62 (Unused) | Bits 63-64 (Lock state) |
| :--- | :--- | :--- | :--- |
| **Normal** | HashCode (31) + Age (4) + Biased (1) | 0 | 01 |
| **Biased** | ThreadID (54) + Epoch (2) + Age (4) | 0 | 01 |
| **Lightweight** | Puntero al stack del hilo | 0 | 00 |
| **Heavyweight** | Puntero al Monitor del SO | 0 | 10 |
| **Marked for GC** | Puntero de reenvío | 0 | 11 |

Este diseño permite que `synchronized` sea casi "gratis" si no hay contención, ya que la JVM solo tiene que escribir el `ThreadID` en el encabezado del objeto (Localidad Temporal perfecta).

#### 2.1.2. Klass Pointer (4 u 8 bytes)
Es una referencia a la metadata de la clase en el **Metaspace**. Cuando llamás a un método polimórfico, la JVM sigue este puntero para encontrar la *vtable* de la clase y saber qué código ejecutar. 

- Si usás `-XX:+UseCompressedClassPointers`, este puntero mide solo 4 bytes, permitiendo que la cabecera total sea de 12 bytes. 
- Debido a la **Alineación de 8 bytes**, un objeto vacío (`new Object()`) ocupará 16 bytes en RAM (12 de header + 4 de padding).

```{mermaid}
graph TD
    subgraph "Objeto en el Heap (64-bit)"
    H[Object Header] --> M[Mark Word: 8 bytes]
    H --> K[Klass Pointer: 4/8 bytes]
    H --> P[Padding: variable]
    F[Fields] --> F1[Campo 1: int]
    F --> F2[Campo 2: boolean]
    F --> F3[...]
    end
```

### 2.2. El Dilema de los 64 bits: Pointer Bloat y Compressed Oops
Cuando la industria migró de 32 a 64 bits, ganamos la capacidad de direccionar más de 4GB de RAM, pero pagamos un precio: las referencias pasaron de medir 4 bytes a medir 8. Esto se conoce como **Pointer Bloat**. Un programa Java típico puede consumir hasta un 50% más de memoria en 64 bits simplemente porque tiene miles de referencias que ahora son el doble de grandes.

Para mitigar esto, HotSpot implementó una optimización magistral: **Compressed Ordinary Object Pointers (Compressed Oops)**.

#### ¿Cómo funcionan los Compressed Oops?
Si tu heap mide menos de 32GB, la JVM no guarda la dirección absoluta en bytes. En cambio, guarda un **índice** de bloques de 8 bytes. Como todos los objetos están alineados a 8 bytes (ver sección 2.3), las direcciones siempre terminan en tres ceros binarios (`...000`).
La JVM "comprime" el puntero eliminando esos ceros. Al leerlo, hace un desplazamiento a la izquierda de 3 bits (`<< 3`) y le suma la dirección base del heap.
- **Beneficio:** Volvés a tener referencias de 4 bytes, ahorrando memoria y, lo más importante, **duplicando la cantidad de referencias que entran en una línea de caché**.
- **El Límite Mágico:** Si asignás un heap de 32GB o más (`-Xmx32g`), la JVM desactiva esta optimización automáticamente porque ya no puede direccionar todo el espacio con 4 bytes. Por eso, **un heap de 31GB suele ser más performante que uno de 33GB**.

### 2.3. Alineación (Alignment) y Padding: El Costo del Silencio
La CPU es eficiente leyendo datos que empiezan en direcciones múltiplos de su palabra natural (8 bytes en 64-bit). Si un `long` cruzara el límite de dos líneas de caché, la CPU tendría que hacer dos lecturas y una fusión de bits, lo cual es lentísimo.

Para evitar esto, la JVM aplica dos tipos de padding:
1.  **Padding Interno:** Reordena los campos de tu clase para que los tipos más grandes estén alineados. Por ejemplo, pondrá todos los `long` y `double` primero, luego los `int`, luego los `short` y al final los `boolean/byte`.
2.  **Padding Externo:** Todo objeto debe tener un tamaño total que sea múltiplo de 8 bytes. Si tu objeto mide 13 bytes, la JVM le suma 3 bytes de basura al final para llegar a 16.

:::{warning} El desastre de los booleanos
Si tenés una clase con 8 campos `boolean`, podrías pensar que ocupa 1 byte. En realidad, debido al header (12-16 bytes) y al padding, ese objeto ocupará al menos 24 bytes. Estás usando 192 bits para guardar 8 bits de información.
:::

### 2.4. Arreglos: Densidad Primitiva vs. Indirección de Objetos
La diferencia entre un arreglo de primitivos y uno de objetos es la diferencia entre el éxito y el fracaso en términos de localidad.

#### Arreglos de Primitivos (`int[]`, `double[]`)
Son bloques contiguos de memoria. El header del arreglo incluye su longitud (4 bytes). Los datos están pegados uno tras otro.
- **Localidad:** Perfecta. El prefetcher de la CPU ve que estás leyendo `arr[i]` y carga preventivamente los siguientes 64 bytes, que contienen los próximos elementos.

#### Arreglos de Objetos (`MiClase[]`)
Un arreglo de objetos es, en realidad, un arreglo de **referencias**. 
1. El arreglo vive en una parte de la memoria.
2. Cada posición del arreglo tiene un puntero (4 u 8 bytes).
3. Esos punteros apuntan a objetos que pueden estar desparramados por todo el heap.

Recorrer este arreglo implica un **Pointer Chasing** constante. La CPU no puede predecir dónde está el siguiente objeto, por lo que cada acceso es un potencial *Cache Miss* de 100ns.

### 2.5. Object Bloat en Colecciones: El Caso de `ArrayList<Integer>`
El "Object Bloat" es el fenómeno donde la metadata y el overhead superan por mucho al dato útil.
Considerá un `ArrayList<Integer>` con 1000 elementos:
- Cada `Integer` es un objeto. Ocupa 12-16 bytes de header + 4 bytes de dato + padding = **16 o 24 bytes**.
- El arreglo de la lista guarda 1000 referencias = **4000 bytes**.
- Total: aprox **20.000 a 28.000 bytes** para guardar 4.000 bytes de datos reales.

Si usaras un `int[]` simple, ocuparías solo **4.016 bytes**. Estás usando entre 5 y 7 veces más memoria y destruyendo la eficiencia de la caché por culpa del *autoboxing*.

### 2.6. Referencias en Java y su Impacto en la Localidad
Java ofrece distintos tipos de referencias (`Strong`, `Weak`, `Soft`, `Phantom`), que gestionan cómo el Garbage Collector interactúa con los objetos. 

- **Strong References:** Las normales. Mantienen al objeto vivo y fijo (hasta que el GC decide moverlo para compactar).
- **Weak/Soft References:** Se usan para cachés o metadatos. El impacto en la localidad es indirecto: estas referencias requieren que el GC trabaje más, moviendo objetos y potencialmente fragmentando el heap si no se gestionan bien.
- **Fragmentación:** Cuando el GC mueve objetos (especialmente en recolectores viejos como CMS), puede que objetos que fueron creados juntos terminen lejos uno del otro, rompiendo la localidad espacial que existía originalmente.

---

## 3. Microarquitectura de Procesadores Modernos

El hardware no es un ejecutor pasivo; es un motor de predicción masivo.

### 3.1. Ejecución Out-of-Order (OoO) y Reorder Buffer (ROB)
Cuando ocurre un *Cache Miss*, el procesador no se detiene.
1. La instrucción bloqueada se guarda en el **ROB**.
2. La CPU busca instrucciones posteriores que no dependan del dato faltante y las ejecuta de forma especulativa.
3. El ROB tiene un límite físico (~200-500 entradas). Si el dato de la RAM tarda 500 ciclos, el ROB se satura y la CPU se detiene totalmente (**Stall**). La localidad es lo que permite que el ROB siempre tenga trabajo productivo.

### 3.2. Prefetching: La Anticipación como Estrategia
- **Hardware Prefetcher:** Circuito que detecta patrones `arr[i], arr[i+1]`.
- **Stride Prefetcher:** Detecta saltos constantes como `arr[i], arr[i+16]` (muy común en arreglos de estructuras).
- **Line Fill Buffers (LFB):** Gestionan las peticiones pendientes. Saturar los LFB es una de las formas más comunes de degradar la performance en sistemas multihilo.

## 4. Localidad en Estructuras de Datos (Análisis Detallado)

En esta sección vamos a bajar al barro. Ya sabés que un arreglo es mejor que una lista, pero ¿por qué una Tabla Hash con *Linear Probing* le pasa el trapo a una con *Chaining*? ¿Por qué los grafos en producción no usan listas de adyacencia? Vamos a desmenuzarlo.

### 4.1. Tablas Hash: La Batalla por la Caché
En la facultad solemos enseñar el manejo de colisiones mediante **Chaining** (listas enlazadas en cada slot) porque es fácil de analizar matemáticamente. Pero en el hardware real, el encadenamiento es un desastre para la performance.

#### Chaining (Encadenamiento) y el "Pointer Chasing"
Cada colisión genera un nuevo nodo en una lista. Como vimos en el punto 2.4, cada nodo es un objeto independiente en el heap. 
- **El problema:** Para buscar una clave, tenés que saltar de puntero en puntero. Cada salto es un potencial *Cache Miss*. Si la tabla está muy llena, el tiempo de búsqueda deja de ser $O(1)$ en la práctica para convertirse en $O(\text{Latencia RAM} \times \text{Colisiones})$.
- **La realidad:** En una arquitectura moderna, una comparación de `int` en la caché L1 toma ~0.5ns. Un salto a RAM toma 100ns. Podés hacer 200 comparaciones en un arreglo contiguo en el tiempo que te toma seguir un solo puntero a un nodo de lista.

#### Linear Probing (Sondeo Lineal): Densidad de Datos
Acá, si hay colisión, buscás el dato en el siguiente slot libre del mismo arreglo.
- **Cache-friendliness:** Como los slots son contiguos, cuando la CPU carga el slot $i$, el prefetcher de hardware ya detectó el patrón de acceso secuencial y te trajo los slots $i+1$ a $i+15$ (en una línea de 64 bytes). 
- **Dominio en Memoria Principal:** Librerías de alta performance como *fastutil* o *Koloboke* usan sondeo lineal (o variantes como *Cuckoo Hashing*) específicamente por esto. El "clustering" es un problema algorítmico que se soluciona con buenas funciones de hash; el "cache miss" es un problema físico que no tiene solución por software más que cambiando el layout.

### 4.2. Tries: El Dilema del Espacio vs. Tiempo
Los Tries (o árboles de prefijos) son estructuras maravillosas para buscar strings, pero su implementación ingenua es una pesadilla de punteros.

- **Trie Tradicional:** Cada nodo tiene un arreglo de 26 a 256 punteros. El desperdicio de memoria es brutal (densidad bajísima) y la localidad es nula.
- **Array-Mapped Tries (AMT):** En lugar de un arreglo de punteros vacío, usan un `long` (64 bits) como máscara de bits. Si el bit $k$ está en 1, el $k$-ésimo hijo existe. Los punteros reales se guardan en un arreglo compacto sin huecos. Para encontrar el hijo $k$, hacés `Long.bitCount(mask & ((1L << k) - 1))`. Esto se resuelve con la instrucción `POPCNT` de la CPU en 1 ciclo.
- **HAT-tries (Hash Array Mapped Tries):** Dividen el trie en niveles. Los niveles superiores son un trie normal, pero las hojas son "Pure Buckets" (arreglos contiguos de strings). Esto permite que la fase final de la búsqueda sea un recorrido lineal ultra-rápido en caché.

### 4.3. Grafos: Del Concepto al Metal
Si implementás un grafo con `Map<Node, List<Edge>>`, estás suicidando la performance. La indirección masiva de los `Map` rompe cualquier intento de la CPU por predecir el acceso.

#### CSR (Compressed Sparse Row): El Rey del Rendimiento
Este es el estándar en computación científica (ej. multiplicación de matrices ralas) y motores de grafos de alto rendimiento. Se usan tres arreglos compactos:
1. **Values:** Los pesos de las aristas (si tiene).
2. **Column Indices:** El ID del nodo destino para cada arista.
3. **Row Pointers:** Un arreglo de tamaño $V+1$ donde `row_ptr[i]` indica dónde empiezan las aristas del nodo $i$ en el arreglo de columnas.

**¿Por qué vuela?** 
Para recorrer los vecinos del nodo `u`, simplemente hacés:
```java
for (int i = rowPtr[u]; i < rowPtr[u+1]; i++) {
    int v = colIndices[i];
    // Procesar arista (u, v)
}
```
Todo el recorrido es secuencial sobre `colIndices`. La CPU carga las líneas de caché de forma masiva. No hay objetos, no hay headers, no hay punteros. Es el límite físico de la velocidad.

---

## 5. Casos de Estudio de Optimización (Transformaciones de Lazos)

El compilador (y vos, si el compilador no es tan vivo) puede transformar la estructura de tus lazos para que "encajen" mejor en la jerarquía de memoria.

### 5.1. Loop Tiling (o Blocking): Venciendo al Tamaño de la Caché
El tiling es la técnica de dividir un dataset que no cabe en L1 in sub-bloques que sí quepan.

**Análisis de Multiplicación de Matrices:**
Sea $C = A \times B$. En el algoritmo $O(N^3)$ estándar, por cada elemento de una fila de $A$, recorres una **columna** entera de $B$. Si la matriz es de $2000 \times 2000$, cada elemento de la columna de $B$ está a 16KB de distancia del anterior. Cada acceso a $B$ es un *Cache Miss*.

**Solución con Tiling:**
```java
for (int ii = 0; ii < N; ii += B)
    for (int jj = 0; jj < N; jj += B)
        for (int kk = 0; kk < N; kk += B)
            // Procesar bloque de B x B
            for (int i = ii; i < ii + B; i++)
                for (int j = jj; j < jj + B; j++)
                    for (int k = kk; k < kk + B; k++)
                        C[i][j] += A[i][k] * B[k][j];
```
Al procesar bloques de tamaño $B$, los datos de $B$ que cargaste para la primera fila se quedan en la L1 para la segunda, tercera, etc. El reuso temporal pasa de ser casi nulo a ser total.

### 5.2. Loop Interchange: La Importancia del Layout
Consiste en intercambiar el orden de los lazos anidados para coincidir con el almacenamiento en memoria. Java usa *Row-Major* (filas contiguas).
```java
// Mal: Atraviesa la memoria en zancadas de N (Cache Misses)
for (int j = 0; j < N; j++)
    for (int i = 0; i < N; i++)
        sum += matrix[i][j];

// Bien: Acceso secuencial (Prefetcher feliz)
for (int i = 0; i < N; i++)
    for (int j = 0; j < N; j++)
        sum += matrix[i][j];
```

### 5.3. Loop Fission, Fusion y Unrolling
- **Fission:** Si un lazo hace `A[i] = ...; B[i] = ...;`, y los arreglos $A$ y $B$ compiten por los mismos sets de la caché (Conflict Misses), dividirlos en dos lazos separados puede mejorar la tasa de aciertos.
- **Fusion:** Si dos lazos recorren el mismo arreglo, combinarlos ahorra una carga completa desde RAM.
- **Unrolling (Desenrollado):** `for(int i=0; i<N; i+=4) { process(i); process(i+1); ... }`. Reduce el overhead del control del lazo y permite a la CPU ejecutar múltiples operaciones en paralelo mediante el *Pipeline*.

### 5.4. Software Prefetching y Non-Temporal Stores
- **Prefetching:** Instrucciones que le dicen a la CPU: "Voy a necesitar este dato en 200 ciclos, empezá a traerlo".
- **Non-Temporal Stores:** Cuando escribís un arreglo gigante que no vas a volver a leer pronto, podés usar instrucciones que escriben directamente en RAM saltándose la caché (*Streaming Stores*). Esto evita "ensuciar" la caché con datos que no vas a reusar, dejando espacio para datos más importantes.

---

## 6. Sistemas Operativos y Localidad: El Piso de la Computación

La localidad no termina en tu código; el Sistema Operativo tiene una influencia masiva en cómo tus datos se mueven por el silicio.

### 6.1. NUMA (Non-Uniform Memory Access)
En servidores con múltiples sockets (CPUs), la RAM está físicamente conectada a un socket específico.
- **Local Access:** La CPU 0 accede a su RAM local. Latencia mínima.
- **Remote Access:** La CPU 0 accede a la RAM de la CPU 1 a través de un bus de interconexión (QPI/UPI en Intel, Infinity Fabric en AMD). 
**El costo:** El acceso remoto no solo es más lento (~2x latencia), sino que consume ancho de banda del bus de interconexión, ralentizando a todas las CPUs. Herramientas como `numactl` en Linux permiten forzar que un proceso solo use memoria local.

### 6.2. Afinidad de CPU e Invalidez de Caché
El planificador del SO es un mal necesario. Si mueve tu hilo del Core 1 al Core 2:
1. **L1/L2 Cold Start:** El Core 2 no tiene tus datos. Stall masivo mientras se llena la caché.
2. **Coherencia:** Si el Core 1 tenía datos "sucios" (Modified), deben escribirse a L3/RAM antes de que el Core 2 pueda leerlos.
**Afinidad:** Anclar hilos a núcleos (`taskset` o librerías como Java-Thread-Affinity) es vital en sistemas de baja latencia.

### 6.3. El Abismo de la Paginación: Page Faults
El SO gestiona la memoria en páginas (4KB). 
- **Minor Page Fault:** La página está en RAM pero no mapeada al proceso. Rápido.
- **Major Page Fault:** La página está en el swap (Disco). **Desastre.** 
La diferencia de velocidad entre RAM (100ns) y un SSD NVMe (10.000ns) es de dos órdenes de magnitud. Si tu algoritmo no tiene localidad y "salta" mucho entre páginas, el SO va a estar constantemente cargando y descargando datos del disco, un fenómeno llamado *Thrashing*.

---

## 7. Algoritmos Cache-Oblivious y Cache-Aware

¿Cómo diseñamos algoritmos que sean rápidos en cualquier máquina?

### 7.1. Modelo de Memoria Externa (Idealized Cache Model)
Analizamos el costo basándonos en:
- **M:** Tamaño de la memoria rápida (Caché).
- **B:** Tamaño del bloque de transferencia (Línea de caché o Página).
El objetivo es minimizar $Q(N, M, B)$, la cantidad de transferencias.

### 7.2. Algoritmos Cache-Aware
Se diseñan conociendo $B$ y $M$. 
- **Ejemplo:** Un B-Tree donde el tamaño del nodo es exactamente igual a una página del SO (4KB) o una línea de caché (64B). Esto garantiza que cada nodo se cargue con una sola operación de E/S.

### 7.3. Algoritmos Cache-Oblivious: La Perfección Recursiva
Funcionan de forma óptima para **cualquier** $B$ y $M$ sin conocerlos. Se basan en la técnica de **Dividir y Conquistar**.

- **Van Emde Boas Layout:** Imagina un árbol binario. En lugar de guardarlo por niveles, lo dividimos a la mitad de su altura. El subárbol superior y los inferiores se guardan recursivamente.
**Resultado:** Para cualquier nivel de la jerarquía (L1, L2, L3, RAM), existe un nivel de recursión donde el subárbol encaja perfectamente en un bloque de ese nivel.
- **Static B-Trees (Layout de Prokop):** Una búsqueda binaria sobre este layout realiza $O(\log_B N)$ transferencias de memoria, igual que un B-Tree, pero ¡sin conocer $B$!

#### Caso de Estudio: Transposición de Matrices Cache-Oblivious
Una transposición $A^T$ ingenua tiene un costo de $O(N^2/B)$ transferencias si se lee por filas, pero la escritura por columnas genera $O(N^2)$ fallos de caché.
El algoritmo *cache-oblivious* divide la matriz en 4 cuadrantes y traspone cada uno recursivamente. 
```java
void transpose(int r0, int c0, int r1, int c1) {
    if (r1 - r0 <= threshold && c1 - c0 <= threshold) {
        // Caso base: transposición normal en bloque pequeño
        return;
    }
    if (r1 - r0 >= c1 - c0) {
        transpose(r0, c0, (r0+r1)/2, c1);
        transpose((r0+r1)/2, c0, r1, c1);
    } else {
        transpose(r0, c0, r1, (c0+c1)/2);
        transpose(r0, (c0+c1)/2, r1, c1);
    }
}
```
**Análisis:** A medida que la recursión divide la matriz, llega un punto donde el bloque cabe en L1. En ese momento, todas las lecturas y escrituras ocurren a velocidad de registros. Lo más asombroso es que el mismo código optimiza para L1, L2, L3 y TLB simultáneamente sin que vos hayas definido sus tamaños.

---

## 8. El Futuro: Proyecto Valhalla y Memoria Heterogénea

**Valhalla** es la respuesta de la JVM al *Memory Wall*. Traerá los **Value Types** (o *Inline Types*).
- **Inlining:** Un arreglo de `ComplexNumber(double re, double im)` hoy es un arreglo de referencias. Con Valhalla, será un bloque de doubles contiguos: `re, im, re, im...`.
- **Impacto:** Se elimina la indirección, el header de objeto y el padding. La densidad de datos en la caché L1 se cuadruplica.

### Memoria Heterogénea (CXL y Persistent Memory)
El futuro trae memorias intermedias entre la RAM y el SSD (como Intel Optane o dispositivos CXL). La localidad ya no será solo una cuestión de "caché o RAM", sino de mover datos entre distintos niveles de latencia de forma dinámica.

---

## 9. Benchmarks y JMH: La Ciencia de Medir

Nunca midas performance con `System.currentTimeMillis()`. La JVM es un sistema dinámico:
1. **JIT Compilation:** El código empieza lento (interpretado) y se vuelve rápido después de miles de ejecuciones.
2. **Dead Code Elimination:** Si hacés un benchmark de un bucle vacío, el JIT lo va a borrar.
3. **Pausas de GC:** Un milisegundo de pausa puede arruinar tu métrica.

**JMH (Java Microbenchmark Harness)** es la herramienta estándar. Maneja el "warmup", evita optimizaciones agresivas irreales y provee métricas precisas de ciclos por instrucción y throughput de memoria.

---

## 10. Herramientas de Diagnóstico y Métricas Reales

Si querés ser un ingeniero de performance, tenés que dejar de adivinar y empezar a medir.

### 10.1. Linux Perf: Los Contadores de Hardware
`perf` permite acceder a los contadores de la CPU (PMU - Performance Monitoring Unit).
- `perf stat -e cache-misses,cache-references,L1-dcache-load-misses ./mi_programa`
Te dirá exactamente cuántos fallos de caché tuviste. Si tu tasa de fallos supera el 5-10%, tenés un problema de localidad serio.

### 10.2. Valgrind / Cachegrind
Valgrind emula una CPU y simula el comportamiento de la caché.
- **Ventaja:** Te da un reporte línea por línea de qué parte de tu código está causando los fallos.
- **Desventaja:** Es extremadamente lento (tu programa corre 20-50 veces más lento).

### 10.3. JOL (Java Object Layout)
Es la herramienta imprescindible para entender qué está pasando dentro del heap de Java.
```java
System.out.println(ClassLayout.parseClass(MiClase.class).toPrintable());
```
Te muestra el header, los campos, el padding y el tamaño total. Es la forma de ver "la Matrix" de tus objetos.

### 10.4. Intel VTune y AMD uProf
Son los "telescopios espaciales" de la optimización. Permiten ver el flujo de datos por el bus, identificar *False Sharing* y detectar stalls del pipeline con precisión de nanosegundos.

---

## 11. Comparativa: Localidad en C++, Rust, Go y Java

No todos los lenguajes tratan la memoria de la misma forma. Entender estas diferencias te va a permitir elegir la herramienta adecuada para cada problema de performance.

### 11.1. C++: El Control Total (y el Peligro Total)
En C++, podés elegir si un objeto vive en el stack o en el heap. 
- **Arreglos de Objetos:** Un `std::vector<Point>` en C++ guarda los `Point` de forma contigua por defecto. No hay indirección. La performance es máxima.
- **Custom Allocators:** Podés escribir tu propio gestor de memoria para asegurar que objetos relacionados vivan en la misma página, minimizando los *TLB Misses*.

### 11.2. Rust: Seguridad sin Costo de Localidad
Rust ofrece el mismo control que C++ pero con garantías de seguridad. 
- **Ownership:** Al no tener Garbage Collector, los objetos se liberan inmediatamente, evitando que la caché se ensucie con objetos "muertos" que el GC aún no procesó.
- **Data Layout:** Al igual que C++, fomenta el uso de tipos "unboxed", lo que resulta en una localidad espacial excelente por defecto.

### 11.3. Go: El Punto Medio
Go tiene GC, pero permite definir "Value Types" (structs) que pueden anidarse sin punteros.
- **Interior Pointers:** Podés tener un puntero al medio de un struct. Esto permite que el GC sea más eficiente y que la localidad se mantenga incluso cuando usás punteros.
- **Escape Analysis:** El compilador de Go intenta poner todo lo posible en el stack para evitar el overhead del heap y mejorar la localidad temporal.

### 11.4. Java: El Peso de la Abstracción (hacia Valhalla)
Java es el más alejado del metal. Todo es un objeto, todo es una referencia (por ahora). La localidad depende casi exclusivamente de la suerte y del trabajo del GC compactador. Por eso, entender estos conceptos es **más crítico** en Java que en C++, porque en Java tenés que "luchar" contra el lenguaje para obtener performance.

---

## 12. Patrones de Diseño Amigables con la Caché (Cache-Friendly Design)

Para cerrar este bloque teórico, repasemos los patrones que separan a los programadores junior de los expertos en performance.

1.  **Data Oriented Design (DOD):** En lugar de "Objetos que tienen Datos", pensamos en "Datos que son procesados por Funciones". Priorizamos arreglos de estructuras simples.
2.  **SoA (Structure of Arrays) vs AoS (Array of Structures):**
    - **AoS:** `[ {x,y,z}, {x,y,z} ... ]`. Mejor para localidad si siempre usás x, y, z juntos.
    - **SoA:** `{ [x,x...], [y,y...], [z,z...] }`. Mejor para procesamiento SIMD/vectorial si solo necesitás operar sobre un eje a la vez.
3.  **Object Pooling:** Reutilizar objetos para evitar que el GC fragmente el heap y rompa la localidad espacial de los datos calientes.
4.  **Bit-Packing:** Usar máscaras de bits para meter múltiples booleanos o estados en un solo `int` o `long`, maximizando la densidad de información en la L1.

---

## 13. Ejercicios Técnicos de Alto Nivel (1 a 100)

Resoluciones exhaustivas que analizan el flujo de datos, la microarquitectura y el impacto asintótico real.

---

### Ejercicio 1: Análisis de Stall y CPI
**Consigna:** Un procesador de 4GHz tiene un IPC ideal de 2. El 20% de las instrucciones son cargas a memoria. El 5% de esas cargas fallan en la L3 (RAM = 100ns). Calculá el IPC real.

:::{solution} ex-p6-1
:class: dropdown
**Resolución Detallada:**
1.  **Parámetros de Tiempo:**
    - Frecuencia: 4 GHz $\Rightarrow$ Ciclo = 0.25 ns.
    - Latencia RAM en ciclos: $100 \text{ ns} / 0.25 \text{ ns/ciclo} = 400$ ciclos.
2.  **Penalización por Instrucción:**
    - Probabilidad de instruccion de memoria: 0.20.
    - Probabilidad de miss total: 0.05.
    - Probabilidad global de miss: $0.20 \times 0.05 = 0.01$ (1 de cada 100 instrucciones).
    - Penalización promedio por instrucción: $0.01 \times 400 \text{ ciclos} = 4$ ciclos/inst.
3.  **Cálculo de CPI e IPC:**
    - CPI Ideal ($1 / \text{IPC ideal}$): $1 / 2 = 0.5$ ciclos/inst.
    - CPI Real: $0.5 \text{ (ideal)} + 4 \text{ (stall)} = 4.5$ ciclos/inst.
    - IPC Real: $1 / 4.5 \approx 0.222$.
4.  **Análisis de Impacto:**
    - El procesador está ejecutando instrucciones al **11.1%** de su capacidad teórica ($0.222 / 2.0$). 
    - El procesador pasa el **88.9%** de su tiempo esperando al bus de memoria.
:::

---

### Ejercicio 2: Traza del Protocolo MESI
**Consigna:** En un sistema de dos núcleos (N1, N2), describí los estados de la línea de caché de la variable `X` (inicialmente en RAM) tras esta secuencia: 1. N1 lee X, 2. N2 lee X, 3. N1 escribe en X, 4. N2 lee X.

:::{solution} ex-p6-2
:class: dropdown
**Análisis de Coherencia Paso a Paso:**
1.  **N1 lee X:** 
    - La línea se carga de RAM. 
    - Estado en N1: **Exclusive (E)**. 
    - N1 sabe que es el único que tiene el dato y que es idéntico a la RAM.
2.  **N2 lee X:** 
    - N1 detecta la petición de lectura en el bus (*Snooping*).
    - N1 responde con el dato y marca su línea como **Shared (S)**.
    - Estado en N2: **Shared (S)**.
    - Ambos núcleos tienen una copia limpia del dato.
3.  **N1 escribe en X:** 
    - N1 debe obtener exclusividad antes de modificar.
    - Envía una señal de "Invalidate" por el bus.
    - N2 recibe la señal y marca su copia como **Invalid (I)**.
    - N1 modifica el dato localmente y marca la línea como **Modified (M)**.
    - La RAM ahora tiene un valor obsoleto; N1 es el "dueño" de la verdad.
4.  **N2 lee X:** 
    - N2 tiene un fallo de caché (está en estado I).
    - Envía petición de lectura al bus.
    - N1 detecta el pedido de una línea que tiene en estado M.
    - N1 debe escribir el dato en RAM o pasarlo directamente a N2.
    - Finalmente, ambos núcleos actualizan sus estados a **Shared (S)** con el nuevo valor.
:::

---

### Ejercicio 3: Cálculo de Overhead JOL
**Consigna:** Calculá el desperdicio de memoria (overhead) de un arreglo `Integer[1000]` comparado con un `int[1000]` en una JVM de 64 bits con punteros comprimidos.

:::{solution} ex-p6-3
:class: dropdown
**Cálculo de Densidad de Datos:**
1.  **Versión int[1000]:**
    - Header del arreglo: 12 bytes + 4 bytes (length) = 16 bytes.
    - Datos: $1000 \times 4 \text{ bytes} = 4000$ bytes.
    - Total: **4016 bytes**.
2.  **Versión Integer[1000]:**
    - Header del arreglo: 16 bytes.
    - Referencias: $1000 \times 4 \text{ bytes} = 4000$ bytes.
    - Objetos Integer: Cada uno tiene Header (12) + valor (4) = 16 bytes.
    - Total objetos: $1000 \times 16 = 16000$ bytes.
    - Total general: $16 + 4000 + 16000 = \mathbf{20016}$ bytes.
3.  **Análisis de Eficiencia:**
    - La versión de objetos usa **5 veces más memoria** ($20016 / 4016$).
    - El "overhead" es del **400%**.
:::

---

### Ejercicio 4: Mecánica del False Sharing
**Consigna:** Tenés una clase `Counter { public volatile long a; public volatile long b; }`. Dos hilos incrementan `a` y `b` respectivamente. Si la línea de caché es de 64 bytes, ¿por qué hay degradación y cómo la cuantificarías?

:::{solution} ex-p6-4
:class: dropdown
**Análisis Microarquitectónico:**
1.  **Layout Físico:** `a` ocupa 8 bytes, `b` ocupa 8 bytes. El header ocupa 12. Total objeto: 32 bytes (con padding).
2.  **Línea de Caché:** Como el objeto mide 32 bytes y la línea 64, es casi seguro que `a` y `b` residan en la **misma línea física**.
3.  **Conflicto de Coherencia:** Cuando el Hilo 1 escribe en `a`, marca la línea entera como **Modified (M)**. El protocolo MESI invalida la línea en el núcleo del Hilo 2.
4.  **Penalización:** El Hilo 2, al intentar incrementar `b`, sufre un fallo de caché. Debe esperar a que la línea viaje desde el Hilo 1. Esto ocurre en cada iteración de ambos hilos.
:::

---

### Ejercicio 5: Cobertura del TLB
**Consigna:** Un procesador tiene un TLB de L1 de 64 entradas para páginas de 4KB. ¿Cuánta memoria puede direccionar "rápidamente" antes de sufrir un TLB Miss? Comparalo con el impacto de usar Huge Pages de 2MB.

:::{solution} ex-p6-5
:class: dropdown
**Cálculo de Memoria Virtual:**
1.  **Páginas estándar (4KB):**
    - Cobertura = $64 \text{ entradas} \times 4 \text{ KB/entrada} = \mathbf{256 \text{ KB}}$.
2.  **Huge Pages (2MB):**
    - Cobertura = $64 \text{ entradas} \times 2 \text{ MB/entrada} = \mathbf{128 \text{ MB}}$.
:::

---

### Ejercicio 6: Cache-Oblivious Matrix Transpose
**Consigna:** Explicá por qué el algoritmo de "Dividir y Conquistar" para transponer una matriz es óptimo para la caché sin conocer su tamaño.

:::{solution} ex-p6-6
:class: dropdown
**Análisis Algorítmico Avanzado:**
1.  **Problema del Tiling Tradicional:** Requiere conocer el `BLOCK_SIZE` exacto de la L1 (ej. 32 o 64). Si el programa corre en otra CPU, deja de ser óptimo.
2.  **Estrategia Oblivious:** El algoritmo divide la matriz $N \times N$ en 4 cuadrantes recursivamente hasta llegar a un caso base.
3.  **Mecanismo de Optimización:** Inevitablemente, se llega a un nivel donde el sub-bloque entra perfectamente en la L1, luego en la L2, etc. Se logra el número mínimo de transferencias de RAM a CPU ($O(N^2 / B)$) sin conocer $B$.
:::

---

### Ejercicio 7: El Costo del Pointer Chasing
**Consigna:** Si recorrer un arreglo de 1 millón de `long` toma 1ms, ¿cuánto esperarías que tome recorrer una `LinkedList<Long>` de 1 millón de elementos, asumiendo latencia de RAM de 100ns?

:::{solution} ex-p6-7
:class: dropdown
**Modelado de Latencia Real:**
1.  **Arreglo:** El prefetcher carga las líneas de forma masiva. Latencia efectiva mínima.
2.  **Lista Enlazada:** No puede pedir el nodo $i+1$ hasta que no tiene el dato del nodo $i$ (dependencia serial).
3.  **Cálculo:** $1,000,000 \times 100 \text{ ns} = 100 \text{ ms}$. La lista es **100 veces más lenta**.
:::

---

### Ejercicio 8: Asociatividad y Conflict Misses
**Consigna:** Tenés una caché de 2 vías con 4 sets totales. Tu programa accede repetidamente a las direcciones 0, 4, 8, 12, 16. Si el mapeo es `Dirección % 4`, describí el comportamiento de la caché.

:::{solution} ex-p6-8
:class: dropdown
**Simulación de Trazas de Caché:**
1.  **Mapeo de Direcciones:** 0, 4, 8, 12, 16 todos dan resto 0 al dividir por 4.
2.  **Conflicto:** Todas las direcciones pelean por el Set 0. Como solo hay 2 vías, solo pueden vivir 2 a la vez.
3.  **Resultado:** Tasa de fallos del **100%**. Esto se llama **Thrashing**.
:::

---

### Ejercicio 9: Write-Through vs Write-Back
**Consigna:** ¿Por qué las arquitecturas modernas no usan Write-Through para la caché L1?

:::{solution} ex-p6-9
:class: dropdown
**Análisis de Ancho de Banda:**
1.  **Write-Through:** Cada escritura se envía inmediatamente a la RAM. El bus colapsaría instantáneamente intentando procesar 4 mil millones de escrituras por segundo.
2.  **Write-Back:** Permite "acumular" miles de escrituras en la misma variable local sin tocar la RAM hasta que la línea es expulsada.
:::

---

### Ejercicio 10: Alineación y AVX-512
**Consigna:** Las instrucciones AVX-512 procesan 64 bytes a la vez. ¿Qué pasa si el arreglo de datos empieza en la dirección de memoria 31?

:::{solution} ex-p6-10
:class: dropdown
**Análisis de Carga Fraccionada:**
1.  **Carga Desalineada:** Los datos cruzan el límite de dos líneas de caché.
2.  **Impacto:** El hardware debe emitir **dos lecturas** y fusionar los bits. El acceso es **2 a 3 veces más lento**.
:::

---

### Ejercicio 11: Paginación y Localidad Temporal
**Consigna:** ¿Cómo afecta un tamaño de página pequeño (ej. 1KB en lugar de 4KB) a la localidad de memoria de un programa que recorre un arreglo?

:::{solution} ex-p6-11
:class: dropdown
**Análisis de Gestión de Memoria:**
1.  **TLB Pressure:** Con páginas de 1KB, necesitas 4 veces más entradas en el TLB para cubrir la misma cantidad de memoria.
2.  **Impacto:** Aumenta masivamente los **TLB Misses**, ralentizando la traducción de direcciones virtuales.
:::

---

### Ejercicio 12: Estructuras Compactas en C vs Java
**Consigna:** En C, `struct { char a; int b; char c; }` ocupa 12 bytes. ¿Por qué Java (HotSpot) suele ser más eficiente en el layout interno de campos?

:::{solution} ex-p6-12
:class: dropdown
**Análisis de Field Reordering:**
1.  **HotSpot JVM:** Tiene libertad total para reordenar campos. Detecta que `b` debe ir primero, seguido de `a` y `c`. 
2.  **Resultado:** El objeto mide 8 bytes en lugar de 12. Menos padding, más densidad en caché.
:::

---

### Ejercicio 13: Búsqueda Binaria vs Lineal en Caché
**Consigna:** ¿Por qué para un arreglo de 64 elementos `int`, una búsqueda lineal suele ser más rápida que una binaria?

:::{solution} ex-p6-13
:class: dropdown
**Análisis de Microarquitectura:**
1.  **Búsqueda Lineal:** Acceso secuencial puro. El prefetcher carga todo de una vez.
2.  **Búsqueda Binaria:** Salta aleatoriamente, causando múltiples lecturas de caché independientes y fallos constantes del Branch Predictor.
:::

---

### Ejercicio 14: Garbage Collection y Localidad Espacial
**Consigna:** ¿Cómo influye un GC compactador (como G1) en la localidad de una aplicación?

:::{solution} ex-p6-14
:class: dropdown
**Análisis:** El GC actúa como una herramienta de desfragmentación. Al mover los objetos vivos y ponerlos uno al lado del otro, restaura la localidad espacial que el programa perdió por su propia dinámica de asignación.
:::

---

### Ejercicio 15: Hyper-threading y Caché L1
**Consigna:** Si dos hilos corren en el mismo núcleo físico compartiendo la L1, ¿cómo afecta esto al rendimiento?

:::{solution} ex-p6-15
:class: dropdown
**Análisis:** La L1 efectiva se divide. Si ambos hilos usan mucha memoria, ocurre **Cache Thrashing**, donde un hilo expulsa los datos del otro constantemente.
:::

---

### Ejercicio 16: El Costo de `volatile` y Barreras
**Consigna:** Explicá el impacto de una variable `volatile` en el *Store Buffer* de la CPU.

:::{solution} ex-p6-16
:class: dropdown
**Análisis de Consistencia de Memoria:**
1.  **Escritura Normal:** La CPU escribe en el *Store Buffer* y sigue ejecutando.
2.  **Escritura Volatile:** La CPU **debe detenerse** hasta que el Store Buffer esté vacío y la escritura sea confirmada por la jerarquía de caché. Puede ser **100 veces más lenta**.
:::

---

### Ejercicio 17: Prefetcher y Listas Circulares
**Consigna:** ¿Por qué un prefetcher de hardware no puede ayudar en el recorrido de una lista circular donde los nodos se asignaron aleatoriamente?

:::{solution} ex-p6-17
:class: dropdown
**Análisis:** El prefetcher busca secuencias aritméticas. En una lista aleatoria, la dirección del siguiente nodo no tiene relación matemática con el actual. La CPU sufre la latencia completa de la RAM en cada salto.
:::

---

### Ejercicio 18: Inline Caching y L1-Instrucciones
**Consigna:** ¿Cómo mejora el *Inline Caching* la localidad de las instrucciones en la caché L1i?

:::{solution} ex-p6-18
:class: dropdown
**Análisis:** Reemplaza una llamada polimórfica (vtable) por un salto directo si la clase es siempre la misma. Esto permite que las instrucciones fluyan de forma lineal, maximizando el aprovechamiento de la L1i.
:::

---

### Ejercicio 19: Mechanical Sympathy en RingBuffers
**Consigna:** ¿Por qué el tamaño de un RingBuffer debe ser siempre una potencia de 2?

:::{solution} ex-p6-19
:class: dropdown
**Análisis:** Convierte la operación módulo (`%`) lenta en un `AND` bit a bit ultra-rápido. Esto libera a la CPU para enfocarse en la pre-carga de los datos del buffer.
:::

---

### Ejercicio 20: El costo de los "Value Types" en Proyecto Valhalla
**Consigna:** ¿Cómo cambiará la localidad de memoria con los Inline Types de Java?

:::{solution} ex-p6-20
:class: dropdown
**Análisis:** Se elimina el header del objeto y el padding. Un arreglo de objetos complejos se convierte en un bloque contiguo de datos. La densidad en caché se cuadruplica y se elimina el *Pointer Chasing*.
:::

---

### Ejercicio 21: Análisis de latencia en RAM LPDDR5
**Consigna:** ¿Cuál es la ventaja de la memoria LPDDR5 en dispositivos móviles en términos de localidad?

:::{solution} ex-p6-21
:class: dropdown
**Análisis:** Usa un bus de datos más ancho y voltajes dinámicos. Su principal ventaja es que reduce la latencia de "despertar" de estados de bajo consumo, mejorando la localidad temporal en aplicaciones interactivas.
:::

---

### Ejercicio 22: Localidad en el algoritmo de búsqueda DFS
**Consigna:** ¿Por qué una DFS (Depth-First Search) suele tener mejor localidad que una BFS (Breadth-First Search)?

:::{solution} ex-p6-22
:class: dropdown
**Análisis:** DFS usa un stack y tiende a procesar nodos hijos que acaban de ser creados o accedidos (localidad temporal). BFS usa una cola y salta entre niveles lejanos del árbol, causando más cache misses.
:::

---

### Ejercicio 23: El impacto de los parches de Microcódigo
**Consigna:** ¿Cómo puede un parche de microcódigo de CPU afectar el rendimiento de la caché?

:::{solution} ex-p6-23
:class: dropdown
**Análisis:** Algunos parches deshabilitan optimizaciones especulativas para cerrar agujeros de seguridad. Esto puede forzar la serialización de accesos a memoria, haciendo que cada cache miss sea mucho más costoso.
:::

---

### Ejercicio 24: Localidad en el diseño de un Garbage Collector ZGC
**Consigna:** ZGC no tiene pausas "Stop-the-World" largas. ¿Cómo afecta esto a la localidad de los hilos de aplicación?

:::{solution} ex-p6-24
:class: dropdown
**Análisis:** Como el GC corre de forma concurrente, compite por la caché con la aplicación. Sin embargo, al usar "Colored Pointers", evita leer headers de objetos, lo que reduce el impacto en la caché de datos de la aplicación.
:::

---

### Ejercicio 25: El costo de la indirección en `std::vector<std::string>`
**Consigna:** Compará la localidad de un `std::vector<char>` vs un `std::vector<std::string>` en C++.

:::{solution} ex-p6-25
:class: dropdown
**Análisis:** El vector de chars es contiguo. El vector de strings es un arreglo de objetos string, cada uno de los cuales tiene un puntero a un bloque de memoria en el heap (si el string es largo). Es una estructura de dos niveles de indirección.
:::

---

### Ejercicio 26: Localidad en algoritmos de multiplicación de matrices ralas
**Consigna:** ¿Por qué el formato CSR (Compressed Sparse Row) es preferible para la caché?

:::{solution} ex-p6-26
:class: dropdown
**Análisis:** Almacena los elementos no nulos de forma contigua. Al recorrer una fila, todos los accesos son secuenciales, permitiendo que el prefetcher de hardware trabaje a máxima capacidad.
:::

---

### Ejercicio 27: El impacto de Spectre v1
**Consigna:** ¿Cómo explota Spectre v1 la jerarquía de caché?

:::{solution} ex-p6-27
:class: dropdown
**Análisis:** Fuerza al procesador a ejecutar especulativamente una lectura de memoria fuera de límites. El dato se carga en la caché, y luego se mide el tiempo de acceso para "adivinar" el valor del dato secreto.
:::

---

### Ejercicio 28: Localidad en el diseño de un motor de búsqueda
**Consigna:** ¿Cómo se optimizan las listas de invertidos (Inverted Index) para la caché?

:::{solution} ex-p6-28
:class: dropdown
**Análisis:** Se guardan como deltas comprimidos en bloques de tamaño fijo que coinciden con el tamaño de la página del SO o de la línea de caché, maximizando el throughput de lectura secuencial.
:::

---

### Ejercicio 29: El costo de los "BigIntegers"
**Consigna:** ¿Por qué operar con `BigInteger` en Java es mucho más lento que con `long`?

:::{solution} ex-p6-29
:class: dropdown
**Análisis:** `BigInteger` es un objeto que contiene un arreglo de `int`. Cada operación requiere múltiples niveles de indirección y la creación de nuevos objetos temporales, lo que ensucia la caché.
:::

---

### Ejercicio 30: Localidad en el diseño de un despachador de eventos (Event Loop)
**Consigna:** ¿Por qué un Event Loop suele tener mejor performance de caché que un sistema multi-hilo con locks?

:::{solution} ex-p6-30
:class: dropdown
**Análisis:** Al correr en un solo hilo, todos los datos calientes se mantienen en la L1/L2 de un solo núcleo. No hay invalidaciones de caché por coherencia entre núcleos ni context switches caros.
:::

---

### Ejercicio 31: Análisis de latencia en redes NUMA
**Consigna:** ¿Qué es la "Afinidad de Memoria" y por qué es vital en servidores con 256 núcleos?

:::{solution} ex-p6-31
:class: dropdown
**Análisis:** Asegura que un hilo acceda a memoria física conectada directamente al socket de su CPU. Acceder a memoria de otro socket duplica la latencia y satura los buses de interconexión.
:::

---

### Ejercicio 32: Localidad en estructuras de datos funcionales
**Consigna:** ¿Por qué las listas inmutables de Scala tienen peor localidad que los `ArrayBuffer`?

:::{solution} ex-p6-32
:class: dropdown
**Análisis:** Cada elemento es un objeto nodo disperso en el heap. El `ArrayBuffer` usa un arreglo contiguo debajo, permitiendo acceso secuencial y prefetching.
:::

---

### Ejercicio 33: El impacto de las Huge Pages en bases de datos
**Consigna:** ¿Por qué MySQL rinde mejor con Huge Pages de 2MB habilitadas?

:::{solution} ex-p6-33
:class: dropdown
**Análisis:** Reduce la cantidad de entradas necesarias en el TLB para cubrir el buffer pool de la base de datos, eliminando la mayoría de los fallos de traducción de direcciones.
:::

---

### Ejercicio 34: Localidad en algoritmos de detección de colisiones 3D
**Consigna:** ¿Cómo ayuda un Quadtree a la localidad en un juego 2D?

:::{solution} ex-p6-34
:class: dropdown
**Análisis:** Agrupa objetos cercanos físicamente en el mundo en nodos cercanos en la estructura de datos. Al recorrer un nodo, procesamos objetos que probablemente ya estén en la caché.
:::

---

### Ejercicio 35: El Futuro y CXL (Compute Express Link)
**Consigna:** ¿Qué es CXL y cómo cambiará nuestra noción de "Localidad de Memoria" en la próxima década?

:::{solution} ex-p6-35
:class: dropdown
**Análisis:** Permite que la RAM viva fuera del socket, conectada por PCIe de alta velocidad. Tendremos "Memoria Cercana" y "Memoria Lejana", forzando a los programadores a ser aún más conscientes de la topografía del sistema.
:::

---

### Ejercicio 36: Prefetching en Estructuras de Datos Ralas
**Consigna:** Estás recorriendo una matriz rala en formato CSR. ¿Por qué el prefetcher de hardware falla al acceder al arreglo `values`?

:::{solution} ex-p6-36
:class: dropdown
**Análisis:** El índice de `values` depende del contenido de `col_indices`. Esto es una indirección de datos impredecible para el hardware.
:::

---

### Ejercicio 37: Loop Unrolling y Registro de Renombrado
**Consigna:** ¿Cómo ayuda el *Loop Unrolling* a mitigar las dependencias de datos en la CPU?

:::{solution} ex-p6-37
:class: dropdown
**Análisis:** Al exponer más instrucciones independientes, permitís que el hardware de renombrado de registros use múltiples unidades funcionales en paralelo, saturando el pipeline.
:::

---

### Ejercicio 38: El costo de `System.arraycopy`
**Consigna:** ¿Por qué `System.arraycopy` es más rápido que un bucle `for` manual en Java?

:::{solution} ex-p6-38
:class: dropdown
**Análisis:** Es un intrínseco de la JVM que usa instrucciones SIMD (SSE/AVX) para mover 32 o 64 bytes por ciclo y aprovecha los "Non-Temporal Stores" para no ensuciar la caché.
:::

---

### Ejercicio 39: Cache Misses en Recursive Descent Parsers
**Consigna:** ¿Por qué los parsers recursivos suelen tener mala localidad comparados con los basados en tablas?

:::{solution} ex-p6-39
:class: dropdown
**Análisis:** Los saltos constantes entre funciones de reglas gramaticales causan fallos en la L1i (caché de instrucciones). Los basados en tablas usan un lazo pequeño siempre caliente en L1i.
:::

---

### Ejercicio 40: Memoria Virtual y Fragmentación Externa
**Consigna:** ¿Puede un programa tener 1GB de RAM libre y fallar al pedir un arreglo de 512MB?

:::{solution} ex-p6-40
:class: dropdown
**Análisis:** Sí, si no hay un bloque de 512MB de direcciones **virtuales contiguas**. La fragmentación destruye la contigüidad virtual necesaria para los arreglos grandes.
:::

---

### Ejercicio 41: El impacto de los Generics en Java (Type Erasure)
**Consigna:** ¿Cómo afecta el *Type Erasure* a la localidad de memoria?

:::{solution} ex-p6-41
:class: dropdown
**Análisis:** Obliga a usar objetos (Integer en lugar de int), lo que introduce indirección y headers de objeto, destruyendo la densidad de datos en la caché.
:::

---

### Ejercicio 42: Cache partitioning en Procesadores Multinúcleo
**Consigna:** Algunos procesadores permiten "particionar" la caché L3. ¿Para qué sirve?

:::{solution} ex-p6-42
:class: dropdown
**Análisis:** Evita que un proceso "ruidoso" (que usa mucha RAM) expulse los datos de una aplicación crítica de baja latencia que necesita su working set caliente en L3.
:::

---

### Ejercicio 43: Write-Combining Buffers
**Consigna:** Estás escribiendo en el Framebuffer de una placa de video. ¿Por qué es vital que la memoria esté marcada como "Write-Combining"?

:::{solution} ex-p6-43
:class: dropdown
**Análisis:** Acumula pequeñas escrituras en paquetes de 64 bytes para enviarlas juntas por el bus PCIe, minimizando el impacto de la latencia del bus.
:::

---

### Ejercicio 44: Localidad en Algoritmos de Ordenamiento
**Consigna:** ¿Por qué *Quicksort* suele ser más rápido que *Heapsort*?

:::{solution} ex-p6-44
:class: dropdown
**Análisis:** Quicksort tiene localidad espacial perfecta (acceso secuencial). Heapsort salta entre $i$ y $2i$, lo que excede la caché a medida que el heap crece.
:::

---

### Ejercicio 45: El costo de la Reflexión en Java
**Consigna:** ¿Cómo afecta el uso masivo de `Method.invoke()` a la caché de instrucciones?

:::{solution} ex-p6-45
:class: dropdown
**Análisis:** Requiere cargar metadatos y realizar múltiples saltos indirectos impredecibles que llenan la L1i con código de gestión de la JVM en lugar de lógica de negocio.
:::

---

### Ejercicio 46: Estructuras de Datos Compactas vs Legibilidad
**Consigna:** Tenés un arreglo de 10 millones de objetos que representan un color RGB (3 bytes). Compará usar `class Color { byte r, g, b; }` vs `int[] colors`.

:::{solution} ex-p6-46
:class: dropdown
**Análisis:** La versión `int[]` usa 4 bytes por color (40MB total). La versión objeto usa 24 bytes (240MB total). La versión compacta vuela porque entran 16 colores en una sola línea de caché.
:::

---

### Ejercicio 47: El impacto del Garbage Collector en el Throughput de Memoria
**Consigna:** ¿Por qué un recolector de basura tipo "Stop-the-World" puede mejorar la localidad temporal?

:::{solution} ex-p6-47
:class: dropdown
**Análisis:** Mueve todos los objetos vivos a una región contigua de un solo golpe, restaurando la localidad espacial perfecta para objetos que se referencian entre sí.
:::

---

### Ejercicio 48: Localidad en Algoritmos de Búsqueda de Caminos (A*)
**Consigna:** ¿Cómo optimizarías la `Open List` de un algoritmo A* para aprovechar la caché?

:::{solution} ex-p6-48
:class: dropdown
**Análisis:** Usá un **Binary Heap** implementado sobre un arreglo de primitivos en lugar de objetos. La relación matemática de los índices permite al prefetcher anticipar los accesos.
:::

---

### Ejercicio 49: El costo de los "Boxing" en Streams de Java
**Consigna:** Compará `mapToInt` vs `reduce(0, Integer::sum)`.

:::{solution} ex-p6-49
:class: dropdown
**Análisis:** `mapToInt` elimina la creación de objetos temporales. `reduce` crea un nuevo objeto `Integer` por cada suma, inundando la L1 con basura.
:::

---

### Ejercicio 50: Memoria No-Volátil (NVRAM) y Localidad
**Consigna:** ¿Qué precauciones de localidad espacial debés tener al usar memoria persistente?

:::{solution} ex-p6-50
:class: dropdown
**Análisis:** Agrupá las escrituras en bloques del tamaño de la línea de caché para minimizar el número de ciclos de persistencia (fsync) caros.
:::

---

### Ejercicio 51: Análisis de Stalls por Dependencia de Carga
**Consigna:** ¿Qué pasa si una instrucción usa el resultado de un `load` inmediatamente?

:::{solution} ex-p6-51
:class: dropdown
**Análisis:** La CPU sufre un **Stall** de ~4-5 ciclos esperando al dato de la L1. Se soluciona reordenando el código para insertar instrucciones independientes.
:::

---

### Ejercicio 52: Localidad en Redes de Neuronas (Deep Learning)
**Consigna:** ¿Por qué las GPUs son tan eficientes procesando tensores?

:::{solution} ex-p6-52
:class: dropdown
**Análisis:** Procesan bloques contiguos de datos de forma puramente secuencial (SIMT), ignorando la latencia a favor de un ancho de banda masivo (1TB/s).
:::

---

### Ejercicio 53: El costo de los logs en sistemas de alta performance
**Consigna:** ¿Cómo afecta el logging masivo a la localidad de la caché de datos?

:::{solution} ex-p6-53
:class: dropdown
**Análisis:** Llenan la L1 y L2 con strings efímeros, expulsando los datos útiles de tu aplicación. Usá logging asíncrono y formatos binarios.
:::

---

### Ejercicio 54: Memory Padding en Estructuras de C
**Consigna:** ¿Por qué `struct { char a; long b; }` ocupa 16 bytes?

:::{solution} ex-p6-54
:class: dropdown
**Análisis:** La CPU requiere que un `long` empiece en una dirección múltiplo de 8. El compilador inserta 7 bytes de "agujero" para alinear `b`.
:::

---

### Ejercicio 55: Localidad de Referencia en Intérpretes de Lenguajes
**Consigna:** ¿Por qué un intérprete es más lento que el JIT en términos de localidad?

:::{solution} ex-p6-55
:class: dropdown
**Análisis:** El intérprete tiene un `switch` gigante que destruye la localidad de las instrucciones. El JIT genera código máquina nativo y lineal.
:::

---

### Ejercicio 56: Cálculo de Ancho de Banda en DDR5
**Consigna:** Una memoria DDR5-6400 opera en Dual Channel. Si un algoritmo lee 10GB de datos, ¿cuál es el tiempo teórico mínimo?

:::{solution} ex-p6-56
:class: dropdown
**Análisis:** El ancho de banda es de 102.4 GB/s. El tiempo teórico es de 97ms. Los fallos de TLB y accesos no secuenciales suelen duplicar este tiempo en la práctica.
:::

---

### Ejercicio 57: Latencia en Redes NUMA Multi-hop
**Consigna:** ¿Qué pasa si un hilo en el Nodo 0 accede a memoria del Nodo 3?

:::{solution} ex-p6-57
:class: dropdown
**Análisis:** El dato atraviesa múltiples saltos de interconexión. La latencia puede ser 4 veces mayor (400ns vs 100ns). Usá *First-touch policy*.
:::

---

### Ejercicio 58: Impacto de Spectre y Meltdown en la Caché
**Consigna:** ¿Cómo afectaron los parches (KPTI) al rendimiento?

:::{solution} ex-p6-58
:class: dropdown
**Análisis:** Fuerzan a vaciar el TLB en cada syscall. Al volver al usuario, el TLB está "frío", causando caídas de hasta el 30% en apps con mucho I/O.
:::

---

### Ejercicio 59: Memory Tagging (MTE) y Localidad
**Consigna:** ¿Cómo afecta Memory Tagging al tamaño de la línea de caché?

:::{solution} ex-p6-59
:class: dropdown
**Análisis:** Añade bits de metadata a cada bloque de memoria. La caché debe guardar estos tags, lo que puede reducir levemente la densidad de datos útiles.
:::

---

### Ejercicio 60: Localidad en el Garbage Collector ZGC
**Consigna:** ¿Por qué los "Colored Pointers" son mejores para la localidad?

:::{solution} ex-p6-60
:class: dropdown
**Análisis:** La CPU verifica el estado del objeto mirando el registro del puntero, sin tener que cargar el header del objeto de la RAM. Ahorra saltos caros.
:::

---

### Ejercicio 61: Análisis de Conflict Misses en Matrices de Potencia de 2
**Consigna:** ¿Por qué una matriz de $1024 \times 1024$ suele dar más fallos de caché que una de $1024 \times 1025$?

:::{solution} ex-p6-61
:class: dropdown
**Análisis:** En potencias de 2, todas las filas mapean al mismo set de la caché, causando **Thrashing**. Una columna extra desalinea los datos y los reparte por la caché.
:::

---

### Ejercicio 62: False Sharing en el mundo real: `LongAdder`
**Consigna:** ¿Cómo soluciona `LongAdder` el problema de la localidad?

:::{solution} ex-p6-62
:class: dropdown
**Análisis:** Usa un arreglo de celdas anotadas con `@Contended`, lo que inserta padding para asegurar que cada hilo trabaje en su propia línea de caché exclusiva.
:::

---

### Ejercicio 63: Prefetching y el "LinkedList" de la muerte
**Consigna:** ¿Puede el prefetching por software ayudar en una lista enlazada?

:::{solution} ex-p6-63
:class: dropdown
**Análisis:** No podés pre-cargar el siguiente nodo, pero podés pre-cargar los **datos** del siguiente nodo mientras procesás el actual, ocultando parte de la latencia.
:::

---

### Ejercicio 64: Localidad en Serialización Avro vs JSON
**Consigna:** ¿Por qué los motores de Big Data prefieren formatos binarios?

:::{solution} ex-p6-64
:class: dropdown
**Análisis:** Permiten mapeo directo a memoria (Zero-copy). La CPU opera sobre el buffer de red sin crear miles de objetos temporales.
:::

---

### Ejercicio 65: El costo de los saltos indirectos en C++ (vtable)
**Consigna:** ¿Qué impacto tiene un arreglo de punteros a interfaces en la i-cache?

:::{solution} ex-p6-65
:class: dropdown
**Análisis:** El Branch Predictor falla constantemente. La L1i se llena de fragmentos de código de diferentes clases, causando fallos de instrucción masivos.
:::

---

### Ejercicio 66: Análisis de latencia en RAM DDR5 vs DDR4
**Consigna:** ¿Por qué DDR5 mejora la localidad a pesar de tener mayor latencia nominal?

:::{solution} ex-p6-66
:class: dropdown
**Análisis:** Divide el módulo en sub-canales independientes, permitiendo que la CPU pida múltiples líneas de caché en paralelo con menos contención.
:::

---

### Ejercicio 67: Localidad en el algoritmo de PageRank
**Consigna:** ¿Cómo afecta el orden de los nodos a la convergencia?

:::{solution} ex-p6-67
:class: dropdown
**Análisis:** Reordenar los nodos por comunidades asegura que el acceso al arreglo de ranks sea secuencial y amigable con el prefetcher.
:::

---

### Ejercicio 68: El impacto de `Final` en Java
**Consigna:** ¿Ayuda marcar campos como `final` a la localidad?

:::{solution} ex-p6-68
:class: dropdown
**Análisis:** El JIT sube el valor a un registro permanentemente, eliminando lecturas de caché redundantes y bajando la presión sobre la L1.
:::

---

### Ejercicio 69: Software Prefetching en B-Trees
**Consigna:** ¿Cómo optimizarías la búsqueda en un nodo de un B-Tree?

:::{solution} ex-p6-69
:class: dropdown
**Análisis:** Emití instrucciones de prefetch para las líneas de caché de las llaves al entrar al nodo, ocultando la latencia mientras hacés otras tareas.
:::

---

### Ejercicio 70: Localidad en Sistemas de Archivos
**Consigna:** ¿Cómo afecta la localidad de los inodos al rendimiento de un escaneo de archivos?

:::{solution} ex-p6-70
:class: dropdown
**Análisis:** XFS guarda inodos cerca de los datos. Esta localidad en disco se traduce en localidad en la Page Cache del kernel, ahorrando I/O.
:::

---

### Ejercicio 71: El costo de la indirección en `std::function`
**Consigna:** Compará un puntero a función simple vs `std::function`.

:::{solution} ex-p6-71
:class: dropdown
**Análisis:** `std::function` usa Type Erasure y puede asignar memoria en el heap, añadiendo una capa de indirección y un potencial cache miss.
:::

---

### Ejercicio 72: Localidad en algoritmos de Hash Join
**Consigna:** ¿Por qué el Grace Hash Join es mejor que un Join anidado?

:::{solution} ex-p6-72
:class: dropdown
**Análisis:** Divide las tablas en buckets que caben en la RAM, asegurando que las búsquedas en la tabla hash resulten siempre en *Cache Hits*.
:::

---

### Ejercicio 73: El impacto de las Huge Pages en la latencia de Red
**Consigna:** ¿Por qué las tarjetas de red de 100Gbps requieren Huge Pages?

:::{solution} ex-p6-73
:class: dropdown
**Análisis:** Permiten que el DMA escriba grandes bloques sin interrumpir a la CPU para actualizar el TLB, algo vital a esa velocidad.
:::

---

### Ejercicio 74: Localidad en el recolector de basura G1 (Regiones)
**Consigna:** ¿Cómo ayuda el diseño por regiones de G1?

:::{solution} ex-p6-74
:class: dropdown
**Análisis:** Permite limpiar porciones pequeñas del heap que caben en la L3, evitando saturar el bus de memoria durante la recolección.
:::

---

### Ejercicio 75: El costo de los Strings en Java (UTF-16)
**Consigna:** ¿Cómo afecta UTF-16 a la localidad frente a UTF-8?

:::{solution} ex-p6-75
:class: dropdown
**Análisis:** Usa el doble de memoria para texto ASCII. Java 9+ solucionó esto con "Compact Strings" que usan 1 byte si es posible.
:::

---

### Ejercicio 76: Análisis de latencia en registros vectoriales (ZMM)
**Consigna:** ¿Por qué mover datos entre RAX y ZMM tiene latencia?

:::{solution} ex-p6-76
:class: dropdown
**Análisis:** Cruza dominios físicos distintos en la CPU. Es mejor cargar datos directamente de la caché a los registros vectoriales.
:::

---

### Ejercicio 77: Localidad en el diseño de un Scheduler
**Consigna:** ¿Por qué preferir el mismo núcleo para una tarea?

:::{solution} ex-p6-77
:class: dropdown
**Análisis:** Evita el "Cold Start" de la caché. Los datos de la tarea probablemente todavía estén en la L1/L2 de ese núcleo.
:::

---

### Ejercicio 78: El costo de `thread_local`
**Consigna:** ¿Cómo se implementa a nivel de hardware?

:::{solution} ex-p6-78
:class: dropdown
**Análisis:** Usa registros de segmento (FS/GS) y añade una instrucción extra para calcular el offset, lo que puede ralentizar el pipeline.
:::

---

### Ejercicio 79: Localidad en Compresión LZ4
**Consigna:** ¿Por qué LZ4 es tan rápido?

:::{solution} ex-p6-79
:class: dropdown
**Análisis:** Usa una ventana de búsqueda pequeña que siempre cabe en L1/L2, comprimiendo a la velocidad del bus de memoria.
:::

---

### Ejercicio 80: El impacto de Spectre v2 en los saltos indirectos
**Consigna:** ¿Qué es un "Retpoline"?

:::{solution} ex-p6-80
:class: dropdown
**Análisis:** Mitigación que reemplaza saltos indirectos por secuencias de `ret`, engañando al predictor pero destruyendo el rendimiento del pipeline.
:::

---

### Ejercicio 81: Localidad en estructuras de datos persistentes
**Consigna:** En Clojure, ¿cómo afecta la inmutabilidad a la caché?

:::{solution} ex-p6-81
:class: dropdown
**Análisis:** Crea muchos objetos pequeños (Path Copying), pero la inmutabilidad permite compartirlos entre hilos sin invalidar cachés constantemente.
:::

---

### Ejercicio 82: Análisis de throughput en buffers circulares
**Consigna:** ¿Por qué el buffer circular es el rey del logging?

:::{solution} ex-p6-82
:class: dropdown
**Análisis:** Es un arreglo con localidad perfecta que reutiliza memoria, manteniendo los datos calientes en la jerarquía de caché.
:::

---

### Ejercicio 83: El impacto de la latencia de red en sistemas distribuidos
**Consigna:** ¿Cuánta CPU se pierde esperando a la red?

:::{solution} ex-p6-83
:class: dropdown
**Análisis:** No es solo la espera; el context switch ensucia todas las cachés, degradando la performance de todo el código subsiguiente.
:::

---

### Ejercicio 84: Localidad en el algoritmo A* en juegos
**Consigna:** ¿Cómo afecta el tamaño del grid?

:::{solution} ex-p6-84
:class: dropdown
**Análisis:** Un layout basado en tiles asegura que los nodos cercanos físicamente estén cercanos en memoria, optimizando la búsqueda.
:::

---

### Ejercicio 85: El costo de la sincronización en ARM
**Consigna:** ¿Por qué LDREX/STREX es más amigable con la caché?

:::{solution} ex-p6-85
:class: dropdown
**Análisis:** Evita bloquear el bus de memoria, permitiendo que otros núcleos sigan trabajando mientras uno intenta una operación atómica.
:::

---

### Ejercicio 86: Localidad en el diseño de una JVM (Metaspace)
**Consigna:** ¿Cómo se acceden a los métodos?

:::{solution} ex-p6-86
:class: dropdown
**Análisis:** Viven fuera del heap. Demasiadas clases causan fallos de caché en el Metaspace, ralentizando todas las llamadas virtuales.
:::

---

### Ejercicio 87: El impacto de la virtualización en la caché
**Consigna:** ¿Qué es el Cache Coloring en VMs?

:::{solution} ex-p6-87
:class: dropdown
**Análisis:** Técnica para asignar páginas físicas de modo que diferentes VMs no compitan por los mismos sets de la L3 compartida.
:::

---

### Ejercicio 88: Localidad en algoritmos de detección de fraude
**Consigna:** ¿Por qué sufren tanto con la memoria?

:::{solution} ex-p6-88
:class: dropdown
**Análisis:** Las relaciones en grafos de transacciones son aleatorias, forzando un *Pointer Chasing* extremo que satura la latencia de la RAM.
:::

---

### Ejercicio 89: El costo de los "Pointers" en Go
**Consigna:** ¿Cómo ayuda el diseño de punteros de Go al GC?

:::{solution} ex-p6-89
:class: dropdown
**Análisis:** Al prohibir aritmética de punteros, las barreras de escritura son más simples y rápidas, mejorando el rendimiento de la caché.
:::

---

### Ejercicio 90: Localidad en sistemas de archivos Log-Structured (LFS)
**Consigna:** ¿Por qué LFS es asimétrico en performance?

:::{solution} ex-p6-90
:class: dropdown
**Análisis:** Escritura secuencial perfecta, pero lectura aleatoria debido a la fragmentación de archivos en diferentes bloques del log.
:::

---

### Ejercicio 91: El impacto del Inlining en el JIT
**Consigna:** ¿Cómo ayuda el Inlining a los registros?

:::{solution} ex-p6-91
:class: dropdown
**Análisis:** Permite mantener variables en registros en lugar de pasarlas por el stack, eliminando accesos a memoria caros.
:::

---

### Ejercicio 92: Localidad en GCs de tiempo real
**Consigna:** ¿Por qué evitan mover objetos?

:::{solution} ex-p6-92
:class: dropdown
**Análisis:** Mover objetos causa picos de latencia por invalidación de cachés. Prefieren fragmentación controlada a cambio de determinismo.
:::

---

### Ejercicio 93: El costo de la indirección de red (Service Mesh)
**Consigna:** ¿Cómo afecta un Sidecar a la latencia?

:::{solution} ex-p6-93
:class: dropdown
**Análisis:** El paquete atraviesa el stack de red del host dos veces extra, ensuciando la caché con datos de red y consumiendo ciclos.
:::

---

### Ejercicio 94: Localidad en algoritmos de Deep Learning
**Consigna:** ¿Qué es im2col?

:::{solution} ex-p6-94
:class: dropdown
**Análisis:** Reorganiza los datos de convolución en una multiplicación de matrices contiguas, maximizando el aprovechamiento de la caché y SIMD.
:::

---

### Ejercicio 95: El impacto de los parches de Microcódigo
**Consigna:** ¿Cómo ralentizan la caché?

:::{solution} ex-p6-95
:class: dropdown
**Análisis:** Forzando la serialización de operaciones de memoria, eliminando el beneficio de la ejecución fuera de orden (OoO).
:::

---

### Ejercicio 96: Localidad en motores de búsqueda
**Consigna:** ¿Cómo se guardan las Posting Lists?

:::{solution} ex-p6-96
:class: dropdown
**Análisis:** Como deltas comprimidos en arreglos contiguos, permitiendo recorridos secuenciales ultra-rápidos en la caché.
:::

---

### Ejercicio 97: El costo de los BigIntegers en criptografía
**Consigna:** ¿Por qué usar arreglos de long?

:::{solution} ex-p6-97
:class: dropdown
**Análisis:** Evita ataques de tiempo al asegurar que el acceso a memoria no dependa de los datos secretos, además de ganar performance.
:::

---

### Ejercicio 98: Localidad en el Event Loop de Node.js
**Consigna:** ¿Por qué es rápido siendo single-threaded?

:::{solution} ex-p6-98
:class: dropdown
**Análisis:** La L1 siempre tiene los datos del loop caliente y no hay invalidaciones de caché por coherencia entre núcleos.
:::

---

### Ejercicio 99: El impacto de los Generics en Rust
**Consigna:** ¿Por qué Rust es más rápido que Java en genéricos?

:::{solution} ex-p6-99
:class: dropdown
**Análisis:** La monomorfización crea código especializado con datos contiguos (unboxed), logrando una localidad que Java no puede igualar.
:::

---

### Ejercicio 100: Computación en Memoria (COMA)
**Consigna:** ¿Qué es Cache-Only Memory Architecture?

:::{solution} ex-p6-100
:class: dropdown
**Análisis:** Sistema donde toda la memoria se comporta como una caché gigante, migrando los datos físicamente hacia el núcleo que los necesita.
:::

---

## 14. Localidad en Sistemas de Bases de Datos (Detallado)

En el mundo de los datos masivos, la localidad no es una sugerencia; es la diferencia entre una consulta que tarda milisegundos y una que tarda horas. Las bases de datos modernas están diseñadas "desde el silicio hacia arriba" para minimizar el movimiento de datos.

### 14.1. Almacenamiento Row-oriented vs Column-oriented
La decisión más fundamental en el diseño de un motor de BD es cómo se disponen los bytes en el disco (y por ende, en la RAM).

-   **Row-oriented (PostgreSQL, MySQL, Oracle):** Los datos de una fila se guardan contiguos: `[ID1, Nombre1, Fecha1, ID2, Nombre2, Fecha2...]`.
    -   **Localidad Espacial:** Excelente para operaciones OLTP (Online Transactional Processing) donde querés leer o escribir un registro completo.
    -   **Problema de Caché:** Si hacés un `SELECT SUM(salario) FROM empleados`, la CPU tiene que cargar toda la fila (incluyendo nombres, direcciones y fotos) solo para extraer 8 bytes de salario. Estás desperdiciando el 95% de tu ancho de banda de caché.
-   **Column-oriented (ClickHouse, Snowflake, DuckDB):** Cada columna vive en su propio archivo o bloque: `[Salario1, Salario2...], [Nombre1, Nombre2...]`.
    -   **Localidad Espacial:** Perfecta para OLAP (Analítica). Al sumar salarios, la CPU carga líneas de caché llenas exclusivamente de números.
    -   **Compresión:** Como los datos de una columna son del mismo tipo, se comprimen muchísimo mejor (ej. usando *Delta Encoding*), lo que aumenta la densidad de datos efectiva en la L1.

### 14.2. Impacto de la Localidad en el Diseño de Índices
Los índices son las estructuras que evitan el "Full Table Scan".

-   **B-Trees:** Diseñados para que cada nodo ocupe exactamente una página de disco (típicamente 4KB o 16KB). Al cargar un nodo, maximizás la localidad espacial del SO. Sin embargo, en RAM, un B-Tree muy grande sufre fallos de caché en cada nivel de la jerarquía.
-   **LSM-Trees (Log-Structured Merge-Trees):** Usados en RocksDB o Cassandra. En lugar de modificar datos en el lugar (que causa escrituras aleatorias lentas), todas las escrituras son secuenciales en un log.
    -   **Write-friendliness:** Aprovecha el ancho de banda secuencial de los SSD y la RAM. 
    -   **Read-locality:** Para leer, usa "Bloom Filters" (que caben en L1) para evitar buscar en archivos donde el dato no está, ahorrando viajes caros a la memoria principal.

### 14.3. El Costo de la Serialización y la Caché
Cuando movés datos entre un servicio y una base de datos, el formato importa.
-   **JSON/XML:** Son texto. Requieren parsing (CPU-intensive) y crean miles de pequeños objetos `String` e `Integer` en el heap de Java, destruyendo la localidad espacial.
-   **Protobuf / FlatBuffers:** FlatBuffers es el rey de la localidad porque permite acceder a los datos **sin deserializar**. El layout en el buffer coincide con el layout en memoria. Podés leer un campo saltando directamente al offset correcto, manteniendo el buffer "caliente" en la caché.

---

## 15. Localidad en Gráficos 3D y Motores de Juego

En un juego a 144 FPS, tenés menos de 7 milisegundos para procesar toda la lógica, física y renderizado. Si no respetás la caché, no llegás ni a los 30 FPS.

### 15.1. Estructuras para Colisiones: Quadtrees y Octrees
Para saber si dos objetos chocan, no podés comparar todos contra todos ($O(N^2)$). Usamos particionamiento espacial.
-   **El Error:** Implementar un Octree como un árbol de punteros. Cada nodo es un objeto `new OctreeNode()`. Recorrerlo para una detección de colisiones es una pesadilla de *Pointer Chasing*.
-   **La Optimización:** Usar un **Linear Octree**. Se guardan todos los nodos en un solo arreglo contiguo. El ID del nodo indica su posición espacial (usando *Morton Codes* o *Z-order curves*). Al recorrer el espacio, te movés secuencialmente por el arreglo, permitiendo que el prefetcher de la CPU trabaje a máxima velocidad.

### 15.2. Vertex y Index Buffers: El Diálogo con la GPU
La GPU es una bestia de throughput pero odia la latencia.
-   **VBO (Vertex Buffer Objects):** Los datos de los vértices (posición, normal, UV) se mandan en bloques contiguos. 
-   **Interleaving:** En lugar de mandar `[pos1, pos2...], [uv1, uv2...]`, mandamos `[pos1, uv1, pos2, uv2...]`. Esto garantiza que cuando la GPU procesa un vértice, todos sus atributos entren en una sola lectura de memoria.

### 15.3. Texturas y Mipmapping: Localidad 2D y 3D
Cuando renderizás un objeto lejano, una textura de $4000 \times 4000$ es contraproducente.
-   **Mipmapping:** Versiones pre-escaladas de la textura. Al usar la versión pequeña para objetos lejanos, asegurás que los texels vecinos en pantalla estén vecinos en la memoria de video.
-   **Texture Tiling (Z-order):** Las texturas no se guardan fila por fila. Se guardan en pequeños bloques "tiles" (ej. de $8 \times 8$). Esto asegura que si accedés al pixel $(x, y)$, los píxeles $(x, y+1)$ y $(x+1, y)$ estén en la misma línea de caché, algo que no pasaría en un layout lineal.

---

## 16. Resumen y Reglas de Oro

Para cerrar, recordá estas máximas que deben guiar tu diseño:

1.  **Los arreglos de primitivos son tus mejores amigos.** Evitá las colecciones de objetos si la performance es crítica.
2.  **Minimizá los saltos de puntero.** Cada `.` en tu código es una oportunidad para un Cache Miss.
3.  **Mantené tus estructuras pequeñas.** Si entra en L1, vuela. Si sale a RAM, se arrastra.
4.  **Respetá el orden de los datos.** Recorré la memoria como está guardada (Row-Major en Java).
5.  **Medí, no adivines.** Usá JMH y Perf para validar tus suposiciones sobre el hardware.

---

## 17. Glosario Técnico de Jerarquía de Memoria

Para dominar este capítulo, tenés que hablar el idioma del silicio. Acá tenés los términos clave:

- **ALU (Arithmetic Logic Unit):** El corazón de la CPU donde se realizan los cálculos aritméticos y lógicos.
- **Associativity (Vías):** Define cuántas líneas de caché pueden mapearse a un mismo set. Una caché de 8 vías permite que 8 direcciones distintas compitan por el mismo set antes de que ocurra una expulsión.
- **Bandwidth (Ancho de Banda):** La tasa máxima de transferencia de datos entre componentes. Se mide típicamente en GB/s.
- **Cache Hit:** Situación ideal donde el dato solicitado ya reside en la caché del nivel actual.
- **Cache Miss:** Situación donde el dato no está en la caché y debe buscarse en un nivel inferior (más lento).
- **Cold Start:** El periodo inicial de ejecución donde la caché está vacía y cada acceso resulta en un fallo.
- **CPI (Cycles Per Instruction):** Métrica de eficiencia que indica cuántos ciclos de CPU toma ejecutar una instrucción en promedio.
- **Dirty Line:** Una línea de caché que ha sido modificada por la CPU pero cuyos cambios aún no han sido propagados a la memoria principal (DRAM).
- **ILP (Instruction Level Parallelism):** La capacidad del procesador de ejecutar múltiples instrucciones simultáneamente mediante unidades funcionales paralelas.
- **IMC (Integrated Memory Controller):** Controlador de memoria integrado directamente en el silicio de la CPU para minimizar la latencia de acceso a la RAM.
- **Latency (Latencia):** El tiempo de espera desde que se solicita un dato hasta que este llega a su destino.
- **MESI Protocol:** Protocolo estándar de coherencia de caché (Modified, Exclusive, Shared, Invalid) que garantiza que múltiples núcleos vean una vista consistente de la memoria.
- **MMU (Memory Management Unit):** Unidad de hardware responsable de la traducción de direcciones virtuales (software) a direcciones físicas (hardware).
- **Pipeline:** Estructura de ejecución segmentada que permite que diferentes etapas de múltiples instrucciones se ejecuten al mismo tiempo.
- **Spatial Locality:** Principio que establece que si se accede a una dirección de memoria, es muy probable que se aceda a direcciones cercanas en un futuro próximo.
- **Temporal Locality:** Principio que establece que si se accede a un dato, es muy probable que se vuelva a acceder a ese mismo dato pronto.
- **Throughput:** La cantidad total de operaciones o datos procesados en un intervalo de tiempo determinado.
- **TLB (Translation Lookaside Buffer):** Una caché especializada que almacena las traducciones recientes de tablas de páginas del SO.
- **Working Set:** La cantidad de memoria activa que un proceso necesita tener en caché para evitar fallos constantes y degradación de performance.

## Conclusión Final

Dominar la **localidad de memoria** es el paso final para convertirte en un arquitecto de software de alto nivel. Ya no ves el código como una serie de abstracciones lógicas, sino como un flujo físico de electrones moviéndose entre capas de silicio. 

Recordá siempre:
- **La RAM es el nuevo disco.**
- **La Caché es la nueva RAM.**
- **La Indirección es el enemigo.**

Escribí código que fluya con el hardware, no contra él. Tu éxito como programador en sistemas de gran escala depende de tu capacidad para tener **Simpatía Mecánica** con las máquinas que ejecutan tus sueños.

---

## 18. Apéndice: Latencias de referencia (Números que todo programador debe saber)

Jeff Dean (Google) popularizó estos números. Si no los tenés en la cabeza, no podés diseñar sistemas escalables.

| Evento | Tiempo (ns) | Proporción Visual |
| :--- | :--- | :--- |
| **Ciclo de CPU L1** | 0.5 | 1 segundo |
| **Fallo L1 (L2)** | 7 | 14 segundos |
| **Fallo L2 (L3)** | 25 | 50 segundos |
| **Acceso a RAM (DRAM)** | 100 | 3.3 minutos |
| **Fallo de TLB** | 10-100 | Varía |
| **Context Switch (SO)** | 5.000 | 2.7 horas |
| **Lectura SSD NVMe** | 10.000 | 5.5 horas |
| **Round trip en el mismo Datacenter** | 500.000 | 11.5 días |
| **Lectura de Disco HDD** | 10.000.000 | 7.6 meses |
| **Reinicio de Servidor** | 300.000.000.000 | 19.000 años |

**Reflexión:** Cuando tu programa sale a la RAM porque no cuidaste la localidad, es como si tuvieras que esperar 3 minutos para que alguien te pase la sal en la mesa. Si salís al disco, es como si el mozo se fuera de vacaciones y tuvieras que esperar meses a que vuelva con el pedido.

## Próximo paso

Ahora que sabés cómo fluyen los datos por el silicio, es hora de aprender a capturar y analizar estas métricas en entornos de producción usando [Profiling](profiling.md).
