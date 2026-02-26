---
title: "Manejo de Archivos en Java"
description: Guía completa sobre lectura y escritura de archivos con java.nio.
---

# Manejo de Archivos en Java

Java proporciona múltiples formas de trabajar con archivos. Este apunte se centra en la API moderna `java.nio.file` (New I/O), disponible desde Java 7, que ofrece un manejo más robusto y flexible que la antigua API `java.io`.

## Conceptos Básicos

### La Clase Path

`Path` es una **interfaz** que representa la ubicación de un archivo o directorio en el sistema de archivos:

```{code} java
:caption: Creación de objetos Path

import java.nio.file.Path;
import java.nio.file.Paths;

// Desde un string
Path archivo = Paths.get("archivo.txt");
Path rutaAbsoluta = Paths.get("C:/datos/archivo.txt");  // Windows
Path rutaUnix = Paths.get("/home/usuario/archivo.txt"); // Unix/Linux

// Combinando partes
Path combinado = Paths.get("directorio", "subdirectorio", "archivo.txt");

// Desde Java 11: método estático en Path
Path moderno = Path.of("archivo.txt");
```

### Rutas Relativas vs Absolutas

```{code} java
:caption: Tipos de rutas

// Ruta relativa: se resuelve desde el directorio de trabajo actual
Path relativa = Paths.get("datos/archivo.txt");

// Ruta absoluta: ubicación completa en el sistema de archivos
Path absoluta = Paths.get("/home/usuario/datos/archivo.txt");

// Verificar tipo
boolean esAbsoluta = archivo.isAbsolute();

// Convertir relativa a absoluta
Path absolutaDesdeRelativa = relativa.toAbsolutePath();
```

:::{note}
**Directorio de trabajo**: En IntelliJ IDEA, es típicamente la raíz del proyecto. Desde consola, es el directorio desde donde se ejecuta el programa.
:::

### Métodos Útiles de Path

```{code} java
:caption: Métodos de Path

Path ruta = Paths.get("/home/usuario/documentos/informe.pdf");

// Obtener partes
Path nombre = ruta.getFileName();     // "informe.pdf"
Path padre = ruta.getParent();        // "/home/usuario/documentos"
Path raiz = ruta.getRoot();           // "/"

// Número de elementos
int elementos = ruta.getNameCount();  // 4

// Obtener elemento por índice
Path elem = ruta.getName(2);          // "documentos"

// Normalizar rutas con . y ..
Path conPuntos = Paths.get("/home/usuario/../usuario/./docs");
Path normalizada = conPuntos.normalize();  // "/home/usuario/docs"

// Resolver rutas (combinar)
Path base = Paths.get("/home/usuario");
Path completa = base.resolve("archivo.txt");  // "/home/usuario/archivo.txt"
```

## La Clase Files

`Files` es una clase utilitaria con métodos estáticos para operaciones con archivos y directorios:

### Verificación de Existencia

```{code} java
:caption: Verificación de archivos

import java.nio.file.Files;
import java.nio.file.Path;

Path archivo = Paths.get("datos.txt");

boolean existe = Files.exists(archivo);
boolean noExiste = Files.notExists(archivo);
boolean esArchivo = Files.isRegularFile(archivo);
boolean esDirectorio = Files.isDirectory(archivo);
boolean legible = Files.isReadable(archivo);
boolean escribible = Files.isWritable(archivo);
```

### Creación de Archivos y Directorios

```{code} java
:caption: Crear archivos y directorios

// Crear archivo vacío
Path nuevoArchivo = Files.createFile(Paths.get("nuevo.txt"));

// Crear directorio
Path nuevoDir = Files.createDirectory(Paths.get("nuevoDirectorio"));

// Crear directorios anidados (incluyendo padres)
Path anidados = Files.createDirectories(Paths.get("a/b/c/d"));

// Crear archivo temporal
Path temporal = Files.createTempFile("prefijo-", ".tmp");
```

### Copiar, Mover y Eliminar

```{code} java
:caption: Operaciones con archivos

import java.nio.file.StandardCopyOption;

Path origen = Paths.get("original.txt");
Path destino = Paths.get("copia.txt");

// Copiar
Files.copy(origen, destino);

// Copiar reemplazando si existe
Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);

// Mover (renombrar)
Files.move(origen, destino);

// Eliminar
Files.delete(destino);  // Lanza excepción si no existe

// Eliminar si existe
Files.deleteIfExists(destino);  // Retorna boolean
```

### Información de Archivos

```{code} java
:caption: Obtener información de archivos

Path archivo = Paths.get("documento.txt");

// Tamaño en bytes
long tamanio = Files.size(archivo);

// Tiempo de última modificación
FileTime tiempo = Files.getLastModifiedTime(archivo);
```

## Lectura de Archivos

### Leer Todo el Contenido

