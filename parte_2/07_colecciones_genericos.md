---
title: "Colecciones en Java"
subtitle: "Estructuras de Datos Eficientes para Almacenar Grupos de Objetos"
subject: Programación Orientada a Objetos
---

(java-colecciones)=
# Colecciones en Java

En los capítulos anteriores aprendimos a crear clases, usar herencia y polimorfismo. Ahora abordamos un tema fundamental para cualquier programa real: **cómo almacenar y manipular grupos de objetos** de manera eficiente.

Este capítulo cubre:

1. **El Framework de Colecciones**: Listas, conjuntos, mapas y colas
2. **Iteración**: Diferentes formas de recorrer colecciones
3. **Comparación y Ordenamiento**: `Comparable` y `Comparator`

Para un tratamiento profundo de los **genéricos** (tipado seguro para colecciones y clases propias), consultá {ref}`java-genericos`.

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, serás capaz de:

1. Elegir la colección adecuada para cada problema
2. Iterar colecciones con diferentes técnicas
3. Ordenar y comparar objetos correctamente
4. Implementar `Comparable` y `Comparator`
:::

:::{important}
**Requisitos previos**:
- Dominar la sintaxis de clases en Java ({ref}`java-sintaxis-clases`)
- Comprender herencia e interfaces ({ref}`java-herencia-polimorfismo`)
- Conocer arreglos básicos de Java

**Nota sobre genéricos:** Este capítulo usa genéricos en los ejemplos (ej: `List<String>`). 
Para comprender genéricos en profundidad, consultá el capítulo dedicado: {ref}`java-genericos`.
:::

---

(problema-arreglos)=
## El Problema con los Arreglos

### Limitaciones de los Arreglos

Los arreglos de Java tienen limitaciones significativas:

```java
// 1. Tamaño fijo: hay que conocerlo al crear
String[] nombres = new String[10];

// 2. No se puede redimensionar
// Si necesitamos 11 elementos, hay que crear uno nuevo y copiar

// 3. Operaciones manuales
// Insertar en el medio requiere desplazar elementos

// 4. No hay métodos de búsqueda ni manipulación
// Hay que implementar todo manualmente
```

**Ejemplo del problema:**

```java
public class ListaContactos {
    private String[] contactos;
    private int cantidad;
    
    public ListaContactos(int capacidadMaxima) {
        contactos = new String[capacidadMaxima];
        cantidad = 0;
    }
    
    public void agregar(String contacto) {
        if (cantidad >= contactos.length) {
            // ¡Problema! Hay que redimensionar manualmente
            String[] nuevo = new String[contactos.length * 2];
            System.arraycopy(contactos, 0, nuevo, 0, cantidad);
            contactos = nuevo;
        }
        contactos[cantidad++] = contacto;
    }
    
    public void eliminar(int indice) {
        // Hay que desplazar todos los elementos
        for (int i = indice; i < cantidad - 1; i++) {
            contactos[i] = contactos[i + 1];
        }
        contactos[--cantidad] = null;
    }
    
    // Mucho código repetitivo para operaciones básicas...
}
```

### La Solución: El Framework de Colecciones

Java proporciona el **Java Collections Framework**: un conjunto de interfaces y clases que implementan estructuras de datos comunes con operaciones optimizadas.

```java
import java.util.ArrayList;
import java.util.List;

public class ListaContactos {
    private List<String> contactos;
    
    public ListaContactos() {
        contactos = new ArrayList<>();  // Se redimensiona automáticamente
    }
    
    public void agregar(String contacto) {
        contactos.add(contacto);  // ¡Una línea!
    }
    
    public void eliminar(int indice) {
        contactos.remove(indice);  // ¡Una línea!
    }
    
    public boolean contiene(String contacto) {
        return contactos.contains(contacto);  // ¡Ya implementado!
    }
}
```

---

(jerarquia-colecciones)=
## Jerarquía de Colecciones

### Vista General

El Framework de Colecciones está organizado en una jerarquía de interfaces:

```{mermaid}
classDiagram
    class Iterable~E~ {
        <<interface>>
        +iterator() Iterator~E~
    }
    
    class Collection~E~ {
        <<interface>>
        +add(E) boolean
        +remove(Object) boolean
        +contains(Object) boolean
        +size() int
        +isEmpty() boolean
        +clear()
    }
    
    class List~E~ {
        <<interface>>
        +get(int) E
        +set(int, E) E
        +add(int, E)
        +remove(int) E
        +indexOf(Object) int
    }
    
    class Set~E~ {
        <<interface>>
        +add(E) boolean
    }
    
    class Queue~E~ {
        <<interface>>
        +offer(E) boolean
        +poll() E
        +peek() E
    }
    
    class ArrayList~E~ {
        -Object[] elementData
        -int size
        +add(E) boolean
        +get(int) E
    }
    
    class LinkedList~E~ {
        -Node~E~ first
        -Node~E~ last
        +addFirst(E)
        +addLast(E)
    }
    
    class HashSet~E~ {
        -HashMap~E,Object~ map
        +add(E) boolean
    }
    
    class TreeSet~E~ {
        -TreeMap~E,Object~ map
        +add(E) boolean
    }
    
    class Map~K,V~ {
        <<interface>>
        +put(K, V) V
        +get(Object) V
        +remove(Object) V
        +containsKey(Object) boolean
        +keySet() Set~K~
    }
    
    class HashMap~K,V~ {
        -Node~K,V~[] table
        +put(K, V) V
    }
    
    Iterable <|.. Collection
    Collection <|.. List
    Collection <|.. Set
    Collection <|.. Queue
    List <|.. ArrayList
    List <|.. LinkedList
    Queue <|.. LinkedList
    Set <|.. HashSet
    Set <|.. TreeSet
    
    note for List "Secuencia ordenada<br>Permite duplicados<br>Acceso por índice"
    note for Set "Sin duplicados<br>No garantiza orden<br>(salvo TreeSet)"
    note for Map "Pares clave-valor<br>Claves únicas<br>No hereda Collection"
```

### Interfaces Principales

| Interface | Descripción | Características |
| :--- | :--- | :--- |
| `List<E>` | Secuencia ordenada | Permite duplicados, acceso por índice |
| `Set<E>` | Conjunto sin duplicados | No permite duplicados |
| `Queue<E>` | Cola (FIFO) | Operaciones de encolar/desencolar |
| `Map<K,V>` | Diccionario clave-valor | Claves únicas, valores asociados |

---

(interface-list)=
## List: Listas Ordenadas

### Características de List

- **Ordenadas**: Los elementos mantienen el orden de inserción
- **Acceso por índice**: Se puede acceder a cualquier posición
- **Permite duplicados**: Puede contener el mismo elemento varias veces
- **Permite null**: Puede contener elementos nulos

### Implementaciones Principales

(arraylist-java)=
#### ArrayList

`ArrayList` es la implementación más usada. Internamente usa un arreglo que se redimensiona automáticamente.

```java
import java.util.ArrayList;
import java.util.List;

public class EjemploArrayList {
    public static void main(String[] args) {
        // Crear lista
        List<String> frutas = new ArrayList<>();
        
        // Agregar elementos
        frutas.add("Manzana");
        frutas.add("Banana");
        frutas.add("Naranja");
        frutas.add("Manzana");  // Duplicado permitido
        
        // Acceder por índice
        String primera = frutas.get(0);  // "Manzana"
        
        // Modificar elemento
        frutas.set(1, "Pera");  // Reemplaza "Banana" por "Pera"
        
        // Insertar en posición específica
        frutas.add(1, "Uva");  // Inserta "Uva" en posición 1
        
        // Eliminar
        frutas.remove("Naranja");     // Por objeto
        frutas.remove(0);             // Por índice
        
        // Tamaño
        int cantidad = frutas.size();  // 3
        
        // Verificar contenido
        boolean tienePera = frutas.contains("Pera");  // true
        
        // Obtener índice
        int indice = frutas.indexOf("Pera");  // 0
        
        // Verificar si está vacía
        boolean vacia = frutas.isEmpty();  // false
        
        // Limpiar
        frutas.clear();
    }
}
```

