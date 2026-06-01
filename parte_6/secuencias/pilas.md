---
title: "Pilas"
subtitle: "Restricción LIFO sobre una secuencia"
subject: Estructuras de Datos
description: Qué problema resuelve una pila, qué significa LIFO en términos de contrato y cómo cambian sus implementaciones con arreglos o nodos.
---

(parte6-pilas)=
# Pilas

Las pilas muestran que no siempre hace falta una secuencia completamente general. A veces conviene restringir el acceso para modelar mejor el problema y simplificar tanto el diseño como la implementación.

La idea central es esta: si el problema solo necesita trabajar con el **último elemento agregado**, permitir acceso arbitrario por posición no aporta claridad; al contrario, debilita el contrato. Una pila gana fuerza justamente porque dice "solo podés operar sobre el tope".

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender la política LIFO y ver cómo una restricción de acceso puede volver más claro el uso de una estructura.

**Prerrequisitos.** Conviene haber leído [Listas enlazadas](listas_enlazadas.md) y recordar que una pila puede implementarse tanto con arreglos como con nodos.

**Desarrollo.** El capítulo define el contrato LIFO, distingue pila abstracta e implementación concreta, compara variantes con arreglo y con nodos, y cierra con aplicaciones como deshacer, backtracking y soporte de recursión.
:::

:::{tip} Idea guía
Una pila no modela cualquier secuencia. Modela problemas donde el último que entra debe ser el primero en salir.
:::

## Qué define a una pila

La política de una pila se resume con la sigla **LIFO**: *last in, first out*. El elemento más recientemente agregado queda en el tope y es el próximo candidato a salir.

Eso no es un detalle de implementación. Es el núcleo del TAD.

Por eso, una pila no expone operaciones como "insertar en la posición 3" o "borrar el elemento del medio". Su contrato observable es más chico que el de una secuencia general, pero también más expresivo cuando el problema realmente sigue esa lógica.

Las operaciones típicas son estas:

| Operación | Qué hace | Observación |
| :--- | :--- | :--- |
| `push(e)` | Agrega un elemento al tope | Cambia el próximo elemento visible |
| `pop()` | Saca y devuelve el tope | Falla si la pila está vacía |
| `peek()` | Consulta el tope sin quitarlo | También puede llamarse `top()` |
| `isEmpty()` | Indica si no hay elementos | Suele ser O(1) |
| `size()` | Informa cuántos elementos hay | Suele ser O(1) |

## Traza visual: qué cambia en cada operación

La política LIFO conviene verla en una secuencia chica de operaciones. La estructura no ordena por valor ni por antigüedad global: solo mantiene un extremo activo, el tope.

```{mermaid}
flowchart LR
    subgraph P1["push(A)"]
        direction TB
        A1["tope -> A"]
    end

    subgraph P2["push(B)"]
        direction TB
        B2["tope -> B"] --> B1["A"]
    end

    subgraph P3["push(C)"]
        direction TB
        C3["tope -> C"] --> C2["B"] --> C1["A"]
    end

    subgraph P4["pop() devuelve C"]
        direction TB
        D2["tope -> B"] --> D1["A"]
    end
```

La lectura correcta es esta:

1. `A` queda abajo porque fue el primer elemento;
2. `B` tapa a `A`;
3. `C` pasa a ser el nuevo tope;
4. `pop()` no busca el más viejo ni el más chico: saca lo último que se apiló.

Esa traza sirve para detectar rápido si un problema realmente necesita una pila o si solo se está usando una colección cualquiera por costumbre.

Una interfaz posible sería:

```java
public interface Pila<T> {
    void push(T elemento);
    T pop();
    T peek();
    boolean isEmpty();
    int size();
}
```

Fijate qué queda fuera del contrato:

- no hay acceso por índice;
- no hay recorrido obligatorio;
- no hay inserción en posiciones arbitrarias.

Eso no es una carencia. Es una forma de decir con precisión qué operaciones tienen sentido en el dominio.

## Cómo reconocer un problema LIFO

No todo problema con elementos pendientes necesita una pila. Conviene mirar si el dominio realmente privilegia al último elemento agregado.

