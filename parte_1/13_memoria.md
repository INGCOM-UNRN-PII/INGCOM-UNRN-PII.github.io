---
title: "Memoria, Referencias y Mutabilidad"
description: Estudio técnico sobre el modelo de memoria de Java, pasaje de argumentos, referencias, efectos secundarios y manejo de Strings.
label: memoria
---

(memoria-referencias-y-mutabilidad)=
# Memoria, Referencias y Mutabilidad

Este capítulo es fundamental para entender cómo funciona Java "por dentro". Si venís de C, muchos conceptos te resultarán familiares pero con diferencias importantes. Vamos a explicar todo desde cero, asumiendo que sabés programar en C pero no conocés los detalles internos de Java.

:::{note} ¿Por qué es importante entender la memoria?
Muchos bugs difíciles de encontrar están relacionados con cómo Java maneja la memoria: variables que "misteriosamente" cambian de valor, comparaciones que dan resultados inesperados, o el temido `NullPointerException`. Entender el modelo de memoria te ayuda a evitar estos problemas.
:::

:::{note} Hoja de ruta del capítulo

**Objetivo.** Comprender las ideas centrales de **Memoria, Referencias y Mutabilidad** y usarlas como base para el resto del recorrido.

**Prerrequisitos.** Conviene haber leído [el material inmediatamente anterior](12_archivos.md) para llegar con el hilo de la parte fresco.

**Desarrollo.** El desarrollo del capítulo aparece en las secciones que siguen. Conviene recorrerlas en orden y volver al resumen antes de pasar al siguiente tema.
:::

(modelo-de-memoria-de-la-jvm)=
## Modelo de Memoria de la JVM

Cuando ejecutás un programa en C, el sistema operativo le asigna memoria directamente. En Java, tu programa corre dentro de la **JVM** (Java Virtual Machine), que actúa como intermediario. La JVM divide la memoria en varias regiones, pero las dos más importantes para nosotros son el **Stack** y el **Heap**.

```{figure} 13/stack_heap_modelo.svg
:label: fig-stack-heap-modelo
:width: 95%

Modelo de memoria de la JVM: Stack (variables locales y referencias) vs Heap (objetos y arreglos).
```

(stack-pila-de-ejecucion)=
### Stack (Pila de Ejecución)

El **Stack** funciona exactamente igual que en C: es una región de memoria que crece y decrece automáticamente cuando llamás y retornás de métodos.

**Características del Stack:**
- Sigue el modelo **LIFO** (_Last In, First Out_): lo último que entra es lo primero que sale.
- Cada vez que llamás a un método, se crea un nuevo **frame** (marco) en el Stack.
- Cuando el método termina, su frame se elimina automáticamente.
- Cada hilo de ejecución tiene su propio Stack privado.
- Es **muy rápido** porque la asignación/liberación es automática.

**¿Qué se almacena en el Stack?**
- Variables locales de los métodos (declaradas dentro del método)
- Parámetros de los métodos
- Valores de tipos primitivos (`int`, `double`, `boolean`, `char`, etc.)
- **Referencias** a objetos (la flecha que apunta al objeto, no el objeto en sí)

En C, cuando declarás `int x = 5;` dentro de una función, la variable `x` vive en el stack. En Java es exactamente igual:

```{code} java
:caption: Variables en el Stack

public static void ejemplo() {
    int edad = 25;           // 'edad' está en el Stack, contiene el valor 25
    double precio = 19.99;   // 'precio' está en el Stack, contiene el valor 19.99
    boolean activo = true;   // 'activo' está en el Stack, contiene true
    char letra = 'A';        // 'letra' está en el Stack, contiene 'A'
    
    int[] numeros;           // 'numeros' está en el Stack, pero solo es una REFERENCIA
                             // Por ahora vale null (no apunta a ningún arreglo)
}
```

(heap-monticulo)=
### Heap (Montículo)

El **Heap** es la región de memoria donde viven los objetos y arreglos. A diferencia del Stack:

- Es **compartido** por toda la aplicación (todos los hilos).
- Los objetos en el Heap **no se eliminan automáticamente** cuando termina un método.
- La JVM tiene un **Garbage Collector** (recolector de basura) que elimina objetos que ya no se usan.
- Es más lento que el Stack, pero permite almacenar estructuras de datos dinámicas.

**¿Qué se almacena en el Heap?**
- Todos los objetos creados con la palabra clave `new`
- Todos los arreglos (incluso de tipos primitivos como `int[]`)
- Todos los Strings

**Analogía con C:** En C, cuando usás `malloc()` para reservar memoria dinámica, esa memoria está en el heap. En Java, cada vez que usás `new`, estás reservando memoria en el Heap. La diferencia es que en Java no necesitás `free()` — el Garbage Collector se encarga.

```{code} java
:caption: Objetos en el Heap

public static void ejemplo() {
    // La variable 'numeros' está en el Stack (es una referencia)
    // El arreglo real {1, 2, 3} está en el Heap
    int[] numeros = new int[]{1, 2, 3};
    
    // La variable 'texto' está en el Stack (es una referencia)
    // El objeto String "Hola" está en el Heap
    String texto = "Hola";
}
```

(visualizacion-del-modelo)=
### Visualización del Modelo

Imaginá que el Stack es tu escritorio y el Heap es un depósito grande. En tu escritorio (Stack) tenés notas con direcciones (referencias) que te dicen dónde están las cosas en el depósito (Heap).

