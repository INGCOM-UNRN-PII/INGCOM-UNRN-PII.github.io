---
title: "Guía Básica: Diagramas de Clases en PlantUML"
description: Introducción a la creación de diagramas de clases UML usando PlantUML.
---

# Guía Básica: Diagramas de Clases en PlantUML

:::{seealso}
Editor online para crear el dibujo y exportar imágenes disponible en [PlantUML Online](https://www.plantuml.com/plantuml/uml/)
:::

## ¿Qué es un Diagrama de Clases?

Un **diagrama de clases** es una representación visual de las clases, atributos, métodos y relaciones entre clases en un sistema orientado a objetos. Es parte esencial de UML (Lenguaje Unificado de Modelado) y se usa para el diseño y la documentación de software.

---

## ¿Qué es PlantUML?

**PlantUML** es una herramienta que permite crear diagramas a partir de texto plano. Es ideal para programadores porque permite versionar diagramas y mantenerlos junto al código fuente.

---

## Estructura básica de un archivo PlantUML

```{code} plantuml
:caption: Estructura básica
@startuml

' Aquí van las definiciones de clases, interfaces y relaciones

@enduml
```

Todo diagrama comienza con `@startuml` y termina con `@enduml`.

---

## Cómo definir una clase

```{code} plantuml
:caption: Definición de clase con atributos y métodos
class Persona {
  -nombre: String
  -edad: int
  +hablar(): void
}
```

:::{table} Convenciones de visibilidad
:label: tbl-visibilidad-plantuml

| Símbolo | Visibilidad |
| :---: | :--- |
| `+` | público (`public`) |
| `-` | privado (`private`) |
| `#` | protegido (`protected`) |

:::

---

## Interfaces

```{code} plantuml
:caption: Definición de interfaz
interface Volador {
  +volar(): void
}
```

---

## Herencia y relaciones

### Herencia (generalización)

```{code} plantuml
:caption: Herencia con extends
class Animal
class Perro extends Animal
```

O bien con flechas:

```plantuml
Animal <|-- Perro
```

### Implementación de interfaz

```plantuml
Volador <|.. Pájaro
```

### Asociación

```plantuml
Persona --> Direccion
```

### Composición

```plantuml
Auto *-- Motor
```

### Agregación

```plantuml
Departamento o-- Profesor
```

---

## Clase abstracta

```{code} plantuml
:caption: Clase abstracta
abstract class Figura {
  +area(): float
}
```

---

## Ejemplo completo

```{code} plantuml
:caption: Diagrama de clases completo
@startuml

abstract class Figura {
  +area(): float
}

class Rectangulo {
  -base: float
  -altura: float
  +area(): float
}

class Circulo {
  -radio: float
  +area(): float
}

Figura <|-- Rectangulo
Figura <|-- Circulo

@enduml
```

---

## Estilos mínimos (opcional)

```{code} plantuml
:caption: Configuración de estilos
skinparam classAttributeIconSize 0
skinparam shadowing true
skinparam classFontColor DarkGreen
```

Estos parámetros cambian el estilo visual del diagrama.

---

## Buenas prácticas

:::{tip}
- Usá nombres significativos para clases y atributos.
- No satures el diagrama con detalles irrelevantes.
- Agrupá clases relacionadas con `package` si el sistema es grande.
:::

```{code} plantuml
:caption: Agrupación con packages
package "Sistema de Usuarios" {
  class Usuario
  class Rol
}
```
