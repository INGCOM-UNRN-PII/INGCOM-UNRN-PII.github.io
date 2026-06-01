---
title: "Colas de prioridad"
subtitle: "Elegir el siguiente elemento por prioridad"
subject: Estructuras de Datos
description: Cómo cambia el TAD cuando el siguiente elemento no depende del orden de llegada, sino de una relación de prioridad.
---

(parte6-colas-prioridad)=
# Colas de prioridad

Las colas de prioridad cierran la familia de secuencias con un cambio fuerte de criterio: el siguiente elemento ya no sale por orden de llegada ni por última inserción, sino por **prioridad relativa**.

Eso vuelve a esta estructura especialmente útil en problemas donde siempre importa recuperar **el mejor candidato actual**: el paciente más urgente, la tarea más prioritaria, el evento con tiempo más cercano o el vértice con menor distancia provisoria.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender la cola de prioridad como TAD y anticipar por qué los heaps se vuelven una implementación dominante.

**Prerrequisitos.** Conviene haber leído [Deques](deques.md) y tener presente el marco de [Análisis de algoritmos](../algoritmos.md).

**Desarrollo.** Primero se define el TAD y se distingue de FIFO y LIFO. Después se comparan implementaciones simples, se introduce el heap binario como solución dominante y se cierra con aplicaciones y errores frecuentes.
:::

## Qué define a una cola de prioridad

Una cola de prioridad mantiene un conjunto dinámico de elementos y permite extraer rápidamente el de mejor prioridad según una regla de comparación.

Las operaciones típicas son:

| Operación | Qué hace |
| :--- | :--- |
| `insert(x)` | Inserta un elemento |
| `findMin()` o `findMax()` | Consulta el mejor candidato sin quitarlo |
| `deleteMin()` o `deleteMax()` | Quita y devuelve el mejor candidato |
| `isEmpty()` | Informa si la estructura está vacía |

Según el problema, la prioridad puede definirse de dos formas simétricas:

- **min-priority queue**: sale primero el menor;
- **max-priority queue**: sale primero el mayor.

La estructura no cambia de naturaleza entre una y otra. Cambia la relación de orden que decide quién es "mejor".

:::{important}
Una cola de prioridad no promete mantener toda la secuencia ordenada para recorrerla cómodamente. Promete algo más específico: poder encontrar y extraer rápido el mejor elemento disponible.
:::

## En qué se diferencia de una cola FIFO

En una cola común, el siguiente elemento depende del tiempo de llegada. En una cola de prioridad, depende de una **regla de comparación**.

| Estructura | Criterio de salida |
| :--- | :--- |
| Cola FIFO | Sale primero quien entró primero |
| Pila LIFO | Sale primero quien entró último |
| Cola de prioridad | Sale primero quien tenga mejor prioridad |

```{mermaid}
flowchart TD
    subgraph Cola FIFO Normal
        direction LR
        In1((1)) --> In2((5)) --> In3((2)) --> Out1((Sale: 1))
        style In1 fill:#e0e0e0
        style In2 fill:#e0e0e0
        style In3 fill:#e0e0e0
    end
    
    subgraph Cola de Prioridad (Min)
        direction LR
        P1((1)) ~~~ P2((5)) ~~~ P3((2))
        P1 -.-> Out2((Sale: 1))
        P3 -.-> Out3((Sale: 2))
        P2 -.-> Out4((Sale: 5))
        style P1 fill:#ffcdd2,stroke:#d32f2f
        style P3 fill:#ffe0b2,stroke:#f57c00
        style P2 fill:#c8e6c9,stroke:#388e3c
    end
```

Eso tiene una consecuencia importante: el orden de inserción puede quedar completamente alterado por la prioridad.

Por ejemplo, si entran tareas con prioridades `5`, `1`, `8` y trabajás con una min-priority queue, la próxima en salir será la `1`, aunque no haya llegado primera.

## Prioridad, empates y estabilidad

El TAD necesita responder una pregunta que suele pasarse por alto: **¿qué pasa si dos elementos tienen la misma prioridad?**

Hay dos enfoques típicos:

1. aceptar cualquier desempate válido,
2. exigir **estabilidad**, es decir, respetar el orden de llegada entre prioridades iguales.

La estabilidad no viene "gratis". Si el problema la exige, la implementación necesita guardar información extra o un comparador que la incluya explícitamente.

Por eso conviene distinguir:

- **criterio principal**: urgencia, costo, distancia, fecha;
- **criterio de desempate**: orden de inserción, ID, timestamp, etcétera.

## Implementaciones simples: qué operación querés abaratar

