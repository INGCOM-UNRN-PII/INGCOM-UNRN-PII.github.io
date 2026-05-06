---
title: "Listas enlazadas"
subtitle: "Representación por nodos y acceso secuencial"
subject: Estructuras de Datos
description: Qué gana y qué pierde una secuencia cuando deja la memoria contigua y pasa a organizarse como nodos enlazados.
---

(parte5-listas-enlazadas)=
# Listas enlazadas

En [Arreglos](arreglos.md) la secuencia estaba apoyada en memoria contigua. Eso hacía muy barato el acceso por índice, pero caro mover elementos. Las listas enlazadas cambian por completo esa prioridad: en vez de guardar todo junto, guardan cada elemento en un **nodo** y conectan esos nodos mediante referencias.

La consecuencia es directa. Se gana flexibilidad estructural y edición local, pero se pierde acceso aleatorio barato. La pregunta ya no es "¿cuál es la posición `i`?", sino "¿cómo llego hasta el nodo que necesito?".

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender qué gana y qué pierde una secuencia cuando se representa con nodos enlazados en lugar de memoria contigua.

**Prerrequisitos.** Conviene haber leído [Arreglos](arreglos.md), porque este capítulo funciona mejor cuando se lo piensa como contraste. También ayuda tener fresco [Análisis de algoritmos](../algoritmos.md).

**Desarrollo.** Primero se fija la idea general de nodo y acceso secuencial. Después se comparan listas simples, dobles y circulares, y se cierra con nodos centinela como técnica para simplificar casos borde.
:::

## Qué resuelven bien las listas enlazadas

Una lista enlazada representa la secuencia como una cadena de nodos. Cada nodo guarda:

1. un dato,
2. una o más referencias a otros nodos.

En la variante más simple, cada nodo conoce solo al siguiente. Con eso alcanza para recorrer toda la lista desde la cabeza.

```java
final class Nodo {
    String dato;
    Nodo siguiente;

    Nodo(String dato) {
        this.dato = dato;
        this.siguiente = null;
    }
}
```

La estructura mínima ya no es "un arreglo y una cantidad", sino:

- una referencia al primer nodo,
- opcionalmente una referencia al último,
- y una invariante que describa cómo están enlazados.

:::{important} Invariante típica de una lista simplemente enlazada
Si la lista mantiene `cabeza`, `cola` y `cantidad`, suele cumplirse:

- `cantidad >= 0`
- si `cantidad == 0`, entonces `cabeza == null` y `cola == null`
- si `cantidad > 0`, entonces `cabeza != null` y `cola != null`
- el último nodo reachable desde `cabeza` es exactamente `cola`
- `cola.siguiente == null`

La estructura no depende de índices físicos. Depende de que la cadena de referencias sea consistente. Eso vuelve central el control de representación y enlaza bien con {ref}`oop-contratos`.
:::

Estas listas convienen cuando:

1. el tamaño cambia seguido,
2. insertar o borrar cerca de un nodo conocido es frecuente,
3. no necesitás acceso aleatorio intenso,
4. o querés modelar una estructura donde el orden se mantiene enlazando elementos, no desplazándolos.

## Lista simplemente enlazada: la forma base

La lista simplemente enlazada es la variante más elemental. Cada nodo conoce a su sucesor y nada más.

```java
public final class ListaDeReproduccion {
    private Nodo cabeza;
    private Nodo cola;
    private int cantidad;

    private static final class Nodo {
        private String cancion;
        private Nodo siguiente;

        private Nodo(String cancion) {
            this.cancion = cancion;
        }
    }

    public ListaDeReproduccion() {
        this.cabeza = null;
        this.cola = null;
        this.cantidad = 0;
    }

    public void agregarAlInicio(String cancion) {
        Nodo nuevo = new Nodo(cancion);
        nuevo.siguiente = this.cabeza;
        this.cabeza = nuevo;

        if (this.cola == null) {
            this.cola = nuevo;
        }

        this.cantidad++;
    }

    public void agregarAlFinal(String cancion) {
        Nodo nuevo = new Nodo(cancion);

        if (this.cola == null) {
            this.cabeza = nuevo;
            this.cola = nuevo;
        } else {
            this.cola.siguiente = nuevo;
            this.cola = nuevo;
        }

        this.cantidad++;
    }

    public String eliminarPrimera() {
        if (this.cabeza == null) {
            throw new IllegalStateException("La lista esta vacia");
        }

        String eliminada = this.cabeza.cancion;
        this.cabeza = this.cabeza.siguiente;
        this.cantidad--;

        if (this.cabeza == null) {
            this.cola = null;
        }

        return eliminada;
    }

    public String obtener(int indice) {
        this.validarIndice(indice);

        Nodo actual = this.cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }

        return actual.cancion;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= this.cantidad) {
            throw new IndexOutOfBoundsException("Indice invalido: " + indice);
        }
    }
}
```

