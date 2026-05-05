---
title: "Fundamentos del Testing de Software"
description: Conceptos teóricos sobre pruebas de software, desarrollo guiado por pruebas (TDD), estrategias de diseño de casos de prueba y principios de calidad.
label: testing
---

(fundamentos-del-testing-de-software)=
# Fundamentos del Testing de Software

En la ingeniería de software, el testing no es una fase posterior al desarrollo, sino una actividad integral del mismo. Las **pruebas de software** permiten validar que el código se comporta según lo esperado, detectar errores tempranamente y documentar el comportamiento del sistema.

:::{note}
Este apunte cubre los **fundamentos teóricos** del testing. Para la implementación práctica con JUnit 5, anotaciones, assertions y ejemplos de código, consultá {ref}`junit`.
:::

(del-testing-manual-al-automatizado)=
## Del Testing Manual al Automatizado

(el-problema-con-el-testing-manual)=
### El Problema con el Testing Manual

Si venís de programar en C, probablemente verificabas que tu código funcionara de manera manual: ejecutabas el programa, ingresabas datos y revisabas la salida con `printf()`. Este enfoque, conocido como **testing manual**, funciona para programas pequeños pero tiene limitaciones serias:

**Problemas del testing manual:**

1. **Es tedioso**: Cada vez que modificás algo, tenés que volver a ejecutar y verificar manualmente.
2. **Es propenso a errores**: Podés olvidar probar algún caso, o verificar mal la salida.
3. **No escala**: Con miles de líneas de código, es imposible probar todo a mano.
4. **No es repetible**: Otros desarrolladores no saben exactamente qué casos verificar.
5. **No hay registro**: Si un test falla, no queda documentado automáticamente.

(la-solucion-testing-automatizado)=
### La Solución: Testing Automatizado

El **testing automatizado** resuelve estos problemas: escribís código que verifica que tu otro código funciona correctamente. Una vez escrito, el test se puede ejecutar miles de veces con un solo comando. La verificación la hace el framework (por ejemplo, JUnit en Java), no vos mirando la pantalla.

(por-que-testeamos)=
## ¿Por Qué Testeamos?

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

Este crecimiento exponencial se debe a que un bug detectado tarde requiere más esfuerzo para ser localizado, reproducido y corregido, además del posible daño a usuarios o reputación.

(beneficios-del-testing-automatizado)=
### Beneficios del Testing Automatizado

El testing automatizado aporta múltiples beneficios:

1. **Detección temprana de errores**: Los bugs encontrados durante el desarrollo cuestan minutos; el mismo error en producción puede costar horas, días, o la reputación del producto.

2. **Documentación viva**: Los tests documentan cómo se espera que funcione el código. A diferencia de los comentarios, los tests no pueden quedar desactualizados: si el comportamiento cambia y los tests no se actualizan, fallan.

3. **Refactoring seguro**: El **refactoring** es modificar la estructura interna del código sin cambiar su comportamiento externo. Sin tests, esto da miedo porque no sabés si rompiste algo. Con tests, simplemente los ejecutás después de cada cambio.

4. **Prevención de regresiones**: Una **regresión** es cuando algo que funcionaba deja de funcionar (típicamente porque un cambio rompió algo inesperado). Los tests detectan regresiones automáticamente.

5. **Diseño mejorado**: Pensar en cómo testear el código mientras lo escribís te obliga a diseñar funciones más modulares, con responsabilidades claras y dependencias explícitas.

(la-piramide-de-testing)=
## La Pirámide de Testing

Mike Cohn propuso la **pirámide de testing** como modelo para distribuir los esfuerzos de prueba. La forma de pirámide no es casualidad: representa la proporción ideal de cada tipo de test.

```{figure} 08/piramide_testing.svg
:label: fig-piramide-testing
:align: center
:width: 70%

Pirámide de testing: muchas pruebas unitarias en la base, pocas E2E en la cima.
```

(tests-unitarios-base)=
### Tests Unitarios (Base)

Prueban una **unidad aislada** (una función, un método). Son:
- **Rápidos**: Se ejecutan en milisegundos
- **Determinísticos**: Siempre dan el mismo resultado
- **Aislados**: No dependen de bases de datos, archivos o red
- **Abundantes**: La mayoría de tus tests deberían estar aquí (70-80%)

