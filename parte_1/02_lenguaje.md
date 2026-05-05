---
title: Características del Lenguaje Java
description:
  Análisis detallado de las características fundamentales que definen a Java
  como lenguaje de programación de propósito general y con tipado estático.
---

(caracteristicas-del-lenguaje-java)=
# Características del Lenguaje Java

Este capítulo analiza las características fundamentales de Java como lenguaje de programación. Entender estas características te ayudará a comprender por qué el código se escribe de cierta manera y qué ventajas ofrece Java frente a otros lenguajes.

Java se destaca por:
- Ser un **lenguaje de propósito general** (sirve para casi cualquier tipo de programa)
- Tener un **sistema de tipos estático y fuerte** (el compilador verifica los tipos antes de ejecutar)
- Estar **orientado a objetos por diseño** (aunque esto se estudiará más adelante)

:::{note} ¿Y la Orientación a Objetos?

Esta parte del apunte se enfoca en las características básicas del lenguaje con
la idea de reutilizar todo lo que ya sabés de C. Los conceptos de programación 
orientada a objetos (clases, objetos, herencia, polimorfismo) se estudiarán en profundidad más adelante en el curso, una vez consolidados los fundamentos del lenguaje.

Por ahora, usaremos Java de forma similar a como usabas C: escribiendo funciones (que en Java se llaman "métodos") dentro de una estructura obligatoria llamada "clase".
:::

(lenguaje-de-proposito-general)=
## Lenguaje de Propósito General

Java es un **lenguaje de propósito general**, lo que significa que no está
diseñado para resolver un único tipo de problema específico, sino que puede utilizarse
para desarrollar prácticamente cualquier tipo de software.

(que-significa-proposito-general)=
### ¿Qué significa "propósito general"?

Para entender esto, primero hay que conocer la diferencia con los **lenguajes de dominio específico** (DSL - Domain Specific Languages). Estos son lenguajes diseñados para resolver un problema muy particular:

- **SQL (Structured Query Language):** Diseñado específicamente para consultar y manipular bases de datos. Podés escribir `SELECT * FROM usuarios WHERE edad > 18`, pero no podés crear una interfaz gráfica ni un servidor web con SQL.

- **HTML (HyperText Markup Language):** Define la estructura de páginas web. Podés marcar títulos, párrafos e imágenes, pero no podés hacer cálculos matemáticos ni procesar archivos.

- **CSS (Cascading Style Sheets):** Controla la presentación visual de páginas web (colores, fuentes, layouts). Solo sirve para eso, no para lógica de negocio.

- **MATLAB:** Especializado en computación numérica, matrices y visualización de datos científicos. Excelente para ingeniería, pero no para hacer una aplicación de chat.

- **Expresiones Regulares (Regex):** Un mini-lenguaje para describir patrones de texto. Útilísimo para validar emails o buscar en texto, pero no es un lenguaje de programación completo.

En contraste, un **lenguaje de propósito general** como Java (o C, Python, C++, Go) puede usarse para crear:

(aplicaciones-donde-java-es-utilizado)=
### Aplicaciones donde Java es utilizado

:::{list-table} Aplicaciones de Java por dominio
:header-rows: 1
:label: tbl-dominios-java

- - Dominio
  - Ejemplos concretos
  - Tecnologías/Frameworks comunes
- - Aplicaciones empresariales
  - Sistemas bancarios (procesamiento de transacciones), sistemas ERP (gestión empresarial), sistemas CRM (gestión de clientes)
  - Spring, Jakarta EE, Hibernate
- - Aplicaciones web (backend)
  - Servidores que procesan peticiones HTTP, APIs RESTful, microservicios
  - Spring Boot, Micronaut, Quarkus
- - Aplicaciones móviles
  - Aplicaciones Android nativas
  - Android SDK, Kotlin/JVM
- - Aplicaciones de escritorio
  - IDEs como IntelliJ IDEA y Eclipse, herramientas de productividad
  - JavaFX, Swing
- - Sistemas embebidos
  - Dispositivos IoT, tarjetas inteligentes (como las SIM de celulares), Blu-ray players
  - Java ME, Embedded Java
- - Big Data
  - Procesamiento de datos masivos, análisis en tiempo real, pipelines de datos
  - Hadoop, Apache Spark, Apache Kafka, Apache Flink
- - Juegos
  - Minecraft (escrito completamente en Java), juegos indie
  - LWJGL (gráficos OpenGL), jMonkeyEngine, libGDX
:::

(significa-que-java-es-la-mejor-opcion-para-todo)=
### ¿Significa que Java es la mejor opción para todo?

**No.** Ser de propósito general significa que Java *puede* usarse para casi cualquier cosa, pero no que sea la *mejor* opción para todo. Cada lenguaje tiene fortalezas:

:::{important} Elegí la herramienta correcta para el trabajo

