---
title: "Testing con JUnit"
description: Estudio técnico sobre pruebas unitarias, desarrollo guiado por pruebas (TDD) y aseguramiento de la calidad de software.
---

# Testing con JUnit

En la ingeniería de software, el testing no es una fase posterior al desarrollo, sino una actividad integral del mismo. Las **pruebas unitarias** permiten validar el comportamiento de los componentes más pequeños de un sistema (clases y métodos) de forma aislada, asegurando que cada "engranaje" funcione antes de ensamblar la maquinaria completa.

## Fundamentos Teóricos del Testing

### ¿Por Qué Testeamos?

El testing de software responde a una realidad ineludible: **los programadores cometen errores**. Según estudios de ingeniería de software, un desarrollador introduce entre 15 y 50 defectos por cada 1000 líneas de código. El costo de corregir un bug crece exponencialmente según la fase en que se detecta:

:::{table} Costo relativo de corrección de defectos
:label: tbl-costo-bugs

| Fase de detección | Costo relativo |
| :--- | :---: |
| Durante codificación | 1x |
| En pruebas unitarias | 5x |
| En pruebas de integración | 10x |
| En pruebas de sistema | 20x |
| En producción | 50-100x |
:::

### La Pirámide de Testing

Mike Cohn propuso la **pirámide de testing** como modelo para distribuir los esfuerzos de prueba:

```
          /\
         /  \      E2E (End-to-End)
        /----\     Pocas, lentas, frágiles
       /      \
      /--------\   Integración
     /          \  Moderadas, verifican colaboración
    /------------\ 
   /              \ Unitarias
  /----------------\ Muchas, rápidas, aisladas
```

- **Unitarias (base):** Prueban una unidad aislada (método, clase). Son rápidas, determinísticas y la mayoría de los tests deberían estar aquí.
- **Integración (medio):** Verifican que múltiples componentes colaboren correctamente.
- **End-to-End (cima):** Simulan el uso real del sistema completo. Son lentas y frágiles.

:::{tip}
Una distribución saludable es aproximadamente 70% unitarias, 20% integración, 10% E2E. Invertir esta pirámide genera suites lentas y difíciles de mantener.
:::

### Verificación vs Validación

Es crucial distinguir estos conceptos:

- **Verificación:** "¿Estamos construyendo el producto correctamente?" — El código hace lo que el programador pretendía.
- **Validación:** "¿Estamos construyendo el producto correcto?" — El software satisface las necesidades del usuario.

Las pruebas unitarias se enfocan primariamente en **verificación**. La validación requiere pruebas de aceptación y retroalimentación del usuario.

### Taxonomía de Defectos

Comprender los tipos de errores ayuda a diseñar tests más efectivos:

- **Errores de especificación:** El código hace exactamente lo programado, pero la lógica es incorrecta.
- **Errores de implementación:** Bugs en la traducción del algoritmo a código (off-by-one, null pointers).
- **Errores de integración:** Incompatibilidades entre componentes que funcionan bien aislados.
- **Errores de regresión:** Funcionalidad que dejó de funcionar tras un cambio.

## Principios F.I.R.S.T.

Para que una suite de pruebas sea efectiva, debe cumplir con los principios F.I.R.S.T.:

1.  **Fast (Rápida)**: Los tests deben ejecutarse en milisegundos para que el desarrollador pueda correrlos constantemente.
2.  **Independent (Independiente)**: Ningún test debe depender del resultado o del estado dejado por otro. El orden de ejecución no debe importar.
3.  **Repeatable (Repetible)**: Deben dar el mismo resultado en cualquier entorno (máquina local, servidor de CI) y en cualquier momento.
4.  **Self-validating (Autovalidable)**: El test debe tener un resultado binario (pasa o falla). No debe requerir interpretación manual de logs.
5.  **Timely (Oportuno)**: Los tests deben escribirse idealmente antes o durante el desarrollo del código productivo.

### Aplicación Práctica de F.I.R.S.T.

