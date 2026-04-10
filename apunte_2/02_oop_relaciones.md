---
title: "2: Encapsulamiento y Relaciones entre Objetos"
subtitle: "Protegiendo el Estado y Modelando Colaboraciones"
subject: Programación Orientada a Objetos
---

(oop2-encapsulamiento-relaciones)=
# OOP 2: Encapsulamiento y Relaciones entre Objetos

En el capítulo anterior ({ref}`fundamentos-de-la-programacion-orientada-a-objetos`) nos enfocamos en el **análisis conceptual** de un dominio: utilizamos {ref}`la-heuristica-linguistica` para identificar objetos a partir de sustantivos, definir su comportamiento a partir de verbos, y aplicar los {ref}`filtro-de-abstraccion` y {ref}`filtro-de-complejidad` para refinar el modelo.

En este capítulo profundizamos en dos conceptos fundamentales de OOP:

1. **Encapsulamiento**: El principio de proteger el estado interno de los objetos
2. **Relaciones**: Cómo los objetos colaboran entre sí (Asociación, Composición, Agregación)

:::{important}
Este capítulo se enfoca en los **conceptos** y el **modelado**. La sintaxis específica de Java para implementar estos conceptos se aborda en el {ref}`java-sintaxis-clases`.
:::

:::{admonition} Objetivos de Aprendizaje
:class: tip

Al finalizar este capítulo, serás capaz de:

1. Explicar qué es el encapsulamiento y por qué es fundamental en OOP
2. Identificar invariantes y diseñar clases que las protejan
3. Distinguir entre asociación, composición y agregación
4. Leer y escribir diagramas UML con cardinalidad
5. Decidir qué tipo de relación usar en diferentes escenarios
:::

---

(encapsulamiento-concepto)=
## Encapsulamiento: Protegiendo el Estado

(que-es-encapsulamiento)=
### ¿Qué es el Encapsulamiento?

El **encapsulamiento** es uno de los cuatro pilares fundamentales de la Programación Orientada a Objetos (junto con herencia, polimorfismo y abstracción). Se trata del principio de **ocultar los detalles internos** de un objeto y exponer únicamente lo necesario para que otros objetos interactúen con él.

:::{admonition} Definición Formal
:class: note
**Encapsulamiento** es el mecanismo mediante el cual se agrupan datos (estado) y operaciones (comportamiento) dentro de una unidad cohesiva (la clase), restringiendo el acceso directo al estado interno y exponiendo únicamente una interfaz pública controlada.
:::

(analogia-encapsulamiento)=
#### Analogía: El Control Remoto

Considerá un control remoto de televisión:

- **Estado interno oculto**: Circuitos, baterías, chips, señales infrarrojas
- **Interfaz pública**: Botones claramente etiquetados (volumen, canal, encendido)

No necesitás conocer cómo funcionan los circuitos internos para usar el control. Los botones son la **interfaz pública** que te permite **interactuar** con la funcionalidad sin acceder directamente al estado interno.

Del mismo modo, un objeto bien encapsulado:
- Oculta sus datos internos (atributos privados)
- Expone métodos públicos que permiten interactuar con él de forma controlada

(fusion-datos-comportamiento)=
### Fusión de Datos y Comportamiento

El primer aspecto del encapsulamiento es la **fusión** de datos y comportamiento en una misma unidad. En el paradigma estructurado, datos y funciones están separados; en POO, están unificados en el objeto.

**Comparación visual:**

```
Paradigma estructurado:              Paradigma OO:

┌──────────────┐                    ┌──────────────────────┐
│    DATOS     │                    │       OBJETO         │
│              │                    │  ┌────────────────┐  │
│  struct      │                    │  │     DATOS      │  │
│  Persona {   │                    │  │   nombre       │  │
│    nombre;   │                    │  │   edad         │  │
│    edad;     │                    │  └────────────────┘  │
│  }           │                    │  ┌────────────────┐  │
└──────────────┘                    │  │ COMPORTAMIENTO │  │
                                    │  │   saludar()    │  │
┌──────────────┐                    │  │   cumplirAnios │  │
│  FUNCIONES   │                    │  └────────────────┘  │
│              │                    └──────────────────────┘
│  saludar(p)  │                    
│  cumplir(p)  │                    El objeto es una unidad
└──────────────┘                    indivisible
                                    
Datos y funciones                   
están separados                     
```

Esta fusión tiene consecuencias profundas:

1. **Los datos conocen sus operaciones**: No hay funciones "sueltas" que operen sobre datos ajenos
2. **Cohesión alta**: Todo lo relacionado con una responsabilidad está en un solo lugar
3. **Menor acoplamiento**: No hay dependencias globales a funciones externas

:::{note}
En C (paradigma estructurado), tendrías:
```c
struct Persona {
    char nombre[50];
    int edad;
};

void saludar(struct Persona* p) { ... }
void cumplirAnios(struct Persona* p) { ... }
```

En Java (POO), todo está unificado:
```java
class Persona {
    private String nombre;
    private int edad;
    
    public void saludar() { ... }
    public void cumplirAnios() { ... }
}
```
:::

(ocultamiento-informacion)=
### Ocultamiento de Información

El segundo aspecto del encapsulamiento es igualmente fundamental: **ocultar los detalles de implementación**.

**Analogía Extendida: El Televisor**

Cuando usás un televisor, interactuás con él a través de una **interfaz simple**: botones de encendido, volumen, canales. No necesitás saber:
- Cómo funciona el circuito interno
- Qué señales electrónicas se procesan
- Cómo se iluminan los píxeles

