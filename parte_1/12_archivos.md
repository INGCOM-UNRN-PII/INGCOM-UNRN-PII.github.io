---
title: "Manejo de Archivos en Java"
description: Estudio técnico sobre flujos de datos, NIO.2, manejo de excepciones y persistencia en sistemas de archivos.
---

(manejo-de-archivos-en-java)=
# Manejo de Archivos en Java

Hasta ahora, todos los datos que manejaste en tus programas existían solo mientras el programa estaba en ejecución. Cuando el programa terminaba, toda la información se perdía. Si querías volver a usar esos datos, tenías que ingresarlos de nuevo. Los **archivos** resuelven este problema: permiten guardar información de manera **persistente** en el disco, para que esté disponible la próxima vez que ejecutes el programa, o incluso para que otros programas la lean.

En la cursada de Programación I con C, ya trabajaste con archivos usando `FILE*`, `fopen()`, `fread()`, `fwrite()` y `fclose()`. Java ofrece un enfoque similar en concepto, pero con diferencias importantes en la implementación que hacen el código más seguro y legible.

(por-que-necesitamos-archivos)=
## ¿Por qué necesitamos archivos?

Pensá en cualquier aplicación real:

- Un editor de texto necesita guardar documentos
- Un juego necesita guardar partidas
- Una aplicación de notas necesita recordar lo que escribiste ayer
- Un sistema de facturación necesita mantener registros históricos

Sin archivos, cada vez que cerrás la aplicación perdés todo. Los archivos permiten **persistir** (guardar de forma permanente) la información más allá de la ejecución del programa.

(memoria-ram-vs-disco)=
### Memoria RAM vs. Disco

Cuando tu programa crea una variable, esa variable vive en la **memoria RAM**. La RAM es rápida pero **volátil**: cuando apagás la computadora (o el programa termina), el contenido desaparece.

Los archivos viven en el **disco** (ya sea un disco rígido tradicional o un SSD). El disco es más lento que la RAM, pero es **persistente**: la información permanece aunque apagues la computadora.

| Característica | Memoria RAM | Disco (archivos) |
|:---|:---|:---|
| Velocidad | Muy rápida (nanosegundos) | Más lenta (microsegundos a milisegundos) |
| Persistencia | Volátil (se pierde al apagar) | Persistente (sobrevive al apagado) |
| Acceso | Acceso directo por dirección | Acceso a través del sistema operativo |
| Capacidad | Limitada (GB) | Mucho mayor (TB) |

:::{note} Comparativa con C
En C, el manejo de archivos se hace con punteros `FILE*` y funciones como `fopen()`, `fread()`, `fclose()`. Java proporciona un enfoque similar pero con mayor seguridad: gestión automática de recursos y excepciones tipadas que indican exactamente qué salió mal.
:::

(conceptos-fundamentales-como-funcionan-los-archivos)=
## Conceptos Fundamentales: Cómo funcionan los archivos

Antes de ver código, es importante entender qué pasa "detrás de escena" cuando un programa trabaja con archivos.

(el-sistema-de-archivos)=
### El Sistema de Archivos

El **sistema de archivos** es la estructura que el sistema operativo usa para organizar la información en el disco. Funciona como un árbol de carpetas (directorios) que contienen archivos o más carpetas.

En Linux y macOS, las rutas usan barras `/`:
```
/home/usuario/documentos/datos.txt
```

En Windows, las rutas usan barras invertidas `\`:
```
C:\Users\usuario\documentos\datos.txt
```

Java maneja automáticamente estas diferencias, por lo que tu código puede funcionar en cualquier sistema operativo sin cambios.

(rutas-absolutas-vs-relativas)=
### Rutas Absolutas vs. Relativas

Una **ruta absoluta** especifica la ubicación completa desde la raíz del sistema de archivos:
```
/home/usuario/proyecto/datos.txt     (Linux/macOS)
C:\Users\usuario\proyecto\datos.txt  (Windows)
```

Una **ruta relativa** especifica la ubicación a partir del **directorio actual** donde se ejecuta el programa:
```
datos.txt                  (en el directorio actual)
subdirectorio/datos.txt    (en un subdirectorio)
../otro/datos.txt          (subiendo un nivel y entrando a otro)
```

En C, recordarás que `fopen("datos.txt", "r")` buscaba el archivo en el directorio actual. Java funciona igual con rutas relativas.

(bytes-vs-caracteres-una-distincion-crucial)=
### Bytes vs. Caracteres: Una distinción crucial

En C, trabajabas con archivos principalmente como secuencias de bytes (`fread`, `fwrite`), aunque también podías leer texto línea por línea con `fgets`. Java hace una distinción más explícita entre dos tipos de datos:

**Bytes**: Son los datos "crudos", sin interpretación. Un byte es simplemente un número entre 0 y 255. Esto incluye:
- Imágenes (PNG, JPG, GIF)
- Audio (MP3, WAV)
- Ejecutables (programas compilados)
- Cualquier archivo que no sea texto puro

**Caracteres**: Son texto interpretado según una codificación. La letra 'A' no es simplemente el número 65; depende de cómo se codifique. Esto incluye:
- Archivos de texto (.txt)
- Código fuente (.java, .c, .py)
- Archivos de configuración (.json, .xml, .csv)

| Tipo de Datos | En C usabas... | En Java se usa... | Ejemplos |
|:---|:---|:---|:---|
| **Bytes** | `fread()`, `fwrite()` | `InputStream`, `OutputStream` | Imágenes, audio, ejecutables |
| **Caracteres** | `fgets()`, `fputs()` | `Reader`, `Writer` | Texto, código fuente, CSV |

```{code} java
:caption: Diferencia conceptual entre bytes y caracteres

// Para archivos binarios (bytes) - como imágenes
InputStream entrada = new FileInputStream("imagen.png");

// Para archivos de texto (caracteres)
Reader lector = new FileReader("documento.txt");
```

:::{important} ¿Cuándo usar cada uno?
- Si el archivo es **texto que vas a leer/escribir como String**, usá Reader/Writer
- Si el archivo es **cualquier otra cosa** (imagen, audio, datos binarios), usá InputStream/OutputStream
- Si tenés dudas, pensá: ¿puedo abrir este archivo en el Bloc de Notas y leer algo coherente? Si sí, es texto.
:::

(el-concepto-de-flujo-stream)=
### El concepto de "Flujo" (Stream)

Tanto en C como en Java, los archivos se manejan como **flujos** (streams) de datos. Un flujo es como una manguera por la que pasan los datos, byte por byte (o carácter por carácter).

En C tenías:
```c
FILE *archivo = fopen("datos.txt", "r");  // Abrir flujo
char c = fgetc(archivo);                  // Leer del flujo
fclose(archivo);                          // Cerrar flujo
```

En Java es conceptualmente igual:
```java
FileReader archivo = new FileReader("datos.txt");  // Abrir flujo
int c = archivo.read();                            // Leer del flujo
archivo.close();                                   // Cerrar flujo
```

La diferencia principal es que en Java usamos diferentes tipos según si trabajamos con bytes o caracteres, y el manejo de errores es más explícito (con excepciones).

(el-buffering-por-que-es-importante)=
### El Buffering: Por qué es importante

Cada vez que tu programa lee o escribe un byte del disco, debe hacer una **llamada al sistema operativo**. Estas llamadas son lentas comparadas con operaciones en memoria. Si leés un archivo de 1 millón de caracteres uno por uno, hacés 1 millón de llamadas al sistema.

El **buffering** es una técnica que agrupa muchas operaciones pequeñas en pocas operaciones grandes. En lugar de leer 1 byte a la vez del disco, se leen (por ejemplo) 8KB de una vez y se guardan en memoria. Las siguientes lecturas se hacen desde memoria hasta que el buffer se vacía, momento en que se lee otro bloque de 8KB.

En C, cuando usabas `FILE*` con funciones como `fgets()`, ya tenías buffering automático. En Java, tenés que ser más explícito:

```{code} java
:caption: Comparación con y sin buffer

