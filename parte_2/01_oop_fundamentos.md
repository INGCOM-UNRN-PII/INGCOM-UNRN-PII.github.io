---
title: "Fundamentos OOP"
description:
  Introducción conceptual al paradigma orientado a objetos. De la programación 
  estructurada a la fusión de datos y comportamiento. Principios fundamentales
  y heurísticas de análisis.
---

(fundamentos-de-la-programacion-orientada-a-objetos)=
# Fundamentos de la Programación Orientada a Objetos

Este capítulo presenta los fundamentos conceptuales del paradigma orientado a objetos (POO). Se explora la transición desde el paradigma estructurado, se introducen los conceptos esenciales que definen este paradigma, y se desarrollan las heurísticas que permiten identificar objetos a partir de los requerimientos de un sistema.

:::{note}
Este capítulo se enfoca en los **conceptos del paradigma**, independientes de cualquier lenguaje de programación particular. Los detalles de implementación en Java se abordan en {ref}`java-sintaxis-clases`, mientras que las relaciones entre objetos se profundizan en {ref}`oop2-encapsulamiento-relaciones`.
:::

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, serás capaz de:

1. Comprender la transición del paradigma estructurado al orientado a objetos
2. Identificar los conceptos fundamentales de POO: objetos, clases, atributos y métodos
3. Aplicar la heurística lingüística para modelar dominios
4. Distinguir entre objetos del dominio y del sistema
5. Evaluar abstracciones usando los filtros de relevancia y complejidad
6. Modelar sistemas básicos usando burbujas conceptuales
:::

:::{note} Hoja de ruta del capítulo

**Prerrequisitos.** Conviene haber leído [el material inmediatamente anterior](indice.md) para llegar con el hilo de la parte fresco.

**Desarrollo.** El desarrollo del capítulo aparece en las secciones que siguen. Conviene recorrerlas en orden y volver al resumen antes de pasar al siguiente tema.
:::

(introduccion-la-transicion-de-paradigma)=
## Introducción: La transición de paradigma

(el-paradigma-estructurado)=
### El paradigma estructurado (Un pequeño flashback a Programación 1)

En Programación 1 se trabajó con el lenguaje C y el **paradigma estructurado** (también conocido como paradigma procedural o imperativo). Este paradigma se caracteriza por una clara **separación entre datos y código**:

- **Datos**: Estructurados en registros (`struct`), arreglos, tipos primitivos.
- **Código**: Organizado en funciones y procedimientos que operan sobre esos datos.

Esta separación implica que los datos son **entidades pasivas** que esperan ser manipuladas por funciones externas. El siguiente ejemplo en C ilustra este modelo:

```c
// Definición de la estructura de datos
struct Persona {
    char nombre[50];
    int edad;
    char nacionalidad[30];
};

// Función separada que opera sobre la estructura
void saludar(struct Persona p) {
    printf("Hola, soy %s\n", p.nombre);
}

// Uso
struct Persona juan = {"Juan", 25, "Argentina"};
saludar(juan);  // Función externa que recibe los datos
```

En este modelo:
- `Persona` es simplemente un **contenedor de datos**.
- `saludar()` es una **función independiente** que recibe esos datos como parámetro.
- No existe una conexión lógica o estructural entre la definición de `Persona` y las operaciones que se pueden realizar sobre una persona.

Este enfoque tiene sentido histórico: las primeras computadoras tenían recursos muy limitados y el paradigma estructurado permitía un control preciso sobre la memoria y el procesamiento. Sin embargo, a medida que los sistemas crecieron en complejidad, las limitaciones de este modelo se hicieron evidentes.

(los-limites-del-paradigma-estructurado)=
### Los límites del paradigma estructurado

El paradigma estructurado funciona bien para problemas de complejidad limitada, pero muestra sus debilidades cuando los sistemas crecen. Considerá el siguiente requerimiento:

:::{note} Planteo del problema

¿Qué pasaría si se necesita que distintas personas puedan saludarse de diferentes formas según su nacionalidad?

- Los argentinos dicen "¿Cómo andás?"
- Los españoles dicen "¿Qué tal?"
- Los estadounidenses dicen "How are you?"
- Los japoneses hacen una reverencia
:::

En el paradigma estructurado, la solución típica involucra múltiples funciones o lógica condicional compleja:

```c
void saludar(struct Persona p) {
    if (strcmp(p.nacionalidad, "Argentina") == 0) {
        printf("¿Cómo andás?\n");
    } else if (strcmp(p.nacionalidad, "España") == 0) {
        printf("¿Qué tal?\n");
    } else if (strcmp(p.nacionalidad, "Estados Unidos") == 0) {
        printf("How are you?\n");
    } else if (strcmp(p.nacionalidad, "Japón") == 0) {
        printf("*hace una reverencia*\n");
    } else {
        printf("Hola\n");
    }
}
```

Este enfoque presenta algunos problemas estructurales:

1. **Acoplamiento alto**: La función `saludar()` debe conocer todos los tipos de nacionalidades posibles y sus comportamientos específicos.

2. **Violación del principio abierto/cerrado**: Agregar una nueva nacionalidad requiere **modificar** el código existente, introduciendo riesgo de errores.

3. **Responsabilidad mal ubicada**: ¿Por qué una función externa debe saber cómo saluda cada nacionalidad? El conocimiento sobre "cómo saluda un argentino" debería residir cerca del concepto "argentino".

4. **Dispersión del conocimiento**: El comportamiento relacionado con "Persona" está fragmentado en múltiples funciones desperdigadas por el código.

5. **Dificultad de mantenimiento**: A medida que se agregan más comportamientos (despedirse, presentarse, etc.) y más tipos, la cantidad de código condicional crece exponencialmente.

(la-crisis-del-software)=
### La crisis del software

Estos problemas se manifestaron a gran escala en lo que se conoció como la **"crisis del software"** en las décadas de 1960 y 1970. Los proyectos de software:

- Excedían sistemáticamente sus presupuestos y plazos
- Contenían errores difíciles de localizar y corregir
- Eran casi imposibles de modificar sin introducir nuevos errores
- Tenían código que nadie comprendía completamente

La comunidad académica y la industria buscaron nuevas formas de organizar el código que permitieran manejar la complejidad creciente. De esta búsqueda surgió el paradigma orientado a objetos.

(el-cambio-de-mentalidad)=
### El cambio de mentalidad: de procedimientos a objetos

La Programación Orientada a Objetos propone un cambio fundamental en la forma de pensar sobre los programas:

:::{important} El cambio conceptual

**Paradigma estructurado**: El programa es una secuencia de instrucciones que transforman datos.

**Paradigma orientado a objetos**: El programa es un conjunto de objetos que colaboran enviándose mensajes.
:::

En lugar de pensar "¿qué pasos debo ejecutar para resolver este problema?", la POO nos invita a pensar "¿qué entidades participan en este problema y cómo interactúan entre sí?".

Este cambio tiene profundas implicaciones:

1. **Los datos dejan de ser pasivos**: En la POO, los datos "saben" qué operaciones pueden realizarse sobre ellos.

2. **El comportamiento se localiza**: Cada tipo de dato define su propio comportamiento, eliminando los interminables bloques condicionales.

3. **La responsabilidad se distribuye**: En lugar de tener pocas funciones "todopoderosas", se tienen muchos objetos con responsabilidades acotadas.

(la-fusion-de-datos-y-comportamiento)=
### La fusión de datos y comportamiento

