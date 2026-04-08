package ar.unrn;

import java.util.ArrayList;
import java.util.List;

/**
 * Ejemplo de Genéricos (Generics) en Java.
 * 
 * Los genéricos te permiten parametrizar tipos, permitiendo que una clase, 
 * interfaz o método opere sobre objetos de diversos tipos manteniendo la 
 * seguridad en tiempo de compilación (type safety).
 * 
 * Comparación con C:
 * En C, para lograr algo similar solés usar punteros 'void*', lo que requiere
 * "castings" constantes y es propenso a errores de memoria (segmentation faults)
 * si te equivocás de tipo. En Java, el compilador verifica que los tipos sean
 * compatibles ANTES de ejecutar el programa.
 */
public class GenericosEjemplo {

    /**
     * Una clase genérica que actúa como un contenedor para cualquier tipo T.
     * T es un "placeholder" que será reemplazado por un tipo real al instanciarla.
     */
    public static class Caja<T> {
        private T contenido;

        public void poner(T algo) {
            this.contenido = algo;
        }

        public T sacar() {
            return contenido;
        }
    }

    /**
     * Un ejemplo de un par genérico con dos tipos distintos (K, V).
     */
    public static class Par<K, V> {
        private final K clave;
        private final V valor;

        public Par(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }

        public K getClave() { return clave; }
        public V getValor() { return valor; }
    }

    public static void main(String[] args) {
        System.out.println("--- Demostración de Genéricos ---");

        // 1. Caja de Integers
        // Observá que usamos 'Integer' (la clase envoltorio) y no 'int'.
        // Los genéricos en Java no funcionan con tipos primitivos directamente.
        Caja<Integer> cajaEntera = new Caja<>();
        cajaEntera.poner(42);
        Integer numero = cajaEntera.sacar(); // No requiere cast, el compilador "sabe" que es Integer.
        System.out.println("Contenido de la caja: " + numero);

        // 2. Seguridad de Tipos (Type Safety)
        // Si intentaras hacer: cajaEntera.poner("Hola"); // El compilador te daría un ERROR.
        
        // 3. Caja de Strings
        Caja<String> cajaTexto = new Caja<>();
        cajaTexto.poner("Ingeniería en Computación");
        System.out.println("Texto en la caja: " + cajaTexto.sacar().toUpperCase());

        // 4. Par Genérico (Clave-Valor)
        Par<Integer, String> alumno = new Par<>(12345, "Juan Pérez");
        System.out.println("Legajo: " + alumno.getClave() + ", Nombre: " + alumno.getValor());

        // 5. Borrado de Tipos (Type Erasure)
        // Tené en cuenta que en tiempo de ejecución, la JVM "borra" la información
        // de los tipos genéricos para mantener compatibilidad con versiones viejas.
        // Esto significa que 'Caja<Integer>' y 'Caja<String>' son la misma clase (.class)
        // pero con restricciones impuestas por el compilador.
        
        demostrarListaGenerica();
    }

    private static void demostrarListaGenerica() {
        // List es una interfaz genérica fundamental en la biblioteca estándar.
        List<String> lenguajes = new ArrayList<>();
        lenguajes.add("Java");
        lenguajes.add("C");
        lenguajes.add("Python");

        System.out.println("\nRecorriendo lista genérica:");
        for (String l : lenguajes) {
            System.out.println("- " + l);
        }
    }
}
