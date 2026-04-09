---
title: "Sintaxis de Java: Control de Flujo"
description: Estudio técnico sobre operadores, lógica booleana y estructuras de control (lazos) en Java.
---

(sintaxis-de-java-control-de-flujo)=
# Sintaxis de Java: Control de Flujo

La sintaxis de Java para el control de flujo hereda la claridad de C, pero introduce salvaguardas críticas para la robustez del software. En esta materia, nos enfocamos en el uso preciso de estas estructuras para construir algoritmos eficientes y seguros.

El **control de flujo** se refiere a las instrucciones que determinan el orden en que se ejecutan las sentencias de un programa. Sin estructuras de control, un programa ejecutaría sus instrucciones de forma secuencial, de arriba hacia abajo, sin posibilidad de tomar decisiones o repetir acciones. Las estructuras de control permiten:

- **Decisiones**: Ejecutar diferentes bloques de código según condiciones (`if`, `switch`)
- **Iteración**: Repetir bloques de código múltiples veces (`for`, `while`, `do-while`)
- **Transferencia**: Alterar el flujo dentro de estructuras (`break`, `continue`, `return`)

(logica-booleana-y-cortocircuito)=
## Lógica Booleana y Cortocircuito

Una de las diferencias fundamentales con C es que en Java el tipo `boolean` no es un entero. Por lo tanto, estructuras como `if (1)` son errores de compilación.

En C, cualquier valor distinto de cero se interpreta como verdadero y cero como falso. Esto permite escribir código como `if (x)` donde `x` es un entero, lo cual puede ser fuente de errores sutiles. Java elimina esta ambigüedad: las condiciones **deben** ser expresiones booleanas.

```{code} java
:caption: Diferencia entre C y Java en condiciones

// En C esto es válido (pero peligroso):
// if (x = 5) { ... }  // Asigna 5 a x, evalúa como verdadero

// En Java esto NO compila (error de tipos):
int x = 5;
// if (x) { }        // Error: incompatible types: int cannot be converted to boolean
if (x != 0) { }      // Correcto: expresión booleana explícita
```

(operadores-relacionales)=
### Operadores Relacionales

Los operadores relacionales comparan dos valores y devuelven un resultado `boolean` (`true` o `false`). Son idénticos a los de C:

| Operador | Significado | Ejemplo | Resultado si `x = 5` |
|:---:|:---|:---|:---:|
| `==` | Igual a | `x == 5` | `true` |
| `!=` | Distinto de | `x != 5` | `false` |
| `<` | Menor que | `x < 5` | `false` |
| `>` | Mayor que | `x > 5` | `false` |
| `<=` | Menor o igual que | `x <= 5` | `true` |
| `>=` | Mayor o igual que | `x >= 5` | `true` |

:::{warning} Comparación de Objetos
El operador `==` compara **referencias** cuando se usa con objetos, no su contenido. Para comparar el contenido de objetos (como cadenas), usá el método `equals()`. Este tema se profundiza cuando se estudian objetos.

```java
String a = new String("hola");
String b = new String("hola");
System.out.println(a == b);       // false (diferentes objetos en memoria)
System.out.println(a.equals(b));  // true (mismo contenido)
```
:::

(operadores-logicos)=
### Operadores Lógicos

Los operadores lógicos combinan expresiones booleanas para formar condiciones más complejas:

| Operador | Significado | Ejemplo | Descripción |
|:---:|:---|:---|:---|
| `&&` | AND lógico (con cortocircuito) | `a && b` | `true` solo si ambos son `true` |
| `\|\|` | OR lógico (con cortocircuito) | `a \|\| b` | `true` si al menos uno es `true` |
| `!` | NOT lógico (negación) | `!a` | Invierte el valor booleano |

Estos operadores son idénticos a los de C, pero solo funcionan con operandos booleanos (no con enteros).

```{code} java
:caption: Uso de operadores lógicos

boolean esMayor = true;
boolean tieneDNI = false;

// AND: ambas condiciones deben ser verdaderas
boolean puedeVotar = esMayor && tieneDNI;  // false

// OR: al menos una condición debe ser verdadera
boolean puedeEntrar = esMayor || tieneDNI;  // true

// NOT: invierte el valor
boolean esmenor = !esMayor;  // false
```

:::{note} Convenciones de nombres lógicos

Para facilitar el uso y lectura de código que utilice este tipo de variables, utilicen prefijos como:

 -   `es` -> para indicar pertenencia, identidad o estado intrínseco (ej. `esAdministrador`, `esValido`).
 -   `puede` -> para indicar capacidad, autorización o permisos para ejecutar una acción (ej. `puedeEscribir`, `puedeEliminar`).
 -   `tiene` -> para indicar posesión de un atributo, propiedad o relación de composición (ej. `tieneHijos`, `tieneSaldo`).
 -   `esta` -> para indicar un estado transitorio o condición actual de la instancia (ej. `estaActivo`, `estaVacio`).
 -   `ha` / `fue` -> para indicar la finalización de una operación, mutación o un evento pasado (ej. `haCargado`, `fueModificado`).

:::

#### Tabla de Verdad de los Operadores Lógicos

| `a` | `b` | `a && b` | `a \|\| b` | `!a` |
|:---:|:---:|:---:|:---:|:---:|
| `true` | `true` | `true` | `true` | `false` |
| `true` | `false` | `false` | `true` | `false` |
| `false` | `true` | `false` | `true` | `true` |
| `false` | `false` | `false` | `false` | `true` |

(evaluacion-en-cortocircuito-short-circuit-evaluation)=
### Evaluación en Cortocircuito (_Short-circuit Evaluation_)

Los operadores `&&` (AND) y `||` (OR) realizan una evaluación **perezosa** o en cortocircuito. Esto significa que Java deja de evaluar la expresión tan pronto como puede determinar el resultado final:

- **`a && b`**: Si `a` es `false`, `b` **no se evalúa** porque el resultado es necesariamente `false` (en un AND, si uno es falso, todo es falso).
- **`a || b`**: Si `a` es `true`, `b` **no se evalúa** porque el resultado es necesariamente `true` (en un OR, si uno es verdadero, todo es verdadero).

Esta característica no es solo una optimización de rendimiento; es una herramienta fundamental para escribir código seguro.

````{mermaid}

flowchart LR
    subgraph AND["Evaluación de a && b"]
        A1[Evaluar a] --> C1{a es false?}
        C1 -->|Sí| R1[Resultado: false]
        C1 -->|No| A2[Evaluar b]
        A2 --> R2[Resultado: valor de b]
    end
    
    subgraph OR["Evaluación de a || b"]
        A3[Evaluar a] --> C2{a es true?}
        C2 -->|Sí| R3[Resultado: true]
        C2 -->|No| A4[Evaluar b]
        A4 --> R4[Resultado: valor de b]
    end
    
    style C1 fill:#ffe0e0,stroke:#eb2141
    style C2 fill:#ffe0e0,stroke:#eb2141
    style R1 fill:#ffcdd2,stroke:#c62828
    style R3 fill:#c8e6c9,stroke:#2e7d32
````

```{figure} 05/cortocircuito.svg
:label: fig-cortocircuito
:width: 95%

Diagrama de evaluación en cortocircuito: cuando la primera condición determina el resultado, la segunda no se evalúa.
```

:::{important} Seguridad con Cortocircuito
Esta característica es vital para evitar excepciones. Permite verificar una precondición en la misma línea que la operación:

```java
// Prevenir NullPointerException
if (objeto != null && objeto.hacerAlgo()) {
    // Si objeto es null, objeto.hacerAlgo() nunca se ejecuta
}

// Prevenir división por cero
if (divisor != 0 && dividendo / divisor > 10) {
    // Si divisor es 0, la división nunca se ejecuta
}

// Prevenir ArrayIndexOutOfBoundsException
if (indice >= 0 && indice < arreglo.length && arreglo[indice] == valor) {
    // Se verifican los límites antes de acceder al arreglo
}
```

Si el objeto es `null`, la segunda parte nunca se ejecuta, evitando un `NullPointerException`.
:::

#### Operadores Lógicos Sin Cortocircuito

Java también proporciona los operadores `&` y `|` (un solo símbolo) que evalúan **ambos operandos siempre**, incluso si el resultado ya está determinado. Estos se usan raramente y principalmente cuando los operandos tienen efectos secundarios que deben ejecutarse.

```{code} java
:caption: Diferencia entre & y &&

int x = 0;
boolean resultado1 = (false && (++x > 0));  // x sigue siendo 0
boolean resultado2 = (false & (++x > 0));   // x ahora es 1 (se evaluó ++x)
```

:::{tip} Recomendación
Usá siempre `&&` y `||` (con cortocircuito) a menos que tengas una razón específica para evaluar ambos operandos.
:::

(estructuras-condicionales)=
## Estructuras Condicionales

Las estructuras condicionales permiten que un programa tome **decisiones** durante su ejecución. En lugar de seguir un camino lineal, el programa puede elegir entre diferentes bloques de código según el valor de una condición.

En términos de flujo de programa, las estructuras condicionales crean **bifurcaciones**: puntos donde el camino de ejecución se divide en dos o más alternativas.

(la-sentencia-if)=
### La Sentencia `if`

La sentencia `if` es la estructura condicional más básica. Ejecuta un bloque de código **solo si** una condición es verdadera.

```{code} java
:caption: Estructura básica del if

if (condicion) {
    // código que se ejecuta si condicion es true
}
// La ejecución continúa aquí, independientemente de la condición
```

**Anatomía del `if`:**
- La palabra reservada `if`
- Una **condición** entre paréntesis que debe evaluar a `boolean`
- Un **bloque de código** entre llaves que se ejecuta si la condición es `true`

La condición debe ser una expresión que evalúe a `boolean`. A diferencia de C, no se puede usar un entero directamente.

````{mermaid}

flowchart TD
    Start([Código anterior]) --> Cond{condición}
    Cond -->|true| Body[Bloque del if]
    Body --> End([Código posterior])
    Cond -->|false| End
    
    style Cond fill:#ffe0e0,stroke:#eb2141
    style Body fill:#c8e6c9,stroke:#2e7d32
````

```{code} java
:caption: Ejemplo de if simple

int temperatura = 35;

if (temperatura > 30) {
    System.out.println("Hace calor");
}
// Si temperatura es 25, no se imprime nada y el programa continúa
```

:::{note} Llaves Obligatorias
Aunque Java permite omitir las llaves cuando el bloque tiene una sola sentencia, en este curso **siempre** usamos llaves para evitar errores al agregar más sentencias posteriormente. Consultá la regla de estilo correspondiente.

```java
// Permitido pero NO recomendado:
if (x > 0)
    System.out.println("Positivo");

// Recomendado (siempre con llaves):
if (x > 0) {
    System.out.println("Positivo");
}
```
:::

(la-sentencia-if-else)=
### La Sentencia `if-else`

Permite ejecutar un bloque alternativo cuando la condición es falsa. Esto crea una **bifurcación completa**: el programa siempre ejecutará exactamente uno de los dos bloques.

```{code} java
:caption: Estructura if-else

if (condicion) {
    // código si condicion es true (rama verdadera)
} else {
    // código si condicion es false (rama falsa)
}
```

````{mermaid}

flowchart TD
    Start([Código anterior]) --> Cond{condición}
    Cond -->|true| BodyT[Bloque if]
    Cond -->|false| BodyF[Bloque else]
    BodyT --> End([Código posterior])
    BodyF --> End
    
    style Cond fill:#ffe0e0,stroke:#eb2141
    style BodyT fill:#c8e6c9,stroke:#2e7d32
    style BodyF fill:#bbdefb,stroke:#1565c0
````

```{code} java
:caption: Ejemplo de if-else

int edad = 17;

if (edad >= 18) {
    System.out.println("Es mayor de edad");
} else {
    System.out.println("Es menor de edad");
}
// Siempre se imprime exactamente uno de los dos mensajes
```

:::{tip} Elegir la Condición Positiva
Cuando sea posible, escribí la condición de forma que el caso "normal" o más común esté en el bloque `if` y el caso excepcional en el `else`. Esto mejora la legibilidad.

```java
// Preferible: caso positivo primero
if (saldo >= monto) {
    realizarTransferencia();
} else {
    mostrarErrorSaldoInsuficiente();
}
```
:::

(la-sentencia-if-else-if-else)=
### La Sentencia `if-else if-else`

Cuando se necesitan evaluar múltiples condiciones mutuamente excluyentes, se encadenan sentencias `else if`. Esta estructura crea una **cadena de decisiones** donde solo se ejecuta el primer bloque cuya condición sea verdadera.

```{code} java
:caption: Estructura if-else if-else

if (condicion1) {
    // código si condicion1 es true
} else if (condicion2) {
    // código si condicion1 es false y condicion2 es true
} else if (condicion3) {
    // código si condicion1 y condicion2 son false y condicion3 es true
} else {
    // código si ninguna condición anterior es true (opcional pero recomendado)
}
```

**Características importantes:**
- Las condiciones se evalúan **en orden**, de arriba hacia abajo
- La ejecución entra **solo** al primer bloque cuya condición es verdadera
- Una vez que se ejecuta un bloque, se salta al código después de toda la estructura
- El `else` final es opcional pero recomendado para manejar casos no previstos

````{mermaid}

flowchart TD
    Start([Inicio]) --> Cond1{nota >= 90?}
    Cond1 -->|Sí| Act1["clasificacion = 'Sobresaliente'"]
    Cond1 -->|No| Cond2{nota >= 80?}
    Cond2 -->|Sí| Act2["clasificacion = 'Muy bueno'"]
    Cond2 -->|No| Cond3{nota >= 70?}
    Cond3 -->|Sí| Act3["clasificacion = 'Bueno'"]
    Cond3 -->|No| Cond4{nota >= 60?}
    Cond4 -->|Sí| Act4["clasificacion = 'Regular'"]
    Cond4 -->|No| Act5["clasificacion = 'Insuficiente'"]
    
    Act1 --> End([Fin])
    Act2 --> End
    Act3 --> End
    Act4 --> End
    Act5 --> End
    
    style Cond1 fill:#ffe0e0,stroke:#eb2141
    style Cond2 fill:#ffe0e0,stroke:#eb2141
    style Cond3 fill:#ffe0e0,stroke:#eb2141
    style Cond4 fill:#ffe0e0,stroke:#eb2141
    style Act1 fill:#c8e6c9,stroke:#2e7d32
    style Act2 fill:#c8e6c9,stroke:#2e7d32
    style Act3 fill:#c8e6c9,stroke:#2e7d32
    style Act4 fill:#c8e6c9,stroke:#2e7d32
    style Act5 fill:#c8e6c9,stroke:#2e7d32
