---
title: "Patrón Abstract Factory"
subtitle: "Familias de objetos relacionados"
subject: Patrones de Diseño Creacionales
---

(patron-abstract-factory)=
# Abstract Factory: Crear Familias Coherentes

El patrón **Abstract Factory** proporciona una interfaz para crear **familias de objetos relacionados** sin especificar sus clases concretas.

:::{note} Propósito

Crear grupos de objetos relacionados manteniendo coherencia entre ellos.
:::

---

## Problema

```java
// ❌ Sin Abstract Factory: inconsistencia entre temas
public class VentanaLinux { }
public class BotonLinux { }
public class VentanaWindows { }
public class BotonWindows { }

// El cliente debe recordar usar componentes del mismo SO
Ventana v;
Boton b;

if (sistemaOperativo.equals("LINUX")) {
    v = new VentanaLinux();
    b = new BotonLinux();
} else {
    v = new VentanaWindows();
    b = new BotonWindows();
}
// Riesgo: mezclar VentanaLinux con BotonWindows
```

---

## Solución: Abstract Factory

```java
/**
 * Interfaz Abstract Factory.
 */
public interface UIFactory {
    Ventana crearVentana();
    Boton crearBoton();
}

/**
 * Interfaces para productos.
 */
public interface Ventana {
    void renderizar();
}

public interface Boton {
    void presionar();
}

/**
 * Implementación para Linux.
 */
public class LinuxFactory implements UIFactory {
    @Override
    public Ventana crearVentana() {
        return new VentanaLinux();
    }
    
    @Override
    public Boton crearBoton() {
        return new BotonLinux();
    }
}

public class VentanaLinux implements Ventana {
    @Override
    public void renderizar() {
        System.out.println("Ventana con GTK...");
    }
}

public class BotonLinux implements Boton {
    @Override
    public void presionar() {
        System.out.println("Botón Linux presionado...");
    }
}

/**
 * Implementación para Windows.
 */
public class WindowsFactory implements UIFactory {
    @Override
    public Ventana crearVentana() {
        return new VentanaWindows();
    }
    
    @Override
    public Boton crearBoton() {
        return new BotonWindows();
    }
}

public class VentanaWindows implements Ventana {
    @Override
    public void renderizar() {
        System.out.println("Ventana con Win32...");
    }
}

public class BotonWindows implements Boton {
    @Override
    public void presionar() {
        System.out.println("Botón Windows presionado...");
    }
}

/**
 * Aplicación que usa la factory.
 */
public class Aplicacion {
    private UIFactory factory;
    private Ventana ventana;
    private Boton boton;
    
    public Aplicacion(UIFactory factory) {
        this.factory = factory;
    }
    
    public void inicializar() {
        ventana = factory.crearVentana();
        boton = factory.crearBoton();
    }
    
    public void mostrar() {
        ventana.renderizar();
        boton.presionar();
    }
}

// Uso:
String so = System.getProperty("os.name");
UIFactory factory = so.contains("Windows") 
    ? new WindowsFactory() 
    : new LinuxFactory();

Aplicacion app = new Aplicacion(factory);
app.inicializar();
app.mostrar();
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Coherencia**: Garantiza que objetos relacionados se usen juntos
- **Escalabilidad**: Agregar nuevas familias es fácil
- **Aislamiento**: Desvincula cliente de implementaciones concretas

### ❌ Desventajas

- **Complejidad**: Muchas clases e interfaces
- **Rigidez**: Difícil agregar nuevos tipos a familias existentes
- **Overhead**: Indirección adicional


