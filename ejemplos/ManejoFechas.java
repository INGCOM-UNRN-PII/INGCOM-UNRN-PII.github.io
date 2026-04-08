package ar.unrn;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Gestión de tiempo con la API java.time (introducida en Java 8).
 * 
 * Antes de Java 8, se usaban las clases 'Date' y 'Calendar', que eran 
 * mutables y tenían un diseño confuso. La nueva API es inmutable, 
 * segura para hilos (thread-safe) y mucho más clara.
 * 
 * Relación con el TP6 (Objetos y Hora):
 * Al trabajar con fechas, recordá que una fecha no es solo un conjunto de 
 * enteros (día, mes, año), sino un objeto con comportamiento propio.
 */
public class ManejoFechas {

    public static void main(String[] args) {
        System.out.println("--- API de Tiempo Moderno ---");

        // 1. Obtener la fecha y hora actuales.
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();
        LocalDateTime fechaHoraActual = LocalDateTime.now();

        System.out.println("Hoy es: " + hoy);
        System.out.println("Hora actual: " + ahora);

        // 2. Crear fechas específicas (of).
        LocalDate fechaExamen = LocalDate.of(2025, 6, 15);
        System.out.println("\nFecha de examen: " + fechaExamen);

        // 3. Inmutabilidad (Concepto clave)
        // El método plusDays no cambia la fecha original, devuelve una NUEVA instancia.
        // Esto evita efectos secundarios (side effects) inesperados en tu código.
        LocalDate proximaSemana = hoy.plusDays(7);
        System.out.println("\nFecha hoy: " + hoy);
        System.out.println("Fecha próxima semana: " + proximaSemana);

        // 4. Períodos (Cálculo de diferencia entre fechas).
        Period hastaElExamen = Period.between(hoy, fechaExamen);
        System.out.println("\nFaltan " + hastaElExamen.getMonths() + " meses y " 
                + hastaElExamen.getDays() + " días para el examen.");

        // 5. Formateo (Parsing y Formatting).
        DateTimeFormatter formatoLocal = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = hoy.format(formatoLocal);
        System.out.println("\nFecha hoy formateada: " + fechaFormateada);

        // 6. Comparación.
        if (hoy.isBefore(fechaExamen)) {
            System.out.println("Todavía tenés tiempo de estudiar.");
        }

        // Reflexión sobre Memoria y Diseño:
        // En C, representar una fecha suele implicar un 'struct tm' y manejo
        // manual de punteros y aritmética de tiempo.
        // En Java, delegamos esta complejidad a objetos inmutables bien diseñados.
        // Al ser inmutables, podés compartirlos entre diferentes partes de tu 
        // programa sin miedo a que alguien cambie "el valor de hoy".
    }
}
