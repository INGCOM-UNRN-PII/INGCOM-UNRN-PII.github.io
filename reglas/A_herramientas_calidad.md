---
title: Herramientas de Análisis y Verificación de Calidad
description: Fundamentos técnicos de las herramientas de análisis estático, testing y verificación de calidad de código utilizadas en Programación II.
---

# Herramientas de Análisis y Verificación de Calidad

## Introducción

En el desarrollo de software profesional, escribir código que funcione no es suficiente. El código debe ser **mantenible**, **legible**, **libre de errores comunes** y estar **bien testeado**. Para lograr esto, utilizamos herramientas automatizadas que verifican la calidad del código en cada paso del desarrollo.

:::{note}
Este apunte se enfoca en los **conceptos técnicos y fundamentos** de cada herramienta. Para instrucciones prácticas sobre cómo ejecutar comandos de Gradle, consultá [la guía práctica de Gradle](../guias/guia_gradle.md).
:::

Este apunte profundiza en:

- **Qué** detecta cada herramienta y **por qué** es importante
- **Cómo funciona** internamente cada herramienta
- **Qué tipo de problemas** encuentra y cómo interpretarlos
- La **filosofía** detrás de cada herramienta

:::{important}
Todas estas herramientas están integradas en el proceso de construcción mediante Gradle. Gradle se encarga de descargarlas, configurarlas y ejecutarlas automáticamente.
:::

## Categorías de Herramientas

Las herramientas se organizan en tres categorías según qué aspecto verifican:

### 1. Análisis Estático de Código

Herramientas que analizan el **código fuente** sin ejecutarlo:

- **Checkstyle**: Estilo y formato
- **PMD**: Patrones problemáticos y complejidad
- **Error Prone**: Errores durante compilación

### 2. Análisis de Bytecode

Herramientas que analizan el **código compilado**:

- **SpotBugs**: Bugs en bytecode
- **NullAway**: Análisis de null safety

### 3. Análisis Dinámico (Testing)

Herramientas que **ejecutan** el código:

- **JaCoCo**: Cobertura de tests
- **PIT**: Mutation testing
- **ArchUnit**: Verificación de arquitectura

:::{note}
Cada categoría tiene fortalezas diferentes. El análisis estático es rápido pero puede tener falsos positivos. El análisis dinámico es más preciso pero requiere tests bien escritos.
:::

## Herramientas de Análisis Estático

### 1. Checkstyle: Verificación de Estilo

**Checkstyle** es una herramienta que verifica que el código siga convenciones de estilo consistentes.

#### Filosofía

La idea detrás de Checkstyle es simple pero poderosa: **el código se lee más veces de las que se escribe**. Por lo tanto, es más importante que sea fácil de leer que fácil de escribir.

Checkstyle no detecta bugs ni problemas lógicos. Su objetivo es:

1. **Legibilidad**: Hacer el código más fácil de entender
2. **Consistencia**: Todo el equipo escribe de la misma manera
3. **Profesionalismo**: El código se ve pulido y cuidado

#### ¿Cómo funciona?

Checkstyle analiza el **código fuente** (archivos `.java`) usando un parser que genera un **Abstract Syntax Tree (AST)**. Luego aplica reglas sobre ese árbol.

```
Código fuente → Parser → AST → Reglas de Checkstyle → Reporte
```

#### Categorías de verificaciones

#### Categorías de verificaciones

**1. Formato y espaciado**

- Indentación consistente (4 espacios)
- Espacios alrededor de operadores
- Líneas no demasiado largas (120 caracteres)
- Líneas en blanco entre métodos

**2. Convenciones de nombrado**

- Clases: `PascalCase`
- Métodos y variables: `camelCase`
- Constantes: `MAYUSCULAS_CON_GUIONES`
- Paquetes: todo en minúsculas

**3. Estructura de clases**

- Orden de elementos (campos, constructores, métodos)
- Modificadores en orden estándar (`public static final`)
- Llaves en estructuras de control

