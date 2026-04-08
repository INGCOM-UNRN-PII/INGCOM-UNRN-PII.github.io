package ar.unrn;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Introducción a la Programación Funcional en Java.
 * 
 * A partir de Java 8, el lenguaje incorporó Lambdas y la API de Streams. 
 * Esto nos permite pasar de un estilo imperativo (¿cómo hacerlo?)
 * a uno declarativo (¿qué queremos obtener?).
 * 
 * Conceptos clave:
 * - Lambdas: Funciones anónimas que se pueden pasar como parámetros.
 * - Streams: Una secuencia de elementos que admite operaciones de agregación.
 * - Inmutabilidad: Las operaciones de Streams no modifican la fuente original.
 */
public class ProgramacionFuncional {

    public static void main(String[] args) {
        System.out.println("--- Programación Funcional y Streams ---");

        List<String> nombres = Arrays.asList("Juan", "Ana", "Marcos", "Agustina", "Beto", "Andrés");

        // 1. Enfoque Imperativo (Clásico)
        // Recorremos la lista y filtramos manualmente en un bucle 'for'.
        System.out.println("\n--- Imperativo: Nombres que empiezan con A ---");
        for (String n : nombres) {
            if (n.startsWith("A")) {
                System.out.println(n);
            }
        }

        // 2. Enfoque Declarativo (Moderno con Streams)
        // Usamos filter(), map() y forEach().
        System.out.println("\n--- Streams: Filtrado y Transformación ---");
        nombres.stream()
               .filter(n -> n.startsWith("A"))    // Predicado: Filtra elementos
               .map(String::toUpperCase)          // Transformación: Convierte a mayúsculas
               .forEach(System.out::println);     // Operación Terminal: Imprime

        // 3. Collect: Transformar el Stream de vuelta a una Colección
        // Útil cuando necesitás el resultado en una nueva lista.
        List<String> filtrados = nombres.stream()
                .filter(n -> n.length() > 4)
                .collect(Collectors.toList());

        System.out.println("\nNombres con más de 4 letras: " + filtrados);

        // 4. Lambdas y Variables Locales
        // Una lambda puede acceder a variables del contexto (efectivamente finales).
        String prefijo = "Alumno: ";
        nombres.stream()
                .limit(2)
                .forEach(n -> System.out.println(prefijo + n));

        // 5. Streams Numéricos
        // Permiten realizar operaciones matemáticas eficientes (sum, average, max).
        List<Integer> notas = Arrays.asList(8, 4, 9, 2, 10, 5, 7);
        
        double promedio = notas.stream()
                               .mapToInt(Integer::intValue) // Unboxing de Integer a int
                               .average()
                               .orElse(0.0);

        System.out.println("\nPromedio de notas: " + promedio);

        // Reflexión sobre Memoria:
        // Los Streams son "perezosos" (lazy evaluation). No procesan los datos
        // hasta que no se llama a una operación terminal (como collect o forEach).
        // Esto optimiza el uso de CPU y memoria al evitar pasos intermedios innecesarios.
    }
}
