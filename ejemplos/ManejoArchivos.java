package ar.unrn;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

/**
 * Ejemplo de manejo de archivos usando la API moderna NIO.2.
 * 
 * Conceptos:
 * - Path para representar rutas.
 * - Files para operaciones atómicas.
 * - Manejo de IOException (Checked Exception).
 * - Especificación explícita de Charset (UTF-8).
 */
public class ManejoArchivos {

    public static void main(String[] args) {
        String nombreArchivo = "ejemplo_archivo.txt";
        
        System.out.println("--- Manejo de Archivos (NIO.2) ---");

        escribirArchivo(nombreArchivo);
        leerArchivo(nombreArchivo);
    }

    public static void escribirArchivo(String ruta) {
        Path path = Path.of(ruta);
        List<String> lineas = Arrays.asList(
            "Primera línea del ejemplo.",
            "Java NIO.2 es potente y seguro.",
            "Soporta caracteres especiales: ñ, á, é."
        );

        try {
            // Escribe todas las líneas de una vez, con codificación explícita
            Files.write(path, lineas, StandardCharsets.UTF_8);
            System.out.println("Archivo escrito exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }
    }

    public static void leerArchivo(String ruta) {
        Path path = Path.of(ruta);

        if (!Files.exists(path)) {
            System.err.println("El archivo no existe.");
            return;
        }

        try {
            // Lee todas las líneas en una lista
            List<String> lineas = Files.readAllLines(path, StandardCharsets.UTF_8);
            
            System.out.println("Contenido del archivo:");
            for (String linea : lineas) {
                System.out.println(" > " + linea);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}
