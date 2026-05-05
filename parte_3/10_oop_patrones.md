---
title: "10: Patrones de Diseño Fundamentales"
subtitle: "Soluciones Probadas a Problemas Recurrentes"
subject: Programación Orientada a Objetos
---

(oop5-patrones-diseno)=
# OOP 5: Patrones de Diseño Fundamentales

En los capítulos anteriores dominamos los fundamentos de OOP ({ref}`fundamentos-de-la-programacion-orientada-a-objetos`), las relaciones entre objetos ({ref}`oop2-encapsulamiento-relaciones`), herencia y polimorfismo ({ref}`oop3-herencia-polimorfismo` y {ref}`java-herencia-polimorfismo`), y el diseño por contratos ({ref}`oop-contratos`).

Ahora aplicamos todo ese conocimiento en **patrones de diseño**: soluciones elegantes y probadas a problemas que aparecen una y otra vez en el desarrollo de software.

:::{admonition} Objetivos de Aprendizaje
:class: tip

Al finalizar este capítulo, serás capaz de:

1. Entender qué son los patrones de diseño y por qué existen
2. Reconocer problemas comunes y sus soluciones
3. Aplicar patrones creacionales: Factory, Singleton, Builder
4. Aplicar patrones estructurales: Adapter, Decorator, Composite
5. Aplicar patrones de comportamiento: Strategy, Observer, Template Method
6. Decidir cuándo usar (y cuándo no usar) cada patrón
:::

---

(que-son-patrones)=
## ¿Qué son los Patrones de Diseño?

(definicion-patron)=
### Definición

Un **patrón de diseño** es una solución general y reutilizable a un problema común en el diseño de software. No es código listo para usar, sino una **plantilla** o **receta** que describe cómo resolver un problema en diferentes contextos.

:::{admonition} Definición Formal
:class: note

Un **patrón de diseño** nombra, abstrae e identifica los aspectos clave de una estructura de diseño común que lo hacen útil para crear un diseño orientado a objetos reutilizable.

— Gang of Four (GoF), "Design Patterns: Elements of Reusable Object-Oriented Software", 1994
:::

(origen-patrones)=
### Origen e Historia

Los patrones de diseño en software se popularizaron con el libro de los "Gang of Four" (GoF) en 1994:

- **Erich Gamma**
- **Richard Helm**
- **Ralph Johnson**
- **John Vlissides**

Documentaron 23 patrones clasificados en tres categorías:

| Categoría | Propósito | Ejemplos |
| :--- | :--- | :--- |
| **Creacionales** | Cómo crear objetos | Factory, Singleton, Builder |
| **Estructurales** | Cómo componer objetos | Adapter, Decorator, Composite |
| **Comportamiento** | Cómo interactúan objetos | Strategy, Observer, Template |

(anatomia-patron)=
### Anatomía de un Patrón

Cada patrón se describe con:

1. **Nombre**: Identificador conciso y memorable
2. **Problema**: Cuándo aplicar el patrón
3. **Solución**: Estructura de clases y objetos
4. **Consecuencias**: Trade-offs y resultados

(beneficios-patrones)=
### Beneficios

1. **Vocabulario común**: "Usemos un Observer aquí" es más claro que explicar toda la estructura
2. **Soluciones probadas**: No reinventar la rueda
3. **Diseños flexibles**: Anticipan cambios futuros
4. **Documentación implícita**: El nombre del patrón comunica la intención

:::{warning}
**Antipatrón: Patternitis**

No todo necesita un patrón. Usar patrones donde no hacen falta agrega complejidad innecesaria. Un patrón es la respuesta a un problema específico; si no tenés ese problema, no necesitás ese patrón.
:::

---

(patrones-creacionales)=
## Patrones Creacionales

Los patrones creacionales abstraen el proceso de instanciación de objetos, haciendo el sistema independiente de cómo se crean, componen y representan los objetos.

(patron-factory-method)=
### Factory Method

#### Problema

Necesitás crear objetos, pero no sabés de antemano qué tipo concreto crear. El código cliente no debería conocer las clases concretas.

#### Solución

Definí una interfaz para crear objetos, pero dejá que las subclases decidan qué clase instanciar.

