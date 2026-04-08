package ar.unrn;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Ejemplo de gestión de errores mediante Excepciones.
 * 
 * Java separa el flujo normal del programa del flujo de error,
 * proporcionando información rica (Stack Trace) y garantizando
 * la limpieza de recursos (finally/try-with-resources).
 */
public class GestionExcepciones {

    public static void main(String[] args) {
        System.out.println("--- Gestión de Excepciones ---");

        try {
            double res = dividir(10, 0);
            System.out.println("Resultado: " + res);
        } catch (ArithmeticException e) {
            System.err.println("Error detectado: " + e.getMessage());
        }

        try {
            validarRango(200);
        } catch (IllegalArgumentException e) {
            System.err.println("Validación fallida: " + e.getMessage());
        }

        demonstrarTryWithResources();
    }

    /**
     * Lanza una excepción Unchecked para indicar un error de lógica o precondición.
     */
    public static double dividir(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("No se puede dividir por cero.");
        }
        return (double) a / b;
    }

    public static void validarRango(int valor) {
        if (valor < 0 || valor > 100) {
            // IllegalArgumentException es ideal para validar argumentos
            throw new IllegalArgumentException("Valor fuera de rango (0-100): " + valor);
        }
    }

    /**
     * try-with-resources garantiza que el recurso se cierre automáticamente
     * al finalizar el bloque, incluso si ocurre una excepción.
     */
    public static void demonstrarTryWithResources() {
        System.out.println("Simulando lectura validada...");
        String entradaSimulada = "no_soy_un_numero";
        
        try (Scanner sc = new Scanner(entradaSimulada)) {
            int n = sc.nextInt();
            System.out.println("Número leído: " + n);
        } catch (InputMismatchException e) {
            System.err.println("El recurso se cerró automáticamente tras el error de formato.");
        }
    }
}
