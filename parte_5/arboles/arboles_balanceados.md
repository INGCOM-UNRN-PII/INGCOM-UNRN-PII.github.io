---
title: "Árboles balanceados"
subtitle: "Sostener altura razonable de manera activa"
subject: Estructuras de Datos
description: Cómo controlar la altura de un árbol de búsqueda para que las operaciones sigan siendo eficientes incluso con inserciones adversas.
---

(parte5-arboles-balanceados)=
# Árboles balanceados

Los árboles balanceados aparecen cuando ya no alcanza con “esperar” que un BST quede razonablemente bien formado. Si el problema requiere garantías más fuertes, el balance pasa a ser parte explícita del diseño.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender por qué el balance no es un detalle estético, sino una condición para sostener la eficiencia de operaciones sobre árboles de búsqueda.

**Prerrequisitos.** Conviene haber leído [Árboles binarios de búsqueda](arboles_busqueda.md), porque este capítulo responde directamente a sus limitaciones.

**Desarrollo.** El capítulo muestra cómo la altura degrada un BST simple, introduce rotaciones como operación estructural y compara estrategias como AVL y Red-Black para sostener búsquedas, inserciones y borrados en tiempo logarítmico.
:::

## Por qué hace falta balance

Un BST simple puede funcionar muy bien o muy mal según el orden de inserción. Si las claves llegan en un patrón favorable, la altura queda baja y las operaciones son rápidas. Si llegan en orden creciente o decreciente, el árbol se puede degenerar y terminar comportándose casi como una lista.

Ese problema importa porque en un árbol de búsqueda el costo de:

- buscar,
- insertar,
- borrar,

depende fuertemente de la altura.

Si la altura se acerca a `n`, las operaciones dejan de ser razonables para entradas grandes. Los árboles balanceados aparecen para evitar esa degradación.

```{code} java
:caption: Inserciones adversas en un BST simple

for (int clave = 1; clave <= n; clave++) {
    arbol.insertar(clave);
}
```

Si el árbol no corrige su forma, este patrón puede construir una cadena sesgada en lugar de una estructura casi logarítmica.

## Qué significa “balanceado”

Balancear no significa que todo quede perfectamente simétrico. Significa imponer invariantes suficientes para que la altura no crezca de forma patológica.

Hay dos ideas útiles:

1. **balance estricto**, donde la diferencia de alturas se controla muy de cerca;
2. **balance relajado**, donde se permite más desvío local, pero igual se mantiene altura logarítmica.

Lo central no es la estética del dibujo. Lo central es esta consecuencia:

- la estructura paga trabajo extra al actualizarse,
- para no pagar costos peores en cada búsqueda futura.

## Rotaciones: la operación estructural clave

Los árboles balanceados no suelen reconstruirse desde cero ante cada inserción. Corrigen desequilibrios con operaciones locales llamadas **rotaciones**.

Una rotación:

- cambia relaciones padre-hijo,
- preserva el orden relativo de las claves,
- y modifica la forma del árbol para bajar la altura de una zona conflictiva.

Las variantes típicas son:

- rotación simple a izquierda,
- rotación simple a derecha,
- rotación doble izquierda-derecha,
- rotación doble derecha-izquierda.

La idea importante no es memorizar dibujos aislados, sino entender por qué sirven: reacomodan subárboles sin romper la invariante de búsqueda.

## AVL: balance más estricto

En un árbol **AVL**, para cada nodo se controla que la diferencia entre altura del subárbol izquierdo y del derecho quede acotada.

La ventaja es fuerte:

- la altura queda muy controlada,
- las búsquedas son muy buenas,
- el árbol tiende a mantenerse “prolijo”.

El costo también es claro:

- hay que guardar o recomputar información de altura o factor de balance,
- inserciones y borrados pueden disparar rotaciones,
- la implementación es más delicada que en un BST simple.

AVL suele ser una muy buena elección cuando dominan las consultas y se quiere una estructura ordenada con comportamiento muy estable.

## Red-Black Tree: balance más relajado

Los **Red-Black Trees** usan una estrategia menos agresiva. En lugar de exigir diferencias de altura tan pequeñas, sostienen un conjunto de reglas de coloración y estructura que impide degradaciones severas.

La intuición útil es:

- permiten un árbol algo menos rígido que AVL,
- hacen menos trabajo correctivo en promedio,
- siguen ofreciendo altura logarítmica.

Por eso aparecen con frecuencia en implementaciones reales de:

- mapas ordenados,
- sets ordenados,
- bibliotecas estándar.

No son “más simples” conceptualmente. Solo hacen otro intercambio entre:

- rigor estructural,
- frecuencia de rebalanceo,
- y complejidad operativa.

## AVL vs Red-Black

Conviene compararlos por criterio, no por eslogan:

| Variante | Qué controla | Ventaja típica | Costo típico |
| :--- | :--- | :--- | :--- |
| AVL | diferencia de alturas muy acotada | búsquedas muy consistentes | más rotaciones y más mantenimiento |
| Red-Black | invariantes más relajadas | actualizaciones más suaves en promedio | peor altura constante que AVL |

Las dos familias comparten una promesa importante:

- las operaciones fundamentales siguen siendo O(log n) respecto de la cantidad de claves.

La elección concreta depende de qué importa más:

- minimizar altura,
- simplificar ciertas actualizaciones,
- o reutilizar una implementación disponible.

## Dónde aparecen en la práctica

Los árboles balanceados suelen sostener problemas donde hace falta:

- buscar por clave,
- insertar y borrar muchas veces,
- recorrer en orden,
- consultar mínimos, máximos, predecesores y sucesores,
- evitar que entradas adversas degraden la estructura.

Por eso encajan bien detrás de:

- diccionarios ordenados,
- conjuntos ordenados,
- índices en memoria,
- estructuras de soporte para algoritmos que necesitan orden dinámico.

## Qué errores conviene evitar

1. **Pensar que un BST “más o menos equilibrado” alcanza siempre.** Si el problema exige garantías, el balance no puede quedar librado a la suerte.
2. **Confundir balance con simetría perfecta.** La meta es controlar altura, no dibujar árboles bonitos.
3. **Olvidar el costo de mantener invariantes.** La mejora en búsquedas se paga durante actualizaciones.
4. **Suponer que AVL y Red-Black resuelven exactamente el mismo problema con la misma estrategia.**

:::{warning}
Un árbol balanceado no gana porque “busca distinto”, sino porque evita que la altura se dispare.
:::

## Resumen

Los árboles balanceados corrigen el defecto central del BST simple: su dependencia extrema de la forma.

Lo hacen imponiendo invariantes adicionales que:

- controlan la altura,
- conservan el orden de búsqueda,
- y mantienen operaciones fundamentales en O(log n).

Ese beneficio no es gratis. Se paga con rotaciones, información extra e implementaciones más cargadas de casos.

## Ejercicios

```{exercise}
:label: ex-parte5-arboles-balanceados-mini

Explicá por qué puede valer la pena asumir más complejidad de implementación en un árbol balanceado si la estructura debe soportar muchas operaciones de búsqueda en producción.
```

```{exercise}
:label: ex-parte5-arboles-balanceados-avl-vs-rb

Un equipo necesita un diccionario ordenado que reciba muchas inserciones y borrados durante el día, pero también muchas búsquedas. Explicá qué preguntas harías antes de inclinarte por AVL o por Red-Black.
```

## Próximo paso

Para seguir, conviene pasar a [Heaps](heaps.md), donde el árbol deja de optimizar búsqueda general y pasa a optimizar prioridad.
