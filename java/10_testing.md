---
title: "Testing con JUnit"
description: Guía sobre pruebas unitarias en Java usando JUnit 5.
---

# Testing con JUnit

Las **pruebas unitarias** son una práctica fundamental en el desarrollo de software que permite verificar que cada unidad de código (típicamente un método) funciona correctamente de forma aislada. JUnit es el framework de testing más utilizado en Java.

## ¿Por Qué Escribir Tests?

- **Detectar errores temprano**: antes de que lleguen a producción
- **Documentar comportamiento**: los tests muestran cómo usar el código
- **Facilitar refactoring**: cambiar código con confianza
- **Diseño mejorado**: código testeable suele ser mejor diseñado
- **Regresión**: evitar que bugs corregidos vuelvan a aparecer

## Configuración de JUnit 5

### Dependencia Maven

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.0</version>
    <scope>test</scope>
</dependency>
```

### Dependencia Gradle

```groovy
testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
```

## Estructura de un Test

### Anatomía Básica

```{code} java
:caption: Test básico con JUnit 5

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculadoraTest {
    
    @Test
    void sumarDosNumerosPositivos() {
        // Arrange (preparar)
        Calculadora calc = new Calculadora();
        
        // Act (actuar)
        int resultado = calc.sumar(2, 3);
        
        // Assert (verificar)
        assertEquals(5, resultado);
    }
}
```

### Patrón AAA (Arrange-Act-Assert)

El patrón AAA organiza cada test en tres secciones claras:

1. **Arrange**: preparar los datos y objetos necesarios
2. **Act**: ejecutar el código que se está probando
3. **Assert**: verificar que el resultado es el esperado

```{code} java
:caption: Ejemplo del patrón AAA

@Test
void calcularPromedioDeArreglo() {
    // Arrange
    int[] numeros = {10, 20, 30, 40, 50};
    Estadisticas stats = new Estadisticas();
    
    // Act
    double promedio = stats.calcularPromedio(numeros);
    
    // Assert
    assertEquals(30.0, promedio, 0.001);  // Con tolerancia
}
```

## Anotaciones Principales

### @Test

Marca un método como test:

```{code} java
:caption: Anotación @Test

@Test
void miPrueba() {
    // código del test
}
```

### @DisplayName

Proporciona un nombre descriptivo para el test:

```{code} java
:caption: Uso de @DisplayName

@Test
@DisplayName("Debe lanzar excepción cuando el divisor es cero")
void divisionPorCero() {
    assertThrows(ArithmeticException.class, () -> {
        int resultado = 10 / 0;
    });
}
```

:::{note}
**Excepción para lambdas en testing:** Las expresiones lambda (`->`) utilizadas con `assertThrows`, `assertAll` y otros métodos de JUnit 5 son **aceptables** porque forman parte de la API del framework de testing. Esta es la única excepción a la regla de no usar programación funcional en el curso.
:::

### @BeforeEach y @AfterEach

Se ejecutan antes y después de **cada** test:

```{code} java
:caption: Métodos de configuración por test

class ArchivoTest {
    private Path archivoTemporal;
    
    @BeforeEach
    void setUp() throws IOException {
        // Se ejecuta antes de cada test
        archivoTemporal = Files.createTempFile("test-", ".txt");
        Files.writeString(archivoTemporal, "contenido de prueba");
    }
    
    @AfterEach
    void tearDown() throws IOException {
        // Se ejecuta después de cada test
        Files.deleteIfExists(archivoTemporal);
    }
    
    @Test
    void leerArchivo() throws IOException {
        String contenido = Files.readString(archivoTemporal);
        assertEquals("contenido de prueba", contenido);
    }
}
```

### @BeforeAll y @AfterAll

Se ejecutan una vez antes y después de **todos** los tests de la clase:

```{code} java
:caption: Métodos de configuración global

class BaseDeDatosTest {
    private static Connection conexion;
    
