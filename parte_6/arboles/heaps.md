---
title: "Heaps"
subtitle: "Árboles para prioridad, no para búsqueda general"
subject: Estructuras de Datos
description: Cómo un árbol binario casi completo permite implementar colas de prioridad de forma eficiente usando un arreglo.
---

(parte5-heaps)=
# Heaps

El heap reutiliza la forma de árbol binario, pero cambia completamente el propósito: ya no intenta mantener orden total para búsqueda, sino solo la información necesaria para extraer rápido el mínimo o el máximo.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender el heap como implementación dominante de colas de prioridad y distinguir su invariante de la de un BST.

**Prerrequisitos.** Conviene haber leído [Árboles balanceados](arboles_balanceados.md) y recordar [Colas de prioridad](../secuencias/colas_prioridad.md).

**Desarrollo.** El capítulo define la propiedad de heap, explica por qué exige forma casi completa, presenta su representación implícita en arreglo y la conecta con colas de prioridad, `buildHeap`, heapsort y algoritmos voraces.
:::

## Qué garantiza un heap

Un heap binario mantiene dos ideas al mismo tiempo:

1. una **restricción de forma**: el árbol es completo o casi completo;
2. una **restricción de orden local**: cada nodo domina a sus hijos según una prioridad.

En un **min-heap**:

- cada nodo es menor o igual que sus hijos,
- por lo tanto, la raíz guarda el mínimo.

En un **max-heap**:

- cada nodo es mayor o igual que sus hijos,
- por lo tanto, la raíz guarda el máximo.

Esa propiedad alcanza para:

- consultar rápido el mejor candidato,
- insertar nuevos elementos,
- quitar repetidamente el extremo prioritario.

## Qué no garantiza

El error más común con heaps es pedirles algo que no prometen.

Un heap:

- **no** mantiene orden total,
- **no** permite recorrido ordenado natural,
- **no** es una buena estructura para búsqueda arbitraria por clave.

La relación entre nodos hermanos puede ser cualquiera. Lo único importante es que cada padre domine a sus hijos. Eso basta para prioridad, pero no para responder consultas generales de orden como en un BST.

:::{important}
Un heap conserva exactamente la información necesaria para prioridad. No más.
:::

## Por qué la forma importa tanto

La propiedad de heap sola no alcanza. Hace falta además que el árbol tenga forma casi completa para poder representarlo eficientemente en arreglo.

Si se guarda por niveles en un arreglo, para un índice `i` valen estas relaciones:

| Relación | Fórmula típica |
| :--- | :--- |
| padre | `(i - 1) / 2` |
| hijo izquierdo | `2 * i + 1` |
| hijo derecho | `2 * i + 2` |

Esta representación implícita:

- evita punteros explícitos,
- usa memoria de forma compacta,
- y hace muy naturales las operaciones de subir y bajar elementos.

## Operaciones básicas

En un heap binario aparecen tres operaciones dominantes:

| Operación | Idea | Costo típico |
| :--- | :--- | :--- |
| `findMin()` / `findMax()` | leer la raíz | O(1) |
| `insert(x)` | agregar al final y reubicar hacia arriba | O(log n) |
| `deleteMin()` / `deleteMax()` | reemplazar la raíz y reubicar hacia abajo | O(log n) |

La razón del costo logarítmico es estructural: los ajustes solo recorren un camino desde una hoja hacia la raíz o desde la raíz hacia una hoja.

```{code} java
:caption: Esquema típico de inserción en un min-heap

public void insert(int valor) {
    this.datos.add(valor);
    this.subir(this.datos.size() - 1);
}
```

La operación `subir` compara el nuevo elemento con su padre e intercambia mientras viole la propiedad de heap.

## Build-heap: construir mejor que insertar uno por uno

Si se tiene un arreglo completo de datos, una estrategia ingenua sería insertar cada elemento en un heap vacío. Funciona, pero no es la forma más eficiente.

`buildHeap` construye el heap reacomodando desde los últimos nodos internos hacia arriba. La idea es:

1. tomar una representación casi completa,
2. aplicar hundimiento (`heapify`) desde abajo,
3. restaurar la propiedad de heap en toda la estructura.

El resultado importante es que `buildHeap` puede hacerse en O(n), no en O(n log n).

## Heapsort

El heapsort usa un heap para ordenar:

1. se construye el heap,
2. se extrae repetidamente el extremo prioritario,
3. cada extracción deja el siguiente candidato en la raíz.

Sus ventajas típicas son:

- no necesita estructuras auxiliares grandes,
- tiene complejidad O(n log n),
- explota una estructura muy regular.

Sus límites también importan:

- no es estable por defecto,
- suele tener peor localidad práctica que otras alternativas muy usadas,
- resuelve orden total aunque a veces el problema solo pedía prioridad dinámica.

## Heap vs BST vs cola de prioridad simple

Conviene comparar estructuras por contrato:

| Estructura | Qué optimiza | Qué no ofrece naturalmente |
| :--- | :--- | :--- |
| heap | extraer mínimo o máximo | búsqueda arbitraria y recorrido ordenado |
| BST balanceado | búsqueda y orden dinámico | prioridad extrema tan directa como raíz única |
| cola de prioridad con arreglo desordenado | inserción muy simple | extracción eficiente del mejor |

La pregunta correcta no es “qué estructura es más poderosa”, sino “qué información hace falta preservar”.

## Dónde aparece

Los heaps aparecen detrás de:

- implementaciones dominantes de colas de prioridad,
- planificación de eventos,
- simulaciones discretas,
- selección repetida del mejor candidato,
- algoritmos voraces como Dijkstra o Prim,
- heapsort.

En todos esos casos se repite el mismo patrón:

- siempre importa el mejor elemento actual,
- pero no hace falta mantener toda la colección ordenada.

## Qué errores conviene evitar

1. **Confundir propiedad de heap con orden total.**
2. **Usar heap cuando el problema necesita búsquedas arbitrarias frecuentes.**
3. **Olvidar la restricción de forma casi completa.**
4. **Pensar que “árbol” implica automáticamente punteros enlazados.** En heaps, el arreglo suele ser la representación natural.

:::{warning}
Si el problema requiere saber dónde está cualquier clave, un heap suele estar resolviendo el problema equivocado.
:::

## Resumen

El heap es una especialización muy precisa:

- usa la forma de árbol binario casi completo,
- mantiene una invariante de prioridad local,
- y aprovecha una representación implícita en arreglo.

Por eso logra una implementación excelente de colas de prioridad. No compite con BST por búsqueda general ni con hashing por pertenencia. Resuelve otro problema.

## Ejercicios

```{exercise}
:label: ex-parte5-heaps-mini

Justificá por qué un heap es una buena implementación para una cola de prioridad, pero no necesariamente para consultas frecuentes de pertenencia o búsqueda arbitraria.
```

```{exercise}
:label: ex-parte5-heaps-buildheap

Explicá por qué `buildHeap` no se piensa igual que insertar `n` elementos uno por uno. ¿Qué aprovecha de la forma casi completa del árbol?
```

## Próximo paso

Para seguir, conviene pasar a [Árboles B](arboles_b.md), donde el foco pasa a almacenamiento por bloques y acceso externo.
