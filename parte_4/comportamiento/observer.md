---
title: "Patrón Observer"
subtitle: "Notificar múltiples objetos sobre cambios"
subject: Patrones de Diseño de Comportamiento
---

(patron-observer)=
# Observer: Notificación Automática

El patrón **Observer** define una relación de uno-a-muchos entre objetos de tal forma que cuando uno cambia su estado, todos los demás son notificados automáticamente.

:::{admonition} Propósito
:class: note

Notificar automáticamente a múltiples objetos sobre cambios.
:::

---

## Origen e Historia

Gang of Four 1994. Surge de sistemas de eventos: necesidad de que múltiples objetos reaccionen a cambios sin acoplamiento directo.

## Motivación

Necesario cuando:
- Cambio en un objeto afecta múltiples
- No sabes cuántos observadores habrá
- Quieres desacoplar publicador de suscriptores
- Reacciones dinámicas (agregar/remover observadores)

## Contexto

**Patrón:** Subject (Observable) → Notifica → Observadores

**Anatomía:**
- **Subject**: Mantiene lista de observadores
- **Observer**: Interfaz de actualización
- **ConcreteObserver**: Reacciona a cambios
- Modelo publicador-suscriptor

**Variantes:**
- Observer con eventos
- Weak references (para no mantener vivos)
- Filtros (observador solo si condición)

---

## Problema

```
Sujeto (Subject)
    ↓ notifica cuando cambia
    └→ Observer1, Observer2, Observer3
```

---

## Problema

```java
// ❌ Sin Observer: Acoplamiento directo
class Temperatura {
    private double valor;
    private Pantalla pantalla;
    private Alarma alarma;
    private Registrador registrador;
    
    public void setValor(double v) {
        valor = v;
        pantalla.actualizar(valor);
        alarma.chequear(valor);
        registrador.registrar(valor);
        // Cada nuevo observador requiere modificar esta clase!
    }
}
```

---

## Solución: Observer

```java
/**
 * Interfaz Observer.
 */
public interface Observador {
    void actualizar(double temperatura);
}

/**
 * Sujeto: la cosa observada.
 */
public class Temperatura {
    private double valor = 0;
    private List<Observador> observadores = new ArrayList<>();
    
    public void registrar(Observador obs) {
        observadores.add(obs);
    }
    
    public void desregistrar(Observador obs) {
        observadores.remove(obs);
    }
    
    /**
     * Notificar a todos los observadores.
     */
    private void notificar() {
        for (Observador obs : observadores) {
            obs.actualizar(valor);
        }
    }
    
    public void setValor(double v) {
        if (v != valor) {
            valor = v;
            notificar();  // Notificar automáticamente
        }
    }
    
    public double getValor() {
        return valor;
    }
}

/**
 * Observador concreto: Pantalla.
 */
public class Pantalla implements Observador {
    private String nombre;
    
    public Pantalla(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public void actualizar(double temperatura) {
        System.out.println("📺 " + nombre + ": " + temperatura + "°C");
    }
}

/**
 * Observador concreto: Alarma.
 */
public class Alarma implements Observador {
    private double umbral;
    
    public Alarma(double umbral) {
        this.umbral = umbral;
    }
    
    @Override
    public void actualizar(double temperatura) {
        if (temperatura > umbral) {
            System.out.println("🚨 ¡ALARMA! Temperatura demasiada alta: " + temperatura);
        }
    }
}

/**
 * Observador concreto: Registrador.
 */
public class Registrador implements Observador {
    @Override
    public void actualizar(double temperatura) {
        System.out.println("📝 Log: Temperatura registrada = " + temperatura);
    }
}

// ✅ Uso: Agregar observadores sin modificar Temperatura
Temperatura temp = new Temperatura();

// Registrar observadores
Pantalla pantalla = new Pantalla("Sala");
Alarma alarma = new Alarma(30.0);
Registrador registro = new Registrador();

temp.registrar(pantalla);
temp.registrar(alarma);
temp.registrar(registro);

// Cambiar temperatura → automáticamente notifica a todos
temp.setValor(25.0);  // Todos se actualizan
temp.setValor(32.0);  // Alarma suena
```

