---
title: Testing con JUnit 5
description: Guía práctica sobre implementación de tests unitarios con JUnit 5, configuración, sintaxis, anotaciones y buenas prácticas.
label: junit
---

(testing-con-junit-5)=
# Testing con JUnit 5

:::{note} Observación I
Este apunte se enfoca en la **implementación práctica** con JUnit 5. Para los fundamentos teóricos del testing (TDD, pirámide, estrategias, FIRST), consultá {ref}`testing`.
:::

:::{note} Observación II
Este apunte trata _todo_ lo referido testing de Java con jUnit, por lo que van a ver un montón de cosas que están, técnicamente, fuera de lugar en el cronograma de la materia.
La idea es que este sea una referencia del tema, por lo que tendrán que volver eventualmente y saltear aquello que no les suene.
:::

(del-testing-manual-en-c-al-automatizado-en-java)=
## Del Testing Manual en C al Automatizado en Java

Si venís de programar en C, probablemente verificabas que tu código funcionara ejecutándolo manualmente e inspeccionando la salida con `printf`:

```c
int main() {
    printf("factorial(0) = %d (esperado: 1)\n", factorial(0));
    printf("factorial(5) = %d (esperado: 120)\n", factorial(5));
    return 0;
}
```

**JUnit automatiza esta verificación**:

```java
@Test
void factorial_conCero_retornaUno() {
    assertEquals(1, Matematica.factorial(0));  // JUnit verifica automáticamente
}
```

Si el resultado no es 1, JUnit falla y te muestra exactamente qué salió mal. Además, podés ejecutar cientos de tests con un solo comando.

(junit-framework-de-testing-para-java)=
## JUnit: Framework de Testing para Java

Un **framework de testing** es una librería que proporciona herramientas para escribir y ejecutar tests de forma organizada. En C no hay un framework estándar (existen librerías como CUnit, Unity, etc., pero no son parte del lenguaje). En Java, **JUnit** es el estándar de facto.

JUnit te da:
- **Anotaciones** para marcar métodos como tests (`\@Test`)
- **Métodos de verificación** (assertions) para comparar resultados esperados con reales
- **Un ejecutor** que encuentra y ejecuta todos los tests, reportando cuáles pasan y cuáles fallan
- **Hooks** para setup y cleanup antes/después de cada test

(historia-breve)=
### Historia breve

- **JUnit 3** (2000): Primera versión ampliamente adoptada. Los tests se identificaban por herencia de clases y nombres de métodos.
- **JUnit 4** (2006): Introdujo anotaciones (`\@Test`, `\@Before`, etc.), simplificando enormemente la escritura de tests.
- **JUnit 5** (2017): Reescritura completa con arquitectura modular, nuevas features, y soporte para Java 8+.

:::{tip}
Utilizaremos **JUnit 5** en toda la cátedra. Si encontrás ejemplos de JUnit 4 en internet (muy comunes en Stack Overflow), tené en cuenta que algunas anotaciones cambiaron: `\@Before` → `\@BeforeEach`, `\@BeforeClass` → `\@BeforeAll`, etc.
:::

(configuracion-del-proyecto)=
### Configuración del proyecto

Para usar JUnit 5 en un proyecto Gradle, agregá la siguiente dependencia al `build.gradle`. _En las prácticas de la cátedra esto ya está configurado._

```gradle
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
}

test {
    useJUnitPlatform()
}
```

**Explicación línea por línea:**

- `testImplementation`: Esta dependencia solo se usa para testing, no se incluye en el código de producción. Es como decir "necesito esta librería solo para mis tests".

- `'org.junit.jupiter:junit-jupiter:5.10.0'`: Coordenadas Maven de JUnit 5. El formato es `grupo:artefacto:versión`.

- `useJUnitPlatform()`: Le dice a Gradle que use el motor de JUnit 5 para ejecutar tests. Sin esta línea, Gradle no encontraría los tests.

(estructura-de-un-proyecto-con-tests)=
### Estructura de un proyecto con tests

Gradle utiliza la estructura estándar de Maven para separar código de producción y tests. Es una convención importante:

```
mi-proyecto/
├── build.gradle              ← Configuración de Gradle
├── settings.gradle           ← Nombre del proyecto
└── src/
    ├── main/
    │   └── java/             ← Código de producción
    │       └── ar/
    │           └── unrn/
    │               └── poo/
    │                   └── Calculadora.java
    └── test/
        └── java/             ← Código de tests
            └── ar/
                └── unrn/
                    └── poo/
                        └── CalculadoraTest.java
```

**Puntos clave:**

- El código de producción va en `src/main/java`
- Los tests van en `src/test/java`
- La estructura de paquetes **debe ser idéntica** entre producción y test

:::{important}
El código de test debe estar en `src/test/java` con la **misma estructura de paquetes** que el código de producción. Esto no es solo convención: permite que los tests accedan a miembros con visibilidad de paquete (sin modificador `public`, `private`, ni `protected`).

```java
// En src/main/java/ar/unrn/poo/Calculadora.java
package ar.unrn.poo;

class Calculadora {  // Sin public → visibilidad de paquete
    static int sumar(int a, int b) { return a + b; }
}

// En src/test/java/ar/unrn/poo/CalculadoraTest.java
package ar.unrn.poo;  // Mismo paquete → puede acceder a Calculadora

class CalculadoraTest {
    @Test
    void testSumar() {
        assertEquals(5, Calculadora.sumar(2, 3));  // Funciona!
    }
}
```
:::

(primer-test-con-junit)=
## Primer Test con JUnit

Veamos un ejemplo completo para entender la estructura básica de un test. Es como el "Hello World" del testing.

(codigo-de-produccion)=
### Código de producción

Supongamos que tenemos una clase `Calculadora` con métodos estáticos. Usamos métodos estáticos por ahora porque aún no vimos programación orientada a objetos en profundidad:

```java
package ar.unrn.poo;

/**
 * Calculadora simple con operaciones básicas.
  */
public class CalculadoraApp {

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

En C, esto sería equivalente a:

```c
// calculadora.h
int sumar(int a, int b);
double dividir(double dividendo, double divisor);

// calculadora.c
int sumar(int a, int b) {
    return a + b;
}

double dividir(double dividendo, double divisor) {
    if (divisor == 0) {
        // En C no hay excepciones, habría que manejar el error de otra forma
        fprintf(stderr, "Error: división por cero\n");
        exit(1);
    }
    return dividendo / divisor;
}
```

(clase-de-test)=
### Clase de test

Ahora creamos la clase de test correspondiente. Aquí aplica la {ref}`regla-0x4000`: el test debe tener el mismo nombre que la clase bajo prueba con `Test` al final.

```java
package ar.unrn.poo;

import org.junit.jupiter.api.Test;           // Anotación para marcar tests
import static org.junit.jupiter.api.Assertions.*;  // Métodos de verificación

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

Analicemos cada parte:

(anatomia-de-un-test)=
### Anatomía de un test

Analicemos los componentes del test anterior en detalle:

#### 1. Imports necesarios

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
```

- `org.junit.jupiter.api.Test`: La anotación `\@Test` que marca métodos como tests.
- `static org.junit.jupiter.api.Assertions.*`: Importa **estáticamente** todos los métodos de verificación. El `static` permite escribir `assertEquals(...)` en lugar de `Assertions.assertEquals(...)`. El `*` importa todos los métodos (`assertEquals`, `assertTrue`, `assertNull`, etc.).

#### 2. Anotación `\@Test`

```java
@Test
void testSumar_ConDosNumerosPositivos_RetornaSuma() {
```

La anotación `\@Test` le dice a JUnit: "este método es un test, ejecutalo cuando corras los tests". Sin esta anotación, el método sería un método normal que JUnit ignoraría.

**Características de los métodos de test:**

- Deben ser `void` (no retornan valor, solo verifican condiciones)
- Pueden tener cualquier nivel de visibilidad, pero se recomienda package-private (sin modificador) o `public`
- El nombre debe ser descriptivo (ver {ref}`regla-0x4003`)
- **No deben tener parámetros** (excepto en tests parametrizados, que veremos después)

#### 3. Estructura AAA (Arrange-Act-Assert)

Aquí aplica la {ref}`regla-0x4001`: todo test debe seguir la estructura AAA. Esta es una convención universal en testing que hace los tests claros y consistentes.

```java
// Arrange: Preparar
int operando1 = 5;
int operando2 = 3;

// Act: Ejecutar
int resultado = Calculadora.sumar(operando1, operando2);

// Assert: Verificar
assertEquals(8, resultado, "5 + 3 debe ser 8");
```

**Arrange (Preparar):** Todo lo necesario antes de ejecutar el código bajo prueba.
- Crear datos de entrada
- Configurar valores necesarios
- Preparar el escenario de prueba

En C sería como declarar e inicializar las variables antes de llamar a la función.

**Act (Ejecutar):** La acción que querés testear.
- Invocar el método bajo prueba
- Aquí aplica la {ref}`regla-0x4002`: una sola llamada por test (con algunas excepciones que veremos)

**Assert (Verificar):** Comprobar que el resultado es el esperado.
- Usar métodos de `Assertions` para verificar condiciones
- Si alguna verificación falla, el test falla

#### 4. Assertions (Verificaciones)

Los **assertions** son métodos que verifican condiciones. Si la condición es verdadera, el assertion "pasa" silenciosamente. Si es falsa, lanza una excepción que JUnit captura y reporta como test fallido.

```java
assertEquals(8, resultado, "5 + 3 debe ser 8");
//           ^esperado  ^actual  ^mensaje opcional
```

**Orden de parámetros:** El primer parámetro es el valor **esperado**, el segundo es el valor **actual** (el resultado del código bajo prueba). Esto es importante para los mensajes de error: JUnit dirá "expected 8 but was 10" si lo escribís correctamente.

El mensaje opcional se muestra solo si el test falla, ayudando a entender qué salió mal.

JUnit provee múltiples métodos de assertion que veremos en detalle más adelante.

(ejecutar-los-tests)=
### Ejecutar los tests

#### Desde el IDE

La mayoría de los IDEs modernos (IntelliJ IDEA, Eclipse, VS Code con extensiones) permiten ejecutar tests con un clic:

- **Ejecutar todos los tests de una clase**: Click derecho sobre la clase → "Run Tests" o "Run 'CalculadoraTest'"
- **Ejecutar un test individual**: Click en el ícono verde (▶) junto al método, o click derecho sobre el método → "Run"
- **Ejecutar todos los tests del proyecto**: En el menú Run, o con atajos de teclado (varían según IDE)

El IDE mostrará una vista con los resultados: tests verdes (✅) pasaron, rojos (❌) fallaron.

#### Desde Gradle (línea de comandos)

Es importante saber ejecutar tests desde la terminal, especialmente para integración continua (CI) o cuando el IDE tiene problemas.

```bash
(ejecutar-todos-los-tests-del-proyecto)=
# Ejecutar todos los tests del proyecto
gradle test

(usando-el-wrapper-recomendado-garantiza-misma-version-de-gradle-para-todos)=
# Usando el wrapper (recomendado, garantiza misma versión de Gradle para todos)
./gradlew test

(ejecutar-tests-de-una-clase-especifica)=
# Ejecutar tests de una clase específica
./gradlew test --tests CalculadoraTest

(ejecutar-un-metodo-de-test-especifico)=
# Ejecutar un método de test específico
./gradlew test --tests CalculadoraTest.testSumar_ConDosNumerosPositivos_RetornaSuma

(ejecutar-tests-que-coincidan-con-un-patron)=
# Ejecutar tests que coincidan con un patrón
./gradlew test --tests "*Calculadora*"  # Todos los tests cuyo nombre contenga "Calculadora"
```

:::{tip}
El **Gradle Wrapper** (`./gradlew` en Unix/Mac, `gradlew.bat` en Windows) es la forma recomendada de ejecutar Gradle. Es un script que descarga automáticamente la versión correcta de Gradle para el proyecto, garantizando que todos los desarrolladores usen exactamente la misma versión.
:::

(interpretacion-de-resultados)=
### Interpretación de resultados

Cuando ejecutás los tests, JUnit muestra un reporte con el estado de cada test:

```
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

**Significado de cada campo:**

- **Tests run**: Total de tests que se ejecutaron
- **Failures**: Tests que fallaron porque un assertion fue falso (el código hizo algo diferente a lo esperado)
- **Errors**: Tests que fallaron por una excepción inesperada (algo se rompió antes de llegar al assertion)
- **Skipped**: Tests que se omitieron (marcados con `@Disabled` o que no cumplieron alguna condición)

**La diferencia entre Failure y Error:**

```java
@Test
void testEjemploFailure() {
    int resultado = Calculadora.sumar(2, 3);
    assertEquals(10, resultado);  // FAILURE: esperaba 10, obtuvo 5
}

@Test
void testEjemploError() {
    int[] numeros = null;
    int resultado = numeros[0];  // ERROR: NullPointerException antes del assert
}
```

:::{tip}
Un test **pasa** (✅) si todas las assertions son verdaderas y no se lanzan excepciones inesperadas.

Un test **falla** (❌) por una de estas razones:
1. Alguna assertion es falsa (Failure)
2. Se lanza una excepción que no era esperada (Error)
3. Se esperaba una excepción que no se lanzó (Failure)
:::

(assertions-verificaciones-en-junit)=
## Assertions: Verificaciones en JUnit

JUnit 5 provee una rica colección de métodos de assertion en la clase `org.junit.jupiter.api.Assertions`. Son las herramientas para verificar que el código hace lo esperado.

En C, la verificación típicamente era manual con `if` y `printf`:

```c
void test_sumar() {
    int resultado = sumar(2, 3);
    if (resultado != 5) {
        printf("ERROR: esperaba 5, obtuve %d\n", resultado);
    }
}
```

En JUnit, los assertions hacen esto de forma más elegante y con mejor información de error:

```java
@Test
void testSumar() {
    int resultado = Calculadora.sumar(2, 3);
    assertEquals(5, resultado);  // Si falla, JUnit reporta: "expected 5 but was X"
}
```

(assertions-basicos)=
### Assertions básicos

#### `assertEquals` y `assertNotEquals`

Verifican igualdad de valores. Son los assertions más usados.

```java
@Test
void testEquals() {
    // Arrange
    int a = 2;
    int b = 3;

    // Act
    int resultado = Calculadora.sumar(a, b);

    // Assert
    assertEquals(5, resultado);                    // Pasa si resultado == 5
    assertNotEquals(6, resultado);                 // Pasa si resultado != 6
}
```

**Para números de punto flotante**, debés especificar un **delta de tolerancia**. Esto es igual que en C: los `float` y `double` tienen errores de precisión inherentes a la representación en punto flotante.

```java
@Test
void testDividir_ConDosNumeros_RetornaCociente() {
    // Act
    double resultado = Calculadora.dividir(10.0, 3.0);

    // Assert: 10/3 = 3.333... pero con precisión limitada
    assertEquals(3.333, resultado, 0.001);  // Tolerancia de ±0.001
}
```

:::{important}
**Siempre usá un delta al comparar `double` o `float`**, debido a imprecisiones de punto flotante. Este problema existe en todos los lenguajes, incluyendo C:

```java
// ❌ Incorrecto - puede fallar por imprecisión
assertEquals(0.3, 0.1 + 0.2);
// 0.1 + 0.2 en punto flotante es 0.30000000000000004, no 0.3

// ✅ Correcto - usa delta
assertEquals(0.3, 0.1 + 0.2, 0.000001);
```

En C tendrías el mismo problema:
```c
double resultado = 0.1 + 0.2;
if (resultado == 0.3) { ... }  // ¡Puede ser falso!
if (fabs(resultado - 0.3) < 0.000001) { ... }  // Correcto
```
:::

#### `assertTrue` y `assertFalse`

Verifican condiciones booleanas. Son útiles cuando tenés un método que retorna `boolean` o querés verificar una condición compleja.

```java
@Test
void testValidador() {
    // Act & Assert combinados para simplificar
    assertTrue(ValidadorEmail.esValido("usuario@ejemplo.com"));
    assertFalse(ValidadorEmail.esValido("sin-arroba"));
}
```

También podés usar `assertTrue` para condiciones más complejas, aunque a veces `assertEquals` da mejores mensajes de error:

```java
@Test
void testNumeroEnRango() {
    int numero = Generador.generarNumero();
    
    // Opción 1: assertTrue (mensaje de error menos informativo)
    assertTrue(numero >= 0 && numero <= 100, 
               "Número debe estar entre 0 y 100");
    
    // Opción 2: múltiples assertions (más explícito)
    assertTrue(numero >= 0, "Número no debe ser negativo");
    assertTrue(numero <= 100, "Número no debe exceder 100");
}
```

#### `assertNull` y `assertNotNull`

Verifican si un valor es `null`. En Java (a diferencia de C), `null` es un valor especial que indica "ausencia de referencia".

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
    // Opcionalmente, también verificar el contenido
    assertEquals("valor esperado", resultado);
}
```

En C, el equivalente sería verificar si un puntero es `NULL`:

```c
char* resultado = buscar_por_clave("inexistente");
if (resultado == NULL) { ... }  // Similar a assertNull
```

(assertions-para-colecciones-y-arrays)=
### Assertions para colecciones y arrays

#### `assertArrayEquals`

Verifica que dos arrays tienen los mismos elementos en el mismo orden. Esto es especialmente útil porque en Java (y en C), comparar arrays directamente con `==` compara referencias, no contenido.

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

**¿Por qué no usar `assertEquals` para arrays?**

```java
int[] a = {1, 2, 3};
int[] b = {1, 2, 3};

