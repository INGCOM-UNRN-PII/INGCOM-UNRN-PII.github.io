---
title: "Funciones y Efectos Secundarios"
description: Guía sobre paso de parámetros, efectos secundarios y manejo de referencias en Java.
---

# Funciones y Efectos Secundarios

Comprender cómo Java pasa argumentos a los métodos es fundamental para evitar bugs sutiles y escribir código predecible. Este apunte explora el mecanismo de paso de parámetros y los efectos secundarios que pueden surgir.

## Paso de Parámetros en Java

En Java, **todos los argumentos se pasan por valor**. Sin embargo, el comportamiento es diferente según el tipo de dato:

### Tipos Primitivos: Paso por Valor Puro

Se crea una **copia** del valor. Las modificaciones dentro del método **no afectan** la variable original:

```{code} java
:caption: Paso de primitivos por valor

public class EjemploPrimitivos {
    
    public static void duplicar(int numero) {
        numero = numero * 2;  // Modifica la copia local
        System.out.println("Dentro del método: " + numero);  // 20
    }
    
    public static void main(String[] args) {
        int valor = 10;
        duplicar(valor);
        System.out.println("Después del método: " + valor);  // 10 (sin cambio)
    }
}
```

### Referencias: Paso del Valor de la Referencia

Para objetos, se pasa una **copia de la referencia** (la dirección de memoria). El parámetro apunta al **mismo objeto** que la variable original:

```{code} java
:caption: Paso de referencias

public class EjemploReferencias {
    
    public static void modificarArreglo(int[] arreglo) {
        arreglo[0] = 999;  // Modifica el objeto original
    }
    
    public static void main(String[] args) {
        int[] numeros = {1, 2, 3};
        modificarArreglo(numeros);
        System.out.println(numeros[0]);  // 999 (¡sí cambió!)
    }
}
```

### Diferencia Clave: Reasignación vs Modificación

```{code} java
:caption: Reasignación no afecta al original

public class ReasignacionVsModificacion {
    
    // Modificar el objeto: SÍ afecta al original
    public static void agregarElemento(StringBuilder sb) {
        sb.append(" mundo");  // Modifica el objeto existente
    }
    
    // Reasignar la referencia: NO afecta al original
    public static void reasignar(StringBuilder sb) {
        sb = new StringBuilder("nuevo");  // Solo cambia la referencia local
    }
    
    public static void main(String[] args) {
        StringBuilder texto = new StringBuilder("hola");
        
        agregarElemento(texto);
        System.out.println(texto);  // "hola mundo"
        
        reasignar(texto);
        System.out.println(texto);  // "hola mundo" (sin cambio)
    }
}
```

:::{important}
**Resumen del comportamiento:**
- **Primitivos**: se pasa una copia del valor → no se puede modificar el original
- **Objetos**: se pasa una copia de la referencia → 
  - Modificar el estado del objeto: **SÍ afecta** al original
  - Reasignar la referencia: **NO afecta** al original
:::

## Efectos Secundarios

Un **efecto secundario** ocurre cuando una función modifica algo fuera de su ámbito local (variables externas, archivos, estado global, etc.).

### Ejemplos de Efectos Secundarios

```{code} java
:caption: Métodos con efectos secundarios

public class EfectosSecundarios {
    
    private int contador = 0;
    
    // Efecto secundario: modifica estado de la instancia
    public void incrementar() {
        contador++;
    }
    
    // Efecto secundario: modifica el arreglo recibido
    public void ordenar(int[] arreglo) {
        Arrays.sort(arreglo);  // Modifica el arreglo original
    }
    
    // Efecto secundario: imprime a consola
    public void saludar(String nombre) {
        System.out.println("Hola, " + nombre);
    }
    
    // Sin efectos secundarios: función pura
    public int sumar(int a, int b) {
        return a + b;
    }
}
```

### Funciones Puras

Una **función pura** cumple dos condiciones:
1. Su resultado depende **solo** de sus argumentos
2. **No** tiene efectos secundarios

