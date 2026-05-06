---
title: "Recorridos"
subtitle: "DFS, BFS y exploración sistemática"
subject: Estructuras de Datos
description: Cómo funcionan DFS y BFS, qué estructuras reutilizan y por qué son la base de gran parte de los algoritmos de grafos.
---

(parte5-recorridos-grafos)=
# Recorridos

Los recorridos son el corazón algorítmico de la familia de grafos. A partir de DFS y BFS se desprenden ideas de alcanzabilidad, detección de ciclos, caminos mínimos no ponderados y análisis de componentes.

En grafos, recorrer no significa simplemente "visitar todos los elementos". Significa explorar una red sin perderse en ciclos, sin repetir trabajo innecesario y manteniendo información suficiente para responder preguntas sobre conectividad, capas o estructura.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender DFS y BFS como patrones de exploración que reutilizan pilas, colas y marcado de visitados.

**Prerrequisitos.** Conviene haber leído [Representación de grafos](representacion.md) y recordar [Pilas](../secuencias/pilas.md) y [Colas](../secuencias/colas.md).

**Desarrollo.** El capítulo presenta la idea general de exploración, desarrolla DFS y BFS, compara profundidad con anchura, y cierra con aplicaciones típicas para alcanzabilidad, componentes, ciclos y caminos mínimos no ponderados.
:::

:::{tip} Idea guía
Muchos algoritmos de grafos no inventan una lógica nueva desde cero: refinan un recorrido bien diseñado con el estado correcto.
:::

## Qué problema resuelven los recorridos

La pregunta base es simple:

- dado un vértice inicial, **¿qué parte del grafo puedo explorar y en qué orden?**

Pero esa pregunta se descompone en varias más concretas:

- qué vértices son alcanzables;
- qué aristas descubrí durante la exploración;
- si hay ciclos;
- si el grafo está conectado;
- qué distancia en cantidad de aristas tiene cada vértice al origen.

Para responderlas, tanto DFS como BFS necesitan algo que en grafos es obligatorio:

### Marcar visitados

Como un grafo puede tener ciclos, no alcanza con seguir aristas "hasta que termine". Sin una marca de visitado, un recorrido puede volver una y otra vez al mismo lugar.

La idea mínima es mantener un conjunto como este:

```java
Set<V> visitados = new HashSet<>();
```

y registrar cada vértice cuando se descubre o cuando se procesa, según la variante elegida.

### Árbol o bosque de recorrido

Mientras el algoritmo explora, muchas veces va construyendo implícitamente un **árbol de recorrido**:

- cada vértice nuevo queda asociado al vértice desde el cual fue descubierto;
- esa relación permite reconstruir caminos o componentes.

Si el grafo no es conexo y el algoritmo se reinicia desde varios vértices, aparece un **bosque** de recorrido.

## DFS: búsqueda en profundidad

La **búsqueda en profundidad** (*depth-first search*, DFS) explora un camino todo lo que puede antes de retroceder.

La intuición correcta es la de una pila:

1. elegís un vecino;
2. seguís profundizando;
3. si no podés avanzar más, retrocedés al último punto pendiente.

Por eso DFS se puede implementar:

- recursivamente, usando la pila de llamadas;
- iterativamente, usando una [Pila](../secuencias/pilas.md) explícita.

### DFS recursivo

```java
public void dfs(Grafo<String> grafo, String origen, Set<String> visitados) {
    visitados.add(origen);

    for (String vecino : grafo.vecinosDe(origen)) {
        if (!visitados.contains(vecino)) {
            dfs(grafo, vecino, visitados);
        }
    }
}
```

La idea del código es simple:

- marco el actual;
- reviso sus vecinos;
- si encuentro uno nuevo, profundizo por ahí.

### DFS iterativo

```java
public void dfsIterativo(Grafo<String> grafo, String origen) {
    Pila<String> pendientes = new PilaArray<>(100);
    Set<String> visitados = new HashSet<>();

    pendientes.push(origen);

    while (!pendientes.isEmpty()) {
        String actual = pendientes.pop();

        if (visitados.contains(actual)) {
            continue;
        }

        visitados.add(actual);

        for (String vecino : grafo.vecinosDe(actual)) {
            if (!visitados.contains(vecino)) {
                pendientes.push(vecino);
            }
        }
    }
}
```

Esta variante hace explícita la estructura que DFS venía usando de manera implícita: una pila de trabajo pendiente.

### Qué caracteriza a DFS

- profundiza antes de ensanchar;
- es natural para razonar recursivamente;
- suele ser útil para detectar ciclos, componentes y ordenamientos derivados;
- no garantiza caminos mínimos en cantidad de aristas.

## BFS: búsqueda por anchura

La **búsqueda por anchura** (*breadth-first search*, BFS) cambia la política de exploración:

- primero explora todos los vecinos a distancia 1;
- después todos los que están a distancia 2;
- luego los de distancia 3;
- y así sucesivamente.

La intuición correcta ahora es la de una [Cola](../secuencias/colas.md): el primer vértice descubierto pendiente es el primero en procesarse.

```java
public void bfs(Grafo<String> grafo, String origen) {
    Cola<String> pendientes = new ColaEnlazada<>();
    Set<String> visitados = new HashSet<>();

    pendientes.enqueue(origen);
    visitados.add(origen);

    while (!pendientes.isEmpty()) {
        String actual = pendientes.dequeue();

        for (String vecino : grafo.vecinosDe(actual)) {
            if (!visitados.contains(vecino)) {
                visitados.add(vecino);
                pendientes.enqueue(vecino);
            }
        }
    }
}
```