````

```{code} java
:caption: Ejemplo de clasificación de notas

int nota = 75;
String clasificacion;

if (nota >= 90) {
    clasificacion = "Sobresaliente";
} else if (nota >= 80) {
    clasificacion = "Muy bueno";
} else if (nota >= 70) {
    clasificacion = "Bueno";
} else if (nota >= 60) {
    clasificacion = "Regular";
} else {
    clasificacion = "Insuficiente";
}
// Si nota es 75, solo se ejecuta clasificacion = "Bueno"
// Las condiciones >= 60 nunca se evalúan porque ya se encontró una verdadera
```

:::{warning} Orden de las Condiciones
Las condiciones se evalúan en orden. Una vez que una condición es verdadera, el resto no se evalúa. Por eso, es importante ordenarlas desde la más específica a la más general.

```java
// INCORRECTO: la primera condición siempre es verdadera para notas >= 60
if (nota >= 60) {
    clasificacion = "Regular";  // Una nota de 95 sería "Regular"
} else if (nota >= 90) {
    clasificacion = "Sobresaliente";  // Nunca se alcanza
}

// CORRECTO: condiciones ordenadas de más restrictiva a menos restrictiva
if (nota >= 90) {
    clasificacion = "Sobresaliente";
} else if (nota >= 60) {
    clasificacion = "Regular";
}
```
:::

(condicionales-anidados)=
### Condicionales Anidados

Es posible colocar estructuras `if` dentro de otras estructuras `if`. Esto se llama **anidamiento** y permite expresar condiciones más complejas.

```{code} java
:caption: Ejemplo de if anidado

int edad = 25;
boolean tieneLicencia = true;

if (edad >= 18) {
    System.out.println("Es mayor de edad");
    if (tieneLicencia) {
        System.out.println("Puede conducir");
    } else {
        System.out.println("Necesita obtener licencia");
    }
} else {
    System.out.println("Es menor de edad, no puede conducir");
}
```

:::{warning} Evitar Anidamiento Excesivo
El anidamiento profundo (más de 2-3 niveles) dificulta la lectura del código. En esos casos, considerá refactorizar usando métodos auxiliares o condiciones combinadas con operadores lógicos.

```java
// Difícil de leer (anidamiento profundo):
if (a) {
    if (b) {
        if (c) {
            hacerAlgo();
        }
    }
}

// Más claro (condición combinada):
if (a && b && c) {
    hacerAlgo();
}
```
:::

(el-operador-ternario)=
### El Operador Ternario `? :`

El operador ternario (también llamado **operador condicional**) es una forma compacta de expresar una selección entre dos valores. Es el único operador de Java que toma tres operandos.

```{code} java
:caption: Sintaxis del operador ternario

resultado = condicion ? valorSiTrue : valorSiFalse;
```

**Funcionamiento:**
1. Se evalúa la `condicion`
2. Si es `true`, el resultado es `valorSiTrue`
3. Si es `false`, el resultado es `valorSiFalse`

```{code} java
:caption: Ejemplo del operador ternario

int edad = 20;
String estado = (edad >= 18) ? "Mayor" : "Menor";

// Equivale a:
String estado2;
if (edad >= 18) {
    estado2 = "Mayor";
} else {
    estado2 = "Menor";
}
```

El operador ternario es una **expresión** (devuelve un valor), no una sentencia. Por eso puede usarse en cualquier lugar donde se espere un valor:

```{code} java
:caption: Usos del operador ternario como expresión

// En una asignación
int maximo = (a > b) ? a : b;

// En una llamada a método
System.out.println("El valor es " + ((x > 0) ? "positivo" : "no positivo"));

// En un return
return (lista.isEmpty()) ? valorPorDefecto : lista.get(0);
```

:::{tip} Uso del Operador Ternario
Usá el operador ternario solo para expresiones simples donde mejore la legibilidad. Para lógica compleja, preferí `if-else` tradicional.

```java
// Apropiado: expresión simple
int absoluto = (x < 0) ? -x : x;

// NO apropiado: demasiado complejo
String resultado = (a > b) ? ((a > c) ? "A" : "C") : ((b > c) ? "B" : "C");
// Mejor usar if-else if-else
```
:::

:::{warning} El Operador Ternario No es un `if`
El operador ternario siempre debe producir un valor. No puede usarse para ejecutar código condicionalmente sin asignar el resultado.

```java
// INCORRECTO: el operador ternario produce un valor que no se usa
(condicion) ? metodo1() : metodo2();  // Puede funcionar pero es mal estilo

// CORRECTO: usar if-else para ejecutar código condicionalmente
if (condicion) {
    metodo1();
} else {
    metodo2();
}
```
:::

(la-sentencia-switch)=
### La Sentencia `switch`

El `switch` permite seleccionar entre múltiples alternativas basándose en el valor de una expresión. Es útil cuando se compara una variable contra varios valores constantes, especialmente cuando hay muchas alternativas.

**Diferencia conceptual con `if-else if`:**
- `if-else if`: evalúa **condiciones** booleanas (pueden ser cualquier expresión)
- `switch`: compara un **valor** contra constantes conocidas

El `switch` es más legible cuando se tienen muchas comparaciones de igualdad contra el mismo valor.

#### Switch Clásico (Sentencia)

La versión clásica del `switch` requiere `break` para evitar el "fall-through" (caída en cascada hacia el siguiente caso).

```{code} java
:caption: Estructura del switch clásico

switch (expresion) {
    case valor1:
        // código para valor1
        break;
    case valor2:
        // código para valor2
        break;
    case valor3:
    case valor4:
        // código para valor3 O valor4 (casos agrupados)
        break;
    default:
        // código si ningún caso coincide
        break;
}
```

**Anatomía del `switch`:**
- `expresion`: El valor a evaluar (debe ser de tipo compatible)
- `case valor:`: Etiqueta que marca el código a ejecutar si `expresion == valor`
- `break`: Termina la ejecución del switch y salta al código posterior
- `default`: Caso opcional que se ejecuta si ningún `case` coincide

````{mermaid}

flowchart TD
    Start([Inicio]) --> Eval[Evaluar expresión]
    Eval --> C1{= valor1?}
    C1 -->|Sí| A1[Código para valor1]
    C1 -->|No| C2{= valor2?}
    C2 -->|Sí| A2[Código para valor2]
    C2 -->|No| C3{= valor3?}
    C3 -->|Sí| A3[Código para valor3]
    C3 -->|No| Def[Código default]
    
    A1 --> End([Fin])
    A2 --> End
    A3 --> End
    Def --> End
    
    style C1 fill:#ffe0e0,stroke:#eb2141
    style C2 fill:#ffe0e0,stroke:#eb2141
    style C3 fill:#ffe0e0,stroke:#eb2141
    style A1 fill:#c8e6c9,stroke:#2e7d32
    style A2 fill:#c8e6c9,stroke:#2e7d32
    style A3 fill:#c8e6c9,stroke:#2e7d32
    style Def fill:#bbdefb,stroke:#1565c0
````