```
┌─────────────────────────┐     ┌─────────────────────────────────┐
│         STACK           │     │             HEAP                │
│   (tu escritorio)       │     │        (el depósito)            │
├─────────────────────────┤     ├─────────────────────────────────┤
│                         │     │                                 │
│  edad: 25               │     │   ┌─────────────────────┐       │
│  (valor directo)        │     │   │ Arreglo int[]       │       │
│                         │     │   │ [1] [2] [3]         │       │
│  precio: 19.99          │     │   │ (dirección: 0x1234) │       │
│  (valor directo)        │     │   └─────────────────────┘       │
│                         │     │            ▲                    │
│  numeros: 0x1234 ───────────────────────────┘                   │
│  (referencia/dirección) │     │                                 │
│                         │     │   ┌─────────────────────┐       │
│  texto: 0x5678 ─────────────────► │ String "Hola"       │       │
│  (referencia/dirección) │     │   │ (dirección: 0x5678) │       │
│                         │     │   └─────────────────────┘       │
└─────────────────────────┘     └─────────────────────────────────┘
```

(que-pasa-cuando-termina-un-metodo)=
### ¿Qué pasa cuando termina un método?

Cuando un método termina:
1. Su frame en el Stack se elimina (las variables locales desaparecen)
2. Los objetos en el Heap **no se eliminan inmediatamente**
3. Si ninguna referencia apunta a un objeto, el Garbage Collector eventualmente lo eliminará

```{code} java
:caption: Ciclo de vida de variables y objetos

public static int[] crearArreglo() {
    int[] arr = new int[]{1, 2, 3};  // arr en Stack, arreglo en Heap
    return arr;  // Retornamos la referencia (la dirección)
}
// Cuando termina el método, 'arr' desaparece del Stack
// PERO el arreglo sigue vivo en el Heap si alguien guardó la referencia

public static void main(String[] args) {
    int[] resultado = crearArreglo();  // 'resultado' guarda la referencia
    // El arreglo sigue existiendo porque 'resultado' apunta a él
    System.out.println(resultado[0]);  // Funciona: imprime 1
}
```

(referencias-los-punteros-seguros-de-java)=
## Referencias: Los "Punteros Seguros" de Java

Una **referencia** en Java es conceptualmente similar a un puntero en C, pero con restricciones de seguridad:

| Característica | Puntero en C | Referencia en Java |
|:---|:---|:---|
| Almacena | Dirección de memoria | Dirección de memoria |
| Aritmética de punteros | Permitida (`p++`, `p + 5`) | **Prohibida** |
| Acceso a direcciones | Directo (`printf("%p", p)`) | **No permitido** |
| Liberación de memoria | Manual (`free(p)`) | Automática (Garbage Collector) |
| Valor para "nada" | `NULL` | `null` |

**¿Por qué Java prohíbe la aritmética de punteros?**
- **Seguridad:** En C, un error con punteros puede leer/escribir memoria que no te pertenece. En Java, esto es imposible.
- **Portabilidad:** La JVM puede mover objetos en memoria sin que tu código se entere.
- **Simplicidad:** No necesitás preocuparte por el tamaño exacto de los tipos para calcular offsets.

(tipos-primitivos-vs-tipos-referencia)=
### Tipos Primitivos vs Tipos Referencia

Esta es una distinción fundamental en Java:

**Tipos Primitivos:** `byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean`
- La variable contiene **el valor directamente**.
- Se almacenan completamente en el Stack.
- No pueden ser `null`.

**Tipos Referencia:** Arreglos, Strings, y cualquier cosa creada con `new`
- La variable contiene **una dirección de memoria** (referencia).
- La referencia está en el Stack, pero el objeto está en el Heap.
- Pueden ser `null`.

```{code} java
:caption: Primitivos vs Referencias — comportamiento crucial

// === PRIMITIVOS: copian el VALOR ===
int a = 10;        // 'a' contiene el valor 10
int b = a;         // 'b' contiene una COPIA INDEPENDIENTE del valor 10
b = 20;            // Cambiar 'b' NO afecta a 'a'
System.out.println(a);  // Imprime: 10 (sin cambios)
System.out.println(b);  // Imprime: 20

// === REFERENCIAS: copian la DIRECCIÓN ===
int[] arr1 = {1, 2, 3};  // 'arr1' contiene dirección del arreglo (ej: 0x1234)
int[] arr2 = arr1;       // 'arr2' contiene COPIA de la dirección (también 0x1234)
                         // ¡AMBOS APUNTAN AL MISMO ARREGLO!
arr2[0] = 999;           // Modifica el arreglo a través de arr2
System.out.println(arr1[0]);  // Imprime: 999 (¡arr1 también ve el cambio!)
```

**Visualización:**

```
PRIMITIVOS (copian valor):           REFERENCIAS (copian dirección):
                                     
Stack:                               Stack:           Heap:
┌────────┐                           ┌────────┐      ┌─────────────┐
│ a: 10  │ (valor propio)            │arr1:0x1234──►│ [1] [2] [3] │
├────────┤                           ├────────┤      │ (un solo    │
│ b: 20  │ (valor propio)            │arr2:0x1234──►│  arreglo)   │
└────────┘                           └────────┘      └─────────────┘
(independientes)                     (apuntan al mismo lugar)
```

(el-valor-null)=
## El Valor `null`

Una referencia puede no apuntar a ningún objeto. Esto se representa con el valor especial `null`. Es el equivalente a `NULL` en C.

```{code} java
:caption: Referencias null

int[] numeros = null;  // La referencia existe pero no apunta a ningún arreglo
String texto = null;   // La referencia existe pero no apunta a ningún String

// La variable existe en el Stack, pero su valor es "nada"
// Es como tener un papel con la dirección borrada
```

