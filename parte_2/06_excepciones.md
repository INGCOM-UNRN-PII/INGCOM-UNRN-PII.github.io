---
title: "Excepciones Orientadas a Objetos"
subtitle: "Diseño de Jerarquías y Contratos de Error"
subject: Programación Orientada a Objetos
---

(java-excepciones)=
# Excepciones Orientadas a Objetos

Este capítulo es una continuación de {ref}`excepciones-en-java`, donde vimos los fundamentos del manejo de errores, la sintaxis `try-catch-finally`, y la diferencia entre excepciones checked y unchecked. Ahora profundizamos en **cómo las excepciones son objetos** y cómo diseñar sistemas de excepciones que aprovechen la orientación a objetos.

Este capítulo cubre:

1. **Excepciones como objetos**: Herencia, polimorfismo y composición
2. **Diseño de jerarquías**: Crear familias de excepciones de dominio
3. **Excepciones y contratos**: Precondiciones, postcondiciones e invariantes
4. **Patrones avanzados**: Encadenamiento, wrapping y traducción
5. **Excepciones en interfaces**: El principio de sustitución
6. **Antipatrones y refactoring**: Mejorando código existente

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, serás capaz de:

1. Diseñar jerarquías de excepciones coherentes para tu dominio
2. Usar excepciones como parte del contrato de clases e interfaces
3. Aplicar polimorfismo en el manejo de errores
4. Traducir excepciones entre capas de abstracción
5. Reconocer y corregir antipatrones comunes
:::

:::{important}
**Requisitos previos**:
- Haber leído {ref}`excepciones-en-java` (conceptos básicos, try-catch, checked vs unchecked)
- Dominar herencia e interfaces ({ref}`java-herencia-polimorfismo`)
- Comprender el diseño por contratos ({ref}`oop-contratos`)
:::

---

(excepciones-son-objetos)=
## Las Excepciones son Objetos

En {ref}`excepciones-en-java` vimos que las excepciones son objetos que contienen información sobre errores. Ahora profundizamos en las implicaciones de esto desde la perspectiva de la orientación a objetos.

### Throwable: La Raíz de la Jerarquía

Todas las excepciones heredan de `Throwable`, que define el contrato básico:

```java
public class Throwable {
    private String message;           // Descripción del error
    private Throwable cause;          // Excepción que causó esta
    private StackTraceElement[] trace; // Pila de llamadas
    
    public Throwable() { }
    public Throwable(String message) { }
    public Throwable(String message, Throwable cause) { }
    public Throwable(Throwable cause) { }
    
    public String getMessage() { ... }
    public Throwable getCause() { ... }
    public void printStackTrace() { ... }
    public StackTraceElement[] getStackTrace() { ... }
}
```

```{mermaid}
classDiagram
    class Throwable {
        -String message
        -Throwable cause
        -StackTraceElement[] trace
        +Throwable()
        +Throwable(String message)
        +Throwable(String message, Throwable cause)
        +getMessage() String
        +getCause() Throwable
        +printStackTrace()
        +getStackTrace() StackTraceElement[]
    }
    
    class Error {
        <<Java>>
    }
    
    class Exception {
        <<Java>>
    }
    
    class RuntimeException {
        <<Java Unchecked>>
    }
    
    class IOException {
        <<Java Checked>>
    }
    
    class SQLException {
        <<Java Checked>>
    }
    
    Throwable <|-- Error
    Throwable <|-- Exception
    Exception <|-- RuntimeException
    Exception <|-- IOException
    Exception <|-- SQLException
    
    note for Throwable "Raíz de jerarquía<br>Contiene mensaje, causa<br>y stack trace"
    note for Error "Errores graves del sistema<br>NO se deben capturar"
    note for Exception "Excepciones de aplicación<br>Pueden ser checked/unchecked"
    note for RuntimeException "Excepciones no verificadas<br>No requieren try-catch"
```

### Herencia en Excepciones

Como cualquier clase, las excepciones pueden:

- **Heredar comportamiento** de excepciones padre
- **Agregar atributos** específicos del error
- **Sobrescribir métodos** para personalizar el comportamiento
- **Ser polimórficas** (un `catch` de la superclase atrapa subclases)

```java
// IOException es padre de FileNotFoundException
try {
    leerArchivo("datos.txt");
} catch (IOException e) {
    // Atrapa IOException Y FileNotFoundException (polimorfismo)
    System.out.println("Error de I/O: " + e.getMessage());
}
```

### Excepciones con Estado

Las excepciones pueden tener **atributos propios** que proveen contexto adicional:

```java
public class SaldoInsuficienteException extends RuntimeException {
    private final double saldoActual;
    private final double montoSolicitado;
    
    public SaldoInsuficienteException(double saldoActual, double montoSolicitado) {
        super(String.format(
            "Saldo insuficiente. Disponible: $%.2f, Solicitado: $%.2f",
            saldoActual, montoSolicitado
        ));
        this.saldoActual = saldoActual;
        this.montoSolicitado = montoSolicitado;
    }
    
    public double getSaldoActual() { return saldoActual; }
    public double getMontoSolicitado() { return montoSolicitado; }
    public double getDeficit() { return montoSolicitado - saldoActual; }
}
```

El código que atrapa puede usar estos métodos:

```java
try {
    cuenta.retirar(1000);
} catch (SaldoInsuficienteException e) {
    System.out.println(e.getMessage());
    System.out.println("Te faltan: $" + e.getDeficit());
    // Podemos tomar decisiones basadas en el déficit
    if (e.getDeficit() < 100) {
        sugerirTransferencia(e.getDeficit());
    }
}
```

---

(diseño-jerarquias)=
## Diseño de Jerarquías de Excepciones