```{code} java
:caption: Test que viola el principio de Independencia

class MalTest {
    static int contadorGlobal = 0;  // Estado compartido entre tests
    
    @Test
    void test1() {
        contadorGlobal++;
        assertEquals(1, contadorGlobal);  // Depende de ejecutarse primero
    }
    
    @Test
    void test2() {
        assertEquals(1, contadorGlobal);  // Falla si test1 no corrió antes
    }
}
```

```{code} java
:caption: Test que respeta F.I.R.S.T.

class BuenTest {
    private Contador contador;  // Instancia fresca por test
    
    @BeforeEach
    void setUp() {
        contador = new Contador();  // Aislamiento garantizado
    }
    
    @Test
    void incrementar_desdeZero_retornaUno() {
        contador.incrementar();
        assertEquals(1, contador.getValor());
    }
    
    @Test
    void getValor_sinIncrementos_retornaZero() {
        assertEquals(0, contador.getValor());  // Independiente del orden
    }
}
```

## Desarrollo Guiado por Pruebas (TDD)

El **Test-Driven Development** (TDD) es una técnica de diseño de software que invierte el proceso tradicional. Se basa en un ciclo corto y repetitivo:

1.  **RED**: Escribir un test que falle para una funcionalidad que aún no existe.
2.  **GREEN**: Escribir el código mínimo necesario para que el test pase.
3.  **REFACTOR**: Mejorar el código (limpieza, patrones, eficiencia) asegurando que el test siga pasando.

:::{important} TDD y Diseño
El TDD no es solo para encontrar bugs; es una herramienta de diseño. Escribir el test primero te obliga a pensar en la **interfaz** y el **uso** de tu clase antes que en su implementación, lo que suele derivar en un código más desacoplado y cohesivo.
:::

### Fundamentos Teóricos del TDD

Kent Beck, creador de TDD, lo describe como una técnica para "manejar el miedo durante la programación". Las premisas fundamentales son:

1. **Código no testeado es código roto:** Si no hay un test que verifique un comportamiento, no hay garantía de que funcione.
2. **Diseño emergente:** La estructura del código emerge de los requerimientos expresados como tests, no de diseños anticipados.
3. **Feedback inmediato:** El ciclo RED-GREEN-REFACTOR provee retroalimentación constante sobre el estado del sistema.

### Las Tres Leyes del TDD

Robert C. Martin formalizó las reglas de TDD:

1. **No escribirás código de producción** sin antes escribir un test que falle.
2. **No escribirás más de un test unitario** que sea suficiente para fallar (incluyendo errores de compilación).
3. **No escribirás más código de producción** del estrictamente necesario para pasar el test actual.

### Ejemplo Completo: TDD de una Calculadora

Veamos el ciclo TDD completo para implementar un método `dividir`:

**Iteración 1: RED**

```{code} java
:caption: Primer test (falla - el método no existe)

@Test
void dividir_seisEntreDos_retornaTres() {
    Calculadora calc = new Calculadora();
    assertEquals(3.0, calc.dividir(6, 2), 0.001);
}
// Error de compilación: dividir() no existe
```

**Iteración 1: GREEN**

```{code} java
:caption: Implementación mínima

public class Calculadora {
    public double dividir(double dividendo, double divisor) {
        return dividendo / divisor;  // Mínimo para pasar
    }
}
// Test pasa ✓
```

**Iteración 2: RED (caso de borde)**

```{code} java
:caption: Test para división por cero

@Test
void dividir_porZero_lanzaExcepcion() {
    Calculadora calc = new Calculadora();
    assertThrows(ArithmeticException.class, 
        () -> calc.dividir(5, 0));
}
// Test falla: retorna Infinity en lugar de lanzar excepción
```

**Iteración 2: GREEN**

```{code} java
:caption: Manejo de división por cero

public double dividir(double dividendo, double divisor) {
    if (divisor == 0) {
        throw new ArithmeticException("División por cero");
    }
    return dividendo / divisor;
}
// Test pasa ✓
```

**Iteración 2: REFACTOR**

