---
title: "Sintaxis de Clases y Objetos"
subtitle: "De la Burbuja al Código"
---

(java-sintaxis-clases)=
# Sintaxis de Clases y Objetos

Este capítulo aborda la **sintaxis concreta de Java** para implementar los conceptos de Programación Orientada a Objetos que viste en los capítulos anteriores:

- {ref}`fundamentos-de-la-programacion-orientada-a-objetos`: Conceptos de clase, objeto, atributo, método
- {ref}`oop2-encapsulamiento-relaciones`: Encapsulamiento y relaciones entre objetos

Aquí aprenderás cómo **materializar** esos conceptos en código Java funcional.

:::{important}
**Requisitos previos**:
- Comprender los conceptos de {ref}`concepto-clase`, {ref}`concepto-objeto`, {ref}`concepto-atributo` y {ref}`concepto-metodo`
- Conocer los principios de {ref}`encapsulamiento-concepto` y {ref}`tipos-de-relaciones`
:::

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, serás capaz de:

1. Escribir la sintaxis completa de una clase en Java
2. Implementar constructores para inicializar objetos correctamente
3. Aplicar modificadores de acceso para controlar el encapsulamiento
4. Crear y utilizar métodos de instancia y estáticos
5. Implementar relaciones de asociación, composición y agregación en código
6. Usar la palabra clave `this` apropiadamente
:::

---

## Bloque 1: De la "Burbuja" al Código

(estructura-clase-java)=
### La Estructura de una Clase en Java

En el capítulo anterior definimos que una {ref}`concepto-clase` es una **descripción abstracta** que funciona como molde o plantilla. En Java, esa descripción se materializa mediante la palabra clave `class` seguida del nombre de la clase. La estructura general es:

```java
public class NombreClase {
    // Zona de declaración de atributos (estado)
    
    // Zona de constructor(es)
    
    // Zona de métodos (comportamiento)
}
```

Esta estructura tripartita no es arbitraria: refleja directamente los tres componentes fundamentales de un objeto según el paradigma ({ref}`concepto-objeto`):

1. **Atributos** → definen el **estado** que cada instancia almacenará
2. **Constructor** → establece el proceso de **construcción** ({ref}`construccion-de-objetos`)
3. **Métodos** → implementan el **comportamiento** del objeto

(anatomia-clase-java)=
#### Anatomía Detallada de una Clase

Consideremos el siguiente ejemplo completo:

```java
public class Persona {
    // ══════════════════════════════════════════════════════════
    // ATRIBUTOS: Variables de instancia que almacenan el estado
    // ══════════════════════════════════════════════════════════
    private String nombre;
    private int edad;
    
    // ══════════════════════════════════════════════════════════
    // CONSTRUCTOR: Método especial de inicialización
    // ══════════════════════════════════════════════════════════
    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }
    
    // ══════════════════════════════════════════════════════════
    // MÉTODOS: Operaciones que definen el comportamiento
    // ══════════════════════════════════════════════════════════
    public void saludar() {
        System.out.println("Hola, soy " + nombre);
    }
    
    public void cumplirAnios() {
        edad = edad + 1;
    }
}
```

Cada zona tiene un propósito específico y reglas sintácticas propias que se detallan a continuación.

```{mermaid}
classDiagram
    class Persona {
        -String nombre
        -int edad
        +Persona(String, int)
        +saludar()
        +cumplirAnios()
    }
    
    note for Persona "Estructura tripartita:\n1. Atributos (estado)\n2. Constructor (inicialización)\n3. Métodos (comportamiento)"
```

:::{note}
La palabra clave `public` antes de `class` indica que la clase es accesible desde cualquier otra clase del programa. Este es un **modificador de acceso** que se explicará en profundidad en la sección {ref}`modificadores-acceso-java`.
:::

(componentes-clase-java)=
### Componentes de una Clase

(atributos-java)=
#### Atributos (Variables de Instancia)

Los atributos, también llamados **variables de instancia** o **campos**, son variables declaradas dentro de la clase pero fuera de cualquier método. Estos atributos almacenan el {ref}`concepto-estado` del objeto.

**Sintaxis de declaración:**

```java
[modificadorDeAcceso] tipo nombreAtributo;
```

**Ejemplos:**

```java
private String nombre;      // Atributo de tipo String (texto)
private int edad;           // Atributo de tipo int (entero)
private double salario;     // Atributo de tipo double (decimal)
private boolean activo;     // Atributo de tipo boolean (verdadero/falso)
private Fecha nacimiento;   // Atributo de tipo Fecha (otra clase)
```

**Características fundamentales de los atributos:**

1. **Pertenencia a la instancia:** Cada objeto creado a partir de la clase tiene su **propia copia** de los atributos. Modificar el atributo `nombre` de un objeto `juan` no afecta al atributo `nombre` de otro objeto `maria`. Esto es exactamente lo que describimos en {ref}`concepto-atributo`.

2. **Persistencia durante la vida del objeto:** Los valores de los atributos se mantienen mientras el objeto exista. A diferencia de las variables locales (dentro de métodos), que desaparecen cuando el método termina, los atributos persisten entre llamadas a métodos.

3. **Valores por defecto:** Si no se inicializa explícitamente, Java asigna valores por defecto según el tipo:

   | Tipo | Valor por defecto |
   |:-----|:------------------|
   | `int`, `long`, `short`, `byte` | `0` |
   | `float`, `double` | `0.0` |
   | `boolean` | `false` |
   | Referencias (objetos) | `null` |

:::{warning}
Aunque Java asigna valores por defecto, **confiar en ellos es una mala práctica**. Siempre se deben inicializar los atributos explícitamente en el constructor para garantizar un estado inicial válido y documentar la intención del programador.
:::

(constructor-java)=
#### El Constructor

El constructor es un **método especial** que se ejecuta automáticamente cuando se crea una nueva instancia de la clase mediante el operador `new`. Su propósito fundamental es garantizar que el objeto nazca en un **estado válido y consistente**, tal como se describió en {ref}`construccion-de-objetos`.

**Sintaxis del constructor:**

```java
[modificadorDeAcceso] NombreClase([parámetros]) {
    // Código de inicialización
}
```

**Características distintivas del constructor:**

1. **Nombre idéntico a la clase:** El constructor debe llamarse exactamente igual que la clase (respetando mayúsculas y minúsculas).

2. **Sin tipo de retorno:** A diferencia de los métodos regulares, el constructor **no declara tipo de retorno**, ni siquiera `void`. Esto no es un olvido; el constructor implícitamente "retorna" la referencia al objeto recién creado.

3. **Ejecución automática:** El constructor se invoca automáticamente cuando se usa `new`. No se puede llamar directamente como un método ordinario (excepto usando `this()` desde otro constructor de la misma clase).

4. **Responsabilidad de inicialización:** El constructor debe establecer todos los atributos en valores válidos. Un objeto nunca debería existir en un estado inconsistente.

**Ejemplo detallado:**

```java
public Persona(String nombre, int edad) {
    // Validación previa (opcional pero recomendada)
    if (nombre == null || nombre.isEmpty()) {
        throw new IllegalArgumentException("El nombre no puede estar vacío");
    }
    if (edad < 0) {
        throw new IllegalArgumentException("La edad no puede ser negativa");
    }
    
    // Inicialización de atributos
    this.nombre = nombre;
    this.edad = edad;
}
```

:::{important}
Un buen constructor actúa como **guardián del estado**: rechaza la creación de objetos inválidos lanzando excepciones cuando los parámetros son inaceptables. Esto garantiza que si un objeto existe, está en un estado coherente.
:::

(constructor-por-defecto)=
##### El Constructor por Defecto

Si no se define ningún constructor explícitamente, Java proporciona automáticamente un **constructor por defecto** (sin parámetros) que:
- No recibe argumentos
- Inicializa los atributos con sus valores por defecto
- Tiene el mismo modificador de acceso que la clase

```java
public class Punto {
    private int x;
    private int y;
    // Java genera implícitamente:
    // public Punto() { }
}

// Se puede usar así:
Punto origen = new Punto();  // x=0, y=0 (valores por defecto)
```

:::{warning}
**En cuanto se define cualquier constructor explícito, Java deja de proporcionar el constructor por defecto.** Si se necesita un constructor sin parámetros además de otros constructores, debe definirse explícitamente.

```java
public class Persona {
    private String nombre;
    
    public Persona(String nombre) {  // Constructor explícito
        this.nombre = nombre;
    }
    
    // Ahora esto NO compila:
    // Persona p = new Persona();  // Error: no existe constructor sin parámetros
}
```
:::

(metodos-java)=
#### Métodos

Los métodos implementan el **comportamiento** de los objetos, es decir, las operaciones que pueden realizar. Esto corresponde directamente al {ref}`concepto-metodo` del capítulo anterior.

**Sintaxis general de un método:**

```java
[modificadorDeAcceso] tipoRetorno nombreMetodo([parámetros]) {
    // Cuerpo del método
    [return valor;]  // Si tipoRetorno no es void
}
```

**Componentes de la firma de un método:**

| Componente | Descripción | Ejemplo |
|:-----------|:------------|:--------|
| Modificador de acceso | Controla quién puede invocar el método | `public`, `private` |
| Tipo de retorno | Tipo del valor que devuelve el método | `int`, `String`, `void` |
| Nombre del método | Identificador que describe la acción | `calcularEdad`, `saludar` |
| Parámetros | Datos que recibe el método para trabajar | `(int cantidad, String texto)` |

**Ejemplo con diferentes tipos de métodos:**

```java
public class CuentaBancaria {
    private double saldo;
    private String titular;
    
    // Constructor
    public CuentaBancaria(String titular, double saldoInicial) {
        this.titular = titular;
        this.saldo = saldoInicial;
    }
    
    // Método de consulta (getter): retorna información, no modifica estado
    public double getSaldo() {
        return saldo;
    }
    
    // Método de modificación: cambia el estado del objeto
    public void depositar(double monto) {
        if (monto > 0) {
            saldo = saldo + monto;
        }
    }
    
    // Método que retorna un resultado calculado
    public boolean puedeExtraer(double monto) {
        return monto > 0 && monto <= saldo;
    }
    
    // Método que modifica estado y retorna resultado
    public boolean extraer(double monto) {
        if (puedeExtraer(monto)) {
            saldo = saldo - monto;
            return true;
        }
        return false;
    }
}
```

**Clasificación de métodos según su propósito:**

Como se mencionó en {ref}`concepto-metodo`, los métodos pueden clasificarse según lo que hacen:

1. **Métodos de consulta (queries):** Retornan información sobre el estado sin modificarlo.
   - Ejemplos: `getSaldo()`, `getNombre()`, `estaVacio()`
   
