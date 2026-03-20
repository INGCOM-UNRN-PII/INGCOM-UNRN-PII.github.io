---
title: "Sintaxis de Java: Control de Flujo"
description: Estudio técnico sobre operadores, lógica booleana y estructuras de control (lazos) en Java.
---

# Sintaxis de Java: Control de Flujo

La sintaxis de Java para el control de flujo hereda la claridad de C, pero introduce salvaguardas críticas para la robustez del software. En esta materia, nos enfocamos en el uso preciso de estas estructuras para construir algoritmos eficientes y seguros.

## Lógica Booleana y Cortocircuito

Una de las diferencias fundamentales con C es que en Java el tipo `boolean` no es un entero. Por lo tanto, estructuras como `if (1)` son errores de compilación.

### Operadores Relacionales

Los operadores relacionales comparan valores y devuelven un resultado `boolean`:

| Operador | Significado | Ejemplo |
|:---:|:---|:---|
| `==` | Igual a | `x == 5` |
| `!=` | Distinto de | `x != 5` |
| `<` | Menor que | `x < 5` |
| `>` | Mayor que | `x > 5` |
| `<=` | Menor o igual que | `x <= 5` |
| `>=` | Mayor o igual que | `x >= 5` |

### Operadores Lógicos

Los operadores lógicos combinan expresiones booleanas:

| Operador | Significado | Ejemplo |
|:---:|:---|:---|
| `&&` | AND lógico (con cortocircuito) | `a && b` |
| `\|\|` | OR lógico (con cortocircuito) | `a \|\| b` |
| `!` | NOT lógico (negación) | `!a` |

### Evaluación en Cortocircuito (_Short-circuiting_)

Los operadores `&&` (AND) y `||` (OR) realizan una evaluación perezosa:

- **`a && b`**: Si `a` es `false`, `b` no se evalúa (el resultado es necesariamente `false`).
- **`a || b`**: Si `a` es `true`, `b` no se evalúa (el resultado es necesariamente `true`).

:::{important} Seguridad con Cortocircuito
Esta característica es vital para evitar excepciones. Permite verificar una precondición en la misma línea que la operación:

```java
if (objeto != null && objeto.hacerAlgo()) { ... }
```

Si el objeto es `null`, la segunda parte nunca se ejecuta, evitando un `NullPointerException`.
:::

## Estructuras Condicionales

### La Sentencia `if`

La sentencia `if` ejecuta un bloque de código solo si una condición es verdadera.

```{code} java
:caption: Estructura básica del if

if (condicion) {
    // código que se ejecuta si condicion es true
}
```

La condición debe ser una expresión que evalúe a `boolean`. A diferencia de C, no se puede usar un entero directamente.

```{code} java
:caption: Ejemplo de if simple

int temperatura = 35;

if (temperatura > 30) {
    System.out.println("Hace calor");
}
```

### La Sentencia `if-else`

Permite ejecutar un bloque alternativo cuando la condición es falsa.

```{code} java
:caption: Estructura if-else

if (condicion) {
    // código si condicion es true
} else {
    // código si condicion es false
}
```

```{code} java
:caption: Ejemplo de if-else

int edad = 17;

if (edad >= 18) {
    System.out.println("Es mayor de edad");
} else {
    System.out.println("Es menor de edad");
}
```

### La Sentencia `if-else if-else`

Cuando se necesitan evaluar múltiples condiciones mutuamente excluyentes, se encadenan sentencias `else if`.

````{mermaid}
:align: center

