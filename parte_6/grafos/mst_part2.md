---
title: "Árboles de Expansión Mínima (MST) - Parte 2: El Algoritmo de Prim"
description: Análisis profundo del algoritmo de Prim, comparando implementaciones para grafos densos y dispersos, con foco en microarquitectura y estructuras de datos indexadas.
---

(parte6-grafos-prim)=
# El Algoritmo de Prim

A diferencia de Kruskal, que adopta un enfoque descentralizado, el algoritmo de Prim es un "planificador central". Comienza en un nodo raíz arbitrario y expande sus fronteras de forma contigua, anexando siempre la arista de menor peso que conecte el árbol actual con un nodo externo.

Si bien la lógica parece trivial, su implementación eficiente oculta sutilezas sobre la gestión de prioridades y el impacto del hardware en el rendimiento real. En este capítulo, vamos a destripar a Prim desde su fundamentación matemática hasta su ejecución en el "hierro".

:::{tip} Objetivos observables

Al finalizar este capítulo, vas a poder:

1. **Justificar** la elección entre la implementación con arreglos ($O(V^2)$) y con heaps ($O(E \log V)$) según la densidad del grafo.
2. **Implementar** el algoritmo de Prim en Java utilizando colas de prioridad indexadas para evitar el overhead de duplicados.
3. **Explicar** la propiedad del corte y cómo garantiza la optimalidad global mediante decisiones locales (greedy).
4. **Analizar** el impacto de la localidad de memoria y los cache misses en la performance del algoritmo según la estructura de datos elegida.
:::

:::{note} Hoja de ruta del capítulo

**Prerrequisitos.** Requiere solidez en representaciones de grafos (matrices y listas de adyacencia) y dominio de la estructura Heap.

**Desarrollo.** Comenzamos con la filosofía del crecimiento local. Luego, contrastamos las dos implementaciones canónicas: el modelo para grafos densos y el modelo para grafos dispersos. Pasamos al análisis de hardware para entender por qué "Big O" no lo es todo. Cerramos con trazas paso a paso y ejercicios de diseño.
:::

## 1. La Filosofía del Crecimiento Local

Prim no salta de una punta del grafo a la otra. Su crecimiento es orgánico y contiguo.

- **Concepto:** Cada paso de Prim está gobernado por la **Propiedad del Corte**. Al particionar los vértices en dos conjuntos (incorporados y pendientes), la arista mínima que cruza esa frontera pertenece obligatoriamente al MST.
- **Ejemplo:** Al colonizar un territorio, Prim no lanza paracaidistas al azar (como Kruskal); expande sus fronteras metro a metro.
- **Contraejemplo:** Un algoritmo que elija la arista mínima global en cada paso sin importar la conectividad con el árbol actual es Kruskal, no Prim.

:::{check} Chequeo de comprensión
¿Qué sucede si el grafo no es conexo? Prim solo encontrará el MST del componente que contiene al nodo raíz. Para el resto, quedará una "frontera" infranqueable.
:::

## 2. El Rey de los Grafos Densos: Arreglos ($O(V^2)$)

Muchos cometen el error de creer que el Heap es la solución universal. En grafos densos ($E \approx V^2$), el Heap es una mochila de plomo.

- **Concepto:** Si casi todos los nodos están conectados, actualizar prioridades en un Heap cuesta $O(E \log V)$. En cambio, recorrer un arreglo de distancias para buscar el mínimo cuesta $O(V)$ por paso.
- **Implementación:** Se mantiene un arreglo `key[]` con la distancia mínima conocida desde el MST a cada nodo.
- **Ventaja:** La CPU ama los arreglos. El acceso secuencial permite al **prefetcher** anticipar datos, reduciendo stalls de memoria.
- **Costo:** $V$ búsquedas del mínimo ($V \times V$) + $E$ actualizaciones ($O(1)$ cada una). Total: $O(V^2)$.

## 3. Grafos Dispersos: Prim con Heaps ($O(E \log V)$)

Cuando el grafo es un desierto de aristas, no podés permitirte recorrer todo el arreglo buscando un mínimo que apenas cambió.

- **Concepto:** El **Min-Heap** permite extraer el siguiente nodo a incorporar en $O(\log V)$.
- **El problema de Java:** `PriorityQueue` no tiene un `decrease-key` eficiente. La solución académica es usar una **Cola de Prioridad Indexada** (`IndexMinPQ`).
- **Contraejemplo:** Meter duplicados en la cola de prioridad (`lazy prim`). Funciona, pero el tamaño de la cola escala con $E$ en lugar de $V$, aumentando el jitter por presión en el Garbage Collector.