```{code} java
:caption: Ejemplo de switch con días de la semana

int diaSemana = 3;
String nombreDia;

switch (diaSemana) {
    case 1:
        nombreDia = "Lunes";
        break;
    case 2:
        nombreDia = "Martes";
        break;
    case 3:
        nombreDia = "Miércoles";
        break;
    case 4:
        nombreDia = "Jueves";
        break;
    case 5:
        nombreDia = "Viernes";
        break;
    case 6:
    case 7:
        nombreDia = "Fin de semana";  // Casos 6 y 7 comparten el mismo código
        break;
    default:
        nombreDia = "Día inválido";
        break;
}
```

:::{warning} El `break` es Obligatorio
Si se omite el `break`, la ejecución "cae" al siguiente caso (_fall-through_). Esto puede causar errores lógicos difíciles de detectar. En este curso, siempre usá `break` al final de cada caso (excepto cuando agrupamos casos intencionalmente).

```java
// PELIGRO: fall-through no intencional
switch (x) {
    case 1:
        System.out.println("Uno");
        // Falta break! Si x=1, imprime "Uno" Y "Dos"
    case 2:
        System.out.println("Dos");
        break;
}
```
:::

**Tipos de datos permitidos en el switch clásico:**
- **Tipos enteros**: `byte`, `short`, `int`, `char` (no `long`)
- **`String`**: desde Java 7 se pueden usar cadenas
- **Tipos enumerados** (`enum`): se estudian más adelante

```{code} java
:caption: Switch con String

String comando = "salir";

switch (comando) {
    case "iniciar":
        System.out.println("Iniciando...");
        break;
    case "pausar":
        System.out.println("Pausando...");
        break;
    case "salir":
        System.out.println("Saliendo...");
        break;
    default:
        System.out.println("Comando no reconocido");
        break;
}
```

#### Switch Moderno (Expresión, Java 14+)

El switch moderno utiliza la flecha `->` para eliminar el riesgo de caída accidental y puede devolver un valor directamente. Esta versión es más segura y concisa.

**Diferencias con el switch clásico:**
- No requiere `break` (no hay fall-through)
- Puede usarse como **expresión** (devuelve un valor)
- Múltiples valores en un solo `case` separados por comas
- Sintaxis más concisa con flechas `->`

```{code} java
:caption: Switch expresión con sintaxis de flecha

int diaSemana = 3;
String nombreDia = switch (diaSemana) {
    case 1 -> "Lunes";
    case 2 -> "Martes";
    case 3 -> "Miércoles";
    case 4 -> "Jueves";
    case 5 -> "Viernes";
    case 6, 7 -> "Fin de semana";  // Múltiples valores en un caso
    default -> "Día inválido";
};  // Nota el punto y coma: es una expresión que termina en asignación
```

Cuando se necesita ejecutar varias sentencias en un caso, se usa un bloque con `yield` para retornar el valor:

```{code} java
:caption: Switch con bloques múltiples líneas y yield

String descripcion = switch (estado) {
    case ACTIVO -> "Sistema funcionando";
    case PENDIENTE -> {
        System.out.println("Procesando pendiente...");
        yield "Esperando confirmación";  // yield retorna el valor del bloque
    }
    case ERROR -> {
        registrarError();
        notificarAdministrador();
        yield "Sistema en error";
    }
    default -> "Estado desconocido";
};
```

:::{note} Exhaustividad
Cuando el switch se usa como expresión (asignando su resultado a una variable), debe ser **exhaustivo**: cubrir todos los valores posibles o incluir un `default`. El compilador verifica esto.
:::

(estructuras-de-repeticion-lazos)=
## Estructuras de Repetición: Lazos

En Java, utilizamos el término **lazos** para referirnos a los bucles o ciclos. Los lazos permiten ejecutar un bloque de código **múltiples veces** sin tener que escribirlo repetidamente.

Los lazos son fundamentales para:
- Procesar colecciones de datos (arreglos, listas)
- Repetir acciones hasta que se cumpla una condición
- Implementar algoritmos iterativos
- Generar secuencias de valores

Cada tipo de lazo tiene una semántica específica según el punto de evaluación de la condición:
- **Lazo `for`**: cuando se conoce la cantidad de iteraciones
- **Lazo `while`**: cuando se repite mientras una condición sea verdadera (evaluación al inicio)
- **Lazo `do-while`**: cuando se necesita al menos una ejecución (evaluación al final)

```{figure} 05/comparacion_lazos.svg
:label: fig-comparacion-lazos
:width: 95%

Comparación del flujo de ejecución entre for, while y do-while. Observá cuándo se evalúa la condición en cada caso.
```

(el-lazo-for)=
### El Lazo `for`

El lazo `for` es ideal cuando se conoce de antemano la cantidad de iteraciones. Consta de tres partes separadas por punto y coma: inicialización, condición y actualización.

```{code} java
:caption: Estructura del lazo for

for (inicializacion; condicion; actualizacion) {
    // código que se repite mientras condicion sea true
}
```

**Las tres partes del `for`:**
1. **Inicialización**: Se ejecuta **una sola vez**, antes de la primera iteración. Generalmente declara e inicializa la variable de control.
2. **Condición**: Se evalúa **antes de cada iteración**. Si es `true`, se ejecuta el cuerpo; si es `false`, el lazo termina.
3. **Actualización**: Se ejecuta **después de cada iteración**, antes de volver a evaluar la condición. Generalmente modifica la variable de control.

````{mermaid}

flowchart TD
    Start([Inicio]) --> Init[1. Inicialización: i = 0]
    Init --> Cond{2. Condición: i < 5?}
    Cond -->|true| Body[3. Cuerpo del lazo]
    Body --> Update[4. Actualización: i = i + 1]
    Update --> Cond
    Cond -->|false| End([Fin])
    
    style Init fill:#c8e6c9,stroke:#2e7d32
    style Cond fill:#ffe0e0,stroke:#eb2141
    style Body fill:#bbdefb,stroke:#1565c0
    style Update fill:#e1bee7,stroke:#6a1b9a
````

La ejecución sigue este orden:
1. Se ejecuta la **inicialización** (una sola vez)
2. Se evalúa la **condición**
3. Si es `true`, se ejecuta el cuerpo del lazo
4. Se ejecuta la **actualización**
5. Se vuelve al paso 2

```{code} java
:caption: Ejemplo de for que imprime números del 1 al 5

for (int i = 1; i <= 5; i = i + 1) {
    System.out.println(i);
}
// Imprime: 1, 2, 3, 4, 5

// Traza de ejecución:
// i=1: condición 1<=5 es true, imprime 1, actualiza i=2
// i=2: condición 2<=5 es true, imprime 2, actualiza i=3
// i=3: condición 3<=5 es true, imprime 3, actualiza i=4
// i=4: condición 4<=5 es true, imprime 4, actualiza i=5
// i=5: condición 5<=5 es true, imprime 5, actualiza i=6
// i=6: condición 6<=5 es false, termina el lazo
```

```{code} java
:caption: Ejemplo de for con conteo descendente

for (int i = 10; i >= 0; i = i - 1) {
    System.out.println(i);
}
// Imprime: 10, 9, 8, ..., 1, 0
```

```{code} java
:caption: Ejemplo de for con incremento diferente

// Imprimir números pares del 0 al 20
for (int i = 0; i <= 20; i = i + 2) {
    System.out.println(i);
}
// Imprime: 0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20
```

:::{tip} Alcance de la Variable de Control
Declarar la variable de control dentro del `for` limita su **alcance** (_scope_) exclusivamente al cuerpo del lazo, liberando la memoria y evitando conflictos de nombres una vez que el lazo termina.