```{code} java
:caption: Extracción de validación

public double dividir(double dividendo, double divisor) {
    validarDivisor(divisor);
    return dividendo / divisor;
}

private void validarDivisor(double divisor) {
    if (divisor == 0) {
        throw new ArithmeticException("División por cero");
    }
}
// Todos los tests siguen pasando ✓
```

### Patrones de TDD

Beck identificó patrones recurrentes en TDD:

:::{table} Patrones de TDD
:label: tbl-patrones-tdd

| Patrón | Descripción | Uso |
| :--- | :--- | :--- |
| **Fake It** | Retornar un valor hardcodeado para pasar el test | Inicio rápido del ciclo |
| **Obvious Implementation** | Si la implementación es trivial, escribirla directamente | Casos simples |
| **Triangulation** | Agregar tests hasta que la generalización sea obvia | Cuando no está claro cómo generalizar |
| **One to Many** | Empezar con un elemento, luego generalizar a colecciones | Algoritmos sobre listas |
:::

### Beneficios y Críticas del TDD

**Beneficios demostrados:**
- Código más modular (para ser testeable, debe ser desacoplado)
- Documentación ejecutable (los tests muestran cómo usar el código)
- Confianza para refactorizar (red de seguridad de tests)
- Menos bugs en producción (detección temprana)

**Críticas válidas:**
- Curva de aprendizaje significativa
- Puede ralentizar el desarrollo inicial
- No reemplaza otros tipos de testing
- Requiere disciplina para mantener los tests actualizados

## JUnit 5: Arquitectura y Fundamentos

JUnit es el framework de testing más utilizado en el ecosistema Java. La versión 5 (también llamada JUnit Jupiter) representa una reescritura completa con arquitectura modular.

### Arquitectura de JUnit 5

JUnit 5 se compone de tres módulos:

- **JUnit Platform:** Fundación para lanzar frameworks de testing. Define la API `TestEngine`.
- **JUnit Jupiter:** El nuevo modelo de programación y extensión para escribir tests.
- **JUnit Vintage:** Compatibilidad con tests escritos en JUnit 3 y 4.

### Anatomía de un Test JUnit

```{code} java
:caption: Estructura completa de una clase de test

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de la clase Calculadora")
class CalculadoraTest {

    private Calculadora calc;

    @BeforeAll
    static void setUpClass() {
        // Ejecuta UNA vez antes de todos los tests
        // Útil para recursos costosos (conexiones, archivos)
    }

    @BeforeEach
    void setUp() {
        // Ejecuta ANTES de cada test
        calc = new Calculadora();
    }

    @Test
    @DisplayName("Suma de positivos retorna resultado correcto")
    void sumar_positivoConPositivo_retornaSuma() {
        // Arrange (preparar)
        int a = 5, b = 3;
        
        // Act (actuar)
        int resultado = calc.sumar(a, b);
        
        // Assert (verificar)
        assertEquals(8, resultado);
    }

    @AfterEach
    void tearDown() {
        // Ejecuta DESPUÉS de cada test
        // Limpieza de estado
    }

    @AfterAll
    static void tearDownClass() {
        // Ejecuta UNA vez después de todos los tests
    }
}
```

### El Ciclo de Vida en Detalle

JUnit 5 maneja el ciclo de vida de las pruebas para asegurar la limpieza del estado entre ejecuciones:

- **`@BeforeEach` / `@AfterEach`**: Ideales para inicializar objetos o limpiar archivos temporales antes y después de cada método de prueba.
- **`@BeforeAll` / `@AfterAll`**: Para operaciones costosas (ej. levantar una base de datos de prueba) que se realizan una sola vez para toda la clase.

Por defecto, JUnit crea una **nueva instancia** de la clase de test para cada método `@Test`. Esto garantiza aislamiento pero significa que los campos de instancia se reinician.

