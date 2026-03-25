---
title: "Tipos de Datos en Java"
description: Estudio profundo sobre el sistema de tipos,
representación de datos y operadores en Java desde la perspectiva de un programador de C.
---

# Tipos de Datos en Java

En el capítulo anterior vimos que Java tiene un sistema de tipos **estático** y **fuertemente tipado**: los tipos se declaran explícitamente, se verifican en tiempo de compilación, y las conversiones con pérdida de información requieren casting explícito.

Este capítulo profundiza en los **tipos de datos concretos** que ofrece Java: los 8 tipos primitivos, sus rangos, cómo se representan en memoria, y todas las reglas de conversión entre ellos.

:::{tip} Similitud con C
Si venís de programar en C, vas a encontrar que la sintaxis de declaración de variables y el uso de tipos primitivos es **casi idéntica**. La diferencia principal es que Java **garantiza** el tamaño de cada tipo en todas las plataformas, mientras que en C el tamaño de `int` o `long` puede variar según el compilador y la arquitectura (16, 32 o 64 bits).
:::

## Declaración de Variables

Una **variable** es un espacio en memoria con un nombre que almacena un valor de un tipo específico. Pensá en una variable como una caja etiquetada: la etiqueta es el nombre, el tipo define qué puede contener la caja, y el valor es lo que está adentro.

La sintaxis para declarar variables es **idéntica a C**:

```{code} java
:caption: Declaración de variables

tipo nombreVariable;           // Declaración (reserva espacio, sin valor inicial)
tipo nombreVariable = valor;   // Declaración con inicialización (reserva y asigna)
```

¿Qué pasa en cada línea?

1. **Declaración sin inicialización:** Le decís al compilador "necesito espacio para guardar un valor de este tipo, y lo voy a llamar así". El espacio se reserva en memoria, pero no tiene un valor definido todavía.

2. **Declaración con inicialización:** Además de reservar espacio, le asignás un valor inicial inmediatamente. Es lo más seguro porque garantiza que la variable siempre tiene un valor conocido.

### Ejemplos de Declaración

```{code} java
:caption: Declaración e inicialización de variables

int edad;                    // Declaración sin inicialización
edad = 25;                   // Asignación posterior

int cantidad = 100;          // Declaración con inicialización
double precio = 19.99;
char letra = 'A';
boolean activo = true;

// Declaración múltiple del mismo tipo (todas int)
int x, y, z;
int a = 1, b = 2, c = 3;
```

Observá que la declaración múltiple (`int x, y, z;`) es exactamente igual que en C. Declara tres variables del tipo `int` en una sola línea.

:::{warning} Inicialización Obligatoria para Variables Locales
Esta es una **diferencia crítica con C**. En Java, las **variables locales** (las que declarás dentro de funciones/métodos) **deben** estar inicializadas antes de usarlas. El compilador lo verifica y emite un error si no cumplís.

```java
int x;
System.out.println(x);  // ❌ ERROR de compilación: variable x might not have been initialized
```

¿Por qué Java hace esto? En C, una variable local sin inicializar contiene "basura" —el valor que había en esa posición de memoria de antes. Esto causa bugs muy difíciles de encontrar porque el comportamiento es impredecible. Java elimina esta clase de errores obligándote a inicializar.

**Comparación C vs Java:**

```c
// En C: Compila, pero x tiene basura (comportamiento indefinido)
int main() {
    int x;
    printf("%d\n", x);  // Puede imprimir cualquier cosa
    return 0;
}
```

```java
// En Java: No compila, error detectado antes de ejecutar
public static void main(String[] args) {
    int x;
    System.out.println(x);  // ERROR de compilación
}
```
:::

### Convención de Nombres

Java tiene convenciones de nombres que **toda la comunidad sigue**. Aunque el compilador no te obliga, seguir estas convenciones hace tu código más legible y profesional.

Java usa **camelCase** para nombres de variables y métodos. El nombre empieza en minúscula, y cada palabra nueva empieza con mayúscula (sin guiones ni guiones bajos):

```{code} java
:caption: Convenciones de nomenclatura

// ✅ Correcto (camelCase para variables y métodos)
int edadUsuario;
double precioTotal;
boolean estaActivo;
String nombreCompleto;

// ❌ Incorrecto (estilos de otros lenguajes)
int edad_usuario;    // snake_case (estilo C/Python) - NO usar
int EdadUsuario;     // PascalCase - reservado para nombres de clases
int EDAD_USUARIO;    // SCREAMING_SNAKE_CASE - reservado para constantes
int edadusuario;     // Sin separación - difícil de leer
```

**¿Por qué importa esto?**

Cuando ves `EdadUsuario` en código Java, inmediatamente sabés que es una **clase**, no una variable. Cuando ves `MAX_INTENTOS`, sabés que es una **constante**. Las convenciones comunican información sin necesidad de leer más código.

:::{tip} Convenciones de nombres en Java
| Elemento | Convención | Ejemplo |
|:---|:---|:---|
| Variables | camelCase | `edadUsuario`, `precioTotal` |
| Métodos | camelCase | `calcularPromedio()`, `esValido()` |
| Clases | PascalCase | `MiClase`, `CalculadoraImpuestos` |
| Constantes | SCREAMING_SNAKE_CASE | `MAX_INTENTOS`, `PI` |
| Paquetes | minúsculas | `java.util`, `com.empresa.proyecto` |
:::

## Tipos Primitivos

Un **tipo primitivo** es un tipo de dato básico provisto directamente por el lenguaje. No es un objeto, no tiene métodos, y se almacena directamente en la variable (no como referencia a otro lugar de memoria). Son los "átomos" con los que se construyen datos más complejos.

Java define exactamente **8 tipos primitivos**. Este número es fijo —no se pueden crear nuevos tipos primitivos, solo usar estos ocho.

### ¿Qué significa "tipo primitivo"?

En Java hay dos categorías de tipos:

1. **Tipos primitivos:** Almacenan valores directamente. Son los 8 que veremos acá.
2. **Tipos de referencia:** Almacenan una "dirección" que apunta a un objeto en memoria (similar a punteros en C, pero manejados automáticamente).

Por ahora nos enfocamos en los primitivos. La diferencia práctica es que los primitivos son más eficientes (no hay indirección) y tienen tamaños garantizados.

### Portabilidad: Tamaños Garantizados

Una **diferencia fundamental con C** es que Java garantiza el tamaño de cada tipo en **todas las plataformas**. No importa si compilás para Windows, Linux, Mac, un teléfono Android o un servidor —un `int` siempre ocupa 32 bits y tiene el mismo rango.

En C, el tamaño de `int` puede ser 16, 32 o 64 bits dependiendo del compilador y la arquitectura. Esto causaba (y causa) muchos problemas de portabilidad. Java eliminó este problema definiendo tamaños fijos.

```{figure} 03/tipos_primitivos.svg
:label: fig-tipos-primitivos
:align: center
:width: 95%

Los 8 tipos primitivos de Java organizados por categoría: enteros, punto flotante, booleano y carácter.
```

### Tabla de Tipos Primitivos

:::{table} Especificación técnica de tipos primitivos
:label: tbl-tipos-primitivos-unrn

| Tipo      | Tamaño (bits) | Rango de valores          | Equivalente en C | Uso típico |
| :-------- | :-----------: | :------------------------ | :--------------- | :--------- |
| `byte`    |       8       | $-128$ a $127$            | `signed char`    | Datos binarios, ahorro de memoria |
| `short`   |      16       | $-32,768$ a $32,767$      | `short`          | Poco usado (histórico) |
| `int`     |      32       | $-2^{31}$ a $2^{31}-1$    | `int` (32-bit)   | **El tipo entero principal** |
| `long`    |      64       | $-2^{63}$ a $2^{63}-1$    | `long long`      | Números muy grandes |
| `float`   |      32       | $\pm 3.4 \times 10^{38}$  | `float`          | Precisión simple (raro en Java) |
| `double`  |      64       | $\pm 1.7 \times 10^{308}$ | `double`         | **El tipo decimal principal** |
| `boolean` |       1*      | `true` o `false`          | (no existe)      | Condiciones lógicas |
| `char`    |      16       | $0$ a $65,535$            | (16-bit Unicode) | Caracteres individuales |

:::

*El tamaño exacto de `boolean` no está especificado por la JVM; típicamente usa 1 byte por eficiencia de acceso a memoria.

### ¿Por qué tantos tipos enteros?

Podrías preguntarte: ¿por qué no usar siempre `long` que es el más grande? La respuesta es **eficiencia**:

- **Memoria:** Un arreglo de 1 millón de `byte` ocupa 1 MB. El mismo arreglo de `long` ocuparía 8 MB.
- **Velocidad:** Las operaciones con tipos más pequeños pueden ser más rápidas en algunas arquitecturas.

En la práctica, para la mayoría de los casos usamos `int` para enteros y `double` para decimales. Los otros tipos se usan en situaciones específicas.

