---
title: "Patrón Visitor"
subtitle: "Representar operación a aplicar a elementos de estructura"
subject: Patrones de Diseño de Comportamiento
---

(patron-visitor)=
# Visitor: Operaciones sobre Estructuras

El patrón **Visitor** representa una operación a ser realizada en los elementos de una estructura de objetos, permitiendo definir nuevas operaciones sin cambiar las clases de los elementos sobre los que opera.

:::{admonition} Propósito
:class: note

Permitir definir nuevas operaciones sobre elementos de estructura sin modificarlos.
:::

---

## Origen e Historia

Gang of Four 1994. Surge de compiladores: necesidad de múltiples operaciones sobre AST sin modificar estructura.

## Motivación

Necesario cuando:
- Múltiples operaciones sobre estructura estable
- Estructura no cambia pero operaciones sí
- Agregar operaciones frecuentemente
- Evitar condicionales de tipos

## Contexto

**Patrón:** Visitor recorre estructura y aplica operación

**Anatomía:**
- **Visitor**: Define operaciones
- **ConcreteVisitor**: Implementa operaciones
- **Element**: Acepta visitor (double dispatch)
- **ObjectStructure**: Colección de elementos

**Técnica:** Double dispatch: tipo del visitante + tipo del elemento

**Distinción de Interpreter:**
- **Interpreter**: Procesa estructura
- **Visitor**: Aplica operación a estructura existente

---

## Problema

```
Estructura: Documento
  ├─ Párrafo
  ├─ Imagen
  └─ Tabla

Visitantes:
  ├─ VisitantePDF (renderizar como PDF)
  ├─ VisitanteHTML (renderizar como HTML)
  └─ VisitanteConteo (contar elementos)
```

---

## Problema

```java
// ❌ Agregar operación = modificar cada clase
abstract class Elemento {
    abstract void renderizar();    // Operación 1
    abstract void exportarPDF();   // Operación 2
    abstract void exportarHTML();  // Operación 3 - nuevo!
}

class Párrafo extends Elemento {
    void renderizar() { ... }
    void exportarPDF() { ... }
    void exportarHTML() { ... }  // Hay que modificar
}
```

---

## Solución: Visitor

```java
/**
 * Visitante: define operaciones.
 */
public interface Visitante {
    void visitar(Párrafo párrafo);
    void visitar(Imagen imagen);
    void visitar(Tabla tabla);
}

/**
 * Elemento: acepta visitantes.
 */
public abstract class Elemento {
    abstract void aceptar(Visitante visitante);
}

/**
 * Elemento concreto: Párrafo.
 */
public class Párrafo extends Elemento {
    private String contenido;
    
    public Párrafo(String contenido) {
        this.contenido = contenido;
    }
    
    @Override
    public void aceptar(Visitante visitante) {
        visitante.visitar(this);  // Double dispatch
    }
    
    public String getContenido() {
        return contenido;
    }
}

/**
 * Elemento concreto: Imagen.
 */
public class Imagen extends Elemento {
    private String ruta;
    
    public Imagen(String ruta) {
        this.ruta = ruta;
    }
    
    @Override
    public void aceptar(Visitante visitante) {
        visitante.visitar(this);
    }
    
    public String getRuta() {
        return ruta;
    }
}

/**
 * Elemento concreto: Tabla.
 */
public class Tabla extends Elemento {
    private int filas, columnas;
    
    public Tabla(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
    }
    
    @Override
    public void aceptar(Visitante visitante) {
        visitante.visitar(this);
    }
    
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
}

/**
 * Visitante concreto: Exportar a PDF.
 */
public class VisitantePDF implements Visitante {
    @Override
    public void visitar(Párrafo párrafo) {
        System.out.println("📄 PDF: Renderizando párrafo: \"" + párrafo.getContenido() + "\"");
    }
    
    @Override
    public void visitar(Imagen imagen) {
        System.out.println("📄 PDF: Insertando imagen: " + imagen.getRuta());
    }
    
    @Override
    public void visitar(Tabla tabla) {
        System.out.println("📄 PDF: Renderizando tabla de " + tabla.getFilas() + "x" + tabla.getColumnas());
    }
}

/**
 * Visitante concreto: Exportar a HTML.
 */
public class VisitanteHTML implements Visitante {
    @Override
    public void visitar(Párrafo párrafo) {
        System.out.println("<p>" + párrafo.getContenido() + "</p>");
    }
    
    @Override
    public void visitar(Imagen imagen) {
        System.out.println("<img src='" + imagen.getRuta() + "' />");
    }
    
    @Override
    public void visitar(Tabla tabla) {
        System.out.println("<table rows='" + tabla.getFilas() + "' cols='" + tabla.getColumnas() + "'>");
    }
}

/**
 * Contenedor: estructura de elementos.
 */
public class Documento {
    private List<Elemento> elementos = new ArrayList<>();
    
    public void agregar(Elemento elemento) {
        elementos.add(elemento);
    }
    
    public void aceptar(Visitante visitante) {
        for (Elemento elemento : elementos) {
            elemento.aceptar(visitante);
        }
    }
}

// ✅ Uso: Nuevas operaciones sin modificar elementos
Documento doc = new Documento();
doc.agregar(new Párrafo("Introducción al patrón Visitor"));
doc.agregar(new Imagen("diagrama.png"));
doc.agregar(new Tabla(3, 4));

System.out.println("=== Exportar a PDF ===");
doc.aceptar(new VisitantePDF());

System.out.println("\n=== Exportar a HTML ===");
doc.aceptar(new VisitanteHTML());

// Agregar nuevo visitante sin tocar Elemento!
```