En C, sería como escribir un `main()` que solo prueba una función específica.

(tests-de-integracion-medio)=
### Tests de Integración (Medio)

Verifican que **múltiples componentes colaboren** correctamente. Por ejemplo, que tu código se conecte bien a una base de datos. Son más lentos porque pueden involucrar operaciones de I/O.

(tests-end-to-end-cima)=
### Tests End-to-End (Cima)

Simulan el **uso real del sistema completo** desde la perspectiva del usuario. Son lentos, frágiles (se rompen fácilmente por cambios menores) y costosos de mantener. Por eso deberías tener pocos (10% aproximadamente).

:::{tip}
Una distribución saludable es aproximadamente 70% unitarias, 20% integración, 10% E2E. En este curso nos concentramos exclusivamente en **tests unitarios**.
:::

(verificacion-vs-validacion)=
## Verificación vs Validación

Es crucial distinguir estos conceptos que a menudo se confunden:

- **Verificación:** "¿Estamos construyendo el producto correctamente?" — El código hace lo que el programador pretendía que hiciera. Las pruebas unitarias verifican que la implementación coincide con la especificación técnica.

- **Validación:** "¿Estamos construyendo el producto correcto?" — El software satisface las necesidades reales del usuario. Esto requiere pruebas de aceptación y retroalimentación del usuario final.

Por ejemplo, podés tener una función `calcularImpuesto()` perfectamente implementada (verificada) pero que use una fórmula fiscal incorrecta (no validada). Las pruebas unitarias se enfocan primariamente en **verificación**.

(taxonomia-de-defectos)=
## Taxonomía de Defectos

Comprender los tipos de errores ayuda a diseñar tests más efectivos:

- **Errores de especificación:** El código hace exactamente lo programado, pero la lógica es incorrecta. Por ejemplo, usar `>=` en lugar de `>` porque se malinterpretó el requerimiento.

- **Errores de implementación:** Bugs en la traducción del algoritmo a código:
  - **Off-by-one:** Errores de "uno más" o "uno menos", típicos en lazos y condiciones de borde.
  - **Null references:** En Java, equivalente al problema de punteros nulos en C.
  - **Overflow:** Desbordamiento de variables numéricas.

- **Errores de integración:** Incompatibilidades entre componentes que funcionan bien aislados pero fallan al combinarse.

- **Errores de regresión:** Funcionalidad que dejó de funcionar tras un cambio.

(principios-f-i-r-s-t)=
## Principios F.I.R.S.T.

Para que una suite de pruebas sea efectiva, debe cumplir con los principios F.I.R.S.T. Este acrónimo resume las características que hacen que los tests sean útiles:

(fast-rapida)=
### Fast (Rápida)

Los tests deben ejecutarse en milisegundos. Una suite de cientos de tests unitarios debería correr en segundos. Si son lentos, los desarrolladores evitan ejecutarlos y pierden su utilidad.

(independent-independiente)=
### Independent (Independiente)

Ningún test debe depender del resultado o del estado dejado por otro. El orden de ejecución no debe importar. Deberías poder ejecutar cualquier test de forma aislada y obtener el mismo resultado.

```java
// ❌ MAL: Tests interdependientes
class MalTest {
    static int contadorGlobal = 0;  // Estado compartido entre tests
    
    @Test void test1() {
        contadorGlobal++;
        assertEquals(1, contadorGlobal);  // Depende de ejecutarse primero
    }
    
    @Test void test2() {
        assertEquals(1, contadorGlobal);  // Falla si test1 no corrió antes
    }
}

// ✅ BIEN: Cada test es independiente
class BuenTest {
    @Test void incrementar_desdeZero_retornaUno() {
        int contador = 0;  // Cada test tiene su propio estado
        contador++;
        assertEquals(1, contador);
    }
}
```

(repeatable-repetible)=
### Repeatable (Repetible)

Deben dar el mismo resultado en cualquier entorno (tu máquina, la de tu compañero, el servidor de CI) y en cualquier momento. Tests que "a veces pasan y a veces fallan" (llamados *flaky tests*) son peores que no tener tests.