// ❌ Esto falla aunque tengan el mismo contenido
assertEquals(a, b);  // Compara referencias, son objetos diferentes

// ✅ Esto funciona correctamente
assertArrayEquals(a, b);  // Compara elemento por elemento
```

Esto es similar al problema en C donde `array1 == array2` compara direcciones de memoria, no contenido.

#### Assertions de colecciones (con métodos helper)

JUnit 5 no tiene assertions específicas para colecciones (`List`, `Set`, etc.) en `Assertions`, pero podés combinar assertions básicos:

```java
@Test
void testFiltrar_ConListaDeNumeros_RetornaListaFiltrada() {
    // Arrange
    int[] numeros = {1, 2, 3, 4, 5, 6};

    // Act
    int[] pares = Filtrador.filtrarPares(numeros);

    // Assert: Verificar múltiples aspectos
    assertNotNull(pares);                          // No es null
    assertEquals(3, pares.length);                 // Tiene 3 elementos
    assertEquals(2, pares[0]);                     // Primer par es 2
    assertArrayEquals(new int[]{2, 4, 6}, pares);  // Contenido completo
}
```

(verificacion-de-excepciones)=
### Verificación de excepciones

Una funcionalidad crucial es verificar que el código lanza excepciones en situaciones de error. Las excepciones son parte del **contrato** de un método: si la documentación dice "lanza `ArithmeticException` si el divisor es cero", debemos testear que eso realmente sucede.

En C no hay excepciones nativas, así que el manejo de errores suele ser por valores de retorno:

```c
// En C, típicamente retornás un código de error o valor especial
int dividir(int a, int b, int* resultado) {
    if (b == 0) return -1;  // Código de error
    *resultado = a / b;
    return 0;  // Éxito
}
```

En Java, usamos excepciones:

```java
public static double dividir(double a, double b) {
    if (b == 0) {
        throw new ArithmeticException("No se puede dividir por cero");
    }
    return a / b;
}
```

#### Verificar que se lanza excepción con try-catch

La forma clásica (y que funciona en cualquier versión de Java) es usar `try-catch` con `fail()`:

```java
@Test
void testDividir_ConDivisorCero_LanzaArithmeticException() {
    // Act & Assert
    try {
        Calculadora.dividir(10, 0);
        fail("Se esperaba ArithmeticException");  // Si llegamos aquí, el test falla
    } catch (ArithmeticException e) {
        // Test pasa - se lanzó la excepción esperada
    }
}
```

**Explicación paso a paso:**

1. Llamamos al método que debería lanzar la excepción
2. Si **no** lanza excepción, llegamos a `fail()`, que hace fallar el test
3. Si **sí** lanza `ArithmeticException`, el `catch` la atrapa y el test pasa
4. Si lanza **otra** excepción (ej: `NullPointerException`), el catch no la atrapa y el test falla como "Error"

#### Verificar mensaje de excepción

Podés capturar la excepción y verificar su mensaje:

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
Verificar excepciones es tan importante como verificar resultados correctos. Las excepciones son parte del contrato de un método y documentan cómo el código maneja situaciones de error.
:::

#### Verificar que NO se lanza excepción

Cuando querés verificar que un método NO lanza excepciones para entradas válidas, simplemente llamalo. Si lanza una excepción, JUnit la captura y el test falla como "Error":

```java
@Test
void testDividir_ConDivisorNoNulo_NoLanzaExcepcion() {
    // Act: Si esto lanza una excepción, el test falla automáticamente
    double resultado = Calculadora.dividir(10, 2);

    // Assert: Verificar el resultado correcto
    assertEquals(5.0, resultado, 0.01);
}
```

(assertions-compuestos)=
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

Utilicen esta estrategia solo para verificar diferentes aspectos de un único resultado.

(mensajes-de-falla-descriptivos)=
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

(ciclo-de-vida-de-los-tests)=
## Ciclo de Vida de los Tests

JUnit ofrece anotaciones para ejecutar código en diferentes momentos del ciclo de vida de los tests. Aunque al testear métodos estáticos no es estrictamente necesario preparar estado, estas anotaciones son útiles para preparar datos de entrada comunes o manejar recursos como archivos temporales.

(anotaciones-de-ciclo-de-vida)=
### Anotaciones de ciclo de vida

#### `\@BeforeEach`

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

#### `\@BeforeAll`

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
Tené cuidado con `\@BeforeAll`: si los datos compartidos son modificables, podés violar la {ref}`regla-0x4005` (independencia de tests). Usalo solo para datos inmutables o de solo lectura.
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

(orden-completo-de-ejecucion)=
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

(ejemplo-completo-del-ciclo-de-vida)=
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

(convenciones-de-nombrado)=
## Convenciones de Nombrado

El nombrado correcto de tests es fundamental para la mantenibilidad. Aquí aplica la {ref}`regla-0x4003`.

(estructura-del-nombre)=
### Estructura del nombre

```
test<MetodoAProbar>_<CondicionOContexto>_<ResultadoEsperado>
```

**Componentes:**

1. **Prefijo `test`**: Identifica claramente que es un método de test
2. **Método a probar**: Nombre del método que se está testeando
3. **Condición**: Bajo qué circunstancias o con qué datos
4. **Resultado esperado**: Qué debe suceder

(ejemplos-practicos)=
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

(patrones-de-nombres-utiles)=
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

(nombres-descriptivos-vs-concisos)=
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

(reglas-de-estilo-para-testing)=
## Reglas de Estilo para Testing

Las reglas de testing son fundamentales para mantener una suite de tests robusta y mantenible. Repasemos las reglas clave.

(regla-0x4000-nomenclatura-de-la-clase-de-test)=
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

(regla-0x4001-estructura-aaa)=
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

(regla-0x4002-una-llamada-por-test)=
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

(regla-0x4003-nombres-descriptivos)=
### Regla 0x4003: Nombres descriptivos

{ref}`regla-0x4003` ya la vimos en la sección de convenciones de nombrado. La convención es:

```
test<Accion>_<Condicion>_<ResultadoEsperado>
```

(regla-0x4004-sin-logica-condicional)=
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

(regla-0x4005-tests-independientes)=
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

1. Usar `\@BeforeEach` para preparar datos comunes
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

(tests-parametrizados)=
## Tests Parametrizados

A menudo queremos probar el mismo comportamiento con múltiples conjuntos de datos. Los **tests parametrizados** permiten esto sin violar la {ref}`regla-0x4004` (sin lógica condicional).

(anotacion-parameterizedtest)=
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

Este test se ejecuta 5 veces, una por cada valor en `\@ValueSource`.

(fuentes-de-parametros)=
### Fuentes de parámetros

#### `\@ValueSource`

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

#### `\@CsvSource`

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

Para casos con múltiples valores, podés usar `\@CsvSource`:

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

#### `\@CsvFileSource`

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

(nombres-personalizados-para-tests-parametrizados)=
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

(tests-de-excepciones)=
## Tests de Excepciones

Las excepciones son parte integral del contrato de un método. Debemos testearlas con el mismo rigor que los casos exitosos.

(verificar-que-se-lanza-excepcion)=
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

(verificar-mensaje-de-excepcion)=
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

(verificar-causa-de-excepcion)=
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

(multiples-verificaciones-sobre-excepcion)=
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

(tests-con-try-catch)=
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

(casos-de-prueba-completos)=
## Casos de Prueba Completos

Veamos ejemplos completos que integran todos los conceptos vistos, centrados en métodos estáticos.

(ejemplo-1-test-de-funciones-matematicas)=
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

(ejemplo-2-test-de-validador-de-emails)=
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

(ejemplo-3-test-de-utilidades-de-strings)=
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

(ejemplo-4-test-de-procesamiento-de-arreglos)=
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

(organizacion-de-tests)=
## Organización de Tests

(tests-por-funcionalidad)=
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

(clases-anidadas-con-nested)=
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
- Cada grupo puede tener su propio `\@BeforeEach` para preparar datos
- Mejora la organización en tests grandes

(cobertura-de-tests)=
## Cobertura de Tests

La **cobertura de tests** (test coverage) mide qué porcentaje del código es ejecutado por los tests.

(08-junit-tipos-de-cobertura)=
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

(herramientas-de-cobertura)=
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

(interpretacion-de-cobertura)=
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

(08-junit-meta-razonable)=
### Meta razonable

- **70-80% de cobertura**: Objetivo razonable para proyectos
- **90-100% de cobertura**: Ideal para código crítico
- **< 60% de cobertura**: Señal de testing insuficiente

:::{tip}
Priorizá **calidad** de tests sobre cantidad de cobertura. Un test bien pensado que verifica comportamiento es más valioso que 10 tests triviales que solo ejecutan código.
:::

(08-junit-buenas-practicas)=
## Buenas Prácticas

(first-caracteristicas-de-buenos-tests)=
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

(08-junit-principios-adicionales)=
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

(anti-patrones-en-testing)=
## Anti-patrones en Testing

(08-junit-tests-fragiles)=
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

(tests-que-ignoran-excepciones)=
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

(assertions-multiples-sin-mensajes-descriptivos)=
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

(test-driven-development-con-junit)=
## Test-Driven Development con JUnit

El **Test-Driven Development** (TDD) es una metodología donde los tests se escriben antes que el código de producción. 

:::{important}
Para los fundamentos teóricos del TDD, el ciclo Red-Green-Refactor completo, las tres reglas, patrones (Fake It, Triangulation), comparación TDD vs Test-After y el ejemplo completo paso a paso de factorial, consultá {ref}`testing`.
:::

En esta sección nos enfocamos en aspectos prácticos específicos de JUnit para aplicar TDD.

(aplicando-tdd-con-junit-en-la-practica)=
### Aplicando TDD con JUnit en la Práctica

Cuando aplicás TDD con JUnit, seguís este flujo:

1. **Escribir el test primero** usando `\@Test`
2. **Ver que falla** (compilación o assertion)
3. **Implementar el mínimo código** para pasar
4. **Refactorizar** con confianza

**Ejemplo práctico de un ciclo:**

```java
// 1. RED: Test que falla
@Test
void testCalcularDescuento_ConMonto100_Retorna5Porciento() {
    double descuento = Calculadora.calcularDescuento(100);
    assertEquals(5.0, descuento, 0.01);
}
// Falla: método no existe

