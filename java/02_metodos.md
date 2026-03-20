---
title: "\"Funciones\" en Java"
description: Estudio técnico sobre la abstracción de procedimientos, gestión de la pila de llamadas y mecanismos de pasaje de parámetros.
---

# Métodos en Java

Un **método** en Java es la unidad fundamental de comportamiento y abstracción. A diferencia de C, donde existen funciones independientes, en Java todo comportamiento debe estar encapsulado dentro de una clase. Esta restricción refuerza la cohesión y el diseño orientado a objetos desde la base del lenguaje.

:::{note} Terminología: Función vs. Método
En C hablamos de **funciones**. En Java, el término correcto es **método** porque siempre pertenece a una clase. Sin embargo, conceptualmente son equivalentes: un bloque de código reutilizable que puede recibir datos (parámetros), procesarlos y opcionalmente devolver un resultado.

A lo largo de este curso, usaremos ambos términos de forma intercambiable cuando el contexto sea claro, pero recordá que en Java técnicamente son **métodos**.
:::

## Anatomía de un Método

### Sintaxis General

```{code} java
:caption: Estructura completa de un método

[modificadores] tipoRetorno nombreMetodo([parámetros]) [throws excepciones] {
    // cuerpo del método
    [return valor;]
}
```

Donde:
- **modificadores** (opcional): `public`, `private`, `protected`, `static`, `final`, etc.
- **tipoRetorno** (obligatorio): El tipo de dato que devuelve, o `void` si no devuelve nada
- **nombreMetodo** (obligatorio): Identificador en camelCase
- **parámetros** (opcional): Lista de parámetros separados por coma
- **throws** (opcional): Excepciones que puede lanzar
- **cuerpo**: Las instrucciones a ejecutar
- **return** (obligatorio si no es `void`): El valor a devolver

### Ejemplos de Declaración

```{code} java
:caption: Método sin parámetros ni retorno

public static void saludar() {
    System.out.println("¡Hola!");
}
```

```{code} java
:caption: Método con parámetros y retorno

public static int sumar(int a, int b) {
    int resultado = a + b;
    return resultado;
}
```

```{code} java
:caption: Método con un solo parámetro

public static double calcularCuadrado(double numero) {
    return numero * numero;
}
```

```{code} java
:caption: Método que retorna boolean

public static boolean esPar(int numero) {
    return numero % 2 == 0;
}
```

## Invocación de Métodos

Para ejecutar un método, se lo **invoca** (o llama) usando su nombre seguido de paréntesis con los argumentos necesarios.

```{code} java
:caption: Invocación de métodos

// Método sin retorno
saludar();

// Método con retorno - guardar resultado
int suma = sumar(5, 3);  // suma = 8

// Método con retorno - usar directamente
System.out.println(sumar(10, 20));  // Imprime: 30

// Usar retorno en expresión
double area = calcularCuadrado(5.0) * 3.14159;

// Usar en condición
if (esPar(numero)) {
    System.out.println("Es par");
}
```

:::{important} Diferencia entre Parámetro y Argumento
- **Parámetro**: Variable declarada en la definición del método (`int a, int b`)
- **Argumento**: Valor concreto pasado al invocar el método (`sumar(5, 3)`)

Los parámetros son las "ranuras" que esperan valores; los argumentos son los valores que se colocan en esas ranuras.
:::

## Tipos de Retorno

### Métodos `void`

Un método `void` no devuelve ningún valor. Se usa para acciones que tienen un efecto pero no producen un resultado.

```{code} java
:caption: Método void

public static void imprimirLinea(int cantidad) {
    for (int i = 0; i < cantidad; i = i + 1) {
        System.out.print("-");
    }
    System.out.println();
}
```

:::{note} Return en Métodos void
Un método `void` puede usar `return;` (sin valor) para terminar anticipadamente, pero no es obligatorio.

```java
public static void procesarEdad(int edad) {
    if (edad < 0) {
        System.out.println("Edad inválida");
        return;  // Termina el método aquí
    }
    System.out.println("Edad válida: " + edad);
}
```
:::

### Métodos con Retorno

Un método con tipo de retorno **debe** devolver un valor de ese tipo en todos los caminos de ejecución.

```{code} java
:caption: Retorno en todos los caminos

public static String clasificarNota(int nota) {
    String clasificacion;
    
    if (nota >= 90) {
        clasificacion = "Excelente";
    } else if (nota >= 70) {
        clasificacion = "Bueno";
    } else if (nota >= 50) {
        clasificacion = "Regular";
    } else {
        clasificacion = "Insuficiente";
    }
    
    return clasificacion;
}
```

