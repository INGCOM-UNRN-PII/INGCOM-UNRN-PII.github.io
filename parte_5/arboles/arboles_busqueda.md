---
title: "Árboles binarios de búsqueda"
subtitle: "Orden para buscar, insertar y recorrer"
subject: Estructuras de Datos
description: Cómo una invariante de orden convierte al árbol binario en una estructura útil para búsqueda, inserción, borrado y recorrido ordenado.
---

(parte5-arboles-busqueda)=
# Árboles binarios de búsqueda

En [Árboles binarios](arboles_binarios.md) apareció la forma base: cada nodo tiene a lo sumo hijo izquierdo e hijo derecho. El BST agrega una idea decisiva sobre esa forma: **orden**.

Si cada nodo respeta una relación de orden respecto de sus subárboles, la estructura deja de ser solo jerárquica y pasa a servir también para búsquedas eficientes, inserción ordenada y recorridos en orden creciente.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender la invariante de orden de un BST y cómo esa invariante permite búsqueda, inserción, borrado y recorrido ordenado.

**Prerrequisitos.** Conviene haber leído [Árboles binarios](arboles_binarios.md), porque el BST se apoya sobre esa forma estructural.

**Desarrollo.** Primero se fija la propiedad de búsqueda. Después se recorren búsqueda, inserción y borrado, y se cierra con el problema de la altura y el puente hacia [Árboles balanceados](arboles_balanceados.md).
:::

## Qué garantiza un BST

En un árbol binario de búsqueda, para cada nodo con clave `k` se cumple:

1. toda clave del subárbol izquierdo es menor que `k`,
2. toda clave del subárbol derecho es mayor que `k`.

Si la estructura admite claves repetidas, hace falta definir una política explícita:

- rechazarlas,
- contarlas,
- o mandar iguales siempre a un lado.

Lo importante no es cuál política elijas, sino que quede fija y sea consistente en toda la estructura.

:::{important}
La fuerza del BST no sale de la forma binaria sola, sino de esta invariante de orden. Sin ella, tener izquierda y derecha no alcanza para buscar mejor.
:::

Una representación enlazada típica se ve así:

```java
final class Nodo {
    int clave;
    String valor;
    Nodo izquierdo;
    Nodo derecho;

    Nodo(int clave, String valor) {
        this.clave = clave;
        this.valor = valor;
    }
}
```

Acá el árbol ya puede modelar algo cercano a un diccionario ordenado: cada nodo guarda clave y valor, y la clave organiza el recorrido.

## Buscar: seguir un solo camino

La búsqueda en un BST aprovecha directamente la invariante:

- si la clave buscada es menor que la actual, bajás a la izquierda;
- si es mayor, bajás a la derecha;
- si coincide, terminaste.

```java
String buscar(Nodo raiz, int claveBuscada) {
    Nodo actual = raiz;

    while (actual != null) {
        if (claveBuscada == actual.clave) {
            return actual.valor;
        }

        if (claveBuscada < actual.clave) {
            actual = actual.izquierdo;
        } else {
            actual = actual.derecho;
        }
    }

    return null;
}
```

La diferencia con un árbol binario cualquiera es fuerte: ya no necesitás explorar ambos subárboles. En cada paso descartás la mitad "conceptual" que no puede contener la clave.

Por eso el costo depende de la **altura** del árbol, no de la cantidad total de nodos recorridos ciegamente.

## Insertar: preservar la invariante

Insertar un nodo nuevo sigue la misma lógica que buscar. Primero descendés hasta encontrar una posición nula donde la nueva clave respete el orden.

```java
Nodo insertar(Nodo raiz, int clave, String valor) {
    if (raiz == null) {
        return new Nodo(clave, valor);
    }

    if (clave < raiz.clave) {
        raiz.izquierdo = insertar(raiz.izquierdo, clave, valor);
    } else if (clave > raiz.clave) {
        raiz.derecho = insertar(raiz.derecho, clave, valor);
    } else {
        raiz.valor = valor; // política: actualizar valor si la clave ya existe
    }

    return raiz;
}
```

La operación es simple porque la invariante te dice exactamente por qué rama seguir. La dificultad no está en decidir por dónde bajar, sino en aceptar que el costo vuelve a depender de la forma que el árbol va tomando.

## Inorden: por qué devuelve las claves ordenadas

En [Árboles binarios](arboles_binarios.md) ya apareció el recorrido inorden. En un árbol binario cualquiera era solo una forma de visita. En un BST, en cambio, se vuelve especial:

1. recorrés el subárbol izquierdo,
2. visitás la raíz,
3. recorrés el subárbol derecho.

Como todo lo izquierdo es menor y todo lo derecho es mayor, el resultado aparece ordenado.

```java
void imprimirOrdenado(Nodo raiz) {
    if (raiz == null) {
        return;
    }

    imprimirOrdenado(raiz.izquierdo);
    System.out.println(raiz.clave + " -> " + raiz.valor);
    imprimirOrdenado(raiz.derecho);
}
```

Esa conexión entre **estructura** y **recorrido** es una de las razones por las que el BST resulta tan elegante: una sola invariante habilita búsqueda, inserción y recorrido ordenado.

## Borrar: el caso que obliga a pensar más

El borrado es la operación más delicada porque no alcanza con sacar un nodo: hay que hacerlo sin romper la invariante.

Hay tres casos clásicos.

### 1. Nodo hoja

Si el nodo no tiene hijos, eliminarlo es directo: su padre pasa a apuntar a `null`.

### 2. Nodo con un solo hijo

Si tiene solo un hijo, ese hijo ocupa su lugar en la estructura.

### 3. Nodo con dos hijos

Acá no podés borrar sin más. Necesitás reemplazar el nodo por otro valor que preserve el orden. La estrategia más común usa:

