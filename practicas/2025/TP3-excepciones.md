---
title: "TP3 - Arreglos y Excepciones"
description: Trabajo práctico sobre manejo de arreglos y excepciones en Java.
---

# TP-3 - Arreglos y Excepciones

## Forma de entrega

:::{important}
- No olviden completar la plantilla con sus datos y agregar la descripción de cada función.
  Aunque `main` puede no tener este comentario, no está de más registrar qué es lo que el
  `Scanner` recibe.
- Siempre que sea posible, los mensajes de commit deben ser descriptivos.
- Implementen un `main` que haga un uso de las funciones implementadas, pueden utilizar un `Scanner`.
- La entrada y salida deben estar separadas de la función que cumple la consigna, salvo que la consigna lo pida.
- No olviden la utilización de auto-formato, las herramientas de corrección le prestan atención
  a este tema.
- Puede ser necesario cambiar y ajustar la documentación de las funciones pedidas.
- Es posible hacer cambios en la forma de las funciones a implementar, pero para esto, indíquenlo explícitamente
  en el comentario de documentación.
- No olviden crear Tests.
:::

:::{tip}
Pueden usar los atajos [SOS](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20)
para preguntar sobre los enunciados individuales.
:::

(tp3-ejercicio1)=
## Ejercicio 1 - Ingreso mejorado

En `IngresoApp`, crear las siguientes funciones a partir de los siguientes prototipos.

```{code} java
:caption: Prototipos de obtieneEntero
/**
 * Solicita al usuario un número entero, repitiendo la solicitud hasta un máximo de 'intentos' veces.
 *
 * @param mensaje es lo que se le mostrara al usuario para solicitar el número entero.
 * @param intentos es la cantidad de intentos permitidos para obtener un entero válido.
 * @return El número entero válido ingresado por el usuario.
 * @throws NoMasIntentosException Si el usuario no ingresa un entero válido 
 *              después de 'intentos' intentos.
 */
public static int obtieneEntero(String mensaje, int intentos) throws NoMasIntentosException

/**
 * Solicita al usuario un número entero dentro de un rango específico, repitiendo la solicitud 
 * hasta un máximo de 'intentos' veces.
 *
 * @param mensaje es lo que se le mostrara al usuario para solicitar el número entero.
 * @param intentos es la cantidad de intentos permitidos para obtener un entero válido.
 * @param minimo El valor mínimo permitido para el entero ingresado.
 * @param maximo El valor máximo permitido para el entero ingresado.
 * @return El número entero válido ingresado por el usuario, dentro del rango especificado.
 * @throws NoMasIntentosException Si el usuario no ingresa un entero válido dentro del rango 
 *              después de 'intentos' intentos.
 */
public static int obtieneEntero(String mensaje, int intentos, int minimo, int maximo)
        throws NoMasIntentosException
```

:::{warning}
**Detalle muy importante.** No es posible armar un test con lo que hemos visto para esta función porque su propósito es
utilizar `Scanner` desde `System.in`.
:::

[SOS Ingreso mejorado](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20Ingreso%20mejorado)

(tp3-ejercicio2)=
## Ejercicio 2 - Arreglos I

En la clase `ArregloApp`, se pide implementar un conjunto de funciones para trabajar con arreglos de números enteros.

:::{note}
Pueden utilizar la funcionalidad presente en [`java.util.Arrays`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html) pero vean de
utilizarla lo menos posible de manera directa en su código.
:::

Utilicen `ArregloException` para indicar fallos que su código produzca, eviten las fallas 'naturales' como los accesos
fuera de lugar, pero lancen esta excepción en su lugar. (No es correcto atajar y lanzar, por razones que veremos más
adelante).

:::{important}
Esto requerirá que revisen la documentación para ver qué `Exception` son lanzadas y en qué situaciones, para ver cómo
evitarlas.
:::

Vean de reducir la duplicación de código dentro de las funciones, para lo cual, pueden usar y crear sus propias
funciones, además de las indicadas en las consignas y sin olvidar su documentación.

[SOS Arreglos I](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20Arreglos%20I)

### 2.1 - Convertir a cadena

Implementar una función que pueda convertir un arreglo de cualquier tamaño a `String`, mostrando su contenido y largo.

```{code} java
:caption: Prototipo aCadena
public static String aCadena(int[] arreglo)
```

Salida de ejemplo:

```java
int[] arreglo = {2, 3, 4, 5, 6, 7};
String cadena = aCadena(arreglo);
System.out.println(cadena);
```

```
6: {2, 3, 4, 5, 6, 7}
```

