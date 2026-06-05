---
title: "Diccionarios ordenados"
subtitle: "Búsqueda por clave con orden y rangos"
subject: Estructuras de Datos
description: Qué agrega un diccionario ordenado frente a una tabla hash y qué estructuras sostienen ese contrato.
---

(parte6-diccionarios-ordenados)=
# Diccionarios ordenados


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

No todos los problemas de búsqueda por clave se resuelven bien con hashing. Cuando importa recorrer en orden, consultar rangos o encontrar predecesores y sucesores, el orden pasa a ser parte del contrato.

Una tabla hash puede ser excelente para responder “¿está esta clave?”. Pero si la pregunta cambia a:

- “¿cuál es la menor clave?”,
- “¿qué claves hay entre `a` y `b`?”,
- “¿cuál viene justo antes de esta?”,

entonces el problema ya no es solo de lookup. Es también un problema de **orden**.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender qué agrega un diccionario ordenado frente a una tabla hash y qué costo implica mantener ese orden.

**Prerrequisitos.** Conviene haber leído [Tablas hash](tablas_hash.md) y tener presente la diferencia entre acceso promedio rápido y garantías estructurales.

**Desarrollo.** El capítulo presenta operaciones ordenadas, conecta estas estructuras con BST y árboles balanceados, y muestra cuándo el orden justifica el costo extra.
:::

## Qué problema resuelven mejor

Un **diccionario ordenado** sigue siendo un diccionario: asocia claves con valores. La diferencia es que además mantiene una relación de orden entre las claves.

Eso habilita operaciones como:

| Operación | Qué permite |
| :--- | :--- |
| `min()` | recuperar la menor clave |
| `max()` | recuperar la mayor clave |
| `successor(k)` | buscar la siguiente clave luego de `k` |
| `predecessor(k)` | buscar la clave inmediatamente anterior |
| `range(a, b)` | recuperar claves dentro de un intervalo |
| recorrido ordenado | visitar entradas en orden creciente o decreciente |

Estas operaciones no son un agregado decorativo. En muchos dominios son parte del problema real:

- índices por fecha,
- padrones por número,
- rankings,
- catálogos ordenados,
- reportes por intervalos.

## Qué exige el orden

Mantener orden no es gratis. Hace falta que las claves:

- admitan una comparación coherente,
- respeten un orden total o al menos un criterio de orden suficiente,
- y puedan insertarse o eliminarse sin romper la estructura.

En esta familia, la igualdad ya no alcanza. También importa la comparación:

```{code} java
:caption: La clave necesita ser comparable

public interface Comparable<T> {
    int compareTo(T otro);
}
```

La estructura no solo pregunta si dos claves son iguales. También pregunta:

- si una es menor,
- si una es mayor,
- y cómo ubicar nuevas entradas sin perder el orden global.

## Qué estructuras sostienen ese contrato

Las implementaciones típicas de diccionarios ordenados aparecen sobre árboles de búsqueda.

### BST

El caso más directo es el **árbol binario de búsqueda** (BST):

- mantiene la relación de orden entre subárbol izquierdo y derecho,
- permite recorrido inorden (visita las claves de menor a mayor),
- y soporta búsqueda, inserción y borrado.

```{mermaid}
flowchart TD
    N50["50: (A)"] --> N30["30: (B)"]
    N50 --> N70["70: (C)"]
    N30 --> N20["20: (D)"]
    N30 --> N40["40: (E)"]
    N70 --> N60["60: (F)"]
    N70 --> N80["80: (G)"]
    
    style N50 fill:#bbdefb,stroke:#0288d1
    style N30 fill:#c8e6c9,stroke:#388e3c
    style N70 fill:#c8e6c9,stroke:#388e3c
    style N20 fill:#e1bee7,stroke:#512da8
    style N40 fill:#e1bee7,stroke:#512da8
    style N60 fill:#e1bee7,stroke:#512da8
    style N80 fill:#e1bee7,stroke:#512da8
```

