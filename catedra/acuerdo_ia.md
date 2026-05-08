---
title: Acuerdo de Uso de IA
description: Marco normativo para el uso de Inteligencia Artificial en Programación II.
---

# Acuerdo de Uso de Inteligencia Artificial

Este documento establece el marco normativo y pedagógico para el uso de herramientas de Inteligencia Artificial Generativa (IA) en la asignatura Programación II. El objetivo es integrar estas tecnologías de manera ética y productiva, priorizando siempre el desarrollo de la autonomía cognitiva y la responsabilidad profesional del estudiante.

## 1. Fundamento Pedagógico

La cátedra reconoce que los asistentes de código y los modelos de lenguaje (LLM) son parte integral del ecosistema profesional actual. Sin embargo, su uso prematuro o excesivo puede atrofiar el aprendizaje de conceptos fundamentales. La meta es que aprendas a programar con estas herramientas, no que delegues el proceso de pensamiento en ellas. El conocimiento debe residir en el estudiante, quien es el único responsable final por la calidad y corrección del software entregado.

## 2. Reglas Operativas

### 2.1. Trabajos Prácticos y Entregas Semanales
El uso de IA está permitido bajo un esquema de **trazabilidad obligatoria**. 

*   **Autonomía:** El estudiante debe ser capaz de explicar y defender cada línea de código entregada.
*   **Trazabilidad:** Cualquier fragmento de código generado, sugerido o refactorizado por una IA debe declararse siguiendo el formato establecido en la sección 3.
*   **Validación:** Es responsabilidad del estudiante verificar la exactitud técnica del código generado. Las alucinaciones de la IA no se aceptan como justificativo de errores.

### 2.2. Evaluaciones Parciales
Durante las instancias de examen parcial y final, el uso de cualquier herramienta de IA generativa está **estrictamente prohibido**. Estas instancias buscan evaluar el desempeño individual del estudiante en un entorno controlado para garantizar la adquisición de las competencias mínimas.

## 3. Formato de Declaración de Uso (Registro de IA)

Toda entrega que haya contado con asistencia de IA debe incluir un archivo llamado `IA_LOG.md` en la raíz del repositorio o una sección específica en el encabezado del archivo fuente, con el siguiente detalle:

| Campo | Descripción |
| :--- | :--- |
| **Herramienta** | Nombre y versión del modelo (ej: ChatGPT 4o, GitHub Copilot). |
| **Objetivo** | Qué se intentó resolver (ej: refactorización de bucle, debugging de excepción). |
| **Prompt** | El texto exacto de la instrucción enviada a la IA. |
| **Análisis Crítico** | Explicación de qué se modificó de la sugerencia de la IA y por qué. |

*El incumplimiento en la entrega de este registro ante la detección de uso de IA invalidará la entrega.*

## 4. Ejemplos de Uso

### 4.1. Uso Permitido con Trazabilidad
*   **Consulta de documentación:** Pedir a la IA que explique el funcionamiento de una clase de la API de Java (ej: `java.nio.file.Files`).
*   **Refactorización:** Solicitar sugerencias para simplificar un método que ya funciona pero es difícil de leer.
*   **Debugging:** Pegar un error de compilación y el código relacionado para identificar un olvido sintáctico.

### 4.2. Uso No Permitido
*   **Uso en exámenes:** Cualquier consulta a una IA durante el horario de parcial.

## 5. Sanciones y Procedimientos

La detección de uso de cualquier tipo en exámenes parciales, activará el siguiente protocolo:

1.  **Detección:** Si el docente identifica patrones de código ajenos al nivel de la cursada o el estudiante no puede explicar la lógica de su entrega.
2.  **Entrevista:** Se citará al estudiante a una defensa oral técnica.
3.  **Sanción en exámenes parciales:** La calificación será 0 (cero) sin posibilidad de recuperar.

## 6. Declaración del Estudiante

Al realizar sus entregas en esta cátedra, el estudiante declara:
*   Haber leído y comprendido este acuerdo.
*   Ser el autor de la arquitectura lógica de sus entregas.
*   Asumir que el uso de IA es un complemento y no un reemplazo del estudio.
*   Aceptar que la incapacidad de explicar el código propio es prueba suficiente de un uso indebido de las herramientas.

---

### Resumen Ejecutivo de Cumplimiento
*   **¿Puedo usar IA en TPs?** Sí, si lo documentás en el `IA_LOG.md` y sabés explicarlo.
*   **¿Puedo usar IA en parciales?** No, bajo ninguna circunstancia.
*   **¿Qué pasa si no declaro el uso?** Te arriesgás a la anulación de la entrega y sanciones académicas.