El televisor **oculta** toda esa complejidad y te expone solo lo que necesitás para usarlo. Si el fabricante cambia la tecnología interna (de LCD a OLED, por ejemplo), tu forma de usar el televisor no cambia.

```
                 ┌─────────────────────────────────────┐
                 │            TELEVISOR                │
                 │                                     │
Usuario ───────► │  INTERFAZ PÚBLICA                   │
                 │  ├── encender()                     │
                 │  ├── apagar()                       │
                 │  ├── subirVolumen()                 │
                 │  └── cambiarCanal()                 │
                 │                                     │
                 │  ─────────────────────────────      │
                 │                                     │
                 │  IMPLEMENTACIÓN OCULTA              │
                 │  ├── procesarSeñal()                │
                 │  ├── decodificarVideo()             │
                 │  ├── ajustarBrillo()                │
                 │  └── sincronizarAudio()             │
                 │                                     │
                 └─────────────────────────────────────┘
```

:::{tip}
El encapsulamiento permite que el objeto **controle** cómo se accede y modifica su estado. En lugar de permitir que cualquiera modifique directamente los atributos, el objeto expone métodos que realizan validaciones y mantienen la consistencia.
:::

(por-que-encapsular)=
### ¿Por Qué Encapsular?

El encapsulamiento no es una restricción arbitraria; tiene beneficios concretos y medibles en el desarrollo de software.

(control-invariantes)=
#### 1. Control de Invariantes

Una **invariante** es una condición que debe mantenerse siempre verdadera durante toda la vida del objeto.

**Ejemplo: Cuenta Bancaria**

```
Clase: CuentaBancaria
Invariante: El saldo nunca debe ser negativo
```

Si permitimos acceso directo al atributo `saldo`, cualquier parte del código podría violarlo:

```
// Mal diseño (sin encapsulamiento)
cuenta.saldo = -1000;  // ❌ Viola la invariante
```

Con encapsulamiento, el objeto **controla** cómo se modifica el saldo:

```
// Buen diseño (con encapsulamiento)
cuenta.retirar(1500);  // ✓ El método verifica que haya fondos
```

El método `retirar()` puede validar:
- Si hay saldo suficiente
- Si el monto es positivo
- Si la cuenta no está bloqueada

(mantenimiento-codigo)=
#### 2. Facilita el Mantenimiento

Cuando el estado es privado, podés cambiar la **representación interna** sin afectar al código que usa la clase.

**Ejemplo: Representación de Fecha**

Podríamos almacenar una fecha de dos formas diferentes:

**Versión 1:**
```
Atributos: dia, mes, anio
```

**Versión 2:**
```
Atributo: timestampUnix (long)
```

Si exponemos métodos públicos (`obtenerAnio()`, `obtenerMes()`, etc.), podemos cambiar de Versión 1 a Versión 2 sin que nadie que use la clase lo note.

(reduccion-acoplamiento)=
#### 3. Reduce el Acoplamiento

El **acoplamiento** mide cuánto depende una clase de los detalles internos de otra.

- **Alto acoplamiento** (malo): Código frágil, cambios en cascada
- **Bajo acoplamiento** (bueno): Código modular, cambios localizados

El encapsulamiento reduce el acoplamiento porque las clases solo dependen de **interfaces públicas**, no de detalles internos.

(seguridad-consistencia)=
#### 4. Seguridad y Consistencia

Al controlar el acceso al estado, garantizamos que:

- Los datos se modifican solo de formas válidas
- No se crean estados inconsistentes o ilegales
- Se pueden aplicar reglas de negocio en un solo lugar

:::{tip}
**Principio General**: Si un objeto necesita mantener su estado consistente, **el propio objeto** debe ser el responsable de modificarlo. No delegues esa responsabilidad a quien lo usa.
:::

(ejemplo-encapsulamiento-completo)=
### Ejemplo Completo: Termostato

Para consolidar los conceptos de encapsulamiento, analicemos un ejemplo más completo.

**Escenario**: Un termostato inteligente que controla la temperatura de una habitación.

**Invariantes del termostato:**

1. La temperatura objetivo debe estar entre 15°C y 30°C
2. El modo solo puede ser "calefacción", "refrigeración" o "apagado"
3. Si el termostato está apagado, no puede estar calentando ni enfriando

**Sin encapsulamiento (mal diseño):**

```
Termostato {
    temperaturaObjetivo: 25      // Cualquiera podría poner -100
    modo: "calefacción"          // Cualquiera podría poner "hola"
    estaActivo: true             // Inconsistente con modo "apagado"
}

// Código externo puede hacer:
termostato.temperaturaObjetivo = -50;  // ❌ Viola invariante 1
termostato.modo = "explosión";         // ❌ Viola invariante 2
termostato.modo = "apagado";
termostato.estaActivo = true;          // ❌ Viola invariante 3
```

**Con encapsulamiento (buen diseño):**

```
Termostato {
    // Estado privado (no accesible directamente)
    - temperaturaObjetivo: 25
    - modo: "calefacción"
    - estaActivo: true
    
    // Interfaz pública (métodos controlados)
    + ajustarTemperatura(nuevaTemp)
        → Si nuevaTemp < 15 o > 30: rechazar
        → Si no: actualizar temperaturaObjetivo
    
    + cambiarModo(nuevoModo)
        → Si nuevoModo no es válido: rechazar
        → Si nuevoModo es "apagado": estaActivo = false
        → Si no: actualizar modo, estaActivo = true
    
    + obtenerEstado()
        → Retorna información del estado actual
}
```

**Beneficios observados:**

