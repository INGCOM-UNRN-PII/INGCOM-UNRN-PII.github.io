---
title: "Sintaxis de Java: Control de Flujo"
description: Guía sobre constantes, operadores y estructuras de control en Java.
---

# Sintaxis de Java: Control de Flujo

Este apunte cubre los elementos fundamentales de la sintaxis de Java: la declaración de constantes, el uso de operadores y las estructuras de control que determinan el flujo de ejecución del programa.

## Constantes

Las constantes son valores que no cambian durante la ejecución del programa. Se declaran con la palabra clave `final`:

```{code} java
:caption: Declaración de constantes

// Constante local
final double PI = 3.14159;
final int MAX_INTENTOS = 3;

// Constante de clase (típicamente con static)
public static final int DIAS_SEMANA = 7;
public static final String MENSAJE_ERROR = "Ha ocurrido un error";
```

:::{tip}
**Convención de nombres**: las constantes se escriben en `SNAKE_CASE_MAYUSCULAS` para distinguirlas visualmente de las variables.
:::

### Parámetros Final

Los parámetros de métodos también pueden ser `final`:

```{code} java
:caption: Parámetros final

public int calcular(final int valor) {
    // valor = 10;  // ERROR: no se puede reasignar
    return valor * 2;
}
```

## Operadores

### Operadores Aritméticos

:::{table} Operadores aritméticos
:label: tbl-op-aritmeticos

| Operador | Descripción | Ejemplo | Resultado |
| :------: | :---------- | :------ | :-------- |
| `+` | Suma | `5 + 3` | `8` |
| `-` | Resta | `5 - 3` | `2` |
| `*` | Multiplicación | `5 * 3` | `15` |
| `/` | División | `5 / 3` | `1` (entero) |
| `%` | Módulo (resto) | `5 % 3` | `2` |

:::

:::{warning}
**División entera**: cuando ambos operandos son enteros, el resultado es entero (se trunca la parte decimal):

```java
int resultado = 5 / 3;     // resultado = 1 (no 1.666...)
double correcto = 5.0 / 3; // correcto = 1.666...
double tambien = (double) 5 / 3; // cast explícito
```
:::

### Operadores de Asignación

:::{table} Operadores de asignación
:label: tbl-op-asignacion

| Operador | Equivalente a | Ejemplo |
| :------: | :------------ | :------ |
| `=` | Asignación simple | `a = 5` |
| `+=` | `a = a + b` | `a += 3` |
| `-=` | `a = a - b` | `a -= 3` |
| `*=` | `a = a * b` | `a *= 3` |
| `/=` | `a = a / b` | `a /= 3` |
| `%=` | `a = a % b` | `a %= 3` |

:::

### Operadores de Incremento y Decremento

```{code} java
:caption: Operadores de incremento y decremento

int a = 5;

// Postfijo: usa el valor actual, luego incrementa/decrementa
int b = a++;  // b = 5, luego a = 6
int c = a--;  // c = 6, luego a = 5

// Prefijo: incrementa/decrementa primero, luego usa el valor
int d = ++a;  // a = 6, luego d = 6
int e = --a;  // a = 5, luego e = 5
```

### Operadores Relacionales (Comparación)

:::{table} Operadores relacionales
:label: tbl-op-relacionales

| Operador | Descripción | Ejemplo | Resultado |
| :------: | :---------- | :------ | :-------- |
| `==` | Igual a | `5 == 3` | `false` |
| `!=` | Diferente de | `5 != 3` | `true` |
| `>` | Mayor que | `5 > 3` | `true` |
| `<` | Menor que | `5 < 3` | `false` |
| `>=` | Mayor o igual | `5 >= 5` | `true` |
| `<=` | Menor o igual | `5 <= 3` | `false` |

:::

### Operadores Lógicos

:::{table} Operadores lógicos
:label: tbl-op-logicos

| Operador | Descripción | Ejemplo | Resultado |
| :------: | :---------- | :------ | :-------- |
| `&&` | AND lógico (cortocircuito) | `true && false` | `false` |
| `\|\|` | OR lógico (cortocircuito) | `true \|\| false` | `true` |
| `!` | NOT lógico | `!true` | `false` |

:::

:::{note}
**Evaluación en cortocircuito**: 
- `&&` no evalúa el segundo operando si el primero es `false`
- `||` no evalúa el segundo operando si el primero es `true`

