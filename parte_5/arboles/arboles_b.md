---
title: "Árboles B"
subtitle: "Balance y ramificación para almacenamiento externo"
subject: Estructuras de Datos
description: Por qué los árboles B y B+Tree se vuelven naturales cuando el costo dominante deja de ser la comparación y pasa a ser leer páginas o bloques.
---

(parte5-arboles-b)=
# Árboles B

Los árboles B cierran la familia con un cambio fuerte de contexto. Hasta acá casi toda la discusión giró alrededor de memoria principal, punteros, arreglos y altura. Cuando los datos viven en disco o en páginas de almacenamiento externo, el modelo de costo cambia: ya no importa tanto ahorrar una comparación, sino **reducir la cantidad de accesos a bloque**.

En ese escenario, un árbol binario deja de ser la opción natural. Si cada nodo solo abre dos caminos, la altura crece más de lo deseable. Los árboles B responden justamente a eso: usan **muchos hijos por nodo** para bajar la altura y aprovechar mejor cada lectura de página.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender por qué los árboles B aparecen cuando la unidad de costo relevante deja de ser la comparación y pasa a ser el acceso a bloque o página.

**Prerrequisitos.** Conviene haber leído [Árboles binarios de búsqueda](arboles_busqueda.md) y tener presente la discusión sobre altura y balance. También ayuda ver [Árboles balanceados](arboles_balanceados.md), porque acá el balance ya no es optativo.

**Desarrollo.** Primero se fija el cambio de modelo de costo. Después se presenta la idea de nodo multicamino, se distinguen B-Tree y B+Tree, y se cierra con división, fusión y usos como índices persistentes.
:::

## Qué problema resuelven

En memoria principal, muchas veces pensamos el costo en términos de comparaciones o de caminos entre nodos. En almacenamiento externo, el costo dominante suele ser otro:

1. leer una página desde disco o desde un nivel más lento de memoria,
2. escribir una página modificada,
3. bajar demasiados niveles para encontrar una clave.

Por eso la pregunta cambia. Ya no alcanza con "¿está balanceado?". Ahora importa:

- cuántas páginas hay que leer,
- cuánta información útil entra en cada página,
- y cuánta altura tiene el árbol en esa unidad de costo.

:::{important}
El árbol B no intenta ser una variante "más rara" del BST. Responde a otra realidad física: cuando una lectura de bloque cuesta mucho más que unas pocas comparaciones, conviene usar nodos grandes y bajar la altura.
:::

## Idea central: nodos con muchas claves y muchos hijos

Un árbol B no es binario. Cada nodo interno puede almacenar varias claves y abrir varios subárboles.

Una intuición útil es pensar que cada nodo representa una **página** o un **bloque** de almacenamiento:

- las claves dentro del nodo separan rangos,
- cada hijo apunta al subárbol que contiene uno de esos rangos,
- y una sola lectura trae muchas decisiones juntas.

```{mermaid}
graph TD
    A[15 | 28 | 42]
    A -->|"< 15"| B[7 | 10 | 12]
    A -->|"15 a 27"| C[16 | 20 | 25]
    A -->|"28 a 41"| D[30 | 35 | 39]
    A -->|">= 42"| E[45 | 50 | 55]
    
    classDef nodo fill:#f8f9fa,stroke:#333,stroke-width:2px;
    class A,B,C,D,E nodo;
```

La idea no es que el nodo "guarde de más", sino al revés: que amortice el costo de haber leído esa página entera desde un disco o red.

## Propiedades estructurales importantes

Sin entrar en una formalización excesiva, un árbol B mantiene estas ideas:

| Propiedad | Qué aporta |
| :--- | :--- |
| Muchos hijos por nodo | Baja altura |
| Todas las hojas a la misma profundidad | Balance global fuerte |
| Nodos ni vacíos ni sobrecargados, salvo casos especiales | Buen aprovechamiento del espacio |
| Claves ordenadas dentro de cada nodo | Permite decidir por qué hijo bajar |

Ese balance es más fuerte que en muchas estructuras binarias: no depende de que "haya salido bien" la secuencia de inserciones. Forma parte del contrato.

## Orden del árbol y factor de ramificación

Cada presentación define el "orden" de un árbol B con notaciones ligeramente distintas, pero la intuición estable es esta:

- existe un máximo de claves o hijos por nodo,
- existe también un mínimo razonable de ocupación,
- y raíz, nodos internos y hojas tienen reglas emparentadas pero no idénticas.

Lo importante para este curso no es memorizar una convención particular de letras. Lo importante es entender el efecto:

| Si aumenta el factor de ramificación... | Entonces pasa esto |
| :--- | :--- |
| cada nodo discrimina más rangos | se necesitan menos niveles |
| la altura baja | hay menos accesos a páginas |
| el árbol se ensancha | pero se vuelve mucho más apto para almacenamiento externo |

## Búsqueda: pocas páginas, varias comparaciones internas

Buscar en un árbol B se parece conceptualmente a buscar en un BST, pero con un paso más rico por nodo:

1. leés un nodo,
2. buscás dentro de sus claves dónde cae la clave objetivo,
3. si no está ahí, elegís el hijo correspondiente,
4. repetís hasta hoja o coincidencia.

```java
Nodo buscar(Nodo raiz, int clave) {
    Nodo actual = raiz;

    while (actual != null) {
        int i = 0;
        while (i < actual.cantidadClaves && clave > actual.claves[i]) {
            i++;
        }

        if (i < actual.cantidadClaves && clave == actual.claves[i]) {
            return actual;
        }

        if (actual.esHoja) {
            return null;
        }

        actual = actual.hijos[i];
    }

    return null;
}
```

No hace falta que el código sea el centro. La idea clave es esta: una lectura de nodo permite tomar varias decisiones porque el nodo contiene varias claves.

## Inserción: dividir antes de desbordar

Insertar en un árbol B significa bajar hasta la hoja adecuada, agregar la nueva clave y verificar si el nodo sigue respetando su capacidad máxima.

Si un nodo se pasa de tamaño, aparece la operación más característica de esta familia: **split** o división.

La división hace tres cosas:

1. separa el nodo en dos,
2. promociona una clave al nodo padre,
3. redistribuye hijos si hacía falta.

```text
[ 10 | 20 | 30 | 40 | 50 ]
          |
       split
          v
        [30]
       /    \
[10 | 20]  [40 | 50]
```

Si el padre también se llena, la división puede propagarse hacia arriba. Si llega a la raíz, se crea una nueva raíz y la altura aumenta en uno.

Eso es importante: el crecimiento vertical del árbol está cuidadosamente controlado.

## Borrado: fusión y redistribución

Eliminar es más delicado que insertar porque ahora el riesgo no es "tener demasiado", sino **quedar por debajo del mínimo de ocupación**.

Las estrategias típicas son:

| Estrategia | Qué hace |
| :--- | :--- |
| **Redistribución** | Toma una clave de un hermano vecino y ajusta el padre |
| **Fusión** | Une nodos hermanos y hace bajar una clave del padre |

La redistribución intenta arreglar localmente el problema sin reducir la cantidad de nodos. La fusión, en cambio, compacta estructura cuando ya no alcanza con prestar una clave.

Estas operaciones hacen que la implementación sea bastante más compleja que en un BST, pero compran la propiedad que acá más importa: altura muy baja y ocupación razonable por página.

## Diferencia entre B-Tree y B+Tree

Hasta acá hablamos de árbol B en sentido general. En la práctica aparecen dos variantes emparentadas:

| Variante | Dónde viven los datos | Qué la vuelve útil |
| :--- | :--- | :--- |
| **B-Tree** | Los datos pueden vivir en nodos internos y hojas | Menos separación conceptual |
| **B+Tree** | Los datos completos viven en hojas; los internos guían búsqueda | Mejor para recorridos secuenciales y rangos |

En un B+Tree:

- los nodos internos funcionan estrictamente como **índice de enrutamiento**,
- los datos reales (o punteros a los registros en disco) viven **solo en las hojas**,
- y las hojas suelen quedar **enlazadas entre sí**, formando una lista secuencial.

