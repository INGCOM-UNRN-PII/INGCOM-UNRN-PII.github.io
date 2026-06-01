---
title: "Conjuntos disjuntos"
subtitle: "Partición dinámica de elementos"
subject: Estructuras de Datos
description: Cómo modelar unión y pertenencia a grupos mediante union-find y sus optimizaciones.
---

(parte5-conjuntos-disjuntos)=
# Conjuntos disjuntos

Los conjuntos disjuntos cierran esta familia con un caso particular: ya no interesa tanto buscar un valor asociado a una clave, sino saber a qué grupo pertenece cada elemento y cómo unir grupos de forma eficiente.

Es una estructura más especializada que un diccionario general, pero justamente ahí está su valor: cuando el problema dominante es **mantener particiones dinámicas**, la especialización paga.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender el TAD de conjuntos disjuntos y por qué `find` y `union` alcanzan para resolver problemas de conectividad incremental.

**Prerrequisitos.** Conviene haber leído [Tries](tries.md) y tener presente el problema de particionar un universo sin superposición.

**Desarrollo.** El capítulo presenta representantes, bosques de conjuntos disjuntos y optimizaciones como union by rank y path compression, conectándolos con algoritmos sobre grafos.
:::

## Qué problema resuelven

Un conjunto disjunto mantiene una partición del universo:

- cada elemento pertenece a un único grupo,
- los grupos no se superponen,
- y los grupos pueden unirse con el tiempo.

Las operaciones centrales son pocas:

| Operación | Qué resuelve |
| :--- | :--- |
| `find(x)` | devuelve el representante del grupo de `x` |
| `union(x, y)` | une los grupos de `x` e `y` |
| `sameSet(x, y)` | informa si comparten grupo |

La estructura no intenta resolver cualquier consulta imaginable. Se concentra en estas operaciones y por eso puede hacerlas muy bien.

## Representantes y partición

La idea de representante es simple:

- cada grupo tiene un identificador interno,
- dos elementos están en el mismo conjunto si tienen el mismo representante.

No importa qué elemento sea el representante, siempre que la estructura lo mantenga de forma consistente.

Esto permite pasar de una pregunta global:

- “¿en qué componente está este vértice?”,

a una pregunta operable:

- “¿qué devuelve `find(v)`?”.

## Bosques de conjuntos disjuntos

La implementación clásica usa un **bosque de árboles**:

- cada conjunto es un árbol,
- cada nodo apunta a un padre,
- la raíz actúa como representante.

```{code} java
:caption: Idea simplificada de parent array

int[] padre;
int[] rango;
```

La estructura concreta suele guardarse en arreglos:

- `padre[i]` dice quién es el padre de `i`,
- y si `i` es raíz, entonces representa su conjunto.

## Optimización 1: union by rank

Cuando se unen dos árboles, no conviene hacerlo arbitrariamente. Si siempre se cuelga un árbol grande debajo de uno chico, la estructura puede degradarse (transformarse en una lista enlazada).

La idea de **union by rank** es:

- mantener una medida aproximada de altura o tamaño (el *rango*),
- y colgar el árbol “menos profundo” debajo de la raíz del “más profundo”.

```{mermaid}
flowchart TD
    subgraph Mal
        direction TB
        R1((A)) --> R2((B))
        R2 --> N1((C))
        R2 --> N2((D))
    end
    
    subgraph Bien
        direction TB
        R3((B)) --> N3((C))
        R3 --> N4((D))
        R3 --> R4((A))
    end
    style R1 fill:#ffcdd2,stroke:#d32f2f
    style R3 fill:#c8e6c9,stroke:#388e3c
```

*(En la opción "Bien", colgar A debajo de B no aumenta la altura total del árbol, manteniendo búsquedas rápidas).*

## Optimización 2: path compression

Cada vez que se hace `find(x)`, se recorre una cadena de padres hasta la raíz. Esa búsqueda puede aprovecharse para comprimir el camino.

La idea es:

- si ya descubrí quién es la raíz de `x`,
- entonces puedo hacer que `x` y todos los nodos intermedios que visité apunten directo a esa raíz.

```{mermaid}
flowchart TD
    subgraph Antes de find E
        direction TB
        A1((A)) --> B1((B))
        B1 --> C1((C))
        C1 --> D1((D))
        D1 --> E1((E))
    end
    
    subgraph Despues de find E
        direction TB
        A2((A)) --> B2((B))
        A2 --> C2((C))
        A2 --> D2((D))
        A2 --> E2((E))
    end
```

```java
public int find(int x) {
    if (this.padre[x] != x) {
        // Asignación recursiva: comprime el camino
        this.padre[x] = find(this.padre[x]);
    }
    return this.padre[x];
}
```

Resultado:

- futuras búsquedas son más cortas,
- y la estructura se va “aplanando” con el uso.

## Por qué funciona tan bien

La combinación de:

- bosque de representantes,
- union by rank,
- y path compression

da una estructura extremadamente eficiente para el patrón de uso correcto.

No hace falta entrar acá en el detalle formal más fino de complejidad, pero sí conviene retener la intuición:

- una vez optimizada,
- las operaciones prácticas de `find` y `union`
- son muy baratas para tamaños grandes.

## Dónde aparece esta estructura

Los conjuntos disjuntos aparecen naturalmente en problemas como:

- componentes conexas,
- clustering incremental,
- agrupación dinámica,
- detección de conectividad en redes,
- algoritmo de Kruskal para árbol de expansión mínima.

En Kruskal, por ejemplo, la estructura responde constantemente:

- “¿estos dos vértices ya estaban conectados?”,
- “si agrego esta arista, ¿uno componentes distintas o creo un ciclo?”.

Ese tipo de consulta es exactamente el terreno donde union-find brilla.

## Qué no resuelve bien

Conviene no sobredimensionar la estructura. Un conjunto disjunto:

- no reemplaza un diccionario general,
- no sirve para recorrer elementos ordenados,
- no está pensado para listar eficientemente todo el contenido de cada conjunto,
- no es una estructura universal de búsqueda.

Su fuerza está en una tarea muy concreta: mantener particiones y uniones de manera eficiente.

## Qué errores conviene evitar

1. **Usarlo como si fuera un mapa general.**
2. **Ignorar la noción de representante.** Sin eso, la estructura pierde sentido.
3. **Olvidar union by rank y path compression en problemas grandes.**
4. **Elegirlo cuando en realidad el problema es de lookup por clave y no de partición.**

:::{warning}
Una estructura especializada gana potencia justamente porque renuncia a resolver otros problemas. Union-find no sirve para todo, pero para conectividad incremental es muy difícil de superar.
:::

## Resumen

Los conjuntos disjuntos son una estructura muy especializada, pero extremadamente potente cuando el problema central es mantener y fusionar componentes.

La idea clave es:

- representar cada conjunto por una raíz,
- responder pertenencia mediante `find`,
- y unir grupos con `union`,
- optimizando con union by rank y path compression.

## Ejercicios

```{exercise}
:label: ex-parte5-conjuntos-disjuntos-mini

Describí un problema donde haga falta unir grupos de elementos repetidamente y consultar si dos elementos quedaron en el mismo conjunto. Explicá por qué un diccionario general no modela tan bien ese uso.
```

```{exercise}
:label: ex-parte5-conjuntos-disjuntos-kruskal

Explicá por qué el algoritmo de Kruskal necesita una estructura como conjuntos disjuntos para evitar ciclos al construir un árbol de expansión mínima.
```

## Próximo paso

Para seguir, conviene pasar a [Árboles](../arboles/indice.md), donde la organización de los datos deja de ser lineal y pasa a ser jerárquica.