(valores-por-defecto)=
### Valores por defecto

Cuando declarás una variable sin inicializarla, Java le asigna un valor por defecto:

| Tipo | Valor por defecto |
|:---|:---|
| `int`, `short`, `byte`, `long` | `0` |
| `float`, `double` | `0.0` |
| `char` | `'\0'` (carácter nulo) |
| `boolean` | `false` |
| Cualquier referencia | `null` |

:::{warning}
Las variables **locales** (dentro de métodos) **no se inicializan automáticamente**. Java te obliga a inicializarlas antes de usarlas.

```java
public static void ejemplo() {
    int x;
    System.out.println(x);  // ❌ Error de compilación: variable might not have been initialized
    
    int y = 0;
    System.out.println(y);  // ✅ OK
}
```
:::

(nullpointerexception)=
### NullPointerException

Esta es una de las excepciones más comunes en Java (y uno de los errores más comunes en programación en general). Ocurre cuando intentás usar una referencia que es `null`.

```{code} java
:caption: Causas de NullPointerException

int[] numeros = null;
String texto = null;

// Todas estas líneas causan NullPointerException:
int len = numeros.length;     // ❌ Intentar acceder a .length de null
int valor = numeros[0];       // ❌ Intentar acceder a un índice de null
int largo = texto.length();   // ❌ Intentar llamar método en null
char c = texto.charAt(0);     // ❌ Intentar llamar método en null
```

**¿Qué pasa internamente?**
Cuando escribís `numeros.length`, Java necesita ir a la dirección guardada en `numeros` para buscar el campo `length`. Si `numeros` es `null`, no hay dirección válida, y la JVM lanza la excepción.

:::{tip} Comparativa con C
En C, acceder a un puntero `NULL` causa un **segmentation fault** (el sistema operativo mata tu programa). En Java, se lanza una `NullPointerException`, que es una excepción que podés capturar y manejar:

```java
try {
    int len = numeros.length;
} catch (NullPointerException e) {
    System.out.println("El arreglo era null");
}
```
:::

(prevenir-nullpointerexception)=
### Prevenir NullPointerException

La forma más simple es verificar antes de usar:

```{code} java
:caption: Verificación defensiva

public static void procesarArreglo(int[] arr) {
    // Verificar ANTES de usar
    if (arr == null) {
        System.out.println("Error: el arreglo es null");
        return;  // Salir del método
    }
    
    // Ahora es seguro usar arr
    System.out.println("Tamaño: " + arr.length);
    for (int i = 0; i < arr.length; i = i + 1) {
        System.out.println(arr[i]);
    }
}
```

(comparacion-de-referencias-vs-equals)=
## Comparación de Referencias: `==` vs `equals()`

El operador `==` tiene comportamiento diferente según el tipo de dato:

**Para primitivos:** Compara los **valores**. ¿Son iguales 5 y 5? Sí.

**Para referencias:** Compara las **direcciones de memoria**. ¿Apuntan al mismo objeto?

```{code} java
:caption: El problema de == con referencias

// Dos arreglos con el MISMO CONTENIDO pero en DIFERENTES lugares del Heap
int[] a = {1, 2, 3};  // Arreglo en dirección 0x1234
int[] b = {1, 2, 3};  // Otro arreglo en dirección 0x5678 (diferente!)
int[] c = a;          // 'c' apunta a la MISMA dirección que 'a': 0x1234

System.out.println(a == b);  // false — diferentes direcciones
System.out.println(a == c);  // true — misma dirección

// a y b tienen el MISMO CONTENIDO pero == dice false
// porque == pregunta "¿son el mismo objeto?" no "¿tienen el mismo contenido?"
```

**Visualización:**

```
Stack:              Heap:
                    
a: 0x1234 ──────► [1][2][3]  (objeto en 0x1234)
                    
b: 0x5678 ──────► [1][2][3]  (objeto diferente en 0x5678)
                    
c: 0x1234 ──────────────────► (apunta al mismo que 'a')

a == b → ¿0x1234 == 0x5678? → NO
a == c → ¿0x1234 == 0x1234? → SÍ
```

(comparar-contenido-de-arreglos)=
### Comparar contenido de arreglos

Para comparar si dos arreglos tienen el mismo contenido, usá `Arrays.equals()`:

```{code} java
:caption: Comparación correcta de contenido

import java.util.Arrays;

int[] a = {1, 2, 3};
int[] b = {1, 2, 3};

System.out.println(a == b);              // false (diferentes objetos)
System.out.println(Arrays.equals(a, b)); // true (mismo contenido)
```

`Arrays.equals()` compara elemento por elemento. Para arreglos multidimensionales, usá `Arrays.deepEquals()`.

(pasaje-de-argumentos-a-metodos)=
## Pasaje de Argumentos a Métodos

Esta es una de las fuentes de confusión más comunes. La regla en Java es simple:

**Java siempre pasa por valor.**

Pero "valor" significa cosas diferentes:
- Para **primitivos**: el valor es el dato (ej: 10, 3.14, true)
- Para **referencias**: el valor es la **dirección** del objeto

````{mermaid}

