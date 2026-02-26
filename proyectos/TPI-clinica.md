Enunciado: Sistema de Gestión Hospitalaria (MultiSalud)
=======================================================

El objetivo es desarrollar un **MultiGestor de Salud** que permita administrar múltiples Centros Médicos, sus agendas de turnos y el historial clínico de los pacientes. El sistema debe seguir un enfoque de Programación Orientada a Objetos estricto y garantizar la persistencia de los datos en disco.

Requerimientos Funcionales
--------------------------

Cada **Gestor** puede administrar múltiples **Centros Médicos**. Un centro contiene un conjunto de **Turnos**, y cada turno tiene asociado un **Médico** y un **Paciente** (quien posee un historial de consultas).

### Gestión de Turnos y Pacientes

1.  **Crear Centro Médico**: Cada centro debe tener un nombre único y una especialidad principal (General, Pediatría, etc.).

2.  **Programar Turno**: Añadir un nuevo turno a un centro específico. Un turno posee un ID único, fecha, hora, médico asignado y una categoría de prioridad (URGENCIA, CONSULTA, CONTROL).

3.  **Registrar Paciente en Turno**: Vincular un paciente al turno. El paciente tiene nombre, DNI y edad. Un paciente no puede tener dos turnos en el mismo horario dentro del mismo centro.

4.  **Cancelar Turno**: Remover un turno del sistema utilizando su ID único.

5.  **Buscar Turno por ID**: Recuperar la información completa del turno, incluyendo datos del médico y del paciente.

6.  **Buscar Paciente por DNI**: Localizar a un paciente y listar todos sus turnos programados e históricos dentro del centro médico.

7.  **Listar Turnos por Fecha**: Devolver una lista de turnos ordenados cronológicamente por fecha y hora.

8.  **Listar Pacientes por Prioridad**: Devolver la lista de pacientes de un día específico, ordenados por la jerarquía de prioridad del turno (URGENCIA > CONSULTA > CONTROL).

9.  **Estadísticas del Centro**:

    -   Cantidad de turnos atendidos vs. programados.

    -   Edad promedio de los pacientes atendidos en el centro.

10. **Limpiar Centro**: Eliminar todos los turnos y registros de pacientes de un centro médico específico.

Requerimientos Técnicos
-----------------------

1.  **Persistencia**: El sistema debe persistir la información en archivos de disco de forma que los datos sobrevivan al cierre de la aplicación. Se requiere el uso de un patrón que desacople la lógica de negocio (e.g., Repository o DAO).

2.  **Estructuras de Datos**: Utilizar colecciones del framework de Java (`List`, `Set`, `Map`) de forma justificada (e.g., `Set` para evitar turnos duplicados, `Map` para búsquedas por DNI).

3.  **Patrones de Diseño**: Se valorará la implementación de patrones como *Strategy* para los criterios de ordenamiento o *Singleton*/*Factory* donde sea pertinente.

4.  **Orientación a Objetos Estricta**:

    -   Encapsulamiento riguroso y uso de modificadores de acceso.

    -   Definición de contratos mediante interfaces.

    -   Relaciones de composición claras (Centro -> Turno -> Paciente/Médico).

5.  **Testing**: La validación se realizará exclusivamente mediante **Tests Unitarios (JUnit 5)**. Se debe cubrir la lógica de negocio, los algoritmos de ordenamiento y la correcta persistencia/recuperación desde archivos. **No desarrollar interfaz de usuario ni método main.**

Criterios de Evaluación
-----------------------

-   **Modelado**: Correcta jerarquía de objetos y manejo de relaciones de asociación y composición.

-   **Excepciones**: Gestión de errores (e.g., superposición de horarios, archivos corruptos, paciente ya registrado).

-   **Clean Code**: Métodos breves, nombres semánticos y cumplimiento de principios SOLID (especialmente Responsabilidad Única e Inversión de Dependencias).

-   **Persistencia**: Garantizar que el estado del sistema se recupera íntegramente desde los archivos al iniciar los tests.