```{mermaid}
classDiagram
    class Creador {
        <<interface>>
        +crearProducto() Producto
        +operacion()
    }
    
    class CreadorConcreto A {
        +crearProducto() Producto
    }
    
    class CreadorConcretoB {
        +crearProducto() Producto
    }
    
    class Producto {
        <<interface>>
        +operacion()
    }
    
    class ProductoA {
        +operacion()
    }
    
    class ProductoB {
        +operacion()
    }
    
    Creador <|.. CreadorConcreto A
    Creador <|.. CreadorConcretoB
    Producto <|.. ProductoA
    Producto <|.. ProductoB
    
    CreadorConcreto A ..> ProductoA : crea
    CreadorConcretoB ..> ProductoB : crea
    Creador ..> Producto : usa
    
    note for Creador "Factory Method define<br>la interfaz de creación"
    note for CreadorConcreto A "Subclases deciden<br>qué crear"
```

#### Ejemplo: Fábrica de Documentos

**Sin Factory (código acoplado):**

```
// ❌ El código cliente conoce todas las clases concretas
Documento doc;
if (tipo.equals("pdf")) {
    doc = new DocumentoPDF();
} else if (tipo.equals("word")) {
    doc = new DocumentoWord();
} else if (tipo.equals("html")) {
    doc = new DocumentoHTML();
}
// Si agrego "markdown", debo modificar este código
```

**Con Factory:**

```
// ✓ El código cliente solo conoce la interfaz
Documento doc = fabrica.crearDocumento(tipo);
doc.abrir();
doc.editar();
doc.guardar();
// Si agrego "markdown", solo creo nueva clase y registro en la fábrica
```

#### Cuándo Usar

- Cuando no sabés de antemano los tipos exactos de objetos a crear
- Cuando querés delegar la creación a subclases
- Cuando querés centralizar la lógica de creación

---

(patron-abstract-factory)=
### Abstract Factory

#### Problema

Necesitás crear **familias de objetos relacionados** sin especificar sus clases concretas. Por ejemplo, componentes de UI que deben ser consistentes (todos Windows o todos Mac).

#### Solución

Provee una interfaz para crear familias de objetos relacionados sin especificar clases concretas.

```
┌─────────────────────────────┐
│       «interface»           │
│       FabricaUI             │
│─────────────────────────────│
│ + crearBoton(): Boton       │
│ + crearCheckbox(): Checkbox │
│ + crearVentana(): Ventana   │
└─────────────┬───────────────┘
              │
     ┌────────┴────────┐
     │                 │
     ▼                 ▼
┌─────────────┐   ┌─────────────┐
│FabricaWindows│  │ FabricaMac  │
│─────────────│   │─────────────│
│+crearBoton()│   │+crearBoton()│
│  → BotonWin │   │  → BotonMac │
└─────────────┘   └─────────────┘
```

#### Ejemplo: UI Multiplataforma

```
// El código cliente trabaja con abstracciones
FabricaUI fabrica = obtenerFabricaParaPlataforma();

Boton boton = fabrica.crearBoton();       // BotonWindows o BotonMac
Checkbox check = fabrica.crearCheckbox(); // CheckboxWindows o CheckboxMac
Ventana ventana = fabrica.crearVentana(); // VentanaWindows o VentanaMac

// Todos los componentes son consistentes entre sí
ventana.agregar(boton);
ventana.agregar(check);
ventana.mostrar();
```

#### Cuándo Usar

- Cuando el sistema debe ser independiente de cómo se crean los productos
- Cuando necesitás garantizar que los productos de una familia sean compatibles entre sí
- Cuando querés proveer una biblioteca de productos sin exponer implementaciones

---

(patron-singleton)=
### Singleton

#### Problema

Necesitás exactamente **una instancia** de una clase, accesible globalmente. Por ejemplo: configuración, logger, pool de conexiones.

#### Solución

La clase controla su propia instanciación y provee un punto de acceso global.

```{mermaid}
classDiagram
    class Singleton {
        -Singleton instancia$
        -Map configuracion
        -Singleton()
        +obtenerInstancia()$ Singleton
        +getConfiguracion() Map
        +setConfiguracion(String, Object)
    }
    
    note for Singleton "Patrón Singleton:<br>- Constructor privado<br>- Instancia estática única<br>- Método de acceso global"
```

#### Implementación Conceptual

```
Singleton {
    private static instancia: Singleton = null
    
    private Singleton() {
        // Constructor privado
    }
    
    public static obtenerInstancia(): Singleton {
        if (instancia == null) {
            instancia = new Singleton()
        }
        return instancia
    }
}

// Uso
Singleton s1 = Singleton.obtenerInstancia();
Singleton s2 = Singleton.obtenerInstancia();
// s1 y s2 son la MISMA instancia
```