```{code} java
:caption: Lectura completa de archivos

import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.util.List;

Path archivo = Paths.get("datos.txt");

// Leer como String (Java 11+)
String contenido = Files.readString(archivo);

// Leer todas las líneas como lista
List<String> lineas = Files.readAllLines(archivo);

// Con codificación específica
List<String> lineasUtf8 = Files.readAllLines(archivo, StandardCharsets.UTF_8);

// Leer bytes
byte[] bytes = Files.readAllBytes(archivo);
```

:::{warning}
Los métodos que leen todo el archivo son convenientes pero **cargan todo en memoria**. Para archivos grandes, usá lectura línea por línea.
:::

### Lectura con BufferedReader

Para archivos grandes o procesamiento línea por línea:

```{code} java
:caption: Lectura con BufferedReader

import java.io.BufferedReader;
import java.nio.file.Files;

Path archivo = Paths.get("grande.txt");

try (BufferedReader reader = Files.newBufferedReader(archivo)) {
    String linea;
    while ((linea = reader.readLine()) != null) {
        System.out.println(linea);
    }
} catch (IOException e) {
    System.err.println("Error de lectura: " + e.getMessage());
}
```

### Lectura con Scanner

```{code} java
:caption: Lectura con Scanner

import java.util.Scanner;
import java.io.File;

try (Scanner scanner = new Scanner(new File("datos.txt"))) {
    while (scanner.hasNextLine()) {
        String linea = scanner.nextLine();
        System.out.println(linea);
    }
} catch (FileNotFoundException e) {
    System.err.println("Archivo no encontrado");
}
```

:::{warning}
**Programación Funcional No Permitida**

En este curso **no se permite** el uso de Streams de Java 8+ (`Files.lines()`, `stream()`, `filter()`, `map()`, etc.) ni expresiones lambda. Se debe usar lectura imperativa con `BufferedReader` y lazos tradicionales.
:::

## Escritura de Archivos

### Escribir Todo el Contenido

```{code} java
:caption: Escritura completa de archivos

import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

Path archivo = Paths.get("salida.txt");

// Escribir String (Java 11+)
Files.writeString(archivo, "Contenido del archivo");

// Escribir lista de líneas
List<String> lineas = List.of("Línea 1", "Línea 2", "Línea 3");
Files.write(archivo, lineas);

// Agregar al final (append)
Files.writeString(archivo, "\nMás contenido", StandardOpenOption.APPEND);

// Escribir bytes
byte[] datos = "Bytes".getBytes();
Files.write(archivo, datos);
```

### Opciones de Apertura

```{code} java
:caption: StandardOpenOption

import java.nio.file.StandardOpenOption;

Path archivo = Paths.get("datos.txt");

// Crear si no existe, truncar si existe
Files.writeString(archivo, "texto", 
    StandardOpenOption.CREATE,
    StandardOpenOption.TRUNCATE_EXISTING);

// Crear si no existe, agregar al final
Files.writeString(archivo, "más texto",
    StandardOpenOption.CREATE,
    StandardOpenOption.APPEND);

// Solo crear (error si ya existe)
Files.writeString(archivo, "nuevo",
    StandardOpenOption.CREATE_NEW);
```

### Escritura con BufferedWriter

Para escritura controlada línea por línea:

```{code} java
:caption: Escritura con BufferedWriter

import java.io.BufferedWriter;

Path archivo = Paths.get("salida.txt");

try (BufferedWriter writer = Files.newBufferedWriter(archivo)) {
    writer.write("Primera línea");
    writer.newLine();
    writer.write("Segunda línea");
    writer.newLine();
} catch (IOException e) {
    System.err.println("Error de escritura: " + e.getMessage());
}
```

### Escritura con Formatter

Para salida formateada similar a `printf`:

```{code} java
:caption: Escritura con Formatter

import java.util.Formatter;

Path archivo = Paths.get("reporte.txt");

try (Formatter formatter = new Formatter(archivo.toFile())) {
    formatter.format("Usuario: %s%n", "Juan");
    formatter.format("Puntuación: %d%n", 95);
    formatter.format("Promedio: %.2f%n", 87.5);
} catch (FileNotFoundException e) {
    System.err.println("No se pudo crear el archivo");
}
```

## Listado de Directorios

### DirectoryStream

```{code} java
:caption: Listar contenido de directorio

import java.nio.file.DirectoryStream;

Path directorio = Paths.get(".");

try (DirectoryStream<Path> stream = Files.newDirectoryStream(directorio)) {
    for (Path entrada : stream) {
        System.out.println(entrada.getFileName());
    }
}

// Con filtro por extensión
try (DirectoryStream<Path> stream = 
        Files.newDirectoryStream(directorio, "*.txt")) {
    for (Path archivo : stream) {
        System.out.println(archivo.getFileName());
    }
}
```

