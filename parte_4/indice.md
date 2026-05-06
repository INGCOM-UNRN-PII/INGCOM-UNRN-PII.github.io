---
title: "Parte 4: Patrones de Diseño"
subtitle: "Soluciones Probadas a Problemas Recurrentes"
subject: Programación Orientada a Objetos
---

(oop5-patrones-diseno)=
# Parte 4: Patrones de Diseño

En los capítulos anteriores dominamos los fundamentos de OOP ({ref}`fundamentos-de-la-programacion-orientada-a-objetos`), las relaciones entre objetos ({ref}`oop2-encapsulamiento-relaciones`), herencia y polimorfismo ({ref}`oop3-herencia-polimorfismo` y {ref}`java-herencia-polimorfismo`), y el diseño por contratos ({ref}`oop-contratos`).

Ahora aplicamos todo ese conocimiento en **patrones de diseño**: soluciones elegantes y probadas a problemas que aparecen una y otra vez en el desarrollo de software.

:::{tip} Objetivos de Aprendizaje

Al finalizar esta parte, vas a poder:

1. Entender qué son los patrones de diseño y por qué existen
2. Reconocer problemas comunes y sus soluciones
3. Aplicar patrones creacionales: Factory, Singleton, Builder
4. Aplicar patrones estructurales: Adapter, Decorator, Composite
5. Aplicar patrones de comportamiento: Strategy, Observer, Template Method
6. Decidir cuándo usar (y cuándo no usar) cada patrón
::: 

---

(navegacion-familias-patrones)=
## Navegación por Familias

Esta portada funciona como mapa general. Para entrar a cada familia y navegar sus patrones individuales:

| Familia | Enlace | Qué vas a encontrar |
| :--- | :--- | :--- |
| **Creacionales** | [Ir al índice creacional](creacionales/indice.md) | Singleton, Factory, Abstract Factory, Builder y Prototype |
| **Estructurales** | [Ir al índice estructural](estructurales/indice.md) | Adapter, Bridge, Composite, Decorator, Facade, Flyweight y Proxy |
| **Comportamiento** | [Ir al índice de comportamiento](comportamiento/indice.md) | Strategy, Observer, Command, State, Template Method y otros |

:::{note}
Si se necesita una vista panorámica, conviene leer esta portada. Si se necesita estudiar una familia completa, conviene entrar directamente a su índice específico.
:::

## Práctica de cursada

Parte 4 ya funciona como referencia. Para usarla como tramo de cursada, conviene trabajarla así:

| Escala | Qué hacer |
| :--- | :--- |
| Patrón individual | Leer el contexto, contrastar **cuándo aplica / cuándo no aplica** y resolver el mini ejercicio del final |
| Familia creacional | Cerrar con [ejercicios integradores de creacionales](creacionales/ejercicios_integradores.md) |
| Familia estructural | Cerrar con [ejercicios integradores de estructurales](estructurales/ejercicios_integradores.md) |
| Familia de comportamiento | Cerrar con [ejercicios integradores de comportamiento](comportamiento/ejercicios_integradores.md) |

## Orden sugerido de lectura

| Orden | Página | Rol en el recorrido |
| :--- | :--- | :--- |
| 0 | Esta portada | Mapa general de la parte y criterios de uso |
| 1 | [Familia Creacional](creacionales/indice.md) | Comparar estrategias de creación |
| 2 | [Singleton](creacionales/singleton.md), [Factory Method](creacionales/factory.md), [Abstract Factory](creacionales/abstract_factory.md), [Builder](creacionales/builder.md), [Prototype](creacionales/prototype.md) | Recorrer los patrones creacionales |
| 3 | [Ejercicios integradores de patrones creacionales](creacionales/ejercicios_integradores.md) | Cerrar la familia con práctica comparativa |
| 4 | [Familia Estructural](estructurales/indice.md) | Entender composición, adaptación y desacoplamiento |
| 5 | [Adapter](estructurales/adapter.md), [Bridge](estructurales/bridge.md), [Composite](estructurales/composite.md), [Decorator](estructurales/decorator.md), [Facade](estructurales/facade.md), [Flyweight](estructurales/flyweight.md), [Proxy](estructurales/proxy.md) | Recorrer los patrones estructurales |
| 6 | [Ejercicios integradores de patrones estructurales](estructurales/ejercicios_integradores.md) | Cerrar la familia con decisiones de arquitectura |
| 7 | [Familia de Comportamiento](comportamiento/indice.md) | Ordenar responsabilidades, algoritmos e interacciones |
| 8 | [Chain of Responsibility](comportamiento/chain_of_responsibility.md), [Command](comportamiento/command.md), [Interpreter](comportamiento/interpreter.md), [Iterator](comportamiento/iterator.md), [Mediator](comportamiento/mediator.md), [Memento](comportamiento/memento.md), [Observer](comportamiento/observer.md), [State](comportamiento/state.md), [Strategy](comportamiento/strategy.md), [Template Method](comportamiento/template_method.md), [Visitor](comportamiento/visitor.md) | Recorrer los patrones de comportamiento |
| 9 | [Ejercicios integradores de patrones de comportamiento](comportamiento/ejercicios_integradores.md) | Cerrar la familia con comparación entre patrones |

