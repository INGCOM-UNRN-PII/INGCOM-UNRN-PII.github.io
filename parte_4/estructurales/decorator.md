---
title: "Decorator"
subtitle: "Agregar responsabilidades a objetos dinámicamente"
subject: Patrones de Diseño Estructurales
---

(patron-decorator)=
# Decorator: Extensión Dinámmica

El patrón **Decorator** permite agregar responsabilidades a un objeto dinámicamente, proporcionando una alternativa flexible a la herencia para extender funcionalidad.

:::{note} Propósito

Agregar responsabilidades a objetos de forma dinámica, en lugar de crear subclases.
:::

---

## Origen e Historia

Gang of Four 1994. Proviene de la necesidad de evitar "explosión de subclases" cuando se combinan múltiples características. Popularizado en frameworks de I/O (Java streams: BufferedInputStream, DataInputStream, etc).

## Motivación

Necesario cuando:
- Necesitas agregar responsabilidades dinámicamente
- Herencia sería explosiva (N características × M tipos = N×M clases)
- Combinar comportamientos en diferentes órdenes
- Cada decorador es responsabilidad única

## Contexto

**Patrón:** Componente base → Stack de decoradores

**Anatomía:**
- **Component**: Interfaz común
- **ConcreteComponent**: Objeto base
- **Decorator**: Encapsula Component, implementa interfaz igual
- Permite stacking: `new D1(new D2(new D3(component)))`

---

## Problema

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

