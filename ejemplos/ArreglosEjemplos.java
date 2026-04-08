package ar.unrn;

import java.util.Arrays;

/**
 * Demostración de uso de Arreglos en Java.
 * 
 * Conceptos:
 * - Atributo .length.
 * - Verificación automática de límites (ArrayIndexOutOfBoundsException).
 * - Clase de utilidad java.util.Arrays.
 * - Patrón de búsqueda con bandera.
 */
public class ArreglosEjemplos {

    public static void main(String[] args) {
        System.out.println("--- Arreglos en Java ---");

        // Declaración e inicialización
        int[] numeros = {5, 2, 8, 1, 9};

        // El arreglo conoce su tamaño
        System.out.println("Longitud: " + numeros.length);

        // Recorrido clásico (permite modificación)
        for (int i = 0; i < numeros.length; i = i + 1) {
            numeros[i] = numeros[i] * 10;
        }

        // Recorrido mejorado (for-each): solo lectura
        System.out.print("Contenido (x10): ");
        for (int n : numeros) {
            System.out.print(n + " ");
        }
        System.out.println();

        // Uso de java.util.Arrays para utilidades comunes
        int[] desordenado = {3, 1, 4, 1, 5};
        Arrays.sort(desordenado);
        System.out.println("Ordenado: " + Arrays.toString(desordenado));

        demonstrarBusqueda();
    }

    /**
     * Ejemplo de búsqueda con bandera booleana, evitando 'break'.
     */
    public static void demonstrarBusqueda() {
        String[] nombres = {"Ana", "Juan", "Pedro", "María"};
        String buscado = "Pedro";
        boolean encontrado = false;
        int i = 0;

        while (i < nombres.length && !encontrado) {
            if (nombres[i].equals(buscado)) {
                encontrado = true;
            } else {
                i = i + 1;
            }
        }

        if (encontrado) {
            System.out.println("Encontrado '" + buscado + "' en índice: " + i);
        }
    }
}