```{code} java
:caption: Demostración del ciclo de vida

class CicloDeVidaTest {
    
    static int contadorInstancias = 0;
    int contadorLocal = 0;
    
    CicloDeVidaTest() {
        contadorInstancias++;
        System.out.println("Nueva instancia: " + contadorInstancias);
    }
    
    @Test
    void test1() {
        contadorLocal++;
        System.out.println("test1 - local: " + contadorLocal);  // Siempre 1
    }
    
    @Test
    void test2() {
        contadorLocal++;
        System.out.println("test2 - local: " + contadorLocal);  // Siempre 1
    }
}
// Salida: 2 instancias creadas, contadorLocal siempre es 1
```

### Anotaciones Avanzadas de JUnit 5

```{code} java
:caption: Anotaciones para control de ejecución

@Disabled("Bug #123 - pendiente de fix")
@Test
void testDeshabilitado() { }

@RepeatedTest(5)
void testRepetido(RepetitionInfo info) {
    System.out.println("Repetición " + info.getCurrentRepetition());
}

@ParameterizedTest
@ValueSource(ints = {1, 2, 3, 5, 8, 13})
void esFibonacci_numerosValidos_retornaTrue(int numero) {
    assertTrue(Fibonacci.es(numero));
}

@ParameterizedTest
@CsvSource({
    "1, 1, 2",
    "2, 3, 5",
    "10, 20, 30"
})
void sumar_variosValores_retornaSumaCorrecta(int a, int b, int esperado) {
    assertEquals(esperado, calc.sumar(a, b));
}

@Nested
@DisplayName("Tests de operaciones aritméticas")
class OperacionesAritmeticasTest {
    @Test
    void suma() { /* ... */ }
    
    @Test
    void resta() { /* ... */ }
}
```

## Estrategias de Prueba: Diseño de Casos de Test

### Técnicas de Caja Negra

Las pruebas de caja negra se diseñan sin conocer la implementación interna, basándose solo en la especificación:

#### Partición de Equivalencia

Dividir el dominio de entrada en clases que se comportan de manera equivalente. Solo es necesario probar un representante de cada clase.

```{code} java
:caption: Particiones para validar edad

// Dominio: edad para obtener licencia de conducir
// Clases de equivalencia:
//   - Inválidas: edad < 0
//   - Menores: 0 <= edad < 18
//   - Válidas: 18 <= edad <= 100
//   - Inválidas: edad > 100

@Test
void validarEdad_negativa_lanzaExcepcion() {
    assertThrows(IllegalArgumentException.class, 
        () -> Licencia.validarEdad(-5));
}

@Test
void validarEdad_menor_retornaFalse() {
    assertFalse(Licencia.validarEdad(15));
}

@Test
void validarEdad_adulto_retornaTrue() {
    assertTrue(Licencia.validarEdad(25));
}
```

#### Análisis de Valores Límite

Probar en las fronteras de las particiones, donde la mayoría de los bugs ocurren:

```{code} java
:caption: Tests de valores límite

// Límite: 18 años
@Test void validar_17_retornaFalse() { assertFalse(Licencia.validarEdad(17)); }
@Test void validar_18_retornaTrue()  { assertTrue(Licencia.validarEdad(18)); }
@Test void validar_19_retornaTrue()  { assertTrue(Licencia.validarEdad(19)); }

// Límite: 0
@Test void validar_menosUno_lanzaExcepcion() { 
    assertThrows(IllegalArgumentException.class, () -> Licencia.validarEdad(-1)); 
}
@Test void validar_cero_retornaFalse() { assertFalse(Licencia.validarEdad(0)); }
```

### Técnicas de Caja Blanca

Con acceso al código fuente, podemos diseñar tests que ejerciten caminos específicos:

#### Cobertura de Sentencias

Cada línea de código debe ejecutarse al menos una vez.

#### Cobertura de Ramas

Cada decisión (`if`, `switch`, `?:`) debe evaluarse tanto a verdadero como falso.

