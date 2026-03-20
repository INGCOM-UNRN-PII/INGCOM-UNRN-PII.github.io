---
title: Testing con JUnit
description: Guía completa sobre testing unitario con JUnit 5, incluyendo conceptos fundamentales, sintaxis, y buenas prácticas.
---

# Testing con JUnit

## Introducción al Testing

El **testing** (pruebas de software) es una disciplina fundamental en el desarrollo de software moderno. Consiste en verificar que el código se comporta según lo esperado, detectar errores tempranamente y documentar el comportamiento del sistema.

### ¿Por qué testear?

El testing aporta múltiples beneficios al proceso de desarrollo:

1. **Detección temprana de errores**: Los bugs encontrados en etapas tempranas son más baratos de corregir
2. **Documentación viva**: Los tests documentan cómo se espera que funcione el código
3. **Refactoring seguro**: Permite modificar el código con confianza
4. **Diseño mejorado**: Escribir tests primero mejora el diseño del código
5. **Regresiones prevenidas**: Los tests evitan que errores corregidos vuelvan a aparecer

:::{important}
En esta cátedra, el testing no es opcional: es una parte integral del desarrollo. Todo código de producción debe estar acompañado de sus tests correspondientes.
:::

### Tipos de testing

Existen varios niveles de testing en el desarrollo de software:

```{figure} 2/piramide_testing.svg
:label: fig-piramide-testing
:align: center
:width: 80%

Pirámide de testing. La base ancha representa tests unitarios (rápidos y numerosos), el medio tests de integración, y la cima tests end-to-end (lentos y escasos).
```

#### Tests Unitarios

Los **tests unitarios** verifican el comportamiento de unidades individuales de código (generalmente métodos o clases) de forma aislada. Son:

- **Rápidos**: Se ejecutan en milisegundos
- **Aislados**: No dependen de bases de datos, archivos o red
- **Determinísticos**: Siempre producen el mismo resultado
- **Abundantes**: Son la mayoría de los tests en un proyecto

#### Tests de Integración

Los **tests de integración** verifican que múltiples componentes funcionen correctamente juntos. Pueden involucrar bases de datos, servicios externos o el sistema de archivos.

#### Tests End-to-End (E2E)

Los **tests E2E** verifican el sistema completo desde la perspectiva del usuario final. Son los más lentos y complejos.

:::{note}
En este apunte nos concentraremos exclusivamente en **tests unitarios** usando JUnit 5.
:::

## JUnit: Framework de Testing para Java

**JUnit** es el framework de testing más popular para Java. La versión actual es JUnit 5 (también llamada JUnit Jupiter), que representa una reescritura completa con arquitectura modular.

### Historia breve

- **JUnit 3** (2000): Primera versión ampliamente adoptada
- **JUnit 4** (2006): Introdujo anotaciones (`@Test`, `@Before`, etc.)
- **JUnit 5** (2017): Arquitectura modular, nuevas features, Java 8+

:::{tip}
Utilizaremos **JUnit 5** en toda la cátedra. Si encontrás ejemplos de JUnit 4 en internet, tené en cuenta que la sintaxis puede diferir.
:::

### Configuración del proyecto

Para usar JUnit 5 en un proyecto Gradle, agregá la siguiente dependencia al `build.gradle` (esto ya está listo en la configuración de las practicas.)

```gradle
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
}

test {
    useJUnitPlatform()
}
```

La configuración `testImplementation` indica que esta dependencia solo se usa para testing, no en producción. El bloque `test { useJUnitPlatform() }` es necesario para que Gradle ejecute tests de JUnit 5.

### Estructura de un proyecto con tests

Gradle utiliza la estructura estándar de Maven para separar código de producción y tests:

```
mi-proyecto/
├── build.gradle
├── settings.gradle
└── src/
    ├── main/
    │   └── java/
    │       └── ar/
    │           └── unrn/
    │               └── poo/
    │                   └── Calculadora.java
    └── test/
        └── java/
            └── ar/
                └── unrn/
                    └── poo/
                        └── CalculadoraTest.java
```

:::{important}
El código de test debe estar en `src/test/java` con la **misma estructura de paquetes** que el código de producción. Esto permite que los tests accedan a miembros con visibilidad de paquete.
:::

## Primer Test con JUnit

Veamos un ejemplo simple para entender la estructura básica de un test con JUnit.

### Código de producción

Supongamos que tenemos una clase `Calculadora` con métodos estáticos:

```java
package ar.unrn.poo;

/**
 * Calculadora simple con operaciones básicas.
 */
public class Calculadora {

    /**
     * Suma dos números enteros.
     *
     * @param a primer sumando
     * @param b segundo sumando
     * @return la suma de a y b
     */
    public static int sumar(int a, int b) {
        return a + b;
    }

    /**
     * Divide dos números.
     *
     * @param dividendo el número a dividir
     * @param divisor el número por el cual dividir
     * @return el cociente de la división
     * @throws ArithmeticException si el divisor es cero
     */
    public static double dividir(double dividendo, double divisor) {
        if (divisor == 0) {
            throw new ArithmeticException("No se puede dividir por cero");
        }
        return dividendo / divisor;
    }
}
```

### Clase de test

Ahora creamos la clase de test correspondiente. Aquí aplica la {ref}`regla-0x4000`: el test debe tener el mismo nombre que la clase con `Test` al final.

```java
package ar.unrn.poo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase Calculadora.
 */
public class CalculadoraTest {

    @Test
    void testSumar_ConDosNumerosPositivos_RetornaSuma() {
        // Arrange: Preparar los datos de entrada
        int operando1 = 5;
        int operando2 = 3;

        // Act: Ejecutar el método estático bajo prueba
        int resultado = Calculadora.sumar(operando1, operando2);

        // Assert: Verificar el resultado
        assertEquals(8, resultado, "5 + 3 debe ser 8");
    }
}
```

### Anatomía de un test

Analicemos los componentes del test anterior:

#### 1. Anotación `@Test`

```java
@Test
void testSumar_ConDosNumerosPositivos_RetornaSuma() {
```

La anotación `@Test` marca un método como método de test. JUnit lo ejecutará automáticamente.

**Características de los métodos de test:**

- Deben ser `void` (no retornan valor)
- Pueden tener cualquier nivel de visibilidad (recomendado: package-private o `public`)
- El nombre debe ser descriptivo (ver {ref}`regla-0x4003`)

#### 2. Estructura AAA (Arrange-Act-Assert)

Aquí aplica la {ref}`regla-0x4001`: todo test debe seguir la estructura AAA.

```java
// Arrange: Preparar
int operando1 = 5;
int operando2 = 3;

// Act: Ejecutar
int resultado = Calculadora.sumar(operando1, operando2);

// Assert: Verificar
assertEquals(8, resultado, "5 + 3 debe ser 8");
```

**Arrange (Preparar):**

- Preparar datos de entrada
- Configurar valores necesarios para la prueba

**Act (Ejecutar):**

- Invocar el método bajo prueba
- Aquí aplica la {ref}`regla-0x4002`: una sola llamada por test

**Assert (Verificar):**

- Comprobar que el resultado es el esperado
- Usar métodos de `Assertions`

#### 3. Assertions (Verificaciones)

Los **assertions** son métodos que verifican condiciones. Si la condición es falsa, el test falla.

```java
assertEquals(8, resultado, "5 + 3 debe ser 8");
//           ^esperado  ^actual  ^mensaje opcional
```

JUnit provee múltiples métodos de assertion que veremos en detalle más adelante.

### Ejecutar los tests

#### Desde el IDE

La mayoría de los IDEs modernos (IntelliJ IDEA, Eclipse, VS Code) permiten ejecutar tests con un clic:

- **Ejecutar todos los tests de una clase**: Click derecho → "Run Tests"
- **Ejecutar un test individual**: Click en el ícono verde junto al método

#### Desde Gradle

```bash
# Ejecutar todos los tests del proyecto
gradle test
# o usando el wrapper (recomendado)
./gradlew test

# Ejecutar tests de una clase específica
gradle test --tests CalculadoraTest
./gradlew test --tests CalculadoraTest

# Ejecutar un método de test específico
gradle test --tests CalculadoraTest.testSumar_ConDosNumerosPositivos_RetornaSuma
./gradlew test --tests CalculadoraTest.testSumar_ConDosNumerosPositivos_RetornaSuma
```

:::{tip}
El Gradle Wrapper (`./gradlew`) es la forma recomendada de ejecutar tareas. Garantiza que todos usen la misma versión de Gradle sin necesidad de instalarlo globalmente.
:::

### Interpretación de resultados

Cuando ejecutás los tests, JUnit muestra un reporte:

```
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

- **Tests run**: Total de tests ejecutados
- **Failures**: Tests que fallaron (assertion falló)
- **Errors**: Tests que lanzaron excepciones inesperadas
- **Skipped**: Tests marcados como ignorados

:::{tip}
Un test **pasa** (✅) si todas las assertions son verdaderas y no se lanzan excepciones inesperadas. Un test **falla** (❌) si alguna assertion es falsa o se lanza una excepción inesperada.
:::

## Assertions: Verificaciones en JUnit

JUnit 5 provee una rica colección de métodos de assertion en la clase `org.junit.jupiter.api.Assertions`. Veamos los más importantes.

### Assertions básicos

#### `assertEquals` y `assertNotEquals`

Verifican igualdad de valores:

```java
@Test
void testEquals() {
    // Act
    int resultado = Calculadora.sumar(2, 3);

    // Assert
    assertEquals(5, resultado);                    // Pasa si resultado == 5
    assertNotEquals(6, resultado);                 // Pasa si resultado != 6
}
```

Para números de punto flotante, especificá un delta de tolerancia:

```java
@Test
void testDividir_ConDosNumeros_RetornaCociente() {
    // Act
    double resultado = Calculadora.dividir(10.0, 3.0);

    // Assert
    assertEquals(3.333, resultado, 0.001);  // Tolerancia de ±0.001
}
```

:::{important}
Siempre usá un delta al comparar `double` o `float`, debido a imprecisiones de punto flotante:

```java
// ❌ Incorrecto - puede fallar por imprecisión
assertEquals(0.3, 0.1 + 0.2);

// ✅ Correcto - usa delta
assertEquals(0.3, 0.1 + 0.2, 0.000001);
```

:::

#### `assertTrue` y `assertFalse`

Verifican condiciones booleanas:

```java
@Test
void testValidador() {
    // Act & Assert
    assertTrue(ValidadorEmail.esValido("usuario@ejemplo.com"));
    assertFalse(ValidadorEmail.esValido("sin-arroba"));
}
```

#### `assertNull` y `assertNotNull`

Verifican si un valor es `null`:

```java
@Test
void testBuscar_ConIdInexistente_RetornaNull() {
    // Act
    String resultado = Buscador.buscarPorClave("inexistente");

    // Assert
    assertNull(resultado);
}