En este diagrama, cada nodo asocia una clave numérica (el criterio de orden) con un valor (A, B, C...). Buscar la clave `60` es caminar según si es mayor o menor que el nodo actual.

El problema es conocido: si la altura se degrada, también se degrada el costo de todas las operaciones (pasan de `O(log n)` a `O(n)`).

### Árboles balanceados

Cuando se necesitan mejores garantías, aparecen variantes balanceadas como:

- AVL,
- Red-Black Tree.

Estas estructuras mantienen el mismo tipo de contrato ordenado, pero controlan la altura para evitar degradación fuerte.

### Árboles B

Cuando el diccionario ordenado vive en disco o se piensa en almacenamiento por bloques, aparecen estructuras como:

- B-Tree,
- B+Tree.

En ese contexto, el orden sigue importando, pero el modelo de costo relevante deja de ser solo la comparación y pasa a incluir accesos a páginas o bloques.

## Hash vs diccionario ordenado

Conviene comparar directamente ambas estrategias:

| Necesidad | Tabla hash | Diccionario ordenado |
| :--- | :--- | :--- |
| búsqueda por clave exacta | muy buena en promedio | buena, con más estructura |
| recorrido ordenado | no natural | natural |
| consultas por rango | mala opción | muy buena opción |
| predecessor / successor | no natural | natural |
| garantía estructural | depende del caso y colisiones | depende del árbol, pero el orden es explícito |

La decisión no es “cuál es mejor en general”, sino “qué información necesita preservar el problema”.

## Ejemplo de uso

Supongamos un sistema que guarda ventas por fecha:

- si solo se necesita buscar una fecha exacta, hash puede alcanzar,
- pero si además se quiere:
  - listar ventas entre dos fechas,
  - encontrar la venta inmediatamente anterior,
  - recorrer cronológicamente,

entonces un diccionario ordenado modela mejor el problema.

```java
NavigableMap<LocalDate, Venta> historial = new TreeMap<>();
// ... (inserciones)

// Obtener todas las ventas de un mes en particular
SortedMap<LocalDate, Venta> ventasAgosto = historial.subMap(
    LocalDate.of(2024, 8, 1), 
    LocalDate.of(2024, 9, 1) // exclusivo
);

// Encontrar la última venta registrada antes de una fecha
Map.Entry<LocalDate, Venta> ultimaVentaAnterior = historial.lowerEntry(LocalDate.of(2024, 8, 15));
```

## Qué errores conviene evitar

En esta estructura conviene vigilar varios errores frecuentes:

1. **Usar hash cuando el problema necesita orden.**
2. **Pensar que “ordenado” es solo un detalle de presentación.** Muchas veces cambia qué operaciones son naturales.
3. **Ignorar el costo de mantener la estructura.** El orden se sostiene con más trabajo interno.
4. **Olvidar que el criterio de comparación es parte del contrato.**

:::{warning}
Un diccionario ordenado no compite con hash por velocidad promedio pura. Compite por **expresividad estructural** cuando el orden es información útil.
:::

## Resumen

Un diccionario ordenado aparece cuando no alcanza con recuperar valores por clave exacta. Lo que agrega es:

- orden mantenido,
- consultas por rango,
- mínimos y máximos,
- predecessor y successor,
- y recorridos con significado semántico.

Ese beneficio se paga con estructuras más complejas que una tabla hash, típicamente apoyadas en BST, árboles balanceados o árboles B.

## Ejercicios

```{exercise}
:label: ex-parte6-diccionarios-ordenados-mini

Proponé un caso donde una tabla hash sea insuficiente porque además de buscar por clave hace falta:

1. recorrer en orden,
2. o consultar por intervalos.

Justificá qué gana un diccionario ordenado.
```

```{exercise}
:label: ex-parte6-diccionarios-ordenados-vs-hash

Compará un padrón por DNI y un historial de turnos por fecha. Indicá en cuál empezarías pensando en hash y en cuál en un diccionario ordenado, y explicá por qué.
```

## Próximo paso

Para seguir, conviene pasar a [Tries](tries.md), donde la estructura de la clave también empieza a importar.
