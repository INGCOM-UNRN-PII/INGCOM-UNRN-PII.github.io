---
title: "Herencia y Polimorfismo"
subtitle: "Reutilización y Flexibilidad en el Diseño"
subject: Programación Orientada a Objetos
---

(java-herencia-polimorfismo)=
# Herencia y Polimorfismo

En el capítulo anterior ({ref}`java-sintaxis-clases`) aprendimos a crear clases, definir atributos y métodos, y establecer relaciones entre objetos. Ahora damos el siguiente paso: aprender a **reutilizar código** y **crear diseños flexibles** mediante dos mecanismos fundamentales de la POO.

Este capítulo cubre:

1. **Herencia**: Crear nuevas clases basadas en clases existentes
2. **Clases abstractas**: Definir plantillas incompletas
3. **Interfaces**: Establecer contratos de comportamiento
4. **Polimorfismo**: Tratar objetos de diferentes tipos de manera uniforme

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, serás capaz de:

1. Usar herencia para reutilizar código entre clases relacionadas
2. Distinguir cuándo usar clases abstractas vs interfaces
3. Implementar polimorfismo para escribir código flexible
4. Aplicar el principio de sustitución de Liskov
5. Diseñar jerarquías de clases coherentes
:::

:::{important}
**Requisitos previos**:
- Dominar la sintaxis de clases en Java ({ref}`java-sintaxis-clases`)
- Comprender encapsulamiento y modificadores de acceso
- Conocer las relaciones entre objetos (asociación, composición, agregación)
:::

---

(herencia-java)=
## Herencia: Extendiendo Clases

(que-es-herencia)=
### ¿Qué es la Herencia?

La **herencia** es un mecanismo que permite crear una nueva clase basada en una clase existente. La nueva clase **hereda** los atributos y métodos de la clase original, y puede agregar nuevos o modificar los existentes.

:::{note} Definición Formal

**Herencia** es una relación "es-un" (*is-a*) entre clases donde una clase derivada (subclase o clase hija) extiende las capacidades de una clase base (superclase o clase padre), heredando su estado y comportamiento.
:::

**Terminología:**

| Término | Sinónimos | Descripción |
| :--- | :--- | :--- |
| Superclase | Clase padre, clase base | La clase de la que se hereda |
| Subclase | Clase hija, clase derivada | La clase que hereda |
| Extender | Heredar, derivar | El acto de crear una subclase |

(motivacion-herencia)=
### ¿Por Qué Usar Herencia?

Considerá el siguiente escenario sin herencia:

```java
public class Empleado {
    private String nombre;
    private String dni;
    private double salarioBase;
    
    public double calcularSalario() {
        return salarioBase;
    }
    
    public String obtenerInformacion() {
        return nombre + " (DNI: " + dni + ")";
    }
}

public class Gerente {
    private String nombre;           // ¡Duplicado!
    private String dni;              // ¡Duplicado!
    private double salarioBase;      // ¡Duplicado!
    private double bonificacion;
    private String departamento;
    
    public double calcularSalario() {     // ¡Similar pero diferente!
        return salarioBase + bonificacion;
    }
    
    public String obtenerInformacion() {  // ¡Duplicado!
        return nombre + " (DNI: " + dni + ")";
    }
}
```

**Problemas:**

1. **Código duplicado**: `nombre`, `dni`, `salarioBase` y `obtenerInformacion()` están repetidos
2. **Mantenimiento difícil**: Cambios en la lógica común requieren modificar múltiples clases
3. **Inconsistencias**: Es fácil que las implementaciones diverjan accidentalmente

**Con herencia:**

```java
public class Empleado {
    private String nombre;
    private String dni;
    protected double salarioBase;  // protected: accesible por subclases
    
    public Empleado(String nombre, String dni, double salarioBase) {
        this.nombre = nombre;
        this.dni = dni;
        this.salarioBase = salarioBase;
    }
    
    public double calcularSalario() {
        return salarioBase;
    }
    
    public String obtenerInformacion() {
        return nombre + " (DNI: " + dni + ")";
    }
}

public class Gerente extends Empleado {
    private double bonificacion;
    private String departamento;
    
    public Gerente(String nombre, String dni, double salarioBase, 
                   double bonificacion, String departamento) {
        super(nombre, dni, salarioBase);  // Llama al constructor padre
        this.bonificacion = bonificacion;
        this.departamento = departamento;
    }
    
    @Override
    public double calcularSalario() {
        return salarioBase + bonificacion;  // Modifica el comportamiento
    }
    
    // obtenerInformacion() se hereda automáticamente
    
    public String obtenerDepartamento() {
        return departamento;
    }
}
```

```{mermaid}
classDiagram
    class Empleado {
        -String nombre
        -String dni
        #double salarioBase
        +Empleado(String, String, double)
        +calcularSalario() double
        +obtenerInformacion() String
    }
    
    class Gerente {
        -double bonificacion
        -String departamento
        +Gerente(String, String, double, double, String)
        +calcularSalario() double
        +obtenerDepartamento() String
    }
    
    class Desarrollador {
        -String lenguajePrincipal
        -int añosExperiencia
        +Desarrollador(String, String, double, String, int)
        +obtenerTecnologias() List~String~
    }
    
    Empleado <|-- Gerente
    Empleado <|-- Desarrollador
    
    note for Empleado "Clase base<br>define comportamiento común"
    note for Gerente "Hereda nombre, dni, salarioBase<br>Sobrescribe calcularSalario()"
    note for Desarrollador "Hereda todo de Empleado<br>Agrega atributos específicos"
```

