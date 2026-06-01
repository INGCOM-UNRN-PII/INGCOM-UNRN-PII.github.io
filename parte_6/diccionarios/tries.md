---
title: "Tries"
subtitle: "Búsqueda por prefijos y claves descomponibles"
subject: Estructuras de Datos
description: Cómo funcionan los tries, por qué sirven para prefijos y qué trade-offs introducen.
---

(parte5-tries)=
# Tries

Los tries muestran otra forma de organizar búsquedas: en lugar de usar una clave como bloque indivisible, aprovechan su estructura interna, por ejemplo caracteres o símbolos, para resolver consultas por prefijo con naturalidad.

Ese cambio de punto de vista es fuerte. Tanto una tabla hash como un árbol ordenado tratan la clave como un valor completo:

- se hashea entera,
- o se compara entera.

Un trie, en cambio, la trata como una **secuencia de decisiones**.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender cuándo conviene modelar las claves como secuencias de símbolos y no solo como valores comparables o hasheables.

**Prerrequisitos.** Conviene haber leído [Diccionarios ordenados](diccionarios_ordenados.md), porque este capítulo vuelve a cambiar el criterio de organización del acceso por clave.

**Desarrollo.** El capítulo presenta trie básico y variantes comprimidas, muestra su costo en función de la longitud de la clave y discute sus aplicaciones sobre cadenas y prefijos.
:::

## La idea central

En un trie (del inglés *retrieval*):

- cada arista o cada paso representa un símbolo (ej. una letra),
- cada camino desde la raíz hasta un nodo marcado representa una clave válida,
- y los nodos intermedios comparten prefijos comunes.

### Ejemplo conceptual

Si se almacenan estas palabras: `casa`, `caso`, `canto`, el prefijo `ca` no se duplica tres veces en memoria. Queda compartido estructuralmente en el árbol.

```{mermaid}
flowchart TD
    Raiz(( )) -->|c| N1(( ))
    N1 -->|a| N2(( ))
    
    N2 -->|s| N3(( ))
    N3 -->|a| N4(((casa)))
    N3 -->|o| N5(((caso)))
    
    N2 -->|n| N6(( ))
    N6 -->|t| N7(( ))
    N7 -->|o| N8(((canto)))
    
    style N4 fill:#c8e6c9,stroke:#388e3c,stroke-width:2px
    style N5 fill:#c8e6c9,stroke:#388e3c,stroke-width:2px
    style N8 fill:#c8e6c9,stroke:#388e3c,stroke-width:2px
```
*(Los nodos con borde verde doble indican el final de una palabra válida).*

Esa propiedad vuelve muy natural responder preguntas como:

- “¿existe alguna clave que empiece con `ca`?”,
- “¿cuáles son las palabras con este prefijo?”,
- “¿hay una clave exacta igual a esta secuencia?”.

## Qué problema resuelven

Los tries son especialmente buenos cuando:

- las claves son cadenas o secuencias de símbolos,
- importa buscar por prefijo,
- el autocompletado es parte del problema,
- o conviene explotar prefijos compartidos.

Operaciones típicas:

| Operación | Qué resuelve |
| :--- | :--- |
| búsqueda exacta | verificar si una clave completa existe |
| búsqueda por prefijo | saber si algún camino continúa |
| inserción | agregar una clave reutilizando prefijos comunes |
| listado por prefijo | devolver todas las claves que comparten inicio |

## Cómo se representa

La forma más simple usa:

- un nodo por prefijo alcanzado,
- referencias a hijos por símbolo,
- una marca para indicar si ese nodo cierra una clave válida.

```{code} java
:caption: Idea simplificada de nodo de trie

class NodoTrie {
    private Map<Character, NodoTrie> hijos;
    private boolean esFinDePalabra;
}
```

La estructura concreta puede variar, pero la idea sigue siendo la misma: avanzar símbolo por símbolo. Si el alfabeto es muy grande (ej. Unicode entero), guardar los hijos en un `Map` es razonable; si el alfabeto es pequeño (ej. 26 letras de a-z), un arreglo estático es mucho más veloz.

```{code} java
:caption: Implementación de un nodo para un Trie de letras minúsculas (alfabeto de tamaño fijo).

class NodoTrie {
    /** Arreglo donde el índice 0 es 'a', el 1 es 'b', etc. */
    private final NodoTrie[] hijos = new NodoTrie[26];
    
    /** True si el camino desde la raíz hasta este nodo forma una palabra ingresada. */
    private boolean esFinDePalabra = false;

    /**
     * @param c un caracter entre 'a' y 'z'.
     * @return el nodo hijo correspondiente, o null si no existe.
     */
    public NodoTrie obtenerHijo(char c) {
        return this.hijos[c - 'a'];
    }
    
    /**
     * Crea un hijo si no existe y lo devuelve.
     */
    public NodoTrie crearHijoSiFalta(char c) {
        int indice = c - 'a';
        if (this.hijos[indice] == null) {
            this.hijos[indice] = new NodoTrie();
        }
        return this.hijos[indice];
    }
    
    public void marcarComoFin() {
        this.esFinDePalabra = true;
    }
}
```

