(varargs-cantidad-variable-de-argumentos)=
## Varargs: Cantidad Variable de Argumentos

A veces es útil escribir un método que pueda recibir cualquier cantidad de argumentos del mismo tipo. Java proporciona una sintaxis especial para esto llamada **varargs** (del inglés _variable arguments_).

:::{note} Hoja de ruta del capítulo

**Objetivo.** Comprender las ideas centrales de **Varargs: Cantidad Variable de Argumentos** y usarlas como base para el resto del recorrido.

**Prerrequisitos.** Conviene haber leído [el material inmediatamente anterior](04_metodos.md) para llegar con el hilo de la parte fresco.

**Desarrollo.** El desarrollo del capítulo aparece en las secciones que siguen. Conviene recorrerlas en orden y volver al resumen antes de pasar al siguiente tema.
:::

(sintaxis-y-uso)=
### Sintaxis y Uso

Se declara con tres puntos (`...`) después del tipo del último parámetro:

```{code} java
:caption: Método con varargs

public static int sumarTodos(int... numeros) {
    int total = 0;
    for (int n : numeros) {
        total = total + n;
    }
    return total;
}

// Se puede llamar con cualquier cantidad de argumentos
int r1 = sumarTodos();              // 0 argumentos, retorna 0
int r2 = sumarTodos(5);             // 1 argumento, retorna 5
int r3 = sumarTodos(1, 2, 3);       // 3 argumentos, retorna 6
int r4 = sumarTodos(1, 2, 3, 4, 5); // 5 argumentos, retorna 15
```

(implementacion-interna)=
### Implementación Interna

Internamente, el compilador convierte los varargs en un **arreglo**. El método `sumarTodos(int... numeros)` es equivalente a `sumarTodos(int[] numeros)`, pero con la diferencia de que al llamarlo no hace falta crear el arreglo explícitamente.

```{code} java
:caption: Equivalencia con arreglos

// Estas dos llamadas son equivalentes:
sumarTodos(1, 2, 3);
sumarTodos(new int[]{1, 2, 3});  // Creando el arreglo explícitamente
```

(restricciones)=
### Restricciones

- Solo puede haber **un parámetro varargs** por método.
- Debe ser el **último parámetro** de la lista.

```{code} java
:caption: Varargs con otros parámetros

// Correcto: varargs al final
public static void imprimir(String prefijo, int... valores) {
    System.out.print(prefijo + ": ");
    for (int v : valores) {
        System.out.print(v + " ");
    }
    System.out.println();
}

imprimir("Notas", 7, 8, 9, 10);  // Imprime: "Notas: 7 8 9 10"

// Error: varargs no está al final
// public static void mal(int... valores, String sufijo) { }
```

:::{note} Varargs y Sobrecarga
Evitá sobrecargar métodos que usen varargs, ya que las reglas de resolución se vuelven complejas. Por ejemplo, si tenés `metodo(int... x)` y `metodo(int x, int... y)`, la llamada `metodo(5)` es ambigua y genera error de compilación.
:::

## Resumen

Este capítulo presentó las ideas centrales de **Varargs: Cantidad Variable de Argumentos** y dejó un marco de referencia para relacionarlas con el resto del recorrido.

## Ejercicios

```{exercise}
:label: ex-04b-varargs-cierre

Escribí un ejemplo propio donde tengas que aplicar al menos dos ideas de este capítulo sobre **Varargs: Cantidad Variable de Argumentos**. Justificá qué decisión de diseño, sintaxis o modelado tomás y por qué.
```

## Próximo paso

Para seguir, conviene pasar a [el material siguiente](05_sintaxis_control.md), donde el recorrido continúa sobre esta base.
