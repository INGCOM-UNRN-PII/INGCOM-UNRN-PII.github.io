---
title: "Orden topológico"
subtitle: "Linearizar dependencias en DAG"
subject: Estructuras de Datos
description: Cómo linearizar dependencias en grafos dirigidos acíclicos y por qué la presencia de ciclos vuelve imposible ese tipo de orden.
---

(parte6-orden-topologico)=
# Orden topológico

Imaginá que estás organizando el plan de estudios de una carrera. Hay materias que podés cursar en cualquier momento, pero otras requieren que hayas aprobado ciertas correlativas antes. ¿Cómo decidís en qué orden rendirlas todas sin violar ninguna regla? Este es el problema del **Orden Topológico**.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender cuándo un grafo dirigido acíclico admite una linearización válida y cómo construirla.

**Prerrequisitos.** [Fundamentos de grafos](fundamentos.md) y [Recorridos](recorridos.md).

**Desarrollo.** El capítulo define DAG y precedencia parcial, muestra por qué un ciclo vuelve imposible el orden topológico y compara los algoritmos de DFS y Kahn.
:::

## El escenario: Grafos Dirigidos Acíclicos (DAG)

Para que un orden topológico exista, el grafo debe ser un **DAG** (Directed Acyclic Graph). 

- **Dirigido:** Porque la relación es de precedencia ($A \to B$ significa "$A$ debe ir antes que $B$").
- **Acíclico:** Porque si hubiera un ciclo ($A \to B \to C \to A$), tendríamos una paradoja: para hacer $A$ necesitás terminar $C$, pero para hacer $C$ necesitás terminar $A$.

:::{important} Detección de Deadlocks
En sistemas operativos, los ciclos en grafos de dependencias representan un **deadlock** (bloqueo mutuo). El orden topológico es la herramienta para verificar que un sistema de tareas es ejecutable.
:::

## 1. Algoritmo de Kahn (Basado en Grados de Entrada)

Es el enfoque más intuitivo. Se basa en una idea simple: si un nodo tiene **grado de entrada 0**, significa que nadie depende de él; por lo tanto, es seguro realizarlo ahora.

### Pasos del algoritmo:
1. Calcular el grado de entrada de todos los nodos.
2. Meter en una `Queue` todos los nodos con grado 0.
3. Mientras la cola no esté vacía:
    a. Sacar un nodo $u$ y agregarlo a la lista final.
    b. Para cada vecino $v$ de $u$:
        - Restar 1 al grado de entrada de $v$.
        - Si el grado de $v$ llega a 0, meterlo en la cola.

**¿Y si sobran nodos?** Si al terminar la cola la lista final no tiene todos los nodos del grafo, significa que hay un **ciclo** oculto que impidió que algunos grados llegaran a cero.

## 2. Algoritmo basado en DFS

Podemos usar un recorrido DFS para obtener el orden. La lógica es: "No puedo terminar una tarea hasta que todas las tareas que dependen de ella estén terminadas".

1. Realizar un DFS sobre el grafo.
2. Cuando un nodo termina de procesar todos sus vecinos (estado **Procesado**), lo metemos en una `Stack`.
3. Al finalizar todo el recorrido, el contenido de la pila (de arriba hacia abajo) es un orden topológico válido.

```java
public void dfsTopologico(V u, Set<V> visitados, Stack<V> pila) {
    visitados.add(u);
    for (V v : grafo.vecinosDe(u)) {
        if (!visitados.contains(v)) {
            dfsTopologico(v, visitados, pila);
        }
    }
    pila.push(u); // Se agrega al terminar sus dependencias
}
```

## Comparativa: ¿Kahn o DFS?

| Característica | Algoritmo de Kahn | Basado en DFS |
| :--- | :--- | :--- |
| **Estructura** | Queue (FIFO). | Stack (LIFO) + Recursión. |
| **Detección de Ciclos** | Explícita (si sobran nodos). | Requiere estados (Gris/Negro). |
| **Intuición** | "Hago lo que está libre ahora". | "Hago lo que libera el futuro". |

## Aplicaciones Reales

- **Gestores de Paquetes:** Cuando instalás un programa en Linux (`apt install`), el sistema calcula un orden topológico para instalar primero las librerías base y al final el programa.
- **Compiladores:** Para decidir en qué orden compilar archivos fuente que se importan entre sí.
- **Excel:** Cuando cambiás el valor de una celda, Excel usa un orden topológico para recalcular todas las celdas que dependen de ese valor sin entrar en bucles infinitos.

## Resumen

1. El orden topológico es una **linearización** de un grafo de dependencias.
2. Solo existe si el grafo es un **DAG** (sin ciclos).
3. **Kahn** usa grados de entrada; **DFS** usa el orden de finalización.
4. Si el algoritmo falla, es prueba fehaciente de que hay una **dependencia circular**.

## Ejercicios

```{exercise}
:label: ex-parte6-topologico-ordenes

Dado un grafo con aristas $(A, B), (A, C), (B, D), (C, D)$. ¿Cuántos órdenes topológicos válidos existen? Listalos todos.
```

```{exercise}
:label: ex-parte6-topologico-ciclo

Explicá por qué el algoritmo de Kahn no puede procesar un nodo que forma parte de un ciclo. ¿Cuál es el valor mínimo del grado de entrada de cualquier nodo dentro de un ciclo?
```

```{exercise}
:label: ex-parte6-topologico-compilacion

Imaginá un proyecto con tres archivos: `A.java` importa a `B.java`, y `B.java` importa a `A.java`. Dibujá el grafo y explicá por qué el compilador dará un error de "dependencia circular".
```

## Próximo paso

Hemos visto cómo movernos, cómo optimizar y cómo ordenar. Ahora cerraremos la familia de grafos estudiando cómo se agrupan los nodos en "comunidades" o [Conectividad](conectividad.md).
