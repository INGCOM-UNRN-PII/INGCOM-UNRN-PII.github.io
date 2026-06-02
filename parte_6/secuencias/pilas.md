---
title: "Pilas: Teoría, Implementación y Aplicaciones"
subtitle: "De la formalización algebraica al control del hardware"
subject: Estructuras de Datos
description: Un análisis exhaustivo sobre el TAD Pila, su formalización matemática, optimización en memoria, gestión en sistemas operativos y concurrencia lock-free.
---

(parte6-pilas)=
# Pilas

Las pilas no son simplemente "listas restringidas". Representan una de las abstracciones más poderosas de la computación, actuando como el mecanismo fundamental para la gestión de la recursión, la evaluación de expresiones y la administración de memoria en tiempo de ejecución.

Si bien la política **LIFO** (*Last-In, First-Out*) parece trivial, su implementación eficiente y su comportamiento bajo carga ocultan sutilezas arquitectónicas que todo ingeniero de software debe dominar. En este capítulo, vamos a destripar la pila desde sus axiomas matemáticos hasta su representación física en los registros de la CPU.

:::{tip} Objetivos de Aprendizaje
Al finalizar este capítulo, vas a poder:
1. Formalizar el comportamiento de una pila mediante axiomas algebraicos e inducción estructural.
2. Implementar pilas optimizadas tanto en memoria contigua como enlazada, comprendiendo sus trade-offs de localidad de cache.
3. Explicar el funcionamiento de la pila de llamadas (call stack) a nivel de registros de hardware (RSP/RBP) y convenciones de llamada ABI.
4. Implementar algoritmos complejos de parsing como Shunting-yard y evaluación de RPN.
5. Diseñar estructuras de datos lock-free para entornos concurrentes de alta performance y resolver el problema ABA.
:::

:::{note} Hoja de ruta del capítulo
**Prerrequisitos.** Requiere solidez en punteros/referencias, gestión de arreglos dinámicos y nociones de complejidad computacional $O$ grande. Para la sección de hardware, se asumen nociones básicas de arquitectura x86-64.

**Desarrollo.** Comenzamos con la formalización matemática para establecer qué es una pila sin ambigüedades. Luego, analizamos las implementaciones "clásicas" (arreglos vs. nodos) con un enfoque en el costo amortizado. Pasamos al "hierro": cómo la CPU usa la pila y cómo se protege el SO. Cerramos con aplicaciones de alto nivel y concurrencia avanzada.
:::

## 1. Formalización: El TAD Pila

Antes de tocar una sola línea de código, necesitamos definir qué es una pila de forma puramente abstracta. No nos importa si hay un arreglo o un nodo; nos importa el **comportamiento**.

### 1.1 Definición Algebraica

Podemos definir el Tipo Abstracto de Datos (TAD) Pila mediante un conjunto de operaciones y axiomas que gobiernan su estado. Sea $S$ el conjunto de todos los estados posibles de una pila y $E$ el conjunto de elementos que puede contener.

Definimos las siguientes funciones:

- $\text{crear}: \to S$ (Crea una pila vacía)
- $\text{push}: S \times E \to S$ (Agrega un elemento y devuelve el nuevo estado)
- $\text{pop}: S \to S \times E$ (Quita el tope y lo devuelve)
- $\text{top}: S \to E$ (Consulta el tope sin quitarlo)
- $\text{isEmpty}: S \to \text{Boolean}$

### 1.2 Axiomas Fundamentales

Para que una estructura sea legalmente una pila, debe satisfacer los siguientes axiomas para cualquier pila $s \in S$ y cualquier elemento $e \in E$:

1.  **Axioma de Identidad LIFO**: $\text{top}(\text{push}(s, e)) = e$
    *Lo último que metiste es lo que ves arriba.*
2.  **Axioma de Reversibilidad**: $\text{pop}(\text{push}(s, e)) = s$
    *Si metés algo y lo sacás inmediatamente, la pila vuelve al estado exacto anterior.*
3.  **Axioma de Vacuidad**: $\text{isEmpty}(\text{crear}()) = \text{true}$
4.  **Axioma de No-Vacuidad**: $\text{isEmpty}(\text{push}(s, e)) = \text{false}$
5.  **Axioma de Error**: $\text{pop}(\text{crear}())$ y $\text{top}(\text{crear}())$ están indefinidos (o lanzan una excepción de *underflow*).

### 1.3 Inducción Estructural sobre Pilas

La estructura de una pila es inductiva por naturaleza. Podemos decir que una pila es:
- O bien la pila vacía ($\lambda$).
- O bien el resultado de $\text{push}(s, e)$, donde $s$ es una pila.

Esto nos permite demostrar propiedades usando **Inducción Estructural**. Por ejemplo, para demostrar que una propiedad $P(s)$ vale para toda pila $s$:
1.  **Caso Base**: Demostrar $P(\lambda)$.
2.  **Paso Inductivo**: Asumiendo que $P(s)$ es cierto (hipótesis inductiva), demostrar que $P(\text{push}(s, e))$ también lo es para cualquier $e$.

Esta técnica es la que usan los verificadores formales de software para garantizar que un compilador o un sistema operativo no corrompa la memoria.

## 2. Implementación Interna: El Precio de la Memoria

Existen tres formas canónicas de implementar una pila. La elección no es estética; depende de si priorizás la **latencia de acceso** (localidad de datos) o la **flexibilidad de crecimiento**.

### 2.1 Pila basada en Arreglo (Contiguity Model)

En esta variante, usamos un bloque de memoria contiguo. El "tope" es simplemente un índice entero.

#### Gestión de Capacidad y Costo Amortizado

Si la pila es acotada, el costo de `push` es siempre $O(1)$. Pero en la vida real, queremos pilas que crezcan. ¿Cómo redimensionamos?

1.  **Estrategia Incremental (Aritmética)**: Aumentar el tamaño en $k$ posiciones (ej. +100).
    - Si hacemos $n$ operaciones de `push`, el costo total es $O(n^2)$.
    - **No lo hagas**. Es ineficiente para grandes volúmenes de datos.
2.  **Estrategia Geométrica (Multiplicativa)**: Duplicar el tamaño cuando se llena.
    - Aunque un `push` individual pueda costar $O(n)$ (cuando toca copiar), el **costo amortizado** por operación es $O(1)$.
    - La demostración técnica (método del potencial) nos dice que la suma de los costos dividida $n$ tiende a una constante.

```java
/**
 * Implementación robusta de una Pila usando Arreglos Genéricos.
 * Incluye gestión de capacidad y fail-fast iterators.
 */
public class PilaDinamica<T> implements Pila<T>, Iterable<T> {
    private T[] elementos;
    private int topeIdx;
    private int modCount = 0; // Para fail-fast iterators
    private static final int CAPACIDAD_INICIAL = 16;

    @SuppressWarnings("unchecked")
    public PilaDinamica() {
        this.elementos = (T[]) new Object[CAPACIDAD_INICIAL];
        this.topeIdx = -1;
    }

    @Override
    public void push(T e) {
        if (e == null) throw new IllegalArgumentException("No se permiten nulos");
        if (topeIdx == elementos.length - 1) {
            redimensionar(elementos.length * 2);
        }
        elementos[++topeIdx] = e;
        modCount++;
    }

    @Override
    public T pop() {
        if (isEmpty()) throw new NoSuchElementException("Pila vacía");
        T valor = elementos[topeIdx];
        elementos[topeIdx] = null; // Evitar memory leaks (loitering)
        topeIdx--;
        modCount++;
        
        // Reducción de capacidad opcional (Shrinking)
        if (topeIdx > 0 && topeIdx == elementos.length / 4) {
            redimensionar(elementos.length / 2);
        }
        return valor;
    }

    @Override
    public T top() {
        if (isEmpty()) throw new NoSuchElementException("Pila vacía");
        return elementos[topeIdx];
    }

    @Override
    public boolean isEmpty() {
        return topeIdx == -1;
    }

    @Override
    public int size() {
        return topeIdx + 1;
    }

    private void redimensionar(int nuevaCapacidad) {
        @SuppressWarnings("unchecked")
        T[] nuevoArr = (T[]) new Object[nuevaCapacidad];
        System.arraycopy(elementos, 0, nuevoArr, 0, topeIdx + 1);
        this.elementos = nuevoArr;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int current = topeIdx;
            private int expectedModCount = modCount;

            @Override
            public boolean hasNext() {
                if (expectedModCount != modCount) throw new ConcurrentModificationException();
                return current >= 0;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return elementos[current--];
            }
        };
    }
}
```

**Trade-off de Localidad de Referencia:**
En la arquitectura de computadoras moderna, la memoria no es un acceso uniforme (UMA). Los procesadores tienen varios niveles de cache (L1, L2, L3).
- **Spatial Locality**: Un arreglo dinámico coloca los elementos en direcciones de memoria adyacentes. Cuando el procesador pide `elementos[i]`, el hardware carga en la cache toda la línea de memoria (típicamente 64 bytes), lo que incluye `elementos[i+1]`, `elementos[i+2]`, etc.
- **Cache Hits**: Gracias a esto, acceder al siguiente elemento de la pila es órdenes de magnitud más rápido que en una estructura enlazada.

