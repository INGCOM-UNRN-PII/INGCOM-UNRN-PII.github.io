---
title: "Entrada y Salida de Datos"
description: Estudio técnico sobre flujos de sistema, parseo de tokens y formateo avanzado en Java.
---

# Entrada y Salida de Datos

Todo programa útil necesita comunicarse con el mundo exterior: mostrar resultados al usuario, recibir datos desde el teclado, leer archivos, o enviar información a otros programas. En Java, esta comunicación se realiza a través de una abstracción denominada **streams** (flujos de datos).

Si ya se trabajó con C, los conceptos son familiares: `printf` para imprimir con formato, `scanf` para leer datos. Java ofrece mecanismos similares pero con diferencias importantes en la sintaxis y el manejo de errores. Este capítulo se centra en la entrada/salida básica por consola; el manejo de archivos se verá más adelante.

## ¿Qué es un Stream?

Un **stream** (flujo) es una secuencia ordenada de datos que fluye desde una fuente hacia un destino. La metáfora es la de un río: los datos "fluyen" en una dirección, uno tras otro.

- **Stream de entrada** (_input stream_): Los datos fluyen desde una fuente externa (teclado, archivo, red) hacia el programa.
- **Stream de salida** (_output stream_): Los datos fluyen desde el programa hacia un destino externo (pantalla, archivo, red).

Esta abstracción permite que el mismo código funcione independientemente de si los datos vienen del teclado o de un archivo: el programa solo ve un flujo de datos.

## Arquitectura de los Flujos de Sistema

El sistema operativo proporciona tres canales de comunicación estándar que todo programa hereda al iniciarse. En C se accede a ellos mediante `stdin`, `stdout` y `stderr`; en Java, a través de la clase `System`.

### Los Tres Flujos Estándar

La clase `System` proporciona tres flujos predefinidos:

1. **`System.out`** (salida estándar): El canal principal para mostrar resultados. Es un objeto de tipo `PrintStream` que ofrece métodos como `print()`, `println()` y `printf()`. Equivale a `stdout` en C.

2. **`System.err`** (error estándar): El canal para mensajes de error y diagnóstico. También es un `PrintStream`, pero con una diferencia importante: no tiene búfer, lo que significa que los mensajes aparecen inmediatamente en pantalla. Esto es crucial porque si el programa falla, los mensajes de error ya habrán sido mostrados. Equivale a `stderr` en C.

3. **`System.in`** (entrada estándar): El canal para recibir datos del usuario (normalmente el teclado). Es un `InputStream` que procesa bytes crudos. Como trabajar con bytes es incómodo, normalmente se envuelve en clases de más alto nivel como `Scanner`. Equivale a `stdin` en C.

### ¿Qué es un Búfer?

Un **búfer** (_buffer_) es una zona de memoria temporal que acumula datos antes de enviarlos a su destino. Imaginá que en lugar de hacer un viaje al supermercado cada vez que necesitás un ingrediente, hacés una lista y vas una sola vez. El búfer funciona igual: acumula varios caracteres y los envía todos juntos, lo cual es más eficiente.

`System.out` tiene búfer: cuando llamás a `print("Hola")`, los caracteres pueden no aparecer inmediatamente en pantalla; el sistema espera acumular más datos o que ocurra un salto de línea. `System.err` no tiene búfer: cada carácter aparece inmediatamente, aunque esto sea menos eficiente.

```{code} java
:caption: Diferencia entre out y err

System.out.print("Mensaje normal... ");  // Puede quedar en el búfer
System.err.print("¡ERROR!");              // Aparece inmediatamente
System.out.println(" continuación");      // El println fuerza la salida
```

:::{note} Comparación con C
En C, `stdout` también tiene búfer de línea (se vacía al encontrar `\n` o al llamar a `fflush`), mientras que `stderr` no tiene búfer. El comportamiento es idéntico en Java.
:::

## Salida de Datos: Métodos de Impresión

Java ofrece tres métodos principales para mostrar información en la consola. Entender cuándo usar cada uno es fundamental para producir salidas claras y bien formateadas.

### `print()` - Impresión Sin Salto de Línea

El método `print()` imprime el texto y deja el cursor en la misma línea. Es útil cuando se quiere construir una línea de salida en varias partes, o cuando se espera que el usuario ingrese datos en la misma línea del mensaje.

```{code} java
:caption: Uso de print()

System.out.print("Hola");
System.out.print(" ");
System.out.print("Mundo");
// Resultado: Hola Mundo (todo en la misma línea, cursor al final)
```

```{code} java
:caption: print() para prompts de entrada

System.out.print("Ingrese su nombre: ");  // El cursor queda después de ": "
// El usuario escribe en la misma línea
```

En C, esto equivale a `printf("texto")` sin `\n` al final.

### `println()` - Impresión Con Salto de Línea

El método `println()` (del inglés _print line_) imprime el texto y **agrega automáticamente un salto de línea** al final. El cursor pasa a la siguiente línea.

```{code} java
:caption: Uso de println()

System.out.println("Primera línea");
System.out.println("Segunda línea");
// Resultado:
// Primera línea
// Segunda línea
```

Este es el método más usado para mostrar mensajes simples. En C, equivale a `printf("texto\n")` con el `\n` incluido.

```{code} java
:caption: println() sin argumentos para línea vacía

System.out.println("Antes");
System.out.println();  // Línea vacía (solo imprime el salto de línea)
System.out.println("Después");
// Resultado:
// Antes
//
// Después
```

### Concatenación de Valores en print/println

A diferencia de C donde hay que especificar el formato de cada variable, Java permite **concatenar** valores de cualquier tipo usando el operador `+`:

```{code} java
:caption: Concatenación con el operador +

int edad = 25;
String nombre = "Ana";
double altura = 1.68;

System.out.println("Nombre: " + nombre);               // Nombre: Ana
System.out.println("Edad: " + edad + " años");         // Edad: 25 años
System.out.println("Altura: " + altura + " metros");   // Altura: 1.68 metros
System.out.println(nombre + " tiene " + edad + " años"); // Ana tiene 25 años
```

