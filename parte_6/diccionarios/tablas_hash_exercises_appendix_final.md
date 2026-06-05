# Tratado Completo de Estructuras de Datos Hash: Un Enfoque de Ingeniería de Sistemas

## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

Este apéndice constituye la guía definitiva, exhaustiva y pormenorizada para entender las tablas hash desde una perspectiva de ingeniería de sistemas de alto rendimiento. En la cátedra de Programación II, no nos conformamos con la teoría abstracta de "papel y lápiz" que se ve en los libros de algoritmos clásicos. Acá nos metemos de lleno en los fierros, en la física real de la computación moderna.

Vas a ver cómo cada línea de Java que escribís se traduce en una coreografía compleja de movimientos de datos en los buses de la placa madre, en cambios de estado eléctricos en los transistores de la caché de la CPU y en latencias que pueden hacer que tu sistema vuele como un cohete o se arrastre como una babosa en el asfalto. Fijate vos que en el desarrollo moderno de software a escala (pienso en sistemas como los de Google, Netflix o Mercado Libre), el cuello de botella casi nunca es la capacidad de cómputo bruta de la CPU. Hoy en día las CPUs son astronómicas. El verdadero drama es la jerarquía de memoria y el ancho de banda hacia la RAM.

Una tabla hash mal diseñada, aunque sea un impecable $O(1)$ en los papeles teóricos, puede estar forzando a la CPU a pasar el 95% de su vida útil "haciendo nada", simplemente esperando a que los electrones viajen desde la memoria principal hasta los registros. Acá vamos a aprender a evitar esa tragedia técnica y a programar con "simpatía mecánica".

---

### Ejercicio 1: Sondeo Lineal, Localidad de Referencia y la Física de la Caché
**Consigna:** Implementá una clase integral `LinearProbingHighPerformanceMap` que utilice arreglos de tipos primitivos para maximizar la localidad de referencia. Realizá un análisis microscópico de cómo el Prefetcher de la CPU interactúa con esta estructura durante una búsqueda de 10.000 claves distribuidas uniformemente. Evaluá el impacto de la fragmentación de memoria en el rendimiento final.

**Resolución Detallada:**
Para que una tabla sea realmente rápida en la JVM, tenemos que evitar a toda costa el "boxing" de tipos (usar `Integer` en lugar de `int`). Cada objeto en Java tiene un overhead de 12 a 16 bytes de cabecera (object header), y lo peor es que los objetos se dispersan por todo el "heap" (montículo de memoria). Esto destruye la caché L1. Al usar arreglos de primitivos, nos aseguramos de que los datos estén pegados uno al lado del otro en la memoria física, permitiendo que la CPU lea bloques enteros de una sola vez.

```java
/**
 * LinearProbingHighPerformanceMap: Una implementación de tabla hash
 * diseñada para maximizar el rendimiento mediante el uso de tipos primitivos
 * y la reducción drástica de fallos de caché (Cache Misses).
 * 
 * Esta clase evita el uso de objetos Entry para mantener la memoria
 * lo más contigua posible, aprovechando el prefetcher de hardware.
 *
 * @author Cátedra Programación II - UNRN
 */
public class LinearProbingHighPerformanceMap {
    // Usamos arreglos paralelos de tipos primitivos.
    // Esto garantiza que la memoria para las claves esté contigua.
    // La CPU ama los arreglos de tipos long porque se alinean bien.
    private long[] keys;
    private long[] values;
    
    // El arreglo metadata nos dice el estado de cada celda.
    // 0: Celda nunca usada (VACÍO)
    // 1: Celda con datos válidos (OCUPADO)
    // 2: Celda donde hubo un dato que se borró (LÁPIDA/TOMBSTONE)
    private byte[] metadata;

    private int size;
    private int capacity;
    private int threshold;
    private final float loadFactor;

    /**
     * Constructor con capacidad inicial.
     * @param initialCapacity Debe ser potencia de dos (ej: 1024, 2048)
     *                        para optimizar el cálculo del índice.
     * @param loadFactor Factor de carga máximo antes de duplicar tamaño.
     */
    public LinearProbingHighPerformanceMap(int initialCapacity, float loadFactor) {
        // Validamos que la capacidad sea potencia de dos
        if ((initialCapacity & (initialCapacity - 1)) != 0) {
            throw new IllegalArgumentException("La capacidad debe ser potencia de 2");
        }
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.threshold = (int) (capacity * loadFactor);
        this.keys = new long[capacity];
        this.values = new long[capacity];
        this.metadata = new byte[capacity];
        this.size = 0;
    }

    /**
     * Función hash de mezcla rápida (Mixing function).
     * No usamos simplemente el modulo, sino que mezclamos los bits
     * para que claves parecidas terminen en lugares muy distintos.
     */
    private int spreadHash(long key) {
        // Algoritmo de mezcla de bits estilo MurmurHash3
        key ^= (key >>> 33);
        key *= 0xff51afd7ed558ccdL;
        key ^= (key >>> 33);
        key *= 0xc4ceb9fe1a85ec53L;
        key ^= (key >>> 33);
        // Operación AND bitwise para el módulo. Es mucho más rápida
        // que el operador '%', que requiere una división costosa.
        return (int) (key & (capacity - 1));
    }

    /**
     * Inserta un par clave-valor en la tabla.
     * Si la clave ya existe, se actualiza el valor.
     */
    public void put(long key, long value) {
        // Verificamos si necesitamos agrandar la tabla antes de insertar
        if (size >= threshold) {
            rehash();
        }

        int index = spreadHash(key);
        
        // Iniciamos el sondeo lineal.
        // Recorremos la memoria de forma secuencial, aprovechando
        // que los datos están contiguos en el arreglo 'metadata' y 'keys'.
        while (metadata[index] == 1) {
            if (keys[index] == key) {
                values[index] = value;
                return;
            }
            // Avanzamos circularmente usando la máscara de capacidad.
            index = (index + 1) & (capacity - 1);
        }

        // Encontramos un lugar vacío o una lápida
        keys[index] = key;
        values[index] = value;
        metadata[index] = 1; // Marcamos como ocupado
        size++;
    }

    /**
     * Busca el valor asociado a una clave.
     * @return El valor o -1 si la clave no está presente.
     */
    public long get(long key) {
        int index = spreadHash(key);
        int initialIndex = index;

        // El prefetcher de la CPU ya trajo metadata[index...index+63] a L1.
        while (metadata[index] != 0) {
            if (metadata[index] == 1 && keys[index] == key) {
                return values[index];
            }
            index = (index + 1) & (capacity - 1);
            
            // Seguridad para evitar bucles infinitos en tablas llenas (aunque no debería pasar)
            if (index == initialIndex) break;
        }
        return -1;
    }

    /**
     * Proceso de redimensionamiento (Rehash).
     * Se dispara cuando superamos el factor de carga.
     */
    private void rehash() {
        long[] oldKeys = keys;
        long[] oldValues = values;
        byte[] oldMeta = metadata;
        int oldCap = capacity;

        // Duplicamos la capacidad usando desplazamiento de bits (shift left)
        capacity <<= 1; 
        threshold = (int) (capacity * loadFactor);
        
        // Reservamos nuevos arreglos
        keys = new long[capacity];
        values = new long[capacity];
        metadata = new byte[capacity];
        size = 0;

        // Reinsertamos los elementos viejos en la nueva tabla
        for (int i = 0; i < oldCap; i++) {
            if (oldMeta[i] == 1) {
                put(oldKeys[i], oldValues[i]);
            }
        }
    }
}
```

**Análisis Detallado de Hardware (Caché y Prefetching):**
Cuando la CPU ejecuta el bucle `while (metadata[index] == 1)`, el Prefetcher de hardware (un circuito especializado en el silicio) observa que estamos accediendo a direcciones de memoria contiguas (ej: `metadata[100]`, `metadata[101]`, `metadata[102]`). El Prefetcher deduce que pronto necesitaremos `metadata[103]` y las siguientes posiciones, por lo que pide a la RAM que envíe esos datos antes de que el software los pida formalmente.

**Traza Microscópica de Hardware (Paso a Paso):**
| Ciclo de Reloj | Operación de CPU | Dirección | Estado de Caché L1 | Acción del Prefetcher | Latencia Percibida |
|----------------|------------------|-----------|--------------------|------------------------|--------------------|
| 0              | Calcular spreadHash| Registros | -                  | -                      | 3 ciclos           |
| 3              | Pedir metadata[i]| 0x1000    | **MISS** (L1)      | Pide bloque 0x1000-3F  | 200 ciclos (Espera)|
| 203            | Compara meta == 1| AL        | **HIT**            | -                      | 1 ciclo            |
| 204            | Pedir keys[i]    | 0x5000    | **MISS** (L1)      | Pide bloque 0x5000-3F  | 200 ciclos (Espera)|
| 404            | Compara keys == k| RAX       | **HIT**            | -                      | 1 ciclo            |
| 405            | Incrementa i     | RBX       | -                  | -                      | 1 ciclo            |
| 406            | Pedir metadata[i+1]| 0x1001  | **HIT**            | Trajo bloque de 64B    | 1 ciclo            |
| 407            | Pedir keys[i+1]  | 0x5008    | **HIT**            | Trajo bloque de 64B    | 1 ciclo            |
| 408            | Pedir metadata[i+2]| 0x1002  | **HIT**            | -                      | 1 ciclo            |
| 409            | Pedir keys[i+2]  | 0x5010    | **HIT**            | -                      | 1 ciclo            |
| 410            | Pedir metadata[i+3]| 0x1003  | **HIT**            | -                      | 1 ciclo            |
| 411            | Pedir keys[i+3]  | 0x5018    | **HIT**            | -                      | 1 ciclo            |

**Impacto de la Simpatía Mecánica:**
Fijate qué locura: los primeros dos accesos tardaron 400 ciclos de CPU (una eternidad en tiempo de procesador). Pero los siguientes accesos tardan **1 ciclo cada uno**. Aunque el sondeo lineal tenga que revisar 10 elementos por una colisión, el tiempo total apenas se incrementa. Esto es lo que llamamos "Aprovechar el Hardware". Si usáramos una lista enlazada (chaining), cada salto sería un MISS de 200 ciclos. ¡La diferencia es de 200 veces!

**Preguntas de Autoevaluación:**
1. ¿Por qué usamos `metadata` en lugar de comparar `keys[i] == 0`? (Pista: Pensá en si el 0 es una clave válida).
2. ¿Qué pasaría si la tabla no fuera potencia de dos? ¿Cómo afectaría a la operación `& (capacity - 1)`?
3. ¿Cómo influye el tamaño de la línea de caché (típicamente 64 bytes) en la decisión de usar arreglos paralelos?

---

### Ejercicio 2: Sondeo Cuadrático y la Dispersión del Agrupamiento
**Consigna:** Implementá una versión de sondeo cuadrático y compará la eficiencia del ancho de banda de memoria contra el sondeo lineal. Analizá por qué el agrupamiento secundario es menos dañino que el primario, pero más costoso en términos de "Cache Thrashing". Presentá una traza de hardware comparativa.

**Resolución Detallada:**
El sondeo cuadrático busca solucionar el agrupamiento primario (donde se forman bloques grandes de celdas ocupadas que atraen más colisiones) haciendo que los saltos sean cada vez más largos: $1, 4, 9, 16, \dots$. Si bien esto distribuye mejor las claves en la tabla, tiene un costo oculto en la eficiencia del hardware moderno.

```java
public class QuadraticProbingTable {
    private long[] table;
    private int capacity;

    public QuadraticProbingTable(int cap) {
        // Para sondeo cuadrático, lo mejor es que la capacidad sea un número primo.
        this.capacity = cap;
        this.table = new long[cap];
    }

    public int search(long key) {
        int h = (int)(Math.abs(key) % capacity);
        int i = 0;
        int pos = h;

        // Bucle de búsqueda con saltos cuadráticos
        while (table[pos] != 0) {
            if (table[pos] == key) {
                return pos;
            }
            i++;
            // Salto cuadrático: pos = (h + i^2) % capacity
            // Esta fórmula asegura que no nos quedemos "pegados" en un cluster.
            pos = (h + i * i) % capacity;
            
            // ANÁLISIS TÉCNICO:
            // Fijate que a partir del tercer intento (i=3), el salto es de 9 posiciones.
            // 9 posiciones de 'long' son 72 bytes. 
            // Como una línea de caché típica es de 64 bytes, es GARANTIZADO
            // que cada salto cuadrático después del segundo va a causar un CACHE MISS.
        }
        return -1;
    }
}
```

**Análisis de Ancho de Banda de Memoria (Bandwidth):**
La memoria RAM no es como una canilla que te da gotas de agua (bytes). Es más bien como un camión que te entrega cajas de 64 bytes (Cache Lines). 
- En el **Sondeo Lineal**, vos traés una caja (64 bytes), usás el primer dato, y si hay colisión, el segundo dato ya está ahí. Usás casi toda la caja. Eficiencia del 100%.
- En el **Sondeo Cuadrático**, traés una caja de 64 bytes para leer la posición `h`. El siguiente salto te manda a `h+9`, que está en OTRA caja distinta. Tiraste a la basura los otros 56 bytes que la RAM se esforzó en enviarte. Esto se llama **Waste of Bandwidth**.

**Traza Comparativa de Hardware (Lineal vs Cuadrático):**
| Intento | Distancia Lineal | Caché Lineal | Distancia Cuadrática | Caché Cuadrática |
|---------|------------------|--------------|----------------------|------------------|
| 1       | 0                | MISS (Carga) | 0                    | MISS (Carga)     |
| 2       | 1                | **HIT**      | 1                    | **HIT** (Misma línea)|
| 3       | 2                | **HIT**      | 4                    | **HIT** (Misma línea)|
| 4       | 3                | **HIT**      | 9                    | **MISS** (Nueva línea)|
| 5       | 4                | **HIT**      | 16                   | **MISS** (Nueva línea)|
| 6       | 5                | **HIT**      | 25                   | **MISS** (Nueva línea)|

**Contexto rioplatense:**
Escuchame una cosa, che: el sondeo cuadrático es como si quisieras estacionar el auto y, para evitar que te toquen el paragolpes, decidís que si un lugar está ocupado, buscás a 1 cuadra, después a 4 cuadras, después a 9. Al final terminás estacionando en otra ciudad. Está bien, no te chocás con nadie, pero caminás como un loco. En la computación, "caminar" es el tiempo que la CPU se queda esperando que los datos lleguen de la RAM. El sondeo lineal es mucho más "amigo" de los fierros.

---

### Ejercicio 3: Chaining con Treeification (El Legado de Java 8)
**Consigna:** Analizá el proceso de transformación (treeification) de una lista enlazada a un Árbol Rojo-Negro dentro de un bucket de un `HashMap`. Implementá un simulador que dispare esta transformación al superar el umbral `TREEIFY_THRESHOLD = 8`. Realizá un análisis de performance comparando la búsqueda en lista vs árbol.

**Resolución Detallada:**
Hasta Java 7, las colisiones se manejaban puramente con listas enlazadas. Si un atacante malintencionado conocía la función hash (que para los `String` es pública), podía enviarte miles de claves distintas que colisionen en el mismo bucket, llevando el tiempo de búsqueda de $O(1)$ a $O(n)$, lo que se conoce como un ataque de denegación de servicio (HashDoS). Desde Java 8, se introdujo el concepto de convertir las listas largas en Árboles Rojo-Negro para asegurar un peor caso de $O(\log n)$.