### 2.2 Pila basada en Nodos (Linked Model)

Acá, cada elemento vive en un "nodo" que tiene una referencia al elemento que está debajo.

#### Overhead y Fragmentación

Cada nodo en Java (o C++) no solo guarda el dato, sino también la referencia al siguiente.
- En una arquitectura de 64 bits, solo la referencia `siguiente` consume 8 bytes.
- A esto sumale el *header* del objeto (12-16 bytes en la JVM).
- Resultado: Guardar un `Integer` de 4 bytes puede terminar costando 32 bytes o más.

**El peligro del Loitering:** En implementaciones con nodos, es crucial que al hacer `pop()`, el nodo eliminado quede libre de referencias para que el Garbage Collector pueda reclamarlo.

```java
public class PilaEnlazada<T> implements Pila<T> {
    private Nodo<T> tope = null;
    private int tamaño = 0;

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        Nodo(T dato, Nodo<T> siguiente) {
            this.dato = dato;
            this.siguiente = siguiente;
        }
    }

    public void push(T e) {
        tope = new Nodo<>(e, tope);
        tamaño++;
    }

    public T pop() {
        if (isEmpty()) throw new NoSuchElementException();
        T dato = tope.dato;
        tope = tope.siguiente; // El nodo viejo queda para el GC
        tamaño--;
        return dato;
    }
    // ...
}
```

### 2.3 Pilas Híbridas: El Modelo Segmentado (std::deque style)

¿Podemos tener lo mejor de ambos mundos? Sí. Las pilas segmentadas usan un **arreglo de punteros a bloques**.
- Cada bloque es un arreglo de tamaño fijo (ej. 512 bytes).
- Cuando un bloque se llena, se pide uno nuevo.
- **Ventajas**: Localidad de memoria excelente (dentro del bloque) y crecimiento sin necesidad de copiar todo el arreglo anterior.
- Es la implementación por defecto de `std::stack` en la librería estándar de C++ (usando `std::deque` como contenedor base).

### 2.4 Comparativa: La Pila en el Ecosistema Real

No todos los lenguajes tratan a la pila de la misma forma. Como ingeniero, tenés que saber qué herramienta estás usando bajo el capó.

#### Java: El error de `java.util.Stack`
En Java, existe la clase `Stack`, pero **no se recomienda usarla**.
- **Problema**: Hereda de `Vector`, lo que significa que todas sus operaciones son `synchronized`. Esto añade un overhead de bloqueo incluso si estás en un solo hilo.
- **Solución moderna**: Usar `ArrayDeque`. Es más rápida, no tiene locks innecesarios y tiene mejor localidad.

#### C++: La flexibilidad de `std::stack`
En C++, `std::stack` es un **adaptador de contenedor**. No implementa la lógica de memoria, sino que envuelve a otro contenedor (por defecto `std::deque`).
- Podés cambiarlo: `std::stack<int, std::vector<int>>` usará un arreglo dinámico internamente.

#### Rust: El enfoque en la seguridad
En Rust, no existe una estructura "Stack" dedicada en la librería estándar. Se usa `Vec` (vector) y sus métodos `push` y `pop`.
- **Ventaja**: El sistema de *ownership* garantiza que no haya condiciones de carrera (Data Races) al acceder a la pila desde múltiples hilos, algo que en C++ requiere disciplina manual.

#### Python: La comodidad de las listas
En Python, las listas (`[]`) funcionan como pilas eficientes. Sin embargo, para entornos multihilo, se prefiere `collections.deque` porque permite `append` y `pop` atómicos y rápidos.

---

---

## 3. Hardware y Sistema Operativo: La Pila de Llamadas

Acá es donde las cosas se ponen serias. La CPU no usa una clase `Pila`; usa registros y direcciones de memoria física.

### 3.1 El Call Stack (Pila de Ejecución)

En arquitecturas x86-64, la pila crece **hacia abajo** (hacia direcciones de memoria menores). Existen dos registros clave:
- **RSP (Stack Pointer)**: Apunta siempre al tope actual de la pila.
- **RBP (Base Pointer / Frame Pointer)**: Apunta a la base del *frame* actual de la función.

#### Anatomía de un Stack Frame y Convenciones de Llamada

Cuando llamás a una función en C o C++ bajo la convención **System V AMD64 ABI**, ocurre lo siguiente:

1.  **Prólogo de la Función**:
    - `push rbp`: Se guarda el puntero de base del llamador.
    - `mov rbp, rsp`: El puntero de base actual pasa a ser el tope de la pila.
    - `sub rsp, N`: Se reserva espacio para variables locales (restando al RSP).

2.  **Cuerpo de la Función**: Se usan las variables locales accediendo a través de `[rbp - offset]`.

3.  **Epílogo de la Función**:
    - `mov rsp, rbp`: Se libera el espacio de las variables locales.
    - `pop rbp`: Se restaura el puntero de base del llamador.
    - `ret`: Se saca la dirección de retorno de la pila y se salta a ella.

### 3.2 Seguridad: "Smashing the Stack" (Buffer Overflow)

La pila es el objetivo #1 de los ataques de seguridad. ¿Por qué? Porque la **Dirección de Retorno** vive justo al lado de los datos del usuario.

Si tenés este código en C:
```c
void vulnerable(char *input) {
    char buffer[8];
    strcpy(buffer, input); // ¡Peligro!
}
```

Si el `input` tiene 20 bytes, los primeros 8 llenan el `buffer`, pero los siguientes sobrescriben el `RBP guardado` y, lo más grave, la **Dirección de Retorno**. Un atacante puede poner ahí la dirección de su propio código malicioso (*shellcode*) y, cuando la función haga `ret`, la CPU saltará a ejecutar el ataque.

#### Mecanismos de Defensa Modernos:
- **Stack Canaries**: El compilador inserta un valor aleatorio (el "canario") entre el buffer y la dirección de retorno. Antes de salir de la función, verifica si el canario sigue igual. Si cambió, es que hubo un desbordamiento y el programa se aborta inmediatamente.
- **DEP / NX (Data Execution Prevention)**: Marca la pila como "no ejecutable". Si la CPU intenta ejecutar código en la pila, el SO lanza una excepción.
- **ASLR (Address Space Layout Randomization)**: Mueve la base de la pila a una dirección aleatoria en cada ejecución para que el atacante no sepa a dónde saltar.

---

## 4. Aplicaciones Avanzadas: El Motor detrás del Software

Las pilas no son solo contenedores; son procesadores de estados. En esta sección vamos a profundizar en algoritmos que definen cómo se interpreta el código y cómo se gestionan las acciones del usuario.

### 4.1 Traza Detallada del Algoritmo Shunting-yard

El algoritmo de Dijkstra es fascinante porque utiliza una pila para transformar la jerarquía implícita de una expresión matemática en una secuencia lineal de ejecución (RPN).

Vamos a procesar una expresión que incluye paréntesis y potencias: `(3 + 4) * 5 ^ 2`

**Reglas de precedencia:** `^` (3) > `*`, `/` (2) > `+`, `-` (1).

| Paso | Token | Acción | Pila de Operadores | Cola de Salida (RPN) | Justificación Técnica |
| :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | `(` | Apilar | `[` `(` `]` | | Los paréntesis inician un nuevo contexto de precedencia. |
| 2 | `3` | A la salida | `[` `(` `]` | `3` | Los operandos pasan directo a la salida. |
| 3 | `+` | Apilar | `[` `(`, `+` `]` | `3` | `+` se apila sobre `(` sin conflictos. |
| 4 | `4` | A la salida | `[` `(`, `+` `]` | `3`, `4` | |
| 5 | `)` | Vaciar hasta `(` | `[` `]` | `3`, `4`, `+` | Al cerrar paréntesis, resolvemos todo lo pendiente adentro. |
| 6 | `*` | Apilar | `[` `*` `]` | `3`, `4`, `+` | La pila está vacía, `*` entra sin problemas. |
| 7 | `5` | A la salida | `[` `*` `]` | `3`, `4`, `+`, `5` | |
| 8 | `^` | Apilar | `[` `*`, `^` `]` | `3`, `4`, `+`, `5` | `^` tiene mayor precedencia que `*`, se apila encima. |
| 9 | `2` | A la salida | `[` `*`, `^` `]` | `3`, `4`, `+`, `5`, `2` | |
| 10 | (fin) | Vaciar pila | `[` `]` | `3`, `4`, `+`, `5`, `2`, `^`, `*` | Se vacía la pila de mayor a menor precedencia. |

**Resultado Final**: `3 4 + 5 2 ^ *`
Este formato es el que usa internamente una **Calculadora de Pila** o una máquina virtual como la de Java (JVM) para ejecutar operaciones aritméticas.

### 4.2 Evaluación de RPN (Reverse Polish Notation)

Una vez que tenemos la RPN, evaluarla es extremadamente simple con una pila de operandos.

```java
public double evaluarRPN(String[] tokens) {
    Pila<Double> stack = new PilaDinamica<>();
    for (String t : tokens) {
        if (esOperador(t)) {
            double a = stack.pop();
            double b = stack.pop();
            stack.push(aplicar(t, b, a)); // Ojo el orden: b op a
        } else {
            stack.push(Double.parseDouble(t));
        }
    }
    return stack.pop();
}
```