:::{warning} Error de Compilación: Falta de Return
Si un camino de ejecución no tiene `return`, el compilador emitirá un error:

```java
public static int obtenerValor(boolean condicion) {
    if (condicion) {
        return 10;
    }
    // ERROR: Falta return cuando condicion es false
}
```
:::

## Parámetros y Argumentos

### Parámetros Múltiples

Un método puede recibir cualquier cantidad de parámetros, separados por comas:

```{code} java
:caption: Método con múltiples parámetros

public static double calcularIMC(double peso, double altura) {
    double imc = peso / (altura * altura);
    return imc;
}

// Invocación
double miIMC = calcularIMC(70.5, 1.75);
```

### Orden de los Argumentos

El orden de los argumentos debe coincidir con el orden de los parámetros:

```{code} java
:caption: Importancia del orden de argumentos

public static void mostrarDatos(String nombre, int edad, double salario) {
    System.out.printf("%s tiene %d años y gana $%.2f%n", nombre, edad, salario);
}

// Correcto
mostrarDatos("Ana", 30, 50000.0);

// Incorrecto - error de compilación o resultado erróneo
// mostrarDatos(30, "Ana", 50000.0);  // Error: tipos incompatibles
```

### Compatibilidad de Tipos

Java permite pasar argumentos de tipos compatibles (promoción automática):

```{code} java
:caption: Promoción automática de tipos

public static double dividir(double dividendo, double divisor) {
    return dividendo / divisor;
}

// Todas estas llamadas son válidas
double r1 = dividir(10.0, 3.0);   // double, double
double r2 = dividir(10, 3.0);     // int se promueve a double
double r3 = dividir(10.0, 3);     // int se promueve a double
double r4 = dividir(10, 3);       // ambos int se promueven a double
```

## Firma de un Método y Sobrecarga

La **firma** (_signature_) de un método es su identificador único para el compilador. Se compone estrictamente de:

1.  El **nombre** del método.
2.  El **número** y **tipo** de sus parámetros (en orden).

El tipo de retorno, los modificadores de acceso y las excepciones declaradas **no forman parte de la firma**.

### Ejemplos de Sobrecarga

```{code} java
:caption: Métodos sobrecargados válidos

// Todas estas son firmas diferentes
public static int sumar(int a, int b) {
    return a + b;
}

public static int sumar(int a, int b, int c) {
    return a + b + c;
}

public static double sumar(double a, double b) {
    return a + b;
}

public static String sumar(String a, String b) {
    return a + b;
}
```

```{code} java
:caption: Sobrecarga inválida - mismo nombre y parámetros

// ERROR DE COMPILACIÓN: misma firma
public static int calcular(int x) {
    return x * 2;
}

public static double calcular(int x) {  // Solo difiere en retorno
    return x * 2.0;
}
```

### Resolución de Sobrecarga (_Overloading_)

La sobrecarga permite que múltiples métodos compartan el mismo nombre siempre que sus firmas sean distintas. Cuando invocás un método sobrecargado, el compilador utiliza un proceso llamado _resolución de sobrecarga_ para determinar cuál ejecutar:

1.  Busca una coincidencia exacta de tipos.
2.  Si no hay coincidencia exacta, intenta promociones primitivas (ej. `int` a `long`).
3.  Si aún hay ambigüedad o falta de coincidencia, intenta el _autoboxing_ (ej. `int` a `Integer`).
4.  Finalmente, busca coincidencias con varargs o tipos genéricos.

```{code} java
:caption: Resolución de sobrecarga en acción

public static void mostrar(int x) {
    System.out.println("int: " + x);
}

public static void mostrar(double x) {
    System.out.println("double: " + x);
}

public static void mostrar(String x) {
    System.out.println("String: " + x);
}

// ¿Cuál se invoca?
mostrar(5);       // int: 5      (coincidencia exacta)
mostrar(5.0);     // double: 5.0 (coincidencia exacta)
mostrar("Hola");  // String: Hola (coincidencia exacta)
mostrar(5L);      // double: 5.0 (long se promueve a double)
```

:::{warning} Ambigüedad en Sobrecarga
Si definís `metodo(int a, long b)` y `metodo(long a, int b)`, la llamada `metodo(5, 5)` causará un error de compilación por ambigüedad, ya que ambas firmas requieren exactamente una promoción y el compilador no puede determinar cuál es "mejor".
:::

## Mecanismo de Pasaje de Parámetros

