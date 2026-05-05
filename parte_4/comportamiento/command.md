---
title: "Patrón Command"
subtitle: "Encapsular una solicitud como un objeto"
subject: Patrones de Diseño de Comportamiento
---

(patron-command)=
# Command: Solicitud como Objeto

El patrón **Command** encapsula una solicitud como un objeto, permitiendo parametrizar clientes con diferentes solicitudes, encolar solicitudes y soportar operaciones deshacibles.

:::{admonition} Propósito
:class: note

Encapsular solicitud como objeto para permitir deshacer, rehacer, encolar.
:::

---

## Concepto

Command transforma método en objeto:

```
Sin Command:  ejecutarAcción()        → Acción inmediata

Con Command:  comando = crearCommand() → Guardar, ejecutar, deshacer
                      → comando.ejecutar()
```

---

## Problema

```java
// ❌ Acoplamiento directo entre solicitante y receptor
class Control {
    private Luz luz;
    
    void presionarBoton() {
        luz.encender();  // Acoplado directo
    }
}

// ¿Y si quiero agregar deshacer? ¿O encolar? ¿O registrar?
```

---

## Solución: Command

```java
/**
 * Interfaz Command: abstracción de solicitud.
 */
public interface Comando {
    void ejecutar();
    void deshacer();
}

/**
 * Receptor: objeto que ejecuta la acción.
 */
public class Luz {
    private boolean estaEncendida = false;
    
    public void encender() {
        estaEncendida = true;
        System.out.println("💡 Luz encendida");
    }
    
    public void apagar() {
        estaEncendida = false;
        System.out.println("💡 Luz apagada");
    }
    
    public boolean estaEncendida() {
        return estaEncendida;
    }
}

/**
 * Comando concreto: Encender luz.
 */
public class ComandoEncenderLuz implements Comando {
    private Luz luz;
    
    public ComandoEncenderLuz(Luz luz) {
        this.luz = luz;
    }
    
    @Override
    public void ejecutar() {
        luz.encender();
    }
    
    @Override
    public void deshacer() {
        luz.apagar();
    }
}

/**
 * Comando concreto: Apagar luz.
 */
public class ComandoApagarLuz implements Comando {
    private Luz luz;
    
    public ComandoApagarLuz(Luz luz) {
        this.luz = luz;
    }
    
    @Override
    public void ejecutar() {
        luz.apagar();
    }
    
    @Override
    public void deshacer() {
        luz.encender();
    }
}

/**
 * Invocador: ejecuta comandos.
 */
public class Control {
    private List<Comando> historial = new ArrayList<>();
    private int indiceActual = -1;
    
    public void presionarBoton(Comando comando) {
        // Ejecutar comando
        comando.ejecutar();
        
        // Agregar al historial
        indiceActual++;
        if (indiceActual < historial.size()) {
            historial.subList(indiceActual, historial.size()).clear();
        }
        historial.add(comando);
    }
    
    public void deshacer() {
        if (indiceActual >= 0) {
            historial.get(indiceActual).deshacer();
            indiceActual--;
        }
    }
    
    public void rehacer() {
        if (indiceActual < historial.size() - 1) {
            indiceActual++;
            historial.get(indiceActual).ejecutar();
        }
    }
}

// ✅ Uso: Desacoplado y flexible
Luz luz = new Luz();
Control control = new Control();

control.presionarBoton(new ComandoEncenderLuz(luz));
control.presionarBoton(new ComandoApagarLuz(luz));
control.presionarBoton(new ComandoEncenderLuz(luz));

control.deshacer();  // Apaga la luz
control.deshacer();  // La enciende nuevamente
control.rehacer();   // Vuelve a apagarla
```

---

## Diagram UML

