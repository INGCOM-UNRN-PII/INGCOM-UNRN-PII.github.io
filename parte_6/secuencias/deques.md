---
title: "Deques"
subtitle: "Acceso eficiente en ambos extremos"
subject: Estructuras de Datos
description: Cuándo una pila o una cola ya no alcanzan y conviene usar una secuencia restringida con operaciones eficientes en ambos extremos.
---

(parte6-deques)=
# Deques

Las [Pilas](pilas.md) y las [Colas](colas.md) mostraron dos restricciones muy útiles: LIFO y FIFO. El deque aparece cuando ninguna de las dos alcanza por sí sola. El problema ya no pide operar solo en un extremo o solo respetar orden de llegada, sino **trabajar con frente y fondo como zonas activas**.

Esa diferencia parece chica, pero cambia bastante la expresividad de la estructura. Un deque permite insertar o quitar elementos en ambos extremos sin abrir la puerta a una secuencia totalmente general.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender cuándo una cola ya no alcanza y hace falta una estructura que opere bien en ambos extremos.

**Prerrequisitos.** Conviene haber leído [Colas](colas.md), porque el deque se entiende mejor como generalización de pila y cola. También ayuda tener presente lo visto en [Arreglos](arreglos.md) y [Listas enlazadas](listas_enlazadas.md).

**Desarrollo.** Primero se define el TAD deque y su relación con pilas y colas. Después se presentan sus variantes restringidas, se comparan implementaciones con arreglo circular y lista doblemente enlazada, y se cierra con aplicaciones típicas.
:::

## Qué define a un deque

Deque viene de **double-ended queue**. La idea central es que la estructura mantiene una secuencia, pero con operaciones eficientes sobre ambos extremos.

Las operaciones típicas son:

| Operación | Sentido |
| :--- | :--- |
| `addFirst(x)` | Inserta al frente |
| `addLast(x)` | Inserta al fondo |
| `removeFirst()` | Quita y devuelve el frente |
| `removeLast()` | Quita y devuelve el fondo |
| `peekFirst()` | Consulta el frente sin quitar |
| `peekLast()` | Consulta el fondo sin quitar |

La diferencia clave con una secuencia general es que el deque **no promete acceso barato al medio**. Su contrato está centrado en los bordes.

:::{important}
Un deque no es "una lista con más métodos". Es otra abstracción. Lo que promete no es edición arbitraria, sino operaciones consistentes y eficientes en ambos extremos.
:::

## Relación con pila y cola

Un deque puede verse como una estructura más general que contiene a pila y cola como casos particulares.

| Estructura | Inserta | Quita | Política resultante |
| :--- | :--- | :--- | :--- |
| Pila | Siempre en el mismo extremo | Siempre en ese mismo extremo | LIFO |
| Cola | Inserta en un extremo y quita en el otro | Extremos fijos | FIFO |
| Deque | Puede insertar y quitar en ambos extremos | Ambos extremos | Doble punta |

Eso no significa que siempre convenga reemplazar pilas y colas por deques. Si el problema tiene una restricción natural más simple, conviene expresarla con esa abstracción. Un deque vale la pena cuando el patrón real de uso cambia entre frente y fondo.

## Variantes restringidas

Además del deque completo, aparecen dos variantes parciales que ayudan a pensar el diseño:

| Variante | Inserciones | Eliminaciones | Idea |
| :--- | :--- | :--- | :--- |
| **Input-restricted deque** | Solo por un extremo | Por ambos extremos | El ingreso es controlado; la salida es flexible |
| **Output-restricted deque** | Por ambos extremos | Solo por un extremo | La extracción queda centralizada |

```{mermaid}
flowchart TD
    subgraph Input-Restricted
        direction LR
        I1((In)) --> F1[Frente] --- B1[Fondo]
        F1 --> O1((Out))
        B1 --> O2((Out))
    end
    
    subgraph Output-Restricted
        direction LR
        I2((In)) --> F2[Frente] --- B2[Fondo] <-- I3((In))
        F2 --> O3((Out))
    end
```