flowchart TD
    Start([Inicio]) --> Cond1{nota >= 90?}
    Cond1 -->|Sí| Act1["clasificacion = 'Sobresaliente'"]
    Cond1 -->|No| Cond2{nota >= 80?}
    Cond2 -->|Sí| Act2["clasificacion = 'Muy bueno'"]
    Cond2 -->|No| Cond3{nota >= 70?}
    Cond3 -->|Sí| Act3["clasificacion = 'Bueno'"]
    Cond3 -->|No| Cond4{nota >= 60?}
    Cond4 -->|Sí| Act4["clasificacion = 'Regular'"]
    Cond4 -->|No| Act5["clasificacion = 'Insuficiente'"]
    
    Act1 --> End([Fin])
    Act2 --> End
    Act3 --> End
    Act4 --> End
    Act5 --> End
    
    style Cond1 fill:#ffe0e0,stroke:#eb2141
    style Cond2 fill:#ffe0e0,stroke:#eb2141
    style Cond3 fill:#ffe0e0,stroke:#eb2141
    style Cond4 fill:#ffe0e0,stroke:#eb2141
    style Act1 fill:#c8e6c9,stroke:#2e7d32
    style Act2 fill:#c8e6c9,stroke:#2e7d32
    style Act3 fill:#c8e6c9,stroke:#2e7d32
    style Act4 fill:#c8e6c9,stroke:#2e7d32
    style Act5 fill:#c8e6c9,stroke:#2e7d32
````

```{code} java
:caption: Estructura if-else if-else

if (condicion1) {
    // código si condicion1 es true
} else if (condicion2) {
    // código si condicion1 es false y condicion2 es true
} else if (condicion3) {
    // código si condicion1 y condicion2 son false y condicion3 es true
} else {
    // código si ninguna condición anterior es true
}
```

```{code} java
:caption: Ejemplo de clasificación de notas

int nota = 75;
String clasificacion;

if (nota >= 90) {
    clasificacion = "Sobresaliente";
} else if (nota >= 80) {
    clasificacion = "Muy bueno";
} else if (nota >= 70) {
    clasificacion = "Bueno";
} else if (nota >= 60) {
    clasificacion = "Regular";
} else {
    clasificacion = "Insuficiente";
}
```

:::{warning} Orden de las Condiciones
Las condiciones se evalúan en orden. Una vez que una condición es verdadera, el resto no se evalúa. Por eso, es importante ordenarlas desde la más específica a la más general.
:::

### El Operador Ternario `? :`

El operador ternario es una forma compacta de expresar una selección entre dos valores.

```{code} java
:caption: Sintaxis del operador ternario

resultado = condicion ? valorSiTrue : valorSiFalse;
```

```{code} java
:caption: Ejemplo del operador ternario

int edad = 20;
String estado = (edad >= 18) ? "Mayor" : "Menor";

// Equivale a:
String estado2;
if (edad >= 18) {
    estado2 = "Mayor";
} else {
    estado2 = "Menor";
}
```

:::{tip} Uso del Operador Ternario
Usá el operador ternario solo para expresiones simples donde mejore la legibilidad. Para lógica compleja, preferí `if-else` tradicional.
:::

### La Sentencia `switch`

El `switch` permite seleccionar entre múltiples alternativas basándose en el valor de una expresión. Es útil cuando se compara una variable contra varios valores constantes.

#### Switch Clásico (Sentencia)

La versión clásica del `switch` requiere `break` para evitar el "fall-through" (caída en cascada hacia el siguiente caso).

```{code} java
:caption: Estructura del switch clásico

switch (expresion) {
    case valor1:
        // código para valor1
        break;
    case valor2:
        // código para valor2
        break;
    case valor3:
    case valor4:
        // código para valor3 O valor4 (casos agrupados)
        break;
    default:
        // código si ningún caso coincide
        break;
}
```

```{code} java
:caption: Ejemplo de switch con días de la semana

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
        break;
}
```

:::{warning} El `break` es Obligatorio
Si se omite el `break`, la ejecución "cae" al siguiente caso (_fall-through_). Esto puede causar errores lógicos difíciles de detectar. En este curso, siempre usá `break` al final de cada caso (excepto cuando agrupamos casos intencionalmente).
:::

**Tipos de datos permitidos en el switch clásico:**
- `byte`, `short`, `int`, `char`
- `String` (desde Java 7)
- Tipos enumerados (`enum`)

#### Switch Moderno (Expresión, Java 14+)

El switch moderno utiliza la flecha `->` para eliminar el riesgo de caída accidental y puede devolver un valor directamente.

```{code} java
:caption: Switch expresión con sintaxis de flecha

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

```{code} java
:caption: Switch con bloques múltiples líneas y yield

