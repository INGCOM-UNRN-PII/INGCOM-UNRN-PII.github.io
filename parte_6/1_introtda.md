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

En lenguajes como Java, vemos esto concretamente: la interfaz `Stack<E>` especifica *qué* debe hacer una pila (push, pop, peek, empty), pero las implementaciones pueden variar. Podría usar un `LinkedList` internamente, un `Vector` (implementación heredada), o tu propia estructura. La especificación es el contrato que todas deben cumplir.

### Por qué importa

La especificación algebraica te obliga a:

1. **Pensar claramente** qué promete tu estructura de datos.
2. **Escribir axiomas** que capturen esas promesas de modo preciso.
3. **Validar** que la implementación cumple los axiomas.
4. **Cambiar implementaciones** sin cambiar el contrato con tus usuarios.

Además, una especificación algebraica rigurosa:

- **Elimina ambigüedad.** No quedan dudas sobre qué comportamiento se espera.
- **Facilita pruebas.** Los axiomas se convierten en tests que cualquier implementación debe pasar.
- **Permite reasoning formal.** Podés demostrar propiedades sobre el TDA sin analizar el código concreto.
- **Favorece la reutilización.** Código que confía en la especificación funciona con cualquier implementación correcta.

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

Los sorts actúan como "universos de discurso": definen el dominio sobre el cual razonamos. Cuando especificamos un TDA, no nos importa cómo se codifican los valores en la máquina (bits, bytes, estructuras). Solo importa que hay un conjunto abstracto de valores y que podemos distinguirlos mediante las operaciones.

### Operaciones

Una **operación** (o función) recibe valores de ciertos sorts y retorna un valor de otro sort. La notación estándar es:

$$\text{nombre} : \text{sort}_1 \times \text{sort}_2 \times \cdots \times \text{sort}_n \to \text{sort}_{\text{resultado}}$$

Por ejemplo, en un TDA de pila que almacena enteros:

- $\text{push} : \mathtt{Stack} \times \mathbb{Z} \to \mathtt{Stack}$
- $\text{pop} : \mathtt{Stack} \to \mathtt{Stack}$
- $\text{top} : \mathtt{Stack} \to \mathbb{Z}$
- $\text{isEmpty} : \mathtt{Stack} \to \mathtt{Bool}$

La **signatura** de un TDA es el conjunto de todos sus sorts y operaciones. Por ejemplo, la signatura de una pila incluye los sorts $\{\mathtt{Stack}, \mathbb{Z}, \mathtt{Bool}\}$ y las operaciones listadas arriba.

Una signatura completa debe listar todos los sorts que intervienen. Si el TDA de pila usa el sort $\mathbb{Z}$ (enteros), la signatura debería incluir también la signatura de $\mathbb{Z}$, que incluye operaciones como $+$, $-$, $=$, etc. En la práctica, damos por supuesto que los tipos básicos (números, booleanos) están ya definidos.

### Operaciones especiales: Generadores, Modificadores y Observadores

Aunque "constructores", "observadores" y "transformadores" son nombres útiles, la teoría de tipos abstractos define una taxonomía más precisa basada en el **rol semántico** de las operaciones. Esta clasificación es fundamental para diseñar axiomas correctos y para verificar que una especificación es **completa** (cubre todos los casos).

#### Generadores (Constructores)

Un **generador** es una operación que crea valores del TDA sin depender de valores previos del mismo TDA. Formalmente, un generador tiene tipo:

$$\text{gen} : \text{sort}_1 \times \text{sort}_2 \times \cdots \times \text{sort}_n \to \mathtt{S}$$

donde **ninguno** de los sorts de entrada es $\mathtt{S}$ (el sort principal del TDA).

**Ejemplos:**

- $\text{empty} : \to \mathtt{Stack}$ — genera una pila vacía sin depender de pilas previas.
- $\text{zero} : \to \mathtt{Nat}$ — genera el número natural 0.
- $\text{true}, \text{false} : \to \mathtt{Bool}$ — generan los valores booleanos.

Los generadores son los **únicos puntos de entrada** al TDA. Todo valor debe ser construible mediante una secuencia de generadores y modificadores.

**Propiedad clave:** Un conjunto de generadores es **completo** si todo valor del sort puede ser generado por ellos. Por ejemplo, $\{\text{empty}, \text{push}\}$ generan cualquier pila: comenzás con $\text{empty}$ y aplicás $\text{push}$ repetidas veces.

#### Modificadores (Transformadores)

Un **modificador** es una operación que recibe un valor del TDA y retorna un nuevo valor del mismo TDA (posiblemente distinto). Formalmente:

$$\text{mod} : \mathtt{S} \times \text{sort}_1 \times \cdots \times \text{sort}_n \to \mathtt{S}$$

donde $\mathtt{S}$ aparece **al menos una vez** en el dominio y la operación retorna $\mathtt{S}$.

**Ejemplos:**

- $\text{push} : \mathtt{Stack} \times \mathbb{Z} \to \mathtt{Stack}$ — recibe una pila y un elemento, retorna una nueva pila.
- $\text{succ} : \mathtt{Nat} \to \mathtt{Nat}$ — recibe un natural y retorna su sucesor.
- $\text{not} : \mathtt{Bool} \to \mathtt{Bool}$ — recibe un booleano y retorna su negación.

Los modificadores permiten construir valores más complejos a partir de valores más simples. Son **esenciales para la recursión**: los axiomas que involucran modificadores típicamente relacionan una expresión con un modificador aplicado a una expresión más pequeña.

