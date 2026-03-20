---
title: "Memoria, Referencias y Mutabilidad"
description: Estudio técnico sobre el modelo de memoria de Java, pasaje de argumentos, referencias, efectos secundarios y manejo de Strings.
---

# Memoria, Referencias y Mutabilidad

Este capítulo marca un punto de inflexión en el curso. Hasta ahora hemos trabajado principalmente con tipos primitivos y arreglos. Ahora vamos a profundizar en cómo Java gestiona la memoria, qué son las referencias, y cómo esto afecta el comportamiento de nuestros programas.

:::{note} Paralelo con C
En C, trabajás directamente con punteros y memoria. En Java, los punteros existen pero están "ocultos" detrás del concepto de **referencia**. Entender este modelo es crucial para evitar bugs sutiles relacionados con la mutabilidad y los efectos secundarios.
:::

## Modelo de Memoria de la JVM

La Máquina Virtual de Java (JVM) divide la memoria en varias regiones. Para nuestros propósitos, las más importantes son:

### Stack (Pila)

El **Stack** es una región de memoria que sigue el modelo LIFO (_Last In, First Out_). Cada hilo de ejecución tiene su propio stack privado.

**¿Qué se almacena en el Stack?**
- Variables locales de los métodos
- Parámetros de los métodos
- Valores de tipos primitivos (`int`, `double`, `boolean`, etc.)
- **Referencias** a objetos (no los objetos en sí)

```{code} java
:caption: Variables en el Stack

public static void ejemplo() {
    int edad = 25;           // 'edad' está en el Stack, valor: 25
    double precio = 19.99;   // 'precio' está en el Stack, valor: 19.99
    int[] numeros;           // 'numeros' está en el Stack, valor: null (referencia)
}
```

### Heap (Montículo)

El **Heap** es una región de memoria compartida por toda la aplicación. Es donde residen los **objetos reales** y los **arreglos**.

**¿Qué se almacena en el Heap?**
- Todos los objetos creados con `new`
- Todos los arreglos (incluso de tipos primitivos)
- Strings

```{code} java
:caption: Objetos en el Heap

public static void ejemplo() {
    // La referencia 'numeros' está en el Stack
    // El arreglo real {1, 2, 3} está en el Heap
    int[] numeros = new int[]{1, 2, 3};
    
    // La referencia 'texto' está en el Stack
    // El objeto String "Hola" está en el Heap
    String texto = "Hola";
}
```

### Visualización del Modelo

```
┌─────────────────────┐     ┌─────────────────────────────────┐
│       STACK         │     │             HEAP                │
│  (por cada hilo)    │     │        (compartido)             │
├─────────────────────┤     ├─────────────────────────────────┤
│                     │     │                                 │
│  edad: 25           │     │   ┌─────────────────────┐       │
│  precio: 19.99      │     │   │ Arreglo int[]       │       │
│  numeros: ─────────────────►  │ [1] [2] [3]         │       │
│  texto: ───────────────────►  └─────────────────────┘       │
│                     │     │   ┌─────────────────────┐       │
│                     │     │   │ String "Hola"       │       │
│                     │     │   └─────────────────────┘       │
└─────────────────────┘     └─────────────────────────────────┘
```

## Referencias: El "Puntero Oculto" de Java

Una **referencia** en Java es similar a un puntero en C, pero con restricciones:
- No podés hacer aritmética de punteros
- No podés acceder a direcciones de memoria directamente
- La JVM gestiona automáticamente la memoria (Garbage Collector)

### Diferencia entre Primitivos y Referencias

| Aspecto | Tipo Primitivo | Tipo Referencia |
|:---|:---|:---|
| Almacena | El valor directamente | Una dirección de memoria |
| Ubicación del valor | Stack | Stack (referencia) + Heap (objeto) |
| Valor por defecto | 0, false, '\0' | `null` |
| Comparación con `==` | Compara valores | Compara direcciones |

```{code} java
:caption: Primitivos vs Referencias

int a = 10;        // a contiene el valor 10
int b = a;         // b contiene una COPIA del valor 10
b = 20;            // Cambiar b NO afecta a a
System.out.println(a);  // Imprime: 10

int[] arr1 = {1, 2, 3};  // arr1 contiene una REFERENCIA al arreglo
int[] arr2 = arr1;       // arr2 contiene una COPIA de la REFERENCIA (misma dirección)
arr2[0] = 999;           // Modifica el arreglo compartido
System.out.println(arr1[0]);  // Imprime: 999 (¡arr1 también cambió!)
```

