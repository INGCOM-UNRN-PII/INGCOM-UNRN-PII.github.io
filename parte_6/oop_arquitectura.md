---
title: "OOP 9: Arquitectura de Software Orientada a Objetos"
subtitle: "Organización de Sistemas a Gran Escala"
subject: Programación Orientada a Objetos
---

(oop9-arquitectura)=
# OOP 9: Arquitectura de Software Orientada a Objetos

Hasta ahora aprendimos a diseñar clases individuales, establecer relaciones entre objetos, aplicar patrones y principios SOLID, y verificar nuestro código con tests. Pero cuando un sistema crece, surge una pregunta fundamental: **¿cómo organizamos cientos o miles de clases de manera coherente?**

La **arquitectura de software** se ocupa de las decisiones de diseño a gran escala: cómo dividimos el sistema en partes, cómo se comunican esas partes, y qué restricciones imponemos para mantener la mantenibilidad a largo plazo.

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, serás capaz de:

1. Comprender qué es la arquitectura de software y por qué importa
2. Aplicar el concepto de capas para organizar sistemas
3. Separar responsabilidades entre presentación, lógica y datos
4. Diseñar interfaces claras entre módulos
5. Evaluar trade-offs arquitectónicos comunes
6. Relacionar arquitectura con los principios SOLID a nivel macro
:::

---

(que-es-arquitectura)=
## ¿Qué es la Arquitectura de Software?

(definicion-arquitectura)=
### Definición

:::{important} Definición

**Arquitectura de software** es el conjunto de estructuras necesarias para razonar sobre un sistema, que comprende elementos de software, las relaciones entre ellos, y las propiedades de ambos.

— Bass, Clements & Kazman, "Software Architecture in Practice"
:::

La arquitectura responde preguntas como:

- ¿Cuáles son los principales componentes del sistema?
- ¿Cómo se comunican entre sí?
- ¿Qué puede cambiar fácilmente y qué es difícil de modificar?
- ¿Cómo escala el sistema cuando crece?

```
┌─────────────────────────────────────────────────────────────────┐
│                 NIVELES DE DISEÑO                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │                    ARQUITECTURA                         │   │
│   │   • División en módulos/componentes                     │   │
│   │   • Decisiones difíciles de revertir                    │   │
│   │   • Afecta a todo el equipo                             │   │
│   └─────────────────────────────────────────────────────────┘   │
│                           │                                     │
│                           ▼                                     │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │                  DISEÑO DETALLADO                       │   │
│   │   • Patrones de diseño                                  │   │
│   │   • Relaciones entre clases                             │   │
│   │   • Principios SOLID                                    │   │
│   └─────────────────────────────────────────────────────────┘   │
│                           │                                     │
│                           ▼                                     │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │                   IMPLEMENTACIÓN                        │   │
│   │   • Código concreto                                     │   │
│   │   • Algoritmos                                          │   │
│   │   • Estructuras de datos                                │   │
│   └─────────────────────────────────────────────────────────┘   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

(importancia-arquitectura)=
### ¿Por qué Importa la Arquitectura?

Las decisiones arquitectónicas tienen **consecuencias de largo alcance**:

| Decisión | Consecuencia |
|----------|--------------|
| Mezclar UI con lógica de negocio | Imposible cambiar la interfaz sin tocar la lógica |
| Acoplar fuertemente a una base de datos | Imposible cambiar de proveedor o testear sin BD |
| No definir límites claros | El sistema se convierte en un "gran bola de barro" |
| Sobre-arquitectura prematura | Complejidad innecesaria, desarrollo lento |

:::{important}
La arquitectura es sobre **trade-offs**. No existe una arquitectura "correcta" universal, solo arquitecturas más o menos apropiadas para un contexto específico.
:::

---

(arquitectura-capas)=
## Arquitectura en Capas

(concepto-capas)=
### El Concepto de Capas

La **arquitectura en capas** (layered architecture) es el estilo arquitectónico más común y fundamental. Consiste en organizar el código en niveles jerárquicos, donde cada capa:

1. Tiene una responsabilidad específica
2. Solo conoce y usa la capa inmediatamente inferior
3. No conoce las capas superiores

```
┌─────────────────────────────────────────────────────────────────┐
│               ARQUITECTURA EN CAPAS CLÁSICA                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │                  PRESENTACIÓN                           │   │
│   │   • Interfaz de usuario (consola, GUI, web)             │   │
│   │   • Formateo de datos para mostrar                      │   │
│   │   • Captura de entrada del usuario                      │   │
│   └────────────────────────┬────────────────────────────────┘   │
│                            │ usa                                │
│                            ▼                                    │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │                  LÓGICA DE NEGOCIO                      │   │
│   │   • Reglas del dominio                                  │   │
│   │   • Cálculos y validaciones                             │   │
│   │   • Flujos de trabajo                                   │   │
│   └────────────────────────┬────────────────────────────────┘   │
│                            │ usa                                │
│                            ▼                                    │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │                  ACCESO A DATOS                         │   │
│   │   • Persistencia (archivos, bases de datos)             │   │
│   │   • Consultas y almacenamiento                          │   │
│   │   • Conversión entre formatos                           │   │
│   └─────────────────────────────────────────────────────────┘   │
│                                                                 │
│   REGLA: Las flechas solo van hacia abajo                       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