// Sin buffer: una llamada al sistema por cada carácter (LENTO)
FileReader lento = new FileReader("datos.txt");

// Con buffer: lee bloques grandes, sirve desde memoria (RÁPIDO)
BufferedReader rapido = new BufferedReader(new FileReader("datos.txt"));
```

Esta técnica de "envolver" un flujo básico con funcionalidad adicional (como buffering) es muy común en Java.

(nio-2-la-api-moderna-de-java)=
## NIO.2: La API Moderna de Java

Java tiene dos APIs principales para manejar archivos:

1. **java.io** (la API clásica): Existe desde Java 1.0, usa clases como `File`, `FileReader`, `FileWriter`
2. **java.nio.file** (NIO.2, la API moderna): Introducida en Java 7, usa clases como `Path`, `Files`

Vamos a usar principalmente la API moderna (NIO.2) porque es más potente, más segura, y produce código más legible. Sin embargo, vas a encontrar la API clásica en código existente, así que es bueno conocer ambas.

(la-clase-path-representando-rutas)=
### La Clase `Path`: Representando Rutas

`Path` representa una ruta en el sistema de archivos. No es el archivo en sí, sino la **dirección** donde está (o estará) el archivo. Pensalo como la diferencia entre una dirección postal y la casa que está en esa dirección.

```{code} java
:caption: Creación de objetos Path

import java.nio.file.Path;

// Crear Path desde un String simple
Path archivo = Path.of("datos.txt");

// Crear Path con ruta absoluta en Linux/macOS
Path rutaAbsoluta = Path.of("/home/usuario/documentos/datos.txt");

// Crear Path con ruta absoluta en Windows
Path rutaWindows = Path.of("C:\\Users\\usuario\\datos.txt");

// Crear Path combinando componentes (funciona en cualquier SO)
Path ruta = Path.of("home", "usuario", "documentos", "datos.txt");
// Java automáticamente usa el separador correcto (/ o \)
```

**¿Por qué `Path.of()` y no un constructor?**

En Java, `Path.of()` es un **método de fábrica** (factory method). En lugar de escribir `new Path(...)`, llamás a un método que te devuelve el objeto. Esto permite que Java elija internamente la implementación más adecuada según tu sistema operativo. No te preocupes por los detalles ahora; simplemente usá `Path.of()`.

(obteniendo-informacion-de-un-path)=
### Obteniendo información de un Path

Una vez que tenés un `Path`, podés extraer información sobre la ruta:

```{code} java
:caption: Métodos de información de Path

import java.nio.file.Path;

Path ruta = Path.of("/home/usuario/documentos/informe.txt");

// Obtener solo el nombre del archivo (sin la ruta)
String nombre = ruta.getFileName().toString();    // "informe.txt"

// Obtener el directorio padre
Path padre = ruta.getParent();                    // "/home/usuario/documentos"

// Obtener la raíz del sistema de archivos
Path raiz = ruta.getRoot();                       // "/"

// Convertir ruta relativa a absoluta
Path relativa = Path.of("datos.txt");
Path absoluta = relativa.toAbsolutePath();        // Ej: "/home/usuario/proyecto/datos.txt"

// Combinar rutas (resolver una ruta relativa a partir de otra)
Path directorio = Path.of("/home/usuario");
Path completa = directorio.resolve("documentos/archivo.txt");
// Resultado: "/home/usuario/documentos/archivo.txt"
```

:::{tip} Path no verifica existencia
Crear un objeto `Path` **no** verifica si el archivo existe. Solo representa una ruta. Podés crear un `Path` a un archivo que no existe, y después crear ese archivo. La verificación de existencia se hace con métodos de la clase `Files`.
:::

(la-clase-files-operaciones-con-archivos)=
### La Clase `Files`: Operaciones con Archivos

`Files` es una clase de utilidad que contiene métodos estáticos para realizar operaciones con archivos y directorios. Pensala como una caja de herramientas donde cada herramienta es un método.

```{code} java
:caption: Operaciones básicas con Files

import java.nio.file.Files;
import java.nio.file.Path;

Path archivo = Path.of("datos.txt");

// Verificar si el archivo o directorio existe
boolean existe = Files.exists(archivo);

// Verificar si es un archivo regular (no directorio ni enlace especial)
boolean esArchivo = Files.isRegularFile(archivo);

// Verificar si es un directorio
boolean esDirectorio = Files.isDirectory(archivo);

// Verificar permisos de acceso
boolean sePuedeLeer = Files.isReadable(archivo);
boolean sePuedeEscribir = Files.isWritable(archivo);
boolean sePuedeEjecutar = Files.isExecutable(archivo);

// Obtener el tamaño del archivo en bytes
long tamanioBytes = Files.size(archivo);
```

(12-comparacion-con-c)=
### Comparación con C

| Operación | En C | En Java (NIO.2) |
|:---|:---|:---|
| Representar ruta | `char* ruta = "/path/file.txt";` | `Path ruta = Path.of("/path/file.txt");` |
| Verificar existencia | `access(ruta, F_OK)` o `fopen` | `Files.exists(ruta)` |
| Obtener tamaño | `stat()` y estructura `st_size` | `Files.size(ruta)` |
| Verificar permisos | `access(ruta, R_OK)` | `Files.isReadable(ruta)` |

(lectura-de-archivos-de-texto)=
## Lectura de Archivos de Texto

Vamos a ver las diferentes formas de leer archivos de texto, desde la más simple hasta las más eficientes.

(metodo-simple-leer-todas-las-lineas-de-una-vez)=
### Método Simple: Leer Todas las Líneas de Una Vez

Para archivos pequeños (que caben cómodamente en memoria), la forma más simple es leer todas las líneas de una vez:

```{code} java
:caption: Leer todas las líneas con Files.readAllLines()

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.io.IOException;

public static void leerArchivoCompleto(String rutaArchivo) {
    Path archivo = Path.of(rutaArchivo);
    
    try {
        // readAllLines() devuelve una Lista con todas las líneas del archivo
        // Cada elemento de la lista es una línea (sin el salto de línea al final)
        List<String> lineas = Files.readAllLines(archivo);
        
        // Recorrer e imprimir cada línea
        for (String linea : lineas) {
            System.out.println(linea);
        }
        
        // También podés acceder por índice:
        // String primeraLinea = lineas.get(0);
        // int cantidadLineas = lineas.size();
        
    } catch (IOException e) {
        System.out.println("Error al leer archivo: " + e.getMessage());
    }
}
```

**¿Qué es `List<String>`?**

`List<String>` es una lista (colección ordenada) donde cada elemento es un String. Es similar a un arreglo de Strings, pero con más flexibilidad. Podés:
- Obtener su tamaño con `lineas.size()`
- Acceder a elementos por índice con `lineas.get(indice)`
- Recorrerla con for-each como en el ejemplo

No te preocupes por los detalles ahora; cuando veamos colecciones en profundidad, todo quedará más claro.

:::{warning} Cuidado con Archivos Grandes
`readAllLines()` carga **todo** el archivo en memoria. Si el archivo tiene 1GB, necesitás 1GB de memoria RAM libre. Para archivos muy grandes, esto puede causar un error `OutOfMemoryError` (quedarse sin memoria). Usá lectura línea por línea para archivos grandes.
:::

(lectura-linea-por-linea-con-bufferedreader)=
### Lectura Línea por Línea con BufferedReader

Para archivos grandes, o cuando no necesitás todo el contenido en memoria al mismo tiempo, leé línea por línea:

```{code} java
:caption: Lectura línea por línea (eficiente para archivos grandes)

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public static void leerLineaPorLinea(String rutaArchivo) {
    Path archivo = Path.of(rutaArchivo);
    
    // try-with-resources: el BufferedReader se cierra automáticamente
    try (BufferedReader reader = Files.newBufferedReader(archivo)) {
        String linea;
        int numeroLinea = 1;
        
        // readLine() devuelve null cuando llega al final del archivo
        while ((linea = reader.readLine()) != null) {
            System.out.println(numeroLinea + ": " + linea);
            numeroLinea = numeroLinea + 1;
        }
    } catch (IOException e) {
        System.out.println("Error al leer: " + e.getMessage());
    }
}
```

**Análisis del código:**

1. `Files.newBufferedReader(archivo)` crea un BufferedReader que lee el archivo con buffering automático
2. `reader.readLine()` lee una línea completa del archivo y la devuelve como String
3. Cuando no hay más líneas, `readLine()` devuelve `null`
4. El patrón `while ((linea = reader.readLine()) != null)` es muy común en Java:
   - Primero asigna el resultado de `readLine()` a `linea`
   - Luego compara si `linea` es diferente de `null`
   - Si es diferente de `null`, entra al lazo; si es `null`, termina

**Comparación con C:**

```c
// En C
FILE *archivo = fopen("datos.txt", "r");
char linea[256];
int numeroLinea = 1;