// 2. GREEN: Implementación mínima
public static double calcularDescuento(double monto) {
    return 5.0;  // Hardcoded para pasar el test
}
// Test pasa ✅

// 3. RED: Agregar otro test
@Test
void testCalcularDescuento_ConMonto500_Retorna10Porciento() {
    double descuento = Calculadora.calcularDescuento(500);
    assertEquals(10.0, descuento, 0.01);
}
// Falla: esperado 10.0, obtenido 5.0

// 4. GREEN: Generalizar
public static double calcularDescuento(double monto) {
    if (monto < 500) return 5.0;
    return 10.0;
}
// Ambos tests pasan ✅✅

// 5. REFACTOR: Mejorar sin romper tests
public static double calcularDescuento(double monto) {
    return monto < 500 ? 5.0 : 10.0;
}
// Tests siguen pasando ✅✅
```

(tips-para-tdd-con-junit)=
### Tips para TDD con JUnit

**Nombres descriptivos en TDD**

Los nombres de tests son especialmente importantes en TDD porque documentan el comportamiento antes de que exista el código:

```java
// ✅ Describe el comportamiento esperado
@Test void testSumar_ConDosPositivos_RetornaSuma() { }
```

**Baby steps (pasos pequeños)**

Cada ciclo debe ser mínimo:

```java
// ✅ Tests pequeños y enfocados
@Test void testValidar_ConEmailValido_RetornaTrue() { }
@Test void testValidar_SinArroba_RetornaFalse() { }
```

**Usar `\@BeforeEach` para evitar duplicación**

```java
private Calculadora calculadora;

