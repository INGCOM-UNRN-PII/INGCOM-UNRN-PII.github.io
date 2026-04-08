package ar.unrn;

/**
 * Ejemplo de estructuras de control de flujo en Java.
 * 
 * Se prioriza el uso de banderas booleanas para el control de lazos,
 * siguiendo las recomendaciones académicas del proyecto (evitar break/continue).
 */
public class ControlFlujo {

    public static void main(String[] args) {
        System.out.println("--- Control de Flujo ---");

        demonstrarSeleccion(25);
        demonstrarSwitchModerno(3);
        demonstrarLazos();
        demonstrarBusquedaConBandera();
    }

    public static void demonstrarSeleccion(int edad) {
        // if-else con lógica booleana estricta
        if (edad >= 18) {
            System.out.println("Es mayor de edad.");
        } else if (edad > 0) {
            System.out.println("Es menor de edad.");
        } else {
            System.out.println("Edad inválida.");
        }

        // Operador ternario
        String categoria = (edad >= 18) ? "ADULTO" : "MENOR";
        System.out.println("Categoría: " + categoria);
    }

    public static void demonstrarSwitchModerno(int dia) {
        // Switch como expresión (Java 14+)
        String nombreDia = switch (dia) {
            case 1 -> "Lunes";
            case 2 -> "Martes";
            case 3 -> "Miércoles";
            case 4 -> "Jueves";
            case 5 -> "Viernes";
            case 6, 7 -> "Fin de semana";
            default -> "Desconocido";
        };
        System.out.println("Día " + dia + ": " + nombreDia);
    }

    public static void demonstrarLazos() {
        // Lazo for (cantidad conocida)
        System.out.print("For: ");
        for (int i = 1; i <= 5; i = i + 1) {
            System.out.print(i + " ");
        }
        System.out.println();

        // Lazo while (evaluación al inicio)
        int j = 1;
        System.out.print("While: ");
        while (j <= 5) {
            System.out.print(j + " ");
            j = j + 1;
        }
        System.out.println();

        // Lazo do-while (al menos una ejecución)
        int k = 1;
        System.out.print("Do-While: ");
        do {
            System.out.print(k + " ");
            k = k + 1;
        } while (k <= 5);
        System.out.println();
    }

    /**
     * Ejemplo de búsqueda sin usar 'break', empleando una bandera.
     * Es el patrón recomendado en la cátedra por claridad.
     */
    public static void demonstrarBusquedaConBandera() {
        int[] numeros = {10, 20, 30, 40, 50};
        int buscado = 30;
        boolean encontrado = false;
        int i = 0;

        while (i < numeros.length && !encontrado) {
            if (numeros[i] == buscado) {
                encontrado = true;
            } else {
                i = i + 1;
            }
        }

        if (encontrado) {
            System.out.println("Valor " + buscado + " encontrado en índice " + i);
        }
    }
}
