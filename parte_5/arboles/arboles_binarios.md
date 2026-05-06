---
title: "Árboles binarios"
subtitle: "A lo sumo dos hijos por nodo"
subject: Estructuras de Datos
description: Esqueleto editorial del capítulo sobre árboles binarios.
---

(parte5-arboles-binarios)=
# Árboles binarios

El árbol binario es la forma más simple de introducir restricciones estructurales útiles. Limitar a dos hijos por nodo no alcanza para resolver todos los problemas, pero sí organiza gran parte del diseño posterior.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender qué cambia cuando un árbol general se restringe a dos hijos por nodo y por qué esa restricción es tan reusable.

**Prerrequisitos.** Conviene haber leído [Fundamentos de árboles](fundamentos.md), porque este capítulo construye sobre ese vocabulario.

**Desarrollo.** El capítulo debería presentar la estructura binaria, sus recorridos clásicos y sus representaciones típicas, preparando el terreno para BST y heaps.
:::

## Qué define a un árbol binario

Este bloque debería cubrir:

- hijo izquierdo e hijo derecho,
- árboles completos, llenos y degenerados,
- recorridos preorden, inorden y postorden,
- relación entre forma y costo.

## Qué usos y variantes aparecen

Este bloque debería cubrir:

- representación enlazada,
- representación implícita en arreglo para casos completos,
- árboles de expresión,
- base conceptual para búsqueda y prioridad.

## Resumen

El capítulo debería dejar clara una idea: el árbol binario no vale solo por sí mismo; funciona como plataforma conceptual para varias estructuras más específicas.

## Ejercicios

```{exercise}
:label: ex-parte5-arboles-binarios-mini

Explicá por qué un árbol binario completo puede representarse bien en arreglo, pero un árbol binario muy desbalanceado no aprovecha igual esa decisión.
```

## Próximo paso

Para seguir, conviene pasar a [Árboles binarios de búsqueda](arboles_busqueda.md), donde aparece una invariante de orden sobre esta forma estructural.