@BeforeEach
void setUp() {
    calculadora = new Calculadora();
}

@Test
void testSumar() {
    assertEquals(5, calculadora.sumar(2, 3));
}
```

-------------------------------------

(estrategias-de-testing-con-junit)=
## Estrategias de Testing con JUnit

Para diseñar buenos tests, necesitamos estrategias sistemáticas. Para la teoría sobre particiones de equivalencia, valores límite y análisis de casos de prueba, consultá {ref}`testing`. Aquí vemos la implementación con JUnit.

(analisis-de-valores-limite-boundary-value-analysis)=
### Análisis de Valores Límite (Boundary Value Analysis)

Los errores de programación ocurren frecuentemente en los **límites** o **bordes** de los rangos válidos. Pensá en los típicos errores off-by-one: `i < n` vs `i <= n`, `>= 0` vs `> 0`, etc.

Esta técnica se enfoca en testear:

- El valor mínimo válido
- Justo **debajo** del mínimo (valor inválido)
- El valor máximo válido
- Justo **encima** del máximo (valor inválido)
- Valores típicos en el medio (para verificar el caso general)

**Ejemplo: Validador de edad**

Supongamos un método que valida si una edad es válida para votar (18-120 años):

```java
public class Validador {
    /**
     * Valida que la edad esté entre 18 y 120 años (inclusive).
     *
     * @param edad edad a validar
     * @return true si es válida para votar, false si no
     */
    public static boolean esEdadParaVotar(int edad) {
        return edad >= 18 && edad <= 120;
    }
}
```

**Tests de valores límite:**

```java
public class ValidadorTest {

