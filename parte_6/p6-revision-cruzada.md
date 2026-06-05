---
title: "Revisión cruzada de estructuras"
subtitle: "Eligiendo la herramienta correcta"
subject: Estructuras de Datos
description: Resumen integrador de todas las familias de estructuras de la parte 6 y criterios de decisión final.
---

(parte6-revision-cruzada)=
# Revisión cruzada de estructuras


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

¡Llegaste al final de la Parte 6! A lo largo de estos capítulos, recorrimos el mundo de las estructuras de datos desde sus fundamentos físicos (localidad de memoria) hasta las redes complejas de los grafos. Ahora el desafío es integrar todo ese conocimiento para saber elegir la herramienta adecuada ante un problema real.

:::{note} Propósito de esta revisión
Este capítulo no introduce conceptos nuevos. Su función es **conectar** lo que aprendiste y ofrecerte un mapa de decisión rápido para tu vida profesional.
:::

## La Gran Tabla de Complejidad

A continuación, comparamos las operaciones principales de las familias más importantes en su caso promedio/esperado:

| Estructura | Búsqueda | Inserción | Borrado | Acceso por Índice | Orden |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Arreglo Dinámico** | $O(N)$ | $O(1)^*$ | $O(N)$ | $O(1)$ | No natural |
| **Lista Enlazada** | $O(N)$ | $O(1)^†$ | $O(1)^†$ | $O(N)$ | No natural |
| **Tabla Hash** | $O(1)$ | $O(1)$ | $O(1)$ | No | No |
| **Árbol Balanceado**| $O(\log N)$| $O(\log N)$| $O(\log N)$| No | **Total** |
| **Heap** | No | $O(\log N)$| $O(\log N)$| No | **Parcial** |
| **Trie** | $O(L)^‡$ | $O(L)^‡$ | $O(L)^‡$ | No | **Prefijo** |

\* *Costo amortizado al final.*
† *Asumiendo que ya se tiene la referencia al nodo.*
‡ *$L$ es la longitud de la clave.*

## Criterios de decisión rápidos

¿Qué estructura elijo? Seguí este flujo de preguntas:

1.  **¿Importa el orden de los elementos?**
    -   No, solo me importa si están: **Tabla Hash** o **Conjunto**.
    -   Sí, importa el orden de llegada: **Pila** o **Cola**.
    -   Sí, importa el orden de los valores (alfabético, numérico): **Árbol Balanceado** o **Diccionario Ordenado**.

2.  **¿Voy a buscar por una clave exacta?**
    -   Sí, y quiero velocidad máxima: **Tabla Hash**.
    -   Sí, pero también necesito rangos (ej: "todos entre A y B"): **Árbol Balanceado**.
    -   Sí, pero las claves son palabras y busco prefijos: **Trie**.

3.  **¿Necesito siempre el elemento "más importante"?**
    -   Sí, de forma dinámica: **Heap (Cola de Prioridad)**.

4.  **¿El problema es una red de relaciones arbitrarias?**
    -   Sí: **Grafo**.

## Relaciones entre familias

Es útil ver cómo las estructuras se "tocan" entre sí:
- Un **Árbol** es un **Grafo** conexo y acíclico.
- Un **Heap** se implementa casi siempre sobre un **Arreglo**.
- Un **Trie** es un tipo especial de **Árbol** donde las aristas tienen significado.
- Los **Conjuntos Disjuntos** se implementan como un **Bosque de Árboles**.

## Conclusión: El fin del recorrido

Dominar las estructuras de datos es como tener una caja de herramientas bien organizada. Sabés que no vas a usar un martillo para atornillar, ni vas a usar una lista enlazada para hacer búsquedas binarias frecuentes. 

El rendimiento real de un sistema depende de la combinación de tres factores:
1.  Elegir la **complejidad algorítmica** correcta ($O$).
2.  Respetar la **localidad de memoria** del hardware.
3.  **Medir** (Profiling) para validar que nuestras suposiciones eran ciertas.

## Ejercicio Integrador

```{exercise}
:label: ex-parte6-revision-final

Diseñá la estructura de datos para un sistema de una clínica:
1. Necesitás atender a los pacientes por orden de llegada, pero las emergencias pasan al frente.
2. Necesitás buscar la ficha de un paciente por su DNI en tiempo constante.
3. Necesitás listar a todos los pacientes que se atendieron entre dos fechas dadas.

¿Qué combinación de estructuras usarías para cumplir con los tres requisitos de forma eficiente?
```

## Próximo paso

¡Felicitaciones! Has completado la Parte 6. Ahora estás listo para enfrentar problemas de diseño de sistemas complejos y optimización de software de nivel profesional.

## Ejercicios de verificación

1. Resolvé un caso mínimo usando la estructura/algoritmo del capítulo y documentá por qué esa elección es válida.
2. Construí un contraejemplo donde una elección alternativa falle (rendimiento o corrección).
3. Escribí una prueba corta en Java que verifique un invariante crítico.

