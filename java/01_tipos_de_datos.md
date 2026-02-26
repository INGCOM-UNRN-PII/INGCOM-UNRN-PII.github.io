---
title: "Tipos de Datos en Java"
description: Guía detallada sobre los tipos de datos primitivos y de referencia en Java.
---

# Tipos de Datos en Java

En Java, los tipos de datos se dividen en dos categorías principales: **tipos primitivos** y **tipos de referencia**. Comprender esta distinción es fundamental para el manejo eficiente de la memoria y el correcto funcionamiento de los programas.

## Tipos Primitivos

Los tipos primitivos son los bloques de construcción más básicos del lenguaje. Se almacenan directamente en el *stack* y contienen el valor en sí mismo, no una referencia a él.

:::{table} Tipos de datos primitivos en Java
:label: tbl-tipos-primitivos

| Tipo    | Tamaño (bits) | Rango de valores | Valor por defecto |
| :------ | :-----------: | :--------------- | :---------------: |
| `byte`  | 8  | -128 a 127 | `0` |
| `short` | 16 | -32,768 a 32,767 | `0` |
| `int`   | 32 | -2³¹ a 2³¹-1 | `0` |
| `long`  | 64 | -2⁶³ a 2⁶³-1 | `0L` |
| `float` | 32 | ±3.4 × 10³⁸ (precisión ~7 dígitos) | `0.0F` |
| `double`| 64 | ±1.7 × 10³⁰⁸ (precisión ~15 dígitos) | `0.0D` |
| `boolean`| 8 | `true` o `false` | `false` |
| `char`  | 16 | Caracteres Unicode (0 a 65,535) | `'\u0000'` |

:::

### Literales Numéricos

Los literales son los valores que escribimos directamente en el código. Java permite varias formas de expresarlos:

```{code} java
:caption: Ejemplos de literales numéricos

// Enteros
int decimal = 42;           // Base 10 (por defecto)
int octal = 052;            // Base 8, prefijo 0
int hexadecimal = 0x2A;     // Base 16, prefijo 0x o 0X
int binario = 0b101010;     // Base 2, prefijo 0b o 0B

// Punto flotante
float flotante = 3.14F;     // Requiere sufijo F o f
double doble = 3.14159;     // Por defecto es double
double notacionCientifica = 1.5e10;  // 1.5 × 10¹⁰

// Long
long grande = 9_876_543_210L;  // Requiere sufijo L o l
```

:::{tip}
Desde Java 7, se pueden usar **guiones bajos** para mejorar la legibilidad de literales numéricos largos:

```java
long tarjetaCredito = 1234_5678_9012_3456L;
int hexConGuiones = 0xFF_EC_DE_5E;
```
:::

### Caracteres y Secuencias de Escape

El tipo `char` almacena un único carácter Unicode de 16 bits:

```{code} java
:caption: Literales de caracteres

char letra = 'A';
char unicode = '\u0041';      // También 'A' en Unicode
char octalChar = '\101';      // 'A' en octal
char nuevaLinea = '\n';       // Secuencia de escape
char tabulador = '\t';
char comillaSimple = '\'';
char barraInvertida = '\\';
```

## Conversión entre Tipos

### Conversión Implícita (Widening)

Java realiza conversiones automáticas cuando no hay pérdida de información:

```
byte → short → int → long → float → double
         ↑
        char
```

```{code} java
:caption: Conversión implícita

int entero = 100;
long largo = entero;      // Automático: int a long
double doble = largo;     // Automático: long a double
```

### Conversión Explícita (Narrowing)

Cuando hay riesgo de pérdida de información, se requiere un *cast* explícito:

```{code} java
:caption: Conversión explícita con cast

double doble = 9.78;
int entero = (int) doble;    // entero = 9 (se pierde la parte decimal)

long largo = 1_000_000_000_000L;
int truncado = (int) largo;  // ¡Cuidado! Overflow si excede el rango
```

:::{warning}
Al realizar conversiones *narrowing*, los valores fuera del rango del tipo destino producen resultados inesperados debido al *overflow*. Siempre verificá que el valor esté dentro del rango válido.
:::

## Tipos de Referencia

A diferencia de los primitivos, los tipos de referencia almacenan una **referencia** (dirección de memoria) a un objeto en el *heap*. Incluyen:

- **Clases** (como `String`, `Scanner`, clases propias)
- **Interfaces**
- **Arreglos**
- **Enumeraciones** (`enum`)

### Clases Wrapper

Cada tipo primitivo tiene una clase *wrapper* correspondiente que permite tratarlo como objeto:

:::{table} Clases Wrapper de tipos primitivos
:label: tbl-wrappers