2. **Métodos de modificación (commands):** Cambian el estado del objeto.
   - Ejemplos: `depositar()`, `cambiarNombre()`, `agregar()`
   
3. **Métodos de cálculo:** Realizan operaciones y retornan resultados.
   - Ejemplos: `calcularIntereses()`, `distanciaA()`

:::{tip}
**Principio de Separación Comando-Consulta (CQS):** Un método debería o bien **retornar información** o bien **modificar estado**, pero idealmente no ambas cosas. Esto hace el código más predecible y fácil de razonar.
:::

(puntero-this-java)=
### La referencia `this`

La palabra clave `this` es una **referencia especial** que todo objeto tiene **a sí mismo**. Cuando un método se ejecuta, `this` apunta automáticamente al objeto sobre el cual se invocó el método.

**Conexión con el modelo de memoria:**

Recordemos de la discusión sobre {ref}`concepto-objeto` que cada objeto tiene una **identidad** única. En términos de implementación, esa identidad corresponde a la **dirección de memoria** donde reside el objeto en el *Heap*. La palabra clave `this` es precisamente esa dirección: una referencia que el objeto tiene hacia sí mismo.

```{figure} 03/modelo_memoria_this.svg
:label: fig-modelo-memoria-this
:align: center
:width: 80%

El puntero `this` como referencia al propio objeto en el Heap.
```

(usos-this)=
#### Usos Principales de `this`

**1. Desambiguación entre atributos y parámetros:**

Cuando un parámetro tiene el mismo nombre que un atributo, Java da prioridad a la variable más "cercana" (el parámetro). Para referirse al atributo, se usa `this`:

```java
public class Persona {
    private String nombre;  // Atributo de instancia
    private int edad;       // Atributo de instancia
    
    public Persona(String nombre, int edad) {
        // Sin 'this', 'nombre' se refiere al parámetro
        // this.nombre se refiere al atributo
        this.nombre = nombre;
        this.edad = edad;
    }
    
    public void setEdad(int edad) {
        // 'edad' (parámetro) vs 'this.edad' (atributo)
        if (edad >= 0) {
            this.edad = edad;
        }
    }
}
```

:::{note}
**¿Por qué usar el mismo nombre?** Aunque podría evitarse el conflicto usando nombres diferentes (como `nombreParam`), la convención en Java es usar el mismo nombre y desambiguar con `this`. Esto hace el código más limpio y autoexplicativo: queda claro que el parámetro `nombre` se usa para inicializar el atributo `nombre`.
:::

**2. Invocar otros métodos del mismo objeto:**

Dentro de un método, se pueden llamar otros métodos del mismo objeto. El uso de `this` es opcional pero puede mejorar la claridad:

```java
public class Persona {
    private String nombre;
    private int edad;
    
    public void saludar() {
        System.out.println("Hola, soy " + nombre);
    }
    
    public void presentarse() {
        // Ambas formas son equivalentes:
        this.saludar();           // Explícito con this
        saludar();                // Implícito (this se asume)
        
        System.out.println("Tengo " + this.edad + " años");
    }
}
```

**3. Retornar la referencia al objeto actual:**

Un método puede retornar `this` para permitir el encadenamiento de llamadas (method chaining):

```java
public class Constructor {
    private String html;
    
    public Constructor() {
        html = "";
    }
    
    public Constructor agregarParrafo(String texto) {
        html += "<p>" + texto + "</p>";
        return this;  // Retorna el mismo objeto
    }
    
    public Constructor agregarTitulo(String texto) {
        html += "<h1>" + texto + "</h1>";
        return this;
    }
    
    public String construir() {
        return html;
    }
}

// Uso con encadenamiento:
String pagina = new Constructor()
    .agregarTitulo("Bienvenido")
    .agregarParrafo("Este es el contenido")
    .agregarParrafo("Otro párrafo")
    .construir();
```

**4. Invocar otro constructor de la misma clase:**

Usando `this()` (con paréntesis y argumentos), un constructor puede invocar a otro constructor de la misma clase. Esto evita duplicación de código:

```java
public class Rectangulo {
    private int ancho;
    private int alto;
    
    // Constructor principal (el más completo)
    public Rectangulo(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
    }
    
    // Constructor para cuadrados: delega al constructor principal
    public Rectangulo(int lado) {
        this(lado, lado);  // Invoca al constructor de dos parámetros
    }
    
    // Constructor por defecto: crea un rectángulo 1x1
    public Rectangulo() {
        this(1, 1);  // Invoca al constructor de dos parámetros
    }
}
```

:::{important}
La llamada `this()` debe ser la **primera instrucción** del constructor. No puede haber ninguna otra sentencia antes de ella.
:::

(instanciacion-objetos-java)=
### Instanciación de Objetos

La **instanciación** es el proceso de crear un objeto concreto a partir de una clase, tal como se describió en {ref}`concepto-instancia`. En Java, este proceso se realiza mediante el operador `new`:

**Sintaxis:**

```java
NombreClase nombreVariable = new NombreClase(argumentos);
```

**Desglose del proceso:**

```java
Persona juan = new Persona("Juan", 25);
```

| Parte | Descripción |
|:------|:------------|
| `Persona` | Tipo de la variable (la clase) |
| `juan` | Nombre de la variable (referencia) |
| `=` | Operador de asignación |
| `new` | Operador que reserva memoria y crea el objeto |
| `Persona("Juan", 25)` | Invocación del constructor con argumentos |

**¿Qué sucede en memoria?**

El proceso de instanciación involucra varias operaciones internas:

1. **Reserva de memoria en el Heap:** Se asigna espacio suficiente para almacenar todos los atributos del objeto.

2. **Inicialización de atributos por defecto:** Antes de ejecutar el constructor, todos los atributos reciben valores por defecto.

3. **Ejecución del constructor:** Se ejecuta el código del constructor, que típicamente asigna valores iniciales a los atributos.

4. **Retorno de la referencia:** El operador `new` retorna una referencia (dirección de memoria) que se almacena en la variable.

```{figure} 03/instanciacion_memoria.svg
:label: fig-instanciacion-memoria
:align: center
:width: 85%

Modelo de memoria Stack/Heap durante la instanciación de múltiples objetos.
```

**Ejemplo completo de instanciación y uso:**

```java
// Instanciación: se crean dos objetos independientes
Persona juan = new Persona("Juan", 25);
Persona maria = new Persona("María", 30);

// Cada objeto tiene su propio estado
juan.saludar();    // Imprime: Hola, soy Juan
maria.saludar();   // Imprime: Hola, soy María

// Modificar uno no afecta al otro
juan.cumplirAnios();  // juan.edad pasa a 26
// maria.edad sigue siendo 30
```

:::{note}
Esto ilustra lo que describimos en {ref}`concepto-objeto`: aunque `juan` y `maria` son instancias de la misma clase `Persona`, son **objetos completamente independientes** con estados separados. Modificar el estado de uno no afecta al otro.
:::

---

(bloque2-encapsulamiento-flexibilidad)=
## Bloque 2: Protegiendo el Estado y Flexibilidad

(encapsulamiento-implementacion)=
### Encapsulamiento: De Concepto a Implementación

En la sección de encapsulamiento en el capítulo anterior del capítulo anterior definimos el encapsulamiento como el principio de **ocultar los detalles internos** de un objeto exponiendo solo una interfaz controlada. Ahora veremos cómo implementar este principio en Java.

El encapsulamiento tiene dos aspectos complementarios:

1. **Fusión de datos y comportamiento:** Ya lo logramos al definir clases con atributos y métodos juntos (ver {ref}`la-fusion-de-datos-y-comportamiento`).

2. **Ocultamiento de información:** Lo implementamos mediante **modificadores de acceso** que restringen quién puede acceder a cada miembro de la clase.

(objetivos-encapsulamiento)=
#### ¿Por Qué Encapsular?

Recordemos los beneficios del encapsulamiento descritos en los beneficios del encapsulamiento descritos en el capítulo anterior:

1. **Protección del estado:** Impedir que código externo corrompa el estado interno del objeto asignando valores inválidos o inconsistentes.

2. **Control de acceso:** El objeto decide qué información expone y cómo permite modificarla, pudiendo aplicar validaciones.

3. **Flexibilidad de implementación:** La estructura interna puede cambiar sin afectar al código que usa la clase, siempre que la interfaz pública se mantenga.

4. **Documentación implícita:** La interfaz pública (métodos `public`) documenta qué operaciones están disponibles.

**Ejemplo del problema sin encapsulamiento:**

```java
// Sin encapsulamiento: atributos públicos
public class CuentaBancaria {
    public double saldo;        // Accesible directamente
    public String titular;      // Accesible directamente
}

// Código externo puede hacer cualquier cosa:
CuentaBancaria cuenta = new CuentaBancaria();
cuenta.saldo = -50000;          // ¡Saldo negativo imposible!
cuenta.saldo = cuenta.saldo * 2; // ¡Duplicó su dinero mágicamente!
cuenta.titular = "";            // ¡Titular vacío!
```

El objeto no tiene control sobre su propio estado. Cualquier código puede asignar valores absurdos.

(modificadores-acceso-java)=
### Modificadores de Acceso en Java

Java proporciona cuatro niveles de acceso para controlar la visibilidad de clases, atributos y métodos:

| Modificador | Clase propia | Mismo paquete | Subclases | Cualquier clase |
|:------------|:------------:|:-------------:|:---------:|:---------------:|
| `private` | ✓ | ✗ | ✗ | ✗ |
| (sin modificador) | ✓ | ✓ | ✗ | ✗ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `public` | ✓ | ✓ | ✓ | ✓ |

**Descripción detallada:**

(modificador-private)=
**`private` — Máxima restricción:**

Un miembro `private` solo es accesible desde dentro de la **misma clase**. Ni siquiera las subclases pueden accederlo directamente.

```java
public class Persona {
    private String nombre;      // Solo accesible dentro de Persona
    private int edad;           // Solo accesible dentro de Persona
    
    public Persona(String nombre, int edad) {
        this.nombre = nombre;   // OK: estamos dentro de Persona
        this.edad = edad;       // OK: estamos dentro de Persona
    }
}

// Desde otra clase:
Persona p = new Persona("Ana", 25);
String n = p.nombre;  // ERROR de compilación: nombre has private access
```

(modificador-default)=
**(sin modificador) — Acceso de paquete:**

También llamado "package-private". El miembro es accesible desde cualquier clase del **mismo paquete** (carpeta), pero no desde otros paquetes.

```java
// Archivo: modelos/Persona.java
package modelos;

class Persona {          // Sin public: solo visible en el paquete 'modelos'
    String nombre;       // Sin modificador: visible en el paquete 'modelos'
}
```

