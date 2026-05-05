---
title: "8: Refactoring y Code Smells"
subtitle: "El Arte de Mejorar Código Existente"
subject: Programación Orientada a Objetos
---

(oop-refactoring)=
# OOP 7: Refactoring y Code Smells

En los capítulos anteriores aprendimos a diseñar sistemas orientados a objetos aplicando contratos ({ref}`oop-contratos`), patrones ({ref}`oop5-patrones-diseno`) y principios sólidos ({ref}`oop-solid`). Pero, ¿qué pasa cuando heredamos código mal diseñado? ¿O cuando nuestro propio código evoluciona y acumula decisiones cuestionables?

El **refactoring** es la disciplina de mejorar la estructura interna del código sin cambiar su comportamiento observable. Los **code smells** son indicadores que nos alertan sobre problemas de diseño que merecen atención.

:::{admonition} Objetivos de Aprendizaje
:class: tip

Al finalizar este capítulo, serás capaz de:

1. Entender qué es refactoring y cuándo aplicarlo
2. Identificar code smells comunes en código orientado a objetos
3. Aplicar refactorizaciones catalogadas para corregir problemas
4. Usar tests como red de seguridad durante el refactoring
5. Equilibrar la mejora del código con la entrega de valor
6. Relacionar code smells con violaciones de principios SOLID
:::

---

(que-es-refactoring)=
## ¿Qué es Refactoring?

(definicion-refactoring)=
### Definición

:::{admonition} Definición Formal
:class: important

**Refactoring** es el proceso de cambiar un sistema de software de manera que no altera el comportamiento externo del código, pero mejora su estructura interna.

— Martin Fowler, "Refactoring: Improving the Design of Existing Code"
:::

La palabra clave es **comportamiento externo**. Después de un refactoring:

- Los tests existentes deben seguir pasando
- Los usuarios no notan ningún cambio
- El código es más legible, mantenible o extensible

```{mermaid}
graph LR
    A[Código<br/>Funcional<br/>pero Desordenado] -->|Refactoring| B[Código<br/>Limpio<br/>y Funcional]
    
    A -.->|mismo comportamiento| C[Tests Pasan ✓]
    B -.->|mismo comportamiento| C
    
    A -.->|misma funcionalidad| D[Usuarios<br/>No Notan Cambios ✓]
    B -.->|misma funcionalidad| D
    
    style A fill:#faa,stroke:#333
    style B fill:#afa,stroke:#333
    style C fill:#aaf,stroke:#333
    style D fill:#ffa,stroke:#333
    
    note1[Estructura interna mejorada]
    note2[Sin cambios en comportamiento observable]
```

(por-que-refactorizar)=
### ¿Por qué Refactorizar?

El código se **degrada naturalmente** con el tiempo:

1. **Requisitos cambian**: Lo que era un buen diseño para la versión 1.0 no escala a la versión 3.0
2. **Conocimiento aumenta**: Aprendemos mejores formas de resolver el problema
3. **Deuda técnica acumulada**: Atajos tomados bajo presión
4. **Múltiples autores**: Diferentes estilos y niveles de experiencia

Sin refactoring, el código se vuelve:

- Difícil de entender
- Difícil de modificar
- Propenso a bugs
- Costoso de mantener

(cuando-refactorizar)=
### ¿Cuándo Refactorizar?

:::{admonition} La Regla de los Tres
:class: tip

**Primera vez**: Hacelo funcionar
**Segunda vez**: Notás la duplicación, pero seguís adelante
**Tercera vez**: ¡Refactorizá!

— Don Roberts
:::

Momentos ideales para refactorizar:

1. **Antes de agregar una feature**: "Primero limpio, luego agrego"
2. **Después de arreglar un bug**: "Si fue difícil encontrarlo, simplifiquemos"
3. **Durante code review**: "Este código podría ser más claro"
4. **Cuando el código huele mal**: Detectás un {ref}`code-smells`

(cuando-no-refactorizar)=
### ¿Cuándo NO Refactorizar?

- **Deadline inminente**: El refactoring lleva tiempo; hay que ser pragmático
- **Código que será descartado**: No pulir código que se va a tirar
- **Sin tests**: Refactorizar sin red de seguridad es arriesgado
- **No entendés el código**: Primero comprendé, luego mejorá

---

(code-smells)=
## Code Smells: Detectando Problemas

(que-son-code-smells)=
### ¿Qué son los Code Smells?

Un **code smell** (olor a código) es una indicación superficial de que probablemente hay un problema más profundo en el sistema. No es un bug — el código funciona — pero sugiere que el diseño podría mejorarse.

:::{admonition} Analogía
:class: note

El olor a gas no es un incendio, pero indica que hay una fuga que podría causar uno. Los code smells no son bugs, pero indican debilidades que podrían causar problemas futuros.
:::

Kent Beck y Martin Fowler catalogaron los code smells más comunes. Los organizamos en categorías:

```
┌─────────────────────────────────────────────────────────────┐
│                    CATEGORÍAS DE CODE SMELLS                │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │   BLOATERS      │  │   COUPLERS      │                  │
│  │ Código inflado  │  │ Acoplamiento    │                  │
│  │                 │  │ excesivo        │                  │
│  └─────────────────┘  └─────────────────┘                  │
│                                                             │
│  ┌─────────────────┐  ┌─────────────────┐                  │
│  │ OBJECT-ORIENTED │  │  DISPENSABLES   │                  │
│  │    ABUSERS      │  │ Código          │                  │
│  │ Mal uso de OOP  │  │ innecesario     │                  │
│  └─────────────────┘  └─────────────────┘                  │
│                                                             │
│  ┌─────────────────┐                                       │
│  │ CHANGE          │                                       │
│  │ PREVENTERS      │                                       │
│  │ Dificultan      │                                       │
│  │ cambios         │                                       │
│  └─────────────────┘                                       │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

(bloaters)=
## Bloaters: Código Inflado

Los bloaters son code smells donde el código ha crecido demasiado, volviéndose difícil de manejar.

(long-method)=
### Long Method (Método Largo)

**Síntoma**: Un método que tiene demasiadas líneas de código, típicamente más de 20-30 líneas.

**Por qué es malo**:
- Difícil de entender de un vistazo
- Difícil de testear en aislamiento
- Probablemente hace más de una cosa (viola SRP)

**Ejemplo problemático**:

```java
public void procesarPedido(Pedido pedido) {
    // Validar pedido (10 líneas)
    if (pedido == null) {
        throw new IllegalArgumentException("Pedido nulo");
    }
    if (pedido.getItems().isEmpty()) {
        throw new IllegalArgumentException("Pedido vacío");
    }
    for (Item item : pedido.getItems()) {
        if (item.getCantidad() <= 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }
        if (item.getPrecio() < 0) {
            throw new IllegalArgumentException("Precio negativo");
        }
    }
    
    // Calcular totales (15 líneas)
    double subtotal = 0;
    for (Item item : pedido.getItems()) {
        subtotal += item.getPrecio() * item.getCantidad();
    }
    double impuestos = subtotal * 0.21;
    double descuento = 0;
    if (pedido.getCliente().esPremium()) {
        descuento = subtotal * 0.10;
    }
    if (subtotal > 1000) {
        descuento += subtotal * 0.05;
    }
    double total = subtotal + impuestos - descuento;
    pedido.setTotal(total);
    
    // Actualizar inventario (10 líneas)
    for (Item item : pedido.getItems()) {
        Producto producto = inventario.buscar(item.getProductoId());
        if (producto.getStock() < item.getCantidad()) {
            throw new StockInsuficienteException(producto);
        }
        producto.reducirStock(item.getCantidad());
        inventario.actualizar(producto);
    }
    
    // Enviar notificaciones (8 líneas)
    String mensaje = "Su pedido #" + pedido.getId() + " ha sido procesado";
    emailService.enviar(pedido.getCliente().getEmail(), mensaje);
    if (pedido.getCliente().getTelefono() != null) {
        smsService.enviar(pedido.getCliente().getTelefono(), mensaje);
    }
    
    // Guardar en base de datos
    pedidoRepository.guardar(pedido);
}
```

**Refactorización: Extract Method**

```java
public void procesarPedido(Pedido pedido) {
    validarPedido(pedido);
    calcularTotales(pedido);
    actualizarInventario(pedido);
    notificarCliente(pedido);
    pedidoRepository.guardar(pedido);
}

