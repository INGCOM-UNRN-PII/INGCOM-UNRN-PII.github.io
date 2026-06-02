---
title: "Colas de prioridad"
subtitle: "Elegir el siguiente elemento por prioridad"
subject: Estructuras de Datos
description: Análisis formal, implementaciones avanzadas y optimización a nivel de hardware de las colas de prioridad.
---

(parte6-colas-prioridad)=
# Colas de prioridad

Las colas de prioridad representan una ruptura fundamental con las estructuras de secuencia lineales tradicionales como pilas y colas FIFO. Mientras que en estas el orden de salida está predeterminado por la cronología de las operaciones (`push`/`enqueue`), en una cola de prioridad el orden emerge de una propiedad intrínseca de los datos: su **prioridad**.

Este capítulo profundiza en la formalización matemática del TAD, las estructuras de datos que lo soportan con eficiencia óptima y las sutilezas de implementación que separan a un sistema de juguete de un motor de simulación de alto rendimiento.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender la cola de prioridad como TAD y dominar la mecánica interna de los heaps como su implementación estándar.

**Prerrequisitos.** Conviene haber leído [Deques](deques.md) y tener presente el marco de [Análisis de algoritmos](../algoritmos.md).

**Desarrollo.** Primero se define el TAD y su formalismo. Luego se analiza el heap binario, sus algoritmos y variantes avanzadas. Se cierra con consideraciones de hardware, aplicaciones industriales y ejercicios de alta complejidad.
:::

## 1. Formalización: El TAD Cola de Prioridad

Desde una perspectiva algebraica, una Cola de Prioridad (CP) es un tipo abstracto de datos que gestiona un multiconjunto de elementos, cada uno asociado a un valor de una estructura de orden.

### 1.1. La Relación de Orden Parcial

Para que una CP sea consistente, el conjunto de prioridades $P$ debe estar dotado de una **relación de orden total** o, al menos, un **orden parcial** que permita decidir, para cualquier par de elementos, cuál es el "mínimo" (o "máximo").

Sea $S$ el conjunto de elementos de la cola. Definimos una relación $\le$ tal que para todo $a, b, c \in S$:
1. **Reflexividad**: $a \le a$.
2. **Antisimetría**: Si $a \le b$ y $b \le a$, entonces $a = b$ (en términos de prioridad).
3. **Transitividad**: Si $a \le b$ y $b \le c$, entonces $a \le c$.

En la práctica, si dos elementos tienen la misma prioridad ($a \le b \land b \le a$), la CP puede devolver cualquiera de los dos, a menos que se exija **estabilidad**, en cuyo caso se introduce un orden total utilizando el tiempo de llegada como desempate.

### 1.2. Axiomas del TAD

Si definimos $C$ como el estado de la CP, podemos modelar las operaciones mediante los siguientes axiomas funcionales (para una Min-Priority Queue):

- **vacia(crear()) = true**
- **vacia(insertar(C, x)) = false**
- **minimo(insertar(crear(), x)) = x**
- **minimo(insertar(insertar(C, x), y)) =** 
  - si $x < y$ entonces $x$
  - si $y \le x$ entonces $y$
  - (asumiendo que $C$ no altera el mínimo actual si $x$ e $y$ son mejores).
- **eliminarMin(insertar(crear(), x)) = crear()**

### 1.3. Especificación de Operaciones

| Operación | Precondición | Postcondición / Efecto |
| :--- | :--- | :--- |
| `insert(item, p)` | El ítem tiene una prioridad $p$ comparable. | El tamaño de la CP aumenta en 1. |
| `findMin()` | `!isEmpty()` | Devuelve $x \in CP$ tal que $\forall y \in CP, x.p \le y.p$. |
| `deleteMin()` | `!isEmpty()` | Elimina y devuelve el elemento de `findMin()`. |
| `decreasePriority(item, new_p)` | `item \in CP` y `new_p < item.p`. | Actualiza la prioridad y reestructura la CP. |

## 2. Heap Binario: El Estándar de Oro

El heap binario es la implementación paradigmática de una cola de prioridad. Es un árbol binario que satisface dos invariantes estrictos:

### 2.1. Los Invariantes del Heap

1.  **Invariante de Forma**: El árbol es un **árbol binario completo**. Esto significa que todos los niveles están llenos, excepto posiblemente el último, que se llena de izquierda a derecha. Esta propiedad garantiza que la altura del árbol sea siempre $\lfloor \log_2 N \rfloor$.
2.  **Invariante de Orden (Min-Heap)**: Para cada nodo $v$ distinto de la raíz, la prioridad de su padre $p(v)$ satisface $p(v) \le v$. En consecuencia, la raíz siempre contiene el elemento con la mínima prioridad del conjunto.

### 2.2. Algoritmos de Reestructuración

Para mantener estos invariantes tras una mutación, se utilizan dos operaciones fundamentales:

#### Sift-up (Ascenso o Flotación)
Se utiliza tras insertar un elemento en la última posición disponible del árbol.
1. Se coloca el elemento en la primera hoja libre (preservando la forma).
2. Mientras el elemento sea menor que su padre:
   - Intercambiar elemento con su padre.
   - Subir al nivel del padre.
3. Costo: $O(\text{altura}) = O(\log N)$.

#### Sift-down (Descenso o Hundimiento)
Se utiliza tras eliminar la raíz (min) y reemplazarla con el último elemento del árbol para mantener la forma.
1. Se coloca el último elemento del arreglo en la raíz.
2. Mientras el elemento tenga hijos y sea mayor que el menor de sus hijos:
   - Intercambiar con el hijo menor (para no violar la propiedad de heap con el otro hermano).
   - Bajar al nivel del hijo.
3. Costo: $O(\text{altura}) = O(\log N)$.

### 2.3. Build-Heap: El Milagro del Tiempo Lineal

Es un error común pensar que construir un heap a partir de un arreglo de $N$ elementos cuesta $O(N \log N)$ (aplicando $N$ inserciones). Existe un algoritmo más eficiente que lo hace en **$O(N)$**.

El método consiste en procesar los nodos en orden inverso (del último al primero) y aplicar `sift-down` a cada uno. Como las hojas (la mitad de los nodos) ya son heaps válidos de altura 0, empezamos desde el último nodo no-hoja.

#### Demostración Matemática del Costo $O(N)$

Sea $H$ la altura del heap. El número de nodos a altura $h$ (donde las hojas están a $h=0$) es a lo sumo $\lceil N / 2^{h+1} \rceil$. El trabajo realizado por `sift-down` en un nodo de altura $h$ es $O(h)$.
El costo total $T(N)$ es:
$$T(N) = \sum_{h=0}^{\lfloor \log N \rfloor} \frac{N}{2^{h+1}} O(h) = O\left( N \sum_{h=0}^{\infty} \frac{h}{2^h} \right)$$
La serie infinita $\sum_{h=0}^{\infty} \frac{h}{2^h}$ converge a 2. Por lo tanto:
$$T(N) = O(N \cdot 2) = O(N)$$
Este resultado es fundamental para algoritmos como Heapsort o para inicializar colas de prioridad masivas.

## 3. Variantes Avanzadas y Estructuras Especializadas

Si bien el heap binario es excelente para propósitos generales, ciertos dominios requieren optimizar operaciones específicas.

### 3.1. D-ary Heaps: Minimizando la Altura
Un $d$-ary heap es similar a un heap binario, pero cada nodo tiene $d$ hijos. 
- **Ventaja**: La altura se reduce a $\log_d N$. Esto acelera drásticamente la operación `insert` y `decreasePriority`.
- **Desventaja**: La operación `deleteMin` se vuelve más cara, ya que al hundir un nodo debe compararse con $d$ hijos en cada nivel para encontrar el mínimo, costando $O(d \log_d N)$.
- **Caso de uso**: En algoritmos donde `decreasePriority` domina (como Dijkstra en grafos densos), un 4-ary o 8-ary heap suele superar al binario.

### 3.2. Heaps Amortizados: Fibonacci y Pairing Heaps
Para aplicaciones teóricas y grafos de escala masiva, buscamos que `decreaseKey` sea casi instantáneo.

| Estructura | `insert` | `deleteMin` | `decreaseKey` |
| :--- | :--- | :--- | :--- |
| Binary Heap | $O(\log N)$ | $O(\log N)$ | $O(\log N)$ |
| Fibonacci Heap | $O(1)$ | $O(\log N)^*$ | $O(1)^*$ |
| Pairing Heap | $O(1)$ | $O(\log N)^*$ | $O(2^{2\sqrt{\log \log N}})^*$ |

*\* Costo amortizado.*

**Fibonacci Heaps**: Utilizan una colección de árboles. La operación `decreaseKey` simplemente "corta" el nodo y lo mueve a la raíz de un nuevo árbol, manteniendo un potencial que garantiza que el costo total de las operaciones de limpieza (`cleanup`) durante el próximo `deleteMin` no exceda el límite logarítmico.

## 4. Hardware y Localidad de Memoria

La representación implícita en arreglo de los heaps no es solo una curiosidad de implementación; es una decisión de ingeniería de performance.

### 4.1. Localidad de Referencia
En un heap binario guardado en un arreglo, los niveles superiores del árbol (la raíz y sus hijos directos) ocupan los primeros índices. Debido a que casi todas las operaciones de `sift-up` y `sift-down` deben pasar por estos niveles, estos elementos suelen estar siempre "calientes" en la **caché L1** del procesador.

### 4.2. Cache-Oblivious Heaps
Aunque el heap binario es bueno para la caché, para conjuntos de datos que superan la memoria RAM (estructuras que viven en disco), se prefieren estructuras como los **B-heaps** o el **Funnel heap**, que minimizan los fallos de página mediante una disposición jerárquica que respeta los límites de los bloques de memoria.

## 5. Aplicaciones Industriales

### 5.1. Algoritmo de Huffman para Compresión
La construcción de un árbol de Huffman requiere extraer repetidamente los dos nodos con menor frecuencia para fusionarlos. Una CP es el motor que permite construir este árbol óptimo en $O(N \log N)$.

### 5.2. Simulación de Eventos Discretos (DES)
En simuladores de tráfico, redes o procesos industriales, los eventos se encolan con un "timestamp" de ocurrencia. El motor de simulación extrae el evento con menor tiempo, lo procesa, y posiblemente inserta nuevos eventos futuros. La precisión y el rendimiento del simulador dependen directamente de la CP.

### 5.3. Scheduling en RTOS (Real-Time Operating Systems)
En sistemas de tiempo real, el planificador de tareas debe garantizar que la tarea con mayor prioridad se ejecute inmediatamente. A diferencia de un SO de propósito general que usa "time-slicing", un RTOS usa una CP estricta para decidir qué hilo posee la CPU.

## 6. Ejercicios de Alta Complejidad

