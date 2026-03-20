---
title: "Excepciones en Java"
description: Estudio avanzado sobre la gestión de errores, jerarquía de Throwable y robustez en el diseño de software.
---

# Excepciones en Java

El manejo de excepciones en Java es un paradigma completo para el diseño de software robusto. A diferencia de C, donde el flujo de error se mezcla con el flujo de datos (usando códigos de retorno como `-1` o `NULL`), Java separa ambos canales, permitiendo una gestión más limpia y centralizada.

## El Problema del Manejo de Errores en C

En C, el manejo de errores tradicional presenta varios problemas:

```{code} c
:caption: Manejo de errores típico en C

// En C: el error se mezcla con el valor de retorno
int resultado = abrir_archivo("datos.txt");
if (resultado == -1) {
    // Error: archivo no encontrado
}
if (resultado == -2) {
    // Error: sin permisos
}
// ... ¿y si -1 o -2 fueran valores válidos?

// Alternativa: variable global errno
FILE *f = fopen("datos.txt", "r");
if (f == NULL) {
    if (errno == ENOENT) {
        // Archivo no existe
    } else if (errno == EACCES) {
        // Sin permisos
    }
}
```

**Problemas de este enfoque:**
1. **Mezcla de canales**: El valor de retorno debe transmitir tanto el resultado exitoso como el código de error.
2. **Fácil de ignorar**: El programador puede olvidar verificar el código de error.
3. **Propagación manual**: Cada función intermedia debe propagar el error explícitamente.
4. **Sin información de contexto**: Un código numérico no indica dónde ni por qué ocurrió el error.

## La Solución de Java: Excepciones

Java introduce un mecanismo que separa completamente el flujo normal del flujo de errores:

```{code} java
:caption: Manejo de errores con excepciones en Java

// El flujo normal no se contamina con códigos de error
try {
    int resultado = leerArchivo("datos.txt");
    procesarDatos(resultado);
    guardarResultado();
} catch (FileNotFoundException e) {
    System.out.println("Error: el archivo no existe");
    System.out.println("Ubicación del error: " + e.getMessage());
} catch (IOException e) {
    System.out.println("Error de lectura/escritura");
}
```

**Ventajas:**
1. **Separación clara**: El código de éxito está en el `try`, el código de error en el `catch`.
2. **Imposible de ignorar**: Las excepciones _checked_ deben manejarse o declararse.
3. **Propagación automática**: Si no se captura, la excepción sube automáticamente.
4. **Información rica**: La excepción incluye mensaje, tipo, y traza de ejecución.

## Sintaxis Básica: try-catch

### Estructura Fundamental

```{code} java
:caption: Estructura try-catch básica

try {
    // Código que puede lanzar una excepción
    // Si ocurre una excepción, el flujo salta al catch correspondiente
} catch (TipoDeExcepcion nombreVariable) {
    // Código para manejar la excepción
    // nombreVariable contiene información sobre el error
}
```

### Ejemplo Práctico: División por Cero

```{code} java
:caption: Captura de ArithmeticException

public static int dividir(int dividendo, int divisor) {
    int resultado = 0;
    
    try {
        resultado = dividendo / divisor;
        System.out.println("División exitosa");
    } catch (ArithmeticException e) {
        System.out.println("Error: división por cero");
        System.out.println("Mensaje: " + e.getMessage());
        resultado = 0;  // Valor por defecto en caso de error
    }
    
    return resultado;
}

// Uso:
int r1 = dividir(10, 2);  // Imprime "División exitosa", retorna 5
int r2 = dividir(10, 0);  // Imprime "Error: división por cero", retorna 0
```

### Múltiples Bloques catch

Podés capturar diferentes tipos de excepciones con bloques `catch` separados:

```{code} java
:caption: Múltiples bloques catch

try {
    int[] numeros = {1, 2, 3};
    int valor = numeros[10];      // Puede lanzar ArrayIndexOutOfBoundsException
    int resultado = valor / 0;     // Puede lanzar ArithmeticException
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Error: índice fuera del arreglo");
} catch (ArithmeticException e) {
    System.out.println("Error: operación aritmética inválida");
}
```