private void validarPedido(Pedido pedido) {
    Objects.requireNonNull(pedido, "Pedido no puede ser nulo");
    if (pedido.getItems().isEmpty()) {
        throw new IllegalArgumentException("Pedido vacío");
    }
    pedido.getItems().forEach(this::validarItem);
}

private void validarItem(Item item) {
    if (item.getCantidad() <= 0) {
        throw new IllegalArgumentException("Cantidad inválida");
    }
    if (item.getPrecio() < 0) {
        throw new IllegalArgumentException("Precio negativo");
    }
}

private void calcularTotales(Pedido pedido) {
    double subtotal = calcularSubtotal(pedido);
    double impuestos = subtotal * 0.21;
    double descuento = calcularDescuento(pedido, subtotal);
    pedido.setTotal(subtotal + impuestos - descuento);
}

private double calcularSubtotal(Pedido pedido) {
    return pedido.getItems().stream()
        .mapToDouble(i -> i.getPrecio() * i.getCantidad())
        .sum();
}

private double calcularDescuento(Pedido pedido, double subtotal) {
    double descuento = 0;
    if (pedido.getCliente().esPremium()) {
        descuento += subtotal * 0.10;
    }
    if (subtotal > 1000) {
        descuento += subtotal * 0.05;
    }
    return descuento;
}

private void actualizarInventario(Pedido pedido) {
    for (Item item : pedido.getItems()) {
        Producto producto = inventario.buscar(item.getProductoId());
        producto.reducirStock(item.getCantidad());
        inventario.actualizar(producto);
    }
}

private void notificarCliente(Pedido pedido) {
    String mensaje = construirMensajeConfirmacion(pedido);
    Cliente cliente = pedido.getCliente();
    emailService.enviar(cliente.getEmail(), mensaje);
    if (cliente.getTelefono() != null) {
        smsService.enviar(cliente.getTelefono(), mensaje);
    }
}
```

Ahora el método principal cuenta la **historia** en 5 líneas. Cada método extraído es:
- Fácil de entender
- Fácil de testear
- Reutilizable

(large-class)=
### Large Class (Clase Grande)

**Síntoma**: Una clase con demasiados campos, métodos y responsabilidades.

**Por qué es malo**:
- Viola el Principio de Responsabilidad Única
- Difícil de entender y mantener
- Alto acoplamiento interno

**Señales de alerta**:
- Más de 200-300 líneas de código
- Más de 10-15 métodos públicos
- Grupos de campos que siempre se usan juntos
- Prefijos o sufijos comunes en grupos de métodos

**Ejemplo problemático**:

```java
public class Usuario {
    // Datos básicos
    private String nombre;
    private String email;
    private String password;
    
    // Dirección
    private String calle;
    private String ciudad;
    private String codigoPostal;
    private String pais;
    
    // Preferencias
    private boolean recibirNewsletter;
    private String idioma;
    private String zonaHoraria;
    
    // Métodos de autenticación
    public boolean validarPassword(String password) { ... }
    public void cambiarPassword(String nueva) { ... }
    public String generarTokenRecuperacion() { ... }
    
    // Métodos de dirección
    public String getDireccionCompleta() { ... }
    public boolean validarDireccion() { ... }
    public double calcularDistanciaA(Usuario otro) { ... }
    
    // Métodos de preferencias
    public void suscribirNewsletter() { ... }
    public void cambiarIdioma(String idioma) { ... }
    
    // Métodos de persistencia
    public void guardar() { ... }
    public void cargar(int id) { ... }
    
    // Métodos de notificación
    public void enviarEmail(String asunto, String cuerpo) { ... }
    public void enviarSMS(String mensaje) { ... }
}
```

**Refactorización: Extract Class**

```java
// Clase principal simplificada
public class Usuario {
    private String nombre;
    private String email;
    private Credenciales credenciales;
    private Direccion direccion;
    private Preferencias preferencias;
    
    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
        this.credenciales = new Credenciales();
        this.direccion = new Direccion();
        this.preferencias = new Preferencias();
    }
    
    // Delegación a clases especializadas
    public boolean autenticar(String password) {
        return credenciales.validar(password);
    }
    
    public String getDireccionCompleta() {
        return direccion.getCompleta();
    }
}

// Clase extraída: Credenciales
public class Credenciales {
    private String passwordHash;
    private Instant ultimoCambio;
    
    public boolean validar(String password) { ... }
    public void cambiar(String nuevaPassword) { ... }
    public String generarTokenRecuperacion() { ... }
}

// Clase extraída: Dirección
public class Direccion {
    private String calle;
    private String ciudad;
    private String codigoPostal;
    private String pais;
    
    public String getCompleta() { ... }
    public boolean esValida() { ... }
    public double calcularDistanciaA(Direccion otra) { ... }
}

// Clase extraída: Preferencias
public class Preferencias {
    private boolean recibirNewsletter;
    private String idioma;
    private String zonaHoraria;
    
    public void suscribirNewsletter() { ... }
    public void cambiarIdioma(String idioma) { ... }
}
```

(primitive-obsession)=
### Primitive Obsession (Obsesión con Primitivos)

**Síntoma**: Usar tipos primitivos (String, int, double) para representar conceptos del dominio.

**Por qué es malo**:
- Pérdida de semántica
- Validación dispersa
- Duplicación de lógica
- Errores difíciles de detectar

**Ejemplo problemático**:

```java
public class Producto {
    private String codigo;        // "ABC-12345"
    private double precio;        // ¿En qué moneda?
    private String moneda;        // "USD", "EUR", "ARS"
    private String email;         // ¿Es válido?
    private String telefono;      // ¿Formato correcto?
    
    public void aplicarDescuento(double porcentaje) {
        // ¿Qué pasa si porcentaje es 150 o -10?
        precio = precio * (1 - porcentaje / 100);
    }
    
    public void cambiarPrecio(double nuevoPrecio, String nuevaMoneda) {
        // ¿Cómo convertimos?
        this.precio = nuevoPrecio;
        this.moneda = nuevaMoneda;
    }
}

// Uso propenso a errores
producto.aplicarDescuento(0.15);   // ¿Es 15% o 0.15%?
producto.aplicarDescuento(150);    // ¡Precio negativo!
producto.cambiarPrecio(100, "dolares");  // ¿Es válido "dolares"?
```

**Refactorización: Replace Primitive with Object**

```java
// Value Objects para conceptos del dominio
public final class CodigoProducto {
    private final String valor;
    
    public CodigoProducto(String valor) {
        if (!valor.matches("[A-Z]{3}-\\d{5}")) {
            throw new IllegalArgumentException(
                "Código debe tener formato XXX-99999"
            );
        }
        this.valor = valor;
    }
    
    public String getValor() { return valor; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodigoProducto)) return false;
        return valor.equals(((CodigoProducto) o).valor);
    }
    
    @Override
    public int hashCode() { return valor.hashCode(); }
}

public final class Dinero {
    private final BigDecimal cantidad;
    private final Moneda moneda;
    
    public Dinero(BigDecimal cantidad, Moneda moneda) {
        if (cantidad.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cantidad no puede ser negativa");
        }
        this.cantidad = cantidad;
        this.moneda = moneda;
    }
    
    public Dinero aplicarDescuento(Porcentaje descuento) {
        BigDecimal factor = BigDecimal.ONE.subtract(
            descuento.comoDecimal()
        );
        return new Dinero(cantidad.multiply(factor), moneda);
    }
    
    public Dinero sumar(Dinero otro) {
        if (!this.moneda.equals(otro.moneda)) {
            throw new MonedaIncompatibleException();
        }
        return new Dinero(cantidad.add(otro.cantidad), moneda);
    }
}

public final class Porcentaje {
    private final BigDecimal valor;
    
    public Porcentaje(double valor) {
        if (valor < 0 || valor > 100) {
            throw new IllegalArgumentException(
                "Porcentaje debe estar entre 0 y 100"
            );
        }
        this.valor = BigDecimal.valueOf(valor);
    }
    
    public BigDecimal comoDecimal() {
        return valor.divide(BigDecimal.valueOf(100));
    }
}

public enum Moneda {
    USD("Dólar estadounidense", "$"),
    EUR("Euro", "€"),
    ARS("Peso argentino", "$");
    
    private final String nombre;
    private final String simbolo;
    
    // Constructor y getters
}

