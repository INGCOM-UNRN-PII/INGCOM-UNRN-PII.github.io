---
title: "Especificaciones algebraicas completas 1"
description: "Definiciones formales con axiomas, generadores, modificadores, observadores y contratos para estructuras fundamentales."
---

(estructuras-tda)=
# Especificaciones Algebraicas completas para secuencias


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

Este capítulo cataloga las especificaciones algebraicas rigurosas de las estructuras de datos más utilizadas en programación. Cada especificación integra:

- **Sorts y Signatura:** tipos abstractos y operaciones tipadas.
- **Generadores, Modificadores, Observadores:** clasificación de operaciones.
- **Axiomas:** ecuaciones que definen semántica.
- **Invariantes:** propiedades que mantiene la estructura.
- **Contratos:** precondiciones, postcondiciones, invariantes de representación.

Estas especificaciones son **implementación-agnósticas**: funcionan con arrays dinámicos, listas enlazadas, o cualquier representación que respete los axiomas.

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, se espera que el estudiante pueda:

1. Reconocer la especificación algebraica de cualquier estructura de datos fundamental.
2. Validar que una implementación concreta respeta los axiomas de su TDA.
3. Derivar propiedades y comportamientos a partir de axiomas.
4. Diseñar casos de prueba basados en axiomas.
:::

:::{note} Prerrequisitos

Este capítulo construye sobre [Introducción a Tipos de Datos Abstractos](./1_intro_tda.md). Necesitás estar familiarizado con sorts, operaciones, axiomas, generadores/modificadores/observadores y demostraciones algebraicas.
:::

---

## TDA Array (Arreglo)

Un arreglo es una secuencia **estática** de elementos de igual tipo, accesibles mediante índices enteros en rango $[0, n)$ donde $n$ es el tamaño.

### Sorts y Signatura

$$\begin{align}
\text{Sorts:} \quad & \mathtt{Array}, \mathtt{Element}, \mathtt{Bool}, \mathbb{N} \\
\\
\text{Generadores:} \\
\quad & \text{empty} : \to \mathtt{Array} \\
\quad & \text{create} : \mathbb{N} \to \mathtt{Array} \quad \text{(crea arreglo de tamaño fijo)} \\
\\
\text{Modificadores:} \\
\quad & \text{set} : \mathtt{Array} \times \mathbb{N} \times \mathtt{Element} \to \mathtt{Array} \\
\\
\text{Observadores:} \\
\quad & \text{get} : \mathtt{Array} \times \mathbb{N} \to \mathtt{Element} \\
\quad & \text{length} : \mathtt{Array} \to \mathbb{N} \\
\quad & \text{eq} : \mathtt{Array} \times \mathtt{Array} \to \mathtt{Bool}
\end{align}$$

### Axiomas

**Axioma 1 (length de empty):**
$$\text{length}(\text{empty}) = 0$$

**Axioma 2 (length de create):**
$$\text{length}(\text{create}(n)) = n \quad \forall n \in \mathbb{N}$$

**Axioma 3 (get de set con índice coincidente):**
$$\text{get}(\text{set}(a, i, v), i) = v \quad \forall a, i, v$$

**Axioma 4 (get de set con índice distinto):**
$$i \neq j \implies \text{get}(\text{set}(a, i, v), j) = \text{get}(a, j)$$

**Axioma 5 (igualdad de arreglos):**
$$\text{eq}(a_1, a_2) = \text{true} \iff \text{length}(a_1) = \text{length}(a_2) \land \forall i < \text{length}(a_1), \text{get}(a_1, i) = \text{get}(a_2, i)$$

### Invariante

$$I(\text{Array}) : \forall a, \; \text{length}(a) \geq 0 \land \forall i, \; 0 \leq i < \text{length}(a)$$

El tamaño es siempre no negativo y los índices válidos están en rango.

### Contratos

| Operación | Precondición | Postcondición | Complejidad |
|-----------|--------------|---------------|-------------|
| `create(n)` | ninguna | $\text{length}(\text{result}) = n$ | $O(n)$ tiempo, $O(n)$ espacio |
| `set(a, i, v)` | $0 \leq i < \text{length}(a)$ | $\text{get}(\text{result}, i) = v$ | $O(1)$ tiempo, $O(1)$ espacio |
| `get(a, i)` | $0 \leq i < \text{length}(a)$ | devuelve elemento en posición $i$ | $O(1)$ tiempo, $O(1)$ espacio |