**Beneficios:**

1. **Reutilización**: El código común está en un solo lugar
2. **Extensibilidad**: Fácil agregar nuevos tipos de empleados
3. **Consistencia**: La lógica común se mantiene sincronizada

---

(sintaxis-herencia)=
### Sintaxis de Herencia en Java

(palabra-extends)=
#### La Palabra Clave `extends`

Para crear una subclase, usamos la palabra clave `extends`:

```java
public class Subclase extends Superclase {
    // Atributos y métodos adicionales
}
```

:::{warning}
**Java solo permite herencia simple**: Una clase puede extender **una sola** superclase directa. No existe herencia múltiple de clases en Java.

```java
// ❌ ILEGAL en Java
public class Anfibio extends Animal, Vehiculo { }

// ✓ Legal: una sola superclase
public class Anfibio extends Animal { }
```
:::

(que-se-hereda)=
#### ¿Qué se Hereda?

Cuando una clase extiende otra, hereda:

| Se hereda | No se hereda |
| :--- | :--- |
| Atributos públicos y protegidos | Constructores |
| Métodos públicos y protegidos | Atributos privados (existen, pero no son accesibles) |
| Métodos de paquete (si están en el mismo paquete) | |

```java
public class Animal {
    private String nombre;        // No accesible directamente en subclases
    protected int edad;           // Accesible en subclases
    public String especie;        // Accesible en subclases
    
    public Animal(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() {   // Heredado
        return nombre;
    }
    
    protected void dormir() {     // Heredado
        System.out.println("Zzz...");
    }
}

public class Perro extends Animal {
    private String raza;
    
    public Perro(String nombre, String raza) {
        super(nombre);  // Llama al constructor de Animal
        this.raza = raza;
    }
    
    public void ladrar() {
        System.out.println(getNombre() + " dice: ¡Guau!");
        // nombre no es accesible directamente (es private)
        // pero getNombre() sí está disponible
        
        dormir();  // Método protegido heredado
        edad = 5;  // Atributo protegido accesible
    }
}
```

---

(constructor-super)=
### El Constructor y `super`

(llamada-super)=
#### Llamando al Constructor Padre

Los constructores **no se heredan**, pero la subclase debe inicializar la parte heredada del objeto. Para esto, usamos `super()`:

```java
public class Vehiculo {
    private String marca;
    private String modelo;
    
    public Vehiculo(String marca, String modelo) {
        this.marca = marca;
        this.modelo = modelo;
    }
}

public class Auto extends Vehiculo {
    private int cantidadPuertas;
    
    public Auto(String marca, String modelo, int cantidadPuertas) {
        super(marca, modelo);  // DEBE ser la primera línea
        this.cantidadPuertas = cantidadPuertas;
    }
}
```

:::{important}
**Reglas de `super()`:**

1. Si la superclase no tiene constructor sin parámetros, **debés** llamar a `super(...)` explícitamente
2. La llamada a `super()` **debe ser la primera instrucción** del constructor
3. Si no llamás a `super()` y la superclase tiene constructor sin parámetros, Java lo llama implícitamente
:::

**Ejemplo de constructor implícito:**

```java
public class Animal {
    // Constructor sin parámetros (implícito o explícito)
    public Animal() {
        System.out.println("Animal creado");
    }
}

public class Gato extends Animal {
    public Gato() {
        // super() se llama implícitamente aquí
        System.out.println("Gato creado");
    }
}

// Al crear: new Gato()
// Salida:
// Animal creado
// Gato creado
```

(super-metodos)=
#### Usando `super` para Acceder a Métodos del Padre

Además de constructores, `super` permite acceder a métodos de la superclase:

```java
public class Empleado {
    protected double salarioBase;
    
    public double calcularSalario() {
        return salarioBase;
    }
}

public class Gerente extends Empleado {
    private double bonificacion;
    
    @Override
    public double calcularSalario() {
        // Reutiliza el cálculo del padre y agrega la bonificación
        return super.calcularSalario() + bonificacion;
    }
}
```

---

(sobreescritura-metodos)=
### Sobreescritura de Métodos (Override)

(que-es-override)=
#### ¿Qué es la Sobreescritura?

La **sobreescritura** (override) permite que una subclase proporcione una implementación diferente de un método heredado.

```java
public class Figura {
    public double calcularArea() {
        return 0;  // Implementación por defecto
    }
}

public class Rectangulo extends Figura {
    private double base;
    private double altura;
    
    @Override
    public double calcularArea() {
        return base * altura;  // Implementación específica
    }
}

public class Circulo extends Figura {
    private double radio;
    
    @Override
    public double calcularArea() {
        return Math.PI * radio * radio;  // Otra implementación
    }
}
```

(anotacion-override)=
#### La Anotación `@Override`

La anotación `@Override` es **opcional pero altamente recomendada**:

```java
@Override
public double calcularArea() {
    return base * altura;
}
```

**Beneficios de usar `@Override`:**

1. **Verificación en compilación**: El compilador verifica que realmente estás sobreescribiendo un método existente
2. **Documentación**: Indica claramente la intención del programador
3. **Detección de errores**: Si escribís mal el nombre del método, el compilador te avisa

```java
public class Rectangulo extends Figura {
    @Override
    public double calcularArae() {  // ❌ Error de compilación: typo detectado
        return base * altura;
    }
}
```

