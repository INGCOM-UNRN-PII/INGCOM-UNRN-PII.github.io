---
title: "Métodos en Java"
description: Guía completa sobre la declaración, tipos y uso de métodos en Java.
---

# Métodos en Java

Un **método** es un bloque de código que realiza una tarea específica y puede ser invocado (llamado) desde otras partes del programa. Los métodos permiten organizar el código de manera modular, evitando la repetición y facilitando el mantenimiento.

## Anatomía de un Método

La sintaxis completa de un método incluye varios componentes:

```
[modificadores] tipoRetorno nombreMetodo([parámetros]) [throws excepciones] {
    // cuerpo del método
    [return valor;]
}
```

```{code} java
:caption: Estructura completa de un método

public static int calcularSuma(int a, int b) throws ArithmeticException {
    int resultado = a + b;
    return resultado;
}
```

Desglosando cada componente:

:::{table} Componentes de la declaración de un método
:label: tbl-componentes-metodo

| Componente | Descripción | Ejemplo |
| :--------- | :---------- | :------ |
| Modificador de acceso | Visibilidad del método | `public`, `private`, `protected` |
| Otros modificadores | Características adicionales | `static`, `final`, `abstract` |
| Tipo de retorno | Tipo del valor devuelto | `int`, `String`, `void` |
| Nombre | Identificador del método | `calcularSuma` |
| Parámetros | Datos de entrada | `(int a, int b)` |
| Cláusula throws | Excepciones que puede lanzar | `throws IOException` |
| Cuerpo | Código a ejecutar | `{ ... }` |

:::

## Modificadores de Acceso

Controlan desde dónde puede ser invocado el método:

```{code} java
:caption: Modificadores de acceso

public void metodoPublico() { }      // Accesible desde cualquier clase
protected void metodoProtegido() { } // Accesible desde subclases y mismo paquete
void metodoPaquete() { }             // Accesible solo desde el mismo paquete
private void metodoPrivado() { }     // Accesible solo desde la misma clase
```

:::{table} Visibilidad según modificador de acceso
:label: tbl-visibilidad

| Modificador | Misma Clase | Mismo Paquete | Subclase | Cualquier Clase |
| :---------- | :---------: | :-----------: | :------: | :-------------: |
| `public`    | ✓ | ✓ | ✓ | ✓ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| (ninguno)   | ✓ | ✓ | ✗ | ✗ |
| `private`   | ✓ | ✗ | ✗ | ✗ |

:::

## Otros Modificadores

### `static`

Un método `static` pertenece a la **clase**, no a las instancias. Puede invocarse sin crear un objeto:

```{code} java
:caption: Métodos estáticos

public class Calculadora {
    // Método estático: se invoca con Calculadora.sumar(2, 3)
    public static int sumar(int a, int b) {
        return a + b;
    }
    
    // Método de instancia: requiere crear un objeto
    public int duplicar(int valor) {
        return valor * 2;
    }
}

// Uso
int suma = Calculadora.sumar(2, 3);  // No necesita instancia

Calculadora calc = new Calculadora();
int doble = calc.duplicar(5);  // Necesita instancia
```

:::{warning}
Dentro de un método `static` no existe la referencia `this` porque no hay instancia asociada. Por lo tanto, no se puede acceder directamente a atributos o métodos de instancia.
:::

### `final`

Un método `final` no puede ser sobreescrito por subclases:

```{code} java
:caption: Métodos final

public class Animal {
    public final void respirar() {
        System.out.println("Respirando...");
    }
}

public class Perro extends Animal {
    // ERROR: No se puede sobreescribir un método final
    // @Override
    // public void respirar() { }
}
```

### `abstract`

Un método `abstract` no tiene implementación y debe ser implementado por las subclases:

```{code} java
:caption: Métodos abstractos

public abstract class Figura {
    // Sin cuerpo, solo la firma
    public abstract double calcularArea();
    
    // Los métodos abstractos obligan a que la clase sea abstracta
}

public class Circulo extends Figura {
    private double radio;
    
    @Override
    public double calcularArea() {
        return Math.PI * radio * radio;
    }
}
```

## Tipo de Retorno

El tipo de retorno indica qué tipo de valor devuelve el método:

```{code} java
:caption: Tipos de retorno

// Retorna un entero
public int obtenerEdad() {
    return 25;
}

// Retorna un String
public String obtenerNombre() {
    return "Juan";
}

// Retorna un objeto
public Persona obtenerPersona() {
    return new Persona("Ana", 30);
}

// No retorna nada
public void imprimirMensaje(String mensaje) {
    System.out.println(mensaje);
    // return; es opcional en métodos void
}
```

:::{tip}
Un método `void` puede usar `return;` (sin valor) para terminar la ejecución anticipadamente:

```java
public void procesar(String dato) {
    if (dato == null) {
        return;  // Sale del método si dato es null
    }
    // Continúa el procesamiento...
}
```
:::

## Parámetros

### Parámetros Básicos

Cada parámetro debe declarar su tipo:

```{code} java
:caption: Declaración de parámetros

public void metodoConParametros(int numero, String texto, boolean activo) {
    // Uso de los parámetros
}
```

### Parámetros Variables (Varargs)

Permiten pasar un número variable de argumentos del mismo tipo:

```{code} java
:caption: Uso de varargs

public int sumarTodos(int... numeros) {
    int suma = 0;
    for (int n : numeros) {
        suma += n;
    }
    return suma;
}

// Uso
int resultado1 = sumarTodos(1, 2, 3);          // 6
int resultado2 = sumarTodos(1, 2, 3, 4, 5);    // 15
int resultado3 = sumarTodos();                  // 0 (arreglo vacío)
```

:::{note}
- Solo puede haber **un parámetro varargs** por método
- Debe ser el **último parámetro** de la lista
- Internamente se trata como un **arreglo**
:::

### Parámetros `final`

Marcar un parámetro como `final` impide su reasignación dentro del método:

```{code} java
:caption: Parámetros final

public void procesar(final int valor) {
    // valor = 10;  // ERROR: no se puede reasignar
    int otroValor = valor * 2;  // Lectura permitida
}
```

## Sobrecarga de Métodos

La **sobrecarga** permite definir múltiples métodos con el mismo nombre pero diferentes parámetros:

```{code} java
:caption: Sobrecarga de métodos

public class Impresora {
    
    public void imprimir(String texto) {
        System.out.println(texto);
    }
    
    public void imprimir(int numero) {
        System.out.println(numero);
    }
    
    public void imprimir(String texto, int veces) {
        for (int i = 0; i < veces; i++) {
            System.out.println(texto);
        }
    }
}
```

:::{important}
La sobrecarga se diferencia por:
- **Número** de parámetros
- **Tipo** de parámetros
- **Orden** de los tipos de parámetros

**No** se considera:
- El tipo de retorno
- Los nombres de los parámetros
- Los modificadores de acceso
:::

## Declaración de Excepciones

Los métodos pueden declarar las excepciones *checked* que pueden lanzar:

```{code} java
:caption: Declaración de excepciones

import java.io.IOException;
import java.io.FileReader;

public String leerArchivo(String ruta) throws IOException {
    FileReader reader = new FileReader(ruta);
    // ... lectura del archivo
    return contenido;
}
```

:::{note}
- Las excepciones `RuntimeException` y sus subclases no necesitan declararse, aunque es buena práctica documentarlas con Javadoc.
- Las excepciones `Exception` (excepto `RuntimeException`) deben declararse o manejarse con `try-catch`.
:::

## Invocación de Métodos

### Métodos de Instancia

Requieren un objeto para ser invocados:

```{code} java
:caption: Invocación de métodos de instancia

Persona persona = new Persona("Carlos", 28);
String nombre = persona.obtenerNombre();  // Usando el objeto
persona.saludar();
```