### El Patrón de Excepción Base de Dominio

Para sistemas complejos, es útil crear una **excepción base** para todo el dominio:

```java
/**
 * Excepción base para todos los errores del sistema bancario.
 * Permite capturar cualquier error bancario con un solo catch.
 */
public abstract class BancoException extends Exception {
    private final String codigoError;
    private final LocalDateTime timestamp;
    
    protected BancoException(String mensaje, String codigoError) {
        super(mensaje);
        this.codigoError = codigoError;
        this.timestamp = LocalDateTime.now();
    }
    
    protected BancoException(String mensaje, String codigoError, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = codigoError;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getCodigoError() { return codigoError; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    /**
     * Indica si el error es recuperable (el usuario puede reintentar).
     */
    public abstract boolean esRecuperable();
}
```

### Excepciones Específicas del Dominio

Las subclases representan errores específicos:

```java
public class CuentaNoEncontradaException extends BancoException {
    private final String numeroCuenta;
    
    public CuentaNoEncontradaException(String numeroCuenta) {
        super("Cuenta no encontrada: " + numeroCuenta, "BANCO-001");
        this.numeroCuenta = numeroCuenta;
    }
    
    public String getNumeroCuenta() { return numeroCuenta; }
    
    @Override
    public boolean esRecuperable() {
        return true; // El usuario puede corregir el número
    }
}

public class LimiteExcedidoException extends BancoException {
    private final double limite;
    private final double montoIntentado;
    
    public LimiteExcedidoException(double limite, double montoIntentado) {
        super(String.format("Límite excedido. Máximo: $%.2f, Intentado: $%.2f",
                           limite, montoIntentado), "BANCO-002");
        this.limite = limite;
        this.montoIntentado = montoIntentado;
    }
    
    public double getLimite() { return limite; }
    public double getMontoIntentado() { return montoIntentado; }
    
    @Override
    public boolean esRecuperable() {
        return true; // El usuario puede intentar con monto menor
    }
}

public class FraudeDetectadoException extends BancoException {
    private final String motivo;
    
    public FraudeDetectadoException(String motivo) {
        super("Operación bloqueada por seguridad: " + motivo, "BANCO-999");
        this.motivo = motivo;
    }
    
    public String getMotivo() { return motivo; }
    
    @Override
    public boolean esRecuperable() {
        return false; // Requiere intervención del banco
    }
}
```

### Manejo Polimórfico

La jerarquía permite diferentes niveles de especificidad:

```java
public void realizarTransferencia(String origen, String destino, double monto) {
    try {
        banco.transferir(origen, destino, monto);
        System.out.println("Transferencia exitosa");
        
    } catch (CuentaNoEncontradaException e) {
        // Manejo específico: sugerir verificar el número
        System.out.println("Verificá el número de cuenta: " + e.getNumeroCuenta());
        
    } catch (LimiteExcedidoException e) {
        // Manejo específico: sugerir monto menor
        System.out.println("Podés transferir hasta $" + e.getLimite());
        
    } catch (FraudeDetectadoException e) {
        // Manejo específico: contactar al banco
        System.out.println("Contactá al banco: " + e.getCodigoError());
        bloquearCuenta(origen);
        
    } catch (BancoException e) {
        // Manejo genérico para cualquier otra excepción bancaria
        System.out.println("Error bancario: " + e.getMessage());
        if (e.esRecuperable()) {
            System.out.println("Podés intentar de nuevo");
        }
    }
}
```

### Jerarquía de Ejemplo Completa

```{mermaid}
classDiagram
    class Throwable {
        <<Java>>
        -String message
        -Throwable cause
        +getMessage() String
        +getCause() Throwable
    }
    
    class Exception {
        <<Java>>
    }
    
    class RuntimeException {
        <<Java>>
    }
    
    class BancoException {
        <<abstract>>
        -String codigoError
        -LocalDateTime timestamp
        +getCodigoError() String
        +esRecuperable()* boolean
    }
    
    class CuentaException {
        <<abstract>>
    }
    
    class TransferenciaException {
        <<abstract>>
    }
    
    class SeguridadException {
        <<abstract>>
    }
    
    class CuentaNoEncontradaException {
        -String numeroCuenta
        +esRecuperable() boolean
    }
    
    class SaldoInsuficienteException {
        -double saldoActual
        -double montoSolicitado
        +getDeficit() double
        +esRecuperable() boolean
    }
    
    class LimiteExcedidoException {
        -double limite
        -double montoIntentado
        +esRecuperable() boolean
    }
    
    class FraudeDetectadoException {
        -String motivo
        +esRecuperable() boolean
    }
    
    Throwable <|-- Exception
    Exception <|-- RuntimeException
    Exception <|-- BancoException
    BancoException <|-- CuentaException
    BancoException <|-- TransferenciaException
    BancoException <|-- SeguridadException
    CuentaException <|-- CuentaNoEncontradaException
    CuentaException <|-- SaldoInsuficienteException
    TransferenciaException <|-- LimiteExcedidoException
    SeguridadException <|-- FraudeDetectadoException
    
    note for BancoException "Excepción base del dominio<br>con atributos comunes"
    note for CuentaException "Agrupa errores<br>relacionados con cuentas"
    note for FraudeDetectadoException "Excepción específica<br>con contexto adicional"
```

---

(excepciones-contratos)=
## Excepciones y Contratos

Las excepciones son parte fundamental del **contrato** de una clase o método. Documentan qué puede salir mal y bajo qué condiciones.

### Precondiciones y Excepciones

Las **precondiciones** son condiciones que deben cumplirse antes de ejecutar un método. Cuando se violan, lanzamos excepciones:

```java
/**
 * Retira dinero de la cuenta.
 * 
 * @param monto El monto a retirar
 * @throws IllegalArgumentException si monto <= 0 (precondición)
 * @throws SaldoInsuficienteException si monto > saldo (precondición)
 * @throws CuentaBloqueadaException si la cuenta está bloqueada (precondición)
 */
public void retirar(double monto) throws SaldoInsuficienteException, CuentaBloqueadaException {
    // Validar precondiciones (fail fast)
    if (monto <= 0) {
        throw new IllegalArgumentException(
            "El monto debe ser positivo, se recibió: " + monto
        );
    }
    if (bloqueada) {
        throw new CuentaBloqueadaException(this.numero, this.motivoBloqueo);
    }
    if (monto > saldo) {
        throw new SaldoInsuficienteException(saldo, monto);
    }
    
    // Lógica principal (solo si las precondiciones se cumplen)
    saldo -= monto;
    registrarMovimiento(TipoMovimiento.RETIRO, monto);
}
```

:::{tip} Fail Fast
Validá las precondiciones **al inicio** del método. Es mejor fallar inmediatamente con un mensaje claro que continuar y fallar después con un error confuso.
:::

### Excepciones en Interfaces

Cuando definís una interfaz, las excepciones son parte del contrato:

```java
public interface Repositorio<T> {
    /**
     * Busca una entidad por su ID.
     * 
     * @param id El identificador de la entidad
     * @return La entidad encontrada
     * @throws EntidadNoEncontradaException si no existe entidad con ese ID
     * @throws RepositorioException si hay error de acceso a datos
     */
    T buscar(String id) throws EntidadNoEncontradaException, RepositorioException;
    
    /**
     * Guarda una entidad.
     * 
     * @param entidad La entidad a guardar (no puede ser null)
     * @throws IllegalArgumentException si entidad es null
     * @throws DuplicadoException si ya existe una entidad con el mismo ID
     * @throws RepositorioException si hay error de acceso a datos
     */
    void guardar(T entidad) throws DuplicadoException, RepositorioException;
}
```

### El Principio de Sustitución y Excepciones

El **Principio de Sustitución de Liskov** (ver {ref}`l-principio-de-sustitucion-de-liskov`) tiene implicaciones para las excepciones:

:::{important} Regla de Covarianza en Excepciones
Una subclase que sobrescribe un método **no puede declarar excepciones checked más amplias** que el método original. Puede:
- Declarar las mismas excepciones
- Declarar subclases de esas excepciones
- Declarar menos excepciones
- No declarar ninguna excepción checked
:::

```java
public interface Servicio {
    void procesar() throws IOException;
}

// ✓ CORRECTO: declara la misma excepción
public class ServicioA implements Servicio {
    @Override
    public void procesar() throws IOException { }
}

// ✓ CORRECTO: declara una subclase de IOException
public class ServicioB implements Servicio {
    @Override
    public void procesar() throws FileNotFoundException { }
}

// ✓ CORRECTO: no declara ninguna excepción
public class ServicioC implements Servicio {
    @Override
    public void procesar() { }
}

// ❌ ERROR: SQLException no es subclase de IOException
public class ServicioD implements Servicio {
    @Override
    public void procesar() throws SQLException { } // No compila
}
```

**¿Por qué esta restricción?** El código cliente que usa la interfaz espera solo `IOException`. Si una implementación pudiera lanzar `SQLException`, el cliente no estaría preparado para manejarla.

---

(traduccion-excepciones)=
## Traducción de Excepciones

### El Problema de las Capas

En arquitecturas por capas, las excepciones de capas inferiores no deberían escapar a capas superiores:

```{mermaid}
graph TB
    subgraph "Arquitectura por Capas"
        A[Capa Presentación<br/>UI/Controllers]
        B[Capa Negocio<br/>Services]
        C[Capa Datos<br/>Repositories]
        D[(Base de Datos)]
    end
    
    A -->|usa| B
    B -->|usa| C
    C -->|accede| D
    
    D -.->|SQLException| C
    C -.->|RepositorioException| B
    B -.->|PedidoException| A
    
    style D fill:#f9f,stroke:#333
    style C fill:#bbf,stroke:#333
    style B fill:#bfb,stroke:#333
    style A fill:#fbb,stroke:#333
    
    note1[❌ SQLException NO debe<br/>llegar a Presentación]
    note2[✓ Cada capa traduce<br/>a sus propias excepciones]
```

**Principio:** Cada capa traduce excepciones técnicas a excepciones de su nivel de abstracción.

### Encadenamiento de Excepciones

La solución es **traducir** excepciones, preservando la causa original:

```java
public class RepositorioUsuarios implements Repositorio<Usuario> {
    private final Connection conexion;
    
    @Override
    public Usuario buscar(String id) throws RepositorioException {
        try {
            PreparedStatement stmt = conexion.prepareStatement(
                "SELECT * FROM usuarios WHERE id = ?"
            );
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            throw new EntidadNoEncontradaException("Usuario", id);
            
        } catch (SQLException e) {
            // Traducir excepción técnica a excepción de dominio
            // Preservamos la causa original para debugging
            throw new RepositorioException(
                "Error al buscar usuario: " + id, 
                e  // ← La SQLException original queda como causa
            );
        }
    }
}
```

### Accediendo a la Causa

El código que maneja puede acceder a toda la cadena:

```java
try {
    Usuario usuario = repositorio.buscar("123");
} catch (RepositorioException e) {
    System.out.println("Error de repositorio: " + e.getMessage());
    
    // Acceder a la causa original (para logging/debugging)
    Throwable causa = e.getCause();
    if (causa instanceof SQLException) {
        SQLException sql = (SQLException) causa;
        System.out.println("Error SQL: " + sql.getSQLState());
    }
    
    // El stack trace muestra toda la cadena
    e.printStackTrace();
}
```