### Comparativa de Tamaños con C

| Aspecto | C | Java |
|:---|:---|:---|
| Tamaño de `int` | **Varía** (16, 32 o 64 bits según plataforma) | **Siempre 32 bits** |
| Tamaño de `long` | **Varía** (32 o 64 bits según plataforma) | **Siempre 64 bits** |
| Tipo booleano | No existe (usa `int`: 0=falso, ≠0=verdadero) | `boolean` nativo (`true`/`false`) |
| Tamaño de `char` | 8 bits (ASCII) | 16 bits (Unicode UTF-16) |
| Tipos sin signo | `unsigned int`, `unsigned char`, etc. | **No existen** (todos con signo, excepto `char`) |

:::{important} Java no tiene tipos sin signo
A diferencia de C que tiene `unsigned int`, `unsigned char`, etc., Java **no tiene tipos sin signo** (excepto `char` que es técnicamente sin signo). Esto simplifica el lenguaje pero ocasionalmente complica operaciones bit a bit.

Si necesitás interpretar un `byte` como sin signo (0-255 en vez de -128 a 127), podés usar:
```java
int valorSinSigno = miByte & 0xFF;  // Máscara para obtener valor 0-255
```
:::

## Tipos Enteros en Detalle

Los tipos enteros almacenan números **sin parte decimal**. Java tiene cuatro tipos enteros, cada uno con diferente tamaño y rango. Todos usan **representación en complemento a dos** para números negativos (igual que en C).

### `byte` (8 bits)

El tipo entero más pequeño. Almacena valores de -128 a 127.

**¿Cuándo usar `byte`?**
- Cuando trabajás con **grandes cantidades** de datos pequeños y querés ahorrar memoria
- Al leer datos binarios (archivos, redes) donde cada byte representa información
- Rara vez se usa para cálculos normales

```{code} java
:caption: Uso de byte

byte edad = 25;          // OK: 25 está en el rango
byte temperatura = -10;  // OK: valores negativos permitidos
byte maximo = 127;       // OK: valor máximo
byte minimo = -128;      // OK: valor mínimo

// byte overflow = 128;  // ❌ ERROR de compilación: 128 fuera de rango

// ¿Qué pasa con overflow en tiempo de ejecución?
byte b = 127;
b = (byte)(b + 1);       // b = -128 (overflow, da la vuelta)
```

:::{note} Sobre el overflow
El **overflow** (desbordamiento) ocurre cuando un cálculo produce un resultado fuera del rango del tipo. En Java (igual que en C), el comportamiento es "dar la vuelta" (*wrap around*): después de 127 viene -128. Esto se debe a la representación en complemento a dos.
:::

### `short` (16 bits)

Entero de 16 bits. Rango: -32,768 a 32,767.

**¿Cuándo usar `short`?**
- Casi nunca en código moderno
- Históricamente se usaba para compatibilidad con sistemas de 16 bits
- Hoy en día, `int` es igual de eficiente en la mayoría de las arquitecturas

```{code} java
:caption: Uso de short

short poblacion = 30000;   // OK para ciudades pequeñas
short altitud = -500;      // Metros bajo el nivel del mar
short temperatura = 32767; // Máximo valor

// En la práctica, casi siempre es mejor usar int
int poblacionMejor = 30000;  // Más idiomático en Java
```

### `int` (32 bits) — El Tipo Entero Principal

El tipo entero más común y el **tipo por defecto** para literales enteros. Cuando escribís `42` en tu código, Java lo interpreta como `int`.

**Rango:** aproximadamente ±2.1 mil millones ($-2^{31}$ a $2^{31}-1$)

**¿Cuándo usar `int`?**
- **Siempre**, salvo que tengas una razón específica para otro tipo
- Contadores, índices, cantidades, identificadores
- Es el tipo entero "natural" de Java

```{code} java
:caption: Uso de int

int cantidad = 1000000;          // Un millón
int saldo = -50000;              // Puede ser negativo
int poblacionCiudad = 45000000;  // Buenos Aires

// Constantes útiles de la clase Integer
int maximo = Integer.MAX_VALUE;  // 2147483647
int minimo = Integer.MIN_VALUE;  // -2147483648

// El literal 42 es automáticamente int
int respuesta = 42;
```

### `long` (64 bits)

Para valores que exceden el rango de `int`. Rango: aproximadamente ±9.2 quintillones.

**¿Cuándo usar `long`?**
- Poblaciones mundiales, distancias astronómicas
- Timestamps en milisegundos (desde 1970)
- Identificadores únicos grandes
- Cualquier cálculo donde `int` pueda hacer overflow

:::{important} El Sufijo `L` es Obligatorio
Los literales `long` deben terminar con `L` (mayúscula recomendada) o `l` (minúscula). Sin el sufijo, Java interpreta el número como `int` y puede causar errores.

```java
// Sin sufijo: Java intenta crear un int, pero el número es muy grande
long grande = 10000000000;   // ❌ ERROR: integer number too large

// Con sufijo L: Java sabe que es un long desde el principio
long grande = 10000000000L;  // ✅ Correcto

// ¿Por qué L mayúscula? La l minúscula se confunde con el 1
long confuso = 10000000001l;  // ¿Es 1L o 11?
long claro = 10000000001L;    // Claramente es el sufijo L
```
:::

```{code} java
:caption: Uso de long

long poblacionMundial = 8_000_000_000L;  // 8 mil millones
long distanciaLuna = 384_400_000L;       // metros
long timestampActual = System.currentTimeMillis();  // Milisegundos desde 1970

// Constantes útiles
long maxLong = Long.MAX_VALUE;   // 9223372036854775807
long minLong = Long.MIN_VALUE;   // -9223372036854775808
```

### Literales Enteros en Diferentes Bases

Un **literal** es un valor escrito directamente en el código (como `42` o `"hola"`). Al igual que en C, Java permite escribir números enteros en diferentes bases numéricas:

```{code} java
:caption: Literales en diferentes bases

int decimal = 42;        // Base 10 (decimal) - sin prefijo
int binario = 0b101010;  // Base 2 (binario) - prefijo 0b o 0B
int octal = 052;         // Base 8 (octal) - prefijo 0
int hexadecimal = 0x2A;  // Base 16 (hexadecimal) - prefijo 0x o 0X

// Todos representan el mismo valor: 42
System.out.println(decimal);      // 42
System.out.println(binario);      // 42
System.out.println(octal);        // 42
System.out.println(hexadecimal);  // 42
```

**¿Por qué usar diferentes bases?**

- **Binario (`0b`):** Útil para operaciones de bits, máscaras, flags
- **Hexadecimal (`0x`):** Compacto para valores binarios (cada dígito hex = 4 bits). Usado en colores RGB, direcciones de memoria, etc.
- **Octal (`0`):** Histórico, usado en permisos de archivos Unix. ¡Cuidado! Un `0` al inicio cambia la interpretación.

:::{warning} Cuidado con el prefijo octal
Un error común es agregar ceros a la izquierda para "alinear" números, sin saber que eso los convierte en octales:

```java
int a = 010;   // ¡NO es 10! Es 8 en octal = 8 en decimal
int b = 0100;  // ¡NO es 100! Es 64 en decimal
int c = 10;    // Este sí es 10 en decimal
```
:::

### Separador de Dígitos (Guión Bajo)

Desde Java 7, podés usar guiones bajos (`_`) dentro de los números para mejorar la legibilidad. El compilador los ignora —son puramente visuales:

```{code} java
:caption: Guiones bajos como separadores visuales

// Sin separadores (difícil de leer)
long poblacion = 8000000000L;
int binario = 0b11110000111100001111000011110000;

// Con separadores (mucho más claro)
long poblacionClara = 8_000_000_000L;            // 8 mil millones
int binarioClaro = 0b1111_0000_1111_0000_1111_0000_1111_0000;
double precio = 1_234_567.89;
long telefono = 011_4444_5555L;

// Reglas: no pueden estar al inicio, al final, ni junto al punto decimal
// int invalido1 = _1000;     // ❌ ERROR
// int invalido2 = 1000_;     // ❌ ERROR
// double invalido3 = 1_.5;   // ❌ ERROR
```

**Tip:** Usá separadores para agrupar de a 3 dígitos (como los miles) o de a 4 bits en binario/hexadecimal.

## Tipos de Punto Flotante

Los tipos de punto flotante almacenan números con **parte decimal** (también llamados "números reales" en matemática, aunque la representación no es exacta). Java tiene dos tipos: `float` (precisión simple) y `double` (precisión doble).

### ¿Qué significa "punto flotante"?

El nombre viene de cómo se almacenan estos números: el "punto decimal" puede "flotar" a diferentes posiciones usando un exponente. Es similar a la notación científica:

$$3.14159 = 3.14159 \times 10^0$$
$$0.000314159 = 3.14159 \times 10^{-4}$$
$$314159 = 3.14159 \times 10^{5}$$