---

## TDA Stack (Pila)

Una pila es una secuencia **dinámica** LIFO (last-in, first-out). Solo el elemento superior es accesible.

### Sorts y Signatura

$$\begin{align}
\text{Sorts:} \quad & \mathtt{Stack}, \mathtt{Element}, \mathtt{Bool} \\
\\
\text{Generadores:} \\
\quad & \text{empty} : \to \mathtt{Stack} \\
\\
\text{Modificadores:} \\
\quad & \text{push} : \mathtt{Stack} \times \mathtt{Element} \to \mathtt{Stack} \\
\quad & \text{pop} : \mathtt{Stack} \to \mathtt{Stack} \\
\\
\text{Observadores:} \\
\quad & \text{top} : \mathtt{Stack} \to \mathtt{Element} \\
\quad & \text{isEmpty} : \mathtt{Stack} \to \mathtt{Bool} \\
\quad & \text{size} : \mathtt{Stack} \to \mathbb{N}
\end{align}$$

### Axiomas

**Axioma 1 (pop de vacía):**
$$\text{pop}(\text{empty}) = \text{empty}$$

**Axioma 2 (size vacía):**
$$\text{size}(\text{empty}) = 0$$

**Axioma 3 (isEmpty vacía):**
$$\text{isEmpty}(\text{empty}) = \text{true}$$

**Axioma 4 (pop de push):**
$$\text{pop}(\text{push}(s, e)) = s \quad \forall s \in \mathtt{Stack}, e \in \mathtt{Element}$$

**Axioma 5 (top de push):**
$$\text{top}(\text{push}(s, e)) = e$$

**Axioma 6 (isEmpty de push):**
$$\text{isEmpty}(\text{push}(s, e)) = \text{false}$$

**Axioma 7 (size de push):**
$$\text{size}(\text{push}(s, e)) = \text{size}(s) + 1$$

### Invariante

$$I(\text{Stack}) : \forall s, \; \text{size}(s) \geq 0 \land (\text{isEmpty}(s) = \text{true} \iff \text{size}(s) = 0)$$

El tamaño es no negativo y vacío es equivalente a tamaño cero.

### Contratos

| Operación | Precondición | Postcondición | Complejidad |
|-----------|--------------|---------------|-------------|
| `push(s, e)` | ninguna | $\text{top}(\text{result}) = e \land \text{isEmpty}(\text{result}) = \text{false}$ | $O(1)$ amortizado tiempo, $O(1)$ amortizado espacio |
| `pop(s)` | ninguna (falla gracefully si vacía) | si $\text{isEmpty}(s) = \text{true}$, resultado = empty; sino, top se restaura | $O(1)$ amortizado tiempo, $O(1)$ amortizado espacio |
| `top(s)` | $\text{isEmpty}(s) = \text{false}$ | devuelve elemento más reciente | $O(1)$ tiempo, $O(1)$ espacio |

---

## TDA Queue (Cola)

Una cola es una secuencia **dinámica** FIFO (first-in, first-out). Se agregan elementos al final; se retiran del frente.

### Sorts y Signatura

$$\begin{align}
\text{Sorts:} \quad & \mathtt{Queue}, \mathtt{Element}, \mathtt{Bool} \\
\\
\text{Generadores:} \\
\quad & \text{empty} : \to \mathtt{Queue} \\
\\
\text{Modificadores:} \\
\quad & \text{enqueue} : \mathtt{Queue} \times \mathtt{Element} \to \mathtt{Queue} \\
\quad & \text{dequeue} : \mathtt{Queue} \to \mathtt{Queue} \\
\\
\text{Observadores:} \\
\quad & \text{front} : \mathtt{Queue} \to \mathtt{Element} \\
\quad & \text{isEmpty} : \mathtt{Queue} \to \mathtt{Bool} \\
\quad & \text{size} : \mathtt{Queue} \to \mathbb{N}
\end{align}$$

### Axiomas

**Axioma 1 (dequeue de vacía):**
$$\text{dequeue}(\text{empty}) = \text{empty}$$

**Axioma 2 (isEmpty vacía):**
$$\text{isEmpty}(\text{empty}) = \text{true}$$

