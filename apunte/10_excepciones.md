---
title: "Excepciones en Java"
description: Estudio avanzado sobre la gestión de errores, jerarquía de Throwable y robustez en el diseño de software.
---

# Excepciones en Java

El manejo de excepciones en Java es un mecanismo que permite gestionar situaciones anormales o errores que ocurren durante la ejecución de un programa. A diferencia de C, donde el flujo de error se mezcla con el flujo de datos (usando códigos de retorno como `-1` o `NULL`), Java separa ambos canales, permitiendo una gestión más limpia y centralizada.

## ¿Qué es una excepción?

Una **excepción** es un evento que ocurre durante la ejecución de un programa e interrumpe el flujo normal de las instrucciones. En términos simples, es la forma que tiene Java de decir "algo salió mal".

Pensalo de esta manera: cuando escribís código, esperás que cada línea se ejecute en orden, una tras otra. Pero ¿qué pasa si intentás dividir por cero? ¿O acceder a una posición inexistente de un arreglo? ¿O leer un archivo que no existe? En estos casos, el programa no puede continuar normalmente.

En C, estas situaciones se manejaban de distintas formas:

- Retornando valores especiales (como `-1` o `NULL`) para indicar error
- Usando la variable global `errno`
- Simplemente terminando el programa con `exit()`

Java propone un enfoque diferente: cuando ocurre un error, se "lanza" una excepción. Esta excepción es un **objeto** que contiene información sobre qué salió mal, dónde ocurrió, y por qué. El programa puede "atrapar" esta excepción y decidir qué hacer: mostrar un mensaje de error, intentar de nuevo, usar un valor por defecto, o propagarla hacia arriba.

## El Problema del Manejo de Errores en C

En C, el manejo de errores tradicional presenta varios problemas que seguramente ya experimentaste en la cursada anterior.

### Valores de retorno especiales

La técnica más común en C es usar valores especiales para indicar errores. Por ejemplo, `fopen()` retorna `NULL` si no puede abrir el archivo, y `getchar()` retorna `EOF` (que es -1) si hay un error de lectura.

```{code} c
:caption: Manejo de errores típico en C

// En C: el error se mezcla con el valor de retorno
int resultado = abrir_archivo("datos.txt");
if (resultado == -1) {
    // Error: archivo no encontrado
}
if (resultado == -2) {
    // Error: sin permisos
}
// ... ¿y si -1 o -2 fueran valores válidos?

// Alternativa: variable global errno
FILE *f = fopen("datos.txt", "r");
if (f == NULL) {
    if (errno == ENOENT) {
        // Archivo no existe
    } else if (errno == EACCES) {
        // Sin permisos
    }
}
```

### El problema con `errno`

En C existe una variable global llamada `errno` que almacena el código del último error ocurrido. Después de llamar a una función que puede fallar, podés revisar `errno` para saber qué pasó. Pero esto tiene problemas:

- `errno` es **global**, lo que significa que cualquier función puede modificarla
- Si llamás a otra función antes de revisar `errno`, perdés la información del error anterior
- En programas con múltiples hilos de ejecución, `errno` se vuelve aún más problemático

### Problemas de este enfoque

1. **Mezcla de canales**: El valor de retorno debe transmitir tanto el resultado exitoso como el código de error. Si una función retorna `int` y usás `-1` para error, ¿qué pasa si `-1` es un resultado válido?

2. **Fácil de ignorar**: El programador puede olvidar verificar el código de error. Nada te obliga a revisar si `malloc()` retornó `NULL`.

3. **Propagación manual**: Si una función A llama a B, y B llama a C, y C falla, B debe detectar el error y propagarlo a A, y A debe hacer lo mismo. Cada función intermedia debe verificar errores explícitamente.

4. **Sin información de contexto**: Un código numérico como `-1` o `errno = 2` no indica dónde ni por qué ocurrió el error. Tampoco te dice qué funciones se estaban ejecutando cuando falló.

## La Solución de Java: Excepciones

Java introduce un mecanismo completamente diferente que separa el flujo normal del programa del flujo de manejo de errores. En lugar de revisar valores de retorno después de cada llamada a función, Java permite que el código de éxito y el código de error estén en lugares separados.

### Analogía con la vida real

Imaginá que estás siguiendo una receta de cocina. Las instrucciones dicen:

1. Precalentar el horno
2. Mezclar los ingredientes
3. Hornear por 30 minutos
4. Servir

En C, la receta incluiría verificaciones después de cada paso:

1. Precalentar el horno. **Si el horno no enciende, ir al paso 10.**
2. Mezclar los ingredientes. **Si falta algún ingrediente, ir al paso 11.**
3. Hornear por 30 minutos. **Si la comida se quema, ir al paso 12.**
4. Servir.
...
10. Manejo de error: horno roto
11. Manejo de error: ingrediente faltante
12. Manejo de error: comida quemada

En Java, la estructura es más limpia:

```
INTENTAR:
    1. Precalentar el horno
    2. Mezclar los ingredientes
    3. Hornear por 30 minutos
    4. Servir
SI FALLA EL HORNO:
    Manejar problema del horno
SI FALTA INGREDIENTE:
    Manejar ingrediente faltante
```

El flujo normal está separado del manejo de errores, y si algo falla, el programa "salta" automáticamente a la sección de manejo de errores.

### Código de ejemplo

```{code} java
:caption: Manejo de errores con excepciones en Java

// El flujo normal no se contamina con códigos de error
try {
    int resultado = leerArchivo("datos.txt");
    procesarDatos(resultado);
    guardarResultado();
} catch (FileNotFoundException e) {
    System.out.println("Error: el archivo no existe");
    System.out.println("Ubicación del error: " + e.getMessage());
} catch (IOException e) {
    System.out.println("Error de lectura/escritura");
}
```

### Ventajas del enfoque de excepciones

1. **Separación clara**: El código de éxito está en el `try`, el código de error en el `catch`. No hay `if` intercalados verificando errores.

2. **Imposible de ignorar**: Las excepciones llamadas "checked" (comprobadas) **deben** manejarse o declararse. El compilador no te deja compilar si ignorás una excepción checked.

3. **Propagación automática**: Si ocurre una excepción y no la atrapás, automáticamente "sube" a la función que te llamó. No tenés que escribir código para propagar el error manualmente.

4. **Información rica**: La excepción no es un simple número. Es un objeto que incluye:
   - Un mensaje descriptivo del error
   - El tipo de error (su clase)
   - El "stack trace": la secuencia completa de llamadas a funciones que llevaron al error
   - Opcionalmente, la causa original si fue envuelta en otra excepción

## Sintaxis Básica: try-catch

### Estructura Fundamental

La estructura básica del manejo de excepciones en Java usa las palabras clave `try` (intentar) y `catch` (atrapar):

