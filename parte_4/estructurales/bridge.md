---
title: "Patrón Bridge"
subtitle: "Desacoplar abstracción de implementación"
subject: Patrones de Diseño Estructurales
---

(patron-bridge)=
# Bridge: Separar Abstracción e Implementación

El patrón **Bridge** desacopla una abstracción de su implementación para que puedan variar independientemente.

:::{note} Propósito

Permitir que abstracción e implementación varíen de manera independiente, evitando explosión de subclases.
:::

---

## Origen e Historia

Documentado por Gang of Four en 1994. Bridge surge del reconocimiento de que herencia múltiple (combinar múltiples dimensiones de variación) crea explosión de clases. Fue popularizado especialmente en frameworks de gráficos.

## Motivación

Necesario cuando:
- Tienes múltiples dimensiones de variación independientes
- Quieres evitar jerarquías de herencia explosivas
- Abstracciones e implementaciones varían independientemente
- Necesitas compartir implementaciones entre abstracciones

## Contexto

**Estructura:**
- **Abstracción**: Define interfaz general (ej: Forma)
- **Implementador**: Define interfaz para implementaciones (ej: Renderizador)
- **La connexión**: Abstracción usa Implementador, ambos varían independientemente

**Anatomía:** N formas × M renderizadores sin N×M clases

---

## Problema

Bridge resuelve el problema de tener múltiples dimensiones de variación:

```
Sin Bridge: Explosión de clases
┌─ Círculo
│  ├─ CirculoOpenGL
│  └─ CirculoVulkan
└─ Cuadrado
   ├─ CuadradoOpenGL
   └─ CuadradoVulkan
```

```
Con Bridge: Variación independiente
Forma ─┬─ Círculo
       └─ Cuadrado

Renderizador ─┬─ OpenGL
              └─ Vulkan
```

---

## Problema

```java
// ❌ Sin Bridge: explosión de clases
abstract class Forma {
    abstract void dibujar();
}

class CirculoOpenGL extends Forma {
    @Override
    void dibujar() { System.out.println("Círculo con OpenGL"); }
}

class CirculoVulkan extends Forma {
    @Override
    void dibujar() { System.out.println("Círculo con Vulkan"); }
}

class CuadradoOpenGL extends Forma {
    @Override
    void dibujar() { System.out.println("Cuadrado con OpenGL"); }
}

class CuadradoVulkan extends Forma {
    @Override
    void dibujar() { System.out.println("Cuadrado con Vulkan"); }
}

// Cada nueva combinación requiere nueva clase
```

---

## Solución: Bridge

```java
/**
 * Interfaz Implementación (Implementor).
 * Define operaciones primitivas en las que se basa la abstracción.
 */
public interface Renderizador {
    void dibujarCirculo(int x, int y, int radio);
    void dibujarCuadrado(int x, int y, int lado);
}

/**
 * Abstracción (Abstract) que usa Renderizador.
 */
public abstract class Forma {
    protected Renderizador renderizador;
    
    public Forma(Renderizador renderizador) {
        this.renderizador = renderizador;
    }
    
    abstract void dibujar();
}

/**
 * Abstracción Refinada (Refined Abstraction).
 */
public class Circulo extends Forma {
    private int x, y, radio;
    
    public Circulo(Renderizador renderizador, int x, int y, int radio) {
        super(renderizador);
        this.x = x;
        this.y = y;
        this.radio = radio;
    }
    
    @Override
    public void dibujar() {
        renderizador.dibujarCirculo(x, y, radio);
    }
}

public class Cuadrado extends Forma {
    private int x, y, lado;
    
    public Cuadrado(Renderizador renderizador, int x, int y, int lado) {
        super(renderizador);
        this.x = x;
        this.y = y;
        this.lado = lado;
    }
    
    @Override
    public void dibujar() {
        renderizador.dibujarCuadrado(x, y, lado);
    }
}

/**
 * Implementador concreto: OpenGL.
 */
public class RenderizadorOpenGL implements Renderizador {
    @Override
    public void dibujarCirculo(int x, int y, int radio) {
        System.out.println("OpenGL: Dibujando círculo en (" + x + "," + y + "), radio=" + radio);
    }
    
    @Override
    public void dibujarCuadrado(int x, int y, int lado) {
        System.out.println("OpenGL: Dibujando cuadrado en (" + x + "," + y + "), lado=" + lado);
    }
}

/**
 * Implementador concreto: Vulkan.
 */
public class RenderizadorVulkan implements Renderizador {
    @Override
    public void dibujarCirculo(int x, int y, int radio) {
        System.out.println("Vulkan: Círculo optimizado en (" + x + "," + y + "), radio=" + radio);
    }
    
    @Override
    public void dibujarCuadrado(int x, int y, int lado) {
        System.out.println("Vulkan: Cuadrado optimizado en (" + x + "," + y + "), lado=" + lado);
    }
}

// ✅ Uso: Sin explosión de clases
Renderizador opengl = new RenderizadorOpenGL();
Renderizador vulkan = new RenderizadorVulkan();

Forma circuloGL = new Circulo(opengl, 50, 50, 20);
Forma circuloVK = new Circulo(vulkan, 100, 100, 30);
Forma cuadradoGL = new Cuadrado(opengl, 200, 200, 40);

circuloGL.dibujar();      // OpenGL: Dibujando círculo...
circuloVK.dibujar();      // Vulkan: Círculo optimizado...
cuadradoGL.dibujar();     // OpenGL: Dibujando cuadrado...
```

