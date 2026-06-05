---
title: "Árboles binarios de búsqueda"
subtitle: "Orden para buscar, insertar y recorrer"
subject: Estructuras de Datos
description: Cómo una invariante de orden convierte al árbol binario en una estructura útil para búsqueda, inserción, borrado y recorrido ordenado.
---

(parte6-arboles-busqueda)=
# Árboles binarios de búsqueda


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

En [Árboles binarios](arboles_binarios.md) apareció la forma base: cada nodo tiene a lo sumo hijo izquierdo e hijo derecho. El BST (Binary Search Tree) agrega una idea decisiva sobre esa forma: **orden**.

Si cada nodo respeta una relación de orden respecto de sus subárboles, la estructura deja de ser solo jerárquica y pasa a servir también para búsquedas eficientes, inserción dinámica y recorridos en orden creciente.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender la invariante de orden de un BST y cómo esa invariante permite búsqueda, inserción, borrado y recorrido ordenado.

**Prerrequisitos.** Conviene haber leído [Árboles binarios](arboles_binarios.md), porque el BST se apoya sobre esa forma estructural.

**Desarrollo.** Primero se fija la propiedad de búsqueda. Después se recorren búsqueda, inserción y borrado, y se cierra con el problema de la altura y el puente hacia [Árboles balanceados](arboles_balanceados.md).
:::

## Qué garantiza un BST

En un árbol binario de búsqueda, para cada nodo con clave `k` se cumple la **invariante de orden**:

1. toda clave del subárbol izquierdo es **menor** que `k`,
2. toda clave del subárbol derecho es **mayor** que `k`.

Esta regla debe cumplirse para *todos* los nodos, no solo para la raíz. Si mirás un nodo en lo profundo del árbol, sus descendientes también deben respetar esta jerarquía respecto de él.

### El problema de las claves repetidas

Si la estructura admite claves iguales, hace falta definir una política explícita para no romper la lógica de búsqueda:

- **Rechazarlas**: No permitir la inserción si la clave ya existe (común en conjuntos o `Set`).
- **Contarlas**: Guardar un contador de ocurrencias en el mismo nodo.
- **Lado fijo**: Definir que los iguales van siempre a la derecha (o izquierda). 

Lo importante es que la política sea consistente. En este apunte, asumiremos que las claves son únicas o que "insertar" una existente actualiza su valor asociado.

## El puente desde C: Arreglos vs. Árboles

Si recordás la **búsqueda binaria** en arreglos ordenados (que vimos en la [Parte 1](../../parte_1/09_arreglos.md)), ya conocés la idea de "dividir para conquistar". Saltás al medio, comparás y descartás la mitad.

Sin embargo, los arreglos tienen un costo oculto: **insertar o borrar** un elemento en el medio cuesta $O(N)$ porque hay que desplazar todos los elementos. 

El **BST** resuelve este compromiso:
- Mantiene la capacidad de descartar ramas en cada paso (como la búsqueda binaria).
- Permite actualizaciones dinámicas en $O(h)$ cambiando solo punteros (como una lista enlazada).

Es, en esencia, una estructura que intenta darnos lo mejor de los dos mundos: búsqueda rápida y flexibilidad para cambiar los datos.

## Buscar: seguir un solo camino

La búsqueda en un BST aprovecha directamente la invariante. En cada paso, el árbol nos "dice" por dónde seguir:

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

La diferencia con un árbol binario genérico es total: ya no necesitás explorar ambos subárboles (lo que sería $O(N)$). Acá recorrés un solo camino desde la raíz hasta el nodo o hasta una hoja. Por eso el costo es **proporcional a la altura** ($O(h)$).

## Insertar: preservar la invariante

Insertar un nodo nuevo sigue la misma lógica de "descenso" que buscar. Primero bajás hasta encontrar un hueco (`null`) que sea el lugar correcto para la nueva clave.

```java
Nodo insertar(Nodo raiz, int clave, String valor) {
    if (raiz == null) {
        return new Nodo(clave, valor); // Encontramos el lugar
    }

    if (clave < raiz.clave) {
        raiz.izquierdo = insertar(raiz.izquierdo, clave, valor);
    } else if (clave > raiz.clave) {
        raiz.derecho = insertar(raiz.derecho, clave, valor);
    } else {
        raiz.valor = valor; // Clave duplicada: actualizamos contenido
    }

    return raiz;
}
```

Al insertar, el árbol crece siempre por las hojas. La forma final del árbol dependerá enteramente del **orden en que lleguen los datos**.

## Inorden: el recorrido que "ordena"