(03-sintaxis-clases-modificador-protected)=
**`protected` — Acceso para herencia:**

El miembro es accesible desde el mismo paquete **y** desde subclases (incluso en otros paquetes). Se utiliza principalmente cuando se desea que las subclases accedan a ciertos miembros sin exponerlos públicamente.

```java
public class Animal {
    protected String nombre;    // Accesible en subclases
    
    protected void emitirSonido() {
        // Las subclases pueden llamar o sobrescribir esto
    }
}
```

(modificador-public)=
**`public` — Sin restricciones:**

El miembro es accesible desde **cualquier parte** del programa. Es la interfaz que el objeto expone al mundo exterior.

```java
public class CuentaBancaria {
    // Métodos públicos: la interfaz del objeto
    public double getSaldo() {
        return saldo;
    }
    
    public void depositar(double monto) {
        // ...
    }
}
```

(regla-encapsulamiento)=
### La Regla de Oro del Encapsulamiento

:::{important}
**Principio fundamental:**
- Los **atributos** deben ser `private`
- Los **métodos** que forman la interfaz pública deben ser `public`
- Los métodos auxiliares internos pueden ser `private`
:::

Esta regla implementa lo descrito en {ref}`ocultamiento-de-informacion`: los detalles internos quedan ocultos y solo se expone una interfaz controlada.

```java
public class CuentaBancaria {
    // ═══════════════════════════════════════
    // ATRIBUTOS: siempre private
    // ═══════════════════════════════════════
    private double saldo;
    private String titular;
    private String numeroCuenta;
    
    // ═══════════════════════════════════════
    // CONSTRUCTOR: típicamente public
    // ═══════════════════════════════════════
    public CuentaBancaria(String titular, String numeroCuenta) {
        this.titular = titular;
        this.numeroCuenta = numeroCuenta;
        this.saldo = 0;  // Saldo inicial
    }
    
    // ═══════════════════════════════════════
    // INTERFAZ PÚBLICA: métodos public
    // ═══════════════════════════════════════
    public double getSaldo() {
        return saldo;
    }
    
    public void depositar(double monto) {
        if (validarMonto(monto)) {
            saldo += monto;
            registrarMovimiento("Depósito", monto);
        }
    }
    
    public boolean extraer(double monto) {
        if (validarMonto(monto) && saldo >= monto) {
            saldo -= monto;
            registrarMovimiento("Extracción", monto);
            return true;
        }
        return false;
    }
    
    // ═══════════════════════════════════════
    // MÉTODOS AUXILIARES: private
    // ═══════════════════════════════════════
    private boolean validarMonto(double monto) {
        return monto > 0;
    }
    
    private void registrarMovimiento(String tipo, double monto) {
        // Lógica interna de registro
        System.out.println(tipo + ": $" + monto);
    }
}
```

(getters-setters-java)=
### Getters y Setters: Práctica Común vs. Buen Diseño

En la industria es común encontrar métodos **accesores (getters)** y **mutadores (setters)** para acceder y modificar atributos privados. Sin embargo, su uso indiscriminado representa una **violación encubierta del encapsulamiento**.

:::{warning}
**Posición de la Cátedra sobre Getters y Setters**

Esta cátedra **no acepta** el uso generalizado de getters y setters como práctica de diseño. Se espera que las clases tengan **comportamiento con nombre significativo** que refleje las operaciones del dominio, en lugar de simples accesores que expongan la estructura interna.

**¿Por qué?** Crear getters y setters para todos los atributos equivale a hacer públicos esos atributos con pasos adicionales. El encapsulamiento no se trata solo de poner `private`; se trata de **ocultar la implementación** y exponer **comportamiento significativo**.
:::

(problema-getters-setters)=
#### El Problema con Getters y Setters

Consideremos una clase `CuentaBancaria` con el enfoque tradicional de getters/setters:

```java
// ❌ MAL DISEÑO: Getters y setters exponen la estructura interna
public class CuentaBancaria {
    private double saldo;
    
    public double getSaldo() {
        return saldo;
    }
    
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}

// El código cliente hace la lógica:
cuenta.setSaldo(cuenta.getSaldo() + 100);  // Depósito
cuenta.setSaldo(cuenta.getSaldo() - 50);   // Extracción
```

**Problemas de este enfoque:**

1. **Lógica dispersa:** La operación "depositar" está fuera del objeto
2. **Sin validaciones:** Cualquiera puede asignar saldos negativos
3. **Acoplamiento alto:** Si cambia la representación interna, todo el código cliente se rompe
4. **Falta de semántica:** `setSaldo` no comunica intención de negocio

(comportamiento-vs-accesores)=
#### Comportamiento con Nombre vs. Accesores

El diseño correcto reemplaza accesores genéricos por **métodos con significado en el dominio**:

```java
// ✓ BUEN DISEÑO: Comportamiento con nombre significativo
public class CuentaBancaria {
    private double saldo;
    
    public CuentaBancaria(double saldoInicial) {
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("Saldo inicial no puede ser negativo");
        }
        this.saldo = saldoInicial;
    }
    
    // En lugar de getSaldo() + setSaldo(), métodos con COMPORTAMIENTO:
    
    public void depositar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("Monto debe ser positivo");
        }
        saldo += monto;
    }
    
    public void extraer(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("Monto debe ser positivo");
        }
        if (monto > saldo) {
            throw new IllegalStateException("Saldo insuficiente");
        }
        saldo -= monto;
    }
    
    public void transferirA(CuentaBancaria destino, double monto) {
        this.extraer(monto);
        destino.depositar(monto);
    }
    
    // Consulta con nombre significativo (no "getSaldo")
    public double consultarSaldo() {
        return saldo;
    }
    
    public boolean puedeExtraer(double monto) {
        return monto > 0 && monto <= saldo;
    }
}

// Uso: el código cliente expresa INTENCIÓN
cuenta.depositar(100);
cuenta.extraer(50);
cuenta.transferirA(otraCuenta, 200);
```

**Ventajas de este enfoque:**

1. **Lógica encapsulada:** Las validaciones están dentro del objeto
2. **Semántica clara:** `depositar()` comunica intención, `setSaldo()` no
3. **Imposible estados inválidos:** No se puede asignar saldo negativo
4. **Bajo acoplamiento:** Si cambia la representación interna, el código cliente no cambia

(cuando-getters-aceptables)=
#### Cuándo Son Aceptables los Getters

Existen casos donde un getter tiene sentido:

1. **Valores inmutables de identidad:** Consultar el DNI, número de cuenta, ISBN
2. **Representación textual:** Métodos como `toString()` necesitan acceder a atributos
3. **Valores calculados:** Cuando el "getter" realmente calcula algo (como `getEdad()` a partir de fecha de nacimiento)

```java
public class Libro {
    private final String isbn;      // Inmutable, identificador
    private final String titulo;    // Inmutable
    private boolean disponible;     // Mutable, pero controlado
    
    // ✓ Aceptable: valor inmutable de identidad
    public String getIsbn() {
        return isbn;
    }
    
    // ✓ Aceptable: valor inmutable para mostrar
    public String getTitulo() {
        return titulo;
    }
    
    // ❌ En lugar de isDisponible() y setDisponible()...
    // ✓ Comportamiento con nombre:
    public void prestar() {
        if (!disponible) {
            throw new IllegalStateException("Libro ya prestado");
        }
        disponible = false;
    }
    
    public void devolver() {
        disponible = true;
    }
    
    public boolean estaDisponible() {
        return disponible;
    }
}
```

(setters-prohibidos)=
#### Los Setters: Casi Siempre Evitables

Los setters son particularmente problemáticos porque:

1. **Exponen mutabilidad:** Permiten cambiar el estado sin control
2. **No expresan intención:** `setEstado(3)` no comunica nada
3. **Violan "Tell, Don't Ask":** En lugar de *pedirle* datos al objeto y procesarlos afuera, debemos *decirle* al objeto qué hacer

:::{tip}
**Principio "Tell, Don't Ask" (Decile, no le preguntes)**

En lugar de:
```java
// ❌ Pedir datos y procesar afuera
if (cuenta.getSaldo() >= monto) {
    cuenta.setSaldo(cuenta.getSaldo() - monto);
}
```

Hacer:
```java
// ✓ Decirle al objeto qué hacer
cuenta.extraer(monto);  // El objeto decide si puede
```

El objeto tiene la información y la lógica; dejá que él tome las decisiones.
:::

(resumen-posicion-catedra)=
#### Resumen: Criterio de la Cátedra

| Situación | Enfoque esperado |
|:----------|:-----------------|
| Necesito leer un valor identificador | Getter aceptable (`getIsbn()`, `getDni()`) |
| Necesito modificar un atributo | **Método con comportamiento** (`depositar()`, `prestar()`) |
| Necesito consultar estado | Método con nombre descriptivo (`estaDisponible()`, `puedeExtraer()`) |
| Necesito cambiar configuración | Método específico (`cambiarEmail()`, `actualizarDireccion()`) |

:::{important}
**En los trabajos prácticos de esta cátedra:**

- **No crear setters** salvo casos excepcionales justificados
- **Preferir métodos con nombre de dominio** sobre accesores genéricos
- **Los getters son aceptables** para valores inmutables o identificadores
- Preguntarse siempre: *"¿Este método expresa una acción del dominio o solo accede a un dato?"*
:::

(atributos-inmutables)=
#### Atributos Inmutables

Algunos atributos no deberían cambiar después de la construcción del objeto. Para estos casos:

1. Declarar el atributo como `private final`
2. Inicializarlo en el constructor
3. Proporcionar solo un método de consulta si es necesario (nunca setter)

```java
public class Persona {
    private final String dni;       // Inmutable: no cambia nunca
    private String nombre;          // Mutable: puede cambiar
    
    public Persona(String dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
    }
    
    // Consulta de valor inmutable (aceptable)
    public String getDni() {
        return dni;
    }
    
    // NO hay setDni(): el DNI no puede cambiar
    
    // En lugar de setNombre(), un método con semántica:
    public void corregirNombre(String nombreCorregido) {
        if (nombreCorregido == null || nombreCorregido.isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacío");
        }
        this.nombre = nombreCorregido;
    }
    
    public String obtenerNombre() {
        return nombre;
    }
}
```

(nomenclatura-java)=
### Convenciones de Nomenclatura en Java

Las convenciones de nomenclatura son fundamentales para escribir código legible y mantenible. Java tiene estándares bien establecidos que esta cátedra adopta y exige.

(nomenclatura-clases)=
#### Nombres de Clases

