---
title: "Manejo de Archivos en Java"
description: Estudio técnico sobre flujos de datos, NIO.2, manejo de excepciones y persistencia en sistemas de archivos.
---

# Manejo de Archivos en Java

El manejo de Entrada/Salida (I/O) en Java ha evolucionado desde un modelo basado en flujos de datos (_streams_) en `java.io` hacia una API más moderna y eficiente denominada NIO.2 (`java.nio.file`). Para un ingeniero, entender esta transición es clave para diseñar sistemas que interactúen correctamente con el almacenamiento persistente.

:::{note} Comparativa con C
En C, el manejo de archivos se hace con punteros `FILE*` y funciones como `fopen()`, `fread()`, `fclose()`. Java proporciona una abstracción más segura con gestión automática de recursos y excepciones tipadas que indican exactamente qué salió mal.
:::

## Arquitectura de I/O en Java

### Bytes vs. Caracteres

Java separa estrictamente el manejo de datos binarios del texto:

| Tipo de Datos | Clases Base | Uso |
|:---|:---|:---|
| **Bytes** | `InputStream`, `OutputStream` | Datos binarios (imágenes, audio, ejecutables) |
| **Caracteres** | `Reader`, `Writer` | Texto (con codificación UTF-8, etc.) |

```{code} java
:caption: Diferencia conceptual

// Para archivos binarios (bytes)
InputStream entrada = new FileInputStream("imagen.png");

// Para archivos de texto (caracteres)
Reader lector = new FileReader("documento.txt");
```

### El Patrón Decorator

La API clásica de Java utiliza el patrón **Decorator** para añadir funcionalidades a los flujos de datos. Esto permite "envolver" flujos básicos con funcionalidades adicionales.

```{code} java
:caption: Composición de flujos (Decorator)

// FileReader: flujo de bajo nivel (accede al archivo)
// BufferedReader: agrega buffering (mejora rendimiento)
BufferedReader reader = new BufferedReader(new FileReader("datos.txt"));
```

## NIO.2: La API Moderna

Introducida en Java 7, NIO.2 (`java.nio.file`) proporciona una API más potente y segura para trabajar con archivos.

### La Clase `Path`

`Path` representa una ruta en el sistema de archivos. Es una abstracción que funciona en cualquier sistema operativo.

```{code} java
:caption: Creación de objetos Path

import java.nio.file.Path;

// Crear Path desde String
Path archivo = Path.of("datos.txt");
Path rutaAbsoluta = Path.of("/home/usuario/documentos/datos.txt");
Path rutaWindows = Path.of("C:\\Users\\usuario\\datos.txt");

// Path con múltiples componentes
Path ruta = Path.of("home", "usuario", "documentos", "datos.txt");

// Obtener información del Path
String nombre = archivo.getFileName().toString();    // "datos.txt"
Path padre = rutaAbsoluta.getParent();               // "/home/usuario/documentos"
Path raiz = rutaAbsoluta.getRoot();                  // "/"
```

### La Clase `Files`

`Files` proporciona métodos estáticos para operaciones comunes con archivos.

```{code} java
:caption: Operaciones básicas con Files

import java.nio.file.Files;
import java.nio.file.Path;

Path archivo = Path.of("datos.txt");

// Verificar existencia
boolean existe = Files.exists(archivo);
boolean esArchivo = Files.isRegularFile(archivo);
boolean esDirectorio = Files.isDirectory(archivo);

// Verificar permisos
boolean lectura = Files.isReadable(archivo);
boolean escritura = Files.isWritable(archivo);

// Obtener tamaño
long tamanio = Files.size(archivo);  // en bytes
```

## Lectura de Archivos de Texto

### Método Simple: `Files.readAllLines()`

Para archivos pequeños, podés leer todas las líneas de una vez:

```{code} java
:caption: Leer todas las líneas

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.io.IOException;

public static void leerArchivoCompleto(String rutaArchivo) {
    Path archivo = Path.of(rutaArchivo);
    
    try {
        List<String> lineas = Files.readAllLines(archivo);
        
        for (String linea : lineas) {
            System.out.println(linea);
        }
    } catch (IOException e) {
        System.out.println("Error al leer archivo: " + e.getMessage());
    }
}
```

