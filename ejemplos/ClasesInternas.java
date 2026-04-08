package ar.unrn;

/**
 * Ejemplo de Clases Internas (Inner Classes) en Java.
 * 
 * Java permite definir clases dentro de otras clases. Existen varios tipos:
 * - Clases Internas (Miembro): Asociadas a una INSTANCIA de la clase externa.
 * - Clases Internas Estáticas (Static Nested): Asociadas a la CLASE externa.
 * - Clases Anónimas: Clases sin nombre definidas al vuelo (inline).
 * 
 * Esto mejora el encapsulamiento y agrupa clases que solo se usan en un 
 * lugar específico.
 */
public class ClasesInternas {

    private String nombreExterno = "Clase Externa";

    /**
     * 1. Clase Interna Miembro.
     * Tiene acceso directo a todos los miembros (incluso privados) de la 
     * instancia de la clase externa que la creó.
     */
    public class Interna {
        public void mostrar() {
            System.out.println("Desde Interna: " + nombreExterno);
        }
    }

    /**
     * 2. Clase Interna Estática.
     * No está vinculada a una instancia. Solo puede acceder a miembros 
     * estáticos de la clase externa.
     */
    public static class EstaticaInterna {
        public void mostrar() {
            System.out.println("Desde Estática: No tengo acceso a 'nombreExterno' directamente.");
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Tipos de Clases Internas ---");

        // Para crear la interna, primero necesitás una instancia de la externa.
        ClasesInternas externa = new ClasesInternas();
        ClasesInternas.Interna objetoInterno = externa.new Interna();
        objetoInterno.mostrar();

        // Para la estática, no hace falta la instancia.
        ClasesInternas.EstaticaInterna objetoEstatico = new ClasesInternas.EstaticaInterna();
        objetoEstatico.mostrar();

        // 3. Clases Anónimas.
        // Se usan mucho para implementar interfaces o extender clases 
        // de forma rápida, sin crear un archivo .java nuevo.
        Runnable tarea = new Runnable() {
            @Override
            public void run() {
                System.out.println("Ejecutando desde una Clase Anónima.");
            }
        };
        tarea.run();

        // Reflexión Técnica:
        // En C, lo más cercano sería tener una estructura (struct) declarada
        // dentro de otra, pero sin la capacidad de heredar contexto o de
        // acceso a ámbitos privados.
        // Las clases internas permiten que tu diseño sea más "limpio" al 
        // esconder implementaciones secundarias que el resto del sistema
        // no necesita conocer.
    }
}
