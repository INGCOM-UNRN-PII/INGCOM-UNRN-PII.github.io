---
title: "Guía Completa: Diagramas de Clases en PlantUML"
description: Guía exhaustiva para crear diagramas de clases UML usando PlantUML, desde conceptos básicos hasta técnicas avanzadas.
---

# Guía Completa: Diagramas de Clases en PlantUML

:::{seealso}
Editor online para crear el dibujo y exportar imágenes disponible en [PlantUML Online](https://www.plantuml.com/plantuml/uml/)
:::

:::{tip}
PlantUML se integra con la mayoría de IDEs (VS Code, IntelliJ, Eclipse) mediante extensiones, lo que permite previsualizar los diagramas mientras se codifican.
:::

## ¿Qué es un Diagrama de Clases?

Un **diagrama de clases** es una representación visual de las clases, atributos, métodos y relaciones entre clases en un sistema orientado a objetos. Es parte esencial de UML (Lenguaje Unificado de Modelado) y se usa para el diseño y la documentación de software.

Un diagrama de clases muestra:

- **Clases**: Los bloques constructivos del sistema
- **Atributos**: Las propiedades o datos de cada clase
- **Métodos**: Las operaciones que puede realizar cada clase
- **Relaciones**: Cómo las clases se conectan entre sí

---

## ¿Qué es PlantUML?

**PlantUML** es una herramienta que permite crear diagramas a partir de texto plano. Es ideal para programadores porque permite versionar diagramas y mantenerlos junto al código fuente.

### Ventajas de usar PlantUML

- **Versionable**: Los archivos de texto plano se integran con Git
- **Reproducible**: El mismo código genera siempre el mismo diagrama
- **Portable**: No requiere software propietario de diseño
- **Automatizable**: Se puede integrar en pipelines de CI/CD

---

## Estructura básica de un archivo PlantUML

```{code} plantuml
:caption: Estructura básica
@startuml

' Aquí van las definiciones de clases, interfaces y relaciones
' Los comentarios empiezan con comilla simple

@enduml
```

Todo diagrama comienza con `@startuml` y termina con `@enduml`. Opcionalmente, se puede nombrar el diagrama:

```{code} plantuml
:caption: Diagrama con nombre
@startuml mi_diagrama

class MiClase

@enduml
```

---

## Cómo definir una clase

### Sintaxis básica

```{code} plantuml
:caption: Definición de clase con atributos y métodos
class Persona {
  -nombre: String
  -edad: int
  +hablar(): void
}
```

### Sintaxis alternativa (estilo Java)

PlantUML también acepta la notación de tipo al estilo Java:

```{code} plantuml
:caption: Notación estilo Java
class Persona {
  -String nombre
  -int edad
  +void hablar()
}
```

### Miembros estáticos y finales

```{code} plantuml
:caption: Miembros estáticos y constantes
class Configuracion {
  {static} -instancia: Configuracion
  {static} +obtenerInstancia(): Configuracion
  
  {field} MAX_CONEXIONES: int = 100
}
```

Los miembros estáticos se muestran subrayados en el diagrama. Para constantes, usá `{field}` o simplemente escribí el valor.

:::{table} Convenciones de visibilidad
:label: tbl-visibilidad-plantuml

| Símbolo | Visibilidad | Descripción |
| :---: | :--- | :--- |
| `+` | público (`public`) | Accesible desde cualquier clase |
| `-` | privado (`private`) | Solo accesible desde la misma clase |
| `#` | protegido (`protected`) | Accesible desde subclases |
| `~` | paquete (`package`) | Accesible dentro del mismo paquete |

:::

---

## Interfaces

### Definición básica

```{code} plantuml
:caption: Definición de interfaz
interface Volador {
  +volar(): void
}
```

### Interfaz con múltiples métodos

```{code} plantuml
:caption: Interfaz completa
interface Comparable<T> {
  +compareTo(otro: T): int
  +equals(obj: Object): boolean
}
```

### Notación abreviada

Para interfaces simples, se puede usar notación de círculo:

```{code} plantuml
:caption: Notación de círculo (lollipop)
interface Serializable

class Documento
Documento -|> Serializable
```

---

## Herencia y relaciones

PlantUML ofrece varias formas de representar las relaciones entre clases. Las flechas pueden escribirse de izquierda a derecha o viceversa.

### Herencia (generalización)

La herencia representa una relación "es un". Se denota con una flecha con triángulo vacío.

```{code} plantuml
:caption: Herencia con extends
class Animal
class Perro extends Animal
```

O bien con flechas:

```{code} plantuml
:caption: Herencia con notación de flecha
@startuml
Animal <|-- Perro
Animal <|-- Gato
Animal <|-- Pajaro
@enduml
```

### Implementación de interfaz

La implementación muestra que una clase cumple con el contrato de una interfaz. Se representa con línea punteada y triángulo vacío.

```{code} plantuml
:caption: Implementación de interfaz
@startuml
interface Volador {
  +volar(): void
}

class Pajaro {
  -nombre: String
  +volar(): void
}

Volador <|.. Pajaro
@enduml
```

### Asociación

La asociación representa una relación "usa a" o "conoce a". Se denota con una flecha simple.

```{code} plantuml
:caption: Asociación simple
@startuml
class Persona {
  -nombre: String
}

class Direccion {
  -calle: String
  -numero: int
}

Persona --> Direccion : vive en
@enduml
```

### Composición

La composición es una relación "tiene un" fuerte donde el componente no puede existir sin el contenedor. Se representa con un diamante relleno.

```{code} plantuml
:caption: Composición (relación fuerte)
@startuml
class Auto {
  -patente: String
}

class Motor {
  -cilindrada: int
}

Auto *-- Motor : tiene
@enduml
```

:::{note}
En la composición, si se destruye el `Auto`, también se destruye el `Motor`. El motor no tiene sentido fuera del contexto del auto.
:::

### Agregación

La agregación es una relación "tiene un" débil donde el componente puede existir independientemente. Se representa con un diamante vacío.

```{code} plantuml
:caption: Agregación (relación débil)
@startuml
class Departamento {
  -nombre: String
}

class Profesor {
  -legajo: int
}

Departamento o-- Profesor : emplea
@enduml
```

:::{note}
En la agregación, un `Profesor` puede existir sin pertenecer a un `Departamento`, y puede cambiar de departamento.
:::

### Dependencia

La dependencia indica que una clase usa otra temporalmente (por ejemplo, como parámetro de método). Se representa con línea punteada.

```{code} plantuml
:caption: Dependencia
@startuml
class Impresora {
  +imprimir(doc: Documento): void
}

class Documento {
  -contenido: String
}

Impresora ..> Documento : usa
@enduml
```

### Tabla resumen de relaciones

:::{table} Tipos de relaciones UML en PlantUML
:label: tbl-relaciones-plantuml

| Relación | Símbolo | Descripción | Ejemplo |
| :--- | :---: | :--- | :--- |
| Herencia | `<\|--` | "es un" | `Animal <\|-- Perro` |
| Implementación | `<\|..` | implementa interfaz | `Volador <\|.. Pajaro` |
| Asociación | `-->` | "conoce a" | `Persona --> Direccion` |
| Composición | `*--` | "tiene un" (fuerte) | `Auto *-- Motor` |
| Agregación | `o--` | "tiene un" (débil) | `Departamento o-- Profesor` |
| Dependencia | `..>` | "usa" temporalmente | `Impresora ..> Documento` |

:::

---

## Clase abstracta

Las clases abstractas se declaran con la palabra clave `abstract` y se muestran en itálica.

```{code} plantuml
:caption: Clase abstracta
abstract class Figura {
  #nombre: String
  +area(): float
  +perimetro(): float
  {abstract} +dibujar(): void
}
```

:::{tip}
Los métodos abstractos se marcan con `{abstract}` y no tienen implementación. Las subclases deben implementarlos.
:::

---

## Cardinalidad (multiplicidad)

La cardinalidad indica cuántas instancias participan en una relación.

```{code} plantuml
:caption: Relaciones con cardinalidad
@startuml
class Empresa {
  -nombre: String
}

class Empleado {
  -legajo: int
}

class Proyecto {
  -codigo: String
}

Empresa "1" -- "1..*" Empleado : contrata
Empleado "1..*" -- "*" Proyecto : trabaja en
@enduml
```

:::{table} Notaciones de cardinalidad
:label: tbl-cardinalidad

| Notación | Significado |
| :---: | :--- |
| `1` | Exactamente uno |
| `0..1` | Cero o uno (opcional) |
| `*` | Cero o más |
| `1..*` | Uno o más |
| `n..m` | Entre n y m |

:::

---

## Enumeraciones

Las enumeraciones se definen con la palabra clave `enum`.

```{code} plantuml
:caption: Definición de enum
@startuml
enum DiaSemana {
  LUNES
  MARTES
  MIERCOLES
  JUEVES
  VIERNES
  SABADO
  DOMINGO
}

enum EstadoPedido {
  PENDIENTE
  PROCESANDO
  ENVIADO
  ENTREGADO
  CANCELADO
}

class Pedido {
  -fecha: Date
  -estado: EstadoPedido
}

Pedido --> EstadoPedido
@enduml
```

---

## Genéricos (Templates)

PlantUML soporta la notación de genéricos.

```{code} plantuml
:caption: Clases genéricas
@startuml
class Lista<T> {
  -elementos: T[]
  +agregar(elemento: T): void
  +obtener(indice: int): T
  +tamanio(): int
}

class Mapa<K, V> {
  +poner(clave: K, valor: V): void
  +obtener(clave: K): V
  +contieneClave(clave: K): boolean
}

class ListaEnteros {
}

Lista <|-- ListaEnteros : <<bind>> T::Integer
@enduml
```

---

## Notas y comentarios en el diagrama

Las notas agregan documentación visual al diagrama.

```{code} plantuml
:caption: Notas en el diagrama
@startuml
class Usuario {
  -id: int
  -email: String
  +validarEmail(): boolean
}

note left of Usuario : Esta clase representa\na un usuario del sistema

note right of Usuario::validarEmail
  Verifica que el email
  tenga formato válido
end note

note "Nota flotante" as N1
Usuario .. N1
@enduml
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

---

## Ejemplo completo: Sistema de Biblioteca

Este ejemplo integra todos los conceptos vistos en un diagrama realista.

```{code} plantuml
:caption: Sistema de gestión de biblioteca
@startuml
skinparam classAttributeIconSize 0

' Enumeraciones
enum EstadoLibro {
  DISPONIBLE
  PRESTADO
  EN_REPARACION
  PERDIDO
}

enum TipoUsuario {
  ESTUDIANTE
  DOCENTE
  ADMINISTRATIVO
}

' Interfaces
interface Prestable {
  +prestar(usuario: Usuario): boolean
  +devolver(): void
  +estaDisponible(): boolean
}

' Clases abstractas
abstract class Persona {
  #dni: String
  #nombre: String
  #email: String
  +{abstract} validar(): boolean
}

' Clases concretas
class Usuario {
  -tipo: TipoUsuario
  -fechaAlta: Date
  -prestamosActivos: int
  +puedeRetirar(): boolean
  +validar(): boolean
}

class Bibliotecario {
  -legajo: int
  -turno: String
  +registrarPrestamo(p: Prestamo): void
  +validar(): boolean
}

class Libro {
  -isbn: String
  -titulo: String
  -anioPublicacion: int
  -estado: EstadoLibro
  +prestar(usuario: Usuario): boolean
  +devolver(): void
  +estaDisponible(): boolean
}

class Autor {
  -nombre: String
  -nacionalidad: String
}

class Editorial {
  -nombre: String
  -pais: String
}

class Prestamo {
  -fechaPrestamo: Date
  -fechaDevolucion: Date
  -fechaLimite: Date
  +estaVencido(): boolean
  +calcularMulta(): float
}

class Biblioteca {
  -nombre: String
  -direccion: String
  +buscarLibro(titulo: String): Libro[]
  +registrarUsuario(u: Usuario): void
}

' Relaciones
Persona <|-- Usuario
Persona <|-- Bibliotecario

Prestable <|.. Libro

Libro --> EstadoLibro
Usuario --> TipoUsuario

Libro "1..*" -- "1..*" Autor : escrito por
Libro "*" -- "1" Editorial : publicado por

Prestamo "1" --> "1" Libro : sobre
Prestamo "1" --> "1" Usuario : a
Prestamo "1" --> "1" Bibliotecario : registrado por

Biblioteca "1" *-- "*" Libro : contiene
Biblioteca "1" o-- "*" Usuario : atiende

@enduml
```

---

## Control de disposición (Layout)

PlantUML intenta organizar automáticamente los elementos, pero se puede influir en la disposición.

### Dirección de las flechas

```{code} plantuml
:caption: Flechas y disposición
@startuml
' Flechas horizontales (doble guión)
A -- B
C -- D

' Flechas verticales (guión simple, orientación por defecto)
E - F

' Dirección explícita
left to right direction

class Izquierda
class Derecha
Izquierda --> Derecha
@enduml
```

### Ocultar elementos

```{code} plantuml
:caption: Ocultar secciones
@startuml
hide empty members
hide circle

class Simple {
  -dato: int
}
@enduml
```

---

## Estereotipos y decoradores

Los estereotipos añaden información semántica a las clases.

```{code} plantuml
:caption: Estereotipos personalizados
@startuml
class ControladorUsuario <<Controller>> {
  +listar(): List<Usuario>
  +crear(datos: Map): Usuario
}

class ServicioEmail <<Service>> {
  +enviar(destinatario: String, mensaje: String): void
}

class RepositorioUsuario <<Repository>> {
  +guardar(u: Usuario): void
  +buscarPorId(id: int): Usuario
}

class Usuario <<Entity>> {
  -id: int
  -nombre: String
}

ControladorUsuario --> ServicioEmail
ControladorUsuario --> RepositorioUsuario
RepositorioUsuario --> Usuario
@enduml
```

---

## Patrones de diseño comunes

### Patrón Singleton

```{code} plantuml
:caption: Patrón Singleton
@startuml
class Configuracion {
  {static} -instancia: Configuracion
  -propiedades: Map<String, String>
  -Configuracion()
  {static} +obtenerInstancia(): Configuracion
  +obtenerPropiedad(clave: String): String
}

note right of Configuracion
  Constructor privado
  impide instanciación externa
end note
@enduml
```

### Patrón Observer

```{code} plantuml
:caption: Patrón Observer
@startuml
interface Observador {
  +actualizar(evento: Evento): void
}

interface Sujeto {
  +agregarObservador(o: Observador): void
  +eliminarObservador(o: Observador): void
  +notificar(): void
}

class SensorTemperatura {
  -temperatura: float
  -observadores: List<Observador>
  +agregarObservador(o: Observador): void
  +eliminarObservador(o: Observador): void
  +notificar(): void
  +setTemperatura(t: float): void
}

class PanelDisplay {
  -ultimaLectura: float
  +actualizar(evento: Evento): void
  +mostrar(): void
}

class AlertaEmail {
  -umbral: float
  +actualizar(evento: Evento): void
}

Sujeto <|.. SensorTemperatura
Observador <|.. PanelDisplay
Observador <|.. AlertaEmail

SensorTemperatura --> "*" Observador : notifica a
@enduml
```

### Patrón Strategy

```{code} plantuml
:caption: Patrón Strategy
@startuml
interface EstrategiaOrdenamiento {
  +ordenar(lista: List<T>): List<T>
}

class OrdenamientoBurbuja {
  +ordenar(lista: List<T>): List<T>
}

class OrdenamientoRapido {
  +ordenar(lista: List<T>): List<T>
}

class OrdenamientoMerge {
  +ordenar(lista: List<T>): List<T>
}

class Contexto {
  -estrategia: EstrategiaOrdenamiento
  +setEstrategia(e: EstrategiaOrdenamiento): void
  +ejecutarOrdenamiento(lista: List<T>): List<T>
}

EstrategiaOrdenamiento <|.. OrdenamientoBurbuja
EstrategiaOrdenamiento <|.. OrdenamientoRapido
EstrategiaOrdenamiento <|.. OrdenamientoMerge

Contexto --> EstrategiaOrdenamiento : usa
@enduml
```

---

## Estilos avanzados

### Colores y temas

```{code} plantuml
:caption: Personalización de colores
@startuml
skinparam class {
  BackgroundColor<<Entity>> LightYellow
  BackgroundColor<<Service>> LightBlue
  BackgroundColor<<Repository>> LightGreen
  BorderColor Black
  ArrowColor DarkBlue
}

skinparam stereotypeCBackgroundColor<<Entity>> Yellow
skinparam stereotypeCBackgroundColor<<Service>> Blue

class Usuario <<Entity>> {
  -id: int
}

class ServicioUsuario <<Service>> {
  +buscar(id: int): Usuario
}

class RepositorioUsuario <<Repository>> {
  +guardar(u: Usuario): void
}

ServicioUsuario --> RepositorioUsuario
RepositorioUsuario --> Usuario
@enduml
```

### Temas predefinidos

PlantUML incluye varios temas listos para usar:

```{code} plantuml
:caption: Uso de temas
@startuml
!theme cerulean

class Usuario {
  -nombre: String
}

class Perfil {
  -avatar: String
}

Usuario --> Perfil
@enduml
```

Algunos temas disponibles: `cerulean`, `materia`, `sketchy`, `blueprint`, `plain`.

---

## Ejercicios propuestos

```{exercise}
:label: ej-plantuml-1
Creá un diagrama de clases para un sistema de e-commerce que incluya: `Producto`, `Carrito`, `Usuario`, `Pedido` y `MetodoPago` (como interfaz). Incluí cardinalidades apropiadas.
```

```{exercise}
:label: ej-plantuml-2
Modelá un sistema de gestión de hospital con: `Persona` (abstracta), `Paciente`, `Medico`, `Enfermero`, `Cita`, `Especialidad` (enum). Usá herencia y composición donde corresponda.
```

```{exercise}
:label: ej-plantuml-3
Diseñá el patrón Factory Method para crear diferentes tipos de documentos (`PDF`, `Word`, `HTML`) usando una interfaz `Documento` y una clase abstracta `CreadorDocumento`.
```

---

## Referencias y recursos adicionales

:::{seealso}
- [Documentación oficial de PlantUML](https://plantuml.com/class-diagram)
- [Guía de referencia rápida](https://plantuml.com/guide)
- [Galería de ejemplos](https://real-world-plantuml.com/)
:::