Un error común al venir de C es pensar que Java pasa objetos "por referencia". **Java siempre pasa por valor**.

### Pasaje de Tipos Primitivos

Se pasa una **copia del valor**. Cambios dentro del método no afectan a la variable original.

```{code} java
:caption: Pasaje por valor con primitivos

public static void duplicar(int numero) {
    numero = numero * 2;  // Solo modifica la copia local
    System.out.println("Dentro: " + numero);  // Imprime: 20
}

public static void main(String[] args) {
    int valor = 10;
    duplicar(valor);
    System.out.println("Fuera: " + valor);  // Imprime: 10 (sin cambios)
}
```

### Pasaje de Referencias (Tipos no Primitivos)

Se pasa una copia **del valor de la referencia** (la dirección de memoria). Es como pasar una copia de un puntero en C.

- Si modificás el _estado_ del objeto, el cambio es visible fuera del método
- Si reasignás la referencia dentro del método, esto **no afecta** a la referencia original

```{code} java
:caption: Modificar estado vs reasignar referencia

public static void modificarContenido(StringBuilder sb) {
    sb.append(" modificado");  // Cambia el objeto, visible afuera
}

public static void reasignarReferencia(StringBuilder sb) {
    sb = new StringBuilder("nuevo");  // Solo cambia la copia local
}

public static void main(String[] args) {
    StringBuilder texto = new StringBuilder("original");
    
    modificarContenido(texto);
    System.out.println(texto);  // Imprime: "original modificado"
    
    reasignarReferencia(texto);
    System.out.println(texto);  // Imprime: "original modificado" (sin cambios)
}
```

:::{important} Comparativa con C
En C, para modificar una variable externa usás punteros (`int *p`). En Java, no podés modificar la referencia externa en sí (el "puntero"), pero al recibir una copia de la dirección, podés manipular el objeto al que apunta.
:::

## Alcance de Variables (Scope)

Las variables tienen un **alcance** que determina dónde pueden ser accedidas.

### Variables Locales

Las variables declaradas dentro de un método solo existen dentro de ese método.

```{code} java
:caption: Alcance de variables locales

public static void metodo1() {
    int x = 10;  // x solo existe en metodo1
    System.out.println(x);
}

public static void metodo2() {
    // System.out.println(x);  // ERROR: x no existe aquí
    int x = 20;  // Esta es una variable diferente
    System.out.println(x);
}
```

### Variables de Bloque

Las variables declaradas dentro de un bloque (`{}`) solo existen dentro de ese bloque.

```{code} java
:caption: Alcance dentro de bloques

public static void ejemplo() {
    int a = 1;  // Visible en todo el método
    
    if (a > 0) {
        int b = 2;  // Solo visible dentro del if
        System.out.println(a + b);  // OK: ambas visibles
    }
    
    // System.out.println(b);  // ERROR: b no existe aquí
    
    for (int i = 0; i < 5; i = i + 1) {
        int c = i * 2;  // Solo visible dentro del for
    }
    
    // System.out.println(i);  // ERROR: i no existe aquí
    // System.out.println(c);  // ERROR: c no existe aquí
}
```

### Parámetros como Variables Locales

Los parámetros de un método actúan como variables locales inicializadas con los argumentos recibidos.

```{code} java
:caption: Parámetros como variables locales

public static int calcular(int valor) {
    // 'valor' es una variable local inicializada con el argumento
    valor = valor + 10;  // Modifica solo la copia local
    return valor;
}
```

## Gestión de la Pila: Stack Frames

Cada vez que se invoca un método, la JVM crea un **Stack Frame** (marco de pila) en el _stack_ del hilo actual. Este marco contiene:

- **Variables Locales**: Incluyendo los parámetros recibidos.
- **Pila de Operandos**: Donde se realizan los cálculos intermedios.
- **Datos de Retorno**: La dirección de memoria donde debe continuar la ejecución tras finalizar el método.

Cuando el método termina (vía `return` o excepción), su _frame_ es destruido y el control regresa al marco anterior.

### Ejemplo de Flujo de Ejecución

```{code} java
:caption: Seguimiento del stack de llamadas

public static void main(String[] args) {
    int resultado = metodoA(5);      // 1. Se crea frame para main
    System.out.println(resultado);   // 5. Continúa main
}                                    // 6. Se destruye frame de main

public static int metodoA(int x) {
    int y = metodoB(x + 1);          // 2. Se crea frame para metodoA
    return y * 2;                    // 4. Se destruye frame de metodoA
}

public static int metodoB(int n) {
    return n + 10;                   // 3. Se crea y destruye frame de metodoB
}
```

