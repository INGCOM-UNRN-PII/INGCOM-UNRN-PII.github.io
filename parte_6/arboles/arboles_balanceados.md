---
title: "Árboles balanceados"
subtitle: "Sostener altura razonable de manera activa"
subject: Estructuras de Datos
description: Cómo controlar la altura de un árbol de búsqueda para que las operaciones sigan siendo eficientes incluso con inserciones adversas.
---

(parte5-arboles-balanceados)=
# Árboles balanceados

Los árboles balanceados aparecen cuando ya no alcanza con “esperar” que un BST quede razonablemente bien formado. Si el problema requiere garantías de rendimiento, el balance pasa a ser parte explícita y activa del diseño.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender por qué el balance no es un detalle estético, sino una condición para sostener la eficiencia de operaciones sobre árboles de búsqueda.

**Prerrequisitos.** Conviene haber leído [Árboles binarios de búsqueda](arboles_busqueda.md), porque este capítulo resuelve el problema de la degradación de altura.

**Desarrollo.** Se introducen las rotaciones como mecanismo de cambio de forma, se explican las estrategias AVL y Red-Black, y se comparan sus trade-offs en sistemas reales.
:::

## El mecanismo de las rotaciones

Las rotaciones son el "átomo" del rebalanceo. Permiten cambiar la forma del árbol sin alterar la invariante de orden. 

Imaginá un desbalance simple (tres nodos en línea):

```{mermaid}
flowchart TD
    subgraph Antes [Antes de Rotar]
        direction TB
        A1((3)) --> A2((2))
        A1 --> Null1([null])
        A2 --> A3((1))
        A2 --> Null2([null])
        style A1 fill:#ffcdd2,stroke:#d32f2f
    end

    subgraph Operacion [Rotación Simple Derecha]
        direction LR
        Op{{"3 baja a la derecha\n2 sube a la raíz"}}
    end

    subgraph Despues [Después de Rotar]
        direction TB
        B2((2)) --> B1((1))
        B2 --> B3((3))
        style B2 fill:#c8e6c9,stroke:#388e3c
    end

    Antes --- Operacion --- Despues
    
    style Null1 fill:#eeeeee,stroke:#9e9e9e
    style Null2 fill:#eeeeee,stroke:#9e9e9e
```

Para balancear esto, realizamos una **rotación simple a la derecha** sobre el nodo 3:
1. El nodo 2 "sube" a la posición de la raíz.
2. El nodo 3 se convierte en su hijo derecho.
3. El orden se preserva: si hacés un recorrido inorden antes y después, el resultado sigue siendo `[1, 2, 3]`.
4. La altura se reduce: pasamos de 3 niveles a solo 2.

Existen cuatro casos de desbalance (Simple Derecha, Simple Izquierda y las rotaciones dobles correspondientes) que permiten corregir cualquier deformación local tras una inserción o borrado.

## AVL: El rigor de la altura

Un árbol **AVL** (Adelson-Velsky y Landis) es un BST que impone una restricción de altura muy estricta:

> Para cada nodo, la diferencia de altura entre sus subárboles izquierdo y derecho (llamada **Factor de Balance**) debe ser como máximo 1.

$$FB = \text{altura}(izquierdo) - \text{altura}(derecho)$$

```{mermaid}
flowchart TD
    N20((20)) --> N10((10))
    N20 --> N30((30))
    N30 --> N25((25))
    N30 --> N35((35))
    
    subgraph Legend [Factor de Balance]
        N20 --- L20["FB: -1"]
        N10 --- L10["FB: 0"]
        N30 --- L30["FB: 0"]
    end
    
    style N20 fill:#e3f2fd,stroke:#1565c0
    style N10 fill:#e3f2fd,stroke:#1565c0
    style N30 fill:#e3f2fd,stroke:#1565c0
    style N25 fill:#e3f2fd,stroke:#1565c0
    style N35 fill:#e3f2fd,stroke:#1565c0
```

