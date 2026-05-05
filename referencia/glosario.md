---
title: Glosario
description: Glosario de terminología técnica utilizada en la cátedra de Programación II.
---

# Glosario de terminología

```{glossary}
Abstracción
: Principio de OOP que consiste en capturar las características esenciales de un objeto, descartando los detalles no relevantes para el contexto actual.

Agregación
: Relación entre dos clases donde una "tiene un" objeto de la otra, pero ambos pueden existir de forma independiente (relación débil).

Algoritmo
: Conjunto ordenado y finito de instrucciones que resuelven un problema.

API
: *Application Programming Interface*. Conjunto de definiciones de protocolos para la comunicación entre componentes de software.

Asociación
: Relación general entre dos clases donde los objetos de una se comunican con los de la otra.

Atributo
: Variable que representa una característica o propiedad de una clase y define el estado de sus objetos.

Bytecode
: Código intermedio generado por el compilador de Java que es ejecutado por la JVM. Es portable entre diferentes sistemas operativos.

Clase
: Plantilla o molde que define la estructura (atributos) y comportamiento (métodos) de los objetos.

Clase Abstracta
: Clase que no puede ser instanciada directamente y que sirve como base para otras subclases, definiendo métodos que estas deben implementar.

Code Smell
: Síntoma en el código fuente que indica un posible problema de diseño o mala práctica. No es un error técnico, pero dificulta el mantenimiento.

Colección
: Objeto que agrupa múltiples elementos en una sola unidad (ej: Listas, Conjuntos, Mapas).

Compilador
: Programa que traduce código fuente a código máquina o bytecode.

Composición
: Relación fuerte entre dos clases donde una contiene a la otra y la vida de los objetos contenidos depende de la existencia del contenedor ("parte-todo").

Constructor
: Método especial que se ejecuta automáticamente al crear una instancia de una clase para inicializar su estado.

Encapsulamiento
: Principio de OOP que consiste en ocultar el estado interno de un objeto y protegerlo de accesos externos indebidos, exponiendo solo lo necesario a través de métodos.

Exception
: Error que ocurre durante la ejecución de un programa y que puede ser capturado y manejado.

Genéricos
: Característica de Java que permite definir clases o métodos con tipos de datos variables, proporcionando seguridad de tipos en tiempo de compilación.

Gradle
: Herramienta de automatización de construcción (build tool) utilizada en la cátedra para compilar, testear y ejecutar proyectos Java.

Heap
: Área de memoria de la JVM donde se almacenan los objetos creados dinámicamente durante la ejecución.

Herencia
: Mecanismo de OOP que permite que una clase herede atributos y métodos de otra clase.

IDE
: Del inglés, *Integrated Development Environment*. Entorno Integrado de Desarrollo. Un conjunto de herramientas empaquetadas para asistir al desarrollo de software.

Instancia
: Un objeto concreto creado a partir de una clase.

Interfaz
: Contrato que especifica qué métodos debe implementar una clase, sin definir cómo lo hacen.

Javadoc
: Herramienta y formato de documentación para generar páginas HTML que describen la API de un proyecto Java a partir de comentarios especiales en el código.

JDK
: *Java Development Kit*. Kit de desarrollo que incluye el compilador, la JVM y las librerías necesarias para programar en Java.

JUnit
: Framework de testing para Java utilizado para crear y ejecutar pruebas unitarias automatizadas.

JVM
: *Java Virtual Machine*. Máquina virtual que ejecuta el bytecode de Java.

Lazo
: Estructura de control que permite repetir un bloque de código mientras se cumpla una condición (comúnmente llamado bucle).

Método
: Función asociada a una clase que define el comportamiento de los objetos.

Objeto
: Instancia de una clase que contiene datos (atributos) y comportamiento (métodos).

Paquete (Package)
: Mecanismo para organizar clases e interfaces relacionadas en grupos, evitando conflictos de nombres.

Parámetro
: Variable definida en la firma de un método que recibe un valor (argumento) cuando el método es invocado.

Polimorfismo
: Capacidad de un objeto de tomar múltiples formas o la capacidad de métodos de tener múltiples implementaciones bajo un mismo nombre.

Refactorización
: Proceso de reestructurar el código existente sin cambiar su comportamiento externo, con el fin de mejorar su legibilidad, mantenibilidad o diseño.

Scanner
: Clase en Java que permite leer entrada del usuario desde la consola o archivos.

Sobrecarga (Overloading)
: Definir múltiples métodos con el mismo nombre pero con diferentes parámetros dentro de la misma clase.

Sobrescritura (Overriding)
: Implementar en una subclase un método que ya está definido en su superclase para cambiar o extender su comportamiento.

Stack
: Área de memoria donde se almacenan las variables locales y las llamadas a métodos en ejecución.

String
: Tipo de dato que representa una secuencia de caracteres.

Unit Test (Prueba Unitaria)
: Prueba automatizada que verifica el funcionamiento de la unidad más pequeña de código (generalmente un método) de forma aislada.

Variable
: Contenedor de datos que almacena un valor en memoria.
```