    @BeforeAll
    static void conectar() {
        // Se ejecuta una vez antes de todos los tests
        conexion = DriverManager.getConnection("...");
    }
    
    @AfterAll
    static void desconectar() {
        // Se ejecuta una vez después de todos los tests
        conexion.close();
    }
    
    @Test
    void consultarUsuarios() {
        // Usa la conexión compartida
    }
}
```

:::{note}
Los métodos `@BeforeAll` y `@AfterAll` deben ser `static` (a menos que se use `@TestInstance(Lifecycle.PER_CLASS)`).
:::

### @Disabled

Desactiva temporalmente un test:

```{code} java
:caption: Test deshabilitado

@Test
@Disabled("Bug #123: funcionalidad pendiente de implementar")
void funcionalidadPendiente() {
    // Este test no se ejecutará
}
```

## Assertions (Verificaciones)

### Igualdad

```{code} java
:caption: Assertions de igualdad

// Valores primitivos
assertEquals(5, resultado);
assertNotEquals(0, resultado);

// Objetos (usa equals())
assertEquals("esperado", cadena);

// Con mensaje personalizado
assertEquals(5, resultado, "El resultado debería ser 5");

// Doubles con tolerancia (delta)
assertEquals(3.14159, calculado, 0.00001);
```

### Booleanos

```{code} java
:caption: Assertions booleanas

assertTrue(edad >= 18, "Debe ser mayor de edad");
assertFalse(lista.isEmpty(), "La lista no debería estar vacía");
```

### Nulos

```{code} java
:caption: Assertions de nulidad

assertNull(objetoNoInicializado);
assertNotNull(resultado, "El resultado no debería ser null");
```

### Referencias

```{code} java
:caption: Assertions de referencia

// Misma instancia (==)
assertSame(singleton1, singleton2);
assertNotSame(objeto1, objeto2);
```

### Arreglos

```{code} java
:caption: Assertions de arreglos

int[] esperado = {1, 2, 3};
int[] obtenido = {1, 2, 3};

assertArrayEquals(esperado, obtenido);
```

### Excepciones

```{code} java
:caption: Verificar excepciones

// Verificar que se lanza la excepción
assertThrows(IllegalArgumentException.class, () -> {
    objeto.metodoQueDebeFallar(-1);
});

// Capturar y examinar la excepción
IllegalArgumentException ex = assertThrows(
    IllegalArgumentException.class,
    () -> objeto.metodoQueDebeFallar(-1)
);
assertEquals("El valor no puede ser negativo", ex.getMessage());
```

### Assertions Múltiples (assertAll)

Ejecuta todas las verificaciones aunque alguna falle:

```{code} java
:caption: assertAll para verificaciones múltiples

@Test
void verificarPersona() {
    Persona p = new Persona("Juan", "Pérez", 30);
    
    assertAll("Verificar datos de persona",
        () -> assertEquals("Juan", p.getNombre()),
        () -> assertEquals("Pérez", p.getApellido()),
        () -> assertEquals(30, p.getEdad()),
        () -> assertNotNull(p.getId())
    );
}
```

## Tests Parametrizados

Ejecutar el mismo test con diferentes datos:

```{code} java
:caption: Test parametrizado

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

class ValidadorTest {
    
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void numerosPositivosSonValidos(int numero) {
        assertTrue(Validador.esPositivo(numero));
    }
    
    @ParameterizedTest
    @CsvSource({
        "1, 1, 2",
        "2, 3, 5",
        "10, 20, 30",
        "-1, 1, 0"
    })
    void sumarDosNumeros(int a, int b, int esperado) {
        assertEquals(esperado, Calculadora.sumar(a, b));
    }
}
```

### Fuentes de Datos para Tests Parametrizados

:::{table} Fuentes de datos parametrizados
:label: tbl-param-sources

| Anotación | Descripción | Ejemplo |
| :-------- | :---------- | :------ |
| `@ValueSource` | Valores simples | `@ValueSource(ints = {1, 2, 3})` |
| `@CsvSource` | Valores CSV | `@CsvSource({"a,1", "b,2"})` |
| `@CsvFileSource` | Datos desde archivo CSV | `@CsvFileSource(resources = "/datos.csv")` |
| `@EnumSource` | Valores de un enum | `@EnumSource(DiaSemana.class)` |
| `@MethodSource` | Datos desde un método | `@MethodSource("proveerDatos")` |

:::

```{code} java
:caption: @MethodSource para datos complejos

