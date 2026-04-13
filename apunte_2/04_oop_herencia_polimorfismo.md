---
title: "4: Herencia y Polimorfismo Conceptual"
subtitle: "Jerarquías, Especialización y Flexibilidad en el Diseño"
subject: Programación Orientada a Objetos
---

(oop3-herencia-polimorfismo)=
# OOP 3: Herencia y Polimorfismo Conceptual

En los capítulos anteriores exploramos los fundamentos de OOP ({ref}`fundamentos-de-la-programacion-orientada-a-objetos`) y las relaciones entre objetos ({ref}`oop2-encapsulamiento-relaciones`). Ahora abordamos dos pilares fundamentales que permiten **reutilizar código** y **diseñar sistemas flexibles**: la **herencia** y el **polimorfismo**.

Este capítulo se enfoca en los **conceptos y el modelado**, no en la sintaxis de implementación. Para la sintaxis específica de Java, consultá {ref}`java-herencia-polimorfismo`.

:::{admonition} Objetivos de Aprendizaje
:class: tip

Al finalizar este capítulo, serás capaz de:

1. Comprender la herencia como mecanismo de especialización
2. Distinguir cuándo usar herencia vs composición
3. Entender el polimorfismo y sus beneficios
4. Aplicar el Principio de Sustitución de Liskov
5. Diseñar jerarquías de clases coherentes
6. Conocer los principios SOLID básicos
:::

---

(herencia-concepto)=
## Herencia: Especialización y Generalización

(que-es-herencia-concepto)=
### ¿Qué es la Herencia?

La **herencia** es un mecanismo que permite crear nuevas clases basadas en clases existentes, heredando sus atributos y comportamiento, y añadiendo o modificando funcionalidad.

:::{admonition} Definición Formal
:class: note

**Herencia** es una relación "es-un" (*is-a*) entre clases donde una clase derivada (subclase) **especializa** una clase base (superclase), heredando su estructura y comportamiento, y potencialmente añadiendo nuevas características o modificando las existentes.
:::

(analogia-herencia)=
### Analogía: La Taxonomía Biológica

La herencia en programación se inspira en la clasificación biológica:

```{mermaid}
classDiagram
    class Animal {
        +respirar()
        +alimentarse()
        +reproducirse()
    }
    
    class Mamifero {
        +amamantarCrias()
        +tenerSangreCaliente()
    }
    
    class Ave {
        +volar()
        +ponerHuevos()
    }
    
    class Reptil {
        +mudarPiel()
        +tenerSangreFria()
    }
    
    class Perro {
        +ladrar()
    }
    
    class Gato {
        +maullar()
    }
    
    class Aguila {
        +planear()
    }
    
    class Pinguino {
        +nadar()
    }
    
    class Serpiente {
        +reptar()
    }
    
    Animal <|-- Mamifero
    Animal <|-- Ave
    Animal <|-- Reptil
    Mamifero <|-- Perro
    Mamifero <|-- Gato
    Ave <|-- Aguila
    Ave <|-- Pinguino
    Reptil <|-- Serpiente
    
    note for Animal "Cada nivel hereda<br>las características del superior"
```

Cada nivel **hereda** las características del nivel superior:

- **Animal**: respira, se alimenta, se reproduce
- **Mamífero** (es un Animal): además, tiene sangre caliente, amamanta crías
- **Perro** (es un Mamífero): además, ladra, tiene cuatro patas

(generalizacion-especializacion)=
### Generalización y Especialización

La herencia puede verse desde dos perspectivas:

**Especialización** (de arriba hacia abajo):
- Partimos de un concepto general
- Creamos versiones más específicas
- Ejemplo: `Vehículo` → `Auto`, `Moto`, `Camión`

**Generalización** (de abajo hacia arriba):
- Identificamos características comunes en clases existentes
- Extraemos una superclase
- Ejemplo: `Perro`, `Gato`, `Caballo` → `Mamífero`

```
┌─────────────────────────────────────────────────────────────┐
│                     GENERALIZACIÓN                          │
│                          ▲                                  │
│                          │                                  │
│    ┌─────────┐    ┌─────────┐    ┌─────────┐               │
│    │ Círculo │    │Rectángulo│   │Triángulo│               │
│    └────┬────┘    └────┬────┘    └────┬────┘               │
│         │              │              │                     │
│         └──────────────┼──────────────┘                     │
│                        │                                    │
│                        ▼                                    │
│                 ┌─────────────┐                             │
│                 │   Figura    │                             │
│                 └─────────────┘                             │
│                        │                                    │
│                        ▼                                    │
│                  ESPECIALIZACIÓN                            │
└─────────────────────────────────────────────────────────────┘
```

