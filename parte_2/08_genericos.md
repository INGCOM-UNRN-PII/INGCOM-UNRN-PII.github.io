---
title: "8: Genéricos en Java"
subtitle: "Parámetros de Tipo, Tipado Seguro y Reutilización de Código"
subject: Programación Orientada a Objetos
---

(java-genericos)=
# Java 8: Genéricos en Java

En los capítulos anteriores vimos cómo las colecciones facilitan almacenar grupos de objetos, pero hay un problema: **pérdida de tipo en tiempo de compilación**. Los genéricos resuelven esto permitiendo crear clases y métodos que funcionan con **cualquier tipo de dato mientras mantienen seguridad de tipos**.

Este capítulo cubre:

1. **El Problema sin Genéricos**: Casting inseguro y errores en tiempo de ejecución
2. **Creación de Clases Genéricas**: Parámetros de tipo y restricciones
3. **Métodos Genéricos**: Reutilización flexible de código
4. **Bounds (Límites de Tipo)**: Restricciones en parámetros de tipo
5. **Comodines (_Wildcards_)**: Flexibilidad en colecciones genéricas

:::{admonition} Objetivos de Aprendizaje
:class: tip

Al finalizar este capítulo, serás capaz de:

1. Comprender el problema que resuelven los genéricos
2. Crear clases genéricas reutilizables
3. Aplicar restricciones a parámetros de tipo
4. Escribir métodos genéricos
5. Usar comodines correctamente en colecciones
6. Evitar castings inseguros y errores de tipo
:::

:::{important}
**Requisitos previos**:
- Dominar la sintaxis de clases en Java ({ref}`java-sintaxis-clases`)
- Comprender herencia e interfaces ({ref}`oop3-herencia-polimorfismo`)
- Conocer colecciones básicas de Java ({ref}`java-colecciones`)
:::

---

(problema-sin-genericos)=
## El Problema: Pérdida de Tipo sin Genéricos

### Antes de los Genéricos

Antes de Java 5, las colecciones trabajaban con `Object`, lo que requería castings inseguros:

```java
// ❌ Sin genéricos: problemas de tipo
List contactos = new ArrayList();
contactos.add("Juan");
contactos.add("María");

// Hay que castear y esto puede fallar en tiempo de ejecución
for (Object obj : contactos) {
    String contacto = (String) obj;  // ¿Y si no es String?
    System.out.println(contacto);
}

// Alguien podría agregar un número por error
contactos.add(42);  // ¡Compilador no lo previene!

// Y esto crashea en ejecución:
String nombre = (String) contactos.get(2);  // ClassCastException!
```

### Problemas Específicos

1. **Sin verificación en compilación**: El compilador no valida tipos
2. **Castings necesarios**: Código repetitivo y propenso a errores
3. **Errores en tiempo de ejecución**: `ClassCastException` inesperada
4. **Código confuso**: No queda claro qué tipos contiene la colección
5. **Falta de documentación automática**: El código no es autodescriptivo

### La Solución: Genéricos

Los **genéricos** permiten especificar el tipo de dato que contiene una colección en tiempo de compilación:

```java
// ✅ Con genéricos: seguridad de tipos
List<String> contactos = new ArrayList<String>();
contactos.add("Juan");
contactos.add("María");

// No es necesario castear
for (String contacto : contactos) {
    System.out.println(contacto);
}

// El compilador rechaza tipos incorrectos:
// contactos.add(42);  // ❌ Error de compilación: incompatible types

// Acceso seguro sin casting:
String nombre = contactos.get(0);  // ✅ Ya es String
```

---

(que-son-genericos)=
## ¿Qué son los Genéricos?

**Genéricos** es un mecanismo que permite escribir código reutilizable que funciona con diferentes tipos, manteniendo **seguridad de tipos en tiempo de compilación**.

:::{admonition} Definición
:class: note

Un **genérico** es una clase, interfaz o método que declara **parámetros de tipo** (type parameters) usando variables de tipo como `<T>`, `<K>`, `<V>`, etc. El compilador verifica que el tipo usado sea compatible antes de ejecutar el código.
:::

### Sintaxis Básica