:::{warning} Cuidado con Archivos Grandes
`readAllLines()` carga **todo** el archivo en memoria. Para archivos muy grandes, esto puede causar `OutOfMemoryError`. Usá lectura línea por línea para archivos grandes.
:::

### Lectura Línea por Línea con BufferedReader

Para archivos grandes, leé línea por línea:

```{code} java
:caption: Lectura línea por línea

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public static void leerLineaPorLinea(String rutaArchivo) {
    Path archivo = Path.of(rutaArchivo);
    
    try (BufferedReader reader = Files.newBufferedReader(archivo)) {
        String linea;
        int numeroLinea = 1;
        
        while ((linea = reader.readLine()) != null) {
            System.out.println(numeroLinea + ": " + linea);
            numeroLinea = numeroLinea + 1;
        }
    } catch (IOException e) {
        System.out.println("Error al leer: " + e.getMessage());
    }
}
```

### Lectura de Todo el Contenido como String

```{code} java
:caption: Leer todo como String

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public static String leerComoString(String rutaArchivo) throws IOException {
    Path archivo = Path.of(rutaArchivo);
    return Files.readString(archivo);  // Java 11+
}
```

## Escritura de Archivos de Texto

### Método Simple: `Files.write()`

```{code} java
:caption: Escribir líneas a un archivo

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Arrays;
import java.io.IOException;

public static void escribirLineas(String rutaArchivo) {
    Path archivo = Path.of(rutaArchivo);
    List<String> lineas = Arrays.asList(
        "Primera línea",
        "Segunda línea",
        "Tercera línea"
    );
    
    try {
        Files.write(archivo, lineas);
        System.out.println("Archivo escrito correctamente");
    } catch (IOException e) {
        System.out.println("Error al escribir: " + e.getMessage());
    }
}
```

### Escritura con BufferedWriter

Para mayor control o archivos grandes:

```{code} java
:caption: Escritura con BufferedWriter

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public static void escribirConBuffer(String rutaArchivo, String[] datos) {
    Path archivo = Path.of(rutaArchivo);
    
    try (BufferedWriter writer = Files.newBufferedWriter(archivo)) {
        for (String linea : datos) {
            writer.write(linea);
            writer.newLine();  // Agrega salto de línea del sistema
        }
    } catch (IOException e) {
        System.out.println("Error al escribir: " + e.getMessage());
    }
}
```

### Agregar al Final (Append)

```{code} java
:caption: Agregar contenido a archivo existente

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.util.Collections;

public static void agregarLinea(String rutaArchivo, String nuevaLinea) {
    Path archivo = Path.of(rutaArchivo);
    
    try {
        Files.write(archivo, 
                   Collections.singletonList(nuevaLinea),
                   StandardOpenOption.APPEND,
                   StandardOpenOption.CREATE);  // Crea si no existe
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
```

## Operaciones con Archivos y Directorios

### Crear, Copiar, Mover y Eliminar

```{code} java
:caption: Operaciones de archivo

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

// Crear directorio
Path nuevoDir = Path.of("nuevo_directorio");
Files.createDirectory(nuevoDir);

// Crear directorios anidados
Path directorios = Path.of("a/b/c/d");
Files.createDirectories(directorios);

// Crear archivo vacío
Path nuevoArchivo = Path.of("nuevo.txt");
Files.createFile(nuevoArchivo);

// Copiar archivo
Path origen = Path.of("original.txt");
Path destino = Path.of("copia.txt");
Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);

// Mover/Renombrar archivo
Path viejo = Path.of("viejo.txt");
Path nuevo = Path.of("nuevo_nombre.txt");
Files.move(viejo, nuevo, StandardCopyOption.REPLACE_EXISTING);

// Eliminar archivo
Path aEliminar = Path.of("temporal.txt");
Files.delete(aEliminar);             // Lanza excepción si no existe
Files.deleteIfExists(aEliminar);     // No lanza excepción si no existe
```

## Manejo de Excepciones en I/O

Las operaciones de I/O pueden fallar por múltiples razones. Java proporciona una jerarquía de excepciones que permite manejar cada caso específicamente.

### Jerarquía de Excepciones de I/O

```
IOException (excepción base)
├── FileNotFoundException        // Archivo no encontrado
├── NoSuchFileException         // Archivo no existe (NIO.2)
├── AccessDeniedException       // Sin permisos
├── FileAlreadyExistsException  // El archivo ya existe
├── DirectoryNotEmptyException  // Directorio no vacío
├── NotDirectoryException       // Se esperaba directorio
├── EOFException                // Fin de archivo inesperado
└── ... otras
```