### Patrón de Traducción Completo

```java
public class ServicioPedidos {
    private final RepositorioPedidos repoPedidos;
    private final ServicioPagos servicioPagos;
    
    public Pedido crearPedido(Cliente cliente, List<Item> items) 
            throws PedidoException {
        
        try {
            // Crear pedido
            Pedido pedido = new Pedido(cliente, items);
            
            // Persistir
            repoPedidos.guardar(pedido);
            
            // Procesar pago
            servicioPagos.cobrar(cliente, pedido.getTotal());
            
            return pedido;
            
        } catch (RepositorioException e) {
            // Traducir error de repositorio
            throw new PedidoException(
                "Error al guardar pedido", "PED-001", e
            );
        } catch (PagoException e) {
            // Traducir error de pago
            throw new PedidoException(
                "Error al procesar pago: " + e.getMessage(), "PED-002", e
            );
        }
    }
}
```

---

(excepciones-personalizadas-oop)=
## Excepciones Personalizadas: Guía Completa

### Anatomía de una Excepción Bien Diseñada

Una excepción personalizada debe seguir ciertos patrones:

```java
public class ValidacionException extends RuntimeException {
    
    // 1. Atributos inmutables (final) para información del error
    private final String campo;
    private final Object valorRechazado;
    private final String restriccion;
    
    // 2. Constructores que cubren los casos de uso comunes
    public ValidacionException(String campo, Object valor, String restriccion) {
        super(construirMensaje(campo, valor, restriccion));
        this.campo = campo;
        this.valorRechazado = valor;
        this.restriccion = restriccion;
    }
    
    public ValidacionException(String campo, Object valor, String restriccion, 
                               Throwable causa) {
        super(construirMensaje(campo, valor, restriccion), causa);
        this.campo = campo;
        this.valorRechazado = valor;
        this.restriccion = restriccion;
    }
    
    // 3. Método auxiliar para construir mensaje descriptivo
    private static String construirMensaje(String campo, Object valor, 
                                           String restriccion) {
        return String.format(
            "Validación fallida en '%s': valor '%s' no cumple '%s'",
            campo, valor, restriccion
        );
    }
    
    // 4. Getters para acceso a la información
    public String getCampo() { return campo; }
    public Object getValorRechazado() { return valorRechazado; }
    public String getRestriccion() { return restriccion; }
    
    // 5. Métodos de conveniencia opcionales
    public boolean esCampo(String nombreCampo) {
        return campo.equals(nombreCampo);
    }
}
```

### ¿Checked o Unchecked?

La decisión entre checked y unchecked depende de la semántica:

**Usar Unchecked (`extends RuntimeException`) cuando:**
- El error indica un bug o violación de precondición
- El llamador no puede hacer nada significativo
- El error es "culpa" del programador

```java
// Unchecked: error de programación
public class EmailInvalidoException extends RuntimeException {
    public EmailInvalidoException(String email) {
        super("Email inválido: " + email);
    }
}
```

**Usar Checked (`extends Exception`) cuando:**
- El error es esperado en operaciones normales
- El llamador **debe** considerar este caso
- Hay acciones de recuperación posibles

```java
// Checked: situación esperada que requiere manejo
public class UsuarioNoEncontradoException extends Exception {
    private final String username;
    
    public UsuarioNoEncontradoException(String username) {
        super("Usuario no encontrado: " + username);
        this.username = username;
    }
    
    public String getUsername() { return username; }
}
```

### Factory Methods para Excepciones

Para excepciones con múltiples causas posibles, los métodos factory clarifican la intención:

```java
public class ConexionException extends Exception {
    
    public enum Tipo { TIMEOUT, RECHAZADA, AUTENTICACION, DESCONOCIDO }
    
    private final Tipo tipo;
    private final String servidor;
    
    private ConexionException(String mensaje, Tipo tipo, String servidor, 
                              Throwable causa) {
        super(mensaje, causa);
        this.tipo = tipo;
        this.servidor = servidor;
    }
    
    // Factory methods con nombres descriptivos
    public static ConexionException porTimeout(String servidor, int segundos) {
        return new ConexionException(
            String.format("Timeout conectando a %s después de %d segundos", 
                         servidor, segundos),
            Tipo.TIMEOUT, servidor, null
        );
    }
    
    public static ConexionException porRechazo(String servidor, String motivo) {
        return new ConexionException(
            String.format("Conexión rechazada por %s: %s", servidor, motivo),
            Tipo.RECHAZADA, servidor, null
        );
    }
    
    public static ConexionException porAutenticacion(String servidor, 
                                                      String usuario) {
        return new ConexionException(
            String.format("Autenticación fallida en %s para usuario %s", 
                         servidor, usuario),
            Tipo.AUTENTICACION, servidor, null
        );
    }
    
    public static ConexionException envolver(String servidor, Exception causa) {
        return new ConexionException(
            "Error de conexión a " + servidor + ": " + causa.getMessage(),
            Tipo.DESCONOCIDO, servidor, causa
        );
    }
    
    public Tipo getTipo() { return tipo; }
    public String getServidor() { return servidor; }
    
    public boolean esRecuperable() {
        return tipo == Tipo.TIMEOUT || tipo == Tipo.RECHAZADA;
    }
}
```

**Uso:**

```java
// Código claro y expresivo
throw ConexionException.porTimeout("db.ejemplo.com", 30);
throw ConexionException.porAutenticacion("api.servicio.com", "admin");

// Manejo basado en tipo
try {
    conectar(servidor);
} catch (ConexionException e) {
    if (e.esRecuperable()) {
        System.out.println("Reintentando...");
        reintentar(e.getServidor());
    } else {
        notificarAdministrador(e);
    }
}
```

