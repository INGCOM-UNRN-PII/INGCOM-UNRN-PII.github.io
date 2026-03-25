---
title: Guía Práctica de Gradle
description: Guía paso a paso para trabajar con Gradle en los proyectos de Programación II.
---

# Guía Práctica de Gradle

## Introducción

**Gradle** es el sistema de automatización de construcción que utilizamos en todos los trabajos prácticos de la cátedra. Si venís de trabajar con C, pensá en Gradle como un **`make` potenciado**: automatiza la compilación, pero además gestiona librerías, ejecuta tests y analiza la calidad del código.

Esta guía está diseñada para que puedas trabajar efectivamente con Gradle desde el primer día, sin necesidad de ser un experto.

:::{important}
No necesitás instalar Gradle manualmente. El proyecto incluye el **Gradle Wrapper** que se encarga de descargar y usar la versión correcta automáticamente.
:::

## ¿Qué es Gradle?

Gradle es una herramienta de **automatización de construcción** (*build automation tool*). ¿Qué significa esto? Cuando desarrollás software, hay muchas tareas que tenés que hacer repetidamente:

1. **Compilar** el código fuente a bytecode ejecutable
2. **Descargar** librerías externas que tu código necesita
3. **Ejecutar tests** para verificar que todo funciona
4. **Empaquetar** la aplicación para distribuirla
5. **Verificar** que el código cumple estándares de calidad

Sin una herramienta de automatización, tendrías que hacer cada una de estas tareas manualmente, recordando el orden correcto y los comandos específicos. Gradle automatiza todo esto.

### Comparación con Make (de C)

Si trabajaste con C, probablemente usaste `make` y archivos `Makefile`. Gradle cumple un rol similar pero más amplio:

| Aspecto | Make (C) | Gradle (Java) |
|---------|----------|---------------|
| Archivo de configuración | `Makefile` | `build.gradle` |
| Compilación | Invoca `gcc` directamente | Invoca `javac` internamente |
| Dependencias | Manejo manual o con `pkg-config` | Descarga automática de repositorios |
| Tests | Configuración manual | Integrado con JUnit |
| Convenciones | Flexibles, definidas por el usuario | Estructura estándar predefinida |

La diferencia clave es que Gradle sigue el principio de **convención sobre configuración**: si ponés los archivos en los lugares esperados, Gradle sabe qué hacer con ellos sin que tengas que explicárselo.

### ¿Qué automatiza Gradle?

Gradle automatiza las siguientes tareas del desarrollo:

- **Compilar** el código Java → Transforma archivos `.java` en archivos `.class` (bytecode)
- **Ejecutar** la aplicación → Inicia la JVM con la clase principal
- **Ejecutar tests** automáticamente → Corre todas las pruebas unitarias y genera reportes
- **Verificar** la calidad del código → Analiza estilo, detecta bugs potenciales, mide cobertura
- **Generar reportes** de análisis → Crea documentos HTML con resultados
- **Gestionar dependencias** (librerías externas) → Descarga JARs de repositorios como Maven Central

Con Gradle, un solo comando hace todo el trabajo.

## Estructura del Proyecto

Todos los trabajos prácticos siguen una **estructura estándar** definida por Gradle. Esta estructura no es arbitraria: es una convención que Gradle (y la mayoría de herramientas Java) esperan encontrar.

### Visión general

```
tp1-2026-usuario/
├── build.gradle              # Configuración del proyecto (¡NO MODIFICAR!)
├── settings.gradle           # Nombre del proyecto
├── gradle/                   # Archivos internos de Gradle
│   └── wrapper/
├── gradlew                   # Script de Gradle para Linux/Mac
├── gradlew.bat              # Script de Gradle para Windows
├── config/                   # Configuraciones de herramientas
│   ├── checkstyle/
│   ├── pmd/
│   └── ...
├── informes/                 # Tus informes en Markdown
│   └── ejercicio1.md
└── src/
    ├── main/
    │   └── java/
    │       └── ar/
    │           └── unrn/
    │               ├── HolaApp.java         # Aplicaciones
    │               ├── Calculadora.java     # Clases de negocio
    │               └── ...
    └── test/
        └── java/
            └── ar/
                └── unrn/
                    └── CalculadoraTest.java # Tests
```

### Explicación de cada componente

**Archivos de configuración de Gradle:**

| Archivo | Propósito | ¿Modificar? |
|---------|-----------|-------------|
| `build.gradle` | Define las dependencias, plugins y tareas del proyecto | ❌ NO |
| `settings.gradle` | Define el nombre del proyecto y subproyectos | ❌ NO |
| `gradle/wrapper/` | Contiene el JAR del wrapper y su configuración | ❌ NO |
| `gradlew` | Script Bash para ejecutar Gradle en Linux/Mac | ❌ NO |
| `gradlew.bat` | Script Batch para ejecutar Gradle en Windows | ❌ NO |

**Directorios de código:**

| Directorio | Contenido | Descripción |
|------------|-----------|-------------|
| `src/main/java/` | Código de producción | El código "real" de tu aplicación |
| `src/test/java/` | Código de tests | Tests unitarios que verifican el código de producción |
| `src/main/resources/` | Recursos de producción | Archivos de configuración, imágenes, etc. |
| `src/test/resources/` | Recursos de tests | Archivos necesarios para los tests |

**Directorios adicionales del proyecto:**

| Directorio | Propósito |
|------------|-----------|
| `config/` | Configuraciones de herramientas de análisis (Checkstyle, PMD, etc.) |
| `informes/` | Tus informes en formato Markdown |
| `build/` | **Generado automáticamente** - contiene archivos compilados y reportes |

### El directorio `src/` en detalle

La estructura `src/main/java/ar/unrn/` puede parecer excesiva si venís de C, donde simplemente ponías archivos `.c` en un directorio. Esta estructura tiene una razón:

```
src/
├── main/                    # Código de producción
│   └── java/               # Código Java (podría haber otros lenguajes)
│       └── ar/             # Paquete raíz: "ar" (Argentina)
│           └── unrn/       # Subpaquete: "unrn" (Universidad)
│               └── *.java  # Tus clases Java
└── test/                    # Código de tests
    └── java/               # Tests en Java
        └── ar/
            └── unrn/
                └── *Test.java  # Tus clases de test
```

