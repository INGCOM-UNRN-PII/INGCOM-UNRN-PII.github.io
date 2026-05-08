---
title: Herramientas de Análisis y Verificación de Calidad
description: Fundamentos técnicos y pedagógicos de las herramientas de análisis estático, testing y verificación de calidad utilizadas en Programación II.
---

# Herramientas de Análisis y Verificación de Calidad

## Introducción

Escribir código que funcione no es suficiente. El código debe ser **mantenible**, **legible**, **libre de errores comunes** y estar **bien testeado**. Para lograr esto, automatizamos la revisión usando herramientas de análisis que detectan fallas tempranamente.

Todas estas herramientas están integradas en el proceso de construcción mediante Gradle y generan el reporte unificado **Dredd** en `build/reports/dredd.md`.

## Herramientas de Análisis Estático (Código Fuente)

### Checkstyle

- **Qué analiza**: Código fuente (`.java`).
- **Qué detecta**: Violaciones de estilo, formato inconsistente, indentación incorrecta, nombres fuera de convención y falta de documentación (Javadoc).
- **Qué NO detecta**: Errores lógicos, bugs de ejecución o mala arquitectura.
- **Impacto pedagógico**: Enseña a escribir código profesional, uniforme y legible, preparándote para el trabajo en equipo donde el estilo compartido es obligatorio.
- **Cómo interpretar el reporte**: Indica el archivo, la línea y la regla infringida. Corregí primero los errores de formato e indentación que rompen la lectura visual.

### PMD

- **Qué analiza**: Código fuente (`.java`).
- **Qué detecta**: Patrones problemáticos (código no utilizado), complejidad ciclomática excesiva, métodos demasiado largos y código duplicado.
- **Qué NO detecta**: Bugs en tiempo de ejecución o correctitud de la lógica de negocio.
- **Impacto pedagógico**: Fomenta el diseño simple. Te avisa si un método está haciendo demasiadas cosas (complejidad alta), obligándote a dividir responsabilidades y aplicar refactoring.
- **Cómo interpretar el reporte**: Revisá las métricas de complejidad. Si PMD marca complejidad mayor a 10 en un método, extraé fragmentos a métodos privados. Si marca variables no usadas, eliminalas.

## Herramientas de Análisis de Bytecode y Compilación

### SpotBugs

- **Qué analiza**: Bytecode compilado (`.class`).
- **Qué detecta**: Patrones que históricamente indican bugs reales (por ejemplo, usar `==` para strings, retornos ignorados, o bucles infinitos).
- **Qué NO detecta**: Errores de sintaxis o de mal estilo.
- **Impacto pedagógico**: Muestra cómo la JVM entiende tu código y descubre errores semánticos sutiles que a simple vista pasan desapercibidos, mejorando tu atención al detalle.
- **Cómo interpretar el reporte**: Priorizá los bugs de severidad alta (High). El reporte indica en qué línea el flujo de datos produce un comportamiento anómalo que probablemente rompa el programa.

### Error Prone

- **Qué analiza**: Código durante el proceso de compilación.
- **Qué detecta**: Errores frecuentes pero graves (faltantes de `@Override`, mal uso de colecciones, comparaciones incompatibles).
- **Qué NO detecta**: Errores lógicos que son sintáctica y estructuralmente válidos.
- **Impacto pedagógico**: Frena el error antes de que corras el programa. Evita horas de debugging al enseñarte a usar correctamente la API estándar de Java.
- **Cómo interpretar el reporte**: Detiene la compilación y muestra el error directamente en la salida de la consola. Muchas veces, sugiere exactamente el código necesario para la corrección.

### NullAway

- **Qué analiza**: Código durante el proceso de compilación.
- **Qué detecta**: Posibles `NullPointerException` siguiendo el flujo de datos.
- **Qué NO detecta**: Otras excepciones.
- **Impacto pedagógico**: Instala la mentalidad de "null-safety". Te obliga a pensar sistemáticamente si una variable puede estar vacía antes de usarla, un hábito profesional clave en Java.
- **Cómo interpretar el reporte**: Si marca error, significa que olvidaste proteger un acceso. Agregá la anotación `@Nullable` si es intencional que sea nulo, o un bloque `if (variable != null)`.

## Herramientas de Testing y Arquitectura

### JaCoCo

- **Qué analiza**: Ejecución dinámica de los tests.
- **Qué detecta**: Porcentaje de cobertura (qué líneas, ramas condicionales y métodos fueron ejecutados).
- **Qué NO detecta**: Si tus tests contienen aserciones útiles (un test sin `assert` puede dar 100% de cobertura).
- **Impacto pedagógico**: Revela qué partes de tu código están a ciegas. Te obliga a pensar casos de prueba específicos para los caminos alternativos (`else` o `catch`).
- **Cómo interpretar el reporte**: Enfocate en las líneas rojas (código no ejecutado) o amarillas (ramas parcialmente cubiertas). Escribí tests para cubrir esos escenarios.

### PIT (Pitest)

- **Qué analiza**: Ejecución de los tests sobre código mutado (con alteraciones artificiales).
- **Qué detecta**: Si tus tests son capaces de detectar cambios que rompen el programa (Mutation Score).
- **Qué NO detecta**: Si el código original está libre de bugs (PIT asume que los tests actuales ya pasan).
- **Impacto pedagógico**: Combate la falsa confianza de tener alta cobertura con aserciones débiles. Enseña a escribir aserciones robustas.
- **Cómo interpretar el reporte**: Si un mutante "sobrevive" (survived), significa que PIT introdujo un bug pero ningún test falló. Debés escribir un test que falle ante ese comportamiento anómalo.

### ArchUnit

- **Qué analiza**: Estructura y dependencias del bytecode de todo el proyecto.
- **Qué detecta**: Violaciones a reglas de diseño (dependencias circulares, atributos públicos, encapsulamiento roto).
- **Qué NO detecta**: Implementación interna de los métodos.
- **Impacto pedagógico**: Materializa las reglas abstractas de la cátedra (ej. "todo atributo debe ser privado") en validaciones automáticas. Enseña a respetar los límites de la arquitectura de objetos.
- **Cómo interpretar el reporte**: El test fallará indicando qué clase violó la regla. Cambiá la visibilidad de los atributos o la ubicación de las clases para solucionar el conflicto.

## Integración Continua y Defensa en Profundidad

Cada herramienta provee una capa distinta de protección. Juntas forman un escudo que valida la salud del proyecto:

1. **Error Prone / NullAway**: Previenen fallos evidentes impidiendo la compilación.
2. **Checkstyle / PMD**: Mantienen el código mantenible y legible.
3. **SpotBugs**: Encuentra bugs en tiempo de ejecución de manera estática.
4. **JaCoCo / PIT**: Garantizan que la red de seguridad de tus tests sea efectiva.
5. **ArchUnit**: Asegura que el proyecto escale sin volverse un espagueti de dependencias.