@ParameterizedTest
@MethodSource("proveerCasosDeTest")
void testConDatosComplejos(String entrada, int esperado) {
    assertEquals(esperado, procesador.procesar(entrada));
}

static List<Arguments> proveerCasosDeTest() {
    List<Arguments> casos = new ArrayList<>();
    casos.add(Arguments.of("abc", 3));
    casos.add(Arguments.of("", 0));
    casos.add(Arguments.of("hello world", 11));
    return casos;
}
```

## Organización de Tests

### Convenciones de Nombres

```{code} java
:caption: Convenciones de nombres

// Nombre de clase: [ClaseBajoTest]Test
class CalculadoraTest { }

// Nombres de métodos: descriptivos del comportamiento
@Test
void sumar_dosNumerosPositivos_retornaSuma() { }

@Test
void dividir_divisorCero_lanzaExcepcion() { }

// O con @DisplayName para mayor legibilidad
@Test
@DisplayName("Sumar dos números positivos retorna la suma correcta")
void sumarPositivos() { }
```

### Estructura de Directorios

```
proyecto/
├── src/
│   └── main/
│       └── java/
│           └── ar/unrn/
│               └── Calculadora.java
└── src/
    └── test/
        └── java/
            └── ar/unrn/
                └── CalculadoraTest.java
```

### Tests Anidados

Agrupar tests relacionados:

```{code} java
:caption: Tests anidados con @Nested

class PilaTest {
    
    @Nested
    @DisplayName("Cuando la pila está vacía")
    class PilaVacia {
        
        private Pila<String> pila;
        
        @BeforeEach
        void crearPilaVacia() {
            pila = new Pila<>();
        }
        
        @Test
        @DisplayName("isEmpty retorna true")
        void isEmpty() {
            assertTrue(pila.isEmpty());
        }
        
        @Test
        @DisplayName("pop lanza excepción")
        void popLanzaExcepcion() {
            assertThrows(EmptyStackException.class, () -> pila.pop());
        }
    }
    
    @Nested
    @DisplayName("Cuando la pila tiene elementos")
    class PilaConElementos {
        
        private Pila<String> pila;
        
        @BeforeEach
        void crearPilaConElementos() {
            pila = new Pila<>();
            pila.push("primero");
            pila.push("segundo");
        }
        
        @Test
        @DisplayName("isEmpty retorna false")
        void noEstaVacia() {
            assertFalse(pila.isEmpty());
        }
        
        @Test
        @DisplayName("pop retorna el último elemento")
        void popRetornaUltimo() {
            assertEquals("segundo", pila.pop());
        }
    }
}
```

## Buenas Prácticas

### 1. Un Concepto por Test

```java
// ✗ Mal: prueba múltiples cosas
@Test
void testCalculadora() {
    assertEquals(5, calc.sumar(2, 3));
    assertEquals(6, calc.multiplicar(2, 3));
    assertThrows(Exception.class, () -> calc.dividir(1, 0));
}

// ✓ Bien: tests separados
@Test void sumar() { }
@Test void multiplicar() { }
@Test void dividirPorCero() { }
```

### 2. Tests Independientes

Los tests no deben depender del orden de ejecución ni de resultados de otros tests.

### 3. Nombres Descriptivos

El nombre del test debe indicar qué se prueba y qué se espera.

### 4. Evitar Lógica en Tests

```java
// ✗ Mal: lógica en el test
@Test
void test() {
    int esperado = 0;
    for (int i = 0; i < 5; i++) {
        esperado += i;
    }
    assertEquals(esperado, calc.sumarRango(0, 4));
}