(reglas-override)=
#### Reglas de la Sobreescritura

Para sobreescribir un método correctamente:

| Regla | Ejemplo |
| :--- | :--- |
| **Mismo nombre** | `calcularArea()` en ambas clases |
| **Mismos parámetros** | Misma cantidad, tipos y orden |
| **Mismo tipo de retorno** (o subtipo) | `double` o un subtipo si aplica |
| **Visibilidad igual o mayor** | Si el padre es `protected`, puede ser `protected` o `public` |
| **No puede ser `final`** | Los métodos `final` no se pueden sobreescribir |

```java
public class Animal {
    protected void hacerSonido() {
        System.out.println("...");
    }
}

public class Perro extends Animal {
    @Override
    public void hacerSonido() {  // ✓ Aumentó visibilidad (protected → public)
        System.out.println("¡Guau!");
    }
}

public class Gato extends Animal {
    @Override
    private void hacerSonido() {  // ❌ Error: reduce visibilidad
        System.out.println("¡Miau!");
    }
}
```

---

(modificador-protected)=
### El Modificador `protected`

El modificador `protected` tiene un rol especial en herencia:

| Modificador | Misma clase | Mismo paquete | Subclases | Otras clases |
| :---: | :---: | :---: | :---: | :---: |
| `private` | ✓ | ✗ | ✗ | ✗ |
| (default) | ✓ | ✓ | ✗ | ✗ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `public` | ✓ | ✓ | ✓ | ✓ |

```java
public class CuentaBancaria {
    private String numeroCuenta;     // Solo esta clase
    protected double saldo;          // Esta clase + subclases
    public String titular;           // Todos
    
    protected void aplicarInteres(double tasa) {
        saldo = saldo * (1 + tasa);
    }
}

public class CuentaAhorro extends CuentaBancaria {
    public void aplicarInteresAnual() {
        // Puede acceder a saldo (protected)
        aplicarInteres(0.05);
        System.out.println("Nuevo saldo: " + saldo);
    }
}
```

:::{tip}
**Cuándo usar `protected`:**

- Para métodos que las subclases necesitan **especializar** (override)
- Para pasos intermedios en algoritmos que la superclase controla (Template Method)
- **NO** como excusa para exponer detalles internos mediante getters/setters

**⚠️ Importante:**
Las subclases interactúan con la superclase mediante **métodos de dominio**, no getters/setters.  
Ver {ref}`regla-0x200C` y {ref}`regla-0x2011` para más detalles sobre encapsulamiento en jerarquías.

**Cuándo evitarlo:**

- Si no esperás que la clase sea extendida
- Si querés mantener control total sobre el acceso (usá `private` + métodos)
- Atributos `protected` que las subclases "usan directamente" (rompe encapsulamiento)
:::

---

(modificador-final)=
### El Modificador `final`

La palabra clave `final` previene la herencia o sobreescritura:

(final-clase)=
#### Clases `final`

Una clase `final` no puede ser extendida:

```java
public final class String {  // La clase String de Java es final
    // ...
}

// ❌ Error de compilación
public class MiString extends String { }
```

**Razones para usar `final` en clases:**

1. **Seguridad**: Prevenir que alguien modifique el comportamiento
2. **Diseño**: La clase no fue diseñada para ser extendida
3. **Optimización**: El compilador puede hacer ciertas optimizaciones

(final-metodo)=
#### Métodos `final`

Un método `final` no puede ser sobreescrito:

```java
public class CuentaBancaria {
    private double saldo;
    
    // Este método no puede ser modificado por subclases
    public final double getSaldo() {
        return saldo;
    }
    
    // Este sí puede ser sobreescrito
    public double calcularInteres() {
        return saldo * 0.01;
    }
}

public class CuentaFraudulenta extends CuentaBancaria {
    @Override
    public double getSaldo() {  // ❌ Error: no se puede sobreescribir
        return 1000000;  // Intento de fraude
    }
    
    @Override
    public double calcularInteres() {  // ✓ Permitido
        return super.getSaldo() * 0.05;
    }
}
```

---

(clases-abstractas)=
## Clases Abstractas

(que-es-clase-abstracta)=
### ¿Qué es una Clase Abstracta?

Una **clase abstracta** es una clase que no puede ser instanciada directamente. Sirve como plantilla para otras clases.

```java
public abstract class Figura {
    protected String color;
    
    public Figura(String color) {
        this.color = color;
    }
    
    // Método abstracto: sin implementación
    public abstract double calcularArea();
    
    // Método concreto: con implementación
    public String getColor() {
        return color;
    }
}

// ❌ Error: no se puede instanciar
Figura f = new Figura("rojo");

// ✓ Se instancian las subclases concretas
Figura r = new Rectangulo("azul", 10, 5);
```

(metodos-abstractos)=
### Métodos Abstractos

Un **método abstracto** es un método sin implementación que debe ser implementado por las subclases:

```java
public abstract class Figura {
    // Método abstracto: solo firma, sin cuerpo
    public abstract double calcularArea();
    
    public abstract double calcularPerimetro();
}

public class Rectangulo extends Figura {
    private double base;
    private double altura;
    
    public Rectangulo(String color, double base, double altura) {
        super(color);
        this.base = base;
        this.altura = altura;
    }
    
    @Override
    public double calcularArea() {
        return base * altura;
    }
    
    @Override
    public double calcularPerimetro() {
        return 2 * (base + altura);
    }
}

public class Circulo extends Figura {
    private double radio;
    
    public Circulo(String color, double radio) {
        super(color);
        this.radio = radio;
    }
    
    @Override
    public double calcularArea() {
        return Math.PI * radio * radio;
    }
    
    @Override
    public double calcularPerimetro() {
        return 2 * Math.PI * radio;
    }
}
```