```{code} java
:caption: Estructura try-catch básica

try {
    // Código que puede lanzar una excepción
    // Si ocurre una excepción, el flujo salta al catch correspondiente
    // Las líneas que siguen a la que lanzó la excepción NO se ejecutan
} catch (TipoDeExcepcion nombreVariable) {
    // Código para manejar la excepción
    // nombreVariable contiene información sobre el error
    // Este bloque solo se ejecuta si ocurre una excepción del tipo especificado
}
```

**Explicación paso a paso:**

1. El programa intenta ejecutar el código dentro del bloque `try`
2. Si **no ocurre ninguna excepción**, todo el bloque `try` se ejecuta y el bloque `catch` se ignora completamente
3. Si **ocurre una excepción**, la ejecución del bloque `try` se interrumpe inmediatamente en la línea que causó el error
4. Java busca un bloque `catch` cuyo tipo coincida con la excepción lanzada
5. Si lo encuentra, ejecuta ese bloque `catch`
6. Después del `catch`, el programa continúa normalmente con las instrucciones que siguen a toda la estructura try-catch

### Ejemplo Práctico: División por Cero

En C, si dividías un entero por cero, el comportamiento era indefinido (undefined behavior). El programa podía terminar abruptamente, producir resultados incorrectos, o incluso parecer funcionar. En Java, esto produce una excepción bien definida: `ArithmeticException`.

```{code} java
:caption: Captura de ArithmeticException

public static int dividir(int dividendo, int divisor) {
    int resultado = 0;
    
    try {
        resultado = dividendo / divisor;
        System.out.println("División exitosa");
        // Esta línea solo se ejecuta si la división fue exitosa
    } catch (ArithmeticException e) {
        System.out.println("Error: división por cero");
        System.out.println("Mensaje: " + e.getMessage());
        resultado = 0;  // Valor por defecto en caso de error
    }
    
    return resultado;
}

// Uso:
int r1 = dividir(10, 2);  // Imprime "División exitosa", retorna 5
int r2 = dividir(10, 0);  // Imprime "Error: división por cero", retorna 0
```

**¿Qué pasa en cada caso?**

- `dividir(10, 2)`: La división `10 / 2` funciona bien, se imprime "División exitosa", y se retorna 5. El bloque `catch` nunca se ejecuta.

- `dividir(10, 0)`: La división `10 / 0` lanza una `ArithmeticException`. El programa **salta inmediatamente** al bloque `catch`, sin ejecutar la línea que imprime "División exitosa". Dentro del `catch`, la variable `e` contiene información sobre el error: `e.getMessage()` retorna "/ by zero".

### Múltiples Bloques catch

Un bloque `try` puede tener varios bloques `catch`, cada uno para un tipo diferente de excepción. Esto permite manejar cada tipo de error de manera específica:

```{code} java
:caption: Múltiples bloques catch

try {
    int[] numeros = {1, 2, 3};
    int valor = numeros[10];      // Puede lanzar ArrayIndexOutOfBoundsException
    int resultado = valor / 0;     // Puede lanzar ArithmeticException
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Error: índice fuera del arreglo");
} catch (ArithmeticException e) {
    System.out.println("Error: operación aritmética inválida");
}
```

**¿Cómo funciona?**

Cuando ocurre una excepción dentro del `try`, Java recorre los bloques `catch` **en orden**, de arriba hacia abajo. El primer `catch` cuyo tipo coincida (o sea compatible) con la excepción lanzada es el que se ejecuta. Los demás bloques `catch` se ignoran.

En el ejemplo anterior:
- Si `numeros[10]` falla (índice 10 no existe en un arreglo de 3 elementos), se ejecuta el primer `catch`
- La línea `valor / 0` **nunca se alcanza** porque la excepción ya ocurrió antes
- Si hipotéticamente la primera línea funcionara y la división fallara, se ejecutaría el segundo `catch`

:::{important} Orden de los catch
Los bloques `catch` se evalúan en orden. Si una excepción coincide con el primer `catch`, los siguientes no se ejecutan. Siempre colocá las excepciones más específicas primero y las más generales después. Si ponés una excepción general primero, las específicas nunca se alcanzarán y el compilador dará error.
:::

### Captura Múltiple (Java 7+)

A partir de Java 7, si querés manejar varias excepciones de la misma manera (con el mismo código de manejo), podés combinarlas usando el operador `|` (pipe, o "barra vertical"):

```{code} java
:caption: Captura múltiple con pipe

try {
    // código que puede lanzar varias excepciones
} catch (ArithmeticException | ArrayIndexOutOfBoundsException e) {
    System.out.println("Error de cálculo o acceso: " + e.getMessage());
}
```

Esto es equivalente a escribir dos bloques `catch` con el mismo código, pero más compacto. Usalo cuando el tratamiento del error sea idéntico para ambos tipos de excepción.

:::{note}
Cuando usás captura múltiple con `|`, las excepciones listadas **no pueden tener relación de herencia** entre sí. Es decir, no podés poner `Exception | ArithmeticException` porque `ArithmeticException` ya es un tipo de `Exception`.
:::

## El Bloque finally

El bloque `finally` es una parte opcional de la estructura try-catch que se ejecuta **siempre**, sin importar si ocurrió una excepción o no, si se atrapó o no, o si el `try` terminó con un `return`. Es útil para código de limpieza que debe ejecutarse en todos los casos.

### ¿Por qué necesitamos finally?

Pensá en recursos como archivos abiertos, conexiones a bases de datos, o memoria reservada. En C, era tu responsabilidad asegurarte de cerrar archivos y liberar memoria, pero era fácil olvidarlo, especialmente cuando el código tenía múltiples puntos de salida o podía fallar en diferentes lugares.

El bloque `finally` garantiza que el código de limpieza se ejecute sin importar qué pase.

```{figure} 10/flujo_try_catch_finally.svg
:label: fig-flujo-try-catch-finally
:align: center
:width: 85%

Flujo de ejecución del bloque try-catch-finally en diferentes escenarios.
```

```{code} java
:caption: Uso del bloque finally

Scanner scanner = null;

try {
    scanner = new Scanner(System.in);
    System.out.print("Ingrese un número: ");
    int numero = scanner.nextInt();
    System.out.println("El doble es: " + (numero * 2));
} catch (InputMismatchException e) {
    System.out.println("Error: no ingresó un número válido");
} finally {
    // Esto se ejecuta SIEMPRE, haya o no excepción
    System.out.println("Limpiando recursos...");
    if (scanner != null) {
        scanner.close();
    }
}
```

**Análisis del código:**

1. Se declara `scanner` fuera del `try` para que sea accesible en el `finally`
2. Dentro del `try`, se crea el Scanner y se intenta leer un número
3. Si el usuario ingresa algo que no es un número (como "abc"), `nextInt()` lanza `InputMismatchException`
4. El `catch` maneja ese error específico
5. El `finally` **siempre** cierra el scanner, independientemente de si hubo error o no

### Flujo de Ejecución con finally

La siguiente tabla resume qué bloques se ejecutan en cada escenario:

| Escenario | try | catch | finally |
|:---|:---:|:---:|:---:|
| Sin excepción | Completo | No ejecuta | **Ejecuta** |
| Excepción capturada | Parcial (hasta el error) | Ejecuta | **Ejecuta** |
| Excepción no capturada | Parcial (hasta el error) | No coincide | **Ejecuta** (y luego propaga) |

```{code} java
:caption: Demostración del flujo de finally

public static void demostrar(int opcion) {
    try {
        System.out.println("1. Inicio del try");
        
        if (opcion == 1) {
            System.out.println("2. Operación exitosa");
        } else if (opcion == 2) {
            throw new RuntimeException("Error simulado");
        }
        
        System.out.println("3. Fin del try");
    } catch (RuntimeException e) {
        System.out.println("4. En el catch: " + e.getMessage());
    } finally {
        System.out.println("5. En el finally (SIEMPRE)");
    }
    System.out.println("6. Después del try-catch-finally");
}

// demostrar(1) imprime: 1, 2, 3, 5, 6  (sin excepción, catch no se ejecuta)
// demostrar(2) imprime: 1, 4, 5, 6     (con excepción, línea 3 no se alcanza)
```

### finally se ejecuta incluso con return

Un detalle importante: el bloque `finally` se ejecuta **incluso si el `try` o el `catch` contienen un `return`**. El `return` queda "pendiente" hasta que termine el `finally`:

```{code} java
:caption: finally con return

public static int prueba() {
    try {
        System.out.println("En el try");
        return 1;  // El return queda "pendiente"
    } finally {
        System.out.println("En el finally");  // Esto se ejecuta antes del return
    }
}
// Imprime: "En el try", luego "En el finally", y retorna 1
```

:::{warning} No pongas return en finally
Si ponés un `return` dentro del `finally`, ese return "sobreescribe" cualquier return anterior del `try` o `catch`. Esto es una muy mala práctica porque oculta el verdadero resultado o excepción.
:::

## Tipos de Excepciones

No todas las excepciones son iguales. Java organiza las excepciones en una jerarquía que refleja diferentes tipos de problemas y diferentes estrategias de manejo.

### La clase Throwable

En Java, todo lo que puede ser "lanzado" con `throw` y "atrapado" con `catch` debe ser una instancia de la clase `Throwable` o alguna de sus subclases. Esta es la raíz de la jerarquía de excepciones.

`Throwable` tiene dos ramas principales:

- **Error**: Problemas graves del sistema que generalmente no deberías intentar manejar
- **Exception**: Problemas que tu programa podría querer manejar

### Jerarquía de Throwable

```{figure} 10/jerarquia_excepciones.svg
:label: fig-jerarquia-excepciones
:align: center
:width: 85%

Jerarquía completa de Throwable: Error vs Exception, Checked vs Unchecked.
```

### Error vs Exception

**Error** representa problemas serios del ambiente de ejecución:
- `OutOfMemoryError`: La JVM se quedó sin memoria
- `StackOverflowError`: Demasiadas llamadas recursivas llenaron la pila
- `NoClassDefFoundError`: No se encontró una clase necesaria

Estos errores generalmente no se pueden (ni se deben) manejar en código normal. Si te quedás sin memoria, no hay mucho que puedas hacer.

**Exception** representa condiciones que un programa razonable podría querer manejar:
- Archivo no encontrado
- Entrada de usuario inválida
- Error de red
- División por cero

### Excepciones Checked vs Unchecked

Dentro de las excepciones (`Exception`), Java hace una distinción crucial entre dos categorías:

**Excepciones Checked (Comprobadas):**
- Son subclases de `Exception` pero **no** de `RuntimeException`
- El compilador **obliga** a manejarlas o declararlas con `throws`
- Representan condiciones que están fuera del control del programador pero que son previsibles
- Ejemplos: `IOException` (error de archivo), `FileNotFoundException` (archivo no existe), `SQLException` (error de base de datos)

**Excepciones Unchecked (No Comprobadas):**
- Son subclases de `RuntimeException`
- El compilador **no** obliga a manejarlas
- Representan errores de programación que podrían haberse evitado
- Ejemplos: `NullPointerException` (usar referencia null), `ArrayIndexOutOfBoundsException` (índice fuera de rango), `ArithmeticException` (división por cero)

### ¿Por qué esta distinción?

La idea detrás de esta distinción es filosófica:

- **Checked exceptions** representan problemas externos que no podés evitar completamente. No importa qué tan bien programes, el archivo podría no existir, la red podría fallar, el disco podría llenarse. El compilador te obliga a pensar en estos casos.

- **Unchecked exceptions** representan bugs en tu código. Si accedés a un índice inválido de un arreglo, podrías haber verificado el tamaño antes. Si usás una referencia null, podrías haber verificado que no fuera null. Son evitables, y obligarte a poner try-catch en todos lados sería excesivo.

```{code} java
:caption: Diferencia entre checked y unchecked

// UNCHECKED: No requiere try-catch ni throws
// ArithmeticException hereda de RuntimeException
public static int dividir(int a, int b) {
    return a / b;  // ArithmeticException si b == 0
    // El compilador no te obliga a manejarla
}

// CHECKED: El compilador obliga a manejarla
// IOException NO hereda de RuntimeException
public static String leerArchivo(String ruta) throws IOException {
    // Si no ponés "throws IOException", el código NO compila
    BufferedReader reader = new BufferedReader(new FileReader(ruta));
    return reader.readLine();
}
```

### Regla para recordar

Si una excepción:
- Hereda de `RuntimeException` → es **unchecked** → manejo opcional
- Hereda de `Exception` pero no de `RuntimeException` → es **checked** → manejo obligatorio
- Hereda de `Error` → es un error grave → generalmente no se maneja

### Excepciones Comunes en Java

La siguiente tabla lista las excepciones que encontrarás con más frecuencia:

| Excepción | Tipo | Causa Común |
|:---|:---:|:---|
| `NullPointerException` | Unchecked | Intentar usar una referencia que tiene valor `null`. Equivalente a usar un puntero no inicializado en C. |
| `ArrayIndexOutOfBoundsException` | Unchecked | Acceder a un índice que no existe en el arreglo (negativo o >= length). En C esto era comportamiento indefinido. |
| `ArithmeticException` | Unchecked | División entera por cero. La división de punto flotante por cero no lanza excepción (da infinito o NaN). |
| `NumberFormatException` | Unchecked | Intentar convertir un String que no representa un número válido (ej: `Integer.parseInt("abc")`). |
| `IllegalArgumentException` | Unchecked | Un método recibió un argumento con un valor no permitido. Usada para validar precondiciones. |
| `IllegalStateException` | Unchecked | Un método fue invocado en un momento inapropiado (el objeto no está en estado válido para esa operación). |
| `InputMismatchException` | Unchecked | `Scanner` encontró un tipo diferente al esperado (ej: se esperaba int y se ingresó texto). |
| `IOException` | Checked | Error general de entrada/salida. Incluye problemas de red, disco, etc. |
| `FileNotFoundException` | Checked | El archivo especificado no existe o no se puede abrir. Es subclase de `IOException`. |

