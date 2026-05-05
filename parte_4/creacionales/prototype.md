---
title: "Patrón Prototype"
subtitle: "Clonar objetos existentes"
subject: Patrones de Diseño Creacionales
---

(patron-prototype)=
# Prototype: Copiar Existentes

El patrón **Prototype** permite crear nuevos objetos clonando un objeto existente (prototipo) en lugar de crear desde cero.

:::{note} Propósito

Crear nuevas instancias copiando un objeto existente, útil cuando la construcción es costosa.
:::

---

## Problema

```java
// ❌ Sin Prototype: crear desde cero es costoso
public class ConfiguracionCompleja {
    private Map<String, String> datos;
    private List<Modulo> modulos;
    private Cache cache;
    
    public ConfiguracionCompleja() {
        // Inicialización costosa
        this.datos = cargarDatos();
        this.modulos = inicializarModulos();
        this.cache = crearCache();
    }
}

// Cada instancia requiere inicialización completa
ConfiguracionCompleja config1 = new ConfiguracionCompleja();  // Costoso
ConfiguracionCompleja config2 = new ConfiguracionCompleja();  // Costoso
```

---

## Solución: Prototype

### Implementación Básica

```java
/**
 * Interfaz Prototype.
 */
public interface Cloneable {
    Object clonar();
}

/**
 * Clase que implementa clonación.
 */
public class Documento implements Cloneable {
    private String titulo;
    private String contenido;
    private List<String> etiquetas;
    private Autor autor;
    
    public Documento(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
        this.etiquetas = new ArrayList<>();
    }
    
    // Constructor de copia
    private Documento(Documento original) {
        this.titulo = original.titulo;
        this.contenido = original.contenido;
        this.etiquetas = new ArrayList<>(original.etiquetas);
        this.autor = original.autor;
    }
    
    @Override
    public Documento clonar() {
        return new Documento(this);
    }
    
    public void agregarEtiqueta(String etiqueta) {
        etiquetas.add(etiqueta);
    }
    
    public void setAutor(Autor autor) {
        this.autor = autor;
    }
    
    @Override
    public String toString() {
        return "Documento{" +
            "titulo='" + titulo + '\'' +
            ", contenido='" + contenido + '\'' +
            ", etiquetas=" + etiquetas +
            ", autor=" + autor +
            '}';
    }
}

// Uso:
Documento original = new Documento("Reporte 2024", "Contenido importante");
original.agregarEtiqueta("importante");
original.agregarEtiqueta("confidencial");

// Clonar es mucho más rápido que crear desde cero
Documento copia = original.clonar();
copia.setAutor(new Autor("Juan"));

System.out.println("Original: " + original);
System.out.println("Copia: " + copia);
```

### Clonación Profunda vs Superficial

```java
public class ImagenConCapas implements Cloneable {
    private String nombre;
    private List<Capa> capas;
    
    public ImagenConCapas(String nombre) {
        this.nombre = nombre;
        this.capas = new ArrayList<>();
    }
    
    /**
     * Clonación superficial: comparte referencias.
     */
    @Override
    public ImagenConCapas clonar() {
        try {
            ImagenConCapas copia = (ImagenConCapas) super.clone();
            // ⚠️ capas todavía apunta a la misma lista
            return copia;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Clonación profunda: copia todo.
     */
    public ImagenConCapas clonarProfundo() {
        ImagenConCapas copia = new ImagenConCapas(this.nombre);
        
        // Copiar todas las capas
        for (Capa capa : this.capas) {
            copia.capas.add(capa.clonar());
        }
        
        return copia;
    }
    
    public void agregarCapa(Capa capa) {
        capas.add(capa);
    }
}

// Diferencia:
ImagenConCapas original = new ImagenConCapas("fotografia.png");
original.agregarCapa(new Capa("fondo"));

ImagenConCapas copia = original.clonar();  // Superficial
copia.agregarCapa(new Capa("filtro"));

// ⚠️ La capa se agregó a AMBAS imágenes
System.out.println("Original tiene " + original.capas.size() + " capas");  // 2!

ImagenConCapas copia2 = original.clonarProfundo();  // Profunda
copia2.agregarCapa(new Capa("efecto"));

// ✅ Solo la copia tiene la capa nueva
System.out.println("Original tiene " + original.capas.size() + " capas");  // 1 (correcto)
```

