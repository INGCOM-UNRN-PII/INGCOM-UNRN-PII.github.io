---
title: Cuestiones de Estilo
description: Reglas de estilo y correcciones generales para código Java en la cátedra de Programación II.
---

# Cuestiones de estilo y correcciones generales

(2025-regla-0x0000)=
## `0x0000` - Sin errores de ortografía y apliquen formato markdown donde sea posible

Usen el [apunte markdown](https://github.com/INGCOM-UNRN-PII/cursada-2025/blob/main/biblio-secundaria/markdown.pdf)
para las cuestiones de formato.

Pero lo más importante...

:::{important}
NO hay excusas, el IDE tiene corrector de ortografía por lo que typos en los identificadores
y documentación no serán aceptados.
:::

(2025-regla-0x0001)=
## `0x0001` - Los nombres de las class van en `CamelloCase`

(2025-regla-0x0002)=
## `0x0002` - Variables, funciones y argumentos van en `dromedarioCase`

(2025-regla-0x0003)=
## `0x0003` - Los identificadores válidos son solo con alfabéticos `[azAZ]`

(2025-regla-0x0004)=
## `0x0004` - Los nombres de las funciones van en `dromedarioCase`

(2025-regla-0x0004-metodos)=
## `0x0004` - Los nombres de los métodos van en `dromedarioCase`

(2025-regla-0x0005)=
## `0x0005` - Los identificadores no son descriptivos; o son letras de contexto matemático o son palabras

(2025-regla-0x0006)=
## `0x0006` - Un solo `return` por función

Solo porque la función, termina con dos puntos de salida diferentes; este y el `return` implícito al finalizar la función.

(2025-regla-0x0007)=
## `0x0007` - La documentación no sigue la forma dada

```{code} java
:caption: Formato básico de documentación Javadoc
/**
* Devuelve el valor absoluto de un número.
* @param numero a eliminar su signo
* @returns el numero sin signo
*/
private static long valorAbsoluto(long numero) {
```

Versión extendida:

```{code} java
:caption: Formato extendido con postcondición
    /**
     * Devuelve el valor absoluto de un número.
     * @param numero a eliminar su signo
     * @returns el numero sin signo
     *      POST: se devuelve un valor con el mismo valor
     *              de numero sin signo.
     */
    private static long valorAbsoluto(long numero) {
```

(2025-regla-0x0008)=
## `0x0008` - Las funciones no van con `printf` o `Scanner`

(2025-regla-0x0008-metodos)=
## `0x0008` - Los métodos no van con `printf` o `Scanner`

A no ser que sea explícitamente su propósito.

(2025-regla-0x0009)=
## `0x0009` - Las constantes van en mayúsculas, con `SNAKE_CASE`

(2025-regla-0x000A)=
## `0x000A` - Un espacio antes y después de los operadores

Esta regla existe casi exclusivamente para exigirles el uso de autoformato, y aunque
el cambio en la prolijidad es realmente bajo, ayuda mucho a no perder de vista código
más complejo.

(2025-regla-0x000B)=
## `0x000B` - Sin usar la asignación compuesta (`+=`,`*=`, etc)

(2025-regla-0x000C)=
## `0x000C` - Sin break y continue en su lugar, usen banderas

(2025-regla-0x000D)=
## `0x000D` - Debe tener el mismo nombre que la class con `Test` al final

(2025-regla-0x000E)=
## `0x000E` - El test no sigue la estructura de test indicada

:::{note}
Debe ser: preparar / ejecutar / comprobar / limpiar. Con identificación y mensajes.
:::

(2025-regla-0x000F)=
## `0x000F` - Una llamada a función en cada caso de prueba

Salvo que estén fuertemente conectadas como en `DivisionLenta`.

(2025-regla-0x0010)=
## `0x0010` - No atajar la excepción si no es posible tomar una decisión

(2025-regla-0x0011)=
## `0x0011` - El main de un programa no debe dejar pasar excepciones de tipo

(2025-regla-0x0012)=
## `0x0012` - Qué familia de excepciones se eligió debe de estar documentada

(2025-regla-0x0013)=
## `0x0013` - Usar argumentos como variables solo si no cambia su significado

(2025-regla-0x0014)=
## `0x0014` - No atajar una excepción lanzada en el mismo bloque

(2025-regla-0x0015)=
## `0x0015` - No convertir excepciones con tipo a sin tipo

(2025-regla-0x0016)=
## `0x0016` - Todas las excepciones que lancemos deben de estar documentadas con `@throws`

Si la misma excepción se lanza en dos contextos diferentes, explicar cada uno de ellos.

Si es posible, incluir casos que están fuera de nuestro control, como por ejemplo, los que
provengan de la librería.

(2025-regla-0x0017)=
## `0x0017` - Las excepciones de tiempo de ejecución deben documentar como evitar su lanzamiento

(2025-regla-0x0018)=
## `0x0018` - Sean específicos con lo que atajan, no está permitido atajar `Exception` o `RuntimeException`

(2025-regla-0x0019)=
## `0x0019` - La inicialización de los atributos va en el constructor

(2025-regla-0x002A)=
## `0x002A` - Las clases van en `CamelloCase` y sus atributos en `dromedarioCase`

(2025-regla-0x002B)=
## `0x002B` - { Los atributos `private` O `protected` con justificación } y nunca `public`

(2025-regla-0x002C)=
## `0x002C` - Los paquetes deben comenzar en `ar.unrn` e ir en minúsculas

(2025-regla-0x002D)=
## `0x002D` - Implementar `equals` requiere implementar `hashCode`

(2025-regla-0x002E)=
## `0x002E` - Al extender, sobreescribir solo para llamar a super no es correcto

Excepto con el constructor.

(2025-regla-0x002F)=
## `0x002F` - Minimizar el código duplicado

(2025-regla-0x0030)=
## `0x0030` - Las clases, atributos y métodos llevan documentación

(2025-regla-0x0031)=
## `0x0031` - Los métodos get/set no pueden ser usados para la lógica del problema

Esto incluye métodos que conceptualmente tengan la misma función.

(2025-regla-0x0032)=
## `0x0032` - La utilización de atributos estáticos debe de estar justificada

(2025-regla-0x0033)=
## `0x0033` - No hacer `import paquete.*`, solo traer lo que se necesita

(2025-regla-0x0034)=
## `0x0034` - No apilen líneas

Todos los bloques llevan sus llaves, y no encadenar más de ~dos llamadas a métodos en una línea.

(2025-regla-0x0035)=
## `0x0035` - Documenten el lanzamiento indirecto de excepciones propias

En especial, cuando se utilizan métodos internos de verificación.

(regla-nuevas)=
# Nuevas reglas y borradores

(2025-regla-0xF000)=
## `0xF000` - No es correcto declarar el lanzamiento de una excepción no controlada

La familia a la que pertenece `ArregloException` no hace que sea correcto declarar su lanzamiento.

(2025-regla-0xF001)=
## `0xF001` - No está permitido atajar para relanzar sin agregar información útil

Esto significa que la mayoría de las situaciones en las que se intenta hacer _probablemente_ no sean correctas.

En este caso, se está perdiendo la información acerca del tipo y causa verdadera.

No está directamente relacionado al I/O, ya que el archivo pudo ser leído correctamente; debiera de ser una excepción propia.

(2025-regla-0xF002)=
## `0xF002` - Atajar para hacer algún tipo de `print` no es gestionar la excepción

(2025-regla-0xF003)=
## `0xF003` - No está permitido lanzar excepciones base, `Exception` o `RuntimeException`

(2025-regla-0xF004)=
## `0xF004` - Aplicar el principio "mejor prevenir que atajar" siempre que sea posible

Si se puede evitar una excepción con un `if`, es preferible hacerlo.

(2025-regla-0xF005)=
## `0xF005` - A la hora de construir cadenas, usar `StringBuilder`

Recordá lo que vimos con respecto a `String` y las concatenaciones. Para estas situaciones,
utilizá [`StringBuilder`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/StringBuilder.html).

(regla-borradores)=
## Reglas en borrador (`0x00`)

:::{warning}
Las siguientes reglas están en proceso de numeración definitiva.
:::

### Las clases van en `CamelloCase`

### Los atributos van en `dromedarioCase`

### Los métodos de tipo `getter`/`setter` no están permitidos

### Eviten todo lo posible los retornos `null`

Y si esto no es posible, empleen `Optional`.

### El código duplicado va en un método privado

Si se encuentran que hay una sección de código que están copiando y pegando en cada una
de las funciones, este debe ir en una función o método separado.

Tengan en cuenta que esto simplifica los tests, al dejar pruebas mucho más 'focalizadas'.

Estas funciones adicionales deben de tener sus pruebas.

Funciones como la verificación de límites, si algún argumento es válido y demás situaciones similares.

### No apilen líneas

Vamos a ir viendo como Java se presta mucho más que C para esto, pero esto significa que si una línea
tiene más de tres llamadas a método hay que dividirla.

### Solo usar `import` a lo que se utiliza, no traer todo junto con `*`

El `import` de todo con `*` no es correcto, no vimos el tema para que exista una regla, pero tendremos una.

Si el `assert` no obtenemos el resultado esperado, el tema es dejar un mensaje de por qué. Esto no siempre es posible, pero algo que al leer el mensaje en la lista de tests sea fácil de identificar qué pasó.

Esto para simplificar los mensajes y que no sea _tan_ laborioso.

(2025-regla-0xE000)=
## `0xE000` - Silenciar una excepción no es la forma de gestionarla

(2025-regla-0xE001)=
## `0xE001` - Mejor prevenir que atajar

Siempre que sea posible, prevenir la excepción en lugar de esperar a que falle.

(2025-regla-0xE002)=
## `0xE002` - Declarar el lanzamiento de una excepción no controlada es un error

Hacer `throws RuntimeException` no es correcto por la familia de excepción a la que pertenece.

(2025-regla-0xE003)=
## `0xE003` - No es correcto concatenar en un lazo

Ya que esto crea una gran cantidad de instancias de `String`. Usá [`StringBuilder`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/StringBuilder.html).

(2025-regla-0xE004)=
## `0xE004` - La implementación de `hashCode` debe emplear la librería

Presente en `Arrays` y `Objects`.

(2025-regla-0xE005)=
## `0xE005` - La implementación de `equals` debe usar Pattern Matching para el cast

Esto para simplificar el código y utilizar la forma correcta de downcast seguro.

(2025-regla-0xE006)=
## `0xE006` - La implementación de `equals` debe ser primero la de `Object`

No la de la clase que implementa.

```{code} java
:caption: Firma correcta de equals
    @Override
    public boolean equals(Object arr) {
```

(2025-regla-0xE007)=
## `0xE007` - `equals` y `hashCode` deben ser implementados juntos o no estar

Es importante respetar el contrato de estos métodos, el cual declara que la implementación de uno de ellos, implica la implementación del otro.

:::{seealso}
- [`Object.hashCode()`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html))
- [`Object.equals(Object)`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Object.html))
:::