:::{tip} Familiarizate con los mensajes
Cuando te aparezca una excepción, leé atentamente el mensaje y el stack trace. Te dicen exactamente qué tipo de error ocurrió, en qué línea, y la secuencia de llamadas que llevó ahí. Esta información es invaluable para debuggear.
:::

## Lanzar Excepciones: throw

Hasta ahora vimos cómo atrapar excepciones que lanza Java. Pero también podés lanzar tus propias excepciones usando la palabra clave `throw` (sin la 's' final).

### ¿Cuándo lanzar una excepción?

Lanzá una excepción cuando tu método no puede cumplir con lo que promete hacer. Por ejemplo:
- Un método que calcula raíz cuadrada recibe un número negativo
- Un método que busca un elemento no lo encuentra
- Un método recibe un argumento null cuando no debería
- Los datos están en un estado inconsistente

```{code} java
:caption: Lanzar excepciones con throw

public static double calcularRaiz(double numero) {
    if (numero < 0) {
        // No podemos calcular raíz de negativo, lanzamos excepción
        throw new IllegalArgumentException(
            "No se puede calcular raíz de número negativo: " + numero
        );
    }
    return Math.sqrt(numero);
}

// Uso:
double r1 = calcularRaiz(16);   // Retorna 4.0
double r2 = calcularRaiz(-5);   // Lanza IllegalArgumentException
```

**Anatomía de un `throw`:**

```java
throw new IllegalArgumentException("mensaje descriptivo");
```

1. `throw`: Palabra clave que indica que vas a lanzar una excepción
2. `new`: Creamos un nuevo objeto (las excepciones son objetos)
3. `IllegalArgumentException`: El tipo de excepción que estamos lanzando
4. `"mensaje descriptivo"`: Un String que explica qué salió mal (muy importante para debugging)

### Comparación con C

En C, si una función no podía cumplir su propósito, tenías que retornar un valor especial y confiar en que el llamador lo verificara:

```c
// En C
double calcular_raiz(double numero) {
    if (numero < 0) {
        return -1.0;  // Valor especial de error
    }
    return sqrt(numero);
}

// El llamador DEBE verificar
double resultado = calcular_raiz(-5);
if (resultado < 0) {
    // Manejar error
}
```

El problema es que `-1.0` podría ser un resultado válido en otro contexto, y el llamador podría olvidar verificar. En Java, la excepción **interrumpe el flujo** y no puede ser ignorada silenciosamente.

### Validación de Precondiciones

Una **precondición** es algo que debe ser verdadero antes de que un método pueda ejecutarse correctamente. Es una buena práctica validar las precondiciones al inicio del método y lanzar excepciones descriptivas si no se cumplen:

```{code} java
:caption: Validación de precondiciones

public static double calcularPromedio(int[] numeros) {
    // Validar precondiciones al inicio del método
    if (numeros == null) {
        throw new NullPointerException("El arreglo no puede ser null");
    }
    if (numeros.length == 0) {
        throw new IllegalArgumentException("El arreglo no puede estar vacío");
    }
    
    // Lógica principal (llegamos acá solo si numeros es válido)
    int suma = 0;
    for (int i = 0; i < numeros.length; i = i + 1) {
        suma = suma + numeros[i];
    }
    return (double) suma / numeros.length;
}
```

**¿Por qué dos excepciones diferentes?**

Fijate que usamos `NullPointerException` para el caso null y `IllegalArgumentException` para el arreglo vacío. Son situaciones diferentes:

- `null` significa "no hay objeto", es la ausencia de un arreglo
- Un arreglo vacío es un objeto válido, pero con cero elementos

Usar excepciones específicas permite al código que atrapa decidir cómo manejar cada caso. Ver {ref}`regla-0x3006` y {ref}`regla-0x3007`.

:::{tip} Lanzamiento Temprano (Fail Fast)
Validá las precondiciones lo antes posible en el método. Es mejor fallar rápido con un mensaje claro ("El arreglo no puede ser null") que fallar después con un error confuso (`NullPointerException` sin contexto al intentar acceder a `numeros.length`).

Este principio se conoce como "fail fast" (fallar rápido): cuanto antes detectes un problema, más fácil será identificar su causa.
:::

## Declarar Excepciones: throws

Cuando un método puede lanzar una excepción **checked** (comprobada), debe declararlo en su firma usando la palabra clave `throws` (con 's' al final). Esto es parte del "contrato" del método: le avisa al código que lo llama que podría tener que lidiar con esa excepción.

### throw vs throws

Es fácil confundir estas dos palabras:

- `throw`: (verbo) Lanza una excepción. Se usa **dentro** del método, seguido de un objeto excepción.
- `throws`: (declara) Indica que el método **puede** lanzar una excepción. Se usa en la **firma** del método, seguido de tipos de excepción.

```java
//                          throws declara qué puede lanzar
//                          ↓
public void metodo() throws IOException {
    // ...
    throw new IOException("error");  // throw lanza la excepción
    //↑
}
```

### ¿Cuándo usar throws?

Solo necesitás `throws` para excepciones **checked**. Las unchecked (RuntimeException y sus subclases) no requieren declaración.

````{mermaid}
:align: center

flowchart TD
    Start([metodoA llama metodoB]) --> Check{metodoB puede<br/>lanzar IOException?}
    Check -->|Sí, checked| Decision{¿Cómo manejar?}
    
    Decision -->|Opción 1| Catch[Capturar con try-catch]
    Decision -->|Opción 2| Throws[Declarar throws en metodoA]
    
    Catch --> Handle[Manejo local del error]
    Throws --> Propagate[Propaga al llamador]
    
    Handle --> End([Fin])
    Propagate --> End
    
    style Check fill:#ffe0e0,stroke:#eb2141
    style Decision fill:#fff3e0,stroke:#f57c00
    style Catch fill:#c8e6c9,stroke:#2e7d32
    style Throws fill:#bbdefb,stroke:#1565c0
````

```{code} java
:caption: Declarar excepciones con throws

// Este método puede lanzar IOException (checked)
// Por eso DEBE declararlo con throws
public static int contarLineas(String rutaArchivo) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo));
    int contador = 0;
    
    while (reader.readLine() != null) {
        contador = contador + 1;
    }
    
    reader.close();
    return contador;
}
```

Si intentás compilar este método **sin** el `throws IOException`, el compilador dará error. Te está forzando a reconocer que este método puede fallar de una manera específica.

### Propagar vs Manejar

Cuando tu método llama a otro que declara `throws` para una excepción checked, tenés exactamente dos opciones. No hay tercera opción; el compilador te obliga a elegir una.

**Opción 1: Manejar la excepción (try-catch)**

