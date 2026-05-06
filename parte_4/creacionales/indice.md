---
title: "creacionales"
subtitle: "Mecanismos para crear objetos de forma flexible"
subject: Patrones de Diseño
---

(patrones-creacionales)=
# Familia Creacional

Los **patrones creacionales** se ocupan de mecanismos de creación de objetos. Proporcionan formas flexibles de crear instancias manteniendo independencia de las clases específicas.

## Patrones en Esta Familia

### [Singleton](singleton.md)
**Una única instancia global controlada**

- Garantiza exactamente una instancia de una clase
- Proporciona acceso global a esa instancia
- Útil para loggers, configuraciones, pools de conexiones

[Singleton](singleton.md) | [Factory Method](factory.md) | [Abstract Factory](abstract_factory.md) | [Builder](builder.md) | [Prototype](prototype.md)

---

### [Factory Method](factory.md)
**Crear objetos sin especificar clases concretas**

- Desacopla la creación del objeto del código cliente
- Permite que subclases decidan qué clase instanciar
- Ideal cuando tienes múltiples tipos relacionados

[Singleton](singleton.md) | [Factory Method](factory.md) | [Abstract Factory](abstract_factory.md) | [Builder](builder.md) | [Prototype](prototype.md)

---

### [Abstract Factory](abstract_factory.md)
**Familias de objetos relacionados**

- Crea grupos coherentes de objetos
- Asegura que objetos relacionados se usen juntos
- Útil para temas, estilos, plataformas

[Singleton](singleton.md) | [Factory Method](factory.md) | [Abstract Factory](abstract_factory.md) | [Builder](builder.md) | [Prototype](prototype.md)

---

### [Builder](builder.md)
**Construir objetos complejos paso a paso**

- Separar construcción de representación
- Interfaces fluidas y legibles
- Valores por defecto opcionales

[Singleton](singleton.md) | [Factory Method](factory.md) | [Abstract Factory](abstract_factory.md) | [Builder](builder.md) | [Prototype](prototype.md)

---

### [Prototype](prototype.md)
**Clonar objetos existentes**

- Crear nuevas instancias copiando prototipos
- Performance cuando creación es costosa
- Registro de objetos reutilizables

[Singleton](singleton.md) | [Factory Method](factory.md) | [Abstract Factory](abstract_factory.md) | [Builder](builder.md) | [Prototype](prototype.md)

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

````{mermaid}

flowchart TD
    inicio{"Necesitas crear un objeto?"}
    unica{"Una unica instancia global?"}
    relacionados{"Multiples tipos relacionados?"}
    familias{"Familias coherentes?"}
    unTipo{"Un tipo a la vez?"}
    complejo{"Objeto muy complejo?"}
    pasoAPaso{"Construccion paso a paso?"}
    copia{"Copia de existente?"}

    inicio --> unica
    unica -->|Si| singleton["Usa Singleton"]
    unica -->|No| relacionados

    relacionados -->|Si| familias
    familias -->|Si| abstractFactory["Usa Abstract Factory"]
    familias -->|No| unTipo
    unTipo -->|Si| factory["Usa Factory Method"]
    unTipo -->|No| complejo

    relacionados -->|No| complejo
    complejo -->|Si| pasoAPaso
    pasoAPaso -->|Si| builder["Usa Builder"]
    pasoAPaso -->|No| copia
    copia -->|Si| prototype["Usa Prototype"]
    copia -->|No| directo["Usa constructor directo"]

    complejo -->|No| directo

````

---

## Características Comunes

Todos los patrones creacionales:

- **Flexibilidad**: Desacoplan creación de uso
- **Extensibilidad**: Agregar nuevos tipos sin modificar código existente
- **Encapsulación**: Ocultan detalles de creación
- **Control**: Centralizan lógica de instanciación

## Práctica integradora

Después de resolver los mini ejercicios de cada patrón, conviene cerrar la familia con los [ejercicios integradores de creacionales](ejercicios_integradores.md).

:::{tip}
La consigna no es "nombrar un patrón", sino justificar por qué una estrategia de creación es mejor que otra en ese problema.
:::
