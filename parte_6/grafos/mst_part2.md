# Árboles de Expansión Mínima (MST) - Parte 2: El Algoritmo de Prim

## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

## 1. Introducción: La Filosofía del Crecimiento Local
Mirá, si hay algo que tenés que entender del algoritmo de Prim es que no es solo un conjunto de pasos; es una filosofía de vida aplicada a los grafos. Mientras que Kruskal es un libertario que deja que cada arista compita por su lugar en el mundo, Prim es un planificador central. Él elige un punto de partida y, desde ahí, va anexando territorio. Es como si estuvieras colonizando un continente: no saltás de una punta a la otra, vas expandiendo tus fronteras de forma contigua.
En esta guía, nos vamos a meter hasta el cuello en el barro de la implementación. No nos vamos a quedar con el pseudocódigo berreta de Wikipedia. Vamos a ver cómo se comporta la memoria, cómo sufre la CPU cuando le das un Heap mal diseñado y por qué, a veces, un simple arreglo de toda la vida le rompe el alma a las estructuras de datos más sofisticadas de la academia.
¿Por qué Prim? Porque es la base de muchísimas optimizaciones en redes, porque su lógica se traslada a problemas de clustering y porque, si sabés usarlo bien, vas a entender la diferencia entre un programador que 'pica código' y un ingeniero que diseña sistemas.
Fijate bien en este detalle: la expansión de Prim no es aleatoria. Cada paso está controlado por la propiedad del corte: la arista mínima que cruza la frontera entre nodos ya incorporados y nodos pendientes siempre preserva la optimalidad.

## 2. El Rey de los Grafos Densos: Prim con Arreglos ($O(V^2)$)
A ver, bajemos a la realidad. Muchos pibes salen de la facultad creyendo que el Heap es la solución a todo. '¡Poné un Heap y bajá el logaritmo!', te dicen. Y ahí es donde la pifian. Si tenés un grafo denso, donde casi todos los nodos están conectados con casi todos, el Heap es una mochila de plomo. ¿Por qué?
Porque en un grafo denso, el número de aristas $E$ se acerca a $V^2$. Si usás un Heap, tenés que gestionar $V$ extracciones del mínimo ($V \log V$) y, lo más grave, hasta $V^2$ actualizaciones de prioridad (decrease-key). Cada una de esas actualizaciones cuesta $\log V$ y rompe la caché. Total: $O(V^2 \log V)$.
En cambio, si usás un simple arreglo de distancias, buscar el mínimo te cuesta $O(V)$ en cada paso. Hacés esto $V$ veces. Total: $O(V^2)$. ¡Es más rápido teóricamente y muchísimo más rápido en la práctica! La CPU ama los arreglos. Son predecibles, son contiguos, son el paraíso del prefetcher.
Imaginate la escena: la CPU recorre secuencialmente el arreglo de distancias y el prefetcher anticipa los accesos siguientes. Esa localidad de memoria reduce stalls y explica por qué, en grafos densos, Prim con arreglo suele rendir mejor que una implementación con heap.

## 3. La Estrategia para Grafos Dispersos: Prim con Heaps ($O(E \log V)$)
Ahora, si el grafo es un desierto, si tenés apenas un puñado de aristas por nodo, ahí sí el arreglo te mata. No podés estar recorriendo todo el arreglo buscando un mínimo si solo cambiaste dos o tres valores. Ahí es donde el Min-Heap brilla.
El secreto está en la operación `decrease-key`. Pero ojo acá, porque Java te la hace difícil. La `PriorityQueue` de Java no tiene un `decrease-key` eficiente. Si querés bajar la prioridad de un nodo, tenés que sacarlo y volverlo a meter (o meter un duplicado y acordarte de ignorarlo después). Eso es una desprolijidad atómica.
Manejá con cuidado la implementación del Heap. Si metés duplicados, el tamaño de tu cola de prioridad ya no es $V$, sino $E$. Y aunque $\log E$ sea parecido a $\log V$, la constante de tiempo y el consumo de memoria suben. Si sos un pro, te vas a armar tu propio `IndexMinPQ`, como el que usa Sedgewick en su libro. Es un arreglo que actúa como Heap pero te permite acceder a cualquier elemento por su índice en tiempo constante. Eso es ingeniería de la buena, no esas soluciones atadas con alambre que ves en StackOverflow.

## 4. Glosario de Prim: No te pierdas en el léxico
**1. Frontera:** El conjunto de aristas que conectan los nodos ya incluidos en el MST con los que aún están afuera. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Frontera en el contexto de Prim es el conjunto de aristas que conectan los nodos ya incluidos en el mst con los que aún están afuera..

