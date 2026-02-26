---
title: "Excepciones en Java"
description: Guía completa sobre el manejo de errores y excepciones en Java.
---

# Excepciones en Java

Las **excepciones** son eventos que interrumpen el flujo normal de ejecución de un programa. Java proporciona un robusto sistema de manejo de excepciones que permite detectar, reportar y manejar errores de manera estructurada.

## Concepto de Excepción

Una excepción representa una condición anormal que ocurre durante la ejecución:

- **División por cero** → `ArithmeticException`
- **Acceso a índice inválido** → `ArrayIndexOutOfBoundsException`
- **Referencia nula** → `NullPointerException`
- **Archivo no encontrado** → `FileNotFoundException`

```{code} java
:caption: Una excepción en acción

public void ejemplo() {
    int[] numeros = {1, 2, 3};
    System.out.println(numeros[5]);  // ¡ArrayIndexOutOfBoundsException!
    System.out.println("Esta línea nunca se ejecuta");
}
```

## Jerarquía de Excepciones

```
                    Throwable
                    /       \
                Error      Exception
                            /      \
              IOException    RuntimeException
              SQLException   NullPointerException
              ...            ArithmeticException
                             ...
```

### Tipos de Throwables

:::{table} Tipos de errores y excepciones
:label: tbl-tipos-excepciones

| Tipo | Descripción | Manejo | Ejemplo |
| :--- | :---------- | :----- | :------ |
| `Error` | Problemas graves del sistema | No se manejan | `OutOfMemoryError` |
| `Exception` (checked) | Excepciones recuperables | Obligatorio | `IOException` |
| `RuntimeException` | Errores de programación | Opcional | `NullPointerException` |

:::

## Estrategias de Manejo

Existen tres formas principales de lidiar con excepciones:

### 1. Atajar (catch)

Capturar y manejar la excepción localmente:

```{code} java
:caption: Atajando una excepción

try {
    int resultado = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("No se puede dividir por cero");
}
System.out.println("El programa continúa...");
```

### 2. Delegar (throws)

Propagar la excepción al código que llamó al método:

```{code} java
:caption: Delegando una excepción

public void leerArchivo(String ruta) throws IOException {
    FileReader reader = new FileReader(ruta);  // Puede lanzar IOException
    // Si ocurre IOException, se propaga al llamador
}
```

### 3. Prevenir

Evitar que la excepción ocurra mediante validación:

```{code} java
:caption: Previniendo una excepción

public int dividir(int a, int b) {
    if (b == 0) {
        return 0;  // O lanzar una excepción controlada
    }
    return a / b;
}
```

## Bloque try-catch

### Estructura Básica

```{code} java
:caption: Estructura try-catch básica

try {
    // Código que puede lanzar excepciones
    String texto = null;
    int longitud = texto.length();  // NullPointerException
} catch (NullPointerException e) {
    // Código para manejar la excepción
    System.out.println("La cadena era nula");
}
```

### Múltiples catch

Se pueden capturar diferentes tipos de excepciones:

```{code} java
:caption: Múltiples bloques catch

try {
    int[] numeros = {1, 2, 3};
    int valor = numeros[Integer.parseInt("abc")];
} catch (NumberFormatException e) {
    System.out.println("Formato de número inválido");
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Índice fuera de rango");
}
```

:::{important}
El orden de los `catch` importa: las excepciones más específicas deben ir primero, seguidas de las más generales. De lo contrario, el compilador generará un error.

```java
// ✗ ERROR: IOException más específica después de Exception
catch (Exception e) { }
catch (IOException e) { }  // Inalcanzable

// ✓ CORRECTO
catch (IOException e) { }
catch (Exception e) { }
```
:::

### Multi-catch (Java 7+)

Capturar múltiples excepciones en un solo bloque:

```{code} java
:caption: Multi-catch

try {
    // código...
} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
    System.out.println("Error de formato o índice: " + e.getMessage());
}
```

### Bloque finally

Se ejecuta **siempre**, haya o no excepción:

```{code} java
:caption: Uso de finally

FileReader reader = null;
try {
    reader = new FileReader("archivo.txt");
    // procesar archivo...
} catch (FileNotFoundException e) {
    System.out.println("Archivo no encontrado");
} finally {
    // Se ejecuta siempre
    if (reader != null) {
        try {
            reader.close();
        } catch (IOException e) {
            // Ignorar error de cierre
        }
    }
}
```