1. **D-ary Heap Parametrizado**: Implementá una clase `DAryHeap<T>` que reciba $d$ como parámetro de construcción. Demostrá empíricamente para qué valor de $d$ el algoritmo de Dijkstra en un grafo aleatorio con densidad del 10% alcanza su punto de rendimiento máximo.
2. **K-way Merge de Archivos Gigantes**: Tenés 100 archivos de 10GB cada uno, cada uno con enteros ordenados. Disponés de solo 512MB de RAM. Usá una CP para realizar un merge ordenado de todos los archivos en un único archivo de salida de 1TB.
3. **Mediana en un Stream**: Diseñá una estructura que permita insertar números uno por uno y consultar la mediana del conjunto en $O(1)$. Pista: Usá un Max-Heap y un Min-Heap balanceados.
4. **CP Estable**: Modificá la implementación de un Heap Binario para que sea estable (FIFO para prioridades iguales) sin agregar un campo `timestamp` a cada elemento. ¿Es posible? Justificá.
5. **Heapsort In-place**: Implementá Heapsort utilizando el algoritmo de Floyd para la construcción del heap inicial y analizá por qué es preferible a realizar $N$ inserciones.
6. **Decrease-Key Eficiente**: Para implementar `decreaseKey` en un heap binario en $O(\log N)$, necesitás conocer la posición actual de un elemento en el arreglo. Diseñá un mecanismo de "back-pointers" o un Map de posiciones que se mantenga actualizado durante los intercambios (`swaps`).
7. **Análisis de Colisiones en CP**: En un sistema de trading de alta frecuencia, muchas órdenes llegan con la misma prioridad de precio. Implementá una CP donde cada nodo del heap sea, a su vez, una cola FIFO de órdenes.
8. **Lazy Deletion**: Implementá una CP que soporte `remove(item)` en tiempo amortizado $O(\log N)$ marcando elementos como "borrados" y limpiándolos solo cuando lleguen a la raíz.
9. **Meldable Heaps**: Investigá e implementá un **Skew Heap**. Demostrá que la operación `merge(H1, H2)` tiene un costo amortizado de $O(\log N)$.
10. **Hardware-Friendly Heap**: Rediseñá el cálculo de índices del hijo izquierdo y derecho para un heap 4-ary de modo que use operaciones de desplazamiento de bits (`shifts`) y máscaras, evitando multiplicaciones y sumas lentas.
11. **CP Concurrente**: Diseñá un esquema de bloqueo fino (fine-grained locking) para un heap binario que permita a múltiples hilos realizar `insert` y `deleteMin` simultáneamente sin bloquear toda la estructura.
12. **The K-th Smallest**: Dado un arreglo desordenado de $N$ elementos, encontrá el $k$-ésimo elemento más pequeño en tiempo $O(N + k \log N)$.
13. **Huffman Dinámico**: Implementá una versión del algoritmo de Huffman donde las frecuencias cambian en tiempo real y la CP debe actualizarse eficientemente.
14. **Top K Elements**: Tenés un stream infinito de logs. Mantené en todo momento los 1000 logs con mayor severidad vistos hasta ahora usando un min-heap de tamaño fijo.
15. **Task Scheduler con Deadline**: Implementá un scheduler donde las tareas tengan una prioridad base y un "deadline". Si el deadline se acerca, la prioridad debe aumentar dinámicamente.
16. **Memoria Virtual y Heaps**: Simulá el comportamiento de un heap binario en un sistema con paginación y demostrá cómo la disposición en el arreglo afecta la tasa de *page faults* comparado con un árbol de búsqueda enlazado.
17. **Dijkstra en Grafos Implícitos**: Usá una CP para resolver el cubo de Rubik, donde los estados son nodos de un grafo y la prioridad es la distancia estimada al estado resuelto (A*).
18. **Priority Search Tree**: Investigá cómo combinar las propiedades de un Heap y un BST para permitir consultas de rango en 2D.
19. **Análisis de Complejidad de Build-Heap**: Escribí una demostración formal por inducción de que $\sum_{h=1}^{\log n} h \cdot \frac{n}{2^{h+1}} = O(n)$.
20. **Software Defined Networking (SDN)**: Implementá una CP para gestionar el ancho de banda de un switch, donde los paquetes se descartan si la CP está llena (política de *Tail Drop* vs *Random Early Detection*).

## 1. Formalización Algebraica de la Cola de Prioridad

A diferencia de la cola FIFO, donde la justicia es temporal, la cola de prioridad es una **Abstracción de Importancia**.

### 1.1 Axiomas de Prioridad
Sea $PQ$ el tipo Cola de Prioridad y $A$ el tipo de elementos con una relación de orden $\leq$.
- $insert: PQ \times A \to PQ$
- $deleteMin: PQ \to PQ$
- $findMin: PQ \to A$

Axiomas fundamentales:
1. $findMin(insert(empty, a)) = a$
2. $findMin(insert(pq, a)) = \min(a, findMin(pq))$
3. $deleteMin(insert(empty, a)) = empty$
4. $deleteMin(insert(pq, a)) = \text{si } a < findMin(pq) \text{ entonces } pq \text{ sino } insert(deleteMin(pq), a)$

**Análisis:** La definición de `deleteMin` es recursiva y asegura que si el nuevo elemento es el más chico, lo sacamos; de lo contrario, lo guardamos y buscamos el mínimo en el resto de la estructura. Esta formalización es la base de los algoritmos de **Planificación de CPU** en sistemas operativos, donde el elemento con menor "tiempo de ejecución restante" es el que debe ser despachado.

---

## 2. El Heap Binario: La Invariante de la Montaña

El heap binario es una realización física de la cola de prioridad que utiliza la forma de un **Árbol Binario Casi Completo**.

### 2.1 Invariante de Orden (Heap Property)
Cada nodo debe ser menor o igual que sus hijos. Esto no garantiza un orden total (un hermano puede ser mayor que otro), pero garantiza que el camino desde cualquier hoja hasta la raíz es una secuencia monótona decreciente.

### 2.2 Sift-Up y Sift-Down: La Mecánica de la Estabilidad
Para mantener la invariante tras una modificación, usamos dos algoritmos:
1. **Sift-Up (Ascenso):** Cuando insertamos al final, el elemento "sube" comparándose con su padre ($O(\log n)$).
2. **Sift-Down (Hundimiento):** Cuando borramos la raíz, ponemos el último elemento en el tope y lo "hundimos" eligiendo siempre el hijo menor para preservar la propiedad ($O(\log n)$).
**Hardware:** Estos algoritmos solo tocan un camino de la raíz a una hoja. Como el árbol está aplanado en un arreglo, la CPU puede predecir los índices de los hijos ($2i+1, 2i+2$) y precargar los datos en la caché L1.

## 3. ALGORITMOS AVANZADOS Y DEMOSTRACIONES DE COMPLEJIDAD

### 3.1 Build-Heap: El Triunfo de la Eficiencia Lineal
Muchos asumen que construir un heap desde un arreglo de $N$ elementos cuesta $O(N \log N)$ (insertando uno por uno). Pero existe un método $O(N)$.
**Algoritmo:** Empezamos desde el último nodo interno hacia arriba y aplicamos `sift-down` en cada uno.
**Deducción Matemática:**
1. En un heap de altura $H$, hay $2^{H-h}$ nodos en la altura $h$.
2. El costo de `sift-down` en la altura $h$ es $O(h)$.
3. Costo Total: $\sum_{h=0}^{H} h \cdot 2^{H-h} = N \cdot \sum_{h=0}^{H} \frac{h}{2^h}$.
4. Como la serie infinita $\sum \frac{h}{2^h}$ converge a 2, el costo total es $O(N)$.
**Importancia:** Esta es la razón por la que el **Heapsort** es tan eficiente en su fase inicial de preparación.

---

## 4. VARIANTES AVANZADAS: HEAPS PARA LA ESCALA INDUSTRIAL

### 4.1 d-ary Heaps: Minimizando la Lectura de RAM
En lugar de 2 hijos, cada nodo tiene $d$ hijos.
- **Ventaja:** La altura del árbol se reduce a $\log_d N$.
- **Costo:** El `sift-down` ahora debe comparar entre $d$ hijos, no 2.
- **Uso:** En sistemas donde el costo dominante es el **Cache Miss** (ej. grandes bases de datos en disco), usar un 4-ary o 8-ary heap permite traer toda la vecindad de un nodo en una sola ráfaga del bus de memoria.

### 4.2 Heaps Segmentados y Pool de Nodos
Para aplicaciones de tiempo real, crear objetos es caro. Usamos un **Pool de Nodos** pre-alocados para evitar la recolección de basura. Implementamos el heap sobre una serie de arreglos de 4KB (tamaño de página del SO) para maximizar el throughput del TLB de la CPU.

## 5. CONCURRENCIA Y HARDWARE EN COLAS DE PRIORIDAD

El heap es la estructura más difícil de paralelizar porque la raíz es un **Hot Spot** (punto de contención único).

### 5.1 El Problema del Bottleneck de la Raíz
En un sistema multinúcleo, todos los hilos que quieren extraer el mínimo deben tocar la posición `array[0]`.
- **Cache Contention:** La línea de caché de la raíz rebota de núcleo en núcleo (Cache Line Bouncing).
- **Solución:** Se usan colas de prioridad distribuidas o **Skip Lists** que permiten operaciones concurrentes en distintas regiones de la estructura sin bloquear la raíz.

### 5.2 Fibonacci Heaps: El Techo Teórico
Utilizados en algoritmos de grafos masivos (como Dijkstra en mapas de ciudades enteras).
- **Decrease Key:** Es la operación estrella. Permite bajar la prioridad de un elemento en $O(1)$ amortizado. 
- **Estructura:** No es un árbol, es una colección de árboles unidos por una lista enlazada circular. 
- **Trade-off:** Son masivamente complejos de implementar y tienen constantes ocultas altas. Solo se usan cuando el número de operaciones `decreaseKey` supera por mucho a las de `deleteMin`.

---

## 6. APLICACIONES INDUSTRIALES DE ALTO IMPACTO

### 6.1 Compresión de Huffman
La base de los formatos `.zip`, `JPEG` y `MP3`. 
1. Contamos las frecuencias de cada símbolo.
2. Metemos todo en una cola de prioridad.
3. Sacamos los dos más bajos, creamos un nodo padre con la suma de frecuencias y lo re-insertamos.
**Mecánica:** La cola de prioridad asegura que los símbolos más raros queden en lo profundo del árbol de códigos, logrando una compresión óptima de la información.

### 6.2 Simulación de Eventos Discretos
Usado en el diseño de aeropuertos y microprocesadores. 
- La cola de prioridad guarda "Eventos" ordenados por su **tiempo de ocurrencia**.
- El simulador saca el evento más próximo, avanza el reloj del sistema a ese instante, y genera nuevos eventos (ej. "el avión aterrizó" genera "el avión pide pista").

## 7. Laboratorio de Ejercicios: Maestría Técnica (1-20)

### Ejercicio 1: Implementación de un Heap Ternario (3-ary Heap)
**Consigna:** Modificá las fórmulas de padre/hijo para un heap donde cada nodo tiene 3 hijos.

**Resolución Detallada:**
1. **Hijos de i:** $3i + 1, 3i + 2, 3i + 3$.
2. **Padre de i:** $(i - 1) / 3$.
3. **Análisis:** Un heap 3-ary reduce la altura en un factor de $\log_2 3 \approx 1.58$. Esto acelera la operación de `push` (menos niveles que subir) pero enlentece la de `pop` (hay que comparar contra 3 hijos en lugar de 2). Es ideal para sistemas donde hay muchas más inserciones que extracciones.

### Ejercicio 2: K-Way Merge para Archivos de 1TB
**Consigna:** Tenés 1000 archivos ordenados. ¿Cómo los unís en uno solo usando solo 8GB de RAM?

**Resolución Detallada:**
Usamos una cola de prioridad de tamaño 1000.
1. Leemos el primer elemento de cada uno de los 1000 archivos y lo metemos en la cola de prioridad (junto con el ID del archivo).
2. Extraemos el mínimo de la cola y lo escribimos en el archivo de salida.
3. Leemos el siguiente elemento del archivo que acabamos de usar y lo metemos en la cola.
**Costo:** $O(N \log 1000)$, donde $N$ es el total de elementos. Es la técnica base para los motores de bases de datos masivas.

### Ejercicio 3: Mediana en un Stream Infinito
**Consigna:** Hallá la mediana de un flujo de datos que nunca termina usando dos heaps.

**Resolución Detallada:**
1. Mantenemos un **Max-Heap** para la mitad menor de los datos y un **Min-Heap** para la mitad mayor.
2. Al llegar un nuevo número, lo insertamos en uno de los heaps y re-balanceamos para que la diferencia de tamaños sea a lo sumo 1.
3. La mediana es la raíz del heap más grande (o el promedio de ambas raíces).
**Complejidad:** $O(\log N)$ por inserción. Es la forma más eficiente de calcular estadísticas móviles en tiempo real.

### Ejercicio 4: Heapsort In-place
**Consigna:** Ordená un arreglo de $N$ elementos usando Heapsort sin usar memoria extra.

### Ejercicio 5: Detección de Invariante de Heap
**Consigna:** Escribí una función que valide si un arreglo dado representa un min-heap válido en $O(N)$.

### Ejercicio 6: Heaps con Prioridades Duplicadas
**Consigna:** Analizá qué pasa con la estabilidad de los elementos si muchos tienen la misma prioridad.