:::{important}
**Reglas de clases abstractas:**

1. Una clase con al menos un método abstracto **debe** ser declarada `abstract`
2. Una clase abstracta **puede** tener métodos concretos (con implementación)
3. Una clase abstracta **puede** tener constructores (para inicializar estado)
4. Una subclase de una clase abstracta **debe** implementar todos los métodos abstractos, o ser declarada abstracta también
:::

(cuando-usar-abstracta)=
### ¿Cuándo Usar Clases Abstractas?

**Usá clases abstractas cuando:**

1. Querés compartir código entre clases relacionadas
2. Esperás que las subclases tengan métodos comunes con implementación
3. Querés declarar campos no públicos (protected o private)
4. El concepto que modelás es "incompleto" por sí solo

**Ejemplo: Sistema de Notificaciones**

```java
public abstract class Notificacion {
    protected String destinatario;
    protected String mensaje;
    protected LocalDateTime fechaCreacion;
    
    public Notificacion(String destinatario, String mensaje) {
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    // Cada tipo de notificación se envía de forma diferente
    public abstract void enviar();
    
    // Comportamiento común
    public void registrarEnvio() {
        System.out.println("Notificación enviada a " + destinatario + 
                           " el " + fechaCreacion);
    }
    
    public boolean esReciente() {
        return fechaCreacion.isAfter(LocalDateTime.now().minusHours(24));
    }
}

public class NotificacionEmail extends Notificacion {
    private String asunto;
    
    public NotificacionEmail(String destinatario, String mensaje, String asunto) {
        super(destinatario, mensaje);
        this.asunto = asunto;
    }
    
    @Override
    public void enviar() {
        System.out.println("Enviando email a " + destinatario);
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + mensaje);
        // Lógica de envío de email...
        registrarEnvio();
    }
}

public class NotificacionSMS extends Notificacion {
    private String codigoPais;
    
    public NotificacionSMS(String destinatario, String mensaje, String codigoPais) {
        super(destinatario, mensaje);
        this.codigoPais = codigoPais;
    }
    
    @Override
    public void enviar() {
        String numeroCompleto = codigoPais + destinatario;
        System.out.println("Enviando SMS a " + numeroCompleto);
        System.out.println("Texto: " + mensaje.substring(0, Math.min(160, mensaje.length())));
        // Lógica de envío de SMS...
        registrarEnvio();
    }
}
```

---

(interfaces-java)=
## Interfaces

(que-es-interface)=
### ¿Qué es una Interface?

Una **interface** define un **contrato**: un conjunto de métodos que una clase debe implementar, sin especificar cómo.

```java
public interface Volador {
    void despegar();
    void volar();
    void aterrizar();
}
```

:::{note} Definición Formal

Una **interface** es un tipo de referencia que define un conjunto de métodos abstractos (y posiblemente constantes y métodos por defecto) que las clases implementadoras deben proporcionar.
:::

(implementar-interface)=
### Implementando Interfaces

Una clase implementa una interface usando `implements`:

```java
public class Avion implements Volador {
    private boolean enVuelo;
    
    @Override
    public void despegar() {
        System.out.println("Avión despegando...");
        enVuelo = true;
    }
    
    @Override
    public void volar() {
        if (enVuelo) {
            System.out.println("Avión volando a 10.000 metros");
        }
    }
    
    @Override
    public void aterrizar() {
        System.out.println("Avión aterrizando...");
        enVuelo = false;
    }
}

public class Pajaro implements Volador {
    @Override
    public void despegar() {
        System.out.println("Pájaro aleteando para despegar");
    }
    
    @Override
    public void volar() {
        System.out.println("Pájaro planeando en el aire");
    }
    
    @Override
    public void aterrizar() {
        System.out.println("Pájaro aterrizando en una rama");
    }
}
```

(multiples-interfaces)=
### Implementando Múltiples Interfaces

A diferencia de la herencia, una clase puede implementar **múltiples interfaces**:

```java
public interface Volador {
    void volar();
}

public interface Nadador {
    void nadar();
}

public interface Caminante {
    void caminar();
}

// Un pato puede hacer todo
public class Pato implements Volador, Nadador, Caminante {
    @Override
    public void volar() {
        System.out.println("Pato volando");
    }
    
    @Override
    public void nadar() {
        System.out.println("Pato nadando");
    }
    
    @Override
    public void caminar() {
        System.out.println("Pato caminando");
    }
}

// Un pingüino solo puede nadar y caminar
public class Pinguino implements Nadador, Caminante {
    @Override
    public void nadar() {
        System.out.println("Pingüino nadando velozmente");
    }
    
    @Override
    public void caminar() {
        System.out.println("Pingüino caminando torpemente");
    }
}
```

(metodos-default)=
### Métodos Default (Java 8+)

Desde Java 8, las interfaces pueden tener métodos con implementación por defecto:

```java
public interface Comparable<T> {
    int compareTo(T otro);
    
    // Método default: tiene implementación
    default boolean esMayorQue(T otro) {
        return compareTo(otro) > 0;
    }
    
    default boolean esMenorQue(T otro) {
        return compareTo(otro) < 0;
    }
    
    default boolean esIgualA(T otro) {
        return compareTo(otro) == 0;
    }
}

public class Persona implements Comparable<Persona> {
    private String nombre;
    private int edad;
    
    @Override
    public int compareTo(Persona otra) {
        return this.edad - otra.edad;
    }
    
    // esMayorQue(), esMenorQue() y esIgualA() ya están disponibles
}
```

**Uso de métodos default:**

```java
Persona juan = new Persona("Juan", 30);
Persona maria = new Persona("María", 25);

juan.esMayorQue(maria);  // true (usa el método default)
```

---

(abstracta-vs-interface)=
## Clases Abstractas vs Interfaces

### Comparación Detallada

| Característica | Clase Abstracta | Interface |
| :--- | :--- | :--- |
| **Herencia** | Solo una (extends) | Múltiples (implements) |
| **Atributos** | Cualquier tipo | Solo constantes (public static final) |
| **Métodos** | Abstractos y concretos | Abstractos, default, static |
| **Constructores** | Sí | No |
| **Modificadores** | Cualquiera | Métodos: public por defecto |
| **Propósito** | "Es un tipo de" | "Puede hacer" |

### ¿Cuándo Usar Cada Una?

**Usá Clase Abstracta cuando:**

```java
// Las subclases comparten código y estado
public abstract class Animal {
    protected String nombre;
    protected int edad;
    
    public Animal(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }
    
    public void envejecer() {
        edad++;
    }
    
    public abstract void hacerSonido();
}
```

- Las clases relacionadas comparten **estado** (atributos)
- Querés proporcionar **implementación parcial**
- Existe una relación jerárquica clara ("es un tipo de")

**Usá Interface cuando:**

```java
// Diferentes clases no relacionadas pueden "hacer algo"
public interface Serializable {
    byte[] serializar();
    void deserializar(byte[] datos);
}

// Clases completamente diferentes pueden ser serializables
public class Usuario implements Serializable { ... }
public class Configuracion implements Serializable { ... }
public class Pedido implements Serializable { ... }
```

- Clases **no relacionadas** comparten comportamiento
- Querés definir un **contrato** sin imponer jerarquía (ver {ref}`oop-contratos`)
- Necesitás **herencia múltiple** de tipos

### Ejemplo Combinado

Es común combinar ambos mecanismos:

```java
// Interface: define el contrato
public interface Dibujable {
    void dibujar(Graphics g);
}

// Clase abstracta: implementación parcial para figuras
public abstract class Figura implements Dibujable {
    protected String color;
    protected int x, y;
    
    public Figura(String color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }
    
    public abstract double calcularArea();
    
    // Implementación parcial de dibujar
    protected void configurarColor(Graphics g) {
        g.setColor(Color.decode(color));
    }
}

// Clases concretas
public class Rectangulo extends Figura {
    private int ancho, alto;
    
    @Override
    public double calcularArea() {
        return ancho * alto;
    }
    
    @Override
    public void dibujar(Graphics g) {
        configurarColor(g);
        g.fillRect(x, y, ancho, alto);
    }
}
```

---

(polimorfismo)=
## Polimorfismo

(que-es-polimorfismo)=
### ¿Qué es el Polimorfismo?

**Polimorfismo** significa "muchas formas". En POO, permite tratar objetos de diferentes clases de manera uniforme a través de una interfaz común.

```java
// Una variable de tipo Figura puede referenciar cualquier subtipo
Figura figura1 = new Rectangulo("rojo", 10, 5);
Figura figura2 = new Circulo("azul", 7);
Figura figura3 = new Triangulo("verde", 6, 8);

// El mismo código funciona para cualquier figura
for (Figura f : Arrays.asList(figura1, figura2, figura3)) {
    System.out.println("Área: " + f.calcularArea());  // Cada una calcula diferente
}
```

(polimorfismo-tipos-avanzados)=
### Tipos de Polimorfismo

#### 1. Polimorfismo de Subtipo (Runtime)

El tipo más común: una variable del tipo padre puede referenciar objetos de cualquier subtipo.

```java
Animal animal;

animal = new Perro("Firulais");
animal.hacerSonido();  // "¡Guau!"

animal = new Gato("Michi");
animal.hacerSonido();  // "¡Miau!"

animal = new Vaca("Clarabella");
animal.hacerSonido();  // "¡Muuu!"
```

#### 2. Polimorfismo de Interface

Similar, pero usando interfaces:

```java
List<Volador> voladores = new ArrayList<>();
voladores.add(new Avion());
voladores.add(new Pajaro());
voladores.add(new Superman());

for (Volador v : voladores) {
    v.despegar();
    v.volar();
    v.aterrizar();
}
```

(binding-dinamico)=
### Binding Dinámico

El **binding dinámico** (o late binding) es el mecanismo por el cual Java determina **en tiempo de ejecución** qué método llamar.

```java
public class Demo {
    public static void main(String[] args) {
        Animal a = new Perro("Rex");
        
        // El compilador ve: tipo Animal
        // En ejecución: el objeto real es Perro
        
        a.hacerSonido();  // ¿Qué se ejecuta?
    }
}
```

**Proceso:**

1. **Compilación**: El compilador verifica que `Animal` tiene un método `hacerSonido()`
2. **Ejecución**: La JVM determina que el objeto real es `Perro` y llama al método de `Perro`

