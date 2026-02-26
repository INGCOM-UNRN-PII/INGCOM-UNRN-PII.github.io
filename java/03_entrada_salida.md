---
title: "Entrada y Salida de Datos"
description: Guía sobre lectura de datos, salida por consola y formateo en Java.
---

# Entrada y Salida de Datos

La interacción con el usuario a través de la consola es fundamental para aplicaciones de línea de comandos. Java proporciona varias herramientas para leer datos del usuario y mostrar resultados formateados.

## Salida de Datos

### System.out

La clase `System` proporciona streams estándar para la salida:

```{code} java
:caption: Métodos básicos de salida

// Imprime sin salto de línea
System.out.print("Hola");
System.out.print(" Mundo");  // Salida: "Hola Mundo"

// Imprime con salto de línea al final
System.out.println("Primera línea");
System.out.println("Segunda línea");

// Imprime con formato (como printf en C)
System.out.printf("Nombre: %s, Edad: %d%n", "Juan", 25);
```

### Formateo con printf

El método `printf` permite formatear la salida usando especificadores de formato:

:::{table} Especificadores de formato comunes
:label: tbl-especificadores

| Especificador | Tipo | Ejemplo | Resultado |
| :------------ | :--- | :------ | :-------- |
| `%d` | Entero decimal | `printf("%d", 42)` | `42` |
| `%f` | Punto flotante | `printf("%f", 3.14159)` | `3.141590` |
| `%s` | Cadena | `printf("%s", "Hola")` | `Hola` |
| `%c` | Carácter | `printf("%c", 'A')` | `A` |
| `%b` | Booleano | `printf("%b", true)` | `true` |
| `%n` | Salto de línea | `printf("Hola%n")` | `Hola\n` |
| `%x` | Hexadecimal | `printf("%x", 255)` | `ff` |
| `%o` | Octal | `printf("%o", 8)` | `10` |
| `%%` | Símbolo % | `printf("100%%")` | `100%` |

:::

### Modificadores de Formato

Los especificadores pueden incluir modificadores para controlar el ancho, precisión y alineación:

```{code} java
:caption: Modificadores de formato

// Ancho mínimo (rellena con espacios a la izquierda)
System.out.printf("[%10d]%n", 42);      // [        42]

// Alineación a la izquierda
System.out.printf("[%-10d]%n", 42);     // [42        ]

// Relleno con ceros
System.out.printf("[%010d]%n", 42);     // [0000000042]

// Precisión para decimales
System.out.printf("[%.2f]%n", 3.14159); // [3.14]

// Ancho y precisión combinados
System.out.printf("[%10.2f]%n", 3.14159); // [      3.14]

// Separador de miles
System.out.printf("[%,d]%n", 1000000);  // [1,000,000]

// Signo positivo explícito
System.out.printf("[%+d]%n", 42);       // [+42]
```

### String.format

Para crear cadenas formateadas sin imprimirlas inmediatamente:

```{code} java
:caption: Uso de String.format

String mensaje = String.format("Usuario: %s, Puntos: %,d", "Ana", 15000);
// mensaje = "Usuario: Ana, Puntos: 15,000"

String coordenada = String.format("(%.2f, %.2f)", 3.14159, 2.71828);
// coordenada = "(3.14, 2.72)"
```

:::{tip}
Usá `%n` en lugar de `\n` para el salto de línea. `%n` genera el separador de línea correcto según el sistema operativo (CRLF en Windows, LF en Unix/Linux).
:::

## Atajos en IntelliJ IDEA

IntelliJ proporciona abreviaturas (*live templates*) para escribir código más rápido:

:::{table} Atajos de escritura en IntelliJ
:label: tbl-atajos-intellij

| Atajo | Expansión |
| :---- | :-------- |
| `sout` | `System.out.println();` |
| `souf` | `System.out.printf("");` |
| `soutv` | `System.out.println("variable = " + variable);` |
| `soutm` | `System.out.println("NombreClase.nombreMetodo");` |

:::

## Entrada de Datos con Scanner

La clase `Scanner` permite leer datos desde diversas fuentes, incluyendo la entrada estándar.

### Configuración Básica