```java
// <T> es el parámetro de tipo (type parameter)
public class Caja<T> {
    private T contenido;
    
    public void guardar(T item) {
        contenido = item;
    }
    
    public T obtener() {
        return contenido;
    }
}

// Al usar la clase, especificamos el tipo concreto
Caja<String> cajaDeTexto = new Caja<>();
cajaDeTexto.guardar("Hola");
String mensaje = cajaDeTexto.obtener();  // Sin casting

Caja<Integer> cajaDeNumeros = new Caja<>();
cajaDeNumeros.guardar(42);
int numero = cajaDeNumeros.obtener();  // Sin casting
```

### Convención de Nombres para Parámetros de Tipo

Por convención, los parámetros de tipo usan **una sola letra mayúscula**:

- `<T>` = Type (tipo genérico)
- `<E>` = Element (elemento de una colección)
- `<K>` = Key (clave en un mapa)
- `<V>` = Value (valor en un mapa)
- `<N>` = Number (número)

---

(crear-clases-genericas)=
## Crear Clases Genéricas

### Ejemplo 1: Caja Genérica Simple

```java
/**
 * Caja genérica que almacena un único objeto de tipo T.
 * @param <T> el tipo de objeto que almacena la caja
 */
public class Caja<T> {
    private T contenido;
    
    public void guardar(T item) {
        this.contenido = item;
    }
    
    public T obtener() {
        return contenido;
    }
    
    public boolean estaVacia() {
        return contenido == null;
    }
}

// Uso:
Caja<String> cajaTexto = new Caja<>();
cajaTexto.guardar("Ajedrez");
System.out.println(cajaTexto.obtener());  // Ajedrez

Caja<Double> cajaNumeros = new Caja<>();
cajaNumeros.guardar(3.14);
System.out.println(cajaNumeros.obtener());  // 3.14
```

### Ejemplo 2: Pila Genérica (Colección Simple)

Vamos a implementar una estructura de pila genérica para entender mejor cómo funcionan:

```java
/**
 * Pila genérica implementada con nodos enlazados.
 * LIFO: Last In, First Out.
 * @param <T> el tipo de elementos en la pila
 */
public class Pila<T> {
    private Nodo<T> cima;
    private int tamaño;
    
    /**
     * Nodo interno de la pila.
     */
    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        
        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }
    
    public Pila() {
        cima = null;
        tamaño = 0;
    }
    
    /**
     * Apila un elemento en la cima.
     */
    public void apilar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        nuevoNodo.siguiente = cima;
        cima = nuevoNodo;
        tamaño++;
    }
    
    /**
     * Desapila el elemento en la cima.
     * @return el elemento desapilado
     * @throws IllegalStateException si la pila está vacía
     */
    public T desapilar() {
        if (estaVacia()) {
            throw new IllegalStateException("Pila vacía");
        }
        T dato = cima.dato;
        cima = cima.siguiente;
        tamaño--;
        return dato;
    }
    
    public T verCima() {
        if (estaVacia()) {
            return null;
        }
        return cima.dato;
    }
    
    public boolean estaVacia() {
        return cima == null;
    }
    
    public int getTamaño() {
        return tamaño;
    }
}

// Uso:
Pila<String> historial = new Pila<>();
historial.apilar("comando1");
historial.apilar("comando2");
historial.apilar("comando3");

while (!historial.estaVacia()) {
    System.out.println(historial.desapilar());
}
// Salida: comando3, comando2, comando1
```

### Ejemplo 3: Mapa Genérico Simple