---

## Double Dispatch

```
1. doc.aceptar(visitante)     → Dispatch 1: tipo de doc
2. elemento.aceptar(visitante) → Dispatch 2: tipo de visitante
3. visitante.visitar(elemento) → Respuesta correcta

Esto permite elegir método según ambos tipos!
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Open/Closed**: Abierto a extensión (nuevos visitantes), cerrado a modificación
- **Separación de intereses**: Operaciones separadas de estructura
- **Facilidad**: Agregar operaciones sin modificar elementos
- **Localización**: Operación en un único visitante

### ❌ Desventajas

- **Complejidad**: Double dispatch es complejo
- **Encapsulación**: Requiere exponer métodos getters
- **Acoplamiento**: Visitante acoplado a estructura
- **Performance**: Llamadas adicionales
- **Dinámico**: No funciona bien con tipos dinámicos

---

## Cuándo Usarlo

✅ **Usa cuando:**
- Múltiples operaciones sobre estructura estable
- Estructura no cambia pero operaciones sí
- Necesitas agregar operaciones frecuentemente
- Ejemplos: Compiladores AST, editores, procesadores de documentos

❌ **Evita cuando:**
- Estructura cambia frecuentemente
- Pocos tipos de elementos
- Las operaciones están mejor en los elementos

---

## Ejercicio

```{exercise}
:label: ej-visitor-expresiones

Crea visitantes para expresiones matemáticas:
1. `Número`, `Suma`, `Multiplicación` (elementos)
2. Visitantes: `Evaluador`, `Impresor`
```

```{solution} ej-visitor-expresiones
:class: dropdown

```java
public interface VisitanteExpr {
    double visitar(Número n);
    double visitar(Suma suma);
    double visitar(Multiplicación mult);
}

public abstract class Expresión {
    abstract double aceptar(VisitanteExpr visitante);
}

public class Número extends Expresión {
    private double valor;
    
    public Número(double v) { valor = v; }
    
    @Override
    public double aceptar(VisitanteExpr v) {
        return v.visitar(this);
    }
    
    public double getValor() { return valor; }
}

public class Suma extends Expresión {
    private Expresión izq, der;
    
    public Suma(Expresión i, Expresión d) { izq = i; der = d; }
    
    @Override
    public double aceptar(VisitanteExpr v) {
        return v.visitar(this);
    }
    
    public Expresión getIzq() { return izq; }
    public Expresión getDer() { return der; }
}

public class Multiplicación extends Expresión {
    private Expresión izq, der;
    
    public Multiplicación(Expresión i, Expresión d) { izq = i; der = d; }
    
    @Override
    public double aceptar(VisitanteExpr v) {
        return v.visitar(this);
    }
    
    public Expresión getIzq() { return izq; }
    public Expresión getDer() { return der; }
}

public class Evaluador implements VisitanteExpr {
    @Override
    public double visitar(Número n) {
        return n.getValor();
    }
    
    @Override
    public double visitar(Suma suma) {
        return suma.getIzq().aceptar(this) + suma.getDer().aceptar(this);
    }
    
    @Override
    public double visitar(Multiplicación mult) {
        return mult.getIzq().aceptar(this) * mult.getDer().aceptar(this);
    }
}

public class Impresor implements VisitanteExpr {
    @Override
    public double visitar(Número n) {
        System.out.print(n.getValor());
        return 0;
    }
    
    @Override
    public double visitar(Suma suma) {
        System.out.print("(");
        suma.getIzq().aceptar(this);
        System.out.print(" + ");
        suma.getDer().aceptar(this);
        System.out.print(")");
        return 0;
    }
    
    @Override
    public double visitar(Multiplicación mult) {
        System.out.print("(");
        mult.getIzq().aceptar(this);
        System.out.print(" * ");
        mult.getDer().aceptar(this);
        System.out.print(")");
        return 0;
    }
}

// Uso
Expresión expr = new Suma(
    new Multiplicación(new Número(2), new Número(3)),
    new Número(4)
); // (2 * 3) + 4 = 10

System.out.print("Expresión: ");
expr.aceptar(new Impresor());
System.out.println();

double resultado = expr.aceptar(new Evaluador());
System.out.println("Resultado: " + resultado);
```
```