### Captura Jerarquizada de Excepciones

Podés capturar excepciones específicas antes que las generales:

```{code} java
:caption: Captura jerarquizada de excepciones

import java.nio.file.*;
import java.io.IOException;

public static void leerArchivoSeguro(String ruta) {
    Path archivo = Path.of(ruta);
    
    try {
        List<String> lineas = Files.readAllLines(archivo);
        for (String linea : lineas) {
            System.out.println(linea);
        }
        
    } catch (NoSuchFileException e) {
        // Más específica: el archivo no existe
        System.out.println("Error: El archivo no existe: " + e.getFile());
        
    } catch (AccessDeniedException e) {
        // Específica: sin permisos de lectura
        System.out.println("Error: Sin permisos para leer: " + e.getFile());
        
    } catch (IOException e) {
        // General: cualquier otro error de I/O
        System.out.println("Error de I/O: " + e.getMessage());
    }
}
```

:::{important} Orden de los catch
Siempre colocá las excepciones más específicas **antes** que las más generales. Si ponés `IOException` primero, las específicas nunca se alcanzarán y el compilador dará error.
:::

### Excepciones Múltiples con Pipe

Si querés manejar varias excepciones de la misma manera:

```{code} java
:caption: Captura múltiple

try {
    // operaciones de archivo
} catch (NoSuchFileException | AccessDeniedException e) {
    System.out.println("Problema de acceso al archivo: " + e.getMessage());
} catch (IOException e) {
    System.out.println("Error de I/O general: " + e.getMessage());
}
```

## Gestión de Recursos: try-finally vs try-with-resources

Los recursos de I/O (archivos, conexiones, streams) **deben cerrarse** cuando ya no se usan. Si no se cierran, se produce una "fuga de recursos" que puede agotar los descriptores de archivo del sistema.

### El Problema: Cerrar Recursos Correctamente

```{code} java
:caption: ❌ Incorrecto: sin cerrar el recurso

BufferedReader reader = new BufferedReader(new FileReader("datos.txt"));
String linea = reader.readLine();
// Si hay excepción aquí, el archivo queda abierto
System.out.println(linea);
// reader.close() nunca se ejecuta si hay error
```

### Solución Clásica: try-finally

El bloque `finally` garantiza que el código de cierre se ejecute siempre:

```{code} java
:caption: try-finally para cerrar recursos

import java.io.*;

public static void leerConFinally(String ruta) {
    BufferedReader reader = null;
    
    try {
        reader = new BufferedReader(new FileReader(ruta));
        String linea;
        while ((linea = reader.readLine()) != null) {
            System.out.println(linea);
        }
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    } finally {
        // Se ejecuta SIEMPRE, haya o no excepción
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar: " + e.getMessage());
            }
        }
    }
}
```

**Problemas del try-finally:**
- Código verboso y propenso a errores
- El close() puede lanzar excepción, requiriendo otro try-catch
- Fácil olvidar el cierre o hacerlo incorrectamente

### Solución Moderna: try-with-resources (Java 7+)

El bloque `try-with-resources` cierra automáticamente los recursos al salir:

```{code} java
:caption: try-with-resources (recomendado)

import java.io.*;
import java.nio.file.*;

public static void leerConTryResources(String ruta) {
    Path archivo = Path.of(ruta);
    
    // El recurso se declara dentro de los paréntesis del try
    try (BufferedReader reader = Files.newBufferedReader(archivo)) {
        String linea;
        while ((linea = reader.readLine()) != null) {
            System.out.println(linea);
        }
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
    // reader.close() se llama automáticamente al salir del try
}
```

### Múltiples Recursos en try-with-resources

Podés declarar varios recursos separados por punto y coma:

```{code} java
:caption: Múltiples recursos

import java.io.*;
import java.nio.file.*;

public static void copiarArchivo(String origen, String destino) {
    Path archivoOrigen = Path.of(origen);
    Path archivoDestino = Path.of(destino);
    
    try (BufferedReader reader = Files.newBufferedReader(archivoOrigen);
         BufferedWriter writer = Files.newBufferedWriter(archivoDestino)) {
        
        String linea;
        while ((linea = reader.readLine()) != null) {
            writer.write(linea);
            writer.newLine();
        }
        
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
    // Ambos se cierran automáticamente (en orden inverso)
}
```

