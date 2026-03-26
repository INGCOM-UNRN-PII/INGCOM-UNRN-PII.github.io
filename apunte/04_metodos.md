---
title: "\"Funciones\" en Java"
description: Estudio técnico sobre la abstracción de procedimientos, gestión de la pila de llamadas y mecanismos de pasaje de parámetros.
---

(metodos-en-java)=
# Métodos en Java

Un **método** en Java es la unidad fundamental de comportamiento y abstracción. Permite agrupar un conjunto de instrucciones bajo un nombre, de manera que puedan ejecutarse múltiples veces sin repetir el código. Si ya se trabajó con funciones en C, la transición a métodos en Java resulta bastante natural: la sintaxis es similar y los conceptos fundamentales —parámetros, retorno, variables locales— funcionan de forma análoga.

La diferencia principal es que en Java todo método debe estar dentro de una clase. Por ahora, no es necesario profundizar en qué significa "clase" desde el punto de vista de la programación orientada a objetos; basta con entenderlo como el contenedor obligatorio donde se escriben los métodos. Más adelante en el curso se explorará este concepto en detalle.

:::{note} Terminología: Función vs. Método
En C hablamos de **funciones**. En Java, el término correcto es **método** porque siempre pertenece a una clase. Sin embargo, conceptualmente son equivalentes: un bloque de código reutilizable que puede recibir datos (parámetros), procesarlos y opcionalmente devolver un resultado.

A lo largo de este curso, usaremos ambos términos de forma intercambiable cuando el contexto sea claro, pero recordá que en Java técnicamente son **métodos**.
:::

(por-que-usar-metodos)=
## ¿Por qué usar métodos?

Antes de entrar en la sintaxis, vale la pena entender las razones por las que se organizan los programas en métodos:

1. **Reutilización**: Si una operación se necesita en varios lugares del programa, escribirla una sola vez en un método evita duplicar código. En C, esto ya se conoce: una función `calcular_promedio()` se escribe una vez y se llama donde haga falta.

2. **Abstracción**: Un método oculta los detalles de *cómo* se hace algo. Quien lo usa solo necesita saber *qué* hace. Por ejemplo, al llamar a `Math.sqrt(25)`, no importa qué algoritmo interno se usa para calcular la raíz cuadrada; solo importa que devuelve `5.0`.

3. **Modularidad**: Dividir un programa grande en métodos pequeños facilita entender, probar y modificar cada parte de forma independiente. Un error en un método no debería afectar a otros si cada uno tiene una responsabilidad clara.

4. **Legibilidad**: Un método con un nombre descriptivo hace que el código sea más fácil de leer. Comparar `if (esPrimo(n))` con `if (n > 1 && ...)` seguido de un lazo complejo muestra claramente la ventaja.

(anatomia-de-un-metodo)=
## Anatomía de un Método

(sintaxis-general)=
### Sintaxis General

```{code} java
:caption: Estructura completa de un método

[modificadores] tipoRetorno nombreMetodo([parámetros]) [throws excepciones] {
    // cuerpo del método
    [return valor;]
}
```

Cada parte de esta estructura tiene un propósito específico:

- **modificadores** (opcional): Palabras clave que alteran el comportamiento o visibilidad del método. Los más comunes son `public` (accesible desde cualquier lugar), `private` (solo accesible dentro de la misma clase) y `static` (pertenece a la clase, no a una instancia). **Por ahora**, todos los métodos llevarán `public static`.

- **tipoRetorno** (obligatorio): El tipo de dato que el método devuelve al terminar. Puede ser cualquier tipo primitivo (`int`, `double`, `boolean`, etc.), cualquier tipo de referencia (`String`, arreglos, objetos), o la palabra especial `void` que indica que el método no devuelve nada. En C, esto es idéntico: `int funcion()` devuelve un entero, `void funcion()` no devuelve nada.

- **nombreMetodo** (obligatorio): El identificador que se usará para invocar al método. Por convención en Java, se usa **camelCase**: la primera palabra en minúscula y las siguientes con inicial mayúscula (`calcularPromedio`, `esPrimo`, `obtenerMaximo`). Esto difiere de C, donde se suele usar snake_case (`calcular_promedio`).

- **parámetros** (opcional): Lista de variables que el método recibe como entrada, separadas por comas. Cada parámetro requiere su tipo: `(int a, double b, String nombre)`. Si el método no necesita datos de entrada, los paréntesis quedan vacíos: `()`.

- **throws** (opcional): Declaración de excepciones que el método puede lanzar. Se verá en detalle en el capítulo de excepciones; por ahora puede ignorarse.

- **cuerpo**: Las instrucciones que se ejecutan cuando se invoca el método, encerradas entre llaves `{}`. Aquí va la lógica del método.

- **return** (obligatorio si no es `void`): La sentencia que finaliza la ejecución del método y devuelve un valor al código que lo llamó. El tipo del valor devuelto debe coincidir con el tipoRetorno declarado.

(comparacion-con-c)=
### Comparación con C

Para quien viene de programar en C, la siguiente tabla muestra las equivalencias:

| Aspecto | C | Java |
| :--- | :--- | :--- |
| Declaración | `int sumar(int a, int b)` | `public static int sumar(int a, int b)` |
| Sin retorno | `void imprimir(char* msg)` | `public static void imprimir(String msg)` |
| Ubicación | Archivo `.c` o `.h` | Dentro de una clase |
| Convención de nombres | `calcular_promedio` | `calcularPromedio` |
| Prototipo | Necesario antes de usar | No necesario (el compilador analiza toda la clase) |

La diferencia más notable es que en Java no existen los prototipos (_forward declarations_). En C, si `main()` llama a `sumar()` pero `sumar()` está definida después, se necesita declarar el prototipo antes. En Java, los métodos pueden estar en cualquier orden dentro de la clase y el compilador los encuentra sin problema.

(ejemplos-de-declaracion)=
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

Este último ejemplo muestra un patrón muy común: métodos que verifican una condición y devuelven `true` o `false`. Por convención, estos métodos suelen nombrarse con prefijos como `es`, `tiene`, `puede` (`esPar`, `tieneElementos`, `puedeAcceder`). En C, esto se haría con `int` retornando 0 o 1, ya que C89 no tiene tipo booleano nativo.

(invocacion-de-metodos)=
## Invocación de Métodos