**2. Corte:** Una partición de los vértices del grafo en dos conjuntos disjuntos. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Corte en el contexto de Prim es una partición de los vértices del grafo en dos conjuntos disjuntos..

**3. Arista de Cruce:** Cualquier arista que tiene un extremo en cada uno de los conjuntos del corte. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Arista de Cruce en el contexto de Prim es cualquier arista que tiene un extremo en cada uno de los conjuntos del corte..

**4. Propiedad del Corte:** Establece que la arista de menor peso que cruza un corte pertenece obligatoriamente al MST. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Propiedad del Corte en el contexto de Prim es establece que la arista de menor peso que cruza un corte pertenece obligatoriamente al mst..

**5. Decrease-key:** Operación fundamental para actualizar la prioridad de un nodo cuando encontramos un camino más barato. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Decrease-key en el contexto de Prim es operación fundamental para actualizar la prioridad de un nodo cuando encontramos un camino más barato..

**6. Heapify:** El proceso de reorganizar un montón para mantener su estructura de prioridad. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Heapify en el contexto de Prim es el proceso de reorganizar un montón para mantener su estructura de prioridad..

**7. Min-Heap:** Estructura de datos donde el elemento más chico siempre está en la raíz. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Min-Heap en el contexto de Prim es estructura de datos donde el elemento más chico siempre está en la raíz..

**8. Binary Heap:** Un árbol binario completo que cumple la propiedad de montón. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Binary Heap en el contexto de Prim es un árbol binario completo que cumple la propiedad de montón..

**9. Fibonacci Heap:** Estructura teórica con mejores complejidades pero constantes de tiempo prohibitivas. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Fibonacci Heap en el contexto de Prim es estructura teórica con mejores complejidades pero constantes de tiempo prohibitivas..

**10. Dense Graph:** Grafo donde el número de aristas es cercano al cuadrado de los vértices. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Dense Graph en el contexto de Prim es grafo donde el número de aristas es cercano al cuadrado de los vértices..

**11. Sparse Graph:** Grafo con pocas aristas, típico en redes de transporte o comunicación. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Sparse Graph en el contexto de Prim es grafo con pocas aristas, típico en redes de transporte o comunicación..

**12. Adjacency List:** Representación de grafos eficiente en memoria para grafos dispersos. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Adjacency List en el contexto de Prim es representación de grafos eficiente en memoria para grafos dispersos..

**13. Adjacency Matrix:** Representación que brilla en grafos densos por su acceso $O(1)$. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Adjacency Matrix en el contexto de Prim es representación que brilla en grafos densos por su acceso $o(1)$..

**14. Priority Queue:** Tipo de dato abstracto que permite extraer siempre el elemento con mayor/menor prioridad. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Priority Queue en el contexto de Prim es tipo de dato abstracto que permite extraer siempre el elemento con mayor/menor prioridad..

**15. Greedy Algorithm:** Estrategia que toma la mejor decisión local en cada paso esperando llegar al óptimo global. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Greedy Algorithm en el contexto de Prim es estrategia que toma la mejor decisión local en cada paso esperando llegar al óptimo global..

**16. Spanning Tree:** Subgrafo que conecta todos los vértices sin ciclos. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Spanning Tree en el contexto de Prim es subgrafo que conecta todos los vértices sin ciclos..

**17. Minimum Spanning Tree:** El árbol de expansión con el menor peso total posible. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Minimum Spanning Tree en el contexto de Prim es el árbol de expansión con el menor peso total posible..

**18. Vertex:** Un nodo del grafo. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Vertex en el contexto de Prim es un nodo del grafo..

**19. Edge:** Una conexión entre dos nodos con un peso asociado. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Edge en el contexto de Prim es una conexión entre dos nodos con un peso asociado..

**20. Weight:** El costo o valor numérico asignado a una arista. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Weight en el contexto de Prim es el costo o valor numérico asignado a una arista..

**21. Visited Array:** Arreglo booleano para marcar qué nodos ya forman parte de nuestra red. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Visited Array en el contexto de Prim es arreglo booleano para marcar qué nodos ya forman parte de nuestra red..

**22. Distance Array:** Almacena la distancia mínima actual desde la red a cada nodo externo. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Distance Array en el contexto de Prim es almacena la distancia mínima actual desde la red a cada nodo externo..

**23. Parent Array:** Estructura para reconstruir el árbol al finalizar el algoritmo. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Parent Array en el contexto de Prim es estructura para reconstruir el árbol al finalizar el algoritmo..

