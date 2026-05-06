---
title: "Heaps"
subtitle: "Árboles para prioridad, no para búsqueda general"
subject: Estructuras de Datos
description: Esqueleto editorial del capítulo sobre heaps.
---

(parte5-heaps)=
# Heaps

El heap reutiliza la forma de árbol binario, pero cambia completamente el propósito: ya no intenta mantener orden total para búsqueda, sino solo la información necesaria para extraer rápido el mínimo o el máximo.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender el heap como implementación dominante de colas de prioridad y distinguir su invariante de la de un BST.

**Prerrequisitos.** Conviene haber leído [Árboles balanceados](arboles_balanceados.md) y recordar [Colas de prioridad](../secuencias/colas_prioridad.md).

**Desarrollo.** El capítulo debería definir la propiedad de heap, mostrar su representación implícita en arreglo y conectar la estructura con heapsort y algoritmos voraces.
:::

## Qué garantiza un heap

Este bloque debería cubrir:

- min-heap y max-heap,
- raíz como mejor candidato,
- inserción y eliminación del extremo prioritario,
- diferencia con orden total de un BST.

## Qué implementaciones y usos aparecen

Este bloque debería cubrir:

- heap binario en arreglo,
- build-heap,
- heapsort,
- uso en colas de prioridad y algoritmos como Dijkstra o Prim.

## Resumen

El capítulo debería dejar instalada una idea central: un heap conserva exactamente la información que hace falta para prioridad y por eso logra eficiencia donde un árbol de búsqueda estaría resolviendo más de lo necesario.

## Ejercicios

```{exercise}
:label: ex-parte5-heaps-mini

Justificá por qué un heap es una buena implementación para una cola de prioridad, pero no necesariamente para consultas frecuentes de pertenencia o búsqueda arbitraria.
```

## Próximo paso

Para seguir, conviene pasar a [Árboles B](arboles_b.md), donde el foco pasa a almacenamiento por bloques y acceso externo.