```{code} java
:caption: Funciones puras vs impuras

// PURA: mismo input → mismo output, sin efectos secundarios
public int calcularArea(int base, int altura) {
    return base * altura;
}

// IMPURA: usa estado externo
private int factor = 2;
public int calcularAreaImpura(int base, int altura) {
    return base * altura * factor;  // Depende de 'factor'
}

// IMPURA: modifica estado externo
private int llamadas = 0;
public int calcularAreaConContador(int base, int altura) {
    llamadas++;  // Efecto secundario
    return base * altura;
}
```

## Evitando Efectos Secundarios No Deseados

### Trabajar con Copias

Para evitar modificar arreglos originales, trabajá con copias:

```{code} java
:caption: Trabajando con copias de arreglos

import java.util.Arrays;

public class CopiasSeguras {
    
    // ✗ Modifica el arreglo original
    public void ordenarPeligroso(int[] arreglo) {
        Arrays.sort(arreglo);
    }
    
    // ✓ Retorna un nuevo arreglo ordenado
    public int[] ordenarSeguro(int[] arreglo) {
        int[] copia = Arrays.copyOf(arreglo, arreglo.length);
        Arrays.sort(copia);
        return copia;
    }
    
    // ✓ Copia dentro del método
    public void procesarSinModificar(int[] arreglo) {
        int[] copia = Arrays.copyOf(arreglo, arreglo.length);
        // Trabajar con 'copia'...
    }
}
```

### Copiar Objetos

Para objetos, la copia puede ser **superficial** o **profunda**:

```{code} java
:caption: Copia superficial vs profunda

public class Persona {
    private String nombre;
    private Direccion direccion;  // Objeto anidado
    
    // Copia superficial: los objetos anidados se comparten
    public Persona copiaSuperficial() {
        Persona copia = new Persona();
        copia.nombre = this.nombre;
        copia.direccion = this.direccion;  // Misma referencia
        return copia;
    }
    
    // Copia profunda: también copia los objetos anidados
    public Persona copiaProfunda() {
        Persona copia = new Persona();
        copia.nombre = this.nombre;
        copia.direccion = new Direccion(
            this.direccion.getCalle(),
            this.direccion.getCiudad()
        );
        return copia;
    }
}
```

:::{warning}
Con copia superficial, modificar un objeto anidado afecta tanto al original como a la copia:

```java
Persona original = new Persona();
Persona copia = original.copiaSuperficial();

copia.getDireccion().setCalle("Nueva calle");
// ¡original.getDireccion().getCalle() también cambió!
```
:::

## Inmutabilidad

### Objetos Inmutables

Un objeto **inmutable** no puede ser modificado después de su creación. `String` es el ejemplo más conocido:

```{code} java
:caption: Strings son inmutables

String original = "hola";
String modificado = original.toUpperCase();

System.out.println(original);    // "hola" (no cambió)
System.out.println(modificado);  // "HOLA" (nuevo String)
```

### Creando Clases Inmutables

```{code} java
:caption: Clase inmutable

public final class Punto {
    private final int x;
    private final int y;
    
    public Punto(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    // No hay setters
    
    // Para "modificar", retornar nuevo objeto
    public Punto mover(int dx, int dy) {
        return new Punto(x + dx, y + dy);
    }
}
```

**Reglas para inmutabilidad:**
1. Declarar la clase como `final`
2. Todos los campos `private final`
3. Sin setters
4. No exponer referencias mutables directamente
5. Crear nuevos objetos en lugar de modificar

### Beneficios de la Inmutabilidad

- **Thread-safe**: seguro para uso en múltiples hilos
- **Predecible**: sin efectos secundarios inesperados
- **Cacheable**: se puede reutilizar y cachear sin riesgo
- **Facilita debugging**: el estado no cambia inesperadamente

## Parámetros Final

Marcar parámetros como `final` previene reasignaciones accidentales:

```{code} java
:caption: Uso de parámetros final

public void procesar(final String texto, final int[] numeros) {
    // texto = "otro";  // ERROR de compilación
    // numeros = new int[10];  // ERROR de compilación
    
    // Pero sí podemos modificar el contenido del arreglo
    numeros[0] = 999;  // Esto sí funciona
}
```

