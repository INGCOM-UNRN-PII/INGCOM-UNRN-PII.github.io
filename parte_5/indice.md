---
title: "Parte 5: Tipos de Datos Abstractos"
description: "Recorrido desde la idea de TDA hasta especificaciones algebraicas y su análisis."
---

(parte5-indice)=
# Parte 5: Tipos de Datos Abstractos


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

Esta parte organiza el paso desde la intuición de un TDA hacia especificaciones algebraicas completas. El foco está en separar contrato e implementación, formalizar operaciones con axiomas y usar ese marco para comparar alternativas con criterio.

:::{tip} Qué se aprende en esta parte

Al finalizar el recorrido por esta parte, se espera que el estudiante pueda:

1. **Distinguir** entre la especificación de un tipo (el "qué") y su implementación concreta (el "cómo").
2. **Definir** contratos claros mediante interfaces, precondiciones e invariantes de representación.
3. **Modelar** tipos de datos usando especificaciones algebraicas (sorts, signaturas y axiomas).
4. **Comparar** la eficiencia de distintas implementaciones aplicando el modelo de costo asintótico.
5. **Validar** decisiones de diseño mediante perfiles de rendimiento y análisis de localidad de memoria.
:::

## Propósito de la parte

El objetivo central es construir un lenguaje común para especificar qué promete una estructura sin fijar su representación. Esto permite verificar que distintas implementaciones cumplen el mismo contrato y decidir entre opciones concretas usando complejidad y contexto de uso.

## Conceptos clave y anclaje en Java

El pilar de esta parte es la **separación de intereses**. En Java, esto se traduce habitualmente en la relación entre una `interface` (el contrato abstracto) y una `class` (la implementación concreta).

```java
// El Contrato (TDA): Define qué se puede hacer
public interface Pila<T> {
    void apilar(T elemento);
    T desapilar();
    boolean estaVacia();
}

// La Implementación: Define cómo se hace (ej. usando un arreglo)
public class PilaArreglo<T> implements Pila<T> {
    private T[] elementos;
    private int tope;
    // ... implementación de métodos e invariantes ...
}
```

## Desafíos y errores frecuentes

- **Fuga de abstracción:** Incluir detalles de la implementación (como el tamaño de un arreglo) en la interfaz del TDA.
- **Violación de invariantes:** Modificar el estado interno sin garantizar que se mantengan las propiedades lógicas del tipo.
- **Acoplamiento al costo:** Diseñar un sistema que dependa de una implementación específica en lugar de depender de la interfaz abstracta.
- **Ignorar el hardware:** Suponer que dos algoritmos con la misma complejidad asintótica se comportan igual, ignorando la localidad de memoria y el impacto de la JVM.

## Orden sugerido de lectura

| Orden | Capítulo | Rol en la progresión |
| :--- | :--- | :--- |
| 1 | [Tipos Abstractos de Datos](1_intro_tda.md) | Introducción conceptual: interfaz, contrato e invariantes. |
| 2 | [Introducción a Tipos de Datos Abstractos](2_tipos_abstractos.md) | Marco formal: signaturas, axiomas e integración con contratos. |
| 3 | [Análisis de algoritmos](3_algoritmos.md) | Modelo de costo para comparar implementaciones. |
| 4 | [Estructuras de Datos: Especificaciones Completas](4_estructuras.md) | Catálogo formal de estructuras base y su complejidad. |
| 5 | [Especificaciones algebraicas avanzadas](5_avanzadas.md) | Extensión a familias no lineales (árboles, grafos). |
| 6 | [Localidad de memoria](6_localidad_memoria.md) | Impacto del hardware y la JVM en el rendimiento real. |
| 7 | [Profiling](7_profiling.md) | Validación empírica y búsqueda de cuellos de botella. |

## Capítulos nucleares

Estos capítulos contienen los fundamentos teóricos y prácticos que sostienen toda la parte:

- [Tipos Abstractos de Datos](1_intro_tda.md)
- [Introducción a Tipos de Datos Abstractos](2_tipos_abstractos.md)
- [Estructuras de Datos: Especificaciones Algebraicas Completas](4_estructuras.md)

## Repaso y ampliación

Capítulos destinados a profundizar en el análisis de rendimiento y en estructuras más complejas:

- [Análisis de algoritmos](3_algoritmos.md)
- [Especificaciones algebraicas avanzadas](5_avanzadas.md)
- [Localidad de memoria](6_localidad_memoria.md)
- [Profiling](7_profiling.md)

## Índice exhaustivo

- `1_intro_tda.md` — Marco intuitivo de TAD, contrato, encapsulamiento e invariantes.
- `2_tipos_abstractos.md` — Desarrollo formal de sorts, operaciones, axiomas y verificación.
- `3_algoritmos.md` — Herramientas asintóticas para comparar costos de implementación.
- `4_estructuras.md` — Especificaciones algebraicas completas de estructuras fundamentales.
- `5_avanzadas.md` — Especificaciones avanzadas de familias no lineales y asociativas.
- `6_localidad_memoria.md` — Impacto de la jerarquía de memoria y el layout de objetos en el rendimiento.
- `7_profiling.md` — Evidencia empírica y herramientas de precisión para encontrar cuellos de botella.

## Autoevaluación de la parte

Antes de dar por concluida esta parte, intentá responder:

1. ¿Podés explicar la diferencia entre una interfaz Java y un TDA formal sin usar términos de implementación?
2. Ante dos implementaciones de una `Lista`, ¿qué criterios usarías para elegir una sobre otra en un sistema de tiempo real?
3. ¿Por qué un axioma en una especificación algebraica es equivalente a una prueba de unidad?
4. ¿Cómo afecta la disposición de los datos en memoria al tiempo de ejecución si la complejidad asintótica es la misma?

## Próximo paso

Para iniciar el recorrido, comenzá por [Tipos Abstractos de Datos](1_intro_tda.md) y usá este índice como mapa de avance de la parte.

## Ejercicios de verificación

1. Resolvé un caso mínimo usando la estructura/algoritmo del capítulo y documentá por qué esa elección es válida.
2. Construí un contraejemplo donde una elección alternativa falle (rendimiento o corrección).
3. Escribí una prueba corta en Java que verifique un invariante crítico.

