---
title: 0x2 - Diseño de Clases y POO
---

# Serie 0x2 - Diseño de Clases y POO

(regla-0x2000)=
## `0x2000` - Las clases van en `CamelloCase`

:::{seealso}
Esta regla es idéntica a {ref}`regla-0x0001`. Consultá esa sección para la explicación completa.
:::

Ejemplo: `CalculadoraAvanzada`, `GestorDeUsuarios`

(regla-0x2001)=
## `0x2001` - Los atributos van en `dromedarioCase` y deben ser `private`

### Explicación

Los atributos (variables de instancia) de una clase deben nombrarse en `dromedarioCase` y declararse con visibilidad `private`. El acceso externo debe realizarse exclusivamente a través de métodos públicos (getters/setters). Este es el principio fundamental del **encapsulamiento** en POO.

### Justificación

1. **Encapsulamiento**: Oculta detalles de implementación y protege invariantes.
2. **Control**: Permite validación y lógica adicional en acceso/modificación.
3. **Flexibilidad**: Facilita cambiar implementación sin afectar clientes.
4. **Mantenibilidad**: Centraliza lógica de acceso a datos.
5. **Debugging**: Facilita colocar breakpoints en accesos a atributos.
6. **Thread-safety**: Permite agregar sincronización si es necesario.

### Visibilidad de atributos

| Modificador | Uso | ¿Cuándo? |
|-------------|-----|----------|
| `private` | ✅ Default | Siempre, salvo excepción justificada |
| `protected` | ⚠️ Con justificación | Solo para herencia planificada |
| `package-private` | ❌ Evitar | Casi nunca |
| `public` | ❌ **Prohibido** | **Nunca** |

### Ejemplos

#### Incorrecto ❌

```java
// ❌ Atributos públicos - Rompe encapsulamiento
class Persona {
    public String nombre;
    public int edad;
    public double saldo;
}

// Cualquiera puede modificar sin validación
Persona p = new Persona();
p.edad = -5;  // ❌ Edad negativa!
p.saldo = -1000;  // ❌ Saldo negativo!
```

```java
// ❌ Atributos con visibilidad package-private (sin modificador)
class CuentaBancaria {
    String numeroCuenta;  // Accesible desde mismo paquete
    double saldo;         // Sin control de acceso
}
```

#### Correcto ✅

```java
/**
 * Representa una persona con datos personales.
 */
public class Persona {
    /**
     * Nombre completo de la persona.
     * INV: nombre != null && !nombre.isEmpty()
     */
    private String nombre;
    
    /**
     * Edad de la persona en años.
     * INV: edad >= 0 && edad <= 150
     */
    private int edad;
    
    /**
     * Saldo disponible de la persona.
     * INV: saldo >= 0
     */
    private double saldo;
    
    /**
     * Obtiene el nombre de la persona.
     * 
     * @return el nombre completo
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Establece el nombre de la persona.
     * 
     * @param nombre nuevo nombre (no puede ser null ni vacío)
     * @throws IllegalArgumentException si nombre es null o vacío
     */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede ser null o vacío");
        }
        this.nombre = nombre;
    }
    
    /**
     * Obtiene la edad actual.
     * 
     * @return edad en años
     */
    public int getEdad() {
        return edad;
    }
    
    /**
     * Establece la edad de la persona.
     * 
     * @param edad nueva edad (debe estar entre 0 y 150)
     * @throws IllegalArgumentException si edad está fuera de rango
     */
    public void setEdad(int edad) {
        if (edad < 0 || edad > 150) {
            throw new IllegalArgumentException("Edad inválida: " + edad);
        }
        this.edad = edad;
    }
}
```

### Ventajas del encapsulamiento

#### Validación centralizada

```java
public class CuentaBancaria {
    private double saldo;
    
    /**
     * Retira dinero de la cuenta.
     * 
     * @param monto cantidad a retirar
     * @throws SaldoInsuficienteException si no hay fondos
     */
    public void retirar(double monto) throws SaldoInsuficienteException {
        // ✅ Validación centralizada
        if (monto > saldo) {
            throw new SaldoInsuficienteException();
        }
        saldo -= monto;
    }
    
    // Si saldo fuera public, cada lugar que lo modifique
    // tendría que duplicar esta validación ❌
}
```

#### Cambio de implementación transparente

```java
// Versión 1: Almacenar edad directamente
public class Persona {
    private int edad;
    
    public int getEdad() {
        return edad;
    }
}

// Versión 2: Calcular edad desde fecha de nacimiento
public class Persona {
    private LocalDate fechaNacimiento;  // ✅ Cambio interno
    
    public int getEdad() {
        // ✅ Los clientes no necesitan cambiar código
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}
```

### Uso de `protected`

Solo usar `protected` cuando hay razón justificada:

```java
/**
 * Clase base para vehículos motorizados.
 */
public abstract class Vehiculo {
    /**
     * Velocidad máxima del vehículo en km/h.
     * Protected porque las subclases necesitan acceso directo
     * para cálculos de performance.
     */
    protected int velocidadMaxima;
    
    /**
     * Número de motor (privado, no necesita ser accedido por subclases).
     */
    private String numeroMotor;
}
```

:::{warning}
Usar `protected` rompe el encapsulamiento parcialmente. Solo hacelo cuando:
1. Las subclases **realmente necesitan** acceso directo
2. No hay forma razonable de proveer el acceso mediante métodos
3. Está documentado por qué es necesario
:::

### Constantes públicas: Excepción

Las constantes (`static final`) pueden ser públicas:

```java
public class Configuracion {
    // ✅ Constantes públicas son aceptables
    public static final int MAX_REINTENTOS = 3;
    public static final String VERSION = "1.0.0";
    
    // ✅ Atributos de instancia siguen siendo privados
    private String nombre;
    private boolean activo;
}
```

### Anti-patrones comunes

#### Atributos públicos "por comodidad"

```java
// ❌ Atributos públicos por pereza
public class Punto {
    public double x;  // "Es más fácil acceder directamente"
    public double y;
}

// Problema: No hay validación
Punto p = new Punto();
p.x = Double.NaN;  // ❌ Valor inválido sin control

// ✅ Encapsulamiento apropiado
public class Punto {
    private double x;
    private double y;
    
    public void setX(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("Coordenada inválida");
        }
        this.x = x;
    }
}
```

#### DTOs y clases de datos

Incluso en DTOs (Data Transfer Objects), preferir private con getters/setters:

```java
// ⚠️ Común pero no ideal
public class PersonaDTO {
    public String nombre;
    public int edad;
}

// ✅ Mejor práctica
public class PersonaDTO {
    private String nombre;
    private int edad;
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
}

// ✅ O usar records de Java 14+ (inmutable por defecto)
public record PersonaDTO(String nombre, int edad) {
    // Getters automáticos, atributos privados finales
}
```

### Nomenclatura: Ver regla dromedarioCase

Para la convención de nomenclatura `dromedarioCase`, consultá {ref}`regla-0x0003`.

:::{important}
**Regla de oro del encapsulamiento**: Si un atributo no es `private`, debe haber una justificación técnica documentada en el código.
:::

(regla-0x2002)=
## `0x2002` - La inicialización de los atributos va en el constructor

### Explicación

Los atributos deben inicializarse en el constructor, no en el punto de declaración (excepto constantes y valores por defecto simples). Esto centraliza la lógica de inicialización y permite diferentes estados iniciales según los parámetros del constructor.

### Justificación

1. **Inicialización parametrizada**: Permite inicializar según parámetros del constructor.
2. **Claridad**: Toda la lógica de inicialización está en un solo lugar.
3. **Flexibilidad**: Facilita tener múltiples constructores con diferentes inicializaciones.
4. **Invariantes**: El constructor es el lugar natural para establecer invariantes.
5. **Validación**: Permite validar valores antes de asignar.

### Ejemplos

#### Incorrecto ❌

```java
class Persona {
    private String nombre = "";  // ❌ Inicialización en declaración
    private int edad = 0;        // ❌ Valor hardcoded
    private List<String> hobbies = new ArrayList<>();  // ❌ En declaración
}
```

#### Correcto ✅

```java
class Persona {
    private String nombre;
    private int edad;
    private List<String> hobbies;
    
    /**
     * Crea una persona con datos iniciales.
     * 
     * @param nombre nombre completo (no null, no vacío)
     * @param edad edad en años (debe ser >= 0)
     */
    public Persona(String nombre, int edad) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("Nombre inválido");
        }
        if (edad < 0) {
            throw new IllegalArgumentException("Edad no puede ser negativa");
        }
        
        this.nombre = nombre;
        this.edad = edad;
        this.hobbies = new ArrayList<>();  // ✅ Inicialización en constructor
    }
}
```

### Excepciones: Cuándo inicializar en declaración

#### Constantes de clase

```java
public class Configuracion {
    // ✅ Constantes pueden inicializarse en declaración
    private static final int MAX_CONEXIONES = 100;
    private static final String VERSION = "1.0.0";
    private static final double PI = 3.14159;
}
```

#### Valores por defecto simples

```java
public class Contador {
    // ✅ Valor por defecto simple y obvio
    private int contador = 0;
    
    // ✅ Booleano con valor por defecto claro
    private boolean activo = false;
    
    // ⚠️ Pero si el valor depende del contexto, usar constructor
    private String nombre;  // Depende del constructor
    
    public Contador(String nombre) {
        this.nombre = nombre;
    }
}
```

#### Inicialización estática

```java
public class Repositorio {
    // ✅ Colecciones compartidas entre instancias
    private static final List<String> VALORES_VALIDOS = 
        Arrays.asList("A", "B", "C");
    
    // ❌ Colección de instancia no debe inicializarse aquí
    private List<String> elementos = new ArrayList<>();  // Mover al constructor
}
```