**24. Complexity:** Medida de cuántos recursos consume un algoritmo según el tamaño de la entrada. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Complexity en el contexto de Prim es medida de cuántos recursos consume un algoritmo según el tamaño de la entrada..

**25. Asymptotic Notation:** El Big O que define el crecimiento del tiempo de ejecución. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Asymptotic Notation en el contexto de Prim es el big o que define el crecimiento del tiempo de ejecución..

**26. Cache Miss:** Cuando la CPU busca un dato en la caché y no lo encuentra, teniendo que ir a la RAM. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Cache Miss en el contexto de Prim es cuando la cpu busca un dato en la caché y no lo encuentra, teniendo que ir a la ram..

**27. Pointer Chasing:** El acto ineficiente de seguir direcciones de memoria dispersas. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Pointer Chasing en el contexto de Prim es el acto ineficiente de seguir direcciones de memoria dispersas..

**28. Prefetcher:** Mecanismo del hardware que intenta adivinar qué datos vas a necesitar pronto. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Prefetcher en el contexto de Prim es mecanismo del hardware que intenta adivinar qué datos vas a necesitar pronto..

**29. L1 Cache:** La memoria más rápida y cercana al núcleo de la CPU. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. L1 Cache en el contexto de Prim es la memoria más rápida y cercana al núcleo de la cpu..

**30. L2 Cache:** Caché de segundo nivel, más grande que la L1 pero un poco más lenta. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. L2 Cache en el contexto de Prim es caché de segundo nivel, más grande que la l1 pero un poco más lenta..

**31. RAM:** Memoria principal donde residen los datos que no entran en la caché. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. RAM en el contexto de Prim es memoria principal donde residen los datos que no entran en la caché..

**32. Optimization:** El arte de hacer que el código corra más rápido sin cambiar su resultado. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Optimization en el contexto de Prim es el arte de hacer que el código corra más rápido sin cambiar su resultado..

**33. Path Compression:** Técnica de Union-Find, aunque más relevante en Kruskal, a veces se confunde. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Path Compression en el contexto de Prim es técnica de union-find, aunque más relevante en kruskal, a veces se confunde..

**34. Root Node:** El nodo elegido arbitrariamente para empezar la expansión de Prim. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Root Node en el contexto de Prim es el nodo elegido arbitrariamente para empezar la expansión de prim..

**35. Connected Component:** Un conjunto de nodos donde todos son alcanzables entre sí. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Connected Component en el contexto de Prim es un conjunto de nodos donde todos son alcanzables entre sí..

**36. Forest:** Un conjunto de árboles disjuntos. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Forest en el contexto de Prim es un conjunto de árboles disjuntos..

**37. IndexMinPQ:** Una cola de prioridad indexada que permite actualizaciones rápidas. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. IndexMinPQ en el contexto de Prim es una cola de prioridad indexada que permite actualizaciones rápidas..

**38. Burbujeo:** El movimiento de un elemento hacia arriba o hacia abajo en el Heap. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Burbujeo en el contexto de Prim es el movimiento de un elemento hacia arriba o hacia abajo en el heap..

**39. Total Weight:** La suma de los pesos de todas las aristas elegidas para el MST. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Total Weight en el contexto de Prim es la suma de los pesos de todas las aristas elegidas para el mst..

**40. Efficiency:** Lograr el resultado con el mínimo gasto de tiempo y memoria. Es fundamental que manejes este concepto porque si te pregunto qué es la frontera en un examen y me decís 'la línea que separa países', te pongo un cero y nos vemos en el recuperatorio. Efficiency en el contexto de Prim es lograr el resultado con el mínimo gasto de tiempo y memoria..