### 4.3 Undo/Redo: El Patrón Command

En aplicaciones complejas, no guardamos el "estado completo" en cada paso (sería un desperdicio de RAM). Guardamos la **diferencia**.

1.  **Undo Stack**: Guarda los comandos ejecutados. Cada comando tiene un método `unexecute()`.
2.  **Redo Stack**: Cuando deshacés algo, el comando se mueve aquí. Si el usuario hace una **nueva** acción, la pila de Redo se borra (invariante de la historia).

#### Gestión de Memoria en Historiales Largos
Si la pila de Undo crece sin control, podemos tener un `OutOfMemoryError`. Las aplicaciones profesionales usan una **Pila Acotada** o una **Eviction Policy**: cuando se llega a 1000 acciones, se elimina la más vieja (la base de la pila).

### 4.4 Pilas Monótonas: El Secreto del $O(n)$

Muchos problemas que parecen requerir $O(n^2)$ se resuelven en $O(n)$ usando una **Pila Monótona**.
- **Definición**: Es una pila donde los elementos siempre se mantienen ordenados (ya sea de forma estrictamente creciente o decreciente).
- **Cómo funciona**: Al insertar un elemento `x`, desapilamos todos los elementos que rompan el orden monótono antes de meter `x`.
- **Por qué es $O(n)$**: Aunque hay un lazo `while` adentro del `for`, cada elemento entra a la pila una vez y sale de la pila **como máximo una vez**. El costo total es $2n$ operaciones, lo cual es $O(n)$.

### 4.5 Máquinas Virtuales basadas en Pila (JVM, CPython)

A diferencia de un procesador real (que usa registros como EAX, EBX), muchas máquinas virtuales usan una **Pila de Operandos**.

Cuando escribís `a = b + c` en Java, el compilador genera un bytecode similar a este:
1. `iload_1` (Cargar `b` al tope de la pila)
2. `iload_2` (Cargar `c` al tope de la pila)
3. `iadd` (Saca los dos de arriba, los suma y apila el resultado)
4. `istore_0` (Saca el resultado y lo guarda en `a`)

**Ventaja**: El bytecode es muy compacto y fácil de interpretar en cualquier arquitectura.
**Desventaja**: Requiere más instrucciones que un modelo basado en registros.

### 4.6 Caso de Estudio: El Motor V8 y el Event Loop

En el mundo de JavaScript, se habla mucho de que es "single-threaded". La pila es la que manda aquí.
- **The Stack**: V8 apila cada llamada a función. Si una función es síncrona y tarda mucho (ej. un lazo pesado), se dice que "bloquea el hilo principal" porque la pila no puede avanzar.
- **The Event Loop**: Cuando hacés una petición asíncrona, V8 no la pone en la pila. La delega al sistema operativo y, cuando vuelve, el resultado se pone en una **Cola**. El resultado solo subirá a la Pila cuando esta esté **completamente vacía**.

---

---

## 5. Concurrencia: Escalando a Miles de Hilos

En un servidor moderno con 64 núcleos, una pila protegida por un `Lock` o un `synchronized` se convierte en un embudo.

### 5.1 La Pila de Treiber (Lock-Free)

El algoritmo de Treiber usa `Compare-And-Swap` (CAS). Es "lock-free" porque al menos un hilo siempre progresa.

```java
public class TreiberStack<T> {
    private AtomicReference<Node<T>> head = new AtomicReference<>();

    public void push(T item) {
        Node<T> newHead = new Node<>(item);
        Node<T> oldHead;
        do {
            oldHead = head.get(); // 1. Leer
            newHead.next = oldHead; // 2. Preparar
        } while (!head.compareAndSet(oldHead, newHead)); // 3. Intentar Swap
    }
}
```

### 5.2 El Fantasma ABA y su Exorcismo

El problema ABA ocurre porque el CAS solo mira el **valor** (la dirección de memoria), no la **historia**.
- Hilo 1 lee A.
- Hilo 2 saca A, mete B, saca B, mete A.
- Hilo 1 hace CAS(A, nuevo) y tiene éxito, aunque la pila cambió totalmente en el medio.

**Solución**: En Java usamos `AtomicStampedReference`. Cada vez que el puntero cambia, aumentamos un "stamp" (versión). El CAS ahora es: "Cambiá A por C solo si el puntero es A **Y** la versión es la misma".

### 5.3 Optimización: Elimination Backoff Stack

Bajo una contención extrema (cientos de hilos), el CAS de la Pila de Treiber falla constantemente porque todos intentan actualizar el mismo `head`.

**La idea de la eliminación:** Si un hilo quiere hacer `push(X)` y otro quiere hacer `pop()` al mismo tiempo, ¡ni siquiera necesitan tocar la pila central! Pueden encontrarse en un "Array de Eliminación" lateral, intercambiar el valor de `X` y ambos terminar su operación exitosamente. Esto permite que la pila escale linealmente con el número de núcleos.

---

## 6. Guía de Ejercicios de Alta Complejidad

Esta sección presenta los desafíos definitivos. No busques soluciones en internet; intentá razonar el flujo de la pila.

```{exercise}
:label: ex-p6-historigrama
**1. El Mayor Rectángulo en un Histograma.**
- **Enunciado**: Dado un arreglo de alturas de barras, encontrá el área del rectángulo más grande que se puede formar.
- **Pista**: Mantené una pila monótona creciente de índices. Si la altura actual es menor que la del tope, el rectángulo que empieza en el tope "se corta". Desapilá y calculá el área usando la altura desapilada y el ancho definido por los índices.
- **Complejidad Esperada**: $O(n)$ tiempo, $O(n)$ espacio.
```

```{exercise}
:label: ex-p6-html-validator
**2. Parser de HTML con Auto-cierre.**
- **Enunciado**: Verificá si las etiquetas de un HTML están balanceadas, pero manejando etiquetas como `<img />` o `<br />` que no requieren cierre.
- **Pista**: Usá una pila de strings. Cuando leas un tag, chequeá si termina en `/>`. Si no, apilalo. Al encontrar un tag de cierre `</...>`, comparalo con el tope.
- **Complejidad**: $O(Longitud\_HTML)$.
```

```{exercise}
:label: ex-p6-dfs-iterative
**3. DFS Iterativo con Control de Ciclos.**
- **Enunciado**: Implementá DFS sin usar la pila de llamadas de Java.
- **Pista**: Apilá el nodo inicial. Al desapilar, si no está visitado, marcalo y apilá todos sus vecinos. Para grafos muy grandes, esto evita el `StackOverflowError`.
```

```{exercise}
:label: ex-p6-stock-span
**4. Stock Span Problem.**
- **Enunciado**: Calculá para cada día cuántos días previos consecutivos el precio fue menor o igual al actual.
- **Pista**: Mantené una pila de índices con precios decrecientes. El span es la diferencia entre el índice actual y el índice que está en el tope de la pila.
- **Complejidad**: $O(n)$.
```

```{exercise}
:label: ex-p6-infix-to-postfix
**5. Shunting-Yard con Funciones.**
- **Enunciado**: Extendé el algoritmo para que soporte `sin(x)`, `max(a, b)` y potencias `^` (asociativas a derecha).
- **Pista**: Las funciones se tratan como operadores de altísima precedencia. La coma `,` actúa como un delimitador que vacía la pila hasta el paréntesis de apertura de la función.
```

```{exercise}
:label: ex-p6-rpn-evaluator
**6. Evaluador de RPN Avanzado.**
- **Enunciado**: Soportá operadores unarios como el `-` de negación (ej: `3 4 -` es 3 - 4, pero `3 -` es -3).
- **Pista**: El evaluador necesita saber cuántos operandos consume cada operador. El `-` binario consume 2, el `-` unario consume 1.
```

```{exercise}
:label: ex-p6-next-greater
**7. Next Greater Element.**
- **Enunciado**: Para cada elemento, hallá el primer mayor a su derecha.
- **Pista**: Recorré de derecha a izquierda. Mantené una pila con los elementos que podrían ser el "próximo mayor" de alguien a la izquierda.
```

```{exercise}
:label: ex-p6-min-stack
**8. Min-Stack en Espacio $O(1)$ Extra.**
- **Enunciado**: Implementá `getMin()` en $O(1)$ sin usar una pila auxiliar completa.
- **Pista**: En lugar de guardar el valor real, guardá la diferencia entre el valor y el mínimo actual. Es un truco matemático avanzado.
```

```{exercise}
:label: ex-p6-two-stacks
**9. El Array Bicefalo.**
- **Enunciado**: Meté dos pilas en un solo arreglo.
- **Pista**: Una crece desde `0` hacia adelante, la otra desde `length-1` hacia atrás. Se llenan cuando los topes se encuentran.
```

```{exercise}
:label: ex-p6-reverse-stack
**10. Inversión Recursiva.**
- **Enunciado**: Invertí una pila sin usar ninguna otra estructura, solo recursión.
- **Pista**: Usá una función `insertAtBottom` que baje un elemento hasta la base de la pila usando la pila de llamadas como almacenamiento temporal.
```