(beneficios-capas)=
### Beneficios de las Capas

1. **Separación de concerns**: Cada capa tiene una responsabilidad clara
2. **Sustituibilidad**: Podemos cambiar una capa sin afectar las otras
3. **Testeabilidad**: Podemos probar cada capa de forma aislada
4. **Reutilización**: La lógica de negocio puede usarse con diferentes interfaces
5. **Desarrollo paralelo**: Diferentes equipos pueden trabajar en diferentes capas

---

(ejemplo-sin-capas)=
## Ejemplo: Sistema sin Arquitectura

(problema-mezclado)=
### El Problema del Código Mezclado

Veamos un ejemplo típico de código sin separación arquitectónica:

```java
// ❌ MAL: Todo mezclado en una sola clase
public class GestorVentas {
    private Scanner scanner = new Scanner(System.in);
    
    public void procesarVenta() {
        // Presentación: capturar entrada
        System.out.print("Ingrese código de producto: ");
        String codigo = scanner.nextLine();
        
        System.out.print("Ingrese cantidad: ");
        int cantidad = Integer.parseInt(scanner.nextLine());
        
        // Acceso a datos: leer archivo
        double precio = 0;
        try (BufferedReader br = new BufferedReader(
                new FileReader("productos.csv"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes[0].equals(codigo)) {
                    precio = Double.parseDouble(partes[2]);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo archivo");
            return;
        }
        
        // Lógica de negocio: calcular total
        double subtotal = precio * cantidad;
        double descuento = 0;
        if (cantidad > 10) {
            descuento = subtotal * 0.1;
        }
        double iva = (subtotal - descuento) * 0.21;
        double total = subtotal - descuento + iva;
        
        // Presentación: mostrar resultado
        System.out.println("========================");
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("Descuento: $" + descuento);
        System.out.println("IVA: $" + iva);
        System.out.println("TOTAL: $" + total);
        
        // Acceso a datos: guardar venta
        try (PrintWriter pw = new PrintWriter(
                new FileWriter("ventas.csv", true))) {
            pw.println(codigo + "," + cantidad + "," + total);
        } catch (IOException e) {
            System.out.println("Error guardando venta");
        }
    }
}
```

**Problemas de este diseño**:

1. **Imposible testear sin I/O**: Cada test necesita archivos reales y consola
2. **Imposible reutilizar la lógica**: Si queremos una GUI, hay que reescribir todo
3. **Difícil de modificar**: Cambiar el formato de archivo afecta a todo
4. **Difícil de entender**: 60 líneas que hacen de todo

---

(ejemplo-con-capas)=
## Refactorización: Aplicando Capas

(capa-dominio)=
### Capa 1: Dominio (Lógica de Negocio)

Empezamos por el **núcleo**: las reglas del negocio, libres de cualquier detalle técnico.

```java
/**
 * Representa un producto del catálogo.
 * Capa: Dominio
 */
public class Producto {
    private final String codigo;
    private final String nombre;
    private final double precio;
    
    public Producto(String codigo, String nombre, double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException(
                "El precio no puede ser negativo");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }
    
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
}
```

```java
/**
 * Representa una línea de venta con su cálculo.
 * Capa: Dominio
 */
public class LineaVenta {
    private final Producto producto;
    private final int cantidad;
    
    private static final double UMBRAL_DESCUENTO = 10;
    private static final double PORCENTAJE_DESCUENTO = 0.10;
    private static final double PORCENTAJE_IVA = 0.21;
    
    public LineaVenta(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                "La cantidad debe ser positiva");
        }
        this.producto = producto;
        this.cantidad = cantidad;
    }
    
    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }
    
    public double getDescuento() {
        if (cantidad > UMBRAL_DESCUENTO) {
            return getSubtotal() * PORCENTAJE_DESCUENTO;
        }
        return 0;
    }
    
    public double getIva() {
        return getBaseImponible() * PORCENTAJE_IVA;
    }
    
    public double getBaseImponible() {
        return getSubtotal() - getDescuento();
    }
    
    public double getTotal() {
        return getBaseImponible() + getIva();
    }
    
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
}
```