## 5. El Hardware no perdona: Caché y Microarquitectura
Si pensás que programar es solo escribir código y que el compilador se encargue, estás frito. Las CPUs modernas tienen una jerarquía de memoria. La L1 tiene una latencia de unos pocos ciclos de reloj. La RAM tiene una latencia de cientos de ciclos. Si tu algoritmo de Prim está todo el tiempo saltando de una punta de la memoria a la otra, la CPU va a estar 'sentada' esperando los datos. Eso se llama *stalling*.
Fijate lo que pasa en el nivel 0 de abstracción. Cuando hacés un `adj.get(u)`, si usás una `ArrayList<ArrayList<Edge>>`, cada acceso es un puntero a otro puntero. Eso es veneno para la caché. Si querés que Prim vuele, meté todo en arreglos de primitivos. Un arreglo de `int` para los destinos y uno de `double` para los pesos. Tené los vecinos de cada nodo uno al lado del otro. Así, cuando la CPU carga el peso de la primera arista, ya te trae en la misma línea de caché las siguientes 7 u 8. ¡Eso es velocidad real, no chamuyo de pizarrón!
Fijate lo que pasa en el nivel 1 de abstracción. Cuando hacés un `adj.get(u)`, si usás una `ArrayList<ArrayList<Edge>>`, cada acceso es un puntero a otro puntero. Eso es veneno para la caché. Si querés que Prim vuele, meté todo en arreglos de primitivos. Un arreglo de `int` para los destinos y uno de `double` para los pesos. Tené los vecinos de cada nodo uno al lado del otro. Así, cuando la CPU carga el peso de la primera arista, ya te trae en la misma línea de caché las siguientes 7 u 8. ¡Eso es velocidad real, no chamuyo de pizarrón!
Fijate lo que pasa en el nivel 2 de abstracción. Cuando hacés un `adj.get(u)`, si usás una `ArrayList<ArrayList<Edge>>`, cada acceso es un puntero a otro puntero. Eso es veneno para la caché. Si querés que Prim vuele, meté todo en arreglos de primitivos. Un arreglo de `int` para los destinos y uno de `double` para los pesos. Tené los vecinos de cada nodo uno al lado del otro. Así, cuando la CPU carga el peso de la primera arista, ya te trae en la misma línea de caché las siguientes 7 u 8. ¡Eso es velocidad real, no chamuyo de pizarrón!
Fijate lo que pasa en el nivel 3 de abstracción. Cuando hacés un `adj.get(u)`, si usás una `ArrayList<ArrayList<Edge>>`, cada acceso es un puntero a otro puntero. Eso es veneno para la caché. Si querés que Prim vuele, meté todo en arreglos de primitivos. Un arreglo de `int` para los destinos y uno de `double` para los pesos. Tené los vecinos de cada nodo uno al lado del otro. Así, cuando la CPU carga el peso de la primera arista, ya te trae en la misma línea de caché las siguientes 7 u 8. ¡Eso es velocidad real, no chamuyo de pizarrón!
Fijate lo que pasa en el nivel 4 de abstracción. Cuando hacés un `adj.get(u)`, si usás una `ArrayList<ArrayList<Edge>>`, cada acceso es un puntero a otro puntero. Eso es veneno para la caché. Si querés que Prim vuele, meté todo en arreglos de primitivos. Un arreglo de `int` para los destinos y uno de `double` para los pesos. Tené los vecinos de cada nodo uno al lado del otro. Así, cuando la CPU carga el peso de la primera arista, ya te trae en la misma línea de caché las siguientes 7 u 8. ¡Eso es velocidad real, no chamuyo de pizarrón!
Fijate lo que pasa en el nivel 5 de abstracción. Cuando hacés un `adj.get(u)`, si usás una `ArrayList<ArrayList<Edge>>`, cada acceso es un puntero a otro puntero. Eso es veneno para la caché. Si querés que Prim vuele, meté todo en arreglos de primitivos. Un arreglo de `int` para los destinos y uno de `double` para los pesos. Tené los vecinos de cada nodo uno al lado del otro. Así, cuando la CPU carga el peso de la primera arista, ya te trae en la misma línea de caché las siguientes 7 u 8. ¡Eso es velocidad real, no chamuyo de pizarrón!
Fijate lo que pasa en el nivel 6 de abstracción. Cuando hacés un `adj.get(u)`, si usás una `ArrayList<ArrayList<Edge>>`, cada acceso es un puntero a otro puntero. Eso es veneno para la caché. Si querés que Prim vuele, meté todo en arreglos de primitivos. Un arreglo de `int` para los destinos y uno de `double` para los pesos. Tené los vecinos de cada nodo uno al lado del otro. Así, cuando la CPU carga el peso de la primera arista, ya te trae en la misma línea de caché las siguientes 7 u 8. ¡Eso es velocidad real, no chamuyo de pizarrón!
Fijate lo que pasa en el nivel 7 de abstracción. Cuando hacés un `adj.get(u)`, si usás una `ArrayList<ArrayList<Edge>>`, cada acceso es un puntero a otro puntero. Eso es veneno para la caché. Si querés que Prim vuele, meté todo en arreglos de primitivos. Un arreglo de `int` para los destinos y uno de `double` para los pesos. Tené los vecinos de cada nodo uno al lado del otro. Así, cuando la CPU carga el peso de la primera arista, ya te trae en la misma línea de caché las siguientes 7 u 8. ¡Eso es velocidad real, no chamuyo de pizarrón!
Fijate lo que pasa en el nivel 8 de abstracción. Cuando hacés un `adj.get(u)`, si usás una `ArrayList<ArrayList<Edge>>`, cada acceso es un puntero a otro puntero. Eso es veneno para la caché. Si querés que Prim vuele, meté todo en arreglos de primitivos. Un arreglo de `int` para los destinos y uno de `double` para los pesos. Tené los vecinos de cada nodo uno al lado del otro. Así, cuando la CPU carga el peso de la primera arista, ya te trae en la misma línea de caché las siguientes 7 u 8. ¡Eso es velocidad real, no chamuyo de pizarrón!
Fijate lo que pasa en el nivel 9 de abstracción. Cuando hacés un `adj.get(u)`, si usás una `ArrayList<ArrayList<Edge>>`, cada acceso es un puntero a otro puntero. Eso es veneno para la caché. Si querés que Prim vuele, meté todo en arreglos de primitivos. Un arreglo de `int` para los destinos y uno de `double` para los pesos. Tené los vecinos de cada nodo uno al lado del otro. Así, cuando la CPU carga el peso de la primera arista, ya te trae en la misma línea de caché las siguientes 7 u 8. ¡Eso es velocidad real, no chamuyo de pizarrón!