- el **sucesor inorden**: el menor nodo del subárbol derecho,
- o el **predecesor inorden**: el mayor del subárbol izquierdo.

```java
Nodo borrar(Nodo raiz, int clave) {
    if (raiz == null) {
        return null;
    }

    if (clave < raiz.clave) {
        raiz.izquierdo = borrar(raiz.izquierdo, clave);
        return raiz;
    }

    if (clave > raiz.clave) {
        raiz.derecho = borrar(raiz.derecho, clave);
        return raiz;
    }

    if (raiz.izquierdo == null) {
        return raiz.derecho;
    }

    if (raiz.derecho == null) {
        return raiz.izquierdo;
    }

    Nodo sucesor = minimo(raiz.derecho);
    raiz.clave = sucesor.clave;
    raiz.valor = sucesor.valor;
    raiz.derecho = borrar(raiz.derecho, sucesor.clave);
    return raiz;
}

Nodo minimo(Nodo raiz) {
    Nodo actual = raiz;
    while (actual.izquierdo != null) {
        actual = actual.izquierdo;
    }
    return actual;
}
```

No hace falta memorizar el código exacto. La idea que sí importa es esta: para borrar con dos hijos, reemplazás por una clave vecina en el orden total que el árbol ya sabe mantener.

## Costos: todo depende de la altura

En un BST, las operaciones principales siguen un camino raíz-hoja o algo muy cercano:

| Operación | Costo típico |
| :--- | :--- |
| Buscar | O(h) |
| Insertar | O(h) |
| Borrar | O(h) |
| Obtener mínimo o máximo | O(h) |

Donde `h` es la altura.

Si el árbol queda razonablemente compacto, `h` puede ser cercana a `log n`. Pero el BST simple no garantiza eso.

## El problema inmediato: la degradación

El BST es muy bueno si la forma acompaña. El problema es que la propia secuencia de inserciones puede deformarlo.

Si insertás:

```text
1, 2, 3, 4, 5
```

y siempre agregás como mayor, el árbol queda parecido a esto:

```{mermaid}
flowchart TD
    N1((1)) --> Null1([null])
    N1 --> N2((2))
    N2 --> Null2([null])
    N2 --> N3((3))
    N3 --> Null3([null])
    N3 --> N4((4))
    N4 --> Null4([null])
    N4 --> N5((5))
    
    style Null1 fill:#eeeeee,stroke:#9e9e9e
    style Null2 fill:#eeeeee,stroke:#9e9e9e
    style Null3 fill:#eeeeee,stroke:#9e9e9e
    style Null4 fill:#eeeeee,stroke:#9e9e9e
```

La estructura sigue siendo un BST correcto, pero su altura ya no se parece a la de un árbol compacto. Se volvió casi una lista enlazada (O(n)).

Ese ejemplo muestra el límite central del BST simple:

- la invariante de orden alcanza para dar operaciones útiles,
- pero no alcanza para controlar la altura.

## Qué lo vuelve útil en la práctica

El BST es un muy buen punto intermedio entre árbol general y estructuras más sofisticadas:

- organiza datos por clave,
- permite recorrido ordenado,
- habilita mínimos, máximos y búsquedas de vecinos,
- y sirve como base conceptual para diccionarios y conjuntos ordenados.

En bibliotecas estándar, esa intuición reaparece en estructuras ordenadas como `TreeSet` y `TreeMap`, aunque en la práctica esas implementaciones usan árboles balanceados para evitar degradación. Ese vínculo ya había aparecido en {ref}`java-colecciones`.

## Errores frecuentes

### Creer que cualquier árbol binario sirve para buscar igual

Sin la invariante de orden, un árbol binario no puede descartar ramas así nomás. El BST sí puede porque su estructura codifica información de orden.

### Ignorar la política de duplicados

Si el problema admite claves repetidas y no definís qué hacer con ellas, la implementación queda ambigua.

### Suponer que "árbol" implica siempre O(log n)

Eso no se puede afirmar para un BST simple. Solo vale cuando la altura queda bien comportada, y el BST no lo garantiza por sí mismo.

### Minimizar el caso de borrado

Borrar con dos hijos no es un detalle de código. Es el punto donde más visible se vuelve que hay que preservar la invariante, no solo quitar un nodo.

## Resumen

El BST agrega orden sobre la forma del árbol binario y con eso consigue una combinación muy potente: búsqueda dirigida, inserción estructurada y recorrido inorden ordenado.

Su límite aparece enseguida: toda esa elegancia depende de la altura. Si el árbol se deforma, la eficiencia se degrada con él. Por eso este capítulo funciona como antesala directa de [Árboles balanceados](arboles_balanceados.md), donde el balance deja de ser opcional y pasa a ser parte del contrato.

## Ejercicios

```{exercise}
:label: ex-parte5-bst-mini

Mostrá con un ejemplo de inserciones por qué un BST puede degradarse hasta parecerse a una lista. Indicá qué operación se vuelve especialmente afectada.
```

```{exercise}
:label: ex-parte5-bst-borrado

Explicá por qué borrar un nodo con dos hijos requiere elegir un sucesor o predecesor inorden. Justificá por qué reemplazarlo por cualquier otro nodo rompería la propiedad de búsqueda.
```

```{exercise}
:label: ex-parte5-bst-tree-map

Tomá un problema que necesite mantener elementos ordenados por clave y justificá por qué un BST es una buena abstracción conceptual, aunque una implementación real de producción prefiera una versión balanceada.
```

## Próximo paso

Para seguir, conviene pasar a [Árboles balanceados](arboles_balanceados.md), donde aparece la respuesta al problema de altura.
s.md), donde aparece la respuesta al problema de altura.
