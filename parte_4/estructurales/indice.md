---
title: "estructurales"
subtitle: "Composición de clases y objetos"
subject: Patrones de Diseño
---

(patrones-estructurales)=
# Familia Estructural

Los **patrones estructurales** se ocupan de cómo las clases y objetos se componen para formar estructuras más grandes, manteniendo flexibilidad y eficiencia. Estos patrones facilitan crear relaciones entre entidades de forma que permitan obtener nuevas funcionalidades.

## Patrones en Esta Familia

| Patrón | Propósito | Uso Principal |
| :--- | :--- | :--- |
| {ref}`patron-adapter` | Compatibilidad entre interfaces incompatibles | Integración de código legacy |
| {ref}`patron-bridge` | Desacoplar abstracción de implementación | Múltiples dimensiones de variación |
| {ref}`patron-composite` | Componer objetos en estructuras de árbol | Estructuras jerárquicas |
| {ref}`patron-decorator` | Agregar responsabilidades dinámicamente | Comportamiento dinámico sin subclases |
| {ref}`patron-facade` | Proporcionar interfaz unificada simplificada | Simplificar sistemas complejos |
| {ref}`patron-flyweight` | Compartir objetos para ahorrar memoria | Optimizar memoria con muchos objetos |
| {ref}`patron-proxy` | Control de acceso a otro objeto | Autorización, lazy loading, logging |

## Cuándo Usarlos

**Adapter**: Integrar código legacy con código nuevo
- Dos interfaces incompatibles necesitan trabajar juntas
- Quieres reutilizar clases existentes sin modificarlas

**Bridge**: Múltiples dimensiones de variación independientes
- Necesitas evitar explosión de subclases (N × M combinaciones)
- Abstracciones e implementaciones varían independientemente

**Composite**: Estructuras jerárquicas (árboles, menús, archivos)
- Necesitas tratar uniformemente hojas y compuestos
- Requieres recursión sobre estructuras arbitrariamente profundas

**Decorator**: Comportamiento dinámico sin crear subclases
- Necesitas agregar funcionalidad en tiempo de ejecución
- Herencia sería explosiva (múltiples combinaciones de características)

**Facade**: Simplificar sistemas complejos
- Subsistema tiene múltiples componentes acoplados
- Quieres proporcionar punto de entrada único y simple

**Flyweight**: Optimizar memoria con muchos objetos similares
- Millones de objetos consumirían mucha memoria
- Puedes separar estado compartible del estado particular

**Proxy**: Controlar acceso, lazy loading, auditoría
- Necesitas interceder en acceso a objeto real
- Objeto es costoso (remoto, BD, archivo grande)
- Requieres autorización, caché, o logging

## Decisión Rápida

¿Dos interfaces incompatibles? → **Adapter**

¿Múltiples dimensiones de variación? → **Bridge**

¿Estructura jerárquica (árbol)? → **Composite**

¿Agregar responsabilidades dinámicamente? → **Decorator**

¿Simplificar acceso a subsistema? → **Facade**

¿Muchos objetos idénticos (memoria)? → **Flyweight**

¿Controlar acceso a objeto? → **Proxy**

## Práctica integradora

Después de recorrer los patrones individuales, conviene resolver los [ejercicios integradores de estructurales](ejercicios_integradores.md).

:::{tip}
En esta familia suele haber más de una respuesta plausible. La clave es justificar el costo de acoplamiento, indirección o complejidad que introduce cada patrón.
:::
