---
title: "Patrón Template Method"
subtitle: "Definir estructura del algoritmo en la superclase"
subject: Patrones de Diseño de Comportamiento
---

(patron-template-method)=
# Template Method: Estructura Fija

El patrón **Template Method** define la estructura de un algoritmo en una clase base, dejando detalles específicos para las subclases. Define el esqueleto del algoritmo pero deja algunos pasos para que las subclases los implementen.

:::{note} Propósito

Definir estructura de algoritmo, permitiendo que subclases implementen pasos específicos.
:::

---

## Origen e Historia

Gang of Four 1994. Surge de frameworks que necesitan estructura fija pero pasos customizables (ej: Spring templates).

## Motivación

Necesario cuando:
- Algoritmo tiene estructura similar, pasos diferentes
- Múltiples clases con estructura parecida
- Quieres reutilizar estructura, variar pasos
- Evitar duplicación de código

## Contexto

**Patrón:** Superclase define estructura, subclases implementan pasos

**Anatomía:**
- **AbstractClass**: Define template method (final)
- **AbstractClass**: Define pasos abstract
- **ConcreteClass**: Implementa pasos
- Inversión de control: framework llama tu código

**Distinción de Strategy vs Template Method:**
- **Template Method**: Herencia, estructura fija
- **Strategy**: Composición, algoritmo intercambiable

---

## Problema

```
Clase Base (Template)
  ├─ paso1() [final]
  ├─ paso2() [abstract] ← Para subclases
  ├─ paso3() [final]
  └─ algoritmo() { paso1(); paso2(); paso3(); }

Subclase A implementa paso2()
Subclase B implementa paso2() diferente
```

---

## Problema

```java
// ❌ Duplicación de estructura
class TéGenerador {
    void preparar() {
        calentar agua();
        poner té();
        colar();
        servir();
    }
}

class CaféGenerador {
    void preparar() {
        calentar agua();
        poner café();      // ← Diferente
        colar();
        servir();
    }
}
// Estructura similar pero código duplicado
```

---

## Solución: Template Method

```java
/**
 * Template: define estructura, algunos pasos son abstract.
 */
public abstract class BebidaGenerador {
    /**
     * Template method: estructura fija.
     */
    public final void preparar() {
        calentar agua();
        agregarIngrediente();  // ← Deja para subclases
        colar();
        servirEnTaza();
    }
    
    protected void calienteAgua() {
        System.out.println("1. Calentando agua a 100°C");
    }
    
    /**
     * Método abstract: cada subclase implementa diferente.
     */
    protected abstract void agregarIngrediente();
    
    protected void colar() {
        System.out.println("3. Colando bebida");
    }
    
    protected void servirEnTaza() {
        System.out.println("4. Sirviendo en taza");
    }
}

/**
 * Subclase concreta: Té.
 */
public class TéGenerador extends BebidaGenerador {
    @Override
    protected void agregarIngrediente() {
        System.out.println("2. Agregando bolsa de té");
    }
}

/**
 * Subclase concreta: Café.
 */
public class CaféGenerador extends BebidaGenerador {
    @Override
    protected void agregarIngrediente() {
        System.out.println("2. Moliendo café fresco");
        System.out.println("2. Agregando café molido");
    }
}

/**
 * Subclase concreta: Chocolate.
 */
public class ChocolateGenerador extends BebidaGenerador {
    @Override
    protected void agregarIngrediente() {
        System.out.println("2. Agregando chocolate en polvo");
        System.out.println("2. Agregando leche");
    }
}

// ✅ Uso: Misma estructura, comportamiento diferente
BebidaGenerador té = new TéGenerador();
té.preparar();

System.out.println();

BebidaGenerador café = new CaféGenerador();
café.preparar();

System.out.println();

BebidaGenerador chocolate = new ChocolateGenerador();
chocolate.preparar();
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Código reutilizable**: Estructura común en base
- **Consistencia**: Todos siguen el mismo estructura
- **Extensibilidad**: Agregar variantes sin modificar base
- **Mantenibilidad**: Cambios en estructura solo en un lugar

### ❌ Desventajas

- **Herencia**: Usa herencia (puede ser rígido)
- **Flexibilidad limitada**: Estructura fija
- **Método virtual costoso**: Si muchas subclases
- **Complejidad**: Puede ser overkill

---

## Comparación: Template Method vs. Strategy

| Aspecto | Template Method | Strategy |
|--------|----------------|----------|
| **Mecanismo** | Herencia | Composición |
| **Flexibilidad** | Fija en tiempo de compilación | Cambiar en runtime |
| **Testabilidad** | Testear subclases | Testear strategies |
| **Acoplamiento** | Más acoplado | Desacoplado |

---

## Cuándo Usarlo

✅ **Usa cuando:**
- Múltiples clases con estructura similar
- Estructura común pero pasos variables
- Quieres evitar duplicación
- Ejemplos: Generadores, procesadores, frameworks

---

## Ejercicio

```{exercise}
:label: ej-template-datos

Crea procesador de datos con Template Method:
1. Cargar datos
2. Procesar (diferente según tipo)
3. Guardar
```

```{solution} ej-template-datos
:class: dropdown

```java
public abstract class ProcesadorDatos {
    public final void procesar() {
        cargarDatos();
        procesarDatos();
        guardarDatos();
    }
    
    protected void cargarDatos() {
        System.out.println("Cargando datos...");
    }
    
    protected abstract void procesarDatos();
    
    protected void guardarDatos() {
        System.out.println("Guardando resultados...");
    }
}

public class ProcesadorCSV extends ProcesadorDatos {
    @Override
    protected void procesarDatos() {
        System.out.println("Procesando CSV: parseando columnas");
    }
}

public class ProcesadorJSON extends ProcesadorDatos {
    @Override
    protected void procesarDatos() {
        System.out.println("Procesando JSON: parseando objetos");
    }
}

// Uso
ProcesadorDatos csv = new ProcesadorCSV();
csv.procesar();

ProcesadorDatos json = new ProcesadorJSON();
json.procesar();
```
```
