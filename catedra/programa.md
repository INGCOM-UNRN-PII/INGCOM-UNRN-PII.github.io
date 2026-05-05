---
title: Programa Analítico
description:
  Programa analítico de la asignatura Programación II (B6008) - Ingeniería en
  Computación UNRN.
---

# Programa analítico de Programación II [B6008] _[v10]_

|                                      |                                    |
| :----------------------------------- | :--------------------------------- |
| **Programa analítico de**            | Programación II                    |
| **Código Guaraní**                   | B6008                              |
| **Plan de estudio**                  | ICOMP 2021 [v9]                    |
| **Ubicación en el plan de estudios** | 2do año - 1er cuatrimestre         |
| **Carga horaria semanal**            | 6 horas (3 teóricas / 3 prácticas) |
| **Régimen de cursada**               | Cuatrimestral                      |
| **Sede**                             | Sede Andina                        |
| **Localidad**                        | San Carlos de Bariloche            |
| **Escuela**                          | Escuela de Producción y Tecnología |
| **Carrera**                          | Ingeniería en Computación          |
| **Profesor/a**                       | Martín René Vilugrón               |
| **Equipo de docencia**               | Sin equipo de docencia             |

---

| Correlativas vigentes |                      |                           |
| :-------------------- | :------------------- | :------------------------ |
| **Para cursar**       | **Cursada aprobada** | Programación I (B6003)    |
|                       | **Materia aprobada** | Sin correlativas vigentes |
| **Para rendir**       | **Materia aprobada** | Programación I (B6003)    |

(fundamentacion)=
## Fundamentación

La programación es uno de los pilares fundamentales de la carrera, dado que
forma la base sobre la cual se construyen todas las demás habilidades técnicas y
analíticas de un profesional en el área de la informática. Por este motivo, el
objetivo principal de esta asignatura es que los estudiantes aprendan a
programar de forma correcta y que sean capaces de desarrollar software de alta
calidad y de manera eficiente desde los inicios de su acercamiento a la
programación como disciplina.

Esta cátedra adopta la estrategia _Late-Objects_ formulada por Cay Horstmann,
para simplificar la transición desde C al introducir el grueso de los nuevos
conceptos, luego de tratar la mayor cantidad de similitudes con el lenguaje de
programación C, visto en la cátedra correlativa.

La cátedra hace un uso intensivo de herramientas de análisis y verificación
automática, no solo por la realimentación a los estudiantes y la velocidad de la
misma, sino por el uso que estas herramientas tienen en su futura carrera
profesional. Se les otorgarán las herramientas para que los estudiantes puedan
diseñar una solución y luego implementarla a partir de una mirada más amplia del
concepto de artefacto de software.

(propositos)=
## Propósitos de la asignatura

Los propósitos de esta asignatura se estructuran en torno a seis ejes
fundamentales que guiarán el desarrollo de competencias profesionales en los
estudiantes a lo largo del cuatrimestre.

### Comprensión Profunda del Lenguaje Java

Java es un lenguaje de programación ampliamente utilizado en la industria por su
robustez, portabilidad y seguridad. Los estudiantes aprenderán a manejar este
lenguaje de manera avanzada, comprendiendo sus características y aplicando las
mejores prácticas en su uso, desde la sintaxis básica hasta conceptos avanzados
como genéricos y manejo de excepciones.

### Diseño Orientado a Objetos

Se enfatizará el uso de la programación orientada a objetos (POO) para
estructurar el software de manera modular y reutilizable. Los estudiantes
aprenderán a diseñar y desarrollar sistemas complejos utilizando principios
sólidos de POO, incluyendo encapsulamiento, herencia, polimorfismo y
abstracción, siguiendo las reglas de diseño establecidas por la cátedra.

### Resolución de Problemas

Se desarrollarán habilidades para descomponer problemas complejos en partes
manejables y diseñar soluciones eficientes. Se fomentará un pensamiento crítico
y una metodología sistemática para abordar y resolver problemas, aplicando
estructuras de datos apropiadas y algoritmos eficientes según el contexto del
problema.

### Calidad y Mantenibilidad del Código

