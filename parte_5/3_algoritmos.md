---
title: "Análisis de algoritmos"
subtitle: "Modelo de costo para comparar estructuras"
subject: Estructuras de Datos
description: Herramientas básicas para comparar costo temporal y espacial de operaciones sobre estructuras de datos.
---

(parte5-analisis-algoritmos)=
# Análisis de algoritmos


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

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

## Modelo formal de costo

Para que la comparación sea reproducible, hay que fijar qué se cuenta y qué se abstrae.

### Supuestos del modelo

Tomamos un modelo uniforme:

1. cada operación elemental de lectura, escritura, comparación aritmética o salto condicional cuesta una unidad constante;
2. el costo total de un algoritmo es la suma de sus costos elementales;
3. el tamaño de entrada se denota con $n$;
4. una función de costo se escribe como $T(n)$ cuando depende solo del tamaño de entrada.

Ese modelo no intenta describir una CPU real. Intenta dar una medida estable para comparar algoritmos entre sí.

### Definiciones asintóticas

Sean $f$ y $g$ funciones de $n$, con $g(n) > 0$ para $n$ suficientemente grande.

- $f(n) \in O(g(n))$ si existen constantes $c > 0$ y $n_0$ tales que $0 \le f(n) \le c\,g(n)$ para todo $n \ge n_0$.
- $f(n) \in \Omega(g(n))$ si existen constantes $c > 0$ y $n_0$ tales que $0 \le c\,g(n) \le f(n)$ para todo $n \ge n_0$.
- $f(n) \in \Theta(g(n))$ si $f(n) \in O(g(n))$ y $f(n) \in \Omega(g(n))$.

En otras palabras:

- `O` da una cota superior asintótica,
- `Ω` da una cota inferior asintótica,
- `Θ` fija el orden exacto de crecimiento.

### Proposición 1

Si $T(n) = a n + b$ con $a > 0$, entonces $T(n) \in \Theta(n)$.

**Demostración.**

Para $n \ge \max(1, |b|/a)$ se cumple:

$$a n - |b| \le a n + b \le a n + |b|$$

y, como $a n - |b| \ge (a/2)n$ para $n$ suficientemente grande, existen constantes $c_1, c_2 > 0$ tales que:

$$c_1 n \le T(n) \le c_2 n$$

Por definición, $T(n) \in \Theta(n)$. ∎

### Proposición 2

Si un bloque de código ejecuta un número fijo $k$ de operaciones elementales, su costo es $\Theta(1)$.

**Demostración.**

El costo total es $T(n)=k$, independiente de $n$. Tomando $c=k$ y $n_0=1$, se verifica $0 \le T(n) \le c$, luego $T(n) \in O(1)$. Como también $T(n)\ge 1$ para $k>0$, se obtiene $T(n) \in \Theta(1)$. ∎

## Qué se analiza y qué se simplifica

Cuando se analiza un algoritmo, no se mide “segundos reales” en una máquina específica. Se construye un modelo más simple que permita comparar crecimiento de costo cuando el problema aumenta de tamaño.

El punto de partida suele ser:

- elegir una medida de tamaño de entrada, por ejemplo $n$,
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

Si $n$ es la longitud del arreglo:

- en el mejor caso se encuentra en la primera posición,
- en el peor caso se revisan los $n$ elementos,
- y el crecimiento del costo es lineal respecto de $n$.

### Demostración de la búsqueda lineal

Sea $T(n)$ el número de comparaciones ejecutadas por `contiene`.

- Si el buscado está en la primera posición, el algoritmo hace una comparación y termina: $T(n)=1$, luego $T(n) \in \Theta(1)$.
- Si el buscado no está o está al final, compara contra cada elemento: $T(n)=n$, luego $T(n) \in \Theta(n)$.

En el caso general, el peor caso de la búsqueda lineal es $\Theta(n)$ porque no existe forma de certificar ausencia sin examinar toda la secuencia en un arreglo no ordenado. Esa afirmación no depende de la implementación concreta: depende de la información disponible.

## Notación O, Theta y Omega

Estas tres notaciones permiten hablar de cotas asintóticas (límites de crecimiento cuando $n \to \infty$).

| Notación | Idea informal | Qué comunica |
| :--- | :--- | :--- |
| $O(f(n))$ | cota superior | el algoritmo no crece más rápido que eso |
| $\Theta(f(n))$ | cota ajustada | el crecimiento es de ese orden |
| $\Omega(f(n))$ | cota inferior | el algoritmo no crece más lento que eso |

En esta parte, la notación que más se usa en comparaciones rápidas suele ser $O(\cdot)$.