```{exercise}
:label: ex-p6-sort-stack
**11. Ordenamiento con Pila Auxiliar.**
- **Enunciado**: Ordená una pila usando solo otra pila como apoyo.
- **Pista**: Es similar al ordenamiento por inserción, pero usando el tope de las pilas para mover elementos.
```

```{exercise}
:label: ex-p6-queue-stacks
**12. Cola con Dos Pilas.**
- **Enunciado**: Simulá una cola FIFO.
- **Pista**: Una pila para "entrar" y otra para "salir". Solo pasás datos de una a otra cuando la de "salir" está vacía. Costo amortizado $O(1)$.
```

```{exercise}
:label: ex-p6-longest-parentheses
**13. Subcadena de Paréntesis más larga.**
- **Enunciado**: Encontrá la longitud de la subcadena balanceada más larga.
- **Pista**: Apilá los índices. Un `)` desapila. Si la pila queda vacía, apilá el índice actual como nueva "base".
```

```{exercise}
:label: ex-p6-simplify-path
**14. Simplify Path.**
- **Enunciado**: De `/a/./b/../../c/` a `/c`.
- **Pista**: Hacé un `split("/")`. Si el token es `..`, hacé `pop()`. Si es `.` o vacío, ignorá. Cualquier otro, `push()`.
```

```{exercise}
:label: ex-p6-decode-string
**15. Decoding Engine.**
- **Enunciado**: Decodificá `2[abc]3[cd]ef`.
- **Pista**: Necesitás dos pilas: una para los números de repetición y otra para los strings que vas formando.
```

```{exercise}
:label: ex-p6-stack-sequences
**16. Validación de Secuencias de Pop.**
- **Enunciado**: Dados dos arreglos (push y pop), ¿es válida la secuencia?
- **Pista**: Simulá el proceso. Empujá elementos y, mientras el tope coincida con el siguiente del arreglo de pop, desapilá.
```

```{exercise}
:label: ex-p6-asteroid-collision
**17. Colisión de Asteroides.**
- **Enunciado**: Simulá asteroides chocando (negativos izquierda, positivos derecha).
- **Pista**: Los choques solo ocurren cuando un positivo está a la izquierda de un negativo en la pila.
```

