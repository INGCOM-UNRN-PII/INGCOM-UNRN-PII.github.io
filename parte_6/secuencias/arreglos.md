---
title: "Arreglos"
subtitle: "Representación contigua y acceso aleatorio"
subject: Estructuras de Datos
description: Cuándo conviene modelar una secuencia con memoria contigua, qué costo pagan las ediciones y cómo cambian los arreglos dinámicos y los buffers circulares.
---

(parte6-arreglos)=
# Arreglos

En {ref}`arreglos-en-java` ya viste la sintaxis básica de `[]`, la creación con `new` y el acceso por índice. Acá cambia el foco: ya no alcanza con saber usar un arreglo. Ahora importa entender **qué compromisos de diseño** asumís cuando representás una secuencia con memoria contigua.

Los arreglos son la implementación lineal más directa cuando interesa acceder por posición con costo bajo y la memoria contigua es una ventaja. También son el punto donde más claramente aparece el trade-off entre **leer rápido** y **editar caro**.

:::{note} Hoja de ruta del capítulo
**Objetivo.** Entender por qué los arreglos son fuertes en acceso por índice y débiles en inserciones o borrados internos.

**Prerrequisitos.** Conviene haber leído [Fundamentos de secuencias](fundamentos.md), porque este capítulo implementa varias de esas operaciones sobre memoria contigua. También ayuda tener fresco {ref}`arreglos-en-java`.

**Desarrollo.** Primero se fija qué aporta la representación contigua. Después se distingue tamaño lógico de capacidad, se comparan arreglos fijos y dinámicos, y se cierra con buffers circulares como variante orientada a colas.
:::

## Qué resuelven bien los arreglos

Un arreglo organiza los elementos en posiciones contiguas. Esa decisión vuelve muy barato el acceso por índice: si conocés la posición, llegás directo al elemento, sin recorrer nodos intermedios ni seguir referencias.

Si venís de C, la intuición es la misma: la dirección de un elemento puede calcularse a partir de una base y un desplazamiento. En Java no manipulás direcciones explícitas, pero el beneficio estructural sigue ahí: acceso posicional simple, buen recorrido secuencial y buena localidad de memoria.

| Rasgo | Qué aporta |
| :--- | :--- |
| Memoria contigua | Recorridos lineales eficientes y buen uso de caché |
| Índices enteros | Acceso y actualización directa por posición |
| Estructura simple | Invariantes fáciles de expresar y controlar |
| Representación compacta | Poco overhead por elemento frente a estructuras enlazadas |

Eso vuelve a los arreglos especialmente útiles cuando:

1. dominan las consultas por índice,
2. el recorrido completo aparece seguido,
3. las inserciones en el medio son raras,
4. o el tamaño máximo puede estimarse razonablemente.

## Tamaño lógico y capacidad

En una secuencia basada en arreglo conviene separar dos ideas:

| Idea | Qué significa |
| :--- | :--- |
| **Capacidad** | Cuántas posiciones físicas tiene el arreglo |
| **Tamaño lógico** | Cuántos elementos válidos hay realmente cargados |

Un error muy común es tratarlos como si fueran lo mismo. Si reservás un arreglo de 10 posiciones para guardar 4 elementos, la capacidad es 10, pero el tamaño lógico es 4. Las posiciones restantes no forman parte de la secuencia todavía.