flowchart LR
    subgraph Primitivo["Tipo Primitivo"]
        P1[int x = 10] --> P2[método recibe<br/>COPIA del valor 10]
        P2 --> P3[Cambios NO afectan<br/>variable original]
    end
    
    subgraph Referencia["Tipo Referencia"]
        R1["int[] arr"] --> R2[método recibe<br/>COPIA de la dirección]
        R2 --> R3[Modificar contenido<br/>SÍ afecta original]
        R2 --> R4[Reasignar referencia<br/>NO afecta original]
    end
    
    style P1 fill:#c8e6c9,stroke:#2e7d32
    style P2 fill:#fff3e0,stroke:#f57c00
    style P3 fill:#ffe0e0,stroke:#eb2141
    
    style R1 fill:#bbdefb,stroke:#1565c0
    style R2 fill:#fff3e0,stroke:#f57c00
    style R3 fill:#c8e6c9,stroke:#2e7d32
    style R4 fill:#ffe0e0,stroke:#eb2141
````

(pasaje-de-primitivos)=
### Pasaje de Primitivos

Se pasa una **copia del valor**. El método trabaja con su propia copia independiente.

```{code} java
:caption: Pasaje de primitivos — el original NO cambia

public static void duplicar(int numero) {
    // 'numero' es una COPIA del valor original
    numero = numero * 2;  // Solo modifica la copia local
    System.out.println("Dentro del método: " + numero);  // 20
}

public static void main(String[] args) {
    int valor = 10;
    duplicar(valor);
    System.out.println("Fuera del método: " + valor);  // 10 (sin cambios)
}
```

**¿Qué pasó?**
1. `main` tiene `valor = 10` en su frame del Stack
2. Al llamar a `duplicar(valor)`, se crea nuevo frame con `numero = 10` (copia)
3. `duplicar` modifica su copia: `numero = 20`
4. `duplicar` termina, su frame se elimina
5. `main` sigue teniendo `valor = 10`

**En C sería igual:**
```c
void duplicar(int numero) {
    numero = numero * 2;  // Solo modifica copia local
}

int main() {
    int valor = 10;
    duplicar(valor);
    printf("%d\n", valor);  // 10 (sin cambios)
}
```

(pasaje-de-referencias)=
### Pasaje de Referencias

Se pasa una **copia de la referencia** (la dirección). El método puede modificar el contenido del objeto, pero **no puede** cambiar a qué apunta la referencia original.

```{code} java
:caption: Pasaje de referencias — contenido SÍ puede cambiar

public static void modificarContenido(int[] arr) {
    arr[0] = 999;  // Modifica el arreglo original ✅
    // 'arr' tiene una COPIA de la dirección, pero apunta al MISMO arreglo
}

public static void main(String[] args) {
    int[] numeros = {1, 2, 3};
    
    System.out.println("Antes: " + numeros[0]);  // 1
    modificarContenido(numeros);
    System.out.println("Después: " + numeros[0]);  // 999 (¡modificado!)
}
```

```{code} java
:caption: Pasaje de referencias — reasignar NO afecta el original

public static void intentarReasignar(int[] arr) {
    // 'arr' es una COPIA de la referencia
    arr = new int[]{100, 200, 300};  // Solo cambia la COPIA local
    // El 'numeros' original sigue apuntando al arreglo viejo
}

public static void main(String[] args) {
    int[] numeros = {1, 2, 3};
    
    System.out.println("Antes: " + numeros[0]);  // 1
    intentarReasignar(numeros);
    System.out.println("Después: " + numeros[0]);  // 1 (sin cambios)
}
```

(visualizacion-del-pasaje-de-referencias)=
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
│ numeros: ─────────────►│ [1] [2] [3] │ ← ambos apuntan aquí
└──────────────┘         └─────────────┘
                               ▲
Stack (método)                 │
┌──────────────┐               │
│ arr: ────────────────────────┘  (copia de la dirección)
└──────────────┘

Cuando hacemos arr[0] = 999, modificamos el arreglo compartido.
```

```
Si intentamos reasignar arr = new int[]{100, 200, 300}:

Stack (main)              Heap
┌──────────────┐         ┌─────────────┐
│ numeros: ─────────────►│ [1] [2] [3] │ ← numeros sigue aquí
└──────────────┘         └─────────────┘
                         
Stack (método)           ┌─────────────────┐
┌──────────────┐         │ [100] [200] [300] │
│ arr: ─────────────────►│ (nuevo arreglo)   │
└──────────────┘         └─────────────────┘

'arr' apunta a un nuevo arreglo, pero 'numeros' sigue apuntando al viejo.
Cuando el método termina, el nuevo arreglo queda sin referencias y será eliminado.
```

(13-comparacion-con-c)=
### Comparación con C

En C, para modificar una variable del llamador, necesitás pasar un puntero:

```c
// En C: para modificar el valor original, usás punteros
void duplicar(int* numero) {
    *numero = *numero * 2;  // Modificás a través del puntero
}

int main() {
    int valor = 10;
    duplicar(&valor);  // Pasás la dirección
    printf("%d\n", valor);  // 20 (modificado)
}
```

En Java no podés hacer esto con primitivos (no hay `&` ni `*`). Si necesitás que un método "retorne" múltiples valores, usá:
1. Un arreglo
2. Retornar un valor y modificar un arreglo recibido
3. (Más adelante) Crear un objeto que contenga los valores

(efectos-secundarios-side-effects)=
## Efectos Secundarios (Side Effects)

Un **efecto secundario** ocurre cuando un método modifica estado fuera de su propio alcance. Esto incluye:

1. Modificar el contenido de un arreglo recibido como parámetro
2. Modificar variables globales (variables `static` de clase)
3. Realizar entrada/salida (imprimir, leer archivos, etc.)

```{code} java
:caption: Método con efecto secundario

