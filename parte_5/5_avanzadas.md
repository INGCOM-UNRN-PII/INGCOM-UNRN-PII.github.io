---
title: "Especificaciones algebraicas completas 2"
description: "Sets, diccionarios, arboles y grafos."
---

(avanzadas-tda)=
# Especificaciones algebraicas avanzadas


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

Este capitulo completa el catalogo algebraico de la parte con cuatro familias que aparecen todo el tiempo en la practica: sets, diccionarios, arboles y grafos. La idea no es fijar una unica implementacion, sino mostrar como se expresa el contrato de cada estructura, que variantes son habituales y cual es la version simplificada que conviene usar cuando queres enseñar la idea sin cargar demasiada maquinaria.

En todos los casos, la tecnica es la misma: definir sorts, operaciones, axiomas e invariantes. Lo que cambia es el vocabulario de cada familia.

:::{tip} Objetivos de Aprendizaje

Al finalizar este capitulo, se espera que el estudiante pueda:

1. Especificar algebraicamente un set, un diccionario, un arbol y un grafo.
2. Reconocer las variantes mas comunes de cada estructura.
3. Distinguir una especificacion general de una version simplificada.
4. Traducir axiomas entre distintas familias de estructuras.
:::

:::{note} Hoja de ruta del capitulo

**Prerequisitos.** Conviene haber leido [Introduccion a Tipos de Datos Abstractos](#introtda) y [Estructuras de Datos: Especificaciones Algebraicas Completas](#estructuras-tda).

**Desarrollo.** Primero se presentan las cuatro especificaciones base. Despues, cada familia se acompaña con variantes comunes y una variante simplificada.
:::

## TDA Set

Un set representa una coleccion finita de elementos sin repetidos y sin orden observable. La igualdad no depende de la forma de construccion, sino del contenido.

### Sorts y signatura

$$\begin{align}
\text{Sorts:} \quad & \mathtt{Set}, \mathtt{Element}, \mathtt{Bool}, \mathbb{N} \\
\\
\text{Generadores:} \\
\quad & \text{empty} : \to \mathtt{Set} \\
\\
\text{Modificadores:} \\
\quad & \text{add} : \mathtt{Set} \times \mathtt{Element} \to \mathtt{Set} \\
\quad & \text{remove} : \mathtt{Set} \times \mathtt{Element} \to \mathtt{Set} \\
\\
\text{Observadores:} \\
\quad & \text{contains} : \mathtt{Set} \times \mathtt{Element} \to \mathtt{Bool} \\
\quad & \text{size} : \mathtt{Set} \to \mathbb{N} \\
\quad & \text{isEmpty} : \mathtt{Set} \to \mathtt{Bool} \\
\quad & \text{union} : \mathtt{Set} \times \mathtt{Set} \to \mathtt{Set} \\
\quad & \text{intersect} : \mathtt{Set} \times \mathtt{Set} \to \mathtt{Set} \\
\quad & \text{difference} : \mathtt{Set} \times \mathtt{Set} \to \mathtt{Set} \\
\quad & \text{subsetEq} : \mathtt{Set} \times \mathtt{Set} \to \mathtt{Bool}
\end{align}$$

### Axiomas

**Axioma 1 (empty no contiene elementos).**
$$\text{contains}(\text{empty}, e) = \text{false}$$

**Axioma 2 (add agrega presencia).**
$$\text{contains}(\text{add}(s, e), e) = \text{true}$$

**Axioma 3 (add es idempotente).**
$$\text{add}(\text{add}(s, e), e) = \text{add}(s, e)$$

**Axioma 4 (remove elimina presencia).**
$$\text{contains}(\text{remove}(s, e), e) = \text{false}$$

**Axioma 5 (remove de un elemento ausente no cambia el set).**
$$\text{contains}(s, e) = \text{false} \implies \text{remove}(s, e) = s$$

**Axioma 6 (union con empty).**
$$\text{union}(s, \text{empty}) = s \quad \land \quad \text{union}(\text{empty}, s) = s$$

**Axioma 7 (extensionalidad).**
$$\text{subsetEq}(s_1, s_2) = \text{true} \iff \forall e,\ \text{contains}(s_1, e) \Rightarrow \text{contains}(s_2, e)$$

**Axioma 8 (igualdad por contenido).**
$$s_1 = s_2 \iff \forall e,\ \text{contains}(s_1, e) = \text{contains}(s_2, e)$$

### Variantes comunes

| Variante | Rasgo distintivo | Que suele cambiar en el contrato |
| :--- | :--- | :--- |
| Set mutable | Permite `add` y `remove` sobre el mismo estado abstracto | La especificacion debe dejar claro si `remove` sobre un elemento ausente es identidad. |
| Set ordenado | Expone un orden observable sobre los elementos | Aparecen observadores como `min`, `max`, `predecessor` y `successor`. |
| Set acotado | Trabaja sobre un universo finito y conocido | `contains` puede razonarse como un vector booleano finito. |
| Set persistente | Cada operacion devuelve un set nuevo sin mutar el anterior | Las ecuaciones deben expresar comparticion semantica, no mutacion. |

Estas variantes cambian la manera de leer el contrato, pero no el principio central: dos sets son iguales si contienen los mismos elementos. Cuando el set deja de ser "solo pertenencia" y empieza a exponer orden o finitud, la signatura gana observadores nuevos.

### Variante simplificada

La version simplificada mas util es el **set sobre universo finito**. En vez de modelar cualquier elemento, se toma un universo fijo $U = \{0, \dots, n-1\}$ y el set se especifica solo con una interfaz minima:

$$\text{add},\ \text{remove},\ \text{contains},\ \text{empty}$$

En esa version, basta con dejar explícitos cuatro hechos:

1. `empty` no contiene nada.
2. `add` vuelve verdadera la pertenencia del elemento agregado.
3. `remove` vuelve falsa la pertenencia del elemento removido.
4. `add` es idempotente.

Eso reduce el esfuerzo de axiomatizacion porque `contains` se comporta como acceso a una posicion booleana. Si queres formalizar un set un poco mas rico sin perder simplicidad, esta es la variante de partida.

## TDA Dictionary

Un diccionario asocia claves con valores. La clave es unica; el valor puede cambiar con una insercion nueva sobre la misma clave.

### Sorts y signatura

$$\begin{align}
\text{Sorts:} \quad & \mathtt{Dict}, \mathtt{Key}, \mathtt{Value}, \mathtt{Bool}, \mathtt{Nat}, \mathtt{MaybeValue} \\
\\
\text{Generadores:} \\
\quad & \text{empty} : \to \mathtt{Dict} \\
\quad & \text{none} : \to \mathtt{MaybeValue} \\
\quad & \text{some} : \mathtt{Value} \to \mathtt{MaybeValue} \\
\\
\text{Modificadores:} \\
\quad & \text{put} : \mathtt{Dict} \times \mathtt{Key} \times \mathtt{Value} \to \mathtt{Dict} \\
\quad & \text{remove} : \mathtt{Dict} \times \mathtt{Key} \to \mathtt{Dict} \\
\\
\text{Observadores:} \\
\quad & \text{lookup} : \mathtt{Dict} \times \mathtt{Key} \to \mathtt{MaybeValue} \\
\quad & \text{containsKey} : \mathtt{Dict} \times \mathtt{Key} \to \mathtt{Bool} \\
\quad & \text{size} : \mathtt{Dict} \to \mathtt{Nat}
\end{align}$$

### Axiomas

**Axioma 1 (empty no contiene claves).**
$$\text{containsKey}(\text{empty}, k) = \text{false}$$

**Axioma 2 (put hace visible la clave).**
$$\text{containsKey}(\text{put}(m, k, v), k) = \text{true}$$

**Axioma 3 (lookup de una clave cargada devuelve su valor).**
$$\text{lookup}(\text{put}(m, k, v), k) = \text{some}(v)$$

**Axioma 4 (put sobre una clave distinta preserva el resto).**
$$k \neq k' \implies \text{lookup}(\text{put}(m, k, v), k') = \text{lookup}(m, k')$$

**Axioma 5 (remove elimina la clave).**
$$\text{containsKey}(\text{remove}(m, k), k) = \text{false}$$

**Axioma 6 (remove de una clave ausente no cambia el diccionario).**
$$\text{containsKey}(m, k) = \text{false} \implies \text{remove}(m, k) = m$$

**Axioma 7 (size cuenta claves distintas).**
$$\text{size}(\text{put}(m, k, v)) =
\begin{cases}
\text{size}(m) & \text{si } \text{containsKey}(m, k) = \text{true} \\
\text{size}(m) + 1 & \text{si } \text{containsKey}(m, k) = \text{false}
\end{cases}$$

### Variantes comunes

| Variante | Rasgo distintivo | Que suele cambiar en el contrato |
| :--- | :--- | :--- |
| Map o diccionario simple | Cada clave tiene un solo valor asociado | `put` reemplaza el valor anterior y `lookup` devuelve a lo sumo un resultado. |
| Diccionario ordenado | Las claves tienen orden observable | Aparecen consultas por rango y extremos. |
| Multidiccionario | Una clave puede tener varios valores | `lookup` devuelve una coleccion y `remove` puede eliminar una ocurrencia o todas. |
| Diccionario hash | La ubicacion depende de una funcion de dispersion | La especificacion suele ocultar el almacenamiento y solo exigir acceso promedio eficiente. |

En el diccionario simple, la propiedad clave es que la igualdad de mapas depende de pares `clave -> valor`, no de la historia de inserciones. Cuando agregas orden, el contrato deja de ser solo asociativo y pasa a incluir recorridos. Cuando agregas multiplicidad, `lookup` ya no puede seguir siendo un unico valor.

### Variante simplificada

La version simplificada mas didactica es el **diccionario parcial sobre claves acotadas**. Se fija un universo de claves finito y se evita modelar iteradores, vistas o estructura interna. El contrato minimo queda asi:

$$\text{empty},\ \text{put},\ \text{remove},\ \text{lookup},\ \text{containsKey}$$

Los axiomas esenciales son:

1. `lookup(empty, k) = none`.
2. `put` sobre una clave nueva produce `some(v)`.
3. `put` sobre una clave existente reemplaza el valor.
4. `remove` elimina la clave y hace que `containsKey` vuelva a falso.

Con eso alcanza para enseñar el contrato semantico sin sumar complejidad accidental. Es la version mas util cuando queres conectar algebra de TDAs con estructuras de mapas concretas.

## TDA Tree

Un arbol representa una estructura jerarquica con una raiz y subarboles. La especificacion cambia segun si el arbol es general, binario o binario de busqueda.

### Sorts y signatura

$$\begin{align}
\text{Sorts:} \quad & \mathtt{Tree}, \mathtt{Element}, \mathtt{MaybeElement}, \mathtt{Bool}, \mathtt{Nat} \\
\\
\text{Generadores:} \\
\quad & \text{empty} : \to \mathtt{Tree} \\
\quad & \text{none} : \to \mathtt{MaybeElement} \\
\quad & \text{some} : \mathtt{Element} \to \mathtt{MaybeElement} \\
\quad & \text{leaf} : \mathtt{Element} \to \mathtt{Tree} \\
\quad & \text{node} : \mathtt{Element} \times \mathtt{Tree} \times \mathtt{Tree} \to \mathtt{Tree} \\
\\
\text{Observadores:} \\
\quad & \text{root} : \mathtt{Tree} \to \mathtt{MaybeElement} \\
\quad & \text{left} : \mathtt{Tree} \to \mathtt{Tree} \\
\quad & \text{right} : \mathtt{Tree} \to \mathtt{Tree} \\
\quad & \text{isEmpty} : \mathtt{Tree} \to \mathtt{Bool} \\
\quad & \text{isLeaf} : \mathtt{Tree} \to \mathtt{Bool} \\
\quad & \text{size} : \mathtt{Tree} \to \mathtt{Nat} \\
\quad & \text{height} : \mathtt{Tree} \to \mathtt{Nat}
\end{align}$$

### Axiomas

**Axioma 1 (empty no tiene raiz).**
$$\text{root}(\text{empty}) = \text{none}$$

**Axioma 2 (leaf tiene raiz y es hoja).**
$$\text{root}(\text{leaf}(e)) = \text{some}(e) \quad \land \quad \text{isLeaf}(\text{leaf}(e)) = \text{true}$$

**Axioma 3 (node conserva la raiz).**
$$\text{root}(\text{node}(e, l, r)) = \text{some}(e)$$

**Axioma 4 (empty no tiene hijos).**
$$\text{left}(\text{empty}) = \text{empty} \quad \land \quad \text{right}(\text{empty}) = \text{empty}$$

**Axioma 5 (size suma subarboles).**
$$\text{size}(\text{node}(e, l, r)) = 1 + \text{size}(l) + \text{size}(r)$$

**Axioma 6 (height toma el mayor camino).**
$$\text{height}(\text{node}(e, l, r)) = 1 + \max(\text{height}(l), \text{height}(r))$$

### Variantes comunes

| Variante | Rasgo distintivo | Que suele cambiar en el contrato |
| :--- | :--- | :--- |
| Arbol general | Un nodo puede tener cualquier cantidad de hijos | La operacion `children` reemplaza a `left` y `right`. |
| Arbol binario | Cada nodo tiene a lo sumo dos hijos | La estructura de nodo queda fijada por raiz, izquierdo y derecho. |
| Arbol binario de busqueda | El orden de las claves queda restringido por la raiz | El contrato agrega la propiedad de orden total entre subarboles. |
| Arbol balanceado | Mantiene una cota sobre la altura | Aparecen axiomas sobre rotaciones y altura acotada. |
| Trie | La forma del arbol sigue prefijos de claves | La ruta desde la raiz representa una clave compuesta. |

En arboles, la variante elegida cambia mucho mas que el nombre: cambia la forma en que se interpreta la jerarquia. Un arbol general sirve para modelar pertenencia estructural; un BST agrega orden; un balanceado agrega restriccion sobre la forma; un trie agrega prefijos.

### Variante simplificada

La version simplificada mas clara es el **arbol binario puro**, donde solo importan la raiz y los dos hijos. Se omiten el orden de busqueda, el balanceo y cualquier politica de rebalanceo. El contrato minimo queda reducido a:

$$\text{empty},\ \text{leaf},\ \text{node},\ \text{root},\ \text{left},\ \text{right},\ \text{size},\ \text{height}$$

Los axiomas que conviene conservar son:

1. `empty` no tiene raiz.
2. `leaf(e)` tiene raiz `e` y altura uno.
3. `node(e, l, r)` conserva la raiz `e`.
4. `size` suma uno mas los tamanos de ambos hijos.
5. `height` toma el maximo de las alturas de sus hijos mas uno.

Con esa base se puede escalar despues hacia arboles de busqueda, heaps o arboles balanceados sin cambiar la idea central del contrato.

## TDA Graph

Un grafo modela relaciones entre vertices. Segun el caso, esas relaciones pueden ser dirigidas, ponderadas o multiples.

### Sorts y signatura

$$\begin{align}
\text{Sorts:} \quad & \mathtt{Graph}, \mathtt{Vertex}, \mathtt{Bool}, \mathtt{Nat}, \mathtt{Weight} \\
\\
\text{Generadores:} \\
\quad & \text{empty} : \to \mathtt{Graph} \\
\\
\text{Modificadores:} \\
\quad & \text{addVertex} : \mathtt{Graph} \times \mathtt{Vertex} \to \mathtt{Graph} \\
\quad & \text{removeVertex} : \mathtt{Graph} \times \mathtt{Vertex} \to \mathtt{Graph} \\
\quad & \text{addEdge} : \mathtt{Graph} \times \mathtt{Vertex} \times \mathtt{Vertex} \to \mathtt{Graph} \\
\quad & \text{removeEdge} : \mathtt{Graph} \times \mathtt{Vertex} \times \mathtt{Vertex} \to \mathtt{Graph} \\
\\
\text{Observadores:} \\
\quad & \text{hasVertex} : \mathtt{Graph} \times \mathtt{Vertex} \to \mathtt{Bool} \\
\quad & \text{adjacent} : \mathtt{Graph} \times \mathtt{Vertex} \times \mathtt{Vertex} \to \mathtt{Bool} \\
\quad & \text{neighbors} : \mathtt{Graph} \times \mathtt{Vertex} \to \mathtt{Set} \\
\quad & \text{degree} : \mathtt{Graph} \times \mathtt{Vertex} \to \mathtt{Nat} \\
\quad & \text{order} : \mathtt{Graph} \to \mathtt{Nat}
\end{align}$$

### Axiomas

**Axioma 1 (empty no tiene vertices ni aristas).**
$$\text{hasVertex}(\text{empty}, v) = \text{false} \quad \land \quad \text{adjacent}(\text{empty}, u, v) = \text{false}$$

**Axioma 2 (addVertex hace visible el vertice).**
$$\text{hasVertex}(\text{addVertex}(g, v), v) = \text{true}$$

**Axioma 3 (addEdge requiere vertices visibles).**
$$\text{adjacent}(\text{addEdge}(g, u, v), u, v) = \text{true}$$

**Axioma 4 (removeEdge elimina la adyacencia).**
$$\text{adjacent}(\text{removeEdge}(g, u, v), u, v) = \text{false}$$

**Axioma 5 (removeVertex elimina toda incidencia).**
$$\text{hasVertex}(\text{removeVertex}(g, v), v) = \text{false}$$

**Axioma 6 (degree cuenta vecinos).**
$$\text{degree}(g, v) = |\text{neighbors}(g, v)|$$

**Axioma 7 (order cuenta vertices).**
$$\text{order}(\text{addVertex}(g, v)) =
\begin{cases}
\text{order}(g) & \text{si } \text{hasVertex}(g, v) = \text{true} \\
\text{order}(g) + 1 & \text{si } \text{hasVertex}(g, v) = \text{false}
\end{cases}$$

### Variantes comunes

| Variante | Rasgo distintivo | Que suele cambiar en el contrato |
| :--- | :--- | :--- |
| Grafo simple | No tiene lazos ni aristas repetidas | `adjacent` es una relacion booleana entre vertices distintos. |
| Grafo dirigido | La relacion depende del sentido | Hay que distinguir `outNeighbors` de `inNeighbors`. |
| Grafo ponderado | Cada arista tiene un peso | El contrato agrega `weight` y restricciones sobre la existencia de peso. |
| Multigrafo | Puede repetir aristas entre vertices | `edgeCount` y multiplicidad pasan a ser observables. |
| Grafo con lazos | Permite aristas de un vertice hacia si mismo | `adjacent(g, v, v)` deja de ser un caso prohibido. |

La variante simple es la base pedagogica mas estable. Las otras se usan cuando el problema real exige direccion, costo o multiplicidad. En todos los casos, la pregunta algebraica sigue siendo la misma: que significa que dos grafos sean iguales, y que operaciones cambian realmente el estado.

### Variante simplificada

La version simplificada mas util es el **grafo simple no dirigido y no ponderado**. En esa version, basta con una interfaz reducida:

$$\text{empty},\ \text{addVertex},\ \text{removeVertex},\ \text{addEdge},\ \text{removeEdge},\ \text{adjacent},\ \text{neighbors}$$

El contrato minimo deberia dejar claros estos puntos:

1. `addVertex` hace visible al vertice.
2. `addEdge` solo agrega incidencia entre vertices existentes.
3. `removeEdge` elimina una incidencia concreta.
4. `neighbors` devuelve exactamente los vertices adyacentes.
5. `adjacent` es simetrica porque el grafo es no dirigido.

Eso alcanza para enseñar conectividad, recorrido y razonamiento sobre incidencia sin mezclar pesos ni orientacion. Si despues queres agregar direccion o ponderacion, la simplificacion sirve como punto de partida limpio.

## Resumen

Los cuatro TDAs comparten la misma forma de especificacion, pero cambian los axiomas que definen identidad, ausencia, reemplazo y relacion entre observadores.

- En un **set**, la igualdad depende del contenido.
- En un **diccionario**, la clave es unica y `lookup` preserva la semantica de reemplazo.
- En un **arbol**, la estructura jerarquica domina y las medidas como `size` y `height` expresan su forma.
- En un **grafo**, la incidencia entre vertices y aristas es el centro de la especificacion.

La version simplificada de cada uno sirve para introducir la idea sin arrastrar variantes de implementacion o detalles de uso que distraen del contrato algebraico.

## Ejercicios

```{exercise}
:label: ex-tda-avanzadas

Resolvé estas consignas:

1. Escribi dos axiomas mas para el TDA Set que capturen la conmutatividad y la asociatividad de `union`.
2. Reescribi el axioma de `lookup` del diccionario usando un tipo `MaybeValue` completo con `none` y `some`.
3. Agregá un axioma para `height` en el caso de `empty` para el TDA Tree.
4. Proponé una operacion adicional para el TDA Graph y explicá que propiedad deberia satisfacer.
```

## Proximo paso

Si queres seguir, el paso natural es aplicar estas especificaciones a una implementacion concreta y verificar que los tests reproduzcan los axiomas.
