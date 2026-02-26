---
title: "Programación Orientada a Objetos"
description: Guía completa sobre POO en Java - clases, objetos, herencia, polimorfismo e interfaces.
---

# Programación Orientada a Objetos

La **Programación Orientada a Objetos** (POO) es un paradigma que organiza el código alrededor de "objetos" que combinan datos (estado) y comportamiento (métodos). Java es un lenguaje fundamentalmente orientado a objetos.

## Conceptos Fundamentales

### Clases y Objetos

- **Clase**: plantilla o "molde" que define la estructura y comportamiento
- **Objeto**: instancia concreta de una clase, con valores específicos

```{code} java
:caption: Clase y objeto

// Clase: la plantilla
public class Persona {
    private String nombre;
    private int edad;
    
    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }
    
    public void saludar() {
        System.out.println("Hola, soy " + nombre);
    }
}

// Objetos: instancias concretas
Persona juan = new Persona("Juan", 25);
Persona maria = new Persona("María", 30);

juan.saludar();  // "Hola, soy Juan"
maria.saludar(); // "Hola, soy María"
```

### Estado y Comportamiento

- **Estado**: los valores de los atributos en un momento dado
- **Comportamiento**: las acciones que el objeto puede realizar (métodos)

```{code} java
:caption: Estado y comportamiento

public class CuentaBancaria {
    // Estado (atributos)
    private String titular;
    private double saldo;
    
    // Comportamiento (métodos)
    public void depositar(double monto) {
        saldo += monto;
    }
    
    public boolean retirar(double monto) {
        if (monto <= saldo) {
            saldo -= monto;
            return true;
        }
        return false;
    }
}
```

## Pilares de la POO

### 1. Encapsulamiento

Ocultar los detalles internos y exponer solo lo necesario. El verdadero encapsulamiento no se trata solo de hacer atributos privados, sino de **ocultar la representación interna** y exponer **comportamiento**.

```{code} java
:caption: Encapsulamiento correcto

public class Temperatura {
    private double celsius;
    
    public Temperatura(double celsius) {
        if (celsius < -273.15) {
            throw new IllegalArgumentException("Temperatura bajo cero absoluto");
        }
        this.celsius = celsius;
    }
    
    // Comportamiento, no acceso a datos
    public boolean esMayorQue(Temperatura otra) {
        return this.celsius > otra.celsius;
    }
    
    public boolean esCongelante() {
        return celsius <= 0;
    }
    
    public Temperatura sumar(double grados) {
        return new Temperatura(celsius + grados);
    }
    
    public String formatearCelsius() {
        return String.format("%.1f°C", celsius);
    }
    
    public String formatearFahrenheit() {
        return String.format("%.1f°F", celsius * 9.0/5.0 + 32);
    }
}
```

:::{important}
**Beneficios del encapsulamiento real:**
- Los objetos **hacen cosas**, no solo almacenan datos
- El código externo no depende de la representación interna
- Libertad total de cambiar la implementación
- Código más expresivo y mantenible
:::

### El Problema de los Getters y Setters

:::{warning}
**Los getters y setters son una mala práctica** que viola el encapsulamiento real. Aunque son muy comunes, representan un anti-patrón conocido como "Anemic Domain Model" (Modelo de Dominio Anémico).
:::

#### ¿Por qué son problemáticos?

```{code} java
:caption: MAL - Getters/Setters exponen la implementación

// EVITAR: Este diseño es problemático
public class CuentaBancaria {
    private double saldo;
    
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
}

// El código cliente manipula directamente el estado
CuentaBancaria cuenta = new CuentaBancaria();
double nuevoSaldo = cuenta.getSaldo() - 100;
if (nuevoSaldo >= 0) {
    cuenta.setSaldo(nuevoSaldo);  // Lógica de negocio AFUERA del objeto
}
```