| Aspecto | Sin encapsulamiento | Con encapsulamiento |
| :--- | :--- | :--- |
| Validación | No hay | Cada método valida |
| Consistencia | Puede romperse | Siempre garantizada |
| Cambios futuros | Afectan a todos | Localizados en la clase |
| Responsabilidad | Del usuario | Del objeto |

:::{note}
Observá que el termostato encapsulado **sabe cómo protegerse**. No depende de que quien lo use "se porte bien". Esta es la esencia del encapsulamiento: **el objeto es responsable de su propia integridad**.
:::

---

(relaciones-entre-objetos)=
## Relaciones entre Objetos

En sistemas orientados a objetos, raramente los objetos existen de forma aislada. Los objetos **colaboran** para resolver problemas complejos, y estas colaboraciones se modelan mediante **relaciones**.

:::{admonition} ¿Por qué son importantes las relaciones?
:class: note

Las relaciones definen **cómo se estructura** un sistema:
- Qué objetos conocen a cuáles otros
- Quién es responsable de qué
- Cómo fluye la información
- Qué pasa cuando un objeto se destruye

Un diseño con relaciones bien definidas es **más fácil de entender, mantener y extender**.
:::

(notacion-uml-cardinalidad)=
### Notación UML y Cardinalidad

El **Lenguaje de Modelado Unificado** (UML, *Unified Modeling Language*) proporciona una notación gráfica estándar para representar relaciones entre clases.

(representacion-asociaciones-uml)=
#### Representación de Asociaciones

En UML, una asociación se representa como una **línea** que conecta dos clases. La línea puede tener:

- **Nombre de la relación** (opcional): Describe la naturaleza de la asociación
- **Cardinalidad** en cada extremo: Indica cuántas instancias participan
- **Roles** (opcional): Nombres que las clases tienen en el contexto de la relación

**Ejemplo básico:**

```
Persona ──────── Direccion
         vive en
```

Esto se lee: "Una Persona **vive en** una Dirección".

(cardinalidad-multiplicidad)=
#### Cardinalidad (Multiplicidad)

La **cardinalidad** especifica cuántas instancias de una clase pueden estar relacionadas con instancias de otra clase. Se anota en los extremos de la línea de asociación.

:::{table} Notaciones de cardinalidad
:label: tbl-cardinalidad-uml

| Notación | Significado | Ejemplo |
| :---: | :--- | :--- |
| `1` | Exactamente uno | Una persona tiene exactamente un DNI |
| `0..1` | Cero o uno (opcional) | Un empleado tiene 0 o 1 cónyuge registrado |
| `*` o `0..*` | Cero o más | Una empresa tiene cero o más empleados |
| `1..*` | Uno o más | Un auto tiene uno o más neumáticos |
| `n..m` | Entre n y m | Un curso tiene entre 5 y 40 estudiantes |

:::

**Ejemplo con cardinalidad:**

```
Empresa  1 ────────── * Empleado
        "emplea"
```

Se lee: "Una Empresa (1) emplea a cero o más (*) Empleados".

(como-leer-cardinalidad)=
#### Cómo Leer la Cardinalidad

La cardinalidad se lee **desde el otro extremo** de la relación:

```
A  cardX ──── cardY  B
```

- **Desde A hacia B**: Cada instancia de A está relacionada con `cardY` instancias de B
- **Desde B hacia A**: Cada instancia de B está relacionada con `cardX` instancias de A

**Ejemplo práctico:**

```
Autor  1..* ──────── 1..* Libro
       "escribe"
```

- Un Autor escribe 1 o más Libros
- Un Libro es escrito por 1 o más Autores (coautoría)

---

(tipos-de-relaciones)=
## Tipos de Relaciones

Existen tres tipos principales de relaciones entre objetos, cada una con semántica diferente:

1. **Asociación**: Relación general, conexión débil
2. **Composición**: Relación fuerte, el todo "contiene" las partes
3. **Agregación**: Relación débil, el contenedor agrupa elementos

(asociacion-concepto)=
### Asociación

La **asociación** es la relación más general entre dos clases. Indica que existe alguna conexión o conocimiento entre ellas, pero no implica propiedad ni ciclo de vida compartido.

(caracteristicas-asociacion)=
#### Características de la Asociación

- **Independencia de ciclo de vida**: Los objetos pueden existir independientemente
- **Relación semántica**: Expresa conocimiento, uso o colaboración
- **Puede ser bidireccional o unidireccional**

**Ejemplos de asociación:**

- `Estudiante` – `Curso`: Un estudiante se inscribe en cursos
- `Medico` – `Paciente`: Un médico atiende a pacientes
- `Cliente` – `Pedido`: Un cliente realiza pedidos

(notacion-uml-asociacion)=
#### Notación UML

```
ClaseA ──────── ClaseB
```

Línea simple sin decoración especial.

**Ejemplo:**

```
Persona  1 ────────── 0..1 Pasaporte
         "tiene"
```

Una persona puede tener 0 o 1 pasaporte. El pasaporte puede existir antes de ser asignado a una persona.

(asociacion-direccionalidad)=
#### Direccionalidad de la Asociación

Las asociaciones pueden ser:

**Unidireccional**: Solo una clase conoce a la otra.

```
Pedido ──────────► Producto
```

El pedido conoce qué productos contiene, pero el producto no sabe en qué pedidos está.

**Bidireccional**: Ambas clases se conocen mutuamente.

```
Estudiante ◄──────► Curso
```

El estudiante sabe en qué cursos está inscripto, y el curso sabe qué estudiantes tiene.

:::{tip}
**Preferí asociaciones unidireccionales** siempre que sea posible. Son más simples de mantener y generan menor acoplamiento.
:::

---