### Recursión y StackOverflow

La recursión utiliza este mecanismo de forma intensiva. Cada llamada recursiva añade un nuevo marco a la pila. Si la recursión es muy profunda o carece de un caso base correcto, se agota el espacio asignado al _stack_, lanzando un `StackOverflowError`.

```{code} java
:caption: Método recursivo para calcular factorial

public static long factorial(int n) {
    // Caso base: detiene la recursión
    if (n <= 1) {
        return 1;
    }
    // Caso recursivo
    return n * factorial(n - 1);
}
```

$$n! = \begin{cases} 1 & \text{si } n = 0 \\ n \times (n-1)! & \text{si } n > 0 \end{cases}$$

```{code} java
:caption: Recursión sin caso base - StackOverflowError

public static void infinito() {
    infinito();  // Sin caso base, nunca termina
    // Eventualmente: java.lang.StackOverflowError
}
```

## Métodos Estáticos vs. de Instancia

### Contexto Estático (`static`)

Los métodos estáticos pertenecen a la clase y se cargan cuando la JVM carga la clase por primera vez.

- **No tienen `this`**: No están asociados a ninguna instancia, por lo que no existe el puntero oculto a la instancia actual.
- **Limitación**: No pueden acceder a miembros de instancia directamente; deben crear un objeto o recibirlo como parámetro.

```{code} java
:caption: Método estático

public class Calculadora {
    
    // Método estático: se invoca con Calculadora.sumar(...)
    public static int sumar(int a, int b) {
        return a + b;
    }
}

// Invocación desde otro lugar
int resultado = Calculadora.sumar(5, 3);
```

:::{note} Por Qué Usamos `static` en este Curso
Durante la primera parte del curso, antes de ver programación orientada a objetos, todos nuestros métodos serán `static`. Esto es porque no estamos trabajando con instancias de objetos todavía, solo con el método `main` y métodos auxiliares.
:::

### El rol de `@Override`

Aunque es una anotación, es vital para la seguridad. Indica que el método tiene la intención de sobrescribir un método de la superclase. Si la firma no coincide exactamente (ej. error de dedo en el nombre o parámetros), el compilador lanzará un error en lugar de crear silenciosamente un método nuevo (sobrecarga accidental).

## Varargs: Azúcar Sintáctico

Los parámetros variables (`tipo... nombre`) permiten pasar una lista arbitraria de argumentos. Internamente, la JVM convierte esto en un **arreglo**.

```{code} java
:caption: Implementación interna de Varargs

public void imprimir(String... mensajes) {
    // mensajes se trata como String[]
    for (String m : mensajes) System.out.println(m);
}
```

:::{note}
Evitá sobrecargar métodos que usen varargs, ya que las reglas de resolución se vuelven complejas y propensas a errores de legibilidad.
:::

## Ejercicios

````{exercise}
:label: ej-pasaje-valor

Analizá el siguiente código y determiná qué imprime:
```java
public static void main(String[] args) {
    int x = 10;
    StringBuilder s = new StringBuilder("A");
    modificar(x, s);
    System.out.println(x + " " + s);
}

public static void modificar(int n, StringBuilder sb) {
    n = 20;
    sb.append("B");
    sb = new StringBuilder("C");
}
```

````

```{solution} ej-pasaje-valor
:class: dropdown

Imprime `10 AB`.
1. `x` no cambia porque se pasó una copia del valor `10`.
2. `s` cambia a "AB" porque `sb.append` modificó el objeto en el *heap* a través de la copia de la referencia.
3. La reasignación `sb = new StringBuilder("C")` solo afectó a la variable local `sb`, no a la variable `s` del `main`.
````

````{exercise}
:label: ej-sobrecarga
¿Cuál de los siguientes métodos se invocará con la llamada `procesar(5)`? Explicá por qué.

```java
public static void procesar(int x) { System.out.println("int"); }
public static void procesar(long x) { System.out.println("long"); }
public static void procesar(double x) { System.out.println("double"); }
```
````

```{solution} ej-sobrecarga
:class: dropdown

Se invocará `procesar(int x)` y se imprimirá "int".

El compilador busca primero una coincidencia exacta de tipos. Como `5` es un literal `int`, coincide exactamente con el primer método. Si no existiera el método con parámetro `int`, el compilador buscaría promociones: primero `long`, luego `double`.
```

````{exercise}
:label: ej-flujo-ejecucion
Dado el siguiente código, indicá en qué orden se ejecutan las líneas numeradas:

```java
public static void main(String[] args) {
    int a = metodoA();           // Línea 1
    System.out.println(a);       // Línea 2
}

