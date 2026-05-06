---
title: "Fundamentos de árboles"
subtitle: "Jerarquía, recursión y recorridos"
subject: Estructuras de Datos
description: Esqueleto editorial del capítulo sobre fundamentos de árboles.
---

(parte5-fundamentos-arboles)=
# Fundamentos de árboles

Los árboles introducen una organización distinta a la lineal: ya no importa solo qué elemento viene antes o después, sino qué relación jerárquica existe entre nodos, subárboles y recorridos posibles.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender el vocabulario estructural mínimo de árboles y la idea de recursión estructural que después atraviesa toda la familia.

**Prerrequisitos.** Conviene haber trabajado [Diccionarios y conjuntos](../diccionarios/indice.md), porque varias estructuras de esa familia luego se implementan con árboles.

**Desarrollo.** El capítulo debería definir raíz, hojas, altura y profundidad, presentar recorridos básicos y distinguir árboles generales de árboles binarios.
:::

## Qué define a un árbol

Este bloque debería cubrir:

- nodos, raíz, hojas y subárboles,
- nivel, profundidad y altura,
- grado de un nodo,
- relación entre definición recursiva y estructura jerárquica.

## Qué ideas reutiliza toda la familia

Este bloque debería cubrir:

- recorridos DFS y BFS,
- diferencia entre forma del árbol y orden almacenado,
- balance vs degradación,
- representación enlazada e implícita.

## Resumen

El capítulo debería dejar instalada una idea: pensar con árboles exige pasar de la intuición lineal a la intuición recursiva. Esa transición es la base para entender búsqueda, prioridad y balance.

## Ejercicios

```{exercise}
:label: ex-parte5-fundamentos-arboles-mini

Tomá una estructura jerárquica cotidiana, por ejemplo carpetas o dependencias de tareas, y describila con el vocabulario de árbol: raíz, hojas, altura y subárboles.
```

## Próximo paso

Para seguir, conviene pasar a [Árboles binarios](arboles_binarios.md), donde aparece la primera especialización estructural importante.