- **Python** es mejor para: scripts rápidos, prototipado, ciencia de datos, machine learning (tiene bibliotecas como NumPy, Pandas, TensorFlow que no tienen equivalente directo en Java).

- **C/C++** son mejores para: sistemas operativos, drivers, motores de videojuegos AAA, software donde cada microsegundo importa.

- **Go** es más simple para: microservicios pequeños y rápidos, herramientas de línea de comandos.

- **Rust** es preferible para: software de sistemas donde la seguridad de memoria es crítica (navegadores, sistemas embebidos críticos).

Java brilla en: aplicaciones empresariales de gran escala, sistemas que necesitan mantenerse por décadas, equipos grandes donde la consistencia y las herramientas importan.
:::

(ventajas-de-aprender-un-lenguaje-de-proposito-general)=
### Ventajas de aprender un lenguaje de propósito general

1. **Reutilización de conocimientos:** Una vez que dominás Java, podés trabajar en aplicaciones web, Android, sistemas distribuidos, o Big Data sin aprender un lenguaje completamente nuevo.

2. **Ecosistema unificado:** Usás las mismas herramientas (IDEs, sistemas de build, bibliotecas base) sin importar el tipo de proyecto.

3. **Comunidad amplia:** Hay millones de desarrolladores Java en el mundo. Cualquier problema que tengas, probablemente alguien ya lo resolvió y documentó.

(tipado-estatico-y-fuerte)=
## Tipado Estático y Fuerte

Java implementa un sistema de **tipado estático y fuerte**. Esta característica
fundamental influye profundamente en cómo se escribe, se verifica y se ejecuta el código.

(que-es-un-tipo-en-programacion)=
### ¿Qué es un "tipo" en programación?

Un **tipo de dato** define tres cosas importantes:

1. **Qué valores puede almacenar una variable:** Un `int` almacena números enteros, un `String` almacena texto, un `boolean` almacena verdadero o falso.

2. **Cuánta memoria ocupa:** Un `int` en Java siempre ocupa 4 bytes (32 bits), un `double` siempre ocupa 8 bytes (64 bits).

3. **Qué operaciones son válidas:** Podés sumar dos `int`, pero no podés sumar un `int` con un `String` directamente (aunque sí concatenarlos).

En C ya trabajaste con tipos: `int`, `float`, `double`, `char`, punteros, structs. Java tiene un sistema de tipos similar pero más estricto.

(tipado-estatico-los-tipos-se-verifican-antes-de-ejecutar)=
### Tipado Estático: los tipos se verifican ANTES de ejecutar

En un lenguaje con **tipado estático**, el tipo de cada variable:

1. **Se declara explícitamente** (con algunas excepciones modernas)
2. **Se conoce en tiempo de compilación** (antes de ejecutar el programa)
3. **No puede cambiar** durante la ejecución del programa

El **compilador** verifica que todas las operaciones de tipos sean correctas **antes** de crear el ejecutable. Si hay un error de tipos, el programa ni siquiera compila.

#### Ejemplo: Declaración de Variables

No se preocupen por los detalles del siguiente código, en la siguiente página del apunte
veremos detalles más profundos de este tema, por ahora, aprovechemos las similitudes con C.

```java
int edad = 25;           // 'edad' es de tipo int, para siempre
String nombre = "Ana";   // 'nombre' es de tipo String, para siempre

// Esto funciona: asignar un nuevo valor del MISMO tipo
edad = 30;               // ✅ OK: 30 también es int

// Esto NO compila: intentar cambiar el tipo de valor
edad = "treinta";        // ❌ Error de compilación: String no es int
```

#### ¿Qué significa "tiempo de compilación" vs "tiempo de ejecución"?

Esta distinción es fundamental:

- **Tiempo de compilación (compile time):** Es cuando el compilador `javac` lee tu código fuente (`.java`) y lo convierte a bytecode (`.class`). En esta etapa, el compilador verifica la sintaxis y los tipos. Si hay errores, no se genera el bytecode y no podés ejecutar nada.

- **Tiempo de ejecución (runtime):** Es cuando la JVM ejecuta el bytecode compilado. El programa ya pasó la verificación del compilador y está "corriendo". Los errores aquí son excepciones que ocurren mientras el programa funciona.

El tipado estático detecta errores de tipos en **tiempo de compilación**, lo que significa que te enterás del error **antes** de ejecutar el programa, antes de que llegue a producción, antes de que un usuario lo vea.

#### Comparación con Tipado Dinámico

En lenguajes con **tipado dinámico** como Python, las variables no tienen tipos fijos. El tipo se determina en tiempo de ejecución, según el valor asignado en cada momento:

**Python (tipado dinámico):**

```python
edad = 25           # edad contiene un int
print(type(edad))   # <class 'int'>

edad = "treinta"    # ✅ Ahora edad contiene un str (¡sin error!)
print(type(edad))   # <class 'str'>

edad = [1, 2, 3]    # ✅ Ahora edad contiene una lista
print(type(edad))   # <class 'list'>
```

