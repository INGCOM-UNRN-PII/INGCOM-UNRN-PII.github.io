---
title: "Strings y StringBuilder"
description: Guía sobre manipulación eficiente de cadenas de texto en Java.
---

# Strings y StringBuilder

Las cadenas de texto son uno de los tipos de datos más utilizados en cualquier aplicación. Java proporciona la clase `String` para cadenas inmutables y `StringBuilder` para manipulación eficiente de cadenas mutables.

## La Clase String

### Inmutabilidad de String

`String` es **inmutable**: una vez creado, su contenido no puede modificarse. Cada operación que "modifica" un String en realidad crea un **nuevo** objeto:

```{code} java
:caption: Inmutabilidad de String

String original = "hola";
String modificado = original.toUpperCase();

System.out.println(original);    // "hola" (no cambió)
System.out.println(modificado);  // "HOLA" (nuevo String)

// Incluso la concatenación crea nuevos objetos
String a = "Java";
String b = a + " es genial";  // Nuevo String, 'a' sigue siendo "Java"
```

### Pool de Strings

Java mantiene un **pool** de Strings para optimizar memoria. Los literales idénticos comparten la misma instancia:

```{code} java
:caption: String pool

String s1 = "hola";
String s2 = "hola";
String s3 = new String("hola");

System.out.println(s1 == s2);  // true (misma referencia en el pool)
System.out.println(s1 == s3);  // false (s3 está fuera del pool)
System.out.println(s1.equals(s3));  // true (mismo contenido)

// Forzar uso del pool
String s4 = s3.intern();
System.out.println(s1 == s4);  // true
```

### Métodos Principales de String

#### Información

```{code} java
:caption: Métodos de información

String texto = "Programación Java";

int longitud = texto.length();           // 17
char caracter = texto.charAt(0);         // 'P'
boolean vacio = texto.isEmpty();         // false
boolean blanco = texto.isBlank();        // false (Java 11+)
```

#### Búsqueda

```{code} java
:caption: Métodos de búsqueda

String texto = "programación java avanzada";

// Buscar caracteres
int indice = texto.indexOf('a');              // 5 (primera 'a')
int indiceDesde = texto.indexOf('a', 10);     // 18 (primera 'a' desde índice 10)
int ultimo = texto.lastIndexOf('a');          // 25 (última 'a')

// Buscar subcadenas
int posJava = texto.indexOf("java");          // 14
int noEncontrado = texto.indexOf("python");   // -1

// Verificaciones
boolean contiene = texto.contains("java");    // true
boolean empieza = texto.startsWith("prog");   // true
boolean termina = texto.endsWith("ada");      // true
```

#### Comparación

```{code} java
:caption: Métodos de comparación

String a = "Hola";
String b = "hola";
String c = "Hola";

// Igualdad de contenido
boolean igual = a.equals(c);                  // true
boolean igualIgnore = a.equalsIgnoreCase(b);  // true

// Comparación lexicográfica
int comp = a.compareTo(b);                    // Negativo ('H' < 'h')
int compIgnore = a.compareToIgnoreCase(b);    // 0 (iguales)

// NUNCA usar == para comparar contenido
System.out.println(a == c);  // Puede ser true o false (depende del pool)
```

:::{warning}
**Siempre usá `.equals()` para comparar contenido de Strings.**

`==` compara **referencias** (direcciones de memoria), no contenido. Puede dar resultados inconsistentes:

```java
String a = "hola";
String b = "hola";
String c = new String("hola");

a == b;  // true (pool)
a == c;  // false (c está fuera del pool)
```
:::

#### Transformación

```{code} java
:caption: Métodos de transformación

String texto = "  Hola Mundo  ";

// Cambio de caso
String mayus = texto.toUpperCase();           // "  HOLA MUNDO  "
String minus = texto.toLowerCase();           // "  hola mundo  "

// Eliminar espacios
String sinEspacios = texto.trim();            // "Hola Mundo"
String strip = texto.strip();                 // "Hola Mundo" (Java 11+)
String stripL = texto.stripLeading();         // "Hola Mundo  "
String stripT = texto.stripTrailing();        // "  Hola Mundo"

// Reemplazo
String reemplazo = texto.replace("Mundo", "Java");  // "  Hola Java  "
String reemplazarTodos = "a-b-c".replace("-", "_"); // "a_b_c"

// Reemplazo con regex
String sinDigitos = "abc123def".replaceAll("\\d", ""); // "abcdef"
```

#### Extracción

```{code} java
:caption: Métodos de extracción

String texto = "uno,dos,tres,cuatro";

// Subcadenas
String desde = texto.substring(4);            // "dos,tres,cuatro"
String rango = texto.substring(4, 7);         // "dos"

// División
String[] partes = texto.split(",");           // ["uno", "dos", "tres", "cuatro"]
String[] limitado = texto.split(",", 2);      // ["uno", "dos,tres,cuatro"]

// Convertir a caracteres
char[] chars = texto.toCharArray();
```