El contraste con el arreglo es fuerte:

- agregar al inicio deja de requerir corrimientos,
- agregar al final puede ser O(1) si además mantenés `cola`,
- pero `obtener(indice)` obliga a caminar nodo por nodo desde la cabeza.

| Operación | Costo típico | Observación |
| :--- | :--- | :--- |
| Agregar al inicio | O(1) | Solo cambia la cabeza |
| Eliminar al inicio | O(1) | Solo cambia la cabeza |
| Agregar al final | O(1) con `cola`, O(n) sin `cola` | Depende de la representación |
| Obtener por índice | O(n) | Hay que recorrer |
| Insertar después de un nodo conocido | O(1) | No hay corrimientos |
| Buscar un valor | O(n) | Sigue siendo recorrido lineal |

La frase importante es "después de un nodo conocido". Si no sabés dónde insertar y primero tenés que buscar la posición, el costo total vuelve a ser lineal.

## Qué se gana y qué se pierde frente al arreglo

Las listas enlazadas no son una mejora general. Son una respuesta distinta a otra prioridad.

| Aspecto | Arreglo | Lista enlazada |
| :--- | :--- | :--- |
| Acceso por índice | Muy fuerte | Débil |
| Inserción al inicio | Mala si hay corrimientos | Muy fuerte |
| Inserción en el medio | Requiere desplazar | Requiere reenlazar |
| Localidad de memoria | Buena | Peor |
| Overhead por elemento | Bajo | Mayor, por referencias extra |
| Crecimiento dinámico | Requiere redimensionar | Natural |

Por eso el patrón de decisión no es "lista para todo". De hecho, en la práctica general suele seguir conviniendo una lista basada en arreglo, como `ArrayList`. En {ref}`java-colecciones` ya apareció esa comparación con `LinkedList`.

## Lista doblemente enlazada: moverse en ambos sentidos

La lista simplemente enlazada alcanza para muchos problemas, pero tiene una limitación dura: desde un nodo no podés volver al anterior sin empezar otra vez desde la cabeza.

La lista doblemente enlazada agrega una segunda referencia:

```java
final class Nodo {
    String dato;
    Nodo anterior;
    Nodo siguiente;
}
```

Con eso se gana:

- recorrido hacia adelante y hacia atrás,
- eliminación más simple del nodo actual cuando ya tenés su referencia,
- operaciones naturales en ambos extremos si además mantenés `cabeza` y `cola`.

Pero también se paga:

- más memoria por nodo,
- más invariantes que mantener,
- y más riesgo de inconsistencias si olvidás actualizar uno de los enlaces.

:::{note}
En una lista doblemente enlazada, si hacés `a.siguiente = b`, no alcanza con eso. En muchos casos también necesitás `b.anterior = a`. La estructura ahora tiene dos sentidos que deben quedar sincronizados.
:::

Ese diseño aparece mucho en listas editables, historiales navegables y estructuras donde avanzar y retroceder son operaciones igualmente importantes.

## Lista circular: el final vuelve al principio

En una lista circular, el último nodo no apunta a `null`, sino otra vez al comienzo. La estructura deja de tener un "fin físico" marcado por ausencia de siguiente.

```java
final class Nodo {
    String dato;
    Nodo siguiente;
}

// si cola es el ultimo nodo:
// cola.siguiente == cabeza
```

La variante circular es útil cuando el recorrido natural del problema no tiene un corte fuerte entre último y primero:

- rondas de turnos,
- reproductores en modo repetición,
- planificadores cíclicos,
- buffers que rotan.

La ventaja conceptual es que el siguiente del último ya está definido. La dificultad es que un error en los enlaces puede dejar un ciclo mal armado y volver más difícil detectar dónde termina el recorrido esperado.

## Nodos centinela: menos casos borde

