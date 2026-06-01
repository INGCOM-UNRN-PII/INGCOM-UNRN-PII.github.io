---
title: "Introducción a Tipos de Datos Abstractos"
description: "Conceptos algebraicos fundamentales para especificar estructuras de datos con rigor formal."
---

(introtda)=
# Introducción a Tipos de Datos Abstractos

Un tipo de dato abstracto (TDA) es una especificación formal de una estructura de datos que describe *qué operaciones se pueden hacer* y *qué propiedades deben cumplir*, sin comprometerse con *cómo se implementan*. Esta separación entre especificación e implementación es central en el diseño de software robusto.

A lo largo de este capítulo, vas a aprender a escribir especificaciones algebraicas rigurosas usando la notación estándar de la teoría de tipos abstractos. Eso significa definir explícitamente los *sorts*, las *operaciones*, los *axiomas* que las gobiernan, y las *demostraciones* que validan propiedades correctas.

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, se espera que el estudiante pueda:

1. Entender la diferencia entre especificación e implementación de un TDA.
2. Definir formalmente los componentes de un TDA: sorts, operaciones, axiomas.
3. Escribir axiomas que capturen el comportamiento esperado de un TDA.
4. Argumentar informalmente por qué un axioma es correcto.
5. Reconocer cuándo dos especificaciones algebraicas son equivalentes.
:::

:::{note} Hoja de ruta del capítulo

**Prerrequisitos.** Lógica proposicional básica, notación matemática elemental.

**Desarrollo.** Comenzamos con la noción de especificación y por qué importa. Luego introducimos sorts, operaciones y axiomas. Finalizamos con demostraciones algebraicas simples y la validación de propiedades.
:::

## ¿Qué es un tipo de dato abstracto?

Cuando escribís código, trabajás con objetos concretos: una `Stack` en Java, una `List` en Python, una estructura de pila en C. Pero cada una de esas implementaciones es simplemente *una* forma de realizar la idea abstracta de "pila".

La pregunta es: ¿cuál es la idea abstracta? ¿Qué debe cumplir cualquier cosa que pretenda ser una pila?

### Especificación vs. Implementación

Una **especificación** responde: "¿qué se promete?". Por ejemplo:
- Podés agregar un elemento a la pila.
- Podés sacar el elemento que agregaste más recientemente.
- Si agregás un elemento y luego lo sacás, la pila vuelve a su estado anterior.

Una **implementación** responde: "¿cómo se cumple esa promesa?". Por ejemplo:
- Usaré un arreglo dinámico.
- El tope será un puntero al último elemento.
- Las operaciones de agregar y sacar serán O(1) amortizado.

Lo crucial es que **una especificación correcta debería funcionar con muchas implementaciones distintas**. Eso es lo que permite que diferentes lenguajes, bibliotecas y proyectos compartan la misma semántica de "pila".

### Por qué importa

La especificación algebraica te obliga a:

1. **Pensar claramente** qué promete tu estructura de datos.
2. **Escribir axiomas** que capturen esas promesas de modo preciso.
3. **Validar** que la implementación cumple los axiomas.
4. **Cambiar implementaciones** sin cambiar el contrato con tus usuarios.

## Sorts, operaciones y signaturas

Un tipo de dato abstracto se define por sus **sorts** (tipos base), sus **operaciones** y su **signatura** (la forma de cada operación).

### Sorts

Un **sort** es una colección abstracta de valores. Piensa en él como el tipo más general posible, sin precisar nada sobre cómo se representan los valores.

Ejemplos:
- El sort $\mathtt{Nat}$ representa los números naturales.
- El sort $\mathtt{Bool}$ representa los valores de verdad.
- El sort $\mathtt{Stack}$ representa cualquier pila.

Usamos **variables de sort** para referirnos a valores arbitrarios. Por ejemplo:
- $x, y \in \mathtt{Nat}$: dos números naturales cualesquiera.
- $s \in \mathtt{Stack}$: una pila cualquiera.

### Operaciones

Una **operación** (o función) recibe valores de ciertos sorts y retorna un valor de otro sort. La notación estándar es:

$$\text{nombre} : \text{sort}_1 \times \text{sort}_2 \times \cdots \times \text{sort}_n \to \text{sort}_{\text{resultado}}$$

Por ejemplo, en un TDA de pila que almacena enteros:

- $\text{push} : \mathtt{Stack} \times \mathbb{Z} \to \mathtt{Stack}$
- $\text{pop} : \mathtt{Stack} \to \mathtt{Stack}$
- $\text{top} : \mathtt{Stack} \to \mathbb{Z}$
- $\text{isEmpty} : \mathtt{Stack} \to \mathtt{Bool}$

La **signatura** de un TDA es el conjunto de todos sus sorts y operaciones. Por ejemplo, la signatura de una pila incluye los sorts $\{\mathtt{Stack}, \mathbb{Z}, \mathtt{Bool}\}$ y las operaciones listadas arriba.