Estas variantes no suelen enseñarse tanto en bibliotecas estándar, pero sirven para razonar qué permisos necesita realmente el problema.

Por ejemplo:

1. si nuevos elementos siempre llegan por atrás, pero a veces necesitás descartar por delante o por detrás, el deque restringido de entrada puede alcanzar;
2. si podés insertar urgentes al frente y normales al fondo, pero toda atención sale por delante, estás más cerca de una restricción de salida.

## Implementación con arreglo circular

El arreglo circular ya apareció como base natural para colas. En un deque, la idea se extiende: además de mover el frente, también necesitás reservar o liberar espacio por atrás sin corrimientos.

La representación típica mantiene:

- un arreglo `datos`,
- un índice `frente`,
- una `cantidad`,
- y una convención para calcular el fondo.

```java
public final class DequeCircular {
    private final int[] datos;
    private int frente;
    private int cantidad;

    public DequeCircular(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva");
        }

        this.datos = new int[capacidad];
        this.frente = 0;
        this.cantidad = 0;
    }

    public void addFirst(int valor) {
        this.validarQueNoEsteLleno();
        this.frente = (this.frente - 1 + this.datos.length) % this.datos.length;
        this.datos[this.frente] = valor;
        this.cantidad++;
    }

    public void addLast(int valor) {
        this.validarQueNoEsteLleno();
        int posicion = (this.frente + this.cantidad) % this.datos.length;
        this.datos[posicion] = valor;
        this.cantidad++;
    }

    public int removeFirst() {
        this.validarQueNoEsteVacio();
        int valor = this.datos[this.frente];
        this.frente = (this.frente + 1) % this.datos.length;
        this.cantidad--;
        return valor;
    }

    public int removeLast() {
        this.validarQueNoEsteVacio();
        int posicion = (this.frente + this.cantidad - 1) % this.datos.length;
        int valor = this.datos[posicion];
        this.cantidad--;
        return valor;
    }

    private void validarQueNoEsteLleno() {
        if (this.cantidad == this.datos.length) {
            throw new IllegalStateException("El deque esta lleno");
        }
    }

    private void validarQueNoEsteVacio() {
        if (this.cantidad == 0) {
            throw new IllegalStateException("El deque esta vacio");
        }
    }
}
```

Lo importante no es memorizar la aritmética modular, sino entender la representación:

- el deque lógico puede "envolver" el arreglo físico,
- frente y fondo no tienen por qué quedar en índices crecientes simples,
- y el objetivo es evitar corrimientos.

En esta variante, las operaciones en extremos quedan en O(1), pero la capacidad puede ser fija o exigir una política de redimensionamiento si querés una versión dinámica.

## Implementación con lista doblemente enlazada

La otra implementación natural es la **lista doblemente enlazada**. Acá cada nodo conoce a su anterior y a su siguiente, y la estructura mantiene referencias a cabeza y cola.

```java
final class Nodo {
    String dato;
    Nodo anterior;
    Nodo siguiente;
}
```

Con esa base:

- insertar al frente es reenlazar cerca de `cabeza`,
- insertar al fondo es reenlazar cerca de `cola`,
- quitar en cualquiera de los extremos también queda local.

Esta representación evita aritmética de índices y crecimiento fijo, pero paga con:

- más memoria por nodo,
- peor localidad de memoria,
- y más cuidado para mantener sincronizados enlaces hacia adelante y hacia atrás.

:::{note}
Una lista **simplemente enlazada** no es una buena base para un deque completo si querés `removeLast()` en O(1). Para quitar el último nodo necesitás conocer también al penúltimo, y sin enlace hacia atrás terminás recorriendo la lista.
:::

## Comparación de implementaciones

