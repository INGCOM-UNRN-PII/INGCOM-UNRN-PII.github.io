---
title: "Entrada y Salida de Datos"
description: Estudio técnico sobre flujos de sistema, parseo de tokens y formateo avanzado en Java.
---

# Entrada y Salida de Datos

La interacción entre el programa y el usuario (o el sistema operativo) se realiza en Java a través de una abstracción denominada **Streams** (flujos de datos). Para un ingeniero, es fundamental entender que estos flujos no son solo "texto", sino canales de comunicación con diferentes propiedades de búfer y sincronización.

## Arquitectura de los Flujos de Sistema

La clase `System` proporciona tres flujos estándar predefinidos:

1.  **`System.out`**: Un `PrintStream`. Es el canal de salida estándar. Generalmente cuenta con un búfer intermedio para mejorar el rendimiento.
2.  **`System.err`**: El canal de error estándar. A diferencia de `out`, suele estar **sin búfer** (_unbuffered_). Esto garantiza que, si el programa falla catastróficamente, los mensajes de error se muestren inmediatamente en la consola antes de que el proceso termine.
3.  **`System.in`**: Un `InputStream`. Es el canal de entrada estándar (teclado). Procesa bytes crudos, por lo que suele envolverse en clases de más alto nivel como `Scanner`.

## Salida de Datos: Métodos de Impresión

Java ofrece tres métodos principales para mostrar información en la consola:

### `print()` - Impresión Sin Salto de Línea

Imprime el texto y deja el cursor en la misma línea.

```{code} java
:caption: Uso de print()

System.out.print("Hola");
System.out.print(" ");
System.out.print("Mundo");
// Resultado: Hola Mundo (todo en la misma línea)
```

### `println()` - Impresión Con Salto de Línea

Imprime el texto y **agrega automáticamente un salto de línea** al final.

```{code} java
:caption: Uso de println()

System.out.println("Primera línea");
System.out.println("Segunda línea");
// Resultado:
// Primera línea
// Segunda línea
```

```{code} java
:caption: println() sin argumentos para línea vacía

System.out.println("Antes");
System.out.println();  // Línea vacía
System.out.println("Después");
```

### `printf()` - Impresión Formateada

Permite controlar exactamente cómo se muestra cada valor mediante especificadores de formato. **No agrega salto de línea automáticamente**.

```{code} java
:caption: Sintaxis básica de printf()

System.out.printf(cadenaDeFormato, argumento1, argumento2, ...);
```

```{code} java
:caption: Ejemplo simple de printf()

int edad = 25;
String nombre = "Ana";
System.out.printf("Nombre: %s, Edad: %d años%n", nombre, edad);
// Resultado: Nombre: Ana, Edad: 25 años
```

## Formateo Completo con `printf`

El método `printf` utiliza `java.util.Formatter` internamente. Es extremadamente potente y permite controlar precisamente el formato de salida.

### Sintaxis General de un Especificador de Formato

```
%[índice$][banderas][ancho][.precisión]conversión
```

Donde:
- **`%`**: Indica el inicio de un especificador de formato
- **`índice$`** (opcional): Posición del argumento (1-based)
- **`banderas`** (opcional): Modifican el formato (alineación, relleno, signo)
- **`ancho`** (opcional): Mínimo de caracteres a mostrar
- **`.precisión`** (opcional): Dígitos decimales o longitud máxima
- **`conversión`**: El tipo de dato a formatear (obligatorio)

### Especificadores de Conversión

| Especificador | Tipo de Dato | Descripción |
|:---:|:---|:---|
| `%d` | `int`, `long`, `byte`, `short` | Entero decimal |
| `%f` | `float`, `double` | Punto flotante decimal |
| `%e` | `float`, `double` | Notación científica |
| `%g` | `float`, `double` | General (usa `%f` o `%e` según convenga) |
| `%s` | Cualquiera | Cadena de texto |
| `%c` | `char` | Carácter único |
| `%b` | `boolean` | Booleano (`true`/`false`) |
| `%x` | `int`, `long` | Hexadecimal (minúsculas) |
| `%X` | `int`, `long` | Hexadecimal (mayúsculas) |
| `%o` | `int`, `long` | Octal |
| `%n` | — | Salto de línea (independiente del SO) |
| `%%` | — | Símbolo de porcentaje literal |

