---
title: "OOP Avanzado"
subtitle: "Calidad de diseño, verificación y evolución del código"
subject: Programación Orientada a Objetos
---

(parte-3-oop-avanzado)=
# OOP Avanzado

En las partes anteriores se construyeron los fundamentos del paradigma orientado a objetos, las relaciones entre objetos, la sintaxis de clases en Java y la herencia con polimorfismo ({ref}`fundamentos-de-la-programacion-orientada-a-objetos`, {ref}`oop2-encapsulamiento-relaciones`, {ref}`oop3-herencia-polimorfismo` y {ref}`java-herencia-polimorfismo`).

Esta parte cambia el foco: ya no alcanza con que el programa "funcione". Ahora importa que el diseño sea **mantenible**, **testeable**, **extensible** y **robusto** frente al cambio.

:::{tip} Objetivos de Aprendizaje

Al finalizar esta parte, serás capaz de:

1. Identificar problemas de diseño en sistemas orientados a objetos
2. Aplicar refactorizaciones para mejorar código existente sin cambiar su comportamiento
3. Evaluar diseños usando principios SOLID
4. Diseñar tests significativos para clases, jerarquías y colaboraciones entre objetos
5. Reconocer anti-patrones y code smells frecuentes
6. Expresar contratos claros mediante precondiciones, postcondiciones e invariantes
:::

---

(mapa-parte-3)=
## Mapa de la Parte

| Capítulo | Tema central | Qué aporta |
| :--- | :--- | :--- |
| [08 - Refactoring y Code Smells](08_oop_refactoring.md) | Mejora de código existente | Enseña a detectar olores de diseño y a corregirlos en forma segura |
| [09 - Principios SOLID](09_oop_solid.md) | Heurísticas de diseño | Da criterios para evaluar cohesión, acoplamiento y extensibilidad |
| [11 - Testing OOP](11_oop_testing.md) | Verificación de objetos y jerarquías | Lleva el testing desde funciones simples a colaboraciones orientadas a objetos |
| [12 - Anti-patrones y Code Smells](12_oop_antipatrones.md) | Errores recurrentes de diseño | Muestra decisiones comunes que degradan la calidad del sistema |
| [13 - Diseño por Contratos](13_oop_contratos.md) | Especificación formal del comportamiento | Formaliza responsabilidades entre cliente y proveedor |

---

(ejes-conceptuales-parte-3)=
## Ejes Conceptuales

### 1. Calidad interna del diseño

Un sistema puede compilar, pasar tests e igual estar mal diseñado. En esta parte se trabajan criterios para reconocer cuándo un diseño se vuelve rígido, frágil o difícil de extender.

### 2. Cambio seguro

Refactorizar sin romper comportamiento exige una combinación de criterio de diseño y red de seguridad. Por eso esta parte articula constantemente **refactoring**, **testing** y **contratos**.

### 3. Detección temprana de problemas

Los anti-patrones y code smells no son errores de sintaxis: son señales de deuda técnica, complejidad innecesaria o mala distribución de responsabilidades.

---

(recorrido-sugerido-parte-3)=
## Recorrido Sugerido

1. Empezar por [Refactoring y Code Smells](08_oop_refactoring.md) para incorporar vocabulario de diagnóstico.
2. Continuar con [SOLID](09_oop_solid.md) para sumar criterios de diseño y justificar refactorizaciones.
3. Seguir con [Testing OOP](11_oop_testing.md) para verificar comportamiento en sistemas más ricos que una función aislada.
4. Leer [Anti-patrones y Code Smells](12_oop_antipatrones.md) como catálogo de errores frecuentes y contraste con buenas decisiones.
5. Cerrar con [Diseño por Contratos](13_oop_contratos.md), que formaliza las responsabilidades de cada objeto y conecta directamente con Liskov, testing y robustez.

:::{note}
Aunque los capítulos pueden consultarse por separado, esta parte rinde mejor como bloque: **detectar problemas**, **entender principios**, **verificar comportamiento** y **formalizar contratos**.
:::

---

(conexiones-parte-3)=
## Conexiones con Otras Partes

- **Parte 2** aporta las bases conceptuales: objetos, encapsulamiento, jerarquías, clases abstractas e interfaces.
- **Reglas y Convenciones** complementan esta parte con criterios operativos sobre documentación, testing, excepciones y diseño.
- **Parte 4** toma estas herramientas de calidad y las proyecta sobre soluciones de diseño reutilizables: los patrones.

---

(resumen-parte-3)=
## Resumen

La Parte 3 funciona como puente entre "saber programar objetos" y "saber diseñar sistemas orientados a objetos con criterio". El foco no está solo en agregar funcionalidades, sino en sostener calidad a medida que el código crece y cambia.
