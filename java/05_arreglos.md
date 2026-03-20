---
title: "Arreglos en Java"
description: Estudio técnico sobre estructuras de datos homogéneas, declaración, acceso, recorrido y pasaje a métodos.
---

# Arreglos en Java

En Java, los **arreglos** (_arrays_) son contenedores de tamaño fijo que almacenan elementos del mismo tipo. Si venís de C, la sintaxis te resultará familiar, aunque hay diferencias importantes en cómo Java gestiona la memoria y verifica los límites.

:::{note} Similitud con C
La idea fundamental es la misma: un arreglo es una secuencia contigua de elementos del mismo tipo, accesibles por índice. La diferencia clave es que en Java el arreglo "conoce" su propio tamaño y verifica automáticamente que no accedas fuera de sus límites.
:::

## Declaración de Arreglos

En Java hay dos sintaxis válidas para declarar arreglos (la primera es preferida):

```{code} java
:caption: Sintaxis de declaración

tipo[] nombreArreglo;    // ✅ Sintaxis preferida en Java
tipo nombreArreglo[];    // Sintaxis estilo C (válida pero no recomendada)
```

```{code} java
:caption: Ejemplos de declaración

int[] numeros;           // Arreglo de enteros
double[] temperaturas;   // Arreglo de doubles
char[] letras;           // Arreglo de caracteres
boolean[] banderas;      // Arreglo de booleanos
String[] nombres;        // Arreglo de Strings
```

:::{important} Declarar No Es Crear
Una declaración como `int[] numeros;` solo crea una **referencia** (similar a un puntero en C), pero no reserva memoria para los elementos. El arreglo todavía no existe; la referencia apunta a `null`.
:::

## Creación de Arreglos

Para crear el arreglo y reservar memoria, se usa la palabra clave `new`:

```{code} java
:caption: Creación con new

int[] numeros = new int[5];        // Arreglo de 5 enteros
double[] precios = new double[10]; // Arreglo de 10 doubles
char[] vocales = new char[5];      // Arreglo de 5 caracteres
```

### Valores por Defecto

Al crear un arreglo con `new`, todos los elementos se inicializan automáticamente:

| Tipo de Elemento | Valor por Defecto |
|:---|:---:|
| `int`, `short`, `byte`, `long` | `0` |
| `float`, `double` | `0.0` |
| `char` | `'\u0000'` (carácter nulo) |
| `boolean` | `false` |
| Referencias (String, etc.) | `null` |

```{code} java
:caption: Valores por defecto

int[] numeros = new int[3];
// numeros[0] = 0, numeros[1] = 0, numeros[2] = 0

boolean[] flags = new boolean[2];
// flags[0] = false, flags[1] = false
```

### Creación con Inicialización

Podés crear e inicializar un arreglo en una sola línea:

```{code} java
:caption: Inicialización directa

int[] primos = {2, 3, 5, 7, 11};           // 5 elementos
double[] notas = {8.5, 9.0, 7.5, 10.0};    // 4 elementos
char[] vocales = {'a', 'e', 'i', 'o', 'u'}; // 5 elementos
String[] dias = {"Lunes", "Martes", "Miércoles"};
```

:::{note} Tamaño Implícito
Cuando usás inicialización con llaves, Java determina automáticamente el tamaño del arreglo según la cantidad de elementos.
:::

### Separación de Declaración e Inicialización

```{code} java
:caption: Declaración e inicialización separadas

int[] numeros;
numeros = new int[5];              // OK

int[] datos;
datos = new int[]{1, 2, 3, 4, 5};  // Nota: new int[] es necesario aquí
```

## Acceso a Elementos

Los elementos se acceden usando índices entre corchetes. **Los índices comienzan en 0**.

```{code} java
:caption: Acceso por índice

int[] numeros = {10, 20, 30, 40, 50};

int primero = numeros[0];   // 10
int tercero = numeros[2];   // 30
int ultimo = numeros[4];    // 50

numeros[1] = 25;            // Modifica: ahora es {10, 25, 30, 40, 50}
```

### El Atributo `length`