// Producto refactorizado
public class Producto {
    private CodigoProducto codigo;
    private Dinero precio;
    private Email email;
    private Telefono telefono;
    
    public void aplicarDescuento(Porcentaje descuento) {
        this.precio = precio.aplicarDescuento(descuento);
    }
}

// Uso seguro y expresivo
Porcentaje descuento = new Porcentaje(15);  // Claro: 15%
producto.aplicarDescuento(descuento);

// Esto no compila - ¡error detectado en compilación!
// producto.aplicarDescuento(0.15);
```

(long-parameter-list)=
### Long Parameter List (Lista Larga de Parámetros)

**Síntoma**: Métodos con más de 3-4 parámetros.

**Por qué es malo**:
- Difícil de recordar el orden
- Fácil de confundir parámetros del mismo tipo
- Indica que el método hace demasiado

**Ejemplo problemático**:

```java
public Reserva crearReserva(
    String nombreHuesped,
    String emailHuesped,
    String telefonoHuesped,
    int habitacionNumero,
    String habitacionTipo,
    LocalDate fechaIngreso,
    LocalDate fechaEgreso,
    int cantidadAdultos,
    int cantidadNinos,
    boolean incluyeDesayuno,
    boolean incluyeEstacionamiento,
    String codigoDescuento,
    String metodoPago
) {
    // ... implementación
}

// Uso propenso a errores
Reserva r = crearReserva(
    "Juan", "juan@email.com", "123456",
    101, "doble",
    LocalDate.of(2024, 1, 15),
    LocalDate.of(2024, 1, 10),  // ¡Fecha invertida!
    2, 1,
    true, false,
    "DESC10", "tarjeta"
);
```

**Refactorización: Introduce Parameter Object**

```java
// Agrupar parámetros relacionados en objetos
public class DatosHuesped {
    private final String nombre;
    private final Email email;
    private final Telefono telefono;
    
    // Constructor, getters
}

public class DatosHabitacion {
    private final int numero;
    private final TipoHabitacion tipo;
    
    // Constructor, getters
}

public class PeriodoEstadia {
    private final LocalDate ingreso;
    private final LocalDate egreso;
    
    public PeriodoEstadia(LocalDate ingreso, LocalDate egreso) {
        if (egreso.isBefore(ingreso)) {
            throw new IllegalArgumentException(
                "Fecha de egreso debe ser posterior a ingreso"
            );
        }
        this.ingreso = ingreso;
        this.egreso = egreso;
    }
    
    public long cantidadNoches() {
        return ChronoUnit.DAYS.between(ingreso, egreso);
    }
}

public class Ocupacion {
    private final int adultos;
    private final int ninos;
    
    // Validaciones y métodos
}

public class Extras {
    private final boolean desayuno;
    private final boolean estacionamiento;
    
    // Constructor, getters
}

// Método simplificado
public Reserva crearReserva(
    DatosHuesped huesped,
    DatosHabitacion habitacion,
    PeriodoEstadia estadia,
    Ocupacion ocupacion,
    Extras extras,
    CodigoDescuento descuento,
    MetodoPago metodoPago
) {
    // ... implementación
}

// Uso más claro y seguro
Reserva r = crearReserva(
    new DatosHuesped("Juan", email, telefono),
    new DatosHabitacion(101, TipoHabitacion.DOBLE),
    new PeriodoEstadia(
        LocalDate.of(2024, 1, 10),
        LocalDate.of(2024, 1, 15)  // ¡Valida automáticamente!
    ),
    new Ocupacion(2, 1),
    new Extras(true, false),
    CodigoDescuento.of("DESC10"),
    MetodoPago.TARJETA
);
```

**Alternativa: Builder Pattern**

```java
Reserva r = Reserva.builder()
    .huesped("Juan", "juan@email.com", "123456")
    .habitacion(101, TipoHabitacion.DOBLE)
    .desde(LocalDate.of(2024, 1, 10))
    .hasta(LocalDate.of(2024, 1, 15))
    .adultos(2)
    .ninos(1)
    .conDesayuno()
    .codigoDescuento("DESC10")
    .metodoPago(MetodoPago.TARJETA)
    .build();
```

---

(object-orientation-abusers)=
## Object-Orientation Abusers: Mal Uso de OOP

Estos smells ocurren cuando las características de la programación orientada a objetos se usan incorrectamente.

(switch-statements)=
### Switch Statements (Cadenas de Switch)

**Síntoma**: Múltiples switch/if-else sobre el mismo tipo o condición dispersos por el código.

**Por qué es malo**:
- Viola el Principio Abierto/Cerrado
- Duplicación de lógica de decisión
- Agregar un nuevo caso requiere modificar múltiples lugares

**Ejemplo problemático**:

```java
public class Empleado {
    private TipoEmpleado tipo;
    private double salarioBase;
    
    public double calcularSalario() {
        switch (tipo) {
            case TIEMPO_COMPLETO:
                return salarioBase;
            case MEDIO_TIEMPO:
                return salarioBase * 0.5;
            case CONTRATISTA:
                return salarioBase * 1.2;
            case PASANTE:
                return salarioBase * 0.3;
            default:
                throw new IllegalStateException();
        }
    }
    
    public int calcularVacaciones() {
        switch (tipo) {  // ¡Mismo switch!
            case TIEMPO_COMPLETO:
                return 20;
            case MEDIO_TIEMPO:
                return 10;
            case CONTRATISTA:
                return 0;
            case PASANTE:
                return 5;
            default:
                throw new IllegalStateException();
        }
    }
    
    public boolean puedeTrabajarRemoto() {
        switch (tipo) {  // ¡Otra vez!
            case TIEMPO_COMPLETO:
            case CONTRATISTA:
                return true;
            case MEDIO_TIEMPO:
            case PASANTE:
                return false;
            default:
                throw new IllegalStateException();
        }
    }
}
```

**Refactorización: Replace Conditional with Polymorphism**

```java
// Clase base abstracta o interfaz
public abstract class Empleado {
    protected double salarioBase;
    
    public abstract double calcularSalario();
    public abstract int calcularVacaciones();
    public abstract boolean puedeTrabajarRemoto();
}

public class EmpleadoTiempoCompleto extends Empleado {
    @Override
    public double calcularSalario() {
        return salarioBase;
    }
    
    @Override
    public int calcularVacaciones() {
        return 20;
    }
    
    @Override
    public boolean puedeTrabajarRemoto() {
        return true;
    }
}

public class EmpleadoMedioTiempo extends Empleado {
    @Override
    public double calcularSalario() {
        return salarioBase * 0.5;
    }
    
    @Override
    public int calcularVacaciones() {
        return 10;
    }
    
    @Override
    public boolean puedeTrabajarRemoto() {
        return false;
    }
}

public class Contratista extends Empleado {
    @Override
    public double calcularSalario() {
        return salarioBase * 1.2;
    }
    
    @Override
    public int calcularVacaciones() {
        return 0;
    }
    
    @Override
    public boolean puedeTrabajarRemoto() {
        return true;
    }
}

public class Pasante extends Empleado {
    @Override
    public double calcularSalario() {
        return salarioBase * 0.3;
    }
    
    @Override
    public int calcularVacaciones() {
        return 5;
    }
    
    @Override
    public boolean puedeTrabajarRemoto() {
        return false;
    }
}
```

Ahora agregar un nuevo tipo de empleado solo requiere crear una nueva clase, sin tocar las existentes.

(refused-bequest)=
### Refused Bequest (Herencia Rechazada)

**Síntoma**: Una subclase hereda métodos que no usa o sobreescribe para lanzar excepciones.

**Por qué es malo**:
- Viola el Principio de Sustitución de Liskov
- La jerarquía de herencia es incorrecta
- Confunde a los usuarios de la clase

**Ejemplo problemático**:

```java
public class Pila<T> {
    protected List<T> elementos = new ArrayList<>();
    
    public void push(T elemento) {
        elementos.add(elemento);
    }
    
    public T pop() {
        return elementos.remove(elementos.size() - 1);
    }
    
    public T peek() {
        return elementos.get(elementos.size() - 1);
    }
    
    public int size() {
        return elementos.size();
    }
    
    // Métodos adicionales de List
    public T get(int index) {
        return elementos.get(index);
    }
    
    public void add(int index, T elemento) {
        elementos.add(index, elemento);
    }
}

