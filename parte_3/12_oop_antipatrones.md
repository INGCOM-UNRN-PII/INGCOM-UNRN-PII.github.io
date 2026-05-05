---
title: "Anti-patrones y Code Smells"
subtitle: "Reconociendo y Evitando Errores de Diseño Comunes"
subject: Programación Orientada a Objetos
---

(oop-antipatrones)=
# OOP 9: Anti-patrones y Code Smells

Hasta ahora estudiamos cómo diseñar bien: patrones ({ref}`oop5-patrones-diseno`), principios SOLID ({ref}`oop-solid`), testing ({ref}`oop-testing`), refactoring ({ref}`oop-refactoring`). Pero igual de importante es aprender a **reconocer qué está mal**. Los anti-patrones y code smells son señales de alerta que indican problemas en el diseño, muchos de ellos sutiles y difíciles de detectar hasta que el código ya está deteriorado.

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, serás capaz de:

1. Distinguir entre patrones, anti-patrones y code smells
2. Identificar los smells más comunes en código orientado a objetos
3. Reconocer anti-patrones de diseño clásicos
4. Aplicar técnicas de refactorización para eliminar smells
5. Desarrollar sensibilidad para detectar código problemático
6. Prevenir la introducción de malas prácticas desde el inicio
:::

---

(que-son-antipatrones)=
## ¿Qué Son los Anti-patrones?

(definiciones)=
### Definiciones

:::{important} Definición

Un **anti-patrón** es una solución común a un problema recurrente que parece apropiada pero resulta inefectiva o contraproducente. A diferencia de un simple error, un anti-patrón:

1. Es una solución que alguien pensó cuidadosamente
2. Parece razonable al principio
3. Tiene consecuencias negativas significativas
4. Existe una alternativa mejor conocida

— Brown et al., "AntiPatterns: Refactoring Software, Architectures, and Projects in Crisis"
:::

:::{important} Definición

Un **code smell** (olor de código) es un síntoma superficial que indica un problema más profundo en el diseño. No es un bug —el código funciona— pero sugiere fragilidad, duplicación o complejidad innecesaria.

— Martin Fowler, "Refactoring"
:::

```{mermaid}
graph TB
    A[Bug] -->|puede causar| B[Code Smell]
    B -->|acumulados forman| C[Anti-patrón]
    C -->|a gran escala| D[Deuda Técnica]
    
    A1[El código NO funciona] -.-> A
    B1[Funciona pero huele mal<br/>Síntoma superficial] -.-> B
    C1[Solución estructurada<br/>pero contraproducente] -.-> C
    D1[Acumulación de decisiones<br/>subóptimas] -.-> D
    
    style A fill:#faa,stroke:#333
    style B fill:#ffa,stroke:#333
    style C fill:#fda,stroke:#333
    style D fill:#daa,stroke:#333
```

---

(smells-de-clase)=
## Code Smells a Nivel de Clase

(clase-dios)=
### God Class (Clase Dios)

La **God Class** o **Blob** es una clase que sabe demasiado y hace demasiado. Viola flagrantemente el Principio de Responsabilidad Única (SRP).

```java
// ❌ MAL: Clase Dios - hace TODO
public class SistemaCompleto {
    private List<Usuario> usuarios;
    private List<Producto> productos;
    private List<Venta> ventas;
    private Connection conexionBD;
    private SmtpClient clienteEmail;
    private PrintWriter logger;
    
    // Gestión de usuarios
    public void registrarUsuario(String nombre, String email) { ... }
    public void autenticarUsuario(String email, String password) { ... }
    public void cambiarPassword(String email, String nuevaPass) { ... }
    public void enviarEmailRecuperacion(String email) { ... }
    
    // Gestión de productos
    public void agregarProducto(String nombre, double precio) { ... }
    public void actualizarStock(String codigo, int cantidad) { ... }
    public List<Producto> buscarProductos(String criterio) { ... }
    
    // Gestión de ventas
    public void procesarVenta(String usuarioId, List<String> productos) { ... }
    public void generarFactura(String ventaId) { ... }
    public void enviarFacturaPorEmail(String ventaId) { ... }
    
    // Reportes
    public void generarReporteVentas(Date desde, Date hasta) { ... }
    public void generarReporteInventario() { ... }
    public void exportarReporteExcel(String reporte) { ... }
    
    // Configuración
    public void cargarConfiguracion(String archivo) { ... }
    public void guardarConfiguracion() { ... }
    
    // Logging
    public void log(String mensaje) { ... }
    public void logError(Exception e) { ... }
    
    // ... 50 métodos más
}
```

**Síntomas de una God Class**:
- Más de 500-1000 líneas de código
- Muchos campos de instancia no relacionados
- Métodos que no usan la mayoría de los campos
- Difícil nombrar la clase sin usar "Manager", "System", "Processor"
- Muchas dependencias (imports)

**Refactorización**:

```java
// ✓ BIEN: Responsabilidades separadas

public class GestorUsuarios {
    private final RepositorioUsuarios repositorio;
    private final ServicioEmail servicioEmail;
    
    public void registrar(String nombre, String email) { ... }
    public Usuario autenticar(String email, String password) { ... }
    public void recuperarPassword(String email) { ... }
}

public class GestorProductos {
    private final RepositorioProductos repositorio;
    
    public void agregar(Producto producto) { ... }
    public void actualizarStock(String codigo, int cantidad) { ... }
    public List<Producto> buscar(CriterioBusqueda criterio) { ... }
}

public class ServicioVentas {
    private final RepositorioVentas repositorio;
    private final GestorProductos productos;
    private final GeneradorFacturas facturas;
    
    public Venta procesar(Usuario usuario, List<Producto> items) { ... }
}

public class GeneradorReportes {
    private final RepositorioVentas ventas;
    private final ExportadorExcel exportador;
    
    public Reporte ventasPorPeriodo(Date desde, Date hasta) { ... }
    public Reporte inventarioActual() { ... }
}
```

