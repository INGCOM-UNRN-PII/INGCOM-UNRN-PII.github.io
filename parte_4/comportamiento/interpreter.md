---
title: "Patrón Interpreter"
subtitle: "Definir representación de gramática y interpretador"
subject: Patrones de Diseño de Comportamiento
---

(patron-interpreter)=
# Interpreter: Lenguaje Personalizado

El patrón **Interpreter** define una representación de una gramática para un lenguaje simple y un interpretador para procesarlo. Útil para lenguajes o expresiones específicas del dominio.

:::{admonition} Propósito
:class: note

Definir gramática personalizada e interpretador para procesarla.
:::

---

## Concepto

```
Entrada: "a + b * c"
         ↓
    Analizador léxico
         ↓
    Árbol de expresión (AST)
         ↓
    Intérprete
         ↓
    Salida: valor o acción
```

---

## Problema

```java
// ❌ Sin Interpreter: evaluación manual con condiciones
class Calculadora {
    int evaluar(String expresión) {
        if (expresión.contains("+")) {
            String[] partes = expresión.split("\\+");
            return Integer.parseInt(partes[0]) + Integer.parseInt(partes[1]);
        } else if (expresión.contains("-")) {
            // ... más condiciones
        }
        // No es escalable
    }
}
```

---

## Solución: Interpreter

```java
/**
 * Interfaz abstracta para expresiones.
 */
public interface Expresión {
    int interpretar();
}

/**
 * Expresión terminal: número.
 */
public class ExpresiónNúmero implements Expresión {
    private int valor;
    
    public ExpresiónNúmero(int valor) {
        this.valor = valor;
    }
    
    @Override
    public int interpretar() {
        return valor;
    }
}

/**
 * Expresión no-terminal: suma.
 */
public class ExpresiónSuma implements Expresión {
    private Expresión izquierda;
    private Expresión derecha;
    
    public ExpresiónSuma(Expresión izq, Expresión der) {
        this.izquierda = izq;
        this.derecha = der;
    }
    
    @Override
    public int interpretar() {
        return izquierda.interpretar() + derecha.interpretar();
    }
}

/**
 * Expresión no-terminal: resta.
 */
public class ExpresiónResta implements Expresión {
    private Expresión izquierda;
    private Expresión derecha;
    
    public ExpresiónResta(Expresión izq, Expresión der) {
        this.izquierda = izq;
        this.derecha = der;
    }
    
    @Override
    public int interpretar() {
        return izquierda.interpretar() - derecha.interpretar();
    }
}

/**
 * Expresión no-terminal: multiplicación.
 */
public class ExpresiónMultiplicación implements Expresión {
    private Expresión izquierda;
    private Expresión derecha;
    
    public ExpresiónMultiplicación(Expresión izq, Expresión der) {
        this.izquierda = izq;
        this.derecha = der;
    }
    
    @Override
    public int interpretar() {
        return izquierda.interpretar() * derecha.interpretar();
    }
}

// ✅ Uso: Construir árbol de expresión
// Evaluar: (5 + 3) * 2

Expresión cinco = new ExpresiónNúmero(5);
Expresión tres = new ExpresiónNúmero(3);
Expresión dos = new ExpresiónNúmero(2);

// (5 + 3)
Expresión suma = new ExpresiónSuma(cinco, tres);

// (5 + 3) * 2
Expresión resultado = new ExpresiónMultiplicación(suma, dos);

System.out.println("Resultado: " + resultado.interpretar());  // 16
```

---

## Árbol de Expresión (AST)

```
        *
       / \
      +   2
     / \
    5   3

Interpretación bottom-up:
1. 5 → 5
2. 3 → 3
3. 5+3 → 8
4. 8*2 → 16
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Flexibilidad**: Agregar nuevas operaciones
- **Extensible**: Nuevos tipos de expresiones
- **Separación**: Gramática separada del interpretador
- **Reutilizable**: AST puede procesarse múltiples veces

### ❌ Desventajas

- **Complejidad**: Muchas clases pequeñas
- **Performance**: Interpretación es lenta vs. compilación
- **Memoria**: AST puede ser grande
- **Difícil de entender**: Requiere conocimiento de lenguajes formales

---

## Cuándo Usarlo

✅ **Usa cuando:**
- Tienes lenguaje simple a interpretar
- Necesitas extender sintaxis frecuentemente
- Ejemplos: Calculadoras, DSL, filtros SQL personalizados

❌ **Evita cuando:**
- Performance crítica (compilar mejor)
- Gramática muy compleja

---

## Ejercicio

```{exercise}
:label: ej-interpreter-booleano

Crea intérprete para expresiones booleanas:
1. Expresiones: `Y`, `O`, `NO`
2. Variables booleanas
3. Evaluar expresión con valores dados
```

```{solution} ej-interpreter-booleano
:class: dropdown

```java
public interface ExpresiónBool {
    boolean evaluar(Map<String, Boolean> variables);
}

public class Variable implements ExpresiónBool {
    private String nombre;
    
    public Variable(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public boolean evaluar(Map<String, Boolean> vars) {
        return vars.getOrDefault(nombre, false);
    }
}

public class Y implements ExpresiónBool {
    private ExpresiónBool izq, der;
    
    public Y(ExpresiónBool izq, ExpresiónBool der) {
        this.izq = izq;
        this.der = der;
    }
    
    @Override
    public boolean evaluar(Map<String, Boolean> vars) {
        return izq.evaluar(vars) && der.evaluar(vars);
    }
}

public class O implements ExpresiónBool {
    private ExpresiónBool izq, der;
    
    public O(ExpresiónBool izq, ExpresiónBool der) {
        this.izq = izq;
        this.der = der;
    }
    
    @Override
    public boolean evaluar(Map<String, Boolean> vars) {
        return izq.evaluar(vars) || der.evaluar(vars);
    }
}

// Uso: (a Y b) O NO c
Map<String, Boolean> vars = Map.of("a", true, "b", false, "c", true);
ExpresiónBool expr = new O(
    new Y(new Variable("a"), new Variable("b")),
    new Variable("c")
);
System.out.println(expr.evaluar(vars)); // true
```
```