    // === Límite inferior ===
    
    @Test
    void testEsEdadParaVotar_Con17_RetornaFalse() {
        // 17 está justo debajo del mínimo (18)
        assertFalse(Validador.esEdadParaVotar(17), "17 está por debajo del mínimo");
    }

    @Test
    void testEsEdadParaVotar_Con18_RetornaTrue() {
        // 18 es exactamente el mínimo válido
        assertTrue(Validador.esEdadParaVotar(18), "18 es el mínimo válido");
    }

    @Test
    void testEsEdadParaVotar_Con19_RetornaTrue() {
        // 19 está justo arriba del mínimo, dentro del rango
        assertTrue(Validador.esEdadParaVotar(19), "19 está dentro del rango");
    }

    // === Límite superior ===
    
    @Test
    void testEsEdadParaVotar_Con119_RetornaTrue() {
        // 119 está justo debajo del máximo, dentro del rango
        assertTrue(Validador.esEdadParaVotar(119), "119 está dentro del rango");
    }

    @Test
    void testEsEdadParaVotar_Con120_RetornaTrue() {
        // 120 es exactamente el máximo válido
        assertTrue(Validador.esEdadParaVotar(120), "120 es el máximo válido");
    }

    @Test
    void testEsEdadParaVotar_Con121_RetornaFalse() {
        // 121 está justo arriba del máximo
        assertFalse(Validador.esEdadParaVotar(121), "121 excede el máximo");
    }

    // === Valor típico (caso nominal) ===
    
    @Test
    void testEsEdadParaVotar_Con50_RetornaTrue() {
        // 50 es un valor típico en medio del rango
        assertTrue(Validador.esEdadParaVotar(50), "50 es un valor típico válido");
    }

    // === Valores extremos adicionales ===
    
    @Test
    void testEsEdadParaVotar_ConValorNegativo_RetornaFalse() {
        assertFalse(Validador.esEdadParaVotar(-5), "Edades negativas son inválidas");
    }