```{mermaid}
classDiagram
    class SistemaCompleto {
        ❌ God Class
        -List~Usuario~ usuarios
        -List~Producto~ productos
        -List~Venta~ ventas
        -Connection conexionBD
        +registrarUsuario()
        +autenticarUsuario()
        +agregarProducto()
        +actualizarStock()
        +procesarVenta()
        +generarFactura()
        +generarReporte()
        +cargarConfiguracion()
        +log()
        ... 50 métodos más
    }
    
    class GestorUsuarios {
        ✓ Responsabilidad única
        -RepositorioUsuarios repo
        -ServicioEmail email
        +registrar()
        +autenticar()
        +recuperarPassword()
    }
    
    class GestorProductos {
        ✓ Responsabilidad única
        -RepositorioProductos repo
        +agregar()
        +actualizarStock()
        +buscar()
    }
    
    class ServicioVentas {
        ✓ Responsabilidad única
        -RepositorioVentas repo
        -GestorProductos productos
        +procesar()
    }
    
    class GeneradorReportes {
        ✓ Responsabilidad única
        -RepositorioVentas ventas
        +ventasPorPeriodo()
        +inventarioActual()
    }
    
    ServicioVentas --> GestorProductos
    
    note for SistemaCompleto "Violación SRP masiva:<br>Sabe demasiado<br>Hace demasiado<br>Difícil de mantener"
    note for GestorUsuarios "Cada clase tiene<br>una responsabilidad<br>clara y cohesiva"
```

(clase-de-datos)=
### Data Class (Clase de Datos)

Una **Data Class** es una clase que solo contiene datos (campos y getters/setters) sin comportamiento significativo. Los datos "viajan" pero el comportamiento está en otro lado.

```java
// ❌ MAL: Clase de datos sin comportamiento
public class Rectangulo {
    private double ancho;
    private double alto;
    
    public double getAncho() { return ancho; }
    public void setAncho(double ancho) { this.ancho = ancho; }
    public double getAlto() { return alto; }
    public void setAlto(double alto) { this.alto = alto; }
}

// El comportamiento está separado (Feature Envy)
public class CalculadoraGeometrica {
    public double calcularArea(Rectangulo r) {
        return r.getAncho() * r.getAlto();
    }
    
    public double calcularPerimetro(Rectangulo r) {
        return 2 * (r.getAncho() + r.getAlto());
    }
    
    public boolean esValido(Rectangulo r) {
        return r.getAncho() > 0 && r.getAlto() > 0;
    }
}
```

**Refactorización**:

```java
// ✓ BIEN: Datos y comportamiento juntos
public class Rectangulo {
    private final double ancho;
    private final double alto;
    
    public Rectangulo(double ancho, double alto) {
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException(
                "Las dimensiones deben ser positivas");
        }
        this.ancho = ancho;
        this.alto = alto;
    }
    
    public double getArea() {
        return ancho * alto;
    }
    
    public double getPerimetro() {
        return 2 * (ancho + alto);
    }
    
    public Rectangulo escalar(double factor) {
        return new Rectangulo(ancho * factor, alto * factor);
    }
    
    // Solo getters si son necesarios, NO setters
    public double getAncho() { return ancho; }
    public double getAlto() { return alto; }
}
```

:::{tip}
No todas las clases de datos son malas. Los **DTOs** (Data Transfer Objects) y los **Value Objects** inmutables son casos legítimos. La diferencia es la intención: un DTO transporta datos entre capas; un Value Object representa un concepto de dominio que debería tener comportamiento.
:::

(clase-perezosa)=
### Lazy Class (Clase Perezosa)

Una **Lazy Class** hace tan poco que no justifica su existencia. Añade complejidad sin valor.

```java
// ❌ MAL: Clase que no aporta nada
public class ValidadorNoVacio {
    public boolean validar(String texto) {
        return texto != null && !texto.isEmpty();
    }
}

// Se usa así:
ValidadorNoVacio validador = new ValidadorNoVacio();
if (validador.validar(nombre)) { ... }
```

**Refactorización**: Eliminar la clase y usar el código directamente, o integrar en una clase con más responsabilidades relacionadas.

```java
// ✓ BIEN: Método estático simple o integrado en otra clase
public class Validaciones {
    public static boolean noEstaVacio(String texto) {
        return texto != null && !texto.isEmpty();
    }
    
    public static boolean esEmailValido(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$");
    }
    
    // Más validaciones relacionadas...
}
```

---

(smells-de-metodo)=
## Code Smells a Nivel de Método

(metodo-largo)=
### Long Method (Método Largo)

Un método que hace demasiado. Difícil de entender, testear y mantener.

```java
// ❌ MAL: Método de 80 líneas que hace de todo
public void procesarPedido(Pedido pedido) {
    // Validar cliente
    Cliente cliente = repositorioClientes.buscar(pedido.getClienteId());
    if (cliente == null) {
        throw new ClienteNoEncontradoException();
    }
    if (cliente.estaBloqueado()) {
        throw new ClienteBloqueadoException();
    }
    if (cliente.getDeuda() > cliente.getLimiteCredito()) {
        throw new LimiteCreditoExcedidoException();
    }
    
    // Validar productos
    double total = 0;
    for (LineaPedido linea : pedido.getLineas()) {
        Producto producto = repositorioProductos.buscar(linea.getProductoId());
        if (producto == null) {
            throw new ProductoNoEncontradoException(linea.getProductoId());
        }
        if (producto.getStock() < linea.getCantidad()) {
            throw new StockInsuficienteException(producto.getNombre());
        }
        double precioLinea = producto.getPrecio() * linea.getCantidad();
        if (linea.getCantidad() > 10) {
            precioLinea *= 0.9; // 10% descuento
        }
        total += precioLinea;
    }
    
    // Aplicar descuentos
    if (cliente.esVip()) {
        total *= 0.95; // 5% descuento VIP
    }
    if (pedido.getLineas().size() > 5) {
        total *= 0.97; // 3% por cantidad de items
    }
    
    // Calcular impuestos
    double subtotal = total;
    double iva = total * 0.21;
    total += iva;
    
    // Actualizar stock
    for (LineaPedido linea : pedido.getLineas()) {
        Producto producto = repositorioProductos.buscar(linea.getProductoId());
        producto.reducirStock(linea.getCantidad());
        repositorioProductos.guardar(producto);
    }
    
    // Generar factura
    Factura factura = new Factura();
    factura.setCliente(cliente);
    factura.setSubtotal(subtotal);
    factura.setIva(iva);
    factura.setTotal(total);
    factura.setFecha(LocalDateTime.now());
    repositorioFacturas.guardar(factura);
    
    // Enviar notificación
    String mensaje = String.format(
        "Estimado %s, su pedido por $%.2f ha sido procesado.",
        cliente.getNombre(), total);
    servicioEmail.enviar(cliente.getEmail(), "Pedido confirmado", mensaje);
    
    // Actualizar estadísticas
    estadisticas.incrementarVentas(total);
    estadisticas.incrementarPedidos();
    if (cliente.esNuevo()) {
        estadisticas.incrementarNuevosClientes();
    }
}
```