## El Valor `null`

Una referencia puede apuntar a "nada". Esto se representa con el valor especial `null`.

```{code} java
:caption: Referencias null

int[] numeros = null;  // La referencia existe pero no apunta a ningún arreglo
String texto = null;   // La referencia existe pero no apunta a ningún String

// Intentar usar una referencia null causa NullPointerException
System.out.println(numeros.length);  // ❌ NullPointerException
System.out.println(texto.length());  // ❌ NullPointerException
```

### NullPointerException

Esta es una de las excepciones más comunes en Java. Ocurre cuando intentás usar una referencia que es `null`.

```{code} java
:caption: Prevenir NullPointerException

public static void procesarArreglo(int[] arr) {
    // Verificar antes de usar
    if (arr == null) {
        System.out.println("Error: el arreglo es null");
        return;
    }
    
    // Ahora es seguro usar arr
    System.out.println("Tamaño: " + arr.length);
}
```

:::{warning} Comparativa con C
En C, acceder a un puntero `NULL` causa un _segmentation fault_ (crash del programa). En Java, se lanza una `NullPointerException` que puede ser capturada y manejada.
:::

## Comparación de Referencias: `==` vs `equals()`

El operador `==` tiene comportamiento diferente según el tipo:

**Para primitivos:** Compara los valores.
**Para referencias:** Compara las **direcciones de memoria** (¿apuntan al mismo objeto?).

```{code} java
:caption: Comparación con == para referencias

int[] a = {1, 2, 3};
int[] b = {1, 2, 3};
int[] c = a;

System.out.println(a == b);  // false (diferentes arreglos en el Heap)
System.out.println(a == c);  // true (misma referencia)
```

Para comparar el **contenido** de objetos, usá el método `equals()` o métodos específicos:

```{code} java
:caption: Comparación de contenido

import java.util.Arrays;

int[] a = {1, 2, 3};
int[] b = {1, 2, 3};

System.out.println(a == b);              // false
System.out.println(Arrays.equals(a, b)); // true (compara contenido)
```

## Pasaje de Argumentos a Métodos

En Java, **todo se pasa por valor**. Pero debés entender qué significa "valor" en cada caso:

### Pasaje de Primitivos

Se pasa una **copia del valor**. Cambios en el parámetro no afectan a la variable original.

```{code} java
:caption: Pasaje de primitivos

public static void duplicar(int numero) {
    numero = numero * 2;  // Solo modifica la copia local
    System.out.println("Dentro: " + numero);  // 20
}

public static void main(String[] args) {
    int valor = 10;
    duplicar(valor);
    System.out.println("Fuera: " + valor);  // 10 (sin cambios)
}
```

### Pasaje de Referencias

Se pasa una **copia de la referencia** (la dirección). El método puede modificar el contenido del objeto, pero no puede cambiar a qué apunta la referencia original.

```{code} java
:caption: Pasaje de referencias

public static void modificarContenido(int[] arr) {
    arr[0] = 999;  // Modifica el arreglo original ✅
}

public static void intentarReasignar(int[] arr) {
    arr = new int[]{100, 200, 300};  // Solo cambia la copia local ❌
}

public static void main(String[] args) {
    int[] numeros = {1, 2, 3};
    
    modificarContenido(numeros);
    System.out.println(numeros[0]);  // 999 (modificado)
    
    intentarReasignar(numeros);
    System.out.println(numeros[0]);  // 999 (no cambió a 100)
}
```

### Visualización del Pasaje de Referencias

```
ANTES de llamar a modificarContenido(numeros):

Stack (main)              Heap
┌──────────────┐         ┌─────────────┐
│ numeros: ─────────────►│ [1] [2] [3] │
└──────────────┘         └─────────────┘

DURANTE modificarContenido(arr):

Stack (main)              Heap
┌──────────────┐         ┌─────────────┐
│ numeros: ─────────────►│ [1] [2] [3] │
└──────────────┘         └─────────────┘
                               ▲
Stack (método)                 │
┌──────────────┐               │
│ arr: ────────────────────────┘
└──────────────┘
(arr es una COPIA de la referencia, apunta al mismo arreglo)
```

## Efectos Secundarios (Side Effects)

Un **efecto secundario** ocurre cuando un método modifica estado fuera de su propio alcance. Esto incluye:

1. **Modificar el contenido de un arreglo o objeto recibido**
2. Modificar variables globales o de instancia
3. Realizar entrada/salida (imprimir, leer archivos)

```{code} java
:caption: Método con efecto secundario

// ⚠️ Este método tiene efecto secundario: modifica el arreglo recibido
public static void ordenar(int[] arr) {
    // ... código de ordenamiento que modifica arr ...
}

// El efecto secundario es visible fuera del método
int[] datos = {5, 2, 8, 1};
ordenar(datos);
// datos ahora está ordenado {1, 2, 5, 8}
```

### Funciones Puras vs Métodos con Efectos

Una **función pura**:
- Siempre retorna el mismo resultado para los mismos argumentos
- No modifica nada fuera de sí misma
- No tiene efectos secundarios

```{code} java
:caption: Función pura vs método con efecto

// ✅ Función pura: no modifica nada, retorna nuevo arreglo
public static int[] ordenarNuevo(int[] original) {
    int[] copia = Arrays.copyOf(original, original.length);
    Arrays.sort(copia);
    return copia;
}

// ⚠️ Método con efecto: modifica el arreglo recibido
public static void ordenarEnLugar(int[] arr) {
    Arrays.sort(arr);
}
```

:::{tip} Documentar Efectos Secundarios
Es importante documentar cuando un método modifica sus argumentos. Los usuarios del método deben saber si su arreglo original será alterado.
:::

## Inmutabilidad

Un objeto **inmutable** es aquel cuyo estado no puede cambiar después de ser creado. Los tipos primitivos son inmutables por naturaleza. Los arreglos son mutables.

### La Palabra Clave `final`

`final` previene la **reasignación** de una variable, pero **no** previene la modificación del contenido si es una referencia.

```{code} java
:caption: final con referencias

final int[] numeros = {1, 2, 3};

numeros[0] = 999;            // ✅ Permitido: modificar contenido
// numeros = new int[5];     // ❌ Error: no se puede reasignar la referencia

final int constante = 10;
// constante = 20;           // ❌ Error: no se puede reasignar
```

### Estrategias para Prevenir Modificaciones

**1. Copia defensiva al recibir:**

```{code} java
:caption: Copia defensiva

public static double calcularPromedio(int[] datos) {
    // Crear copia para evitar que cambios externos afecten el cálculo
    int[] copia = Arrays.copyOf(datos, datos.length);
    
    int suma = 0;
    for (int i = 0; i < copia.length; i = i + 1) {
        suma = suma + copia[i];
    }
    return (double) suma / copia.length;
}
```

**2. Retornar copia en lugar del original:**

```{code} java
:caption: Retornar copia

public static int[] obtenerDatos() {
    // No retornar referencia a datos internos
    return Arrays.copyOf(datosInternos, datosInternos.length);
}
```

## Strings: Inmutabilidad Especial

La clase `String` en Java es **inmutable por diseño**. Esto significa que una vez creado un String, su contenido no puede cambiar.

### String Pool

Para optimizar memoria, Java mantiene un **String Pool**: una caché de strings en el heap.

```{code} java
:caption: String Pool

String s1 = "Hola";      // Busca/crea en el pool
String s2 = "Hola";      // Encuentra el mismo en el pool
String s3 = new String("Hola");  // Fuerza nuevo objeto fuera del pool

System.out.println(s1 == s2);  // true (misma referencia del pool)
System.out.println(s1 == s3);  // false (s3 es objeto diferente)
System.out.println(s1.equals(s3));  // true (mismo contenido)
```

### Métodos de String NO Modifican el Original

Todos los métodos de String que parecen "modificar" en realidad retornan un **nuevo String**:

```{code} java
:caption: Strings son inmutables

String original = "Hola";

String mayusculas = original.toUpperCase();  // Retorna nuevo String
String recortado = original.trim();          // Retorna nuevo String
String reemplazado = original.replace('o', '0');  // Retorna nuevo String

System.out.println(original);      // "Hola" (sin cambios)
System.out.println(mayusculas);    // "HOLA"
System.out.println(reemplazado);   // "H0la"
```

### Métodos Comunes de String

