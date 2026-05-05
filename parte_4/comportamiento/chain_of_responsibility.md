---
title: "Patrón Chain of Responsibility"
subtitle: "Pasar una solicitud por una cadena de controladores"
subject: Patrones de Diseño de Comportamiento
---

(patron-chain-of-responsibility)=
# Chain of Responsibility: Procesamiento Encadenado

El patrón **Chain of Responsibility** evita acoplar el remitente de una solicitud con su receptor permitiendo que múltiples objetos tengan la oportunidad de procesar la solicitud. Encadena los receptores y pasa la solicitud por la cadena hasta que un objeto la procesa.

:::{admonition} Propósito
:class: note

Permitir que múltiples objetos procesen una solicitud, pasándola por una cadena.
:::

---

## Origen e Historia

Gang of Four 1994. Surge del reconocimiento de que algunos sistemas necesitan una cadena de responsabilidad implícita (handlers) en lugar de a quién enviar directamente.

## Motivación

Necesario cuando:
- Múltiples objetos podrían procesar solicitud
- No sabes quién lo hará en compile time
- Quieres agregar nuevos handlers sin cambiar código
- Desacoplar remitente de receptores

## Contexto

**Patrón:** Solicitud → Handler1 → Handler2 → Handler3

**Anatomía:**
- **Handler**: Interfaz (procesar o pasar siguiente)
- **ConcreteHandler**: Implementa o pasa
- **chain**: Cada handler conoce al siguiente
- Cada manejador es responsabilidad única

**Ejemplo:** Sistema de tickets por prioridad, validación en cascada

---

## Problema

Cada manejador en la cadena puede:
1. Procesar la solicitud → **Fin**
2. Pasar al siguiente → **Continúa**

```
Solicitud → [Manejador1] → [Manejador2] → [Manejador3]
              (¿yo?)        (¿yo?)        (yo!)
```

---

## Problema

```java
// ❌ Sin Chain: condiciones anidadas
class ProcesoSolicitud {
    void procesar(SolicitudAyuda solicitud) {
        if (solicitud.getPrioridad() == 1) {
            // Despacho administrativo
            System.out.println("Administrativo resuelve");
        } else if (solicitud.getPrioridad() == 2) {
            // Supervisor
            System.out.println("Supervisor resuelve");
        } else if (solicitud.getPrioridad() == 3) {
            // Gerente
            System.out.println("Gerente resuelve");
        } else {
            System.out.println("No se pudo resolver");
        }
    }
}
```

---

## Solución: Chain of Responsibility

```java
/**
 * Solicitud a procesar.
 */
public class SolicitudAyuda {
    private String descripción;
    private int prioridad;  // 1=baja, 2=media, 3=alta, 4=urgente
    
    public SolicitudAyuda(String desc, int prior) {
        this.descripción = desc;
        this.prioridad = prior;
    }
    
    public String getDescripción() { return descripción; }
    public int getPrioridad() { return prioridad; }
}

/**
 * Manejador base: cada manejador conoce al siguiente.
 */
public abstract class ManejadorSolicitud {
    protected ManejadorSolicitud siguiente;
    
    public void setSiguiente(ManejadorSolicitud sig) {
        this.siguiente = sig;
    }
    
    public final void procesar(SolicitudAyuda solicitud) {
        if (puedeProcesar(solicitud)) {
            manejar(solicitud);
        } else if (siguiente != null) {
            siguiente.procesar(solicitud);
        } else {
            System.out.println("❌ Nadie pudo procesar la solicitud");
        }
    }
    
    protected abstract boolean puedeProcesar(SolicitudAyuda solicitud);
    protected abstract void manejar(SolicitudAyuda solicitud);
}

/**
 * Manejador concreto: Staff administrativo.
 */
public class ManejadorAdministrativo extends ManejadorSolicitud {
    @Override
    protected boolean puedeProcesar(SolicitudAyuda solicitud) {
        return solicitud.getPrioridad() <= 1;
    }
    
    @Override
    protected void manejar(SolicitudAyuda solicitud) {
        System.out.println("✅ [Administrativo] Procesando: " + solicitud.getDescripción());
    }
}

/**
 * Manejador concreto: Supervisor.
 */
public class ManejadorSupervisor extends ManejadorSolicitud {
    @Override
    protected boolean puedeProcesar(SolicitudAyuda solicitud) {
        return solicitud.getPrioridad() <= 2;
    }
    
    @Override
    protected void manejar(SolicitudAyuda solicitud) {
        System.out.println("✅ [Supervisor] Procesando: " + solicitud.getDescripción());
    }
}

/**
 * Manejador concreto: Gerente.
 */
public class ManejadorGerente extends ManejadorSolicitud {
    @Override
    protected boolean puedeProcesar(SolicitudAyuda solicitud) {
        return solicitud.getPrioridad() <= 3;
    }
    
    @Override
    protected void manejar(SolicitudAyuda solicitud) {
        System.out.println("✅ [Gerente] Procesando: " + solicitud.getDescripción());
    }
}

/**
 * Manejador concreto: Director (última instancia).
 */
public class ManejadorDirector extends ManejadorSolicitud {
    @Override
    protected boolean puedeProcesar(SolicitudAyuda solicitud) {
        return true;  // Siempre puede procesar
    }
    
    @Override
    protected void manejar(SolicitudAyuda solicitud) {
        System.out.println("✅ [Director] Resolviendo urgencia: " + solicitud.getDescripción());
    }
}

// ✅ Uso: Encadenar manejadores
ManejadorSolicitud admin = new ManejadorAdministrativo();
ManejadorSolicitud supervisor = new ManejadorSupervisor();
ManejadorSolicitud gerente = new ManejadorGerente();
ManejadorSolicitud director = new ManejadorDirector();

admin.setSiguiente(supervisor);
supervisor.setSiguiente(gerente);
gerente.setSiguiente(director);

// Procesar solicitudes de diferentes prioridades
admin.procesar(new SolicitudAyuda("Cambiar contraseña", 1));
admin.procesar(new SolicitudAyuda("Problema con acceso", 2));
admin.procesar(new SolicitudAyuda("Falla en sistema crítico", 4));
```