#### Unión

```{code} java
:caption: Métodos de unión

String[] palabras = {"uno", "dos", "tres"};

// Unir con delimitador
String unido = String.join("-", palabras);    // "uno-dos-tres"
String unido2 = String.join(", ", "a", "b", "c");  // "a, b, c"
```

### Formateo de Strings

```{code} java
:caption: Formateo con String.format

String nombre = "Ana";
int edad = 25;
double altura = 1.68;

String info = String.format("Nombre: %s, Edad: %d, Altura: %.2f m", 
                           nombre, edad, altura);
// "Nombre: Ana, Edad: 25, Altura: 1.68 m"

// También con printf para salida directa
System.out.printf("Usuario: %s (%d años)%n", nombre, edad);
```

## StringBuilder

### Por Qué Usar StringBuilder

La concatenación de Strings en un lazo es **ineficiente** porque crea un nuevo objeto en cada operación:

```{code} java
:caption: Problema de concatenación en lazos

// ✗ INEFICIENTE: crea muchos objetos intermedios
String resultado = "";
for (int i = 0; i < 1000; i++) {
    resultado = resultado + i + ", ";  // Nuevo String cada iteración
}

// ✓ EFICIENTE: usa StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i).append(", ");
}
String resultado = sb.toString();
```

### Creación de StringBuilder

```{code} java
:caption: Constructores de StringBuilder

// Vacío con capacidad por defecto (16)
StringBuilder sb1 = new StringBuilder();

// Con capacidad inicial específica
StringBuilder sb2 = new StringBuilder(100);

// Con contenido inicial
StringBuilder sb3 = new StringBuilder("Hola");
```

### Métodos Principales

#### Agregar (append)

```{code} java
:caption: Método append

StringBuilder sb = new StringBuilder();

sb.append("Hola");
sb.append(' ');
sb.append("Mundo");
sb.append(42);
sb.append(true);

System.out.println(sb);  // "Hola Mundo42true"

// Encadenamiento (append retorna this)
sb.append("A").append("B").append("C");
```

#### Insertar (insert)

```{code} java
:caption: Método insert

StringBuilder sb = new StringBuilder("Hola Mundo");

sb.insert(5, "Querido ");  // Inserta en posición 5
System.out.println(sb);    // "Hola Querido Mundo"

sb.insert(0, "¡");
System.out.println(sb);    // "¡Hola Querido Mundo"
```

#### Eliminar (delete)

```{code} java
:caption: Métodos delete y deleteCharAt

StringBuilder sb = new StringBuilder("Hola Mundo Cruel");

sb.delete(5, 11);          // Elimina índices 5 a 10
System.out.println(sb);    // "Hola Cruel"

sb.deleteCharAt(4);        // Elimina el espacio
System.out.println(sb);    // "HolaCruel"
```

#### Reemplazar (replace)

```{code} java
:caption: Método replace

StringBuilder sb = new StringBuilder("Hola Mundo");

sb.replace(5, 10, "Java");  // Reemplaza "Mundo" por "Java"
System.out.println(sb);     // "Hola Java"
```

#### Invertir (reverse)

```{code} java
:caption: Método reverse

StringBuilder sb = new StringBuilder("Hola");

sb.reverse();
System.out.println(sb);  // "aloH"
```

#### Información

```{code} java
:caption: Métodos de información

StringBuilder sb = new StringBuilder("Hola");

int longitud = sb.length();      // 4
int capacidad = sb.capacity();   // 20 (capacidad interna)
char c = sb.charAt(0);           // 'H'

// Modificar carácter individual
sb.setCharAt(0, 'h');
System.out.println(sb);  // "hola"
```

#### Convertir a String

```{code} java
:caption: Conversión a String

StringBuilder sb = new StringBuilder("Construido");

String resultado = sb.toString();
```

### StringBuilder vs StringBuffer

:::{table} Comparación StringBuilder vs StringBuffer
:label: tbl-sb-vs-sbuffer

| Característica | StringBuilder | StringBuffer |
| :------------- | :------------ | :----------- |
| Thread-safe | No | Sí (sincronizado) |
| Rendimiento | Más rápido | Más lento |
| Uso típico | Un solo hilo | Múltiples hilos |
| Desde | Java 5 | Java 1.0 |

:::

:::{tip}
En la mayoría de los casos, usá `StringBuilder`. Solo usá `StringBuffer` si necesitás acceso concurrente desde múltiples hilos.
:::

## Patrones Comunes

### Construir Cadenas con Separadores