```java
for (int i = 0; i < 10; i = i + 1) {
    System.out.println(i);  // i existe aquí
}
// System.out.println(i);  // Error: i no existe fuera del for

// Si necesitás el valor final:
int i;
for (i = 0; i < 10; i = i + 1) {
    // ...
}
System.out.println("Valor final: " + i);  // i = 10
```
:::

#### Partes Opcionales del `for`

Todas las partes del `for` son opcionales (aunque se deben mantener los punto y coma):

```{code} java
:caption: Variantes del for

// Variable ya inicializada afuera
int i = 0;
for (; i < 10; i = i + 1) {
    System.out.println(i);
}

// Actualización dentro del cuerpo
for (int j = 0; j < 10; ) {
    System.out.println(j);
    j = j + 1;
}

// Lazo infinito (usar con cuidado)
// for (;;) {
//     // Se ejecuta indefinidamente
// }
```

(el-lazo-while)=
### El Lazo `while`

El lazo `while` evalúa la condición **antes** de ejecutar el cuerpo. Si la condición es falsa desde el inicio, el cuerpo nunca se ejecuta (cero iteraciones).

```{code} java
:caption: Estructura del lazo while

while (condicion) {
    // código que se repite mientras condicion sea true
}
```

**Características del `while`:**
- La condición se evalúa **antes** de cada iteración
- Si la condición es inicialmente `false`, el cuerpo no se ejecuta nunca
- No tiene inicialización ni actualización integradas (se deben hacer por separado)
- Es ideal cuando no se sabe cuántas iteraciones serán necesarias

````{mermaid}

flowchart TD
    Start([Inicio]) --> Cond{Condición}
    Cond -->|true| Body[Cuerpo del lazo]
    Body --> Cond
    Cond -->|false| End([Fin])
    
    style Cond fill:#ffe0e0,stroke:#eb2141
    style Body fill:#bbdefb,stroke:#1565c0
    
    Note1[/"Puede ejecutarse 0 veces"/]
    style Note1 fill:#fff3e0,stroke:#f57c00
    End --> Note1
````

```{code} java
:caption: Ejemplo de while para sumar números hasta llegar a un límite

int suma = 0;
int numero = 1;

while (suma < 100) {
    suma = suma + numero;
    numero = numero + 1;
}

System.out.println("Suma final: " + suma);
System.out.println("Último número sumado: " + (numero - 1));

// Traza: suma=0, agrega 1 (suma=1), agrega 2 (suma=3), agrega 3 (suma=6)...
// Continúa hasta que suma >= 100
```

```{code} java
:caption: Ejemplo de while que no ejecuta ninguna iteración

int x = 10;

while (x < 5) {  // Condición es false desde el inicio
    System.out.println(x);  // Nunca se ejecuta
    x = x + 1;
}

System.out.println("Lazo terminado");  // Se imprime directamente
```

:::{important} Asegurar la Terminación
Un error común es crear un lazo infinito olvidando modificar las variables de la condición dentro del cuerpo:

```java
// PELIGRO: lazo infinito
int i = 0;
while (i < 10) {
    System.out.println(i);
    // Falta: i = i + 1;  // i siempre es 0, condición siempre true
}
```
:::

:::{tip} Patrón de Búsqueda con Bandera
En este curso, para implementar búsquedas que pueden terminar anticipadamente, usamos una variable booleana (bandera) en lugar de `break`. Consultá la {ref}`regla-0x5002`.
:::

```{figure} 05/patron_bandera.svg
:label: fig-patron-bandera
:width: 85%

Patrón recomendado: búsqueda con bandera booleana en lugar de break. La bandera `encontrado` controla la terminación anticipada del lazo.
```

```{code} java
:caption: Búsqueda con bandera (patrón recomendado)

boolean encontrado = false;  // Bandera: indica si encontramos el valor
int indice = 0;

// El lazo continúa mientras no hayamos encontrado Y queden elementos
while (indice < cantidad && !encontrado) {
    if (elementos[indice] == valorBuscado) {
        encontrado = true;  // Encontramos el valor, el lazo terminará
        // NO incrementamos indice para conservar la posición encontrada
    } else {
        indice = indice + 1;  // Avanzar solo si no encontramos
    }
}

// Después del lazo, verificamos qué pasó
if (encontrado) {
    System.out.println("Encontrado en posición: " + indice);
} else {
    System.out.println("No encontrado");
}
```

Este patrón tiene varias ventajas:
- El lazo tiene una única condición de salida clara
- El estado final es predecible: `encontrado` indica el resultado, `indice` indica la posición
- Es fácil de depurar y entender

(el-lazo-do-while)=
### El Lazo `do-while`

El lazo `do-while` evalúa la condición **después** de ejecutar el cuerpo. Esto garantiza que el cuerpo se ejecuta **al menos una vez**, independientemente del valor de la condición.

```{code} java
:caption: Estructura del lazo do-while

do {
    // código que se ejecuta al menos una vez
} while (condicion);  // Nota: termina con punto y coma
```

**Características del `do-while`:**
- El cuerpo se ejecuta **antes** de evaluar la condición
- Siempre hay al menos una iteración (mínimo garantizado: 1)
- La condición se evalúa al final de cada iteración
- Si la condición es `false` después de la primera ejecución, el lazo termina

````{mermaid}

flowchart TD
    Start([Inicio]) --> Body[Cuerpo del lazo]
    Body --> Cond{Condición}
    Cond -->|true| Body
    Cond -->|false| End([Fin])
    
    style Cond fill:#ffe0e0,stroke:#eb2141
    style Body fill:#bbdefb,stroke:#1565c0
    
    Note1[/"Se ejecuta al menos 1 vez"/]
    style Note1 fill:#c8e6c9,stroke:#2e7d32
    End --> Note1
````

```{code} java
:caption: Ejemplo de do-while para validar entrada

int opcion;
do {
    System.out.println("Menú:");
    System.out.println("1. Opción A");
    System.out.println("2. Opción B");
    System.out.println("0. Salir");
    System.out.print("Seleccione: ");
    opcion = scanner.nextInt();
} while (opcion != 0);

// El menú se muestra al menos una vez antes de verificar si el usuario quiere salir
```

```{code} java
:caption: Ejemplo de do-while para validar rango

int numero;
do {
    System.out.print("Ingrese un número entre 1 y 10: ");
    numero = scanner.nextInt();
    
    if (numero < 1 || numero > 10) {
        System.out.println("Valor fuera de rango. Intente nuevamente.");
    }
} while (numero < 1 || numero > 10);

System.out.println("Número válido ingresado: " + numero);
```

:::{note} Cuándo Usar do-while
El `do-while` es útil cuando necesitás ejecutar una acción antes de verificar si debe repetirse. Casos típicos:

- **Menús interactivos**: mostrar opciones antes de pedir selección
- **Validación de entrada**: pedir un dato al menos una vez, repetir si es inválido
- **Intentos con reintentos**: ejecutar una operación al menos una vez, repetir si falla

Si podrías necesitar cero iteraciones, usá `while` en su lugar.
:::

#### Diferencia entre `while` y `do-while`

```{code} java
:caption: Comparación con condición inicialmente falsa

int x = 100;

// Con while: no se ejecuta ninguna vez
while (x < 10) {
    System.out.println("while: " + x);
    x = x + 1;
}

// Con do-while: se ejecuta exactamente una vez
do {
    System.out.println("do-while: " + x);  // Imprime "do-while: 100"
    x = x + 1;
} while (x < 10);
```

