---
title: "Arreglos en Java"
description: Estudio técnico sobre estructuras de datos homogéneas, declaración, acceso, recorrido y pasaje a métodos.
---

(arreglos-en-java)=
# Arreglos en Java

En Java, los **arreglos** (_arrays_) son contenedores de tamaño fijo que almacenan elementos del mismo tipo. Si venís de C, la sintaxis te resultará familiar, aunque hay diferencias importantes en cómo Java gestiona la memoria y verifica los límites.

(que-es-un-arreglo)=
## ¿Qué es un Arreglo?

Un **arreglo** es una estructura de datos que permite almacenar múltiples valores del mismo tipo bajo un único nombre, organizados en posiciones consecutivas de memoria. Cada posición se identifica mediante un **índice numérico** que comienza en cero.

Pensalo como una fila de casilleros numerados: cada casillero puede guardar exactamente un elemento del mismo tipo, y para acceder a un casillero específico usás su número (índice).

:::{note} Similitud con C
La idea fundamental es la misma que en C: un arreglo es una secuencia contigua de elementos del mismo tipo, accesibles por índice. Las diferencias clave son:

1. **El arreglo "conoce" su propio tamaño**: en Java podés consultar `arr.length` en cualquier momento, mientras que en C debés pasar el tamaño como parámetro adicional.
2. **Verificación automática de límites**: Java lanza una excepción si accedés fuera del rango válido, mientras que C permite acceder a memoria inválida (comportamiento indefinido).
3. **Gestión de memoria automática**: no hay `malloc()` ni `free()`, el _garbage collector_ se encarga de liberar la memoria.
:::

(caracteristicas-fundamentales)=
## Características Fundamentales

Antes de ver la sintaxis, es importante entender qué caracteriza a los arreglos en Java:

| Característica | Descripción |
|:---|:---|
| **Tamaño fijo** | Una vez creado, el tamaño no puede cambiar. Si necesitás más espacio, debés crear un arreglo nuevo. |
| **Homogéneo** | Todos los elementos deben ser del mismo tipo (todos `int`, todos `double`, etc.). |
| **Indexado desde cero** | El primer elemento está en la posición 0, el segundo en la posición 1, y así sucesivamente. |
| **Acceso directo** | Podés acceder a cualquier elemento directamente por su índice en tiempo constante O(1). |
| **Almacenamiento contiguo** | Los elementos se almacenan en posiciones consecutivas de memoria (en el _heap_). |

(declaracion-de-arreglos)=
## Declaración de Arreglos

**Declarar** un arreglo significa indicarle al compilador que vamos a usar una variable que contendrá una referencia a un arreglo de un tipo específico.

En Java hay dos sintaxis válidas para declarar arreglos (la primera es preferida):

```{code} java
:caption: Sintaxis de declaración

tipo[] nombreArreglo;    // ✅ Sintaxis preferida en Java
tipo nombreArreglo[];    // Sintaxis estilo C (válida pero no recomendada)
```

La sintaxis `tipo[]` es preferida porque mantiene toda la información del tipo junta: `int[]` se lee claramente como "arreglo de enteros".

```{code} java
:caption: Ejemplos de declaración

int[] numeros;           // Arreglo de enteros
double[] temperaturas;   // Arreglo de doubles
char[] letras;           // Arreglo de caracteres
boolean[] banderas;      // Arreglo de booleanos
String[] nombres;        // Arreglo de Strings (referencias a objetos)
```

:::{important} Declarar No Es Crear
Una declaración como `int[] numeros;` solo crea una **referencia** (similar a un puntero en C), pero **no reserva memoria para los elementos**. El arreglo todavía no existe; la referencia contiene el valor especial `null`, que significa "no apunta a nada".

```java
int[] numeros;              // numeros vale null
System.out.println(numeros); // Imprime: null
numeros[0] = 5;             // ❌ NullPointerException: no hay arreglo
```

En C sería equivalente a declarar un puntero sin asignarle memoria:
```c
int *numeros;  // Puntero sin inicializar (apunta a basura)
```

La diferencia es que en Java la referencia está inicializada a `null`, un valor conocido, en lugar de contener basura.
:::

(creacion-de-arreglos)=
## Creación de Arreglos

**Crear** un arreglo significa reservar memoria en el _heap_ para almacenar los elementos. Para esto se usa la palabra clave `new`, seguida del tipo y el tamaño entre corchetes:

```{code} java
:caption: Creación con new

int[] numeros = new int[5];        // Arreglo de 5 enteros
double[] precios = new double[10]; // Arreglo de 10 doubles
char[] vocales = new char[5];      // Arreglo de 5 caracteres
```

La expresión `new int[5]` hace lo siguiente:
1. Reserva espacio en el _heap_ para 5 valores de tipo `int` (20 bytes, ya que cada `int` ocupa 4 bytes).
2. Inicializa todos los elementos a su valor por defecto (cero para `int`).
3. Retorna una **referencia** (dirección de memoria) al arreglo creado.

Esta referencia se almacena en la variable `numeros`, que vive en el _stack_.

:::{tip} Comparación con C
En C usarías `malloc()` para reservar memoria dinámica:

```c
int *numeros = malloc(5 * sizeof(int));  // Reserva memoria
if (numeros == NULL) {
    // Manejar error de memoria
}
// IMPORTANTE: Los valores son basura hasta que los inicialices
free(numeros);  // Liberar memoria manualmente
```

En Java:
```java
int[] numeros = new int[5];  // Reserva e inicializa a 0
// No hay free(): el garbage collector libera automáticamente
```

Java elimina dos fuentes comunes de errores: valores sin inicializar y memory leaks por olvidar llamar a `free()`.
:::

(09-arreglos-valores-por-defecto)=
### Valores por Defecto

Al crear un arreglo con `new`, Java **garantiza** que todos los elementos se inicializan automáticamente a un valor por defecto. Esto es una diferencia importante con C, donde la memoria dinámica contiene "basura" hasta que la inicializás explícitamente.

| Tipo de Elemento | Valor por Defecto | Explicación |
|:---|:---:|:---|
| `int`, `short`, `byte`, `long` | `0` | Cero numérico entero |
| `float`, `double` | `0.0` | Cero numérico decimal |
| `char` | `'\u0000'` | Carácter nulo (código Unicode 0) |
| `boolean` | `false` | El valor booleano falso |
| Referencias (String, objetos, etc.) | `null` | No apunta a ningún objeto |

```{code} java
:caption: Valores por defecto

int[] numeros = new int[3];
// numeros[0] = 0, numeros[1] = 0, numeros[2] = 0

boolean[] flags = new boolean[2];
// flags[0] = false, flags[1] = false

String[] palabras = new String[3];
// palabras[0] = null, palabras[1] = null, palabras[2] = null
```

:::{warning} Cuidado con null en arreglos de referencias
Cuando creás un arreglo de objetos (como `String[]`), los elementos son `null` por defecto. Si intentás usar un elemento sin asignarle un objeto, obtenés `NullPointerException`:

```java
String[] nombres = new String[3];
int longitud = nombres[0].length();  // ❌ NullPointerException
                                      // nombres[0] es null

// Primero debés asignar un valor:
nombres[0] = "Juan";
int longitud = nombres[0].length();  // ✅ OK, retorna 4
```
:::