| Método | Descripción | Retorna |
|:---|:---|:---|
| `length()` | Cantidad de caracteres | `int` |
| `charAt(i)` | Carácter en posición i | `char` |
| `substring(inicio, fin)` | Subcadena | `String` |
| `toUpperCase()` | Convertir a mayúsculas | `String` |
| `toLowerCase()` | Convertir a minúsculas | `String` |
| `trim()` | Quitar espacios al inicio/fin | `String` |
| `replace(old, new)` | Reemplazar caracteres | `String` |
| `contains(s)` | ¿Contiene la subcadena? | `boolean` |
| `startsWith(s)` | ¿Empieza con...? | `boolean` |
| `endsWith(s)` | ¿Termina con...? | `boolean` |
| `equals(s)` | ¿Mismo contenido? | `boolean` |
| `equalsIgnoreCase(s)` | ¿Mismo contenido (ignorando mayúsculas)? | `boolean` |
| `indexOf(s)` | Posición de la primera ocurrencia | `int` (-1 si no está) |

```{code} java
:caption: Ejemplos de métodos de String

String texto = "  Hola Mundo  ";

System.out.println(texto.length());           // 14
System.out.println(texto.charAt(2));          // 'H'
System.out.println(texto.trim());             // "Hola Mundo"
System.out.println(texto.substring(2, 6));    // "Hola"
System.out.println(texto.contains("Mun"));    // true
System.out.println(texto.indexOf("Mundo"));   // 7
System.out.println(texto.replace(' ', '-'));  // "--Hola-Mundo--"
```

### Comparación de Strings

:::{warning} Nunca usar == para comparar contenido de Strings
El operador `==` compara referencias, no contenido. Usá siempre `equals()`.

```java
String a = "hola";
String b = new String("hola");

System.out.println(a == b);       // false (diferentes referencias)
System.out.println(a.equals(b));  // true (mismo contenido)
```
:::

## StringBuilder: Strings Mutables

Cuando necesitás construir un String de forma incremental (especialmente en lazos), usá `StringBuilder`.

### El Problema de la Concatenación en Lazos

```{code} java
:caption: Concatenación ineficiente

// ❌ Ineficiente: crea muchos objetos String temporales
String resultado = "";
for (int i = 0; i < 1000; i = i + 1) {
    resultado = resultado + i + ",";  // Crea nuevo String en cada iteración
}
```

Cada `+` crea un nuevo objeto String, copiando todo el contenido anterior. Esto es O(n²) en tiempo.

### La Solución: StringBuilder

```{code} java
:caption: StringBuilder eficiente

// ✅ Eficiente: modifica un buffer interno
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i = i + 1) {
    sb.append(i);
    sb.append(",");
}
String resultado = sb.toString();
```

### Métodos de StringBuilder

| Método | Descripción |
|:---|:---|
| `append(x)` | Agrega al final |
| `insert(pos, x)` | Inserta en posición |
| `delete(inicio, fin)` | Elimina rango |
| `reverse()` | Invierte el contenido |
| `toString()` | Convierte a String |
| `length()` | Longitud actual |

```{code} java
:caption: Uso de StringBuilder

StringBuilder sb = new StringBuilder();

sb.append("Hola");
sb.append(" ");
sb.append("Mundo");
System.out.println(sb.toString());  // "Hola Mundo"

sb.insert(5, "Java ");
System.out.println(sb.toString());  // "Hola Java Mundo"

sb.reverse();
System.out.println(sb.toString());  // "odnuM avaJ aloH"
```

## Resumen: Reglas Clave

1. **Tipos primitivos** almacenan valores directamente en el Stack.
2. **Referencias** almacenan direcciones; los objetos reales están en el Heap.
3. **Java pasa todo por valor**: primitivos copian el valor, referencias copian la dirección.
4. **`==`** compara direcciones para referencias; usá **`equals()`** para comparar contenido.
5. **`null`** significa "no apunta a nada"; usarlo causa `NullPointerException`.
6. **Arreglos son mutables**: modificar un arreglo recibido afecta el original.
7. **Strings son inmutables**: los métodos retornan nuevos Strings.
8. **Usá StringBuilder** para construir Strings en lazos.

## Ejercicios de Aplicación

```exercise
:label: ej-memoria-que-imprime
¿Qué imprime el siguiente código? Explicá por qué.

```java
int[] a = {1, 2, 3};
int[] b = a;
b[0] = 100;
System.out.println(a[0] + " " + b[0]);
```
```