**Axioma 3 (enqueue-dequeue FIFO):**
$$\text{front}(\text{enqueue}(\text{empty}, e)) = e$$

**Axioma 4 (dequeue preserva frente):** Si la cola tiene múltiples elementos, dequeue lo retira:
$$\text{dequeue}(\text{enqueue}(q, e_{\text{nuevo}})) = q \quad \text{si } \text{isEmpty}(q) = \text{false}$$

**Axioma 5 (dequeue-enqueue conmutan casi):**
$$\text{dequeue}(\text{enqueue}(q, e_1)) = \text{enqueue}(\text{dequeue}(q), e_1) \quad \text{si } \text{isEmpty}(q) = \text{false}$$

**Axioma 6 (isEmpty de enqueue):**
$$\text{isEmpty}(\text{enqueue}(q, e)) = \text{false}$$

**Axioma 7 (size de enqueue):**
$$\text{size}(\text{enqueue}(q, e)) = \text{size}(q) + 1$$

### Invariante

$$I(\text{Queue}) : \forall q, \; \text{size}(q) \geq 0 \land (\text{isEmpty}(q) = \text{true} \iff \text{size}(q) = 0)$$

### Contratos

| Operación | Precondición | Postcondición | Complejidad |
|-----------|--------------|---------------|-------------|
| `enqueue(q, e)` | ninguna | elemento agregado al final; $\text{isEmpty}(\text{result}) = \text{false}$ | $O(1)$ amortizado tiempo, $O(1)$ amortizado espacio |
| `dequeue(q)` | ninguna | si vacía, resultado = empty; sino, primer elemento removido | $O(1)$ amortizado tiempo, $O(1)$ amortizado espacio |
| `front(q)` | $\text{isEmpty}(q) = \text{false}$ | devuelve elemento al frente | $O(1)$ tiempo, $O(1)$ espacio |

---

## TDA LinkedList (Lista Enlazada Simple)

Una lista enlazada simple es una secuencia **dinámica** de nodos donde cada nodo referencia al siguiente. Permite inserción/remoción eficiente en cualquier posición conocida.

### Sorts y Signatura

$$\begin{align}
\text{Sorts:} \quad & \mathtt{LinkedList}, \mathtt{Element}, \mathbb{N}, \mathtt{Bool} \\
\\
\text{Generadores:} \\
\quad & \text{empty} : \to \mathtt{LinkedList} \\
\quad & \text{singleton} : \mathtt{Element} \to \mathtt{LinkedList} \\
\\
\text{Modificadores:} \\
\quad & \text{insertAt} : \mathtt{LinkedList} \times \mathbb{N} \times \mathtt{Element} \to \mathtt{LinkedList} \\
\quad & \text{removeAt} : \mathtt{LinkedList} \times \mathbb{N} \to \mathtt{LinkedList} \\
\quad & \text{prepend} : \mathtt{LinkedList} \times \mathtt{Element} \to \mathtt{LinkedList} \\
\quad & \text{append} : \mathtt{LinkedList} \times \mathtt{Element} \to \mathtt{LinkedList} \\
\\
\text{Observadores:} \\
\quad & \text{get} : \mathtt{LinkedList} \times \mathbb{N} \to \mathtt{Element} \\
\quad & \text{size} : \mathtt{LinkedList} \to \mathbb{N} \\
\quad & \text{isEmpty} : \mathtt{LinkedList} \to \mathtt{Bool} \\
\quad & \text{contains} : \mathtt{LinkedList} \times \mathtt{Element} \to \mathtt{Bool}
\end{align}$$

### Axiomas

**Axioma 1 (isEmpty vacía):**
$$\text{isEmpty}(\text{empty}) = \text{true}$$

**Axioma 2 (size vacía):**
$$\text{size}(\text{empty}) = 0$$

**Axioma 3 (prepend a vacía):**
$$\text{get}(\text{prepend}(\text{empty}, e), 0) = e$$

**Axioma 4 (prepend desplaza índices):**
$$\text{get}(\text{prepend}(l, e), i+1) = \text{get}(l, i) \quad \forall i \geq 0$$

**Axioma 5 (append a vacía):**
$$\text{get}(\text{append}(\text{empty}, e), 0) = e$$

**Axioma 6 (size de prepend):**
$$\text{size}(\text{prepend}(l, e)) = \text{size}(l) + 1$$