### Consecuencia útil

Si $f(n) \in \Theta(g(n))$, entonces cualquier implementación cuyo costo pueda escribirse como $a\,g(n)+b$ tiene el mismo orden asintótico. Eso permite ignorar constantes multiplicativas y términos menores sin perder la comparación estructural.

### Catálogo de Complejidades Comunes

Para tener intuición sobre qué significa cada cota en la práctica, conviene conocer las familias más frecuentes:

1. **$O(1)$ (Constante):** El costo no depende del tamaño de los datos. 
   - *Ejemplo:* Acceder a `arreglo[5]`. Así el arreglo tenga 10 elementos o 10 millones, el tiempo es el mismo.
2. **$O(\log n)$ (Logarítmica):** El costo crece muy lento. Típicamente ocurre cuando el algoritmo descarta la mitad de los datos en cada paso.
   - *Ejemplo:* Búsqueda binaria en un arreglo ordenado. Si el tamaño se duplica, solo hace falta un paso más.
3. **$O(n)$ (Lineal):** El costo crece proporcionalmente al tamaño de los datos. Hay que mirar, al menos, cada elemento una vez.
   - *Ejemplo:* Encontrar el máximo en un arreglo desordenado.
4. **$O(n \log n)$ (Lineal-Logarítmica):** Típica de los mejores algoritmos de ordenamiento basados en comparaciones. 
   - *Ejemplo:* MergeSort o QuickSort (en caso promedio).
5. **$O(n^2)$ (Cuadrática):** El costo crece con el cuadrado del tamaño. Suele aparecer cuando hay bucles anidados procesando la misma colección.
   - *Ejemplo:* Comparar todos contra todos para encontrar pares duplicados. Si la entrada se multiplica por 10, el tiempo se multiplica por 100.

Conviene no olvidar que:

- $O(n)$ no significa “siempre hace exactamente $n$ operaciones”,
- $O(1)$ no significa “gratis” (podría ser constante pero lentísimo),
- y $O(\log n)$ no siempre es mejor en la práctica si la constante oculta o la implementación (como seguir punteros dispersos en memoria) son demasiado costosas.

### Proposición 3

Un doble bucle triangular sobre una colección de tamaño $n$ tiene costo $\Theta(n^2)$.

**Demostración.**

Si el bucle externo corre $n$ veces y el interno corre $i$ veces en la iteración $i$, el número total de pasos es:

$$\sum_{i=1}^{n-1} i = \frac{n(n-1)}{2}$$

Como:

$$\frac{n^2-n}{2} \in \Theta(n^2)$$

el costo total es $\Theta(n^2)$. ∎

:::{note}
En clase conviene leer estas notaciones como una herramienta de comparación de crecimiento, no como una promesa exacta de tiempo real.
:::

## Mejor caso, peor caso y caso promedio

Decir que una operación es $O(n)$ no agota la discusión. También importa **en qué escenario** se está midiendo.

### Mejor caso

Es el escenario más favorable. A veces sirve para entender un límite inferior práctico, pero casi nunca alcanza para justificar una estructura.

### Peor caso

Es el escenario más desfavorable. Suele ser el dato más útil cuando se necesita garantía.

### Caso promedio

Depende de supuestos sobre la distribución de entradas. Puede ser muy informativo, pero también muy engañoso si esos supuestos no se explicitan.

Ejemplo:

| Operación | Mejor caso | Peor caso |
| :--- | :--- | :--- |
| Buscar en arreglo desordenado | $O(1)$ | $O(n)$ |
| Acceder por índice en arreglo | $O(1)$ | $O(1)$ |
| Buscar en BST balanceado | $O(\log n)$ | $O(\log n)$ |
| Buscar en BST degradado | $O(1)$ | $O(n)$ |

En estructuras de datos, una parte grande del diseño consiste en decidir **qué caso importa más** para el problema real.

### Regla práctica

Cuando el usuario necesita garantía, el peor caso manda. Cuando las operaciones se repiten muchas veces sobre datos cambiantes, el costo amortizado suele ser la mejor lectura. El promedio solo es útil si la distribución de entradas está definida y justificada.

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

### Formalización del costo espacial

Si $M(n)$ es la memoria total usada por un algoritmo sobre entradas de tamaño $n$, conviene distinguir:

- **espacio de entrada** $I(n)$: memoria donde ya viven los datos a procesar;
- **espacio auxiliar** $A(n)$: memoria adicional reservada por el algoritmo.

Entonces:

$$M(n) = I(n) + A(n)$$

