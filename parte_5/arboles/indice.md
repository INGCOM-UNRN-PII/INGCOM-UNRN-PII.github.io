---
title: "Árboles"
subtitle: "Índice de familia"
subject: Estructuras de Datos
description: Mapa de la familia de árboles para la parte 5.
---

(parte5-arboles)=
# Árboles

Esta familia reúne estructuras jerárquicas donde la recursión estructural, el orden implícito y la altura condicionan la eficiencia de las operaciones. Si en secuencias dominaba la posición y en diccionarios dominaba la clave, acá aparece otra idea: **la forma de la estructura también importa**.

Los árboles sirven como puente entre varios problemas:

- jerarquía pura,
- búsqueda ordenada,
- extracción por prioridad,
- almacenamiento eficiente por bloques.

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

## Qué preguntas debería ayudar a responder esta familia

Al terminar esta familia, conviene poder responder con criterio:

1. cuándo el problema es jerárquico y no lineal,
2. cuándo un árbol sirve para representar forma y cuándo sirve para buscar,
3. cuándo la altura pasa a ser el cuello de botella,
4. cuándo hace falta balance,
5. cuándo una cola de prioridad pide heap y no BST,
6. cuándo el almacenamiento externo cambia la estructura adecuada.

## Decisiones rápidas

Si el problema dominante es:

- **modelar jerarquía o recursión estructural**, conviene empezar por [árboles binarios](arboles_binarios.md),
- **buscar y recorrer ordenado**, conviene mirar [BST](arboles_busqueda.md),
- **garantizar altura razonable**, conviene mirar [árboles balanceados](arboles_balanceados.md),
- **extraer el mínimo o máximo repetidamente**, conviene mirar [heaps](heaps.md),
- **trabajar por bloques o páginas**, conviene mirar [árboles B](arboles_b.md).

## Conexiones con el resto de la parte

Esta familia conecta varias piezas ya instaladas:

- reutiliza el lenguaje de [análisis de algoritmos](../algoritmos.md),
- sirve de soporte para [diccionarios ordenados](../diccionarios/diccionarios_ordenados.md),
- y prepara intuiciones para algoritmos sobre grafos donde reaparecen recorridos, prioridades y estructuras jerárquicas.

## Cierre integrador sugerido

Un buen cierre para esta familia sería justificar qué árbol usar para:

- un índice ordenado en memoria,
- una cola de prioridad,
- un índice persistente en disco,
- una estructura que no debe degradarse con inserciones adversas.