(2025-regla-0xE008)=
## `0xE008` - Superposición en el lanzamiento de excepciones

Estás superponiendo dos situaciones que, por lo menos, ameritan un mensaje separado,
e idealmente, dos tipos de excepciones diferentes, de forma que se puedan atajar por separado.

Una cosa es que el arreglo esté vacío, pero otra muy diferente es que sea `null`.

(2025-regla-0xE007-doc)=
## `0xE007` - Al documentar, no se indica el tipo de los argumentos o retorno

Solo se indica cuál es su rol o propósito, redacten de forma que la explicación fluya del mismo, para esto, es necesario que el identificador sea
apropiado.

(2025-regla-0xE009)=
## `0xE009` - Los identificadores, no llevan el tipo (o clase) de lo que procesan

Las funciones no necesitan indicar sobre qué trabajan cuando los argumentos que están a continuación lo indican.

Por ejemplo con arreglos; `sumaArreglo` puede ser simplemente `suma`.

(2025-regla-0xE00A)=
## `0xE00A` - Lanzar excepciones raíz no es correcto

Ya que no es posible atajar la situación específica que las originó.

(2025-regla-0xE00B)=
## `0xE00B` - Algo como 'largo cero' y `null` son dos situaciones bastante diferentes

Que requieren de excepciones distintas para que su tratamiento pueda ser más específico.