Este código Python ejecuta sin problemas. Pero esto puede causar errores inesperados:

```python
def calcular_doble(x):
    return x * 2

print(calcular_doble(5))      # 10 (OK, número)
print(calcular_doble("hola")) # "holahola" (¿¡¿Qué?!? String repetido)
```

¿Es esto un bug o una feature? Depende de tu intención, pero el punto es que Python no te avisa que algo raro está pasando.

**Java (tipado estático):**

```java
int edad = 25;
edad = "treinta";   // ❌ Error de compilación: esto no se llega a ejecutar
```

El compilador de Java te fuerza a pensar en los tipos desde el principio. Esto puede parecer restrictivo, pero en proyectos grandes con múltiples desarrolladores, previene categorías enteras de bugs.

(tipado-fuerte-conversiones-explicitas-obligatorias)=
### Tipado Fuerte: conversiones explícitas obligatorias

Java también implementa **tipado fuerte**, lo que significa que no permite
conversiones automáticas entre tipos cuando podría haber pérdida de información o ambigüedad. Si querés convertir entre tipos, debés hacerlo **explícitamente** mediante **casting**.

#### ¿Qué es el casting?

El **casting** (conversión de tipos) es decirle explícitamente al compilador: "Sé que estos tipos son diferentes, pero quiero hacer esta conversión de todas formas". La sintaxis es poner el tipo destino entre paréntesis antes del valor:

```java
double precio = 19.99;
int precioEntero = (int) precio;  // Casting explícito: convierte a int
System.out.println(precioEntero);  // Imprime: 19 (se pierde el .99)
```
El `(int)` es el casting. Estás diciendo "convertí este `double` a `int`". El compilador acepta porque vos lo pediste explícitamente, pero te advierte implícitamente que hay pérdida de información (se pierde el `.99`).

#### ¿Por qué Java no hace esto automáticamente?

Sin el casting, este código no compila:

```java
double precio = 19.99;
int precioEntero = precio;  // ❌ Error: possible lossy conversion from double to int
```

:::{note} Más detalles sobre tipos
Los tipos primitivos, sus rangos, las reglas de conversión (widening y narrowing), y todos los detalles técnicos sobre el sistema de tipos se estudian en profundidad en el capítulo {ref}`tipos-de-datos-en-java`.
:::

(comparacion-con-tipado-dinamico-python)=
### Comparación con Tipado Dinámico (Python)

En lenguajes con **tipado dinámico** como Python, las variables no tienen tipos fijos:

```python
edad = 25           # edad contiene un int
edad = "treinta"    # ✅ Ahora edad contiene un str (¡sin error!)
edad = [1, 2, 3]    # ✅ Ahora edad contiene una lista
```

Este código Python ejecuta sin problemas, pero puede causar errores inesperados en tiempo de ejecución. Java detecta estos problemas en tiempo de compilación, antes de que el programa se ejecute.

(ventajas-del-tipado-estatico-y-fuerte)=
### Ventajas del tipado estático y fuerte

Ahora que entendés la diferencia, veamos por qué Java eligió este enfoque:

1. **Detección temprana de errores:** Muchos bugs se detectan en tiempo de
   compilación, antes de ejecutar el programa. No necesitás ejecutar todos los casos posibles para encontrar un error de tipos —el compilador lo encuentra por vos.

2. **Mejor rendimiento:** El compilador conoce los tipos de antemano, entonces puede generar código más eficiente. No necesita verificar tipos en cada operación durante la ejecución.

3. **Herramientas inteligentes:** Los IDEs (como IntelliJ o Eclipse) pueden ofrecerte:
   - **Autocompletado preciso:** Saben qué métodos y propiedades tiene cada variable
   - **Refactorización segura:** Pueden renombrar variables o métodos en todo el proyecto sin romper nada
   - **Detección de errores en tiempo real:** Ves los errores mientras escribís, no al ejecutar

4. **Documentación implícita:** Los tipos actúan como documentación. Cuando ves:
   ```java
   public double calcularImpuesto(double monto, double tasa)
   ```
   Sabés inmediatamente que la función recibe dos números decimales y devuelve otro decimal. No necesitás leer la documentación o el código interno.

5. **Contratos claros:** Los tipos establecen "contratos" entre diferentes partes del código. Si una función espera un `String`, no podés pasarle un `int` por error.

(tabla-comparativa-de-sistemas-de-tipos)=
### Tabla comparativa de sistemas de tipos

:::{list-table} Comparación de sistemas de tipos
:header-rows: 1
:label: tbl-sistemas-tipos

- - Característica
  - Java
  - Python
  - C
- - Tipado
  - Estático y fuerte
  - Dinámico y fuerte
  - Estático y débil
- - Declaración de tipos
  - Obligatoria
  - Opcional (type hints desde Python 3.5)
  - Obligatoria
- - Conversiones implícitas
  - Solo widening (sin pérdida)
  - Limitadas
  - Muchas (especialmente con punteros)