Se destacará la importancia de escribir código limpio, documentado y fácilmente
mantenible. Los estudiantes aprenderán sobre pruebas unitarias, refactorización
y otras prácticas que mejoran la calidad del software, aplicando las reglas de 
documentación y las convenciones de nomenclatura establecidas en la cátedra.

### Trabajo en Equipo y Herramientas de Desarrollo

Se promoverá el trabajo colaborativo y el uso de herramientas modernas de
desarrollo, como sistemas de control de versiones (Git/GitHub), entornos de
desarrollo integrados (IntelliJ IDEA), herramientas de análisis estático (PMD,
CheckStyle, SpotBugs) y construcción automatizada (Gradle).

### Aplicaciones de Estructuras de Datos, Algoritmos y Análisis de Complejidad

Los estudiantes profundizarán en el estudio de estructuras de datos
fundamentales como listas, pilas, colas, árboles y grafos, analizando su
complejidad temporal y espacial. Este tema, visto parcialmente en Programación
I, se consolidará aquí con implementaciones prácticas y análisis de casos de uso
reales.

(contenidos-minimos)=
## Contenidos mínimos según plan de estudio

Los contenidos mínimos establecidos por el plan de estudios de la carrera de
Ingeniería en Computación (ICOMP 2021) para esta asignatura incluyen los
siguientes temas centrales que estructuran el desarrollo curricular de la
materia:

Entrada/salida de información por archivos. Archivos de acceso aleatorio.
Memoria dinámica; reserva, liberación de memoria y ciclo de vida de objetos.
Puntero a función. Patrones de diseño. Refactorización y Deuda Técnica.
Estructuras de datos. Tipos de datos definidos por el usuario (TAD: Tipo
Abstracto de Dato). Listas, pilas, colas, tablas de hash, árboles, colas
priorizadas, conjuntos y grafos.

(metodologia)=
## Propuesta metodológica

La asignatura implementa una metodología mixta que combina clases teóricas
expositivas con prácticas de laboratorio, maximizando la aplicación concreta de
los conceptos enseñados y preparando a los estudiantes para el trabajo
profesional en desarrollo de software.

### Clases teóricas

En las clases teóricas se desarrollarán los temas del programa de la asignatura,
incluyendo múltiples ejemplos que faciliten la asimilación de los contenidos
conceptuales. Se utilizarán ejemplos prácticos para relacionar cada uno de los
temas vistos, fomentando la interacción del alumno con el objetivo de que logre
una actitud activa para, a través de su propio razonamiento, crear soluciones
creativas a problemas planteados durante las clases.

### Clases prácticas

En las clases prácticas se plantearán distintos conjuntos de ejercicios para
favorecer la asimilación de los conceptos vistos en la materia. Al inicio de
cada sesión práctica, el profesor hará una breve explicación con ejemplos
similares a los ejercicios propuestos en la práctica. Los estudiantes trabajarán
en la resolución de estos ejercicios, aplicando los conocimientos teóricos en un
entorno práctico, con asistencia del docente para resolver dudas y validar
enfoques.

### Herramientas de desarrollo

Se fomentará el uso de herramientas modernas de desarrollo de software para
mejorar la experiencia de aprendizaje y preparar a los estudiantes para el
entorno profesional. Las principales herramientas y metodologías a utilizar son:

- **Git**: Para el control de cambios, permitiéndoles gestionar sus proyectos de
  manera eficiente y colaborar con sus compañeros.
- **GitHub**: Será utilizado como plataforma para alojar y compartir
  repositorios, facilitando el seguimiento del progreso de los estudiantes y la
  colaboración en equipo.
- **Herramientas de verificación**: La combinación de PMD, CheckStyle, SpotBugs
  y JaCoCo con reglas personalizadas y adaptadas a la cátedra, documentadas en
  las {ref}`reglas de estilo <regla-0x0000>`.
- **Gradle**: Los estudiantes aprenderán a usar esta herramienta de construcción
  de software para gestionar las dependencias del proyecto, compilar el código y
  ejecutar las pruebas. Gradle simplifica el proceso de construcción y permite
  integrar diversas herramientas en el flujo de desarrollo.
- **IntelliJ IDEA Community Edition**: El entorno de desarrollo integrado que
  unifica todas las herramientas descritas anteriormente en una interfaz
  coherente y profesional.

### Enfoque pedagógico