```{mermaid}
block-beta
  columns 10
  A["A"] B["B"] C["C"] D["D"] E["(libre)"] F["(libre)"] G["(libre)"] H["(libre)"] I["(libre)"] J["(libre)"]
  
  style A fill:#e3f2fd,stroke:#0277bd
  style B fill:#e3f2fd,stroke:#0277bd
  style C fill:#e3f2fd,stroke:#0277bd
  style D fill:#e3f2fd,stroke:#0277bd
  
  style E fill:#f5f5f5,stroke:#9e9e9e,stroke-dasharray: 5 5
  style F fill:#f5f5f5,stroke:#9e9e9e,stroke-dasharray: 5 5
  style G fill:#f5f5f5,stroke:#9e9e9e,stroke-dasharray: 5 5
  style H fill:#f5f5f5,stroke:#9e9e9e,stroke-dasharray: 5 5
  style I fill:#f5f5f5,stroke:#9e9e9e,stroke-dasharray: 5 5
  style J fill:#f5f5f5,stroke:#9e9e9e,stroke-dasharray: 5 5
```
*(Nota: Las posiciones ocupadas conforman el **tamaño lógico** (4); el total de bloques es la **capacidad** (10).)*

:::{important} Invariante de representación típica
En una secuencia implementada con arreglo suele cumplirse:

- `0 <= cantidad`
- `cantidad <= datos.length`
- las posiciones `0 .. cantidad - 1` contienen elementos válidos
- las posiciones `cantidad .. datos.length - 1` representan capacidad disponible

Esa frontera entre datos válidos y espacio libre forma parte del contrato interno de la estructura. Si la rompés, no falló la sintaxis: falló la representación. La idea conecta directamente con {ref}`oop-contratos`.
:::

## Arreglo fijo: simple cuando el máximo es conocido

Si el problema tiene un máximo claro, un arreglo fijo es la opción más directa. Pasa, por ejemplo, con los asientos de una sala, los meses del año o una agenda que no debería superar cierto límite.

En ese contexto, el diseño típico combina:

- un arreglo físico,
- una variable `cantidad`,
- y operaciones que respetan la invariante anterior.

```java
public final class ListaDeEspera {
    private final String[] personas;
    private int cantidad;

    public ListaDeEspera(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva");
        }
        this.personas = new String[capacidad];
        this.cantidad = 0;
    }

    public void agregarAlFinal(String persona) {
        if (this.cantidad == this.personas.length) {
            throw new IllegalStateException("No hay lugar disponible");
        }

        this.personas[this.cantidad] = persona;
        this.cantidad++;
    }

    public String obtener(int indice) {
        this.validarIndice(indice);
        return this.personas[indice];
    }

    public void insertarEn(int indice, String persona) {
        if (this.cantidad == this.personas.length) {
            throw new IllegalStateException("No hay lugar disponible");
        }
        if (indice < 0 || indice > this.cantidad) {
            throw new IndexOutOfBoundsException("Indice invalido: " + indice);
        }

        for (int i = this.cantidad; i > indice; i--) {
            this.personas[i] = this.personas[i - 1];
        }

        this.personas[indice] = persona;
        this.cantidad++;
    }

    public String eliminarEn(int indice) {
        this.validarIndice(indice);

        String eliminada = this.personas[indice];
        for (int i = indice; i < this.cantidad - 1; i++) {
            this.personas[i] = this.personas[i + 1];
        }

        this.cantidad--;
        this.personas[this.cantidad] = null;
        return eliminada;
    }

    private void validarIndice(int indice) {
        if (indice < 0 || indice >= this.cantidad) {
            throw new IndexOutOfBoundsException("Indice invalido: " + indice);
        }
    }
}
```

El código muestra el trade-off central:

- `obtener(indice)` accede directo,
- `agregarAlFinal()` es barato mientras quede espacio,
- `insertarEn()` y `eliminarEn()` necesitan correr una cola de elementos.

| Operación | Costo típico en arreglo fijo | Motivo |
| :--- | :--- | :--- |
| Obtener por índice | O(1) | Acceso directo |
| Actualizar por índice | O(1) | Escritura directa |
| Agregar al final | O(1) si hay capacidad | Se escribe en `cantidad` |
| Insertar al principio o al medio | O(n) | Hay que desplazar elementos |
| Eliminar al principio o al medio | O(n) | Hay que cerrar el hueco |
| Buscar por valor | O(n) | No hay acceso por clave |