**Propiedad importante:** Un modificador debe estar **bien fundado** para evitar infinitos. En los naturales, $\text{succ}$ es bien fundado porque siempre progresa hacia valores "más grandes" (en cierto sentido abstracto). En las pilas, $\text{push}$ es bien fundado porque construye pilas más profundas.

#### Observadores (Selectores)

Un **observador** es una operación que consulta información sobre un valor del TDA **sin modificarlo**. Formalmente:

$$\text{obs} : \mathtt{S} \times \text{sort}_1 \times \cdots \times \text{sort}_n \to \text{sort}_{\text{resultado}}$$

donde $\mathtt{S}$ aparece en el dominio pero **no en la imagen** (el resultado es de un sort diferente, frecuentemente $\mathtt{Bool}$ o un sort base).

**Ejemplos:**

- $\text{top} : \mathtt{Stack} \to \mathbb{Z}$ — observa el elemento en el tope sin cambiar la pila.
- $\text{isEmpty} : \mathtt{Stack} \to \mathtt{Bool}$ — observa si la pila está vacía.
- $\text{eq} : \mathtt{Nat} \times \mathtt{Nat} \to \mathtt{Bool}$ — observa si dos naturales son iguales.

Los observadores **no cambian el estado**: aplicar un observador múltiples veces con los mismos argumentos siempre retorna el mismo resultado.

**Propiedad clave:** Todo observador debe estar definido completamente mediante axiomas que relacionen el observador con los generadores y modificadores. Por ejemplo, $\text{isEmpty}$ se define diciendo qué retorna cuando se aplica a $\text{empty}$ y cuando se aplica a $\text{push}(s, x)$.

#### Relación entre categorías

La interacción entre estas tres categorías sigue un patrón predecible:

1. **Generadores crean valores base.** Comenzás aquí.
2. **Modificadores construyen valores complejos.** Aplicás modificadores a generadores y a sus resultados.
3. **Observadores inspeccionan valores sin cambiarlos.** Usás observadores para extraer información de cualquier valor.

En los axiomas:

- Los axiomas de **observadores sobre generadores** definen el caso base (por ejemplo, $\text{isEmpty}(\text{empty}) = \text{true}$).
- Los axiomas de **observadores sobre modificadores** definen el caso recursivo (por ejemplo, $\text{isEmpty}(\text{push}(s, x)) = \text{false}$).

#### Ejemplo completo: Pila de enteros

Clasificando las operaciones de pila por categoría:

**Generadores:**
- $\text{empty} : \to \mathtt{Stack}$

**Modificadores:**
- $\text{push} : \mathtt{Stack} \times \mathbb{Z} \to \mathtt{Stack}$
- $\text{pop} : \mathtt{Stack} \to \mathtt{Stack}$

**Observadores:**
- $\text{top} : \mathtt{Stack} \to \mathbb{Z}$
- $\text{isEmpty} : \mathtt{Stack} \to \mathtt{Bool}$

Con esta clasificación, los axiomas se escriben sistemáticamente:

1. Cada observador tiene un axioma para el generador.
2. Cada observador tiene un axioma para cada modificador.

Para $\text{isEmpty}$:
- Axioma 1: $\text{isEmpty}(\text{empty}) = \text{true}$ (observador sobre generador)
- Axioma 2: $\text{isEmpty}(\text{push}(s, x)) = \text{false}$ (observador sobre modificador push)
- Axioma 3: $\text{isEmpty}(\text{pop}(s)) = \ldots$ (observador sobre modificador pop; se define recursivamente)

Esta estructura sistemática garantiza que los axiomas cubren **todos los casos posibles** de construcción de valores.

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

## TDAs Básicos: Element, Boolean, Natural, Integer, Decimal

Antes de especificar TDAs complejos como pilas o colas, es fundamental formalizar los tipos de datos elementales que usaremos como bloques de construcción. Estos TDAs básicos son los "ladrillos" sobre los cuales se construyen todas las estructuras de datos más sofisticadas.

### TDA Element

El TDA `Element` representa un valor genérico sin estructura interna. Es el TDA más abstracto: solo sabemos que existen elementos distinguibles.

**Signatura:**

$$\begin{align}
\text{eq} &: \mathtt{Element} \times \mathtt{Element} \to \mathtt{Bool}
\end{align}$$

**Axiomas:**

**Axioma 1 (reflexividad):** Un elemento es igual a sí mismo.
$$\text{eq}(x, x) = \text{true} \quad \forall x \in \mathtt{Element}$$

**Axioma 2 (simetría):** Si $x = y$, entonces $y = x$.
$$\text{eq}(x, y) = \text{eq}(y, x) \quad \forall x, y \in \mathtt{Element}$$

**Axioma 3 (transitividad):** Si $x = y$ e $y = z$, entonces $x = z$.
$$\text{eq}(x, y) = \text{true} \land \text{eq}(y, z) = \text{true} \Rightarrow \text{eq}(x, z) = \text{true}$$

El TDA `Element` es tan simple que prácticamente no tiene operaciones. Se usa principalmente como parámetro genérico: "una pila de elementos", sin especificar qué son esos elementos.

### TDA Boolean

El TDA `Boolean` representa los valores de verdad.

**Signatura:**

$$\begin{align}
\text{true} &: \to \mathtt{Bool}\\
\text{false} &: \to \mathtt{Bool}\\
\text{and} &: \mathtt{Bool} \times \mathtt{Bool} \to \mathtt{Bool}\\
\text{or} &: \mathtt{Bool} \times \mathtt{Bool} \to \mathtt{Bool}\\
\text{not} &: \mathtt{Bool} \to \mathtt{Bool}\\
\text{eq} &: \mathtt{Bool} \times \mathtt{Bool} \to \mathtt{Bool}
\end{align}$$