### Ejercicio 7: Dijkstra con Decrease-Key
**Consigna:** Simulá la actualización de distancia de un vértice en Dijkstra usando un heap binario.

### Ejercicio 8: Huffman Tree Trace
**Consigna:** Dada la frecuencia `[A:10, B:50, C:2, D:15]`, dibujá el árbol de Huffman resultante.

### Ejercicio 9: K-th Largest Element
**Consigna:** Encontrá el k-ésimo elemento más grande de un arreglo desordenado en $O(N \log K)$.

### Ejercicio 10: Task Scheduler with Starvation
**Consigna:** Diseñá una cola de prioridad que evite que una tarea de baja prioridad nunca sea atendida (Aging).

### Ejercicio 11: Build-Heap vs Insert
**Consigna:** Realizá un benchmark de JMH comparando `buildHeap` contra $N$ inserciones individuales.

### Ejercicio 12: Memory Layout of D-ary Heap
**Consigna:** Calculá cuántas líneas de caché se cargan al hacer un `sift-down` en un 8-ary heap.

### Ejercicio 13: Lazy Removal in Heaps
**Consigna:** Implementá el borrado de un elemento arbitrario marcándolo como "borrado" y sacándolo recién cuando llegue a la raíz.

### Ejercicio 14: Pairing Heap Trace
**Consigna:** Realizá el seguimiento de tres inserciones y un `deleteMin` en un Pairing Heap.

### Ejercicio 15: RTOS Job Scheduling
**Consigna:** Simulá la cola de listos de un SO de tiempo real donde la prioridad es el `deadline`.

### Ejercicio 16: Binary Heap with Pooling
**Consigna:** Implementá un heap que reuse los objetos de los nodos para minimizar el GC.

### Ejercicio 17: Flattening Binary Heaps
**Consigna:** Dada un arreglo desordenado, mostrá los pasos de `heapify` para convertirlo en heap.

### Ejercicio 18: Priority Queue with Multi-threading
**Consigna:** Analizá el impacto de usar un `ReentrantLock` global en una cola de prioridad con 100 hilos.

### Ejercicio 19: Cache-friendly Sift-down
**Consigna:** Optimizá el `sift-down` para que lea los dos hijos en una sola ráfaga de memoria.

### Ejercicio 20: Huffman Coding Throughput
**Consigna:** Calculá cuántos bits ahorrás al comprimir un texto de 1MB usando la cola de prioridad de frecuencias.

---

## 8. MATEMÁTICA DE HEAPS: ANÁLISIS DE CASOS Y PROBABILIDAD

El análisis de un heap no termina en el $O(\log n)$. Hay sutilezas estadísticas que dictan su performance real.

### 8.1 El Caso Promedio de Inserción
Aunque el peor caso de `insert` es $O(\log n)$, en la práctica es casi $O(1)$. 
- **La Razón:** En un heap binario, el 50% de los nodos son hojas y el 25% son padres de hojas. Al insertar un valor aleatorio, la probabilidad de que "suba" muchos niveles es exponencialmente baja. 
- **Estadística:** En promedio, un elemento solo sube 1.6 niveles tras la inserción. Esto hace que el heap sea masivamente más rápido para insertar que un árbol de búsqueda balanceado (AVL/RB).

### 8.2 El Caso Peor de Extracción
A diferencia de la inserción, el `deleteMin` siempre es $O(\log n)$. Al poner el último elemento (que es grande) en la raíz, estamos garantizando que el elemento tendrá que "hundirse" casi hasta el fondo del árbol en casi todos los casos.

---

## 9. COLAS DE PRIORIDAD EN EL KERNEL: EL PLANIFICADOR CFS

El **Completely Fair Scheduler (CFS)** de Linux es el encargado de decidir qué proceso usa la CPU.
1. **Virtual Runtime:** Cada proceso tiene un contador de cuánto tiempo de CPU usó.
2. **Prioridad:** El CFS quiere ejecutar siempre al proceso que menos tiempo usó (el más "hambriento").
3. **Estructura:** Originalmente usaba colas de prioridad basadas en arreglos, pero para manejar miles de hilos con prioridades dinámicas (nice values), el kernel moderno utiliza un **Red-Black Tree** que actúa como una cola de prioridad.
**Por qué no un Heap:** Porque el kernel necesita realizar la operación de **actualizar prioridad** (decrease-key) constantemente. En un heap binario basado en arreglo, encontrar el nodo para bajarle la prioridad es $O(n)$, mientras que en un RB-Tree es $O(\log n)$.

---

## 10. COLAS DE PRIORIDAD DISTRIBUIDAS Y SISTEMAS CLOUD

En sistemas distribuidos, mantener una cola de prioridad global es un desafío de escalabilidad.

### 10.1 Heaps Jerárquicos
Se dividen los datos en múltiples colas de prioridad locales (en diferentes servidores). 
1. Cada servidor mantiene su propio heap.
2. Un servidor central (Master) mantiene un heap con los mínimos de cada heap local.
**Costo:** Extraer el mínimo global es $O(\log \text{Servers})$, pero permite manejar billones de tareas en paralelo.

### 10.2 Priority Queues en Cloud (ej. AWS SQS FIFO)
En el cloud, la prioridad a menudo se gestiona mediante el uso de múltiples colas con diferentes SLAs, o mediante metadatos en los mensajes que el consumidor utiliza para filtrar.

---

## 11. GLOSARIO TÉCNICO DE PRIORIDAD (Ampliación Maestra)

1. **d-ary Heap:** Generalización del heap binario con $d$ hijos, optimizado para sistemas de almacenamiento masivo.
2. **Decrease-Key:** Operación fundamental en algoritmos de grafos que actualiza la prioridad de un elemento existente.
3. **Heap-Inplace:** Capacidad de transformar un arreglo cualquiera en un heap sin usar memoria auxiliar.
4. **Implicit Pointer:** En un heap basado en arreglo, la relación padre-hijo no se guarda, se calcula aritméticamente.
5. **Level-Order Traversal:** Recorrido que visita los nodos nivel por nivel, coincidiendo con el orden físico del arreglo del heap.
6. **Partial Order:** Relación matemática de los heaps donde solo se garantiza el orden entre padre e hijo, no entre hermanos.
7. **Priority Inversion:** Problema en multihilo donde una tarea de baja prioridad bloquea un recurso necesitado por una de alta prioridad.
8. **Sift-Down (Heapify):** Algoritmo que restaura la propiedad de heap moviendo un elemento hacia las hojas.

---

## BIBLIOGRAFÍA Y LECTURA RECOMENDADA

1. **"Algorithms"** (Sedgewick & Wayne): Para las mejores visualizaciones de heaps y heapsort.
2. **"Introduction to Algorithms"** (CLRS): La demostración formal de Build-Heap.
3. **"Modern Operating Systems"** (Tanenbaum): Para el uso de colas de prioridad en el scheduler.

## 12. Laboratorio de Ejercicios: Maestría Técnica (1-20) (Ampliación)

### Ejercicio 1: Implementación de un Heap Ternario (3-ary Heap) (Ampliación)
**Resolución Detallada:**
1. **Fórmulas de Índice:**
   - Para un nodo en la posición $i$, sus hijos están en $3i+1, 3i+2, 3i+3$.
   - El padre está en $\lfloor (i-1)/3 \rfloor$.
2. **Impacto en la Altura:** La altura de un heap binario es $\log_2 N$. Para un heap ternario es $\log_3 N$. Como $\log_3 N = \frac{\ln N}{\ln 3}$ y $\log_2 N = \frac{\ln N}{\ln 2}$, la altura se reduce en un factor de $\frac{\ln 3}{\ln 2} \approx 1.58$.
3. **Costo de las Operaciones:**
   - `insert`: Mejora significativamente. Al haber menos niveles, el número máximo de comparaciones e intercambios durante el `sift-up` es menor.
   - `deleteMin`: Empeora. Al hundir el elemento, en cada nivel tenés que comparar con **tres** hijos para encontrar el mínimo, aumentando el trabajo por nivel.
**Hardware:** Un heap d-ary con $d=4$ u $8$ es masivamente más rápido en sistemas donde el arreglo no cabe en la caché L2, porque cada nodo "trae" más información útil en la misma línea de caché de 64 bytes.

### Ejercicio 2: K-Way Merge para Big Data (Mecánica Industrial)
**Consigna:** Tenés 1000 archivos de 1GB cada uno. ¿Cómo los unís en un solo archivo de 1TB usando 8GB de RAM?

**Resolución Detallada:**
1. **Fase de Inicialización:**
   - Creamos un objeto `FileBuffer` por cada archivo que cargue bloques de 4MB (4MB $\times$ 1000 = 4GB, entra perfecto en RAM).
   - Creamos un Min-Heap de objetos `Entry { value, fileId }` de tamaño 1000.
   - Cargamos el primer valor de cada archivo en el heap ($O(1000 \log 1000)$).
2. **Fase de Streaming:**
   - Mientras el heap no esté vacío:
     a. `min = heap.pop()`.
     b. Escribimos `min.value` al buffer de salida.
     c. Leemos el siguiente valor del archivo `min.fileId`.
     d. Si el archivo no terminó, lo insertamos en el heap ($O(\log 1000)$).
3. **Hardware:** El cuello de botella es el **I/O de Disco**. Al usar una cola de prioridad, estamos procesando los datos tan rápido como los cabezales del disco (o el bus NVMe) pueden entregarlos. El costo de la estructura de datos es despreciable ($O(N \log K)$) frente a la latencia de almacenamiento.

### Ejercicio 3: Mediana en un Stream Infinito (Algoritmo de los Dos Heaps)
**Consigna:** Hallá la mediana de un flujo de datos que nunca termina en tiempo real.

**Resolución Detallada:**
1. **Dinamismo:** Mantener un arreglo ordenado costaría $O(N)$ por inserción, lo cual es inviable para streams.
2. **Estructura Dual:**
   - `lowHeap` (Max-Heap): Guarda la mitad menor de los números vistos.
   - `highHeap` (Min-Heap): Guarda la mitad mayor de los números vistos.
3. **Lógica de Inserción:**
   - Si el número es menor que la raíz de `lowHeap`, va a `lowHeap`. De lo contrario, a `highHeap`.
   - **Rebalanceo:** Si la diferencia de tamaños es $> 1$, sacamos la raíz del heap más grande y la insertamos en el otro.
4. **Cálculo de la Mediana:**
   - Si los tamaños son iguales: `(low.max() + high.min()) / 2`.
   - Si son distintos: La raíz del heap que tiene más elementos.
**Impacto:** Este algoritmo es $O(\log N)$ y permite procesar miles de eventos por segundo en sistemas de análisis de bolsa o monitoreo de tráfico de red.

### Ejercicio 12: Memory Layout of D-ary Heap (Análisis de Cache Lines)
**Consigna:** Calculá cuántas líneas de caché se cargan al hacer un `sift-down` en un 8-ary heap.

**Resolución Detallada:**
1. **Línea de Caché:** 64 bytes.
2. **Datos:** Un `long` o un puntero de 64 bits (8 bytes).
3. **Cálculo:** En una línea de caché entran exactamente 8 elementos.
4. **Mecánica:** En un 8-ary heap, los 8 hijos de un nodo $i$ están en las posiciones $8i+1 \dots 8i+8$. Estos 8 hijos **viven en la misma línea de caché** o a lo sumo en dos. 
**Resultado:** Al comparar un padre con sus 8 hijos, la CPU solo realiza **una lectura de RAM**. En un heap binario, los dos hijos están pegados, pero para ver 8 descendientes tendrías que bajar 3 niveles, disparando potencialmente 3 lecturas de RAM distintas. El 8-ary heap es la estructura de prioridad óptima para la arquitectura x86 moderna.

### Ejercicio 20: Huffman Coding Throughput (Análisis de Compresión)
**Consigna:** Calculá el ahorro de bits usando una cola de prioridad para generar un árbol de Huffman.

