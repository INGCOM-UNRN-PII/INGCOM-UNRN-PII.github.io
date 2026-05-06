---
title: "Fundamentos de grafos"
subtitle: "Relaciones generales entre vértices"
subject: Estructuras de Datos
description: Qué problemas modelan los grafos, qué significan vértices, aristas, caminos y componentes, y por qué esta familia exige pensar más allá de listas y árboles.
---

(parte5-fundamentos-grafos)=
# Fundamentos de grafos

Los grafos cierran la parte con la familia más general: cuando ni la linealidad ni la jerarquía alcanzan para modelar el problema, hace falta pensar en vértices, aristas, caminos y conectividad.

En una secuencia el problema dominante era el orden. En un árbol, la jerarquía. En un grafo, en cambio, el centro pasa a ser la **red de relaciones**: quién está conectado con quién, por qué tipo de vínculo y con qué costo o dirección.

Eso vuelve a los grafos especialmente útiles cuando el dominio ya no encaja ni como lista ni como estructura jerárquica estricta. Redes sociales, rutas, dependencias entre tareas, circuitos, vínculos entre páginas o conexiones entre ciudades son problemas donde la relación importa tanto como los objetos relacionados.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Instalar el vocabulario mínimo de grafos para poder hablar después de representación, recorridos y algoritmos clásicos.

**Prerrequisitos.** Conviene haber trabajado [Árboles](../arboles/indice.md), porque ayuda a contrastar jerarquía estricta con relaciones arbitrarias.

**Desarrollo.** El capítulo define vértices y aristas, distingue tipos de grafo, fija el lenguaje de caminos, ciclos y conectividad, y cierra mostrando qué preguntas abre esta familia para representación y recorridos.
:::

:::{tip} Idea guía
Un grafo conviene cuando el problema no pregunta solo "qué elemento sigue" o "de quién depende", sino "qué conexiones existen dentro de una red".
:::

## Qué hace abstracto a un grafo

Un **grafo** modela un conjunto de entidades y un conjunto de relaciones entre ellas.

La notación matemática más habitual es:

- `G = (V, E)`
- `V`: conjunto de **vértices** o nodos
- `E`: conjunto de **aristas** o conexiones

Lo importante no es la letra usada, sino la idea: un grafo separa claramente:

1. **qué elementos participan**;
2. **qué relaciones existen entre ellos**.

### Vértices y aristas

Los **vértices** representan objetos del dominio:

- ciudades,
- personas,
- materias,
- páginas,
- servidores,
- estaciones.

Las **aristas** representan relaciones:

- ruta entre ciudades,
- amistad o seguimiento,
- dependencia entre materias,
- enlace entre páginas,
- cable o conexión de red.

Ejemplos:

| Dominio | Vértices | Aristas |
| :--- | :--- | :--- |
| Red vial | ciudades | rutas |
| Red social | usuarios | relaciones |
| Correlatividades | materias | dependencia "para cursar A antes hay que aprobar B" |
| Sitio web | páginas | enlaces |

El grafo no obliga a que todas las relaciones sean del mismo tipo semántico, pero sí obliga a decidir **qué aspecto del problema querés modelar**. Si mezclás demasiados significados distintos en la misma arista, el modelo pierde claridad.

## Grafos dirigidos, no dirigidos y ponderados

No todas las aristas expresan lo mismo. Por eso conviene distinguir desde el inicio varias familias.

### Grafo no dirigido

En un grafo **no dirigido**, la relación es simétrica. Si hay una arista entre `A` y `B`, se interpreta que el vínculo vale en ambos sentidos.

Ejemplos típicos:

- amistad mutua,
- cable físico entre dos equipos,
- camino bidireccional.

### Grafo dirigido

En un grafo **dirigido**, cada arista tiene orientación. No alcanza con saber que `A` y `B` están relacionados; importa además si la relación va de `A` a `B`, de `B` a `A` o en ambos sentidos.

Ejemplos:

- una página enlaza a otra,
- una materia requiere otra como correlativa,
- un usuario sigue a otro,
- una calle es de una sola mano.

En grafos dirigidos conviene hablar de:

- **arista saliente** de un vértice,
- **arista entrante** a un vértice.

### Grafo ponderado

Un grafo es **ponderado** si sus aristas tienen un peso o costo asociado.

Ese peso puede representar:

- distancia,
- tiempo,
- precio,
- latencia,
- capacidad,
- riesgo.

Si la arista no tiene peso explícito, muchas veces se trabaja como si todas costaran lo mismo. Esa diferencia después será clave para distinguir BFS de Dijkstra o Bellman-Ford.