- - Detección de errores de tipos
  - Compilación
  - Ejecución
  - Compilación (parcial)
:::

:::{note}
C también es de tipado estático, pero es de tipado **débil**: permite muchas conversiones implícitas (especialmente con punteros) y no verifica límites de arreglos. Por eso C es más propenso a bugs de memoria que Java.
:::

(la-estructura-obligatoria-clases-en-java)=
## La Estructura Obligatoria: Clases en Java

Aunque Java es fundamentalmente un lenguaje **orientado a objetos**, en las
primeras etapas del aprendizaje trabajaremos principalmente con los aspectos
procedurales del lenguaje: tipos de datos, operadores, estructuras de control y
funciones (que en Java se llaman "métodos").

(por-que-todo-debe-estar-en-una-clase)=
### ¿Por qué todo debe estar en una clase?

Esta es una diferencia importante con C. En C, podés escribir funciones "sueltas":

**En C:**
```c
#include <stdio.h>

// Función suelta, no pertenece a ninguna estructura
int sumar(int a, int b) {
    return a + b;
}

int main() {
    printf("%d\n", sumar(3, 5));
    return 0;
}
```

**En Java, esto no es posible.** Todo código debe estar dentro de una clase:

```java
public class Calculadora {      // ← Todo dentro de una clase
    
    public static int sumar(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        System.out.println(sumar(3, 5));
    }
}
```

¿Por qué Java toma esta decisión? Porque fue diseñado desde cero como un lenguaje orientado a objetos. La filosofía es: "todo es un objeto" (o pertenece a una clase que puede crear objetos). Incluso cuando no necesitás objetos, la estructura de clase está presente.

(la-clase-como-contenedor-obligatorio)=
### La clase como "contenedor obligatorio"

Por ahora, pensá en la clase como un **contenedor obligatorio** para tu código. Es como un archivo en C, pero con reglas adicionales:

1. **El nombre del archivo debe coincidir con el nombre de la clase pública.** Si tu clase se llama `Calculadora`, el archivo debe llamarse `Calculadora.java`.

2. **Cada archivo `.java` típicamente contiene una clase pública.** Puede contener más clases, pero solo una puede ser `public`.

3. **El método `main` es el punto de entrada**, igual que en C, pero debe estar dentro de una clase.

(el-programa-java-mas-simple)=
### El programa Java más simple

```java
public class HolaMundo {
    public static void main(String[] args) {
        System.out.println("¡Hola, mundo!");
    }
}
```

Descomponiendo cada parte:

- **`public`**: Modificador de acceso. Significa que esta clase/método es accesible desde cualquier otro código. (Más adelante verás otros como `private` y `protected`.)

- **`class HolaMundo`**: Declara una clase llamada `HolaMundo`. El archivo debe llamarse `HolaMundo.java`.

- **`static`**: Indica que el método pertenece a la **clase**, no a un objeto instanciado. Esto es crucial para `main` y para nuestro uso inicial.

- **`void`**: El método no devuelve ningún valor (igual que en C).

- **`main`**: Nombre especial que la JVM busca como punto de entrada.

- **`String[] args`**: Parámetro que recibe argumentos de línea de comandos (igual que `char* argv[]` en C).

- **`System.out.println(...)`**: Forma de imprimir en Java. `System.out` es el equivalente a `stdout` en C.

:::{tip}
Por ahora, memorizá esta estructura como una "receta" necesaria. Todos tus programas empezarán así. Con el tiempo, cada parte tendrá sentido completo cuando estudies orientación a objetos.
:::

(metodos-estaticos-funciones-sin-necesidad-de-objetos)=
### Métodos Estáticos: Funciones sin necesidad de objetos

En las primeras semanas del curso, escribirás métodos dentro de clases, pero
todos serán **estáticos** (con la palabra clave `static`).

#### ¿Qué significa `static`?

La palabra `static` significa que el método (o variable) pertenece a la **clase misma**, no a los objetos que podrían crearse a partir de ella.

Para entenderlo con una analogía:
- Una **clase** es como un molde para hacer galletas
- Un **objeto** es una galleta hecha con ese molde
- Un **método estático** es algo que podés hacer con el molde directamente, sin necesidad de hacer galletas primero

Esto se verá en profundidad cuando estudies OOP. Por ahora, lo importante es:

- **Con `static`:** Podés llamar al método directamente usando el nombre de la clase: `Calculadora.sumar(3, 5)`
- **Sin `static`:** Necesitarías crear un objeto primero (lo verás más adelante)

#### Ejemplo: Biblioteca de funciones matemáticas