```{code} java
:caption: Ejemplos de especificadores básicos

int entero = 42;
double decimal = 3.14159;
char letra = 'A';
boolean activo = true;

System.out.printf("Entero: %d%n", entero);         // Entero: 42
System.out.printf("Decimal: %f%n", decimal);       // Decimal: 3.141590
System.out.printf("Científico: %e%n", decimal);    // Científico: 3.141590e+00
System.out.printf("Carácter: %c%n", letra);        // Carácter: A
System.out.printf("Booleano: %b%n", activo);       // Booleano: true
System.out.printf("Hexadecimal: %x%n", 255);       // Hexadecimal: ff
System.out.printf("Porcentaje: 50%% de 100%n");    // Porcentaje: 50% de 100
```

:::{important} Usar `%n` en Lugar de `\n`
El especificador `%n` genera el salto de línea apropiado para el sistema operativo (`\n` en Unix/Linux/Mac, `\r\n` en Windows). Es más portable que usar `\n` directamente.
:::

### Ancho de Campo

El ancho especifica el número **mínimo** de caracteres a mostrar. Si el valor es más corto, se rellena con espacios (por defecto, a la izquierda).

```{code} java
:caption: Uso del ancho de campo

System.out.printf("[%10d]%n", 42);      // [        42]  (10 caracteres, alineado derecha)
System.out.printf("[%10s]%n", "Hola");  // [      Hola]  (10 caracteres, alineado derecha)
```

### Precisión

Para números de punto flotante, la precisión indica la cantidad de dígitos decimales. Para cadenas, indica la longitud máxima.

```{code} java
:caption: Uso de la precisión

double pi = 3.14159265359;

System.out.printf("%.2f%n", pi);    // 3.14       (2 decimales)
System.out.printf("%.4f%n", pi);    // 3.1416     (4 decimales, redondeado)
System.out.printf("%.0f%n", pi);    // 3          (sin decimales)

String texto = "Universidad";
System.out.printf("%.5s%n", texto); // Unive      (máximo 5 caracteres)
```

### Combinación de Ancho y Precisión

```{code} java
:caption: Ancho y precisión combinados

double valor = 3.14159;

System.out.printf("[%10.2f]%n", valor);  // [      3.14]  (10 ancho, 2 decimales)
System.out.printf("[%8.4f]%n", valor);   // [  3.1416]    (8 ancho, 4 decimales)
```

### Banderas de Formato

| Bandera | Descripción | Ejemplo |
|:---:|:---|:---|
| `-` | Alinear a la izquierda | `%-10d` |
| `+` | Mostrar signo (+ o -) siempre | `%+d` |
| ` ` (espacio) | Espacio antes de positivos | `% d` |
| `0` | Rellenar con ceros a la izquierda | `%05d` |
| `,` | Separador de miles | `%,d` |
| `(` | Negativos entre paréntesis | `%(d` |

```{code} java
:caption: Ejemplos de banderas

int positivo = 42;
int negativo = -42;
int grande = 1234567;

// Alineación a la izquierda
System.out.printf("[%-10d]%n", positivo);    // [42        ]

// Mostrar signo siempre
System.out.printf("[%+d]%n", positivo);      // [+42]
System.out.printf("[%+d]%n", negativo);      // [-42]

// Rellenar con ceros
System.out.printf("[%05d]%n", positivo);     // [00042]

// Separador de miles
System.out.printf("[%,d]%n", grande);        // [1,234,567]

// Negativos entre paréntesis (útil para contabilidad)
System.out.printf("[%(d]%n", negativo);      // [(42)]

// Espacio antes de positivos (alinea con negativos)
System.out.printf("[% d]%n", positivo);      // [ 42]
System.out.printf("[% d]%n", negativo);      // [-42]
```

### Combinación de Múltiples Banderas

```{code} java
:caption: Banderas combinadas

double precio = 1234.5;
double descuento = -50.0;

// Alineado izquierda, 12 ancho, 2 decimales, con signo
System.out.printf("[%-+12.2f]%n", precio);     // [+1234.50    ]
System.out.printf("[%-+12.2f]%n", descuento);  // [-50.00      ]

// Relleno con ceros, 10 ancho, 2 decimales
System.out.printf("[%010.2f]%n", precio);      // [0001234.50]
```

### Indexación de Argumentos

Podés reutilizar argumentos o cambiar su orden usando `n$`:

```{code} java
:caption: Reutilización de argumentos

System.out.printf("%1$s %2$s %1$s%n", "Hola", "Mundo");
// Resultado: Hola Mundo Hola

// Útil para formatear el mismo valor de diferentes formas
int numero = 255;
System.out.printf("Decimal: %1$d, Hexadecimal: %1$x, Octal: %1$o%n", numero);
// Resultado: Decimal: 255, Hexadecimal: ff, Octal: 377
```

### Tablas Formateadas

Una aplicación práctica de `printf` es crear tablas alineadas:

```{code} java
:caption: Ejemplo de tabla formateada

System.out.printf("%-15s %10s %10s%n", "Producto", "Cantidad", "Precio");
System.out.printf("%-15s %10d %10.2f%n", "Manzanas", 50, 1.50);
System.out.printf("%-15s %10d %10.2f%n", "Naranjas", 30, 2.25);
System.out.printf("%-15s %10d %10.2f%n", "Bananas", 100, 0.75);

// Resultado:
// Producto              Cantidad     Precio
// Manzanas                    50       1.50
// Naranjas                    30       2.25
// Bananas                    100       0.75
```

### Formateo de Fechas y Horas

Aunque en el curso usamos clases modernas, `printf` tiene soporte para fechas mediante el prefijo `t` o `T`:

| Especificador | Descripción | Ejemplo de Salida |
|:---:|:---|:---|
| `%tH` | Hora (00-23) | `14` |
| `%tM` | Minutos (00-59) | `30` |
| `%tS` | Segundos (00-59) | `45` |
| `%tT` | Hora completa (HH:MM:SS) | `14:30:45` |
| `%td` | Día del mes (01-31) | `25` |
| `%tm` | Mes (01-12) | `03` |
| `%tY` | Año (4 dígitos) | `2026` |
| `%tD` | Fecha (MM/DD/YY) | `03/25/26` |
| `%tF` | Fecha ISO (YYYY-MM-DD) | `2026-03-25` |

```{code} java
:caption: Ejemplos de formato de fecha/hora

// Requiere: import java.time.LocalDateTime;
LocalDateTime ahora = LocalDateTime.now();

System.out.printf("Hora: %tT%n", ahora);           // Hora: 14:30:45
System.out.printf("Fecha: %tF%n", ahora);          // Fecha: 2026-03-25
System.out.printf("Fecha y hora: %1$tF %1$tT%n", ahora);  // 2026-03-25 14:30:45
```

:::{tip} `String.format()` para Cadenas Formateadas
Si necesitás guardar el resultado formateado en una variable en lugar de imprimirlo, usá `String.format()` con la misma sintaxis:

```java
String mensaje = String.format("El precio es $%.2f", 19.99);
```
:::

## Entrada de Datos con `Scanner`

La clase `Scanner` es la forma más común de leer entrada del usuario en Java. Proporciona métodos para parsear diferentes tipos de datos desde el flujo de entrada.

### Creación de un Scanner

```{code} java
:caption: Creación de un Scanner para leer del teclado

// Requiere: import java.util.Scanner;
Scanner scanner = new Scanner(System.in);
```

### Métodos de Lectura de Scanner

| Método | Tipo de Retorno | Descripción |
|:---|:---:|:---|
| `next()` | `String` | Lee el siguiente token (hasta el espacio) |
| `nextLine()` | `String` | Lee toda la línea (hasta Enter) |
| `nextInt()` | `int` | Lee un número entero |
| `nextLong()` | `long` | Lee un entero largo |
| `nextDouble()` | `double` | Lee un número decimal |
| `nextFloat()` | `float` | Lee un número decimal de precisión simple |
| `nextBoolean()` | `boolean` | Lee `true` o `false` |
| `nextByte()` | `byte` | Lee un byte |
| `nextShort()` | `short` | Lee un entero corto |

```{code} java
:caption: Ejemplos de lectura con Scanner

Scanner scanner = new Scanner(System.in);

System.out.print("Ingrese su nombre: ");
String nombre = scanner.nextLine();

System.out.print("Ingrese su edad: ");
int edad = scanner.nextInt();

System.out.print("Ingrese su altura en metros: ");
double altura = scanner.nextDouble();

System.out.printf("Hola %s, tenés %d años y medís %.2f metros.%n", 
                  nombre, edad, altura);
```

### Diferencia entre `next()` y `nextLine()`

Es crucial entender la diferencia:

- **`next()`**: Lee hasta el próximo espacio en blanco. No incluye el espacio.
- **`nextLine()`**: Lee toda la línea hasta el Enter. Incluye espacios intermedios.

```{code} java
:caption: Diferencia entre next() y nextLine()

// Si el usuario escribe: "Juan Pérez"
String palabra = scanner.next();      // palabra = "Juan"
String resto = scanner.nextLine();    // resto = " Pérez"

// Si usamos solo nextLine():
String lineaCompleta = scanner.nextLine();  // lineaCompleta = "Juan Pérez"
```

### Verificación de Entrada con `hasNext`

Antes de leer, podés verificar si hay datos disponibles del tipo esperado:

| Método | Descripción |
|:---|:---|
| `hasNext()` | ¿Hay otro token? |
| `hasNextLine()` | ¿Hay otra línea? |
| `hasNextInt()` | ¿El siguiente token es un entero? |
| `hasNextDouble()` | ¿El siguiente token es un decimal? |
| `hasNextBoolean()` | ¿El siguiente token es un booleano? |

```{code} java
:caption: Validación de entrada con hasNextInt()

System.out.print("Ingrese un número entero: ");

if (scanner.hasNextInt()) {
    int numero = scanner.nextInt();
    System.out.println("Leíste: " + numero);
} else {
    System.out.println("Error: no ingresaste un número válido");
    scanner.next(); // Descartar la entrada inválida
}
```

### El Problema del Salto de Línea Residual

Este es el error más común al usar `Scanner`. Métodos como `nextInt()` o `nextDouble()` leen solo el token numérico y **dejan el carácter de salto de línea (`\n`) en el búfer**. El siguiente `nextLine()` consumirá ese salto de línea inmediatamente y retornará una cadena vacía.

```{code} java
:caption: El problema del salto de línea residual

Scanner scanner = new Scanner(System.in);

System.out.print("Ingrese su edad: ");
int edad = scanner.nextInt();          // Lee "25", deja "\n" en el búfer

System.out.print("Ingrese su nombre: ");
String nombre = scanner.nextLine();    // Lee "\n" inmediatamente → cadena vacía

System.out.println("Nombre: [" + nombre + "]");  // Imprime: Nombre: []
```

:::{important} Regla de Oro del Scanner
Si vas a leer una cadena con `nextLine()` después de haber leído un primitivo con `nextInt()`, `nextDouble()`, etc., debés realizar una llamada extra a `scanner.nextLine()` para "limpiar" el búfer.
:::

```{code} java
:caption: Solución al problema del salto de línea

Scanner scanner = new Scanner(System.in);

System.out.print("Ingrese su edad: ");
int edad = scanner.nextInt();
scanner.nextLine();  // ← Limpia el salto de línea residual

System.out.print("Ingrese su nombre: ");
String nombre = scanner.nextLine();  // Ahora lee correctamente

System.out.println("Hola " + nombre + ", tenés " + edad + " años.");
```

### Patrón Alternativo: Leer Todo como Línea

Una alternativa es leer siempre con `nextLine()` y convertir manualmente:

```{code} java
:caption: Leer todo como línea y convertir

Scanner scanner = new Scanner(System.in);

System.out.print("Ingrese su edad: ");
int edad = Integer.parseInt(scanner.nextLine());  // Lee línea y convierte

System.out.print("Ingrese su nombre: ");
String nombre = scanner.nextLine();  // No hay problema de búfer

System.out.print("Ingrese su altura: ");
double altura = Double.parseDouble(scanner.nextLine());

System.out.printf("%s tiene %d años y mide %.2f metros.%n", nombre, edad, altura);
```

:::{note} Ventajas del Patrón de Línea Completa
Este patrón evita completamente el problema del salto de línea residual, pero requiere manejo de excepciones si el usuario ingresa texto en lugar de números.
:::

### Localización y Decimales

`Scanner` es sensible a la configuración regional (_Locale_). En sistemas en español, esperará una coma `,` como separador decimal. Para forzar el punto `.` (estándar de programación), usá:

```{code} java
:caption: Configurar Scanner para usar punto decimal

// Requiere: import java.util.Locale;
Scanner scanner = new Scanner(System.in);
scanner.useLocale(Locale.US);  // Fuerza el punto como separador decimal

System.out.print("Ingrese un decimal (con punto): ");
double valor = scanner.nextDouble();  // Ahora acepta 3.14 en vez de 3,14
```

### Cierre del Scanner

Es buena práctica cerrar el Scanner cuando ya no se necesita para liberar recursos:

```{code} java
:caption: Cerrar el Scanner

Scanner scanner = new Scanner(System.in);
// ... usar el scanner ...
scanner.close();  // Libera recursos
```

:::{warning} No Cerrar Scanner de System.in Prematuramente
Si cerrás el Scanner que lee de `System.in`, también cerrarás el flujo de entrada estándar y no podrás volver a leer del teclado en tu programa.
:::

## Comparación de Métodos de Salida

| Método | Salto de Línea | Formato | Uso Típico |
|:---|:---:|:---:|:---|
| `print()` | No | No | Texto continuo |
| `println()` | Sí | No | Líneas simples |
| `printf()` | No (usar `%n`) | Sí | Formato preciso |

```{code} java
:caption: Comparación práctica

double precio = 19.99;

System.out.print("Precio: ");           // Sin formato, sin salto
System.out.println(precio);             // Solo el valor, con salto

System.out.printf("Precio: $%.2f%n", precio);  // Formato controlado
```

