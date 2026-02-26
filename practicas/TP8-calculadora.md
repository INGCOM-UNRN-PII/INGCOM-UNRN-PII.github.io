# TP8 - Calculadora Orientada a Objetos

## Forma de entrega

- No olviden completar la plantilla con sus datos y agregar la descripción de cada función.
  Aunque `main` puede no tener este comentario, no está de más registrar que es lo que el
  `Scanner` recibe.
- Siempre que sea posible, los mensajes de commit deben ser descriptivos.
- Implementen un main que haga un uso de las funciones implementadas, pueden utilizar un `Scanner`.
- La entrada y salida debe estar separada de la función que cumple la consigna, salvo que la consigna lo pida.
- No olviden la utilización de auto-formato, las herramientas de corrección le prestan atención
  a este tema.
- Puede ser necesario completar, cambiar y ajustar la documentación de las funciones pedidas.
- Es posible hacer cambios en la forma de las funciones a implementar, pero para esto, indiquenló explicitamente
  en el comentario de documentación.
- No olviden crear Tests.

Esta práctica no tiene atajos especificos [SOS - 🆘](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP8%20-%20) 
como el resto del los TP, dado que la mayoría de la consigna está en el diagrama de clases.

## Calculadora

Siguiendo lo que estuvimos viendo en clases como base. Su trabajo ahora es completar todo lo que falta 
y algunas operaciones adicionales.

Observen especial atención al código duplicado, que es una oportunidad para cambiar código de lugar y 
aprovechar la jerarquía de clases.

Para este TP, no es necesario crear un `main`, pero sí tests lo más exhaustivos posible, incluyendo casos 
que involucren multiples operaciones juntas.

Creen puntualmente un caso de prueba en el que se haga una operación más compleja.

Implementen las clases siguiendo el siguiente diagrama (y la estructura de paquetes que está detallada 
a continuación)

Las operaciones deben utilizar tipos `long`.

![Diagrama UML](//www.plantuml.com/plantuml/png/hP8z3zCm48Pt_mgpKLMLc153IylGWGzHjVl5FOKJzbradwkAqlzEmA4VAIuCJ4dkUVDOzoRpf10r1szUw1SCQ3iFn2Pz9UuWGh3EGqeBrKXDLbmxuGrzG_lA66YIHd1Q8lPZX_qJ2P-CFVrcPpkr3hpB7kBBoMiltDvJbKslvhQ7vSQSw-OjGoGO-QzwSwjxvdbB_qdqFdkbpcDlcXw4zXRQY6slk2IiT9D_FgROF-I0KNhbi-ZrW3VVPxFX7LCkPRJxkMBZyMwu54uAhFVJXKTGYIGFtd5bPZmeiKqEDVGPazROK1-vNwWIMj61KWqNlrJOksCdZEoe-AlBRKUxRMySQpvWL8f_fZlm4XTD4fzLQW8NCI3N-0e7Nc__yf-PXybG9345gL8uLcdvTdN_jUI_XZv52RYdwa5BOOLeviZx7FmF)

Si se les ocurren más operaciones, ¡bienvenidas sean!

## Estructura de paquetes

Creen un paquete para la `calculadora`, en la cual estarán las clases `Operación`, `Numero` y la excepción raíz.

La estructura resultante debiera de quedar, siendo la raíz: `ar.unrn`

1. El paquete `calculadora` aloja las clases `Operación`, `Numero` y la `CalculadoraException`
2. Dentro de `ar.unrn.calculadora` paquetes para las familias principales de operaciones:
    1. `unarias`
    2. `binarias`
    3. `multiples`

# Información adicional

Cuestiones misceláneas sobre las operaciones a implementar.

## Opcional - Operaciones lentas

Pueden tomar las operaciones lentas implementadas en el TP2 y usarlas en lugar de los operadores tradicionales.

## Sobre el `Número` de tipo `Aleatorio`

Este debe de recibir en su construcción los parámetros que limiten el rango de valores.

Para que las cuentas no den continuamente cualquier valor, agreguen un método para "refrescar" el valor
aleatorio.

## Sobre `toString` y el tipo de cálculos que se pueden construir.

El objetivo de este método, es obtener la representación de la fórmula que se representa.

No hacer el cálculo, esto porque hay operaciones que pueden fallar (la división, por ejemplo) y porque al final
no tenemos ningún control sobre lo que hay adentro.

Otro detalle es el hecho de que probablemente sea necesario meter paréntesis a todo ¯\\\_(ツ)\_/¯.

Por ejemplo, con una operación de `Suma`:

```java
Operacion operandoA = new Numero(10L);
Operacion operandoB = new Numero(20L);
Operacion operandoC = new Suma(operandoA, operandoB);

System.out.println(operandoC);
```

Mostraría `(10+20)`

Y una operación más compleja;

```java
Operacion operando = new Suma(
        new Producto(
                new Numero(3L),
                new Numero(2L)),
        new Numero(40L);

System.out.println(operando);
```

Mostraría `((3*2)+40)`

Y en un caso todavía más complejo:

```java
Operacion op = new Suma(
        new Suma(
                new Multiplicacion(
                        new Numero(4),
                        new Numero(5)),
                new Numero(2)),
        new Invertir(
                new Numero(20)));
```

Esto daria como resultado algo como:

```
(((4x5)+2)+(-(20)))
```

La mejor implementación de este método, consiste en una que aplica a todas las clases de nivel "medio":

- `OperacionUnaria`
- `OperacionBinaria`
- `OperacionMultiple`
- `Numero`

Tengan presente que cambia y que no entre las operaciones de cada tipo en particular.

## Sobre los tests

Solo es necesario construir tests para las clases en donde se implementan los métodos abstractos.

Esto es; si `toString` es implementado en cada una de las `Operacion` individuales, es necesario un test para cada una.
Pero si la implementación "está más arriba", en las clases del medio, solo son necesario crear tests para esas tres
implementaciones.

## Detalles sobre los diagramas de clase

En el siguiente diagrama hay un par de cuestiones de notación para
tener en cuenta:

* \+ es público (`public`)
* \# es protegido (`protected`)
* \- es privado (`private`)

Las letras en la fila del nombre de la clase también
tienen su significado.

* (A) Abstracto (`abstract class`)
* (C) Clase (`class`)

Finalmente, los métodos abstractos, se ven en '_itálica_'
en el diagrama.

El código fuente del diagráma está en `diagrama.plantuml`. 