Se fomentará el aprendizaje a través de la resolución de problemas prácticos,
estimulando a que los estudiantes propongan y programen soluciones creativas a
distintos problemas propuestos. Este enfoque práctico permitirá a los
estudiantes aplicar los conocimientos teóricos en situaciones reales,
desarrollando habilidades críticas para su futuro profesional.

Todo el contenido de la cátedra quedará alojado en repositorios GitHub que serán
actualizados a lo largo del cuatrimestre y trabajados por cursada. Por ejemplo,
[INGCOM-UNRN-PII/cursada-2026](https://github.com/INGCOM-UNRN-PII/cursada-2026)
contiene todo el material de las clases de dicho año.

(ajustes-discapacidad)=
## Ajustes para estudiantes con discapacidad

La UNRN desarrolla políticas en torno a accesibilidad académica para estudiantes
con discapacidad y acompañamiento de equipos docentes, en consonancia con las
normativas nacionales y orientaciones del CIN. Al inicio de la cursada se espera
contar con el relevamiento del espacio de APD (Asistencia Pedagógica en
Discapacidad) sobre los estudiantes que cursen la materia con el fin de
identificar barreras y definir las configuraciones de apoyo necesarias.

La cátedra se compromete a implementar los ajustes razonables necesarios para
garantizar la igualdad de oportunidades en el acceso al conocimiento, trabajando
de manera coordinada con el área de APD y adaptando materiales, evaluaciones y
metodologías según las necesidades específicas identificadas.

(unidades)=
## Unidades

El programa se estructura en seis unidades temáticas que progresan desde los
conceptos fundamentales de Java hasta temas avanzados de estructuras de datos y
patrones de diseño, siguiendo la estrategia _Late-Objects_ que permite una
transición natural desde la programación estructurada hacia la orientación a
objetos.

(unidad-1)=
### Unidad 1: Java desde C

Tema previsto para ser dictado en las dos primeras semanas de clase.

#### Contenidos

El objetivo de esta unidad es proporcionar a los estudiantes que ya tienen
conocimientos en programación en C una transición suave y comprensiva hacia la
programación en Java. Se abordarán las similitudes y diferencias entre ambos
lenguajes, facilitando la adaptación a Java mediante ejemplos y ejercicios
prácticos que reutilizan conceptos ya conocidos.

- **Introducción a Java**: Historia y evolución de Java. Características
principales de Java. Comparación entre C y Java.
- **Sintaxis y Estructura del Lenguaje**: Estructura de un programa en Java.
Diferencias sintácticas entre C y Java. Tipos de datos y variables en Java.
Operadores y expresiones en Java.
- **Control de Flujo**: Instrucciones de control de flujo (`if`, `else`,
`switch`). Lazos (`for`, `while`, `do-while`). Comparación de estructuras de
control entre C y Java.
- **Funciones**: Declaración y definición de funciones (métodos estáticos) en
Java. Parámetros y retorno de métodos. Sobrecarga de métodos. Diferencias entre
funciones en C y métodos en Java.

#### Actividades prácticas

Las prácticas de esta unidad están diseñadas para consolidar la transición de C
a Java, aprovechando el conocimiento previo de los estudiantes y
familiarizándolos con el entorno de desarrollo y las herramientas que se
utilizarán durante todo el cuatrimestre.

##### TP1-2024 - Puesta en marcha

El objetivo de esta práctica es garantizar que todos estemos en la misma página
con respecto a las herramientas que utilizaremos en el cuatrimestre, así como
familiarizarnos con el ciclo de trabajo.

Durante la cursada, utilizaremos la última versión vigente del JDK LTS e IntelliJ 
IDEA Community Edition como entorno de desarrollo integrado, y los proyectos
individuales están basados en Gradle con una amplia gama de herramientas de verificación
integradas.

##### TP2-2024 - Java desde C

El objetivo de esta práctica es aprovechar que la sintaxis del lenguaje que
utilizaremos es prácticamente la misma para familiarizarnos con las diferencias
del lenguaje con respecto a C, reutilizando consignas de Programación I.

En esta práctica, no está permitido el uso de la librería estándar de Java, más
allá del `java.util.Scanner`, forzando a los estudiantes a implementar
soluciones algorítmicas sin depender de abstracciones de alto nivel.

#### Bibliografía obligatoria

Schildt, H. (2022). _Java: A Beginner's Guide_ (Ninth edition). McGraw Hill.

#### Bibliografía complementaria

Lopez Roman, L. (2011). _Programación Estructurada y Orientada a Objetos: Un
Enfoque Algorítmico_. Alfaomega Grupo Editor.

(unidad-2)=
### Unidad 2: Excepciones, arreglos y archivos

Tema previsto para ser dictado en dos semanas de dictado de clases.

#### Contenidos

El objetivo de esta unidad es comprender el manejo de excepciones en Java,
situándolas en el contexto de la transición entre la programación estructurada
clásica y la orientación a objetos. Además, se abordarán los arreglos (`arrays`)
y el manejo de archivos, mostrando cómo estos temas aplican las dos principales
familias de excepciones en Java. Este enfoque permite a los estudiantes entender
la gestión de errores como parte integral del diseño de software robusto.

**Manejo de Excepciones**: Introducción a las excepciones en Java. Bloques
`try-catch-finally`. Diferencias en el manejo de errores entre C y Java.
Excepciones _checked_ y _unchecked_. Documentación de excepciones según
{ref}`regla-0x1002`.

**Arreglos en Java**: Declaración e inicialización de arreglos. Diferencias con
los arreglos en C. Excepciones asociadas a operaciones con arreglos
(`ArrayIndexOutOfBoundsException`).

**Entrada/Salida (I/O)**: Introducción a las clases de I/O en Java (`java.io` y
`java.nio`). Lectura y escritura de archivos en Java. Comparación entre
funciones de I/O en C y métodos de I/O en Java. Manejo de
{ref}`excepciones checked <regla-0x3001>` en operaciones de archivos.

#### Actividades prácticas

Las prácticas de esta unidad introducen gradualmente el concepto de excepciones,
primero con arreglos (que lanzan excepciones _unchecked_) y luego con archivos
(que requieren manejo de excepciones _checked_), permitiendo a los estudiantes
comprender ambas familias de excepciones de forma progresiva.

##### TP3-2024 - Arreglos y excepciones

El objetivo de esta práctica es familiarizarnos con los arreglos en Java, viendo
las diferencias con los de C y también tener el primer contacto con excepciones,
siendo que las operaciones con arreglos naturalmente lanzan excepciones sin tipo
(`ArrayIndexOutOfBoundsException`, `NullPointerException`).

##### TP4-2024 - Archivos y excepciones

El objetivo de esta práctica es comenzar a utilizar más clases y trabajar con
archivos empleando la API `java.nio` y seguir trabajando con excepciones, ya que
los mismos lanzan excepciones con tipo (`IOException`, `NoSuchFileException`),
introduciendo el manejo obligatorio de excepciones _checked_.

#### Bibliografía obligatoria

Schildt, H. (2022). _Java: A Beginner's Guide_ (Ninth edition). McGraw Hill.

#### Bibliografía complementaria

Lopez Roman, L. (2011). _Programación Estructurada y Orientada a Objetos: Un
Enfoque Algorítmico_. Alfaomega Grupo Editor.

(unidad-3)=
### Unidad 3: Orientación a Objetos

Tema previsto para ser dictado en cuatro semanas de dictado de clases.

#### Contenidos

El objetivo de esta unidad es proporcionar a los estudiantes una comprensión
sólida de los conceptos fundamentales de la programación orientada a objetos
(POO), el análisis y diseño orientado a objetos, y su aplicación en Java. Se
explorarán temas clave como la definición y distinción entre clase y objeto,
asociaciones, ciclo de vida de un objeto, encapsulamiento, colaboración entre
objetos, herencia, identidad de objetos, el protocolo `equals`/`hashCode` y los
diferentes tipos de polimorfismo.

- **Conceptos fundamentales**: Clases y objetos. Estado y comportamiento.
Constructores y ciclo de vida de objetos. La clase `Object` y sus métodos
fundamentales.
- **Encapsulamiento**: Modificadores de acceso según {ref}`regla-0x2001`. Getters
y setters: cuándo usarlos y cuándo no, según {ref}`regla-0x2008`. Inmutabilidad
y sus ventajas.
- **Herencia y polimorfismo**: Herencia de clases. Sobrescritura de métodos.
Polimorfismo de subtipos. Clases abstractas e interfaces. Diferencias y casos de
uso apropiados.
- **Identidad de objetos**: Implementación del protocolo `equals`/`hashCode` según
{ref}`regla-0x2004` y {ref}`regla-0x200E`. Comparación de objetos con
`Comparable` y `Comparator`.
- **Genéricos**: Introducción a tipos genéricos. Clases y métodos genéricos.
Restricciones de tipo (_bounded types_). Aplicación en estructuras de datos
reutilizables para su uso en la {ref}`unidad-6`.

#### Actividades prácticas

Las prácticas de esta unidad alternan entre análisis conceptual y implementación
práctica, permitiendo a los estudiantes desarrollar tanto la capacidad de
modelado como las habilidades de codificación orientada a objetos.

##### TP5-2024 - Análisis Orientado a Objetos I

El objetivo de esta práctica es ir introduciéndonos en los conceptos de clases y
objetos a través del análisis de diferentes contextos, pero también para ir
viendo cómo otras personas ven la misma cosa, al requerir de la revisión entre
pares.

##### TP6-2024 - Arreglos III

El objetivo de esta práctica es tomar las funciones de los prácticos 3 y 4 en
clases que encapsulen un arreglo en uno propio, incluyendo funcionalidad para
guardar en archivos a partir del mismo, aplicando los principios de
{ref}`encapsulamiento <regla-0x2001>`.

##### TP7-2024 - Análisis Orientado a Objetos II

En este trabajo, le daremos una nueva vuelta de tuerca a los contextos
analizados en el TP5, incorporando lo visto de Orientación a Objetos. Esta
práctica también se desarrolló con revisión de pares en el Campus, promoviendo
el aprendizaje colaborativo y la discusión de diferentes enfoques de diseño.

##### TP8-2024 - Calculadora

El objetivo de esta práctica es implementar una calculadora orientada a objetos
aplicando polimorfismo, una de las técnicas más importantes del paradigma. Se
trabajará con jerarquías de clases y el patrón Composite para construir
expresiones matemáticas complejas.

##### TP9-2024 - Arreglos Genéricos

El objetivo de esta práctica es continuar desarrollando el arreglo desarrollado
en el TP6, haciendo que el mismo emplee genéricos, para que pueda almacenar
cualquier tipo de referencia, aplicando el polimorfismo paramétrico.

#### Bibliografía obligatoria

Schildt, H. (2022). _Java: A Beginner's Guide_ (Ninth edition). McGraw Hill.

#### Bibliografía complementaria

Lopez Roman, L. (2011). _Programación Estructurada y Orientada a Objetos: Un
Enfoque Algorítmico_. Alfaomega Grupo Editor.

(unidad-4)=
### Unidad 4: Patrones de diseño

Tema pensado para ser dictado en tres semanas de cursado.

#### Contenidos

El objetivo de esta unidad es introducir a los estudiantes a los patrones de
diseño orientado a objetos, mostrando cómo estos patrones pueden resolver
problemas comunes en el desarrollo de software. Los estudiantes aprenderán a
reconocer, aplicar y adaptar estos patrones en sus proyectos de Java, mejorando
la calidad y mantenibilidad de su código mediante soluciones probadas y
documentadas por la comunidad de desarrollo de software.

**Introducción a los Patrones de Diseño**: Definición y origen de los patrones
de diseño. El catálogo Gang of Four (GoF). Importancia de los patrones de diseño
en el desarrollo de software. Clasificación de los patrones de diseño:
creacionales, estructurales y de comportamiento.

- **Patrones Creacionales**: Singleton, Factory Method, Abstract Factory. Control
de la creación de objetos.
- **Patrones Estructurales**: Decorator, Adapter, Facade, Composite. Composición
de clases y objetos.
- **Patrones de Comportamiento**: Strategy, Observer, Template Method, Iterator.
Interacción entre objetos y distribución de responsabilidades.

#### Actividades prácticas

La práctica de esta unidad consolida todos los conceptos de POO vistos hasta el
momento, aplicándolos en el contexto de patrones de diseño reconocidos.

##### TP10-2024 - Patrones de Diseño

En esta práctica, extenderemos el arreglo del Trabajo Práctico anterior para que
el mismo emplee patrones de diseño como Decorator, Strategy, Observer e
Iterator, demostrando cómo los patrones mejoran la extensibilidad y
mantenibilidad del código.

#### Bibliografía obligatoria

Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1995). _Design Patterns:
Elements of Reusable Object-Oriented Software_. Addison-Wesley.