---

## Diagrama UML

```
     ┌────────────────────┐
     │   Observador       │
     │  <<interface>>     │
     ├────────────────────┤
     │+ actualizar()      │
     └────────────────────┘
                ▲
                │ implementa
                │
      ┌─────────┼─────────┐
      │         │         │
  ┌───▼──┐ ┌───▼──┐  ┌──▼───┐
  │Pantalla│ │Alarma│ │Registrador│
  └────────┘ └──────┘  └──────┘
       ▲         ▲         ▲
       │ registra│         │
       └─────────┼─────────┘
                 │
         ┌───────▼────────┐
         │  Temperatura   │ Sujeto
         ├────────────────┤
         │- observadores  │
         │+ registrar()   │
         │+ setValor()    │
         │- notificar()   │
         └────────────────┘
```

---

## Variantes

**1. Observer con eventos:**
```java
public class Evento {
    public Object origen;
    public String tipo;
    public Object datos;
}

public interface Observador {
    void actualizar(Evento evento);
}
```

**2. Subject con getters:**
```java
public interface SujetoObservable {
    void registrar(Observador obs);
    void desregistrar(Observador obs);
    Object getEstado();
}
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Desacoplamiento**: Sujeto y observadores desacoplados
- **Dinámico**: Agregar/remover observadores en runtime
- **Reutilización**: Observadores reutilizables
- **Broadcasting**: Notificación a múltiples sin conocerlos

### ❌ Desventajas

- **Orden impredecible**: No se garantiza orden de notificación
- **Memory leaks**: Olvidar desregistrar causa problemas
- **Performance**: Muchos observadores puede ser lento
- **Debugging**: Difícil rastrear flujo

---

## Cuándo Usarlo

✅ **Usa cuando:**
- Cambio en un objeto afecta múltiples
- No conoces número de observadores
- Necesitas desacoplamiento
- Ejemplos: Eventos GUI, MVC, publicador-suscriptor

---

## Ejercicio

```{exercise}
:label: ej-observer-acciones

Crea monitor de acciones bursátiles:
1. `Acción` (sujeto) con precio
2. Observadores: `Inversor`, `Analista`
3. Notificar cuando precio cambia
```

```{solution} ej-observer-acciones
:class: dropdown

```java
public interface ObservadorAcción {
    void precioActualizado(String símbolo, double precio);
}

public class Acción {
    private String símbolo;
    private double precio;
    private List<ObservadorAcción> observadores = new ArrayList<>();
    
    public Acción(String símbolo, double precio) {
        this.símbolo = símbolo;
        this.precio = precio;
    }
    
    public void registrar(ObservadorAcción obs) {
        observadores.add(obs);
    }
    
    public void setPrecio(double nuevoPrecio) {
        if (nuevoPrecio != precio) {
            precio = nuevoPrecio;
            notificar();
        }
    }
    
    private void notificar() {
        for (ObservadorAcción obs : observadores) {
            obs.precioActualizado(símbolo, precio);
        }
    }
}

public class Inversor implements ObservadorAcción {
    private String nombre;
    
    public Inversor(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public void precioActualizado(String símbolo, double precio) {
        System.out.println("💰 " + nombre + " ve: " + símbolo + " = $" + precio);
    }
}

public class Analista implements ObservadorAcción {
    @Override
    public void precioActualizado(String símbolo, double precio) {
        if (precio > 100) {
            System.out.println("📊 Analista: " + símbolo + " está caro!");
        } else if (precio < 50) {
            System.out.println("📊 Analista: " + símbolo + " está barato!");
        }
    }
}

// Uso
Acción apple = new Acción("AAPL", 150.0);
Inversor juan = new Inversor("Juan");
Analista ana = new Analista();

apple.registrar(juan);
apple.registrar(ana);

apple.setPrecio(155.0); // Notifica a ambos
apple.setPrecio(45.0);  // Notifica a ambos
```
```