#### Cuándo Usar

- Cuando debe existir exactamente una instancia de una clase
- Cuando esa instancia debe ser accesible desde un punto conocido
- Ejemplos: Configuración, Logger, Cache, Pool de conexiones

:::{warning}
**Controversia del Singleton**

El Singleton es uno de los patrones más criticados:

- Introduce **estado global** (dificulta testing, ver {ref}`oop-testing`)
- Viola el **Principio de Responsabilidad Única** (ver {ref}`s-principio-de-responsabilidad-unica`) - la clase controla su ciclo de vida
- Dificulta el **testing** (no se puede inyectar mock fácilmente)

Alternativa: **Inyección de Dependencias** (ver {ref}`d-principio-de-inversion-de-dependencias`) - crear la instancia externamente e inyectarla donde se necesita.
:::

---

(patron-builder)=
### Builder

#### Problema

Necesitás crear objetos complejos con muchos parámetros opcionales. Los constructores con muchos parámetros son difíciles de usar y entender.

#### Solución

Separá la construcción de un objeto complejo de su representación, permitiendo crear diferentes representaciones con el mismo proceso.

```{mermaid}
classDiagram
    class Pizza {
        -String tamaño
        -String masa
        -String queso
        -boolean tomate
        -boolean jamon
        -boolean cebolla
        +Pizza()
    }
    
    class PizzaBuilder {
        -String tamaño
        -String masa
        -String queso
        -boolean tomate
        -boolean jamon
        -boolean cebolla
        +tamaño(String) PizzaBuilder
        +masa(String) PizzaBuilder
        +conQueso(String) PizzaBuilder
        +conTomate() PizzaBuilder
        +conJamon() PizzaBuilder
        +sinCebolla() PizzaBuilder
        +construir() Pizza
    }
    
    PizzaBuilder ..> Pizza : construye
    
    note for PizzaBuilder "Fluent Interface:<br>Métodos encadenables<br>que retornan this"
    note for Pizza "Objeto complejo<br>con múltiples parámetros"
```

#### Ejemplo: Constructor de Pizzas

**Sin Builder (constructor telescópico):**

```
// ❌ Difícil de leer y usar
Pizza p = new Pizza("grande", "fina", true, false, true, true, false, "muzzarella");
// ¿Qué significa cada boolean?
```

**Con Builder (fluent interface):**

```
// ✓ Claro y expresivo
Pizza pizza = new PizzaBuilder()
    .tamaño("grande")
    .masa("fina")
    .conQueso("muzzarella")
    .conTomate()
    .conJamon()
    .sinCebolla()
    .construir();
```

#### Ejemplo: Constructor de Emails

```
Email email = new EmailBuilder()
    .de("remitente@ejemplo.com")
    .para("destinatario@ejemplo.com")
    .cc("copia@ejemplo.com")           // opcional
    .asunto("Reunión mañana")
    .cuerpo("Confirmamos la reunión...")
    .adjuntar("agenda.pdf")            // opcional
    .adjuntar("mapa.png")              // opcional
    .conPrioridadAlta()                // opcional
    .construir();
```

#### Cuándo Usar

- Cuando el algoritmo de creación debe ser independiente de las partes que componen el objeto
- Cuando el proceso de construcción debe permitir diferentes representaciones
- Cuando hay muchos parámetros opcionales

---

(patrones-estructurales)=
## Patrones Estructurales

Los patrones estructurales se ocupan de cómo se componen las clases y objetos para formar estructuras más grandes.

(patron-adapter)=
### Adapter (Adaptador)

#### Problema

Tenés una clase con una interfaz incompatible con lo que el cliente espera. Necesitás que clases con interfaces incompatibles trabajen juntas.

#### Solución

Crea una clase intermedia que "traduce" entre las dos interfaces.

```{mermaid}
classDiagram
    class Target {
        <<interface>>
        +operacion()
    }
    
    class Cliente {
        +ejecutar()
    }
    
    class Adaptador {
        -Adaptado adaptado
        +operacion()
    }
    
    class Adaptado {
        +metodoEspecifico()
    }
    
    Cliente --> Target : usa
    Target <|.. Adaptador : implementa
    Adaptador --> Adaptado : delega
    
    note for Adaptador "Traduce entre<br>Target y Adaptado"
    note for Adaptado "Clase existente<br>con interfaz incompatible"
```

