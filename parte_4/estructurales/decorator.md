---
title: "Patrón Decorator"
subtitle: "Agregar responsabilidades a objetos dinámicamente"
subject: Patrones de Diseño Estructurales
---

(patron-decorator)=
# Decorator: Extensión Dinámmica

El patrón **Decorator** permite agregar responsabilidades a un objeto dinámicamente, proporcionando una alternativa flexible a la herencia para extender funcionalidad.

:::{admonition} Propósito
:class: note

Agregar responsabilidades a objetos de forma dinámica, en lugar de crear subclases.
:::

---

## Concepto

Decorator resuelve el problema de:
- Herencia explosiva (múltiples combinaciones de características)
- Extensión en tiempo de compilación vs. runtime
- Añadir funcionalidad sin modificar la clase original

```
Comparación:
Herencia:     Café → CaféConLeche, CaféConLeche → CaféConLeche+Azúcar (combinatorial)
Decorator:    Café → Decorator → Decorator → Decorator (lineal, componible)
```

---

## Problema

```java
// ❌ Explosión de subclases
abstract class Bebida {
    abstract double costo();
}

class Café extends Bebida {
    public double costo() { return 3.00; }
}

class CaféConLeche extends Café {
    public double costo() { return 3.00 + 0.50; }
}

class CaféConLecheYAzúcar extends CaféConLeche {
    public double costo() { return 3.00 + 0.50 + 0.10; }
}

// Cada combinación es una clase nueva!
```

---

## Solución: Decorator

```java
/**
 * Componente base.
 */
public abstract class Bebida {
    protected String descripcion = "Bebida desconocida";
    
    public String getDescripcion() {
        return descripcion;
    }
    
    abstract double costo();
}

/**
 * Componente concreto: implementación simple.
 */
public class Café extends Bebida {
    public Café() {
        descripcion = "Café";
    }
    
    @Override
    public double costo() {
        return 3.00;
    }
}

/**
 * Clase Decorator base: también es Bebida.
 */
public abstract class AditamentoBebida extends Bebida {
    protected Bebida bebidaDecorada;
    
    public AditamentoBebida(Bebida bebida) {
        this.bebidaDecorada = bebida;
    }
    
    @Override
    public String getDescripcion() {
        return bebidaDecorada.getDescripcion();
    }
}

/**
 * Decorador concreto: Leche.
 */
public class Leche extends AditamentoBebida {
    public Leche(Bebida bebida) {
        super(bebida);
    }
    
    @Override
    public String getDescripcion() {
        return bebidaDecorada.getDescripcion() + ", leche";
    }
    
    @Override
    public double costo() {
        return bebidaDecorada.costo() + 0.50;
    }
}

/**
 * Decorador concreto: Azúcar.
 */
public class Azúcar extends AditamentoBebida {
    public Azúcar(Bebida bebida) {
        super(bebida);
    }
    
    @Override
    public String getDescripcion() {
        return bebidaDecorada.getDescripcion() + ", azúcar";
    }
    
    @Override
    public double costo() {
        return bebidaDecorada.costo() + 0.10;
    }
}

/**
 * Decorador concreto: Crema.
 */
public class Crema extends AditamentoBebida {
    public Crema(Bebida bebida) {
        super(bebida);
    }
    
    @Override
    public String getDescripcion() {
        return bebidaDecorada.getDescripcion() + ", crema";
    }
    
    @Override
    public double costo() {
        return bebidaDecorada.costo() + 0.75;
    }
}

// ✅ Composición flexible
Bebida café = new Café();                              // 3.00
Bebida caféConLeche = new Leche(café);                 // 3.50
Bebida caféConLecheYAzúcar = new Azúcar(caféConLeche); // 3.60
Bebida caféDeluxe = new Crema(caféConLecheYAzúcar);    // 4.35

System.out.println(caféDeluxe.getDescripcion()); // Café, leche, azúcar, crema
System.out.println("Costo: $" + caféDeluxe.costo()); // 4.35
```

---

## Diagrama UML

```
         ┌──────────────┐
         │   Bebida     │
         │  <<abstract>>│
         ├──────────────┤
         │+ costo()     │
         │+ descripción │
         └──────┬───────┘
                │
      ┌─────────┴─────────┐
      │                   │
 ┌────▼────────┐  ┌──────▼─────────────┐
 │   Café      │  │ AditamentoBebida  │
 ├─────────────┤  │   <<abstract>>     │
 │+ costo()    │  ├──────────────────┤
 │ return 3.00 │  │ - bebidaDecorada  │
 └─────────────┘  │+ costo()          │
                  │+ getDescripción() │
                  └────────┬──────────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
         ┌────▼──┐     ┌───▼───┐   ┌──▼────┐
         │ Leche │     │Azúcar │   │Crema  │
         ├───────┤     ├───────┤   ├───────┤
         │costo()│     │costo()│   │costo()│
         └───────┘     └───────┘   └───────┘
```