```{exercise}
:label: ex-p6-basic-calculator
**18. Calculadora Básica con Paréntesis.**
- **Enunciado**: Evaluá `1 + (4 + 5 + 2) - 3`.
- **Pista**: Cuando encuentres `(`, apilá el resultado acumulado y el signo, y empezá una nueva suma "fresca".
```

```{exercise}
:label: ex-p6-exclusive-time
**19. Tiempo Exclusivo de Funciones.**
- **Enunciado**: Calculá el tiempo neto de ejecución de funciones anidadas.
- **Pista**: Apilá el ID de la función. Al recibir un `end`, restá el tiempo transcurrido del tiempo de la función "padre" que está debajo en la pila.
```

```{exercise}
:label: ex-p6-binary-matrix
**20. Rectángulo Máximo en Matriz.**
- **Enunciado**: En una matriz de 0s y 1s, hallá el área más grande de 1s.
- **Pista**: Para cada fila, construí un histograma (contando 1s hacia arriba) y aplicá el algoritmo del Ejercicio 1.
```

## 7. Implementaciones de Referencia: Del Desafío al Código

Para que no quede todo en la teoría, acá tenés cómo se ven implementadas las soluciones a los dos problemas más icónicos.

### 7.1 El Algoritmo del Histograma ($O(n)$)

Este código demuestra el poder de una **Pila Monótona**.

```java
public int areaMaximaHistograma(int[] alturas) {
    Pila<Integer> stack = new PilaDinamica<>();
    int maxArea = 0;
    int n = alturas.length;

    for (int i = 0; i <= n; i++) {
        // Al final (i == n), usamos altura 0 para vaciar la pila
        int h = (i == n) ? 0 : alturas[i];

        while (!stack.isEmpty() && alturas[stack.top()] > h) {
            int altura = alturas[stack.pop()];
            int ancho = stack.isEmpty() ? i : i - stack.top() - 1;
            maxArea = Math.max(maxArea, altura * ancho);
        }
        stack.push(i);
    }
    return maxArea;
}
```

### 7.2 Shunting-Yard con Precedencia Real

Una versión simplificada pero funcional para operadores binarios.

```java
public String infixToPostfix(String expresion) {
    StringBuilder output = new StringBuilder();
    Pila<Character> stack = new PilaDinamica<>();

    for (char c : expresion.toCharArray()) {
        if (Character.isLetterOrDigit(c)) {
            output.append(c).append(" ");
        } else if (c == '(') {
            stack.push(c);
        } else if (c == ')') {
            while (!stack.isEmpty() && stack.top() != '(') {
                output.append(stack.pop()).append(" ");
            }
            stack.pop(); // Sacar '('
        } else {
            while (!stack.isEmpty() && precedencia(stack.top()) >= precedencia(c)) {
                output.append(stack.pop()).append(" ");
            }
            stack.push(c);
        }
    }
    while (!stack.isEmpty()) output.append(stack.pop()).append(" ");
    return output.toString();
}
```

## 8. El Futuro de las Pilas: Corrutinas y Go

En los lenguajes tradicionales, cada hilo tiene una pila de 1MB o 2MB. Si tenés 10,000 hilos, consumís 20GB de RAM solo en pilas vacías.

**Go (Golang)** resolvió esto con **Pilas Redimensionables**:
- Las "goroutines" empiezan con una pila minúscula de **2KB**.
- Cuando la goroutine se queda sin espacio, el *runtime* de Go reserva un bloque nuevo más grande, copia la pila vieja al nuevo lugar y actualiza todos los punteros.
- Esto permite tener **millones** de goroutines en una sola máquina, algo imposible con hilos de sistema operativo.

---

## Resumen

A lo largo de este capítulo, hemos transformado la noción simple de "apilar platos" en un arsenal de ingeniería:

1.  **Formalmente**, establecimos que una pila se define por axiomas y que podemos usar inducción estructural para garantizar su comportamiento.
2.  **En la implementación**, analizamos cómo la localidad de cache favorece a los arreglos y cómo los modelos segmentados equilibran el crecimiento.
3.  **En el hardware**, descubrimos que la pila es el mecanismo de control de la CPU y que un descuido en su gestión (Buffer Overflow) puede comprometer todo un sistema.
4.  **En algoritmos**, aprendimos que las pilas monótonas y el Shunting-yard son los motores de la eficiencia en el procesamiento de datos.
5.  **En concurrencia**, exploramos el mundo lock-free, enfrentando el problema ABA y escalando mediante la eliminación.

Dominar las pilas es entender cómo el software maneja el contexto, el tiempo y la memoria. Sin pilas, no hay recursión; sin recursión, la computación moderna simplemente no existiría.

## Próximo paso

Ahora que comprendés la política LIFO y su inmenso poder, es momento de ver el reverso de la moneda: [Colas](colas.md), donde el primero en llegar es el primero en ser atendido (FIFO).

## 1. Formalización Algebraica y Axiomática de la Pila

Para un científico de la computación, una pila no es una "lista de cosas"; es un **Sistema Formal** definido por sus leyes de interacción.

### 1.1 Axiomas de LIFO
Sea $S$ el tipo Pila y $A$ el tipo de elementos. Las operaciones se definen como:
- $empty: \to S$
- $push: S \times A \to S$
- $pop: S \to S$
- $top: S \to A$

Los axiomas fundamentales que rigen el comportamiento son:
1. $pop(push(s, a)) = s$
2. $top(push(s, a)) = a$
3. $isEmpty(empty) = true$
4. $isEmpty(push(s, a)) = false$

Estas leyes garantizan que la pila sea **determinística**. Si aplicás una secuencia de operaciones, el estado final es predecible e independiente de la representación física. Esta formalización permite aplicar **Inducción Estructural** para demostrar que un programa que usa una pila nunca violará su invariante de orden.

---

## 4. HARDWARE Y SISTEMAS OPERATIVOS: LA PILA FÍSICA

Tu programa no es el único que usa pilas. El procesador mismo está diseñado alrededor de una pila de hardware para gestionar la ejecución.

### 4.1 Registros y Stack Frames en x86-64
Cuando llamás a una función, la CPU usa la pila de hardware:
- **RSP (Stack Pointer):** Apunta al tope actual de la pila en la RAM.
- **RBP (Base Pointer):** Apunta a la base del *frame* de la función actual.
Al hacer un `call`, la CPU:
1. Mete la dirección de retorno en la pila.
2. Salta a la función.
3. Reserva espacio para variables locales restando a RSP.
**Hardware Optimization:** Las CPUs modernas tienen una unidad llamada **Stack Engine** que predice los cambios en RSP para que las operaciones de pila no frenen el pipeline de ejecución.

### 4.2 Stack Overflow: El Abismo de la Memoria
Un `StackOverflowError` no es solo un mensaje de Java. Es un evento de hardware. 
1. La pila crece hacia direcciones de memoria más bajas.
2. El SO coloca una "página de guarda" (Guard Page) al final de la región de la pila.
3. Cuando tu recursión infinita intenta escribir en la Guard Page, el hardware dispara una **Segmentación Fault**.
4. La JVM captura esta señal y la traduce a la excepción que ves en consola.
**Seguridad:** El ataque de *Stack Smashing* consiste en escribir más datos de los que entran en una variable local para pisar la dirección de retorno en la pila y ejecutar código malicioso. Las CPUs modernas usan "Canarios de Pila" y "Shadow Stacks" (en arquitecturas Intel CET) para prevenir esto.

## 5. APLICACIONES AVANZADAS: PARSING Y ALGORITMOS DE CÁLCULO

La pila es el corazón de cualquier lenguaje de programación. Sin pilas, no existiría el orden de precedencia matemático ni la recursión.

### 5.1 El Algoritmo Shunting-Yard (Dijkstra)
Diseñado por Edsger Dijkstra, este algoritmo utiliza dos pilas (o una pila y una salida) para convertir expresiones infijas (ej: `3 + 4 * 2`) a postfijas (ej: `3 4 2 * +`), eliminando la necesidad de paréntesis.
1. **Lógica de Operadores:** Si llega un operador, lo apilás. 
2. **Prioridad:** Si el operador en el tope tiene mayor o igual prioridad que el nuevo, desapilás el tope a la salida antes de apilar el nuevo.
3. **Paréntesis:** Un `(` se apila siempre. Un `)` fuerza el desapilado hasta encontrar su pareja.
**Performance:** Este algoritmo es $O(n)$ en tiempo. Es la base de los motores de cálculo de Excel, calculadoras científicas y compiladores.

### 5.2 Evaluación de RPN (Reverse Polish Notation)
Evaluar la salida de Shunting-yard es trivial con una pila:
- Si es número, `push`.
- Si es operador, `pop` dos veces, aplicás la operación y `push` el resultado.
**Ventaja:** Esta forma de evaluar expresiones es **branch-free** y lineal, lo que la hace extremadamente rápida para el hardware de la CPU.

---

## 6. ARQUITECTURAS DE UNDO/REDO Y EL PATRÓN COMMAND

En aplicaciones profesionales (Photoshop, CAD, VS Code), la pila gestiona el tiempo del usuario.

### 6.1 Gestión de Snapshots vs Deltas
- **Snapshots:** Guardamos el estado completo del objeto. Memoria ineficiente ($O(N \times Size)$).
- **Deltas (Comandos):** Guardamos solo el cambio. El comando `DeleteTextCommand` guarda el texto borrado y la posición. La pila de Undo guarda estos objetos comando.
**Estrategia:** La mayoría de los editores limitan la pila de Undo a ~1000 elementos para evitar que el consumo de memoria del historial sature el Heap de la JVM. Al superar el límite, se descarta el fondo de la pila (lo cual es una operación $O(1)$ si usás una `Deque` como base).

## 7. PILAS MONÓTONAS: LA TÉCNICA DE OPTIMIZACIÓN SUPREMA

Una **Pila Monótona** es aquella que mantiene sus elementos en orden estrictamente creciente o decreciente. 
- **Algoritmo:** Al hacer `push(x)`, desapilás todos los elementos que violen el orden monótono antes de insertar $x$.
- **Uso:** Resolver problemas de "El siguiente elemento mayor" o "El rectángulo más grande en un histograma" en tiempo lineal $O(N)$ en lugar de $O(N^2)$.
**Impacto:** Es la herramienta favorita en las entrevistas técnicas de Google/Amazon porque demuestra que entendés cómo usar una restricción de datos para eliminar la necesidad de búsquedas repetitivas.

---

## 8. CONCURRENCIA: LA PILA DE TREIBER (LOCK-FREE)

Para implementar una pila en multihilo sin locks, usamos una lista enlazada y operaciones **CAS (Compare-And-Swap)**.

### 8.1 El Problema del Bloqueo
Si usás `synchronized`, un hilo puede ser suspendido por el SO mientras tiene el lock del tope, dejando a todos los demás procesadores en espera.

### 8.2 La Solución Treiber
1. Leés el tope actual (`oldTop`).
2. Creás el nuevo nodo.
3. Hacés que `newNode.next = oldTop`.
4. Ejecutás `CAS(topRef, oldTop, newNode)`. 
5. Si falló (otro hilo cambió el tope), reintentás el bucle.
**ABA Problem:** En pilas basadas en nodos, el problema ABA ocurre si un nodo se borra y se vuelve a insertar en la misma dirección de memoria mientras un hilo está haciendo el CAS. Java lo soluciona mediante el Garbage Collector (que no reutiliza direcciones de memoria tan rápido) o usando `AtomicStampedReference`.

## 9. Laboratorio de Ejercicios: Maestría Técnica (1-20)

### Ejercicio 1: Mayor Rectángulo en Histograma (Pila Monótona)
**Consigna:** Calculá el área del mayor rectángulo en un histograma en $O(N)$.

**Resolución Detallada:**
Usamos una pila monótona creciente que guarda índices.
1. Si la altura actual es mayor al tope, apilamos.
2. Si es menor, desapilamos y calculamos el área: `altura = heights[pila.pop()]`. El ancho es la distancia entre el nuevo tope y el índice actual.
**Hardware:** El algoritmo es $O(N)$ porque cada índice se apila y se desapila exactamente una vez. La localidad espacial es excelente porque procesamos el arreglo linealmente.

### Ejercicio 2: Shunting-yard Trace
**Consigna:** Convertí `(3 + 4) * 5` a postfija.

**Resolución Detallada:**
1. `(`: Pila = `[(]`. Salida = ``.
2. `3`: Salida = `3`.
3. `+`: Pila = `[(, +]`.
4. `4`: Salida = `3, 4`.
5. `)`: Desapilamos hasta `(`. Pila vacía. Salida = `3, 4, +`.
6. `*`: Pila = `[*]`.
7. `5`: Salida = `3, 4, +, 5`.
8. Final: Salida = `3, 4, +, 5, *`.

### Ejercicio 3: Validación de Delimitadores
**Consigna:** Validá `{[()]}`.

**Resolución Detallada:**
Cada apertura se apila. Al encontrar un cierre, el tope DEBE ser su pareja. Si la pila está vacía al terminar, la cadena es válida. Es la base de los motores de sintaxis de IDEs como IntelliJ.

### Ejercicio 4: Evaluación de RPN
**Consigna:** Evaluá `10 5 + 2 *`.

**Resolución Detallada:**
1. `10`, `5` -> Pila = `[10, 5]`.
2. `+` -> `pop(5)`, `pop(10)`. `10+5=15`. `push(15)`.
3. `2` -> Pila = `[15, 2]`.
4. `*` -> `pop(2)`, `pop(15)`. `15*2=30`.
**Resultado:** 30.

### Ejercicio 5: Próximo Elemento Mayor
**Consigna:** Para `[4, 5, 2, 10]`, hallá el próximo mayor para cada elemento.

**Resolución Detallada:**
Usamos pila monótona decreciente.
- `4`: apilo.
- `5`: desapilo 4 (su mayor es 5), apilo 5.
- `2`: apilo 2 (menor que 5).
- `10`: desapilo 2 (su mayor es 10), desapilo 5 (su mayor es 10).
**Resultado:** `[5, 10, 10, -1]`.

### Ejercicio 6: Inversión de Pila (In-place Recursivo)
**Consigna:** Invertí una pila sin usar otras estructuras, solo recursión.

**Resolución Detallada:**
`reverse()` saca el tope, llama recursivo a `reverse()`, y luego usa `insertAtBottom()`. Es un ejercicio académico para entender cómo el Call Stack de la CPU reemplaza a la estructura de datos explícita.

### Ejercicio 7: Implementación de Pila con 2 Colas
**Consigna:** Simulá LIFO usando FIFO.

**Resolución Detallada:**
Para el `push`, metés el dato en `Q2`, pasás todo de `Q1` a `Q2`, y luego intercambiás los nombres de las colas. Es una operación $O(N)$ carísima que muestra por qué las restricciones de acceso importan.

### Ejercicio 8: Pila con Mínimo en O(1)
**Consigna:** Diseñá una pila que devuelva el mínimo en tiempo constante.

**Resolución Detallada:**
Mantenés una "pila de mínimos" paralela. Cada vez que apilás $X$, apilás en la segunda pila `min(X, actualMin)`. Consume el doble de memoria pero garantiza $O(1)$ para análisis de datos en tiempo real.

### Ejercicio 9: Historial de Navegador
**Consigna:** Simulá los botones "Atrás" y "Adelante".

**Resolución Detallada:**
Usás dos pilas: `backStack` y `forwardStack`. Al hacer click en un link, `push` a `backStack` y vaciás `forwardStack`. Al ir atrás, `pop` de `backStack` y `push` a `forwardStack`.

### Ejercicio 10: Sort Stack
**Consigna:** Ordená una pila usando solo otra pila auxiliar.

**Resolución Detallada:**
Sacás de la original. Mientras el tope de la auxiliar sea mayor al sacado, devolvés de la auxiliar a la original. Luego apilás el sacado en la auxiliar. Es un algoritmo de ordenamiento por inserción sobre una restricción LIFO.

### Ejercicio 11: Call Stack Trace
**Consigna:** Dibujá el estado del stack de la CPU para `fibonacci(3)`.

### Ejercicio 12: Simulación de Recursión
**Consigna:** Implementá un DFS iterativo usando una pila explícita.

### Ejercicio 13: Paréntesis con Pesos
**Consigna:** Validá expresiones donde `[` vale más que `(`.

### Ejercicio 14: Pila de Treiber (ABA Proof)
**Consigna:** Demostrá cómo un número de versión evita el problema ABA en la pila de Treiber.

### Ejercicio 15: Segmented Stack
**Consigna:** Analizá la performance de una pila que reserva memoria en bloques de 4KB (páginas de SO).

### Ejercicio 16: Undo masivo
**Consigna:** Diseñá la política de purga de una pila de Undo que no puede superar 500MB de RAM.

### Ejercicio 17: Infix to RPN with Functions
**Consigna:** Extendé Shunting-yard para soportar `sin()`, `cos()`.

### Ejercicio 18: Stack Smashing Defence
**Consigna:** Explicá cómo un "Canary" de pila detecta un buffer overflow.

### Ejercicio 19: Longest Valid Parentheses
**Consigna:** Encontrá la longitud de la subcadena de paréntesis más larga válida.

### Ejercicio 20: Monotonic Stack for Trapping Rain Water
**Consigna:** Resolvé el problema de "Atrapando agua de lluvia" en $O(n)$ usando pilas.

---

## 10. PILAS EN EL DESARROLLO DE COMPILADORES: LA STACK MACHINE

La mayoría de las máquinas virtuales modernas (como la JVM o CPython) son **Stack-based Machines**. 
1. **Lógica de Ejecución:** A diferencia de una CPU física que usa registros (`rax`, `rbx`), una Stack Machine realiza todas sus operaciones en el tope de una pila de operandos.
2. **Ejemplo:** Para hacer `a = b + c`, la JVM genera:
   - `ILOAD b` (mete b en la pila)
   - `ILOAD c` (mete c en la pila)
   - `IADD` (saca los dos topes, los suma, y mete el resultado)
   - `ISTORE a` (saca el tope y lo guarda en a)
**Ventaja:** Este diseño simplifica enormemente la generación de código y permite que el compilador sea agnóstico del hardware subyacente. La pila es la abstracción suprema del cómputo.

---

## 11. ANÁLISIS DE P99 Y JITTER CAUSADO POR PILAS MASIVAS

Cuando usás una pila basada en nodos (`LinkedList` style) y crece hasta millones de elementos:
1. **Pausas de GC:** El Garbage Collector debe rastrear cada nodo. Si tenés una pila de 10 millones de objetos, el GC tardará milisegundos en escanearla, inyectando **Jitter** en tu aplicación.
2. **Allocation Pressure:** Si hacés `push/pop` millones de veces por segundo, estás saturando los TLABs de la JVM, obligando a recolectas constantes.
**Solución Industrial:** Usá una pila basada en un arreglo primitivo (`long[]`) y gestioná la capacidad manualmente. Esto vuelve a la pila invisible para el GC y reduce la latencia p99 a microsegundos.

---

## 12. GLOSARIO TÉCNICO DE PILAS (Ampliación)

- **Backtracking:** Técnica algorítmica que usa una pila para recordar puntos de decisión y volver atrás si una rama falla.
- **Call Stack:** Pila mantenida por el SO/JVM para gestionar las llamadas a funciones y sus variables locales.
- **LIFO (Last-In, First-Out):** Política de acceso donde el elemento más joven es el primero en ser procesado.
- **Stack Engine:** Unidad de hardware en CPUs modernas que optimiza los accesos al registro RSP.
- **Treiber Stack:** Implementación de pila concurrente que no usa bloqueos, basándose en la instrucción CAS.
- **Underflow:** Error que ocurre al intentar desapilar un elemento de una pila que ya está vacía.

---

## BIBLIOGRAFÍA RECOMENDADA

1. **"The Art of Computer Programming"** (Knuth): Análisis formal de la recursión y el stack.
2. **"Compilers: Principles, Techniques, and Tools"** (Dragon Book): Para entender el Shunting-yard y la generación de código de pila.
3. **"Intel 64 and IA-32 Architectures Software Developer's Manual"**: Para los detalles microscópicos de RSP y RBP.

### Ejercicio 1: Mayor Rectángulo en Histograma (Pila Monótona) (Ampliación)
**Resolución Detallada:**
1. **El Problema de la Bruta:** Comparar cada par de barras es $O(N^2)$.
2. **Pila Monótona:** Guardamos índices de barras. Si `heights[i]` es menor que el tope, significa que encontramos el límite derecho del rectángulo que tiene la altura del tope.
3. **Cálculo:** `h = heights[pila.pop()]`. El ancho es `i - pila.peek() - 1`.
4. **Hardware:** Al procesar el arreglo en una sola pasada lineal, el prefetcher de la CPU mantiene los datos en L1. El uso de la pila (que también es un bloque contiguo en el heap) minimiza el tráfico del bus de memoria.
**Análisis:** Es el algoritmo base para sistemas de visión artificial que detectan objetos rectangulares en nubes de puntos 1D.

### Ejercicio 11: Call Stack Trace (Física de la Recursión)
**Consigna:** Dibujá el estado del stack de la CPU para `fibonacci(3)`.

**Resolución Detallada:**
1. `main` llama a `fib(3)`. Se apila frame `fib(3)`. RSP baja.
2. `fib(3)` llama a `fib(2)`. Se apila frame `fib(2)`.
3. `fib(2)` llama a `fib(1)`. Retorna 1. Se desapila frame `fib(1)`.
4. `fib(2)` llama a `fib(0)`. Retorna 0. Se desapila frame `fib(0)`.
5. `fib(2)` suma y retorna 1. Se desapila frame `fib(2)`.
6. `fib(3)` llama a `fib(1)`. Retorna 1. Se desapila frame `fib(1)`.
7. `fib(3)` retorna 2.
**Impacto:** Cada llamada consume ciclos de CPU en el prólogo y epílogo de la función (guardar registros, mover RSP). La pila es el mecanismo que permite al software "recordar" de dónde vino.

### Ejercicio 12: Simulación de Recursión (DFS Iterativo)
**Consigna:** Implementá un DFS iterativo usando una pila explícita y compará el consumo de memoria.

**Resolución Detallada:**
```java
Stack<Node> s = new Stack<>();
s.push(root);
while (!s.isEmpty()) {
    Node n = s.pop();
    process(n);
    for (Node child : n.children) s.push(child);
}
```
**Análisis:** La versión iterativa usa memoria en el **Heap** (donde vive la estructura `Stack`), mientras que la recursiva la usa en el **Thread Stack**.
**Ventaja:** Podés recorrer árboles de profundidad infinita sin que el sistema operativo mate tu proceso con un `StackOverflow`, ya que el Heap suele ser Gigabytes más grande que el Stack.

### Ejercicio 20: Trapping Rain Water (Pila Monótona Decreciente)
**Consigna:** Resolvé el problema de "Atrapando agua de lluvia" en $O(n)$.

**Resolución Detallada:**
1. Mantenemos una pila de índices con alturas decrecientes.
2. Si la altura actual es mayor al tope, encontramos una "fosa".
3. Desapilamos la fosa, calculamos el área usando el nuevo tope (pared izquierda) y el índice actual (pared derecha).
**Mecánica:** Estamos usando la pila para "llenar" los huecos de la secuencia de izquierda a derecha.
**Hardware:** El algoritmo es $O(n)$ porque cada gota de agua virtual es procesada una sola vez. Es la técnica más eficiente para simulaciones hidráulicas en 1D.

---

## 31. PILAS EN EL DESARROLLO DE LENGUAJES: FORTH Y POSTSCRIPT

Existen lenguajes donde no hay variables, solo pilas. **Forth** y **PostScript** (el lenguaje que usa tu impresora para dibujar PDFs) son los mejores ejemplos.
- **Lógica de Sufijo:** Para sumar 1 y 2, escribís `1 2 add`. 
- **La Pila de Diccionario:** Además de la pila de datos, estos lenguajes usan una pila para guardar las definiciones de funciones.
- **Influencia Moderna:** Esta filosofía "pila-céntrica" es la que heredó la **WebAssembly (Wasm)**, el estándar para correr código de alto rendimiento en el navegador. Una pila es la forma más compacta de representar cómputo en binario.

---

## 32. ANÁLISIS DE HARDWARE: TLB Y PAGE FAULTS EN PILAS MASIVAS

Aunque solemos pensar en las pilas como estructuras pequeñas (1MB), existen problemas donde la pila de datos crece a Gigabytes (ej: análisis de series temporales con pilas monótonas).
1. **Localidad Temporal:** La pila es la estructura más amigable para la caché. Casi siempre accedés al tope, que suele estar en L1.
2. **El Problema del Salto:** Si una operación te obliga a desapilar millones de elementos, estás recorriendo la RAM en sentido inverso. 
3. **TLB Misses:** Si la pila está basada en nodos dispersos, cada `pop` masivo dispara un TLB Miss. 
**Recomendación:** Para pilas de escala industrial, usá siempre arreglos contiguos. La CPU puede predecir el acceso descendente mucho mejor que el salto entre nodos.

---

## GLOSARIO ENCICLOPÉDICO DE PILAS (Ampliación Final)

1. **Activation Record:** Bloque de datos en la pila de llamadas que contiene los parámetros y variables de una función.
2. **Bottom-of-Stack:** El elemento más viejo de la pila. Acceder a él rompe el contrato LIFO y requiere una secuencia general.
3. **Control Stack:** Pila usada por el procesador para manejar las interrupciones de hardware.
4. **Depth-of-Stack:** Medida de cuántos elementos hay acumulados. Crucial para evitar desbordamientos.
5. **Frame Pointer:** Registro de CPU que apunta a una dirección fija dentro del stack frame para facilitar el acceso a variables locales.
6. **Guard Page:** Página de memoria virtual sin permisos de escritura al final de la pila para detectar desbordamientos.
7. **Infix-to-Postfix:** Proceso de traducción de lenguaje humano a lenguaje de máquina de pila.
8. **Monotonic Stack:** Técnica para mantener elementos ordenados y resolver problemas de búsqueda en $O(n)$.
9. **Return Address:** Dirección guardada en la pila al llamar a una función para saber a dónde volver al terminar.
10. **Shadow Stack:** Pila secundaria de hardware usada para proteger las direcciones de retorno de ataques de hacking.
11. **Stack Engine:** Circuito de la CPU que realiza sumas/restas al puntero de pila en paralelo a la ejecución normal.
12. **Thread Stack:** Espacio de memoria dedicado a la pila de un hilo específico. Típicamente de 1MB en la JVM.
13. **Top-of-Stack:** El único elemento legalmente visible y modificable según el contrato LIFO.
14. **Unwinding:** Proceso de limpiar la pila de llamadas cuando ocurre una excepción o un error fatal.
15. **Zero-Address Architecture:** Diseño de computadoras donde las instrucciones no tienen operandos porque siempre operan sobre la pila.

---

---

## CODA: LA PILA COMO METÁFORA DEL TIEMPO

Si la vida fuera un arreglo, podríamos volver a cualquier momento con solo conocer el índice. Pero la vida se parece mucho más a una pila. Solo tenemos acceso al presente, al tope de nuestra experiencia. El pasado queda sepultado por capas de nuevas memorias, y solo podemos recuperarlo desandando el camino, una pieza a la vez. 

En ingeniería, esta limitación es nuestra salvación. Nos obliga a enfocarnos, a ser prolijos y a respetar el orden en que las cosas deben suceder. Esperamos que este recorrido masivo por la arquitectura y la algoritmia de las pilas te haya dado no solo las herramientas para ser un mejor programador, sino también una nueva forma de ver la estructura misma de la realidad. Construí sistemas que respiren, que fluyan y que, sobre todo, sepan siempre cómo volver a casa.

## Próximo paso

Habiendo dominado la ley del "Último en Entrar, Primero en Salir", es momento de explorar la estructura que respeta la justicia del tiempo: las [Colas](colas.md).

---

## 13. ESTUDIO DE CASO: EL MOTOR DE RENDERING DE POSTSCRIPT

PostScript no es solo un lenguaje de impresora; es una máquina de Turing completa que funciona exclusivamente con pilas.
1. **La Pila de Gráficos:** Para dibujar un círculo rojo en (100, 100), PostScript hace:
   `100 100 50 0 360 arc`
   `1 0 0 setrgbcolor`
   `fill`
2. **Transformaciones de Coordenadas:** Cuando rotás un dibujo, PostScript empuja la matriz de transformación actual a la **GState Stack** (Graphics State Stack). Una vez que terminás de dibujar, hacés `grestore` y la pila restaura la escala y rotación anterior.
**Mecánica de Memoria:** Al usar una pila para el estado gráfico, PostScript permite anidar transformaciones (ej. una mano que rota sobre un brazo que rota sobre un cuerpo) de forma matemáticamente elegante y con un uso de memoria constante.

---

## 14. ANÁLISIS DE SEGURIDAD: EL ATAQUE ROP (RETURN-ORIENTED PROGRAMMING)

A medida que las defensas contra el *Stack Smashing* mejoraron (como el bit NX que impide ejecutar código en la pila), los hackers inventaron el **ROP**.
1. **Gadgets:** El atacante busca pedazos de código legítimo en la memoria del programa que terminen en la instrucción `ret` (gadgets).
2. **Encadenamiento:** El atacante pisa la pila de llamadas con una secuencia de direcciones de estos gadgets.
3. **Ejecución:** Cada `ret` saca la siguiente dirección de la pila de llamadas y salta a ella.
**Impacto:** El atacante "programa" usando la pila de llamadas como si fuera una cinta de instrucciones. Es el recordatorio definitivo de que la pila de llamadas es el control de flujo de la computadora y quien domina la pila, domina la máquina.

---

## 15. DEDUCCIÓN MATEMÁTICA: LA COMPLEJIDAD DE LA PILA DE TREIBER

Sea $H$ el número de hilos compitiendo por el tope.
1. **Costo Ideal:** $O(1)$. En ausencia de contención, el CAS tiene éxito al primer intento.
2. **Bajo Contención:** Si $H$ hilos intentan un `push` al mismo tiempo, solo 1 tiene éxito. Los otros $H-1$ deben reintentar.
3. **Probabilidad:** En el peor caso, el costo promedio de una operación bajo contención extrema es $O(H)$.
**Optimización (Elimination Backoff):** Para escalar a miles de hilos, se usa una "arena de eliminación". Si un hilo quiere hacer `push(X)` y otro `pop()`, se encuentran en la arena y el dato pasa de mano en mano sin tocar el tope de la pila central, reduciendo la contención de $O(H)$ a $O(1)$ efectivo.

---

## RESUMEN FINAL PARA EL EXAMEN (Ampliación)

1. **Dualidad LIFO:** La pila es tanto un TAD como un mecanismo de hardware (RSP).
2. **Parsing:** El algoritmo de Dijkstra (Shunting-yard) es la aplicación industrial más importante.
3. **Recursión:** Toda recursión es una pila implícita. Si el árbol es profundo, usá una pila explícita en el Heap.
4. **Concurrencia:** La Pila de Treiber es la base de las colas de alto rendimiento.

## Próximo paso

---

## 33. DUELO DE GIGANTES: HP VS TI Y LA REVOLUCIÓN DE LA PILA

En los años 70, hubo una guerra cultural en el mundo de las calculadoras.
1. **TI (Texas Instruments):** Usaba el sistema algebraico estándar (`3 + 4 =`). Requería paréntesis y un parser complejo.
2. **HP (Hewlett-Packard):** Introdujo la **Notación Polaca Inversa (RPN)**. No había botón `=`. Escribías `3 ENTER 4 +`.
**Por qué ganó HP en ingeniería:** Los ingenieros de la NASA y el MIT amaban RPN porque permitía ver los resultados intermedios en la pila y eliminaba la ambigüedad de los paréntesis. Programar una calculadora RPN es programar directamente sobre la estructura de datos, lo que la hace masivamente más eficiente para cálculos complejos.

---

## 34. GLOSARIO TÉCNICO DE PILAS (Nivel Arquitecto)

1. **Alignment Padding:** Espacio extra en el stack frame para que las variables empiecen en direcciones múltiplos de 16 bytes (requerido por instrucciones SIMD).
2. **Back-Pointer (Frame):** El valor del registro RBP guardado en la pila, permitiendo al debugger reconstruir el árbol de llamadas.
3. **Canary Value:** Valor aleatorio colocado antes de la dirección de retorno para detectar desbordamientos de buffer antes de que la función retorne.
4. **Deep-Stack-Analysis:** Herramienta de profiling que detecta si tu aplicación está desperdiciando ciclos de CPU en llamadas a funciones triviales demasiado anidadas.
5. **Frame Pointer Omission (FPO):** Optimización del compilador que elimina el uso de RBP para ganar un registro extra, a costa de hacer el debugging mucho más difícil.
6. **Interrupt Stack:** Pila especial usada por el Kernel para manejar señales de hardware sin contaminar la pila del proceso de usuario.
7. **LIFO- Justicia Temporal:** Concepto filosófico de las pilas donde el elemento más nuevo es el más importante, a diferencia de las colas donde prima la antigüedad.
8. **Monomorphic Call Site:** Optimización de la JVM donde un sitio de llamada siempre apunta al mismo método, permitiendo un inlining agresivo y eliminando el overhead de la pila.
9. **Operand Stack:** Pila interna de la JVM donde se realizan las operaciones aritméticas y de carga de datos.
10. **Red Zone:** Espacio de 128 bytes debajo de RSP en la arquitectura AMD64 que una función puede usar sin mover el puntero de pila, ahorrando instrucciones.

---

## EPÍLOGO: LA LEY DEL TOPE

Dominar la pila es entender la elegancia de la restricción. Al limitar el acceso a un solo punto, ganamos una claridad estructural que permite construir desde simples calculadoras hasta los compiladores más complejos del mundo. Que la ley del tope nunca te limite, sino que te brinde la seguridad de que el último paso es siempre el primero hacia la solución.

## Próximo paso

---

## 35. ANALISIS DE P99 Y JITTER: EL IMPACTO DE LA PILA DE LLAMADAS

A veces, tu aplicación tiene picos de latencia de 100ms que no son culpa del GC.
1. **TTSP (Time To Safepoint):** Para hacer un GC o un dump de hilos, la JVM debe frenar todos los hilos en un punto seguro.
2. **El Problema:** Si un hilo está haciendo un lazo matemático intenso con una pila de llamadas muy profunda, el JIT puede haber eliminado los chequeos de safepoint para ganar velocidad.
3. **Hardware:** El hilo sigue corriendo ignorando la señal de freno, dejando a todos los demás hilos (y al usuario) esperando. El análisis de la pila de llamadas es vital para detectar estos "secuestros" de la JVM y asegurar una latencia p99 estable.

---

## 36. TEORÍA DE LENGUAJES FORMALES: EL AUTÓMATA DE PILA (PDA)

En la Jerarquía de Chomsky, los lenguajes de Tipo 2 (Libres de Contexto) no pueden ser reconocidos por una simple máquina de estados finitos. Requieren una pila.
1. **Memoria Infinita:** A diferencia de una cinta de Turing que es aleatoria, la pila provee una memoria infinita pero restringida.
2. **Aplicación:** Si un lenguaje permite anidamiento (como HTML o Java), **matemáticamente** necesitás una pila para parsearlo. Por eso la pila es la frontera entre los lenguajes triviales (RegEx) y la verdadera programación.

---

## 40. RESUMEN FINAL DEL TRATADO

- **Hardware:** La pila es el mecanismo de control de flujo de la CPU.
- **TAD:** LIFO es la restricción que habilita la simplicidad.
- **Industria:** Desde el parseo de JSON hasta el Undo de Photoshop, la pila es omnipresente.
- **Riesgo:** Un mal manejo de la profundidad de pila (recursión) mata al sistema; un mal manejo de la localidad (nodos) mata al rendimiento.

## Próximo paso

### Ejercicio 1: Mayor Rectángulo en Histograma (Pila Monótona) (Ampliación Maestro)
**Análisis de Microarquitectura:**
Cuando la pila desapila porque encontró una barra menor, la CPU está ejecutando un flujo de control altamente dependiente de los datos. Si el histograma tiene una forma de "serrucho", el **Branch Predictor** de la CPU va a fallar constantemente, vaciando el pipeline. Sin embargo, como el algoritmo es $O(n)$, la reducción en la cantidad de instrucciones compensa con creces los fallos de predicción.

### Ejercicio 2: Shunting-yard Trace (Ampliación)
**Mecánica de Compilador:**
Este algoritmo es el primer paso del **Lexical Analysis**. Al convertir a postfija, estás eliminando la ambigüedad del lenguaje humano. La pila de operadores actúa como una memoria de corto plazo que permite al compilador "posponer" una decisión hasta tener toda la información necesaria de la expresión.

### Ejercicio 3: Validación de Delimitadores (Ampliación)
**Análisis de Memoria:**
Para validar un archivo JSON de 1GB, no necesitás cargar el archivo entero. Podés usar un **Stream Parser** (como Jackson Afterburner) que lee byte a byte y solo mantiene en la pila los delimitadores abiertos. Una estructura de 1GB de datos anidados podría validarse usando solo unos pocos KB de RAM para la pila de llaves.

### Ejercicio 4: Evaluación de RPN (Ampliación)
**Física del Cómputo:**
Evaluar RPN es el modo de operación más eficiente para una CPU porque los datos están en el orden exacto en que los registros los necesitan. No hay saltos de memoria de ida y vuelta. Cada operación `pop` extrae datos que probablemente ya están en la caché L1 porque fueron insertados hace apenas unos nanosegundos.

### Ejercicio 5: Próximo Elemento Mayor (Ampliación)
**Algoritmia Avanzada:**
Este patrón se conoce como "Pila Monótona Decreciente". Es vital en algoritmos de **Trading de Alta Frecuencia** para detectar cambios bruscos en el precio de una acción. Al mantener solo los elementos que "miran" hacia el futuro, eliminás el ruido estadístico de la secuencia de precios.

### Ejercicio 6: Inversión de Pila (Ampliación)
**Análisis de Stack vs Heap:**
Aunque este ejercicio es elegante, recordá que en Java el stack de cada hilo tiene apenas 1MB. Si la pila tiene 10.000 elementos, este algoritmo destruirá tu aplicación con un error de memoria. Es un recordatorio de que la elegancia matemática debe negociar con los límites físicos del hardware.

### Ejercicio 7: Implementación de Pila con 2 Colas (Ampliación)
**Teoría de la Computación:**
Este ejercicio demuestra que LIFO y FIFO son potencias de cómputo equivalentes, pero con eficiencias opuestas. Forzar una estructura a comportarse como su opuesta es el ejemplo perfecto de una **Abstracción con Fugas** (Leaky Abstraction): el contrato se cumple, pero la performance se desangra.

### Ejercicio 8: Pila con Mínimo en O(1) (Ampliación)
**Diseño de Sistemas:**
Esta técnica se usa en los motores de bases de datos para calcular estadísticas en tiempo real sobre ventanas deslizantes. Al pagar un pequeño costo en memoria (duplicar la pila), ganás la capacidad de responder consultas analíticas instantáneamente, lo cual es el trade-off preferido en el mundo del **Big Data**.

### Ejercicio 9: Historial de Navegador (Ampliación)
**Arquitectura de UI:**
Este patrón es la base de los frameworks modernos como React o Redux. La pila de navegación no es solo una lista de URLs; es una pila de "Estados de Aplicación". Al ir atrás, estás realizando un "Time-Travel Debugging", recreando el pasado a partir de la información guardada en el tope.

### Ejercicio 10: Sort Stack (Ampliación)
**Análisis de Ordenamiento:**
Aunque este algoritmo es $O(n^2)$, su simplicidad lo hace ideal para ordenar colecciones pequeñas en sistemas con memoria de programa (ROM) muy limitada, donde el código de un Merge Sort no entraría físicamente en el chip.

---

## 41. TABLA COMPARATIVA GIGANTE: LAS RESTRICCIONES DE ACCESO

Para cerrar este tratado de pilas, comparamos todas las estructuras restringidas según métricas de ingeniería real.

| Estructura | Política | Operación Clave | Falla en Vacío | Falla en Lleno | Localidad |
| :--- | :--- | :--- | :---: | :---: | :---: |
| **Pila Array** | LIFO | `push/pop` | Underflow | Overflow | Excelente |
| **Pila Enlazada** | LIFO | `push/pop` | Underflow | No (RAM) | Nula |
| **Treiber Stack** | LIFO | `CAS` | Underflow | No | Baja |
| **Call Stack (CPU)** | LIFO | `call/ret` | Fatal | StackOverflow | Máxima |

### Criterios de Selección:
1. **Pila de Datos:** Usá Arreglos para performance, Listas para flexibilidad total.
2. **Pila de Control:** Reservada para el sistema operativo y el Call Stack.
3. **Pila Concurrente:** Treiber es el estándar. Si hay mucha contención, usá Deques.

---

## 42. CONCLUSIÓN FINAL: EL EQUILIBRIO DEL LIFO

Hemos visto que la pila no es solo una estructura de datos; es el andamiaje sobre el que se construye el pensamiento recursivo y el control de hardware. Al limitar el acceso, ganamos seguridad y velocidad. No permitas que la aparente sencillez de un `pop` te engañe: debajo de esa instrucción hay décadas de ingeniería en microarquitectura y teoría de lenguajes.

---

## EPÍLOGO: LA ELEGANCIA DE LA ÚLTIMA PIEZA

Dominar la pila es, en última instancia, aprender a confiar en que la última decisión tomada es la que debe guiar el presente. Hemos visto cómo una simple restricción puede transformarse en una entidad matemática pura mediante los axiomas LIFO, o en una pieza de ingeniería de precisión que negocia con los registros RSP y RBP del hardware.

No te quedes con la superficie. La próxima vez que veas un historial de navegación, una llamada a función o un sistema de "deshacer", recordá que estás frente a la herencia de Dijkstra y Turing. Que la búsqueda de la claridad sea tu brújula, pero que la comprensión de las restricciones sea tu ancla. La informática es una disciplina de capas, y hoy has descendido hasta las raíces mismas de la ejecución secuencial. Construí con sabiduría, medí con rigor y nunca dejes de preguntarte qué sucede debajo del capó de tus abstracciones. Que tus pilas nunca desborden y tu lógica siempre encuentre su camino de retorno.

## Próximo paso

---

## 43. ANEXO: TABLA DE COSTOS DE HARDWARE

| Operación | Ciclos de CPU | Latencia Estimada |
| :--- | :---: | :---: |
| Acceso a RSP | 1 | 0.25ns |
| Fallo en L1 | 4 | 1ns |
| Fallo en L2 | 12 | 3ns |
| Fallo en L3 | 40 | 10ns |
| Fallo en RAM | 200+ | 50-100ns |

**Nota:** Estos valores son aproximados para una arquitectura x86_64 de última generación. Reflejan por qué mantener la pila en los niveles superiores de la caché es vital para la performance del sistema completo.

## Próximo paso

Habiendo dominado la ley del "Último en Entrar, Primero en Salir", es momento de explorar la estructura que respeta la justicia del tiempo: las [Colas](colas.md).