:::{check} Chequeo de comprensión
¿Por qué Sedgewick prefiere una `IndexMinPQ`? Porque permite actualizar la prioridad de un nodo específico en $O(\log V)$ sin ensuciar la memoria con entradas redundantes.
:::

## 4. El Hardware no perdona: Caché y Microarquitectura

Programar no es solo escribir código; es negociar con la jerarquía de memoria.

- **Niveles de Abstracción:**
    - **Nivel L0 (Datos):** Usar `ArrayList<ArrayList<Edge>>` es veneno. Cada acceso es un puntero a otro puntero (*pointer chasing*).
    - **Nivel L1 (Optimizado):** Usar arreglos de primitivos contiguos. Al cargar una arista, la CPU trae automáticamente las siguientes en la misma línea de caché.
- **Efecto:** Una implementación $O(V^2)$ con arreglos puede ser más rápida que una $O(E \log V)$ con Heaps incluso en grafos medianos, simplemente por la eficiencia del acceso lineal a memoria.

## 5. Trazas de Ejecución: El paso a paso

### 5.1 Traza en Grafo Disperso
**Grafo:** V={0,1,2,3,4}, E={(0,1,4), (0,2,8), (1,2,2), (1,3,5), (2,3,5), (2,4,9), (3,4,4)}

1. **Inicio:** Raíz 0. PQ: `{1:4, 2:8}`.
2. **Paso 1:** Extraigo 1. MST: `{(0,1)}`. Vecinos de 1: 2(2), 3(5). Actualizo 2: `8 -> 2`. PQ: `{2:2, 3:5}`.
3. **Paso 2:** Extraigo 2. MST: `{(0,1), (1,2)}`. Vecinos de 2: 3(5), 4(9). 3 ya tiene 5, no cambia. PQ: `{3:5, 4:9}`.
4. **Paso 3:** Extraigo 3. MST: `{(0,1), (1,2), (1,3)}`. Vecinos de 3: 4(4). Actualizo 4: `9 -> 4`. PQ: `{4:4}`.
5. **Fin:** Extraigo 4. MST completo. Peso total: 15.

## 6. Glosario Técnico

1. **Frontera:** Aristas que conectan los nodos del MST con los nodos externos.
2. **Corte:** Partición de vértices en dos conjuntos disjuntos.
3. **Arista de Cruce:** Arista con un extremo en cada conjunto del corte.
4. **Propiedad del Corte:** La arista mínima de un corte pertenece al MST.
5. **Decrease-key:** Actualización de prioridad de un nodo en el Heap.
6. **IndexMinPQ:** Cola de prioridad que permite acceso directo por índice de nodo.
7. **Cache Miss:** Fallo al buscar datos en caché, obligando a ir a la RAM lenta.
8. **Stalling:** Tiempo muerto de la CPU esperando datos de memoria.
9. **Prefetcher:** Unidad de hardware que anticipa qué datos se necesitarán.
10. **Greedy:** Estrategia de elegir el óptimo local esperando el óptimo global.

## 7. Banco de Ejercicios

### Ejercicio 1: Prim vs. Kruskal
**Consigna:** Explicá un escenario donde Prim supere a Kruskal en rendimiento real y justificá basándote en estructuras de datos.
**Solución:** En un grafo extremadamente denso, Kruskal debe ordenar $E$ aristas ($V^2 \log V^2$). Prim con arreglos corre en $O(V^2)$. Prim gana no solo por el logaritmo, sino por el acceso contiguo al arreglo de distancias.

### Ejercicio 2: El costo de la desprolijidad
**Consigna:** Implementá Prim en Java usando `PriorityQueue` y manejá duplicados para simular un `decrease-key`. ¿Cuál es la complejidad espacial en el peor caso?
**Solución:** La complejidad espacial sube a $O(E)$, ya que en el peor caso cada arista genera un duplicado en la cola antes de que el nodo sea procesado.

### Ejercicio 3: Prim Máximo
**Consigna:** Modificá el algoritmo para hallar el Árbol de Expansión Máximo. ¿Cambia la lógica del corte?
**Solución:** La lógica es idéntica. La Propiedad del Corte establece que la arista de **mayor** peso de un corte pertenece al Árbol de Expansión Máximo. Solo cambiamos el Min-Heap por un Max-Heap.

## 8. Conclusión: La Ingeniería del Detalle
Prim nos enseña que el diseño de algoritmos no termina en el Big O. Entender si tu grafo es denso o disperso, y cómo tu código va a interactuar con la caché de la CPU, es lo que separa a un programador de un ingeniero. Nos vemos en la Parte 3 con Kruskal, el enfoque descentralizado para conquistar grafos.
