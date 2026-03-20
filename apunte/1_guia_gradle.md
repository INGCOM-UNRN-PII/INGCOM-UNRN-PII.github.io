---
title: Guía Práctica de Gradle
description: Guía paso a paso para trabajar con Gradle en los proyectos de Programación II.
---

# Guía Práctica de Gradle

## Introducción

**Gradle** es el sistema de automatización de construcción que utilizamos en todos los trabajos prácticos de la cátedra. Esta guía está diseñada para que puedas trabajar efectivamente con Gradle desde el primer día, sin necesidad de ser un experto.

:::{important}
No necesitás instalar Gradle manualmente. El proyecto incluye el **Gradle Wrapper** que se encarga de descargar y usar la versión correcta automáticamente.
:::

## ¿Qué es Gradle?

Gradle es una herramienta que **automatiza tareas repetitivas** del desarrollo de software:

- **Compilar** el código Java
- **Ejecutar** la aplicación
- **Ejecutar tests** automáticamente
- **Verificar** la calidad del código
- **Generar reportes** de análisis
- **Gestionar dependencias** (librerías externas)

Antes de Gradle, tenías que hacer todo esto manualmente. Con Gradle, un solo comando hace todo el trabajo.

## Estructura del Proyecto

Todos los trabajos prácticos siguen esta estructura estándar:

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

:::{warning}
**¡MUY IMPORTANTE!** No cambies la estructura de directorios. Gradle espera que el código esté exactamente en estos lugares:

- Código de producción: `src/main/java/ar/unrn/`
- Código de tests: `src/test/java/ar/unrn/`
- Informes: `informes/`

Si ponés código en otro lugar, **no funcionará**.
:::

## Gradle Wrapper: Tu Mejor Amigo

El **Gradle Wrapper** son los scripts `gradlew` (Linux/Mac) y `gradlew.bat` (Windows) en la raíz del proyecto.

### ¿Por qué usar el Wrapper?

1. **No necesitás instalar Gradle**: El wrapper lo descarga automáticamente
2. **Versión correcta**: Todos usan exactamente la misma versión
3. **Funciona en cualquier computadora**: Sin configuración adicional

### Cómo usar el Wrapper

```bash
# En Linux / Mac / Git Bash
./gradlew <comando>

# En Windows PowerShell / CMD
gradlew <comando>

# En Windows con Git Bash (recomendado)
./gradlew <comando>
```

:::{tip}
**En Windows**, si usás Git Bash (recomendado), usá `./gradlew`. Si usás PowerShell o CMD, usá `gradlew` sin el `./`.

En esta guía usaremos `./gradlew` en todos los ejemplos.
:::

## Comandos Básicos de Gradle

Estos son los comandos que vas a usar en el día a día:

### Compilar el proyecto

```bash
./gradlew build
```

Este comando:

- Compila todo el código Java
- Ejecuta todos los tests
- Ejecuta todas las herramientas de verificación (Checkstyle, PMD, etc.)
- Genera reportes

**Usalo antes de entregar** para asegurarte que todo funciona.

### Ejecutar tests

```bash
./gradlew test
```

Este comando:

- Compila el código (si es necesario)
- Ejecuta todos los tests de JUnit
- Genera reporte de tests

**Usalo frecuentemente** mientras desarrollás para verificar que tus tests pasan.

### Limpiar archivos generados

```bash
./gradlew clean
```

Este comando:

- Elimina el directorio `build/` completo
- Borra todos los archivos compilados y reportes

**Usalo cuando** querés empezar "de cero" o tenés problemas raros de compilación.

### Ejecutar la aplicación

```bash
./gradlew run
```

Este comando:

- Compila el código (si es necesario)
- Ejecuta la aplicación principal

:::{note}
Para que esto funcione, el proyecto debe tener configurada una clase principal con método `main`. En los TPs, la clase principal suele llamarse `LoaderApp`.
:::

### Ver todas las tareas disponibles

```bash
./gradlew tasks
```

Muestra todas las tareas que podés ejecutar. Muy útil cuando querés ver qué más podés hacer.

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

### Durante el desarrollo de un ejercicio

```bash
# 1. Crear la clase en src/main/java/ar/unrn/
# 2. Crear el test en src/test/java/ar/unrn/

# 3. Ejecutar tests frecuentemente
./gradlew test

# 4. Ver el reporte si algo falla
# Se abre automáticamente en: build/reports/tests/test/index.html
```

### Antes de hacer commit

```bash
# Verificar que todo compila y pasa los tests
./gradlew build

# Si todo está OK, hacer commit
git add .
git commit -m "Completo ejercicio X"
git push
```

