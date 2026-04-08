---
title: Análisis y Diseño Orientado a Objetos
description:
  Introducción a los conceptos fundamentales de la Programación Orientada a Objetos.
  De la programación estructurada a la fusión de datos y comportamiento.
---

(analisis-y-diseno-orientado-a-objetos)=
# Análisis y Diseño Orientado a Objetos

Este capítulo presenta la transición del paradigma estructurado al paradigma orientado a objetos. Se introducen los conceptos fundamentales que permiten modelar sistemas de software como conjuntos de objetos que colaboran entre sí, y se desarrollan las heurísticas para identificar estos objetos a partir de los requerimientos del sistema.

(introduccion-la-transicion-de-paradigma)=
## Introducción: La transición de paradigma

(un-pequeno-flashback-a-programacion-1)=
### Un pequeño flashback a Programación 1

En la correlativa anterior, se trabajó con el lenguaje C y el paradigma estructurado. Este paradigma se caracteriza por una clara **separación entre datos y código**:

- **Datos**: Estructurados en `struct`, arreglos, tipos primitivos.
- **Código**: Organizado en funciones y procedimientos que operan sobre esos datos.

Esta separación implica que los datos son entidades pasivas que esperan ser manipuladas por funciones externas. Considerá el siguiente ejemplo en C:

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

En este modelo, `Persona` es simplemente un **contenedor de datos**, y `saludar()` es una **función independiente** que recibe esos datos como parámetro. No hay una conexión lógica o estructural entre la definición de `Persona` y las operaciones que se pueden realizar sobre una persona.

(el-limite-del-paradigma-estructurado)=
### El límite del paradigma estructurado

El paradigma estructurado funciona bien para problemas simples, pero muestra sus limitaciones cuando la complejidad crece. Considerá el siguiente requerimiento:

:::{admonition} Planteo del problema
:class: note

¿Qué pasaría si se necesita que distintas personas puedan saludarse de diferentes formas según su nacionalidad?

- Los argentinos dicen "¿Cómo andás?"
- Los españoles dicen "¿Qué tal?"
- Los estadounidenses dicen "How are you?"
:::

En el paradigma estructurado, la solución típica sería crear múltiples funciones o agregar lógica condicional compleja:

```c
void saludar(struct Persona p) {
    if (strcmp(p.nacionalidad, "Argentina") == 0) {
        printf("¿Cómo andás?\n");
    } else if (strcmp(p.nacionalidad, "España") == 0) {
        printf("¿Qué tal?\n");
    } else if (strcmp(p.nacionalidad, "Estados Unidos") == 0) {
        printf("How are you?\n");
    } else {
        printf("Hola\n");
    }
}
```

Este enfoque presenta varios problemas:

1. **Acoplamiento alto**: La función `saludar()` debe conocer todos los tipos de nacionalidades posibles.
2. **Difícil de extender**: Agregar una nueva nacionalidad requiere modificar el código existente.
3. **Responsabilidad mal ubicada**: ¿Por qué una función externa debe saber cómo saluda cada nacionalidad?
4. **Dispersión del conocimiento**: El comportamiento relacionado con "Persona" está fragmentado en múltiples funciones.

(la-solucion-de-la-oop-fusion-de-datos-y-comportamiento)=
### La solución de la OOP: Fusión de datos y comportamiento

La Programación Orientada a Objetos propone un cambio fundamental: **fusionar el dato y el comportamiento en una misma entidad**. Esta fusión se llama **encapsulamiento**, y es la piedra angular del paradigma orientado a objetos.

En Java, el ejemplo anterior se transformaría en:

```java
// Datos y comportamiento fusionados en una clase
public class Persona {
    // ESTADO: Datos (atributos)
    private String nombre;
    private int edad;
    private String nacionalidad;
    
    // COMPORTAMIENTO: Código (métodos)
    public void saludar() {
        switch (nacionalidad) {
            case "Argentina" -> System.out.println("¿Cómo andás?");
            case "España" -> System.out.println("¿Qué tal?");
            case "Estados Unidos" -> System.out.println("How are you?");
            default -> System.out.println("Hola");
        }
    }
}

// Uso
Persona juan = new Persona();
juan.saludar();  // El objeto sabe cómo saludarse a sí mismo
```

