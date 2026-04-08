package ar.unrn;

/**
 * Ejemplo detallado de los tipos de datos primitivos en Java.
 * 
 * Java garantiza el tamaño de los tipos en todas las plataformas,
 * resolviendo problemas de portabilidad comunes en C.
 */
public class TiposDeDatos {

    public static void main(String[] args) {
        System.out.println("--- Tipos de Datos Primitivos ---");

        // --- Enteros ---
        byte unByte = 127;          // 8 bits (-128 a 127)
        short unShort = 32767;      // 16 bits
        int unInt = 2147483647;     // 32 bits (Tipo principal)
        long unLong = 9223372036854775807L; // 64 bits (Requiere sufijo L)

        // --- Punto Flotante ---
        float unFloat = 3.14f;      // 32 bits (Requiere sufijo f)
        double unDouble = 3.14159;  // 64 bits (Tipo principal para decimales)

        // --- Booleano ---
        // A diferencia de C, no es un entero. Solo acepta true o false.
        boolean esJavaDivertido = true;

        // --- Carácter ---
        // 16 bits, usa Unicode (soporta tildes, ñ, emojis, etc.)
        char letra = 'A';
        char enie = 'ñ';

        System.out.println("Entero largo: " + unLong);
        System.out.println("Carácter especial: " + enie);

        // --- Ejemplo de precisión con Punto Flotante ---
        // Los decimales no son exactos en binario (IEEE 754).
        double sumaCuidado = 0.1 + 0.2;
        System.out.println("¿0.1 + 0.2 es 0.3? " + sumaCuidado); // Imprime 0.30000000000000004

        // --- Casting (Conversiones) ---
        // Widening (Automático): De menor a mayor capacidad.
        int i = 100;
        long l = i; 

        // Narrowing (Explícito): De mayor a menor capacidad.
        double d = 10.5;
        int truncado = (int) d; // Se pierde el .5
        
        // --- Constantes ---
        final double PI = 3.14159265359;
        // PI = 3.0; // ERROR: No se puede cambiar una variable final.
    }
}