**Rendimiento de ArrayList:**

| Operación | Complejidad | Descripción |
| :--- | :---: | :--- |
| `get(index)` | O(1) | Acceso directo |
| `add(elemento)` | O(1)* | Agrega al final (*amortizado) |
| `add(index, elemento)` | O(n) | Debe desplazar elementos |
| `remove(index)` | O(n) | Debe desplazar elementos |
| `contains(elemento)` | O(n) | Búsqueda lineal |

(linkedlist-java)=
#### LinkedList

`LinkedList` implementa una lista doblemente enlazada. Es más eficiente para inserciones/eliminaciones frecuentes.

```java
import java.util.LinkedList;
import java.util.List;

public class EjemploLinkedList {
    public static void main(String[] args) {
        LinkedList<String> tareas = new LinkedList<>();
        
        // Operaciones de lista
        tareas.add("Tarea 1");
        tareas.add("Tarea 2");
        
        // Operaciones adicionales de LinkedList
        tareas.addFirst("Tarea urgente");   // Al principio
        tareas.addLast("Tarea final");      // Al final
        
        String primera = tareas.getFirst();  // "Tarea urgente"
        String ultima = tareas.getLast();    // "Tarea final"
        
        tareas.removeFirst();  // Elimina "Tarea urgente"
        tareas.removeLast();   // Elimina "Tarea final"
    }
}
```

**¿Cuándo usar cada una?**

| Situación | Usar |
| :--- | :--- |
| Acceso frecuente por índice | `ArrayList` |
| Muchas inserciones/eliminaciones al inicio | `LinkedList` |
| Caso general | `ArrayList` (más eficiente en memoria) |

---

(interface-set)=
## Set: Conjuntos Sin Duplicados

### Características de Set

- **Sin duplicados**: Cada elemento aparece una sola vez
- **Sin orden garantizado** (excepto implementaciones ordenadas)
- **Un solo null** permitido (en la mayoría de implementaciones)

### Implementaciones Principales

(hashset-java)=
#### HashSet

`HashSet` es la implementación más rápida. No garantiza orden.

```java
import java.util.HashSet;
import java.util.Set;

public class EjemploHashSet {
    public static void main(String[] args) {
        Set<String> paises = new HashSet<>();
        
        // Agregar elementos
        paises.add("Argentina");
        paises.add("Brasil");
        paises.add("Chile");
        paises.add("Argentina");  // ¡No se agrega! Ya existe
        
        System.out.println(paises.size());  // 3
        
        // Verificar existencia (muy rápido)
        boolean tieneArgentina = paises.contains("Argentina");  // true
        
        // Eliminar
        paises.remove("Brasil");
        
        // Iterar (orden no garantizado)
        for (String pais : paises) {
            System.out.println(pais);
        }
    }
}
```

**Rendimiento de HashSet:**

| Operación | Complejidad | Descripción |
| :--- | :---: | :--- |
| `add(elemento)` | O(1) | Muy rápido |
| `remove(elemento)` | O(1) | Muy rápido |
| `contains(elemento)` | O(1) | Muy rápido |

:::{important}
**Requisito para HashSet**: Los objetos deben implementar correctamente `hashCode()` y `equals()` (ver {ref}`equals-hashcode`). Si no, el comportamiento es impredecible.
:::

(treeset-java)=
#### TreeSet

`TreeSet` mantiene los elementos **ordenados**. Internamente usa un árbol rojo-negro.

