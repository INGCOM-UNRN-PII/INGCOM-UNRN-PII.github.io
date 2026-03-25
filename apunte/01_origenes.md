---
title: Orígenes e Historia de Java
description:
  Un recorrido por la creación y evolución del lenguaje Java desde sus inicios en Sun
  Microsystems hasta la actualidad.
---

# Orígenes e Historia de Java

Este capítulo presenta el contexto histórico y técnico que dio origen a Java. Comprender
estos orígenes ayuda a entender las decisiones de diseño del lenguaje y por qué funciona
como funciona.

## El Nacimiento de Java: El Green Project

### El contexto: Sun Microsystems y los años 90

**Sun Microsystems** fue una empresa estadounidense fundada en 1982, conocida por fabricar
estaciones de trabajo (computadoras de alto rendimiento para profesionales) y servidores.
El nombre "SUN" era un acrónimo de "Stanford University Network", ya que sus fundadores
eran estudiantes de Stanford.

A principios de la década de 1990, Sun anticipó que la próxima gran ola tecnológica no
vendría de las estaciones de trabajo tradicionales, sino de la **electrónica de consumo**:
televisores inteligentes, microondas programables, controles remotos avanzados y otros
dispositivos del hogar que comenzaban a incluir pequeños procesadores.

### El Green Team y el proyecto secreto

En 1991, James Gosling (un ingeniero canadiense reconocido por crear el editor de texto
Emacs para Unix) lideró un equipo de ingenieros de élite conocido como el **"Green
Team"**. Este equipo operó en secreto durante 18 meses, trabajando en un edificio apartado
de las oficinas principales de Sun —casi como una startup interna— lo que les permitió
tomar decisiones de diseño radicales sin las restricciones burocráticas típicas de una
gran corporación.

El objetivo principal era desarrollar un **sistema operativo** y un **lenguaje de
programación** para dispositivos inteligentes con recursos limitados (poca memoria,
procesadores lentos). El resultado de este esfuerzo fue el **Star7** (_\*7_), un prototipo
de dispositivo portátil con:

- **Pantalla táctil** de 5 pulgadas (revolucionario para 1992)
- **Interfaz gráfica animada** con un personaje llamado "Duke" (que se convertiría en la
  mascota de Java)
- **Conectividad inalámbrica** para comunicarse con otros dispositivos
- Un **sistema operativo propio** escrito en un nuevo lenguaje de programación

Este dispositivo era, esencialmente, un **PDA** (Personal Digital Assistant) —precursor de
los smartphones actuales— creado más de una década antes del iPhone.

### El Green Team: Los Arquitectos Originales

El equipo que dio vida a Java estaba compuesto por ingenieros excepcionales, cada uno con
contribuciones específicas:

:::{table} Miembros clave del Green Team 
:label: tbl-green-team

| Ingeniero            | Rol Principal           | Contribución Notable                                       |
| :------------------- | :---------------------- | :--------------------------------------------------------- |
| **James Gosling**    | Arquitecto del lenguaje | Diseño del compilador y la sintaxis de Oak/Java            |
| **Mike Sheridan**    | Gerente de proyecto     | Coordinación y visión de negocio                           |
| **Patrick Naughton** | Interfaz gráfica        | Creación de Duke y el sistema de ventanas                  |
| **Chris Warth**      | Compilador              | Optimización del bytecode                                  |
| **Ed Frank**         | Hardware                | Diseño del dispositivo Star7                               |
| **Jonathan Payne**   | Herramientas            | Desarrollo del primer navegador con soporte Java (HotJava) |

:::

:::{note}

El lenguaje se llamó inicialmente **"Oak"** (roble en inglés), en honor a un
árbol que Gosling observaba desde la ventana de su oficina. Sin embargo, al intentar
registrar el nombre, descubrieron que "Oak" ya era una marca registrada por otra empresa
de tecnología. El equipo se reunió en una cafetería local para hacer una lluvia de ideas,
y entre las opciones que surgieron (DNA, Silk, Ruby) eligieron **Java**, el nombre de una
isla de Indonesia famosa por producir un tipo de café que el equipo consumía en grandes
cantidades.

:::

### ¿Por qué necesitaban un nuevo lenguaje?

Para entender por qué el Green Team no usó un lenguaje existente, es necesario comprender
el panorama de la programación en 1991.

### El Desafío Tecnológico frente a C/C++

En aquella época, el desarrollo de software estaba dominado por **C** y **C++**. Para
entender qué significan los problemas que estos lenguajes presentaban, es necesario
explicar algunos conceptos:

#### ¿Qué es la gestión manual de memoria?

Cuando un programa necesita almacenar datos (por ejemplo, una lista de números o el texto
de un documento), debe pedirle al sistema operativo un espacio en la memoria RAM. En
C/C++, el programador es responsable de:

1. **Reservar memoria** usando funciones como `malloc()` (memory allocation)
2. **Usar esa memoria** para almacenar datos
3. **Liberar la memoria** con `free()` cuando ya no la necesita

Si el programador olvida liberar la memoria, el programa consume cada vez más RAM hasta
agotar los recursos del sistema (esto se llama **memory leak** o fuga de memoria). Si
libera la memoria pero luego intenta usarla, el programa falla de forma impredecible
(**dangling pointer** o puntero colgante).

#### ¿Qué son los punteros?

Un **puntero** es una variable que almacena una dirección de memoria en lugar de un valor
directo. Pensá en los punteros como "direcciones postales" que indican dónde encontrar los
datos reales. En C/C++, los punteros permiten:

- Acceder directamente a cualquier ubicación de memoria
- Realizar "aritmética de punteros" (sumar/restar a una dirección para moverse por la
  memoria)

Esta flexibilidad es poderosa pero peligrosa: un error de un byte en la dirección puede
hacer que el programa lea o escriba en el lugar equivocado, corrompiendo datos o causando
que el programa se cierre abruptamente.

#### ¿Qué es un buffer overflow?

Un **buffer** es un espacio de memoria reservado para almacenar datos temporalmente (como
un arreglo de caracteres para guardar texto). Un **buffer overflow** (desbordamiento de
búfer) ocurre cuando un programa escribe más datos de los que caben en el buffer,
sobrescribiendo la memoria adyacente.

Este error no solo causa fallos; es una de las vulnerabilidades de seguridad más
explotadas en la historia de la informática. Un atacante puede diseñar datos especiales
que, al desbordar el buffer, sobrescriban instrucciones del programa y ejecuten código
malicioso.

#### Los problemas concretos de C/C++ para dispositivos embebidos

Para el Green Team, estos problemas eran críticos:

- **Fragilidad de memoria:** La gestión manual (punteros, `malloc`, `free`) era fuente
  constante de _memory leaks_ y errores de segmentación que podían hacer fallar un
  televisor inteligente.
- **Dependencia binaria:** Un programa compilado para un procesador Intel x86 **no
  funciona** en un chip Motorola 68000 o ARM sin ser completamente recompilado (y a veces
  reescrito). Los dispositivos de consumo usaban docenas de procesadores diferentes.
