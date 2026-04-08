package ar.unrn;

import java.util.ArrayList;
import java.util.List;

/**
 * Muestra el uso de colecciones dinámicas en Java.
 * 
 * En C, para una lista dinámica debíamos usar malloc, realloc, free 
 * y manejar punteros con cuidado (o implementar listas enlazadas). 
 * Java provee el Framework de Colecciones (JCF), que encapsula toda 
 * esa complejidad.
 */
public class EjemploColecciones {
    public static void main(String[] args) {
        // List es la interfaz, ArrayList la implementación con un arreglo interno.
        // Los diamantes <> indican el tipo genérico (en este caso, String).
        List<String> lenguajes = new ArrayList<>();

        // El ArrayList crece automáticamente cuando agregamos elementos.
        lenguajes.add("C");
        lenguajes.add("Java");
        lenguajes.add("Python");
        lenguajes.add("Go");

        System.out.println("Lista inicial: " + lenguajes);

        // Iteración moderna for-each (más segura que recorrer con índices).
        System.out.println("--- Recorrido con for-each ---");
        for (String l : lenguajes) {
            System.out.println("Lenguaje: " + l);
        }

        // Remover un elemento por valor o índice. 
        // En C, esto requeriría desplazar el resto de los elementos manualmente.
        lenguajes.remove("Python");
        System.out.println("Después de borrar Python: " + lenguajes);

        // Acceso por índice (similar a los arreglos en C, pero con comprobación de límites).
        System.out.println("Primer lenguaje: " + lenguajes.get(0));

        // Introducción a Streams para operaciones funcionales simples.
        System.out.println("--- Filtrado con Streams ---");
        lenguajes.stream()
                .filter(nombre -> nombre.startsWith("J"))
                .forEach(nombre -> System.out.println("Empieza con J: " + nombre));
    }
}