## Qué costo tienen

El costo temporal típico de búsqueda o inserción en un trie **no depende de N** (la cantidad total de palabras guardadas en el diccionario) como ocurre en los árboles binarios de búsqueda. Depende exclusivamente de **L** (la longitud de la clave).

- Insertar `"casa"` cuesta exactamente 4 pasos, sin importar si el diccionario tiene 10 palabras o 10 millones. Es `O(L)`.
- Buscar `"casa"` cuesta exactamente 4 pasos. Es `O(L)`.

Eso puede ser una ventaja gigante cuando:

- las claves no son demasiado largas,
- hay muchos prefijos compartidos,
- o la consulta por prefijo domina el uso.

También puede ser una desventaja cuando:

- las claves son muy largas,
- el alfabeto es grande,
- o la memoria disponible es limitada.

## Variantes importantes

### Trie básico

Es la versión más directa:

- un paso por símbolo,
- nodos explícitos,
- fácil de entender,
- puede gastar bastante memoria.

### Trie comprimido o radix tree

Reduce nodos intermedios innecesarios agrupando segmentos de camino que no tienen ramificaciones.

```{mermaid}
flowchart LR
    subgraph Trie Basico
        direction TB
        R1(( )) -->|r| N1(( ))
        N1 -->|o| N2(( ))
        N2 -->|m| N3(( ))
        N3 -->|a| N4(((roma)))
        N3 -->|e| N5(( ))
        N5 -->|o| N6(((romeo)))
    end

    subgraph Trie Comprimido
        direction TB
        R2(( )) -->|rom| N7(( ))
        N7 -->|a| N8(((roma)))
        N7 -->|eo| N9(((romeo)))
    end
    
    style N4 fill:#c8e6c9,stroke:#388e3c,stroke-width:2px
    style N6 fill:#c8e6c9,stroke:#388e3c,stroke-width:2px
    style N8 fill:#c8e6c9,stroke:#388e3c,stroke-width:2px
    style N9 fill:#c8e6c9,stroke:#388e3c,stroke-width:2px
```

Ventaja:

- menor costo de memoria y menos saltos de punteros para caminos largos sin desvíos.

Desventaja:

- implementación más compleja (hay que partir y fusionar aristas al insertar nuevas palabras).

## Trie vs hash vs diccionario ordenado

| Pregunta dominante | Hash | Diccionario ordenado | Trie |
| :--- | :--- | :--- | :--- |
| clave exacta | muy bueno | bueno | bueno |
| prefijo | malo | no natural | excelente |
| orden global | no | sí | a veces, según recorrido |
| costo de memoria | moderado | moderado a alto | puede ser alto |

Si el problema principal es lookup exacto, hash suele ser más simple.

Si el problema principal es orden o rango, conviene un diccionario ordenado.

Si el problema principal es prefijo o autocompletado, el trie gana expresividad.

## Aplicaciones típicas

Los tries aparecen de forma natural en problemas como:

- autocompletado,
- correctores ortográficos,
- búsqueda de prefijos en diccionarios,
- routing por prefijos,
- análisis léxico.

## Qué errores conviene evitar

En tries conviene vigilar varios errores frecuentes:

1. **Usarlos cuando el problema no necesita prefijos.** Ahí suelen ser más complejos de lo necesario.
2. **Subestimar el costo de memoria.**
3. **Pensar que reemplazan a cualquier diccionario.** No es así.
4. **Ignorar el tamaño del alfabeto.**

:::{warning}
Un trie no es “mejor hash”. Es otra forma de organizar la búsqueda, útil cuando la estructura interna de la clave importa.
:::

## Resumen

Cuando la estructura interna de la clave importa, un trie puede resolver consultas que una tabla hash o un árbol ordenado no modelan con la misma naturalidad.

Su fuerza está en:

- compartir prefijos,
- buscar por símbolo,
- y responder autocompletado o prefijos de forma natural.

Su costo aparece sobre todo en memoria y complejidad de implementación.

## Ejercicios

```{exercise}
:label: ex-parte5-tries-mini

Explicá por qué un autocompletado por prefijo suele ser un mejor candidato para tries que para tablas hash. Indicá también qué costo adicional podría aparecer.
```

```{exercise}
:label: ex-parte5-tries-vs-ordenados

Compará trie y diccionario ordenado para almacenar palabras de un diccionario. Indicá en qué consulta gana cada uno:

1. búsqueda exacta,
2. búsqueda por prefijo,
3. recorrido ordenado completo.
```

## Próximo paso

Para seguir, conviene pasar a [Conjuntos disjuntos](conjuntos_disjuntos.md), donde ya no interesa recuperar valores sino mantener particiones dinámicas.