Las clases se nombran en **PascalCase** (también llamado UpperCamelCase): cada palabra comienza con mayúscula, sin separadores.

```java
// ✓ Correcto: PascalCase
public class CuentaBancaria { }
public class RegistroDeVentas { }
public class SistemaDeGestion { }

// ❌ Incorrecto
public class cuentaBancaria { }     // Empieza con minúscula
public class cuenta_bancaria { }    // Usa guion bajo
public class CUENTABANCARIA { }     // Todo mayúsculas
```

**El nombre debe ser un sustantivo** que represente el concepto que modela:

```java
// ✓ Sustantivos que representan entidades
public class Persona { }
public class Factura { }
public class Motor { }

// ❌ Verbos o acciones (eso es para métodos)
public class Calcular { }
public class Procesar { }
```

(nomenclatura-atributos)=
#### Nombres de Atributos

Los atributos se nombran en **camelCase**: comienzan con minúscula, y cada palabra subsiguiente con mayúscula.

```java
public class Persona {
    // ✓ Correcto: camelCase
    private String nombre;
    private String apellido;
    private int edad;
    private LocalDate fechaNacimiento;
    private String direccionPostal;
    
    // ❌ Incorrecto
    private String Nombre;           // Empieza con mayúscula
    private String fecha_nacimiento; // Usa guion bajo
    private int EDAD;                // Todo mayúsculas
}
```

(nomenclatura-plural-colecciones)=
#### Nombres en Plural para Colecciones

:::{important}
**Regla de la Cátedra: Plural para Colecciones**

Cuando un atributo representa una **colección de elementos** (lista, conjunto, etc.), su nombre debe estar en **plural** para indicar que contiene múltiples elementos.
:::

```java
public class Biblioteca {
    // ✓ Correcto: plural indica que son múltiples
    private List<Libro> libros;
    private List<Socio> socios;
    private List<Prestamo> prestamos;
    
    // ❌ Incorrecto: singular para colecciones
    private List<Libro> libro;      // Confuso: ¿es uno o varios?
    private List<Socio> listaSocio; // Redundante y mal nombrado
}

public class Equipo {
    // ✓ Correcto
    private List<Jugador> jugadores;
    private List<Partido> partidosJugados;
    
    // ❌ Incorrecto
    private List<Jugador> jugador;
    private List<Partido> listaDePartidos;  // "lista" es redundante
}
```

**Contraste con atributos singulares:**

```java
public class Auto {
    // Singular: hay exactamente uno
    private Motor motor;
    private Conductor conductor;
    private String patente;
    
    // Plural: hay cero o más
    private List<Pasajero> pasajeros;
    private List<Multa> multas;
}
```

(nomenclatura-metodos)=
#### Nombres de Métodos

Los métodos se nombran en **camelCase** y generalmente comienzan con un **verbo** que indica la acción:

```java
public class CuentaBancaria {
    // ✓ Correcto: verbos que indican acción
    public void depositar(double monto) { }
    public void extraer(double monto) { }
    public void transferirA(CuentaBancaria destino, double monto) { }
    
    // ✓ Correcto: consultas con verbos
    public double consultarSaldo() { }
    public boolean puedeExtraer(double monto) { }
    public boolean estaActiva() { }
    
    // ❌ Incorrecto: no comunican acción
    public void saldo() { }          // ¿Consulta o modifica?
    public void monto(double m) { }  // ¿Qué hace con el monto?
}
```

**Patrones comunes de nombres de métodos:**

| Patrón | Uso | Ejemplos |
|:-------|:----|:---------|
| `verbo` | Acción simple | `depositar()`, `guardar()`, `eliminar()` |
| `verboSustantivo` | Acción sobre algo | `agregarLibro()`, `calcularTotal()` |
| `verboPreposición` | Acción con destino | `transferirA()`, `enviarPor()` |
| `esSustantivo` / `estáAdjetivo` | Consulta booleana | `esValido()`, `estaVacio()`, `estaActivo()` |
| `tieneSustantivo` | Consulta de existencia | `tieneDeuda()`, `tieneSocio()` |
| `puedeverbo` | Consulta de capacidad | `puedeExtraer()`, `puedePrestar()` |
| `obtenerSustantivo` | Consulta de valor | `obtenerSaldo()`, `obtenerNombre()` |
| `calcularSustantivo` | Cálculo | `calcularInteres()`, `calcularEdad()` |

(nomenclatura-metodos-colecciones)=
#### Métodos que Operan sobre Colecciones

Cuando un método opera sobre una colección interna, el nombre debe reflejar la operación sobre **un elemento** de esa colección:

```java
public class Biblioteca {
    private List<Libro> libros;
    private List<Socio> socios;
    
    // ✓ Correcto: operan sobre UN elemento de la colección
    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }
    
    public void quitarLibro(Libro libro) {
        libros.remove(libro);
    }
    
    public void registrarSocio(Socio socio) {
        socios.add(socio);
    }
    
    public void darDeBajaSocio(Socio socio) {
        socios.remove(socio);
    }
    
    // ✓ Consultas sobre la colección
    public int cantidadLibros() {
        return libros.size();
    }
    
    public boolean tieneSocio(Socio socio) {
        return socios.contains(socio);
    }
    
    // ❌ Incorrecto: nombres confusos
    public void libros(Libro l) { }      // ¿Qué hace?
    public void addLibro(Libro l) { }    // Mezcla inglés/español
    public void setLibros(List<Libro> l) { } // Setter genérico (evitar)
}
```

(nomenclatura-parametros)=
#### Nombres de Parámetros

Los parámetros siguen **camelCase** y deben tener nombres **descriptivos**:

```java
public class Transferencia {
    // ✓ Correcto: parámetros descriptivos
    public void ejecutar(CuentaBancaria origen, 
                         CuentaBancaria destino, 
                         double monto) {
        origen.extraer(monto);
        destino.depositar(monto);
    }
    
    // ❌ Incorrecto: nombres crípticos
    public void ejecutar(CuentaBancaria c1, 
                         CuentaBancaria c2, 
                         double m) {
        // ¿Cuál es origen y cuál destino?
    }
}
```

(nomenclatura-constantes)=
#### Constantes

Las constantes (atributos `static final`) se nombran en **SCREAMING_SNAKE_CASE**: todo mayúsculas con guiones bajos:

```java
public class Configuracion {
    // ✓ Correcto: constantes en mayúsculas
    public static final int MAX_INTENTOS = 3;
    public static final double TASA_INTERES = 0.05;
    public static final String MENSAJE_ERROR = "Operación inválida";
    
    // ❌ Incorrecto
    public static final int maxIntentos = 3;    // Parece variable
    public static final int Max_Intentos = 3;   // Mezcla estilos
}
```

(nomenclatura-resumen)=
#### Resumen de Convenciones

| Elemento | Convención | Ejemplo |
|:---------|:-----------|:--------|
| Clase | `PascalCase` (sustantivo) | `CuentaBancaria`, `RegistroVentas` |
| Atributo | `camelCase` | `saldoActual`, `fechaCreacion` |
| Atributo colección | `camelCase` **plural** | `libros`, `prestamos`, `jugadores` |
| Método | `camelCase` (verbo) | `depositar()`, `calcularTotal()` |
| Parámetro | `camelCase` descriptivo | `montoDeposito`, `cuentaDestino` |
| Constante | `SCREAMING_SNAKE_CASE` | `MAX_REINTENTOS`, `TASA_IVA` |

:::{tip}
**Criterio general:** Un buen nombre hace que el código se lea casi como prosa en español. Si necesitás agregar un comentario para explicar qué es una variable o qué hace un método, probablemente el nombre no sea lo suficientemente descriptivo.

```java
// ❌ Necesita comentario para entenderse
int d; // días desde la última compra

// ✓ Se entiende solo
int diasDesdeUltimaCompra;
```
:::

La **sobrecarga** (overloading) es la capacidad de definir **múltiples métodos con el mismo nombre** dentro de la misma clase, siempre que difieran en su **lista de parámetros** (cantidad, tipos, o ambos).

(que-es-sobrecarga)=
#### ¿Qué es la Sobrecarga?

Cuando Java encuentra una llamada a un método, determina cuál versión invocar basándose en los **argumentos proporcionados**. Esto se conoce como **resolución de sobrecarga** y ocurre en **tiempo de compilación**.

```java
public class Calculadora {
    // Versión 1: suma de dos enteros
    public int sumar(int a, int b) {
        return a + b;
    }
    
    // Versión 2: suma de tres enteros
    public int sumar(int a, int b, int c) {
        return a + b + c;
    }
    
    // Versión 3: suma de dos decimales
    public double sumar(double a, double b) {
        return a + b;
    }
}
```

**Uso:**

```java
Calculadora calc = new Calculadora();

int r1 = calc.sumar(5, 3);          // Invoca versión 1 → 8
int r2 = calc.sumar(5, 3, 2);       // Invoca versión 2 → 10
double r3 = calc.sumar(5.5, 3.2);   // Invoca versión 3 → 8.7
```

Java selecciona automáticamente la versión correcta según los argumentos.

(por-que-sobrecargar)=
#### ¿Por Qué es Útil la Sobrecarga?

La sobrecarga proporciona **flexibilidad** y **comodidad** al usuario de la clase:

1. **Misma operación, diferentes datos:** Permite realizar la "misma" acción con distintos tipos o cantidades de información.

2. **Valores por defecto implícitos:** Versiones con menos parámetros pueden asumir valores predeterminados.

3. **Interfaz intuitiva:** El usuario no necesita recordar nombres diferentes para variantes de la misma operación.

(reglas-sobrecarga)=
#### Reglas de la Sobrecarga

:::{important}
**Requisitos para que dos métodos sean considerados sobrecargas:**

1. **Mismo nombre:** Los métodos deben llamarse exactamente igual.

2. **Diferente lista de parámetros:** Debe variar al menos uno de estos aspectos:
   - Cantidad de parámetros
   - Tipo de parámetros
   - Orden de los tipos (si son diferentes)

3. **El tipo de retorno NO diferencia sobrecargas:** Dos métodos con los mismos parámetros pero distinto retorno causan error de compilación.

4. **Los nombres de parámetros NO diferencian sobrecargas:** Solo importan los tipos.
:::

**Ejemplos válidos e inválidos:**

```java
public class Ejemplo {
    // ✓ VÁLIDO: diferente cantidad de parámetros
    public void metodo(int a) { }
    public void metodo(int a, int b) { }
    
    // ✓ VÁLIDO: diferente tipo de parámetro
    public void metodo(double a) { }
    
    // ✓ VÁLIDO: diferente orden de tipos
    public void metodo(int a, String b) { }
    public void metodo(String a, int b) { }
    
    // ✗ INVÁLIDO: solo cambia el nombre del parámetro
    // public void metodo(int x) { }  // ERROR: ya existe metodo(int)
    
    // ✗ INVÁLIDO: solo cambia el tipo de retorno
    // public String metodo(int a) { }  // ERROR: ya existe metodo(int)
}
```