Para ejecutar un método, se lo **invoca** (o llama) usando su nombre seguido de paréntesis con los argumentos necesarios. La invocación puede hacerse de varias formas según el contexto.

(invocacion-basica)=
### Invocación Básica

```{code} java
:caption: Invocación de métodos

// Método sin retorno - se ejecuta por su efecto
saludar();

// Método con retorno - guardar resultado en variable
int suma = sumar(5, 3);  // suma = 8

// Método con retorno - usar directamente en otra llamada
System.out.println(sumar(10, 20));  // Imprime: 30

// Usar retorno en expresión aritmética
double area = calcularCuadrado(5.0) * 3.14159;

// Usar retorno boolean en condición
if (esPar(numero)) {
    System.out.println("Es par");
}
```

(el-flujo-de-ejecucion-durante-una-llamada)=
### El Flujo de Ejecución Durante una Llamada

Cuando se invoca un método, ocurre lo siguiente:

1. **Se evalúan los argumentos**: Si los argumentos son expresiones (como `sumar(2+3, 4*2)`), primero se calculan sus valores (`5` y `8`).

2. **Se transfiere el control**: La ejecución "salta" desde el punto de llamada hacia la primera línea del cuerpo del método.

3. **Se ejecuta el cuerpo**: Las instrucciones del método se ejecutan secuencialmente.

4. **Se retorna**: Al encontrar `return` (o al llegar al final si es `void`), el control vuelve al punto de llamada y, si hay valor de retorno, este reemplaza a la expresión de llamada.

:::{important} Diferencia entre Parámetro y Argumento
Estos dos términos se confunden frecuentemente, pero tienen significados distintos:

- **Parámetro** (o parámetro formal): Variable declarada en la definición del método. Actúa como un "hueco" o "ranura" que espera recibir un valor. En `public static int sumar(int a, int b)`, los parámetros son `a` y `b`.

- **Argumento** (o parámetro actual): Valor concreto que se pasa al invocar el método. Llena las "ranuras" definidas por los parámetros. En `sumar(5, 3)`, los argumentos son `5` y `3`.

En otras palabras: los parámetros se definen una vez cuando se escribe el método; los argumentos se proporcionan cada vez que se llama al método. Esta distinción es la misma que en C.
:::

(tipos-de-retorno)=
## Tipos de Retorno

(metodos-void)=
### Métodos `void`

Un método `void` no devuelve ningún valor. Se usa para acciones que producen un efecto (imprimir en pantalla, modificar estado) pero no generan un resultado que el código llamante necesite usar.

```{code} java
:caption: Método void

public static void imprimirLinea(int cantidad) {
    for (int i = 0; i < cantidad; i = i + 1) {
        System.out.print("-");
    }
    System.out.println();
}
```

En C, `void` funciona exactamente igual. La función `printf()` de C técnicamente retorna un `int` (la cantidad de caracteres impresos), pero casi siempre se ignora y se la usa como si fuera `void`. En Java, `System.out.println()` es genuinamente `void`.

:::{note} Return en Métodos void
Un método `void` puede usar `return;` (sin valor) para terminar anticipadamente, pero no es obligatorio ponerlo al final. Si la ejecución llega al cierre de llave `}`, el método termina automáticamente.

```java
public static void procesarEdad(int edad) {
    if (edad < 0) {
        System.out.println("Edad inválida");
        return;  // Termina el método aquí, no ejecuta el resto
    }
    System.out.println("Edad válida: " + edad);
    // No hace falta "return;" aquí, termina solo
}
```
:::

(metodos-con-retorno)=
### Métodos con Retorno

Un método con tipo de retorno **debe** devolver un valor de ese tipo en todos los caminos de ejecución posibles. Esto significa que sin importar qué rama de un `if` se tome o cuántas vueltas dé un lazo, eventualmente debe ejecutarse un `return` con un valor del tipo correcto.

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

En este ejemplo, la variable `clasificacion` siempre recibe un valor porque el `else` final cubre todos los casos restantes. Una forma equivalente y más directa:

```{code} java
:caption: Return directo en cada rama

public static String clasificarNota(int nota) {
    if (nota >= 90) {
        return "Excelente";
    } else if (nota >= 70) {
        return "Bueno";
    } else if (nota >= 50) {
        return "Regular";
    } else {
        return "Insuficiente";
    }
}
```

Ambas versiones son correctas. La segunda es más compacta, pero puede resultar menos clara cuando la lógica es más compleja.

:::{warning} Error de Compilación: Falta de Return
El compilador de Java analiza todos los caminos posibles de ejecución. Si encuentra algún camino donde no hay `return`, rechaza el código:

```java
public static int obtenerValor(boolean condicion) {
    if (condicion) {
        return 10;
    }
    // ERROR: "missing return statement"
    // ¿Qué devuelve si condicion es false?
}
```

Para corregirlo, hay que agregar un `return` para el caso `false`:

```java
public static int obtenerValor(boolean condicion) {
    if (condicion) {
        return 10;
    }
    return 0;  // Valor por defecto cuando condicion es false
}
```

Este análisis del compilador es más estricto que en C, donde podés olvidar un `return` y el programa compila (aunque con advertencias) y produce comportamiento indefinido.
:::

(parametros-y-argumentos)=
## Parámetros y Argumentos

(parametros-multiples)=
### Parámetros Múltiples

Un método puede recibir cualquier cantidad de parámetros, separados por comas. Cada parámetro necesita su propio tipo declarado, incluso si varios parámetros tienen el mismo tipo:

```{code} java
:caption: Método con múltiples parámetros

public static double calcularIMC(double peso, double altura) {
    double imc = peso / (altura * altura);
    return imc;
}

// Invocación
double miIMC = calcularIMC(70.5, 1.75);
```

:::{note} Sintaxis de Parámetros: Diferencia con C
En C se puede declarar `int sumar(int a, b)` donde `b` hereda el tipo de `a`. En Java esto **no está permitido**; cada parámetro requiere su tipo explícito: `int sumar(int a, int b)`.
:::

(orden-de-los-argumentos)=
### Orden de los Argumentos

El orden de los argumentos debe coincidir exactamente con el orden de los parámetros. Java asocia el primer argumento con el primer parámetro, el segundo con el segundo, y así sucesivamente:

