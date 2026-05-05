---
title: "Patrón Composite"
subtitle: "Componer objetos en estructuras de árbol"
subject: Patrones de Diseño Estructurales
---

(patron-composite)=
# Composite: Jerarquía Uniforme

El patrón **Composite** permite componer objetos en estructuras de árbol para representar jerarquías parte-todo, permitiendo que los clientes traten objetos individuales y composiciones de objetos de manera uniforme.

:::{note} Propósito

Componer objetos en estructuras de árbol y poder tratarlos como objetos individuales.
:::

---

## Origen e Historia

Gang of Four 1994. Surge de la necesidad de tratar uniformemente estructuras jerárquicas (árboles) sin que el cliente conozca si está trabajando con una hoja o un compuesto.

## Motivación

Necesario cuando:
- Tienes estructuras jerárquicas parte-todo
- Quieres que cliente trate uniformemente hojas y compuestos
- Necesitas recursión sobre estructuras arbitrariamente profundas
- Implementar sin Composite requiere condicionales constantes

## Contexto

**Escenario:** Archivos/carpetas, widgets UI, menús anidados

**Anatomía:**
- **Component**: Interfaz común
- **Leaf**: Sin hijos (archivo, MenuItem)
- **Composite**: Puede tener Component (carpeta, Menu)
- Ambos responden a mismas operaciones

---

## Problema

Composite permite crear estructuras donde:
- **Hoja**: No tiene hijos (elemento terminal)
- **Compuesto**: Puede contener hojas o compuestos
- **Interfaz uniforme**: Ambos implementan la misma interfaz

---

## Problema

```java
// ❌ Sin Composite: Se debe diferenciar entre tipos
class Archivo {
    private String nombre;
    private int tamaño;
    
    public int obtenerTamaño() {
        return tamaño;
    }
}

class Carpeta {
    private String nombre;
    private List<Archivo> archivos;
    
    public int obtenerTamaño() {
        int total = 0;
        for (Archivo archivo : archivos) {
            total += archivo.obtenerTamaño();
        }
        return total;
    }
}

// ¿Y si quiero carpetas anidadas?
// ¿Y si quiero un método que funcione con ambos?
```

---

## Solución: Composite

```java
/**
 * Componente común: interfaz uniforme.
 */
public abstract class ElementoSistemaArchivos {
    protected String nombre;
    
    public ElementoSistemaArchivos(String nombre) {
        this.nombre = nombre;
    }
    
    abstract void mostrar(int indentacion);
    abstract int obtenerTamaño();
    
    public String getNombre() {
        return nombre;
    }
}

/**
 * Hoja: Archivo sin hijos.
 */
public class Archivo extends ElementoSistemaArchivos {
    private int tamaño;
    
    public Archivo(String nombre, int tamaño) {
        super(nombre);
        this.tamaño = tamaño;
    }
    
    @Override
    public void mostrar(int indentacion) {
        for (int i = 0; i < indentacion; i++) {
            System.out.print("  ");
        }
        System.out.println("📄 " + nombre + " (" + tamaño + " KB)");
    }
    
    @Override
    public int obtenerTamaño() {
        return tamaño;
    }
}

/**
 * Compuesto: Carpeta que puede contener archivos y carpetas.
 */
public class Carpeta extends ElementoSistemaArchivos {
    private List<ElementoSistemaArchivos> elementos;
    
    public Carpeta(String nombre) {
        super(nombre);
        this.elementos = new ArrayList<>();
    }
    
    public void agregar(ElementoSistemaArchivos elemento) {
        elementos.add(elemento);
    }
    
    public void remover(ElementoSistemaArchivos elemento) {
        elementos.remove(elemento);
    }
    
    @Override
    public void mostrar(int indentacion) {
        for (int i = 0; i < indentacion; i++) {
            System.out.print("  ");
        }
        System.out.println("📁 " + nombre);
        
        for (ElementoSistemaArchivos elemento : elementos) {
            elemento.mostrar(indentacion + 1);
        }
    }
    
    @Override
    public int obtenerTamaño() {
        int total = 0;
        for (ElementoSistemaArchivos elemento : elementos) {
            total += elemento.obtenerTamaño();
        }
        return total;
    }
}

// ✅ Uso uniforme
Carpeta raiz = new Carpeta("C:\\");

Carpeta documentos = new Carpeta("Documentos");
Archivo tesis = new Archivo("tesis.pdf", 5000);
Archivo carta = new Archivo("carta.doc", 100);

documentos.agregar(tesis);
documentos.agregar(carta);

Carpeta fotos = new Carpeta("Fotos");
Archivo foto1 = new Archivo("vacaciones.jpg", 2000);
Archivo foto2 = new Archivo("familia.jpg", 1500);

fotos.agregar(foto1);
fotos.agregar(foto2);

raiz.agregar(documentos);
raiz.agregar(fotos);
raiz.agregar(new Archivo("readme.txt", 10));

// Mismo método para todos
raiz.mostrar(0);
System.out.println("Tamaño total: " + raiz.obtenerTamaño() + " KB");
```

