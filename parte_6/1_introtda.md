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