El mismo conjunto de dígitos (3.14159) representa diferentes magnitudes moviendo el punto.

### `float` (32 bits) — Precisión Simple

Almacena aproximadamente **7 dígitos significativos** de precisión. Los literales deben terminar con `F` o `f`.

**¿Cuándo usar `float`?**
- Casi nunca en Java moderno
- Históricamente se usaba para ahorrar memoria en gráficos 3D
- Hoy en día, `double` es preferido por su mayor precisión

```{code} java
:caption: Uso de float

float temperatura = 36.5f;   // La f es OBLIGATORIA
float pi = 3.14159f;
float pequeno = 1.5e-10f;    // Notación científica: 1.5 × 10⁻¹⁰

// Sin la f, Java interpreta el literal como double
// float error = 36.5;   // ❌ ERROR: incompatible types (double to float)
float correcto = 36.5f;     // ✅ Con sufijo f
float tambienOk = (float)36.5;  // ✅ Con casting explícito
```

### `double` (64 bits) — Precisión Doble — El Tipo Decimal Principal

Almacena aproximadamente **15-16 dígitos significativos** de precisión. Es el **tipo por defecto** para literales decimales —cuando escribís `3.14` sin sufijo, Java lo interpreta como `double`.

**¿Cuándo usar `double`?**
- **Siempre** que necesites números decimales
- Cálculos científicos, financieros, físicos
- Coordenadas, porcentajes, promedios

```{code} java
:caption: Uso de double

double precio = 19.99;       // No necesita sufijo (es el tipo por defecto)
double pi = 3.141592653589793;
double avogadro = 6.022e23;  // Notación científica: 6.022 × 10²³
double pequeno = 1.5e-300;   // Número muy pequeño

// Constantes útiles de la clase Double
double maxDouble = Double.MAX_VALUE;  // ≈1.7 × 10³⁰⁸
double minPositivo = Double.MIN_VALUE;  // ≈4.9 × 10⁻³²⁴ (mínimo positivo)
```

### Notación Científica

Para números muy grandes o muy pequeños, usá notación científica con `e` (o `E`):

```{code} java
:caption: Notación científica

// numeroEexponente significa numero × 10^exponente
double grande = 1.5e10;    // 1.5 × 10¹⁰ = 15,000,000,000
double pequeno = 2.5e-8;   // 2.5 × 10⁻⁸ = 0.000000025
double velocidadLuz = 3e8; // 3 × 10⁸ m/s

// También funciona con float (agregá la f)
float grandeF = 1.5e10f;
```

### Representación IEEE 754 — Cómo se almacenan los decimales

Los tipos `float` y `double` siguen el estándar internacional **IEEE 754**, igual que en C y prácticamente todos los lenguajes modernos. Entender este estándar te ayuda a comprender por qué los números decimales a veces se comportan de formas "extrañas".

Un número de punto flotante se descompone en tres partes:

- **Signo** ($s$): 1 bit que indica si es positivo (0) o negativo (1)
- **Exponente** ($e$): 8 bits en `float`, 11 bits en `double`. Indica la magnitud (la posición del punto)
- **Mantisa** ($m$): 23 bits en `float`, 52 bits en `double`. Los dígitos significativos

La fórmula para reconstruir el número es:

$$x = (-1)^s \times 1.m \times 2^{e-sesgo}$$

Donde:
- $(-1)^s$ determina el signo
- $1.m$ es la mantisa con un "1." implícito al inicio (normalización)
- $2^{e-sesgo}$ es el factor de escala (el sesgo es 127 para float, 1023 para double)

```{figure} 03/ieee754_representacion.svg
:label: fig-ieee754
:align: center
:width: 85%

Representación IEEE 754 de un número `double` de 64 bits, mostrando la distribución de bits entre signo, exponente y mantisa.
```

**¿Por qué importa esto?**

La cantidad de bits en la mantisa limita la **precisión** (cuántos dígitos significativos podés representar). Los bits del exponente limitan el **rango** (cuán grande o pequeño puede ser el número).

| Tipo | Bits mantisa | Dígitos significativos | Rango aproximado |
|:---|:---:|:---:|:---|
| `float` | 23 | ~7 | $10^{-38}$ a $10^{38}$ |
| `double` | 52 | ~15-16 | $10^{-308}$ a $10^{308}$ |

### El Problema de la Precisión — ¡Importante!

Aquí viene un concepto **fundamental** que causa confusión a muchos programadores: la mayoría de los números decimales **no tienen representación binaria exacta**.

Esto no es un bug de Java —es una limitación matemática de representar fracciones decimales en binario. El número 0.1 en decimal es una fracción infinita en binario, igual que 1/3 = 0.333... es infinito en decimal.

```{code} java
:caption: Errores de precisión de punto flotante

double resultado = 0.1 + 0.2;
System.out.println(resultado);  // Imprime: 0.30000000000000004 (¡no 0.3!)

// ¿Por qué? 0.1 y 0.2 no tienen representación binaria exacta
// La suma acumula pequeños errores de redondeo

// Otro ejemplo clásico
double suma = 0.0;
for (int i = 0; i < 10; i = i + 1) {
    suma = suma + 0.1;
}
System.out.println(suma);  // Imprime: 0.9999999999999999 (¡no 1.0!)
```

:::{warning} Nunca compares doubles con `==`
Debido a los errores de precisión, comparar doubles con `==` es peligroso:

```java
double a = 0.1 + 0.2;
double b = 0.3;

// ❌ PELIGROSO: puede dar false aunque "deberían" ser iguales
if (a == b) { ... }

// ✅ CORRECTO: usar tolerancia (epsilon)
double epsilon = 0.0000001;  // Tolerancia aceptable
if (Math.abs(a - b) < epsilon) {
    // Los consideramos "iguales" si la diferencia es despreciable
}
```

La función `Math.abs()` devuelve el valor absoluto (siempre positivo).
:::

Este comportamiento es **idéntico en C** y en casi todos los lenguajes. No es un defecto de Java.

### Valores Especiales de Punto Flotante

IEEE 754 define valores especiales para representar situaciones matemáticas excepcionales. Java los expone como constantes:

```{code} java
:caption: Valores especiales de punto flotante

// Infinitos (cuando el resultado es demasiado grande)
double infinito = Double.POSITIVE_INFINITY;     // +∞
double negInfinito = Double.NEGATIVE_INFINITY;  // -∞

// Not a Number (resultado indefinido o inválido)
double noEsNumero = Double.NaN;  // "Not a Number"

// ¿Cómo se generan estos valores?
double divisionPorCero = 1.0 / 0.0;    // POSITIVE_INFINITY (¡no es error!)
double divNegativa = -1.0 / 0.0;       // NEGATIVE_INFINITY
double overflow = 1e308 * 10;          // POSITIVE_INFINITY (overflow)
double indeterminado = 0.0 / 0.0;      // NaN (0/0 es indefinido)
double raizNegativa = Math.sqrt(-1);   // NaN (raíz de negativo)

// Comparación con Double.POSITIVE_INFINITY funciona normalmente
System.out.println(divisionPorCero == Double.POSITIVE_INFINITY);  // true
```

:::{important} NaN es especial
`NaN` tiene un comportamiento único: **no es igual a nada, ni siquiera a sí mismo**.

```java
double nan = Double.NaN;

// NaN no es igual a nada
System.out.println(nan == nan);     // false (!)
System.out.println(nan == 0);       // false
System.out.println(nan < 0);        // false
System.out.println(nan > 0);        // false

// Para verificar si algo es NaN, usá Double.isNaN()
System.out.println(Double.isNaN(nan));  // true
```

Este comportamiento está definido en IEEE 754 y es igual en C.
:::

**Diferencia con C:** En C, dividir un entero por cero causa un error fatal (crash). En Java con punto flotante, obtenés infinito o NaN sin que el programa falle. Sin embargo, dividir **enteros** por cero en Java sí lanza una excepción (`ArithmeticException`).

```java
double d = 1.0 / 0.0;  // OK, d = Infinity
int i = 1 / 0;         // ❌ ArithmeticException: / by zero
```

## El Tipo `boolean` — Verdadero o Falso

El tipo `boolean` representa valores lógicos: solo puede ser `true` (verdadero) o `false` (falso). No hay otros valores posibles.

### Diferencia Crítica con C

Esta es una de las **diferencias más importantes** entre Java y C:

| Aspecto | C | Java |
|:---|:---|:---|
| Tipo booleano | No existe nativamente | `boolean` nativo |
| Valores "verdaderos" | Cualquier valor ≠ 0 | Solo `true` |
| Valores "falsos" | El valor 0 | Solo `false` |
| En condiciones (`if`, `while`) | Acepta cualquier expresión numérica | **Solo** expresiones `boolean` |

En C, 0 es "falso" y cualquier otro número es "verdadero". Esto permite código como:

```c
// En C: Esto compila y funciona (aunque es confuso)
int x = 5;
if (x) {
    printf("x es distinto de cero\n");
}

int ptr = NULL;
if (!ptr) {
    printf("ptr es nulo\n");
}
```

**Java rechaza esto completamente:**

```{code} java
:caption: Uso de boolean

boolean activo = true;
boolean encontrado = false;

// ✅ Correcto en Java: condiciones booleanas explícitas
if (activo) {
    System.out.println("Está activo");
}

int x = 5;
// ❌ En Java esto NO compila
// if (x) { ... }           // ERROR: int no es boolean

// ✅ Correcto: comparación explícita
if (x != 0) {
    System.out.println("x es distinto de cero");
}

// ❌ No se puede asignar int a boolean
// boolean b = 1;           // ERROR: incompatible types
// boolean b = 0;           // ERROR: incompatible types

// ✅ Correcto
boolean b = true;
boolean c = (x > 0);  // Resultado de comparación es boolean
```

### ¿Por qué Java es tan estricto?

Esta restricción previene una clase muy común de bugs en C: confundir asignación (`=`) con comparación (`==`):

```c
// En C: BUG clásico — esto compila pero es un error lógico
int x = 0;
if (x = 5) {    // ¡Asignación, no comparación!
    // Siempre se ejecuta porque x=5 retorna 5, que es "verdadero"
}
```

```java
// En Java: El compilador detecta el error
int x = 0;
// if (x = 5) {  // ❌ ERROR de compilación: int cannot be converted to boolean
// }

if (x == 5) {   // ✅ Correcto: comparación
    // ...
}
```

El tipado estricto de Java convierte un bug silencioso en un error de compilación.

### Operaciones con boolean

Los valores `boolean` solo participan en operaciones lógicas:

```{code} java
:caption: Operaciones booleanas

boolean a = true;
boolean b = false;

boolean and = a && b;    // false (AND lógico)
boolean or = a || b;     // true (OR lógico)
boolean not = !a;        // false (NOT lógico)

// Comparaciones producen boolean
int x = 10;
int y = 20;
boolean mayor = x > y;      // false
boolean igual = x == y;     // false
boolean diferente = x != y; // true
```

:::{note} Tamaño del boolean
Aunque conceptualmente un `boolean` solo necesita 1 bit, la JVM típicamente usa **1 byte** (8 bits) para almacenarlo. Esto es porque los procesadores acceden a memoria de byte en byte —acceder a bits individuales sería más lento. En arreglos de boolean, algunas JVMs pueden optimizar esto.
:::

## El Tipo `char` — Caracteres Unicode

El tipo `char` almacena un **carácter individual**. Pero hay una diferencia importante con C:

| Aspecto | C | Java |
|:---|:---|:---|
| Tamaño | 8 bits | **16 bits** |
| Codificación | ASCII (128 caracteres) | Unicode UTF-16 (65,536 caracteres) |
| Caracteres especiales | Solo inglés básico | Acentos, ñ, cirílico, griego, chino, emoji, etc. |

### ¿Por qué 16 bits?

ASCII (7-8 bits) fue diseñado para inglés y solo tiene 128 caracteres. No incluye acentos, ni la ñ, ni caracteres de otros idiomas.

Unicode es un estándar internacional que asigna un número único a cada carácter de (casi) todos los idiomas del mundo. Java adoptó Unicode desde su primera versión (1995), lo que lo hizo verdaderamente internacional.

```{code} java
:caption: Uso de char

char letra = 'A';          // Carácter ASCII básico
char digito = '7';         // Los dígitos también son caracteres
char simbolo = '@';
char enie = 'ñ';           // ¡Directamente soportado!
char omega = 'Ω';          // Letra griega
char corazon = '♥';        // Símbolo especial

// Secuencias de escape para caracteres especiales
char nuevaLinea = '\n';    // Salto de línea
char tabulacion = '\t';    // Tabulación
char comillaSimple = '\''; // Comilla simple (escapada)
char barraInvertida = '\\';// Barra invertida (escapada)

// Caracteres Unicode por código hexadecimal
char omegaPorCodigo = '\u03A9';    // Ω (U+03A9)
char corazonPorCodigo = '\u2665';  // ♥ (U+2665)
```

### Secuencias de Escape

Las secuencias de escape permiten representar caracteres que no se pueden escribir directamente:

| Secuencia | Significado | Código ASCII |
|:---:|:---|:---:|
| `\n` | Nueva línea (line feed) | 10 |
| `\t` | Tabulación horizontal | 9 |
| `\r` | Retorno de carro (carriage return) | 13 |
| `\\` | Barra invertida literal | 92 |
| `\'` | Comilla simple literal | 39 |
| `\"` | Comilla doble literal | 34 |
| `\0` | Carácter nulo | 0 |
| `\uXXXX` | Carácter Unicode (XXXX en hexadecimal) | — |

```{code} java
:caption: Secuencias de escape en acción

// Imprimir texto con formato
System.out.println("Línea 1\nLínea 2\nLínea 3");
// Salida:
// Línea 1
// Línea 2
// Línea 3

System.out.println("Columna1\tColumna2\tColumna3");
// Salida:
// Columna1    Columna2    Columna3

System.out.println("Ella dijo: \"Hola\"");
// Salida:
// Ella dijo: "Hola"

System.out.println("Ruta: C:\\Users\\Juan");
// Salida:
// Ruta: C:\Users\Juan
```

### `char` como Valor Numérico

Al igual que en C, `char` es técnicamente un tipo numérico. Internamente almacena el **código Unicode** del carácter (un número entre 0 y 65535). Esto permite hacer aritmética con caracteres:

```{code} java
:caption: char como valor numérico

char letra = 'A';
int codigo = letra;              // codigo = 65 (código ASCII/Unicode de 'A')
System.out.println(codigo);      // Imprime: 65

// Obtener la siguiente letra
char siguiente = (char)(letra + 1);  // siguiente = 'B' (código 66)
System.out.println(siguiente);   // Imprime: B

// Convertir minúscula a mayúscula (los códigos difieren en 32)
char minuscula = 'a';            // código 97
char mayuscula = (char)(minuscula - 32);  // código 65 = 'A'
// O más legible:
mayuscula = (char)(minuscula - 'a' + 'A');

// Iterar sobre todas las letras del alfabeto
for (char c = 'a'; c <= 'z'; c = (char)(c + 1)) {
    System.out.print(c);  // Imprime: abcdefghijklmnopqrstuvwxyz
}
System.out.println();

// Verificar si un char es un dígito
char caracter = '7';
boolean esDigito = (caracter >= '0' && caracter <= '9');  // true

// Convertir char dígito a su valor numérico
int valor = caracter - '0';  // valor = 7 (no 55 que es el código de '7')
```

:::{note} Códigos importantes
| Carácter | Código | Rango |
|:---|:---|:---|
| '0' a '9' | 48 a 57 | Dígitos |
| 'A' a 'Z' | 65 a 90 | Mayúsculas |
| 'a' a 'z' | 97 a 122 | Minúsculas |
| Espacio | 32 | — |

La diferencia entre mayúsculas y minúsculas es 32 (`'a' - 'A' = 32`).
:::

### Diferencia con C: `char` es sin signo

En Java, `char` es el **único tipo primitivo sin signo**. Su rango es 0 a 65535 (no tiene valores negativos). En C, `char` puede ser con signo o sin signo dependiendo del compilador.

## Constantes con `final`

Una **constante** es una variable cuyo valor no puede cambiar después de la inicialización. En Java, se declaran con la palabra clave `final`.

### ¿Para qué sirven las constantes?

1. **Evitar "números mágicos":** En vez de escribir `7` por todos lados, usás `DIAS_POR_SEMANA`. Más legible y si cambia, lo modificás en un solo lugar.

2. **Prevenir errores:** El compilador impide que modifiques el valor accidentalmente.

3. **Documentación implícita:** El nombre de la constante explica qué significa el valor.

```{code} java
:caption: Declaración de constantes

final int MAX_INTENTOS = 3;
final double PI = 3.14159265359;
final char SEPARADOR = ',';
final String MENSAJE_ERROR = "Operación inválida";

// Convención: SCREAMING_SNAKE_CASE para constantes
final int DIAS_POR_SEMANA = 7;
final double GRAVEDAD = 9.81;
final int PUERTO_HTTP = 80;

// Una vez asignado, no se puede cambiar
// MAX_INTENTOS = 5;  // ❌ ERROR: cannot assign a value to final variable
```

### Comparativa con C

| C | Java |
|:---|:---|
| `#define MAX 100` (preprocesador, sin tipo) | `final int MAX = 100;` (con tipo) |
| `const int MAX = 100;` (variable constante) | `final int MAX = 100;` |

La diferencia es que `#define` en C es una sustitución textual del preprocesador (sin verificación de tipos), mientras que `final` en Java crea una variable real con tipo verificado.