```{code} java
:caption: Importancia del orden de argumentos

public static void mostrarDatos(String nombre, int edad, double salario) {
    System.out.printf("%s tiene %d años y gana $%.2f%n", nombre, edad, salario);
}

// Correcto: "Ana" → nombre, 30 → edad, 50000.0 → salario
mostrarDatos("Ana", 30, 50000.0);

// Incorrecto - error de compilación por tipos incompatibles
// mostrarDatos(30, "Ana", 50000.0);  // Error: int no es compatible con String
```

Si los tipos coincidieran por casualidad (por ejemplo, tres parámetros `int`), el compilador no detectaría el error pero el programa produciría resultados incorrectos. Por eso es importante elegir nombres de parámetros claros y verificar el orden al invocar.

(compatibilidad-de-tipos-y-promocion-automatica)=
### Compatibilidad de Tipos y Promoción Automática

Java permite pasar argumentos de tipos "más pequeños" cuando el parámetro espera un tipo "más grande". Esto se llama **promoción automática** o _widening conversion_. Es el mismo mecanismo que existe en C.

```{code} java
:caption: Promoción automática de tipos

public static double dividir(double dividendo, double divisor) {
    return dividendo / divisor;
}

// Todas estas llamadas son válidas
double r1 = dividir(10.0, 3.0);   // double, double - coincidencia exacta
double r2 = dividir(10, 3.0);     // int se promueve a double
double r3 = dividir(10.0, 3);     // int se promueve a double
double r4 = dividir(10, 3);       // ambos int se promueven a double
```

La promoción sigue una jerarquía de tipos numéricos:

```
byte → short → int → long → float → double
```

Un tipo puede promoverse a cualquier tipo que esté a su derecha en esta jerarquía. Por ejemplo:
- `byte` puede promoverse a `short`, `int`, `long`, `float` o `double`
- `int` puede promoverse a `long`, `float` o `double`
- `double` no puede promoverse a nada (ya es el más "amplio")

:::{warning} La Promoción Solo Funciona "Hacia Arriba"
El proceso inverso (_narrowing_) **no es automático** porque puede perder información:

```java
public static void metodo(int x) { }

// Error de compilación: double no se convierte automáticamente a int
// metodo(5.7);

// Hay que hacer cast explícito
metodo((int) 5.7);  // Funciona, pero pierde la parte decimal
```

En C esto también genera advertencias, aunque el compilador suele permitirlo. Java es más estricto y lo rechaza directamente.
:::

(firma-de-un-metodo-y-sobrecarga)=
## Firma de un Método y Sobrecarga

(que-es-la-firma-de-un-metodo)=
### ¿Qué es la Firma de un Método?

La **firma** (_signature_) de un método es su identificador único para el compilador. Permite distinguir un método de otro, incluso si tienen el mismo nombre. Se compone estrictamente de:

1.  El **nombre** del método.
2.  El **número**, **tipo** y **orden** de sus parámetros.

Elementos que **no forman parte de la firma**:
- El tipo de retorno
- Los modificadores de acceso (`public`, `private`, etc.)
- Los nombres de los parámetros (solo importan los tipos)
- Las excepciones declaradas con `throws`

Esto significa que dos métodos con el mismo nombre y parámetros pero diferente tipo de retorno son considerados **el mismo método** por el compilador, lo cual genera un error.

```{figure} 04/sobrecarga_metodos.svg
:label: fig-sobrecarga-metodos
:align: center
:width: 90%

Sobrecarga de métodos: ejemplos válidos e inválidos basados en la firma.
```

(que-es-la-sobrecarga)=
### ¿Qué es la Sobrecarga?

La **sobrecarga** (_overloading_) es la capacidad de definir múltiples métodos con el mismo nombre pero con firmas diferentes. Esto permite que un mismo nombre represente operaciones conceptualmente similares pero que trabajan con distintos tipos o cantidades de datos.

(ejemplos-de-sobrecarga)=
### Ejemplos de Sobrecarga

```{code} java
:caption: Métodos sobrecargados válidos

// Todas estas son firmas diferentes porque varían en parámetros

// Firma: sumar(int, int)
public static int sumar(int a, int b) {
    return a + b;
}

// Firma: sumar(int, int, int) - diferente cantidad de parámetros
public static int sumar(int a, int b, int c) {
    return a + b + c;
}

// Firma: sumar(double, double) - diferentes tipos de parámetros
public static double sumar(double a, double b) {
    return a + b;
}

// Firma: sumar(String, String) - diferentes tipos de parámetros
public static String sumar(String a, String b) {
    return a + b;  // Concatenación de cadenas
}
```

La sobrecarga es útil porque permite usar nombres intuitivos. Sin ella, tendríamos que inventar nombres como `sumarEnteros`, `sumarTresEnteros`, `sumarDobles`, `concatenarStrings`, etc.

```{code} java
:caption: Sobrecarga inválida - mismo nombre y parámetros

// ERROR DE COMPILACIÓN: tienen la misma firma
public static int calcular(int x) {
    return x * 2;
}

public static double calcular(int x) {  // Solo difiere en retorno
    return x * 2.0;
}
// Error: method calcular(int) is already defined
```

El compilador no puede distinguir cuál método llamar basándose solo en el tipo de retorno. ¿Qué debería hacer con `calcular(5)` si se usa sin asignar a ninguna variable?

(resolucion-de-sobrecarga-overload-resolution)=
### Resolución de Sobrecarga (_Overload Resolution_)

Cuando invocás un método sobrecargado, el compilador debe determinar cuál de las versiones ejecutar. Este proceso se llama **resolución de sobrecarga** y sigue un orden de prioridad:

1.  **Coincidencia exacta de tipos**: Si existe un método cuyos parámetros coinciden exactamente con los tipos de los argumentos, se elige ese.

2.  **Promoción de primitivos**: Si no hay coincidencia exacta, el compilador intenta promociones automáticas (`int` → `long` → `float` → `double`).

3.  **Autoboxing/Unboxing**: Si aún no hay coincidencia, intenta convertir entre tipos primitivos y sus clases envolventes (`int` ↔ `Integer`, `double` ↔ `Double`). Esto se verá más adelante en el curso.