:::{important} Orden de los catch
Los bloques `catch` se evalúan en orden. Si una excepción coincide con el primer `catch`, los siguientes no se ejecutan. Siempre colocá las excepciones más específicas primero.
:::

### Captura Múltiple (Java 7+)

Si querés manejar varias excepciones de la misma manera:

```{code} java
:caption: Captura múltiple con pipe

try {
    // código que puede lanzar varias excepciones
} catch (ArithmeticException | ArrayIndexOutOfBoundsException e) {
    System.out.println("Error de cálculo o acceso: " + e.getMessage());
}
```

## El Bloque finally

El bloque `finally` se ejecuta **siempre**, haya o no excepción. Es útil para liberar recursos.

```{figure} 10/flujo_try_catch_finally.svg
:label: fig-flujo-try-catch-finally
:align: center
:width: 85%

Flujo de ejecución del bloque try-catch-finally en diferentes escenarios.
```

```{code} java
:caption: Uso del bloque finally

Scanner scanner = null;

try {
    scanner = new Scanner(System.in);
    System.out.print("Ingrese un número: ");
    int numero = scanner.nextInt();
    System.out.println("El doble es: " + (numero * 2));
} catch (InputMismatchException e) {
    System.out.println("Error: no ingresó un número válido");
} finally {
    // Esto se ejecuta SIEMPRE
    System.out.println("Limpiando recursos...");
    if (scanner != null) {
        scanner.close();
    }
}
```

### Flujo de Ejecución con finally

| Escenario | try | catch | finally |
|:---|:---:|:---:|:---:|
| Sin excepción | ✅ Completo | ❌ No ejecuta | ✅ Ejecuta |
| Excepción capturada | ✅ Parcial | ✅ Ejecuta | ✅ Ejecuta |
| Excepción no capturada | ✅ Parcial | ❌ No coincide | ✅ Ejecuta |

```{code} java
:caption: Demostración del flujo de finally

public static void demostrar(int opcion) {
    try {
        System.out.println("1. Inicio del try");
        
        if (opcion == 1) {
            System.out.println("2. Operación exitosa");
        } else if (opcion == 2) {
            throw new RuntimeException("Error simulado");
        }
        
        System.out.println("3. Fin del try");
    } catch (RuntimeException e) {
        System.out.println("4. En el catch: " + e.getMessage());
    } finally {
        System.out.println("5. En el finally (SIEMPRE)");
    }
    System.out.println("6. Después del try-catch-finally");
}

// demostrar(1) imprime: 1, 2, 3, 5, 6
// demostrar(2) imprime: 1, 4, 5, 6
```

## Tipos de Excepciones

### Jerarquía de Throwable

En Java, todo lo que puede ser lanzado (`throw`) o capturado (`catch`) forma parte de una jerarquía:

```{figure} 10/jerarquia_excepciones.svg
:label: fig-jerarquia-excepciones
:align: center
:width: 85%

Jerarquía completa de Throwable: Error vs Exception, Checked vs Unchecked.
```

### Excepciones Checked vs Unchecked

Java distingue entre dos categorías de excepciones:

**Excepciones Checked (Comprobadas):**
- Heredan de `Exception` (pero no de `RuntimeException`)
- El compilador **obliga** a manejarlas o declararlas
- Representan condiciones externas previsibles
- Ejemplos: `IOException`, `FileNotFoundException`, `SQLException`

**Excepciones Unchecked (No Comprobadas):**
- Heredan de `RuntimeException`
- No requieren manejo obligatorio
- Representan errores de programación
- Ejemplos: `NullPointerException`, `ArrayIndexOutOfBoundsException`, `ArithmeticException`

```{code} java
:caption: Diferencia entre checked y unchecked

// UNCHECKED: No requiere try-catch ni throws
public static int dividir(int a, int b) {
    return a / b;  // ArithmeticException si b == 0
}

// CHECKED: El compilador obliga a manejarla
public static String leerArchivo(String ruta) throws IOException {
    // Si no ponés "throws IOException", el código no compila
    BufferedReader reader = new BufferedReader(new FileReader(ruta));
    return reader.readLine();
}
```