**4. Documentación**

- Javadoc en métodos públicos
- Comentarios de parámetros y retornos
- Descripciones de clases

#### ¿Por qué es importante cada regla?

**Indentación consistente**: Sin ella, es difícil ver la estructura del código (qué está dentro de qué).

```java
// ❌ Sin indentación - difícil de leer
public class Ejemplo {
public void metodo() {
if (condicion) {
hacer algo();
}
}
}

// ✅ Con indentación - estructura clara
public class Ejemplo {
    public void metodo() {
        if (condicion) {
            hacerAlgo();
        }
    }
}
```

**Espacios alrededor de operadores**: Mejoran la legibilidad al separar visualmente los elementos.

```java
// ❌ Sin espacios - denso y difícil de leer
int resultado=a+b*c-d/e;

// ✅ Con espacios - cada operación es clara
int resultado = a + b * c - d / e;
```

**Longitud de líneas**: Líneas muy largas requieren scrolling horizontal, lo que dificulta la lectura.

**Javadoc**: Documenta el contrato del método sin necesidad de leer su implementación.

#### Configuración del proyecto

El archivo `config/checkstyle/checkstyle.xml` define todas las reglas específicas de la cátedra. Este archivo usa el formato XML de Checkstyle:

```xml
<module name="Checker">
    <module name="TreeWalker">
        <module name="Indentation">
            <property name="basicOffset" value="4"/>
        </module>
        <module name="MethodName">
            <property name="format" value="^[a-z][a-zA-Z0-9]*$"/>
        </module>
    </module>
</module>
```

Cada `<module>` representa una regla con sus propiedades.

#### Interpretando el reporte

El reporte de Checkstyle indica:

- **Archivo y línea** donde está el problema
- **Regla violada** (nombre técnico)
- **Mensaje descriptivo** de qué corregir
- **Severidad** (error, warning, info)

:::{tip}
No todos los warnings son iguales. Enfocáte primero en los errores de severidad alta que afectan la legibilidad significativamente.
:::

:::{note}
Checkstyle se enfoca en el **formato y estilo** del código, no en errores lógicos. Es como un revisor que verifica que el código sea legible y consistente.
:::

### 2. PMD: Análisis Estático de Código

**PMD** (Programming Mistake Detector) analiza el código fuente buscando **patrones problemáticos**, **código complejo** y **malas prácticas**.

#### Filosofía

PMD parte de la premisa de que ciertos patrones de código, aunque compilen y funcionen, son **propensos a errores** o **dificultan el mantenimiento**. Su objetivo es detectar estos patrones antes de que causen problemas.

A diferencia de Checkstyle (que se enfoca en formato), PMD se enfoca en **calidad estructural**:

- ¿El código es demasiado complejo?
- ¿Hay código muerto o no utilizado?
- ¿Se usan patrones problemáticos?

#### ¿Cómo funciona?

PMD analiza el código fuente generando un AST y buscando patrones específicos:

```
Código fuente → Parser → AST → Búsqueda de patrones → Reporte
```

#### Categorías de detección

**1. Código no utilizado**

- Variables declaradas pero nunca leídas
- Parámetros de métodos no usados
- Métodos privados nunca llamados
- Imports innecesarios

**¿Por qué es malo?**

- Confunde al lector ("¿para qué está esto?")
- Aumenta el tamaño del código sin beneficio
- Puede indicar código incompleto o errores

**2. Complejidad excesiva**

- Métodos muy largos (>50 líneas)
- Complejidad ciclomática alta (muchos if/for anidados)
- Clases con demasiadas responsabilidades

**¿Por qué es malo?**

- Difícil de entender
- Difícil de testear
- Propenso a bugs

**3. Código duplicado (Copy-Paste)**

PMD detecta bloques de código idénticos o muy similares en diferentes lugares.

