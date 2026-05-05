---
title: "Patrón Memento"
subtitle: "Capturar y restaurar estado de un objeto"
subject: Patrones de Diseño de Comportamiento
---

(patron-memento)=
# Memento: Capturar Estado

El patrón **Memento** captura y externaliza el estado interno de un objeto sin violar su encapsulación, permitiendo restaurarlo posteriormente.

:::{note} Propósito

Capturar estado de un objeto para poder restaurarlo sin revelar detalles internos.
:::

---

## Origen e Historia

Gang of Four 1994. Surge de necesidad de capturar "snapshots" de estado para deshacer/restaurar sin violar encapsulación.

## Motivación

Necesario cuando:
- Necesitas deshacer operaciones
- Necesitas guardar puntos de guardado (checkpoint)
- Quieres capturar estado sin violar encapsulación
- Restaurar estado debe ser transparente

## Contexto

**Patrón:** Originador → Memento → Cuidador

**Anatomía:**
- **Originator**: Objeto cuyo estado se captura (Documento)
- **Memento**: Captura inmutable (MementoDocumento)
- **Caretaker**: Almacena mementos (Historial)
- Memento no expone internos del Originador

**Variantes:**
- Historial amplio vs. snapshots
- Compresión de mementos
- Serialización para persistencia

---

## Problema

```
Originador (Originator): objeto cuyo estado se desea capturar
   ↓
Memento: captura estado (inmutable)
   ↓
Cuidador (Caretaker): almacena mementos
   ↓
Restaurar: volver estado anterior
```

---

## Problema

```java
// ❌ Sin Memento: Violación de encapsulación
class Documento {
    private String contenido;
    
    // ¿Cómo hacer copia sin exponer contenido?
    public String getContenido() {
        return contenido;  // Violación de encapsulación
    }
}
```

---

## Solución: Memento

```java
/**
 * Memento: captura del estado (inmutable).
 */
public class MementoDocumento {
    private final String contenido;
    private final long timestamp;
    
    public MementoDocumento(String contenido) {
        this.contenido = contenido;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
}

/**
 * Originador: objeto cuyo estado capturamos.
 */
public class Documento {
    private String contenido = "";
    
    public void escribir(String texto) {
        contenido += texto;
        System.out.println("Escribiendo: " + contenido);
    }
    
    public void borrar() {
        contenido = "";
        System.out.println("Documento borrado");
    }
    
    /**
     * Crear memento: capturar estado.
     */
    public MementoDocumento crearMemento() {
        return new MementoDocumento(contenido);
    }
    
    /**
     * Restaurar memento: sin exponer contenido interno.
     */
    public void restaurarMemento(MementoDocumento memento) {
        this.contenido = memento.getContenido();
        System.out.println("Documentorestaurado: " + contenido);
    }
    
    public String getContenido() {
        return contenido;
    }
}

/**
 * Cuidador: almacena mementos.
 */
public class HistorialDocumento {
    private List<MementoDocumento> historial = new ArrayList<>();
    private Documento documento;
    
    public HistorialDocumento(Documento documento) {
        this.documento = documento;
    }
    
    public void guardarEstado() {
        historial.add(documento.crearMemento());
    }
    
    public void deshacer() {
        if (historial.isEmpty()) {
            System.out.println("No hay historial");
            return;
        }
        MementoDocumento memento = historial.remove(historial.size() - 1);
        documento.restaurarMemento(memento);
    }
    
    public void listarHistorial() {
        for (int i = 0; i < historial.size(); i++) {
            MementoDocumento m = historial.get(i);
            System.out.println((i+1) + ": '" + m.getContenido() + "' @ " + m.getTimestamp());
        }
    }
}

// ✅ Uso: Capturar y restaurar sin violación de encapsulación
Documento doc = new Documento();
HistorialDocumento historial = new HistorialDocumento(doc);

doc.escribir("Hola");
historial.guardarEstado();

doc.escribir(" mundo");
historial.guardarEstado();

doc.escribir("!");
historial.guardarEstado();

System.out.println("Estado actual: " + doc.getContenido());  // Hola mundo!

historial.deshacer();
System.out.println("Después de deshacer: " + doc.getContenido()); // Hola mundo
```

---

## Diagrama UML

```
  ┌──────────────────┐
  │    Documento     │ Originador
  ├──────────────────┤
  │- contenido       │
  │+ escribir()      │
  │+ crearMemento()  │◄─────────┐
  │+ restaurar()     │◄──────┐  │
  └──────────────────┘       │  │
                             │  │
  ┌──────────────────────┐   │  │
  │ MementoDocumento     │   │  │
  ├──────────────────────┤   │  │
  │- contenido (final)   │   │  │
  │- timestamp (final)   │   │  │
  │+ getContenido()      │───┘  │
  └──────────────────────┘      │
                                │
  ┌──────────────────────┐      │
  │ HistorialDocumento   │──────┘
  ├──────────────────────┤
  │- historial: List     │
  │+ guardarEstado()     │
  │+ deshacer()          │
  └──────────────────────┘
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Encapsulación**: Estado capturado sin violarla
- **Flexibilidad**: Múltiples puntos de restauración
- **Historial**: Fácil implementar deshacer
- **Seguridad**: Mementos inmutables

### ❌ Desventajas

- **Memoria**: Guardar muchos estados consume memoria
- **Performance**: Copiar estado puede ser lento
- **Clases**: Más clases Memento
- **Complejidad**: Conceptualmente más difícil

---

## Cuándo Usarlo

✅ **Usa cuando:**
- Necesitas capturar estado para deshacer
- Necesitas guardar puntos de guardado
- Snapshots periódicos son útiles
- Ejemplos: Editores, videojuegos, transacciones BD

---

## Ejercicio

```{exercise}
:label: ej-memento-configuración

Crea sistema de configuración con Memento:
1. `Configuración` con múltiples parámetros
2. Guardar/restaurar estados
```

```{solution} ej-memento-configuración
:class: dropdown

```java
public class MementoConfig {
    private final Map<String, String> valores;
    
    public MementoConfig(Map<String, String> valores) {
        this.valores = new HashMap<>(valores);
    }
    
    public Map<String, String> getValores() {
        return new HashMap<>(valores);
    }
}

public class Configuración {
    private Map<String, String> valores = new HashMap<>();
    
    public void set(String clave, String valor) {
        valores.put(clave, valor);
    }
    
    public String get(String clave) {
        return valores.get(clave);
    }
    
    public MementoConfig crearMemento() {
        return new MementoConfig(valores);
    }
    
    public void restaurar(MementoConfig memento) {
        valores = memento.getValores();
    }
}

// Uso
Configuración config = new Configuración();
config.set("idioma", "es");
config.set("tema", "oscuro");
MementoConfig snapshot1 = config.crearMemento();

config.set("tema", "claro");
config.set("tamaño_fuente", "14");

config.restaurar(snapshot1);
System.out.println(config.get("tema")); // oscuro
```
```
