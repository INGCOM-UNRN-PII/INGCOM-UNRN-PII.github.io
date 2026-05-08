---
title: 0xE - Errores Comunes
---

# Serie 0xE - Errores Comunes (Error-Prone)

(regla-0xE000)=
## `0xE000` - Precaución con la concatenación de `String` en lazos

### Explicación

Concatenar cadenas dentro de un lazo de múltiples iteraciones usando el operador `+` suele ser ineficiente porque cada operación crea un nuevo objeto `String` inmutable. Para procesamientos de alto volumen, es preferible usar `StringBuilder`. No obstante, la concatenación clásica con `+` es perfectamente válida y legible fuera de lazos o en combinaciones simples de pocas variables.

### Justificación

1. **Performance e Inmutabilidad**: Cada concatenación con `+` crea un nuevo `String`. En lazos grandes, esto genera exceso de objetos temporales.
2. **Uso de `StringBuilder`**: Muta internamente un buffer de caracteres sin crear objetos descartables.

### Comparación ilustrativa

```java
// Benchmarks ilustrativos (los tiempos reales varían según el hardware)
// Concatenación en un lazo de 10.000 iteraciones puede tomar ~500ms
String resultado = "";
for (int i = 0; i < 10000; i++) {
    resultado += i; 
}

// Usando StringBuilder, la misma operación toma ~2ms
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append(i);
}
String resultadoOptimo = sb.toString();
```

(regla-0xE001)=
## `0xE001` - Diferenciar el uso de `==` y `equals()` para objetos

### Explicación

El operador `==` compara **referencias de memoria** (identidad), mientras que el método `equals()` compara el **contenido lógico** (igualdad). Salvo excepciones particulares, los objetos (especialmente `String`) deben compararse usando `equals()`.

### Cuándo usar qué

- **Usar `equals()`**: Para comparar si el contenido de dos objetos, como cadenas de texto, es el mismo.
- **Usar `==`**: Únicamente cuando trabajamos con primitivos (`int`, `boolean`, etc.), comparamos contra `null`, o si necesitamos garantizar intencionalmente que dos referencias apuntan exactamente al mismo objeto en memoria.

**Incorrecto** ❌:
```java
String nombre = new String("Juan");
if (nombre == "Juan") { // Compara memoria, no valor
    // ...
}
```

**Correcto** ✅:
```java
String nombre = new String("Juan");
if ("Juan".equals(nombre)) { // Compara contenido, protege contra null
    // ...
}
```

(regla-0xE002)=
## `0xE002` - Fugas de recursos por no cerrar streams

### Explicación

Los recursos del sistema (archivos, conexiones de red) deben cerrarse explícitamente para evitar fugas de memoria o bloqueos. Java proporciona la estructura **try-with-resources** para automatizar esto.

**Correcto** ✅:
```java
public String leerArchivo(String ruta) throws IOException {
    try (BufferedReader buffer = new BufferedReader(new FileReader(ruta))) {
        return buffer.readLine();
    }
}
```

(regla-0xE003)=
## `0xE003` - Modificación de colecciones durante la iteración

### Explicación

Modificar una colección (agregar o eliminar elementos) con sus métodos normales mientras se itera sobre ella con un `for-each` lanza `ConcurrentModificationException`. 

### Soluciones comunes

1. Usar explícitamente un `Iterator` y llamar a `it.remove()`.
2. Crear una lista paralela con los elementos a eliminar, y llamar a `listaOriginal.removeAll(listaARemover)` fuera del lazo.

(regla-0xE004)=
## `0xE004` - Ignorar el valor de retorno de métodos inmutables

### Explicación

Las clases inmutables (como `String`, `BigDecimal`, `LocalDate`) no modifican el objeto original. Sus métodos retornan **un nuevo objeto** con la modificación aplicada. Si ignorás el valor de retorno, el método no tiene ningún efecto.

**Correcto** ✅:
```java
String texto = "hola";
texto = texto.toUpperCase(); // Es necesario asignar el retorno
```

(regla-0xE005)=
## `0xE005` - Usar punto flotante para cálculos monetarios o exactos

### Explicación

Los tipos `float` y `double` (punto flotante binario) causan pequeños errores de redondeo acumulativos. Si la precisión es crítica (ej: dinero), es necesario usar herramientas que garanticen precisión decimal exacta, como `BigDecimal`, o representar la moneda en su subunidad entera (centavos).

(regla-0xE006)=
## `0xE006` - Condicionales basados en strings para simular comportamiento polimórfico

### Explicación

Un error de diseño frecuente es utilizar secuencias condicionales largas para despachar lógica basándose en el valor de una cadena. Esto viola el principio Abierto/Cerrado. Este escenario suele ser un síntoma de que el sistema necesita **polimorfismo**.

**Observación situada (TP7 Calculadora)**: 
Es común intentar identificar el tipo de operación matemática preguntando si el símbolo es `"+"` o `"-"` dentro del método que ejecuta la operación. Aunque las comprobaciones por string son aceptables en la etapa de instanciación (en un Patrón Factory) o para procesar comandos simples en la UI, el comportamiento de las entidades de negocio debe resolverse por su tipo real (ej. clases `Suma` y `Resta` implementando una interfaz `Operacion`), no por `ifs` sobre strings.

(regla-0xE007)=
## `0xE007` - Separadores de línea hardcodeados

### Explicación

No asumas que el salto de línea siempre es `\n` (Unix) o `\r\n` (Windows). Hardcodear estos valores puede producir errores sutiles al procesar o escribir archivos de texto en plataformas distintas a la tuya.

**Observación situada (TP7 Calculadora y parseo de archivos)**:
Si leés un archivo línea por línea usando los métodos de la API estándar como `BufferedReader.readLine()` o expresiones regulares multiplataforma (`String.split("\\R")`), el lenguaje abstrae el salto de línea. Evitá analizar archivos carácter por carácter buscando símbolos `\n`. Si debés escribir un archivo, utilizá herramientas que manejen automáticamente el salto de la plataforma destino, como `System.lineSeparator()`.