:::{note}
`final` en parámetros solo previene la **reasignación** de la referencia. No convierte objetos mutables en inmutables.
:::

## Retorno de Referencias

### Cuidado al Retornar Referencias Internas

```{code} java
:caption: Exposición de estado interno (antipatrón)

public class CuentaBancaria {
    private List<Transaccion> historial = new ArrayList<>();
    
    // ✗ PELIGROSO: expone la lista interna
    public List<Transaccion> getHistorial() {
        return historial;  // Cualquiera puede modificar la lista
    }
    
    // ✓ SEGURO: retorna copia defensiva
    public List<Transaccion> getHistorialSeguro() {
        return new ArrayList<>(historial);
    }
    
    // ✓ SEGURO: retorna vista no modificable
    public List<Transaccion> getHistorialInmodificable() {
        return Collections.unmodifiableList(historial);
    }
}
```

## Comparación con Punteros (C/C++)

Si venís de C/C++, esta tabla ayuda a entender las diferencias:

:::{table} Comparación con C/C++
:label: tbl-comparacion-c

| Concepto | C/C++ | Java |
| :------- | :---- | :--- |
| Pasar primitivo | Por valor | Por valor |
| Pasar "puntero" | Por valor (de la dirección) | Por valor (de la referencia) |
| Aritmética de punteros | Sí | No |
| Null | `NULL` / `nullptr` | `null` |
| Dereferenciación explícita | Sí (`*ptr`) | No (automática) |
| Memory management | Manual | Garbage Collector |

:::

## Ejercicios

```{exercise}
:label: ej-efectos-1

Explicá qué imprime el siguiente código y por qué:

```java
public static void intercambiar(int[] a, int[] b) {
    int[] temp = a;
    a = b;
    b = temp;
}

int[] x = {1, 2, 3};
int[] y = {4, 5, 6};
intercambiar(x, y);
System.out.println(Arrays.toString(x));
System.out.println(Arrays.toString(y));
```
```

```{solution} ej-efectos-1
El código imprime:
```
[1, 2, 3]
[4, 5, 6]
```

Los arreglos **no se intercambian** porque el método solo intercambia las referencias locales (`a` y `b`), que son copias de las referencias originales (`x` e `y`). Las variables `x` e `y` en `main` siguen apuntando a los mismos arreglos originales.
```

```{exercise}
:label: ej-efectos-2

Implementá una clase `HistorialMensajes` inmutable que almacene una lista de strings. Debe tener un método `agregarMensaje(String)` que retorne una nueva instancia con el mensaje agregado.
```

```{solution} ej-efectos-2
```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HistorialMensajes {
    private final List<String> mensajes;
    
    public HistorialMensajes() {
        this.mensajes = new ArrayList<>();
    }
    
    private HistorialMensajes(List<String> mensajes) {
        this.mensajes = new ArrayList<>(mensajes);  // Copia defensiva
    }
    
    public HistorialMensajes agregarMensaje(String mensaje) {
        List<String> nuevosMensajes = new ArrayList<>(this.mensajes);
        nuevosMensajes.add(mensaje);
        return new HistorialMensajes(nuevosMensajes);
    }
    
    public List<String> getMensajes() {
        return Collections.unmodifiableList(mensajes);
    }
    
    public int cantidad() {
        return mensajes.size();
    }
}
```
```

```{exercise}
:label: ej-efectos-3

Convertí el siguiente método para que sea una "función pura" (sin efectos secundarios):

```java
private int total = 0;

public void sumarAlTotal(int[] numeros) {
    for (int n : numeros) {
        total += n;
    }
    System.out.println("Total actualizado: " + total);
}
```
```

```{solution} ej-efectos-3
```java
// Versión pura: sin estado externo, sin efectos secundarios
public int calcularSuma(int[] numeros) {
    int suma = 0;
    for (int n : numeros) {
        suma += n;
    }
    return suma;
}

// Uso:
int resultado = calcularSuma(new int[]{1, 2, 3, 4, 5});
System.out.println("Suma: " + resultado);
```
```

:::{seealso}
- [Paso de parámetros en Java](https://docs.oracle.com/javase/tutorial/java/javaOO/arguments.html)
:::