(comparacion-de-lazos)=
### Comparación de Lazos

| Lazo | Evaluación de Condición | Ejecución Mínima | Uso Típico | Estructura Equivalente en C |
|:---:|:---|:---:|:---|:---:|
| `for` | Antes de cada iteración | 0 veces | Cantidad de iteraciones conocida | Idéntica |
| `while` | Antes de cada iteración | 0 veces | Cantidad de iteraciones desconocida | Idéntica |
| `do-while` | Después de cada iteración | 1 vez | Se requiere al menos una ejecución | Idéntica |

**Guía para elegir el lazo correcto:**

```{code} java
:caption: Cuándo usar cada tipo de lazo

// FOR: cuando conocés la cantidad de iteraciones
// "Hacer algo N veces" o "recorrer de A a B"
for (int i = 0; i < 10; i = i + 1) {
    // Exactamente 10 iteraciones (0 a 9)
}

// WHILE: cuando no sabés cuántas iteraciones, pero podría ser cero
// "Mientras haya datos" o "hasta que se cumpla X"
while (hayMasDatos()) {
    procesarSiguiente();
}

// DO-WHILE: cuando necesitás al menos una iteración
// "Hacer al menos una vez, repetir si es necesario"
do {
    pedirDato();
} while (!datoValido());
```

(equivalencia-entre-for-y-while)=
### Equivalencia entre `for` y `while`

Todo lazo `for` puede reescribirse como `while` y viceversa. La elección entre uno u otro es principalmente una cuestión de **claridad y convención**:

- Usá `for` cuando la estructura tiene una inicialización, condición y actualización claras
- Usá `while` cuando solo tenés una condición de continuación

```{code} java
:caption: Lazo for

for (int i = 0; i < 10; i = i + 1) {
    System.out.println(i);
}
```

```{code} java
:caption: Equivalente con while

int i = 0;                    // Inicialización (antes del lazo)
while (i < 10) {              // Condición
    System.out.println(i);
    i = i + 1;                // Actualización (al final del cuerpo)
}
```

````{mermaid}

flowchart LR
    subgraph FOR["for (init; cond; upd) { cuerpo }"]
        F1[init] --> F2{cond}
        F2 -->|true| F3[cuerpo]
        F3 --> F4[upd]
        F4 --> F2
        F2 -->|false| F5([fin])
    end
    
    subgraph WHILE["init; while (cond) { cuerpo; upd }"]
        W1[init] --> W2{cond}
        W2 -->|true| W3[cuerpo]
        W3 --> W4[upd]
        W4 --> W2
        W2 -->|false| W5([fin])
    end
    
    style F2 fill:#ffe0e0,stroke:#eb2141
    style W2 fill:#ffe0e0,stroke:#eb2141
````

:::{note} Convención de Uso
Aunque son equivalentes, la comunidad Java tiene convenciones:
- **`for`**: recorrer rangos numéricos, iterar sobre arreglos con índice
- **`while`**: esperar eventos, procesar hasta condición de fin, entrada de usuario
- **`do-while`**: validación de entrada, menús interactivos
:::

(lazos-anidados)=
### Lazos Anidados

Es posible colocar un lazo dentro de otro. El lazo interno se ejecuta **completamente** por cada iteración del lazo externo. Esto es útil para trabajar con estructuras bidimensionales (matrices) o generar combinaciones.

```{code} java
:caption: Ejemplo de lazos anidados (tabla de multiplicar)

for (int fila = 1; fila <= 5; fila = fila + 1) {
    for (int columna = 1; columna <= 5; columna = columna + 1) {
        int producto = fila * columna;
        System.out.print(producto + "\t");  // \t es tabulador
    }
    System.out.println();  // Nueva línea después de cada fila
}

// Salida:
// 1    2    3    4    5
// 2    4    6    8    10
// 3    6    9    12   15
// 4    8    12   16   20
// 5    10   15   20   25
```

**Análisis de la ejecución:**
- El lazo externo (`fila`) itera 5 veces
- Por cada iteración del lazo externo, el lazo interno (`columna`) itera 5 veces
- Total de iteraciones del cuerpo interno: 5 × 5 = 25 veces

```{code} java
:caption: Traza de lazos anidados

for (int i = 1; i <= 3; i = i + 1) {
    System.out.println("Lazo externo: i = " + i);
    for (int j = 1; j <= 2; j = j + 1) {
        System.out.println("  Lazo interno: j = " + j);
    }
}

// Salida:
// Lazo externo: i = 1
//   Lazo interno: j = 1
//   Lazo interno: j = 2
// Lazo externo: i = 2
//   Lazo interno: j = 1
//   Lazo interno: j = 2
// Lazo externo: i = 3
//   Lazo interno: j = 1
//   Lazo interno: j = 2
```

````{mermaid}

flowchart TD
    Start([Inicio]) --> Init1[i = 1]
    Init1 --> Cond1{i <= 3?}
    Cond1 -->|true| Init2[j = 1]
    Init2 --> Cond2{j <= 2?}
    Cond2 -->|true| Body[Cuerpo interno]
    Body --> Update2[j = j + 1]
    Update2 --> Cond2
    Cond2 -->|false| Update1[i = i + 1]
    Update1 --> Cond1
    Cond1 -->|false| End([Fin])
    
    style Cond1 fill:#ffe0e0,stroke:#eb2141
    style Cond2 fill:#ffcc80,stroke:#ef6c00
    style Body fill:#bbdefb,stroke:#1565c0
````

:::{warning} Complejidad de Lazos Anidados
La cantidad total de iteraciones de lazos anidados se **multiplica**. Un lazo de N iteraciones dentro de otro de M iteraciones ejecuta el cuerpo interno N × M veces. Esto puede afectar significativamente el rendimiento con valores grandes.

```java
// 1000 × 1000 = 1,000,000 de iteraciones
for (int i = 0; i < 1000; i = i + 1) {
    for (int j = 0; j < 1000; j = j + 1) {
        // Este código se ejecuta UN MILLÓN de veces
    }
}
```
:::

:::{warning} Punto Flotante en Lazos
Nunca uses tipos `double` o `float` como variables de control en un lazo. Debido a los errores de precisión de la representación IEEE 754, una condición como `i != 1.0` podría no cumplirse nunca, generando un lazo infinito.

```java
// PELIGRO: puede ser un lazo infinito
for (double d = 0.0; d != 1.0; d = d + 0.1) {
    System.out.println(d);
    // d nunca será exactamente 1.0 debido a errores de redondeo
    // d tomará valores como: 0.1, 0.2, 0.30000000000000004, ...
}

// CORRECTO: usar enteros o comparar con tolerancia
for (int i = 0; i < 10; i = i + 1) {
    double d = i * 0.1;
    System.out.println(d);
}

// O usar comparación con margen de error
for (double d = 0.0; d < 0.99; d = d + 0.1) {
    System.out.println(d);
}
```

Este problema se hereda de C y es común a todos los lenguajes que usan punto flotante IEEE 754.
:::

(lazos-infinitos-y-como-evitarlos)=
### Lazos Infinitos y Cómo Evitarlos

Un **lazo infinito** es un lazo cuya condición nunca se vuelve falsa, causando que el programa se ejecute indefinidamente (hasta que se lo detenga externamente o se agote algún recurso).