```solution
:for: ej-memoria-que-imprime
Imprime `100 100`.

Al hacer `int[] b = a`, no se copia el arreglo, solo se copia la **referencia**. Tanto `a` como `b` apuntan al mismo arreglo en el Heap. Cuando modificamos `b[0]`, estamos modificando el único arreglo que existe, por lo que `a[0]` también muestra el cambio.
```

```exercise
:label: ej-string-inmutable
¿Qué imprime el siguiente código?

```java
String s = "hola";
s.toUpperCase();
System.out.println(s);
```
```

```solution
:for: ej-string-inmutable
Imprime `hola` (en minúsculas).

Los Strings son inmutables. El método `toUpperCase()` **retorna un nuevo String** con el contenido en mayúsculas, pero no modifica el String original. El resultado retornado se pierde porque no lo asignamos a ninguna variable.

Para obtener el resultado en mayúsculas:
```java
String s = "hola";
s = s.toUpperCase();  // Reasignar a s
System.out.println(s);  // Imprime: HOLA
```
```

```exercise
:label: ej-metodo-efectos
Escribí un método `invertir(int[] arr)` que invierta el arreglo **sin crear uno nuevo** (efecto secundario). Luego escribí otro método `invertirNuevo(int[] arr)` que retorne un nuevo arreglo invertido **sin modificar el original** (función pura).
```

````solution
:for: ej-metodo-efectos
```java
// Método con efecto secundario: modifica el arreglo recibido
public static void invertir(int[] arr) {
    int izq = 0;
    int der = arr.length - 1;
    
    while (izq < der) {
        int temp = arr[izq];
        arr[izq] = arr[der];
        arr[der] = temp;
        
        izq = izq + 1;
        der = der - 1;
    }
}

// Función pura: retorna nuevo arreglo, no modifica el original
public static int[] invertirNuevo(int[] original) {
    int[] resultado = new int[original.length];
    
    for (int i = 0; i < original.length; i = i + 1) {
        resultado[i] = original[original.length - 1 - i];
    }
    
    return resultado;
}
```
````

```exercise
:label: ej-stringbuilder-uso
Escribí un método que reciba un arreglo de Strings y retorne un único String con todos los elementos separados por comas, usando StringBuilder.
```

````solution
:for: ej-stringbuilder-uso
```java
public static String unirConComas(String[] elementos) {
    if (elementos == null || elementos.length == 0) {
        return "";
    }
    
    StringBuilder sb = new StringBuilder();
    
    for (int i = 0; i < elementos.length; i = i + 1) {
        sb.append(elementos[i]);
        
        // No agregar coma después del último elemento
        if (i < elementos.length - 1) {
            sb.append(", ");
        }
    }
    
    return sb.toString();
}

// Ejemplo:
String[] frutas = {"manzana", "banana", "naranja"};
String resultado = unirConComas(frutas);
// resultado = "manzana, banana, naranja"
```
````

```exercise
:label: ej-null-check
¿Qué problema tiene el siguiente código y cómo lo corregirías?

```java
public static int contarMayusculas(String texto) {
    int contador = 0;
    for (int i = 0; i < texto.length(); i = i + 1) {
        if (Character.isUpperCase(texto.charAt(i))) {
            contador = contador + 1;
        }
    }
    return contador;
}
```
```

````solution
:for: ej-null-check
El problema es que si `texto` es `null`, se lanzará `NullPointerException` al llamar a `texto.length()`.

Corrección:
```java
public static int contarMayusculas(String texto) {
    if (texto == null) {
        return 0;  // O lanzar excepción con mensaje claro
    }
    
    int contador = 0;
    for (int i = 0; i < texto.length(); i = i + 1) {
        if (Character.isUpperCase(texto.charAt(i))) {
            contador = contador + 1;
        }
    }
    return contador;
}
```

Siempre verificá que las referencias no sean `null` antes de usarlas, especialmente en parámetros de métodos públicos.
````

## Referencias Bibliográficas

- **Schildt, H.** (2022). *Java: A Beginner's Guide* (9na ed.). McGraw Hill.
- **Liang, Y. D.** (2017). *Introduction to Java Programming and Data Structures* (11va ed.). Pearson.
- **Bloch, J.** (2018). *Effective Java* (3ra ed.). Addison-Wesley Professional.
- **Oracle Corporation.** (2023). *The Java Language Specification*.

:::seealso
- {ref}`regla-0xE001` - Comparación de objetos con equals vs ==.
- {ref}`regla-0x3002` - Manejo de NullPointerException.
:::
