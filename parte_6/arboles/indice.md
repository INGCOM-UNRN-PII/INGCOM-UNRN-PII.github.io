---
title: "Árboles"
subtitle: "Índice de familia"
subject: Estructuras de Datos
description: Mapa de la familia de árboles para la parte 6.
---

(parte6-arboles)=
# Árboles


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

Esta familia reúne estructuras jerárquicas donde la **recursión estructural**, el **orden implícito** y la **altura** condicionan la eficiencia de las operaciones. Si en secuencias dominaba la posición y en diccionarios dominaba la clave, acá aparece otra idea: **la forma de la estructura determina el rendimiento**.

Los árboles son, quizás, la estructura no lineal más versátil de la computación. Actúan como puente entre el modelado de jerarquías naturales (archivos, organizaciones, expresiones) y la optimización de algoritmos de búsqueda y prioridad.

## La potencia del crecimiento logarítmico

La razón principal por la que estudiamos árboles es la **altura**. Un árbol bien balanceado nos permite "navegar" entre millones de elementos en apenas una decena de pasos. 

- En una secuencia lineal de $N$ elementos, encontrar uno puede costar $O(N)$.
- En un árbol balanceado de $N$ elementos, la altura es $O(\log N)$.
- **Intuición**: Con una altura de 30, un árbol binario puede direccionar más de mil millones de nodos.

:::{important}
El gran desafío de esta familia no es solo insertar o buscar, sino **mantener el árbol "corto"** (balanceado) para que la promesa del $O(\log N)$ se cumpla.
:::

## Mapa de la familia

| Orden | Página | Concepto central | Aplicación típica |
| :--- | :--- | :--- | :--- |
| 1 | [Fundamentos](fundamentos.md) | Jerarquía, altura y recorridos | Modelado de dominios jerárquicos |
| 2 | [Árboles binarios](arboles_binarios.md) | Estructura base (izq/der) | Árboles de sintaxis, expresiones |
| 3 | [BST](arboles_busqueda.md) | El orden como invariante | Índices de búsqueda en memoria |
| 4 | [Árboles balanceados](arboles_balanceados.md) | Autocorrección de altura | Sistemas de alto rendimiento (AVL, RB) |
| 5 | [Heaps](heaps.md) | Prioridad sobre orden total | Colas de prioridad, algoritmos de grafos |
| 6 | [Árboles B](arboles_b.md) | Estructuras para disco | Bases de datos y sistemas de archivos |

## Criterios de elección

¿Cuándo conviene saltar de una estructura lineal a un árbol?

| Si tu problema es... | Probablemente necesites... | Porque... |
| :--- | :--- | :--- |
| Representar una jerarquía (ej: carpetas) | [Árboles generales/binarios](fundamentos.md) | Reflejan la relación padre-hijo naturalmente. |
| Buscar elementos en un conjunto dinámico | [Árboles balanceados](arboles_balanceados.md) | Mantienen el costo $O(\log N)$ pase lo que pase. |
| Extraer siempre "el más importante" | [Heaps](heaps.md) | Son extremadamente eficientes para el acceso a la raíz. |
| Manejar volúmenes masivos de datos | [Árboles B](arboles_b.md) | Minimizan el acceso a disco leyendo bloques grandes. |

## Árboles y recursión

A diferencia de las secuencias, donde el lazo (`for`/`while`) es el rey, en los árboles la **recursión** es la herramienta natural. Un árbol se define como un nodo raíz conectado a otros *subárboles*. Esta definición fractal impregna todos los algoritmos que vamos a ver: desde los recorridos (in-order, pre-order, post-order) hasta las inserciones y rotaciones de balance.

## Qué deberías dominar al finalizar

Un estudiante que recorrió esta familia con éxito debería poder:

1. Identificar la altura y el grado de un árbol.
2. Realizar recorridos manuales sobre cualquier estructura arbórea.
3. Explicar la diferencia entre orden total (BST) y orden parcial (Heap).
4. Justificar por qué un BST puede degradarse a una lista y cómo lo evita un AVL.
5. Seleccionar la variante de árbol adecuada para un problema de almacenamiento persistente vs. volátil.

## Próximo paso

Empezamos por los conceptos base: [Fundamentos de árboles](fundamentos.md), donde definimos qué es un padre, qué es una hoja y por qué la altura es la métrica que nos quita el sueño.

## Ejercicios de verificación

1. Resolvé un caso mínimo usando la estructura/algoritmo del capítulo y documentá por qué esa elección es válida.
2. Construí un contraejemplo donde una elección alternativa falle (rendimiento o corrección).
3. Escribí una prueba corta en Java que verifique un invariante crítico.

## Cierre operativo

Este capítulo se considera dominado cuando podés explicar el modelo, implementarlo en Java y justificar la complejidad sin ambigüedades.

### Checklist de salida

- Podés describir el contrato de operaciones sin mencionar representación interna.
- Podés anticipar costo temporal/espacial del caso típico y peor caso.
- Podés detectar un anti-patrón y proponer una corrección concreta.