```{code} java
:caption: Código con múltiples ramas

public String clasificar(int valor) {
    if (valor < 0) {
        return "negativo";      // Rama 1
    } else if (valor == 0) {
        return "cero";          // Rama 2
    } else {
        return "positivo";      // Rama 3
    }
}

// Tests para 100% cobertura de ramas:
@Test void clasificar_negativo() { assertEquals("negativo", clasificar(-1)); }
@Test void clasificar_cero()     { assertEquals("cero", clasificar(0)); }
@Test void clasificar_positivo() { assertEquals("positivo", clasificar(1)); }
```

#### Cobertura de Condiciones

En condiciones compuestas, cada subcondición debe ser verdadera y falsa:

```{code} java
:caption: Cobertura de condición compuesta

public boolean esValido(int x, int y) {
    return x > 0 && y > 0;  // Dos subcondiciones
}

// Tests para cobertura de condiciones:
@Test void ambosPositivos()    { assertTrue(esValido(1, 1)); }   // T && T
@Test void xNegativo()         { assertFalse(esValido(-1, 1)); } // F && T
@Test void yNegativo()         { assertFalse(esValido(1, -1)); } // T && F
@Test void ambosNegativos()    { assertFalse(esValido(-1, -1)); }// F && F
```

### Casos Especiales a Considerar

:::{table} Casos especiales comunes
:label: tbl-casos-especiales

| Tipo de entrada | Casos a probar |
| :--- | :--- |
| Strings | `null`, vacío `""`, espacios, muy largo, caracteres especiales |
| Números | 0, 1, -1, MAX_VALUE, MIN_VALUE, NaN, Infinity |
| Colecciones | `null`, vacía, un elemento, muchos elementos, duplicados |
| Fechas | Bisiestos, fin de mes, cambio de año, zonas horarias |
| Archivos | No existe, vacío, sin permisos, en uso |
:::

## Aserciones en JUnit 5

Las aserciones son el mecanismo para verificar que el código bajo prueba produce los resultados esperados.

### Aserciones Básicas

```{code} java
:caption: Aserciones fundamentales

// Igualdad
assertEquals(esperado, actual);
assertEquals(3.14, resultado, 0.001);  // Con delta para doubles
assertNotEquals(valorNoEsperado, actual);

// Booleanos
assertTrue(condicion);
assertFalse(condicion);

// Nulidad
assertNull(objeto);
assertNotNull(objeto);

// Identidad (misma referencia)
assertSame(objetoEsperado, objetoActual);
assertNotSame(objeto1, objeto2);

// Arrays
assertArrayEquals(arrayEsperado, arrayActual);
```

### Aserciones Avanzadas

Además de `assertEquals`, JUnit 5 ofrece herramientas para escenarios complejos:

```{code} java
:caption: Aserciones avanzadas

// Verificar que un proceso termina en tiempo razonable
assertTimeout(Duration.ofMillis(100), () -> {
    algoritmoPesado.ejecutar();
});

// Aserciones agrupadas (no se detiene al primer fallo)
assertAll("Atributos de usuario",
    () -> assertEquals("admin", u.getRole()),
    () -> assertTrue(u.isActive())
);
```

### Testing de Excepciones

Una de las aserciones más importantes es verificar que el código lanza excepciones apropiadas:

```{code} java
:caption: Patrones para testear excepciones

// Forma básica: solo verifica el tipo
@Test
void dividirPorCero_lanzaArithmeticException() {
    assertThrows(ArithmeticException.class, 
        () -> calculadora.dividir(10, 0));
}

// Forma avanzada: capturar y verificar mensaje
@Test
void indiceInvalido_lanzaExcepcionConMensaje() {
    IndexOutOfBoundsException ex = assertThrows(
        IndexOutOfBoundsException.class,
        () -> lista.get(-1)
    );
    assertTrue(ex.getMessage().contains("Index"));
}

// Verificar que NO lanza excepción
@Test
void operacionValida_noLanzaExcepcion() {
    assertDoesNotThrow(() -> calculadora.dividir(10, 2));
}
```

### Mensajes de Fallo

Siempre es recomendable incluir mensajes descriptivos:

```{code} java
:caption: Aserciones con mensajes informativos

@Test
void verificarDescuento_clientePremium() {
    Cliente cliente = new Cliente(TipoCliente.PREMIUM);
    double descuento = cliente.calcularDescuento(100);
    
    assertEquals(20.0, descuento, 
        () -> "Cliente premium debería tener 20% de descuento, " +
              "pero obtuvo: " + descuento);
}
```

:::{tip}
El mensaje se pasa como `Supplier<String>` (lambda) para que solo se construya si el test falla, evitando overhead en tests que pasan.
:::

## Cobertura de Código (_Code Coverage_)

La cobertura es una métrica que indica qué porcentaje del código ha sido ejecutado por los tests.

### Tipos de Cobertura

- **Cobertura de Líneas:** ¿Se ejecutó esta línea?
- **Cobertura de Ramas (Branches):** En un `if`, ¿se probaron tanto el camino verdadero como el falso?
- **Cobertura de Métodos:** ¿Se invocó este método al menos una vez?
- **Cobertura de Condiciones:** ¿Cada subcondición booleana fue verdadera y falsa?
- **Cobertura de Caminos:** ¿Se ejercitaron todas las combinaciones posibles de ramas? (computacionalmente explosivo)

### Herramientas de Cobertura

En el ecosistema Java, las herramientas más utilizadas son:

- **JaCoCo:** Estándar de facto, integración con Gradle/Maven, reportes HTML
- **Cobertura:** Alternativa histórica
- **IntelliJ IDEA:** Cobertura integrada en el IDE

```{code} groovy
:caption: Configuración de JaCoCo en Gradle

plugins {
    id 'jacoco'
}

jacocoTestReport {
    reports {
        xml.required = true
        html.required = true
    }
}

test {
    finalizedBy jacocoTestReport
}

// Opcional: forzar mínimo de cobertura
jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = 0.80  // 80% mínimo
            }
        }
    }
}
```

:::{warning} El mito de la cobertura
Una cobertura del 100% no garantiza la ausencia de bugs. La cobertura indica qué código **no ha sido probado**, pero no garantiza que las pruebas existentes sean de calidad o cubran todos los casos de borde lógicos.
:::

### Interpretando los Reportes

Un reporte de JaCoCo típico muestra:

- **Verde:** Línea completamente cubierta
- **Amarillo:** Cobertura parcial (algunas ramas no ejecutadas)
- **Rojo:** Línea no ejecutada

:::{important}
Enfocate en aumentar la cobertura de **código crítico** (lógica de negocio, manejo de errores) antes que perseguir números absolutos. Un getter trivial sin cobertura es menos preocupante que un algoritmo de cálculo sin tests.
:::

## Organización y Nombrado de Tests

### Convenciones de Nombrado

Un buen nombre de test comunica:
- Qué se está probando
- Bajo qué condiciones
- Qué resultado se espera

```{code} java
:caption: Patrones de nombrado

// Patrón: metodo_condicion_resultadoEsperado
@Test void dividir_porCero_lanzaExcepcion() { }
@Test void isEmpty_listaVacia_retornaTrue() { }
@Test void depositar_montoNegativo_lanzaIllegalArgument() { }

// Patrón: should_resultado_when_condicion (BDD style)
@Test void should_throwException_when_dividingByZero() { }
@Test void should_returnTrue_when_listIsEmpty() { }
```

### Patrón AAA (Arrange-Act-Assert)

Estructura cada test en tres secciones claramente separadas:

```{code} java
:caption: Patrón Arrange-Act-Assert

@Test
void calcularTotal_conDescuento_aplicaDescuentoCorrectamente() {
    // Arrange (preparar el escenario)
    Carrito carrito = new Carrito();
    carrito.agregarProducto(new Producto("Laptop", 1000));
    Descuento descuento = new DescuentoPorcentual(10);
    
    // Act (ejecutar la acción bajo prueba)
    double total = carrito.calcularTotal(descuento);
    
    // Assert (verificar resultados)
    assertEquals(900.0, total, 0.01);
}
```

