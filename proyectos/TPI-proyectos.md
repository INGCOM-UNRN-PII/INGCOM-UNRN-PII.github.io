---
title: "TPI - Sistema de Gestión de Proyectos (MultiGestor)"
description: Proyecto integrador para desarrollar un sistema de gestión de proyectos.
---

# Sistema de Gestión de Proyectos (MultiGestor)

El objetivo es desarrollar un **MultiGestor de Proyectos** que permita administrar múltiples carteras de proyectos, sus tareas asociadas y los recursos humanos asignados. El sistema debe seguir un enfoque de Programación Orientada a Objetos estricto y garantizar la persistencia de los datos.

## Requerimientos Funcionales

Cada **Gestor** puede contener múltiples **Proyectos**. Un proyecto está compuesto por un conjunto de **Tareas**, y a cada tarea se le pueden asignar **Colaboradores**.

### Gestión de Tareas y Colaboradores

1.  **Crear Proyecto**: Cada proyecto debe tener un nombre único, una descripción y una fecha de inicio.

2.  **Agregar Tarea**: Añadir tareas a un proyecto específico. Una tarea tiene un título, una prioridad (ALTA, MEDIA, BAJA), una fecha límite y un estado (PENDIENTE, EN_PROCESO, COMPLETADA).

3.  **Asignar Colaborador**: Vincular un colaborador (nombre, legajo, especialidad) a una tarea específica. Una tarea puede tener múltiples colaboradores.

4.  **Eliminar Tarea**: Remover una tarea del proyecto mediante su identificador único.

5.  **Buscar Tarea por Título**: Recuperar la información de una tarea filtrando por su título.

6.  **Buscar Colaborador por Legajo**: Localizar a un colaborador en el sistema y listar las tareas en las que participa.

7.  **Listar Tareas por Fecha Límite**: Devolver una lista de tareas ordenadas cronológicamente por su fecha de vencimiento.

8.  **Listar Tareas por Prioridad**: Devolver una lista de tareas ordenadas por jerarquía de prioridad (ALTA > MEDIA > BAJA).

9.  **Estadísticas de Proyecto**:
    - Cantidad total de tareas.
    - Porcentaje de tareas completadas.

10. **Limpiar Proyecto**: Eliminar todas las tareas y asignaciones de un proyecto.

## Requerimientos Técnicos

1.  **Persistencia**: El sistema debe persistir la información en archivos de disco (puede ser mediante Serialización, CSV o JSON manual). Se debe implementar un patrón que desacople la lógica de negocio del almacenamiento (e.g., Repository o DAO).

2.  **Estructuras de Datos**: Utilizar colecciones del framework de Java (`List`, `Set`, `Map`) de forma justificada según la necesidad de ordenamiento o unicidad.

3.  **Patrones de Diseño**: Se valorará el uso de patrones como *Strategy* para los diferentes criterios de ordenamiento, *Factory* para la creación de objetos complejos o *Observer* si fuera necesario.

4.  **Orientación a Objetos Estricta**:
    - Uso correcto de modificadores de acceso.
    - Aplicación de interfaces para definir comportamientos.
    - Evitar el acoplamiento fuerte entre clases.

:::{important}
**Testing**: La validación del sistema se realizará exclusivamente mediante **Tests Unitarios (JUnit 5)**. Debe haber una cobertura que garantice el funcionamiento de la lógica de negocio y la persistencia. **No es necesario desarrollar un método main ni interfaz de usuario.**
:::

## Criterios de Evaluación

- **Modelado de Clases**: Relaciones de composición y agregación correctas.
- **Manejo de Excepciones**: Gestión de errores robusta (e.g., búsqueda de elementos inexistentes, archivos corruptos).
- **Clean Code**: Nombramiento semántico, métodos cortos y responsabilidad única.
- **Persistencia**: Capacidad de recuperar el estado del sistema tras cerrar la ejecución.