**¿Por qué `ar/unrn/`?** Es el **paquete** (*package*) de tus clases. Los paquetes en Java organizan el código y evitan conflictos de nombres. Por convención, se usa el dominio de internet invertido: `unrn.edu.ar` → `ar.edu.unrn` (simplificado a `ar.unrn` en la cátedra).

:::{warning}
**¡MUY IMPORTANTE!** No cambies la estructura de directorios. Gradle espera que el código esté exactamente en estos lugares:

- Código de producción: `src/main/java/ar/unrn/`
- Código de tests: `src/test/java/ar/unrn/`
- Informes: `informes/`

Si ponés código en otro lugar, **no funcionará**. Gradle no encontrará los archivos y la compilación fallará.
:::

### Comparación con la estructura de C

En C, probablemente organizabas tu proyecto así:

```
proyecto_c/
├── Makefile
├── src/
│   ├── main.c
│   ├── calculadora.c
│   └── calculadora.h
└── tests/
    └── test_calculadora.c
```

En Java con Gradle, la estructura es más profunda pero más organizada:

```
proyecto_java/
├── build.gradle
├── src/main/java/ar/unrn/
│   ├── MainApp.java        # Equivalente a main.c
│   └── Calculadora.java    # Equivalente a calculadora.c + calculadora.h
└── src/test/java/ar/unrn/
    └── CalculadoraTest.java # Equivalente a test_calculadora.c
```

La diferencia principal es que en Java no hay archivos de cabecera (`.h`): la interfaz pública está definida en la misma clase.

## Gradle Wrapper: Tu Mejor Amigo

El **Gradle Wrapper** son los scripts `gradlew` (Linux/Mac) y `gradlew.bat` (Windows) en la raíz del proyecto. Son la forma recomendada de ejecutar Gradle.

### ¿Qué es el Wrapper?

El wrapper es un mecanismo que permite ejecutar Gradle **sin tenerlo instalado** en el sistema. Funciona así:

1. El proyecto incluye un pequeño JAR (`gradle/wrapper/gradle-wrapper.jar`)
2. Este JAR sabe qué versión de Gradle necesita el proyecto (definida en `gradle-wrapper.properties`)
3. La primera vez que ejecutás `./gradlew`, descarga esa versión específica
4. Las siguientes veces, usa la versión ya descargada

### ¿Por qué usar el Wrapper?

1. **No necesitás instalar Gradle**: El wrapper lo descarga automáticamente la primera vez
2. **Versión correcta garantizada**: Todos los desarrolladores usan exactamente la misma versión
3. **Reproducibilidad**: El proyecto se compila igual en cualquier computadora
4. **Funciona en cualquier computadora**: Sin configuración adicional del entorno

Pensalo como si el `Makefile` incluyera su propia versión de `make`. En el mundo de C, esto no existe y es fuente de muchos problemas de "funciona en mi máquina".

### Cómo usar el Wrapper

```bash
# En Linux / Mac / Git Bash
./gradlew <comando>

# En Windows PowerShell / CMD
gradlew <comando>

# En Windows con Git Bash (recomendado)
./gradlew <comando>
```

El `./` es necesario en sistemas Unix porque el directorio actual no está en el PATH por defecto (medida de seguridad). En Windows, el sistema busca en el directorio actual automáticamente.

:::{tip}
**En Windows**, si usás Git Bash (recomendado), usá `./gradlew`. Si usás PowerShell o CMD, usá `gradlew` sin el `./`.

En esta guía usaremos `./gradlew` en todos los ejemplos.
:::

### La primera ejecución

La primera vez que ejecutás `./gradlew` en un proyecto nuevo, verás algo como:

```
Downloading https://services.gradle.org/distributions/gradle-8.5-bin.zip
..........10%..........20%..........30%...
```

Esto es normal. Gradle se está descargando. Una vez descargado, se guarda en `~/.gradle/` y no se vuelve a descargar (a menos que el proyecto requiera otra versión).

## Comandos Básicos de Gradle

Estos son los comandos que vas a usar en el día a día. Cada comando se llama **tarea** (*task*) en la terminología de Gradle.

### El modelo de tareas de Gradle

Gradle organiza el trabajo en **tareas** (*tasks*). Cada tarea hace algo específico:

- `compileJava` → Compila el código fuente
- `test` → Ejecuta los tests
- `build` → Ejecuta múltiples tareas en orden

Las tareas tienen **dependencias**: por ejemplo, `test` depende de `compileJava` (no podés testear código sin compilar antes). Gradle resuelve estas dependencias automáticamente.

### Compilar el proyecto

```bash
./gradlew build
```

Este es el comando más completo. Equivale a hacer `make all` en C. Ejecuta:

1. **Compilación** del código fuente (`compileJava`)
2. **Procesamiento** de recursos (`processResources`)
3. **Compilación** de tests (`compileTestJava`)
4. **Ejecución** de todos los tests (`test`)
5. **Verificaciones** de calidad (Checkstyle, PMD, etc.)
6. **Empaquetado** en JAR (`jar`)

Si cualquier paso falla, Gradle se detiene y muestra el error.

**Usalo antes de entregar** para asegurarte que todo funciona.

### Ejecutar tests

```bash
./gradlew test
```

Este comando se enfoca específicamente en los tests:

1. Compila el código de producción (si cambió)
2. Compila el código de tests (si cambió)
3. Ejecuta todos los tests de JUnit
4. Genera un reporte HTML con los resultados

El reporte se genera en `build/reports/tests/test/index.html`. Si algún test falla, Gradle muestra un resumen en la terminal y te indica dónde ver el detalle.

**Usalo frecuentemente** mientras desarrollás para verificar que tus tests pasan.

:::{note}
Gradle es **incremental**: si no cambiaste nada desde la última vez, no recompila ni re-ejecuta tests. Verás `UP-TO-DATE` junto a las tareas que no necesitaron ejecutarse.
:::

### Limpiar archivos generados

```bash
./gradlew clean
```

Este comando elimina todo lo generado:

- El directorio `build/` completo
- Todos los archivos `.class` compilados
- Todos los reportes generados
- El JAR empaquetado

Es el equivalente a `make clean` en C. Después de `clean`, Gradle tiene que recompilar todo desde cero.

**Usalo cuando:**
- Querés empezar "de cero"
- Tenés problemas raros de compilación que no se explican
- Sospechás que hay archivos viejos causando conflictos

### Ejecutar la aplicación

```bash
./gradlew run
```

Este comando ejecuta la aplicación:

1. Compila el código (si es necesario)
2. Encuentra la clase principal (con método `main`)
3. Inicia la JVM y ejecuta el programa

