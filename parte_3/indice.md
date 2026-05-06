---
title: "OOP Avanzado"
subtitle: "Calidad de diseño, verificación y evolución del código"
subject: Programación Orientada a Objetos
---

(parte-3-oop-avanzado)=
# OOP Avanzado

## Cómo usar esta parte

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

## Orden sugerido de lectura

| Orden | Capítulo | Rol en la progresión |
| :--- | :--- | :--- |
| 1 | [Refactoring y Code Smells](08_oop_refactoring.md) | Instala vocabulario de diagnóstico y mejora segura |
| 2 | [SOLID](09_oop_solid.md) | Agrega criterios para evaluar cohesión, acoplamiento y extensibilidad |
| 3 | [Testing OOP](11_oop_testing.md) | Lleva la verificación desde métodos aislados a colaboraciones y jerarquías |
| 4 | [Anti-patrones y Code Smells](12_oop_antipatrones.md) | Funciona como contraste y catálogo de errores frecuentes |
| 5 | [Diseño por Contratos](13_oop_contratos.md) | Cierra la parte formalizando responsabilidades y reglas de sustitución |

## Capítulos nucleares

Estos capítulos conviene trabajarlos en secuencia, porque arman el hilo principal de la parte:

| Capítulo | Por qué es nuclear |
| :--- | :--- |
| [Refactoring y Code Smells](08_oop_refactoring.md) | Define el vocabulario para detectar problemas y priorizar mejoras |
| [SOLID](09_oop_solid.md) | Da criterios de diseño para justificar refactorizaciones y evaluar decisiones |
| [Testing OOP](11_oop_testing.md) | Aporta la red de seguridad necesaria para cambiar diseño sin romper comportamiento |

## Repaso y ampliación

Estos capítulos funcionan mejor como contraste, consolidación o cierre formal del recorrido:

| Capítulo | Tipo | Uso sugerido |
| :--- | :--- | :--- |
| [Anti-patrones y Code Smells](12_oop_antipatrones.md) | Contraste y diagnóstico | Leer después de refactoring y SOLID para reconocer decisiones de diseño pobres |
| [Diseño por Contratos](13_oop_contratos.md) | Formalización y cierre | Usar para cerrar la parte conectando Liskov, testing y robustez contractual |

:::{note}
Lo que sigue debajo es el **índice exhaustivo** del material. Sirve para ubicar temas puntuales una vez que ya se tiene el mapa general de la parte.
:::

## Ejes conceptuales

### Calidad interna del diseño

Un sistema puede compilar, pasar tests e igual estar mal diseñado. En esta parte se trabajan criterios para reconocer cuándo un diseño se vuelve rígido, frágil o difícil de extender.

### Cambio seguro

Refactorizar sin romper comportamiento exige una combinación de criterio de diseño y red de seguridad. Por eso esta parte articula constantemente **refactoring**, **testing** y **contratos**.

### Detección temprana de problemas

Los anti-patrones y code smells no son errores de sintaxis: son señales de deuda técnica, complejidad innecesaria o mala distribución de responsabilidades.

## Conexiones con otras partes

- **Parte 2** aporta las bases conceptuales: objetos, encapsulamiento, jerarquías, clases abstractas e interfaces.
- **Reglas y Convenciones** complementan esta parte con criterios operativos sobre documentación, testing, excepciones y diseño.
- **Parte 4** toma estas herramientas de calidad y las proyecta sobre soluciones de diseño reutilizables: los patrones.

---

