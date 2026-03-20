---
title: Características del Lenguaje Java
description:
  Análisis detallado de las características fundamentales que definen a Java
  como lenguaje de programación de propósito general y con tipado estático.
---

# Características del Lenguaje Java

Java se destaca por un conjunto de características que lo convierten en uno de
los lenguajes más populares y versátiles para el desarrollo de software. En este
apunte, analizaremos en profundidad sus características fundamentales: su
naturaleza de **propósito general** y su **sistema de tipos estático y fuerte**.

:::{note} ¿Y la Orientacion a Objetos?

Este apunte se enfoca en las características básicas del lenguaje. Los
conceptos de programación orientada a objetos se estudiarán en profundidad más
adelante en el curso, una vez consolidados los fundamentos del lenguaje.

:::

## Lenguaje de Propósito General

Java es un **lenguaje de propósito general**, lo que significa que no está
diseñado para un dominio específico de aplicación, sino que puede utilizarse
para desarrollar una amplia variedad de sistemas de software.

### ¿Qué Significa Propósito General?

A diferencia de lenguajes especializados como:

- **SQL:** Diseñado específicamente para consultas y manipulación de bases de
  datos.
- **HTML/CSS:** Enfocados en la estructura y presentación de páginas web.
- **MATLAB:** Especializado en computación numérica y análisis de datos
  científicos.

Java puede utilizarse para construir prácticamente cualquier tipo de aplicación:

:::{list-table} Aplicaciones de Java por dominio
:header-rows: 1
:label: tbl-dominios-java

- - Dominio
  - Ejemplos
  - Frameworks/Herramientas
- - Aplicaciones empresariales
  - Sistemas bancarios, ERP, CRM
  - Spring, Jakarta EE, Hibernate
- - Aplicaciones web
  - Portales, servicios RESTful, APIs
  - Spring Boot, Micronaut, Quarkus
- - Aplicaciones móviles
  - Android (nativo)
  - Android SDK, Kotlin (JVM)
- - Aplicaciones de escritorio
  - IDEs, herramientas de productividad
  - JavaFX, Swing
- - Sistemas embebidos
  - Dispositivos IoT, tarjetas inteligentes
  - Java ME, Embedded Java
- - Big Data y procesamiento distribuido
  - Análisis de datos masivos
  - Hadoop, Apache Spark, Apache Kafka
- - Juegos - Minecraft, libGDX - LWJGL, jMonkeyEngine
    :::

:::{important} Ponele Java a todo

La versatilidad de Java no significa que sea la mejor opción para
todos los dominios. Por ejemplo, Python puede ser más apropiado para scripts
rápidos o aprendizaje automático, mientras que C++ es preferido para sistemas
que requieren máximo rendimiento y control del hardware.
:::

### Ventajas de un Lenguaje de Propósito General

1. **Reutilización de conocimientos:** Aprender Java permite trabajar en
   diversos proyectos sin cambiar de lenguaje.
2. **Ecosistema unificado:** Una sola plataforma para múltiples necesidades.
3. **Comunidad amplia:** Recursos, bibliotecas y soporte para cualquier dominio.

## Tipado Estático y Fuerte

Java implementa un sistema de **tipado estático y fuerte**, una característica
fundamental que influye en cómo se escribe, se verifica y se ejecuta el código.

### Tipado Estático

En un lenguaje con **tipado estático**, el tipo de cada variable debe declararse
explícitamente en tiempo de compilación y no puede cambiar durante la ejecución
del programa.

#### Ejemplo: Declaración de Variables

```java
int edad = 25;           // Variable de tipo entero
String nombre = "Ana";   // Variable de tipo cadena
double precio = 19.99;   // Variable de tipo decimal

// ERROR: No se puede cambiar el tipo
edad = "treinta";  // ❌ Error de compilación
```

#### Comparación con Tipado Dinámico

En lenguajes con **tipado dinámico** como Python o JavaScript, las variables no
tienen tipos fijos:

**Python (tipado dinámico):**

```python
edad = 25           # int
edad = "treinta"    # ✅ Ahora es str (sin error)
```

**Java (tipado estático):**

```java
int edad = 25;
edad = "treinta";   // ❌ Error de compilación
```

### Tipado Fuerte

Java también implementa **tipado fuerte**, lo que significa que no permite
conversiones implícitas (automáticas) entre tipos incompatibles. Las
conversiones deben ser **explícitas** mediante **casting**.

#### Ejemplo: Casting Explícito

