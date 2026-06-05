---
title: "Árboles binarios"
subtitle: "A lo sumo dos hijos por nodo"
subject: Estructuras de Datos
description: Qué cambia cuando un árbol general se restringe a dos hijos por nodo y por qué esa forma sostiene varias estructuras posteriores.
---

(parte6-arboles-binarios)=
# Árboles binarios


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

El árbol binario es la forma más simple de introducir restricciones estructurales útiles. Limitar a dos hijos por nodo no alcanza para resolver todos los problemas, pero sí organiza gran parte del diseño posterior.

El valor del árbol binario no está solo en los problemas que modela directamente. También funciona como plataforma conceptual para:

- árboles de búsqueda,
- heaps,
- árboles de expresión,
- y varias representaciones recursivas compactas.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender qué cambia cuando un árbol general se restringe a dos hijos por nodo y por qué esa restricción es tan reusable.

**Prerrequisitos.** Conviene haber leído [Fundamentos de árboles](fundamentos.md), porque este capítulo construye sobre ese vocabulario.

**Desarrollo.** El capítulo presenta la estructura binaria, sus recorridos clásicos, sus representaciones típicas y varios casos de uso que preparan el terreno para BST y heaps.
:::

## Qué define a un árbol binario

Un árbol binario es un árbol donde cada nodo tiene a lo sumo:

- un hijo izquierdo,
- y un hijo derecho.

Eso instala varias preguntas nuevas:

- qué significa “izquierda” y “derecha”,
- cuándo importa distinguirlas,
- qué propiedades de forma aparecen,
- y cómo se representan eficientemente.

## Formas típicas

No todos los árboles binarios tienen la misma forma. Conviene distinguir al menos:

```{mermaid}
flowchart TD
    subgraph Lleno
        direction TB
        L1((A)) --> L2((B))
        L1 --> L3((C))
        L2 --> L4((D))
        L2 --> L5((E))
        L3 --> L6((F))
        L3 --> L7((G))
    end
    
    subgraph Completo
        direction TB
        C1((A)) --> C2((B))
        C1 --> C3((C))
        C2 --> C4((D))
        C2 --> C5((E))
        C3 --> C6((F))
    end
    
    subgraph Degenerado
        direction TB
        D1((A)) --> D2((B))
        D2 --> D3((C))
        D3 --> D4((D))
    end
```

| Forma | Idea básica |
| :--- | :--- |
| completo | todos los niveles salvo quizá el último están llenos |
| lleno | cada nodo interno tiene exactamente dos hijos |
| degenerado | cada nodo tiene a lo sumo un hijo y la forma se acerca a una lista |

Estas diferencias importan porque:

- la altura cambia,
- la representación cambia,
- y también cambia el costo de varias operaciones.

## Recorridos clásicos

En árboles binarios, los recorridos en profundidad se vuelven especialmente claros porque cada nodo tiene dos subárbolos distinguidos.

### Preorden

1. visitar raíz,
2. recorrer subárbol izquierdo,
3. recorrer subárbol derecho.

### Inorden

1. recorrer subárbol izquierdo,
2. visitar raíz,
3. recorrer subárbol derecho.

### Postorden

1. recorrer subárbol izquierdo,
2. recorrer subárbol derecho,
3. visitar raíz.

En un árbol binario cualquiera, estos recorridos son solo variantes de visita. En un BST, en cambio, el inorden adquiere un significado especial porque entrega claves ordenadas.

## Representación enlazada

La forma más natural de representar un árbol binario es con nodos que guardan:

- el dato,
- referencia a hijo izquierdo,
- referencia a hijo derecho.

```{code} java
:caption: Nodo binario simple

class Nodo<T> {
    private T dato;
    private Nodo<T> izquierdo;
    private Nodo<T> derecho;
}
```

Ventajas:

- muy flexible,
- clara para árboles no completos,
- fácil de extender para BST u otras variantes.

## Representación implícita en arreglo

Cuando el árbol binario tiene forma suficientemente regular, sobre todo si es completo o casi completo, puede representarse en arreglo.

La idea típica es:

- si un nodo está en la posición `i`,
- su hijo izquierdo está en `2i + 1`,
- y su hijo derecho en `2i + 2`.

```{mermaid}
block-beta
    columns 7
    block:Arr:7
        A0["[0]\nA"] A1["[1]\nB"] A2["[2]\nC"] A3["[3]\nD"] A4["[4]\nE"] A5["[5]\nF"] A6["[6]\n-"]
    end
    style Arr fill:#e3f2fd,stroke:#1e88e5
```
*(Si A está en `0`, su hijo izquierdo B está en `2(0)+1 = 1`, y su derecho C en `2(0)+2 = 2`)*.

Esto vuelve muy natural:

- acceder a padres e hijos por índice,
- evitar punteros explícitos,
- y aprovechar buena localidad de memoria.

Pero esa ventaja depende de la forma. Si el árbol está muy desbalanceado, el arreglo desperdicia espacio o vuelve incómodo el manejo.

## Casos de uso típicos

Los árboles binarios aparecen naturalmente en problemas como:

- árboles de expresión,
- representaciones jerárquicas simples,
- base conceptual de BST,
- base estructural de heaps.

### Árboles de expresión

Un operador puede quedar en un nodo interno y sus operandos en subárboles izquierdo y derecho.

### Base de BST

La forma binaria permite imponer una relación de orden:

- menor a la izquierda,
- mayor a la derecha.

### Base de heaps

La forma binaria completa y la representación en arreglo vuelven natural la implementación de colas de prioridad.

## Qué gana y qué limita esta restricción

La restricción “a lo sumo dos hijos” da:

- estructura simple,
- recorridos bien definidos,
- representación clara,
- reutilización algorítmica.

Pero también limita:

- árboles con alta ramificación natural,
- modelos donde la jerarquía no es binaria,
- estructuras donde conviene agrupar muchos hijos por nodo.

Por eso el árbol binario no reemplaza a todos los árboles. Es una especialización muy útil.

## Qué errores conviene evitar

1. **Pensar que cualquier árbol jerárquico real conviene forzarlo a binario.**
2. **Usar representación en arreglo sin mirar la forma del árbol.**
3. **Confundir recorrido inorden con “ordenado” en cualquier árbol binario.** Eso solo vale en BST.
4. **Olvidar que forma y altura siguen importando, incluso en esta variante.**

:::{warning}
Que una estructura sea binaria no implica automáticamente que sea de búsqueda ni que esté balanceada. Esas son propiedades adicionales.
:::

## Resumen

El árbol binario vale por dos razones:

1. como modelo jerárquico simple y muy expresivo,
2. como base para varias estructuras más potentes, como BST y heaps.

La distinción entre hijo izquierdo y derecho, más la posibilidad de representación enlazada o implícita, lo vuelven una forma especialmente reusable.

## Ejercicios

```{exercise}
:label: ex-parte6-arboles-binarios-mini

Explicá por qué un árbol binario completo puede representarse bien en arreglo, pero un árbol binario muy desbalanceado no aprovecha igual esa decisión.
```

```{exercise}
:label: ex-parte6-arboles-binarios-recorridos

Describí un caso donde un recorrido preorden resulte natural y otro donde un recorrido postorden resulte más adecuado. Justificá la diferencia.
```

## Próximo paso

Para seguir, conviene pasar a [Árboles binarios de búsqueda](arboles_busqueda.md), donde aparece una invariante de orden sobre esta forma estructural.
