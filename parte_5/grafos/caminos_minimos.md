---
title: "Caminos mínimos"
subtitle: "Optimizar rutas según un costo"
subject: Estructuras de Datos
description: Cómo cambia el problema de caminos mínimos según haya pesos uniformes, pesos no negativos o pesos negativos, y qué hipótesis justifican BFS, Dijkstra y Bellman-Ford.
---

(parte5-caminos-minimos)=
# Caminos mínimos

Cuando el grafo tiene pesos o costos, ya no alcanza con saber si un vértice es alcanzable: hace falta decidir cuál es la mejor ruta. Ahí aparecen algoritmos que combinan representación, prioridad y relajación.

La dificultad no está solo en encontrar **algún** camino, sino en definir correctamente qué significa "mejor". A veces importa la menor cantidad de aristas. Otras veces importa la menor suma de distancias, tiempos o precios. Y en algunos dominios aparecen incluso pesos negativos, que rompen intuiciones que antes parecían obvias.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender qué cambia entre caminos mínimos en grafos no ponderados, ponderados sin pesos negativos y casos más generales.

**Prerrequisitos.** Conviene haber leído [Recorridos](recorridos.md) y recordar [Colas de prioridad](../secuencias/colas_prioridad.md).

**Desarrollo.** El capítulo define costo de camino y relajación, contrasta BFS, Dijkstra y Bellman-Ford según sus hipótesis, y cierra con criterios para elegir el algoritmo correcto en cada problema.
:::

:::{tip} Idea guía
"Camino mínimo" no nombra un único algoritmo. Nombra una familia de problemas donde cambia el algoritmo correcto según cómo se modelen los pesos.
:::

## Qué problema resuelven los caminos mínimos

La pregunta base es:

- dado un vértice origen, **¿cuál es el costo mínimo para llegar a cada destino y cómo se reconstruye esa ruta?**

Eso se puede particularizar así:

- costo mínimo desde un origen a todos los vértices;
- costo mínimo entre dos vértices puntuales;
- reconstrucción del camino óptimo;
- detección de casos donde el problema deja de estar bien definido.

### Qué significa el costo de un camino

Si el camino es:

- `v0 -> v1 -> v2 -> ... -> vk`

entonces su costo suele ser la suma de los pesos de sus aristas.

| Tipo de grafo | Qué se minimiza |
| :--- | :--- |
| No ponderado | cantidad de aristas |
| Ponderado | suma de pesos |
| Con pesos negativos | suma de pesos, pero con cuidado extra |

La definición importa porque cambia por completo qué algoritmo sirve.

## Distancia provisoria y relajación

Los algoritmos clásicos de caminos mínimos comparten una idea central: mantener para cada vértice una **mejor distancia conocida hasta ahora**.

Por ejemplo:

```java
Map<V, Integer> distancia = new HashMap<>();
Map<V, V> padre = new HashMap<>();
```

Al comienzo:

- el origen vale `0`;
- el resto suele inicializarse con infinito conceptual.

### Relajar una arista

**Relajar** una arista `u -> v` significa preguntar:

- "si ya sé llegar a `u` con cierto costo, ¿pasar por `u` mejora lo que sé sobre `v`?"

La regla típica es:

```java
if (distancia.get(u) + peso(u, v) < distancia.get(v)) {
    distancia.put(v, distancia.get(u) + peso(u, v));
    padre.put(v, u);
}
```

Esa operación parece pequeña, pero es el núcleo conceptual de Dijkstra y Bellman-Ford.

## BFS cuando todos los pesos valen lo mismo

En [Recorridos](recorridos.md) ya apareció que BFS descubre vértices por capas.

Eso implica que, cuando:

- el grafo no tiene pesos,
- o todas las aristas cuentan igual,

el primer momento en que BFS llega a un vértice coincide con una distancia mínima en cantidad de aristas.

### Por qué alcanza BFS

Si todas las aristas "cuestan 1", minimizar costo es exactamente lo mismo que minimizar cantidad de saltos.

Entonces la exploración por capas de BFS ya resuelve el problema.

```java
public void bfsDistancias(Grafo<String> grafo, String origen) {
    Cola<String> pendientes = new ColaEnlazada<>();
    Map<String, Integer> distancia = new HashMap<>();
    Map<String, String> padre = new HashMap<>();

    pendientes.enqueue(origen);
    distancia.put(origen, 0);

    while (!pendientes.isEmpty()) {
        String actual = pendientes.dequeue();

        for (String vecino : grafo.vecinosDe(actual)) {
            if (!distancia.containsKey(vecino)) {
                distancia.put(vecino, distancia.get(actual) + 1);
                padre.put(vecino, actual);
                pendientes.enqueue(vecino);
            }
        }
    }
}
```