```java
import java.util.TreeSet;
import java.util.Set;

public class EjemploTreeSet {
    public static void main(String[] args) {
        Set<Integer> numeros = new TreeSet<>();
        
        numeros.add(5);
        numeros.add(2);
        numeros.add(8);
        numeros.add(1);
        numeros.add(9);
        
        // Los elementos están ordenados
        for (Integer n : numeros) {
            System.out.print(n + " ");  // 1 2 5 8 9
        }
        
        // Operaciones adicionales de TreeSet
        TreeSet<Integer> ts = new TreeSet<>(numeros);
        
        System.out.println(ts.first());    // 1 (menor)
        System.out.println(ts.last());     // 9 (mayor)
        System.out.println(ts.lower(5));   // 2 (menor que 5)
        System.out.println(ts.higher(5));  // 8 (mayor que 5)
        System.out.println(ts.floor(6));   // 5 (menor o igual a 6)
        System.out.println(ts.ceiling(6)); // 8 (mayor o igual a 6)
    }
}
```

**Rendimiento de TreeSet:**

| Operación | Complejidad | Descripción |
| :--- | :---: | :--- |
| `add(elemento)` | O(log n) | Mantiene orden |
| `remove(elemento)` | O(log n) | Mantiene orden |
| `contains(elemento)` | O(log n) | Búsqueda binaria |

(linkedhashset-java)=
#### LinkedHashSet

`LinkedHashSet` mantiene el **orden de inserción**.

```java
import java.util.LinkedHashSet;
import java.util.Set;

public class EjemploLinkedHashSet {
    public static void main(String[] args) {
        Set<String> colores = new LinkedHashSet<>();
        
        colores.add("Rojo");
        colores.add("Verde");
        colores.add("Azul");
        
        // Mantiene el orden de inserción
        for (String color : colores) {
            System.out.println(color);  // Rojo, Verde, Azul (en ese orden)
        }
    }
}
```

**¿Cuándo usar cada Set?**

| Situación | Usar |
| :--- | :--- |
| Máxima velocidad, sin importar orden | `HashSet` |
| Elementos ordenados naturalmente | `TreeSet` |
| Mantener orden de inserción | `LinkedHashSet` |

---

(interface-map)=
## Map: Diccionarios Clave-Valor

### Características de Map

- **Asocia claves con valores**: Cada clave tiene un valor asociado
- **Claves únicas**: No puede haber claves duplicadas
- **Valores duplicados**: Los valores sí pueden repetirse
- **Un null como clave** (en `HashMap`)

### Implementaciones Principales

(hashmap-java)=
#### HashMap

`HashMap` es la implementación más usada y rápida.

```java
import java.util.HashMap;
import java.util.Map;

public class EjemploHashMap {
    public static void main(String[] args) {
        Map<String, Integer> edades = new HashMap<>();
        
        // Agregar pares clave-valor
        edades.put("Ana", 25);
        edades.put("Juan", 30);
        edades.put("María", 28);
        
        // Obtener valor por clave
        Integer edadAna = edades.get("Ana");  // 25
        
        // Si la clave no existe, retorna null
        Integer edadPedro = edades.get("Pedro");  // null
        
        // Obtener con valor por defecto
        Integer edadPedro2 = edades.getOrDefault("Pedro", 0);  // 0
        
        // Verificar si existe la clave
        boolean tieneJuan = edades.containsKey("Juan");  // true
        
        // Verificar si existe el valor
        boolean tieneEdad30 = edades.containsValue(30);  // true
        
        // Actualizar valor (misma clave)
        edades.put("Ana", 26);  // Ahora Ana tiene 26
        
        // Eliminar por clave
        edades.remove("Juan");
        
        // Tamaño
        int cantidad = edades.size();  // 2
        
        // Iterar sobre claves
        for (String nombre : edades.keySet()) {
            System.out.println(nombre);
        }
        
        // Iterar sobre valores
        for (Integer edad : edades.values()) {
            System.out.println(edad);
        }
        
        // Iterar sobre pares clave-valor
        for (Map.Entry<String, Integer> entry : edades.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```

**Métodos útiles adicionales:**

