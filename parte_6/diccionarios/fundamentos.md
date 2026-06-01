---
title: "Fundamentos de diccionarios y conjuntos"
subtitle: "Claves, pertenencia y asociaciones"
subject: Estructuras de Datos
description: Qué problemas modelan diccionarios y conjuntos, qué operaciones los definen y cómo se distinguen entre sí.
---

(parte6-fundamentos-diccionarios)=
# Fundamentos de diccionarios y conjuntos

Esta página instala la familia donde el problema central ya no es la posición, sino la pertenencia o la asociación por clave. Es el punto donde conviene distinguir con claridad mapa, conjunto y otras variantes relacionadas.

En una secuencia, la pregunta típica es “¿qué hay en la posición `i`?” o “¿qué viene antes y después?”. En esta familia, en cambio, las preguntas cambian:

- “¿existe una entrada con esta clave?”,
- “¿qué valor está asociado a esta clave?”,
- “¿cuáles son todas las claves entre este límite y este otro?”,
- “¿pertenecen estos dos elementos al mismo grupo?”.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender qué operaciones definen a un diccionario o a un conjunto y qué cambia cuando el acceso se organiza por clave.

**Prerrequisitos.** Conviene haber trabajado [Secuencias](../secuencias/indice.md), porque ayuda a contrastar estructuras lineales con estructuras orientadas a búsqueda.

**Desarrollo.** El capítulo define operaciones básicas, distingue diccionario de conjunto, conecta estas ideas con `Map` y `Set` en Java y prepara el terreno para hashing, orden y búsqueda por prefijo.
:::

## Del acceso posicional al acceso por clave

Un diccionario o un conjunto aparece cuando el problema necesita identificar elementos por alguna propiedad significativa.

### Ejemplos típicos

| Problema | Clave relevante | Tipo de estructura natural |
| :--- | :--- | :--- |
| Agenda telefónica | nombre o número | diccionario |
| Padrón de estudiantes | DNI | diccionario |
| Lista de palabras usadas | la palabra | conjunto |
| Catálogo por código | código del producto | diccionario |

Lo importante ya no es la posición en una lista, sino la capacidad de:

- recuperar rápido una entrada,
- actualizar una asociación,
- verificar pertenencia,
- o recorrer claves según algún criterio.

## Qué es un diccionario

Un **diccionario** modela una colección de pares **clave-valor**.

La idea abstracta es:

- cada clave identifica a lo sumo un valor asociado,
- se puede consultar si una clave existe,
- se puede agregar o reemplazar una asociación,
- se puede eliminar una clave y su valor.

Operaciones típicas:

| Operación | Intención |
| :--- | :--- |
| `put(clave, valor)` | agregar o actualizar asociación |
| `get(clave)` | recuperar valor asociado |
| `remove(clave)` | eliminar entrada |
| `containsKey(clave)` | consultar existencia de clave |

En Java, este contrato se parece a lo que ofrece `Map<K, V>`, pero en esta parte conviene pensar primero el TAD y después las implementaciones.

## Qué es un conjunto

Un **conjunto** modela una colección de elementos **sin repetidos**, donde el problema dominante suele ser la pertenencia.

Operaciones típicas:

| Operación | Intención |
| :--- | :--- |
| `add(x)` | agregar elemento |
| `remove(x)` | eliminar elemento |
| `contains(x)` | consultar pertenencia |
| `size()` | informar cantidad |

En Java, esto se parece a `Set<E>`.

La diferencia conceptual importante es:

- en un diccionario la clave apunta a un valor,
- en un conjunto el propio elemento cumple el rol de clave.

## Variantes dentro de la familia

No todos los problemas de clave se resuelven igual. En esta familia aparecen varias necesidades distintas.

### 1. Acceso promedio rápido

Es el caso típico de las **tablas hash**:

- interesa buscar, insertar y borrar rápido en promedio,
- no importa tanto el orden de recorrido,
- el foco está en función hash, colisiones y factor de carga.

### 2. Orden de claves

Es el caso de los **diccionarios ordenados**:

- importa recorrer claves en orden,
- consultar mínimos, máximos, rangos,
- encontrar predecessor o successor.