Atrapás la excepción y decidís qué hacer con ella. Esto tiene sentido cuando:
- Sabés cómo recuperarte del error
- Podés proveer un valor por defecto razonable
- Querés mostrar un mensaje de error al usuario
- Querés reintentar la operación

```{code} java
:caption: Manejar la excepción

public static void procesarArchivo(String ruta) {
    try {
        int lineas = contarLineas(ruta);
        System.out.println("El archivo tiene " + lineas + " líneas");
    } catch (IOException e) {
        System.out.println("Error al leer el archivo: " + e.getMessage());
        // Acá podrías: usar un valor por defecto, pedir otro archivo, etc.
    }
}
```

**Opción 2: Propagar la excepción (throws)**

Declarás que tu método también puede lanzar esa excepción, pasándole el problema al código que te llamó. Esto tiene sentido cuando:
- No tenés suficiente información para decidir qué hacer
- Tu método es de bajo nivel y la decisión corresponde a niveles superiores
- Querés que el error se maneje de manera centralizada

```{code} java
:caption: Propagar la excepción

public static void procesarArchivo(String ruta) throws IOException {
    // No atrapamos la excepción, la propagamos
    int lineas = contarLineas(ruta);
    System.out.println("El archivo tiene " + lineas + " líneas");
}
// Ahora quien llame a procesarArchivo debe manejar o propagar IOException
```

:::{important} Regla de Propagación
Propagá la excepción cuando no sabés cómo manejarla en el nivel actual. Manejala cuando tenés suficiente contexto para tomar una decisión (mostrar mensaje, reintentar, usar valor por defecto, etc.).

En general, las capas bajas del programa (que hacen el trabajo técnico) propagan, y las capas altas (que interactúan con el usuario) manejan.
:::

### El método main y las excepciones checked

El método `main` es el "tope" de la pila de llamadas. Si una excepción llega hasta `main` sin ser atrapada, el programa termina con un error.

Es una **mala práctica** declarar `throws Exception` en el `main`:

```java
// MAL: lazy, no informa al usuario
public static void main(String[] args) throws Exception {
    // Si algo falla, el usuario ve un stack trace feo
}

// BIEN: maneja el error y muestra mensaje amigable
public static void main(String[] args) {
    try {
        ejecutarPrograma(args);
    } catch (FileNotFoundException e) {
        System.err.println("Error: no se encontró el archivo: " + e.getMessage());
        System.exit(1);
    } catch (IOException e) {
        System.err.println("Error de lectura: " + e.getMessage());
        System.exit(1);
    }
}
```

Ver {ref}`regla-0x3001`.

## El Costo de las Excepciones

Lanzar una excepción en Java es una operación **costosa** en términos de rendimiento computacional. Entender por qué te ayudará a usarlas correctamente.

### ¿Por qué son costosas?

Cuando lanzás una excepción, la JVM (Java Virtual Machine) debe:

1. **Crear el objeto excepción**: Reservar memoria y inicializar el objeto
2. **Capturar el stack trace completo**: Recorrer toda la pila de llamadas activas, registrando cada método, archivo, y número de línea. Si tu programa tiene 20 métodos anidados, se registran los 20.
3. **Buscar el handler**: Subir por la pila buscando un bloque `catch` que coincida con el tipo de excepción
4. **Desenrollar la pila**: Si el handler está varios niveles arriba, "deshacer" las llamadas intermedias

El paso 2 es especialmente costoso. El stack trace es muy útil para debugging, pero generarlo tiene un costo.

### El stack trace: tu mejor amigo para debugging

A pesar del costo, el stack trace es invaluable cuando ocurre un error:

```{code} java
:caption: El stack trace proporciona información valiosa

try {
    metodoA();
} catch (Exception e) {
    e.printStackTrace();  // Imprime toda la traza
}

// Salida típica:
// java.lang.ArithmeticException: / by zero
//     at Ejemplo.metodoC(Ejemplo.java:25)  ← El error ocurrió aquí
//     at Ejemplo.metodoB(Ejemplo.java:20)  ← metodoB llamó a metodoC
//     at Ejemplo.metodoA(Ejemplo.java:15)  ← metodoA llamó a metodoB
//     at Ejemplo.main(Ejemplo.java:10)     ← main llamó a metodoA
```

**Cómo leer un stack trace:**

1. La primera línea dice **qué** pasó: tipo de excepción y mensaje
2. Las líneas siguientes dicen **dónde** pasó, de más específico a más general
3. Leé de arriba hacia abajo: la primera línea con "at" es donde ocurrió el error
4. El nombre del archivo y número de línea te llevan directo al problema

En C, cuando algo fallaba, a menudo solo tenías "Segmentation fault" sin ninguna pista de dónde ocurrió. El stack trace de Java es muchísimo más informativo.

:::{warning} No Usar Excepciones para Control de Flujo
Dado el costo de las excepciones, **nunca** deben usarse para el control de flujo normal del programa. Las excepciones son para situaciones **excepcionales**, no para lógica ordinaria.

```java
// INCORRECTO: usar excepción para control de flujo
// Esto es lento y confuso
try {
    int i = 0;
    while (true) {  // Lazo "infinito" que depende de excepción para terminar
        System.out.println(arreglo[i]);
        i = i + 1;
    }
} catch (ArrayIndexOutOfBoundsException e) {
    // Fin del arreglo - ¡NO es un error, es el fin normal!
}

// CORRECTO: usar condición normal
// Esto es rápido y claro
for (int i = 0; i < arreglo.length; i = i + 1) {
    System.out.println(arreglo[i]);
}
```

La versión incorrecta no solo es más lenta (por el costo de lanzar la excepción), sino que también es confusa: usa un mecanismo de **errores** para una terminación **normal**.
:::

### Cuándo es apropiado usar excepciones

Las excepciones son apropiadas para:
- Errores de los que no se puede recuperar localmente
- Condiciones que raramente ocurren (el archivo no existe, la red falla)
- Violaciones de precondiciones (argumentos inválidos)

Las excepciones **no** son apropiadas para:
- Control de flujo normal (detectar fin de arreglo, fin de archivo normal)
- Condiciones que ocurren frecuentemente como parte del funcionamiento normal
- Reemplazar simples `if` que podrían verificar la condición

## Información de la Excepción

Las excepciones en Java son objetos completos que contienen información útil sobre el error. Saber extraer esta información es clave para el debugging y para proporcionar mensajes útiles al usuario.

### Métodos principales de una excepción

```{code} java
:caption: Métodos de información de excepciones

try {
    int resultado = 10 / 0;
} catch (ArithmeticException e) {
    // getMessage(): Obtiene el mensaje descriptivo del error
    String mensaje = e.getMessage();  // Retorna: "/ by zero"
    
    // getClass().getName(): Obtiene el nombre completo del tipo de excepción
    String tipo = e.getClass().getName();  // "java.lang.ArithmeticException"
    
    // getClass().getSimpleName(): Solo el nombre de la clase, sin paquete
    String tipoSimple = e.getClass().getSimpleName();  // "ArithmeticException"
    
    // printStackTrace(): Imprime la traza completa en el error estándar
    e.printStackTrace();
    
    // getStackTrace(): Obtiene la traza como arreglo para procesamiento
    StackTraceElement[] traza = e.getStackTrace();
    for (StackTraceElement elemento : traza) {
        System.out.println("  en " + elemento.getMethodName() + 
                          " (" + elemento.getFileName() + 
                          ":" + elemento.getLineNumber() + ")");
    }
}
```