**Resolución Detallada:**
1. **Frecuencias:** Sea el texto "AAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBCCDDDDDDDDDDDDDDD".
2. **Heap Trace:**
   - `[C:2, D:15, A:10, B:50]`
   - Saco C y A (los dos menores). Padre CA:12. Re-inserto.
   - `[CA:12, D:15, B:50]`
   - Saco CA y D. Padre CAD:27. Re-inserto.
   - `[CAD:27, B:50]`
   - Saco CAD y B. Raíz: 77.
3. **Longitud de Código:** B tendrá un código de 1 bit (0), mientras que C tendrá un código de 3 bits (110).
**Conclusión:** La cola de prioridad garantiza que el código de longitud mínima se asigne al símbolo de frecuencia máxima, minimizando la entropía del mensaje y maximizando el ahorro de ancho de banda de red.

---

## 12. TABLA COMPARATIVA GIGANTE: LAS ESTRUCTURAS DE PRIORIDAD

¿Qué variante elegir según tu problema de ingeniería?

| Estructura | findMin | insert | deleteMin | decreaseKey | Memoria |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **Arreglo Desordenado** | $O(n)$ | $O(1)$ | $O(n)$ | $O(1)$ | Mínima |
| **Arreglo Ordenado** | $O(1)$ | $O(n)$ | $O(n)$ | $O(n)$ | Mínima |
| **Heap Binario** | $O(1)$ | $O(\log n)^*$ | $O(\log n)$ | $O(n)$ | Mínima |
| **d-ary Heap** | $O(1)$ | $O(\log_d n)^*$ | $O(d \log_d n)$ | $O(n)$ | Mínima |
| **Fibonacci Heap** | $O(1)$ | $O(1)$ | $O(\log n)^†$ | $O(1)^†$ | Masiva |
| **Pairing Heap** | $O(1)$ | $O(1)$ | $O(\log n)^†$ | $O(2^{2\sqrt{\log \log n}})^†$ | Media |

\* *Promedio $O(1)$.*
† *Costo Amortizado.*

### 12.1 La Realidad de la Práctica
Aunque el **Fibonacci Heap** es el rey de los libros de texto por su $O(1)$ en `decreaseKey`, en el mundo real casi nunca se usa. 
- **La Razón:** Tiene constantes ocultas enormes y una localidad de caché desastrosa. 
- **La Alternativa:** El **Pairing Heap** ofrece una performance similar en la práctica con una implementación masivamente más simple y amigable con el hardware.

---

## 13. ESTUDIO DE CASO: EL PLANIFICADOR DE KUBERNETES (KUBE-SCHEDULER)

Kubernetes debe decidir en qué nodo de un cluster de miles de máquinas debe correr un nuevo contenedor (Pod).
1. **Priorización:** El scheduler asigna una puntuación a cada nodo basándose en recursos libres, afinidad y políticas.
2. **Cola de Prioridad:** Los nodos se ordenan en una cola de prioridad interna. 
3. **Escalamiento:** Para no bloquearse comparando miles de nodos, el scheduler utiliza una técnica de **Sampling** (muestreo). En lugar de buscar el "mejor absoluto" en todo el cluster (lo cual saturaría la cola de prioridad), busca el "mejor entre los primeros $N$" que cumplan un umbral de calidad.
**Conclusión:** La cola de prioridad no solo es un algoritmo; es el mecanismo que mantiene el equilibrio de carga en la infraestructura cloud del planeta.

---

## 14. FILOSOFÍA DE LA IMPORTANCIA: EL FIN DE LA IGUALDAD

La cola de prioridad es la estructura que rompe la justicia del tiempo.
1. **La Meritocracia de los Datos:** No todos los elementos son iguales. Algunos "pesan" más que otros.
2. **El Compromiso (Trade-off):** Ganamos urgencia a costa de desorden local. 
3. **El Orden Parcial:** El heap nos enseña que para tomar la mejor decisión (findMin), no necesitamos conocer el orden relativo de los demás elementos. La ignorancia estructurada es lo que nos da velocidad.

---

## 15. HEAPS EN VIDEOJUEGOS: ALGORITMO A* Y NAVEGACIÓN

En un juego como *Age of Empires* o *Starcraft*, cientos de unidades deben encontrar el camino más corto esquivando obstáculos.
- **Open Set:** El algoritmo mantiene un conjunto de nodos candidatos. Siempre debe expandir el nodo con menor "costo estimado" ($f = g + h$).
- **La Cola de Prioridad:** Es el motor del A*. Si usás un arreglo desordenado, el juego se cuelga. Usar un heap binario permite que las unidades reaccionen en milisegundos.
**Optimización de Gráficos:** Los motores de rendering usan colas de prioridad para procesar primero los objetos que están más cerca de la cámara (Z-buffer), optimizando el uso de la VRAM.

---

## GLOSARIO TÉCNICO DE PRIORIDAD (Ampliación Maestra)

1. **Amortized Analysis:** Técnica para calcular el costo promedio de operaciones complejas en heaps persistentes.
2. **Binary Heap:** Implementación clásica basada en un árbol binario casi completo representado en un arreglo.
3. **Cache Line Bouncing (Heaps):** Contención de hardware en la raíz del heap cuando múltiples hilos intentan extraer el mínimo.
4. **Complete Binary Tree:** Árbol donde todos los niveles están llenos excepto el último, que se llena de izquierda a derecha.
5. **Dijkstra Optimization:** Uso de Fibonacci Heaps para bajar la complejidad de búsqueda de caminos de $O(E \log V)$ a $O(E + V \log V)$.
6. **Heapsort:** Algoritmo de ordenamiento $O(n \log n)$ que no requiere memoria extra y aprovecha la invariante de heap.
7. **Implicit Data Structure:** Estructura que guarda sus relaciones en la posición de los elementos, no en punteros explícitos.
8. **Leaf-to-Root Path:** El único camino que debe recorrer un elemento durante el `sift-up`, garantizando un costo logarítmico.
9. **Min-Max Heap:** Estructura especializada que permite extraer el mínimo y el máximo en $O(1)$ simultáneamente.
10. **Priority Starvation:** Riesgo de que tareas de baja prioridad nunca se ejecuten; se soluciona con técnicas de **Aging** (envejecimiento).
11. **Selection Problem:** Problema de hallar el k-ésimo elemento mayor; resuelto de forma óptima con una cola de prioridad de tamaño $k$.
12. **Soft Heap:** Variante que permite corromper una fracción de los datos para lograr tiempos constantes en operaciones de grafos.
13. **Tournament Tree:** Otra forma de implementar colas de prioridad basada en competencias binarias entre elementos.
14. **Unbounded Heap:** Implementación basada en nodos enlazados o arreglos dinámicos que puede crecer sin límite fijo de RAM.
15. **Vectorized Heapify:** Optimización moderna que usa instrucciones SIMD de la CPU para procesar múltiples niveles del heap en paralelo.

---

## Próximo paso

Habiendo dominado el orden de la importancia y el rigor de los heaps, es momento de entrar en la estructura que nos permite organizar la información en el disco: los [Árboles B](../arboles/arboles_b.md).

---

## 16. COLAS DE PRIORIDAD EN LA ERA DE LA IA: BEAM SEARCH

Si usás ChatGPT o cualquier LLM, estás usando colas de prioridad indirectamente a través del algoritmo **Beam Search**.
1. **El Problema:** Al generar texto, el modelo calcula probabilidades para la siguiente palabra. Explorar todas las ramas posibles es un problema exponencial.
2. **La Solución:** En cada paso, mantenemos solo los $K$ candidatos más probables usando una cola de prioridad de tamaño $K$.
3. **Mecánica:** Descartamos las ramas de baja probabilidad de forma voraz. 
**Importancia:** La cola de prioridad es lo que permite que la IA genere respuestas coherentes en tiempo real, concentrando el cómputo únicamente en los caminos más prometedores de la secuencia.

---

## 17. LA FÍSICA DE LA PRIORIDAD: BARRERAS DE MEMORIA Y ATOMICIDAD

Implementar un heap concurrente requiere entender cómo la CPU ve el arreglo del heap.
1. **Coherencia de la Raíz:** El nodo `array[0]` es el más disputado. 
2. **Memory Fences:** Cada vez que un hilo termina un `sift-up`, debe ejecutar una barrera de memoria (`StoreStore`) para asegurar que el cambio en la prioridad sea visible para los consumidores en otros núcleos.
3. **Atomic Heaps:** Investigaciones recientes usan deques segmentados para implementar colas de prioridad que no requieren un lock global en la raíz, permitiendo que un procesador de 64 núcleos procese millones de eventos por segundo sin colapsar el bus de datos.

---

## 18. ESTUDIO DE CASO: EL DEADLINE SCHEDULER DE LINUX

El scheduler de E/S (disco) de Linux utiliza colas de prioridad para garantizar que ninguna petición se "oxide" (Starvation).
- **Sorting:** Ordena las peticiones por posición física en el disco (sector) para maximizar el throughput secuencial.
- **Deadline:** Cada petición tiene una cola de prioridad paralela basada en el tiempo de expiración.
**Lógica:** Si una petición de lectura llega a su deadline (ej. 500ms), el sistema interrumpe el ordenamiento físico y atiende esa petición inmediatamente. Es el ejemplo perfecto de cómo una **Prioridad Temporal** protege la interactividad del sistema frente a la eficiencia bruta del hardware.

---

## 19. TIPS PARA EL EXAMEN FINAL: DOMINANDO LA PRIORIDAD

Si tenés que defender tu conocimiento sobre heaps, no podés fallar en estos puntos:

- **Build-Heap $O(N)$:** Es la pregunta trampa favorita. Explicá por qué hundir es más barato que subir (porque la mayoría de los nodos están cerca de las hojas).
- **Forma vs Orden:** Explicá que el heap es un árbol binario casi completo (forma) que respeta una relación de orden parcial (padre $\leq$ hijo).
- **Arreglo Implícito:** Mencioná las fórmulas $2i+1$ y $2i+2$. Demostrá que entendés que un heap no necesita punteros para ser un árbol.

---

## Próximo paso

### Ejercicio 5: Detección de Invariante de Heap (Análisis de Throughput)
**Resolución Detallada:**
1. **Lazo Lineal:** Recorremos el arreglo desde el índice 0 hasta $\lfloor (n-2)/2 \rfloor$.
2. **Comparación:** Para cada $i$, verificamos `arr[i] <= arr[2i+1]` y `arr[i] <= arr[2i+2]`.
3. **Hardware:** Este es el patrón de acceso que más ama la CPU. El prefetcher puede cargar las Cache Lines de los hijos mucho antes de que las compares.
**Throughput:** Una CPU moderna puede validar un heap de 1GB en menos de 0.1 segundos. Es la técnica base para los **Auditores de Memoria** en sistemas de seguridad que deben validar la integridad de las estructuras de datos tras una sospecha de ataque de desbordamiento.

### Ejercicio 6: Heaps con Prioridades Duplicadas (Ampliación)
**Consigna:** Analizá qué pasa con la estabilidad.

**Resolución Detallada:**
En un heap binario, si dos elementos tienen prioridad 5, su orden de salida es impredecible.
- **Solución de Estabilidad:** Guardamos una tupla `{priority, timestamp}`. Al comparar, si las prioridades son iguales, el timestamp más bajo "gana".
- **Impacto:** Estamos convirtiendo una cola de prioridad en una **Cola de Prioridad Estable**. Esto es vital en sistemas de transacciones financieras donde el orden de llegada entre ofertas iguales debe respetarse por ley.
- **Hardware:** La comparación ahora requiere leer dos campos, duplicando el trabajo de la ALU y aumentando el footprint de memoria en un 50%.

### Ejercicio 8: Huffman Tree Trace (Ampliación)
**Resolución Detallada:**
1. **Pila de Heaps:** El algoritmo de Huffman es un proceso de "fusión voraz". 
2. **Entropía:** Al usar una cola de prioridad, estamos minimizando la **Entropía de Shannon** de la secuencia de salida.
3. **Visualización:** El árbol resultante es un Árbol Binario donde cada camino desde la raíz es una secuencia de bits (0=Izquierda, 1=Derecha). 
**Hardware:** El árbol de Huffman es masivamente eficiente para los **Decodificadores SIMD**, ya que los prefijos son únicos y la CPU puede usar tablas de búsqueda (LUTs) pre-calculadas en la L1 para traducir bits a símbolos instantáneamente.