Todo arreglo tiene un atributo `length` que indica su tamaño. Es de solo lectura y no puede modificarse.

```{code} java
:caption: Uso de length

int[] datos = {1, 2, 3, 4, 5};
int tamanio = datos.length;        // 5

int ultimoIndice = datos.length - 1;  // 4
int ultimoElemento = datos[datos.length - 1];  // 5
```

:::{important} Comparativa con C
En C, debés pasar el tamaño del arreglo como parámetro adicional a las funciones. En Java, el arreglo "sabe" su tamaño gracias a `length`:

```c
// En C:
void procesar(int arr[], int size) { ... }
```

```java
// En Java:
public static void procesar(int[] arr) {
    int size = arr.length;  // No necesita parámetro extra
}
```
:::

### Verificación de Límites

Java verifica automáticamente que el índice esté dentro del rango válido `[0, length-1]`. Si el índice es inválido, se lanza `ArrayIndexOutOfBoundsException`.

```{code} java
:caption: Error de índice fuera de rango

int[] numeros = new int[5];  // Índices válidos: 0, 1, 2, 3, 4

numeros[5] = 10;   // ❌ ArrayIndexOutOfBoundsException
numeros[-1] = 10;  // ❌ ArrayIndexOutOfBoundsException
```

:::{warning} Diferencia con C
En C, acceder fuera de los límites del arreglo es comportamiento indefinido (puede funcionar, corromper memoria, o causar un crash). En Java, **siempre** se detecta y se lanza una excepción, eliminando una fuente común de bugs y vulnerabilidades de seguridad.
:::

## Recorrido de Arreglos

### Recorrido con `for` Clásico

```{code} java
:caption: Recorrido con for

int[] numeros = {10, 20, 30, 40, 50};

// Recorrido de lectura
for (int i = 0; i < numeros.length; i = i + 1) {
    System.out.println("Elemento " + i + ": " + numeros[i]);
}

// Recorrido de modificación
for (int i = 0; i < numeros.length; i = i + 1) {
    numeros[i] = numeros[i] * 2;  // Duplica cada elemento
}
```

### Recorrido con `for-each`

El lazo `for-each` (también llamado "enhanced for") permite recorrer elementos sin usar índices:

```{code} java
:caption: Recorrido con for-each

int[] numeros = {10, 20, 30, 40, 50};

for (int numero : numeros) {
    System.out.println(numero);
}
```

La sintaxis es: `for (tipo elemento : arreglo)`.

:::{warning} Limitación del for-each
El `for-each` solo permite **leer** elementos. No podés modificar el arreglo ni conocer el índice actual:

```java
int[] numeros = {1, 2, 3};

// ❌ Esto NO modifica el arreglo original
for (int n : numeros) {
    n = n * 2;  // Solo modifica la variable local 'n'
}
// numeros sigue siendo {1, 2, 3}

// ✅ Para modificar, usar for clásico
for (int i = 0; i < numeros.length; i = i + 1) {
    numeros[i] = numeros[i] * 2;
}
// Ahora numeros es {2, 4, 6}
```
:::

### Recorrido con Bandera (Patrón de Búsqueda)

Para buscar elementos usando el patrón recomendado en el curso (sin `break`):

```{code} java
:caption: Búsqueda con bandera

int[] numeros = {10, 25, 30, 45, 50};
int buscado = 30;

boolean encontrado = false;
int posicion = -1;
int i = 0;

while (i < numeros.length && !encontrado) {
    if (numeros[i] == buscado) {
        encontrado = true;
        posicion = i;
    }
    i = i + 1;
}

if (encontrado) {
    System.out.println("Encontrado en posición: " + posicion);
} else {
    System.out.println("No encontrado");
}
```

## Arreglos como Referencias

En Java, las variables de arreglo son **referencias** (similares a punteros en C). Esto tiene implicaciones importantes.

### Asignación de Referencias

Cuando asignás un arreglo a otra variable, **no se copian los datos**. Ambas variables apuntan al mismo arreglo en memoria.