- **Seguridad:** Los desbordamientos de búfer permitían la ejecución de código malicioso,
  algo inaceptable para dispositivos conectados en red.

Java nació con la misión de eliminar estas deficiencias a través de una arquitectura
basada en **abstracción**: el lenguaje escondería los detalles peligrosos del hardware
detrás de una capa segura.

### Los Cinco Principios de Diseño Originales

El equipo de Gosling estableció cinco objetivos fundamentales que guiaron el diseño del
lenguaje. Cada decisión del lenguaje puede rastrearse a uno de estos principios:

1. **Simple, orientado a objetos y familiar**

   Debía ser fácil de aprender para programadores de C/C++, manteniendo una sintaxis
   similar (llaves, punto y coma, tipos de datos), pero eliminando las complejidades
   innecesarias:
   - **Sin herencia múltiple de clases:** En C++, una clase puede heredar de múltiples
     clases padres, lo que genera conflictos cuando dos padres definen el mismo método (el
     "problema del diamante"). Java permite implementar múltiples _interfaces_, pero solo
     heredar de una clase.
   - **Sin aritmética de punteros:** No se puede sumar números a direcciones de memoria
     para "navegar" arbitrariamente.
   - **Sin preprocesador:** C usa directivas como `#define` y `#include` que el
     preprocesador ejecuta antes de compilar, generando código difícil de depurar. Java
     eliminó este paso.

2. **Robusto y seguro**

   El lenguaje debía prevenir errores comunes:
   - **Verificación de tipos estricta:** El compilador rechaza operaciones entre tipos
     incompatibles.
   - **Verificación de límites de arreglos:** Acceder al elemento 10 de un arreglo de 5
     elementos lanza una excepción en lugar de corromper memoria.
   - **Sin conversiones implícitas peligrosas:** Convertir un número de punto flotante a
     entero requiere un cast explícito.

3. **Arquitectura neutral y portable**

   El mismo programa debía ejecutarse sin modificaciones en cualquier hardware. Esto se
   logró mediante el bytecode y la JVM (explicados más adelante).

4. **Alto rendimiento**

   A pesar de la capa de abstracción, el rendimiento debía ser aceptable para aplicaciones
   reales. El compilador JIT (Just-In-Time) fue la solución: traducir el código más usado
   a instrucciones nativas del procesador mientras el programa se ejecuta.

5. **Interpretado, multihilo y dinámico**
   - **Interpretado:** El bytecode se ejecuta sin necesidad de compilación separada para
     cada plataforma.
   - **Multihilo (multithreaded):** Soporte nativo para ejecutar múltiples tareas
     simultáneamente (hilos o _threads_), algo que en C requería bibliotecas específicas
     del sistema operativo.
   - **Dinámico:** Las clases se cargan cuando se necesitan, no todas al inicio del
     programa.

:::{tip}

Estos principios explican muchas decisiones del lenguaje que a veces parecen
restrictivas. Por ejemplo, la ausencia de herencia múltiple de clases simplifica
drásticamente la resolución de conflictos y hace el código más predecible. Cuando te
preguntes "¿por qué Java no permite esto?", la respuesta suele estar en estos cinco
principios. 

:::

## La Máquina Virtual de Java (JVM) y el Bytecode

La innovación fundamental de Java no fue solo el lenguaje, sino la creación de una
**Máquina Virtual (JVM)** — una computadora simulada por software que ejecuta los
programas Java.

### ¿Qué es una máquina virtual?

Una **máquina virtual** es un programa que simula ser una computadora completa. Así como
un emulador de videojuegos simula una consola antigua en tu PC moderna, la JVM simula una
computadora especial diseñada específicamente para ejecutar programas Java.

Esta "computadora virtual" tiene:

- Su propio conjunto de instrucciones (el **bytecode**)
- Su propia organización de memoria (heap, stack, method area)
- Sus propias reglas de seguridad

La diferencia crucial con C++ es la siguiente:

- **C++ (compilación tradicional):** El compilador traduce el código fuente directamente a
  instrucciones que el procesador físico (Intel, ARM, etc.) entiende. El resultado es un
  archivo ejecutable que **solo funciona** en ese tipo de procesador y sistema operativo.

- **Java (compilación a bytecode):** El compilador traduce el código fuente a
  **bytecode**, un formato intermedio que ningún procesador físico entiende directamente.
  Luego, la JVM instalada en cada sistema lee ese bytecode y lo ejecuta.

### ¿Qué es el bytecode?

El **bytecode** es un conjunto de instrucciones diseñadas para una máquina virtual, no
para un procesador real. Cada instrucción ocupa uno o más bytes (de ahí el nombre
"byte-code").

Por ejemplo, el bytecode de Java incluye instrucciones como:

- `iload` - cargar un entero desde una variable local
- `iadd` - sumar dos enteros
- `invokevirtual` - llamar a un método de un objeto

Estas instrucciones son más simples y abstractas que las de un procesador real, lo que las
hace portables pero también más lentas de ejecutar directamente.

### ¿Cómo funciona el flujo WORA?

**WORA** significa "Write Once, Run Anywhere" (Escribilo una vez, ejecutalo en cualquier
lugar). Este es el proceso completo:

1. **Escritura:** El programador escribe código fuente en archivos `.java` usando
   cualquier editor de texto.

2. **Compilación a Bytecode:** El compilador `javac` (Java Compiler) transforma el código
   fuente en **bytecode**, guardado en archivos `.class`. Este bytecode es idéntico sin
   importar en qué sistema operativo se compiló.

3. **Distribución:** Los archivos `.class` (o empaquetados en archivos `.jar`) se
   distribuyen a los usuarios.

4. **Ejecución:** En la computadora del usuario, la JVM lee el bytecode. La JVM existe en
   versiones específicas para cada combinación de sistema operativo y procesador
   (Windows/Intel, macOS/ARM, Linux/x86, etc.), pero todas entienden el mismo bytecode.

5. **Compilación JIT:** Para maximizar el rendimiento, la JVM incluye un compilador
   **Just-In-Time (JIT)** que identifica las partes del código que se ejecutan
   frecuentemente ("puntos calientes" o _hotspots_) y las traduce a código máquina nativo
   del procesador. Esto ocurre mientras el programa se ejecuta, de forma transparente.

```{figure} 01/jvm_arquitectura.svg
:label: fig-jvm-arquitectura
:align: center
:width: 85%

Arquitectura de la JVM: El código fuente se compila a bytecode, un formato universal que la JVM de cada plataforma puede ejecutar.
```

:::{important} La JVM es la pieza que permite la filosofía WORA. 

Oracle, la empresa dueña de Java, no es la única que produce JVMs. Existen implementaciones de:

- **Oracle HotSpot:** La implementación de referencia.
- **OpenJDK:** Versión de código abierto (la que usaremos en el curso).
- **Eclipse OpenJ9:** Optimizada para bajo consumo de memoria.
- **GraalVM:** Con capacidades avanzadas de compilación.

Todas deben pasar las mismas pruebas de compatibilidad (TCK - Technology Compatibility
Kit) para garantizar que ejecutan el bytecode de forma idéntica. 