// ⚠️ Este método tiene efecto secundario: modifica el arreglo recibido
public static void ordenar(int[] arr) {
    // Algoritmo de ordenamiento que modifica arr directamente
    for (int i = 0; i < arr.length - 1; i = i + 1) {
        for (int j = 0; j < arr.length - 1 - i; j = j + 1) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

// El efecto secundario es visible fuera del método
int[] datos = {5, 2, 8, 1};
ordenar(datos);
// datos ahora está ordenado: {1, 2, 5, 8}
```

(funciones-puras-vs-metodos-con-efectos)=
### Funciones Puras vs Métodos con Efectos

Una **función pura**:
- Siempre retorna el mismo resultado para los mismos argumentos
- No modifica nada fuera de sí misma (no modifica parámetros, no usa/modifica variables globales)
- No tiene efectos secundarios
- Su resultado solo depende de sus argumentos

```{code} java
:caption: Función pura vs método con efecto

// ✅ Función pura: no modifica nada, retorna un nuevo arreglo
public static int[] ordenarNuevo(int[] original) {
    // Crear una copia del arreglo
    int[] copia = new int[original.length];
    for (int i = 0; i < original.length; i = i + 1) {
        copia[i] = original[i];
    }
    
    // Ordenar la copia (no el original)
    for (int i = 0; i < copia.length - 1; i = i + 1) {
        for (int j = 0; j < copia.length - 1 - i; j = j + 1) {
            if (copia[j] > copia[j + 1]) {
                int temp = copia[j];
                copia[j] = copia[j + 1];
                copia[j + 1] = temp;
            }
        }
    }
    
    return copia;  // Retornar la copia ordenada
}

// ⚠️ Método con efecto secundario: modifica el arreglo recibido
public static void ordenarEnLugar(int[] arr) {
    // Ordena directamente 'arr', modificando el original
    // ... código de ordenamiento ...
}

// Uso:
int[] datos = {5, 2, 8, 1};

int[] datosOrdenados = ordenarNuevo(datos);
// datos sigue siendo {5, 2, 8, 1}
// datosOrdenados es {1, 2, 5, 8}

ordenarEnLugar(datos);
// datos ahora es {1, 2, 5, 8}
```

**¿Cuándo usar cada uno?**
- **Funciones puras** son más fáciles de razonar y testear (no hay sorpresas).
- **Métodos con efectos** son más eficientes en memoria (no crean copias).

:::{tip} Documentar Efectos Secundarios
Si un método modifica sus argumentos, documentalo claramente. Los usuarios del método deben saber si su arreglo original será alterado.

```java
/**
 * Ordena el arreglo recibido de menor a mayor.
 * ADVERTENCIA: Este método MODIFICA el arreglo original.
 * 
 * @param arr arreglo a ordenar (será modificado)
 */
public static void ordenar(int[] arr) {
    // ...
}
```
:::

(inmutabilidad)=
## Inmutabilidad

Un valor u objeto **inmutable** es aquel cuyo estado no puede cambiar después de ser creado.

**Tipos primitivos:** Son inmutables por naturaleza. Cuando hacés `x = x + 1`, no estás "cambiando" el valor 5 a 6; estás reemplazando el valor de `x` con un nuevo valor.

**Arreglos:** Son **mutables**. Podés cambiar sus elementos: `arr[0] = 999`.

**Strings:** Son **inmutables**. No podés cambiar el contenido de un String existente.

(la-palabra-clave-final)=
### La Palabra Clave `final`

`final` previene la **reasignación** de una variable. Pero si la variable es una referencia, **no** previene la modificación del contenido del objeto.

```{code} java
:caption: final con primitivos y referencias

// Con primitivos: no se puede reasignar
final int constante = 10;
// constante = 20;  // ❌ Error de compilación

// Con referencias: no se puede reasignar, PERO se puede modificar contenido
final int[] numeros = {1, 2, 3};

numeros[0] = 999;            // ✅ Permitido: modificar CONTENIDO
System.out.println(numeros[0]);  // 999

// numeros = new int[5];     // ❌ Error: no se puede REASIGNAR la referencia
```

**Analogía:** `final` significa "esta variable siempre apuntará a este objeto". Pero no dice nada sobre qué hay dentro del objeto.

(estrategias-para-prevenir-modificaciones)=
### Estrategias para Prevenir Modificaciones

**1. Copia defensiva al recibir:**

Si tu método no quiere que cambios externos afecten su trabajo, hacé una copia:

```{code} java
:caption: Copia defensiva al recibir

public static double calcularPromedio(int[] datos) {
    // Crear copia para trabajar de forma segura
    int[] copia = new int[datos.length];
    for (int i = 0; i < datos.length; i = i + 1) {
        copia[i] = datos[i];
    }
    
    // Trabajar con la copia
    int suma = 0;
    for (int i = 0; i < copia.length; i = i + 1) {
        suma = suma + copia[i];
    }
    return (double) suma / copia.length;
}
```

**2. Retornar copia en lugar del original:**

Si tenés datos internos que no querés que modifiquen, retorná una copia:

```{code} java
:caption: Retornar copia protege datos internos

// Variable de módulo (simulando datos internos)
static int[] datosSecretos = {100, 200, 300};

// ❌ Mal: expone los datos internos
public static int[] obtenerDatosMal() {
    return datosSecretos;  // El llamador puede modificar nuestros datos
}

// ✅ Bien: retorna una copia
public static int[] obtenerDatosBien() {
    int[] copia = new int[datosSecretos.length];
    for (int i = 0; i < datosSecretos.length; i = i + 1) {
        copia[i] = datosSecretos[i];
    }
    return copia;  // El llamador solo puede modificar la copia
}
```

(strings-inmutabilidad-especial)=
## Strings: Inmutabilidad especial

En C, un "string" es simplemente un arreglo de `char` terminado en `'\0'`. Podés modificar cualquier carácter directamente:

```c
// En C: strings son arreglos modificables
char texto[] = "Hola";
texto[0] = 'M';  // Ahora es "Mola"
```

En Java, la clase `String` es **inmutable por diseño**. Una vez que creás un String, su contenido **nunca puede cambiar**. Esto tiene varias implicaciones importantes.

(por-que-los-strings-son-inmutables)=
### ¿Por qué los Strings son inmutables?

Java diseñó los Strings inmutables por varias razones:

1. **Seguridad:** Si pasás un String a un método, sabés que no te lo van a modificar.
2. **Thread-safety:** Múltiples hilos pueden usar el mismo String sin problemas de sincronización.
3. **Optimización (String Pool):** Java puede reutilizar Strings idénticos.
4. **Uso como claves:** Los Strings pueden usarse como claves en tablas hash de forma segura.

(string-pool)=
### String Pool

Para optimizar memoria, Java mantiene un **String Pool** (también llamado String Intern Pool): una caché especial de Strings únicos en el Heap.

Cuando escribís un literal de String (texto entre comillas), Java primero busca en el pool. Si ya existe ese String, te da una referencia al existente. Si no existe, lo crea en el pool.

```{figure} 13/string_pool.svg
:label: fig-string-pool
:width: 90%

String Pool e inmutabilidad: cómo Java optimiza el uso de Strings en memoria.
```

```{code} java
:caption: Cómo funciona el String Pool

String s1 = "Hola";      // Busca "Hola" en el pool, no existe, lo crea
String s2 = "Hola";      // Busca "Hola" en el pool, ya existe, retorna la misma referencia
String s3 = new String("Hola");  // Fuerza creación de NUEVO objeto fuera del pool

System.out.println(s1 == s2);  // true (misma referencia del pool)
System.out.println(s1 == s3);  // false (s3 es objeto diferente, no está en el pool)
System.out.println(s1.equals(s3));  // true (mismo contenido)
```

**Visualización:**

```
Stack:                    Heap:
                          
s1: 0x1234 ─────┐         ┌──────────────────────┐
               ├────────► │ String Pool          │
s2: 0x1234 ─────┘         │ ┌─────────────────┐  │
                          │ │ "Hola" (0x1234) │  │
                          │ └─────────────────┘  │
                          └──────────────────────┘
                          
s3: 0x5678 ──────────────► "Hola" (0x5678) ← objeto separado, fuera del pool
```

**¿Cuándo usar `new String()`?**
Casi nunca. Usar `new String("texto")` crea un objeto innecesario fuera del pool. El uso principal es en casos muy específicos de performance o cuando necesitás explícitamente un objeto nuevo (raro).

(metodos-de-string-no-modifican-el-original)=
### Métodos de String NO Modifican el Original

Esta es la consecuencia más importante de la inmutabilidad. Todos los métodos de String que "transforman" el texto en realidad **crean y retornan un nuevo String**:

```{code} java
:caption: Error común: olvidar guardar el resultado

String original = "Hola";

// ❌ ERROR COMÚN: llamar al método pero no guardar el resultado
original.toUpperCase();  // Esto CREA un nuevo String "HOLA" pero nadie lo guarda
System.out.println(original);  // Sigue imprimiendo "Hola"

// ✅ CORRECTO: guardar el resultado en una variable
String mayusculas = original.toUpperCase();  // Guardar el nuevo String
System.out.println(mayusculas);  // "HOLA"
System.out.println(original);    // Sigue siendo "Hola" (inmutable)

// ✅ También válido: reasignar a la misma variable
String texto = "  espacios  ";
texto = texto.trim();  // La variable 'texto' ahora apunta al nuevo String
System.out.println(texto);  // "espacios"
```

(metodos-comunes-de-string)=
### Métodos Comunes de String

| Método | Descripción | Retorna | Ejemplo |
|:---|:---|:---|:---|
| `length()` | Cantidad de caracteres | `int` | `"Hola".length()` → `4` |
| `charAt(i)` | Carácter en posición i | `char` | `"Hola".charAt(1)` → `'o'` |
| `substring(inicio, fin)` | Subcadena desde inicio hasta fin-1 | `String` | `"Hola".substring(1, 3)` → `"ol"` |
| `substring(inicio)` | Subcadena desde inicio hasta el final | `String` | `"Hola".substring(2)` → `"la"` |
| `toUpperCase()` | Convertir a mayúsculas | `String` | `"Hola".toUpperCase()` → `"HOLA"` |
| `toLowerCase()` | Convertir a minúsculas | `String` | `"HOLA".toLowerCase()` → `"hola"` |
| `trim()` | Quitar espacios al inicio y fin | `String` | `" Hola ".trim()` → `"Hola"` |
| `replace(old, new)` | Reemplazar caracteres o subcadenas | `String` | `"Hola".replace('o', '0')` → `"H0la"` |
| `contains(s)` | ¿Contiene la subcadena? | `boolean` | `"Hola".contains("ol")` → `true` |
| `startsWith(s)` | ¿Empieza con...? | `boolean` | `"Hola".startsWith("Ho")` → `true` |
| `endsWith(s)` | ¿Termina con...? | `boolean` | `"Hola".endsWith("la")` → `true` |
| `equals(s)` | ¿Mismo contenido? | `boolean` | `"Hola".equals("Hola")` → `true` |
| `equalsIgnoreCase(s)` | ¿Mismo contenido ignorando mayúsculas? | `boolean` | `"HOLA".equalsIgnoreCase("hola")` → `true` |
| `indexOf(s)` | Posición de primera ocurrencia | `int` | `"Hola".indexOf("la")` → `2` |
| `isEmpty()` | ¿Es cadena vacía? | `boolean` | `"".isEmpty()` → `true` |
| `split(regex)` | Dividir por delimitador | `String[]` | `"a,b,c".split(",")` → `["a", "b", "c"]` |

```{code} java
:caption: Ejemplos prácticos de métodos de String

String texto = "  Hola Mundo  ";

// Información sobre el String
System.out.println(texto.length());        // 14 (incluye espacios)
System.out.println(texto.charAt(2));       // 'H' (índice 2, tercer carácter)
System.out.println(texto.isEmpty());       // false

// Transformaciones (todas retornan NUEVO String)
System.out.println(texto.trim());          // "Hola Mundo"
System.out.println(texto.toUpperCase());   // "  HOLA MUNDO  "
System.out.println(texto.substring(2, 6)); // "Hola"
System.out.println(texto.replace(' ', '-')); // "--Hola-Mundo--"

// Búsquedas
System.out.println(texto.contains("Mun")); // true
System.out.println(texto.indexOf("Mundo")); // 7
System.out.println(texto.indexOf("xyz"));  // -1 (no encontrado)
System.out.println(texto.startsWith("  H")); // true

// División
String csv = "manzana,banana,naranja";
String[] frutas = csv.split(",");
// frutas = ["manzana", "banana", "naranja"]
```

(comparacion-de-strings)=
### Comparación de Strings

:::{warning} Nunca usar == para comparar contenido de Strings
El operador `==` compara **referencias** (direcciones de memoria), no contenido. Usá siempre `equals()`.

```java
String a = "hola";
String b = new String("hola");
String c = "hola";

System.out.println(a == b);       // false (diferentes objetos)
System.out.println(a == c);       // true (mismo objeto del pool)
System.out.println(a.equals(b));  // true (mismo CONTENIDO)
```

Este comportamiento confuso es una trampa común. Siempre usá `equals()` para comparar Strings.
:::

**Comparación ignorando mayúsculas:**

```{code} java
:caption: Comparación case-insensitive

String entrada = "ADMIN";
String esperado = "admin";

// ❌ Mal: esto da false
if (entrada.equals(esperado)) { }

// ✅ Bien: ignora mayúsculas/minúsculas
if (entrada.equalsIgnoreCase(esperado)) { }

// ✅ También válido: convertir ambos al mismo caso
if (entrada.toLowerCase().equals(esperado.toLowerCase())) { }
```

(concatenacion-de-strings)=
### Concatenación de Strings

El operador `+` concatena Strings:

```{code} java
:caption: Concatenación básica

String nombre = "Juan";
int edad = 25;

String mensaje = "Hola, " + nombre + ". Tenés " + edad + " años.";
// mensaje = "Hola, Juan. Tenés 25 años."

// Java convierte automáticamente los no-String a String
```

**¿Qué pasa internamente con `+`?**

Cada vez que usás `+`, Java crea un nuevo objeto String con la concatenación. Para una sola concatenación está bien, pero en un lazo esto es muy ineficiente.

(stringbuilder-strings-mutables)=
## StringBuilder: Strings Mutables

Cuando necesitás construir un String de forma incremental, especialmente dentro de lazos, usá `StringBuilder`. Esta clase es similar a un String pero **mutable**: podés modificar su contenido sin crear objetos nuevos.

(el-problema-de-la-concatenacion-en-lazos)=
### El Problema de la Concatenación en Lazos

```{code} java
:caption: ❌ Concatenación ineficiente en lazo

String resultado = "";
for (int i = 0; i < 1000; i = i + 1) {
    resultado = resultado + i + ",";
    // En cada iteración:
    // 1. Java crea un nuevo String temporal
    // 2. Copia todo el contenido anterior
    // 3. Agrega los nuevos caracteres
    // 4. El String viejo queda para el Garbage Collector
}
// Total: se crean ~1000 Strings temporales
// Complejidad: O(n²) porque cada iteración copia más caracteres
```

**¿Por qué es O(n²)?**
- Iteración 1: copia 0 caracteres, agrega "0,"
- Iteración 2: copia ~2 caracteres, agrega "1,"
- Iteración 3: copia ~4 caracteres, agrega "2,"
- ...
- Iteración 1000: copia ~3000 caracteres, agrega "999,"

Total de caracteres copiados: 0 + 2 + 4 + ... + 3000 ≈ n²/2

(la-solucion-stringbuilder)=
### La Solución: StringBuilder

`StringBuilder` mantiene un buffer interno que crece cuando es necesario. Agregar caracteres es O(1) amortizado.

```{code} java
:caption: ✅ StringBuilder eficiente

StringBuilder sb = new StringBuilder();  // Crear el builder

for (int i = 0; i < 1000; i = i + 1) {
    sb.append(i);     // Agregar al buffer interno
    sb.append(",");   // No crea objetos nuevos
}

String resultado = sb.toString();  // Convertir a String al final
// Total: 1 StringBuilder, 1 String final
// Complejidad: O(n)
```

(metodos-de-stringbuilder)=
### Métodos de StringBuilder

| Método | Descripción | Modifica el StringBuilder |
|:---|:---|:---|
| `append(x)` | Agrega al final | Sí |
| `insert(pos, x)` | Inserta en posición | Sí |
| `delete(inicio, fin)` | Elimina rango | Sí |
| `deleteCharAt(pos)` | Elimina carácter en posición | Sí |
| `reverse()` | Invierte el contenido | Sí |
| `setCharAt(pos, c)` | Cambia carácter en posición | Sí |
| `toString()` | Convierte a String inmutable | No |
| `length()` | Longitud actual | No |

```{code} java
:caption: Uso completo de StringBuilder

StringBuilder sb = new StringBuilder();

// Construir texto
sb.append("Hola");
sb.append(" ");
sb.append("Mundo");
System.out.println(sb.toString());  // "Hola Mundo"

// Insertar en el medio
sb.insert(5, "Java ");
System.out.println(sb.toString());  // "Hola Java Mundo"

// Modificar un carácter
sb.setCharAt(0, 'h');
System.out.println(sb.toString());  // "hola Java Mundo"

// Eliminar un rango
sb.delete(5, 10);  // Elimina "Java "
System.out.println(sb.toString());  // "hola Mundo"

// Invertir
sb.reverse();
System.out.println(sb.toString());  // "odnuM aloh"
```

(cuando-usar-string-vs-stringbuilder)=
### ¿Cuándo usar String vs StringBuilder?

| Situación | Usar |
|:---|:---|
| Texto fijo o pocas concatenaciones | `String` |
| Concatenación en lazo | `StringBuilder` |
| Construir texto paso a paso | `StringBuilder` |
| Pasar texto entre métodos | `String` |
| Clave de diccionario/mapa | `String` |

(resumen-reglas-clave)=
## Resumen: Reglas Clave

Para cerrar, estas son las reglas fundamentales que debés recordar:

1. **Stack vs Heap**: Las variables locales y referencias viven en el Stack; los objetos y arreglos viven en el Heap.

2. **Primitivos vs Referencias**: Los primitivos contienen el valor; las referencias contienen direcciones.

3. **Pasaje por valor**: Java siempre pasa por valor. Para primitivos, es el valor; para referencias, es la dirección (copia de la referencia).

4. **Modificar contenido vs reasignar**: Un método puede modificar el contenido de un arreglo recibido, pero no puede hacer que la variable original apunte a otro lugar.

5. **`==` vs `equals()`**: Para referencias, `==` compara direcciones; `equals()` compara contenido.

6. **`null`**: Significa "no apunta a nada". Usarlo causa `NullPointerException`.

7. **Arreglos son mutables**: Modificar `arr[i]` cambia el arreglo para todos los que tienen una referencia.

8. **Strings son inmutables**: Los métodos retornan nuevos Strings; el original nunca cambia.

9. **StringBuilder para lazos**: Usá `StringBuilder` cuando concatenás en lazos para evitar O(n²).

10. **`final` no es inmutabilidad**: `final` previene reasignación, no modificación de contenido.

(ejercicios-de-aplicacion-13)=
## Ejercicios de Aplicación

````{exercise}
:label: ej-memoria-que-imprime
¿Qué imprime el siguiente código? Explicá por qué.

```java
int[] a = {1, 2, 3};
int[] b = a;
b[0] = 100;
System.out.println(a[0] + " " + b[0]);
```
````

```{solution} ej-memoria-que-imprime
:class: dropdown
Imprime `100 100`.

Al hacer `int[] b = a`, no se copia el arreglo, solo se copia la **referencia**. Tanto `a` como `b` apuntan al mismo arreglo en el Heap. Cuando modificamos `b[0]`, estamos modificando el único arreglo que existe, por lo que `a[0]` también muestra el cambio.
```

````{exercise}
:label: ej-string-inmutable
¿Qué imprime el siguiente código?

```java
String s = "hola";
s.toUpperCase();
System.out.println(s);
```
````

````{solution} ej-string-inmutable
:class: dropdown
Imprime `hola` (en minúsculas).

Los Strings son inmutables. El método `toUpperCase()` **retorna un nuevo String** con el contenido en mayúsculas, pero no modifica el String original. El resultado retornado se pierde porque no lo asignamos a ninguna variable.

Para obtener el resultado en mayúsculas:
```java
String s = "hola";
s = s.toUpperCase();  // Reasignar a s
System.out.println(s);  // Imprime: HOLA
```
````

```{exercise}
:label: ej-metodo-efectos
Escribí un método `invertir(int[] arr)` que invierta el arreglo **sin crear uno nuevo** (efecto secundario). Luego escribí otro método `invertirNuevo(int[] arr)` que retorne un nuevo arreglo invertido **sin modificar el original** (función pura).
```

````{solution} ej-metodo-efectos
:class: dropdown
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

```{exercise}
:label: ej-stringbuilder-uso
Escribí un método que reciba un arreglo de Strings y retorne un único `String` con todos los elementos separados por comas, usando `StringBuilder`.
```

````{solution} ej-stringbuilder-uso
:class: dropdown

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

````{exercise}
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
````

````{solution} ej-null-check
:class: dropdown
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

(13-referencias-bibliograficas)=
## Referencias Bibliográficas

- **Schildt, H.** (2022). *Java: A Beginner's Guide* (9na ed.). McGraw Hill.
- **Liang, Y. D.** (2017). *Introduction to Java Programming and Data Structures* (11va ed.). Pearson.
- **Bloch, J.** (2018). *Effective Java* (3ra ed.). Addison-Wesley Professional.
- **Oracle Corporation.** (2023). *The Java Language Specification*.

:::seealso
- {ref}`regla-0xE001` - Comparación de objetos con equals vs ==.
- {ref}`regla-0x3002` - Manejo de NullPointerException.
:::

## Próximo paso

Para seguir, conviene pasar a [el material siguiente](../parte_2/indice.md), donde el recorrido continúa sobre esta base.
