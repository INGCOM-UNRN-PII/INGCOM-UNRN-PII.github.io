package ar.unrn;

/**
 * Ejemplos de recursión y explicación conceptual del Stack (Pila de llamadas).
 * 
 * Al igual que en C, cada vez que se llama a un método, se crea un 
 * 'Stack Frame' que almacena parámetros y variables locales. 
 * En Java, la recursión profunda sin un caso base adecuado lanzará un 
 * StackOverflowError.
 */
public class EjemploRecursion {

    public static void main(String[] args) {
        int n = 5;

        // Factorial: ejemplo clásico.
        System.out.println("Factorial de " + n + ": " + factorial(n));

        // Fibonacci: ejemplo de recursión múltiple.
        System.out.println("Fibonacci (" + n + "): " + fibonacci(n));

        // Búsqueda binaria recursiva sobre un arreglo ordenado.
        int[] arreglo = {1, 3, 5, 7, 9, 11, 13};
        int buscar = 7;
        int pos = busquedaBinaria(arreglo, buscar, 0, arreglo.length - 1);
        System.out.println("El número " + buscar + " está en la posición: " + pos);
    }

    /**
     * Calcula el factorial de un número.
     * n! = n * (n-1)!
     * Caso base: 0! = 1
     */
    public static long factorial(int n) {
        if (n <= 1) { // Caso base fundamental para detener la recursión.
            return 1;
        }
        return n * factorial(n - 1); // Paso recursivo.
    }

    /**
     * Sucesión de Fibonacci: f(n) = f(n-1) + f(n-2).
     * Nota: Este enfoque recursivo simple es ineficiente por el 
     * recálculo constante de valores (O(2^n)).
     */
    public static int fibonacci(int n) {
        if (n <= 1) { // Casos base f(0)=0, f(1)=1.
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    /**
     * Búsqueda binaria en un arreglo ordenado (recursiva).
     * En cada paso se descarta la mitad de los elementos.
     */
    public static int busquedaBinaria(int[] arr, int objetivo, int inicio, int fin) {
        if (inicio > fin) {
            return -1; // No encontrado.
        }

        int medio = inicio + (fin - inicio) / 2;

        if (arr[medio] == objetivo) {
            return medio; // Encontrado.
        }

        if (arr[medio] > objetivo) {
            // Busca en la mitad izquierda.
            return busquedaBinaria(arr, objetivo, inicio, medio - 1);
        } else {
            // Busca en la mitad derecha.
            return busquedaBinaria(arr, objetivo, medio + 1, fin);
        }
    }
}