// ¡PilaEstricta rechaza parte de la herencia!
public class PilaEstricta<T> extends Pila<T> {
    
    @Override
    public T get(int index) {
        throw new UnsupportedOperationException(
            "Solo se puede acceder al tope"
        );
    }
    
    @Override
    public void add(int index, T elemento) {
        throw new UnsupportedOperationException(
            "Solo se puede agregar al tope"
        );
    }
}
```

**Refactorización: Replace Inheritance with Delegation**

```java
// Interfaz que define solo lo necesario
public interface Pila<T> {
    void push(T elemento);
    T pop();
    T peek();
    int size();
    boolean isEmpty();
}

// Implementación usando composición
public class PilaConLista<T> implements Pila<T> {
    private final List<T> elementos = new ArrayList<>();
    
    @Override
    public void push(T elemento) {
        elementos.add(elemento);
    }
    
    @Override
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return elementos.remove(elementos.size() - 1);
    }
    
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return elementos.get(elementos.size() - 1);
    }
    
    @Override
    public int size() {
        return elementos.size();
    }
    
    @Override
    public boolean isEmpty() {
        return elementos.isEmpty();
    }
}
```

(temporary-field)=
### Temporary Field (Campo Temporal)

**Síntoma**: Un campo de instancia que solo se usa en algunos métodos o bajo ciertas condiciones.

**Por qué es malo**:
- Confunde: ¿Cuándo tiene valor válido?
- Estado inconsistente del objeto
- Dificulta el razonamiento sobre el código

**Ejemplo problemático**:

```java
public class ReporteVentas {
    private List<Venta> ventas;
    private double totalCalculado;      // Solo válido después de calcular()
    private double promedioCalculado;   // Solo válido después de calcular()
    private Venta ventaMayorCalculada;  // Solo válido después de calcular()
    
    public void cargarVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }
    
    public void calcular() {
        totalCalculado = 0;
        for (Venta v : ventas) {
            totalCalculado += v.getMonto();
        }
        promedioCalculado = totalCalculado / ventas.size();
        ventaMayorCalculada = ventas.stream()
            .max(Comparator.comparing(Venta::getMonto))
            .orElse(null);
    }
    
    public String generarTexto() {
        // ¿Qué pasa si no llamaron a calcular()?
        return String.format(
            "Total: %.2f\nPromedio: %.2f\nMayor venta: %s",
            totalCalculado,
            promedioCalculado,
            ventaMayorCalculada
        );
    }
}
```

**Refactorización: Replace Method with Method Object**

```java
// Mover los campos temporales a una clase dedicada
public class ResultadoAnalisisVentas {
    private final double total;
    private final double promedio;
    private final Venta ventaMayor;
    
    private ResultadoAnalisisVentas(double total, double promedio, Venta ventaMayor) {
        this.total = total;
        this.promedio = promedio;
        this.ventaMayor = ventaMayor;
    }
    
    public static ResultadoAnalisisVentas analizar(List<Venta> ventas) {
        if (ventas.isEmpty()) {
            return new ResultadoAnalisisVentas(0, 0, null);
        }
        
        double total = ventas.stream()
            .mapToDouble(Venta::getMonto)
            .sum();
        
        double promedio = total / ventas.size();
        
        Venta mayor = ventas.stream()
            .max(Comparator.comparing(Venta::getMonto))
            .orElse(null);
        
        return new ResultadoAnalisisVentas(total, promedio, mayor);
    }
    
    public double getTotal() { return total; }
    public double getPromedio() { return promedio; }
    public Venta getVentaMayor() { return ventaMayor; }
    
    public String comoTexto() {
        return String.format(
            "Total: %.2f\nPromedio: %.2f\nMayor venta: %s",
            total, promedio, ventaMayor
        );
    }
}

// Uso
public class ReporteVentas {
    public String generar(List<Venta> ventas) {
        ResultadoAnalisisVentas resultado = ResultadoAnalisisVentas.analizar(ventas);
        return resultado.comoTexto();
    }
}
```

---

(change-preventers)=
## Change Preventers: Código que Resiste el Cambio

Estos smells hacen que modificar el código sea innecesariamente difícil.

(divergent-change)=
### Divergent Change (Cambio Divergente)

**Síntoma**: Una clase cambia frecuentemente por diferentes razones no relacionadas.

**Por qué es malo**:
- Viola el Principio de Responsabilidad Única
- Cambios de un tipo afectan código de otro tipo
- Alto riesgo de introducir bugs

**Ejemplo**:

```java
public class GestorClientes {
    // Cambios por requisitos de UI
    public String formatearParaPantalla(Cliente c) { ... }
    public String formatearParaImpresion(Cliente c) { ... }
    
    // Cambios por requisitos de persistencia
    public void guardarEnMySQL(Cliente c) { ... }
    public void guardarEnMongoDB(Cliente c) { ... }
    
    // Cambios por requisitos de validación
    public boolean validarDatos(Cliente c) { ... }
    
    // Cambios por requisitos de negocio
    public double calcularDescuento(Cliente c) { ... }
}
```

Esta clase cambia cuando:
- Cambia el diseño de UI
- Cambia la base de datos
- Cambian las reglas de validación
- Cambian las reglas de descuento

**Refactorización: Extract Class** (siguiendo SRP)

```java
public class FormateadorCliente {
    public String formatearParaPantalla(Cliente c) { ... }
    public String formatearParaImpresion(Cliente c) { ... }
}

public interface RepositorioClientes {
    void guardar(Cliente c);
    Cliente buscar(String id);
}

public class RepositorioClientesMySQL implements RepositorioClientes { ... }
public class RepositorioClientesMongo implements RepositorioClientes { ... }

public class ValidadorCliente {
    public boolean validar(Cliente c) { ... }
}

public class CalculadorDescuentos {
    public double calcular(Cliente c) { ... }
}
```

(shotgun-surgery)=
### Shotgun Surgery (Cirugía de Escopeta)

**Síntoma**: Un cambio pequeño requiere modificaciones en muchos archivos diferentes.

**Por qué es malo**:
- Alto riesgo de olvidar algún cambio
- Difícil de estimar el esfuerzo
- Código relacionado disperso

**Ejemplo**:

```java
// Para agregar un nuevo campo "telefono" al cliente:

// Archivo 1: Cliente.java
public class Cliente {
    private String nombre;
    private String email;
    private String telefono;  // AGREGAR
    
    // getters y setters para telefono  // AGREGAR
}

// Archivo 2: ClienteDTO.java
public class ClienteDTO {
    private String telefono;  // AGREGAR
}

// Archivo 3: ClienteMapper.java
public class ClienteMapper {
    public ClienteDTO toDTO(Cliente c) {
        dto.setTelefono(c.getTelefono());  // AGREGAR
    }
}

// Archivo 4: ClienteValidator.java
public class ClienteValidator {
    public void validar(Cliente c) {
        validarTelefono(c.getTelefono());  // AGREGAR
    }
}

// Archivo 5: ClienteRepository.java
// Archivo 6: ClienteController.java
// Archivo 7: cliente.sql
// Archivo 8: ClienteFormView.java
// ... y así sucesivamente
```

**Refactorización: Move Method / Inline Class**

La solución depende del caso. Algunas estrategias:

1. **Centralizar lógica relacionada**
2. **Usar generación de código** para capas repetitivas
3. **Revisar la arquitectura** si el problema es estructural

(parallel-inheritance)=
### Parallel Inheritance Hierarchies (Jerarquías Paralelas)

**Síntoma**: Cada vez que creás una subclase de A, debés crear una subclase de B.

**Por qué es malo**:
- Duplicación estructural
- Fácil olvidar crear la clase paralela
- Acoplamiento entre jerarquías

**Ejemplo problemático**:

```java
// Cada vez que agregamos un Empleado, necesitamos un Formulario
abstract class Empleado { ... }
class EmpleadoTiempoCompleto extends Empleado { ... }
class EmpleadoMedioTiempo extends Empleado { ... }
class Contratista extends Empleado { ... }

abstract class FormularioEmpleado { ... }
class FormularioTiempoCompleto extends FormularioEmpleado { ... }
class FormularioMedioTiempo extends FormularioEmpleado { ... }
class FormularioContratista extends FormularioEmpleado { ... }
```

**Refactorización: Collapse Hierarchy o usar composición**

```java
// Opción 1: El empleado sabe cómo generar su formulario
abstract class Empleado {
    public abstract FormularioEmpleado crearFormulario();
}