### Múltiples constructores

La inicialización en constructor facilita tener diferentes formas de crear objetos:

```java
public class CuentaBancaria {
    private String numeroCuenta;
    private double saldo;
    private String titular;
    
    /**
     * Crea cuenta nueva con saldo cero.
     */
    public CuentaBancaria(String numeroCuenta, String titular) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = 0.0;  // Saldo inicial cero
    }
    
    /**
     * Crea cuenta con saldo inicial.
     */
    public CuentaBancaria(String numeroCuenta, String titular, double saldoInicial) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.saldo = saldoInicial;  // Saldo parametrizado
    }
}
```

### Delegación entre constructores

```java
public class Producto {
    private String nombre;
    private double precio;
    private String categoria;
    
    /**
     * Constructor completo.
     */
    public Producto(String nombre, double precio, String categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }
    
    /**
     * Constructor con categoría por defecto.
     */
    public Producto(String nombre, double precio) {
        this(nombre, precio, "General");  // ✅ Delega al constructor principal
    }
}
```

### Inicialización compleja

Para inicializaciones complejas, usar bloques de inicialización o métodos privados:

```java
public class Configuracion {
    private Map<String, String> propiedades;
    private List<Validador> validadores;
    
    public Configuracion() {
        this.propiedades = inicializarPropiedades();
        this.validadores = crearValidadores();
    }
    
    /**
     * Inicializa las propiedades con valores por defecto.
     */
    private Map<String, String> inicializarPropiedades() {
        Map<String, String> props = new HashMap<>();
        props.put("timeout", "30");
        props.put("max-conexiones", "100");
        return props;
    }
    
    /**
     * Crea la lista de validadores necesarios.
     */
    private List<Validador> crearValidadores() {
        List<Validador> vals = new ArrayList<>();
        vals.add(new ValidadorEmail());
        vals.add(new ValidadorTelefono());
        return vals;
    }
}
```

:::{tip}
Si la inicialización de un atributo es compleja (más de 2-3 líneas), extraé la lógica a un método privado llamado desde el constructor. Esto mantiene el constructor limpio y facilita testing.
:::

(regla-0x2003)=
## `0x2003` - Los paquetes deben comenzar en `ar.unrn` e ir en minúsculas

### Explicación

Los paquetes deben seguir la convención de Java: todo en minúsculas, sin guiones bajos ni caracteres especiales. Para este curso, todos los paquetes deben comenzar con `ar.unrn` (dominio de la universidad).

### Justificación

1. **Estándar Java**: Convención universal del lenguaje.
2. **Evita conflictos**: Nombres en minúsculas no colisionan con clases.
3. **Organización**: Estructura jerárquica clara del proyecto.
4. **Identificación**: `ar.unrn` identifica el origen del código.
5. **Escalabilidad**: Facilita agregar subpaquetes organizados.

### Convención de nomenclatura

```
ar.unrn.[modulo].[submodulo]
```

Ejemplos correctos:
- `ar.unrn.dominio`
- `ar.unrn.persistencia`
- `ar.unrn.util`
- `ar.unrn.poo.calculadora`
- `ar.unrn.poo.agenda`

### Ejemplos

#### Incorrecto ❌

```java
// ❌ Sin dominio institucional
package calculadora;
package Dominio;  // Mayúsculas
package mi_proyecto;  // Guión bajo
package ar.UNRN.dominio;  // Mayúsculas

// ❌ Nombres genéricos sin jerarquía
package utils;
package helpers;
```

#### Correcto ✅

```java
// ✅ Con dominio institucional y minúsculas
package ar.unrn.poo.calculadora;
package ar.unrn.dominio.modelo;
package ar.unrn.persistencia.dao;
package ar.unrn.util.validadores;
```

### Estructura típica de paquetes

```
ar.unrn.poo.tpX/
├── dominio/           # Clases del modelo de dominio
├── excepcion/         # Excepciones personalizadas
├── persistencia/      # Acceso a datos
├── servicio/          # Lógica de negocio
└── util/              # Utilidades generales
```

Ejemplo completo:

```java
package ar.unrn.poo.tp5.dominio;

public class Producto {
    // ...
}
```

```java
package ar.unrn.poo.tp5.excepcion;

public class ProductoNoEncontradoException extends Exception {
    // ...
}
```

:::{tip}
Organizá tu proyecto por **capas** o **módulos funcionales**, no por tipo de archivo (no hagas un paquete "interfaces" y otro "implementaciones").
:::

### Convenciones Java estándar

Elementos de un nombre de paquete:

1. **Todo en minúsculas**: `ar.unrn`, nunca `Ar.Unrn`
2. **Sin guiones**: `ar.unrn.util`, no `ar.unrn.util_helpers`
3. **Sin números al inicio**: `ar.unrn.version1`, no `ar.unrn.1version`
4. **Palabras completas**: `ar.unrn.utilidades`, no `ar.unrn.util`