:::{note}
Para que esto funcione, el proyecto debe tener configurada una clase principal con método `main`. En los TPs, la clase principal suele llamarse `LoaderApp` o similar.

La clase principal se configura en `build.gradle`:
```groovy
application {
    mainClass = 'ar.unrn.LoaderApp'
}
```
:::

### Ver todas las tareas disponibles

```bash
./gradlew tasks
```

Muestra todas las tareas organizadas por categoría:

```
Build tasks
-----------
build - Assembles and tests this project.
clean - Deletes the build directory.
jar - Assembles a jar archive containing the main classes.

Verification tasks
------------------
check - Runs all checks.
test - Runs the unit tests.
checkstyleMain - Run Checkstyle analysis for main classes.
...
```

Muy útil cuando querés ver qué más podés hacer o no recordás el nombre exacto de una tarea.

### Combinando tareas

Podés ejecutar múltiples tareas en un solo comando:

```bash
# Limpiar y luego compilar todo
./gradlew clean build

# Solo compilar sin tests (más rápido)
./gradlew build -x test

# Limpiar, compilar y ejecutar
./gradlew clean build run
```

La opción `-x` excluye una tarea. Es útil cuando querés compilar rápido sin esperar los tests.

## Trabajando con IntelliJ IDEA

IntelliJ IDEA tiene **integración nativa con Gradle**. Podés hacer casi todo desde el IDE sin usar la terminal.

### Panel de Gradle

1. Abrí el panel de Gradle: **View → Tool Windows → Gradle**
2. Verás todas las tareas disponibles organizadas por categoría
3. Hacé doble clic en cualquier tarea para ejecutarla

```{figure} 1/gradle_panel_idea.png
:label: fig-gradle-panel
:align: center
:width: 80%

Panel de Gradle en IntelliJ IDEA mostrando todas las tareas disponibles.
```

### Ejecutar tests desde el IDE

Hay varias formas:

**Opción 1: Click derecho en el archivo de test**

```
1. Abrir CalculadoraTest.java
2. Click derecho en el editor
3. "Run 'CalculadoraTest'"
```

**Opción 2: Click en el ícono verde**

```
1. Junto a la clase de test hay un ícono verde ▶
2. Click en el ícono
3. "Run 'CalculadoraTest'"
```

**Opción 3: Usando Gradle**

```
1. Abrir panel de Gradle
2. Navegar a: Tasks → verification → test
3. Doble click en "test"
```

:::{tip}
**Recomendación**: Usá las opciones 1 o 2 durante desarrollo (son más rápidas), pero ejecutá la opción 3 antes de entregar para asegurarte que todo pasa con Gradle.
:::

### Ejecutar aplicación desde el IDE

**Opción 1: Click derecho en la clase con main**

```
1. Abrir HolaApp.java (o la clase con main)
2. Click derecho en el editor
3. "Run 'HolaApp.main()'"
```

**Opción 2: Usando Gradle**

```
1. Panel de Gradle → Tasks → application → run
2. Doble click
```

## Estructura de un Archivo Java en el Proyecto

### Aplicaciones (clases con main)

Las aplicaciones **deben** terminar en `App`:

```java
package ar.unrn;

/**
 * Aplicación de ejemplo que saluda.
 */
public class HolaApp {
    public static void main(String[] args) {
        System.out.println("¡Hola, Programación II!");
    }
}
```

**Ubicación**: `src/main/java/ar/unrn/HolaApp.java`

:::{important}
**Convención obligatoria**: Todas las clases con método `main` deben terminar en `App`. Esto ayuda a distinguirlas de las clases de negocio.
:::

### Clases de negocio

Las clases normales **no** terminan en `App`:

```java
package ar.unrn;

/**
 * Calculadora con operaciones básicas.
 */
public class Calculadora {

    /**
     * Suma dos números.
     *
     * @param operando1 primer sumando
     * @param operando2 segundo sumando
     * @return la suma de ambos números
     */
    public int sumar(int operando1, int operando2) {
        return operando1 + operando2;
    }
}
```

**Ubicación**: `src/main/java/ar/unrn/Calculadora.java`

### Clases de test

Las clases de test **deben** terminar en `Test`:

```java
package ar.unrn;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase Calculadora.
 */
public class CalculadoraTest {

    @Test
    void testSumar_ConDosNumeros_RetornaSuma() {
        // Arrange
        Calculadora calc = new Calculadora();

        // Act
        int resultado = calc.sumar(2, 3);

        // Assert
        assertEquals(5, resultado);
    }
}
```

**Ubicación**: `src/test/java/ar/unrn/CalculadoraTest.java`

:::{note}
El paquete `ar.unrn` debe ser **exactamente el mismo** en código de producción y tests. Solo cambia el directorio (`main` vs `test`).
:::

## Ciclo de Trabajo Típico

Esta sección describe el flujo de trabajo recomendado para desarrollar con Gradle. Si seguís este ciclo, vas a evitar la mayoría de los problemas comunes.

### El ciclo Red-Green-Refactor con Gradle

El desarrollo con tests sigue un ciclo conocido como **Red-Green-Refactor**:

1. **Red**: Escribí un test que falla (porque el código todavía no existe)
2. **Green**: Escribí el código mínimo para que el test pase
3. **Refactor**: Mejorá el código manteniendo los tests en verde

Con Gradle, este ciclo se ve así:

```bash
# 1. Escribir el test → ./gradlew test → FALLA (Red)
# 2. Escribir el código → ./gradlew test → PASA (Green)
# 3. Mejorar el código → ./gradlew test → SIGUE PASANDO (Refactor)
```

### Durante el desarrollo de un ejercicio

```bash
# 1. Crear la clase en src/main/java/ar/unrn/
# 2. Crear el test en src/test/java/ar/unrn/

# 3. Ejecutar tests frecuentemente (cada cambio significativo)
./gradlew test

# 4. Ver el reporte si algo falla
# Se genera en: build/reports/tests/test/index.html

# 5. Repetir hasta que todos los tests pasen
```

:::{tip}
Ejecutá tests **frecuentemente**. Es mejor descubrir un problema cuando cambiaste 5 líneas que cuando cambiaste 500.
:::

### Antes de hacer commit

Antes de guardar cambios en Git, asegurate de que todo funciona:

```bash
# Verificar que todo compila y pasa los tests
./gradlew build

# Si todo está OK (BUILD SUCCESSFUL), hacer commit
git add .
git commit -m "Completo ejercicio X"
git push
```

**¿Por qué `build` y no solo `test`?** Porque `build` también ejecuta las verificaciones de estilo y calidad. Es posible que tus tests pasen pero Checkstyle encuentre problemas de formato.

### Cuando algo no funciona

Si tenés errores inexplicables o comportamiento extraño:

```bash
# Paso 1: Limpiar todo
./gradlew clean

# Paso 2: Intentar compilar desde cero
./gradlew build
```

Esto elimina cualquier archivo compilado viejo que pueda estar causando conflictos. Es el equivalente a "apagar y encender de nuevo" pero para la compilación.

### Flujo completo de un TP

```bash
# 1. Clonar el repositorio
git clone <url-del-tp>
cd tp1-2026-usuario

# 2. Verificar que compila (setup inicial)
./gradlew build

# 3. Desarrollar (repetir muchas veces)
# ... editar código ...
./gradlew test

# 4. Antes de cada commit
./gradlew build
git add .
git commit -m "Mensaje descriptivo"

# 5. Subir cambios
git push

# 6. Antes de entregar: análisis completo
./gradlew clean
./gradlew analyzeAll
```

## Comandos de Verificación

El proyecto incluye **herramientas de análisis estático** que verifican tu código sin ejecutarlo. Estas herramientas encuentran problemas que el compilador no detecta: errores de estilo, malas prácticas, bugs potenciales y código difícil de mantener.

### ¿Qué es el análisis estático?

A diferencia de los tests (que ejecutan el código y verifican resultados), el análisis estático **lee el código fuente** y busca patrones problemáticos. Es como tener un revisor de código automático que nunca se cansa.

En C, quizás usaste `cppcheck` o las opciones `-Wall -Wextra` de GCC. En Java, tenemos herramientas más sofisticadas.

### Checkstyle: Verificar estilo de código

**Checkstyle** verifica que el código siga las convenciones de formato definidas por la cátedra.

```bash
# Verificar estilo del código de producción
./gradlew checkstyleMain

# Verificar estilo del código de tests
./gradlew checkstyleTest

# Ver reporte detallado
open build/reports/checkstyle/main.html
```

**¿Qué verifica Checkstyle?**

| Categoría | Ejemplos de reglas |
|-----------|-------------------|
| **Indentación** | 4 espacios (no tabs), alineación de llaves |
| **Espaciado** | Espacios después de comas, alrededor de operadores |
| **Nombres** | `camelCase` para variables, `PascalCase` para clases |
| **Documentación** | Javadoc obligatorio en métodos públicos |
| **Complejidad** | Líneas no muy largas, métodos no muy extensos |
| **Imports** | Sin imports no usados, sin `import *` |

**Ejemplo de error de Checkstyle:**

```
[ERROR] Calculadora.java:15: 'if' is not followed by whitespace. [WhitespaceAfter]
```

Significa que escribiste `if(condicion)` en lugar de `if (condicion)`.

### PMD: Analizar problemas de código

**PMD** busca patrones de código que suelen causar problemas: código muerto, variables sin usar, estructuras confusas.

```bash
# Analizar código de producción
./gradlew pmdMain

# Analizar código de tests
./gradlew pmdTest

# Ver reporte detallado
open build/reports/pmd/main.html
```

**¿Qué detecta PMD?**

| Problema | Descripción |
|----------|-------------|
| **Variables no usadas** | Declaraste una variable pero nunca la usás |
| **Código inalcanzable** | Código después de un `return` que nunca se ejecuta |
| **Complejidad ciclomática alta** | Método con demasiados caminos de ejecución |
| **Código duplicado** | Bloques de código repetidos que deberían ser métodos |
| **Nombres confusos** | Variables de una sola letra (excepto en lazos) |
| **Empty catch blocks** | `catch (Exception e) { }` que ignora errores |

**Ejemplo de error de PMD:**

```
[WARN] Calculadora.java:20: Avoid unused local variables such as 'temp'. [UnusedLocalVariable]
```

### SpotBugs: Detectar bugs potenciales

**SpotBugs** (antes FindBugs) analiza el bytecode compilado buscando patrones que suelen ser bugs.

```bash
# Buscar bugs en código de producción
./gradlew spotbugsMain

# Ver reporte detallado
open build/reports/spotbugs/main.html
```

**¿Qué encuentra SpotBugs?**

| Bug | Descripción |
|-----|-------------|
| **Null pointer dereference** | Usás un objeto que puede ser `null` sin verificar |
| **Comparación incorrecta de strings** | Usás `==` en lugar de `.equals()` para strings |
| **Recursos no cerrados** | Archivos abiertos que nunca se cierran |
| **Infinite loops** | Lazos que nunca terminan |
| **Integer overflow** | Operaciones que pueden desbordar el tipo |
| **Synchronization problems** | Problemas de concurrencia (avanzado) |

**Ejemplo de error de SpotBugs:**

```
[BUG] Comparison of String objects using == or != (ES_COMPARING_STRINGS_WITH_EQ)
```

### JaCoCo: Cobertura de tests

**JaCoCo** mide qué porcentaje de tu código es ejecutado por los tests. No es una verificación de calidad per se, pero te ayuda a identificar código sin testear.

```bash
# Ejecutar tests y medir cobertura
./gradlew test jacocoTestReport

# Ver reporte de cobertura
open build/reports/jacoco/test/html/index.html
```

El reporte muestra líneas en colores:
- **Verde**: Línea ejecutada por tests
- **Rojo**: Línea nunca ejecutada
- **Amarillo**: Parcialmente cubierta (algunas ramas sí, otras no)

### Análisis completo

```bash
# Ejecutar TODAS las verificaciones + tests
./gradlew analyzeAll

# Ver reporte consolidado
open build/reports/dredd.md
```

Este comando ejecuta en orden:

1. **Compilación** del código
2. **Tests** unitarios
3. **Checkstyle** (estilo)
4. **PMD** (análisis de código)
5. **SpotBugs** (bugs potenciales)
6. **JaCoCo** (cobertura)
7. Genera un **reporte unificado** en Markdown

:::{tip}
Ejecutá `./gradlew analyzeAll` antes de entregar el TP para asegurarte que todo está perfecto. Si este comando pasa sin errores, tu código cumple todos los requisitos técnicos.
:::

## Reportes Generados

