# Serie 0x3 - Manejo de Excepciones

(regla-0x3000)=
## `0x3000` - No atajar la excepción si no es posible tomar una decisión

### Explicación

Si solo vas a loggear o relanzar sin agregar valor, dejá que la excepción se propague naturalmente.

**Incorrecto** ❌:
```java
try {
    procesarArchivo();
} catch (IOException e) {
    // Solo loggear y relanzar
    logger.error("Error: " + e.getMessage());
    throw e;  // ❌ No agrega valor
}
```

**Correcto** ✅:
```java
// ✅ Dejar propagar - agregar throws en firma
public void procesar() throws IOException {
    procesarArchivo();  // Propaga naturalmente
}
```

(regla-0x3001)=
## `0x3001` - El main de un programa no debe dejar pasar excepciones checked

### Explicación

El `main` debe manejar todas las excepciones checked y proporcionar mensajes de error apropiados al usuario final.

**Incorrecto** ❌:
```java
public static void main(String[] args) throws Exception {  // ❌
    // código
}
```

**Correcto** ✅:
```java
public static void main(String[] args) {
    try {
        ejecutarPrograma();
    } catch (IOException e) {
        System.err.println("Error de archivo: " + e.getMessage());
        System.exit(1);
    }
}
```

(regla-0x3002)=
## `0x3002` - Qué familia de excepciones se eligió debe estar documentada

### Explicación

Documentar en el paquete o clase base por qué se usa checked vs unchecked para las excepciones del dominio.

```java
/**
 * Excepciones del dominio de Facturación.
 * <p>
 * Se usan excepciones UNCHECKED porque:
 * - Los errores son de programación (precondiciones violadas)
 * - No se espera recuperación en tiempo de ejecución
 */
package ar.unrn.facturacion.excepciones;
```

(regla-0x3003)=
## `0x3003` - No atajar una excepción lanzada en el mismo bloque

### Explicación

Si lanzás una excepción dentro de un try y la atajás en el mismo catch, usá `if-else` en su lugar.

**Incorrecto** ❌:
```java
try {
    if (invalido) {
        throw new IllegalArgumentException();
    }
} catch (IllegalArgumentException e) {
    // manejar
}
```

**Correcto** ✅:
```java
if (invalido) {
    // manejar directamente
} else {
    // flujo normal
}
```

(regla-0x3004)=
## `0x3004` - No convertir excepciones checked a unchecked sin justificación

### Explicación

No atajar una excepción checked (IOException, SQLException) y relanzar una excepción unchecked genérica perdiendo información del tipo original.

**Incorrecto** ❌:
```java
try {
    leerArchivo();
} catch (IOException e) {
    throw new RuntimeException("Error");  // ❌ Pérdida de información
}
```

**Correcto** ✅:
```java
try {
    leerArchivo();
} catch (IOException e) {
    throw new ArchivoNoDisponibleException("No se pudo leer: " + archivo, e);
}
```

(regla-0x3005)=
## `0x3005` - Sean específicos con lo que atajan, no está permitido atajar `Exception` o `RuntimeException`

### Explicación

Atajar excepciones específicas, no genéricas. Esto permite manejar cada caso apropiadamente.

**Incorrecto** ❌:
```java
try {
    // código
} catch (Exception e) {  // ❌ Demasiado genérico
    // manejar
}
```

**Correcto** ✅:
```java
try {
    // código
} catch (IOException e) {
    // manejar IO
} catch (SQLException e) {
    // manejar BD
}
```

(regla-0x3006)=
## `0x3006` - Situaciones diferentes requieren excepciones diferentes

### Explicación

Situaciones como "arreglo vacío" y "arreglo null" son casos diferentes que ameritan mensajes y tipos de excepciones distintos.

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

**Ejemplo reutilizable** (TP3 - Arreglos):
```java
/**
 * Verifica que un arreglo no sea null ni esté vacío.
 * @param arreglo el arreglo a verificar
 * @throws NullPointerException si el arreglo es null
 * @throws IllegalArgumentException si el arreglo está vacío
 */
private static void validarArreglo(int[] arreglo) {
    if (arreglo == null) {
        throw new NullPointerException("El arreglo no puede ser null");
    }
    if (arreglo.length == 0) {
        throw new IllegalArgumentException("El arreglo no puede estar vacío");
    }
}
```

(regla-0x3007)=
## `0x3007` - 'Largo cero' y `null` son dos situaciones bastante diferentes

### Explicación

Que requieren de excepciones distintas para que su tratamiento pueda ser más específico. Ver {ref}`regla-0x3006`.

(regla-0x3008)=
## `0x3008` - Declarar el lanzamiento de una excepción no controlada es un error

### Explicación

No es correcto (ni necesario) declarar `throws` para RuntimeException y sus subclases.

**Incorrecto** ❌:
```java
public void metodo() throws RuntimeException {  // ❌ Innecesario
    // código
}
```

**Correcto** ✅:
```java
public void metodo() {  // ✅ RuntimeException no se declara
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

(regla-0x300A)=
## `0x300A` - Mejor prevenir que atajar

### Explicación

Siempre que sea posible, prevenir la excepción en lugar de esperar a que falle (LBYL - Look Before You Leap).

**Menos óptimo** ⚠️:
```java
try {
    int resultado = dividir(a, b);
} catch (ArithmeticException e) {
    // manejar división por cero
}
```

**Mejor** ✅:
```java
if (b != 0) {
    int resultado = dividir(a, b);
} else {
    // manejar caso especial
}
```

(regla-0x300B)=
## `0x300B` - Silenciar una excepción no es la forma de gestionarla

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

(regla-0x300D)=
## `0x300D` - Atajar para hacer algún tipo de `print` no es gestionar la excepción

### Explicación

Imprimir el stack trace no es manejar la excepción. Usar logging apropiado y tomar decisión sobre cómo continuar.

**Incorrecto** ❌:
```java
try {
    operacion();
} catch (Exception e) {
    e.printStackTrace();  // ❌ Solo imprime, no maneja
}
```

**Correcto** ✅:
```java
try {
    operacion();
} catch (OperacionException e) {
    logger.error("Error en operación", e);
    // Reintentar, usar valor por defecto, o relanzar
}
```