```java
public class Matematica {
    
    // Método estático: se puede llamar sin crear un objeto
    public static int sumar(int a, int b) {
        return a + b;
    }

    public static int restar(int a, int b) {
        return a - b;
    }

    public static double promediar(double[] numeros) {
        if (numeros.length == 0) {
            return 0.0;  // Evitar división por cero
        }
        double suma = 0;
        for (double num : numeros) {
            suma += num;
        }
        return suma / numeros.length;
    }

    public static int factorial(int n) {
        if (n < 0) {
            return -1;  // Indicar error (factorial no definido para negativos)
        }
        int resultado = 1;
        for (int i = 2; i <= n; i++) {
            resultado *= i;
        }
        return resultado;
    }

    public static void main(String[] args) {
        // Llamadas a métodos estáticos desde la misma clase
        // (no hace falta escribir "Matematica.")
        int suma = sumar(5, 3);
        System.out.println("5 + 3 = " + suma);

        int resta = restar(10, 4);
        System.out.println("10 - 4 = " + resta);

        double[] valores = {10.0, 20.0, 30.0, 40.0};
        double promedio = promediar(valores);
        System.out.println("Promedio: " + promedio);

        System.out.println("5! = " + factorial(5));
    }
}
```

**Salida:**
```
5 + 3 = 8
10 - 4 = 6
Promedio: 25.0
5! = 120
```

#### Llamar métodos estáticos desde otra clase

Si querés usar estos métodos desde otro archivo, usás el nombre de la clase:

```java
// Archivo: OtroPrograma.java
public class OtroPrograma {
    public static void main(String[] args) {
        // Llamada con nombre de clase completo
        int resultado = Matematica.sumar(100, 200);
        System.out.println("Resultado: " + resultado);
        
        // También podemos usar factorial
        System.out.println("10! = " + Matematica.factorial(10));
    }
}
```

Este es exactamente el mismo patrón que usás con métodos de Java como `Math.sqrt()`, `Math.abs()`, `Integer.parseInt()`, etc. Son métodos estáticos de clases de la biblioteca estándar.

(por-que-empezar-con-metodos-estaticos)=
### ¿Por qué empezar con métodos estáticos?

Este enfoque te permite:

1. **Concentrarte en la lógica algorítmica:** Pensás en cómo resolver problemas con funciones, entrada y salida, sin la complejidad adicional de objetos.

2. **Reutilizar conocimientos de C:** La estructura `funcion(parámetros) → resultado` es idéntica conceptualmente. Solo cambia un poco la sintaxis.

3. **Dominar tipos de datos y estructuras de control:** Practicás con `if`, `for`, `while`, arreglos, etc., antes de agregar la capa de objetos.

4. **Prepararte para OOP gradualmente:** Cuando llegue el momento de crear objetos, ya dominarás la sintaxis y podrás concentrarte en los conceptos nuevos.

:::{important}
Más adelante en el curso, aprenderás cuándo y por qué usar objetos en lugar de métodos estáticos. Hay situaciones donde los métodos estáticos son apropiados (funciones utilitarias, factories) y situaciones donde crear objetos es mejor (modelar entidades con estado y comportamiento). Por ahora, todos tus métodos serán estáticos.
:::

(diferencias-clave-entre-java-y-c)=
## Diferencias Clave entre Java y C

Dado que venís de programar en C, es útil tener un resumen de las diferencias principales que encontrarás al empezar con Java:

:::{list-table} Comparación Java vs C
:header-rows: 1
:label: tbl-java-vs-c

- - Aspecto
  - C
  - Java
- - Gestión de memoria
  - Manual (`malloc`, `free`)
  - Automática (Garbage Collector)
- - Punteros
  - Acceso directo a memoria
  - No existen (solo referencias a objetos)
- - Estructura del código
  - Funciones sueltas
  - Todo dentro de clases
- - Arreglos
  - No verifican límites (buffer overflow posible)
  - Verifican límites (lanza excepción)
- - Strings
  - Arrays de `char` terminados en `\0`
  - Tipo `String` con métodos integrados
- - Booleanos
  - No existe (se usa `int`: 0=false, ≠0=true)
  - Tipo `boolean` (`true`/`false`)
- - Compilación
  - A código máquina nativo
  - A bytecode (ejecuta en JVM)
- - Portabilidad
  - Recompilar para cada plataforma
  - El mismo bytecode en cualquier JVM
- - Archivos header
  - `.h` para declaraciones
  - No existen (todo en `.java`)
- - Preprocesador
  - `#define`, `#include`, `#ifdef`
  - No existe
:::

(ejemplo-lado-a-lado)=
### Ejemplo lado a lado

**El mismo programa en C y Java:**

```c
// programa.c
#include <stdio.h>

int calcular_suma(int arr[], int n) {
    int suma = 0;
    for (int i = 0; i < n; i++) {
        suma += arr[i];
    }
    return suma;
}

int main() {
    int numeros[] = {1, 2, 3, 4, 5};
    int n = sizeof(numeros) / sizeof(numeros[0]);
    int resultado = calcular_suma(numeros, n);
    printf("La suma es: %d\n", resultado);
    return 0;
}
```