:::{note}
`finally` se ejecuta incluso si hay un `return` dentro del `try` o `catch`. Es ideal para liberar recursos (archivos, conexiones, etc.).
:::

## Try-with-resources (Java 7+)

Una forma más elegante de manejar recursos que implementan `AutoCloseable`:

```{code} java
:caption: Try-with-resources

try (FileReader reader = new FileReader("archivo.txt");
     BufferedReader buffer = new BufferedReader(reader)) {
    
    String linea = buffer.readLine();
    System.out.println(linea);
    
} catch (IOException e) {
    System.out.println("Error de lectura: " + e.getMessage());
}
// Los recursos se cierran automáticamente
```

## Lanzamiento de Excepciones

### throw

Para lanzar una excepción explícitamente:

```{code} java
:caption: Lanzamiento con throw

public void validarEdad(int edad) {
    if (edad < 0) {
        throw new IllegalArgumentException("La edad no puede ser negativa");
    }
    if (edad > 150) {
        throw new IllegalArgumentException("La edad no es realista");
    }
}
```

### Constructores de Excepciones

```{code} java
:caption: Diferentes formas de crear excepciones

// Sin mensaje
throw new RuntimeException();

// Con mensaje descriptivo
throw new RuntimeException("Algo salió mal");

// Con causa (excepción original)
catch (SQLException e) {
    throw new RuntimeException("Error de base de datos", e);
}

// Con mensaje y causa
throw new RuntimeException("Error al procesar", excepcionOriginal);
```

## Excepciones Checked vs Unchecked

### Checked (Comprobadas)

Deben ser manejadas obligatoriamente (catch o throws):

```{code} java
:caption: Excepción checked

// Obliga a manejar o declarar IOException
public void leerArchivo() throws IOException {
    FileReader reader = new FileReader("archivo.txt");
}

// O capturarla
public void leerArchivoSeguro() {
    try {
        FileReader reader = new FileReader("archivo.txt");
    } catch (FileNotFoundException e) {
        // Manejar el error
    }
}
```

### Unchecked (No Comprobadas)

Son `RuntimeException` y sus subclases. No requieren declaración ni manejo obligatorio:

```{code} java
:caption: Excepciones unchecked

public int dividir(int a, int b) {
    // ArithmeticException es unchecked
    // No necesita throws ni try-catch
    return a / b;
}
```

:::{table} Comparación checked vs unchecked
:label: tbl-checked-unchecked

| Característica | Checked | Unchecked |
| :------------- | :------ | :-------- |
| Clase base | `Exception` | `RuntimeException` |
| Manejo obligatorio | Sí | No |
| Declaración throws | Obligatoria | Opcional |
| Típico uso | Errores recuperables externos | Errores de programación |
| Ejemplos | `IOException`, `SQLException` | `NullPointerException`, `IllegalArgumentException` |

:::

## Creación de Excepciones Propias

### Excepción Checked

```{code} java
:caption: Excepción checked personalizada

public class SaldoInsuficienteException extends Exception {
    
    private final double saldoActual;
    private final double montoSolicitado;
    
    public SaldoInsuficienteException(double saldo, double monto) {
        super(String.format(
            "Saldo insuficiente: disponible %.2f, solicitado %.2f",
            saldo, monto
        ));
        this.saldoActual = saldo;
        this.montoSolicitado = monto;
    }
    
    public double getSaldoActual() {
        return saldoActual;
    }
    
    public double getMontoSolicitado() {
        return montoSolicitado;
    }
}
```

### Excepción Unchecked

```{code} java
:caption: Excepción unchecked personalizada

public class ValidacionException extends RuntimeException {
    
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
    
    public ValidacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
```

### Uso de Excepciones Propias

```{code} java
:caption: Uso de excepción personalizada

public class CuentaBancaria {
    private double saldo;
    
    public void retirar(double monto) throws SaldoInsuficienteException {
        if (monto > saldo) {
            throw new SaldoInsuficienteException(saldo, monto);
        }
        saldo -= monto;
    }
}

// Uso
try {
    cuenta.retirar(1000);
} catch (SaldoInsuficienteException e) {
    System.out.printf("No se puede retirar. Saldo: %.2f%n", 
                      e.getSaldoActual());
}
```

## Buenas Prácticas

### 1. Capturar Excepciones Específicas

```java
// ✗ Muy general
catch (Exception e) { }

// ✓ Específico
catch (FileNotFoundException e) { }
```

### 2. No Suprimir Excepciones