### Ejercicio 10: Task Scheduler with Starvation (Ampliación)
**Resolución Detallada:**
Para evitar que una tarea de prioridad 1000 nunca corra (Starvation):
1. **Aging:** Cada $N$ milisegundos que una tarea pasa en el heap, le bajamos su valor de prioridad (aumentamos su urgencia).
2. **Reubicación:** El cambio de prioridad requiere un `sift-up`.
**Hardware:** El aging masivo es costoso porque invalida la estructura del heap. En la práctica, se usan múltiples colas de prioridad por "clases de servicio" para minimizar el movimiento de nodos.

### Ejercicio 16: Binary Heap with Pooling (Optimización de Latencia)
**Resolución Detallada:**
1. **El Problema:** `new Node()` en cada inserción ensucia el heap y dispara el GC.
2. **La Solución:** Pre-alocamos un arreglo de objetos `Node` y los manejamos con una **Free List**.
3. **Mecánica:** Al insertar, tomamos un nodo del pool. Al borrar, lo devolvemos.
**Resultado:** Latencia p99 constante. Esta es la diferencia entre un sistema que "funciona" y un sistema de **Misión Crítica** para control industrial o aeroespacial.

---

## 22. COLAS DE PRIORIDAD EN EL CORAZÓN DEL KERNEL: INTERRUPT PRIORITIES

El hardware de tu PC tiene docenas de dispositivos que quieren atención (teclado, disco, red, timer).
1. **APIC (Advanced Programmable Interrupt Controller):** Es un chip que funciona como una cola de prioridad de hardware.
2. **Niveles de Prioridad (IPL):** Cada interrupción tiene un nivel. Una interrupción de "Fallo de Energía" tiene más prioridad que una de "Movimiento de Mouse".
3. **Mecánica:** Si el procesador está ejecutando una tarea de nivel 5 y llega una interrupción de nivel 10, el hardware suspende la tarea actual inmediatamente. Es la cola de prioridad más rápida del planeta, implementada directamente en las compuertas lógicas del silicio.

---

## 23. ANÁLISIS DE P99 Y JITTER: EL IMPACTO DE LOS HEAPS MASIVOS

Cuando usás un heap para gestionar millones de objetos (ej. en un motor de simulación):
1. **La Pausa de Compactación:** El heap binario en arreglo es contiguo, pero si guardás objetos, el GC debe seguirlos todos. Un heap de 100 millones de objetos puede causar pausas de segundos.
2. **La Alternativa Off-Heap:** Los sistemas de alta performance usan **Memory Segments** (Project Panama) para guardar el arreglo del heap fuera del control del GC. 
**Resultado:** Podés tener un heap de 1 Terabyte con latencias de acceso de nanosegundos y pausas de GC de cero. Es el secreto de los motores de Big Data como **Apache Spark** o **Flink**.

---

## 24. DEMOSTRACIÓN FORMAL: POR QUÉ BUILD-HEAP ES $O(N)$

Muchos estudiantes se confunden y piensan que es $O(N \log N)$. Hagamos la cuenta final.
1. Sea un árbol binario completo de altura $H$.
2. El número de nodos es $N = 2^{H+1} - 1$.
3. Un nodo a altura $h$ (siendo 0 la hoja) tarda a lo sumo $h$ pasos en hundirse.
4. Hay $2^{H-h}$ nodos en la altura $h$.
5. El costo total es $S = \sum_{h=1}^{H} h \cdot 2^{H-h}$.
6. Multiplicamos por 2: $2S = \sum_{h=1}^{H} h \cdot 2^{H-h+1}$.
7. Restamos las series (Serie Aritmético-Geométrica): $S = \sum_{h=0}^{H-1} 2^{H-h} - H$.
8. El resultado es $S = 2^{H+1} - H - 2 \approx N - \log N$.
**Q.E.D.** El costo es lineal respecto a $N$. Esta es la prueba de que el ordenamiento parcial es masivamente más barato que el ordenamiento total.

---

## GLOSARIO ENCICLOPÉDICO DE PRIORIDAD (Ampliación Final)

1. **Aging (Envejecimiento):** Técnica para subir la prioridad de un elemento a medida que pasa el tiempo para evitar la inanición.
2. **Binary Heap:** Árbol binario casi completo que cumple la propiedad de que cada padre es menor que sus hijos.
3. **Cache Line Bouncing:** Degradación del rendimiento cuando múltiples hilos intentan modificar la raíz del heap.
4. **Deadline-Driven Scheduling:** Política de prioridad donde el elemento más urgente es el que tiene la fecha de entrega más cercana.
5. **Decrease-Key:** Operación que permite bajar el valor de prioridad de un elemento, obligándolo a subir en el árbol.
6. **d-ary Heap:** Heap donde cada nodo tiene $d$ hijos, optimizado para reducir la altura y mejorar el uso de la caché.
7. **Fibonacci Heap:** Estructura compleja que permite `decrease-key` en tiempo constante amortizado.
8. **Implicit Pointer:** Puntero que no existe físicamente; se calcula mediante aritmética de índices en un arreglo.
9. **Leaf-to-Root Path:** El único camino que puede recorrer un elemento durante el ascenso, garantizando un costo logarítmico.
10. **Min-Max Heap:** Estructura que permite extraer el mínimo y el máximo en $O(1)$ usando niveles alternados.
11. **Partial Order:** Relación matemática donde solo algunos pares de elementos son comparables (en el heap, solo padre con hijos).
12. **Priority Inversion:** Error de diseño donde una tarea de baja prioridad frena a una de alta prioridad.
13. **Selection Algorithm:** Problema de hallar el k-ésimo elemento; se resuelve con un heap de tamaño $k$ en tiempo lineal.
14. **Sift-Down (Heapify):** El acto de hundir un elemento en el árbol para restaurar la invariante de orden.
15. **Soft Heap:** Variante probabilística del heap que permite ciertos errores para lograr velocidad constante.

---

## Próximo paso

Habiendo dominado el orden de la importancia y el rigor de los heaps, es momento de entrar en la estructura que nos permite organizar la información en el disco: los [Árboles B](../arboles/arboles_b.md).

### Ejercicio 11: Build-Heap vs Insert (Análisis Empírico)
**Consigna:** Realizá un benchmark comparando ambos métodos de construcción.

**Resolución Detallada:**
1. **Insert Masivo:** Insertar $N$ elementos uno por uno cuesta $O(N \log N)$. Al graficarlo, verás una curva que crece más rápido que la lineal.
2. **Build-Heap:** Procesar el arreglo desde el último padre cuesta $O(N)$. La curva es una recta perfecta.
3. **Hardware:** `buildHeap` es más rápido no solo por la complejidad asintótica, sino por la **Localidad Temporal**. Al procesar el arreglo de atrás hacia adelante, estás trabajando sobre sub-árboles pequeños que caben enteros en la caché L1. Las inserciones individuales, en cambio, saltan por todo el heap, causando fallos de caché en cada nivel.
**Throughput:** Para un arreglo de 100M de elementos, `buildHeap` puede ser hasta 10 veces más rápido que las inserciones secuenciales.

### Ejercicio 14: Pairing Heap Trace (Algoritmos Autonómicos)
**Consigna:** Realizá el seguimiento de tres inserciones y un `deleteMin`.

**Resolución Detallada:**
1. **Estructura:** El Pairing Heap es un árbol multi-camino. No mantiene balanceo rígido.
2. **Insert:** Simplemente "colgás" el nuevo árbol debajo de la raíz menor. $O(1)$.
3. **DeleteMin:** Esta es la operación inteligente. Al borrar la raíz, quedan muchos sub-árboles huérfanos. 
4. **Fase de Fusión:** Se unen de a pares (First Pass) y luego de atrás hacia adelante (Second Pass).
**Análisis:** Esta fase de fusión es la que realiza el rebalanceo de forma "perezosa". Es un algoritmo **Self-Adjusting** que se adapta al patrón de datos del usuario, similar a como un Splay Tree se adapta a las búsquedas.

### Ejercicio 15: RTOS Job Scheduling (Prioridad Estricta)
**Consigna:** Simulá la cola de listos de un SO de tiempo real donde la prioridad es el `deadline`.

**Resolución Detallada:**
1. **Tarea:** `{id: 1, deadline: 50ms}`, `{id: 2, deadline: 10ms}`.
2. **Cola:** El Max-Heap (o Min-Heap invertido) asegura que la tarea con el deadline más cercano esté siempre en la raíz.
3. **Preemption:** Si llega una tarea nueva con un deadline menor al de la tarea actual, el hardware dispara una interrupción. El scheduler inserta la tarea en el heap y realiza un cambio de contexto.
**Hardware:** En un RTOS, el heap suele estar en **SRAM** (memoria estática ultra-rápida) para garantizar que la latencia de inserción sea determinística y no dependa de los caprichos del controlador de DDR5.

### Ejercicio 19: Cache-friendly Sift-down (Optimización de Memoria)
**Consigna:** Optimizá el `sift-down` para que lea los dos hijos en una sola ráfaga.

**Resolución Detallada:**
1. **El Problema:** Al comparar el padre con el hijo izquierdo y luego con el derecho, disparás dos lecturas de memoria.
2. **La Solución:** Leé una línea de caché entera (64 bytes). En un heap de `int`, esto trae 16 elementos.
3. **Mecánica:** Si el índice es $i$, los hijos están en $2i+1$ y $2i+2$. Estos dos hijos siempre caen en la misma línea de caché o en dos contiguas.
**Hardware:** Al leer ambos hijos simultáneamente usando una sola instrucción de carga masiva, reducís el tráfico del bus de memoria a la mitad, duplicando el throughput de la fase de extracción de Heapsort.

---

## 31. COLAS DE PRIORIDAD EN REDES: QUALITY OF SERVICE (QOS)

En los routers que sostienen Internet, no todos los paquetes se tratan igual.
1. **Prioridad de Tráfico:** Un paquete de una llamada de voz (VoIP) tiene más prioridad que uno de una descarga de archivo.
2. **Buffer Management:** El router mantiene múltiples colas de prioridad. Si el enlace se satura, el router saca primero los paquetes de la cola de alta prioridad.
**Por qué no FIFO:** Porque el jitter (variación de latencia) destruye la comunicación en tiempo real. La cola de prioridad es lo que asegura que puedas hablar por Skype mientras bajás un juego en Steam.

---

## 32. LA FÍSICA DE LA RAÍZ: CONTENCIÓN DE CACHÉ EN HEAPS CONCURRENTES

Cuando escalás una cola de prioridad a cientos de hilos, la raíz (`array[0]`) se convierte en el mayor cuello de botella del sistema.
1. **Cache Contention:** Cada hilo que hace `deleteMin` o `insert` debe modificar la raíz. 
2. **Serialización del Bus:** El protocolo MESI obliga a que solo un núcleo tenga la línea de caché de la raíz en estado `Modified`. Los otros núcleos deben esperar.
**Solución Arquitectónica:** Se usan **Flat Combining** o **Relaxed Priority Queues**. Estas estructuras permiten que los hilos no choquen en la raíz, devolviendo un elemento que es "casi el mínimo" (p.ej. uno de los 10 más chicos), lo cual es suficiente para el 99% de las aplicaciones masivas.

---

## 33. ANÁLISIS DE HARDWARE: TLB Y PAGE FAULTS EN HEAPS MASIVOS

Si tu heap es de 100GB y hacés búsquedas aleatorias:
1. **TLB Misses:** Al hundir un elemento, saltás a índices como $2i+1$. En un heap gigante, estos saltos cruzan páginas de memoria virtual constantemente.
2. **Hard Page Faults:** Si parte del heap está en el archivo de intercambio (Swap), el rendimiento cae en un factor de 100.000x.
**Recomendación:** Siempre pre-alocá tu heap usando **Huge Pages** (2MB) para que la CPU pueda traducir las direcciones del arreglo masivo sin fallos de TLB.

---

## EPÍLOGO: LA LEY DE LA IMPORTANCIA

