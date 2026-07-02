---
title: 0x3 - Manejo de Excepciones
---

# Serie 0x3 - Manejo de Excepciones

(regla-0x3000)=
## `0x3000` - Validación previa (Look Before You Leap) antes que excepciones

### Explicación

Siempre que sea posible, validá las condiciones antes de realizar una operación (LBYL) en lugar de intentar la operación y atajar la excepción. Si una excepción se lanza y se ataja dentro del mismo bloque o método que podría haberla prevenido, debés usar un `if-else`.

**Incorrecto** ❌:
```java
try {
    if (invalido) {
        throw new IllegalArgumentException();
    }
    int resultado = dividir(a, b);
} catch (IllegalArgumentException e) {
    // manejar
} catch (ArithmeticException e) {
    // manejar
}
```

**Correcto** ✅:
```java
if (invalido) {
    // manejar directamente
} else if (b != 0) {
    int resultado = dividir(a, b);
} else {
    // manejar división por cero
}
```

(regla-0x3001)=
## `0x3001` - Propagación natural: No atajar si no podés tomar una decisión útil

### Explicación

Si solo vas a logguear el error, silenciarlo con un catch vacío, imprimir el stack trace o envolver y relanzar la excepción sin agregar contexto, dejá que la excepción se propague naturalmente hacia arriba en la pila de llamadas. 

**Incorrecto** ❌ (Silenciar, print, relanzar sin valor):
```java
try {
    procesarArchivo();
} catch (IOException e) {
    // ❌ Solo logguear y relanzar
    logger.error("Error: " + e.getMessage());
    throw e;  
}

try {
    operacion();
} catch (Exception e) {
    e.printStackTrace();  // ❌ Solo imprime
}
```

**Correcto** ✅ (Propagar o manejar con decisión):
```java
// ✅ Dejar propagar - agregar throws en firma
public void procesar() throws IOException {
    procesarArchivo();  // Propaga naturalmente
}

// ✅ Atajar tomando una decisión
try {
    operacionRiesgosa();
} catch (IOException e) {
    logger.error("Error leyendo, usando valor por defecto", e);
    // Y tomar decisión (ej. usar configuración default)
}
```

(regla-0x3002)=
## `0x3002` - Traducción con contexto

### Explicación

Cuando necesites atrapar una excepción para relanzarla, debe ser para agregar contexto útil al error o para traducirla a una excepción del dominio de tu aplicación. Nunca conviertas excepciones checked a unchecked genéricas sin justificación ni pérdida de la causa original.

**Incorrecto** ❌:
```java
try {
    leerArchivo();
} catch (IOException e) {
    throw new RuntimeException("Error");  // ❌ Pérdida del error original (cause) e información
}

try {
    conectar();
} catch (SQLException e) {
    throw new Exception("Falló la conexión");  // ❌ Pierde tipo y causa (ver 0x300C)
}
```

**Correcto** ✅:

```java
try {
    leerArchivo();
} catch (IOException e) {
    // Convierte, y ademas, no pierde contexto.
    throw new ArchivoNoDisponibleException("No se pudo leer la configuración de: " + archivo, e);
}
```

(regla-0x3003)=
## `0x3003` - Captura terminal en el main (Convención del Curso)

### Explicación

Como convención en nuestros proyectos, el método `main` (o el punto de entrada superior de la aplicación) actúa como captura terminal. No debe dejar pasar excepciones (checked o unchecked) hacia la máquina virtual. Debe proveer un mensaje de error limpio al usuario.

**Incorrecto** ❌:
```java
public static void main(String[] args) throws Exception {  // ❌
    ejecutarPrograma();
}
```

**Correcto** ✅:
```java
public static void main(String[] args) {
    try {
        ejecutarPrograma();
    } catch (Exception e) {
        System.err.println("Error fatal de la aplicación: " + e.getMessage());
        System.exit(1);
    }
}
```

(regla-0x3004)=
## `0x3004` - Lanzar y atajar excepciones específicas, no las clases base