```{code} java
:caption: Asignación de referencias

int[] original = {1, 2, 3};
int[] copia = original;    // copia apunta al MISMO arreglo

copia[0] = 100;            // Modifica el arreglo compartido

System.out.println(original[0]);  // Imprime: 100
System.out.println(copia[0]);     // Imprime: 100
// Ambos "ven" el mismo cambio porque son el mismo arreglo
```

### Comparación de Arreglos

El operador `==` compara **referencias**, no contenido:

```{code} java
:caption: Comparación de arreglos

int[] a = {1, 2, 3};
int[] b = {1, 2, 3};
int[] c = a;

System.out.println(a == b);  // false (diferentes arreglos en memoria)
System.out.println(a == c);  // true (misma referencia)
```

Para comparar contenido, usá `Arrays.equals()`:

```{code} java
:caption: Comparación de contenido con Arrays.equals

import java.util.Arrays;

int[] a = {1, 2, 3};
int[] b = {1, 2, 3};

System.out.println(Arrays.equals(a, b));  // true (mismo contenido)
```

### Copia de Arreglos

Para crear una copia independiente:

```{code} java
:caption: Copia de arreglos

import java.util.Arrays;

int[] original = {1, 2, 3, 4, 5};

// Opción 1: Arrays.copyOf
int[] copia1 = Arrays.copyOf(original, original.length);

// Opción 2: Copia manual
int[] copia2 = new int[original.length];
for (int i = 0; i < original.length; i = i + 1) {
    copia2[i] = original[i];
}

// Ahora las copias son independientes
copia1[0] = 100;
System.out.println(original[0]);  // 1 (no afectado)
System.out.println(copia1[0]);    // 100
```

## Arreglos y Métodos

### Pasaje de Arreglos a Métodos

Cuando pasás un arreglo a un método, se pasa la **referencia** (no una copia). Esto significa que el método puede modificar el contenido del arreglo original.

```{code} java
:caption: Pasaje de arreglo a método

public static void duplicarElementos(int[] arr) {
    for (int i = 0; i < arr.length; i = i + 1) {
        arr[i] = arr[i] * 2;
    }
}

public static void main(String[] args) {
    int[] numeros = {1, 2, 3, 4, 5};
    
    duplicarElementos(numeros);
    
    // numeros ahora es {2, 4, 6, 8, 10}
    for (int n : numeros) {
        System.out.print(n + " ");  // Imprime: 2 4 6 8 10
    }
}
```

:::{important} Efecto Secundario en Métodos
Cuando un método recibe un arreglo y modifica sus elementos, esos cambios son visibles fuera del método. Esto se llama **efecto secundario** (_side effect_). Es importante documentar si un método modifica el arreglo que recibe.
:::

### Retorno de Arreglos

Un método puede crear y retornar un arreglo:

```{code} java
:caption: Método que retorna un arreglo

public static int[] crearSecuencia(int inicio, int cantidad) {
    int[] resultado = new int[cantidad];
    for (int i = 0; i < cantidad; i = i + 1) {
        resultado[i] = inicio + i;
    }
    return resultado;
}

public static void main(String[] args) {
    int[] secuencia = crearSecuencia(10, 5);
    // secuencia es {10, 11, 12, 13, 14}
}
```

Como los arreglos se crean en el Heap, no hay comportamiento indefinido, lo que se retorna es la referencia al arreglo creado.

### Patrón: Método que Calcula sin Modificar

Para evitar efectos secundarios, podés crear métodos que retornen un nuevo arreglo:

```{code} java
:caption: Método sin efecto secundario

public static int[] duplicar(int[] original) {
    int[] resultado = new int[original.length];
    for (int i = 0; i < original.length; i = i + 1) {
        resultado[i] = original[i] * 2;
    }
    return resultado;  // Retorna nuevo arreglo
}

public static void main(String[] args) {
    int[] numeros = {1, 2, 3};
    int[] duplicados = duplicar(numeros);
    
    // numeros sigue siendo {1, 2, 3}
    // duplicados es {2, 4, 6}
}
```

## Arreglos Multidimensionales

Java permite crear arreglos de arreglos, comúnmente usados como matrices.