Antes de llegar al heap, conviene ver el dilema estructural de fondo. Si no querés pagar mucho al extraer el mejor, probablemente pagues más al insertar. Si querés insertar barato, vas a pagar más al buscar o quitar el mejor.

### Arreglo o lista desordenada

La opción más directa es guardar todo sin orden especial.

```java
public final class ColaPrioridadSimple {
    private final List<Integer> datos = new ArrayList<>();

    public void insert(int valor) {
        this.datos.add(valor);
    }

    public int deleteMin() {
        if (this.datos.isEmpty()) {
            throw new IllegalStateException("La cola esta vacia");
        }

        int mejorIndice = 0;
        for (int i = 1; i < this.datos.size(); i++) {
            if (this.datos.get(i) < this.datos.get(mejorIndice)) {
                mejorIndice = i;
            }
        }

        return this.datos.remove(mejorIndice);
    }
}
```

Acá:

- insertar es barato,
- pero encontrar o quitar el mejor exige recorrido lineal.

### Arreglo o lista ordenada

La idea opuesta es mantener los elementos ordenados todo el tiempo. Así, el mejor candidato queda siempre en un extremo conocido.

En esa variante:

- `findMin()` y `deleteMin()` pueden quedar en O(1) si el mejor está en un extremo,
- pero `insert(x)` pasa a costar O(n) porque hay que encontrar la posición correcta y desplazar o reenlazar.

### Resumen del trade-off

| Representación | `insert` | `findMin` | `deleteMin` | Idea |
| :--- | :--- | :--- | :--- | :--- |
| Arreglo o lista desordenada | O(1) | O(n) | O(n) | Insertar ahora, pagar al extraer |
| Arreglo ordenado | O(n) | O(1) | O(1) | Mantener orden todo el tiempo |
| Lista enlazada ordenada | O(n) | O(1) | O(1) | Evita corrimientos, pero igual recorre para insertar |

Estas opciones sirven para entender el problema, pero no escalan bien cuando tanto inserción como extracción del mejor importan mucho.

## Heap binario: la implementación dominante

El heap binario aparece justamente para evitar ese dilema extremo. No mantiene un orden total, pero sí conserva la información suficiente para que el mejor candidato quede accesible en la raíz.

En un **min-heap** se cumple:

- cada nodo es menor o igual que sus hijos;
- por lo tanto, la raíz contiene el mínimo.

En un **max-heap**, la desigualdad se invierte.

```{mermaid}
graph TD
    subgraph Min-Heap Binario
        N1((1)) --> N2((5))
        N1 --> N3((2))
        N2 --> N4((10))
        N2 --> N5((6))
        N3 --> N6((8))
        N3 --> N7((4))
    end
    
    style N1 fill:#ffcdd2,stroke:#d32f2f
```

La clave es notar qué **no** garantiza:

- no dice nada fuerte sobre el orden entre hermanos,
- no sirve para recorrer todo en orden,
- no reemplaza a un árbol de búsqueda.

Solo mantiene la información necesaria para prioridad.

## Representación implícita en arreglo

El heap binario suele implementarse en un arreglo, no con nodos enlazados explícitos. Si guardás el árbol por niveles, los índices alcanzan para reconstruir parentescos.

Para un índice `i`:

| Relación | Fórmula típica |
| :--- | :--- |
| Padre | `(i - 1) / 2` |
| Hijo izquierdo | `2 * i + 1` |
| Hijo derecho | `2 * i + 2` |

Eso evita punteros extra y aprovecha bien la localidad de memoria.

```java
public final class MinHeap {
    private final List<Integer> datos = new ArrayList<>();

    public void insert(int valor) {
        this.datos.add(valor);
        this.subir(this.datos.size() - 1);
    }

    public int findMin() {
        if (this.datos.isEmpty()) {
            throw new IllegalStateException("El heap esta vacio");
        }
        return this.datos.get(0);
    }

    public int deleteMin() {
        if (this.datos.isEmpty()) {
            throw new IllegalStateException("El heap esta vacio");
        }

        int minimo = this.datos.get(0);
        int ultimo = this.datos.remove(this.datos.size() - 1);

        if (!this.datos.isEmpty()) {
            this.datos.set(0, ultimo);
            this.bajar(0);
        }

        return minimo;
    }

    private void subir(int indice) {
        while (indice > 0) {
            int padre = (indice - 1) / 2;
            if (this.datos.get(indice) >= this.datos.get(padre)) {
                return;
            }

            this.intercambiar(indice, padre);
            indice = padre;
        }
    }

    private void bajar(int indice) {
        int size = this.datos.size();

        while (true) {
            int izquierdo = 2 * indice + 1;
            int derecho = 2 * indice + 2;
            int menor = indice;

            if (izquierdo < size && this.datos.get(izquierdo) < this.datos.get(menor)) {
                menor = izquierdo;
            }

            if (derecho < size && this.datos.get(derecho) < this.datos.get(menor)) {
                menor = derecho;
            }

            if (menor == indice) {
                return;
            }

            this.intercambiar(indice, menor);
            indice = menor;
        }
    }

    private void intercambiar(int a, int b) {
        int tmp = this.datos.get(a);
        this.datos.set(a, this.datos.get(b));
        this.datos.set(b, tmp);
    }
}
```