### Explicación

No está permitido lanzar o atajar las clases genéricas `Exception` o `RuntimeException`. Usá siempre clases de excepciones específicas (estándar de Java o del dominio) para poder dar a cada situación un manejo diferenciado y correcto.

**Incorrecto** ❌:
```java
try {
    if (error) throw new RuntimeException("error"); // ❌ Lanza base
} catch (Exception e) {  // ❌ Ataja base
    // manejar todo igual
}
```

**Correcto** ✅:
```java
try {
    if (error) throw new IllegalArgumentException("parámetro inválido"); // ✅ Lanza específica
} catch (IOException e) {
    // manejar IO
} catch (IllegalArgumentException e) {
    // manejar parámetros
}
```

(regla-0x3005)=
## `0x3005` - Distinguir 'null' de 'vacío' al validar parámetros (Convención del Curso)

### Explicación

Situaciones como "arreglo vacío" y "arreglo null" (o strings vacíos vs null) representan fallos diferentes y deben recibir excepciones diferentes. Como convención del curso, usaremos `NullPointerException` explícitamente para el caso de que la referencia sea null, y `IllegalArgumentException` u otra específica para colecciones vacías.

**Incorrecto** ❌:
```java
if (arreglo == null || arreglo.length == 0) {
    throw new IllegalArgumentException("Arreglo inválido");
}
```

**Correcto** ✅:
```java
if (arreglo == null) {
    throw new NullPointerException("El arreglo no puede ser null");
}
if (arreglo.length == 0) {
    throw new IllegalArgumentException("El arreglo no puede estar vacío");
}
```

(regla-0x3006)=
## `0x3006` - Documentación y declaración correcta de Excepciones

### Explicación

No debés declarar (`throws`) excepciones no controladas (`RuntimeException` y derivadas) en la firma del método, ya que es redundante y confuso. Sin embargo, sí debés documentar qué familia de excepciones elegís usar en el Javadoc de la clase o paquete.

**Incorrecto** ❌:
```java
public void metodo() throws RuntimeException {  // ❌ Innecesario
    // código
}
```

**Correcto** ✅:
```java
/**
 * Realiza una operación.
 * @throws IllegalArgumentException si los argumentos son inválidos
 */
public void metodo() {  // RuntimeException no se declara
    // código
}
```

(regla-0x3009)=
## `0x3009` - No está permitido lanzar excepciones base: `Exception` o `RuntimeException`

### Explicación

Lanzar excepciones específicas del dominio o estándar de Java, no las clases base.

**Incorrecto** ❌:
```java
throw new Exception("error");
throw new RuntimeException("error");
```

**Correcto** ✅:
```java
throw new MiExcepcionEspecifica("error");
throw new IllegalArgumentException("parámetro inválido");
```

(regla-0x300B)=
## `0x300B` - Silenciar una excepción no es la forma de gestionarla

Esto incluye atajar para hacer únicamente algún tipo de `print`.
### Explicación

No dejar bloques catch vacíos. Como mínimo, loggear el error.

**Incorrecto** ❌:
```java
try {
    operacionRiesgosa();
} catch (Exception e) {
    // ❌ Bloque vacío - se silencia el error
}
```

**Correcto** ✅:
```java
try {
    operacionRiesgosa();
} catch (Exception e) {
    logger.error("Error en operación riesgosa", e);
    // Y tomar decisión: reintentar, valor por defecto, etc.
}
```


(regla-0x300C)=
## `0x300C` - No está permitido atajar para relanzar sin agregar información útil

### Explicación

Si solo envolvés la excepción sin agregar contexto, dejá que se propague.

**Incorrecto** ❌:
```java
try {
    leerArchivo();
} catch (IOException e) {
    throw new IOException(e);  // ❌ Solo envuelve, no agrega valor
}
```

**Correcto** ✅:
```java
try {
    leerArchivo();
} catch (IOException e) {
    throw new ArchivoConfiguracionException(
        "No se pudo leer configuración de: " + archivo, e);
}
```