(sobrecarga-constructores)=
#### Sobrecarga de Constructores

Es extremadamente común sobrecargar constructores para ofrecer **diferentes formas de crear objetos**:

```java
public class Persona {
    private String nombre;
    private int edad;
    private String email;
    private String telefono;
    
    // Constructor completo: todos los datos
    public Persona(String nombre, int edad, String email, String telefono) {
        this.nombre = nombre;
        this.edad = edad;
        this.email = email;
        this.telefono = telefono;
    }
    
    // Constructor parcial: sin teléfono
    public Persona(String nombre, int edad, String email) {
        this(nombre, edad, email, "No especificado");
    }
    
    // Constructor mínimo: solo datos obligatorios
    public Persona(String nombre, int edad) {
        this(nombre, edad, "sin-email@example.com", "No especificado");
    }
    
    // Constructor con valores por defecto
    public Persona(String nombre) {
        this(nombre, 0, "sin-email@example.com", "No especificado");
    }
}
```

**Uso:**

```java
// Diferentes formas de crear el mismo tipo de objeto
Persona p1 = new Persona("Carlos", 35, "carlos@mail.com", "261-555-1234");
Persona p2 = new Persona("Laura", 42, "laura@mail.com");
Persona p3 = new Persona("Pedro", 28);
Persona p4 = new Persona("Ana");
```

:::{tip}
**Patrón de delegación de constructores:**

Usá `this()` para que los constructores más simples invoquen a los más completos. Esto centraliza la lógica de inicialización y evita duplicación:

```java
// Constructor principal: toda la lógica aquí
public Persona(String nombre, int edad, String email) {
    // Validaciones
    if (nombre == null || nombre.isEmpty()) {
        throw new IllegalArgumentException("Nombre requerido");
    }
    if (edad < 0) {
        throw new IllegalArgumentException("Edad inválida");
    }
    
    // Inicialización
    this.nombre = nombre;
    this.edad = edad;
    this.email = email;
}

// Constructores secundarios: delegan al principal
public Persona(String nombre, int edad) {
    this(nombre, edad, "sin-email@example.com");
}

public Persona(String nombre) {
    this(nombre, 0);
}
```

Así, las validaciones solo se escriben una vez.
:::

(sobrecarga-metodos-regulares)=
#### Sobrecarga de Métodos Regulares

Los métodos ordinarios también pueden sobrecargarse:

```java
public class Impresora {
    // Imprimir un mensaje simple
    public void imprimir(String mensaje) {
        System.out.println(mensaje);
    }
    
    // Imprimir un mensaje con formato
    public void imprimir(String mensaje, boolean mayusculas) {
        if (mayusculas) {
            System.out.println(mensaje.toUpperCase());
        } else {
            System.out.println(mensaje);
        }
    }
    
    // Imprimir múltiples veces
    public void imprimir(String mensaje, int veces) {
        for (int i = 0; i < veces; i++) {
            System.out.println(mensaje);
        }
    }
    
    // Imprimir un número
    public void imprimir(int numero) {
        System.out.println("Número: " + numero);
    }
}
```

**Uso:**

```java
Impresora imp = new Impresora();

imp.imprimir("Hola");                    // "Hola"
imp.imprimir("Hola", true);              // "HOLA"
imp.imprimir("Hola", 3);                 // "Hola" (3 veces)
imp.imprimir(42);                        // "Número: 42"
```

(sobrecarga-vs-parametros-opcionales)=
#### Sobrecarga vs Parámetros Opcionales

Algunos lenguajes (como Python o JavaScript) permiten parámetros opcionales con valores por defecto:

```python
# Python (NO es Java)
def saludar(nombre, formal=False):
    if formal:
        print(f"Estimado/a {nombre}")
    else:
        print(f"Hola {nombre}")
```

**Java no soporta parámetros opcionales directamente**, por lo que la sobrecarga es la alternativa:

```java
public class Saludador {
    public void saludar(String nombre) {
        saludar(nombre, false);  // Delega con valor por defecto
    }
    
    public void saludar(String nombre, boolean formal) {
        if (formal) {
            System.out.println("Estimado/a " + nombre);
        } else {
            System.out.println("Hola " + nombre);
        }
    }
}
```

---

(bloque3-relaciones-colaboracion)=
### Implementación de Asociaciones en Java

Las asociaciones se implementan mediante **atributos** que contienen referencias a objetos de la otra clase.

(asociacion-uno-a-uno)=
#### Asociación Uno a Uno (1:1)

```{figure} 03/asociacion_uno_a_uno.svg
:label: fig-asociacion-1-1
:align: center
:width: 60%

Asociación uno a uno (1:1) entre Persona y DNI.
```

**Implementación:**

```java
public class DNI {
    private String numero;
    private LocalDate fechaEmision;
    
    public DNI(String numero, LocalDate fechaEmision) {
        this.numero = numero;
        this.fechaEmision = fechaEmision;
    }
    
    public String getNumero() {
        return numero;
    }
}

public class Persona {
    private String nombre;
    private DNI documento;  // Asociación 1:1
    
    public Persona(String nombre, DNI documento) {
        this.nombre = nombre;
        this.documento = documento;
    }
    
    public String getNumeroDocumento() {
        return documento.getNumero();  // Delega al objeto asociado
    }
}
```

(asociacion-uno-a-muchos)=
#### Asociación Uno a Muchos (1:*)

```{figure} 03/asociacion_uno_a_muchos.svg
:label: fig-asociacion-1-n
:align: center
:width: 60%

Asociación uno a muchos (1:*) entre Persona y Libro.
```

**Implementación usando colecciones:**

```java
import java.util.ArrayList;
import java.util.List;

public class Libro {
    private String titulo;
    private String autor;
    
    public Libro(String titulo, String autor) {
        this.titulo = titulo;
        this.autor = autor;
    }
    
    public String getTitulo() {
        return titulo;
    }
}

public class Persona {
    private String nombre;
    private List<Libro> libros;  // Asociación 1:* usando ArrayList
    
    public Persona(String nombre) {
        this.nombre = nombre;
        this.libros = new ArrayList<>();  // Inicializar la colección
    }
    
    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }
    
    public void quitarLibro(Libro libro) {
        libros.remove(libro);
    }
    
    public int cantidadLibros() {
        return libros.size();
    }
    
    public List<Libro> getLibros() {
        // Retornar copia para proteger la colección interna
        return new ArrayList<>(libros);
    }
}
```

**Uso:**

```java
Persona juan = new Persona("Juan");
Libro libro1 = new Libro("1984", "George Orwell");
Libro libro2 = new Libro("El Principito", "Saint-Exupéry");

juan.agregarLibro(libro1);
juan.agregarLibro(libro2);

System.out.println(juan.cantidadLibros());  // 2
```

:::{note}
El atributo `libros` es de tipo `List<Libro>`, que es una **interfaz** de Java que representa una lista ordenada. `ArrayList` es una **implementación** concreta de esa interfaz. Usar la interfaz como tipo del atributo permite cambiar la implementación sin modificar el resto del código.
:::

(asociacion-bidireccional)=
#### Asociación Bidireccional

A veces, la navegación debe ser posible en **ambas direcciones**:

```{figure} 03/asociacion_bidireccional.svg
:label: fig-asociacion-bidireccional
:align: center
:width: 70%

Asociación bidireccional entre Persona y Libro. Ambos lados mantienen la referencia al otro.
```

**Implementación:**

```java
public class Libro {
    private String titulo;
    private Persona propietario;  // Navegación inversa
    
    public Libro(String titulo) {
        this.titulo = titulo;
        this.propietario = null;
    }
    
    public void setPropietario(Persona propietario) {
        this.propietario = propietario;
    }
    
    public Persona getPropietario() {
        return propietario;
    }
}

public class Persona {
    private String nombre;
    private List<Libro> libros;
    
    public Persona(String nombre) {
        this.nombre = nombre;
        this.libros = new ArrayList<>();
    }
    
    public void agregarLibro(Libro libro) {
        libros.add(libro);
        libro.setPropietario(this);  // Mantener consistencia bidireccional
    }
    
    public void quitarLibro(Libro libro) {
        if (libros.remove(libro)) {
            libro.setPropietario(null);  // Mantener consistencia
        }
    }
}
```

:::{warning}
**Las asociaciones bidireccionales requieren cuidado:** Ambos lados de la relación deben mantenerse sincronizados. Es una fuente común de errores cuando se modifica solo un lado.
:::

(composicion-java)=
### Composición

La **composición** es un tipo especial de asociación que representa una relación fuerte de **"todo/parte"** donde:

- La **parte no puede existir sin el todo**
- El **ciclo de vida** de la parte está controlado completamente por el todo
- Si el todo se destruye, las partes también se destruyen
- Las partes son **exclusivas** del todo (no se comparten)

Esta relación corresponde conceptualmente a lo que se describió en {ref}`relaciones-entre-clases` como una relación de pertenencia estricta.

(composicion-uml)=
#### Representación UML

La composición se representa con un **rombo relleno (♦)** del lado del "todo":

```{figure} 03/uml_composicion.svg
:label: fig-uml-composicion
:align: center
:width: 80%

Diagrama de clases UML que representa la composición entre Auto y Motor.
```

(composicion-implementacion)=
#### Implementación de Composición

La característica distintiva en código: **el todo crea la parte** dentro de sí mismo, típicamente en el constructor.

```java
public class Motor {
    private int cilindros;
    private int potenciaHP;
    private boolean encendido;
    
    public Motor(int cilindros, int potenciaHP) {
        this.cilindros = cilindros;
        this.potenciaHP = potenciaHP;
        this.encendido = false;
    }
    
    public void encender() {
        encendido = true;
        System.out.println("Motor encendido: " + potenciaHP + " HP");
    }
    
    public void apagar() {
        encendido = false;
        System.out.println("Motor apagado");
    }
    
    public boolean estaEncendido() {
        return encendido;
    }
}

public class Auto {
    private String modelo;
    private int anio;
    private Motor motor;  // Composición: el auto "tiene un" motor
    
    public Auto(String modelo, int anio, int cilindros, int potencia) {
        this.modelo = modelo;
        this.anio = anio;
        
        // ═══════════════════════════════════════════════════════════
        // CLAVE DE LA COMPOSICIÓN: el Auto CREA su propio Motor
        // El Motor no existe antes del Auto, ni existirá después
        // ═══════════════════════════════════════════════════════════
        this.motor = new Motor(cilindros, potencia);
    }
    
    public void arrancar() {
        System.out.println("Arrancando " + modelo + " (" + anio + ")");
        motor.encender();  // Delega al componente
    }
    
    public void detener() {
        motor.apagar();
        System.out.println(modelo + " detenido");
    }
    
    public boolean estaEnMarcha() {
        return motor.estaEncendido();
    }
}
```