En análisis de estructuras de datos, casi siempre se reporta $A(n)$, porque $I(n)$ depende del problema y no de la estrategia de implementación.

### Espacio auxiliar vs memoria de la estructura

En una estructura mutable, hay dos lecturas complementarias:

1. **Costo estructural**: memoria persistente para almacenar los datos (nodos, punteros, capacidad reservada).
2. **Costo auxiliar por operación**: memoria temporal durante una operación puntual.

Ejemplo: una tabla hash puede tener costo estructural $\Theta(n)$ en entradas más capacidad reservada, pero una operación `lookup` típica usa $O(1)$ memoria auxiliar.

### Algoritmos in-place y no in-place

Un algoritmo se considera **in-place** cuando su espacio auxiliar es $O(1)$ (o, en algunas variantes, $O(\log n)$ si se descuenta la pila de recursión controlada). Esta distinción importa porque dos algoritmos con igual tiempo pueden diferir fuerte en memoria.

| Algoritmo / operación | Tiempo típico | Espacio auxiliar típico |
| :--- | :--- | :--- |
| Búsqueda lineal en arreglo | $O(n)$ | $O(1)$ |
| Búsqueda binaria iterativa | $O(\log n)$ | $O(1)$ |
| Búsqueda binaria recursiva | $O(\log n)$ | $O(\log n)$ |
| MergeSort | $O(n \log n)$ | $O(n)$ |
| QuickSort (in-place, promedio) | $O(n \log n)$ | $O(\log n)$ por pila |

### Costo espacial de recursión

Si una recursión tiene profundidad $d(n)$ y cada frame agrega costo constante, su espacio auxiliar por pila es:

$$A_{\text{stack}}(n) \in \Theta(d(n))$$

Por eso:

- un recorrido DFS recursivo en árbol balanceado usa $O(\log n)$ stack;
- el mismo DFS en árbol degradado usa $O(n)$ stack.

### Consideraciones prácticas de memoria

Además del orden asintótico, conviene explicitar:

1. **factor de sobrecarga por elemento** (punteros, metadatos, padding);
2. **capacidad ociosa esperada** (por ejemplo en arreglos dinámicos o tablas hash);
3. **localidad de memoria** (contigua vs dispersa), que impacta caché y tiempo real;
4. **picos de memoria** durante redimensionamientos o fases de copia.

Estas consideraciones no reemplazan a $O(\cdot)$, pero evitan decisiones erróneas cuando dos soluciones tienen el mismo orden asintótico.

Esto importa mucho en comparaciones como:

| Implementación | Ventaja temporal | Costo espacial típico |
| :--- | :--- | :--- |
| Arreglo | acceso por índice rápido | memoria contigua, poco overhead |
| Lista enlazada | inserción local eficiente | punteros extra por nodo |
| Tabla hash | lookup promedio rápido | buckets, capacidad ociosa, colisiones |
| Árbol balanceado | búsquedas garantizadas | metadatos o estructura adicional para balance |

### Observación formal

Dos algoritmos pueden tener el mismo orden temporal $\Theta(\cdot)$ y distinto costo espacial. Por eso, al comparar estructuras de datos, el orden temporal no alcanza para decidir.

## Demostraciones de costos típicos

### Búsqueda binaria

Sea $T(n)$ el número de comparaciones de una búsqueda binaria sobre un arreglo ordenado de tamaño $n$.

La recurrencia es:

$$T(n) = T(\lfloor n/2 \rfloor) + c$$

con $T(1)=c_0$.

**Demostración de la cota.**

Desplegando la recurrencia $k$ veces:

$$T(n) = T(\lfloor n/2^k \rfloor) + kc$$

Cuando $\frac{n}{2^k}\le 1$, la recursión termina. Eso ocurre para $k \ge \log_2(n)$. Entonces:

$$T(n) \le c0 + c \log_2(n)$$

y por lo tanto $T(n)\in O(\log n)$. Como además se hace al menos una comparación por nivel, $T(n)\in \Omega(\log n)$. Luego:

$$T(n) \in \Theta(\log n)$$

### Inserción al final en un arreglo dinámico

Sea un arreglo dinámico que duplica su capacidad cuando se llena.

- Una inserción sin redimensionar cuesta $\Theta(1)$.
- Una inserción con redimensionamiento cuesta $\Theta(n)$ porque copia $n$ elementos.

La pregunta correcta es qué pasa en una secuencia larga de $m$ inserciones.

**Demostración por método agregado.**

