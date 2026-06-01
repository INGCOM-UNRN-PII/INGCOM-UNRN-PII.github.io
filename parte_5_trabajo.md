# Trabajo de completado de `parte_5`

Inventario compartido para completar `parte_5/` por lotes concurrentes.

## Convención de estado

- `[ ]` pendiente
- `[~]` reclamado / en proceso
- `[x]` resuelto
- `[z]` resuelta fase 2

## Reglas de uso con lock

1. Releer este archivo y `parte_5_trabajo.lock` antes de reclamar trabajo.
2. En la pasada inicial, reclamar solo ítems en `[ ]`. En la pasada de ampliación, reclamar solo ítems en `[x]`.
3. Actualizar el marcador del ítem y el `.lock` en la misma edición.
4. Si el archivo ya fue completado por otro agente, reconciliar el estado a `[x]` o `[z]` según la fase en lugar de reescribir encima.
5. Al cerrar un lote, limpiar `active_claims` y registrar el lote en `last_completed_batch`.

## Criterio de completado

Un ítem se considera resuelto cuando el archivo deja de ser solo esqueleto y pasa a tener desarrollo real suficiente para publicarse más adelante sin rehacer su estructura editorial.

## Núcleo transversal

- [z] `p5-indice` — `parte_5/indice.md` — consolidar el mapa de aprendizaje final de la parte una vez que avance el contenido real.
- [z] `p5-adt` — `parte_5/adt.md` — desarrollar el capítulo base sobre TAD, contratos, representación e invariantes.
- [z] `p5-algoritmos` — `parte_5/algoritmos.md` — desarrollar el marco de complejidad reutilizable en toda la parte.

## Secuencias

- [z] `p5-secuencias-indice` — `parte_5/secuencias/indice.md` — ajustar el índice de familia cuando existan capítulos con contenido pleno.
- [z] `p5-secuencias-fundamentos` — `parte_5/secuencias/fundamentos.md` — desarrollar el TAD de secuencia, operaciones y comparaciones base.
- [z] `p5-secuencias-arreglos` — `parte_5/secuencias/arreglos.md` — desarrollar arreglos fijos, dinámicos, buffers circulares y trade-offs.
- [z] `p5-secuencias-listas` — `parte_5/secuencias/listas_enlazadas.md` — desarrollar listas simples, dobles, circulares y centinelas.
- [z] `p5-secuencias-pilas` — `parte_5/secuencias/pilas.md` — desarrollar TAD pila, implementaciones y aplicaciones.
- [z] `p5-secuencias-colas` — `parte_5/secuencias/colas.md` — desarrollar TAD cola, implementaciones y aplicaciones.
- [z] `p5-secuencias-deques` — `parte_5/secuencias/deques.md` — desarrollar deques, variantes restringidas y usos típicos.
- [z] `p5-secuencias-prioridad` — `parte_5/secuencias/colas_prioridad.md` — desarrollar colas de prioridad e implementación puente hacia heaps.

## Diccionarios y conjuntos

- [z] `p5-diccionarios-indice` — `parte_5/diccionarios/indice.md` — ajustar el índice de familia cuando existan capítulos con contenido pleno.
- [z] `p5-diccionarios-fundamentos` — `parte_5/diccionarios/fundamentos.md` — desarrollar claves, pertenencia, mapas, sets y variantes.
- [z] `p5-diccionarios-hash` — `parte_5/diccionarios/tablas_hash.md` — desarrollar funciones hash, colisiones, factor de carga y rehash.
- [z] `p5-diccionarios-ordenados` — `parte_5/diccionarios/diccionarios_ordenados.md` — desarrollar orden, rangos y operaciones derivadas.
- [z] `p5-diccionarios-tries` — `parte_5/diccionarios/tries.md` — desarrollar búsqueda por prefijo y variantes comprimidas.
- [z] `p5-diccionarios-disjuntos` — `parte_5/diccionarios/conjuntos_disjuntos.md` — desarrollar union-find, rank y path compression.

## Árboles

- [z] `p5-arboles-indice` — `parte_5/arboles/indice.md` — ajustar el índice de familia cuando existan capítulos con contenido pleno.
- [z] `p5-arboles-fundamentos` — `parte_5/arboles/fundamentos.md` — desarrollar jerarquía, recorridos, altura y recursión estructural.
- [z] `p5-arboles-binarios` — `parte_5/arboles/arboles_binarios.md` — desarrollar forma binaria, recorridos y representación.
- [z] `p5-arboles-bst` — `parte_5/arboles/arboles_busqueda.md` — desarrollar BST, operaciones y costo dependiente de altura.
- [z] `p5-arboles-balanceados` — `parte_5/arboles/arboles_balanceados.md` — desarrollar AVL, Red-Black y balance.
- [z] `p5-arboles-heaps` — `parte_5/arboles/heaps.md` — desarrollar heaps, heapsort y relación con colas de prioridad.
- [z] `p5-arboles-b` — `parte_5/arboles/arboles_b.md` — desarrollar árboles B y B+Tree para almacenamiento externo.

## Grafos

- [z] `p5-grafos-indice` — `parte_5/grafos/indice.md` — ajustar el índice de familia cuando existan capítulos con contenido pleno.
- [z] `p5-grafos-fundamentos` — `parte_5/grafos/fundamentos.md` — desarrollar vocabulario base de grafos y problemas típicos.
- [z] `p5-grafos-representacion` — `parte_5/grafos/representacion.md` — desarrollar matrices, listas, aristas y costo.
- [z] `p5-grafos-recorridos` — `parte_5/grafos/recorridos.md` — desarrollar DFS, BFS y aplicaciones básicas.
- [z] `p5-grafos-caminos` — `parte_5/grafos/caminos_minimos.md` — desarrollar BFS, Dijkstra, Bellman-Ford y variantes.
- [z] `p5-grafos-expansion` — `parte_5/grafos/arboles_de_expansion.md` — desarrollar Prim, Kruskal y spanning trees mínimos.
- [z] `p5-grafos-topologico` — `parte_5/grafos/orden_topologico.md` — desarrollar DAG, DFS y Kahn.
- [z] `p5-grafos-conectividad` — `parte_5/grafos/conectividad.md` — desarrollar componentes, SCC y partición del grafo.

## Cierre editorial y publicación futura

- [z] `p5-revision-cruzada` — revisar consistencia terminológica, enlaces internos, progresión pedagógica y referencias entre familias.
- [z] `p5-publicacion` — decidir cuándo `parte_5` deja de ser “en revisión”, alinear `myst.yml` y adaptar la portada final para publicación.