(creacion-con-inicializacion)=
### Creación con Inicialización

Cuando conocés los valores iniciales al momento de crear el arreglo, podés usar una sintaxis abreviada con llaves. Java determina automáticamente el tamaño según la cantidad de elementos:

```{code} java
:caption: Inicialización directa

int[] primos = {2, 3, 5, 7, 11};           // 5 elementos
double[] notas = {8.5, 9.0, 7.5, 10.0};    // 4 elementos
char[] vocales = {'a', 'e', 'i', 'o', 'u'}; // 5 elementos
String[] dias = {"Lunes", "Martes", "Miércoles"};
```

Esta sintaxis es equivalente a:

```{code} java
:caption: Equivalencia de la inicialización directa

int[] primos = new int[5];
primos[0] = 2;
primos[1] = 3;
primos[2] = 5;
primos[3] = 7;
primos[4] = 11;
```

:::{note} Tamaño Implícito
Cuando usás inicialización con llaves, Java determina automáticamente el tamaño del arreglo según la cantidad de elementos. No podés especificar un tamaño diferente:

```java
int[] numeros = {1, 2, 3};  // Tamaño es 3, determinado automáticamente
// NO podés hacer: int[5] numeros = {1, 2, 3};  // Error de sintaxis
```
:::

:::{tip} Similitud con C
Esta sintaxis es casi idéntica a la inicialización de arreglos en C:

```c
// En C:
int primos[] = {2, 3, 5, 7, 11};

// En Java:
int[] primos = {2, 3, 5, 7, 11};
```

La única diferencia es la posición de los corchetes (antes o después del nombre).
:::

(separacion-de-declaracion-e-inicializacion)=
### Separación de Declaración e Inicialización

A veces necesitás declarar la variable en un lugar y crear el arreglo en otro (por ejemplo, dentro de un condicional). En ese caso, la sintaxis con llaves requiere incluir `new tipo[]` explícitamente:

```{code} java
:caption: Declaración e inicialización separadas

int[] numeros;
numeros = new int[5];              // OK: creación con tamaño

int[] datos;
datos = new int[]{1, 2, 3, 4, 5};  // OK: creación con valores
// Nota: new int[] es NECESARIO aquí, no podés usar solo {1, 2, 3, 4, 5}
```

¿Por qué se necesita `new int[]` en el segundo caso? Porque las llaves solas (`{1, 2, 3}`) solo son válidas como parte de una declaración con inicialización. Es una restricción sintáctica del lenguaje.

```{code} java
:caption: Casos de uso de la separación

// Útil cuando el arreglo depende de una condición:
int[] resultado;
if (modo == 1) {
    resultado = new int[]{1, 2, 3};
} else {
    resultado = new int[]{10, 20};
}

// Útil cuando el tamaño se conoce en tiempo de ejecución:
int[] dinamico;
int tamanio = obtenerTamanioDeUsuario();
dinamico = new int[tamanio];
```

(acceso-a-elementos)=
## Acceso a Elementos

Una vez creado el arreglo, podés **leer** y **escribir** elementos individuales usando el operador de indexación `[]`. Los elementos se acceden usando índices entre corchetes, y **los índices comienzan en 0** (igual que en C).

```{code} java
:caption: Acceso por índice

int[] numeros = {10, 20, 30, 40, 50};
//              [0]  [1]  [2]  [3]  [4]  ← índices

// Lectura de elementos
int primero = numeros[0];   // 10
int tercero = numeros[2];   // 30
int ultimo = numeros[4];    // 50

// Escritura de elementos
numeros[1] = 25;            // Modifica: ahora es {10, 25, 30, 40, 50}
```

El acceso por índice es una operación de **tiempo constante** O(1): acceder al elemento 0 toma el mismo tiempo que acceder al elemento 1000, porque Java calcula directamente la dirección de memoria usando aritmética de punteros internamente.

(indices-validos)=
### Índices Válidos

Para un arreglo de tamaño `n`, los índices válidos van de `0` a `n-1`:

| Arreglo | Tamaño | Índices válidos |
|:---|:---:|:---|
| `new int[5]` | 5 | 0, 1, 2, 3, 4 |
| `new int[1]` | 1 | 0 |
| `new int[100]` | 100 | 0 a 99 |

(el-atributo-length)=
### El Atributo `length`

Todo arreglo en Java tiene un atributo llamado `length` que indica cuántos elementos puede contener. Este atributo:

- Es de **solo lectura**: no podés modificarlo.
- Está **siempre disponible**: no necesitás importar nada.
- Tiene un valor **constante**: se establece al crear el arreglo y nunca cambia.

```{code} java
:caption: Uso de length

int[] datos = {1, 2, 3, 4, 5};
int tamanio = datos.length;        // 5

int ultimoIndice = datos.length - 1;  // 4 (índice del último elemento)
int ultimoElemento = datos[datos.length - 1];  // 5 (valor del último elemento)
```

:::{important} Comparativa con C
En C, debés pasar el tamaño del arreglo como parámetro adicional a las funciones porque los arreglos "no saben" su propio tamaño. En Java, el arreglo "conoce" su tamaño gracias a `length`:

```c
// En C: necesitás pasar el tamaño como parámetro
void procesar(int arr[], int size) {
    for (int i = 0; i < size; i++) {
        printf("%d ", arr[i]);
    }
}

int main() {
    int numeros[] = {1, 2, 3, 4, 5};
    procesar(numeros, 5);  // Debés recordar pasar el tamaño correcto
}
```

```java
// En Java: el arreglo conoce su propio tamaño
public static void procesar(int[] arr) {
    for (int i = 0; i < arr.length; i = i + 1) {
        System.out.print(arr[i] + " ");
    }
}

public static void main(String[] args) {
    int[] numeros = {1, 2, 3, 4, 5};
    procesar(numeros);  // No necesitás parámetro de tamaño
}
```

Esto elimina una fuente común de errores en C: pasar un tamaño incorrecto a la función.
:::

:::{note} `length` vs `length()`
No confundas `length` (atributo de arreglos) con `length()` (método de String):

```java
int[] numeros = {1, 2, 3};
String texto = "Hola";

int tamArreglo = numeros.length;   // Sin paréntesis (atributo)
int tamString = texto.length();    // Con paréntesis (método)
```
:::

(verificacion-de-limites)=
### Verificación de Límites

Java verifica **automáticamente** en tiempo de ejecución que el índice esté dentro del rango válido `[0, length-1]`. Si intentás acceder con un índice inválido (negativo o mayor o igual a `length`), Java lanza una excepción `ArrayIndexOutOfBoundsException` y el programa se detiene.

```{code} java
:caption: Error de índice fuera de rango

int[] numeros = new int[5];  // Índices válidos: 0, 1, 2, 3, 4

numeros[5] = 10;   // ❌ ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5
numeros[-1] = 10;  // ❌ ArrayIndexOutOfBoundsException: Index -1 out of bounds for length 5
```

:::{warning} Diferencia Crítica con C
En C, acceder fuera de los límites del arreglo es **comportamiento indefinido** (_undefined behavior_). El programa puede:
- Funcionar aparentemente bien (pero con datos corruptos).
- Corromper otras variables en memoria.
- Causar un _segmentation fault_ (crash).
- Crear vulnerabilidades de seguridad (_buffer overflow_).