**Refactorización**: Extraer métodos con nombres descriptivos.

```java
// ✓ BIEN: Método corto que delega
public void procesarPedido(Pedido pedido) {
    Cliente cliente = validarCliente(pedido.getClienteId());
    validarDisponibilidadProductos(pedido.getLineas());
    
    ResumenPedido resumen = calcularTotales(pedido, cliente);
    
    actualizarStock(pedido.getLineas());
    Factura factura = generarFactura(cliente, resumen);
    
    notificarCliente(cliente, resumen.getTotal());
    actualizarEstadisticas(cliente, resumen.getTotal());
}

private Cliente validarCliente(String clienteId) {
    Cliente cliente = repositorioClientes.buscar(clienteId);
    if (cliente == null) {
        throw new ClienteNoEncontradoException();
    }
    cliente.validarPuedeComprar();
    return cliente;
}

private void validarDisponibilidadProductos(List<LineaPedido> lineas) {
    for (LineaPedido linea : lineas) {
        Producto producto = obtenerProducto(linea.getProductoId());
        producto.validarDisponibilidad(linea.getCantidad());
    }
}

private ResumenPedido calcularTotales(Pedido pedido, Cliente cliente) {
    double subtotal = calcularSubtotal(pedido.getLineas());
    double descuentos = calcularDescuentos(subtotal, pedido, cliente);
    double iva = (subtotal - descuentos) * 0.21;
    
    return new ResumenPedido(subtotal, descuentos, iva);
}

// ... más métodos pequeños y enfocados
```

(lista-de-parametros-larga)=
### Long Parameter List (Lista de Parámetros Larga)

Métodos con demasiados parámetros son difíciles de usar y propensos a errores.

```java
// ❌ MAL: Demasiados parámetros
public Reserva crearReserva(
    String nombreCliente,
    String emailCliente,
    String telefonoCliente,
    Date fechaInicio,
    Date fechaFin,
    String tipoHabitacion,
    int cantidadPersonas,
    boolean incluyeDesayuno,
    boolean incluyeEstacionamiento,
    String codigoDescuento,
    String metodoPago,
    String comentarios) {
    // ...
}

// Muy fácil equivocarse al llamar
Reserva r = crearReserva(
    "Juan", "juan@mail.com", "555-1234",
    inicio, fin,
    "doble", 2,
    true, false,   // ¿cuál es cuál?
    "DESC10", "VISA",
    "Cama extra si es posible"
);
```

**Refactorización**: Usar objetos para agrupar parámetros relacionados.

```java
// ✓ BIEN: Parámetros agrupados en objetos
public class DatosCliente {
    private final String nombre;
    private final String email;
    private final String telefono;
    // constructor, getters
}

public class ConfiguracionReserva {
    private final Date fechaInicio;
    private final Date fechaFin;
    private final TipoHabitacion tipoHabitacion;
    private final int cantidadPersonas;
    private final Set<Servicio> serviciosAdicionales;
    // constructor, getters
}

public Reserva crearReserva(
    DatosCliente cliente,
    ConfiguracionReserva configuracion,
    OpcionesPago pago) {
    // ...
}

// Más claro y difícil equivocarse
Reserva r = crearReserva(
    new DatosCliente("Juan", "juan@mail.com", "555-1234"),
    new ConfiguracionReserva.Builder()
        .desde(inicio)
        .hasta(fin)
        .habitacion(TipoHabitacion.DOBLE)
        .personas(2)
        .conServicio(Servicio.DESAYUNO)
        .build(),
    new OpcionesPago("VISA", "DESC10")
);
```

(envidia-de-caracteristicas)=
### Feature Envy (Envidia de Características)

Un método que usa más datos de otra clase que de la propia. El comportamiento debería moverse a donde están los datos.

```java
// ❌ MAL: El método "envidia" los datos de Empleado
public class CalculadoraSalarios {
    
    public double calcularSalarioNeto(Empleado e) {
        double salarioBruto = e.getSalarioBase();
        
        // Bonificación por antigüedad
        int años = e.getAntiguedad();
        if (años > 10) {
            salarioBruto += e.getSalarioBase() * 0.15;
        } else if (años > 5) {
            salarioBruto += e.getSalarioBase() * 0.10;
        }
        
        // Deducciones
        double deducciones = 0;
        if (e.getTipoContrato() == TipoContrato.PERMANENTE) {
            deducciones += salarioBruto * 0.11; // Jubilación
            deducciones += salarioBruto * 0.03; // Obra social
        }
        
        // Horas extra
        if (e.getHorasExtra() > 0) {
            double valorHora = e.getSalarioBase() / 160;
            salarioBruto += e.getHorasExtra() * valorHora * 1.5;
        }
        
        return salarioBruto - deducciones;
    }
}
```

**Refactorización**: Mover el comportamiento a la clase que tiene los datos.