(relacion-es-un)=
### La Relación "Es-Un"

La herencia modela una relación **"es-un"** (o "es-un-tipo-de"):

| Afirmación | ¿Válida? | Justificación |
| :--- | :---: | :--- |
| Un Perro **es un** Animal | ✓ | Herencia correcta |
| Un Auto **es un** Vehículo | ✓ | Herencia correcta |
| Un Círculo **es una** Figura | ✓ | Herencia correcta |
| Un Motor **es un** Auto | ✗ | Motor es **parte de** Auto |
| Un Empleado **es una** Empresa | ✗ | Empleado **trabaja para** Empresa |

:::{warning}
**Regla fundamental**: Si la frase "X es un Y" no tiene sentido semántico en el dominio, entonces X **no debe** heredar de Y. Probablemente se trate de otra relación (composición, asociación).
:::

(ejemplo-jerarquia)=
### Ejemplo: Jerarquía de Figuras Geométricas

Modelemos un sistema de figuras geométricas:

```{mermaid}
classDiagram
    class Figura {
        -String color
        -double posicionX
        -double posicionY
        +mover(double x, double y)
        +dibujar()
        +area() double
    }
    
    class Circulo {
        -double radio
        +area() double
        +perimetro() double
    }
    
    class Rectangulo {
        -double ancho
        -double alto
        +area() double
        +perimetro() double
    }
    
    class Triangulo {
        -double base
        -double altura
        +area() double
        +perimetro() double
    }
    
    Figura <|-- Circulo
    Figura <|-- Rectangulo
    Figura <|-- Triangulo
    
    note for Figura "Clase base con<br>características comunes"
    note for Circulo "Especializa el cálculo<br>de área: π * r²"
```

**Análisis:**

- `Figura` es la **superclase** (clase base)
- `Círculo`, `Rectángulo`, `Triángulo` son **subclases** (clases derivadas)
- Las subclases **heredan** `color`, `posicionX`, `posicionY`, `mover()`, `dibujar()`
- Las subclases **añaden** atributos propios (`radio`, `ancho`, etc.)
- Las subclases **especializan** el método `area()` con su propia fórmula

---

(herencia-vs-composicion)=
## Herencia vs Composición

(cuando-usar-herencia)=
### ¿Cuándo Usar Herencia?

La herencia es apropiada cuando:

1. Existe una relación **"es-un"** clara y semánticamente correcta
2. La subclase es una **especialización** de la superclase
3. La subclase puede **sustituir** a la superclase sin problemas
4. Se comparte **comportamiento** significativo (no solo datos)

**Ejemplo correcto:**

```{mermaid}
classDiagram
    class CuentaBancaria {
        -String numero
        -String titular
        -double saldo
        +depositar(double monto)
        +retirar(double monto) boolean
        +consultarSaldo() double
    }
    
    class CuentaAhorro {
        -double tasaInteres
        +aplicarInteres()
        +calcularIntereses() double
    }
    
    class CuentaCorriente {
        -double limiteDescubierto
        +permitirDescubierto(double monto) boolean
        +verificarSobregiro() boolean
    }
    
    CuentaBancaria <|-- CuentaAhorro
    CuentaBancaria <|-- CuentaCorriente
    
    note for CuentaBancaria "Herencia correcta:<br>Comparten comportamiento real"
    note for CuentaAhorro "Es una CuentaBancaria ✓"
    note for CuentaCorriente "Es una CuentaBancaria ✓"
```

- `CuentaAhorro` **es una** `CuentaBancaria` ✓
- `CuentaCorriente` **es una** `CuentaBancaria` ✓
- Comparten comportamiento real (depositar, retirar, consultar)

(cuando-usar-composicion)=
### ¿Cuándo Usar Composición?

La composición es apropiada cuando:

1. Existe una relación **"tiene-un"** o **"usa-un"**
2. Un objeto **contiene** o **utiliza** otro objeto
3. No hay sustitución semántica posible
4. Se quiere **flexibilidad** para cambiar componentes

**Ejemplo incorrecto de herencia (debería ser composición):**

```{mermaid}
classDiagram
    class Motor {
        -int cilindrada
        -String tipo
        +encender()
        +apagar()
    }
    
    class Auto {
        -String marca
        -String modelo
        -Motor motor
        +arrancar()
        +acelerar()
    }
    
    Auto *-- Motor : tiene-un
    
    note for Auto "✓ BIEN: Composición<br>Auto TIENE-UN Motor<br>(no ES-UN Motor)"
```