(composicion-concepto)=
### Composición

La **composición** es una relación **fuerte** de tipo "tiene-un" donde:

- El **todo** (contenedor) es responsable del ciclo de vida de las **partes**
- Las partes **no tienen sentido** fuera del contexto del todo
- Si se destruye el todo, se destruyen las partes

:::{admonition} Definición Formal
:class: note
**Composición** es una relación de agregación fuerte donde las partes no pueden existir independientemente del todo. El contenedor crea y destruye las partes.
:::

(analogia-composicion)=
#### Analogía: El Auto y su Motor

Un auto **está compuesto** por un motor, ruedas, carrocería, etc.

- Si destruís el auto (lo desarmás completamente), el motor ya no es "motor de ese auto"
- El motor fue creado **para** ese auto específico
- No tiene sentido hablar del motor sin hablar del auto

(representacion-uml-composicion)=
#### Representación UML

La composición se representa con un **diamante relleno (♦)** en el extremo del contenedor:

```
Auto ♦────── Motor
     ♦────── Carroceria
     ♦────── 4 Rueda
```

El diamante negro indica propiedad fuerte.

(caracteristicas-composicion)=
#### Características de la Composición

1. **Ciclo de vida dependiente**: Cuando se destruye el todo, se destruyen las partes
2. **Creación por el contenedor**: El todo típicamente crea las partes en su constructor
3. **Cardinalidad del todo**: Una parte pertenece a **exactamente un** todo
4. **Semántica fuerte**: "No puede existir sin"

**Ejemplos de composición:**

- `Universidad` ♦─ `Facultad`: Las facultades existen porque existe la universidad
- `Libro` ♦─ `Pagina`: Las páginas son parte integral del libro
- `Casa` ♦─ `Habitacion`: Las habitaciones no existen fuera de la casa

(composicion-ejemplo-detallado)=
#### Ejemplo Detallado: Factura y Líneas de Detalle

Una factura **está compuesta** por líneas de detalle:

```
┌─────────────────────────────────┐
│          Factura                │
│  - numero: "F-2024-001"         │
│  - fecha: 2024-03-15            │
│  - cliente: "Juan Pérez"        │
│                                 │
│  ♦─── LineaDetalle              │
│       - producto: "Laptop"      │
│       - cantidad: 1             │
│       - precioUnitario: 1500.00 │
│                                 │
│  ♦─── LineaDetalle              │
│       - producto: "Mouse"       │
│       - cantidad: 2             │
│       - precioUnitario: 25.00   │
│                                 │
│  + calcularTotal(): 1550.00     │
└─────────────────────────────────┘
```

**¿Por qué es composición?**

1. Las líneas de detalle **no existen sin la factura** — no tiene sentido una línea suelta
2. La factura **crea** las líneas cuando se agregan productos
3. Si se **elimina** la factura, las líneas desaparecen con ella
4. Una línea pertenece a **exactamente una** factura

**Ciclo de vida:**

```
Crear factura → Agregar líneas → La factura existe con sus líneas
                                            │
                                            ▼
                                    Eliminar factura
                                            │
                                            ▼
                            Las líneas se eliminan automáticamente
```

:::{warning}
**Diferencia crucial**: En composición, las partes se **crean dentro** del constructor del todo y se **destruyen** cuando el todo se destruye. No se pasan partes preexistentes.
:::

---

(agregacion-concepto)=
### Agregación

La **agregación** es una relación **débil** de tipo "tiene-un" donde:

- El contenedor agrupa o contiene objetos
- Los objetos contenidos **pueden existir independientemente**
- El ciclo de vida no está acoplado

:::{admonition} Definición Formal
:class: note
**Agregación** es una relación de pertenencia débil donde los componentes pueden existir independientemente del contenedor. El contenedor recibe referencias a objetos ya existentes.
:::

(analogia-agregacion)=
#### Analogía: Departamento y Profesores

Un departamento universitario **tiene** profesores, pero:

- Los profesores existían antes de unirse al departamento
- Si el departamento se disuelve, los profesores siguen existiendo
- Un profesor puede cambiar de departamento

(representacion-uml-agregacion)=
#### Representación UML

La agregación se representa con un **diamante vacío (◊)** en el extremo del contenedor:

```
Departamento ◊────── * Profesor
```

El diamante blanco indica una relación de pertenencia más laxa.

(caracteristicas-agregacion)=
#### Características de la Agregación

1. **Ciclo de vida independiente**: Las partes pueden existir antes y después del contenedor
2. **Recepción de referencias**: El contenedor recibe objetos ya creados (típicamente en constructor o setters)
3. **Cardinalidad flexible**: Una parte puede pertenecer a múltiples contenedores
4. **Semántica débil**: "Agrupa a" o "Contiene temporalmente"

**Ejemplos de agregación:**

- `Equipo` ◊─ `Jugador`: Los jugadores pueden cambiar de equipo
- `Biblioteca` ◊─ `Libro`: Los libros existían antes y pueden moverse entre bibliotecas
- `PlayList` ◊─ `Cancion`: Las canciones pueden estar en múltiples playlists

(agregacion-ejemplo-detallado)=
#### Ejemplo Detallado: Equipo de Fútbol

Un equipo de fútbol **agrupa** jugadores:

```
┌─────────────────────────────────┐
│        Equipo                   │
│  - nombre: "River Plate"        │
│  - fundacion: 1901              │
│                                 │
│  ◊─── Jugador                   │
│       - nombre: "Enzo Pérez"    │
│       - posicion: "Mediocampo"  │
│       - añoNacimiento: 1986     │
│                                 │
│  ◊─── Jugador                   │
│       - nombre: "Nacho Fernández│
│       - posicion: "Mediocampo"  │
│       - añoNacimiento: 1990     │
│                                 │
│  + obtenerPlantilla()           │
└─────────────────────────────────┘
```