**Problemas de este enfoque:**
1. **Rompe el encapsulamiento**: el objeto es solo un contenedor de datos
2. **Lógica dispersa**: las reglas de negocio quedan fuera del objeto
3. **Difícil de mantener**: si cambia la representación interna, hay que modificar todo el código cliente
4. **Violación de "Tell, Don't Ask"**: le preguntamos al objeto su estado para tomar decisiones afuera

#### La Alternativa: Diseño Orientado a Comportamiento

```{code} java
:caption: BIEN - Comportamiento en lugar de datos

public class CuentaBancaria {
    private double saldo;
    private String titular;
    
    public CuentaBancaria(String titular, double saldoInicial) {
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("Saldo inicial no puede ser negativo");
        }
        this.titular = titular;
        this.saldo = saldoInicial;
    }
    
    // Comportamiento: el objeto HACE cosas
    public void depositar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("Monto debe ser positivo");
        }
        saldo += monto;
    }
    
    public void retirar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("Monto debe ser positivo");
        }
        if (monto > saldo) {
            throw new IllegalStateException("Fondos insuficientes");
        }
        saldo -= monto;
    }
    
    public void transferirA(CuentaBancaria destino, double monto) {
        this.retirar(monto);
        destino.depositar(monto);
    }
    
    // Consultas que devuelven información formateada o derivada
    public boolean tieneDisponible(double monto) {
        return saldo >= monto;
    }
    
    public String obtenerResumen() {
        return String.format("Cuenta de %s: $%.2f", titular, saldo);
    }
}
```

```{code} java
:caption: Uso del diseño correcto

// El código cliente le DICE al objeto qué hacer
CuentaBancaria cuenta = new CuentaBancaria("Juan", 1000);
cuenta.retirar(100);  // El objeto maneja su propia lógica
cuenta.transferirA(otraCuenta, 200);

// Si necesito mostrar información
System.out.println(cuenta.obtenerResumen());
```

#### Principio "Tell, Don't Ask"

:::{tip} Tell, Don't Ask
En lugar de **preguntar** al objeto su estado para tomar decisiones afuera, **decile** al objeto qué hacer y dejá que él tome las decisiones internamente.

**Preguntando (mal):**
```java
if (cuenta.getSaldo() >= monto) {
    cuenta.setSaldo(cuenta.getSaldo() - monto);
}
```

**Diciendo (bien):**
```java
cuenta.retirar(monto);  // El objeto decide si puede o no
```
:::

#### Cuándo Podría Necesitarse Exponer Datos

Hay casos limitados donde exponer información es aceptable:

1. **Objetos de transferencia de datos (DTOs)**: clases cuyo único propósito es transportar datos entre capas
2. **Representación para la UI**: métodos que devuelven información formateada para mostrar
3. **Inmutabilidad**: objetos inmutables donde exponer valores no permite modificación

```{code} java
:caption: Alternativas aceptables

public class Punto {
    private final int x;  // inmutable
    private final int y;
    
    public Punto(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // Aceptable en objeto inmutable: no hay riesgo de modificación externa
    public int x() { return x; }
    public int y() { return y; }
    
    // Pero preferí métodos de comportamiento
    public double distanciaA(Punto otro) {
        int dx = this.x - otro.x;
        int dy = this.y - otro.y;
        return Math.sqrt(dx*dx + dy*dy);
    }
    
    public Punto mover(int deltaX, int deltaY) {
        return new Punto(x + deltaX, y + deltaY);
    }
}
```

#### Resumen: Diseño Correcto de Clases

:::{note}
**Reglas para un buen diseño orientado a objetos:**

1. **Escondé los datos**: los atributos siempre `private`
2. **Exponé comportamiento**: métodos que realizan acciones
3. **Validá en el constructor**: asegurá que el objeto nazca en estado válido
4. **Usá excepciones**: para indicar operaciones inválidas
5. **Preferí inmutabilidad**: objetos que no cambian son más seguros
6. **Pensá en qué HACE el objeto**, no en qué datos TIENE
:::

:::{table} Getters/Setters vs Comportamiento
:label: tbl-getters-vs-comportamiento

