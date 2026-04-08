package ar.unrn;

/**
 * Demostración de conceptos de memoria, referencias y mutabilidad.
 * 
 * Temas:
 * - Stack (primitivos) vs Heap (objetos).
 * - Alias de referencias.
 * - Inmutabilidad de Strings.
 * - Uso de StringBuilder para eficiencia.
 */
public class MemoriaYStrings {

    public static void main(String[] args) {
        System.out.println("--- Memoria y Referencias ---");

        demonstrarAlias();
        demonstrarInmutabilidadString();
        demonstrarStringBuilder();
    }

    public static void demonstrarAlias() {
        int[] original = {1, 2, 3};
        int[] alias = original; // Copia la referencia, no el contenido

        alias[0] = 99; // Afecta a ambos porque apuntan al mismo objeto en el Heap

        System.out.println("¿Original[0] es 99? " + (original[0] == 99));
    }

    public static void demonstrarInmutabilidadString() {
        System.out.println("\n--- Inmutabilidad de Strings ---");
        String s = "Hola";
        
        // toUpperCase() no modifica 's', retorna un NUEVO String
        String resultado = s.toUpperCase();

        System.out.println("Original: " + s); // Sigue siendo "Hola"
        System.out.println("Resultado: " + resultado); // "HOLA"
        
        // Comparación de contenido: SIEMPRE usar .equals()
        String s2 = new String("Hola");
        System.out.println("¿s == s2? " + (s == s2)); // false (distintas referencias)
        System.out.println("¿s.equals(s2)? " + s.equals(s2)); // true (mismo contenido)
    }

    /**
     * StringBuilder es la alternativa mutable y eficiente para concatenar
     * texto dentro de lazos, evitando la creación de múltiples objetos intermedios.
     */
    public static void demonstrarStringBuilder() {
        System.out.println("\n--- StringBuilder ---");
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < 5; i = i + 1) {
            sb.append("Paso ").append(i).append(". ");
        }
        
        String finalTexto = sb.toString();
        System.out.println("Resultado: " + finalTexto);
    }
}
