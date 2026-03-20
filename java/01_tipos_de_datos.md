---
title: "Tipos de Datos en Java"
description: Estudio profundo sobre el sistema de tipos, representación de datos y operadores en Java desde la perspectiva de un programador de C.
---

# Tipos de Datos en Java

En Java, el sistema de tipos es estático y fuertemente tipado, lo que significa que el tipo de cada variable se conoce en tiempo de compilación. Esta rigurosidad, heredada de lenguajes como C, garantiza que el compilador pueda detectar errores de tipo antes de la ejecución.

:::{note} Similitud con C
Si venís de programar en C, vas a encontrar que la sintaxis de declaración de variables y el uso de tipos primitivos es muy similar. La diferencia principal es que Java **garantiza** el tamaño de cada tipo, mientras que en C el tamaño puede variar según la plataforma.
:::

## Declaración de Variables

La sintaxis para declarar variables es idéntica a C:

```{code} java
:caption: Declaración de variables

tipo nombreVariable;           // Declaración
tipo nombreVariable = valor;   // Declaración con inicialización
```

### Ejemplos de Declaración

```{code} java
:caption: Declaración e inicialización de variables

int edad;                    // Declaración sin inicialización
edad = 25;                   // Asignación posterior

int cantidad = 100;          // Declaración con inicialización
double precio = 19.99;
char letra = 'A';
boolean activo = true;

// Declaración múltiple del mismo tipo
int x, y, z;
int a = 1, b = 2, c = 3;
```

:::{warning} Inicialización Obligatoria
A diferencia de C, Java **exige** que las variables locales estén inicializadas antes de usarlas. El compilador emitirá un error si intentás usar una variable sin valor asignado.

```java
int x;
System.out.println(x);  // ERROR de compilación: variable no inicializada
```
:::

### Convención de Nombres

Java usa **camelCase** para nombres de variables:

```{code} java
:caption: Convenciones de nomenclatura

// ✅ Correcto (camelCase)
int edadUsuario;
double precioTotal;
boolean estaActivo;

// ❌ Incorrecto (no seguir convención)
int edad_usuario;    // snake_case (estilo C)
int EdadUsuario;     // PascalCase (reservado para clases)
int EDAD_USUARIO;    // SCREAMING_CASE (reservado para constantes)
```

## Tipos Primitivos

Java define exactamente **8 tipos primitivos**. A diferencia de C, donde el tamaño de `int` o `long` puede variar según el compilador y la arquitectura, Java garantiza tamaños fijos en todas las plataformas.

:::{table} Especificación técnica de tipos primitivos
:label: tbl-tipos-primitivos-unrn

| Tipo      | Tamaño (bits) | Rango de valores          | Equivalente en C |
| :-------- | :-----------: | :------------------------ | :--------------- |
| `byte`    |       8       | $-128$ a $127$            | `signed char`    |
| `short`   |      16       | $-32,768$ a $32,767$      | `short`          |
| `int`     |      32       | $-2^{31}$ a $2^{31}-1$    | `int` (32-bit)   |
| `long`    |      64       | $-2^{63}$ a $2^{63}-1$    | `long long`      |
| `float`   |      32       | $\pm 3.4 \times 10^{38}$  | `float`          |
| `double`  |      64       | $\pm 1.7 \times 10^{308}$ | `double`         |
| `boolean` |       1*      | `true` o `false`          | (no existe)      |
| `char`    |      16       | $0$ a $65,535$            | (16-bit Unicode) |

:::

### Comparativa de Tamaños con C

| Aspecto | C | Java |
|:---|:---|:---|
| Tamaño de `int` | Varía (16, 32 o 64 bits) | Siempre 32 bits |
| Tamaño de `long` | Varía (32 o 64 bits) | Siempre 64 bits |
| Tipo booleano | No existe (usa `int`) | `boolean` nativo |
| Tamaño de `char` | 8 bits (ASCII) | 16 bits (Unicode) |