// Opción 2: Un solo formulario genérico con Strategy
class FormularioEmpleado {
    private EstrategiaFormulario estrategia;
    
    public FormularioEmpleado(Empleado empleado) {
        this.estrategia = EstrategiaFormulario.para(empleado);
    }
}
```

---

(dispensables)=
## Dispensables: Código Innecesario

Código que no aporta valor y puede eliminarse.

(dead-code)=
### Dead Code (Código Muerto)

**Síntoma**: Código que nunca se ejecuta.

**Tipos comunes**:
- Variables que se asignan pero nunca se leen
- Métodos privados nunca llamados
- Condiciones que siempre son falsas
- Código comentado "por si acaso"

```java
public class Calculadora {
    private int ultimoResultado;  // Nunca se lee
    
    public int sumar(int a, int b) {
        int resultado = a + b;
        // Log antiguo que ya no usamos
        // System.out.println("Sumando " + a + " + " + b);
        ultimoResultado = resultado;
        return resultado;
    }
    
    // Método que nadie llama
    private void logOperacion(String op) {
        System.out.println(op);
    }
    
    public int dividir(int a, int b) {
        if (false) {  // Condición siempre falsa
            throw new RuntimeException("Debug");
        }
        return a / b;
    }
}
```

**Refactorización: Delete!**

```java
public class Calculadora {
    public int sumar(int a, int b) {
        return a + b;
    }
    
    public int dividir(int a, int b) {
        return a / b;
    }
}
```

:::{tip}
El código comentado "por si lo necesitamos después" es innecesario. Para eso está el control de versiones (Git). Si lo necesitás, lo recuperás del historial.
:::

(speculative-generality)=
### Speculative Generality (Generalidad Especulativa)

**Síntoma**: Abstracciones, parámetros o código "por si algún día lo necesitamos".

**Por qué es malo**:
- Complejidad sin beneficio actual
- Dificulta entender el código
- La abstracción probablemente sea incorrecta cuando realmente se necesite

**Ejemplo problemático**:

```java
// "Algún día tendremos múltiples tipos de Logger"
public interface ILogger {
    void log(String message);
    void log(String message, LogLevel level);
    void log(String message, LogLevel level, Map<String, Object> context);
}

// La única implementación que existe
public class ConsoleLogger implements ILogger {
    @Override
    public void log(String message) {
        System.out.println(message);
    }
    
    @Override
    public void log(String message, LogLevel level) {
        log(message);  // Ignora el level
    }
    
    @Override
    public void log(String message, LogLevel level, Map<String, Object> context) {
        log(message);  // Ignora todo lo adicional
    }
}

// Factory "por si algún día..."
public class LoggerFactory {
    public static ILogger create(String type) {
        if ("console".equals(type)) {
            return new ConsoleLogger();
        }
        // Nunca hay otro tipo
        return new ConsoleLogger();
    }
}
```

**Refactorización: Collapse Hierarchy / Inline Class**

```java
// Simple y directo - cuando necesitemos más, lo agregamos
public class Logger {
    public void log(String message) {
        System.out.println(message);
    }
}
```

:::{admonition} YAGNI
:class: important

**You Aren't Gonna Need It** (No lo vas a necesitar)

No agregues funcionalidad hasta que realmente la necesites. El código más fácil de mantener es el que no existe.
:::

(comments)=
### Comments (Comentarios Excesivos)

**Síntoma**: Comentarios que explican qué hace el código en lugar de por qué.

**Por qué es malo**:
- Los comentarios se desactualizan
- Indican que el código no es claro
- El código debería ser auto-documentado

**Ejemplo problemático**:

```java
// Calcular el precio final aplicando el descuento
public double calcPF(double p, double d) {
    // p es el precio
    // d es el descuento
    // Primero verificamos que el descuento sea válido
    if (d < 0 || d > 100) {
        // Si no es válido, lanzamos excepción
        throw new IllegalArgumentException("Descuento inválido");
    }
    // Calculamos el factor de descuento
    double f = 1 - (d / 100);
    // Multiplicamos el precio por el factor
    double resultado = p * f;
    // Retornamos el resultado
    return resultado;
}
```

**Refactorización: Rename / Extract Method**

```java
public double calcularPrecioConDescuento(double precioBase, double porcentajeDescuento) {
    validarPorcentaje(porcentajeDescuento);
    return precioBase * factorDescuento(porcentajeDescuento);
}

private void validarPorcentaje(double porcentaje) {
    if (porcentaje < 0 || porcentaje > 100) {
        throw new IllegalArgumentException(
            "Porcentaje debe estar entre 0 y 100, recibido: " + porcentaje
        );
    }
}

private double factorDescuento(double porcentaje) {
    return 1 - (porcentaje / 100);
}
```

:::{tip}
Los comentarios son útiles cuando explican **por qué** se tomó una decisión, no **qué** hace el código:

```java
// Usamos insertion sort en lugar de quicksort porque
// el arreglo casi siempre está ordenado (datos de sensor)
```
:::

---

(couplers)=
## Couplers: Acoplamiento Excesivo

Smells que indican dependencias problemáticas entre clases.

(feature-envy)=
### Feature Envy (Envidia de Funcionalidad)

**Síntoma**: Un método usa más datos de otra clase que de la propia.

**Por qué es malo**:
- La lógica está en el lugar equivocado
- Acopla clases innecesariamente
- Dificulta la modificación

**Ejemplo problemático**:

```java
public class ServicioEnvio {
    
    public double calcularCostoEnvio(Pedido pedido) {
        // Este método "envidia" los datos de Pedido
        double pesoTotal = 0;
        for (Item item : pedido.getItems()) {
            pesoTotal += item.getProducto().getPeso() * item.getCantidad();
        }
        
        Direccion destino = pedido.getCliente().getDireccion();
        String zona = determinarZona(destino.getCodigoPostal());
        
        double costoBase = pesoTotal * 0.5;
        if ("remota".equals(zona)) {
            costoBase *= 1.5;
        }
        
        if (pedido.getCliente().esPremium()) {
            costoBase *= 0.9;  // 10% descuento
        }
        
        return costoBase;
    }
}
```

**Refactorización: Move Method**

```java
// Mover la lógica a donde están los datos
public class Pedido {
    private List<Item> items;
    private Cliente cliente;
    
    public double calcularPesoTotal() {
        return items.stream()
            .mapToDouble(item -> item.getPeso())
            .sum();
    }
    
    public double calcularCostoEnvio(CalculadorZonas calculadorZonas) {
        double costoBase = calcularPesoTotal() * 0.5;
        
        String zona = calculadorZonas.determinarZona(
            cliente.getDireccion().getCodigoPostal()
        );
        
        if ("remota".equals(zona)) {
            costoBase *= 1.5;
        }
        
        return cliente.aplicarDescuentoEnvio(costoBase);
    }
}

public class Cliente {
    public double aplicarDescuentoEnvio(double costo) {
        return esPremium() ? costo * 0.9 : costo;
    }
}

public class Item {
    public double getPeso() {
        return producto.getPeso() * cantidad;
    }
}
```

(inappropriate-intimacy)=
### Inappropriate Intimacy (Intimidad Inapropiada)

**Síntoma**: Dos clases acceden demasiado a los detalles internos de la otra.

**Por qué es malo**:
- Alto acoplamiento
- Difícil modificar una sin afectar la otra
- Violación de encapsulamiento

**Ejemplo problemático**:

```java
public class Motor {
    List<Cilindro> cilindros;
    double temperatura;
    double combustible;
    
    // Getters que exponen estructura interna
    public List<Cilindro> getCilindros() {
        return cilindros;  // ¡Expone la lista!
    }
}

public class Mecanico {
    public void diagnosticar(Motor motor) {
        // Accede directamente a la estructura interna
        for (Cilindro c : motor.getCilindros()) {
            if (c.compresion < 120) {
                c.necesitaReparacion = true;
            }
            c.ultimaRevision = LocalDate.now();
        }
        
        // Manipula directamente el estado
        motor.temperatura = 90;
    }
}
```

**Refactorización: Hide Delegate / Move Method**

```java
public class Motor {
    private List<Cilindro> cilindros;
    private double temperatura;
    private double combustible;
    
