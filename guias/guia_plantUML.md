📘 Guía Básica: Diagramas de Clases en [PlantUML](https://www.plantuml.com/plantuml/uml/)
===============================================

Editor online para crear el dibujo y exportar imágenes disponible en [PlantUML Online](https://www.plantuml.com/plantuml/uml/)

🧩 ¿Qué es un Diagrama de Clases?
---------------------------------

Un **diagrama de clases** es una representación visual de las clases, atributos, métodos y relaciones entre clases en un sistema orientado a objetos. Es parte esencial de UML (Lenguaje Unificado de Modelado) y se usa para el diseño y la documentación de software.

* * * * *

🛠️ ¿Qué es PlantUML?
---------------------

**PlantUML** es una herramienta que permite crear diagramas a partir de texto plano. Es ideal para programadores porque permite versionar diagramas y mantenerlos junto al código fuente.

* * * * *

📄 Estructura básica de un archivo PlantUML
-------------------------------------------

```
@startuml

' Aquí van las definiciones de clases, interfaces y relaciones

@enduml

```

Todo diagrama comienza con `@startuml` y termina con `@enduml`.

* * * * *

🧱 Cómo definir una clase
-------------------------

```
class Persona {
  -nombre: String
  -edad: int
  +hablar(): void
}

```

**Convenciones**:

-   `+` público (`public`)

-   `-` privado (`private`)

-   `#` protegido (`protected`)

* * * * *

🏛️ Interfaces
--------------

```
interface Volador {
  +volar(): void
}

```

* * * * *

📎 Herencia y relaciones
------------------------

### 🔁 Herencia (generalización)

```
class Animal
class Perro extends Animal

```

O bien con flechas:

```
Animal <|-- Perro

```

### ⚙️ Implementación de interfaz

```
Volador <|.. Pájaro

```

### 🧩 Asociación

```
Persona --> Direccion

```

### 💙 Composición

```
Auto *-- Motor

```

### 💛 Agregación

```
Departamento o-- Profesor

```

* * * * *

🧠 Clase abstracta
------------------

```
abstract class Figura {
  +area(): float
}

```

* * * * *

🌟 Ejemplo completo
-------------------

```
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

* * * * *

🎨 Estilos mínimos (opcional)
-----------------------------

```
skinparam classAttributeIconSize 0
skinparam shadowing true
skinparam classFontColor DarkGreen

```

Estos parámetros cambian el estilo visual del diagrama.

* * * * *

✅ Buenas prácticas
------------------

-   Usa nombres significativos para clases y atributos.

-   No satures el diagrama con detalles irrelevantes.

-   Agrupa clases relacionadas con `package` si el sistema es grande.

```
package "Sistema de Usuarios" {
  class Usuario
  class Rol
}

```
