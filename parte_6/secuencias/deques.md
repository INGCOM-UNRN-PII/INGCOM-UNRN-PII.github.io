---
title: "Deques"
subtitle: "Acceso eficiente en ambos extremos"
subject: Estructuras de Datos
description: Generalización de pilas y colas para escenarios con operaciones en frente y fondo.
---

(parte6-deques)=
# Deques: Estructuras de Doble Extremo

Las [Pilas](pilas.md) y las [Colas](colas.md) representan restricciones fundamentales de acceso (LIFO y FIFO). Sin embargo, existen escenarios donde la unidireccionalidad del flujo de datos resulta insuficiente. El **Deque** (del inglés *Double-Ended Queue*, pronunciado /dek/) surge como la generalización simétrica de estas estructuras, permitiendo inserciones y eliminaciones en tiempo constante $O(1)$ tanto en el frente (*front*) como en el fondo (*rear* o *back*).

En este capítulo no nos limitaremos a la definición básica. Analizaremos el Deque como un objeto matemático formal, desarmaremos sus implementaciones más eficientes a nivel de bits y hardware, y estudiaremos cómo esta estructura es la piedra angular del paralelismo moderno mediante algoritmos de *work-stealing*.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Dominar la estructura de Deque no solo como usuario de una biblioteca, sino como diseñador de sistemas de alto rendimiento.

**Prerrequisitos.** Comprensión sólida de [Arreglos](arreglos.md) y [Listas enlazadas](listas_enlazadas.md). Se recomienda haber cursado o estar cursando Arquitectura de Computadores para las secciones de localidad y *false sharing*.

**Desarrollo.**
1. Historia y Evolución (De Knuth a Go).
2. Formalización matemática (Axiomas y Teoría de Conjuntos).
3. Implementación interna: Arreglo Circular Dinámico (Aritmética bitwise y redimensionamiento).
4. Implementación interna: Lista Doblemente Enlazada (Centinelas y gestión de nodos).
5. Interacción con el Hardware (Caché, MESI y concurrencia).
6. Algoritmos avanzados (Work-stealing, Sliding Window, 0-1 BFS).
7. Variantes restringidas en la Teoría de Autómatas.
8. Finger Trees y Persistencia Total en lenguajes funcionales.
9. Verificación Formal con Lógica de Hoare y TLA+.
10. Deques en el Kernel de Linux y Protocolos de Red (TCP).
11. Taller de Implementación Masivo: 20 ejercicios de alta complejidad con código completo.
:::

---

## 1. Historia y Evolución del Deque

La estructura fue formalizada por **Donald Knuth** en su obra monumental *The Art of Computer Programming* (Vol. 1, 1968). Knuth la definió como una lista lineal en la que todas las inserciones y eliminaciones se realizan en los extremos.

A lo largo de las décadas, el Deque pasó de ser una curiosidad teórica a ser el motor de:
- **Smalltalk-80:** Donde se usaba para la gestión de contextos de ejecución.
- **C++ STL (1994):** `std::deque` introdujo la idea de la "lista de bloques", optimizando el acceso aleatorio.
- **Java (2004):** `ArrayDeque` reemplazó a `Stack` y `LinkedList` como la recomendación estándar para colas y pilas.
- **Go (2009):** El scheduler de goroutines basa su escalabilidad en deques de robo de trabajo.

---

## 2. Formalización Axiomática y Matemática

### 2.1. Definición Formal en Teoría de Conjuntos

Sea $V$ el conjunto de valores y $D$ el conjunto de todos los estados posibles de un Deque.
Definimos las transiciones como funciones parciales:

$$f_{addFirst}: D \times V \to D$$
$$f_{addLast}: D \times V \to D$$
$$f_{removeFirst}: D \to D \times V$$
$$f_{removeLast}: D \to D \times V$$

### 2.2. Axiomas de Coherencia Espacial

Para toda secuencia $s$ y elementos $x, y$:

1.  **Axioma de la Pila Dual:**
    $$\text{removeFirst}(\text{addFirst}(s, x)) = (s, x)$$
    $$\text{removeLast}(\text{addLast}(s, x)) = (s, x)$$