### Cuándo conviene

- grafos no ponderados;
- redes donde solo importa la cantidad de pasos;
- problemas de "mínimo número de enlaces".

### Cuándo no alcanza

Si una arista vale `100` y otra vale `1`, ya no alcanza contar saltos. Ahí BFS puede encontrar un camino con menos aristas pero más caro.

## Dijkstra para pesos no negativos

Cuando las aristas tienen pesos, pero **ninguno es negativo**, aparece Dijkstra.

La intuición es esta:

1. mantenés una distancia provisoria para cada vértice;
2. elegís siempre el vértice pendiente con menor distancia conocida;
3. desde ahí relajás sus aristas salientes;
4. repetís hasta agotar pendientes.

La estructura auxiliar natural es una **cola de prioridad**, porque siempre hace falta extraer el siguiente mejor candidato.

### Esquema básico

```java
public void dijkstra(GrafoPesado<String> grafo, String origen) {
    Map<String, Integer> distancia = new HashMap<>();
    Map<String, String> padre = new HashMap<>();
    PriorityQueue<Estado> pendientes = new PriorityQueue<>();

    for (String v : grafo.vertices()) {
        distancia.put(v, Integer.MAX_VALUE);
    }

    distancia.put(origen, 0);
    pendientes.add(new Estado(origen, 0));

    while (!pendientes.isEmpty()) {
        Estado actual = pendientes.remove();

        if (actual.distancia() > distancia.get(actual.vertice())) {
            continue;
        }

        for (AristaPesada<String> arista : grafo.aristasDesde(actual.vertice())) {
            int candidata = distancia.get(actual.vertice()) + arista.peso();
            if (candidata < distancia.get(arista.destino())) {
                distancia.put(arista.destino(), candidata);
                padre.put(arista.destino(), actual.vertice());
                pendientes.add(new Estado(arista.destino(), candidata));
            }
        }
    }
}
```

No hace falta fijarse en cada detalle de sintaxis. Lo importante es la lógica:

- la cola de prioridad siempre propone el vértice con mejor distancia conocida;
- una vez consolidado bajo pesos no negativos, esa distancia ya no necesita empeorar;
- las relajaciones mejoran información de sus vecinos.

### Por qué exige pesos no negativos

La corrección de Dijkstra depende de que, una vez elegido el vértice más prometedor, no aparezca después un camino más barato "desde atrás" gracias a un peso negativo.

Si hay pesos negativos, esa garantía se rompe.

### Costos típicos

Con lista de adyacencia + cola de prioridad:

- suele analizarse como `O((V + E) log V)` o formas equivalentes según implementación.

Con matriz:

- el costo puede empeorar hacia `O(V^2)`.

## Bellman-Ford para pesos negativos

Si el grafo puede tener pesos negativos, Bellman-Ford amplía el alcance del problema.

La idea ya no es elegir siempre el mejor candidato pendiente, sino relajar sistemáticamente todas las aristas una cantidad acotada de veces.

### Intuición

En un grafo sin ciclos negativos, cualquier camino simple usa a lo sumo `V - 1` aristas.

Entonces, si relajás todas las aristas `V - 1` veces, las distancias mínimas deberían estabilizarse.

```java
public boolean bellmanFord(GrafoPesado<String> grafo, String origen) {
    Map<String, Integer> distancia = new HashMap<>();

    for (String v : grafo.vertices()) {
        distancia.put(v, Integer.MAX_VALUE);
    }
    distancia.put(origen, 0);

    for (int i = 1; i < grafo.cantidadVertices(); i++) {
        for (AristaPesada<String> arista : grafo.aristas()) {
            if (distancia.get(arista.origen()) == Integer.MAX_VALUE) {
                continue;
            }

            int candidata = distancia.get(arista.origen()) + arista.peso();
            if (candidata < distancia.get(arista.destino())) {
                distancia.put(arista.destino(), candidata);
            }
        }
    }

    for (AristaPesada<String> arista : grafo.aristas()) {
        if (distancia.get(arista.origen()) == Integer.MAX_VALUE) {
            continue;
        }

        int candidata = distancia.get(arista.origen()) + arista.peso();
        if (candidata < distancia.get(arista.destino())) {
            return false; // hay ciclo negativo alcanzable
        }
    }

    return true;
}
```