Las ventajas de este enfoque son inmediatas:

1. **Cohesión alta**: El dato (`nacionalidad`) y el comportamiento (`saludar()`) están juntos.
2. **Responsabilidad clara**: Cada `Persona` sabe cómo saludarse.
3. **Facilidad de extensión**: Se puede crear subclases para nacionalidades específicas.
4. **Encapsulamiento**: Los detalles internos están ocultos al mundo exterior.

:::{important}
La clave del paradigma orientado a objetos es pensar en **objetos que colaboran** enviándose mensajes entre sí, en lugar de pensar en funciones que operan sobre datos pasivos.
:::

(glosario-fundamental-conceptos-clave)=
## Glosario Fundamental: Conceptos Clave

(definiendo-las-bases)=
### Definiendo las bases

(concepto-clase)=
#### Clase

:::{admonition} Definición: Clase
:class: note

Una **clase** es un conjunto de elementos con características comunes. Es el **molde** o **plantilla** que define la estructura y el comportamiento que tendrán los objetos creados a partir de ella.
:::

La clase es una abstracción que describe:
- **Qué información** puede almacenar (atributos).
- **Qué operaciones** puede realizar (métodos).

Ejemplo:

```java
// La clase "Persona" es el molde
public class Persona {
    // Atributos: qué información tiene una persona
    private String nombre;
    private int edad;
    
    // Métodos: qué puede hacer una persona
    public void saludar() {
        System.out.println("Hola, soy " + nombre);
    }
}
```

:::{tip}
En el paradigma orientado a objetos, la clase es el **concepto abstracto** (la idea de "Persona" en general), mientras que el objeto es la **instancia concreta** (Juan, María, Pedro).
:::

(concepto-objeto)=
#### Objeto

:::{admonition} Definición: Objeto
:class: note

Un **objeto** es la entidad viva en el sistema, la materialización concreta de una clase. Está compuesto indivisiblemente por:

1. **Estado**: Los valores específicos de sus atributos en un momento dado.
2. **Comportamiento**: Las operaciones que puede realizar (métodos).
:::

Cada objeto es una instancia única e independiente de su clase:

```java
// Creación de objetos (instancias de la clase Persona)
Persona juan = new Persona();    // Objeto 1
Persona maria = new Persona();   // Objeto 2

// Cada objeto tiene su propio estado independiente
juan.setNombre("Juan");
juan.setEdad(25);

maria.setNombre("María");
maria.setEdad(30);

// Cada objeto ejecuta su propio comportamiento
juan.saludar();   // "Hola, soy Juan"
maria.saludar();  // "Hola, soy María"
```

:::{important}
Aunque `juan` y `maria` provienen de la misma clase (`Persona`), son objetos **completamente independientes**. Modificar el estado de uno no afecta al otro.
:::

(estructura-interna-del-objeto)=
### Estructura interna del Objeto

(concepto-atributo)=
#### Atributo

:::{admonition} Definición: Atributo
:class: note

Un **atributo** es la información definida en una clase que, al recibir valores concretos, se transforma en el estado de un objeto.
:::

Los atributos son las **variables de instancia** de la clase. Cada objeto tendrá su propia copia de estos atributos con valores independientes:

```java
public class Vehiculo {
    // Atributos: las variables que definen la información del vehículo
    private String patente;
    private String marca;
    private String modelo;
    private int anioFabricacion;
    private int kilometraje;
}
```

:::{seealso}
Sobre la nomenclatura y visibilidad de atributos, consultá la regla {ref}`regla-0x2001`.
:::

(concepto-estado)=
#### Estado

:::{admonition} Definición: Estado
:class: note

El **estado** es la combinación específica de valores en los atributos de un objeto en un momento determinado. El estado le pertenece exclusivamente a una sola instancia, garantizando su independencia.
:::

El estado es dinámico: puede cambiar a lo largo de la vida del objeto mediante la ejecución de métodos:

```java
Vehiculo auto = new Vehiculo();

// Estado inicial (valores por defecto)
// patente = null
// marca = null
// kilometraje = 0

auto.setPatente("ABC123");
auto.setMarca("Ford");
auto.setKilometraje(50000);

// Estado actual:
// patente = "ABC123"
// marca = "Ford"
// kilometraje = 50000

auto.recorrer(100);  // Método que modifica el estado

// Estado nuevo:
// patente = "ABC123"
// marca = "Ford"
// kilometraje = 50100  // Cambió
```

(concepto-instancia)=
#### Instancia

:::{admonition} Definición: Instancia
:class: note

Una **instancia** es la materialización concreta y específica de una clase en memoria, donde los atributos tienen valores particulares y se convierten en estado.
:::

"Instancia" y "objeto" son términos sinónimos en la práctica, aunque "instancia" enfatiza la relación con su clase:

```java
// "vehiculo1" es una instancia de la clase Vehiculo
Vehiculo vehiculo1 = new Vehiculo();

// "vehiculo2" es otra instancia de la misma clase
Vehiculo vehiculo2 = new Vehiculo();

// Verificación: ambos son instancias de Vehiculo
System.out.println(vehiculo1 instanceof Vehiculo);  // true
System.out.println(vehiculo2 instanceof Vehiculo);  // true
```

(ciclo-de-vida-y-abstraccion)=
### Ciclo de vida y Abstracción

(concepto-construccion)=
#### Construcción

:::{admonition} Definición: Construcción
:class: note

La **construcción** es el comportamiento dedicado a tomar una clase (el molde) y darle valores iniciales a los atributos para crear un objeto. Es el proceso de **instanciación** de una clase.
:::

En Java, la construcción se realiza mediante:
1. El operador `new`.
2. La invocación de un **constructor**.

```java
public class Producto {
    private String nombre;
    private double precio;
    
    // Constructor: método especial para la construcción
    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
}

// Construcción de objetos
Producto laptop = new Producto("Laptop Dell", 850000.0);
Producto mouse = new Producto("Mouse Logitech", 15000.0);
```

:::{seealso}
Sobre la inicialización de atributos en constructores, consultá la regla {ref}`regla-0x2002`.
:::

(concepto-destruccion)=
#### Destrucción

:::{admonition} Definición: Destrucción
:class: note

La **destrucción** es el final de la vida útil del objeto, cuando el objeto ya no es necesario y se libera la memoria que ocupaba.
:::

En lenguajes como C, el programador debe liberar manualmente la memoria con `free()`. En Java, este proceso es automático gracias al **Garbage Collector** (Recolector de Basura):

```java
Producto temp = new Producto("Temporal", 100.0);
// ... uso del objeto ...
temp = null;  // El objeto queda sin referencias

// En algún momento, el Garbage Collector liberará automáticamente
// la memoria ocupada por el objeto "Temporal"
```

:::{tip}
En Java **no hay `free()`**. El Garbage Collector se encarga de detectar objetos sin referencias y liberar su memoria automáticamente. Esto elimina problemas comunes como memory leaks y doble liberación.
:::

(concepto-identidad)=
#### Identidad

:::{admonition} Definición: Identidad
:class: note

La **identidad** es lo que diferencia a un objeto de otro. En Java existen dos tipos de identidad:

1. **Identidad Física**: Dirección de memoria del objeto (operador `==`).
2. **Identidad Lógica**: Semántica del dominio (método `.equals()`).
:::

##### Identidad Física

La identidad física compara si dos referencias apuntan al **mismo objeto en memoria**:

```java
Persona p1 = new Persona("Juan", 25);
Persona p2 = new Persona("Juan", 25);
Persona p3 = p1;  // p3 apunta al mismo objeto que p1

// Comparación de identidad física
System.out.println(p1 == p2);  // false (objetos diferentes en memoria)
System.out.println(p1 == p3);  // true (misma referencia)
```

##### Identidad Lógica

La identidad lógica compara si dos objetos son **semánticamente equivalentes** según las reglas del dominio:

```java
public class Persona {
    private String nombre;
    private int edad;
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Persona otra)) {
            return false;
        }
        // Dos personas son "iguales" si tienen mismo nombre y edad
        return this.nombre.equals(otra.nombre) && 
               this.edad == otra.edad;
    }
}

Persona p1 = new Persona("Juan", 25);
Persona p2 = new Persona("Juan", 25);

System.out.println(p1 == p2);        // false (identidad física)
System.out.println(p1.equals(p2));   // true (identidad lógica)
```

