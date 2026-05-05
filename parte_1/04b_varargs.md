(varargs-cantidad-variable-de-argumentos)=
## Varargs: Cantidad Variable de Argumentos

A veces es útil escribir un método que pueda recibir cualquier cantidad de argumentos del mismo tipo. Java proporciona una sintaxis especial para esto llamada **varargs** (del inglés _variable arguments_).

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