Supongamos capacidades $1,2,4,8,\dots$. Cada vez que se duplica la capacidad, se copian exactamente tantos elementos como capacidad actual tenga el arreglo. Si las inserciones totales son $m$, cada elemento puede copiarse a lo sumo una vez por nivel de duplicación. La suma total de copias está acotada por:

$$1 + 2 + 4 + \cdots + 2^{\lfloor \log_2 m \rfloor} < 2m$$

El costo total de $m$ inserciones es entonces $\Theta(m)$, y el costo amortizado por inserción es:

$$T_{\text{total}}(m) \in \Theta(m) \;\Rightarrow\; \frac{T_{\text{total}}(m)}{m} \in \Theta(1)$$

Eso justifica que `append` en un vector dinámico sea $O(1)$ amortizado, aunque algunas inserciones individuales sean $O(n)$.

## Análisis amortizado

Algunas operaciones son raramente caras, pero la mayoría de las veces son baratas. En esos casos conviene mirar el costo **amortizado**, que promedia el tiempo de ejecución sobre una secuencia de operaciones.

### La Analogía del Ahorro (Método del Banquero o Monedas)

Para entender el costo amortizado, pensá en **ahorrar monedas (tokens) para pagar el costo futuro**:

1. Supongamos que cada operación básica cuesta "1 moneda" de tiempo de procesador.
2. Cada vez que hacés una operación rápida, le cobrás al usuario "3 monedas". Gastás 1 para hacer la operación y guardás las 2 restantes en una "alcancía".
3. Cuando llega la operación costosa, usás las monedas ahorradas para "pagarla" sin pedir tiempo extra, porque en promedio, el presupuesto de 3 monedas por operación cubrió todo el trabajo.

### Demostración del método de monedas para un vector dinámico

Asigná 3 unidades de crédito a cada $\texttt{append}$:

1. 1 unidad paga la escritura del nuevo elemento;
2. 2 unidades se guardan como crédito.

Cuando el arreglo se llena y hay que duplicar:

- cada elemento copiado consume 1 crédito,
- y esos créditos ya se fueron acumulando en inserciones previas.

Como cada elemento participa en a lo sumo una copia por redimensionamiento relevante, el crédito acumulado alcanza para cubrir todas las copias. Por lo tanto, la secuencia completa de inserciones tiene costo amortizado $\Theta(1)$ por operación.

### Ejemplo: Arreglo Dinámico (ArrayList)

Supongamos un vector que arranca vacío, se llena y entonces **duplica su capacidad**:

- El primer elemento cuesta 1 moneda insertarlo.
- Cuando se llena (tamaño $N$), el siguiente $\texttt{append}$ requiere copiar $N$ elementos viejos y agregar el nuevo. Costo real: $N + 1$ monedas.
- Pero durante las $N$ inserciones rápidas previas, ahorramos suficientes "monedas" para pagar la copia costosa.

Una inserción puntual puede costar $O(n)$ en tiempo real, pero una secuencia larga de inserciones al final tiene un **costo amortizado $O(1)$ por operación**, porque las inserciones constantes pagan el redimensionamiento esporádico.

```{code} java
:caption: Inserción al final con redimensionamiento ocasional

public void agregar(int valor) {
    if (this.cantidad == this.datos.length) {
        // Operación rara y costosa O(N)
        // Está "pagada" por los ahorros previos.
        redimensionar();
    }
    // Operación frecuente y rápida O(1)
    this.datos[this.cantidad] = valor;
    this.cantidad++;
}
```

El análisis amortizado no niega el costo caro. Lo ubica correctamente dentro de una secuencia larga de operaciones para demostrar que, a la larga, el sistema no se degrada.

### Qué no demuestra el costo amortizado

El amortizado no prueba que ninguna operación sea cara. Solo prueba que el promedio por operación, sobre una secuencia suficientemente larga, es acotado por una constante.

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

### Criterio de decisión

Si una operación aparece en casi todas las rutas de ejecución, su costo domina. Si solo aparece esporádicamente, puede tolerarse una operación cara siempre que el amortizado sea bajo y el espacio extra sea razonable.

## Qué errores conviene evitar

En esta parte conviene evitar varios abusos frecuentes:

1. **Usar Big-O como slogan.** “Es $O(1)$” no dice nada si no está claro qué caso se está midiendo.
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

```{exercise}
:label: ex-parte5-algoritmos-pruebas

Demostrá formalmente que una función $T(n)=a n+b$ pertenece a $\Theta(n)$ y aplicá la definición a un algoritmo concreto de tu elección.
```

## Próximo paso

Para seguir, conviene entrar a [Secuencias](secuencias/indice.md), donde estas herramientas empiezan a aplicarse sobre la familia lineal más general.
