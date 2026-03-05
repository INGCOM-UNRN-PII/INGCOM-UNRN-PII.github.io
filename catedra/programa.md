---
title: Programa Analitico
description:
  Reglas de estilo y correcciones generales para código Java en la cátedra de
  Programación II.
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

## Fundamentación

La programación es uno de los pilares fundamentales de la carrera, dado que
forma la base sobre la cual se construyen todas las demás habilidades técnicas y
analíticas de un profesional en el área de la informática. Por este motivo, el
objetivo principal de esta asignatura es que los estudiantes aprendan a
programar de forma correcta y que sean capaces de desarrollar software de alta
calidad y de manera eficiente desde los inicios de su acercamiento a la
programación como disciplina. Esta cátedra adopta la estrategia "Late-Objects"
formulada por Cay Horstmann, para simplificar la transición desde C al
introducir el grueso de los nuevos conceptos, luego de tratar la mayor cantidad
de similitudes con el lenguaje de programación C, visto en la cátedra
correlativa.

La cátedra hace un uso intensivo de herramientas de análisis y verificación
automática, no solo por la realimentación a los estudiantes y la velocidad de la
misma, sino por el uso que estas herramientas tienen en su futura carrera
profesional.

Se les otorgarán las herramientas para que los estudiantes puedan diseñar una
solución y luego implementarla a partir de una mirada más amplia del concepto de
artefacto de software.

## Propósitos de la asignatura

### Comprensión Profunda del Lenguaje Java

Java es un lenguaje de programación ampliamente utilizado en la industria por su
robustez, portabilidad y seguridad. Los estudiantes aprenderán a manejar este
lenguaje de manera avanzada, comprendiendo sus características y aplicando las
mejores prácticas en su uso.

### Diseño Orientado a Objetos

Se enfatizará el uso de la programación orientada a objetos (POO) para
estructurar el software de manera modular y reutilizable. Los estudiantes
aprenderán a diseñar y desarrollar sistemas complejos utilizando principios
sólidos de POO.

### Resolución de Problemas

Se desarrollarán habilidades para descomponer problemas complejos en partes
manejables y diseñar soluciones eficientes. Se fomentará un pensamiento crítico
y una metodología sistemática para abordar y resolver problemas.

### Calidad y Mantenibilidad del Código

Se destacará la importancia de escribir código limpio, documentado y fácilmente
mantenible. Los estudiantes aprenderán sobre pruebas unitarias, refactorización
y otras prácticas que mejoran la calidad del software.

### Trabajo en Equipo y Herramientas de Desarrollo

Se promoverá el trabajo colaborativo y el uso de herramientas modernas de
desarrollo, como sistemas de control de versiones y entornos de desarrollo
integrados (IDEs).

### Aplicaciones de estructuras de datos, algoritmos y análisis de complejidad

Tema visto en parte en Programación 1 y profundizados aquí con usos prácticos.

## Contenidos mínimos según plan de estudio

Entrada/salida de información por archivos. Archivos de acceso aleatorio.
Memoria dinámica; reserva, liberación de memoria y ciclo de vida de objetos.
Puntero a función. Patrones de diseño. Refactorización y Deuda Técnica.
Estructuras de datos. Tipos de datos definidos por el usuario (TAD: Tipo
Abstracto de Dato). Listas, pilas, colas, tablas de hash, árboles, colas
priorizadas, conjuntos y grafos.

## Propuesta metodológica

La asignatura constará de clases teóricas y clases prácticas diferenciadas,
adoptando una estrategia práctica para maximizar el aprendizaje y la aplicación
de los conceptos enseñados.

En las clases teóricas se desarrollarán los temas del programa de la asignatura,
incluyendo múltiples ejemplos que faciliten la asimilación de los contenidos
conceptuales. Se utilizarán ejemplos prácticos para relacionar cada uno de los
temas vistos, fomentando la interacción del alumno con el objetivo de que logre
una actitud activa para, a través de su propio razonamiento, crear soluciones
creativas a problemas planteados durante las clases.

En las clases prácticas se plantearán distintos conjuntos de ejercicios para
favorecer la asimilación de los conceptos vistos en la materia. Al inicio de
cada sesión práctica, el profesor hará una breve explicación con ejemplos
similares a los ejercicios propuestos en la práctica. Los estudiantes trabajarán
en la resolución de estos ejercicios, aplicando los conocimientos teóricos en un
entorno práctico.