Java convierte automáticamente los valores numéricos a texto cuando se concatenan con un `String`. Esto es muy conveniente pero ofrece poco control sobre el formato (cantidad de decimales, alineación, etc.). Para eso existe `printf()`.

### `printf()` - Impresión Formateada

El método `printf()` permite controlar exactamente cómo se muestra cada valor mediante **especificadores de formato**. Es prácticamente idéntico al `printf()` de C, lo cual facilita la transición.

**Importante:** `printf()` **no agrega salto de línea automáticamente**. Si se quiere terminar la línea, hay que incluir `%n` o `\n` en el formato.

```{code} java
:caption: Sintaxis básica de printf()

System.out.printf(cadenaDeFormato, argumento1, argumento2, ...);
```

La cadena de formato contiene texto literal y **especificadores** que comienzan con `%`. Cada especificador indica dónde y cómo insertar el valor de un argumento.

```{code} java
:caption: Ejemplo simple de printf()

int edad = 25;
String nombre = "Ana";
System.out.printf("Nombre: %s, Edad: %d años%n", nombre, edad);
// Resultado: Nombre: Ana, Edad: 25 años
```

En este ejemplo:
- `%s` es reemplazado por el valor de `nombre` (un String)
- `%d` es reemplazado por el valor de `edad` (un entero decimal)
- `%n` produce un salto de línea

### Comparación con printf() de C

| C | Java | Descripción |
| :--- | :--- | :--- |
| `printf("Hola\n");` | `System.out.printf("Hola%n");` | Impresión simple |
| `printf("%d", x);` | `System.out.printf("%d", x);` | Entero |
| `printf("%f", x);` | `System.out.printf("%f", x);` | Flotante |
| `printf("%s", s);` | `System.out.printf("%s", s);` | Cadena |
| `printf("%.2f", x);` | `System.out.printf("%.2f", x);` | 2 decimales |

Como se puede ver, la sintaxis es casi idéntica. La diferencia principal es que en Java se llama como método de `System.out` y se recomienda usar `%n` en lugar de `\n` para portabilidad.

## Formateo Completo con `printf`

El método `printf` utiliza internamente la clase `java.util.Formatter`. Es extremadamente potente y permite controlar precisamente el formato de salida. La sintaxis es compatible con la de C, por lo que los conocimientos previos son directamente aplicables.

### Sintaxis General de un Especificador de Formato

Cada especificador de formato sigue esta estructura:

```
%[índice$][banderas][ancho][.precisión]conversión
```

Parece complicado, pero la mayoría de las partes son opcionales. Desglosemos cada elemento:

- **`%`**: Marca el inicio de un especificador de formato. Es obligatorio.

- **`índice$`** (opcional): Número que indica qué argumento usar (contando desde 1). Por defecto, los especificadores consumen los argumentos en orden.

- **`banderas`** (opcional): Caracteres que modifican el formato, como `-` para alinear a la izquierda o `0` para rellenar con ceros.

- **`ancho`** (opcional): Número mínimo de caracteres que ocupará el valor. Si el valor es más corto, se rellena con espacios.

- **`.precisión`** (opcional): Para números decimales, indica cuántos dígitos después del punto. Para cadenas, la longitud máxima.

- **`conversión`** (obligatorio): Una letra que indica el tipo de dato: `d` para enteros, `f` para decimales, `s` para cadenas, etc.

Por ejemplo, en `%+10.2f`:
- `%` → inicio del especificador
- `+` → bandera: mostrar siempre el signo
- `10` → ancho mínimo de 10 caracteres
- `.2` → precisión de 2 decimales
- `f` → conversión: número de punto flotante

### Especificadores de Conversión

La letra de conversión indica qué tipo de dato se va a formatear. Estos son los especificadores más comunes:

| Especificador | Tipo de Dato | Descripción | Equivalente en C |
|:---:|:---|:---|:---:|
| `%d` | `int`, `long`, `byte`, `short` | Entero en base decimal | `%d` |
| `%f` | `float`, `double` | Número de punto flotante | `%f` |
| `%e` | `float`, `double` | Notación científica (ej: 3.14e+00) | `%e` |
| `%g` | `float`, `double` | Usa `%f` o `%e` según convenga | `%g` |
| `%s` | Cualquier tipo | Cadena de texto (llama a `toString()`) | `%s` |
| `%c` | `char` | Un carácter único | `%c` |
| `%b` | `boolean` | Valor booleano (`true` o `false`) | — |
| `%x` | `int`, `long` | Entero en hexadecimal (minúsculas) | `%x` |
| `%X` | `int`, `long` | Entero en hexadecimal (mayúsculas) | `%X` |
| `%o` | `int`, `long` | Entero en octal | `%o` |
| `%n` | — | Salto de línea (independiente del SO) | `\n` |
| `%%` | — | El carácter `%` literal | `%%` |

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
System.out.printf("Hexadecimal: %X%n", 255);       // Hexadecimal: FF
System.out.printf("Octal: %o%n", 64);              // Octal: 100
System.out.printf("Porcentaje: 50%% de 100%n");    // Porcentaje: 50% de 100
```

:::{important} Usar `%n` en Lugar de `\n`
El especificador `%n` genera el salto de línea apropiado para el sistema operativo:
- En Unix/Linux/Mac: genera `\n`
- En Windows: genera `\r\n`

Aunque en la práctica `\n` funciona en casi todos lados, usar `%n` es la forma correcta y portable. En C se usa `\n` directamente porque no existe `%n`.
:::

### Ancho de Campo

El **ancho** especifica el número **mínimo** de caracteres que ocupará el valor formateado. Si el valor tiene menos caracteres que el ancho especificado, se rellena con espacios.

Por defecto, el relleno se hace a la **izquierda** (el valor queda alineado a la derecha).

```{code} java
:caption: Uso del ancho de campo