```java
// ✓ BIEN: El comportamiento está con los datos
public class Empleado {
    private double salarioBase;
    private int antiguedad;
    private TipoContrato tipoContrato;
    private int horasExtra;
    
    public double getSalarioNeto() {
        double bruto = calcularSalarioBruto();
        double deducciones = calcularDeducciones(bruto);
        return bruto - deducciones;
    }
    
    private double calcularSalarioBruto() {
        return salarioBase + getBonificacionAntiguedad() + getPagoHorasExtra();
    }
    
    private double getBonificacionAntiguedad() {
        if (antiguedad > 10) return salarioBase * 0.15;
        if (antiguedad > 5) return salarioBase * 0.10;
        return 0;
    }
    
    private double getPagoHorasExtra() {
        if (horasExtra <= 0) return 0;
        double valorHora = salarioBase / 160;
        return horasExtra * valorHora * 1.5;
    }
    
    private double calcularDeducciones(double bruto) {
        if (tipoContrato != TipoContrato.PERMANENTE) return 0;
        return bruto * 0.14; // 11% jubilación + 3% obra social
    }
}
```

---

(smells-de-codigo)=
## Code Smells en el Código

(codigo-duplicado)=
### Duplicate Code (Código Duplicado)

El smell más común y más dañino. Cambios deben hacerse en múltiples lugares.

```java
// ❌ MAL: Lógica duplicada en varios lugares
public class ReporteVentas {
    public String generar(List<Venta> ventas) {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(50)).append("\n");
        sb.append("      REPORTE DE VENTAS\n");
        sb.append("=".repeat(50)).append("\n\n");
        
        double total = 0;
        for (Venta v : ventas) {
            sb.append(String.format("%-30s $%10.2f\n", 
                v.getDescripcion(), v.getMonto()));
            total += v.getMonto();
        }
        
        sb.append("\n").append("-".repeat(50)).append("\n");
        sb.append(String.format("TOTAL: %28s $%10.2f\n", "", total));
        sb.append("=".repeat(50)).append("\n");
        
        return sb.toString();
    }
}

public class ReporteCompras {
    public String generar(List<Compra> compras) {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(50)).append("\n");
        sb.append("      REPORTE DE COMPRAS\n");     // Solo cambia el título
        sb.append("=".repeat(50)).append("\n\n");
        
        double total = 0;
        for (Compra c : compras) {
            sb.append(String.format("%-30s $%10.2f\n", 
                c.getDescripcion(), c.getMonto()));  // Misma lógica
            total += c.getMonto();
        }
        
        sb.append("\n").append("-".repeat(50)).append("\n");
        sb.append(String.format("TOTAL: %28s $%10.2f\n", "", total));
        sb.append("=".repeat(50)).append("\n");
        
        return sb.toString();
    }
}
```

**Refactorización**: Extraer la lógica común.

```java
// ✓ BIEN: Template Method o composición
public interface ItemReporte {
    String getDescripcion();
    double getMonto();
}

public class GeneradorReportes {
    
    public String generar(String titulo, List<? extends ItemReporte> items) {
        StringBuilder sb = new StringBuilder();
        
        imprimirEncabezado(sb, titulo);
        double total = imprimirItems(sb, items);
        imprimirPie(sb, total);
        
        return sb.toString();
    }
    
    private void imprimirEncabezado(StringBuilder sb, String titulo) {
        sb.append("=".repeat(50)).append("\n");
        sb.append(String.format("%30s\n", titulo));
        sb.append("=".repeat(50)).append("\n\n");
    }
    
    private double imprimirItems(StringBuilder sb, List<? extends ItemReporte> items) {
        double total = 0;
        for (ItemReporte item : items) {
            sb.append(String.format("%-30s $%10.2f\n", 
                item.getDescripcion(), item.getMonto()));
            total += item.getMonto();
        }
        return total;
    }
    
    private void imprimirPie(StringBuilder sb, double total) {
        sb.append("\n").append("-".repeat(50)).append("\n");
        sb.append(String.format("TOTAL: %28s $%10.2f\n", "", total));
        sb.append("=".repeat(50)).append("\n");
    }
}
```

(numeros-magicos)=
### Magic Numbers (Números Mágicos)

Literales numéricos sin explicación clara de su significado.

```java
// ❌ MAL: ¿Qué significan estos números?
public double calcularPrecioFinal(double precio, int cantidad) {
    double total = precio * cantidad;
    
    if (cantidad > 10) {
        total *= 0.9;   // ¿Por qué 0.9?
    }
    
    if (total > 1000) {
        total -= 50;    // ¿Por qué 50?
    }
    
    total *= 1.21;      // ¿Qué es 1.21?
    
    if (total > 5000) {
        total += 150;   // ¿Y esto?
    }
    
    return total;
}
```

**Refactorización**: Usar constantes con nombres descriptivos.

```java
// ✓ BIEN: Constantes autoexplicativas
public class CalculadoraPrecios {
    private static final int UMBRAL_DESCUENTO_CANTIDAD = 10;
    private static final double DESCUENTO_POR_CANTIDAD = 0.10;
    
    private static final double UMBRAL_DESCUENTO_MONTO = 1000.0;
    private static final double DESCUENTO_FIJO_MONTO_ALTO = 50.0;
    
    private static final double TASA_IVA = 0.21;
    
    private static final double UMBRAL_ENVIO_ESPECIAL = 5000.0;
    private static final double COSTO_ENVIO_ESPECIAL = 150.0;
    
    public double calcularPrecioFinal(double precio, int cantidad) {
        double total = precio * cantidad;
        
        if (cantidad > UMBRAL_DESCUENTO_CANTIDAD) {
            total *= (1 - DESCUENTO_POR_CANTIDAD);
        }
        
        if (total > UMBRAL_DESCUENTO_MONTO) {
            total -= DESCUENTO_FIJO_MONTO_ALTO;
        }
        
        total *= (1 + TASA_IVA);
        
        if (total > UMBRAL_ENVIO_ESPECIAL) {
            total += COSTO_ENVIO_ESPECIAL;
        }
        
        return total;
    }
}
```