## Tipos Enteros

### `byte` (8 bits)

El tipo más pequeño. Útil para ahorrar memoria cuando se trabaja con grandes cantidades de datos pequeños.

```{code} java
:caption: Uso de byte

byte edad = 25;
byte temperatura = -10;
byte maximo = 127;
byte minimo = -128;
// byte overflow = 128;  // ERROR: fuera de rango
```

### `short` (16 bits)

Entero de 16 bits. Poco usado en la práctica.

```{code} java
:caption: Uso de short

short poblacion = 30000;
short altitud = -500;
```

### `int` (32 bits)

El tipo entero más común y el **tipo por defecto** para literales enteros.

```{code} java
:caption: Uso de int

int cantidad = 1000000;
int saldo = -50000;
int maximo = 2147483647;   // Integer.MAX_VALUE
int minimo = -2147483648;  // Integer.MIN_VALUE
```

### `long` (64 bits)

Para valores que exceden el rango de `int`. Los literales `long` deben terminar con `L` o `l`.

```{code} java
:caption: Uso de long

long poblacionMundial = 8000000000L;  // La L es obligatoria
long distanciaKm = 384400L;           // Distancia a la Luna
long nanosegundos = System.nanoTime();
```

:::{important} El Sufijo L es Obligatorio
Sin el sufijo `L`, Java interpreta el número como `int` y puede causar errores:

```java
long grande = 10000000000;   // ERROR: el literal es muy grande para int
long grande = 10000000000L;  // ✅ Correcto
```
:::

### Literales Enteros en Diferentes Bases

Al igual que en C, Java permite escribir números en diferentes bases:

```{code} java
:caption: Literales en diferentes bases

int decimal = 42;        // Base 10 (decimal)
int binario = 0b101010;  // Base 2 (binario) - prefijo 0b
int octal = 052;         // Base 8 (octal) - prefijo 0
int hexadecimal = 0x2A;  // Base 16 (hexadecimal) - prefijo 0x

// Todos representan el mismo valor: 42
```

### Separador de Dígitos (Guión Bajo)

Java permite usar guiones bajos para mejorar la legibilidad de números grandes:

```{code} java
:caption: Guiones bajos como separadores

long poblacion = 8_000_000_000L;
int binario = 0b1111_0000_1111_0000;
double precio = 1_234_567.89;
```

## Tipos de Punto Flotante

### `float` (32 bits)

Precisión simple. Los literales deben terminar con `F` o `f`.

```{code} java
:caption: Uso de float

float temperatura = 36.5f;   // La f es obligatoria
float pi = 3.14159f;
float pequeno = 1.5e-10f;    // Notación científica
```

### `double` (64 bits)

Precisión doble. Es el **tipo por defecto** para literales decimales.

```{code} java
:caption: Uso de double

double precio = 19.99;       // No necesita sufijo
double pi = 3.141592653589793;
double avogadro = 6.022e23;  // Notación científica
double pequeno = 1.5e-300;
```

### Representación IEEE 754

Los tipos `float` y `double` siguen el estándar IEEE 754, igual que en C. Un número se descompone en:
- **Signo** ($s$): 1 bit
- **Exponente** ($e$): 8 bits (float) o 11 bits (double)
- **Mantisa** ($m$): 23 bits (float) o 52 bits (double)

$$x = (-1)^s \times 1.m \times 2^{e-sesgo}$$

:::{warning} Errores de Precisión
Muchos números decimales no tienen representación binaria exacta. Esto es idéntico al comportamiento en C:

```java
double resultado = 0.1 + 0.2;
System.out.println(resultado);  // Imprime: 0.30000000000000004

// Para comparar doubles, usar tolerancia
double a = 0.1 + 0.2;
double b = 0.3;
double epsilon = 0.0000001;
boolean sonIguales = Math.abs(a - b) < epsilon;  // ✅ Correcto
```
:::