#### Ejemplo: Adaptador de Formatos

Tenemos un sistema que trabaja con JSON, pero necesitamos integrar una librería que solo maneja XML:

```
// Interfaz que nuestro sistema espera
interface ProcesadorDatos {
    void procesar(String json);
}

// Librería externa que solo maneja XML
class ProcesadorXMLLegacy {
    void procesarXML(String xml) { ... }
}

// Adaptador que traduce
class AdaptadorXML implements ProcesadorDatos {
    private ProcesadorXMLLegacy procesador;
    
    void procesar(String json) {
        String xml = convertirJsonAXml(json);  // Traducción
        procesador.procesarXML(xml);
    }
}

// Uso transparente
ProcesadorDatos procesador = new AdaptadorXML();
procesador.procesar(miJson);  // Funciona aunque internamente use XML
```

#### Cuándo Usar

- Cuando querés usar una clase existente pero su interfaz no coincide con lo que necesitás
- Cuando querés crear una clase reutilizable que coopere con clases no relacionadas
- Integración de código legacy o librerías de terceros

---

(patron-decorator)=
### Decorator (Decorador)

#### Problema

Necesitás agregar responsabilidades a objetos individuales dinámicamente, sin afectar a otros objetos de la misma clase. La herencia no es viable porque las combinaciones serían exponenciales.

#### Solución

Envuelve el objeto en otro objeto que agrega el comportamiento extra.

```{mermaid}
classDiagram
    class Componente {
        <<interface>>
        +operacion()
    }
    
    class ComponenteConcreto {
        +operacion()
    }
    
    class Decorador {
        <<abstract>>
        #Componente componente
        +Decorador(Componente)
        +operacion()
    }
    
    class DecoradorA {
        +operacion()
        +comportamientoExtra()
    }
    
    class DecoradorB {
        +operacion()
        +otraFuncionalidad()
    }
    
    Componente <|.. ComponenteConcreto
    Componente <|.. Decorador
    Decorador <|-- DecoradorA
    Decorador <|-- DecoradorB
    Decorador o-- Componente : envuelve
    
    note for Decorador "Mantiene referencia<br>al componente envuelto"
    note for DecoradorA "Agrega funcionalidad<br>antes/después de delegar"
```

#### Ejemplo: Café con Decoradores

**Sin Decorator (explosión de clases):**

```
// ❌ Combinaciones exponenciales
CafeSimple
CafeConLeche
CafeConAzucar
CafeConLecheYAzucar
CafeConLecheYCremaBatida
CafeConAzucarYCanela
// ... ¡decenas de clases!
```

**Con Decorator:**

```
// ✓ Composición flexible
Cafe cafe = new CafeSimple();                    // $2.00
cafe = new DecoradorLeche(cafe);                 // +$0.50
cafe = new DecoradorAzucar(cafe);                // +$0.10
cafe = new DecoradorCremaBatida(cafe);           // +$0.75

System.out.println(cafe.descripcion());  // "Café con leche, azúcar, crema batida"
System.out.println(cafe.precio());       // $3.35
```

#### Ejemplo: Streams de Java

Los streams de Java usan el patrón Decorator:

```
// Cada wrapper agrega funcionalidad
InputStream base = new FileInputStream("archivo.txt");
InputStream buffered = new BufferedInputStream(base);      // Agrega buffering
InputStream data = new DataInputStream(buffered);          // Agrega lectura de tipos

// Se pueden combinar libremente
```

#### Cuándo Usar

- Cuando necesitás agregar responsabilidades a objetos individuales dinámicamente
- Cuando la extensión por herencia no es práctica (demasiadas combinaciones)
- Cuando querés que las responsabilidades puedan retirarse

---

(patron-composite)=
### Composite (Compuesto)

#### Problema

Tenés una estructura jerárquica (árbol) donde los clientes deben tratar objetos individuales y composiciones de objetos de manera uniforme.

#### Solución

Compone objetos en estructuras de árbol. Los clientes tratan objetos simples y compuestos uniformemente.