```java
// putIfAbsent: solo agrega si la clave no existe
edades.putIfAbsent("Pedro", 22);

// computeIfAbsent: calcula el valor si la clave no existe
Map<String, List<String>> grupos = new HashMap<>();
grupos.computeIfAbsent("A", k -> new ArrayList<>()).add("Estudiante 1");

// merge: combina valores
Map<String, Integer> conteo = new HashMap<>();
conteo.merge("palabra", 1, Integer::sum);  // Si existe, suma; si no, pone 1

// getOrDefault ya visto
int edad = edades.getOrDefault("Inexistente", -1);
```

(treemap-java)=
#### TreeMap

`TreeMap` mantiene las claves **ordenadas**.

```java
import java.util.TreeMap;
import java.util.Map;

public class EjemploTreeMap {
    public static void main(String[] args) {
        Map<String, Double> notas = new TreeMap<>();
        
        notas.put("Matemática", 8.5);
        notas.put("Historia", 7.0);
        notas.put("Física", 9.0);
        notas.put("Arte", 8.0);
        
        // Las claves están ordenadas alfabéticamente
        for (String materia : notas.keySet()) {
            System.out.println(materia + ": " + notas.get(materia));
        }
        // Arte: 8.0
        // Física: 9.0
        // Historia: 7.0
        // Matemática: 8.5
        
        // Operaciones adicionales de TreeMap
        TreeMap<String, Double> tm = new TreeMap<>(notas);
        
        System.out.println(tm.firstKey());  // "Arte"
        System.out.println(tm.lastKey());   // "Matemática"
    }
}
```
4. **No excepciones genéricas:** Una clase que extiende `Exception` o `Throwable` no puede ser genérica.

---

(iteracion-colecciones)=
## Iteración de Colecciones

### For-Each (Recomendado)

```java
List<String> nombres = List.of("Ana", "Juan", "María");

// For-each: limpio y legible
for (String nombre : nombres) {
    System.out.println(nombre);
}
```

### Iterator

Para cuando necesitás eliminar elementos mientras iterás:

```java
List<Integer> numeros = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));

// Eliminar pares usando Iterator
Iterator<Integer> it = numeros.iterator();
while (it.hasNext()) {
    Integer n = it.next();
    if (n % 2 == 0) {
        it.remove();  // ¡Seguro! No lanza ConcurrentModificationException
    }
}

System.out.println(numeros);  // [1, 3, 5]
```

:::{warning}
**No modifiques una colección mientras iterás con for-each:**

```java
// ❌ INCORRECTO: lanza ConcurrentModificationException
for (Integer n : numeros) {
    if (n % 2 == 0) {
        numeros.remove(n);  // ¡Error!
    }
}

// ✓ CORRECTO: usar Iterator o removeIf
numeros.removeIf(n -> n % 2 == 0);
```
:::

### forEach con Lambda (Java 8+)

```java
List<String> nombres = List.of("Ana", "Juan", "María");

// forEach con lambda
nombres.forEach(nombre -> System.out.println(nombre));

// forEach con method reference
nombres.forEach(System.out::println);

// Con Maps
Map<String, Integer> edades = Map.of("Ana", 25, "Juan", 30);
edades.forEach((nombre, edad) -> 
    System.out.println(nombre + " tiene " + edad + " años")
);
```

### Streams (Java 8+)

Los streams permiten operaciones declarativas y funcionales:

```java
List<Integer> numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Filtrar, transformar y recolectar
List<Integer> paresDoblados = numeros.stream()
    .filter(n -> n % 2 == 0)       // Solo pares: 2, 4, 6, 8, 10
    .map(n -> n * 2)               // Doblar: 4, 8, 12, 16, 20
    .collect(Collectors.toList()); // Convertir a lista

// Sumar todos
int suma = numeros.stream()
    .reduce(0, Integer::sum);  // 55

// Encontrar máximo
Optional<Integer> max = numeros.stream()
    .max(Integer::compareTo);  // Optional[10]

// Contar
long cantidadPares = numeros.stream()
    .filter(n -> n % 2 == 0)
    .count();  // 5

// Verificar condiciones
boolean todosMayoresACero = numeros.stream()
    .allMatch(n -> n > 0);  // true

boolean algunoMayorA5 = numeros.stream()
    .anyMatch(n -> n > 5);  // true
```

