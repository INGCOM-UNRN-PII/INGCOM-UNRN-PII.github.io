---
title: "Iterator"
subtitle: "Acceder a elementos de una colección secuencialmente"
subject: Patrones de Diseño de Comportamiento
---

(patron-iterator)=
# Iterator: Recorrido Uniforme

El patrón **Iterator** proporciona una forma de acceder secuencialmente a los elementos de una colección sin exponer su estructura subyacente.

:::{note} Propósito

Recorrer elementos de colección sin exponer su estructura interna.
:::

---

## Origen e Historia

Gang of Four 1994. Surge de colecciones heterogéneas: necesidad de recorrer sin exponer estructura interna.

## Motivación

Necesario cuando:
- Múltiples estructuras (Lista, Árbol, Grafo)
- Cliente no debe conocer estructura interna
- Múltiples recorridos simultáneamente
- Quieres encapsulación

## Contexto

**Patrón:** Estructura → Iterator → Recorrido

**Anatomía:**
- **Iterator**: Interfaz (tieneProximo, proximo)
- **ConcreteIterator**: Implementación específica
- **Collection**: Crea iterador
- **Client**: Usa Iterator

**Variantes:**
- Iterator bidireccional
- Iterator con filtro
- Deep iterator (recorrido profundo)

---

## Problema

Iterator desacopla el algoritmo de recorrido de la estructura de datos:

```
Colección          Iterator
┌─────────┐        ┌──────────┐
│ [1,2,3] │──────→ │ actual=0 │ ← Cliente
└─────────┘        │ siguiente│
                   │ tieneNext│
                   └──────────┘
```

---

## Problema

```java
// ❌ Cliente acoplado a estructura específica
class Lista {
    private int[] elementos;
    
    public void recorrer() {
        for (int i = 0; i < elementos.length; i++) {
            System.out.println(elementos[i]);
        }
    }
}

class Árbol {
    private Nodo raíz;
    
    public void recorrer() {
        // Diferente lógica por cada estructura!
    }
}

// No es uniforme
```

---

## Solución: Iterator

```java
/**
 * Interfaz Iterator.
 */
public interface Iterador<T> {
    boolean tieneProximo();
    T proximo();
}

/**
 * Interfaz Colección.
 */
public interface Colección<T> {
    Iterador<T> crear Iterador();
}

/**
 * Implementación concreta: Lista.
 */
public class Lista<T> implements Colección<T> {
    private List<T> elementos = new ArrayList<>();
    
    public void agregar(T elemento) {
        elementos.add(elemento);
    }
    
    @Override
    public Iterador<T> crearIterador() {
        return new IteradorLista();
    }
    
    // Iterador interno (private)
    private class IteradorLista implements Iterador<T> {
        private int indice = 0;
        
        @Override
        public boolean tieneProximo() {
            return indice < elementos.size();
        }
        
        @Override
        public T proximo() {
            return elementos.get(indice++);
        }
    }
}

/**
 * Implementación concreta: Pila.
 */
public class Pila<T> implements Colección<T> {
    private List<T> elementos = new ArrayList<>();
    
    public void apilar(T elemento) {
        elementos.add(elemento);
    }
    
    public T desapilar() {
        if (!elementos.isEmpty()) {
            return elementos.remove(elementos.size() - 1);
        }
        return null;
    }
    
    @Override
    public Iterador<T> crearIterador() {
        return new IteradorPila();
    }
    
    // Iterador LIFO (Last In First Out)
    private class IteradorPila implements Iterador<T> {
        private int indice;
        
        public IteradorPila() {
            this.indice = elementos.size() - 1;
        }
        
        @Override
        public boolean tieneProximo() {
            return indice >= 0;
        }
        
        @Override
        public T proximo() {
            return elementos.get(indice--);
        }
    }
}

// ✅ Uso uniforme: El cliente no conoce la estructura
Colección<Integer> lista = new Lista<>();
lista.agregar(1);
lista.agregar(2);
lista.agregar(3);

Colección<Integer> pila = new Pila<>();
pila.apilar(10);
pila.apilar(20);
pila.apilar(30);

// Mismo código para ambas!
void recorrer(Colección<Integer> colección) {
    Iterador<Integer> it = colección.crearIterador();
    while (it.tieneProximo()) {
        System.out.println(it.proximo());
    }
}

recorrer(lista);  // 1, 2, 3
recorrer(pila);   // 30, 20, 10 (LIFO)
```