Dominar la cola de prioridad es, en última instancia, aprender a gestionar el valor de la información en el tiempo. Hemos visto cómo una simple idea de "padre menor que hijos" escala desde los registros de interrupción de tu CPU hasta los algoritmos de navegación que guían a millones de conductores cada día.

No te quedes con la superficie. La próxima vez que veas un sistema respondiendo con urgencia, un archivo comprimiéndose o un router priorizando tu voz, recordá que hay un heap asegurando que lo importante no se pierda en la marea de lo trivial. Que la búsqueda de la eficiencia sea tu brújula, pero que el respeto por la urgencia sea tu ancla. La informática es una disciplina de decisiones, y hoy has dominado la estructura que toma las mejores decisiones por vos. Construí con sabiduría, medí con rigor y nunca dejes de vigilar la altura de tus heaps. Que tus raíces siempre sean mínimas y tu complejidad siempre sea logarítmica.

## Próximo paso

Habiendo dominado el orden de la importancia y el rigor de los heaps, es momento de entrar en la estructura que nos permite organizar la información en el disco: los [Árboles B](../arboles/arboles_b.md).

### Ejercicio 5: Detección de Invariante de Heap (Ampliación Maestro)
**Análisis de Throughput:**
Para validar un heap de 1GB en una CPU Intel Xeon de 32 núcleos:
1. **El Lazo Crítico:** `for (int i=0; i < (n-2)/2; i++)`. 
2. **Localidad:** Como los hijos están en $2i+1$ y $2i+2$, la CPU está leyendo memoria que está físicamente cerca del padre. El hardware prefetcher puede traer los bloques de los hijos mucho antes de que los necesites.
3. **Throughput Estimado:** 4.000 millones de comparaciones por segundo. Validar un heap de 1GB tarda menos de 50 milisegundos. Es la base de los sistemas de **Integridad de Datos** en tiempo real.

### Ejercicio 6: Heaps con Prioridades Duplicadas (Ampliación)
**Mecánica de Desempate:**
En un motor de emparejamiento de una bolsa de valores (ej. BYMA), cientos de órdenes pueden tener el mismo precio (prioridad).
1. **El Problema del Azar:** Si el desempate es aleatorio, los inversores se quejarán. 
2. **FIFO-Priority Queue:** Guardamos `{priority, timestamp}`. El heap ordena por prioridad, y a igual prioridad, por timestamp. 
3. **Mecánica:** Esto asegura que entre dos ofertas iguales, la que llegó primero se ejecuta primero. La cola de prioridad se convierte en una **Estructura Justa y Eficiente**.

### Ejercicio 8: Huffman Tree Trace (Física de la Información)
**Resolución Detallada:**
Dada la frecuencia `[A:10, B:50, C:2, D:15]`.
1. **Saco C(2) y A(10).** Padre CA(12). Meto al heap. Heap: `[CA:12, D:15, B:50]`.
2. **Saco CA(12) y D(15).** Padre CAD(27). Meto al heap. Heap: `[CAD:27, B:50]`.
3. **Saco CAD(27) y B(50).** Raíz CADB(77).
**Árbol:** 
- B: 1 bit (0).
- D: 2 bits (11).
- A: 3 bits (101).
- C: 3 bits (100).
**Ahorro:** En lugar de 8 bits por carácter, el promedio es $\approx 1.5$ bits. La cola de prioridad es el motor que permite que Netflix y YouTube funcionen en tu celular.

### Ejercicio 10: Task Scheduler with Aging (Diseño de RTOS)
**Consigna:** Evitá el Starvation.

**Resolución Detallada:**
1. **Aging:** Cada 100ms que una tarea pasa en el heap sin ser ejecutada, incrementamos su urgencia (bajamos su valor numérico de prioridad).
2. **Re-heapify:** Esto obliga a realizar un `sift-up` en el nodo de la tarea.
3. **Performance:** Realizar `sift-up` masivos es caro. Los sistemas operativos modernos usan múltiples colas de prioridad por "clases" para que el aging ocurra únicamente entre tareas de la misma categoría, minimizando el movimiento en el árbol global.

---

## 43. COLAS DE PRIORIDAD EN GRÁFICOS 3D: EL SCANLINE Z-BUFFER

Para dibujar un mundo 3D en tu pantalla, la placa de video (GPU) debe decidir qué píxel está por encima de otro.
1. **La Escena:** Tenés miles de triángulos en el espacio 3D.
2. **Scanline:** El hardware procesa la pantalla línea por línea (de arriba a abajo).
3. **Active Edge Table (AET):** Para cada línea, la GPU mantiene una cola de prioridad con los bordes de los triángulos que cruzan esa línea, ordenados por su profundidad (Z). 
**Mecánica:** El heap asegura que el triángulo más cercano a tus ojos se dibuje primero, tapando a los de atrás. Sin esta cola de prioridad masiva en el silicio, los videojuegos 3D serían imposibles.

---

## 44. TABLA COMPARATIVA FINAL DE ESTRUCTURAS SECUENCIALES

Para cerrar esta familia, comparamos todas las herramientas según el criterio de **Importancia vs Justicia**.

| Estructura | Política | Costo Dominante | Hardware | Uso Ideal |
| :--- | :--- | :--- | :--- | :--- |
| **Arreglo** | Posicional | Búsqueda $O(n)$ | Prefetcher | Lectura intensa |
| **Lista** | Punteros | Salto $O(n)$ | TLB | Edición local |
| **Pila** | LIFO | Tope $O(1)$ | Stack Pointer | Recursión / Undo |
| **Cola** | FIFO | Frente $O(1)$ | kfifo | Mensajería / Redes |
| **Deque** | Dual | Extremos $O(1)$ | Work-stealer | Hilos virtuales |
| **Prioridad**| Importancia| Heap $O(\log n)$ | Cache L1 (Root) | Scheduling / IA |

---

## GLOSARIO ENCICLOPÉDICO DE PRIORIDAD (Nivel Arquitecto)

1. **Atomic Dual-Heap:** Técnica concurrente que permite insertar y extraer de dos heaps paralelos para reducir la contención en la raíz.
2. **Branchless Sift-down:** Optimización del compilador que elimina los `if` del hundimiento usando operaciones de comparación aritmética, acelerando Heapsort un 20%.
3. **Comparison Count Complexity:** Métrica que mide el rendimiento de un heap basada en el número de veces que el hardware toca la ALU.
4. **Distance-to-Root:** Propiedad que define cuántos ciclos de reloj tarda un elemento en llegar a ser el mínimo global.
5. **Heap-friendly Memory Segments:** Uso de memoria alineada a 64KB para que el heap binario no sufra de fallos de caché en los niveles medios.
6. **Lazy Heapification:** Estrategia donde el heap no se balancea inmediatamente, sino que acumula cambios y se reorganiza en lotes.
7. **Monotonicity Guard:** Invariante de seguridad que valida que ningún hijo sea menor que su padre durante la ejecución.
8. **Parallel Build-Heap:** Algoritmo que utiliza múltiples hilos para aplicar `sift-down` en diferentes sub-árboles simultáneamente, logrando tiempos de construcción sub-lineales en máquinas multi-core.
9. **Radix Heap:** Variante del heap optimizada para valores de prioridad pequeños o de rango fijo (ej: timestamps).
10. **Wait-free Priority Queue:** Estructura que garantiza que un hilo de alta prioridad nunca sea frenado por uno de baja prioridad en la inserción.

---

## RESUMEN FINAL DEL TRATADO (Ampliación)

Habiendo recorrido desde los axiomas de la importancia hasta la física de los registros de la GPU, queda claro que la cola de prioridad es la estructura que da sentido al caos.
- **Microarquitectura:** El APIC de tu PC es una cola de prioridad.
- **Grafos:** Dijkstra es un recorrido de grafo guiado por una cola de prioridad.
- **Software:** La `PriorityQueue` de Java es tu mejor aliada para simulaciones masivas.
La ley de la importancia es lo que permite que el software sea inteligente, decidiendo qué es urgente y qué puede esperar.

## Próximo paso

---

## 52. ANEXO: TABLA DE COSTOS DE HARDWARE PARA HEAPS

| Operación | Ciclos de CPU | Latencia Estimada | Motivo |
| :--- | :---: | :---: | :--- |
| findMin (L1 hit) | 1 | 0.25ns | Root is always hot |
| sift-up (L2 hit) | 20 | 5ns | Parent is nearby |
| sift-down (L3 hit) | 100 | 25ns | Children are far |
| build-Heap (RAM) | 200+ | 50ns+ | Cold start |

**Nota:** El heap binario es una de las estructuras que mejor aprovecha la jerarquía de memoria porque los niveles superiores (donde ocurren la mayoría de los accesos) son lo suficientemente pequeños como para vivir enteramente en la caché L1 de la CPU.

## Próximo paso

Habiendo dominado el orden de la importancia y el rigor de los heaps, es momento de entrar en la estructura que nos permite organizar la información en el disco: los [Árboles B](../arboles/arboles_b.md).


---

## 45. ESTUDIO DE CASO: PRIORIDAD EN EL KERNEL - INVERSIÓN Y HERENCIA

En sistemas embebidos (ej. el robot Mars Pathfinder), ocurrió un error histórico de colas de prioridad.
1. **La Inversión de Prioridad:** Una tarea de BAJA prioridad tenía un lock sobre un recurso compartido. Una tarea de ALTA prioridad quería el recurso y quedó bloqueada. Una tarea de MEDIA prioridad empezó a correr (ya que era más importante que la baja), impidiendo que la baja termine y suelte el lock.
2. **El Resultado:** La tarea de ALTA prioridad nunca corrió, causando que el robot se reseteara constantemente en Marte.
3. **La Solución (Priority Inheritance):** Cuando una tarea de alta prioridad se bloquea por una de baja, la de baja **hereda temporalmente** la prioridad alta para terminar rápido y soltar el lock.
**Lección:** La cola de prioridad no es una isla; interactúa con los locks y el planificador del sistema operativo, requiriendo mecanismos de herencia para evitar colapsos sistémicos.

---

## GLOSARIO ENCICLOPÉDICO DE PRIORIDAD (Final Maestría)

11. **Soft Priority:** Política donde la prioridad es una sugerencia al sistema, pero no una garantía absoluta, permitiendo un mejor throughput a cambio de jitter controlado.
12. **Static vs Dynamic Priority:** Diferencia entre prioridades fijas al nacer (ej: nivel de interrupción) vs prioridades que cambian según el estado del sistema (ej: virtual runtime en CFS).
13. **Strict Priority Queueing:** Algoritmo de red donde la cola $N$ no se atiende hasta que la cola $N-1$ esté totalmente vacía.
14. **Tree-based Heap:** Implementación de cola de prioridad que usa nodos enlazados en lugar de arreglos, necesaria cuando el heap es masivamente disperso y la memoria no es contigua.
15. **Vectorized Sift-down:** Técnica de compilador que usa instrucciones AVX para comparar un nodo con múltiples descendientes en un solo ciclo, ideal para heaps d-ary.
16. **Wait-free Priority Queue:** Implementación concurrente basada en el algoritmo de "helping" donde ningún hilo puede ser bloqueado por otro, garantizando latencia p99.99 determinística.
17. **Weight-fair Queueing (WFQ):** Generalización de la cola de prioridad donde cada cola recibe una fracción del ancho de banda proporcional a su peso.

---

## EPÍLOGO: LA SINFONÍA DE LA RELEVANCIA

Dominar la cola de prioridad es, en última instancia, aprender a orquestar el valor de la información en el tiempo y el espacio. Hemos visto cómo una simple flecha de "padre menor que hijos" escala desde las señales eléctricas de interrupción en tu CPU hasta los algoritmos de navegación que guían el tráfico de ciudades enteras.

No te quedes con la superficie. La próxima vez que veas un sistema respondiendo con urgencia, un archivo comprimiéndose sin pérdida de calidad o una IA tomando una decisión inteligente en milisegundos, recordá que hay un heap asegurando que lo importante no se pierda en la marea de lo trivial. Que la búsqueda de la relevancia sea tu brújula, pero que el rigor de la estructura sea tu ancla. La informática es el arte de decidir qué procesar ahora y qué dejar para después, y hoy has descendido hasta las raíces mismas de esa decisión. Construí con sabiduría, medí con el rigor del profiling y nunca dejes de vigilar la altura de tus heaps. Que tu complejidad sea siempre logarítmica y tu rendimiento siempre sea el máximo.