---

(comparable-comparator)=
## Comparación y Ordenamiento

### La Interface Comparable

`Comparable` define el **orden natural** de una clase:

```java
public class Persona implements Comparable<Persona> {
    private String nombre;
    private int edad;
    
    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }
    
    // Ordenar por edad (orden natural)
    @Override
    public int compareTo(Persona otra) {
        return Integer.compare(this.edad, otra.edad);
        // Alternativa: return this.edad - otra.edad;
    }
    
    // getters...
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
}

// Uso
List<Persona> personas = new ArrayList<>();
personas.add(new Persona("Ana", 25));
personas.add(new Persona("Juan", 20));
personas.add(new Persona("María", 30));

Collections.sort(personas);  // Ordena por edad (orden natural)
// Resultado: Juan(20), Ana(25), María(30)
```

**Reglas de compareTo:**

- Retorna **negativo** si `this < otro`
- Retorna **cero** si `this == otro`
- Retorna **positivo** si `this > otro`

### La Interface Comparator

`Comparator` define **criterios alternativos** de ordenamiento:

```java
import java.util.Comparator;

// Comparador por nombre
Comparator<Persona> porNombre = new Comparator<Persona>() {
    @Override
    public int compare(Persona p1, Persona p2) {
        return p1.getNombre().compareTo(p2.getNombre());
    }
};

// Con lambda (más conciso)
Comparator<Persona> porNombreLambda = 
    (p1, p2) -> p1.getNombre().compareTo(p2.getNombre());

// Con method reference (aún más conciso)
Comparator<Persona> porNombreRef = 
    Comparator.comparing(Persona::getNombre);

// Uso
Collections.sort(personas, porNombre);  // Ordena por nombre
```

### Comparadores Compuestos

```java
// Ordenar por edad, y si empatan, por nombre
Comparator<Persona> porEdadYNombre = 
    Comparator.comparing(Persona::getEdad)
              .thenComparing(Persona::getNombre);

// Orden descendente
Comparator<Persona> porEdadDesc = 
    Comparator.comparing(Persona::getEdad).reversed();

// Manejo de nulls
Comparator<Persona> porNombreNullsFirst = 
    Comparator.comparing(Persona::getNombre, 
                         Comparator.nullsFirst(String::compareTo));

// Uso
personas.sort(porEdadYNombre);
personas.sort(porEdadDesc);
```

### Ordenamiento con TreeSet y TreeMap

```java
// TreeSet con orden natural (requiere Comparable)
Set<Persona> personasOrdenadas = new TreeSet<>();
personasOrdenadas.add(new Persona("Ana", 25));
personasOrdenadas.add(new Persona("Juan", 20));
// Quedan ordenadas por edad

// TreeSet con Comparator personalizado
Set<Persona> personasPorNombre = new TreeSet<>(
    Comparator.comparing(Persona::getNombre)
);

// TreeMap con Comparator
Map<Persona, String> empleados = new TreeMap<>(
    Comparator.comparing(Persona::getEdad).reversed()
);
```

---

(equals-hashcode)=
## equals() y hashCode()

### La Importancia de equals() y hashCode()

Para que los objetos funcionen correctamente en colecciones, debés implementar `equals()` y `hashCode()`.

```java
public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    
    public Producto(String codigo, String nombre, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Producto otro = (Producto) obj;
        return Objects.equals(codigo, otro.codigo);  // Igualdad por código
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(codigo);  // Hash basado en código
    }
    
    // getters...
}
```

:::{important}
**Contrato equals/hashCode:**

1. Si `a.equals(b)` es `true`, entonces `a.hashCode() == b.hashCode()`
2. Si `a.hashCode() != b.hashCode()`, entonces `a.equals(b)` debe ser `false`
3. Si los hashCodes son iguales, los objetos **pueden o no** ser iguales