---

## Diagrama UML

```
    ┌──────────────────┐
    │   Iterador<T>    │
    │  <<interface>>   │
    ├──────────────────┤
    │+ tieneProximo()  │
    │+ proximo(): T    │
    └────────┬─────────┘
             ▲
             │
             │ implementa
             │
       ┌─────┴──────┐
       │             │
 ┌─────▼──────┐  ┌──▼──────────┐
 │IteradorLista│  │IteradorPila │
 ├──────────────┤  ├─────────────┤
 │- indice      │  │- indice     │
 │+ tieneProx() │  │+ tieneProx()│
 │+ proximo()   │  │+ proximo()  │
 └──────────────┘  └─────────────┘
        ▲                 ▲
        │ crea             │ crea
        │                  │
    ┌───┴──────────────────┴────┐
    │     Colección<T>           │
    │    <<interface>>           │
    ├───────────────────────────┤
    │+ crearIterador(): Iter.   │
    └─────────────┬─────────────┘
                  │
          ┌───────┴──────────┐
          │                  │
      ┌───▼──────┐      ┌───▼──────┐
      │  Lista   │      │  Pila    │
      ├──────────┤      ├──────────┤
      │- elementos│      │- elementos│
      │+ agregar()│      │+ apilar()│
      └──────────┘      └──────────┘
```

---

## Variantes

**1. Iterator bidireccional:**
```java
public interface IteradorBidireccional<T> extends Iterador<T> {
    boolean tieneAnterior();
    T anterior();
}
```

**2. Iterator con filtro:**
```java
public class IteradorFiltrado<T> implements Iterador<T> {
    private Iterador<T> iterador;
    private Predicate<T> filtro;
    // Implementar saltar elementos que no cumplen
}
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Uniformidad**: Mismo código para diferentes colecciones
- **Encapsulación**: Estructura interna oculta
- **Flexibilidad**: Múltiples iteradores simultáneamente
- **Facilidad**: Cliente no maneja índices

### ❌ Desventajas

- **Clases**: Más clases por cada colección
- **Performance**: Overhead vs. acceso directo
- **Complejidad**: Más difícil de entender inicialmente
- **Java built-in**: Ya existe Iterator en Java

---

## Cuándo Usarlo

✅ **Usa cuando:**
- Múltiples estructuras de datos diferentes
- Necesitas ocultar estructura interna
- Quieres múltiples recorridos simultáneamente
- Ejemplos: Bases de datos (cursores), colecciones personalizadas

❌ **Evita cuando:**
- Java Collections (ya implementa)
- Acceso aleatorio es crítico

---

## Ejercicio

```{exercise}
:label: ej-iterator-arbol

Crea iteradores para un árbol binario:
1. `IteradorInorden` (izq-raíz-der)
2. `IteradorPreorden` (raíz-izq-der)
3. `IteradorPostorden` (izq-der-raíz)
```

```{solution} ej-iterator-arbol
:class: dropdown

```java
public class Nodo {
    int valor;
    Nodo izq, der;
    
    public Nodo(int valor) {
        this.valor = valor;
    }
}

public interface IteradorÁrbol {
    boolean tieneProximo();
    int proximo();
}

public class IteradorInorden implements IteradorÁrbol {
    private Stack<Nodo> pila = new Stack<>();
    
    public IteradorInorden(Nodo raíz) {
        empujar_izquierdo(raíz);
    }
    
    private void empujar_izquierdo(Nodo nodo) {
        while (nodo != null) {
            pila.push(nodo);
            nodo = nodo.izq;
        }
    }
    
    @Override
    public boolean tieneProximo() {
        return !pila.isEmpty();
    }
    
    @Override
    public int proximo() {
        Nodo nodo = pila.pop();
        empujar_izquierdo(nodo.der);
        return nodo.valor;
    }
}

// Uso
Nodo raíz = new Nodo(5);
raíz.izq = new Nodo(3);
raíz.der = new Nodo(7);

IteradorInorden it = new IteradorInorden(raíz);
while (it.tieneProximo()) {
    System.out.println(it.proximo()); // 3, 5, 7
}
```
```
