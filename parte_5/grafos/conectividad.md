---
title: "Conectividad"
subtitle: "Componentes, partición y robustez"
subject: Estructuras de Datos
description: Cómo decidir qué vértices pertenecen a la misma componente y qué cambia cuando el grafo es dirigido o exige conectividad fuerte.
---

(parte5-conectividad)=
# Conectividad

La conectividad cierra la familia volviendo sobre una de las preguntas más generales de grafos: cómo se parte el problema en componentes, qué tan conectado está el sistema y qué algoritmos permiten detectar esa estructura.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender conectividad en grafos dirigidos y no dirigidos, y vincularla con componentes y partición del problema.

**Prerrequisitos.** Conviene haber leído [Recorridos](recorridos.md) y tener presente [Conjuntos disjuntos](../diccionarios/conjuntos_disjuntos.md), porque este capítulo reutiliza exploración por componentes y partición dinámica.

**Desarrollo.** El capítulo distingue conectividad en grafos no dirigidos y dirigidos, presenta componentes conexas y fuertemente conexas, compara recorridos con estructuras disjuntas y cierra con aplicaciones sobre robustez, segmentación y partición del problema.
:::

## Qué preguntas resuelve la conectividad

La conectividad responde preguntas como estas:

- ¿todo el grafo forma una sola pieza?;
- ¿qué vértices pertenecen a la misma componente?;
- ¿puedo llegar de un vértice a otro?;
- ¿cuántos bloques independientes tiene la red?;
- ¿qué cambia si las aristas son dirigidas?

En muchos problemas, antes de optimizar o recorrer mejor, primero hay que saber si el sistema es una sola red o varias subredes desconectadas.

## Componentes conexas en grafos no dirigidos

En un grafo no dirigido, una **componente conexa** es un conjunto maximal de vértices donde todos son alcanzables entre sí.

La intuición correcta es:

- dentro de una componente hay camino entre cualquier par;
- entre componentes distintas no hay camino.

Si se ejecuta DFS o BFS desde un vértice y se marca todo lo alcanzado, se obtiene exactamente una componente. Repetir el proceso desde los no visitados permite particionar todo el grafo.

```{code} java
:caption: Esquema para contar componentes conexas

int componentes = 0;

for (String v : grafo.vertices()) {
    if (!visitados.contains(v)) {
        dfs(grafo, v, visitados);
        componentes++;
    }
}
```

La idea importante no es el contador, sino la semántica:

- cada recorrido nuevo empieza una componente nueva.

## Qué significa que un grafo sea conexo

En un grafo no dirigido, decir que el grafo es **conexo** equivale a decir:

- tiene una sola componente conexa.

Eso tiene consecuencias prácticas fuertes:

- si una red física no es conexa, hay nodos aislados respecto del resto;
- si un mapa de ciudades no es conexo, algunas regiones no se alcanzan sin salir del sistema modelado;
- si un conjunto de usuarios se parte en varias componentes, la difusión no recorre toda la red.

## Digrafos: conectividad débil y fuerte

En grafos dirigidos la situación cambia, porque la alcanzabilidad deja de ser simétrica.

Conviene distinguir:

| Noción | Qué exige |
| :--- | :--- |
| conectividad débil | si se ignoran direcciones, el grafo queda conectado |
| conectividad fuerte | para cualquier par `u`, `v`, hay camino de `u` a `v` y de `v` a `u` |

La conectividad fuerte es mucho más exigente.

Ejemplo:

```text
A -> B -> C
```

Acá:

- `A` alcanza a `C`,
- pero `C` no alcanza a `A`.

Entonces el grafo no es fuertemente conexo.

## Componentes fuertemente conexas

Una **componente fuertemente conexa** (SCC) es un conjunto maximal de vértices donde todos se alcanzan mutuamente respetando dirección.

Las SCC importan porque detectan regiones del digrafo donde:

- la circulación es bidireccional por caminos,
- hay realimentación,
- o aparecen bloques muy acoplados.

En dependencias de software o de tareas, una SCC grande puede señalar:

- acoplamiento circular,
- diseño difícil de desacoplar,
- o una restricción mal planteada.

## Cómo se calculan

### Recorridos por componentes en grafos no dirigidos

Para componentes conexas comunes alcanza con:

- DFS,
- o BFS.

La lógica es simple y muy reusable:

1. elegir un vértice no visitado,
2. recorrer todo lo alcanzable,
3. registrar esa componente,
4. repetir.

### Algoritmos para SCC

En digrafos, detectar SCC requiere algo más elaborado. Dos nombres clásicos son:

- **Kosaraju**,
- **Tarjan**.

No hace falta bajar al detalle completo de implementación para fijar la idea conceptual:

- una SCC no se detecta mirando solo vecinos inmediatos;
- hace falta combinar recorridos e información estructural sobre el digrafo.

## Relación con conjuntos disjuntos

La estructura de [conjuntos disjuntos](../diccionarios/conjuntos_disjuntos.md) no reemplaza a DFS o BFS en todos los problemas de conectividad, pero sí sirve cuando la pregunta dominante es:

- ¿estos dos vértices quedaron en la misma componente según las uniones realizadas?

Eso aparece naturalmente en contextos como:

- construcción incremental de conectividad,
- Kruskal para árboles de expansión,
- seguimiento de componentes mientras se agregan aristas.

Conviene distinguir:

| Herramienta | Cuándo brilla |
| :--- | :--- |
| DFS / BFS | explorar una componente existente en un grafo dado |
| conjuntos disjuntos | mantener particiones bajo operaciones de unión |

## Dónde aparece

La conectividad aparece en problemas como:

### Redes y robustez

Si una red se parte en varias componentes, deja de funcionar como sistema único. Detectar eso permite:

- señalar cortes,
- ubicar nodos aislados,
- medir fragilidad estructural.

### Segmentación o clustering grueso

En algunos dominios alcanza con saber qué grupos están conectados y cuáles no, sin optimizar todavía rutas internas.

### Análisis de dependencias

En digrafos, una SCC puede señalar ciclos de dependencia o módulos demasiado acoplados.

## Qué errores conviene evitar

1. **Usar la intuición de grafo no dirigido en un digrafo.** Alcanzar no implica ser alcanzado.
2. **Confundir componente conexa con componente fuertemente conexa.**
3. **Pensar que conectividad es un detalle menor.** Muchas veces define si el problema debe resolverse en bloque o por partes.
4. **Creer que union-find reemplaza cualquier algoritmo de recorrido.**

:::{warning}
En grafos dirigidos, la pregunta “¿están conectados?” está incompleta si no se aclara si se habla de conectividad débil o fuerte.
:::

## Resumen

La conectividad decide cómo se parte un grafo en piezas con sentido algorítmico. En no dirigidos aparecen componentes conexas; en dirigidos, la distinción entre conectividad débil y fuerte cambia por completo el análisis.

Por eso este capítulo cierra la familia: antes de buscar mejores rutas o mejores costos, muchas veces primero hace falta saber si el sistema forma una sola red o varias.

## Ejercicios

```{exercise}
:label: ex-parte5-conectividad-mini

Describí un problema real donde sea importante detectar componentes separadas de una red. Indicá qué decisión práctica podría tomarse a partir de ese resultado.
```

```{exercise}
:label: ex-parte5-conectividad-fuerte

Explicá por qué en un grafo dirigido puede pasar que dos vértices pertenezcan a la misma componente débil, pero no a la misma componente fuertemente conexa.
```

## Próximo paso

Para seguir, conviene volver a [la portada de la parte](../indice.md) y revisar qué familias ya tienen un esqueleto editorial suficientemente claro como para avanzar hacia contenido pleno.