```java
/**
 * Mapa genérico que asocia claves con valores.
 * @param <K> el tipo de las claves
 * @param <V> el tipo de los valores
 */
public class MiMapa<K, V> {
    private static final int CAPACIDAD = 16;
    private Entrada<K, V>[] tabla;
    private int cantidad;
    
    @SuppressWarnings("unchecked")
    public MiMapa() {
        tabla = new Entrada[CAPACIDAD];
        cantidad = 0;
    }
    
    /**
     * Nodo para almacenar pares clave-valor.
     */
    private static class Entrada<K, V> {
        K clave;
        V valor;
        Entrada<K, V> siguiente;
        
        Entrada(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }
    }
    
    /**
     * Asocia una clave con un valor.
     */
    public void poner(K clave, V valor) {
        if (clave == null) {
            throw new IllegalArgumentException("Clave no puede ser null");
        }
        
        int indice = Math.abs(clave.hashCode() % CAPACIDAD);
        Entrada<K, V> entrada = tabla[indice];
        
        // Buscar si la clave ya existe
        while (entrada != null) {
            if (entrada.clave.equals(clave)) {
                entrada.valor = valor;  // Actualizar
                return;
            }
            entrada = entrada.siguiente;
        }
        
        // Agregar nueva entrada
        Entrada<K, V> nueva = new Entrada<>(clave, valor);
        nueva.siguiente = tabla[indice];
        tabla[indice] = nueva;
        cantidad++;
    }
    
    /**
     * Obtiene el valor asociado a una clave.
     * @return el valor, o null si la clave no existe
     */
    public V obtener(K clave) {
        if (clave == null) {
            return null;
        }
        
        int indice = Math.abs(clave.hashCode() % CAPACIDAD);
        Entrada<K, V> entrada = tabla[indice];
        
        while (entrada != null) {
            if (entrada.clave.equals(clave)) {
                return entrada.valor;
            }
            entrada = entrada.siguiente;
        }
        
        return null;
    }
    
    public int getCantidad() {
        return cantidad;
    }
}

// Uso:
MiMapa<String, Integer> edades = new MiMapa<>();
edades.poner("Juan", 25);
edades.poner("María", 30);
edades.poner("Pedro", 28);

System.out.println(edades.obtener("Juan"));  // 25
```

---

(bounds-limites-tipo)=
## Bounds: Restricciones en Parámetros de Tipo

A veces queremos limitar qué tipos pueden ser usados en un parámetro de tipo. Esto se hace con **bounds**.

### Upper Bounds (Límite Superior)

Especifica que el tipo debe ser una subclase de una clase o implementar una interfaz:

```java
// <T extends Numero> significa: T debe ser Numero o subclase de Numero
public class Calculadora<T extends Number> {
    public double suma(T a, T b) {
        return a.doubleValue() + b.doubleValue();
    }
    
    public T maximo(T a, T b) {
        double aVal = a.doubleValue();
        double bVal = b.doubleValue();
        return aVal > bVal ? a : b;
    }
}

// Uso válido:
Calculadora<Integer> calc1 = new Calculadora<>();
System.out.println(calc1.suma(5, 3));  // 8.0

Calculadora<Double> calc2 = new Calculadora<>();
System.out.println(calc2.suma(5.5, 3.3));  // 8.8

// ❌ Uso inválido:
// Calculadora<String> calc3 = new Calculadora<>();  // Error: String no es Number
```

### Bounds con Interfaz

```java
/**
 * Clase que funciona solo con tipos comparables.
 * @param <T> debe implementar Comparable<T>
 */
public class Ordenador<T extends Comparable<T>> {
    /**
     * Retorna el mayor de dos elementos.
     */
    public T maximo(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }
    
    /**
     * Retorna el menor de dos elementos.
     */
    public T minimo(T a, T b) {
        return a.compareTo(b) < 0 ? a : b;
    }
}

// Uso:
Ordenador<String> ordTexto = new Ordenador<>();
System.out.println(ordTexto.maximo("Zebra", "Abedul"));  // Zebra

Ordenador<Integer> ordNums = new Ordenador<>();
System.out.println(ordNums.minimo(100, 50));  // 50
```

### Multiple Bounds

Un parámetro puede tener múltiples límites (una clase y varias interfaces):

```java
public interface Serializable { }
public interface Comparable<T> { int compareTo(T o); }

// <T extends Class & Interface1 & Interface2>
public class Contenedor<T extends Comparable<T> & Serializable> {
    // T debe implementar Comparable Y Serializable
}
```

---

(metodos-genericos)=
## Métodos Genéricos

No solo las clases pueden ser genéricas; también los métodos individuales:

### Método Genérico Simple