    // Exponer comportamiento, no estructura
    public DiagnosticoMotor diagnosticar() {
        List<Cilindro> conProblemas = cilindros.stream()
            .filter(c -> c.compresion < 120)
            .collect(Collectors.toList());
        
        return new DiagnosticoMotor(conProblemas, temperatura);
    }
    
    public void marcarCilindrosRevisados() {
        cilindros.forEach(c -> c.marcarRevisado());
    }
    
    public void normalizarTemperatura() {
        this.temperatura = 90;
    }
}

public class Mecanico {
    public void diagnosticar(Motor motor) {
        DiagnosticoMotor diagnostico = motor.diagnosticar();
        if (diagnostico.tieneProblemas()) {
            programarReparacion(diagnostico);
        }
        motor.marcarCilindrosRevisados();
        motor.normalizarTemperatura();
    }
}
```

(message-chains)=
### Message Chains (Cadenas de Mensajes)

**Síntoma**: Largas cadenas de llamadas como `a.getB().getC().getD().hacerAlgo()`.

**Por qué es malo**:
- Conocimiento excesivo de la estructura
- Frágil ante cambios en la cadena
- Viola la Ley de Demeter

**Ejemplo problemático**:

```java
// El cliente conoce toda la estructura de navegación
String nombreCiudad = pedido
    .getCliente()
    .getDireccion()
    .getCiudad()
    .getNombre();

// Peor aún: modificando a través de la cadena
pedido.getCliente().getDireccion().getCiudad().setNombre("Nueva Ciudad");
```

**Refactorización: Hide Delegate**

```java
// Pedido expone lo necesario directamente
public class Pedido {
    private Cliente cliente;
    
    public String getCiudadEntrega() {
        return cliente.getCiudadDireccion();
    }
}

public class Cliente {
    private Direccion direccion;
    
    public String getCiudadDireccion() {
        return direccion.getNombreCiudad();
    }
}

public class Direccion {
    private Ciudad ciudad;
    
    public String getNombreCiudad() {
        return ciudad.getNombre();
    }
}

// Uso simplificado
String nombreCiudad = pedido.getCiudadEntrega();
```

:::{admonition} Ley de Demeter
:class: note

"Solo hablá con tus amigos inmediatos"

Un método solo debería llamar a:
- Métodos de su propia clase
- Métodos de objetos pasados como parámetros
- Métodos de objetos que crea
- Métodos de objetos en variables de instancia
:::

---

(proceso-refactoring)=
## El Proceso de Refactoring

(refactoring-seguro)=
### Refactoring Seguro

```
┌─────────────────────────────────────────────────────────────┐
│                 CICLO DE REFACTORING                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│    ┌─────────────┐                                         │
│    │ 1. TESTS    │ Verificar que todos pasan              │
│    │    VERDES   │                                         │
│    └──────┬──────┘                                         │
│           │                                                 │
│           ▼                                                 │
│    ┌─────────────┐                                         │
│    │ 2. PEQUEÑO  │ Un solo cambio estructural              │
│    │    CAMBIO   │                                         │
│    └──────┬──────┘                                         │
│           │                                                 │
│           ▼                                                 │
│    ┌─────────────┐                                         │
│    │ 3. EJECUTAR │ Si fallan, deshacer y analizar         │
│    │    TESTS    │                                         │
│    └──────┬──────┘                                         │
│           │                                                 │
│           ▼                                                 │
│    ┌─────────────┐                                         │
│    │ 4. COMMIT   │ Punto de control                        │
│    └──────┬──────┘                                         │
│           │                                                 │
│           └──────────────▶ Repetir                          │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

**Reglas fundamentales**:

1. **Siempre tener tests** antes de refactorizar
2. **Cambios pequeños**: Un refactoring a la vez
3. **Ejecutar tests** después de cada cambio
4. **Commit frecuente**: Poder volver atrás fácilmente

(catalogo-refactorings)=
### Catálogo de Refactorizaciones Comunes

| Refactoring | Descripción | Aplica cuando |
|------------|-------------|---------------|
| **Extract Method** | Extraer código a un nuevo método | Método largo, código duplicado |
| **Inline Method** | Reemplazar llamada con el cuerpo | Método trivial, indirección innecesaria |
| **Extract Class** | Mover campos/métodos a nueva clase | Clase con múltiples responsabilidades |
| **Inline Class** | Fusionar clase pequeña en otra | Clase que hace muy poco |
| **Move Method** | Mover método a otra clase | Feature Envy |
| **Move Field** | Mover campo a otra clase | Campo más usado por otra clase |
| **Rename** | Cambiar nombre | Nombre no claro o engañoso |
| **Replace Temp with Query** | Reemplazar variable temporal con método | Variable asignada una vez |
| **Replace Conditional with Polymorphism** | Usar subclases en lugar de switch | Switch sobre tipo |
| **Introduce Parameter Object** | Agrupar parámetros en objeto | Lista larga de parámetros |
| **Preserve Whole Object** | Pasar objeto en lugar de sus campos | Se extraen múltiples campos del mismo objeto |
| **Replace Magic Number with Constant** | Crear constante con nombre | Números literales en el código |
| **Encapsulate Field** | Hacer campo privado, agregar accessors | Campo público |

(metricas-codigo)=
### Métricas para Detectar Problemas

**Métricas de complejidad**:

| Métrica | Qué mide | Umbral típico |
|---------|----------|---------------|
| **LOC** (Lines of Code) | Tamaño del método/clase | <30 líneas/método, <300/clase |
| **Cyclomatic Complexity** | Caminos de ejecución | <10 por método |
| **Depth of Inheritance** | Profundidad de herencia | <6 niveles |
| **Coupling** | Dependencias entre clases | Minimizar |
| **Cohesion** | Relación entre elementos de una clase | Maximizar |

```java
// Complejidad ciclomática = 1 + número de decisiones
public String clasificar(int valor) {    // +1 base
    if (valor < 0) {                       // +1
        return "negativo";
    } else if (valor == 0) {               // +1
        return "cero";
    } else if (valor < 10) {               // +1
        return "bajo";
    } else if (valor < 100) {              // +1
        return "medio";
    } else {
        return "alto";
    }
}
// Complejidad = 5
```

---

(relacion-solid-smells)=
## Relación entre SOLID y Code Smells

Los code smells frecuentemente indican violaciones de principios SOLID (ver {ref}`oop-solid`):

```
┌─────────────────────────────────────────────────────────────────┐
│           CODE SMELL → PRINCIPIO VIOLADO                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  Long Method          ───▶  SRP (hace demasiadas cosas)        │
│  Large Class          ───▶  SRP (múltiples responsabilidades)  │
│  Divergent Change     ───▶  SRP (cambia por múltiples razones) │
│                                                                 │
│  Switch Statements    ───▶  OCP (agregar caso = modificar)     │
│  Parallel Hierarchies ───▶  OCP (extensión requiere cambios)   │
│                                                                 │
│  Refused Bequest      ───▶  LSP (subtipo no sustituible)       │
│                                                                 │
│  Inappropriate Intimacy ──▶  ISP + DIP (acoplamiento)          │
│  Feature Envy         ───▶  SRP + DIP (lógica en lugar errado) │
│                                                                 │
│  Message Chains       ───▶  DIP (dependencia de estructura)    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

(resumen-refactoring)=
## Resumen

```
┌─────────────────────────────────────────────────────────────────┐
│                    REFACTORING Y CODE SMELLS                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  REFACTORING                                                    │
│    • Mejorar estructura sin cambiar comportamiento              │
│    • Siempre con tests como red de seguridad                   │
│    • Cambios pequeños e incrementales                          │
│    • Commit después de cada paso exitoso                       │
│                                                                 │
│  CODE SMELLS                                                    │
│    • Indicadores de problemas potenciales                      │
│    • No son bugs, pero merecen atención                        │
│    • Guían hacia dónde refactorizar                            │
│                                                                 │
│  CATEGORÍAS PRINCIPALES                                         │
│    • Bloaters: código inflado (Long Method, Large Class)       │
│    • OO Abusers: mal uso de OOP (Switch, Refused Bequest)      │
│    • Change Preventers: dificultan cambios (Shotgun Surgery)   │
│    • Dispensables: código innecesario (Dead Code, Comments)    │
│    • Couplers: acoplamiento excesivo (Feature Envy, Chains)    │
│                                                                 │
│  REFACTORIZACIONES CLAVE                                        │
│    • Extract Method/Class: dividir código grande               │
│    • Move Method/Field: ubicar lógica correctamente            │
│    • Replace Conditional with Polymorphism: eliminar switches  │
│    • Introduce Parameter Object: simplificar parámetros        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