La idea central de la POO es **fusionar el dato y el comportamiento en una misma entidad**. Esta fusión recibe el nombre de **encapsulamiento** y constituye la piedra angular del paradigma.

Conceptualmente, el ejemplo de las nacionalidades se resolvería así en POO:

```{mermaid}
classDiagram
    class Persona {
        -String nombre
        -int edad
        -String nacionalidad
        +saludar()
        +despedirse()
        +presentarse()
    }
    
    note for Persona "Fusión de datos y comportamiento:<br>El objeto sabe cómo saludarse a sí mismo"
```

```java
// Uso conceptual:
persona.saludar()   // El objeto sabe cómo saludarse a sí mismo
```

Las ventajas de este enfoque son inmediatas:

1. **Cohesión alta**: El dato (`nacionalidad`) y el comportamiento que depende de él (`saludar()`) están juntos en la misma unidad.

2. **Responsabilidad clara**: Cada persona "sabe" cómo saludarse. No hay funciones externas que deban conocer los detalles internos.

3. **Extensibilidad**: Se pueden crear variantes especializadas (mediante herencia o composición) sin modificar el código existente.

4. **Ocultamiento de información**: Los detalles internos de cómo una persona saluda están ocultos al resto del sistema.

:::{important}
La clave del paradigma orientado a objetos es pensar en **objetos que colaboran** enviándose mensajes entre sí, en lugar de pensar en funciones que operan sobre datos pasivos.
:::

(la-metafora-de-los-objetos)=
### La metáfora de los objetos

La palabra "objeto" no es casual. La POO propone modelar los programas como si fueran **simulaciones del mundo real**, donde entidades con existencia propia interactúan entre sí.

Pensá en un sistema de una biblioteca:

**Visión estructurada**:
```
Datos: arreglo de libros, arreglo de socios, arreglo de préstamos
Funciones: prestarLibro(), devolverLibro(), buscarLibro(), etc.
```

**Visión orientada a objetos**:

```{mermaid}
classDiagram
    class Biblioteca {
        -List~Libro~ libros
        -List~Socio~ socios
        +prestar(Socio, Libro) Prestamo
        +devolver(Prestamo)
        +verificarDisponibilidad(Libro) boolean
    }
    
    class Libro {
        -String titulo
        -String isbn
        -boolean disponible
        +marcarPrestado()
        +marcarDisponible()
    }
    
    class Socio {
        -String nombre
        -int numeroSocio
        -List~Prestamo~ prestamosActivos
        +solicitarLibro(Libro)
        +devolverLibro(Prestamo)
    }
    
    class Prestamo {
        -Socio socio
        -Libro libro
        -Date fechaPrestamo
        -Date fechaVencimiento
        +estaVencido() boolean
    }
    
    Biblioteca "1" -- "*" Libro : gestiona
    Biblioteca "1" -- "*" Socio : registra
    Prestamo "*" -- "1" Socio : realiza
    Prestamo "*" -- "1" Libro : involucra
    
    note for Biblioteca "Cada entidad tiene<br>responsabilidades claras<br>y colaboran entre sí"
```

**Interacciones (comportamiento):**
- El socio le pide un libro a la biblioteca
- La biblioteca verifica disponibilidad
- El libro se marca como prestado
- Se crea un préstamo que relaciona socio y libro

En la visión orientada a objetos, cada entidad tiene "vida propia": el libro sabe si está disponible, el socio sabe cuántos préstamos activos tiene, el préstamo sabe cuándo vence. Esta distribución de responsabilidades hace que el sistema sea más fácil de entender, modificar y extender

(01-oop-fundamentos-conceptos-fundamentales)=
## Conceptos Fundamentales del Paradigma

Esta sección presenta los conceptos esenciales que definen el paradigma orientado a objetos. Estos conceptos son **universales**: aplican independientemente del lenguaje de programación que se utilice.

(concepto-clase)=
### Clase

:::{note} Definición: Clase

Una **clase** es la descripción abstracta de un conjunto de entidades con características y comportamientos comunes. Funciona como un **molde** o **plantilla** que define:

1. **Qué información** caracteriza a las entidades de ese tipo (atributos).
2. **Qué operaciones** pueden realizar esas entidades (métodos).
:::

La clase es una abstracción: existe solo como concepto, como definición. Por sí sola, una clase no ocupa espacio en memoria para almacenar datos concretos; es una especificación de cómo serán los objetos que se creen a partir de ella.

**Analogía del mundo real:**

Pensá en los planos de una casa. Los planos especifican:
- Cuántas habitaciones tendrá la casa
- Dónde estarán las ventanas y puertas
- Las dimensiones de cada ambiente
- Los materiales a utilizar

Pero los planos **no son una casa**. Son la descripción de cómo construir casas de ese tipo. A partir de los mismos planos se pueden construir múltiples casas, cada una con sus propios habitantes, muebles y decoración.

De la misma manera, una clase `Persona` describe qué información tiene una persona (nombre, edad, nacionalidad) y qué puede hacer (saludar, presentarse), pero la clase en sí no es ninguna persona en particular.

```{mermaid}
classDiagram
    class Persona {
        -String nombre
        -int edad
        -String nacionalidad
        +saludar()
        +cumplirAnios()
        +presentarse()
    }
    
    note for Persona "Clase: El molde/plantilla<br>Define estructura y comportamiento"
```

:::{tip}
En el paradigma orientado a objetos, la clase representa el **concepto abstracto** (la idea de "Persona" en general), mientras que el objeto representa la **instancia concreta** (Juan, María, Pedro).
:::

(concepto-objeto)=
### Objeto

:::{note} Definición: Objeto

Un **objeto** es la materialización concreta de una clase: una entidad que existe en el sistema con valores específicos en sus atributos. Todo objeto está compuesto indivisiblemente por:

1. **Estado**: Los valores particulares de sus atributos en un momento dado.
2. **Comportamiento**: Las operaciones que puede realizar (definidas por los métodos de su clase).
3. **Identidad**: Lo que lo distingue de otros objetos, incluso si tienen el mismo estado.
:::

Mientras que la clase es la definición, el objeto es la "cosa" real que existe durante la ejecución del programa. Cada objeto:

- Ocupa espacio en memoria
- Tiene valores concretos en sus atributos
- Puede recibir mensajes y responder a ellos
- Tiene una existencia temporal: nace, vive y eventualmente muere

**Continuando la analogía:**

Si la clase `Casa` es el plano arquitectónico, entonces cada casa construida es un **objeto**:

```{figure} 01/analogia_casa.svg
:label: fig-analogia-casa
:align: center
:width: 80%

Analogía de la casa: la clase como plano y los objetos como construcciones reales.
```

Ambas casas fueron construidas según los mismos planos, pero son casas **diferentes**: están en ubicaciones distintas, tienen dueños distintos, pueden tener colores distintos. Modificar una no afecta a la otra.

:::{important}
Aunque dos objetos provengan de la misma clase, son **entidades completamente independientes**. Modificar el estado de uno no afecta el estado del otro.
:::

(concepto-atributo)=
### Atributo

:::{note} Definición: Atributo

Un **atributo** es una característica o propiedad definida en una clase. Representa la información que describe a los objetos de esa clase. Cuando un objeto es creado, cada atributo recibe un valor concreto que contribuye a formar el estado del objeto.
:::

Los atributos son las "variables" que cada objeto tiene para almacenar su información. Cada objeto de una clase tiene su **propia copia** de los atributos con valores potencialmente diferentes.

**Características de los atributos:**