**Incorrecto:**
```
Auto hereda de Motor ❌ (Un Auto NO ES UN Motor)
```

**Correcto:**
```
Auto tiene-un Motor ✓ (Composición)
```

(favor-composicion)=
### "Favor Composition Over Inheritance"

Este es un principio de diseño ampliamente aceptado:

> **Prefiere la composición sobre la herencia**

**¿Por qué?**

| Aspecto | Herencia | Composición |
| :--- | :--- | :--- |
| Acoplamiento | Fuerte (código entrelazado) | Débil (componentes independientes) |
| Flexibilidad | Fija en tiempo de compilación | Puede cambiar en runtime |
| Reutilización | Solo a través de la jerarquía | De cualquier clase |
| Complejidad | Jerarquías profundas son difíciles | Más modular y simple |
| Encapsulamiento | Se hereda implementación | Solo se usa interfaz pública |

**Ejemplo: El problema del "Cuadrado y Rectángulo"**

Intuitivamente, un Cuadrado "es un" Rectángulo (matemáticamente, un cuadrado es un rectángulo con lados iguales). Pero en código:

```
┌─────────────┐
│ Rectángulo  │
│─────────────│
│ - ancho     │
│ - alto      │
│─────────────│
│ + setAncho()│
│ + setAlto() │
│ + area()    │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  Cuadrado   │  ← ¿Cómo maneja setAncho() y setAlto()?
└─────────────┘
```

**El problema**: Si `Cuadrado` hereda de `Rectángulo`, ¿qué pasa cuando alguien llama `setAncho(5)` seguido de `setAlto(3)`? Un cuadrado no puede tener ancho ≠ alto.

**Solución con composición**:

```
┌─────────────┐      ┌─────────────┐
│  Cuadrado   │      │ Rectángulo  │
│─────────────│      │─────────────│
│ - lado      │      │ - ancho     │
│─────────────│      │ - alto      │
│ + setLado() │      │─────────────│
│ + area()    │      │ + area()    │
└─────────────┘      └─────────────┘
       │                    │
       └────────┬───────────┘
                │
                ▼
         ┌─────────────┐
         │   Figura    │  ← Interface común
         │─────────────│
         │ + area()    │
         └─────────────┘
```

Ambos implementan `Figura`, pero no hay herencia entre ellos.

---

(polimorfismo-concepto)=
## Polimorfismo: Muchas Formas

(que-es-polimorfismo-concepto)=
### ¿Qué es el Polimorfismo?

**Polimorfismo** (del griego: "muchas formas") es la capacidad de tratar objetos de diferentes tipos de manera uniforme a través de una interfaz común.

:::{admonition} Definición Formal
:class: note

**Polimorfismo** es el principio por el cual una referencia de un tipo base puede referirse a objetos de cualquier tipo derivado, y las operaciones sobre esa referencia invocarán el comportamiento específico del tipo real del objeto.
:::

(analogia-polimorfismo)=
### Analogía: El Control Universal

Imaginá un control remoto universal:

- Tiene un botón "Play" ▶️
- Funciona con TV, DVD, Streaming, Radio
- Cada dispositivo **interpreta** "Play" de manera diferente
- El usuario no necesita saber los detalles internos

```
                    ┌──────────────┐
                    │   Control    │
                    │   (Play ▶️)   │
                    └───────┬──────┘
                            │
       ┌────────────────────┼────────────────────┐
       │                    │                    │
       ▼                    ▼                    ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│     TV      │    │    DVD      │    │  Streaming  │
│─────────────│    │─────────────│    │─────────────│
│ Play:       │    │ Play:       │    │ Play:       │
│ Mostrar     │    │ Girar disco │    │ Descargar   │
│ canal       │    │ y reproducir│    │ y reproducir│
└─────────────┘    └─────────────┘    └─────────────┘
```

(beneficios-polimorfismo)=
### Beneficios del Polimorfismo

1. **Código genérico**: Escribís código que funciona con cualquier subtipo
2. **Extensibilidad**: Agregás nuevos tipos sin modificar código existente
3. **Mantenibilidad**: Cambios localizados en cada clase
4. **Abstracción**: Trabajás con conceptos, no con implementaciones

**Ejemplo sin polimorfismo:**

