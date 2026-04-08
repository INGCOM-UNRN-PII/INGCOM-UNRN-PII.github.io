package ar.unrn;

/**
 * Demostración de las características fundamentales del lenguaje Java.
 * 
 * Temas cubiertos:
 * - Estructura obligatoria de clases.
 * - Tipado estático y fuerte.
 * - Métodos estáticos (paradigma procedural inicial).
 * - Diferencias conceptuales con C.
 */
public class CaracteristicasLenguaje {

    /**
     * Todo programa Java debe comenzar en un método main dentro de una clase.
     * @param args argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        System.out.println("--- Características del Lenguaje Java ---");

        // 1. Tipado Estático: El tipo se define en compilación y no cambia.
        int edad = 20;
        // edad = "veinte"; // ERROR DE COMPILACIÓN: No se puede asignar un String a un int.

        // 2. Tipado Fuerte: No hay conversiones mágicas peligrosas.
        double precio = 99.99;
        // int precioEntero = precio; // ERROR DE COMPILACIÓN: Posible pérdida de precisión.
        int precioEntero = (int) precio; // Casting explícito: El programador asume el riesgo.

        System.out.println("Casting de double a int: " + precio + " -> " + precioEntero);

        // 3. Llamada a un método estático.
        // Similar a las funciones en C, pero contenidas en la clase.
        int resultado = sumar(10, 5);
        System.out.println("Resultado de la suma: " + resultado);
    }

    /**
     * Método estático que funciona como una función pura.
     * En Java, los métodos estáticos pertenecen a la clase y no requieren
     * instanciar un objeto para ser utilizados.
     */
    public static int sumar(int a, int b) {
        return a + b;
    }
}