(self-validating-autovalidable)=
### Self-validating (Autovalidable)

El test debe tener un resultado binario: **pasa** o **falla**. No debe requerir que un humano revise logs o archivos de salida para determinar si funcionó.

(timely-oportuno)=
### Timely (Oportuno)

Los tests deben escribirse junto con el código productivo, no "cuando haya tiempo" (que suele ser nunca).

(desarrollo-guiado-por-pruebas-tdd)=
## Desarrollo Guiado por Pruebas (TDD)

El **Test-Driven Development** (TDD) es una técnica de diseño de software que invierte el proceso tradicional: en lugar de escribir código y después testearlo, escribís el test primero y después el código que lo hace pasar.

(el-ciclo-red-green-refactor)=
### El Ciclo RED-GREEN-REFACTOR

TDD se basa en un ciclo corto y repetitivo de tres pasos:

```{figure} 08/tdd_ciclo.svg
:label: fig-tdd-ciclo
:align: center
:width: 60%

El ciclo TDD: Red → Green → Refactor.
```

1. **RED (Rojo)**: Escribir un test que falle para una funcionalidad que aún no existe. **Esto es esperado y correcto** — si el test pasara sin código, el test estaría mal escrito.

2. **GREEN (Verde)**: Escribir el código **mínimo necesario** para que el test pase. No te preocupes por elegancia o eficiencia; solo hacé que pase.

3. **REFACTOR**: Mejorar el código **asegurando que el test siga pasando**. Los tests verdes te dan confianza para modificar el código sin miedo.

Después repetís el ciclo con el siguiente test.

:::{important}
TDD no es solo para encontrar bugs; es una **herramienta de diseño**. Escribir el test primero te obliga a pensar en cómo se va a usar tu función antes de implementarla.
:::

(por-que-escribir-tests-primero)=
### ¿Por qué escribir tests primero?

La idea clave de TDD es que **los tests definen el comportamiento deseado**. Antes de escribir código, te forzás a pensar:

1. ¿Qué debería hacer este método?
2. ¿Qué entrada recibe?
3. ¿Qué salida produce?
4. ¿Cómo debería manejar errores?

En lugar de escribir código y después preguntarte "¿cómo lo testeo?", definís primero el comportamiento esperado y después escribís el código que lo cumple.

**Analogía**: Es como escribir el enunciado de un problema antes de resolverlo. Si no tenés claro qué querés lograr, es difícil saber si lo lograste.

(beneficios-de-tdd)=
### Beneficios de TDD

1. **Diseño mejorado**: Al escribir tests primero, pensás en la API desde la perspectiva del usuario (quien llama al método). Esto produce interfaces más limpias y usables.

2. **Código testeable por naturaleza**: El código escrito para pasar tests tiende a ser más modular, con dependencias claras y responsabilidades separadas.

3. **Cobertura de tests garantizada**: Todo código que escribís tiene al menos un test, porque el test vino primero.

4. **Documentación ejecutable**: Los tests muestran exactamente cómo usar el código y qué comportamiento esperar.

5. **Refactoring seguro**: Podés mejorar el código con confianza porque los tests verifican que no rompiste nada.

6. **Progreso medible**: Cada test verde es progreso tangible. Sabés cuánto avanzaste y cuánto falta.

(ejemplo-completo-funcion-factorial)=
### Ejemplo Completo: Función Factorial

Desarrollemos una función `factorial` usando TDD paso a paso para ilustrar el ciclo completo.

#### Iteración 1: El caso más simple (factorial de 0)

**Red - Escribir test que falle:**

```java
@Test
void testFactorial_ConCero_RetornaUno() {
    assertEquals(1, Matematica.factorial(0));  // Error: clase no existe
}
```

Ni siquiera compila. ¡Bien! El test "falla" porque no hay código.

**Green - Escribir código mínimo:**

```java
public class Matematica {
    public static long factorial(int n) {
        return 1;  // Implementación trivial que pasa el test
    }
}
```

Ejecutás el test: ✅ pasa. ¿Pero esto es trampa? ¡Siempre retorna 1!