while (fgets(linea, sizeof(linea), archivo) != NULL) {
    printf("%d: %s", numeroLinea, linea);
    numeroLinea++;
}
fclose(archivo);
```

La estructura es muy similar: un lazo que lee línea por línea hasta que la función de lectura indica que no hay más datos (retornando `NULL` en C o `null` en Java).

(lectura-de-todo-el-contenido-como-un-solo-string)=
### Lectura de Todo el Contenido como un Solo String

A veces querés todo el archivo como un único String (por ejemplo, para buscarlo con expresiones regulares):

```{code} java
:caption: Leer todo como un único String (Java 11+)

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public static String leerComoString(String rutaArchivo) throws IOException {
    Path archivo = Path.of(rutaArchivo);
    return Files.readString(archivo);  // Disponible desde Java 11
}

// Uso:
String contenido = leerComoString("poema.txt");
System.out.println("El archivo tiene " + contenido.length() + " caracteres");

// Podés buscar texto dentro del contenido:
if (contenido.contains("palabra buscada")) {
    System.out.println("Se encontró la palabra");
}
```

:::{note} Versión de Java
`Files.readString()` está disponible desde Java 11. Si usás una versión anterior, tendrías que usar `Files.readAllBytes()` y convertir a String:
```java
String contenido = new String(Files.readAllBytes(archivo), StandardCharsets.UTF_8);
```
:::

(escritura-de-archivos-de-texto)=
## Escritura de Archivos de Texto

Ahora veamos cómo guardar información en archivos. Las opciones son similares a la lectura: podés escribir todo de una vez o línea por línea.

(metodo-simple-escribir-una-lista-de-lineas)=
### Método Simple: Escribir una Lista de Líneas

La forma más simple de escribir un archivo es pasarle una lista de Strings a `Files.write()`:

```{code} java
:caption: Escribir líneas a un archivo

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Arrays;
import java.io.IOException;