:::{seealso}
Sobre la implementación correcta de `equals()` y `hashCode()`, consultá las reglas {ref}`regla-0x2004` y {ref}`regla-0x200E`.
:::

(concepto-abstraccion)=
#### Abstracción

:::{admonition} Definición: Abstracción
:class: note

La **abstracción** es el proceso de identificar las características esenciales de una entidad para el objetivo del sistema, dejando de lado los detalles que no son relevantes.
:::

La abstracción es fundamental en el diseño orientado a objetos porque permite:
- Reducir la complejidad.
- Enfocarse en lo importante.
- Crear modelos manejables del mundo real.

Ejemplo: Sistema de gestión universitaria

```java
// Una "Persona" real tiene infinitas características:
// - Color de pelo
// - Altura
// - Peso
// - Color de ojos
// - Huella dactilar
// - ADN
// - etc.

// PERO, para un sistema universitario, solo importan:
public class Persona {
    // ✅ Esencial para el sistema universitario
    private String nombre;
    private String dni;
    private LocalDate fechaNacimiento;
    private String email;
    
    // ❌ NO están: colorDePelo, altura, peso
    // Esos detalles NO son relevantes para el objetivo del sistema
}
```

:::{important}
La abstracción depende del **contexto** y del **objetivo del sistema**. Los mismos conceptos pueden modelarse de formas diferentes según el dominio.
:::

**Ejemplo de abstracción contextual:**

Sistema de control de acceso (gimnasio):
```java
public class Persona {
    private String nombre;
    private LocalDate fechaNacimiento;
    private byte[] huellaDactilar;  // ✅ Relevante para este contexto
    private byte[] fotoRostro;      // ✅ Relevante para reconocimiento
}
```

Sistema académico:
```java
public class Persona {
    private String nombre;
    private String dni;
    private String legajo;          // ✅ Relevante para este contexto
    private List<Materia> cursadas; // ✅ Relevante para lo académico
}
```

(modelado-de-objetos-heuristicas-de-analisis)=
## Modelado de Objetos: Heurísticas de Análisis

Una de las habilidades más importantes en la programación orientada a objetos es **identificar objetos y su comportamiento** a partir de los requerimientos. Esta sección presenta técnicas prácticas para realizar esta transformación.

(la-heuristica-linguistica)=
### La Heurística Lingüística

La heurística lingüística es una técnica que permite identificar candidatos a clases y métodos mediante el análisis del lenguaje natural usado en los requerimientos.

(analisis-de-sustantivos)=
#### Análisis de Sustantivos

:::{admonition} Regla: Sustantivos → Clases o Atributos
:class: tip

Los **sustantivos** en los requerimientos son candidatos primarios para convertirse en:
- **Clases**: Si representan entidades con comportamiento propio.
- **Atributos**: Si representan características de una entidad.
:::

**Ejemplo de análisis:**

> "El sistema debe permitir que un **cliente** realice una **reserva** de un **vehículo** especificando la **fecha** de inicio y la **fecha** de fin. El **vehículo** tiene una **patente**, una **marca** y un **modelo**."

Sustantivos identificados:
- **Cliente** → ¿Clase?
- **Reserva** → ¿Clase?
- **Vehículo** → ¿Clase?
- **Fecha** → ¿Clase o Atributo?
- **Patente** → ¿Atributo?
- **Marca** → ¿Atributo?
- **Modelo** → ¿Atributo?

(analisis-de-verbos)=
#### Análisis de Verbos

:::{admonition} Regla: Verbos → Métodos
:class: tip

Los **verbos** en los requerimientos son candidatos directos para definir el **comportamiento** (métodos) de las clases. También ayudan a identificar los mensajes que los objetos se envían entre sí.
:::

**Continuando el ejemplo:**

> "El sistema debe permitir que un cliente **realice** una reserva de un vehículo especificando la fecha de inicio y la fecha de fin. El vehículo **puede verificar** su disponibilidad en un período dado."

Verbos identificados:
- **Realice** (una reserva) → Método `realizarReserva()` en clase `Cliente` o `SistemaReservas`
- **Verificar** (disponibilidad) → Método `verificarDisponibilidad()` en clase `Vehiculo`