Este comportamiento se llama **dispatch dinámico** y es lo que hace posible el polimorfismo.

(principio-sustitucion)=
### Principio de Sustitución de Liskov

El **Principio de Sustitución de Liskov** (LSP) establece:

> "Si S es un subtipo de T, entonces los objetos de tipo T pueden ser reemplazados por objetos de tipo S sin alterar las propiedades deseables del programa."

Este principio es fundamental para el diseño de jerarquías correctas. Para un análisis detallado, consultá {ref}`l-principio-de-sustitucion-de-liskov`.

**Ejemplo que viola LSP:**

```java
public class Rectangulo {
    protected int ancho;
    protected int alto;
    
    public void setAncho(int ancho) {
        this.ancho = ancho;
    }
    
    public void setAlto(int alto) {
        this.alto = alto;
    }
    
    public int calcularArea() {
        return ancho * alto;
    }
}

public class Cuadrado extends Rectangulo {
    @Override
    public void setAncho(int ancho) {
        this.ancho = ancho;
        this.alto = ancho;  // Mantiene el invariante del cuadrado
    }
    
    @Override
    public void setAlto(int alto) {
        this.alto = alto;
        this.ancho = alto;  // Mantiene el invariante del cuadrado
    }
}

// Código que asume comportamiento de Rectangulo
public void test(Rectangulo r) {
    r.setAncho(5);
    r.setAlto(4);
    assert r.calcularArea() == 20;  // ¡Falla si r es Cuadrado!
}
```

**El problema**: `Cuadrado` no puede sustituir completamente a `Rectangulo` porque tiene restricciones adicionales (ancho == alto).

**Solución**: No modelar `Cuadrado` como subclase de `Rectangulo`. Ambos podrían implementar una interface `Figura` sin herencia directa.

---

(ejemplo-completo-herencia)=
## Ejemplo Completo: Sistema de Empleados

Integremos todos los conceptos en un ejemplo realista:

```java
// Interface para comportamientos opcionales
public interface Bonificable {
    double calcularBonificacion();
}

public interface Reportable {
    String generarReporte();
}

// Clase abstracta base
public abstract class Empleado implements Reportable {
    private static int contadorId = 0;
    
    private final int id;
    private String nombre;
    private String dni;
    protected double salarioBase;
    private LocalDate fechaIngreso;
    
    public Empleado(String nombre, String dni, double salarioBase) {
        this.id = ++contadorId;
        this.nombre = nombre;
        this.dni = dni;
        this.salarioBase = salarioBase;
        this.fechaIngreso = LocalDate.now();
    }
    
    // Método abstracto: cada tipo calcula diferente
    public abstract double calcularSalario();
    
    // Método concreto: comportamiento común
    public int calcularAntiguedad() {
        return Period.between(fechaIngreso, LocalDate.now()).getYears();
    }
    
    // Implementación de Reportable
    @Override
    public String generarReporte() {
        return String.format("Empleado #%d: %s (DNI: %s) - Salario: $%.2f",
                            id, nombre, dni, calcularSalario());
    }
    
    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getSalarioBase() { return salarioBase; }
}

// Subclase concreta simple
public class EmpleadoTiempoCompleto extends Empleado {
    
    public EmpleadoTiempoCompleto(String nombre, String dni, double salarioBase) {
        super(nombre, dni, salarioBase);
    }
    
    @Override
    public double calcularSalario() {
        double antiguedad = calcularAntiguedad() * 0.02 * salarioBase;
        return salarioBase + antiguedad;
    }
}

// Subclase con bonificación
public class Gerente extends Empleado implements Bonificable {
    private String departamento;
    private double bonoPorResultados;
    
    public Gerente(String nombre, String dni, double salarioBase, 
                   String departamento) {
        super(nombre, dni, salarioBase);
        this.departamento = departamento;
        this.bonoPorResultados = 0;
    }
    
    public void establecerBono(double bono) {
        this.bonoPorResultados = bono;
    }
    
    @Override
    public double calcularSalario() {
        return salarioBase + calcularBonificacion();
    }
    
    @Override
    public double calcularBonificacion() {
        double bonoBase = salarioBase * 0.2;  // 20% fijo
        return bonoBase + bonoPorResultados;
    }
    
    @Override
    public String generarReporte() {
        return super.generarReporte() + 
               String.format(" - Dept: %s - Bono: $%.2f", 
                            departamento, calcularBonificacion());
    }
}

// Subclase: empleado por hora
public class EmpleadoPorHora extends Empleado {
    private int horasTrabajadas;
    private double valorHora;
    
    public EmpleadoPorHora(String nombre, String dni, double valorHora) {
        super(nombre, dni, 0);  // No tiene salario base fijo
        this.valorHora = valorHora;
        this.horasTrabajadas = 0;
    }
    
    public void registrarHoras(int horas) {
        this.horasTrabajadas += horas;
    }
    
    public void reiniciarHoras() {
        this.horasTrabajadas = 0;
    }
    
    @Override
    public double calcularSalario() {
        double salarioNormal = Math.min(horasTrabajadas, 160) * valorHora;
        double horasExtra = Math.max(0, horasTrabajadas - 160);
        double salarioExtra = horasExtra * valorHora * 1.5;
        return salarioNormal + salarioExtra;
    }
    
    @Override
    public String generarReporte() {
        return super.generarReporte() + 
               String.format(" - Horas: %d - $/hora: %.2f", 
                            horasTrabajadas, valorHora);
    }
}
```