```
┌───────────────────────┐
│     «interface»       │
│      Componente       │
│───────────────────────│
│ + operacion()         │
│ + agregar(c)          │
│ + eliminar(c)         │
│ + obtenerHijo(i)      │
└───────────┬───────────┘
            │
   ┌────────┴────────┐
   │                 │
   ▼                 ▼
┌─────────────┐  ┌─────────────────────┐
│    Hoja     │  │     Compuesto       │
│─────────────│  │─────────────────────│
│+operacion() │  │ - hijos: List       │
│             │  │─────────────────────│
│             │  │ + operacion()       │
│             │  │   → para cada hijo: │
│             │  │     hijo.operacion()│
│             │  │ + agregar(c)        │
│             │  │ + eliminar(c)       │
└─────────────┘  └─────────────────────┘
```

#### Ejemplo: Sistema de Archivos

```
// Interfaz común
interface ElementoArchivo {
    String nombre();
    long tamaño();
    void mostrar(int nivel);
}

// Hoja: Archivo individual
class Archivo implements ElementoArchivo {
    long tamaño() { return this.bytes; }
}

// Compuesto: Carpeta que contiene otros elementos
class Carpeta implements ElementoArchivo {
    private List<ElementoArchivo> contenido;
    
    long tamaño() {
        long total = 0;
        for (ElementoArchivo elem : contenido) {
            total += elem.tamaño();  // Funciona con archivos y carpetas
        }
        return total;
    }
    
    void agregar(ElementoArchivo elem) {
        contenido.add(elem);
    }
}

// Uso uniforme
ElementoArchivo raiz = new Carpeta("Documentos");
raiz.agregar(new Archivo("informe.pdf", 1024));
raiz.agregar(new Carpeta("Fotos"));

System.out.println(raiz.tamaño());  // Calcula recursivamente
```

#### Ejemplo: UI con Componentes Anidados

```
Componente formulario = new Panel("Formulario");
formulario.agregar(new Etiqueta("Nombre:"));
formulario.agregar(new CampoTexto("nombre"));

Componente botonesPanel = new Panel("Botones");
botonesPanel.agregar(new Boton("Guardar"));
botonesPanel.agregar(new Boton("Cancelar"));

formulario.agregar(botonesPanel);

formulario.dibujar();  // Dibuja todo el árbol recursivamente
```

#### Cuándo Usar

- Cuando querés representar jerarquías parte-todo
- Cuando querés que los clientes ignoren la diferencia entre composiciones y objetos individuales
- Ejemplos: Sistemas de archivos, UI, menús, organizaciones

---

(patrones-comportamiento)=
## Patrones de Comportamiento

Los patrones de comportamiento se ocupan de algoritmos y la asignación de responsabilidades entre objetos.

(patron-strategy)=
### Strategy (Estrategia)

#### Problema

Tenés varias formas de hacer algo (algoritmos) y querés poder cambiar entre ellas dinámicamente sin modificar el código cliente.

#### Solución

Definí una familia de algoritmos, encapsulá cada uno, y hacelos intercambiables.

```{mermaid}
classDiagram
    class Contexto {
        -Estrategia estrategia
        +setEstrategia(Estrategia)
        +ejecutarOperacion()
    }
    
    class Estrategia {
        <<interface>>
        +algoritmo()
    }
    
    class EstrategiaA {
        +algoritmo()
    }
    
    class EstrategiaB {
        +algoritmo()
    }
    
    class EstrategiaC {
        +algoritmo()
    }
    
    Contexto o-- Estrategia : usa
    Estrategia <|.. EstrategiaA
    Estrategia <|.. EstrategiaB
    Estrategia <|.. EstrategiaC
    
    note for Contexto "Delega el algoritmo<br>a la estrategia actual"
    note for Estrategia "Familia de algoritmos<br>intercambiables"
```

#### Ejemplo: Estrategias de Ordenamiento

```
interface EstrategiaOrdenamiento {
    void ordenar(List<Integer> lista);
}

class QuickSort implements EstrategiaOrdenamiento {
    void ordenar(List<Integer> lista) { /* QuickSort */ }
}

class MergeSort implements EstrategiaOrdenamiento {
    void ordenar(List<Integer> lista) { /* MergeSort */ }
}

class BubbleSort implements EstrategiaOrdenamiento {
    void ordenar(List<Integer> lista) { /* BubbleSort */ }
}

// Contexto
class Ordenador {
    private EstrategiaOrdenamiento estrategia;
    
    void setEstrategia(EstrategiaOrdenamiento e) {
        this.estrategia = e;
    }
    
    void ordenar(List<Integer> lista) {
        estrategia.ordenar(lista);
    }
}

// Uso
Ordenador ord = new Ordenador();
ord.setEstrategia(new QuickSort());   // Usa QuickSort
ord.ordenar(lista);

ord.setEstrategia(new MergeSort());   // Cambia a MergeSort
ord.ordenar(lista);
```