```java
// ❌ Código duplicado detectado por PMD
public int calcularDescuentoCliente(double monto) {
    if (monto > 1000) {
        return (int) (monto * 0.1);
    }
    return 0;
}

public int calcularDescuentoProveedor(double monto) {
    if (monto > 1000) {
        return (int) (monto * 0.1);
    }
    return 0;
}

// ✅ Sin duplicación
private int calcularDescuento(double monto, double porcentaje) {
    if (monto > 1000) {
        return (int) (monto * porcentaje);
    }
    return 0;
}
```

**¿Por qué es malo el código duplicado?**

- Si hay un bug, tenés que corregirlo en múltiples lugares
- Si cambia la lógica, tenés que actualizarlo en todos lados
- Aumenta el tamaño del código sin razón

**4. Patrones problemáticos**

PMD conoce muchos patrones que son técnicamente válidos pero problemáticos:

- Comparar strings con `==` en lugar de `.equals()`
- Catch de `Exception` genérica
- Uso de `System.out.println()` en código de producción
- No cerrar recursos (archivos, conexiones)

#### Métricas de complejidad

PMD calcula métricas cuantitativas de complejidad:

**Complejidad Ciclomática**: Cuenta el número de caminos diferentes a través del código.

```java
// Complejidad = 1 (un solo camino)
public int absoluto(int n) {
    return Math.abs(n);
}

// Complejidad = 2 (dos caminos: if verdadero, if falso)
public int absoluto(int n) {
    if (n < 0) {
        return -n;
    }
    return n;
}

// Complejidad = 4 (muchos caminos)
public String evaluar(int nota) {
    if (nota >= 90) {
        return "Excelente";
    } else if (nota >= 70) {
        return "Bueno";
    } else if (nota >= 50) {
        return "Suficiente";
    } else {
        return "Insuficiente";
    }
}
```

**Regla general**: Complejidad > 10 indica que el método debería dividirse.

#### Configuración personalizada

El archivo `config/pmd/programacion2.xml` define reglas específicas de la cátedra. Las reglas están organizadas por categorías (Best Practices, Code Style, Design, etc.) y pueden personalizarse.

:::{important}
PMD detecta problemas que **podrían** causar bugs o dificultar el mantenimiento. No todos son errores críticos, pero mejorar estos aspectos hace el código más profesional y mantenible.
:::

## Herramientas de Análisis de Bytecode

### 3. SpotBugs: Detección de Bugs

**SpotBugs** (sucesor de FindBugs) analiza el **bytecode compilado** (.class) buscando patrones que indican bugs reales.

#### Filosofía

SpotBugs parte de décadas de experiencia analizando bugs comunes en código Java. Busca patrones que casi siempre indican un error, no solo malas prácticas.

**Diferencia clave**: Analiza bytecode, no código fuente. Esto le permite:

1. Ver el código como lo ve la JVM
2. Detectar optimizaciones del compilador
3. Encontrar problemas que desaparecen en el código fuente

#### Categorías de bugs

**1. Correctness (Corrección)**

Bugs que casi seguro causan comportamiento incorrecto:

- Comparar strings con `==` en lugar de `.equals()`
- `equals()` implementado incorrectamente
- Array indexing sin verificar límites
- Operaciones que siempre producen el mismo resultado

**2. Bad Practice (Mala Práctica)**

No son bugs pero son patrones problemáticos:

- `equals()` sin `hashCode()`
- Ignorar valores de retorno importantes
- `finalize()` que no llama a `super.finalize()`

**3. Performance (Rendimiento)**

Código que funciona pero es ineficiente:

- Crear objetos innecesarios en lazos
- Llamar a métodos costosos repetidamente
- Usar estructuras de datos incorrectas

**4. Vulnerability (Vulnerabilidad)**

Potenciales problemas de seguridad:

- SQL injection
- Path traversal
- Exposición de información sensible

#### ¿Cómo funciona?

```
Bytecode (.class) → Análisis de flujo de datos → Detección de patrones → Reporte
```

SpotBugs hace análisis de **flujo de datos**: sigue el valor de las variables a través del código para detectar usos incorrectos.