```java
/**
 * Representa una venta completa.
 * Capa: Dominio
 */
public class Venta {
    private final List<LineaVenta> lineas;
    private final LocalDateTime fecha;
    
    public Venta() {
        this.lineas = new ArrayList<>();
        this.fecha = LocalDateTime.now();
    }
    
    public void agregarLinea(LineaVenta linea) {
        lineas.add(linea);
    }
    
    public double getTotal() {
        return lineas.stream()
            .mapToDouble(LineaVenta::getTotal)
            .sum();
    }
    
    public List<LineaVenta> getLineas() {
        return Collections.unmodifiableList(lineas);
    }
    
    public LocalDateTime getFecha() { return fecha; }
}
```

:::{note}
Observá que estas clases **no conocen**:
- Cómo se captura la entrada (consola, GUI, web)
- Cómo se almacenan los datos (archivos, base de datos)
- Cómo se muestra el resultado

Son **puras**: solo contienen reglas de negocio.
:::

(capa-datos)=
### Capa 2: Acceso a Datos

Definimos **interfaces** para abstraer la persistencia:

```java
/**
 * Contrato para acceder a productos.
 * Capa: Acceso a Datos (interfaz)
 */
public interface RepositorioProductos {
    Optional<Producto> buscarPorCodigo(String codigo);
    List<Producto> listarTodos();
}
```

```java
/**
 * Contrato para almacenar ventas.
 * Capa: Acceso a Datos (interfaz)
 */
public interface RepositorioVentas {
    void guardar(Venta venta);
    List<Venta> listarTodas();
}
```

Luego, implementaciones concretas:

```java
/**
 * Implementación que lee productos de un archivo CSV.
 * Capa: Acceso a Datos (implementación)
 */
public class RepositorioProductosCsv implements RepositorioProductos {
    private final Path archivo;
    
    public RepositorioProductosCsv(Path archivo) {
        this.archivo = archivo;
    }
    
    @Override
    public Optional<Producto> buscarPorCodigo(String codigo) {
        return listarTodos().stream()
            .filter(p -> p.getCodigo().equals(codigo))
            .findFirst();
    }
    
    @Override
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(archivo)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                productos.add(new Producto(
                    partes[0], 
                    partes[1], 
                    Double.parseDouble(partes[2])
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException(
                "Error leyendo productos: " + e.getMessage(), e);
        }
        return productos;
    }
}
```

```java
/**
 * Implementación en memoria para testing.
 * Capa: Acceso a Datos (implementación)
 */
public class RepositorioProductosEnMemoria implements RepositorioProductos {
    private final Map<String, Producto> productos = new HashMap<>();
    
    public void agregar(Producto producto) {
        productos.put(producto.getCodigo(), producto);
    }
    
    @Override
    public Optional<Producto> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(productos.get(codigo));
    }
    
    @Override
    public List<Producto> listarTodos() {
        return new ArrayList<>(productos.values());
    }
}
```

(capa-servicio)=
### Capa 2.5: Servicios de Aplicación

Entre la presentación y el dominio, colocamos **servicios** que orquestan los casos de uso:

```java
/**
 * Servicio que orquesta el proceso de venta.
 * Capa: Servicios de Aplicación
 */
public class ServicioVentas {
    private final RepositorioProductos repoProductos;
    private final RepositorioVentas repoVentas;
    
    public ServicioVentas(RepositorioProductos repoProductos,
                          RepositorioVentas repoVentas) {
        this.repoProductos = repoProductos;
        this.repoVentas = repoVentas;
    }
    
    /**
     * Crea una nueva venta con un producto.
     * @throws ProductoNoEncontradoException si el código no existe
     */
    public Venta crearVenta(String codigoProducto, int cantidad) {
        Producto producto = repoProductos.buscarPorCodigo(codigoProducto)
            .orElseThrow(() -> new ProductoNoEncontradoException(
                "Producto no encontrado: " + codigoProducto));
        
        LineaVenta linea = new LineaVenta(producto, cantidad);
        Venta venta = new Venta();
        venta.agregarLinea(linea);
        
        repoVentas.guardar(venta);
        
        return venta;
    }
}
```

```java
/**
 * Excepción de dominio.
 */
public class ProductoNoEncontradoException extends RuntimeException {
    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
```

(capa-presentacion)=
### Capa 3: Presentación

Finalmente, la capa de presentación, que **solo se ocupa de I/O**:

```java
/**
 * Interfaz de usuario por consola.
 * Capa: Presentación
 */
public class ConsolaVentas {
    private final ServicioVentas servicioVentas;
    private final Scanner scanner;
    
    public ConsolaVentas(ServicioVentas servicioVentas) {
        this.servicioVentas = servicioVentas;
        this.scanner = new Scanner(System.in);
    }
    
    public void ejecutar() {
        try {
            String codigo = pedirTexto("Ingrese código de producto: ");
            int cantidad = pedirEntero("Ingrese cantidad: ");
            
            Venta venta = servicioVentas.crearVenta(codigo, cantidad);
            
            mostrarVenta(venta);
            
        } catch (ProductoNoEncontradoException e) {
            mostrarError(e.getMessage());
        } catch (IllegalArgumentException e) {
            mostrarError("Datos inválidos: " + e.getMessage());
        }
    }
    
    private String pedirTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    
    private int pedirEntero(String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine());
    }
    
    private void mostrarVenta(Venta venta) {
        System.out.println();
        System.out.println("════════════════════════════");
        System.out.println("        TICKET DE VENTA     ");
        System.out.println("════════════════════════════");
        
        for (LineaVenta linea : venta.getLineas()) {
            System.out.printf("%s x%d%n", 
                linea.getProducto().getNombre(),
                linea.getCantidad());
            System.out.printf("  Subtotal:  $%,.2f%n", linea.getSubtotal());
            
            if (linea.getDescuento() > 0) {
                System.out.printf("  Descuento: -$%,.2f%n", linea.getDescuento());
            }
            
            System.out.printf("  IVA:       $%,.2f%n", linea.getIva());
        }
        
        System.out.println("────────────────────────────");
        System.out.printf("  TOTAL:     $%,.2f%n", venta.getTotal());
        System.out.println("════════════════════════════");
    }
    
    private void mostrarError(String mensaje) {
        System.err.println("ERROR: " + mensaje);
    }
}
```

(armando-todo)=
### Armando las Piezas

El punto de entrada conecta todas las capas:

```java
/**
 * Punto de entrada de la aplicación.
 */
public class Main {
    public static void main(String[] args) {
        // Crear capa de datos
        RepositorioProductos repoProductos = 
            new RepositorioProductosCsv(Path.of("productos.csv"));
        RepositorioVentas repoVentas = 
            new RepositorioVentasCsv(Path.of("ventas.csv"));
        
        // Crear capa de servicios
        ServicioVentas servicioVentas = 
            new ServicioVentas(repoProductos, repoVentas);
        
        // Crear capa de presentación
        ConsolaVentas consola = new ConsolaVentas(servicioVentas);
        
        // Ejecutar
        consola.ejecutar();
    }
}
```

```
┌─────────────────────────────────────────────────────────────────┐
│                  DIAGRAMA DE DEPENDENCIAS                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌──────────────┐                                              │
│   │    Main      │ ─────────────────────────────────────┐       │
│   └──────┬───────┘                                      │       │
│          │ crea                                         │       │
│          ▼                                              ▼       │
│   ┌──────────────┐     ┌──────────────┐     ┌──────────────┐   │
│   │ConsolaVentas │     │ServicioVentas│     │RepositorioCsv│   │
│   └──────┬───────┘     └──────┬───────┘     └──────────────┘   │
│          │ usa                │ usa                ▲            │
│          └────────────────────┤                    │            │
│                               ▼                    │            │
│                        ┌──────────────┐            │            │
│                        │<<interface>> │────────────┘            │
│                        │Repositorio   │  implementa             │
│                        └──────────────┘                         │
│                               ▲                                 │
│                               │ usa                             │
│                        ┌──────────────┐                         │
│                        │   Dominio    │                         │
│                        │  (Producto,  │                         │
│                        │   Venta...)  │                         │
│                        └──────────────┘                         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

(beneficios-arquitectura-capas)=
## Beneficios en Acción

(testeabilidad)=
### Testeabilidad

Ahora podemos testear la lógica de negocio **sin archivos ni consola**:

```java
class LineaVentaTest {
    
    @Test
    void calcularSubtotalSinDescuento() {
        Producto producto = new Producto("ABC", "Test", 100.0);
        LineaVenta linea = new LineaVenta(producto, 5);
        
        assertEquals(500.0, linea.getSubtotal(), 0.001);
        assertEquals(0.0, linea.getDescuento(), 0.001);
    }
    
    @Test
    void aplicarDescuentoPorCantidad() {
        Producto producto = new Producto("ABC", "Test", 100.0);
        LineaVenta linea = new LineaVenta(producto, 15);
        
        assertEquals(1500.0, linea.getSubtotal(), 0.001);
        assertEquals(150.0, linea.getDescuento(), 0.001); // 10%
    }
    