### ¿Cuándo usar cada método?

- **getMessage()**: Cuando querés mostrar un mensaje al usuario o registrar en un log
- **printStackTrace()**: Principalmente para debugging durante el desarrollo. En producción, usarías un sistema de logging apropiado.
- **getStackTrace()**: Cuando necesitás procesar la traza programáticamente (por ejemplo, para enviarla a un servidor de errores)
- **getClass().getName()**: Cuando querés saber exactamente qué tipo de excepción ocurrió

:::{note}
El método `toString()` de una excepción combina el tipo y el mensaje: `java.lang.ArithmeticException: / by zero`
:::

## Try-with-resources (Java 7+)

Recordás que en C tenías que cerrar archivos con `fclose()` y liberar memoria con `free()`? Era fácil olvidarlo, especialmente cuando el código tenía múltiples puntos de salida o podía lanzar excepciones.

Java 7 introdujo una sintaxis especial llamada "try-with-resources" que garantiza que ciertos recursos se cierren automáticamente cuando termina el bloque `try`, haya o no excepción.

### Sintaxis básica

```{code} java
:caption: Try-with-resources

// El Scanner se cierra automáticamente al salir del try
try (Scanner scanner = new Scanner(new File("datos.txt"))) {
    while (scanner.hasNextLine()) {
        System.out.println(scanner.nextLine());
    }
} catch (FileNotFoundException e) {
    System.out.println("Archivo no encontrado");
}
// No necesitás llamar a scanner.close() - se hace automáticamente
// Incluso si ocurre una excepción, el scanner se cierra
```

Fijate en la diferencia: el recurso (Scanner) se declara **dentro de los paréntesis** del `try`, no antes.

### Comparación con el enfoque manual

El try-with-resources es equivalente a escribir un try-finally, pero más compacto y menos propenso a errores:

```{code} java
:caption: Equivalente manual con finally

Scanner scanner = null;
try {
    scanner = new Scanner(new File("datos.txt"));
    while (scanner.hasNextLine()) {
        System.out.println(scanner.nextLine());
    }
} catch (FileNotFoundException e) {
    System.out.println("Archivo no encontrado");
} finally {
    if (scanner != null) {
        scanner.close();  // Tenés que acordarte de cerrar
    }
}
```

La versión manual tiene más líneas, y es fácil olvidarse del `finally` o del chequeo de `null`.

### Múltiples recursos

Podés declarar varios recursos separados por punto y coma:

```{code} java
:caption: Múltiples recursos en try-with-resources

try (Scanner entrada = new Scanner(new File("entrada.txt"));
     PrintWriter salida = new PrintWriter("salida.txt")) {
    while (entrada.hasNextLine()) {
        salida.println(entrada.nextLine().toUpperCase());
    }
}
// Ambos se cierran automáticamente, en orden inverso al de apertura
```

### ¿Qué recursos se pueden usar?

Solo pueden usarse en try-with-resources los objetos que implementan la interfaz `AutoCloseable`. Esto incluye:
- `Scanner`
- `BufferedReader`, `BufferedWriter`
- `FileInputStream`, `FileOutputStream`
- `PrintWriter`
- Conexiones a bases de datos
- Y muchos más...

## Encadenamiento de Excepciones

A veces, cuando atrapás una excepción, querés lanzar una diferente (quizás más significativa para el contexto actual). Pero no querés perder la información de la excepción original, que puede ser crucial para debugging.

Java permite **encadenar** excepciones: la nueva excepción puede contener una referencia a la excepción que la causó.

### Preservando la causa

```{code} java
:caption: Encadenamiento de excepciones

public static void procesarConfiguracion(String archivo) {
    try {
        leerArchivo(archivo);
    } catch (IOException e) {
        // Creamos una excepción más específica para nuestro dominio
        // pero preservamos la IOException original como "causa"
        throw new RuntimeException("Error al cargar configuración: " + archivo, e);
        //                                                                    ↑
        //                                             La excepción original se pasa como segundo argumento
    }
}
```

El segundo argumento del constructor de la excepción (`e` en este caso) se convierte en la "causa" de la nueva excepción.

### Recuperando la causa

Quien atrape la excepción externa puede acceder a la causa original:

```{code} java
:caption: Accediendo a la causa de una excepción

try {
    procesarConfiguracion("config.txt");
} catch (RuntimeException e) {
    System.out.println("Error: " + e.getMessage());
    // "Error al cargar configuración: config.txt"
    
    // Obtener la causa original
    Throwable causa = e.getCause();
    if (causa != null) {
        System.out.println("Causa: " + causa.getMessage());
        // Podría ser: "config.txt (No such file or directory)"
    }
    
    // printStackTrace() muestra toda la cadena de causas
    e.printStackTrace();
}
```

### ¿Por qué encadenar excepciones?

1. **Abstracción**: La capa superior no necesita saber los detalles técnicos. Un método de "cargar configuración" lanza una excepción de "configuración", no de "entrada/salida".

2. **Preservación de información**: La causa original no se pierde; está disponible para debugging.

3. **Mejores mensajes**: Podés agregar contexto ("Error al procesar usuario ID 42") mientras mantenés el error técnico original.

Ver {ref}`regla-0x300C` sobre cuándo tiene sentido encadenar vs. simplemente propagar.

## Buenas Prácticas

Esta sección resume las prácticas recomendadas para el manejo de excepciones. Muchas de estas están formalizadas como reglas de estilo de la cátedra.

### 1. No Silenciar Excepciones

Nunca dejes un bloque `catch` vacío. Un catch vacío significa que algo salió mal y nadie se enteró. El programa continúa en un estado potencialmente inconsistente.

```{code} java
:caption: No silenciar excepciones

// INCORRECTO: excepción silenciada
try {
    operacionRiesgosa();
} catch (Exception e) {
    // Vacío - ¡el error se pierde completamente!
    // El programa continúa como si nada hubiera pasado
}

// CORRECTO: al menos registrar el error
try {
    operacionRiesgosa();
} catch (Exception e) {
    System.err.println("Error en operación: " + e.getMessage());
    e.printStackTrace();  // Para debugging
    // Y luego decidir qué hacer: reintentar, usar default, relanzar, etc.
}
```

Ver {ref}`regla-0x300B`.

### 2. Capturar Excepciones Específicas

Evitá capturar `Exception` de forma genérica. Esto atrapa **todo**, incluyendo excepciones que no esperabas y que podrían indicar bugs serios en tu código.