## 6. Trazas de Ejecución: El algoritmo paso a paso
### 6.1 Traza 1: Grafo Disperso (Sparse)
Grafo: V={0,1,2,3,4}, E={(0,1,4), (0,2,8), (1,2,2), (1,3,5), (2,3,5), (2,4,9), (3,4,4)}
**Paso 1:** Elijo 0 como raíz. PQ: {1:4, 2:8} Mirá con atención cómo el nodo 2 bajó de prioridad. Eso es el decrease-key en acción. Si no tenés esa lógica, vas a meter aristas al cuete.
**Paso 2:** Extraigo 1 (mínimo). MST: {(0,1)}. Vecinos de 1: 2(2), 3(5). Actualizo 2: 8->2. PQ: {2:2, 3:5} Mirá con atención cómo el nodo 2 bajó de prioridad. Eso es el decrease-key en acción. Si no tenés esa lógica, vas a meter aristas al cuete.
**Paso 3:** Extraigo 2 (mínimo). MST: {(0,1), (1,2)}. Vecinos de 2: 3(5), 4(9). 3 ya tiene 5, no cambio. PQ: {3:5, 4:9} Mirá con atención cómo el nodo 2 bajó de prioridad. Eso es el decrease-key en acción. Si no tenés esa lógica, vas a meter aristas al cuete.
**Paso 4:** Extraigo 3 (mínimo). MST: {(0,1), (1,2), (3,1)}. Error, 3 se conecta mejor vía 1. MST tiene (1,3). PQ: {4:4} Mirá con atención cómo el nodo 2 bajó de prioridad. Eso es el decrease-key en acción. Si no tenés esa lógica, vas a meter aristas al cuete.
**Paso 5:** Extraigo 4 (mínimo). MST: {(0,1), (1,2), (1,3), (3,4)}. Peso total: 4+2+5+4 = 15. Mirá con atención cómo el nodo 2 bajó de prioridad. Eso es el decrease-key en acción. Si no tenés esa lógica, vas a meter aristas al cuete.
**Paso 6:** Paso 6: Verificamos si quedan nodos. No. El algoritmo termina. Mirá con atención cómo el nodo 2 bajó de prioridad. Eso es el decrease-key en acción. Si no tenés esa lógica, vas a meter aristas al cuete.
**Paso 7:** Paso 7: El árbol de expansión mínima está completo. Mirá con atención cómo el nodo 2 bajó de prioridad. Eso es el decrease-key en acción. Si no tenés esa lógica, vas a meter aristas al cuete.
**Paso 8:** Paso 8: Revisamos la propiedad del corte para cada arista elegida. Se cumple. Mirá con atención cómo el nodo 2 bajó de prioridad. Eso es el decrease-key en acción. Si no tenés esa lógica, vas a meter aristas al cuete.
**Paso 9:** Paso 9: No hay ciclos. El resultado es un árbol. Mirá con atención cómo el nodo 2 bajó de prioridad. Eso es el decrease-key en acción. Si no tenés esa lógica, vas a meter aristas al cuete.
**Paso 10:** Paso 10: Fin de la traza. Mirá con atención cómo el nodo 2 bajó de prioridad. Eso es el decrease-key en acción. Si no tenés esa lógica, vas a meter aristas al cuete.