```java
// Programa.java
public class Programa {
    
    public static int calcularSuma(int[] arr) {
        int suma = 0;
        for (int i = 0; i < arr.length; i++) {  // .length en vez de pasar n
            suma += arr[i];
        }
        return suma;
    }

    public static void main(String[] args) {
        int[] numeros = {1, 2, 3, 4, 5};
        int resultado = calcularSuma(numeros);  // No necesita pasar el tamaño
        System.out.println("La suma es: " + resultado);
    }
}
```

**Diferencias a notar:**

1. **No hay `#include`:** Java usa `import` para traer clases de otros paquetes, pero las clases básicas están disponibles automáticamente.

2. **Todo dentro de clase:** El código está envuelto en `public class Programa { ... }`.

3. **Métodos son `static`:** Equivalente a funciones libres de C para nuestro uso inicial.

4. **`int[]` en vez de `int arr[]`:** Java prefiere poner los corchetes junto al tipo, no junto al nombre de la variable.

5. **`arr.length` en vez de pasar tamaño:** Los arreglos en Java conocen su propio tamaño. No hay que calcularlo ni pasarlo como parámetro.

6. **`System.out.println` en vez de `printf`:** Diferente API para salida estándar. Java también tiene `System.out.printf()` si preferís format strings.

7. **Concatenación con `+`:** En vez de usar `%d` y format strings, Java permite concatenar directamente strings con números.

(resumen)=
## Resumen

Java se caracteriza por ser:

1. **Lenguaje de propósito general:** Puede usarse para casi cualquier tipo de software (empresarial, web, móvil, embebido, Big Data, juegos), aunque no siempre es la mejor opción para cada dominio específico.

2. **Tipado estático:** Los tipos de las variables se declaran explícitamente y se verifican en **tiempo de compilación**. El compilador detecta errores de tipos antes de que el programa ejecute.

3. **Tipado fuerte:** Las conversiones entre tipos incompatibles deben ser **explícitas** (casting). Java no "adivina" qué conversión querés hacer —te obliga a ser explícito sobre posibles pérdidas de información.

4. **Orientado a objetos por diseño:** Todo código debe estar dentro de clases. Inicialmente usamos métodos `static` para trabajar de forma similar a C, pero la estructura de clase siempre está presente.

5. **Diferencias principales con C:**
   - No hay punteros ni gestión manual de memoria
   - Los arreglos conocen su tamaño y verifican límites
   - Todo está dentro de clases
   - Tipo `boolean` nativo y `String` como tipo de primera clase

La estrategia pedagógica **"Late Objects"** que usamos en este curso permite dominar primero los fundamentos del lenguaje (tipos, operadores, estructuras de control, métodos) antes de abordar los conceptos de programación orientada a objetos. Esto aprovecha tu conocimiento previo de C mientras te prepara gradualmente para las características más avanzadas de Java.

(referencias-bibliograficas)=
## Referencias Bibliográficas

- Gosling, J., Joy, B., Steele, G., & Bracha, G. (2005). _The Java Language
  Specification_ (3rd ed.). Addison-Wesley Professional.
- Bloch, J. (2018). _Effective Java_ (3rd ed.). Addison-Wesley Professional.
- Eckel, B. (2006). _Thinking in Java_ (4th ed.). Prentice Hall.
- Deitel, P., & Deitel, H. (2017). _Java How to Program, Early Objects_ (11th
  ed.). Pearson.