---

(excepciones-recursos)=
## Excepciones y Gestión de Recursos

### AutoCloseable como Contrato

La interfaz `AutoCloseable` define un contrato para recursos que deben cerrarse:

```java
public interface AutoCloseable {
    /**
     * Cierra este recurso, liberando cualquier recurso subyacente.
     * 
     * @throws Exception si no se puede cerrar correctamente
     */
    void close() throws Exception;
}
```

### Implementando AutoCloseable

```java
public class ConexionBaseDatos implements AutoCloseable {
    private Connection conexion;
    private boolean cerrada = false;
    
    public ConexionBaseDatos(String url, String usuario, String password) 
            throws SQLException {
        this.conexion = DriverManager.getConnection(url, usuario, password);
    }
    
    public ResultSet ejecutarConsulta(String sql) throws SQLException {
        verificarAbierta();
        Statement stmt = conexion.createStatement();
        return stmt.executeQuery(sql);
    }
    
    private void verificarAbierta() {
        if (cerrada) {
            throw new IllegalStateException("Conexión ya cerrada");
        }
    }
    
    @Override
    public void close() throws SQLException {
        if (!cerrada) {
            cerrada = true;
            conexion.close();
        }
    }
}
```

**Uso con try-with-resources:**

```java
try (ConexionBaseDatos db = new ConexionBaseDatos(url, user, pass)) {
    ResultSet rs = db.ejecutarConsulta("SELECT * FROM usuarios");
    while (rs.next()) {
        System.out.println(rs.getString("nombre"));
    }
}  // db.close() se llama automáticamente, incluso si hay excepción
```

### Suppressed Exceptions

Cuando el bloque try lanza una excepción Y el close también falla, Java "suprime" la segunda excepción pero la preserva:

```java
public class RecursoProblematico implements AutoCloseable {
    @Override
    public void close() {
        throw new RuntimeException("Error al cerrar");
    }
}

try (RecursoProblematico r = new RecursoProblematico()) {
    throw new RuntimeException("Error en operación");
} catch (RuntimeException e) {
    System.out.println("Principal: " + e.getMessage());
    // Error en operación
    
    Throwable[] suprimidas = e.getSuppressed();
    for (Throwable t : suprimidas) {
        System.out.println("Suprimida: " + t.getMessage());
        // Error al cerrar
    }
}
```

---

(antipatrones-excepciones)=
## Antipatrones y Cómo Evitarlos

### 1. Catch Genérico

```java
// ❌ MAL: atrapa todo, incluso bugs
try {
    procesarPedido(pedido);
} catch (Exception e) {
    logger.error("Error", e);
}

// ✓ BIEN: específico
try {
    procesarPedido(pedido);
} catch (ProductoNoDisponibleException e) {
    notificarCliente(e.getProducto());
} catch (PagoRechazadoException e) {
    solicitarOtroPago(e.getMotivo());
}
```

### 2. Excepción Silenciada

```java
// ❌ MAL: el error desaparece
try {
    guardar(datos);
} catch (IOException e) {
    // Vacío - nadie sabe que falló
}

// ✓ BIEN: al menos loggear
try {
    guardar(datos);
} catch (IOException e) {
    logger.warn("No se pudo guardar: {}", e.getMessage());
    // Y decidir qué hacer: reintentar, usar cache, etc.
}
```

### 3. Throw en Finally

```java
// ❌ MAL: oculta la excepción original
try {
    return calcular();
} finally {
    throw new RuntimeException("Limpieza fallida");
    // La excepción de calcular() se pierde
}

// ✓ BIEN: loggear errores de limpieza
try {
    return calcular();
} finally {
    try {
        limpiar();
    } catch (Exception e) {
        logger.error("Error en limpieza", e);
    }
}
```

### 4. Excepciones para Control de Flujo

```java
// ❌ MAL: excepción como goto
public int buscarIndice(int[] arr, int valor) {
    try {
        for (int i = 0; ; i++) {  // Sin condición de fin
            if (arr[i] == valor) return i;
        }
    } catch (ArrayIndexOutOfBoundsException e) {
        return -1;  // "Fin" del arreglo
    }
}

// ✓ BIEN: condición explícita
public int buscarIndice(int[] arr, int valor) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == valor) return i;
    }
    return -1;
}
```

### 5. Retornar null en Lugar de Excepción

```java
// ❌ MAL: null se propaga y causa NPE después
public Usuario buscarUsuario(String id) {
    try {
        return repositorio.buscar(id);
    } catch (SQLException e) {
        return null;  // El llamador no sabe que hubo error
    }
}

// ✓ BIEN: excepción clara
public Usuario buscarUsuario(String id) throws RepositorioException {
    try {
        return repositorio.buscar(id);
    } catch (SQLException e) {
        throw new RepositorioException("Error buscando usuario", e);
    }
}

// ✓ También bien: Optional para ausencia esperada
public Optional<Usuario> buscarUsuario(String id) {
    return Optional.ofNullable(repositorio.buscar(id));
}
```

### 6. Log and Throw

```java
// ❌ MAL: el error se loggea múltiples veces
try {
    procesar();
} catch (Exception e) {
    logger.error("Error", e);  // Log aquí
    throw e;  // Y el que atrapa también loggea
}

// ✓ BIEN: una cosa u otra
try {
    procesar();
} catch (Exception e) {
    throw new ProcesamientoException("Falló procesamiento", e);
    // El que atrapa final decide si loggear
}
```

---