Después de ejecutar comandos de verificación, Gradle genera reportes en `build/reports/`:

```
build/
└── reports/
    ├── tests/
    │   └── test/
    │       └── index.html         # Resultados de tests
    ├── checkstyle/
    │   ├── main.html              # Reporte de estilo (producción)
    │   └── test.html              # Reporte de estilo (tests)
    ├── pmd/
    │   ├── main.html              # Reporte de PMD (producción)
    │   └── test.html              # Reporte de PMD (tests)
    ├── spotbugs/
    │   └── main.html              # Reporte de SpotBugs
    ├── jacoco/
    │   └── test/
    │       └── html/
    │           └── index.html     # Reporte de cobertura
    └── dredd.md                   # Reporte consolidado
```

### Cómo ver los reportes

**En Linux/Mac**:

```bash
open build/reports/tests/test/index.html
```

**En Windows**:

```bash
start build/reports/tests/test/index.html
```

**Desde IntelliJ**:

1. Panel de proyecto → carpeta `build/reports/`
2. Click derecho en `index.html` → Open in Browser

## Problemas Comunes y Soluciones

Esta sección cubre los errores más frecuentes que vas a encontrar y cómo resolverlos. Guardá esta página en favoritos.

### Error: "Permission denied" al ejecutar gradlew

**Síntoma**:
```
bash: ./gradlew: Permission denied
```

**Causa**: En Linux/Mac, los archivos descargados de internet pierden el permiso de ejecución por seguridad.

**Solución**:
```bash
chmod +x gradlew
./gradlew build
```

El comando `chmod +x` agrega el permiso de ejecución (`x` = execute) al archivo.

### Error: "Cannot find symbol" al compilar

**Síntoma**:
```
error: cannot find symbol
  symbol:   class Calculadora
  location: class CalculadoraTest
```

**Causas posibles**:

1. **La clase no existe**: Verificá que hayas creado el archivo
2. **Nombre incorrecto**: Java es case-sensitive; `Calculadora` ≠ `calculadora`
3. **Ubicación incorrecta**: El archivo no está en `src/main/java/ar/unrn/`
4. **Paquete incorrecto**: Falta `package ar.unrn;` al inicio del archivo

**Solución**:

1. Verificá que el archivo exista: `ls src/main/java/ar/unrn/Calculadora.java`
2. Verificá que la primera línea sea: `package ar.unrn;`
3. Verificá que el nombre de la clase coincida con el nombre del archivo
4. Limpiá y recompilá:

```bash
./gradlew clean build
```

### Error: "package ar.unrn does not exist"

**Síntoma**:
```
error: package ar.unrn does not exist
import ar.unrn.Calculadora;
```

**Causa**: Estás importando una clase que no existe o está en otro paquete.

**Solución**: Verificá que la clase que querés importar:
1. Exista en `src/main/java/ar/unrn/`
2. Tenga `package ar.unrn;` como primera línea (no comentarios antes)
3. Se llame exactamente como la estás importando

### Error: Tests no se encuentran

**Síntoma**:
```
> Task :test
0 tests completed
```

O el test aparece como ignorado/skipped.

**Causas posibles**:

1. La clase de test no termina en `Test`
2. Los métodos de test no tienen `@Test`
3. El archivo está en el lugar incorrecto

**Solución**:

1. Verificá que el archivo esté en `src/test/java/ar/unrn/`
2. Verificá que la clase termine en `Test`: `CalculadoraTest`
3. Verificá cada método de test:

```java
import org.junit.jupiter.api.Test;  // ← Import correcto (JUnit 5)

class CalculadoraTest {
    @Test  // ← Anotación obligatoria
    void testSumar() {
        // ...
    }
}
```

### Error: "Gradle daemon disappeared unexpectedly"

**Síntoma**:
```
The Gradle daemon disappeared unexpectedly (it may have been killed or may have crashed)
```

**Causa**: El proceso de Gradle se colgó o fue terminado por el sistema (falta de memoria, timeout).

**Solución**:

```bash
# Detener todos los daemons de Gradle
./gradlew --stop

# Intentar nuevamente
./gradlew build
```

Si persiste, puede ser un problema de memoria. Probá:

```bash
# Ejecutar sin daemon (más lento pero más estable)
./gradlew build --no-daemon
```

### Error: "Could not resolve all dependencies"

**Síntoma**:
```
Could not resolve all dependencies for configuration ':compileClasspath'.
Could not download junit-jupiter-5.10.0.jar
```

**Causa**: Gradle no puede descargar las librerías de internet.

**Solución**:

1. Verificá tu conexión a internet
2. Si estás detrás de un proxy, configuralo en `~/.gradle/gradle.properties`
3. Intentá forzar la descarga:

```bash
./gradlew build --refresh-dependencies
```

4. Si estás en la universidad, puede ser el firewall. Probá desde otra red.

### Error: "Class has already been declared in this compilation unit"

**Síntoma**:
```
error: class Calculadora is already defined in package ar.unrn
```

**Causa**: Tenés dos archivos con la misma clase, o la clase está definida dos veces.

**Solución**:

```bash
# Buscar archivos duplicados
find src -name "Calculadora.java"
```

Borrá el archivo duplicado o renombrá una de las clases.

### IntelliJ no reconoce las clases

**Síntoma**: El código tiene subrayados rojos pero compila bien con `./gradlew build`.

**Causa**: IntelliJ no sincronizó su índice con los cambios del proyecto.

**Solución**:

**Opción 1: Reimportar Gradle**
1. Click derecho en `build.gradle`
2. "Reload Gradle Project"

**Opción 2: Invalidar caché**
1. File → Invalidate Caches / Restart
2. Seleccionar "Invalidate and Restart"

**Opción 3: Desde el panel de Gradle**
1. Abrir panel de Gradle
2. Click en el ícono de "reload" (↻) arriba a la izquierda

### Error: "Execution failed for task ':checkstyleMain'"

**Síntoma**:
```
> Task :checkstyleMain FAILED
```

**Causa**: Tu código tiene errores de estilo según las reglas configuradas.

**Solución**:

1. Mirá el reporte para ver qué reglas violaste:
```bash
open build/reports/checkstyle/main.html
```

2. Corregí los errores indicados (generalmente son espacios, indentación, o falta de Javadoc)

3. Volvé a ejecutar:
```bash
./gradlew checkstyleMain
```

### Error: "org.gradle.api.GradleException: Spotbugs violations"