```
// ❌ Código frágil y difícil de extender
void calcularAreaTotal(List<Object> figuras) {
    double total = 0;
    for (Object obj : figuras) {
        if (obj instanceof Circulo) {
            Circulo c = (Circulo) obj;
            total += 3.14159 * c.radio * c.radio;
        } else if (obj instanceof Rectangulo) {
            Rectangulo r = (Rectangulo) obj;
            total += r.ancho * r.alto;
        } else if (obj instanceof Triangulo) {
            Triangulo t = (Triangulo) obj;
            total += (t.base * t.altura) / 2;
        }
        // ¿Y si agrego Pentágono? Debo modificar este método
    }
    return total;
}
```

**Ejemplo con polimorfismo:**

```
// ✓ Código limpio y extensible
double calcularAreaTotal(List<Figura> figuras) {
    double total = 0;
    for (Figura f : figuras) {
        total += f.area();  // Cada figura sabe calcular su área
    }
    return total;
}
// Si agrego Pentágono, solo creo la clase. Este código no cambia.
```

(tipos-polimorfismo)=
### Tipos de Polimorfismo

#### 1. Polimorfismo de Subtipo (Herencia)

El más común: una variable del tipo base puede referenciar cualquier subtipo.

```
Figura figura;

figura = new Circulo(5);
System.out.println(figura.area());  // área del círculo

figura = new Rectangulo(4, 6);
System.out.println(figura.area());  // área del rectángulo
```

#### 2. Polimorfismo de Interfaz

Similar, pero usando interfaces en lugar de clases base.

```
┌─────────────┐
│ «interface» │
│  Ordenable  │
│─────────────│
│ + comparar()│
└──────┬──────┘
       │
  ┌────┴────────────┐
  │                 │
  ▼                 ▼
┌─────────┐   ┌─────────┐
│ Persona │   │Producto │
│(por edad)│   │(por precio)│
└─────────┘   └─────────┘
```

Cualquier clase que implemente `Ordenable` puede ser ordenada, sin importar qué tan diferentes sean.

#### 3. Polimorfismo Paramétrico (Genéricos)

Escribir código que funciona con cualquier tipo (profundizado en {ref}`genericos-java`).

```
// Una lista que funciona con cualquier tipo T
Lista<T>

Lista<String> nombres;
Lista<Integer> numeros;
Lista<Persona> personas;
```

---

(principio-sustitucion-liskov)=
## Principio de Sustitución de Liskov (LSP)

(que-es-lsp)=
### ¿Qué es el LSP?

El **Principio de Sustitución de Liskov** (LSP) es uno de los principios SOLID y establece:

> "Los objetos de una superclase deben poder ser reemplazados por objetos de sus subclases sin alterar la correctitud del programa."

En otras palabras: si `S` es subclase de `T`, entonces cualquier código que funcione con `T` debe funcionar igual de bien con `S`.

:::{admonition} Definición Formal
:class: note

**LSP**: Sea `φ(x)` una propiedad demostrable sobre objetos `x` de tipo `T`. Entonces `φ(y)` debe ser verdadera para objetos `y` de tipo `S` donde `S` es un subtipo de `T`.
:::

(violaciones-lsp)=
### Violaciones del LSP

**Ejemplo 1: El Cuadrado y el Rectángulo (revisitado)**

```
Rectangulo r = obtenerRectangulo();  // Puede ser Cuadrado
r.setAncho(5);
r.setAlto(4);
assert r.area() == 20;  // ¡FALLA si r es Cuadrado!
```

El código espera que `setAncho()` y `setAlto()` sean independientes. `Cuadrado` viola esta expectativa.

**Ejemplo 2: El Ave que no vuela**

```
┌─────────────┐
│     Ave     │
│─────────────│
│ + volar()   │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  Pingüino   │
│─────────────│
│ + volar()   │  ← ¿Qué hace? ¡Los pingüinos no vuelan!
└─────────────┘
```

Opciones problemáticas:
- Lanzar excepción: viola LSP (el código que espera Ave.volar() falla)
- No hacer nada: comportamiento sorpresivo
- Retornar error: cambia la semántica

**Solución: Rediseñar la jerarquía**

