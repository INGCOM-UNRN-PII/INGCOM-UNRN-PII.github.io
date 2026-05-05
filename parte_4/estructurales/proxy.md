---
title: "Patrón Proxy"
subtitle: "Controlar acceso a otro objeto"
subject: Patrones de Diseño Estructurales
---

(patron-proxy)=
# Proxy: Control de Acceso

El patrón **Proxy** proporciona un sustituto (proxy) para otro objeto para controlar su acceso, permitiendo operaciones adicionales antes/después de acceder al objeto real.

:::{note} Propósito

Controlar acceso a un objeto proporcionando un sustituto.
:::

---

## Origen e Historia

Gang of Four 1994. Surge de sistemas distribuidos: necesidad de interceder antes de acceder a objetos remotos. Popularizado en RPC, ORM (Hibernate), y frameworks web.

## Motivación

Necesario cuando:
- Necesitas control sobre acceso a objeto (autorización)
- Objeto es costoso (remoto, BD, archivo grande)
- Necesitas ejecutar lógica pre/post-acceso
- Lazy loading: crear cuando se necesita
- Ejemplo: proxy remoto = RPC, proxy de caché = BD

## Contexto

**Patrón:** Cliente → Proxy → RealSubject

**Anatomía:**
- **Subject**: Interfaz común
- **RealSubject**: Objeto real (costoso/protegido)
- **Proxy**: Intercede, mantiene referencia a RealSubject
- Proxy y Real tienen misma interfaz

**Tipos:**
- Proxy virtual: lazy loading
- Proxy remoto: llamada a servicio remoto
- Proxy protector: autorización

---

## Problema

Proxy actúa como intermediario:

```
Cliente → Proxy → RealSubject

Proxy puede:
- Aplazar creación del objeto real (lazy loading)
- Controlar acceso (autorización)
- Registrar accesos (logging)
- Cachear resultados
```

---

## Problema

```java
// ❌ Sin Proxy: cliente directo a recurso costoso
class BaseDatos {
    public String query(String sql) {
        // Operación costosa: acceso a disco, red, etc
        System.out.println("Ejecutando: " + sql);
        return "Resultado";
    }
}

// Cada cliente puede ejecutar cualquier query
// Sin control, caché, o auditoria
BaseDatos db = new BaseDatos();
db.query("DELETE FROM usuarios"); // ¡Peligroso!
```

---

## Solución: Proxy

```java
/**
 * Sujeto real: operación costosa.
 */
public class BaseDatos {
    public String query(String sql) {
        System.out.println("[BD] Ejecutando: " + sql);
        // Simulación de operación costosa
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        return "Resultado de: " + sql;
    }
}

/**
 * Interfaz común.
 */
public interface AccesoBD {
    String query(String sql);
}

/**
 * Implementación real.
 */
public class AccesoBDReal implements AccesoBD {
    private BaseDatos bd = new BaseDatos();
    
    @Override
    public String query(String sql) {
        return bd.query(sql);
    }
}

/**
 * Proxy: añade autenticación, caché y logging.
 */
public class ProxyAccesoBD implements AccesoBD {
    private AccesoBDReal acceso;
    private Map<String, String> caché = new HashMap<>();
    private String usuarioActual;
    private boolean estaAutenticado = false;
    
    public ProxyAccesoBD(String usuario, String contraseña) {
        // Simulación de autenticación
        if ("admin".equals(usuario) && "123".equals(contraseña)) {
            this.usuarioActual = usuario;
            this.estaAutenticado = true;
            this.acceso = new AccesoBDReal();
            System.out.println("[PROXY] Usuario " + usuario + " autenticado");
        } else {
            System.out.println("[PROXY] ¡Acceso denegado!");
        }
    }
    
    @Override
    public String query(String sql) {
        if (!estaAutenticado) {
            throw new SecurityException("No autenticado");
        }
        
        // Verificar si es operación permitida
        if (sql.toUpperCase().contains("DELETE") || sql.toUpperCase().contains("DROP")) {
            throw new SecurityException("Operación no permitida: " + sql);
        }
        
        // Verificar caché
        if (caché.containsKey(sql)) {
            System.out.println("[PROXY] Retornando resultado en caché");
            return caché.get(sql);
        }
        
        // Registrar acceso
        System.out.println("[PROXY] Usuario " + usuarioActual + " ejecutando query");
        
        // Delegar al objeto real
        String resultado = acceso.query(sql);
        
        // Almacenar en caché
        caché.put(sql, resultado);
        
        return resultado;
    }
}

// ✅ Uso controlado
ProxyAccesoBD proxy = new ProxyAccesoBD("admin", "123");

// Exitoso
System.out.println(proxy.query("SELECT * FROM usuarios"));
System.out.println(proxy.query("SELECT * FROM usuarios")); // Del caché

// Rechazado
try {
    proxy.query("DELETE FROM usuarios"); // Bloqueado por seguridad
} catch (SecurityException e) {
    System.out.println("[ERROR] " + e.getMessage());
}
```

---

## Variantes de Proxy

**1. Proxy Virtual (Lazy Loading):**
```java
public class ProxyImagenVirtual implements Imagen {
    private String archivo;
    private ImagenReal imagenReal;
    
    public ProxyImagenVirtual(String archivo) {
        this.archivo = archivo;
    }
    
    @Override
    public void mostrar() {
        if (imagenReal == null) {
            System.out.println("[PROXY] Cargando imagen: " + archivo);
            imagenReal = new ImagenReal(archivo);
        }
        imagenReal.mostrar();
    }
}
```

**2. Proxy Remoto (RPC):**
```java
public class ProxyServidor implements ServicioRemoto {
    private String url;
    
    @Override
    public String llamada(String param) {
        // Simulación de llamada HTTP
        System.out.println("[PROXY] Conectando a " + url);
        // ... ejecutar RPC ...
        return "Respuesta del servidor";
    }
}
```

---

## Diagrama UML

```
         ┌──────────────────┐
         │   Sujeto         │
         │  <<interface>>   │
         ├──────────────────┤
         │+ operación()     │
         └────────┬─────────┘
                  │
         ┌────────┴────────┐
         │                 │
    ┌────▼────────┐  ┌────▼───────────┐
    │ SujetoReal  │  │     Proxy      │
    ├─────────────┤  ├────────────────┤
    │+ operación()│  │- sujetoReal    │
    └─────────────┘  │- caché         │
                     │+ operación()   │
                     └────────────────┘
```

---

## Comparación: Proxy vs. Decorator

| Aspecto | Proxy | Decorator |
|--------|-------|-----------|
| **Intención** | Controlar acceso | Agregar responsabilidades |
| **Creación** | Proxy crea real | Cliente inyecta componente |
| **Relación** | Uno-a-uno | Múltiple (stack) |
| **Interfaz** | Igual | Igual |
| **Propósito** | Protección | Extensión |

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Control**: Autorización, autenticación
- **Performance**: Caché, lazy loading
- **Auditoría**: Registrar accesos
- **Protección**: El cliente no accede directo

### ❌ Desventajas

- **Complejidad**: Indirección adicional
- **Performance**: Overhead del proxy
- **Confusión**: Similar a Decorator
- **Sincronización**: Thread safety en caché

---

## Cuándo Usarlo

✅ **Usa Proxy cuando:**
- Necesitas controlar acceso al objeto real
- Lazy loading es importante
- Auditoria o logging es necesario
- Ejemplos: Conexiones BD, APIs remotas, imágenes grandes

❌ **Evita cuando:**
- El objeto es simple y rápido
- El control de acceso es innecesario