**Uso del sistema:**

```java
public class SistemaRRHH {
    private List<Empleado> empleados = new ArrayList<>();
    
    public void agregarEmpleado(Empleado e) {
        empleados.add(e);
    }
    
    public double calcularNominaTotal() {
        double total = 0;
        for (Empleado e : empleados) {
            total += e.calcularSalario();  // Polimorfismo
        }
        return total;
    }
    
    public void imprimirReportes() {
        for (Empleado e : empleados) {
            System.out.println(e.generarReporte());  // Polimorfismo
        }
    }
    
    public double calcularBonificacionesTotal() {
        double total = 0;
        for (Empleado e : empleados) {
            if (e instanceof Bonificable) {
                total += ((Bonificable) e).calcularBonificacion();
            }
        }
        return total;
    }
    
    public static void main(String[] args) {
        SistemaRRHH sistema = new SistemaRRHH();
        
        sistema.agregarEmpleado(new EmpleadoTiempoCompleto("Ana García", "12345678", 50000));
        
        Gerente gerente = new Gerente("Carlos López", "87654321", 80000, "Ventas");
        gerente.establecerBono(5000);
        sistema.agregarEmpleado(gerente);
        
        EmpleadoPorHora contractor = new EmpleadoPorHora("María Ruiz", "11223344", 500);
        contractor.registrarHoras(180);  // 20 horas extra
        sistema.agregarEmpleado(contractor);
        
        System.out.println("=== REPORTES DE EMPLEADOS ===");
        sistema.imprimirReportes();
        
        System.out.println("\n=== TOTALES ===");
        System.out.printf("Nómina total: $%.2f%n", sistema.calcularNominaTotal());
        System.out.printf("Bonificaciones total: $%.2f%n", sistema.calcularBonificacionesTotal());
    }
}
```

---

(testing-herencia-sin-getters)=
## Testing de Herencia sin Getters

Un desafío común es: **¿Cómo testeau código con jerarquías si no puedo usar getters?**

La respuesta es: **Usa métodos de dominio públicos** para verificar comportamiento.

(testing-comportamiento-no-estado)=
### Prueba Comportamiento, No Estado

En lugar de verificar el estado interno (que requeriría getters), verifica el comportamiento resultante:

**Incorrecto ❌:**
```java
@Test
void testGerenciaSalario() {
    Gerente g = new Gerente("Carlos", 5000.0, 1000.0);
    
    // ❌ Necesita getters para "verficar" estado
    assertEquals(5000.0, g.getSalario());
    assertEquals(1000.0, g.getBonificacion());
}
```

**Correcto ✅:**
```java
@Test
void testGerenciaSueldoFinal() {
    Gerente g = new Gerente("Carlos", 5000.0, 1000.0);
    
    // ✅ Verifica el comportamiento mediante método de dominio
    assertEquals(6000.0, g.calcularSueldoFinal(), 0.01);
}
```

(ejemplo-testing-gerente-empleado)=
### Ejemplo Completo: Gerente extends Empleado

**Clase base (Empleado):**
```java
public class Empleado {
    private String nombre;
    private double salarioBase;
    
    public Empleado(String nombre, double salarioBase) {
        this.nombre = nombre;
        this.salarioBase = salarioBase;
    }
    
    // Métodos de dominio (sin getters)
    public double calcularSueldoFinal() {
        return salarioBase;
    }
    
    public boolean tieneNombre(String nombre) {
        return this.nombre.equals(nombre);
    }
    
    public void cambiarNombre(String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.isEmpty()) {
            throw new IllegalArgumentException("Nombre inválido");
        }
        this.nombre = nuevoNombre;
    }
}
```

**Subclase (Gerente):**
```java
public class Gerente extends Empleado {
    private double bonificacion;
    
    public Gerente(String nombre, double salarioBase, double bonificacion) {
        super(nombre, salarioBase);
        this.bonificacion = bonificacion;
    }
    
    // Especialización: calcula sueldo con bonificación
    @Override
    public double calcularSueldoFinal() {
        return super.calcularSueldoFinal() + bonificacion;
    }
    
    // Método específico de gerente
    public void incrementarBonificacion(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("Bonificación no puede ser negativa");
        }
        this.bonificacion += monto;
    }
}
```

**Tests (Sin getters):**
```java
class TestEmpleado {
    
    @Test
    void testEmpleadoCalculaSueldoBase() {
        Empleado e = new Empleado("Ana", 3000.0);
        assertEquals(3000.0, e.calcularSueldoFinal(), 0.01);
    }
    
    @Test
    void testEmpleadoTieneNombre() {
        Empleado e = new Empleado("Ana", 3000.0);
        assertTrue(e.tieneNombre("Ana"));
        assertFalse(e.tieneNombre("Carlos"));
    }
    
    @Test
    void testEmpleadoCambiarNombre() {
        Empleado e = new Empleado("Ana", 3000.0);
        e.cambiarNombre("Analía");
        assertTrue(e.tieneNombre("Analía"));
    }
    
    @Test
    void testEmpleadoCambiarNombreInvalido() {
        Empleado e = new Empleado("Ana", 3000.0);
        assertThrows(
            IllegalArgumentException.class,
            () -> e.cambiarNombre("")
        );
    }
}

class TestGerente {
    
    @Test
    void testGerenteSueldoConBonificacion() {
        Gerente g = new Gerente("Carlos", 5000.0, 1000.0);
        
        // Verifica comportamiento polimórfico
        assertEquals(6000.0, g.calcularSueldoFinal(), 0.01);
    }
    
    @Test
    void testGerenteHeredaNombre() {
        Gerente g = new Gerente("Carlos", 5000.0, 1000.0);
        
        // Usa método heredado
        assertTrue(g.tieneNombre("Carlos"));
    }
    
    @Test
    void testGerenteIncrementoBonificacion() {
        Gerente g = new Gerente("Carlos", 5000.0, 1000.0);
        
        g.incrementarBonificacion(500.0);
        
        // Verifica el cambio mediante comportamiento
        assertEquals(6500.0, g.calcularSueldoFinal(), 0.01);
    }
    
    @Test
    void testGerenteIncrementoBonificacionNegativa() {
        Gerente g = new Gerente("Carlos", 5000.0, 1000.0);
        
        assertThrows(
            IllegalArgumentException.class,
            () -> g.incrementarBonificacion(-100.0)
        );
    }
}
```