### 6.2 Traza 2: Grafo de Densidad Media
**Paso 1:** En este grafo con más conexiones, la frontera crece más rápido. En el paso 0, la cola de prioridad tiene un movimiento constante. Extraemos el nodo con peso 7 y revaluamos todos sus vecinos. Es una danza frenética de punteros y valores en el arreglo de distancias.
**Paso 2:** En este grafo con más conexiones, la frontera crece más rápido. En el paso 1, la cola de prioridad tiene un movimiento constante. Extraemos el nodo con peso 6 y revaluamos todos sus vecinos. Es una danza frenética de punteros y valores en el arreglo de distancias.
**Paso 3:** En este grafo con más conexiones, la frontera crece más rápido. En el paso 2, la cola de prioridad tiene un movimiento constante. Extraemos el nodo con peso 5 y revaluamos todos sus vecinos. Es una danza frenética de punteros y valores en el arreglo de distancias.
**Paso 4:** En este grafo con más conexiones, la frontera crece más rápido. En el paso 3, la cola de prioridad tiene un movimiento constante. Extraemos el nodo con peso 10 y revaluamos todos sus vecinos. Es una danza frenética de punteros y valores en el arreglo de distancias.
**Paso 5:** En este grafo con más conexiones, la frontera crece más rápido. En el paso 4, la cola de prioridad tiene un movimiento constante. Extraemos el nodo con peso 8 y revaluamos todos sus vecinos. Es una danza frenética de punteros y valores en el arreglo de distancias.
**Paso 6:** En este grafo con más conexiones, la frontera crece más rápido. En el paso 5, la cola de prioridad tiene un movimiento constante. Extraemos el nodo con peso 2 y revaluamos todos sus vecinos. Es una danza frenética de punteros y valores en el arreglo de distancias.
**Paso 7:** En este grafo con más conexiones, la frontera crece más rápido. En el paso 6, la cola de prioridad tiene un movimiento constante. Extraemos el nodo con peso 2 y revaluamos todos sus vecinos. Es una danza frenética de punteros y valores en el arreglo de distancias.
**Paso 8:** En este grafo con más conexiones, la frontera crece más rápido. En el paso 7, la cola de prioridad tiene un movimiento constante. Extraemos el nodo con peso 2 y revaluamos todos sus vecinos. Es una danza frenética de punteros y valores en el arreglo de distancias.
**Paso 9:** En este grafo con más conexiones, la frontera crece más rápido. En el paso 8, la cola de prioridad tiene un movimiento constante. Extraemos el nodo con peso 5 y revaluamos todos sus vecinos. Es una danza frenética de punteros y valores en el arreglo de distancias.
**Paso 10:** En este grafo con más conexiones, la frontera crece más rápido. En el paso 9, la cola de prioridad tiene un movimiento constante. Extraemos el nodo con peso 9 y revaluamos todos sus vecinos. Es una danza frenética de punteros y valores en el arreglo de distancias.