### Excepciones Comunes en Java

| Excepción | Tipo | Causa Común |
|:---|:---:|:---|
| `NullPointerException` | Unchecked | Usar una referencia que es `null` |
| `ArrayIndexOutOfBoundsException` | Unchecked | Índice fuera de rango del arreglo |
| `ArithmeticException` | Unchecked | División por cero (enteros) |
| `NumberFormatException` | Unchecked | Convertir String inválido a número |
| `IllegalArgumentException` | Unchecked | Argumento con valor no permitido |
| `InputMismatchException` | Unchecked | Scanner recibe tipo incorrecto |
| `IOException` | Checked | Error de entrada/salida |
| `FileNotFoundException` | Checked | Archivo no existe |

## Lanzar Excepciones: throw

Podés lanzar excepciones explícitamente usando `throw`:

```{code} java
:caption: Lanzar excepciones con throw

public static double calcularRaiz(double numero) {
    if (numero < 0) {
        throw new IllegalArgumentException("No se puede calcular raíz de número negativo: " + numero);
    }
    return Math.sqrt(numero);
}

// Uso:
double r1 = calcularRaiz(16);   // Retorna 4.0
double r2 = calcularRaiz(-5);   // Lanza IllegalArgumentException
```

### Validación de Precondiciones

Es una buena práctica validar los parámetros al inicio del método:

```{code} java
:caption: Validación de precondiciones

public static double calcularPromedio(int[] numeros) {
    // Validar precondiciones
    if (numeros == null) {
        throw new IllegalArgumentException("El arreglo no puede ser null");
    }
    if (numeros.length == 0) {
        throw new IllegalArgumentException("El arreglo no puede estar vacío");
    }
    
    // Lógica principal (sabemos que numeros es válido)
    int suma = 0;
    for (int i = 0; i < numeros.length; i = i + 1) {
        suma = suma + numeros[i];
    }
    return (double) suma / numeros.length;
}
```

:::{tip} Lanzamiento Temprano
Validá las precondiciones lo antes posible en el método. Es mejor fallar rápido con un mensaje claro que fallar después con un error confuso.
:::

## Declarar Excepciones: throws

Para excepciones **checked**, debés declararlas en la firma del método con `throws`:

````{mermaid}
:align: center

flowchart TD
    Start([metodoA llama metodoB]) --> Check{metodoB puede<br/>lanzar IOException?}
    Check -->|Sí, checked| Decision{¿Cómo manejar?}
    
    Decision -->|Opción 1| Catch[Capturar con try-catch]
    Decision -->|Opción 2| Throws[Declarar throws en metodoA]
    
    Catch --> Handle[Manejo local del error]
    Throws --> Propagate[Propaga al llamador]
    
    Handle --> End([Fin])
    Propagate --> End
    
    style Check fill:#ffe0e0,stroke:#eb2141
    style Decision fill:#fff3e0,stroke:#f57c00
    style Catch fill:#c8e6c9,stroke:#2e7d32
    style Throws fill:#bbdefb,stroke:#1565c0
````

```{code} java
:caption: Declarar excepciones con throws

// Este método puede lanzar IOException
public static int contarLineas(String rutaArchivo) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo));
    int contador = 0;
    
    while (reader.readLine() != null) {
        contador = contador + 1;
    }
    
    reader.close();
    return contador;
}
```

### Propagar vs Manejar

Cuando un método llama a otro que declara `throws`, tenés dos opciones:

**Opción 1: Manejar la excepción (try-catch)**

```{code} java
:caption: Manejar la excepción

public static void procesarArchivo(String ruta) {
    try {
        int lineas = contarLineas(ruta);
        System.out.println("El archivo tiene " + lineas + " líneas");
    } catch (IOException e) {
        System.out.println("Error al leer el archivo: " + e.getMessage());
    }
}
```

**Opción 2: Propagar la excepción (throws)**