int resultado = switch (estado) {
    case ACTIVO -> 1;
    case PENDIENTE -> {
        System.out.println("Procesando pendiente...");
        yield 2; // yield retorna el valor en bloques multilínea
    }
    default -> 0;
};
```

## Estructuras de Repetición: Lazos

En Java, utilizamos el término **lazos** para referirnos a los bucles. Cada tipo de lazo tiene una semántica específica según el punto de evaluación de la condición.

```{figure} 05/comparacion_lazos.svg
:label: fig-comparacion-lazos
:align: center
:width: 95%

Comparación del flujo de ejecución entre for, while y do-while.
```

### El Lazo `for`

El lazo `for` es ideal cuando se conoce de antemano la cantidad de iteraciones. Consta de tres partes separadas por punto y coma: inicialización, condición y actualización.

````{mermaid}
:align: center

flowchart TD
    Start([Inicio]) --> Init[Inicialización: i = 0]
    Init --> Cond{Condición: i < 5?}
    Cond -->|true| Body[Cuerpo del lazo]
    Body --> Update[Actualización: i = i + 1]
    Update --> Cond
    Cond -->|false| End([Fin])
    
    style Init fill:#c8e6c9,stroke:#2e7d32
    style Cond fill:#ffe0e0,stroke:#eb2141
    style Body fill:#bbdefb,stroke:#1565c0
    style Update fill:#e1bee7,stroke:#6a1b9a
````

```{code} java
:caption: Estructura del lazo for

for (inicializacion; condicion; actualizacion) {
    // código que se repite mientras condicion sea true
}
```

La ejecución sigue este orden:
1. Se ejecuta la **inicialización** (una sola vez)
2. Se evalúa la **condición**
3. Si es `true`, se ejecuta el cuerpo del lazo
4. Se ejecuta la **actualización**
5. Se vuelve al paso 2

```{code} java
:caption: Ejemplo de for que imprime números del 1 al 5

for (int i = 1; i <= 5; i = i + 1) {
    System.out.println(i);
}
// Imprime: 1, 2, 3, 4, 5
```

```{code} java
:caption: Ejemplo de for con conteo descendente

for (int i = 10; i >= 0; i = i - 1) {
    System.out.println(i);
}
// Imprime: 10, 9, 8, ..., 1, 0
```

:::{tip} Alcance de la Variable de Control
Declarar la variable de control dentro del `for` limita su **alcance** (_scope_) exclusivamente al cuerpo del lazo, liberando la memoria y evitando conflictos de nombres una vez que el lazo termina.
:::

### El Lazo `while`

El lazo `while` evalúa la condición **antes** de ejecutar el cuerpo. Si la condición es falsa desde el inicio, el cuerpo nunca se ejecuta.

````{mermaid}
:align: center

flowchart TD
    Start([Inicio]) --> Cond{Condición}
    Cond -->|true| Body[Cuerpo del lazo]
    Body --> Cond
    Cond -->|false| End([Fin])
    
    style Cond fill:#ffe0e0,stroke:#eb2141
    style Body fill:#bbdefb,stroke:#1565c0
    
    Note1[/"Puede ejecutarse 0 veces"/]
    style Note1 fill:#fff3e0,stroke:#f57c00
    End --> Note1
````

```{code} java
:caption: Estructura del lazo while

while (condicion) {
    // código que se repite mientras condicion sea true
}
```

```{code} java
:caption: Ejemplo de while para sumar números hasta llegar a un límite

int suma = 0;
int numero = 1;

while (suma < 100) {
    suma = suma + numero;
    numero = numero + 1;
}

System.out.println("Suma final: " + suma);
```

:::{tip} Patrón de Búsqueda con Bandera
En este curso, para implementar búsquedas que pueden terminar anticipadamente, usamos una variable booleana (bandera) en lugar de `break`. Consultá la {ref}`regla-0x5002`.
:::

```{figure} 05/patron_bandera.svg
:label: fig-patron-bandera
:align: center
:width: 85%

Patrón recomendado: búsqueda con bandera booleana en lugar de break.
```

```{code} java
:caption: Búsqueda con bandera (patrón recomendado)

boolean encontrado = false;
int indice = 0;

while (indice < cantidad && !encontrado) {
    if (elementos[indice] == valorBuscado) {
        encontrado = true;
    } else {
        indice = indice + 1;
    }
}

