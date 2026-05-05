---
title: "Patrón State"
subtitle: "Cambiar comportamiento según estado"
subject: Patrones de Diseño de Comportamiento
---

(patron-state)=
# State: Comportamiento Dependiente del Estado

El patrón **State** permite que un objeto altere su comportamiento cuando su estado interno cambia, aparentando cambiar su clase.

:::{admonition} Propósito
:class: note

Permitir que un objeto cambie comportamiento según su estado.
:::

---

## Origen e Historia

Gang of Four 1994. Surge de máquinas de estado complejas: necesidad de separar lógica de estado del contexto.

## Motivación

Necesario cuando:
- Comportamiento depende del estado
- Condicionales sobre estado son complejos
- Estado cambia frecuentemente
- Cada estado tiene responsabilidades únicas

## Contexto

**Patrón:** Contexto delega a objeto State

**Anatomía:**
- **State**: Interfaz (operaciones)
- **ConcreteState**: Implementa comportamiento
- **Context**: Mantiene referencia al estado actual
- Cambios de estado son transiciones

**Variantes:**
- Estados con historial
- Transiciones condicionales
- Estados paralelos

---

## Problema

```
Sin State: Condicionales complejos
if (estado == PAUSADO)
  ...
else if (estado == REPRODUCIENDO)
  ...
else if (estado == DETENIDO)
  ...

Con State: Objetos que representan estados
reproducir() → delegado al estado actual
```

---

## Problema

```java
// ❌ Condicionales esparcidos
class ReproductorMúsica {
    enum Estado { PAUSADO, REPRODUCIENDO, DETENIDO }
    private Estado estado = Estado.DETENIDO;
    
    void reproducir() {
        if (estado == DETENIDO) {
            estado = REPRODUCIENDO;
            System.out.println("Reproduciendo...");
        } else if (estado == PAUSADO) {
            estado = REPRODUCIENDO;
        }
        // Cada acción requiere condicionales
    }
}
```

---

## Solución: State

```java
/**
 * Estado: interfaz para cada estado.
 */
public interface EstadoReproductor {
    void reproducir(ReproductorMúsica reproductor);
    void pausar(ReproductorMúsica reproductor);
    void detener(ReproductorMúsica reproductor);
}

/**
 * Estado concreto: Detenido.
 */
public class EstadoDetenido implements EstadoReproductor {
    @Override
    public void reproducir(ReproductorMúsica reproductor) {
        System.out.println("▶️ Reproduciendo desde el inicio");
        reproductor.setState(new EstadoReproduciendo());
    }
    
    @Override
    public void pausar(ReproductorMúsica reproductor) {
        System.out.println("⏸️ Ya está detenido");
    }
    
    @Override
    public void detener(ReproductorMúsica reproductor) {
        System.out.println("⏹️ Ya está detenido");
    }
}

/**
 * Estado concreto: Reproduciendo.
 */
public class EstadoReproduciendo implements EstadoReproductor {
    @Override
    public void reproducir(ReproductorMúsica reproductor) {
        System.out.println("▶️ Ya está reproduciendo");
    }
    
    @Override
    public void pausar(ReproductorMúsica reproductor) {
        System.out.println("⏸️ Pausado");
        reproductor.setState(new EstadoPausado());
    }
    
    @Override
    public void detener(ReproductorMúsica reproductor) {
        System.out.println("⏹️ Detenido");
        reproductor.setState(new EstadoDetenido());
    }
}

/**
 * Estado concreto: Pausado.
 */
public class EstadoPausado implements EstadoReproductor {
    @Override
    public void reproducir(ReproductorMúsica reproductor) {
        System.out.println("▶️ Reanudando");
        reproductor.setState(new EstadoReproduciendo());
    }
    
    @Override
    public void pausar(ReproductorMúsica reproductor) {
        System.out.println("⏸️ Ya está pausado");
    }
    
    @Override
    public void detener(ReproductorMúsica reproductor) {
        System.out.println("⏹️ Detenido");
        reproductor.setState(new EstadoDetenido());
    }
}

/**
 * Contexto: objeto que cambia comportamiento.
 */
public class ReproductorMúsica {
    private EstadoReproductor estado = new EstadoDetenido();
    
    public void setState(EstadoReproductor nuevoEstado) {
        this.estado = nuevoEstado;
    }
    
    public void reproducir() {
        estado.reproducir(this);
    }
    
    public void pausar() {
        estado.pausar(this);
    }
    
    public void detener() {
        estado.detener(this);
    }
}

// ✅ Uso: Sin condicionales
ReproductorMúsica reproductor = new ReproductorMúsica();
reproductor.reproducir();   // ▶️ Reproduciendo
reproductor.pausar();       // ⏸️ Pausado
reproductor.reproducir();   // ▶️ Reanudando
reproductor.detener();      // ⏹️ Detenido
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Claridad**: Cada estado es una clase clara
- **Localización**: Lógica del estado en un lugar
- **Extensibilidad**: Agregar estados sin modificar existentes
- **Testabilidad**: Testear cada estado independientemente

### ❌ Desventajas

- **Clases**: Muchas clases de estado
- **Overhead**: Cambios de estado frecuentes pueden ser costosos
- **Complejidad**: Más difícil que condicionales simples
- **Memory**: Múltiples instancias de estados

---

## Cuándo Usarlo

✅ **Usa cuando:**
- Comportamiento depende del estado
- Condicionales complejos sobre estado
- Estado cambia frecuentemente
- Ejemplos: Máquinas de estado, reproductores, máquinas expendedoras

---

## Ejercicio

```{exercise}
:label: ej-state-pedido