| Si el problema dice... | La intuición natural suele ser... |
| :--- | :--- |
| "deshacer la última acción" | pila |
| "cerrar el último paréntesis abierto" | pila |
| "volver al último punto de decisión" | pila |
| "atender al primero que llegó" | cola, no pila |
| "elegir el candidato más urgente" | cola de prioridad, no pila |

Una regla práctica:

- si el problema se entiende en términos de anidamiento, retroceso o deshacer, conviene sospechar LIFO;
- si se entiende en términos de espera, turnos o prioridad, probablemente la pila no sea la abstracción correcta.

## Contrato, errores e invariantes

Una pila bien diseñada necesita aclarar tanto su semántica como sus condiciones de uso.

### Precondiciones y postcondiciones típicas

| Operación | Qué exige | Qué garantiza |
| :--- | :--- | :--- |
| `push(e)` | Si la pila es acotada, que haya capacidad disponible | El nuevo tope pasa a ser `e` |
| `pop()` | Que la pila no esté vacía | Devuelve el tope anterior y reduce el tamaño en uno |
| `peek()` | Que la pila no esté vacía | Devuelve el tope sin cambiar el tamaño |

En una pila aparecen dos errores clásicos:

| Error | Cuándo aparece |
| :--- | :--- |
| **Subdesbordamiento** (*underflow*) | Se intenta hacer `pop()` o `peek()` sobre una pila vacía |
| **Desbordamiento** (*overflow*) | Se intenta hacer `push()` en una pila acotada ya llena |

No todas las pilas tienen overflow: una implementación enlazada suele crecer mientras haya memoria disponible. En cambio, una pila basada en arreglo fijo sí necesita decidir qué hacer cuando se llena.

### Qué pasa con valores inválidos

Según el dominio, una pila también puede necesitar reglas extra sobre qué valores admite:

- algunas implementaciones aceptan cualquier referencia;
- otras prohíben `null` para evitar ambigüedades;
- otras restringen el tipo de elemento por contrato del dominio.

Eso no cambia la idea LIFO, pero sí forma parte del contrato observable cuando el cliente necesita saber qué entradas son válidas.

### Invariante de una pila con arreglo

Si la pila mantiene un arreglo `datos` y una variable `cantidad`, suele cumplirse:

- `0 <= cantidad`
- `cantidad <= datos.length`
- el tope está en `datos[cantidad - 1]` cuando `cantidad > 0`
- las posiciones `0 .. cantidad - 1` contienen los elementos válidos de la pila

### Invariante de una pila enlazada

Si la pila mantiene una referencia `tope` y una cantidad:

- `cantidad >= 0`
- si `cantidad == 0`, entonces `tope == null`
- si `cantidad > 0`, entonces `tope` referencia al nodo más recientemente agregado
- cada nodo apunta al nodo que quedó inmediatamente debajo en la pila

Como ya pasó en [Fundamentos de secuencias](fundamentos.md), el cliente no debería conocer esos detalles internos. El contrato es `push`/`pop`/`peek`; la representación queda encapsulada.

## Pila basada en arreglo

Cuando interesa una representación compacta y el crecimiento está acotado o puede redimensionarse, una pila con arreglo es una opción natural.

```java
public final class PilaDeAcciones {
    private final String[] datos;
    private int cantidad;

    public PilaDeAcciones(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva");
        }
        this.datos = new String[capacidad];
        this.cantidad = 0;
    }

    public void push(String accion) {
        if (this.cantidad == this.datos.length) {
            throw new IllegalStateException("La pila esta llena");
        }
        this.datos[this.cantidad] = accion;
        this.cantidad++;
    }

    public String pop() {
        if (this.cantidad == 0) {
            throw new IllegalStateException("La pila esta vacia");
        }

        this.cantidad--;
        String accion = this.datos[this.cantidad];
        this.datos[this.cantidad] = null;
        return accion;
    }

    public String peek() {
        if (this.cantidad == 0) {
            throw new IllegalStateException("La pila esta vacia");
        }
        return this.datos[this.cantidad - 1];
    }

    public boolean isEmpty() {
        return this.cantidad == 0;
    }

    public int size() {
        return this.cantidad;
    }
}
```

La ventaja es clara: todas las operaciones importantes suceden en el mismo extremo lógico y no requieren corrimientos.

| Operación | Costo típico |
| :--- | :--- |
| `push()` | O(1) si hay capacidad |
| `pop()` | O(1) |
| `peek()` | O(1) |
| `isEmpty()` | O(1) |
| `size()` | O(1) |