### Operaciones especiales

Algunas operaciones tienen nombres convencionales:

- **Constructores**: crean instancias del sort. Ejemplo: $\text{empty} : \to \mathtt{Stack}$ crea la pila vacía.
- **Observadores**: consultan propiedades sin modificar. Ejemplo: $\text{isEmpty} : \mathtt{Stack} \to \mathtt{Bool}$.
- **Transformadores**: modifican la estructura. Ejemplo: $\text{push}$.

Notá que $\text{empty}$ no recibe argumentos (dominio vacío). Se nota como $\to \mathtt{Stack}$.

## Axiomas

Un **axioma** es una ecuación o propiedad que toda implementación del TDA debe satisfacer. Los axiomas capturan el significado semántico de las operaciones.

### Estructura de un axioma

Un axioma típicamente tiene la forma:

$$f(\text{expresión}_1) = g(\text{expresión}_2)$$

donde $f$ y $g$ son operaciones, y las expresiones pueden incluir variables de sort y valores constantes.

### Ejemplo: Pila de enteros

Considerá el TDA `Stack` con signatura:

$$\begin{align}
\text{empty} &: \to \mathtt{Stack}\\
\text{push} &: \mathtt{Stack} \times \mathbb{Z} \to \mathtt{Stack}\\
\text{pop} &: \mathtt{Stack} \to \mathtt{Stack}\\
\text{top} &: \mathtt{Stack} \to \mathbb{Z}\\
\text{isEmpty} &: \mathtt{Stack} \to \mathtt{Bool}
\end{align}$$

Los axiomas serían:

**Axioma 1 (pop de vacía):** La pila vacía no puede perder más elementos.
$$\text{pop}(\text{empty}) = \text{empty}$$

**Axioma 2 (top de vacía):** Consultar el tope de una pila vacía no está definido en esta especificación (o se define como undefined). Aquí lo omitimos porque es un caso de error.

**Axioma 3 (pop de push):** Sacar un elemento que acababas de agregar devuelve la pila anterior.
$$\text{pop}(\text{push}(s, x)) = s$$

donde $s \in \mathtt{Stack}$ y $x \in \mathbb{Z}$.

**Axioma 4 (top de push):** El tope de una pila donde acababas de agregar $x$ es justamente $x$.
$$\text{top}(\text{push}(s, x)) = x$$

**Axioma 5 (isEmpty vacía):** Una pila vacía está vacía.
$$\text{isEmpty}(\text{empty}) = \text{true}$$

**Axioma 6 (isEmpty después de push):** Una pila donde agregaste algo no está vacía.
$$\text{isEmpty}(\text{push}(s, x)) = \text{false}$$

Estos seis axiomas **definen completamente** el comportamiento de una pila de enteros, sin especificar si vas a usar un arreglo, una lista enlazada, o cualquier otra estructura.

## Omega: la operación indefinida

En algunas especificaciones, una operación puede no estar definida para ciertos argumentos. Por ejemplo, no tiene sentido hacer `top` de una pila vacía.

El símbolo $\omega$ (omega) representa el estado **indefinido** o **error**. Formalmente:

$$\text{top}(\text{empty}) = \omega$$

Esto indica que la operación no devuelve un valor válido de su sort destino. Las especificaciones que admiten $\omega$ se llaman **especificaciones parciales**, en contraste con las **especificaciones totales** donde toda operación devuelve siempre un valor válido.

En nuestro ejemplo de pila, consideramos que `top` es una operación parcial. Podrías agregar un axioma:

**Axioma 7 (top de vacía):**
$$\text{top}(\text{empty}) = \omega$$

## Demostraciones algebraicas

Una **demostración algebraica** es un argumento que deriva nuevas ecuaciones a partir de los axiomas mediante sustitución y simplificación.

### Ejemplo de demostración

Supongamos que querés demostrar que si agregás dos elementos a la pila vacía y luego sacas uno, el tope es el primer elemento que agregaste.

**Proposición:** $\text{top}(\text{pop}(\text{push}(\text{push}(\text{empty}, 5), 7))) = 5$

**Demostración:**

$$\begin{align}
\text{top}(\text{pop}(\text{push}(\text{push}(\text{empty}, 5), 7)))
&= \text{top}(\text{pop}(\text{push}(\text{push}(\text{empty}, 5), 7))) \tag{inicio}\\
&= \text{top}(\text{push}(\text{empty}, 5)) \tag{aplicar Axioma 3}\\
&= 5 \tag{aplicar Axioma 4}
\end{align}$$

En cada paso, reemplazamos una expresión por otra equivalente usando uno de nuestros axiomas. La demostración muestra que el comportamiento es consistente con nuestra especificación.

### Estructura general de una demostración