1. **Están definidos en la clase**: La clase especifica qué atributos existen.
2. **Cada objeto tiene su copia**: Los valores son independientes entre objetos.
3. **Definen qué se puede "saber" de un objeto**: Son las preguntas que se pueden hacer sobre él.

**Ejemplo conceptual:**

```{figure} 01/vehiculo_atributos.svg
:label: fig-vehiculo-atributos
:align: center
:width: 85%

Relación entre la clase Vehículo y sus instancias concretas con valores específicos.
```

(concepto-estado)=
### Estado

:::{note} Definición: Estado

El **estado** de un objeto es la combinación específica de valores en todos sus atributos en un momento determinado. El estado:

- Es **único** para cada objeto (le pertenece exclusivamente a esa instancia).
- Es **dinámico**: puede cambiar a lo largo de la vida del objeto.
- **Define la situación actual** del objeto en el sistema.
:::

El estado es lo que diferencia a un objeto "recién creado" de uno que ha "vivido" en el sistema. A medida que el objeto recibe mensajes y ejecuta métodos, su estado puede transformarse.

**Ejemplo de evolución del estado:**

```{figure} 01/auto_estado_evolucion.svg
:label: fig-auto-estado-evolucion
:align: center
:width: 80%

Evolución del estado de un objeto Auto tras recibir mensajes.
```

:::{note}
Algunos atributos pueden ser **inmutables** (no cambian después de la construcción), mientras que otros son **mutables** (pueden cambiar durante la vida del objeto). El diseño de cuáles atributos son mutables es una decisión importante en el modelado.
:::

(concepto-instancia)=
### Instancia

:::{note} Definición: Instancia

Una **instancia** es un objeto considerado en relación con su clase. Decir que un objeto es "una instancia de la clase X" significa que ese objeto fue creado a partir de la clase X y, por lo tanto, tiene la estructura y el comportamiento definidos por X.
:::

Los términos "objeto" e "instancia" son prácticamente sinónimos, pero se usan en contextos ligeramente diferentes:

- **Objeto**: Enfatiza la entidad en sí misma, su existencia independiente.
- **Instancia**: Enfatiza la relación con la clase de la que proviene.

**Ejemplo:**

> "Juan es un **objeto** que representa a una persona en nuestro sistema."
> 
> "Juan es una **instancia** de la clase Persona."

Ambas frases son correctas y dicen esencialmente lo mismo, pero con diferente énfasis.

El proceso de crear un objeto a partir de una clase se llama **instanciación**:

```{figure} 01/persona_instanciacion.svg
:label: fig-persona-instanciacion
:align: center
:width: 60%

Instanciación de la clase Persona para crear el objeto juan.
```

(concepto-metodo)=
### Método

:::{note} Definición: Método

Un **método** es una operación o comportamiento definido en una clase que describe algo que los objetos de esa clase pueden hacer. Los métodos:

1. Definen el **comportamiento** de los objetos.
2. Pueden modificar el **estado** del objeto.
3. Pueden retornar información sobre el objeto.
4. Pueden interactuar con otros objetos.
:::

Si los atributos responden a la pregunta "¿qué sabe el objeto?", los métodos responden a "¿qué puede hacer el objeto?".

**Tipos de métodos según su propósito:**

1. **Métodos de consulta**: Obtienen información sin modificar el estado.
   - Ejemplo: `obtenerEdad()`, `estaEncendido()`, `calcularArea()`

2. **Métodos de modificación**: Cambian el estado del objeto.
   - Ejemplo: `cumplirAnios()`, `encender()`, `mover()`

3. **Métodos de construcción**: Inicializan el objeto al crearlo (constructores).
   - Ejemplo: Crear una persona con nombre y edad específicos.

**Ejemplo conceptual:**

```{figure} 01/cuenta_bancaria_estructura.svg
:label: fig-cuenta-bancaria-estructura
:align: center
:width: 85%

Representación de atributos y métodos de la clase CuentaBancaria.
```

(concepto-mensaje)=
### Mensaje

:::{note} Definición: Mensaje

Un **mensaje** es la forma en que los objetos se comunican entre sí. Enviar un mensaje a un objeto significa solicitarle que ejecute uno de sus métodos. El objeto receptor decide cómo responder al mensaje basándose en su propia implementación.
:::

La comunicación mediante mensajes es central en la POO. Los objetos no "llaman funciones"; se **envían mensajes** unos a otros:

```{figure} 01/mensaje_pasaje.svg
:label: fig-mensaje-pasaje
:align: center
:width: 80%

Envío de un mensaje de un objeto emisor a un receptor.
```

Esta forma de pensar tiene implicaciones importantes:

1. **El emisor no sabe cómo se implementa** la operación; solo sabe que el receptor puede responder a ese mensaje.

2. **Diferentes objetos pueden responder al mismo mensaje de formas distintas** (esto es la base del polimorfismo, que se verá más adelante).

3. **La responsabilidad está en el receptor**: Es el objeto receptor quien decide qué hacer con el mensaje.

(ciclo-de-vida-de-los-objetos)=
## Ciclo de Vida de los Objetos

Todo objeto tiene un ciclo de vida: nace, existe durante un tiempo, y eventualmente deja de existir. Comprender este ciclo es fundamental para diseñar sistemas correctos.

(construccion-de-objetos)=
### Construcción

:::{note} Definición: Construcción

La **construcción** (o **instanciación**) es el comportamiento dedicado al proceso de crear un objeto a partir de una clase. Durante la construcción:

1. Se reserva espacio en memoria para el nuevo objeto.
2. Se inicializan los atributos con valores iniciales.
3. Se ejecuta código de inicialización específico (el constructor).
:::

La construcción es el "nacimiento" del objeto. Es el momento en que pasa de ser una mera posibilidad (la clase) a una realidad concreta (el objeto en memoria).

**El constructor:**

El constructor es un método especial que se ejecuta automáticamente cuando se crea un objeto. Su responsabilidad es garantizar que el objeto nazca en un **estado válido y consistente**.

```{figure} 01/construccion_proceso.svg
:label: fig-construccion-proceso
:align: center
:width: 80%

Etapas del proceso de construcción de un nuevo objeto.
```

:::{important}
Un buen constructor debe garantizar que el objeto nunca exista en un estado inválido. Si se necesitan datos para que el objeto tenga sentido (como el nombre de una persona), esos datos deben proporcionarse durante la construcción.
:::

(vida-del-objeto)=
### Vida del objeto

Una vez construido, el objeto "vive" en el sistema:

- Recibe mensajes de otros objetos
- Ejecuta sus métodos
- Modifica su estado
- Envía mensajes a otros objetos
- Colabora para cumplir los objetivos del sistema

La duración de la vida de un objeto depende de las necesidades del sistema. Algunos objetos viven durante toda la ejecución del programa; otros existen solo brevemente para realizar una tarea específica.

(destruccion-de-objetos)=
### Destrucción

:::{note} Definición: Destrucción
La **destrucción** es el final de la vida de un objeto, cuando ya no es necesario y los recursos que ocupa (principalmente memoria) deben liberarse.
:::

La gestión de la destrucción varía según el lenguaje:

**Destrucción manual (como en C):**
- El programador es responsable de liberar la memoria.
- Requiere llamadas explícitas como `free()`.
- Errores comunes: olvidar liberar (memory leak) o liberar dos veces.

**Destrucción automática (Garbage Collection):**
- El sistema detecta automáticamente cuándo un objeto ya no es accesible.
- Un componente llamado "Garbage Collector" (recolector de basura) libera la memoria.
- El programador no debe preocuparse por la liberación manual.
- Común en lenguajes modernos como Java, Python, C#, Go.

