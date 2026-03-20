
# Serie 0x4 - Testing

(regla-0x4000)=
## `0x4000` - El test debe tener el mismo nombre que la clase con `Test` al final

### Explicación

Las clases de test deben seguir una convención de nombrado estricta: el nombre de la clase bajo prueba seguido del sufijo `Test`. Esto permite identificar rápidamente qué clase está siendo probada y facilita la navegación entre código de producción y sus tests.

### Justificación

1. **Navegación rápida**: Los IDEs modernos permiten saltar entre clase y test con atajos de teclado.
2. **Organización clara**: Facilita localizar tests en grandes proyectos.
3. **Convención estándar**: Herramientas de cobertura y reportes esperan este formato.
4. **Agrupación lógica**: Los exploradores de tests agrupan por clase automáticamente.
5. **Prevención de ambigüedad**: Evita confusión sobre qué clase se está probando.

### Sintaxis

```
<NombreClase>Test

Donde <NombreClase> es el nombre exacto de la clase bajo prueba
```

### Ejemplos correctos

```java
// Clase de producción
public class Calculadora {
    public int sumar(int a, int b) {
        return a + b;
    }
}

// Clase de test ✅
public class CalculadoraTest {
    @Test
    void testSumar() {
        Calculadora calc = new Calculadora();
        assertEquals(5, calc.sumar(2, 3));
    }
}
```

```java
// Clase de producción
public class GestorUsuarios {
    // ...
}

// Clase de test ✅
public class GestorUsuariosTest {
    // ...
}
```

```java
// Clase de producción
public class ValidadorEmail {
    // ...
}

// Clase de test ✅
public class ValidadorEmailTest {
    // ...
}
```

### Ejemplos incorrectos

```java
// ❌ Incorrecto - Plural
public class CalculadoraTests {
    // ...
}

// ❌ Incorrecto - Prefijo en lugar de sufijo
public class TestCalculadora {
    // ...
}

// ❌ Incorrecto - Nombre genérico
public class PruebasCalculadora {
    // ...
}

// ❌ Incorrecto - Sin relación clara
public class PruebasMatematicas {
    // Prueba Calculadora pero no es obvio
}

// ❌ Incorrecto - Abreviación
public class CalcTest {
    // La clase se llama Calculadora, no Calc
}
```

### Estructura de paquetes

Los tests deben estar en el mismo paquete que la clase bajo prueba, pero en el directorio `src/test/java`:

```
src/
├── main/
│   └── java/
│       └── ar/
│           └── unrn/
│               └── calculo/
│                   └── Calculadora.java
└── test/
    └── java/
        └── ar/
            └── unrn/
                └── calculo/
                    └── CalculadoraTest.java  ✅
```

:::{important}
El paquete del test debe ser **idéntico** al de la clase bajo prueba. Esto permite que el test acceda a miembros con visibilidad de paquete (`package-private`).
:::

### Casos especiales

#### Clases internas

Si probás una clase interna, incluir el nombre de la clase contenedora:

```java
// Clase de producción
public class Procesador {
    public static class ResultadoProcesamiento {
        // ...
    }
}

// Test ✅
public class ProcesadorResultadoProcesamientoTest {
    // ...
}
```

#### Tests de integración

Para tests de integración que prueban múltiples clases, usá un sufijo más descriptivo:

```java
// ✅ Test de integración
public class SistemaFacturacionIntegrationTest {
    // Prueba la integración entre Factura, Cliente, Producto
}
```

:::{note}
Esta regla aplica para tests unitarios. Los tests de integración pueden usar convenciones diferentes según el alcance de las pruebas.
:::

(regla-0x4001)=
## `0x4001` - El test debe seguir la estructura AAA (Arrange-Act-Assert)

### Explicación

Cada test debe organizarse en tres fases claramente diferenciadas: **Arrange** (preparar), **Act** (ejecutar) y **Assert** (verificar). Esta estructura proporciona un flujo lógico y predecible que facilita la comprensión y mantenimiento de los tests.

### Justificación

1. **Claridad**: La estructura explícita hace obvio qué está siendo probado y cómo.
2. **Mantenibilidad**: Facilita modificar y extender tests existentes.
3. **Depuración**: Cuando un test falla, es fácil identificar en qué fase ocurrió el problema.
4. **Estándar universal**: Reconocida en múltiples lenguajes y frameworks.
5. **Legibilidad**: Reduce la carga cognitiva al leer tests ajenos.

### Estructura

```
TEST = ARRANGE + ACT + ASSERT + [CLEANUP]

Donde:
- ARRANGE: Preparación del contexto y datos
- ACT: Ejecución de la operación bajo prueba
- ASSERT: Verificación de resultados
- CLEANUP: Limpieza opcional de recursos
```

### Las tres fases

#### Fase 1: Arrange (Preparar)

En esta fase se prepara todo lo necesario para ejecutar el test:

```java
@Test
void testCalcularDescuento_ConClientePremium_AplicaDescuentoDel20Porciento() {
    // Arrange: Preparar objetos y datos
    Cliente cliente = new Cliente("Juan", TipoCliente.PREMIUM);
    Producto producto = new Producto("Laptop", 1000.0);
    CarritoCompras carrito = new CarritoCompras(cliente);
    carrito.agregarProducto(producto);
    
    // ...continúa
}
```

:::{tip}
En la fase Arrange, creá todos los objetos necesarios, inicializá variables y configurá el estado requerido. Esta fase puede ser extensa para tests complejos.
:::

#### Fase 2: Act (Ejecutar)

En esta fase se ejecuta **una única acción** - el método bajo prueba:

```java
@Test
void testCalcularDescuento_ConClientePremium_AplicaDescuentoDel20Porciento() {
    // Arrange
    Cliente cliente = new Cliente("Juan", TipoCliente.PREMIUM);
    Producto producto = new Producto("Laptop", 1000.0);
    CarritoCompras carrito = new CarritoCompras(cliente);
    carrito.agregarProducto(producto);
    
    // Act: UNA llamada al método bajo prueba
    double precioFinal = carrito.calcularTotal();
    
    // ...continúa
}
```

:::{important}
La fase Act debe ser **mínima** - idealmente una sola línea. Si necesitás múltiples llamadas, probablemente debés dividir en múltiples tests.
:::

#### Fase 3: Assert (Verificar)

En esta fase se verifica que el resultado sea el esperado:

```java
@Test
void testCalcularDescuento_ConClientePremium_AplicaDescuentoDel20Porciento() {
    // Arrange
    Cliente cliente = new Cliente("Juan", TipoCliente.PREMIUM);
    Producto producto = new Producto("Laptop", 1000.0);
    CarritoCompras carrito = new CarritoCompras(cliente);
    carrito.agregarProducto(producto);
    
    // Act
    double precioFinal = carrito.calcularTotal();
    
    // Assert: Verificar resultado esperado
    assertEquals(800.0, precioFinal, 0.01, 
                "Cliente premium debe recibir 20% de descuento");
}
```

#### Fase 4: Cleanup (Limpiar) - Opcional

Cuando el test usa recursos externos, debe limpiarlos:

```java
@Test
void testGuardarArchivo_ConDatosValidos_CreaArchivo() throws IOException {
    // Arrange
    String nombreArchivo = "test_temporal.txt";
    String contenido = "Datos de prueba";
    GestorArchivos gestor = new GestorArchivos();
    
    try {
        // Act
        gestor.guardarArchivo(nombreArchivo, contenido);
        
        // Assert
        assertTrue(new File(nombreArchivo).exists());
        
    } finally {
        // Cleanup: Eliminar archivo de prueba
        new File(nombreArchivo).delete();
    }
}
```