```c
// En C: comportamiento indefinido
int numeros[5];
numeros[5] = 10;   // Puede funcionar, corromper memoria, o crashear
numeros[100] = 20; // Nadie sabe qué pasa
```

En Java, **siempre** se detecta el acceso inválido y se lanza una excepción con un mensaje claro. Esto hace que los errores sean más fáciles de encontrar y corregir, y elimina una categoría completa de bugs y vulnerabilidades de seguridad.
:::

```{code} java
:caption: Ejemplo de manejo del error

int[] datos = {10, 20, 30};
int indice = 5;

// Forma segura: verificar antes de acceder
if (indice >= 0 && indice < datos.length) {
    System.out.println(datos[indice]);
} else {
    System.out.println("Índice fuera de rango: " + indice);
}
```

(recorrido-de-arreglos)=
## Recorrido de Arreglos

**Recorrer** un arreglo significa visitar cada uno de sus elementos, generalmente para leerlos, procesarlos, o modificarlos. Es una de las operaciones más comunes.

(recorrido-con-for-clasico)=
### Recorrido con `for` Clásico

El lazo `for` clásico es la forma más flexible de recorrer un arreglo. Usás una variable índice que va desde 0 hasta `length - 1`:

```{code} java
:caption: Recorrido con for - estructura básica

for (int i = 0; i < arreglo.length; i = i + 1) {
    // Usar arreglo[i]
}
```

La condición `i < arreglo.length` (no `<=`) es fundamental: si usaras `<=` accederías a `arreglo[length]` que está fuera de rango.

```{code} java
:caption: Recorrido de lectura

int[] numeros = {10, 20, 30, 40, 50};

// Recorrido para mostrar elementos
for (int i = 0; i < numeros.length; i = i + 1) {
    System.out.println("Elemento " + i + ": " + numeros[i]);
}
// Salida:
// Elemento 0: 10
// Elemento 1: 20
// Elemento 2: 30
// Elemento 3: 40
// Elemento 4: 50
```

```{code} java
:caption: Recorrido de modificación

int[] numeros = {10, 20, 30, 40, 50};

// Recorrido para modificar elementos
for (int i = 0; i < numeros.length; i = i + 1) {
    numeros[i] = numeros[i] * 2;  // Duplica cada elemento
}
// numeros ahora es {20, 40, 60, 80, 100}
```

:::{tip} Cuándo usar el for clásico
Usá el `for` clásico cuando necesités:
- Conocer el índice actual durante el recorrido.
- Modificar los elementos del arreglo.
- Recorrer solo una parte del arreglo (por ejemplo, del índice 2 al 5).
- Recorrer en orden inverso (de atrás hacia adelante).
:::

(recorrido-con-for-each)=
### Recorrido con `for-each`

El lazo `for-each` (también llamado "enhanced for" o "for mejorado") proporciona una sintaxis más limpia cuando solo necesitás leer los elementos sin importar el índice:

```{code} java
:caption: Recorrido con for-each

int[] numeros = {10, 20, 30, 40, 50};

for (int numero : numeros) {
    System.out.println(numero);
}
// Salida: 10, 20, 30, 40, 50 (cada uno en su línea)
```

La sintaxis es: `for (tipo elemento : arreglo)`. Se lee como "para cada `numero` en `numeros`".

**¿Cómo funciona internamente?** El `for-each` es azúcar sintáctica. El compilador lo transforma en un `for` clásico:

```{code} java
:caption: Equivalencia del for-each

// Esto:
for (int numero : numeros) {
    System.out.println(numero);
}

// Es equivalente a esto:
for (int i = 0; i < numeros.length; i = i + 1) {
    int numero = numeros[i];  // Copia el valor en variable local
    System.out.println(numero);
}
```

:::{warning} Limitación Fundamental del for-each
El `for-each` solo permite **leer** elementos. **No podés modificar el arreglo** ni conocer el índice actual. Esto ocurre porque la variable del lazo (`numero` en el ejemplo) es una **copia** del valor, no una referencia al elemento original.

```java
int[] numeros = {1, 2, 3};

// ❌ Esto NO modifica el arreglo original
for (int n : numeros) {
    n = n * 2;  // Solo modifica la variable local 'n' (la copia)
}
// numeros sigue siendo {1, 2, 3}

// ✅ Para modificar, usá el for clásico
for (int i = 0; i < numeros.length; i = i + 1) {
    numeros[i] = numeros[i] * 2;  // Modifica el elemento real
}
// Ahora numeros es {2, 4, 6}
```
:::

:::{tip} Cuándo usar for-each
Usá `for-each` cuando:
- Solo necesitás leer los valores.
- No necesitás conocer el índice.
- Querés código más legible y menos propenso a errores.

Es especialmente útil para calcular sumas, promedios, o buscar si existe un elemento.
:::

(recorrido-parcial)=
### Recorrido Parcial

A veces necesitás recorrer solo una porción del arreglo. El `for` clásico permite controlar exactamente dónde empezar y terminar:

```{code} java
:caption: Recorrido de una porción del arreglo

int[] datos = {10, 20, 30, 40, 50, 60, 70};

// Recorrer solo del índice 2 al 4 (inclusive)
for (int i = 2; i <= 4; i = i + 1) {
    System.out.print(datos[i] + " ");  // Imprime: 30 40 50
}
```

(recorrido-inverso)=
### Recorrido Inverso

Para recorrer de atrás hacia adelante, empezá en `length - 1` y decrementá:

```{code} java
:caption: Recorrido en orden inverso

int[] numeros = {10, 20, 30, 40, 50};

for (int i = numeros.length - 1; i >= 0; i = i - 1) {
    System.out.print(numeros[i] + " ");  // Imprime: 50 40 30 20 10
}
```

(recorrido-con-while)=
### Recorrido con `while`

Aunque el `for` es más común para recorrer arreglos, también podés usar `while`. Es especialmente útil cuando la condición de terminación es más compleja que simplemente llegar al final:

```{code} java
:caption: Recorrido con while

int[] numeros = {10, 20, 30, 40, 50};
int i = 0;

while (i < numeros.length) {
    System.out.println(numeros[i]);
    i = i + 1;
}
```

(recorrido-con-bandera-patron-de-busqueda)=
### Recorrido con Bandera (Patrón de Búsqueda)

Uno de los patrones más importantes es la **búsqueda con bandera**: recorrer el arreglo hasta encontrar un elemento específico o llegar al final. Usamos una variable booleana (la "bandera") para controlar cuándo terminar.

Este patrón evita usar `break`, lo cual produce código más claro y predecible:

```{code} java
:caption: Búsqueda con bandera

int[] numeros = {10, 25, 30, 45, 50};
int buscado = 30;

boolean encontrado = false;
int posicion = -1;  // -1 indica "no encontrado"
int i = 0;

// Recorre mientras haya elementos Y no hayamos encontrado
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

**Análisis del patrón:**

1. **Inicialización**: `encontrado = false` y `posicion = -1` (valor centinela que indica "no encontrado").
2. **Condición del while**: `i < numeros.length && !encontrado`
   - Continúa si hay elementos por revisar **Y** todavía no encontramos el buscado.
   - Cuando encontramos el elemento, `encontrado` se vuelve `true`, `!encontrado` es `false`, y el lazo termina.
3. **Resultado**: Después del lazo, `encontrado` nos dice si tuvimos éxito, y `posicion` nos da la ubicación.

:::{tip} ¿Por qué usar bandera en lugar de break?
El patrón con bandera tiene ventajas:
- **Una sola salida del lazo**: la condición del `while` controla todo.
- **Código más predecible**: siempre sabés dónde termina el lazo.
- **Fácil de verificar**: la post-condición del lazo es clara (`encontrado` es verdadero si y solo si el elemento existe).
:::

(arreglos-como-referencias)=
## Arreglos como Referencias

En Java, las variables de arreglo son **referencias** (conceptualmente similares a punteros en C). Entender esto es fundamental para evitar errores comunes.

(que-es-una-referencia)=
### ¿Qué es una Referencia?

Una **referencia** es un valor que indica la ubicación de un objeto en memoria. Cuando declarás `int[] numeros`, la variable `numeros` no contiene el arreglo en sí, sino una referencia (dirección de memoria) hacia donde está el arreglo en el _heap_.

```{figure} 09/arreglo_memoria.svg
:label: fig-arreglo-memoria
:align: center
:width: 90%

Representación de un arreglo en memoria: la variable (referencia) vive en el Stack, mientras que el arreglo con sus datos vive en el Heap.
```

**Analogía**: pensá en la referencia como la dirección de una casa. La variable `numeros` contiene la dirección "Calle 123", pero la casa (el arreglo) está en esa dirección, no dentro de la variable.

:::{note} Comparación con C
En C, esto sería similar a un puntero:

```c
// En C:
int *numeros = malloc(5 * sizeof(int));  // numeros contiene una dirección
```

```java
// En Java:
int[] numeros = new int[5];  // numeros contiene una referencia
```

La diferencia principal es que en Java no podés hacer aritmética de punteros ni acceder a memoria arbitraria. Las referencias son "punteros seguros".
:::

(asignacion-de-referencias-alias)=
### Asignación de Referencias (Alias)

Cuando asignás un arreglo a otra variable, **no se copian los datos**. Solo se copia la referencia, por lo que ambas variables terminan apuntando al mismo arreglo en memoria. Esto se llama crear un **alias**.

```{figure} 09/referencias_copia.svg
:label: fig-referencias-copia
:align: center
:width: 85%

Diferencia entre asignación de referencias (alias) y copia real de arreglos.
```

```{code} java
:caption: Asignación de referencias - creando un alias

int[] original = {1, 2, 3};
int[] alias = original;    // alias apunta al MISMO arreglo, NO es una copia

alias[0] = 100;            // Modifica a través del alias

System.out.println(original[0]);  // Imprime: 100
System.out.println(alias[0]);     // Imprime: 100
// Ambos "ven" el mismo cambio porque apuntan al mismo arreglo
```

**¿Por qué pasa esto?** La línea `int[] alias = original;` copia el **valor de la referencia** (la dirección de memoria), no el contenido del arreglo. Después de esta línea, tanto `original` como `alias` contienen la misma dirección, por lo que ambos apuntan al mismo arreglo.

:::{warning} Error común: creer que `=` copia el arreglo
Muchos programadores novatos esperan que `int[] copia = original;` cree una copia independiente. ¡No es así! Si modificás `copia[0]`, también cambia `original[0]` porque son el mismo arreglo.
:::

(comparacion-de-arreglos)=
### Comparación de Arreglos

El operador `==` compara **referencias** (direcciones de memoria), no el contenido de los arreglos:

```{code} java
:caption: Comparación de referencias vs contenido

int[] a = {1, 2, 3};
int[] b = {1, 2, 3};  // Mismo contenido, pero otro arreglo en memoria
int[] c = a;           // Alias: misma referencia que 'a'

System.out.println(a == b);  // false: diferentes referencias (diferentes arreglos)
System.out.println(a == c);  // true: misma referencia (mismo arreglo)
```

Aunque `a` y `b` tienen exactamente el mismo contenido `{1, 2, 3}`, son dos arreglos diferentes en memoria, por lo que `a == b` es `false`.

Para comparar el **contenido** de dos arreglos (verificar si tienen los mismos elementos), usá `Arrays.equals()`:

```{code} java
:caption: Comparación de contenido con Arrays.equals

import java.util.Arrays;

int[] a = {1, 2, 3};
int[] b = {1, 2, 3};
int[] d = {1, 2, 4};

System.out.println(Arrays.equals(a, b));  // true: mismo contenido
System.out.println(Arrays.equals(a, d));  // false: contenido diferente
```

`Arrays.equals()` compara elemento por elemento y retorna `true` solo si:
1. Ambos arreglos tienen la misma longitud.
2. Cada elemento en la posición `i` de un arreglo es igual al elemento en la posición `i` del otro.

(copia-de-arreglos)=
### Copia de Arreglos

Para crear una **copia independiente** de un arreglo (donde modificar uno no afecte al otro), tenés varias opciones:

```{code} java
:caption: Diferentes formas de copiar un arreglo

import java.util.Arrays;

int[] original = {1, 2, 3, 4, 5};

// Opción 1: Arrays.copyOf (recomendada)
// Copia todos los elementos a un nuevo arreglo
int[] copia1 = Arrays.copyOf(original, original.length);

// Opción 2: clone() (método heredado de Object)
int[] copia2 = original.clone();

// Opción 3: Copia manual con lazo
int[] copia3 = new int[original.length];
for (int i = 0; i < original.length; i = i + 1) {
    copia3[i] = original[i];
}

// Opción 4: System.arraycopy (más bajo nivel, más rápida para arreglos grandes)
int[] copia4 = new int[original.length];
System.arraycopy(original, 0, copia4, 0, original.length);

// Ahora las copias son independientes
copia1[0] = 100;
System.out.println(original[0]);  // 1 (no afectado)
System.out.println(copia1[0]);    // 100
```

**`Arrays.copyOf(arreglo, nuevoTamaño)`** también permite cambiar el tamaño:

```{code} java
:caption: Copia con cambio de tamaño

int[] original = {1, 2, 3};

// Copia más grande: los nuevos espacios se llenan con 0
int[] masGrande = Arrays.copyOf(original, 5);  // {1, 2, 3, 0, 0}

// Copia más pequeña: se trunca
int[] masPequeno = Arrays.copyOf(original, 2);  // {1, 2}
```

**`Arrays.copyOfRange(arreglo, desde, hasta)`** copia una porción:

```{code} java
:caption: Copia de un rango

int[] original = {10, 20, 30, 40, 50};

// Copia desde índice 1 hasta índice 4 (sin incluir el 4)
int[] porcion = Arrays.copyOfRange(original, 1, 4);  // {20, 30, 40}
```

:::{tip} Comparación con C
En C usarías `memcpy()` o un lazo manual:

```c
// En C:
int copia[5];
memcpy(copia, original, 5 * sizeof(int));
```

```java
// En Java:
int[] copia = Arrays.copyOf(original, original.length);
```

Java ofrece métodos de más alto nivel que son más seguros (no podés especificar tamaños incorrectos que causen buffer overflow).
:::

(arreglos-y-metodos)=
## Arreglos y Métodos

Los arreglos interactúan con los métodos de formas específicas que es importante entender para escribir código correcto.

(pasaje-de-arreglos-a-metodos)=
### Pasaje de Arreglos a Métodos

Cuando pasás un arreglo a un método, se pasa la **referencia** (la dirección de memoria), no una copia del arreglo. Esto tiene dos consecuencias importantes:

1. **Es eficiente**: no se copia todo el contenido del arreglo, solo la referencia (unos pocos bytes).
2. **El método puede modificar el original**: cualquier cambio que haga el método a los elementos será visible después de que el método termine.

```{code} java
:caption: Pasaje de arreglo a método