#### Niveles de confianza

SpotBugs asigna un nivel de confianza a cada bug detectado:

- **High**: Casi seguro que es un bug
- **Medium**: Probablemente un bug
- **Low**: Podría ser un bug

El proyecto está configurado con `reportLevel = LOW` para ver todos los posibles problemas.

:::{warning}
SpotBugs analiza el **bytecode**, no el código fuente. Por eso puede detectar problemas que otras herramientas no encuentran y también puede reportar false positives en código correcto pero inusual.
:::

### 4. Error Prone: Detección Durante Compilación

**Error Prone** es un analizador desarrollado por Google que se integra en el **proceso de compilación** y eleva ciertos patrones problemáticos a **errores de compilación**.

#### Filosofía

Error Prone parte de la premisa: "Si algo casi siempre es un error, debería ser un error de compilación".

A diferencia de otras herramientas que generan reportes que podés ignorar, Error Prone **previene** que el código compile si detecta problemas graves.

#### ¿Cómo funciona?

Error Prone se integra como un plugin del compilador de Java:

```
Código fuente → javac + Error Prone → Errores de compilación o bytecode
```

Durante la compilación, Error Prone analiza el AST y aplica sus verificaciones antes de generar bytecode.

#### Categorías de checks

**1. Patrones casi siempre erróneos**

- `@Override` faltante: Previene errores de typos
- Modificar colección durante iteración: ConcurrentModificationException
- `equals()` sin `hashCode()`: Rompe contratos de colecciones
- Comparar tipos incompatibles: Siempre retorna false

**2. Errores sutiles**

- Crear excepciones sin lanzarlas
- Ignorar valores de retorno importantes
- Condiciones que siempre son verdaderas/falsas

#### Ejemplo conceptual

```java
// Error Prone PREVIENE que esto compile
public class Usuario {
    private String nombre;

    // ERROR EN COMPILACIÓN: MissingOverride
    public boolean equals(Object obj) {  // Falta @Override
        return nombre.equals(((Usuario) obj).nombre);
    }

    // ERROR EN COMPILACIÓN: EqualsHashCode
    // equals() sin hashCode()
}
```

El código no compilará hasta que agregues `@Override` e implementes `hashCode()`.

:::{tip}
Error Prone previene bugs **antes** de que lleguen al código compilado. Es la primera y más efectiva línea de defensa.
:::

### 5. NullAway: Análisis de Null Safety

**NullAway** es un plugin de Error Prone especializado en detectar posibles `NullPointerException` mediante análisis estático de flujo de datos.

#### El problema de null

`NullPointerException` es uno de los errores más comunes en Java. Ocurre cuando intentás usar un objeto que es `null`:

```java
String nombre = obtenerNombre();  // Puede retornar null
int longitud = nombre.length();    // NullPointerException si nombre es null
```

El problema es que el compilador de Java **no verifica** si una variable puede ser `null` antes de usarla.

#### Filosofía de NullAway

NullAway introduce **null safety** al código Java mediante estas reglas:

1. Por defecto, **ninguna variable puede ser null** (a menos que lo declares explícitamente)
2. Si un método puede retornar `null`, debe estar anotado con `@Nullable`
3. Antes de usar una variable `@Nullable`, debes verificar que no sea `null`

#### Anotaciones

```java
import javax.annotation.Nullable;

public class Repositorio {
    // Método que PUEDE retornar null
    @Nullable
    public Usuario buscarPorId(int id) {
        // Si no se encuentra, retorna null
        return baseDatos.query(id);
    }

    // Método que NUNCA retorna null (no necesita anotación)
    public Usuario crear(String nombre) {
        return new Usuario(nombre);
    }

    public void procesar(int id) {
        Usuario usuario = buscarPorId(id);

        // ERROR: NullAway detecta que usuario puede ser null
        System.out.println(usuario.getNombre());

        // CORRECTO: Verificar null antes de usar
        if (usuario != null) {
            System.out.println(usuario.getNombre());
        }
    }
}
```