:::{note}
JUnit 5 ofrece `@TempDir` para directorios temporales que se limpian automáticamente, reduciendo la necesidad de cleanup manual.
:::

### Ejemplos completos

#### Test simple con las tres fases

```java
public class CalculadoraTest {
    @Test
    void testSumar_ConDosNumerosPositivos_RetornaSuma() {
        // Arrange: Preparar calculadora y datos de entrada
        Calculadora calc = new Calculadora();
        int operando1 = 5;
        int operando2 = 7;
        
        // Act: Ejecutar operación
        int resultado = calc.sumar(operando1, operando2);
        
        // Assert: Verificar resultado
        assertEquals(12, resultado, "5 + 7 debe ser 12");
    }
}
```

#### Test con verificación de excepción

```java
@Test
void testDividir_CuandoDivisorEsCero_LanzaArithmeticException() {
    // Arrange: Preparar datos inválidos
    Calculadora calc = new Calculadora();
    int dividendo = 10;
    int divisor = 0;
    
    // Act & Assert: Verificar que se lance la excepción esperada
    assertThrows(ArithmeticException.class, () -> {
        calc.dividir(dividendo, divisor);
    }, "Dividir por cero debe lanzar ArithmeticException");
}
```

#### Test con setup complejo

```java
@Test
void testProcesarPedido_ConMultiplesProductos_CalculaTotalCorrectamente() {
    // Arrange: Setup complejo de múltiples objetos
    Cliente cliente = new Cliente("María", "maria@ejemplo.com");
    Producto prod1 = new Producto("Mouse", 25.0);
    Producto prod2 = new Producto("Teclado", 45.0);
    Producto prod3 = new Producto("Monitor", 150.0);
    
    Pedido pedido = new Pedido(cliente);
    pedido.agregarProducto(prod1, 2);  // 2 mouses
    pedido.agregarProducto(prod2, 1);  // 1 teclado
    pedido.agregarProducto(prod3, 1);  // 1 monitor
    
    GestorPedidos gestor = new GestorPedidos();
    
    // Act: Procesar el pedido
    ResumenPedido resumen = gestor.procesarPedido(pedido);
    
    // Assert: Verificar cálculos
    assertEquals(245.0, resumen.getTotal(), 0.01, 
                "Total: 2*25 + 1*45 + 1*150 = 245");
    assertEquals(3, resumen.getCantidadProductos(),
                "Debe haber 3 tipos de productos diferentes");
    assertEquals(4, resumen.getCantidadItems(),
                "Debe haber 4 items en total (2+1+1)");
}
```

### Separación visual con líneas en blanco

Para mejorar la legibilidad, separar las fases con líneas en blanco:

```java
@Test
void testRegistrarUsuario_ConDatosValidos_GuardaEnBaseDeDatos() {
    // Arrange
    String nombre = "Carlos";
    String email = "carlos@ejemplo.com";
    RepositorioUsuarios repo = new RepositorioUsuarios();
    
    // Act
    Usuario usuario = repo.registrar(nombre, email);
    
    // Assert
    assertNotNull(usuario);
    assertEquals(nombre, usuario.getNombre());
    assertEquals(email, usuario.getEmail());
    assertTrue(usuario.getId() > 0, "El ID debe ser asignado");
}
```

### Comentarios explícitos opcionales

Aunque no es obligatorio, es recomendable incluir comentarios que marquen cada fase:

```java
@Test
void testBuscar_ConElementoExistente_RetornaIndice() {
    // Arrange: Crear lista con elementos conocidos
    List<String> lista = Arrays.asList("manzana", "banana", "pera");
    Buscador buscador = new Buscador();
    
    // Act: Buscar elemento que sabemos existe
    int indice = buscador.buscar(lista, "banana");
    
    // Assert: Debe retornar índice 1
    assertEquals(1, indice);
}
```

:::{tip}
Cuando un test es largo o complejo, los comentarios `// Arrange`, `// Act`, `// Assert` ayudan muchísimo a entender la estructura rápidamente.
:::

### Casos especiales

#### Tests que verifican excepciones

En tests de excepciones, Act y Assert se combinan:

```java
@Test
void testCrearUsuario_ConEmailInvalido_LanzaValidacionException() {
    // Arrange
    String nombre = "Ana";
    String emailInvalido = "no-es-un-email";
    RegistroUsuarios registro = new RegistroUsuarios();
    
    // Act & Assert combinados
    assertThrows(ValidacionException.class, () -> {
        registro.crearUsuario(nombre, emailInvalido);
    });
}
```

#### Tests con verificación de estado

```java
@Test
void testAgregarProducto_AlCarritoVacio_IncrementaCantidad() {
    // Arrange
    CarritoCompras carrito = new CarritoCompras();
    Producto producto = new Producto("Libro", 20.0);
    int cantidadInicial = carrito.getCantidadItems();
    
    // Act
    carrito.agregarProducto(producto);
    
    // Assert: Verificar cambio de estado
    assertEquals(cantidadInicial + 1, carrito.getCantidadItems());
    assertTrue(carrito.contieneProducto(producto));
}
```

### Anti-patrones

#### Mezclar fases ❌

```java
@Test
void testMalEstructurado() {
    // ❌ Arrange y Act mezclados
    Calculadora calc = new Calculadora();
    int resultado = calc.sumar(2, 3);  // Act prematuro
    
    int otroNumero = 10;  // Más Arrange después de Act
    
    assertEquals(5, resultado);  // Assert
    assertEquals(15, calc.sumar(resultado, otroNumero));  // Otro Act+Assert
}
```

#### Múltiples Acts ❌

```java
@Test
void testMultiplesOperaciones() {
    // Arrange
    Calculadora calc = new Calculadora();
    
    // ❌ Múltiples Acts - debería ser tests separados
    int suma = calc.sumar(2, 3);
    int resta = calc.restar(5, 2);
    int mult = calc.multiplicar(3, 4);
    
    // Assert mezclado
    assertEquals(5, suma);
    assertEquals(3, resta);
    assertEquals(12, mult);
}
```

:::{warning}
Si tu test tiene múltiples llamadas al código bajo prueba, probablemente debas dividirlo en varios tests independientes. Ver regla {ref}`regla-0x4002`.
:::

(regla-0x4002)=
## `0x4002` - Una llamada a método en cada caso de prueba

### Explicación

Cada test debe verificar **una única funcionalidad** realizando una sola llamada al método bajo prueba. Un test debe tener un único propósito y verificar una sola cosa. Tests con múltiples llamadas son más difíciles de mantener, depurar y entender cuando fallan.

### Justificación

1. **Foco único**: Cada test verifica un comportamiento específico.
2. **Diagnóstico preciso**: Cuando falla, es obvio qué funcionalidad está rota.
3. **Mantenibilidad**: Modificar un comportamiento requiere tocar un solo test.
4. **Principio SRP**: Single Responsibility Principle aplicado a tests.
5. **Reportes claros**: Los resultados de tests indican exactamente qué funciona y qué no.

### Sintaxis conceptual

```
TEST = ARRANGE + UNA_LLAMADA + ASSERT

Donde UNA_LLAMADA es la ejecución del método bajo prueba
```

### Ejemplos correctos

#### Test simple con una llamada

```java
public class CalculadoraTest {
    @Test
    void testSumar_ConDosNumerosPositivos_RetornaSuma() {
        // Arrange
        Calculadora calc = new Calculadora();
        
        // Act: UNA llamada
        int resultado = calc.sumar(5, 3);
        
        // Assert
        assertEquals(8, resultado);
    }
    
    @Test
    void testRestar_ConMinuendo MayorQueSustraendo_RetornaDiferenciaPositiva() {
        // Arrange
        Calculadora calc = new Calculadora();
        
        // Act: UNA llamada
        int resultado = calc.restar(10, 4);
        
        // Assert
        assertEquals(6, resultado);
    }
}
```