---

## Registro de Prototipos

```java
/**
 * Registry: almacena prototipos para clonarlos.
 */
public class RegistroFormas {
    private Map<String, Forma> formas = new HashMap<>();
    
    public void registrar(String nombre, Forma forma) {
        formas.put(nombre, forma);
    }
    
    public Forma obtener(String nombre) {
        Forma forma = formas.get(nombre);
        return forma != null ? forma.clonar() : null;
    }
}

public interface Forma extends Cloneable {
    Forma clonar();
    void dibujar();
}

public class Circulo implements Forma {
    private int radio;
    private String color;
    
    public Circulo(int radio, String color) {
        this.radio = radio;
        this.color = color;
    }
    
    @Override
    public Circulo clonar() {
        return new Circulo(radio, color);
    }
    
    @Override
    public void dibujar() {
        System.out.println("Dibujando círculo: r=" + radio + ", color=" + color);
    }
}

// Uso:
RegistroFormas registro = new RegistroFormas();
registro.registrar("circulo_rojo", new Circulo(5, "rojo"));
registro.registrar("circulo_azul", new Circulo(10, "azul"));

Forma c1 = registro.obtener("circulo_rojo");  // Clon
Forma c2 = registro.obtener("circulo_rojo");  // Otro clon

c1.dibujar();  // Dibujando círculo: r=5, color=rojo
c2.dibujar();  // Dibujando círculo: r=5, color=rojo
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Performance**: Más rápido que crear desde cero
- **Flexibilidad**: Agregar nuevos tipos sin cambiar código cliente
- **Independencia**: No necesitas conocer constructores complejos

### ❌ Desventajas

- **Complejidad**: Clonación profunda es complicada
- **Confusión**: Difícil distinguir clones de origales
- **Overhead**: Clonar puede ser costoso si el objeto es grande

---

## Cuándo Usarlo

✅ **Usa Prototype cuando:**
- Crear objetos desde cero es muy costoso
- Necesitas muchas instancias similares
- Construcción es compleja

❌ **Evita cuando:**
- Construcción es simple
- Clonación profunda es complicada

---

## Ejercicio

```{exercise}
:label: ej-prototype-usuario

Crea un sistema de clonación para usuarios:
1. Clase `Usuario` que implemente `Cloneable`
2. Método `clonar()` que haga copia profunda
3. `RepositorioUsuarios` que almacene prototipos
4. Prueba clonando usuarios existentes
```

```{solution} ej-prototype-usuario
:class: dropdown

```java
public class Usuario implements Cloneable {
    private String nombre;
    private String email;
    private List<String> roles;
    private Perfil perfil;
    
    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
        this.roles = new ArrayList<>();
    }
    
    private Usuario(Usuario original) {
        this.nombre = original.nombre;
        this.email = original.email;
        this.roles = new ArrayList<>(original.roles);
        this.perfil = original.perfil;
    }
    
    @Override
    public Usuario clonar() {
        return new Usuario(this);
    }
    
    public void agregarRol(String rol) {
        roles.add(rol);
    }
    
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
    
    @Override
    public String toString() {
        return "Usuario{" + nombre + ", " + email + ", roles=" + roles + '}';
    }
}

public class RepositorioUsuarios {
    private Map<String, Usuario> plantillas = new HashMap<>();
    
    public void registrar(String id, Usuario usuario) {
        plantillas.put(id, usuario);
    }
    
    public Usuario crear(String plantillaId) {
        Usuario plantilla = plantillas.get(plantillaId);
        if (plantilla == null) {
            throw new IllegalArgumentException("Plantilla no encontrada");
        }
        return plantilla.clonar();
    }
}

// Uso:
RepositorioUsuarios repo = new RepositorioUsuarios();

Usuario admin = new Usuario("admin", "admin@example.com");
admin.agregarRol("ADMIN");
admin.agregarRol("USUARIO");

repo.registrar("admin", admin);

Usuario nuevoAdmin = repo.crear("admin");
nuevoAdmin.nombre = "admin2";

System.out.println(admin);      // Usuario{admin, ...}
System.out.println(nuevoAdmin);  // Usuario{admin2, ...}
```
```