**Axioma 7 (removeAt índice válido):**
$$\text{size}(\text{removeAt}(l, i)) = \text{size}(l) - 1 \quad \text{si } 0 \leq i < \text{size}(l)$$

**Axioma 8 (removeAt preserva otros elementos):**
$$j < i \implies \text{get}(\text{removeAt}(l, i), j) = \text{get}(l, j)$$
$$j \geq i \land j < \text{size}(l) - 1 \implies \text{get}(\text{removeAt}(l, i), j) = \text{get}(l, j+1)$$

### Invariante

$$I(\text{LinkedList}) : \forall l, \; \text{size}(l) \geq 0 \land (\text{isEmpty}(l) = \text{true} \iff \text{size}(l) = 0) \land \text{no circular references}$$

### Contratos

| Operación | Precondición | Postcondición | Complejidad |
|-----------|--------------|---------------|-------------|
| `insertAt(l, i, e)` | $0 \leq i \leq \text{size}(l)$ | elemento insertado en posición $i$; size aumenta | $O(n)$ tiempo (búsqueda + inserción), $O(1)$ espacio |
| `removeAt(l, i)` | $0 \leq i < \text{size}(l)$ | elemento removido; elementos posteriores se desplazan | $O(n)$ tiempo (búsqueda + remoción), $O(1)$ espacio |
| `get(l, i)` | $0 \leq i < \text{size}(l)$ | devuelve elemento en posición $i$ | $O(n)$ tiempo (búsqueda lineal), $O(1)$ espacio |
| `prepend(l, e)` | ninguna | elemento agregado al inicio | $O(1)$ tiempo, $O(1)$ espacio |
| `append(l, e)` | ninguna | elemento agregado al final | $O(n)$ tiempo (sin tail pointer) o $O(1)$ (con tail pointer), $O(1)$ espacio |

---

## TDA DoublyLinkedList (Lista Enlazada Doble)

Una lista enlazada doble permite navegación bidireccional. Cada nodo referencia al siguiente y al anterior.

### Sorts y Signatura

$$\begin{align}
\text{Sorts:} \quad & \mathtt{DoublyLinkedList}, \mathtt{Element}, \mathbb{N}, \mathtt{Bool} \\
\\
\text{Generadores:} \\
\quad & \text{empty} : \to \mathtt{DoublyLinkedList} \\
\\
\text{Modificadores:} \\
\quad & \text{insertAt} : \mathtt{DoublyLinkedList} \times \mathbb{N} \times \mathtt{Element} \to \mathtt{DoublyLinkedList} \\
\quad & \text{removeAt} : \mathtt{DoublyLinkedList} \times \mathbb{N} \to \mathtt{DoublyLinkedList} \\
\quad & \text{prepend} : \mathtt{DoublyLinkedList} \times \mathtt{Element} \to \mathtt{DoublyLinkedList} \\
\quad & \text{append} : \mathtt{DoublyLinkedList} \times \mathtt{Element} \to \mathtt{DoublyLinkedList} \\
\\
\text{Observadores:} \\
\quad & \text{get} : \mathtt{DoublyLinkedList} \times \mathbb{N} \to \mathtt{Element} \\
\quad & \text{getFromEnd} : \mathtt{DoublyLinkedList} \times \mathbb{N} \to \mathtt{Element} \quad \text{(desde el final)} \\
\quad & \text{size} : \mathtt{DoublyLinkedList} \to \mathbb{N} \\
\quad & \text{isEmpty} : \mathtt{DoublyLinkedList} \to \mathtt{Bool}
\end{align}$$

### Axiomas (adicionales a los de LinkedList)

**Axioma 1 (navegación bidireccional):**
$$\text{getFromEnd}(l, i) = \text{get}(l, \text{size}(l) - 1 - i) \quad \forall i < \text{size}(l)$$

**Axioma 2 (get es consistente en ambas direcciones):**
$$\text{get}(l, i) = \text{getFromEnd}(l, \text{size}(l) - 1 - i)$$

**Axioma 3 (prepend refleja en navegación desde fin):**
$$\text{getFromEnd}(\text{prepend}(l, e), 0) = \text{get}(l, \text{size}(l) - 1) \quad \text{si } \text{isEmpty}(l) = \text{false}$$

### Invariante (extendida)