La diferencia clave con DFS no está en una línea aislada de código. Está en la política de pendientes:

- DFS usa pila y privilegia el último descubierto;
- BFS usa cola y privilegia el primero descubierto.

### Qué caracteriza a BFS

- explora por capas;
- calcula distancias mínimas en cantidad de aristas cuando el grafo no tiene pesos;
- es muy natural para problemas de alcance por niveles;
- suele requerir más memoria que DFS cuando la frontera crece mucho.

## Comparación directa entre DFS y BFS

| Aspecto | DFS | BFS |
| :--- | :--- | :--- |
| Estructura de soporte | pila / recursión | cola |
| Política | profundizar primero | expandir por capas |
| Camino mínimo en aristas | no lo garantiza | sí, si no hay pesos |
| Uso típico | ciclos, componentes, orden estructural | distancias, niveles, expansión mínima no ponderada |
| Intuición | backtracking | atención por turnos |

La comparación importante no es cuál es "mejor", sino cuál responde a la pregunta correcta.

## Costos y relación con la representación

El costo del recorrido no depende solo del algoritmo. También depende de cómo está guardado el grafo.

### Con lista de adyacencia

Cuando el grafo está representado con listas de adyacencia:

- DFS y BFS suelen recorrer todo el grafo en `O(V + E)`.

Eso pasa porque:

- cada vértice se visita una vez;
- cada arista se procesa una cantidad acotada de veces.

### Con matriz de adyacencia

Si el grafo está representado con matriz:

- revisar los vecinos de un vértice puede obligar a escanear una fila completa;
- por eso el costo total de explorar suele acercarse a `O(V^2)`.

Esto muestra por qué [Representación de grafos](representacion.md) no era un detalle previo sin importancia: cambia el costo real del algoritmo.

## Aplicaciones típicas

DFS y BFS son recorridos generales, pero sostienen varias tareas concretas.

### Alcanzabilidad

Pregunta típica:

- "¿Puedo llegar desde `A` hasta `B`?"

Tanto DFS como BFS sirven para responderla. Si `B` aparece durante la exploración, entonces es alcanzable desde `A`.

### Componentes conexas

En un grafo no dirigido, si corrés un recorrido desde un vértice y marcás todo lo alcanzado, obtenés una componente conexa. Repetir el proceso desde los no visitados permite contar todas las componentes.

### Detección de ciclos

DFS es especialmente natural para esta tarea porque su estructura de profundización deja más clara la diferencia entre:

- volver al padre inmediato;
- encontrar una arista que cierra un ciclo;
- o reencontrar un vértice ya procesado.

### Caminos mínimos no ponderados

En grafos sin peso, BFS descubre vértices por capas. Eso significa que el primer momento en que llega a un vértice coincide con una distancia mínima en cantidad de aristas.

Por eso BFS es la base de una versión simple del problema de caminos mínimos antes de pasar a [Caminos mínimos](caminos_minimos.md).

### Orden topológico y componentes fuertes

Más adelante, en [Orden topológico](orden_topologico.md) y [Conectividad](conectividad.md), van a aparecer algoritmos que ya no son "solo" DFS, pero dependen directamente de ideas que nacen acá:

- marcar visitados;
- recorrer sistemáticamente;
- guardar información de descubrimiento o finalización.

## Qué errores conviene evitar

Errores frecuentes:

1. olvidarse de marcar visitados y quedar atrapado en ciclos;
2. usar DFS cuando se necesita distancia mínima en cantidad de aristas;
3. usar BFS sin preguntarse si el grafo tiene pesos, caso donde ya no alcanza;
4. pensar que el costo del recorrido es independiente de la representación;
5. no guardar padre o distancia cuando después se necesita reconstruir información adicional.

## Resumen

Muchos algoritmos de grafos no parten de cero: son refinamientos de un buen recorrido con el estado correcto.

Las ideas que deberían quedar instaladas son estas:

1. recorrer un grafo exige marcar visitados;
2. DFS profundiza usando lógica de pila;
3. BFS expande por capas usando lógica de cola;
4. el costo del recorrido depende también de la representación;
5. alcanzabilidad, componentes, ciclos y caminos mínimos no ponderados nacen de estas dos estrategias.

## Ejercicios

```{exercise}
:label: ex-parte5-recorridos-grafos-mini

Explicá por qué BFS encuentra caminos mínimos en cantidad de aristas cuando el grafo no tiene pesos, pero DFS no garantiza eso.
```

```{exercise}
:label: ex-parte5-recorridos-grafos-visitados

Mostrá con un ejemplo de grafo con ciclo qué podría pasar si un DFS o un BFS no mantuviera un conjunto de visitados. Explicá por qué el problema no es solo de eficiencia, sino también de corrección.
```

```{exercise}
:label: ex-parte5-recorridos-grafos-capas

Tomá una red social y explicá qué significaría aplicar BFS desde una persona. Después justificá por qué el orden de descubrimiento puede interpretarse como distancia en cantidad de vínculos.
```

## Próximo paso

Para seguir, conviene pasar a [Caminos mínimos](caminos_minimos.md), donde el problema deja de ser solo recorrer y pasa a ser optimizar.