:::

### Estructura Interna de la JVM

La JVM no es un componente monolítico; está organizada en subsistemas especializados que
trabajan juntos:

#### Class Loader (Cargador de Clases)

Cuando un programa Java necesita usar una clase (por ejemplo, `String` o una clase que vos
escribiste), el Class Loader la busca y la carga en memoria. Las clases se cargan **bajo
demanda**: si tu programa nunca usa la clase `ArrayList`, nunca se carga.

El Class Loader tiene una jerarquía de tres niveles:

1. **Bootstrap ClassLoader:** Carga las clases fundamentales de Java (`java.lang.String`,
   `java.lang.Object`, etc.) desde el JDK.
2. **Extension ClassLoader:** Carga extensiones del JDK.
3. **Application ClassLoader:** Carga las clases de tu aplicación.

#### Verificador de Bytecode

Antes de ejecutar cualquier clase cargada, la JVM analiza su bytecode para verificar que:

- El código es estructuralmente correcto (las instrucciones están bien formadas)
- Los tipos son consistentes (no se intenta usar un String como si fuera un número)
- No se violan las reglas de acceso (código externo no puede acceder a miembros privados)
- El stack no se desborda ni se accede fuera de límites

Esta verificación es crucial para la seguridad: previene que código malicioso manipule la
JVM.

#### Intérprete

El intérprete lee el bytecode instrucción por instrucción y ejecuta la acción
correspondiente. Es la forma más simple de ejecución, pero también la más lenta.

#### Compilador JIT (Just-In-Time)

El compilador JIT observa qué partes del código se ejecutan repetidamente y las compila a
código máquina nativo del procesador. Esto significa que:

- La primera vez que se ejecuta un método, es interpretado (lento)
- Después de muchas ejecuciones, se compila a código nativo (rápido)

El JIT realiza optimizaciones sofisticadas:

- **Inlining:** Reemplaza llamadas a métodos cortos con el código del método directamente
- **Eliminación de código muerto:** Remueve código que nunca se ejecuta
- **Desenrollado de lazos:** Optimiza bucles expandiéndolos

#### Garbage Collector (Recolector de Basura)

El Garbage Collector (GC) gestiona automáticamente la memoria. Cuando creás un objeto con
`new`, la JVM reserva espacio en el **heap** (montículo). Cuando ese objeto ya no es
accesible (ninguna variable lo referencia), el GC lo identifica y libera su memoria.

El GC funciona en segundo plano, pausando brevemente el programa para realizar la
limpieza. Las JVMs modernas tienen algoritmos sofisticados que minimizan estas pausas:

- **G1 (Garbage First):** El recolector por defecto desde Java 9
- **ZGC:** Pausas de menos de 10 milisegundos incluso con heaps de terabytes
- **Shenandoah:** Similar a ZGC, desarrollado por Red Hat

#### Áreas de Memoria

La JVM organiza la memoria en regiones específicas:

- **Heap:** Donde viven todos los objetos. Es compartido por todos los hilos del programa.
  Aquí trabaja el Garbage Collector.
- **Stack (uno por hilo):** Cada hilo tiene su propia pila donde se almacenan las
  variables locales y las llamadas a métodos pendientes. Cuando un método llama a otro, se
  apila un nuevo "frame"; cuando el método retorna, se desapila.
- **Method Area:** Almacena la información de las clases cargadas (métodos, constantes,
  metadatos).
- **PC Register:** Cada hilo tiene un registro que indica qué instrucción de bytecode está
  ejecutando actualmente.

### El Modelo de Seguridad: El Sandbox

Uno de los aspectos más innovadores de Java fue su **modelo de seguridad en capas**,
diseñado originalmente para proteger a los usuarios de código potencialmente malicioso
descargado de internet.

#### ¿Qué es un Sandbox?

Un **sandbox** (caja de arena) es un entorno de ejecución restringido donde el código
puede ejecutarse sin poder dañar el sistema que lo hospeda. Imaginá un niño jugando en una
caja de arena: puede hacer lo que quiera dentro de la caja, pero no puede salir de ella
para causar problemas en el resto del parque.

En Java, el sandbox original permitía que los **Applets** (pequeños programas Java
embebidos en páginas web) se ejecutaran en el navegador del usuario sin poder:

- Leer archivos del disco duro del usuario
- Escribir archivos en el disco
- Conectarse a servidores diferentes al que sirvió el applet
- Ejecutar otros programas del sistema

```{figure} 01/sandbox_modelo.svg
:label: fig-sandbox-modelo
:align: center
:width: 80%

El modelo de seguridad de Java: múltiples capas de verificación protegen el sistema del código no confiable.
```

#### Las capas del modelo de seguridad

1. **Verificación de Bytecode:**

   Antes de ejecutar cualquier clase, la JVM verifica que el bytecode sea estructuralmente
   correcto. Esto previene que un atacante modifique manualmente un archivo `.class` para
   incluir instrucciones ilegales que exploten vulnerabilidades.

2. **Security Manager (Gestor de Seguridad):**

   Un componente configurable que intercepta operaciones sensibles y decide si
   permitirlas. Por ejemplo, antes de que el código intente abrir un archivo, el Security
   Manager verifica si tiene permiso. Las aplicaciones pueden definir **políticas de
   seguridad** que especifican qué permisos tiene cada porción de código.

3. **Class Loader jerárquico:**

   Separa el código confiable del no confiable mediante diferentes cargadores de clases.
   Las clases del sistema (cargadas por el Bootstrap ClassLoader) tienen todos los
   privilegios; las clases descargadas de internet tienen privilegios restringidos.

:::{warning} 

El modelo de Applets fue eventualmente abandonado debido a la evolución de las 
tecnologías web (JavaScript, HTML5 Canvas, WebAssembly). Los navegadores modernos 
ya no soportan applets Java.

Sin embargo, los conceptos de seguridad de la JVM siguen siendo fundamentales:

- Servidores de aplicaciones que ejecutan código de múltiples clientes
- Aplicaciones que cargan plugins de terceros
- Sistemas que necesitan aislar componentes no confiables 

:::

## El Salto a la Web (1995)

### El fracaso comercial inicial

Hacia 1994, el mercado de dispositivos inteligentes aún no estaba maduro. El Green Project
intentó vender su tecnología a empresas de electrónica de consumo, pero ninguna mostró
interés. El proyecto enfrentaba la cancelación.

### La oportunidad de la World Wide Web

Sin embargo, el surgimiento explosivo de la **World Wide Web** ofreció una oportunidad
inesperada. La web de 1994-1995 era muy diferente a la actual:

- Las páginas eran **estáticas**: solo texto e imágenes, sin interactividad
- **JavaScript no existía** (se creó el mismo año que Java, 1995)
- No había forma de ejecutar programas dentro del navegador
- Cada página requería una recarga completa del servidor para cualquier cambio

Sun Microsystems se dio cuenta de que la red necesitaba precisamente lo que Java ofrecía:
un lenguaje **seguro** (para que los usuarios confiaran en ejecutar código descargado) e
**independiente de la plataforma** (ya que los usuarios tenían Windows, Mac y Unix).

