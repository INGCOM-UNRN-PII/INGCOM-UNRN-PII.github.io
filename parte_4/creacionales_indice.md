---
title: "Patrones Creacionales"
subtitle: "Mecanismos para crear objetos de forma flexible"
subject: Patrones de Diseño
---

(patrones-creacionales)=
# Familia Creacional

Los **patrones creacionales** se ocupan de mecanismos de creación de objetos. Proporcionan formas flexibles de crear instancias manteniendo independencia de las clases específicas.

## Patrones en Esta Familia

### {ref}`patron-singleton`
**Una única instancia global controlada**

- Garantiza exactamente una instancia de una clase
- Proporciona acceso global a esa instancia
- Útil para loggers, configuraciones, pools de conexiones

{ref}`patron-singleton` | [Factory Method](factory.md) | [Abstract Factory](abstract_factory.md) | [Builder](builder.md) | [Prototype](prototype.md)

---

### {ref}`patron-factory-method`
**Crear objetos sin especificar clases concretas**

- Desacopla la creación del objeto del código cliente
- Permite que subclases decidan qué clase instanciar
- Ideal cuando tienes múltiples tipos relacionados

{ref}`patron-singleton` | [Factory Method](factory.md) | [Abstract Factory](abstract_factory.md) | [Builder](builder.md) | [Prototype](prototype.md)

---

### Abstract Factory
**Familias de objetos relacionados**

- Crea grupos coherentes de objetos
- Asegura que objetos relacionados se usen juntos
- Útil para temas, estilos, plataformas

{ref}`patron-singleton` | [Factory Method](factory.md) | [Abstract Factory](abstract_factory.md) | [Builder](builder.md) | [Prototype](prototype.md)

---

### Builder
**Construir objetos complejos paso a paso**

- Separar construcción de representación
- Interfaces fluidas y legibles
- Valores por defecto opcionales

{ref}`patron-singleton` | [Factory Method](factory.md) | [Abstract Factory](abstract_factory.md) | [Builder](builder.md) | [Prototype](prototype.md)

---

### Prototype
**Clonar objetos existentes**

- Crear nuevas instancias copiando prototipos
- Performance cuando creación es costosa
- Registro de objetos reutilizables

{ref}`patron-singleton` | [Factory Method](factory.md) | [Abstract Factory](abstract_factory.md) | [Builder](builder.md) | [Prototype](prototype.md)

---

## Comparación Rápida

```{table} Patrones Creacionales
:label: tbl-creacionales

| Patrón | Propósito | Cuándo Usar |
| :--- | :--- | :--- |
| **Singleton** | Una instancia | Logger, Config, Pool |
| **Factory** | Crear sin especificar tipo | Múltiples subtipos |
| **Abstract Factory** | Familias coherentes | Temas, Plataformas |
| **Builder** | Construcción paso a paso | Objetos complejos |
| **Prototype** | Clonar existentes | Copias costosas |
```

---

## Flujo de Decisión

```
¿Necesitas crear un objeto?
│
├─ ¿Una única instancia global?
│  └─ Usa Singleton
│
├─ ¿Múltiples tipos relacionados?
│  ├─ ¿Familias coherentes?
│  │  └─ Usa Abstract Factory
│  └─ ¿Un tipo a la vez?
│     └─ Usa Factory Method
│
├─ ¿Objeto muy complejo?
│  ├─ ¿Construcción paso a paso?
│  │  └─ Usa Builder
│  └─ ¿Copia de existente?
│     └─ Usa Prototype
│
└─ Usa constructor directo (simple)
```

---

## Características Comunes

Todos los patrones creacionales:

- **Flexibilidad**: Desacoplan creación de uso
- **Extensibilidad**: Agregar nuevos tipos sin modificar código existente
- **Encapsulación**: Ocultan detalles de creación
- **Control**: Centralizan lógica de instanciación