4.  **Varargs**: Como último recurso, busca coincidencias con parámetros variables.

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
mostrar(5);       // int: 5      (coincidencia exacta con int)
mostrar(5.0);     // double: 5.0 (coincidencia exacta con double)
mostrar("Hola");  // String: Hola (coincidencia exacta con String)
mostrar(5L);      // double: 5.0 (long se promueve a double, no hay mostrar(long))
mostrar('A');     // int: 65     (char se promueve a int)
```

El último caso es interesante: `'A'` es un `char`, pero como no hay `mostrar(char)`, se promueve a `int` (el valor ASCII de 'A' es 65).

:::{warning} Ambigüedad en Sobrecarga
Hay situaciones donde el compilador no puede decidir cuál método es "mejor" porque múltiples opciones requieren el mismo "nivel" de conversión:

```java
public static void metodo(int a, long b) {
    System.out.println("int, long");
}

public static void metodo(long a, int b) {
    System.out.println("long, int");
}

// Error de compilación: ambigüedad
// metodo(5, 5);
```

Ambos métodos requieren exactamente una promoción de `int` a `long`. Para el compilador, ninguno es preferible sobre el otro. La solución es hacer cast explícito: `metodo(5, 5L)` o `metodo(5L, 5)`.
:::

:::{note} Sobrecarga en C
C no soporta sobrecarga de funciones. Si necesitás una función que opere sobre diferentes tipos, tenés que darle nombres diferentes (`sumar_int`, `sumar_double`) o usar punteros genéricos (`void *`). Java, como C++, permite sobrecarga, lo que simplifica las interfaces de las bibliotecas.
:::

(mecanismo-de-pasaje-de-parametros)=
## Mecanismo de Pasaje de Parámetros

Entender cómo se pasan los datos a los métodos es fundamental para evitar errores sutiles. En Java existe una regla simple pero que genera confusión: **Java siempre pasa por valor**.

¿Qué significa "pasar por valor"? Que cuando se invoca un método, se crea una **copia** del dato que se pasa como argumento. El método trabaja con esa copia, no con el original.

Sin embargo, hay un matiz importante que depende de si el dato es un tipo primitivo o una referencia.

```{figure} 04/pasaje_parametros.svg
:label: fig-pasaje-parametros
:align: center
:width: 90%

Diferencia entre pasaje de primitivos (copia de valor) y referencias (copia de dirección).
```

(pasaje-de-tipos-primitivos)=
### Pasaje de Tipos Primitivos

Cuando se pasa un tipo primitivo (`int`, `double`, `boolean`, etc.), se copia el valor numérico. Cualquier modificación dentro del método afecta solo a la copia local, no a la variable original.

```{code} java
:caption: Pasaje por valor con primitivos

public static void duplicar(int numero) {
    numero = numero * 2;  // Modifica solo la copia local
    System.out.println("Dentro del método: " + numero);  // Imprime: 20
}

public static void main(String[] args) {
    int valor = 10;
    duplicar(valor);
    System.out.println("Fuera del método: " + valor);  // Imprime: 10 (sin cambios)
}
```

Esto es idéntico a lo que ocurre en C con parámetros no-puntero:

```c
// Equivalente en C - mismo comportamiento
void duplicar(int numero) {
    numero = numero * 2;
    printf("Dentro: %d\n", numero);  // 20
}

int main() {
    int valor = 10;
    duplicar(valor);
    printf("Fuera: %d\n", valor);  // 10
}
```

(pasaje-de-referencias-tipos-no-primitivos)=
### Pasaje de Referencias (Tipos no Primitivos)

Cuando se pasa un objeto (un tipo no primitivo como `String`, arreglos, o cualquier instancia de clase), lo que se pasa es una **copia de la referencia**. La referencia es esencialmente la dirección de memoria donde está el objeto.

Esto es análogo a pasar un puntero por valor en C: el puntero se copia, pero ambas copias apuntan a la misma zona de memoria.

Las consecuencias son dos:

1. Si se **modifica el estado interno** del objeto (llamando a sus métodos o modificando sus campos), el cambio **sí es visible** fuera del método, porque ambas referencias apuntan al mismo objeto.

2. Si se **reasigna la referencia** a un nuevo objeto, esto **no afecta** a la variable original, porque solo se modifica la copia local de la referencia.

```{code} java
:caption: Modificar estado vs reasignar referencia

public static void modificarContenido(StringBuilder sb) {
    sb.append(" modificado");  // Cambia el objeto al que apunta sb
}

public static void reasignarReferencia(StringBuilder sb) {
    sb = new StringBuilder("nuevo");  // sb ahora apunta a otro objeto
    // La variable original sigue apuntando al objeto anterior
}

public static void main(String[] args) {
    StringBuilder texto = new StringBuilder("original");
    
    modificarContenido(texto);
    System.out.println(texto);  // Imprime: "original modificado"
    // El cambio persiste porque modificamos el objeto, no la referencia
    
    reasignarReferencia(texto);
    System.out.println(texto);  // Imprime: "original modificado"
    // No cambió porque reasignar sb no afecta a texto
}
```

:::{important} Comparativa con C
En C, para modificar una variable externa desde una función, se pasa un puntero a esa variable:

```c
// C: modificar un entero externo
void duplicar(int *p) {
    *p = *p * 2;  // Modifica el valor apuntado
}

int main() {
    int valor = 10;
    duplicar(&valor);  // Se pasa la dirección
    printf("%d\n", valor);  // 20
}
```

En Java no existen punteros explícitos ni el operador `&`. Las referencias a objetos funcionan implícitamente como punteros, pero no se puede tomar la "dirección" de un primitivo para modificarlo desde afuera. Si necesitás ese comportamiento, tenés que encapsular el primitivo en un objeto o retornar el nuevo valor.
:::

(resumen-del-pasaje-de-parametros)=
### Resumen del Pasaje de Parámetros

| Tipo de Dato | ¿Qué se Copia? | ¿Modificar afecta el original? |
| :--- | :--- | :--- |
| Primitivo (`int`, `double`, etc.) | El valor numérico | No |
| Referencia (objetos, arreglos) | La dirección de memoria | Depende: modificar el objeto sí, reasignar la referencia no |

(alcance-de-variables-scope)=
## Alcance de Variables (Scope)

El **alcance** (o _scope_) de una variable determina en qué parte del código esa variable existe y puede ser accedida. Este concepto funciona de manera casi idéntica en C y Java.

(variables-locales)=
### Variables Locales

Las variables declaradas dentro de un método son **locales** a ese método. Existen desde el momento en que se declaran hasta que el método termina. Fuera del método, esas variables no existen.

```{code} java
:caption: Alcance de variables locales