No es trampa: el único test que tenemos es `factorial(0)`, y `0! = 1`. La implementación es correcta para los tests que existen. Si queremos que funcione para otros valores, necesitamos más tests.

#### Iteración 2: Factorial de un número positivo

**Red - Nuevo test:**

```java
@Test
void testFactorial_ConCinco_Retorna120() {
    assertEquals(120, Matematica.factorial(5));  // 5! = 120
}
```

Ejecutás: ❌ falla (esperado 120, obtenido 1).

**Green - Ampliar implementación:**

```java
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

Ejecutás ambos tests: ✅ ✅ pasan.

#### Iteración 3: Manejar entrada inválida

**Red - Test para caso de error:**

```java
@Test
void testFactorial_ConNumeroNegativo_LanzaExcepcion() {
    try {
        Matematica.factorial(-1);
        fail("Se esperaba IllegalArgumentException");
    } catch (IllegalArgumentException e) {
        assertTrue(e.getMessage().contains("negativo"));
    }
}
```

Ejecutás: ❌ falla (no se lanza excepción).

**Green - Agregar validación:**

```java
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

Ejecutás los tres tests: ✅ ✅ ✅ todos pasan.

#### Iteración 4: Refactorizar

Con todos los tests verdes, podemos mejorar el código con confianza:

```java
public class Matematica {
    /**
     * Calcula el factorial de un número entero no negativo.
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
            throw new IllegalArgumentException(
                "El número no puede ser negativo: " + n);
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
```

Ejecutás todos los tests después del refactor: ✅ ✅ ✅ siguen pasando. El refactor fue seguro.

(las-tres-reglas-del-tdd)=
### Las Tres Reglas del TDD

Robert C. Martin (Uncle Bob) formalizó las reglas estrictas del TDD:

1. **No escribir código de producción** excepto para pasar un test que falla.
2. **No escribir más de un test unitario** que sea suficiente para fallar (y no compilar es fallar).
3. **No escribir más código de producción** del necesario para pasar el test actual.

Estas reglas mantienen el ciclo corto y enfocado. Cada iteración agrega una pequeña pieza de funcionalidad verificada.

(patrones-de-tdd)=
### Patrones de TDD

Beck identificó patrones recurrentes que te ayudan a avanzar:

:::{table} Patrones de TDD
:label: tbl-patrones-tdd

| Patrón | Descripción | Cuándo usarlo |
| :--- | :--- | :--- |
| **Fake It** | Retornar un valor hardcodeado | Cuando no sabés cómo generalizar |
| **Obvious Implementation** | Escribir la implementación directamente | Casos simples donde la solución es obvia |
| **Triangulation** | Agregar más tests hasta que la generalización sea obvia | Cuando no está claro cómo generalizar |
:::

(tdd-vs-test-after)=
### TDD vs Test-After

| Aspecto | TDD (Test-First) | Test-After |
| :--- | :--- | :--- |
| **Cuándo se escriben tests** | Antes del código | Después del código |
| **Cobertura de tests** | Naturalmente alta | Puede ser parcial |
| **Influencia en diseño** | Los tests guían el diseño | Tests se adaptan al diseño |
| **Seguridad al refactorizar** | Alta | Menor |
| **Curva de aprendizaje** | Más empinada | Más suave |
| **Tiempo inicial** | Mayor | Menor |
| **Tiempo total** | A menudo menor (menos bugs) | A menudo mayor (más debugging) |

**¿Cuándo usar cada uno?**

- **TDD**: Para código nuevo importante, cuando querés diseño limpio, en equipos que lo practican
- **Test-After**: Para prototipos rápidos, código legacy que no tenía tests, cuando estás explorando una solución

:::{tip}
TDD es especialmente útil cuando:

- **Estás aprendiendo una nueva API o librería**: Los tests te ayudan a explorar cómo funciona.
- **El problema es complejo**: Dividirlo en tests pequeños hace el problema manejable.
- **El código es crítico**: Código que maneja dinero, seguridad, o vidas necesita máxima confiabilidad.
- **Trabajás en equipo**: Los tests documentan el comportamiento esperado para otros desarrolladores.
- **Vas a refactorizar código existente**: Primero escribís tests que capturan el comportamiento actual, después refactorizás con seguridad.
:::