Si además hacés redimensionamiento geométrico, `push()` puede tener peor caso O(n) cuando copia, pero costo amortizado O(1).

## Pila basada en lista enlazada

Una pila también puede implementarse con nodos. En ese caso, el tope coincide con la cabeza de la lista.

```java
public final class PilaEnlazadaDeTareas {
    private Nodo tope;
    private int cantidad;

    private static final class Nodo {
        private final String dato;
        private final Nodo siguiente;

        private Nodo(String dato, Nodo siguiente) {
            this.dato = dato;
            this.siguiente = siguiente;
        }
    }

    public void push(String dato) {
        this.tope = new Nodo(dato, this.tope);
        this.cantidad++;
    }

    public String pop() {
        if (this.tope == null) {
            throw new IllegalStateException("La pila esta vacia");
        }

        String dato = this.tope.dato;
        this.tope = this.tope.siguiente;
        this.cantidad--;
        return dato;
    }

    public String peek() {
        if (this.tope == null) {
            throw new IllegalStateException("La pila esta vacia");
        }
        return this.tope.dato;
    }
}
```

Acá desaparece el problema de capacidad fija, pero aparece otro trade-off:

- más flexibilidad de crecimiento,
- más overhead por nodo,
- peor localidad de memoria,
- y más presión sobre el recolector si la pila cambia todo el tiempo.

## Cuándo conviene cada implementación

| Implementación | Conviene cuando... | Costo principal |
| :--- | :--- | :--- |
| Arreglo fijo | Hay máximo claro y importa simplicidad | Overflow al llenarse |
| Arreglo dinámico | El tamaño varía pero interesa buena localidad | Redimensionamientos ocasionales |
| Lista enlazada | No hay cota clara o el crecimiento es impredecible | Más memoria por elemento |

La decisión no depende de la palabra "pila", sino del patrón de uso real.

## Comparación operativa entre implementaciones

Conviene mirar la misma pila desde dos planos a la vez:

| Pregunta | Pila con arreglo | Pila enlazada |
| :--- | :--- | :--- |
| ¿Dónde vive el tope? | en `datos[cantidad - 1]` | en la referencia `tope` |
| ¿Qué cambia en `push`? | se escribe al final lógico | se crea un nodo nuevo al frente |
| ¿Qué cambia en `pop`? | baja `cantidad` | avanza `tope` al siguiente nodo |
| ¿Qué riesgo interno aparece? | overflow o redimensionamiento | sobrecosto por nodos y referencias |

Esta tabla ayuda a separar dos preguntas:

1. si el TAD correcto es una pila;
2. si la representación correcta de esa pila es contigua o enlazada.

```{mermaid}
block-beta
    columns 2
    block:Arr:1
        A1["tope (índice 2)"]
        A2["dato 2"]
        A3["dato 1"]
        A4["[0] (base)"]
    end
    block:List:1
        L1["tope (nodo)"]
        L2["nodo"]
        L3["nodo"]
        L4["null (base)"]
    end
    
    L1 --> L2
    L2 --> L3
    L3 --> L4
    
    style Arr fill:#e3f2fd,stroke:#1e88e5
    style List fill:#fff3e0,stroke:#fb8c00
```

## Aplicaciones típicas

Una pila aparece cuando el trabajo pendiente respeta una lógica de anidamiento o deshacer.

### Deshacer y rehacer

En un editor, cada acción nueva se apila arriba de la anterior. La última acción es la primera que debería revertirse.

```java
public class Editor {
    private Pila<Accion> historialDeshacer = new PilaEnlazada<>();
    private Pila<Accion> historialRehacer = new PilaEnlazada<>();

    public void ejecutar(Accion accion) {
        accion.aplicar();
        this.historialDeshacer.push(accion);
        // Al ejecutar una nueva acción, se pierde la historia de rehacer
        this.historialRehacer.vaciar(); 
    }

    public void deshacer() {
        if (!this.historialDeshacer.isEmpty()) {
            Accion accion = this.historialDeshacer.pop();
            accion.revertir();
            this.historialRehacer.push(accion);
        }
    }
}
```

### Llamadas recursivas

