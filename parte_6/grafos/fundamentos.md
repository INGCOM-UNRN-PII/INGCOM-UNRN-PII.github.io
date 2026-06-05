---
title: "Fundamentos de grafos"
subtitle: "Relaciones generales entre vértices"
subject: Estructuras de Datos
description: Qué problemas modelan los grafos, qué significan vértices, aristas, caminos y componentes, y por qué esta familia exige pensar más allá de listas y árboles.
---

(parte6-fundamentos-grafos)=
# Fundamentos de grafos


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

Los grafos cierran la parte con la familia más general: cuando ni la linealidad ni la jerarquía alcanzan para modelar el problema, hace falta pensar en **vértices, aristas, caminos y conectividad**.

En una secuencia el problema dominante era el orden. En un árbol, la jerarquía. En un grafo, en cambio, el centro pasa a ser la **red de relaciones**: quién está conectado con quién, por qué tipo de vínculo y con qué costo o dirección.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Instalar el vocabulario mínimo de grafos para poder hablar después de representación, recorridos y algoritmos clásicos.

**Prerrequisitos.** Conviene haber trabajado [Árboles](../arboles/indice.md), porque ayuda a contrastar jerarquía estricta con relaciones arbitrarias.

**Desarrollo.** El capítulo define vértices y aristas, distingue tipos de grafo, fija el lenguaje de caminos, ciclos y conectividad, y cierra mostrando qué preguntas abre esta familia para representación y recorridos.
:::

## El nacimiento de los grafos: Los Puentes de Königsberg

La teoría de grafos nació de un problema de ingeniería civil. En la ciudad de Königsberg, siete puentes conectaban dos islas con las orillas de un río. Los ciudadanos se preguntaban: *¿Es posible caminar por la ciudad cruzando cada puente exactamente una vez y volver al punto de partida?*

En 1736, Leonhard Euler demostró que era imposible. Su genialidad no fue solo la respuesta, sino el **método**: eliminó los detalles irrelevantes (el tamaño de las islas, la forma de los puentes) y representó el problema como puntos (tierras) y líneas (puentes). Había inventado el primer grafo.

## Anatomía de un grafo

Un **grafo** es una construcción matemática que modela un conjunto de entidades y sus interconexiones.

$$G = (V, E)$$

- **$V$ (Vértices):** Los nodos o entidades del sistema.
- **$E$ (Aristas):** Los vínculos o relaciones entre los nodos.

### Ejemplos de modelado

| Dominio | Vértices (Nodos) | Aristas (Vínculos) |
| :--- | :--- | :--- |
| **Infraestructura** | Ciudades o Subestaciones | Rutas o Cables eléctricos |
| **Software** | Clases o Funciones | Herencia o Llamadas de función |
| **Social** | Usuarios | Amistad, "Follow" o Bloqueo |
| **Web** | URLs | Hipervínculos |

## Dimensiones de un Grafo

Para elegir el algoritmo correcto, primero debemos clasificar nuestro grafo según tres dimensiones principales:

### 1. Dirección: ¿El vínculo es mutuo?

```{mermaid}
flowchart LR
    subgraph NoDirigido [No Dirigido]
        direction LR
        A((A)) --- B((B))
        B --- C((C))
    end
    
    subgraph Dirigido [Dirigido (Dígrafo)]
        direction LR
        D((D)) --> E((E))
        E --> F((F))
        F --> D
    end
    
    style A fill:#e3f2fd,stroke:#1565c0
    style B fill:#e3f2fd,stroke:#1565c0
    style C fill:#e3f2fd,stroke:#1565c0
    style D fill:#f1f8e9,stroke:#33691e
    style E fill:#f1f8e9,stroke:#33691e
    style F fill:#f1f8e9,stroke:#33691e
```

- **No dirigido:** La relación es simétrica. Si $A$ está conectado con $B$, $B$ está conectado con $A$. Ejemplo: Un cable de red entre dos PCs.
- **Dirigido (Dígrafo):** La relación tiene sentido. $A \to B$ no implica $B \to A$. Ejemplo: Un seguidor en Twitter o una calle contramano.

### 2. Peso: ¿Las relaciones tienen costo?

