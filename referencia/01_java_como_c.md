(referencia-java-como-c)=
# Java para Programadores C

Esta referencia de sintaxis está diseñada para ayudarte en la transición de C a Java durante la primera parte de la cátedra. En esta etapa, usaremos Java de una forma "imperativa", centrándonos en la lógica y algoritmos antes de sumergirnos en la Programación Orientada a Objetos (POO).

---

## 1. Estructura Básica de un Programa

En Java, a diferencia de C, **todo** debe estar dentro de una clase. Pensá en la clase como un contenedor obligatorio para tu código.

```java
public class MiPrograma { // El nombre del archivo debe ser MiPrograma.java

    // Este es el punto de entrada, equivalente al main() de C
    public static void main(String[] args) {
        // Tu código va acá
        System.out.println("Hola desde Java!");
    }
}
```

:::{note} Similitudes con C
- Los bloques de código se delimitan con llaves `{ }`.
- Las sentencias terminan con punto y coma `;`.
- Los comentarios usan `//` para una línea y `/* ... */` para bloques.
:::

Para más detalles sobre por qué usamos clases como contenedores, consultá {doc}`../parte_1/02_lenguaje`.

---

## 2. Tipos de Datos Primitivos

Java tiene tipos de datos muy similares a C, pero con una diferencia fundamental: **sus tamaños están garantizados** por la Máquina Virtual de Java (JVM), independientemente de la plataforma.

| Tipo en Java | Equivalente en C | Tamaño | Descripción |
| :--- | :--- | :--- | :--- |
| `int` | `int` | 32 bits | Entero estándar. |
| `long` | `long long` | 64 bits | Entero de rango extendido. |
| `double` | `double` | 64 bits | Punto flotante de doble precisión (preferido). |
| `float` | `float` | 32 bits | Punto flotante de precisión simple. |
| `char` | `char` | 16 bits | Carácter Unicode (no solo ASCII). |
| `boolean` | `int` (0 o 1) | - | Valores `true` o `false`. |

:::{important} El tipo `boolean` es estricto
En Java, no podés usar un entero como si fuera un valor lógico. Esto significa que `if (1)` es un error de compilación. Debés usar `true` o `false` explícitamente.
:::

Podés profundizar en estos tipos en {doc}`../parte_1/03_tipos_de_datos`.

---

## 3. Estructuras de Control (Lazos y Condicionales)

La buena noticia es que los **lazos** y condicionales son prácticamente idénticos a los de C.

### Condicionales

```java
if (edad >= 18) {
    System.out.println("Es mayor de edad");
} else if (edad > 0) {
    System.out.println("Es menor de edad");
} else {
    System.out.println("Edad no válida");
}
```

### Lazos (Loops)

Java soporta los tres lazos clásicos:

- `for (int i = 0; i < n; i++) { ... }`
- `while (condicion) { ... }`
- `do { ... } while (condicion);`

:::{tip} Estilo de Lazos
Recordá que en la cátedra preferimos el término **lazo** en lugar de bucle. Consultá la regla de estilo sobre el uso de lazos en {doc}`../parte_1/05_sintaxis_control`.
:::

---

## 4. Métodos (Funciones)

Lo que en C llamás "funciones", en Java lo llamamos **métodos**. Durante esta primera parte, todos nuestros métodos llevarán la palabra clave `static`, lo que permite invocarlos sin necesidad de crear objetos.

### Declaración de un Método

```java
// Equivalente a: int sumar(int a, int b) en C
public static int sumar(int a, int b) {
    return a + b;
}
```

### Llamadas a funciones (Invocación)

```java
int resultado = sumar(5, 3);
```

Para entender cómo se gestiona la memoria durante las llamadas a métodos, revisá {doc}`../parte_1/04_metodos`.

---

## 5. Arreglos (Arrays)

A diferencia de C, los arreglos en Java son **objetos**. Esto significa que:
1. Conocen su propio tamaño a través del atributo `.length`.
2. Se inicializan automáticamente con valores por defecto (ej: `0` para `int`).
3. Son más seguros: la JVM verifica que no te salgas de los límites.

```java
// Declaración y creación de un arreglo de 10 enteros
int[] numeros = new int[10];

// Acceso y asignación
numeros[0] = 42;
int largo = numeros.length; // En C tendrías que pasar el tamaño por separado
```

Para una comparativa detallada entre los arreglos de Java y C, consultá {doc}`../parte_1/09_arreglos`.

---

## 6. Entrada y Salida Básica

### Salida por Consola

Java utiliza `System.out`. El método más parecido al `printf` de C es, justamente, `printf`.

```java
System.out.println("Imprime y salta de línea");
System.out.print("Imprime sin saltar");
System.out.printf("Formateado: %s tiene %d años\n", nombre, edad);
```

### Entrada de Datos

Para leer datos del usuario, usamos la clase `Scanner`.

```java
import java.util.Scanner;

// ... dentro del main ...
Scanner teclado = new Scanner(System.in);
System.out.print("Ingresá un número: ");
int numero = teclado.nextInt();
```

Encontrá más ejemplos en {doc}`../parte_1/06_entrada_salida`.

---

## 7. Reglas de Estilo Fundamentales

Para que tu código sea legible y profesional, debés seguir las convenciones de la cátedra:

- **Nomenclatura:** 
  - Clases en `CamelloCase` (ej: `CalculadoraEstadistica`).
  - Métodos y variables en `dromedarioCase` (ej: `calcularPromedio`).
  - Constantes en `SNAKE_CASE` (ej: `VALOR_MAXIMO`).
- **Nombres descriptivos:** Evitá variables de una sola letra (salvo en lazos simples).

Consultá el {doc}`../reglas/indice` para el listado completo de reglas, como la {ref}`regla-0x0001` o la {ref}`regla-0x0003`.