:::{note}
La única excepción común son abreviaturas universalmente reconocidas: `util`, `dao`, `dto`.
:::

(regla-0x2004)=
## `0x2004` - Implementar `equals` requiere implementar `hashCode`

### Explicación

Este es el **contrato fundamental de `Object`** en Java: si sobreescribís `equals()`, **debés** sobreescribir `hashCode()`. Si dos objetos son iguales según `equals()`, deben tener el mismo valor de `hashCode()`.

### Justificación

1. **Contrato de Object**: Requerimiento del lenguaje Java.
2. **Colecciones basadas en hash**: `HashMap`, `HashSet` no funcionan correctamente sin esto.
3. **Consistencia**: Garantiza comportamiento predecible.
4. **Debugging**: Evita bugs sutiles y difíciles de rastrear.

### El contrato

```
Si a.equals(b) == true, entonces a.hashCode() == b.hashCode()
```

:::{warning}
Lo inverso **no** es necesariamente cierto: dos objetos pueden tener el mismo hashCode sin ser iguales (colisión de hash).
:::

### Problema sin hashCode

```java
// ❌ Solo implementa equals, NO hashCode
public class Persona {
    private String nombre;
    private int edad;
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Persona)) return false;
        Persona otra = (Persona) obj;
        return this.nombre.equals(otra.nombre) && this.edad == otra.edad;
    }
    
    // ❌ FALTA hashCode()
}

// Resultado: HashSet no funciona correctamente
Set<Persona> personas = new HashSet<>();
Persona p1 = new Persona("Juan", 25);
Persona p2 = new Persona("Juan", 25);

personas.add(p1);
personas.add(p2);

// ❌ Esperado: size() == 1 (son iguales según equals)
// ❌ Real: size() == 2 (diferentes hashCode!)
System.out.println(personas.size());  // Imprime 2 ❌
```

### Implementación correcta

```java
// ✅ Implementa AMBOS métodos
public class Persona {
    private String nombre;
    private int edad;
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Persona otra)) return false;
        return this.nombre.equals(otra.nombre) && this.edad == otra.edad;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nombre, edad);  // ✅ Usa mismos campos que equals
    }
}

// ✅ Ahora HashSet funciona correctamente
Set<Persona> personas = new HashSet<>();
Persona p1 = new Persona("Juan", 25);
Persona p2 = new Persona("Juan", 25);

personas.add(p1);
personas.add(p2);

System.out.println(personas.size());  // Imprime 1 ✅
```

Ver regla {ref}`regla-0x2010` para más detalles sobre la implementación de `hashCode()`.

### Referencias

Ver:
- [Object.hashCode()](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html))
- [Object.equals()](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html))

(regla-0x2005)=
## `0x2005` - `equals` y `hashCode` deben ser implementados juntos o no estar

### Explicación

Esta regla refuerza {ref}`regla-0x2004`: ambos métodos deben implementarse **juntos** o **ninguno** debe estar. No implementar solo uno.

### Casos válidos

| `equals()` | `hashCode()` | ¿Válido? |
|------------|--------------|----------|
| ❌ No implementado | ❌ No implementado | ✅ SÍ - Usa implementación de Object |
| ✅ Implementado | ✅ Implementado | ✅ SÍ - Contrato respetado |
| ✅ Implementado | ❌ No implementado | ❌ **NO** - Rompe contrato |
| ❌ No implementado | ✅ Implementado | ❌ **NO** - Sin sentido |

### Generación automática con IDE

Los IDEs pueden generar ambos métodos automáticamente:

- **IntelliJ IDEA**: `Alt + Insert` → "equals() and hashCode()"
- **Eclipse**: `Source` → "Generate hashCode() and equals()"

:::{tip}
**Siempre** generá ambos métodos juntos usando el IDE. Esto garantiza que el contrato se respeta correctamente.
:::

### Referencias

- [`Object.hashCode()`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html))
- [`Object.equals(Object)`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html))

(regla-0x2006)=
## `0x2006` - Al extender, sobreescribir solo para llamar a super no es correcto

### Explicación

No sobreescribir un método únicamente para llamar a su implementación en la superclase. Si no agregás lógica adicional, la sobreescritura es innecesaria y debe eliminarse.

### Justificación

1. **Código innecesario**: No agrega valor.
2. **Mantenibilidad**: Más código para mantener sin beneficio.
3. **Confusión**: Hace creer que hay lógica especial cuando no la hay.
4. **Rendimiento**: Llamada de método adicional sin propósito.

### Excepción

**Los constructores** son la excepción: es válido (y a veces necesario) tener un constructor que solo llama a `super()`.

### Ejemplos

#### Incorrecto ❌

```java
public class EmpleadoTemporal extends Empleado {
    
    // ❌ Override inútil - solo llama a super
    @Override
    public void calcularSalario() {
        super.calcularSalario();
    }
    
    // ❌ Override sin valor agregado
    @Override
    public String toString() {
        return super.toString();
    }
}
```