Muchas complicaciones de implementación aparecen por los extremos:

- lista vacía,
- inserción al principio,
- borrado del único elemento,
- manejo especial de cabeza o cola.

Un **centinela** es un nodo especial que no representa un dato del dominio, pero simplifica la representación. Funciona como marca estructural permanente.

En vez de empezar con `cabeza == null`, podés tener una lista que siempre conserva un nodo centinela:

```java
public final class ListaConCentinela {
    private final Nodo centinela;
    private int cantidad;

    private static final class Nodo {
        private String dato;
        private Nodo siguiente;
    }

    public ListaConCentinela() {
        this.centinela = new Nodo();
        this.centinela.siguiente = null;
        this.cantidad = 0;
    }

    public void agregarAlInicio(String dato) {
        Nodo nuevo = new Nodo();
        nuevo.dato = dato;
        nuevo.siguiente = this.centinela.siguiente;
        this.centinela.siguiente = nuevo;
        this.cantidad++;
    }
}
```

Con este patrón:

- la cabeza lógica es `centinela.siguiente`,
- el centinela siempre existe,
- varias operaciones dejan de preguntar "¿estoy en el primer nodo real?".

En listas doblemente enlazadas y circulares, los centinelas pueden simplificar todavía más la implementación. La ventaja no es de complejidad asintótica, sino de claridad y reducción de casos especiales.

## Comparación rápida de variantes

| Variante | Qué simplifica | Qué complica | Cuándo conviene |
| :--- | :--- | :--- | :--- |
| Simplemente enlazada | Estructura mínima | No retrocede, acceso secuencial | Cuando importa insertar o borrar cerca del frente |
| Doblemente enlazada | Operar en ambos sentidos | Más memoria y más invariantes | Cuando avanzar y retroceder son parte del problema |
| Circular | Modelar recorridos cíclicos | Más fácil armar ciclos incorrectos | Cuando no hay corte fuerte entre fin y comienzo |
| Con centinela | Casos borde de implementación | Agrega un nodo que no es dato real | Cuando querés simplificar el código de enlace |

## Errores de implementación frecuentes

### Perder la referencia al resto de la lista

Si reasignás un enlace sin guardar antes lo necesario, podés desconectar una parte entera de la estructura.

### Olvidar actualizar `cola`

En listas que mantienen cabeza y cola, borrar el último nodo o vaciar la lista obliga a revisar ambas referencias. Si una queda vieja, la estructura se corrompe.

### Asumir que insertar "en el medio" siempre es O(1)

Eso solo vale si ya tenés la referencia al nodo anterior o al nodo objetivo. Si primero hay que buscarlo por posición, el recorrido domina.

### Exponer nodos al cliente

Si el resto del sistema puede manipular `Nodo` directamente, la lista pierde control sobre su representación. Lo sano es que el nodo sea detalle interno y que la interfaz pública trabaje con operaciones del dominio.

## Resumen

Las listas enlazadas reemplazan desplazamientos por reenlaces. Esa es su idea central. Cuando el problema edita seguido la secuencia o crece de manera impredecible, eso puede ser una ventaja fuerte.

La contrapartida es clara: el acceso por posición deja de ser directo, la localidad de memoria empeora y la estructura interna exige más cuidado. Las variantes simple, doble, circular y con centinela no cambian esa lógica de base; solo ajustan qué operaciones se vuelven más cómodas y qué invariantes hay que sostener.

## Ejercicios

```{exercise}
:label: ex-parte5-listas-enlazadas-mini

Explicá por qué una lista enlazada puede ser conveniente para modelar una cola de reproducción editable, pero no necesariamente para consultas frecuentes por índice.
```

```{exercise}
:label: ex-parte5-listas-doble-o-simple

Tenés que modelar un historial de navegación con operaciones "atrás" y "adelante". Decidí si conviene una lista simplemente enlazada o doblemente enlazada y justificá qué operaciones quedan favorecidas.
```

```{exercise}
:label: ex-parte5-listas-centinela

Mostrá un caso borde de inserción o borrado donde usar un nodo centinela simplifique la implementación. No hace falta escribir código completo, pero sí describir qué condición especial desaparece.
```

## Próximo paso

Ahora conviene pasar a [Pilas](pilas.md), donde una secuencia general se restringe para resolver un patrón de acceso específico y varias de estas decisiones de representación vuelven a aparecer.
