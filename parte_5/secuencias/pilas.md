---
title: "Pilas"
subtitle: "Restricción LIFO sobre una secuencia"
subject: Estructuras de Datos
description: Qué problema resuelve una pila, qué significa LIFO en términos de contrato y cómo cambian sus implementaciones con arreglos o nodos.
---

(parte5-pilas)=
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

## Aplicaciones típicas

Una pila aparece cuando el trabajo pendiente respeta una lógica de anidamiento o deshacer.

### Deshacer y rehacer

En un editor, cada acción nueva se apila arriba de la anterior. La última acción es la primera que debería revertirse.

### Llamadas recursivas

Cada llamada deja contexto pendiente: parámetros, variables locales y punto de retorno. Por eso se habla de **call stack**.

### Backtracking

En problemas donde se prueba una decisión y, si falla, se vuelve al último punto de elección, la pila modela exactamente ese retroceso.

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

En ambos casos se rompe la ventaja de la abstracción. Si el problema es una pila, el contrato debería decirlo sin ambigüedades.

## Resumen

Una pila no es menos que una secuencia: es una **restricción correcta** cuando el problema trabaja con el último elemento agregado.

La idea fuerte del capítulo es esta:

1. el contrato LIFO define el TAD;
2. `push`, `pop` y `peek` concentran las operaciones importantes;
3. arreglo y lista enlazada pueden implementar la misma pila, pero con costos y trade-offs distintos.

## Ejercicios

```{exercise}
:label: ex-parte5-pilas-mini

Proponé dos problemas reales donde una pila sea una abstracción natural. En cada caso, justificá por qué una cola sería una mala elección.
```

```{exercise}
:label: ex-parte5-pilas-contrato

Especificá el contrato de una pila acotada de enteros. Indicá precondiciones, postcondiciones y qué debería pasar ante overflow y underflow.
```

```{exercise}
:label: ex-parte5-pilas-implementacion

Querés implementar el historial de deshacer de un editor. Compará una pila basada en arreglo dinámico y una pila enlazada. Explicá qué gana y qué pierde cada una si el historial suele crecer mucho, pero también vaciarse seguido.
```

## Próximo paso

Para seguir, conviene pasar a [Colas](colas.md), donde la restricción cambia de LIFO a FIFO.