### El lanzamiento y los Applets

El lanzamiento oficial de Java en **mayo de 1995** incluyó los **Applets**, pequeños
programas Java que se descargaban automáticamente cuando un usuario visitaba una página
web y se ejecutaban dentro del navegador.

Por primera vez, las páginas web podían tener:

- Animaciones complejas
- Juegos interactivos
- Visualizaciones de datos en tiempo real
- Interfaces de usuario sofisticadas

La demostración más famosa fue una animación de moléculas en 3D que los usuarios podían
rotar con el mouse —algo trivial hoy, pero revolucionario en 1995.

### ¿Qué son exactamente los Applets?

Un **Applet** era una clase Java que:

1. Se almacenaba en un servidor web
2. Se descargaba al navegador cuando el usuario visitaba la página
3. Se ejecutaba dentro de un área rectangular de la página web
4. Tenía acceso limitado al sistema del usuario (sandbox)

El HTML para incluir un applet era:

```html
<applet code="MiApplet.class" width="300" height="200">
  Tu navegador no soporta applets.
</applet>
```

:::{note} 

Los applets fueron deprecados en Java 9 (2017) y eliminados en Java 11 (2018).
Las tecnologías web modernas (JavaScript, HTML5 Canvas, WebGL, WebAssembly) ofrecen las
mismas capacidades sin necesitar plugins adicionales.

:::

### Competidores y el Contexto Tecnológico de los 90

Java no surgió en el vacío. Durante la década de 1990, varias tecnologías competían por
dominar el desarrollo de software:

:::{table} Tecnologías competidoras de Java en 1995 
:label: tbl-competidores-90s

| Tecnología       | Fortalezas                                                                        | Debilidades frente a Java                                                                  |
| :--------------- | :-------------------------------------------------------------------------------- | :----------------------------------------------------------------------------------------- |
| **C++**          | Máximo rendimiento, control total del hardware                                    | Complejidad extrema, errores de memoria frecuentes, cada plataforma requiere recompilación |
| **Visual Basic** | Muy fácil de aprender, desarrollo rápido de interfaces gráficas                   | Solo funcionaba en Windows, rendimiento limitado para aplicaciones grandes                 |
| **Smalltalk**    | Orientación a objetos pura (todo es un objeto), entorno de desarrollo interactivo | Rendimiento inferior, ecosistema pequeño, sintaxis muy diferente a C                       |
| **Delphi**       | Alta productividad, compilación a código nativo veloz                             | Solo Windows, licencia propietaria costosa                                                 |
| **ActiveX/COM**  | Integración total con Windows, podía usar cualquier lenguaje                      | Solo Windows, graves problemas de seguridad (acceso completo al sistema)                   |

:::

#### ¿Qué es ActiveX y por qué era peligroso?

**ActiveX** fue la respuesta de Microsoft a los applets de Java. Los controles ActiveX
eran componentes de software que podían embeberse en páginas web, pero con una diferencia
crucial: **tenían acceso completo al sistema del usuario**.

Mientras un applet Java solo podía dibujar en su rectángulo asignado, un control ActiveX
podía leer cualquier archivo del disco, instalar software, modificar el registro de
Windows, o hacer cualquier cosa que el usuario pudiera hacer.

Microsoft intentó mitigar esto con un sistema de "firma digital": los desarrolladores
podían firmar sus controles, y el navegador preguntaba al usuario si confiaba en ese
desarrollador. Pero esto dependía de que:

1. Los usuarios leyeran y entendieran las advertencias (la mayoría hacía clic en "Sí" sin
   leer)
2. Los desarrolladores legítimos no cometieran errores de seguridad
3. Nadie robara certificados de firma

Los tres supuestos resultaron falsos, y ActiveX se convirtió en uno de los vectores de
ataque más explotados de la historia de la computación.

La ventaja competitiva de Java fue combinar la potencia de un lenguaje serio con la
**seguridad del sandbox** que ActiveX no ofrecía.

### La Guerra de los Navegadores y Microsoft

El éxito de Java en la web provocó una respuesta agresiva de Microsoft. En 1996, Microsoft
licenció Java de Sun y creó su propia implementación: **Microsoft J++** (Java plus plus).

#### El problema: extensiones propietarias

Microsoft modificó su JVM para incluir **extensiones propietarias** que solo funcionaban
en Windows:

- Acceso directo a la API de Windows (Win32)
- Integración con COM/ActiveX
- Tipos de datos adicionales

Un programa Java escrito usando estas extensiones funcionaba perfectamente en Windows,
pero fallaba en cualquier otro sistema operativo. Esto violaba directamente la filosofía
WORA y fragmentaba el ecosistema.

Sun demandó a Microsoft en 1997, argumentando que Microsoft había incumplido los términos
de la licencia al crear una versión incompatible de Java. El resultado del conflicto legal
fue:

1. **Microsoft perdió** y debió pagar más de 20 millones de dólares
2. **Microsoft abandonó J++** y dejó de incluir la JVM en Windows
3. **Microsoft creó C# y .NET** como respuesta directa a Java —un ecosistema propio sobre
   el que tendrían control total

:::{note} La rivalidad Java vs .NET

Moldeó la industria del software empresarial durante dos décadas. Curiosamente, ambas
plataformas evolucionaron adoptando características una de la otra:

- Java incorporó genéricos (Java 5, 2004) y lambdas (Java 8, 2014), características que C#
  tuvo primero
- C# adoptó la recolección de basura automática y el concepto de máquina virtual desde el
  principio, inspirado directamente en Java

Hoy en día, la competencia se ha moderado: muchos desarrolladores conocen ambas
plataformas, y la elección depende más del ecosistema existente de la empresa que de
diferencias técnicas fundamentales. 

:::

## Evolución y la Era de Oracle

### De Sun Microsystems a Oracle (2010)

Sun Microsystems, a pesar de haber creado Java y otras tecnologías influyentes (como el
sistema de archivos NFS y el sistema operativo Solaris), enfrentó dificultades financieras
durante la década de 2000. En 2010, **Oracle Corporation** —una empresa especializada en
bases de datos empresariales— adquirió Sun por 7.400 millones de dólares.

Esta transición generó incertidumbre en la comunidad Java:

- ¿Oracle mantendría Java como proyecto de código abierto?
- ¿Se aceleraría o desaceleraría el desarrollo?
- ¿Habría demandas legales contra usuarios de Java? (Oracle es conocida por litigar
  agresivamente)

#### La demanda Oracle vs Google

Los temores se materializaron parcialmente cuando Oracle demandó a Google en 2010,
alegando que Android violaba patentes y derechos de autor de Java. El caso llegó hasta la
Corte Suprema de Estados Unidos, que en 2021 falló a favor de Google, estableciendo que el
uso de APIs de Java en Android constituía "uso justo" (_fair use_).

#### El lado positivo: desarrollo acelerado

Bajo el mando de Oracle, Java adoptó un ritmo de innovación más acelerado que en los
últimos años de Sun:

- **Java 7 (2011):** Después de 5 años sin versiones mayores, introdujo
  try-with-resources, el operador diamante, y mejoras en concurrencia.
- **Java 8 (2014):** Marcó un cambio de paradigma al introducir la **programación
  funcional** (Lambdas y Streams API), la característica más significativa desde los
  genéricos.
- **Java 9 (2017):** Sistema de módulos (Project Jigsaw), permitiendo crear aplicaciones
  más pequeñas y seguras.

### Jakarta EE: El ecosistema empresarial

**Java EE** (Enterprise Edition) era el conjunto de especificaciones para aplicaciones
empresariales: servidores web, bases de datos, mensajería, etc. En 2017, Oracle transfirió
Java EE a la **Eclipse Foundation**, una organización sin fines de lucro.

Debido a restricciones de marca (Oracle mantiene los derechos sobre el nombre "Java"), el
proyecto fue renombrado **Jakarta EE**. Esto consolidó un ecosistema de código abierto
donde la comunidad —no una sola empresa— controla el futuro de las especificaciones
empresariales.

### El Ciclo Moderno de Lanzamientos

#### El problema del ciclo antiguo

Históricamente, las versiones de Java se espaciaban por años:

- Java 6 a Java 7: **5 años** (2006-2011)
- Java 7 a Java 8: **3 años** (2011-2014)
- Java 8 a Java 9: **3 años** (2014-2017)

Este ritmo lento significaba que las nuevas características tardaban años en llegar a los
desarrolladores, mientras que lenguajes más nuevos (como Kotlin, Swift o Go) evolucionaban
rápidamente.

#### El nuevo modelo: lanzamientos cada 6 meses

A partir de Java 10 (2018), Oracle adoptó un ciclo de lanzamientos **cada seis meses**:

- Marzo y septiembre de cada año hay una nueva versión
- Cada versión incluye las características que estén listas en ese momento
- Las características grandes se dividen en partes más pequeñas ("preview features")

#### ¿Qué es LTS (Long Term Support)?

No todas las versiones son iguales:

- **Versiones de corta duración (non-LTS):** Reciben actualizaciones de seguridad solo por
  6 meses, hasta que sale la siguiente versión. Ejemplos: Java 10, 12, 13, 14, 15, 16, 18,
  19, 20.

- **Versiones de soporte extendido (LTS):** Reciben actualizaciones de seguridad por
  **años**. Son las versiones recomendadas para producción. Se lanzan cada 2 años
  aproximadamente.

Las versiones LTS actuales son:

- **Java 8** (2014): Todavía muy usada en sistemas legacy
- **Java 11** (2018): Primera LTS del nuevo ciclo
- **Java 17** (2021): LTS actual recomendada
- **Java 21** (2023): LTS más reciente

:::{important} Para proyectos nuevos en 2024+, se recomienda usar **Java 21** (LTS). Las
versiones intermedias (18, 19, 20, 22, 23) son versiones de "feature preview" con soporte
limitado de 6 meses, útiles para probar nuevas características pero no para producción.
:::

### Línea Temporal de las Versiones Principales

La siguiente tabla resume las versiones más importantes de Java y sus características
destacadas:

:::{table} Evolución de Java por versiones
:label: tbl-versiones-java

