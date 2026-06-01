---
title: "Recorridos"
subtitle: "DFS, BFS y exploración sistemática"
subject: Estructuras de Datos
description: Cómo funcionan DFS y BFS, qué estructuras reutilizan y por qué son la base de gran parte de los algoritmos de grafos.
---

(parte6-recorridos-grafos)=
# Recorridos

Recorrer un grafo es el arte de visitar sus nodos de manera sistemática. A diferencia de un arreglo (donde avanzamos en línea recta) o un árbol (donde bajamos por niveles), en un grafo podemos encontrar **ciclos** y **múltiples caminos** para llegar al mismo lugar.

Para no perdernos, todos los algoritmos de recorrido comparten una regla de oro: **Debemos marcar lo que ya visitamos**.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender DFS y BFS como patrones de exploración que reutilizan pilas, colas y marcado de visitados.

**Prerrequisitos.** [Representación de grafos](representacion.md), [Pilas](../secuencias/pilas.md) y [Colas](../secuencias/colas.md).

**Desarrollo.** El capítulo presenta DFS y BFS, compara sus estrategias (profundidad vs. anchura) y muestra sus aplicaciones para conectividad, ciclos y distancias.
:::

### Esquema General de Exploración (Pseudocódigo)

Casi cualquier recorrido sigue esta lógica de gestión de nodos pendientes:

```text
algoritmo recorrer(grafo, origen)
    pendientes ← crearEstructuraVacia()
    visitados ← crearConjuntoVacio()
    
    pendientes.agregar(origen)
    visitados.add(origen)
    
    mientras pendientes no este vacia hacer
        u ← pendientes.quitarProximo()
        procesar(u)
        
        para cada vecino v de u hacer
            si v no esta en visitados entonces
                visitados.add(v)
                pendientes.agregar(v)
            fin si
        fin para
    fin mientras
fin algoritmo
```

La diferencia fundamental entre DFS y BFS radica únicamente en qué tipo de estructura es `pendientes`: si es una **Pila**, el recorrido es DFS; si es una **Cola**, es BFS.

## El mecanismo base: Colores y Estados

Para que un recorrido sea correcto y eficiente, solemos pensar en tres estados para cada nodo:

1. **No visitado:** El nodo aún no fue descubierto.
2. **En proceso:** El nodo fue descubierto y está en la pila/cola, pero aún no terminamos de explorar sus vecinos.
3. **Procesado:** Ya exploramos todos sus vecinos y terminamos con él.

En la práctica, muchas veces nos alcanza con un simple `Set<V> visitados` para distinguir entre "No visitado" y "El resto".

## DFS: Búsqueda en Profundidad (Depth-First Search)

DFS funciona como alguien perdido en un laberinto: elige un camino y lo sigue hasta chocar con una pared o un nodo ya visitado; en ese momento, retrocede (**backtracking**) hasta la última bifurcación y prueba otro camino.

### Implementación Recursiva (La más natural)
La recursión usa implícitamente la pila del sistema.

```java
public void dfs(Grafo<V> g, V actual, Set<V> visitados) {
    visitados.add(actual);
    System.out.println("Visitando: " + actual);

    for (V vecino : g.vecinosDe(actual)) {
        if (!visitados.contains(vecino)) {
            dfs(g, vecino, visitados);
        }
    }
}
```

- **Uso ideal:** Detección de ciclos, orden topológico, laberintos, juegos donde queremos explorar una rama de decisión hasta el final.
- **Dato clave:** DFS genera un **árbol de expansión** (Spanning Tree). Si durante el recorrido encontramos un vecino que ya está "en proceso" pero no es nuestro padre, ¡hemos detectado un **ciclo**!

## BFS: Búsqueda en Anchura (Breadth-First Search)

BFS funciona como una onda en el agua: desde el origen, visita primero a todos sus vecinos directos (distancia 1), luego a los vecinos de sus vecinos (distancia 2), y así sucesivamente.

### Implementación Iterativa (Obligatoria con Cola)
Para explorar por capas, necesitamos una `Queue`.

```java
public void bfs(Grafo<V> g, V inicio) {
    Queue<V> cola = new LinkedList<>();
    Set<V> visitados = new HashSet<>();

    cola.add(inicio);
    visitados.add(inicio);

    while (!cola.isEmpty()) {
        V actual = cola.poll();
        System.out.println("Procesando: " + actual);

        for (V vecino : g.vecinosDe(actual)) {
            if (!visitados.contains(vecino)) {
                visitados.add(vecino); // Marcamos al descubrir, no al procesar
                cola.add(vecino);
            }
        }
    }
}
```

- **Uso ideal:** Encontrar el **camino más corto** en grafos sin pesos (o con pesos uniformes). 
- **Dato clave:** En un BFS, la primera vez que "tocamos" un nodo, lo hacemos por el camino más corto posible desde el origen.

## Comparativa: Profundidad vs. Anchura

| Característica | DFS | BFS |
| :--- | :--- | :--- |
| **Estructura** | Pila (explícita o recursión). | Cola (FIFO). |
| **Estrategia** | "Ir lejos rápido". | "Explorar los alrededores". |
| **Memoria** | $O(h)$ donde $h$ es la altura del árbol de exploración. | $O(W)$ donde $W$ es el ancho máximo de una capa. |
| **Camino Corto** | No lo garantiza. | **Sí**, en cantidad de aristas. |
| **Ciclos** | Muy eficiente para detectarlos. | Menos directo para ciclos en dirigidos. |

## Aplicaciones de los recorridos

1. **Alcanzabilidad:** ¿Puedo llegar de A a B? (Cualquiera de los dos sirve).
2. **Componentes Conexas:** En un grafo no dirigido, ¿cuántas "islas" separadas hay? (Corremos DFS/BFS desde un nodo, marcamos todo lo alcanzable, y si quedan nodos sin visitar, repetimos).
3. **Validación de Árbol:** Un grafo de $N$ nodos es un árbol si es conexo y tiene exactamente $N-1$ aristas.
4. **Flood Fill:** El algoritmo de "balde de pintura" en editores de imagen es un BFS/DFS sobre una grilla de píxeles.

## Resumen

- **Marcar visitados** es lo único que evita el lazo infinito.
- **DFS** es recursivo y va al fondo; **BFS** es iterativo y va por capas.
- Si buscás la distancia mínima en un laberinto o red sin pesos, usá **BFS**.
- Si buscás detectar ciclos o dependencias, usá **DFS**.

## Ejercicios

```{exercise}
:label: ex-parte6-recorridos-ciclos

Dibujá un grafo dirigido de 4 nodos que tenga un ciclo. Realizá un DFS manual y marcá en qué momento el algoritmo "se da cuenta" de que hay un ciclo. ¿Qué relación hay entre el nodo actual y el vecino ya visitado?
```

```{exercise}
:label: ex-parte6-recorridos-bfs-distancia

En un BFS, si queremos guardar no solo los visitados sino también la **distancia** desde el origen, ¿qué estructura adicional usarías? Modificá el código de BFS para que imprima la distancia de cada nodo al origen.
```

```{exercise}
:label: ex-parte6-recorridos-memoria

En un grafo que representa la web (miles de millones de páginas), ¿por qué un BFS podría agotar la memoria de una computadora mientras que un DFS no? Pensá en el tamaño de la "frontera" (la cola vs la pila).
```

## Próximo paso

¿Qué pasa si las aristas tienen pesos (como kilómetros o tiempo)? El BFS ya no nos garantiza el camino más corto. Para eso, necesitamos subir un nivel de complejidad: [Caminos mínimos](caminos_minimos.md).