### Organización de Archivos

```
src/
├── main/java/
│   └── com/ejemplo/
│       ├── Calculadora.java
│       └── Usuario.java
└── test/java/
    └── com/ejemplo/
        ├── CalculadoraTest.java    // Misma estructura
        └── UsuarioTest.java
```

## Antipatrones de Testing

Evitá estos errores comunes:

### Tests Frágiles

Tests que fallan por cambios no relacionados con lo que prueban:

```{code} java
:caption: Test frágil (depende del orden de elementos)

// MAL: Falla si el orden de usuarios cambia
@Test
void buscarUsuarios_retornaListaCorrecta() {
    List<Usuario> usuarios = servicio.buscarTodos();
    assertEquals("Ana", usuarios.get(0).getNombre());  // Frágil
}

// BIEN: Verifica contenido, no orden
@Test
void buscarUsuarios_contieneUsuarioEsperado() {
    List<Usuario> usuarios = servicio.buscarTodos();
    assertTrue(usuarios.stream()
        .anyMatch(u -> u.getNombre().equals("Ana")));
}
```

### Tests que Prueban la Implementación

```{code} java
:caption: Test acoplado a la implementación

// MAL: Si cambio cómo calculo internamente, el test falla
@Test
void calcular_usaFormulaCuadratica() {
    // Verifica que se llamaron ciertos métodos internos
}

// BIEN: Verifica comportamiento observable
@Test
void calcular_valorPositivo_retornaResultadoCorrecto() {
    assertEquals(4.0, calculadora.raizCuadrada(16), 0.001);
}
```

### Tests sin Aserciones

```{code} java
:caption: Test vacío (falso positivo)

// MAL: Siempre pasa, no verifica nada
@Test
void procesarDatos() {
    servicio.procesar(datos);
    // ¿Y qué? No hay assert
}

// BIEN: Verifica el efecto
@Test
void procesarDatos_actualizaEstado() {
    servicio.procesar(datos);
    assertEquals(Estado.PROCESADO, datos.getEstado());
}
```

## Ejercicios de Aplicación

```exercise
:label: ej-tdd-stack
Aplicando TDD, describí los pasos para implementar un método `pop()` en una clase `Pila`.
```

```solution
:for: ej-tdd-stack
1. **Red**: Escribo un test que cree una pila, inserte un elemento y verifique que `pop()` devuelva ese elemento. El test no compila o falla porque `pop()` no existe o retorna null.
2. **Green**: Implemento `pop()` de la forma más simple posible (ej. retornando el valor del tope y decrementando el índice). El test pasa.
3. **Red**: Escribo un test que verifique que llamar a `pop()` en una pila vacía lanza `EmptyStackException`. El test falla.
4. **Green**: Agrego el chequeo `if (isEmpty()) throw ...` en `pop()`. El test pasa.
5. **Refactor**: Reviso si puedo extraer lógica común a `isEmpty()` o mejorar la gestión del arreglo.
```

```exercise
:label: ej-valores-limite
Dado un método `esAdulto(int edad)` que retorna `true` si `edad >= 18` y `false` en caso contrario (asumiendo edades válidas de 0 a 120), listá todos los valores límite que deberían testearse.
```

```solution
:for: ej-valores-limite
:class: dropdown

Los valores límite a probar son:

**Límite inferior del dominio (0):**
- `edad = -1` → debería lanzar excepción o retornar false
- `edad = 0` → retorna false

**Límite de mayoría de edad (18):**
- `edad = 17` → retorna false
- `edad = 18` → retorna true
- `edad = 19` → retorna true

**Límite superior del dominio (120):**
- `edad = 119` → retorna true
- `edad = 120` → retorna true
- `edad = 121` → debería lanzar excepción o retornar false

Total: 8 casos de prueba para cobertura completa de valores límite.
```

```exercise
:label: ej-tdd-email
Usando TDD, describí el proceso para implementar un método `esEmailValido(String email)` que valide direcciones de correo electrónico. Incluí al menos 3 iteraciones del ciclo RED-GREEN-REFACTOR.
```

