---
title: "Fundamentos de secuencias"
subtitle: "Colecciones ordenadas y acceso posicional"
subject: Estructuras de Datos
description: Qué define el TAD secuencia, qué operaciones forman parte de su contrato y cómo cambian sus costos según la representación.
---

(parte6-fundamentos-secuencias)=
# Fundamentos de secuencias

Las secuencias aparecen como la familia lineal más general de la parte. Antes de imponer restricciones como pila o cola, conviene fijar qué significa mantener elementos en cierto orden y qué operaciones suelen pedirse sobre esa organización.

Una secuencia no queda definida por la estructura física que usa, sino por el **contrato observable** que ofrece: hay elementos, hay un orden entre ellos y existen operaciones para consultar, insertar, reemplazar, recorrer o eliminar según una posición o un criterio de acceso.

Ese punto importa porque en esta familia el error más común es confundir el TAD con una de sus implementaciones. Una secuencia puede sostenerse con memoria contigua, con nodos enlazados o con variantes híbridas, y cada elección cambia costo, memoria e invariantes sin cambiar necesariamente el problema abstracto que se quiere modelar.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender la secuencia como TAD general y distinguir acceso, recorrido e inserción según la representación elegida.

**Prerrequisitos.** Conviene haber leído [Análisis de algoritmos](../algoritmos.md), porque este capítulo ya empieza a comparar costos de operación.

**Desarrollo.** El capítulo define qué hace abstracta a una secuencia, qué operaciones forman parte de su contrato, cómo cambian recorrido e inserción según la representación y por qué pilas, colas o deques pueden verse como restricciones del mismo problema lineal.
:::

:::{tip} Idea guía
Una secuencia sirve cuando no alcanza con saber **qué** elementos hay: también hace falta saber **en qué orden** están y cómo se los va a recorrer o modificar.
:::

## Qué hace abstracta a una secuencia

La palabra **secuencia** no nombra una clase concreta de Java ni una única técnica de implementación. Nombra una familia de problemas donde el orden entre elementos forma parte del comportamiento observable.

Por eso, dos implementaciones distintas pueden representar el mismo TAD secuencia si ambas respetan las mismas operaciones y el mismo significado del orden.

En una secuencia importan, como mínimo, estas ideas:

1. **orden lógico**: el primero, el segundo, el último y los elementos intermedios no son intercambiables sin cambiar el significado;
2. **posición**: se puede hablar de un elemento por su lugar dentro del recorrido;
3. **recorrido**: existe una manera natural de visitar los elementos respetando ese orden;
4. **actualización estructural**: insertar o borrar modifica no solo el conjunto de elementos, sino también su distribución relativa.

Eso distingue a una secuencia de otros TAD:

| TAD | Qué privilegia | Qué no pone en primer plano |
| :--- | :--- | :--- |
| Secuencia | Orden y posición | Unicidad o acceso por clave |
| Conjunto | Pertenencia | Orden relativo |
| Diccionario | Asociación clave-valor | Recorrido posicional |
| Árbol | Jerarquía | Linealidad estricta |

En otras palabras: una secuencia no pregunta primero “¿está el elemento?”, sino “¿dónde está, cómo se recorre y cuánto cuesta moverlo?”.

```{mermaid}
flowchart LR
    subgraph Secuencia
        direction LR
        S1[A] --> S2[B] --> S3[C] --> S4[A]
    end
    
    subgraph Conjunto
        direction LR
        C1((A)) ~~~ C2((B)) ~~~ C3((C))
    end
    
    subgraph Diccionario
        direction LR
        D1[Clave 1] -.-> V1(A)
        D2[Clave 2] -.-> V2(B)
    end
    
    classDef seq fill:#e3f2fd,stroke:#0277bd,stroke-width:2px;
    classDef set fill:#f1f8e9,stroke:#33691e,stroke-width:2px;
    classDef map fill:#fff3e0,stroke:#e65100,stroke-width:2px;
    
    class S1,S2,S3,S4 seq;
    class C1,C2,C3 set;
    class D1,V1,D2,V2 map;
```