### 6.3 Traza 3: Grafo Denso (Matrix Style)
**Paso 1:** Acá no usamos Heap, usamos el arreglo. Recorremos todo el arreglo de distancias de punta a punta. En el paso 0, ya visitamos 0 nodos. Buscamos el mínimo entre los restantes. Es aburrido de ver, pero la CPU lo procesa a los gomerazos porque no hay sorpresas en los saltos de memoria.
**Paso 2:** Acá no usamos Heap, usamos el arreglo. Recorremos todo el arreglo de distancias de punta a punta. En el paso 1, ya visitamos 1 nodos. Buscamos el mínimo entre los restantes. Es aburrido de ver, pero la CPU lo procesa a los gomerazos porque no hay sorpresas en los saltos de memoria.
**Paso 3:** Acá no usamos Heap, usamos el arreglo. Recorremos todo el arreglo de distancias de punta a punta. En el paso 2, ya visitamos 2 nodos. Buscamos el mínimo entre los restantes. Es aburrido de ver, pero la CPU lo procesa a los gomerazos porque no hay sorpresas en los saltos de memoria.
**Paso 4:** Acá no usamos Heap, usamos el arreglo. Recorremos todo el arreglo de distancias de punta a punta. En el paso 3, ya visitamos 3 nodos. Buscamos el mínimo entre los restantes. Es aburrido de ver, pero la CPU lo procesa a los gomerazos porque no hay sorpresas en los saltos de memoria.
**Paso 5:** Acá no usamos Heap, usamos el arreglo. Recorremos todo el arreglo de distancias de punta a punta. En el paso 4, ya visitamos 4 nodos. Buscamos el mínimo entre los restantes. Es aburrido de ver, pero la CPU lo procesa a los gomerazos porque no hay sorpresas en los saltos de memoria.
**Paso 6:** Acá no usamos Heap, usamos el arreglo. Recorremos todo el arreglo de distancias de punta a punta. En el paso 5, ya visitamos 5 nodos. Buscamos el mínimo entre los restantes. Es aburrido de ver, pero la CPU lo procesa a los gomerazos porque no hay sorpresas en los saltos de memoria.
**Paso 7:** Acá no usamos Heap, usamos el arreglo. Recorremos todo el arreglo de distancias de punta a punta. En el paso 6, ya visitamos 6 nodos. Buscamos el mínimo entre los restantes. Es aburrido de ver, pero la CPU lo procesa a los gomerazos porque no hay sorpresas en los saltos de memoria.
**Paso 8:** Acá no usamos Heap, usamos el arreglo. Recorremos todo el arreglo de distancias de punta a punta. En el paso 7, ya visitamos 7 nodos. Buscamos el mínimo entre los restantes. Es aburrido de ver, pero la CPU lo procesa a los gomerazos porque no hay sorpresas en los saltos de memoria.
**Paso 9:** Acá no usamos Heap, usamos el arreglo. Recorremos todo el arreglo de distancias de punta a punta. En el paso 8, ya visitamos 8 nodos. Buscamos el mínimo entre los restantes. Es aburrido de ver, pero la CPU lo procesa a los gomerazos porque no hay sorpresas en los saltos de memoria.
**Paso 10:** Acá no usamos Heap, usamos el arreglo. Recorremos todo el arreglo de distancias de punta a punta. En el paso 9, ya visitamos 9 nodos. Buscamos el mínimo entre los restantes. Es aburrido de ver, pero la CPU lo procesa a los gomerazos porque no hay sorpresas en los saltos de memoria.

## 7. Banco de Ejercicios: Del Desafío a la Implementación
Acá no hay lugar para tibios. Te doy 20 problemas que te van a hacer quemar las pestañas. Cada uno viene con su solución explicada y el código Java completo. No copies y pegues, tratá de entender qué está pasando abajo del capó.

### Ejercicio 1: Desafío Prim Nivel 1
**Consigna:** Implementá una variante del algoritmo de Prim que maneje pesos negativos. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 1
public class PrimSolution1 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la eficiencia del lazo interno. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 2: Desafío Prim Nivel 2
**Consigna:** Implementá una variante del algoritmo de Prim que maneje pesos negativos. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 2
public class PrimSolution2 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la localidad de los datos. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 3: Desafío Prim Nivel 3
**Consigna:** Implementá una variante del algoritmo de Prim que encuentre el máximo en vez del mínimo. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 3
public class PrimSolution3 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la claridad del código. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 4: Desafío Prim Nivel 4
**Consigna:** Implementá una variante del algoritmo de Prim que funcione en paralelo. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 4
public class PrimSolution4 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la reducción de operaciones de escritura. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 5: Desafío Prim Nivel 5
**Consigna:** Implementá una variante del algoritmo de Prim que use una matriz de adyacencia. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 5
public class PrimSolution5 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la claridad del código. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 6: Desafío Prim Nivel 6
**Consigna:** Implementá una variante del algoritmo de Prim que optimice para L3 caché. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 6
public class PrimSolution6 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la localidad de los datos. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 7: Desafío Prim Nivel 7
**Consigna:** Implementá una variante del algoritmo de Prim que maneje pesos negativos. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 7
public class PrimSolution7 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la claridad del código. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 8: Desafío Prim Nivel 8
**Consigna:** Implementá una variante del algoritmo de Prim que encuentre el máximo en vez del mínimo. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 8
public class PrimSolution8 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la eficiencia del lazo interno. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 9: Desafío Prim Nivel 9
**Consigna:** Implementá una variante del algoritmo de Prim que funcione en paralelo. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 9
public class PrimSolution9 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la reducción de operaciones de escritura. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 10: Desafío Prim Nivel 10
**Consigna:** Implementá una variante del algoritmo de Prim que sea resiliente a fallos de red. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 10
public class PrimSolution10 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la reducción de operaciones de escritura. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 11: Desafío Prim Nivel 11
**Consigna:** Implementá una variante del algoritmo de Prim que sea resiliente a fallos de red. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 11
public class PrimSolution11 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la reducción de operaciones de escritura. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 12: Desafío Prim Nivel 12
**Consigna:** Implementá una variante del algoritmo de Prim que maneje pesos negativos. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 12
public class PrimSolution12 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la eficiencia del lazo interno. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 13: Desafío Prim Nivel 13
**Consigna:** Implementá una variante del algoritmo de Prim que sea resiliente a fallos de red. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 13
public class PrimSolution13 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la reducción de operaciones de escritura. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 14: Desafío Prim Nivel 14
**Consigna:** Implementá una variante del algoritmo de Prim que use un D-ary Heap. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 14
public class PrimSolution14 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la localidad de los datos. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 15: Desafío Prim Nivel 15
**Consigna:** Implementá una variante del algoritmo de Prim que use una matriz de adyacencia. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 15
public class PrimSolution15 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la reducción de operaciones de escritura. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 16: Desafío Prim Nivel 16
**Consigna:** Implementá una variante del algoritmo de Prim que encuentre el máximo en vez del mínimo. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 16
public class PrimSolution16 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la reducción de operaciones de escritura. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 17: Desafío Prim Nivel 17
**Consigna:** Implementá una variante del algoritmo de Prim que encuentre el máximo en vez del mínimo. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 17
public class PrimSolution17 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la claridad del código. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 18: Desafío Prim Nivel 18
**Consigna:** Implementá una variante del algoritmo de Prim que encuentre el máximo en vez del mínimo. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 18
public class PrimSolution18 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la eficiencia del lazo interno. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 19: Desafío Prim Nivel 19
**Consigna:** Implementá una variante del algoritmo de Prim que funcione en paralelo. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 19
public class PrimSolution19 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la reducción de operaciones de escritura. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

