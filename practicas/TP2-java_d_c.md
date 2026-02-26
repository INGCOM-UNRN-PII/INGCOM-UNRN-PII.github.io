# TP2 - Java desde C

## Forma de entrega

- No olviden completar la plantilla con sus datos y agregar la descripción de cada función.
  Aunque `main` puede no tener este comentario, no está de más registrar que es lo que el
  `Scanner` recibe.
- Siempre que sea posible, los mensajes de commit deben ser descriptivos.
- Implementen un main que haga un uso de las funciones implementadas, pueden utilizar un `Scanner`.
- La entrada y salida debe estar separada de la función que cumple la consigna, salvo que la consigna lo pida.
- No olviden la utilización de auto-formato, las herramientas de corrección le prestan atención
  a este tema.
- Puede ser necesario cambiar y ajustar la documentación de las funciones pedidas.
- Es posible hacer cambios en la forma de las funciones a implementar, pero para esto, indiquenló explicitamente
  en el comentario de documentación.
- No olviden crear Tests.

Finalmente, pueden usar los atajos tienen los
atajos [SOS - 🆘](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP2%20-%20)
para crear preguntas sobre los enunciados individuales, si es posible, no fusionen las preguntas, así es más fácil que
sus compañeros encuentren las respuestas, y pueden otorgarles mas puntos a quienes responden.

## Ejercicios

Aprovechando que la sintaxis base de Java es prácticamente la misma que la de C, reciclaremos
algunos ejercicios de Programación 1, como para tener el primer contacto con el lenguaje.

Esta práctica no está pensada para el uso de la librería de Java, más allá de
[`java.util.Scanner`](https://docs.oracle.com/javase/17/docs/api/java/util/Scanner.html)

Todos los ejercicios están planteados desde la función que resuelve la consigna. Todos deben ir
acompañados de un `main` dentro del mismo archivo como programa que les dé uso de manera
interactiva empleando `Scanner`.

### [1] Suma lenta [🆘](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP2%20-%20Suma%20lenta)

En el archivo [`SumaApp.java`](src/main/java/ar/unrn/SumaApp.java), Completar la función
suma, de forma que haga la suma entre dos números enteros sin hacer la operación de manera directa,
esto sería hacer sumas o restas de a uno.

La implementación debe pasar todos los tests (`gradlew check`). Como ya existen tests, no cambien el
nombre de la clase o función para que siga funcionando.

### [2] Suma de dígitos [🆘](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=Suma%20de%20d%C3%ADgitos)

Implementa una función que calcule la suma de los dígitos de un número entero positivo pasado como
parámetro.

En el archivo `DigitosApp`

```java
public static long sumaDigitos(int numero)
```

### [3] División lenta [🆘](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP2%20-%20Divisi%C3%B3n%20lenta)

Escribir una función que mediante restas sucesivas, obtenga el valor del cociente y el resto
de dos números enteros.

La función debe ser capaz de operar sobre cualquier par de números enteros sin importar su signo.

En el archivo `DivisionApp`

```java
public static long division(long dividendo, long divisor)

public static long resto(long dividendo, long divisor)
``` 

### [4] Potencia lenta [🆘](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP2%20-%20Potencia%20lenta)

Escribir una función que mediante sumas sucesivas, obtenga el valor de potencia entre dos
números enteros.

En el archivo `PotenciaApp`

```java
public static long potencia(short base, short exponente)
```

### [5] Factorial [🆘](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP2%20-%20Factorial)

Escribir una función que, de manera iterativa, calcule el factorial de un número entero.

En el archivo `FactorialApp`

```java
public static long factorial(short numero) 
```

### [6] Primos [🆘](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP2%20-%20Primos)

Escribir una función que indique si un número es primo o no.

En el archivo `PrimoApp`

```java
public static boolean esPrimo(long numero)
```

### [7] ¿Es Palindromo? [🆘](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP2%20-%20%C2%BFEs%20Palindromo%3F)

Escribir una función que indique si una cadena es palindromo o no.

**Sin utilizar las funciones de la libreria** Empleen `charAt`.

En el archivo `PalindromoApp`

```java
public static boolean esPalindromo(String cadena)
```

### [8] Fibonacci [🆘](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP2%20-%20Fibonacci)

Escribir una función que devuelva el n-esimo término de la sucesión de Fibonacci.

Recuerden que la sucesión se compone de la suma de los últimos dos términos. De esta manera,
el primer término vale cero y el segundo uno.

En el archivo `FibonacciApp`

```java
public static long fibonacci(int termino)
```

## Tests [🆘](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP2%20-%20Tests)

En la próxima clase veremos detalles, pero pueden tener una idea de como armarlos.
De momento, pueden ver como se construyen con el ejemplo para el ejercicio de Suma Lenta.
[`SumaAppTest`](src/test/java/ar/unrn/SumaAppTest.java)

La idea es tener código que pruebe por lo menos los casos principales del código que han
implementado, y que todos los ejercicios tengan su conjunto de tests.

Para los tests, estamos utilizando Junit 5, pueden consultar
su [manual](https://junit.org/junit5/docs/current/user-guide/)
por aca para más detalles, pero con lo que está construido el ejemplo de suma es mucho más que suficiente.

Es una herramienta _mucho_ más compleja de lo que vamos a ver como usar.

### Casos de ejemplo

A modo de ejemplo, casos de prueba para implementar.

**Ejercicio 2**

* Numero negativo
* Entre uno y cuatro dígitos

**Ejercicio 5**

* Factorial de 1
* Factorial de 5