#### Bibliografía complementaria

Sin bibliografía complementaria.

(unidad-5)=
### Unidad 5: Principios SOLID y Refactorización

Tema previsto para ser dictado en una semana de clases.

#### Contenidos

El objetivo de esta unidad es proporcionar a los estudiantes una comprensión
profunda de los principios SOLID, que son fundamentales para el diseño orientado
a objetos de software robusto y mantenible, así como técnicas de refactorización
para mejorar el código existente. También se introducirá el concepto de deuda
técnica y cómo gestionarla en proyectos de software.

- **Principios SOLID**: Single Responsibility Principle (SRP). Open/Closed
Principle (OCP). Liskov Substitution Principle (LSP). Interface Segregation
Principle (ISP). Dependency Inversion Principle (DIP). Aplicación práctica de
cada principio según {ref}`regla-0x200D`.
- **Refactorización**: Concepto de refactorización. Técnicas de refactorización
comunes (_Extract Method_, _Rename_, _Extract Class_). Herramientas de
refactorización en el IDE.
- **Deuda Técnica**: Definición y causas de la deuda técnica. Identificación y
medición de deuda técnica. Estrategias para gestionar y reducir la deuda
técnica.

#### Actividades prácticas

Esta práctica enfoca en el análisis crítico del código producido durante el
cuatrimestre, aplicando los principios SOLID como marco de evaluación.