### Filtros Personalizados

```{code} java
:caption: Filtro personalizado de DirectoryStream

DirectoryStream.Filter<Path> filtroGrandes = new DirectoryStream.Filter<>() {
    @Override
    public boolean accept(Path entry) throws IOException {
        return Files.size(entry) > 1024 * 1024;  // > 1 MB
    }
};

try (DirectoryStream<Path> stream = 
        Files.newDirectoryStream(directorio, filtroGrandes)) {
    for (Path archivo : stream) {
        System.out.printf("%s: %d bytes%n", 
            archivo.getFileName(), 
            Files.size(archivo));
    }
}
```

### Recorrido Recursivo

```{code} java
:caption: Recorrer directorios recursivamente

import java.nio.file.FileVisitResult;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

Path inicio = Paths.get(".");

Files.walkFileTree(inicio, new SimpleFileVisitor<Path>() {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        System.out.println(file);
        return FileVisitResult.CONTINUE;
    }
});
```

## Manejo de Excepciones de I/O

### Excepciones Comunes

:::{table} Excepciones de entrada/salida
:label: tbl-excepciones-io

| Excepción | Descripción |
| :-------- | :---------- |
| `IOException` | Error general de I/O |
| `FileNotFoundException` | Archivo no encontrado |
| `NoSuchFileException` | Archivo/directorio no existe (NIO) |
| `FileAlreadyExistsException` | Archivo ya existe |
| `DirectoryNotEmptyException` | Directorio no está vacío |
| `AccessDeniedException` | Sin permisos de acceso |

:::

### Try-with-resources

Siempre usá try-with-resources para cerrar recursos automáticamente:

```{code} java
:caption: Try-with-resources para archivos

// Cierra automáticamente incluso si hay excepción
try (BufferedReader reader = Files.newBufferedReader(archivo);
     BufferedWriter writer = Files.newBufferedWriter(salida)) {
    
    String linea;
    while ((linea = reader.readLine()) != null) {
        writer.write(linea.toUpperCase());
        writer.newLine();
    }
    
} catch (IOException e) {
    System.err.println("Error: " + e.getMessage());
}
```

## Ejercicios

```{exercise}
:label: ej-archivos-1

Escribí un método `int contarLineas(Path archivo)` que cuente la cantidad de líneas no vacías en un archivo de texto.
```

```{solution} ej-archivos-1
```java
public int contarLineas(Path archivo) throws IOException {
    int contador = 0;
    
    try (BufferedReader reader = Files.newBufferedReader(archivo)) {
        String linea;
        while ((linea = reader.readLine()) != null) {
            if (!linea.isBlank()) {
                contador++;
            }
        }
    }
    
    return contador;
}
```
```

```{exercise}
:label: ej-archivos-2

Implementá un método que lea un archivo CSV simple (valores separados por coma) y retorne una lista de arreglos de Strings, donde cada arreglo representa una fila.
```

```{solution} ej-archivos-2
```java
public List<String[]> leerCSV(Path archivo) throws IOException {
    List<String[]> datos = new ArrayList<>();
    
    try (BufferedReader reader = Files.newBufferedReader(archivo)) {
        String linea;
        while ((linea = reader.readLine()) != null) {
            if (!linea.isBlank()) {
                String[] campos = linea.split(",");
                // Trim de cada campo
                for (int i = 0; i < campos.length; i++) {
                    campos[i] = campos[i].trim();
                }
                datos.add(campos);
            }
        }
    }
    
    return datos;
}
```
```

```{exercise}
:label: ej-archivos-3

Creá un método que copie un directorio completo (incluyendo subdirectorios y archivos) a otra ubicación.
```

```{solution} ej-archivos-3
```java
public void copiarDirectorio(Path origen, Path destino) throws IOException {
    Files.walkFileTree(origen, new SimpleFileVisitor<Path>() {
        
        @Override
        public FileVisitResult preVisitDirectory(Path dir, 
                BasicFileAttributes attrs) throws IOException {
            Path dirDestino = destino.resolve(origen.relativize(dir));
            Files.createDirectories(dirDestino);
            return FileVisitResult.CONTINUE;
        }
        
        @Override
        public FileVisitResult visitFile(Path file, 
                BasicFileAttributes attrs) throws IOException {
            Path archivoDestino = destino.resolve(origen.relativize(file));
            Files.copy(file, archivoDestino, 
                      StandardCopyOption.REPLACE_EXISTING);
            return FileVisitResult.CONTINUE;
        }
    });
}
```
```

:::{seealso}
- [Documentación de Path](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/nio/file/Path.html)
- [Documentación de Files](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/nio/file/Files.html)
- [Tutorial oficial de I/O](https://docs.oracle.com/javase/tutorial/essential/io/)
:::