#### Análisis de flujo de datos

NullAway hace **análisis de flujo**: rastrea si una variable puede ser `null` en cada punto del código.

```java
public void ejemplo(@Nullable String texto) {
    // Aquí texto puede ser null

    if (texto != null) {
        // Aquí NullAway sabe que texto NO es null
        int longitud = texto.length();  // OK
    }

    // Aquí texto puede ser null nuevamente
    int longitud = texto.length();  // ERROR
}
```

#### Beneficios

1. **Prevención**: Detecta `NullPointerException` antes de ejecutar el código
2. **Documentación**: Las anotaciones documentan qué puede ser `null`
3. **Confianza**: Podés usar variables sin verificaciones redundantes

:::{important}
NullAway ayuda a prevenir uno de los errores más comunes en Java: el `NullPointerException`. Usá las anotaciones `@Nullable` para documentar explícitamente cuándo algo puede ser `null`.
:::

## Herramientas de Testing Dinámico

Las herramientas de análisis dinámico **ejecutan** el código para verificar su comportamiento y calidad.

### 6. JaCoCo: Cobertura de Código

**JaCoCo** (Java Code Coverage) mide qué porcentaje del código es **ejecutado** por los tests.

#### Filosofía

La cobertura de código responde la pregunta: "¿Qué partes de mi código están siendo verificadas por tests?"

**No es** una métrica de calidad de tests (tests malos pueden tener 100% de cobertura).
**Es** una métrica de **completitud**: identifica código que NO está siendo testeado en absoluto.

#### Tipos de cobertura

**1. Cobertura de líneas**

**1. Cobertura de líneas**: ¿Qué líneas de código se ejecutaron?

```java
public int dividir(int a, int b) {
    if (b == 0) {              // Línea ejecutada ✓
        throw new Exception(); // Línea ejecutada ✓
    }
    return a / b;              // Línea NO ejecutada ✗
}
```

Cobertura: 66% (2 de 3 líneas)

**2. Cobertura de ramas**: ¿Qué caminos condicionales se probaron?

```java
if (nota >= 70) {
    return "Aprobado";    // Rama A
} else {
    return "Desaprobado"; // Rama B
}
```

Si solo probaste `nota = 80`, cubriste la rama A pero no la B. Cobertura de ramas: 50%.

**3. Cobertura de métodos**: ¿Qué métodos se invocaron?

Simple: método llamado = cubierto, método no llamado = no cubierto.

#### Limitaciones de la cobertura

```java
// 100% de cobertura pero test inútil
@Test
void testSumar() {
    Calculadora calc = new Calculadora();
    calc.sumar(2, 3);  // Ejecuta la línea pero NO verifica resultado
}
```

Este test tiene 100% de cobertura pero no verifica nada. Por eso la cobertura es **necesaria pero no suficiente**.

**Regla de oro**: Cobertura alta + tests que verifican = código bien testeado.

:::{warning}
**100% de cobertura NO garantiza que el código esté bien testeado.** Es posible tener 100% de cobertura con tests que no verifican nada. La cobertura es una métrica necesaria pero no suficiente.
:::

### 7. PIT (Pitest): Mutation Testing

**PIT (Pitest)** mide la **calidad de los tests** introduciendo bugs artificiales y verificando si los tests los detectan.

#### Filosofía

JaCoCo mide si el código se ejecutó. PIT mide si los tests **realmente verifican** el comportamiento.

**Pregunta clave**: Si introduzco un bug en el código, ¿algún test falla?

#### ¿Cómo funciona?

1. PIT modifica el código (crea un **mutante**)
2. Ejecuta todos los tests
3. Si algún test falla, el mutante fue **detectado** (bueno)
4. Si todos pasan, el mutante **sobrevivió** (malo - tests débiles)

#### Tipos de mutaciones

**Operadores aritméticos**: `+` → `-`, `*` → `/`