```java
public class TreeificationLab {
    // Umbral de Java para convertir lista en árbol
    static final int TREEIFY_THRESHOLD = 8;
    // Umbral para volver de árbol a lista (si se borran elementos)
    static final int UNTREEIFY_THRESHOLD = 6;

    static class Node {
        final int hash;
        final Object key;
        Object value;
        Node next; // Para cuando operamos como lista
        
        // Campos adicionales para cuando operamos como Árbol Rojo-Negro
        Node left, right, parent;
        boolean red; // True si es rojo, false si es negro
        
        Node(int h, Object k, Object v, Node n) {
            this.hash = h; this.key = k; this.value = v; this.next = n;
        }
    }

    /**
     * Simula la lógica de inserción de un HashMap moderno.
     */
    public void simulateInsertion(int currentBucketSize) {
        System.out.println("--- Proceso de Inserción en Bucket ---");
        if (currentBucketSize < TREEIFY_THRESHOLD) {
            System.out.println("Estado: LISTA ENLAZADA.");
            System.out.println("Costo de búsqueda: O(" + currentBucketSize + ")");
            System.out.println("Acción: Simplemente agregamos el nodo al final.");
        } else {
            System.out.println("¡ALERTA!: Umbral alcanzado (" + TREEIFY_THRESHOLD + ").");
            System.out.println("Acción: Ejecutando treeifyBin().");
            System.out.println("Nuevo Estado: ÁRBOL ROJO-NEGRO.");
            System.out.println("Costo de búsqueda: O(log " + currentBucketSize + ")");
        }
        System.out.println("--------------------------------------");
    }
}
```

**Análisis de Complejidad en el Peor Caso:**
| Tamaño del Bucket | Costo Lista (Sondas) | Costo Árbol (Sondas) | Diferencia |
|-------------------|----------------------|----------------------|------------|
| 8                 | 8                    | 3                    | 2.6x       |
| 64                | 64                   | 6                    | 10.6x      |
| 1024              | 1024                 | 10                   | 102.4x     |

**Impacto en la CPU y el Pipeline:**
Aunque el árbol tiene una complejidad asintótica menor, cada comparación en un árbol es un "salto condicional" (`if (key < node.key)`). Las CPUs modernas usan una técnica llamada **Pipeline Execution** donde intentan predecir el resultado de los `if`. En una lista enlazada, la predicción es fácil: el bucle casi siempre sigue. En un árbol balanceado, la CPU tiene un 50% de probabilidad de equivocarse en cada nivel. Esto causa lo que llamamos **Branch Misprediction Penalty**, lo que hace que para buckets chiquitos (menos de 8), la lista enlazada sea en realidad más rápida que el árbol a pesar de tener más comparaciones.

**Análisis Técnico del Árbol:**
- **Balanceo:** El árbol rojo-negro se mantiene balanceado mediante rotaciones y cambios de color después de cada inserción. Esto garantiza que la altura nunca supere los $2 \log (n+1)$.
- **Memoria:** Cada nodo de árbol es un 40% más pesado en memoria que un nodo de lista debido a los punteros adicionales (`parent`, `left`, `right`).

---

### Ejercicio 4: Robin Hood Hashing y la Reducción de la Varianza (Latencia Predictiva)
**Consigna:** Implementá Robin Hood Hashing y demostrá, mediante una traza de ejecución detallada, cómo el algoritmo reduce la desviación estándar de las sondas de búsqueda. Explicá por qué esto es crucial para sistemas que deben cumplir con SLAs (Service Level Agreements) de baja latencia.

**Resolución Detallada:**
Robin Hood Hashing es una variante brillante del sondeo lineal que utiliza una política de "redistribución de la frustración". En el sondeo lineal común, el primero que llega se queda con su lugar ideal y los que vienen después "pagan el pato" yéndose cada vez más lejos. Robin Hood dice: "Si yo estoy más lejos de mi casa que vos de la tuya, vos sos el rico y yo el pobre, así que dame tu lugar".

```java
public class RobinHoodVisualizer {
    static class Entry {
        long key;
        int dib; // Distance In Bucket (Qué tan lejos está de su hash ideal)
        
        Entry(long k, int d) { this.key = k; this.dib = d; }
    }

    private Entry[] table = new Entry[16];

    /**
     * Algoritmo de inserción Robin Hood.
     * Mantiene la tabla ordenada de forma que las distancias sean parejas.
     */
    public void put(long key) {
        int h = (int)(key % table.length);
        Entry toInsert = new Entry(key, 0);

        for (int i = h; ; i = (i + 1) % table.length) {
            if (table[i] == null) {
                table[i] = toInsert;
                return;
            }

            // Si el elemento en el bucket es "más rico" (DIB menor) que el que traigo...
            if (table[i].dib < toInsert.dib) {
                // Le robamos el lugar (The rich get displaced)
                Entry temp = table[i];
                table[i] = toInsert;
                toInsert = temp;
                // Ahora seguimos intentando colocar al desplazado.
                System.out.println("Desplazando clave " + toInsert.key + " por clave " + table[i].key);
            }
            // En cada paso que avanzamos, nuestra frustración (DIB) aumenta.
            toInsert.dib++;
        }
    }
}
```

**Análisis Estadístico de Latencia:**
En una tabla con sondeo lineal normal al 90% de factor de carga, podés tener:
- 50% de las claves con 1 sonda (suerte total).
- 5% de las claves con 50 sondas (mala suerte total).
Esto genera una **Varianza Altísima**. Tu sistema es rápido a veces y lentísimo otras.

En Robin Hood:
- 99% de las claves tienen entre 2 y 4 sondas.
La **Varianza es Mínima**. Esto es lo que buscan los ingenieros que hacen sistemas de trading o juegos online: no me importa que el caso promedio sea 0.1ms más lento si me garantizás que NINGUNA búsqueda va a tardar más de 0.5ms.

**Traza de Búsqueda Fallida (La optimización mágica):**
Fijate qué bueno esto: en Robin Hood, podés terminar una búsqueda fallida ANTES de encontrar un `null`.
| Paso | Operación | DIB Actual | DIB en Tabla | Acción |
|------|-----------|------------|--------------|--------|
| 1    | Hash Pos 10| 0          | 2            | Sigo... |
| 2    | Hash Pos 11| 1          | 3            | Sigo... |
| 3    | Hash Pos 12| 2          | **1**        | **¡STOP!** |

¿Por qué paramos en el paso 3? Porque si nuestra clave estuviera en la tabla, por el algoritmo de inserción, su DIB debería ser AL MENOS 2 en esa posición. Como encontramos a alguien con DIB=1, significa que nuestra clave nunca pasó por acá. Ahorramos sondeos en búsquedas que no existen.

---

### Ejercicio 5: Double Hashing y la Independencia de Sondas
**Consigna:** Implementá Double Hashing y demostrá matemáticamente por qué elimina el agrupamiento secundario. Realizá un análisis de costo computacional comparando el cálculo de una segunda función hash contra el costo de un Cache Miss a la RAM.

**Resolución Detallada:**
El Double Hashing es una técnica de direccionamiento abierto donde el "salto" no es constante ni lineal, sino que depende de la propia clave: $pos = (h1(key) + i \cdot h2(key)) \pmod M$. Esto asegura que dos claves que colisionen en $h1$ tengan secuencias de sondeo completamente distintas, eliminando cualquier tipo de agrupamiento.

```java
public class DoubleHashingMap {
    private int capacity = 1024; // Siempre usamos potencias de 2 por velocidad
    private long[] keys = new long[capacity];

    /**
     * Función hash primaria: determina la ubicación inicial.
     */
    private int h1(long key) {
        return (int)(key & (capacity - 1));
    }

    /**
     * Función hash secundaria: determina el tamaño del salto.
     * IMPORTANTE: Debe devolver siempre un número impar para ser coprimo con 1024.
     */
    private int h2(long key) {
        // Mezclamos los bits superiores y forzamos a que sea impar con '| 1'.
        return (int)(((key >>> 16) | 1) & (capacity - 1));
    }

    public void insert(long key) {
        int pos = h1(key);
        int step = h2(key);
        int probes = 1;

        while (keys[pos] != 0) {
            // Avanzamos usando el paso específico de esta clave.
            pos = (pos + step) & (capacity - 1);
            probes++;
        }
        keys[pos] = key;
        System.out.println("Insertada clave " + key + " en " + probes + " sondas.");
    }
}
```

**Análisis de Costo: CPU vs RAM:**
Muchos programadores dicen: "Che, calcular un segundo hash es lento". Vamos a ver los números reales:
| Operación | Ciclos de CPU | Tiempo (3GHz) |
|-----------|----------------|---------------|
| `(key >>> 16) | 1` | 1 ciclo | 0.3 nanosegundos |
| `pos + step` | 1 ciclo | 0.3 nanosegundos |
| `key & (cap - 1)`| 1 ciclo | 0.3 nanosegundos |
| **Cache Miss (RAM)** | **300 ciclos** | **100.0 nanosegundos** |

**Conclusión Técnica:**
Podés calcular 100 funciones hash en el tiempo que la CPU tarda en traer UN SOLO dato de la memoria RAM. Por lo tanto, cualquier técnica que te ahorre UN solo sondeo (un posible Cache Miss) es una victoria rotunda para el rendimiento. El Double Hashing es excelente porque minimiza las colisiones a un nivel casi teórico de "hashing uniforme".

---

### Ejercicio 6: Cuckoo Hashing y la Búsqueda O(1) en el Peor Caso
**Consigna:** Implementá la lógica de Cuckoo Hashing y analizá su comportamiento bajo alta carga. Explicá por qué es la estructura preferida para hardware de red de alta velocidad (Routers/Switches).

**Resolución Detallada:**
El Cuckoo Hashing es como un juego de sillas musicales para tus datos. Cada clave tiene exactamente dos lugares posibles (Tabla A o Tabla B). Si ambos están ocupados, la clave "patea" a la que estaba ahí y se queda con el lugar. La clave desalojada debe irse a su OTRA posición posible, y así siguiendo.

```java
public class CuckooHash {
    private Long[] t1 = new Long[1024];
    private Long[] t2 = new Long[1024];
    private final int MAX_KICKS = 100;

    public boolean put(long key) {
        for (int i = 0; i < MAX_KICKS; i++) {
            // Intento en Tabla 1
            int p1 = hash1(key);
            if (t1[p1] == null) {
                t1[p1] = key; return true;
            }
            
            // Si está ocupado, desalojo al inquilino actual
            long evicted = t1[p1];
            t1[p1] = key;
            key = evicted; // El desalojado ahora busca su nuevo hogar

            // Intento en Tabla 2 para el desalojado
            int p2 = hash2(key);
            if (t2[p2] == null) {
                t2[p2] = key; return true;
            }
            
            evicted = t2[p2];
            t2[p2] = key;
            key = evicted;
        }
        // Si llegamos acá, entramos en un bucle infinito de desalojos.
        // Significa que la tabla está muy llena o tuvimos mala suerte.
        System.out.println("¡Ciclo de desalojos detectado! Redimensionando...");
        rehash();
        return put(key);
    }
}
```

**Análisis de Hardware (Latencia Determinística):**
La gran ventaja del Cuckoo Hashing es la búsqueda. En el peor caso posible, solo tenés que mirar en DOS lugares.
- **Get(K):** Mirás `t1[h1(K)]` y `t2[h2(K)]`. Si no está en ninguno de los dos, NO ESTÁ.
Esto es ideal para hardware donde necesitás latencia fija. Por ejemplo, en un router que procesa 10 millones de paquetes por segundo, no podés permitirte que una búsqueda tarde 50 sondas. Cuckoo te garantiza que siempre tarda máximo 2.

**Desventaja:** La inserción puede ser muy costosa y, si el factor de carga supera el 50%, la probabilidad de entrar en un ciclo de desalojos se dispara.

---

### Ejercicio 7: Hopscotch Hashing: Vecindarios y Localidad Extrema
**Consigna:** Explicá el funcionamiento del vecindario en Hopscotch Hashing y cómo utiliza un bitmap de 32 bits para acelerar búsquedas locales. Analizá por qué esta estructura es superior para bases de datos en memoria (In-Memory DBs).

**Resolución Detallada:**
Hopscotch (Rayuela) combina lo mejor de dos mundos: la búsqueda rápida de Cuckoo y la localidad de referencia del sondeo lineal. Cada bucket tiene un "vecindario" asociado de tamaño fijo $H$ (típicamente 32). El algoritmo garantiza que si una clave hashea a la posición $i$, se encontrará en algún lugar del rango $[i, i+H-1]$.

```java
class HopscotchBucket {
    long key;
    long value;
    /**
     * Bitmap de 32 bits. 
     * Si el bit j está en 1, significa que un elemento que
     * hashea a este índice está guardado en index + j.
     */
    int hopInfo; 
}
```

**Hardware Trace (Búsqueda):**
1. Calculás el hash $h$.
2. Cargás el bucket `table[h]`. Esto causa un **Cache Miss** inevitable.
3. Al cargar el bucket, la CPU también trae al caché L1 el `hopInfo` y las siguientes celdas (porque están en la misma línea de caché de 64 bytes).
4. Usás una instrucción de hardware llamada `CTZ` (Count Trailing Zeros) para encontrar los bits en 1 del bitmap.
5. Accedés a esas posiciones. Como están a pocos bytes de distancia, son **Cache Hits** instantáneos.

**Conclusión:** Encontrás el dato con un solo acceso a la RAM lenta. Es la estructura de datos más amigable con la jerarquía de caché que existe para direccionamiento abierto.

---

### Ejercicio 8: Ataques de Colisión HashDoS y la Defensa con SipHash
**Consigna:** Realizá una simulación de un ataque de denegación de servicio por colisiones de hash. Implementá una defensa basada en una función hash con semilla aleatoria (keyed hash) y analizá el trade-off en performance.

**Resolución Detallada:**
Si usás una función hash predecible como `String.hashCode()`, un atacante puede generar miles de claves como "AaAa", "AaBB", "BBAa", "BBBB" que todas den el mismo resultado. Si tu servidor usa encadenamiento, se convierte en un $O(n)$ que congela la CPU.

**Implementación de la Defensa (SipHash):**
```java
public class SecuredHashMap {
    private final long k0, k1; // Llaves secretas generadas al iniciar el servidor

    public SecuredHashMap() {
        SecureRandom sr = new SecureRandom();
        this.k0 = sr.nextLong();
        this.k1 = sr.nextLong();
    }

    private int secureHash(byte[] data) {
        // SipHash es una función criptográfica rápida diseñada para esto.
        // Sin conocer k0 y k1, es imposible predecir colisiones.
        return (int) SipHash.digest(k0, k1, data);
    }
}
```
**Análisis:** SipHash es unas 3 veces más lenta que un hash simple, pero te protege de que un pibe con una netbook te tire abajo un servidor de 64 núcleos. En ingeniería, a veces la seguridad es más importante que los nanosegundos.

---

### Ejercicio 9: ConcurrentHashMap y la Escalabilidad de Locks
**Consigna:** Analizá cómo `ConcurrentHashMap` logra escalar a 128 núcleos de CPU sin bloquearse. Explicá la diferencia entre el Lock Stripping de Java 7 y el modelo de CAS (Compare-And-Swap) de Java 8+.