:::{important} Orden de Cierre
Los recursos se cierran en **orden inverso** al que fueron declarados. En el ejemplo anterior, primero se cierra `writer`, luego `reader`.
:::

### Requisito: La Interfaz AutoCloseable

Para que un recurso funcione con try-with-resources, debe implementar la interfaz `AutoCloseable`. Todas las clases de I/O de Java lo implementan.

```{code} java
:caption: Cualquier AutoCloseable funciona

// Scanner implementa AutoCloseable
try (Scanner scanner = new Scanner(System.in)) {
    System.out.print("Ingrese un número: ");
    int numero = scanner.nextInt();
    System.out.println("El doble es: " + (numero * 2));
}
// scanner.close() se llama automáticamente
```

## Codificación de Caracteres (Charset)

Los archivos de texto almacenan bytes, no caracteres. La **codificación** define cómo se traducen bytes a caracteres.

### Especificar Codificación

```{code} java
:caption: Especificar codificación UTF-8

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

// Lectura con codificación específica
List<String> lineas = Files.readAllLines(
    Path.of("datos.txt"), 
    StandardCharsets.UTF_8
);

// Escritura con codificación específica
Files.write(
    Path.of("salida.txt"),
    lineas,
    StandardCharsets.UTF_8
);

// BufferedReader con codificación
try (BufferedReader reader = Files.newBufferedReader(
        Path.of("datos.txt"), 
        StandardCharsets.UTF_8)) {
    // ...
}
```

### Codificaciones Comunes

| Codificación | Descripción | Uso |
|:---|:---|:---|
| `UTF_8` | Unicode variable (1-4 bytes) | Estándar moderno, recomendado |
| `ISO_8859_1` | Latin-1 (1 byte) | Archivos antiguos en español |
| `US_ASCII` | ASCII (7 bits) | Solo caracteres ingleses básicos |
| `UTF_16` | Unicode fijo (2 bytes) | Menos común |

:::{tip} Usá UTF-8
Siempre usá UTF-8 a menos que tengas una razón específica para otra codificación. Es el estándar moderno y soporta todos los caracteres de todos los idiomas.
:::

## Eficiencia: Buffering

Las operaciones de I/O son lentas porque requieren acceso al disco. El **buffering** reduce este costo leyendo/escribiendo bloques grandes en lugar de bytes individuales.

```{code} java
:caption: Impacto del buffering

// ❌ Sin buffer: una llamada al sistema por cada byte
InputStream sinBuffer = new FileInputStream("datos.bin");

// ✅ Con buffer: lee bloques de ~8KB, sirve desde memoria
InputStream conBuffer = new BufferedInputStream(
    new FileInputStream("datos.bin")
);

// NIO.2: Files.newBufferedReader ya incluye buffering
BufferedReader reader = Files.newBufferedReader(Path.of("datos.txt"));
```

## Ejemplo Completo: Procesar Archivo CSV

```{code} java
:caption: Leer y procesar archivo CSV

import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProcesadorCSV {
    
    public static List<String[]> leerCSV(String rutaArchivo) {
        List<String[]> datos = new ArrayList<>();
        Path archivo = Path.of(rutaArchivo);
        
        try (BufferedReader reader = Files.newBufferedReader(archivo)) {
            String linea;
            
            // Saltar encabezado
            reader.readLine();
            
            while ((linea = reader.readLine()) != null) {
                String[] campos = linea.split(",");
                datos.add(campos);
            }
            
        } catch (NoSuchFileException e) {
            System.out.println("Error: Archivo no encontrado: " + rutaArchivo);
        } catch (IOException e) {
            System.out.println("Error al leer CSV: " + e.getMessage());
        }
        
        return datos;
    }
    
    public static void escribirCSV(String rutaArchivo, 
                                   String[] encabezados, 
                                   List<String[]> datos) {
        Path archivo = Path.of(rutaArchivo);
        
        try (BufferedWriter writer = Files.newBufferedWriter(archivo)) {
            // Escribir encabezados
            writer.write(String.join(",", encabezados));
            writer.newLine();
            
            // Escribir datos
            for (String[] fila : datos) {
                writer.write(String.join(",", fila));
                writer.newLine();
            }
            
            System.out.println("CSV escrito: " + datos.size() + " filas");
            
        } catch (IOException e) {
            System.out.println("Error al escribir CSV: " + e.getMessage());
        }
    }
}
```