### Otras clasificaciones útiles

| Tipo | Qué significa |
| :--- | :--- |
| **Simple** | no hay lazos ni aristas repetidas entre el mismo par de vértices |
| **Con lazos** | un vértice puede tener arista hacia sí mismo |
| **Multigrafo** | puede haber varias aristas entre el mismo par de vértices |
| **Completo** | todos los pares de vértices están conectados |

No hace falta usar todas estas variantes siempre. Hace falta reconocer que cada una cambia qué algoritmos tienen sentido y qué invariantes debería respetar la representación.

## Cómo se piensa un grafo como TAD

Antes de discutir matrices o listas de adyacencia, conviene fijar qué operaciones forman parte del problema abstracto.

Un contrato posible para un TAD grafo podría incluir operaciones como estas:

| Operación | Qué expresa |
| :--- | :--- |
| agregar vértice | incorporar una entidad a la red |
| agregar arista | crear una relación |
| quitar arista | eliminar una relación |
| consultar adyacencia | saber si dos vértices están conectados |
| vecinos de un vértice | recuperar conexiones salientes o incidentes |
| recorrer vértices | iterar el conjunto de participantes |

Un ejemplo mínimo:

```java
public interface Grafo<V> {
    void agregarVertice(V vertice);
    void agregarArista(V origen, V destino);
    boolean sonAdyacentes(V origen, V destino);
    Iterable<V> vecinosDe(V vertice);
    Iterable<V> vertices();
}
```

Ese contrato todavía no dice **cómo** se guarda el grafo. Lo que fija es la semántica observable.

Según el dominio, después podrían agregarse:

- pesos,
- eliminación de vértices,
- consulta de grado,
- aristas entrantes,
- etiquetas sobre aristas.

La idea es la misma que en el resto de la parte: primero se fija el TAD; después se compara qué representación lo sostiene mejor.

## Vecinos, grado y adyacencia

Hay tres nociones que conviene dejar muy claras desde el inicio.

### Adyacencia

Dos vértices son **adyacentes** si existe una arista relevante entre ellos.

- en grafos no dirigidos, la adyacencia es simétrica;
- en grafos dirigidos, puede valer en un sentido y no en el otro.

### Vecinos

Los **vecinos** de un vértice son los vértices a los que está conectado.

En grafos dirigidos muchas veces conviene separar:

- vecinos salientes,
- vecinos entrantes.

### Grado

El **grado** mide cuántas aristas están asociadas a un vértice.

| Tipo de grafo | Medida útil |
| :--- | :--- |
| No dirigido | grado total |
| Dirigido | grado de entrada y grado de salida |

Estas nociones parecen pequeñas, pero reaparecen una y otra vez:

- al contar conectividad,
- al elegir representación,
- al analizar complejidad,
- al construir algoritmos sobre vecinos.

## Caminos, ciclos y alcanzabilidad

La pregunta más común sobre un grafo no es "¿están directamente conectados?", sino "¿puedo llegar desde un vértice hasta otro?".

### Camino

Un **camino** es una secuencia de vértices (o de aristas) donde cada paso respeta una conexión existente.

Ejemplo:

- Córdoba -> Rosario -> Buenos Aires

es un camino si cada tramo corresponde a una arista válida.

En un camino importa:

- cuántas aristas recorre,
- qué costo total acumula si hay pesos,
- si repite o no vértices.

### Alcanzabilidad

Un vértice `B` es **alcanzable** desde `A` si existe algún camino de `A` a `B`.

Esta idea es central porque muchos problemas reales no necesitan la mejor ruta todavía; primero necesitan saber si **hay alguna** ruta.

### Ciclo

Un **ciclo** aparece cuando un camino vuelve al punto de partida respetando las aristas del grafo.

Los ciclos importan mucho porque cambian el tipo de problema:

- en dependencias entre tareas, un ciclo puede ser un error;
- en una red vial, puede ser algo totalmente natural;
- en recorridos, obliga a marcar visitados para no quedar en lazo infinito.

### Camino simple

Un **camino simple** no repite vértices. Esta variante suele ser la más útil cuando se quiere razonar sobre estructura sin contar vueltas redundantes.

## Conectividad y componentes

Una vez que aparece la idea de alcanzabilidad, surge otra pregunta: ¿el grafo forma una sola pieza o varias?

### Conectividad en grafos no dirigidos

Un grafo no dirigido es **conexo** si cualquier vértice puede alcanzar a cualquier otro.