```java
double precio = 19.99;
int precioEntero = (int) precio;  // Casting explícito: 19

// Sin casting, genera error de compilación
int precioEntero = precio;  // ❌ Error: incompatible types
```

#### Conversiones automáticas (Widening)

Java permite conversiones automáticas solo cuando no hay pérdida de información
(**widening**):

```java
int x = 10;
double y = x;  // ✅ Conversión automática de int a double
System.out.println(y);  // 10.0
```

Pero no en sentido contrario (**narrowing**):

```java
double precio = 19.99;
int precioEntero = precio;  // ❌ Error: requiere casting explícito
```

:::{tip}

El tipado fuerte previene errores sutiles en tiempo de ejecución al
forzar al programador a ser explícito sobre las conversiones de tipo.

:::

### Ventajas del tipado estático y fuerte

1. **Detección temprana de errores:** Muchos errores se detectan en tiempo de
   compilación, antes de ejecutar el programa.
2. **Mejor rendimiento:** El compilador puede optimizar el código conociendo los
   tipos de antemano.
3. **Autocompletado y refactorización:** Los IDEs pueden ofrecer sugerencias
   precisas.
4. **Documentación implícita:** Los tipos actúan como documentación del código.

:::{list-table} Comparación de sistemas de tipos
:header-rows: 1
:label: tbl-sistemas-tipos

- - Característica
  - Java
  - Python
  - JavaScript
- - Tipado
  - Estático y fuerte
  - Dinámico y fuerte
  - Dinámico y débil
- - Declaración de tipos
  - Obligatoria
  - Opcional (type hints)
  - No requerida
- - Conversiones implícitas
  - Limitadas
  - Limitadas
  - Frecuentes
- - Detección de errores - Compilación - Ejecución - Ejecución
    :::

### Ejemplo Completo: Tipado en Acción

```java
public class EjemploTipado {
    public static void main(String[] args) {
        // Declaraciones con tipos explícitos
        int cantidad = 5;
        double precioUnitario = 12.50;

        // Operación entre tipos compatibles
        double total = cantidad * precioUnitario;  // int * double = double
        System.out.println("Total: " + total);      // Total: 62.5

        // Casting explícito necesario para narrowing
        int totalEntero = (int) total;
        System.out.println("Total entero: " + totalEntero);  // Total entero: 62

        // Error sin casting
        // int resultado = total;  // ❌ Error de compilación

        // Conversión entre tipos incompatibles
        String texto = "100";
        int numero = Integer.parseInt(texto);  // Conversión explícita
        System.out.println("Número: " + numero);
    }
}
```

## La Naturaleza Orientada a Objetos de Java

Aunque Java es fundamentalmente un lenguaje **orientado a objetos**, en las
primeras etapas del aprendizaje trabajaremos principalmente con los aspectos
procedurales del lenguaje: tipos de datos, operadores, estructuras de control y
métodos estáticos.

### Una Observación Importante

Todo programa Java debe organizarse dentro de **clases**. Incluso el programa
más simple requiere esta estructura:

```java
public class HolaMundo {
    public static void main(String[] args) {
        System.out.println("¡Hola, mundo!");
    }
}
```

Por ahora, podés pensar en esta estructura como un "contenedor obligatorio" para
tu código. Los conceptos completos de clases, objetos, encapsulamiento, herencia
y polimorfismo se estudiarán en profundidad más adelante, una vez que domines
los fundamentos del lenguaje.

:::{tip} Inicialmente, todos tus métodos serán `static`, lo que significa que no
necesitarás crear objetos para usarlos. Esto te permite concentrarte en aprender
la sintaxis básica, los tipos de datos y las estructuras de control sin la
complejidad adicional de la programación orientada a objetos. :::

### Métodos Estáticos: Tu Punto de Partida

En las primeras semanas del curso, escribirás métodos dentro de clases, pero
todos serán **estáticos** (con la palabra clave `static`):

```java
public class Calculadora {
    public static int sumar(int a, int b) {
        return a + b;
    }

    public static double promediar(double[] numeros) {
        double suma = 0;
        for (double num : numeros) {
            suma += num;
        }
        return suma / numeros.length;
    }

    public static void main(String[] args) {
        int resultado = sumar(5, 3);
        System.out.println("5 + 3 = " + resultado);

        double[] valores = {10.0, 20.0, 30.0};
        double promedio = promediar(valores);
        System.out.println("Promedio: " + promedio);
    }
}
```

Este enfoque te permite:

- Concentrarte en la lógica algorítmica
- Dominar tipos de datos y estructuras de control
- Practicar el diseño de métodos sin la complejidad de objetos