public static void metodo1() {
    int x = 10;  // x solo existe en metodo1
    System.out.println(x);  // OK
}

public static void metodo2() {
    // System.out.println(x);  // ERROR: x no existe en este método
    int x = 20;  // Esta es una variable DIFERENTE, también llamada x
    System.out.println(x);  // OK, imprime 20
}
```

Las dos variables `x` son completamente independientes. Que tengan el mismo nombre no las relaciona de ninguna manera. Esto es igual que en C.

(variables-de-bloque)=
### Variables de Bloque

Dentro de un método, cada par de llaves `{}` define un **bloque**. Las variables declaradas dentro de un bloque solo existen dentro de ese bloque y sus bloques internos.

```{code} java
:caption: Alcance dentro de bloques

public static void ejemplo() {
    int a = 1;  // 'a' visible en todo el método
    
    if (a > 0) {
        int b = 2;  // 'b' solo visible dentro del if
        System.out.println(a + b);  // OK: 'a' viene del bloque externo
    }
    
    // System.out.println(b);  // ERROR: 'b' ya no existe
    
    for (int i = 0; i < 5; i = i + 1) {
        int c = i * 2;  // 'c' solo visible dentro del for
        System.out.println(c);
    }
    
    // System.out.println(i);  // ERROR: 'i' solo existía en el for
    // System.out.println(c);  // ERROR: 'c' solo existía en el for
}
```

Una variable declarada en un bloque externo es visible en los bloques internos, pero no al revés. Esta regla se conoce como **anidamiento léxico** y es idéntica en C.

(parametros-como-variables-locales)=
### Parámetros como Variables Locales

Los parámetros de un método se comportan exactamente como variables locales que ya vienen inicializadas con los valores de los argumentos. Existen durante toda la ejecución del método y desaparecen cuando el método termina.

```{code} java
:caption: Parámetros como variables locales

public static int calcular(int valor) {
    // 'valor' es una variable local inicializada con el argumento
    valor = valor + 10;  // Modifica solo la copia local
    return valor;
}

public static void main(String[] args) {
    int x = 5;
    int resultado = calcular(x);
    System.out.println(x);          // 5 (no cambió)
    System.out.println(resultado);  // 15
}
```

Esto refuerza el concepto de pasaje por valor: modificar un parámetro es modificar una variable local, no el argumento original.

(sombreado-de-variables-shadowing)=
### Sombreado de Variables (_Shadowing_)

Java no permite declarar una variable local con el mismo nombre que un parámetro, pero sí permite que una variable local "sombree" (_shadow_) a una variable de un ámbito externo como un campo de clase, los campos de clase, son similares a las variables globales en el hecho que es un valor compartido, pero son mucho, pero mucho, más, ya que son la base de la programación orientada a objetos, que veremos mas adelante.

```{code} java
:caption: Sombreado (se evitará, por ahora, por claridad)

public class Ejemplo {
    static int valor = 100;  // Variable de clase
    
    public static void metodo(int valor) {  // Parámetro sombrea a la de clase
        // Aquí "valor" se refiere al parámetro, no al campo de clase
        System.out.println(valor);  // Imprime el parámetro
        System.out.println(Ejemplo.valor);  // Para acceder al campo de clase
    }
}
```

El sombreado puede generar confusión, por lo que es mejor evitarlo usando nombres distintos.

(gestion-de-la-pila-stack-frames)=
## Gestión de la Pila: Stack Frames

Cuando un programa ejecuta métodos, la JVM (Java Virtual Machine) utiliza una estructura de datos llamada **pila de llamadas** (_call stack_) para gestionar la ejecución. Esta pila funciona exactamente igual que en C: cada vez que se invoca un método, se crea un nuevo "marco" (_frame_) en la pila; cuando el método termina, su marco se destruye.

(que-contiene-un-stack-frame)=
### ¿Qué Contiene un Stack Frame?

Cada **stack frame** (marco de pila) almacena toda la información necesaria para ejecutar un método:

- **Variables Locales**: Todas las variables declaradas dentro del método, incluyendo los parámetros recibidos. Estas se almacenan en un arreglo interno de posiciones numeradas.

- **Pila de Operandos**: Una pila auxiliar donde la JVM realiza los cálculos intermedios. Por ejemplo, para calcular `a + b * c`, primero se apila `b`, luego `c`, se multiplican (el resultado queda en la pila), luego se apila `a` y se suma.

- **Dirección de Retorno**: La posición en el código donde debe continuar la ejecución cuando el método termine. Así el programa "sabe" a dónde volver.

- **Referencia al pool de constantes**: Información de la clase que permite resolver nombres de métodos y campos.

```{figure} 04/stack_frames.svg
:label: fig-stack-frames
:align: center
:width: 80%

Stack de llamadas mostrando los frames de métodos anidados.
```

(el-ciclo-de-vida-de-un-frame)=
### El Ciclo de Vida de un Frame

1. **Creación**: Cuando se invoca un método, se reserva espacio en el tope de la pila para su frame.
2. **Ejecución**: El método trabaja con sus variables locales y operandos.
3. **Retorno**: Cuando el método ejecuta `return` o llega al final de su cuerpo (si es `void`), su frame se destruye.
4. **Continuación**: El control vuelve al frame anterior, que estaba "esperando" debajo en la pila.

Este mecanismo de pila tiene una propiedad importante: los métodos se completan en orden inverso al que fueron llamados (el último en entrar es el primero en salir, LIFO).

(ejemplo-de-flujo-de-ejecucion)=
### Ejemplo de Flujo de Ejecución

El siguiente ejemplo muestra paso a paso cómo se construye y destruye la pila de llamadas:

```{code} java
:caption: Seguimiento del stack de llamadas

public static void main(String[] args) {
    int resultado = metodoA(5);      // 1. Se crea frame para main
    System.out.println(resultado);   // 5. Continúa main con resultado = 22
}                                    // 6. Se destruye frame de main

public static int metodoA(int x) {
    int y = metodoB(x + 1);          // 2. Se crea frame para metodoA (x=5)
    return y * 2;                    // 4. metodoA calcula 11*2=22 y retorna
}                                    // Su frame se destruye