    @Test
    void calcularIvaCorrectamente() {
        Producto producto = new Producto("ABC", "Test", 100.0);
        LineaVenta linea = new LineaVenta(producto, 1);
        
        // 100 * 0.21 = 21
        assertEquals(21.0, linea.getIva(), 0.001);
    }
}
```

Y podemos testear el servicio con repositorios **en memoria**:

```java
class ServicioVentasTest {
    
    @Test
    void crearVentaExitosa() {
        // Arrange
        RepositorioProductosEnMemoria repoProductos = 
            new RepositorioProductosEnMemoria();
        repoProductos.agregar(new Producto("ABC", "Laptop", 1000.0));
        
        RepositorioVentasEnMemoria repoVentas = 
            new RepositorioVentasEnMemoria();
        
        ServicioVentas servicio = 
            new ServicioVentas(repoProductos, repoVentas);
        
        // Act
        Venta venta = servicio.crearVenta("ABC", 2);
        
        // Assert
        assertEquals(2420.0, venta.getTotal(), 0.001); // 2000 + 21% IVA
        assertEquals(1, repoVentas.listarTodas().size());
    }
    
    @Test
    void lanzarExcepcionSiProductoNoExiste() {
        RepositorioProductosEnMemoria repoProductos = 
            new RepositorioProductosEnMemoria();
        RepositorioVentasEnMemoria repoVentas = 
            new RepositorioVentasEnMemoria();
        
        ServicioVentas servicio = 
            new ServicioVentas(repoProductos, repoVentas);
        
        assertThrows(ProductoNoEncontradoException.class, () -> {
            servicio.crearVenta("NOEXISTE", 1);
        });
    }
}
```

(sustituibilidad)=
### Sustituibilidad

Cambiar la interfaz de usuario es trivial. Si queremos una GUI:

```java
public class GuiVentas extends JFrame {
    private final ServicioVentas servicioVentas;
    
    public GuiVentas(ServicioVentas servicioVentas) {
        this.servicioVentas = servicioVentas;
        // ... configurar componentes Swing
    }
    