(ejemplo-sistema-completo)=
## Ejemplo: Sistema de Reservas

Un ejemplo completo que muestra todos los conceptos de excepciones OOP en acción.

### Jerarquía de Excepciones

```java
// Excepción base del dominio
public abstract class ReservaException extends Exception {
    private final String codigoError;
    private final LocalDateTime timestamp;
    
    protected ReservaException(String mensaje, String codigo) {
        super(mensaje);
        this.codigoError = codigo;
        this.timestamp = LocalDateTime.now();
    }
    
    protected ReservaException(String mensaje, String codigo, Throwable causa) {
        super(mensaje, causa);
        this.codigoError = codigo;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getCodigoError() { return codigoError; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public abstract boolean esRecuperable();
}

// Excepciones específicas
public class HabitacionNoDisponibleException extends ReservaException {
    private final int numeroHabitacion;
    private final LocalDate fechaDeseada;
    private final List<LocalDate> alternativas;
    
    public HabitacionNoDisponibleException(int numero, LocalDate fecha,
                                           List<LocalDate> alternativas) {
        super(String.format("Habitación %d no disponible para %s", numero, fecha),
              "RES-001");
        this.numeroHabitacion = numero;
        this.fechaDeseada = fecha;
        this.alternativas = List.copyOf(alternativas);
    }
    
    public int getNumeroHabitacion() { return numeroHabitacion; }
    public LocalDate getFechaDeseada() { return fechaDeseada; }
    public List<LocalDate> getAlternativas() { return alternativas; }
    
    @Override
    public boolean esRecuperable() {
        return !alternativas.isEmpty();
    }
}

public class ClienteBloqueadoException extends ReservaException {
    private final String clienteId;
    private final String motivo;
    
    public ClienteBloqueadoException(String clienteId, String motivo) {
        super("Cliente bloqueado: " + motivo, "RES-002");
        this.clienteId = clienteId;
        this.motivo = motivo;
    }
    
    public String getClienteId() { return clienteId; }
    public String getMotivo() { return motivo; }
    
    @Override
    public boolean esRecuperable() {
        return false; // Requiere intervención manual
    }
}

public class PagoFallidoException extends ReservaException {
    private final double monto;
    private final String metodoPago;
    private final boolean reintentable;
    
    private PagoFallidoException(String mensaje, String codigo, double monto,
                                  String metodoPago, boolean reintentable,
                                  Throwable causa) {
        super(mensaje, codigo, causa);
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.reintentable = reintentable;
    }
    
    // Factory methods
    public static PagoFallidoException porFondosInsuficientes(double monto) {
        return new PagoFallidoException(
            "Fondos insuficientes para pago de $" + monto,
            "PAG-001", monto, "tarjeta", false, null
        );
    }
    
    public static PagoFallidoException porTimeout(double monto, String metodo) {
        return new PagoFallidoException(
            "Timeout procesando pago",
            "PAG-002", monto, metodo, true, null
        );
    }
    
    public static PagoFallidoException porError(double monto, Exception causa) {
        return new PagoFallidoException(
            "Error de procesamiento: " + causa.getMessage(),
            "PAG-999", monto, "desconocido", true, causa
        );
    }
    
    public double getMonto() { return monto; }
    public String getMetodoPago() { return metodoPago; }
    
    @Override
    public boolean esRecuperable() {
        return reintentable;
    }
}
```

### Clases de Dominio

```java
public class Habitacion {
    private final int numero;
    private final TipoHabitacion tipo;
    private final double precioPorNoche;
    private final Set<LocalDate> fechasOcupadas;
    
    public Habitacion(int numero, TipoHabitacion tipo, double precio) {
        this.numero = numero;
        this.tipo = tipo;
        this.precioPorNoche = precio;
        this.fechasOcupadas = new HashSet<>();
    }
    
    public boolean estaDisponible(LocalDate fecha) {
        return !fechasOcupadas.contains(fecha);
    }
    
    public boolean estaDisponible(LocalDate desde, LocalDate hasta) {
        LocalDate fecha = desde;
        while (!fecha.isAfter(hasta)) {
            if (!estaDisponible(fecha)) return false;
            fecha = fecha.plusDays(1);
        }
        return true;
    }
    
    public void reservar(LocalDate desde, LocalDate hasta) {
        LocalDate fecha = desde;
        while (!fecha.isAfter(hasta)) {
            fechasOcupadas.add(fecha);
            fecha = fecha.plusDays(1);
        }
    }
    
    public void cancelarReserva(LocalDate desde, LocalDate hasta) {
        LocalDate fecha = desde;
        while (!fecha.isAfter(hasta)) {
            fechasOcupadas.remove(fecha);
            fecha = fecha.plusDays(1);
        }
    }
    
    public List<LocalDate> buscarAlternativas(LocalDate cercaA, int dias) {
        List<LocalDate> alternativas = new ArrayList<>();
        for (int offset = 1; offset <= 14 && alternativas.size() < 3; offset++) {
            LocalDate antes = cercaA.minusDays(offset);
            LocalDate despues = cercaA.plusDays(offset);
            
            if (estaDisponible(antes, antes.plusDays(dias - 1))) {
                alternativas.add(antes);
            }
            if (estaDisponible(despues, despues.plusDays(dias - 1))) {
                alternativas.add(despues);
            }
        }
        return alternativas;
    }
    
    // Getters...
    public int getNumero() { return numero; }
    public TipoHabitacion getTipo() { return tipo; }
    public double getPrecioPorNoche() { return precioPorNoche; }
}

public enum TipoHabitacion {
    SIMPLE(1), DOBLE(2), SUITE(4);
    
    private final int capacidad;
    
    TipoHabitacion(int capacidad) {
        this.capacidad = capacidad;
    }
    
    public int getCapacidad() { return capacidad; }
}

public class Cliente {
    private final String id;
    private final String nombre;
    private final String email;
    private boolean bloqueado;
    private String motivoBloqueo;
    
    public Cliente(String id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.bloqueado = false;
    }
    
    public void bloquear(String motivo) {
        this.bloqueado = true;
        this.motivoBloqueo = motivo;
    }
    
    // Getters...
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public boolean estaBloqueado() { return bloqueado; }
    public String getMotivoBloqueo() { return motivoBloqueo; }
}

public class Reserva implements AutoCloseable {
    private final String codigo;
    private final Cliente cliente;
    private final Habitacion habitacion;
    private final LocalDate fechaEntrada;
    private final LocalDate fechaSalida;
    private EstadoReserva estado;
    private boolean cerrada = false;
    
    public Reserva(String codigo, Cliente cliente, Habitacion habitacion,
                   LocalDate entrada, LocalDate salida) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.habitacion = habitacion;
        this.fechaEntrada = entrada;
        this.fechaSalida = salida;
        this.estado = EstadoReserva.PENDIENTE;
    }
    
    public void confirmar() {
        verificarNoCerrada();
        this.estado = EstadoReserva.CONFIRMADA;
    }
    
    public void cancelar() {
        verificarNoCerrada();
        this.estado = EstadoReserva.CANCELADA;
    }
    
    public double calcularTotal() {
        long noches = ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
        return noches * habitacion.getPrecioPorNoche();
    }
    
    private void verificarNoCerrada() {
        if (cerrada) {
            throw new IllegalStateException("Reserva ya cerrada: " + codigo);
        }
    }
    
    @Override
    public void close() {
        if (!cerrada) {
            cerrada = true;
            if (estado == EstadoReserva.PENDIENTE) {
                // Liberar habitación si no se confirmó
                habitacion.cancelarReserva(fechaEntrada, fechaSalida);
            }
        }
    }
    
    // Getters...
    public String getCodigo() { return codigo; }
    public Cliente getCliente() { return cliente; }
    public Habitacion getHabitacion() { return habitacion; }
    public EstadoReserva getEstado() { return estado; }
}

public enum EstadoReserva {
    PENDIENTE, CONFIRMADA, CANCELADA
}
```