Se fomentará el uso de herramientas modernas de desarrollo de software para
mejorar la experiencia de aprendizaje y preparar a los estudiantes para el
entorno profesional. Las principales herramientas y metodologías a utilizar son:

- git para el control de cambios, permitiéndoles gestionar sus proyectos de
  manera eficiente y colaborar con sus compañeros.
- Github será utilizado como plataforma para alojar y compartir repositorios,
  facilitando el seguimiento del progreso de los estudiantes.
- La combinación de las herramientas de verificación PMD, CheckStyle, SpotBugs y
  JaCOCO con reglas personalizadas y adaptadas a la cátedra.
- Gradle Los estudiantes aprenderán a usar esta herramienta de construcción de
  software para gestionar las dependencias del proyecto, compilar el código y
  ejecutar las pruebas. Gradle simplifica el proceso de construcción y permite
  integrar diversas herramientas en el flujo de desarrollo.
- El entorno de desarrollo IntelliJ Community Edition, el cual es una
  herramienta de desarrollo avanzada que integra todas las herramientas
  descritas anteriormente.

Se fomentará el aprendizaje a través de la resolución de problemas prácticos,
estimulando a que los estudiantes propongan y programen soluciones creativas a
distintos problemas propuestos. Este enfoque práctico permitirá a los
estudiantes aplicar los conocimientos teóricos en situaciones reales,
desarrollando habilidades críticas para su futuro profesional.