:::{seealso}
Comparen con [`Arrays.toString`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html#toString(int%5B%5D))
:::

[SOS aCadena](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20aCadena)

### 2.2 - Cargar

:::{tip}
Para cargar el arreglo, empleen las funciones del punto 1, pueden acceder a las mismas
indicando su nombre completo `IngresoApp.obtieneEntero`.
:::

```{code} java
:caption: Prototipo cargaManual
public static int[] cargaManual(String mensaje, int cantidad)
```

[SOS Cargar](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20Cargar)

### 2.3 - Carga aleatoria

Implementar una función que cargue un arreglo con números aleatorios.

:::{note}
Utilizando [`java.util.Random`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Random.html):

```java
Random generator = new Random();
int aleatorio = generator.nextInt(minimo, maximo + 1);
```

Para darle uso, es necesario importarlo:

```java
import java.util.Random;
```
:::

Y la función a implementar debe tener este prototipo:

```{code} java
:caption: Prototipo aleatorio
public static int[] aleatorio(int minimo, int maximo, int cantidad)
```

Esta función es interesante para probar el funcionamiento del resto.

[SOS Carga aleatoria](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20Carga%20aleatoria)

### 2.4 - Ordenar

Implementar una función que cree una copia del arreglo, pero ordenado de manera ascendente
empleando el algoritmo de ordenamiento que deseen.

:::{seealso}
Funcionalidad de librería: [`Arrays.sort`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html#sort(int%5B%5D))
:::

[SOS Ordenar](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20Ordenar)

### 2.5 - Sumar

Implementar una función que sume todos los valores del arreglo, el resultado debe ser un valor `long` para minimizar
desbordamientos.

[SOS Sumar](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20Sumar)

### 2.6 - Promediar

Implementar una función que promedie los valores del arreglo, dando el resultado como un valor decimal `double`.

[SOS Promediar](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20Promediar)

### 2.7 - Máximo / mínimo

Implementar dos funciones que obtengan el valor máximo y el mínimo.

[SOS Máximo/mínimo](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20M%C3%A1ximo%20%2F%20m%C3%ADnimo)

### 2.8 - Valor medio

Desarrollar una función que, dado un arreglo de números enteros, devuelva el elemento del arreglo que esté más cercano
al valor medio entre el máximo y el mínimo del arreglo.

#### Detalles

**Cálculo del Valor Medio**: La función debe calcular el valor medio entre el elemento máximo y el elemento mínimo del
arreglo.

**Búsqueda del Elemento Más Cercano**: Luego, debe iterar sobre el arreglo para encontrar el elemento cuya diferencia
absoluta con el valor medio calculado sea la menor.

**Manejo de empates**: Si hay varios elementos equidistantes al valor medio, la función puede devolver a cualquiera de
ellas.

:::{table} Ejemplos de valor medio
:label: tbl-valor-medio

| Ejemplo | Arreglo | Máximo | Mínimo | Valor medio | Elemento más cercano |
| :--- | :--- | :---: | :---: | :---: | :---: |
| 1 | `[1, 2, 3, 4, 5]` | 5 | 1 | 3 | 3 |
| 2 | `[2, 4, 6, 8]` | 8 | 2 | 5 | 4 o 6 |
| 3 | `[-3, 0, 5, 10]` | 10 | -3 | 3.5 | 5 |

:::

:::{seealso}
Pueden usar [`Math.abs`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Math.html#abs(int)) para
obtener el valor absoluto.
:::

[SOS Valor medio](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20Valor%20medio)

### 2.9 - Redimensión

Implementar una función que, dado un arreglo, una nueva dimensión y un valor de relleno, devuelva uno nuevo con la nueva
capacidad.

[SOS Redimensión](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20Redimensi%C3%B3n)

### 2.10 - Remover

Implementar una función que remueva el elemento en una posición del arreglo, retornando un nuevo arreglo más chico.

[SOS Remover](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20Remover)

### 2.11 - Encontrar

Implementar una función que devuelva la posición de la primera vez que aparece un número
en un arreglo.

:::{seealso}
Funcionalidad de librería: [`Arrays.binarySearch`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html#binarySearch(int%5B%5D,int,int,int))
:::

[SOS Encontrar](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20Encontrar)

(tp3-ejercicio3)=
## Ejercicio 3 - División Lenta II

Mejorar la división lenta del TP anterior para que las funciones implementadas ahora eleven un
`DivisionPorCeroException` al intentar dividir por cero.

:::{tip}
Agregar tests con `fail("Mensaje")` como vimos en clase, evitando usar el `assertThrows` que de momento IntelliJ les
sugerirá utilizar.
:::

[SOS División lenta](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP3%20-%20Divisi%C3%B3n%20lenta)