(beneficios-y-criticas-del-tdd)=
### Beneficios y Críticas del TDD

**Beneficios demostrados:**

- Código más modular y testeable
- Documentación ejecutable
- Confianza para refactorizar
- Menos bugs en producción
- Progreso medible

**Críticas válidas:**

- Curva de aprendizaje significativa
- Puede ralentizar el desarrollo inicial
- No reemplaza otros tipos de testing
- Requiere disciplina constante

:::{important}
En esta cátedra, practicaremos ambas aproximaciones. Lo fundamental es que **el código tenga tests**, independientemente de cuándo se escribieron.
:::

(estrategias-de-diseno-de-casos-de-prueba)=
## Estrategias de Diseño de Casos de Prueba

(tecnicas-de-caja-negra)=
### Técnicas de Caja Negra

Las pruebas de caja negra se diseñan **sin conocer la implementación interna**, basándose solo en la especificación.

#### Partición de Equivalencia

Dividir el dominio de entrada en **clases que se comportan de manera equivalente**. Solo es necesario probar un representante de cada clase.

**Ejemplo conceptual:**

Para validar edad para licencia de conducir, las clases de equivalencia son:
- Inválidas: edad < 0
- Menores: 0 ≤ edad < 18
- Válidas: 18 ≤ edad ≤ 100
- Inválidas: edad > 100

Se necesita un test representativo de cada clase.

#### Análisis de Valores Límite

Probar en las **fronteras** de las particiones, donde la mayoría de los bugs ocurren. Para el límite de 18 años, probar:
- 17 (justo antes del límite)
- 18 (exactamente en el límite)
- 19 (justo después del límite)

(tecnicas-de-caja-blanca)=
### Técnicas de Caja Blanca

Con acceso al código fuente, podemos diseñar tests que ejerciten caminos específicos.

#### Cobertura de Ramas

Cada decisión (`if`, `switch`) debe evaluarse tanto a verdadero como falso. Para un método con tres ramas de decisión, se necesitan al menos tres tests que ejecuten cada rama.

(casos-especiales-a-considerar)=
### Casos Especiales a Considerar

:::{table} Casos especiales comunes
:label: tbl-casos-especiales

| Tipo de entrada | Casos a probar |
| :--- | :--- |
| Strings | `null`, vacío `""`, espacios, muy largo |
| Números | 0, 1, -1, MAX_VALUE, MIN_VALUE |
| Colecciones | `null`, vacía, un elemento, muchos elementos |
| Fechas | Bisiestos, fin de mes, cambio de año |
:::

(cobertura-de-codigo)=
## Cobertura de Código

La **cobertura** es una métrica que indica qué porcentaje del código ha sido ejecutado por los tests.

(tipos-de-cobertura)=
### Tipos de Cobertura

- **Cobertura de Líneas:** ¿Se ejecutó esta línea?
- **Cobertura de Ramas:** ¿Se probaron todos los caminos de un `if`?
- **Cobertura de Métodos:** ¿Se invocó este método al menos una vez?

(11-testing-herramientas)=
### Herramientas

En Java, la herramienta más utilizada es **JaCoCo** (Java Code Coverage), que se integra con Gradle y genera reportes HTML detallados.

:::{warning}
Una cobertura del 100% **no garantiza** la ausencia de bugs. La cobertura indica qué código no ha sido probado, pero no garantiza que las pruebas existentes sean de calidad.
:::

(meta-razonable)=
### Meta Razonable

- **70-80%**: Objetivo razonable para proyectos típicos
- **90-100%**: Para código crítico (seguridad, finanzas)
- **< 60%**: Señal de testing insuficiente

(antipatrones-de-testing)=
## Antipatrones de Testing

(tests-fragiles)=
### Tests Frágiles

Tests que fallan por cambios no relacionados con lo que prueban. Por ejemplo, un test que depende del orden de elementos en una colección cuando el orden no es parte del contrato.

**Solución:** Verificar propiedades esenciales (contenido, tamaño) en lugar de detalles de implementación (orden específico).

(tests-que-prueban-la-implementacion)=
### Tests que Prueban la Implementación