```java
public class Utilidad {
    /**
     * Imprime un array de cualquier tipo.
     * @param <T> el tipo de elementos del array
     * @param array el array a imprimir
     */
    public static <T> void imprimir(T[] array) {
        for (T elemento : array) {
            System.out.println(elemento);
        }
    }
    
    /**
     * Busca un elemento en un array.
     * @return el índice del elemento, o -1 si no está
     */
    public static <T> int buscar(T[] array, T elemento) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(elemento)) {
                return i;
            }
        }
        return -1;
    }
}

// Uso:
String[] nombres = {"Juan", "María", "Pedro"};
Utilidad.imprimir(nombres);

Integer[] numeros = {10, 20, 30, 40};
int indice = Utilidad.buscar(numeros, 30);
System.out.println("Índice: " + indice);  // 2
```

### Método Genérico con Bounds

```java
public class Estadisticas {
    /**
     * Calcula el máximo de una lista de números.
     */
    public static <T extends Number> double maximo(List<T> numeros) {
        if (numeros.isEmpty()) {
            throw new IllegalArgumentException("Lista vacía");
        }
        
        double max = Double.NEGATIVE_INFINITY;
        for (T numero : numeros) {
            max = Math.max(max, numero.doubleValue());
        }
        return max;
    }
    
    /**
     * Ordena una lista de elementos comparables.
     */
    public static <T extends Comparable<T>> void ordenar(List<T> lista) {
        // Ordenamiento simple (burbuja)
        for (int i = 0; i < lista.size() - 1; i++) {
            for (int j = 0; j < lista.size() - 1 - i; j++) {
                if (lista.get(j).compareTo(lista.get(j + 1)) > 0) {
                    // Intercambiar
                    T temp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, temp);
                }
            }
        }
    }
}

// Uso:
List<Integer> numeros = Arrays.asList(5, 2, 8, 1);
System.out.println(Estadisticas.maximo(numeros));  // 8.0

List<String> textos = Arrays.asList("Zebra", "Abedul", "Manzana");
Estadisticas.ordenar(textos);
System.out.println(textos);  // [Abedul, Manzana, Zebra]
```

---

(comodines-wildcards)=
## Comodines (Wildcards): Flexibilidad en Colecciones

A veces queremos trabajar con una colección sin especificar exactamente qué tipo contiene, pero aún así queremos algún nivel de seguridad. Para eso usamos **comodines** (`?`).

### Wildcard Simple

```java
/**
 * Imprime cualquier lista, sin importar qué tipo contiene.
 */
public static void imprimirLista(List<?> lista) {
    for (Object elemento : lista) {
        System.out.println(elemento);
    }
}

// Uso:
List<String> strings = Arrays.asList("A", "B", "C");
List<Integer> numeros = Arrays.asList(1, 2, 3);

imprimirLista(strings);   // Funciona
imprimirLista(numeros);   // Funciona
```

### Upper Bounded Wildcard

Acepta cualquier subtipo de una clase específica:

```java
/**
 * Suma una lista de números (Integer, Double, Float, etc.)
 */
public static double sumar(List<? extends Number> numeros) {
    double suma = 0;
    for (Number n : numeros) {
        suma += n.doubleValue();
    }
    return suma;
}

// Uso:
List<Integer> enteros = Arrays.asList(1, 2, 3);
System.out.println(sumar(enteros));  // 6.0

List<Double> decimales = Arrays.asList(1.5, 2.5, 3.0);
System.out.println(sumar(decimales));  // 7.0
```

### Lower Bounded Wildcard

Acepta la clase específica y sus superclases:

```java
/**
 * Agrega números a una lista.
 * Útil cuando necesitas garantizar que puedas escribir.
 */
public static void agregarNumeros(List<? super Integer> lista) {
    lista.add(1);
    lista.add(2);
    lista.add(3);
}

// Uso válido:
List<Integer> listaEnteros = new ArrayList<>();
agregarNumeros(listaEnteros);  // OK: List<Integer>

List<Number> listaNumeros = new ArrayList<>();
agregarNumeros(listaNumeros);  // OK: List<Number> es supertipo de Integer

// ❌ Inválido:
// List<Double> listaDoubles = new ArrayList<>();
// agregarNumeros(listaDoubles);  // Error: Double no es supertipo de Integer
```

---

(erasure-limitaciones)=
## Type Erasure y Limitaciones

