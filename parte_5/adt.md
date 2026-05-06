---
title: "Tipos Abstractos de Datos"
subtitle: "Separar interfaz, contrato e implementación"
subject: Estructuras de Datos
description: Qué es un TAD, cómo se relaciona con la representación y por qué conviene distinguir contrato de implementación.
---

(parte5-tipos-abstractos-datos)=
# Tipos Abstractos de Datos

Esta página abre la parte con el lenguaje más general. Antes de discutir arreglos, árboles o grafos, conviene fijar qué se considera un tipo abstracto de datos y por qué la representación no debería confundirse con la interfaz.

Si se viene de C, la intuición inicial suele ser pensar primero en la estructura concreta: `struct`, campos, punteros, arreglos, nodos. En esta parte conviene invertir ese orden. Primero importa **qué problema modela la estructura** y **qué operaciones promete**. Recién después conviene decidir cómo se la representa en memoria.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender qué gana el diseño cuando se separa problema abstracto, contrato de operaciones e implementación concreta.

**Prerrequisitos.** Conviene llegar con frescos los capítulos de encapsulamiento, contratos e invariantes vistos en partes anteriores.

**Desarrollo.** El capítulo parte de la idea de interfaz observable, sigue con contrato e invariantes, y cierra comparando varias implementaciones del mismo TAD sin cambiar el punto de vista del cliente.
:::

## Del problema a la interfaz

Un **Tipo Abstracto de Datos** (TAD) es un modelo matemático o lógico que describe:

1. un conjunto de valores posibles,
2. un conjunto de operaciones válidas sobre esos valores,
3. las reglas o axiomas que esas operaciones deben respetar.

Lo que **no** define es una única representación interna obligatoria en la memoria de la computadora. 

Para entender esta diferencia, conviene pensar en una **máquina expendedora**. Como cliente, conocés su interfaz y su comportamiento observable: insertás dinero, presionás un botón con un código (B4) y obtenés un producto. Ese es el TAD. No necesitás saber si adentro hay un sistema de espirales metálicas, un brazo robótico o rieles inclinados. Esa mecánica interna es la **estructura de datos** concreta.

Una pila en programación puede modelarse de la misma manera:

- tiene un estado interno con cero o más elementos,
- permite apilar, desapilar y consultar el tope,
- respeta una política LIFO (Last In, First Out): lo último que entra es lo primero que sale.

Nada de ese modelo abstracto obliga todavía a usar:

- un arreglo de memoria contigua,
- una lista de nodos enlazados por punteros,
- o cualquier otra representación física.

:::{important}
La idea central es esta: **el TAD describe el "qué" (el comportamiento observable)**. La estructura de datos concreta describe el **"cómo" (la disposición en memoria y los algoritmos específicos)** para lograr ese comportamiento.
:::

## Interfaz, representación y cliente

Conviene distinguir tres planos y cómo se relacionan a través de una barrera de abstracción:

```{mermaid}
flowchart LR
    C[Código Cliente] -->|usa operaciones| I((Interfaz / TAD))
    
    subgraph Barrera de Abstracción
        I
    end
    
    I -.->|implementa| R[Representación Concreta]
    R --> D[(Memoria: arreglos, nodos, etc)]
    
    classDef client fill:#d4edda,stroke:#28a745,stroke-width:2px;
    classDef interface fill:#fff3cd,stroke:#ffc107,stroke-width:2px;
    classDef rep fill:#f8d7da,stroke:#dc3545,stroke-width:2px;
    
    class C client;
    class I interface;
    class R,D rep;
```

| Plano | Pregunta central | Quién debería conocerlo |
| :--- | :--- | :--- |
| TAD (Interfaz) | ¿Qué operaciones existen y qué prometen? | Cliente e implementación |
| Representación | ¿Cómo se guardan los datos? | Solo la implementación |
| Uso cliente | ¿Qué necesita hacer el programa con esa estructura? | Código cliente |

En Java, esa separación suele expresarse con:

- una interfaz pública o un conjunto claro de métodos públicos,
- una clase concreta con atributos privados,
- y código cliente que trabaja contra el contrato, no contra detalles internos.