### Qué gana

- soporta pesos negativos;
- detecta ciclos negativos alcanzables desde el origen.

### Qué paga

- es más caro que Dijkstra;
- su costo típico se analiza como `O(V * E)`.

## Qué pasa con los ciclos negativos

Un **ciclo negativo** rompe el problema de camino mínimo clásico.

Si podés dar vueltas por un ciclo que resta costo cada vez, entonces:

- no existe un mínimo bien definido;
- siempre podrías volver a bajar el costo una vez más.

Por eso Bellman-Ford no solo calcula distancias: también puede avisar que el problema, tal como está modelado, no tiene solución válida bajo ese criterio.

## Comparación rápida entre algoritmos

| Algoritmo | Hipótesis | Qué minimiza | Estructura clave |
| :--- | :--- | :--- | :--- |
| BFS | pesos uniformes o inexistentes | cantidad de aristas | cola |
| Dijkstra | pesos no negativos | suma de pesos | cola de prioridad |
| Bellman-Ford | puede haber pesos negativos | suma de pesos | relajación repetida de aristas |

Esto deja clara una idea importante: los algoritmos no compiten en el vacío. Cada uno responde a una versión distinta del problema.

## Reconstrucción del camino

No alcanza con conocer el costo mínimo. Muchas veces también hace falta recuperar la ruta.

Para eso se mantiene un mapa de **padres** o predecesores:

- si `padre[v] = u`, el mejor camino conocido a `v` llega desde `u`.

Al terminar, se reconstruye de atrás hacia adelante:

1. empezar en el destino;
2. seguir `padre[destino]`, `padre[padre[destino]]`, etc.;
3. invertir la secuencia.

Esta técnica vale para BFS, Dijkstra y Bellman-Ford.

## Panorama adicional: todos contra todos

Hasta acá el foco fue **single-source shortest paths**: un origen hacia todos los destinos.

Existe además la variante:

- **all-pairs shortest paths**, donde se quiere la mejor distancia entre todos los pares.

No hace falta desarrollarla a fondo acá, pero conviene saber que:

- puede resolverse repitiendo algoritmos de origen único en varios contextos;
- o con algoritmos específicos como Floyd-Warshall.

## Qué errores conviene evitar

Errores frecuentes:

1. usar BFS en un grafo ponderado solo porque "igual recorre";
2. usar Dijkstra sin verificar que no existan pesos negativos;
3. hablar de camino mínimo sin aclarar si el costo es por aristas o por suma de pesos;
4. olvidar reconstruir padres cuando después hace falta la ruta y no solo el costo;
5. ignorar que un ciclo negativo puede invalidar el problema.

## Resumen

Camino mínimo no nombra un único algoritmo. Nombra una familia de soluciones cuyo criterio cambia según cómo esté modelado el costo.

Las ideas que deberían quedar instaladas son estas:

1. BFS alcanza cuando todas las aristas valen lo mismo;
2. Dijkstra necesita pesos no negativos;
3. Bellman-Ford amplía el problema a pesos negativos y detecta ciclos negativos;
4. relajación y distancia provisoria son las nociones comunes de fondo.

## Ejercicios

```{exercise}
:label: ex-parte5-caminos-minimos-mini

Indicá en qué contexto BFS alcanza para caminos mínimos y en qué contexto hace falta Dijkstra. Justificá qué dato del problema cambia entre ambos casos.
```

```{exercise}
:label: ex-parte5-caminos-minimos-negativos

Explicá por qué Dijkstra puede fallar si existe una arista de peso negativo. No hace falta demostrar el algoritmo completo: alcanza con construir un ejemplo chico y justificar dónde se rompe su hipótesis.
```

```{exercise}
:label: ex-parte5-caminos-minimos-rutas

En una red vial, distinguí tres casos y elegí algoritmo para cada uno:

1. todas las rutas cuentan igual;
2. cada ruta tiene una distancia positiva;
3. algunas aristas pueden representar descuentos o créditos negativos.

Justificá la elección en cada caso.
```

## Próximo paso

Para seguir, conviene pasar a [Árboles de expansión](arboles_de_expansion.md), donde la pregunta deja de ser una ruta y pasa a ser una red completa de conexión mínima.