**Resolución Detallada:**
En el mundo multi-core, el bloqueo (`synchronized`) es el enemigo de la velocidad. Si un núcleo tiene el lock, los otros 127 están desperdiciando energía.
- **Java 7 (Lock Stripping):** Dividía la tabla en 16 segmentos. Cada segmento tenía su lock. Permitía que 16 hilos escriban a la vez si iban a segmentos distintos.
- **Java 8+ (CAS):** Ya no usa locks para la mayoría de las operaciones. Usa instrucciones de hardware como `CMPXCHG`. El hilo dice: "Si el valor actual es A, poné B". Si alguien más lo cambió antes, el hilo simplemente reintenta. Es **Lock-Free**.

**Hardware y Coherencia de Caché:**
Fijate que el desafío acá es el bus de memoria. Cuando el Núcleo 1 cambia un valor, el hardware debe "invalidar" esa dirección en las cachés de los otros 127 núcleos. Esto se llama **Cache Coherency Traffic**. Si muchos hilos tocan la misma tabla, el bus se satura de mensajes de control y el rendimiento cae aunque no haya locks.

---

### Ejercicio 10: Redimensionamiento Incremental (Latencia Constante)
**Consigna:** Diseñá un algoritmo de rehash que evite las pausas de "Stop the World". Implementá una lógica donde cada operación de usuario (put/get) mueva una pequeña porción de datos a la nueva tabla.

**Resolución Detallada:**
Si tenés una tabla de 10GB, el rehash tradicional tardaría unos 10 segundos. Para una aplicación de chat o un juego, 10 segundos de congelamiento es el fin.
**Estrategia (usada en Redis):**
1. Mantenés dos tablas: `Vieja` y `Nueva`.
2. En cada `put`, movés los elementos de las próximas 10 posiciones de la `Vieja` a la `Nueva`.
3. Las búsquedas miran en ambas tablas.
4. Cuando la `Vieja` queda vacía, la borrás.
**Resultado:** La latencia se distribuye. Cada operación es un 0.1% más lenta, pero ninguna tarda 10 segundos.

---

### Ejercicio 11: Perfect Hashing FKS (Fredman, Komlós, Szemerédi)
**Consigna:** Implementá una tabla de dos niveles para un conjunto de 1000 claves estáticas. Demostrá por qué el espacio total ocupado es lineal $O(n)$ a pesar de usar sub-tablas cuadráticas.

**Resolución Detallada:**
Es una técnica para cuando conocés las claves de antemano (ej: palabras reservadas de un lenguaje).
1. Nivel 1: Tabla de tamaño $N$ con colisiones.
2. Nivel 2: Si el bucket $i$ tiene $k$ elementos, le hacés una tabla de tamaño $k^2$.
3. Con $k^2$, la probabilidad de colisión es menor al 50%. Si hay colisión, elegís otro hash al azar hasta que sea perfecto.
**Matemática:** La suma de los cuadrados de las colisiones en una tabla hash es estadísticamente menor a $2N$. Por eso el espacio sigue siendo $O(n)$.

---

### Ejercicio 12: SIMD y la Búsqueda Vectorizada
**Consigna:** Explicá cómo las instrucciones AVX-512 permiten acelerar el sondeo lineal. Escribí el pseudo-código para una búsqueda que procesa 16 claves de 32 bits simultáneamente.

**Resolución Detallada:**
Las CPUs modernas tienen registros "anchos" de 512 bits.
```java
// Pseudo-código SIMD
Vector16<Int> clavesEnTabla = memoria.cargar512Bits(posicion);
Vector16<Int> miClaveVector = Vector.distribuir(miClave);
Mask16 resultado = Vector.compararIgual(clavesEnTabla, miClaveVector);
if (resultado.algunaVerdadera()) {
    return posicion + resultado.primerIndiceVerdadero();
}
```
Esto permite recorrer la tabla hash a la velocidad de la luz, procesando 16 entradas en el tiempo que antes hacías una sola. Es la base de los motores de bases de datos analíticas modernas.

---

### Ejercicio 13: Manejo de Borrado en Direccionamiento Abierto
**Consigna:** Compará el uso de lápidas (tombstones) contra el "borrado por desplazamiento circular". Analizá cuál es más eficiente para una tabla con una tasa de borrado del 30%.

**Resolución Detallada:**
- **Lápidas:** Son fáciles de implementar pero ensucian la tabla. Con el tiempo, tus búsquedas exitosas son rápidas pero las fallidas son lentas porque tenés que pasar por todas las lápidas.
- **Desplazamiento:** Cuando borrás, buscás elementos posteriores que "quieran" estar más atrás y los movés para tapar el hueco. Es más complejo pero mantiene la tabla limpia.
**Conclusión:** Si la tabla es de larga duración, el desplazamiento es mejor. Si la tabla es efímera, las lápidas alcanzan.

---

### Ejercicio 14: Cache-Line Alignment y False Sharing en Java
**Consigna:** Explicá por qué dos hilos que escriben en buckets distintos pueden ralentizarse mutuamente debido al diseño de la caché L1. Implementá una solución usando la anotación `@Contended`.

**Resolución Detallada:**
La caché trabaja con líneas de 64 bytes. Si el Bucket 1 y el Bucket 2 están pegados en memoria, entran en la misma línea. Cuando el Hilo 1 escribe en el Bucket 1, el hardware de coherencia de caché de la CPU bloquea TODA la línea, incluyendo el Bucket 2. El Hilo 2 tiene que esperar. Eso se llama **False Sharing**.
**Solución:** Agregar bytes de relleno (padding) para que cada bucket esté en su propia línea de caché.

---

### Ejercicio 15: Tablas Hash en Disco (SSD y NVMe)
**Consigna:** Diseñá un layout de tabla hash para un archivo de 100GB mapeado a memoria (`mmap`). Explicá por qué el sondeo lineal es la única opción que no destruye el rendimiento del disco.

**Resolución Detallada:**
Un SSD lee en páginas de 4KB. Si usás encadenamiento (punteros), cada salto te obliga a leer una nueva página del disco (latencia de microsegundos). El sondeo lineal mantiene los accesos dentro de la misma página de 4KB, permitiendo que el sistema operativo use su "Page Cache" de forma eficiente.

---

### Ejercicio 16: Bloom Filters como Guardianes de Memoria
**Consigna:** Implementá un Bloom Filter de 8KB para proteger una tabla hash de 1GB. Analizá cuántos accesos a la RAM lenta evitás en un flujo de trabajo con un 90% de búsquedas negativas.

**Resolución Detallada:**
El Bloom Filter es una estructura probabilística que entra en la caché L1. Antes de ir a la RAM lenta a buscar en la tabla hash, le preguntás al filtro. Si el filtro dice "No", ya sabés que no está. Te ahorrás 100ns de latencia de RAM en el 90% de los casos.

---

### Ejercicio 17: Arquitecturas NUMA y Sharding Local
**Consigna:** En un servidor con 2 sockets de CPU, explicá el costo de acceder a la memoria del socket remoto. Diseñá una estrategia de "NUMA-local hashing".

**Resolución Detallada:**
Cada CPU en un servidor moderno tiene su propia memoria RAM conectada directamente. Si la CPU 0 quiere leer de la RAM de la CPU 1, los datos tienen que viajar por el bus QPI/UPI, lo que agrega 50-100ns de latencia extra.
**Estrategia:** Tener una tabla hash por cada socket. Cada hilo solo toca la tabla de su socket local.

---

### Ejercicio 18: Zero-Copy Deserialization de Tablas Hash
**Consigna:** Diseñá un formato binario para guardar una tabla hash en disco que no requiera ninguna transformación al cargarse (Zero-Copy).

**Resolución Detallada:**
El formato en disco debe ser un "volcado de memoria" exacto del arreglo de primitivos. Al cargar, usás `FileChannel.map()` para obtener un `ByteBuffer` que apunte directamente a los datos físicos. Podés empezar a hacer búsquedas en una tabla de 50GB en microsegundos, sin instanciar ningún objeto.

---

### Ejercicio 20: El Futuro: Tablas Hash en GPU y FPGAs
**Consigna:** Explicá cómo el modelo de memoria masivamente paralelo de una GPU permite resolver colisiones de forma cooperativa entre miles de hilos.

**Resolución Detallada:**
En la GPU, no resolvemos las colisiones una por una. Usamos un algoritmo llamado **Massive Parallel Probing**. Todos los hilos intentan escribir a la vez usando operaciones atómicas. Los que fallan, prueban la siguiente posición en el siguiente ciclo de reloj. Como la GPU tiene un ancho de banda de 1 TB/s (10 veces más que una CPU), puede procesar colisiones por fuerza bruta de forma increíblemente eficiente.

---

### Epílogo: La Filosofía de los Datos
Llegamos al final de este recorrido, che. Si algo te tiene que quedar claro después de estos 20 ejercicios, es que las estructuras de datos no viven en el vacío. Viven en un mundo de silicio, electrones y latencias físicas. Un ingeniero de software que ignora el hardware es como un arquitecto que ignora la gravedad: sus edificios (sistemas) se van a caer cuando sople el viento (cuando llegue la carga de usuarios).

Entender cómo fluyen los datos, cómo se comporta la caché y cómo la CPU predice tus movimientos es lo que te va a permitir construir sistemas que no solo funcionen, sino que escalen al infinito. Espero que este tratado te haya abierto la cabeza y que la próxima vez que escribas un simple `get(key)`, te imagines toda la coreografía que está pasando por debajo. ¡A seguir metiendo código con simpatía mecánica, que ahí es donde está la verdadera magia de nuestra profesión!

---
*Este documento fue generado como material de estudio avanzado para la cátedra de Programación II - Universidad Nacional de Río Negro.*

## Tratado de Ingeniería en Tablas Hash: Un Análisis de Bajo Nivel

Para un ingeniero especializado en sistemas de alto rendimiento, la tabla hash no es una "caja negra" que da $O(1)$. Es una estructura que vive y muere según su interacción con la microarquitectura de la CPU.