(comentarios-que-mienten)=
### Comments (Comentarios Innecesarios o Engañosos)

Los comentarios que explican "qué hace el código" suelen indicar que el código no es claro. Peor aún son los comentarios desactualizados que mienten.

```java
// ❌ MAL: Comentarios que compensan código poco claro
public class Procesador {
    
    // Procesa el pedido
    public void proc(Ped p) {
        // Verificar que el cliente existe
        if (getCl(p.cId) == null) {
            throw new Ex("Cliente no encontrado");
        }
        
        // Calcular el total
        double t = 0;
        for (Lin l : p.lins) {
            // Multiplicar precio por cantidad
            t += l.pr * l.ct;
        }
        
        // Aplicar descuento si corresponde (NOTA: esto ya no se usa,
        // el descuento se calcula en otro lado, pero dejamos el código
        // por las dudas)
        // if (t > 100) {
        //     t = t * 0.9;
        // }
        
        // Agregar IVA (21%)
        t = t * 1.21;
        
        // Guardar
        save(p, t);
    }
}
```

**Refactorización**: Código que se explica solo, sin comentarios innecesarios.

```java
// ✓ BIEN: Código autoexplicativo
public class ProcesadorPedidos {
    private final RepositorioClientes clientes;
    private final CalculadoraImpuestos calculadora;
    private final RepositorioPedidos repositorio;
    
    public void procesar(Pedido pedido) {
        validarClienteExiste(pedido.getClienteId());
        
        double subtotal = calcularSubtotal(pedido.getLineas());
        double total = calculadora.aplicarIva(subtotal);
        
        repositorio.guardar(pedido, total);
    }
    
    private void validarClienteExiste(String clienteId) {
        if (!clientes.existe(clienteId)) {
            throw new ClienteNoEncontradoException(clienteId);
        }
    }
    
    private double calcularSubtotal(List<LineaPedido> lineas) {
        return lineas.stream()
            .mapToDouble(LineaPedido::getTotal)
            .sum();
    }
}
```

:::{note}
Los comentarios son útiles para explicar **por qué** (decisiones de diseño, contexto del negocio), no **qué** (eso debe ser obvio del código). También son útiles para documentar APIs públicas (Javadoc).
:::

---

(antipatrones-clasicos)=
## Anti-patrones Clásicos de Diseño

(spaghetti-code)=
### Spaghetti Code

Código sin estructura clara, con flujo de control enredado y dependencias cruzadas.

```java
// ❌ MAL: Spaghetti - todo depende de todo
public class App {
    static Usuario usuarioActual;
    static Connection conexion;
    static boolean modoDebug = true;
    
    public static void main(String[] args) {
        try {
            conexion = DriverManager.getConnection("jdbc:...");
            procesarComando(args[0]);
        } catch (Exception e) {
            if (modoDebug) e.printStackTrace();
            System.exit(1);
        }
    }
    
    static void procesarComando(String cmd) throws Exception {
        if (cmd.equals("login")) {
            // 50 líneas de código de login inline
            // que modifican usuarioActual, usan conexion, etc.
        } else if (cmd.equals("listar")) {
            if (usuarioActual == null) {
                System.out.println("Debe loguearse primero");
                procesarComando("login"); // recursión problemática
            }
            // 80 líneas de código de listado
        } else if (cmd.equals("comprar")) {
            // Más código que depende del estado global...
        }
        // ... 20 else-if más
    }
}
```

**Refactorización**: Estructura clara con separación de responsabilidades (ver capítulo de Arquitectura).

(golden-hammer)=
### Golden Hammer (Martillo de Oro)

Usar la misma solución para todos los problemas porque es la que conocemos.

```java
// ❌ MAL: Usar herencia para TODO
public class Persona { }
public class Estudiante extends Persona { }
public class EstudianteRegular extends Estudiante { }
public class EstudianteBecado extends EstudianteRegular { }
public class EstudianteBecadoCompleto extends EstudianteBecado { }
public class EstudianteBecadoParcial extends EstudianteBecado { }
public class EstudianteInternacional extends Estudiante { }
public class EstudianteInternacionalBecado extends EstudianteInternacional { }
// La jerarquía sigue creciendo...

// Problema: ¿qué pasa con un estudiante internacional con beca parcial?
```

**Solución**: Usar la herramienta adecuada para cada problema.

```java
// ✓ BIEN: Composición donde corresponde
public class Estudiante {
    private final Persona datosPersonales;
    private final TipoResidencia residencia;  // LOCAL, INTERNACIONAL
    private final Beca beca;  // puede ser null, o BecaCompleta, BecaParcial...
    
    public boolean tieneBeca() {
        return beca != null;
    }
    
    public double calcularArancel() {
        double base = residencia.getArancelBase();
        if (beca != null) {
            return beca.aplicarDescuento(base);
        }
        return base;
    }
}
```

(lava-flow)=
### Lava Flow (Flujo de Lava)

Código muerto o de propósito desconocido que nadie se atreve a eliminar.

```java
// ❌ MAL: Código "por las dudas"
public class Procesador {
    
    // TODO: revisar si esto todavía se usa (comentario de 2015)
    @Deprecated
    public void metodoViejo() {
        // ...
    }
    
    // Versión 2 del método (la 1 está arriba)
    public void metodoNuevo() {
        // ...
    }
    
    // Copia de seguridad antes del refactor de Juan
    public void metodoNuevo_backup() {
        // ...
    }
    
    // No borrar - lo usa el sistema legacy (¿cuál?)
    public void metodoMisterioso() {
        // 200 líneas de código que nadie entiende
    }
}
```

**Solución**: 
1. Usar control de versiones (Git) en lugar de comentarios y backups
2. Eliminar código muerto — siempre se puede recuperar del historial
3. Documentar decisiones en el momento, no después

(copy-paste-programming)=
### Copy-Paste Programming

Resolver problemas copiando y modificando código existente en lugar de abstraer.