### Valores Especiales

```{code} java
:caption: Valores especiales de punto flotante

double infinito = Double.POSITIVE_INFINITY;
double negInfinito = Double.NEGATIVE_INFINITY;
double noEsNumero = Double.NaN;  // Not a Number

// Operaciones que generan valores especiales
double divisionPorCero = 1.0 / 0.0;  // POSITIVE_INFINITY
double indeterminado = 0.0 / 0.0;    // NaN
```

## El Tipo `boolean`

A diferencia de C, donde no existe un tipo booleano nativo y se usa `int` (0 = falso, distinto de 0 = verdadero), Java tiene un tipo `boolean` dedicado.

```{code} java
:caption: Uso de boolean

boolean activo = true;
boolean encontrado = false;

// ❌ En Java esto NO compila (sí en C)
// if (1) { ... }           // ERROR: int no es boolean
// boolean b = 1;           // ERROR: no se puede asignar int a boolean

// ✅ Correcto en Java
if (activo) { ... }
boolean b = true;
```

:::{important} Diferencia Crítica con C
En C, cualquier valor distinto de 0 es "verdadero". En Java, las condiciones **deben** ser expresiones `boolean`. Esto evita errores comunes como:

```c
// En C: compila pero es un bug (asignación en vez de comparación)
if (x = 5) { ... }
```

```java
// En Java: ERROR de compilación (x = 5 retorna int, no boolean)
if (x = 5) { ... }
```
:::

## El Tipo `char`

En Java, `char` es de **16 bits** y representa un carácter Unicode (UTF-16), a diferencia de C donde es de 8 bits (ASCII).

```{code} java
:caption: Uso de char

char letra = 'A';
char digito = '7';
char simbolo = '@';
char nuevaLinea = '\n';
char tabulacion = '\t';

// Caracteres Unicode
char omega = '\u03A9';    // Ω (letra griega)
char corazon = '\u2665';  // ♥
char enie = 'ñ';          // Directamente soportado
```

### Secuencias de Escape

| Secuencia | Significado |
|:---:|:---|
| `\n` | Nueva línea |
| `\t` | Tabulación |
| `\\` | Barra invertida |
| `\'` | Comilla simple |
| `\"` | Comilla doble |
| `\r` | Retorno de carro |
| `\uXXXX` | Carácter Unicode |

### `char` como Valor Numérico

Al igual que en C, `char` es técnicamente un valor numérico y puede participar en operaciones aritméticas:

```{code} java
:caption: char como valor numérico

char letra = 'A';
int codigo = letra;           // codigo = 65
char siguiente = (char)(letra + 1);  // siguiente = 'B'

// Iterar sobre letras
for (char c = 'a'; c <= 'z'; c = (char)(c + 1)) {
    System.out.print(c);  // Imprime: abcdefghijklmnopqrstuvwxyz
}
```

## Constantes

En Java, las constantes se declaran con la palabra clave `final`:

```{code} java
:caption: Declaración de constantes

final int MAX_INTENTOS = 3;
final double PI = 3.14159265359;
final char SEPARADOR = ',';

// Convención: SCREAMING_SNAKE_CASE para constantes
final int DIAS_POR_SEMANA = 7;
final double GRAVEDAD = 9.81;
```

:::{note} Comparativa con C
En C usás `#define` o `const`. En Java, usás `final`:

| C | Java |
|:---|:---|
| `#define MAX 100` | `final int MAX = 100;` |
| `const int MAX = 100;` | `final int MAX = 100;` |
:::

## Operadores

### Operadores Aritméticos

Idénticos a C:

| Operador | Operación | Ejemplo |
|:---:|:---|:---|
| `+` | Suma | `a + b` |
| `-` | Resta | `a - b` |
| `*` | Multiplicación | `a * b` |
| `/` | División | `a / b` |
| `%` | Módulo (resto) | `a % b` |