1. **Enunciado.** Escribe claramente la proposición que querés demostrar.
2. **Derivación.** Reemplaza subexpresiones usando axiomas hasta llegar al resultado esperado.
3. **Justificación.** Indica qué axioma (o axiomas) permitieron cada reemplazo.

### Inducción estructural

Para proposiciones sobre construcciones recursivas, usamos **inducción estructural**. La idea es demostrar que si una propiedad vale para elementos simples (casos base), y si vale para construcciones más grandes suponiendo que vale para sus componentes (caso inductivo), entonces vale para todos los elementos del sort.

**Ejemplo:** Demostrá que para cualquier pila $s$, aplicar `push` seguido de `pop` devuelve la pila original.

**Proposición:** $\text{pop}(\text{push}(s, x)) = s$ para todo $s \in \mathtt{Stack}$ y $x \in \mathbb{Z}$.

**Demostración por inducción:**

**Caso base:** $s = \text{empty}$

$$\text{pop}(\text{push}(\text{empty}, x)) = \text{empty} \quad \text{(por Axioma 3)}$$

**Caso inductivo:** Asumimos que la proposición vale para alguna pila $s$ (hipótesis inductiva):
$$\text{pop}(\text{push}(s, x)) = s$$

Queremos demostrar que también vale para $\text{push}(s, y)$ para cualquier $y \in \mathbb{Z}$:

$$\text{pop}(\text{push}(\text{push}(s, y), x)) = \text{push}(s, y) \quad \text{(por Axioma 3)}$$

El axioma 3 aplica directamente sin necesidad de usar la hipótesis inductiva en este caso. La proposición es un axioma, así que es válida para cualquier pila. ∎

## Axiomas vs. propiedades derivadas

Es importante notar que los axiomas son el conjunto **mínimo** de ecuaciones que necesitás para capturar el significado de un TDA. Cualquier otra ecuación válida se puede derivar de los axiomas mediante demostraciones.

Por ejemplo, de los axiomas de la pila podés derivar:

$$\text{pop}(\text{empty}) = \text{empty} \quad \text{(Axioma 1)}$$

Pero también podés derivar:

$$\text{isEmpty}(\text{pop}(\text{empty})) = \text{true}$$

Esto no es un axioma; es una **propiedad derivada**, porque se sigue de aplicar sucesivamente los axiomas 1 y 5.

## Resumen

Un tipo de dato abstracto es una especificación formal que captura el comportamiento esencial de una estructura de datos sin revelar su implementación:

- **Sorts:** colecciones abstractas de valores (tipos base del TDA).
- **Operaciones:** funciones que transforman u observan valores de sorts.
- **Signatura:** el conjunto de sorts y operaciones de un TDA.
- **Axiomas:** ecuaciones que cada implementación debe satisfacer.
- **Omega:** símbolo para operaciones indefinidas o errores.
- **Demostraciones algebraicas:** argumentos que derivan nuevas ecuaciones de los axiomas.

La especificación algebraica garantiza que distintas implementaciones compartan el mismo contrato semántico, lo que permite razonar sobre correctness, reemplazar implementaciones y construir código robusto.

## Ejercicios

```{exercise}
:label: ex-tda-pila-axiomas

Escribí una especificación algebraica completa para un TDA de **cola** (queue). Incluí:
- Sorts y operaciones (enqueue, dequeue, front, isEmpty, empty).
- Axiomas que capturen el comportamiento FIFO (first-in, first-out).
- Considera qué pasa cuando se intenta dequeue de una cola vacía.
```

```{exercise}
:label: ex-tda-axiomas-conjunto

Escribí los axiomas para un TDA de **conjunto** (set) que almacena enteros. Operaciones: empty, add, remove, member, isEmpty. ¿Qué axiomas garantizan que un elemento no puede repetirse?
```

```{exercise}
:label: ex-demostracion-pila

Demostrá por inducción estructural que para cualquier pila $s$ y elementos $x, y \in \mathbb{Z}$:
$$\text{top}(\text{push}(\text{push}(s, x), y)) = y$$
Escribí la demostración paso a paso, justificando cada reemplazo con el axioma correspondiente.
```

```{exercise}
:label: ex-equivalencia-especificaciones

Se te dan dos especificaciones algebraicas de una pila:

**Especificación A** (axiomas 1–6, como en el texto).

**Especificación B** agrega un axioma extra:
$$\text{pop}(\text{pop}(\text{push}(\text{push}(s, x), y))) = s$$

¿Son equivalentes? ¿Se puede derivar el axioma de B a partir de los axiomas de A? Justificá.
```

## Próximo paso

Con las bases algebraicas en lugar, estás listo para especificar TDAs más complejos. En el próximo capítulo exploraremos cómo traducir especificaciones algebraicas a código Java, manteniendo el rigor de la especificación mientras implementamos operaciones concretas.