public static void duplicarElementos(int[] arr) {
    for (int i = 0; i < arr.length; i = i + 1) {
        arr[i] = arr[i] * 2;
    }
}

public static void main(String[] args) {
    int[] numeros = {1, 2, 3, 4, 5};
    
    System.out.println("Antes: " + Arrays.toString(numeros));  // [1, 2, 3, 4, 5]
    
    duplicarElementos(numeros);
    
    System.out.println("Después: " + Arrays.toString(numeros)); // [2, 4, 6, 8, 10]
    // ¡El arreglo original fue modificado!
}
```

:::{important} Efecto Secundario en Métodos
Cuando un método recibe un arreglo y modifica sus elementos, esos cambios son visibles fuera del método. Esto se llama **efecto secundario** (_side effect_). 

**Buenas prácticas:**
- Documentá si el método modifica el arreglo que recibe.
- Si no querés modificar el original, trabajá sobre una copia.
- Considerá retornar un nuevo arreglo en lugar de modificar el existente.
:::

:::{note} Comparación con C
El comportamiento es similar a C, donde los arreglos se pasan por referencia (técnicamente, se pasa un puntero al primer elemento):

```c
// En C:
void duplicar(int arr[], int size) {
    for (int i = 0; i < size; i++) {
        arr[i] *= 2;  // Modifica el arreglo original
    }
}
```

La diferencia es que en Java no necesitás pasar el tamaño como parámetro adicional.
:::

(retorno-de-arreglos)=
### Retorno de Arreglos

Un método puede crear un arreglo internamente y retornarlo. El método retorna la **referencia** al arreglo creado, que vive en el _heap_ y sobrevive después de que el método termine.

```{code} java
:caption: Método que retorna un arreglo

public static int[] crearSecuencia(int inicio, int cantidad) {
    int[] resultado = new int[cantidad];  // Se crea en el heap
    for (int i = 0; i < cantidad; i = i + 1) {
        resultado[i] = inicio + i;
    }
    return resultado;  // Retorna la referencia al arreglo
}

public static void main(String[] args) {
    int[] secuencia = crearSecuencia(10, 5);
    // secuencia es {10, 11, 12, 13, 14}
    System.out.println(Arrays.toString(secuencia));
}
```

:::{note} Comparación con C
En C, retornar un arreglo local es peligroso (comportamiento indefinido) porque el arreglo se aloja en el stack y se destruye al terminar la función:

```c
// ❌ En C: PELIGROSO - Comportamiento indefinido
int* crearArreglo() {
    int arr[5] = {1, 2, 3, 4, 5};  // En el stack
    return arr;  // ¡La memoria se libera al terminar!
}
```

En Java esto nunca es un problema porque los arreglos siempre se crean en el _heap_ con `new`, y el _garbage collector_ se asegura de que no se liberen mientras haya referencias a ellos.
:::

(patron-metodo-que-calcula-sin-modificar)=
### Patrón: Método que Calcula sin Modificar

Para evitar efectos secundarios y hacer el código más predecible, podés crear métodos que retornen un nuevo arreglo en lugar de modificar el original. Este estilo se llama **programación sin efectos secundarios** o estilo **funcional**:

```{code} java
:caption: Método sin efecto secundario

public static int[] duplicar(int[] original) {
    // Crear un NUEVO arreglo para el resultado
    int[] resultado = new int[original.length];
    
    // Copiar los valores duplicados al nuevo arreglo
    for (int i = 0; i < original.length; i = i + 1) {
        resultado[i] = original[i] * 2;
    }
    
    return resultado;  // Retorna el nuevo arreglo
}

public static void main(String[] args) {
    int[] numeros = {1, 2, 3};
    int[] duplicados = duplicar(numeros);
    
    System.out.println(Arrays.toString(numeros));     // [1, 2, 3] - sin cambios
    System.out.println(Arrays.toString(duplicados));  // [2, 4, 6] - nuevo arreglo
}
```

**Ventajas de este patrón:**
- El arreglo original no se modifica, evitando sorpresas.
- El código es más fácil de entender y testear.
- Es seguro llamar al método desde cualquier parte del código.

**Desventaja:**
- Usa más memoria porque crea un nuevo arreglo.

(reasignacion-de-la-referencia-dentro-del-metodo)=
### Reasignación de la Referencia dentro del Método

Un detalle importante: un método **no puede cambiar a qué apunta una referencia fuera del método**. Reasignar el parámetro solo afecta la copia local de la referencia:

```{code} java
:caption: Reasignar la referencia no afecta afuera

public static void intentarReemplazar(int[] arr) {
    arr = new int[]{100, 200, 300};  // Solo cambia la copia local
    // El arreglo original sigue intacto
}

public static void main(String[] args) {
    int[] datos = {1, 2, 3};
    intentarReemplazar(datos);
    System.out.println(Arrays.toString(datos));  // [1, 2, 3] - sin cambios
}
```

El parámetro `arr` es una **copia de la referencia**. Modificar el contenido del arreglo (`arr[0] = 999`) sí afecta al original, pero reasignar `arr` a otro arreglo solo cambia la copia local.

(arreglos-multidimensionales)=
## Arreglos Multidimensionales

Java permite crear **arreglos de arreglos**, que se usan comúnmente para representar estructuras bidimensionales como matrices, tableros de juego, imágenes, etc.

(concepto-arreglo-de-arreglos)=
### Concepto: Arreglo de Arreglos

Técnicamente, Java no tiene "arreglos multidimensionales" verdaderos como otros lenguajes. Lo que tiene son **arreglos cuyos elementos son otros arreglos**. Una matriz `int[][]` es un arreglo de `int[]`.

```{figure} 09/matriz_memoria.svg
:label: fig-matriz-memoria
:align: center
:width: 85%

Representación de una matriz en memoria: un arreglo principal contiene referencias a arreglos individuales (las filas).
```

Esta arquitectura tiene implicaciones importantes:
- Cada fila es un arreglo independiente en memoria.
- Las filas pueden tener diferentes tamaños (arreglos irregulares).
- Acceder a un elemento requiere dos accesos a memoria (primero la fila, luego el elemento).

(declaracion-y-creacion-de-matrices)=
### Declaración y Creación de Matrices

```{code} java
:caption: Matriz bidimensional rectangular

int[][] matriz = new int[3][4];  // 3 filas, 4 columnas

// Acceso: matriz[fila][columna]
matriz[0][0] = 1;   // Primera fila, primera columna
matriz[2][3] = 12;  // Tercera fila (índice 2), cuarta columna (índice 3)