| Primitivo | Wrapper | Ejemplo de uso |
| :-------- | :------ | :------------- |
| `byte`    | `Byte`  | `Byte.parseByte("127")` |
| `short`   | `Short` | `Short.MAX_VALUE` |
| `int`     | `Integer` | `Integer.parseInt("42")` |
| `long`    | `Long`  | `Long.valueOf(100L)` |
| `float`   | `Float` | `Float.isNaN(valor)` |
| `double`  | `Double` | `Double.parseDouble("3.14")` |
| `boolean` | `Boolean` | `Boolean.TRUE` |
| `char`    | `Character` | `Character.isDigit('5')` |

:::

### Autoboxing y Unboxing

Java convierte automáticamente entre primitivos y sus *wrappers*:

```{code} java
:caption: Autoboxing y Unboxing

// Autoboxing: primitivo → objeto
Integer objetoEntero = 42;  // Equivale a Integer.valueOf(42)

// Unboxing: objeto → primitivo
int primitivo = objetoEntero;  // Equivale a objetoEntero.intValue()

// En colecciones (que solo aceptan objetos)
List<Integer> numeros = new ArrayList<>();
numeros.add(10);  // Autoboxing
int primero = numeros.get(0);  // Unboxing
```

## Números de Precisión Arbitraria

Para cálculos que requieren precisión exacta o rangos muy amplios, Java ofrece:

### BigInteger

Para enteros de cualquier magnitud:

```{code} java
:caption: Uso de BigInteger

import java.math.BigInteger;

BigInteger grande = new BigInteger("123456789012345678901234567890");
BigInteger otro = BigInteger.valueOf(1000);

BigInteger suma = grande.add(otro);
BigInteger producto = grande.multiply(otro);
BigInteger potencia = grande.pow(10);
```

### BigDecimal

Para decimales con precisión exacta (ideal para cálculos financieros):

```{code} java
:caption: Uso de BigDecimal

import java.math.BigDecimal;
import java.math.RoundingMode;

BigDecimal precio = new BigDecimal("19.99");
BigDecimal cantidad = new BigDecimal("3");

BigDecimal total = precio.multiply(cantidad);
BigDecimal promedio = total.divide(cantidad, 2, RoundingMode.HALF_UP);
```

:::{important}
Nunca uses `double` o `float` para cálculos monetarios. La representación binaria de punto flotante no puede representar exactamente valores como `0.1`, lo que genera errores de redondeo acumulativos.

```java
// ¡Incorrecto!
double precio = 0.1 + 0.1 + 0.1;  // Resultado: 0.30000000000000004

// ¡Correcto!
BigDecimal precio = new BigDecimal("0.1")
    .add(new BigDecimal("0.1"))
    .add(new BigDecimal("0.1"));  // Resultado exacto: 0.3
```
:::

## Inferencia de Tipos con `var`

Desde Java 10, se puede usar `var` para variables locales, permitiendo que el compilador infiera el tipo:

```{code} java
:caption: Uso de var para inferencia de tipos

var mensaje = "Hola";           // String
var numero = 42;                // int
var lista = new ArrayList<String>();  // ArrayList<String>
var scanner = new Scanner(System.in); // Scanner
```

:::{note}
`var` solo puede usarse para variables locales con inicialización inmediata. No puede usarse para parámetros de métodos, atributos de clase, ni variables sin inicializar.
:::

## Ejercicios

```{exercise}
:label: ej-tipos-1

Dado el siguiente código, ¿cuál es el valor final de `resultado`?

```java
byte a = 100;
byte b = 50;
byte resultado = a + b;
```
```

```{solution} ej-tipos-1
El código **no compila**. La suma de dos `byte` produce un `int`, y no se puede asignar directamente a un `byte` sin cast explícito:

```java
byte resultado = (byte)(a + b);  // resultado = -106 (overflow!)
```

El resultado es `-106` debido al *overflow*: 150 excede el rango de `byte` (máximo 127).
```

```{exercise}
:label: ej-tipos-2

Explicá por qué el siguiente código produce resultados diferentes:

```java
System.out.println(0.1 + 0.2);
System.out.println(new BigDecimal("0.1").add(new BigDecimal("0.2")));
```
```

```{solution} ej-tipos-2
- La primera línea imprime `0.30000000000000004` porque `double` usa representación binaria de punto flotante (IEEE 754), donde `0.1` y `0.2` no pueden representarse exactamente.
- La segunda línea imprime `0.3` exacto porque `BigDecimal` usa representación decimal interna, almacenando el número como un entero escalado.
```

:::{seealso}
- [Documentación oficial de tipos primitivos](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html)
- [BigInteger](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/math/BigInteger.html)
- [BigDecimal](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/math/BigDecimal.html)
:::
