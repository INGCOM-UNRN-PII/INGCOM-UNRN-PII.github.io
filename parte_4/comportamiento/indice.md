---
title: "comportamiento"
subtitle: "Responsabilidades de objetos e interacciones"
subject: Patrones de Diseño
---

(patrones-comportamiento)=
# Familia de Comportamiento

Los **patrones de comportamiento** se ocupan de cómo distribuir responsabilidades entre objetos y cómo manejan la comunicación entre ellos.

## Patrones en Esta Familia

| Patrón | Propósito |
| :--- | :--- |
| **Chain of Responsibility** | Pasar solicitudes a lo largo de una cadena |
| **Command** | Encapsular solicitudes como objetos |
| **Interpreter** | Interpretar un lenguaje definido |
| **Iterator** | Recorrer elementos sin exponer estructura |
| **Mediator** | Reducir acoplamiento entre objetos |
| **Memento** | Capturar y restaurar estado interno |
| **Observer** | Notificar múltiples objetos de cambios |
| **State** | Cambiar comportamiento según estado |
| **Strategy** | Encapsular algoritmos intercambiables |
| **Template Method** | Esqueleto de algoritmo en clase base |
| **Visitor** | Operaciones sobre elementos sin cambiarlos |

## Cuándo Usarlos

- **Chain of Responsibility**: Procesar solicitudes secuencialmente
- **Command**: Deshacer/rehacer, colas de comandos
- **Interpreter**: DSLs, expresiones complejas
- **Iterator**: Recorrer colecciones de forma genérica
- **Mediator**: Reducir complejidad de comunicación
- **Memento**: Deshacer/historial
- **Observer**: Notificaciones de cambios
- **State**: Máquinas de estados
- **Strategy**: Múltiples algoritmos intercambiables
- **Template Method**: Algoritmo con pasos customizables
- **Visitor**: Operaciones sobre estructuras complejas

## Comparación rápida

| Si el problema dominante es... | Conviene mirar primero |
| :--- | :--- |
| Pasar una solicitud por múltiples pasos o filtros | **Chain of Responsibility** |
| Encapsular acciones para ejecutarlas, deshacerlas o encolarlas | **Command** |
| Cambiar entre algoritmos equivalentes | **Strategy** |
| Cambiar comportamiento según el estado actual | **State** |
| Notificar cambios a varios interesados | **Observer** |
| Fijar la secuencia general de un algoritmo con pasos variables | **Template Method** |
| Recorrer una estructura sin exponer su representación interna | **Iterator** |
| Centralizar la comunicación entre muchos objetos | **Mediator** |
| Guardar y restaurar snapshots de estado | **Memento** |
| Agregar operaciones nuevas sobre una estructura estable | **Visitor** |
| Interpretar expresiones o un mini lenguaje | **Interpreter** |

## Decisión rápida

- ¿Cambia el **algoritmo** y el resto del contexto queda igual? → **Strategy**
- ¿Cambia el **estado** del objeto y con eso cambia su comportamiento? → **State**
- ¿La duda principal es la **comunicación entre objetos**? → **Observer** o **Mediator**
- ¿Hace falta **deshacer**, registrar o encolar acciones? → **Command**
- ¿El problema es **recorrer** una estructura? → **Iterator**
- ¿El problema es una **secuencia fija con pasos redefinibles**? → **Template Method**

## Práctica integradora

Después de los mini ejercicios por patrón, conviene cerrar con los [ejercicios integradores de comportamiento](ejercicios_integradores.md).

:::{tip}
En esta familia el error típico es elegir por parecido superficial. Antes de decidir, conviene mirar qué cambia: el algoritmo, el estado, la comunicación o la secuencia de pasos.
:::