**Uso:**

```java
// Se crea el auto, y automáticamente se crea su motor interno
Auto miAuto = new Auto("Fiat 147", 1985, 4, 65);

miAuto.arrancar();
// Salida:
// Arrancando Fiat 147 (1985)
// Motor encendido: 65 HP

System.out.println(miAuto.estaEnMarcha());  // true

// Cuando miAuto deje de existir (garbage collected),
// su motor interno también dejará de existir.
```

:::{important}
**Señales de composición en el código:**

1. La parte se **crea dentro del constructor** del todo
2. La parte **no se recibe como parámetro** (o si se recibe, se copia)
3. La parte **no tiene setter público** para ser reemplazada externamente
4. El todo **no comparte** la referencia a la parte con otros objetos
:::

(composicion-multiples-partes)=
#### Composición con Múltiples Partes

Un objeto puede estar compuesto por múltiples partes:

```java
public class Computadora {
    private Procesador cpu;
    private MemoriaRAM ram;
    private DiscoDuro disco;
    
    public Computadora(String modeloCPU, int velocidadGHz,
                       int gbRAM, int gbDisco) {
        // La computadora crea todos sus componentes
        this.cpu = new Procesador(modeloCPU, velocidadGHz);
        this.ram = new MemoriaRAM(gbRAM);
        this.disco = new DiscoDuro(gbDisco);
    }
    
    public void encender() {
        cpu.iniciar();
        ram.inicializar();
        disco.montar();
        System.out.println("Computadora lista");
    }
}
```

(agregacion-java)=
### Agregación

La **agregación** es una relación de **"todo/parte"** más débil que la composición:

- La parte **puede existir independientemente** del todo
- El ciclo de vida de la parte **no depende** del todo
- Las partes pueden ser **compartidas** entre varios todos
- Si el todo se destruye, las partes **siguen existiendo**

(agregacion-uml)=
#### Representación UML

La agregación se representa con un **rombo vacío (◇)** del lado del contenedor:

```{figure} 03/uml_agregacion.svg
:label: fig-uml-agregacion
:align: center
:width: 80%

Diagrama de clases UML que representa la agregación entre Auto y Conductor.
```

(agregacion-implementacion)=
#### Implementación de Agregación

La característica distintiva: **el todo recibe la parte** como parámetro (no la crea).

```java
public class Conductor {
    private String nombre;
    private String licencia;
    
    public Conductor(String nombre, String licencia) {
        this.nombre = nombre;
        this.licencia = licencia;
    }
    
    public void conducir() {
        System.out.println(nombre + " está conduciendo");
    }
    
    public String getNombre() {
        return nombre;
    }
}

public class Auto {
    private String modelo;
    private int anio;
    private Conductor conductor;  // Agregación: el auto "tiene un" conductor
    
    public Auto(String modelo, int anio) {
        this.modelo = modelo;
        this.anio = anio;
        this.conductor = null;  // El auto puede existir SIN conductor
    }
    
    // ═══════════════════════════════════════════════════════════
    // CLAVE DE LA AGREGACIÓN: el Auto RECIBE un Conductor externo
    // El Conductor existe antes y después de la asociación
    // ═══════════════════════════════════════════════════════════
    public void asignarConductor(Conductor conductor) {
        this.conductor = conductor;
    }
    
    public void liberarConductor() {
        this.conductor = null;
    }
    
    public void iniciarViaje() {
        if (conductor != null) {
            System.out.println("Viaje en " + modelo);
            conductor.conducir();
        } else {
            System.out.println("No hay conductor asignado al " + modelo);
        }
    }
    
    public boolean tieneConductor() {
        return conductor != null;
    }
}
```

**Uso:**

```java
// El conductor existe ANTES de cualquier auto
Conductor pedro = new Conductor("Pedro García", "B-12345678");

// Los autos se crean SIN conductor
Auto auto1 = new Auto("Ford Ka", 2020);
Auto auto2 = new Auto("Chevrolet Corsa", 2018);

// Se asigna el mismo conductor a un auto
auto1.asignarConductor(pedro);
auto1.iniciarViaje();
// Salida: Viaje en Ford Ka
//         Pedro García está conduciendo

// El conductor puede ser reasignado a otro auto
auto1.liberarConductor();
auto2.asignarConductor(pedro);
auto2.iniciarViaje();
// Salida: Viaje en Chevrolet Corsa
//         Pedro García está conduciendo

// Si auto1 se destruye, pedro sigue existiendo
auto1 = null;  // auto1 será garbage collected
// pedro sigue siendo un objeto válido
System.out.println(pedro.getNombre());  // "Pedro García"
```

:::{note}
**Señales de agregación en el código:**

1. La parte se **recibe como parámetro** (no se crea internamente)
2. La parte **existe antes** de ser asociada al todo
3. La parte puede tener un **setter** para cambiarla
4. El todo puede existir **sin** la parte (`null` es válido)
5. La misma parte puede **pertenecer a múltiples** todos (simultáneamente o secuencialmente)
:::

(comparacion-composicion-agregacion)=
### Comparación: Composición vs Agregación

| Aspecto | Composición ♦ | Agregación ◇ |
|:--------|:--------------|:-------------|
| **Fuerza de relación** | Fuerte ("es parte esencial de") | Débil ("está asociado con") |
| **Ciclo de vida** | La parte depende del todo | La parte es independiente |
| **Creación de la parte** | El todo crea la parte | La parte existe externamente |
| **Exclusividad** | La parte es exclusiva del todo | La parte puede compartirse |
| **En código** | `new Parte()` en constructor | Parte recibida como parámetro |
| **Existencia sin relación** | La parte no existe sola | La parte puede existir sola |
| **Ejemplo** | Auto-Motor, Casa-Habitación | Auto-Conductor, Equipo-Jugador |

(cuando-usar-composicion-agregacion)=
#### ¿Cuándo Usar Cada Una?

**Usá Composición cuando:**
- La parte **no tiene sentido** sin el todo
- El todo **controla completamente** el ciclo de vida de la parte
- La parte es un **detalle de implementación** del todo
- No necesitás **compartir** la parte con otros objetos

**Ejemplos de composición:**
- Factura → LíneasDeFactura (las líneas no existen sin la factura)
- Edificio → Pisos (un piso no existe sin su edificio)
- Pedido → Items (los items son del pedido)

**Usá Agregación cuando:**
- La parte puede **existir independientemente**
- La parte puede ser **compartida** entre varios todos
- El todo **no crea** la parte, sino que la **recibe**
- La parte tiene un **ciclo de vida propio**

**Ejemplos de agregación:**
- Empresa → Empleados (un empleado puede cambiar de empresa)
- Curso → Estudiantes (un estudiante puede estar en varios cursos)
- Biblioteca → Libros (un libro puede transferirse a otra biblioteca)

(ejemplo-mixto)=
#### Ejemplo Mixto: Auto con Composición y Agregación

Un diseño realista suele combinar ambos tipos de relaciones:

```java
public class Auto {
    private String patente;
    
    // COMPOSICIÓN: el auto CREA sus propias partes internas
    private Motor motor;           // ♦ No existe sin el auto
    private Chasis chasis;         // ♦ No existe sin el auto
    private SistemaElectrico electrica;  // ♦ No existe sin el auto
    
    // AGREGACIÓN: el auto RECIBE elementos externos
    private Conductor conductor;    // ◇ Existe independientemente
    private Seguro seguro;         // ◇ Puede cambiarse
    private GPS gps;               // ◇ Puede quitarse y ponerse
    
    public Auto(String patente, int cilindros, int potencia) {
        this.patente = patente;
        
        // Composición: crear las partes esenciales
        this.motor = new Motor(cilindros, potencia);
        this.chasis = new Chasis(patente);
        this.electrica = new SistemaElectrico();
        
        // Agregación: inicialmente sin elementos opcionales
        this.conductor = null;
        this.seguro = null;
        this.gps = null;
    }
    
    // Métodos para agregaciones
    public void asignarConductor(Conductor c) { this.conductor = c; }
    public void contratarSeguro(Seguro s) { this.seguro = s; }
    public void instalarGPS(GPS g) { this.gps = g; }
    public void desinstalarGPS() { this.gps = null; }
}

---

(bloque4-integracion-practica)=
## Bloque 4: Integración y Práctica

En esta sección integramos todos los conceptos vistos en ejemplos completos que demuestran cómo trabajar con clases, encapsulamiento, sobrecarga, y relaciones entre objetos.

(ejemplo-completo-biblioteca)=
### Ejemplo Completo: Sistema de Biblioteca

Desarrollemos un sistema simplificado de biblioteca que integre todos los conceptos:

```java
import java.util.ArrayList;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════
// CLASE LIBRO
// Representa un libro físico en la biblioteca.
// Demuestra: encapsulamiento, getters, métodos de comportamiento
// ═══════════════════════════════════════════════════════════════════════════
public class Libro {
    // Atributos privados (encapsulamiento)
    private final String isbn;        // Inmutable: identificador único
    private final String titulo;      // Inmutable: no cambia
    private final String autor;       // Inmutable: no cambia
    private boolean disponible;       // Mutable: cambia con préstamos
    