| Implementación | Ventaja fuerte | Costo principal | Cuándo conviene |
| :--- | :--- | :--- | :--- |
| Arreglo circular | Bajo overhead y muy buen rendimiento local | Manejo más delicado de índices y capacidad | Cuando importa eficiencia compacta y el patrón es muy de extremos |
| Lista doblemente enlazada | Crecimiento natural y reenlace simple en ambos bordes | Más memoria y peor localidad | Cuando el tamaño cambia mucho y la capacidad no se conoce |

En ambos casos, si el diseño está bien hecho, las operaciones del TAD deque sobre los extremos quedan en O(1).

## Dónde sirve un deque

El deque conviene cuando el problema alterna decisiones sobre frente y fondo. Algunos casos típicos:

### Ventanas deslizantes

En varios algoritmos de ventanas deslizantes se mantiene un conjunto de candidatos útiles y se descartan elementos viejos o dominados desde uno u otro extremo. El deque permite que ese mantenimiento sea natural.

### Procesamiento con urgencias locales

Si ciertos eventos entran al frente por prioridad operativa y otros al fondo por llegada normal, una cola simple se queda corta. Tampoco hace falta una cola de prioridad completa si solo distinguís extremos.

### Verificación por extremos

Problemas donde se comparan o consumen elementos desde ambos lados de una secuencia también encajan bien en la intuición de deque.

### Historiales o buffers editables en los bordes

Si el trabajo real está concentrado en agregar o quitar de ambos extremos, pero no en editar posiciones intermedias, el deque expresa mejor esa intención que una lista general.

## Errores frecuentes

### Usar un deque cuando una cola o una pila alcanzan

Si el problema es estrictamente FIFO o estrictamente LIFO, usar deque puede diluir el contrato sin aportar valor. La abstracción más específica suele comunicar mejor.

### Confundir deque con cola de prioridad

En un deque el siguiente elemento depende del **extremo** que elegís operar. En una cola de prioridad depende de una relación de orden o prioridad. Son problemas distintos.

### Implementarlo sobre lista simplemente enlazada y prometer demasiado

Si tu implementación no puede quitar del fondo en tiempo constante, entonces no está sosteniendo bien el contrato esperado del deque.

### Romper la invariante de extremos

En un arreglo circular o en una lista doblemente enlazada, un pequeño error en `frente`, `cola`, `anterior` o `siguiente` puede dejar la estructura inconsistente. El problema ya no es algorítmico: es de representación.

## Resumen

El deque es una secuencia restringida que habilita operaciones eficientes en ambos extremos. Su valor no está en ser "más general" que una pila o una cola, sino en capturar problemas donde el trabajo natural ocurre tanto al frente como al fondo.

Las implementaciones más razonables son el arreglo circular y la lista doblemente enlazada. La primera privilegia compacidad y localidad; la segunda, flexibilidad estructural. En ambos casos, el diseño correcto consiste en mantener bien los extremos sin convertir al medio en protagonista.

## Ejercicios

```{exercise}
:label: ex-parte6-deques-mini

Pensá un problema donde a veces convenga insertar al frente y otras al fondo. Explicá por qué una cola simple o una pila simple volverían incómodo ese diseño.
```

```{exercise}
:label: ex-parte6-deques-restringidos

Inventá un caso donde alcance un deque restringido de entrada y otro donde alcance un deque restringido de salida. En cada uno, indicá qué operaciones deberían quedar prohibidas.
```

```{exercise}
:label: ex-parte6-deques-implementacion

Compará un deque implementado con arreglo circular y otro con lista doblemente enlazada para un buffer de tareas urgentes y normales. Indicá qué representación elegirías si el tamaño máximo es conocido y cuál si cambia mucho durante la ejecución.
```

## Próximo paso

Para seguir, conviene pasar a [Colas de prioridad](colas_prioridad.md), donde el siguiente elemento ya no depende del orden de llegada ni del extremo activo, sino de una relación de prioridad.