### Declaración y Creación de Matrices

```{code} java
:caption: Matriz bidimensional

int[][] matriz = new int[3][4];  // 3 filas, 4 columnas

// Acceso: matriz[fila][columna]
matriz[0][0] = 1;   // Primera fila, primera columna
matriz[2][3] = 12;  // Tercera fila, cuarta columna
```

### Inicialización de Matrices

```{code} java
:caption: Inicialización directa de matriz

int[][] matriz = {
    {1, 2, 3, 4},     // Fila 0
    {5, 6, 7, 8},     // Fila 1
    {9, 10, 11, 12}   // Fila 2
};

// matriz[1][2] es 7
```

### Recorrido de Matrices

```{code} java
:caption: Recorrido de matriz con for anidado

int[][] matriz = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

int filas = matriz.length;         // 3
int columnas = matriz[0].length;   // 3

for (int fila = 0; fila < filas; fila = fila + 1) {
    for (int col = 0; col < columnas; col = col + 1) {
        System.out.print(matriz[fila][col] + "\t");
    }
    System.out.println();  // Nueva línea después de cada fila
}
```

### Matrices Irregulares (Jagged Arrays)

Cada fila puede tener diferente cantidad de columnas:

```{code} java
:caption: Matriz irregular

int[][] triangulo = new int[3][];
triangulo[0] = new int[1];  // Primera fila: 1 elemento
triangulo[1] = new int[2];  // Segunda fila: 2 elementos
triangulo[2] = new int[3];  // Tercera fila: 3 elementos

// O con inicialización directa
int[][] pascal = {
    {1},
    {1, 1},
    {1, 2, 1},
    {1, 3, 3, 1}
};
```

## Operaciones Comunes con Arreglos

### Encontrar Máximo y Mínimo

```{code} java
:caption: Buscar máximo en un arreglo

public static int maximo(int[] arr) {
    int max = arr[0];
    for (int i = 1; i < arr.length; i = i + 1) {
        if (arr[i] > max) {
            max = arr[i];
        }
    }
    return max;
}
```

### Sumar Elementos

```{code} java
:caption: Sumar todos los elementos

public static int sumar(int[] arr) {
    int suma = 0;
    for (int i = 0; i < arr.length; i = i + 1) {
        suma = suma + arr[i];
    }
    return suma;
}
```

### Calcular Promedio

```{code} java
:caption: Calcular promedio

public static double promedio(int[] arr) {
    int suma = sumar(arr);
    return (double) suma / arr.length;
}
```

### Contar Elementos

```{code} java
:caption: Contar elementos que cumplen condición

public static int contarPares(int[] arr) {
    int contador = 0;
    for (int i = 0; i < arr.length; i = i + 1) {
        if (arr[i] % 2 == 0) {
            contador = contador + 1;
        }
    }
    return contador;
}
```

### Invertir Arreglo

```{code} java
:caption: Invertir arreglo en su lugar

public static void invertir(int[] arr) {
    int izq = 0;
    int der = arr.length - 1;
    
    while (izq < der) {
        // Intercambiar elementos
        int temp = arr[izq];
        arr[izq] = arr[der];
        arr[der] = temp;
        
        izq = izq + 1;
        der = der - 1;
    }
}
```

## Clase `Arrays` (Utilidades)

Java provee la clase `java.util.Arrays` con métodos útiles:

```{code} java
:caption: Métodos de la clase Arrays

import java.util.Arrays;

int[] numeros = {5, 2, 8, 1, 9};

// Ordenar
Arrays.sort(numeros);  // numeros queda {1, 2, 5, 8, 9}

// Convertir a String (para imprimir)
System.out.println(Arrays.toString(numeros));  // [1, 2, 5, 8, 9]

// Llenar con un valor
int[] ceros = new int[5];
Arrays.fill(ceros, 0);  // {0, 0, 0, 0, 0}

// Buscar (requiere arreglo ordenado)
int pos = Arrays.binarySearch(numeros, 5);  // Retorna índice de 5

// Comparar contenido
int[] a = {1, 2, 3};
int[] b = {1, 2, 3};
boolean iguales = Arrays.equals(a, b);  // true

// Copiar
int[] copia = Arrays.copyOf(numeros, numeros.length);
int[] parcial = Arrays.copyOfRange(numeros, 1, 4);  // Copia índices 1, 2, 3
```