    // Constructor: garantiza estado inicial válido
    public Libro(String isbn, String titulo, String autor) {
        // Validaciones (ver {ref}`construccion-de-objetos`)
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN requerido");
        }
        if (titulo == null || titulo.isEmpty()) {
            throw new IllegalArgumentException("Título requerido");
        }
        
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = true;  // Los libros nacen disponibles
    }
    
    // Constructor sobrecargado: autor desconocido
    public Libro(String isbn, String titulo) {
        this(isbn, titulo, "Autor desconocido");
    }
    
    // Getters para atributos
    public String getIsbn() {
        return isbn;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public String getAutor() {
        return autor;
    }
    
    public boolean isDisponible() {
        return disponible;
    }
    
    // Métodos de comportamiento (no simples setters)
    public boolean prestar() {
        if (disponible) {
            disponible = false;
            return true;
        }
        return false;  // Ya estaba prestado
    }
    
    public void devolver() {
        disponible = true;
    }
    
    @Override
    public String toString() {
        String estado = disponible ? "Disponible" : "Prestado";
        return titulo + " (" + autor + ") [" + estado + "]";
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// CLASE SOCIO
// Representa un socio de la biblioteca.
// Demuestra: agregación (tiene libros prestados), validaciones
// ═══════════════════════════════════════════════════════════════════════════
public class Socio {
    private final int numeroSocio;
    private String nombre;
    private String email;
    private List<Libro> librosPrestados;  // AGREGACIÓN con Libro
    private static final int MAX_LIBROS = 5;
    
    public Socio(int numeroSocio, String nombre, String email) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("Nombre requerido");
        }
        
        this.numeroSocio = numeroSocio;
        this.nombre = nombre;
        this.email = email;
        this.librosPrestados = new ArrayList<>();
    }
    
    // Constructor sobrecargado: sin email
    public Socio(int numeroSocio, String nombre) {
        this(numeroSocio, nombre, null);
    }
    
    // Getters
    public int getNumeroSocio() {
        return numeroSocio;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public int cantidadLibrosPrestados() {
        return librosPrestados.size();
    }
    
    public boolean puedeTomarPrestado() {
        return librosPrestados.size() < MAX_LIBROS;
    }
    
    // Comportamiento: tomar un libro prestado
    // El libro existe EXTERNAMENTE (agregación)
    public boolean tomarPrestado(Libro libro) {
        if (!puedeTomarPrestado()) {
            System.out.println(nombre + " ya tiene " + MAX_LIBROS + " libros");
            return false;
        }
        
        if (libro.prestar()) {
            librosPrestados.add(libro);
            System.out.println(nombre + " tomó prestado: " + libro.getTitulo());
            return true;
        } else {
            System.out.println("El libro no está disponible");
            return false;
        }
    }
    
    // Comportamiento: devolver un libro
    public boolean devolverLibro(Libro libro) {
        if (librosPrestados.remove(libro)) {
            libro.devolver();
            System.out.println(nombre + " devolvió: " + libro.getTitulo());
            return true;
        }
        return false;  // No tenía ese libro
    }
    
    // Devolver todos los libros
    public void devolverTodos() {
        for (Libro libro : librosPrestados) {
            libro.devolver();
        }
        librosPrestados.clear();
        System.out.println(nombre + " devolvió todos los libros");
    }
    
    public List<Libro> getLibrosPrestados() {
        // Retornar copia defensiva
        return new ArrayList<>(librosPrestados);
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// CLASE ESTANTERIA
// Representa una estantería física que contiene libros.
// Demuestra: composición (crea sus propios libros)
// ═══════════════════════════════════════════════════════════════════════════
public class Estanteria {
    private final int numero;
    private final String seccion;
    private List<Libro> libros;  // COMPOSICIÓN: la estantería crea sus libros
    
    public Estanteria(int numero, String seccion) {
        this.numero = numero;
        this.seccion = seccion;
        this.libros = new ArrayList<>();
    }
    
    public int getNumero() {
        return numero;
    }
    
    public String getSeccion() {
        return seccion;
    }
    
    // COMPOSICIÓN: la estantería CREA el libro internamente
    public Libro agregarLibro(String isbn, String titulo, String autor) {
        Libro nuevoLibro = new Libro(isbn, titulo, autor);
        libros.add(nuevoLibro);
        return nuevoLibro;  // Retorna referencia para poder prestarlo
    }
    
    // Sobrecarga: agregar sin autor
    public Libro agregarLibro(String isbn, String titulo) {
        return agregarLibro(isbn, titulo, "Autor desconocido");
    }
    
    public int cantidadLibros() {
        return libros.size();
    }
    
    public int cantidadDisponibles() {
        int count = 0;
        for (Libro libro : libros) {
            if (libro.isDisponible()) {
                count++;
            }
        }
        return count;
    }
    
    public void listarLibros() {
        System.out.println("═══ Estantería " + numero + " (" + seccion + ") ═══");
        for (Libro libro : libros) {
            System.out.println("  • " + libro);
        }
    }
    
    public Libro buscarPorTitulo(String titulo) {
        for (Libro libro : libros) {
            if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                return libro;
            }
        }
        return null;
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// PROGRAMA PRINCIPAL: DEMOSTRACIÓN
// ═══════════════════════════════════════════════════════════════════════════
public class SistemaBiblioteca {
    public static void main(String[] args) {
        // Crear estanterías (las estanterías CREAN sus libros: composición)
        Estanteria ficcion = new Estanteria(1, "Ficción");
        Estanteria ciencia = new Estanteria(2, "Ciencia");
        
        // Agregar libros (composición: la estantería los crea)
        Libro libro1 = ficcion.agregarLibro("978-1", "Cien años de soledad", "García Márquez");
        Libro libro2 = ficcion.agregarLibro("978-2", "1984", "George Orwell");
        Libro libro3 = ciencia.agregarLibro("978-3", "Cosmos", "Carl Sagan");
        
        // Crear socios (existen independientemente de los libros)
        Socio maria = new Socio(1001, "María López", "maria@email.com");
        Socio juan = new Socio(1002, "Juan Pérez");  // Constructor sobrecargado
        
        // Mostrar estado inicial
        ficcion.listarLibros();
        ciencia.listarLibros();
        
        System.out.println("\n─── Préstamos ───");
        
        // María toma prestados libros (agregación: recibe libros externos)
        maria.tomarPrestado(libro1);  // OK
        maria.tomarPrestado(libro3);  // OK
        
        // Juan intenta el mismo libro
        juan.tomarPrestado(libro1);   // Falla: ya prestado
        juan.tomarPrestado(libro2);   // OK
        
        System.out.println("\n─── Estado después de préstamos ───");
        ficcion.listarLibros();
        ciencia.listarLibros();
        
        System.out.println("\nLibros de María: " + maria.cantidadLibrosPrestados());
        System.out.println("Libros de Juan: " + juan.cantidadLibrosPrestados());
        
        System.out.println("\n─── Devoluciones ───");
        maria.devolverLibro(libro1);
        
        System.out.println("\n─── Estado final ───");
        ficcion.listarLibros();
    }
}
```

**Salida del programa:**

```
═══ Estantería 1 (Ficción) ═══
  • Cien años de soledad (García Márquez) [Disponible]
  • 1984 (George Orwell) [Disponible]
═══ Estantería 2 (Ciencia) ═══
  • Cosmos (Carl Sagan) [Disponible]

─── Préstamos ───
María López tomó prestado: Cien años de soledad
María López tomó prestado: Cosmos
El libro no está disponible
Juan Pérez tomó prestado: 1984

─── Estado después de préstamos ───
═══ Estantería 1 (Ficción) ═══
  • Cien años de soledad (García Márquez) [Prestado]
  • 1984 (George Orwell) [Prestado]
═══ Estantería 2 (Ciencia) ═══
  • Cosmos (Carl Sagan) [Prestado]

Libros de María: 2
Libros de Juan: 1

─── Devoluciones ───
María López devolvió: Cien años de soledad

─── Estado final ───
═══ Estantería 1 (Ficción) ═══
  • Cien años de soledad (García Márquez) [Disponible]
  • 1984 (George Orwell) [Prestado]
```

(del-diagrama-al-codigo)=
### Del Diagrama al Código: Proceso Sistemático

El proceso completo de transformación desde el análisis hasta el código funcional sigue estos pasos:

(paso1-analisis)=
#### Paso 1: Análisis (Capítulo Anterior)

Usando las {ref}`la-heuristica-linguistica`, identificamos:
- **Sustantivos → Clases candidatas:** Biblioteca, Libro, Socio, Estantería, Préstamo
- **Verbos → Métodos candidatos:** prestar, devolver, buscar, listar
- Aplicamos {ref}`filtro-de-abstraccion` para eliminar lo irrelevante
- Aplicamos {ref}`filtro-de-complejidad` para decidir qué es clase y qué es atributo

(paso2-diseno)=
#### Paso 2: Diseño del Diagrama de Clases

Dibujamos el diagrama UML identificando:
- Atributos de cada clase
- Métodos de cada clase
- Relaciones (asociación, composición ♦, agregación ◇)
- Cardinalidades

```{figure} 03/uml_biblioteca_integracion.svg
:label: fig-uml-biblioteca-completo
:align: center
:width: 100%

Diagrama de clases completo para el sistema de biblioteca, integrando composición y agregación.
```

(paso3-implementacion)=
#### Paso 3: Implementación en Java

Para cada clase del diagrama:

1. **Declarar la clase:** `public class NombreClase { }`

2. **Definir atributos privados:**
   ```java
   private tipo nombreAtributo;
   ```

3. **Crear constructor(es):**
   - Validar parámetros
   - Inicializar todos los atributos
   - Considerar sobrecarga para diferentes formas de construcción

4. **Implementar métodos públicos:**
   - Getters para atributos que se necesiten leer
   - Métodos de comportamiento (no solo setters)
   - Usar `this` para referencias internas

5. **Establecer relaciones:**
   - **Composición:** Crear objetos internos en el constructor
   - **Agregación:** Recibir objetos como parámetros

:::{tip}
**Orden recomendado de implementación:**

1. Empezar por las clases más **simples** (sin dependencias de otras clases del dominio)
2. Luego implementar las clases que **dependen** de las anteriores
3. Probar cada clase individualmente antes de integrar

En el ejemplo de la biblioteca:
1. Primero: `Libro` (no depende de otras clases del dominio)
2. Luego: `Socio` (usa `Libro` pero no lo crea)
3. Luego: `Estanteria` (crea `Libro`s)
4. Finalmente: Programa de prueba que integra todo
:::

---

(resumen-oop2)=
## Resumen

Este capítulo cubrió la transición desde el diseño conceptual (las "burbujas" del diagrama de clases) hacia código funcional en Java. Los conceptos clave son:

(resumen-sintaxis)=
### Sintaxis de Clases en Java

- Una **clase** se declara con `public class NombreClase { }`
- Los **atributos** almacenan el estado ({ref}`concepto-estado`)
- El **constructor** inicializa el objeto garantizando un estado válido ({ref}`construccion-de-objetos`)
- Los **métodos** implementan el comportamiento ({ref}`concepto-metodo`)
- La palabra clave **`this`** es una referencia al objeto actual

(resumen-encapsulamiento-impl)=
### Encapsulamiento en Práctica

- Implementa el principio descrito en la sección de encapsulamiento en el capítulo anterior
- **Atributos `private`:** Protegen el estado interno
- **Métodos `public`:** Exponen la interfaz controlada
- **Comportamiento con nombre:** Preferir métodos con semántica de dominio sobre getters/setters genéricos
- Los **modificadores de acceso** (`private`, `public`, `protected`) controlan la visibilidad

(resumen-nomenclatura)=
### Convenciones de Nomenclatura

| Elemento | Convención | Ejemplo |
|:---------|:-----------|:--------|
| Clase | `PascalCase` | `CuentaBancaria` |
| Atributo singular | `camelCase` | `saldoActual` |
| Atributo colección | `camelCase` **plural** | `libros`, `socios` |
| Método | `camelCase` (verbo) | `depositar()`, `agregarLibro()` |
| Constante | `SCREAMING_SNAKE_CASE` | `MAX_INTENTOS` |

(resumen-sobrecarga)=
### Sobrecarga de Métodos

- Permite **múltiples versiones** de un método con el mismo nombre
- Se diferencian por la **lista de parámetros** (cantidad o tipos)
- Común en **constructores** para ofrecer diferentes formas de crear objetos
- Proporciona **flexibilidad** sin cambiar la interfaz conceptual
- No confundir con **sobreescritura** (ver la sección de sobreescritura)

(resumen-relaciones)=
### Relaciones entre Clases

| Tipo | Símbolo UML | Ciclo de vida | Implementación |
|:-----|:-----------:|:--------------|:---------------|
| **Asociación** | ─── | Independiente | Atributo referencia |
| **Composición** | ♦ | Dependiente (parte del todo) | `new` en constructor |
| **Agregación** | ◇ | Independiente | Recibido como parámetro |

(resumen-cardinalidad)=
### Cardinalidad

- Indica **cuántos objetos** participan en cada lado de la relación
- Notaciones: `1`, `0..1`, `1..*`, `0..*` (o `*`), `n..m`
- Se implementa con referencias simples (1) o colecciones (*)

(resumen-conexion)=
### Conexión con Conceptos Previos

Este capítulo materializa los conceptos abstractos del capítulo {ref}`fundamentos-de-la-programacion-orientada-a-objetos`:

| Concepto abstracto | Implementación Java |
|:-------------------|:--------------------|
| {ref}`concepto-clase` | `public class NombreClase { }` |
| {ref}`concepto-objeto` | `new NombreClase(args)` |
| {ref}`concepto-atributo` | `private tipo nombre;` |
| {ref}`concepto-metodo` | `public tipoRetorno nombre(params) { }` |
| {ref}`construccion-de-objetos` | Constructor |
| la sección de encapsulamiento en el capítulo anterior | Modificadores de acceso |
| {ref}`concepto-mensaje` | Invocación de métodos |
| {ref}`relaciones-entre-clases` | Composición y agregación |

---

(proximos-pasos-oop2)=
## Próximos Pasos

Con los fundamentos de sintaxis, encapsulamiento y asociaciones establecidos, la siguiente unidad abordará conceptos más avanzados del paradigma:

- **Herencia:** Crear jerarquías de clases donde las subclases extienden a las superclases (ver {ref}`java-herencia-polimorfismo`)
- **Polimorfismo:** Permitir que diferentes objetos respondan al mismo mensaje de formas distintas (ver {ref}`polimorfismo-concepto`)
- **Interfaces y clases abstractas:** Definir contratos y comportamientos compartidos (ver {ref}`clases-abstractas-e-interfaces`)

Estos conceptos permitirán crear diseños más flexibles y aprovechar al máximo la reutilización de código que ofrece el paradigma orientado a objetos.

:::{important}
**Transición al TP6: Clases y Objetos 1**

El Trabajo Práctico 6 consiste en tomar el diagrama de clases producido en el TP5 (resultado del análisis usando {ref}`la-heuristica-linguistica`) y transformarlo en código Java funcional, aplicando todos los conceptos vistos:

1. Declarar las clases con sintaxis correcta
2. Definir atributos privados (encapsulamiento)
3. Crear constructores con validaciones
4. Implementar métodos de comportamiento
5. Establecer relaciones (composición vs agregación)
6. Aplicar sobrecarga donde corresponda

**Actividad sugerida:** Tomá el diagrama de clases del juego "Cazadores y Auditores" de la clase anterior y escribí el código completo de una de las clases identificadas.
:::

---

(ejercicios-oop2)=
## Ejercicios

```{exercise}
:label: ej-sintaxis-clase

Dado el siguiente fragmento de código, identificá y corregí todos los errores de sintaxis:

```java
public Class Producto {
    String nombre
    private precio double;
    
    public Producto(nombre, double precio)
        nombre = nombre;
        this.precio = precio
    }
    
    void String getNombre() {
        return nombre
    }
}
```
```

```{solution} ej-sintaxis-clase
:class: dropdown

Errores identificados y correcciones:

1. `Class` → `class` (minúscula)
2. `String nombre` → `private String nombre;` (falta modificador y punto y coma)
3. `private precio double;` → `private double precio;` (orden incorrecto)
4. `(nombre, double precio)` → `(String nombre, double precio)` (falta tipo)
5. Falta `{` después del constructor
6. `nombre = nombre;` → `this.nombre = nombre;` (ambigüedad)
7. Falta `;` después de `this.precio = precio`
8. `void String getNombre()` → `public String getNombre()` (conflicto de tipos)
9. Falta `;` después de `return nombre`

**Código corregido:**

```java
public class Producto {
    private String nombre;
    private double precio;
    
    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
    
    public String getNombre() {
        return nombre;
    }
}
```
```

```{exercise}
:label: ej-encapsulamiento

Refactorizá la siguiente clase para aplicar correctamente el encapsulamiento:

```java
public class Empleado {
    public String nombre;
    public double salario;
    public int horasTrabajadas;
    
    public double calcularSueldo() {
        return salario * horasTrabajadas;
    }
}
```

Agregá:
1. Modificadores de acceso apropiados
2. Constructor con validaciones
3. Getters necesarios
4. Setter para `horasTrabajadas` con validación (0-300 horas)
```

````{solution} ej-encapsulamiento
:class: dropdown

```java
public class Empleado {
    private String nombre;
    private double salarioPorHora;
    private int horasTrabajadas;
    
    public Empleado(String nombre, double salarioPorHora) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("Nombre requerido");
        }
        if (salarioPorHora <= 0) {
            throw new IllegalArgumentException("Salario debe ser positivo");
        }
        
        this.nombre = nombre;
        this.salarioPorHora = salarioPorHora;
        this.horasTrabajadas = 0;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public double getSalarioPorHora() {
        return salarioPorHora;
    }
    
    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }
    
    public void setHorasTrabajadas(int horas) {
        if (horas >= 0 && horas <= 300) {
            this.horasTrabajadas = horas;
        } else {
            throw new IllegalArgumentException(
                "Horas deben estar entre 0 y 300"
            );
        }
    }
    
    public double calcularSueldo() {
        return salarioPorHora * horasTrabajadas;
    }
}
```
````

```{exercise}
:label: ej-composicion-agregacion

Para cada uno de los siguientes escenarios, indicá si corresponde usar **Composición** o **Agregación**, y justificá tu respuesta:

a) Una `Factura` contiene `Items` (líneas de detalle).

b) Un `Equipo` tiene `Jugadores`.