### Métodos Estáticos

Se invocan usando el nombre de la clase:

```{code} java
:caption: Invocación de métodos estáticos

int maximo = Math.max(10, 20);
String texto = String.valueOf(42);
double aleatorio = Math.random();
```

### Encadenamiento de Métodos

Cuando los métodos retornan `this` o un nuevo objeto del mismo tipo:

```{code} java
:caption: Encadenamiento de métodos

StringBuilder sb = new StringBuilder();
sb.append("Hola")
  .append(" ")
  .append("Mundo")
  .append("!");

String resultado = sb.toString();  // "Hola Mundo!"
```

## Recursión

Un método puede llamarse a sí mismo:

```{code} java
:caption: Método recursivo

public int factorial(int n) {
    // Caso base
    if (n <= 1) {
        return 1;
    }
    // Caso recursivo
    return n * factorial(n - 1);
}
```

:::{warning}
Toda recursión debe tener:
1. **Caso base**: condición de terminación
2. **Progreso hacia el caso base**: cada llamada recursiva debe acercarse a la condición de terminación

Sin estas condiciones, se produce un `StackOverflowError`.
:::

## Buenas Prácticas

### Nombres Descriptivos

```java
// ✗ Malo
public void p(int x) { }

// ✓ Bueno
public void procesarPedido(int numeroPedido) { }
```

### Métodos Pequeños y Enfocados

Cada método debería realizar una única tarea bien definida:

```java
// ✗ Hace demasiadas cosas
public void procesarYGuardarYEnviarEmail() { }

// ✓ Responsabilidades separadas
public void procesar() { }
public void guardar() { }
public void enviarEmail() { }
```

### Documentación con Javadoc

```{code} java
:caption: Documentación Javadoc

/**
 * Calcula el factorial de un número entero no negativo.
 *
 * @param n el número del cual calcular el factorial (debe ser >= 0)
 * @return el factorial de n
 * @throws IllegalArgumentException si n es negativo
 */
public int factorial(int n) {
    if (n < 0) {
        throw new IllegalArgumentException("n debe ser no negativo");
    }
    // implementación...
}
```

## Ejercicios

```{exercise}
:label: ej-metodos-1

Implementá un método `esPalindromo(String texto)` que determine si una cadena es un palíndromo (se lee igual de izquierda a derecha que de derecha a izquierda), ignorando mayúsculas y espacios.
```

```{solution} ej-metodos-1
```java
public boolean esPalindromo(String texto) {
    String limpio = texto.toLowerCase().replaceAll("\\s", "");
    int izquierda = 0;
    int derecha = limpio.length() - 1;
    
    while (izquierda < derecha) {
        if (limpio.charAt(izquierda) != limpio.charAt(derecha)) {
            return false;
        }
        izquierda++;
        derecha--;
    }
    return true;
}
```
```

```{exercise}
:label: ej-metodos-2

Creá una clase `Matematicas` con métodos sobrecargados `maximo` que acepten:
- Dos enteros
- Tres enteros
- Un arreglo de enteros
```

```{solution} ej-metodos-2
```java
public class Matematicas {
    
    public static int maximo(int a, int b) {
        return (a > b) ? a : b;
    }
    
    public static int maximo(int a, int b, int c) {
        return maximo(maximo(a, b), c);
    }
    
    public static int maximo(int... numeros) {
        if (numeros.length == 0) {
            throw new IllegalArgumentException("Se requiere al menos un número");
        }
        int max = numeros[0];
        for (int i = 1; i < numeros.length; i++) {
            if (numeros[i] > max) {
                max = numeros[i];
            }
        }
        return max;
    }
}
```
```

:::{seealso}
- [Documentación oficial sobre métodos](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html)
- [Sobrecarga de métodos](https://docs.oracle.com/javase/tutorial/java/javaOO/methods.html)
:::
