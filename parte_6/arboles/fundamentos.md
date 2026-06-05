---
title: "Fundamentos de árboles"
subtitle: "Jerarquía, recursión y recorridos"
subject: Estructuras de Datos
description: Vocabulario base para trabajar con árboles y entender por qué la estructura jerárquica exige otra intuición que listas o arreglos.
---

(parte6-fundamentos-arboles)=
# Fundamentos de árboles


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

Los árboles introducen una organización distinta a la lineal: ya no importa solo qué elemento viene antes o después, sino qué relación jerárquica existe entre nodos, subárboles y recorridos posibles.

Pensar con árboles exige dejar atrás una intuición muy arraigada: en una secuencia se avanza de izquierda a derecha; en un árbol se entra a una estructura donde cada nodo puede abrir varios caminos. Eso vuelve natural una idea que en esta familia aparece todo el tiempo: **la recursión estructural**.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender el vocabulario estructural mínimo de árboles y la idea de recursión estructural que después atraviesa toda la familia.

**Prerrequisitos.** Conviene haber trabajado [Diccionarios y conjuntos](../diccionarios/indice.md), porque varias estructuras de esa familia luego se implementan con árboles.

**Desarrollo.** El capítulo define raíz, hojas, altura y profundidad, presenta recorridos básicos y distingue árboles generales de árboles binarios.
:::

## Qué define a un árbol

Un árbol es una estructura jerárquica compuesta por nodos conectados por relaciones padre-hijo.

El vocabulario mínimo incluye:

```{mermaid}
flowchart TD
    R((Raíz)) --> N1((Padre/Hijo))
    R --> N2((Hoja))
    R --> N3((Padre))
    N1 --> H1((Hoja))
    N1 --> H2((Hoja))
    N3 --> H3((Hoja))
    
    subgraph Subárbol
        N1
        H1
        H2
    end
    
    style R fill:#bbdefb,stroke:#0288d1
    style N2 fill:#e1bee7,stroke:#512da8
    style H1 fill:#e1bee7,stroke:#512da8
    style H2 fill:#e1bee7,stroke:#512da8
    style H3 fill:#e1bee7,stroke:#512da8
```

| Término | Idea básica |
| :--- | :--- |
| raíz | nodo inicial del árbol |
| hijo | nodo descendiente directo |
| padre | nodo del cual depende un hijo |
| hoja | nodo sin hijos |
| subárbol | árbol formado por un nodo y todos sus descendientes |
| nivel o profundidad | distancia desde la raíz |
| altura | longitud del camino más largo hasta una hoja |

Estas definiciones importan porque no son solo descriptivas. Muchas propiedades algorítmicas dependen de:

- cuántos hijos puede tener cada nodo,
- qué altura tiene la estructura,
- y cómo se recorre.

## Por qué aparecen naturalmente

Los árboles modelan bien problemas como:

- carpetas y subcarpetas,
- organigramas,
- expresiones aritméticas,
- índices jerárquicos,
- dominios donde cada elemento se subdivide en partes.

La intuición común es:

- un nodo representa una unidad,
- sus hijos representan refinamientos, dependencias o componentes,
- y el árbol completo representa la organización global.

## Recursión estructural

Una de las ideas más importantes de esta familia es que muchos algoritmos sobre árboles se expresan mejor de forma recursiva.

Eso ocurre porque un árbol está hecho de subárboles del mismo tipo. Por ejemplo:

- el tamaño de un árbol depende del tamaño de sus subárboles,
- la altura de un árbol depende de la altura de sus subárboles,
- un recorrido visita un nodo y luego recorre sus subárboles.

```{code} java
:caption: Idea recursiva de tamaño de un árbol

int tamanio(Nodo nodo) {
    if (nodo == null) {
        return 0;
    }
    return 1 + tamanio(nodo.izquierdo()) + tamanio(nodo.derecho());
}
```

Aunque la implementación concreta cambie, esta forma de pensar reaparece en casi toda la familia.

## Forma, altura y costo

En árboles, la forma no es un detalle estético. La forma condiciona costo.

Dos estructuras con la misma cantidad de nodos pueden comportarse muy distinto si:

- una tiene altura baja,
- y la otra está casi degenerada como una lista.

Esto importa porque muchas operaciones dependen de la altura:

- buscar,
- insertar,
- borrar,
- o alcanzar una hoja.

Cuanto más baja y controlada sea la altura, más razonable suele ser el costo.

## Recorridos básicos

Un árbol puede recorrerse de varias maneras. Dos familias de recorrido aparecen una y otra vez:

### Recorridos en profundidad

Exploran un camino hacia abajo antes de volver:

- preorden,
- inorden,
- postorden.

### Recorrido por anchura

Explora por niveles. Suele apoyarse en una cola y visitar primero nodos cercanos a la raíz.

Cada recorrido comunica algo distinto:

| Recorrido | Uso típico |
| :--- | :--- |
| preorden | procesar primero la raíz |
| inorden | recorrer ordenadamente en BST |
| postorden | procesar hijos antes del padre |
| por niveles | analizar forma o distancia desde la raíz |

## Árbol general vs árbol binario

No todos los árboles tienen la misma restricción estructural.

Un árbol general:

- puede tener muchos hijos por nodo.

Un árbol binario:

- tiene a lo sumo dos hijos por nodo.

Esa restricción parece pequeña, pero cambia bastante:

- simplifica recorridos y representación,
- prepara el terreno para BST y heaps,
- y vuelve muy útil la noción de hijo izquierdo e hijo derecho.

## Representación enlazada e implícita

En esta familia reaparecen dos grandes estrategias de representación:

### Representación enlazada

Cada nodo guarda referencias a sus hijos.

Ventajas:

- flexible,
- natural para árboles no completos,
- fácil de entender conceptualmente.

### Representación implícita

Se aprovecha una regularidad de la forma para guardar el árbol en arreglo.

Ventajas:

- muy útil en árboles binarios completos,
- buena localidad de memoria.

Desventajas:

- no sirve igual de bien cuando la forma está muy desbalanceada.

## Qué errores conviene evitar

1. **Pensar un árbol como si fuera una lista con más punteros.**
2. **Ignorar la altura.** Suele ser la diferencia entre una estructura razonable y una que se degrada.
3. **Elegir recorrido sin justificar qué información se quiere obtener.**
4. **Olvidar que la forma del árbol impacta sobre la complejidad.**

:::{warning}
En árboles no alcanza con contar nodos. También hace falta mirar cómo están organizados.
:::

## Resumen

Pensar con árboles exige pasar de la intuición lineal a la intuición recursiva. Esa transición es la base para entender:

- jerarquía,
- recorridos,
- altura,
- representación,
- y después búsqueda, prioridad y balance.

Toda la familia se apoya en estas ideas. Por eso conviene fijarlas antes de entrar a variantes más específicas.

## Ejercicios

```{exercise}
:label: ex-parte6-fundamentos-arboles-mini

Tomá una estructura jerárquica cotidiana, por ejemplo carpetas o dependencias de tareas, y describila con el vocabulario de árbol: raíz, hojas, altura y subárboles.
```

```{exercise}
:label: ex-parte6-fundamentos-arboles-recorridos

Pensá un problema donde importe procesar un nodo antes que sus hijos, y otro donde importe procesar primero los hijos. Indicá qué tipo de recorrido elegirías en cada caso.
```

## Próximo paso

Para seguir, conviene pasar a [Árboles binarios](arboles_binarios.md), donde aparece la primera especialización estructural importante.