## Ejercicios de Aplicación

````{exercise}
:label: ej-arreglo-modificacion
¿Qué imprime el siguiente código? Explicá por qué.

```java
public static void modificar(int[] arr) {
    arr[0] = 999;
    arr = new int[]{100, 200, 300};
}

public static void main(String[] args) {
    int[] datos = {1, 2, 3};
    modificar(datos);
    System.out.println(Arrays.toString(datos));
}
```
````

```{solution} ej-arreglo-modificacion
Imprime `[999, 2, 3]`.

Explicación:
1. `arr[0] = 999` modifica el arreglo original porque `arr` apunta al mismo arreglo que `datos`.
2. `arr = new int[]{100, 200, 300}` solo cambia la referencia local `arr` para que apunte a un nuevo arreglo. Esto **no afecta** a la referencia `datos` en `main`.
3. Al terminar el método, el nuevo arreglo `{100, 200, 300}` se pierde (no hay referencia a él).

Este es el mismo comportamiento que vimos con el pasaje de referencias: podés modificar el contenido del arreglo, pero reasignar la referencia local no afecta la referencia original.
```

```{exercise}
:label: ej-matriz-suma-filas
Escribí un método que reciba una matriz de enteros y retorne un arreglo con la suma de cada fila.
```

````{solution} ej-matriz-suma-filas

```java
public static int[] sumarFilas(int[][] matriz) {
    int[] sumas = new int[matriz.length];
    
    for (int fila = 0; fila < matriz.length; fila = fila + 1) {
        int suma = 0;
        for (int col = 0; col < matriz[fila].length; col = col + 1) {
            suma = suma + matriz[fila][col];
        }
        sumas[fila] = suma;
    }
    
    return sumas;
}

// Ejemplo de uso:
int[][] m = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
int[] resultado = sumarFilas(m);  // {6, 15, 24}
```
````

````exercise`
:label: ej-matriz-irregular
Dada una matriz `int[][] m`, escribí un método que determine si la matriz es "rectangular" (todas las filas tienen la misma longitud) o "irregular".
```

````{solution} ej-matriz-irregular

```java
public static boolean esRectangular(int[][] m) {
    if (m == null || m.length == 0) {
        return true;
    }
    
    int columnasEsperadas = m[0].length;
    boolean esRect = true;
    int i = 1;
    
    while (i < m.length && esRect) {
        if (m[i] == null || m[i].length != columnasEsperadas) {
            esRect = false;
        }
        i = i + 1;
    }
    
    return esRect;
}
```
````

```{exercise}
:label: ej-rotar-arreglo
Escribí un método que rote los elementos de un arreglo una posición hacia la derecha. El último elemento pasa a ser el primero.
```

````{solution} ej-rotar-arreglo

```java
public static void rotarDerecha(int[] arr) {
    if (arr.length <= 1) {
        return;  // Nada que rotar
    }
    
    // Guardar el último elemento
    int ultimo = arr[arr.length - 1];
    
    // Desplazar todos hacia la derecha
    for (int i = arr.length - 1; i > 0; i = i - 1) {
        arr[i] = arr[i - 1];
    }
    
    // Colocar el último al principio
    arr[0] = ultimo;
}

// Ejemplo: {1, 2, 3, 4, 5} → {5, 1, 2, 3, 4}
```
````

## Referencias Bibliográficas

- **Schildt, H.** (2022). *Java: A Beginner's Guide* (9na ed.). McGraw Hill. (Capítulo 5: Arrays).
- **Liang, Y. D.** (2017). *Introduction to Java Programming and Data Structures* (11va ed.). Pearson.
- **Oracle Corporation.** (2023). *The Java Language Specification*. [Arrays](https://docs.oracle.com/javase/specs/jls/se21/html/jls-10.html).