```java
// Original
return a + b;

// Mutante
return a - b;  // ¿Los tests detectan que cambió de suma a resta?
```

**Condicionales**: `<` → `<=`, `==` → `!=`

```java
// Original
if (edad < 18) { }

// Mutante
if (edad <= 18) { }  // ¿Los tests detectan la diferencia?
```

**Retornos**: cambiar valores de retorno

```java
// Original
return true;

// Mutante
return false;  // ¿Los tests detectan el cambio?
```

**Llamadas a métodos**: remover llamadas

```java
// Original
lista.add(elemento);

// Mutante
// (línea removida)  // ¿Los tests detectan que no se agregó?
```

#### Métricas

**Mutation Score**: Porcentaje de mutantes detectados.

```
Mutation Score = (Mutantes detectados / Total de mutantes) * 100
```

- **80-100%**: Excelente - tests robustos
- **60-80%**: Bueno - algunos tests débiles
- **<60%**: Tests muy débiles

#### Ejemplo práctico

```java
public int absoluto(int n) {
    if (n < 0) {
        return -n;
    }
    return n;
}

// Test débil - solo un caso
@Test
void testAbsoluto() {
    assertEquals(5, absoluto(-5));
}
```

**Mutaciones que PIT genera**:

1. `n < 0` → `n <= 0` (sobrevive - no hay test para n=0)
2. `n < 0` → `n > 0` (sobrevive - no hay test para n>0)
3. `return -n` → `return n` (detectado por el test)

Mutation Score: 33% (1 de 3 detectados)

```java
// Tests robustos - cubren todos los casos
@Test
void testAbsoluto_Negativo() {
    assertEquals(5, absoluto(-5));
}

@Test
void testAbsoluto_Positivo() {
    assertEquals(5, absoluto(5));  // Detecta mutación 2
}

@Test
void testAbsoluto_Cero() {
    assertEquals(0, absoluto(0));  // Detecta mutación 1
}
```

Mutation Score: 100% (3 de 3 detectados)

:::{important}
PIT mide la **calidad de los tests**, no la calidad del código. Un Mutation Score alto indica que los tests son efectivos en detectar cambios incorrectos.
:::

### 8. ArchUnit: Reglas de Arquitectura

**ArchUnit** permite escribir **tests sobre la estructura** del código, no sobre su comportamiento.

#### Filosofía

El código debe seguir reglas arquitectónicas (capas, dependencias, convenciones). ArchUnit permite **automatizar** la verificación de estas reglas mediante tests.

#### Tipos de reglas

**1. Encapsulamiento**

```java
@Test
void atributosDebenSerPrivados() {
    fields()
        .that().areDeclaredInClassesThat().resideInPackage("ar.unrn..")
        .should().bePrivate()
        .check(classes);
}
```

**2. Dependencias entre capas**

```java
@Test
void modeloNoDebeDependerDeVista() {
    noClasses()
        .that().resideInPackage("..modelo..")
        .should().dependOnClassesThat().resideInPackage("..vista..")
        .check(classes);
}
```

**3. Convenciones de nombrado**

```java
@Test
void testsDebenTerminarEnTest() {
    classes()
        .that().resideInPackage("..test..")
        .should().haveSimpleNameEndingWith("Test")
        .check(classes);
}
```

**4. Uso de anotaciones**

```java
@Test
void clasesConEqualsDebenTenerHashCode() {
    classes()
        .that().overrideMethod(Object.class, "equals", Object.class)
        .should().overrideMethod(Object.class, "hashCode")
        .check(classes);
}
```

#### Ventajas

1. **Automatización**: No dependés de revisiones manuales
2. **Regresión**: Las reglas no se olvidan ni se ignoran
3. **Documentación**: Las reglas documentan la arquitectura
4. **CI**: Se ejecutan automáticamente en cada build

:::{tip}
ArchUnit permite automatizar la verificación de las reglas de estilo de la cátedra (serie 0x2xxx). Si una regla se puede expresar como código, ArchUnit puede verificarla.
:::