##### TP11-2024 - SOLIDificación de las prácticas

En esta práctica analizaremos el código que hemos desarrollado antes, indicando
si cumple o no con los criterios SOLID, así como el planteo de las
refactorizaciones necesarias para que lo haga. Se espera que los estudiantes
identifiquen violaciones de principios y propongan soluciones concretas mediante
refactorización.

#### Bibliografía obligatoria

Martin, R. C. (2009). _Clean Code: A Handbook of Agile Software Craftsmanship_.
Prentice Hall.

#### Bibliografía complementaria

Sin bibliografía complementaria.

(unidad-6)=
### Unidad 6: Estructuras de datos

Tema pensado para ser dictado en cinco semanas de clases.

#### Contenidos

El objetivo de esta unidad es proporcionar a los estudiantes una comprensión
completa de las estructuras de datos fundamentales y los tipos abstractos de
datos (TAD). Los estudiantes aprenderán a implementar y utilizar diversas
estructuras de datos en Java, comprendiendo sus características, ventajas,
desventajas y análisis de complejidad temporal y espacial.

- **Tipos Abstractos de Datos (TAD)**: Concepto de TAD. Separación entre interfaz
e implementación. Ventajas del uso de TADs en el diseño de software.
- **Estructuras Lineales**: Listas enlazadas (simples, dobles, circulares). Pilas
(stack) y sus aplicaciones. Colas (queue) y colas de prioridad. Implementaciones
basadas en arreglos y en nodos.
- **Estructuras No Lineales**: Árboles binarios y árboles binarios de búsqueda.
Árboles balanceados (conceptos básicos). Grafos: representación y recorridos
básicos.
- **Tablas Hash**: Concepto de función hash. Manejo de colisiones. Implementación
de diccionarios.
- **Análisis de Complejidad**: Notación Big-O. Análisis temporal y espacial de
operaciones en estructuras de datos. Comparación de eficiencia entre diferentes
implementaciones.