### ¿Qué es Type Erasure?

Java implementa genéricos mediante **type erasure**: en tiempo de ejecución, toda información de tipo genérico se **borra**. Esto se debe a compatibilidad hacia atrás con código antiguo.

```java
List<String> strings = new ArrayList<>();
List<Integer> numeros = new ArrayList<>();

// En tiempo de compilación: diferentes tipos
// En tiempo de ejecución: IDÉNTICOS
System.out.println(strings.getClass() == numeros.getClass());  // true!
```

### Limitaciones que Resultan

1. **No puedes usar tipos primitivos**:
```java
// ❌ Error: los genéricos requieren tipos de referencia
// List<int> numeros = new ArrayList<>();

// ✅ Usa sus tipos wrapper
List<Integer> numeros = new ArrayList<>();
```

2. **No puedes crear arrays genéricos**:
```java
// ❌ Error: no puedes hacer esto
// Caja<String>[] cajas = new Caja<String>[10];

// ✅ Usa List en su lugar
List<Caja<String>> cajas = new ArrayList<>();
```

3. **No puedes acceder a información de tipo en tiempo de ejecución**:
```java
public <T> void procesar(T objeto) {
    // ❌ Error: no puedes hacer esto
    // if (T instanceof String) { ... }
    
    // ✅ Usa el objeto mismo
    if (objeto instanceof String) {
        String s = (String) objeto;
    }
}
```

---

(ejercicios)=
## Ejercicios

```{exercise}
:label: ej-caja-generica

Crea una clase genérica `Caja<T>` que:
1. Almacene un único elemento de tipo `T`
2. Tenga métodos `guardar(T item)` y `obtener()`
3. Tenga un método `estaVacia()` que retorne `true` si no hay contenido
4. Tenga un método `limpiar()` que vacíe la caja

Prueba la clase con tipos diferentes (String, Integer, Double).
```

```{solution} ej-caja-generica
:class: dropdown

```java
public class Caja<T> {
    private T contenido;
    
    public void guardar(T item) {
        this.contenido = item;
    }
    
    public T obtener() {
        return contenido;
    }
    
    public boolean estaVacia() {
        return contenido == null;
    }
    
    public void limpiar() {
        contenido = null;
    }
}

// Pruebas:
public class PruebaCaja {
    public static void main(String[] args) {
        // Con String
        Caja<String> cajaTexto = new Caja<>();
        cajaTexto.guardar("Hola");
        System.out.println(cajaTexto.obtener());  // Hola
        System.out.println(cajaTexto.estaVacia());  // false
        
        cajaTexto.limpiar();
        System.out.println(cajaTexto.estaVacia());  // true
        
        // Con Integer
        Caja<Integer> cajaNumeros = new Caja<>();
        cajaNumeros.guardar(42);
        System.out.println(cajaNumeros.obtener());  // 42
        
        // Con Double
        Caja<Double> cajaDecimales = new Caja<>();
        cajaDecimales.guardar(3.14);
        System.out.println(cajaDecimales.obtener());  // 3.14
    }
}
```
```

```{exercise}
:label: ej-par-generica

Crea una clase genérica `Par<K, V>` que:
1. Almacene una clave y un valor de tipos independientes
2. Tenga métodos `getClave()` y `getValor()`
3. Tenga un método `toString()` que retorne "(clave=valor)"
4. Tenga un método `intercambiar()` que retorne un nuevo Par con clave y valor intercambiados

Nota: Para intercambiar necesitarás hacer que `Par` tenga un tipo adicional.
```

```{solution} ej-par-generica
:class: dropdown

```java
public class Par<K, V> {
    private K clave;
    private V valor;
    
    public Par(K clave, V valor) {
        this.clave = clave;
        this.valor = valor;
    }
    
    public K getClave() {
        return clave;
    }
    
    public V getValor() {
        return valor;
    }
    
    @Override
    public String toString() {
        return "(" + clave + "=" + valor + ")";
    }
    
    // Intercambia clave y valor, invirtiendo tipos
    public Par<V, K> intercambiar() {
        return new Par<>(valor, clave);
    }
}