@Test
void testBuscar_ConClaveExistente_RetornaValor() {
    // Act
    String resultado = Buscador.buscarPorClave("existente");

    // Assert
    assertNotNull(resultado);
}
```

### Assertions para colecciones y arrays

#### `assertArrayEquals`

Verifica que dos arrays tienen los mismos elementos en el mismo orden:

```java
@Test
void testOrdenar_ConArregloDesordenado_RetornaArregloOrdenado() {
    // Arrange
    int[] entrada = {3, 1, 4, 1, 5};
    int[] esperado = {1, 1, 3, 4, 5};

    // Act
    int[] resultado = Ordenador.ordenar(entrada);

    // Assert
    assertArrayEquals(esperado, resultado);
}
```

#### Assertions de colecciones (con métodos helper)

JUnit 5 no tiene assertions específicas para colecciones en `Assertions`, pero podés combinar assertions:

```java
@Test
void testFiltrar_ConListaDeNumeros_RetornaListaFiltrada() {
    // Arrange
    int[] numeros = {1, 2, 3, 4, 5, 6};

    // Act
    int[] pares = Filtrador.filtrarPares(numeros);

    // Assert
    assertNotNull(pares);
    assertEquals(3, pares.length);
    assertEquals(2, pares[0]);
}
```

### Verificación de excepciones

Una funcionalidad crucial es verificar que el código lanza excepciones en situaciones incorrectas.

#### `assertThrows`

Verifica que un bloque de código lanza una excepción específica:

```java
@Test
void testDividir_ConDivisorCero_LanzaArithmeticException() {
    // Act & Assert
    try {
        Calculadora.dividir(10, 0);
        fail("Se esperaba ArithmeticException");
    } catch (ArithmeticException e) {
        // Test pasa - se lanzó la excepción esperada
    }
}
```

Podés capturar la excepción para verificar su mensaje:

```java
@Test
void testDividir_ConDivisorCero_LanzaExcepcionConMensajeCorrecto() {
    // Act & Assert
    try {
        Calculadora.dividir(10, 0);
        fail("Se esperaba ArithmeticException");
    } catch (ArithmeticException excepcion) {
        assertEquals("No se puede dividir por cero", excepcion.getMessage());
    }
}
```

:::{important}
Verificar excepciones es tan importante como verificar resultados correctos. Las excepciones son parte del contrato de un método.
:::

#### Verificar que NO se lanza excepción

Cuando querés verificar que un método NO lanza excepciones, simplemente llamalo:

```java
@Test
void testDividir_ConDivisorNoNulo_NoLanzaExcepcion() {
    // Act
    double resultado = Calculadora.dividir(10, 2);

    // Assert
    assertEquals(5.0, resultado, 0.01);
}
```

### Assertions compuestos

#### Múltiples verificaciones

Cuando necesitás verificar múltiples aspectos del resultado, podés hacer múltiples assertions:

```java
@Test
void testParsearFecha_ConFormatoCorrecto_RetornaComponentes() {
    // Arrange
    String fecha = "2024-03-15";

    // Act
    int[] componentes = Parser.parsearFecha(fecha);

    // Assert: Verificar múltiples valores del resultado
    assertNotNull(componentes, "Resultado no debe ser null");
    assertEquals(3, componentes.length, "Debe tener 3 componentes");
    assertEquals(2024, componentes[0], "Año debe ser 2024");
    assertEquals(3, componentes[1], "Mes debe ser 3");
    assertEquals(15, componentes[2], "Día debe ser 15");
}
```

:::{note}
Si una assertion falla, las siguientes no se ejecutan. Por esto es importante que cada test verifique un concepto específico según la {ref}`regla-0x4002`.
:::

### Mensajes de falla descriptivos

Todos los assertions aceptan un mensaje opcional que se muestra cuando el test falla:

```java
@Test
void testCalcularDescuento_ConClientePremium_Aplica20Porciento() {
    // Arrange
    double precioOriginal = 1000.0;
    double descuentoPremium = 0.20;

    // Act
    double total = Calculadora.aplicarDescuento(precioOriginal, descuentoPremium);

    // Assert
    assertEquals(800.0, total, 0.01,
        "Cliente premium debe recibir 20% de descuento: 1000 * 0.8 = 800");
}
```

Cuando el test falla, JUnit muestra:

```
org.opentest4j.AssertionFailedError:
Cliente premium debe recibir 20% de descuento: 1000 * 0.8 = 800 ==>
expected: <800.0> but was: <1000.0>
```

:::{tip}
Escribí mensajes de falla que expliquen **por qué** el test espera ese valor, no solo **qué** valor espera. Esto ayuda enormemente cuando un test falla meses después.
:::

## Ciclo de Vida de los Tests

JUnit ofrece anotaciones para ejecutar código en diferentes momentos del ciclo de vida de los tests. Aunque al testear métodos estáticos no es estrictamente necesario preparar estado, estas anotaciones son útiles para preparar datos de entrada comunes o manejar recursos como archivos temporales.

### Anotaciones de ciclo de vida

#### `@BeforeEach`

El método anotado se ejecuta **antes de cada test**. Es útil para preparar datos de entrada comunes:

```java
public class CalculadoraEstadisticasTest {
    private int[] datos;

    @BeforeEach
    void setUp() {
        // Preparar datos de entrada comunes para varios tests
        datos = new int[]{10, 20, 30, 40, 50};
        System.out.println("Datos preparados para test");
    }

    @Test
    void testCalcularPromedio_ConDatosValidos_RetornaPromedio() {
        double resultado = Estadisticas.calcularPromedio(datos);
        assertEquals(30.0, resultado, 0.01);
    }

    @Test
    void testCalcularSuma_ConDatosValidos_RetornaSuma() {
        int resultado = Estadisticas.calcularSuma(datos);
        assertEquals(150, resultado);
    }
}
```

**Orden de ejecución:**

```
setUp() → testCalcularPromedio... → (fin test)
setUp() → testCalcularSuma... → (fin test)
```

Cada test recibe los datos frescos, garantizando la {ref}`regla-0x4005` (tests independientes).

#### `@AfterEach`

El método anotado se ejecuta **después de cada test**:

```java
public class ArchivoTest {
    private File archivoTemporal;

    @BeforeEach
    void setUp() throws IOException {
        archivoTemporal = File.createTempFile("test_", ".txt");
    }

    @AfterEach
    void tearDown() {
        // Limpieza después de cada test
        if (archivoTemporal != null && archivoTemporal.exists()) {
            archivoTemporal.delete();
        }
        System.out.println("Recursos liberados");
    }

    @Test
    void testGuardar_ConDatos_CreaArchivo() throws IOException {
        GestorArchivos.guardar(archivoTemporal.getPath(), "contenido");
        assertTrue(archivoTemporal.exists());
    }
}
```

**Uso típico de `@AfterEach`:**

- Eliminar archivos temporales
- Restaurar configuraciones globales
- Liberar recursos del sistema

#### `@BeforeAll`

El método anotado se ejecuta **una vez antes de todos los tests** de la clase:

```java
public class ProcesadorDatosTest {
    private static String[] datosGrandes;

    @BeforeAll
    static void setupClass() {
        // Cargar datos costosos UNA VEZ
        datosGrandes = cargarDatosDeArchivo("datos_prueba.csv");
        System.out.println("Datos cargados");
    }

    @Test
    void testProcesar_PrimerElemento_RetornaResultado() {
        String resultado = Procesador.procesar(datosGrandes[0]);
        assertNotNull(resultado);
    }

    @Test
    void testProcesar_UltimoElemento_RetornaResultado() {
        String resultado = Procesador.procesar(datosGrandes[datosGrandes.length - 1]);
        assertNotNull(resultado);
    }
    
    private static String[] cargarDatosDeArchivo(String ruta) {
        // Simulación de carga de datos
        return new String[]{"dato1", "dato2", "dato3"};
    }
}
```

**Importante:**

- El método debe ser `static`
- Útil para setup costoso que no cambia entre tests
- Solo usar con datos de **solo lectura** para mantener independencia de tests

:::{warning}
Tené cuidado con `@BeforeAll`: si los datos compartidos son modificables, podés violar la {ref}`regla-0x4005` (independencia de tests). Usalo solo para datos inmutables o de solo lectura.
:::

#### `@AfterAll`

El método anotado se ejecuta **una vez después de todos los tests**:

```java
public class ProcesadorDatosTest {
    private static File archivoLog;

    @BeforeAll
    static void setupClass() throws IOException {
        archivoLog = File.createTempFile("log_test_", ".txt");
    }

    @AfterAll
    static void tearDownClass() {
        // Limpieza final después de todos los tests
        if (archivoLog != null && archivoLog.exists()) {
            archivoLog.delete();
        }
        System.out.println("Recursos globales liberados");
    }

    // ... tests ...
}
```

### Orden completo de ejecución

Para una clase con múltiples tests, el orden de ejecución es:

```
@BeforeAll (una vez)
    @BeforeEach
        @Test (test 1)
    @AfterEach
    @BeforeEach
        @Test (test 2)
    @AfterEach
    @BeforeEach
        @Test (test 3)
    @AfterEach
@AfterAll (una vez)
```

### Ejemplo completo del ciclo de vida

```java
public class EjemploOrdenTest {
    private static int contador = 0;
    private int testId;

    @BeforeAll
    static void setupClass() {
        System.out.println("@BeforeAll: Ejecutado UNA vez");
        contador = 0;
    }

    @BeforeEach
    void setUp() {
        testId = ++contador;
        System.out.println("@BeforeEach: Preparando test #" + testId);
    }

    @Test
    void test1() {
        System.out.println("  @Test: Ejecutando test #" + testId);
        assertTrue(true);
    }

    @Test
    void test2() {
        System.out.println("  @Test: Ejecutando test #" + testId);
        assertTrue(true);
    }

    @AfterEach
    void tearDown() {
        System.out.println("@AfterEach: Limpiando test #" + testId);
    }

    @AfterAll
    static void tearDownClass() {
        System.out.println("@AfterAll: Ejecutado UNA vez");
    }
}
```

**Salida:**

```
@BeforeAll: Ejecutado UNA vez
@BeforeEach: Preparando test #1
  @Test: Ejecutando test #1
@AfterEach: Limpiando test #1
@BeforeEach: Preparando test #2
  @Test: Ejecutando test #2