System.out.printf("[%5d]%n", 42);       // [   42]  (5 caracteres, relleno izquierda)
System.out.printf("[%5d]%n", 12345);    // [12345]  (exactamente 5, sin relleno)
System.out.printf("[%5d]%n", 123456);   // [123456] (6 caracteres, el ancho es mínimo)

System.out.printf("[%10s]%n", "Hola");  // [      Hola]  (10 caracteres)
System.out.printf("[%10s]%n", "Mundo"); // [     Mundo]  (10 caracteres)
```

El ancho es especialmente útil para crear columnas alineadas:

```{code} java
:caption: Columnas alineadas con ancho fijo

System.out.printf("%10s%10s%n", "Nombre", "Edad");
System.out.printf("%10s%10d%n", "Ana", 25);
System.out.printf("%10s%10d%n", "Carlos", 30);
System.out.printf("%10s%10d%n", "María", 28);

// Resultado:
//     Nombre      Edad
//        Ana        25
//     Carlos        30
//      María        28
```

:::{note} Ancho Dinámico
En C se puede usar `*` para especificar el ancho como argumento: `printf("%*d", 10, valor)`. En Java esto también funciona, aunque es menos común.
:::

### Precisión

La **precisión** tiene diferentes significados según el tipo de dato:

- **Para números de punto flotante (`%f`, `%e`)**: Indica cuántos dígitos mostrar después del punto decimal. El valor se redondea si es necesario.

- **Para cadenas (`%s`)**: Indica la longitud **máxima** de caracteres a mostrar. Si la cadena es más larga, se trunca.

- **Para enteros (`%d`)**: Indica el número mínimo de dígitos (se rellena con ceros a la izquierda).

```{code} java
:caption: Precisión con números decimales

double pi = 3.14159265359;

System.out.printf("Sin precisión: %f%n", pi);   // 3.141593 (6 decimales por defecto)
System.out.printf("2 decimales: %.2f%n", pi);   // 3.14
System.out.printf("4 decimales: %.4f%n", pi);   // 3.1416 (redondeado)
System.out.printf("0 decimales: %.0f%n", pi);   // 3
System.out.printf("8 decimales: %.8f%n", pi);   // 3.14159265
```

```{code} java
:caption: Precisión con cadenas

String texto = "Universidad Nacional";

System.out.printf("Completo: [%s]%n", texto);      // [Universidad Nacional]
System.out.printf("Máximo 5: [%.5s]%n", texto);    // [Unive]
System.out.printf("Máximo 11: [%.11s]%n", texto);  // [Universidad]
```

:::{note} Precisión por Defecto
Si no se especifica precisión para `%f`, Java usa 6 decimales por defecto. Esto es igual que en C.
:::

### Combinación de Ancho y Precisión

El ancho y la precisión se pueden usar juntos para un control total sobre el formato:

```{code} java
:caption: Ancho y precisión combinados

double valor = 3.14159;