## Arreglo dinámico: misma idea, capacidad adaptable

El arreglo fijo falla cuando no podés prever cuántos elementos vas a guardar. La salida no es abandonar la representación contigua, sino **agregar una política de redimensionamiento**.

Esa es la idea del arreglo dinámico: mantener la misma estructura base, pero crear un arreglo más grande cuando la capacidad ya no alcanza.

```java
public final class ListaDinamicaDeTareas {
    private String[] tareas;
    private int cantidad;

    public ListaDinamicaDeTareas() {
        this.tareas = new String[4];
        this.cantidad = 0;
    }

    public void agregarAlFinal(String tarea) {
        this.asegurarCapacidad();
        this.tareas[this.cantidad] = tarea;
        this.cantidad++;
    }

    private void asegurarCapacidad() {
        if (this.cantidad < this.tareas.length) {
            return;
        }

        String[] nuevo = new String[this.tareas.length * 2];
        for (int i = 0; i < this.cantidad; i++) {
            nuevo[i] = this.tareas[i];
        }
        this.tareas = nuevo;
    }
}
```

La operación cara es el redimensionamiento: cuando el arreglo se llena, hay que reservar otro más grande y copiar todo. Pero esa copia **no ocurre en cada inserción**, sino de manera ocasional. Por eso:

- el peor caso de un `append` puede ser O(n),
- pero el costo amortizado de agregar al final sigue siendo O(1) si la capacidad crece geométricamente.

:::{note}
No conviene confundir **costo amortizado** con **garantía de peor caso**. Si tu contrato exige latencia estricta en cada operación, ese detalle importa.
:::

La mayoría de las listas redimensionables de bibliotecas estándar siguen esta idea. En Java, por ejemplo, `ArrayList` encapsula exactamente esa decisión de diseño; el foco práctico de uso aparece en {ref}`java-colecciones`.

## Buffer circular: cuando importa operar en los extremos

Si implementaras una cola con un arreglo común y cada `dequeue` obligara a correr todos los elementos una posición hacia la izquierda, desperdiciarías la principal fortaleza del arreglo.

El buffer circular evita ese corrimiento reinterpretando el arreglo como si el final estuviera conectado con el comienzo. En vez de mover datos, movés índices.

```java
public final class ColaDeTurnos {
    private final int[] datos;
    private int frente;
    private int cantidad;

    public ColaDeTurnos(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva");
        }

        this.datos = new int[capacidad];
        this.frente = 0;
        this.cantidad = 0;
    }

    public void encolar(int valor) {
        if (this.cantidad == this.datos.length) {
            throw new IllegalStateException("La cola esta llena");
        }

        int fondo = (this.frente + this.cantidad) % this.datos.length;
        this.datos[fondo] = valor;
        this.cantidad++;
    }

    public int desencolar() {
        if (this.cantidad == 0) {
            throw new IllegalStateException("La cola esta vacia");
        }

        int valor = this.datos[this.frente];
        this.frente = (this.frente + 1) % this.datos.length;
        this.cantidad--;
        return valor;
    }
}
```

La mejora conceptual es fuerte: el arreglo sigue siendo contiguo, pero la secuencia lógica ya no coincide siempre con el tramo `0 .. cantidad - 1`. Ahora importa:

- dónde está el frente (`frente`),
- cuántos elementos hay (`cantidad`),
- y cómo se calcula el fondo con aritmética modular: `(frente + cantidad) % capacidad`.

```{mermaid}
flowchart LR
    subgraph Buffer Circular
        direction LR
        0(libre) --- 1(frente: A) --- 2(B) --- 3(fondo: C) --- 4(libre) --- 5(libre)
        
        style 1 fill:#c8e6c9,stroke:#388e3c
        style 2 fill:#c8e6c9,stroke:#388e3c
        style 3 fill:#c8e6c9,stroke:#388e3c
    end
```
*Si seguimos encolando hasta el final físico, el módulo `% capacidad` hace que el índice "pegue la vuelta" y el próximo elemento se encole en la posición `0`, siempre que esté libre.*