```{code} java
:caption: Creación de un Scanner

import java.util.Scanner;

public class LecturaBasica {
    public static void main(String[] args) {
        Scanner lector = new Scanner(System.in);
        
        System.out.print("Ingresá tu nombre: ");
        String nombre = lector.nextLine();
        
        System.out.print("Ingresá tu edad: ");
        int edad = lector.nextInt();
        
        System.out.printf("Hola %s, tenés %d años.%n", nombre, edad);
        
        // No cerrar el Scanner si usa System.in y se sigue leyendo después
    }
}
```

### Métodos de Lectura

:::{table} Métodos de lectura de Scanner
:label: tbl-scanner-metodos

| Método | Lee | Ejemplo |
| :----- | :-- | :------ |
| `next()` | Siguiente token (hasta espacio) | `"Hola"` |
| `nextLine()` | Línea completa (hasta Enter) | `"Hola Mundo"` |
| `nextInt()` | Entero | `42` |
| `nextLong()` | Entero largo | `123456789L` |
| `nextFloat()` | Decimal float | `3.14f` |
| `nextDouble()` | Decimal double | `3.14159` |
| `nextBoolean()` | Booleano | `true` |
| `nextByte()` | Byte | `127` |
| `nextShort()` | Short | `1000` |

:::

### El Problema del Buffer

Un problema común ocurre al mezclar `nextLine()` con otros métodos:

```{code} java
:caption: Problema del buffer con nextLine

Scanner scanner = new Scanner(System.in);

System.out.print("Ingresá un número: ");
int numero = scanner.nextInt();  // Lee el número, deja '\n' en el buffer

System.out.print("Ingresá tu nombre: ");
String nombre = scanner.nextLine();  // Lee el '\n' restante, no espera!

// nombre queda vacío
```

**Solución**: Consumir el salto de línea restante:

```{code} java
:caption: Solución al problema del buffer

Scanner scanner = new Scanner(System.in);

System.out.print("Ingresá un número: ");
int numero = scanner.nextInt();
scanner.nextLine();  // Consume el '\n' restante

System.out.print("Ingresá tu nombre: ");
String nombre = scanner.nextLine();  // Ahora funciona correctamente
```

### Verificación de Entrada

Scanner permite verificar si hay datos disponibles antes de leerlos:

```{code} java
:caption: Métodos de verificación

Scanner scanner = new Scanner(System.in);

System.out.print("Ingresá un número: ");

if (scanner.hasNextInt()) {
    int numero = scanner.nextInt();
    System.out.println("Leído: " + numero);
} else {
    System.out.println("Eso no es un número válido");
    scanner.next();  // Descarta la entrada inválida
}
```

### Lectura de Archivos

Scanner también puede leer desde archivos:

```{code} java
:caption: Lectura de archivo con Scanner

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public void leerArchivo(String ruta) {
    try {
        Scanner scanner = new Scanner(new File(ruta));
        
        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine();
            System.out.println(linea);
        }
        
        scanner.close();  // Sí cerrar cuando se lee de archivo
        
    } catch (FileNotFoundException e) {
        System.out.println("Archivo no encontrado: " + ruta);
    }
}
```

:::{warning}
**Sobre cerrar el Scanner**:
- **No cerrar** si usa `System.in` y vas a seguir leyendo datos más adelante (cerrar el Scanner cierra también `System.in`).
- **Sí cerrar** si usa archivos u otras fuentes, preferentemente con *try-with-resources*.
:::

### Try-with-resources

Para manejo automático de recursos:

```{code} java
:caption: Try-with-resources para Scanner

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public void leerArchivoSeguro(String ruta) {
    try (Scanner scanner = new Scanner(new File(ruta))) {
        while (scanner.hasNextLine()) {
            String linea = scanner.nextLine();
            System.out.println(linea);
        }
        // El scanner se cierra automáticamente
    } catch (FileNotFoundException e) {
        System.out.println("Archivo no encontrado: " + ruta);
    }
}
```

## Métodos Útiles de String

La clase `String` proporciona numerosos métodos para manipular texto:

### Información Básica

```{code} java
:caption: Métodos de información de String

String texto = "Hola Mundo Java";

int longitud = texto.length();           // 15
char caracter = texto.charAt(0);         // 'H'
boolean vacio = texto.isEmpty();         // false
boolean enBlanco = texto.isBlank();      // false (Java 11+)
```

### Búsqueda