#### Ejemplo: Estrategias de Pago

```
interface EstrategiaPago {
    void pagar(double monto);
}

class PagoTarjeta implements EstrategiaPago { ... }
class PagoPayPal implements EstrategiaPago { ... }
class PagoCripto implements EstrategiaPago { ... }

class CarritoCompras {
    private EstrategiaPago metodoPago;
    
    void checkout() {
        double total = calcularTotal();
        metodoPago.pagar(total);  // Delega al método elegido
    }
}
```

#### Cuándo Usar

- Cuando tenés múltiples algoritmos relacionados que difieren solo en comportamiento
- Cuando necesitás variantes de un algoritmo
- Cuando un algoritmo usa datos que el cliente no debería conocer
- Cuando una clase define muchos comportamientos con condicionales

---

(patron-observer)=
### Observer (Observador)

#### Problema

Un objeto cambia de estado y otros objetos necesitan ser notificados automáticamente. No querés acoplar el objeto observado a sus observadores.

#### Solución

Definí una dependencia uno-a-muchos donde cuando un objeto cambia, todos sus dependientes son notificados.

```{mermaid}
classDiagram
    class Sujeto {
        <<interface>>
        +agregarObservador(Observador)
        +eliminarObservador(Observador)
        +notificar()
    }
    
    class SujetoConcreto {
        -List~Observador~ observadores
        -String estado
        +setEstado(String)
        +getEstado() String
        +notificar()
    }
    
    class Observador {
        <<interface>>
        +actualizar(String estado)
    }
    
    class ObservadorA {
        +actualizar(String estado)
    }
    
    class ObservadorB {
        +actualizar(String estado)
    }
    
    class ObservadorC {
        +actualizar(String estado)
    }
    
    Sujeto <|.. SujetoConcreto
    SujetoConcreto o-- Observador : notifica a
    Observador <|.. ObservadorA
    Observador <|.. ObservadorB
    Observador <|.. ObservadorC
    
    note for SujetoConcreto "Cuando cambia estado,<br>notifica a todos los observadores"
    note for Observador "Reciben actualizaciones<br>automáticamente"
```

#### Ejemplo: Sistema de Notificaciones

```
interface Observador {
    void actualizar(String mensaje);
}

class Sujeto {
    private List<Observador> observadores = new ArrayList<>();
    private String estado;
    
    void agregarObservador(Observador o) {
        observadores.add(o);
    }
    
    void setEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
        notificar();
    }
    
    private void notificar() {
        for (Observador o : observadores) {
            o.actualizar(estado);
        }
    }
}

// Observadores concretos
class PanelUI implements Observador {
    void actualizar(String mensaje) {
        refrescarPantalla(mensaje);
    }
}

class Logger implements Observador {
    void actualizar(String mensaje) {
        escribirLog(mensaje);
    }
}

class NotificadorEmail implements Observador {
    void actualizar(String mensaje) {
        enviarEmail(mensaje);
    }
}

// Uso
Sujeto sistema = new Sujeto();
sistema.agregarObservador(new PanelUI());
sistema.agregarObservador(new Logger());
sistema.agregarObservador(new NotificadorEmail());

sistema.setEstado("Nuevo pedido recibido");
// Automáticamente: se refresca UI, se loggea, se envía email
```

#### Ejemplo: Suscripción a Eventos

```
// Canal de YouTube
class Canal {
    private List<Suscriptor> suscriptores = new ArrayList<>();
    
    void publicarVideo(Video video) {
        // Notifica a todos los suscriptores
        for (Suscriptor s : suscriptores) {
            s.nuevoVideo(video);
        }
    }
}

// Los suscriptores reciben notificaciones automáticamente
```

#### Cuándo Usar

- Cuando un cambio en un objeto requiere cambiar otros, y no sabés cuántos
- Cuando un objeto debe notificar a otros sin hacer suposiciones sobre quiénes son
- Ejemplos: UI events, suscripciones, sistemas reactivos

---

(patron-template-method)=
### Template Method (Método Plantilla)

#### Problema

Tenés un algoritmo con pasos fijos, pero algunos pasos varían según la implementación. Querés definir el esqueleto del algoritmo y dejar que las subclases redefinan ciertos pasos.