public static int metodoB(int n) {
    return n + 10;                   // 3. metodoB calcula 6+10=16 y retorna
}                                    // Su frame se destruye inmediatamente
```

El estado de la pila en cada momento:

| Paso | Acción | Estado de la Pila (tope → base) |
| :---: | :--- | :--- |
| 1 | `main` llama a `metodoA(5)` | `metodoA` ← `main` |
| 2 | `metodoA` llama a `metodoB(6)` | `metodoB` ← `metodoA` ← `main` |
| 3 | `metodoB` retorna 16 | `metodoA` ← `main` |
| 4 | `metodoA` retorna 22 | `main` |
| 5 | `main` imprime 22 | `main` |
| 6 | `main` termina | (pila vacía) |

(recursion-y-stackoverflowerror)=
### Recursión y `StackOverflowError`

Un método puede llamarse a sí mismo; esto se llama **recursión**. Cada llamada recursiva crea un nuevo frame en la pila, con sus propias copias de las variables locales. Es fundamental que exista un **caso base** que detenga la recursión; de lo contrario, la pila crecerá indefinidamente hasta agotar la memoria asignada.

```{code} java
:caption: Método recursivo para calcular factorial

public static long factorial(int n) {
    // Caso base: detiene la recursión
    if (n <= 1) {
        return 1;
    }
    // Caso recursivo: n! = n * (n-1)!
    return n * factorial(n - 1);
}
```

La definición matemática del factorial es naturalmente recursiva:

$$n! = \begin{cases} 1 & \text{si } n = 0 \text{ o } n = 1 \\ n \times (n-1)! & \text{si } n > 1 \end{cases}$$

Para `factorial(4)`, la pila crece así:
1. `factorial(4)` espera el resultado de `factorial(3)`
2. `factorial(3)` espera el resultado de `factorial(2)`
3. `factorial(2)` espera el resultado de `factorial(1)`
4. `factorial(1)` retorna 1 (caso base)
5. `factorial(2)` retorna 2 * 1 = 2
6. `factorial(3)` retorna 3 * 2 = 6
7. `factorial(4)` retorna 4 * 6 = 24

```{code} java
:caption: Recursión sin caso base - `StackOverflowError`

public static void infinito() {
    infinito();  // Sin caso base, nunca termina
    // Eventualmente: java.lang.StackOverflowError
}
```

El **`StackOverflowError`** ocurre cuando la pila de llamadas crece más allá del límite de memoria asignado. En Java, el tamaño de la pila es configurable pero finito. La recursión muy profunda puede causar este error incluso con un caso base correcto, simplemente porque los datos de entrada requieren demasiados niveles.

:::{note} Recursión en C
En C ocurre lo mismo: la recursión usa el stack del sistema, y si se excede su tamaño, el programa falla (generalmente con "stack overflow" o "segmentation fault"). La diferencia es que Java detecta el problema y lanza una excepción controlable, mientras que C simplemente colapsa.
:::

(metodos-estaticos-vs-de-instancia)=
## Métodos Estáticos vs. de Instancia

Los métodos (y variables) estáticos pertenecen a la clase y se cargan cuando la JVM carga la clase por primera vez, mientras que lo que _no_ es `static`, pertenece a los objetos creados a partir de esa clase.

Este es _otro_ de los temas que veremos en la parte de Orientación a Objetos más adelante.

De momento, lo que tenemos que saber sobre este calificador es que:

1. **Se puede invocar sin crear un objeto**: Basta con usar el nombre de la clase seguido de un punto y el nombre del método: `NombreClase.metodo()`.

3. **Solo puede acceder a otros miembros estáticos**: No puede usar variables de instancia ni llamar a métodos de instancia directamente, ya que tendría que crear un objeto primero.


:::{note} Por Qué Usamos `static`, por ahora...

Durante la primera parte del curso, antes de introducir programación orientada a objetos, todos los métodos serán `static`. La razón es simple: el método `main` es estático (así lo exige Java para que la JVM pueda ejecutarlo sin crear objetos), y desde un contexto estático solo se pueden llamar otros métodos estáticos de la misma clase directamente.

Cuando se introduzcan objetos, se verán métodos sin `static` que operan sobre instancias específicas.
:::

(comparacion-rapida)=
### Comparación Rápida

| Aspecto | Método Estático | Método de Instancia |
| :--- | :--- | :--- |
| Palabra clave | Incluye `static` | No incluye `static` |
| Invocación | `Clase.metodo()` | `objeto.metodo()` |
| Acceso a `this` | No | Sí |
| Típico uso | Utilidades, funciones matemáticas | Operaciones sobre datos del objeto |

Por ahora, todos los métodos que escribamos serán `public static` para poder llamarlos desde `main`. La distinción con métodos de instancia se explorará más adelante.


(ejercicios)=
## Ejercicios

Los siguientes ejercicios permiten practicar los conceptos vistos. Se recomienda intentar resolverlos antes de ver las soluciones.

````{exercise}
:label: ej-pasaje-valor

Analizá el siguiente código y determiná qué imprime. Explicá paso a paso qué ocurre con cada variable.

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

El programa imprime `10 AB`.

Análisis paso a paso:

1. En `main`, se declara `x = 10` y `s` apunta a un `StringBuilder` con contenido "A".

2. Se llama a `modificar(x, s)`:
   - Se crea una copia de `x` (el valor 10) en el parámetro `n`
   - Se crea una copia de `s` (la referencia al StringBuilder) en el parámetro `sb`
   - Ahora `sb` apunta al mismo objeto StringBuilder que `s`

3. Dentro de `modificar`:
   - `n = 20`: Modifica solo la copia local. `x` en main sigue siendo 10.
   - `sb.append("B")`: Modifica el objeto StringBuilder. Como `sb` y `s` apuntan al mismo objeto, el cambio es visible desde ambas referencias. El contenido ahora es "AB".
   - `sb = new StringBuilder("C")`: Reasigna `sb` a un nuevo objeto. Esto **no afecta** a `s` porque solo cambia la copia local de la referencia.

4. Al volver a `main`:
   - `x` sigue siendo 10 (nunca se modificó)
   - `s` sigue apuntando al StringBuilder original, cuyo contenido es "AB"
```

````{exercise}
:label: ej-sobrecarga
¿Cuál de los siguientes métodos se invocará con la llamada `procesar(5)`? Explicá el proceso de resolución.