// Pruebas:
public class PruebaPar {
    public static void main(String[] args) {
        Par<String, Integer> pareja = new Par<>("edad", 25);
        System.out.println(pareja);  // (edad=25)
        
        Par<Integer, String> invertido = pareja.intercambiar();
        System.out.println(invertido);  // (25=edad)
        
        Par<String, Double> precio = new Par<>("costo", 19.99);
        System.out.println(precio);  // (costo=19.99)
    }
}
```
```

```{exercise}
:label: ej-contenedor-comparable

Crea una clase genérica `ContenedorOrdenado<T extends Comparable<T>>` que:
1. Almacene tres elementos del tipo `T` (que debe ser Comparable)
2. Tenga un método `agregarElemento(T elem)` que inserte manteniendo orden
3. Tenga un método `obtenerMenor()` que retorne el elemento mínimo
4. Tenga un método `obtenerMayor()` que retorne el elemento máximo

Prueba con String e Integer.
```

```{solution} ej-contenedor-comparable
:class: dropdown

```java
public class ContenedorOrdenado<T extends Comparable<T>> {
    private T[] elementos;
    private int cantidad;
    
    @SuppressWarnings("unchecked")
    public ContenedorOrdenado() {
        elementos = new Comparable[3];
        cantidad = 0;
    }
    
    public void agregarElemento(T elem) {
        if (cantidad >= elementos.length) {
            throw new IllegalStateException("Contenedor lleno");
        }
        
        // Insertar en posición ordenada
        int pos = cantidad;
        for (int i = 0; i < cantidad; i++) {
            if (elem.compareTo((T) elementos[i]) < 0) {
                pos = i;
                break;
            }
        }
        
        // Desplazar elementos
        for (int i = cantidad; i > pos; i--) {
            elementos[i] = elementos[i - 1];
        }
        
        elementos[pos] = elem;
        cantidad++;
    }
    
    public T obtenerMenor() {
        if (cantidad == 0) {
            return null;
        }
        return (T) elementos[0];
    }
    
    public T obtenerMayor() {
        if (cantidad == 0) {
            return null;
        }
        return (T) elementos[cantidad - 1];
    }
}

// Pruebas:
public class PruebaContenedor {
    public static void main(String[] args) {
        ContenedorOrdenado<Integer> contenedor = new ContenedorOrdenado<>();
        contenedor.agregarElemento(30);
        contenedor.agregarElemento(10);
        contenedor.agregarElemento(20);
        
        System.out.println("Menor: " + contenedor.obtenerMenor());  // 10
        System.out.println("Mayor: " + contenedor.obtenerMayor());  // 30
        
        ContenedorOrdenado<String> textos = new ContenedorOrdenado<>();
        textos.agregarElemento("Cebra");
        textos.agregarElemento("Abedul");
        textos.agregarElemento("Manzana");
        
        System.out.println("Menor: " + textos.obtenerMenor());  // Abedul
        System.out.println("Mayor: " + textos.obtenerMayor());  // Cebra
    }
}
```
```

---

(resumen)=
## Resumen

Los genéricos son un pilar fundamental de Java moderno:

- **Seguridad de tipos**: El compilador valida tipos en tiempo de compilación
- **Reutilización**: Una clase genérica funciona con infinitos tipos
- **Sin castings**: Código más limpio y sin errores de tipo en ejecución
- **Documentación**: El código es autodescriptivo

```{table} Conceptos Clave de Genéricos
:label: tbl-genericos-resumen

| Concepto | Ejemplo | Uso |
| :--- | :--- | :--- |
| **Clase genérica** | `class Caja<T>` | Almacenar cualquier tipo |
| **Método genérico** | `<T> void imprimir(T obj)` | Lógica reutilizable |
| **Upper bound** | `<T extends Number>` | Restringir a subtipo |
| **Wildcard** | `List<?>` | Flexibilidad sin garantía |
| **Upper wildcard** | `List<? extends Number>` | Leer cualquier Number |
| **Lower wildcard** | `List<? super Integer>` | Escribir Integer o supertipo |
```

Para profundizar, consultá:
- {ref}`java-colecciones` para colecciones genéricas
- {ref}`oop3-herencia-polimorfismo` para entender polimorfismo (base de genéricos)
