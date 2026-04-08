package ar.unrn;

/**
 * Ejemplo sobre la definición e invocación de métodos en Java.
 * 
 * Conceptos clave:
 * - Firma del método: nombre + parámetros.
 * - Sobrecarga: mismos nombres, distintas firmas.
 * - Pasaje por valor: Java siempre copia el valor (o la referencia).
 * - Recursión: métodos que se llaman a sí mismos.
 */
public class Metodos {

    public static void main(String[] args) {
        System.out.println("--- Métodos en Java ---");

        // Invocación simple
        int s = sumar(5, 10);
        System.out.println("Suma: " + s);

        // Demostración de sobrecarga
        System.out.println("Suma de 3: " + sumar(5, 10, 15));
        System.out.println("Suma de doubles: " + sumar(2.5, 3.5));

        // Pasaje por valor (primitivos)
        int x = 10;
        intentarModificar(x);
        System.out.println("Valor de x tras intentar modificar: " + x); // Sigue siendo 10

        // Recursión
        System.out.println("Factorial de 5: " + factorial(5));
    }

    // --- Sobrecarga de métodos ---
    
    public static int sumar(int a, int b) {
        return a + b;
    }

    public static int sumar(int a, int b, int c) {
        return a + b + c;
    }

    public static double sumar(double a, double b) {
        return a + b;
    }

    // --- Pasaje de parámetros ---

    /**
     * En Java, los primitivos se pasan por copia de valor.
     * El cambio aquí no afecta a la variable original en el llamador.
     */
    public static void intentarModificar(int n) {
        n = 99;
    }

    // --- Recursión ---

    /**
     * Calcula el factorial de forma recursiva.
     * Requiere un caso base para evitar el StackOverflowError.
     */
    public static long factorial(int n) {
        if (n <= 1) { // Caso base
            return 1;
        }
        return n * factorial(n - 1); // Caso recursivo
    }
}
