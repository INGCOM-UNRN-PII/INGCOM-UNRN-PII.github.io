---
title: "Patrón Factory Method"
subtitle: "Crear objetos sin especificar clases concretas"
subject: Patrones de Diseño Creacionales
---

(patron-factory-method)=
# Factory Method: Creación Flexible

El patrón **Factory Method** define una interfaz para crear objetos, permitiendo que las subclases decidan qué clase instanciar. Desacopla la creación de objetos del código cliente.

:::{note} Propósito

Crear objetos sin que el cliente necesite conocer las clases concretas exactas.
:::

---

## Problema

```java
// ❌ Sin Factory: acoplamiento a clases concretas
public class Aplicacion {
    public void crearDocumento(String tipo) {
        Documento doc;
        
        if (tipo.equals("PDF")) {
            doc = new DocumentoPDF();
        } else if (tipo.equals("WORD")) {
            doc = new DocumentoWORD();
        } else if (tipo.equals("EXCEL")) {
            doc = new DocumentoEXCEL();
        }
        
        doc.abrir();
        doc.editar();
    }
}
// Problema: agregación de tipos requiere modificar código
```

---

## Solución: Factory Method

### Estructura Básica

```java
/**
 * Clase abstracta (o interfaz) que define el factory method.
 */
public abstract class Aplicacion {
    protected abstract Documento crearDocumento();
    
    public void abrirDocumento() {
        Documento doc = crearDocumento();  // Factory Method
        doc.abrir();
        doc.editar();
    }
}

/**
 * Interfaz para productos.
 */
public interface Documento {
    void abrir();
    void editar();
    void guardar();
}

/**
 * Implementaciones concretas.
 */
public class DocumentoPDF implements Documento {
    @Override
    public void abrir() {
        System.out.println("Abriendo PDF...");
    }
    
    @Override
    public void editar() {
        System.out.println("Editando PDF...");
    }
    
    @Override
    public void guardar() {
        System.out.println("Guardando como PDF...");
    }
}

public class DocumentoWORD implements Documento {
    @Override
    public void abrir() {
        System.out.println("Abriendo WORD...");
    }
    
    @Override
    public void editar() {
        System.out.println("Editando WORD...");
    }
    
    @Override
    public void guardar() {
        System.out.println("Guardando como WORD...");
    }
}

/**
 * Subclase que implementa el factory method.
 */
public class AplicacionPDF extends Aplicacion {
    @Override
    protected Documento crearDocumento() {
        return new DocumentoPDF();
    }
}

public class AplicacionWORD extends Aplicacion {
    @Override
    protected Documento crearDocumento() {
        return new DocumentoWORD();
    }
}

// Uso:
Aplicacion app = new AplicacionPDF();
app.abrirDocumento();  // Crea DocumentoPDF sin acoplamiento
```

---

## Variantes

### Factory Method Paramétrico

```java
/**
 * Factory method que acepta parámetro para decidir tipo.
 */
public abstract class TransporteFactory {
    public abstract Transporte crearTransporte();
    
    public void entregar() {
        Transporte t = crearTransporte();
        t.cargar();
        t.entregar();
    }
}

public interface Transporte {
    void cargar();
    void entregar();
}

/**
 * Factory method con parámetro en la clase base.
 */
public class LogisticaFactory {
    public Transporte crearTransporte(String tipo) {
        switch (tipo) {
            case "CAMION":
                return new Camion();
            case "BARCO":
                return new Barco();
            case "AVION":
                return new Avion();
            default:
                throw new IllegalArgumentException("Tipo desconocido: " + tipo);
        }
    }
}

class Camion implements Transporte {
    @Override
    public void cargar() { System.out.println("Cargando en camión..."); }
    
    @Override
    public void entregar() { System.out.println("Entregando por camión..."); }
}

// Uso:
LogisticaFactory factory = new LogisticaFactory();
Transporte transporte = factory.crearTransporte("CAMION");
transporte.cargar();
transporte.entregar();
```

### Factory Estática Simple

Para casos simples sin necesidad de subclases:

```java
public class ConexionFactory {
    public static Conexion crearConexion(String tipo) {
        switch (tipo) {
            case "MYSQL":
                return new ConexionMySQL();
            case "POSTGRESQL":
                return new ConexionPostgreSQL();
            case "SQLITE":
                return new ConexionSQLite();
            default:
                throw new IllegalArgumentException("BD desconocida");
        }
    }
}

interface Conexion {
    void conectar();
    void desconectar();
}

class ConexionMySQL implements Conexion {
    @Override
    public void conectar() { System.out.println("MySQL conectada"); }
    
    @Override
    public void desconectar() { System.out.println("MySQL desconectada"); }
}

// Uso:
Conexion conn = ConexionFactory.crearConexion("MYSQL");
conn.conectar();
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Desacoplamiento**: No dependes de clases concretas
- **Flexibilidad**: Agregar nuevos tipos solo requiere nuevas subclases
- **Responsabilidad única**: Creación separada del uso
- **Mantenibilidad**: Cambios centralizados en el factory

### ❌ Desventajas

- **Complejidad**: Introduce más clases que una creación directa
- **Jerarquía de clases**: Requiere heredar para cambiar creación
- **Overhead**: Pequeño costo de abstracción

---

## Cuándo Usarlo

✅ **Usa Factory Method cuando:**
- No sabes en tiempo de compilación qué tipos necesitarás
- Tipos dependen de configuración externa o usuario
- Quieres agregar nuevos tipos fácilmente sin modificar código existente

❌ **Evita cuando:**
- Solo hay un tipo de objeto
- La creación es simple y no va a cambiar