## Capítulos nucleares

Estos capítulos conviene dominarlos sí o sí para construir un criterio de diseño reutilizable:

| Capítulo | Por qué es nuclear |
| :--- | :--- |
| [Factory Method](creacionales/factory.md) | Fija la lógica de desacoplar creación y uso |
| [Builder](creacionales/builder.md) | Introduce construcción paso a paso y objetos complejos |
| [Adapter](estructurales/adapter.md) | Instala el problema de compatibilidad entre interfaces |
| [Decorator](estructurales/decorator.md) | Muestra cómo extender comportamiento sin herencia explosiva |
| [Strategy](comportamiento/strategy.md) | Fija la idea de algoritmos intercambiables |
| [Observer](comportamiento/observer.md) | Introduce coordinación entre objetos con bajo acoplamiento |

## Repaso y ampliación

Estas páginas cumplen mejor un rol de navegación, consolidación o profundización:

| Página | Tipo | Uso sugerido |
| :--- | :--- | :--- |
| [Familia Creacional](creacionales/indice.md) | Índice de familia | Usar como mapa comparativo antes o después de los patrones individuales |
| [Familia Estructural](estructurales/indice.md) | Índice de familia | Releer al decidir entre composición, adaptación y control de acceso |
| [Familia de Comportamiento](comportamiento/indice.md) | Índice de familia | Usar como tabla de decisión para algoritmos, estados e interacciones |
| [Singleton](creacionales/singleton.md) | Profundización | Revisar con cautela por sus trade-offs de diseño |
| [Abstract Factory](creacionales/abstract_factory.md) | Profundización | Releer cuando haga falta coherencia entre familias de objetos |
| [Prototype](creacionales/prototype.md) | Profundización | Volver cuando el costo de creación o clonado sea central |
| [Bridge](estructurales/bridge.md) | Profundización | Releer si aparecen jerarquías combinatorias |
| [Composite](estructurales/composite.md) | Profundización | Releer frente a árboles o estructuras recursivas |
| [Facade](estructurales/facade.md), [Flyweight](estructurales/flyweight.md), [Proxy](estructurales/proxy.md) | Ampliación | Consultar según problemas concretos de simplificación, memoria o acceso |
| [Chain of Responsibility](comportamiento/chain_of_responsibility.md), [Command](comportamiento/command.md), [Interpreter](comportamiento/interpreter.md), [Iterator](comportamiento/iterator.md), [Mediator](comportamiento/mediator.md), [Memento](comportamiento/memento.md), [State](comportamiento/state.md), [Template Method](comportamiento/template_method.md), [Visitor](comportamiento/visitor.md) | Ampliación | Consultar según el problema dominante de control de flujo o interacción |
| [Ejercicios integradores creacionales](creacionales/ejercicios_integradores.md), [estructurales](estructurales/ejercicios_integradores.md) y [de comportamiento](comportamiento/ejercicios_integradores.md) | Consolidación | Usar para cerrar cada familia con comparación y justificación |

## Índice exhaustivo

### Creacionales

- [Familia Creacional](creacionales/indice.md)
- [Singleton](creacionales/singleton.md)
- [Factory Method](creacionales/factory.md)
- [Abstract Factory](creacionales/abstract_factory.md)
- [Builder](creacionales/builder.md)
- [Prototype](creacionales/prototype.md)
- [Ejercicios integradores de patrones creacionales](creacionales/ejercicios_integradores.md)

### Estructurales

- [Familia Estructural](estructurales/indice.md)
- [Adapter](estructurales/adapter.md)
- [Bridge](estructurales/bridge.md)
- [Composite](estructurales/composite.md)
- [Decorator](estructurales/decorator.md)
- [Facade](estructurales/facade.md)
- [Flyweight](estructurales/flyweight.md)
- [Proxy](estructurales/proxy.md)
- [Ejercicios integradores de patrones estructurales](estructurales/ejercicios_integradores.md)

### Comportamiento

- [Familia de Comportamiento](comportamiento/indice.md)
- [Chain of Responsibility](comportamiento/chain_of_responsibility.md)
- [Command](comportamiento/command.md)
- [Interpreter](comportamiento/interpreter.md)
- [Iterator](comportamiento/iterator.md)
- [Mediator](comportamiento/mediator.md)
- [Memento](comportamiento/memento.md)
- [Observer](comportamiento/observer.md)
- [State](comportamiento/state.md)
- [Strategy](comportamiento/strategy.md)
- [Template Method](comportamiento/template_method.md)
- [Visitor](comportamiento/visitor.md)
- [Ejercicios integradores de patrones de comportamiento](comportamiento/ejercicios_integradores.md)