---

## Diagrama UML

```
     ┌─────────────────────────┐
     │ ManejadorSolicitud      │
     │    <<abstract>>          │
     ├─────────────────────────┤
     │- siguiente              │
     │+ setSiguiente()         │
     │+ procesar()             │
     │# puedeProcesar() [abs]  │
     │# manejar() [abs]        │
     └─────────────┬───────────┘
                   │
        ┌──────────┼──────────┬──────────┐
        │          │          │          │
   ┌────▼──┐ ┌────▼──┐ ┌────▼──┐ ┌────▼──┐
   │Admin  │→│Supervisor│→│Gerente│→│Director│
   └───────┘ └────────┘ └───────┘ └────────┘
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Desacoplamiento**: Remitente no conoce receptores
- **Flexibilidad**: Reordenar/agregar manejadores fácilmente
- **Responsabilidad única**: Cada manejador hace una cosa
- **Dinámico**: Construir cadena en runtime

### ❌ Desventajas

- **Debugging**: Difícil rastrear quién procesa
- **Garantía**: No garantiza que se procese la solicitud
- **Performance**: Recorrer cadena puede ser lento
- **Complejidad**: Más clases para casos simples

---

## Cuándo Usarlo

✅ **Usa cuando:**
- Múltiples objetos pueden procesar solicitud
- No conoces en compile time quién lo hará
- Necesitas desacoplar remitente de receptores
- Ejemplos: Sistemas de tickets, logging, validación

---

## Ejercicio

```{exercise}
:label: ej-cor-validacion

Crea cadena de validación para usuario:
1. Validar email
2. Validar contraseña
3. Verificar disponibilidad
4. Guardar en BD
```

```{solution} ej-cor-validacion
:class: dropdown

```java
public class Usuario {
    private String email;
    private String contraseña;
}

public abstract class ValidadorRegistro {
    protected ValidadorRegistro siguiente;
    
    public void setSiguiente(ValidadorRegistro sig) {
        this.siguiente = sig;
    }
    
    public final boolean validar(Usuario usuario) {
        if (esValido(usuario)) {
            if (siguiente != null) {
                return siguiente.validar(usuario);
            }
            return true;
        }
        return false;
    }
    
    protected abstract boolean esValido(Usuario usuario);
}

public class ValidadorEmail extends ValidadorRegistro {
    @Override
    protected boolean esValido(Usuario usuario) {
        if (usuario.getEmail().contains("@")) {
            System.out.println("✅ Email válido");
            return true;
        }
        System.out.println("❌ Email inválido");
        return false;
    }
}

public class ValidadorContraseña extends ValidadorRegistro {
    @Override
    protected boolean esValido(Usuario usuario) {
        if (usuario.getContraseña().length() >= 8) {
            System.out.println("✅ Contraseña válida");
            return true;
        }
        System.out.println("❌ Contraseña muy corta");
        return false;
    }
}

// Uso
ValidadorEmail ve = new ValidadorEmail();
ValidadorContraseña vc = new ValidadorContraseña();
ve.setSiguiente(vc);

Usuario usuario = new Usuario("test@mail.com", "pass1234");
if (ve.validar(usuario)) {
    System.out.println("✅ Usuario registrado");
}
```
```