Todo el contenido de la cátedra quedará alojado en repositorios Github que serán
actualizados a lo largo del cuatrimestre, y trabajados por cursada. Por ejemplo
[INGCOM-UNRN-PII/cursada-2026](https://github.com/INGCOM-UNRN-PII/cursada-2026)
contiene todo el material de las clases de dicho año.

## Ajustes para estudiantes con discapacidad

La UNRN desarrolla políticas en torno a accesibilidad académica para estudiantes
con discapacidad y acompañamiento de equipos docentes, en consonancia con las
normativas nacionales y orientaciones del CIN. Al inicio de la cursada se espera
contar con el relevamiento del espacio de APD (Asistencia Pedagógica en
discapacidad) sobre los/las estudiantes que cursen la materia con el fin de
identificar barreras y definir las configuraciones de apoyo necesarias.

## Unidades

### Unidad 1: Java desde C

Tema previsto para ser dictado en las dos primeras semanas de clase.

#### Contenidos

El objetivo de esta unidad es proporcionar a los estudiantes que ya tienen
conocimientos en programación en C una transición suave y comprensiva hacia la
programación en Java. Se abordarán las similitudes y diferencias entre ambos
lenguajes, facilitando la adaptación a Java mediante ejemplos y ejercicios
prácticos.

Introducción a Java; Historia y evolución de Java. Características principales
de Java. Comparación entre C y Java. Sintaxis y Estructura del Lenguaje
Estructura de un programa en Java; Diferencias sintácticas entre C y Java. Tipos
de datos y variables en Java. Operadores y expresiones en Java. Control de
Flujo; Instrucciones de control de flujo (`if`, `else`, `switch`). Bucles
(`for`, `while`, `do-while`). Comparación de estructuras de control entre C y
Java. Funciones; Declaración y definición de funciones (métodos estáticos) en
Java. Parámetros y retorno de métodos. Sobrecarga de métodos. Diferencias entre
funciones en C y métodos en Java.

#### Actividades prácticas

##### TP1-2024 - Puesta en marcha

El objetivo de esta práctica es garantizar que todos estemos en la misma página
con respecto a las herramientas que utilizaremos en el cuatrimestre, así como
familiarizarnos con el ciclo de trabajo.

Durante la cursada, utilizaremos la versión del JDK 17, la misma es la versión
de soporte extendido (LTS) vigente por el 2024. Utilizaremos IntelliJ Idea 2024
Community Edition como entorno de desarrollo integrado, y los proyectos
individuales están basados en Gradle.

##### TP2-2024 - Java desde C

El objetivo de esta práctica es aprovechar que la sintaxis del lenguaje que
utilizaremos es prácticamente la misma para familiarizarnos con las diferencias
del lenguaje con respecto a C, reutilizando consignas de Programación 1.

En esta práctica, no está permitido el uso de la librería estándar de Java, más
allá del `java.util.Scanner`

#### Bibliografía obligatoria:

Schildt, H. (2022). Java: A beginner’s guide (Ninth edition). McGraw Hill.

#### Bibliografía complementaria:

Lopez Roman, L. (2011). Programacion estructurada y orientada a objetos: Un
enfoque algoritmico. Alfaomega Grupo Editor.

### Unidad 2: Excepciones, arreglos y archivos

Tema previsto para ser dictado en dos semanas de dictado de clases.

#### Contenidos

El objetivo de esta unidad es comprender el manejo de excepciones en Java,
situándolas en el contexto de la transición entre la programación estructurada
clásica y la orientación a objetos. Además, se abordarán los arreglos (`arrays`)
y el manejo de archivos, mostrando cómo estos temas aplican las dos principales
familias de excepciones en Java.

Manejo de Excepciones; Introducción a las excepciones en Java. Bloques
`try-catch-finally`. Diferencias en el manejo de errores entre C y Java.
Entrada/Salida (I/O); Introducción a las clases de I/O en Java (`java.io` y
`java.nio`). Lectura y escritura de archivos en Java. Comparación entre
funciones de I/O en C y métodos de I/O en Java.

#### Actividades prácticas

##### TP3-2024 - Arreglos y excepciones

El objetivo de esta práctica es familiarizarnos con los arreglos en Java, viendo
las diferencias con los de C y también tener el primer contacto con Excepciones,
siendo que las operaciones con arreglos naturalmente lanzan excepciones sin
tipo.

##### TP4-2024 - Archivos y excepciones

El objetivo de esta práctica es comenzar a utilizar más clases y trabajar con
archivos empleando la API `java.nio` y seguir trabajando con excepciones, ya que
los mismos lanzan excepciones con tipo.

#### Bibliografía obligatoria:

Schildt, H. (2022). Java: A beginner’s guide (Ninth edition). McGraw Hill.

#### Bibliografía complementaria:

Lopez Roman, L. (2011). Programacion estructurada y orientada a objetos: Un
enfoque algoritmico. Alfaomega Grupo Editor.

### Unidad 3: Orientación a Objetos

Tema previsto para ser dictado en cuatro semanas de dictado de clases.

#### Contenidos:

El objetivo de esta unidad es proporcionar a los estudiantes una comprensión
sólida de los conceptos fundamentales de la programación orientada a objetos
(OOP), el análisis y diseño orientado a objetos, y su aplicación en Java. Se
explorarán temas clave como la definición y distinción entre clase y objeto,
asociaciones, ciclo de vida de un objeto, encapsulamiento, colaboración entre
objetos, herencia, identidad de objetos, el protocolo `equals`/`hashcode` y los
diferentes tipos de polimorfismo. Asimismo, se verá el tema `generics`,
polimorfismo de tipos, para su posterior uso en la unidad de estructuras de
datos.

#### Actividades prácticas

##### TP5-2024 - Análisis Orientado a Objetos I

El objetivo de esta práctica es ir introduciéndonos en los conceptos de clases y
objetos a través del análisis de diferentes contextos, pero también para ir
viendo como otras personas ven la misma cosa, al requerir de la revisión entre
pares.

##### TP6-2024 - Arreglos III

El objetivo de esta práctica es tomar las funciones de los prácticos 3 y 4 en
clases que encapsulen un arreglo en uno propio, incluyendo funcionalidad para
guardar en archivos a partir del mismo.

##### TP7-2024 - Análisis Orientado a Objetos II

En este trabajo, le daremos una nueva vuelta de tuerca a los contextos
analizados en el TP5, incorporando lo visto de Orientación a Objetos. Esta
práctica también se desarrolló con revisión de pares en el Campus.

##### TP8-2024 - Calculadora

El objetivo de esta práctica es implementar una calculadora orientada a objetos
aplicando polimorfismo, una de las técnicas más importantes del paradigma.

##### TP9-2024 - Arreglos Genéricos

El objetivo de esta práctica, es continuar desarrollando el arreglo desarrollado
en el TP6, haciendo que el mismo emplee Genéricos, para que pueda almacenar
cualquier tipo de referencia.

#### Bibliografía obligatoria:

Bibliografía obligatoria: Schildt, H. (2022). Java: A beginner’s guide (Ninth
edition). McGraw Hill.

#### Bibliografía complementaria:

Lopez Roman, L. (2011). Programación estructurada y orientada a objetos: Un
enfoque algorítmico. Alfaomega Grupo Editor.

### Unidad 4: Patrones de diseño

Tema pensado para ser dictado en tres semanas de cursado.

#### Contenidos

El objetivo de esta unidad es introducir a los estudiantes a los patrones de
diseño orientado a objetos, mostrando cómo estos patrones pueden resolver
problemas comunes en el desarrollo de software. Los estudiantes aprenderán a
reconocer, aplicar y adaptar estos patrones en sus proyectos de Java, mejorando
la calidad y mantenibilidad de su código. Introducción a los Patrones de Diseño;
Definición y origen de los patrones de diseño. Importancia de los patrones de
diseño en el desarrollo de software. Clasificación de los patrones de diseño:
creacionales, estructurales y de comportamiento.

#### Actividades prácticas

##### TP10-2024 - Patrones de Diseño;

En esta práctica, extenderemos el arreglo del Trabajo Práctico anterior, para
que el mismo emplee patrones de diseño.

#### Bibliografía obligatoria:

Gamma, E. (Ed.). (1995). Design patterns: Elements of reusable object-oriented
software. Addison-Wesley.

#### Bibliografía complementaria:

Sin bibliografía complementaria

### Unidad 5: Principios SOLID y Refactorización

Tema previsto para ser dictado en una semana de clases.

#### Contenidos

El objetivo de esta unidad es proporcionar a los estudiantes una comprensión
profunda de los principios SOLID, que son fundamentales para el diseño orientado
a objetos de software, así como técnicas de refactorización para mejorar el
código existente. También se introducirá el concepto de deuda técnica y cómo
gestionarla en proyectos de software.

#### Actividades prácticas

##### TP11-2024 - SOLIDificación de las prácticas;

En esta práctica analizaremos el código que hemos desarrollado antes, indicando
si cumple o no con los criterios SOLID, así como el planteo de las
refactorizaciones para que lo haga.

#### Bibliografía obligatoria:

Martin, R. C. (Ed.). (2009). Clean code: A handbook of agile software
craftsmanship. Prentice Hall.

#### Bibliografía complementaria:

Sin bibliografía complementaria

### Unidad 6: Estructuras de datos

Tema pensado para ser dictado en cinco semanas de clases

#### Contenidos:

El objetivo de esta unidad es proporcionar a los estudiantes una comprensión
completa de las estructuras de datos fundamentales y los tipos abstractos de
datos (TAD). Los estudiantes aprenderán a implementar y utilizar diversas
estructuras de datos en Java, comprendiendo sus características, ventajas y
desventajas.

#### Actividades prácticas

##### TP12-2024 - Estructuras basadas en Nodos

En esta práctica, implementaremos estructuras de datos basadas en Nodos y les
daremos uso en aplicaciones simples.

##### TP13-2024 - Árboles

Aquí implementaremos un árbol binario.

#### Bibliografía obligatoria:

Introduction to Java Programming and Data Structures - 11ava edición, Y. Daniel
Liang - Pearson

#### Bibliografía complementaria:

Sin bibliografía complementaria

## Propuesta de evaluación

La evaluación del curso consiste en dos exámenes parciales con una única
instancia de recuperación y un Trabajo Integrador de mayor complejidad que una
práctica.

Esta asignatura es posible de ser promocionada sin examen final.

Para acceder a los recuperatorios, es necesario rendir el examen a ser
recuperado, salvo justificación _previa_.

Pueden rendir el examen recuperatorio para subir la nota e intentar llegar a la
zona de promoción, pero teniendo en cuenta que

La cátedra tendrá en cuenta los aspectos de participación en clase y en el foro
de intercambio
[INGCOM-UNRN-PII/discussions](https://github.com/orgs/INGCOM-UNRN-PII/discussions)
en la nota final.

### Evaluación de los Trabajos Prácticos

La evaluación de los mismos se apoya en la utilización de herramientas de
análisis automático, a pesar de que no es necesario resolver el 100% de los
comentarios que estas hacen, si hay una gran cantidad de observaciones,
probablemente este faltando algo.

La cátedra está abierta a la discusión y conversación sobre cada regla de
estilo.

### Requisitos regularización

Para la regularización de la materia es necesario:

- Obtener una nota superior a 4 en los parciales (>=60%) (o en el recuperatorio)
- Entregar todas las prácticas
- Ninguna práctica rechazada

### Requisitos promoción

Para la promoción de la cátedra, es necesario:

- Cumplir los requisitos de regularización.
- Obtener una nota superior a siete (7 / 80%) en las evaluaciones parciales
- Un máximo de una (1) práctica con revisiones pendientes.

La nota final será el promedio entre las evaluaciones parciales.

### Requisitos Trabajo Integrador

Este proyecto final tiene como objetivo principal evaluar la comprensión y
aplicación de los principios fundamentales de la Programación Orientada a
Objetos (POO). Se espera que demuestren su habilidad para diseñar, implementar y
probar soluciones robustas, haciendo uso de patrones de diseño adecuados y
estructuras de datos eficientes para resolver problemas complejos.

Las consignas planteadas están para guiarlos, y no para ser completadas tal cual
fueron planteadas.

La entrega del repositorio para revisión es en el mismo formulario que el usado
para los Trabajos Prácticos y debe ser 24 horas antes de la defensa del mismo;
**sin excepciones**.

La evaluación del proyecto se basará en los siguientes criterios:

- **Funcionalidad del proyecto** (25 %)
  - El proyecto debe compilar, ejecutarse sin errores y operar correctamente.
  - La aplicación debe ser estable y robusta.
- **Estructura y diseño del código** (25 %)
  - La estructura del código debe ser clara, modular y seguir los principios de
    la Programación Orientada a Objetos vistos en clase.
  - Se espera la aplicación de patrones de diseño cuando sea apropiado,
    demostrando una comprensión de su utilidad.
  - Uso adecuado de estructuras de datos para optimizar el rendimiento y la
    organización de la información.
- **Encapsulamiento y abstracción** (15 %)
  - El encapsulamiento debe ser respetado rigurosamente.
  - Uso efectivo de la abstracción, interfaces y herencia para modelar el
    dominio del problema.
- **Participación del equipo** (10 %)
  - Se observará en el historial del repositorio de control de versiones la
    participación activa y equitativa de todos los miembros del equipo.
  - Los commits deben ser significativos y reflejar el trabajo individual y
    colaborativo.
- **Documentación y legibilidad del código** (10 %)
  - La documentación externa (README, diagramas UML) debe ser completa, clara y
    ser consistente con lo implementado.
  - El código debe ser legible, bien comentado y seguir los estándares de
    codificación indicados en clase.
- **Pruebas automáticas** (15 %)
  - Implementación de tests rigurosos para el aseguramiento de la calidad y
    fiabilidad del código.
  - Una cobertura de los mismos razonable, en el orden del 70%.

### Requisitos de aprobación libre

Se establece el régimen para las/los estudiantes que deseen rendir libre la
asignatura deben:

- Contactarse con la cátedra con antelación para solicitar una consigna y el
  repositorio de trabajo.
- El proyecto a desarrollar debe aplicar las técnicas y herramientas descritas
  en el presente programa.
- Este código será la base sobre la cual se harán preguntas conceptuales y
  solicitudes de cambio durante el examen con el objetivo de evaluar la
  comprensión de los temas.

Este ejercicio será evaluado como un TPI (ver rúbrica específica) y el tiempo
para su completado será de aproximadamente una semana.

### Cronograma genérico

Este cronograma se establece como el 'ideal', consulten el específico del año
de cursada que será compartido en el Campus Bimodal para la implementación
concreta (por feriados y otros detalles similares).

Se prevé aproximadamente, la entrega de un Trabajo Práctico por semana, salvo en
las semanas que se tome una evaluación parcial.

El primer parcial se ubicará en la semana 4 y evaluará las unidades 1 y 2.

El segundo parcial será en la semana 12 y evaluará las unidades 3 a 5.

En la semana 15, se ubicarán los recuperatorios mientras que la defensa del TPI
será en la número 16, para maximizar la cantidad de alumnos que promocionen.

## Declaración del uso de IA

Esta cátedra fomentará el uso de herramientas de programación basadas en LLM's,
pero, tendrá tolerancia cero durante las evaluaciones.

Para más detalles, ver el acuerdo de uso en su página dedicada.