```java
public static void procesar(int x) { System.out.println("int"); }
public static void procesar(long x) { System.out.println("long"); }
public static void procesar(double x) { System.out.println("double"); }
```
````

```{solution} ej-sobrecarga
:class: dropdown

Se invocará `procesar(int x)` y se imprimirá "int".

El proceso de resolución de sobrecarga sigue estas prioridades:

1. **Coincidencia exacta**: El literal `5` es de tipo `int`. Existe un método `procesar(int x)`, por lo tanto hay coincidencia exacta.

2. Si no hubiera `procesar(int x)`, el compilador buscaría promociones:
   - `int` → `long`: Usaría `procesar(long x)`
   - Si tampoco existiera, `int` → `double`: Usaría `procesar(double x)`

La regla general es que el compilador siempre prefiere la coincidencia más específica (que requiera menos conversiones).
```

````{exercise}
:label: ej-flujo-ejecucion
Dado el siguiente código, indicá en qué orden se ejecutan las líneas numeradas y describí el estado de la pila de llamadas en cada paso:

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

Desglose detallado:

1. **Línea 1**: `main` comienza a ejecutar. Al evaluar `metodoA()`, se crea un frame para `metodoA` y se transfiere el control.
   - Pila: `metodoA` ← `main`

2. **Línea 3**: Dentro de `metodoA`, al evaluar `metodoB()`, se crea un frame para `metodoB`.
   - Pila: `metodoB` ← `metodoA` ← `main`

3. **Línea 5**: `metodoB` calcula y retorna 10. Su frame se destruye.
   - Pila: `metodoA` ← `main`

4. **Línea 4**: `metodoA` recibe el valor 10 en `b`, calcula `10 + 1 = 11` y retorna. Su frame se destruye.
   - Pila: `main`

5. **Línea 2**: `main` recibe el valor 11 en `a` y lo imprime.
   - Pila: `main`

El resultado impreso es **11**.
```

````{exercise}
:label: ej-crear-metodo
Escribí un método llamado `esPrimo` que reciba un número entero positivo y retorne `true` si es primo, `false` en caso contrario. Un número es primo si solo es divisible por 1 y por sí mismo.

Considerá los casos especiales: el 1 no es primo, el 2 sí lo es.
````

````{solution} ej-crear-metodo
:class: dropdown

```java
public static boolean esPrimo(int numero) {
    // El 1 no es primo por definición
    if (numero <= 1) {
        return false;
    }
    
    // El 2 es el único primo par
    if (numero == 2) {
        return true;
    }
    
    // Los demás números pares no son primos
    if (numero % 2 == 0) {
        return false;
    }
    
    // Verificar divisores impares hasta la raíz cuadrada
    // Si n tiene un divisor mayor que √n, también tiene uno menor
    boolean esPrimo = true;
    int divisor = 3;
    
    while (divisor * divisor <= numero && esPrimo) {
        if (numero % divisor == 0) {
            esPrimo = false;  // Encontramos un divisor, no es primo
        }
        divisor = divisor + 2;  // Solo probamos impares
    }
    
    return esPrimo;
}
```

**Explicación de la optimización:** 
- Solo verificamos hasta la raíz cuadrada porque si `n = a × b` con `a ≤ √n`, entonces `b ≥ √n`. Si no encontramos ningún divisor hasta `√n`, no habrá ninguno mayor.
- Solo probamos divisores impares (excepto el 2) porque todos los números pares mayores que 2 ya fueron descartados.
````

````{exercise}
:label: ej-recursion
Escribí un método recursivo `potencia` que calcule $b^n$ (base elevada al exponente) donde `n` es un entero no negativo. No uses `Math.pow()`.

Pista: $b^0 = 1$ y $b^n = b \times b^{n-1}$
````

````{solution} ej-recursion
:class: dropdown

```java
public static long potencia(int base, int exponente) {
    // Caso base: cualquier número elevado a 0 es 1
    if (exponente == 0) {
        return 1;
    }
    
    // Caso recursivo: b^n = b * b^(n-1)
    return base * potencia(base, exponente - 1);
}
```

Ejemplo de ejecución para `potencia(2, 4)`:
1. `potencia(2, 4)` → 2 × `potencia(2, 3)`
2. `potencia(2, 3)` → 2 × `potencia(2, 2)`
3. `potencia(2, 2)` → 2 × `potencia(2, 1)`
4. `potencia(2, 1)` → 2 × `potencia(2, 0)`
5. `potencia(2, 0)` → 1 (caso base)
6. Volviendo: 2×1=2, 2×2=4, 2×4=8, 2×8=16

Resultado: 16

**Nota:** Esta implementación es simple pero no es la más eficiente. Existe una versión que usa $b^n = (b^{n/2})^2$ cuando n es par, reduciendo la cantidad de llamadas recursivas de O(n) a O(log n).
````

(buenas-practicas-para-metodos)=
## Buenas Prácticas para Métodos

Las siguientes recomendaciones ayudan a escribir métodos claros, mantenibles y menos propensos a errores.

(nombres-descriptivos)=
### Nombres descriptivos

El nombre del método debe indicar claramente qué hace. La convención en Java es usar **verbos** o **frases verbales** en camelCase.

```{code} java
:caption: Buenos nombres de métodos

// Buenos nombres - indican claramente la acción
public static double calcularPromedio(int[] numeros) { ... }
public static boolean esEmailValido(String email) { ... }
public static void imprimirReporte(String datos) { ... }
public static int contarPalabras(String texto) { ... }
public static String obtenerNombreCompleto(String nombre, String apellido) { ... }

// Nombres poco claros - evitar
public static double cp(int[] n) { ... }        // Abreviatura críptica
public static boolean validar(String s) { ... } // ¿Qué valida?
public static void proceso1(String d) { ... }   // Completamente opaco
public static int f(int x) { ... }              // Sin significado
```

Para métodos que devuelven `boolean`, se suelen usar prefijos como:
- `es` / `is`: `esValido()`, `esPar()`, `isEmpty()`
- `tiene` / `has`: `tieneElementos()`, `hasNext()`
- `puede` / `can`: `puedeAcceder()`, `canRead()`

(metodos-cortos-y-enfocados-principio-de-responsabilidad-unica)=
### Métodos Cortos y Enfocados (Principio de Responsabilidad Única)

Cada método debe hacer **una sola cosa** y hacerla bien. Si un método hace demasiadas cosas, se vuelve difícil de entender, probar y modificar. 

Un buen indicador de que un método hace demasiado es si cuesta resumir qué hace en una oración simple.

```{code} java
:caption: Un método haciendo demasiadas cosas