```{figure} 01/destruccion_garbage_collector.svg
:label: fig-destruccion-garbage-collector
:align: center
:width: 80%

Objeto sin referencias (huérfano) esperando ser recolectado por el Garbage Collector.
```

:::{note} El destructor en Java
La idea de que existe un comportamiento que será invocado en el momento de la 
liberación de la memoria tomada por un objeto es un concepto del paradigma, pero
esta no está disponible en Java por como gestiona la memoria de forma automática.

Simplemente, no es predecible (determinista), cuando se ejecutará esta 'limpieza'.

De todas formas, sí está disponible en C++ que es orientado a objetos.

:::

(concepto-identidad)=
### Identidad

:::{note} Definición: Identidad

La **identidad** es lo que hace único a cada objeto, lo que lo distingue de todos los demás objetos del sistema, incluso de aquellos que tienen exactamente el mismo estado.
:::

La identidad es un concepto sutil pero fundamental. Considerá este escenario:

```{figure} 01/persona_identidad.svg
:label: fig-persona-identidad
:align: center
:width: 80%

Dos objetos con el mismo estado pero diferente identidad física.
```

Ambos objetos tienen **exactamente los mismos valores** en todos sus atributos. Sin embargo, son **dos objetos diferentes**: ocupan diferentes posiciones en memoria y tienen existencias independientes. Si modificamos `persona1`, `persona2` no se ve afectada.

**Tipos de igualdad:**

1. **Identidad física** (¿son el mismo objeto?):
   - Compara si dos referencias apuntan al **mismo objeto en memoria**.
   - Es la igualdad más estricta.

2. **Igualdad lógica** (¿representan lo mismo?):
   - Compara si dos objetos son **equivalentes según las reglas del dominio**.
   - Definida por el programador.

**Ejemplo:**

```
Identidad física:
persona1 y persona2 son DIFERENTES objetos (diferentes ubicaciones en memoria)

Igualdad lógica (si se define que dos personas son "iguales" cuando tienen el mismo DNI):
persona1 y persona2 serían IGUALES (mismo DNI: "12345678")
```

:::{important}
La igualdad lógica es una **decisión de diseño**. ¿Qué significa que dos objetos sean "iguales"? La respuesta depende del dominio del problema y debe ser definida explícitamente por el programador.
:::

(concepto-abstraccion)=
## Abstracción

:::{note} Definición: Abstracción

La **abstracción** es el proceso mental de identificar las características esenciales de una entidad para un propósito específico, ignorando deliberadamente los detalles irrelevantes.
:::

La abstracción es quizás el concepto más importante de todo el paradigma, porque está en la base de todas las decisiones de diseño. Sin abstracción, sería imposible modelar sistemas complejos.

(la-necesidad-de-abstraer)=
### La necesidad de abstraer

El mundo real es infinitamente complejo. Una persona tiene:
- Nombre, apellido, edad, DNI
- Color de pelo, color de ojos, altura, peso
- Huella dactilar, ADN, tipo de sangre
- Dirección, teléfono, email
- Historial médico, educación, trabajo
- Preferencias, hobbies, relaciones familiares
- Millones de características más...

Es **imposible** (e innecesario) representar toda esta complejidad en un sistema de software. La abstracción permite **seleccionar** solo aquellas características que son relevantes para el objetivo específico del sistema.

(la-abstraccion-es-contextual)=
### La abstracción es contextual

:::{important}
La abstracción **depende del contexto y del objetivo del sistema**. Los mismos conceptos del mundo real pueden modelarse de formas completamente diferentes según el dominio de aplicación.
:::

Considerá cómo se modelaría una "Persona" en diferentes sistemas:

```{mermaid}
classDiagram
    class PersonaUniversidad {
        -String nombre
        -String dni
        -Date fechaNacimiento
        -String emailInstitucional
        -String legajo
        -List~Carrera~ carreras
        +inscribirCarrera(Carrera)
        +consultarHistorial()
    }
    
    class PersonaGimnasio {
        -String nombre
        -byte[] fotoRostro
        -byte[] huellaDactilar
        -boolean membresiaActiva
        -Date fechaVencimiento
        +verificarAcceso() boolean
        +renovarMembresia()
    }
    
    class PersonaDonante {
        -String nombre
        -String dni
        -TipoSangre tipoSangre
        -FactorRH factorRH
        -Date ultimaDonacion
        -List~String~ enfermedades
        +puedeDonar() boolean
        +registrarDonacion()
    }
    
    note for PersonaUniversidad "Sistema Universitario:<br>Solo atributos académicos relevantes"
    note for PersonaGimnasio "Control de Acceso:<br>Datos biométricos y membresía"
    note for PersonaDonante "Donación de Sangre:<br>Datos médicos críticos"
```

**Sistema de gestión universitaria:**
```java
// Irrelevantes: color de pelo, altura, peso, tipo de sangre...
```

**Sistema de control de acceso a un gimnasio:**
```java
// Irrelevantes: DNI, carrera universitaria, email...
```

**Sistema de donación de sangre:**
```java
// Irrelevantes: legajo universitario, huella dactilar...
```

Observá cómo la misma entidad del mundo real (una persona) se modela de formas radicalmente diferentes según el propósito del sistema.

(niveles-de-abstraccion)=
### Niveles de abstracción

La abstracción opera en múltiples niveles:

1. **Abstracción de datos**: Decidir qué atributos son relevantes.
   - ¿El color de pelo es importante? ¿La altura? ¿El peso?

2. **Abstracción de comportamiento**: Decidir qué operaciones son relevantes.
   - ¿Una persona debe poder "saludar"? ¿"caminar"? ¿"respirar"?

3. **Abstracción de relaciones**: Decidir qué conexiones entre entidades son relevantes.
   - ¿Importa quiénes son los padres de una persona? ¿Sus amigos? ¿Su jefe?

(el-proceso-de-abstraccion)=
### El proceso de abstracción

Abstraer es un proceso de **filtrado consciente**:

```{figure} 01/abstraccion_filtro.svg
:label: fig-abstraccion-filtro
:align: center
:width: 80%

El proceso de filtrado de detalles para obtener un modelo abstracto.
```

:::{tip}
Una buena abstracción es aquella que captura exactamente lo necesario: ni más (complejidad innecesaria) ni menos (información faltante).
:::

(encapsulamiento-introduccion)=
## El Encapsulamiento

:::{note} Definición: Encapsulamiento

El **encapsulamiento** es el principio de ocultar los detalles internos de un objeto, exponiendo solo una interfaz controlada para interactuar con él. El encapsulamiento tiene dos aspectos:

1. **Fusión**: Agrupar datos y comportamiento en una misma unidad (el objeto).
2. **Ocultamiento**: Restringir el acceso directo a los componentes internos del objeto.
:::

(la-fusion-de-datos-y-comportamiento-detalle)=
### Fusión de datos y comportamiento

Ya vimos que la POO fusiona datos y comportamiento. Esta fusión tiene consecuencias importantes:

```{figure} 01/encapsulamiento_comparacion.svg
:label: fig-encapsulamiento-comparacion
:align: center
:width: 85%

Comparación entre la separación de datos/funciones y la unidad objeto (encapsulamiento).
```

(ocultamiento-de-informacion)=
### Ocultamiento de información

El segundo aspecto del encapsulamiento es igualmente importante: **ocultar los detalles de implementación**.

**¿Por qué ocultar?**