---

## Diagrama UML

```
┌───────────────────────────────────────┐
│      Abstracción                      │
│     (Forma)                           │
├───────────────────────────────────────┤
│- renderizador: Renderizador           │
│+ Forma(renderizador)                  │
│+ dibujar(): void [abstract]           │
└─────────────┬───────────────────────┬─┘
              │ usa                   │
              │                       │
              │              ┌────────┴──────────────┐
              │              │                       │
              │     ┌────────▼─────────┐   ┌────────▼─────────┐
              │     │ Circulo          │   │ Cuadrado         │
              │     ├──────────────────┤   ├──────────────────┤
              │     │ - x, y, radio    │   │ - x, y, lado     │
              │     │+ dibujar()       │   │+ dibujar()       │
              │     └──────────────────┘   └──────────────────┘
              │
              ▼
     ┌─────────────────────────┐
     │ <<interface>>           │
     │ Renderizador            │
     ├─────────────────────────┤
     │+ dibujarCirculo()       │
     │+ dibujarCuadrado()      │
     └─────────────┬───────────┘
                   │
        ┌──────────┴──────────┐
        │                     │
  ┌─────▼────────────┐  ┌────▼────────────┐
  │RenderizadorOpenGL│  │RenderizadorVulkan│
  ├──────────────────┤  ├──────────────────┤
  │+ dibujarCirculo()│  │+ dibujarCirculo()│
  │+ dibujarCuadrado│  │+ dibujarCuadrado │
  └──────────────────┘  └──────────────────┘
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Desacoplamiento**: Abstracción e implementación independientes
- **Escalabilidad**: Agregar formas o renderizadores sin afectar otros
- **Evita explosión de clases**: N*M en lugar de N+M clases
- **Delegación**: Usa composición sobre herencia

### ❌ Desventajas

- **Complejidad**: Más difícil de entender que herencia simple
- **Indirección**: Extra de llamadas
- **Overhead**: Capas adicionales de abstracción

---

## Cuándo Usarlo

✅ **Usa Bridge cuando:**
- Tienes múltiples dimensiones de variación
- Quieres evitar combinaciones explosivas
- La abstracción e implementación deben cambiar independientemente

❌ **Evita cuando:**
- Solo una dimensión de variación
- La herencia simple es suficiente

---

## Ejercicio

```{exercise}
:label: ej-bridge-dispositivos

Crea un sistema con:
1. Abstracción: `Dispositivo` (Computadora, Telefono)
2. Implementación: `SO` (Windows, Linux, Android)
3. Bridge entre ellos
```

```{solution} ej-bridge-dispositivos
:class: dropdown

```java
public interface SO {
    void iniciar();
    void apagar();
    void instalarApp(String nombre);
}

public class SOWindows implements SO {
    @Override
    public void iniciar() { System.out.println("Windows iniciando..."); }
    
    @Override
    public void apagar() { System.out.println("Windows apagando..."); }
    
    @Override
    public void instalarApp(String nombre) {
        System.out.println("Instalando " + nombre + " en Windows");
    }
}

public class SOAndroid implements SO {
    @Override
    public void iniciar() { System.out.println("Android iniciando..."); }
    
    @Override
    public void apagar() { System.out.println("Android apagando..."); }
    
    @Override
    public void instalarApp(String nombre) {
        System.out.println("Instalando APK " + nombre + " en Android");
    }
}

public abstract class Dispositivo {
    protected SO so;
    
    public Dispositivo(SO so) {
        this.so = so;
    }
    
    abstract void usar();
}

public class Computadora extends Dispositivo {
    public Computadora(SO so) {
        super(so);
    }
    
    @Override
    public void usar() {
        so.iniciar();
        so.instalarApp("Visual Studio Code");
    }
}

public class Telefono extends Dispositivo {
    public Telefono(SO so) {
        super(so);
    }
    
    @Override
    public void usar() {
        so.iniciar();
        so.instalarApp("WhatsApp");
    }
}

// Uso
Dispositivo pc = new Computadora(new SOWindows());
Dispositivo telefono = new Telefono(new SOAndroid());

pc.usar();         // Windows iniciando... Instalando...
telefono.usar();   // Android iniciando... Instalando APK...
```
```