```{code} java
:caption: Ejemplos de lazos infinitos (errores comunes)

// Error 1: Condición siempre verdadera
while (true) {
    System.out.println("Infinito!");
}

// Error 2: Variable de control no modificada
int i = 0;
while (i < 10) {
    System.out.println(i);
    // Falta: i = i + 1;
}

// Error 3: Actualización en dirección incorrecta
for (int j = 0; j < 10; j = j - 1) {  // j decrece, nunca llega a 10
    System.out.println(j);
}

// Error 4: Condición que no puede volverse falsa
int k = 1;
while (k != 10) {  // k solo toma valores impares
    System.out.println(k);
    k = k + 2;  // k: 1, 3, 5, 7, 9, 11, 13, ... nunca es 10
}
```

**Cómo evitar lazos infinitos:**
1. Verificar que la variable de control se modifique en cada iteración
2. Verificar que la modificación acerque la condición a ser falsa
3. Usar condiciones con `<` o `>` en lugar de `!=` cuando sea posible
4. Evitar punto flotante como variables de control

(sentencias-de-control-de-flujo-break-y-continue)=
## Sentencias de Control de Flujo: `break` y `continue`

Java proporciona las sentencias `break` y `continue` para alterar el flujo normal de los lazos. Estas sentencias permiten salir anticipadamente de un lazo o saltar a la siguiente iteración.

:::{important} Restricción del Curso
En este curso, **no se permite** el uso de `break` ni `continue` en lazos. En su lugar, se deben usar variables booleanas (banderas) para controlar el flujo. El único contexto donde `break` está permitido es en sentencias `switch`. Consultá la {ref}`regla-0x5002` para más detalles y ejemplos.

Esta restricción existe porque:
1. El uso de banderas hace el flujo más explícito y predecible
2. Facilita la depuración y el análisis del código
3. Prepara para entornos donde `break` en lazos está prohibido por estándares de codificación
:::

(la-sentencia-break)=
### La Sentencia `break`

La sentencia `break` termina inmediatamente el lazo más interno que la contiene. La ejecución continúa en la primera sentencia después del lazo.

```{code} java
:caption: Ejemplo de break (uso general, NO permitido en este curso)

// Búsqueda con break - NO USAR en este curso
for (int i = 0; i < 100; i = i + 1) {
    if (valores[i] == buscado) {
        System.out.println("Encontrado en: " + i);
        break;  // Sale del lazo inmediatamente
    }
}

// Equivalente con bandera (USAR en este curso)
boolean encontrado = false;
int i = 0;
while (i < 100 && !encontrado) {
    if (valores[i] == buscado) {
        encontrado = true;
    } else {
        i = i + 1;
    }
}
if (encontrado) {
    System.out.println("Encontrado en: " + i);
}
```

(la-sentencia-continue)=
### La Sentencia `continue`

La sentencia `continue` salta a la siguiente iteración del lazo, omitiendo el resto del código en la iteración actual. En un `for`, ejecuta la actualización antes de verificar la condición.

```{code} java
:caption: Ejemplo de continue (uso general, NO permitido en este curso)

// Imprimir solo impares con continue - NO USAR en este curso
for (int i = 0; i < 10; i = i + 1) {
    if (i % 2 == 0) {
        continue;  // Salta los números pares
    }
    System.out.println(i);  // Solo imprime impares
}

// Equivalente sin continue (USAR en este curso)
for (int i = 0; i < 10; i = i + 1) {
    if (i % 2 != 0) {  // Condición invertida
        System.out.println(i);
    }
}
```

(uso-permitido-de-break-en-switch)=
### Uso Permitido de `break`: en `switch`

El único lugar donde `break` está permitido en este curso es dentro de sentencias `switch`, donde es necesario para evitar el fall-through:

```{code} java
:caption: break permitido en switch

switch (opcion) {
    case 1:
        hacerOpcion1();
        break;  // Permitido y necesario
    case 2:
        hacerOpcion2();
        break;  // Permitido y necesario
    default:
        hacerDefault();
        break;
}
```

(comparativa-de-seguridad-con-c)=
## Comparativa de Seguridad con C

Java hereda la sintaxis de control de flujo de C pero añade verificaciones que previenen errores comunes:

(1-asignacion-en-condiciones)=
### 1. Asignación en Condiciones

En C, `if (x = 5)` es un error común que asigna 5 a x y evalúa a verdadero (porque 5 es distinto de cero). En Java, esto falla en compilación (a menos que x sea `boolean`), previniendo errores lógicos sutiles.

```{code} java
:caption: Protección contra asignación accidental

int x = 0;

// En C esto compila y siempre es verdadero (asigna 5 a x)
// En Java esto NO compila:
// if (x = 5) { }  // Error: incompatible types: int cannot be converted to boolean

// Forma correcta:
if (x == 5) { }  // Comparación
```

(2-inicializacion-obligatoria)=
### 2. Inicialización Obligatoria

Java exige que las variables locales estén inicializadas antes de ser usadas en cualquier estructura de control. El compilador analiza todos los caminos posibles.

```{code} java
:caption: Verificación de inicialización

int resultado;

if (condicion) {
    resultado = 10;
}

// Error de compilación: variable resultado might not have been initialized
// System.out.println(resultado);

// Correcto: inicializar en todas las ramas
if (condicion) {
    resultado = 10;
} else {
    resultado = 0;
}
System.out.println(resultado);  // OK
```

(3-tipo-booleano-estricto)=
### 3. Tipo Booleano Estricto

Las condiciones deben ser expresiones `boolean`. No se puede usar un entero, referencia a objeto, o cualquier otro tipo directamente como condición.

```{code} java
:caption: Condiciones estrictamente booleanas

int cantidad = 5;
String texto = "hola";

// Estos NO compilan en Java (sí en C):
// if (cantidad) { }      // Error: int no es boolean
// while (texto) { }      // Error: String no es boolean

// Forma correcta:
if (cantidad != 0) { }     // Comparación explícita
while (texto != null) { }  // Comparación explícita
```

(4-alcance-de-variables-en-lazos)=
### 4. Alcance de Variables en Lazos

En Java moderno, declarar la variable de control en el `for` la limita al ámbito del lazo, evitando conflictos de nombres y uso accidental posterior.

```{code} java
:caption: Alcance limitado de variables de control

for (int i = 0; i < 10; i = i + 1) {
    System.out.println(i);
}
// i no existe aquí - evita errores

for (int i = 0; i < 5; i = i + 1) {
    // Esta i es diferente, sin conflicto
    System.out.println(i);
}
```

Estas diferencias hacen que Java sea más seguro que C para programación a gran escala, aunque la sintaxis sea visualmente similar.

(ejercicios-control)=
## Ejercicios

```{exercise}
:label: ej-logic-short
Dada la expresión `(x != 0) && (y / x > 1)`, explicá por qué nunca lanzará una `ArithmeticException` (excepción de división por cero) incluso si `x` es cero.
````

````{solution} ej-logic-short
:class: dropdown

Gracias al **cortocircuito** del operador `&&`, si `x` es 0, la primera condición `(x != 0)` evalúa a `false`. En ese momento, Java detiene la evaluación de la expresión completa porque ya sabe que el resultado será `false` (en un AND, si uno es falso, todo es falso).

Por lo tanto, la segunda parte `(y / x > 1)` **nunca se ejecuta** cuando `x` es 0, evitando así la división por cero.

Este patrón es muy común para proteger operaciones que podrían fallar:
```java
// Proteger división
if (divisor != 0 && dividendo / divisor > limite) { ... }

