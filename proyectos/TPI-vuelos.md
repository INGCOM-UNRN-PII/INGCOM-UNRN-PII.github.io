Enunciado: Sistema de Gestión de Reservas Aéreas (MultiFly)
===========================================================

El objetivo es desarrollar un **MultiGestor de Reservas** que permita administrar múltiples Aerolíneas, sus vuelos programados y el manifiesto de pasajeros con su respectivo historial de fidelidad (millas). El sistema debe seguir un enfoque de Programación Orientada a Objetos estricto y garantizar la persistencia de los datos en disco.

Requerimientos Funcionales
--------------------------

Cada **Gestor** puede administrar múltiples **Aerolíneas**. Una aerolínea contiene un conjunto de **Vuelos**, y cada vuelo tiene asociado un **Manifiesto de Pasajeros** (quienes acumulan millas por cada viaje).

### Gestión de Vuelos y Fidelidad

1.  **Crear Aerolínea**: Cada aerolínea debe tener un nombre único y un código de operador (IATA).

2.  **Programar Vuelo**: Añadir un nuevo vuelo a una aerolínea específica. Un vuelo posee un número de vuelo único, origen, destino, capacidad máxima y precio base.

3.  **Registrar Pasajero en Vuelo**: Añadir un pasajero al manifiesto de un vuelo. Un pasajero tiene nombre, DNI y millas acumuladas. No se pueden superar la capacidad máxima del vuelo.

4.  **Cancelar Vuelo**: Remover un vuelo del sistema utilizando su número de vuelo.

5.  **Buscar Vuelo por Número**: Recuperar la información completa de un vuelo y su lista de pasajeros.

6.  **Buscar Pasajero por DNI**: Localizar a un pasajero y listar todos los vuelos en los que está registrado dentro de la aerolínea.

7.  **Listar Vuelos por Precio**: Devolver una lista de vuelos registrados ordenados de menor a mayor precio base.

8.  **Listar Pasajeros por Millas**: Devolver el manifiesto de un vuelo ordenado descendentemente por la cantidad de millas de los pasajeros (prioridad de abordaje).

9.  **Estadísticas de la Aerolínea**:

    -   Recaudación total proyectada (suma de precios base de todos los pasajes vendidos).

    -   Ocupación promedio de sus vuelos (porcentaje).

10. **Resetear Aerolínea**: Eliminar todos los vuelos y registros de pasajeros de una aerolínea específica.

Requerimientos Técnicos
-----------------------

1.  **Persistencia**: El sistema debe persistir la información en archivos de disco de forma que los datos sobrevivan al cierre de la aplicación. Se requiere el uso de un patrón que desacople la lógica de negocio (e.g., Repository o DAO).

2.  **Estructuras de Datos**: Utilizar colecciones del framework de Java (`List`, `Set`, `Map`) de forma justificada según el caso de uso (unicidad de vuelos, búsqueda rápida de pasajeros, etc.).

3.  **Patrones de Diseño**: Se valorará la implementación de patrones como *Strategy* para los criterios de ordenamiento o *Factory* para la instanciación de entidades.

4.  **Orientación a Objetos Estricta**:

    -   Encapsulamiento riguroso y uso de modificadores de acceso.

    -   Definición de contratos mediante interfaces.

    -   Relaciones de composición claras (Aerolínea -> Vuelo -> Pasajero).

5.  **Testing**: La validación se realizará exclusivamente mediante **Tests Unitarios (JUnit 5)**. Se debe cubrir la lógica de negocio, los algoritmos de ordenamiento y la correcta persistencia/recuperación desde archivos. **No desarrollar interfaz de usuario ni método main.**

Criterios de Evaluación
-----------------------

-   **Modelado**: Correcta jerarquía de objetos y manejo de relaciones uno-a-muchos.

-   **Excepciones**: Gestión de errores (e.g., sobreventa de vuelos, archivos no encontrados, DNI duplicados).

-   **Clean Code**: Métodos breves, nombres semánticos en inglés o español (pero consistentes) y cumplimiento de principios SOLID.

-   **Persistencia**: Integridad de los datos al realizar operaciones de lectura/escritura en disco.
