---
title: "Orden topológico"
subtitle: "Linearizar dependencias en DAG"
subject: Estructuras de Datos
description: Cómo linearizar dependencias en grafos dirigidos acíclicos y por qué la presencia de ciclos vuelve imposible ese tipo de orden.
---

(parte5-orden-topologico)=
# Orden topológico

El orden topológico muestra un caso donde el grafo no modela distancias ni costos, sino dependencias parciales. La pregunta ya no es “qué vértices conectan”, sino “qué debe ocurrir antes que qué”.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender cuándo un grafo dirigido acíclico admite una linearización válida y cómo construirla.

**Prerrequisitos.** Conviene haber leído [Fundamentos de grafos](fundamentos.md) y recordar [Recorridos](recorridos.md), porque este capítulo reutiliza grafos dirigidos, ciclos y exploración.

**Desarrollo.** El capítulo define DAG y precedencia parcial, muestra por qué un ciclo vuelve imposible el orden topológico, compara DFS con el algoritmo de Kahn y cierra con aplicaciones sobre planificación, correlatividades y compilación.
:::

## Qué problema resuelve

Un orden topológico intenta construir una lista lineal de vértices de modo que toda arista dirigida `u -> v` respete la precedencia:

- `u` debe aparecer antes que `v`.

Eso modela muy bien problemas como:

- materias con correlatividades,
- tareas de un cronograma,
- módulos de compilación,
- pasos de una receta o pipeline,
- dependencias entre paquetes.

Lo importante es entender que no se busca "el único orden correcto", sino **un orden válido** entre varios posibles.

## DAG: la condición estructural

El orden topológico solo tiene sentido en un **grafo dirigido acíclico** (*directed acyclic graph*, DAG).

La razón es directa:

- si existe un ciclo, cada vértice del ciclo depende indirectamente de sí mismo;
- entonces ninguna linearización puede ubicar a todos "antes" de todos.

Ejemplo:

```text
A -> B -> C -> A
```

Acá:

- `A` debe ir antes que `B`,
- `B` antes que `C`,
- y `C` antes que `A`.

No existe una lista lineal que satisfaga todo a la vez.

:::{important}
El orden topológico no "ordena cualquier digrafo". Solo funciona cuando la estructura expresa precedencias sin ciclos.
:::

## Qué significa una linearización válida

Dado un DAG, una salida posible puede ser:

```text
Introduccion -> Estructuras -> Grafos -> Proyecto
```

si las dependencias eran:

- Introducción antes que Estructuras,
- Estructuras antes que Grafos,
- Grafos antes que Proyecto.

Pero también podrían aparecer otros órdenes válidos si hay vértices independientes entre sí.

Eso importa porque el resultado no es necesariamente único. Lo que se exige es esta propiedad:

- toda arista debe apuntar desde un vértice que aparece antes hacia uno que aparece después.

## Estrategia 1: DFS y orden de finalización

Una forma clásica de construir el orden topológico usa DFS.

La idea intuitiva es:

1. profundizar por dependencias,
2. dejar un vértice en la salida cuando ya se procesaron sus sucesores,
3. invertir el orden de finalización.

En un DAG, ese esquema produce un orden válido porque un vértice se agrega recién cuando ya quedaron resueltas las partes que dependen de él.

```{code} java
:caption: Esquema simplificado de orden topológico por DFS

void dfsTopologico(Grafo<String> grafo,
                   String actual,
                   Set<String> visitados,
                   Deque<String> salida) {
    visitados.add(actual);

    for (String vecino : grafo.vecinosDe(actual)) {
        if (!visitados.contains(vecino)) {
            dfsTopologico(grafo, vecino, visitados, salida);
        }
    }

    salida.push(actual);
}
```

Esta versión es conceptualmente elegante, pero en digrafos generales necesita además distinguir estados de visita para detectar ciclos durante la exploración.

## Estrategia 2: algoritmo de Kahn

La segunda estrategia clásica trabaja con **grado de entrada**.

La observación central es:

- un vértice con grado de entrada cero no depende de ningún otro pendiente;
- por lo tanto, puede ir primero.

El algoritmo de Kahn hace esto:

1. calcula el grado de entrada de cada vértice;
2. pone en una cola todos los que tienen grado `0`;
3. extrae uno, lo agrega al orden;
4. reduce el grado de entrada de sus sucesores;
5. si alguno queda en `0`, lo encola.

```{code} java
:caption: Esquema de Kahn

Queue<String> pendientes = new ArrayDeque<>();

for (String v : grafo.vertices()) {
    if (gradoEntrada.get(v) == 0) {
        pendientes.add(v);
    }
}

while (!pendientes.isEmpty()) {
    String actual = pendientes.remove();
    orden.add(actual);

    for (String vecino : grafo.vecinosDe(actual)) {
        gradoEntrada.put(vecino, gradoEntrada.get(vecino) - 1);
        if (gradoEntrada.get(vecino) == 0) {
            pendientes.add(vecino);
        }
    }
}
```

Si al final quedaron vértices sin emitir, el grafo tenía al menos un ciclo.

## DFS vs Kahn

Conviene compararlos por la información que hacen visible:

| Estrategia | Idea dominante | Ventaja típica | Señal útil |
| :--- | :--- | :--- | :--- |
| DFS | orden de finalización | muy natural desde recorridos | conecta con detección de ciclos por estados |
| Kahn | grados de entrada | hace explícitas las dependencias resueltas | detecta ciclo cuando sobran vértices |

Ambas soluciones tienen el mismo espíritu:

- extraer una linearización compatible con las precedencias del grafo.

La elección concreta depende más de:

- qué estructura auxiliar resulta más clara,
- si ya se tiene implementado DFS,
- o si interesa trabajar explícitamente con grados de entrada.

## Dónde aparece

El orden topológico aparece en problemas donde importa producir una secuencia ejecutable:

### Correlatividades

Si una materia `B` exige haber aprobado `A`, entonces la arista puede pensarse como:

- `A -> B`

Un orden topológico posible describe una secuencia de cursada compatible con esas restricciones.

### Planificación de tareas

En proyectos con prerequisitos, un DAG permite calcular:

- qué tareas pueden empezar ya,
- cuáles están bloqueadas,
- y en qué orden general se puede avanzar.

### Compilación y dependencias de módulos

Si un módulo usa a otro, compilar en orden arbitrario puede fallar. El orden topológico describe una secuencia compatible con esas dependencias.

## Qué errores conviene evitar

1. **Pedir orden topológico en un grafo no dirigido.** La noción nace de precedencias dirigidas.
2. **Olvidar que puede haber más de una respuesta correcta.**
3. **Confundir ciclo con simple “dificultad de implementación”.** Si hay ciclo, el orden puede no existir.
4. **Pensar que orden topológico resuelve costos o distancias.** Resuelve precedencias, no optimización.

:::{warning}
Que un grafo tenga un camino entre dos vértices no alcanza. Para hablar de orden topológico hace falta además que no existan ciclos dirigidos.
:::

## Resumen

El orden topológico linealiza una relación parcial de precedencia. Solo existe en DAG y puede construirse con DFS o con Kahn.

Su valor no está en “ordenar mejor” un grafo, sino en responder una pregunta específica:

- dado un conjunto de dependencias, ¿qué secuencia respeta todas sin violarlas?

## Ejercicios

```{exercise}
:label: ex-parte5-orden-topologico-mini

Modelá un conjunto de materias con correlatividades como un grafo y explicá qué significaría encontrar un ciclo en ese contexto.
```

```{exercise}
:label: ex-parte5-orden-topologico-kahn

Explicá por qué un vértice con grado de entrada cero puede ubicarse sin violar dependencias. Después, describí qué indicio de ciclo aparece si el algoritmo de Kahn se detiene antes de emitir todos los vértices.
```

## Próximo paso

Para seguir, conviene pasar a [Conectividad](conectividad.md), donde la atención se concentra en componentes y particiones del grafo.