// Proteger acceso a arreglo
if (indice >= 0 && indice < arr.length && arr[indice] == valor) { ... }

// Proteger referencia nula
if (objeto != null && objeto.metodo()) { ... }
```
````

::::{exercise}
:label: ej-for-while
Reescribí el siguiente lazo `for` como un lazo `while` equivalente:

`for (int i = 10; i > 0; i = i - 2) { System.out.println(i); }`
::::

::::{solution} ej-for-while
:class: dropdown

```java
int i = 10;              // Inicialización: antes del while
while (i > 0) {          // Condición: en el while
    System.out.println(i);
    i = i - 2;           // Actualización: al final del cuerpo
}
```

**Análisis de la conversión:**
1. La **inicialización** (`int i = 10`) se mueve antes del `while`
2. La **condición** (`i > 0`) se coloca en el `while`
3. La **actualización** (`i = i - 2`) se coloca al final del cuerpo del lazo

Ambos lazos imprimen: 10, 8, 6, 4, 2

**Nota**: Cuando la variable se declara fuera del `while`, su alcance es mayor (existe después del lazo). En el `for` original, `i` deja de existir al terminar el lazo.
::::

::::{exercise}
:label: ej-busqueda-bandera 

Escribí un fragmento de código que busque el primer número negativo en una secuencia de 10 números ingresados por el usuario, usando una bandera booleana (sin usar `break`).
::::

::::{solution} ej-busqueda-bandera
:class:dropdown

```java
Scanner scanner = new Scanner(System.in);
boolean encontrado = false;  // Bandera: ¿encontramos un negativo?
int posicion = 0;            // Posición donde se encontró
int contador = 0;            // Contador de números ingresados

while (contador < 10 && !encontrado) {
    System.out.print("Ingrese número " + (contador + 1) + ": ");
    int numero = scanner.nextInt();
    
    if (numero < 0) {
        encontrado = true;
        posicion = contador;
        // NO incrementamos contador para conservar la posición correcta
    } else {
        contador = contador + 1;
    }
}

// Verificar resultado después del lazo
if (encontrado) {
    System.out.println("Primer negativo en posición: " + (posicion + 1));
} else {
    System.out.println("No se encontraron números negativos");
}
```

**Puntos clave del patrón:**
- La condición `!encontrado` permite salir del lazo anticipadamente
- Solo incrementamos `contador` cuando NO encontramos (para conservar la posición)
- Después del lazo, la bandera indica si la búsqueda fue exitosa
::::

::::{exercise}
:label: ej-do-while 

¿Cuál es la diferencia fundamental entre `while` y `do-while`? Escribí un ejemplo donde sea más apropiado usar `do-while` que `while`.
::::

::::{solution} ej-do-while
:class: dropdown

La diferencia fundamental es el **momento de evaluación de la condición**:
- `while` evalúa la condición **antes** de ejecutar el cuerpo (puede ejecutarse 0 veces)
- `do-while` evalúa la condición **después** (se ejecuta **al menos 1 vez**)

Un caso apropiado para `do-while` es la **validación de entrada** donde necesitamos al menos un intento:

```java
int numero;
do {
    System.out.print("Ingrese un número entre 1 y 10: ");
    numero = scanner.nextInt();
    
    if (numero < 1 || numero > 10) {
        System.out.println("Valor inválido. Intente nuevamente.");
    }
} while (numero < 1 || numero > 10);

System.out.println("Número válido: " + numero);
```

**¿Por qué `do-while` es mejor aquí?**
- Siempre necesitamos pedir **al menos un número** antes de verificar si es válido
- Con `while`, tendríamos que duplicar el código de entrada o usar una variable auxiliar inicializada con un valor inválido
- El `do-while` expresa naturalmente la semántica de "hacer algo y repetir si es necesario"
::::

::::{exercise}
:label: ej-switch-vs-if

¿Cuándo es preferible usar `switch` en lugar de `if-else if`? Reescribí el siguiente código usando switch:

```java
String tipo;
if (codigo == 1) {
    tipo = "Estudiante";
} else if (codigo == 2) {
    tipo = "Docente";
} else if (codigo == 3) {
    tipo = "Administrativo";
} else {
    tipo = "Desconocido";
}
```
::::

::::{solution} ej-switch-vs-if
:class: dropdown

**Cuándo preferir `switch`:**
- Cuando se compara **una variable** contra **múltiples valores constantes**
- Cuando hay **más de 2-3 alternativas**
- Cuando los valores son del tipo compatible con switch (int, char, String, enum)

**Código reescrito con switch:**

```java
// Switch clásico
String tipo;
switch (codigo) {
    case 1:
        tipo = "Estudiante";
        break;
    case 2:
        tipo = "Docente";
        break;
    case 3:
        tipo = "Administrativo";
        break;
    default:
        tipo = "Desconocido";
        break;
}

// Switch moderno (Java 14+) - más conciso
String tipo = switch (codigo) {
    case 1 -> "Estudiante";
    case 2 -> "Docente";
    case 3 -> "Administrativo";
    default -> "Desconocido";
};
```

El `switch` es más legible cuando hay muchas comparaciones de igualdad contra el mismo valor.
::::

::::{exercise}
:label: ej-lazos-anidados
Escribí un programa que imprima el siguiente patrón usando lazos anidados:

```
*
**
***
****
*****
```
::::

````{solution} ej-lazos-anidados
:class: dropdown

```java
for (int fila = 1; fila <= 5; fila = fila + 1) {
    for (int columna = 1; columna <= fila; columna = columna + 1) {
        System.out.print("*");
    }
    System.out.println();  // Nueva línea después de cada fila
}
```

**Análisis:**
- El lazo externo controla las **filas** (de 1 a 5)
- El lazo interno controla las **columnas** (de 1 hasta el número de fila actual)
- En la fila 1, el lazo interno ejecuta 1 vez (imprime 1 asterisco)
- En la fila 2, el lazo interno ejecuta 2 veces (imprime 2 asteriscos)
- Y así sucesivamente...

**Traza:**
| Fila | Columnas | Asteriscos |
|:---:|:---:|:---|
| 1 | 1 | * |
| 2 | 1, 2 | ** |
| 3 | 1, 2, 3 | *** |
| 4 | 1, 2, 3, 4 | **** |
| 5 | 1, 2, 3, 4, 5 | ***** |
````

(referencias-bibliograficas)=
## Referencias Bibliográficas

- **Schildt, H.** (2022). _Java: A Beginner's Guide_ (9na ed.). McGraw Hill. (Capítulo 3: Program Control Statements).
- **Liang, Y. D.** (2017). _Introduction to Java Programming and Data Structures_ (11va ed.). Pearson.
- **Bloch, J.** (2018). _Effective Java_ (3ra ed.). Addison-Wesley Professional.
- **Oracle Corporation.** (2023). _The Java Language Specification_. [Control Flow](https://docs.oracle.com/javase/specs/jls/se21/html/jls-14.html#jls-14.14).

:::seealso

- {ref}`regla-0x5001` - Estilo de llaves y bloques en estructuras de control.
- {ref}`regla-0xE001` - Comparación de tipos primitivos vs objetos.
:::