#### Verificación de múltiples propiedades del resultado

Es válido tener múltiples assertions sobre el **mismo resultado**:

```java
@Test
void testCrearUsuario_ConDatosValidos_RetornaUsuarioCompleto() {
    // Arrange
    String nombre = "María";
    String email = "maria@ejemplo.com";
    RegistroUsuarios registro = new RegistroUsuarios();
    
    // Act: UNA llamada
    Usuario usuario = registro.crearUsuario(nombre, email);
    
    // Assert: Múltiples verificaciones sobre el MISMO objeto retornado
    assertNotNull(usuario, "No debe retornar null");
    assertEquals(nombre, usuario.getNombre(), "El nombre debe coincidir");
    assertEquals(email, usuario.getEmail(), "El email debe coincidir");
    assertTrue(usuario.getId() > 0, "Debe asignar un ID positivo");
    assertNotNull(usuario.getFechaCreacion(), "Debe asignar fecha de creación");
}
```

:::{note}
Tener múltiples `assert` está bien siempre que todos verifiquen aspectos del **mismo resultado** de la **misma llamada**.
:::

#### Verificación de estado del objeto

También es válido verificar el estado resultante después de una operación:

```java
@Test
void testAgregarProducto_AlCarritoVacio_ActualizaEstadoCarrito() {
    // Arrange
    CarritoCompras carrito = new CarritoCompras();
    Producto producto = new Producto("Mouse", 25.0);
    
    // Act: UNA llamada al método bajo prueba
    carrito.agregarProducto(producto);
    
    // Assert: Verificar cambios en el estado del objeto
    assertEquals(1, carrito.getCantidadItems());
    assertTrue(carrito.contieneProducto(producto));
    assertEquals(25.0, carrito.getTotal(), 0.01);
}
```

### Ejemplos incorrectos

#### Múltiples llamadas al código bajo prueba ❌

```java
@Test
void testOperacionesMatematicas() {  // ❌ Nombre genérico
    // Arrange
    Calculadora calc = new Calculadora();
    
    // Act: ❌ MÚLTIPLES llamadas - viola la regla
    int suma = calc.sumar(2, 3);
    int resta = calc.restar(5, 2);
    int multiplicacion = calc.multiplicar(3, 4);
    int division = calc.dividir(10, 2);
    
    // Assert
    assertEquals(5, suma);
    assertEquals(3, resta);
    assertEquals(12, multiplicacion);
    assertEquals(5, division);
}
```

**Problema**: Si alguna assertion falla, no sabés cuál de las cuatro operaciones tiene el bug. Además, si falla la primera assertion, las demás no se ejecutan.

#### Solución correcta: Tests separados ✅

```java
@Test
void testSumar_ConDosNumerosPositivos_RetornaSuma() {
    // Arrange
    Calculadora calc = new Calculadora();
    
    // Act
    int resultado = calc.sumar(2, 3);
    
    // Assert
    assertEquals(5, resultado);
}

@Test
void testRestar_ConMinuendoMayorQueSustraendo_RetornaDiferencia() {
    // Arrange
    Calculadora calc = new Calculadora();
    
    // Act
    int resultado = calc.restar(5, 2);
    
    // Assert
    assertEquals(3, resultado);
}

@Test
void testMultiplicar_ConDosFactores_RetornaProducto() {
    // Arrange
    Calculadora calc = new Calculadora();
    
    // Act
    int resultado = calc.multiplicar(3, 4);
    
    // Assert
    assertEquals(12, resultado);
}

@Test
void testDividir_ConDivisorNoNulo_RetornaCociente() {
    // Arrange
    Calculadora calc = new Calculadora();
    
    // Act
    int resultado = calc.dividir(10, 2);
    
    // Assert
    assertEquals(5, resultado);
}
```

### Casos especiales permitidos

#### Setup compartido con `@BeforeEach`

Cuando múltiples tests requieren el mismo setup, usá `@BeforeEach`:

```java
public class GestorCuentasTest {
    private GestorCuentas gestor;
    private Cliente clientePrueba;
    
    @BeforeEach
    void setUp() {
        // Setup común para todos los tests
        gestor = new GestorCuentas();
        clientePrueba = new Cliente("Juan", "juan@ejemplo.com");
    }
    
    @Test
    void testCrearCuenta_ConClienteNuevo_RetornaCuentaActiva() {
        // Arrange: Ya tenemos gestor y cliente desde setUp()
        double saldoInicial = 100.0;
        
        // Act: UNA llamada
        Cuenta cuenta = gestor.crearCuenta(clientePrueba, saldoInicial);
        
        // Assert
        assertEquals(EstadoCuenta.ACTIVA, cuenta.getEstado());
        assertEquals(saldoInicial, cuenta.getSaldo(), 0.01);
    }
    
    @Test
    void testCerrarCuenta_ConSaldoCero_CambiEstadoACerrada() {
        // Arrange
        Cuenta cuenta = gestor.crearCuenta(clientePrueba, 0.0);
        
        // Act: UNA llamada
        gestor.cerrarCuenta(cuenta);
        
        // Assert
        assertEquals(EstadoCuenta.CERRADA, cuenta.getEstado());
    }
}
```

:::{important}
Las llamadas en `@BeforeEach` no cuentan como llamadas "bajo prueba". Solo la llamada en la fase **Act** del test cuenta para esta regla.
:::

#### Llamadas auxiliares en Arrange

Podés hacer llamadas auxiliares durante la preparación, siempre que no sean el foco del test:

```java
@Test
void testTransferir_EntreDosCuentas_ActualizaSaldos() {
    // Arrange: Llamadas auxiliares para preparar el contexto
    GestorCuentas gestor = new GestorCuentas();
    Cuenta origen = gestor.crearCuenta("Cuenta Origen", 500.0);  // Auxiliar
    Cuenta destino = gestor.crearCuenta("Cuenta Destino", 100.0); // Auxiliar
    double montoTransferencia = 200.0;
    
    // Act: UNA llamada al método bajo prueba
    gestor.transferir(origen, destino, montoTransferencia);
    
    // Assert: Verificar resultado de la transferencia
    assertEquals(300.0, origen.getSaldo(), 0.01);
    assertEquals(300.0, destino.getSaldo(), 0.01);
}
```

:::{note}
La regla se refiere a **una llamada al método que estás probando**, no a todas las llamadas posibles. Podés hacer llamadas auxiliares para preparar el contexto.
:::

### Tests parametrizados como excepción

JUnit 5 permite tests parametrizados que prueban el mismo comportamiento con diferentes datos:

```java
@ParameterizedTest
@CsvSource({
    "2, 3, 5",
    "10, 20, 30",
    "-5, 5, 0",
    "0, 0, 0"
})
void testSumar_ConDiferentesValores_RetornaResultadoCorrecto(
        int a, int b, int esperado) {
    // Arrange
    Calculadora calc = new Calculadora();
    
    // Act: UNA llamada por cada ejecución parametrizada
    int resultado = calc.sumar(a, b);
    
    // Assert
    assertEquals(esperado, resultado);
}
```

:::{tip}
Los tests parametrizados son una forma elegante de probar el mismo comportamiento con múltiples entradas sin violar esta regla.
:::

### Beneficios de seguir esta regla