(ejercicios-refactoring)=
## Ejercicios

```{exercise}
:label: refactor-ex-long-method

**Refactorizar Método Largo**

El siguiente método hace demasiadas cosas. Refactorizalo aplicando Extract Method para que el método principal cuente una historia clara.

```java
public void procesarTransaccion(Transaccion tx) {
    // Validaciones
    if (tx.getMonto() <= 0) {
        throw new IllegalArgumentException("Monto inválido");
    }
    if (tx.getCuenta() == null) {
        throw new IllegalArgumentException("Cuenta requerida");
    }
    if (tx.getCuenta().estaBloqueada()) {
        throw new CuentaBloqueadaException();
    }
    
    // Verificar fondos
    double saldoActual = tx.getCuenta().getSaldo();
    double montoConComision = tx.getMonto() * 1.02;
    if (saldoActual < montoConComision) {
        throw new SaldoInsuficienteException();
    }
    
    // Aplicar transacción
    tx.getCuenta().setSaldo(saldoActual - montoConComision);
    tx.setFecha(LocalDateTime.now());
    tx.setEstado("COMPLETADA");
    
    // Registrar
    transaccionRepository.guardar(tx);
    
    // Notificar
    String mensaje = "Transacción de $" + tx.getMonto() + " completada";
    emailService.enviar(tx.getCuenta().getEmail(), mensaje);
}
```
```

```{solution} refactor-ex-long-method
:class: dropdown

```java
public void procesarTransaccion(Transaccion tx) {
    validar(tx);
    verificarFondosSuficientes(tx);
    aplicar(tx);
    registrar(tx);
    notificar(tx);
}

private void validar(Transaccion tx) {
    validarMonto(tx.getMonto());
    validarCuenta(tx.getCuenta());
}

private void validarMonto(double monto) {
    if (monto <= 0) {
        throw new IllegalArgumentException("Monto debe ser positivo");
    }
}

private void validarCuenta(Cuenta cuenta) {
    Objects.requireNonNull(cuenta, "Cuenta requerida");
    if (cuenta.estaBloqueada()) {
        throw new CuentaBloqueadaException();
    }
}

private void verificarFondosSuficientes(Transaccion tx) {
    double montoRequerido = calcularMontoConComision(tx.getMonto());
    if (tx.getCuenta().getSaldo() < montoRequerido) {
        throw new SaldoInsuficienteException();
    }
}

private double calcularMontoConComision(double monto) {
    return monto * 1.02;  // 2% de comisión
}

private void aplicar(Transaccion tx) {
    double montoTotal = calcularMontoConComision(tx.getMonto());
    tx.getCuenta().debitar(montoTotal);
    tx.setFecha(LocalDateTime.now());
    tx.setEstado("COMPLETADA");
}

private void registrar(Transaccion tx) {
    transaccionRepository.guardar(tx);
}

private void notificar(Transaccion tx) {
    String mensaje = String.format(
        "Transacción de $%.2f completada", 
        tx.getMonto()
    );
    emailService.enviar(tx.getCuenta().getEmail(), mensaje);
}
```
```

```{exercise}
:label: refactor-ex-primitive

**Eliminar Obsesión con Primitivos**

Refactorizá el siguiente código creando Value Objects apropiados.

```java
public class Producto {
    private String sku;           // Formato: "CAT-00000"
    private double precio;        // Siempre positivo
    private String moneda;        // "USD", "EUR", "ARS"
    private int stockDisponible;  // Nunca negativo
    private double descuentoPorcentaje;  // 0-100
    
    public void aplicarDescuento(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("Porcentaje inválido");
        }
        precio = precio * (1 - porcentaje / 100);
    }
    
    public void reducirStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("Cantidad negativa");
        }
        if (cantidad > stockDisponible) {
            throw new IllegalArgumentException("Stock insuficiente");
        }
        stockDisponible -= cantidad;
    }
}
```
```

```{solution} refactor-ex-primitive
:class: dropdown

```java
// Value Object: SKU
public final class SKU {
    private static final Pattern FORMATO = Pattern.compile("[A-Z]{3}-\\d{5}");
    private final String valor;
    
    public SKU(String valor) {
        Objects.requireNonNull(valor);
        if (!FORMATO.matcher(valor).matches()) {
            throw new IllegalArgumentException(
                "SKU debe tener formato XXX-00000, recibido: " + valor
            );
        }
        this.valor = valor;
    }
    
    public String getValor() { return valor; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SKU)) return false;
        return valor.equals(((SKU) o).valor);
    }
    
    @Override
    public int hashCode() { return valor.hashCode(); }
    
    @Override
    public String toString() { return valor; }
}

// Value Object: Dinero
public final class Dinero {
    private final BigDecimal cantidad;
    private final Moneda moneda;
    
    public Dinero(BigDecimal cantidad, Moneda moneda) {
        Objects.requireNonNull(cantidad);
        Objects.requireNonNull(moneda);
        if (cantidad.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cantidad no puede ser negativa");
        }
        this.cantidad = cantidad.setScale(2, RoundingMode.HALF_UP);
        this.moneda = moneda;
    }
    
    public static Dinero of(double cantidad, Moneda moneda) {
        return new Dinero(BigDecimal.valueOf(cantidad), moneda);
    }
    
    public Dinero aplicarDescuento(Porcentaje descuento) {
        BigDecimal factor = BigDecimal.ONE.subtract(descuento.comoDecimal());
        return new Dinero(cantidad.multiply(factor), moneda);
    }
    
    // equals, hashCode, toString...
}

// Enum: Moneda
public enum Moneda {
    USD("Dólar", "$", 2),
    EUR("Euro", "€", 2),
    ARS("Peso Argentino", "$", 2);
    
    private final String nombre;
    private final String simbolo;
    private final int decimales;
    
    // Constructor y getters...
}

// Value Object: Porcentaje
public final class Porcentaje {
    private final BigDecimal valor;
    
    public Porcentaje(double valor) {
        if (valor < 0 || valor > 100) {
            throw new IllegalArgumentException(
                "Porcentaje debe estar entre 0 y 100, recibido: " + valor
            );
        }
        this.valor = BigDecimal.valueOf(valor);
    }
    
    public static Porcentaje of(double valor) {
        return new Porcentaje(valor);
    }
    
    public BigDecimal comoDecimal() {
        return valor.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
    }
    
    // equals, hashCode, toString...
}

// Value Object: Stock
public final class Stock {
    private final int cantidad;
    
    public Stock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("Stock no puede ser negativo");
        }
        this.cantidad = cantidad;
    }
    
    public static Stock of(int cantidad) {
        return new Stock(cantidad);
    }
    
    public Stock reducir(int cantidadAReducir) {
        if (cantidadAReducir < 0) {
            throw new IllegalArgumentException("Cantidad a reducir no puede ser negativa");
        }
        if (cantidadAReducir > this.cantidad) {
            throw new StockInsuficienteException(this.cantidad, cantidadAReducir);
        }
        return new Stock(this.cantidad - cantidadAReducir);
    }
    
    public boolean permiteSacar(int cantidad) {
        return cantidad >= 0 && cantidad <= this.cantidad;
    }
    
    public int getCantidad() { return cantidad; }
}

// Producto refactorizado
public class Producto {
    private final SKU sku;
    private Dinero precio;
    private Stock stock;
    
    public Producto(SKU sku, Dinero precio, Stock stockInicial) {
        this.sku = Objects.requireNonNull(sku);
        this.precio = Objects.requireNonNull(precio);
        this.stock = Objects.requireNonNull(stockInicial);
    }
    
    public void aplicarDescuento(Porcentaje descuento) {
        this.precio = precio.aplicarDescuento(descuento);
    }
    
    public void reducirStock(int cantidad) {
        this.stock = stock.reducir(cantidad);
    }
    
    public SKU getSku() { return sku; }
    public Dinero getPrecio() { return precio; }
    public Stock getStock() { return stock; }
}
```
```

```{exercise}
:label: refactor-ex-switch

**Reemplazar Switch con Polimorfismo**

Refactorizá el siguiente código eliminando el switch statement.

```java
public class CalculadorEnvio {
    