### Cuando algo no funciona

```bash
# Limpiar todo y empezar de nuevo
./gradlew clean

# Intentar compilar nuevamente
./gradlew build
```

## Comandos de Verificación

El proyecto incluye herramientas de análisis de código que Gradle ejecuta automáticamente.

### Checkstyle: Verificar estilo

```bash
# Verificar estilo del código de producción
./gradlew checkstyleMain

# Verificar estilo del código de tests
./gradlew checkstyleTest

# Ver reporte
open build/reports/checkstyle/main.html
```

**¿Qué verifica?**

- Indentación correcta (4 espacios)
- Espacios alrededor de operadores
- Javadoc en métodos públicos
- Nombres de variables y métodos
- Y más...

### PMD: Analizar código

```bash
# Analizar código de producción
./gradlew pmdMain

# Ver reporte
open build/reports/pmd/main.html
```

**¿Qué detecta?**

- Variables no utilizadas
- Código duplicado
- Métodos muy largos
- Complejidad alta
- Y más...

### SpotBugs: Detectar bugs

```bash
# Buscar bugs en código de producción
./gradlew spotbugsMain

# Ver reporte
open build/reports/spotbugs/main.html
```

**¿Qué encuentra?**

- Comparaciones incorrectas
- Posibles NullPointerException
- Recursos no cerrados
- Y más...

### Análisis completo

```bash
# Ejecutar TODAS las verificaciones + tests
./gradlew analyzeAll

# Ver reporte consolidado
open build/reports/dredd.md
```

Este comando ejecuta:

- Compilación
- Tests
- Checkstyle
- PMD
- SpotBugs
- Cobertura de tests
- Y genera un reporte unificado

:::{tip}
Ejecutá `./gradlew analyzeAll` antes de entregar el TP para asegurarte que todo está perfecto.
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

### Error: "Permission denied" al ejecutar gradlew

**Problema**: En Linux/Mac, el script no tiene permisos de ejecución.

**Solución**:

```bash
chmod +x gradlew
./gradlew build
```

### Error: "Cannot find symbol" al compilar

**Problema**: La clase está en el lugar incorrecto o el paquete es incorrecto.

**Solución**:

1. Verificá que el archivo esté en `src/main/java/ar/unrn/`
2. Verificá que la primera línea sea: `package ar.unrn;`
3. Si moviste el archivo, limpiá y recompilá:

```bash
./gradlew clean build
```

### Error: Tests no se encuentran

**Problema**: Los tests están en el lugar incorrecto o no terminan en `Test`.

**Solución**:

1. Verificá que el archivo esté en `src/test/java/ar/unrn/`
2. Verificá que la clase termine en `Test`: `CalculadoraTest`
3. Verificá que tenga la anotación `@Test` en los métodos

### Error: "Gradle daemon disappeared unexpectedly"

**Problema**: El proceso de Gradle se colgó.

**Solución**:

```bash
./gradlew --stop
./gradlew build
```

### Error: "Could not resolve all dependencies"

**Problema**: Gradle no puede descargar librerías (problema de red).

**Solución**:

1. Verificá tu conexión a internet
2. Intentá nuevamente:

```bash
./gradlew build --refresh-dependencies
```

### IntelliJ no reconoce las clases

**Problema**: IntelliJ no sincronizó con Gradle.

**Solución**:

1. Click derecho en `build.gradle`
2. "Reload Gradle Project"
3. O desde el panel de Gradle: click en el ícono de "reload" (↻)

## Trabajando sin IntelliJ (solo terminal)

Si por alguna razón no podés usar IntelliJ, podés trabajar completamente desde la terminal.

### Crear un nuevo archivo Java

```bash
# 1. Crear el directorio si no existe
mkdir -p src/main/java/ar/unrn

# 2. Crear el archivo
nano src/main/java/ar/unrn/MiClase.java
# o usar cualquier editor: vim, gedit, code, etc.
```

### Compilar y ejecutar

```bash
# Compilar
./gradlew build

# Ejecutar tests
./gradlew test

# Ejecutar aplicación
./gradlew run
```

### Ver errores de compilación

```bash
# Los errores se muestran en la terminal
./gradlew build

# Si querés más detalle
./gradlew build --stacktrace
```

## Tips y Trucos

### Ejecutar tareas en paralelo

```bash
# Más rápido en computadoras con múltiples núcleos
./gradlew build --parallel
```

### Ver qué está haciendo Gradle

```bash
# Modo verbose - muestra más información
./gradlew build --info

# Modo debug - muestra TODO
./gradlew build --debug
```