**Consecuencia**: Si sobreescribís `equals()`, **debés** sobreescribir `hashCode()`.
:::

### Sin equals/hashCode Correctos

```java
public class ProductoMal {
    private String codigo;
    // No implementa equals ni hashCode
}

Set<ProductoMal> productos = new HashSet<>();
productos.add(new ProductoMal("ABC"));
productos.add(new ProductoMal("ABC"));

System.out.println(productos.size());  // ¡2! Debería ser 1
```

### Con equals/hashCode Correctos

```java
Set<Producto> productos = new HashSet<>();
productos.add(new Producto("ABC", "Laptop", 1500));
productos.add(new Producto("ABC", "Laptop diferente", 1600));

System.out.println(productos.size());  // 1 (correcto)
```

---

(ejemplo-completo-colecciones)=
## Ejemplo Completo: Sistema de Inventario

```java
import java.util.*;

public class Producto implements Comparable<Producto> {
    private final String codigo;
    private String nombre;
    private double precio;
    private int stock;
    
    public Producto(String codigo, String nombre, double precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }
    
    // Getters
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    
    // Setters
    public void setPrecio(double precio) { this.precio = precio; }
    public void ajustarStock(int cantidad) { this.stock += cantidad; }
    
    @Override
    public int compareTo(Producto otro) {
        return this.codigo.compareTo(otro.codigo);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Producto)) return false;
        Producto otro = (Producto) obj;
        return Objects.equals(codigo, otro.codigo);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
    
    @Override
    public String toString() {
        return String.format("%s: %s ($%.2f) - Stock: %d", 
                            codigo, nombre, precio, stock);
    }
}

public class Inventario {
    private Map<String, Producto> productos;
    private Set<String> categorias;
    private Map<String, Set<Producto>> productosPorCategoria;
    
    public Inventario() {
        this.productos = new HashMap<>();
        this.categorias = new TreeSet<>();  // Ordenadas alfabéticamente
        this.productosPorCategoria = new HashMap<>();
    }
    
    public void agregarProducto(Producto producto, String categoria) {
        productos.put(producto.getCodigo(), producto);
        categorias.add(categoria);
        
        productosPorCategoria
            .computeIfAbsent(categoria, k -> new HashSet<>())
            .add(producto);
    }
    
    public Optional<Producto> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(productos.get(codigo));
    }
    
    public List<Producto> buscarPorNombre(String texto) {
        return productos.values().stream()
            .filter(p -> p.getNombre().toLowerCase()
                          .contains(texto.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    public List<Producto> obtenerConBajoStock(int minimo) {
        return productos.values().stream()
            .filter(p -> p.getStock() < minimo)
            .sorted(Comparator.comparing(Producto::getStock))
            .collect(Collectors.toList());
    }
    
    public List<Producto> obtenerPorCategoria(String categoria) {
        Set<Producto> prods = productosPorCategoria.get(categoria);
        if (prods == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(prods);
    }
    
    public double calcularValorTotal() {
        return productos.values().stream()
            .mapToDouble(p -> p.getPrecio() * p.getStock())
            .sum();
    }
    
    public Map<String, Long> contarPorCategoria() {
        Map<String, Long> conteo = new HashMap<>();
        for (Map.Entry<String, Set<Producto>> entry : 
             productosPorCategoria.entrySet()) {
            conteo.put(entry.getKey(), (long) entry.getValue().size());
        }
        return conteo;
    }
    
    public void imprimirReporte() {
        System.out.println("=== REPORTE DE INVENTARIO ===\n");
        
        System.out.println("Categorías:");
        for (String cat : categorias) {
            System.out.println("  - " + cat);
        }
        
        System.out.println("\nProductos por categoría:");
        for (String cat : categorias) {
            System.out.println("\n" + cat + ":");
            for (Producto p : obtenerPorCategoria(cat)) {
                System.out.println("  " + p);
            }
        }
        
        System.out.printf("\nValor total del inventario: $%.2f%n", 
                         calcularValorTotal());
        
        List<Producto> bajoStock = obtenerConBajoStock(10);
        if (!bajoStock.isEmpty()) {
            System.out.println("\n⚠️ Productos con bajo stock:");
            bajoStock.forEach(p -> System.out.println("  " + p));
        }
    }
    
    public static void main(String[] args) {
        Inventario inv = new Inventario();
        
        inv.agregarProducto(
            new Producto("LAP001", "Laptop HP", 1500.00, 15),
            "Electrónica"
        );
        inv.agregarProducto(
            new Producto("LAP002", "Laptop Dell", 1800.00, 8),
            "Electrónica"
        );
        inv.agregarProducto(
            new Producto("MOU001", "Mouse Logitech", 45.00, 50),
            "Periféricos"
        );
        inv.agregarProducto(
            new Producto("TEC001", "Teclado Mecánico", 120.00, 5),
            "Periféricos"
        );
        inv.agregarProducto(
            new Producto("CAB001", "Cable HDMI", 15.00, 100),
            "Accesorios"
        );
        
        inv.imprimirReporte();
        
        System.out.println("\n--- Búsqueda ---");
        inv.buscarPorNombre("laptop").forEach(System.out::println);
    }
}
```