## Resumen: Mejores Prácticas

1. **Usá NIO.2** (`java.nio.file`) en lugar de la API antigua (`java.io.File`).
2. **Siempre usá try-with-resources** para garantizar el cierre de recursos.
3. **Capturá excepciones específicas** antes que las generales.
4. **Especificá la codificación** (UTF-8) explícitamente.
5. **Usá BufferedReader/Writer** para mejor rendimiento.
6. **Verificá existencia** del archivo antes de operar si es necesario.
7. **No cargues archivos enormes** en memoria completa.

## Ejercicios de Aplicación

```exercise
:label: ej-contar-lineas
Escribí un método `contarLineas(String ruta)` que cuente y retorne la cantidad de líneas de un archivo de texto, manejando correctamente las excepciones y cerrando el recurso.
```

````solution
:for: ej-contar-lineas
```java
import java.nio.file.*;
import java.io.*;

public static int contarLineas(String ruta) {
    Path archivo = Path.of(ruta);
    int contador = 0;
    
    try (BufferedReader reader = Files.newBufferedReader(archivo)) {
        while (reader.readLine() != null) {
            contador = contador + 1;
        }
    } catch (NoSuchFileException e) {
        System.out.println("Error: El archivo no existe: " + ruta);
        return -1;
    } catch (IOException e) {
        System.out.println("Error al leer: " + e.getMessage());
        return -1;
    }
    
    return contador;
}
```
````

```exercise
:label: ej-io-buffer
Dada una tarea que consiste en leer un archivo de 1GB carácter por carácter, compará el impacto en el rendimiento de usar `FileReader` directamente vs. envolverlo en un `BufferedReader`.
```

```solution
:for: ej-io-buffer
El uso de `FileReader` directamente causaría millones de llamadas al sistema (una por cada carácter), lo que degradaría el rendimiento debido al *overhead* del sistema operativo. `BufferedReader` leería bloques (típicamente de 8KB) en una sola operación, sirviendo los siguientes caracteres desde la RAM, lo que resultaría en un tiempo de ejecución órdenes de magnitud menor.
```

```exercise
:label: ej-copiar-filtrado
Escribí un método que copie un archivo de texto a otro, pero solo las líneas que contengan una palabra específica.
```

````solution
:for: ej-copiar-filtrado
```java
import java.nio.file.*;
import java.io.*;

public static void copiarLineasConPalabra(String origen, 
                                          String destino, 
                                          String palabra) {
    Path archivoOrigen = Path.of(origen);
    Path archivoDestino = Path.of(destino);
    
    try (BufferedReader reader = Files.newBufferedReader(archivoOrigen);
         BufferedWriter writer = Files.newBufferedWriter(archivoDestino)) {
        
        String linea;
        int copiadas = 0;
        
        while ((linea = reader.readLine()) != null) {
            if (linea.contains(palabra)) {
                writer.write(linea);
                writer.newLine();
                copiadas = copiadas + 1;
            }
        }
        
        System.out.println("Se copiaron " + copiadas + " líneas");
        
    } catch (NoSuchFileException e) {
        System.out.println("Archivo no encontrado: " + e.getFile());
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
```
````

```exercise
:label: ej-try-resources
¿Por qué el siguiente código es problemático? Corregilo usando try-with-resources.

```java
BufferedReader reader = new BufferedReader(new FileReader("datos.txt"));
String primera = reader.readLine();
String segunda = reader.readLine();
reader.close();
System.out.println(primera + " - " + segunda);
```
```

````solution
:for: ej-try-resources
**Problemas:**
1. Si `readLine()` lanza una excepción, `close()` nunca se ejecuta (fuga de recursos)
2. Si `close()` falla, no hay manejo del error

**Corrección:**
```java
try (BufferedReader reader = new BufferedReader(new FileReader("datos.txt"))) {
    String primera = reader.readLine();
    String segunda = reader.readLine();
    System.out.println(primera + " - " + segunda);
} catch (IOException e) {
    System.out.println("Error al leer: " + e.getMessage());
}
// El reader se cierra automáticamente, incluso si hay excepción
```
````

