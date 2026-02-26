---
title: "Arreglos en Java"
description: Guía completa sobre declaración, manipulación y operaciones con arreglos.
---

# Arreglos en Java

Los **arreglos** (arrays) son estructuras de datos fundamentales que permiten almacenar múltiples valores del mismo tipo en posiciones de memoria contiguas. Son de tamaño fijo una vez creados.

## Características de los Arreglos

- **Tipo homogéneo**: todos los elementos deben ser del mismo tipo
- **Tamaño fijo**: se define al crear el arreglo y no puede cambiar
- **Acceso por índice**: los elementos se acceden mediante índices (0 a length-1)
- **Referencia**: las variables de arreglo almacenan referencias, no los datos en sí
- **Mutables**: el contenido puede modificarse después de la creación

## Declaración e Inicialización

### Declaración

```{code} java
:caption: Formas de declarar arreglos

// Estilo preferido en Java
int[] numeros;
String[] nombres;
double[] valores;

// Estilo alternativo (estilo C)
int numeros2[];  // Válido pero menos común
```

### Inicialización

```{code} java
:caption: Formas de inicializar arreglos

// Con tamaño específico (elementos con valores por defecto)
int[] numeros = new int[5];  // [0, 0, 0, 0, 0]
String[] nombres = new String[3];  // [null, null, null]
boolean[] flags = new boolean[4];  // [false, false, false, false]

// Con valores explícitos
int[] primos = {2, 3, 5, 7, 11};
String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie"};

// Forma completa (necesaria cuando no es declaración directa)
int[] pares = new int[]{2, 4, 6, 8, 10};
```

:::{table} Valores por defecto según tipo
:label: tbl-valores-defecto

| Tipo | Valor por defecto |
| :--- | :---------------- |
| `int`, `short`, `byte`, `long` | `0` |
| `float`, `double` | `0.0` |
| `char` | `'\u0000'` |
| `boolean` | `false` |
| Referencias (objetos) | `null` |

:::

## Acceso a Elementos

### Lectura y Escritura

```{code} java
:caption: Acceso a elementos del arreglo

int[] numeros = {10, 20, 30, 40, 50};

// Lectura
int primero = numeros[0];   // 10
int tercero = numeros[2];   // 30
int ultimo = numeros[numeros.length - 1];  // 50

// Escritura
numeros[0] = 100;   // Ahora: [100, 20, 30, 40, 50]
numeros[2] = 300;   // Ahora: [100, 20, 300, 40, 50]
```

### Propiedad length

```{code} java
:caption: Uso de length

int[] datos = {1, 2, 3, 4, 5};
int tamanio = datos.length;  // 5 (sin paréntesis, es un atributo)

// Recorrer con for tradicional
for (int i = 0; i < datos.length; i++) {
    System.out.println("Elemento " + i + ": " + datos[i]);
}
```

:::{warning}
**ArrayIndexOutOfBoundsException**: acceder a un índice fuera del rango `[0, length-1]` lanza esta excepción en tiempo de ejecución:

```java
int[] arr = new int[5];
int x = arr[5];   // ERROR: índices válidos son 0-4
int y = arr[-1];  // ERROR: índices negativos no son válidos
```
:::

## Recorrido de Arreglos

### For Tradicional

Útil cuando necesitás el índice o modificar elementos:

```{code} java
:caption: Recorrido con for tradicional

int[] numeros = {1, 2, 3, 4, 5};

// Lectura
for (int i = 0; i < numeros.length; i++) {
    System.out.println(numeros[i]);
}

// Modificación
for (int i = 0; i < numeros.length; i++) {
    numeros[i] = numeros[i] * 2;  // Duplica cada elemento
}
```

### For-Each

Más simple para recorrido de solo lectura:

```{code} java
:caption: Recorrido con for-each

int[] numeros = {1, 2, 3, 4, 5};

for (int numero : numeros) {
    System.out.println(numero);
}

// Calcular suma
int suma = 0;
for (int numero : numeros) {
    suma += numero;
}
```

:::{note}
El for-each no permite modificar el arreglo original. La variable del lazo es una **copia** del valor:

```java
for (int numero : numeros) {
    numero = numero * 2;  // ¡No modifica el arreglo!
}
```
:::

## Arreglos Multidimensionales

### Matrices (2D)

```{code} java
:caption: Declaración e inicialización de matrices

// Declaración con tamaño
int[][] matriz = new int[3][4];  // 3 filas, 4 columnas

// Inicialización con valores
int[][] tabla = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// Acceso a elementos
int elemento = tabla[1][2];  // Fila 1, Columna 2 = 6
tabla[0][0] = 100;           // Modifica primera celda
```

### Matrices Irregulares (Jagged Arrays)

Las filas pueden tener diferentes longitudes:

```{code} java
:caption: Matrices irregulares

int[][] irregular = new int[3][];
irregular[0] = new int[2];   // Fila 0: 2 columnas
irregular[1] = new int[4];   // Fila 1: 4 columnas
irregular[2] = new int[3];   // Fila 2: 3 columnas

// También con inicialización directa
int[][] triangular = {
    {1},
    {1, 2},
    {1, 2, 3},
    {1, 2, 3, 4}
};
```

### Recorrido de Matrices

```{code} java
:caption: Recorrido de matriz 2D

int[][] matriz = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

// For tradicional
for (int fila = 0; fila < matriz.length; fila++) {
    for (int col = 0; col < matriz[fila].length; col++) {
        System.out.print(matriz[fila][col] + " ");
    }
    System.out.println();
}

// For-each anidado
for (int[] fila : matriz) {
    for (int elemento : fila) {
        System.out.print(elemento + " ");
    }
    System.out.println();
}
```

## Clase Arrays

La clase `java.util.Arrays` proporciona métodos utilitarios para trabajar con arreglos:

### Copia de Arreglos

```{code} java
:caption: Copia de arreglos

import java.util.Arrays;

int[] original = {1, 2, 3, 4, 5};

// Copia completa
int[] copia = Arrays.copyOf(original, original.length);

// Copia con nuevo tamaño (agranda o achica)
int[] ampliado = Arrays.copyOf(original, 10);  // [1,2,3,4,5,0,0,0,0,0]
int[] reducido = Arrays.copyOf(original, 3);   // [1,2,3]

// Copia de rango
int[] rango = Arrays.copyOfRange(original, 1, 4);  // [2,3,4]
```

### Ordenamiento

```{code} java
:caption: Ordenamiento de arreglos

int[] numeros = {5, 2, 8, 1, 9, 3};

// Ordena in-place (modifica el arreglo original)
Arrays.sort(numeros);  // [1, 2, 3, 5, 8, 9]

// Ordenar strings
String[] palabras = {"banana", "manzana", "cereza"};
Arrays.sort(palabras);  // ["banana", "cereza", "manzana"]

// Ordenar rango específico
int[] datos = {5, 2, 8, 1, 9, 3};
Arrays.sort(datos, 1, 4);  // [5, 1, 2, 8, 9, 3] (ordena índices 1-3)
```

### Búsqueda Binaria

```{code} java
:caption: Búsqueda binaria (requiere arreglo ordenado)

int[] ordenados = {1, 3, 5, 7, 9, 11, 13};

int indice = Arrays.binarySearch(ordenados, 7);   // 3
int noEncontrado = Arrays.binarySearch(ordenados, 4);  // Negativo
```

:::{warning}
`binarySearch` solo funciona correctamente en arreglos **ordenados**. Si el arreglo no está ordenado, el resultado es indefinido.
:::

### Comparación

```{code} java
:caption: Comparación de arreglos

int[] a = {1, 2, 3};
int[] b = {1, 2, 3};
int[] c = {1, 2, 4};

boolean iguales1 = Arrays.equals(a, b);  // true
boolean iguales2 = Arrays.equals(a, c);  // false

// Para arreglos 2D usar deepEquals
int[][] m1 = {{1, 2}, {3, 4}};
int[][] m2 = {{1, 2}, {3, 4}};
boolean igualesDeep = Arrays.deepEquals(m1, m2);  // true
```

### Llenado y Conversión

```{code} java
:caption: Llenado y conversión a String

int[] numeros = new int[5];
Arrays.fill(numeros, 42);  // [42, 42, 42, 42, 42]

// Convertir a String para impresión
String representacion = Arrays.toString(numeros);
System.out.println(representacion);  // [42, 42, 42, 42, 42]

// Para matrices usar deepToString
int[][] matriz = {{1, 2}, {3, 4}};
System.out.println(Arrays.deepToString(matriz));  // [[1, 2], [3, 4]]
```

## Paso de Arreglos a Métodos

Los arreglos se pasan **por referencia** (más precisamente, se pasa una copia de la referencia):

```{code} java
:caption: Arreglos como parámetros

public static void duplicar(int[] arreglo) {
    for (int i = 0; i < arreglo.length; i++) {
        arreglo[i] = arreglo[i] * 2;  // Modifica el original
    }
}

public static void main(String[] args) {
    int[] numeros = {1, 2, 3, 4, 5};
    duplicar(numeros);
    System.out.println(Arrays.toString(numeros));  // [2, 4, 6, 8, 10]
}
```

### Evitar Modificaciones

Si no querés modificar el arreglo original, trabajá con una copia:

```{code} java
:caption: Trabajo con copia del arreglo

public static int[] duplicarSinModificar(int[] arreglo) {
    int[] copia = Arrays.copyOf(arreglo, arreglo.length);
    for (int i = 0; i < copia.length; i++) {
        copia[i] = copia[i] * 2;
    }
    return copia;  // Retorna la copia modificada
}
```

## Retorno de Arreglos

Los métodos pueden retornar arreglos:

```{code} java
:caption: Métodos que retornan arreglos

public static int[] generarRango(int inicio, int fin) {
    int[] resultado = new int[fin - inicio + 1];
    for (int i = 0; i < resultado.length; i++) {
        resultado[i] = inicio + i;
    }
    return resultado;
}

// Uso
int[] rango = generarRango(5, 10);  // [5, 6, 7, 8, 9, 10]
```

## Patrones Comunes

### Encontrar Máximo/Mínimo

```{code} java
:caption: Búsqueda de máximo

public static int encontrarMaximo(int[] arreglo) {
    if (arreglo.length == 0) {
        throw new IllegalArgumentException("Arreglo vacío");
    }
    
    int maximo = arreglo[0];
    for (int i = 1; i < arreglo.length; i++) {
        if (arreglo[i] > maximo) {
            maximo = arreglo[i];
        }
    }
    return maximo;
}
```

### Filtrado

```{code} java
:caption: Filtrar elementos

public static int[] filtrarPositivos(int[] arreglo) {
    // Contar positivos
    int cuenta = 0;
    for (int num : arreglo) {
        if (num > 0) cuenta++;
    }
    
    // Crear arreglo resultado
    int[] positivos = new int[cuenta];
    int indice = 0;
    for (int num : arreglo) {
        if (num > 0) {
            positivos[indice++] = num;
        }
    }
    return positivos;
}
```

### Invertir Arreglo

```{code} java
:caption: Invertir arreglo in-place

public static void invertir(int[] arreglo) {
    int izq = 0;
    int der = arreglo.length - 1;
    
    while (izq < der) {
        // Intercambiar
        int temp = arreglo[izq];
        arreglo[izq] = arreglo[der];
        arreglo[der] = temp;
        
        izq++;
        der--;
    }
}
```

## Ejercicios

```{exercise}
:label: ej-arrays-1

Implementá un método `int[] eliminarDuplicados(int[] arreglo)` que retorne un nuevo arreglo sin elementos duplicados, manteniendo el orden original.
```

```{solution} ej-arrays-1
```java
public static int[] eliminarDuplicados(int[] arreglo) {
    // Primero contar únicos
    int unicos = 0;
    for (int i = 0; i < arreglo.length; i++) {
        boolean esDuplicado = false;
        for (int j = 0; j < i; j++) {
            if (arreglo[i] == arreglo[j]) {
                esDuplicado = true;
                break;
            }
        }
        if (!esDuplicado) {
            unicos++;
        }
    }
    
    // Crear arreglo resultado
    int[] resultado = new int[unicos];
    int indice = 0;
    for (int i = 0; i < arreglo.length; i++) {
        boolean esDuplicado = false;
        for (int j = 0; j < i; j++) {
            if (arreglo[i] == arreglo[j]) {
                esDuplicado = true;
                break;
            }
        }
        if (!esDuplicado) {
            resultado[indice++] = arreglo[i];
        }
    }
    return resultado;
}
```
```

```{exercise}
:label: ej-arrays-2

Escribí un método `void rotarIzquierda(int[] arreglo, int posiciones)` que rote los elementos del arreglo hacia la izquierda la cantidad de posiciones indicada.

Ejemplo: `[1,2,3,4,5]` rotado 2 posiciones → `[3,4,5,1,2]`
```

```{solution} ej-arrays-2
```java
public static void rotarIzquierda(int[] arreglo, int posiciones) {
    int n = arreglo.length;
    if (n == 0) return;
    
    posiciones = posiciones % n;  // Normalizar
    if (posiciones < 0) posiciones += n;
    
    // Algoritmo de inversión triple
    invertirRango(arreglo, 0, posiciones - 1);
    invertirRango(arreglo, posiciones, n - 1);
    invertirRango(arreglo, 0, n - 1);
}

private static void invertirRango(int[] arr, int inicio, int fin) {
    while (inicio < fin) {
        int temp = arr[inicio];
        arr[inicio] = arr[fin];
        arr[fin] = temp;
        inicio++;
        fin--;
    }
}
```
```

```{exercise}
:label: ej-arrays-3

Implementá un método `int[][] multiplicarMatrices(int[][] a, int[][] b)` que multiplique dos matrices y retorne el resultado.
```

```{solution} ej-arrays-3
```java
public static int[][] multiplicarMatrices(int[][] a, int[][] b) {
    int filasA = a.length;
    int colsA = a[0].length;
    int colsB = b[0].length;
    
    if (colsA != b.length) {
        throw new IllegalArgumentException(
            "Dimensiones incompatibles para multiplicación"
        );
    }
    
    int[][] resultado = new int[filasA][colsB];
    
    for (int i = 0; i < filasA; i++) {
        for (int j = 0; j < colsB; j++) {
            for (int k = 0; k < colsA; k++) {
                resultado[i][j] += a[i][k] * b[k][j];
            }
        }
    }
    
    return resultado;
}
```
```

:::{seealso}
- [Documentación de Arrays](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html)
- [Tutorial de arreglos de Oracle](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html)
:::