```
                    ┌─────────────┐
                    │     Ave     │
                    │─────────────│
                    │ + comer()   │
                    │ + dormir()  │
                    └──────┬──────┘
                           │
         ┌─────────────────┴─────────────────┐
         │                                   │
         ▼                                   ▼
┌─────────────────┐               ┌─────────────────┐
│   AveVoladora   │               │ AveNoVoladora   │
│─────────────────│               │─────────────────│
│ + volar()       │               │ + nadar()       │
└────────┬────────┘               └────────┬────────┘
         │                                 │
    ┌────┴────┐                      ┌─────┴─────┐
    │         │                      │           │
    ▼         ▼                      ▼           ▼
┌───────┐ ┌───────┐              ┌───────┐  ┌───────┐
│Águila │ │Gorrión│              │Pingüino│ │Avestruz│
└───────┘ └───────┘              └───────┘  └───────┘
```

(como-cumplir-lsp)=
### Cómo Cumplir con LSP

**Reglas prácticas:**

1. **Precondiciones**: La subclase no puede exigir más que la superclase
2. **Postcondiciones**: La subclase no puede prometer menos que la superclase
3. **Invariantes**: La subclase debe mantener todas las invariantes de la superclase
4. **Comportamiento**: La subclase debe comportarse como la superclase espera

**Test del "Si funciona con la clase base..."**

Antes de crear una herencia, preguntate:
- ¿Todo el código que funciona con la superclase funcionará con la subclase?
- ¿La subclase puede hacer todo lo que la superclase promete?
- ¿La subclase respeta las expectativas de los clientes de la superclase?

---

(clases-abstractas-interfaces)=
## Clases Abstractas e Interfaces

(conceptos-abstraccion)=
### Abstracción en el Diseño

A veces queremos definir un concepto que **no tiene sentido instanciar directamente**, pero que sirve como base para otras clases.

**Ejemplo: ¿Qué es una "Figura" sin forma específica?**

```
Figura f = new Figura();  // ¿Qué forma tiene? ¿Cuál es su área?
```

No tiene sentido crear una "Figura genérica". Lo que queremos es definir el **concepto** de Figura para que Círculo, Rectángulo, etc. lo especialicen.

(clase-abstracta-concepto)=
### Clases Abstractas

Una **clase abstracta** es una clase que:
- No puede ser instanciada directamente
- Puede tener métodos abstractos (sin implementación)
- Puede tener métodos concretos (con implementación)
- Sirve como plantilla para subclases

```
┌─────────────────────┐
│    «abstract»       │
│      Figura         │
│─────────────────────│
│ - color             │
│ - posicion          │
│─────────────────────│
│ + mover()           │  ← Método concreto (tiene implementación)
│ + area() {abstract} │  ← Método abstracto (sin implementación)
└──────────┬──────────┘
           │
           ▼
    ┌─────────────┐
    │   Circulo   │
    │─────────────│
    │ + area()    │  ← DEBE implementar area()
    └─────────────┘
```

(interface-concepto)=
### Interfaces

Una **interface** define un **contrato**: un conjunto de métodos que una clase debe implementar, sin especificar cómo.

```
┌─────────────────────┐
│    «interface»      │
│     Dibujable       │
│─────────────────────│
│ + dibujar()         │
│ + ocultar()         │
└─────────────────────┘
         △
         │
         │ implementa
         │
┌────────┴────────┐
│                 │
▼                 ▼
┌─────────┐    ┌─────────┐
│ Circulo │    │ Boton   │
└─────────┘    └─────────┘
```

Tanto `Circulo` (una figura) como `Boton` (un componente de UI) pueden ser `Dibujable`, aunque no comparten ninguna otra característica.

(abstracta-vs-interface-concepto)=
### ¿Cuándo Usar Cada Una?

| Usar Clase Abstracta cuando... | Usar Interface cuando... |
| :--- | :--- |
| Hay código que compartir | Solo hay contrato (firma de métodos) |
| Existe una relación "es-un" | Existe una capacidad "puede-hacer" |
| Las subclases están relacionadas | Las clases no están relacionadas |
| Querés definir estado común | No hay estado compartido |

**Ejemplo combinado:**

```
┌─────────────────────┐
│    «interface»      │
│    Reproducible     │
│─────────────────────│
│ + play()            │
│ + pause()           │
│ + stop()            │
└─────────────────────┘
         △
         │
┌────────┴─────────────────────────────┐
│                                      │
▼                                      ▼
┌─────────────────────┐    ┌─────────────────────┐
│     «abstract»      │    │        Radio        │
│      Multimedia     │    │─────────────────────│
│─────────────────────│    │ + play()            │
│ - archivo           │    │ + pause()           │
│ - duracion          │    │ + stop()            │
│─────────────────────│    │ + sintonizar()      │
│ + play()            │    └─────────────────────┘
│ + pause()           │
│ + stop()            │
└──────────┬──────────┘
           │
      ┌────┴────┐
      │         │
      ▼         ▼
┌─────────┐  ┌─────────┐
│  Audio  │  │  Video  │
└─────────┘  └─────────┘
```