```{code} java
:caption: Misma interfaz, distintas implementaciones, mismo contrato

/**
 * TAD Pila (Stack).
 * @param <T> el tipo de elementos en la pila.
 */
public interface Pila<T> {
    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a apilar.
     */
    void apilar(T elemento);

    /**
     * Quita y devuelve el elemento en el tope de la pila.
     * @return el elemento desapilado.
     * @throws IllegalStateException si la pila está vacía.
     */
    T desapilar();

    /**
     * Devuelve el elemento en el tope de la pila sin quitarlo.
     * @return el elemento en el tope.
     * @throws IllegalStateException si la pila está vacía.
     */
    T verTope();

    /**
     * @return true si la pila no contiene elementos, false en caso contrario.
     */
    boolean estaVacia();
}

public class PilaArreglo<T> implements Pila<T> {
    // detalles internos ocultos protegidos por el encapsulamiento
}

public class PilaEnlazada<T> implements Pila<T> {
    // detalles internos ocultos protegidos por el encapsulamiento
}
```

Desde el punto de vista del cliente, ambas clases son válidas si respetan el mismo contrato.

## El contrato de un TAD

Definir un TAD no es solo listar nombres de métodos. Como se vio en {ref}`oop-contratos`, hace falta definir formalmente las obligaciones y garantías:

- **qué hace** cada operación (Postcondiciones),
- **cuándo puede usarse** (Precondiciones),
- **qué devuelve o qué modifica** (Efectos colaterales),
- **qué errores o casos borde existen** (Excepciones lanzadas si el cliente viola el contrato).

Por ejemplo, para la pila anterior:

| Operación | Postcondición (Garantía) | Precondición (Obligación del cliente) | Si se viola la precondición |
| :--- | :--- | :--- | :--- |
| `apilar(x)` | agrega `x` al tope, la pila ya no está vacía | ninguna (salvo límites físicos de memoria) | N/A |
| `desapilar()` | quita y devuelve el tope | la pila no está vacía (`!estaVacia()`) | lanza `IllegalStateException` |
| `verTope()` | devuelve el tope sin modificar la pila | la pila no está vacía (`!estaVacia()`) | lanza `IllegalStateException` |
| `estaVacia()` | informa si hay elementos | ninguna | N/A |

Ese contrato ya alcanza para testear comportamiento (ver {ref}`oop-testing`), incluso si no se sabe si la pila usa:

- un arreglo redimensionable,
- una lista enlazada,
- o una estructura híbrida.

## Invariantes y representación

El contrato visible no alcanza para implementar bien una estructura. También hace falta sostener reglas internas que el cliente no ve, pero la implementación sí debe respetar rigurosamente. Esas reglas son las **invariantes de representación** (ver {ref}`oop-contratos`).

Ejemplo: una cola implementada con arreglo circular podría exigir:

- que `0 <= cantidad <= capacidad`,
- que `frente` siempre apunte al primer elemento lógico si la cola no está vacía,
- que `fondo` se actualice usando aritmética modular,
- que no existan “agujeros” lógicos entre frente y fondo.

El cliente no necesita conocer esos detalles. Sí necesita que las operaciones se comporten como cola FIFO.

### El peligro de romper invariantes

Si una implementación no encapsula correctamente su estado interno, las invariantes pueden romperse. Imaginá una pila basada en un arreglo donde se expone (por error de diseño) el índice `tope`:

```{code} java
:caption: Diseño frágil que expone la representación

public class PilaRota<T> {
    public Object[] elementos = new Object[10];
    public int tope = -1; // ❌ ESTO ES UN PELIGRO
    
    public void apilar(T elemento) {
        tope++;
        elementos[tope] = elemento;
    }
}
```

Si el código cliente hace `pila.tope = 5;` sin haber apilado elementos reales, **la invariante se rompe**. A partir de ese momento, la estructura de datos es inconsistente y cualquier llamada a `desapilar()` probablemente lance excepciones internas no esperadas (como `NullPointerException`) o, peor aún, devuelva datos basura silenciosamente, incumpliendo el contrato del TAD.

:::{note}
Una buena implementación protege sus invariantes usando encapsulamiento (`private`) y evita que el cliente pueda romperlas desde afuera. Por eso en esta materia no alcanza con “tener getters y setters para todo”: el punto es diseñar operaciones de dominio (como `apilar` y `desapilar`) que cuiden el contrato y la coherencia de la memoria.
:::