```{code} java
:caption: Captura específica

// INCORRECTO: muy genérico
try {
    procesarArchivo();
} catch (Exception e) {
    System.out.println("Algo salió mal");
    // ¿Qué salió mal? ¿El archivo no existe? ¿No se puede leer?
    // ¿Hubo un NullPointerException por un bug?
}

// CORRECTO: específico
try {
    procesarArchivo();
} catch (FileNotFoundException e) {
    System.out.println("El archivo no existe: " + e.getMessage());
    // Podemos sugerir crear el archivo o pedir otro nombre
} catch (IOException e) {
    System.out.println("Error al leer el archivo: " + e.getMessage());
    // Problema de lectura, quizás reintentar
}
```

Capturar excepciones específicas tiene varias ventajas:
- Podés dar mensajes de error más precisos
- Podés manejar cada caso de manera diferente
- Los bugs (excepciones inesperadas) no se ocultan

Ver {ref}`regla-0x3005`.

### 3. Mensajes Descriptivos

Cuando lances una excepción, incluí información útil en el mensaje. Un buen mensaje de error debería permitir entender qué pasó sin tener que leer el código.

```{code} java
:caption: Mensajes descriptivos

// INCORRECTO: mensaje vago
if (edad < 0) {
    throw new IllegalArgumentException("Valor inválido");
    // ¿Qué valor? ¿Por qué es inválido?
}

// CORRECTO: mensaje informativo
if (edad < 0) {
    throw new IllegalArgumentException("La edad no puede ser negativa: " + edad);
    // Dice qué está mal (edad negativa) y qué valor se recibió (-5)
}

// AÚN MEJOR: incluir contexto adicional si es relevante
if (edad < 0) {
    throw new IllegalArgumentException(
        "La edad no puede ser negativa. Se recibió: " + edad + 
        ". Valores válidos: 0 a 150."
    );
}
```

Un buen mensaje de excepción incluye:
- Qué condición se violó
- Qué valor causó el problema
- Opcionalmente, qué valores serían válidos

### 4. Limpiar Recursos Siempre

Siempre liberá recursos (archivos, conexiones, etc.) en el bloque `finally` o, mejor aún, usá try-with-resources:

```{code} java
:caption: Liberar recursos correctamente

// CORRECTO: recursos liberados siempre con finally
Scanner scanner = null;
try {
    scanner = new Scanner(System.in);
    // usar scanner...
} finally {
    if (scanner != null) {
        scanner.close();
    }
}

// MEJOR: try-with-resources (más compacto, menos propenso a errores)
try (Scanner scanner = new Scanner(System.in)) {
    // usar scanner...
}  // Se cierra automáticamente
```

### 5. Prevenir es Mejor que Curar

Cuando sea posible, verificá las condiciones antes de que ocurra el error, en lugar de depender de excepciones. Esto es más eficiente y más claro:

```{code} java
:caption: Prevención vs captura

// FUNCIONA pero es menos eficiente y menos claro
try {
    int resultado = dividir(a, b);
} catch (ArithmeticException e) {
    resultado = 0;  // valor por defecto
}

// MEJOR: verificar antes
int resultado;
if (b != 0) {
    resultado = dividir(a, b);
} else {
    resultado = 0;  // valor por defecto
}
```

Ver {ref}`regla-0x300A`.

## Comparativa Final: C vs Java

Para cerrar, una comparación completa de cómo manejan los errores ambos lenguajes:

| Aspecto | C | Java |
|:---|:---|:---|
| Mecanismo principal | Códigos de retorno (-1, NULL), `errno` | Excepciones (objetos) |
| Propagación de errores | Manual: cada función debe verificar y propagar | Automática: si no se atrapa, sube sola |
| Información disponible | Código numérico (2, -1) | Mensaje + tipo + stack trace + causa |
| ¿Se puede ignorar? | Sí, muy fácil (no verificar el retorno) | Difícil para checked (el compilador obliga) |
| Overhead de rendimiento | Muy bajo (solo un if) | Mayor (crear objeto, capturar stack trace) |
| Limpieza de recursos | Manual (`free()`, `fclose()`) | `finally` o try-with-resources |
| Debugging | Solo "Segmentation fault" o similar | Stack trace completo con números de línea |
| Separación de flujos | Mezclados (if después de cada llamada) | Separados (try tiene el código normal, catch el de error) |

## Ejercicios de Aplicación

Los siguientes ejercicios te ayudarán a practicar los conceptos de manejo de excepciones.

```{exercise}
:label: ej-validar-edad
Escribí un método `validarEdad(int edad)` que lance `IllegalArgumentException` si la edad es negativa o mayor a 150, y retorne la edad si es válida. El mensaje de error debe indicar claramente qué condición se violó y qué valor se recibió.
```

````solution
:for: ej-validar-edad
```java
public static int validarEdad(int edad) {
    if (edad < 0) {
        throw new IllegalArgumentException(
            "La edad no puede ser negativa. Se recibió: " + edad
        );
    }
    if (edad > 150) {
        throw new IllegalArgumentException(
            "La edad no puede ser mayor a 150. Se recibió: " + edad
        );
    }
    return edad;
}

// Uso:
try {
    int edadValida = validarEdad(25);   // OK, retorna 25
    int edadInvalida = validarEdad(-5); // Lanza excepción
} catch (IllegalArgumentException e) {
    System.out.println("Error: " + e.getMessage());
    // Imprime: "Error: La edad no puede ser negativa. Se recibió: -5"
}
```

**Explicación**: Usamos validaciones separadas para cada condición, con mensajes específicos que incluyen el valor recibido. Esto facilita enormemente el debugging.
````

````{exercise}
:label: ej-try-finally
Analizá el siguiente método y respondé: ¿qué valor retorna `dividir(10, 0)`? ¿Y `dividir(10, 2)`? Explicá por qué.

```java
public int dividir(int a, int b) {
    try {
        return a / b;
    } catch (ArithmeticException e) {
        return -1;
    } finally {
        return 0;
    }
}
```
````

```{solution} ej-try-finally
:class: dropdown

**Ambas llamadas retornan `0`.**

- `dividir(10, 0)`: La división lanza `ArithmeticException`. El catch prepara `return -1`. Pero antes de que el return se ejecute, corre el finally con `return 0`. Este último return "gana".

- `dividir(10, 2)`: La división da 5. El try prepara `return 5`. Pero antes de que se ejecute, corre el finally con `return 0`. Este último return "gana".

**Esto es una muy mala práctica (anti-patrón)**. El `finally` con `return` está "ocultando" todos los resultados reales del método. Nunca pongas `return` dentro de un bloque `finally`.

**Moraleja**: El `finally` siempre se ejecuta antes de que el método retorne, y si el `finally` tiene su propio `return`, ese es el que cuenta.
```

```{exercise}
:label: ej-convertir-numero
Escribí un método `convertirAEntero(String texto)` que convierta un String a int usando `Integer.parseInt()`. Si el texto no es un número válido, debe retornar 0 e imprimir una advertencia. Pensá: ¿qué excepción lanza `parseInt` cuando el formato es inválido?
```