- `Reproducible`: Interface que define el contrato
- `Multimedia`: Clase abstracta que implementa el contrato y agrega estado/comportamiento común
- `Audio`, `Video`: Clases concretas que especializan `Multimedia`
- `Radio`: Clase que implementa `Reproducible` sin heredar de `Multimedia`

---

(principios-solid-introduccion)=
## Introducción a los Principios SOLID

Los principios SOLID son cinco principios de diseño orientado a objetos que promueven código mantenible, extensible y robusto.

| Letra | Principio | Resumen |
| :---: | :--- | :--- |
| **S** | Single Responsibility | Una clase, una responsabilidad |
| **O** | Open/Closed | Abierto a extensión, cerrado a modificación |
| **L** | Liskov Substitution | Las subclases deben ser sustituibles |
| **I** | Interface Segregation | Interfaces pequeñas y específicas |
| **D** | Dependency Inversion | Depender de abstracciones, no de concreciones |

(srp-concepto)=
### S - Principio de Responsabilidad Única (SRP)

> "Una clase debe tener una, y solo una, razón para cambiar."

**Ejemplo de violación:**

```
┌─────────────────────────┐
│        Empleado         │  ← ¡Demasiadas responsabilidades!
│─────────────────────────│
│ + calcularSalario()     │  ← Reglas de negocio de RRHH
│ + guardarEnBD()         │  ← Persistencia
│ + generarReporte()      │  ← Presentación
│ + enviarEmail()         │  ← Comunicación
└─────────────────────────┘
```

**Diseño correcto:**

```
┌───────────────┐    ┌───────────────┐    ┌───────────────┐
│   Empleado    │    │RepositorioEmp│    │ ReporteEmp    │
│───────────────│    │───────────────│    │───────────────│
│ + calcularSalario()│ + guardar()   │    │ + generar()   │
└───────────────┘    │ + buscar()    │    └───────────────┘
                     └───────────────┘
```

(ocp-concepto)=
### O - Principio Abierto/Cerrado (OCP)

> "Las entidades de software deben estar abiertas a extensión pero cerradas a modificación."

**Ejemplo de violación:**

```
// Cada vez que agrego una figura, modifico este método
double calcularArea(Figura f) {
    if (f.tipo == "circulo") {
        return 3.14 * f.radio * f.radio;
    } else if (f.tipo == "rectangulo") {
        return f.ancho * f.alto;
    }
    // Si agrego triángulo, debo modificar este código
}
```

**Diseño correcto (usando polimorfismo):**

```
// Nunca modifico este código, solo agrego nuevas clases
double calcularArea(Figura f) {
    return f.area();  // Cada figura sabe su área
}

// Para agregar triángulo: creo clase Triangulo con area()
// El código anterior no cambia
```

(lsp-recordatorio)=
### L - Principio de Sustitución de Liskov (LSP)

Ya lo vimos en detalle: las subclases deben poder reemplazar a la superclase sin afectar el funcionamiento.

(isp-concepto)=
### I - Principio de Segregación de Interfaces (ISP)

> "Los clientes no deben depender de interfaces que no usan."

**Ejemplo de violación:**

```
┌─────────────────────────┐
│    «interface»          │
│       Trabajador        │
│─────────────────────────│
│ + trabajar()            │
│ + comer()               │
│ + dormir()              │
│ + programar()           │  ← No todos los trabajadores programan
│ + conducir()            │  ← No todos los trabajadores conducen
└─────────────────────────┘
```

**Diseño correcto:**

```
┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│ «interface» │  │ «interface» │  │ «interface» │
│ Trabajador  │  │ Programador │  │  Conductor  │
│─────────────│  │─────────────│  │─────────────│
│ + trabajar()│  │ + programar()│ │ + conducir()│
└─────────────┘  └─────────────┘  └─────────────┘
       △               △               △
       │               │               │
       └───────┬───────┴───────┬───────┘
               │               │
               ▼               ▼
        ┌───────────┐   ┌───────────┐
        │Desarrollador│  │ Chofer   │
        │(implementa  │  │(implementa│
        │ ambas)      │  │ Conductor)│
        └───────────┘   └───────────┘
```

(dip-concepto)=
### D - Principio de Inversión de Dependencias (DIP)