```java
// ❌ MAL: Copiar y pegar con pequeños cambios
public void exportarClientesExcel() {
    Workbook wb = new HSSFWorkbook();
    Sheet sheet = wb.createSheet("Clientes");
    
    Row header = sheet.createRow(0);
    header.createCell(0).setCellValue("ID");
    header.createCell(1).setCellValue("Nombre");
    header.createCell(2).setCellValue("Email");
    
    int rowNum = 1;
    for (Cliente c : clientes) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(c.getId());
        row.createCell(1).setCellValue(c.getNombre());
        row.createCell(2).setCellValue(c.getEmail());
    }
    
    FileOutputStream out = new FileOutputStream("clientes.xls");
    wb.write(out);
    out.close();
}

public void exportarProductosExcel() {
    Workbook wb = new HSSFWorkbook();
    Sheet sheet = wb.createSheet("Productos");  // Solo cambia esto
    
    Row header = sheet.createRow(0);
    header.createCell(0).setCellValue("Código");  // y esto
    header.createCell(1).setCellValue("Nombre");
    header.createCell(2).setCellValue("Precio");  // y esto
    
    int rowNum = 1;
    for (Producto p : productos) {  // y esto
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(p.getCodigo());  // y esto
        row.createCell(1).setCellValue(p.getNombre());
        row.createCell(2).setCellValue(p.getPrecio());  // y esto
    }
    
    FileOutputStream out = new FileOutputStream("productos.xls");  // y esto
    wb.write(out);
    out.close();
}
// Y 10 métodos más casi idénticos...
```

**Refactorización**: Abstraer la lógica común.

```java
// ✓ BIEN: Una abstracción para todas las exportaciones
public interface Exportable {
    String[] getEncabezados();
    Object[] getValores();
}

public class Cliente implements Exportable {
    @Override
    public String[] getEncabezados() {
        return new String[]{"ID", "Nombre", "Email"};
    }
    
    @Override
    public Object[] getValores() {
        return new Object[]{id, nombre, email};
    }
}

public class ExportadorExcel {
    
    public void exportar(String nombreHoja, List<? extends Exportable> items, 
                         Path destino) throws IOException {
        try (Workbook wb = new HSSFWorkbook()) {
            Sheet sheet = wb.createSheet(nombreHoja);
            
            if (!items.isEmpty()) {
                crearEncabezado(sheet, items.get(0).getEncabezados());
                crearFilas(sheet, items);
            }
            
            try (OutputStream out = Files.newOutputStream(destino)) {
                wb.write(out);
            }
        }
    }
    
    private void crearEncabezado(Sheet sheet, String[] titulos) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < titulos.length; i++) {
            header.createCell(i).setCellValue(titulos[i]);
        }
    }
    
    private void crearFilas(Sheet sheet, List<? extends Exportable> items) {
        int rowNum = 1;
        for (Exportable item : items) {
            Row row = sheet.createRow(rowNum++);
            Object[] valores = item.getValores();
            for (int i = 0; i < valores.length; i++) {
                setCellValue(row.createCell(i), valores[i]);
            }
        }
    }
    
    private void setCellValue(Cell cell, Object valor) {
        if (valor instanceof Number) {
            cell.setCellValue(((Number) valor).doubleValue());
        } else {
            cell.setCellValue(String.valueOf(valor));
        }
    }
}
```

---

(jerarquias-problematicas)=
## Anti-patrones en Jerarquías

(yo-yo-problem)=
### Yo-Yo Problem

Jerarquías donde hay que "subir y bajar" constantemente para entender el flujo.

```java
// ❌ MAL: Para entender D, hay que leer A, B, C y D
public abstract class A {
    public void proceso() {
        paso1();
        paso2();
        paso3();
    }
    protected abstract void paso1();
    protected void paso2() { /* algo */ }
    protected abstract void paso3();
}

public abstract class B extends A {
    @Override
    protected void paso1() {
        subpaso1a();
        subpaso1b();
    }
    protected abstract void subpaso1a();
    protected void subpaso1b() { /* algo */ }
}

public abstract class C extends B {
    @Override
    protected void subpaso1a() {
        prepararSubpaso();
        ejecutarSubpaso();
    }
    protected abstract void prepararSubpaso();
    protected abstract void ejecutarSubpaso();
    
    @Override
    protected void paso3() {
        subpaso3a();
    }
    protected abstract void subpaso3a();
}

public class D extends C {
    @Override
    protected void prepararSubpaso() { /* finalmente, código real */ }
    @Override
    protected void ejecutarSubpaso() { /* más código real */ }
    @Override
    protected void subpaso3a() { /* y más */ }
}
```

**Solución**: Preferir composición sobre herencia; jerarquías poco profundas.

(circle-ellipse-problem)=
### Circle-Ellipse Problem (Problema Círculo-Elipse)

Herencia que parece correcta matemáticamente pero viola LSP.

```java
// ❌ MAL: Un círculo "es" una elipse, ¿no?
public class Elipse {
    protected double radioMayor;
    protected double radioMenor;
    
    public void setRadioMayor(double r) { this.radioMayor = r; }
    public void setRadioMenor(double r) { this.radioMenor = r; }
    
    public double getArea() {
        return Math.PI * radioMayor * radioMenor;
    }
}

public class Circulo extends Elipse {
    @Override
    public void setRadioMayor(double r) {
        this.radioMayor = r;
        this.radioMenor = r;  // Mantener iguales
    }
    
    @Override
    public void setRadioMenor(double r) {
        this.radioMayor = r;  // Mantener iguales
        this.radioMenor = r;
    }
}

// Problema:
Elipse e = new Circulo();
e.setRadioMayor(10);
e.setRadioMenor(5);
// ¡El código que usa Elipse espera radios diferentes!
// Circulo viola las expectativas de Elipse
```

**Solución**: No usar herencia si la subclase restringe el comportamiento de la superclase.