Tests acoplados a cómo se hace algo en lugar de qué se hace. Esto hace que refactorings seguros rompan tests.

**Solución:** Verificar comportamiento observable desde la perspectiva del usuario del código.

(tests-sin-aserciones)=
### Tests sin Aserciones

Tests que ejecutan código pero no verifican nada. Siempre pasan, incluso si el código falla.

**Solución:** Todo test debe tener al menos una verificación explícita del resultado esperado.

(11-testing-ejercicios-conceptuales)=
## Ejercicios Conceptuales

```{exercise}
:label: ej-tdd-validador

Aplicando TDD, describí los pasos (ciclos RED-GREEN-REFACTOR) para implementar un método estático `esNumeroPar(int n)` que retorna `true` si el número es par y `false` si es impar.
```

```{solution} ej-tdd-validador
:class: dropdown

1. **Red**: Escribo un test que verifique `esNumeroPar(2)` retorna `true`. El test falla porque el método no existe.
2. **Green**: Implemento el método con `return true;` (implementación mínima que pasa el test actual).
3. **Red**: Escribo un test que verifique `esNumeroPar(3)` retorna `false`. El test falla (esperado `false`, obtenido `true`).
4. **Green**: Generalizo la implementación: `return n % 2 == 0;`. Ambos tests pasan.
5. **Refactor**: El código ya es simple y claro. Podría agregar JavaDoc.
6. **Red**: Escribo un test para número negativo par: `esNumeroPar(-4)` retorna `true`. Ya pasa (la lógica funciona para negativos).
7. **Red**: Escribo un test para cero: `esNumeroPar(0)` retorna `true`. Ya pasa.

**Observación**: Los últimos dos tests pasan sin cambiar el código, confirmando que la implementación es correcta para todos los casos.
```

```{exercise}
:label: ej-valores-limite

Dado un método `esAdulto(int edad)` que retorna `true` si `edad >= 18` (asumiendo edades válidas de 0 a 120), listá todos los valores límite que deberían testearse.
```

```{solution} ej-valores-limite
:class: dropdown

**Límite inferior (0):** -1 (inválido), 0 (válido mínimo)

**Límite de mayoría de edad (18):** 17 (false), 18 (true), 19 (true)

**Límite superior (120):** 119 (true), 120 (true máximo), 121 (inválido)

Total: 8 casos de prueba para cobertura completa de valores límite.
```

```{exercise}
:label: ej-particiones

Para un método `calcularDescuento(double monto)` con las siguientes reglas:
- Menos de $100: 0% descuento
- $100 a $499: 5% descuento
- $500 a $999: 10% descuento
- $1000 o más: 15% descuento

Identificá las particiones de equivalencia y proponé un conjunto mínimo de tests.
```

```{solution} ej-particiones
:class: dropdown

**Particiones identificadas:**
- P1: monto < 0 (inválida)
- P2: 0 ≤ monto < 100 (0%)
- P3: 100 ≤ monto < 500 (5%)
- P4: 500 ≤ monto < 1000 (10%)
- P5: monto ≥ 1000 (15%)

**Tests mínimos (representantes + límites):**
1. monto = -5 (P1, inválido)
2. monto = 50 (P2, representante)
3. monto = 99.99 (límite P2/P3)
4. monto = 100 (límite P2/P3)
5. monto = 250 (P3, representante)
6. monto = 500 (límite P3/P4)
7. monto = 750 (P4, representante)
8. monto = 1000 (límite P4/P5)
9. monto = 1500 (P5, representante)
```

(referencias-bibliograficas)=
## Referencias Bibliográficas

- **Beck, K.** (2003). _Test Driven Development: By Example_. Addison-Wesley.
- **Martin, R. C.** (2009). _Clean Code_. Prentice Hall. (Capítulo 9: Unit Tests).
- **Meszaros, G.** (2007). _xUnit Test Patterns: Refactoring Test Code_. Addison-Wesley.
- **Cohn, M.** (2009). _Succeeding with Agile_. Addison-Wesley. (Pirámide de testing).

:::{seealso}
- {ref}`junit` — Implementación práctica de testing con JUnit 5
- {ref}`regla-0x4000` — Estándares de testing de la cátedra
:::