### Constantes locales vs constantes de clase

Las constantes pueden ser locales (dentro de un método) o de clase. Las constantes de clase se verán más adelante; por ahora, usá `final` para valores que no deben cambiar dentro de tu método:

```{code} java
:caption: Constantes locales en un método

public static void main(String[] args) {
    final double TASA_IVA = 0.21;
    final int DESCUENTO_MAXIMO = 50;
    
    double precio = 100.0;
    double precioConIva = precio * (1 + TASA_IVA);
    
    System.out.println(precioConIva);  // 121.0
}
```

## Operadores

Los operadores son símbolos que realizan operaciones sobre valores (operandos). Java tiene operadores muy similares a C, con algunas diferencias importantes.

### Operadores Aritméticos

Realizan operaciones matemáticas básicas. **Idénticos a C:**

| Operador | Operación | Ejemplo | Resultado |
|:---:|:---|:---|:---|
| `+` | Suma | `5 + 3` | `8` |
| `-` | Resta | `5 - 3` | `2` |
| `*` | Multiplicación | `5 * 3` | `15` |
| `/` | División | `5 / 3` | `1` (entera) o `1.666...` (flotante) |
| `%` | Módulo (resto) | `5 % 3` | `2` |

```{code} java
:caption: Operadores aritméticos

int a = 17;
int b = 5;

int suma = a + b;       // 22
int resta = a - b;      // 12
int producto = a * b;   // 85
int cociente = a / b;   // 3 (división ENTERA, trunca el decimal)
int resto = a % b;      // 2 (17 = 5×3 + 2)

// El operador - también es unario (negación)
int negativo = -a;      // -17
```

### División Entera vs División Real — ¡Cuidado!

Este es un punto que causa muchos errores. La división entre enteros **siempre produce un entero**, truncando (no redondeando) la parte decimal:

```{code} java
:caption: División entera vs división real

// División entre enteros → resultado entero (truncado)
int resultado1 = 7 / 2;     // 3 (no 3.5, no 4)
int resultado2 = 1 / 2;     // 0 (no 0.5)
int resultado3 = -7 / 2;    // -3 (trunca hacia cero)

// Para obtener decimal, al menos UN operando debe ser double/float
double decimal1 = 7.0 / 2;      // 3.5 (7.0 es double)
double decimal2 = 7 / 2.0;      // 3.5 (2.0 es double)
double decimal3 = (double)7 / 2; // 3.5 (casting a double)
double decimal4 = 7 / (double)2; // 3.5

// ❌ ERROR COMÚN: asignar división entera a double
double mal = 7 / 2;  // mal = 3.0 (primero divide enteros → 3, luego convierte a double)
```

Este comportamiento es **idéntico en C**. La regla es: si ambos operandos son enteros, el resultado es entero.

### Operador Módulo (Resto)

El operador `%` devuelve el **resto** de la división entera. Muy útil para:

```{code} java
:caption: Usos del operador módulo

// Verificar si un número es par o impar
int numero = 17;
boolean esPar = (numero % 2 == 0);  // false (17 es impar)

// Obtener el último dígito de un número
int n = 12345;
int ultimoDigito = n % 10;  // 5

// Ciclar valores (ej: horas del reloj)
int hora = 25;
int horaReal = hora % 24;  // 1 (25 horas = 1 hora del día siguiente)

// Verificar divisibilidad
boolean divisiblePor5 = (numero % 5 == 0);  // false
```

### Operadores de Asignación Compuesta — Prohibidos en el Curso

:::{warning} Restricción Pedagógica del Curso
En este curso, **no se permite** el uso de operadores de asignación compuesta (`+=`, `-=`, `*=`, `/=`, `%=`) ni de incremento/decremento (`++`, `--`).

**¿Por qué?** Estos operadores, aunque convenientes, pueden ocultar la lógica de lo que está pasando y generar confusión con el orden de evaluación. Para aprender, es mejor ser explícito.

```java
// ❌ Prohibido en el curso
contador += 1;
i++;
j--;
total *= 2;

// ✅ Usar forma explícita (más clara)
contador = contador + 1;
i = i + 1;
j = j - 1;
total = total * 2;
```

Para más detalles, consultá la {ref}`regla-0x5001`.
:::

**Referencia:** Estos son los operadores que existen pero no debés usar en el curso:

| Operador | Equivalente explícito |
|:---|:---|
| `a += b` | `a = a + b` |
| `a -= b` | `a = a - b` |
| `a *= b` | `a = a * b` |
| `a /= b` | `a = a / b` |
| `a %= b` | `a = a % b` |
| `a++` o `++a` | `a = a + 1` |
| `a--` o `--a` | `a = a - 1` |

### Operadores Relacionales (Comparación)

Comparan dos valores y devuelven un `boolean` (`true` o `false`). **Idénticos a C:**

| Operador | Significado | Ejemplo | Resultado |
|:---:|:---|:---|:---|
| `==` | Igual a | `5 == 5` | `true` |
| `!=` | Distinto de | `5 != 3` | `true` |
| `<` | Menor que | `3 < 5` | `true` |
| `>` | Mayor que | `3 > 5` | `false` |
| `<=` | Menor o igual | `5 <= 5` | `true` |
| `>=` | Mayor o igual | `3 >= 5` | `false` |

```{code} java
:caption: Operadores relacionales

int edad = 18;

boolean esMayor = edad >= 18;       // true
boolean esExacto = edad == 18;      // true
boolean esDiferente = edad != 21;   // true
boolean esMenor = edad < 18;        // false

// Pueden usarse directamente en condiciones
if (edad >= 18) {
    System.out.println("Es mayor de edad");
}
```

:::{important} `==` vs `=`
Recordá: `==` es **comparación** (¿son iguales?), `=` es **asignación** (guardar valor).

```java
int x = 5;       // Asignación: x ahora vale 5
if (x == 5) {    // Comparación: ¿x es igual a 5?
    // ...
}
```

En Java, confundir estos en un `if` genera error de compilación (porque `x = 5` devuelve `int`, no `boolean`). En C, compila silenciosamente y causa bugs.
:::

### Operadores Lógicos

Operan sobre valores `boolean` y devuelven `boolean`. Son fundamentales para construir condiciones complejas:

| Operador | Significado | Resultado |
|:---:|:---|:---|
| `&&` | AND lógico | `true` solo si **ambos** son `true` |
| `\|\|` | OR lógico | `true` si **al menos uno** es `true` |
| `!` | NOT lógico | Invierte el valor |

**Tablas de verdad:**

| A | B | A && B | A \|\| B |
|:---:|:---:|:---:|:---:|
| true | true | true | true |
| true | false | false | true |
| false | true | false | true |
| false | false | false | false |

| A | !A |
|:---:|:---:|
| true | false |
| false | true |

```{code} java
:caption: Operadores lógicos

boolean a = true;
boolean b = false;

boolean and = a && b;  // false (true Y false = false)
boolean or = a || b;   // true (true O false = true)
boolean not = !a;      // false (NO true = false)

// Ejemplos prácticos
int edad = 25;
boolean tieneLicencia = true;

// Puede manejar si es mayor de 18 Y tiene licencia
boolean puedeManejar = (edad >= 18) && tieneLicencia;  // true

// Descuento si es estudiante O es jubilado
boolean esEstudiante = true;
boolean esJubilado = false;
boolean tieneDescuento = esEstudiante || esJubilado;   // true

// Entrada gratuita si NO es mayor de 12
int edadVisitante = 10;
boolean entradaGratis = !(edadVisitante > 12);  // true (10 no es > 12)
// Equivalente más claro:
entradaGratis = edadVisitante <= 12;
```

### Evaluación en Cortocircuito — Muy Importante

Los operadores `&&` y `||` usan **evaluación en cortocircuito** (*short-circuit evaluation*): si el resultado se puede determinar con el primer operando, el segundo **no se evalúa**.

```{code} java
:caption: Cortocircuito en operadores lógicos

// && (AND): si el primero es false, el resultado ya es false
// → no evalúa el segundo
boolean resultado1 = false && metodoQueNuncaSeEjecuta();  // false, no llama al método

// || (OR): si el primero es true, el resultado ya es true
// → no evalúa el segundo
boolean resultado2 = true || metodoQueNuncaSeEjecuta();   // true, no llama al método
```

**¿Para qué sirve?** El cortocircuito es muy útil para evitar errores:

```{code} java
:caption: Uso práctico del cortocircuito

// Evitar división por cero
int divisor = 0;
// Si divisor es 0, la primera condición es false y NO evalúa la división
if (divisor != 0 && numero / divisor > 10) {
    // ...
}

// Evitar NullPointerException (más adelante verás referencias)
String texto = null;
// Si texto es null, no intenta llamar .length() (que fallaría)
if (texto != null && texto.length() > 5) {
    // ...
}

// Sin cortocircuito, esto fallaría si texto es null
```