// ✓ Bien: valor explícito
@Test
void sumarRango() {
    assertEquals(10, calc.sumarRango(0, 4));
}
```

### 5. Tests Rápidos

Los tests unitarios deben ejecutarse en milisegundos. Tests lentos desalientan su ejecución frecuente.

## Ejercicios

```{exercise}
:label: ej-test-1

Escribí tests para una clase `Validador` con el método `boolean esEmailValido(String email)`. Considerá casos como emails válidos, sin @, sin dominio, etc.
```

```{solution} ej-test-1
```java
class ValidadorTest {
    
    private Validador validador;
    
    @BeforeEach
    void setUp() {
        validador = new Validador();
    }
    
    @Test
    @DisplayName("Email válido retorna true")
    void emailValido() {
        assertTrue(validador.esEmailValido("usuario@ejemplo.com"));
    }
    
    @Test
    @DisplayName("Email sin @ retorna false")
    void emailSinArroba() {
        assertFalse(validador.esEmailValido("usuarioejemplo.com"));
    }
    
    @Test
    @DisplayName("Email sin dominio retorna false")
    void emailSinDominio() {
        assertFalse(validador.esEmailValido("usuario@"));
    }
    
    @Test
    @DisplayName("Email vacío retorna false")
    void emailVacio() {
        assertFalse(validador.esEmailValido(""));
    }
    
    @Test
    @DisplayName("Email null lanza excepción")
    void emailNull() {
        assertThrows(NullPointerException.class, 
            () -> validador.esEmailValido(null));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
        "test@test.com",
        "usuario.nombre@empresa.org",
        "a@b.co"
    })
    void emailsValidos(String email) {
        assertTrue(validador.esEmailValido(email));
    }
}
```
```

```{exercise}
:label: ej-test-2

Creá tests para una clase `Carrito` de compras con métodos `agregar(Producto)`, `eliminar(Producto)`, `total()` y `cantidadItems()`.
```

```{solution} ej-test-2
```java
class CarritoTest {
    
    private Carrito carrito;
    private Producto manzana;
    private Producto banana;
    
    @BeforeEach
    void setUp() {
        carrito = new Carrito();
        manzana = new Producto("Manzana", 1.50);
        banana = new Producto("Banana", 0.75);
    }
    
    @Test
    @DisplayName("Carrito nuevo está vacío")
    void carritoNuevoVacio() {
        assertEquals(0, carrito.cantidadItems());
        assertEquals(0.0, carrito.total(), 0.001);
    }
    
    @Test
    @DisplayName("Agregar producto incrementa cantidad")
    void agregarProducto() {
        carrito.agregar(manzana);
        assertEquals(1, carrito.cantidadItems());
    }
    
    @Test
    @DisplayName("Total suma precios correctamente")
    void calcularTotal() {
        carrito.agregar(manzana);
        carrito.agregar(banana);
        assertEquals(2.25, carrito.total(), 0.001);
    }
    
    @Test
    @DisplayName("Eliminar producto reduce cantidad")
    void eliminarProducto() {
        carrito.agregar(manzana);
        carrito.agregar(banana);
        carrito.eliminar(manzana);
        
        assertEquals(1, carrito.cantidadItems());
        assertEquals(0.75, carrito.total(), 0.001);
    }
    
    @Test
    @DisplayName("Eliminar de carrito vacío no falla")
    void eliminarDeCarritoVacio() {
        assertDoesNotThrow(() -> carrito.eliminar(manzana));
    }
}
```
```

:::{seealso}
- [Guía de usuario de JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- [Assertions de JUnit 5](https://junit.org/junit5/docs/current/api/org.junit.jupiter.api/org/junit/jupiter/api/Assertions.html)
:::