## Testing de Archivos con JUnit

Cuando se prueban métodos que trabajan con archivos, es fundamental asegurar que cada test tenga un entorno limpio e independiente. Las anotaciones `@BeforeEach` y `@AfterEach` de JUnit permiten crear y eliminar archivos temporales antes y después de cada test.

### Ciclo de Vida de un Test

```
@BeforeEach → Se ejecuta ANTES de cada método @Test
    ↓
@Test        → Se ejecuta el test
    ↓
@AfterEach  → Se ejecuta DESPUÉS de cada método @Test (incluso si falla)
```

### Ejemplo Básico: Crear y Limpiar Archivos

```{code} java
:caption: Test con gestión de archivos temporales

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LectorArchivosTest {
    
    private Path archivoTemporal;
    
    @BeforeEach
    void setUp() throws IOException {
        // Crear archivo temporal antes de cada test
        archivoTemporal = Files.createTempFile("test_", ".txt");
    }
    
    @AfterEach
    void tearDown() throws IOException {
        // Eliminar archivo temporal después de cada test
        Files.deleteIfExists(archivoTemporal);
    }
    
    @Test
    void leerArchivo_conContenido_retornaLineas() throws IOException {
        // Arrange: preparar el archivo con contenido
        List<String> contenido = List.of("línea 1", "línea 2", "línea 3");
        Files.write(archivoTemporal, contenido);
        
        // Act: leer el archivo
        List<String> resultado = Files.readAllLines(archivoTemporal);
        
        // Assert: verificar el contenido
        assertEquals(3, resultado.size());
        assertEquals("línea 1", resultado.get(0));
    }
    
    @Test
    void leerArchivo_vacio_retornaListaVacia() throws IOException {
        // El archivo ya existe vacío por @BeforeEach
        
        List<String> resultado = Files.readAllLines(archivoTemporal);
        
        assertTrue(resultado.isEmpty());
    }
}
```

### Testing con Directorio Temporal

Para tests que requieren múltiples archivos, se puede crear un directorio temporal:

```{code} java
:caption: Test con directorio temporal

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ProcesadorDirectorioTest {
    
    private Path directorioTemporal;
    
    @BeforeEach
    void setUp() throws IOException {
        directorioTemporal = Files.createTempDirectory("test_dir_");
    }
    
    @AfterEach
    void tearDown() throws IOException {
        // Eliminar todos los archivos del directorio
        if (Files.exists(directorioTemporal)) {
            Files.walk(directorioTemporal)
                 .sorted((a, b) -> b.compareTo(a))  // Orden inverso: archivos antes que directorios
                 .forEach(path -> {
                     try {
                         Files.delete(path);
                     } catch (IOException e) {
                         // Ignorar errores de eliminación en cleanup
                     }
                 });
        }
    }
    
    @Test
    void contarArchivos_conTresArchivos_retornaTres() throws IOException {
        // Crear archivos de prueba
        Files.createFile(directorioTemporal.resolve("archivo1.txt"));
        Files.createFile(directorioTemporal.resolve("archivo2.txt"));
        Files.createFile(directorioTemporal.resolve("archivo3.txt"));
        
        long cantidad = Files.list(directorioTemporal).count();
        
        assertEquals(3, cantidad);
    }
}
```

### Uso de `@TempDir` de JUnit 5

JUnit 5 proporciona la anotación `@TempDir` que simplifica la gestión de archivos temporales:

```{code} java
:caption: Test con @TempDir (JUnit 5)

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArchivoCSVTest {
    
    @TempDir
    Path directorioTemporal;  // JUnit crea y elimina automáticamente
    
    @Test
    void escribirCSV_creaArchivoConContenido() throws IOException {
        Path archivo = directorioTemporal.resolve("datos.csv");
        
        List<String> lineas = List.of("nombre,edad", "Juan,25", "María,30");
        Files.write(archivo, lineas);
        
        assertTrue(Files.exists(archivo));
        assertEquals(3, Files.readAllLines(archivo).size());
    }
    
    @Test
    void leerCSV_archivoConDatos_parseaCorrectamente() throws IOException {
        // Preparar archivo CSV
        Path archivo = directorioTemporal.resolve("entrada.csv");
        Files.writeString(archivo, "producto,precio\nmanzana,100\npera,150");
        
        // Leer y verificar
        List<String> lineas = Files.readAllLines(archivo);
        
        assertEquals(3, lineas.size());
        assertTrue(lineas.get(1).contains("manzana"));
    }
}
```