if (encontrado) {
    System.out.println("Encontrado en posición: " + indice);
} else {
    System.out.println("No encontrado");
}
```

### El Lazo `do-while`

El lazo `do-while` evalúa la condición **después** de ejecutar el cuerpo. Esto garantiza que el cuerpo se ejecuta **al menos una vez**.

````{mermaid}
:align: center

flowchart TD
    Start([Inicio]) --> Body[Cuerpo del lazo]
    Body --> Cond{Condición}
    Cond -->|true| Body
    Cond -->|false| End([Fin])
    
    style Cond fill:#ffe0e0,stroke:#eb2141
    style Body fill:#bbdefb,stroke:#1565c0
    
    Note1[/"Se ejecuta al menos 1 vez"/]
    style Note1 fill:#c8e6c9,stroke:#2e7d32
    End --> Note1
````

```{code} java
:caption: Estructura del lazo do-while

do {
    // código que se ejecuta al menos una vez
} while (condicion);
```

```{code} java
:caption: Ejemplo de do-while para validar entrada

int opcion;
do {
    System.out.println("Menú:");
    System.out.println("1. Opción A");
    System.out.println("2. Opción B");
    System.out.println("0. Salir");
    System.out.print("Seleccione: ");
    opcion = scanner.nextInt();
} while (opcion != 0);
```

:::{note} Cuándo Usar do-while
El `do-while` es útil cuando necesitás ejecutar una acción antes de verificar si debe repetirse, como en menús interactivos o validación de entrada donde al menos se pide un dato una vez.
:::

### Comparación de Lazos

| Lazo | Evaluación de Condición | Ejecución Mínima | Uso Típico |
|:---:|:---|:---:|:---|
| `for` | Antes | 0 veces | Cantidad de iteraciones conocida |
| `while` | Antes | 0 veces | Cantidad de iteraciones desconocida |
| `do-while` | Después | 1 vez | Se requiere al menos una ejecución |

### Equivalencia entre `for` y `while`

Todo lazo `for` puede reescribirse como `while` y viceversa:

```{code} java
:caption: Lazo for

for (int i = 0; i < 10; i = i + 1) {
    System.out.println(i);
}
```

```{code} java
:caption: Equivalente con while

int i = 0;
while (i < 10) {
    System.out.println(i);
    i = i + 1;
}
```

### Lazos Anidados

Es posible colocar un lazo dentro de otro. El lazo interno se ejecuta completamente por cada iteración del lazo externo.

```{code} java
:caption: Ejemplo de lazos anidados (tabla de multiplicar)

for (int fila = 1; fila <= 5; fila = fila + 1) {
    for (int columna = 1; columna <= 5; columna = columna + 1) {
        int producto = fila * columna;
        System.out.print(producto + "\t");
    }
    System.out.println(); // Nueva línea después de cada fila
}
```

:::{warning} Punto Flotante en Lazos
Nunca uses tipos `double` o `float` como variables de control en un lazo. Debido a los errores de precisión de la IEEE 754, una condición como `i != 1.0` podría no cumplirse nunca, generando un lazo infinito.
:::

## Sentencias de Control de Flujo: `break` y `continue`

Java proporciona las sentencias `break` y `continue` para alterar el flujo normal de los lazos.

### La Sentencia `break`

La sentencia `break` termina inmediatamente el lazo más interno que la contiene.

```{code} java
:caption: Ejemplo de break (uso general, no recomendado en este curso)

for (int i = 0; i < 100; i = i + 1) {
    if (valores[i] == buscado) {
        System.out.println("Encontrado en: " + i);
        break; // Sale del lazo
    }
}
```

### La Sentencia `continue`

La sentencia `continue` salta a la siguiente iteración del lazo, omitiendo el resto del código en la iteración actual.

```{code} java
:caption: Ejemplo de continue (uso general, no recomendado en este curso)