```{code} java
:caption: Métodos de búsqueda en String

String texto = "programación java";

boolean contiene = texto.contains("java");        // true
boolean empieza = texto.startsWith("prog");       // true
boolean termina = texto.endsWith("java");         // true

int indice = texto.indexOf('a');                  // 5 (primera 'a')
int indiceDesde = texto.indexOf('a', 6);          // 9 (primera 'a' desde índice 6)
int ultimoIndice = texto.lastIndexOf('a');        // 17 (última 'a')

// indexOf retorna -1 si no encuentra
int noEncontrado = texto.indexOf('z');            // -1
```

### Transformación

```{code} java
:caption: Métodos de transformación de String

String texto = "  Hola Mundo  ";

String mayusculas = texto.toUpperCase();          // "  HOLA MUNDO  "
String minusculas = texto.toLowerCase();          // "  hola mundo  "
String sinEspacios = texto.trim();                // "Hola Mundo"
String strip = texto.strip();                     // "Hola Mundo" (Java 11+)

String reemplazo = texto.replace("Mundo", "Java"); // "  Hola Java  "
```

### Extracción

```{code} java
:caption: Métodos de extracción de String

String texto = "uno,dos,tres,cuatro";

String subcadena = texto.substring(4);            // "dos,tres,cuatro"
String rango = texto.substring(4, 7);             // "dos"

String[] partes = texto.split(",");               // ["uno", "dos", "tres", "cuatro"]
char[] caracteres = texto.toCharArray();          // ['u','n','o',',',...}
```

### Unión

```{code} java
:caption: Unión de cadenas

String[] palabras = {"uno", "dos", "tres"};

String unidos = String.join("-", palabras);       // "uno-dos-tres"
String unidosLista = String.join(", ", "a", "b", "c");  // "a, b, c"
```

:::{important}
**Los String son inmutables**: cada operación de transformación crea un **nuevo** String. El original no se modifica:

```java
String original = "hola";
String mayus = original.toUpperCase();

System.out.println(original);  // "hola" (no cambió)
System.out.println(mayus);     // "HOLA" (nuevo String)
```
:::

### Comparación

```{code} java
:caption: Comparación de Strings

String a = "Hola";
String b = "hola";
String c = "Hola";

// Comparación de contenido
boolean igual = a.equals(c);                      // true
boolean igualIgnorando = a.equalsIgnoreCase(b);   // true

// Comparación lexicográfica
int comparacion = a.compareTo(b);                 // Negativo (H < h en ASCII)
int compIgn = a.compareToIgnoreCase(b);           // 0 (iguales ignorando caso)
```

:::{warning}
**Nunca uses `==` para comparar contenido de Strings**. El operador `==` compara referencias (direcciones de memoria), no contenido:

```java
String a = new String("hola");
String b = new String("hola");

System.out.println(a == b);        // false (diferentes objetos)
System.out.println(a.equals(b));   // true (mismo contenido)
```
:::

## Ejercicios

```{exercise}
:label: ej-io-1

Escribí un programa que lea el nombre, edad y altura de una persona, y muestre los datos formateados así:
```
Nombre: Juan Pérez
Edad: 25 años
Altura: 1.75 m
```
```

```{solution} ej-io-1
```java
import java.util.Scanner;

public class DatosPersona {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Ingresá tu nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Ingresá tu edad: ");
        int edad = scanner.nextInt();
        
        System.out.print("Ingresá tu altura (en metros): ");
        double altura = scanner.nextDouble();
        
        System.out.printf("%nNombre: %s%n", nombre);
        System.out.printf("Edad: %d años%n", edad);
        System.out.printf("Altura: %.2f m%n", altura);
    }
}
```
```

```{exercise}
:label: ej-io-2

Implementá un método `contarPalabras(String texto)` que cuente la cantidad de palabras en una cadena, considerando que las palabras están separadas por uno o más espacios.
```

```{solution} ej-io-2
```java
public int contarPalabras(String texto) {
    if (texto == null || texto.isBlank()) {
        return 0;
    }
    
    // split("\\s+") divide por uno o más espacios
    String[] palabras = texto.trim().split("\\s+");
    return palabras.length;
}
```
```

:::{seealso}
- [Documentación de Scanner](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Scanner.html)
- [Documentación de String](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/String.html)
- [Formatter - Especificadores de formato](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Formatter.html)
:::