Este patrón aparece mucho en colas, buffers de entrada/salida y estructuras donde ambos extremos son relevantes. Más adelante vuelve en [Colas](colas.md) y [Deques](deques.md).

## Comparación rápida de variantes

| Variante | Qué optimiza | Qué paga | Cuándo conviene |
| :--- | :--- | :--- | :--- |
| Arreglo fijo | Simplicidad y acceso por índice | Capacidad rígida | Cuando el máximo es conocido |
| Arreglo dinámico | Acceso por índice y crecimiento gradual | Copias ocasionales al redimensionar | Cuando domina `append` y el tamaño real es incierto |
| Buffer circular | Operar en frente y fondo sin corrimientos | Más cuidado con índices e invariantes | Cuando modelás colas o buffers de turnos |

La decisión no es "qué estructura es mejor", sino **qué costo querés pagar**:

- si querés acceso aleatorio barato, el arreglo es muy fuerte;
- si querés crecer sin límite conocido, necesitás redimensionamiento;
- si querés editar seguido en el medio, la memoria contigua empieza a jugar en contra.

## Errores de diseño frecuentes

### Exponer el arreglo interno

Si un método devuelve directamente la referencia al arreglo interno, el cliente puede romper la representación desde afuera.

```java
public String[] datosInternos() {
    return this.tareas; // mala idea
}
```

Con eso, cualquier código cliente podría escribir donde no corresponde, saltear validaciones o inconsistir `cantidad` con el contenido real. La estructura deja de controlar su propio contrato.

### Confundir capacidad con cantidad

`datos.length` no dice cuántos elementos lógicos tiene la secuencia. Dice cuánto espacio físico reservaste. Si recorrés hasta `length` cuando solo hay `cantidad` elementos válidos, empezás a procesar basura lógica o `null`.

### Prometer O(1) sin aclarar el contexto

En un arreglo dinámico, agregar al final es O(1) amortizado, no O(1) de peor caso. Decirlo sin la distinción correcta confunde el análisis.

### Usar arreglos cuando dominan inserciones internas

Si el problema obliga a insertar y borrar seguido en el medio, cada operación arrastra corrimientos. Ahí conviene comparar con una representación enlazada antes de seguir agregando parches.

## Resumen

Los arreglos son una muy buena representación cuando el problema necesita acceso por posición, recorrido lineal eficiente y bajo overhead estructural. Su fuerza viene de la memoria contigua.

Esa misma decisión explica su límite: insertar o borrar en el medio cuesta porque hay que preservar el orden físico. Los arreglos dinámicos alivian el problema del tamaño incierto, y los buffers circulares evitan corrimientos cuando el trabajo se concentra en los extremos, pero ninguno borra el trade-off de base.

## Ejercicios

```{exercise}
:label: ex-parte6-arreglos-mini

Compará un arreglo fijo con un arreglo dinámico para implementar una lista de reproducción. Indicá qué operaciones quedan favorecidas y cuáles se vuelven más costosas.
```

```{exercise}
:label: ex-parte6-arreglos-buffer-circular

Explicá por qué una cola implementada con arreglo común y corrimientos tiene peor comportamiento que una implementada con buffer circular. Mostrá qué índices habría que mantener para evitar mover elementos.
```

```{exercise}
:label: ex-parte6-arreglos-eleccion

Tenés que modelar tres problemas: un tablero de 8x8, un historial que crece de forma impredecible y una cola de impresión. Para cada caso, decidí entre arreglo fijo, arreglo dinámico o buffer circular y justificá la elección.
```

## Próximo paso

Si en este capítulo te quedó claro que el costo de los corrimientos domina cuando editás seguido la secuencia, el paso natural es [Listas enlazadas](listas_enlazadas.md), donde la prioridad cambia de memoria contigua a edición local.