```java
// ❌ Test malo: Prueba múltiples cosas
@Test
void testOperaciones() {
    Calculadora calc = new Calculadora();
    assertEquals(5, calc.sumar(2, 3));
    assertEquals(3, calc.restar(5, 2));
    assertEquals(12, calc.multiplicar(3, 4));
}
// Si falla la suma, nunca sabemos si resta y multiplicación funcionan

// ✅ Tests buenos: Uno por funcionalidad
@Test
void testSumar_ConDosNumeros_RetornaSuma() {
    Calculadora calc = new Calculadora();
    assertEquals(5, calc.sumar(2, 3));
}

@Test
void testRestar_ConDosNumeros_RetornaResta() {
    Calculadora calc = new Calculadora();
    assertEquals(3, calc.restar(5, 2));
}

@Test
void testMultiplicar_ConDosNumeros_RetornaProducto() {
    Calculadora calc = new Calculadora();
    assertEquals(12, calc.multiplicar(3, 4));
}
// Cada test falla independientemente, diagnóstico preciso
```

(regla-0x4003)=
## `0x4003` - Los nombres de tests deben ser descriptivos y usar convención `test<Accion><Condicion><ResultadoEsperado>`

### Explicación

Los nombres de los métodos de test deben ser altamente descriptivos y seguir una convención que haga obvio: **qué se está probando**, **bajo qué condiciones** y **qué resultado se espera**. A diferencia del código de producción donde se prefieren nombres cortos, en tests se privilegia la claridad absoluta sobre la brevedad.

### Justificación

1. **Documentación viva**: Los nombres de tests documentan el comportamiento esperado del sistema.
2. **Reportes claros**: Cuando un test falla, el nombre indica exactamente qué funcionalidad está rota.
3. **Especificación ejecutable**: Los nombres actúan como especificación del comportamiento del código.
4. **Mantenimiento**: Facilita entender qué hace cada test sin leer el código completo.
5. **Diseño guiado por tests**: Nombres descriptivos ayudan a pensar en casos límite y condiciones.

### Convención recomendada

```
test<MetodoAProbar>_<CondicionOContexto>_<ResultadoEsperado>

Componentes:
- test: Prefijo estándar (obligatorio en JUnit 3/4, opcional en JUnit 5)
- MetodoAProbar: Nombre del método que se está probando
- CondicionOContexto: Bajo qué circunstancias
- ResultadoEsperado: Qué debe suceder
```

### Ejemplos correctos

#### Tests de métodos que retornan valores

```java
public class CalculadoraTest {
    @Test
    void testSumar_ConDosNumerosPositivos_RetornaSuma() {
        Calculadora calc = new Calculadora();
        assertEquals(8, calc.sumar(5, 3));
    }
    
    @Test
    void testSumar_ConUnNumeroNegativo_RetornaResultadoCorrecto() {
        Calculadora calc = new Calculadora();
        assertEquals(2, calc.sumar(5, -3));
    }
    
    @Test
    void testDividir_ConDosNumerosValidos_RetornaCociente() {
        Calculadora calc = new Calculadora();
        assertEquals(5.0, calc.dividir(10.0, 2.0), 0.001);
    }
    
    @Test
    void testDividir_ConDivisorCero_LanzaArithmeticException() {
        Calculadora calc = new Calculadora();
        assertThrows(ArithmeticException.class, () -> calc.dividir(10, 0));
    }
}
```

#### Tests de métodos que modifican estado

```java
public class ListaTest {
    @Test
    void testAgregar_EnListaVacia_IncrementaTamanioAUno() {
        Lista<String> lista = new Lista<>();
        lista.agregar("elemento");
        assertEquals(1, lista.tamanio());
    }
    
    @Test
    void testRemover_ElementoExistente_DecrementaTamanio() {
        Lista<String> lista = new Lista<>();
        lista.agregar("elemento");
        lista.remover("elemento");
        assertEquals(0, lista.tamanio());
    }
    
    @Test
    void testRemover_ElementoNoExistente_NoModificaLista() {
        Lista<String> lista = new Lista<>();
        lista.agregar("elemento1");
        lista.remover("elemento2");
        assertEquals(1, lista.tamanio());
    }
}
```

#### Tests de métodos booleanos

```java
public class ValidadorEmailTest {
    @Test
    void testEsValido_ConEmailCorrecto_RetornaTrue() {
        ValidadorEmail validador = new ValidadorEmail();
        assertTrue(validador.esValido("usuario@ejemplo.com"));
    }
    
    @Test
    void testEsValido_SinArroba_RetornaFalse() {
        ValidadorEmail validador = new ValidadorEmail();
        assertFalse(validador.esValido("usuarioejemplo.com"));
    }
    
    @Test
    void testEsValido_SinDominio_RetornaFalse() {
        ValidadorEmail validador = new ValidadorEmail();
        assertFalse(validador.esValido("usuario@"));
    }
    
    @Test
    void testEsValido_ConEspacios_RetornaFalse() {
        ValidadorEmail validador = new ValidadorEmail();
        assertFalse(validador.esValido("usuario @ejemplo.com"));
    }
}
```

### Ejemplos incorrectos

#### Nombres demasiado genéricos ❌

```java
// ❌ No indica qué se está probando
@Test
void test1() {
    // ...
}

// ❌ No indica condición ni resultado esperado
@Test
void testSumar() {
    // ...
}

// ❌ Nombre ambiguo
@Test
void testCalculadora() {
    // ¿Qué método? ¿Qué condición?
}

// ❌ No indica resultado esperado
@Test
void testDividirPorCero() {
    // ¿Debe fallar? ¿Retornar cero? ¿Retornar infinito?
}
```

#### Nombres sin estructura ❌

```java
// ❌ Sin convención clara
@Test
void sumaTest() {
    // ...
}

// ❌ Orden incorrecto
@Test
void cuandoSeAgregaUnElementoLaListaCreceDeTamanio() {
    // Demasiado narrativo, difícil de escanear visualmente
}

// ❌ Abreviaciones innecesarias
@Test
void testCalcDescCli() {
    // ¿Qué significa? No es obvio
}
```

### Convenciones alternativas válidas

Además de la convención principal, estas alternativas son aceptables:

#### Notación con guiones bajos para legibilidad

```java
@Test
void test_sumar_con_dos_numeros_positivos_retorna_suma() {
    // Algunos equipos prefieren guiones bajos para mejorar legibilidad
    Calculadora calc = new Calculadora();
    assertEquals(8, calc.sumar(5, 3));
}
```

#### Notación narrativa (Given-When-Then)

```java
@Test
void givenDosNumerosPositivos_whenSumar_thenRetornaSuma() {
    // Inspirado en BDD (Behavior-Driven Development)
    Calculadora calc = new Calculadora();
    assertEquals(8, calc.sumar(5, 3));
}
```

#### Notación solo descriptiva (sin prefijo test)

```java
// JUnit 5 permite omitir el prefijo "test"
@Test
void sumarDosNumerosPositivosRetornaSuma() {
    Calculadora calc = new Calculadora();
    assertEquals(8, calc.sumar(5, 3));
}
```

:::{important}
Lo crucial es la **consistencia dentro del proyecto**. Elegí una convención y mantenela en todos los tests del curso.
:::

### Estructura recomendada por componente

#### Acción (Qué se prueba)

Usar el nombre del método exacto:

```java
testSumar_...           // Para método sumar()
testDividir_...         // Para método dividir()
testCalcularDescuento_...  // Para método calcularDescuento()
testCrearUsuario_...    // Para método crearUsuario()
```

#### Condición (Bajo qué circunstancias)

Describir el contexto o precondición:

```java
testDividir_ConDivisorCero_...
testBuscar_EnListaVacia_...
testCalcularDescuento_ConClientePremium_...
testRegistrarUsuario_ConEmailDuplicado_...
testProcesar_ConConexionCerrada_...
```

#### Resultado esperado (Qué debe suceder)

Describir claramente el comportamiento esperado:

```java
..._RetornaCociente
..._LanzaIllegalArgumentException
..._RetornaListaVacia
..._GuardaEnBaseDeDatos
..._NoModificaElEstado
..._RetornaNull
..._AplicaDescuentoDel20Porciento
```

### Ejemplos del mundo real

#### Test de repositorio

```java
public class RepositorioUsuariosTest {
    @Test
    void testBuscarPorId_ConIdExistente_RetornaUsuario() {
        // ...
    }
    
    @Test
    void testBuscarPorId_ConIdNoExistente_RetornaNull() {
        // ...
    }
    
    @Test
    void testGuardar_ConUsuarioNuevo_AsignaId() {
        // ...
    }
    
    @Test
    void testGuardar_ConUsuarioExistente_ActualizaDatos() {
        // ...
    }
    
    @Test
    void testEliminar_ConIdExistente_RetornaTrue() {
        // ...
    }
    
    @Test
    void testEliminar_ConIdNoExistente_RetornaFalse() {
        // ...
    }
}
```

#### Test de validador

```java
public class ValidadorContrasenaTest {
    @Test
    void testEsSegura_ConLongitudMinima8Caracteres_RetornaTrue() {
        // ...
    }
    
    @Test
    void testEsSegura_ConMenosDe8Caracteres_RetornaFalse() {
        // ...
    }
    
    @Test
    void testEsSegura_SinMayusculas_RetornaFalse() {
        // ...
    }
    
    @Test
    void testEsSegura_SinNumeros_RetornaFalse() {
        // ...
    }
    
    @Test
    void testEsSegura_ConTodosLosRequisitos_RetornaTrue() {
        // ...
    }
    
    @Test
    void testValidar_ConContrasenaNull_LanzaIllegalArgumentException() {
        // ...
    }
}
```

#### Test de clase con lógica de negocio

```java
public class CarritoComprasTest {
    @Test
    void testCalcularTotal_ConCarritoVacio_RetornaCero() {
        // ...
    }
    
    @Test
    void testCalcularTotal_ConUnProducto_RetornaPrecioProducto() {
        // ...
    }
    
    @Test
    void testCalcularTotal_ConMultiplesProductos_RetornaSumaPrecios() {
        // ...
    }
    
    @Test
    void testAplicarDescuento_ConCodigoValido_ReduceTotal() {
        // ...
    }
    
    @Test
    void testAplicarDescuento_ConCodigoInvalido_LanzaDescuentoInvalidoException() {
        // ...
    }
    
    @Test
    void testAplicarDescuento_ConCodigoYaUsado_LanzaDescuentoException() {
        // ...
    }
}
```

### Patrones de nombres para casos comunes

#### Valores límite

```java
testCalcular_ConValorCero_...
testCalcular_ConValorMaximo_...
testCalcular_ConValorMinimo_...
testCalcular_ConValorNegativo_...
```

#### Colecciones

```java
testProcesar_ConListaVacia_...
testProcesar_ConUnSoloElemento_...
testProcesar_ConMultiplesElementos_...
testProcesar_ConListaNull_...
```

#### Estados de objeto

```java
testOperacion_ConObjetoNuevo_...
testOperacion_ConObjetoInicializado_...
testOperacion_ConObjetoModificado_...
testOperacion_ConObjetoNull_...
```

#### Operaciones de modificación

```java
testAgregar_..._IncrementaTamanio
testRemover_..._DecrementaTamanio
testModificar_..._ActualizaValor
testLimpiar_..._DejaObjetoEnEstadoInicial
```

### Longitud de nombres

Aunque los nombres de tests son naturalmente más largos que los del código de producción, intentá mantener un equilibrio:

```java
// ✅ Descriptivo pero razonable
@Test
void testDividir_ConDivisorCero_LanzaArithmeticException() {
    // ...
}

// ⚠️ Excesivamente largo - considerar simplificar
@Test
void testCalcularElDescuentoAplicableParaClientesPremiumDuranteElMesDeSeptiembre() {
    // Demasiado específico, dificulta refactoring
}

// ✅ Mejor versión simplificada
@Test
void testCalcularDescuento_ClientePremiumEnSeptiembre_AplicaDescuentoEspecial() {
    // Más conciso pero sigue siendo claro
}
```

:::{tip}
Si el nombre del test supera los 80-100 caracteres, considerá si no estás siendo demasiado específico o si el método bajo prueba hace demasiadas cosas.
:::

### Agrupación de tests relacionados

Para tests relacionados, usar nombres que se agrupen naturalmente:

```java
public class ValidadorTest {
    // Grupo: tests de validación de formato
    @Test
    void testValidarFormato_ConFormatoCorrecto_RetornaTrue() { }
    
    @Test
    void testValidarFormato_ConFormatoIncorrecto_RetornaFalse() { }
    
    // Grupo: tests de validación de longitud
    @Test
    void testValidarLongitud_ConLongitudMinima_RetornaTrue() { }
    
    @Test
    void testValidarLongitud_MenorAMinima_RetornaFalse() { }
    
    @Test
    void testValidarLongitud_MayorAMaxima_RetornaFalse() { }
    
    // Grupo: tests de validación completa
    @Test
    void testValidar_ConTodosLosCriteriosCumplidos_RetornaTrue() { }
    
    @Test
    void testValidar_ConAlgunCriterioIncumplido_RetornaFalse() { }
}
```

### Anotación `@DisplayName` como alternativa

JUnit 5 ofrece `@DisplayName` para nombres más legibles en reportes:

```java
public class CalculadoraTest {
    @Test
    @DisplayName("Sumar dos números positivos retorna la suma correcta")
    void testSumarDosNumerosPositivos() {
        Calculadora calc = new Calculadora();
        assertEquals(8, calc.sumar(5, 3));
    }
    
    @Test
    @DisplayName("Dividir por cero lanza ArithmeticException")
    void testDividirPorCero() {
        Calculadora calc = new Calculadora();
        assertThrows(ArithmeticException.class, () -> calc.dividir(10, 0));
    }
}
```

:::{note}
Usar `@DisplayName` permite nombres de método más cortos mientras mantenés descripciones legibles en reportes. Sin embargo, el nombre del método debe seguir siendo descriptivo.
:::

### Tests parametrizados

Los tests parametrizados pueden tener nombres más genéricos porque la parametrización añade contexto:

```java
@ParameterizedTest
@DisplayName("Sumar diferentes pares de números retorna el resultado correcto")
@CsvSource({
    "2, 3, 5",
    "10, 20, 30",
    "-5, 5, 0",
    "0, 0, 0",
    "-10, -5, -15"
})
void testSumar_ConDiferentesValores_RetornaResultadoCorrecto(
        int a, int b, int esperado) {
    Calculadora calc = new Calculadora();
    assertEquals(esperado, calc.sumar(a, b));
}
```

### Anti-patrones comunes

#### Nombres sin información ❌

```java
// ❌ Totalmente inútil
@Test
void test1() { }

@Test
void test2() { }

@Test
void testA() { }

@Test
void testMetodo() { }
```

#### Nombres ambiguos ❌

```java
// ❌ ¿Qué debería hacer?
@Test
void testCalcular() { }

// ❌ ¿Qué se verifica?
@Test
void testUsuario() { }

// ❌ ¿Cuál es el caso específico?
@Test
void testValidacion() { }
```

#### Nombres que describen implementación en lugar de comportamiento ❌

```java
// ❌ Describe cómo funciona internamente, no qué hace
@Test
void testIteraSobreArregloYSumaElementos() { }

// ✅ Describe qué hace desde perspectiva del usuario
@Test
void testCalcularSuma_ConArregloDeNumeros_RetornaSumaTotal() { }
```

### Guía práctica

Para nombrar un test, hacete estas preguntas:

1. **¿Qué método estoy probando?** → Parte de "Acción"
2. **¿Bajo qué condiciones?** → Parte de "Condición"
3. **¿Qué debería pasar?** → Parte de "Resultado"

**Ejemplo práctico:**

```
1. Método: buscarPorEmail()
2. Condición: Con un email que existe en la base
3. Resultado: Retorna el usuario correspondiente

→ testBuscarPorEmail_ConEmailExistente_RetornaUsuario
```

```
1. Método: procesarPago()
2. Condición: Con saldo insuficiente
3. Resultado: Lanza SaldoInsuficienteException

→ testProcesarPago_ConSaldoInsuficiente_LanzaSaldoInsuficienteException
```

### Lectura del reporte de tests

Buenos nombres producen reportes auto-explicativos:

```
CalculadoraTest
  ✅ testSumar_ConDosNumerosPositivos_RetornaSuma
  ✅ testSumar_ConUnNumeroNegativo_RetornaResultadoCorrecto
  ✅ testDividir_ConDosNumerosValidos_RetornaCociente
  ❌ testDividir_ConDivisorCero_LanzaArithmeticException
  ✅ testMultiplicar_ConFactorCero_RetornaCero
```

Al ver el reporte, inmediatamente sabés que la división por cero no está funcionando correctamente.

:::{important}
El nombre del test debe ser tan descriptivo que alguien pueda entender qué funcionalidad está rota **sin leer el código del test**.
:::

(regla-0x4004)=
## `0x4004` - Los tests no deben tener lógica condicional

### Explicación

Los tests no deben contener estructuras de control de flujo como `if`, `else`, `switch`, `for`, `while` o `do-while`. Si un test necesita lógica condicional, probablemente está intentando probar múltiples escenarios en un solo test y debe dividirse en tests separados.

### Justificación

1. **Simplicidad**: Los tests deben ser simples y directos, sin caminos alternativos.
2. **Determinismo**: Sin condicionales, el test siempre ejecuta el mismo código.
3. **Cobertura clara**: Cada test cubre un camino específico, sin ambigüedad.
4. **Fallos precisos**: Cuando falla, sabés exactamente qué escenario está roto.
5. **Tests no testeables**: Si el test tiene lógica, ¿quién prueba el test?

### Anti-patrón: Condicional en test ❌

```java
// ❌ INCORRECTO: Lógica condicional en el test
@Test
void testCalcular() {
    Calculadora calc = new Calculadora();
    int numero = 5;
    int resultado = calc.calcular(numero);
    
    if (numero % 2 == 0) {
        assertEquals(numero * 2, resultado);
    } else {
        assertEquals(numero * 3, resultado);
    }
}
```

### Solución correcta: Tests separados ✅

```java
// ✅ CORRECTO: Test separado para cada caso
@Test
void testCalcular_ConNumeroPar_RetornaDoble() {
    Calculadora calc = new Calculadora();
    int numeroPar = 4;
    
    int resultado = calc.calcular(numeroPar);
    
    assertEquals(8, resultado);
}

@Test
void testCalcular_ConNumeroImpar_RetornaTriple() {
    Calculadora calc = new Calculadora();
    int numeroImpar = 5;
    
    int resultado = calc.calcular(numeroImpar);
    
    assertEquals(15, resultado);
}
```

### Anti-patrón: Bucle en test ❌

```java
// ❌ INCORRECTO: Bucle en el test
@Test
void testSumar() {
    Calculadora calc = new Calculadora();
    
    for (int i = 0; i < 10; i++) {
        int resultado = calc.sumar(i, i);
        assertEquals(i * 2, resultado);
    }
}
```

### Solución: Test parametrizado ✅

```java
// ✅ CORRECTO: Test parametrizado
@ParameterizedTest
@CsvSource({
    "0, 0, 0",
    "1, 1, 2",
    "5, 5, 10",
    "9, 9, 18"
})
void testSumar_ConNumerosSumadosASiMismos_RetornaElDoble(
        int numero, int mismo, int esperado) {
    Calculadora calc = new Calculadora();
    assertEquals(esperado, calc.sumar(numero, mismo));
}
```

:::{tip}
Si te encontrás escribiendo un `if` o `for` en un test, considerá usar tests parametrizados o dividir en múltiples tests independientes.
:::

(regla-0x4005)=
## `0x4005` - Cada test debe ser independiente y poder ejecutarse en cualquier orden

### Explicación

Los tests deben ser completamente independientes entre sí: cada test debe preparar su propio estado, ejecutar su verificación y limpiar recursos sin depender de que otros tests se hayan ejecutado antes o después. El orden de ejecución de los tests no debe afectar los resultados.

### Justificación

1. **Ejecución paralela**: Los frameworks modernos ejecutan tests en paralelo para velocidad.
2. **Depuración**: Podés ejecutar un solo test sin preocuparte por setup previo.
3. **Mantenimiento**: Agregar o eliminar tests no afecta a los demás.
4. **Determinismo**: Los resultados son consistentes independientemente del orden.
5. **Aislamiento de fallos**: Un test que falla no hace fallar a otros.

### Principio de independencia

```
CADA TEST = SETUP + EJECUCIÓN + VERIFICACIÓN + CLEANUP

No debe existir:
- Estado compartido mutable entre tests
- Dependencias de orden de ejecución
- Efectos colaterales que afecten otros tests
```

### Ejemplos correctos

#### Independencia con @BeforeEach

```java
public class CuentaBancariaTest {
    private CuentaBancaria cuenta;
    
    @BeforeEach
    void setUp() {
        // Cada test recibe una cuenta nueva e independiente
        cuenta = new CuentaBancaria("12345", 1000.0);
    }
    
    @Test
    void testDepositar_ConMontoPositivo_IncrementaSaldo() {
        cuenta.depositar(500.0);
        assertEquals(1500.0, cuenta.getSaldo(), 0.01);
    }
    
    @Test
    void testRetirar_ConMontoValido_DecrementaSaldo() {
        cuenta.retirar(300.0);
        assertEquals(700.0, cuenta.getSaldo(), 0.01);
    }
    
    @Test
    void testRetirar_ConMontoMayorASaldo_LanzaSaldoInsuficienteException() {
        assertThrows(SaldoInsuficienteException.class, 
                     () -> cuenta.retirar(1500.0));
    }
}
```

:::{note}
Cada test inicia con una cuenta en el mismo estado (saldo 1000.0) gracias a `@BeforeEach`. Los tests pueden ejecutarse en cualquier orden.
:::

#### Independencia sin estado compartido

```java
public class CalculadoraTest {
    @Test
    void testSumar_ConDosNumeros_RetornaSuma() {
        // Cada test crea su propia instancia
        Calculadora calc = new Calculadora();
        assertEquals(5, calc.sumar(2, 3));
    }
    
    @Test
    void testRestar_ConDosNumeros_RetornaResta() {
        // Independiente del test anterior
        Calculadora calc = new Calculadora();
        assertEquals(2, calc.restar(5, 3));
    }
}
```

### Anti-patrón: Estado compartido mutable ❌

#### Problema: Tests que comparten estado

```java
// ❌ INCORRECTO: Estado compartido mutable
public class ContadorTest {
    private static int contador = 0;  // ❌ Estado compartido
    
    @Test
    void testIncrementar() {
        contador++;  // ❌ Modifica estado compartido
        assertEquals(1, contador);  // ❌ Depende del orden
    }
    
    @Test
    void testIncrementarDosVeces() {
        contador += 2;  // ❌ Modifica estado compartido
        assertEquals(3, contador);  // ❌ Asume que testIncrementar() se ejecutó primero
    }
}
```

