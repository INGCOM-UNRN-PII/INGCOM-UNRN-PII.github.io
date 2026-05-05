---
title: "Mediator"
subtitle: "Centralizar comunicación entre objetos"
subject: Patrones de Diseño de Comportamiento
---

(patron-mediator)=
# Mediator: Comunicación Centralizada

El patrón **Mediator** define un objeto que encapsula cómo una serie de objetos interactúan, promoviendo débil acoplamiento al evitar que los objetos se refieran explícitamente entre sí.

:::{note} Propósito

Centralizar comunicación entre múltiples objetos para reducir acoplamiento.
:::

---

## Origen e Historia

Gang of Four 1994. Surge de sistemas complejos donde objetos interactúan de forma complicada (ej: controladores MVC).

## Motivación

Necesario cuando:
- Múltiples objetos comunicándose de forma compleja
- Relaciones de muchos-a-muchos
- Necesitas reutilizar objetos en contextos diferentes
- Cambios de interacciones frecuentes

## Contexto

**Patrón:** Colegas → Mediador ← Colegas

**Anatomía:**
- **Mediator**: Define interfaz de interacción
- **ConcreteMediator**: Implementa coordinación
- **Colleague**: Conoce al Mediador pero no a otros colegas
- Todos los mensajes van al Mediador

**Ventaja:** N objetos acoplados → centralizados en Mediador

---

## Problema

**Sin Mediator:** Objetos acoplados directamente
```
Colega1 ←→ Colega2
  ↕        ↕
Colega3 ←→ Colega4
```

**Con Mediator:** Comunicación centralizada
```
Colega1  Colega2
   ↑        ↑
   └─→ Mediator ←─┘
   ┌─────────────┐
   ↓             ↓
Colega3  Colega4
```

---

## Problema

```java
// ❌ Acoplamiento directo: cada botón conoce al otro
class UsuarioUI {
    private CampoTexto usuario;
    private CampoTexto contraseña;
    private Botón aceptar;
    private Botón cancelar;
    
    void inicializar() {
        aceptar.setOnClick(() -> {
            if (usuario.esValido() && contraseña.esValido()) {
                cancelar.desactivar();
                aceptar.desactivar();
            }
        });
    }
}
```

---

## Solución: Mediator

```java
/**
 * Colega: componente de la UI.
 */
public abstract class DialogoComponent {
    protected DialogoMediator mediador;
    
    public void setMediator(DialogoMediator mediador) {
        this.mediador = mediador;
    }
    
    abstract boolean esValido();
}

/**
 * Campo de texto.
 */
public class CampoTexto extends DialogoComponent {
    private String texto = "";
    
    public void setText(String texto) {
        this.texto = texto;
        mediador.componenteCambió(this);  // Notificar al mediador
    }
    
    public String getText() {
        return texto;
    }
    
    @Override
    public boolean esValido() {
        return !texto.isEmpty();
    }
}

/**
 * Botón.
 */
public class Botón extends DialogoComponent {
    private boolean estaActivo = true;
    
    public void setActivo(boolean activo) {
        this.estaActivo = activo;
    }
    
    public boolean estaActivo() {
        return estaActivo;
    }
    
    @Override
    public boolean esValido() {
        return true;
    }
    
    public void click() {
        System.out.println("Botón clickeado");
        mediador.botónPresionado(this);
    }
}

/**
 * Mediador: coordina interacciones.
 */
public abstract class DialogoMediator {
    abstract void componenteCambió(DialogoComponent component);
    abstract void botónPresionado(Botón botón);
}

/**
 * Mediador concreto: Diálogo de login.
 */
public class DialogoLogin extends DialogoMediator {
    private CampoTexto usuario;
    private CampoTexto contraseña;
    private Botón aceptar;
    private Botón cancelar;
    
    public DialogoLogin() {
        usuario = new CampoTexto();
        contraseña = new CampoTexto();
        aceptar = new Botón();
        cancelar = new Botón();
        
        usuario.setMediator(this);
        contraseña.setMediator(this);
        aceptar.setMediator(this);
        cancelar.setMediator(this);
    }
    
    @Override
    public void componenteCambió(DialogoComponent component) {
        // Cuando cambia un campo, actualizar estado de botones
        if (component == usuario || component == contraseña) {
            aceptar.setActivo(usuario.esValido() && contraseña.esValido());
        }
    }
    
    @Override
    public void botónPresionado(Botón botón) {
        if (botón == aceptar) {
            System.out.println("✅ Login con: " + usuario.getText());
        } else if (botón == cancelar) {
            System.out.println("❌ Cancelado");
        }
    }
}

// ✅ Uso: Los componentes no se conocen entre sí
DialogoLogin dialogo = new DialogoLogin();
dialogo.usuario.setText("admin");  // Notifica mediador → actualiza botones
dialogo.contraseña.setText("1234");
dialogo.aceptar.click();
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Desacoplamiento**: Colegas no conocen entre sí
- **Centralización**: Lógica de interacción en un lugar
- **Reutilización**: Colegas reutilizables
- **Mantenibilidad**: Cambios en interacciones sin modificar colegas

### ❌ Desventajas

- **Dios Mediador**: El mediador puede crecer demasiado
- **Complejidad**: Más difícil de entender
- **Testing**: Difícil testear mediador y colegas
- **Performance**: Capas adicionales de indirección

---

## Cuándo Usarlo

✅ **Usa cuando:**
- Múltiples objetos comunicándose de forma compleja
- Necesitas evitar referencias directas
- Necesitas reutilizar objetos en contextos diferentes
- Ejemplos: Diálogos complejos, controladores MVC

---

## Ejercicio

```{exercise}
:label: ej-mediator-chat

Crea sala de chat con Mediator:
1. `Colega`: Usuario
2. `Mediador`: SalaChat que distribuye mensajes
```

```{solution} ej-mediator-chat
:class: dropdown

```java
public abstract class Usuario {
    protected SalaChat sala;
    protected String nombre;
    
    public Usuario(String nombre) {
        this.nombre = nombre;
    }
    
    public void setSala(SalaChat sala) {
        this.sala = sala;
    }
    
    public void enviar(String mensaje) {
        sala.distribuir(nombre, mensaje);
    }
    
    public abstract void recibir(String de, String mensaje);
}

public class UsuarioConcreto extends Usuario {
    @Override
    public void recibir(String de, String mensaje) {
        System.out.println(nombre + " recibió de " + de + ": " + mensaje);
    }
}

public class SalaChat {
    private List<Usuario> usuarios = new ArrayList<>();
    
    public void registrar(Usuario usuario) {
        usuarios.add(usuario);
        usuario.setSala(this);
    }
    
    public void distribuir(String de, String mensaje) {
        for (Usuario usuario : usuarios) {
            if (!usuario.nombre.equals(de)) {
                usuario.recibir(de, mensaje);
            }
        }
    }
}

// Uso
SalaChat sala = new SalaChat();
Usuario alice = new UsuarioConcreto("Alice");
Usuario bob = new UsuarioConcreto("Bob");
sala.registrar(alice);
sala.registrar(bob);

alice.enviar("Hola Bob!");
bob.enviar("Hola Alice!");
```
```