> "Los módulos de alto nivel no deben depender de módulos de bajo nivel. Ambos deben depender de abstracciones."

**Ejemplo de violación:**

```
┌───────────────┐
│   Servicio    │
│───────────────│
│ - mysql: MySQL│  ← Depende directamente de MySQL
│───────────────│
│ + guardar()   │
└───────────────┘
        │
        │ depende de
        ▼
┌───────────────┐
│     MySQL     │  ← Si cambio a PostgreSQL, debo modificar Servicio
└───────────────┘
```

**Diseño correcto:**

```
┌───────────────┐
│   Servicio    │
│───────────────│
│ - db: BaseDatos│  ← Depende de abstracción
│───────────────│
│ + guardar()   │
└───────────────┘
        │
        │ depende de
        ▼
┌───────────────────┐
│   «interface»     │
│    BaseDatos      │
│───────────────────│
│ + conectar()      │
│ + ejecutar()      │
└─────────┬─────────┘
          │
     ┌────┴────┐
     │         │
     ▼         ▼
┌─────────┐ ┌───────────┐
│  MySQL  │ │ PostgreSQL│  ← Puedo cambiar sin tocar Servicio
└─────────┘ └───────────┘
```

---

(diseno-jerarquias)=
## Diseño de Jerarquías de Clases

(guias-diseno)=
### Guías para Diseñar Jerarquías

1. **Empezá simple**: No crees jerarquías antes de necesitarlas
2. **Máximo 3-4 niveles**: Jerarquías profundas son difíciles de entender
3. **Verificá el LSP**: Cada subclase debe ser sustituible
4. **Preferí composición**: Solo usá herencia cuando sea claramente apropiada
5. **Interfaces sobre clases abstractas**: Cuando solo necesitás contrato

(errores-comunes)=
### Errores Comunes

**Error 1: Herencia para reutilizar código**

```
// ❌ MAL: Stack hereda de ArrayList solo por reutilizar código
Stack extends ArrayList  // Un Stack NO ES una ArrayList

// ✓ BIEN: Stack USA una ArrayList internamente
Stack {
    private ArrayList<T> elementos;
}
```

**Error 2: Jerarquías demasiado profundas**

```
// ❌ MAL: Demasiados niveles
SerVivo → Animal → Vertebrado → Mamifero → Carnivoro → Canino → Perro → Pastor → PastorAleman

// ✓ BIEN: Más plano, usar interfaces para capacidades
Animal → Perro
Perro implementa: Mamifero, Carnivoro, Domesticable
```

**Error 3: Herencia para modelar estados**

```
// ❌ MAL: Estados como clases
Usuario → UsuarioActivo
       → UsuarioInactivo
       → UsuarioBloqueado

// ✓ BIEN: Estado como atributo
Usuario {
    private Estado estado;  // ACTIVO, INACTIVO, BLOQUEADO
}
```

---

(ejemplo-completo-oop3)=
## Ejemplo Completo: Sistema de Medios de Pago

Diseñemos un sistema que maneje diferentes medios de pago:

### Análisis del Dominio

**Medios de pago identificados:**
- Tarjeta de crédito
- Tarjeta de débito
- Transferencia bancaria
- Efectivo
- Billetera virtual (MercadoPago, PayPal)

### Diseño con Herencia y Polimorfismo

```
                    ┌───────────────────────┐
                    │     «interface»       │
                    │      MedioPago        │
                    │───────────────────────│
                    │ + procesarPago(monto) │
                    │ + verificarFondos()   │
                    │ + obtenerRecibo()     │
                    └───────────┬───────────┘
                                │
        ┌───────────────────────┼───────────────────────┐
        │                       │                       │
        ▼                       ▼                       ▼
┌───────────────┐     ┌─────────────────┐     ┌───────────────┐
│   «abstract»  │     │    Efectivo     │     │ «abstract»    │
│    Tarjeta    │     │─────────────────│     │  Billetera    │
│───────────────│     │ + procesarPago()│     │───────────────│
│ - numero      │     │ + verificar...()│     │ - email       │
│ - titular     │     │ + obtenerRecibo()│    │ - saldo       │
│ - vencimiento │     └─────────────────┘     │───────────────│
│───────────────│                             │ + recargar()  │
│ + validar()   │                             └───────┬───────┘
└───────┬───────┘                                     │
        │                                    ┌────────┴────────┐
   ┌────┴────┐                               │                 │
   │         │                               ▼                 ▼
   ▼         ▼                      ┌─────────────┐  ┌─────────────┐
┌─────────┐ ┌─────────┐             │ MercadoPago │  │   PayPal    │
│ Credito │ │ Debito  │             └─────────────┘  └─────────────┘
│─────────│ │─────────│
│ - limite│ │ - cuenta│
│─────────│ │─────────│
│ + procesarPago()│ │ + procesarPago()│
└─────────┘ └─────────┘
```