(apuntes-practicas)=
# Apuntes específicos de las prácticas

(apuntes-tp3)=
## TP3

Como función y con tipo de retorno, ya sabemos que obtenemos algo.

:::{tip}
Esto debiera de estar en una función aparte, ya que se repite en varias funciones.
Por ejemplo:

```java
public static void verificar(int[] arreglo)
```
:::

Si te fijás, esta función es idéntica a la otra, solo que más simple.
Lo único que realmente cambia, son los límites que debieran ser los representables a `int`:

```{code} java
:caption: Sobrecarga simplificada de obtieneEntero
    public static int obtieneEntero(String mensaje, int intentos) throws NoMasIntentosException{
        return obtieneEntero(mensaje, intentos, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
```

Si el arreglo vino 'nulo', no hay arreglo para mostrar, lo cual debiera de ser una excepción.

:::{important}
Es muy importante ser específico sobre las situaciones que pueden provocar excepciones, lo que describís acá aplica a cualquier función que trabaje con archivos.
:::

(apuntes-tp7)=
## TP7 - CALCULADORA

:::{warning}
Corregir
:::

- Usar `operador.equals("-")` es equivalente a ver si un objeto pertenece a una determinada clase, por lo que su uso en `toString` no es correcto; la solución implementada no sigue el principio OCP.