```{code} java
:caption: Propagar la excepción

public static void procesarArchivo(String ruta) throws IOException {
    // Propagamos la excepción al llamador
    int lineas = contarLineas(ruta);
    System.out.println("El archivo tiene " + lineas + " líneas");
}
```

:::{important} Regla de Propagación
Propagá la excepción cuando no sabés cómo manejarla en el nivel actual. Manejala cuando tenés suficiente contexto para tomar una decisión (mostrar mensaje, reintentar, usar valor por defecto, etc.).
:::

## El Costo de las Excepciones

Lanzar una excepción en Java es una operación **costosa** en términos de rendimiento. Esto se debe a que la JVM debe:

1. Crear el objeto excepción
2. Capturar el _stack trace_ completo (todas las llamadas activas)
3. Buscar el handler apropiado subiendo por la pila

```{code} java
:caption: El stack trace proporciona información valiosa

try {
    metodoA();
} catch (Exception e) {
    e.printStackTrace();  // Imprime toda la traza
}

// Salida típica:
// java.lang.ArithmeticException: / by zero
//     at Ejemplo.metodoC(Ejemplo.java:25)
//     at Ejemplo.metodoB(Ejemplo.java:20)
//     at Ejemplo.metodoA(Ejemplo.java:15)
//     at Ejemplo.main(Ejemplo.java:10)
```

:::{warning} No Usar Excepciones para Control de Flujo
Las excepciones **nunca** deben usarse para el control de flujo normal del programa:

```java
// ❌ INCORRECTO: usar excepción para control de flujo
try {
    int i = 0;
    while (true) {
        System.out.println(arreglo[i]);
        i = i + 1;
    }
} catch (ArrayIndexOutOfBoundsException e) {
    // Fin del arreglo
}

// ✅ CORRECTO: usar condición normal
for (int i = 0; i < arreglo.length; i = i + 1) {
    System.out.println(arreglo[i]);
}
```
:::

## Información de la Excepción

Las excepciones proporcionan métodos útiles para obtener información:

```{code} java
:caption: Métodos de información de excepciones

try {
    int resultado = 10 / 0;
} catch (ArithmeticException e) {
    // Mensaje descriptivo del error
    String mensaje = e.getMessage();  // "/ by zero"
    
    // Nombre completo del tipo de excepción
    String tipo = e.getClass().getName();  // "java.lang.ArithmeticException"
    
    // Imprimir traza completa en consola de error
    e.printStackTrace();
    
    // Obtener traza como arreglo (para procesamiento)
    StackTraceElement[] traza = e.getStackTrace();
    for (StackTraceElement elemento : traza) {
        System.out.println("  en " + elemento.getMethodName() + 
                          " (" + elemento.getFileName() + 
                          ":" + elemento.getLineNumber() + ")");
    }
}
```

## Try-with-resources (Java 7+)

Para recursos que deben cerrarse (archivos, conexiones), Java ofrece una sintaxis que garantiza el cierre automático:

```{code} java
:caption: Try-with-resources

// El Scanner se cierra automáticamente al salir del try
try (Scanner scanner = new Scanner(new File("datos.txt"))) {
    while (scanner.hasNextLine()) {
        System.out.println(scanner.nextLine());
    }
} catch (FileNotFoundException e) {
    System.out.println("Archivo no encontrado");
}
// No necesitás llamar a scanner.close() - se hace automáticamente
```

Esto es equivalente a:

```{code} java
:caption: Equivalente manual con finally

Scanner scanner = null;
try {
    scanner = new Scanner(new File("datos.txt"));
    while (scanner.hasNextLine()) {
        System.out.println(scanner.nextLine());
    }
} catch (FileNotFoundException e) {
    System.out.println("Archivo no encontrado");
} finally {
    if (scanner != null) {
        scanner.close();
    }
}
```

## Encadenamiento de Excepciones

Es importante no perder la causa original de un error al propagarlo. Java permite encadenar excepciones:

```{code} java
:caption: Encadenamiento de excepciones

public static void procesarDatos() {
    try {
        leerArchivo();
    } catch (IOException e) {
        // Creamos una nueva excepción pero preservamos la original como "causa"
        throw new RuntimeException("Error al procesar datos", e);
    }
}

// Al capturar:
try {
    procesarDatos();
} catch (RuntimeException e) {
    System.out.println("Error: " + e.getMessage());
    
    // Obtener la causa original
    Throwable causa = e.getCause();
    if (causa != null) {
        System.out.println("Causa: " + causa.getMessage());
    }
}
```

## Buenas Prácticas

### 1. No Silenciar Excepciones

Nunca dejes un bloque `catch` vacío:

```{code} java
:caption: No silenciar excepciones

// ❌ INCORRECTO: excepción silenciada
try {
    operacionRiesgosa();
} catch (Exception e) {
    // Vacío - ¡el error se pierde!
}

// ✅ CORRECTO: al menos registrar el error
try {
    operacionRiesgosa();
} catch (Exception e) {
    System.err.println("Error en operación: " + e.getMessage());
    e.printStackTrace();
}
```

### 2. Capturar Excepciones Específicas

Evitá capturar `Exception` de forma genérica:

```{code} java
:caption: Captura específica

// ❌ INCORRECTO: muy genérico
try {
    procesarArchivo();
} catch (Exception e) {
    System.out.println("Algo salió mal");
}

// ✅ CORRECTO: específico
try {
    procesarArchivo();
} catch (FileNotFoundException e) {
    System.out.println("El archivo no existe");
} catch (IOException e) {
    System.out.println("Error al leer el archivo");
}
```

### 3. Mensajes Descriptivos

Incluí información útil en los mensajes de excepción:

```{code} java
:caption: Mensajes descriptivos

// ❌ INCORRECTO: mensaje vago
if (edad < 0) {
    throw new IllegalArgumentException("Valor inválido");
}

// ✅ CORRECTO: mensaje informativo
if (edad < 0) {
    throw new IllegalArgumentException("La edad no puede ser negativa: " + edad);
}
```

### 4. Limpiar Recursos en finally

Siempre liberá recursos en el bloque `finally` o usá try-with-resources:

```{code} java
:caption: Liberar recursos correctamente

// ✅ CORRECTO: recursos liberados siempre
Scanner scanner = null;
try {
    scanner = new Scanner(System.in);
    // usar scanner...
} finally {
    if (scanner != null) {
        scanner.close();
    }
}

// ✅ MEJOR: try-with-resources
try (Scanner scanner = new Scanner(System.in)) {
    // usar scanner...
}
```

## Comparativa: C vs Java

| Aspecto | C | Java |
|:---|:---|:---|
| Mecanismo | Códigos de retorno, `errno` | Excepciones |
| Propagación | Manual (cada función debe verificar) | Automática |
| Información | Código numérico | Mensaje + tipo + stack trace |
| Ignorar error | Fácil (no verificar retorno) | Difícil (checked exceptions) |
| Rendimiento | Muy bajo overhead | Mayor overhead (crear excepción) |
| Limpieza recursos | Manual (`free()`, `fclose()`) | `finally` o try-with-resources |

## Ejercicios de Aplicación

```exercise
:label: ej-validar-edad
Escribí un método `validarEdad(int edad)` que lance `IllegalArgumentException` si la edad es negativa o mayor a 150, y retorne la edad si es válida.
```

````solution
:for: ej-validar-edad
```java
public static int validarEdad(int edad) {
    if (edad < 0) {
        throw new IllegalArgumentException("La edad no puede ser negativa: " + edad);
    }
    if (edad > 150) {
        throw new IllegalArgumentException("La edad no puede ser mayor a 150: " + edad);
    }
    return edad;
}

// Uso:
try {
    int edadValida = validarEdad(25);   // OK, retorna 25
    int edadInvalida = validarEdad(-5); // Lanza excepción
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
}
```
````

````exercise
:label: ej-try-finally
¿Qué devuelve el siguiente método si se llama con `dividir(10, 0)`?
```java
public int dividir(int a, int b) {
    try {
        return a / b;
    } catch (ArithmeticException e) {
        return -1;
    } finally {
        return 0;
    }
}
```
````