:::{tip} Orden de las condiciones
Cuando uses `&&`, poné primero la condición que actúa como "guarda" (la que evita el error). Cuando uses `||`, poné primero la condición más probable de ser `true` (optimización menor).
:::

### Operadores de Bits

Operan sobre la representación binaria de los números, bit a bit. **Idénticos a C** (con una adición):

| Operador | Operación | Ejemplo |
|:---:|:---|:---|
| `&` | AND bit a bit | `0b1010 & 0b1100` → `0b1000` |
| `\|` | OR bit a bit | `0b1010 \| 0b1100` → `0b1110` |
| `^` | XOR bit a bit | `0b1010 ^ 0b1100` → `0b0110` |
| `~` | Complemento (NOT) | `~0b1010` → `0b...0101` |
| `<<` | Desplazamiento izquierda | `0b0001 << 2` → `0b0100` |
| `>>` | Desplazamiento derecha (con signo) | `0b1000 >> 2` → `0b0010` |
| `>>>` | Desplazamiento derecha (sin signo) | Solo en Java |

```{code} java
:caption: Operadores de bits

int a = 0b1010;  // 10 en decimal
int b = 0b1100;  // 12 en decimal

int and = a & b;   // 0b1000 = 8   (bits en común)
int or = a | b;    // 0b1110 = 14  (bits de alguno)
int xor = a ^ b;   // 0b0110 = 6   (bits diferentes)
int not = ~a;      // Complemento a uno (invierte todos los bits)

// Desplazamientos
int izq = a << 2;  // 0b101000 = 40 (equivale a multiplicar por 4)
int der = a >> 1;  // 0b0101 = 5    (equivale a dividir por 2)
```

**¿Para qué sirven?** Los operadores de bits se usan para:
- **Máscaras:** Extraer o modificar bits específicos
- **Flags:** Almacenar múltiples booleanos en un solo entero
- **Optimización:** Multiplicar/dividir por potencias de 2 muy rápido
- **Protocolos:** Trabajar con datos binarios (redes, archivos)

```{code} java
:caption: Ejemplos prácticos de operadores de bits

// Verificar si un número es par (bit menos significativo = 0)
int numero = 42;
boolean esPar = (numero & 1) == 0;  // true (42 en binario termina en 0)

// Multiplicar por 2, 4, 8... (desplazar a la izquierda)
int x = 5;
int porDos = x << 1;    // 10
int porCuatro = x << 2; // 20
int porOcho = x << 3;   // 40

// Dividir por 2, 4, 8... (desplazar a la derecha)
int y = 40;
int entreDos = y >> 1;    // 20
int entreCuatro = y >> 2; // 10

// Extraer el byte menos significativo de un int
int valor = 0x12345678;
int byteMenor = valor & 0xFF;  // 0x78 = 120

// Intercambiar dos valores sin variable temporal (truco clásico)
int p = 5, q = 3;
p = p ^ q;  // p = 6
q = p ^ q;  // q = 5
p = p ^ q;  // p = 3
// Ahora p = 3, q = 5
```

### Operador `>>>` — Exclusivo de Java

Java tiene un operador de desplazamiento a la derecha **sin signo** (`>>>`), que no existe en C. La diferencia:

- `>>` preserva el signo (rellena con el bit de signo)
- `>>>` siempre rellena con ceros

```{code} java
:caption: Diferencia entre >> y >>>

int negativo = -8;  // En binario: 11111111111111111111111111111000

int conSigno = negativo >> 2;   // -2 (rellena con 1s, preserva signo)
int sinSigno = negativo >>> 2;  // 1073741822 (rellena con 0s)

// Con números positivos, ambos dan el mismo resultado
int positivo = 8;
int a = positivo >> 2;   // 2
int b = positivo >>> 2;  // 2
```

### Precedencia de Operadores

Cuando una expresión tiene múltiples operadores, Java los evalúa en un orden específico llamado **precedencia**. Los operadores con mayor precedencia se evalúan primero.

| Precedencia | Operadores | Asociatividad |
|:---:|:---|:---:|
| 1 (mayor) | `()` (paréntesis) | — |
| 2 | `!` `~` `-` (unarios) | Derecha a izquierda |
| 3 | `*` `/` `%` | Izquierda a derecha |
| 4 | `+` `-` | Izquierda a derecha |
| 5 | `<<` `>>` `>>>` | Izquierda a derecha |
| 6 | `<` `<=` `>` `>=` | Izquierda a derecha |
| 7 | `==` `!=` | Izquierda a derecha |
| 8 | `&` | Izquierda a derecha |
| 9 | `^` | Izquierda a derecha |
| 10 | `\|` | Izquierda a derecha |
| 11 | `&&` | Izquierda a derecha |
| 12 | `\|\|` | Izquierda a derecha |
| 13 (menor) | `=` (asignación) | Derecha a izquierda |

```{code} java
:caption: Ejemplos de precedencia

// Multiplicación antes que suma
int a = 2 + 3 * 4;      // 2 + 12 = 14, no 20

// Paréntesis fuerzan orden
int b = (2 + 3) * 4;    // 5 * 4 = 20

// Comparación antes que lógicos
boolean c = 5 > 3 && 2 < 4;  // (5 > 3) && (2 < 4) = true && true = true

// AND antes que OR
boolean d = true || false && false;  // true || (false && false) = true || false = true
boolean e = (true || false) && false; // true && false = false
```

:::{tip} Usá paréntesis para claridad
Aunque conozcas las reglas de precedencia, usar paréntesis hace el código más legible y evita errores:

```java
// Técnicamente correcto, pero confuso
boolean ok = x > 0 && x < 100 || y > 0 && y < 100;

// Mucho más claro con paréntesis
boolean ok = (x > 0 && x < 100) || (y > 0 && y < 100);
```
:::

## Conversiones de Tipo (Casting)

En ocasiones necesitás convertir un valor de un tipo a otro. Java tiene dos tipos de conversiones:

1. **Implícitas (automáticas):** Java las hace sin que lo pidas
2. **Explícitas (casting):** Tenés que indicarlas con `(tipo)`

### Conversiones Implícitas (Promoción Automática / Widening)

Java promueve automáticamente tipos más pequeños a más grandes **cuando no hay riesgo de perder información**. Esto se llama *widening* (ensanchamiento) porque el tipo destino es "más ancho" (tiene más bits).

La regla general es: si el tipo destino puede contener todos los valores posibles del tipo origen, la conversión es automática.

```{figure} 03/conversion_tipos.svg
:label: fig-conversion-tipos
:align: center
:width: 80%

Jerarquía de promoción automática entre tipos primitivos. Las flechas indican conversiones implícitas permitidas.
```

**Cadena de promoción:**
```
byte → short → int → long → float → double
         ↑
        char
```

```{code} java
:caption: Promoción automática (widening)

byte b = 10;
short s = b;     // ✅ OK: byte cabe en short
int i = s;       // ✅ OK: short cabe en int
long l = i;      // ✅ OK: int cabe en long
float f = l;     // ✅ OK: long cabe en float (pero puede perder precisión)
double d = f;    // ✅ OK: float cabe en double

// char también se promueve a int
char c = 'A';
int codigo = c;  // ✅ OK: codigo = 65
```

:::{warning} Conversión long → float puede perder precisión
Aunque `long` (64 bits) se convierte automáticamente a `float` (32 bits), puede haber pérdida de precisión porque `float` solo tiene 23 bits de mantisa:

```java
long grande = 123456789012345L;
float f = grande;  // f = 1.23456788E14 (perdió dígitos)
```

Esto compila sin advertencia, pero puede causar problemas. Usá `double` si necesitás mantener la precisión.
:::

### Conversiones Explícitas (Casting / Narrowing)

Cuando querés convertir de un tipo más grande a uno más pequeño, hay riesgo de **perder información**. Java no hace esto automáticamente —tenés que indicarlo explícitamente con un **cast**: `(tipo)valor`.

Esto se llama *narrowing* (estrechamiento) porque el tipo destino es "más estrecho".

```{code} java
:caption: Casting explícito (narrowing)

double d = 9.99;
int i = (int) d;       // i = 9 (trunca la parte decimal, NO redondea)

long l = 1000L;
int j = (int) l;       // ✅ OK si el valor cabe en int

float f = 3.7f;
int k = (int) f;       // k = 3 (trunca)

// Casting entre enteros
int grande = 300;
byte b = (byte) grande;  // b = 44 (overflow, 300 no cabe en byte)
```

**¿Qué pasa con el overflow en casting?**

Cuando el valor no cabe en el tipo destino, Java trunca los bits más significativos. El resultado puede ser muy diferente al original:

```{code} java
:caption: Overflow en casting

// int usa 32 bits, byte usa 8 bits
// Solo se mantienen los 8 bits menos significativos

int valor = 300;           // En binario: 00000000 00000000 00000001 00101100
byte b = (byte) valor;     // Solo toma:                         00101100 = 44

int negativo = -1;         // En binario: 11111111 11111111 11111111 11111111
byte bn = (byte) negativo; // Solo toma:                         11111111 = -1

int grande = 128;          // En binario: 00000000 00000000 00000000 10000000
byte bg = (byte) grande;   // Solo toma:                         10000000 = -128 (!)
```

:::{warning} El casting no valida rangos
Java **no verifica** si el valor cabe en el tipo destino. Es tu responsabilidad asegurar que la conversión tenga sentido. Si el valor está fuera de rango, obtenés un resultado incorrecto sin ningún error ni advertencia.

```java
// ❌ Esto compila pero da resultado incorrecto
long poblacionMundial = 8_000_000_000L;
int poblacion = (int) poblacionMundial;  // poblacion = -589934592 (¡negativo!)
```
:::

### Truncamiento vs Redondeo

El casting de `double`/`float` a entero **trunca** (elimina la parte decimal), no redondea:

```{code} java
:caption: Truncamiento en casting

double d1 = 9.1;
double d2 = 9.9;
double d3 = -9.9;

int i1 = (int) d1;  // 9 (no 9)
int i2 = (int) d2;  // 9 (no 10 — trunca, no redondea)
int i3 = (int) d3;  // -9 (trunca hacia cero, no hacia -10)

// Si querés redondear, usá Math.round()
long redondeado = Math.round(9.5);  // 10
int redondeadoInt = (int) Math.round(9.5);  // 10
```

### Promoción Automática en Expresiones

Cuando operás con valores de diferentes tipos en una expresión, Java promueve automáticamente al tipo "más grande" antes de operar. Esta regla es idéntica a C:

**Reglas de promoción en expresiones:**

1. Si hay un `double`, todo se convierte a `double`
2. Si no hay `double` pero hay `float`, todo se convierte a `float`
3. Si no hay flotantes pero hay `long`, todo se convierte a `long`
4. En cualquier otro caso, **todo se convierte a `int`** (incluyendo `byte`, `short`, `char`)

```{code} java
:caption: Promoción en expresiones

int entero = 5;
double decimal = 2.0;
double resultado = entero / decimal;  // entero se promueve a double → 2.5

// Regla importante: byte, short y char se promueven a int en CUALQUIER operación
byte x = 10;
byte y = 20;
// byte z = x + y;  // ❌ ERROR: x + y es int, no byte
int z = x + y;      // ✅ OK: el resultado es int
byte z2 = (byte)(x + y);  // ✅ OK con casting explícito

// Esto también aplica a operaciones unarias (excepto asignación)
short s = 100;
// short s2 = -s;   // ❌ ERROR: -s es int
int s2 = -s;        // ✅ OK
```

**¿Por qué `byte + byte` da `int`?**

Esta regla existe para evitar overflows silenciosos. Si `byte x = 100` y `byte y = 100`, entonces `x + y = 200`, que no cabe en `byte` (-128 a 127). Java promueve a `int` para que el cálculo intermedio no haga overflow.

```{code} java
:caption: Ejemplo de por qué se promueve a int

byte a = 100;
byte b = 100;

// Sin promoción automática (hipotético), esto daría overflow:
// byte c = a + b;  // 200 no cabe en byte → resultado incorrecto

// Con promoción automática:
int c = a + b;      // c = 200 (correcto, cabe en int)

// Si realmente querés un byte (sabiendo que puede haber overflow):
byte d = (byte)(a + b);  // d = -56 (overflow intencional)
```

### Resumen de Conversiones

| Conversión | Tipo | Sintaxis | ¿Pérdida de información? |
|:---|:---|:---|:---|
| `byte` → `int` | Implícita | `int i = b;` | No |
| `int` → `long` | Implícita | `long l = i;` | No |
| `int` → `double` | Implícita | `double d = i;` | No |
| `long` → `float` | Implícita | `float f = l;` | Posible (precisión) |
| `double` → `int` | **Explícita** | `int i = (int) d;` | Sí (trunca) |
| `long` → `int` | **Explícita** | `int i = (int) l;` | Posible (overflow) |
| `int` → `byte` | **Explícita** | `byte b = (byte) i;` | Posible (overflow) |

### Conversiones entre String y Tipos Primitivos

El casting **no funciona** entre `String` y tipos primitivos porque son categorías completamente diferentes. Para estas conversiones, Java provee métodos específicos.

#### De String a tipos primitivos (parsing)

Para convertir texto a número, usás los métodos `parse` de las clases envolventes (wrapper classes):

```{code} java
:caption: Parsing de String a tipos numéricos

String textoEntero = "100";
int numero = Integer.parseInt(textoEntero);     // 100
System.out.println(numero + 50);                // 150

String textoDecimal = "19.99";
double precio = Double.parseDouble(textoDecimal);  // 19.99

String textoLargo = "9876543210";
long grande = Long.parseLong(textoLargo);       // 9876543210L

String textoByte = "42";
byte pequeño = Byte.parseByte(textoByte);       // 42

// Para boolean
String textoBoolean = "true";
boolean activo = Boolean.parseBoolean(textoBoolean);  // true
// Nota: cualquier valor distinto de "true" (ignorando mayúsculas) da false
```

:::{warning} NumberFormatException
Si el texto no representa un número válido, estos métodos lanzan una excepción en tiempo de ejecución:

```java
String invalido = "hola";
int numero = Integer.parseInt(invalido);  // ❌ Lanza NumberFormatException

String conEspacios = " 42 ";
int n = Integer.parseInt(conEspacios);    // ❌ También falla (tiene espacios)

String vacio = "";
int v = Integer.parseInt(vacio);          // ❌ También falla
```

El programa compila sin problemas, pero falla cuando se ejecuta esa línea. Más adelante aprenderás a manejar estas excepciones con `try-catch`.
:::

#### De tipos primitivos a String

Para convertir en la dirección opuesta, tenés varias opciones:

```{code} java
:caption: Conversión de primitivos a String

int numero = 42;
double precio = 19.99;
boolean activo = true;

// Opción 1: String.valueOf() — la más explícita
String s1 = String.valueOf(numero);   // "42"
String s2 = String.valueOf(precio);   // "19.99"
String s3 = String.valueOf(activo);   // "true"

// Opción 2: concatenación con String vacío — común pero menos clara
String s4 = "" + numero;              // "42"
String s5 = "" + precio;              // "19.99"

// Opción 3: métodos toString() de las clases wrapper
String s6 = Integer.toString(numero);      // "42"
String s7 = Double.toString(precio);       // "19.99"

// Opción 4: con formato específico
String s8 = String.format("%d", numero);   // "42"
String s9 = String.format("%.2f", precio); // "19.99" (2 decimales)
```

:::{tip}
La opción `String.valueOf()` es la más legible y explícita. La concatenación `"" + numero` es común pero puede confundir a lectores novatos. Elegí un estilo y mantenelo consistente en tu código.
:::

### Comparación con Python

Python, al igual que Java, requiere conversiones explícitas entre tipos incompatibles (tipado fuerte). La diferencia clave es **cuándo** se detectan los errores:

**Python (tipado dinámico y fuerte):**

```python
print("5" + str(3))   # "53" - hay que convertir explícitamente
print("5" + 3)        # ❌ TypeError: can only concatenate str (not "int") to str
print("5" - 3)        # ❌ TypeError: unsupported operand type(s)
print("5" * 3)        # "555" - repetición de string (válido en Python)
print(int("5") * 3)   # 15 - conversión explícita a entero
```

Python rechaza mezclar tipos incompatibles, pero **el error ocurre en tiempo de ejecución**. Si el código problemático está en una rama que no se ejecuta, el programa no falla.

**Java (tipado estático y fuerte):**

```java
System.out.println("5" + 3);   // "53" (concatenación, única excepción permitida)
// System.out.println("5" - 3);  // ❌ Error de compilación
// System.out.println("5" * 3);  // ❌ Error de compilación
```

Java detecta estos errores **en tiempo de compilación**, antes de ejecutar. Incluso si el código estuviera dentro de un `if (false)` que nunca se ejecutaría, el compilador lo rechaza.

**Ejemplo práctico de la diferencia:**

```python
# En Python: funciona hasta que se ejecuta la rama problemática
def calcular(x, es_texto):
    if es_texto:
        return x.upper()
    else:
        return x + "!"  # Si x es int, falla aquí (solo al ejecutar)
```

```java
// En Java: el compilador detecta problemas de tipos aunque no se ejecuten
public static String calcular(Object x, boolean esTexto) {
    if (esTexto) {
        return ((String) x).toUpperCase();
    } else {
        return x + "!";  // Compila: + con cualquier Object concatena a String
    }
}
```

:::{note}
Java te obliga a ser explícito sobre los tipos desde el principio. Esto puede parecer restrictivo, pero en proyectos grandes previene categorías enteras de bugs que en Python solo descubrirías al ejecutar ese código específico.
:::