Diseño preliminar:

```java
public class Cliente {
    private String nombre;
    private String dni;
    
    public void realizarReserva(Vehiculo vehiculo, LocalDate inicio, LocalDate fin) {
        // Lógica para crear una reserva
    }
}

public class Vehiculo {
    private String patente;
    private String marca;
    private String modelo;
    
    public boolean verificarDisponibilidad(LocalDate inicio, LocalDate fin) {
        // Lógica para verificar disponibilidad
        return true;
    }
}

public class Reserva {
    private Cliente cliente;
    private Vehiculo vehiculo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
```

(aplicacion-de-filtros)=
### Aplicación de Filtros: Refinando el diseño

Una vez identificados los candidatos mediante la heurística lingüística, es necesario aplicar filtros para refinar el diseño y evitar clases innecesarias.

(filtro-de-abstraccion)=
#### El Filtro de Abstracción

:::{admonition} Filtro: Abstracción
:class: warning

**Eliminar** todo sustantivo o verbo que no aporte valor al objetivo específico del sistema.
:::

**Ejemplo:**

> "El sistema debe registrar **personas** con su **nombre**, **DNI**, **fecha de nacimiento** y **color de pelo** para gestionar el acceso a un edificio."

Análisis:
- **Nombre** → ✅ Esencial (identificación)
- **DNI** → ✅ Esencial (identificación única)
- **Fecha de nacimiento** → ⚠️ ¿Realmente necesaria para control de acceso?
- **Color de pelo** → ❌ Irrelevante para el objetivo

Aplicando el filtro de abstracción:

```java
public class Persona {
    private String nombre;          // ✅ Mantener
    private String dni;             // ✅ Mantener
    // private LocalDate fechaNacimiento;  // ❌ Eliminar (no aporta valor)
    // private String colorDePelo;         // ❌ Eliminar (irrelevante)
}
```

(filtro-de-complejidad)=
#### El Filtro de Complejidad: ¿Primitivo vs. Clase nueva?

:::{admonition} Filtro: Complejidad
:class: warning

Decidir si un sustantivo debe ser:
- **Atributo primitivo**: Si es un valor simple (String, int, double).
- **Clase independiente**: Si tiene su propio estado y comportamiento.
:::

Esta es una de las decisiones más difíciles para programadores novatos. La regla general:

**Si el sustantivo tiene:**
- Solo un valor → **Atributo primitivo**
- Múltiples valores relacionados + comportamiento → **Clase nueva**

**Ejemplo: ¿`fechaNacimiento` es un atributo o una clase?**

Opción 1: Atributo primitivo
```java
public class Persona {
    private String nombre;
    private String dni;
    private String fechaNacimiento;  // ❌ String: "15/03/1998"
    
    public int calcularEdad() {
        // ❌ Lógica compleja para parsear el String y calcular edad
        String[] partes = fechaNacimiento.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int anio = Integer.parseInt(partes[2]);
        // ... cálculo complejo ...
    }
}
```

Opción 2: Clase independiente (Aplicando filtro de complejidad)
```java
public class Fecha {
    private int dia;
    private int mes;
    private int anio;
    
    // ✅ La clase Fecha encapsula su propia lógica
    public int calcularDiferencia(Fecha otra) {
        // Lógica de comparación de fechas
    }
    
    public boolean esAnteriorA(Fecha otra) {
        // Lógica de comparación
    }
}

public class Persona {
    private String nombre;
    private String dni;
    private Fecha fechaNacimiento;  // ✅ Objeto con comportamiento propio
    
    public int calcularEdad() {
        // ✅ Delega en el objeto Fecha
        return fechaNacimiento.calcularDiferencia(Fecha.hoy());
    }
}
```

:::{note}
En Java moderno, para fechas se utiliza la clase `LocalDate` de la API estándar, que ya encapsula todo el comportamiento relacionado con fechas. Esto ejemplifica el concepto: las fechas son lo suficientemente complejas como para merecer una clase propia.
:::

**Otro ejemplo: DNI**

¿El DNI debe ser un `String` o una clase `DNI`?