En un BST, el recorrido **inorden** (izquierda, raíz, derecha) tiene una propiedad mágica: visita los nodos en orden creciente de sus claves.

Como todo lo izquierdo es menor y todo lo derecho es mayor, al procesar primero la izquierda, luego el medio y luego la derecha, obtenemos una secuencia ordenada. Esto permite implementar un "ordenamiento por árbol" (Tree Sort) simplemente insertando elementos y recorriéndolos en inorden.

## Borrar: el desafío de los dos hijos

El borrado es la operación más compleja porque eliminar un nodo interno deja "huérfanos" a sus subárboles. Hay tres escenarios:

1. **Nodo hoja**: Se elimina sin más (el padre apunta a `null`).
2. **Nodo con un hijo**: El hijo "sube" y ocupa el lugar del padre borrado.
3. **Nodo con dos hijos**: No podemos subir a ambos. Necesitamos un reemplazo que preserve la invariante.

### El uso del sucesor inorden

Para borrar un nodo con dos hijos, buscamos su **sucesor inorden** (el nodo con la clave más chica del subárbol derecho). 

**¿Por qué ese?** Porque es el nodo "inmediatamente mayor" al que estamos borrando. Al ponerlo en la raíz de ese subárbol, garantizamos que seguirá siendo mayor que todo el subárbol izquierdo y menor que el resto del subárbol derecho.

```java
Nodo borrar(Nodo raiz, int clave) {
    if (raiz == null) return null;

    if (clave < raiz.clave) {
        raiz.izquierdo = borrar(raiz.izquierdo, clave);
    } else if (clave > raiz.clave) {
        raiz.derecho = borrar(raiz.derecho, clave);
    } else {
        // Encontramos el nodo a borrar
        if (raiz.izquierdo == null) return raiz.derecho;
        if (raiz.derecho == null) return raiz.izquierdo;

        // Caso 2 hijos: buscar el mínimo del subárbol derecho
        Nodo sucesor = minimo(raiz.derecho);
        raiz.clave = sucesor.clave;
        raiz.valor = sucesor.valor;
        raiz.derecho = borrar(raiz.derecho, sucesor.clave);
    }
    return raiz;
}
```

## Costos y la amenaza de la degradación

En un BST, las operaciones principales (`buscar`, `insertar`, `borrar`) tienen un costo de **$O(h)$**, donde $h$ es la altura.

Si el árbol está balanceado, $h \approx \log_2 N$. Pero si los datos llegan en un orden "malo" (por ejemplo, ya ordenados: 1, 2, 3, 4...), el árbol se deforma:

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

En este caso, la altura $h$ es igual a $N$. Las operaciones pasan de ser $O(\log N)$ a ser $O(N)$. El árbol "se convierte" en una lista enlazada y perdemos toda la ventaja de la estructura.

:::{warning} El gran límite del BST
El BST simple es una estructura **pasiva**: no hace nada para corregir su forma si los datos vienen sesgados. Confía en que el azar mantendrá el árbol balanceado, lo cual es un riesgo inaceptable en sistemas críticos.
:::

## Resumen

El BST es la base de las estructuras de búsqueda eficiente. Su elegancia radica en cómo una simple regla de orden habilita algoritmos poderosos. Sin embargo, su dependencia crítica de la altura lo vuelve frágil frente a datos ordenados o patrones específicos de inserción.

Esta fragilidad es la que motiva el siguiente paso: los [Árboles balanceados](arboles_balanceados.md), que agregan "inteligencia" a la inserción y el borrado para garantizar que la altura nunca se escape del control logarítmico.

## Ejercicios

```{exercise}
:label: ex-parte6-bst-secuencia

Dibujá el BST resultante de insertar la secuencia: `[15, 10, 20, 5, 12, 17, 25]`. Luego, realizá el recorrido inorden y verificá si el resultado está ordenado.
```

```{exercise}
:label: ex-parte6-bst-borrado-logica

Dada la estructura del ejercicio anterior, borrá el nodo `15`. ¿Qué nodo elegiste como sucesor? Dibujá cómo queda el árbol final.
```

```{exercise}
:label: ex-parte6-bst-peor-caso

Si un BST tiene 1000 nodos, ¿cuál es la altura mínima posible y cuál es la máxima? ¿Qué implicancia tiene esto en el tiempo de respuesta de una búsqueda?
```

## Próximo paso

Una vez entendido el potencial y el riesgo del BST, estamos listos para estudiar cómo las **rotaciones** pueden salvarnos de la degradación en [Árboles balanceados](arboles_balanceados.md).