| Aspecto | Getters/Setters | Comportamiento |
| :------ | :-------------- | :------------- |
| Encapsulamiento | Falso (expone estructura) | Real (oculta implementación) |
| Lógica de negocio | Dispersa en el cliente | Centralizada en el objeto |
| Mantenibilidad | Baja | Alta |
| Testeo | Difícil de aislar | Fácil de testear |
| Cambios internos | Rompen código cliente | No afectan al cliente |

:::

### 2. Herencia

Permite que una clase "herede" atributos y métodos de otra:

```{code} java
:caption: Herencia

// Clase padre (superclase)
public class Animal {
    protected String nombre;
    
    public Animal(String nombre) {
        this.nombre = nombre;
    }
    
    public void dormir() {
        System.out.println(nombre + " está durmiendo");
    }
    
    public void hacerSonido() {
        System.out.println("Algún sonido");
    }
}

// Clase hija (subclase)
public class Perro extends Animal {
    private String raza;
    
    public Perro(String nombre, String raza) {
        super(nombre);  // Llama al constructor del padre
        this.raza = raza;
    }
    
    @Override
    public void hacerSonido() {
        System.out.println(nombre + " dice: ¡Guau!");
    }
    
    // Método propio de Perro
    public void buscarPelota() {
        System.out.println(nombre + " busca la pelota");
    }
}

// Uso
Perro fido = new Perro("Fido", "Labrador");
fido.dormir();       // Heredado de Animal
fido.hacerSonido();  // Sobreescrito en Perro
fido.buscarPelota(); // Propio de Perro
```

### 3. Polimorfismo

Permite tratar objetos de diferentes clases de manera uniforme:

```{code} java
:caption: Polimorfismo

public class Granja {
    
    public static void main(String[] args) {
        // Todos son "Animal", pero cada uno tiene su comportamiento
        Animal[] animales = {
            new Perro("Fido", "Labrador"),
            new Gato("Michi"),
            new Vaca("Lola")
        };
        
        // Polimorfismo: cada animal hace su propio sonido
        for (Animal animal : animales) {
            animal.hacerSonido();
        }
    }
}
// Salida:
// Fido dice: ¡Guau!
// Michi dice: ¡Miau!
// Lola dice: ¡Muuu!
```

### 4. Abstracción

Enfocarse en **qué** hace un objeto, no en **cómo** lo hace:

```{code} java
:caption: Abstracción

// La abstracción define QUÉ debe hacer una figura
public abstract class Figura {
    protected String color;
    
    public abstract double calcularArea();
    public abstract double calcularPerimetro();
    
    // Método concreto compartido
    public void mostrarInfo() {
        System.out.printf("Figura %s: área=%.2f%n", color, calcularArea());
    }
}

// Las implementaciones definen CÓMO hacerlo
public class Circulo extends Figura {
    private double radio;
    
    public Circulo(String color, double radio) {
        this.color = color;
        this.radio = radio;
    }
    
    @Override
    public double calcularArea() {
        return Math.PI * radio * radio;
    }
    
    @Override
    public double calcularPerimetro() {
        return 2 * Math.PI * radio;
    }
}

public class Rectangulo extends Figura {
    private double ancho, alto;
    
    @Override
    public double calcularArea() {
        return ancho * alto;
    }
    
    @Override
    public double calcularPerimetro() {
        return 2 * (ancho + alto);
    }
}
```

## Clases Abstractas

Una clase `abstract` no puede instanciarse directamente:

```{code} java
:caption: Clase abstracta

public abstract class Empleado {
    protected String nombre;
    protected double salarioBase;
    
    public Empleado(String nombre, double salarioBase) {
        this.nombre = nombre;
        this.salarioBase = salarioBase;
    }
    
    // Método abstracto: las subclases DEBEN implementarlo
    public abstract double calcularSalario();
    
    // Método concreto: las subclases lo heredan
    public void mostrarInfo() {
        System.out.printf("%s: $%.2f%n", nombre, calcularSalario());
    }
}

public class EmpleadoFijo extends Empleado {
    
    public EmpleadoFijo(String nombre, double salario) {
        super(nombre, salario);
    }
    
    @Override
    public double calcularSalario() {
        return salarioBase;
    }
}

public class EmpleadoPorHora extends Empleado {
    private int horasTrabajadas;
    
    public EmpleadoPorHora(String nombre, double tarifaHora, int horas) {
        super(nombre, tarifaHora);
        this.horasTrabajadas = horas;
    }
    
    @Override
    public double calcularSalario() {
        return salarioBase * horasTrabajadas;
    }
}
```

## Interfaces

Una **interfaz** define un **contrato** de comportamiento:

```{code} java
:caption: Definición e implementación de interfaces

// Interfaz: define QUÉ métodos debe tener
public interface Volador {
    void despegar();
    void volar();
    void aterrizar();
}

public interface Nadador {
    void nadar();
    void sumergirse();
}

// Una clase puede implementar múltiples interfaces
public class Pato extends Animal implements Volador, Nadador {
    
    @Override
    public void despegar() {
        System.out.println("El pato despega del agua");
    }
    
    @Override
    public void volar() {
        System.out.println("El pato vuela");
    }
    
    @Override
    public void aterrizar() {
        System.out.println("El pato aterriza");
    }
    
    @Override
    public void nadar() {
        System.out.println("El pato nada");
    }
    
    @Override
    public void sumergirse() {
        System.out.println("El pato se sumerge");
    }
}
```

### Interfaces como Tipos

```{code} java
:caption: Uso de interfaces como tipos

public class Aeropuerto {
    
    // Acepta cualquier cosa que pueda volar
    public void recibirVuelo(Volador volador) {
        volador.aterrizar();
    }
}

// Se puede pasar cualquier implementación de Volador
Aeropuerto aeropuerto = new Aeropuerto();
aeropuerto.recibirVuelo(new Avion());
aeropuerto.recibirVuelo(new Helicoptero());
aeropuerto.recibirVuelo(new Pato());  // ¡También funciona!
```

### Diferencias: Clase Abstracta vs Interfaz

:::{table} Clase abstracta vs Interfaz
:label: tbl-abstracta-interfaz

| Característica | Clase Abstracta | Interfaz |
| :------------- | :-------------- | :------- |
| Herencia | `extends` (una sola) | `implements` (múltiples) |
| Constructores | Sí | No |
| Atributos de instancia | Sí | Solo constantes (`static final`) |
| Métodos concretos | Sí | Sí (desde Java 8: `default`) |
| Modificadores de acceso | Cualquiera | `public` implícito |

:::

## La Clase Object

Todas las clases heredan de `Object`. Métodos importantes a sobrescribir:

### toString()

```{code} java
:caption: Sobrescribir toString

public class Producto {
    private String nombre;
    private double precio;
    
    @Override
    public String toString() {
        return String.format("Producto[nombre=%s, precio=%.2f]", 
                            nombre, precio);
    }
}

// Uso
Producto p = new Producto("Laptop", 999.99);
System.out.println(p);  // Llama a toString() automáticamente
// Salida: Producto[nombre=Laptop, precio=999.99]
```

### equals() y hashCode()

```{code} java
:caption: Sobrescribir equals y hashCode

public class Persona {
    private String dni;
    private String nombre;
    
    @Override
    public boolean equals(Object o) {
        // Mismo objeto
        if (this == o) return true;
        
        // Null o clase diferente
        if (o == null || getClass() != o.getClass()) return false;
        
        // Comparar atributos relevantes
        Persona persona = (Persona) o;
        return Objects.equals(dni, persona.dni);
    }
    
    @Override
    public int hashCode() {
        // DEBE usar los mismos atributos que equals()
        return Objects.hash(dni);
    }
}
```

:::{warning}
**Reglas de equals:**
1. **Reflexivo**: `x.equals(x)` siempre `true`
2. **Simétrico**: si `x.equals(y)` entonces `y.equals(x)`
3. **Transitivo**: si `x.equals(y)` y `y.equals(z)` entonces `x.equals(z)`