Esto es útil para evitar errores:
```java
// Seguro: no evalúa lista.size() si lista es null
if (lista != null && lista.size() > 0) {
    // ...
}
```
:::

### Operador Ternario

Una forma compacta de expresar condiciones simples:

```{code} java
:caption: Operador ternario

int edad = 20;
String categoria = (edad >= 18) ? "adulto" : "menor";

// Equivalente a:
String categoria2;
if (edad >= 18) {
    categoria2 = "adulto";
} else {
    categoria2 = "menor";
}
```

## Estructuras Condicionales

### if-else

La estructura condicional más básica:

```{code} java
:caption: Estructura if-else

int nota = 75;

if (nota >= 90) {
    System.out.println("Excelente");
} else if (nota >= 70) {
    System.out.println("Aprobado");
} else if (nota >= 60) {
    System.out.println("Recuperatorio");
} else {
    System.out.println("Desaprobado");
}
```

:::{tip}
Aunque las llaves son opcionales para bloques de una sola línea, **siempre es recomendable usarlas** para evitar errores:

```java
// ✗ Peligroso
if (condicion)
    hacerAlgo();
    hacerOtraCosa();  // ¡Esto siempre se ejecuta!

// ✓ Seguro
if (condicion) {
    hacerAlgo();
    hacerOtraCosa();
}
```
:::

### switch

Para comparar una variable contra múltiples valores:

```{code} java
:caption: Switch clásico

int diaSemana = 3;
String nombreDia;

switch (diaSemana) {
    case 1:
        nombreDia = "Lunes";
        break;
    case 2:
        nombreDia = "Martes";
        break;
    case 3:
        nombreDia = "Miércoles";
        break;
    case 4:
        nombreDia = "Jueves";
        break;
    case 5:
        nombreDia = "Viernes";
        break;
    case 6:
    case 7:
        nombreDia = "Fin de semana";
        break;
    default:
        nombreDia = "Día inválido";
}
```

:::{warning}
**Fall-through**: sin `break`, la ejecución continúa al siguiente `case`:

```java
switch (valor) {
    case 1:
        System.out.println("Uno");
        // Sin break: también ejecuta el caso 2
    case 2:
        System.out.println("Dos");
        break;
}
// Si valor = 1, imprime "Uno" y "Dos"
```
:::

### Switch Expressions (Java 14+)

Una forma más moderna y segura:

```{code} java
:caption: Switch expression

int diaSemana = 3;

String nombreDia = switch (diaSemana) {
    case 1 -> "Lunes";
    case 2 -> "Martes";
    case 3 -> "Miércoles";
    case 4 -> "Jueves";
    case 5 -> "Viernes";
    case 6, 7 -> "Fin de semana";
    default -> "Día inválido";
};
```

## Estructuras de Repetición (Lazos)

### while

Repite mientras la condición sea verdadera (evalúa antes de cada iteración):

```{code} java
:caption: Lazo while

int contador = 0;

while (contador < 5) {
    System.out.println("Contador: " + contador);
    contador++;
}
// Imprime: 0, 1, 2, 3, 4
```

### do-while

Garantiza al menos una ejecución (evalúa después de cada iteración):

```{code} java
:caption: Lazo do-while

int contador = 10;

do {
    System.out.println("Contador: " + contador);
    contador++;
} while (contador < 5);
// Imprime: 10 (se ejecuta una vez aunque la condición sea falsa)
```

### for tradicional

Ideal cuando se conoce el número de iteraciones:

```{code} java
:caption: Lazo for tradicional

for (int i = 0; i < 5; i++) {
    System.out.println("Iteración: " + i);
}
// Imprime: 0, 1, 2, 3, 4
```

La estructura es: `for (inicialización; condición; actualización)`

```{code} java
:caption: Variaciones del for

// Múltiples variables
for (int i = 0, j = 10; i < j; i++, j--) {
    System.out.printf("i=%d, j=%d%n", i, j);
}

// Lazo infinito
for (;;) {
    // Necesita un break para salir
}

// Contando hacia atrás
for (int i = 10; i >= 0; i--) {
    System.out.println(i);
}
```

### for-each (for mejorado)

Para recorrer colecciones y arreglos de forma simplificada:

```{code} java
:caption: Lazo for-each

String[] frutas = {"manzana", "banana", "naranja"};

for (String fruta : frutas) {
    System.out.println(fruta);
}

// También funciona con colecciones
List<Integer> numeros = List.of(1, 2, 3, 4, 5);

for (int numero : numeros) {
    System.out.println(numero);
}
```