#### Actividades prácticas

Las prácticas de esta unidad se centran en la implementación de estructuras de
datos desde cero, aplicando todos los conceptos vistos durante el cuatrimestre.

##### TP12-2024 - Estructuras basadas en Nodos

En esta práctica implementaremos estructuras de datos basadas en nodos (listas
enlazadas, pilas y colas) y les daremos uso en aplicaciones simples,
comprendiendo las ventajas de las estructuras dinámicas sobre las estáticas.

##### TP13-2024 - Árboles

Aquí implementaremos un árbol binario de búsqueda, incluyendo operaciones de
inserción, búsqueda, eliminación y recorridos (inorden, preorden, postorden),
analizando la complejidad de cada operación.

#### Bibliografía obligatoria

Liang, Y. D. (2017). _Introduction to Java Programming and Data Structures_
(11th edition). Pearson.

#### Bibliografía complementaria

Sin bibliografía complementaria.

(evaluacion)=
## Propuesta de evaluación

La evaluación del curso se estructura en múltiples instancias que permiten
valorar tanto el proceso de aprendizaje como el producto final, combinando
evaluaciones parciales, trabajos prácticos semanales y un trabajo integrador
final.

### Estructura general

La evaluación del curso consiste en dos exámenes parciales con una única
instancia de recuperación y un Trabajo Integrador de mayor complejidad que una
práctica.