- Oracle Corporation. (n.d.). _The Java Tutorials_. Recuperado de
  [https://docs.oracle.com/javase/tutorial/](https://docs.oracle.com/javase/tutorial/)

---

(ejercicios)=
## Ejercicios

````{exercise}
:label: ej-tipado-1

Explicá por qué el siguiente código genera un error de compilación y cómo solucionarlo:

```java
double precio = 19.99;
int precioEntero = precio;
```
````

````{solution} ej-tipado-1
:class: dropdown

El código genera un error porque Java tiene **tipado fuerte** y no permite conversiones implícitas que puedan causar pérdida de información (narrowing). En este caso, convertir de `double` a `int` implica descartar la parte decimal.

**Solución:**
```java
double precio = 19.99;
int precioEntero = (int) precio;  // Casting explícito
System.out.println(precioEntero);  // 19
```

El **casting explícito** `(int)` indica al compilador que el programador es
consciente de la posible pérdida de precisión.

````

```{exercise}
:label: ej-metodos-estaticos

Escribí un método estático `esPar` que reciba un número entero y devuelva `true` si es par y `false` si es impar. Luego, crea un método `main` que lo pruebe.
```

````{solution} ej-metodos-estaticos
:class: dropdown

```java
public class Utilidades {
    public static boolean esPar(int numero) {
        return numero % 2 == 0;
    }

    public static void main(String[] args) {
        System.out.println("4 es par: " + esPar(4));   // true
        System.out.println("7 es par: " + esPar(7));   // false
        System.out.println("-2 es par: " + esPar(-2)); // true
    }
}
```

**Nota:** El operador `%` (módulo) devuelve el resto de la división. Si el resto
de dividir por 2 es 0, el número es par.

````

```{exercise}
:label: ej-oop-vs-procedural

¿Por qué en Java todo programa necesita al menos una clase, incluso si solo querés escribir código procedural simple?
```

````{solution} ej-oop-vs-procedural
:class: dropdown

Java fue diseñado desde sus orígenes como un lenguaje **puramente orientado a objetos**. Esta decisión arquitectónica significa que:

1. **No existen funciones globales:** Todo método debe pertenecer a una clase.
2. **El punto de entrada es un método:** El método `main` debe estar dentro de una clase para que la JVM pueda ejecutarlo.
3. **Organización consistente:** Incluso programas simples siguen la misma estructura que aplicaciones complejas.

**Ventajas:**
- Consistencia: Todo el código sigue las mismas reglas.
- Escalabilidad: Es fácil expandir un programa simple a uno complejo.
- Integración: El código se organiza naturalmente en unidades reutilizables.

**Ejemplo mínimo:**
```java
public class HolaMundo {
    public static void main(String[] args) {
        System.out.println("¡Hola!");
    }
}
```

Aunque usemos `static` para evitar crear objetos inicialmente, la estructura de
clase está presente desde el principio.

````

```{exercise}
:label: ej-proposito-general

Java es un lenguaje de propósito general, pero ¿significa eso que es la mejor opción para cualquier proyecto? Argumentá tu respuesta con ejemplos.
```

```{solution} ej-proposito-general
:class: dropdown

**No**, ser de propósito general no significa ser óptimo para todos los casos. La elección del lenguaje depende del contexto:

**Cuándo Java es una buena elección:**
- Aplicaciones empresariales complejas (Spring Boot, Jakarta EE).
- Sistemas que requieren portabilidad entre plataformas.
- Proyectos con requisitos de escalabilidad y mantenibilidad a largo plazo.

**Cuándo otros lenguajes pueden ser mejores:**
- **Python:** Scripts rápidos, ciencia de datos, machine learning (bibliotecas como NumPy, TensorFlow).
- **Python** es preferido para: scripts rápidos, ciencia de datos, machine learning (bibliotecas como NumPy, TensorFlow, PyTorch).
- **C/C++:** Sistemas embebidos, drivers, software que requiere máximo rendimiento (motores gráficos, sistemas operativos).
- **Go:** Microservicios ligeros con alta concurrencia.
- **Rust:** Sistemas donde la seguridad de memoria es crítica.

**Conclusión:** Java es versátil, pero la mejor herramienta depende del problema específico, el ecosistema existente y las competencias del equipo.
```

````{exercise}
:label: ej-estructura-basica

Analizá este programa y explicá qué hace cada parte:

```java
public class Conversor {
    public static double celsiusAFahrenheit(double celsius) {
        return (celsius * 9.0 / 5.0) + 32.0;
    }

    public static void main(String[] args) {
        double temperatura = 25.0;
        double resultado = celsiusAFahrenheit(temperatura);
        System.out.println(temperatura + "°C = " + resultado + "°F");
    }
}
```
````

````{solution} ej-estructura-basica
:class: dropdown

**Análisis del programa:**

1. **`public class Conversor`**: Define una clase llamada `Conversor` que actúa como contenedor del código. El archivo debe llamarse `Conversor.java`.

2. **`public static double celsiusAFahrenheit(double celsius)`**:
   - `public`: Accesible desde cualquier parte del código.
   - `static`: Pertenece a la clase, no necesita un objeto para ser llamado.
   - `double`: Tipo de retorno, el método devuelve un número decimal.
   - `celsiusAFahrenheit`: Nombre del método (convención camelCase en Java).
   - `(double celsius)`: Parámetro de tipo `double`.
   - La fórmula `(celsius * 9.0 / 5.0) + 32.0` convierte Celsius a Fahrenheit.

3. **`public static void main(String[] args)`**:
   - Punto de entrada del programa (la JVM busca este método para comenzar).
   - `void`: No devuelve ningún valor.
   - `String[] args`: Arreglo de strings con argumentos de línea de comandos.

4. **`System.out.println(...)`**: Imprime una línea en la salida estándar. El operador `+` concatena los valores en un solo String.

**Salida esperada:**
```
25.0°C = 77.0°F
```

**Conceptos clave demostrados:**
- Estructura obligatoria de clase
- Métodos estáticos (sin necesidad de objetos)
- Tipado explícito (`double`)
- Concatenación de String con `+`
````

````{exercise}
:label: ej-widening-narrowing

Indicá cuáles de las siguientes líneas compilan y cuáles no. Para las que no compilan, explicá por qué y cómo solucionarlo:

```java
int a = 100;
long b = a;
double c = b;
int d = c;
byte e = a;
float f = 3.14;
```
````

```{solution} ej-widening-narrowing
:class: dropdown

**Análisis línea por línea:**

1. `int a = 100;` — ✅ **Compila.** Declaración simple de entero.

2. `long b = a;` — ✅ **Compila.** Widening automático: `int` (32 bits) → `long` (64 bits). No hay pérdida de información.

3. `double c = b;` — ✅ **Compila.** Widening automático: `long` (64 bits entero) → `double` (64 bits punto flotante). Seguro para la mayoría de valores.

4. `int d = c;` — ❌ **No compila.** Narrowing: `double` → `int` podría perder información (la parte decimal).
   **Solución:** `int d = (int) c;`

5. `byte e = a;` — ❌ **No compila.** Narrowing: `int` (32 bits) → `byte` (8 bits). El valor 100 cabe en un byte, pero el compilador no lo sabe en general.
   **Solución:** `byte e = (byte) a;`

6. `float f = 3.14;` — ❌ **No compila.** Los literales decimales como `3.14` son `double` por defecto. Asignar `double` a `float` es narrowing.
   **Solución 1:** `float f = 3.14f;` (sufijo `f` indica literal float)
   **Solución 2:** `float f = (float) 3.14;`

**Regla general:** Las conversiones de "mayor a menor" (long→int, double→float, int→byte) requieren casting explícito porque pueden perder información.
```

````{exercise}
:label: ej-comparacion-c-java

Convertí el siguiente programa de C a Java, manteniendo la misma lógica pero adaptando la sintaxis:

```c
#include <stdio.h>

int maximo(int a, int b) {
    if (a > b) {
        return a;
    } else {
        return b;
    }
}

int main() {
    int x = 10, y = 25;
    int resultado = maximo(x, y);
    printf("El máximo entre %d y %d es %d\n", x, y, resultado);
    return 0;
}
```
````

````{solution} ej-comparacion-c-java
:class: dropdown

```java
public class Comparador {
    
    public static int maximo(int a, int b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

    public static void main(String[] args) {
        int x = 10, y = 25;
        int resultado = maximo(x, y);
        
        // Opción 1: Concatenación con +
        System.out.println("El máximo entre " + x + " y " + y + " es " + resultado);
        
        // Opción 2: printf (más similar a C)
        System.out.printf("El máximo entre %d y %d es %d%n", x, y, resultado);
    }
}
```

**Cambios realizados:**
1. Todo el código está dentro de `public class Comparador`
2. Se agregó `static` a los métodos
3. Se eliminó `#include` (Java no usa preprocesador)
4. `printf` se convierte en `System.out.printf` o se usa concatenación
5. En `printf` de Java, usamos `%n` en vez de `\n` para portabilidad
6. No hace falta `return 0;` en `main` de Java (es `void`)
````

````{exercise}
:label: ej-tipado-estatico-dinamico

Explicá qué imprimiría cada línea en Python y qué pasaría si intentaras lo mismo en Java:

```python
print("5" + str(3))
print("5" + 3)
print("5" * 3)
print(int("5") + 3)
```
````

```{solution} ej-tipado-estatico-dinamico
:class: dropdown

**En Python (tipado dinámico y fuerte):**

1. `print("5" + str(3))` → `"53"`
   - Convertimos explícitamente `3` a string con `str()`
   - Luego concatenamos dos strings

2. `print("5" + 3)` → ❌ `TypeError: can only concatenate str (not "int") to str`
   - Python **no** convierte implícitamente int a str
   - Es tipado **fuerte**: rechaza mezclar tipos incompatibles

3. `print("5" * 3)` → `"555"`
   - En Python, `str * int` es **repetición de string**
   - Esta es una operación válida definida para strings

4. `print(int("5") + 3)` → `8`
   - Convertimos explícitamente `"5"` a entero con `int()`
   - Sumamos dos enteros

**En Java (tipado estático y fuerte):**

1. `System.out.println("5" + 3);` → `"53"` ✅
   - Java permite concatenación de String con otros tipos usando `+`
   - Es la **única excepción** a las reglas estrictas de tipos

2. En Java no necesitás `str()`, el `+` con String ya convierte
   - `System.out.println("5" + 3);` funciona directamente

3. `System.out.println("5" * 3);` → ❌ **Error de compilación**
   - Java **no** tiene operador de repetición de string
   - Para repetir: `"5".repeat(3)` (desde Java 11) → `"555"`

4. `System.out.println(Integer.parseInt("5") + 3);` → `8` ✅
   - Usamos `Integer.parseInt()` en vez de `int()`
   - La sintaxis cambia, pero el concepto es igual

**Diferencia clave:** Tanto Python como Java son de tipado **fuerte** y rechazan mezclar tipos incompatibles. La diferencia es que Java detecta los errores en **compilación** (antes de ejecutar), mientras que Python los detecta en **ejecución** (cuando llega a esa línea).
```

---

**Anterior:** {ref}`origenes-e-historia-de-java`

**Siguiente:** Continuá explorando los fundamentos de Java con los tipos de
datos primitivos y operadores.