```solution
:for: ej-tdd-email
:class: dropdown

**Iteración 1: Caso básico**
- RED: `@Test void email_valido_retornaTrue()` → `assertTrue(esEmailValido("user@domain.com"))` falla
- GREEN: `return email.contains("@");` → pasa
- REFACTOR: Ninguno necesario aún

**Iteración 2: Email sin arroba**
- RED: `@Test void email_sinArroba_retornaFalse()` → `assertFalse(esEmailValido("invalido"))` ya pasa por la implementación anterior
- Agregar: `@Test void email_sinDominio_retornaFalse()` → `assertFalse(esEmailValido("user@"))` falla
- GREEN: `return email.contains("@") && email.indexOf("@") < email.length() - 1;`
- REFACTOR: Extraer validación a método privado

**Iteración 3: Email nulo**
- RED: `@Test void email_null_retornaFalse()` → `assertFalse(esEmailValido(null))` lanza NullPointerException
- GREEN: Agregar `if (email == null) return false;` al inicio
- REFACTOR: Considerar usar `Optional` o validación más robusta

**Iteración 4: Email con múltiples arrobas**
- RED: `assertFalse(esEmailValido("a@b@c.com"))` → falla (retorna true)
- GREEN: Verificar que solo hay un "@": `email.indexOf("@") == email.lastIndexOf("@")`
- REFACTOR: Consolidar validaciones en un método más legible
```

```exercise
:label: ej-particion-equivalencia
Para un método `calcularTarifa(int edad, boolean esEstudiante)` que calcula tarifas de transporte, identificá las clases de equivalencia y diseñá un conjunto mínimo de tests.
```

```solution
:for: ej-particion-equivalencia
:class: dropdown

**Clases de equivalencia identificadas:**

Por edad:
1. Inválida: `edad < 0`
2. Niños (gratis): `0 <= edad < 6`
3. Menores (50%): `6 <= edad < 18`
4. Adultos (100%): `18 <= edad < 65`
5. Jubilados (gratis): `edad >= 65`

Por condición de estudiante:
1. Es estudiante (25% descuento adicional)
2. No es estudiante

**Tests mínimos (un representante por clase):**

```java
@Test void tarifa_edadNegativa_lanzaExcepcion() { }
@Test void tarifa_ninoCuatroAnios_retornaCero() { }
@Test void tarifa_menorDoceAnios_retornaMitad() { }
@Test void tarifa_menorDoceEstudiante_retornaMitadMenos25() { }
@Test void tarifa_adultoTreinta_retornaCompleta() { }
@Test void tarifa_adultoEstudiante_retornaCompletaMenos25() { }
@Test void tarifa_jubilado_retornaCero() { }
```

Total: 7 tests cubren todas las combinaciones relevantes.
```

## Referencias Bibliográficas

Para profundizar en testing y TDD:

- **Beck, K.** (2003). _Test Driven Development: By Example_. Addison-Wesley. (Libro fundacional sobre TDD).
- **Martin, R. C.** (2009). _Clean Code_. Prentice Hall. (Capítulo 9: Unit Tests).
- **Liang, Y. D.** (2017). _Introduction to Java Programming and Data Structures_ (11va ed.). Pearson.
- **Meszaros, G.** (2007). _xUnit Test Patterns: Refactoring Test Code_. Addison-Wesley.
- **Freeman, S. & Pryce, N.** (2009). _Growing Object-Oriented Software, Guided by Tests_. Addison-Wesley. (TDD avanzado con mocks).
- **Koskela, L.** (2013). _Effective Unit Testing: A Guide for Java Developers_. Manning.

### Recursos Online

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/) — Documentación oficial
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/) — Cobertura de código
- [Baeldung Testing Tutorials](https://www.baeldung.com/junit-5) — Tutoriales prácticos

:::seealso

- {ref}`regla-0x4001` - Estándares de testing de la cátedra.
- {ref}`regla-0x3001` - Testeo de excepciones.
  :::