---

## Variantes

**1. Stack de Decoradores:**
```java
// Apilar múltiples decoradores
new Crema(new Azúcar(new Leche(new Café())));
```

**2. Decoradores con estado:**
```java
public class LecheDescremada extends AditamentoBebida {
    private boolean esOrgánica;
    
    public LecheDescremada(Bebida bebida, boolean esOrgánica) {
        super(bebida);
        this.esOrgánica = esOrgánica;
    }
    
    @Override
    public double costo() {
        return bebidaDecorada.costo() + (esOrgánica ? 1.00 : 0.50);
    }
}
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Flexibilidad**: Combinar comportamientos en runtime
- **Principio Open/Closed**: Abierto a extensión, cerrado a modificación
- **Alternativa a herencia**: Evita explosión de subclases
- **Responsabilidad única**: Cada decorador hace una cosa

### ❌ Desventajas

- **Complejidad**: Stack de decoradores es difícil de entender
- **Debugging**: Difícil rastrear en debugger
- **Orden importa**: El orden de decoradores puede afectar resultado
- **Overhead**: Múltiples capas de indirección

---

## Comparación con otros patrones

| Aspecto | Decorator | Proxy | Strategy |
|--------|-----------|-------|----------|
| **Intención** | Agregar responsabilidades | Controlar acceso | Encapsular algoritmo |
| **Composición** | Múltiple | Uno-a-uno | Intercambiable |
| **Timing** | Tiempo de objeto | Tiempo de objeto | Tiempo de uso |
| **Interfaz** | Igual que componente | Igual que sujeto | Diferente |

---

## Cuándo Usarlo

✅ **Usa Decorator cuando:**
- Necesitas agregar funcionalidad dinámicamente
- Herencia sería explosiva
- Beneficio de responsabilidad única
- Ejemplos: I/O streams (BufferedInputStream), UI widgets

❌ **Evita cuando:**
- Solo una responsabilidad adicional (herencia es más simple)
- El orden no importa y es uno-a-uno (Proxy es mejor)

---

## Ejercicio

```{exercise}
:label: ej-decorator-logs

Crea un sistema de logging con decoradores:
1. `Componente`: `OperaciónMatematica` con `ejecutar(int a, int b): int`
2. Decoradores: `LoggingOperacion`, `CacheOperacion`, `MedicióCompensaciónidorTiempo`
3. Combina decoradores en diferentes órdenes
```

```{solution} ej-decorator-logs
:class: dropdown

```java
public abstract class OperaciónMatematica {
    abstract int ejecutar(int a, int b);
}

public class Suma extends OperaciónMatematica {
    @Override
    int ejecutar(int a, int b) {
        return a + b;
    }
}

public abstract class OperaciónDecorada extends OperaciónMatematica {
    protected OperaciónMatematica operación;
    
    public OperaciónDecorada(OperaciónMatematica op) {
        this.operación = op;
    }
}

public class LoggingOperacion extends OperaciónDecorada {
    public LoggingOperacion(OperaciónMatematica op) {
        super(op);
    }
    
    @Override
    int ejecutar(int a, int b) {
        System.out.println("[LOG] Ejecutando operación con a=" + a + ", b=" + b);
        int resultado = operación.ejecutar(a, b);
        System.out.println("[LOG] Resultado: " + resultado);
        return resultado;
    }
}

public class MedidorTiempo extends OperaciónDecorada {
    public MedidorTiempo(OperaciónMatematica op) {
        super(op);
    }
    
    @Override
    int ejecutar(int a, int b) {
        long inicio = System.nanoTime();
        int resultado = operación.ejecutar(a, b);
        long fin = System.nanoTime();
        System.out.println("[TIEMPO] " + (fin - inicio) + " ns");
        return resultado;
    }
}

public class CacheOperacion extends OperaciónDecorada {
    private Map<String, Integer> cache = new HashMap<>();
    
    public CacheOperacion(OperaciónMatematica op) {
        super(op);
    }
    
    @Override
    int ejecutar(int a, int b) {
        String clave = a + "," + b;
        if (cache.containsKey(clave)) {
            System.out.println("[CACHE] Usando valor cacheado");
            return cache.get(clave);
        }
        int resultado = operación.ejecutar(a, b);
        cache.put(clave, resultado);
        return resultado;
    }
}

// Uso
OperaciónMatematica suma = new Suma();
suma = new LoggingOperacion(suma);
suma = new MedidorTiempo(suma);
suma = new CacheOperacion(suma);

suma.ejecutar(5, 3);
suma.ejecutar(5, 3); // Desde cache
```
```