2.  **Axioma de la Cola Cruzada:**
    Si $\text{size}(s) = 0$:
    $$\text{removeFirst}(\text{addLast}(s, x)) = (s, x)$$
    $$\text{removeLast}(\text{addFirst}(s, x)) = (s, x)$$
    Si $\text{size}(s) > 0$:
    $$\text{removeFirst}(\text{addLast}(s, x)) = (\text{addLast}(s', x), y) \text{ donde } (s', y) = \text{removeFirst}(s)$$

---

## 3. Implementación con Arreglo Circular Dinámico

### 3.1. Optimización Bitwise: El Poder del 2^k

Si el tamaño $N$ no es potencia de dos, el wrap-around requiere un `if` o un `%`:
```java
// Lento
head = (head == 0) ? N - 1 : head - 1;
```
Si $N = 2^k$, la CPU usa la bandera de acarreo de forma natural:
```java
// Rápido
head = (head - 1) & (N - 1);
```

### 3.2. Prueba de la Potencia de Dos

Sea $N = 2^k$. En binario, $N$ se representa como un `1` seguido de $k$ ceros.
Entonces $N-1$ se representa como $k$ unos.
Al aplicar `& (N-1)`, estamos extrayendo los $k$ bits de menor peso.
Cualquier número $X \pmod{2^k}$ es equivalente a los $k$ bits de menor peso de $X$.
Por lo tanto, `(head - 1) & (N - 1)` es matemáticamente idéntico a `(head - 1) mod N`.

---

## 4. Hardware y Latencia de Memoria

### 4.1. Jerarquía de Caché en Deques

| Nivel | Latencia (Ciclos) | Capacidad Típica | Impacto en Deque |
| :--- | :--- | :--- | :--- |
| **L1** | 4 | 32 KB | Ideal para `ArrayDeque` pequeño |
| **L2** | 12 | 256 KB | Capacidad de redimensionamiento común |
| **L3** | 40 | 8-16 MB | Compartido entre núcleos (False Sharing) |
| **RAM** | 200+ | GB | Cache Miss fatal para `LinkedList` |

---

## 5. Algoritmos de Alto Nivel: Work-Stealing

### 5.1. El Algoritmo de Cilk

En el sistema Cilk (MIT), se acuñó el término "Work-Stealing".
1. **Dueño:** Trabaja en el fondo (LIFO). Maximiza la localidad.
2. **Ladrón:** Roba del frente (FIFO). Maximiza el tamaño de la tarea robada.

Este diseño asegura que el sistema sea **proporcionalmente eficiente**: a medida que agregás núcleos, la contención en el Deque no crece linealmente porque los ladrones operan en el extremo opuesto al dueño.

---

## 6. Ejercicios I

### Ejercicio 1: Deque con Dos Pilas (Equilibrio Dinámico)

**Problema:** Implementar un Deque usando dos pilas de tal forma que todas las operaciones sean $O(1)$ amortizado.

```java
public class TwoStackDeque<T> {
    private final Stack<T> front = new Stack<>();
    private final Stack<T> back = new Stack<>();

    public void addFirst(T item) { front.push(item); }
    public void addLast(T item) { back.push(item); }

    public T removeFirst() {
        if (front.isEmpty()) {
            if (back.isEmpty()) throw new NoSuchElementException();
            rebalance(back, front);
        }
        return front.pop();
    }

    public T removeLast() {
        if (back.isEmpty()) {
            if (front.isEmpty()) throw new NoSuchElementException();
            rebalance(front, back);
        }
        return back.pop();
    }

    private void rebalance(Stack<T> src, Stack<T> dest) {
        int n = src.size();
        Stack<T> tmp = new Stack<>();
        for (int i = 0; i < n / 2; i++) tmp.push(src.pop());
        while (!src.isEmpty()) dest.push(src.pop());
        while (!tmp.isEmpty()) src.push(tmp.pop());
    }
}
```
**Análisis:** Cada elemento se mueve a lo sumo 3 veces (inserción, balance, extracción). El costo total de $M$ operaciones es $3M$, por lo tanto es $O(1)$ amortizado.

---

### Ejercicio 2: El Deque Evictivo de Logs (Circular)

**Problema:** Un Deque de tamaño fijo que descarta el elemento más viejo al llenarse.

```java
public class CircularLog<T> {
    private final T[] buffer;
    private int head = 0;
    private int tail = 0;
    private int size = 0;

    public CircularLog(int capacity) {
        this.buffer = (T[]) new Object[capacity];
    }

    public synchronized void log(T event) {
        if (size == buffer.length) {
            head = (head + 1) % buffer.length;
            size--;
        }
        buffer[tail] = event;
        tail = (tail + 1) % buffer.length;
        size++;
    }

    public synchronized T[] getRecent(int n) {
        int count = Math.min(n, size);
        T[] result = (T[]) new Object[count];
        for (int i = 0; i < count; i++) {
            result[i] = buffer[(head + i) % buffer.length];
        }
        return result;
    }
}
```
**Análisis:** Ideal para buffers de red o logs de auditoría donde solo importa la historia reciente.

---

### Ejercicio 3: Deque Concurrente Lock-Free (Chase-Lev)

**Problema:** Implementar la lógica central de un Deque de robo de trabajo.

```java
import java.util.concurrent.atomic.AtomicLong;

public class ChaseLevDeque<T> {
    private static final int INITIAL_CAPACITY = 1024;
    private T[] array = (T[]) new Object[INITIAL_CAPACITY];
    private final AtomicLong head = new AtomicLong(0);
    private final AtomicLong tail = new AtomicLong(0);

    public void push(T item) {
        long t = tail.get();
        long h = head.get();
        if (t - h >= array.length) resize();
        array[(int)(t & (array.length - 1))] = item;
        tail.lazySet(t + 1);
    }

    public T pop() {
        long t = tail.get() - 1;
        tail.set(t);
        long h = head.get();
        if (h > t) {
            tail.set(h);
            return null;
        }
        T item = array[(int)(t & (array.length - 1))];
        if (h < t) return item;
        if (!head.compareAndSet(h, h + 1)) item = null;
        tail.set(h + 1);
        return item;
    }

    public T steal() {
        long h = head.get();
        long t = tail.get();
        if (h >= t) return null;
        T item = array[(int)(h & (array.length - 1))];
        if (!head.compareAndSet(h, h + 1)) return null;
        return item;
    }
}
```
**Análisis:** Usa `AtomicLong` para los índices y `lazySet` para minimizar las barreras de memoria en la ruta caliente del dueño.

---

### Ejercicio 4: Sliding Window Maximum (O(N))

**Problema:** Máximo de cada ventana de tamaño $K$ en un arreglo de $N$.

```java
public int[] maxSlidingWindow(int[] nums, int k) {
    if (nums == null || k <= 0) return new int[0];
    int n = nums.length;
    int[] res = new int[n - k + 1];
    Deque<Integer> dq = new ArrayDeque<>();
    for (int i = 0; i < n; i++) {
        // Eliminar elementos fuera de la ventana
        if (!dq.isEmpty() && dq.peekFirst() < i - k + 1) dq.pollFirst();
        // Mantener propiedad monótona: eliminar menores al actual
        while (!dq.isEmpty() && nums[dq.peekLast()] < nums[i]) dq.pollLast();
        dq.offerLast(i);
        // El frente siempre es el máximo
        if (i >= k - 1) res[i - k + 1] = nums[dq.peekFirst()];
    }
    return res;
}
```
**Análisis:** Cada índice se agrega y se quita una vez. $O(N)$ tiempo, $O(K)$ espacio.

---

### Ejercicio 5: Deque con `min()` en O(1)

**Problema:** Deque que soporta `getMin()` en tiempo constante.

```java
public class MinDeque<T extends Comparable<T>> {
    private final Deque<T> data = new ArrayDeque<>();
    private final Deque<T> mins = new ArrayDeque<>();

    public void addLast(T val) {
        data.addLast(val);
        while (!mins.isEmpty() && mins.peekLast().compareTo(val) > 0) mins.removeLast();
        mins.addLast(val);
    }

    public T removeFirst() {
        T val = data.removeFirst();
        if (val.equals(mins.peekFirst())) mins.removeFirst();
        return val;
    }

    public T getMin() {
        return mins.peekFirst();
    }
}
```

---

### Ejercicio 6: Deque Persistente (Undo/Redo)

**Problema:** Implementar un Deque inmutable usando copia de camino.

```java
public class PersistentDeque<T> {
    private static class Node<T> {
        final T val;
        final Node<T> next, prev;
        Node(T v, Node<T> n, Node<T> p) { this.val = v; this.next = n; this.prev = p; }
    }

    private final Node<T> head, tail;
    private final int size;

    public PersistentDeque<T> addFirst(T val) {
        Node<T> newNode = new Node<>(val, head, null);
        return new PersistentDeque<>(newNode, tail == null ? newNode : tail, size + 1);
    }
}
```

---

### Ejercicio 7: Redimensionamiento SIMD en C++

**Problema:** Usar instrucciones de 256 bits para mover el buffer circular.

```cpp
#include <immintrin.h>

void fast_copy(int* src, int* dest, size_t n) {
    for (size_t i = 0; i < n; i += 8) {
        __m256i data = _mm256_loadu_si256((__m256i*)&src[i]);
        _mm256_storeu_si256((__m256i*)&dest[i], data);
    }
}
```

---

### Ejercicio 8: Double-Ended Priority Queue (Min-Max Heap)

**Problema:** Estructura que soporta `extractMin` y `extractMax` en $O(\log N)$.

```java
public class MinMaxHeap<T extends Comparable<T>> {
    private List<T> h = new ArrayList<>();
    
    public void insert(T x) {
        h.add(x);
        bubbleUp(h.size() - 1);
    }
    
    private void bubbleUp(int i) {
        if (isMinLevel(i)) {
            if (i > 0 && h.get(i).compareTo(h.get(parent(i))) > 0) {
                swap(i, parent(i));
                bubbleUpMax(parent(i));
            } else bubbleUpMin(i);
        }
    }
}
```

---

### Ejercicio 9: Análisis de Fragmentación de Memoria

**Tarea:** Comparar el uso de memoria de 1 millón de nodos de `LinkedList` vs `ArrayDeque`.

**Resultado:**
- `LinkedList`: 1M * 40 bytes = 40 MB + overhead de objetos.
- `ArrayDeque`: 1M * 8 bytes = 8 MB + overhead del arreglo.
- **Ratio:** 5:1.

---

### Ejercicio 10: Deque en Raft (Log Truncation)

**Problema:** Implementar el truncado de log en un nodo seguidor.

```java
public void receiveEntries(int prevLogIndex, List<Entry> newEntries) {
    if (log.size() > prevLogIndex + 1) {
        while (log.size() > prevLogIndex + 1) log.removeLast();
    }
    log.addAll(newEntries);
}
```

---

### Ejercicio 11: Deque de Chunks (Bloques)

**Problema:** Implementar un Deque que no requiera copiar todo el arreglo al crecer.

```java
public class ChunkedDeque<T> {
    private List<T[]> chunks = new ArrayList<>();
    private int startChunk, endChunk;
    private int startIdx, endIdx;
    
    public void addLast(T val) {
        if (endIdx == CHUNK_SIZE) {
            chunks.add((T[]) new Object[CHUNK_SIZE]);
            endChunk++;
            endIdx = 0;
        }
        chunks.get(endChunk)[endIdx++] = val;
    }
}
```

---

### Ejercicio 12: Validador de XML "Fuzzy"

**Problema:** Usar un Deque para sugerir la etiqueta de cierre faltante.

```java
public String validate(String xml) {
    Deque<String> stack = new ArrayDeque<>();
    for (String tag : tags(xml)) {
        if (tag.startsWith("/")) {
            if (!stack.isEmpty() && stack.peekLast().equals(tag.substring(1))) stack.removeLast();
            else return "Missing closing for " + stack.peekLast();
        } else stack.addLast(tag);
    }
    return "OK";
}
```

---

### Ejercicio 13: La Máquina de Turing de un Deque

**Problema:** Simular la cinta infinita.

```java
public class TuringTape {
    private Deque<Character> left = new ArrayDeque<>();
    private Deque<Character> right = new ArrayDeque<>();
    private char current = ' ';

    public void moveLeft() {
        right.addFirst(current);
        current = left.isEmpty() ? ' ' : left.removeLast();
    }
}
```

---

### Ejercicio 14: Verificación de Invariantes Formales

**Tarea:** Implementar un método que valide el estado interno.

```java
public void validate() {
    assert size >= 0;
    assert (tail - head + capacity) % capacity == size % capacity;
    if (size > 0) assert elements[head] != null;
}
```

---

### Ejercicio 15: Deque con Saltos de Prioridad Operativa

**Problema:** Tareas urgentes saltan al frente; tareas normales al fondo.

```java
public void submit(Task t) {
    if (t.isUrgent()) queue.addFirst(t);
    else queue.addLast(t);
}
```

---

### Ejercicio 16: Benchmark de False Sharing

**Tarea:** Medir el throughput de dos hilos modificando extremos opuestos.

```java
@Benchmark
public void benchPadded(PaddedState s) {
    if (Thread.currentThread().getId() % 2 == 0) s.dq.addFirst(1);
    else s.dq.addLast(2);
}
```

---

### Ejercicio 17: Finger Trees (Estructura de Nodo)

**Problema:** Definir los nodos 2-3 para la estructura interna.

```java
abstract class Node<T> {
    abstract int size();
}
class Node2<T> extends Node<T> { T a, b; }
class Node3<T> extends Node<T> { T a, b, c; }
```

---

### Ejercicio 18: 0-1 BFS en Laberinto de Agua y Tierra

**Problema:** Encontrar el camino más corto donde el agua cuesta 0 y la tierra 1.

```java
if (grid[nx][ny] == WATER) dq.addFirst(new Point(nx, ny, dist));
else dq.addLast(new Point(nx, ny, dist + 1));
```

---

### Ejercicio 19: Sundell-Tsigas (Pseudocódigo)

**Tarea:** Explicar el concepto de "marcado de punteros" para evitar el problema ABA.

**Respuesta:** Cada puntero tiene un bit extra que indica si el nodo está siendo eliminado. Si un hilo ve el bit, "ayuda" a la eliminación antes de proceder.

---

### Ejercicio 20: Deque de Evicción por Prioridad

**Problema:** Al llenarse, eliminar el de menor prioridad global.

```java
public void add(T val, int priority) {
    if (size == MAX) {
        T victim = minHeap.poll();
        deque.remove(victim);
    }
    deque.addLast(val);
    minHeap.add(val);
}
```

---

## 11. Conclusión y Resumen Final

El Deque es la estructura de datos definitiva para el manejo de extremos. Su diseño es una lección de compromiso técnico:
- Si buscás **velocidad bruta**, usás un Arreglo Circular con máscara de bits.
- Si buscás **flexibilidad**, usás una Lista Doblemente Enlazada con Centinelas.
- Si diseñás **sistemas distribuidos o paralelos**, usás el Deque como motor de Work-Stealing.

Dominar el Deque no es saber qué métodos llamar; es entender por qué `(i - 1) & mask` es mejor que `(i - 1 + N) % N` y por qué el "robo de trabajo" se hace por el fondo.

---

## Próximo paso

Ahora que dominás el acceso por ambos extremos, es momento de subir un nivel de complejidad. En el próximo capítulo, [Colas de prioridad](colas_priority.md), veremos qué pasa cuando el orden de salida ya no depende de por dónde entró el dato, sino de su valor intrínseco.

## 1. Formalización Algebraica del Deque (Doble Punta)

El Deque es la estructura lineal más simétrica. No impone una dirección preferencial para el tiempo o el espacio.

### 1.1 Axiomas de Simetría
Sea $D$ el tipo Deque y $A$ el tipo de elementos. Las operaciones se definen como pares simétricos:
- $addFirst / addLast$
- $removeFirst / removeLast$
- $peekFirst / peekLast$

Axiomas fundamentales (Muestra):
1. $removeFirst(addFirst(d, a)) = d$
2. $removeLast(addLast(d, a)) = d$
3. $peekFirst(addFirst(d, a)) = a$
4. $peekLast(addLast(d, a)) = a$
5. $removeFirst(addLast(empty, a)) = empty$

**Análisis:** Esta simetría permite que el Deque sea usado como una **Abstracción Universal**. Podés implementar una Pila usando solo `addFirst/removeFirst` o una Cola usando `addLast/removeFirst`. Matemáticamente, el Deque es el super-conjunto de las restricciones LIFO y FIFO.

---

## 2. Hardware y Localidad en Accesos Duales

A diferencia de la pila (que solo toca un extremo), el Deque toca ambos.
1. **La Cache Line de 64 bytes:** En un `ArrayDeque` pequeño, `head` y `tail` pueden caer en la misma línea de caché.
2. **False Sharing:** Si el Hilo 1 agrega al frente y el Hilo 2 agrega al fondo, compiten por la misma línea física de RAM.
3. **Solución Mecánica:** En implementaciones de alto rendimiento (como el `ForkJoinPool`), se agrega **Padding** entre los punteros de los extremos para asegurar que cada punta de la estructura viva en un núcleo distinto de la CPU, maximizando el paralelismo real.

## 3. ALGORITMOS DE ALTO NIVEL: WORK-STEALING Y FORK-JOIN

El Deque es la estructura secreta que hace que las aplicaciones modernas (como las que usan Streams paralelos o `CompletableFuture`) escalen en sistemas multinúcleo.

### 3.1 El Algoritmo de Work-Stealing (Robo de Trabajo)
Imaginá un servidor con 16 núcleos. Si cada núcleo tiene su propia cola FIFO de tareas:
1. Un núcleo puede terminar sus tareas rápido y quedarse ocioso.
2. Otro núcleo puede estar saturado.
**La Solución Deque:** Cada núcleo tiene un Deque privado. 
- El dueño (hilo local) usa el Deque como **Pila** (`push/pop` al frente). Esto mejora la localidad temporal (procesa primero la tarea que acaba de crear).
- Si un núcleo se queda sin trabajo, se convierte en un "ladrón" y va a los Deques de otros núcleos. El ladrón roba tareas del **Fondo** (extremo opuesto al dueño) usando una operación de **Cola**.
**Por qué el Fondo:** Porque las tareas en el fondo son las más antiguas y probablemente las más grandes (raíces de un árbol de división). Robar una tarea grande asegura que el ladrón tenga trabajo durante mucho tiempo, minimizando la contención.

### 3.2 Implementación en Java: ForkJoinPool
En la JVM, cada `ForkJoinWorkerThread` tiene una cola interna. El uso del Deque permite que miles de hilos virtuales se coordinen sin necesidad de un planificador centralizado, eliminando el cuello de botella de los locks globales.

## 4. ALGORITMOS DE OPTIMIZACIÓN: DEQUE MONÓTONO

El Deque permite resolver problemas de ventanas deslizantes en tiempo lineal, algo que con una cola simple requeriría un Heap ($O(N \log K)$).

### 4.1 El Problema del Máximo en Ventana
Dada una secuencia de números y una ventana de tamaño $K$, hallá el máximo de la ventana en cada paso.
**La Estrategia del Deque Monótono:**
1. Mantener un Deque con los índices de los elementos.
2. Al procesar un nuevo número $X$:
   - Por el **Fondo**: Descartar todos los elementos menores a $X$ (ya no pueden ser máximos nunca).
   - Por el **Frente**: Descartar los elementos que ya quedaron fuera del rango de la ventana.
3. El máximo actual es siempre el elemento en el **Frente** del Deque.
**Complejidad:** Cada elemento entra y sale del Deque exactamente una vez. Costo total $O(N)$. Esta técnica es fundamental en análisis de series temporales y procesamiento de señales.

## 5. Ejercicios II


### Ejercicio 1: Implementación de Deque usando 2 Pilas
**Consigna:** Simulá un Deque usando dos estructuras LIFO.

**Resolución Detallada:**
1. Usamos `leftStack` y `rightStack`.
2. `addFirst` va a `leftStack`. `addLast` va a `rightStack`.
3. El problema ocurre al hacer `remove` cuando una pila está vacía. 
4. **Estrategia de Balanceo:** En lugar de pasar todo (costo $O(N)$), pasamos solo la mitad de los elementos de la otra pila. Esto asegura que el costo amortizado de cada operación sea $O(1)$.
**Análisis:** Es un ejercicio clásico de **Análisis Amortizado** que muestra cómo la combinación de dos restricciones direccionales puede producir una libertad dual.

### Ejercicio 2: Sliding Window Maximum Trace
**Consigna:** Hallá los máximos de la ventana $K=3$ para `[1, 3, -1, -3, 5, 3, 6, 7]`.

### Ejercicio 3: Browser History with Tab Management
**Consigna:** Diseñá un historial de navegación donde podés ir atrás/adelante, pero también cerrar pestañas antiguas por el fondo si la memoria se llena.

### Ejercicio 4: Steal-First Deque Implementation
**Consigna:** Escribí el código para una operación `steal()` que extraiga el elemento más viejo de forma segura en multihilo.

### Ejercicio 5: Palindrome Checker
**Consigna:** Usá un Deque para determinar si una frase es palíndromo ignorando espacios.

### Ejercicio 6: In-place Reversal of Deque
**Consigna:** Invertí un Deque basado en arreglo circular sin crear un arreglo nuevo.

### Ejercicio 7: Multiple Queues on One Array
**Consigna:** Implementá dos Deques que crecen uno hacia el otro desde los extremos de un mismo arreglo.

### Ejercicio 8: Task Scheduler with Priority Jump
**Consigna:** Simulá una cola de tareas donde las normales entran al fondo, pero las de sistema se "adelantan" entrando al frente.

### Ejercicio 9: First Negative in every Window
**Consigna:** Hallá el primer número negativo en cada ventana de tamaño $K$ usando Deques.

### Ejercicio 10: Deque using Circular Linked List
**Consigna:** Implementá un Deque sobre una lista circular simplemente enlazada. Analizá por qué `removeLast()` es $O(n)$ y cómo lo solucionarías.

### Ejercicio 11: Memory Footprint of ArrayDeque
**Consigna:** Calculá cuánta RAM consume un `ArrayDeque<Integer>` con 1 millón de elementos incluyendo el Object Header.

### Ejercicio 12: Work-stealing Simulation
**Consigna:** Escribí un programa que simule 2 hilos robando trabajo de un Deque central.

### Ejercicio 13: Zig-Zag Iterator
**Consigna:** Implementá un iterador que recorra un Deque saltando de un extremo a otro: Primero, Último, Segundo, Penúltimo...

### Ejercicio 14: Bounded Deque with Policy
**Consigna:** Implementá un Deque de tamaño fijo que, al llenarse, borre automáticamente el más viejo para dejar entrar al nuevo (Estrategia de Evicción).

### Ejercicio 15: Deque monótono decreciente
**Consigna:** Implementá una estructura que siempre mantenga los elementos en orden decreciente en tiempo lineal.

### Ejercicio 16: Min-Max Deque
**Consigna:** Diseñá un Deque que permita obtener el mínimo y el máximo de toda la estructura en $O(1)$.

### Ejercicio 17: Flattening Nested Deques
**Consigna:** Dada un Deque de Deques, aplanalo recursivamente.

### Ejercicio 18: Thread-Safe Deque with Fine-grained Locking
**Consigna:** Implementá un Deque que use un lock para el frente y otro para el fondo.

### Ejercicio 19: Cache Contention Benchmark
**Consigna:** Escribí un benchmark de JMH que mida la contención de caché al acceder simultáneamente a ambos extremos.

### Ejercicio 20: Event loop with Urgent Queue
**Consigna:** Simulá un bucle de eventos donde el Deque se usa para manejar tanto microtareas como macrotareas.

---

## 6. ALGEBRA DE DEQUES RESTRINGIDOS Y TEORÍA DE AUTÓMATAS

En la informática teórica, no siempre usamos el Deque completo. Las variantes restringidas nos permiten modelar clases específicas de lenguajes y procesos.

### 6.1 Input-Restricted Deque (Entrada Restringida)
Permite inserciones en un solo extremo, pero eliminaciones en ambos.
- **Uso:** Útil en procesos donde la llegada de datos es centralizada (ej. un bus de datos), pero el procesamiento puede decidir descartar elementos por antigüedad (fondo) o urgencia (frente).
- **Teoría:** Equivale a una máquina de estados que puede "deshacer" su última decisión o "purgar" su memoria más vieja según el contexto.

### 6.2 Output-Restricted Deque (Salida Restringida)
Permite inserciones en ambos extremos, pero eliminaciones en uno solo.
- **Uso:** Ideal para planificadores donde podés insertar tareas normales al fondo y tareas de alta prioridad al frente, pero el procesador siempre toma la siguiente del frente (un solo punto de consumo).

---

## 7. DEQUES EN EL SISTEMA OPERATIVO: EL IO SCHEDULER

El kernel de Linux utiliza Deques para gestionar las peticiones de lectura y escritura al disco.

### 7.1 El Algoritmo Elevator (C-LOOK)
Para evitar que el cabezal del disco se mueva de forma errática, el SO agrupa las peticiones.
1. Las peticiones entran al Deque.
2. El SO las ordena según su posición física en el plato del disco.
3. Si llega una petición urgente (ej. un fallo de página), se inserta al **Frente** del Deque para ser atendida inmediatamente después de la actual.
4. Las peticiones normales se agregan al **Fondo**.

### 7.2 Work-Stealing en el Scheduler de Procesos
En sistemas con arquitectura NUMA, el planificador de procesos utiliza Deques para balancear la carga de hilos. Si una CPU está saturada, otra CPU "roba" hilos del fondo del Deque de la CPU vecina. Esto minimiza los **Cache Misses** porque el hilo robado es el que más tiempo lleva sin ejecutarse, por lo que sus datos probablemente ya no estén en la caché caliente de la CPU original.

---

## 8. COMPARATIVA INTEGRAL: DEQUE VS OTROS LINEALES

¿Cuándo conviene pagar el precio de mantener dos extremos activos?

| Estructura | Inserción (Ambos Extremos) | Acceso al Medio | Localidad | Overhead |
| :--- | :---: | :---: | :---: | :---: |
| **Arreglo Plano** | $O(n)$ al frente | $O(1)$ | Máxima | Mínimo |
| **Pila (Array)** | $O(1)^*$ solo tope | N/A | Excelente | Mínimo |
| **Cola (Array)** | $O(1)^*$ solo fondo | N/A | Excelente | Mínimo |
| **Deque (Array)** | $O(1)^*$ ambos | $O(1)$ | Excelente | Mínimo |
| **Deque (Doble Lista)** | $O(1)$ ambos | $O(n)$ | Nula | Masivo |

\* *Costo amortizado.*

**Criterio de Ingeniería:** Si necesitás la potencia de la pila y la cola en una sola estructura, el `ArrayDeque` de Java es casi siempre la opción ganadora. Es más rápido que `Stack` y más ligero que `LinkedList`.

---

## 9. GLOSARIO TÉCNICO DE DEQUES (Ampliación)

- **ArrayDeque:** Implementación de Java basada en un arreglo circular que no permite nulos y es masivamente más rápida que `LinkedList`.
- **Double-Ended Queue:** Definición formal del Deque, permitiendo operaciones simétricas en ambos bordes.
- **Monotonic Deque:** Variante que mantiene elementos en orden para resolver problemas de rango en tiempo lineal.
- **Steal-Last Policy:** Estrategia de los algoritmos de Work-stealing donde los hilos ladrones roban del extremo más viejo para maximizar el tamaño de la tarea.
- **Wrap-around Logic:** Técnica de aritmética modular que permite que el frente de un arreglo circular esté físicamente después del fondo.

---

## BIBLIOGRAFÍA RECOMENDADA

1. **"The Art of Multiprocessor Programming"** (Herlihy): La base teórica de los algoritmos de Work-stealing.
2. **"Data Structures and Algorithms in Java"** (Goodrich et al.): Análisis formal del Deque como TAD.
3. **"Algorithm Design Manual"** (Steven Skiena): Casos de estudio sobre ventanas deslizantes y deques.

## Ejercicios III

### Ejercicio 1: Implementación de Deque usando 2 Pilas (Análisis Amortizado)
**Consigna:** Demostrá por qué el costo amortizado es $O(1)$ usando el método de los créditos.

**Resolución Detallada:**
1. **Asignación de Créditos:** Cada operación `add` paga 1 crédito para su inserción y guarda 2 créditos adicionales.
2. **El Gasto:** Cuando una pila se vacía y hay que pasar elementos, cada elemento que se mueve de la pila A a la B consume 1 crédito para ser desapilado y 1 crédito para ser apilado en la nueva.
3. **Suficiencia:** Como cada elemento ya traía sus 2 créditos desde la inserción, el costo de la reorganización masiva está pre-pagado.
**Hardware:** Aunque matemáticamente sea $O(1)$, físicamente estás duplicando la cantidad de escrituras en la RAM. Es un ejemplo de cómo la elegancia matemática de las pilas puede ser menos eficiente que un simple Ring Buffer debido al tráfico del bus de memoria.

### Ejercicio 2: Sliding Window Maximum Trace (Walkthrough)
**Consigna:** Hallá los máximos de la ventana $K=3$ para `[1, 3, -1, -3, 5, 3, 6, 7]`.

**Resolución Detallada:**
1. `[1]`: Deque = `[1]`. Máximo = 1.
2. `[1, 3]`: Descarto 1 por el fondo. Deque = `[3]`. Máximo = 3.
3. `[1, 3, -1]`: Apilo -1. Deque = `[3, -1]`. Máximo = 3.
4. `[3, -1, -3]`: Apilo -3. Deque = `[3, -1, -3]`. Máximo = 3.
5. `[-1, -3, 5]`: El 3 sale por el frente (fuera de ventana). Descarto -1, -3 por el fondo. Deque = `[5]`. Máximo = 5.
6. `[-3, 5, 3]`: Apilo 3. Deque = `[5, 3]`. Máximo = 5.
**Análisis:** El Deque mantiene únicamente los candidatos a ser máximos en el futuro, eliminando el ruido de la secuencia de forma óptima.

### Ejercicio 4: Steal-First Deque (Mecánica de Robo)
**Consigna:** Escribí el pseudocódigo para la operación `steal()` y explicá por qué usa el fondo del Deque.

**Resolución Detallada:**
```text
algoritmo steal()
    mientras true hacer
        t ← tail
        h ← head
        si h == t entonces retornar VACÍO
        dato ← array[t]
        si CAS(tail, t, t - 1) entonces
            retornar dato
        fin si
    fin mientras
fin algoritmo
```
**Análisis:** Robar del fondo minimiza la probabilidad de que el ladrón y el dueño del hilo choquen en el mismo índice, reduciendo las invalidaciones de líneas de caché de la CPU.

### Ejercicio 11: Memory Footprint of ArrayDeque (Cálculo de 64-bits)
**Consigna:** Calculá cuánta RAM consume un `ArrayDeque<Integer>` con 1 millón de elementos.

**Resolución Detallada:**
1. **Arreglo de Referencias:** 1M $\times$ 4 bytes (Compressed Oops) = 4MB.
2. **Objetos Integer:** 1M $\times$ 16 bytes (Header + int) = 16MB.
3. **Header del Deque:** 16 bytes.
4. **Total:** ~20MB.
**Comparativa:** Una `LinkedList<Integer>` ocuparía ~36MB (4MB datos + 32MB de nodos). El `ArrayDeque` es casi un 50% más eficiente en memoria simplemente por eliminar los punteros `next/prev` y los headers de los nodos.

### Ejercicio 14: Bounded Deque with Eviction (Cache de Extremos)
**Consigna:** Implementá un Deque que, al llenarse, borre automáticamente el más viejo.

**Resolución Detallada:**
```java
public void addFirstWithEviction(E e) {
    if (isFull()) removeLast();
    addFirst(e);
}
```
**Aplicación:** Este patrón es la base de las memorias caché tipo **LRU** simplificadas y de los buffers de registro (logging) de los sistemas operativos, donde siempre querés tener los últimos eventos ocurridos pero no podés permitir que el log consuma toda la RAM.

### Ejercicio 18: Thread-Safe Deque (Fine-grained Locking)
**Consigna:** Diseñá un Deque con un lock para el frente y otro para el fondo. ¿Qué pasa si la cola tiene solo 1 o 2 elementos?

**Resolución Detallada:**
Si el Deque tiene pocos elementos, el hilo que opera en el frente y el que opera en el fondo podrían intentar modificar el mismo nodo o el mismo índice. 
**Solución:** Se necesita un tercer lock de "Consolidación" o forzar a que el sistema use un solo lock cuando el tamaño es menor a un umbral de seguridad. Este es un desafío clásico de los algoritmos de **Baja Contención**.

---

## 12. COMPARATIVA MAESTRA: LA ESTRUCTURA LINEAL DEFINITIVA

¿Cuándo usar un Deque frente a una Lista o un Arreglo?

| Métrica | ArrayList | LinkedList | ArrayDeque |
| :--- | :---: | :---: | :---: |
| **Pila (LIFO)** | Buena | Pobre | **Excelente** |
| **Cola (FIFO)** | Pobre | Buena | **Excelente** |
| **Acceso al Medio** | **Máxima** | Nula | Nula |
| **Memoria** | Mínima | Masiva | **Mínima** |
| **Localidad** | **Máxima** | Nula | **Máxima** |

### 12.1 Por qué `ArrayDeque` es el rey silencioso
Muchos programadores usan `LinkedList` para colas por inercia de la facultad. Sin embargo, en la JVM moderna, el `ArrayDeque` es casi siempre superior:
1. **Sin Nodos:** Evita la creación de millones de objetos `Node`, reduciendo la presión sobre el Garbage Collector.
2. **Caché Friendly:** Al ser un arreglo circular, los elementos están contiguos, permitiendo que la CPU use su **Prefetcher** de forma óptima.
3. **Sin Nulos:** No permite elementos nulos, lo que simplifica las invariantes internas y evita chequeos de seguridad extra en el lazo caliente.

---

## 13. ESTUDIO DE CASO: EL MOTOR DE NAVEGACIÓN DE UN BROWSER (CHROME/FIREFOX)

Tu navegador gestiona el historial de cada pestaña usando una estructura que es, funcionalmente, un Deque.

### 13.1 El Problema del "Atrás" y "Adelante"
Cuando navegás de la página A $\to$ B $\to$ C:
1. La página actual es C.
2. Atrás contiene [A, B].
3. Adelante está vacío.
Si hacés click en "Atrás":
1. La página actual pasa a ser B.
2. B se inserta en la pila de "Adelante".
3. Se extrae el tope de "Atrás" (A).

### 13.2 Gestión de Memoria y Pestañas Cerradas
Los navegadores modernos no pueden guardar un historial infinito en la RAM.
- **Estrategia de Deque:** El historial es un Deque con una capacidad máxima (ej. 100 páginas).
- **Evicción:** Si abrís la página 101, el Deque borra automáticamente la página 1 (la más vieja) por el **Fondo** para dejar espacio al frente. Esto garantiza que el navegador nunca colapse por culpa de un historial de navegación de meses.

---

## 14. DEMOSTRACIÓN FORMAL: EL DEQUE MONÓTONO EN TIEMPO LINEAL

Muchos dudan de que el máximo de una ventana deslizante pueda hallarse en $O(N)$.
1. **La Observación Clave:** En cada paso del algoritmo, realizamos una inserción y, potencialmente, muchas eliminaciones.
2. **Análisis por Nodos:** Sea $N$ el largo de la secuencia. Cada elemento de la secuencia entra al Deque **exactamente una vez** (`addLast`).
3. **El Costo de Eliminar:** Un elemento puede salir por el frente (vencimiento) o por el fondo (dominación). En total, cada elemento sale **exactamente una vez**.
4. **Conclusión:** Como hay $2N$ operaciones estructurales en total, el costo acumulado es $O(2N) = O(N)$. 
**Importancia:** Esta demostración te enseña que una operación individual puede ser $O(K)$, pero si el sistema promedia el costo, el rendimiento es óptimo.

---

## GLOSARIO ENCICLOPÉDICO DE DEQUES (Ampliación Maestra)

1. **Array-wrapped Index:** Técnica de aritmética modular donde el índice lineal "da la vuelta" al llegar al fin físico del arreglo, permitiendo que el frente sea mayor que el fondo.
2. **Bi-directional Access:** Propiedad fundamental de los Deques que permite operaciones eficientes sin importar el sentido de la secuencia.
3. **Capacity Doubling:** Estrategia de crecimiento del `ArrayDeque` que asegura un costo amortizado constante para las inserciones masivas.
4. **Circular Buffer:** Implementación física de un Deque que maximiza la localidad espacial evitando corrimientos de memoria.
5. **Cilk Scheduler:** Algoritmo de planificación de tareas que inventó la técnica de Work-stealing basándose en Deques concurrentes.
6. **Head/Tail Coalescing:** Fenómeno negativo donde los punteros de ambos extremos caen en la misma línea de caché, causando contención de hardware innecesaria.
7. **Input-Restricted Automata:** Modelo teórico de una computadora que solo puede agregar datos por una punta de su cinta de memoria.
8. **Monotonic Invariant:** Regla que obliga al Deque a mantener sus elementos ordenados para resolver problemas de picos y valles en streams de datos.
9. **No-Null Constraint:** Regla del `ArrayDeque` de Java que previene errores de ambigüedad y optimiza los chequeos de nulidad en el lazo caliente.
10. **Padding (Cache Aligned):** Técnica de insertar bytes vacíos entre el frente y el fondo del Deque para mejorar el paralelismo en procesadores multinúcleo.
11. **Potency of 2 Size:** Diseño de buffers donde el tamaño es una potencia de 2 para usar la instrucción `AND` en lugar de `%` (módulo).
12. **Self-Balancing Deque:** Estructura que redistribuye elementos internamente para asegurar que las operaciones en ambos extremos sigan siendo $O(1)$.
13. **Sliding Window:** Patrón de algoritmo donde un Deque representa una sub-porción móvil de una secuencia masiva.
14. **Stealing Policy:** Conjunto de reglas que definen qué extremo del Deque es "robable" por otros hilos en un entorno paralelo.
15. **Wall-clock Profiling:** Técnica de medición que detecta cuánto tiempo pasan los hilos esperando en el frente de un Deque de trabajo vacío.

---

## Próximo paso

Habiendo dominado la libertad dual de los deques, es momento de entrar en la estructura que nos permite romper la justicia del tiempo para servir a la urgencia: las [Colas de prioridad](colas_prioridad.md).

### Ejercicio 11: Memory Footprint of ArrayDeque (Ampliación)
**Resolución Detallada:**
Para calcular el footprint de un `ArrayDeque<Integer>` de 1M de elementos en una JVM HotSpot de 64 bits:
1. **El Objeto Deque:** 12 bytes header + 4 bytes puntero arreglo + 4 bytes head + 4 bytes tail + 4 bytes padding = 32 bytes.
2. **El Arreglo Interno:** 16 bytes header + (1M $\times$ 4 bytes de referencias comprimidas) = 4,000,016 bytes.
3. **Los Elementos:** 1M objetos `Integer`. Cada uno: 12 bytes header + 4 bytes int = 16 bytes. Total = 16,000,000 bytes.
**Hardware:** El `ArrayDeque` utiliza el 100% de la Cache Line cargada porque las referencias de 4 bytes están pegadas. Una `LinkedList` desperdicia el 60% de cada línea de caché en punteros `prev/next` y metadatos de los nodos que no contienen información útil para el algoritmo.

### Ejercicio 12: Work-stealing Simulation (Mecánica de Hilos)
**Consigna:** Escribí un programa que simule 2 hilos robando trabajo de un Deque central.

**Resolución Detallada:**
1. Hilo A (Dueño) hace `popFront()`.
2. Hilo B (Ladrón) hace `removeLast()`.
3. **Conflicto:** El único caso de conflicto real es cuando queda 1 solo elemento. En ese caso, ambos hilos intentan un CAS sobre el mismo puntero. 
**Hardware:** Esta arquitectura minimiza el **Cache Invalidation Traffic**. El dueño y el ladrón operan en extremos opuestos del arreglo, por lo que casi siempre tocan líneas de caché distintas en el hardware multinúcleo.

### Ejercicio 15: Deque Monótono Decreciente (Mecánica de Alarma)
**Consigna:** Implementá una estructura que mantenga el elemento mayor de una secuencia infinita en una ventana fija.

**Resolución Detallada:**
Es la base de los sistemas de **Monitoreo de Infraestructura**. 
1. Llega un uso de CPU del 90%. Lo ponés al fondo del Deque.
2. Descartás por el fondo todos los usos anteriores menores (ej. 10%, 20%) porque el 90% "manda".
3. Al pasar el tiempo, el 90% sale por el frente (ventana de tiempo expirada).
4. El nuevo máximo es el siguiente elemento en el Deque monótono.
**Performance:** Este algoritmo permite que un servidor de monitoreo procese métricas de 10.000 servidores en tiempo real sin saturar la CPU.

### Ejercicio 20: Event loop with Urgent Queue (La Cola de React)
**Consigna:** Simulá un bucle de eventos donde el Deque se usa para manejar tanto microtareas como macrotareas.

**Resolución Detallada:**
1. Las tareas de renderizado (URGENTES) se inyectan por el **Frente** del Deque.
2. Las tareas de red (NORMALES) se inyectan por el **Fondo**.
3. El loop siempre saca del **Frente**.
**Filosofía:** Este uso del Deque permite implementar una **Prioridad de Dos Niveles** sin la complejidad de un Heap. Asegura que el usuario nunca sienta que la pantalla se congela mientras se descargan datos en segundo plano.

---

## 31. ANALISIS DE P99 Y JITTER: EL IMPACTO DE LOS DEQUES MASIVOS

Cuando usás un Deque para gestionar millones de tareas en un servidor web:
1. **La Pausa de Redimensionamiento:** Si un `ArrayDeque` se llena, debe duplicar su tamaño. Esto implica pedir un arreglo nuevo y copiar los elementos viejos. Para 10 millones de elementos, esto puede tardar cientos de milisegundos, causando un pico de latencia (Jitter) que el usuario siente.
2. **Loitering (Memoria que Flota):** Al remover un elemento del frente en un arreglo circular, es vital poner esa posición en `null`. Si no, el objeto permanecerá en el heap hasta que el puntero `head` vuelva a pasar por esa posición física, lo que puede tardar mucho tiempo en Deques grandes, causando un uso de RAM artificialmente alto.

---

## 32. DEQUES EN EL CORAZÓN DEL KERNEL: GESTIÓN DE INTERRUPCIONES

Cuando presionás una tecla, el hardware dispara una interrupción. El Kernel no puede procesar la tecla inmediatamente porque frenaría otras tareas críticas.
- **Top Half:** El driver de teclado recibe la interrupción y guarda el código de tecla en un Deque circular de hardware ultra-rápido.
- **Bottom Half:** El planificador del SO saca los códigos del Deque y los manda a la aplicación.
**Por qué Deque:** Porque permite que eventos urgentes del sistema (como un error de hardware) se inyecten por el **Frente** para ser procesados antes que las teclas normales, asegurando la estabilidad del sistema operativo.

---

## 40. RESUMEN FINAL DEL TRATADO DE DEQUES

Habiendo recorrido desde los axiomas de simetría hasta la arquitectura de los hilos virtuales de Java, queda claro que el Deque es la estructura lineal más potente.
- **Microarquitectura:** El uso de deques en el ROB de la CPU asegura el orden de commit.
- **Multinúcleo:** El Work-stealing basado en deques es lo que permite que el software moderno use todos los procesadores de tu PC.
- **Algoritmia:** El Deque monótono es la clave para procesar streams de datos en tiempo real.

## Próximo paso

Habiendo dominado la libertad dual de los deques, es momento de entrar en la estructura que nos permite romper la justicia del tiempo para servir a la urgencia: las [Colas de prioridad](colas_prioridad.md).

---

## 41. DEQUES EN EL DESARROLLO DE COMPILADORES: EL BUFFER DE LOOKAHEAD

Para parsear lenguajes complejos (como Java o C++), el compilador necesita mirar varios tokens hacia adelante sin consumirlos todavía.
1. **Lookahead Buffer:** Se implementa como un Deque restringido de salida. 
2. **Lógica:** El Lexer lee tokens y los mete al **Fondo**. El Parser mira el **Frente** para decidir qué regla de gramática aplicar. Si necesita volver atrás (backtracking), puede volver a meter tokens por el **Frente**.
**Ventaja:** Esta estructura permite que el compilador procese el código fuente en una sola pasada de $O(n)$, manteniendo un buffer de contexto constante que cabalga entre la entrada y el árbol sintáctico.

---

## 42. LA FÍSICA DEL DUAL-END: BANCOS DE MEMORIA Y DRAM

Cuando un Deque es muy grande y operás en ambos extremos, estás forzando al **Memory Controller** a saltar entre regiones de la RAM.
1. **Bancos de RAM:** La memoria física está dividida en bancos. Si el frente y el fondo caen en bancos distintos, el controlador puede disparar lecturas paralelas.
2. **Throughput:** El Deque basado en arreglo permite que el bus de datos sature su capacidad de ráfaga (Burst), lo que lo hace masivamente más rápido que una lista doble donde cada nodo requiere una señal de activación de fila DRAM independiente.

---

## BIBLIOGRAFÍA Y LECTURA RECOMENDADA

1. **"The Cilk System"** (Leiserson et al.): El paper fundacional del Work-stealing.
2. **"Computer Systems: A Programmer's Perspective"** (Bryant & O'Hallaron): Para entender la física de la memoria circular.
3. **"Modern C++ Design"** (Andrei Alexandrescu): Análisis de la performance de los deques segmentados.

---

## Próximo paso

### Ejercicio 6: In-place Reversal of Deque (Ampliación)
**Análisis de Algoritmo:**
Invertir un Deque circular no es solo dar vuelta el arreglo.
1. **Opción Ineficiente:** Crear un arreglo nuevo y copiar. $O(N)$ espacio.
2. **Opción Óptima:** Intercambiar el contenido de `head` y `tail`, avanzar `head` y retroceder `tail` circularmente hasta que se crucen.
**Hardware:** Estás modificando las dos puntas de la estructura simultáneamente. Si el Deque es muy grande, esto dispara dos flujos de lectura/escritura independientes en el controlador de memoria, saturando los canales de datos.

### Ejercicio 16: Min-Max Deque (Diseño de Estructura)
**Consigna:** Obtené el min y el max en $O(1)$.

**Resolución Detallada:**
Para lograr $O(1)$, necesitás una estructura híbrida.
1. Usamos el Deque principal para los datos.
2. Usamos dos **Deques Monótonos** auxiliares: uno para el mínimo y otro para el máximo.
3. Al agregar $X$: 
   - En el `minDeque`, descartamos por el fondo todo lo mayor a $X$.
   - En el `maxDeque`, descartamos por el fondo todo lo menor a $X$.
4. El mínimo global es `minDeque.peekFirst()`.
**Costo:** Triplicamos el uso de memoria, pero ganamos una capacidad analítica en tiempo real inalcanzable para un arreglo simple.

### Ejercicio 19: Cache Contention Benchmark (JMH Analysis)
**Consigna:** Medí la contención al acceder a ambos extremos.

**Resolución Detallada:**
Si dos hilos están en un bucle cerrado haciendo `addFirst` y `addLast` sobre el mismo `ArrayDeque`:
1. Verás que el throughput total es menor que si cada uno operara en su propia estructura.
2. **Causa:** El protocolo de coherencia **MESI** está saltando entre los estados `Modified` e `Invalid` en los núcleos de la CPU.
**Solución:** Usá la anotación `@Contended` en los campos `head` y `tail` del Deque para forzar la alineación a líneas de caché distintas.

---

## 43. DEQUES EN BASES DE DATOS: GESTIÓN DE WRITE-AHEAD LOGS (WAL)

Las bases de datos (PostgreSQL, MySQL) usan deques internos para gestionar el buffer de logs.
1. **Commit al Fondo:** Cada transacción escribe su log al final de un deque en memoria.
2. **Flush al Frente:** Un hilo de fondo saca lotes de logs del frente del deque para persistirlos en el disco.
**Resiliencia:** Si ocurre un error de escritura en disco, el sistema puede re-insertar el lote fallido por el **Frente** del Deque, asegurando que se intente de nuevo antes que los nuevos logs que siguen llegando. Esto garantiza la integridad atómica de la base de datos ante picos de latencia de I/O.

---

## GLOSARIO TÉCNICO DE DEQUES (Nivel Arquitecto)

1. **Amortized Simmetry:** Garantía de que, en promedio, las operaciones en ambos extremos del Deque tienen el mismo costo computacional.
2. **Atomic Dual-Reference:** Técnica concurrente que permite actualizar el frente y el fondo de un Deque en una sola instrucción atómica de hardware (ej. en procesadores con instrucciones `CAS` de 128 bits).
3. **Deque-based Automata:** Modelo de cómputo superior a la pila, capaz de reconocer una clase más amplia de lenguajes (lenguajes sensibles al contexto limitados).
4. **Double-Ended Priority Queue:** Estructura que combina un Deque con un Heap, permitiendo extraer el mínimo y el máximo de forma eficiente.
5. **Heuristic Stealing:** Política de balanceo de carga donde un procesador decide de qué Deque robar basándose en el historial de performance de sus vecinos.
6. **LIFO/FIFO Dualism:** Propiedad de los Deques que les permite actuar como pilas o colas indistintamente según el contrato requerido por la aplicación.
7. **Monotonic Deque Invariant:** Regla de orden que permite resolver problemas de picos y valles en streams de datos en tiempo lineal $O(n)$.
8. **Paged ArrayDeque:** Implementación donde el Deque no es un solo arreglo, sino una lista de bloques de arreglos, evitando la pausa de redimensionamiento masivo.
9. **Segmented Deque Layout:** Disposición de memoria donde el Deque está fragmentado en páginas del SO para minimizar el footprint de RAM.
10. **Victim Thread:** En el algoritmo de Work-stealing, el hilo cuyo Deque está siendo asaltado por un procesador ocioso.

---

## RESUMEN FINAL DEL TRATADO (Ampliación)

Habiendo recorrido desde la simetría algebraica hasta la arquitectura de los hilos virtuales y los buffers de bases de datos, queda claro que el Deque es la estructura lineal suprema.
- **Microarquitectura:** El commit FIFO de la CPU se gestiona con Deques.
- **Multinúcleo:** La escalabilidad de Java 21 (Virtual Threads) depende del Work-stealing de los Deques.
- **Diseño:** Representa la unión perfecta entre la justicia del tiempo (FIFO) y la memoria del pasado (LIFO).

## Próximo paso

---

## 44. DEQUES EN LA PROGRAMACIÓN FUNCIONAL: EL REAL-TIME DEQUE

En lenguajes puramente inmutables, implementar un Deque con operaciones $O(1)$ de peor caso (no solo amortizado) es un reto monumental.
1. **La Técnica de Hood y Melville:** Usan cuatro listas inmutables y un proceso de "rebalanceo perezoso" (lazy).
2. **Determinismo:** A diferencia del `ArrayDeque` de Java, que puede tener picos de latencia al redimensionarse, un Deque funcional en tiempo real garantiza que CUALQUIER operación tarde lo mismo. 
**Aplicación:** Es vital en sistemas de control de satélites o medicina robótica donde un pico de latencia del 10% puede significar un error catastrófico.

---

## 45. TABLA FINAL DE COMPLEJIDADES LINEALES (EL MAPA DEFINITIVO)

| Operación | ArrayList | LinkedList | ArrayDeque | Deque Enlazado |
| :--- | :---: | :---: | :---: | :---: |
| addFirst | $O(n)$ | $O(1)$ | $O(1)^*$ | $O(1)$ |
| addLast | $O(1)^*$ | $O(1)$ | $O(1)^*$ | $O(1)$ |
| removeFirst | $O(n)$ | $O(1)$ | $O(1)$ | $O(1)$ |
| removeLast | $O(1)$ | $O(n)$ | $O(1)$ | $O(1)$ (si es doble) |
| get(i) | $O(1)$ | $O(n)$ | $O(1)$ | $O(n)$ |

\* *Costo amortizado.*

---

## EPÍLOGO: LA SINFONÍA DE LOS EXTREMOS

Dominar el deque es, en última instancia, aprender a orquestar el movimiento de la información sin prejuicios de dirección. Hemos visto cómo una simple idea de "doble punta" escala desde los buffers de teclado de tu sistema operativo hasta los complejos algoritmos de robo de trabajo que mantienen viva la web moderna.

No te quedes con la superficie. La próxima vez que veas un sistema equilibrado, un procesador repartiendo carga o un historial de navegación fluido, recordá que hay un deque asegurando que la libertad de acceso no signifique caos. Que la búsqueda de la simetría sea tu brújula, pero que la comprensión de las latencias sea tu ancla. La informática es una disciplina de equilibrios, y hoy has descendido hasta las raíces mismas de la dualidad lineal. Construí con sabiduría, medí con rigor y nunca dejes de preguntarte qué sucede en las puntas de tus estructuras. Que tus deques nunca desborden y tu paralelismo siempre sea escalable.

## Próximo paso

Habiendo dominado la libertad dual de los deques, es momento de entrar en la estructura que nos permite romper la justicia del tiempo para servir a la urgencia: las [Colas de prioridad](colas_prioridad.md).

### Ejercicio 8: Task Scheduler with Priority Jump (Ampliación)
**Mecánica de Procesamiento:**
En un servidor de alta disponibilidad, las tareas de "Mantenimiento de Red" o "Salud del Nodo" no pueden esperar en una cola de 10.000 peticiones de usuario.
- **Inserción al Frente:** Usar `addFirst` permite que estas tareas salten toda la espera sin necesidad de implementar una lógica de comparación compleja en un Heap.
- **Simplicidad:** Esta es una forma de **Prioridad de Dos Niveles** que mantiene el costo de inserción en $O(1)$ amortizado, comparado con el $O(\log N)$ de una cola de prioridad completa.

### Ejercicio 9: First Negative in every Window (Ampliación)
**Algoritmia de Señales:**
Este problema es fundamental para detectar "caídas" en señales de sensores. 
1. Usamos un Deque para guardar índices de números negativos.
2. Al mover la ventana, el Deque nos dice instantáneamente cuál es el negativo más viejo que todavía está en rango.
3. Si el Deque está vacío, no hay negativos en la ventana.
**Hardware:** Estás reduciendo el número de comparaciones en un factor de $K$ (el tamaño de la ventana). Para ventanas grandes (ej. 10.000 muestras), la diferencia de performance es abismal.

### Ejercicio 13: Zig-Zag Iterator (Ampliación)
**Algoritmo de Recorrido:**
```java
public E next() {
    return leftSide ? deque.removeFirst() : deque.removeLast();
}
```
**Análisis:** Este iterador es destructivo (consume la estructura). Es la base de algoritmos de **Balanceo de Carga** donde querés alternar entre procesar lo más nuevo y lo más viejo para evitar que ninguna parte de la secuencia se "oxide" en el heap.

---

## 46. TIPS PARA EL EXAMEN FINAL: DOMINANDO EL DEQUE

Si tenés que defender tu conocimiento sobre deques, asegurate de tener estos conceptos grabados a fuego:

- **La Invariante Circular:** Explicá cómo se calcula el fondo en un `ArrayDeque` y por qué se usa el módulo (o el AND bitwise).
- **Work-Stealing:** Es la aplicación estrella. Explicá por qué el ladrón roba del fondo (para maximizar el tamaño de la tarea y minimizar la contención).
- **Simetría:** Mencioná que el Deque es la estructura lineal "definitiva" porque contiene a la Pila y a la Cola como casos especiales.

---

## Próximo paso

---

## 47. ANEXO: TABLA DE COSTOS DE HARDWARE PARA DEQUES

| Escenario | Ciclos de CPU | Latencia Estimada | Motivo |
| :--- | :---: | :---: | :--- |
| addFirst (ArrayDeque) | 20 | 5ns | Bounds check + Modulo |
| addFirst (LinkedList) | 500+ | 125ns | Node Allocation (Eden) |
| Work-steal (Fondo) | 1000+ | 250ns | CAS Conflict |
| removeFirst (Vacio) | 10 | 2ns | Immediate Fails |

**Nota:** El ArrayDeque es sistemáticamente más rápido porque evita el 'Eden Space Allocation' que tanto castiga a las listas enlazadas en Java. Al operar sobre un arreglo pre-alocado, estás convirtiendo un problema de gestión de objetos en un problema de aritmética simple.

## 48. RESUMEN DE LA FAMILIA LINEAL

- **Arreglos:** El espacio contiguo (Máxima performance).
- **Listas:** Los nodos libres (Máxima flexibilidad).
- **Pilas:** La restricción LIFO (Memoria del pasado).
- **Colas:** La restricción FIFO (Justicia del presente).
- **Deques:** La libertad simétrica (El super-conjunto lineal).

## Próximo paso

Habiendo dominado la libertad dual de los deques, es momento de entrar en la estructura que nos permite romper la justicia del tiempo para servir a la urgencia: las [Colas de prioridad](colas_prioridad.md).


### Palabras finales
No subestimes el Deque. Aunque parezca solo una 'cola con más métodos', es la pieza de ingeniería que permite que tu procesador Intel maneje hilos, que tu navegador Chrome sea fluido y que los servidores de streaming no pierdan paquetes. Has llegado a la cima de las estructuras lineales. Lo que sigue es entrar en el terreno donde el orden deja de ser posicional para convertirse en una relación de importancia: las colas de prioridad.


---

## 49. RECORRIDO DE LA PARTE 6: PUNTO DE CONTROL

Llegaste a la mitad del recorrido de las estructuras secuenciales. Ya dominás:
1. La física de la RAM y la caché.
2. La medición de performance (Profiling).
3. La dualidad Arreglo/Lista.
4. Las restricciones Pila/Cola/Deque.

Este conocimiento te diferencia de un simple 'programador' y te convierte en un 'arquitecto de software' que entiende el costo de cada línea de código. Usá este poder con sabiduría.


### Reflexión sobre la Simetría
La simetría es una propiedad rara en el software. La mayoría de las veces el flujo es unidireccional. El Deque nos recuerda que, a veces, la mejor solución es aquella que no toma partido por ninguna dirección y nos da la libertad total de elegir.


---

## 50. BIBLIOGRAFÍA AVANZADA
- **Algorithm Design** (Kleinberg & Tardos): Análisis de flujos y deques.
- **The Art of Computer Programming** (Knuth): El capítulo de secuencias es legendario.

