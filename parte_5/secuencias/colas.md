---
title: "Colas"
subtitle: "Restricción FIFO sobre una secuencia"
subject: Estructuras de Datos
description: Qué problema resuelve una cola, qué significa FIFO como contrato y cómo comparar variantes enlazadas y circulares.
---

(parte5-colas)=
# Colas

Las colas modelan problemas donde importa preservar el orden de llegada. Igual que las pilas, muestran que restringir operaciones no reduce expresividad: muchas veces mejora el encuadre del problema.

Una cola aparece cuando el problema no pregunta "¿cuál fue el último?", sino "¿quién estaba esperando primero?". Si esa prioridad temporal es parte del dominio, una secuencia general sobra: lo que hace falta es una abstracción que preserve el frente y el fondo con reglas claras.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender la política FIFO y comparar las implementaciones más comunes de una cola.

**Prerrequisitos.** Conviene haber leído [Pilas](pilas.md), porque el contraste entre LIFO y FIFO ayuda a fijar cuándo conviene cada restricción.

**Desarrollo.** El capítulo define el contrato FIFO, distingue frente y fondo, compara colas enlazadas con buffers circulares y muestra aplicaciones en planificación, simulación y recorridos por anchura.
:::

:::{tip} Idea guía
Una cola conviene cuando el orden de llegada no es un accidente, sino una regla del problema.
:::

## Qué define a una cola

La política de una cola se resume como **FIFO**: *first in, first out*. El primer elemento que entra es el primero que debería salir.

Eso implica dos extremos lógicos:

- **frente**: próximo elemento a salir;
- **fondo**: posición donde entra el siguiente elemento.

El contrato típico incluye:

| Operación | Qué hace | Observación |
| :--- | :--- | :--- |
| `enqueue(e)` | Agrega un elemento al fondo | Respeta el orden de llegada |
| `dequeue()` | Quita y devuelve el frente | Falla si la cola está vacía |
| `front()` | Consulta el frente sin quitarlo | También puede llamarse `peek()` |
| `isEmpty()` | Informa si no hay elementos | Suele ser O(1) |
| `size()` | Informa la cantidad de elementos | Suele ser O(1) |

Una interfaz posible sería:

```java
public interface Cola<T> {
    void enqueue(T elemento);
    T dequeue();
    T front();
    boolean isEmpty();
    int size();
}
```

De nuevo, lo importante es notar qué **no** aparece:

- no hay acceso por índice;
- no hay borrado en el medio;
- no hay inserción arbitraria.

La cola no quiere modelar cualquier secuencia. Quiere modelar espera, atención, procesamiento o exploración en orden de llegada.

## Contrato, errores e invariantes

Igual que en pilas, el contrato necesita aclarar qué se exige y qué se garantiza.

### Precondiciones y postcondiciones típicas

| Operación | Qué exige | Qué garantiza |
| :--- | :--- | :--- |
| `enqueue(e)` | Si la cola es acotada, que haya lugar | El elemento queda al fondo |
| `dequeue()` | Que la cola no esté vacía | Devuelve el frente anterior y reduce el tamaño |
| `front()` | Que la cola no esté vacía | Devuelve el frente sin modificar la estructura |

Los errores clásicos son análogos a los de pila:

| Error | Cuándo aparece |
| :--- | :--- |
| **Underflow** | Se intenta sacar o mirar el frente de una cola vacía |
| **Overflow** | Se intenta agregar a una cola acotada ya llena |

### Invariante de una cola enlazada con frente y fondo

Si la cola mantiene `frente`, `fondo` y `cantidad`, suele cumplirse:

- `cantidad >= 0`
- si `cantidad == 0`, entonces `frente == null` y `fondo == null`
- si `cantidad > 0`, entonces `frente != null` y `fondo != null`
- el primer nodo reachable es exactamente `frente`
- el último nodo reachable es exactamente `fondo`
- `fondo.siguiente == null`

### Invariante de un buffer circular

Si la cola mantiene un arreglo `datos`, un índice `frente` y una cantidad:

- `0 <= cantidad <= datos.length`
- `0 <= frente < datos.length`
- el elemento al frente está en `datos[frente]` cuando `cantidad > 0`
- el próximo lugar libre al fondo se calcula con aritmética modular

La clave del buffer circular es que el arreglo no se interpreta de forma lineal rígida, sino como un anillo lógico.

## Cola enlazada: simple cuando importa crecer sin límite fijo

La implementación enlazada expresa bien la idea de frente y fondo. Agregar al final y sacar del frente son operaciones naturales.

```java
public final class ColaDePacientes {
    private Nodo frente;
    private Nodo fondo;
    private int cantidad;

    private static final class Nodo {
        private final String dato;
        private Nodo siguiente;

        private Nodo(String dato) {
            this.dato = dato;
        }
    }

    public void enqueue(String dato) {
        Nodo nuevo = new Nodo(dato);

        if (this.fondo == null) {
            this.frente = nuevo;
            this.fondo = nuevo;
        } else {
            this.fondo.siguiente = nuevo;
            this.fondo = nuevo;
        }

        this.cantidad++;
    }

    public String dequeue() {
        if (this.frente == null) {
            throw new IllegalStateException("La cola esta vacia");
        }

        String dato = this.frente.dato;
        this.frente = this.frente.siguiente;
        this.cantidad--;

        if (this.frente == null) {
            this.fondo = null;
        }

        return dato;
    }

    public String front() {
        if (this.frente == null) {
            throw new IllegalStateException("La cola esta vacia");
        }
        return this.frente.dato;
    }
}
```

Mientras mantengas ambos extremos, el costo típico queda así:

| Operación | Costo típico |
| :--- | :--- |
| `enqueue()` | O(1) |
| `dequeue()` | O(1) |
| `front()` | O(1) |
| `isEmpty()` | O(1) |
| `size()` | O(1) |

La desventaja no está en el costo asintótico, sino en el overhead por nodo y en la menor localidad de memoria.

## Cola con buffer circular: fuerte cuando querés arreglo sin corrimientos

En [Arreglos](arreglos.md) ya apareció la idea de buffer circular. Acá vale la pena entender por qué resuelve exactamente el problema de una cola.

Si cada `dequeue()` obligara a mover todos los elementos una posición, la implementación con arreglo estaría desperdiciando su tiempo en corrimientos. El buffer circular evita eso moviendo índices, no datos.

```java
public final class ColaCircularDeTurnos {
    private final int[] datos;
    private int frente;
    private int cantidad;

    public ColaCircularDeTurnos(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva");
        }
        this.datos = new int[capacidad];
        this.frente = 0;
        this.cantidad = 0;
    }

    public void enqueue(int turno) {
        if (this.cantidad == this.datos.length) {
            throw new IllegalStateException("La cola esta llena");
        }

        int posicionFondo = (this.frente + this.cantidad) % this.datos.length;
        this.datos[posicionFondo] = turno;
        this.cantidad++;
    }

    public int dequeue() {
        if (this.cantidad == 0) {
            throw new IllegalStateException("La cola esta vacia");
        }

        int turno = this.datos[this.frente];
        this.frente = (this.frente + 1) % this.datos.length;
        this.cantidad--;
        return turno;
    }

    public int front() {
        if (this.cantidad == 0) {
            throw new IllegalStateException("La cola esta vacia");
        }
        return this.datos[this.frente];
    }
}
```

La aritmética modular parece un detalle técnico, pero en realidad expresa una decisión conceptual: el "fondo" no es una posición fija del arreglo, sino una posición calculada a partir del frente y la cantidad actual.

## Cuándo conviene cada implementación

| Implementación | Conviene cuando... | Qué paga |
| :--- | :--- | :--- |
| Cola enlazada | El tamaño varía y no hay cota clara | Más memoria por nodo |
| Buffer circular fijo | Hay capacidad máxima conocida y se quiere evitar corrimientos | Overflow si se llena |
| Buffer circular redimensionable | Se quiere combinar arreglo con crecimiento flexible | Copias ocasionales al expandir |

La comparación no es "enlazada vs arreglo" en abstracto. La pregunta correcta es qué patrón de carga, descarga y capacidad necesita el problema.

## Aplicaciones típicas

La cola aparece cuando importa el orden temporal o la exploración por capas.

### Atención por turnos

Quien llegó primero debería ser atendido primero. Si alguien se agrega al fondo, no debería adelantar a quienes ya estaban esperando.

### Buffers de trabajo

En sistemas de impresión, procesamiento de eventos o mensajería, las tareas pendientes suelen ir entrando al fondo y saliendo por el frente.

### Simulación

Modelar filas en un banco, un supermercado o una sala de espera requiere preservar orden de llegada y medir cómo evoluciona esa espera.

### Recorridos BFS

En búsqueda por anchura, la cola sostiene la frontera de vértices pendientes en orden de descubrimiento. El primero descubierto pero aún no procesado es el próximo en salir.

```java
public void bfs(Grafo grafo, Vertice origen) {
    Cola<Vertice> pendientes = new ColaEnlazada<>();
    Set<Vertice> visitados = new HashSet<>();

    pendientes.enqueue(origen);
    visitados.add(origen);

    while (!pendientes.isEmpty()) {
        Vertice actual = pendientes.dequeue();

        for (Vertice vecino : grafo.vecinosDe(actual)) {
            if (!visitados.contains(vecino)) {
                visitados.add(vecino);
                pendientes.enqueue(vecino);
            }
        }
    }
}
```

La lógica del ejemplo es FIFO pura: primero se procesan los vértices descubiertos antes, y recién después sus vecinos agregados más tarde.

## Qué no conviene hacer

Errores típicos:

1. implementar una cola con arreglo y hacer corrimientos en cada `dequeue()` sin preguntarse si un buffer circular resolvería mejor el problema;
2. exponer el arreglo o los nodos internos en lugar de conservar el contrato `enqueue`/`dequeue`/`front`;
3. usar una pila cuando el dominio claramente necesita respetar llegada.

## Resumen

Cuando el orden temporal de llegada importa, la cola ofrece una abstracción más clara que una secuencia general.

La idea central del capítulo es esta:

1. FIFO define el TAD;
2. frente y fondo organizan la semántica de la estructura;
3. cola enlazada y buffer circular implementan el mismo contrato, pero con compromisos distintos sobre memoria, capacidad y locality.

## Ejercicios

```{exercise}
:label: ex-parte5-colas-mini

Compará una cola implementada con arreglo circular y una cola implementada con lista enlazada para un sistema de impresión. Indicá qué ventajas y desventajas tendría cada una.
```

```{exercise}
:label: ex-parte5-colas-contrato

Especificá el contrato observable de una cola de atención. Indicá qué precondiciones tienen `dequeue()` y `front()`, y qué invariantes deberían cumplirse si la implementación mantiene `frente`, `fondo` y `cantidad`.
```

```{exercise}
:label: ex-parte5-colas-bfs

Explicá por qué BFS necesita una cola y no una pila. Después describí qué recorrido obtendrías si reemplazaras la cola por una pila sin cambiar nada más del algoritmo.
```

## Próximo paso

Para seguir, conviene pasar a [Deques](deques.md), donde ambos extremos pasan a ser relevantes.