#### Solución

Definí el esqueleto de un algoritmo en una operación, delegando algunos pasos a las subclases.

```{mermaid}
classDiagram
    class ClaseAbstracta {
        <<abstract>>
        +metodoPlantilla() final
        +paso1()* abstract
        +paso2()* abstract
        #paso3() hook
    }
    
    class ClaseConcretaA {
        +paso1()
        +paso2()
        #paso3()
    }
    
    class ClaseConcretaB {
        +paso1()
        +paso2()
    }
    
    ClaseAbstracta <|-- ClaseConcretaA
    ClaseAbstracta <|-- ClaseConcretaB
    
    note for ClaseAbstracta "metodoPlantilla() define<br>el esqueleto del algoritmo:<br>1. paso1()<br>2. paso2()<br>3. paso3()"
    note for ClaseConcretaA "Implementa pasos abstractos<br>Puede override hooks"
```

#### Ejemplo: Preparación de Bebidas

```
abstract class BebidaCaliente {
    
    // Método plantilla - define el algoritmo
    public final void preparar() {
        hervirAgua();
        agregarIngrediente();    // Abstracto
        servirEnTaza();
        agregarCondimentos();    // Hook
    }
    
    private void hervirAgua() {
        System.out.println("Hirviendo agua...");
    }
    
    protected abstract void agregarIngrediente();
    
    private void servirEnTaza() {
        System.out.println("Sirviendo en taza...");
    }
    
    // Hook: implementación por defecto que subclases pueden override
    protected void agregarCondimentos() {
        // Por defecto no agrega nada
    }
}

class Cafe extends BebidaCaliente {
    protected void agregarIngrediente() {
        System.out.println("Agregando café molido...");
    }
    
    protected void agregarCondimentos() {
        System.out.println("Agregando azúcar y leche...");
    }
}

class Te extends BebidaCaliente {
    protected void agregarIngrediente() {
        System.out.println("Agregando bolsita de té...");
    }
    
    protected void agregarCondimentos() {
        System.out.println("Agregando limón...");
    }
}

// Uso
BebidaCaliente bebida = new Cafe();
bebida.preparar();
// Hirviendo agua...
// Agregando café molido...
// Sirviendo en taza...
// Agregando azúcar y leche...
```

#### Ejemplo: Procesamiento de Datos

```
abstract class ProcesadorDatos {
    
    public final void procesar() {
        abrirFuente();
        leerDatos();
        procesarDatos();
        guardarResultados();
        cerrarFuente();
    }
    
    protected abstract void abrirFuente();
    protected abstract void leerDatos();
    protected abstract void guardarResultados();
    protected abstract void cerrarFuente();
    
    // Paso común
    private void procesarDatos() {
        // Lógica compartida de procesamiento
    }
}

class ProcesadorCSV extends ProcesadorDatos {
    protected void abrirFuente() { /* abrir archivo CSV */ }
    protected void leerDatos() { /* parsear CSV */ }
    // ...
}

class ProcesadorAPI extends ProcesadorDatos {
    protected void abrirFuente() { /* conectar a API */ }
    protected void leerDatos() { /* fetch JSON */ }
    // ...
}
```

#### Cuándo Usar

- Cuando querés implementar las partes invariantes de un algoritmo una vez y dejar que las subclases implementen el comportamiento variable
- Cuando el comportamiento común debe estar centralizado en una clase
- Cuando querés controlar las extensiones de las subclases

---

(cuando-usar-patrones)=
## ¿Cuándo Usar (y No Usar) Patrones?

(senales-para-usar)=
### Señales de que Necesitás un Patrón

| Problema | Patrón Sugerido |
| :--- | :--- |
| "Tengo muchos if/else para crear objetos" | Factory |
| "Necesito una sola instancia global" | Singleton (con cuidado) |
| "El constructor tiene demasiados parámetros" | Builder |
| "Necesito adaptar una interfaz incompatible" | Adapter |
| "Quiero agregar funcionalidad dinámicamente" | Decorator |
| "Tengo estructura de árbol/jerarquía" | Composite |
| "Tengo múltiples algoritmos intercambiables" | Strategy |
| "Objetos deben reaccionar a cambios de otro" | Observer |
| "Tengo un algoritmo con pasos variables" | Template Method |

(senales-para-no-usar)=
### Señales de que NO Necesitás un Patrón