$$I(\text{DoublyLinkedList}) : \text{LinkedList invariants} \land \forall i, \; \text{next}[\text{prev}[i]] = i \land \text{prev}[\text{next}[i]] = i$$

Cada nodo tiene referencias bidireccionales consistentes.

### Contratos

| Operación | Precondición | Postcondición | Complejidad |
|-----------|--------------|---------------|-------------|
| `insertAt(l, i, e)` | $0 \leq i \leq \text{size}(l)$ | elemento insertado en posición $i$ | $O(n)$ tiempo (búsqueda), $O(1)$ espacio |
| `removeAt(l, i)` | $0 \leq i < \text{size}(l)$ | elemento removido | $O(n)$ tiempo (búsqueda), $O(1)$ espacio |
| `get(l, i)` | $0 \leq i < \text{size}(l)$ | devuelve elemento en posición $i$ | $O(\min(i, n-i))$ tiempo (búsqueda desde ambos extremos), $O(1)$ espacio |
| `getFromEnd(l, i)` | $0 \leq i < \text{size}(l)$ | devuelve elemento desde el final | $O(\min(i, n-i))$ tiempo, $O(1)$ espacio |
| `prepend(l, e)` | ninguna | elemento agregado al inicio | $O(1)$ tiempo, $O(1)$ espacio |
| `append(l, e)` | ninguna | elemento agregado al final | $O(1)$ tiempo (con tail pointer), $O(1)$ espacio |

---

## TDA CircularLinkedList (Lista Enlazada Circular)

Una lista enlazada circular es una secuencia donde el último nodo referencia al primero, formando un ciclo cerrado.

### Sorts y Signatura

$$\begin{align}
\text{Sorts:} \quad & \mathtt{CircularLinkedList}, \mathtt{Element}, \mathbb{N}, \mathtt{Bool} \\
\\
\text{Generadores:} \\
\quad & \text{empty} : \to \mathtt{CircularLinkedList} \\
\\
\text{Modificadores:} \\
\quad & \text{insertAt} : \mathtt{CircularLinkedList} \times \mathbb{N} \times \mathtt{Element} \to \mathtt{CircularLinkedList} \\
\quad & \text{removeAt} : \mathtt{CircularLinkedList} \times \mathbb{N} \to \mathtt{CircularLinkedList} \\
\quad & \text{rotate} : \mathtt{CircularLinkedList} \times \mathbb{N} \to \mathtt{CircularLinkedList} \\
\\
\text{Observadores:} \\
\quad & \text{get} : \mathtt{CircularLinkedList} \times \mathbb{N} \to \mathtt{Element} \\
\quad & \text{size} : \mathtt{CircularLinkedList} \to \mathbb{N} \\
\quad & \text{isEmpty} : \mathtt{CircularLinkedList} \to \mathtt{Bool}
\end{align}$$

### Axiomas

**Axioma 1 (circularidad: acceso modular):**
$$\text{get}(l, i) = \text{get}(l, i \bmod \text{size}(l)) \quad \forall i \geq \text{size}(l)$$

(Los índices se envuelven circularmente.)

**Axioma 2 (rotación preserva elementos):**
$$\text{size}(\text{rotate}(l, k)) = \text{size}(l)$$
$$\text{get}(\text{rotate}(l, k), i) = \text{get}(l, (i + k) \bmod \text{size}(l))$$

**Axioma 3 (isEmpty vacía):**
$$\text{isEmpty}(\text{empty}) = \text{true}$$

**Axioma 4 (removeAt en lista circular):**
$$\text{size}(\text{removeAt}(l, i)) = \text{size}(l) - 1 \quad \text{si } \text{isEmpty}(l) = \text{false}$$

### Invariante

$$I(\text{CircularLinkedList}) : \text{último nodo} \to \text{primer nodo} \land \text{sin terminación nula}$$

Todos los nodos están conectados en un ciclo cerrado.

### Contratos

| Operación | Precondición | Postcondición | Complejidad |
|-----------|--------------|---------------|-------------|
| `rotate(l, k)` | ninguna | orden cíclico rotado $k$ posiciones | $O(k)$ tiempo (actualizar referencias), $O(1)$ espacio |
| `get(l, i)` | ninguna (índices envolventes) | devuelve elemento; $i \bmod \text{size}$ si $i \geq \text{size}$ | $O(n)$ tiempo (búsqueda desde inicio), $O(1)$ espacio |
| `insertAt(l, i, e)` | ninguna | elemento insertado; ciclo preservado | $O(n)$ tiempo (búsqueda + inserción), $O(1)$ espacio |
| `removeAt(l, i)` | $\text{isEmpty}(l) = \text{false}$ | elemento removido; ciclo preservado | $O(n)$ tiempo (búsqueda + remoción), $O(1)$ espacio |