```java
// ✓ BIEN: Interfaces comunes sin herencia problemática
public interface Forma {
    double getArea();
    double getPerimetro();
}

public class Elipse implements Forma {
    private final double radioMayor;
    private final double radioMenor;
    
    public Elipse(double radioMayor, double radioMenor) {
        this.radioMayor = radioMayor;
        this.radioMenor = radioMenor;
    }
    
    @Override
    public double getArea() {
        return Math.PI * radioMayor * radioMenor;
    }
    // ...
}

public class Circulo implements Forma {
    private final double radio;
    
    public Circulo(double radio) {
        this.radio = radio;
    }
    
    @Override
    public double getArea() {
        return Math.PI * radio * radio;
    }
    // ...
}
```

---

(detectando-smells)=
## Cómo Detectar Code Smells

(heuristicas-deteccion)=
### Heurísticas de Detección

```
┌─────────────────────────────────────────────────────────────────┐
│               SEÑALES DE ALERTA                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   NOMBRES                                                       │
│   ─────────                                                     │
│   • Clases con "Manager", "Processor", "Handler", "Helper"      │
│   • Métodos con "And" en el nombre (hacen más de una cosa)      │
│   • Variables de una letra (excepto en lazos cortos)            │
│   • Nombres que no explican el propósito                        │
│                                                                 │
│   TAMAÑOS                                                       │
│   ───────                                                       │
│   • Clases > 300-500 líneas                                     │
│   • Métodos > 20-30 líneas                                      │
│   • Más de 3-4 niveles de indentación                           │
│   • Más de 3-4 parámetros en un método                          │
│                                                                 │
│   ESTRUCTURA                                                    │
│   ──────────                                                    │
│   • Muchos if/else anidados o en cascada                        │
│   • Switch/case que se repiten en varios lugares                │
│   • Try/catch muy grandes                                       │
│   • Código comentado                                            │
│                                                                 │
│   DEPENDENCIAS                                                  │
│   ─────────────                                                 │
│   • Muchos imports                                              │
│   • Acceso frecuente a campos de otros objetos                  │
│   • Dependencias circulares entre clases                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

(herramientas)=
### Herramientas de Análisis

Existen herramientas que detectan smells automáticamente:

| Herramienta | Tipo | Detecta |
|-------------|------|---------|
| **SonarQube** | Análisis estático | Smells, bugs, vulnerabilidades |
| **PMD** | Análisis estático | Código muerto, complejidad |
| **Checkstyle** | Estilo | Convenciones, tamaños |
| **IntelliJ IDEA** | IDE | Inspecciones, sugerencias |
| **JDeodorant** | Eclipse plugin | Smells específicos de diseño |

Sin embargo, **el criterio humano es irremplazable**. Las herramientas ayudan, pero la experiencia y el contexto son fundamentales.

---

(prevencion)=
## Prevención: Evitar Smells desde el Inicio

(practicas-preventivas)=
### Prácticas Preventivas

1. **Revisiones de código (Code Reviews)**: Otra persona detecta lo que nosotros no vemos
2. **TDD**: El código difícil de testear suele tener smells
3. **Refactoring continuo**: Pequeñas mejoras constantes
4. **Pair Programming**: Dos cabezas detectan más problemas
5. **Estándares de equipo**: Convenciones claras y compartidas

(regla-boy-scout)=
### La Regla del Boy Scout

:::{tip} Regla del Boy Scout

"Dejá el código mejor de lo que lo encontraste."

Cada vez que tocás un archivo, hacé una pequeña mejora: renombrá una variable, extraé un método, eliminá código muerto. Las mejoras pequeñas y constantes previenen la acumulación de deuda técnica.
:::

---

(resumen-antipatrones)=
## Resumen

:::{tip} Conceptos Clave

1. **Anti-patrones** son soluciones que parecen buenas pero causan problemas
2. **Code smells** son síntomas de problemas de diseño más profundos
3. **God Class** y **Data Class** son smells de clase comunes
4. **Long Method** y **Feature Envy** son smells de método frecuentes
5. **Código duplicado** es el smell más dañino
6. **Números mágicos** y **comentarios excesivos** dificultan la comprensión
7. **La prevención** es más barata que la corrección
8. **Refactorizar continuamente** evita la acumulación de problemas
:::

```
┌─────────────────────────────────────────────────────────────────┐
│          TAXONOMÍA DE SMELLS Y SOLUCIONES                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   SMELL                    │  SOLUCIÓN                          │
│   ────────────────────────────────────────────────────          │
│   God Class                │  Extract Class, SRP                │
│   Data Class               │  Move Method, encapsular           │
│   Long Method              │  Extract Method                    │
│   Long Parameter List      │  Introduce Parameter Object        │
│   Feature Envy             │  Move Method                       │
│   Duplicate Code           │  Extract Method/Class, Template    │
│   Magic Numbers            │  Extract Constant                  │
│   Comments                 │  Rename, Extract Method            │
│   Primitive Obsession      │  Replace with Object               │
│   Switch Statements        │  Replace with Polymorphism         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

````{exercise}
:label: ej-identificar-smells

Identificá al menos 5 code smells en el siguiente código y explicá cómo los refactorizarías:

```java
public class S {
    public double c(int t, double p, int q, boolean d, String cod) {
        double r = p * q;
        // aplicar descuento
        if (d) {
            r = r * 0.9;
        }
        // verificar codigo promocional
        if (cod != null) {
            if (cod.equals("PROMO10")) {
                r = r * 0.9;
            } else if (cod.equals("PROMO20")) {
                r = r * 0.8;
            } else if (cod.equals("PROMO50")) {
                r = r * 0.5;
            }
        }
        // calcular impuesto
        if (t == 1) {
            r = r * 1.21;
        } else if (t == 2) {
            r = r * 1.105;
        } else if (t == 3) {
            r = r * 1.0;
        }
        return r;
    }
}
```
````

````{solution} ej-identificar-smells
:class: dropdown

**Smells identificados:**