```{code} java
:caption: Operadores aritméticos

int a = 17;
int b = 5;

int suma = a + b;       // 22
int resta = a - b;      // 12
int producto = a * b;   // 85
int cociente = a / b;   // 3 (división entera)
int resto = a % b;      // 2

// División entre enteros trunca
int resultado = 7 / 2;  // resultado = 3, no 3.5

// Para obtener decimal, al menos un operando debe ser double
double decimal = 7.0 / 2;  // decimal = 3.5
double decimal2 = (double)7 / 2;  // decimal2 = 3.5
```

### Operadores de Asignación Compuesta

:::{warning} Restricción del Curso
En este curso, **no se permite** el uso de operadores de asignación compuesta (`+=`, `-=`, etc.) ni de incremento/decremento (`++`, `--`) por motivos pedagógicos. Consultá la {ref}`regla-0x5001`.

```java
// ❌ Prohibido en el curso
contador += 1;
i++;

// ✅ Usar forma explícita
contador = contador + 1;
i = i + 1;
```
:::

### Operadores Relacionales

Devuelven un valor `boolean`:

| Operador | Significado | Ejemplo |
|:---:|:---|:---|
| `==` | Igual a | `a == b` |
| `!=` | Distinto de | `a != b` |
| `<` | Menor que | `a < b` |
| `>` | Mayor que | `a > b` |
| `<=` | Menor o igual | `a <= b` |
| `>=` | Mayor o igual | `a >= b` |

### Operadores Lógicos

| Operador | Significado | Cortocircuito |
|:---:|:---|:---:|
| `&&` | AND lógico | Sí |
| `\|\|` | OR lógico | Sí |
| `!` | NOT lógico | — |

```{code} java
:caption: Operadores lógicos con cortocircuito

boolean a = true;
boolean b = false;

boolean and = a && b;  // false
boolean or = a || b;   // true
boolean not = !a;      // false

// Cortocircuito: si a es false, b no se evalúa
if (a && b) { ... }

// Cortocircuito útil para evitar errores
if (x != 0 && y / x > 1) { ... }  // Evita división por cero
```

### Operadores de Bits

Igual que en C:

| Operador | Operación |
|:---:|:---|
| `&` | AND bit a bit |
| `\|` | OR bit a bit |
| `^` | XOR bit a bit |
| `~` | Complemento (NOT) |
| `<<` | Desplazamiento izquierda |
| `>>` | Desplazamiento derecha (con signo) |
| `>>>` | Desplazamiento derecha (sin signo) |

```{code} java
:caption: Operadores de bits

int a = 0b1010;  // 10 en binario
int b = 0b1100;  // 12 en binario

int and = a & b;   // 0b1000 = 8
int or = a | b;    // 0b1110 = 14
int xor = a ^ b;   // 0b0110 = 6
int not = ~a;      // Complemento

int izq = a << 2;  // 0b101000 = 40 (multiplica por 4)
int der = a >> 1;  // 0b0101 = 5 (divide por 2)
```

## Conversiones de Tipo (Casting)

### Promoción Automática (Implícita)

Java promueve automáticamente tipos más pequeños a más grandes:

```
byte → short → int → long → float → double
          ↑
        char
```

```{code} java
:caption: Promoción automática

byte b = 10;
int i = b;      // OK: byte se promueve a int
long l = i;     // OK: int se promueve a long
double d = l;   // OK: long se promueve a double
```

### Casting Explícito

Necesario cuando hay riesgo de pérdida de información:

```{code} java
:caption: Casting explícito

double d = 9.99;
int i = (int) d;       // i = 9 (trunca, no redondea)

long l = 1000L;
int j = (int) l;       // OK si el valor cabe en int

// Cuidado con overflow
long grande = 3000000000L;
int overflow = (int) grande;  // Resultado incorrecto (overflow)
```

### Promoción en Expresiones

En operaciones mixtas, Java promueve al tipo más grande:

```{code} java
:caption: Promoción en expresiones

int a = 5;
double b = 2.0;
double resultado = a / b;  // a se promueve a double → 2.5

// byte y short se promueven a int en operaciones
byte x = 10;
byte y = 20;
// byte z = x + y;  // ERROR: x + y es int
int z = x + y;      // OK
byte z2 = (byte)(x + y);  // OK con casting explícito
```

## Ejercicios de Aplicación

::::{exercise}
:label: ej-tipos-c-java
:class: dropdown

Dado el siguiente código en C, escribí el equivalente en Java indicando las diferencias principales:

```c
int main() {
    int x = 10;
    long y = 1000000000000;
    char c = 'A';
    float f = 3.14;
    if (x) {
        printf("x es verdadero\n");
    }
    return 0;
}
```
::::

::::{solution} ej-tipos-c-java
:class: dropdown

```java
public static void main(String[] args) {
    int x = 10;
    long y = 1000000000000L;     // Necesita sufijo L
    char c = 'A';
    float f = 3.14f;            // Necesita sufijo f
    if (x != 0) {               // No puede usar int como boolean
        System.out.println("x es verdadero");
    }
}
```

Diferencias:
1. `long` necesita sufijo `L` para literales grandes
2. `float` necesita sufijo `f`
3. La condición `if` debe ser `boolean`, no `int`
4. `printf` se reemplaza por `System.out.println`
::::

::::{exercise}
:label: ej-ieee754

Dada la representación IEEE 754, explicá por qué un `long` de 64 bits puede representar algunos números enteros que un `double` (también de 64 bits) no puede representar con precisión exacta.
::::

::::{solution} ej-ieee754
:class: dropdown

Aunque ambos ocupan 64 bits, el `long` dedica todos sus bits (salvo el de signo) a la magnitud entera. En cambio, el `double` reparte sus 64 bits entre el signo (1), el exponente (11) y la mantisa (52). Una vez que un número entero supera la capacidad de la mantisa ($2^{53}$), el `double` debe empezar a "saltar" valores (aproximar) usando el exponente, perdiendo la precisión de la unidad.
::::

::::{exercise}
:label: ej-division-entera

¿Qué valor tienen las siguientes variables después de ejecutar el código?

```java
int a = 17 / 5;
int b = 17 % 5;
double c = 17 / 5;
double d = 17.0 / 5;
```
::::

::::{solution} ej-division-entera
:class: dropdown

- `a = 3` — División entera, trunca el decimal
- `b = 2` — Resto de la división (17 = 5×3 + 2)
- `c = 3.0` — 17/5 se evalúa como división entera (3), luego se promueve a double
- `d = 3.4` — Al menos un operando es double, se hace división real
::::

::::{exercise}
:label: ej-promocion

¿Compila el siguiente código? Si no, explicá por qué y cómo corregirlo.

```java
byte a = 10;
byte b = 20;
byte c = a + b;
```
::::


````{solution} ej-promocion
:class: dropdown

No compila. El error es: "incompatible types: possible lossy conversion from `int` to `byte`".

En Java, cuando se suman dos `byte`, el resultado se promueve automáticamente a `int` para evitar overflow durante el cálculo. Por eso, no se puede asignar directamente a un `byte`.

Corrección con casting:

```java
byte a = 10;
byte b = 20;
byte c = (byte)(a + b);  // Casting explícito a byte
```
````

## Referencias Bibliográficas

- **Bloch, J.** (2018). _Effective Java_ (3ra ed.). Addison-Wesley Professional.
- **Liang, Y. D.** (2017). _Introduction to Java Programming and Data Structures_ (11va ed.). Pearson.
- **Schildt, H.** (2022). _Java: A Beginner's Guide_ (9na ed.). McGraw Hill.
- **Oracle Corporation.** (2023). _The Java Language Specification_. [Disponible en línea](https://docs.oracle.com/javase/specs/).