```{code} java
:caption: Unir elementos con separador

public String unirConComas(String[] elementos) {
    if (elementos.length == 0) {
        return "";
    }
    
    StringBuilder sb = new StringBuilder();
    sb.append(elementos[0]);
    
    for (int i = 1; i < elementos.length; i++) {
        sb.append(", ");
        sb.append(elementos[i]);
    }
    
    return sb.toString();
}

// O más simple con String.join
String resultado = String.join(", ", elementos);
```

### Repetir Cadena

```{code} java
:caption: Repetir una cadena

// Java 11+
String repetido = "ab".repeat(3);  // "ababab"

// Antes de Java 11
public String repetir(String texto, int veces) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < veces; i++) {
        sb.append(texto);
    }
    return sb.toString();
}
```

### Construir Tablas de Texto

```{code} java
:caption: Generar tabla formateada

public String generarTabla(String[][] datos) {
    StringBuilder sb = new StringBuilder();
    
    for (String[] fila : datos) {
        sb.append("| ");
        for (String celda : fila) {
            sb.append(String.format("%-15s | ", celda));
        }
        sb.append("\n");
    }
    
    return sb.toString();
}
```

## Rendimiento

### Cuándo Usar Qué

```{code} java
:caption: Guía de uso

// String: operaciones simples, pocas concatenaciones
String saludo = "Hola " + nombre + "!";

// StringBuilder: lazos, muchas concatenaciones
StringBuilder sb = new StringBuilder();
for (String linea : lineas) {
    sb.append(linea).append("\n");
}

// String.join: unir elementos con delimitador
String csv = String.join(",", valores);

// String.format: formateo complejo
String info = String.format("%s: %d (%.2f%%)", nombre, valor, porcentaje);
```

### Preallocación de Capacidad

Si conocés el tamaño aproximado, prealocá capacidad:

```{code} java
:caption: Prealocar capacidad

// Cada línea tiene ~50 caracteres, 1000 líneas
StringBuilder sb = new StringBuilder(50 * 1000);

for (String linea : lineas) {
    sb.append(linea);
}
```

## Ejercicios

```{exercise}
:label: ej-strings-1

Implementá un método `String invertirPalabras(String texto)` que invierta el orden de las palabras pero mantenga cada palabra intacta.

Ejemplo: "Hola Mundo Java" → "Java Mundo Hola"
```

```{solution} ej-strings-1
```java
public String invertirPalabras(String texto) {
    String[] palabras = texto.trim().split("\\s+");
    StringBuilder sb = new StringBuilder();
    
    for (int i = palabras.length - 1; i >= 0; i--) {
        sb.append(palabras[i]);
        if (i > 0) {
            sb.append(" ");
        }
    }
    
    return sb.toString();
}
```
```

```{exercise}
:label: ej-strings-2

Escribí un método `String comprimirCadena(String texto)` que comprima secuencias de caracteres repetidos.

Ejemplo: "aaabbbccca" → "a3b3c3a1"

Si la cadena comprimida no es más corta, retorná la original.
```

```{solution} ej-strings-2
```java
public String comprimirCadena(String texto) {
    if (texto.isEmpty()) {
        return texto;
    }
    
    StringBuilder sb = new StringBuilder();
    char actual = texto.charAt(0);
    int cuenta = 1;
    
    for (int i = 1; i < texto.length(); i++) {
        if (texto.charAt(i) == actual) {
            cuenta++;
        } else {
            sb.append(actual).append(cuenta);
            actual = texto.charAt(i);
            cuenta = 1;
        }
    }
    sb.append(actual).append(cuenta);
    
    String comprimido = sb.toString();
    return comprimido.length() < texto.length() ? comprimido : texto;
}
```
```

```{exercise}
:label: ej-strings-3

Implementá un método `boolean sonAnagramas(String a, String b)` que determine si dos cadenas son anagramas (contienen las mismas letras en diferente orden), ignorando espacios y mayúsculas.

Ejemplo: "Tom Marvolo Riddle" y "I am Lord Voldemort" → true
```

```{solution} ej-strings-3
```java
public boolean sonAnagramas(String a, String b) {
    // Normalizar: quitar espacios y pasar a minúsculas
    String normA = a.replaceAll("\\s", "").toLowerCase();
    String normB = b.replaceAll("\\s", "").toLowerCase();
    
    if (normA.length() != normB.length()) {
        return false;
    }
    
    // Ordenar caracteres y comparar
    char[] charsA = normA.toCharArray();
    char[] charsB = normB.toCharArray();
    Arrays.sort(charsA);
    Arrays.sort(charsB);
    
    return Arrays.equals(charsA, charsB);
}
```
```

:::{seealso}
- [Documentación de String](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/String.html)
- [Documentación de StringBuilder](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/StringBuilder.html)
:::