**Axiomas:**

**Axioma 1 (true es verdadero):**
$$\text{eq}(\text{true}, \text{true}) = \text{true}$$

**Axioma 2 (false es falso):**
$$\text{eq}(\text{false}, \text{false}) = \text{true}$$

**Axioma 3 (distinción):**
$$\text{eq}(\text{true}, \text{false}) = \text{false}$$
$$\text{eq}(\text{false}, \text{true}) = \text{false}$$

**Axioma 4 (not invierte):**
$$\text{not}(\text{true}) = \text{false}$$
$$\text{not}(\text{false}) = \text{true}$$

**Axioma 5 (and es conjunción):**
$$\text{and}(\text{true}, \text{true}) = \text{true}$$
$$\text{and}(\text{true}, \text{false}) = \text{false}$$
$$\text{and}(\text{false}, \text{true}) = \text{false}$$
$$\text{and}(\text{false}, \text{false}) = \text{false}$$

**Axioma 6 (or es disyunción):**
$$\text{or}(\text{true}, \text{true}) = \text{true}$$
$$\text{or}(\text{true}, \text{false}) = \text{true}$$
$$\text{or}(\text{false}, \text{true}) = \text{true}$$
$$\text{or}(\text{false}, \text{false}) = \text{false}$$

Estos axiomas capturan exactamente la semántica de la lógica proposicional estándar.

### TDA Natural

El TDA `Natural` representa los números naturales (0, 1, 2, 3, ...).

**Signatura:**

$$\begin{align}
\text{zero} &: \to \mathtt{Nat}\\
\text{succ} &: \mathtt{Nat} \to \mathtt{Nat}\\
\text{plus} &: \mathtt{Nat} \times \mathtt{Nat} \to \mathtt{Nat}\\
\text{mult} &: \mathtt{Nat} \times \mathtt{Nat} \to \mathtt{Nat}\\
\text{eq} &: \mathtt{Nat} \times \mathtt{Nat} \to \mathtt{Bool}\\
\text{leq} &: \mathtt{Nat} \times \mathtt{Nat} \to \mathtt{Bool}
\end{align}$$

Aquí $\text{succ}(n)$ es el sucesor de $n$, es decir, $n+1$. La operación $\text{succ}$ es el constructor fundamental; cualquier número se obtiene aplicando $\text{succ}$ repetidamente a $\text{zero}$.

**Axiomas:**

**Axioma 1 (succ es inyectivo):** Números distintos tienen sucesores distintos.
$$\text{succ}(m) = \text{succ}(n) \Rightarrow m = n \quad \forall m, n \in \mathtt{Nat}$$

**Axioma 2 (zero no es sucesor):** No existe un natural cuyo sucesor sea zero.
$$\text{succ}(n) = \text{zero} \Rightarrow \text{false} \quad \forall n \in \mathtt{Nat}$$

**Axioma 3 (suma con zero):**
$$\text{plus}(\text{zero}, n) = n \quad \forall n \in \mathtt{Nat}$$

**Axioma 4 (suma recursiva):**
$$\text{plus}(\text{succ}(m), n) = \text{succ}(\text{plus}(m, n)) \quad \forall m, n \in \mathtt{Nat}$$

Estos dos axiomas definen la suma recursivamente: $0 + n = n$ y $(m+1) + n = (m+n) + 1$.

**Axioma 5 (producto con zero):**
$$\text{mult}(\text{zero}, n) = \text{zero} \quad \forall n \in \mathtt{Nat}$$

**Axioma 6 (producto recursivo):**
$$\text{mult}(\text{succ}(m), n) = \text{plus}(\text{mult}(m, n), n) \quad \forall m, n \in \mathtt{Nat}$$

**Axioma 7 (igualdad en zero):**
$$\text{eq}(\text{zero}, \text{zero}) = \text{true}$$

**Axioma 8 (igualdad de sucesores):**
$$\text{eq}(\text{succ}(m), \text{succ}(n)) = \text{eq}(m, n) \quad \forall m, n \in \mathtt{Nat}$$

**Axioma 9 (zero vs sucesor):**
$$\text{eq}(\text{zero}, \text{succ}(n)) = \text{false} \quad \forall n \in \mathtt{Nat}$$
$$\text{eq}(\text{succ}(n), \text{zero}) = \text{false} \quad \forall n \in \mathtt{Nat}$$

**Axioma 10 (leq define orden):**
$$\text{leq}(\text{zero}, n) = \text{true} \quad \forall n \in \mathtt{Nat}$$
$$\text{leq}(\text{succ}(m), \text{zero}) = \text{false} \quad \forall m \in \mathtt{Nat}$$
$$\text{leq}(\text{succ}(m), \text{succ}(n)) = \text{leq}(m, n) \quad \forall m, n \in \mathtt{Nat}$$

Estos axiomas capturan la estructura recursiva de los números naturales según la axiomatización de Peano.

### TDA Integer

El TDA `Integer` extiende los naturales para incluir números negativos.

**Signatura:**

$$\begin{align}
\text{zero} &: \to \mathtt{Int}\\
\text{succ} &: \mathtt{Int} \to \mathtt{Int}\\
\text{pred} &: \mathtt{Int} \to \mathtt{Int}\\
\text{plus} &: \mathtt{Int} \times \mathtt{Int} \to \mathtt{Int}\\
\text{minus} &: \mathtt{Int} \times \mathtt{Int} \to \mathtt{Int}\\
\text{mult} &: \mathtt{Int} \times \mathtt{Int} \to \mathtt{Int}\\
\text{eq} &: \mathtt{Int} \times \mathtt{Int} \to \mathtt{Bool}\\
\text{leq} &: \mathtt{Int} \times \mathtt{Int} \to \mathtt{Bool}
\end{align}$$