No hace falta memorizar el código completo. Lo importante es ver la lógica:

1. al insertar, el nuevo elemento sube hasta restaurar la propiedad de heap;
2. al quitar la raíz, el último elemento ocupa ese lugar y baja hasta restaurar la propiedad.

Con eso:

| Operación | Costo típico en heap binario |
| :--- | :--- |
| `findMin` / `findMax` | O(1) |
| `insert` | O(log n) |
| `deleteMin` / `deleteMax` | O(log n) |

Ese equilibrio explica por qué el heap domina como implementación práctica de colas de prioridad.

## Relación con heaps y con Java

En este capítulo alcanza con entender al heap como implementación dominante de la cola de prioridad. El desarrollo estructural más fino sigue en [Heaps](../arboles/heaps.md).

En Java, la clase estándar `PriorityQueue<E>` responde justamente a esta idea: una cola donde el siguiente elemento depende del orden natural o de un `Comparator`, no del orden de llegada.

:::{note}
Una `PriorityQueue` no es estable por defecto. Si el problema necesita desempate por orden de llegada, esa regla debe incorporarse explícitamente.
:::

## Dónde sirve una cola de prioridad

### Planificación y scheduling

Si las tareas tienen prioridad distinta, una cola FIFO no alcanza. Querés despachar antes las urgentes o las de menor costo estimado.

### Simulación por eventos

En simulaciones discretas suele interesar ejecutar primero el evento con tiempo más próximo. La prioridad es temporal, no de llegada.

### Algoritmos voraces

Muchos algoritmos extraen repetidamente el mejor candidato actual. Ese patrón aparece, por ejemplo, en Dijkstra, Prim y otras estrategias voraces.

### Atención con urgencia

Si una guardia médica debe priorizar gravedad y no solo orden de ingreso, la abstracción natural deja de ser la cola FIFO.

## Errores frecuentes

### Confundir prioridad con orden total

Una cola de prioridad no está pensada para consultas de rango ni para recorrer todo ordenado. Si eso domina, probablemente estés más cerca de un diccionario ordenado.

### Exigir búsqueda arbitraria barata

El heap es excelente para recuperar el mejor elemento, pero no para responder "¿está X?" o "¿dónde está X?" de manera eficiente. Resolver más cosas de las que el TAD promete cambia la estructura necesaria.

### Ignorar la política de empate

Si dos elementos tienen igual prioridad y el problema necesita desempate reproducible, dejarlo implícito es un error de especificación.

### Usar una cola FIFO por costumbre

Cuando el siguiente elemento debería elegirse por urgencia o costo, modelarlo como cola común obliga a meter excepciones y parches. Ahí el error no es de implementación: es de abstracción.

## Resumen

La cola de prioridad organiza una secuencia según una pregunta muy específica: **quién es el mejor candidato ahora**. No intenta conservar todo ordenado ni ofrecer acceso general al medio.

Las implementaciones simples muestran el trade-off entre insertar barato y extraer barato. El heap binario resuelve mejor ese equilibrio porque mantiene exactamente la información necesaria para prioridad y nada más. Por eso es el puente natural entre esta familia de secuencias y el capítulo de [Heaps](../arboles/heaps.md).

## Ejercicios

```{exercise}
:label: ex-parte6-colas-prioridad-mini

Justificá por qué una cola FIFO no alcanza para modelar la atención de pacientes en una guardia cuando la prioridad médica debe alterar el orden de ingreso.
```

```{exercise}
:label: ex-parte6-colas-prioridad-empates

Diseñá una cola de prioridad para soporte técnico donde la prioridad principal sea severidad y el desempate se haga por orden de llegada. Explicá qué información extra debería guardar cada elemento.
```

```{exercise}
:label: ex-parte6-colas-prioridad-implementaciones

Compará una implementación con arreglo desordenado y otra con heap binario para un sistema que inserta trabajos todo el tiempo y extrae seguido el de mayor prioridad. Indicá qué costo paga cada una y cuál elegirías.
```

## Próximo paso

Para seguir, conviene pasar a [Diccionarios y conjuntos](../diccionarios/indice.md), donde la pregunta central deja de ser la prioridad posicional y pasa a ser la clave.