**Síntoma**: SpotBugs encontró bugs potenciales en tu código.

**Solución**:

1. Mirá el reporte:
```bash
open build/reports/spotbugs/main.html
```

2. Cada bug tiene una explicación de por qué es problemático
3. Corregí el código según las sugerencias

### La compilación es muy lenta

**Síntomas**: Gradle tarda varios minutos en cada compilación.

**Soluciones**:

```bash
# Usar el daemon (debería estar activo por defecto)
./gradlew build --daemon

# Usar compilación paralela
./gradlew build --parallel

# Configurar más memoria para Gradle (en ~/.gradle/gradle.properties)
org.gradle.jvmargs=-Xmx2g
```

Si tu computadora tiene poca RAM, las herramientas de análisis pueden ser lentas. Podés saltarlas temporalmente:

```bash
# Solo compilar y testear, sin análisis de calidad
./gradlew test
```

Pero recordá ejecutar `./gradlew build` antes de entregar.

## Trabajando sin IntelliJ (solo terminal)

Si por alguna razón no podés usar IntelliJ (estás en un servidor remoto, tu computadora es muy lenta, o simplemente preferís la terminal), podés trabajar completamente desde la línea de comandos.

### Estructura mínima de trabajo

Necesitás un editor de texto y una terminal. Cualquier editor sirve:

- **nano** / **vim** - Editores de terminal (ya instalados en Linux/Mac)
- **VS Code** - Editor gráfico ligero con soporte de terminal integrado
- **gedit** / **kate** - Editores gráficos simples en Linux

### Crear un nuevo archivo Java

```bash
# 1. Asegurarte de estar en la raíz del proyecto
ls build.gradle  # Debería mostrar el archivo

# 2. Crear el directorio si no existe (solo la primera vez)
mkdir -p src/main/java/ar/unrn
mkdir -p src/test/java/ar/unrn

# 3. Crear el archivo con tu editor favorito
nano src/main/java/ar/unrn/MiClase.java
# o
code src/main/java/ar/unrn/MiClase.java
# o
vim src/main/java/ar/unrn/MiClase.java
```

### Plantilla básica de clase

Cuando crees un archivo nuevo, empezá con esta estructura:

```java
package ar.unrn;

/**
 * Descripción breve de la clase.
 */
public class MiClase {
    
    /**
     * Descripción del método.
     *
     * @param parametro descripción del parámetro
     * @return descripción del valor retornado
     */
    public int miMetodo(int parametro) {
        return parametro * 2;
    }
}
```

### Plantilla básica de test

```java
package ar.unrn;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para MiClase.
 */
class MiClaseTest {

    @Test
    void testMiMetodo_CuandoRecibe5_Retorna10() {
        // Arrange
        MiClase objeto = new MiClase();
        
        // Act
        int resultado = objeto.miMetodo(5);
        
        // Assert
        assertEquals(10, resultado);
    }
}
```

### Flujo de trabajo en terminal

```bash
# 1. Editar código
nano src/main/java/ar/unrn/MiClase.java

# 2. Compilar y verificar errores
./gradlew compileJava

# 3. Si hay errores, volver al paso 1

# 4. Editar test
nano src/test/java/ar/unrn/MiClaseTest.java

# 5. Ejecutar tests
./gradlew test

# 6. Si fallan, volver al paso 1 o 4

# 7. Antes de commit, verificar todo
./gradlew build
```

### Ver errores de compilación

Los errores de Gradle se muestran en la terminal. Para entenderlos:

```bash
# Compilación básica (muestra errores principales)
./gradlew build

# Más detalle sobre qué hace Gradle
./gradlew build --info

# Stack trace completo (útil para errores raros)
./gradlew build --stacktrace

# Todo el detalle posible (muy verbose)
./gradlew build --debug
```

### Navegando los archivos del proyecto

```bash
# Ver estructura del proyecto
tree -L 3 src/

# Buscar un archivo
find src -name "*.java" | grep -i calculadora

# Ver contenido de un archivo
cat src/main/java/ar/unrn/Calculadora.java

# Buscar texto en todos los archivos Java
grep -r "public class" src/
```

## Tips y Trucos

Esta sección contiene técnicas avanzadas para trabajar más eficientemente con Gradle.

### Ejecutar tareas en paralelo

Si tu computadora tiene múltiples núcleos de CPU, Gradle puede compilar en paralelo:

```bash
./gradlew build --parallel
```

Para hacerlo permanente, agregá a `~/.gradle/gradle.properties`:

```properties
org.gradle.parallel=true
```

### Ver qué está haciendo Gradle

```bash
# Modo info - muestra tareas y decisiones
./gradlew build --info

# Modo debug - muestra TODO (genera mucha salida)
./gradlew build --debug

# Dry-run - muestra qué haría sin hacerlo
./gradlew build --dry-run
```

### Ejecutar solo tests específicos

No necesitás esperar todos los tests si estás trabajando en uno específico:

```bash
# Ejecutar solo una clase de test
./gradlew test --tests CalculadoraTest

# Ejecutar solo un método de test
./gradlew test --tests "CalculadoraTest.testSumar*"

# Ejecutar tests que coincidan con un patrón
./gradlew test --tests "*Calculadora*"

# Ejecutar tests de múltiples clases
./gradlew test --tests CalculadoraTest --tests OtraClaseTest
```

### Excluir tareas

La opción `-x` excluye tareas del build:

```bash
# Compilar sin ejecutar tests (más rápido)
./gradlew build -x test

# Compilar sin ninguna verificación
./gradlew build -x test -x checkstyleMain -x pmdMain -x spotbugsMain

# Solo compilar el código de producción
./gradlew compileJava
```

### Ver dependencias del proyecto

Las dependencias son librerías externas que tu proyecto usa. Gradle las descarga automáticamente.

```bash
# Ver todas las librerías y sus versiones
./gradlew dependencies

# Ver solo las dependencias de compilación
./gradlew dependencies --configuration compileClasspath

# Ver dependencias en formato árbol (muestra dependencias transitivas)
./gradlew dependencies --configuration runtimeClasspath
```

Las **dependencias transitivas** son librerías que tus dependencias necesitan. Por ejemplo, si usás JUnit 5, JUnit necesita otras librerías internamente, y Gradle las descarga también.

### Actualizar el wrapper de Gradle

Normalmente no necesitás hacer esto (el wrapper ya está configurado). Pero si te piden actualizar:

```bash
# Actualizar a una versión específica
./gradlew wrapper --gradle-version 8.5

# Esto modifica:
# - gradle/wrapper/gradle-wrapper.properties
# - gradle/wrapper/gradle-wrapper.jar
```

## Entendiendo build.gradle

Aunque **no debés modificar** el archivo `build.gradle` de los TPs, es útil entender qué hace. El archivo está escrito en **Groovy**, un lenguaje que corre sobre la JVM.

### Estructura básica

```groovy
// Plugins: agregan funcionalidad a Gradle
plugins {
    id 'java'                    // Soporte para Java
    id 'application'             // Permite ejecutar con 'run'
    id 'checkstyle'             // Análisis de estilo
    id 'pmd'                     // Análisis de código
    id 'com.github.spotbugs'    // Detección de bugs
    id 'jacoco'                  // Cobertura de tests
}

// Repositorios: de dónde descargar dependencias
repositories {
    mavenCentral()              // Repositorio principal de Java
}

// Dependencias: librerías que usa el proyecto
dependencies {
    // Para compilar
    implementation 'com.google.guava:guava:32.1.2-jre'
    
    // Solo para tests
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
}

// Configuración de la aplicación
application {
    mainClass = 'ar.unrn.MainApp'  // Clase con main()
}

// Configuración de Java
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
```

### Tipos de dependencias

| Configuración | Propósito | Disponible en |
|--------------|-----------|---------------|
| `implementation` | Dependencia de producción | Código principal |
| `testImplementation` | Dependencia solo para tests | Código de tests |
| `compileOnly` | Solo para compilar, no en runtime | Anotaciones |
| `runtimeOnly` | Solo en runtime, no para compilar | Drivers JDBC |

### ¿Por qué no modificar build.gradle?

En los TPs, el `build.gradle` está configurado específicamente para:

1. Usar la versión correcta de Java
2. Incluir las herramientas de análisis configuradas
3. Generar los reportes que la cátedra necesita
4. Tener las dependencias necesarias para los ejercicios

Si lo modificás, podrías:
- Romper la compilación
- Desactivar verificaciones importantes
- Hacer que tu código no compile en el servidor de la cátedra

## Configuración Avanzada (Opcional)

Esta sección es para cuando necesitás configurar tu entorno. No es necesaria para trabajar con los TPs.

### Configurar Java en el sistema

Gradle usa el JDK configurado en el sistema. Para verificar:

```bash
# Ver versión de Java instalada
java --version

# Ver dónde está instalado Java (si JAVA_HOME está configurado)
echo $JAVA_HOME  # Linux/Mac
echo %JAVA_HOME% # Windows
```

Si la versión es incorrecta o JAVA_HOME no está configurado:

**En Linux/Mac:**

Agregá a `~/.bashrc` o `~/.zshrc`:

```bash
export JAVA_HOME=/ruta/a/jdk-25
export PATH=$JAVA_HOME/bin:$PATH
```

Luego recargá el archivo: `source ~/.bashrc`

**En Windows:**

1. Panel de Control → Sistema → Configuración avanzada del sistema
2. Variables de entorno
3. Nueva variable de sistema: `JAVA_HOME` = `C:\Program Files\Java\jdk-25`
4. Editar `Path` y agregar `%JAVA_HOME%\bin`

### Configurar Gradle Daemon

El **Gradle Daemon** es un proceso que queda corriendo en background después de la primera ejecución. Las siguientes compilaciones son más rápidas porque no necesitan iniciar una nueva JVM.

```bash
# Ver status de los daemons activos
./gradlew --status

# Detener todos los daemons (libera memoria)
./gradlew --stop

# Ejecutar sin daemon (más lento pero usa menos memoria)
./gradlew build --no-daemon
```

El daemon se detiene automáticamente después de 3 horas de inactividad.

### Archivo gradle.properties

Podés crear `~/.gradle/gradle.properties` para configuración global:

```properties
# Usar más memoria (útil para proyectos grandes)
org.gradle.jvmargs=-Xmx2g

# Habilitar compilación paralela
org.gradle.parallel=true

# Habilitar caché de configuración (más rápido)
org.gradle.configuration-cache=true

# Modo offline (no descarga dependencias)
# org.gradle.offline=true
```

## Comandos de Referencia Rápida

```bash
# === COMANDOS BÁSICOS ===
./gradlew build              # Compilar todo + tests + verificaciones
./gradlew clean              # Limpiar archivos generados
./gradlew test               # Solo ejecutar tests
./gradlew run                # Ejecutar la aplicación
./gradlew tasks              # Ver todas las tareas

# === VERIFICACIONES ===
./gradlew checkstyleMain     # Verificar estilo del código
./gradlew pmdMain            # Analizar código con PMD
./gradlew spotbugsMain       # Detectar bugs
./gradlew analyzeAll         # Análisis completo

# === REPORTES ===
# Linux/Mac:
open build/reports/tests/test/index.html
open build/reports/checkstyle/main.html
open build/reports/pmd/main.html
open build/reports/jacoco/test/html/index.html
# Windows:
start build/reports/tests/test/index.html

# === UTILIDADES ===
./gradlew --stop             # Detener daemon
./gradlew build --info       # Más información
./gradlew build -x test      # Sin ejecutar tests
./gradlew test --tests CalculadoraTest  # Test específico
./gradlew dependencies       # Ver dependencias

# === SOLUCIÓN DE PROBLEMAS ===
chmod +x gradlew             # Dar permisos (Linux/Mac)
./gradlew clean build        # Limpiar y recompilar
./gradlew build --refresh-dependencies  # Recargar librerías
./gradlew build --no-daemon  # Sin daemon (usa menos memoria)
```

## Checklist para Entregar un TP

Antes de entregar, seguí este checklist en orden:

```bash
# 1. Asegurarte de estar en el directorio correcto
ls build.gradle  # Debería mostrar el archivo

# 2. Limpiar todo (empezar de cero)
./gradlew clean

# 3. Análisis completo (esto puede tardar unos minutos)
./gradlew analyzeAll

# 4. Si todo pasó OK (BUILD SUCCESSFUL), revisar el reporte
open build/reports/dredd.md   # Linux/Mac
# o
start build/reports/dredd.md  # Windows

# 5. Verificar estado de Git
git status

# 6. Agregar todos los cambios
git add .

# 7. Hacer commit con mensaje descriptivo
git commit -m "TP completo - todos los ejercicios implementados"

# 8. Subir al repositorio remoto
git push
```