public static int metodoA() {
    int b = metodoB();           // Línea 3
    return b + 1;                // Línea 4
}

public static int metodoB() {
    return 10;                   // Línea 5
}
```
````

```{solution} ej-flujo-ejecucion
:class: dropdown
El orden de ejecución es: **1 → 3 → 5 → 4 → 2**

1. Línea 1: Se invoca `metodoA()`
2. Línea 3: Dentro de `metodoA()`, se invoca `metodoB()`
3. Línea 5: `metodoB()` retorna 10
4. Línea 4: `metodoA()` retorna 10 + 1 = 11
5. Línea 2: Se imprime 11

Este orden refleja cómo se apilan y desapilan los stack frames en la JVM.
```

````exercise
:label: ej-crear-metodo
Escribí un método llamado `esPrimo` que reciba un número entero positivo y retorne `true` si es primo, `false` en caso contrario. Un número es primo si solo es divisible por 1 y por sí mismo.
````

````solution
:for: ej-crear-metodo
```java
public static boolean esPrimo(int numero) {
    // Casos especiales
    if (numero <= 1) {
        return false;
    }
    if (numero == 2) {
        return true;
    }
    if (numero % 2 == 0) {
        return false;
    }
    
    // Verificar divisores impares hasta la raíz cuadrada
    boolean esPrimo = true;
    int divisor = 3;
    
    while (divisor * divisor <= numero && esPrimo) {
        if (numero % divisor == 0) {
            esPrimo = false;
        }
        divisor = divisor + 2;
    }
    
    return esPrimo;
}
```

El método verifica casos especiales primero, luego solo comprueba divisores impares hasta la raíz cuadrada del número (optimización matemática).
````

## Buenas Prácticas para Métodos

### Nombres Descriptivos

El nombre del método debe indicar claramente qué hace. Usá verbos en infinitivo o tercera persona.

```{code} java
:caption: Buenos nombres de métodos

// ✅ Buenos nombres
public static double calcularPromedio(int[] numeros) { ... }
public static boolean esValido(String email) { ... }
public static void imprimirReporte(String datos) { ... }

// ❌ Nombres poco claros
public static double cp(int[] n) { ... }
public static boolean validar(String s) { ... }  // ¿Qué valida?
public static void proceso1(String d) { ... }
```

### Métodos Cortos y Enfocados

Cada método debe hacer **una sola cosa** y hacerla bien.

```{code} java
:caption: Un método, una responsabilidad

// ❌ Hace demasiadas cosas
public static void procesarDatos(int[] datos) {
    // Lee datos
    // Valida datos
    // Calcula promedio
    // Imprime resultados
}

// ✅ Responsabilidades separadas
public static int[] leerDatos() { ... }
public static boolean validarDatos(int[] datos) { ... }
public static double calcularPromedio(int[] datos) { ... }
public static void imprimirResultado(double promedio) { ... }
```

### Evitar Efectos Secundarios Inesperados

Un método debe hacer lo que su nombre indica, nada más.

```{code} java
:caption: Evitar efectos secundarios

// ❌ Efecto secundario: imprime además de calcular
public static int sumar(int a, int b) {
    int resultado = a + b;
    System.out.println("Sumando...");  // Efecto secundario inesperado
    return resultado;
}

// ✅ Solo hace lo que dice
public static int sumar(int a, int b) {
    return a + b;
}
```

### Documentación con Javadoc

Los métodos públicos deben documentarse explicando qué hacen, sus parámetros y su retorno.

```{code} java
:caption: Documentación Javadoc

/**
 * Calcula el factorial de un número entero no negativo.
 *
 * @param n Número del cual calcular el factorial (debe ser >= 0)
 * @return El factorial de n (n!)
 * @throws IllegalArgumentException si n es negativo
 */
public static long factorial(int n) {
    if (n < 0) {
        throw new IllegalArgumentException("n debe ser no negativo");
    }
    if (n <= 1) {
        return 1;
    }
    return n * factorial(n - 1);
}
```

## Referencias Bibliográficas

- **Bloch, J.** (2018). _Effective Java_ (3ra ed.). Addison-Wesley Professional.
- **Liang, Y. D.** (2017). _Introduction to Java Programming and Data Structures_ (11va ed.). Pearson.
- **Schildt, H.** (2022). _Java: A Beginner's Guide_ (9na ed.). McGraw Hill.
- **Gosling, J., et al.** (2015). _The Java Language Specification_. Oracle.