### 1. La Batalla de las Cachés: L1, L2 y L3
Cuando consultamos una tabla hash masiva, el procesador realiza un salto de memoria hacia una dirección dictada por la función hash. Si la tabla no entra en la caché L3 (típicamente 16MB-64MB), cada consulta disparará un **DRAM Access** con una latencia de ~100ns. 
- **Solución:** Usar tablas hash orientadas a caché (\`Cache-aware Hashing\`), donde cada bucket tiene el tamaño exacto de una línea de caché (64 bytes). De este modo, al cargar un bucket, cargamos todos sus datos y sus métodos de resolución de colisiones en un solo ciclo de bus.

### 2. El Veneno de la Varianza en el Sondeo Lineal
El sondeo lineal es amigable con la caché, pero sufre de \`Primary Clustering\`. A medida que el factor de carga se acerca al 80%, las cadenas de colisión crecen exponencialmente. El procesador, intentando predecir qué entrada será la correcta, falla constantemente en su **Branch Predictor**, provocando que el pipeline se vacíe. 
- **Mecánica Senior:** Implementamos un sistema de \`Early Exit\` basado en el bit de signo del hash guardado en un arreglo paralelo, permitiendo descartar colisiones sin leer el dato completo.

### 3. Funciones Hash y el Costo de la Seguridad
No todas las funciones hash son iguales.
- **MurmurHash3:** Excelente performance, baja colisión, pero vulnerable a ataques de denegación de servicio (DoS).
- **SipHash:** El estándar de seguridad moderno (usado en Rust, Python y Ruby). Es un poco más lenta pero garantiza que un atacante no pueda generar colisiones masivas a propósito.
- **HighwayHash:** Uso de instrucciones SIMD para calcular hashes a velocidades de 10GB/s.

---
**Ejercicio 21: Optimización de Tablas para GPUs**
**Consigna:** ¿Cómo diseñarías una tabla hash que pueda ser consultada por 10.000 hilos simultáneamente en una NVIDIA RTX 4090?
**Resolución Detallada:**
1. **Representación:** Usaríamos **Cuckoo Hashing** para garantizar lecturas de tiempo constante (máximo 2 accesos).
2. **Atomicidad:** Las inserciones usarían instrucciones \`atomicCAS\` (Compare and Swap) para evitar cerrojos globales que matarían el paralelismo.
3. **Coalescencia:** Los buckets se alinearían para que los hilos de un mismo \`Warp\` accedan a direcciones de memoria contiguas, maximizando el ancho de banda del bus de memoria de video (VRAM).
4. **Throughput:** Un diseño así puede alcanzar trillones de consultas por segundo para aplicaciones de búsqueda de contraseñas o criptografía.

---
**Checklist de Calidad para Implementaciones Senior:**
- [ ] ¿Usaste un factor de carga de 0.7 o menor?
- [ ] ¿Tu función hash es resistente a ataques de colisión?
- [ ] ¿El tamaño de la tabla es una potencia de 2 para usar \`& (N-1)\` en lugar de \`%\`?
- [ ] ¿Implementaste \`Robin Hood Hashing\` para reducir la varianza?
- [ ] ¿Monitoreás el máximo desplazamiento (Max Distance from Home)?

### Glosario Técnico Gigante de Diccionarios y Tablas Hash (200 Términos)

1. **Abstracción de Datos**
   Es el concepto que te permite usar una estructura sin volverte loco con los detalles de implementación internos.
   Fijate que vos usás un Map sin saber si por debajo hay un árbol o un arreglo, y eso es clave para el diseño.
   En Programación II, buscamos que seas capaz de construir estas abstracciones para que tu código sea escalable.

2. **Acceso Aleatorio (Random Access)**
   Es la capacidad de llegar a cualquier celda de memoria con el mismo costo temporal, sin importar dónde esté.
   En las tablas hash, esto es lo que nos permite saltar directamente al bucket calculado mediante la función hash.
   Si no tuviéramos acceso aleatorio, el $O(1)$ sería un cuento chino y estaríamos todos usando listas enlazadas.

3. **Agrupamiento Primario (Primary Clustering)**
   Fenómeno nefasto del sondeo lineal donde se forman bloques gigantes de celdas ocupadas que atraen más colisiones.
   Es como un choque en la General Paz: una colisión genera una frenada que causa más choques atrás, formando un embotellamiento.
   Esto destruye el rendimiento porque te obliga a recorrer media tabla para encontrar un lugarcito libre.

4. **Agrupamiento Secundario (Secondary Clustering)**
   Ocurre cuando claves distintas tienen la misma secuencia de sondeo, aunque no colisionen en el primer índice.
   Es típico del sondeo cuadrático si no tenés cuidado con cómo definís los saltos entre una celda y la otra.
   No es tan grave como el primario, pero igual te hace perder ciclos de CPU valiosos buscando dónde meter el dato.

5. **Alineación de Memoria (Memory Alignment)**
   Es la práctica de poner los datos en direcciones que sean múltiplos del tamaño de palabra de la CPU (ej: 8 bytes).
   Si tus buckets no están alineados, la CPU tiene que hacer dos lecturas para traer un solo dato, lo que es un pecado.
   En sistemas de alto rendimiento, alineamos las tablas a la línea de caché de 64 bytes para que todo vuele.

6. **Algoritmo de Mezcla (Mixing Function)**
   Es una fase de la función hash donde "sacudís" los bits de la clave para que un cambio mínimo genere un hash distinto.
   Sin una buena mezcla, claves consecutivas (como IDs de una DB) podrían terminar colisionando de forma sistemática.
   Usamos operaciones como XOR y corrimientos de bits (shifts) para que la distribución sea lo más uniforme posible.

7. **Ancho de Banda de Memoria (Memory Bandwidth)**
   La cantidad de datos que pueden viajar de la RAM a la CPU por segundo; es el verdadero cuello de botella hoy.
   Una tabla hash ineficiente desperdicia ancho de banda trayendo datos que nunca vas a usar de la memoria principal.
   Por eso preferimos el sondeo lineal: aprovechamos cada byte que el camión de la RAM nos deja en la puerta.

8. **Árbol Rojo-Negro (Red-Black Tree)**
   Estructura de datos balanceada que Java usa para manejar colisiones cuando un bucket se vuelve demasiado grande.
   Garantiza que la búsqueda sea $O(\log n)$ incluso si un atacante te llena la tabla de colisiones a propósito.
   Es la defensa definitiva contra el HashDoS, aunque sea un poco más pesado en memoria que una lista común.

9. **Asignación de Memoria (Memory Allocation)**
   El acto de pedirle al sistema operativo o a la JVM un bloque de memoria para guardar los arreglos de la tabla.
   Pedir memoria es caro, por eso intentamos redimensionar la tabla lo menos posible para no frenar la ejecución.
   En Programación II, te enseñamos a pre-calcular el tamaño si ya sabés cuántos datos vas a meter de antemano.

10. **Ataque de Colisión (Hash Collision Attack)**
    Intento malintencionado de saturar una tabla enviando miles de claves que colisionan en el mismo bucket exacto.
    Esto transforma tu $O(1)$ en un $O(n)$, haciendo que tu servidor se ponga a 100% de CPU y deje de responder.
    Es un ataque clásico de denegación de servicio que se soluciona con funciones hash seguras o "treeification".

11. **Atomicidad (Atomicity)**
    Propiedad que garantiza que una operación en la tabla hash se haga de un solo tirón, sin interferencias.
    En ambientes multihilo, necesitás que el `put` sea atómico para no terminar con la memoria toda corrupta y rota.
    Usamos instrucciones de hardware como CAS (Compare-And-Swap) para lograr esto sin tener que bloquear todo el sistema.

12. **AVX-512 (Advanced Vector Extensions)**
    Instrucciones de la CPU que permiten operar sobre 512 bits de datos al mismo tiempo; ideal para búsquedas masivas.
    Con AVX, podés comparar 16 claves de una tabla hash en un solo ciclo de reloj, algo que antes era ciencia ficción.
    Es el futuro de las bases de datos analíticas que necesitan procesar billones de registros por segundo en memoria.

13. **Balanceo de Carga (Load Balancing)**
    En tablas hash distribuidas, es el arte de repartir las claves de forma que ningún servidor se sature de laburo.
    Si tu hash es malo, un servidor va a estar prendido fuego mientras los otros están tomando mate rascándose.
    Usamos técnicas como el Consistent Hashing para que la carga sea pareja y el sistema sea realmente elástico.

14. **Barra de Error (Error Bound)**
    En estructuras probabilísticas como los Bloom Filters, es la probabilidad aceptable de que te dé un falso positivo.
    Si ponés la barra muy baja, necesitás mucha memoria; si la ponés muy alta, el filtro no te sirve para nada útil.
    Como ingeniero, tenés que encontrar el punto justo entre el ahorro de espacio y la precisión que necesita tu app.

15. **Big-O Notation**
    La forma en que medimos cómo escala un algoritmo a medida que le tiramos más y más datos encima (el "peor caso").
    Para las tablas hash, el promedio es $O(1)$, pero el peor caso puede ser $O(n)$ si no sos pillo con las colisiones.
    Es la herramienta básica que usamos para decidir si una estructura sirve para un problema real o es un juguete.

16. **Bitmasking**
    Técnica de usar una máscara de bits para calcular el índice del bucket sin usar el operador de módulo (`%`).
    Funciona solo si el tamaño de la tabla es potencia de dos, y es órdenes de magnitud más rápido para la CPU.
    Es el típico truco de optimización que separa a un programador junior de un ingeniero que sabe lo que hace.

17. **Bitmap**
    Un arreglo de bits donde cada posición representa el estado (ocupado/libre) de un bucket de la tabla hash.
    Es súper eficiente en espacio y permite que la CPU use instrucciones vectoriales para encontrar huecos libres rápido.
    Se usa mucho en estructuras avanzadas como Hopscotch Hashing para gestionar los vecindarios de forma compacta.

18. **Bloom Filter**
    Estructura de datos que te dice si un elemento "seguro que no está" o "podría estar" en un conjunto de datos.
    Se usa como escudo para no ir a buscar al disco o a la RAM lenta algo que sabemos que no existe de entrada.
    Es una de las herramientas más brillantes de la informática moderna para sistemas que manejan volúmenes gigantes.

19. **Boxing/Unboxing**
    El proceso de la JVM de convertir un tipo primitivo (como `int`) en un objeto (como `Integer`) y viceversa.
    Es un desastre para el rendimiento de las tablas hash porque llena el heap de objetos inútiles y rompe la caché.
    Por eso en la cátedra insistimos en que para alto rendimiento tenés que usar arreglos de tipos primitivos puros.

20. **Branch Prediction**
    Capacidad de la CPU de adivinar el resultado de un `if` antes de que se ejecute para no frenar el procesamiento.
    En las tablas hash con muchas colisiones, la CPU se equivoca seguido y pierde mucho tiempo "limpiando" el error.
    Diseñar código "branch-predictable" es el nivel más alto de optimización que podés alcanzar como programador.

21. **Bucket**
    Es cada una de las posiciones del arreglo principal donde guardamos los datos (o punteros a listas) de la tabla.
    El número de buckets determina la capacidad base y qué tan probable es que dos claves distintas choquen entre sí.
    Si tenés pocos buckets para muchos datos, tu tabla va a ser un festival de colisiones y se va a arrastrar.

22. **Buffer Overflow**
    Error grave donde escribís datos fuera de los límites del arreglo de la tabla hash, rompiendo otra parte de la memoria.
    En Java esto te tira una excepción, pero en C/C++ puede causar fallos de seguridad o comportamientos muy locos.
    Es fundamental validar siempre los índices, especialmente cuando hacés cálculos complejos con funciones hash.

23. **Bytecode**
    El código intermedio que genera el compilador de Java y que después la JVM traduce a instrucciones reales de CPU.
    Analizar el bytecode de tu tabla hash te permite ver si el compilador está haciendo optimizaciones o si te está matando.
    A veces, un cambio sutil en el código Java genera un bytecode mucho más amigable para el compilador JIT.

24. **Caché L1/L2/L3**
    Pequeñas memorias ultra-rápidas que están adentro de la CPU para evitar tener que ir a buscar todo a la RAM lenta.
    Tu tabla hash tiene que ser "cache-friendly" para que los datos que más usás estén siempre a mano del procesador.
    Un "Cache Miss" es como ir a comprar pan y que la panadería esté en otra provincia: te arruina el tiempo de respuesta.

25. **Cache Line**
    Es la unidad mínima de datos (generalmente 64 bytes) que la CPU mueve entre la memoria principal y las cachés.
    Si tu tabla hash aprovecha bien la cache line, vas a traer 8 o 16 datos de un solo saque, lo que es un golazo.
    Ignorar esto hace que desperdicies el 90% del potencial de tu hardware moderno por no saber cómo fluyen los bits.

26. **Cache Miss**
    Ocurre cuando la CPU busca un dato en la caché y no lo encuentra, teniendo que esperar a que llegue desde la RAM.
    Es el enemigo número uno de las tablas hash de gran escala, porque la CPU se queda ociosa cientos de ciclos.
    Reducir los cache misses es la obsesión de cualquier ingeniero que trabaje en motores de bases de datos o juegos.

27. **Capacity (Capacidad)**
    Es el número total de buckets disponibles en la tabla; no confundir con el `size`, que son los elementos reales.
    Mantener una capacidad mayor al número de elementos es vital para que el direccionamiento abierto no explote.
    Casi siempre usamos una potencia de dos para que los cálculos matemáticos internos sean lo más simples posibles.

28. **CAS (Compare-And-Swap)**
    Instrucción de hardware que permite actualizar un valor de forma segura en ambientes concurrentes sin usar locks.
    Es la base de las estructuras "lock-free" que permiten que miles de hilos operen en la misma tabla sin frenarse.
    Si querés hacer un sistema que escale a 128 núcleos, tenés que ser un experto en usar CAS de forma correcta.

29. **Chaining (Encadenamiento)**
    Método de resolución de colisiones donde cada bucket tiene una lista de todos los elementos que hashearon ahí.
    Es fácil de implementar pero tiene el problema de que los punteros de la lista matan la localidad de referencia.
    Java lo usa en su `HashMap`, pero con la mejora de pasar a árboles si la lista se vuelve excesivamente larga.

30. **Checksum**
    Un valor hash simplificado que se usa para verificar que los datos no se hayan corrompido durante el viaje.
    No se usa para indexar tablas, pero el concepto matemático por debajo es el mismo que el de una función hash.
    En ingeniería de redes, es lo que garantiza que el paquete que mandaste llegue entero al otro lado del mundo.

31. **Clave (Key)**
    El dato único que usás para identificar y buscar un valor dentro de tu diccionario o tabla hash.
    La clave tiene que ser inmutable; si la cambiás mientras está en la tabla, el hash cambia y no la encontrás más.
    En Java, esto se traduce en que tenés que implementar `hashCode()` y `equals()` de forma consistente y prolija.

32. **Cluster**
    Un grupo de celdas ocupadas contiguas en una tabla de direccionamiento abierto que degradan el rendimiento.
    Los clusters actúan como imanes: cuantas más celdas tienen, más probable es que una nueva colisión caiga ahí.
    Evitar la formación de clusters es el objetivo principal de algoritmos como el Doble Hashing o Robin Hood.

33. **Clustering Primario**
    Ver "Agrupamiento Primario". Es el mal que aqueja al sondeo lineal y que hace que las búsquedas se vuelvan lentas.
    Es una de las razones por las que no podés llenar una tabla hash al 100% si usás direccionamiento abierto.
    En Programación II, analizamos este fenómeno para que entiendas por qué el factor de carga es tan importante.

34. **Clustering Secundario**
    Ver "Agrupamiento Secundario". Ocurre cuando claves con el mismo valor hash inicial siguen la misma ruta de sondeo.
    Aunque es menos dañino que el primario, igual genera ineficiencias que un buen ingeniero sabe cómo mitigar.
    Usar saltos que dependan de la clave (como en Double Hashing) es la cura definitiva para este problema técnico.

35. **Coherencia de Caché (Cache Coherency)**
    El sistema de hardware que garantiza que todos los núcleos de la CPU vean la misma versión de los datos en sus cachés.
    En tablas hash concurrentes, esto puede generar un tráfico enorme entre núcleos si no se diseña con cuidado extremo.
    Es el costo oculto de la programación multihilo que puede hacer que agregar más CPUs en realidad lentifique todo.

36. **Colisión (Collision)**
    Situación donde dos claves distintas producen el mismo valor hash y, por ende, quieren ir al mismo bucket.
    Las colisiones son inevitables por el Principio del Palomar (Pigeonhole Principle) a menos que la tabla sea gigante.
    Lo que define a una buena tabla hash no es si tiene colisiones, sino qué tan bien y rápido las resuelve el código.

37. **Complejidad Asintótica**
    Análisis de cómo se comporta un algoritmo cuando el tamaño de la entrada tiende a infinito (el famoso $n$).
    Para las tablas hash, nos obsesiona el $O(1)$ en promedio, que es lo que las hace superiores a los árboles para buscar.
    Entender esto te permite elegir la herramienta correcta para cada problema sin tener que adivinar o probar todo.

38. **Compresión de Hash (Hash Compression)**
    El paso final de la función hash donde reducís un número gigante (de 32 o 64 bits) al rango de índices de tu tabla.
    Se puede hacer con el operador módulo (`%`) o con un AND bitwise si sos vivo y usás tamaños potencia de dos.
    Es un paso crítico porque si la compresión es mala, vas a tener colisiones aunque tu función hash sea perfecta.

39. **Concurrency (Concurrencia)**
    Capacidad de tu tabla hash de manejar múltiples operaciones de lectura y escritura al mismo tiempo desde distintos hilos.
    Es un dolor de cabeza técnico porque tenés que evitar que los datos se corrompan sin matar el rendimiento del sistema.
    En Java, `ConcurrentHashMap` es la implementación estrella que resuelve esto usando técnicas muy avanzadas.

40. **ConcurrentHashMap**
    La implementación de diccionario thread-safe de la biblioteca estándar de Java, optimizada para alta escalabilidad.
    No usa un solo lock para toda la tabla, sino que permite que muchos hilos trabajen en buckets distintos a la vez.
    Es una obra maestra de la ingeniería de software que todo alumno de Programación II debería estudiar a fondo.

41. **Consistent Hashing**
    Técnica de hashing donde solo una pequeña fracción de las claves se remapea cuando la tabla cambia de tamaño.
    Es fundamental para sistemas distribuidos como bases de datos NoSQL donde agregás o sacás servidores todo el tiempo.
    Sin consistent hashing, agregar un servidor obligaría a mover todos los datos del sistema, lo que sería un desastre.

42. **Constant Time (Tiempo Constante)**
    Cuando una operación tarda lo mismo sin importar si tenés 10 elementos o 10 billones; se representa como $O(1)$.
    Es el "santo grial" de la computación y las tablas hash son la estructura que mejor lo logra para búsquedas.
    Fijate que "constante" no significa "instantáneo", sino que el tiempo no depende del volumen de los datos guardados.

43. **CPU Bound**
    Cuando el límite de velocidad de tu programa es la potencia de procesamiento de la CPU y no la memoria o el disco.
    En tablas hash, esto ocurre si tu función hash es demasiado compleja matemáticamente para el beneficio que da.
    Casi siempre preferimos funciones hash simples para que el programa sea "Memory Bound" y no "CPU Bound".

44. **CPU Cycle (Ciclo de Reloj)**
    La unidad mínima de tiempo de un procesador; una CPU de 3GHz hace 3 mil millones de ciclos por segundo.
    Una búsqueda exitosa en una tabla hash bien diseñada debería tomar solo unas decenas de ciclos si hay un cache hit.
    Pensar en ciclos te ayuda a entender por qué un cache miss (que tarda 300 ciclos) es tan destructivo para el código.

45. **Cuckoo Hashing**
    Técnica donde cada clave tiene dos posiciones posibles y "desaloja" a la anterior si necesita ocupar el lugar.
    Garantiza búsquedas $O(1)$ en el peor caso absoluto, lo que la hace ideal para sistemas de tiempo real o redes.
    El nombre viene del pájaro cuco, que pone sus huevos en nidos ajenos y tira los huevos que ya estaban ahí.

46. **Data Locality (Localidad de Datos)**
    Propiedad de los programas donde los datos que se usan juntos están guardados cerca en la memoria física.
    Es vital para las tablas hash porque permite que la CPU aproveche las líneas de caché de forma eficiente y rápida.
    El direccionamiento abierto tiene mucha mejor localidad de datos que el encadenamiento con listas enlazadas.

47. **Deadlock (Interbloqueo)**
    Situación donde dos hilos se quedan esperando eternamente porque cada uno tiene un recurso que el otro necesita.
    En tablas hash mal diseñadas, esto puede pasar si intentás bloquear varios buckets a la vez de forma desordenada.
    Para evitarlo, los ingenieros usamos jerarquías de locks o, mejor aún, algoritmos que no necesiten bloquear nada.

48. **Deletion (Borrado)**
    El acto de quitar un par clave-valor de la tabla hash; es más complejo de lo que parece en direccionamiento abierto.
    No podés simplemente dejar un hueco vacío porque romperías las cadenas de búsqueda de otras claves que colisionaron.
    Usamos "lápidas" (tombstones) o desplazamos los elementos para mantener la integridad de la estructura de datos.

49. **Denial of Service (DoS)**
    Ataque que busca dejar un sistema fuera de servicio; en tablas hash se logra forzando colisiones masivas.
    Un servidor web que tarda 1ms en procesar un request puede pasar a tardar 10 segundos si su tabla hash colapsa.
    Protegerse contra el HashDoS es una responsabilidad ética y técnica de cualquier desarrollador de sistemas modernos.

50. **Deterministic Function**
    Una función que, para la misma entrada, siempre devuelve exactamente la misma salida sin importar cuándo se llame.
    Es un requisito innegociable para cualquier función hash; si fuera aleatoria, nunca podrías volver a encontrar tu dato.
    Fijate que la función puede usar una "semilla" (seed), pero esa semilla debe ser constante durante la vida de la tabla.

51. **Diccionario (Dictionary)**
    TDA que asocia claves con valores, permitiendo insertar, buscar y borrar elementos de forma eficiente y sencilla.
    Es una de las abstracciones más poderosas de la computación, presente en casi todos los lenguajes de programación.
    En Programación II, estudiamos cómo implementar diccionarios usando tablas hash para lograr el mejor rendimiento.

52. **Direccionamiento Abierto (Open Addressing)**
    Familia de técnicas donde todos los elementos se guardan directamente en el arreglo principal de buckets de la tabla.
    Si hay una colisión, buscamos otra posición dentro del mismo arreglo siguiendo una secuencia de sondeo predefinida.
    Es muy eficiente en memoria y localidad de datos, pero se degrada rápido si el factor de carga es demasiado alto.

53. **Distributed Hash Table (DHT)**
    Una tabla hash donde los datos están repartidos entre miles de computadoras conectadas a través de una red.
    Es la base de sistemas como BitTorrent o bases de datos globales que manejan volúmenes de datos impensables.
    Requiere algoritmos muy complejos para manejar la caída de nodos y garantizar que los datos sigan estando ahí.

54. **Doble Hashing**
    Técnica de direccionamiento abierto que usa una segunda función hash para determinar el tamaño del salto en colisiones.
    $pos = (h1(key) + i \cdot h2(key)) \pmod M$. Esto elimina el agrupamiento secundario y distribuye las claves de forma excelente.
    Es un poco más caro de calcular que el sondeo lineal, pero vale la pena si tenés muchas colisiones en tu sistema.

55. **DRAM (Dynamic RAM)**
    La memoria principal de tu computadora; es barata y grande, pero lentísima comparada con los registros de la CPU.
    Tarda unos 100 nanosegundos en responder a un pedido, lo que para una CPU moderna es una eternidad de tiempo perdido.
    Nuestra meta con las tablas hash es tocar la DRAM lo menos posible y quedarse viviendo en las cachés de la CPU.

56. **Dynamic Resizing**
    Capacidad de una tabla hash de agrandarse (o achicarse) automáticamente cuando la cantidad de datos lo requiere.
    Generalmente duplicamos el tamaño de la tabla para amortizar el costo de tener que re-insertar todos los elementos.
    Es un proceso caro, por lo que las implementaciones senior intentan hacerlo de forma incremental o en segundo plano.

57. **Entry (Entrada)**
    El objeto o estructura que contiene el par clave-valor dentro de la tabla; a veces incluye también el valor hash.
    En Java, `HashMap.Node` es la clase interna que representa una entrada y que permite armar las listas o árboles.
    Evitar la creación de objetos `Entry` es una técnica común en sistemas de ultra-alto rendimiento para ahorrar RAM.

58. **Enumeration**
    El proceso de recorrer todos los elementos de la tabla hash uno por uno; fíjate que el orden suele ser caótico.
    Como el hash "desparrama" las claves de forma pseudo-aleatoria, no podés esperar que la enumeración sea ordenada.
    Si necesitás orden, tenés que usar estructuras como `LinkedHashMap` o árboles, que mantienen una lista aparte.

59. **Equals (Método)**
    Método en Java que define si dos objetos son iguales "por valor" y no solo por su dirección de memoria física.
    Es fundamental para resolver colisiones: una vez que el hash te lleva al bucket, usás `equals` para ver si es tu clave.
    Si implementás `hashCode()` tenés que implementar `equals()`, es la ley primera de la programación en Java.

60. **Factor de Carga (Load Factor)**
    La relación entre la cantidad de elementos en la tabla y su capacidad total (elementos / capacidad).
    Determina cuándo hay que redimensionar la tabla; un valor común es 0.75 para balances entre velocidad y memoria.
    En direccionamiento abierto, un factor de carga cercano a 1.0 es una sentencia de muerte para el rendimiento.

61. **False Positive (Falso Positivo)**
    Cuando una estructura (como un Bloom Filter) dice que un elemento está, pero en realidad no está en la tabla.
    Es un error aceptable en ciertos algoritmos probabilísticos a cambio de una velocidad o ahorro de espacio brutal.
    Lo importante es que nunca den "falsos negativos": si te dice que no está, podés estar 100% seguro de que no está.

62. **Fenómeno de Avalancha (Avalanche Effect)**
    Propiedad de una función hash donde cambiar un solo bit de la clave cambia, en promedio, la mitad de los bits del hash.
    Es lo que garantiza que claves parecidas terminen en buckets muy lejanos, minimizando las colisiones por cercanía.
    Las funciones hash criptográficas tienen un efecto de avalancha perfecto, pero las de diccionarios buscan algo similar.

63. **FIFO (First-In, First-Out)**
    Política de "primero en entrar, primero en salir"; algunas tablas hash la usan para decidir qué borrar si están llenas.
    No es común en tablas hash generales, pero sí en cachés de memoria donde el espacio es limitado y el flujo es constante.
    Ayuda a mantener los datos frescos en el sistema, descartando lo viejo que ya no se usa tanto.

64. **Fingerprint (Huella)**
    Un valor hash corto (ej: 8 o 16 bits) que se guarda junto a la entrada para acelerar las comparaciones de claves.
    Si las huellas son distintas, ya sabés que las claves son distintas sin tener que llamar al método `equals()` que es caro.
    Se usa mucho en implementaciones de alto rendimiento para evitar tocar la memoria de las claves reales innecesariamente.

65. **FKS Hashing**
    Técnica de hashing perfecto diseñada por Fredman, Komlós y Szemerédi que usa dos niveles de tablas hash.
    Garantiza búsquedas $O(1)$ en el peor caso absoluto con espacio $O(n)$, ideal para conjuntos de claves que no cambian.
    Es un concepto avanzado de teoría de algoritmos que demuestra que el hash perfecto es posible y eficiente.

66. **Flat Array (Arreglo Plano)**
    Técnica de guardar todos los datos de la tabla hash en un solo arreglo contiguo de tipos primitivos en la memoria.
    Es lo opuesto a tener un arreglo de punteros a objetos dispersos; mejora drásticamente la localidad de referencia.
    En Programación II, es el enfoque que usamos cuando queremos que el código exprima cada ciclo de la CPU.

67. **Free List**
    Una lista de buckets vacíos o disponibles que se mantiene para acelerar las inserciones en ciertas tablas hash.
    No es común en direccionamiento abierto puro, pero sí en implementaciones que manejan memoria de forma manual.
    Ayuda a reducir el tiempo de búsqueda de un hueco libre cuando la tabla empieza a estar bastante llena.

68. **Full Table Scan**
    Operación ineficiente donde tenés que recorrer todos los buckets de la tabla para encontrar algo o procesar todo.
    Es lo que intentamos evitar a toda costa con el uso del hash, ya que es una operación de complejidad lineal $O(n)$.
    Ocurre por ejemplo cuando buscás por valor en lugar de buscar por clave, lo cual es un error de diseño común.

69. **Función Hash (Hash Function)**
    Algoritmo matemático que transforma un dato de tamaño arbitrario en un número de tamaño fijo (el valor hash).
    Es el corazón de la tabla; de su calidad depende que los datos se distribuyan bien y no haya cuellos de botella.
    Tiene que ser rápida, determinística y minimizar las colisiones para ser útil en la práctica de ingeniería.

70. **Garbage Collection (GC)**
    El proceso automático de la JVM de liberar la memoria de los objetos que ya no se usan en tu programa.
    Una tabla hash que crea y destruye muchos objetos `Entry` genera mucha presión sobre el GC, causando pausas (stutter).
    Por eso los sistemas senior usan pools de objetos o arreglos de primitivos para no molestar al Garbage Collector.

71. **Generics (Genéricos)**
    Característica de Java que permite que tu tabla hash maneje cualquier tipo de objeto (K, V) manteniendo la seguridad.
    Te permite escribir el código una sola vez y usarlo para `Strings`, `Integers` o tus propias clases de negocio.
    Por debajo, Java usa "Erasure", lo que significa que en tiempo de ejecución todo se maneja como objetos genéricos.

72. **Glosario**
    Una lista de términos técnicos con sus definiciones; como este que estás leyendo ahora para aprender sobre hash.
    Es una herramienta pedagógica vital para nivelar el conocimiento y hablar todos el mismo lenguaje técnico en la cátedra.
    Si dominás los términos de este glosario, estás a mitad de camino de ser un experto en estructuras de datos.

73. **Gold Ratio Hashing**
    Técnica de compresión de hash que usa la Razón Áurea para distribuir las claves de forma uniforme en la tabla.
    Es una constante matemática que tiene propiedades mágicas para evitar patrones repetitivos en la indexación.
    Se usa mucho cuando el tamaño de la tabla no es potencia de dos y querés evitar el uso del operador módulo.

74. **Hardware Prefetcher**
    Circuito de la CPU que intenta adivinar qué memoria vas a pedir pronto y la trae a la caché antes de que la pidas.
    Es el mejor amigo del sondeo lineal: como vas de a uno, la CPU se da cuenta y te va trayendo los buckets por adelantado.
    Entender cómo piensa el prefetcher te permite escribir código que corre 10 veces más rápido sin cambiar la lógica.

75. **Hash Code (hashCode)**
    El número entero que devuelve un objeto en Java para representar su identidad a fines de indexación en tablas.
    Si dos objetos son iguales según `equals()`, DEBEN devolver el mismo `hashCode()`, sino la tabla hash se rompe.
    Es el contrato básico de la clase `Object` que tenés que respetar a rajatabla en todos tus desarrollos.

66. **Hash Collision**
    Ver "Colisión". Es cuando dos entradas terminan queriendo ocupar el mismo lugar por tener el mismo valor hash.
    No te asustes por las colisiones, son parte de la vida del ingeniero; lo importante es cómo tu código las maneja.
    En Programación II, vemos diferentes estrategias para que las colisiones no te arruinen el rendimiento del sistema.

77. **Hash Distribution**
    Qué tan parejo quedan repartidos los elementos entre todos los buckets disponibles de la tabla hash.
    Una distribución uniforme es el ideal: cada bucket tiene la misma probabilidad de ser elegido por una clave al azar.
    Si la distribución es mala, vas a tener "hotspots" (buckets muy llenos) y muchos buckets vacíos desperdiciados.

78. **Hash Map**
    La clase de Java que implementa la interfaz `Map` usando una tabla hash con encadenamiento para las colisiones.
    Es probablemente la estructura de datos más usada en todo el ecosistema Java por su versatilidad y velocidad.
    Aunque es potente, no es segura para hilos; para eso tenés que usar otras variantes como `ConcurrentHashMap`.

79. **Hash Table**
    Término genérico para la estructura de datos que usa una función hash para organizar la información en memoria.
    A veces se usa específicamente para referirse a la clase antigua `Hashtable` de Java, que está obsoleta (no la uses).
    En general, cuando hablemos de "Hash Table" nos referimos al concepto abstracto de indexación por hash.

80. **Hash Value (Valor Hash)**
    El resultado numérico de pasar una clave por la función hash; es lo que determina la posición ideal en la tabla.
    Suele ser un entero de 32 o 64 bits que después se "comprime" para que entre en el rango de los buckets disponibles.
    Un buen valor hash tiene que parecer aleatorio para claves que son muy parecidas entre sí (como "User1" y "User2").

81. **Heap Memory**
    El área de memoria de la JVM donde viven todos los objetos que creás con el operador `new` en tu código.
    Las tablas hash grandes pueden ocupar mucho heap, y si se llenan, la JVM va a pasar mucho tiempo limpiando (GC).
    Gestionar bien el heap es clave para que tu aplicación no se cuelgue o se ponga lenta bajo mucha carga de usuarios.

82. **High-Performance Computing (HPC)**
    Campo de la informática que busca exprimir cada gota de rendimiento del hardware para problemas complejos.
    En HPC, las tablas hash se diseñan a nivel de bits y ciclos de CPU para procesar trillones de datos por segundo.
    Muchos de los conceptos que vemos acá (como SIMD o alineación de caché) vienen directamente de este mundo.

83. **Hopscotch Hashing**
    Algoritmo de direccionamiento abierto donde cada bucket tiene un "vecindario" de tamaño fijo alrededor de su posición ideal.
    Usa un bitmap para saber rápido dónde están los elementos del vecindario, logrando búsquedas ultra-rápidas y locales.
    Es excelente para bases de datos en memoria porque respeta mucho la jerarquía de caché de los procesadores modernos.

84. **Hotspot**
    Un área de la tabla hash que recibe muchísimos accesos o colisiones, convirtiéndose en un cuello de botella térmico y lógico.
    Los hotspots ocurren por malas funciones hash o por patrones de acceso de los usuarios (ej: un tweet viral).
    Detectar y mitigar hotspots es una tarea diaria de los ingenieros que manejan sistemas de gran escala como Twitter.

85. **Identity Hash Map**
    Versión especial de tabla hash que compara claves usando `==` (dirección de memoria) en lugar de `.equals()`.
    Es útil para casos muy específicos como grafos de objetos o compiladores donde querés distinguir instancias exactas.
    No la uses para lógica de negocio común porque te va a dar resultados inesperados con `Strings` o `Integers`.

86. **Immutable Object**
    Un objeto que no puede cambiar de estado una vez creado; es el candidato ideal para ser clave en una tabla hash.
    Si usás objetos mutables como clave y los cambiás, el `hashCode` cambia y el dato "desaparece" de la tabla.
    En Java, `String` es inmutable y por eso es la clave más usada y segura para cualquier diccionario.

87. **Incremental Rehashing**
    Técnica de mover los datos a una tabla más grande poco a poco, en lugar de hacerlo todo de un solo saque.
    Evita las pausas largas de "Stop the World" que arruinarían la experiencia en una app de tiempo real o un juego.
    Sistemas como Redis usan esto para manejar gigabytes de datos sin que el usuario note ningún retraso.

88. **Intrusive List**
    Lista enlazada donde los punteros `next/prev` están adentro del objeto de datos en lugar de en un nodo aparte.
    Se usa en tablas hash de ultra-alto rendimiento para evitar una asignación de memoria extra por cada elemento.
    Es una técnica común en sistemas operativos y motores de juegos escritos en C++ o Rust por su eficiencia.

89. **Invariante de Estructura**
    Una condición que siempre debe ser verdadera para que la tabla hash funcione correctamente (ej: "no hay ciclos en las listas").
    Mantener las invariantes es lo que garantiza que tu código no se rompa o entre en bucles infinitos en casos bordes.
    En la cátedra, te enseñamos a pensar en estas condiciones para que tus implementaciones sean sólidas como una roca.

90. **Iterador (Iterator)**
    Objeto que te permite recorrer todos los elementos de la tabla hash de forma segura y estandarizada.
    Fíjate que si modificás la tabla mientras estás iterando, el iterador suele "explotar" (fail-fast) para evitar errores.
    Es la forma correcta de procesar todos los datos sin tener que conocer la estructura interna de los buckets.

91. **JIT (Just-In-Time) Compiler**
    Parte de la JVM que traduce el bytecode a código de máquina real mientras el programa está corriendo.
    El JIT puede optimizar tu tabla hash "en caliente", por ejemplo, eliminando chequeos innecesarios o inleando métodos.
    Escribir código que el JIT pueda optimizar fácilmente es una habilidad avanzada que buscamos desarrollar en vos.

92. **Key Set**
    La colección de todas las claves que están presentes en la tabla hash en un momento determinado.
    Obtener el key set es útil para mostrar opciones al usuario o para iterar solo sobre las identidades de los datos.
    Generalmente es una "vista" de la tabla: si borrás algo de la tabla, también desaparece del key set automáticamente.

93. **Key-Value Pair (Par Clave-Valor)**
    La unidad básica de información en un diccionario; asocia una identidad (clave) con un contenido (valor).
    Es la forma más natural de representar mapeos, como un DNI con una persona o un nombre de usuario con su perfil.
    Toda la teoría de tablas hash gira en torno a cómo guardar y recuperar estos pares de la forma más veloz posible.

94. **Lápida (Tombstone)**
    Marca especial que se deja en un bucket cuando se borra un elemento en direccionamiento abierto.
    Indica que "acá hubo algo", por lo que la búsqueda debe seguir de largo en lugar de frenar como si estuviera vacío.
    Demasiadas lápidas ensucian la tabla y la vuelven lenta; por eso a veces hay que hacer una limpieza general.

95. **Latency (Latencia)**
    El tiempo que tarda una sola operación de búsqueda en completarse; se mide en nanosegundos o microsegundos.
    Para una tabla hash, queremos una latencia baja y, sobre todo, predecible (que no haya picos de lentitud).
    La latencia es lo que siente el usuario final cuando hace click en un botón de tu aplicación.

96. **Linear Probing (Sondeo Lineal)**
    La forma más simple de direccionamiento abierto: si el bucket $h$ está ocupado, probás en $h+1, h+2, \dots$ circularmente.
    Es increíblemente rápido para la CPU por su excelente localidad de datos y simplicidad de implementación.
    Su único problema es el agrupamiento primario, pero con un buen hash y factor de carga bajo, es imbatible.

97. **LinkedHashMap**
    Variante de `HashMap` que mantiene una lista doblemente enlazada de todos los elementos en el orden que fueron insertados.
    Te permite tener la velocidad del hash para buscar, pero también un orden predecible para mostrar o iterar.
    Es ideal para implementar cachés de tipo LRU (Least Recently Used) con muy poco esfuerzo de programación.

98. **List-Based Map**
    Un diccionario implementado puramente con una lista enlazada; es la forma más lenta posible ($O(n)$).
    Solo se usa para fines pedagógicos o para tablas que sabemos que nunca van a tener más de 5 o 10 elementos.
    Es el punto de partida para entender por qué necesitamos el hash: la lista simplemente no escala a grandes volúmenes.

99. **Load Factor Threshold**
    El valor límite (ej: 0.75) que dispara el redimensionamiento automático de la tabla hash.
    Si el umbral es muy bajo, desperdiciás memoria; si es muy alto, la tabla se llena de colisiones y se vuelve lenta.
    Elegir el umbral correcto es un arte de ingeniería que depende de si priorizás el espacio o la velocidad pura.

100. **Lock**
    Mecanismo de sincronización que permite que solo un hilo a la vez acceda a una parte de la tabla hash.
    Los locks son seguros pero lentos porque obligan a los otros hilos a "dormir" y esperar su turno de ejecución.
    En Programación II, buscamos minimizar el uso de locks pesados en favor de técnicas más modernas y ágiles.

101. **Lock Stripping**
    Técnica de dividir la tabla hash en varios segmentos, cada uno con su propio lock independiente.
    Permite que hilos que tocan partes distintas de la tabla no se bloqueen entre sí, mejorando mucho la escalabilidad.
    Era la base de `ConcurrentHashMap` en versiones viejas de Java antes de que pasaran a usar CAS y nodos individuales.

102. **LRU (Least Recently Used)**
    Política de reemplazo que descarta el elemento que no se ha usado por más tiempo cuando la tabla está llena.
    Se usa muchísimo en sistemas de caché para mantener en memoria solo los datos que son realmente populares.
    Implementar un LRU eficiente requiere combinar una tabla hash con una lista doblemente enlazada.

103. **Map Interface**
    La interfaz de Java que define el contrato de lo que un diccionario debe poder hacer (put, get, remove, etc.).
    Programar contra la interfaz en lugar de contra la implementación te permite cambiar de estructura sin romper tu app.
    Es un principio básico de diseño orientado a objetos que insistimos mucho en que apliques en tus trabajos.

104. **Masking**
    Ver "Bitmasking". El acto de ocultar bits para quedarse solo con los que nos sirven para el índice de la tabla.
    Es una operación de un solo ciclo de CPU, lo que la hace la forma más eficiente de compresión de hash que existe.
    Recordá: `hash & (capacity - 1)` es tu mejor amigo para el rendimiento si la capacidad es potencia de dos.

105. **Max Distance from Home (MDFH)**
    La distancia máxima que cualquier elemento de la tabla ha tenido que viajar desde su bucket ideal por culpa de las colisiones.
    Es una métrica vital para entender la salud de una tabla de direccionamiento abierto y predecir tiempos de búsqueda.
    Si el MDFH es muy alto, significa que tu tabla está sufriendo mucho y quizás necesite un rehash urgente.

106. **Memory Bound**
    Cuando la velocidad de tu programa está limitada por qué tan rápido la RAM puede entregarle datos a la CPU.
    Casi todas las tablas hash modernas son "Memory Bound" porque la CPU es muchísimo más rápida que la memoria física.
    Por eso nos obsesionamos con la caché: queremos que el programa deje de esperar a la RAM y se ponga a laburar.

107. **Memory Leak (Fuga de Memoria)**
    Error donde tu tabla hash mantiene referencias a objetos que ya no necesitás, impidiendo que el GC los libere.
    En diccionarios globales o estáticos, esto puede hacer que tu programa consuma cada vez más RAM hasta explotar.
    Siempre tenés que limpiar la tabla o usar `WeakReferences` si los objetos tienen ciclos de vida independientes.

108. **Mixing (Mezcla)**
    Ver "Algoritmo de Mezcla". El proceso de desordenar los bits del hash para que no haya patrones predecibles.
    Un buen mixing garantiza que aunque las claves sean muy parecidas, sus posiciones en la tabla sean muy distintas.
    Es lo que hace que una función hash sea "buena" en la práctica y no solo en los papeles de la teoría matemática.

109. **Modulo Operator (%)**
    Operación matemática que devuelve el resto de una división; se usa para comprimir el hash al tamaño de la tabla.
    Es una operación "cara" para la CPU (tarda muchos ciclos) comparada con las operaciones de bits como AND o XOR.
    Siempre que puedas, evitá el módulo usando tamaños potencia de dos y máscaras de bits para ganar velocidad.

110. **Multithreading**
    Capacidad de un programa de ejecutar varias tareas al mismo tiempo usando los distintos núcleos de la CPU.
    Para una tabla hash, el multithreading es un desafío gigante porque los hilos pueden pisarse los datos entre sí.
    Aprender a manejar hilos en estructuras de datos es una de las habilidades más valiosas y difíciles del mercado.

111. **MurmurHash**
    Función hash no criptográfica muy popular por ser extremadamente rápida y tener excelentes propiedades de distribución.
    Se usa en muchísimos sistemas de big data (como Cassandra o Hadoop) para indexar billones de registros.
    No es segura contra ataques DoS, pero para uso general es de las mejores opciones que podés elegir.

112. **Mutabilidad (Mutability)**
    La capacidad de un objeto de cambiar su estado interno después de haber sido creado y puesto en la tabla hash.
    La mutabilidad es el enemigo de las claves de hash: si el objeto cambia, su `hashCode` cambia y lo perdés para siempre.
    Por eso siempre preferimos claves inmutables como `String`, `Integer` o `Records` de Java modernos.

113. **N-ary Tree**
    Árbol donde cada nodo puede tener más de dos hijos; a veces se usa dentro de buckets de hash en lugar de listas.
    No es común en la biblioteca estándar, pero sí en implementaciones especializadas para discos de alta velocidad.
    Ayuda a reducir la altura del árbol y, por ende, el número de saltos de memoria necesarios para buscar.

114. **Namespace**
    Un espacio de nombres donde las claves son únicas; una tabla hash es, en esencia, un implementador de namespaces.
    Evita que haya colisiones de nombres en tu programa, permitiendo organizar la información de forma lógica y clara.
    En Programación II, usamos diccionarios para gestionar namespaces en compiladores, bases de datos y más.

115. **Node (Nodo)**
    El bloque básico de construcción en estructuras encadenadas; en `HashMap` contiene la clave, el valor y el puntero `next`.
    Cada nodo es un objeto separado en el heap, lo que genera una dispersión de memoria que puede lentificar la caché.
    Entender cómo se conectan los nodos es fundamental para visualizar cómo funciona el encadenamiento por dentro.

116. **Non-Blocking Algorithm**
    Algoritmo que permite que los hilos avancen sin quedarse nunca bloqueados esperando por otro hilo.
    Son mucho más complejos de escribir pero garantizan que el sistema nunca se frene del todo por un hilo lento.
    Las tablas hash modernas de alto rendimiento intentan ser lo más "non-blocking" posible para escalar.

117. **Null Key / Null Value**
    La capacidad de una tabla hash de aceptar `null` como una clave o como un valor válido en su diccionario.
    Java `HashMap` permite una clave `null` (que siempre va al bucket 0), pero `Hashtable` o `ConcurrentHashMap` no.
    Tenés que tener mucho cuidado con los `null` para no terminar con `NullPointerExceptions` por todos lados.

118. **NUMA (Non-Uniform Memory Access)**
    Arquitectura de servidores donde cada CPU tiene su propia memoria local, pero puede acceder a la de otros procesadores.
    Acceder a la memoria "remota" es mucho más lento que a la local; por eso las tablas hash grandes se dividen por CPU.
    Es un tema de ingeniería de sistemas de alto nivel que cobra mucha importancia en la nube y en centros de datos.

119. **Object Header**
    Los 12 o 16 bytes iniciales que cada objeto tiene en la JVM para guardar metadatos como el tipo o el estado de locks.
    Es un costo oculto de memoria: si tenés millones de nodos pequeños, el header puede ocupar más que tus datos reales.
    Esta es otra razón técnica de peso para preferir arreglos de primitivos en tablas hash que necesitan ser compactas.

120. **Open Addressing**
    Ver "Direccionamiento Abierto". Técnica de meter todo en el mismo arreglo buscando huecos libres ante colisiones.
    Es la base de muchas optimizaciones modernas porque se lleva muy bien con cómo funcionan las CPUs de hoy.
    En la cátedra le damos mucha importancia porque es donde realmente se ve la interacción entre software y hardware.

121. **Optimización Prematura**
    El error de intentar hacer el código súper rápido antes de saber si realmente es un problema para el usuario.
    "La optimización prematura es la raíz de todos los males", decía Knuth. Primero hacelo andar, después hacelo volar.
    Pero ojo: elegir una tabla hash en lugar de una lista no es optimización prematura, es sentido común de ingeniero.

122. **Overflow**
    Situación donde la tabla hash ya no puede aceptar más elementos porque está llena y no puede redimensionarse.
    En sistemas embebidos con memoria fija, el overflow es un problema real que hay que gestionar con mucho cuidado.
    Requiere políticas de descarte o de error muy claras para que el sistema no colapse totalmente.

123. **Padding (Relleno)**
    Bytes extra que se agregan a una estructura para forzar que el siguiente dato empiece en una dirección de memoria específica.
    Se usa para evitar el "False Sharing" en CPUs multihilo, separando buckets para que no compartan la misma línea de caché.
    Es un truco "sucio" pero necesario cuando buscás exprimir hasta el último nanosegundo de performance.

124. **Page Cache**
    El sistema del sistema operativo que guarda en RAM pedazos de archivos del disco para que el acceso sea más rápido.
    Si tu tabla hash vive en un archivo mapeado (`mmap`), el rendimiento depende de qué tan bien uses la page cache.
    El sondeo lineal es genial para esto porque accede a las páginas de forma secuencial y predecible para el SO.

125. **Parallel Processing**
    Dividir una tarea de procesamiento de la tabla hash entre varios núcleos para terminar mucho más rápido.
    Por ejemplo, podés calcular los hashes de 1 millón de claves en paralelo antes de insertarlas en la tabla.
    Es una técnica clave para el manejo de Big Data donde el tiempo de procesamiento es un factor crítico de costo.

126. **Perfect Hashing**
    Ver "Hashing Perfecto". Función que garantiza que no habrá ni una sola colisión para un conjunto de datos estático.
    Es ideal para diccionarios de palabras reservadas, configuraciones fijas o tablas de búsqueda en sistemas embebidos.
    Encontrar una función hash perfecta es un problema matemático interesante que se resuelve con algoritmos específicos.

127. **Performance Counter**
    Herramientas de la CPU (como `perf` en Linux) que te permiten contar cuántos cache misses o colisiones tiene tu código.
    Es la única forma de saber de verdad qué está pasando "bajo el capó" de tu tabla hash sin tener que adivinar nada.
    Un ingeniero senior se basa en estos números y no en intuiciones para optimizar sus sistemas de datos.

128. **Pigeonhole Principle (Principio del Palomar)**
    Dicta que si tenés $n$ palomas y $m$ nidos, y $n > m$, al menos un nido tendrá más de una paloma adentro.
    En tablas hash, esto significa que si tenés más claves que buckets, las colisiones son matemáticamente obligatorias.
    Es la base teórica que nos obliga a diseñar estrategias para manejar choques de datos de forma eficiente.

129. **Pipeline Execution**
    Técnica de las CPUs de procesar varias instrucciones en distintas etapas de ejecución al mismo tiempo (como una fábrica).
    Las colisiones de hash rompen el pipeline porque obligan a la CPU a frenar y esperar resultados de memoria o saltos.
    Mantener el pipeline lleno es el secreto para que tu código corra a la velocidad máxima que permite el silicio.

130. **Pointer Chasing (Persecución de Punteros)**
    El acto de seguir una cadena de referencias en memoria (como en una lista enlazada o un árbol) para buscar un dato.
    Es nefasto para el rendimiento porque cada puntero puede ser un cache miss que frena a la CPU cientos de ciclos.
    Las tablas hash de direccionamiento abierto evitan esto usando saltos matemáticos en lugar de punteros físicos.

131. **Polynomial Rolling Hash**
    Tipo de función hash usada mucho para Strings; trata a los caracteres como coeficientes de un polinomio gigante.
    Es buena porque es fácil de calcular de forma incremental (útil para algoritmos de búsqueda de texto como Rabin-Karp).
    Java usa una variante de esto (`s[0]*31^(n-1) + s[1]*31^(n-2) + ...`) para el `hashCode()` de los `String`.

132. **Pool de Objetos (Object Pool)**
    Técnica de reutilizar los objetos `Entry` en lugar de crear nuevos y dejar que el GC los borre todo el tiempo.
    Reduce la presión sobre la memoria y mejora la latencia en sistemas que insertan y borran datos constantemente.
    Es una optimización común en servidores de juegos o sistemas de trading financiero de alta frecuencia.

133. **Potencia de Dos (Power of Two)**
    Tamaños de tabla como 1024, 2048, 4096... son ideales porque simplifican el cálculo del índice a un simple AND de bits.
    Además, se llevan muy bien con la forma en que el hardware gestiona las páginas de memoria y las líneas de caché.
    Siempre que diseñes una tabla hash, intentá que su capacidad sea una potencia de dos para ganar velocidad gratis.

134. **Prefetching**
    Ver "Hardware Prefetcher". También existe el prefetching por software, donde vos le avisás a la CPU qué va a necesitar.
    En Java es difícil de hacer, pero en lenguajes como C++ podés decirle al procesador: "Che, en 10 ciclos voy a usar este bucket".
    Es una técnica de élite para sistemas que manejan tablas tan grandes que nunca entran en la caché de la CPU.

135. **Primary Clustering**
    Ver "Agrupamiento Primario". Es el talón de Aquiles del sondeo lineal: la formación de bloques que atraen colisiones.
    Se soluciona con una mejor función hash o usando técnicas como Robin Hood que "rompen" los bloques por la fuerza.
    No dejes que el clustering primario te asuste: al 50% de carga, el sondeo lineal sigue siendo imbatible por su sencillez.

136. **Primitive Collections**
    Bibliotecas que ofrecen tablas hash diseñadas específicamente para tipos primitivos (`int`, `long`, `double`) sin boxing.
    Ejemplos son fastutil, Koloboke o Trove. Son muchísimo más rápidas y compactas que el `HashMap` estándar de Java.
    En la cátedra recomendamos conocerlas si alguna vez tenés que manejar millones de números en memoria.

137. **Priority Queue**
    Estructura que te da siempre el elemento con mayor prioridad; a veces se usa junto a tablas hash para algoritmos complejos.
    Por ejemplo, para saber rápido qué elemento borrar de una caché según su frecuencia de uso o su tiempo de vida.
    Es un ejemplo de cómo combinar estructuras de datos simples para crear sistemas de gestión de datos más potentes.

138. **Probabilística (Estructura)**
    Estructura de datos que no te da una respuesta 100% exacta, sino una respuesta con una probabilidad de error conocida.
    Los Bloom Filters o los HyperLogLog son ejemplos brillantes que ahorran muchísima memoria a cambio de una pizca de duda.
    Son el alma de los sistemas de Big Data donde "exacto" es sinónimo de "imposible" o "carísimo" de computar.

139. **Probing (Sondeo)**
    El proceso de buscar el siguiente bucket disponible cuando ocurre una colisión en direccionamiento abierto.
    La secuencia de sondeo (lineal, cuadrática, doble hash) define cómo te vas moviendo por el arreglo de la tabla.
    Un buen sondeo debe ser capaz de visitar todos los buckets de la tabla antes de rendirse y decir que está llena.

140. **Profiling**
    El acto de medir tu programa mientras corre para encontrar dónde está perdiendo el tiempo o consumiendo mucha RAM.
    Usamos profilers para ver si nuestra función hash es lenta o si tenemos demasiadas colisiones en una parte de la tabla.
    Sin profiling, cualquier intento de optimización es como tirar dardos a la oscuridad con los ojos vendados.

141. **Put (Operación)**
    El método para insertar o actualizar un par clave-valor en la tabla hash; es la operación que más laburo interno tiene.
    Tiene que calcular el hash, encontrar el bucket, manejar colisiones, verificar si hay que redimensionar y guardar el dato.
    En Programación II, es la primera operación que implementamos para entender la mecánica interna del hash.

142. **Quadratic Probing (Sondeo Cuadrático)**
    Técnica de sondeo donde los saltos crecen de forma cuadrática: $1, 4, 9, 16 \dots$ posiciones desde el hash original.
    Elimina el agrupamiento primario pero tiene una localidad de caché horrible porque salta cada vez más lejos en la RAM.
    Es un compromiso teórico que hoy en día se usa menos que el sondeo lineal optimizado con Robin Hood.

143. **Razón Áurea (Golden Ratio)**
    Número irracional ($\approx 1.618$) que se usa en funciones hash para distribuir las claves de forma uniforme.
    Se descubrió que multiplicar el hash por la razón áurea ayuda mucho a que los bits se mezclen de forma caótica y útil.
    Es un ejemplo de cómo la matemática pura termina ayudando a que tu servidor de Discord o Instagram ande más rápido.

144. **Read-Modify-Write**
    Operación atómica donde leés un valor de la tabla, lo modificás y lo volvés a escribir sin que nadie se meta en el medio.
    Es fundamental para implementar contadores o actualizaciones seguras en ambientes multihilo de alta concurrencia.
    En Java se logra con clases como `AtomicInteger` o usando los métodos atómicos de `ConcurrentHashMap`.

145. **Recursión**
    Técnica donde una función se llama a sí misma; en tablas hash se puede usar para recorrer estructuras de árboles internos.
    Ojo con la recursión en buckets muy profundos porque podés terminar con un `StackOverflowError` que te tire la app.
    Siempre que puedas, intentá transformar la recursión en un bucle iterativo para que sea más eficiente y seguro.

146. **Redimensión Amortizada**
    Concepto que explica por qué, aunque redimensionar sea caro ($O(n)$), si lo hacés pocas veces, el costo promedio es $O(1)$.
    Al duplicar el tamaño, garantizamos que pasará mucho tiempo antes de tener que volver a hacer el proceso de redimensión.
    Es la base económica de por qué las estructuras dinámicas son viables en la práctica diaria de la ingeniería.

147. **Redis**
    Base de datos en memoria ultra-veloz que es, básicamente, una tabla hash gigante accesible a través de la red.
    Se usa como caché en casi todas las apps modernas para no saturar a las bases de datos relacionales lentas (como SQL).
    Estudiar cómo Redis maneja sus diccionarios internos es una clase magistral de ingeniería de alto rendimiento.

148. **Referential Transparency**
    Propiedad de las funciones que siempre devuelven lo mismo para los mismos argumentos; clave para funciones hash.
    Permite que el compilador o la CPU hagan optimizaciones agresivas porque saben que el resultado es predecible.
    En programación funcional, esto es sagrado y ayuda mucho a razonar sobre la corrección del sistema de datos.

149. **Rehash**
    El proceso de tomar todos los elementos de una tabla vieja y volver a insertarlos en una tabla nueva más grande.
    Es necesario para bajar el factor de carga y que la tabla vuelva a ser rápida después de haber recibido muchos datos.
    Durante el rehash, la tabla suele estar bloqueada o ser más lenta, por lo que es un momento crítico del sistema.

150. **Remove (Operación)**
    El método para borrar un elemento de la tabla hash; requiere cuidado para no romper las cadenas de búsqueda.
    En direccionamiento abierto, solemos usar "lápidas" o compactar el arreglo para mantener la coherencia de la estructura.
    Es una de las operaciones más subestimadas pero que más errores genera en las implementaciones de alumnos.

151. **Resizing**
    Ver "Dynamic Resizing". El acto de cambiar la capacidad de la tabla hash para adaptarse al volumen de datos actual.
    Un buen resizing debe ser lo suficientemente inteligente para no ocurrir todo el tiempo pero tampoco quedarse corto.
    Como ingeniero, vos podés tunear los parámetros de resizing para que tu app se comporte de forma óptima.

152. **Robin Hood Hashing**
    Algoritmo de direccionamiento abierto donde los elementos "ricos" (cerca de su hash) ceden su lugar a los "pobres" (lejos).
    Esto equilibra las distancias y reduce la varianza de los tiempos de búsqueda, haciendo que el sistema sea más estable.
    Es una de las técnicas más brillantes y populares en las implementaciones modernas de alto rendimiento (como en Rust).

153. **Runtime**
    El entorno donde se ejecuta tu programa (ej: la JVM de Java o el runtime de Node.js).
    El runtime gestiona la memoria y la ejecución de tu tabla hash, aplicando optimizaciones que vos ni te imaginás.
    Conocer los detalles del runtime te permite escribir código que se lleve bien con el sistema operativo por debajo.

154. **Salt (Sal)**
    Un valor aleatorio que se mezcla con la clave antes de calcular el hash para que el resultado sea impredecible.
    Se usa para evitar que un atacante genere colisiones sabiendo de antemano qué función hash estás usando en tu server.
    Es una medida de seguridad básica en cualquier sistema expuesto a la internet pública de hoy en día.

155. **Scalability (Escalabilidad)**
    Capacidad de tu tabla hash de mantener el rendimiento a medida que aumentás la cantidad de datos o de hilos de CPU.
    Una tabla que vuela con 100 elementos pero explota con 100 millones no es escalable y no sirve para la industria real.
    Diseñar para la escalabilidad es lo que diferencia a un programador de sistemas de uno de aplicaciones simples.

156. **Secondary Clustering**
    Ver "Agrupamiento Secundario". Ocurre cuando claves con el mismo hash inicial siguen el mismo camino de colisiones.
    Es típico del sondeo lineal y cuadrático si no usás una segunda función hash para variar los saltos del sondeo.
    Aunque es menos grave que el primario, igual hay que tenerlo en cuenta cuando diseñás para latencia ultra-baja.

157. **Seed (Semilla)**
    Valor inicial constante que se le pasa a una función hash para que el resultado sea determinístico pero variado.
    Cambiando la semilla, podés obtener una distribución de datos totalmente distinta sin cambiar el algoritmo base.
    Es muy útil para pruebas o para implementar "rehashing" cambiando solo la semilla en lugar de la función entera.

158. **Segmented Locking**
    Ver "Lock Stripping". Dividir la tabla en trozos independientes para que los hilos no se choquen entre sí innecesariamente.
    Fue la técnica que permitió que las primeras versiones de Java multihilo fueran realmente rápidas y útiles.
    Hoy en día se prefiere el CAS, pero el concepto de "dividir para reinar" sigue siendo súper válido en ingeniería.

159. **Sentinel Value (Valor Centinela)**
    Un valor especial (como -1 o null) que se usa para marcar el final de una búsqueda o una condición especial en la tabla.
    Ayuda a simplificar los bucles de código, eliminando chequeos extras y haciendo que la CPU corra más rápido.
    Es un truco clásico de programación de bajo nivel que ahorra ciclos y reduce la complejidad del código fuente.

160. **Separate Chaining**
    Ver "Chaining". Usar una estructura externa (como una lista) para guardar los elementos que colisionan en un bucket.
    Es la técnica más flexible: la tabla nunca se llena del todo, solo se vuelve cada vez más lenta a medida que se satura.
    Es la base de la mayoría de las implementaciones didácticas y comerciales por su robustez ante malas funciones hash.

161. **Sequential Access**
    Acceder a los datos uno por uno en el orden que están guardados en la memoria física; la CPU ama esto.
    El sondeo lineal aprovecha el sequential access al máximo, lo que lo hace volar comparado con los saltos de los punteros.
    Siempre que puedas, diseñá tus algoritmos para que lean la memoria de forma secuencial y no dando saltos locos.

162. **Shared Memory**
    Memoria que es accesible por varios hilos o procesos al mismo tiempo; es donde viven las tablas hash concurrentes.
    Gestionar la memoria compartida es peligroso por las condiciones de carrera (race conditions) que pueden romper todo.
    Requiere un uso experto de primitivas de sincronización para que los datos sean consistentes y correctos siempre.

163. **Shift Operator (<<, >>)**
    Operaciones de mover los bits de un número a la izquierda o derecha; son ultra-rápidas para cualquier procesador.
    Se usan en funciones hash para mezclar bits y en cálculos de índices para multiplicar o dividir por potencias de dos.
    Saber usar corrimientos de bits es una señal clara de que entendés cómo funciona la computadora por dentro.

164. **SIMD (Single Instruction, Multiple Data)**
    Capacidad de la CPU de aplicar la misma operación a un "vector" de datos al mismo tiempo; es como magia de velocidad.
    Permite buscar en una tabla hash comparando muchas claves de un solo saque, logrando rendimientos asombrosos.
    Es la base de la computación moderna de alto rendimiento, desde el procesamiento de video hasta la IA y las DBs.

165. **SipHash**
    Función hash diseñada específicamente para ser rápida pero segura contra ataques de colisión intencionales.
    Es el estándar actual en lenguajes como Python, Rust y Ruby para sus diccionarios internos por su robustez técnica.
    Si estás haciendo un servicio web que recibe claves del usuario, deberías considerar seriamente usar SipHash.

166. **Size (Tamaño)**
    La cantidad real de pares clave-valor que hay guardados en la tabla en un momento dado; no confundir con capacidad.
    Llevar la cuenta del `size` es vital para saber cuándo redimensionar y para responder rápido al método `size()`.
    En implementaciones concurrentes, mantener un `size` exacto es un desafío porque muchos hilos cambian datos a la vez.

167. **Slot**
    Sinónimo de "bucket" o posición en el arreglo de la tabla hash; es el lugar donde "estacionamos" nuestros datos.
    Un slot puede estar en tres estados: vacío, ocupado o con una lápida (si se borró algo en direccionamiento abierto).
    Visualizar la tabla como una fila de slots te ayuda a entender cómo funcionan los algoritmos de sondeo y búsqueda.

168. **Software Simpatía (Mechanical Sympathy)**
    Filosofía de programar entendiendo cómo funciona el hardware por debajo para que el código sea eficiente de verdad.
    No es solo hacer que el código funcione, sino que "fluya" con la CPU, la caché y la RAM de forma armónica y veloz.
    En Programación II, buscamos que desarrolles esta simpatía mecánica para que seas un ingeniero de primer nivel.

169. **Sondeo Cuadrático**
    Ver "Quadratic Probing". Técnica de saltos $i^2$ para evitar el agrupamiento primario en direccionamiento abierto.
    Tiene la desventaja de que no visita todos los buckets a menos que el tamaño de la tabla sea un número primo especial.
    Es un tema clásico de examen para ver si entendés las sutilezas matemáticas de las secuencias de sondeo en hash.

170. **Sondeo Lineal**
    Ver "Linear Probing". La técnica de buscar el siguiente bucket libre de a uno por vez; simple, rápida y efectiva.
    A pesar de sus críticas teóricas, es la reina del rendimiento en hardware moderno por su uso impecable de la caché L1.
    Si vas a implementar una tabla hash de alto rendimiento, el sondeo lineal con Robin Hood es tu mejor opción hoy.

171. **Space Complexity**
    Medida de cuánta memoria RAM consume tu estructura de datos en relación a la cantidad de elementos que guarda.
    Para una tabla hash, solemos tener un overhead de memoria (espacio extra vacío) para garantizar que sea rápida.
    Un buen ingeniero equilibra el consumo de RAM con la velocidad de búsqueda según las necesidades del proyecto real.

172. **Spreading (Dispersión)**
    Capacidad de la función hash de repartir claves similares por toda la tabla, evitando que se amontonen en un área.
    Una buena dispersión es la clave para que el tiempo promedio de búsqueda se mantenga cerca del ideal $O(1)$.
    Se logra con algoritmos de mezcla de bits potentes y con un buen diseño de la fase de compresión del hash.

173. **Stack Memory**
    El área de memoria ultra-rápida donde viven las variables locales y las llamadas a funciones de tu programa.
    A diferencia del heap, el stack se gestiona solo y es muy pequeño; no podés guardar tablas hash grandes acá.
    Pero entender cómo el stack interactúa con el heap te ayuda a evitar errores de memoria y a optimizar el código.

174. **Static Hashing**
    Cuando el tamaño de la tabla hash se define al principio y nunca cambia durante toda la ejecución del programa.
    Se usa en sistemas donde el volumen de datos es conocido de antemano y querés evitar el costo de redimensionar.
    Es común en sistemas embebidos, juegos de consola o tablas de configuración de servidores de alto tráfico.

175. **Stop-the-World**
    Pausa total de la ejecución de tu programa para que la JVM pueda hacer tareas de mantenimiento como el Garbage Collection.
    Las tablas hash gigantes pueden causar pausas Stop-the-World muy largas si el GC tiene que recorrer millones de nodos.
    Reducir estas pausas es vital para que tu aplicación no se sienta "trabada" o lenta para el usuario que la usa.

176. **Stride (Paso)**
    La distancia entre una celda y la otra en una secuencia de sondeo; en sondeo lineal el stride es siempre 1.
    En Doble Hashing, el stride se calcula con una segunda función hash, lo que hace que cada clave tenga su propio paso.
    Variar el stride es lo que permite evitar los agrupamientos que lentifican las búsquedas en direccionamiento abierto.

177. **String Hashing**
    El arte y la ciencia de convertir una cadena de caracteres en un número entero de forma rápida y con pocas colisiones.
    Como las Strings pueden ser muy largas, el hash tiene que procesarlas de forma eficiente (generalmente de a 4 u 8 bytes).
    Java usa el multiplicador 31 porque es un primo que permite optimizaciones con shifts y restas en la CPU (`31*i == (i<<5)-i`).

178. **Structural Modification**
    Cualquier operación que cambie el número de elementos de la tabla o su estructura interna (como un rehash).
    Estas modificaciones invalidan los iteradores activos y requieren sincronización especial en ambientes concurrentes.
    Es un concepto clave para entender por qué algunas operaciones son más peligrosas que otras en sistemas multi-hilo.

179. **Symmetry (Simetría)**
    Propiedad donde si `a.equals(b)` es verdadero, entonces `b.equals(a)` también debe serlo; vital para la consistencia.
    Si tu método `equals` no es simétrico, tu tabla hash va a tener comportamientos erráticos y bugs imposibles de encontrar.
    Es parte del contrato de Java que tenés que respetar para que las colecciones de la biblioteca estándar funcionen bien.

180. **Table Size (Tamaño de Tabla)**
    Ver "Capacity". El número total de buckets; elegir el tamaño correcto es un balance entre desperdicio de RAM y velocidad.
    Si el tamaño es muy chico, hay muchas colisiones; si es muy grande, desperdiciás memoria y arruinás la localidad de caché.
    Un ingeniero senior monitorea el uso de la tabla para ajustar este tamaño de forma dinámica y eficiente según la carga.

181. **Tercera Ley de Amdahl**
    Ley que dice que la mejora de rendimiento de un sistema está limitada por la parte que no se puede paralelizar.
    En tablas hash concurrentes, el "cuello de botella" suele ser el acceso único a un bucket o el redimensionamiento global.
    Entender a Amdahl te ayuda a no gastar tiempo optimizando partes del código que no van a mover la aguja del rendimiento.

182. **Thread Safety**
    Garantía de que una estructura de datos se comporta correctamente cuando es accedida por múltiples hilos a la vez.
    Lograr thread safety sin matar la performance es el desafío máximo de la ingeniería de software moderna en servidores.
    En la cátedra estudiamos cómo el uso inteligente de locks y operaciones atómicas nos permite lograr este objetivo.

183. **Throughput (Rendimiento Bruto)**
    La cantidad total de operaciones (put/get) que tu tabla hash puede procesar por segundo bajo una carga constante.
    A diferencia de la latencia, el throughput mide la capacidad total del sistema para manejar volúmenes de laburo.
    En sistemas de Big Data, el throughput es a menudo más importante que la latencia de una sola operación individual.

184. **Time Complexity**
    Análisis de cuánto tiempo tarda un algoritmo en función del tamaño de la entrada; para hash buscamos el $O(1)$.
    Es lo que nos permite predecir si un sistema va a seguir funcionando bien cuando pasemos de mil a un millón de usuarios.
    Es la base de toda la teoría de algoritmos que vemos en Programación II y que vas a usar en toda tu carrera profesional.

185. **Tombstone (Lápida)**
    Ver "Lápida". Marcador de borrado en direccionamiento abierto que permite que las búsquedas continúen correctamente.
    Las lápidas son necesarias pero dañinas si se acumulan; actúan como "ruido" que la CPU tiene que procesar sin beneficio.
    Implementar una política de limpieza de lápidas es una marca de calidad en cualquier implementación de tabla hash senior.

186. **Transacción (Transaction)**
    Secuencia de operaciones en la tabla hash que deben ejecutarse como una unidad: o se hacen todas o no se hace ninguna.
    Lograr transacciones en tablas hash distribuidas es un problema complejo que requiere protocolos como Two-Phase Commit.
    Es vital para sistemas financieros o de inventario donde la consistencia de los datos es un requisito de vida o muerte.

187. **Traversal (Recorrido)**
    El acto de visitar todos los elementos de la tabla hash; acordate que en hash el orden suele ser impredecible y caótico.
    Si necesitás recorrer los datos en un orden específico, la tabla hash por sí sola no te sirve y necesitás algo extra.
    Pero para procesos de exportación o limpieza, el traversal común es la forma más rápida de tocar todos los datos.

188. **Treeification**
    Proceso de convertir una lista de colisiones en un árbol balanceado cuando supera un umbral (en Java 8+ es 8 elementos).
    Evita que un bucket saturado arruine el rendimiento de toda la tabla, manteniendo el peor caso en un aceptable $O(\log n)$.
    Es una de las optimizaciones más inteligentes de la historia de la JDK de Java para protegerse contra ataques externos.

189. **True Positive**
    Cuando buscás una clave que realmente está en la tabla hash y el algoritmo te devuelve el valor correcto de forma exitosa.
    Es el camino feliz (happy path) de tu código, el que debería ocurrir el 99% del tiempo en un sistema bien diseñado.
    Optimizar el "True Positive" es la prioridad número uno, ya que es lo que define la experiencia normal del usuario final.

190. **Two-Choice Hashing**
    Variante donde cada clave tiene dos funciones hash y se guarda en el bucket que esté menos lleno de los dos posibles.
    Reduce drásticamente el tamaño de la cadena de colisión más larga, mejorando el balanceo de carga de forma notable.
    Es una técnica simple pero poderosa que se usa mucho en balanceadores de carga de red y sistemas distribuidos.

191. **Uniform Hashing Assumption**
    Asunción teórica de que nuestra función hash reparte las claves de forma perfectamente uniforme entre todos los buckets.
    Aunque en la realidad nunca es perfecto, nos sirve como modelo para calcular probabilidades y rendimientos esperados.
    Como ingenieros, buscamos funciones que se acerquen lo más posible a este ideal matemático en la práctica diaria.

192. **Universal Hashing**
    Técnica de elegir una función hash al azar de una familia de funciones al iniciar el programa para evitar ataques.
    Garantiza que, sin importar qué claves elija el atacante, la probabilidad de colisión sea siempre baja en promedio.
    Es la base de la seguridad en muchos lenguajes de programación modernos para sus diccionarios y sets internos.

193. **Unsuccessful Search**
    Cuando buscás una clave que no existe en la tabla; suele ser más lenta que la búsqueda exitosa en direccionamiento abierto.
    En direccionamiento abierto, tenés que llegar a un bucket vacío (o recorrer toda la cadena) para estar seguro de que no está.
    Reducir el costo de la búsqueda fallida es clave para sistemas que validan datos (ej: ver si un usuario ya existe).

194. **Value (Valor)**
    El dato que querés guardar y recuperar asociado a una clave; puede ser cualquier cosa, desde un número a un objeto complejo.
    A diferencia de las claves, los valores no necesitan ser inmutables ni tener un hash, ya que no se usan para indexar.
    La tabla hash actúa como un mapa que te lleva de la identidad (clave) al contenido (valor) de forma instantánea.

195. **Variance (Varianza)**
    Medida de qué tan desparejas son las longitudes de las cadenas de colisión en la tabla; queremos que sea baja.
    Una varianza alta significa que algunas búsquedas son súper rápidas y otras son lentísimas, lo cual es malo para el usuario.
    Técnicas como Robin Hood Hashing se enfocan justamente en bajar la varianza para tener latencias más predecibles.

196. **Vectorization**
    Ver "SIMD". Proceso de transformar un bucle de código para que opere sobre múltiples datos en una sola instrucción.
    Es una técnica de optimización avanzada que las CPUs modernas y las GPUs usan para procesar datos a velocidades locas.
    Las tablas hash vectorizadas son el estado del arte en la ingeniería de datos actual para analítica de alto nivel.

197. **Virtual Memory**
    Sistema del SO que permite que un programa crea que tiene una memoria contigua gigante, aunque esté fragmentada en la RAM.
    Las tablas hash muy grandes dependen de cómo el SO gestiona la memoria virtual y el intercambio (swapping) con el disco.
    Si tu tabla hash cae en el área de swap (disco), su rendimiento va a pasar de nanosegundos a milisegundos (un desastre).

198. **Wait-Free Algorithm**
    Algoritmo concurrente donde cada hilo tiene garantizado terminar su operación en un número finito de pasos, pase lo que pase.
    Es el nivel más alto de thread-safety y escalabilidad, superior incluso a los algoritmos lock-free comunes.
    Son extremadamente difíciles de implementar correctamente, pero son el oro puro de la computación distribuida y crítica.

199. **Weak Reference**
    Referencia a un objeto que no impide que el Garbage Collector lo borre si no hay otras referencias "fuertes" apuntándolo.
    Se usa en `WeakHashMap` para que las entradas se borren solas cuando la clave ya no se usa en el resto del programa.
    Es fundamental para evitar fugas de memoria en sistemas de caché o registros globales de objetos de larga duración.

200. **XOR (Exclusive OR)**
    Operación lógica de bits fundamental para mezclar datos en funciones hash; es súper rápida y tiene buenas propiedades.
    Un truco común es `hash = hash ^ (hash >>> 16)` para que los bits superiores influyan en los inferiores antes de indexar.
    Es el "ingrediente secreto" de casi cualquier algoritmo de manipulación de bits que veas en la industria de sistemas.

### Bibliografía Comentada Senior: El Camino del Maestro

Si llegaste hasta acá, che, es porque realmente te apasionan los fierros y querés ser un ingeniero de los de verdad. No te quedes solo con lo que te damos en la cátedra; la informática es un océano de conocimiento que cambia todos los días. Acá te dejo una lista de libros y recursos que son la "crema de la crema" para entender cómo se construyen sistemas que no solo funcionan, sino que escalan y vuelan.

1.  **"The Art of Computer Programming, Volume 3: Sorting and Searching" - Donald Knuth**
    Es la biblia. Punto. Si querés entender la matemática profunda detrás del hash, Knuth lo explicó todo en los 70 y sigue siendo vigente. No es una lectura ligera; es para sentarse con un café y una libreta. Knuth te enseña a pensar con un rigor que no vas a encontrar en ningún tutorial de YouTube de 10 minutos. Es el fundamento de todo lo que hacemos hoy.

2.  **"Introduction to Algorithms (CLRS)" - Cormen, Leiserson, Rivest, Stein**
    El libro de algoritmos por excelencia en todo el mundo. El capítulo de tablas hash es excelente porque equilibra la teoría con la práctica. Te explica el hashing universal, el direccionamiento abierto y el encadenamiento con una claridad meridiana. Es el libro que tenés que tener en la mesa de luz si querés pasar cualquier entrevista técnica en una empresa grande (FAANG).

3.  **"Algorithms" - Robert Sedgewick y Kevin Wayne**
    Sedgewick fue alumno de Knuth y tiene una capacidad pedagógica increíble. Sus visualizaciones de cómo se forman los clusters en el sondeo lineal son oro puro. Además, tiene implementaciones en Java que son muy prolijas y siguen las convenciones que usamos en la cátedra. Es un enfoque más práctico y moderno que el de Knuth o Cormen.

4.  **"Purely Functional Data Structures" - Chris Okasaki**
    Si te interesa cómo implementar diccionarios y otras estructuras en lenguajes que no permiten mutar variables (como Haskell o el lado funcional de Scala/Java), este es el libro. Te vuela la cabeza porque te obliga a pensar sin "efectos secundarios". Las estructuras persistentes que explica son la base de cómo funcionan las bases de datos modernas como Datomic o los sistemas de control de versiones como Git.

5.  **"Systems Performance: Enterprise and the Cloud" - Brendan Gregg**
    Este libro no es de algoritmos, es de cómo medir el rendimiento real en servidores. Gregg es el genio que inventó los Flame Graphs en Netflix. Te enseña a usar herramientas para ver por qué tu tabla hash está causando cache misses o por qué el kernel de Linux está tardando en darte memoria. Es la diferencia entre un programador que "cree" que su código es rápido y un ingeniero que lo "demuestra" con datos de los contadores de performance de la CPU.

6.  **"Computer Architecture: A Quantitative Approach" - Hennessy and Patterson**
    Para entender la "simpatía mecánica" que tanto mencionamos, tenés que leer a los padres de la computación moderna. Acá vas a aprender cómo funciona el pipeline, las jerarquías de caché y los buses de memoria. Entender el hardware te hace escribir mejor software. Después de leer esto, nunca más vas a mirar un arreglo o un puntero de la misma forma.

Escuchame bien: ser un senior no es saberse todos los frameworks de moda de esta semana. Ser un senior es entender los fundamentos que no cambian con el tiempo. Estos libros te dan esos fundamentos. Meteles ficha, leelos con paciencia y vas a ver cómo tu nivel como desarrollador pega un salto cuántico. ¡Nos vemos en el código!

---
**Soli Deo Gloria**
---

## Cierre operativo

Este capítulo se considera dominado cuando podés explicar el modelo, implementarlo en Java y justificar la complejidad sin ambigüedades.

### Checklist de salida

- Podés describir el contrato de operaciones sin mencionar representación interna.
- Podés anticipar costo temporal/espacial del caso típico y peor caso.
- Podés detectar un anti-patrón y proponer una corrección concreta.