**¿Por qué es agregación?**

1. Los jugadores **existían antes** de unirse al equipo (nacieron, crecieron, jugaron en otros equipos)
2. Los jugadores **pueden cambiar** de equipo (pases, transferencias)
3. Si el equipo **se disuelve**, los jugadores **siguen existiendo** (pueden retirarse o ir a otro equipo)
4. Un jugador puede incluso pertenecer a **múltiples contextos** (equipo + selección nacional)

**Ciclo de vida:**

```
Jugador existe ──► Se une al equipo ──► El equipo lo tiene
      │                                        │
      │                                        ▼
      │                               Equipo se disuelve
      │                                        │
      ▼                                        ▼
Jugador sigue existiendo ◄────────── Jugador sigue existiendo
(puede ir a otro equipo)             (puede ir a otro equipo)
```

:::{important}
**La pregunta clave sigue siendo:**

> ¿Los jugadores existen independientemente del equipo?

**Sí** → Por eso es **agregación** (◊)

Si los jugadores fueran robots creados específicamente para ese equipo y destruidos cuando el equipo se disuelve, sería **composición** (♦).
:::

:::{tip}
**Pregunta clave** para distinguir composición de agregación:

> ¿Tiene sentido que la parte exista sin el todo?

- **Sí** → Agregación (◊)
- **No** → Composición (♦)
:::

---

(composicion-vs-agregacion)=
## Comparación: Composición vs Agregación

:::{table} Diferencias clave entre Composición y Agregación
:label: tbl-composicion-agregacion

| Aspecto | Composición (♦) | Agregación (◊) |
| :--- | :--- | :--- |
| **Ciclo de vida** | Acoplado (se crea y destruye junto) | Independiente (preexiste y sobrevive) |
| **Creación** | El todo crea las partes | Las partes se pasan al todo |
| **Destrucción** | Se destruyen con el todo | Sobreviven al todo |
| **Exclusividad** | Una parte pertenece a un solo todo | Una parte puede pertenecer a varios contenedores |
| **Semántica** | "No existe sin" | "Pertenece temporalmente a" |
| **Fuerza** | Relación fuerte | Relación débil |

:::

(cuando-usar-cada-una)=
### ¿Cuándo Usar Cada Una?

**Usá Composición cuando:**

- Las partes no tienen sentido fuera del contexto del todo
- El todo es responsable de crear las partes
- La destrucción del todo implica la destrucción de las partes
- Ejemplo: `Persona` ♦─ `Corazon`, `Casa` ♦─ `Cimientos`

**Usá Agregación cuando:**

- Las partes pueden existir independientemente
- Las partes se crean fuera del contenedor y se le pasan
- El contenedor es solo un "agrupador" o "coordinador"
- Ejemplo: `Curso` ◊─ `Estudiante`, `Carpeta` ◊─ `Archivo`

:::{important}
En la práctica, la diferencia entre composición y agregación a veces es sutil y depende del contexto del dominio. Lo importante es **documentar la decisión** y ser consistente.
:::

---

(patron-todo-parte)=
## El Patrón Todo-Parte

Tanto la composición como la agregación son instancias del **patrón estructural Todo-Parte**, que modela la relación entre un objeto complejo (todo) y sus componentes (partes).

(beneficios-patron-todo-parte)=
### Beneficios del Patrón

1. **Modularidad**: El todo delega responsabilidades a las partes
2. **Reusabilidad**: Las partes pueden reutilizarse en diferentes contextos (especialmente en agregación)
3. **Jerarquía conceptual**: Modela naturalmente estructuras jerárquicas

(ejemplo-mixto)=
### Ejemplo Mixto: Sistema de Biblioteca

Un sistema de biblioteca puede combinar composición y agregación:

```
Biblioteca
    ♦─── Seccion  (Composición: las secciones son creadas por la biblioteca)
    ◊─── Libro    (Agregación: los libros preexisten y pueden moverse)

Libro
    ♦─── Pagina   (Composición: las páginas son parte del libro)
    ◊─── Autor    (Agregación: los autores existen independientemente)
```

**Interpretación:**

- Una `Biblioteca` **crea** sus `Secciones` (Infantil, Referencia, etc.)
- Una `Biblioteca` **contiene** `Libros` que fueron adquiridos
- Un `Libro` **está compuesto** por `Paginas`
- Un `Libro` **está asociado** a `Autores` (que pueden escribir múltiples libros)

(caso-estudio-sistema-universidad)=
### Caso de Estudio: Sistema Universitario

Analicemos un sistema más complejo para practicar la identificación de relaciones.

**Requerimiento:**

> "Una universidad tiene facultades. Cada facultad ofrece carreras. Los estudiantes se inscriben en una carrera y cursan materias. Los profesores dictan materias y pertenecen a un departamento dentro de una facultad."

**Paso 1: Identificar clases principales**

- Universidad
- Facultad
- Carrera
- Estudiante
- Materia
- Profesor
- Departamento

**Paso 2: Analizar relaciones**

| Relación | Tipo | Justificación |
| :--- | :---: | :--- |
| Universidad – Facultad | ♦ | Las facultades no existen sin la universidad |
| Facultad – Departamento | ♦ | Los departamentos son parte de la facultad |
| Facultad – Carrera | ♦ | Las carreras pertenecen a una facultad específica |
| Carrera – Materia | ♦ | Las materias son parte del plan de estudios |
| Estudiante – Carrera | Asoc | Un estudiante puede cambiarse de carrera |
| Estudiante – Materia | Asoc | Se inscribe/cursa/aprueba materias |
| Profesor – Departamento | ◊ | El profesor existe independientemente |
| Profesor – Materia | Asoc | El profesor "dicta" materias (no las posee) |