## Un mismo TAD, varias implementaciones

La separación entre TAD e implementación permite comparar alternativas de forma limpia.

### Ejemplo: pila con arreglo vs pila enlazada

| Aspecto | Pila con arreglo | Pila enlazada |
| :--- | :--- | :--- |
| Acceso al tope | directo | directo |
| `apilar` / `desapilar` | eficiente en el extremo | eficiente en el extremo |
| Crecimiento | requiere redimensionar si se llena | crece nodo a nodo |
| Memoria | contigua, más compacta | más overhead por nodo |
| Localidad de referencia | mejor | peor |
| Capacidad fija | posible | no natural |

Las dos implementaciones sirven para el mismo TAD. Lo que cambia no es la definición del problema, sino:

- el costo de ciertas operaciones,
- el uso de memoria,
- la complejidad de implementación,
- y la conveniencia según el contexto.

## Por qué esta separación mejora el diseño

Separar TAD de implementación da varias ventajas:

1. **Permite reemplazar estructuras** sin reescribir todo el cliente.
2. **Obliga a pensar operaciones** antes que campos.
3. **Facilita testear comportamiento** en lugar de detalles internos.
4. **Hace comparables varias implementaciones** del mismo problema.
5. **Reduce acoplamiento** entre el código que usa una estructura y el código que la implementa.

En términos de cursada, esto tiene una consecuencia importante: cuando se compare una estructura con otra en esta parte, no conviene mezclar dos preguntas distintas:

- “¿Qué TAD conviene para este problema?”
- “¿Qué implementación conviene para este TAD?”

No siempre tienen la misma respuesta.

## TAD, clases e interfaces

En Java, un TAD no coincide automáticamente con una `interface`, pero esa suele ser una buena forma de expresarlo.

Conviene pensar así:

- el **TAD** es la idea abstracta,
- la **interfaz** o la API pública es su traducción al código,
- la **clase concreta** resuelve la representación,
- y los **tests** deberían verificar el contrato observable.

```{code} java
:caption: El cliente depende del contrato, no de la representación

public class Navegador {
    private final Pila<String> historial;

    public Navegador(Pila<String> historial) {
        this.historial = historial;
    }

    public void visitar(String url) {
        this.historial.apilar(url);
    }

    public String volver() {
        return this.historial.desapilar();
    }
}
```

`Navegador` no necesita saber si `historial` usa nodos o arreglo. Necesita solamente que el comportamiento de pila sea correcto.

## Qué errores conviene evitar

En esta parte conviene vigilar varios errores frecuentes:

1. **Confundir el TAD con la representación.** “Una pila es un arreglo” es falso; una pila puede implementarse con arreglo.
2. **Diseñar desde campos en lugar de operaciones.** Eso suele producir APIs pobres y muy acopladas.
3. **Exponer detalles internos.** Si el cliente depende del `tope`, del arreglo interno o de nodos concretos, el reemplazo de implementación se vuelve caro.
4. **Comparar implementaciones sin fijar el contrato.** Si no está claro qué operaciones importan, la comparación queda vacía.

## Resumen

Un TAD nombra un problema y una interfaz, no una estructura física única. Esa diferencia permite:

- discutir comportamiento sin casarse con una representación,
- comparar varias implementaciones del mismo TAD,
- proteger invariantes internos,
- y diseñar código cliente menos acoplado.

En esta parte, cada familia de estructuras conviene leer así: primero el problema abstracto, después las variantes concretas que lo resuelven.

## Ejercicios

```{exercise}
:label: ex-parte5-adt-mini

Tomá una estructura conocida, por ejemplo una pila o una cola, y describí:

1. qué parte corresponde al TAD,
2. qué parte corresponde a una implementación concreta,
3. y qué cambiaría si se reemplaza un arreglo por nodos.
```

```{exercise}
:label: ex-parte5-adt-cliente

Diseñá una API mínima para un TAD `Cola<T>` sin decidir todavía cómo se implementa. Después listá dos implementaciones posibles y explicá qué parte del código cliente debería poder permanecer igual.
```

## Próximo paso

Para seguir, conviene pasar a [Análisis de algoritmos](algoritmos.md), donde aparece el criterio de comparación que después se reutiliza en toda la parte.
