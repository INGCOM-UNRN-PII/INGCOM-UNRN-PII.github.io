---
title: "Builder"
subtitle: "Construir objetos complejos paso a paso"
subject: Patrones de Diseño Creacionales
---

(patron-builder)=
# Builder: Construcción Gradual

El patrón **Builder** separa la construcción de un objeto complejo de su representación, permitiendo construir el objeto paso a paso.

:::{note} Propósito

Construir objetos complejos de manera legible y flexible, sin requerir múltiples constructores.
:::

---

## Problema

```java
// ❌ Sin Builder: constructores con muchos parámetros
public class Casa {
    public Casa(int habitaciones, int baños, boolean piscina,
                boolean garaje, boolean sotano, boolean terraza, ...) {
        // ¿Qué es cada parámetro?
    }
}

// Uso confuso:
Casa casa = new Casa(3, 2, true, true, false, true);  // ¿Qué significa esto?
```

---

## Solución: Builder

```java
/**
 * Clase a construir.
 */
public class Casa {
    private int habitaciones;
    private int baños;
    private boolean piscina;
    private boolean garaje;
    private boolean sotano;
    private boolean terraza;
    
    // Constructor privado: solo el builder puede instanciar
    private Casa(CasaBuilder builder) {
        this.habitaciones = builder.habitaciones;
        this.baños = builder.baños;
        this.piscina = builder.piscina;
        this.garaje = builder.garaje;
        this.sotano = builder.sotano;
        this.terraza = builder.terraza;
    }
    
    // Getters...
    public int getHabitaciones() { return habitaciones; }
    public int getBaños() { return baños; }
    public boolean tienePiscina() { return piscina; }
    
    @Override
    public String toString() {
        return "Casa{" +
            "habitaciones=" + habitaciones +
            ", baños=" + baños +
            ", piscina=" + piscina +
            ", garaje=" + garaje +
            ", sotano=" + sotano +
            ", terraza=" + terraza +
            '}';
    }
    
    /**
     * Builder anidado.
     */
    public static class CasaBuilder {
        private int habitaciones = 0;
        private int baños = 0;
        private boolean piscina = false;
        private boolean garaje = false;
        private boolean sotano = false;
        private boolean terraza = false;
        
        public CasaBuilder habitaciones(int cantidad) {
            this.habitaciones = cantidad;
            return this;
        }
        
        public CasaBuilder baños(int cantidad) {
            this.baños = cantidad;
            return this;
        }
        
        public CasaBuilder conPiscina() {
            this.piscina = true;
            return this;
        }
        
        public CasaBuilder conGaraje() {
            this.garaje = true;
            return this;
        }
        
        public CasaBuilder conSotano() {
            this.sotano = true;
            return this;
        }
        
        public CasaBuilder conTerraza() {
            this.terraza = true;
            return this;
        }
        
        public Casa construir() {
            return new Casa(this);
        }
    }
}

// Uso fluido y legible:
Casa casa = new Casa.CasaBuilder()
    .habitaciones(3)
    .baños(2)
    .conPiscina()
    .conGaraje()
    .conTerraza()
    .construir();

System.out.println(casa);
```

---

## Variantes

### Builder Separado

```java
public interface Constructor {
    void construirBase();
    void construirPuertas();
    void construirVentanas();
}

public class CasaConstructor implements Constructor {
    private Casa casa = new Casa();
    
    @Override
    public void construirBase() {
        casa.base = "Hormigón";
    }
    
    @Override
    public void construirPuertas() {
        casa.puertas = 4;
    }
    
    @Override
    public void construirVentanas() {
        casa.ventanas = 8;
    }
    
    public Casa obtener() {
        return casa;
    }
}

// Director que orquesta la construcción
public class Capataz {
    private Constructor constructor;
    
    public Capataz(Constructor constructor) {
        this.constructor = constructor;
    }
    
    public void construir() {
        constructor.construirBase();
        constructor.construirPuertas();
        constructor.construirVentanas();
    }
}

// Uso:
Constructor constructor = new CasaConstructor();
Capataz capataz = new Capataz(constructor);
capataz.construir();
Casa casa = constructor.obtener();
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Claridad**: Construcción explícita y legible
- **Flexibilidad**: Pasos opcionales y orden flexible
- **Inmutabilidad**: Objeto final es inmutable
- **Valores por defecto**: Fácil tener defaults

### ❌ Desventajas

- **Complejidad**: Más código que constructores simples
- **Overhead**: Creación de objeto builder adicional
- **Mutabilidad del builder**: Puede reutilizarse incorrectamente

---