```
        ┌──────────────┐
        │   Comando    │
        │ <<interface>>│
        ├──────────────┤
        │+ ejecutar()  │
        │+ deshacer()  │
        └────────┬─────┘
                 │
      ┌──────────┴──────────┐
      │                     │
 ┌────▼──────────┐  ┌──────▼────────────┐
 │ComandoEncender│  │ComandoApagar      │
 ├────────────────┤  ├──────────────────┤
 │- luz: Luz     │  │- luz: Luz        │
 │+ ejecutar()   │  │+ ejecutar()      │
 │+ deshacer()   │  │+ deshacer()      │
 └────────────────┘  └──────────────────┘
        ▲                      ▲
        │ usa                  │ usa
        └──────────────────────┘
              │
        ┌─────▼──────┐
        │  Control   │
        ├────────────┤
        │- historial │
        │+ presionar()
        │+ deshacer()
        └────────────┘
```

---

## Variantes

**1. Macro Command (Composite):**
```java
public class MacroComando implements Comando {
    private List<Comando> comandos = new ArrayList<>();
    
    public void agregar(Comando cmd) {
        comandos.add(cmd);
    }
    
    @Override
    public void ejecutar() {
        for (Comando cmd : comandos) {
            cmd.ejecutar();
        }
    }
}
```

**2. Command con parámetros:**
```java
public class ComandoLuz implements Comando {
    private Luz luz;
    private int brillo;  // Parámetro
    
    public ComandoLuz(Luz luz, int brillo) {
        this.luz = luz;
        this.brillo = brillo;
    }
}
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Desacoplamiento**: Solicitante no conoce receptor
- **Flexibilidad**: Parámetrizar comandos
- **Historial**: Deshacer/rehacer fácilmente
- **Encolar**: Guardar comandos para ejecución posterior

### ❌ Desventajas

- **Clases**: Muchas clases Command pequeñas
- **Overhead**: Objeto por cada comando
- **Memoria**: Historial grande consume recursos
- **Complejidad**: Overkill para casos simples

---

## Cuándo Usarlo

✅ **Usa cuando:**
- Necesitas deshacer/rehacer
- Necesitas encolar operaciones
- Necesitas registrar cambios
- Ejemplos: Editores, IDEs, shells

---

## Ejercicio

```{exercise}
:label: ej-command-editor

Crea editor con Command:
1. Comando: `InsertarTexto`, `BorrarTexto`
2. Control: historial de deshacer/rehacer
```

```{solution} ej-command-editor
:class: dropdown

```java
public interface ComandoEdición {
    void ejecutar();
    void deshacer();
}

public class Documento {
    private StringBuilder contenido = new StringBuilder();
    
    public void insertar(String texto) {
        contenido.append(texto);
    }
    
    public void borrar(int inicio, int fin) {
        contenido.delete(inicio, fin);
    }
    
    public String getContenido() {
        return contenido.toString();
    }
}

public class ComandoInsertar implements ComandoEdición {
    private Documento doc;
    private String texto;
    
    public ComandoInsertar(Documento doc, String texto) {
        this.doc = doc;
        this.texto = texto;
    }
    
    @Override
    public void ejecutar() {
        doc.insertar(texto);
        System.out.println("Insertado: " + texto);
    }
    
    @Override
    public void deshacer() {
        // Simplificado: realmente habría que guardar estado
        System.out.println("Deshaciendo inserción");
    }
}

public class Editor {
    private List<ComandoEdición> historial = new ArrayList<>();
    
    public void ejecutar(ComandoEdición cmd) {
        cmd.ejecutar();
        historial.add(cmd);
    }
    
    public void deshacer() {
        if (!historial.isEmpty()) {
            ComandoEdición cmd = historial.remove(historial.size() - 1);
            cmd.deshacer();
        }
    }
}

// Uso
Editor editor = new Editor();
Documento doc = new Documento();
editor.ejecutar(new ComandoInsertar(doc, "Hola"));
editor.ejecutar(new ComandoInsertar(doc, " mundo"));
System.out.println(doc.getContenido()); // Hola mundo
editor.deshacer();
```
```