## [Refactoring y Code Smells](./08_oop_refactoring.md)
  * [¿Qué es Refactoring?](08_oop_refactoring.md)
    * [Definición](08_oop_refactoring.md)
    * [¿Por qué Refactorizar?](08_oop_refactoring.md)
    * [¿Cuándo Refactorizar?](08_oop_refactoring.md)
    * [¿Cuándo NO Refactorizar?](08_oop_refactoring.md)
  * [Code Smells: Detectando Problemas](08_oop_refactoring.md)
    * [¿Qué son los Code Smells?](08_oop_refactoring.md)
  * [Bloaters: Código Inflado](08_oop_refactoring.md)
    * [Long Method (Método Largo)](08_oop_refactoring.md)
    * [Large Class (Clase Grande)](08_oop_refactoring.md)
    * [Primitive Obsession (Obsesión con Primitivos)](08_oop_refactoring.md)
    * [Long Parameter List (Lista Larga de Parámetros)](08_oop_refactoring.md)
  * [Object-Orientation Abusers: Mal Uso de OOP](08_oop_refactoring.md)
    * [Switch Statements (Cadenas de Switch)](08_oop_refactoring.md)
    * [Refused Bequest (Herencia Rechazada)](08_oop_refactoring.md)
    * [Temporary Field (Campo Temporal)](08_oop_refactoring.md)
  * [Change Preventers: Código que Resiste el Cambio](08_oop_refactoring.md)
    * [Divergent Change (Cambio Divergente)](08_oop_refactoring.md)
    * [Shotgun Surgery (Cirugía de Escopeta)](08_oop_refactoring.md)
    * [Parallel Inheritance Hierarchies (Jerarquías Paralelas)](08_oop_refactoring.md)
  * [Dispensables: Código Innecesario](08_oop_refactoring.md)
    * [Dead Code (Código Muerto)](08_oop_refactoring.md)
    * [Speculative Generality (Generalidad Especulativa)](08_oop_refactoring.md)
    * [Comments (Comentarios Excesivos)](08_oop_refactoring.md)
  * [Couplers: Acoplamiento Excesivo](08_oop_refactoring.md)
    * [Feature Envy (Envidia de Funcionalidad)](08_oop_refactoring.md)
    * [Inappropriate Intimacy (Intimidad Inapropiada)](08_oop_refactoring.md)
    * [Message Chains (Cadenas de Mensajes)](08_oop_refactoring.md)
  * [El Proceso de Refactoring](08_oop_refactoring.md)
    * [Refactoring Seguro](08_oop_refactoring.md)
    * [Catálogo de Refactorizaciones Comunes](08_oop_refactoring.md)
    * [Métricas para Detectar Problemas](08_oop_refactoring.md)
  * [Relación entre SOLID y Code Smells](08_oop_refactoring.md)
  * [Resumen](08_oop_refactoring.md)
  * [Ejercicios](08_oop_refactoring.md)
  * [Lecturas Recomendadas](08_oop_refactoring.md)
  * [Próximo paso](08_oop_refactoring.md)

## [SOLID](./09_oop_solid.md)
  * [Introducción: ¿Por qué SOLID?](09_oop_solid.md)
    * [El Costo del Mal Diseño](09_oop_solid.md)
    * [SOLID como Antídoto](09_oop_solid.md)
  * [S: Principio de Responsabilidad Única](09_oop_solid.md)
    * [Definición](09_oop_solid.md)
    * [Ejemplo: Violación del SRP](09_oop_solid.md)
    * [Problemas de Esta Violación](09_oop_solid.md)
    * [Refactorización: Separar Responsabilidades](09_oop_solid.md)
    * [¿Cuánta Separación es Suficiente?](09_oop_solid.md)
  * [O: Principio Abierto/Cerrado](09_oop_solid.md)
    * [Definición](09_oop_solid.md)
    * [Ejemplo: Violación del OCP](09_oop_solid.md)
    * [Refactorización: Usar Abstracción](09_oop_solid.md)
    * [Mecanismos para Lograr OCP](09_oop_solid.md)
    * [El Problema de la Anticipación](09_oop_solid.md)
  * [L: Principio de Sustitución de Liskov](09_oop_solid.md)
    * [Definición](09_oop_solid.md)
    * [El Ejemplo Clásico: Rectángulo y Cuadrado](09_oop_solid.md)
    * [Reglas de LSP](09_oop_solid.md)
    * [Solución al Problema Rectángulo-Cuadrado](09_oop_solid.md)
    * [Relación con Diseño por Contratos](09_oop_solid.md)
  * [I: Principio de Segregación de Interfaces](09_oop_solid.md)
    * [Definición](09_oop_solid.md)
    * [Ejemplo: Interfaz Gorda](09_oop_solid.md)
    * [Refactorización: Interfaces Segregadas](09_oop_solid.md)
    * [Beneficios de ISP](09_oop_solid.md)
    * [ISP y Cohesión de Interfaces](09_oop_solid.md)
  * [D: Principio de Inversión de Dependencias](09_oop_solid.md)
    * [Definición](09_oop_solid.md)
    * [Ejemplo: Violación de DIP](09_oop_solid.md)
    * [Refactorización: Invertir la Dependencia](09_oop_solid.md)
    * [Inversión de Control (IoC)](09_oop_solid.md)
    * [¿Quién "Posee" la Abstracción?](09_oop_solid.md)
  * [Interrelaciones entre Principios SOLID](09_oop_solid.md)
    * [Ejemplo Integrado: Sistema de Pagos](09_oop_solid.md)
  * [Antipatrones y Errores Comunes](09_oop_solid.md)
    * [Sobre-Ingeniería por SOLID](09_oop_solid.md)
    * [Principios Mal Aplicados](09_oop_solid.md)
    * [Encontrar el Balance](09_oop_solid.md)
  * [Resumen](09_oop_solid.md)
  * [Ejercicios](09_oop_solid.md)
  * [Lecturas Recomendadas](09_oop_solid.md)
  * [Próximo paso](09_oop_solid.md)

