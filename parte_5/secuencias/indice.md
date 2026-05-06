---
title: "Secuencias"
subtitle: "Índice de familia en revisión"
subject: Estructuras de Datos
description: Mapa de la familia de secuencias, con estado editorial actual y recorrido sugerido de la parte 5.
---

(parte5-secuencias)=
# Secuencias

Esta familia reúne las estructuras lineales donde el recorrido, el acceso posicional y la inserción o eliminación en extremos o posiciones arbitrarias son el problema central.

:::{note}
Conviene usar este índice como mapa comparativo. Primero se fija el TAD general de secuencia y después se baja a restricciones o implementaciones concretas.
:::

## Estado editorial actual

La familia sigue **en revisión**, pero ya no está toda en el mismo estado.

| Página | Estado actual | Rol |
| :--- | :--- | :--- |
| [Fundamentos de secuencias](fundamentos.md) | Desarrollo base disponible | Fija el TAD general, operaciones y trade-offs |
| [Arreglos](arreglos.md) | Esqueleto editorial | Primera implementación contigua |
| [Listas enlazadas](listas_enlazadas.md) | Esqueleto editorial | Contraste enlazado |
| [Pilas](pilas.md) y [Colas](colas.md) | Desarrollo base disponible | Restricciones clásicas de acceso |
| [Deques](deques.md) | Esqueleto editorial | Generalización sobre ambos extremos |
| [Colas de prioridad](colas_prioridad.md) | Esqueleto editorial | Puente hacia heaps |

## Recorrido sugerido

| Orden | Página | Rol en la familia |
| :--- | :--- | :--- |
| 1 | [Fundamentos de secuencias](fundamentos.md) | Define operaciones, posiciones y recorridos |
| 2 | [Arreglos](arreglos.md) | Instala la representación contigua |
| 3 | [Listas enlazadas](listas_enlazadas.md) | Contrasta con representación por nodos |
| 4 | [Pilas](pilas.md) y [Colas](colas.md) | Muestra restricciones de acceso sobre secuencias |
| 5 | [Deques](deques.md) | Amplía operaciones eficientes en ambos extremos |
| 6 | [Colas de prioridad](colas_prioridad.md) | Cierra la transición hacia heaps y algoritmos voraces |

## Comparación rápida

| Estructura | Fuerte principal | Costo dominante | Cuándo conviene |
| :--- | :--- | :--- | :--- |
| [Arreglos](arreglos.md) | Acceso aleatorio | Insertar o borrar en el medio | Cuando la consulta por índice domina |
| [Listas enlazadas](listas_enlazadas.md) | Inserción local | Búsqueda y acceso por posición | Cuando importa editar sin corrimientos masivos |
| [Pilas](pilas.md) | LIFO | Acceso restringido | Cuando el problema sigue la lógica de deshacer o backtracking |
| [Colas](colas.md) | FIFO | Acceso restringido | Cuando importa el orden de llegada |
| [Deques](deques.md) | Dos extremos | Gestión de ambos bordes | Cuando se necesita flexibilidad en frente y fondo |
| [Colas de prioridad](colas_prioridad.md) | Prioridad | Mantener orden parcial | Cuando el siguiente elemento no depende del tiempo de llegada |

## Qué ya conviene leer y qué todavía funciona como mapa

Hoy la lectura más útil de esta familia es:

1. leer completo [Fundamentos de secuencias](fundamentos.md), porque ya fija el marco conceptual;
2. usar el resto del índice como **hoja de desarrollo** para las implementaciones que faltan profundizar;
3. volver a este mapa a medida que cada capítulo pase de esqueleto a contenido pleno.

## Criterios de uso

Esta familia debería ayudar a distinguir:

1. acceso por índice vs acceso secuencial,
2. representación contigua vs enlazada,
3. secuencia general vs TAD con acceso restringido,
4. costo de insertar al principio, al medio o al final.

## Decisión rápida

Si el problema todavía no está bien encuadrado, conviene decidir en este orden:

1. si hace falta una **secuencia general** o un TAD más restringido;
2. si domina el **acceso por posición** o la **edición local**;
3. si el costo importante aparece en el **principio**, el **medio** o el **final**;
4. si el siguiente capítulo a leer debería ser [Arreglos](arreglos.md) o [Listas enlazadas](listas_enlazadas.md).

## Cierre integrador sugerido

Un buen cierre para esta familia sería comparar qué estructura conviene para:

- historial de navegación,
- cola de impresión,
- editor de texto con inserciones frecuentes,
- planificador con prioridades.

## Próximo paso

El siguiente paso natural es [Arreglos](arreglos.md): ahí la secuencia deja de ser solo contrato abstracto y pasa a verse cómo se sostiene con memoria contigua.