---

(resumen-java3)=
## Resumen

### Colecciones

- **List**: Secuencia ordenada con duplicados (`ArrayList`, `LinkedList`)
- **Set**: Sin duplicados (`HashSet`, `TreeSet`, `LinkedHashSet`)
- **Map**: Pares clave-valor (`HashMap`, `TreeMap`)

### Iteración

- **For-each**: Limpio y legible para lectura
- **Iterator**: Cuando necesitás modificar durante iteración
- **Streams**: Operaciones funcionales (Java 8+)

### Comparación

- `Comparable<T>`: Orden natural (implementa la clase)
- `Comparator<T>`: Orden alternativo (clase separada o lambda)

### equals() y hashCode()

- **Siempre** sobreescribir ambos juntos
- Usar `Objects.equals()` y `Objects.hash()` para simplificar

**Nota:** Para un estudio detallado de genéricos (tipado seguro, clases genéricas, bounded types, wildcards), consultá el capítulo dedicado {ref}`java-genericos`.

---

(ejercicios-java3)=
## Ejercicios

```{exercise}
:label: ej-lista-compras
Implementá una clase `ListaCompras` que:
- Almacene productos con cantidad
- No permita productos duplicados (usar Map)
- Permita aumentar/disminuir cantidades
- Calcule el total (suponiendo que cada producto tiene precio)
- Imprima la lista ordenada por nombre de producto
```

```{exercise}
:label: ej-cache-generico
Implementá una clase genérica `Cache<K, V>` que:
- Almacene pares clave-valor
- Tenga un tamaño máximo configurable
- Cuando se llene, elimine la entrada más antigua (FIFO)
- Proporcione métodos `get`, `put` y `contains`
```

```{exercise}
:label: ej-agenda-contactos
Creá un sistema de agenda de contactos:
- Clase `Contacto` con nombre, teléfono, email
- Clase `Agenda` que almacene contactos
- Búsqueda por nombre (parcial)
- Búsqueda por inicial
- Ordenamiento por nombre o por fecha de agregado
- No permitir contactos con el mismo email
```

```{exercise}
:label: ej-frecuencia-palabras
Escribí un programa que:
- Lea un texto
- Cuente la frecuencia de cada palabra
- Muestre las N palabras más frecuentes
- Ignore mayúsculas/minúsculas
- Use las colecciones apropiadas
```

```{exercise}
:label: ej-grafo-generico
Implementá una clase genérica `Grafo<T>` que:
- Represente un grafo dirigido
- Permita agregar nodos y aristas
- Implemente búsqueda en profundidad (DFS)
- Implemente búsqueda en anchura (BFS)
- Determine si existe camino entre dos nodos
```