Si tras una operación el $|FB| > 1$, se aplica una rotación. Esta vigilancia constante garantiza que el árbol esté siempre "muy apretado", logrando búsquedas extremadamente veloces.

**Costo:** Cada inserción o borrado puede requerir recalcular alturas y realizar rotaciones hacia arriba hasta la raíz.

## Red-Black Tree: Balance relajado para alto rendimiento

Los **Red-Black Trees** (Árboles Rojo-Negro) usan una estrategia distinta. En lugar de medir alturas exactas, pintan los nodos de dos colores y siguen un conjunto de reglas (como: "un nodo rojo no puede tener un hijo rojo").

```{mermaid}
flowchart TD
    N20((20)) --> N10((10))
    N20 --> N30((30))
    N10 --> N5((5))
    N10 --> N15((15))
    
    classDef black fill:#192437,color:#fff,stroke:#000
    classDef red fill:#eb2141,color:#fff,stroke:#000
    
    class N20,N5,N15,N30 black;
    class N10 red;
```

Estas reglas garantizan que el camino más largo desde la raíz hasta una hoja no sea más del doble de largo que el camino más corto.

- **Ventaja:** Son "menos estrictos" que los AVL. Al permitir un poco más de holgura en la altura, realizan menos rotaciones durante las inserciones y borrados.
- **Uso real:** Es la estructura que usan `TreeMap` y `TreeSet` en Java. Se prefiere para bibliotecas de propósito general porque ofrece el mejor compromiso entre velocidad de búsqueda y velocidad de actualización.

## Comparativa: ¿Cuál elegir?

| Criterio | AVL | Red-Black |
| :--- | :--- | :--- |
| **Búsqueda** | Más rápida (árbol más compacto). | Muy rápida, pero un poco menos que AVL. |
| **Inserción/Borrado** | Más lenta (más rotaciones). | Más rápida (menos rotaciones). |
| **Memoria** | Necesita guardar alturas (un `int`). | Necesita guardar el color (un `boolean`). |
| **Uso ideal** | Bases de datos con pocas escrituras. | Estructuras en memoria con cambios frecuentes. |

## Conexión con Java y el mundo real

Como vimos en {ref}`java-colecciones`, Java nos abstrae de estas implementaciones. Cuando creás un `TreeMap`, el lenguaje gestiona internamente los colores y las rotaciones para que tus búsquedas siempre sean $O(\log N)$. 

Entender el balanceo te permite saber por qué un `TreeMap` no se vuelve lento de repente (a diferencia de lo que podría pasar con un BST manual) y por qué las operaciones de ordenamiento dinámico son tan potentes.

## Resumen

Los árboles balanceados son la respuesta de la ingeniería al caos de los datos. No confían en que los datos lleguen en un orden amigable; toman el control de su propia forma mediante rotaciones para garantizar que la altura se mantenga siempre bajo control logarítmico.

## Ejercicios

```{exercise}
:label: ex-parte5-balanceado-fb

Dada una raíz con un subárbol izquierdo de altura 5 y un derecho de altura 3, calculá el Factor de Balance. ¿Es un árbol AVL válido? ¿Hacia qué lado está desbalanceado?
```

```{exercise}
:label: ex-parte5-balanceado-java

Investigá por qué Java eligió Red-Black Trees para sus colecciones en lugar de AVL. ¿Qué característica del Red-Black lo vuelve más atractivo para una biblioteca estándar?
```

```{exercise}
:label: ex-parte5-balanceado-rotacion

Dibujá una rotación simple a la izquierda sobre tres nodos `[10, 20, 30]` que están insertados en orden creciente. Mostrá el estado antes y después.
```

## Próximo paso

Con la búsqueda controlada, podemos pasar a una estructura que se olvida del orden total para enfocarse en la prioridad máxima: los [Heaps](heaps.md).
