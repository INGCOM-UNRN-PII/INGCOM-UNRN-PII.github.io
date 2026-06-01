---
title: "Parte 6: Estructuras de datos"
subtitle: "Mapa de aprendizaje"
subject: Estructuras de Datos
description: "Continuación de la parte 5: pasar del contrato abstracto al diseño, implementación y evaluación de estructuras concretas."
---

(parte-6-estructuras-de-datos)=
# Parte 6: Estructuras de datos

Esta parte continúa directamente después de la parte 5. Si en la parte anterior se fijó el contrato de los TDA (qué se promete), acá el foco pasa a decidir representaciones concretas, medir su costo real y elegir estructuras según restricciones del problema.

:::{important}
**Estado.** Índice base de trabajo para ampliar la parte. Se organiza el recorrido general y el orden pedagógico de consolidación.
:::

## Propósito de la parte

Consolidar el puente entre especificación e implementación:

1. bajar de axiomas a decisiones de representación;
2. usar localidad de memoria y profiling para validar decisiones;
3. recorrer familias de estructuras y sus trade-offs;
4. justificar elecciones con complejidad, invariantes y contexto de uso.

## Orden sugerido de lectura

1. [Localidad de memoria](localidad_memoria.md) — Marco de rendimiento real para no decidir solo con notación asintótica.
2. [Profiling](profiling.md) — Cómo medir antes de optimizar y cómo comparar implementaciones con evidencia.
3. [Secuencias](secuencias/indice.md) — Entrada por estructuras lineales y restricciones de acceso.
4. [Diccionarios y conjuntos](diccionarios/indice.md) — Paso de acceso posicional a acceso por clave.
5. [Árboles](arboles/indice.md) — Jerarquía, búsqueda, balance y prioridades.
6. [Grafos](grafos/indice.md) — Modelado de relaciones generales y algoritmos clásicos.

## Capítulos nucleares

- [Localidad de memoria](localidad_memoria.md)
- [Fundamentos de secuencias](secuencias/fundamentos.md)
- [Tablas hash](diccionarios/tablas_hash.md)
- [Árboles binarios de búsqueda](arboles/arboles_busqueda.md)
- [Recorridos](grafos/recorridos.md)

## Repaso y ampliación

- [Profiling](profiling.md)
- [Deques](secuencias/deques.md) y [Colas de prioridad](secuencias/colas_prioridad.md)
- [Tries](diccionarios/tries.md) y [Conjuntos disjuntos](diccionarios/conjuntos_disjuntos.md)
- [Árboles balanceados](arboles/arboles_balanceados.md) y [Árboles B](arboles/arboles_b.md)
- [Caminos mínimos](grafos/caminos_minimos.md), [Árboles de expansión](grafos/arboles_de_expansion.md), [Orden topológico](grafos/orden_topologico.md), [Conectividad](grafos/conectividad.md)

## Índice exhaustivo

### Núcleo transversal

- [Localidad de memoria](localidad_memoria.md)
- [Profiling](profiling.md)

### Secuencias

- [Índice de secuencias](secuencias/indice.md)
- [Fundamentos de secuencias](secuencias/fundamentos.md)
- [Arreglos](secuencias/arreglos.md)
- [Listas enlazadas](secuencias/listas_enlazadas.md)
- [Pilas](secuencias/pilas.md)
- [Colas](secuencias/colas.md)
- [Deques](secuencias/deques.md)
- [Colas de prioridad](secuencias/colas_prioridad.md)

### Diccionarios y conjuntos

- [Índice de diccionarios y conjuntos](diccionarios/indice.md)
- [Fundamentos de diccionarios y conjuntos](diccionarios/fundamentos.md)
- [Tablas hash](diccionarios/tablas_hash.md)
- [Diccionarios ordenados](diccionarios/diccionarios_ordenados.md)
- [Tries](diccionarios/tries.md)
- [Conjuntos disjuntos](diccionarios/conjuntos_disjuntos.md)

### Árboles

- [Índice de árboles](arboles/indice.md)
- [Fundamentos de árboles](arboles/fundamentos.md)
- [Árboles binarios](arboles/arboles_binarios.md)
- [Árboles binarios de búsqueda](arboles/arboles_busqueda.md)
- [Árboles balanceados](arboles/arboles_balanceados.md)
- [Heaps](arboles/heaps.md)
- [Árboles B](arboles/arboles_b.md)

### Grafos

- [Índice de grafos](grafos/indice.md)
- [Fundamentos de grafos](grafos/fundamentos.md)
- [Representación de grafos](grafos/representacion.md)
- [Recorridos](grafos/recorridos.md)
- [Caminos mínimos](grafos/caminos_minimos.md)
- [Árboles de expansión](grafos/arboles_de_expansion.md)
- [Orden topológico](grafos/orden_topologico.md)
- [Conectividad](grafos/conectividad.md)

### Cierre
- [Revisión cruzada de estructuras](p6-revision-cruzada.md)

## Próximo paso

La entrada recomendada para seguir el hilo de la parte 5 es [Localidad de memoria](localidad_memoria.md). Después conviene pasar por [Profiling](profiling.md) antes de abrir las familias de estructuras.
