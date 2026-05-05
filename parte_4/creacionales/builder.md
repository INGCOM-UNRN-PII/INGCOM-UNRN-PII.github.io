---
title: "Builder"
subtitle: "Construir objetos complejos paso a paso"
subject: Patrones de Diseño Creacionales
---

(patron-builder)=
# Builder

:::{note} Hoja de ruta del capítulo

**Objetivo.** Comprender las ideas centrales de **Builder** y usarlas como base para el resto del recorrido.

**Prerrequisitos.** Conviene haber leído [el material inmediatamente anterior](abstract_factory.md) para llegar con el hilo de la parte fresco.

**Desarrollo.** El desarrollo del capítulo aparece en las secciones que siguen. Conviene recorrerlas en orden y volver al resumen antes de pasar al siguiente tema.
:::

## Definición

El patrón **Builder** (Constructor) es un patrón de diseño creacional que permite construir objetos complejos paso a paso. El patrón permite producir distintos tipos y representaciones de un objeto utilizando el mismo código de construcción.

A diferencia de otros patrones creacionales, el Builder no requiere que los productos tengan una interfaz común, ya que los procesos de construcción pueden variar significativamente entre productos.

## Origen e Historia

Al igual que el resto de los patrones clásicos, fue documentado por el GoF en 1994. Se inspiró en los procesos de fabricación industriales donde un objeto se ensambla a partir de componentes menores siguiendo un orden específico, permitiendo que el mismo proceso de ensamblaje pueda producir diferentes resultados finales.

## Motivacion

La motivación surge ante el problema del "constructor telescópico". Cuando una clase tiene muchos parámetros opcionales, el constructor se vuelve difícil de manejar y leer.

```java
// Ejemplo de constructor telescópico (Inconveniente)
public Casa(int h, int b, boolean p, boolean g, boolean s, boolean t) { ... }
```

El Builder resuelve esto permitiendo que el cliente llame solo a los métodos que necesita para configurar las partes que le interesan del objeto.

## Contexto

Se utiliza cuando:
- El proceso de creación de un objeto complejo debe ser independiente de las partes que lo componen y de cómo se ensamblan.
- El proceso de construcción debe permitir diferentes representaciones del objeto que se construye.
- Se desea evitar constructores con demasiados parámetros (muchos de los cuales podrían ser `null` o valores por defecto).

### Cuando aplica

- **Construcción de documentos:** Generar archivos PDF, HTML o RTF utilizando el mismo proceso de lectura de datos.
- **Configuración de objetos complejos:** Crear una conexión a base de datos o un cliente HTTP con múltiples opciones (timeout, autenticación, proxies, etc.).
- **Ensamblaje de productos:** Construir una "Computadora" configurando el CPU, RAM, Disco, etc., paso a paso.

### Cuando no aplica

- **Objetos simples:** Si el objeto tiene solo dos o tres atributos obligatorios, el Builder añade una verbosidad innecesaria.
- **Objetos inmutables con pocos parámetros:** Un constructor simple es más directo.
- **Cuando no hay variabilidad en la construcción:** Si el objeto siempre se construye de la misma forma, no hay beneficio en usar un Builder.

## Consecuencias de su uso

### Positivas

- **Permite variar la representación interna de un producto:** El Director puede usar diferentes Builders para obtener resultados distintos.
- **Encapsula el código de construcción y representación:** Mejora la modularidad al separar la lógica de ensamblaje de la lógica del objeto final.
- **Control fino sobre el proceso de construcción:** El objeto se construye solo cuando se llama al método final (`build()`), lo que permite validar el estado antes de la instanciación.

### Negativas

- **Requiere un Builder específico para cada producto:** Si los productos son muy diferentes, la jerarquía de Builders puede crecer mucho.
- **Complejidad adicional:** Introduce múltiples clases nuevas (Director, BuilderAbstracto, BuildersConcretos).
- **Mutabilidad temporal:** El objeto Builder es mutable mientras se configura, lo que requiere cuidado en entornos multi-hilo antes de llamar a `build()`.

## Alternativas

- **Abstract Factory:** Se centra en familias de productos. El Builder se centra en la construcción paso a paso de un objeto complejo.
- **Factory Method:** Crea objetos en un solo paso, no permite una configuración gradual.

## Estructura

### Diagramas

**Diagrama de Clases**

```mermaid
classDiagram
    class Director {
        -builder: Builder
        +construir(tipo: String)
    }
    
    class Builder {
        <<interface>>
        +reset()
        +construirParteA()
        +construirParteB()
        +obtenerResultado() Producto
    }
    
    class BuilderConcreto {
        -producto: Producto
        +reset()
        +construirParteA()
        +construirParteB()
        +obtenerResultado() Producto
    }
    
    class Producto {
        +partes: List
    }
    
    Director o--> Builder
    Builder <|.. BuilderConcreto
    BuilderConcreto ..> Producto : crea
```

**Diagrama de Secuencia**

```mermaid
sequenceDiagram
    participant C as Cliente
    participant D as Director
    participant B as BuilderConcreto
    
    C->>B: new BuilderConcreto()
    C->>D: new Director(builder)
    C->>D: construir()
    activate D
    D->>B: reset()
    D->>B: construirParteA()
    D->>B: construirParteB()
    D-->>C: OK
    deactivate D
    C->>B: obtenerResultado()
    activate B
    B-->>C: Producto
    deactivate B
```

## Ejemplos

```java
/**
 * Clase a construir.
 */
public class Casa {
    private int habitaciones;
    private int baños;
    private boolean piscina;
    private boolean garaje;
    
    private Casa(CasaBuilder builder) {
        this.habitaciones = builder.habitaciones;
        this.baños = builder.baños;
        this.piscina = builder.piscina;
        this.garaje = builder.garaje;
    }
    
    public static class CasaBuilder {
        private int habitaciones = 0;
        private int baños = 0;
        private boolean piscina = false;
        private boolean garaje = false;
        
        public CasaBuilder habitaciones(int cantidad) {
            this.habitaciones = cantidad;
            return this;
        }
        
        public CasaBuilder baños(int cantidad) {
            this.baños = cantidad;
            return this;
        }
        
        public CasaBuilder conPiscina() {
            this.piscina = true;
            return this;
        }
        
        public Casa construir() {
            return new Casa(this);
        }
    }
}

// Uso fluido:
Casa miCasa = new Casa.CasaBuilder()
    .habitaciones(3)
    .baños(2)
    .conPiscina()
    .construir();
```

## Ejercicios

```{exercise}
:label: ex-parte4-builder-mini

Diseñá la creación de un `ClienteHttp` que puede llevar `timeout`, autenticación, proxy, compresión y reintentos, pero no siempre usa todas esas opciones. Justificá por qué **Builder** mejora la legibilidad frente a un constructor con muchos parámetros.
```

## Resumen

El Builder es el patrón ideal para "ensamblar" objetos. Su mayor virtud es la legibilidad y la flexibilidad que aporta al cliente, permitiéndole construir objetos complejos sin perderse en una maraña de parámetros de constructor. Es especialmente popular en Java moderno a través de bibliotecas como Lombok o en la construcción de APIs fluidas.

## Próximo paso

Para seguir, conviene pasar a [el material siguiente](prototype.md), donde el recorrido continúa sobre esta base.