@AfterEach: Limpiando test #2
@AfterAll: Ejecutado UNA vez
```

## Convenciones de Nombrado

El nombrado correcto de tests es fundamental para la mantenibilidad. Aquí aplica la {ref}`regla-0x4003`.

### Estructura del nombre

```
test<MetodoAProbar>_<CondicionOContexto>_<ResultadoEsperado>
```

**Componentes:**

1. **Prefijo `test`**: Identifica claramente que es un método de test
2. **Método a probar**: Nombre del método que se está testeando
3. **Condición**: Bajo qué circunstancias o con qué datos
4. **Resultado esperado**: Qué debe suceder

### Ejemplos prácticos

#### Tests de métodos que retornan valores

```java
public class BuscadorTest {
    @Test
    void testBuscar_EnArregloVacio_RetornaNull() {
        int[] arreglo = {};
        Integer resultado = Buscador.buscarMaximo(arreglo);
        assertNull(resultado);
    }

    @Test
    void testBuscar_ConElementoExistente_RetornaIndice() {
        int[] arreglo = {10, 20, 30, 40};
        int indice = Buscador.buscarIndice(arreglo, 30);
        assertEquals(2, indice);
    }

    @Test
    void testBuscar_ConElementoInexistente_RetornaMenosUno() {
        int[] arreglo = {10, 20, 30};
        int indice = Buscador.buscarIndice(arreglo, 99);
        assertEquals(-1, indice);
    }
}
```

#### Tests de métodos que modifican datos

```java
public class OrdenadorTest {
    @Test
    void testOrdenar_ConArregloDesordenado_OrdenaCorrectamente() {
        int[] entrada = {5, 2, 8, 1, 9};
        int[] esperado = {1, 2, 5, 8, 9};
        
        int[] resultado = Ordenador.ordenar(entrada);
        
        assertArrayEquals(esperado, resultado);
    }

    @Test
    void testOrdenar_ConArregloVacio_RetornaArregloVacio() {
        int[] entrada = {};
        
        int[] resultado = Ordenador.ordenar(entrada);
        
        assertEquals(0, resultado.length);
    }
}
```

#### Tests de validadores

```java
public class ValidadorEmailTest {
    @Test
    void testEsValido_ConEmailCorrecto_RetornaTrue() {
        assertTrue(ValidadorEmail.esValido("usuario@ejemplo.com"));
    }

    @Test
    void testEsValido_SinArroba_RetornaFalse() {
        assertFalse(ValidadorEmail.esValido("usuario.ejemplo.com"));
    }

    @Test
    void testEsValido_SinDominio_RetornaFalse() {
        assertFalse(ValidadorEmail.esValido("usuario@"));
    }

    @Test
    void testEsValido_ConEspacios_RetornaFalse() {
        assertFalse(ValidadorEmail.esValido("usuario @ejemplo.com"));
    }

    @Test
    void testEsValido_ConCaracteresEspeciales_RetornaFalse() {
        assertFalse(ValidadorEmail.esValido("usuario!#$@ejemplo.com"));
    }
}
```

### Patrones de nombres útiles

#### Para valores límite

```java
testCalcular_ConValorCero_...
testCalcular_ConValorMaximo_...
testCalcular_ConValorMinimo_...
testCalcular_ConValorNegativo_...
```

#### Para colecciones y arreglos

```java
testProcesar_ConArregloVacio_...
testProcesar_ConUnSoloElemento_...
testProcesar_ConMultiplesElementos_...
testProcesar_ConArregloNull_...
```

#### Para valores de entrada

```java
testOperacion_ConEntradaValida_...
testOperacion_ConEntradaNula_...
testOperacion_ConEntradaVacia_...
```

### Nombres descriptivos vs concisos

```java
// ❌ Demasiado genérico
@Test
void testSumar() { }

// ❌ Ambiguo
@Test
void testConCero() { }

// ✅ Claro y específico
@Test
void testSumar_ConUnOperandoCero_RetornaSuma() { }

// ✅ Describe condición y resultado
@Test
void testSumar_ConDosNumerosNegativos_RetornaSumaNegativa() { }
```

:::{important}
El nombre del test debe ser tan descriptivo que cuando falle, sepas exactamente qué funcionalidad está rota **sin leer el código del test**.
:::

## Reglas de Estilo para Testing

Las reglas de testing son fundamentales para mantener una suite de tests robusta y mantenible. Repasemos las reglas clave.

### Regla 0x4000: Nomenclatura de la clase de test

{ref}`regla-0x4000` establece que la clase de test debe nombrarse como la clase bajo prueba con el sufijo `Test`:

```java
// Clase de producción
public class Calculadora { }

// ✅ Correcto
public class CalculadoraTest { }

// ❌ Incorrecto
public class TestCalculadora { }       // Prefijo en lugar de sufijo
public class CalculadoraTests { }      // Plural
public class PruebasCalculadora { }   // Nombre diferente
```

**Ubicación:** La clase de test debe estar en el mismo paquete que la clase bajo prueba, pero en `src/test/java`:

```
src/main/java/ar/unrn/poo/Calculadora.java
src/test/java/ar/unrn/poo/CalculadoraTest.java
```

### Regla 0x4001: Estructura AAA

{ref}`regla-0x4001` requiere que cada test siga la estructura Arrange-Act-Assert:

```java
@Test
void testCalcularDescuento_ConPorcentajeValido_RetornaDescuento() {
    // Arrange: Preparar datos de entrada
    double precio = 1000.0;
    double porcentaje = 0.20;

    // Act: Ejecutar el método estático bajo prueba
    double resultado = Calculadora.aplicarDescuento(precio, porcentaje);

    // Assert: Verificar el resultado
    assertEquals(800.0, resultado, 0.01,
        "Precio 1000 - 20% descuento = 800");
}
```

**Beneficios de la estructura AAA:**

1. **Claridad**: El flujo lógico es obvio
2. **Mantenibilidad**: Fácil modificar cada fase
3. **Depuración**: Identificar dónde falla el test

:::{tip}
Usá líneas en blanco para separar visualmente las tres fases. Opcionalmente, agregá comentarios `// Arrange`, `// Act`, `// Assert`.
:::

### Regla 0x4002: Una llamada por test

{ref}`regla-0x4002` establece que cada test debe hacer una sola llamada al método bajo prueba:

```java
// ❌ Incorrecto: Múltiples llamadas
@Test
void testOperacionesMatematicas() {
    assertEquals(5, Calculadora.sumar(2, 3));
    assertEquals(2, Calculadora.restar(5, 3));
    assertEquals(12, Calculadora.multiplicar(3, 4));
}

// ✅ Correcto: Tests separados
@Test
void testSumar_ConDosNumeros_RetornaSuma() {
    assertEquals(5, Calculadora.sumar(2, 3));
}

@Test
void testRestar_ConDosNumeros_RetornaResta() {
    assertEquals(2, Calculadora.restar(5, 3));
}

@Test
void testMultiplicar_ConDosNumeros_RetornaProducto() {
    assertEquals(12, Calculadora.multiplicar(3, 4));
}
```

**Ventaja:** Cuando un test falla, sabés exactamente qué funcionalidad está rota.

**Excepción permitida:** Múltiples assertions sobre el mismo resultado están bien:

```java
@Test
void testParsearFecha_ConFormatoValido_RetornaComponentes() {
    // Act: UNA llamada
    int[] componentes = Parser.parsearFecha("2024-03-15");

    // Assert: Múltiples verificaciones sobre EL MISMO resultado
    assertNotNull(componentes);
    assertEquals(3, componentes.length);
    assertEquals(2024, componentes[0]);
    assertEquals(3, componentes[1]);
}
```

### Regla 0x4003: Nombres descriptivos

{ref}`regla-0x4003` ya la vimos en la sección de convenciones de nombrado. La convención es:

```
test<Accion>_<Condicion>_<ResultadoEsperado>
```

### Regla 0x4004: Sin lógica condicional

{ref}`regla-0x4004` prohíbe estructuras de control (`if`, `for`, `while`, etc.) en los tests:

```java
// ❌ Incorrecto: Condicional en test
@Test
void testCalcular() {
    int numero = 5;
    int resultado = Calculadora.calcular(numero);

    if (numero % 2 == 0) {
        assertEquals(numero * 2, resultado);
    } else {
        assertEquals(numero * 3, resultado);
    }
}

// ✅ Correcto: Tests separados
@Test
void testCalcular_ConNumeroPar_RetornaDoble() {
    assertEquals(8, Calculadora.calcular(4));
}

@Test
void testCalcular_ConNumeroImpar_RetornaTriple() {
    assertEquals(15, Calculadora.calcular(5));
}
```

**¿Por qué no lógica condicional?**

1. Los tests deben ser simples y directos
2. Lógica condicional añade complejidad
3. Si el test tiene bugs, ¿quién testea el test?

**Alternativa para datos múltiples:** Tests parametrizados (ver sección siguiente).

### Regla 0x4005: Tests independientes

{ref}`regla-0x4005` requiere que cada test sea completamente independiente:

```java
// ❌ Incorrecto: Estado compartido mutable
public class ContadorTest {
    private static int contador = 0;  // ❌ Compartido entre tests

    @Test
    void test1_Incrementar() {
        contador++;
        assertEquals(1, contador);  // Depende del orden
    }

    @Test
    void test2_IncrementarDosVeces() {
        contador += 2;
        assertEquals(3, contador);  // ❌ Asume que test1 ya ejecutó
    }
}

// ✅ Correcto: Cada test es independiente usando datos propios
public class CalculadoraTest {
    @Test
    void testSumar_ConDosPositivos_RetornaSuma() {
        int a = 5;  // ✅ Datos propios del test
        int b = 3;
        assertEquals(8, Calculadora.sumar(a, b));
    }

    @Test
    void testSumar_ConDosNegativos_RetornaSumaNegativa() {
        int a = -5;  // ✅ Datos propios del test
        int b = -3;
        assertEquals(-8, Calculadora.sumar(a, b));
    }
}
```

**Cómo lograr independencia:**

1. Usar `@BeforeEach` para preparar datos comunes
2. Evitar variables `static` mutables
3. Cada test prepara sus propios datos de entrada
4. Limpiar recursos en `@AfterEach`

**Verificación:** Tus tests deben pasar en cualquier orden. Podés verificarlo configurando JUnit para orden aleatorio:

```java
@TestMethodOrder(MethodOrderer.Random.class)
public class MiTest {
    // Tests se ejecutan en orden aleatorio
}
```

## Tests Parametrizados

A menudo queremos probar el mismo comportamiento con múltiples conjuntos de datos. Los **tests parametrizados** permiten esto sin violar la {ref}`regla-0x4004` (sin lógica condicional).