**Paso 3: Diagrama resultante**

```
Universidad
    ♦──── * Facultad
              ♦──── * Departamento
              │            ◊──── * Profesor ─────┐
              │                                   │
              ♦──── * Carrera                    │ dicta
                       ♦──── * Materia ◄────────┘
                       │
                       │ cursa
                       │
                 Estudiante
```

**Paso 4: Cardinalidades**

```
Universidad  1 ────♦──── 1..* Facultad
Facultad     1 ────♦──── 1..* Departamento
Facultad     1 ────♦──── 1..* Carrera
Carrera      1 ────♦──── 5..* Materia
Departamento 1 ────◊──── 1..* Profesor
Profesor     1..* ────── 1..* Materia
Estudiante   * ────────── 1   Carrera
Estudiante   * ────────── *   Materia
```

**Lecturas:**
- Una universidad tiene 1 o más facultades
- Una facultad tiene 1 o más departamentos y 1 o más carreras
- Una carrera tiene al menos 5 materias
- Un departamento agrupa 1 o más profesores
- Un profesor dicta 1 o más materias; una materia puede ser dictada por varios profesores
- Un estudiante está inscripto en exactamente 1 carrera
- Un estudiante cursa 0 o más materias; una materia puede tener 0 o más estudiantes inscriptos

:::{tip}
**Proceso recomendado:**

1. Identificar clases del dominio
2. Para cada par de clases relacionadas, preguntar: "¿La parte existe sin el todo?"
3. Definir cardinalidades pensando en casos reales
4. Verificar consistencia del modelo
:::

---

(referencias-cruzadas)=
## Referencias Cruzadas y Asociaciones Bidireccionales

En algunos casos, dos objetos necesitan conocerse mutuamente. Esto se llama **asociación bidireccional**.

(cuando-son-necesarias)=
### ¿Cuándo Son Necesarias?

**Ejemplo: Pedido y Cliente**

- El pedido necesita saber **quién** lo realizó (Cliente)
- El cliente necesita consultar **sus** pedidos

Esto crea una navegación en ambas direcciones:

```
Cliente  1 ←──→ * Pedido
```

(problemas-asociaciones-bidireccionales)=
### Problemas de las Asociaciones Bidireccionales

1. **Complejidad**: Hay que mantener la consistencia en ambas direcciones
2. **Acoplamiento**: Las dos clases dependen mutuamente
3. **Riesgo de inconsistencia**: Si actualizás una dirección y no la otra

:::{warning}
**Regla general**: **Evitá las asociaciones bidireccionales** siempre que sea posible. Usá navegación unidireccional y consultá datos a través de servicios o repositorios.
:::

(cuando-son-aceptables)=
### Cuándo Son Aceptables

Las asociaciones bidireccionales son aceptables cuando:

- La navegación en ambos sentidos es una **necesidad del dominio**
- Podés garantizar la consistencia (típicamente mediante métodos que actualizan ambos lados)
- El beneficio supera el costo en complejidad

(ejemplo-bidireccional)=
#### Ejemplo: Implementación Correcta

Si decidís que necesitás bidireccionalidad entre `Cliente` y `Pedido`, tenés que mantener la consistencia:

```
Clase Cliente {
    - pedidos: Lista<Pedido>
    
    + agregarPedido(pedido) {
        pedidos.agregar(pedido)
        pedido.establecerCliente(this)  // Mantiene consistencia
    }
    
    + removerPedido(pedido) {
        pedidos.remover(pedido)
        pedido.establecerCliente(null)  // Mantiene consistencia
    }
}

Clase Pedido {
    - cliente: Cliente
    
    // Método interno, no público
    ~ establecerCliente(nuevoCliente) {
        this.cliente = nuevoCliente
    }
}
```

**Patrón recomendado:** Un solo lado de la relación (el "dueño") maneja las modificaciones. El otro lado tiene métodos de acceso restringido.

(alternativa-unidireccional)=
#### Alternativa: Evitar la Bidireccionalidad

En muchos casos, podés evitar la bidireccionalidad usando un **repositorio** o **servicio**:

```
Clase RepositorioPedidos {
    + buscarPorCliente(cliente): Lista<Pedido> {
        // Busca en la base de datos o colección
        return pedidos.filtrar(p -> p.cliente == cliente)
    }
}
```

Así, `Cliente` no necesita conocer sus pedidos directamente; los consultás a través del repositorio cuando los necesitás.

:::{important}
**Regla práctica:**

- En **modelos de dominio pequeños**: La bidireccionalidad puede ser aceptable
- En **sistemas grandes**: Preferí repositorios y servicios para consultas
- **Siempre:** Documentá tu decisión y las reglas de consistencia
:::

---

(resumen-relaciones)=
## Resumen

### Encapsulamiento

- **Oculta el estado interno** y expone solo una interfaz pública
- **Protege invariantes** del objeto
- **Reduce acoplamiento** entre clases
- **Facilita mantenimiento** al permitir cambios internos sin afectar clientes

### Asociación

- Relación general de "conoce a" o "usa a"
- **Ciclo de vida independiente**
- No implica propiedad
- Se representa con línea simple en UML

### Composición (♦)

- Relación fuerte "tiene-un"
- **El todo crea y destruye las partes**
- Las partes no existen fuera del todo
- Diamante negro en UML