Aquí $\text{pred}(n)$ es el predecesor de $n$, es decir, $n-1$. Números negativos se construyen aplicando $\text{pred}$ a $\text{zero}$.

**Axiomas clave:**

**Axioma 1 (succ-pred inversa):** $\text{succ}$ y $\text{pred}$ se invierten mutuamente.
$$\text{succ}(\text{pred}(n)) = n \quad \forall n \in \mathtt{Int}$$
$$\text{pred}(\text{succ}(n)) = n \quad \forall n \in \mathtt{Int}$$

**Axioma 2 (suma con zero):**
$$\text{plus}(\text{zero}, n) = n \quad \forall n \in \mathtt{Int}$$

**Axioma 3 (suma recursiva positiva):**
$$\text{plus}(\text{succ}(m), n) = \text{succ}(\text{plus}(m, n)) \quad \forall m, n \in \mathtt{Int}$$

**Axioma 4 (suma recursiva negativa):**
$$\text{plus}(\text{pred}(m), n) = \text{pred}(\text{plus}(m, n)) \quad \forall m, n \in \mathtt{Int}$$

**Axioma 5 (menos es suma inversa):**
$$\text{minus}(m, n) = \text{plus}(m, \text{negate}(n))$$

donde $\text{negate}(n)$ es la negación de $n$ (aquí la omitimos por brevedad).

### TDA Decimal

El TDA `Decimal` representa números con parte fraccionaria.

**Signatura:**

$$\begin{align}
\text{fromInt} &: \mathtt{Int} \to \mathtt{Decimal}\\
\text{numerator} &: \mathtt{Decimal} \to \mathtt{Int}\\
\text{denominator} &: \mathtt{Decimal} \to \mathtt{Nat}\\
\text{plus} &: \mathtt{Decimal} \times \mathtt{Decimal} \to \mathtt{Decimal}\\
\text{mult} &: \mathtt{Decimal} \times \mathtt{Decimal} \to \mathtt{Decimal}\\
\text{eq} &: \mathtt{Decimal} \times \mathtt{Decimal} \to \mathtt{Bool}\\
\text{leq} &: \mathtt{Decimal} \times \mathtt{Decimal} \to \mathtt{Bool}
\end{align}$$

Aquí representamos decimales como fracciones (par numerador-denominador). $\text{fromInt}(i)$ convierte un entero en su equivalente decimal.

**Axiomas clave:**

**Axioma 1 (conversión de enteros):**
$$\text{numerator}(\text{fromInt}(i)) = i$$
$$\text{denominator}(\text{fromInt}(i)) = 1$$

**Axioma 2 (suma de decimales):** Para $d_1 = \frac{n_1}{d_1}$ y $d_2 = \frac{n_2}{d_2}$:
$$\text{numerator}(\text{plus}(d_1, d_2)) = n_1 \cdot d_2 + n_2 \cdot d_1$$
$$\text{denominator}(\text{plus}(d_1, d_2)) = d_1 \cdot d_2$$

(En la práctica, se simplificaría al mínimo común divisor, pero omitimos eso aquí para claridad.)

**Axioma 3 (multiplicación de decimales):**
$$\text{numerator}(\text{mult}(d_1, d_2)) = n_1 \cdot n_2$$
$$\text{denominator}(\text{mult}(d_1, d_2)) = d_1 \cdot d_2$$

**Axioma 4 (igualdad de decimales):** Dos decimales son iguales si sus fracciones son equivalentes.
$$\text{eq}(d_1, d_2) = \text{true} \iff \text{numerator}(d_1) \cdot \text{denominator}(d_2) = \text{numerator}(d_2) \cdot \text{denominator}(d_1)$$

---

Estos cinco TDAs básicos (Element, Boolean, Natural, Integer, Decimal) forman la base sobre la cual construimos TDAs más complejos. Cualquier estructura de datos que trabaje con números, booleanos o elementos genéricos confía en que estos TDAs cumplen sus axiomas.

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

## Integración con Contratos: Precondiciones, Postcondiciones e Invariantes

La especificación algebraica que desarrollamos hasta aquí es **denotacional** y **funcional**: describe *qué* hace cada operación mediante ecuaciones. Sin embargo, en la programación práctica (especialmente en lenguajes como Java), utilizamos **contratos** basados en estado: precondiciones, postcondiciones e invariantes (Lógica de Hoare, Diseño por Contrato).

La conexión entre el álgebra de tipos abstractos y los contratos es profunda y rigurosa. No son mundos separados; los axiomas algebraicos **fundamentan y garantizan** la correctness de los contratos.

### Invariantes de Representación y Restricción de Sorts

Un **invariante** es un predicado $I : s \to \mathtt{Bool}$ que especifica cuál es el conjunto válido de valores del sort. En la visión algebraica, el invariante **restringe el dominio semántico** del tipo.

**Formalmente:** Mientras que el sort $\mathtt{Stack}$ en teoría admite cualquier secuencia de elementos, en la práctica queremos que todas las pilas generadas cumplan ciertas propiedades. Por ejemplo:

$$I(\text{Stack}) : \forall s \in \mathtt{Stack}, \; \text{depth}(s) \geq 0$$

(El número de elementos es siempre no negativo.)

En el marco de **álgebras ordenadas por sorts**, el invariante se modela como un **subsort**. En lugar de trabajar con el sort general $\mathtt{Stack}$, trabajamos con sorts más específicos:

- $\mathtt{EmptyStack}$ — pilas que satisfacen $\text{isEmpty}(s) = \text{true}$
- $\mathtt{NeStack}$ (non-empty stack) — pilas que satisfacen $\text{isEmpty}(s) = \text{false}$

Entonces:
- $\text{empty} : \to \mathtt{EmptyStack}$ — genera pilas vacías
- $\text{push} : \mathtt{Stack} \times \mathbb{Z} \to \mathtt{NeStack}$ — transforma cualquier pila en una no vacía

Con esta tipificación estricta, el invariante se **eleva a la signatura**: no es una aserción que chequeés en runtime, sino una restricción de tipos que se valida en tiempo de especificación.

**Demostración inductiva de un invariante:**

Querés demostrar que el invariante $I(s) : \text{depth}(s) \geq 0$ vale para toda pila generada.

**Base:** $I(\text{empty})$ se cumple porque $\text{depth}(\text{empty}) = 0 \geq 0$. ✓

**Paso inductivo:** Asumís que $I(s)$ vale para alguna pila $s$ (hipótesis inductiva). Querés demostrar que $I(\text{push}(s, x))$ también vale.

$$\text{depth}(\text{push}(s, x)) = \text{depth}(s) + 1$$

Por hipótesis inductiva, $\text{depth}(s) \geq 0$, así que:
$$\text{depth}(\text{push}(s, x)) = \text{depth}(s) + 1 \geq 0 + 1 = 1 > 0$$

Por lo tanto, $I(\text{push}(s, x))$ se cumple. ✓

Por inducción estructural, el invariante vale para todas las pilas. ∎

### Precondiciones y Funciones Parciales

Una **precondición** es un predicado que debe ser verdadero *antes* de ejecutar una operación. Formalmente, una función parcial se define solo cuando su precondición es verdadera.

En la especificación algebraica estándar, todas las operaciones son **totales**: $\text{pop} : \mathtt{Stack} \to \mathtt{Stack}$ puede aplicarse a cualquier pila. Pero en la práctica, $\text{pop}$ solo tiene sentido si la pila es no vacía. La precondición es:

$$\text{pre}(\text{pop}(s)) : \text{isEmpty}(s) = \text{false}$$

Para modelar esto algebraicamente, usamos **subsorts** y **axiomas condicionales**:

En lugar de $\text{pop} : \mathtt{Stack} \to \mathtt{Stack}$, escribimos:
$$\text{pop} : \mathtt{NeStack} \to \mathtt{Stack}$$

Esto eleva la precondición a la signatura. Un término $\text{pop}(s)$ solo es válido si $s$ tiene sort $\mathtt{NeStack}$, es decir, si $\text{isEmpty}(s) = \text{false}$.

**Alternativamente**, usamos **axiomas condicionales** que protegen la ecuación:

$$\text{isEmpty}(s) = \text{false} \implies \text{pop}(s) = \text{...}$$

**Demostración de corrección de precondición:**

Querés demostrar que si respetás la precondición de $\text{pop}$, garantizás que el invariante de pila se mantiene.

**Proposición:** Si $\text{isEmpty}(s) = \text{false}$, entonces $\text{depth}(\text{pop}(s)) \geq 0$.

**Demostración:** 

Si $\text{isEmpty}(s) = \text{false}$, entonces existe un elemento en la pila. Esto significa que $s$ fue construida usando al menos una operación $\text{push}$ desde el $\text{empty}$ original.