### Servicio de Reservas

```java
public class ServicioReservas {
    private final RepositorioHabitaciones habitaciones;
    private final ServicioPagos pagos;
    private final ServicioNotificaciones notificaciones;
    private int contadorReservas = 0;
    
    public ServicioReservas(RepositorioHabitaciones habitaciones,
                            ServicioPagos pagos,
                            ServicioNotificaciones notificaciones) {
        this.habitaciones = habitaciones;
        this.pagos = pagos;
        this.notificaciones = notificaciones;
    }
    
    /**
     * Crea una nueva reserva.
     * 
     * @throws IllegalArgumentException si los parámetros son inválidos
     * @throws ClienteBloqueadoException si el cliente está bloqueado
     * @throws HabitacionNoDisponibleException si no hay disponibilidad
     * @throws PagoFallidoException si el pago no se procesa
     */
    public Reserva crearReserva(Cliente cliente, int numeroHabitacion,
                                 LocalDate entrada, LocalDate salida) 
            throws ReservaException {
        
        // Validar precondiciones
        validarParametros(cliente, entrada, salida);
        
        // Verificar cliente
        if (cliente.estaBloqueado()) {
            throw new ClienteBloqueadoException(
                cliente.getId(), cliente.getMotivoBloqueo()
            );
        }
        
        // Buscar habitación
        Habitacion habitacion = habitaciones.buscar(numeroHabitacion);
        
        // Verificar disponibilidad
        if (!habitacion.estaDisponible(entrada, salida)) {
            int dias = (int) ChronoUnit.DAYS.between(entrada, salida);
            List<LocalDate> alternativas = habitacion.buscarAlternativas(entrada, dias);
            throw new HabitacionNoDisponibleException(
                numeroHabitacion, entrada, alternativas
            );
        }
        
        // Crear reserva provisional
        String codigo = generarCodigo();
        Reserva reserva = new Reserva(codigo, cliente, habitacion, entrada, salida);
        habitacion.reservar(entrada, salida);
        
        // Procesar pago (con rollback si falla)
        try {
            pagos.cobrar(cliente, reserva.calcularTotal());
        } catch (PagoFallidoException e) {
            // Rollback: liberar habitación
            habitacion.cancelarReserva(entrada, salida);
            throw e;
        }
        
        // Confirmar y notificar
        reserva.confirmar();
        notificaciones.enviarConfirmacion(cliente.getEmail(), reserva);
        
        return reserva;
    }
    
    private void validarParametros(Cliente cliente, LocalDate entrada, 
                                    LocalDate salida) {
        Objects.requireNonNull(cliente, "Cliente no puede ser null");
        Objects.requireNonNull(entrada, "Fecha de entrada no puede ser null");
        Objects.requireNonNull(salida, "Fecha de salida no puede ser null");
        
        if (entrada.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                "Fecha de entrada no puede ser pasada: " + entrada
            );
        }
        if (!salida.isAfter(entrada)) {
            throw new IllegalArgumentException(
                "Fecha de salida debe ser posterior a entrada"
            );
        }
    }
    
    private String generarCodigo() {
        return "RES-" + String.format("%06d", ++contadorReservas);
    }
}
```

### Uso del Sistema