```solution
:for: ej-try-finally
Devuelve `0`. El bloque `finally` tiene prioridad sobre cualquier sentencia `return` previa en el `try` o `catch`. Esta es una **mala práctica** (anti-patrón), ya que el `finally` está "ocultando" el resultado real del error. Nunca pongas `return` dentro de un bloque `finally`.
```

```exercise
:label: ej-convertir-numero
Escribí un método `convertirAEntero(String texto)` que convierta un String a int, retornando 0 si el texto no es un número válido.
```

````solution
:for: ej-convertir-numero
```java
public static int convertirAEntero(String texto) {
    int resultado = 0;
    
    try {
        resultado = Integer.parseInt(texto);
    } catch (NumberFormatException e) {
        System.out.println("Advertencia: '" + texto + "' no es un número válido, usando 0");
        resultado = 0;
    }
    
    return resultado;
}

// Ejemplos:
int n1 = convertirAEntero("42");     // Retorna 42
int n2 = convertirAEntero("abc");    // Retorna 0 (con advertencia)
int n3 = convertirAEntero("3.14");   // Retorna 0 (con advertencia)
```
````

```exercise
:label: ej-orden-catch
¿Por qué el siguiente código no compila?

```java
try {
    // código
} catch (Exception e) {
    System.out.println("Error general");
} catch (ArithmeticException e) {
    System.out.println("Error aritmético");
}
```
```

```solution
:for: ej-orden-catch
No compila porque `ArithmeticException` es una subclase de `Exception`. El primer `catch` captura **todas** las excepciones (incluyendo `ArithmeticException`), por lo que el segundo `catch` es **inalcanzable** (_unreachable code_).

El compilador de Java detecta este error porque el orden de los `catch` debe ir de más específico a más general:

```java
try {
    // código
} catch (ArithmeticException e) {     // Específico primero
    System.out.println("Error aritmético");
} catch (Exception e) {               // General después
    System.out.println("Error general");
}
```
```

```exercise
:label: ej-dividir-seguro
Escribí un método `dividirSeguro(int[] numeros, int indice, int divisor)` que retorne el resultado de dividir `numeros[indice] / divisor`, manejando todos los posibles errores (índice inválido, división por cero, arreglo null) y retornando 0 en caso de error.
```

````solution
:for: ej-dividir-seguro
```java
public static int dividirSeguro(int[] numeros, int indice, int divisor) {
    int resultado = 0;
    
    try {
        if (numeros == null) {
            throw new IllegalArgumentException("El arreglo es null");
        }
        resultado = numeros[indice] / divisor;
    } catch (ArrayIndexOutOfBoundsException e) {
        System.out.println("Error: índice " + indice + " fuera de rango");
    } catch (ArithmeticException e) {
        System.out.println("Error: división por cero");
    } catch (IllegalArgumentException e) {
        System.out.println("Error: " + e.getMessage());
    }
    
    return resultado;
}

// Ejemplos:
int[] arr = {10, 20, 30};
int r1 = dividirSeguro(arr, 1, 2);    // Retorna 10
int r2 = dividirSeguro(arr, 5, 2);    // Error índice, retorna 0
int r3 = dividirSeguro(arr, 1, 0);    // Error división, retorna 0
int r4 = dividirSeguro(null, 0, 1);   // Error null, retorna 0
```
````

## Referencias Bibliográficas

- **Schildt, H.** (2022). _Java: A Beginner's Guide_ (9na ed.). McGraw Hill. (Capítulo 9: Exception Handling).
- **Liang, Y. D.** (2017). _Introduction to Java Programming and Data Structures_ (11va ed.). Pearson.
- **Bloch, J.** (2018). _Effective Java_ (3ra ed.). Addison-Wesley Professional. (Capítulo 10: Exceptions).
- **Martin, R. C.** (2009). _Clean Code_. Prentice Hall. (Capítulo 7: Error Handling).

:::seealso
- {ref}`regla-0x3001` - Manejo de excepciones checked.
- {ref}`regla-0x3002` - Manejo de excepciones unchecked.
:::