// Dimensiones
int filas = matriz.length;        // 3 (cantidad de filas)
int columnas = matriz[0].length;  // 4 (cantidad de columnas en la fila 0)
```

La expresión `new int[3][4]` crea:
1. Un arreglo de 3 referencias (las filas).
2. Tres arreglos de 4 enteros cada uno (el contenido de cada fila).
3. Todos los elementos se inicializan a 0.

:::{note} Comparación con C
La declaración es similar a C, pero la semántica es diferente:

```c
// En C: matriz contigua en memoria (3×4 = 12 enteros seguidos)
int matriz[3][4];
```

```java
// En Java: arreglo de referencias a arreglos separados
int[][] matriz = new int[3][4];
```

En C, toda la matriz está en un bloque contiguo de memoria. En Java, cada fila es un arreglo separado, lo que permite filas de diferentes tamaños.
:::

(inicializacion-de-matrices)=
### Inicialización de Matrices

Al igual que con arreglos unidimensionales, podés inicializar una matriz directamente con valores:

```{code} java
:caption: Inicialización directa de matriz

int[][] matriz = {
    {1, 2, 3, 4},     // Fila 0: 4 columnas
    {5, 6, 7, 8},     // Fila 1: 4 columnas
    {9, 10, 11, 12}   // Fila 2: 4 columnas
};

// Acceso a elementos específicos
int valor = matriz[1][2];  // Fila 1, columna 2 → 7
```

El compilador deduce las dimensiones automáticamente (3 filas × 4 columnas en este caso).

(recorrido-de-matrices)=
### Recorrido de Matrices

Para procesar todos los elementos de una matriz, usás **lazos anidados**: un lazo externo para las filas y uno interno para las columnas.

```{code} java
:caption: Recorrido de matriz con for anidado

int[][] matriz = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

int filas = matriz.length;         // 3 filas
int columnas = matriz[0].length;   // 3 columnas (asumiendo matriz rectangular)

// Recorrido fila por fila
for (int fila = 0; fila < filas; fila = fila + 1) {
    for (int col = 0; col < columnas; col = col + 1) {
        System.out.print(matriz[fila][col] + "\t");
    }
    System.out.println();  // Nueva línea después de cada fila
}
// Salida:
// 1    2    3
// 4    5    6
// 7    8    9
```

:::{tip} Recorrido más robusto
Si la matriz puede ser irregular (filas de diferente tamaño), usá `matriz[fila].length` en el lazo interno:

```java
for (int fila = 0; fila < matriz.length; fila = fila + 1) {
    for (int col = 0; col < matriz[fila].length; col = col + 1) {  // Tamaño de ESTA fila
        System.out.print(matriz[fila][col] + " ");
    }
    System.out.println();
}
```
:::

(recorrido-for-each-matrices)=
### Recorrido con for-each

También podés usar `for-each` anidados para recorrer matrices:

```{code} java
:caption: Recorrido de matriz con for-each

int[][] matriz = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

for (int[] fila : matriz) {           // Cada fila es un int[]
    for (int elemento : fila) {        // Cada elemento de la fila
        System.out.print(elemento + " ");
    }
    System.out.println();
}
```

(matrices-irregulares-jagged-arrays)=
### Matrices Irregulares (Jagged Arrays)

Como cada fila es un arreglo independiente, pueden tener diferentes tamaños. Esto se llama **arreglo irregular** o **jagged array**:

```{code} java
:caption: Matriz irregular

// Crear solo el arreglo de filas (sin especificar columnas)
int[][] triangulo = new int[3][];

// Crear cada fila con tamaño diferente
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

**Casos de uso de matrices irregulares:**
- Triángulo de Pascal (como arriba)
- Representar estructuras donde las filas tienen naturalmente tamaños diferentes
- Ahorrar memoria cuando muchas filas serían pequeñas

:::{warning} Cuidado al recorrer matrices irregulares
Si la matriz es irregular, usar `matriz[0].length` para todas las filas causará errores. Siempre usá `matriz[fila].length` para obtener el tamaño de cada fila específica.
:::

(operaciones-comunes-con-arreglos)=
## Operaciones Comunes con Arreglos

Esta sección presenta algoritmos fundamentales que todo programador debe conocer. Estas operaciones son la base para resolver problemas más complejos.

(encontrar-maximo-y-minimo)=
### Encontrar Máximo y Mínimo

El patrón para encontrar el máximo es: asumir que el primer elemento es el máximo, y luego recorrer el resto comparando:

```{code} java
:caption: Buscar máximo en un arreglo

public static int maximo(int[] arr) {
    // Precondición: el arreglo no debe estar vacío
    int max = arr[0];  // Asumir que el primero es el máximo
    
    for (int i = 1; i < arr.length; i = i + 1) {  // Empezar desde 1
        if (arr[i] > max) {
            max = arr[i];  // Encontramos uno mayor
        }
    }
    return max;
}

// Uso:
int[] datos = {3, 7, 2, 9, 1};
int mayor = maximo(datos);  // 9
```

El algoritmo para el mínimo es idéntico, solo cambiás `>` por `<`:

```{code} java
:caption: Buscar mínimo en un arreglo

public static int minimo(int[] arr) {
    int min = arr[0];
    for (int i = 1; i < arr.length; i = i + 1) {
        if (arr[i] < min) {
            min = arr[i];
        }
    }
    return min;
}
```

:::{note} ¿Por qué empezamos con arr[0]?
Empezar con `arr[0]` en lugar de un valor arbitrario (como `Integer.MIN_VALUE`) tiene ventajas:
- Funciona para cualquier tipo de dato.
- El resultado siempre es un elemento del arreglo.
- Es más claro conceptualmente.

La desventaja es que debés asegurarte de que el arreglo no esté vacío.
:::

(sumar-elementos)=
### Sumar Elementos

El patrón acumulador: inicializar una variable a 0 y sumar cada elemento:

```{code} java
:caption: Sumar todos los elementos

public static int sumar(int[] arr) {
    int suma = 0;  // Acumulador inicializado en el elemento neutro de la suma
    
    for (int i = 0; i < arr.length; i = i + 1) {
        suma = suma + arr[i];
    }
    return suma;
}

// También se puede usar for-each (más legible para este caso):
public static int sumarConForEach(int[] arr) {
    int suma = 0;
    for (int elemento : arr) {
        suma = suma + elemento;
    }
    return suma;
}
```

(calcular-promedio)=
### Calcular Promedio

El promedio es la suma dividida por la cantidad de elementos. Importante: hacer la división con punto flotante:

```{code} java
:caption: Calcular promedio

public static double promedio(int[] arr) {
    int suma = sumar(arr);
    // El cast a double evita división entera
    return (double) suma / arr.length;
}

// Uso:
int[] notas = {7, 8, 6, 9, 10};
double prom = promedio(notas);  // 8.0
```

(contar-elementos-que-cumplen-una-condicion)=
### Contar Elementos que Cumplen una Condición

Otro patrón fundamental: recorrer contando cuántos elementos satisfacen cierta condición:

```{code} java
:caption: Contar elementos que cumplen condición

public static int contarPares(int[] arr) {
    int contador = 0;
    for (int i = 0; i < arr.length; i = i + 1) {
        if (arr[i] % 2 == 0) {  // Condición: ser par
            contador = contador + 1;
        }
    }
    return contador;
}

// Más general: contar mayores a un umbral
public static int contarMayoresQue(int[] arr, int umbral) {
    int contador = 0;
    for (int i = 0; i < arr.length; i = i + 1) {
        if (arr[i] > umbral) {
            contador = contador + 1;
        }
    }
    return contador;
}
```