````{solution} ej-convertir-numero
:class: dropdown

```java
public static int convertirAEntero(String texto) {
    int resultado = 0;
    
    try {
        resultado = Integer.parseInt(texto);
    } catch (NumberFormatException e) {
        // parseInt lanza NumberFormatException si el formato es inválido
        System.out.println("Advertencia: '" + texto + "' no es un número válido, usando 0");
        resultado = 0;
    }
    
    return resultado;
}

// Ejemplos:
int n1 = convertirAEntero("42");     // Retorna 42
int n2 = convertirAEntero("abc");    // Imprime advertencia, retorna 0
int n3 = convertirAEntero("3.14");   // Imprime advertencia, retorna 0 (parseInt no acepta decimales)
int n4 = convertirAEntero("");       // Imprime advertencia, retorna 0
```

**Explicación**: `Integer.parseInt()` lanza `NumberFormatException` (que es unchecked, hereda de `RuntimeException`) cuando el String no puede convertirse a entero. Esto incluye textos con letras, números decimales, o cadenas vacías.
````

````{exercise}
:label: ej-orden-catch
El siguiente código no compila. ¿Por qué? ¿Cómo lo arreglarías?

```java
try {
    // código
} catch (Exception e) {
    System.out.println("Error general");
} catch (ArithmeticException e) {
    System.out.println("Error aritmético");
}
```
````

````{solution} ej-orden-catch
:class: dropdown

No compila porque `ArithmeticException` es una **subclase** de `Exception`. El primer `catch (Exception e)` captura **todas** las excepciones, incluyendo `ArithmeticException`. Por lo tanto, el segundo `catch` nunca puede alcanzarse: es **código inalcanzable** (_unreachable code_).

El compilador de Java detecta este error y se niega a compilar, porque sería código muerto que da una falsa sensación de que estás manejando casos específicos.

**Solución**: Invertir el orden, poniendo lo específico antes de lo general:

```java
try {
    // código
} catch (ArithmeticException e) {     // Específico primero
    System.out.println("Error aritmético");
} catch (Exception e) {               // General después
    System.out.println("Error general");
}
```

**Regla**: En una cadena de catches, siempre va de más específico (subclases) a más general (superclases).
````

```{exercise}
:label: ej-dividir-seguro
Escribí un método `dividirSeguro(int[] numeros, int indice, int divisor)` que retorne el resultado de dividir `numeros[indice] / divisor`. El método debe manejar **todos** los posibles errores (arreglo null, índice inválido, división por cero) y retornar 0 en caso de cualquier error, imprimiendo un mensaje específico para cada caso.
```

````{solution} ej-dividir-seguro
:class: dropdown

```java
public static int dividirSeguro(int[] numeros, int indice, int divisor) {
    int resultado = 0;
    
    // Opción 1: Verificar primero (prevención - más claro)
    if (numeros == null) {
        System.out.println("Error: el arreglo es null");
        return 0;
    }
    if (indice < 0 || indice >= numeros.length) {
        System.out.println("Error: índice " + indice + " fuera de rango [0, " + 
                          (numeros.length - 1) + "]");
        return 0;
    }
    if (divisor == 0) {
        System.out.println("Error: no se puede dividir por cero");
        return 0;
    }
    
    return numeros[indice] / divisor;
}

// Opción 2: Usando try-catch (atrapa lo que se escape)
public static int dividirSeguroConCatch(int[] numeros, int indice, int divisor) {
    int resultado = 0;
    
    try {
        if (numeros == null) {
            throw new NullPointerException("El arreglo es null");
        }
        resultado = numeros[indice] / divisor;
    } catch (NullPointerException e) {
        System.out.println("Error: " + e.getMessage());
    } catch (ArrayIndexOutOfBoundsException e) {
        System.out.println("Error: índice " + indice + " fuera de rango");
    } catch (ArithmeticException e) {
        System.out.println("Error: división por cero");
    }
    
    return resultado;
}

// Ejemplos de uso:
int[] arr = {10, 20, 30};
int r1 = dividirSeguro(arr, 1, 2);    // Retorna 10 (20/2)
int r2 = dividirSeguro(arr, 5, 2);    // Error índice, retorna 0
int r3 = dividirSeguro(arr, 1, 0);    // Error división, retorna 0
int r4 = dividirSeguro(null, 0, 1);   // Error null, retorna 0
```

**Nota**: La opción 1 (verificación preventiva) es generalmente preferible cuando sabés exactamente qué puede fallar. Es más eficiente y el código es más claro. La opción 2 con try-catch es útil cuando no podés predecir todas las condiciones de error, o cuando trabajás con código externo que puede lanzar excepciones.
````

````{exercise}
:label: ej-propagacion
Dado el siguiente código, indicá qué se imprime cuando se ejecuta `main`. Seguí el flujo de excepciones paso a paso.

```java
public static void main(String[] args) {
    try {
        System.out.println("1");
        metodoA();
        System.out.println("2");
    } catch (RuntimeException e) {
        System.out.println("3");
    }
    System.out.println("4");
}

public static void metodoA() {
    System.out.println("A");
    metodoB();
    System.out.println("B");
}

public static void metodoB() {
    System.out.println("X");
    throw new RuntimeException("Error en B");
}
```
````

````{solution} ej-propagacion
:class: dropdown

La salida es: **1, A, X, 3, 4**

Paso a paso:

1. `main` entra al try e imprime **"1"**
2. `main` llama a `metodoA()`
3. `metodoA` imprime **"A"**
4. `metodoA` llama a `metodoB()`
5. `metodoB` imprime **"X"**
6. `metodoB` lanza `RuntimeException` - `metodoB` termina abruptamente
7. La excepción "sube" a `metodoA`. Como `metodoA` no tiene try-catch, termina abruptamente. No se imprime "B".
8. La excepción "sube" a `main`. El catch en `main` atrapa `RuntimeException`.
9. Se ejecuta el catch e imprime **"3"**
10. Después del try-catch, se imprime **"4"**

**No se imprime**: "2" (porque la excepción interrumpió el try antes de llegar ahí) ni "B" (porque metodoA terminó abruptamente).
````

- **Schildt, H.** (2022). _Java: A Beginner's Guide_ (9na ed.). McGraw Hill. (Capítulo 9: Exception Handling).
- **Liang, Y. D.** (2017). _Introduction to Java Programming and Data Structures_ (11va ed.). Pearson.
- **Bloch, J.** (2018). _Effective Java_ (3ra ed.). Addison-Wesley Professional. (Capítulo 10: Exceptions).
- **Martin, R. C.** (2009). _Clean Code_. Prentice Hall. (Capítulo 7: Error Handling).

:::{seealso}
- {ref}`regla-0x3001` - Manejo de excepciones checked.
- {ref}`regla-0x3002` - Manejo de excepciones unchecked.
:::