Análisis:
- ¿Tiene validaciones específicas? (formato, dígito verificador)
- ¿Tiene operaciones propias? (formateo, comparación)
- ¿Se repite la lógica en múltiples lugares?

Si la respuesta es **sí** a varias preguntas → **Clase nueva**

```java
public class DNI {
    private String numero;
    
    public DNI(String numero) {
        if (!validar(numero)) {
            throw new IllegalArgumentException("DNI inválido");
        }
        this.numero = numero;
    }
    
    private boolean validar(String numero) {
        // ✅ Lógica de validación encapsulada
        return numero.matches("\\d{7,8}");
    }
    
    public String formatear() {
        // ✅ Formateo: "12345678" → "12.345.678"
        return numero.replaceAll("(\\d{2})(\\d{3})(\\d{3})", "$1.$2.$3");
    }
}

public class Persona {
    private String nombre;
    private DNI dni;  // ✅ Clase con validación y formateo propios
}
```

(aplicacion-practica)=
## Aplicación Práctica

(ejemplo-completo-sistema-de-biblioteca)=
### Ejemplo completo: Sistema de Biblioteca

**Requerimiento:**

> "El sistema debe permitir que un **socio** de la biblioteca **tome prestado** un **libro**. Cada libro tiene un **título**, un **autor**, un **ISBN** y un **año de publicación**. El socio tiene un **nombre**, un **número de socio** y un **domicilio**. Un préstamo registra la **fecha de inicio** y la **fecha de devolución**."

#### Paso 1: Análisis de sustantivos

Sustantivos identificados:
- Socio
- Libro
- Título
- Autor
- ISBN
- Año de publicación
- Nombre
- Número de socio
- Domicilio
- Préstamo
- Fecha de inicio
- Fecha de devolución

#### Paso 2: Análisis de verbos

Verbos identificados:
- Tomar prestado

#### Paso 3: Aplicar filtro de complejidad

| Sustantivo | ¿Clase o Atributo? | Justificación |
|------------|-------------------|---------------|
| Socio | **Clase** | Entidad con estado y comportamiento |
| Libro | **Clase** | Entidad con estado y comportamiento |
| Título | Atributo (String) | Valor simple |
| Autor | **Clase** | Puede tener múltiples libros, nombre, biografía |
| ISBN | Atributo (String) | Valor simple (aunque podría ser clase si hay validaciones complejas) |
| Año de publicación | Atributo (int) | Valor simple |
| Nombre | Atributo (String) | Valor simple |
| Número de socio | Atributo (String) | Valor simple |
| Domicilio | **Clase** | Tiene calle, número, ciudad, código postal |
| Préstamo | **Clase** | Entidad que relaciona Socio y Libro |
| Fecha de inicio | Atributo (LocalDate) | Usar clase de la API estándar |
| Fecha de devolución | Atributo (LocalDate) | Usar clase de la API estándar |

#### Paso 4: Diseño de clases

