package ar.unrn;

/**
 * Ejemplo que resume los conceptos de los orígenes e historia de Java.
 * 
 * Java nace en 1991 (Green Project) con el objetivo de ser un lenguaje
 * para dispositivos embebidos, priorizando la robustez y la portabilidad.
 * 
 * Conceptos clave:
 * - JVM (Java Virtual Machine): La pieza que permite la independencia del hardware.
 * - Bytecode: El lenguaje intermedio que entiende la JVM.
 * - WORA: "Write Once, Run Anywhere".
 * - Garbage Collector: Gestión automática de memoria para evitar fugas (memory leaks).
 */
public class OrigenesJava {

    public static void main(String[] args) {
        System.out.println("--- Orígenes e Historia de Java ---");
        
        // El concepto de portabilidad se basa en que este código compilado
        // generará un archivo .class (Bytecode) que funcionará en cualquier
        // sistema con una JVM instalada.
        
        System.out.println("Filosofía: WORA (Write Once, Run Anywhere)");
        
        // Java elimina los punteros directos y la gestión manual de memoria (malloc/free)
        // presentes en C/C++ para evitar errores comunes de seguridad y estabilidad.
        
        demostrarGestionMemoria();
    }

    /**
     * En Java, no liberamos la memoria manualmente. El Garbage Collector (GC)
     * se encarga de identificar objetos que ya no tienen referencias y liberar su espacio.
     */
    public static void demostrarGestionMemoria() {
        System.out.println("La gestión de memoria es automática gracias al Garbage Collector.");
        
        // Simplemente dejamos de usar un objeto y el GC eventualmente lo limpiará.
        String mensaje = new String("Este objeto será recolectado si ya no se usa.");
        mensaje = null; // Al asignar null, el objeto original queda huérfano y es candidato para el GC.
    }
}