c) Un `Pedido` tiene una `DireccionEntrega`.

d) Una `Universidad` tiene `Profesores`.

e) Un `Cuerpo` tiene `Organos`.
```

````{solution} ej-composicion-agregacion
:class: dropdown

**a) Factura - Items: COMPOSICIÓN ♦**
- Los items son parte esencial de la factura
- No tienen sentido sin ella
- Si se elimina la factura, los items también
- La factura crea sus propios items

**b) Equipo - Jugadores: AGREGACIÓN ◇**
- Un jugador existe independientemente del equipo
- Puede cambiar de equipo (transferencias)
- Si el equipo desaparece, los jugadores siguen existiendo
- El equipo recibe jugadores que ya existen

**c) Pedido - DireccionEntrega: COMPOSICIÓN ♦**
- La dirección es creada específicamente para ese pedido
- Aunque físicamente la dirección exista, la instancia del objeto es del pedido
- Cambiar la dirección original no afecta el pedido histórico
- El pedido crea su copia de la dirección

**d) Universidad - Profesores: AGREGACIÓN ◇**
- Los profesores existen como personas antes de pertenecer a la universidad
- Pueden trabajar en múltiples universidades
- Si la universidad cierra, los profesores siguen existiendo
- La universidad recibe profesores existentes

**e) Cuerpo - Órganos: COMPOSICIÓN ♦**
- Los órganos son partes esenciales del cuerpo
- No funcionan independientemente del cuerpo
- Si el cuerpo muere, los órganos dejan de funcionar
- El cuerpo "crea" sus órganos (biológicamente)
````

```{exercise}
:label: ej-sobrecarga-2

Implementá una clase `Mensaje` con constructores sobrecargados que permitan crear mensajes de las siguientes formas:

1. Solo con texto
2. Con texto y destinatario
3. Con texto, destinatario y asunto
4. Con texto, destinatario, asunto y prioridad (alta/media/baja)

Usá delegación de constructores para evitar duplicación de código.
```

````{solution} ej-sobrecarga-2
:class: dropdown

```java
public class Mensaje {
    private String texto;
    private String destinatario;
    private String asunto;
    private String prioridad;
    
    // Constructor principal (más completo)
    public Mensaje(String texto, String destinatario, 
                   String asunto, String prioridad) {
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException("Texto requerido");
        }
        
        this.texto = texto;
        this.destinatario = destinatario;
        this.asunto = asunto;
        
        // Validar prioridad
        if (prioridad.equals("alta") || 
            prioridad.equals("media") || 
            prioridad.equals("baja")) {
            this.prioridad = prioridad;
        } else {
            this.prioridad = "media";  // valor por defecto
        }
    }
    
    // Con texto, destinatario y asunto (prioridad media por defecto)
    public Mensaje(String texto, String destinatario, String asunto) {
        this(texto, destinatario, asunto, "media");
    }
    
    // Con texto y destinatario (sin asunto)
    public Mensaje(String texto, String destinatario) {
        this(texto, destinatario, "(Sin asunto)", "media");
    }
    
    // Solo con texto
    public Mensaje(String texto) {
        this(texto, "Sin destinatario", "(Sin asunto)", "media");
    }
    
    // Getters
    public String getTexto() { return texto; }
    public String getDestinatario() { return destinatario; }
    public String getAsunto() { return asunto; }
    public String getPrioridad() { return prioridad; }
}

// Uso:
Mensaje m1 = new Mensaje("Hola");
Mensaje m2 = new Mensaje("Hola", "juan@mail.com");
Mensaje m3 = new Mensaje("Hola", "juan@mail.com", "Saludo");
Mensaje m4 = new Mensaje("URGENTE", "juan@mail.com", "Alerta", "alta");
```
````