    @Test
    void testEsEdadParaVotar_ConCero_RetornaFalse() {
        assertFalse(Validador.esEdadParaVotar(0), "Cero es inválido");
    }
}
```

**Con tests parametrizados (más compacto):**

```java
@ParameterizedTest(name = "Edad {0} debe ser {1}")
@CsvSource({
    "-5,  false",  // Muy debajo del mínimo
    "0,   false",  // Cero
    "17,  false",  // Justo debajo del mínimo (LÍMITE)
    "18,  true",   // Mínimo válido (LÍMITE)
    "19,  true",   // Justo arriba del mínimo (LÍMITE)
    "50,  true",   // Valor típico
    "119, true",   // Justo debajo del máximo (LÍMITE)
    "120, true",   // Máximo válido (LÍMITE)
    "121, false",  // Justo arriba del máximo (LÍMITE)
    "200, false"   // Muy arriba del máximo
})
void testEsEdadParaVotar_ValoresLimite(int edad, boolean esperado) {
    assertEquals(esperado, Validador.esEdadParaVotar(edad),
                 "Edad " + edad + " debería ser " + (esperado ? "válida" : "inválida"));
}
```

Notá que los valores marcados como "(LÍMITE)" son los críticos. Si hay un bug off-by-one, uno de estos tests lo detectará.

(particiones-de-equivalencia)=
### Particiones de Equivalencia

Esta técnica complementa el análisis de límites. La idea es dividir el dominio de entrada en **clases de equivalencia**: grupos de valores que deberían comportarse igual.

**Principio**: Si el código funciona para un valor de una clase, probablemente funciona para todos los valores de esa clase. Entonces, testeamos un representante de cada clase.

**Ejemplo: Calculadora de descuentos**

```java
public class Calculadora {
    /**
     * Calcula el porcentaje de descuento según el monto de compra:
     * - Menos de $100: sin descuento (0%)
     * - $100 a $499: descuento pequeño (5%)
     * - $500 a $999: descuento medio (10%)
     * - $1000 o más: descuento grande (15%)
     *
     * @param monto monto de la compra (debe ser >= 0)
     * @return porcentaje de descuento como decimal (0.0, 0.05, 0.10, o 0.15)
     */
    public static double calcularDescuento(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        if (monto < 100) return 0.0;
        if (monto < 500) return 0.05;
        if (monto < 1000) return 0.10;
        return 0.15;
    }
}
```

**Identificación de particiones:**

El dominio de entrada (montos válidos) se divide naturalmente en 4 clases:

| Partición | Rango | Descuento | Ejemplo representativo |
| :--- | :--- | :--- | :--- |
| P1 | 0 ≤ monto < 100 | 0% | 50 |
| P2 | 100 ≤ monto < 500 | 5% | 250 |
| P3 | 500 ≤ monto < 1000 | 10% | 750 |
| P4 | monto ≥ 1000 | 15% | 1500 |

Además hay una partición inválida: montos negativos.

**Tests que cubren todas las particiones:**

```java
@ParameterizedTest(name = "Monto ${0} → descuento {1}")
@CsvSource({
    // Representantes de cada partición
    "50,    0.0",    // P1: un valor típico de la partición 1
    "250,   0.05",   // P2: un valor típico de la partición 2
    "750,   0.10",   // P3: un valor típico de la partición 3
    "1500,  0.15",   // P4: un valor típico de la partición 4
    
    // Límites entre particiones (combinando ambas técnicas)
    "99.99, 0.0",    // Límite P1/P2 (lado P1)
    "100,   0.05",   // Límite P1/P2 (lado P2)
    "499,   0.05",   // Límite P2/P3 (lado P2)
    "500,   0.10",   // Límite P2/P3 (lado P3)
    "999,   0.10",   // Límite P3/P4 (lado P3)
    "1000,  0.15"    // Límite P3/P4 (lado P4)
})
void testCalcularDescuento_ConDiferentesMontos(double monto, double descuentoEsperado) {
    assertEquals(descuentoEsperado, Calculadora.calcularDescuento(monto), 0.001,
                 "Monto $" + monto + " debería tener " + (descuentoEsperado * 100) + "% de descuento");
}
```

**Combinando particiones y límites:**

La estrategia más efectiva es combinar ambas técnicas:
1. Identificar las particiones del dominio
2. Para cada partición, testear un valor representativo del medio
3. Testear los valores en los límites entre particiones

(casos-de-prueba-especiales)=
### Casos de Prueba Especiales

Además de límites y particiones, hay ciertos casos que siempre deberías considerar:

#### Valores nulos (`null`)

En Java, a diferencia de C, hay una distinción clara entre "no hay valor" (`null`) y "valor vacío" (ej: string vacío `""`). Siempre testeá cómo tu código maneja `null`:

```java
@Test
void testProcesar_ConEntradaNull_LanzaIllegalArgumentException() {
    try {
        Procesador.procesar(null);
        fail("Se esperaba IllegalArgumentException para entrada null");
    } catch (IllegalArgumentException e) {
        assertTrue(e.getMessage().contains("null") || 
                   e.getMessage().contains("nulo"),
                   "El mensaje debería mencionar el problema");
    }
}
```

#### Arreglos y colecciones vacías

Un arreglo vacío no es `null`, pero tiene características especiales (no hay elementos, `length == 0`):

```java
@Test
void testCalcularPromedio_ConArregloVacio_LanzaIllegalArgumentException() {
    int[] valoresVacios = {};  // Arreglo vacío, no null

    try {
        Estadisticas.calcularPromedio(valoresVacios);
        fail("Se esperaba IllegalArgumentException para arreglo vacío");
    } catch (IllegalArgumentException e) {
        // Calcular promedio de cero elementos no tiene sentido matemático
    }
}

@Test
void testSumar_ConArregloVacio_RetornaCero() {
    int[] valoresVacios = {};
    
    // Sumar cero elementos tiene sentido: el resultado es 0
    assertEquals(0, Arreglos.sumar(valoresVacios));
}
```

#### Strings vacíos y con solo espacios

Un string puede estar vacío (`""`), contener solo espacios (`"   "`), o contener espacios "invisibles" (tabs, newlines):

```java
@ParameterizedTest
@ValueSource(strings = {"", "   ", "\t", "\n", "  \t\n  "})
void testValidarNombre_ConStringBlanco_RetornaFalse(String nombreBlanco) {
    assertFalse(Validador.esNombreValido(nombreBlanco),
                "Nombres en blanco no son válidos: '" + nombreBlanco + "'");
}
```

#### Duplicados en arreglos

Si tu código procesa colecciones, considerá qué pasa con elementos duplicados:

```java
@Test
void testContarUnicos_ConDuplicados_RetornaCantidadSinRepetidos() {
    int[] conDuplicados = {1, 2, 2, 3, 3, 3, 4};
    
    int unicos = Arreglos.contarUnicos(conDuplicados);
    
    assertEquals(4, unicos, "Debe contar solo valores únicos: 1, 2, 3, 4");
}
```

#### Un solo elemento

El caso de un solo elemento a veces tiene comportamiento especial:

```java
@Test
void testOrdenar_ConUnElemento_RetornaMismoArreglo() {
    int[] unElemento = {42};
    
    int[] resultado = Ordenador.ordenar(unElemento);
    
    assertArrayEquals(new int[]{42}, resultado);
}

