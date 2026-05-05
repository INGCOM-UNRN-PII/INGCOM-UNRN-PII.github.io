---
title: "Patrón Facade"
subtitle: "Proporcionar interfaz unificada a subsistemas complejos"
subject: Patrones de Diseño Estructurales
---

(patron-facade)=
# Facade: Interfaz Simplificada

El patrón **Facade** proporciona una interfaz unificada y simplificada a un conjunto de interfaces de un subsistema, facilitando su uso.

:::{note} Propósito

Proporcionar interfaz simplificada a subsistema complejo.
:::

---

## Origen e Historia

Gang of Four 1994. Surge de la necesidad de simplificar sistemas complejos. Popularizado en frameworks web (Rails, Spring): ActiveRecord, DataMapper, etc.

## Motivación

Necesario cuando:
- Subsistema es complejo con múltiples componentes
- Cliente solo quiere hacer operaciones simples
- Necesitas desacoplar cliente del subsistema
- Quieres punto de entrada único

## Contexto

**Patrón:** Cliente → Facade → [Subsistema interno]

**Anatomía:**
- **Facade**: Interfaz simplificada pública
- **Subsistema**: Clases complejas internas (generalmente private)
- Facade delega a componentes internos coordinadamente

---

## Problema

Facade actúa como "puerta de entrada" a un sistema complejo:

```
Cliente → Facade → [Subsistema interno]
          │    
          ├─→ ComponenteA
          ├─→ ComponenteB
          └─→ ComponenteC
```

---

## Problema

```java
// ❌ Sin Facade: Cliente debe conocer muchas clases
class Cliente {
    void reproducirPelícula(String nombre) {
        // Configuración manual de todas las partes
        PopcornEra era = new PopcornEra();
        era.iniestabilidadarApagadaLuz();
        
        Proyector proyector = new Proyector();
        proyector.encender();
        proyector.setBrillo(100);
        
        DVD dvd = new DVD();
        dvd.cargarDVD(nombre);
        dvd.reproducir();
        
        Amplificador amp = new Amplificador();
        amp.encender();
        amp.setVolumen(30);
        
        // Demasiado acoplamiento!
    }
}
```

---

## Solución: Facade

```java
/**
 * Componentes del subsistema (se asumen públicos para este ejemplo).
 */
public class PopcornEra {
    public void encender() { System.out.println("Máquina de palomitas encendida"); }
    public void apagar() { System.out.println("Máquina de palomitas apagada"); }
}

public class Proyector {
    public void encender() { System.out.println("Proyector encendido"); }
    public void apagar() { System.out.println("Proyector apagado"); }
    public void setBrillo(int nivel) { System.out.println("Brillo: " + nivel); }
}

public class DVD {
    public void cargarDVD(String película) { System.out.println("DVD cargado: " + película); }
    public void reproducir() { System.out.println("Reproduciendo..."); }
    public void parar() { System.out.println("DVD detenido"); }
}

public class Amplificador {
    public void encender() { System.out.println("Amplificador encendido"); }
    public void apagar() { System.out.println("Amplificador apagado"); }
    public void setVolumen(int nivel) { System.out.println("Volumen: " + nivel); }
}

/**
 * Facade: interfaz simplificada al sistema de cine en casa.
 */
public class CineEnCasaFacade {
    private PopcornEra popcorn;
    private Proyector proyector;
    private DVD dvd;
    private Amplificador amplificador;
    
    public CineEnCasaFacade() {
        this.popcorn = new PopcornEra();
        this.proyector = new Proyector();
        this.dvd = new DVD();
        this.amplificador = new Amplificador();
    }
    
    public void verPelícula(String nombre) {
        System.out.println("=== INICIANDO PELÍCULA ===");
        popcorn.encender();
        proyector.encender();
        proyector.setBrillo(100);
        amplificador.encender();
        amplificador.setVolumen(30);
        dvd.cargarDVD(nombre);
        dvd.reproducir();
        System.out.println("¡Que disfrutes!");
    }
    
    public void terminarPelícula() {
        System.out.println("=== FINALIZANDO ===");
        dvd.parar();
        proyector.apagar();
        amplificador.apagar();
        popcorn.apagar();
        System.out.println("Sistema de cine apagado");
    }
}

// ✅ Uso simplificado
CineEnCasaFacade cine = new CineEnCasaFacade();
cine.verPelícula("The Matrix");
// ... ver película ...
cine.terminarPelícula();
```

---

## Diagrama UML

```
              ┌────────────────────┐
              │ CineEnCasaFacade   │
              ├────────────────────┤
              │- popcorn           │
              │- proyector         │
              │- dvd               │
              │- amplificador      │
              ├────────────────────┤
              │+ verPelícula()     │
              │+ terminarPelícula()│
              └──────────┬─────────┘
                         │
         ┌───────────┬───┼───┬────────────┐
         │           │       │            │
    ┌────▼──┐  ┌────▼──┐ ┌──▼──┐  ┌─────▼────┐
    │ DVD   │  │Proyector│ │Popcorn│  │Amplificador│
    └────────┘  └─────────┘ └──────┘  └──────────┘
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Simplificación**: Cliente no ve complejidad interna
- **Desacoplamiento**: Cambios internos no afectan cliente
- **Punto de entrada único**: Fácil de mantener
- **Escalabilidad**: Agregar componentes sin afectar interfaz

### ❌ Desventajas

- **Pérdida de control**: Cliente pierde flexibilidad
- **Dios Facade**: Facade puede crecer demasiado
- **Overhead**: Indirección adicional
- **Testing**: Más difícil testear componentes individuales

---

## Cuándo Usarlo

✅ **Usa Facade cuando:**
- Subsistema es complejo y difícil de usar
- Necesitas simplificar interfaz
- Quieres desacoplar cliente de subsistema
- Ejemplo: librerías complejas (Spring, Apache Commons)

❌ **Evita cuando:**
- Subsistema es simple
- Cliente necesita control detallado