:::{important} Más adelante en el curso, aprenderás cuándo y por qué usar
objetos en lugar de métodos estáticos. Por ahora, este enfoque simplifica el
aprendizaje de los fundamentos. :::

## Resumen

Java se caracteriza por ser:

1. **Lenguaje de propósito general:** Versátil y aplicable a múltiples dominios.
2. **Tipado estático y fuerte:** Los tipos se verifican en tiempo de compilación
   y las conversiones deben ser explícitas.
3. **Orientado a objetos por diseño:** Aunque todo el código debe organizarse en
   clases, inicialmente trabajaremos con métodos estáticos para concentrarnos en
   los fundamentos algorítmicos.

Estas características hacen de Java un lenguaje robusto, seguro y adecuado para
proyectos de gran escala. La estrategia pedagógica "Late Objects" permite
dominar primero los fundamentos del lenguaje antes de abordar los conceptos más
avanzados de la programación orientada a objetos.

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

## Ejercicios

```{exercise}
:label: ej-tipado-1

Explicá por qué el siguiente código genera un error de compilación y cómo solucionarlo:

\`\`\`java
double precio = 19.99;
int precioEntero = precio;
\`\`\`
```

````{solution} ej-tipado-1
:class: dropdown

El código genera un error porque Java tiene **tipado fuerte** y no permite conversiones implícitas que puedan causar pérdida de información (narrowing). En este caso, convertir de `double` a `int` implica descartar la parte decimal.

**Solución:**
```java
double precio = 19.99;
int precioEntero = (int) precio;  // Casting explícito
System.out.println(precioEntero);  // 19
````

El **casting explícito** `(int)` indica al compilador que el programador es
consciente de la posible pérdida de precisión.

````

```{exercise}
:label: ej-metodos-estaticos

Escribí un método estático `esPar` que reciba un número entero y devuelva `true` si es par y `false` si es impar. Luego, crea un método `main` que lo pruebe.
````

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
````

**Nota:** El operador `%` (módulo) devuelve el resto de la división. Si el resto
de dividir por 2 es 0, el número es par.

````

```{exercise}
:label: ej-oop-vs-procedural

¿Por qué en Java todo programa necesita al menos una clase, incluso si solo querés escribir código procedural simple?
````

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
````

Aunque usemos `static` para evitar crear objetos inicialmente, la estructura de
clase está presente desde el principio.

````

```{exercise}
:label: ej-proposito-general

Java es un lenguaje de propósito general, pero ¿significa eso que es la mejor opción para cualquier proyecto? Argumentá tu respuesta con ejemplos.
````

```{solution} ej-proposito-general
:class: dropdown

**No**, ser de propósito general no significa ser óptimo para todos los casos. La elección del lenguaje depende del contexto:

**Cuándo Java es una buena elección:**
- Aplicaciones empresariales complejas (Spring Boot, Jakarta EE).
- Sistemas que requieren portabilidad entre plataformas.
- Proyectos con requisitos de escalabilidad y mantenibilidad a largo plazo.

**Cuándo otros lenguajes pueden ser mejores:**
- **Python:** Scripts rápidos, ciencia de datos, machine learning (bibliotecas como NumPy, TensorFlow).
- **JavaScript/TypeScript:** Aplicaciones web frontend (React, Angular, Vue).
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

```{solution} ej-estructura-basica
:class: dropdown

**Análisis del programa:**

1. **`public class Conversor`**: Define una clase llamada `Conversor` que actúa como contenedor del código.

2. **`public static double celsiusAFahrenheit(double celsius)`**:
   - Método **estático** (no requiere crear un objeto).
   - Recibe un `double` (temperatura en Celsius).
   - Devuelve un `double` (temperatura en Fahrenheit).
   - Aplica la fórmula de conversión: `F = (C × 9/5) + 32`.

3. **`public static void main(String[] args)`**:
   - Punto de entrada del programa.
   - Define una temperatura de 25°C.
   - Llama al método `celsiusAFahrenheit` de forma directa (sin crear objetos).
   - Imprime el resultado.

**Salida esperada:**
```

25.0°C = 77.0°F

```

**Conceptos clave:**
- Uso de métodos estáticos para evitar objetos en programas simples.
- Tipado fuerte: todos los valores tienen tipos explícitos (`double`).
- Organización en clases, aunque el código sea procedural.
```

---

**Anterior:** {ref}`Orígenes e Historia de Java <apunte/01_origenes.md>`

**Siguiente:** Continuá explorando los fundamentos de Java con los tipos de
datos primitivos y operadores.