- El código es simple y funciona bien
- Solo hay una implementación posible
- No hay necesidad de extensibilidad
- El patrón agrega complejidad sin beneficio claro
- "Por si acaso lo necesite en el futuro"

:::{admonition} Regla de Oro
:class: tip

**No uses un patrón hasta que el problema que resuelve sea evidente.**

Es más fácil refactorizar hacia un patrón cuando lo necesitás que cargar con complejidad innecesaria desde el principio.
:::

---

(ejemplo-combinando-patrones)=
## Ejemplo: Combinando Patrones

Un sistema real suele combinar varios patrones:

```
// FACTORY: crea procesadores según el tipo
ProcesadorFactory factory = new ProcesadorFactory();
Procesador proc = factory.crear(tipoArchivo);

// STRATEGY: diferentes algoritmos de compresión
proc.setCompresion(new CompresionZIP());

// DECORATOR: agrega funcionalidades
proc = new ProcesadorConLog(proc);
proc = new ProcesadorConCache(proc);

// OBSERVER: notifica progreso
proc.agregarObservador(new BarraProgreso());
proc.agregarObservador(new Logger());

// TEMPLATE METHOD (interno al procesador)
proc.procesar(archivo);
```

---

(resumen-oop4)=
## Resumen

### Patrones Creacionales

| Patrón | Propósito |
| :--- | :--- |
| **Factory** | Crear objetos sin especificar clases concretas |
| **Abstract Factory** | Crear familias de objetos relacionados |
| **Singleton** | Garantizar una única instancia |
| **Builder** | Construir objetos complejos paso a paso |

### Patrones Estructurales

| Patrón | Propósito |
| :--- | :--- |
| **Adapter** | Convertir interfaz incompatible |
| **Decorator** | Agregar responsabilidades dinámicamente |
| **Composite** | Tratar objetos y composiciones uniformemente |

### Patrones de Comportamiento

| Patrón | Propósito |
| :--- | :--- |
| **Strategy** | Encapsular algoritmos intercambiables |
| **Observer** | Notificar cambios a múltiples objetos |
| **Template Method** | Definir esqueleto de algoritmo |

### Principios Clave

1. **Identificá el problema primero**: No busques patrones, buscá soluciones
2. **Preferí composición sobre herencia**: Más flexible
3. **Programá hacia interfaces**: Más desacoplado
4. **Evitá la complejidad innecesaria**: YAGNI (You Aren't Gonna Need It)

---

(ejercicios-oop4)=
## Ejercicios

```{exercise}
:label: ej-factory-vehiculos
Diseñá un sistema de fábrica de vehículos:
- Tipos: Auto, Moto, Camión, Bicicleta
- Cada tipo tiene diferentes atributos
- El cliente no debe conocer las clases concretas
- Dibujá el diagrama de clases
- Implementá la lógica conceptual
```

```{exercise}
:label: ej-decorator-notificaciones
Implementá un sistema de notificaciones con decoradores:
- Notificación base: mensaje de texto
- Decoradores: con emoji, con timestamp, con prioridad, encriptada
- Debe ser posible combinar decoradores arbitrariamente
- Ejemplo: `new ConTimestamp(new ConEmoji(new Notificacion("Hola")))`
```

```{exercise}
:label: ej-strategy-descuentos
Diseñá un sistema de descuentos usando Strategy:
- Estrategias: SinDescuento, PorcentajeFijo, MontoFijo, CompraMayorista, ClienteVIP
- El carrito de compras debe poder cambiar de estrategia dinámicamente
- Calculá el precio final según la estrategia activa
```

```{exercise}
:label: ej-observer-bolsa
Implementá un sistema de cotizaciones de bolsa:
- Sujeto: Acción (con símbolo y precio)
- Observadores: PanelCotizaciones, AlertaEmail, AppMóvil, Logger
- Cuando el precio cambia, todos los observadores son notificados
- Algunos observadores solo reaccionan si el cambio supera un umbral
```

```{exercise}
:label: ej-combinar-patrones
Diseñá un sistema de procesamiento de pedidos que combine:
- **Factory**: para crear diferentes tipos de pedidos
- **Builder**: para construir pedidos complejos
- **Strategy**: para diferentes métodos de envío
- **Observer**: para notificar cambios de estado
- **Template Method**: para el proceso de fulfillment

Dibujá el diagrama de clases completo y explicá cómo interactúan los patrones.
```
