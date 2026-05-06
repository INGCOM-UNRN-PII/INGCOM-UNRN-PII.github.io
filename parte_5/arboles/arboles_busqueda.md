---
title: "Árboles binarios de búsqueda"
subtitle: "Orden para buscar, insertar y recorrer"
subject: Estructuras de Datos
description: Esqueleto editorial del capítulo sobre árboles binarios de búsqueda.
---

(parte5-arboles-busqueda)=
# Árboles binarios de búsqueda

El BST agrega una idea potente sobre el árbol binario: si cada nodo respeta una relación de orden respecto de sus subárboles, la estructura deja de ser solo jerárquica y pasa a servir también para búsquedas eficientes.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender la invariante de orden de un BST y cómo esa invariante permite búsqueda, inserción, borrado y recorrido ordenado.

**Prerrequisitos.** Conviene haber leído [Árboles binarios](arboles_binarios.md), porque el BST se apoya sobre esa forma estructural.

**Desarrollo.** El capítulo debería presentar la propiedad de búsqueda, mostrar las operaciones básicas y dejar visible que la altura condiciona toda la eficiencia de la estructura.
:::

## Qué garantiza un BST

Este bloque debería cubrir:

- invariante de orden,
- búsqueda, inserción y borrado,
- recorrido inorden como recorrido ordenado,
- manejo de casos de borrado.

## Qué problema aparece de inmediato

Este bloque debería cubrir:

- dependencia del costo respecto de la altura,
- degradación a estructura casi lineal,
- necesidad posterior de balance,
- uso como base de diccionarios ordenados.

## Resumen

El capítulo debería dejar instalada una idea central: el BST es elegante porque con una sola invariante obtiene varias operaciones útiles, pero esa elegancia no alcanza si la altura se degrada demasiado.

## Ejercicios

```{exercise}
:label: ex-parte5-bst-mini

Mostrá con un ejemplo de inserciones por qué un BST puede degradarse hasta parecerse a una lista. Indicá qué operación se vuelve especialmente afectada.
```

## Próximo paso

Para seguir, conviene pasar a [Árboles balanceados](arboles_balanceados.md), donde aparece la respuesta al problema de altura.