```java
public class Autor {
    private String nombre;
    private String nacionalidad;
    
    public Autor(String nombre, String nacionalidad) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
    }
    
    // Getters
    public String getNombre() { return nombre; }
}

public class Domicilio {
    private String calle;
    private String numero;
    private String ciudad;
    private String codigoPostal;
    
    public Domicilio(String calle, String numero, String ciudad, String codigoPostal) {
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
    }
    
    @Override
    public String toString() {
        return calle + " " + numero + ", " + ciudad + " (CP: " + codigoPostal + ")";
    }
}

public class Libro {
    private String titulo;
    private Autor autor;
    private String isbn;
    private int anioPublicacion;
    private boolean disponible;
    
    public Libro(String titulo, Autor autor, String isbn, int anioPublicacion) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anioPublicacion = anioPublicacion;
        this.disponible = true;
    }
    
    public boolean estaDisponible() {
        return disponible;
    }
    
    public void marcarComoPrestado() {
        this.disponible = false;
    }
    
    public void marcarComoDevuelto() {
        this.disponible = true;
    }
    
    // Getters
    public String getTitulo() { return titulo; }
    public Autor getAutor() { return autor; }
}

public class Socio {
    private String nombre;
    private String numeroSocio;
    private Domicilio domicilio;
    private List<Prestamo> prestamosActivos;
    
    public Socio(String nombre, String numeroSocio, Domicilio domicilio) {
        this.nombre = nombre;
        this.numeroSocio = numeroSocio;
        this.domicilio = domicilio;
        this.prestamosActivos = new ArrayList<>();
    }
    
    public Prestamo tomarPrestado(Libro libro) {
        if (!libro.estaDisponible()) {
            throw new IllegalStateException("El libro no está disponible");
        }
        
        Prestamo prestamo = new Prestamo(this, libro, LocalDate.now());
        prestamosActivos.add(prestamo);
        libro.marcarComoPrestado();
        
        return prestamo;
    }
    
    public void devolverLibro(Prestamo prestamo) {
        prestamo.registrarDevolucion(LocalDate.now());
        prestamosActivos.remove(prestamo);
        prestamo.getLibro().marcarComoDevuelto();
    }
}

public class Prestamo {
    private Socio socio;
    private Libro libro;
    private LocalDate fechaInicio;
    private LocalDate fechaDevolucion;
    
    public Prestamo(Socio socio, Libro libro, LocalDate fechaInicio) {
        this.socio = socio;
        this.libro = libro;
        this.fechaInicio = fechaInicio;
        this.fechaDevolucion = null;  // Aún no devuelto
    }
    
    public void registrarDevolucion(LocalDate fecha) {
        this.fechaDevolucion = fecha;
    }
    
    public boolean estaVencido() {
        LocalDate fechaLimite = fechaInicio.plusDays(15);  // 15 días de plazo
        return fechaDevolucion == null && LocalDate.now().isAfter(fechaLimite);
    }
    
    // Getters
    public Libro getLibro() { return libro; }
}
```

#### Paso 5: Uso del sistema

```java
// Crear objetos
Autor borges = new Autor("Jorge Luis Borges", "Argentina");
Libro ficciones = new Libro("Ficciones", borges, "978-0802130303", 1944);

Domicilio domicilioJuan = new Domicilio("San Martín", "1234", "Bariloche", "8400");
Socio juan = new Socio("Juan Pérez", "S001", domicilioJuan);

// Realizar préstamo
Prestamo prestamo = juan.tomarPrestado(ficciones);

// Verificar disponibilidad
System.out.println(ficciones.estaDisponible());  // false

// Devolver libro
juan.devolverLibro(prestamo);

// Verificar disponibilidad nuevamente
System.out.println(ficciones.estaDisponible());  // true
```

(dinamica-de-taller-en-clase)=
### Dinámica de Taller en Clase

:::{admonition} Actividad: Cazadores y Auditores
:class: tip

**Objetivo:** Aplicar las heurísticas sobre un mismo texto con objetivos de sistema divergentes.

**Enunciado:**
> "Una empresa de transporte necesita gestionar sus **vehículos**. Cada vehículo tiene una **patente**, una **marca**, un **modelo**, un **color** y un **kilometraje** actual. Los vehículos realizan **viajes** que tienen un **origen**, un **destino** y una **distancia**."

**Consigna Grupo A (Cazadores):**
Diseñar un sistema para **mantenimiento preventivo** de vehículos.

**Consigna Grupo B (Auditores):**
Diseñar un sistema para **liquidación de viáticos** a conductores.

**Reflexión:**
- ¿Qué atributos son relevantes para cada grupo?
- ¿Qué comportamientos (métodos) necesita cada diseño?
- ¿El `color` del vehículo es relevante en algún caso?
- ¿La `distancia` del viaje es igualmente importante para ambos?
:::

(resumen)=
## Resumen

En este capítulo se ha cubierto:

1. **La transición del paradigma estructurado al orientado a objetos**: Fusión de datos y comportamiento.

2. **Conceptos fundamentales**:
   - Clase: molde o plantilla.
   - Objeto: instancia viva con estado y comportamiento.
   - Atributo: información definida en la clase.
   - Estado: valores específicos de los atributos.
   - Instancia: materialización concreta de una clase.

3. **Ciclo de vida**:
   - Construcción: creación del objeto.
   - Destrucción: liberación automática por el Garbage Collector.
   - Identidad: física (dirección de memoria) vs. lógica (semántica).

4. **Abstracción**: Identificar características esenciales según el contexto.