Si no pasa eso, el grafo queda partido en **componentes conexas**.

Cada componente conexa es un subconjunto de vértices internamente conectado, pero aislado del resto.

### Conectividad en grafos dirigidos

En dirigidos, la cosa se bifurca:

- **conectividad débil**: si se ignoran las direcciones, todo queda conectado;
- **conectividad fuerte**: cada vértice puede alcanzar a cada otro respetando las direcciones.

Las **componentes fuertemente conexas** reaparecerán más adelante porque son una forma importante de particionar grafos dirigidos.

## Qué relación tienen con árboles y secuencias

Conviene ubicar esta familia frente a las anteriores.

| Familia | Qué organiza | Restricción fuerte |
| :--- | :--- | :--- |
| Secuencia | orden lineal | cada elemento ocupa una posición |
| Árbol | jerarquía | no hay ciclos y cada nodo tiene estructura de subárbol |
| Grafo | red general de relaciones | puede haber ciclos, múltiples caminos y conexiones arbitrarias |

Todo árbol puede verse como un grafo, pero no todo grafo es un árbol.

La diferencia importante es que el árbol impone una estructura mucho más rígida:

- hay una raíz,
- cada nodo ocupa un lugar jerárquico claro,
- no debería haber ciclos,
- y el camino entre raíz y un nodo tiene una forma bien definida.

En grafos, en cambio, puede haber:

- varios caminos entre dos vértices,
- componentes separadas,
- ciclos,
- relaciones cruzadas.

Por eso esta familia exige pasar de la intuición "recorrer una estructura" a la intuición "explorar una red".

## Qué preguntas abre esta familia

Una vez fijado el vocabulario base, los grafos abren una colección de preguntas más algorítmicas.

| Pregunta | Capítulo que la trabaja |
| :--- | :--- |
| ¿Cómo conviene representar el grafo? | [Representación de grafos](representacion.md) |
| ¿Qué vértices son alcanzables? | [Recorridos](recorridos.md) |
| ¿Cuál es la mejor ruta? | [Caminos mínimos](caminos_minimos.md) |
| ¿Cómo conectar con costo mínimo? | [Árboles de expansión](arboles_de_expansion.md) |
| ¿Cómo ordenar dependencias? | [Orden topológico](orden_topologico.md) |
| ¿Cómo detectar grupos o particiones? | [Conectividad](conectividad.md) |

Esta tabla ayuda a evitar una confusión común: "grafos" no es un único algoritmo. Es una familia de problemas sobre el mismo tipo de modelo.

## Qué errores conviene evitar

Hay varios errores de encuadre que conviene desactivar desde el principio:

1. **Forzar un árbol cuando el dominio tiene ciclos o múltiples padres.**
2. **Forzar una secuencia cuando el problema real es de alcanzabilidad o red.**
3. **Olvidar la dirección de las aristas cuando el dominio la necesita.**
4. **Usar pesos sin aclarar qué representan.**
5. **Hablar de "camino más corto" sin especificar si se mide por cantidad de aristas o por peso total.**

## Resumen

Un grafo no es solo "muchos nodos conectados". Es un modelo general para problemas donde la relación entre entidades ya no puede imponerse como lista ni como jerarquía.

Las ideas que tienen que quedar instaladas son estas:

1. un grafo separa vértices y aristas;
2. dirección y peso cambian el significado del problema;
3. caminos, ciclos y conectividad forman el vocabulario base de toda la familia;
4. primero se fija el TAD y recién después se discute la representación.

## Ejercicios

```{exercise}
:label: ex-parte5-fundamentos-grafos-mini

Elegí un dominio real, por ejemplo una red de rutas o una red social, e indicá qué representarían sus vértices y aristas. Aclará además si el grafo debería ser dirigido, ponderado o ambos.
```

```{exercise}
:label: ex-parte5-fundamentos-grafos-modelado

Tomá el problema de correlatividades entre materias y justificá:

1. si el grafo debería ser dirigido o no;
2. si tendría sentido poner pesos;
3. qué significaría un ciclo dentro de ese dominio.
```

```{exercise}
:label: ex-parte5-fundamentos-grafos-familias

Inventá un problema donde una secuencia sea una mala abstracción y un árbol también resulte insuficiente, pero un grafo sí modele correctamente la situación. Explicá qué relación adicional aparece y por qué obliga a pasar a una red general.
```

## Próximo paso

Para seguir, conviene pasar a [Representación de grafos](representacion.md), donde el problema conceptual se transforma en estructura concreta.