### Anotación `@ParameterizedTest`

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CalculadoraTest {
    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, 8, 10})
    void testEsPar_ConNumerosPares_RetornaTrue(int numero) {
        assertTrue(Calculadora.esPar(numero));
    }
}
```

Este test se ejecuta 5 veces, una por cada valor en `@ValueSource`.

### Fuentes de parámetros

#### `@ValueSource`

Para un solo parámetro de tipos primitivos o String:

```java
@ParameterizedTest
@ValueSource(strings = {"", "  ", "\t", "\n"})
void testEsBlanco_ConStringsBlancos_RetornaTrue(String input) {
    assertTrue(input.isBlank());
}

@ParameterizedTest
@ValueSource(ints = {1, 3, 5, 7, 9})
void testEsImpar_ConNumerosImpares_RetornaTrue(int numero) {
    assertTrue(Calculadora.esImpar(numero));
}

@ParameterizedTest
@ValueSource(doubles = {0.0, -0.0, Double.NaN, Double.POSITIVE_INFINITY})
void testEsEspecial_ConValoresEspeciales_RetornaTrue(double valor) {
    assertTrue(Analizador.esValorEspecial(valor));
}
```

#### `@CsvSource`

Para múltiples parámetros en formato CSV:

```java
@ParameterizedTest
@CsvSource({
    "2, 3, 5",
    "10, 20, 30",
    "-5, 5, 0",
    "0, 0, 0",
    "-10, -5, -15"
})
void testSumar_ConDiferentesValores_RetornaResultadoCorrecto(
        int a, int b, int esperado) {
    assertEquals(esperado, Calculadora.sumar(a, b));
}
```

Podés usar comillas para strings con espacios o comas:

```java
@ParameterizedTest
@CsvSource({
    "usuario@ejemplo.com, true",
    "'usuario sin arroba', false",
    "'', false",
    "usuario@, false"
})
void testValidarEmail_ConDiferentesFormatos_RetornaResultadoCorrecto(
        String email, boolean esperado) {
    assertEquals(esperado, ValidadorEmail.esValido(email));
}
```

#### Tests parametrizados con múltiples valores

Para casos con múltiples valores, podés usar `@CsvSource`:

```java
public class CalculadoraDescuentosTest {
    @ParameterizedTest
    @CsvSource({
        "100.0, 0.0, 100.0",   // Sin descuento
        "100.0, 0.1, 90.0",    // 10% descuento
        "100.0, 0.5, 50.0",    // 50% descuento
        "100.0, 1.0, 0.0"      // 100% descuento
    })
    void testAplicarDescuento_ConDiferentesPorcentajes_RetornaCorrectamente(
            double precio, double descuento, double esperado) {
        assertEquals(esperado, Calculadora.aplicarDescuento(precio, descuento), 0.01);
    }
}
```

#### `@CsvFileSource`

Para datasets grandes, cargá datos desde un archivo CSV:

```java
@ParameterizedTest
@CsvFileSource(resources = "/datos-prueba.csv", numLinesToSkip = 1)
void testProcesar_ConDatosDeArchivo_RetornaCorrectamente(
        String entrada, String esperado) {
    assertEquals(esperado, Procesador.procesar(entrada));
}
```

Archivo `src/test/resources/datos-prueba.csv`:

```csv
entrada,esperado
"abc","ABC"
"hello","HELLO"
"123","123"
```

### Nombres personalizados para tests parametrizados

Por defecto, JUnit genera nombres como `[1] 2, 3, 5`. Podés personalizarlos:

```java
@ParameterizedTest(name = "Sumar {0} + {1} debe dar {2}")
@CsvSource({
    "2, 3, 5",
    "10, 20, 30",
    "-5, 5, 0"
})
void testSumar(int a, int b, int esperado) {
    assertEquals(esperado, Calculadora.sumar(a, b));
}
```

Salida en el reporte:

```
✅ Sumar 2 + 3 debe dar 5
✅ Sumar 10 + 20 debe dar 30
✅ Sumar -5 + 5 debe dar 0
```

:::{tip}
Tests parametrizados son ideales para:

- Probar valores límite (boundary values)
- Verificar tablas de verdad
- Validar múltiples formatos de entrada
- Testing exhaustivo de funciones matemáticas
  :::

## Tests de Excepciones

Las excepciones son parte integral del contrato de un método. Debemos testearlas con el mismo rigor que los casos exitosos.

### Verificar que se lanza excepción

```java
@Test
void testDividir_ConDivisorCero_LanzaArithmeticException() {
    // Act & Assert
    try {
        Calculadora.dividir(10, 0);
        fail("Se esperaba ArithmeticException");
    } catch (ArithmeticException e) {
        // Test pasa - se lanzó la excepción esperada
    }
}
```

### Verificar mensaje de excepción

```java
@Test
void testValidarEdad_ConEdadNegativa_LanzaExcepcionConMensajeEspecifico() {
    // Act & Assert
    try {
        Validador.validarEdad(-5);
        fail("Se esperaba IllegalArgumentException");
    } catch (IllegalArgumentException excepcion) {
        assertEquals("La edad no puede ser negativa", excepcion.getMessage());
    }
}
```

### Verificar causa de excepción

En algunos casos, necesitás verificar que una excepción envuelve otra (la causa):

```java
@Test
void testParsearNumero_ConTextoInvalido_LanzaExcepcionConCausa() {
    // Act & Assert
    try {
        Parser.parsearEntero("no-es-numero");
        fail("Se esperaba ParseException");
    } catch (ParseException excepcion) {
        assertNotNull(excepcion.getCause());
        assertTrue(excepcion.getCause() instanceof NumberFormatException);
    }
}
```

### Múltiples verificaciones sobre excepción

```java
@Test
void testValidar_ConDatosInvalidos_LanzaExcepcionConDetalles() {
    // Arrange
    String[] datosInvalidos = {"", null, "  "};

    // Act & Assert
    try {
        Validador.validarTodos(datosInvalidos);
        fail("Se esperaba ValidacionException");
    } catch (ValidacionException excepcion) {
        assertNotNull(excepcion.getMessage());
        assertTrue(excepcion.getMessage().contains("inválido"));
        assertEquals(3, excepcion.getCantidadErrores());
    }
}
```

### Tests con try-catch

Para verificar excepciones, usá bloques try-catch:

```java
@Test
void testProcesar_ConDatoNull_LanzaExcepcion() {
    // Act & Assert
    try {
        Procesador.procesar(null);
        fail("Debería haber lanzado IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        assertTrue(e.getMessage().contains("no puede ser null"));
    }
}
```

## Casos de Prueba Completos

Veamos ejemplos completos que integran todos los conceptos vistos, centrados en métodos estáticos.

### Ejemplo 1: Test de funciones matemáticas

```java
package ar.unrn.poo.matematica;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase Matematica con métodos estáticos.
 */
public class MatematicaTest {

    @Test
    void testFactorial_ConCero_RetornaUno() {
        // Act
        long resultado = Matematica.factorial(0);

        // Assert
        assertEquals(1, resultado);
    }

    @Test
    void testFactorial_ConCinco_Retorna120() {
        // Act
        long resultado = Matematica.factorial(5);

        // Assert
        assertEquals(120, resultado);
    }

    @Test
    void testFactorial_ConNumeroNegativo_LanzaIllegalArgumentException() {
        // Act & Assert
        try {
            Matematica.factorial(-1);
            fail("Se esperaba IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("negativo"));
        }
    }

    @ParameterizedTest(name = "Fibonacci({0}) = {1}")
    @CsvSource({
        "0, 0",
        "1, 1",
        "2, 1",
        "5, 5",
        "10, 55"
    })
    void testFibonacci_ConDiferentesValores_RetornaSecuenciaCorrecta(
            int n, long esperado) {
        assertEquals(esperado, Matematica.fibonacci(n));
    }

    @Test
    void testEsPrimo_ConNumerosPrimos_RetornaTrue() {
        assertTrue(Matematica.esPrimo(2));
        assertTrue(Matematica.esPrimo(7));
        assertTrue(Matematica.esPrimo(13));
    }

    @Test
    void testEsPrimo_ConNumerosCompuestos_RetornaFalse() {
        assertFalse(Matematica.esPrimo(1));
        assertFalse(Matematica.esPrimo(4));
        assertFalse(Matematica.esPrimo(9));
    }

    @Test
    void testMcd_ConDosNumeros_RetornaMaximoComunDivisor() {
        assertEquals(6, Matematica.mcd(12, 18));
        assertEquals(1, Matematica.mcd(7, 11));
        assertEquals(5, Matematica.mcd(15, 25));
    }
}
```

### Ejemplo 2: Test de validador de emails

```java
package ar.unrn.poo.validacion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para ValidadorEmail con métodos estáticos.
 */
public class ValidadorEmailTest {
    @Test
    void testEsValido_ConEmailCorrecto_RetornaTrue() {
        // Arrange
        String emailValido = "usuario@ejemplo.com";

        // Act
        boolean resultado = ValidadorEmail.esValido(emailValido);

        // Assert
        assertTrue(resultado);
    }

    @ParameterizedTest(name = "Email inválido: ''{0}''")
    @ValueSource(strings = {
        "",                          // Vacío
        "   ",                       // Solo espacios
        "sin-arroba",                // Sin @
        "@ejemplo.com",              // Sin usuario
        "usuario@",                  // Sin dominio
        "usuario @ejemplo.com",      // Con espacio
        "usuario@@ejemplo.com",      // Doble @
        "usuario@ejemplo",           // Sin TLD
        "usuario.ejemplo.com",       // Punto en lugar de @
        "usuario#ejemplo@com"        // Carácter inválido
    })
    void testEsValido_ConEmailsInvalidos_RetornaFalse(String emailInvalido) {
        assertFalse(ValidadorEmail.esValido(emailInvalido),
            "El email '" + emailInvalido + "' debería ser inválido");
    }

    @ParameterizedTest(name = "Email válido: ''{0}''")
    @ValueSource(strings = {
        "simple@ejemplo.com",
        "nombre.apellido@ejemplo.com",
        "usuario+tag@ejemplo.com",
        "usuario123@ejemplo.com",
        "usuario@sub.ejemplo.com",
        "a@b.co"
    })
    void testEsValido_ConEmailsValidos_RetornaTrue(String emailValido) {
        assertTrue(ValidadorEmail.esValido(emailValido),
            "El email '" + emailValido + "' debería ser válido");
    }

    @Test
    void testEsValido_ConEmailNull_LanzaIllegalArgumentException() {
        // Act & Assert
        try {
            ValidadorEmail.esValido(null);
            fail("Se esperaba IllegalArgumentException");
        } catch (IllegalArgumentException excepcion) {
            assertEquals("El email no puede ser null", excepcion.getMessage());
        }
    }

    @Test
    void testEsValido_ConEmailMuyLargo_RetornaFalse() {
        // Arrange - Email con más de 254 caracteres
        String usuarioLargo = "a".repeat(250);
        String emailLargo = usuarioLargo + "@ejemplo.com";

        // Act
        boolean resultado = ValidadorEmail.esValido(emailLargo);

        // Assert
        assertFalse(resultado, "Emails con más de 254 caracteres son inválidos");
    }
}
```

### Ejemplo 3: Test de utilidades de strings

```java
package ar.unrn.poo.utilidades;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para StringUtils con métodos estáticos.
 */
public class StringUtilsTest {

    @ParameterizedTest(name = "Invertir ''{0}'' resulta en ''{1}''")
    @CsvSource({
        "hola, aloh",
        "java, avaj",
        "a, a",
        "ab, ba"
    })
    void testInvertir_ConStringsValidos_RetornaInvertido(
            String entrada, String esperado) {
        assertEquals(esperado, StringUtils.invertir(entrada));
    }

    @Test
    void testInvertir_ConStringVacio_RetornaVacio() {
        assertEquals("", StringUtils.invertir(""));
    }

    @Test
    void testInvertir_ConNull_LanzaIllegalArgumentException() {
        try {
            StringUtils.invertir(null);
            fail("Se esperaba IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test pasa
        }
    }

    @ParameterizedTest(name = "ContarVocales(''{0}'') = {1}")
    @CsvSource({
        "hola mundo, 4",
        "xyz, 0",
        "aeiou, 5",
        "AEIOU, 5",
        "'', 0"
    })
    void testContarVocales_ConDiferentesStrings_RetornaCantidadCorrecta(
            String texto, int esperado) {
        assertEquals(esperado, StringUtils.contarVocales(texto));
    }

    @Test
    void testEsPalindromo_ConPalindromo_RetornaTrue() {
        assertTrue(StringUtils.esPalindromo("ana"));
        assertTrue(StringUtils.esPalindromo("reconocer"));
        assertTrue(StringUtils.esPalindromo("Anita lava la tina"));
    }

    @Test
    void testEsPalindromo_ConNoPalindromo_RetornaFalse() {
        assertFalse(StringUtils.esPalindromo("hola"));
        assertFalse(StringUtils.esPalindromo("java"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testEsPalindromo_ConNullOVacio_LanzaExcepcion(String entrada) {
        try {
            StringUtils.esPalindromo(entrada);
            fail("Se esperaba excepción para entrada null o vacía");
        } catch (IllegalArgumentException e) {
            // Test pasa
        }
    }

    @Test
    void testCapitalizar_ConPalabras_CapitalizaCadaUna() {
        String resultado = StringUtils.capitalizar("hola mundo");
        assertEquals("Hola Mundo", resultado);
    }
}
```

### Ejemplo 4: Test de procesamiento de arreglos

```java
package ar.unrn.poo.arreglos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para Arreglos con métodos estáticos.
 */
public class ArreglosTest {
    
    private int[] datosOrdenados;
    private int[] datosDesordenados;
    
    @BeforeEach
    void setUp() {
        // Preparar datos de entrada comunes
        datosOrdenados = new int[]{1, 2, 3, 4, 5};
        datosDesordenados = new int[]{5, 2, 8, 1, 9};
    }

    @Test
    void testSuma_ConArregloValido_RetornaSumaCorrecta() {
        assertEquals(15, Arreglos.suma(datosOrdenados));
    }

    @Test
    void testSuma_ConArregloVacio_RetornaCero() {
        assertEquals(0, Arreglos.suma(new int[]{}));
    }

    @Test
    void testPromedio_ConArregloValido_RetornaPromedioCorreto() {
        assertEquals(3.0, Arreglos.promedio(datosOrdenados), 0.01);
    }

    @Test
    void testPromedio_ConArregloVacio_LanzaIllegalArgumentException() {
        try {
            Arreglos.promedio(new int[]{});
            fail("Se esperaba IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("vacío"));
        }
    }

    @Test
    void testMaximo_ConArregloValido_RetornaMaximo() {
        assertEquals(9, Arreglos.maximo(datosDesordenados));
    }

    @Test
    void testMinimo_ConArregloValido_RetornaMinimo() {
        assertEquals(1, Arreglos.minimo(datosDesordenados));
    }

    @Test
    void testOrdenar_ConArregloDesordenado_RetornaArregloOrdenado() {
        int[] resultado = Arreglos.ordenar(datosDesordenados);
        assertArrayEquals(new int[]{1, 2, 5, 8, 9}, resultado);
    }

    @Test
    void testContiene_ConElementoExistente_RetornaTrue() {
        assertTrue(Arreglos.contiene(datosOrdenados, 3));
    }

    @Test
    void testContiene_ConElementoInexistente_RetornaFalse() {
        assertFalse(Arreglos.contiene(datosOrdenados, 99));
    }

    @Test
    void testBuscar_ConElementoExistente_RetornaIndice() {
        assertEquals(2, Arreglos.buscar(datosOrdenados, 3));
    }

    @Test
    void testBuscar_ConElementoInexistente_RetornaMenosUno() {
        assertEquals(-1, Arreglos.buscar(datosOrdenados, 99));
    }
}
```

## Organización de Tests

### Tests por funcionalidad

Organizá los tests agrupando funcionalidades relacionadas:

```java
public class CalculadoraTest {
    // Grupo: Tests de suma
    @Test
    void testSumar_ConDosPositivos_RetornaSuma() { }

    @Test
    void testSumar_ConDosNegativos_RetornaSumaNegativa() { }

    @Test
    void testSumar_ConCero_RetornaOtroOperando() { }

    // Grupo: Tests de división
    @Test
    void testDividir_ConDivisorNoNulo_RetornaCociente() { }

    @Test
    void testDividir_ConDivisorCero_LanzaExcepcion() { }
}
```

### Clases anidadas con `@Nested`

JUnit 5 permite agrupar tests con clases internas:

```java
import org.junit.jupiter.api.Nested;

public class CalculadoraTest {

    @Nested
    class OperacionesSuma {
        @Test
        void conDosPositivos_RetornaSuma() {
            assertEquals(5, Calculadora.sumar(2, 3));
        }

        @Test
        void conDosNegativos_RetornaSumaNegativa() {
            assertEquals(-5, Calculadora.sumar(-2, -3));
        }
    }

    @Nested
    class OperacionesDivision {
        @Test
        void conDivisorNoNulo_RetornaCociente() {
            assertEquals(2.0, Calculadora.dividir(10, 5), 0.01);
        }

        @Test
        void conDivisorCero_LanzaExcepcion() {
            try {
                Calculadora.dividir(10, 0);
                fail("Se esperaba ArithmeticException");
            } catch (ArithmeticException e) {
                // Test pasa
            }
        }
    }
}
```

Ventajas de `@Nested`:

- Agrupación lógica visible en reportes
- Cada grupo puede tener su propio `@BeforeEach` para preparar datos
- Mejora la organización en tests grandes

## Cobertura de Tests

La **cobertura de tests** (test coverage) mide qué porcentaje del código es ejecutado por los tests.

### Tipos de cobertura

#### Cobertura de líneas

Porcentaje de líneas de código ejecutadas:

```java
public int calcular(int x) {
    if (x > 0) {
        return x * 2;  // Línea A
    } else {
        return x * 3;  // Línea B
    }
}

// Test con 50% de cobertura de líneas
@Test
void testCalcular_ConNumeroPositivo() {
    assertEquals(10, calcular(5));  // Ejecuta línea A, no B
}
```

#### Cobertura de ramas

Porcentaje de caminos condicionales ejecutados:

```java
// Para 100% cobertura de ramas, necesitás tests para:
@Test void testConPositivo() { calcular(5); }   // if (x > 0) → true
@Test void testConNegativo() { calcular(-5); }  // if (x > 0) → false
```

#### Cobertura de métodos

Porcentaje de métodos invocados al menos una vez.

### Herramientas de cobertura

#### JaCoCo (Java Code Coverage)

Agregá el plugin a `build.gradle`:

```gradle
plugins {
    id 'java'
    id 'jacoco'
}

jacoco {
    toolVersion = "0.8.10"
}

test {
    useJUnitPlatform()
    finalizedBy jacocoTestReport
}

jacocoTestReport {
    dependsOn test
    reports {
        xml.required = true
        html.required = true
    }
}
```

Ejecutar:

```bash
./gradlew test jacocoTestReport
```

El reporte se genera en `build/reports/jacoco/test/html/index.html`.

### Interpretación de cobertura

:::{important}
**100% de cobertura NO garantiza que el código esté bien testeado.** Solo indica que todas las líneas se ejecutaron, no que todos los casos se verificaron correctamente.
:::

**Ejemplo:**

```java
public int dividir(int a, int b) {
    return a / b;  // Puede lanzar ArithmeticException
}

// Test con 100% cobertura de líneas
@Test
void testDividir() {
    assertEquals(2, dividir(6, 3));  // Ejecuta la línea
}
```

Este test tiene 100% cobertura pero NO verifica el caso `b == 0`.

### Meta razonable

- **70-80% de cobertura**: Objetivo razonable para proyectos
- **90-100% de cobertura**: Ideal para código crítico
- **< 60% de cobertura**: Señal de testing insuficiente

:::{tip}
Priorizá **calidad** de tests sobre cantidad de cobertura. Un test bien pensado que verifica comportamiento es más valioso que 10 tests triviales que solo ejecutan código.
:::

## Buenas Prácticas

### FIRST: Características de buenos tests

Acrónimo que resume las características de tests efectivos:

**F - Fast (Rápidos)**

- Tests deben ejecutarse en milisegundos
- Suite completa en pocos segundos
- Si son lentos, los desarrolladores no los ejecutan

**I - Independent (Independientes)**

- Cada test se ejecuta en cualquier orden ({ref}`regla-0x4005`)
- No comparten estado mutable
- Resultado no depende de otros tests

**R - Repeatable (Repetibles)**

- Mismo resultado cada vez que se ejecutan
- No dependen de red, fechas, aleatorios sin seed
- Determinísticos

**S - Self-validating (Auto-validantes)**

- Test pasa (✅) o falla (❌), sin inspección manual
- No requieren verificar logs o archivos
- Assertions claros

**T - Timely (Oportunos)**

- Escribir tests junto con el código (o antes, en TDD)
- No postponer el testing

### Principios adicionales

#### DRY en tests, con moderación

Evitá duplicación, pero no sacrifiques claridad:

```java
// ❌ Demasiado DRY - dificulta entender cada test
private void verificarOperacion(int a, int b, int esperado) {
    assertEquals(esperado, Calculadora.sumar(a, b));
}

@Test void test1() { verificarOperacion(2, 3, 5); }
@Test void test2() { verificarOperacion(10, 20, 30); }

// ✅ Balance entre DRY y claridad - datos preparados pero test explícito
private int[] datosComunes;

@BeforeEach
void setUp() {
    datosComunes = new int[]{1, 2, 3, 4, 5};  // Setup común de datos
}

@Test
void testSumar_ConDosNumeros_RetornaSuma() {
    assertEquals(5, Calculadora.sumar(2, 3));  // Lógica explícita en test
}
```

#### Tests como documentación

Los tests deben documentar **cómo usar** los métodos:

```java
@Test
void ejemploDeUsoDeCalculadora() {
    // Un desarrollador nuevo puede leer este test
    // para entender cómo usar los métodos estáticos
    
    // 1. Operaciones básicas
    int suma = Calculadora.sumar(10, 5);
    int resta = Calculadora.restar(10, 5);
    
    // 2. División con validación
    double cociente = Calculadora.dividir(10, 2);
    
    // 3. Funciones matemáticas
    long factorial = Matematica.factorial(5);
    boolean esPrimo = Matematica.esPrimo(7);
    
    assertTrue(suma > 0);
    assertTrue(esPrimo);
}
```

#### Arrange explícito

Hacé explícito el setup, no relies en "magia":

```java
// ❌ Setup implícito - no es obvio de dónde vienen los datos
@Test
void testProcesar() {
    assertEquals(expected, Procesador.procesar(entrada));  // ¿Qué es expected?
}

// ✅ Setup explícito
@Test
void testProcesar_ConEntradaEspecifica_RetornaResultadoEsperado() {
    // Arrange - Claro qué datos usamos
    String entrada = "datos de prueba";
    String esperado = "DATOS DE PRUEBA";

    // Act
    String resultado = Procesador.procesar(entrada);

    // Assert
    assertEquals(esperado, resultado);
}
```

#### Un concepto por test

Cada test debe verificar un solo aspecto del comportamiento:

```java
// ❌ Test verifica múltiples conceptos
@Test
void testTodo() {
    int[] datos = {5, 3, 8, 1};
    
    // Concepto 1: Suma funciona
    assertEquals(17, Arreglos.suma(datos));
    
    // Concepto 2: Máximo funciona
    assertEquals(8, Arreglos.maximo(datos));
    
    // Concepto 3: Ordenar funciona
    assertArrayEquals(new int[]{1, 3, 5, 8}, Arreglos.ordenar(datos));
}

// ✅ Cada test un concepto
@Test
void testSuma_ConArregloValido_RetornaSuma() {
    int[] datos = {5, 3, 8, 1};
    assertEquals(17, Arreglos.suma(datos));
}

@Test
void testMaximo_ConArregloValido_RetornaMaximo() {
    int[] datos = {5, 3, 8, 1};
    assertEquals(8, Arreglos.maximo(datos));
}

@Test
void testOrdenar_ConArregloDesordenado_RetornaOrdenado() {
    int[] datos = {5, 3, 8, 1};
    assertArrayEquals(new int[]{1, 3, 5, 8}, Arreglos.ordenar(datos));
}
```

## Anti-patrones en Testing

### Tests frágiles

Tests que fallan frecuentemente por razones no relacionadas con bugs:

```java
// ❌ Frágil - depende de fecha actual del sistema
@Test
void testObtenerFecha() {
    String fechaActual = Fechas.obtenerFechaActual();
    assertEquals("2024-03-15", fechaActual);
    // Falla si se ejecuta en cualquier otro día
}

// ✅ Robusto - verifica formato, no valor exacto
@Test
void testObtenerFecha_RetornaFormatoISO() {
    String fechaActual = Fechas.obtenerFechaActual();
    assertTrue(fechaActual.matches("\\d{4}-\\d{2}-\\d{2}"),
        "Fecha debe tener formato YYYY-MM-DD");
}
```

### Tests que ignoran excepciones

```java
// ❌ Ignora excepciones - test siempre pasa
@Test
void testDividir() {
    try {
        double resultado = Calculadora.dividir(10, 0);
        assertEquals(0, resultado);
    } catch (Exception e) {
        // Silencia errores - MAL!
    }
}

// ✅ Deja que excepciones propaguen o las verifica
@Test
void testDividir_ConDivisorCero_LanzaExcepcion() {
    try {
        Calculadora.dividir(10, 0);
        fail("Se esperaba ArithmeticException");
    } catch (ArithmeticException e) {
        // Test pasa correctamente
    }
}
```

### Assertions múltiples sin mensajes descriptivos

```java
// ❌ Si falla, no sabés cuál assertion falló
@Test
void testParsearFecha() {
    int[] componentes = Parser.parsearFecha("2024-03-15");
    assertEquals(2024, componentes[0]);
    assertEquals(3, componentes[1]);
    assertEquals(15, componentes[2]);
}

// ✅ Con mensajes descriptivos
@Test
void testParsearFecha_RetornaComponentesCorrectos() {
    int[] componentes = Parser.parsearFecha("2024-03-15");
    
    assertEquals(2024, componentes[0], "Año debe ser 2024");
    assertEquals(3, componentes[1], "Mes debe ser 3");
    assertEquals(15, componentes[2], "Día debe ser 15");
}
```

## Test-Driven Development (TDD)

**Test-Driven Development** es una metodología de desarrollo donde los tests se escriben **antes** que el código de producción.

### El ciclo Red-Green-Refactor

TDD sigue un ciclo iterativo de tres pasos:

```{figure} 2/tdd_ciclo.svg
:label: fig-tdd-ciclo
:align: center
:width: 70%

Ciclo TDD: Red (test falla) → Green (test pasa) → Refactor (mejora código).
```

**1. Red (Rojo) - Escribir test que falle:**

```java
@Test
void testSumar_ConDosNumeros_RetornaSuma() {
    assertEquals(5, Calculadora.sumar(2, 3));  // Método no existe aún
}
```

Ejecutás el test y **falla** (rojo ❌) porque el código no existe.

**2. Green (Verde) - Escribir código mínimo que pase:**

```java
public class Calculadora {
    public static int sumar(int a, int b) {
        return a + b;  // Implementación mínima
    }
}
```

Ejecutás el test y **pasa** (verde ✅).

**3. Refactor - Mejorar código manteniendo tests verdes:**

```java
public class Calculadora {
    /**
     * Suma dos números enteros.
     *
     * @param sumando1 primer número
     * @param sumando2 segundo número
     * @return suma de los dos números
     */
    public static int sumar(int sumando1, int sumando2) {
        return sumando1 + sumando2;
    }
}
```

Mejorás el código (nombres, documentación, estructura) y los tests siguen pasando.

### Beneficios de TDD

1. **Diseño mejorado**: Escribir tests primero fuerza a pensar en la API antes de implementar
2. **Código testeable**: El código naturalmente es más modular y testeable
3. **Cobertura alta**: Todo el código tiene tests porque se escribieron primero
4. **Documentación**: Los tests documentan el comportamiento esperado
5. **Confianza**: Sabés que cada línea funciona porque hay un test que la verifica

### Ejemplo completo de TDD

Desarrollemos funciones matemáticas usando TDD.

**Iteración 1: Factorial de 0**

```java
// Test (Red)
@Test
void testFactorial_ConCero_RetornaUno() {
    assertEquals(1, Matematica.factorial(0));  // Método no existe
}

// Implementación mínima (Green)
public class Matematica {
    public static long factorial(int n) {
        return 1;  // Implementación trivial que pasa el test
    }
}
```

**Iteración 2: Factorial de número positivo**

```java
// Test (Red)
@Test
void testFactorial_ConCinco_Retorna120() {
    assertEquals(120, Matematica.factorial(5));
}

// Implementación (Green)
public class Matematica {
    public static long factorial(int n) {
        if (n == 0) return 1;
        long resultado = 1;
        for (int i = 1; i <= n; i++) {
            resultado = resultado * i;
        }
        return resultado;
    }
}
```

**Iteración 3: Validar entrada negativa**

```java
// Test (Red)
@Test
void testFactorial_ConNumeroNegativo_LanzaExcepcion() {
    try {
        Matematica.factorial(-1);
        fail("Se esperaba IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        assertTrue(e.getMessage().contains("negativo"));
    }
}

// Implementación (Green)
public class Matematica {
    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("El número no puede ser negativo");
        }
        if (n == 0) return 1;
        long resultado = 1;
        for (int i = 1; i <= n; i++) {
            resultado = resultado * i;
        }
        return resultado;
    }
}
```

**Iteración 4: Refactorizar (mantener tests verdes)**

```java
public class Matematica {
    /**
     * Calcula el factorial de un número.
     *
     * @param n número del cual calcular el factorial (debe ser >= 0)
     * @return factorial de n
     * @throws IllegalArgumentException si n es negativo
     */
    public static long factorial(int n) {
        validarNoNegativo(n);
        return calcularFactorial(n);
    }
    
    private static void validarNoNegativo(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("El número no puede ser negativo: " + n);
        }
    }
    
    private static long calcularFactorial(int n) {
        long resultado = 1;
        for (int i = 2; i <= n; i++) {
            resultado = resultado * i;
        }
        return resultado;
    }
}

// Refactor: Ahora todos los tests siguen pasando
```

:::{tip}
TDD es especialmente útil cuando:

- Estás aprendiendo una nueva API o librería
- El problema es complejo y necesitás dividirlo en pasos pequeños
- Trabajás en código crítico que debe ser robusto
- Querés diseñar la API pública antes de implementarla
  :::

### TDD vs Test-After

| Aspecto                   | TDD (Test-First)  | Test-After                    |
| ------------------------- | ----------------- | ----------------------------- |
| **Cuándo escribir tests** | Antes del código  | Después del código            |
| **Cobertura**             | Naturalmente alta | Puede ser parcial             |
| **Diseño**                | Influye en diseño | Se adapta al diseño existente |
| **Refactoring**           | Más seguro        | Menos confiable               |
| **Curva de aprendizaje**  | Más empinada      | Más suave                     |

:::{important}
En esta cátedra, practicaremos ambas aproximaciones. Lo fundamental es que **el código tenga tests**, independientemente de cuándo se escribieron.
:::

## Estrategias de Testing

### Análisis de valores límite (Boundary Value Analysis)

Los errores ocurren frecuentemente en los **límites** o **bordes** de los rangos válidos. Esta técnica se enfoca en testear:

- El valor mínimo válido
- Justo por debajo del mínimo
- El valor máximo válido
- Justo por encima del máximo
- Valores típicos en el medio

**Ejemplo: Validador de edad**

```java
public class Validador {
    /**
     * Valida que la edad esté entre 18 y 120 años.
     *
     * @param edad edad a validar
     * @return true si es válida, false en caso contrario
     */
    public static boolean esEdadValida(int edad) {
        return edad >= 18 && edad <= 120;
    }
}
```

**Tests de valores límite:**

```java
public class ValidadorTest {

    // Límite inferior
    @Test
    void testEsEdadValida_Con17_RetornaFalse() {
        assertFalse(Validador.esEdadValida(17), "17 está por debajo del mínimo");
    }

    @Test
    void testEsEdadValida_Con18_RetornaTrue() {
        assertTrue(Validador.esEdadValida(18), "18 es el mínimo válido");
    }

    @Test
    void testEsEdadValida_Con19_RetornaTrue() {
        assertTrue(Validador.esEdadValida(19), "19 está dentro del rango");
    }

    // Límite superior
    @Test
    void testEsEdadValida_Con119_RetornaTrue() {
        assertTrue(Validador.esEdadValida(119), "119 está dentro del rango");
    }

    @Test
    void testEsEdadValida_Con120_RetornaTrue() {
        assertTrue(Validador.esEdadValida(120), "120 es el máximo válido");
    }

    @Test
    void testEsEdadValida_Con121_RetornaFalse() {
        assertFalse(Validador.esEdadValida(121), "121 excede el máximo");
    }

    // Valor típico
    @Test
    void testEsEdadValida_Con50_RetornaTrue() {
        assertTrue(Validador.esEdadValida(50), "50 es un valor típico válido");
    }

    // Valores extremos adicionales
    @Test
    void testEsEdadValida_ConValorNegativo_RetornaFalse() {
        assertFalse(Validador.esEdadValida(-5));
    }

    @Test
    void testEsEdadValida_Con0_RetornaFalse() {
        assertFalse(Validador.esEdadValida(0));
    }
}
```

**Con tests parametrizados:**

```java
@ParameterizedTest(name = "Edad {0} debe ser {1}")
@CsvSource({
    "-5,  false",  // Negativo
    "0,   false",  // Cero
    "17,  false",  // Justo debajo del mínimo
    "18,  true",   // Mínimo válido
    "19,  true",   // Justo arriba del mínimo
    "50,  true",   // Valor típico
    "119, true",   // Justo debajo del máximo
    "120, true",   // Máximo válido
    "121, false",  // Justo arriba del máximo
    "200, false"   // Muy por encima
})
void testEsEdadValida_ValoresLimite(int edad, boolean esperado) {
    assertEquals(esperado, Validador.esEdadValida(edad));
}
```

### Particiones de equivalencia

Dividí el dominio de entrada en **clases de equivalencia** donde todos los valores deberían comportarse igual. Testeá un valor representativo de cada clase.

**Ejemplo: Calculadora de descuentos**

```java
public class Calculadora {
    /**
     * Calcula descuento según el monto de compra:
     * - Menos de 100: sin descuento (0%)
     * - 100-499: descuento pequeño (5%)
     * - 500-999: descuento medio (10%)
     * - 1000 o más: descuento grande (15%)
     */
    public static double calcularDescuento(double monto) {
        if (monto < 100) return 0.0;
        if (monto < 500) return 0.05;
        if (monto < 1000) return 0.10;
        return 0.15;
    }
}
```

**Particiones:**

1. **P1**: monto < 100 → descuento 0%
2. **P2**: 100 ≤ monto < 500 → descuento 5%
3. **P3**: 500 ≤ monto < 1000 → descuento 10%
4. **P4**: monto ≥ 1000 → descuento 15%

**Tests:**

```java
@ParameterizedTest(name = "Monto {0} debe tener descuento {1}")
@CsvSource({
    "50,    0.0",    // P1: representante partición 1
    "250,   0.05",   // P2: representante partición 2
    "750,   0.10",   // P3: representante partición 3
    "1500,  0.15",   // P4: representante partición 4
    // Límites
    "99.99, 0.0",    // Límite P1/P2
    "100,   0.05",   // Límite P1/P2
    "499,   0.05",   // Límite P2/P3
    "500,   0.10",   // Límite P2/P3
    "999,   0.10",   // Límite P3/P4
    "1000,  0.15"    // Límite P3/P4
})
void testCalcularDescuento_ConDiferentesMontos(double monto, double descuentoEsperado) {
    assertEquals(descuentoEsperado, Calculadora.calcularDescuento(monto), 0.001);
}
```

### Casos de prueba especiales

#### Valores nulos

Siempre considerá el caso `null`:

```java
@Test
void testProcesar_ConEntradaNull_LanzaIllegalArgumentException() {
    try {
        Procesador.procesar(null);
        fail("Se esperaba IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        assertTrue(e.getMessage().contains("null"));
    }
}
```

#### Arreglos vacíos

```java
@Test
void testCalcularPromedio_ConArregloVacio_LanzaIllegalArgumentException() {
    int[] valoresVacios = {};

    try {
        Estadisticas.calcularPromedio(valoresVacios);
        fail("Se esperaba IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        // Test pasa
    }
}
```

#### Strings vacíos y con espacios

```java
@ParameterizedTest
@ValueSource(strings = {"", "   ", "\t", "\n", "  \t\n  "})
void testValidarNombre_ConStringBlanco_RetornaFalse(String nombreBlanco) {
    assertFalse(Validador.esNombreValido(nombreBlanco));
}
```

#### Duplicados en arreglos

```java
@Test
void testContarUnicos_ConDuplicados_RetornaCantidadSinRepetidos() {
    int[] conDuplicados = {1, 2, 2, 3, 3, 3};
    
    int unicos = Arreglos.contarUnicos(conDuplicados);
    
    assertEquals(3, unicos, "Debe contar solo valores únicos: 1, 2, 3");
}
```

## Test Smells: Olores en Tests

Los **test smells** son señales de que los tests tienen problemas de diseño o mantenibilidad.

### Test interdependiente

**Olor:** Tests que deben ejecutarse en orden específico.

```java
// ❌ Tests interdependientes (usando estado estático mutable)
public class ContadorGlobalTest {
    private static int contador = 0;  // ❌ Estado compartido

    @Test
    void test1_Incrementar() {
        contador++;
        assertEquals(1, contador);  // Depende del orden
    }

    @Test
    void test2_IncrementarOtraVez() {
        contador++;
        assertEquals(2, contador);  // ❌ Asume que test1 ya ejecutó
    }
}

// ✅ Tests independientes (sin estado compartido)
public class CalculadoraTest {
    @Test
    void testSumar_ConDosPositivos_RetornaSuma() {
        assertEquals(5, Calculadora.sumar(2, 3));
    }

    @Test
    void testSumar_ConDosNegativos_RetornaSumaNegativa() {
        assertEquals(-5, Calculadora.sumar(-2, -3));
    }
}
```

### Test obscuro

**Olor:** No es claro qué se está testeando o por qué.

```java
// ❌ Test obscuro
@Test
void testMetodo1() {
    int resultado = Calculadora.metodo(123, 456, true);
    assertEquals(42, resultado);  // ¿Por qué 42?
}

// ✅ Test claro
@Test
void testCalcularAniosHastaJubilacion_ConEdadActual35_Retorna30() {
    // Arrange
    int edadActual = 35;
    int edadJubilacion = 65;

    // Act
    int aniosRestantes = Calculadora.calcularAniosHastaJubilacion(edadActual, edadJubilacion);

    // Assert
    assertEquals(30, aniosRestantes, "65 - 35 = 30 años hasta jubilación");
}
```

### Test con lógica compleja

**Olor:** Tests con condicionales, lazos, o cálculos complejos.

```java
// ❌ Test con lógica
@Test
void testCalcular() {
    for (int i = 0; i < 10; i++) {
        int resultado = Calculadora.calcular(i);
        if (i % 2 == 0) {
            assertEquals(i * 2, resultado);
        } else {
            assertEquals(i * 3, resultado);
        }
    }
}

// ✅ Tests separados sin lógica
@ParameterizedTest
@CsvSource({
    "0, 0",   // 0 * 2
    "2, 4",   // 2 * 2
    "4, 8",   // 4 * 2
    "6, 12"   // 6 * 2
})
void testCalcular_ConNumeroPar_RetornaDoble(int entrada, int esperado) {
    assertEquals(esperado, Calculadora.calcular(entrada));
}

@ParameterizedTest
@CsvSource({
    "1, 3",   // 1 * 3
    "3, 9",   // 3 * 3
    "5, 15",  // 5 * 3
    "7, 21"   // 7 * 3
})
void testCalcular_ConNumeroImpar_RetornaTriple(int entrada, int esperado) {
    assertEquals(esperado, Calculadora.calcular(entrada));
}
```

### Test demasiado largo

**Olor:** Tests con más de 20-30 líneas, difíciles de entender.

```java
// ❌ Test muy largo
@Test
void testProcesoCompleto() {
    // 100 líneas de setup
    // Múltiples operaciones
    // Muchas assertions
}

// ✅ Dividir en tests más pequeños
@Test
void testPaso1_Validacion() { }

@Test
void testPaso2_Calculo() { }

@Test
void testPaso3_Formato() { }
```

### Assertion redundante

**Olor:** Assertions que no aportan valor.

```java
// ❌ Assertions redundantes
@Test
void testSumar() {
    int resultado = Calculadora.sumar(2, 3);
    assertTrue(resultado == 5);      // Redundante
    assertEquals(5, resultado);       // Este alcanza
    assertFalse(resultado != 5);     // Redundante
}

// ✅ Assertion única y clara
@Test
void testSumar_ConDosYTres_RetornaCinco() {
    int resultado = Calculadora.sumar(2, 3);
    assertEquals(5, resultado);
}
```

### Test silencioso

**Olor:** Test que no falla cuando debería.

```java
// ❌ Test silencioso - no verifica nada
@Test
void testProcesar() {
    Procesador.procesar("datos");
    // ¿Funcionó? No hay assertion - siempre pasa
}

// ✅ Test con verificación
@Test
void testProcesar_ConDatosValidos_RetornaResultado() {
    String resultado = Procesador.procesar("datos");
    
    assertNotNull(resultado);
    assertFalse(resultado.isEmpty());
}
```

## Debugging de Tests Fallidos

Cuando un test falla, seguí estos pasos sistemáticos:

### 1. Leer el mensaje de error completo

```
org.opentest4j.AssertionFailedError:
Cliente premium debe recibir 20% de descuento: 1000 * 0.8 = 800 ==>
expected: <800.0> but was: <1000.0>
	at CalculadoraTest.testCalcularDescuento(CalculadoraTest.java:45)
```

Información clave:

- **Qué falló**: Un assertion
- **Dónde**: Línea 45 de CalculadoraTest
- **Esperado vs. Obtenido**: 800.0 vs 1000.0
- **Contexto**: Mensaje descriptivo

### 2. Ejecutar solo ese test

```bash
./gradlew test --tests CalculadoraTest.testCalcularDescuento
```

Aislá el problema ejecutando solo el test que falla.

### 3. Agregar prints temporales

```java
@Test
void testCalcularDescuento() {
    double monto = 1000.0;
    double porcentaje = 0.20;

    System.out.println("Monto: " + monto);
    System.out.println("Porcentaje: " + porcentaje);

    double descuento = Calculadora.aplicarDescuento(monto, porcentaje);
    System.out.println("Descuento calculado: " + descuento);

    assertEquals(800.0, descuento, 0.001);
}
```

:::{note}
Recordá eliminar los prints antes de commitear el código.
:::

### 4. Usar el debugger del IDE

1. Ponele un **breakpoint** en la línea del assertion
2. Ejecutá el test en **modo debug**
3. Inspeccioná variables
4. Avanzá paso a paso (Step Into/Over)

### 5. Simplificar el test

Si el test es complejo, simplificalo temporalmente:

```java
@Test
void testProblematico() {
    // Simplificar a lo mínimo que reproduce el error
    double resultado = Calculadora.dividir(10, 0);
    assertEquals(0, resultado);  // ¿Por qué falla?
}
```

### 6. Verificar precondiciones

```java
@Test
void testProcesar_ConDatosValidos_RetornaResultado() {
    // Verificar datos de entrada
    String entrada = "datos válidos";
    assertNotNull(entrada, "Entrada no debe ser null");
    assertFalse(entrada.isEmpty(), "Entrada no debe estar vacía");

    // Realizar operación
    String resultado = Procesador.procesar(entrada);

    // Verificar resultado
    assertNotNull(resultado);
}
```

### 7. Revisar cambios recientes

Si el test pasaba antes:

- ¿Qué código cambió?
- ¿Se modificó alguna dependencia?
- ¿Se agregaron nuevos datos o configuración?

```bash
# Ver diferencias desde último commit
git diff HEAD
```

### Ejemplo de debugging completo

**Test que falla:**

```java
@Test
void testAplicarDescuento_ConPorcentaje20_RetornaPrecioConDescuento() {
    // Arrange
    double precioOriginal = 1000.0;
    double porcentajeDescuento = 0.20;

    // Act
    double precioFinal = Calculadora.aplicarDescuento(precioOriginal, porcentajeDescuento);

    // Assert
    assertEquals(800.0, precioFinal, 0.01, "1000 - 20% = 800");
}
```

**Error:**

```
expected: <800.0> but was: <1000.0>
```

**Paso 1: Agregar prints**

```java
@Test
void testAplicarDescuento_ConPorcentaje20_RetornaPrecioConDescuento() {
    double precioOriginal = 1000.0;
    System.out.println("Precio original: " + precioOriginal);

    double porcentajeDescuento = 0.20;
    System.out.println("Porcentaje descuento: " + porcentajeDescuento);

    double precioFinal = Calculadora.aplicarDescuento(precioOriginal, porcentajeDescuento);
    System.out.println("Precio final: " + precioFinal);

    assertEquals(800.0, precioFinal, 0.01);
}
```

**Output:**

```
Precio original: 1000.0
Porcentaje descuento: 0.20
Precio final: 1000.0
```

**Paso 2: Revisar implementación**

```java
public static double aplicarDescuento(double precio, double porcentaje) {
    // ❌ Bug: no aplica el descuento
    return precio;
}
```

**Paso 3: Corregir**

```java
public static double aplicarDescuento(double precio, double porcentaje) {
    double montoDescuento = precio * porcentaje;
    return precio - montoDescuento;
}
```

**Paso 4: Ejecutar test nuevamente**

```
✅ Test pasa
```

## Resumen

En este apunte hemos explorado:

1. **Fundamentos del testing**: Por qué testear y tipos de tests
2. **JUnit 5**: Framework moderno para testing en Java
3. **Estructura AAA**: Arrange-Act-Assert ({ref}`regla-0x4001`)
4. **Assertions**: Verificaciones de JUnit (`assertEquals`, `assertTrue`, etc.)
5. **Ciclo de vida**: `@BeforeEach`, `@AfterEach`, `@BeforeAll`, `@AfterAll`
6. **Convenciones de nombrado**: ({ref}`regla-0x4003`)
7. **Reglas de testing**: Independencia ({ref}`regla-0x4005`), una llamada ({ref}`regla-0x4002`), sin lógica ({ref}`regla-0x4004`)
8. **Tests parametrizados**: `@ParameterizedTest` para múltiples casos
9. **Excepciones**: Verificación con try-catch de comportamiento excepcional
10. **TDD**: Test-Driven Development y el ciclo Red-Green-Refactor
11. **Estrategias de testing**: Valores límite, particiones de equivalencia, casos especiales
12. **Test smells**: Identificar y evitar olores en tests
13. **Debugging**: Diagnosticar y resolver tests fallidos
14. **Buenas prácticas**: FIRST, claridad, mantenibilidad

:::{important}
**Testing no es opcional**: Es una habilidad fundamental del desarrollo profesional. Código sin tests es código legacy desde el día cero.
:::

## Ejercicios

```exercise
:label: ej-junit-calculadora

Implementá la clase `Calculadora` con métodos estáticos `sumar`, `restar`, `multiplicar` y `dividir`. Luego escribí una clase `CalculadoraTest` completa que:

1. Verifique operaciones básicas con números positivos
2. Verifique operaciones con números negativos
3. Verifique división por cero (debe lanzar excepción)
4. Verifique casos límite (cero, números grandes)
5. Use tests parametrizados para múltiples casos

Asegurate de seguir todas las reglas de testing vistas.
```

```exercise
:label: ej-junit-arreglos

Implementá una clase `Arreglos` con métodos estáticos para manipular arreglos de enteros: `suma`, `promedio`, `maximo`, `minimo`, `ordenar`, `buscar`. Escribí tests completos que:

1. Verifiquen cada operación individualmente
2. Prueben casos límite (arreglo vacío, un elemento)
3. Verifiquen excepciones en operaciones inválidas (arreglo null, vacío)
4. Usen `@BeforeEach` para preparar datos de entrada comunes
5. Sean independientes entre sí

Incluí al menos 15 tests diferentes.
```

```exercise
:label: ej-junit-validador

Creá una clase `ValidadorContrasena` con un método estático `esValida(String contrasena)` que valide que una contraseña cumpla:
- Mínimo 8 caracteres
- Al menos una mayúscula
- Al menos un número
- Al menos un carácter especial (@, #, $, etc.)

Escribí tests parametrizados que verifiquen múltiples contraseñas válidas e inválidas. Usá `@CsvSource` con al menos 10 casos de prueba.
```

```exercise
:label: ej-junit-strings

Implementá una clase `StringUtils` con métodos estáticos:
- `invertir(String s)`: Invierte el string
- `contarVocales(String s)`: Cuenta vocales
- `esPalindromo(String s)`: Verifica si es palíndromo
- `capitalizar(String s)`: Capitaliza cada palabra

Escribí tests completos para toda la funcionalidad, manteniendo las reglas de testing.
```

```exercise
:label: ej-junit-tdd-matematica

Usando TDD (Test-Driven Development), implementá una clase `Matematica` con métodos estáticos:
- `factorial(int n)`: Calcula el factorial
- `fibonacci(int n)`: Retorna el n-ésimo número de Fibonacci
- `esPrimo(int n)`: Verifica si es primo
- `mcd(int a, int b)`: Calcula el máximo común divisor

Requisitos:
1. Comenzá escribiendo el test antes de cada método
2. Implementá el mínimo código para que el test pase
3. Refactorizá manteniendo los tests verdes
4. Documentá cada iteración del ciclo Red-Green-Refactor en comentarios
5. Al finalizar, deberías tener al menos 12 tests
```

```exercise
:label: ej-junit-valores-limite

Creá una clase `Validador` con métodos estáticos que validen fechas según estas reglas:
- `esAnioValido(int anio)`: Año válido entre 1900-2100
- `esMesValido(int mes)`: Mes válido entre 1-12
- `esDiaValido(int dia, int mes, int anio)`: Día válido según el mes (28/29/30/31)
- `esBisiesto(int anio)`: Verifica si es año bisiesto

Escribí tests usando análisis de valores límite para:
1. Límites de año (1899, 1900, 1901, 2099, 2100, 2101)
2. Límites de mes (0, 1, 2, 11, 12, 13)
3. Límites de día para cada tipo de mes
4. Casos especiales de febrero en años bisiestos y no bisiestos

Usá tests parametrizados donde sea apropiado.
```

```exercise
:label: ej-junit-particiones

Implementá una clase `Tarifas` con un método estático `calcularTarifaEstacionamiento(double horas)`:
- Primera hora: $100
- Horas 2-5: $80 por hora
- Horas 6-12: $60 por hora
- Más de 12 horas: tarifa plana de $1000

Identificá las particiones de equivalencia y escribí tests que cubran:
1. Al menos un caso representativo de cada partición
2. Todos los valores límite entre particiones
3. Casos especiales (0 horas, valores negativos)
4. Valores decimales (ej: 2.5 horas)

Usá `@CsvSource` para organizar los casos de prueba.
```

```exercise
:label: ej-junit-test-smells

El siguiente código contiene múltiples test smells. Identificalos y refactorizá los tests corrigiendo cada problema:

\`\`\`java
public class CalculadoraTest {
    private static int acumulador = 0;  // Estado compartido

    @Test
    void test1() {
        acumulador = acumulador + Calculadora.sumar(2, 3);
        assertTrue(acumulador > 0);
    }

    @Test
    void test2() {
        acumulador = acumulador + Calculadora.sumar(10, 20);
        assertEquals(35, acumulador);  // Depende de test1
    }

    @Test
    void test3() {
        for (int i = 0; i < 5; i++) {
            int resultado = Calculadora.multiplicar(i, 2);
            if (i % 2 == 0) {
                assertTrue(resultado >= 0);
            }
        }
    }

    @Test
    void test4() {
        Calculadora.dividir(10, 2);  // Sin assertion
    }
}
\`\`\`

Reescribí todos los tests siguiendo las buenas prácticas vistas en el apunte.
```

```exercise
:label: ej-junit-debugging

Un test está fallando con el siguiente error:

\`\`\`
expected: <[1, 2, 3, 4, 5]> but was: <[5, 4, 3, 2, 1]>
\`\`\`

El test es:

\`\`\`java
@Test
void testOrdenar_ConArregloDesordenado_RetornaOrdenado() {
    int[] entrada = {3, 1, 4, 5, 2};
    
    int[] resultado = Ordenador.ordenar(entrada);
    
    assertArrayEquals(new int[]{1, 2, 3, 4, 5}, resultado);
}
\`\`\`

Tareas:
1. Analizá el error: ¿Qué está pasando?
2. Formulá al menos 3 hipótesis de qué puede estar causando el problema
3. Describí qué pasos de debugging usarías (prints, breakpoints, etc.)
4. Proponé posibles soluciones según cada hipótesis
```
