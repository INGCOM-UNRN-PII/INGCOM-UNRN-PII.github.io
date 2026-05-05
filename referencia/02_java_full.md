(referencia-java-full)=
# Sintaxis Completa de Java (POO)

Esta referencia cubre los conceptos avanzados de Java centrados en la **Programación Orientada a Objetos (POO)**. Estos temas se exploran en profundidad durante la segunda parte de la cátedra.

---

## 1. Clases y Objetos

A diferencia de la programación imperativa pura, en la POO el código se organiza alrededor de los objetos y sus interacciones.

### Definición de una Clase
Una clase es la "plantilla" o plano para crear objetos.

```java
public class CuentaBancaria {
    // Atributos (Estado)
    private String numero;
    private double saldo;

    // Constructor (Inicialización)
    public CuentaBancaria(String numero, double saldoInicial) {
        this.numero = numero;
        this.saldo = saldoInicial;
    }

    // Métodos (Comportamiento)
    public void depositar(double monto) {
        if (monto > 0) {
            this.saldo += monto;
        }
    }
}
```

Para profundizar en la estructura de clases, consultá [../parte_2/03_sintaxis_clases](.\/../parte_2/03_sintaxis_clases.md).

---

## 2. Los Pilares de la POO

### Encapsulamiento
Protección del estado interno mediante modificadores de acceso.
- `private`: Solo accesible dentro de la clase.
- `public`: Accesible desde cualquier lugar.
- `protected`: Accesible por subclases y dentro del mismo paquete.

:::{important} Encapsulamiento Estricto
Esta cátedra promueve un **encapsulamiento estricto**. Esto implica la **prohibición del uso generalizado de getters y setters**. En su lugar, se deben usar **métodos de dominio** que reflejen comportamiento semántico.
- No "pidas" datos para decidir afuera (**Don't Ask**).
- "Decile" al objeto qué hacer (**Tell**).
- Consultá las reglas {ref}`regla-0x200C` y {ref}`regla-0x2011`.
:::

### Herencia
Permite crear nuevas clases basadas en clases existentes. Se usa la palabra clave `extends`.

```java
public class CuentaAhorro extends CuentaBancaria {
    private double tasaInteres;

    public CuentaAhorro(String numero, double saldo, double tasa) {
        super(numero, saldo); // Llama al constructor de la superclase
        this.tasaInteres = tasa;
    }
}
```

### Polimorfismo
Capacidad de un objeto de tomar múltiples formas. Una variable de tipo superclase puede referenciar un objeto de cualquier subclase.

```java
CuentaBancaria cuenta = new CuentaAhorro("123", 1000, 0.05);
cuenta.depositar(500); // Se ejecuta el comportamiento apropiado
```

Consultá [../parte_2/04_oop_herencia_polimorfismo](.\/../parte_2/04_oop_herencia_polimorfismo.md) y [../parte_2/05_herencia_polimorfismo](.\/../parte_2/05_herencia_polimorfismo.md) para más detalles.

---

## 3. Interfaces y Clases Abstractas

Las **interfaces** definen contratos (qué debe hacer un objeto), mientras que las **clases abstractas** sirven como bases parciales que no pueden instanciarse directamente.

```java
public interface Validador {
    boolean esValido();
}

public abstract class Vehiculo {
    public abstract void mover(); // Método sin implementación
}
```

---

## 4. Genéricos y Colecciones

Java proporciona un potente framework para manejar conjuntos de datos de forma dinámica y segura.

### El Java Collections Framework (JCF)
- `List<T>`: Listas ordenadas (ej: `ArrayList`, `LinkedList`).
- `Set<T>`: Conjuntos sin duplicados (ej: `HashSet`, `TreeSet`).
- `Map<K, V>`: Diccionarios clave-valor (ej: `HashMap`, `TreeMap`).

```java
List<String> nombres = new ArrayList<>();
nombres.add("Java");
nombres.add("POO");

for (String n : nombres) {
    System.out.println(n);
}
```

Podés encontrar la guía completa de colecciones en [../parte_2/07_colecciones_genericos](.\/../parte_2/07_colecciones_genericos.md).

---

## 5. Excepciones Avanzadas

El manejo de errores en Java es robusto y obliga al programador a considerar situaciones de fallo.

```java
try (BufferedReader br = new BufferedReader(new FileReader("archivo.txt"))) {
    // Código que puede fallar
} catch (FileNotFoundException e) {
    System.err.println("Archivo no encontrado");
} catch (IOException e) {
    System.err.println("Error de lectura");
} finally {
    // Se ejecuta siempre (opcional si se usa try-with-resources)
}
```

---

## 6. Programación Funcional y Streams (Temas Excluidos)

:::{warning} Temas fuera de programa
Los siguientes temas pertenecen a la sintaxis de Java moderno (8+), pero **no se dictan ni se permiten** en esta cátedra para priorizar el aprendizaje de los fundamentos de la POO clásica.
:::

### Expresiones Lambda
Son funciones anónimas que permiten escribir código más conciso.
- **Restricción:** No se permiten. Consultá la {ref}`regla-0x6000`.

```java
// PROHIBIDO en el curso
lista.forEach(n -> System.out.println(n));
```

### API de Streams
Permite procesamiento de datos en estilo declarativo/funcional.
- **Restricción:** No se permite. Consultá la {ref}`regla-0x6002`.

```java
// PROHIBIDO en el curso
int suma = numeros.stream()
    .filter(n -> n > 0)
    .mapToInt(Integer::intValue)
    .sum();
```

### Referencias a Métodos
Sintaxis compacta para llamar a métodos existentes.
- **Restricción:** No se permiten. Consultá la {ref}`regla-0x6001`.

```java
// PROHIBIDO en el curso
lista.forEach(System.out::println);
```

---

## 7. Temas Complementarios

Durante el curso también exploraremos:
- **SOLID:** Principios de diseño para código mantenible ([../parte_2/09_oop_solid](.\/../parte_2/09_oop_solid.md)).
- **Patrones de Diseño:** Soluciones probadas a problemas recurrentes ([../parte_2/10_oop_patrones](.\/../parte_2/10_oop_patrones.md)).
- **Testing de Objetos:** Uso de JUnit en contextos de POO ([../parte_2/11_oop_testing](.\/../parte_2/11_oop_testing.md)).

---

## 8. Clases Internas Anónimas

Las clases internas anónimas permiten declarar e instanciar una clase al mismo tiempo, sin darle un nombre. Son útiles para implementaciones de un solo uso de interfaces o clases abstractas.

```java
// Ejemplo usando un Comparador externo
Collections.sort(lista, new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
        return s1.length() - s2.length();
    }
});
```

:::{note} Contexto
Dado que las expresiones lambda están prohibidas ({ref}`regla-0x6000`), las **clases anónimas** son la herramienta principal para pasar comportamiento como parámetro en este curso.
:::
