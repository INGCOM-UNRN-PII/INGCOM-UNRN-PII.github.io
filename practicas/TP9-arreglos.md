---
title: "TP9 - Arreglos III"
description: Trabajo práctico sobre arreglos genéricos y dinámicos con iteradores.
---

# TP9 - Arreglos III

## Forma de entrega

:::{important}
- No olviden completar la plantilla con sus datos y agregar la descripción de cada función.
  Aunque `main` puede no tener este comentario, no está de más registrar qué es lo que el
  `Scanner` recibe.
- Siempre que sea posible, los mensajes de commit deben ser descriptivos.
- Implementen un main que haga un uso de las funciones implementadas, pueden utilizar un `Scanner`.
- La entrada y salida debe estar separada de la función que cumple la consigna, salvo que la consigna lo pida.
- No olviden la utilización de auto-formato, las herramientas de corrección le prestan atención
  a este tema.
- Puede ser necesario completar, cambiar y ajustar la documentación de las funciones pedidas.
- Es posible hacer cambios en la forma de las funciones a implementar, pero para esto, indíquenlo explícitamente
  en el comentario de documentación.
- No olviden crear Tests.
:::

:::{tip}
Pueden usar los atajos [SOS](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP8%20-%20)
para crear preguntas sobre los enunciados individuales, si es posible, no fusionen las preguntas, así es más fácil que
sus compañeros encuentren las respuestas, y pueden otorgarles más puntos a quienes responden.
:::

## Consideraciones generales

:::{note}
En ambos puntos, los `main` son opcionales, creen tests lo más exhaustivos posibles.
:::

:::{seealso}
Pueden usar las funciones de la librería como [`Arrays.copyOf`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html#copyOf(T%5B%5D,int))
para las manipulaciones de los arreglos.
:::

En el paquete `ar.unrn.arreglos`

(tp9-punto1)=
## Punto 1 - ArregloGenérico

Creen una clase `ArregloGenérico` con el mismo comportamiento base que el `ArregloConvencional` 
y que pueda almacenar cualquier tipo de referencia `T`:

- Constructor con tamaño
- Constructor de copia
- `obtener`, equivalente a `[]` del lado derecho.
- `modificar`, equivalente a `[]` del lado izquierdo.
- `comoArreglo` que devuelve un `T[]` con la copia del arreglo interno.
- `largo`, equivalente a `length`
- `toString`, entre corchetes y separados por comas. (x ej. `[1, 2, 3]`)

:::{note}
Recuerden que los arreglos de `Object` pueden contener elementos como `null`, pero el largo del 
arreglo no depende de los elementos que estén o no presentes en el arreglo.

Esto es similar al arreglo del TP6, pero la utilización de referencias y tipos genéricos no permite 
reutilizar de manera directa el código.
:::

:::{important}
Debe implementar la interfaz `Iterable` y la implementación del `Iterator` como clase interna anónima, 
sin el método `remove`.
:::

:::{seealso}
- [`Iterator`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Iterator.html)
- [`Iterable`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Iterable.html)
:::

[SOS ArregloGenérico](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP8%20-%20ArregloGenerico)


(tp9-punto2)=
## Punto 2 - ArregloDinámico

Crear una clase, llamada `ArregloDinamico` que extienda el arreglo del punto anterior para que el 
mismo funcione como el explicado en clase.

Operaciones para agregar:

- `insertar`, este método modifica el arreglo interno agregando un nuevo lugar.
- `borrar`, este método modifica el arreglo interno eliminando el lugar del elemento indicado.

[SOS ArregloDinámico](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP8%20-%20ArregloDinamico)

(tp9-punto3)=
## Punto 3 - ArregloOrdenable

Crear una clase de `ArregloDinamico` llamada `ArregloOrdenable` que acepte elementos `Comparable`.

### Operaciones para agregar

- `ordenar`, este método utiliza el método `compareTo` definido en `Comparable` para ordenar de menor a mayor
  lo que sea que contenga.

[SOS ArregloOrdenable](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP8%20-%20ArregloOrdenable)

(tp9-punto4)=
## Punto 4 - ArregloOrdenado

Crear una clase de `ArregloOrdenable` llamada `ArregloOrdenado`.

Operaciones para agregar:

- `insertar` (como sobrecarga), sin indicar posición.

:::{important}
Todas las operaciones que cambien los valores deben de mantener ordenado el arreglo, en lo posible, sin llamar
a `ordenar`.
:::

### ¿Y qué hacemos con el `insertar` original?

Para el método `insertar`, definida en `ArregloDinamico`, tienen dos opciones:

1. Pueden hacer que la operación lance un tipo de `RuntimeException`, indicando que no es válida
2. O pueden implementarla ignorando el parámetro de posición

:::{warning}
Sea cual fuere la opción elegida, no olviden de su documentación.
:::

[SOS ArregloOrdenado](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP8%20-%20ArregloOrdenado)

## Incisos opcionales

:::{tip}
La magia de los incisos opcionales, es que los tests de la funcionalidad básica ya están 
resueltos, y los mismos se pueden concentrar en lo nuevo.
:::

### Opcional A - ArregloCentrado

Crear una clase, llamada `ArregloCentrado` que extienda `ArregloDinamico`, para que funcione como
el arreglo descrito en clases.

Operaciones para agregar:

- `balancear`, esta operación protegida copia los elementos para que los márgenes sean iguales o
  con una diferencia de 1 entre sí.
- `empujar`, agrega un elemento del lado izquierdo
- `tomar`, extrae un elemento del lado derecho.

### Opcional B - ArregloFragmentado

Extiendan la clase de `ArregloDinamico` para implementar el `ArregloFragmentado` que fue
comentado en clases.

### Opcional C - ArregloPersistente

Creen una clase, llamada `ArregloPersistente` que extienda `ArregloDinámico` y que permita guardar arreglos con
`String`s.

### Opcional D - ArregloFragmentado Genérico

Modificar el Opcional B para que sea genérico.

:::{warning}
Sin usar reflexión o persistencia.
:::

[SOS Opcionales](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP8%20-%20Opcionales)