public static void escribirLineas(String rutaArchivo) {
    Path archivo = Path.of(rutaArchivo);
    
    // Arrays.asList() crea una lista a partir de elementos individuales
    List<String> lineas = Arrays.asList(
        "Primera línea",
        "Segunda línea",
        "Tercera línea"
    );
    
    try {
        // write() crea el archivo si no existe
        // Si el archivo ya existe, lo SOBRESCRIBE completamente
        Files.write(archivo, lineas);
        System.out.println("Archivo escrito correctamente");
    } catch (IOException e) {
        System.out.println("Error al escribir: " + e.getMessage());
    }
}
```

**¿Qué hace `Files.write()` exactamente?**

1. Si el archivo no existe, lo crea
2. Si el archivo ya existe, lo **sobrescribe** (borra el contenido anterior)
3. Escribe cada String de la lista como una línea, agregando saltos de línea automáticamente
4. Cierra el archivo automáticamente

**Comparación con C:**

```c
// En C
FILE *archivo = fopen("datos.txt", "w");  // "w" = write (sobrescribe)
fprintf(archivo, "Primera línea\n");
fprintf(archivo, "Segunda línea\n");
fprintf(archivo, "Tercera línea\n");
fclose(archivo);
```

(escribir-un-unico-string)=
### Escribir un Único String

Si tenés todo el contenido en un solo String:

```{code} java
:caption: Escribir un String completo (Java 11+)

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public static void escribirString(String rutaArchivo, String contenido) {
    Path archivo = Path.of(rutaArchivo);
    
    try {
        Files.writeString(archivo, contenido);
        System.out.println("Contenido guardado");
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

// Uso:
escribirString("mensaje.txt", "Hola, mundo!\nEsta es la segunda línea.");
```

(escritura-con-bufferedwriter-mayor-control)=
### Escritura con BufferedWriter (Mayor Control)

Para mayor control o archivos grandes, usá BufferedWriter:

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
            writer.write(linea);      // Escribe el texto
            writer.newLine();         // Agrega salto de línea del sistema
        }
    } catch (IOException e) {
        System.out.println("Error al escribir: " + e.getMessage());
    }
}
```

**¿Por qué `writer.newLine()` en lugar de `"\n"`?**

El salto de línea varía según el sistema operativo:
- Linux/macOS: `\n` (LF, Line Feed)
- Windows: `\r\n` (CRLF, Carriage Return + Line Feed)

`writer.newLine()` usa automáticamente el salto de línea correcto para el sistema donde se ejecuta el programa. Esto hace que tu código sea portable.

(agregar-al-final-de-un-archivo-append)=
### Agregar al Final de un Archivo (Append)

A veces no querés sobrescribir el archivo, sino **agregar** contenido al final. Esto es común para archivos de log o registros históricos:

```{code} java
:caption: Agregar contenido a un archivo existente

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.util.Collections;

public static void agregarLinea(String rutaArchivo, String nuevaLinea) {
    Path archivo = Path.of(rutaArchivo);
    
    try {
        // Collections.singletonList() crea una lista con un único elemento
        Files.write(archivo, 
                   Collections.singletonList(nuevaLinea),
                   StandardOpenOption.APPEND,   // Agregar al final (no sobrescribir)
                   StandardOpenOption.CREATE);  // Crear el archivo si no existe
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

// Uso:
agregarLinea("log.txt", "2024-03-15 10:30:00 - Usuario ingresó al sistema");
agregarLinea("log.txt", "2024-03-15 10:31:15 - Usuario consultó saldo");
// Cada llamada agrega una línea al final del archivo
```

**Las opciones de `StandardOpenOption`:**

| Opción | Significado |
|:---|:---|
| `CREATE` | Crea el archivo si no existe |
| `APPEND` | Agrega al final del archivo existente |
| `TRUNCATE_EXISTING` | Borra el contenido existente (por defecto con `write`) |
| `CREATE_NEW` | Crea el archivo, falla si ya existe |

**Comparación con C:**

```c
// En C, "a" significa append (agregar al final)
FILE *archivo = fopen("log.txt", "a");
fprintf(archivo, "Nueva línea\n");
fclose(archivo);
```

(operaciones-con-archivos-y-directorios)=
## Operaciones con Archivos y Directorios

Además de leer y escribir contenido, a menudo necesitás manipular los archivos mismos: crearlos, copiarlos, moverlos o eliminarlos. La clase `Files` proporciona métodos para todas estas operaciones.

(crear-directorios-y-archivos-vacios)=
### Crear Directorios y Archivos Vacíos

```{code} java
:caption: Crear directorios y archivos

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

// Crear un directorio simple
Path nuevoDir = Path.of("nuevo_directorio");
Files.createDirectory(nuevoDir);  // Falla si el padre no existe

// Crear directorios anidados (crea todos los padres necesarios)
// Equivalente a "mkdir -p" en Linux
Path directorios = Path.of("a/b/c/d");
Files.createDirectories(directorios);  // Crea a, luego a/b, luego a/b/c, luego a/b/c/d

// Crear archivo vacío
Path nuevoArchivo = Path.of("nuevo.txt");
Files.createFile(nuevoArchivo);  // Falla si ya existe
```

**Diferencia entre `createDirectory` y `createDirectories`:**

- `createDirectory()`: Crea un único directorio. Si los directorios padres no existen, lanza excepción.
- `createDirectories()`: Crea el directorio y todos los padres necesarios. Si ya existe, no hace nada (no falla).

**Comparación con C/Linux:**

| Java | Linux (shell) | Comportamiento |
|:---|:---|:---|
| `Files.createDirectory(path)` | `mkdir directorio` | Crea uno, falla si padre no existe |
| `Files.createDirectories(path)` | `mkdir -p a/b/c/d` | Crea toda la jerarquía |
| `Files.createFile(path)` | `touch archivo` | Crea archivo vacío |

(copiar-y-mover-archivos)=
### Copiar y Mover Archivos

```{code} java
:caption: Copiar y mover archivos

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

// Copiar un archivo
Path origen = Path.of("original.txt");
Path destino = Path.of("copia.txt");
Files.copy(origen, destino);  // Falla si destino ya existe

// Copiar sobrescribiendo si ya existe
Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);

// Mover/Renombrar un archivo
Path viejo = Path.of("viejo_nombre.txt");
Path nuevo = Path.of("nuevo_nombre.txt");
Files.move(viejo, nuevo);  // Renombra el archivo

// Mover a otro directorio
Path archivoEnCarpeta = Path.of("carpeta/archivo.txt");
Files.move(origen, archivoEnCarpeta, StandardCopyOption.REPLACE_EXISTING);
```

**¿`move` es renombrar o mover?**

En realidad, son la misma operación a nivel del sistema de archivos. "Renombrar" es solo "mover" al mismo directorio con diferente nombre. "Mover" es "renombrar" cambiando también el directorio.

(eliminar-archivos-y-directorios)=
### Eliminar Archivos y Directorios

```{code} java
:caption: Eliminar archivos y directorios

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

// Eliminar un archivo
Path aEliminar = Path.of("temporal.txt");

// delete() lanza excepción si el archivo no existe
Files.delete(aEliminar);

// deleteIfExists() no lanza excepción si no existe
// Retorna true si eliminó algo, false si no existía
boolean eliminado = Files.deleteIfExists(aEliminar);
if (eliminado) {
    System.out.println("Archivo eliminado");
} else {
    System.out.println("El archivo no existía");
}
```

:::{warning} Eliminar directorios
`Files.delete()` **solo puede eliminar directorios vacíos**. Si el directorio contiene archivos o subdirectorios, lanza `DirectoryNotEmptyException`. Para eliminar un directorio con contenido, primero tenés que eliminar todo su contenido recursivamente.
:::

**Comparación con C/Linux:**

| Java | Linux (shell) | Notas |
|:---|:---|:---|
| `Files.delete(path)` | `rm archivo` | Falla si no existe |
| `Files.deleteIfExists(path)` | `rm -f archivo` | No falla si no existe |
| No hay equivalente directo | `rm -r directorio` | Hay que hacerlo manualmente |

(manejo-de-excepciones-en-i-o)=
## Manejo de Excepciones en I/O

Las operaciones con archivos pueden fallar por muchas razones que están fuera del control de tu programa:

- El archivo no existe
- No tenés permisos para leerlo o escribirlo
- El disco está lleno
- Otro programa tiene el archivo bloqueado
- El dispositivo de almacenamiento fue removido

Por eso, casi todas las operaciones de I/O en Java pueden lanzar excepciones. Entender estas excepciones te permite escribir programas robustos que manejan errores de manera elegante.

(jerarquia-de-excepciones-de-i-o)=
### Jerarquía de Excepciones de I/O

```
IOException (excepción base para errores de entrada/salida)
├── FileNotFoundException        // El archivo no existe (API clásica)
├── NoSuchFileException         // El archivo no existe (NIO.2)
├── AccessDeniedException       // Sin permisos de lectura/escritura
├── FileAlreadyExistsException  // El archivo ya existe (al crear)
├── DirectoryNotEmptyException  // Directorio no vacío (al eliminar)
├── NotDirectoryException       // Se esperaba un directorio, hay un archivo
├── EOFException                // Fin de archivo inesperado
└── ... otras
```

**¿Por qué hay `FileNotFoundException` y `NoSuchFileException`?**

`FileNotFoundException` es de la API clásica (`java.io`), mientras que `NoSuchFileException` es de la API moderna (`java.nio.file`). Son conceptualmente lo mismo, pero de diferentes "generaciones" de Java. Si usás NIO.2 (como venimos haciendo), vas a encontrar `NoSuchFileException`.

(captura-especifica-de-excepciones)=
### Captura Específica de Excepciones

Capturar excepciones específicas te permite dar mensajes de error más útiles y tomar acciones diferentes según el tipo de problema:

```{code} java
:caption: Captura jerarquizada de excepciones

import java.nio.file.*;
import java.io.IOException;
import java.util.List;

public static void leerArchivoSeguro(String ruta) {
    Path archivo = Path.of(ruta);
    
    try {
        List<String> lineas = Files.readAllLines(archivo);
        for (String linea : lineas) {
            System.out.println(linea);
        }
        
    } catch (NoSuchFileException e) {
        // Más específica: el archivo no existe
        // e.getFile() devuelve la ruta del archivo que no se encontró
        System.out.println("Error: El archivo no existe: " + e.getFile());
        System.out.println("Verificá que la ruta sea correcta.");
        
    } catch (AccessDeniedException e) {
        // Específica: sin permisos de lectura
        System.out.println("Error: No tenés permisos para leer: " + e.getFile());
        System.out.println("Intentá ejecutar el programa con otros permisos.");
        
    } catch (IOException e) {
        // General: cualquier otro error de I/O no capturado arriba
        System.out.println("Error de I/O inesperado: " + e.getMessage());
    }
}
```

:::{important} Orden de los catch
Siempre colocá las excepciones más específicas **antes** que las más generales. `NoSuchFileException` y `AccessDeniedException` son subtipos de `IOException`. Si ponés `IOException` primero, las específicas nunca se alcanzarán y el compilador dará error.
:::

**Comparación con C:**

En C, cuando `fopen()` fallaba, retornaba `NULL` y tenías que revisar `errno` para saber qué pasó:

```c
// En C
FILE *archivo = fopen("datos.txt", "r");
if (archivo == NULL) {
    if (errno == ENOENT) {
        printf("Archivo no existe\n");
    } else if (errno == EACCES) {
        printf("Sin permisos\n");
    } else {
        printf("Error desconocido\n");
    }
}
```

En Java, las excepciones hacen que el código de manejo de errores esté separado del código normal, y cada tipo de error tiene su propia excepción con información relevante.

(excepciones-multiples-con-pipe)=
### Excepciones Múltiples con Pipe

Si querés manejar varias excepciones de la misma manera (con el mismo código):

```{code} java
:caption: Captura múltiple con |

try {
    // operaciones de archivo
    Files.copy(origen, destino);
} catch (NoSuchFileException | AccessDeniedException e) {
    // Mismo manejo para ambas: problema de acceso al archivo
    System.out.println("No se puede acceder al archivo: " + e.getMessage());
} catch (IOException e) {
    // Otros errores de I/O
    System.out.println("Error de I/O: " + e.getMessage());
}
```

(gestion-de-recursos-el-problema-de-cerrar-archivos)=
## Gestión de Recursos: El Problema de Cerrar Archivos

Cuando abrís un archivo, el sistema operativo reserva recursos (un "descriptor de archivo") para esa conexión. Estos recursos son limitados: si abrís muchos archivos sin cerrarlos, eventualmente el sistema operativo te rechazará nuevas aperturas.

En C, esto se manejaba con `fopen()` y `fclose()`:

```c
// En C
FILE *archivo = fopen("datos.txt", "r");
// usar el archivo...
fclose(archivo);  // MUY IMPORTANTE: liberar el recurso
```

El problema es que si algo falla entre `fopen` y `fclose`, el archivo queda abierto. En Java, este problema se manifiesta de manera similar.

(el-problema-que-pasa-si-hay-una-excepcion)=
### El Problema: ¿Qué pasa si hay una excepción?

```{code} java
:caption: ❌ Incorrecto: el archivo puede quedar abierto

BufferedReader reader = new BufferedReader(new FileReader("datos.txt"));
String linea = reader.readLine();
// Si readLine() lanza una excepción, nunca llegamos a close()
System.out.println(linea);
reader.close();  // Esta línea no se ejecuta si hubo excepción
```

Si `readLine()` (o cualquier operación intermedia) lanza una excepción, el flujo del programa salta al manejo de la excepción y `reader.close()` nunca se ejecuta. El archivo queda abierto, "fugando" recursos del sistema.

(solucion-clasica-try-finally)=
### Solución Clásica: try-finally

El bloque `finally` se ejecuta **siempre**, haya o no excepción. Esto garantiza que el código de limpieza se ejecute:

```{code} java
:caption: try-finally para cerrar recursos

import java.io.*;

public static void leerConFinally(String ruta) {
    BufferedReader reader = null;  // Declarar fuera del try para acceder en finally
    
    try {
        reader = new BufferedReader(new FileReader(ruta));
        String linea;
        while ((linea = reader.readLine()) != null) {
            System.out.println(linea);
        }
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    } finally {
        // Este bloque se ejecuta SIEMPRE, haya o no excepción
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                // El close() también puede lanzar excepción
                System.out.println("Error al cerrar: " + e.getMessage());
            }
        }
    }
}
```

**Análisis del código:**

1. Se declara `reader` fuera del `try` (inicializado en `null`) para que sea accesible en `finally`
2. Dentro del `try`, se crea el reader y se lee el archivo
3. Si ocurre cualquier excepción, el `catch` la maneja
4. Independientemente de si hubo excepción o no, el `finally` se ejecuta
5. En el `finally`, verificamos si `reader` no es `null` (podría serlo si la apertura falló)
6. Llamamos a `close()`, pero ¡ojo! `close()` también puede lanzar excepción, así que necesita su propio try-catch

**Problemas de este enfoque:**
- Código muy verboso (muchas líneas para algo simple)
- Propenso a errores (fácil olvidar el chequeo de null, el try-catch interno)
- Difícil de leer y mantener

(solucion-moderna-try-with-resources-java-7)=
### Solución Moderna: try-with-resources (Java 7+)

Java 7 introdujo una sintaxis especial que simplifica enormemente la gestión de recursos:

```{code} java
:caption: try-with-resources (la forma recomendada)

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
    // reader.close() se llama AUTOMÁTICAMENTE al salir del try
    // Incluso si ocurre una excepción
}
```

**¿Cómo funciona?**

1. El recurso se declara **dentro de los paréntesis** del `try`: `try (BufferedReader reader = ...)`
2. Java automáticamente llama a `close()` cuando el bloque `try` termina
3. El cierre ocurre aunque haya una excepción
4. No necesitás escribir el bloque `finally` manualmente

Es exactamente equivalente al try-finally anterior, pero mucho más corto y legible.

(multiples-recursos-en-try-with-resources)=
### Múltiples Recursos en try-with-resources

Podés declarar varios recursos separados por punto y coma:

```{code} java
:caption: Múltiples recursos que se cierran automáticamente

import java.io.*;
import java.nio.file.*;

public static void copiarArchivo(String origen, String destino) {
    Path archivoOrigen = Path.of(origen);
    Path archivoDestino = Path.of(destino);
    
    // Ambos recursos se declaran en el try
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
    // Ambos se cierran automáticamente, en orden INVERSO:
    // Primero writer, luego reader
}
```

:::{important} Orden de Cierre
Los recursos se cierran en **orden inverso** al que fueron declarados. En el ejemplo, primero se cierra `writer`, luego `reader`. Esto tiene sentido: si el writer depende del reader de alguna manera, querés cerrar el dependiente primero.
:::

(que-recursos-se-pueden-usar-con-try-with-resources)=
### ¿Qué recursos se pueden usar con try-with-resources?

Para que un tipo funcione con try-with-resources, debe "prometer" que tiene un método `close()`. En términos técnicos, debe implementar la interfaz `AutoCloseable`. 

No te preocupes por los detalles de interfaces ahora; lo importante es saber que todas las clases de I/O de Java funcionan con try-with-resources:

- `BufferedReader`, `BufferedWriter`
- `FileReader`, `FileWriter`
- `FileInputStream`, `FileOutputStream`
- `Scanner`
- `PrintWriter`
- Y muchas más...

```{code} java
:caption: Scanner también funciona con try-with-resources

// Scanner implementa AutoCloseable
try (Scanner scanner = new Scanner(System.in)) {
    System.out.print("Ingrese un número: ");
    int numero = scanner.nextInt();
    System.out.println("El doble es: " + (numero * 2));
}
// scanner.close() se llama automáticamente
```

:::{tip} Regla simple
Siempre que abras un recurso de I/O (archivo, conexión, etc.), usá try-with-resources. Es más seguro, más corto, y previene fugas de recursos.
:::

(codificacion-de-caracteres-charset)=
## Codificación de Caracteres (Charset)

Cuando guardás texto en un archivo, las letras se convierten en bytes. Cuando leés ese archivo, los bytes se convierten de vuelta en letras. La **codificación** define las reglas de esta conversión.

(por-que-importa-la-codificacion)=
### ¿Por qué importa la codificación?

Pensá en la letra **ñ**. En tu pantalla, la ves como un único carácter. Pero en el archivo, puede representarse de diferentes maneras según la codificación:

- En **ISO-8859-1** (Latin-1): un byte con valor 241
- En **UTF-8**: dos bytes con valores 195 y 177
- En **UTF-16**: dos bytes con valores 0 y 241

Si escribís un archivo con una codificación y lo leés con otra, los caracteres especiales (ñ, á, é, ü, etc.) aparecerán mal: verás símbolos extraños como "Ã±" en lugar de "ñ".

**Ejemplo del problema:**

```
// Archivo guardado en UTF-8: "niño"
// Bytes: 6E 69 C3 B1 6F (n=6E, i=69, ñ=C3B1, o=6F)

// Si lo leés asumiendo ISO-8859-1:
// Interpreta C3 como "Ã" y B1 como "±"
// Resultado: "niÃ±o"  ← ¡Incorrecto!
```

(la-solucion-especificar-siempre-utf-8)=
### La solución: Especificar siempre UTF-8

UTF-8 es el estándar moderno. Puede representar cualquier carácter de cualquier idioma, y es compatible con ASCII (los caracteres ingleses básicos usan los mismos códigos).

```{code} java
:caption: Especificar codificación UTF-8 explícitamente

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.List;

// Lectura con codificación explícita
List<String> lineas = Files.readAllLines(
    Path.of("datos.txt"), 
    StandardCharsets.UTF_8
);

// Escritura con codificación explícita
Files.write(
    Path.of("salida.txt"),
    lineas,
    StandardCharsets.UTF_8
);

// BufferedReader con codificación explícita
try (BufferedReader reader = Files.newBufferedReader(
        Path.of("datos.txt"), 
        StandardCharsets.UTF_8)) {
    // leer...
}
```

**¿Qué pasa si no especificás la codificación?**

Java usa la codificación por defecto del sistema operativo, que varía:
- Linux: generalmente UTF-8
- macOS: generalmente UTF-8
- Windows: a menudo Windows-1252 (similar a Latin-1)

Esto significa que un programa puede funcionar bien en tu computadora pero fallar en otra. Por eso, siempre es mejor especificar la codificación explícitamente.

(codificaciones-comunes)=
### Codificaciones Comunes

| Codificación | Descripción | Cuándo usarla |
|:---|:---|:---|
| `UTF_8` | Unicode, tamaño variable (1-4 bytes por carácter) | **Siempre que sea posible** - es el estándar |
| `ISO_8859_1` | Latin-1, 1 byte por carácter | Archivos viejos en español/portugués |
| `US_ASCII` | ASCII básico (7 bits) | Solo para texto en inglés sin acentos |
| `UTF_16` | Unicode, 2 bytes por carácter | Raramente usado en archivos de texto |

:::{tip} Regla simple: usá UTF-8
A menos que tengas una razón específica (como leer un archivo viejo que sabés que está en otra codificación), siempre usá UTF-8. Es el estándar de internet y soporta todos los caracteres de todos los idiomas.
:::

(eficiencia-la-importancia-del-buffering)=
## Eficiencia: La Importancia del Buffering

Ya mencionamos el buffering antes, pero vale la pena profundizar porque tiene un impacto enorme en el rendimiento.

(el-costo-de-acceder-al-disco)=
### El costo de acceder al disco

Cada vez que tu programa lee o escribe datos del disco, ocurre lo siguiente:

1. Tu programa pide datos al sistema operativo
2. El sistema operativo verifica permisos y ubicación del archivo
3. El sistema operativo envía una solicitud al disco
4. El disco (especialmente si es mecánico) mueve el cabezal de lectura
5. El disco lee los datos
6. Los datos viajan de vuelta a tu programa

Este proceso toma **milisegundos**, que parecen poco, pero son eternidades para un procesador que hace billones de operaciones por segundo.

(sin-buffer-vs-con-buffer)=
### Sin buffer vs. Con buffer

```{code} java
:caption: Impacto del buffering en el rendimiento

import java.io.*;

// ❌ Sin buffer: una llamada al sistema por cada byte leído
// Si el archivo tiene 1 millón de bytes = 1 millón de llamadas al sistema
InputStream sinBuffer = new FileInputStream("datos.bin");

// ✅ Con buffer: lee bloques de ~8KB a la vez
// Si el archivo tiene 1 millón de bytes ≈ 125 llamadas al sistema
InputStream conBuffer = new BufferedInputStream(
    new FileInputStream("datos.bin")
);

// NIO.2: Files.newBufferedReader ya incluye buffering automáticamente
BufferedReader reader = Files.newBufferedReader(Path.of("datos.txt"));
```

**Analogía:** Imaginá que tenés que traer agua de un pozo a 100 metros de tu casa.

- **Sin buffer:** Vas al pozo, llenás un vaso, volvés, lo vacías. Repetís 1000 veces.
- **Con buffer:** Vas al pozo, llenás un balde de 50 vasos, volvés, servís 50 vasos. Repetís 20 veces.

El tiempo de caminar al pozo es el cuello de botella, igual que el tiempo de acceso al disco.

(impacto-real-en-tiempos)=
### Impacto real en tiempos

Para darte una idea concreta, leer un archivo de 100MB carácter por carácter:

| Método | Tiempo aproximado |
|:---|:---|
| Sin buffer (byte por byte) | 30-60 segundos |
| Con buffer (bloques de 8KB) | 0.1-0.5 segundos |

¡La diferencia puede ser de 100x o más!

(cuando-necesitas-pensar-en-buffering)=
### ¿Cuándo necesitás pensar en buffering?

- Si usás `Files.newBufferedReader()` o `Files.newBufferedWriter()`: **ya tenés buffer**
- Si usás `Files.readAllLines()` o `Files.write()`: **ya tenés buffer internamente**
- Si creás `FileReader` o `FileWriter` directamente: **envolvelo en BufferedReader/Writer**

```{code} java
:caption: Asegurar que siempre haya buffer

// Si por alguna razón usás la API clásica:
// NO hagas esto:
FileReader sinBuffer = new FileReader("datos.txt");

// Hacé esto:
BufferedReader conBuffer = new BufferedReader(new FileReader("datos.txt"));

// Pero mejor aún, usá NIO.2 que ya incluye buffer:
BufferedReader mejor = Files.newBufferedReader(Path.of("datos.txt"));
```

(ejemplo-completo-procesar-archivo-csv)=
## Ejemplo Completo: Procesar Archivo CSV

Los archivos CSV (Comma-Separated Values, valores separados por comas) son una forma común de almacenar datos tabulares. Cada línea representa una fila, y los valores de cada columna están separados por comas.

Ejemplo de archivo CSV (`alumnos.csv`):
```
nombre,edad,promedio
Juan,20,7.5
María,22,8.9
Carlos,21,6.8
```

Veamos un ejemplo completo que lee y escribe archivos CSV:

```{code} java
:caption: Leer y procesar archivo CSV

import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProcesadorCSV {
    
    /**
     * Lee un archivo CSV y devuelve los datos como lista de arreglos de String.
     * Cada elemento de la lista es una fila del CSV.
     * Cada arreglo contiene los valores de esa fila (un elemento por columna).
     * 
     * @param rutaArchivo Ruta al archivo CSV
     * @return Lista con los datos (sin el encabezado)
     */
    public static List<String[]> leerCSV(String rutaArchivo) {
        List<String[]> datos = new ArrayList<>();
        Path archivo = Path.of(rutaArchivo);
        
        try (BufferedReader reader = Files.newBufferedReader(archivo)) {
            String linea;
            
            // Saltar la primera línea (encabezado)
            // Si no querés saltarla, comentá esta línea
            reader.readLine();
            
            // Leer el resto de líneas
            while ((linea = reader.readLine()) != null) {
                // split(",") divide el String por comas
                // "Juan,20,7.5" se convierte en ["Juan", "20", "7.5"]
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
    
    /**
     * Escribe datos en formato CSV.
     * 
     * @param rutaArchivo Ruta donde guardar el archivo
     * @param encabezados Nombres de las columnas
     * @param datos Lista de filas (cada fila es un arreglo de valores)
     */
    public static void escribirCSV(String rutaArchivo, 
                                   String[] encabezados, 
                                   List<String[]> datos) {
        Path archivo = Path.of(rutaArchivo);
        
        try (BufferedWriter writer = Files.newBufferedWriter(archivo)) {
            // Escribir encabezados
            // String.join(",", encabezados) une los elementos con comas
            // ["nombre", "edad"] se convierte en "nombre,edad"
            writer.write(String.join(",", encabezados));
            writer.newLine();
            
            // Escribir cada fila de datos
            for (String[] fila : datos) {
                writer.write(String.join(",", fila));
                writer.newLine();
            }
            
            System.out.println("CSV escrito: " + datos.size() + " filas");
            
        } catch (IOException e) {
            System.out.println("Error al escribir CSV: " + e.getMessage());
        }
    }
    
    // Ejemplo de uso
    public static void main(String[] args) {
        // Leer un CSV existente
        List<String[]> alumnos = leerCSV("alumnos.csv");
        
        // Procesar los datos
        for (String[] alumno : alumnos) {
            String nombre = alumno[0];
            String edad = alumno[1];
            String promedio = alumno[2];
            System.out.println(nombre + " tiene " + edad + " años");
        }
        
        // Crear datos nuevos
        List<String[]> nuevos = new ArrayList<>();
        nuevos.add(new String[]{"Ana", "19", "9.2"});
        nuevos.add(new String[]{"Pedro", "23", "7.0"});
        
        // Guardar en otro archivo
        String[] columnas = {"nombre", "edad", "promedio"};
        escribirCSV("nuevos_alumnos.csv", columnas, nuevos);
    }
}
```

:::{note} Limitaciones del ejemplo
Este ejemplo asume que los datos no contienen comas. Si un valor puede contener comas (por ejemplo, "Pérez, Juan"), necesitarías un parser CSV más robusto que maneje valores entre comillas. Para casos complejos, considerá usar una biblioteca como OpenCSV.
:::

(resumen-mejores-practicas)=
## Resumen: Mejores Prácticas

Después de todo lo visto, estas son las reglas que deberías seguir:

1. **Usá NIO.2** (`java.nio.file`) en lugar de la API antigua (`java.io.File`). Es más moderna, más legible, y tiene mejor manejo de errores.

2. **Siempre usá try-with-resources** para garantizar que los archivos se cierren. Nunca dependas de acordarte de llamar a `close()` manualmente.

3. **Capturá excepciones específicas** antes que las generales. Esto permite dar mejores mensajes de error y tomar acciones diferentes según el problema.

4. **Especificá la codificación UTF-8** explícitamente, especialmente si tu texto puede contener caracteres no ingleses (acentos, ñ, etc.).

5. **Usá BufferedReader/Writer** para mejor rendimiento. Si usás `Files.newBufferedReader()`, el buffer ya está incluido.

6. **Verificá existencia del archivo** antes de operar si es necesario. Aunque las excepciones te avisarán si falla, a veces es mejor verificar antes y dar un mensaje más claro.

7. **No cargues archivos enormes** en memoria completa. Si el archivo puede ser grande, procesalo línea por línea en lugar de usar `readAllLines()`.

(comparacion-final-con-c)=
### Comparación final con C

| Operación | C | Java (NIO.2) |
|:---|:---|:---|
| Abrir archivo | `FILE *f = fopen("a.txt", "r");` | `BufferedReader r = Files.newBufferedReader(Path.of("a.txt"));` |
| Leer línea | `fgets(linea, 100, f);` | `String linea = reader.readLine();` |
| Escribir línea | `fprintf(f, "texto\n");` | `writer.write("texto"); writer.newLine();` |
| Cerrar archivo | `fclose(f);` | Automático con try-with-resources |
| Verificar error | `if (f == NULL) { ... errno ... }` | `catch (IOException e) { ... }` |
| Verificar existencia | `access(ruta, F_OK)` | `Files.exists(path)` |

(ejercicios-de-aplicacion)=
## Ejercicios de Aplicación

```{exercise}
:label: ej-contar-lineas
Escribí un método `contarLineas(String ruta)` que cuente y retorne la cantidad de líneas de un archivo de texto. El método debe manejar correctamente las excepciones y cerrar el recurso automáticamente. Si el archivo no existe, debe retornar -1 y mostrar un mensaje de error.
```

````{solution} ej-contar-lineas
:class: dropdown
```java
import java.nio.file.*;
import java.io.*;

public static int contarLineas(String ruta) {
    Path archivo = Path.of(ruta);
    int contador = 0;
    
    try (BufferedReader reader = Files.newBufferedReader(archivo)) {
        // Cada llamada a readLine() consume una línea
        // Cuando retorna null, ya no hay más líneas
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

// Uso:
int lineas = contarLineas("poema.txt");
if (lineas >= 0) {
    System.out.println("El archivo tiene " + lineas + " líneas");
}
```

**Explicación:** Usamos try-with-resources para que el BufferedReader se cierre automáticamente. El lazo `while` lee líneas sin guardarlas (no las necesitamos, solo contamos). Manejamos `NoSuchFileException` específicamente para dar un mensaje claro.
````

```{exercise}
:label: ej-io-buffer
Explicá con tus palabras por qué leer un archivo de 1GB carácter por carácter con `FileReader` directamente sería mucho más lento que usando `BufferedReader`. ¿Aproximadamente cuántas llamadas al sistema operativo haría cada enfoque?
```

```{solution} ej-io-buffer
:class: dropdown

**Sin buffer (`FileReader` directo):**
- Un archivo de 1GB tiene aproximadamente 1.000.000.000 bytes
- Cada llamada a `read()` hace una llamada al sistema operativo para leer un byte
- Total: ~1.000.000.000 llamadas al sistema

**Con buffer (`BufferedReader`):**
- `BufferedReader` usa un buffer interno de aproximadamente 8KB (8.192 bytes)
- Cada vez que se vacía el buffer, hace UNA llamada al sistema para llenarlo con 8KB
- Total: 1.000.000.000 / 8.192 ≈ 122.000 llamadas al sistema

**Diferencia:** El enfoque sin buffer hace **8.000 veces más llamadas al sistema**. Como cada llamada tiene un costo fijo (cambio de contexto, verificaciones del SO, acceso al disco), la versión sin buffer puede ser entre 10x y 100x más lenta en la práctica.

**Analogía:** Es como ir al supermercado a buscar un producto, volver a casa, y repetir 1000 veces, en lugar de hacer una lista y traer todo en un viaje.
```

```{exercise}
:label: ej-copiar-filtrado
Escribí un método `copiarLineasConPalabra(String origen, String destino, String palabra)` que copie de un archivo de texto a otro, pero solo las líneas que contengan una palabra específica. Mostrá cuántas líneas se copiaron al finalizar.
```

````{solution} ej-copiar-filtrado
:class: dropdown
```java
import java.nio.file.*;
import java.io.*;

public static void copiarLineasConPalabra(String origen, 
                                          String destino, 
                                          String palabra) {
    Path archivoOrigen = Path.of(origen);
    Path archivoDestino = Path.of(destino);
    
    // Abrimos ambos archivos en el mismo try-with-resources
    try (BufferedReader reader = Files.newBufferedReader(archivoOrigen);
         BufferedWriter writer = Files.newBufferedWriter(archivoDestino)) {
        
        String linea;
        int copiadas = 0;
        
        while ((linea = reader.readLine()) != null) {
            // contains() verifica si la línea contiene la palabra
            if (linea.contains(palabra)) {
                writer.write(linea);
                writer.newLine();
                copiadas = copiadas + 1;
            }
        }
        
        System.out.println("Se copiaron " + copiadas + " líneas que contienen '" + palabra + "'");
        
    } catch (NoSuchFileException e) {
        System.out.println("Archivo no encontrado: " + e.getFile());
    } catch (IOException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

// Uso:
copiarLineasConPalabra("libro.txt", "capitulo1.txt", "Capítulo 1");
// Copia solo las líneas que mencionan "Capítulo 1"
```

**Explicación:** Abrimos ambos archivos simultáneamente. Por cada línea del origen, verificamos si contiene la palabra buscada usando `contains()`. Si la contiene, la escribimos en el destino. Ambos archivos se cierran automáticamente al salir del try.
````

````{exercise}
:label: ej-try-resources
¿Por qué el siguiente código es problemático? Explicá qué puede salir mal y reescribilo usando try-with-resources.

```java
BufferedReader reader = new BufferedReader(new FileReader("datos.txt"));
String primera = reader.readLine();
String segunda = reader.readLine();
reader.close();
System.out.println(primera + " - " + segunda);
```
````

````{solution} ej-try-resources
:class: dropdown
**Problemas del código original:**

1. **Fuga de recursos si hay excepción:** Si `readLine()` lanza una excepción (por ejemplo, si hay un error de lectura), el programa salta directamente al manejo de la excepción y `reader.close()` nunca se ejecuta. El archivo queda abierto.

2. **Si `close()` falla:** El cierre del archivo también puede fallar, y no hay manejo de esa posible excepción.

3. **El código de limpieza está mezclado con la lógica:** Hace que sea más fácil olvidar el cierre o complicar el mantenimiento.

**Corrección con try-with-resources:**
```java
try (BufferedReader reader = new BufferedReader(new FileReader("datos.txt"))) {
    String primera = reader.readLine();
    String segunda = reader.readLine();
    System.out.println(primera + " - " + segunda);
} catch (FileNotFoundException e) {
    System.out.println("El archivo no existe: " + e.getMessage());
} catch (IOException e) {
    System.out.println("Error al leer: " + e.getMessage());
}
// El reader se cierra automáticamente, INCLUSO si hay excepción
```

**Mejoras:**
- El `reader` se cierra automáticamente al salir del `try`, sin importar cómo se salga (normalmente o por excepción)
- Las excepciones se manejan explícitamente con mensajes claros
- El código es más corto y más fácil de entender
````

```{exercise}
:label: ej-agregar-log
Escribí un método `agregarAlLog(String mensaje)` que agregue una línea con fecha y hora al final de un archivo "aplicacion.log". Si el archivo no existe, debe crearlo. El formato de cada línea debe ser: `[YYYY-MM-DD HH:MM:SS] mensaje`
```

````{solution} ej-agregar-log
:class: dropdown
```java
import java.nio.file.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public static void agregarAlLog(String mensaje) {
    Path archivoLog = Path.of("aplicacion.log");
    
    // Obtener fecha y hora actual
    LocalDateTime ahora = LocalDateTime.now();
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String fechaHora = ahora.format(formato);
    
    // Formatear la línea completa
    String lineaLog = "[" + fechaHora + "] " + mensaje;
    
    try {
        // APPEND: agregar al final (no sobrescribir)
        // CREATE: crear el archivo si no existe
        Files.write(archivoLog, 
                   Collections.singletonList(lineaLog),
                   StandardOpenOption.APPEND,
                   StandardOpenOption.CREATE);
    } catch (IOException e) {
        System.err.println("Error escribiendo al log: " + e.getMessage());
    }
}

// Uso:
agregarAlLog("Aplicación iniciada");
agregarAlLog("Usuario 'juan' ingresó al sistema");
agregarAlLog("Error: conexión perdida con servidor");

// Resultado en aplicacion.log:
// [2024-03-15 10:30:00] Aplicación iniciada
// [2024-03-15 10:30:01] Usuario 'juan' ingresó al sistema
// [2024-03-15 10:30:05] Error: conexión perdida con servidor
```

**Explicación:** Usamos `LocalDateTime.now()` para obtener la fecha y hora actual, y `DateTimeFormatter` para darle el formato deseado. Las opciones `APPEND` y `CREATE` garantizan que el archivo se cree si no existe, y que cada nueva línea se agregue al final sin borrar el contenido anterior.
````

(testing-de-archivos-con-junit)=
## Testing de Archivos con JUnit

Cuando escribís tests para métodos que trabajan con archivos, surge un problema: ¿dónde ponés los archivos de prueba? ¿Qué pasa si un test falla y deja basura? ¿Cómo evitás que un test afecte a otro?

La solución es usar **archivos temporales** que se crean antes de cada test y se eliminan después.

(el-problema)=
### El Problema

Imaginá que tenés un método `contarLineas(String ruta)` y querés testearlo. Podrías:

1. **Usar un archivo "real"** en tu proyecto → Malo: si cambiás el archivo, rompés el test
2. **Crear el archivo en el test** → Mejor, pero: ¿quién lo borra? ¿Y si el test falla antes de borrarlo?

(la-solucion-tempdir-de-junit-5)=
### La Solución: `@TempDir` de JUnit 5

JUnit 5 ofrece la anotación `@TempDir` que crea un directorio temporal automáticamente. JUnit garantiza:
- El directorio existe antes de cada test
- El directorio (y todo su contenido) se elimina después de cada test
- Incluso si el test falla con una excepción

```{code} java
:caption: Uso básico de `@TempDir`

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LectorArchivosTest {
    
    // JUnit crea este directorio automáticamente antes de cada test
    // y lo elimina automáticamente después
    @TempDir
    Path directorioTemporal;
    
    @Test
    void leerArchivo_conContenido_retornaLineas() throws IOException {
        // Arrange: crear un archivo dentro del directorio temporal
        Path archivo = directorioTemporal.resolve("datos.txt");
        Files.write(archivo, List.of("línea 1", "línea 2", "línea 3"));
        
        // Act: leer el archivo
        List<String> resultado = Files.readAllLines(archivo);
        
        // Assert: verificar
        assertEquals(3, resultado.size());
        assertEquals("línea 1", resultado.get(0));
    }
    
    @Test
    void leerArchivo_vacio_retornaListaVacia() throws IOException {
        // Crear archivo vacío
        Path archivo = directorioTemporal.resolve("vacio.txt");
        Files.createFile(archivo);
        
        List<String> resultado = Files.readAllLines(archivo);
        
        assertTrue(resultado.isEmpty());
    }
}
```

**¿Qué es `directorioTemporal.resolve("datos.txt")`?**

`resolve()` combina el directorio con un nombre de archivo para crear la ruta completa. Si `directorioTemporal` es `/tmp/junit123456/`, entonces `resolve("datos.txt")` da `/tmp/junit123456/datos.txt`.

(testing-de-excepciones)=
### Testing de Excepciones

También podés verificar que tu código lance las excepciones correctas:

```{code} java
:caption: Verificar que se lanzan excepciones

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class ManejadorArchivosTest {
    
    @TempDir
    Path directorioTemporal;
    
    @Test
    void leerArchivo_noExiste_lanzaNoSuchFileException() {
        // El archivo no existe - nunca lo creamos
        Path archivoInexistente = directorioTemporal.resolve("no_existe.txt");
        
        // assertThrows verifica que se lance la excepción esperada
        assertThrows(NoSuchFileException.class, () -> {
            Files.readAllLines(archivoInexistente);
        });
    }
    
    @Test
    void crearArchivo_yaExiste_lanzaFileAlreadyExistsException() throws IOException {
        Path archivo = directorioTemporal.resolve("existente.txt");
        Files.createFile(archivo);  // Crear el archivo primero
        
        // Intentar crear el mismo archivo de nuevo debe fallar
        assertThrows(FileAlreadyExistsException.class, () -> {
            Files.createFile(archivo);
        });
    }
}
```

(patron-arrange-act-assert-con-archivos)=
### Patrón Arrange-Act-Assert con Archivos

```{code} java
:caption: Ejemplo completo de un test de procesamiento de archivos

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ProcesadorCSVTest {
    
    @TempDir
    Path dirTemp;
    
    @Test
    void procesarCSV_datosValidos_calculaPromedioCorrectamente() throws IOException {
        // ARRANGE: Crear el archivo CSV de entrada
        Path entrada = dirTemp.resolve("notas.csv");
        Files.writeString(entrada, 
            "nombre,nota\n" +
            "Juan,8\n" +
            "María,6\n" +
            "Pedro,10\n"
        );
        
        // ACT: Ejecutar el método que queremos testear
        double promedio = calcularPromedioNotas(entrada);
        
        // ASSERT: Verificar el resultado
        assertEquals(8.0, promedio, 0.001);  // 0.001 es la tolerancia para comparar doubles
    }
    
    @Test
    void procesarCSV_archivoVacio_retornaCero() throws IOException {
        Path entrada = dirTemp.resolve("vacio.csv");
        Files.writeString(entrada, "nombre,nota\n");  // Solo encabezado
        
        double promedio = calcularPromedioNotas(entrada);
        
        assertEquals(0.0, promedio, 0.001);
    }
    
    // Método auxiliar (en un test real, este sería el método que estás testeando)
    private double calcularPromedioNotas(Path archivo) throws IOException {
        var lineas = Files.readAllLines(archivo);
        if (lineas.size() <= 1) return 0.0;  // Solo encabezado
        
        double suma = 0;
        for (int i = 1; i < lineas.size(); i++) {  // Saltear encabezado
            String[] campos = lineas.get(i).split(",");
            suma = suma + Double.parseDouble(campos[1]);
        }
        return suma / (lineas.size() - 1);
    }
}
```

:::{tip} Buenas prácticas para tests con archivos
1. **Usá `@TempDir`** para que JUnit maneje la limpieza automáticamente
2. **Creá los archivos dentro del test** con contenido específico para cada caso
3. **No dependas de archivos externos** que puedan cambiar
4. **Cada test debe ser independiente**: no asumas nada sobre el estado del sistema de archivos
:::

(12-referencias-bibliograficas)=
## Referencias Bibliográficas

- **Schildt, H.** (2022). _Java: A Beginner's Guide_ (9na ed.). McGraw Hill. (Capítulo 10: Using I/O).
- **Liang, Y. D.** (2017). _Introduction to Java Programming and Data Structures_ (11va ed.). Pearson.
- **Bloch, J.** (2018). _Effective Java_ (3ra ed.). Addison-Wesley Professional. (Item 9: Prefer try-with-resources to try-finally).
- **Oracle Corporation.** (2023). _Java I/O, NIO, and NIO.2_. [Official Documentation](https://docs.oracle.com/javase/tutorial/essential/io/).

:::seealso
- {ref}`regla-0x3001` - Manejo de excepciones en operaciones de I/O.
- {ref}`regla-0x000D` - Documentación de métodos que lanzan IOException.
:::