## Integración: analyzeAll y Dredd

### Tarea analyzeAll

El proyecto incluye una tarea que ejecuta **todas** las herramientas en secuencia y genera un reporte consolidado.

Para instrucciones de uso, ver [la guía de Gradle](../guias/guia_gradle.md).

### Reporte Dredd

**Dredd** es una tarea que consolida los resultados de todas las herramientas en un solo documento Markdown.

El reporte incluye:

- Violaciones de Checkstyle
- Problemas de PMD
- Bugs de SpotBugs
- Cobertura de JaCoCo
- Resultados de tests

Ubicación: `build/reports/dredd.md`

## Filosofía General: Defensa en Profundidad

Cada herramienta detecta un tipo diferente de problema. Juntas forman **capas de defensa**:

```
Error Prone
    ↓ (previene bugs en compilación)
Checkstyle + PMD
    ↓ (detectan problemas de estilo y patrones)
SpotBugs + NullAway
    ↓ (detectan bugs en bytecode)
Tests + JaCoCo + PIT
    ↓ (verifican comportamiento)
ArchUnit
    ↓ (verifica arquitectura)
```

Si un problema pasa una capa, otra lo detecta.

## Resumen

| Herramienta     | Analiza       | Detecta                | Cuándo        |
| :-------------- | :------------ | :--------------------- | :------------ |
| **Checkstyle**  | Código fuente | Estilo y formato       | Build         |
| **PMD**         | Código fuente | Patrones problemáticos | Build         |
| **SpotBugs**    | Bytecode      | Bugs comunes           | Build         |
| **Error Prone** | Compilación   | Errores frecuentes     | Compilación   |
| **NullAway**    | Compilación   | Posibles NPE           | Compilación   |
| **JaCoCo**      | Ejecución     | Cobertura de tests     | Tests         |
| **PIT**         | Ejecución     | Calidad de tests       | Tests (lento) |
| **ArchUnit**    | Estructura    | Reglas arquitectónicas | Tests         |

:::{important}
**Estas herramientas no son obstáculos, son aliados.** Su objetivo es ayudarte a escribir mejor código y aprender buenas prácticas que usarás en tu carrera profesional.
:::

## Para Profundizar

### Conceptos avanzados

- **Análisis de flujo de datos**: Cómo las herramientas rastrean valores
- **Abstract Syntax Tree (AST)**: Representación del código para análisis
- **Bytecode**: Código compilado que analiza SpotBugs
- **Mutant equivalents**: Mutantes que no cambian comportamiento
- **False positives**: Cuando la herramienta reporta un problema que no existe

### Documentación oficial

- [Checkstyle](https://checkstyle.org/)
- [PMD](https://pmd.github.io/)
- [SpotBugs](https://spotbugs.github.io/)
- [Error Prone](https://errorprone.info/)
- [NullAway](https://github.com/uber/NullAway)
- [JaCoCo](https://www.jacoco.org/)
- [PIT](https://pitest.org/)
- [ArchUnit](https://www.archunit.org/)

## Ejercicios Conceptuales

````exercise
:label: ej-herramientas-1

Analizá este código y determiná qué problemas detectaría cada herramienta:

```java
public class gestor {
    private String nombre;
    public void procesar(List<String> lista){
        for(String item:lista){
            if(item=="admin"){lista.remove(item);}
        }
    }
}
````

Indicá para cada herramienta (Checkstyle, PMD, SpotBugs, Error Prone) qué problemas específicos reportaría.

````

```exercise
:label: ej-herramientas-2

Explicá la diferencia entre:
1. Cobertura de código de 100%
2. Mutation Score de 100%

¿Cuál es más importante? ¿Por qué?
````

```exercise
:label: ej-herramientas-3

Diseñá una regla de ArchUnit que verifique:
"Todas las clases en el paquete `controlador` deben terminar en `Controller`"

Escribí el código conceptual de cómo sería esta regla.
```
