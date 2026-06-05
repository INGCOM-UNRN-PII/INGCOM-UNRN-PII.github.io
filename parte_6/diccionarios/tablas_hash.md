---
title: "Tablas hash"
subtitle: "Acceso promedio rápido por clave"
subject: Estructuras de Datos
description: Cómo funciona el hashing, qué papel tienen las colisiones y por qué el buen rendimiento promedio depende de decisiones concretas.
---

(parte6-tablas-hash)=
# Tablas hash


## Objetivos observables

- Identificás el problema que modela el capítulo y el TAD/estructura más adecuada.
- Justificás decisiones de diseño con costo temporal/espacial y contrato de operaciones.
- Aplicás el contenido en Java sin romper invariantes ni contrato público.

Las tablas hash son probablemente la respuesta más usada cuando se quiere acceso rápido por clave. También son el lugar donde más claramente aparece la tensión entre muy buen promedio y peor caso problemático.

Su atractivo es fácil de entender: si una clave puede transformarse rápidamente en un índice o en un bucket, entonces buscar deja de parecerse a recorrer una lista y pasa a parecerse a “ir casi directo” al lugar correcto.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender cómo funciona el hashing, por qué aparecen colisiones y qué decisiones de implementación sostienen el rendimiento promedio.

**Prerrequisitos.** Conviene haber leído [Fundamentos de diccionarios y conjuntos](fundamentos.md) y recordar el análisis de costo promedio y peor caso.

**Desarrollo.** El capítulo presenta la idea de función hash, bucket y factor de carga, contrasta encadenamiento separado con direccionamiento abierto y cierra mostrando qué errores de diseño arruinan una tabla hash aunque la idea general sea correcta.
:::

## La idea central del hashing

Una tabla hash busca transformar una clave en una posición aproximada de almacenamiento mediante una **función hash**.

La intuición es:

1. tomar una clave,
2. calcular un número a partir de esa clave,
3. usar ese número para ubicar la entrada en una tabla.

```{mermaid}
flowchart LR
    Clave["Clave (ej: 'Ana')"] --> Hash[Función Hash]
    Hash -->|int: 21345| Compresión[Módulo o Compresión]
    Compresión -->|índice: 3| Bucket["Bucket [3]"]
    
    style Hash fill:#ffecb3,stroke:#ef6c00
    style Compresión fill:#ffecb3,stroke:#ef6c00
```

```{code} java
:caption: Esquema conceptual de acceso por hash

int indice = hash(clave) % capacidad;
// ir al bucket o posición asociada a ese índice
```

Si varias claves distintas caen en posiciones bien distribuidas, el acceso promedio puede ser muy eficiente.

### Algoritmos Básicos (Pseudocódigo)

En una tabla con **encadenamiento separado**, las operaciones se delegan al bucket correspondiente:

```text
algoritmo buscar(clave)
    indice ← hash(clave) modulo capacidad
    bucket ← tabla[indice]
    
    para cada (c, v) en bucket hacer
        si c == clave entonces
            devolver v
        fin si
    fin para
    
    devolver NULO
fin algoritmo

algoritmo insertar(clave, valor)
    indice ← hash(clave) modulo capacidad
    bucket ← tabla[indice]
    
    para cada entrada en bucket hacer
        si entrada.clave == clave entonces
            entrada.valor ← valor // Actualizar
            retornar
        fin si
    fin para
    
    bucket.agregar(clave, valor) // Insertar nuevo
    cantidad ← cantidad + 1
    
    si factorDeCarga() > LIMITE_REHASH entonces
        rehash()
    fin si
fin algoritmo
```

## Qué hace buena a una función hash

Una función hash útil no necesita ser “mágica”. Sí necesita cumplir varios criterios prácticos:

- ser rápida de calcular,
- dar siempre el mismo resultado para la misma clave,
- ser coherente con la noción de igualdad,
- distribuir claves de manera razonablemente uniforme.

:::{important}
Si dos claves son iguales según el criterio de la estructura, entonces su hash también debe coincidir. Si eso no se cumple, la tabla puede comportarse de forma incorrecta aunque compile perfecto.
:::

No hace falta que claves distintas produzcan hashes distintos. De hecho, eso suele ser imposible en dominios grandes. Por eso aparecen las colisiones.

## Colisiones: el problema inevitable

Una **colisión** ocurre cuando dos claves distintas terminan asociadas al mismo bucket o a la misma posición.

Las colisiones no son un bug. Son una consecuencia normal del hashing. El diseño real de una tabla hash depende de **cómo las resuelve**.

### Encadenamiento separado

Cada bucket guarda una colección de entradas:

- normalmente una lista,
- a veces otra estructura auxiliar.

Si dos claves caen en el mismo bucket, conviven en esa colección.

```{mermaid}
flowchart LR
    subgraph Tabla Hash
        direction TB
        B0["[0]"]
        B1["[1]"]
        B2["[2]"]
        B3["[3]"]
    end
    
    L1_1["Clave: A"]
    L1_2["Clave: X"]
    L3_1["Clave: C"]
    
    B1 --> L1_1 --> L1_2
    B3 --> L3_1
    
    style B0 fill:#eeeeee,stroke:#9e9e9e
    style B2 fill:#eeeeee,stroke:#9e9e9e
    style B1 fill:#bbdefb,stroke:#0288d1
    style B3 fill:#bbdefb,stroke:#0288d1
```

Ventajas:

- implementación conceptualmente simple,
- borrado natural,
- tolera mejor factores de carga altos.

Desventajas:

- requiere estructuras auxiliares por bucket,
- puede degradarse si muchos elementos caen juntos.

### Direccionamiento abierto

La tabla guarda todo dentro del arreglo principal. Si la posición esperada está ocupada, se busca otra según una estrategia de exploración (probing).

```{mermaid}
flowchart LR
    subgraph Tabla Hash Lineal
        direction TB
        B0["[0]"]
        B1["[1] Ocupado por A (Hash=1)"]
        B2["[2] Ocupado por B (Hash=1, colisiona, baja 1)"]
        B3["[3] Libre"]
    end
    
    style B1 fill:#ffcdd2,stroke:#d32f2f
    style B2 fill:#fff9c4,stroke:#fbc02d
    style B3 fill:#c8e6c9,stroke:#388e3c
```

Estrategias típicas:

- sondeo lineal,
- sondeo cuadrático,
- doble hashing.

Ventajas:

- buena localidad de memoria,
- menos estructuras auxiliares.

Desventajas:

- borrado más delicado,
- más sensibilidad al factor de carga,
- clustering si la estrategia está mal elegida.

## Factor de carga y redimensionamiento

El **factor de carga** mide qué tan llena está la tabla. En términos simples:

$$\text{factor de carga} = \frac{\text{cantidad de elementos}}{\text{capacidad de la tabla}}$$

Cuando el factor de carga crece demasiado:

- aumentan colisiones,
- se degradan búsquedas e inserciones,
- y conviene redimensionar y reubicar entradas.

Ese proceso suele llamarse **rehash**.

```{code} java
:caption: Idea general de rehash

if (factorDeCarga > limite) {
    redimensionar();
    reubicarTodasLasEntradas();
}
```

El rehash puede ser costoso en un momento puntual, pero muchas implementaciones lo aceptan para sostener buen rendimiento promedio a largo plazo.

## Qué operaciones suelen ofrecer

En una tabla hash típica aparecen operaciones como:

| Operación | Idea general |
| :--- | :--- |
| `put(clave, valor)` | insertar o actualizar asociación |
| `get(clave)` | recuperar valor |
| `remove(clave)` | borrar entrada |
| `containsKey(clave)` | consultar existencia |

En promedio, estas operaciones suelen ser muy eficientes **si**:

- la función hash distribuye bien,
- el factor de carga se controla,
- y la estrategia de colisiones está bien elegida.

En el peor caso, en cambio, varias pueden degradarse fuerte.

## Ejemplo conceptual

Supongamos una tabla con capacidad 7 y una función hash simple:

| Clave | Hash | Bucket |
| :--- | ---: | ---: |
| `"Ana"` | 15 | 1 |
| `"Luz"` | 22 | 1 |
| `"Paz"` | 8 | 1 |

Las tres claves caen en el mismo bucket. El acceso sigue siendo correcto si la tabla sabe manejar colisiones, pero el costo local ya no se parece al caso ideal.

Este ejemplo muestra algo importante: no alcanza con mirar la idea abstracta de “usar hash”. También importan:

- distribución real de claves,
- tamaño de tabla,
- estrategia de resolución,
- y cambios en la carga.

## Hashing vs otras estrategias

Conviene comparar tabla hash con otras estructuras cercanas:

| Estructura | Fuerte principal | Lo que no resuelve bien |
| :--- | :--- | :--- |
| Tabla hash | acceso promedio rápido por clave | orden, rangos, predecessor/successor |
| Diccionario ordenado | orden y rangos | lookup promedio tan barato como hash |
| Trie | prefijos | claves arbitrarias sin estructura interna útil |

Si el problema dominante es “buscar por clave exacta”, hash suele ser muy buen punto de partida.

Si además importa:

- recorrer en orden,
- pedir rangos,
- o explotar prefijos,

entonces probablemente haga falta otra estructura.

## Qué errores conviene evitar

En tablas hash conviene vigilar errores muy típicos:

1. **Elegir una función hash pobre.** Produce clustering y mal promedio.
2. **Ignorar el factor de carga.** La tabla parece funcionar, pero se degrada con el crecimiento.
3. **Pensar que el peor caso desaparece.** Hash mejora el promedio; no elimina todos los riesgos.
4. **Romper la coherencia entre igualdad y hash.** Eso puede volver incorrecto el comportamiento.
5. **Usar hash cuando el problema exige orden.**

:::{warning}
“Usar hash” no es una solución universal. Es una decisión muy buena para cierto tipo de lookup, pero mala si el problema necesita orden, rango o prefijos.
:::

## Resumen

Una tabla hash puede ser excelente para acceso por clave, pero su rendimiento depende de decisiones concretas:

- función hash,
- estrategia de colisiones,
- factor de carga,
- y política de redimensionamiento.

Su valor principal está en el muy buen comportamiento promedio para búsqueda, inserción y borrado por clave exacta. Su límite aparece cuando el problema necesita otra clase de garantía.

## Ejercicios

```{exercise}
:label: ex-parte6-tablas-hash-mini

Explicá por qué dos tablas hash con la misma interfaz pueden tener desempeños muy distintos según:

1. la función hash elegida,
2. el factor de carga,
3. y la forma de resolver colisiones.
```

```{exercise}
:label: ex-parte6-tablas-hash-orden

Justificá por qué una tabla hash no es la mejor elección si el problema exige listar claves en orden o responder consultas por rango de fechas.
```

## Próximo paso

Para seguir, conviene pasar a [Diccionarios ordenados](diccionarios_ordenados.md), donde el foco deja de estar en el promedio y pasa a incluir orden y consultas por rango.