## [Testing OOP](./11_oop_testing.md)
  * [Fundamentos del Testing](11_oop_testing.md)
    * [¿Por qué Testing?](11_oop_testing.md)
    * [Niveles de Testing](11_oop_testing.md)
    * [Anatomía de un Test](11_oop_testing.md)
  * [Testing de Clases y Objetos](11_oop_testing.md)
    * [¿Qué Testear en una Clase?](11_oop_testing.md)
    * [Testing Sin Getters/Setters](11_oop_testing.md)
    * [Testing de Invariantes sin Getters](11_oop_testing.md)
    * [Patrones para Testing de Código Encapsulado](11_oop_testing.md)
    * [Organización de Tests](11_oop_testing.md)
  * [Test-Driven Development (TDD)](11_oop_testing.md)
    * [¿Qué es TDD?](11_oop_testing.md)
    * [Ejemplo TDD: Implementando una Pila](11_oop_testing.md)
    * [Beneficios de TDD](11_oop_testing.md)
  * [Dobles de Prueba (Test Doubles)](11_oop_testing.md)
    * [El Problema de las Dependencias](11_oop_testing.md)
    * [Tipos de Dobles de Prueba](11_oop_testing.md)
    * [Ejemplo: Usando Stubs](11_oop_testing.md)
    * [Usando Frameworks de Mocking: Mockito](11_oop_testing.md)
  * [Diseño para Testeabilidad](11_oop_testing.md)
    * [Características del Código Testeable](11_oop_testing.md)
    * [Código Difícil de Testear](11_oop_testing.md)
    * [Relación SOLID-Testing](11_oop_testing.md)
  * [Testing de Herencia y Polimorfismo](11_oop_testing.md)
    * [Testing de Jerarquías de Clases](11_oop_testing.md)
    * [Testing de Comportamiento Polimórfico](11_oop_testing.md)
  * [Buenas Prácticas de Testing](11_oop_testing.md)
    * [Principios FIRST](11_oop_testing.md)
    * [Nombres Descriptivos](11_oop_testing.md)
    * [Un Concepto por Test](11_oop_testing.md)
    * [Secciones Claramente Separadas](11_oop_testing.md)
  * [Patrones de Testing Avanzados](11_oop_testing.md)
    * [Object Mother](11_oop_testing.md)
    * [Builder para Tests](11_oop_testing.md)
    * [Cuándo usar cada patrón](11_oop_testing.md)
  * [Resumen](11_oop_testing.md)
  * [Ejercicios](11_oop_testing.md)
  * [Lecturas Recomendadas](11_oop_testing.md)
  * [Próximo paso](11_oop_testing.md)

## [Anti-patrones y Code Smells](./12_oop_antipatrones.md)
  * [¿Qué Son los Anti-patrones?](12_oop_antipatrones.md)
    * [Definiciones](12_oop_antipatrones.md)
  * [Code Smells a Nivel de Clase](12_oop_antipatrones.md)
    * [God Class (Clase Dios)](12_oop_antipatrones.md)
    * [Data Class (Clase de Datos)](12_oop_antipatrones.md)
    * [Lazy Class (Clase Perezosa)](12_oop_antipatrones.md)
  * [Code Smells a Nivel de Método](12_oop_antipatrones.md)
    * [Long Method (Método Largo)](12_oop_antipatrones.md)
    * [Long Parameter List (Lista de Parámetros Larga)](12_oop_antipatrones.md)
    * [Feature Envy (Envidia de Características)](12_oop_antipatrones.md)
  * [Code Smells en el Código](12_oop_antipatrones.md)
    * [Duplicate Code (Código Duplicado)](12_oop_antipatrones.md)
    * [Magic Numbers (Números Mágicos)](12_oop_antipatrones.md)
    * [Comments (Comentarios Innecesarios o Engañosos)](12_oop_antipatrones.md)
  * [Anti-patrones Clásicos de Diseño](12_oop_antipatrones.md)
    * [Spaghetti Code](12_oop_antipatrones.md)
    * [Golden Hammer (Martillo de Oro)](12_oop_antipatrones.md)
    * [Lava Flow (Flujo de Lava)](12_oop_antipatrones.md)
    * [Copy-Paste Programming](12_oop_antipatrones.md)
  * [Anti-patrones en Jerarquías](12_oop_antipatrones.md)
    * [Yo-Yo Problem](12_oop_antipatrones.md)
    * [Circle-Ellipse Problem (Problema Círculo-Elipse)](12_oop_antipatrones.md)
  * [Cómo Detectar Code Smells](12_oop_antipatrones.md)
    * [Heurísticas de Detección](12_oop_antipatrones.md)
    * [Herramientas de Análisis](12_oop_antipatrones.md)
  * [Prevención: Evitar Smells desde el Inicio](12_oop_antipatrones.md)
    * [Prácticas Preventivas](12_oop_antipatrones.md)
    * [La Regla del Boy Scout](12_oop_antipatrones.md)
  * [Resumen](12_oop_antipatrones.md)
  * [Ejercicios](12_oop_antipatrones.md)
  * [Lecturas Recomendadas](12_oop_antipatrones.md)
  * [Próximo paso](12_oop_antipatrones.md)