| Versión    | Año  | Características Destacadas                                                                |
| :--------- | :--: | :---------------------------------------------------------------------------------------- |
| JDK 1.0    | 1996 | Lanzamiento inicial. AWT (interfaz gráfica básica), Applets                               |
| JDK 1.1    | 1997 | Clases internas (_inner classes_), JDBC (conexión a bases de datos), JavaBeans            |
| [J2SE 1.2](https://www.oracle.com/java/technologies/javase/release-notes-sdk.html)   | 1998 | Collections Framework (listas, mapas, conjuntos), Swing (GUI mejorada), compilador JIT    |
| [J2SE 1.4](https://www.oracle.com/java/technologies/javase/releasenotes-v142.html)   | 2002 | Assertions, NIO (entrada/salida no bloqueante), logging integrado, expresiones regulares  |
| [J2SE 5.0](https://www.oracle.com/java/technologies/javase/releasenotes-v150.html)  | 2004 | **Genéricos**, `enums`, autoboxing, for-each (`for (x : lista)`), `varargs`, anotaciones      |
| [JDK 6](https://www.oracle.com/java/technologies/javase/6-relnotes.html) | 2006 | Mejoras de rendimiento, Scripting API (ejecutar JavaScript desde Java)                    |
| [JDK 7](https://www.oracle.com/java/technologies/javase/7u-relnotes.html) | 2011 | Try-with-resources, operador diamante (`<>`), strings en switch, NIO.2                    |
| [JDK 8](https://www.oracle.com/java/technologies/javase/8u-relnotes.html) | 2014 | **Lambdas**, Streams API, Optional, nueva API de Date/Time, métodos default en interfaces |
| [JDK 9](https://www.oracle.com/java/technologies/javase/9-all-relnotes.html) | 2017 | Sistema de módulos (Jigsaw), JShell (REPL interactivo), métodos privados en interfaces    |
| [JDK 11](https://www.oracle.com/java/technologies/javase/11all-relnotes.html) | 2018 | Primer release **LTS**, `var` para variables locales, HTTP Client moderno, eliminación de Java EE          |
| [JDK 17](https://www.oracle.com/java/technologies/javase/17all-relnotes.html) | 2021 | Records, Sealed Classes, Pattern Matching para `instanceof`                        |
| [JDK 21](https://www.oracle.com/java/technologies/javase/21all-relnotes.html) | 2023 | Virtual Threads, Sequenced Collections, Pattern Matching en `switch`               |
| [JDK 25](https://www.oracle.com/java/technologies/javase/25all-relnotes.html) | 2025 | Importación de módulos, constructores flexibles, tipos primitivos en pattern matching, archivos fuente compactos, Compact Object Headers|

:::

Vean [JDK Releases](https://www.java.com/releases/) para más información y detalles de los
otros lanzamientos y evolución detallada de la plataforma.

#### ¿Qué significan estos términos?

Algunos de los conceptos mencionados en la tabla se estudiarán durante el curso; otros son
más avanzados:

- **Genéricos:** Permiten crear clases y métodos que funcionan con cualquier tipo de dato,
  verificando tipos en compilación (por ejemplo, `List<String>` solo acepta strings).
- **Lambdas:** Funciones anónimas que pueden pasarse como parámetros, habilitando un
  estilo de programación funcional.
- **Streams:** Una forma de procesar colecciones de datos de manera declarativa (filtrar,
  transformar, agrupar).
- **Records:** Clases inmutables para datos puros, sin necesidad de escribir getters,
  equals, hashCode manualmente.
- **Virtual Threads:** Hilos livianos gestionados por la JVM que permiten crear millones
  de hilos concurrentes.

## Impacto y Legado

Java no es solo un lenguaje de programación; es una de las **plataformas de software más
influyentes de la historia**. Su impacto se mide no solo en líneas de código escritas,
sino en la infraestructura crítica del mundo moderno que depende de él.

### ¿Dónde se usa Java hoy?

- **Ecosistema empresarial:** Frameworks como **Spring** dominan el desarrollo de
  aplicaciones empresariales globalmente. La mayoría de los sistemas bancarios, de seguros
  y de telecomunicaciones están escritos en Java.

- **Android:** El sistema operativo móvil más usado del mundo (aproximadamente 70% del
  mercado global) se construyó originalmente sobre Java. Aunque Kotlin es ahora el
  lenguaje preferido para nuevas aplicaciones, millones de aplicaciones Android existentes
  están en Java.

- **Big Data:** Las herramientas fundamentales del ecosistema de datos masivos están
  escritas en Java o lenguajes de la JVM:
  - **Apache Hadoop:** Procesamiento distribuido de datos
  - **Apache Kafka:** Mensajería en tiempo real
  - **Apache Spark:** Análisis de datos a gran escala (escrito en Scala, ejecuta en JVM)
  - **Elasticsearch:** Motor de búsqueda

- **Infraestructura crítica:** Desde sistemas bancarios que procesan millones de
  transacciones por segundo hasta el software de control de misiones de la NASA, Java es
  el estándar para software que **no puede fallar**.

### El Ecosistema de Herramientas

Alrededor de Java se desarrolló un ecosistema maduro de herramientas que amplificaron su
productividad:

#### Entornos de Desarrollo (IDEs)

Un **IDE** (Integrated Development Environment) es un programa que integra editor de
código, compilador, depurador y otras herramientas en una sola aplicación.

- **IntelliJ IDEA:** Considerado el IDE más avanzado para Java. Desarrollado por JetBrains
  (la misma empresa que creó Kotlin). Ofrece refactorización inteligente, análisis de
  código profundo y excelente integración con frameworks. Tiene versión gratuita
  (Community) y de pago (Ultimate).

- **Eclipse:** IDE de código abierto, extensible mediante plugins. Históricamente
  dominante en el ámbito empresarial. Gratis completamente.

- **NetBeans:** IDE oficial de Apache (originalmente de Sun/Oracle). Buena integración con
  herramientas estándar de Java. Gratis y de código abierto.

- **Visual Studio Code:** No es un IDE completo, sino un editor de código ligero que puede
  extenderse con plugins para soportar Java. Popular entre desarrolladores que prefieren
  herramientas más livianas.

#### Sistemas de Build (Construcción)

Un **sistema de build** automatiza la compilación, pruebas y empaquetado del software. En
proyectos grandes con decenas de bibliotecas externas, gestionar esto manualmente sería
imposible.

- **Maven:** El sistema más usado. Define el proyecto en un archivo XML (`pom.xml`) que
  especifica dependencias, plugins y configuración. Sigue el principio de "convención
  sobre configuración": si seguís la estructura de directorios estándar, casi no hay que
  configurar nada.

- **Gradle:** Sistema más moderno. Usa scripts en Groovy o Kotlin en lugar de XML,
  ofreciendo mayor flexibilidad. Tiene mejor rendimiento para builds incrementales (solo
  recompila lo que cambió). Es el sistema oficial para proyectos Android.

#### Frameworks de Aplicación

Un **framework** es un conjunto de bibliotecas y convenciones que proporcionan una
estructura base para desarrollar aplicaciones.

- **Spring Framework:** El estándar de facto para aplicaciones empresariales en Java.
  Proporciona:
  - **Inyección de dependencias:** El framework crea y conecta los objetos automáticamente
  - **Spring Boot:** Configuración automática para crear aplicaciones rápidamente
  - **Spring Security:** Autenticación y autorización
  - **Spring Data:** Acceso simplificado a bases de datos

- **Jakarta EE (ex Java EE):** Conjunto de especificaciones estándar para aplicaciones
  empresariales. Define APIs para servlets (aplicaciones web), JPA (bases de datos), CDI
  (inyección de dependencias), etc. Múltiples proveedores implementan estas
  especificaciones.

- **Quarkus y Micronaut:** Frameworks modernos optimizados para contenedores (Docker,
  Kubernetes) y entornos cloud. Tienen tiempos de inicio muy rápidos y bajo consumo de
  memoria.

### Lenguajes sobre la JVM

La JVM demostró ser una plataforma tan robusta y bien optimizada que otros lenguajes
fueron diseñados para ejecutarse sobre ella, aprovechando:

- El compilador JIT maduro y altamente optimizado
- El Garbage Collector sofisticado
- Las miles de bibliotecas Java existentes
- La portabilidad a cualquier sistema con JVM

#### Kotlin

Desarrollado por **JetBrains** (los creadores de IntelliJ IDEA), Kotlin es un lenguaje
moderno diseñado para ser 100% interoperable con Java: podés llamar código Java desde
Kotlin y viceversa.

Características principales:

- **Null-safety integrado:** El sistema de tipos distingue entre referencias que pueden
  ser null y las que no, previniendo NullPointerException en tiempo de compilación
- **Sintaxis más concisa:** Menos código repetitivo (_boilerplate_) que Java
- **Coroutines:** Soporte nativo para programación asíncrona sin callbacks complejos

Kotlin es el **lenguaje oficial para desarrollo Android** desde 2019.

#### Scala

**Scala** (Scalable Language) combina programación orientada a objetos con programación
funcional avanzada. Es conocido por:

- **Sistema de tipos sofisticado:** Inferencia de tipos potente, tipos de alto orden
- **Inmutabilidad por defecto:** Favorece estructuras de datos que no cambian
- **Pattern matching avanzado:** Descomposición de estructuras de datos elegante

Scala es el lenguaje detrás de **Apache Spark**, el framework dominante para procesamiento
de datos a gran escala.

#### Groovy

**Groovy** es un lenguaje dinámico (los tipos se verifican en ejecución, no en
compilación) diseñado para ser familiar a programadores Java pero más conciso.

Usos principales:

- **Scripts de Gradle:** El sistema de build más moderno usa Groovy (o Kotlin) para su
  configuración
- **Testing con Spock:** Framework de pruebas con sintaxis expresiva
- **Scripting rápido:** Para tareas de automatización donde la flexibilidad importa más
  que la seguridad de tipos

#### Clojure

**Clojure** es un dialecto de **Lisp** (un lenguaje de los años 50 conocido por su
sintaxis de paréntesis) modernizado para la JVM. Se caracteriza por:

- **Programación funcional pura:** Las funciones no tienen efectos secundarios
- **Inmutabilidad por defecto:** Las estructuras de datos nunca se modifican; se crean
  nuevas versiones
- **Concurrencia simplificada:** El modelo de memoria inmutable evita muchos problemas de
  programación concurrente

:::{note}

La capacidad de ejecutar múltiples lenguajes sobre la misma máquina virtual
permite a los equipos elegir el lenguaje más apropiado para cada tarea mientras comparten
bibliotecas y herramientas. Un proyecto puede usar Java para la lógica de negocio, Kotlin
para la aplicación Android, Scala para procesamiento de datos, y Groovy para scripts de
build, todo ejecutándose en la misma JVM. 

:::

## Java en la Actualidad y el Futuro

### Estadísticas de Adopción

Según el índice **TIOBE** (que mide la popularidad de lenguajes según búsquedas en
internet) y encuestas de **Stack Overflow** (la comunidad de programadores más grande del
mundo), Java se mantiene consistentemente entre los **3 lenguajes más utilizados**, junto
con Python y C.

Su presencia es especialmente fuerte en sectores donde la estabilidad y el rendimiento son
críticos:

- **Banca y finanzas:** Sistemas de trading de alta frecuencia, procesamiento de
  transacciones, gestión de riesgos. Los bancos valoran la estabilidad de Java y la
  facilidad para encontrar desarrolladores.

- **Telecomunicaciones:** Infraestructura de red, sistemas de facturación, plataformas de
  mensajería. Empresas como Ericsson y Nokia usan Java extensivamente.

- **Gobierno y salud:** Sistemas críticos que requieren estabilidad a largo plazo y
  soporte garantizado por décadas.

- **E-commerce:** Plataformas como Amazon, eBay y Alibaba usan Java para sus backends
  debido a su escalabilidad.

- **Big Data:** Apache Hadoop, Kafka, Spark, Flink, Cassandra —la mayoría de las
  herramientas de datos masivos están escritas en Java o Scala (que corre en la JVM).

### Proyectos de Innovación Activos

Oracle y la comunidad OpenJDK continúan evolucionando la plataforma mediante proyectos
específicos con nombres de código. Estos proyectos desarrollan características que
eventualmente se incorporan al lenguaje:

#### Project Loom (Completado en Java 21)

**Virtual Threads** (hilos virtuales) permiten crear millones de hilos concurrentes con un
costo mínimo de memoria. Antes de Loom, cada hilo de Java mapeaba a un hilo del sistema
operativo, que consume aproximadamente 1MB de memoria. Con Virtual Threads, la JVM
gestiona los hilos internamente, usando solo unos pocos kilobytes por hilo.

Esto es revolucionario para servidores que manejan muchas conexiones simultáneas (como
servidores web o de bases de datos).

#### Project Amber (En progreso)

Mejoras en la **sintaxis del lenguaje** para hacer el código más conciso y expresivo:

- **Records** (Java 14+): Clases para datos inmutables sin boilerplate
- **Pattern Matching** (Java 16+): Verificación de tipos y extracción de datos combinadas
- **String Templates** (Preview en Java 21): Interpolación de strings más segura y
  flexible

#### Project Panama (En progreso)

Mejorar la **interoperabilidad con código nativo** (C, C++, bibliotecas del sistema).
Históricamente, llamar a código nativo desde Java requería **JNI** (Java Native
Interface), una API compleja y propensa a errores.

Panama introduce:

- **Foreign Function & Memory API:** Llamar funciones C directamente desde Java
- **Vector API:** Usar instrucciones SIMD del procesador para cálculos paralelos

#### Project Valhalla (En progreso)

**Value Types** y genéricos especializados para eliminar el overhead de "boxing".
Actualmente, cuando guardás un `int` en una colección genérica como `List<Integer>`, Java
debe "envolver" (_box_) el primitivo en un objeto, consumiendo memoria adicional.

Valhalla permitirá crear tipos de valor que se comporten como primitivos pero puedan
usarse con genéricos.

### GraalVM: El Futuro de la Ejecución

**GraalVM** es una máquina virtual de nueva generación desarrollada por **Oracle Labs**
(el laboratorio de investigación de Oracle) que representa el futuro de la ejecución de
código en la JVM.

#### ¿Qué problemas resuelve?

La JVM tradicional tiene algunas limitaciones:

1. **Tiempo de inicio lento:** La JVM necesita cargar clases, verificar bytecode y
   "calentar" el compilador JIT antes de alcanzar máximo rendimiento. Esto puede tomar
   segundos o incluso minutos.

2. **Alto consumo de memoria inicial:** Incluso una aplicación simple consume decenas de
   megabytes porque la JVM reserva memoria para el JIT, metadatos de clases, etc.

3. **Barrera entre lenguajes:** Llamar código Python desde Java (o viceversa) requiere
   mecanismos complejos y tiene overhead significativo.

#### Las soluciones de GraalVM

**Compilación Ahead-of-Time (AOT) con Native Image:**

GraalVM puede compilar una aplicación Java completa a un **ejecutable nativo** (como un
programa C). El resultado:

- **Tiempo de inicio de milisegundos** en lugar de segundos
- **Consumo de memoria reducido** dramáticamente
- **Sin necesidad de JVM instalada** para ejecutar

Esto es ideal para:

- Aplicaciones de línea de comandos
- Microservicios en contenedores (donde cada milisegundo de inicio importa)
- Funciones serverless (AWS Lambda, Google Cloud Functions)

**Soporte políglota:**

GraalVM puede ejecutar múltiples lenguajes en la misma máquina virtual con
**interoperabilidad nativa**:

- Java, Kotlin, Scala, Groovy (lenguajes JVM tradicionales)
- JavaScript y Node.js
- Python
- Ruby
- R (lenguaje estadístico)
- LLVM bitcode (código compilado de C, C++, Rust)

Los lenguajes pueden llamarse entre sí directamente, compartiendo objetos sin
serialización.

```{figure} 01/graalvm_polyglot.svg
:label: fig-graalvm-polyglot
:align: center
:width: 85%

GraalVM permite ejecutar múltiples lenguajes en una única máquina virtual con interoperabilidad nativa, sin las barreras tradicionales entre lenguajes.
```

**Compilador optimizador avanzado:**

El compilador de GraalVM (escrito en Java, irónicamente) produce código más optimizado que
el compilador JIT tradicional de HotSpot para muchas cargas de trabajo, especialmente las
que usan mucha programación funcional o abstracciones complejas.

---

## Referencias Bibliográficas

Para profundizar en la historia técnica y social de Java, se recomiendan las siguientes
fuentes:

- **Bloch, J.** (2018). _Effective Java_ (3ra ed.). Addison-Wesley Professional.
  (Referencia fundamental sobre las mejores prácticas y el diseño del lenguaje).
- **Gosling, J., Joy, B., Steele, G., & Bracha, G.** (2015). _The Java Language
  Specification, Java SE 8 Edition_. Oracle America, Inc.
  [Disponible en línea](https://docs.oracle.com/javase/specs/).
- **Naughton, P.** (1996). _The Java Handbook_. McGraw-Hill. (Escrito por uno de los
  miembros originales del Green Team).
- **Oracle Corporation.** (2023). _Java Timeline_.
  [Oracle.com](https://www.oracle.com/java/moved-by-java/timeline/).
- **Venners, B.** (2003). _The Making of Java: A Conversation with James Gosling_.
  Artima.com. [Entrevista histórica](https://www.artima.com/articles/the-making-of-java).

---

## Ejercicios

```{exercise}
:label: ej-java-historia-1

Investigá y respondé: ¿En qué se diferencia el manejo de memoria en Java (Garbage Collector) respecto al manejo en C? ¿Qué impacto tiene esto en la seguridad del software?
```

```{solution} ej-java-historia-1
:class: dropdown

En **C**, el programador es responsable de reservar (`malloc`) y liberar (`free`) la memoria. Si se olvida de liberar, se producen fugas de memoria (*leaks*); si la libera dos veces o accede a un puntero inválido, el programa falla de forma impredecible.

En **Java**, existe el **Garbage Collector (GC)**, un proceso automático que identifica qué objetos ya no están en uso y libera su memoria.

**Impacto en la seguridad:**
1. **Eliminación de punteros colgantes:** No podés acceder a una dirección de memoria que ya fue liberada.
2. **Prevención de Buffer Overflows:** Java verifica los límites de los arreglos en tiempo de ejecución, impidiendo que un atacante escriba fuera del espacio asignado.
```

```{exercise}
:label: ej-java-historia-2

¿Por qué se dice que el Bytecode de Java es para una "máquina de pila"? Buscá qué significa este concepto y cómo se diferencia de una arquitectura basada en registros (como la de un procesador Intel o ARM).
```

```{solution} ej-java-historia-2
:class: dropdown

Una **máquina de pila (stack machine)** realiza operaciones utilizando una estructura de datos de pila (LIFO). Por ejemplo, para sumar `2 + 3`, el bytecode hace: `push 2`, `push 3`, `add`. El resultado queda en el tope de la pila.

Un procesador físico (Intel/ARM) usa **registros** (pequeñas memorias internas muy rápidas): `MOV EAX, 2`, `ADD EAX, 3`.

**Diferencia clave:**
- La arquitectura de pila es más fácil de implementar para una máquina virtual y genera un bytecode muy compacto e independiente del hardware.
- La arquitectura de registros es más rápida pero depende directamente del diseño específico del procesador.
```

```{exercise}
:label: ej-java-historia-3

Investigá sobre el "Proyecto Loom" de Java. ¿Qué problema resuelven los Virtual Threads y en qué se diferencian de los threads tradicionales del sistema operativo?
```

```{solution} ej-java-historia-3
:class: dropdown

Los **Virtual Threads** (hilos virtuales) resuelven el problema de la concurrencia masiva. Los threads tradicionales del sistema operativo son recursos costosos:
- Cada thread consume ~1MB de memoria para su stack.
- El cambio de contexto entre threads tiene overhead significativo.
- Un servidor típico puede manejar miles, pero no millones de threads.

Los Virtual Threads son:
- **Livianos:** Consumen kilobytes, no megabytes.
- **Gestionados por la JVM:** No mapean 1:1 con threads del SO.
- **Escalables:** Podés crear millones de ellos.
- **Compatibles:** Usan las mismas APIs que los threads tradicionales (`Thread`, `ExecutorService`).

Esto permite escribir código secuencial y bloqueante (más simple) mientras se obtiene la escalabilidad del código asíncrono.
```

```{exercise}
:label: ej-java-historia-4

Compará brevemente Java, Kotlin y Scala. ¿Por qué un equipo de desarrollo elegiría uno sobre otro? Mencioná al menos dos escenarios de uso para cada uno.
```

```{solution} ej-java-historia-4
:class: dropdown

**Java:**
- Estabilidad y compatibilidad hacia atrás garantizadas.
- Mayor cantidad de desarrolladores disponibles en el mercado.
- *Escenarios:* Sistemas bancarios legacy, aplicaciones empresariales de largo plazo.

**Kotlin:**
- Sintaxis más concisa, null-safety integrado.
- Coroutines nativas para programación asíncrona.
- *Escenarios:* Desarrollo Android (lenguaje oficial), nuevos proyectos backend con Spring.

**Scala:**
- Sistema de tipos avanzado, programación funcional de primera clase.
- Inferencia de tipos sofisticada, pattern matching poderoso.
- *Escenarios:* Procesamiento de datos con Apache Spark, sistemas que requieren modelado de dominio complejo.

La elección depende del equipo existente, los requisitos del proyecto y el ecosistema de bibliotecas necesarias.
```

```{exercise}
:label: ej-java-historia-5

Explicá con tus propias palabras qué significa "Write Once, Run Anywhere" (WORA). ¿Por qué este principio fue revolucionario en 1995? ¿Qué componente técnico de Java lo hace posible?
```

```{solution} ej-java-historia-5
:class: dropdown

**WORA** significa que un programa Java, una vez compilado, puede ejecutarse en cualquier sistema operativo (Windows, Linux, macOS) y cualquier tipo de procesador (Intel, ARM, etc.) sin necesidad de recompilarlo o modificarlo.

**Por qué fue revolucionario en 1995:**
- En esa época, cada combinación de sistema operativo y procesador requería compilar el programa específicamente para ella.
- Un programa para Windows/Intel no funcionaba en Mac/Motorola ni en Unix/SPARC.
- Esto multiplicaba el trabajo de desarrollo y testing por cada plataforma soportada.
- La web estaba naciendo y necesitaba código que funcionara en cualquier navegador, sin importar el sistema del usuario.

**El componente técnico que lo hace posible: la JVM (Java Virtual Machine)**

El código Java se compila a **bytecode**, un formato intermedio que no es específico de ningún hardware. Luego, la JVM de cada plataforma traduce ese bytecode a instrucciones del procesador local. Cada sistema operativo tiene su propia implementación de la JVM, pero todas entienden el mismo bytecode estándar.
```

```{exercise}
:label: ej-java-historia-6

Un compañero te dice: "Java es lento porque es interpretado, no como C++ que se compila a código máquina". ¿Es correcta esta afirmación? Explicá tu respuesta mencionando el rol del compilador JIT.
```

```{solution} ej-java-historia-6
:class: dropdown

La afirmación es **parcialmente incorrecta** y representa una visión desactualizada de Java.

**Es verdad que:**
- Java inicialmente interpreta el bytecode, lo que es más lento que ejecutar código máquina directamente.
- C++ se compila directamente a código máquina del procesador, sin intermediarios.

**Pero la realidad moderna es más compleja:**

La JVM incluye un **compilador JIT (Just-In-Time)** que detecta las partes del código que se ejecutan frecuentemente ("puntos calientes" o *hotspots*) y las compila **a código máquina nativo** mientras el programa se ejecuta.

Esto significa que:
1. La primera vez que se ejecuta un método, se interpreta (lento).
2. Después de varias ejecuciones, el JIT lo compila a código nativo (rápido).
3. El JIT puede hacer **optimizaciones dinámicas** que un compilador estático como el de C++ no puede hacer, porque tiene información sobre el comportamiento real del programa en ejecución.

En la práctica, para aplicaciones de larga duración (servidores, por ejemplo), **Java puede ser tan rápido o incluso más rápido que C++** en ciertos escenarios, gracias a las optimizaciones del JIT.

Donde Java sí es más lento es en el **tiempo de inicio** (la JVM necesita cargarse) y en el **consumo de memoria** (la JVM consume recursos adicionales). GraalVM con Native Image resuelve estos problemas.
```