---

(que-son-patrones)=
## ¿Qué son los Patrones de Diseño?

(definicion-patron)=
### Definición

Un **patrón de diseño** es una solución general y reutilizable a un problema común en el diseño de software. No es código listo para usar, sino una **plantilla** o **receta** que describe cómo resolver un problema en diferentes contextos.

:::{note} Definición Formal

Un **patrón de diseño** nombra, abstrae e identifica los aspectos clave de una estructura de diseño común que lo hacen útil para crear un diseño orientado a objetos reutilizable.

— Gang of Four (GoF), "Design Patterns: Elements of Reusable Object-Oriented Software", 1994
:::

Los patrones de diseño, establecen un lenguaje, y aunque no se utilice exactamente como se lo describe aquí, facilita la comunicación entre desarrolladores, introduciendo conceptos reutilizables de alto nivel.

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

:::{warning} **Antipatrón: Patternitis**

No todo necesita un patrón. Usar patrones donde no hacen falta agrega complejidad innecesaria. Un patrón es la respuesta a un problema específico; si no tenés ese problema, no necesitás ese patrón.
:::

---

(maestro-patrones-creacionales)=
## Patrones Creacionales

Los patrones creacionales abstraen el proceso de instanciación de objetos, haciendo el sistema independiente de cómo se crean, componen y representan los objetos.

Para un análisis profundo de patrones creacionales, incluidos **Singleton, Factory Method, Abstract Factory, Builder y Prototype**, consultá la sección dedicada: [índice de patrones creacionales](creacionales/indice.md).

### Resumen Rápido

| Patrón | Propósito | Ejemplo |
| :--- | :--- | :--- |
| **Factory Method** | Delega creación a subclases | Sistema de transporte |
| **Abstract Factory** | Crea familias de objetos | UI multiplataforma |
| **Singleton** | Garantiza una única instancia | Logger, Configuración |
| **Builder** | Construye objetos complejos paso a paso | Constructor de pizzas |
| **Prototype** | Clona objetos existentes | Clonar documentos |

---

(maestro-patrones-estructurales)=
## Patrones Estructurales

Los patrones estructurales se ocupan de cómo se componen las clases y objetos para formar estructuras más grandes, facilitando la comunicación entre entidades.

Para un análisis profundo de patrones estructurales, incluidos **Adapter, Bridge, Composite, Decorator, Facade, Flyweight y Proxy**, consultá la sección dedicada: [índice de patrones estructurales](estructurales/indice.md).

### Resumen Rápido

| Patrón | Propósito | Ejemplo |
| :--- | :--- | :--- |
| **Adapter** | Convierte interfaz incompatible | Integración con sistemas legacy |
| **Bridge** | Desacopla abstracción de implementación | Múltiples drivers |
| **Composite** | Estructura jerárquica uniforme | Sistema de archivos, UI |
| **Decorator** | Agrega responsabilidades dinámicamente | Java I/O, coffee shop |
| **Facade** | Interfaz simplificada a subsistema | API unificada |
| **Flyweight** | Comparte objetos para optimizar | Cache, pool de objetos |
| **Proxy** | Controla acceso a otro objeto | Lazy loading, logging |

---

(maestro-patrones-comportamiento)=
## Patrones de Comportamiento

Los patrones de comportamiento se ocupan de algoritmos y la asignación de responsabilidades entre objetos.

Para un análisis profundo de patrones de comportamiento, incluidos **Strategy, Observer, Template Method, State, Command** y otros, consultá la sección dedicada: [índice de patrones de comportamiento](comportamiento/indice.md).

### Resumen Rápido

| Patrón | Propósito | Ejemplo |
| :--- | :--- | :--- |
| **Strategy** | Encapsula algoritmos intercambiables | Múltiples formas de ordenamiento |
| **Observer** | Notifica cambios a múltiples objetos | Suscripciones, eventos |
| **Template Method** | Define esqueleto reutilizable | Procesamiento de datos |
| **State** | Cambia comportamiento según estado | Máquina de estados |
| **Command** | Encapsula solicitudes como objetos | Undo/Redo |
| **Iterator** | Accede elementos secuencialmente | Recorrido de colecciones |
| **Mediator** | Centraliza comunicación | Chat, panel de control |
| **Chain of Responsibility** | Pasa solicitudes en cadena | Manejo de excepciones |

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

:::{tip} Regla de Oro

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

## Próximo paso

Para seguir, conviene entrar a una familia concreta de patrones: [creacionales](creacionales/indice.md), [estructurales](estructurales/indice.md) o [de comportamiento](comportamiento/indice.md).