## [Diseño por Contratos](./13_oop_contratos.md)
  * [La Filosofía del Contrato](13_oop_contratos.md)
    * [La Metáfora del Contrato Legal](13_oop_contratos.md)
    * [Origen: Bertrand Meyer y Eiffel](13_oop_contratos.md)
    * [Beneficios del Diseño por Contratos](13_oop_contratos.md)
  * [Precondiciones: Lo que el Cliente Debe Garantizar](13_oop_contratos.md)
    * [Definición](13_oop_contratos.md)
    * [Ejemplos de Precondiciones](13_oop_contratos.md)
    * [Verificación de Precondiciones](13_oop_contratos.md)
    * [¿Quién Verifica las Precondiciones?](13_oop_contratos.md)
  * [Postcondiciones: Lo que el Método Garantiza](13_oop_contratos.md)
    * [Definición](13_oop_contratos.md)
    * [Ejemplos de Postcondiciones](13_oop_contratos.md)
    * [Postcondiciones Excepcionales](13_oop_contratos.md)
  * [Invariantes de Clase: Lo que Siempre Debe Ser Verdad](13_oop_contratos.md)
    * [Definición](13_oop_contratos.md)
    * [Ejemplos de Invariantes](13_oop_contratos.md)
    * [Invariantes y Momentos de Verificación](13_oop_contratos.md)
  * [El Contrato Completo](13_oop_contratos.md)
    * [Estructura de un Contrato](13_oop_contratos.md)
    * [Ejemplo Completo: Pila con Contratos](13_oop_contratos.md)
  * [Responsabilidades: Cliente vs Proveedor](13_oop_contratos.md)
    * [El Modelo Cliente-Proveedor](13_oop_contratos.md)
    * [¿Qué Pasa Cuando se Viola el Contrato?](13_oop_contratos.md)
    * [Ejemplo: Identificando Responsables](13_oop_contratos.md)
    * [Programación Defensiva vs Diseño por Contratos](13_oop_contratos.md)
  * [Contratos y Herencia: El Principio de Liskov Revisitado](13_oop_contratos.md)
    * [Reglas para Subtipos](13_oop_contratos.md)
    * [Ejemplo: Precondiciones Más Débiles (Correcto)](13_oop_contratos.md)
    * [Ejemplo: Precondiciones Más Fuertes (Incorrecto)](13_oop_contratos.md)
    * [Ejemplo: Postcondiciones Más Fuertes (Correcto)](13_oop_contratos.md)
    * [Relación con Covarianza y Contravarianza](13_oop_contratos.md)
  * [Contratos en la Práctica](13_oop_contratos.md)
    * [Documentación de Contratos](13_oop_contratos.md)
    * [Uso de Assertions en Java](13_oop_contratos.md)
    * [Bibliotecas para Contratos](13_oop_contratos.md)
    * [Testing y Contratos](13_oop_contratos.md)
  * [El Problema del Null y los Contratos](13_oop_contratos.md)
    * [Null como Fuente de Violaciones](13_oop_contratos.md)
    * [Estrategias para Manejar Null](13_oop_contratos.md)
  * [Resumen](13_oop_contratos.md)
    * [Elementos del Contrato](13_oop_contratos.md)
    * [Reglas en Herencia](13_oop_contratos.md)
    * [Beneficios Clave](13_oop_contratos.md)
  * [Ejercicios](13_oop_contratos.md)
  * [Próximo paso](13_oop_contratos.md)