:::{tip} Preferí `@TempDir`
La anotación `@TempDir` es más limpia y segura que manejar archivos temporales manualmente. JUnit garantiza la limpieza incluso si el test falla con una excepción inesperada.
:::

### Testing de Excepciones de I/O

```{code} java
:caption: Test de excepciones

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ManejadorArchivosTest {
    
    @TempDir
    Path directorioTemporal;
    
    @Test
    void leerArchivo_noExiste_lanzaNoSuchFileException() {
        Path archivoInexistente = directorioTemporal.resolve("no_existe.txt");
        
        assertThrows(NoSuchFileException.class, () -> {
            Files.readAllLines(archivoInexistente);
        });
    }
    
    @Test
    void crearArchivo_yaExiste_lanzaFileAlreadyExistsException() throws IOException {
        Path archivo = directorioTemporal.resolve("existente.txt");
        Files.createFile(archivo);  // Crear primero
        
        assertThrows(FileAlreadyExistsException.class, () -> {
            Files.createFile(archivo);  // Intentar crear de nuevo
        });
    }
}
```

### Patrón Completo: Test de un Procesador de Archivos

```{code} java
:caption: Ejemplo completo de testing

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContadorPalabrasTest {
    
    @TempDir
    Path dirTemp;
    
    private Path archivoEntrada;
    private Path archivoSalida;
    
    @BeforeEach
    void setUp() throws IOException {
        archivoEntrada = dirTemp.resolve("entrada.txt");
        archivoSalida = dirTemp.resolve("salida.txt");
    }
    
    @Test
    void contarPalabras_textoSimple_cuentaCorrectamente() throws IOException {
        // Arrange
        Files.writeString(archivoEntrada, "hola mundo\nadios mundo");
        
        // Act
        int cantidad = contarPalabras(archivoEntrada);
        
        // Assert
        assertEquals(4, cantidad);
    }
    
    @Test
    void contarPalabras_archivoVacio_retornaCero() throws IOException {
        Files.createFile(archivoEntrada);
        
        int cantidad = contarPalabras(archivoEntrada);
        
        assertEquals(0, cantidad);
    }
    
    @Test
    void procesarArchivo_guardaResultado() throws IOException {
        Files.writeString(archivoEntrada, "uno dos tres");
        
        procesarYGuardar(archivoEntrada, archivoSalida);
        
        assertTrue(Files.exists(archivoSalida));
        String contenido = Files.readString(archivoSalida);
        assertTrue(contenido.contains("3"));
    }
    
    // Métodos auxiliares para el test
    private int contarPalabras(Path archivo) throws IOException {
        return Files.readAllLines(archivo).stream()
                    .flatMap(linea -> List.of(linea.split("\\s+")).stream())
                    .filter(palabra -> !palabra.isEmpty())
                    .mapToInt(palabra -> 1)
                    .sum();
    }
    
    private void procesarYGuardar(Path entrada, Path salida) throws IOException {
        int cantidad = contarPalabras(entrada);
        Files.writeString(salida, "Palabras: " + cantidad);
    }
}
```

:::{important} Independencia de Tests
Cada test debe ser independiente. Nunca asumas que un test anterior dejó el sistema en cierto estado. Usá `@BeforeEach` para establecer las precondiciones de cada test.
:::

## Referencias Bibliográficas

- **Schildt, H.** (2022). _Java: A Beginner's Guide_ (9na ed.). McGraw Hill. (Capítulo 10: Using I/O).
- **Liang, Y. D.** (2017). _Introduction to Java Programming and Data Structures_ (11va ed.). Pearson.
- **Bloch, J.** (2018). _Effective Java_ (3ra ed.). Addison-Wesley Professional. (Item 9: Prefer try-with-resources to try-finally).
- **Oracle Corporation.** (2023). _Java I/O, NIO, and NIO.2_. [Official Documentation](https://docs.oracle.com/javase/tutorial/essential/io/).

:::seealso
- {ref}`regla-0x3001` - Manejo de excepciones en operaciones de I/O.
- {ref}`regla-0x000D` - Documentación de métodos que lanzan IOException.
:::