#### Correcto ✅

```java
public class EmpleadoTemporal extends Empleado {
    
    // ✅ No sobreescribe - usa implementación heredada
    // (método calcularSalario() disponible desde Empleado)
    
    // ✅ O sobreescribe agregando lógica
    @Override
    public void calcularSalario() {
        super.calcularSalario();
        aplicarBonificacionTemporal();  // ✅ Agrega lógica
    }
}
```

#### Excepción: Constructores ✅

```java
public class EmpleadoTemporal extends Empleado {
    
    // ✅ VÁLIDO - Constructores pueden solo llamar a super
    public EmpleadoTemporal(String nombre, double salario) {
        super(nombre, salario);  // Válido en constructores
    }
}
```

:::{note}
Para constructores es **normal** y **correcto** solo llamar a `super()`. Esta regla aplica solo a métodos regulares.
:::

(regla-0x2007)=
## `0x2007` - Minimizar el código duplicado

### Explicación

Seguir el principio **DRY** (Don't Repeat Yourself): si hay código repetido en múltiples lugares, extraerlo a un método privado, método estático, o clase utilitaria.

### Justificación

1. **Mantenibilidad**: Cambios en un solo lugar.
2. **Consistencia**: Un bug se arregla en un lugar, no en N.
3. **Legibilidad**: Métodos más cortos y claros.
4. **Reusabilidad**: Lógica puede usarse en nuevos contextos.
5. **Testing**: Más fácil probar un método que N fragmentos duplicados.

### Ejemplos

#### Código duplicado ❌

```java
public class ProcesadorPedidos {
    public void procesarPedidoNuevo(Pedido pedido) {
        // Validación duplicada
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido null");
        }
        if (pedido.getItems().isEmpty()) {
            throw new IllegalArgumentException("Pedido sin items");
        }
        if (pedido.getTotal() <= 0) {
            throw new IllegalArgumentException("Total inválido");
        }
        // Procesar...
    }
    
    public void procesarPedidoModificado(Pedido pedido) {
        // ❌ Misma validación duplicada
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido null");
        }
        if (pedido.getItems().isEmpty()) {
            throw new IllegalArgumentException("Pedido sin items");
        }
        if (pedido.getTotal() <= 0) {
            throw new IllegalArgumentException("Total inválido");
        }
        // Procesar...
    }
}
```

#### Código refactorizado ✅

```java
public class ProcesadorPedidos {
    
    /**
     * Valida que un pedido sea válido para procesamiento.
     * 
     * @param pedido pedido a validar
     * @throws IllegalArgumentException si el pedido es inválido
     */
    private void validarPedido(Pedido pedido) {
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido null");
        }
        if (pedido.getItems().isEmpty()) {
            throw new IllegalArgumentException("Pedido sin items");
        }
        if (pedido.getTotal() <= 0) {
            throw new IllegalArgumentException("Total inválido");
        }
    }
    
    public void procesarPedidoNuevo(Pedido pedido) {
        validarPedido(pedido);  // ✅ Reutiliza validación
        // Procesar...
    }
    
    public void procesarPedidoModificado(Pedido pedido) {
        validarPedido(pedido);  // ✅ Reutiliza validación
        // Procesar...
    }
}
```

### Caso específico: toString() en jerarquías

**Ejemplo importante** del TP7 - Calculadora: Revisar `toString()` en jerarquías de herencia para que se resuelva de manera general en la clase base, sin duplicar código en las clases específicas.

#### Incorrecto ❌

```java
public abstract class Operacion {
    protected double operando1;
    protected double operando2;
}

public class Suma extends Operacion {
    @Override
    public String toString() {
        // ❌ Lógica duplicada en cada subclase
        return operando1 + " + " + operando2 + " = " + calcular();
    }
}

public class Resta extends Operacion {
    @Override
    public String toString() {
        // ❌ Misma estructura, diferente operador
        return operando1 + " - " + operando2 + " = " + calcular();
    }
}
```

#### Correcto ✅

```java
public abstract class Operacion {
    protected double operando1;
    protected double operando2;
    
    /**
     * Retorna el símbolo de la operación (+, -, *, /).
     */
    protected abstract String obtenerSimbolo();
    
    /**
     * Calcula el resultado de la operación.
     */
    public abstract double calcular();
    
    @Override
    public String toString() {
        // ✅ Lógica general en clase base
        return operando1 + " " + obtenerSimbolo() + " " + 
               operando2 + " = " + calcular();
    }
}

public class Suma extends Operacion {
    @Override
    protected String obtenerSimbolo() {
        return "+";  // ✅ Solo lo específico
    }
    
    @Override
    public double calcular() {
        return operando1 + operando2;
    }
    
    // ✅ No sobreescribe toString() - usa el heredado
}

public class Resta extends Operacion {
    @Override
    protected String obtenerSimbolo() {
        return "-";  // ✅ Solo lo específico
    }
    
    @Override
    public double calcular() {
        return operando1 - operando2;
    }
}
```

:::{important}
En jerarquías de herencia, **subir** la lógica común a la clase base. Las subclases solo deben implementar lo que es **específico** de cada una.
:::

**Separación de responsabilidades** (TP7 - Calculadora):
- `toString()` NO debe calcular, solo mostrar la estructura de la operación
- `calcular()` realiza el cálculo numérico
- Llamar a `toString()` es redundante al concatenar (el compilador lo hace automático)
- Calcular subexpresiones una vez y guardar el resultado evita recalcular operaciones potencialmente costosas

(regla-0x2008)=
## `0x2008` - Los métodos get/set no pueden ser usados para la lógica del problema

### Explicación

Los getters y setters deben ser simples **accesores**: obtener o establecer un valor, con validación mínima. La lógica de negocio compleja debe ir en métodos con nombres semánticamente apropiados que describan la operación real.

### Justificación

1. **Principio de menor sorpresa**: Un setter no debería tener efectos secundarios complejos.
2. **Legibilidad**: `actualizarEdad()` es más claro que `setEdad()` si hace más que asignar.
3. **Testing**: Métodos simples son más fáciles de probar.
4. **Mantenibilidad**: Lógica compleja en métodos dedicados es más fácil de encontrar y modificar.
5. **Reutilización**: Lógica de negocio puede necesitarse sin cambiar el atributo.

### Qué puede hacer un setter

#### Setter aceptable ✅

```java
// ✅ Setter mínimo - Solo asigna
public void setNombre(String nombre) {
    this.nombre = nombre;
}

// ✅ Setter con validación simple - Aceptable
public void setEdad(int edad) {
    if (edad < 0 || edad > 150) {
        throw new IllegalArgumentException("Edad inválida: " + edad);
    }
    this.edad = edad;
}
```

#### Setter problemático ❌

```java
// ❌ Setter con lógica de negocio excesiva
public void setEdad(int edad) {
    if (edad < 0 || edad > 150) {
        throw new IllegalArgumentException();
    }
    this.edad = edad;
    calcularCategoria();           // ❌ Lógica de negocio
    actualizarEstadisticas();      // ❌ Efectos secundarios
    notificarObservadores();       // ❌ Notificaciones
    guardarEnBaseDatos();          // ❌ Persistencia
    enviarEmail();                 // ❌ Comunicación externa
}
```

### Refactoring: Extraer lógica

#### Antes ❌

```java
public class Empleado {
    private double salario;
    private CategoriaSalarial categoria;
    
    // ❌ Setter sobrecargado
    public void setSalario(double nuevoSalario) {
        if (nuevoSalario < 0) {
            throw new IllegalArgumentException("Salario negativo");
        }
        if (nuevoSalario < this.salario) {
            registrarReduccion(this.salario, nuevoSalario);
        }
        this.salario = nuevoSalario;
        recalcularBeneficios();
        actualizarCategoria();
        notificarRRHH();
    }
}
```

#### Después ✅

```java
public class Empleado {
    private double salario;
    private CategoriaSalarial categoria;
    
    // ✅ Setter simple - Solo validación y asignación
    private void setSalario(double salario) {
        if (salario < 0) {
            throw new IllegalArgumentException("Salario negativo");
        }
        this.salario = salario;
    }
    
    // ✅ Método de negocio con nombre apropiado
    /**
     * Actualiza el salario del empleado y ejecuta procesos asociados.
     * <p>
     * Registra cambios históricos, recalcula beneficios y notifica a RRHH.
     * 
     * @param nuevoSalario nuevo salario (debe ser >= 0)
     * @throws IllegalArgumentException si nuevoSalario < 0
     */
    public void actualizarSalario(double nuevoSalario) {
        if (nuevoSalario < this.salario) {
            registrarReduccion(this.salario, nuevoSalario);
        }
        setSalario(nuevoSalario);
        recalcularBeneficios();
        actualizarCategoria();
        notificarRRHH();
    }
}
```

### Getters con cálculo

Los getters tampoco deben contener lógica compleja de negocio:

```java
// ⚠️ Getter que hace cálculos costosos
public double getSalarioAnual() {
    // ❌ Múltiples cálculos y lógica compleja
    double base = salarioMensual * 12;
    double bonos = calcularBonos();
    double impuestos = calcularImpuestos(base + bonos);
    return base + bonos - impuestos;
}

// ✅ Método con nombre descriptivo
public double calcularSalarioAnualNeto() {
    double base = salarioMensual * MESES_POR_ANIO;
    double bonos = calcularBonos();
    double impuestos = calcularImpuestos(base + bonos);
    return base + bonos - impuestos;
}
```

### Regla de oro

**Si un getter/setter hace más que obtener/establecer + validación mínima, debería ser un método con nombre descriptivo.**

```
Setter simple:   set + sustantivo
Método negocio:  verbo + sustantivo
```

Ejemplos:
- `setNombre()` vs `actualizarNombre()`
- `setEstado()` vs `activar()`, `desactivar()`, `cambiarEstado()`
- `setSaldo()` vs `depositar()`, `retirar()`

(regla-0x2009)=
## `0x2009` - La utilización de atributos estáticos debe estar justificada

### Explicación

Los atributos `static` son compartidos por **todas las instancias** de una clase. Solo deben usarse cuando sea semánticamente correcto que el valor sea único para toda la clase, no por instancia.

### Justificación

1. **Claridad semántica**: `static` indica "compartido por todos".
2. **Prevención de bugs**: Uso incorrecto causa comportamiento inesperado.
3. **Thread-safety**: Atributos static compartidos requieren sincronización.
4. **Testing**: Atributos static complican los tests (estado compartido entre tests).
5. **Diseño**: Uso excesivo indica problemas de diseño.

### Usos válidos de `static`

#### Constantes de clase

```java
// ✅ Constantes compartidas
public class Configuracion {
    public static final int MAX_CONEXIONES = 100;
    public static final String VERSION = "1.0.0";
    public static final double PI = 3.14159;
}
```

#### Contadores globales

```java
// ✅ Contador compartido entre instancias
public class Usuario {
    private static int contadorInstancias = 0;
    private int id;
    
    public Usuario() {
        this.id = ++contadorInstancias;  // ID único por instancia
    }
    
    public static int getCantidadUsuarios() {
        return contadorInstancias;
    }
}
```

#### Caches compartidos

```java
// ✅ Cache compartido (con justificación)
public class RepositorioUsuarios {
    /**
     * Cache de usuarios consultados recientemente.
     * Static porque es compartido entre todas las instancias del repositorio
     * para evitar duplicación de datos en memoria.
     */
    private static final Map<Integer, Usuario> CACHE_GLOBAL = new ConcurrentHashMap<>();
}
```

### Usos incorrectos de `static`

#### "Atajos" problemáticos ❌

```java
// ❌ Static por conveniencia, no por diseño
public class Calculadora {
    private static double resultado;  // ❌ Compartido entre instancias!
    
    public void sumar(double a, double b) {
        resultado = a + b;  // ❌ Afecta a todas las calculadoras
    }
}

// Problema: Comportamiento inesperado
Calculadora c1 = new Calculadora();
Calculadora c2 = new Calculadora();

c1.sumar(10, 5);  // resultado = 15
c2.sumar(3, 2);   // resultado = 5

// ❌ c1 ahora tiene resultado = 5 también!
```

#### Corrección ✅

```java
// ✅ Atributo de instancia
public class Calculadora {
    private double resultado;  // ✅ Cada instancia tiene su propio valor
    
    public void sumar(double a, double b) {
        this.resultado = a + b;  // ✅ Solo afecta esta instancia
    }
}
```

### Cuándo justificar uso de `static`

Si usás un atributo `static` (que no sea constante), documentá la justificación:

```java
public class Conexion {
    /**
     * Pool compartido de conexiones a base de datos.
     * <p>
     * Static porque:
     * 1. Las conexiones son costosas de crear
     * 2. Se reutilizan entre diferentes partes del sistema
     * 3. El pool debe ser único para toda la aplicación
     * <p>
     * Thread-safe: sincronizado internamente por el ConnectionPool.
     */
    private static final ConnectionPool POOL = new ConnectionPool(10);
}
```

### Problemas comunes

#### Race conditions con static

```java
// ❌ No thread-safe
public class Contador {
    private static int valor = 0;
    
    public void incrementar() {
        valor++;  // ❌ Race condition con múltiples hilos
    }
}

// ✅ Thread-safe
public class Contador {
    private static AtomicInteger valor = new AtomicInteger(0);
    
    public void incrementar() {
        valor.incrementAndGet();  // ✅ Atómico
    }
}
```

(regla-0x200A)=
## `0x200A` - Los métodos deben tener máximo 20-30 líneas de código

### Explicación

Los métodos deben ser cortos y enfocados. Si un método excede 20-30 líneas, probablemente hace demasiadas cosas y debe dividirse en submétodos privados.

### Límite: 10-15 ideal, 20-30 máximo

:::{tip}
Método coordinador corto que delega en submétodos privados enfocados.
:::

**Correcto**:
```java
public void procesarPedido(Pedido pedido) {
    validarPedido(pedido);
    calcularTotales(pedido);
    aplicarDescuentos(pedido);
    finalizarPedido(pedido);
}

private void validarPedido(Pedido pedido) {
    // lógica de validación
}
```

(regla-0x200B)=
## `0x200B` - Evitar retornos `null` cuando sea posible

### Explicación

Retornar `null` obliga a verificaciones constantes. Preferir `Optional<T>`, colecciones vacías, o Null Object pattern.

### Alternativas

**Optional para valores únicos**:
```java
public Optional<String> buscarNombre(int id) {
    if (id < 0) {
        return Optional.empty();
    }
    return Optional.ofNullable(nombres.get(id));
}
```

**Colecciones vacías**:
```java
public List<String> buscarNombres(String patron) {
    if (patron == null) {
        return Collections.emptyList();  // No null
    }
    // búsqueda
}
```

(regla-0x200C)=
## `0x200C` - No usar métodos getter/setter si violan encapsulamiento

### Explicación

Crear getters/setters automáticamente para todo atributo puede violar el encapsulamiento. En muchos casos, métodos específicos del dominio son mejores que getters/setters genéricos.

Ver también {ref}`regla-0x2001` sobre encapsulamiento y {ref}`regla-0x2011` sobre no exponer detalles internos.

(regla-0x200D)=
## `0x200D` - Las clases deben tener una única responsabilidad (SRP)

### Explicación

**Single Responsibility Principle**: cada clase debe tener una sola razón para cambiar. Una clase con múltiples responsabilidades debe dividirse.

### Ejemplos

#### Incorrecto ❌

```java
// ❌ Clase "God Object" - Hace todo
class Usuario {
    private String nombre;
    private String email;
    
    // Responsabilidad 1: Gestión de datos
    public void setNombre(String nombre) { }
    
    // Responsabilidad 2: Validación
    public boolean validarEmail() { }
    
    // Responsabilidad 3: Persistencia
    public void guardarEnBaseDeDatos() { }
    
    // Responsabilidad 4: Notificaciones
    public void enviarEmailBienvenida() { }
}
```

#### Correcto ✅

```java
// ✅ Responsabilidades separadas
class Usuario {
    private String nombre;
    private String email;
    
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
}

class ValidadorUsuario {
    public boolean validarEmail(String email) { }
}

class RepositorioUsuario {
    public void guardar(Usuario usuario) { }
}

class NotificadorUsuario {
    public void enviarBienvenida(Usuario usuario) { }
}
```

(regla-0x200E)=
## `0x200E` - La implementación de `equals` debe usar Pattern Matching para el cast

### Explicación

Usar **Pattern Matching for instanceof** (Java 16+) en `equals()`. Elimina el cast explícito.

**Con Pattern Matching** ✅:
```java
@Override
public boolean equals(Object obj) {
    if (!(obj instanceof Persona otro)) {  // ✅ Cast automático
        return false;
    }
    return this.nombre.equals(otro.nombre);
}
```

(regla-0x200F)=
## `0x200F` - La implementación de `equals` debe ser primero la de `Object`

### Explicación

Sobreescribir (`@Override`) `equals(Object)`, no sobrecargar con tipo específico.

**Correcto** ✅:
```java
@Override
public boolean equals(Object obj) {  // ✅ Override de Object
    if (!(obj instanceof Persona otro)) {
        return false;
    }
    return this.nombre.equals(otro.nombre);
}
```

:::{warning}
**Importante** (TP9 - Agenda): NO comparar usando `hashCode()`. Los valores de `hashCode()` no garantizan ausencia de colisiones.
:::

(regla-0x2010)=
## `0x2010` - La implementación de `hashCode` debe emplear la librería

### Explicación

Usar `Objects.hash()` para implementar `hashCode()`. Presente en `Arrays` y `Objects`.

**Correcto** ✅:
```java
@Override
public int hashCode() {
    return Objects.hash(nombre, edad);  // ✅ Librería estándar
}
```

(regla-0x2011)=
## `0x2011` - No exponer detalles internos mediante getters (TP9 - Agenda)

### Explicación

No usar getters para extraer información y hacer comparaciones fuera de la clase. Esto rompe el encapsulamiento. En su lugar, crear métodos de dominio que encapsulen la comparación.

**Incorrecto** ❌:
```java
// En clase Agenda
for (Contacto c : contactos) {
    if (c.getNombre().equals(nombre)) {  // ❌ Rompe encapsulamiento
        return c;
    }
}
```

**Correcto** ✅:
```java
// En clase Contacto
public boolean tieneNombre(String nombre) {
    return this.nombre.equals(nombre);
}

// En clase Agenda
for (Contacto c : contactos) {
    if (c.tieneNombre(nombre)) {  // ✅ Encapsulamiento respetado
        return c;
    }
}
```

(regla-0x2012)=
## `0x2012` - Usar Factory Methods para construcción compleja (TP9 - Agenda)

### Explicación

Si el constructor tiene muchos parámetros o múltiples formas de construcción, usar factory methods con nombres descriptivos.

```java
public static Contacto crearContactoCompleto(String nombre, String telefono, 
                                             String email, String direccion) {
    return new Contacto(nombre, telefono, email, direccion);
}

public static Contacto crearContactoBasico(String nombre, String telefono) {
    return new Contacto(nombre, telefono, null, null);
}
```