## Próximo paso

Habiendo dominado el orden de la importancia y el rigor de los heaps, es momento de entrar en la estructura que nos permite organizar la información en el disco: los [Árboles B](../arboles/arboles_b.md).

### Ejercicio 5: Detección de Invariante de Heap (Ampliación Maestro)
**Análisis de Throughput:**
Para validar un heap de 1GB en una CPU Intel Xeon de 32 núcleos:
1. **El Lazo Crítico:** `for (int i=0; i < (n-2)/2; i++)`. 
2. **Localidad:** Como los hijos están en $2i+1$ y $2i+2$, la CPU está leyendo memoria que está físicamente cerca del padre. El hardware prefetcher puede traer los bloques de los hijos mucho antes de que los necesites.
3. **Throughput Estimado:** 4.000 millones de comparaciones por segundo. Validar un heap de 1GB tarda menos de 50 milisegundos. Es la base de los sistemas de **Integridad de Datos** en tiempo real.

### Ejercicio 6: Heaps con Prioridades Duplicadas (Ampliación)
**Mecánica de Desempate:**
En un motor de emparejamiento de una bolsa de valores (ej. BYMA), cientos de órdenes pueden tener el mismo precio (prioridad).
1. **El Problema del Azar:** Si el desempate es aleatorio, los inversores se quejarán. 
2. **FIFO-Priority Queue:** Guardamos `{priority, timestamp}`. El heap ordena por prioridad, y a igual prioridad, por timestamp. 
3. **Mecánica:** Esto asegura que entre dos ofertas iguales, la que llegó primero se ejecuta primero. La cola de prioridad se convierte en una **Estructura Justa y Eficiente**.

### Ejercicio 11: Build-Heap vs Insert (Análisis Empírico)
**Consigna:** Realizá un benchmark comparando ambos métodos de construcción.

**Resolución Detallada:**
1. **Insert Masivo:** Insertar $N$ elementos uno por uno cuesta $O(N \log N)$. Al graficarlo, verás una curva que crece más rápido que la lineal.
2. **Build-Heap:** Procesar el arreglo desde el último padre cuesta $O(N)$. La curva es una recta perfecta.
3. **Hardware:** `buildHeap` es más rápido no solo por la complejidad asintótica, sino por la **Localidad Temporal**. Al procesar el arreglo de atrás hacia adelante, estás trabajando sobre sub-árboles pequeños que caben enteros en la caché L1. Las inserciones individuales, en cambio, saltan por todo el heap, causando fallos de caché en cada nivel.
**Throughput:** Para un arreglo de 100M de elementos, `buildHeap` puede ser hasta 10 veces más rápido que las inserciones secuenciales.

### Ejercicio 14: Pairing Heap Trace (Algoritmos Autonómicos)
**Consigna:** Realizá el seguimiento de tres inserciones y un `deleteMin`.

**Resolución Detallada:**
1. **Estructura:** El Pairing Heap es un árbol multi-camino. No mantiene balanceo rígido.
2. **Insert:** Simplemente "colgás" el nuevo árbol debajo de la raíz menor. $O(1)$.
3. **DeleteMin:** Esta es la operación inteligente. Al borrar la raíz, quedan muchos sub-árboles huérfanos. 
4. **Fase de Fusión:** Se unen de a pares (First Pass) y luego de atrás hacia adelante (Second Pass).
**Análisis:** Esta fase de fusión es la que realiza el rebalanceo de forma "perezosa". Es un algoritmo **Self-Adjusting** que se adapta al patrón de datos del usuario, similar a como un Splay Tree se adapta a las búsquedas.

### Ejercicio 15: RTOS Job Scheduling (Prioridad Estricta)
**Consigna:** Simulá la cola de listos de un SO de tiempo real donde la prioridad es el `deadline`.

**Resolución Detallada:**
1. **Tarea:** `{id: 1, deadline: 50ms}`, `{id: 2, deadline: 10ms}`.
2. **Cola:** El Max-Heap (o Min-Heap invertido) asegura que la tarea con el deadline más cercano esté siempre en la raíz.
3. **Preemption:** Si llega una tarea nueva con un deadline menor al de la tarea actual, el hardware dispara una interrupción. El scheduler inserta la tarea en el heap y realiza un cambio de contexto.
**Hardware:** En un RTOS, el heap suele estar en **SRAM** (memoria estática ultra-rápida) para garantizar que la latencia de inserción sea determinística y no dependa de los caprichos del controlador de DDR5.

---

## 53. COLAS DE PRIORIDAD EN COMPILADORES: REGISTRO DE ALIGNMENT

Durante la fase de **Asignación de Registros**, el compilador debe decidir qué variable vive en la CPU y cuál se va a la RAM.
1. **Interference Graph:** Las variables se representan como nodos.
2. **Prioridad por Grado:** El compilador usa una cola de prioridad para procesar primero los nodos con menos conexiones (grado bajo).
3. **Mecánica:** Al asignar un registro a una variable, actualizamos la prioridad de sus vecinos en el heap. Es el ejemplo perfecto de cómo una cola de prioridad permite resolver un problema NP-Completo (Coloreo de Grafos) mediante una heurística voraz eficiente.

---

## 54. LA FÍSICA DEL HEAP D-ARY: ASOCIATIVIDAD DE CACHÉ

¿Por qué un 4-ary heap es a menudo más rápido que uno binario en una CPU moderna?
1. **Set Associativity:** Las cachés L1 están organizadas en conjuntos (ej. 8-way associative). 
2. **Localidad de Hijos:** En un 4-ary heap, los 4 hijos de un nodo $i$ tienen una alta probabilidad de mapear a diferentes "sets" dentro de la misma línea de caché o líneas contiguas.
3. **Throughput:** Al comparar el padre con 4 hijos, la CPU puede disparar las comparaciones en paralelo usando su unidad de ejecución **Out-of-Order**, ocultando la latencia de las comparaciones individuales.

---

## 55. ESTUDIO DE CASO: EL SCHEDULER O(1) DE LINUX (HISTORIA)

Antes del CFS actual, Linux usaba el **Scheduler O(1)**.
- **Estructura:** Tenía dos arreglos de colas de prioridad (Active y Expired).
- **Mecánica:** Cada prioridad (0 a 140) tenía su propia cola FIFO. El sistema usaba un **Bitset** masivo para encontrar instantáneamente la cola con la prioridad más alta.
- **Lección:** Este diseño demostró que podés lograr tiempos constantes ($O(1)$) si limitás el rango de las prioridades, convirtiendo una búsqueda en un heap en una simple operación de bits en el hardware.

---

## 60. ANEXO: TABLA DE COSTOS DE COMPARACIÓN EN LA JVM

| Tipo | Ciclos CPU | Instrucción |
| :--- | :---: | :--- |
| int | 1 | `CMP` |
| double | 3 | `COMISD` |
| String | 50+ | `String.compareTo()` |
| Object | 100+ | `Comparator.compare()` |

**Nota de Performance:** En un heap masivo, el costo de la comparación domina sobre el costo del movimiento de datos. Si podés usar primitivos, tu heap será 10 veces más rápido que si usás objetos genéricos.

---

## Próximo paso

### Ejercicio 11: Build-Heap vs Insert (Ampliación Maestro)
**Análisis de Microarquitectura:**
Cuando ejecutás `buildHeap`, la CPU está procesando el arreglo de atrás hacia adelante. Esto es **Cache Friendly** porque en las primeras etapas trabajás con sub-heaps de tamaño 3 (un padre y dos hijos) que caben perfectamente en un solo bloque de 64 bytes de la caché L1. Al procesar estos bloques locales, reducís las señales de arbitraje del bus de memoria. En cambio, insertar $N$ elementos uno por uno implica que cada `push` recorre un camino desde una hoja hasta la raíz, saltando por regiones de memoria que ya fueron expulsadas de la caché, disparando fallos de L2 y L3 de forma masiva.

### Ejercicio 12: Memory Layout of D-ary Heap (Ampliación)
**Física de la RAM:**
En un heap binario, el padre está en $i$ y los hijos en $2i+1, 2i+2$. A medida que $i$ crece, la distancia física en bytes entre padre e hijos se duplica en cada nivel. Esto destruye la **Localidad Espacial**. En un 8-ary heap, tenés 8 hijos pegados en un bloque de 64 bytes. Al leer el primer hijo, el hardware prefetcher trae los otros 7 "gratis". Estás optimizando el ancho de banda del bus DDR5 al máximo, reduciendo el tiempo de `deleteMin` en un 40% a pesar de tener que hacer más comparaciones locales.

### Ejercicio 16: Binary Heap with Pooling (Ampliación)
**Mecánica de la JVM:**
Si implementás un heap sobre una estructura enlazada (`new Node()`), estás bombardeando al **Eden Space**.
1. **Pausas de GC:** El Garbage Collector debe escanear cada nodo para ver si está vivo. Un heap de 10 millones de nodos enlazados puede tardar 200ms en ser escaneado.
2. **La Solución del Pool:** Al usar un arreglo primitivo (`long[]`) y manejar los índices manualmente, el heap se vuelve invisible para el GC. La JVM ve un solo objeto gigante inerte.
**Resultado:** Podés procesar billones de eventos con latencia p99 de microsegundos, algo que con `PriorityQueue<Node>` sería imposible por el jitter del recolector de basura.

### Ejercicio 17: Flattening Binary Heaps (Análisis de Forma)
**Consigna:** Dada un arreglo desordenado, mostrá los pasos de `heapify`.

**Resolución Detallada:**
Sea `[10, 20, 15, 30, 40]`.
1. Empezamos en el último padre: índice 1 (valor 20). Sus hijos son 30 y 40. Como 20 < 30 y 20 < 40, no cambia.
2. Pasamos al índice 0 (valor 10). Sus hijos son 20 y 15. Como 10 es menor, no cambia.
**Resultado:** Este arreglo ya era un heap. El ejercicio demuestra que la relación de orden parcial es muy permisiva, permitiendo que muchas secuencias aleatorias cumplan la propiedad de heap sin necesidad de movimientos, lo cual explica por qué las inserciones promedio son tan rápidas.

### Ejercicio 18: Priority Queue with Multi-threading (Análisis de Contención)
**Mecánica de Locks:**
Si usás un `ReentrantLock` global, cada hilo que hace `insert` frena a todos los que hacen `deleteMin`.
**La Alternativa:** Se usan **Heaps Concurrentes de Skip-List**. 
- Permiten que hilos operen en diferentes niveles del árbol simultáneamente.
- La contención en la raíz se resuelve mediante el uso de un **Buffer de Eliminación** (Elimination Array), donde hilos que quieren insertar y hilos que quieren borrar se encuentran y "cancelan" su operación mutuamente sin tocar el heap central.
- **Hardware:** Esto reduce la temperatura del silicio al minimizar las colisiones en el bus de datos de la memoria L3.

---

## 61. HEAPS EN LA PROGRAMACIÓN FUNCIONAL: EL LEFTIST HEAP

En lenguajes inmutables, no podemos usar arreglos dinámicos para los heaps porque cada intercambio requeriría copiar el arreglo entero ($O(n)$).
1. **La Estructura:** Se usa un árbol binario explícito.
2. **Invariante Leftist:** El "rango" (distancia al nodo nulo más cercano) del hijo izquierdo es siempre mayor o igual al del hijo derecho.
3. **Mecánica:** Al insertar o borrar, el árbol tiende a "pesarse" hacia la izquierda. Las operaciones ocurren en la rama derecha, que garantizamos que es corta ($O(\log n)$).
**Resultado:** Tenés una cola de prioridad con garantías $O(\log n)$ que respeta la inmutabilidad y permite el **Structural Sharing** entre diferentes versiones del heap.

---

## 62. DEMOSTRACIÓN FORMAL: CORRECTITUD DEL HEAPSORT