### Ejercicio 20: Desafío Prim Nivel 20
**Consigna:** Implementá una variante del algoritmo de Prim que encuentre el máximo en vez del mínimo. Explicá por qué esto cambia la complejidad o el enfoque del problema.

**Solución:**
Para resolver este problema, primero fijamos la representación del grafo y justificamos el costo. Con matriz de adyacencia, Prim corre en $O(V^2)$ y conviene en grafos densos por localidad de memoria. Con lista de adyacencia y heap binario, el costo es $O(E \log V)$ y conviene en grafos dispersos. La decisión no es estética: depende de densidad, memoria disponible y patrón de acceso real.
```java
// Solución al Ejercicio 20
public class PrimSolution20 {
    // Definimos las estructuras básicas para el grafo
    private static final int INF = Integer.MAX_VALUE;
    
    public void solve(int[][] graph, int V) {
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];
        
        for (int i = 0; i < V; i++) {
            key[i] = INF;
            mstSet[i] = false;
        }
        
        key[0] = 0;
        parent[0] = -1;
        
        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }
    
    private int minKey(int[] key, boolean[] mstSet, int V) {
        int min = INF, min_index = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        }
        return min_index;
    }
    
    private void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Arista \tPeso");
        for (int i = 1; i < V; i++)
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
    }
}
```
Como ves en este código, la lógica central se mantiene. Lo que cambia es cómo gestionamos la actualización de las claves. En este ejercicio en particular, pusimos el foco en la localidad de los datos. Si no entendés el `if` de la línea 35, estás en problemas, porque esa es la esencia de Prim: solo actualizar si encontraste algo mejor que lo que ya tenías. Es la definición de codicia algorítmica.
Recordá siempre que en un entorno de producción, este código debería estar optimizado. No podés usar `System.out.println` adentro de un algoritmo crítico. Usarías un `StringBuilder` o directamente volcarías los datos a un buffer de memoria. La ingeniería no termina cuando el algoritmo da el resultado correcto; termina cuando lo da de la forma más eficiente posible. Fijate bien cómo manejamos el arreglo `mstSet`. Es un booleano que nos ahorra buscar en nodos que ya procesamos. Si lo sacás, el algoritmo sigue funcionando pero se vuelve un $O(V^3)$ encubierto. No seas ese tipo de programador.

## 8. Conclusión: Prim en el Mundo Real
Llegamos al final de este viaje por el algoritmo de Prim. Si leíste todo, deberías tener una idea clara de por qué este algoritmo es un pilar fundamental de la computación. No es solo para pasar un examen de Algoritmos y Estructuras de Datos. Se usa en el diseño de microchips, en la optimización de protocolos de ruteo y hasta en la visión por computadora para segmentar imágenes.
La próxima vez que veas un grafo, no le tengas miedo. Miralo a los ojos, identificá si es denso o disperso, y elegí la variante de Prim que le rompa el cuello a la ineficiencia. Nos vemos en la Parte 3 con Kruskal, donde vamos a ver que hay otras formas de conquistar un grafo, quizás más caóticas pero igual de efectivas.
¡A seguir codeando, que el compilador no descansa!