Esta asignatura es posible de ser promocionada sin examen final, cumpliendo los
requisitos especificados en la sección de
{ref}`promoción <requisitos-promocion>`.

Para acceder a los recuperatorios, es necesario rendir el examen a ser
recuperado, salvo justificación _previa_ debidamente documentada.

Pueden rendir el examen recuperatorio para subir la nota e intentar llegar a la
zona de promoción, pero teniendo en cuenta que la nueva calificación reemplaza a
la anterior, incluso si fuera inferior.

La cátedra tendrá en cuenta los aspectos de participación en clase y en el foro
de intercambio
[INGCOM-UNRN-PII/discussions](https://github.com/orgs/INGCOM-UNRN-PII/discussions)
en la nota final, valorando la contribución activa al aprendizaje colectivo.

### Evaluación de los Trabajos Prácticos

La evaluación de los mismos se apoya en la utilización de herramientas de
análisis automático (PMD, CheckStyle, SpotBugs, JaCoCo), que proporcionan
retroalimentación inmediata sobre calidad de código, estilo y cobertura de
tests. A pesar de que no es necesario resolver el 100% de los comentarios que
estas hacen, si hay una gran cantidad de observaciones, probablemente esté
faltando algo.

La cátedra está abierta a la discusión y conversación sobre cada regla de estilo
documentada en las {ref}`reglas de estilo <regla-0x0000>`, fomentando el
pensamiento crítico sobre las convenciones de código.

(requisitos-regularizacion)=
### Requisitos regularización

Para la regularización de la materia es necesario cumplir con los siguientes
requisitos mínimos que demuestran un nivel adecuado de comprensión de los
contenidos:

- Obtener una nota superior a 4 en los parciales (≥60%) (o en el recuperatorio)
- Entregar todas las prácticas en tiempo y forma
- Ninguna práctica rechazada por incumplimiento grave de consignas

(requisitos-promocion)=
### Requisitos promoción

Para la promoción de la cátedra, es necesario cumplir con requisitos más
exigentes que demuestren un dominio sólido de los contenidos:

- Cumplir los {ref}`requisitos de regularización <requisitos-regularizacion>`
- Obtener una nota superior a siete (7 / 80%) en las evaluaciones parciales
- Un máximo de una (1) práctica con revisiones pendientes

La nota final será el promedio entre las evaluaciones parciales, considerando
también la participación activa en las clases y el foro de discusión.

(requisitos-tpi)=
### Requisitos Trabajo Integrador

Este proyecto final tiene como objetivo principal evaluar la comprensión y
aplicación de los principios fundamentales de la Programación Orientada a
Objetos (POO). Se espera que demuestren su habilidad para diseñar, implementar y
probar soluciones robustas, haciendo uso de patrones de diseño adecuados y
estructuras de datos eficientes para resolver problemas complejos.

Las consignas planteadas están para guiarlos, y no para ser completadas tal cual
fueron planteadas. Se valora la creatividad y la capacidad de adaptación de las
soluciones al contexto específico del problema.

La entrega del repositorio para revisión es en el mismo formulario que el usado
para los Trabajos Prácticos y debe ser 24 horas antes de la defensa del mismo;
**sin excepciones**.

#### Criterios de evaluación

La evaluación del proyecto se basará en los siguientes criterios:

- **Funcionalidad del proyecto** (25%)
  - El proyecto debe compilar, ejecutarse sin errores y operar correctamente.
  - La aplicación debe ser estable y robusta, manejando apropiadamente casos
    excepcionales.
- **Estructura y diseño del código** (25%)
  - La estructura del código debe ser clara, modular y seguir los principios de
    la Programación Orientada a Objetos vistos en clase.
  - Se espera la aplicación de patrones de diseño cuando sea apropiado,
    demostrando una comprensión de su utilidad.
  - Uso adecuado de estructuras de datos para optimizar el rendimiento y la
    organización de la información.
- **Encapsulamiento y abstracción** (15%)
  - El encapsulamiento debe ser respetado rigurosamente según las
    {ref}`reglas establecidas <regla-0x2001>`.
  - Uso efectivo de la abstracción, interfaces y herencia para modelar el
    dominio del problema.
- **Participación del equipo** (10%)
  - Se observará en el historial del repositorio de control de versiones la
    participación activa y equitativa de todos los miembros del equipo.
  - Los commits deben ser significativos y reflejar el trabajo individual y
    colaborativo.
- **Documentación y legibilidad del código** (10%)
  - La documentación externa (README, diagramas UML) debe ser completa, clara y
    ser consistente con lo implementado.
  - El código debe ser legible, bien comentado según {ref}`regla-0x000D` y
    seguir las {ref}`convenciones de nomenclatura <regla-0x0001>` indicadas en
    clase.
- **Pruebas automáticas** (15%)
  - Implementación de tests rigurosos según {ref}`regla-0x4001` para el
    aseguramiento de la calidad y fiabilidad del código.
  - Una cobertura de los mismos razonable, en el orden del 70%.

(requisitos-libre)=
### Requisitos de aprobación libre

Se establece el régimen para las estudiantes que deseen rendir libre la
asignatura. Este régimen permite que estudiantes que no puedan cursar
regularmente demuestren el dominio de los contenidos mediante un proyecto
integral.

Quienes deseen rendir libre la asignatura deben:

- Contactarse con la cátedra con antelación (al menos 3 semanas antes del
  examen) para solicitar una consigna y el repositorio de trabajo.
- El proyecto a desarrollar debe aplicar las técnicas y herramientas descritas
  en el presente programa, incluyendo POO, patrones de diseño, estructuras de
  datos y tests.
- Este código será la base sobre la cual se harán preguntas conceptuales y
  solicitudes de cambio durante el examen con el objetivo de evaluar la
  comprensión de los temas.

Este ejercicio será evaluado como un TPI (ver
{ref}`rúbrica específica <requisitos-tpi>`) y el tiempo para su completado será
de aproximadamente una semana desde su asignación.

(cronograma)=
## Cronograma genérico

Este cronograma se establece como el ideal; consulten el específico del año de
cursada que será compartido en el Campus Bimodal para la implementación concreta
(por feriados y otros detalles similares).

Se prevé, aproximadamente, la entrega de un Trabajo Práctico por semana, salvo
en las semanas que se tome una evaluación parcial, permitiendo a los estudiantes
consolidar gradualmente los conocimientos adquiridos.

El primer parcial se ubicará en la semana 4 y evaluará las
{ref}`unidades 1 <unidad-1>` y {ref}`2 <unidad-2>`, correspondientes a la
transición de C a Java y el manejo básico de excepciones.

El segundo parcial será en la semana 12 y evaluará las
{ref}`unidades 3 <unidad-3>` a {ref}`5 <unidad-5>`, cubriendo POO, patrones de
diseño y principios SOLID.

En la semana 15 se ubicarán los recuperatorios, mientras que la defensa del TPI
será en la número 16, para maximizar la cantidad de alumnos que promocionen la
materia.

(uso-ia)=
## Declaración del uso de IA

Esta cátedra fomentará el uso de herramientas de programación basadas en LLM
(Large Language Models) como apoyo en el proceso de aprendizaje y desarrollo de
software, reconociendo que estas herramientas forman parte del ecosistema
profesional actual. Sin embargo, tendrá tolerancia cero durante las evaluaciones
parciales, donde se espera que los estudiantes demuestren comprensión genuina de
los conceptos sin asistencia artificial.

Para más detalles sobre el uso responsable de IA, las expectativas de la cátedra
y las consecuencias del uso indebido durante evaluaciones, ver el
[acuerdo de uso](acuerdo_ia.md) en su página dedicada.