```java
public class AplicacionReservas {
    
    public static void main(String[] args) {
        // Configurar sistema
        RepositorioHabitaciones habitaciones = new RepositorioHabitacionesMemoria();
        ServicioPagos pagos = new ServicioPagosSimulado();
        ServicioNotificaciones notifs = new ServicioNotificacionesConsola();
        
        ServicioReservas servicio = new ServicioReservas(habitaciones, pagos, notifs);
        
        // Crear cliente
        Cliente cliente = new Cliente("CLI-001", "Ana García", "ana@email.com");
        
        // Intentar reserva
        try {
            Reserva reserva = servicio.crearReserva(
                cliente, 
                101,  // habitación
                LocalDate.of(2024, 7, 15),
                LocalDate.of(2024, 7, 20)
            );
            
            System.out.println("¡Reserva exitosa!");
            System.out.println("Código: " + reserva.getCodigo());
            System.out.println("Total: $" + reserva.calcularTotal());
            
        } catch (HabitacionNoDisponibleException e) {
            System.out.println("No hay disponibilidad: " + e.getMessage());
            
            if (e.esRecuperable()) {
                System.out.println("Fechas alternativas disponibles:");
                for (LocalDate alternativa : e.getAlternativas()) {
                    System.out.println("  - " + alternativa);
                }
            }
            
        } catch (ClienteBloqueadoException e) {
            System.out.println("No se puede procesar: " + e.getMessage());
            System.out.println("Contacte a soporte con código: " + e.getCodigoError());
            
        } catch (PagoFallidoException e) {
            System.out.println("Error de pago: " + e.getMessage());
            
            if (e.esRecuperable()) {
                System.out.println("Puede intentar nuevamente");
            } else {
                System.out.println("Verifique su método de pago");
            }
            
        } catch (ReservaException e) {
            // Catch genérico para cualquier otra excepción de reserva
            System.out.println("Error inesperado: " + e.getMessage());
            System.out.println("Código: " + e.getCodigoError());
            System.out.println("Timestamp: " + e.getTimestamp());
        }
    }
}
```

---

(resumen-java4)=
## Resumen

### Excepciones como Objetos

- Las excepciones son objetos completos con estado y comportamiento
- Pueden heredar, agregar atributos y sobrescribir métodos
- El polimorfismo permite captura genérica o específica

### Diseño de Jerarquías

- Crear una excepción base abstracta para el dominio
- Las subclases representan errores específicos
- Incluir información contextual como atributos
- Métodos como `esRecuperable()` guían el manejo

### Excepciones y Contratos

- Las excepciones son parte del contrato del método
- Documentar con `@throws` en Javadoc
- Validar precondiciones al inicio (fail fast)
- Respetar covarianza en subclases (Liskov)

### Traducción de Excepciones

- Traducir excepciones técnicas a excepciones de dominio
- Preservar la causa original con el constructor de dos argumentos
- Cada capa maneja excepciones de su nivel de abstracción

### Buenas Prácticas

| Práctica | Descripción |
| :--- | :--- |
| Captura específica | Evitar `catch (Exception e)` |
| No silenciar | Siempre manejar o loggear |
| Mensajes descriptivos | Incluir contexto y valores |
| Preservar causa | Usar constructor con `Throwable` |
| Factory methods | Para excepciones con múltiples causas |
| AutoCloseable | Para recursos que deben cerrarse |

---

(ejercicios-java4)=
## Ejercicios

```{exercise}
:label: ej-jerarquia-biblioteca
Diseñá una jerarquía de excepciones para un sistema de biblioteca:

- `BibliotecaException` (base abstracta) con código de error y método `puedeReintentar()`
- `LibroNoDisponibleException` con información del libro y fecha estimada de devolución
- `UsuarioSuspendidoException` con motivo y fecha de fin de suspensión
- `PrestamoVencidoException` con días de atraso y multa calculada

Implementá las clases y demostrá el manejo polimórfico.
```

```{exercise}
:label: ej-repositorio-traduccion
Implementá un repositorio que traduzca excepciones:

- Interfaz `Repositorio<T>` con métodos que lanzan `RepositorioException`
- Implementación `RepositorioArchivo` que internamente usa `IOException`
- Implementación `RepositorioMemoria` que no lanza excepciones técnicas
- El cliente no debe ver diferencia entre implementaciones

Demostrá el encadenamiento de excepciones y cómo acceder a la causa.
```

```{exercise}
:label: ej-recurso-autocloseable
Creá un recurso `ConexionSimulada` que implemente `AutoCloseable`:

- Simula conexión a un servicio externo
- Puede fallar al conectar, al operar, o al cerrar
- Implementá `Suppressed Exceptions` cuando falla tanto la operación como el cierre
- Demostrá uso correcto con try-with-resources
```

```{exercise}
:label: ej-validador-factory
Creá un sistema de validación con factory methods:

- `ValidacionException` con factory methods: `campoRequerido()`, `formatoInvalido()`, `fueraDeRango()`, `valorDuplicado()`
- Cada factory incluye información contextual apropiada
- Método `esCorregible()` que indica si el usuario puede corregir el error
- Demostrá uso en validación de formulario de registro
```

```{exercise}
:label: ej-interfaz-excepciones
Demostrá el principio de sustitución con excepciones:

- Interfaz `Procesador` con método `procesar() throws ProcesamientoException`
- Implementación `ProcesadorLocal` que no lanza excepciones
- Implementación `ProcesadorRemoto` que lanza `ProcesamientoException`
- Intentá crear una implementación que lance `SQLException` y explicá por qué no compila
```

---

:::{seealso}
- {ref}`excepciones-en-java` - Fundamentos de excepciones (try-catch, checked vs unchecked)
- {ref}`oop-contratos` - Diseño por contratos y precondiciones
- {ref}`java-herencia-polimorfismo` - Herencia e interfaces en Java
:::