```java
// ✗ Mal: ignora el error
catch (Exception e) { }

// ✓ Al menos registrar
catch (Exception e) {
    System.err.println("Error: " + e.getMessage());
    e.printStackTrace();
}
```

### 3. Documentar Excepciones

```{code} java
:caption: Documentación de excepciones

/**
 * Divide dos números enteros.
 *
 * @param dividendo el número a dividir
 * @param divisor el divisor
 * @return el cociente de la división
 * @throws IllegalArgumentException si el divisor es cero
 */
public int dividir(int dividendo, int divisor) {
    if (divisor == 0) {
        throw new IllegalArgumentException("El divisor no puede ser cero");
    }
    return dividendo / divisor;
}
```

### 4. Proporcionar Contexto

```java
// ✗ Poco informativo
throw new RuntimeException("Error");

// ✓ Con contexto útil
throw new RuntimeException(
    String.format("Error al procesar usuario ID=%d: %s", userId, detalle)
);
```

### 5. Preservar la Causa Original

```java
catch (SQLException e) {
    // ✗ Pierde información de la causa
    throw new RuntimeException("Error de BD");
    
    // ✓ Preserva la causa original
    throw new RuntimeException("Error de BD", e);
}
```

## Ejercicios

```{exercise}
:label: ej-exc-1

Creá una excepción `EdadInvalidaException` que se lance cuando una edad esté fuera del rango 0-150. Implementá un método `validarPersona(String nombre, int edad)` que la utilice.
```

```{solution} ej-exc-1
```java
public class EdadInvalidaException extends Exception {
    private final int edad;
    
    public EdadInvalidaException(int edad) {
        super("Edad inválida: " + edad + ". Debe estar entre 0 y 150.");
        this.edad = edad;
    }
    
    public int getEdad() {
        return edad;
    }
}

public void validarPersona(String nombre, int edad) 
        throws EdadInvalidaException {
    if (nombre == null || nombre.isBlank()) {
        throw new IllegalArgumentException("El nombre no puede estar vacío");
    }
    if (edad < 0 || edad > 150) {
        throw new EdadInvalidaException(edad);
    }
}
```
```

```{exercise}
:label: ej-exc-2

Implementá un método `int[] parsearNumeros(String[] textos)` que convierta un arreglo de strings a enteros. Si algún string no es válido, debe capturar la excepción y usar 0 como valor por defecto, pero además debe registrar qué índices fallaron.
```

```{solution} ej-exc-2
```java
public int[] parsearNumeros(String[] textos) {
    int[] resultado = new int[textos.length];
    List<Integer> indicesFallidos = new ArrayList<>();
    
    for (int i = 0; i < textos.length; i++) {
        try {
            resultado[i] = Integer.parseInt(textos[i].trim());
        } catch (NumberFormatException e) {
            resultado[i] = 0;
            indicesFallidos.add(i);
        }
    }
    
    if (!indicesFallidos.isEmpty()) {
        System.out.println("Advertencia: valores inválidos en índices " + 
                          indicesFallidos + " (se usó 0)");
    }
    
    return resultado;
}
```
```

```{exercise}
:label: ej-exc-3

Escribí un método que lea un archivo de configuración clave=valor y retorne un `Map<String, String>`. Debe manejar `FileNotFoundException` e `IOException` de forma diferente, y propagar una excepción propia `ConfiguracionException` con un mensaje claro.
```

```{solution} ej-exc-3
```java
public class ConfiguracionException extends Exception {
    public ConfiguracionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

public Map<String, String> leerConfiguracion(String ruta) 
        throws ConfiguracionException {
    
    Map<String, String> config = new HashMap<>();
    
    try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
        String linea;
        int numeroLinea = 0;
        
        while ((linea = reader.readLine()) != null) {
            numeroLinea++;
            linea = linea.trim();
            
            if (linea.isEmpty() || linea.startsWith("#")) {
                continue;  // Ignorar líneas vacías y comentarios
            }
            
            String[] partes = linea.split("=", 2);
            if (partes.length == 2) {
                config.put(partes[0].trim(), partes[1].trim());
            }
        }
        
    } catch (FileNotFoundException e) {
        throw new ConfiguracionException(
            "Archivo de configuración no encontrado: " + ruta, e);
    } catch (IOException e) {
        throw new ConfiguracionException(
            "Error al leer archivo de configuración: " + ruta, e);
    }
    
    return config;
}
```
```

:::{seealso}
- [Tutorial oficial de excepciones](https://docs.oracle.com/javase/tutorial/essential/exceptions/)
- [Documentación de Throwable](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Throwable.html)
:::