---

## Diagrama UML

```
         ┌─────────────────────────────┐
         │ ElementoSistemaArchivos     │
         │     <<abstract>>             │
         ├─────────────────────────────┤
         │ # nombre: String            │
         │+ mostrar()                  │
         │+ obtenerTamaño()            │
         └──────────────┬──────────────┘
                        │
             ┌──────────┴──────────┐
             │                     │
        ┌────▼──────────┐  ┌──────▼────────────┐
        │   Archivo     │  │    Carpeta       │
        ├───────────────┤  ├──────────────────┤
        │ - tamaño      │  │ - elementos: []  │
        │+ mostrar()    │  │+ agregar()       │
        │+ obtenerTamaño│  │+ remover()       │
        └───────────────┘  │+ mostrar()       │
                           │+ obtenerTamaño()│
                           └──────────────────┘
                                   │
                                   │ contiene
                                   ▼
                           ElementoSistemaArchivos
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Uniformidad**: Interfaz única para individuales y composiciones
- **Flexibilidad**: Agregar nuevos tipos sin cambiar código
- **Recursión**: Navegar estructuras complejas fácilmente
- **Simplicidad**: Cliente no necesita saber sobre estructura

### ❌ Desventajas

- **Complejidad**: Más difícil de diseñar
- **Overhead**: Comparaciones de tipos
- **Confusión**: Diferencias semánticas entre hoja y compuesto
- **Performance**: Recalcular valores puede ser costoso

---

## Cuándo Usarlo

✅ **Usa Composite cuando:**
- Tienes estructuras jerárquicas parte-todo
- Quieres tratar uniformemente hojas y compuestos
- Ejemplos: Archivos/carpetas, widgets de UI, menús

❌ **Evita cuando:**
- La estructura es plana
- El costo de recursión es prohibitivo

---

## Ejercicio

```{exercise}
:label: ej-composite-menu

Crea un sistema de menús:
1. `MenuItem` (hoja) con acción
2. `Menu` (compuesto) que contiene items y submenús
3. Método `mostrar()` que dibuja árbol de menús
```

```{solution} ej-composite-menu
:class: dropdown

```java
public abstract class MenuElement {
    protected String nombre;
    
    public MenuElement(String nombre) {
        this.nombre = nombre;
    }
    
    abstract void mostrar(int nivel);
    abstract void ejecutar();
}

public class MenuItem extends MenuElement {
    private Runnable accion;
    
    public MenuItem(String nombre, Runnable accion) {
        super(nombre);
        this.accion = accion;
    }
    
    @Override
    public void mostrar(int nivel) {
        for (int i = 0; i < nivel; i++) System.out.print("  ");
        System.out.println("→ " + nombre);
    }
    
    @Override
    public void ejecutar() {
        System.out.println("Ejecutando: " + nombre);
        accion.run();
    }
}

public class Menu extends MenuElement {
    private List<MenuElement> items = new ArrayList<>();
    
    public Menu(String nombre) {
        super(nombre);
    }
    
    public void agregar(MenuElement item) {
        items.add(item);
    }
    
    @Override
    public void mostrar(int nivel) {
        for (int i = 0; i < nivel; i++) System.out.print("  ");
        System.out.println("╔ " + nombre);
        for (MenuElement item : items) {
            item.mostrar(nivel + 1);
        }
    }
    
    @Override
    public void ejecutar() {
        System.out.println("Abriendo menú: " + nombre);
    }
}

// Uso
Menu principal = new Menu("Archivo");
principal.agregar(new MenuItem("Nuevo", () -> System.out.println("Creando nuevo...")));
principal.agregar(new MenuItem("Abrir", () -> System.out.println("Abriendo...")));

Menu recientes = new Menu("Recientes");
recientes.agregar(new MenuItem("documento1.txt", () -> {}));
recientes.agregar(new MenuItem("documento2.txt", () -> {}));

principal.agregar(recientes);
principal.agregar(new MenuItem("Salir", () -> System.exit(0)));

principal.mostrar(0);
```
```
