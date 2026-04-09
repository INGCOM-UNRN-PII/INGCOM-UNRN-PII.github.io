---
title: "OOP 2: Encapsulamiento y Relaciones entre Objetos"
subtitle: "Protegiendo el Estado y Modelando Colaboraciones"
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

---

(relaciones-entre-objetos)=
## Relaciones entre Objetos

En sistemas orientados a objetos, raramente los objetos existen de forma aislada. Los objetos **colaboran** para resolver problemas complejos, y estas colaboraciones se modelan mediante **relaciones**.

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
- {ref}`java-modificadores-acceso`: Implementación de encapsulamiento con `private` y `public`
- {ref}`java-asociaciones`: Cómo implementar asociaciones, composición y agregación en código

:::{seealso}
- {ref}`fundamentos-de-la-programacion-orientada-a-objetos`: Conceptos básicos de OOP
- Guía de PlantUML para diagramas de clases
:::

---

(ejercicios-oop2)=
## Ejercicios

```{exercise}
:label: ej-identificar-relacion-1
Analizá el siguiente escenario e identificá el tipo de relación (asociación, composición o agregación):

"Un pedido en una tienda online contiene productos. Los productos existen en el catálogo de la tienda independientemente de los pedidos."

¿Qué tipo de relación existe entre `Pedido` y `Producto`?
```

```{exercise}
:label: ej-identificar-relacion-2
Para cada par de clases, indicá si la relación debería ser **composición** o **agregación**. Justificá tu respuesta.

a) `Computadora` – `Procesador`  
b) `Playlist` – `Cancion`  
c) `Documento` – `Parrafo`  
d) `Proyecto` – `Desarrollador`
```

```{exercise}
:label: ej-cardinalidad-uml
Dibujá el diagrama UML con cardinalidad correcta para:

"Un estudiante puede estar inscripto en múltiples cursos. Un curso debe tener al menos 5 estudiantes y como máximo 40."
```

```{exercise}
:label: ej-encapsulamiento-invariante
Diseñá una clase `Rectangulo` que debe mantener la siguiente invariante:

- El ancho y alto siempre deben ser valores positivos (mayor que 0)

¿Qué atributos necesitás? ¿Qué métodos expondrías? ¿Cómo garantizarías la invariante?
```

```{exercise}
:label: ej-asociacion-bidireccional
Analizá si necesitás asociación bidireccional o unidireccional en:

"Un libro pertenece a una editorial. Queremos poder preguntarle a un libro cuál es su editorial, y también consultar todos los libros que publicó una editorial."

¿Es necesario que `Libro` conozca a `Editorial` **y** que `Editorial` conozca sus libros? ¿O hay una forma mejor de modelarlo?
```