## Secuencias de Escape

Las secuencias de escape permiten incluir caracteres especiales en las cadenas:

| Secuencia | Significado |
|:---:|:---|
| `\n` | Salto de línea |
| `\t` | Tabulación horizontal |
| `\\` | Barra invertida literal |
| `\"` | Comilla doble literal |
| `\'` | Comilla simple literal |
| `\r` | Retorno de carro |

```{code} java
:caption: Uso de secuencias de escape

System.out.println("Primera línea\nSegunda línea");
System.out.println("Columna1\tColumna2\tColumna3");
System.out.println("Ruta: C:\\Users\\Juan\\Documentos");
System.out.println("Él dijo: \"Hola\"");
```

## Interacción Directa con la Terminal: `Console`

Para aplicaciones que se ejecutan en una terminal real, Java ofrece `System.console()`. Es superior a `Scanner` para:

- **Seguridad**: El método `readPassword()` oculta los caracteres mientras el usuario escribe y retorna un `char[]` (que puede borrarse de la memoria) en lugar de un `String`.
- **Detección**: `System.console()` devuelve `null` si el programa se está ejecutando en un entorno sin terminal (como un IDE o un pipe de Unix).

## Rendimiento: `Scanner` vs. `BufferedReader`

- **`Scanner`**: Es conveniente pero **lento**. Utiliza expresiones regulares para parsear la entrada, lo que consume mucha CPU.
- **`BufferedReader`**: Es mucho más rápido. Lee bloques grandes de texto. Si necesitás parsear números, debés hacerlo manualmente con `Integer.parseInt()`. En concursos de programación o procesamiento de grandes volúmenes de datos, `Scanner` suele ser el cuello de botella.

## Ejercicios de Aplicación

```exercise
:label: ej-scanner-logic
Dada la entrada `42\nJuan\n`, ¿qué sucede si ejecuto `nextInt()` seguido de `nextLine()`? ¿Cómo se soluciona?
```

```solution
:for: ej-scanner-logic
`nextInt()` lee `42` y deja `\n` en el búfer. El `nextLine()` encuentra el `\n`, asume que la línea terminó y devuelve una cadena vacía. La solución es llamar a `nextLine()` una vez después de `nextInt()` para descartar el salto de línea residual antes de leer el nombre real.
```

```exercise
:label: ej-printf-formato
Escribí una sentencia `printf` que muestre un número de punto flotante con exactamente 3 decimales, alineado a la derecha en un campo de 10 caracteres, con el signo siempre visible.
```

```solution
:for: ej-printf-formato
```java
double numero = 3.14159;
System.out.printf("%+10.3f%n", numero);  // Resultado: "    +3.142"
```
Los especificadores usados son:
- `+` para mostrar siempre el signo
- `10` para el ancho mínimo de 10 caracteres
- `.3` para 3 decimales
- `f` para punto flotante
```

```exercise
:label: ej-tabla-printf
Usando `printf`, mostrá una tabla de conversión de temperaturas de Celsius a Fahrenheit para los valores 0, 10, 20, 30 y 40 grados, con las columnas alineadas.
```

```solution
:for: ej-tabla-printf
```java
System.out.printf("%-10s %10s%n", "Celsius", "Fahrenheit");
System.out.printf("%-10d %10.1f%n", 0, 32.0);
System.out.printf("%-10d %10.1f%n", 10, 50.0);
System.out.printf("%-10d %10.1f%n", 20, 68.0);
System.out.printf("%-10d %10.1f%n", 30, 86.0);
System.out.printf("%-10d %10.1f%n", 40, 104.0);
```

Resultado:
```
Celsius    Fahrenheit
0                32.0
10               50.0
20               68.0
30               86.0
40              104.0
```
```

## Referencias Bibliográficas

- **Schildt, H.** (2022). _Java: A Beginner's Guide_ (9na ed.). McGraw Hill. (Capítulo 10: Enumerations, Autoboxing, and Annotations - sección sobre I/O).
- **Liang, Y. D.** (2017). _Introduction to Java Programming and Data Structures_ (11va ed.). Pearson.
- **Oracle Corporation.** (2023). _Scanning and Formatting_. [Official Tutorial](https://docs.oracle.com/javase/tutorial/essential/io/scanning.html).
- **Bloch, J.** (2018). _Effective Java_ (3ra ed.). Addison-Wesley Professional.

:::seealso

- {ref}`regla-0x0001` - Convenciones de nomenclatura.
- {ref}`regla-0x3001` - Manejo de errores en la entrada de datos.
  :::