## Operaciones y contrato observable

Si una secuencia se trata como TAD, lo que el cliente debería ver no es el arreglo interno ni los nodos, sino un **conjunto de operaciones con significado claro**.

Un contrato mínimo razonable para esta familia suele incluir:

| Operación | Qué permite observar o hacer | Qué debería quedar claro |
| :--- | :--- | :--- |
| `size()` | Saber cuántos elementos hay | Si el costo es constante o no |
| `isEmpty()` | Saber si no hay elementos | Que no depende del tipo concreto |
| `get(i)` | Consultar el elemento en cierta posición | Qué rango de índices es válido |
| `set(i, e)` | Reemplazar el elemento en una posición | Si modifica tamaño o solo contenido |
| `insert(i, e)` | Insertar preservando el orden | Qué pasa con los elementos siguientes |
| `remove(i)` | Eliminar el elemento de una posición | Cómo se recompone la secuencia |
| `append(e)` | Agregar al final | Si es una operación privilegiada |
| iteración | Recorrer de principio a fin | Qué orden garantiza |

Un ejemplo posible de interfaz es este:

```java
/**
 * TAD Secuencia (Lista abstracta).
 * @param <T> el tipo de elementos en la secuencia.
 */
public interface Secuencia<T> {
    /** @return la cantidad de elementos, siempre >= 0. */
    int size();
    
    boolean isEmpty();
    
    /**
     * @param indice posición a consultar.
     * @return el elemento en la posición dada.
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= size().
     */
    T get(int indice);
    
    /**
     * Reemplaza el elemento en la posición, sin cambiar el tamaño.
     * @throws IndexOutOfBoundsException si indice es inválido.
     */
    void set(int indice, T elemento);
    
    /**
     * Inserta un elemento desplazando los siguientes a la derecha.
     * Postcondición: size() aumenta en 1.
     * @throws IndexOutOfBoundsException si indice < 0 o indice > size().
     */
    void insert(int indice, T elemento);
    
    /**
     * Elimina el elemento y desplaza los siguientes a la izquierda.
     * Postcondición: size() disminuye en 1.
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= size().
     */
    T remove(int indice);
    
    /**
     * Agrega el elemento al final de la secuencia.
     * Es semánticamente equivalente a insert(size(), elemento).
     */
    void append(T elemento);
}
```

Ese código no resuelve la implementación, pero sí muestra qué parte pertenece al problema abstracto. El cliente necesita saber que `get(3)` devuelve el cuarto elemento y que `insert(0, e)` lo agrega al principio. No necesita saber si por detrás hay un arreglo contiguo o una cadena de nodos dispersos.

Acá vuelve a aparecer la idea de **contrato** trabajada en [Diseño por Contratos](../../parte_3/13_oop_contratos.md): cada operación necesita precondiciones, postcondiciones y una semántica estable.

Por ejemplo:

- `get(i)` exige que `0 <= i < size()`;
- `insert(i, e)` exige que `0 <= i <= size()`;
- `remove(i)` exige que la posición exista antes de borrar;
- después de insertar, el tamaño aumenta en uno;
- después de borrar, el tamaño disminuye en uno y el orden relativo del resto debe seguir siendo coherente.

Si esas reglas no están claras, la secuencia deja de ser un TAD bien especificado y pasa a ser una implementación opaca que hay que adivinar.

## Orden, posición y recorrido

Una secuencia siempre tiene una lectura natural de izquierda a derecha, de principio a fin o de frente a fondo. Esa lectura permite hablar de:

- **primer elemento**,
- **último elemento**,
- **elemento en la posición `i`**,
- **siguiente** y **anterior** respecto de un punto dado.

Sin embargo, una cosa es que la **posición exista** y otra muy distinta es que acceder a ella sea barato.

### Acceso por índice vs acceso secuencial

Cuando la representación es contigua, como en los arreglos, el elemento en la posición `i` suele obtenerse rápido porque la dirección se calcula a partir de una base más un desplazamiento.