    private void onBotonVender() {
        String codigo = txtCodigo.getText();
        int cantidad = Integer.parseInt(txtCantidad.getText());
        
        try {
            Venta venta = servicioVentas.crearVenta(codigo, cantidad);
            mostrarTicket(venta);
        } catch (ProductoNoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
```

**La lógica de negocio no cambió ni una línea.**

(cambiar-persistencia)=
### Cambiar la Persistencia

Si mañana necesitamos usar una base de datos SQL:

```java
public class RepositorioProductosSql implements RepositorioProductos {
    private final Connection conexion;
    
    public RepositorioProductosSql(Connection conexion) {
        this.conexion = conexion;
    }
    
    @Override
    public Optional<Producto> buscarPorCodigo(String codigo) {
        String sql = "SELECT codigo, nombre, precio FROM productos WHERE codigo = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Producto(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getDouble("precio")
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    // ... resto de métodos
}
```

Solo cambiamos el `Main`:

```java
// Antes
RepositorioProductos repoProductos = 
    new RepositorioProductosCsv(Path.of("productos.csv"));

// Después
Connection conn = DriverManager.getConnection("jdbc:mysql://...");
RepositorioProductos repoProductos = 
    new RepositorioProductosSql(conn);
```

**El servicio y la presentación no cambian.**

---

(inversion-dependencias)=
## Inversión de Dependencias a Nivel Arquitectónico

(problema-dependencias)=
### El Problema de las Dependencias Tradicionales

En una arquitectura "ingenua", las dependencias siguen el flujo de ejecución:

```
┌─────────────────────────────────────────────────────────────────┐
│           DEPENDENCIAS TRADICIONALES (problemático)             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌──────────────┐                                              │
│   │ Presentación │                                              │
│   └──────┬───────┘                                              │
│          │ depende de                                           │
│          ▼                                                      │
│   ┌──────────────┐                                              │
│   │   Dominio    │                                              │
│   └──────┬───────┘                                              │
│          │ depende de   ← PROBLEMA: Dominio depende de          │
│          ▼                         detalles técnicos            │
│   ┌──────────────┐                                              │
│   │  Base Datos  │                                              │
│   │  (concreto)  │                                              │
│   └──────────────┘                                              │
│                                                                 │
│   Si cambia la BD, hay que modificar el Dominio                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

(solucion-inversion)=
### La Solución: Invertir las Dependencias

Aplicamos el **Principio de Inversión de Dependencias (DIP)** a nivel arquitectónico:

```
┌─────────────────────────────────────────────────────────────────┐
│           DEPENDENCIAS INVERTIDAS (correcto)                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌──────────────┐                                              │
│   │ Presentación │                                              │
│   └──────┬───────┘                                              │
│          │ depende de                                           │
│          ▼                                                      │
│   ┌──────────────────────────────────────────────┐              │
│   │              DOMINIO                         │              │
│   │  ┌────────────┐     ┌────────────────────┐   │              │
│   │  │  Entidades │     │  <<interface>>     │   │              │
│   │  │  Servicios │     │  RepositorioXxx    │   │              │
│   │  └────────────┘     └─────────▲──────────┘   │              │
│   │                               │              │              │
│   └───────────────────────────────│──────────────┘              │
│                                   │                             │
│          ┌────────────────────────┘                             │
│          │ implementa                                           │
│          │                                                      │
│   ┌──────┴───────┐                                              │
│   │  Infraestruc │  ← Los detalles dependen de abstracciones    │
│   │  (BD real)   │                                              │
│   └──────────────┘                                              │
│                                                                 │
│   El Dominio define qué necesita (interfaces)                   │
│   La Infraestructura provee implementaciones                    │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

:::{important}
**La regla de oro**: El dominio (las reglas de negocio) **no debe depender de nada externo**. Todo lo externo (UI, bases de datos, frameworks) depende del dominio a través de interfaces.
:::

---

(paquetes-java)=
## Organización en Paquetes (Java)

(estructura-paquetes)=
### Estructura de Paquetes Recomendada

En Java, organizamos las capas en paquetes:

```
src/
├── main/
│   └── java/
│       └── ar/
│           └── edu/
│               └── unrn/
│                   └── ventas/
│                       │
│                       ├── dominio/           ← Capa de Dominio
│                       │   ├── Producto.java
│                       │   ├── LineaVenta.java
│                       │   ├── Venta.java
│                       │   └── RepositorioProductos.java  (interfaz)
│                       │
│                       ├── aplicacion/        ← Servicios de Aplicación
│                       │   ├── ServicioVentas.java
│                       │   └── ProductoNoEncontradoException.java
│                       │
│                       ├── infraestructura/   ← Implementaciones técnicas
│                       │   ├── persistencia/
│                       │   │   ├── RepositorioProductosCsv.java
│                       │   │   └── RepositorioProductosSql.java
│                       │   └── configuracion/
│                       │       └── ConfiguracionApp.java
│                       │
│                       └── presentacion/      ← Interfaces de Usuario
│                           ├── ConsolaVentas.java
│                           └── GuiVentas.java
│
└── test/
    └── java/
        └── ar/
            └── edu/
                └── unrn/
                    └── ventas/
                        ├── dominio/
                        │   └── LineaVentaTest.java
                        └── aplicacion/
                            └── ServicioVentasTest.java
```

(reglas-dependencias)=
### Reglas de Dependencias entre Paquetes

```
┌─────────────────────────────────────────────────────────────────┐
│           DEPENDENCIAS PERMITIDAS ENTRE PAQUETES                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   presentacion ──────────────────┐                              │
│        │                         │                              │
│        │ usa                     │                              │
│        ▼                         │                              │
│   aplicacion                     │ solo Main puede              │
│        │                         │ crear todo                   │
│        │ usa                     │                              │
│        ▼                         ▼                              │
│   dominio ◄────────────── infraestructura                       │
│                implementa                                       │
│                                                                 │
│   ✓ presentacion puede usar aplicacion y dominio                │
│   ✓ aplicacion puede usar dominio                               │
│   ✓ infraestructura puede usar dominio (implementa interfaces)  │
│   ✗ dominio NO puede usar ningún otro paquete                   │
│   ✗ aplicacion NO puede usar infraestructura directamente       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

(patrones-arquitectonicos)=
## Patrones Arquitectónicos Relacionados

(mvc)=
### Model-View-Controller (MVC)

El patrón **MVC** es una especialización de la arquitectura en capas para interfaces de usuario:

```
┌─────────────────────────────────────────────────────────────────┐
│                         MVC                                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│                    ┌─────────────┐                              │
│          ┌────────▶│    Vista    │◀────────┐                    │
│          │         │  (muestra)  │         │                    │
│          │         └─────────────┘         │                    │
│          │                                 │                    │
│     observa                            actualiza                │
│          │                                 │                    │
│          │                                 │                    │
│   ┌──────┴──────┐                  ┌───────┴──────┐             │
│   │   Modelo    │◀─────────────────│ Controlador  │             │
│   │  (datos y   │     modifica     │  (coordina)  │             │
│   │   lógica)   │                  └──────────────┘             │
│   └─────────────┘                         ▲                     │
│                                           │                     │
│                                    entrada usuario              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

(repository-pattern)=
### Patrón Repository

Ya lo vimos en acción. El **Repository** abstrae el acceso a datos:

```java
// Interfaz en el dominio
public interface RepositorioProductos {
    Optional<Producto> buscarPorCodigo(String codigo);
    List<Producto> buscarPorCategoria(String categoria);
    void guardar(Producto producto);
    void eliminar(String codigo);
}

// Implementaciones en infraestructura
public class RepositorioProductosJpa implements RepositorioProductos { ... }
public class RepositorioProductosMongoDB implements RepositorioProductos { ... }
public class RepositorioProductosEnMemoria implements RepositorioProductos { ... }
```

(dto)=
### Data Transfer Objects (DTOs)

Cuando necesitamos pasar datos entre capas con estructuras diferentes:

```java
/**
 * DTO para transferir datos de venta a la presentación.
 * No contiene lógica, solo datos.
 */
public class VentaDto {
    private final String fecha;
    private final List<LineaVentaDto> lineas;
    private final String totalFormateado;
    
    public static VentaDto desde(Venta venta) {
        return new VentaDto(
            venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            venta.getLineas().stream()
                .map(LineaVentaDto::desde)
                .collect(Collectors.toList()),
            String.format("$%,.2f", venta.getTotal())
        );
    }
    
    // Constructor, getters...
}
```

---

(errores-comunes)=
## Errores Comunes

(sobre-arquitectura)=
### Sobre-Arquitectura

:::{warning}
**No toda aplicación necesita capas elaboradas.**

Para un script de 200 líneas o un prototipo descartable, aplicar arquitectura completa es sobre-ingeniería. La arquitectura tiene un costo en complejidad que debe justificarse con los beneficios.
:::

(regla-general)=
### Regla General

```
┌─────────────────────────────────────────────────────────────────┐
│           ¿CUÁNTA ARQUITECTURA NECESITO?                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   Tamaño/Vida del proyecto          Nivel de arquitectura       │
│   ─────────────────────────         ─────────────────────       │
│                                                                 │
│   Script descartable (<500 LOC)     Ninguna especial            │
│                                                                 │
│   Proyecto pequeño (500-2000 LOC)   Separación básica           │
│                                     (lógica vs I/O)             │
│                                                                 │
│   Proyecto mediano (2K-20K LOC)     Capas claras                │
│                                     Interfaces para repos       │
│                                                                 │
│   Proyecto grande (>20K LOC)        Arquitectura formal         │
│                                     Módulos independientes      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

(capas-filtradas)=
### Capas que se "Filtran"

Un error común es que detalles de una capa "filtren" hacia otras:

```java
// ❌ MAL: La capa de servicio expone detalles de infraestructura
public class ServicioVentas {
    public Venta crearVenta(String codigo, int cantidad) 
            throws SQLException {  // ← SQLException es detalle de JDBC
        // ...
    }
}

// ✓ BIEN: Encapsular excepciones de infraestructura
public class ServicioVentas {
    public Venta crearVenta(String codigo, int cantidad) 
            throws ProductoNoEncontradoException {  // ← Excepción de dominio
        try {
            // ...
        } catch (SQLException e) {
            throw new ErrorDePersistenciaException("Error guardando venta", e);
        }
    }
}
```

---

(resumen-arquitectura)=
## Resumen

:::{tip} Conceptos Clave

1. **Arquitectura de software** organiza el sistema a gran escala
2. **Capas** separan responsabilidades (presentación, lógica, datos)
3. **Las dependencias fluyen hacia abajo** (o hacia abstracciones)
4. **El dominio no depende de nada externo** (DIP arquitectónico)
5. **Interfaces** en el dominio permiten sustituir implementaciones
6. **La arquitectura habilita testing** aislado de cada componente
7. **No sobre-arquitectar**: la complejidad debe justificarse
:::

```
┌─────────────────────────────────────────────────────────────────┐
│              ARQUITECTURA EN UNA IMAGEN                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│                        ┌─────────┐                              │
│                        │   UI    │                              │
│                        └────┬────┘                              │
│                             │                                   │
│                             ▼                                   │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │                      DOMINIO                            │   │
│   │                                                         │   │
│   │    Entidades   +   Servicios   +   Interfaces Repo      │   │
│   │                                                         │   │
│   └─────────────────────────────────────────────────────────┘   │
│                             ▲                                   │
│                             │ implementa                        │
│                   ┌─────────┴─────────┐                         │
│                   │                   │                         │
│              ┌────┴────┐         ┌────┴────┐                    │
│              │   CSV   │         │   SQL   │                    │
│              └─────────┘         └─────────┘                    │
│                                                                 │
│   El Dominio es el centro. Todo lo demás es periférico.         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

```{exercise}
:label: ej-identificar-capas

Dado el siguiente código, identificá qué partes pertenecen a cada capa y proponé cómo refactorizarlo:

```java
public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Usuario: ");
        String user = sc.nextLine();
        System.out.print("Contraseña: ");
        String pass = sc.nextLine();
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/app")) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM usuarios WHERE nombre = ? AND password = ?");
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                System.out.println("Bienvenido, " + rs.getString("nombre_completo"));
            } else {
                System.out.println("Credenciales inválidas");
            }
        } catch (SQLException e) {
            System.out.println("Error de base de datos");
        }
    }
}
```
```

```{solution} ej-identificar-capas
:class: dropdown

**Identificación de capas mezcladas:**

- **Presentación**: `Scanner`, `System.out.print/println`
- **Lógica**: La comparación usuario/contraseña (implícita en SQL)
- **Datos**: `Connection`, `PreparedStatement`, `ResultSet`

**Refactorización propuesta:**

```java
// Dominio
public class Usuario {
    private final String nombre;
    private final String nombreCompleto;
    // constructor, getters
}

public interface RepositorioUsuarios {
    Optional<Usuario> autenticar(String nombre, String password);
}

// Aplicación
public class ServicioAutenticacion {
    private final RepositorioUsuarios repo;
    
    public Usuario login(String nombre, String password) {
        return repo.autenticar(nombre, password)
            .orElseThrow(() -> new CredencialesInvalidasException());
    }
}

// Infraestructura
public class RepositorioUsuariosSql implements RepositorioUsuarios {
    // implementación con JDBC
}

// Presentación
public class ConsolaLogin {
    private final ServicioAutenticacion servicio;
    // captura entrada, muestra resultado
}
```
```

```{exercise}
:label: ej-disenar-capas

Diseñá la estructura de capas para un sistema de biblioteca que permita:
- Registrar libros (título, autor, ISBN)
- Prestar libros a socios
- Devolver libros
- Consultar disponibilidad

Definí las clases de dominio, las interfaces de repositorio, y el servicio de aplicación principal.
```

```{solution} ej-disenar-capas
:class: dropdown

```java
// === DOMINIO ===

public class Libro {
    private final String isbn;
    private final String titulo;
    private final String autor;
    private EstadoLibro estado;
    
    public boolean estaDisponible() {
        return estado == EstadoLibro.DISPONIBLE;
    }
}

public enum EstadoLibro {
    DISPONIBLE, PRESTADO
}

public class Socio {
    private final String id;
    private final String nombre;
    private final List<Prestamo> prestamosActivos;
    
    public boolean puedeTomarPrestamo() {
        return prestamosActivos.size() < 3;
    }
}

public class Prestamo {
    private final Libro libro;
    private final Socio socio;
    private final LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    
    public boolean estaActivo() {
        return fechaDevolucion == null;
    }
}

public interface RepositorioLibros {
    Optional<Libro> buscarPorIsbn(String isbn);
    List<Libro> buscarDisponibles();
    void guardar(Libro libro);
}

public interface RepositorioPrestamos {
    void guardar(Prestamo prestamo);
    List<Prestamo> buscarActivosPorSocio(String socioId);
}

// === APLICACIÓN ===

public class ServicioBiblioteca {
    private final RepositorioLibros repoLibros;
    private final RepositorioSocios repoSocios;
    private final RepositorioPrestamos repoPrestamos;
    
    public Prestamo prestarLibro(String isbn, String socioId) {
        Libro libro = repoLibros.buscarPorIsbn(isbn)
            .orElseThrow(() -> new LibroNoEncontradoException(isbn));
        
        if (!libro.estaDisponible()) {
            throw new LibroNoDisponibleException(isbn);
        }
        
        Socio socio = repoSocios.buscarPorId(socioId)
            .orElseThrow(() -> new SocioNoEncontradoException(socioId));
        
        if (!socio.puedeTomarPrestamo()) {
            throw new LimitePrestamosException(socioId);
        }
        
        Prestamo prestamo = new Prestamo(libro, socio, LocalDate.now());
        libro.marcarComoPrestado();
        
        repoLibros.guardar(libro);
        repoPrestamos.guardar(prestamo);
        
        return prestamo;
    }
    
    public void devolverLibro(String isbn) {
        // similar...
    }
}
```
```

---

## Lecturas Recomendadas

- Martin, Robert C. "Clean Architecture: A Craftsman's Guide to Software Structure and Design"
- Evans, Eric. "Domain-Driven Design: Tackling Complexity in the Heart of Software"
- Fowler, Martin. "Patterns of Enterprise Application Architecture"