(verificar-si-todos-alguno-cumple-una-condicion)=
### Verificar si Todos/Alguno Cumple una Condición

Dos patrones relacionados con el conteo:

```{code} java
:caption: Verificar condiciones universales y existenciales

// ¿TODOS los elementos son positivos?
public static boolean todosSonPositivos(int[] arr) {
    boolean todosCumplen = true;
    int i = 0;
    
    while (i < arr.length && todosCumplen) {
        if (arr[i] <= 0) {
            todosCumplen = false;  // Encontramos uno que no cumple
        }
        i = i + 1;
    }
    return todosCumplen;
}

// ¿ALGÚN elemento es negativo?
public static boolean existeNegativo(int[] arr) {
    boolean encontrado = false;
    int i = 0;
    
    while (i < arr.length && !encontrado) {
        if (arr[i] < 0) {
            encontrado = true;
        }
        i = i + 1;
    }
    return encontrado;
}
```

(invertir-arreglo)=
### Invertir Arreglo

Intercambiar elementos desde los extremos hacia el centro:

```{code} java
:caption: Invertir arreglo en su lugar

public static void invertir(int[] arr) {
    int izq = 0;
    int der = arr.length - 1;
    
    while (izq < der) {
        // Intercambiar arr[izq] y arr[der]
        int temp = arr[izq];
        arr[izq] = arr[der];
        arr[der] = temp;
        
        izq = izq + 1;
        der = der - 1;
    }
}

// Ejemplo:
// Antes:  {1, 2, 3, 4, 5}
// Después: {5, 4, 3, 2, 1}
```

Este algoritmo es **in-place**: modifica el arreglo original sin crear uno nuevo, usando solo O(1) memoria adicional (la variable `temp`).

(clase-arrays-utilidades)=
## Clase `Arrays` (Utilidades)

Java provee la clase `java.util.Arrays` con métodos de utilidad para trabajar con arreglos. Estos métodos están optimizados y son más seguros que implementaciones manuales.

Para usar esta clase, debés importarla al inicio del archivo:

```java
import java.util.Arrays;
```

(metodos-principales)=
### Métodos Principales

```{code} java
:caption: Métodos de la clase Arrays

import java.util.Arrays;

int[] numeros = {5, 2, 8, 1, 9};

// ========== ORDENAMIENTO ==========
Arrays.sort(numeros);  
// numeros ahora es {1, 2, 5, 8, 9}
// Usa un algoritmo eficiente (Dual-Pivot Quicksort)

// ========== CONVERSIÓN A STRING ==========
String texto = Arrays.toString(numeros);
System.out.println(texto);  // Imprime: [1, 2, 5, 8, 9]
// Muy útil para debugging

// ========== LLENAR CON UN VALOR ==========
int[] ceros = new int[5];
Arrays.fill(ceros, 0);  // {0, 0, 0, 0, 0}

int[] unos = new int[3];
Arrays.fill(unos, 1);   // {1, 1, 1}

// ========== BÚSQUEDA BINARIA ==========
// IMPORTANTE: el arreglo DEBE estar ordenado
int[] ordenado = {1, 2, 5, 8, 9};
int pos = Arrays.binarySearch(ordenado, 5);  // Retorna 2 (índice de 5)
int noEncontrado = Arrays.binarySearch(ordenado, 7);  // Retorna negativo

// ========== COMPARACIÓN DE CONTENIDO ==========
int[] a = {1, 2, 3};
int[] b = {1, 2, 3};
int[] c = {1, 2, 4};
boolean iguales1 = Arrays.equals(a, b);  // true
boolean iguales2 = Arrays.equals(a, c);  // false

// ========== COPIA ==========
int[] original = {1, 2, 3, 4, 5};
int[] copiaCompleta = Arrays.copyOf(original, original.length);  // {1, 2, 3, 4, 5}
int[] copiaParcial = Arrays.copyOf(original, 3);                 // {1, 2, 3}
int[] copiaExtendida = Arrays.copyOf(original, 7);               // {1, 2, 3, 4, 5, 0, 0}

// Copiar un rango específico
int[] rango = Arrays.copyOfRange(original, 1, 4);  // {2, 3, 4} (índices 1, 2, 3)
```

(resumen-de-metodos-de-arrays)=
### Resumen de Métodos de Arrays

| Método | Descripción | Precondición |
|:---|:---|:---|
| `sort(arr)` | Ordena el arreglo de menor a mayor | - |
| `toString(arr)` | Convierte a String legible | - |
| `fill(arr, valor)` | Llena todo el arreglo con el valor | - |
| `equals(arr1, arr2)` | Compara contenido elemento por elemento | - |
| `copyOf(arr, len)` | Copia a un nuevo arreglo de tamaño `len` | - |
| `copyOfRange(arr, from, to)` | Copia el rango `[from, to)` | `from <= to` |
| `binarySearch(arr, val)` | Busca `val` y retorna su índice | **Arreglo ordenado** |

:::{warning} binarySearch requiere arreglo ordenado
El método `binarySearch` **solo funciona correctamente si el arreglo está ordenado**. Si el arreglo no está ordenado, el resultado es indefinido (puede retornar cualquier cosa).

```java
int[] desordenado = {5, 2, 8, 1};
int pos = Arrays.binarySearch(desordenado, 2);  // ❌ Resultado impredecible

int[] ordenado = {1, 2, 5, 8};
int pos2 = Arrays.binarySearch(ordenado, 2);    // ✅ Retorna 1 (índice correcto)
```
:::

(ejercicios-de-aplicacion)=
## Ejercicios de Aplicación

Los siguientes ejercicios te ayudarán a consolidar los conceptos de arreglos. Intentá resolverlos antes de ver las soluciones.

(ejercicios-de-comprension)=
### Ejercicios de Comprensión

````{exercise}
:label: ej-arreglo-modificacion
¿Qué imprime el siguiente código? Explicá por qué paso a paso.

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
:class: dropdown

Imprime `[999, 2, 3]`.

**Explicación paso a paso:**

1. Se crea `datos` apuntando a un arreglo `{1, 2, 3}` en el heap.
2. Se llama a `modificar(datos)`, pasando una **copia de la referencia**.
3. Dentro del método, `arr` apunta al mismo arreglo que `datos`.
4. `arr[0] = 999` modifica el arreglo original (ahora es `{999, 2, 3}`).
5. `arr = new int[]{100, 200, 300}` crea un NUEVO arreglo y hace que `arr` apunte a él. Pero esto solo cambia la variable local `arr`, no afecta a `datos`.
6. El método termina. El nuevo arreglo `{100, 200, 300}` se pierde (no hay referencias a él).
7. `datos` sigue apuntando al arreglo original, que ahora es `{999, 2, 3}`.

**Moraleja:** Podés modificar el **contenido** del arreglo desde un método, pero no podés cambiar a qué arreglo apunta la referencia original.
```

(ejercicios-de-programacion)=
### Ejercicios de Programación

```{exercise}
:label: ej-matriz-suma-filas
Escribí un método `sumarFilas` que reciba una matriz de enteros y retorne un arreglo con la suma de cada fila.