Por lo tanto, $s = \text{push}(s', x)$ para algún $s'$ y $x$.

Aplicando el axioma 3 ($\text{pop}(\text{push}(s, x)) = s$):
$$\text{pop}(s) = \text{pop}(\text{push}(s', x)) = s'$$

Dado que $s'$ es una pila válida, el invariante de $s'$ se cumple: $\text{depth}(s') \geq 0$. ✓

### Postcondiciones y Ecuaciones de Equivalencia

Una **postcondición** especifica *qué estado resulta* después de ejecutar una operación. Formalmente, en lenguaje imperativo:

$$\{P\} \; \text{operación} \; \{Q\}$$

indica que si $P$ era verdadera antes, después de la operación, $Q$ será verdadera.

En la visión algebraica (sin estado mutable), las postcondiciones se **codifican como identidades** en el conjunto de axiomas $E$.

**Ejemplo:** La postcondición de $\text{push}(s, x)$ es que el elemento en el tope sea $x$. Algebraicamente, esto es el axioma:

$$\text{top}(\text{push}(s, x)) = x$$

Este axioma **garantiza** formalmente que la postcondición se cumple en cualquier implementación.

**Demostración: Postcondición de push**

Querés demostrar que después de hacer $\text{push}(s, x)$, el tope es $x$.

**Proposición:** $\text{top}(\text{push}(s, x)) = x$ para toda pila $s$ y elemento $x$.

**Demostración:** 

Este es exactamente el Axioma 4 de la pila. Por definición de axioma, se cumple para toda pila generada por los constructores y modificadores. ∎

**Ejemplo más complejo: Composición de postcondiciones**

Supongamos que querés demostrar que si hacés dos $\text{push}$ seguidos y luego un $\text{pop}$, el tope es el primer elemento que agregaste.

**Proposición:** 

Sea $s$ una pila. Después de $\text{push}(s, x)$, $\text{push}$ nuevamente con $y$, y luego $\text{pop}$, el tope debería ser $x$.

$$\text{top}(\text{pop}(\text{push}(\text{push}(s, x), y))) = x$$

**Demostración:**

$$\begin{align}
\text{top}(\text{pop}(\text{push}(\text{push}(s, x), y)))
&= \text{top}(\text{push}(s, x)) && \text{(aplicar Axioma 3: pop de push)} \\
&= x && \text{(aplicar Axioma 4: top de push)}
\end{align}$$

Esta demostración prueba formalmente que la composición de postcondiciones individuales garantiza el resultado esperado. ∎

### Relación entre Axiomas, Precondiciones y Postcondiciones

La estructura es jerárquica:

1. **Axiomas algebraicos** ($\Sigma, E$) son los cimientos. Define completamente el comportamiento.
2. **Invariantes** restringen el conjunto válido de valores mediante subsorts.
3. **Precondiciones** especifican cuándo una operación es aplicable; se implementan como requisitos de sort.
4. **Postcondiciones** describen el resultado; son ecuaciones que se derivan de los axiomas.

**Tabla de correspondencia:**

| Concepto | Especificación Algebraica | Contrato Imperativo |
|----------|--------------------------|-------------------|
| ¿Qué valores son válidos? | Axiomas + subsorts | Invariante |
| ¿Cuándo puedo usar esta op? | Tipo del dominio (subsort) | Precondición |
| ¿Qué pasa después? | Ecuación axiomática | Postcondición |
| ¿Se mantiene siempre? | Demostración inductiva | Verificación del invariante |

### Ventajas de esta integración

La especificación algebraica **fundamenta** los contratos:

- **Claridad total:** Los axiomas explicitan exactamente qué hace cada operación. No quedan ambigüedades sobre qué promete la postcondición.
- **Demostrabilidad:** Los contratos no son afirmaciones sueltas; se derivan formalmente de los axiomas.
- **Composicionalidad:** Si dos operaciones satisfacen sus axiomas individuales, la composición también satisface sus axiomas (ver ejemplo de dos push + pop).
- **Verificabilidad:** Los axiomas se convierten en casos de test. Si tu implementación satisface los axiomas, satisface los contratos.

## La Arquitectura de las Cuatro Fases

Para comprender cómo los conceptos de tipos de datos abstractos evolucionan desde su pura abstracción matemática hasta su utilidad práctica en ingeniería de software, organizamos el framework como **cuatro capas arquitectónicas sucesivas**. Cada capa construye sobre la anterior, añadiendo rigor, expresividad y aplicabilidad.

### Fase 1: Tipado Estático (La Signatura $\Sigma$)

**Función:** Actúa como el compilador formal a nivel de dominio.

**Perspectiva:** Puramente **sintáctica**. La signatura desconoce la semántica y se enfoca únicamente en que las operaciones respeten los dominios abstractos definidos y su aridad (número y tipos de argumentos).

**Componentes:**

Una signatura $\Sigma$ especifica:
- **Sorts:** $S = \{\mathtt{Stack}, \mathbb{Z}, \mathtt{Bool}, \ldots\}$
- **Operaciones tipadas:** Cada operación $op : s_1 \times \cdots \times s_n \to s$ establece un contrato sintáctico.

**Ejemplo con pila:**

$$\Sigma_{\text{Stack}} = \begin{cases}
\text{Sorts:} & \mathtt{Stack}, \mathbb{Z}, \mathtt{Bool} \\
\text{Operaciones:} & \\
\quad \text{empty} : \to \mathtt{Stack} \\
\quad \text{push} : \mathtt{Stack} \times \mathbb{Z} \to \mathtt{Stack} \\
\quad \text{pop} : \mathtt{Stack} \to \mathtt{Stack} \\
\quad \text{top} : \mathtt{Stack} \to \mathbb{Z} \\
\quad \text{isEmpty} : \mathtt{Stack} \to \mathtt{Bool}
\end{cases}$$

**Aporte fundamental:** 

La signatura **prohibe operaciones divergentes**. Por ejemplo:
- $\text{isEmpty}(\text{true})$ es un término **inválido** (type error): el parámetro debe ser $\mathtt{Stack}$, no $\mathtt{Bool}$.
- $\text{push}(5, \text{empty})$ es inválido: el primer argumento debe ser $\mathtt{Stack}$, no $\mathbb{Z}$.

Esta validación sintáctica es crucial: restringe el conjunto de términos potencialmente evaluables a aquellos que respetan la estructura de tipos. Los términos bien tipados forman el vocabulario $T_\Sigma$, que es el universo sobre el cual operan las fases subsecuentes.

**Demostración: Validez de términos**

Sea el término $t = \text{top}(\text{push}(\text{empty}, 5))$. Demostrá que es un término bien tipado en $\Sigma$.

1. $\text{empty}$ tiene tipo $\mathtt{Stack}$. ✓ (constructor)
2. $5$ tiene tipo $\mathbb{Z}$. ✓ (literal)
3. $\text{push}(\text{empty}, 5)$ tiene tipo $\mathtt{Stack}$ porque $\text{push} : \mathtt{Stack} \times \mathbb{Z} \to \mathtt{Stack}$ y los argumentos respetan los tipos. ✓
4. $\text{top}(\text{push}(\text{empty}, 5))$ tiene tipo $\mathbb{Z}$ porque $\text{top} : \mathtt{Stack} \to \mathbb{Z}$ y su argumento es $\mathtt{Stack}$. ✓

Por lo tanto, $t \in T_\Sigma$ es un término válido de sort $\mathbb{Z}$. ∎

### Fase 2: Reescritura Semántica (La Axiomatización $E$)

**Función:** Actúa como el motor de evaluación ecuacional.

**Perspectiva:** **Denotacional**. Mientras que $\Sigma$ solo verifica sintaxis, $E$ estipula *qué combinaciones sintácticas representan el mismo objeto abstracto* mediante ecuaciones de equivalencia.

**Componentes:**

El conjunto de axiomas $E = \{e_1, e_2, \ldots, e_n\}$ define relaciones de igualdad entre términos. Cada axioma tiene la forma:

$$\text{expresión}_1 = \text{expresión}_2$$

donde ambos lados son términos bien tipados del mismo sort.

**Ejemplo con pila:**

$$E_{\text{Stack}} = \begin{cases}
\text{Axioma 1:} & \text{pop}(\text{empty}) = \text{empty} \\
\text{Axioma 3:} & \text{pop}(\text{push}(s, x)) = s \\
\text{Axioma 4:} & \text{top}(\text{push}(s, x)) = x \\
\text{Axioma 5:} & \text{isEmpty}(\text{empty}) = \text{true} \\
\text{Axioma 6:} & \text{isEmpty}(\text{push}(s, x)) = \text{false}
\end{cases}$$

**Aporte fundamental:**

A través de axiomas, transformamos el vocabulario tipado $T_\Sigma$ en un **álgebra cociente**. Dos términos que se reducen al mismo resultado son **semánticamente equivalentes**. Por ejemplo:

$$\text{pop}(\text{push}(\text{push}(\text{empty}, 3), 5)) \equiv \text{push}(\text{empty}, 3)$$

porque ambos lados denotan "una pila con un solo elemento: 3 en el tope".

Sin los axiomas, $T_\Sigma$ sería un conjunto de términos estáticos; con axiomas, obtenemos un **sistema algebraico dinámico** donde la reescritura ecuacional evalúa términos a sus formas canónicas.

**Demostración: Equivalencia por axiomas**

Demostrá que $\text{top}(\text{pop}(\text{push}(\text{push}(\text{empty}, 3), 5))) = 3$ utilizando solo axiomas.

$$\begin{align}
\text{top}(\text{pop}(\text{push}(\text{push}(\text{empty}, 3), 5)))
&= \text{top}(\text{push}(\text{empty}, 3)) && \text{(Axioma 3: pop de push)} \\
&= 3 && \text{(Axioma 4: top de push)}
\end{align}$$

Cada paso reemplaza una subexpresión por otra equivalente según axiomas. La reescritura termina en la forma canónica $3 \in \mathbb{Z}$. ∎

### Fase 3: Verificación Lógica (Demostración Estructural)

**Función:** Actúa como el sistema de prueba deductiva formal del TDA.

**Perspectiva:** **Analítica formal**. Explota la recursión inherente de la signatura (especialmente los generadores y modificadores) para validar **teoremas** sobre el universo potencialmente infinito de términos.

**Componentes:**

Las demostraciones estructurales utilizan:
- **Inducción estructural:** razonar sobre cómo se construyen todos los términos mediante generadores y modificadores.
- **Sustitución ecuacional:** aplicar axiomas de $E$ de forma mecánica.
- **Razonamiento compositivo:** si una propiedad vale para $t_1$ y $t_2$, vale para cualquier término que los combina.

**Ejemplo con pila:**

**Proposición:** Para toda pila $s$ y elementos $x, y \in \mathbb{Z}$:
$$\text{isEmpty}(\text{pop}(\text{push}(s, x))) = \text{isEmpty}(s)$$

Esta proposición afirma que hacer push y luego pop no cambia si la pila está vacía. Es una propiedad estructural infinita (vale para cualquiera de las infinitas pilas posibles).

**Demostración por inducción estructural:**

**Base:** $s = \text{empty}$

$$\begin{align}
\text{isEmpty}(\text{pop}(\text{push}(\text{empty}, x)))
&= \text{isEmpty}(\text{empty}) && \text{(Axioma 3)} \\
&= \text{isEmpty}(\text{empty}) && \text{(hipótesis)}
\end{align}$$

Ambos lados son iguales. ✓

**Paso inductivo:** Asumimos que la propiedad vale para $s$ (hipótesis inductiva):
$$\text{isEmpty}(\text{pop}(\text{push}(s, x))) = \text{isEmpty}(s)$$

Queremos demostrar que vale para $\text{push}(s, y)$:
$$\text{isEmpty}(\text{pop}(\text{push}(\text{push}(s, y), x))) = \text{isEmpty}(\text{push}(s, y))$$

Derivación:
$$\begin{align}
\text{isEmpty}(\text{pop}(\text{push}(\text{push}(s, y), x)))
&= \text{isEmpty}(\text{push}(s, y)) && \text{(Axioma 3)} \\
&= \text{isEmpty}(\text{push}(s, y)) && \text{(lo que queríamos demostrar)}
\end{align}$$

Por inducción, la proposición vale para toda pila generada. ∎

**Aporte fundamental:**

La inducción estructural escala el razonamiento de casos finitos a universos infinitos de términos. Garantiza que las propiedades derivadas son **universalmente válidas** antes de implementar nada. Es el marco riguroso de prueba de correctness.

### Fase 4: Restricción Pragmática (Contratos)

**Función:** Actúa como el puente operacional desde la especificación algebraica hacia la implementación imperativa concreta.

**Perspectiva:** **Operacional e ingenieril**. Reconoce que en software real, las álgebras totales (donde toda operación está definida para todo input) son ideales teóricos. En la práctica, necesitamos mecanismos para fallar gracefully, validar precondiciones y mantener invariantes.

**Componentes:**

Tres mecanismos vinculan especificación algebraica con ingeniería práctica:

**1. Subsorts para Invariantes:**

Un invariante restringe el dominio válido. Modelamos esto elevando subsorts:

$$\begin{align}
\mathtt{Stack} &\supseteq \mathtt{EmptyStack} \cup \mathtt{NeStack}\\
\text{where} \quad \mathtt{EmptyStack} &= \{s : \text{isEmpty}(s) = \text{true}\}\\
\mathtt{NeStack} &= \{s : \text{isEmpty}(s) = \text{false}\}
\end{align}$$

Entonces:
- $\text{empty} : \to \mathtt{EmptyStack}$
- $\text{push} : \mathtt{Stack} \times \mathbb{Z} \to \mathtt{NeStack}$
- $\text{pop} : \mathtt{NeStack} \to \mathtt{Stack}$ ← precondición integrada en el tipo

**2. Axiomas Condicionales para Precondiciones:**

Cuando una operación tiene una precondición, protegemos los axiomas:

$$\text{isEmpty}(s) = \text{false} \implies \text{top}(s) = e$$

Esta forma condicional captura "si $s$ es no vacía, entonces el top es bien definido".

**3. Postcondiciones como Identidades Axiomáticas:**

Toda postcondición se expresa como un axioma. Ejemplo:

- **Postcondición informal:** "Después de push(s, x), el elemento top es x"
- **Axioma formal:** $\text{top}(\text{push}(s, x)) = x$

**Ejemplo integrado: Validación de un push**

**Contrato en pseudocódigo Java:**

```java
/** 
 * Agrega un elemento a la pila.
 * 
 * Precondición: ninguna (siempre es válido)
 * Invariante: depth(s) >= 0
 * Postcondición: top(push(s, x)) == x && isEmpty(push(s, x)) == false
 */
void push(Stack s, int x)
```

**Correspondencia algebraica:**

| Contrato | Álgebra |
|----------|---------|
| Precondición: ninguna | Operación total: $\text{push} : \mathtt{Stack} \times \mathbb{Z} \to \mathtt{Stack}$ |
| Invariante: depth ≥ 0 | Axioma 1 + subsort NeStack garantiza que toda pila generada satisface el invariante |
| Postcondición: top = x | Axioma 4: $\text{top}(\text{push}(s, x)) = x$ |
| Postcondición: not empty | Axioma 6: $\text{isEmpty}(\text{push}(s, x)) = \text{false}$ |

**Aporte fundamental:**

Los contratos pragmáticos se **anclan en los axiomas**. No son afirmaciones sueltas; cada cláusula del contrato corresponde a una ecuación verificable algebraicamente. Esto permite:

- **Testabilidad automática:** Los axiomas generan casos de prueba.
- **Verificación estática:** El tipado de sorts captura precondiciones en tiempo de compilación.
- **Composicionalidad:** Si dos operaciones cumplen sus axiomas, su composición también.

### Integración: El Flujo Arquitectónico

Las cuatro fases no operan de forma aislada; forman un pipeline de validación:

```
Código de usuario 
    ↓
[Fase 1: Tipado] → ¿El término está bien tipado en Σ?
    ↓ (sí)
[Fase 2: Reescritura] → Evalúa el término usando E hasta forma canónica
    ↓
[Fase 3: Verificación] → ¿La propiedad derivada es válida por inducción?
    ↓ (sí)
[Fase 4: Contratos] → ¿Se respetan precondiciones, invariantes, postcondiciones?
    ↓ (sí)
Ejecución segura
```

**Ejemplo integral: Evaluación de top(push(empty, 5))**

**Fase 1 (Tipado):** 
- $\text{empty}$ tiene sort $\mathtt{Stack}$ ✓
- $5$ tiene sort $\mathbb{Z}$ ✓
- $\text{push}(\text{empty}, 5)$ tiene sort $\mathtt{Stack}$ ✓
- $\text{top}(\text{push}(\text{empty}, 5))$ tiene sort $\mathbb{Z}$ ✓

**Fase 2 (Reescritura):**
$$\text{top}(\text{push}(\text{empty}, 5)) \xrightarrow{\text{Axioma 4}} 5$$

**Fase 3 (Verificación):**
- Proposición: $\text{top}(\text{push}(s, x)) = x$ vale para toda $s$ y $x$.
- Demostración: Por inducción estructural (ya hecha).
- Conclusión: La evaluación es universalmente válida. ✓

**Fase 4 (Contratos):**
- Precondición de top: ninguna (operación total) ✓
- Invariante: $\text{depth}(\text{push}(\text{empty}, 5)) = 1 \geq 0$ ✓
- Postcondición: resultado = 5 ✓

**Resultado:** El término se evalúa con garantía formal de correctness en todas las fases.

## Resumen

Un tipo de dato abstracto es una especificación formal que captura el comportamiento esencial de una estructura de datos sin revelar su implementación:

- **Sorts:** colecciones abstractas de valores (tipos base del TDA).
- **Operaciones:** funciones que transforman u observan valores de sorts.
- **Signatura:** el conjunto de sorts y operaciones de un TDA.
- **Axiomas:** ecuaciones que cada implementación debe satisfacer.
- **Generadores, Modificadores, Observadores:** taxonomía de operaciones que estructura el diseño de axiomas.
- **Invariantes:** restricciones de representación modeladas como subsorts.
- **Precondiciones y Postcondiciones:** especificadas mediante subsorts y ecuaciones algebraicas.
- **Demostraciones:** pruebas formales que garantizan correctness.

La especificación algebraica garantiza que distintas implementaciones compartan el mismo contrato semántico, lo que permite razonar sobre correctness, reemplazar implementaciones y construir código robusto. Los axiomas algebraicos no son decorativos; son el fundamento técnico sobre el cual se construyen contratos verificables y composicionales.

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