// Problema: este método hace cuatro cosas diferentes
public static void procesarDatos(int[] datos) {
    // Lee datos del usuario
    Scanner sc = new Scanner(System.in);
    for (int i = 0; i < datos.length; i++) {
        datos[i] = sc.nextInt();
    }
    
    // Valida que no haya negativos
    for (int d : datos) {
        if (d < 0) {
            System.out.println("Error: dato negativo");
            return;
        }
    }
    
    // Calcula el promedio
    int suma = 0;
    for (int d : datos) {
        suma = suma + d;
    }
    double promedio = (double) suma / datos.length;
    
    // Imprime el resultado
    System.out.println("Promedio: " + promedio);
}
```

```{code} java
:caption: Responsabilidades separadas en métodos pequeños

public static void leerDatos(int[] datos) {
    Scanner sc = new Scanner(System.in);
    for (int i = 0; i < datos.length; i++) {
        datos[i] = sc.nextInt();
    }
}

public static boolean sonDatosValidos(int[] datos) {
    for (int d : datos) {
        if (d < 0) {
            return false;
        }
    }
    return true;
}

public static double calcularPromedio(int[] datos) {
    int suma = 0;
    for (int d : datos) {
        suma = suma + d;
    }
    return (double) suma / datos.length;
}

public static void imprimirResultado(double promedio) {
    System.out.println("Promedio: " + promedio);
}

// El método principal coordina, pero no hace el trabajo
public static void procesarDatos(int[] datos) {
    leerDatos(datos);
    if (sonDatosValidos(datos)) {
        double promedio = calcularPromedio(datos);
        imprimirResultado(promedio);
    } else {
        System.out.println("Error: datos inválidos");
    }
}
```

La segunda versión es más larga en total, pero cada método es simple, fácil de entender y fácil de probar de forma independiente.

(evitar-efectos-secundarios-inesperados)=
### Evitar Efectos Secundarios Inesperados

Un **efecto secundario** es cualquier cambio de estado observable fuera del método: imprimir en pantalla, modificar variables globales, escribir en archivos, etc. No todos los efectos secundarios son malos, pero deberían ser **esperables** según el nombre del método.

Un método llamado `calcularSuma` debería calcular y retornar una suma, no imprimir mensajes ni modificar variables externas. Si lo hace, sorprende al programador que lo usa.

```{code} java
:caption: Efecto secundario inesperado

// Problema: el nombre sugiere que solo calcula, pero también imprime
public static int sumar(int a, int b) {
    int resultado = a + b;
    System.out.println("Calculando suma...");  // Efecto inesperado
    return resultado;
}

// ¿Por qué es malo? Imaginar que se usa así:
// int total = sumar(x, y) + sumar(z, w);
// Esto imprimirá "Calculando suma..." DOS veces, lo cual es confuso
```

```{code} java
:caption: Sin efectos secundarios inesperados

// Mejor: hace exactamente lo que el nombre indica
public static int sumar(int a, int b) {
    return a + b;
}

// Si se necesita imprimir, hacerlo en un método cuyo nombre lo indique
public static void imprimirSuma(int a, int b) {
    System.out.println("La suma es: " + (a + b));
}
```

**Regla práctica:** Un método con nombre que empiece con "calcular", "obtener", "es", "tiene" no debería tener efectos secundarios. Un método con nombre que empiece con "imprimir", "guardar", "enviar" claramente tiene efectos y eso está bien.

(documentacion-con-javadoc)=
### Documentación con Javadoc

Los métodos públicos deberían documentarse con **Javadoc**, un formato especial de comentario que comienza con `/**` y puede incluir etiquetas estructuradas. Las herramientas de Java pueden extraer estos comentarios y generar documentación HTML automáticamente.

```{code} java
:caption: Documentación Javadoc completa

/**
 * Calcula el factorial de un número entero no negativo.
 * 
 * El factorial de n (escrito n!) es el producto de todos los
 * enteros positivos menores o iguales a n. Por ejemplo,
 * factorial(5) = 5 × 4 × 3 × 2 × 1 = 120.
 *
 * @param n Número del cual calcular el factorial. Debe ser >= 0.
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

Las etiquetas más comunes son:
- `@param nombreParametro descripción`: Describe un parámetro de entrada.
- `@return descripción`: Describe qué devuelve el método.
- `@throws NombreExcepcion condición`: Describe cuándo se lanza una excepción.

Para métodos privados o muy simples, un comentario breve o ninguno puede ser suficiente si el código es autoexplicativo. La documentación excesiva de lo obvio puede ser contraproducente.

(limitar-la-cantidad-de-parametros)=
### Limitar la Cantidad de Parámetros

Un método con muchos parámetros (más de 3 o 4) se vuelve difícil de usar correctamente. Es fácil equivocarse en el orden de los argumentos.

```{code} java
:caption: Demasiados parámetros

// Problema: ¿cuál va primero, ancho o alto? ¿y los colores?
public static void dibujarRectangulo(int x, int y, int ancho, int alto, 
        int colorBorde, int colorRelleno, boolean rellenar) { ... }

// Al llamarlo, es fácil equivocarse:
dibujarRectangulo(10, 20, 100, 50, 0xFF0000, 0x00FF00, true);
// ¿O era así?
dibujarRectangulo(10, 20, 50, 100, 0x00FF00, 0xFF0000, false);
```

Soluciones posibles:
- Dividir el método en varios más específicos
- Agrupar parámetros relacionados en una estructura (cuando se vean clases)
- Usar patrones como Builder (tema avanzado)

(referencias-bibliograficas)=
## Referencias Bibliográficas

- **Bloch, J.** (2018). _Effective Java_ (3ra ed.). Addison-Wesley Professional.
- **Liang, Y. D.** (2017). _Introduction to Java Programming and Data Structures_ (11va ed.). Pearson.
- **Schildt, H.** (2022). _Java: A Beginner's Guide_ (9na ed.). McGraw Hill.
- **Gosling, J., et al.** (2015). _The Java Language Specification_. Oracle.