**Siempre sobrescribí `hashCode()` cuando sobrescribas `equals()`**.
:::

## Composición y Agregación

### Composición

Relación **fuerte**: el objeto contenido no existe sin el contenedor:

```{code} java
:caption: Composición

public class Auto {
    private Motor motor;  // El motor "pertenece" al auto
    
    public Auto(String modelo) {
        // El auto CREA su motor
        this.motor = new Motor("V8");
    }
    // Si el auto se destruye, el motor también
}
```

### Agregación

Relación **débil**: los objetos existen independientemente:

```{code} java
:caption: Agregación

public class Equipo {
    private List<Jugador> jugadores;  // Los jugadores existen independientemente
    
    public void agregarJugador(Jugador jugador) {
        // El jugador YA EXISTE, el equipo solo lo referencia
        jugadores.add(jugador);
    }
    // Si el equipo se disuelve, los jugadores siguen existiendo
}
```

## Genéricos

Permiten crear clases y métodos que operan con diferentes tipos:

```{code} java
:caption: Clase genérica

public class Caja<T> {
    private T contenido;
    
    public void guardar(T item) {
        this.contenido = item;
    }
    
    public T obtener() {
        return contenido;
    }
}

// Uso con diferentes tipos
Caja<String> cajaTexto = new Caja<>();
cajaTexto.guardar("Hola");
String texto = cajaTexto.obtener();

Caja<Integer> cajaNumero = new Caja<>();
cajaNumero.guardar(42);
Integer numero = cajaNumero.obtener();
```

### Genéricos Acotados

```{code} java
:caption: Genéricos con límites

// T debe ser Number o subclase de Number
public class Calculadora<T extends Number> {
    
    public double sumar(T a, T b) {
        return a.doubleValue() + b.doubleValue();
    }
}

Calculadora<Integer> calcInt = new Calculadora<>();
Calculadora<Double> calcDouble = new Calculadora<>();
// Calculadora<String> calcString;  // ERROR: String no es Number
```

## Interfaces Útiles

### Comparable

Para comparar objetos del mismo tipo:

```{code} java
:caption: Implementación de Comparable

public class Estudiante implements Comparable<Estudiante> {
    private String nombre;
    private double promedio;
    
    @Override
    public int compareTo(Estudiante otro) {
        // Ordenar por promedio descendente
        return Double.compare(otro.promedio, this.promedio);
    }
}

// Uso
List<Estudiante> estudiantes = new ArrayList<>();
// ... agregar estudiantes
Collections.sort(estudiantes);  // Ordena por promedio
```

### Comparator

Para definir múltiples criterios de ordenamiento:

```{code} java
:caption: Uso de Comparator

public class Estudiante {
    private String nombre;
    private double promedio;
    private int edad;
    
    // Comparadores como constantes estáticas usando clases anónimas
    public static final Comparator<Estudiante> POR_NOMBRE = new Comparator<Estudiante>() {
        @Override
        public int compare(Estudiante e1, Estudiante e2) {
            return e1.nombre.compareTo(e2.nombre);
        }
    };
    
    public static final Comparator<Estudiante> POR_EDAD = new Comparator<Estudiante>() {
        @Override
        public int compare(Estudiante e1, Estudiante e2) {
            return Integer.compare(e1.edad, e2.edad);
        }
    };
    
    public static final Comparator<Estudiante> POR_PROMEDIO_DESC = new Comparator<Estudiante>() {
        @Override
        public int compare(Estudiante e1, Estudiante e2) {
            return Double.compare(e2.promedio, e1.promedio); // Descendente
        }
    };
}

// Uso
Collections.sort(estudiantes, Estudiante.POR_NOMBRE);
Collections.sort(estudiantes, Estudiante.POR_PROMEDIO_DESC);
```

