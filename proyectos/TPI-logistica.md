---
title: "TPI - Sistema de Gestión de Logística"
description: Proyecto integrador para desarrollar un sistema de gestión logística con envíos.
---

# Sistema de Gestión de Logística

El objetivo es desarrollar un **Gestor de Logística** que permita administrar múltiples centros de distribución, sus envíos asociados y el historial de estados de cada paquete. El sistema debe seguir un enfoque de Programación Orientada a Objetos estricto y garantizar la persistencia de los datos en disco.

## Requerimientos Funcionales

Cada **Gestor** puede administrar múltiples **Centros de Distribución**. Un centro contiene un conjunto de **Envíos**, y cada envío tiene asociado un **Historial de Estados** (eventos de seguimiento, como el ingreso, el viaje a otro centro de distribución y la entrega).

### Gestión de Envíos y Seguimiento

1.  **Crear Centro de Distribución**: Cada centro debe tener un nombre único y una ubicación geográfica.

2.  **Registrar Envío**: Añadir un nuevo paquete a un centro específico. Un envío posee un código de seguimiento único (tracking ID), peso, destinatario y una prioridad (URGENTE, ESTÁNDAR, ECONÓMICO).

3.  **Actualizar Estado**: Registrar un nuevo evento de estado para un envío (e.g., "En depósito", "En viaje", "Entregado"). Cada evento debe almacenar la descripción y la fecha/hora del suceso.

4.  **Eliminar Envío**: Remover un paquete del sistema utilizando su código de seguimiento.

5.  **Buscar Envío por Tracking ID**: Recuperar la información completa de un envío y su historial de estados.

6.  **Buscar Envíos por Destinatario**: Listar todos los paquetes asociados a un mismo nombre de destinatario.

7.  **Listar Envíos por Peso**: Devolver una lista de envíos ordenados de mayor a menor peso.

8.  **Listar Envíos por Prioridad**: Devolver una lista de envíos ordenados por jerarquía de prioridad (URGENTE > ESTÁNDAR > ECONÓMICO).

9.  **Estadísticas del Centro**:
    - Peso total transportado en el centro.
    - Porcentaje de envíos con estado "Entregado".

10. **Vaciar Centro**: Eliminar todos los envíos y sus historiales de un centro de distribución.

## Requerimientos Técnicos

1.  **Persistencia**: El sistema debe guardar la información a disco.

2.  **Estructuras de Datos**: Utilizar colecciones del framework de Java (`List`, `Set`, `Map`) de forma justificada.

3.  **Patrones de Diseño**: Se valorará la utilización de patrones de diseño como parte de la estructura del proyecto.

4.  **Orientación a Objetos Estricta**:
    - Uso riguroso de encapsulamiento y modificadores de acceso.
    - Definición de comportamientos mediante interfaces.
    - Relaciones de composición claras entre Centro, Envío y Estado.

:::{important}
**Testing**: La validación se realizará exclusivamente mediante **Tests Unitarios (JUnit 5)**. Debe haber cobertura sobre la lógica de ordenamiento, cálculos estadísticos y la integridad de la persistencia. **No desarrollar interfaz de usuario ni método main.**
:::