Cada llamada deja contexto pendiente: parámetros, variables locales y punto de retorno. Por eso se habla de **call stack**.

```{mermaid}
flowchart TB
    F3["tope -> factorial(3)"] --> F2["factorial(2)"] --> F1["factorial(1)"] --> F0["caso base / retorno"]
```

La idea del diagrama no es describir todos los detalles de ejecución de Java, sino mostrar qué queda pendiente:

- la llamada más reciente queda arriba;
- cuando termina, se retoma la inmediatamente anterior;
- el desapilado ocurre en orden inverso al apilado.

Por eso recursión y pila están tan cerca conceptualmente: una recursión profunda no es magia, es una pila de contextos de ejecución.

### Backtracking

En problemas donde se prueba una decisión y, si falla, se vuelve al último punto de elección, la pila modela exactamente ese retroceso.

```{mermaid}
flowchart TD
    Inicio --> OpcionA
    Inicio --> OpcionB
    OpcionA --> Falla((Falla))
    OpcionA -.->|backtrack| Inicio
    Inicio --> OpcionC
    
    style Falla fill:#ffcdd2,stroke:#d32f2f
```

Ejemplos típicos:

- resolver un laberinto;
- explorar combinaciones;
- evaluar movimientos posibles en un juego;
- volver al último nodo pendiente en un recorrido DFS iterativo.

### Balanceo y parsing

El chequeo de paréntesis, llaves o etiquetas anidadas también sigue LIFO: el último delimitador abierto es el primero que debe cerrarse.

```java
public static boolean estanBalanceados(String expresion) {
    Pila<Character> pila = new PilaArray<>(expresion.length());

    for (int i = 0; i < expresion.length(); i++) {
        char actual = expresion.charAt(i);
        if (actual == '(' || actual == '[' || actual == '{') {
            pila.push(actual);
        } else if (actual == ')' || actual == ']' || actual == '}') {
            if (pila.isEmpty()) {
                return false;
            }
            char apertura = pila.pop();
            if (!coinciden(apertura, actual)) {
                return false;
            }
        }
    }

    return pila.isEmpty();
}
```

El punto del ejemplo no es la sintaxis puntual, sino la decisión conceptual: cada cierre necesita comparar contra la **última** apertura pendiente, no contra la primera.

## Qué no conviene hacer

Hay dos errores de diseño frecuentes:

1. modelar una pila y después agregarle getters o acceso por índice "por comodidad";
2. usar una secuencia general cuando el dominio claramente necesita LIFO.

También conviene evitar un tercer error:

3. confundir la pila del problema con una colección cualquiera y perder la semántica del tope.

En ambos casos se rompe la ventaja de la abstracción. Si el problema es una pila, el contrato debería decirlo sin ambigüedades.

## Resumen

Una pila no es menos que una secuencia: es una **restricción correcta** cuando el problema trabaja con el último elemento agregado.

La idea fuerte del capítulo es esta:

1. el contrato LIFO define el TAD;
2. `push`, `pop` y `peek` concentran las operaciones importantes;
3. arreglo y lista enlazada pueden implementar la misma pila, pero con costos y trade-offs distintos.

## Ejercicios

```{exercise}
:label: ex-parte6-pilas-mini

Proponé dos problemas reales donde una pila sea una abstracción natural. En cada caso, justificá por qué una cola sería una mala elección.
```

```{exercise}
:label: ex-parte6-pilas-contrato

Especificá el contrato de una pila acotada de enteros. Indicá precondiciones, postcondiciones y qué debería pasar ante overflow y underflow.
```

```{exercise}
:label: ex-parte6-pilas-implementacion

Querés implementar el historial de deshacer de un editor. Compará una pila basada en arreglo dinámico y una pila enlazada. Explicá qué gana y qué pierde cada una si el historial suele crecer mucho, pero también vaciarse seguido.
```

```{exercise}
:label: ex-parte6-pilas-traza

Mostrá el estado de una pila después de esta secuencia:

1. `push(10)`
2. `push(20)`
3. `push(30)`
4. `pop()`
5. `push(40)`

Indicá cuál es el tope final y en qué orden saldrían los elementos si después desapilaras hasta vaciarla.
```

## Próximo paso

Para seguir, conviene pasar a [Colas](colas.md), donde la restricción cambia de LIFO a FIFO.