**Problema:** Si `testIncrementarDosVeces` se ejecuta primero, `contador` sería 2, no 3. Si se ejecutan en paralelo, hay condiciones de carrera.

#### Solución correcta ✅

```java
// ✅ CORRECTO: Cada test es independiente
public class ContadorTest {
    @Test
    void testIncrementar_DesdeEstadoInicial_ResultaEnUno() {
        Contador contador = new Contador();  // ✅ Instancia propia
        contador.incrementar();
        assertEquals(1, contador.getValor());
    }
    
    @Test
    void testIncrementar_DosVecesDesdeEstadoInicial_ResultaEnDos() {
        Contador contador = new Contador();  // ✅ Instancia propia
        contador.incrementar();
        contador.incrementar();
        assertEquals(2, contador.getValor());
    }
}
```

### Anti-patrón: Dependencia de orden ❌

#### Problema: Tests que dependen de ejecución previa

```java
// ❌ INCORRECTO: Tests con dependencia de orden
public class BaseDeDatosTest {
    private static BaseDeDatos bd;
    
    @Test
    void test1_Conectar() {
        bd = new BaseDeDatos();  // ❌ Inicializa variable estática
        bd.conectar();
        assertTrue(bd.estaConectada());
    }
    
    @Test
    void test2_GuardarDatos() {
        // ❌ Asume que test1_Conectar() ya se ejecutó
        bd.guardar("dato");
        assertEquals(1, bd.contarRegistros());
    }
    
    @Test
    void test3_ConsultarDatos() {
        // ❌ Asume que test2_GuardarDatos() ya se ejecutó
        String dato = bd.consultar(0);
        assertEquals("dato", dato);
    }
}
```

**Problemas:**
- Si test2 se ejecuta antes que test1, falla con NullPointerException
- No podés ejecutar test3 aisladamente
- Ejecución paralela causaría fallos aleatorios

#### Solución correcta ✅

```java
// ✅ CORRECTO: Cada test es autosuficiente
public class BaseDeDatosTest {
    private BaseDeDatos bd;
    
    @BeforeEach
    void setUp() {
        // Cada test recibe una BD nueva y conectada
        bd = new BaseDeDatos();
        bd.conectar();
    }
    
    @AfterEach
    void tearDown() {
        // Limpieza después de cada test
        if (bd != null && bd.estaConectada()) {
            bd.desconectar();
        }
    }
    
    @Test
    void testGuardar_ConDatoValido_GuardaEnBaseDeDatos() {
        // ✅ Independiente - prepara todo lo que necesita
        bd.guardar("dato");
        assertEquals(1, bd.contarRegistros());
    }
    
    @Test
    void testConsultar_ConDatoGuardado_RetornaDato() {
        // ✅ Independiente - prepara su propio contexto
        bd.guardar("dato_especifico");
        
        String resultado = bd.consultar(0);
        assertEquals("dato_especifico", resultado);
    }
    
    @Test
    void testConectar_ConCredencialesValidas_EstableceConexion() {
        // ✅ Independiente - verifica conexión solamente
        assertTrue(bd.estaConectada());
    }
}
```

### Anti-patrón: Efectos colaterales en recursos compartidos ❌

#### Problema: Modificar archivos o bases de datos compartidas

```java
// ❌ INCORRECTO: Efectos colaterales en archivo compartido
public class GestorArchivosTest {
    private static final String ARCHIVO = "datos.txt";
    
    @Test
    void testGuardar_ConDato_EscribeEnArchivo() {
        GestorArchivos gestor = new GestorArchivos();
        gestor.guardar(ARCHIVO, "dato1");  // ❌ Modifica archivo compartido
        
        String contenido = gestor.leer(ARCHIVO);
        assertEquals("dato1", contenido);
    }
    
    @Test
    void testAgregar_ConDato_AnadeAlFinal() {
        GestorArchivos gestor = new GestorArchivos();
        // ❌ Asume que el archivo ya existe con "dato1"
        gestor.agregar(ARCHIVO, "dato2");
        
        String contenido = gestor.leer(ARCHIVO);
        assertTrue(contenido.contains("dato2"));
    }
}
```

#### Solución correcta: Archivos temporales únicos ✅

```java
// ✅ CORRECTO: Cada test usa su propio archivo temporal
public class GestorArchivosTest {
    private GestorArchivos gestor;
    private String archivoTemporal;
    
    @BeforeEach
    void setUp() throws IOException {
        gestor = new GestorArchivos();
        // Crear archivo temporal único para este test
        File temp = File.createTempFile("test_", ".txt");
        archivoTemporal = temp.getAbsolutePath();
        temp.deleteOnExit();
    }
    
    @Test
    void testGuardar_ConDato_EscribeEnArchivo() throws IOException {
        // ✅ Usa archivo propio
        gestor.guardar(archivoTemporal, "dato1");
        
        String contenido = gestor.leer(archivoTemporal);
        assertEquals("dato1", contenido);
    }
    
    @Test
    void testAgregar_EnArchivoNuevo_CreaDato() throws IOException {
        // ✅ Independiente - prepara su archivo
        gestor.guardar(archivoTemporal, "dato_inicial");
        gestor.agregar(archivoTemporal, "dato2");
        
        String contenido = gestor.leer(archivoTemporal);
        assertTrue(contenido.contains("dato2"));
    }
}
```

:::{important}
JUnit 5 ofrece `@TempDir` que proporciona directorios temporales únicos automáticamente:

```java
@Test
void testGuardar(@TempDir Path directorioTemporal) {
    Path archivo = directorioTemporal.resolve("test.txt");
    // Cada test recibe su propio directorio temporal
}
```
:::

### Verificar independencia

#### Test de orden aleatorio

JUnit permite ejecutar tests en orden aleatorio:

```java
@TestMethodOrder(MethodOrderer.Random.class)
public class MiTest {
    // Tests se ejecutarán en orden aleatorio
}
```

#### Ejecución paralela

```java
// En junit-platform.properties
junit.jupiter.execution.parallel.enabled = true
junit.jupiter.execution.parallel.mode.default = concurrent
```

:::{note}
Si tus tests fallan al ejecutarse en orden aleatorio o en paralelo, significa que tienen dependencias ocultas que deben eliminarse.
:::

### Casos especiales permitidos

#### Estado compartido inmutable está bien

```java
public class MatemáticasTest {
    // ✅ Constante compartida es aceptable (inmutable)
    private static final double DELTA = 0.0001;
    
    @Test
    void testCalcularCircunferencia_ConRadio1_RetornaDosPi() {
        double resultado = Matematicas.calcularCircunferencia(1.0);
        assertEquals(2 * Math.PI, resultado, DELTA);
    }
    
    @Test
    void testCalcularArea_ConRadio1_RetornaPi() {
        double resultado = Matematicas.calcularArea(1.0);
        assertEquals(Math.PI, resultado, DELTA);
    }
}
```

#### Setup pesado con @BeforeAll

Para recursos muy costosos de crear, `@BeforeAll` es aceptable si el recurso es **de solo lectura**:

```java
public class AnalizadorTextoTest {
    private static BaseDeDatosDiccionario diccionario;
    
    @BeforeAll
    static void setupClass() {
        // ✅ Setup una sola vez (pero solo si es read-only)
        diccionario = new BaseDeDatosDiccionario();
        diccionario.cargarDesdeArchivo("diccionario.dat");
    }
    
    @Test
    void testBuscarPalabra_ConPalabraExistente_RetornaDefinicion() {
        // ✅ Solo lee del diccionario, no lo modifica
        String definicion = diccionario.buscar("computadora");
        assertNotNull(definicion);
    }
    
    @Test
    void testBuscarPalabra_ConPalabraInexistente_RetornaNull() {
        // ✅ Solo lee del diccionario, no lo modifica
        String definicion = diccionario.buscar("xyzabc123");
        assertNull(definicion);
    }
}
```