Crea sistema de pedidos con State:
1. Estados: Pendiente, Procesando, Enviado, Entregado
2. Transiciones válidas entre estados
```

```{solution} ej-state-pedido
:class: dropdown

```java
public interface EstadoPedido {
    void procesar(Pedido pedido);
    void enviar(Pedido pedido);
    void entregar(Pedido pedido);
}

public class EstadoPendiente implements EstadoPedido {
    @Override
    public void procesar(Pedido pedido) {
        pedido.setEstado(new EstadoProcesando());
    }
    
    @Override
    public void enviar(Pedido pedido) {
        System.out.println("No se puede enviar, aún no procesado");
    }
    
    @Override
    public void entregar(Pedido pedido) {
        System.out.println("No se puede entregar");
    }
}

public class EstadoProcesando implements EstadoPedido {
    @Override
    public void procesar(Pedido pedido) {
        System.out.println("Ya está procesando");
    }
    
    @Override
    public void enviar(Pedido pedido) {
        pedido.setEstado(new EstadoEnviado());
    }
    
    @Override
    public void entregar(Pedido pedido) {
        System.out.println("Aún no enviado");
    }
}

public class EstadoEnviado implements EstadoPedido {
    @Override
    public void procesar(Pedido pedido) {
        System.out.println("Ya está enviado");
    }
    
    @Override
    public void enviar(Pedido pedido) {
        System.out.println("Ya está enviado");
    }
    
    @Override
    public void entregar(Pedido pedido) {
        pedido.setEstado(new EstadoEntregado());
    }
}

public class EstadoEntregado implements EstadoPedido {
    @Override
    public void procesar(Pedido pedido) { }
    @Override
    public void enviar(Pedido pedido) { }
    @Override
    public void entregar(Pedido pedido) {
        System.out.println("Ya entregado");
    }
}

public class Pedido {
    private EstadoPedido estado = new EstadoPendiente();
    
    void setEstado(EstadoPedido e) { estado = e; }
    void procesar() { estado.procesar(this); }
    void enviar() { estado.enviar(this); }
    void entregar() { estado.entregar(this); }
}
```
```