Demostramos que tras la fase de extracción, el arreglo queda ordenado.
1. **Invariante:** Al inicio de la iteración $k$, los elementos de $n-k+1$ a $n$ están ordenados y son los mayores del arreglo. La porción $0 \dots n-k$ es un Max-Heap.
2. **Paso:** Intercambiamos `arr[0]` (máximo de la porción heap) con `arr[n-k]`. Ahora la porción ordenada creció en uno.
3. **Restauración:** Aplicamos `sift-down` en la nueva raíz de la porción $0 \dots n-k-1$. 
**Conclusión:** Por inducción sobre $k$, cuando $k=n$, el arreglo está totalmente ordenado.

---

## EPÍLOGO: LA DANZA DE LAS PRIORIDADES

Dominar la cola de prioridad es, en última instancia, aprender a orquestar el valor de la información en el tiempo y el espacio. Hemos visto cómo una simple flecha de "padre menor que hijos" escala desde los registros de interrupción de tu CPU hasta los algoritmos de navegación que guían el tráfico de ciudades enteras.

No te quedes con la superficie. La próxima vez que veas un sistema respondiendo con urgencia, un archivo comprimiéndose sin pérdida de calidad o una IA tomando una decisión inteligente en milisegundos, recordá que hay un heap asegurando que lo importante no se pierda en la marea de lo trivial. Que la búsqueda de la relevancia sea tu brújula, pero que el rigor de la estructura sea tu ancla. La informática es el arte de decidir qué procesar ahora y qué dejar para después, y hoy has descendido hasta las raíces mismas de esa decisión. Construí con sabiduría, medí con el rigor del profiling y nunca dejes de preguntarte qué sucede debajo del capó de tus abstracciones. Que tus raíces siempre sean mínimas y tu complejidad siempre sea logarítmica.

## Próximo paso

Habiendo dominado el orden de la importancia y el rigor de los heaps, es momento de entrar en la estructura que nos permite organizar la información en el disco: los [Árboles B](../arboles/arboles_b.md).

---

## 63. ESTUDIO DE CASO: EL ERROR DE PRIORIDAD EN EL MARS PATHFINDER (DETALLE)

En 1997, el robot de la NASA en Marte empezó a reiniciarse sin razón aparente. El profiling remoto reveló un problema de **Inversión de Prioridad**.
1. **La Tarea de Baja Prioridad:** Un hilo de recolección de datos meteorológicos tenía el lock sobre un bus de memoria.
2. **La Tarea de Alta Prioridad:** El hilo de navegación necesitaba el bus y quedó bloqueado esperando a la tarea meteorológica.
3. **La Interferencia:** Un hilo de media prioridad (comunicaciones) empezó a correr, como era más importante que el meteorológico pero menos que el de navegación, el sistema operativo le dio la CPU.
4. **El Bloqueo Infinito:** La tarea meteorológica nunca terminó, la de navegación nunca despertó, y el "Watchdog Timer" del robot decidió reiniciar el sistema al detectar que el bus estaba bloqueado demasiado tiempo.
**La Solución:** Se inyectó un parche desde la Tierra para activar el protocolo de **Herencia de Prioridad**, permitiendo que la tarea de baja prioridad "pidiera prestada" la importancia de la de navegación para terminar su trabajo y soltar el lock.

---

## 70. RESUMEN FINAL DEL TRATADO DE PRIORIDAD

Hemos descendido hasta las raíces del silicio para entender que la importancia es un recurso limitado.
- **Hardware:** El APIC y los registros de interrupción imponen la ley de la urgencia.
- **TAD:** El heap binario es el equilibrio perfecto entre simplicidad y velocidad logarítmica.
- **Industria:** Desde el A* de los videojuegos hasta el Beam Search de la IA, la cola de prioridad es la que permite que el software sea inteligente.

## Próximo paso

---

## 71. LA FÍSICA DE LA PRIORIDAD: VOLTAJE Y GESTIÓN DE ENERGÍA

En dispositivos móviles (ej. tu celular), la cola de prioridad interactúa con el hardware de gestión de energía.
1. **DVFS (Dynamic Voltage and Frequency Scaling):** Si la cola de prioridad del SO tiene muchas tareas de "Alta Prioridad", el hardware sube el voltaje y la frecuencia de la CPU.
2. **Race to Sleep:** La estrategia es ejecutar las tareas de alta prioridad lo más rápido posible para poder apagar los núcleos y ahorrar batería.
**Importancia:** Un algoritmo de cola de prioridad ineficiente (ej. $O(n)$) no solo hace el software más lento, sino que agota físicamente la batería de tu dispositivo.

---

## 72. ESTUDIO DE CASO: HEAPS VS ARREGLOS ORDENADOS EN BÚSQUEDA DE SEÑALES

En el procesamiento de señales de radioastronomía, debemos encontrar los $K$ picos más fuertes en un stream de Gigabytes de datos.
- **Opción A (Arreglo Ordenado):** Mantener un arreglo de tamaño $K$ ordenado. Cada inserción es $O(K)$.
- **Opción B (Min-Heap):** Mantener un heap de tamaño $K$. Cada inserción es $O(\log K)$.
**Resultado:** Para $K=10.000$, el heap es 1000 veces más rápido. Esto demuestra que la elección de la estructura de datos es lo que permite que un telescopio procese el cielo en tiempo real.

---

## 80. RECORRIDO DE LA PARTE 6: SEGUNDO PUNTO DE CONTROL

Habiendo dominado todas las estructuras lineales restringidas (Pilas, Colas, Deques y Prioridad), ya tenés las herramientas para modelar el 90% de los flujos de datos de la industria. Lo que sigue es entrar en el terreno donde la linealidad se rompe para dar paso a la jerarquía: los [Árboles](../arboles/indice.md).

---

## Próximo paso

---

## 91. REFLEXIÓN FINAL: EL EQUILIBRIO DE LA IMPORTANCIA

El diseño de una cola de prioridad es, en última instancia, el diseño de la urgencia del software. Un sistema que trata a todos sus datos por igual es un sistema democrático pero a menudo ineficiente ante la emergencia. El heap nos enseña que podemos ser rápidos siendo selectivos. Esperamos que este tratado masivo te haya dado la profundidad necesaria para que tu código nunca sea el causante de una 'Inversión de Prioridad' en el mundo real.

Construí con sabiduría, medí con el rigor de JMH y nunca dejes de preguntarte qué sucede en la raíz de tus estructuras. El tiempo es el recurso más caro, y las colas de prioridad son las que nos permiten gastarlo con inteligencia.

## 92. BIBLIOGRAFÍA COMPLETA (Ampliación)
- **The Art of Computer Programming, Vol 3** (Knuth): El capítulo de heaps es la fuente original.
- **Introduction to Algorithms** (CLRS): La demostración formal de Build-Heap es obligatoria.
- **Algorithms** (Sedgewick): Para las mejores trazas visuales de sift-up y sift-down.
- **Computer Architecture** (Hennessy): Para entender por qué el 4-ary heap domina en el silicio.

## Próximo paso

Habiendo dominado el orden de la importancia y el rigor de los heaps, es momento de entrar en la estructura que nos permite organizar la información en el disco: los [Árboles B](../arboles/arboles_b.md).


---

## 81. DEMOSTRACIÓN FORMAL: CORRECTITUD DE DIJKSTRA CON HEAPS

Dijkstra es un algoritmo voraz. Demostramos por inducción que cuando un vértice $u$ es extraído de la cola de prioridad, su distancia `dist[u]` es la mínima posible.
1. **Caso Base:** El origen $s$ tiene `dist[s]=0`. Es extraído primero. Correcto.
2. **Hipótesis:** Se cumple para todos los vértices ya extraídos.
3. **Paso:** Sea $v$ el próximo a extraer. Si hubiera un camino más corto, tendría que pasar por un vértice $w$ que todavía está en la cola. Pero como el heap nos dio a $v$, significa que `dist[v] <= dist[w]`. Como los pesos son no-negativos, cualquier camino a través de $w$ será $\geq dist[w]$, lo que contradice que sea más corto que `dist[v]`.
**Q.E.D.** Esta prueba es lo que nos da la seguridad de que el GPS no nos está mandando por una ruta ineficiente.

---

## 82. EVOLUCIÓN HISTÓRICA: EL NACIMIENTO DEL HEAP

El heap fue inventado por **J.W.J. Williams** en 1964 específicamente para el algoritmo de ordenamiento Heapsort.
- **Contexto:** En esa época, la RAM era tan cara que no podías permitirte un arreglo auxiliar (como en Merge Sort). 
- **Genialidad:** Williams se dio cuenta de que podías superponer una estructura de árbol sobre un arreglo lineal, logrando lo mejor de dos mundos: la jerarquía para la velocidad y la contigüidad para el ahorro de memoria.
- **Legado:** Hoy, 60 años después, la misma estructura que Williams diseñó para computadoras de válvulas de vacío sigue siendo el estándar de oro en los centros de datos de Google.

---

## 90. CONCLUSIÓN FINAL DEL RECORRIDO LINEAL

Hemos terminado el estudio de las estructuras lineales. 
1. **Espacio:** Arreglos y Listas.
2. **Tiempo:** Pilas y Colas.
3. **Poder:** Deques y Prioridad.
Esta trilogía es la base de todo el software moderno. Lo que sigue es entrar en la **Tercera Dimensión** del cómputo: la jerarquía de los árboles.

---

## Próximo paso

Habiendo dominado el orden de la importancia y el rigor de los heaps, es momento de entrar en la estructura que nos permite organizar la información en el disco: los [Árboles B](../arboles/arboles_b.md).

---

## 95. APÉNDICE: TABLA DE COSTOS DE HEAPS EN DIFERENTES HARDWARES

| Arquitectura | L1 Sift-up | L2 Sift-down | RAM Build-Heap |
| :--- | :---: | :---: | :---: |
| x86_64 (AVX-512) | 0.5ns | 4ns | 40ns |
| ARM (Apple M2) | 0.3ns | 3ns | 30ns |
| FPGA (Custom) | 0.1ns | 1ns | 10ns |

**Nota:** Estos valores demuestran que el heap es una estructura que escala de forma lineal con la potencia del hardware. A diferencia de las estructuras basadas en punteros dispersos, el heap binario en arreglo contiguo permite que las unidades de ejecución vectorial de la CPU trabajen al máximo de su capacidad.

### Palabras finales
Has completado el estudio de las estructuras lineales restringidas. Lo que aprendiste hoy sobre el control del tiempo y la importancia te servirá para diseñar sistemas que no solo funcionen, sino que se sientan vivos por su capacidad de respuesta ante la urgencia. El camino sigue en la jerarquía de los árboles.

## Próximo paso

Habiendo dominado el orden de la importancia y el rigor de los heaps, es momento de entrar en la estructura que nos permite organizar la información en el disco: los [Árboles B](../arboles/arboles_b.md).


### Reflexión sobre la Jerarquía
La jerarquía no es solo una forma de organizar archivos. Es una forma de optimizar la realidad. Al entender que no todo es igual de importante, liberamos recursos para lo que realmente importa. Que tu código sea un reflejo de esta sabiduría.

---

## 100. ANEXO FINAL: RESUMEN DE COMPLEJIDAD PARA EXAMEN
- findMin: O(1)
- insert: O(log n) (Promedio O(1))
- deleteMin: O(log n)
- buildHeap: O(n)
- heapsort: O(n log n)


### Un último dato curioso
¿Sabías que el algoritmo de búsqueda A* es simplemente una búsqueda por anchura (BFS) donde la cola FIFO ha sido reemplazada por una cola de prioridad? Este pequeño cambio estructural es lo que permite que una IA encuentre el camino más corto en un mapa gigante sin explorar billones de nodos inútiles. La importancia lo es todo.


---

## 110. REFERENCIAS DE IMPLEMENTACIÓN
- **Java:** PriorityQueue<E>
- **C++:** std::priority_queue
- **Python:** heapq
- **Go:** container/heap

Todas estas bibliotecas estándar implementan un heap binario sobre un arreglo dinámico, confirmando que es la implementación industrial más robusta y eficiente jamás diseñada.


---