1. **Protección**: Impide que código externo corrompa el estado del objeto.
2. **Flexibilidad**: Permite cambiar la implementación interna sin afectar al código que usa el objeto.
3. **Simplicidad**: El usuario del objeto solo ve lo que necesita saber.

:::{seealso}
El tema de encapsulamiento se profundiza en {ref}`encapsulamiento-concepto`, donde se analizan:
- Analogías detalladas (control remoto, televisor)
- Control de invariantes
- Beneficios para mantenimiento y acoplamiento
- Implementación en Java con modificadores de acceso
:::


(heuristicas-de-analisis)=
## Modelado de Objetos: Heurísticas de Análisis

Una de las habilidades más importantes en la programación orientada a objetos es **identificar objetos y su comportamiento** a partir de los requerimientos de un sistema. Esta sección presenta técnicas prácticas para realizar esta transformación del "texto" al "modelo".

(del-problema-al-modelo)=
### Del problema al modelo

Cuando se enfrenta un problema nuevo, la primera tarea es identificar:
- ¿Qué **entidades** participan en el problema?
- ¿Qué **información** caracteriza a cada entidad?
- ¿Qué **operaciones** realizan las entidades?
- ¿Cómo **interactúan** entre sí?

Las heurísticas de análisis son "reglas del pulgar" que ayudan a responder estas preguntas de manera sistemática.

(la-heuristica-linguistica)=
### La Heurística Lingüística

La heurística lingüística es una técnica que permite identificar candidatos a clases y métodos mediante el **análisis del lenguaje natural** usado en la descripción del problema.

:::{note} Fundamento

El lenguaje natural que usamos para describir un problema refleja nuestra comprensión del dominio. Los sustantivos tienden a representar "cosas" (candidatos a clases), mientras que los verbos tienden a representar "acciones" (candidatos a métodos).
:::

(analisis-de-sustantivos)=
#### Análisis de Sustantivos

:::{tip} Regla: Sustantivos → Clases o Atributos

Los **sustantivos** en los requerimientos son candidatos primarios para convertirse en:
- **Clases**: Si representan entidades con existencia propia y comportamiento.
- **Atributos**: Si representan características o propiedades de una entidad.
:::

**Proceso:**

1. Leer el texto del requerimiento.
2. Identificar todos los sustantivos.
3. Para cada sustantivo, preguntarse:
   - ¿Es una "cosa" con existencia propia? → Posible **clase**
   - ¿Es una característica de otra cosa? → Posible **atributo**

**Ejemplo de análisis:**

> "El sistema debe permitir que un **cliente** realice una **reserva** de un **vehículo** especificando la **fecha** de inicio y la **fecha** de fin. El **vehículo** tiene una **patente**, una **marca** y un **modelo**."

Sustantivos identificados y análisis inicial:

| Sustantivo | ¿Clase o Atributo? | Razonamiento |
|------------|-------------------|--------------|
| Cliente | ¿Clase? | Tiene existencia propia, realiza acciones |
| Reserva | ¿Clase? | Es una "cosa" que relaciona cliente y vehículo |
| Vehículo | ¿Clase? | Tiene existencia propia, tiene características |
| Fecha | ¿Clase o Atributo? | ¿Es simple o compleja? |
| Patente | ¿Atributo? | Es una característica del vehículo |
| Marca | ¿Atributo? | Es una característica del vehículo |
| Modelo | ¿Atributo? | Es una característica del vehículo |

(analisis-de-verbos)=
#### Análisis de Verbos

:::{tip} Regla: Verbos → Métodos

Los **verbos** en los requerimientos son candidatos directos para definir el **comportamiento** (métodos) de las clases. También revelan las interacciones entre objetos.
:::

**Proceso:**

1. Identificar todos los verbos de acción.
2. Para cada verbo, preguntarse:
   - ¿Quién realiza esta acción? → Posible clase que tiene el método
   - ¿Sobre qué/quién se realiza? → Posibles parámetros o colaboradores

**Continuando el ejemplo:**

> "El sistema debe permitir que un cliente **realice** una reserva de un vehículo especificando la fecha de inicio y la fecha de fin. El vehículo **puede verificar** su disponibilidad en un período dado."

Verbos identificados:

| Verbo | Acción | ¿Quién la realiza? | Método candidato |
|-------|--------|-------------------|------------------|
| Realice | Crear una reserva | Cliente (o Sistema) | `realizarReserva()` |
| Verificar | Comprobar disponibilidad | Vehículo | `verificarDisponibilidad()` |

**Modelo preliminar:**

```{figure} 01/modelo_preliminar_vehiculos.svg
:label: fig-modelo-preliminar-vehiculos
:align: center
:width: 85%

Modelo preliminar resultante del análisis lingüístico inicial.
```

(refinando-el-modelo)=
### Aplicación de Filtros: Refinando el modelo

El análisis lingüístico produce una lista de candidatos, pero no todos ellos deben convertirse en elementos del modelo final. Los **filtros** permiten refinar el diseño eliminando elementos innecesarios o transformándolos apropiadamente.

(filtro-de-abstraccion)=
#### El Filtro de Abstracción

:::{warning} Filtro: Abstracción

**Eliminar** todo sustantivo o verbo que no aporte valor al objetivo específico del sistema.
:::

Este filtro aplica directamente el principio de abstracción: solo modelar lo que es relevante para el problema en cuestión.

**Ejemplo:**

> "El sistema debe registrar **personas** con su **nombre**, **DNI**, **fecha de nacimiento**, **color de pelo** y **equipo de fútbol favorito** para gestionar el acceso a un edificio corporativo."

Aplicando el filtro de abstracción para un sistema de **control de acceso**:

| Sustantivo | ¿Relevante? | Justificación |
|------------|-------------|---------------|
| Nombre | ✅ Sí | Identificación del usuario |
| DNI | ✅ Sí | Identificación única |
| Fecha de nacimiento | ⚠️ Dudoso | ¿Se requiere verificar edad? |
| Color de pelo | ❌ No | Irrelevante para acceso |
| Equipo de fútbol favorito | ❌ No | Completamente irrelevante |

**Modelo filtrado:**

```{figure} 01/persona_modelo_filtrado.svg
:label: fig-persona-modelo-filtrado
:align: center
:width: 70%

Modelo filtrado de Persona para un sistema de control de acceso.
```

:::{important}
El filtro de abstracción es **contextual**. El "color de pelo" sería relevante en un sistema de identificación visual, y el "equipo de fútbol" podría ser relevante en un sistema de venta de entradas para estadios.
:::

(filtro-de-complejidad)=
#### El Filtro de Complejidad: ¿Valor simple o Clase nueva?

:::{warning} Filtro: Complejidad

Decidir si un sustantivo debe representarse como:
- **Valor simple (primitivo)**: Sí es un dato atómico sin comportamiento propio.
- **Clase independiente**: Si tiene estructura interna y/o comportamiento propio.
:::

Esta es una de las decisiones de diseño más difíciles para programadores novatos. La regla general:

**Preguntas guía:**

1. ¿El concepto tiene **múltiples componentes** relacionados?
2. ¿El concepto tiene **operaciones propias** que se le pueden aplicar?
3. ¿La **validación** del concepto es compleja?
4. ¿Se **repite** la misma lógica relacionada con este concepto en múltiples lugares?

Si la respuesta es **sí** a una o más preguntas, considerar crear una **clase independiente**.

**Ejemplo 1: Fecha de nacimiento**

¿`fechaNacimiento` debe ser un texto simple o una clase?

