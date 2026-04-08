package ar.unrn;

/**
 * Implementación del patrón de diseño Singleton.
 * 
 * Este patrón garantiza que una clase tenga una única instancia 
 * y proporciona un punto de acceso global a ella. 
 * Es útil para representar recursos únicos del sistema, como un gestor de 
 * base de datos, un pool de conexiones o un gestor de configuración.
 * 
 * En C, algo similar se lograba mediante variables estáticas globales en un
 * módulo (archivo .c) que no se exportaban fuera del mismo.
 */
public class PatronSingleton {

    // 1. Atributo estático privado para almacenar la única instancia.
    // Inicialización perezosa (Lazy initialization).
    private static PatronSingleton instanciaUnica;

    private String estado;

    // 2. Constructor privado: Evita que otras clases puedan hacer 'new PatronSingleton()'.
    private PatronSingleton() {
        this.estado = "Configuración por defecto";
        System.out.println("Creando la instancia única del Singleton...");
    }

    // 3. Método estático público para obtener la instancia.
    // Usamos 'synchronized' para garantizar la seguridad en entornos multi-hilo (thread-safe).
    public static synchronized PatronSingleton getInstancia() {
        if (instanciaUnica == null) {
            instanciaUnica = new PatronSingleton();
        }
        return instanciaUnica;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    /**
     * Ejemplo de uso del Singleton.
     */
    public static void main(String[] args) {
        // Obtenemos la instancia por primera vez.
        PatronSingleton s1 = PatronSingleton.getInstancia();
        System.out.println("Estado inicial s1: " + s1.getEstado());

        // Intentamos obtener otra instancia.
        PatronSingleton s2 = PatronSingleton.getInstancia();
        
        // Verificamos que ambas referencias apuntan al MISMO objeto.
        if (s1 == s2) {
            System.out.println("Ambos objetos son la misma instancia (mismo espacio de memoria).");
        }

        // Si modificamos s2, el cambio se refleja en s1.
        s2.setEstado("Nueva configuración actualizada");
        System.out.println("Estado final s1: " + s1.getEstado());
    }
}