Por ejemplo, para la matriz `{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}`, debe retornar `{6, 15, 24}`.
```

````{solution} ej-matriz-suma-filas
:class: dropdown

```java
public static int[] sumarFilas(int[][] matriz) {
    // Un resultado por cada fila
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

```{exercise}
:label: ej-matriz-irregular
Dada una matriz `int[][] m`, escribí un método `esRectangular` que retorne `true` si todas las filas tienen la misma longitud, o `false` si alguna fila tiene tamaño diferente (matriz irregular).
```

````{solution} ej-matriz-irregular
:class: dropdown

```java
public static boolean esRectangular(int[][] m) {
    // Caso especial: matriz vacía o nula
    if (m == null || m.length == 0) {
        return true;  // Consideramos que una matriz vacía es rectangular
    }
    
    // Todas las filas deben tener esta cantidad de columnas
    int columnasEsperadas = m[0].length;
    
    // Usar patrón de bandera
    boolean esRect = true;
    int i = 1;  // Empezamos desde la fila 1 (ya vimos la 0)
    
    while (i < m.length && esRect) {
        // Verificar que la fila no sea null y tenga el tamaño correcto
        if (m[i] == null || m[i].length != columnasEsperadas) {
            esRect = false;
        }
        i = i + 1;
    }
    
    return esRect;
}

// Ejemplos:
// esRectangular({{1, 2}, {3, 4}, {5, 6}}) → true
// esRectangular({{1}, {2, 3}, {4, 5, 6}}) → false
```
````

```{exercise}
:label: ej-rotar-arreglo
Escribí un método `rotarDerecha` que rote los elementos de un arreglo una posición hacia la derecha. El último elemento debe pasar a ser el primero.

Ejemplo: `{1, 2, 3, 4, 5}` → `{5, 1, 2, 3, 4}`
```

````{solution} ej-rotar-arreglo
:class: dropdown

```java
public static void rotarDerecha(int[] arr) {
    // Caso especial: arreglo vacío o de un elemento
    if (arr.length <= 1) {
        return;  // Nada que rotar
    }
    
    // Guardar el último elemento (se "caería" del arreglo)
    int ultimo = arr[arr.length - 1];
    
    // Desplazar todos los elementos una posición a la derecha
    // Empezamos desde el final para no sobrescribir datos
    for (int i = arr.length - 1; i > 0; i = i - 1) {
        arr[i] = arr[i - 1];
    }
    
    // Colocar el último elemento guardado al principio
    arr[0] = ultimo;
}

// Traza para {1, 2, 3, 4, 5}:
// 1. Guardamos: ultimo = 5
// 2. Desplazamos: {1, 2, 3, 4, 4} → {1, 2, 3, 3, 4} → {1, 2, 2, 3, 4} → {1, 1, 2, 3, 4}
// 3. Ponemos ultimo: {5, 1, 2, 3, 4}
```
````

```{exercise}
:label: ej-buscar-posicion
Escribí un método `buscarPosicion` que reciba un arreglo de enteros y un valor a buscar, y retorne la posición de la primera ocurrencia del valor. Si no se encuentra, debe retornar -1. Usá el patrón de búsqueda con bandera.
```

````{solution} ej-buscar-posicion
:class: dropdown

```java
public static int buscarPosicion(int[] arr, int buscado) {
    boolean encontrado = false;
    int posicion = -1;  // Valor que indica "no encontrado"
    int i = 0;
    
    while (i < arr.length && !encontrado) {
        if (arr[i] == buscado) {
            encontrado = true;
            posicion = i;
        }
        i = i + 1;
    }
    
    return posicion;
}

// Ejemplos:
// buscarPosicion({10, 20, 30, 40}, 30) → 2
// buscarPosicion({10, 20, 30, 40}, 25) → -1
// buscarPosicion({5, 5, 5}, 5) → 0 (primera ocurrencia)
```
````

```{exercise}
:label: ej-eliminar-duplicados
Escribí un método que reciba un arreglo **ordenado** de enteros y retorne un nuevo arreglo sin elementos duplicados.

Por ejemplo: `{1, 1, 2, 2, 2, 3, 4, 4}` → `{1, 2, 3, 4}`
```

````{solution} ej-eliminar-duplicados
:class: dropdown

```java
public static int[] eliminarDuplicados(int[] ordenado) {
    if (ordenado.length == 0) {
        return new int[0];
    }
    
    // Primero contamos cuántos elementos únicos hay
    int unicos = 1;  // El primero siempre es único
    for (int i = 1; i < ordenado.length; i = i + 1) {
        if (ordenado[i] != ordenado[i - 1]) {
            unicos = unicos + 1;
        }
    }
    
    // Creamos el arreglo resultado con el tamaño exacto
    int[] resultado = new int[unicos];
    resultado[0] = ordenado[0];
    int indiceResultado = 1;
    
    // Copiamos solo los elementos diferentes al anterior
    for (int i = 1; i < ordenado.length; i = i + 1) {
        if (ordenado[i] != ordenado[i - 1]) {
            resultado[indiceResultado] = ordenado[i];
            indiceResultado = indiceResultado + 1;
        }
    }
    
    return resultado;
}

// El algoritmo funciona porque el arreglo está ordenado:
// los duplicados siempre están consecutivos.
```
````

(resumen)=
## Resumen

Los arreglos en Java son estructuras fundamentales que todo programador debe dominar. Los puntos clave a recordar:

| Concepto | Descripción |
|:---|:---|
| **Declaración** | `tipo[] nombre;` crea una referencia, no el arreglo |
| **Creación** | `new tipo[tamaño]` reserva memoria e inicializa valores |
| **Tamaño fijo** | Una vez creado, el tamaño no puede cambiar |
| **Índices** | Comienzan en 0, hasta `length - 1` |
| **Verificación de límites** | Java lanza excepción si el índice es inválido |
| **Referencias** | Las variables de arreglo son referencias, no copias |
| **Pasaje a métodos** | Se pasa la referencia; el método puede modificar el contenido |

(comparacion-java-vs-c)=
### Comparación Java vs C

| Aspecto | C | Java |
|:---|:---|:---|
| Tamaño del arreglo | Debe pasarse como parámetro | Disponible con `.length` |
| Verificación de límites | No hay (comportamiento indefinido) | Automática (excepción) |
| Gestión de memoria | Manual (`malloc`/`free`) | Automática (garbage collector) |
| Valores iniciales | Basura | Valores por defecto (0, false, null) |
| Retornar arreglos | Peligroso si es local | Seguro (siempre en heap) |

(09-referencias-bibliograficas)=
## Referencias Bibliográficas

- **Schildt, H.** (2022). *Java: A Beginner's Guide* (9na ed.). McGraw Hill. Capítulo 5: Arrays.
- **Liang, Y. D.** (2017). *Introduction to Java Programming and Data Structures* (11va ed.). Pearson. Capítulos 7 y 8.
- **Horstmann, C.** (2019). *Core Java Volume I: Fundamentals* (11va ed.). Pearson. Sección 3.10.
- **Oracle Corporation.** (2023). *The Java Language Specification*. [Chapter 10: Arrays](https://docs.oracle.com/javase/specs/jls/se21/html/jls-10.html).
- **Oracle Corporation.** (2023). *Java API Documentation*. [java.util.Arrays](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html).