    public double calcularCosto(Paquete paquete, String tipoEnvio) {
        double costoBase = paquete.getPeso() * 10;
        
        switch (tipoEnvio) {
            case "STANDARD":
                return costoBase;
            case "EXPRESS":
                return costoBase * 2;
            case "OVERNIGHT":
                return costoBase * 3 + 50;
            case "ECONOMY":
                return costoBase * 0.7;
            default:
                throw new IllegalArgumentException("Tipo desconocido: " + tipoEnvio);
        }
    }
    
    public int calcularDiasEntrega(String tipoEnvio) {
        switch (tipoEnvio) {
            case "STANDARD":
                return 5;
            case "EXPRESS":
                return 2;
            case "OVERNIGHT":
                return 1;
            case "ECONOMY":
                return 10;
            default:
                throw new IllegalArgumentException("Tipo desconocido: " + tipoEnvio);
        }
    }
}
```
```

```{solution} refactor-ex-switch
:class: dropdown

```java
// Interfaz para tipos de envío
public interface TipoEnvio {
    double calcularCosto(double costoBase);
    int getDiasEntrega();
    String getNombre();
}

// Implementaciones
public class EnvioStandard implements TipoEnvio {
    @Override
    public double calcularCosto(double costoBase) {
        return costoBase;
    }
    
    @Override
    public int getDiasEntrega() {
        return 5;
    }
    
    @Override
    public String getNombre() {
        return "Standard";
    }
}

public class EnvioExpress implements TipoEnvio {
    @Override
    public double calcularCosto(double costoBase) {
        return costoBase * 2;
    }
    
    @Override
    public int getDiasEntrega() {
        return 2;
    }
    
    @Override
    public String getNombre() {
        return "Express";
    }
}

public class EnvioOvernight implements TipoEnvio {
    private static final double CARGO_URGENTE = 50;
    
    @Override
    public double calcularCosto(double costoBase) {
        return costoBase * 3 + CARGO_URGENTE;
    }
    
    @Override
    public int getDiasEntrega() {
        return 1;
    }
    
    @Override
    public String getNombre() {
        return "Overnight";
    }
}

public class EnvioEconomy implements TipoEnvio {
    @Override
    public double calcularCosto(double costoBase) {
        return costoBase * 0.7;
    }
    
    @Override
    public int getDiasEntrega() {
        return 10;
    }
    
    @Override
    public String getNombre() {
        return "Economy";
    }
}

// Calculador simplificado
public class CalculadorEnvio {
    
    public double calcularCosto(Paquete paquete, TipoEnvio tipoEnvio) {
        double costoBase = paquete.getPeso() * 10;
        return tipoEnvio.calcularCosto(costoBase);
    }
    
    public int calcularDiasEntrega(TipoEnvio tipoEnvio) {
        return tipoEnvio.getDiasEntrega();
    }
}

// Uso
TipoEnvio tipo = new EnvioExpress();
double costo = calculador.calcularCosto(paquete, tipo);
int dias = calculador.calcularDiasEntrega(tipo);

// Para agregar EnvioInternacional: solo crear nueva clase
public class EnvioInternacional implements TipoEnvio {
    private final String paisDestino;
    
    public EnvioInternacional(String paisDestino) {
        this.paisDestino = paisDestino;
    }
    
    @Override
    public double calcularCosto(double costoBase) {
        return costoBase * 4 + costoAdicionalPorPais();
    }
    
    private double costoAdicionalPorPais() {
        // Lógica según país
        return 100;
    }
    
    @Override
    public int getDiasEntrega() {
        return 15;
    }
    
    @Override
    public String getNombre() {
        return "Internacional - " + paisDestino;
    }
}
```
```

```{exercise}
:label: refactor-ex-feature-envy

**Corregir Feature Envy**

El siguiente método tiene "envidia" de otra clase. Identificá el problema y refactorizá.

```java
public class ReporteCliente {
    
    public String generarResumen(Cliente cliente) {
        StringBuilder sb = new StringBuilder();
        
        // Calcula estadísticas accediendo mucho a Pedido
        int totalPedidos = cliente.getPedidos().size();
        
        double totalGastado = 0;
        int itemsTotales = 0;
        for (Pedido p : cliente.getPedidos()) {
            for (Item i : p.getItems()) {
                totalGastado += i.getPrecio() * i.getCantidad();
                itemsTotales += i.getCantidad();
            }
        }
        
        double promedioGasto = totalPedidos > 0 ? totalGastado / totalPedidos : 0;
        
        Pedido ultimoPedido = null;
        LocalDate fechaMasReciente = LocalDate.MIN;
        for (Pedido p : cliente.getPedidos()) {
            if (p.getFecha().isAfter(fechaMasReciente)) {
                fechaMasReciente = p.getFecha();
                ultimoPedido = p;
            }
        }
        
        sb.append("Cliente: ").append(cliente.getNombre()).append("\n");
        sb.append("Total pedidos: ").append(totalPedidos).append("\n");
        sb.append("Total gastado: $").append(totalGastado).append("\n");
        sb.append("Promedio por pedido: $").append(promedioGasto).append("\n");
        sb.append("Items comprados: ").append(itemsTotales).append("\n");
        if (ultimoPedido != null) {
            sb.append("Último pedido: ").append(ultimoPedido.getFecha());
        }
        
        return sb.toString();
    }
}
```
```

```{solution} refactor-ex-feature-envy
:class: dropdown

```java
// Mover lógica a donde están los datos

public class Pedido {
    private List<Item> items;
    private LocalDate fecha;
    
    public double getTotal() {
        return items.stream()
            .mapToDouble(Item::getSubtotal)
            .sum();
    }
    
    public int getCantidadItems() {
        return items.stream()
            .mapToInt(Item::getCantidad)
            .sum();
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
}

public class Item {
    private double precio;
    private int cantidad;
    
    public double getSubtotal() {
        return precio * cantidad;
    }
    
    public int getCantidad() {
        return cantidad;
    }
}

public class Cliente {
    private String nombre;
    private List<Pedido> pedidos;
    
    public String getNombre() {
        return nombre;
    }
    
    public int getTotalPedidos() {
        return pedidos.size();
    }
    
    public double getTotalGastado() {
        return pedidos.stream()
            .mapToDouble(Pedido::getTotal)
            .sum();
    }
    
    public double getPromedioGastoPorPedido() {
        if (pedidos.isEmpty()) return 0;
        return getTotalGastado() / pedidos.size();
    }
    
    public int getTotalItemsComprados() {
        return pedidos.stream()
            .mapToInt(Pedido::getCantidadItems)
            .sum();
    }
    
    public Optional<Pedido> getUltimoPedido() {
        return pedidos.stream()
            .max(Comparator.comparing(Pedido::getFecha));
    }
    
    public EstadisticasCliente getEstadisticas() {
        return new EstadisticasCliente(
            getTotalPedidos(),
            getTotalGastado(),
            getPromedioGastoPorPedido(),
            getTotalItemsComprados(),
            getUltimoPedido().map(Pedido::getFecha).orElse(null)
        );
    }
}

// Value Object para estadísticas
public class EstadisticasCliente {
    private final int totalPedidos;
    private final double totalGastado;
    private final double promedioGasto;
    private final int itemsTotales;
    private final LocalDate fechaUltimoPedido;
    
    // Constructor...
    
    public String comoTexto() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total pedidos: ").append(totalPedidos).append("\n");
        sb.append("Total gastado: $").append(String.format("%.2f", totalGastado)).append("\n");
        sb.append("Promedio por pedido: $").append(String.format("%.2f", promedioGasto)).append("\n");
        sb.append("Items comprados: ").append(itemsTotales).append("\n");
        if (fechaUltimoPedido != null) {
            sb.append("Último pedido: ").append(fechaUltimoPedido);
        }
        return sb.toString();
    }
}

// ReporteCliente simplificado
public class ReporteCliente {
    
    public String generarResumen(Cliente cliente) {
        return "Cliente: " + cliente.getNombre() + "\n" +
               cliente.getEstadisticas().comoTexto();
    }
}
```
```

---

## Lecturas Recomendadas

- Fowler, M. (2018). *Refactoring: Improving the Design of Existing Code* (2nd ed.)
- Beck, K. (1999). *Extreme Programming Explained*
- Martin, R. C. (2008). *Clean Code: A Handbook of Agile Software Craftsmanship*
- Kerievsky, J. (2004). *Refactoring to Patterns*