```{figure} 01/filtro_complejidad_fecha.svg
:label: fig-filtro-complejidad-fecha
:align: center
:width: 85%

Comparación entre representar la fecha como un valor simple vs. una clase independiente.
```

La Opción B es superior porque:
- La lógica de fechas está **encapsulada** en un solo lugar.
- Se pueden **reutilizar** las operaciones de Fecha en todo el sistema.
- La **validación** está centralizada.

**Ejemplo 2: DNI**

¿El DNI debe ser un texto simple o una clase?

Análisis:
- ¿Tiene múltiples componentes? → En Argentina, no.
- ¿Tiene operaciones propias? → Validación de formato, formateo con puntos.
- ¿La validación es compleja? → Moderadamente (longitud, solo dígitos).
- ¿Se repite la lógica? → Posiblemente.

**Decisión**: Depende del contexto. Si el sistema necesita validar y formatear DNIs frecuentemente, una clase `DNI` es apropiada. Si solo se guarda y muestra, un texto puede bastar.

```{figure} 01/filtro_complejidad_dni.svg
:label: fig-filtro-complejidad-dni
:align: center
:width: 70%

Estructura de la clase DNI con sus métodos de validación y formateo.
```

:::{tip}
Ante la duda, es preferible **empezar simple** (valor primitivo) y **refactorizar** a una clase cuando la complejidad lo justifique. Crear clases innecesarias también es un error de diseño.
:::

(criterios-de-decision)=
#### Criterios de decisión resumidos

| Situación | Decisión sugerida |
|-----------|------------------|
| Solo almacena un valor, sin operaciones | Atributo primitivo |
| Múltiples valores relacionados | Clase nueva |
| Requiere validación compleja | Clase nueva |
| Tiene operaciones específicas | Clase nueva |
| La lógica se repite en varios lugares | Clase nueva |
| Es un concepto del dominio con "vida propia" | Clase nueva |

(relaciones-entre-clases)=
### Identificación de Relaciones

Además de clases y atributos, el análisis debe identificar las **relaciones** entre clases.

**Tipos de relaciones comunes:**

1. **Asociación**: Una clase "conoce" o "usa" a otra.
2. **Agregación**: Una clase "contiene" a otras (relación todo-parte débil).
3. **Composición**: Una clase "está compuesta por" otras (relación todo-parte fuerte).
4. **Dependencia**: Una clase "usa temporalmente" a otra.

:::{seealso}
Las relaciones entre objetos se estudian en profundidad en {ref}`tipos-de-relaciones`, que incluye:
- Notación UML y cardinalidad
- Diferencias entre Composición y Agregación
- Asociaciones bidireccionales
- Implementación en Java
:::

**Ejemplo rápido:**

> "Una **biblioteca** tiene muchos **libros**. Cada libro tiene un **autor**. Los **socios** pueden tomar **préstamos** de libros."

```{mermaid}
classDiagram
    class Biblioteca {
        -String nombre
        -List~Libro~ libros
        +agregarLibro(Libro)
        +buscarLibro(String) Libro
    }
    
    class Libro {
        -String titulo
        -String isbn
        -Autor autor
        +getAutor() Autor
    }
    
    class Autor {
        -String nombre
        -String nacionalidad
        +getLibros() List~Libro~
    }
    
    class Socio {
        -String nombre
        -List~Prestamo~ prestamos
        +tomarPrestamo(Libro)
    }
    
    class Prestamo {
        -Socio socio
        -Libro libro
        -Date fechaPrestamo
        -Date fechaDevolucion
    }
    
    Biblioteca "1" -- "*" Libro : tiene
    Libro "*" -- "1" Autor : tiene
    Socio "1" -- "*" Prestamo : realiza
    Prestamo "*" -- "1" Libro : involucra
```

(ejemplo-completo-de-analisis)=
### Ejemplo Completo de Análisis

**Requerimiento:**

> "Una **veterinaria** necesita gestionar la atención de **mascotas**. Cada mascota tiene un **nombre**, una **especie**, una **raza** y una **fecha de nacimiento**. Las mascotas pertenecen a **dueños** que tienen **nombre**, **teléfono** y **dirección**. Los **veterinarios** de la clínica realizan **consultas** a las mascotas, registrando la **fecha**, el **motivo**, el **diagnóstico** y el **tratamiento** indicado."

**Paso 1: Identificar sustantivos**

| Sustantivo | Primera impresión |
|------------|-------------------|
| Veterinaria | Clase (el sistema) |
| Mascotas | Clase |
| Nombre | Atributo |
| Especie | ¿Atributo o Clase? |
| Raza | Atributo |
| Fecha de nacimiento | ¿Atributo o Clase? |
| Dueños | Clase |
| Teléfono | Atributo |
| Dirección | ¿Atributo o Clase? |
| Veterinarios | Clase |
| Consultas | Clase |
| Fecha | ¿Atributo o Clase? |
| Motivo | Atributo |
| Diagnóstico | Atributo |
| Tratamiento | ¿Atributo o Clase? |

**Paso 2: Identificar verbos**

| Verbo | Acción | Actor probable |
|-------|--------|----------------|
| Gestionar | Coordinar atención | Veterinaria (sistema) |
| Pertenecen | Relación mascota-dueño | Relación |
| Realizan | Hacer consultas | Veterinario |
| Registrando | Guardar información | Consulta |

**Paso 3: Aplicar filtros**

*Filtro de abstracción:* Todos los elementos parecen relevantes para el objetivo.

*Filtro de complejidad:*
- `Especie`: Valor simple (texto: "Perro", "Gato", etc.)
- `Fecha de nacimiento`: Usar fecha del sistema
- `Dirección`: Clase si tiene calle, número, ciudad, CP; texto simple si no importa la estructura
- `Tratamiento`: Podría ser texto o clase si incluye medicamentos, dosis, duración

**Paso 4: Modelo resultante**

```{mermaid}
classDiagram
    class Veterinaria {
        -List~Mascota~ mascotas
        -List~Veterinario~ veterinarios
        -List~Consulta~ consultas
        +registrarMascota(Mascota)
        +buscarMascota(String nombre) Mascota
        +obtenerHistorialConsultas(Mascota) List~Consulta~
    }
    
    class Mascota {
        -String nombre
        -String especie
        -String raza
        -Date fechaNacimiento
        -Duenio duenio
        +obtenerEdad() int
    }
    
    class Duenio {
        -String nombre
        -String telefono
        -Direccion direccion
        +obtenerMascotas() List~Mascota~
    }
    
    class Direccion {
        -String calle
        -String numero
        -String ciudad
        -String codigoPostal
        +formatear() String
    }
    
    class Veterinario {
        -String nombre
        -String matricula
        +realizarConsulta(Mascota, String) Consulta
    }
    
    class Consulta {
        -Date fecha
        -Mascota mascota
        -Veterinario veterinario
        -String motivo
        -String diagnostico
        -String tratamiento
        +registrarDiagnostico(String, String)
    }
    
    Veterinaria "1" -- "*" Mascota : gestiona
    Veterinaria "1" -- "*" Veterinario : emplea
    Veterinaria "1" -- "*" Consulta : registra
    Mascota "*" -- "1" Duenio : pertenece
    Duenio "1" *-- "1" Direccion : tiene
    Consulta "*" -- "1" Mascota : sobre
    Consulta "*" -- "1" Veterinario : realiza
    
    note for Veterinaria "Sistema coordinador<br>que gestiona todas las entidades"
```