### Aplicación de Principios

**SRP**: Cada clase tiene una responsabilidad clara
- `Tarjeta`: Validar datos de tarjeta
- `Credito`: Lógica de crédito (límites, cuotas)
- `Debito`: Lógica de débito (saldo en cuenta)

**OCP**: Agregar nuevo medio de pago no requiere modificar código existente
- Creo `Criptomoneda implements MedioPago`
- El sistema que usa `MedioPago` funciona automáticamente

**LSP**: Cualquier `MedioPago` puede ser usado donde se espera un `MedioPago`

**ISP**: Interface `MedioPago` es pequeña y enfocada

**DIP**: El sistema depende de `MedioPago` (abstracción), no de `TarjetaCredito` (concreción)

---

(resumen-oop3)=
## Resumen

### Herencia

- Modela relación "es-un"
- Permite especialización y reutilización
- Usar con moderación (preferir composición)

### Polimorfismo

- "Muchas formas" para un mismo comportamiento
- Permite código genérico y extensible
- Base para diseños flexibles

### Principio de Sustitución de Liskov

- Subclases deben ser sustituibles por superclases
- Verificar precondiciones, postcondiciones, invariantes
- Rediseñar si hay violaciones

### Clases Abstractas e Interfaces

- Abstracta: plantilla con implementación parcial
- Interface: contrato puro
- Combinar según necesidad

### Principios SOLID

- **S**ingle Responsibility: Una razón para cambiar (ver {ref}`s-principio-de-responsabilidad-unica`)
- **O**pen/Closed: Extensible, no modificable (ver {ref}`o-principio-abierto-cerrado`)
- **L**iskov Substitution: Subclases sustituibles (ver {ref}`l-principio-de-sustitucion-de-liskov`)
- **I**nterface Segregation: Interfaces pequeñas (ver {ref}`i-principio-de-segregacion-de-interfaces`)
- **D**ependency Inversion: Depender de abstracciones (ver {ref}`d-principio-de-inversion-de-dependencias`)

---

(ejercicios-oop3)=
## Ejercicios

```{exercise}
:label: ej-jerarquia-empleados
Diseñá una jerarquía de clases para empleados de una empresa:
- Empleado base con nombre, legajo, salario base
- Tipos: Desarrollador, Gerente, Vendedor
- Cada tipo tiene reglas diferentes para calcular salario
- Verificá que cumple con LSP
- Dibujá el diagrama de clases
```

```{exercise}
:label: ej-identificar-violaciones
Analizá las siguientes jerarquías e identificá violaciones a LSP:

a) `Pez extends Animal` con método `respirarBajoeAgua()`
   `Ballena extends Pez`

b) `ColeccionModificable extends Coleccion` con `agregar()` y `eliminar()`
   `ColeccionInmutable extends ColeccionModificable`

c) `Empleado` con `trabajar()` que lanza excepción si está de vacaciones
   `EmpleadoTemporal extends Empleado`

Proponé soluciones.
```

```{exercise}
:label: ej-solid-refactoring
Refactorizá la siguiente clase para que cumpla con los principios SOLID:

```
class GestorPedidos {
    void crearPedido(datos) { ... }
    void calcularTotal(pedido) { ... }
    void guardarEnBaseDatos(pedido) { ... }
    void enviarEmailConfirmacion(pedido) { ... }
    void generarFacturaPDF(pedido) { ... }
    void imprimirFactura(pedido) { ... }
}
```
```

```{exercise}
:label: ej-composicion-vs-herencia
Para cada caso, decidí si usar herencia o composición y justificá:

a) Motor y Auto
b) CuentaCorriente y CuentaBancaria
c) Alarma y Reloj
d) Estudiante y Persona
e) Rueda y Bicicleta
f) Stack y ArrayList
```

```{exercise}
:label: ej-diseno-sistema-notificaciones
Diseñá un sistema de notificaciones:
- Tipos: Email, SMS, Push, WhatsApp
- Cada tipo tiene diferentes datos y formas de envío
- Debe ser fácil agregar nuevos tipos
- Aplicá los principios vistos
- Dibujá el diagrama de clases completo
```