:::{warning}
`@BeforeAll` solo es seguro si el objeto compartido es **inmutable** o solo se usa para **lectura**. Cualquier modificación rompe la independencia.
:::

### Comparación: Test dependiente vs independiente

#### Tests dependientes ❌

```java
// ❌ INCORRECTO: Tests con dependencias
public class PilaTest {
    private static Pila<String> pila = new Pila<>();  // ❌ Estado compartido
    
    @Test
    void test1_Apilar() {
        pila.apilar("elemento1");
        assertEquals(1, pila.tamanio());
    }
    
    @Test
    void test2_ApilarDosVeces() {
        // ❌ Asume que test1 ya ejecutó
        pila.apilar("elemento2");
        assertEquals(2, pila.tamanio());  // Falla si test1 no se ejecutó
    }
    
    @Test
    void test3_Desapilar() {
        // ❌ Asume que test1 y test2 ya ejecutaron
        String elemento = pila.desapilar();
        assertEquals("elemento2", elemento);  // Depende de orden
    }
}
```

#### Tests independientes ✅

```java
// ✅ CORRECTO: Tests independientes
public class PilaTest {
    private Pila<String> pila;
    
    @BeforeEach
    void setUp() {
        // Cada test recibe una pila nueva
        pila = new Pila<>();
    }
    
    @Test
    void testApilar_EnPilaVacia_IncrementaTamanioAUno() {
        pila.apilar("elemento1");
        assertEquals(1, pila.tamanio());
    }
    
    @Test
    void testApilar_DosElementos_IncrementaTamanioADos() {
        // ✅ Prepara su propio contexto
        pila.apilar("elemento1");
        pila.apilar("elemento2");
        assertEquals(2, pila.tamanio());
    }
    
    @Test
    void testDesapilar_DePilaConDosElementos_RetornaUltimoApilado() {
        // ✅ Totalmente independiente
        pila.apilar("primero");
        pila.apilar("segundo");
        
        String elemento = pila.desapilar();
        assertEquals("segundo", elemento);
    }
}
```

### Recursos externos y bases de datos

#### Problema: Base de datos compartida

```java
// ❌ INCORRECTO: Tests modifican BD compartida
public class UsuarioRepositorioTest {
    private static UsuarioRepositorio repo = new UsuarioRepositorio();
    
    @Test
    void testGuardar() {
        Usuario usuario = new Usuario("juan@ejemplo.com");
        repo.guardar(usuario);  // ❌ Modifica BD compartida
        assertEquals(1, repo.contar());
    }
    
    @Test
    void testBuscar() {
        // ❌ Asume que testGuardar() ya insertó datos
        Usuario encontrado = repo.buscarPorEmail("juan@ejemplo.com");
        assertNotNull(encontrado);
    }
}
```

#### Solución: Base de datos en memoria o transacciones

```java
// ✅ CORRECTO: Cada test con BD limpia
public class UsuarioRepositorioTest {
    private UsuarioRepositorio repo;
    private BaseDeDatos bd;
    
    @BeforeEach
    void setUp() {
        // Crear BD en memoria única para cada test
        bd = new BaseDeDatos("jdbc:h2:mem:test_" + UUID.randomUUID());
        bd.inicializarEsquema();
        repo = new UsuarioRepositorio(bd);
    }
    
    @AfterEach
    void tearDown() {
        // Limpiar después de cada test
        bd.cerrar();
    }
    
    @Test
    void testGuardar_ConUsuarioNuevo_LoGuardaEnBD() {
        Usuario usuario = new Usuario("juan@ejemplo.com");
        repo.guardar(usuario);
        assertEquals(1, repo.contar());
    }
    
    @Test
    void testBuscar_ConEmailExistente_RetornaUsuario() {
        // ✅ Prepara sus propios datos
        Usuario usuario = new Usuario("maria@ejemplo.com");
        repo.guardar(usuario);
        
        Usuario encontrado = repo.buscarPorEmail("maria@ejemplo.com");
        assertNotNull(encontrado);
        assertEquals("maria@ejemplo.com", encontrado.getEmail());
    }
}
```

### Archivos temporales

#### Con JUnit 5 @TempDir

```java
public class ProcesadorArchivosTest {
    @Test
    void testProcesar_ConArchivoValido_GeneraSalida(@TempDir Path dirTemp) {
        // ✅ Cada test recibe su propio directorio temporal
        Path archivoEntrada = dirTemp.resolve("entrada.txt");
        Path archivoSalida = dirTemp.resolve("salida.txt");
        
        Files.writeString(archivoEntrada, "contenido");
        
        ProcesadorArchivos proc = new ProcesadorArchivos();
        proc.procesar(archivoEntrada, archivoSalida);
        
        assertTrue(Files.exists(archivoSalida));
    }
    
    @Test
    void testProcesar_ConArchivoVacio_GeneraArchivoVacio(@TempDir Path dirTemp) {
        // ✅ Otro test con su propio directorio temporal
        Path archivoEntrada = dirTemp.resolve("vacio.txt");
        Path archivoSalida = dirTemp.resolve("salida.txt");
        
        Files.writeString(archivoEntrada, "");
        
        ProcesadorArchivos proc = new ProcesadorArchivos();
        proc.procesar(archivoEntrada, archivoSalida);
        
        assertEquals(0, Files.size(archivoSalida));
    }
}
```

### Verificar independencia en la práctica

#### Ejecutar tests en orden inverso

```bash
# Si tus tests son independientes, esto debería funcionar
mvn test -Djunit.jupiter.execution.parallel.enabled=true
```

#### Ejecutar un solo test

```bash
# Cada test debería poder ejecutarse solo
mvn test -Dtest=CalculadoraTest#testSumar_ConDosNumeros_RetornaSuma
```

#### Ejecutar subset de tests

```bash
# Cualquier combinación debería funcionar
mvn test -Dtest=CalculadoraTest#test*Sumar*
```

:::{tip}
Durante el desarrollo, ejecutá tests individualmente y en orden aleatorio para detectar dependencias ocultas tempranamente.
:::

### Patrón: Builders para setup complejo

Cuando el setup es complejo, usar el patrón Builder mantiene la independencia:

```java
public class PedidoTest {
    @Test
    void testProcesar_ConPedidoCompleto_CalculaTotalCorrectamente() {
        // ✅ Builder crea objeto independiente
        Pedido pedido = new PedidoBuilder()
            .conCliente("María")
            .conProducto("Laptop", 1000.0)
            .conProducto("Mouse", 25.0)
            .conDescuento(0.10)
            .construir();
        
        double total = pedido.calcularTotal();
        
        assertEquals(922.5, total, 0.01);
    }
    
    @Test
    void testProcesar_ConPedidoSinDescuento_CalculaSumaSimple() {
        // ✅ Otro pedido independiente
        Pedido pedido = new PedidoBuilder()
            .conCliente("Juan")
            .conProducto("Teclado", 50.0)
            .construir();
        
        double total = pedido.calcularTotal();
        
        assertEquals(50.0, total, 0.01);
    }
}
```

### Resumen de principios

```java
// ❌ Tests dependientes - frágiles y no paralelizables
@Test void test1() { x = 1; }
@Test void test2() { x++; assertEquals(2, x); }  // Depende de test1

// ✅ Tests independientes - robustos y paralelizables
@Test void test1() { int x = 1; assertEquals(1, x); }
@Test void test2() { int x = 1; x++; assertEquals(2, x); }
```

:::{important}
**Regla de oro**: Cada test debe poder ejecutarse exitosamente si fuera el único test del proyecto.
:::