## Ejercicios de Aplicación

::::{exercise}
:label: ej-tipos-c-java
:class: dropdown

Dado el siguiente código en C, escribí el equivalente en Java indicando las diferencias
principales:

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

Dada la representación IEEE 754, explicá por qué un `long` de 64 bits puede representar
algunos números enteros que un `double` (también de 64 bits) no puede representar con
precisión exacta.
::::

::::{solution} ej-ieee754
:class: dropdown

Aunque ambos ocupan 64 bits, el `long` dedica todos sus bits (salvo el de signo) a la
magnitud entera. En cambio, el `double` reparte sus 64 bits entre el signo (1), el
exponente (11) y la mantisa (52). Una vez que un número entero supera la capacidad de la
mantisa ($2^{53}$), el `double` debe empezar a "saltar" valores (aproximar) usando el
exponente, perdiendo la precisión de la unidad.
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
byte a = 10; byte b = 20; byte c = (byte)(a + b); // Casting explícito a byte
```
````

::::{exercise}
:label: ej-char-aritmetica

Dado el siguiente código, ¿qué valores tienen las variables al final?

```java
char letra = 'D';
int codigo = letra;
char anterior = (char)(letra - 1);
char mayuscula = 'a';
mayuscula = (char)(mayuscula - 32);
boolean esDigito = (letra >= '0' && letra <= '9');
```
::::

::::{solution} ej-char-aritmetica
:class: dropdown

- `letra = 'D'` — El carácter D
- `codigo = 68` — El código ASCII/Unicode de 'D' es 68
- `anterior = 'C'` — El carácter anterior a 'D' (código 67)
- `mayuscula = 'A'` — Convertimos 'a' (código 97) restando 32 para obtener 'A' (código 65)
- `esDigito = false` — 'D' no está en el rango '0'-'9'

**Explicación:** Los caracteres en Java son valores numéricos (su código Unicode). Podemos hacer aritmética con ellos porque internamente son números.
::::

::::{exercise}
:label: ej-comparacion-doubles

¿Por qué el siguiente código imprime "No son iguales", aunque matemáticamente 0.1 + 0.2 = 0.3?

```java
double a = 0.1 + 0.2;
double b = 0.3;
if (a == b) {
    System.out.println("Son iguales");
} else {
    System.out.println("No son iguales");
}
```

Reescribí el código para que funcione correctamente.
::::

::::{solution} ej-comparacion-doubles
:class: dropdown

**¿Por qué no son iguales?**

Los números 0.1, 0.2 y 0.3 no tienen representación binaria exacta en IEEE 754. Son fracciones infinitas en binario, igual que 1/3 = 0.333... es infinito en decimal.

- `a = 0.1 + 0.2` resulta en `0.30000000000000004`
- `b = 0.3` resulta en `0.29999999999999999`

Son números muy cercanos, pero no exactamente iguales.

**Solución: usar tolerancia (epsilon)**

```java
double a = 0.1 + 0.2;
double b = 0.3;
double epsilon = 0.0000001;  // Tolerancia aceptable

if (Math.abs(a - b) < epsilon) {
    System.out.println("Son iguales (dentro de tolerancia)");
} else {
    System.out.println("No son iguales");
}
```

Esto compara si la diferencia absoluta es menor que un valor pequeño, lo cual es la forma correcta de comparar números de punto flotante.
::::

::::{exercise}
:label: ej-literales

¿Cuáles de las siguientes líneas compilan? Para las que no compilan, explicá el error.

```java
int a = 1000000000000;
long b = 1000000000000;
long c = 1000000000000L;
float d = 3.14;
float e = 3.14f;
double f = 3.14;
byte g = 200;
byte h = 100;
```
::::

::::{solution} ej-literales
:class: dropdown

| Línea | ¿Compila? | Explicación |
|:---|:---:|:---|
| `int a = 1000000000000;` | ❌ | El literal es muy grande para `int` (máx ~2.1 mil millones) |
| `long b = 1000000000000;` | ❌ | Sin sufijo `L`, Java interpreta el literal como `int`, que es muy grande |
| `long c = 1000000000000L;` | ✅ | Con sufijo `L`, el literal es `long` |
| `float d = 3.14;` | ❌ | `3.14` es `double`, no se puede asignar a `float` sin cast |
| `float e = 3.14f;` | ✅ | Con sufijo `f`, el literal es `float` |
| `double f = 3.14;` | ✅ | `3.14` es `double` por defecto |
| `byte g = 200;` | ❌ | 200 está fuera del rango de `byte` (-128 a 127) |
| `byte h = 100;` | ✅ | 100 está dentro del rango de `byte` |
::::

::::{exercise}
:label: ej-cortocircuito

¿Qué imprime el siguiente código? Explicá paso a paso la evaluación.

```java
int x = 0;
int y = 5;

if (x != 0 && y / x > 1) {
    System.out.println("Condición verdadera");
} else {
    System.out.println("Condición falsa");
}

System.out.println("Programa terminó normalmente");
```
::::

::::{solution} ej-cortocircuito
:class: dropdown

**Salida:**
```
Condición falsa
Programa terminó normalmente
```

**Explicación paso a paso:**

1. Se evalúa `x != 0` → `0 != 0` → `false`
2. Como el operador es `&&` (AND) y el primer operando es `false`, el resultado ya es `false` sin importar el segundo operando
3. **Cortocircuito:** Java NO evalúa `y / x > 1` porque ya sabe que el resultado es `false`
4. Esto evita la división por cero (`y / x` con `x = 0` causaría `ArithmeticException`)
5. Se ejecuta el bloque `else`

**Si no existiera el cortocircuito**, el programa fallaría con una excepción de división por cero. El cortocircuito permite usar este patrón común de "verificar antes de operar".
::::

::::{exercise}
:label: ej-precedencia

Sin ejecutar el código, determiná el valor de cada variable:

```java
int a = 2 + 3 * 4;
int b = (2 + 3) * 4;
boolean c = 5 > 3 && 2 < 1;
boolean d = 5 > 3 || 2 < 1;
boolean e = !(5 > 3);
int f = 10 / 3 * 3;
double g = 10.0 / 3 * 3;
```
::::

::::{solution} ej-precedencia
:class: dropdown

| Variable | Valor | Explicación |
|:---|:---|:---|
| `a` | `14` | `*` tiene mayor precedencia: `2 + (3 * 4)` = `2 + 12` |
| `b` | `20` | Paréntesis fuerzan orden: `(2 + 3) * 4` = `5 * 4` |
| `c` | `false` | `(5 > 3)` = `true`, `(2 < 1)` = `false`, `true && false` = `false` |
| `d` | `true` | `(5 > 3)` = `true`, `(2 < 1)` = `false`, `true \|\| false` = `true` |
| `e` | `false` | `(5 > 3)` = `true`, `!true` = `false` |
| `f` | `9` | División entera: `10 / 3` = `3`, luego `3 * 3` = `9` |
| `g` | `10.0` | División real: `10.0 / 3` ≈ `3.333...`, luego `3.333... * 3` = `10.0` |

**Nota sobre `f` y `g`:** Esto ilustra cómo la división entera puede causar pérdida de información. Aunque matemáticamente `10 / 3 * 3 = 10`, con enteros obtenemos `9`.
::::

::::{exercise}
:label: ej-overflow

Predecí qué valores tienen las siguientes variables después de ejecutar el código:

```java
byte b1 = 127;
b1 = (byte)(b1 + 1);

int i1 = Integer.MAX_VALUE;
int i2 = i1 + 1;

long l1 = 3000000000L;
int i3 = (int) l1;
```
::::

::::{solution} ej-overflow
:class: dropdown

| Variable | Valor | Explicación |
|:---|:---|:---|
| `b1` | `-128` | Overflow: 127 + 1 = 128, pero 128 no cabe en byte. "Da la vuelta" a -128 (complemento a dos) |
| `i2` | `-2147483648` | Overflow: `Integer.MAX_VALUE` (2147483647) + 1 "da la vuelta" a `Integer.MIN_VALUE` |
| `i3` | `-1294967296` | 3 mil millones no cabe en `int` (máx ~2.1 mil millones). Se truncan bits y el resultado es negativo |

**Lección:** Java no detecta overflow en tiempo de ejecución para tipos primitivos. Es responsabilidad del programador verificar que los valores estén dentro del rango esperado.
::::

## Referencias Bibliográficas

- **Bloch, J.** (2018). _Effective Java_ (3ra ed.). Addison-Wesley Professional.
- **Liang, Y. D.** (2017). _Introduction to Java Programming and Data Structures_ (11va ed.). Pearson.
- **Schildt, H.** (2022). _Java: A Beginner's Guide_ (9na ed.). McGraw Hill.
- **Oracle Corporation.** (2023). _The Java Language Specification_. [Disponible en línea](https://docs.oracle.com/javase/specs/).

