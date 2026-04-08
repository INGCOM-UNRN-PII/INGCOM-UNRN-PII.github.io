package ar.unrn;

/**
 * Interfaz que define un contrato para objetos que pueden buscarse.
 * En C, logramos comportamientos genéricos mediante punteros a funciones
 * (void*) y qsort/bsearch. En Java, las interfaces permiten definir 
 * contratos que las clases deben cumplir para garantizar que ciertos 
 * comportamientos estén presentes.
 */
interface Buscable {
    /**
     * @param criterio término para la búsqueda
     * @return verdadero si el objeto coincide con el criterio.
     */
    boolean coincideCon(String criterio);
}

/**
 * Clase Libro que implementa la interfaz Buscable.
 */
class Libro implements Buscable {
    private String titulo;
    private String autor;

    public Libro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
    }

    @Override
    public boolean coincideCon(String criterio) {
        // En Java, el manejo de Strings es mucho más simple que con strcmp y punteros char.
        return titulo.toLowerCase().contains(criterio.toLowerCase()) || 
               autor.toLowerCase().contains(criterio.toLowerCase());
    }

    @Override
    public String toString() {
        return "'" + titulo + "' de " + autor;
    }
}

/**
 * Demostración de Polimorfismo mediante interfaces.
 */
public class EjemploInterfaces {
    public static void main(String[] args) {
        // El tipo de la referencia es la Interfaz, no la clase concreta.
        Buscable item = new Libro("Rayuela", "Julio Cortázar");
        
        String termino = "Cortázar";
        System.out.println("Buscando '" + termino + "' en: " + item);
        
        if (item.coincideCon(termino)) {
            System.out.println("¡Coincidencia encontrada!");
        } else {
            System.out.println("No se encontró el término.");
        }
    }
}