- Llamar a `toString` es redundante al concatenar.

- La documentación de esta clase es la más importante de todo el ejercicio, debe establecer qué se debe hacer y qué no en los métodos que deben ser implementados; debe ser más que solo describir lo que se ve en el código.

- Revisá la generación de las cadenas. Esta operación no calcula, solo muestra la estructura de la operación que representa.

- Revisá `toString` en `OperacionMultiple` y `OperacionUnaria` para que se resuelva de manera general, sin duplicar código en las operaciones específicas.

- No es el nombre del método apropiado, debe ser `toString` y el mismo no debe de calcular, solo mostrar la «estructura» de la operación.

- `toString` no debe calcular, solo mostrar la «estructura» de la operación.

:::{note}
Es un detalle supermenor, pero está relacionado con que, a ciencia cierta, nunca vas a saber qué implica calcular el lado derecho, en particular, si esto es costoso en tiempo; por lo que calcularlo una vez no estaría de más.

O; si hay un valor aleatorio mezclado que cambia cada vez que calculás :-)
:::

Quizás debas usar algo como:

```{code} java
:caption: Separador de línea portable
                if (contenidoArchivo.charAt(i) == System.lineSeparator()) {
```

Para que pueda procesar archivos creados en cualquier plataforma.

:::{seealso}
[System.lineSeparator()](http://docs.oracle.com/javase/8/docs/api/java/lang/System.html)
:::

(apuntes-tp9)=
## TP9

:::{warning}
La implementación de `equals` es con igualdad, no con los valores de `hashCode` que no tienen garantías de falsas igualdades.
:::

- Revisá las búsquedas de la `Agenda`, que están tomando valores de `Contacto` y haciendo esto fuera de la clase que es responsable de la información, lo que en definitiva, rompe el encapsulamiento.

- Estás comparando las referencias, no su contenido.

- La implementación de búsquedas hecha en la `Agenda` rompe el encapsulamiento de `Contacto`.

:::{tip}
Esto debiera de ser una fábrica, para evitar atiborrar el constructor y simplificar la elección del constructor.
:::