for (int i = 0; i < 10; i = i + 1) {
    if (i % 2 == 0) {
        continue; // Salta los números pares
    }
    System.out.println(i); // Solo imprime impares
}
```

:::{important} Restricción del Curso
En este curso, **no se permite** el uso de `break` ni `continue` en lazos. En su lugar, se deben usar variables booleanas (banderas) para controlar el flujo. El único contexto donde `break` está permitido es en sentencias `switch`. Consultá la {ref}`regla-0x5002` para más detalles y ejemplos.
:::

## Comparativa de Seguridad con C

1.  **Asignación en condiciones**: En C, `if (x = 5)` es un error común que asigna 5 a x y evalúa a verdadero. En Java, esto falla en compilación (a menos que x sea `boolean`), previniendo errores lógicos sutiles.
2.  **Inicialización**: Java exige que las variables locales estén inicializadas antes de ser usadas en cualquier estructura de control.

## Ejercicios de Nivel Universitario

```exercise
:label: ej-logic-short
Dada la expresión `(x != 0) && (y / x > 1)`, explicá por qué nunca lanzará una `ArithmeticException` incluso si `x` es cero.
```

```solution
:for: ej-logic-short
Gracias al cortocircuito del operador `&&`, si `x` es 0, la primera condición `(x != 0)` evalúa a `false`. En ese momento, la JVM detiene la evaluación de la expresión y no intenta ejecutar `(y / x > 1)`, evitando así la división por cero.
```

```exercise
:label: ej-for-while
Reescribí el siguiente lazo `for` como un lazo `while` equivalente:

`for (int i = 10; i > 0; i = i - 2) { System.out.println(i); }`
```

```solution
:for: ej-for-while
```java
int i = 10;
while (i > 0) {
    System.out.println(i);
    i = i - 2;
}
```

La inicialización (`int i = 10`) se mueve antes del `while`, la condición (`i > 0`) se coloca en el `while`, y la actualización (`i = i - 2`) se coloca al final del cuerpo del lazo.
```

```exercise
:label: ej-busqueda-bandera
Escribí un fragmento de código que busque el primer número negativo en una secuencia de 10 números ingresados por el usuario, usando una bandera booleana (sin usar `break`).
```

```solution
:for: ej-busqueda-bandera
```java
Scanner scanner = new Scanner(System.in);
boolean encontrado = false;
int posicion = 0;
int contador = 0;

while (contador < 10 && !encontrado) {
    System.out.print("Ingrese número " + (contador + 1) + ": ");
    int numero = scanner.nextInt();
    
    if (numero < 0) {
        encontrado = true;
        posicion = contador;
    }
    contador = contador + 1;
}

if (encontrado) {
    System.out.println("Primer negativo en posición: " + (posicion + 1));
} else {
    System.out.println("No se encontraron números negativos");
}
```
```

```exercise
:label: ej-do-while
¿Cuál es la diferencia fundamental entre `while` y `do-while`? Escribí un ejemplo donde sea más apropiado usar `do-while` que `while`.
```

```solution
:for: ej-do-while
La diferencia fundamental es que `while` evalúa la condición **antes** de ejecutar el cuerpo (puede ejecutarse 0 veces), mientras que `do-while` evalúa la condición **después** (se ejecuta al menos 1 vez).

Un caso apropiado para `do-while` es la validación de entrada donde necesitamos al menos un intento:

```java
int numero;
do {
    System.out.print("Ingrese un número entre 1 y 10: ");
    numero = scanner.nextInt();
} while (numero < 1 || numero > 10);
```

En este caso, siempre necesitamos pedir al menos un número antes de verificar si es válido, por lo que `do-while` es la estructura más apropiada.
```

## Referencias Bibliográficas

- **Schildt, H.** (2022). _Java: A Beginner's Guide_ (9na ed.). McGraw Hill. (Capítulo 3: Program Control Statements).
- **Liang, Y. D.** (2017). _Introduction to Java Programming and Data Structures_ (11va ed.). Pearson.
- **Bloch, J.** (2018). _Effective Java_ (3ra ed.). Addison-Wesley Professional.
- **Oracle Corporation.** (2023). _The Java Language Specification_. [Control Flow](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.14).

:::seealso

- {ref}`regla-0x5001` - Estilo de llaves y bloques en estructuras de control.
- {ref}`regla-0xE001` - Comparación de tipos primitivos vs objetos.
  :::
