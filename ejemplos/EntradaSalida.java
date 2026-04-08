package ar.unrn;

import java.util.Scanner;
import java.util.Locale;

/**
 * Demostración de Entrada y Salida de datos en consola.
 * 
 * Temas:
 * - System.out.printf para salidas formateadas.
 * - Scanner para lectura de tokens y líneas.
 * - Manejo del salto de línea residual (\n) en Scanner.
 * - Configuración de Locale para separadores decimales.
 */
public class EntradaSalida {

    public static void main(String[] args) {
        // 1. Salida formateada (printf)
        demonstrarPrintf();

        // 2. Entrada de datos (Scanner)
        // demonstrarScanner(); // Comentado para evitar bloqueo en ejecución automática
        System.out.println("\n(Nota: La demostración interactiva de Scanner se omite para ejecución fluida)");
    }

    public static void demonstrarPrintf() {
        System.out.println("--- Salida Formateada (printf) ---");
        
        String producto = "Manzanas";
        int cantidad = 50;
        double precio = 1.50;

        // %-15s: String alineado a izquierda, 15 espacios.
        // %10d: Entero alineado a derecha, 10 espacios.
        // %10.2f: Double, 10 espacios total, 2 decimales.
        // %n: Salto de línea portable.
        System.out.printf("%-15s %10s %10s%n", "PRODUCTO", "CANTIDAD", "PRECIO");
        System.out.printf("%-15s %10d %10.2f%n", producto, cantidad, precio);
        System.out.printf("%-15s %10d %10.2f%n", "Naranjas", 30, 2.25);
    }

    /**
     * Muestra cómo usar Scanner correctamente, incluyendo la limpieza del buffer.
     */
    public static void demonstrarScanner() {
        Scanner sc = new Scanner(System.in);
        sc.useLocale(Locale.US); // Usar punto como separador decimal

        System.out.print("Ingrese su edad: ");
        int edad = sc.nextInt();
        
        // LIMPIEZA DEL BUFFER: El nextInt() deja un \n que debe ser consumido
        // antes de llamar a nextLine().
        sc.nextLine(); 

        System.out.print("Ingrese su nombre completo: ");
        String nombre = sc.nextLine();

        System.out.printf("Hola %s, tenés %d años.%n", nombre, edad);
        
        sc.close();
    }
}