### Ejecutar solo tests específicos

```bash
# Ejecutar solo una clase de test
./gradlew test --tests CalculadoraTest

# Ejecutar solo un método de test
./gradlew test --tests CalculadoraTest.testSumar_ConDosNumeros_RetornaSuma
```

### No ejecutar tests al compilar

```bash
# Útil cuando solo querés compilar rápido
./gradlew build -x test
```

### Ver dependencias del proyecto

```bash
# Ver todas las librerías que usa el proyecto
./gradlew dependencies
```

### Actualizar el wrapper de Gradle

```bash
# Si te piden actualizar la versión de Gradle
./gradlew wrapper --gradle-version 8.5
```

## Configuración Avanzada (Opcional)

### Configurar Java en el sistema

Gradle usa el JDK configurado en el sistema. Para verificar:

```bash
# Ver versión de Java
java --version

# Ver dónde está instalado Java
echo $JAVA_HOME  # Linux/Mac
echo %JAVA_HOME% # Windows
```

Si necesitás cambiar la versión de Java:

```bash
# Linux/Mac - agregar a ~/.bashrc o ~/.zshrc
export JAVA_HOME=/ruta/a/jdk-25

# Windows - Variables de entorno del sistema
# Panel de Control → Sistema → Variables de entorno
```

### Configurar Gradle Daemon

El daemon de Gradle mantiene un proceso en background para compilaciones más rápidas.

```bash
# Ver status del daemon
./gradlew --status

# Detener todos los daemons
./gradlew --stop

# Deshabilitar daemon (no recomendado)
./gradlew build --no-daemon
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
open build/reports/tests/test/index.html      # Ver tests
open build/reports/checkstyle/main.html       # Ver estilo
open build/reports/pmd/main.html              # Ver PMD
open build/reports/jacoco/test/html/index.html # Ver cobertura
open build/reports/dredd.md                   # Ver consolidado

# === UTILIDADES ===
./gradlew --stop             # Detener daemon
./gradlew build --info       # Más información
./gradlew build -x test      # Sin ejecutar tests
./gradlew test --tests CalculadoraTest  # Test específico

# === SOLUCIÓN DE PROBLEMAS ===
chmod +x gradlew             # Dar permisos (Linux/Mac)
./gradlew clean build        # Limpiar y recompilar
./gradlew build --refresh-dependencies  # Recargar librerías
```

## Checklist para Entregar un TP

Antes de entregar, ejecutá estos comandos en orden:

```bash
# 1. Limpiar todo
./gradlew clean

# 2. Análisis completo
./gradlew analyzeAll

# 3. Si todo pasó OK, revisar el reporte
open build/reports/dredd.md

# 4. Hacer commit y push
git status
git add .
git commit -m "TP completo - todos los tests pasan"
git push
```

:::{important}
**Criterio de entrega**: El comando `./gradlew build` debe completar exitosamente sin errores. Si hay errores, el TP no se considera completo.
:::

## Preguntas Frecuentes

### ¿Tengo que instalar Gradle?

**No.** El proyecto incluye el Gradle Wrapper que descarga automáticamente la versión correcta.

### ¿Puedo usar otra versión de Java?

El proyecto está configurado para **Java 25**. Otras versiones pueden no funcionar correctamente.

### ¿Por qué tarda tanto la primera vez?

La primera vez, Gradle:

1. Se descarga a sí mismo
2. Descarga todas las librerías del proyecto
3. Configura todo

Las siguientes veces es **mucho más rápido**.

### ¿Qué hago si Gradle se cuelga?

```bash
# Detener todos los procesos de Gradle
./gradlew --stop

# Intentar nuevamente
./gradlew build
```

### ¿Puedo ejecutar Gradle desde cualquier directorio?

**No.** Tenés que estar en la raíz del proyecto (donde está `build.gradle`).

```bash
# Verificar que estás en el lugar correcto
ls build.gradle

# Si no ves build.gradle, navegá al directorio correcto
cd ruta/al/proyecto
```

### ¿Qué significa "FAILED"?

Cuando ves `BUILD FAILED`, significa que algo salió mal:

- Tests fallaron
- Código no compila
- Verificaciones de estilo fallaron

Lee el mensaje de error que aparece antes de `BUILD FAILED` para saber qué arreglar.

### ¿Cómo sé si mi código está bien?

Si `./gradlew build` termina con `BUILD SUCCESSFUL`, tu código:

- Compila correctamente ✓
- Todos los tests pasan ✓
- No tiene errores de estilo críticos ✓
- No tiene bugs detectables ✓

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