(actividad-practica)=
### Actividad Práctica: Cazadores y Auditores

:::{tip} Actividad de Taller

**Objetivo:** Demostrar cómo el mismo texto puede producir modelos diferentes según el objetivo del sistema.

**Enunciado:**
> "Una empresa de transporte gestiona sus **vehículos**. Cada vehículo tiene una **patente**, una **marca**, un **modelo**, un **color** y un **kilometraje** actual. Los vehículos realizan **viajes** que tienen un **origen**, un **destino**, una **distancia** y un **conductor**."

**Grupo A - Sistema de Mantenimiento Preventivo:**
Diseñar un modelo para predecir cuándo los vehículos necesitan service.

**Grupo B - Sistema de Liquidación de Viáticos:**
Diseñar un modelo para calcular los viáticos a pagar a los conductores.

**Preguntas de reflexión:**
1. ¿El `color` del vehículo es relevante en ambos sistemas?
2. ¿El `kilometraje` tiene la misma importancia en ambos?
3. ¿La `distancia` del viaje interesa igual a ambos grupos?
4. ¿Qué atributos/métodos aparecen en un modelo pero no en otro?
:::

**Posible resolución:**

```{figure} 01/actividad_cazadores_auditores.svg
:label: fig-actividad-cazadores-auditores
:align: center
:width: 85%

Comparación de abstracciones para sistemas de mantenimiento y viáticos.
```

:::{note}
El `color` del vehículo no aparece en ninguno de los dos modelos porque no es relevante ni para mantenimiento ni para viáticos. Esta es la abstracción en acción.
:::

(principios-adicionales)=
## Principios Adicionales del Paradigma

(colaboracion-entre-objetos)=
### Colaboración entre Objetos

En un sistema orientado a objetos bien diseñado, ningún objeto hace todo el trabajo solo. Los objetos **colaboran** entre sí, cada uno aportando su especialidad.

:::{important} Principio de Colaboración

Un sistema orientado a objetos es una **red de objetos que colaboran** para cumplir un objetivo. Cada objeto tiene responsabilidades acotadas y delega en otros cuando necesita capacidades que no posee.
:::

**Ejemplo de colaboración:**

```{mermaid}
classDiagram
    class Factura {
        -Cliente cliente
        -List~LineaFactura~ lineas
        -Date fecha
        +calcularTotal() double
        +aplicarImpuestos() double
    }
    
    class Cliente {
        -String nombre
        -String cuit
        -String direccion
        +obtenerDatos() String
    }
    
    class LineaFactura {
        -Producto producto
        -int cantidad
        +subtotal() double
    }
    
    class Producto {
        -String nombre
        -double precio
        -String codigo
        +getPrecio() double
    }
    
    Factura "1" -- "1" Cliente : emitida a
    Factura "1" *-- "*" LineaFactura : contiene
    LineaFactura "*" -- "1" Producto : referencia
    
    note for Factura "Para calcular total:<br>1. Pide a cada línea su subtotal<br>2. Suma todos los subtotales<br>3. Aplica impuestos"
    note for LineaFactura "Delega en Producto:<br>subtotal = cantidad * producto.precio"
```

La Factura no sabe cómo calcular el precio de un producto; **delega** esa responsabilidad en `Producto`. La `LineaFactura` no sabe el nombre del cliente; esa información está en `Cliente`. Cada objeto hace lo suyo y **colabora** con los demás.

(responsabilidad-unica)=
### El Principio de Responsabilidad Única

:::{note} Principio de Responsabilidad Única (SRP)

Una clase debe tener **una única razón para cambiar**. Esto significa que cada clase debe tener una única responsabilidad bien definida.
:::

Este principio, formulado por Robert C. Martin, es una guía fundamental para diseñar clases cohesivas.

**Ejemplo de violación y diseño mejorado:**

```{figure} 01/srp_ejemplo.svg
:label: fig-srp-ejemplo
:align: center
:width: 85%

Refactorización de una clase con múltiples responsabilidades hacia un diseño que cumple el SRP.
```

Ahora cada clase tiene una única responsabilidad y una única razón para cambiar.

(cohesion-y-acoplamiento)=
### Cohesión y Acoplamiento

Dos métricas fundamentales para evaluar la calidad de un diseño orientado a objetos son la **cohesión** y el **acoplamiento**.

**Cohesión**: Mide qué tan relacionados están los elementos dentro de una clase.
- **Alta cohesión** (deseable): Todos los atributos y métodos trabajan juntos hacia un propósito común.
- **Baja cohesión** (problemática): La clase hace cosas poco relacionadas entre sí.

**Acoplamiento**: Mide qué tan dependiente es una clase de otras clases.
- **Bajo acoplamiento** (deseable): Las clases pueden funcionar y cambiar con independencia relativa.
- **Alto acoplamiento** (problemático): Un cambio en una clase requiere cambios en muchas otras.

:::{tip}
Un buen diseño busca **maximizar la cohesión** (cada clase hace una cosa bien) y **minimizar el acoplamiento** (las clases dependen poco unas de otras).
:::

```{figure} 01/cohesion_ejemplo.svg
:label: fig-cohesion-ejemplo
:align: center
:width: 85%

Comparación entre una clase altamente cohesiva y una clase de utilidades con baja cohesión.
```

(resumen-conceptual)=
## Resumen

Este capítulo ha presentado los **fundamentos conceptuales** del paradigma orientado a objetos:

1. **La transición de paradigma**:
   - El paradigma estructurado separa datos y código.
   - El paradigma OO los fusiona en objetos.
   - Los objetos colaboran enviándose mensajes.

2. **Conceptos fundamentales**:
   - **Clase**: Molde o plantilla que define estructura y comportamiento.
   - **Objeto**: Instancia concreta con estado, comportamiento e identidad.
   - **Atributo**: Información que caracteriza al objeto.
   - **Estado**: Valores específicos de los atributos en un momento dado.
   - **Método**: Operación que el objeto puede realizar.
   - **Mensaje**: Comunicación entre objetos.

3. **Ciclo de vida**:
   - **Construcción**: Creación del objeto con estado inicial válido.
   - **Vida**: El objeto existe, recibe mensajes, modifica su estado.
   - **Destrucción**: Liberación de recursos (manual o automática).
   - **Identidad**: Física (mismo objeto) vs. lógica (equivalencia semántica).

4. **Abstracción y Encapsulamiento**:
   - La abstracción selecciona solo lo relevante para el objetivo.
   - El encapsulamiento fusiona datos y comportamiento y oculta detalles (ver {ref}`encapsulamiento-concepto`).

5. **Heurísticas de análisis**:
   - Sustantivos → Clases o Atributos.
   - Verbos → Métodos.
   - Filtro de abstracción: eliminar lo irrelevante.
   - Filtro de complejidad: decidir entre primitivo y clase nueva (implementación en {ref}`java-sintaxis-clases`).

6. **Principios de diseño**:
   - Los objetos colaboran, ninguno hace todo (ver {ref}`tipos-de-relaciones`).
   - Responsabilidad única: una razón para cambiar (ver [Principio de Responsabilidad Única (S)](../parte_3/09_oop_solid.md)).
   - Alta cohesión, bajo acoplamiento (ver [Principio de Inversión de Dependencias (D)](../parte_3/09_oop_solid.md)).

:::{important}
El diseño orientado a objetos es un **proceso iterativo** y **contextual**. No existe un único diseño "correcto"; el mejor diseño depende del problema específico, los requerimientos, y las restricciones del proyecto. Las heurísticas y principios presentados son guías, no reglas absolutas.
:::