### Agregación (◊)

- Relación débil "contiene a"
- **Las partes preexisten y sobreviven al contenedor**
- El contenedor solo agrupa
- Diamante blanco en UML

### Cardinalidad

- Especifica **cuántas instancias** participan en la relación
- Notaciones: `1`, `0..1`, `*`, `1..*`, `n..m`
- Se lee desde el **otro extremo** de la relación

---

(proximos-pasos-oop2)=
## Próximos Pasos

En este capítulo viste los **conceptos fundamentales** de encapsulamiento y relaciones entre objetos. Para implementar estos conceptos en Java, consultá:

- {ref}`java-sintaxis-clases`: Sintaxis de clases, atributos, constructores y métodos

:::{seealso}
- {ref}`fundamentos-de-la-programacion-orientada-a-objetos`: Conceptos básicos de OOP
- [Guía de PlantUML](guia_plantUML.md) para diagramas de clases
:::

---

(ejercicios-oop2)=
## Ejercicios

### Identificación de Relaciones

```{exercise}
:label: ej-identificar-relacion-1
Analizá el siguiente escenario e identificá el tipo de relación (asociación, composición o agregación):

"Un pedido en una tienda online contiene productos. Los productos existen en el catálogo de la tienda independientemente de los pedidos."

¿Qué tipo de relación existe entre `Pedido` y `Producto`?
```

```{solution} ej-identificar-relacion-1
:class: dropdown

**Respuesta: Agregación (◊)**

**Justificación:**

1. Los productos **preexisten** al pedido — están en el catálogo antes de que alguien los pida
2. Si se cancela el pedido, los productos **siguen existiendo** en el catálogo
3. Un producto puede estar en **múltiples pedidos** simultáneamente
4. El pedido no "crea" los productos, solo los **referencia**

**Diagrama:**
```
Pedido ◊────── * Producto
```

**Nota:** El pedido contiene **líneas de pedido** (cantidad + producto), pero la relación con el producto en sí es de agregación. Las líneas de pedido serían composición del pedido.
```

```{exercise}
:label: ej-identificar-relacion-2
Para cada par de clases, indicá si la relación debería ser **composición** o **agregación**. Justificá tu respuesta.

a) `Computadora` – `Procesador`  
b) `Playlist` – `Cancion`  
c) `Documento` – `Parrafo`  
d) `Proyecto` – `Desarrollador`
```

```{solution} ej-identificar-relacion-2
:class: dropdown

**a) `Computadora` – `Procesador`: Composición (♦)**

- El procesador fue diseñado/elegido **para** esa computadora específica
- Si destruís la computadora, el procesador pierde su contexto de uso
- Generalmente un procesador no se "reutiliza" en otra computadora

Sin embargo, esto es **debatible**: si considerás que los procesadores pueden venderse como repuestos, sería agregación. Depende del contexto del sistema.

**b) `Playlist` – `Cancion`: Agregación (◊)**

- Las canciones **preexisten** a la playlist
- Una canción puede estar en **múltiples playlists**
- Si elimino la playlist, las canciones **siguen existiendo**

**c) `Documento` – `Parrafo`: Composición (♦)**

- Los párrafos se **crean dentro** del documento
- Un párrafo no tiene sentido fuera de su documento
- Si elimino el documento, los párrafos desaparecen

**d) `Proyecto` – `Desarrollador`: Agregación (◊)**

- Los desarrolladores **existen antes** de unirse al proyecto
- Un desarrollador puede trabajar en **múltiples proyectos**
- Si termina el proyecto, los desarrolladores **siguen existiendo**
```

### Cardinalidad y UML

```{exercise}
:label: ej-cardinalidad-uml
Dibujá el diagrama UML con cardinalidad correcta para:

"Un estudiante puede estar inscripto en múltiples cursos. Un curso debe tener al menos 5 estudiantes y como máximo 40."
```

```{solution} ej-cardinalidad-uml
:class: dropdown

**Diagrama:**

```
Estudiante  * ────────── 5..40  Curso
             "inscripto en"
```

**Lectura:**

- Un estudiante está inscripto en 0 o más cursos (`*`)
- Un curso tiene entre 5 y 40 estudiantes (`5..40`)

**Alternativa con clase de asociación** (si necesitamos guardar información de la inscripción):

```
Estudiante  * ────────── 5..40  Curso
                 │
                 ▼
           Inscripcion
           - fecha
           - estado
```
```

```{exercise}
:label: ej-encapsulamiento-invariante
Diseñá una clase `Rectangulo` que debe mantener la siguiente invariante:

- El ancho y alto siempre deben ser valores positivos (mayor que 0)

¿Qué atributos necesitás? ¿Qué métodos expondrías? ¿Cómo garantizarías la invariante?
```

```{solution} ej-encapsulamiento-invariante
:class: dropdown

**Diseño de la clase:**

```
Clase Rectangulo {
    // Atributos privados
    - ancho: double
    - alto: double
    
    // Constructor que valida
    + Rectangulo(ancho, alto) {
        validarDimension(ancho, "ancho")
        validarDimension(alto, "alto")
        this.ancho = ancho
        this.alto = alto
    }
    
    // Métodos de consulta (getters válidos en este caso)
    + obtenerAncho(): double { return ancho }
    + obtenerAlto(): double { return alto }
    
    // Métodos de modificación con validación
    + cambiarAncho(nuevoAncho) {
        validarDimension(nuevoAncho, "ancho")
        this.ancho = nuevoAncho
    }
    
    + cambiarAlto(nuevoAlto) {
        validarDimension(nuevoAlto, "alto")
        this.alto = nuevoAlto
    }
    
    // Métodos de comportamiento
    + calcularArea(): double {
        return ancho * alto
    }
    
    + calcularPerimetro(): double {
        return 2 * (ancho + alto)
    }
    
    // Método privado de validación
    - validarDimension(valor, nombre) {
        if (valor <= 0) {
            lanzar error "El " + nombre + " debe ser mayor a 0"
        }
    }
}
```

**Garantías:**

1. Los atributos son **privados** → no se pueden modificar directamente
2. El **constructor valida** → no se puede crear un rectángulo inválido
3. Los **métodos de modificación validan** → no se puede violar la invariante después
4. La lógica de validación está **centralizada** → fácil de mantener
```