1. **Nombres crípticos**: `S`, `c`, `t`, `p`, `q`, `d`, `cod`, `r` no dicen nada
2. **Números mágicos**: 0.9, 0.8, 0.5, 1.21, 1.105, 1, 2, 3
3. **Comentarios que compensan código poco claro**: Los comentarios explican lo que deberían decir los nombres
4. **Long Parameter List**: 5 parámetros, algunos relacionados
5. **Primitive Obsession**: `int t` debería ser un enum, `String cod` debería ser un tipo
6. **Switch/if en cascada**: Los códigos promocionales y tipos de impuesto

**Refactorización:**

```java
public enum TipoImpuesto {
    IVA_GENERAL(0.21),
    IVA_REDUCIDO(0.105),
    EXENTO(0.0);
    
    private final double tasa;
    TipoImpuesto(double tasa) { this.tasa = tasa; }
    public double aplicar(double monto) { return monto * (1 + tasa); }
}

public enum CodigoPromocion {
    PROMO10(0.10),
    PROMO20(0.20),
    PROMO50(0.50),
    NINGUNO(0.0);
    
    private final double descuento;
    CodigoPromocion(double descuento) { this.descuento = descuento; }
    
    public double aplicar(double monto) {
        return monto * (1 - descuento);
    }
    
    public static CodigoPromocion desde(String codigo) {
        if (codigo == null) return NINGUNO;
        try {
            return valueOf(codigo);
        } catch (IllegalArgumentException e) {
            return NINGUNO;
        }
    }
}

public class CalculadoraPrecio {
    private static final double DESCUENTO_CLIENTE_FRECUENTE = 0.10;
    
    public double calcularPrecioFinal(
            TipoImpuesto tipoImpuesto,
            double precioUnitario,
            int cantidad,
            boolean esClienteFrecuente,
            String codigoPromocion) {
        
        double subtotal = precioUnitario * cantidad;
        
        if (esClienteFrecuente) {
            subtotal *= (1 - DESCUENTO_CLIENTE_FRECUENTE);
        }
        
        CodigoPromocion promo = CodigoPromocion.desde(codigoPromocion);
        subtotal = promo.aplicar(subtotal);
        
        return tipoImpuesto.aplicar(subtotal);
    }
}
```
````

````{exercise}
:label: ej-refactorizar-god-class

La siguiente clase es una God Class. Identificá las responsabilidades separadas y proponé una refactorización con al menos 3 clases.

```java
public class Tienda {
    private Map<String, Producto> productos = new HashMap<>();
    private Map<String, Cliente> clientes = new HashMap<>();
    private List<Venta> ventas = new ArrayList<>();
    private double dineroEnCaja;
    
    public void agregarProducto(String cod, String nom, double precio, int stock) { }
    public void actualizarStock(String cod, int cantidad) { }
    public Producto buscarProducto(String cod) { }
    
    public void registrarCliente(String id, String nombre, String email) { }
    public Cliente buscarCliente(String id) { }
    public void enviarEmailPromocional(String clienteId, String mensaje) { }
    
    public void realizarVenta(String clienteId, List<String> codProductos) { }
    public double calcularTotalVenta(List<String> codProductos) { }
    public void aplicarDescuento(String ventaId, double porcentaje) { }
    
    public void abrirCaja(double montoInicial) { }
    public void cerrarCaja() { }
    public double consultarSaldo() { }
    
    public void generarReporteVentas(Date desde, Date hasta) { }
    public void imprimirTicket(String ventaId) { }
}
```
````

````{solution} ej-refactorizar-god-class
:class: dropdown

**Responsabilidades identificadas:**
1. Gestión de productos (CRUD de productos, stock)
2. Gestión de clientes (CRUD de clientes, comunicación)
3. Procesamiento de ventas (crear venta, calcular total, descuentos)
4. Gestión de caja (apertura, cierre, saldo)
5. Reportes e impresión

**Refactorización propuesta:**

```java
// Gestión de productos
public class CatalogoProductos {
    private final Map<String, Producto> productos = new HashMap<>();
    
    public void agregar(Producto producto) { }
    public void actualizarStock(String codigo, int cantidad) { }
    public Optional<Producto> buscar(String codigo) { }
    public List<Producto> listarTodos() { }
}

// Gestión de clientes
public class RegistroClientes {
    private final Map<String, Cliente> clientes = new HashMap<>();
    private final ServicioEmail servicioEmail;
    
    public void registrar(Cliente cliente) { }
    public Optional<Cliente> buscar(String id) { }
    public void enviarPromocion(String clienteId, String mensaje) { }
}

// Procesamiento de ventas
public class ServicioVentas {
    private final CatalogoProductos catalogo;
    private final RegistroClientes clientes;
    private final RepositorioVentas repositorio;
    private final Caja caja;
    
    public Venta realizar(String clienteId, List<String> codigosProductos) { }
    public void aplicarDescuento(String ventaId, double porcentaje) { }
}

// Caja
public class Caja {
    private double saldo;
    private boolean abierta;
    
    public void abrir(double montoInicial) { }
    public void registrarIngreso(double monto) { }
    public void cerrar() { }
    public double getSaldo() { }
}

// Reportes
public class GeneradorReportes {
    private final RepositorioVentas ventas;
    private final Impresora impresora;
    
    public Reporte ventasPorPeriodo(Date desde, Date hasta) { }
    public void imprimirTicket(String ventaId) { }
}

// La "fachada" simplificada
public class Tienda {
    private final CatalogoProductos catalogo;
    private final RegistroClientes clientes;
    private final ServicioVentas ventas;
    private final Caja caja;
    private final GeneradorReportes reportes;
    
    // Métodos que delegan a los componentes apropiados
}
```
````

---

## Lecturas Recomendadas

- Fowler, Martin. "Refactoring: Improving the Design of Existing Code"
- Brown et al. "AntiPatterns: Refactoring Software, Architectures, and Projects in Crisis"
- Martin, Robert C. "Clean Code: A Handbook of Agile Software Craftsmanship"
- Wake, William. "Refactoring Workbook"