// [ancho.precisión]: primero se aplica precisión, luego ancho
System.out.printf("[%10.2f]%n", valor);  // [      3.14]  (10 ancho total, 2 decimales)
System.out.printf("[%8.4f]%n", valor);   // [  3.1416]    (8 ancho total, 4 decimales)
System.out.printf("[%5.2f]%n", valor);   // [ 3.14]       (5 ancho total, 2 decimales)
System.out.printf("[%4.2f]%n", valor);   // [3.14]        (el ancho mínimo se supera)
```

Para entender el resultado: primero el número se formatea con la precisión indicada (por ejemplo, `3.14` para `.2f`), y luego se rellena con espacios hasta alcanzar el ancho (10 en el primer ejemplo, resultando 6 espacios + 4 caracteres del número).

### Banderas de Formato

Las **banderas** son caracteres que modifican cómo se presenta el valor. Se colocan inmediatamente después del `%` y antes del ancho.

| Bandera | Descripción | Ejemplo | Resultado |
|:---:|:---|:---|:---|
| `-` | Alinear a la izquierda (relleno a la derecha) | `%-10d` con 42 | `[42        ]` |
| `+` | Mostrar siempre el signo (+ para positivos) | `%+d` con 42 | `+42` |
| ` ` (espacio) | Espacio antes de números positivos | `% d` con 42 | ` 42` |
| `0` | Rellenar con ceros en lugar de espacios | `%05d` con 42 | `00042` |
| `,` | Usar separador de miles | `%,d` con 1234567 | `1,234,567` |
| `(` | Mostrar negativos entre paréntesis | `%(d` con -42 | `(42)` |
| `#` | Formato alternativo (prefijo 0x para hex) | `%#x` con 255 | `0xff` |

```{code} java
:caption: Ejemplos de banderas

int positivo = 42;
int negativo = -42;
int grande = 1234567;

// Alineación a la izquierda (por defecto es a la derecha)
System.out.printf("[%-10d]%n", positivo);    // [42        ]
System.out.printf("[%10d]%n", positivo);     // [        42]

// Mostrar signo siempre
System.out.printf("Positivo: [%+d]%n", positivo);   // [+42]
System.out.printf("Negativo: [%+d]%n", negativo);   // [-42]

// Rellenar con ceros
System.out.printf("Con ceros: [%05d]%n", positivo); // [00042]
System.out.printf("Con ceros: [%05d]%n", negativo); // [-0042]

// Separador de miles (muy útil para legibilidad)
System.out.printf("Grande: [%,d]%n", grande);       // [1,234,567]
System.out.printf("Con decimales: [%,.2f]%n", 1234567.89); // [1,234,567.89]

// Negativos entre paréntesis (formato contable)
System.out.printf("Contable: [%(d]%n", negativo);   // [(42)]
System.out.printf("Contable: [%(d]%n", positivo);   // [42]

// Espacio antes de positivos (alinea con negativos sin mostrar +)
System.out.printf("Espacio: [% d]%n", positivo);    // [ 42]
System.out.printf("Espacio: [% d]%n", negativo);    // [-42]

// Formato alternativo para hexadecimal
System.out.printf("Hex normal: [%x]%n", 255);       // [ff]
System.out.printf("Hex con prefijo: [%#x]%n", 255); // [0xff]
```

### Combinación de Múltiples Banderas

Las banderas se pueden combinar. El orden no importa:

```{code} java
:caption: Banderas combinadas

double precio = 1234.5;
double descuento = -50.0;

// Alineado izquierda + signo + ancho + precisión
System.out.printf("[%-+12.2f]%n", precio);     // [+1234.50    ]
System.out.printf("[%-+12.2f]%n", descuento);  // [-50.00      ]

// Relleno con ceros + ancho + precisión
System.out.printf("[%010.2f]%n", precio);      // [0001234.50]
System.out.printf("[%010.2f]%n", descuento);   // [-000050.00]

// Separador de miles + precisión
System.out.printf("[%,.2f]%n", 1234567.89);    // [1,234,567.89]

// Signo + separador de miles
System.out.printf("[%+,d]%n", 1234567);        // [+1,234,567]
```

:::{warning} Banderas Incompatibles
Algunas combinaciones no tienen sentido y generan error:
- `-` (alinear izquierda) con `0` (rellenar con ceros): ¿ceros a la derecha? No tiene sentido.
- El espacio ` ` con `+`: El signo `+` hace redundante el espacio.
:::

### Indexación de Argumentos

Por defecto, los especificadores consumen los argumentos en orden: el primer `%` usa el primer argumento, el segundo `%` usa el segundo, etc. Pero podés especificar explícitamente qué argumento usar con la notación `n$`:

```{code} java
:caption: Reutilización de argumentos

// Sin índices: los argumentos se usan en orden
System.out.printf("%s %s%n", "Hola", "Mundo");  // Hola Mundo

// Con índices: podés reutilizar o reordenar
System.out.printf("%1$s %2$s %1$s%n", "Hola", "Mundo");  // Hola Mundo Hola
System.out.printf("%2$s %1$s%n", "Hola", "Mundo");       // Mundo Hola
```

Esto es especialmente útil para mostrar el mismo valor en diferentes formatos:

```{code} java
:caption: Mismo valor, múltiples formatos

int numero = 255;
System.out.printf("Decimal: %1$d, Hex: %1$x, Octal: %1$o%n", numero);
// Resultado: Decimal: 255, Hex: ff, Octal: 377

// También útil para traducciones donde el orden de palabras cambia
String nombre = "Ana";
int edad = 25;
System.out.printf("En español: %1$s tiene %2$d años%n", nombre, edad);
System.out.printf("En inglés: %1$s is %2$d years old%n", nombre, edad);
```

:::{note} Indexación en C
En C, la indexación de argumentos (`%1$d`) es una extensión POSIX, no parte del estándar C89. En Java está siempre disponible.
:::

### Tablas Formateadas

Una aplicación práctica de `printf` es crear tablas con columnas alineadas. La clave está en usar el mismo ancho para cada columna en todas las filas:

```{code} java
:caption: Ejemplo de tabla formateada

// Encabezado: cadenas alineadas a izquierda y números a derecha
System.out.printf("%-15s %10s %10s%n", "Producto", "Cantidad", "Precio");
System.out.printf("%-15s %10s %10s%n", "---------------", "----------", "----------");
System.out.printf("%-15s %10d %10.2f%n", "Manzanas", 50, 1.50);
System.out.printf("%-15s %10d %10.2f%n", "Naranjas", 30, 2.25);
System.out.printf("%-15s %10d %10.2f%n", "Bananas", 100, 0.75);
System.out.printf("%-15s %10s %10.2f%n", "TOTAL", "", 215.00);

// Resultado:
// Producto          Cantidad     Precio
// ---------------   ---------- ----------
// Manzanas                50       1.50
// Naranjas                30       2.25
// Bananas                100       0.75
// TOTAL                          215.00
```

**Consejos para tablas:**
- Usar `%-` para alinear texto a la izquierda
- Usar anchos positivos (sin `-`) para alinear números a la derecha
- Mantener los mismos anchos en todas las filas
- Considerar usar separador de miles `%,` para números grandes

### Formateo de Fechas y Horas

`printf` tiene soporte para fechas y horas mediante especificadores que comienzan con `t` (minúscula) o `T` (mayúscula para versiones en mayúsculas). Requieren un objeto que represente fecha/hora como argumento.

| Especificador | Descripción | Ejemplo de Salida |
|:---:|:---|:---|
| `%tH` | Hora en formato 24h (00-23) | `14` |
| `%tI` | Hora en formato 12h (01-12) | `02` |
| `%tM` | Minutos (00-59) | `30` |
| `%tS` | Segundos (00-59) | `45` |
| `%tT` | Hora completa 24h (HH:MM:SS) | `14:30:45` |
| `%tr` | Hora completa 12h (hh:mm:ss AM/PM) | `02:30:45 PM` |
| `%td` | Día del mes (01-31) | `25` |
| `%tm` | Mes (01-12) | `03` |
| `%tY` | Año (4 dígitos) | `2026` |
| `%ty` | Año (2 dígitos) | `26` |
| `%tD` | Fecha formato USA (MM/DD/YY) | `03/25/26` |
| `%tF` | Fecha formato ISO (YYYY-MM-DD) | `2026-03-25` |
| `%tA` | Nombre del día (completo) | `Wednesday` |
| `%ta` | Nombre del día (abreviado) | `Wed` |
| `%tB` | Nombre del mes (completo) | `March` |
| `%tb` | Nombre del mes (abreviado) | `Mar` |

```{code} java
:caption: Ejemplos de formato de fecha/hora

// Requiere: import java.time.LocalDateTime;
LocalDateTime ahora = LocalDateTime.now();

System.out.printf("Hora: %tT%n", ahora);           // Hora: 14:30:45
System.out.printf("Fecha ISO: %tF%n", ahora);      // Fecha ISO: 2026-03-25
System.out.printf("Fecha USA: %tD%n", ahora);      // Fecha USA: 03/25/26

// Usando índices para reutilizar el mismo argumento
System.out.printf("Completo: %1$tF %1$tT%n", ahora);  // 2026-03-25 14:30:45
System.out.printf("Día: %1$tA %1$td de %1$tB%n", ahora); // Wednesday 25 de March
```

:::{note} Formateo de Fechas en Java Moderno
Aunque `printf` soporta fechas, en Java moderno es más común usar `DateTimeFormatter` de la API `java.time` para un control más fino y mejor internacionalización. Los especificadores de `printf` son útiles para salidas rápidas y simples.
:::

:::{tip} `String.format()` para Cadenas Formateadas
Si necesitás guardar el resultado formateado en una variable en lugar de imprimirlo directamente, usá `String.format()`. Tiene exactamente la misma sintaxis que `printf`:

```java
// En lugar de imprimir, guarda en una variable
String mensaje = String.format("El precio es $%.2f", 19.99);
String linea = String.format("%-20s %10.2f", "Producto", 99.99);

// Luego podés usar la cadena donde quieras
System.out.println(mensaje);
guardarEnArchivo(linea);
```

Esto es equivalente a `sprintf` en C.
:::

## Entrada de Datos con `Scanner`

En C, la entrada de datos se hace típicamente con `scanf`, que lee directamente de `stdin` parseando según especificadores de formato. Java no tiene un equivalente directo a `scanf` incorporado en el lenguaje; en su lugar, se usa la clase `Scanner`.

`Scanner` es más flexible que `scanf`: puede leer de múltiples fuentes (teclado, archivos, cadenas), tiene manejo de errores más robusto, y ofrece métodos específicos para cada tipo de dato. Sin embargo, tiene algunas peculiaridades que hay que entender para usarlo correctamente.

### Creación de un Scanner

Para leer del teclado, se crea un `Scanner` que envuelve a `System.in`:

```{code} java
:caption: Creación de un Scanner para leer del teclado

// Requiere agregar al inicio del archivo:
import java.util.Scanner;

// En el código:
Scanner scanner = new Scanner(System.in);
```

El `Scanner` actúa como intermediario: lee bytes de `System.in`, los convierte a texto, y ofrece métodos para extraer valores de diferentes tipos.

### Métodos de Lectura

`Scanner` ofrece un método `nextX()` para cada tipo de dato primitivo:

| Método | Tipo de Retorno | Lee... | Equivalente en C |
|:---|:---:|:---|:---:|
| `next()` | `String` | Siguiente token (hasta espacio/enter) | — |
| `nextLine()` | `String` | Toda la línea (hasta Enter) | `fgets()` |
| `nextInt()` | `int` | Un número entero | `scanf("%d", &x)` |
| `nextLong()` | `long` | Un entero largo | `scanf("%ld", &x)` |
| `nextDouble()` | `double` | Un número decimal | `scanf("%lf", &x)` |
| `nextFloat()` | `float` | Un decimal de precisión simple | `scanf("%f", &x)` |
| `nextBoolean()` | `boolean` | `true` o `false` | — |
| `nextByte()` | `byte` | Un byte | — |
| `nextShort()` | `short` | Un entero corto | `scanf("%hd", &x)` |

```{code} java
:caption: Ejemplos de lectura con Scanner

Scanner scanner = new Scanner(System.in);

System.out.print("Ingrese su nombre: ");
String nombre = scanner.nextLine();  // Lee toda la línea

System.out.print("Ingrese su edad: ");
int edad = scanner.nextInt();        // Lee un entero

System.out.print("Ingrese su altura en metros: ");
double altura = scanner.nextDouble(); // Lee un decimal

System.out.printf("Hola %s, tenés %d años y medís %.2f metros.%n", 
                  nombre, edad, altura);
```

### ¿Qué es un Token?

Un **token** es una secuencia de caracteres delimitada por espacios en blanco (espacios, tabulaciones, saltos de línea). `Scanner` trabaja internamente dividiendo la entrada en tokens.

Por ejemplo, si el usuario escribe `"Hola Mundo 42"` y presiona Enter, hay tres tokens: `"Hola"`, `"Mundo"`, y `"42"`.

### Diferencia entre `next()` y `nextLine()`

Esta diferencia es crucial y fuente de muchos errores:

- **`next()`**: Lee **un solo token** (hasta el próximo espacio en blanco). No consume el espacio ni el salto de línea que sigue.

- **`nextLine()`**: Lee **toda la línea** hasta el Enter, incluyendo espacios intermedios. **Sí consume el salto de línea** pero no lo incluye en el resultado.

```{code} java
:caption: Diferencia entre next() y nextLine()

Scanner scanner = new Scanner(System.in);

// El usuario escribe: "Juan Pérez" y presiona Enter
System.out.print("Ingrese nombre completo: ");

// Opción 1: usando next()
String palabra1 = scanner.next();     // palabra1 = "Juan"
String palabra2 = scanner.next();     // palabra2 = "Pérez"

// Opción 2: usando nextLine()
String lineaCompleta = scanner.nextLine();  // lineaCompleta = "Juan Pérez"
```

```{code} java
:caption: next() vs nextLine() con múltiples tokens

// Entrada del usuario: "abc def ghi" (en una línea)
String token1 = scanner.next();       // "abc"
String token2 = scanner.next();       // "def"
String resto = scanner.nextLine();    // " ghi" (nota el espacio al inicio)
```

### Verificación de Entrada con `hasNext`

Antes de leer, se puede verificar si hay datos disponibles y si son del tipo esperado. Esto permite evitar errores si el usuario ingresa algo inesperado:

| Método | Pregunta que responde |
|:---|:---|
| `hasNext()` | ¿Hay al menos un token disponible? |
| `hasNextLine()` | ¿Hay otra línea disponible? |
| `hasNextInt()` | ¿El siguiente token puede leerse como entero? |
| `hasNextDouble()` | ¿El siguiente token puede leerse como decimal? |
| `hasNextBoolean()` | ¿El siguiente token es `true` o `false`? |

```{code} java
:caption: Validación de entrada con hasNextInt()

System.out.print("Ingrese un número entero: ");

if (scanner.hasNextInt()) {
    int numero = scanner.nextInt();
    System.out.println("Leíste el número: " + numero);
} else {
    System.out.println("Error: no ingresaste un número válido");
    String invalido = scanner.next();  // Descartar la entrada inválida
    System.out.println("Ingresaste: " + invalido);
}
```

Este patrón es útil para validar entrada antes de procesarla, evitando excepciones por datos mal formados.

### El Problema del Salto de Línea Residual

Este es el error más común y confuso al usar `Scanner`. Ocurre cuando se mezclan métodos como `nextInt()` o `nextDouble()` con `nextLine()`.

**El problema:** Los métodos `nextInt()`, `nextDouble()`, etc., leen solo el **valor numérico** y dejan el **carácter de salto de línea (`\n`)** en el búfer. Si después se llama a `nextLine()`, este encuentra el `\n` inmediatamente y retorna una cadena vacía.

```{code} java
:caption: El problema del salto de línea residual

Scanner scanner = new Scanner(System.in);

System.out.print("Ingrese su edad: ");
int edad = scanner.nextInt();
// El usuario escribe "25" y presiona Enter
// nextInt() lee "25", pero el '\n' queda en el búfer

System.out.print("Ingrese su nombre: ");
String nombre = scanner.nextLine();
// nextLine() encuentra '\n' inmediatamente → retorna ""

System.out.println("Nombre: [" + nombre + "]");  // Imprime: Nombre: []
System.out.println("¡El nombre está vacío!");
```

**Visualización del búfer:**

```
Entrada del usuario: "25\nJuan\n"
                     ↑
Después de nextInt():  Búfer contiene "\nJuan\n"
                       ↑ cursor aquí
Después de nextLine(): Búfer contiene "Juan\n"
                       ↑ cursor aquí (leyó "" antes del primer \n)
```

:::{important} Regla de Oro del Scanner
Si vas a leer una cadena con `nextLine()` después de haber usado `nextInt()`, `nextDouble()`, `next()`, o similar, debés llamar a `nextLine()` una vez extra para "limpiar" el salto de línea residual del búfer.
:::

```{code} java
:caption: Solución al problema del salto de línea

Scanner scanner = new Scanner(System.in);

System.out.print("Ingrese su edad: ");
int edad = scanner.nextInt();
scanner.nextLine();  // ← IMPORTANTE: Limpia el '\n' residual

System.out.print("Ingrese su nombre: ");
String nombre = scanner.nextLine();  // Ahora lee correctamente

System.out.println("Hola " + nombre + ", tenés " + edad + " años.");
```

Este problema no existe en C con `scanf` porque `scanf("%d", &x)` también deja el `\n`, pero `scanf("%s", s)` lo ignora automáticamente. Sin embargo, si en C usás `fgets()` después de `scanf()`, tendrás el mismo problema.

### Patrón Alternativo: Leer Todo como Línea

Una forma de evitar completamente el problema del salto de línea residual es **siempre** usar `nextLine()` y convertir manualmente cuando se necesite un número:

```{code} java
:caption: Leer todo como línea y convertir

Scanner scanner = new Scanner(System.in);

System.out.print("Ingrese su edad: ");
String lineaEdad = scanner.nextLine();
int edad = Integer.parseInt(lineaEdad);  // Convierte String a int

System.out.print("Ingrese su nombre: ");
String nombre = scanner.nextLine();  // No hay problema de búfer

System.out.print("Ingrese su altura: ");
String lineaAltura = scanner.nextLine();
double altura = Double.parseDouble(lineaAltura);  // Convierte String a double

System.out.printf("%s tiene %d años y mide %.2f metros.%n", nombre, edad, altura);
```

**Métodos de conversión disponibles:**
- `Integer.parseInt(String)` → `int`
- `Long.parseLong(String)` → `long`
- `Double.parseDouble(String)` → `double`
- `Float.parseFloat(String)` → `float`
- `Boolean.parseBoolean(String)` → `boolean`

:::{note} Ventajas y Desventajas
**Ventajas:**
- Evita completamente el problema del salto de línea
- Es consistente: siempre se usa el mismo método

**Desventajas:**
- Si el usuario ingresa texto donde debía ir un número, se lanza una excepción (`NumberFormatException`)
- Requiere importar conocimiento sobre manejo de excepciones (se verá más adelante)

Para programas simples donde se confía en que el usuario ingresa datos correctos, este patrón es muy conveniente.
:::

### Localización y Separador Decimal

`Scanner` es sensible a la **configuración regional** (_Locale_) del sistema. En países hispanohablantes, el separador decimal suele ser la **coma** (`,`), mientras que en el estándar de programación y en países anglosajones se usa el **punto** (`.`).

Si el sistema está configurado en español y el usuario escribe `3.14`, `nextDouble()` puede fallar o interpretar incorrectamente el valor. Para forzar el uso del punto como separador decimal (lo recomendado en programación):

```{code} java
:caption: Configurar Scanner para usar punto decimal

import java.util.Scanner;
import java.util.Locale;

Scanner scanner = new Scanner(System.in);
scanner.useLocale(Locale.US);  // Fuerza el punto como separador decimal

System.out.print("Ingrese un decimal (con punto): ");
double valor = scanner.nextDouble();  // Ahora acepta 3.14
System.out.printf("Leíste: %.4f%n", valor);
```

:::{tip} Configuración Recomendada
Al inicio del programa, inmediatamente después de crear el `Scanner`, configurarlo con `Locale.US` evita problemas de compatibilidad:

```java
Scanner scanner = new Scanner(System.in);
scanner.useLocale(Locale.US);
```

Esto asegura que el programa funcione igual sin importar la configuración regional del sistema donde se ejecute.
:::

En C, `scanf` también es sensible al locale, pero es menos común encontrar el problema porque la configuración por defecto suele usar punto.

### Cierre del Scanner

`Scanner` utiliza recursos del sistema (conexión al stream de entrada). Es buena práctica cerrarlo cuando ya no se necesita:

```{code} java
:caption: Cerrar el Scanner

Scanner scanner = new Scanner(System.in);

// ... usar el scanner para leer datos ...

scanner.close();  // Libera recursos
```

:::{warning} Cuidado al Cerrar Scanner de System.in
Si cerrás un `Scanner` que lee de `System.in`, **también cerrarás `System.in`**. Esto significa que no podrás volver a leer del teclado en tu programa, ni siquiera creando un nuevo `Scanner`.

```java
Scanner sc1 = new Scanner(System.in);
int x = sc1.nextInt();
sc1.close();  // Cierra System.in

Scanner sc2 = new Scanner(System.in);  // System.in ya está cerrado
int y = sc2.nextInt();  // ¡Error! No se puede leer
```

**Recomendación práctica:** En programas simples donde se lee del teclado durante toda la ejecución, es común **no cerrar** el `Scanner` de `System.in`. El sistema operativo liberará los recursos cuando el programa termine.

En programas más complejos o cuando se lee de archivos, el cierre es más importante y se maneja típicamente con `try-with-resources` (se verá más adelante).
:::

### Lectura de Datos en un Lazo

Un patrón común es leer datos repetidamente hasta que el usuario indique que quiere terminar. Hay varias formas de hacerlo:

```{code} java
:caption: Lectura en lazo con valor centinela

Scanner scanner = new Scanner(System.in);
scanner.useLocale(Locale.US);

int suma = 0;
int contador = 0;

System.out.println("Ingrese números (0 para terminar):");

int numero = scanner.nextInt();
while (numero != 0) {
    suma = suma + numero;
    contador = contador + 1;
    numero = scanner.nextInt();
}

if (contador > 0) {
    double promedio = (double) suma / contador;
    System.out.printf("Promedio de %d números: %.2f%n", contador, promedio);
} else {
    System.out.println("No se ingresaron números.");
}
```

Otra opción es usar `hasNext()` para detectar cuándo no hay más entrada (útil cuando los datos vienen de un archivo):

```{code} java
:caption: Lectura hasta fin de entrada

Scanner scanner = new Scanner(System.in);

// hasNextInt() retorna false cuando no hay más entrada
while (scanner.hasNextInt()) {
    int numero = scanner.nextInt();
    System.out.println("Leíste: " + numero);
}

System.out.println("Fin de la entrada");
```

Este segundo patrón es especialmente útil cuando la entrada viene de un archivo mediante **redirección** en la línea de comandos:

```bash
java MiPrograma < datos.txt
```

En C, el equivalente sería `while (scanf("%d", &x) == 1)` para detectar el fin de archivo.

## Comparación de Métodos de Salida

Esta tabla resume cuándo usar cada método de impresión:

| Método | Salto de Línea | Permite Formato | Uso Típico |
|:---|:---:|:---:|:---|
| `print()` | No | No | Mensajes parciales, prompts |
| `println()` | Sí (automático) | No | Mensajes simples, depuración |
| `printf()` | No (usar `%n`) | Sí | Tablas, reportes, formato preciso |

```{code} java
:caption: Comparación práctica de los tres métodos

double precio = 19.99;
String producto = "Manzanas";

// print(): para construir líneas en partes
System.out.print("Producto: ");
System.out.print(producto);
System.out.print(" - ");
System.out.println(precio);  // println al final para el salto

// println(): para mensajes simples
System.out.println("Precio: " + precio);
System.out.println(producto + " cuesta $" + precio);

// printf(): para control preciso del formato
System.out.printf("%-15s $%8.2f%n", producto, precio);
```

**Recomendación general:**
- Usar `println()` para mensajes de depuración y salidas simples
- Usar `printf()` cuando se necesite alinear columnas o controlar decimales
- Usar `print()` solo cuando se necesite continuar en la misma línea

## Secuencias de Escape

Las **secuencias de escape** permiten incluir caracteres especiales dentro de las cadenas de texto. Comienzan con una barra invertida (`\`) seguida de un carácter que indica qué carácter especial representar.

| Secuencia | Significado | Ejemplo |
|:---:|:---|:---|
| `\n` | Salto de línea (new line) | `"Línea 1\nLínea 2"` |
| `\t` | Tabulación horizontal | `"Col1\tCol2"` |
| `\\` | Barra invertida literal | `"C:\\Users"` |
| `\"` | Comilla doble literal | `"Él dijo \"Hola\""` |
| `\'` | Comilla simple literal | `'It\'s'` (en char) |
| `\r` | Retorno de carro | Usado en Windows |
| `\b` | Retroceso (backspace) | Borra un carácter |

```{code} java
:caption: Uso de secuencias de escape

// Salto de línea dentro de una cadena
System.out.println("Primera línea\nSegunda línea");
// Resultado:
// Primera línea
// Segunda línea

// Tabulaciones para alinear
System.out.println("Nombre\tEdad\tCiudad");
System.out.println("Ana\t25\tNeuquén");
System.out.println("Carlos\t30\tRoca");

// Barras invertidas (necesarias para rutas en Windows)
System.out.println("Ruta: C:\\Users\\Juan\\Documentos");
// Resultado: Ruta: C:\Users\Juan\Documentos

// Comillas dentro de cadenas
System.out.println("Él dijo: \"Hola, ¿cómo estás?\"");
// Resultado: Él dijo: "Hola, ¿cómo estás?"
```

Estas secuencias son idénticas a las de C. La razón por la que `\\` representa una sola barra es que la barra invertida es el carácter de escape, así que para incluirla literalmente hay que "escaparla" con otra barra.

:::{note} Text Blocks en Java Moderno
Desde Java 15, existen los **text blocks** que permiten escribir cadenas multilínea sin escapar las comillas ni los saltos de línea:

```java
String json = """
    {
        "nombre": "Ana",
        "edad": 25
    }
    """;
```

Esto es más cómodo para textos largos, pero por ahora las secuencias de escape son la forma estándar.
:::

## Temas Avanzados (Referencia)

Esta sección menciona brevemente temas que se profundizarán más adelante o que son útiles como referencia.

### `System.console()` - Interacción con Terminal

Para aplicaciones que se ejecutan en una terminal real (no en un IDE), Java ofrece `System.console()`:

```{code} java
:caption: Uso básico de Console

Console console = System.console();
if (console != null) {
    String nombre = console.readLine("Ingrese su nombre: ");
    char[] password = console.readPassword("Ingrese su contraseña: ");
    // password no se muestra mientras se escribe
}
```

**Ventajas:**
- `readPassword()` oculta los caracteres mientras el usuario escribe
- Retorna `char[]` en lugar de `String` para poder borrar la contraseña de memoria

**Desventaja:**
- Retorna `null` si el programa corre en un IDE o con redirección de entrada

### Rendimiento: `Scanner` vs. `BufferedReader`

Para la mayoría de los programas, `Scanner` es suficiente. Sin embargo, tiene un costo de rendimiento:

- **`Scanner`**: Conveniente pero **relativamente lento**. Usa expresiones regulares internamente para parsear la entrada.

- **`BufferedReader`**: Mucho más rápido. Lee bloques grandes de texto de una vez. Requiere convertir números manualmente con `Integer.parseInt()`.

En concursos de programación o procesamiento de archivos grandes (millones de líneas), `Scanner` puede ser el cuello de botella. Para la mayoría de las tareas académicas y profesionales, la diferencia es imperceptible.

## Ejercicios

Los siguientes ejercicios permiten practicar los conceptos de entrada y salida. Se recomienda resolverlos antes de ver las soluciones.

````{exercise}
:label: ej-scanner-logic

Dada la siguiente entrada del usuario:
```
42
Juan
```

¿Qué sucede si ejecutamos este código?

```java
Scanner scanner = new Scanner(System.in);
int edad = scanner.nextInt();
String nombre = scanner.nextLine();
System.out.println("Nombre: [" + nombre + "]");
System.out.println("Edad: " + edad);
```

¿Cuál es el problema y cómo se soluciona?
````

```{solution} ej-scanner-logic
:class: dropdown

El problema es que `nextInt()` lee `42` pero deja el `\n` (salto de línea) en el búfer. Cuando se ejecuta `nextLine()`, encuentra inmediatamente el `\n`, asume que la línea terminó, y devuelve una cadena vacía.

**Salida del programa con el bug:**
```
Nombre: []
Edad: 42
```

**Solución:** Agregar una llamada a `nextLine()` después de `nextInt()` para limpiar el salto de línea residual:

```java
Scanner scanner = new Scanner(System.in);
int edad = scanner.nextInt();
scanner.nextLine();  // ← Limpia el \n residual
String nombre = scanner.nextLine();
System.out.println("Nombre: [" + nombre + "]");
System.out.println("Edad: " + edad);
```

**Salida corregida:**
```
Nombre: [Juan]
Edad: 42
```
```

````{exercise}
:label: ej-printf-formato

Escribí una sentencia `printf` que muestre el número `3.14159` con:
- Exactamente 3 decimales
- Alineado a la derecha en un campo de 10 caracteres
- Con el signo siempre visible (+ para positivos)

¿Cuál es el resultado exacto?
````

```{solution} ej-printf-formato
:class: dropdown

```java
double numero = 3.14159;
System.out.printf("[%+10.3f]%n", numero);
```

**Resultado:** `[    +3.142]`

Explicación de los especificadores:
- `+` → muestra siempre el signo (+ para positivos, - para negativos)
- `10` → ancho mínimo de 10 caracteres
- `.3` → precisión de 3 decimales (el valor se redondea)
- `f` → formato de punto flotante

El número formateado es `+3.142` (6 caracteres), y como el ancho es 10, se agregan 4 espacios a la izquierda.
```

````{exercise}
:label: ej-tabla-printf

Usando `printf`, mostrá una tabla de conversión de temperaturas de Celsius a Fahrenheit para los valores 0, 10, 20, 30 y 40 grados. La fórmula es: F = C × 9/5 + 32

Las columnas deben estar alineadas y los valores Fahrenheit con 1 decimal.
````

```{solution} ej-tabla-printf
:class: dropdown

```java
System.out.printf("%-10s %12s%n", "Celsius", "Fahrenheit");
System.out.printf("%-10s %12s%n", "----------", "------------");

int[] celsius = {0, 10, 20, 30, 40};
for (int c : celsius) {
    double f = c * 9.0 / 5.0 + 32;
    System.out.printf("%-10d %12.1f%n", c, f);
}
```

**Resultado:**
```
Celsius    Fahrenheit
---------- ------------
0                 32.0
10                50.0
20                68.0
30                86.0
40               104.0
```

Explicación:
- `%-10s` y `%-10d`: alinean a la izquierda en 10 caracteres
- `%12.1f`: alinea a la derecha en 12 caracteres con 1 decimal
```

````{exercise}
:label: ej-lectura-validada

Escribí un programa que pida al usuario un número entero positivo. Si el usuario ingresa algo que no es un entero o es negativo, debe mostrar un mensaje de error y volver a pedir el número. Use `hasNextInt()` para la validación.
````

```{solution} ej-lectura-validada
:class: dropdown

```java
import java.util.Scanner;

public class LecturaValidada {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numero = -1;
        
        while (numero < 0) {
            System.out.print("Ingrese un número entero positivo: ");
            
            if (scanner.hasNextInt()) {
                numero = scanner.nextInt();
                if (numero < 0) {
                    System.out.println("Error: el número debe ser positivo.");
                }
            } else {
                System.out.println("Error: debe ingresar un número entero.");
                scanner.next();  // Descartar la entrada inválida
            }
        }
        
        System.out.println("Número válido: " + numero);
    }
}
```

**Ejemplo de ejecución:**
```
Ingrese un número entero positivo: hola
Error: debe ingresar un número entero.
Ingrese un número entero positivo: -5
Error: el número debe ser positivo.
Ingrese un número entero positivo: 42
Número válido: 42
```

La clave es usar `hasNextInt()` para verificar si lo que sigue es un entero, y `scanner.next()` para descartar la entrada inválida cuando no lo es.
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