```{exercise}
:label: ej-asociacion-bidireccional
Analizá si necesitás asociación bidireccional o unidireccional en:

"Un libro pertenece a una editorial. Queremos poder preguntarle a un libro cuál es su editorial, y también consultar todos los libros que publicó una editorial."

¿Es necesario que `Libro` conozca a `Editorial` **y** que `Editorial` conozca sus libros? ¿O hay una forma mejor de modelarlo?
```

```{solution} ej-asociacion-bidireccional
:class: dropdown

**Análisis:**

| Caso de uso | Dirección necesaria |
| :--- | :--- |
| Dado un libro, obtener su editorial | Libro → Editorial |
| Dada una editorial, obtener sus libros | Editorial → Libro |

Parece que necesitamos **bidireccionalidad**, pero hay alternativas.

**Opción 1: Bidireccional simple**

```
Libro {
    - editorial: Editorial
    + obtenerEditorial(): Editorial
}

Editorial {
    - libros: Lista<Libro>
    + obtenerLibros(): Lista<Libro>
}
```

**Problema:** Hay que mantener consistencia en ambos lados.

**Opción 2: Unidireccional + Repositorio (recomendada)**

```
Libro {
    - editorial: Editorial
    + obtenerEditorial(): Editorial
}

Editorial {
    // No conoce directamente sus libros
}

RepositorioLibros {
    + buscarPorEditorial(editorial): Lista<Libro>
}
```

**Ventajas:**
- `Libro` → `Editorial` es la relación natural (el libro sabe quién lo publicó)
- La consulta inversa se hace a través del repositorio
- No hay riesgo de inconsistencia
- El modelo es más simple

**Recomendación:** Opción 2, especialmente si usás una base de datos donde las consultas inversas son eficientes.
```

### Ejercicio Integrador

```{exercise}
:label: ej-integrador-hospital
Analizá el siguiente requerimiento y diseñá el modelo de clases con relaciones:

"Un hospital tiene varios pisos. Cada piso tiene habitaciones. Las habitaciones pueden ser simples (1 cama) o compartidas (2-4 camas). Los pacientes son internados en camas específicas. Los médicos atienden a pacientes y pueden tener varias especialidades. Las enfermeras están asignadas a pisos específicos."

Incluí:
1. Identificación de clases
2. Tipo de relación entre cada par de clases
3. Cardinalidades
4. Justificación de las decisiones
```

```{solution} ej-integrador-hospital
:class: dropdown

**1. Clases identificadas:**

- Hospital
- Piso
- Habitacion
- Cama
- Paciente
- Medico
- Especialidad
- Enfermera

**2. Análisis de relaciones:**

| Relación | Tipo | Justificación |
| :--- | :---: | :--- |
| Hospital – Piso | ♦ | Los pisos son parte estructural del hospital |
| Piso – Habitacion | ♦ | Las habitaciones son parte del piso |
| Habitacion – Cama | ♦ | Las camas son parte de la habitación |
| Cama – Paciente | Asoc | El paciente existe independientemente |
| Medico – Paciente | Asoc | Relación de "atiende", no de posesión |
| Medico – Especialidad | ◊ | Las especialidades preexisten |
| Piso – Enfermera | ◊ | Las enfermeras pueden cambiar de piso |

**3. Diagrama con cardinalidades:**

```
Hospital  1 ──♦── 1..* Piso  1 ──◊── 1..* Enfermera
                    │
                    ♦
                    │
                  1..*
              Habitacion
                    │
                    ♦
                    │
                  1..4
                 Cama ──────── 0..1 Paciente
                                      │
                                      │ atiende
                                      │
                               1..* Medico ◊── 1..* Especialidad
```

**4. Justificaciones detalladas:**

**Hospital ♦── Piso:**
- Un piso no tiene sentido fuera del hospital
- El hospital define cuántos pisos tiene desde su construcción

**Piso ♦── Habitacion:**
- Las habitaciones son parte del piso
- Si "destruís" el piso (remodelación total), las habitaciones desaparecen

**Habitacion ♦── Cama:**
- Las camas están fijas en la habitación
- Cardinalidad 1..4 porque puede ser simple (1) o compartida (2-4)

**Cama ── Paciente:**
- Asociación simple: el paciente "ocupa" la cama temporalmente
- Cardinalidad 0..1: una cama puede estar vacía o tener un paciente

**Piso ◊── Enfermera:**
- Las enfermeras existen independientemente del piso
- Pueden ser reasignadas a otros pisos
- Cardinalidad 1..* en piso: al menos una enfermera por piso

**Medico ── Paciente:**
- Relación de "atiende", no posesión
- Un médico atiende varios pacientes
- Un paciente puede ser atendido por varios médicos

**Medico ◊── Especialidad:**
- Las especialidades (Cardiología, Pediatría) preexisten
- Un médico puede tener varias especialidades
- Las especialidades no desaparecen si el médico deja el hospital
```