(testing-principios-jerarquias)=
### Principios para Testing de Jerarquías

1. **Cada clase tesatea su comportamiento específico**
   - `TestEmpleado` verifica métodos de `Empleado`
   - `TestGerente` verifica especialización y métodos nuevos

2. **Prueba polimorfismo**
   - Asegúrate que `Gerente.calcularSueldoFinal()` se comporta diferente a `Empleado`

3. **Prueba invariantes**
   - Si una clase tiene reglas (ej: "bonificación no negativa"), tesatea que se cumplen

4. **No uses getters**
   - Usa métodos públicos de dominio para verificar estado indirectamente
   - Los invariantes se verifican a través del comportamiento

5. **Usa métodos de dominio bien diseñados**
   - `tieneNombre()`, `cambiarNombre()` son más expresivos que getters
   - `incrementarBonificacion()` encapsula la lógica de validación

---

### Herencia

- Usa `extends` para heredar de una clase
- Solo herencia simple de clases en Java
- `super()` llama al constructor padre
- `super.metodo()` llama al método del padre

### Sobreescritura

- `@Override` marca un método sobreescrito
- Mismo nombre, parámetros y tipo de retorno (o subtipo)
- Visibilidad igual o mayor que el padre

### Clases Abstractas

- `abstract class` no puede ser instanciada
- Puede tener métodos abstractos y concretos
- Las subclases deben implementar los métodos abstractos

### Interfaces

- `interface` define un contrato
- `implements` para implementar (puede ser múltiple)
- Métodos `default` permiten implementación en la interface

### Polimorfismo

- Una variable del tipo padre puede referenciar subtipos
- El método ejecutado se determina en runtime (binding dinámico)
- Respetar el Principio de Sustitución de Liskov

---

(ejercicios-java2)=
## Ejercicios

```{exercise}
:label: ej-jerarquia-vehiculos
Diseñá e implementá una jerarquía de clases para vehículos:

- Clase abstracta `Vehiculo` con: marca, modelo, año, método abstracto `calcularAutonomiakm()`
- `Auto`: tiene tanque de combustible (litros) y consumo (km/litro)
- `Moto`: tiene tanque y consumo
- `Bicicleta`: no tiene motor, autonomía basada en resistencia del ciclista (km)
- `AutoElectrico`: tiene capacidad de batería (kWh) y consumo (km/kWh)

Implementá el método `calcularAutonomia()` en cada subclase.
```

```{exercise}
:label: ej-interfaces-dispositivos
Creá las siguientes interfaces y clases:

- Interface `Encendible` con métodos `encender()`, `apagar()`, `estaEncendido()`
- Interface `Recargable` con métodos `recargar()`, `nivelBateria()`
- Clase `Telefono` que implemente ambas interfaces
- Clase `Lampara` que implemente solo `Encendible`
- Clase `PowerBank` que implemente solo `Recargable`

Demostrá el uso de polimorfismo con estas clases.
```

```{exercise}
:label: ej-figuras-geometricas
Implementá un sistema de figuras geométricas:

- Interface `Dibujable` con método `dibujar()`
- Clase abstracta `Figura` con color, posición (x, y), y método abstracto `calcularArea()`
- Clases concretas: `Rectangulo`, `Circulo`, `Triangulo`
- Todas deben implementar `Dibujable`

Creá un método que reciba una lista de `Figura` y calcule el área total.
```

```{exercise}
:label: ej-lsp-violacion
Analizá el siguiente código e identificá la violación del Principio de Sustitución de Liskov:

```java
public class Ave {
    public void volar() {
        System.out.println("Volando...");
    }
}

public class Pinguino extends Ave {
    @Override
    public void volar() {
        throw new UnsupportedOperationException("Los pingüinos no vuelan");
    }
}
```

¿Cómo rediseñarías esta jerarquía para cumplir con LSP?
```

```{exercise}
:label: ej-sistema-pagos
Diseñá un sistema de procesamiento de pagos:

- Interface `MetodoPago` con `procesarPago(double monto)` y `verificarFondos(double monto)`
- Clases: `TarjetaCredito`, `TarjetaDebito`, `PayPal`, `Efectivo`
- Cada método de pago tiene sus propias reglas (límites, comisiones, etc.)
- Creá una clase `Caja` que procese pagos usando polimorfismo

Implementá validaciones apropiadas en cada método de pago.
```