:::{note}
Los comparadores definidos como constantes estáticas dentro de la clase pueden acceder directamente a los atributos privados sin necesidad de getters, ya que están en el mismo ámbito de la clase.
:::

### Iterable e Iterator

Para hacer una clase recorrible con for-each:

```{code} java
:caption: Implementación de Iterable

public class ListaSimple<T> implements Iterable<T> {
    private Object[] elementos;
    private int cantidad;
    
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int indice = 0;
            
            @Override
            public boolean hasNext() {
                return indice < cantidad;
            }
            
            @Override
            @SuppressWarnings("unchecked")
            public T next() {
                return (T) elementos[indice++];
            }
        };
    }
}

// Uso con for-each
ListaSimple<String> lista = new ListaSimple<>();
for (String item : lista) {
    System.out.println(item);
}
```

## Clases Internas

### Clase Interna Regular

```{code} java
:caption: Clase interna

public class Externa {
    private int valor = 10;
    
    public class Interna {
        public void mostrar() {
            // Puede acceder a miembros privados de Externa
            System.out.println("Valor: " + valor);
        }
    }
}

// Uso
Externa externa = new Externa();
Externa.Interna interna = externa.new Interna();
interna.mostrar();
```

### Clase Interna Estática

```{code} java
:caption: Clase interna estática

public class Externa {
    private static int valorEstatico = 5;
    
    public static class InternaEstatica {
        public void mostrar() {
            // Solo puede acceder a miembros estáticos
            System.out.println("Valor: " + valorEstatico);
        }
    }
}

// Uso: no necesita instancia de Externa
Externa.InternaEstatica interna = new Externa.InternaEstatica();
```

### Clase Anónima

```{code} java
:caption: Clase anónima

Comparator<String> porLongitud = new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
        return Integer.compare(s1.length(), s2.length());
    }
};

// Uso
List<String> palabras = new ArrayList<>();
palabras.add("gato");
palabras.add("elefante");
palabras.add("oso");
Collections.sort(palabras, porLongitud);
```

:::{warning}
**Programación Funcional No Permitida**

En este curso **no se permite** el uso de programación funcional de Java 8+:
- Expresiones lambda (`->`)
- Streams (`stream()`, `filter()`, `map()`, `reduce()`)
- Referencias a métodos (`::`)
- Interfaces funcionales como `Predicate`, `Function`, `Consumer`

Se debe usar programación imperativa tradicional con lazos `for`, `while` y clases anónimas.
:::

## Ejercicios

```{exercise}
:label: ej-poo-1

Diseñá una jerarquía de clases para un sistema de figuras geométricas. Debe incluir una clase abstracta `Figura` con métodos para calcular área y perímetro, y al menos tres subclases concretas.
```

```{solution} ej-poo-1
```java
public abstract class Figura {
    protected String nombre;
    
    public Figura(String nombre) {
        this.nombre = nombre;
    }
    
    public abstract double calcularArea();
    public abstract double calcularPerimetro();
    
    @Override
    public String toString() {
        return String.format("%s: área=%.2f, perímetro=%.2f",
            nombre, calcularArea(), calcularPerimetro());
    }
}

public class Circulo extends Figura {
    private double radio;
    
    public Circulo(double radio) {
        super("Círculo");
        this.radio = radio;
    }
    
    @Override
    public double calcularArea() {
        return Math.PI * radio * radio;
    }
    
    @Override
    public double calcularPerimetro() {
        return 2 * Math.PI * radio;
    }
}

public class Rectangulo extends Figura {
    private double ancho, alto;
    
    public Rectangulo(double ancho, double alto) {
        super("Rectángulo");
        this.ancho = ancho;
        this.alto = alto;
    }
    
    @Override
    public double calcularArea() {
        return ancho * alto;
    }
    
    @Override
    public double calcularPerimetro() {
        return 2 * (ancho + alto);
    }
}

public class Triangulo extends Figura {
    private double lado1, lado2, lado3;
    
    public Triangulo(double lado1, double lado2, double lado3) {
        super("Triángulo");
        this.lado1 = lado1;
        this.lado2 = lado2;
        this.lado3 = lado3;
    }
    