:::{note}
El for-each es de **solo lectura**: no permite modificar el arreglo/colección durante la iteración. Para modificar elementos, usá el for tradicional con índice.
:::

## Control de Lazos

### break

Sale inmediatamente del lazo actual:

```{code} java
:caption: Uso de break

for (int i = 0; i < 10; i++) {
    if (i == 5) {
        break;  // Sale del lazo
    }
    System.out.println(i);
}
// Imprime: 0, 1, 2, 3, 4
```

### continue

Salta a la siguiente iteración:

```{code} java
:caption: Uso de continue

for (int i = 0; i < 10; i++) {
    if (i % 2 == 0) {
        continue;  // Salta los pares
    }
    System.out.println(i);
}
// Imprime: 1, 3, 5, 7, 9
```

### Etiquetas (Labels)

Para controlar lazos anidados:

```{code} java
:caption: Break y continue con etiquetas

externo:
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (i == 1 && j == 1) {
            break externo;  // Sale de ambos lazos
        }
        System.out.printf("(%d,%d) ", i, j);
    }
}
// Imprime: (0,0) (0,1) (0,2) (1,0)
```

## Patrones Comunes

### Validación de Entrada

```{code} java
:caption: Validación de entrada con lazo

Scanner scanner = new Scanner(System.in);
int numero;

while (true) {
    System.out.print("Ingresá un número positivo: ");
    if (scanner.hasNextInt()) {
        numero = scanner.nextInt();
        if (numero > 0) {
            break;  // Entrada válida
        }
        System.out.println("El número debe ser positivo.");
    } else {
        System.out.println("Eso no es un número.");
        scanner.next();  // Descarta la entrada inválida
    }
}
```

### Búsqueda en Arreglo

```{code} java
:caption: Búsqueda con salida temprana

int[] numeros = {5, 8, 12, 3, 9, 15, 7};
int buscado = 9;
int indice = -1;

for (int i = 0; i < numeros.length; i++) {
    if (numeros[i] == buscado) {
        indice = i;
        break;  // Encontrado, no seguir buscando
    }
}

if (indice >= 0) {
    System.out.println("Encontrado en índice: " + indice);
} else {
    System.out.println("No encontrado");
}
```

### Acumulador

```{code} java
:caption: Patrón acumulador

int[] valores = {1, 2, 3, 4, 5};
int suma = 0;

for (int valor : valores) {
    suma += valor;
}

System.out.println("Suma total: " + suma);  // 15
```

## Ejercicios

```{exercise}
:label: ej-control-1

Escribí un programa que imprima los números del 1 al 100, pero para los múltiplos de 3 imprima "Fizz", para los múltiplos de 5 imprima "Buzz", y para los múltiplos de ambos imprima "FizzBuzz".
```

```{solution} ej-control-1
```java
for (int i = 1; i <= 100; i++) {
    if (i % 3 == 0 && i % 5 == 0) {
        System.out.println("FizzBuzz");
    } else if (i % 3 == 0) {
        System.out.println("Fizz");
    } else if (i % 5 == 0) {
        System.out.println("Buzz");
    } else {
        System.out.println(i);
    }
}
```
```

```{exercise}
:label: ej-control-2

Implementá un método que reciba un número entero positivo y retorne `true` si es primo, `false` en caso contrario.
```

```{solution} ej-control-2
```java
public boolean esPrimo(int n) {
    if (n < 2) {
        return false;
    }
    if (n == 2) {
        return true;
    }
    if (n % 2 == 0) {
        return false;
    }
    
    for (int i = 3; i * i <= n; i += 2) {
        if (n % i == 0) {
            return false;
        }
    }
    return true;
}
```
```

```{exercise}
:label: ej-control-3

Usando un switch expression, implementá un método que reciba el número de un mes (1-12) y retorne la cantidad de días de ese mes (asumí que febrero tiene 28 días).
```

```{solution} ej-control-3
```java
public int diasDelMes(int mes) {
    return switch (mes) {
        case 1, 3, 5, 7, 8, 10, 12 -> 31;
        case 4, 6, 9, 11 -> 30;
        case 2 -> 28;
        default -> throw new IllegalArgumentException("Mes inválido: " + mes);
    };
}
```
```

:::{seealso}
- [Documentación sobre operadores](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/operators.html)
- [Estructuras de control](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/flow.html)
- [Switch expressions](https://docs.oracle.com/en/java/javase/14/language/switch-expressions.html)
:::
