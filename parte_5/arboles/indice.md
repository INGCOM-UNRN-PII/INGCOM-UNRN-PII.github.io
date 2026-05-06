---
title: "Árboles"
subtitle: "Índice de familia en revisión"
subject: Estructuras de Datos
description: Mapa de la familia de árboles para la parte 5.
---

(parte5-arboles)=
# Árboles

Esta familia reúne estructuras jerárquicas donde la recursión estructural, el orden implícito y la altura condicionan la eficiencia de las operaciones.

:::{note}
Conviene leer esta familia como una progresión: primero la idea general de árbol, después el caso binario, luego búsqueda, balance, prioridad y finalmente almacenamiento externo.
:::

## Recorrido sugerido

| Orden | Página | Rol en la familia |
| :--- | :--- | :--- |
| 1 | [Fundamentos de árboles](fundamentos.md) | Define jerarquía, altura y recorridos |
| 2 | [Árboles binarios](arboles_binarios.md) | Presenta el caso estructural más simple |
| 3 | [Árboles binarios de búsqueda](arboles_busqueda.md) | Instala búsqueda ordenada |
| 4 | [Árboles balanceados](arboles_balanceados.md) | Muestra cómo sostener eficiencia |
| 5 | [Heaps](heaps.md) | Cambia foco desde orden total a prioridad |
| 6 | [Árboles B](arboles_b.md) | Extiende el problema a almacenamiento externo |

## Comparación rápida

| Estructura | Fuerte principal | Trade-off central | Cuándo conviene |
| :--- | :--- | :--- | :--- |
| [Árboles binarios](arboles_binarios.md) | Modelo recursivo claro | No garantizan orden ni balance | Cuando importa la forma jerárquica |
| [BST](arboles_busqueda.md) | Búsqueda ordenada | Dependen de la altura | Cuando se necesitan consultas ordenadas |
| [Árboles balanceados](arboles_balanceados.md) | Garantías de altura | Mayor complejidad de implementación | Cuando no se puede tolerar degradación |
| [Heaps](heaps.md) | Acceso al mínimo o máximo | No sirven para búsquedas generales eficientes | Cuando domina la prioridad |
| [Árboles B](arboles_b.md) | Buen desempeño por bloques | Mayor complejidad estructural | Cuando los datos viven fuera de memoria principal |

## Criterios de uso

Esta familia debería ayudar a distinguir:

1. jerarquía estructural vs orden de búsqueda,
2. balance como necesidad y no como adorno,
3. prioridad vs orden total,
4. memoria principal vs almacenamiento externo.

## Cierre integrador sugerido

Un buen cierre para esta familia sería justificar qué árbol usar para:

- un índice ordenado en memoria,
- una cola de prioridad,
- un índice persistente en disco,
- una estructura que no debe degradarse con inserciones adversas.