    @Override
    public double calcularArea() {
        // Fórmula de Herón
        double s = calcularPerimetro() / 2;
        return Math.sqrt(s * (s-lado1) * (s-lado2) * (s-lado3));
    }
    
    @Override
    public double calcularPerimetro() {
        return lado1 + lado2 + lado3;
    }
}
```
```

```{exercise}
:label: ej-poo-2

Implementá una clase `Pila<T>` genérica con métodos `push`, `pop`, `peek`, `isEmpty` y `size`. Debe lanzar excepciones apropiadas cuando se intente hacer `pop` o `peek` en una pila vacía.
```

```{solution} ej-poo-2
```java
import java.util.EmptyStackException;

public class Pila<T> {
    private Object[] elementos;
    private int tope;
    private static final int CAPACIDAD_INICIAL = 10;
    
    public Pila() {
        elementos = new Object[CAPACIDAD_INICIAL];
        tope = -1;
    }
    
    public void push(T elemento) {
        if (tope == elementos.length - 1) {
            expandir();
        }
        elementos[++tope] = elemento;
    }
    
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T elemento = (T) elementos[tope];
        elementos[tope--] = null;  // Ayuda al GC
        return elemento;
    }
    
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return (T) elementos[tope];
    }
    
    public boolean isEmpty() {
        return tope == -1;
    }
    
    public int size() {
        return tope + 1;
    }
    
    private void expandir() {
        Object[] nuevo = new Object[elementos.length * 2];
        System.arraycopy(elementos, 0, nuevo, 0, elementos.length);
        elementos = nuevo;
    }
}
```
```

```{exercise}
:label: ej-poo-3

Refactorizá la siguiente clase que usa getters/setters para que siga el principio "Tell, Don't Ask" y exponga comportamiento en lugar de datos:

```java
// Clase a refactorizar
public class Rectangulo {
    private double ancho;
    private double alto;
    
    public double getAncho() { return ancho; }
    public void setAncho(double ancho) { this.ancho = ancho; }
    public double getAlto() { return alto; }
    public void setAlto(double alto) { this.alto = alto; }
}

// Código cliente actual
Rectangulo r = new Rectangulo();
r.setAncho(10);
r.setAlto(5);
double area = r.getAncho() * r.getAlto();
if (r.getAncho() == r.getAlto()) {
    System.out.println("Es un cuadrado");
}
```
```

```{solution} ej-poo-3
```java
public class Rectangulo {
    private final double ancho;
    private final double alto;
    
    public Rectangulo(double ancho, double alto) {
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("Dimensiones deben ser positivas");
        }
        this.ancho = ancho;
        this.alto = alto;
    }
    
    // Comportamiento en lugar de datos
    public double calcularArea() {
        return ancho * alto;
    }
    
    public double calcularPerimetro() {
        return 2 * (ancho + alto);
    }
    
    public boolean esCuadrado() {
        return ancho == alto;
    }
    
    public Rectangulo escalar(double factor) {
        return new Rectangulo(ancho * factor, alto * factor);
    }
    
    public boolean cabeEn(Rectangulo otro) {
        return this.ancho <= otro.ancho && this.alto <= otro.alto;
    }
    
    @Override
    public String toString() {
        return String.format("Rectángulo[%.2f x %.2f]", ancho, alto);
    }
}

// Código cliente mejorado
Rectangulo r = new Rectangulo(10, 5);
double area = r.calcularArea();
if (r.esCuadrado()) {
    System.out.println("Es un cuadrado");
}
```
```

:::{seealso}
- [Tutorial de POO de Oracle](https://docs.oracle.com/javase/tutorial/java/concepts/)
- [Documentación de Object](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html)
- [Tutorial de genéricos](https://docs.oracle.com/javase/tutorial/java/generics/)
- [Tell, Don't Ask - Martin Fowler](https://martinfowler.com/bliki/TellDontAsk.html)
:::
