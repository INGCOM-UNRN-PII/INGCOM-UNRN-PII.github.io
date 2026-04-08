package ar.unrn;

/**
 * Demostración de Varargs (Argumentos Variables).
 * 
 * Permite que un método reciba una cantidad indeterminada de argumentos
 * del mismo tipo, tratándolos internamente como un arreglo.
 */
public class ArgumentosVariables {

    public static void main(String[] args) {
        System.out.println("--- Varargs en Java ---");

        // Podemos llamar al mismo método con distinta cantidad de argumentos
        int s1 = sumarVarios(10, 20);
        int s2 = sumarVarios(1, 2, 3, 4, 5);
        int s3 = sumarVarios(); // 0 argumentos también es válido

        System.out.println("Suma 1: " + s1);
        System.out.println("Suma 2: " + s2);
        System.out.println("Suma 3: " + s3);

        imprimirConPrefijo("Notas", 7, 8, 9, 10);
    }

    /**
     * El parámetro varargs (int... numeros) debe ser el último.
     */
    public static int sumarVarios(int... numeros) {
        int total = 0;
        // Se recorre como un arreglo común
        for (int n : numeros) {
            total = total + n;
        }
        return total;
    }

    public static void imprimirConPrefijo(String prefijo, int... valores) {
        System.out.print(prefijo + ": ");
        for (int i = 0; i < valores.length; i = i + 1) {
            System.out.print(valores[i] + (i < valores.length - 1 ? ", " : ""));
        }
        System.out.println();
    }
}