### Lista de verificación manual

Antes del commit, verificá:

- [ ] Todos los archivos `.java` están en `src/main/java/ar/unrn/` o `src/test/java/ar/unrn/`
- [ ] Cada clase tiene `package ar.unrn;` como primera línea (después de comentarios de licencia si los hay)
- [ ] Las clases con `main()` terminan en `App`
- [ ] Las clases de test terminan en `Test`
- [ ] `./gradlew build` termina con `BUILD SUCCESSFUL`
- [ ] Los informes en `informes/` están completos
- [ ] No hay archivos de más (`.class`, `.jar`, temporales de IDE)

:::{important}
**Criterio de entrega**: El comando `./gradlew build` debe completar exitosamente sin errores. Si hay errores, el TP no se considera completo. La cátedra ejecuta este mismo comando para verificar tu entrega.
:::

## Preguntas Frecuentes

### ¿Tengo que instalar Gradle?

**No.** El proyecto incluye el Gradle Wrapper (`gradlew`) que descarga automáticamente la versión correcta de Gradle la primera vez que lo ejecutás.

### ¿Puedo usar otra versión de Java?

El proyecto está configurado para **Java 25**. Otras versiones pueden no funcionar correctamente porque:

- Java 25 tiene características nuevas que versiones anteriores no soportan
- El `build.gradle` especifica la versión exacta
- La cátedra evalúa con Java 25

Si tenés otra versión instalada, podés tener ambas y configurar `JAVA_HOME` para apuntar a Java 25 cuando trabajés en los TPs.

### ¿Por qué tarda tanto la primera vez?

La primera vez que ejecutás Gradle en un proyecto, tiene que:

1. **Descargar Gradle** mismo (si no lo tiene en caché)
2. **Descargar todas las dependencias** (JUnit, herramientas de análisis, etc.)
3. **Configurar el proyecto** (parsear `build.gradle`, resolver dependencias)
4. **Compilar todo** desde cero

Las siguientes veces es **mucho más rápido** porque:
- Gradle ya está descargado
- Las dependencias están en caché
- Solo recompila lo que cambió

### ¿Qué hago si Gradle se cuelga?

```bash
# Detener todos los procesos de Gradle
./gradlew --stop

# Intentar nuevamente
./gradlew build

# Si sigue fallando, probar sin daemon
./gradlew build --no-daemon
```

Si el problema persiste, puede ser falta de memoria. Cerrá otras aplicaciones o aumentá la memoria de Gradle (ver sección de configuración avanzada).

### ¿Puedo ejecutar Gradle desde cualquier directorio?

**No.** Tenés que estar en la raíz del proyecto (donde está `build.gradle`).

```bash
# Verificar que estás en el lugar correcto
ls build.gradle

# Si no ves build.gradle, navegá al directorio correcto
cd ruta/al/proyecto

# Ver en qué directorio estás
pwd
```

### ¿Qué significa "FAILED"?

Cuando ves `BUILD FAILED`, significa que algo salió mal. Las causas más comunes:

| Mensaje | Causa probable |
|---------|---------------|
| `Compilation failed` | Error de sintaxis o clase no encontrada |
| `Test failed` | Un test no pasó |
| `Checkstyle violations` | Error de estilo |
| `PMD rule violations` | Problema de calidad de código |
| `SpotBugs violations` | Bug potencial detectado |

Lee el mensaje de error que aparece **antes** de `BUILD FAILED` para saber exactamente qué arreglar.

### ¿Cómo sé si mi código está bien?

Si `./gradlew build` termina con `BUILD SUCCESSFUL`, tu código:

- ✓ Compila correctamente
- ✓ Todos los tests pasan
- ✓ No tiene errores de estilo críticos
- ✓ No tiene bugs detectables

Esto es el **mínimo requerido** para entregar. La calidad del diseño y la lógica se evalúan por separado.

## Recursos Adicionales

### Documentación oficial

- [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
- [Gradle Build Language Reference](https://docs.gradle.org/current/dsl/)

### En el repositorio de la cátedra

- [Wiki del curso](https://dub.sh/p2/cursada)
- [Discussions - Preguntas y respuestas](https://github.com/orgs/INGCOM-UNRN-PII/discussions)

### Videos recomendados

- [Gradle Tutorial for Beginners](https://www.youtube.com/watch?v=gKPMKRnnbXU) (inglés)
- [IntelliJ IDEA Gradle Tutorial](https://www.youtube.com/watch?v=6V6G3RyxEMk) (inglés)

## Resumen

**Lo mínimo que necesitás saber**:

1. **Estructura**: Código en `src/main/java/ar/unrn/`, tests en `src/test/java/ar/unrn/`
2. **Compilar**: `./gradlew build`
3. **Tests**: `./gradlew test`
4. **Ejecutar**: `./gradlew run`
5. **Limpiar**: `./gradlew clean`
6. **Antes de entregar**: `./gradlew analyzeAll`

:::{tip}
Guardá esta guía en favoritos. La vas a consultar frecuentemente durante la cursada.
:::

## Ejercicios de Práctica

```exercise
:label: ej-gradle-1

Practicá los comandos básicos:

1. Cloná el repositorio de un TP
2. Ejecutá `./gradlew tasks` y leé la salida
3. Ejecutá `./gradlew build`
4. Navegá a `build/reports/tests/test/` y abrí `index.html`
5. Ejecutá `./gradlew clean` y verificá que la carpeta `build/` desapareció
```

```exercise
:label: ej-gradle-2

Creá una clase nueva:

1. Creá `src/main/java/ar/unrn/Saludador.java`
2. Agregá un método `saludar(String nombre)` que retorne un saludo
3. Creá `src/test/java/ar/unrn/SaludadorTest.java`
4. Escribí un test para el método
5. Ejecutá `./gradlew test` y verificá que pase
```

```exercise
:label: ej-gradle-3

Practicá con los reportes:

1. Creá código con un error de estilo (sin espacios después de coma)
2. Ejecutá `./gradlew checkstyleMain`
3. Abrí el reporte HTML y leé el error
4. Corregí el error
5. Ejecutá nuevamente y verificá que pase
```

```exercise
:label: ej-gradle-4

Usá IntelliJ:

1. Abrí el panel de Gradle en IntelliJ
2. Ejecutá la tarea `test` desde el panel
3. Ejecutá la tarea `build` desde el panel
4. Compará los tiempos con ejecutar desde terminal
```