Cuando la representación es enlazada, la posición sigue existiendo como parte del contrato lógico, pero llegar a ella puede exigir recorrer paso a paso desde el inicio o desde un nodo conocido.

Por eso conviene separar dos preguntas que a veces se mezclan:

1. **¿La operación está permitida por el TAD?**
2. **¿Cuánto cuesta sostenerla en esta implementación?**

La primera pregunta pertenece a la abstracción. La segunda pertenece a la representación.

### Recorrer no es lo mismo que indexar

Muchos problemas no necesitan saltar a una posición arbitraria, sino visitar todos los elementos en orden. En esos casos, el recorrido puede ser más importante que el acceso aleatorio.

Ejemplos típicos:

- procesar una cola de eventos en orden de llegada,
- aplicar una transformación a todos los elementos,
- buscar el primer elemento que cumple cierta condición,
- reconstruir una salida respetando el orden original.

Cuando el recorrido domina, una estructura con acceso secuencial puede ser perfectamente adecuada aunque `get(i)` no sea su punto fuerte.

## Representaciones y trade-offs principales

La misma secuencia puede implementarse con estrategias muy distintas. Las dos familias base que conviene fijar desde ahora son estas:

| Representación | Qué gana | Qué paga |
| :--- | :--- | :--- |
| Memoria contigua | Acceso por índice, buena localidad de memoria, recorrido cache-friendly | Corrimientos al insertar o borrar en posiciones internas |
| Nodos enlazados | Inserciones y borrados locales, crecimiento flexible | Recorrido más costoso, peor localidad, acceso posicional indirecto |

```{mermaid}
flowchart TD
    subgraph Memoria Contigua (Ej. Arreglo)
        direction LR
        A1[0: A] -.- A2[1: B] -.- A3[2: C] -.- A4[3: D]
        style A1 fill:#e3f2fd,stroke:#0277bd,stroke-width:2px;
        style A2 fill:#e3f2fd,stroke:#0277bd,stroke-width:2px;
        style A3 fill:#e3f2fd,stroke:#0277bd,stroke-width:2px;
        style A4 fill:#e3f2fd,stroke:#0277bd,stroke-width:2px;
    end
    
    subgraph Memoria Enlazada (Ej. Nodos)
        direction LR
        N1(A) --> N2(B)
        N2 --> N3(C)
        N3 --> N4(D)
        
        style N1 fill:#f1f8e9,stroke:#33691e,stroke-width:2px;
        style N2 fill:#f1f8e9,stroke:#33691e,stroke-width:2px;
        style N3 fill:#f1f8e9,stroke:#33691e,stroke-width:2px;
        style N4 fill:#f1f8e9,stroke:#33691e,stroke-width:2px;
    end
```

Eso explica por qué no existe “la mejor secuencia” en abstracto. Lo que existe es una mejor decisión para cierto patrón de uso (ver {ref}`parte6-localidad-memoria`).

### Cuando domina el acceso

Si el problema consulta muchas veces “dame el elemento en la posición `i`”, una representación contigua suele ser fuerte. Ahí aparece el caso clásico de los arreglos.

### Cuando domina la edición local

Si el problema inserta o elimina cerca de una posición ya conocida, una representación enlazada puede evitar corrimientos masivos. Ahí aparecen las listas enlazadas.

### Cuando el acceso se restringe

Si el cliente no necesita cualquier operación, sino solo trabajar con un extremo o con ambos extremos, se puede especializar la secuencia y obtener TAD más simples:

- **pila**: acceso LIFO;
- **cola**: acceso FIFO;
- **deque**: inserción y borrado eficientes en ambos extremos.

La ventaja pedagógica de pensar así la familia es que esas estructuras dejan de verse como cosas separadas y pasan a verse como restricciones útiles sobre una misma base lineal.

## Invariantes y encapsulamiento

Toda implementación concreta de secuencia necesita invariantes que mantengan coherente la estructura interna.

Algunos ejemplos frecuentes:

- el tamaño lógico no puede ser negativo;
- si la estructura está vacía, su representación interna debe reflejarlo sin contradicciones;
- en un arreglo con capacidad, tiene que cumplirse `0 <= size <= capacity`;
- en una lista doble, los enlaces hacia adelante y hacia atrás tienen que ser consistentes;
- si hay nodos centinela, no deberían confundirse con datos reales.

Esos invariantes importan porque una secuencia mal encapsulada se rompe rápido. Si el código cliente puede tocar nodos, índices internos o arreglos de respaldo, la abstracción deja de proteger su representación.

Esto conecta directo con {ref}`regla-0x200C` y {ref}`regla-0x2011`: la cátedra insiste en no exponer detalles internos cuando eso obliga al cliente a operar sobre la representación en lugar de trabajar con comportamiento del dominio.

En estructuras de datos, eso significa evitar diseños como estos:

- devolver el arreglo interno para que el cliente mire;
- exponer nodos para que código externo rearme enlaces;
- ofrecer getters de bajo nivel en lugar de operaciones de secuencia con sentido abstracto.

La buena pregunta no es “¿cómo accedo al nodo siguiente desde afuera?”, sino “¿qué operación del TAD falta para expresar esa necesidad sin romper encapsulamiento?”.

## Cómo decidir qué secuencia conviene

Antes de elegir una implementación, conviene describir el patrón de operaciones dominante.

| Si el problema hace sobre todo... | Conviene empezar preguntándose... |
| :--- | :--- |
| consultas por posición | si el acceso aleatorio domina sobre la edición |
| inserciones o borrados internos | si se conoce la posición de trabajo o hay que buscarla |
| trabajo en un solo extremo | si en realidad el TAD correcto es una pila |
| trabajo en extremos opuestos | si en realidad el TAD correcto es una cola |
| trabajo en ambos extremos | si un deque expresa mejor el problema |
| recorrido completo frecuente | si el costo de iteración pesa más que el acceso por índice |

Este análisis evita elegir una estructura porque sí. Primero se fija el comportamiento requerido; después se compara qué representación lo sostiene mejor.

## Relación con los capítulos que siguen

Este capítulo no intenta cerrar la discusión de implementaciones. Intenta fijar el mapa conceptual que se va a reutilizar en toda la familia.

Desde acá, el recorrido natural es:

1. [Arreglos](arreglos.md), para estudiar representación contigua;
2. [Listas enlazadas](listas_enlazadas.md), para contrastar con representación por nodos;
3. [Pilas](pilas.md), [Colas](colas.md) y [Deques](deques.md), para ver restricciones de acceso;
4. [Colas de prioridad](colas_prioridad.md), como puente hacia heaps y algoritmos voraces.

## Resumen

El capítulo deja instalada una idea central: **secuencia** nombra una familia de problemas lineales, no una única implementación. Lo que define al TAD es el contrato sobre orden, posición, recorrido e inserción; lo que cambia entre variantes es el costo de sostener ese contrato.

Por eso, antes de elegir entre arreglos, listas o variantes restringidas, conviene responder tres preguntas:

1. qué operaciones dominan;
2. qué acceso necesita realmente el cliente;
3. qué invariantes y qué costo práctico trae cada representación.

## Ejercicios

```{exercise}
:label: ex-parte6-secuencias-fundamentos-mini

Describí una estructura de datos de uso cotidiano que pueda verse como secuencia. Indicá qué operaciones serían las más frecuentes y qué costo convendría optimizar.
```

```{exercise}
:label: ex-parte6-secuencias-fundamentos-contrato

Definí el contrato observable de una secuencia de canciones para una aplicación de reproducción. Indicá al menos cinco operaciones, sus precondiciones principales y un invariante de representación que una implementación debería respetar.
```

```{exercise}
:label: ex-parte6-secuencias-fundamentos-eleccion

Un editor mantiene una colección ordenada de líneas de texto. A veces necesita consultar una línea por número, pero también inserta y borra líneas en el medio. Explicá qué preguntas harías antes de decidir entre una representación contigua y una enlazada.
```

## Próximo paso

Para seguir, conviene pasar a [Arreglos](arreglos.md), donde aparece la primera implementación concreta de esta familia.