```{mermaid}
flowchart LR
    subgraph NoPonderado [No Ponderado]
        direction LR
        A((A)) --- B((B))
    end
    
    subgraph Ponderado [Ponderado]
        direction LR
        C((C)) ---|5 km| D((D))
        D ---|12 km| E((E))
    end
    
    style A fill:#e3f2fd,stroke:#1565c0
    style B fill:#e3f2fd,stroke:#1565c0
    style C fill:#fff3e0,stroke:#e65100
    style D fill:#fff3e0,stroke:#e65100
    style E fill:#fff3e0,stroke:#e65100
```

- **No ponderado:** Todas las conexiones son iguales. Solo importa si existe o no el vínculo.
- **Ponderado:** Cada arista tiene un **peso** (costo, distancia, capacidad). Ejemplo: Una ruta con una distancia en kilómetros o una latencia en milisegundos.

### 3. Densidad: ¿Cuántas conexiones hay?
Esta dimensión es clave para la eficiencia en memoria:
- **Disperso (Sparse):** La mayoría de los pares de nodos no tienen conexión. La cantidad de aristas $|E|$ es mucho menor que $|V|^2$.
- **Denso (Dense):** Casi todos están conectados con todos. $|E|$ se acerca a $|V|^2$.

## Conceptos Fundamentales

### Adyacencia e Incidencia
Dos vértices son **adyacentes** si hay una arista que los une directamente. Decimos que una arista es **incidente** a los vértices que conecta.

### Grado (Degree)
- En grafos no dirigidos, el **grado** de un vértice es la cantidad de aristas conectadas a él.
- En grafos dirigidos, se divide en **grado de entrada** (in-degree) y **grado de salida** (out-degree). Un nodo con grado de salida 0 se llama *sumidero* (sink), y uno con grado de entrada 0 es una *fuente* (source).

### Caminos y Ciclos
- **Camino:** Una secuencia de vértices donde cada par consecutivo es adyacente.
- **Camino Simple:** Un camino donde no se repiten vértices.
- **Ciclo:** Un camino que empieza y termina en el mismo vértice sin repetir otros nodos.

### Conectividad
- Un grafo no dirigido es **conexo** si hay un camino entre cualquier par de vértices. Si no, se divide en **componentes conexas** (islas aisladas).
- En grafos dirigidos, hablamos de **Fuertemente Conexo** si existe un camino de ida y vuelta entre cualquier par de nodos respetando las flechas.

## Grafos como Tipo Abstracto de Datos (TAD)

Antes de programar, definimos qué operaciones mínimas debe soportar nuestro Grafo:

```java
public interface Grafo<V> {
    void agregarVertice(V vertice);
    void conectar(V origen, V destino);
    void conectar(V origen, V destino, double peso); // Para ponderados
    
    boolean sonAdyacentes(V v1, V v2);
    Iterable<V> obtenerVecinos(V v);
    
    int cantidadVertices();
    int cantidadAristas();
}
```

Este contrato es agnóstico: no nos dice si usaremos una matriz gigante de booleanos o una red compleja de punteros. Esas decisiones las tomaremos en el capítulo de [Representación](representacion.md).

## Resumen

El grafo es la estructura de datos definitiva por su generalidad. Permite modelar mundos donde las reglas de "arriba/abajo" o "antes/después" no existen. 

1. **Vértices** son objetos, **Aristas** son relaciones.
2. La **dirección** define si la relación es simétrica o no.
3. El **peso** permite optimizar costos.
4. La **conectividad** nos dice si la red es una sola pieza o está fragmentada.

## Ejercicios

```{exercise}
:label: ex-parte6-fundamentos- Königsberg

Investigá el problema de los Siete Puentes de Königsberg. ¿Por qué Euler determinó que era imposible? ¿Qué condición deben cumplir los grados de los vértices para que exista un camino que recorra todas las aristas sin repetir?
```

```{exercise}
:label: ex-parte6-fundamentos-modelado-social

Diseñá un modelo de grafo para una red social como LinkedIn. 
1. ¿Los vértices son personas o empresas? 
2. ¿Es dirigido o no dirigido? 
3. ¿Qué representaría el peso de una arista?
```

```{exercise}
:label: ex-parte6-fundamentos-ciclos

En un grafo que representa dependencias de paquetes de software (A depende de B), ¿qué significaría la presencia de un ciclo? ¿Es deseable? Justificá tu respuesta.
```

## Próximo paso

Con los conceptos claros, es hora de bajar a la tierra: [Representación de grafos](representacion.md). Veremos cómo guardar estos dibujos en la memoria de una computadora de la manera más eficiente posible.