(lecturas-recomendadas)=
## Lecturas Recomendadas

- Meyer, B. (1997). *Object-Oriented Software Construction* (2nd ed.). Prentice Hall. — Una referencia fundamental sobre los principios de la POO.
- Martin, R. C. (2003). *Agile Software Development: Principles, Patterns, and Practices*. Prentice Hall. — Excelente cobertura de principios de diseño OO (SOLID).
- Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley. — El libro clásico de patrones de diseño.
- Wirfs-Brock, R., & McKean, A. (2002). *Object Design: Roles, Responsibilities, and Collaborations*. Addison-Wesley. — Enfoque en diseño basado en responsabilidades.

(ejercicios-conceptuales)=
## Ejercicios

```{exercise}
:label: ej-conceptos-basicos

Indicá si las siguientes afirmaciones son verdaderas o falsas, justificando tu respuesta:

a) Una clase es un objeto que sirve como plantilla para crear otros objetos.

b) Dos objetos de la misma clase siempre tienen el mismo estado.

c) El encapsulamiento permite que un objeto oculte cómo implementa sus operaciones.

d) La abstracción es un proceso objetivo: dado un problema, siempre produce el mismo modelo.

e) Si dos objetos tienen exactamente los mismos valores en sus atributos, son el mismo objeto.
```

```{solution} ej-conceptos-basicos
:class: dropdown

a) **Falso**. Una clase NO es un objeto; es una definición o especificación. La clase describe cómo serán los objetos, pero ella misma no es un objeto.

b) **Falso**. Dos objetos de la misma clase tienen la misma ESTRUCTURA (los mismos atributos), pero cada uno tiene sus PROPIOS valores. Juan y María son ambos instancias de Persona, pero tienen nombres diferentes.

c) **Verdadero**. El encapsulamiento tiene dos aspectos: fusionar datos y comportamiento, y ocultar los detalles de implementación. El usuario del objeto solo ve la interfaz pública.

d) **Falso**. La abstracción es SUBJETIVA y CONTEXTUAL. Depende del objetivo del sistema. Una persona se modela diferente en un sistema universitario que en un gimnasio.

e) **Falso**. Dos objetos con el mismo estado siguen siendo OBJETOS DIFERENTES (diferente identidad física). Pueden ser EQUIVALENTES según algún criterio de igualdad lógica, pero no son "el mismo objeto".
```

```{exercise}
:label: ej-heuristica-hospital

Aplicá las heurísticas de análisis al siguiente requerimiento. Identificá clases, atributos y métodos candidatos.

> "Un **hospital** necesita gestionar las **citas médicas**. Los **pacientes** tienen **nombre**, **DNI**, **obra social** y **teléfono**. Los **médicos** tienen **nombre**, **matrícula** y **especialidad**. Una cita registra el **paciente**, el **médico**, la **fecha y hora**, y el **consultorio** donde se realizará. Los pacientes pueden **solicitar** citas y **cancelarlas**. Los médicos pueden **confirmar** o **reprogramar** citas."
```

````{solution} ej-heuristica-hospital
:class: dropdown

**Sustantivos identificados:**

| Sustantivo | Clasificación | Justificación |
|------------|---------------|---------------|
| Hospital | Clase | Sistema coordinador |
| Citas médicas | Clase | Entidad central con relaciones |
| Pacientes | Clase | Entidad con comportamiento propio |
| Nombre | Atributo | Dato simple |
| DNI | Atributo/Clase | Simple si solo se guarda, Clase si hay validación |
| Obra social | ¿Atributo/Clase? | Podría tener código, nombre, cobertura |
| Teléfono | Atributo | Dato simple |
| Médicos | Clase | Entidad con comportamiento propio |
| Matrícula | Atributo | Identificador simple |
| Especialidad | ¿Atributo/Clase? | Simple si es texto, Clase si tiene más datos |
| Fecha y hora | Atributo | Usar tipo temporal del lenguaje |
| Consultorio | ¿Atributo/Clase? | Clase si tiene ubicación, capacidad, equipamiento |

**Verbos identificados:**

| Verbo | Actor | Método candidato |
|-------|-------|------------------|
| Solicitar | Paciente | `solicitarCita(medico, fecha)` |
| Cancelar | Paciente | `cancelarCita(cita)` |
| Confirmar | Médico | `confirmarCita(cita)` |
| Reprogramar | Médico | `reprogramarCita(cita, nuevaFecha)` |

**Modelo propuesto:**

```{figure} 01/ejercicio_hospital_modelo.svg
:label: fig-ejercicio-hospital-modelo
:align: center
:width: 85%

Modelo de clases propuesto para la resolución del ejercicio del Hospital.
```
````

```{exercise}
:label: ej-filtro-complejidad

Para cada uno de los siguientes conceptos, decidí si debe ser un **valor simple** o una **clase independiente** en el contexto indicado. Justificá tu decisión.

    a) **Temperatura** en un sistema de monitoreo climático.
    b) **Precio** en un sistema de comercio electrónico.
    c) **Dirección de email** en un sistema de newsletter.
    d) **Coordenadas GPS** en un sistema de delivery.
    e) **Nombre de archivo** en un sistema de gestión documental.
```

```{solution} ej-filtro-complejidad
:class: dropdown

a) **Temperatura → Clase**
   - Tiene múltiples componentes: valor numérico + unidad (°C, °F, K).
   - Operaciones: convertir entre unidades, verificar si está en rango, comparar.
   - Validaciones: temperaturas imposibles (< -273.15°C).

b) **Precio → Clase**
   - Tiene múltiples componentes: monto + moneda.
   - Operaciones: convertir monedas, aplicar descuentos, sumar precios.
   - Un sistema de e-commerce que opere en múltiples países necesita manejar monedas.

c) **Dirección de email → Clase o Valor simple, depende**
   - Si solo se guarda y envía: valor simple (texto).
   - Si se valida formato, se extrae dominio, se verifica existencia: clase.
   - Para newsletter básico: probablemente texto sea suficiente.

d) **Coordenadas GPS → Clase**
   - Múltiples componentes: latitud + longitud.
   - Operaciones: calcular distancia entre puntos, verificar si está en zona.
   - Para delivery, es crítico poder calcular distancias y rutas.

e) **Nombre de archivo → Clase o Valor simple, depende**
   - Si solo se guarda: valor simple (texto).
   - Si se necesita extraer extensión, validar caracteres permitidos, generar nombre único: clase.
   - Sistema de gestión documental: probablemente clase para manejar extensiones, versionado, etc.
```

```{exercise}
:label: ej-abstraccion-contextual

El concepto "Libro" aparece en tres sistemas diferentes. Para cada uno, identificá qué atributos y métodos serían relevantes:

a) Sistema de biblioteca (préstamos y devoluciones).

b) Sistema de librería online (venta de libros).

c) Sistema de recomendaciones de lectura.
```

````{solution} ej-abstraccion-contextual
:class: dropdown

```{figure} 01/ejercicio_abstraccion_contextos.svg
:label: fig-ejercicio-abstraccion-contextos
:align: center
:width: 85%

Comparación de modelos para el concepto "Libro" en diferentes contextos de aplicación.
```

**Observación clave:** El mismo concepto del mundo real (un libro) produce modelos completamente diferentes según el objetivo del sistema. Esto demuestra que la abstracción es contextual.
````

## Próximo paso

Para seguir, conviene pasar a [el material siguiente](02_oop_relaciones.md), donde el recorrido continúa sobre esta base.
