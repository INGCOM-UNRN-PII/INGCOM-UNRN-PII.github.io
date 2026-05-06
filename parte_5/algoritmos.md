---
title: "Análisis de algoritmos"
subtitle: "Modelo de costo para comparar estructuras"
subject: Estructuras de Datos
description: Herramientas básicas para comparar costo temporal y espacial de operaciones sobre estructuras de datos.
---

(parte5-analisis-algoritmos)=
# Análisis de algoritmos

Esta página fija el lenguaje de complejidad que después debería aparecer cada vez que se compare una implementación con otra. La meta no es transformar la parte en una materia separada de algoritmos, sino dar el marco mínimo para discutir costo con criterio.

En estructuras de datos no alcanza con decir “funciona”. También hace falta poder decir:

- cuánto cuesta insertar,
- cuánto cuesta buscar,
- cuánto cuesta recorrer,
- y qué recursos extra hacen falta para sostener esas operaciones.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Instalar un modelo de costo simple y reusable para comparar operaciones sobre estructuras de datos.

**Prerrequisitos.** Conviene haber leído [Tipos Abstractos de Datos](adt.md), porque el análisis cobra sentido cuando ya está claro qué operación se quiere implementar.

**Desarrollo.** El capítulo introduce tamaño de entrada, notación O, Theta y Omega, distingue mejor caso, peor caso y costo amortizado, y cierra con criterios prácticos para comparar implementaciones sin perder el foco del problema.
:::

## Qué se analiza y qué se simplifica

Cuando se analiza un algoritmo, no se mide “segundos reales” en una máquina específica. Se construye un modelo más simple que permita comparar crecimiento de costo cuando el problema aumenta de tamaño.

El punto de partida suele ser:

- elegir una medida de tamaño de entrada, por ejemplo `n`,
- identificar las operaciones relevantes,
- y contar cómo crece la cantidad de trabajo en función de `n`.

### Ejemplo simple

```{code} java
:caption: Búsqueda lineal

public static boolean contiene(int[] datos, int buscado) {
    for (int i = 0; i < datos.length; i++) {
        if (datos[i] == buscado) {
            return true;
        }
    }
    return false;
}
```

Si `n` es la longitud del arreglo:

- en el mejor caso se encuentra en la primera posición,
- en el peor caso se revisan los `n` elementos,
- y el crecimiento del costo es lineal respecto de `n`.

## Notación O, Theta y Omega

Estas tres notaciones permiten hablar de cotas asintóticas.

| Notación | Idea informal | Qué comunica |
| :--- | :--- | :--- |
| `O(f(n))` | cota superior | el algoritmo no crece más rápido que eso |
| `Theta(f(n))` | cota ajustada | el crecimiento es de ese orden |
| `Omega(f(n))` | cota inferior | el algoritmo no crece más lento que eso |

En esta parte, la notación que más se usa en comparaciones rápidas suele ser `O(...)`, pero conviene no olvidar que:

- `O(n)` no significa “siempre hace exactamente `n` operaciones”,
- `O(1)` no significa “gratis”,
- y `O(log n)` no siempre es mejor en la práctica si la constante oculta o la implementación son demasiado costosas.

:::{note}
En clase conviene leer estas notaciones como una herramienta de comparación de crecimiento, no como una promesa exacta de tiempo real.
:::

## Mejor caso, peor caso y caso promedio

Decir que una operación es `O(n)` no agota la discusión. También importa **en qué escenario** se está midiendo.

### Mejor caso

Es el escenario más favorable. A veces sirve para entender un límite inferior práctico, pero casi nunca alcanza para justificar una estructura.

### Peor caso

Es el escenario más desfavorable. Suele ser el dato más útil cuando se necesita garantía.

### Caso promedio

Depende de supuestos sobre la distribución de entradas. Puede ser muy informativo, pero también muy engañoso si esos supuestos no se explicitan.

Ejemplo:

| Operación | Mejor caso | Peor caso |
| :--- | :--- | :--- |
| Buscar en arreglo desordenado | `O(1)` | `O(n)` |
| Acceder por índice en arreglo | `O(1)` | `O(1)` |
| Buscar en BST balanceado | `O(log n)` | `O(log n)` |
| Buscar en BST degradado | `O(1)` | `O(n)` |

En estructuras de datos, una parte grande del diseño consiste en decidir **qué caso importa más** para el problema real.

## Costo temporal y costo espacial

El análisis no se limita al tiempo. También importa cuánto espacio adicional consume una implementación.

### Costo temporal

Cuenta cuánto trabajo hace la operación al crecer la entrada.

### Costo espacial

Cuenta cuánta memoria extra requiere:

- nodos adicionales,
- arreglos auxiliares,
- recursión en pila de llamadas,
- buffers o estructuras temporales.

Esto importa mucho en comparaciones como:

| Implementación | Ventaja temporal | Costo espacial típico |
| :--- | :--- | :--- |
| Arreglo | acceso por índice rápido | memoria contigua, poco overhead |
| Lista enlazada | inserción local eficiente | punteros extra por nodo |
| Tabla hash | lookup promedio rápido | buckets, capacidad ociosa, colisiones |
| Árbol balanceado | búsquedas garantizadas | metadatos o estructura adicional para balance |

## Análisis amortizado

Algunas operaciones son raramente caras, pero la mayoría de las veces son baratas. En esos casos conviene mirar el costo **amortizado**.

### Ejemplo: arreglo dinámico

Supongamos un vector que duplica su capacidad cuando se llena:

- la mayoría de los `append` agregan un elemento al final con costo bajo,
- de vez en cuando aparece un redimensionamiento,
- y ese redimensionamiento copia muchos elementos de golpe.

Una inserción puntual puede costar `O(n)`, pero una larga secuencia de inserciones al final tiene costo amortizado `O(1)` por operación.

```{code} java
:caption: Inserción al final con redimensionamiento ocasional

public void agregar(int valor) {
    if (this.cantidad == this.datos.length) {
        redimensionar();
    }
    this.datos[this.cantidad] = valor;
    this.cantidad++;
}
```

El análisis amortizado no niega el costo caro. Lo ubica correctamente dentro de una secuencia larga de operaciones.

## Cómo comparar implementaciones de una misma estructura

En esta parte, el análisis aparece sobre todo para comparar varias implementaciones del mismo TAD. Esa comparación conviene hacerla con preguntas concretas:

1. ¿Qué operaciones dominan el uso real?
2. ¿Qué caso importa más: promedio, peor caso, amortizado?
3. ¿Cuánta memoria extra se tolera?
4. ¿La estructura necesita garantías fuertes o buen promedio alcanza?

### Ejemplo: lista basada en arreglo vs lista basada en nodos

| Operación dominante | Arreglo dinámico | Lista enlazada |
| :--- | :--- | :--- |
| acceso por índice | muy bueno | malo |
| inserción local con referencia al nodo | no natural | muy buena |
| recorridos secuenciales | buena localidad de memoria | peor localidad |
| redimensionamiento | ocasional y costoso | no aplica |

No hay una ganadora universal. La decisión depende del perfil de uso.

## Qué errores conviene evitar

En esta parte conviene evitar varios abusos frecuentes:

1. **Usar Big-O como slogan.** “Es `O(1)`” no dice nada si no está claro qué caso se está midiendo.
2. **Ignorar constantes y contexto.** Dos soluciones con la misma cota asintótica pueden comportarse muy distinto.
3. **Comparar operaciones aisladas sin mirar la secuencia real de uso.**
4. **Olvidar el costo espacial.**
5. **Tomar el mejor caso como si fuera garantía.**

:::{warning}
La complejidad no reemplaza al criterio de diseño. Sirve para justificar elecciones, no para fetichizar fórmulas.
:::

## Cómo usar este capítulo en el resto de la parte

Desde acá en adelante, cada estructura conviene leer con la misma grilla:

- operaciones principales,
- costo temporal esperado,
- costo espacial,
- peor caso relevante,
- trade-offs de implementación.

Ese patrón permite comparar:

- arreglos con listas,
- hash con árboles ordenados,
- BST con árboles balanceados,
- BFS con Dijkstra o Kruskal.

## Resumen

No alcanza con que una estructura “funcione”. También hace falta poder explicar cuánto cuesta usarla y por qué una representación conviene más que otra en un contexto dado.

En esta parte, la notación asintótica no se usa para decorar texto. Se usa para tomar decisiones:

- qué estructura conviene,
- qué implementación conviene,
- y qué trade-offs se están aceptando.

## Ejercicios

```{exercise}
:label: ex-parte5-algoritmos-mini

Compará el costo de insertar al final en:

1. un arreglo fijo,
2. un arreglo dinámico,
3. y una lista enlazada.

Indicá qué información adicional hace falta para que la comparación sea justa.
```

```{exercise}
:label: ex-parte5-algoritmos-casos

Para una búsqueda lineal en un arreglo, describí:

1. mejor caso,
2. peor caso,
3. y por qué el caso promedio no puede discutirse seriamente sin suponer algo sobre las entradas.
```

## Próximo paso

Para seguir, conviene entrar a [Secuencias](secuencias/indice.md), donde estas herramientas empiezan a aplicarse sobre la familia lineal más general.