```{mermaid}
graph TD
    Raiz[40]
    Raiz -->|"< 40"| I1[15 | 25]
    Raiz -->|">= 40"| I2[60 | 80]
    
    I1 --> H1[10 | 12]
    I1 --> H2[15 | 20]
    I1 --> H3[25 | 30]
    
    I2 --> H4[40 | 50]
    I2 --> H5[60 | 70]
    I2 --> H6[80 | 90]
    
    H1 -.->|next| H2
    H2 -.->|next| H3
    H3 -.->|next| H4
    H4 -.->|next| H5
    H5 -.->|next| H6
    
    classDef indice fill:#e3f2fd,stroke:#0277bd,stroke-width:2px;
    classDef hoja fill:#fff3e0,stroke:#f57c00,stroke-width:2px;
    
    class Raiz,I1,I2 indice;
    class H1,H2,H3,H4,H5,H6 hoja;
```

Esta estructura enlazada hace que resolver una consulta por rango (ej. "todos los sueldos entre 20.000 y 70.000") sea trivial: el árbol busca el inicio del rango bajando por la raíz (muy rápido) y, una vez en la hoja correcta, avanza secuencialmente por los enlaces hasta pasarse del límite, sin tener que volver a navegar el árbol por cada elemento.

Por eso los B+Tree aparecen casi universalmente en índices de bases de datos relacionales y sistemas que necesitan:

1. búsquedas por clave exactas,
2. consultas por rango,
3. recorrido ordenado secuencial eficiente.

## Por qué no usar un árbol binario balanceado

La pregunta es válida: si ya existen AVL o Red-Black, ¿por qué no usarlos en disco?

La respuesta está en el factor de ramificación.

| Estructura | Hijos por nodo | Consecuencia en almacenamiento externo |
| :--- | :--- | :--- |
| AVL / Red-Black | 2 | Más altura, más páginas a leer |
| Árbol B | Muchos | Menos altura, mejor aprovechamiento de cada página |

Aunque una comparación interna dentro de un nodo grande cueste algo más, ese costo suele ser insignificante frente a evitar varias lecturas de bloque.

## Dónde se usan

### Índices de bases de datos

Cuando una tabla necesita encontrar registros por clave o por rango sin escanear todo, los árboles B y sobre todo los B+Tree resultan naturales.

### Sistemas de archivos

Directorios, metadatos e índices persistentes también se benefician de árboles de baja altura y alta ramificación.

### Estructuras persistentes grandes

Cuando el conjunto de datos supera cómodamente la memoria principal, deja de tener sentido pensar solo como si todo fuera un BST residente en RAM.

## Errores frecuentes

### Pensar el costo como si todo viviera en RAM

Si medís solo comparaciones, subestimás por qué los árboles B existen.

### Quedarse con la intuición binaria

Querer forzar hijos izquierdo y derecho hace perder la ventaja estructural central de esta familia: muchos caminos por nodo.

### Creer que más complejidad interna siempre es peor

En árboles B la lógica de split, merge y redistribución es más compleja, sí. Pero esa complejidad compra menos altura y menos I/O, que es exactamente lo que el problema necesita.

### Confundir B-Tree con B+Tree

Son cercanos, pero no idénticos. Si el problema necesita recorridos por rango muy eficientes, la diferencia importa.

## Resumen

Los árboles B aparecen cuando el modelo de costo cambia y leer páginas domina el tiempo. En ese contexto, la mejor decisión ya no es mantener nodos pequeños y binarios, sino agrupar muchas claves por nodo para bajar la altura.

El B-Tree y el B+Tree resuelven esa necesidad con nodos multicamino, balance global fuerte y operaciones de división, redistribución y fusión. Por eso resultan más adecuados que los árboles binarios clásicos cuando el índice vive en almacenamiento externo.

## Ejercicios

```{exercise}
:label: ex-parte5-arboles-b-mini

Explicá por qué una estructura con alto factor de ramificación puede ser ventajosa en disco aunque resulte menos natural que un árbol binario para explicar en clase.
```

```{exercise}
:label: ex-parte5-arboles-b-bplus

Compará B-Tree y B+Tree para un índice que necesita muchas consultas por rango. Indicá qué variante convendría más y por qué.
```

```{exercise}
:label: ex-parte5-arboles-b-costo

Justificá por qué en almacenamiento externo puede convenir hacer más comparaciones dentro de un nodo si eso evita leer varias páginas adicionales.
```

## Próximo paso

Para seguir, conviene pasar a [Grafos](../grafos/indice.md), donde la organización deja de ser jerárquica y pasa a ser completamente general.
