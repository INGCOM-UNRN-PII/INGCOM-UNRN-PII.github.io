---
title: "Guía: Diagramas de Clases en PlantUML"
description: Guía práctica para crear diagramas de clases UML usando PlantUML, desde lo esencial hasta publicación.
---

# Guía: Diagramas de Clases en PlantUML

Un **diagrama de clases** es una representación visual de las clases, atributos, métodos y relaciones en un sistema orientado a objetos. **PlantUML** es una herramienta que permite crear estos diagramas a partir de texto plano, lo que los hace versionables en Git.

Esta guía está diseñada para que puedas empezar a graficar de inmediato.

---

## Parte 1: Lo Mínimo para Empezar

Para empezar a crear diagramas sin instalar nada, podés usar el editor online oficial:

:::{seealso} Editor Online
[PlantUML Web Server](https://www.plantuml.com/plantuml/uml/) - Escribí el código a la izquierda y exportá la imagen generada.
:::

Todo diagrama de PlantUML comienza con `@startuml` y termina con `@enduml`.

### 1. Definir una Clase

Podés definir clases, atributos y métodos usando sintaxis muy similar a Java:

```{code} plantuml
:caption: Definición de clase
@startuml
class Persona {
  -String nombre
  -int edad
  +void hablar()
}
@enduml
```

*Los símbolos `-`, `+` y `#` representan visibilidad privada, pública y protegida respectivamente.*

### 2. Interfaces y Clases Abstractas

```{code} plantuml
:caption: Interfaces y Abstractas
@startuml
interface Volador {
  +volar(): void
}

abstract class Figura {
  {abstract} +dibujar(): void
}
@enduml
```

### 3. Relaciones entre clases

Las flechas indican cómo se relacionan las clases.

```{code} plantuml
:caption: Tipos de Relaciones
@startuml
' Herencia (es un)
Animal <|-- Perro

' Implementación de interfaz
Volador <|.. Pajaro

' Asociación (conoce a)
Persona --> Direccion

' Composición (tiene un - fuerte)
Auto *-- Motor

' Agregación (tiene un - débil)
Departamento o-- Profesor
@enduml
```

### 4. Cardinalidad (Multiplicidad)

Podés indicar cuántos elementos participan en la relación añadiendo cadenas de texto entre comillas sobre la flecha.

```{code} plantuml
:caption: Cardinalidad
@startuml
Empresa "1" -- "1..*" Empleado : contrata
Empleado "1..*" -- "*" Proyecto : trabaja en
@enduml
```

---

## Parte 2: Uso Intermedio y Organización

Cuando tus diagramas empiecen a crecer, vas a necesitar organizarlos.

### Disposición (Layout)

PlantUML intenta organizar automáticamente, pero podés forzar la dirección:

```{code} plantuml
@startuml
left to right direction
class Izquierda
class Derecha
Izquierda --> Derecha
@enduml
```

### Notas y Comentarios

Ayudan a documentar visualmente el diagrama.

```{code} plantuml
@startuml
class Usuario
note right of Usuario
  Esta clase representa
  a un usuario del sistema.
end note
@enduml
```

### Agrupación por Paquetes

```{code} plantuml
@startuml
package "Sistema de Facturación" {
  class Factura
  class Detalle
}
@enduml
```

---

## Parte 3: Publicación y GitHub (Avanzado)

Es común querer mostrar los diagramas directamente en los archivos Markdown (`README.md`) de GitHub.

:::{warning} Fragilidad de servicios externos
GitHub **no renderiza** PlantUML nativamente. Los métodos mostrados a continuación dependen de servidores externos (como `www.plantuml.com`). Si ese servidor se cae, cambia sus políticas, o tu red bloquea la URL, los diagramas dejarán de verse en GitHub.
:::

### Método 1: Proxy de PlantUML (Recomendado para repos públicos)

Podés usar el servidor público de PlantUML para que lea el archivo `.puml` de tu repositorio y devuelva una imagen.

En tu archivo Markdown de GitHub, usá esta sintaxis:

```markdown
![Diagrama](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/TU_USUARIO/TU_REPO/TU_RAMA/ruta/al/archivo.puml)
```

**Atención a los placeholders que debés reemplazar:**
*   `TU_USUARIO`: Tu nombre de usuario en GitHub (ej: `mrtin`).
*   `TU_REPO`: El nombre de tu repositorio (ej: `tpi-2025`).
*   `TU_RAMA`: La rama donde está el archivo (usualmente `main`).
*   `ruta/al/archivo.puml`: La ruta completa desde la raíz del repo (ej: `diagramas/clases.puml`).

### Método 2: Exportar imágenes localmente (Más robusto)

Si trabajás en repositorios privados (el proxy público no podrá acceder a tu código) o querés evitar depender de servidores externos, lo mejor es exportar la imagen localmente.

1. Instalá un plugin de PlantUML en IntelliJ IDEA o VS Code.
2. Exportá el diagrama como `.svg` o `.png` y guardalo en tu repositorio (ej: `docs/imagenes/clases.svg`).
3. Referencialo de forma nativa en Markdown:

```markdown
![Diagrama de Clases](./docs/imagenes/clases.svg)
```

---

## Ejercicios propuestos

```{exercise}
:label: ej-plantuml-1
Creá un diagrama de clases usando el editor online oficial para un sistema de e-commerce que incluya: `Producto`, `Carrito`, `Usuario`, `Pedido` y `MetodoPago` (como interfaz). Incluí cardinalidades apropiadas.
```

```{exercise}
:label: ej-plantuml-2
Modelá el patrón de diseño Observer, incluyendo las interfaces `Sujeto` y `Observador`, y al menos dos clases concretas que las implementen.
```