5. **Heurísticas de análisis**:
   - Sustantivos → Clases o Atributos.
   - Verbos → Métodos.
   - Filtro de abstracción: eliminar lo irrelevante.
   - Filtro de complejidad: decidir entre primitivo y clase nueva.

:::{important}
El diseño orientado a objetos es un **proceso iterativo**. Las clases identificadas inicialmente pueden refinarse, dividirse o combinarse a medida que se comprende mejor el dominio del problema.
:::

(lecturas-recomendadas)=
## Lecturas Recomendadas

- Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley.
- Martin, R. C. (2017). *Clean Architecture: A Craftsman's Guide to Software Structure and Design*. Prentice Hall.
- Bloch, J. (2018). *Effective Java* (3rd ed.). Addison-Wesley.

(ejercicios)=
## Ejercicios

```{exercise}
:label: ej-oop-banco

Dado el siguiente requerimiento, identificá las clases, atributos y métodos aplicando las heurísticas presentadas:

> "Un banco necesita un sistema para gestionar cuentas bancarias. Cada cuenta tiene un número de cuenta, un titular, un saldo y un tipo (caja de ahorro o cuenta corriente). Los clientes pueden depositar dinero, retirar dinero y consultar el saldo. Las cuentas corrientes permiten un descubierto de hasta $10,000."
```

```{solution} ej-oop-banco
:class: dropdown

**Sustantivos identificados:**
- Banco → Clase (coordinador del sistema)
- Cuenta bancaria → Clase
- Número de cuenta → Atributo (String)
- Titular → Clase (tiene nombre, DNI, etc.)
- Saldo → Atributo (double)
- Tipo → Atributo o Jerarquía (caja de ahorro vs cuenta corriente)
- Cliente → Clase (igual a Titular)
- Dinero → Atributo (double)
- Descubierto → Atributo de CuentaCorriente (double)

**Verbos identificados:**
- Depositar → Método `depositar(double monto)`
- Retirar → Método `retirar(double monto)`
- Consultar → Método `getSaldo()`
- Permitir → Lógica de validación en `retirar()`

**Diseño propuesto:**

```java
public class Titular {
    private String nombre;
    private String dni;
}

public abstract class CuentaBancaria {
    protected String numeroCuenta;
    protected Titular titular;
    protected double saldo;
    
    public void depositar(double monto) {
        saldo += monto;
    }
    
    public abstract void retirar(double monto);
    
    public double getSaldo() {
        return saldo;
    }
}

public class CajaDeAhorro extends CuentaBancaria {
    @Override
    public void retirar(double monto) {
        if (monto > saldo) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        saldo -= monto;
    }
}

public class CuentaCorriente extends CuentaBancaria {
    private static final double DESCUBIERTO_MAXIMO = 10000.0;
    
    @Override
    public void retirar(double monto) {
        if (monto > saldo + DESCUBIERTO_MAXIMO) {
            throw new IllegalStateException("Excede descubierto permitido");
        }
        saldo -= monto;
    }
}
```
```

```{exercise}
:label: ej-oop-filtro-complejidad

Para cada uno de los siguientes casos, decidí si debe ser un **atributo primitivo** o una **clase independiente**. Justificá tu respuesta.

a) Dirección de email  
b) Número de teléfono  
c) Coordenada geográfica (latitud y longitud)  
d) Nombre de persona
```

```{solution} ej-oop-filtro-complejidad
:class: dropdown

a) **Dirección de email**: 
   - ✅ **Clase independiente** si necesitás validaciones complejas (formato RFC), separación de usuario/dominio, etc.
   - ⚠️ **Atributo String** si solo lo guardás y mostrás sin validaciones.

b) **Número de teléfono**: 
   - ✅ **Clase independiente**: Tiene formato (código de área, número), validación de país, formateo para visualización.
   
c) **Coordenada geográfica**: 
   - ✅ **Clase independiente**: Dos valores relacionados (latitud, longitud) + operaciones (calcular distancia, verificar radio).

d) **Nombre de persona**: 
   - ⚠️ **Atributo String** en la mayoría de los casos.
   - ✅ **Clase independiente** si necesitás separar nombre/apellido, nombre completo, abreviación, internacionalización.
```