@Test
void testBuscarMaximo_ConUnElemento_RetornaEseElemento() {
    int[] unElemento = {42};
    
    int maximo = Arreglos.buscarMaximo(unElemento);
    
    assertEquals(42, maximo);
}
```

(test-smells-con-junit)=
## Test Smells con JUnit

Los **test smells** son señales de que los tests tienen problemas. Aquí vemos ejemplos concretos con JUnit.

(test-interdependiente)=
### Test interdependiente

**Problema:** Tests que deben ejecutarse en orden específico.

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

(test-obscuro)=
### Test obscuro

**Problema:** No es claro qué se está testeando o por qué.

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

(test-con-logica-compleja)=
### Test con lógica compleja

**Problema:** Tests con condicionales, lazos, o cálculos complejos.

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

(test-demasiado-largo)=
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

(assertion-redundante)=
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

(test-silencioso)=
### Test silencioso

**Problema:** Test que no falla cuando debería.

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

:::{seealso}
Para más detalles sobre test smells y buenas prácticas generales, consultá {ref}`testing`.
:::

(debugging-de-tests-fallidos-con-junit)=
## Debugging de Tests Fallidos con JUnit

Cuando un test falla, seguí estos pasos sistemáticos:

(1-leer-el-mensaje-de-error-completo)=
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

(2-ejecutar-solo-ese-test)=
### 2. Ejecutar solo ese test

```bash
./gradlew test --tests CalculadoraTest.testCalcularDescuento
```

Aislá el problema ejecutando solo el test que falla.

(3-agregar-prints-temporales)=
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

(4-usar-el-debugger-del-ide)=
### 4. Usar el debugger del IDE

1. Ponele un **breakpoint** en la línea del assertion
2. Ejecutá el test en **modo debug**
3. Inspeccioná variables
4. Avanzá paso a paso (Step Into/Over)

(5-simplificar-el-test)=
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

(6-verificar-precondiciones)=
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

(7-revisar-cambios-recientes)=
### 7. Revisar cambios recientes

Si el test pasaba antes:

- ¿Qué código cambió?
- ¿Se modificó alguna dependencia?
- ¿Se agregaron nuevos datos o configuración?

```bash
(ver-diferencias-desde-ultimo-commit)=
# Ver diferencias desde último commit
git diff HEAD
```

(ejemplo-de-debugging-completo)=
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

(resumen-junit)=
## Resumen

Este apunte cubrió la implementación práctica de testing con JUnit 5:

1. **Configuración**: Gradle, estructura de proyecto, dependencias
2. **JUnit 5**: Framework moderno para testing en Java
3. **Anatomía de un test**: @Test, imports, estructura AAA
4. **Assertions**: assertEquals, assertTrue, assertNull, assertArrayEquals, etc.
5. **Excepciones**: Verificación con try-catch
6. **Ciclo de vida**: @BeforeEach, @AfterEach, @BeforeAll, @AfterAll
7. **Convenciones**: Reglas {ref}`regla-0x4000` a {ref}`regla-0x4005`
8. **Tests parametrizados**: @ParameterizedTest, @ValueSource, @CsvSource
9. **TDD en práctica**: Ejemplo completo con factorial
10. **Estrategias con JUnit**: Implementación de valores límite y particiones
11. **Test smells**: Ejemplos concretos con JUnit
12. **Debugging**: Técnicas para diagnosticar tests fallidos
13. **Cobertura**: JaCoCo y métricas

:::{seealso}
- {ref}`testing` — Fundamentos teóricos (pirámide, FIRST, TDD conceptual)
- {ref}`regla-0x4000` — Reglas de estilo para testing
:::

(ejercicios)=
## Ejercicios

```{exercise}
:label: ej-junit-calculadora

Implementá la clase `Calculadora` con métodos estáticos `sumar`, `restar`, `multiplicar` y `dividir`. Luego escribí una clase `CalculadoraTest` completa que:

1. Verifique operaciones básicas con números positivos
2. Verifique operaciones con números negativos
3. Verifique división por cero (debe lanzar excepción)
4. Verifique casos límite (cero, números grandes)
5. Use tests parametrizados para múltiples casos

Asegurate de seguir todas las reglas de testing vistas.
```

```{exercise}
:label: ej-junit-arreglos

Implementá una clase `Arreglos` con métodos estáticos para manipular arreglos de enteros: `suma`, `promedio`, `maximo`, `minimo`, `ordenar`, `buscar`. Escribí tests completos que:

1. Verifiquen cada operación individualmente
2. Prueben casos límite (arreglo vacío, un elemento)
3. Verifiquen excepciones en operaciones inválidas (arreglo null, vacío)
4. Usen `\@BeforeEach` para preparar datos de entrada comunes
5. Sean independientes entre sí

Incluí al menos 15 tests diferentes.
```

```{exercise}
:label: ej-junit-validador

Creá una clase `ValidadorContrasena` con un método estático `esValida(String contrasena)` que valide que una contraseña cumpla:
- Mínimo 8 caracteres
- Al menos una mayúscula
- Al menos un número
- Al menos un carácter especial (@, #, $, etc.)

Escribí tests parametrizados que verifiquen múltiples contraseñas válidas e inválidas. Usá `\@CsvSource` con al menos 10 casos de prueba.
```

```{exercise}
:label: ej-junit-strings

Implementá una clase `StringUtils` con métodos estáticos:
- `invertir(String s)`: Invierte el string
- `contarVocales(String s)`: Cuenta vocales
- `esPalindromo(String s)`: Verifica si es palíndromo
- `capitalizar(String s)`: Capitaliza cada palabra

Escribí tests completos para toda la funcionalidad, manteniendo las reglas de testing.
```

```{exercise}
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

```{exercise}
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

```{exercise}
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

Usá `\@CsvSource` para organizar los casos de prueba.
```

````{exercise}
:label: ej-junit-test-smells

El siguiente código contiene múltiples test smells. Identificalos y refactorizá los tests corrigiendo cada problema:

```java
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
```

Reescribí todos los tests siguiendo las buenas prácticas vistas en el apunte.
````

````{exercise}
:label: ej-junit-debugging

Un test está fallando con el siguiente error:

```
expected: <[1, 2, 3, 4, 5]> but was: <[5, 4, 3, 2, 1]>
```

El test es:

```java
@Test
void testOrdenar_ConArregloDesordenado_RetornaOrdenado() {
    int[] entrada = {3, 1, 4, 5, 2};
    
    int[] resultado = Ordenador.ordenar(entrada);
    
    assertArrayEquals(new int[]{1, 2, 3, 4, 5}, resultado);
}
```

Tareas:
1. Analizá el error: ¿Qué está pasando?
2. Formulá al menos 3 hipótesis de qué puede estar causando el problema
3. Describí qué pasos de debugging usarías (prints, breakpoints, etc.)
4. Proponé posibles soluciones según cada hipótesis

````