---

## Comparativa: Propiedades Algebraicas y Complejidad

| TDA | Acceso | Inserción | Remoción | Iteración | Complejidad Típica | Invariante clave |
|-----|--------|-----------|----------|-----------|-------------------|------------------|
| **Array** | O(1) directo | Estática | Estática | Lineal | Acceso O(1), modificación O(1) | Tamaño fijo |
| **Stack** | LIFO | Final | Final | LIFO | push/pop/top O(1) | Vacía ↔ size = 0 |
| **Queue** | FIFO | Final | Inicio | FIFO | enqueue/dequeue/front O(1) | FIFO order |
| **LinkedList** | O(n) | Cualquier | Cualquier | Unidireccional | get O(n), insert/remove O(n) | No circular |
| **DoublyLinkedList** | O(n) | Cualquier | Cualquier | Bidireccional | get O(min(i,n-i)), insert/remove O(n) | Refs. simétricas |
| **CircularLinkedList** | O(n) | Cualquier | Cualquier | Cíclica | get O(n), rotate O(k), insert/remove O(n) | Ciclo cerrado |

---

## Validación: Demostración de Axiomas

### Ejemplo: Stack

**Proposición:** Para toda pila $s$ y elemento $e$:
$$\text{isEmpty}(\text{pop}(\text{push}(s, e))) = \text{isEmpty}(s)$$

**Demostración por inducción:**

**Base:** $s = \text{empty}$

$$\begin{align}
\text{isEmpty}(\text{pop}(\text{push}(\text{empty}, e)))
&= \text{isEmpty}(\text{empty}) && \text{(Axioma 4: pop de push)} \\
&= \text{true} && \text{(Axioma 3)} \\
&= \text{isEmpty}(\text{empty}) && \text{(hipótesis)}
\end{align}$$

**Paso inductivo:** Asumimos $\text{isEmpty}(\text{pop}(\text{push}(s, e))) = \text{isEmpty}(s)$ para pila $s$.

Para $s' = \text{push}(s, e')$:

$$\begin{align}
\text{isEmpty}(\text{pop}(\text{push}(\text{push}(s, e'), e)))
&= \text{isEmpty}(\text{push}(s, e')) && \text{(Axioma 4)} \\
&= \text{isEmpty}(s') && \text{(definición)}
\end{align}$$

Por inducción, la proposición vale. ∎

### Ejemplo: LinkedList

**Proposición:** Para lista $l$, índice $i$ válido y elemento $e$:
$$\text{get}(\text{removeAt}(\text{insertAt}(l, i, e), i), i) \neq e$$

(Insertar y luego remover en el mismo lugar restaura la lista original.)

Esta proposición se valida por análisis de casos sobre la estructura de la lista, similar a la demostración de stack.

## Resumen

Las especificaciones algebraicas de estructuras de datos fundamentales proporcionan:

- **Precisión formal:** Cada operación tiene axiomas que gobiernan su comportamiento.
- **Independencia de implementación:** Los axiomas no prescriben cómo implementar, solo qué garantizar.
- **Verificabilidad:** Los axiomas se convierten directamente en tests unitarios.
- **Composicionalidad:** Operaciones correctas se componen en programas correctos.

Estas especificaciones son el cimiento sobre el cual se construyen implementaciones seguras, testables y reemplazables en cualquier lenguaje de programación.

## Próximo paso

Una vez que comprendés las especificaciones algebraicas completas de estructuras fundamentales, el siguiente paso es **implementarlas en Java** respetando los contratos algebraicos. Esto significa escribir código que satisfaga todos los axiomas, con tests que validen cada axioma para garantizar que la implementación es correcta.

## Ejercicios de verificación

1. Resolvé un caso mínimo usando la estructura/algoritmo del capítulo y documentá por qué esa elección es válida.
2. Construí un contraejemplo donde una elección alternativa falle (rendimiento o corrección).
3. Escribí una prueba corta en Java que verifique un invariante crítico.