### 3. Prefijos o estructura interna de la clave

Es el caso de los **tries**:

- la clave no se trata como bloque indivisible,
- importa la secuencia de caracteres o símbolos,
- aparecen consultas por prefijo y autocompletado.

### 4. Partición dinámica

Es el caso de los **conjuntos disjuntos**:

- no interesa recuperar valores generales,
- interesa saber a qué grupo pertenece cada elemento,
- y unir grupos eficientemente.

## Igualdad, clave y comparador

Para que esta familia funcione bien, hace falta tener muy claro qué significa que dos claves sean “la misma clave”. En una secuencia, la identidad la da la posición (índice 3). En diccionarios y conjuntos, la identidad la da el contenido de la clave.

Ese criterio puede apoyarse en distintas reglas, que en Java se traducen a contratos específicos (ver {ref}`java-colecciones`):

| Estructura | Qué necesita de la clave | Contrato en Java |
| :--- | :--- | :--- |
| Tabla Hash | Función hash coherente con la igualdad | `equals(Object)` y `hashCode()` |
| Diccionario Ordenado | Comparación total consistente | `Comparable<T>` o `Comparator<T>` |
| Trie | Posibilidad de recorrer la clave por símbolos | Iteración de caracteres o bytes |

Este punto es **crítico**: una misma noción de “clave” puede comportarse bien en una estructura y romper otra. 

### El peligro de romper el contrato de igualdad

Si redefinís cómo se comparan dos objetos, pero te olvidás de ajustar su identificador numérico, las estructuras basadas en *hashing* van a fallar silenciosamente.

:::{warning} Contrato `equals` y `hashCode`
Si dos claves son iguales según `equals()`, **deben obligatoriamente** devolver el mismo `hashCode()`. Si rompés esta regla, podés insertar un valor en un diccionario y luego ser incapaz de encontrarlo, porque la tabla lo va a ir a buscar al "balde" equivocado.
:::

## Cómo se comparan con secuencias

Conviene no mezclar familias:

| Familia | Pregunta dominante |
| :--- | :--- |
| Secuencias | ¿en qué posición está? |
| Diccionarios y conjuntos | ¿existe esta clave? ¿qué valor tiene? |

Si el problema principal es lookup por clave, forzar una secuencia suele producir:

- búsquedas lineales,
- actualización manual,
- código cliente más frágil,
- y menor claridad conceptual.

## Qué errores conviene evitar

En esta familia conviene vigilar varios errores frecuentes:

1. **Usar una lista cuando el problema es de clave.** Suele funcionar al principio, pero escala mal.
2. **Confundir conjunto con diccionario.** No todo lookup necesita un valor asociado.
3. **Pensar que hash resuelve todos los casos.** Si importa orden o rango, no alcanza.
4. **Olvidar el criterio de igualdad o comparación.** Eso rompe el contrato de muchas estructuras.
5. **Elegir implementación sin fijar qué operación domina.**

## Resumen

Diccionarios y conjuntos son familias de problemas orientados a clave. Su comparación no pasa por el recorrido posicional, sino por:

- qué operación de búsqueda o actualización domina,
- qué garantías hacen falta,
- si el orden importa,
- y qué criterio define la identidad de las claves.

Desde acá en adelante, la pregunta central deja de ser “cómo recorrer” y pasa a ser “cómo localizar, organizar o agrupar”.

## Ejercicios

```{exercise}
:label: ex-parte6-fundamentos-diccionarios-mini

Tomá estos tres problemas:

1. padrón de estudiantes,
2. lista de palabras únicas,
3. agenda telefónica.

Indicá cuál modelarías como diccionario, cuál como conjunto y por qué.
```

```{exercise}
:label: ex-parte6-fundamentos-diccionarios-claves

Inventá un problema donde usar una secuencia sea una mala decisión porque la operación dominante es búsqueda por clave. Explicá qué se vuelve costoso y qué familia conviene usar en su lugar.
```

## Próximo paso

Para seguir, conviene pasar a [Tablas hash](tablas_hash.md), donde aparece la estrategia más directa para acceso rápido por clave.
