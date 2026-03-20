
# Serie 0x5 - Estructuras de Control y Flujo

(regla-0x5000)=
## `0x5000` - Un solo `return` por método

### Explicación

Para métodos que retornan valor (non-void), debe haber exactamente **un** `return` al final del método. Esto facilita razonar sobre el flujo y garantiza que las postcondiciones se cumplan.

### Justificación

1. **Flujo lineal**: Más fácil seguir la lógica.
2. **Postcondiciones**: Se pueden establecer justo antes del return.
3. **Debugging**: Un solo punto para breakpoint de salida.
4. **Claridad**: Se sabe dónde termina el método.

### Excepción

Métodos `void` pueden tener múltiples returns (o ninguno), porque tienen dos puntos de salida naturales: el `return` explícito y el fin del método.

**Correcto para métodos non-void**:
```java
public int calcular(int x) {
    int resultado;
    
    if (x < 0) {
        resultado = 0;
    } else {
        resultado = x * 2;
    }
    
    return resultado;  // ✅ Un solo return al final
}
```

(regla-0x5001)=
## `0x5001` - Sin usar la asignación compuesta (`+=`, `-=`, `*=`, etc)

### Explicación

Por motivos pedagógicos, en este curso **no se permite** el uso de operadores de asignación compuesta (`+=`, `-=`, `*=`, `/=`, `%=`, etc.). En su lugar, debe usarse la forma explícita de la operación completa.

### Justificación

1. **Claridad pedagógica**: La forma explícita hace evidente la operación completa.
2. **Razonamiento explícito**: Obliga a pensar en términos de "calcular nuevo valor y asignar".
3. **Menos errores**: Evita confusiones sobre qué está siendo modificado.
4. **Uniformidad**: Todas las asignaciones lucen similares, facilitando el análisis.
5. **Base conceptual**: Antes de usar atajos, entender la operación completa.

### Sintaxis

```
PROHIBIDO:  variable OP= expresion
REQUERIDO:  variable = variable OP expresion

Donde OP puede ser: +, -, *, /, %, &, |, ^, <<, >>
```

### Operadores prohibidos

```java
// ❌ Todos estos están prohibidos en el curso
contador += 1;           // Usar: contador = contador + 1
total -= descuento;      // Usar: total = total - descuento
precio *= 1.21;          // Usar: precio = precio * 1.21
dividendo /= 2;          // Usar: dividendo = dividendo / 2
resto %= 10;             // Usar: resto = resto % 10

// ❌ También los operadores de incremento/decremento
i++;                     // Usar: i = i + 1
++i;                     // Usar: i = i + 1
i--;                     // Usar: i = i - 1
--i;                     // Usar: i = i - 1
```

### Ejemplos correctos

```java
// ✅ CORRECTO: Forma explícita
public int calcularFactorial(int n) {
    int resultado = 1;
    for (int i = 2; i <= n; i = i + 1) {  // ✅ No i++
        resultado = resultado * i;  // ✅ No resultado *= i
    }
    return resultado;
}

public int sumarArray(int[] numeros) {
    int suma = 0;
    for (int i = 0; i < numeros.length; i = i + 1) {  // ✅ Explícito
        suma = suma + numeros[i];  // ✅ Explícito
    }
    return suma;
}
```

:::{note}
Esta es una regla **didáctica** para que piensen explícitamente sobre las operaciones. En código profesional, los operadores compuestos son estándar y recomendados.
:::

:::{warning}
Esta regla aplica a **todo el código del curso**. El uso de operadores compuestos será penalizado en evaluaciones.
:::

(regla-0x5002)=
## `0x5002` - Sin `break` y `continue`, en su lugar usen banderas

### Explicación

En este curso, no se permite el uso de las sentencias `break` y `continue` para controlar el flujo de lazos (excepto en `switch`). En su lugar, deben usarse variables booleanas (banderas) en la condición del lazo para controlar su terminación o continuación.

### Justificación

1. **Flujo explícito**: Las banderas hacen explícita la condición de terminación en la declaración del lazo.
2. **Claridad**: La condición del `while`/`for` muestra todas las razones para continuar/terminar.
3. **Razonamiento**: Facilita el análisis formal y la verificación de invariantes.
4. **Menos saltos**: Evita saltos implícitos que dificultan seguir el flujo.
5. **Pedagogía**: Refuerza el pensamiento sobre condiciones de lazo antes de escribir el código.

### `break` - Terminación anticipada

#### Problema: Uso de break ❌

```java
// ❌ INCORRECTO: Usar break
public int buscarElemento(int[] arreglo, int elemento) {
    int indice = -1;
    for (int i = 0; i < arreglo.length; i = i + 1) {
        if (arreglo[i] == elemento) {
            indice = i;
            break;  // ❌ No permitido
        }
    }
    return indice;
}
```

**Problemas:**
- La condición de terminación no está visible en la declaración del `for`
- Requiere seguir el cuerpo del lazo para entender cuándo termina
- El `break` es un salto implícito fuera del lazo

#### Solución: Bandera en la condición ✅

```java
// ✅ CORRECTO: Usar bandera
public int buscarElemento(int[] arreglo, int elemento) {
    int indice = -1;
    boolean encontrado = false;
    int i = 0;
    
    while (i < arreglo.length && !encontrado) {  // ✅ Condición explícita
        if (arreglo[i] == elemento) {
            indice = i;
            encontrado = true;  // ✅ Actualizar bandera
        }
        i = i + 1;
    }
    
    return indice;
}
```

**Ventajas:**
- La condición `!encontrado` está visible en la declaración del lazo
- Es obvio que el lazo termina cuando encuentra el elemento
- No hay saltos implícitos

### `continue` - Saltar iteración

#### Problema: Uso de continue ❌

```java
// ❌ INCORRECTO: Usar continue
public int contarPositivos(int[] numeros) {
    int contador = 0;
    for (int i = 0; i < numeros.length; i = i + 1) {
        if (numeros[i] <= 0) {
            continue;  // ❌ No permitido
        }
        contador = contador + 1;
    }
    return contador;
}
```

#### Solución: Condicional simple ✅

```java
// ✅ CORRECTO: Usar condicional
public int contarPositivos(int[] numeros) {
    int contador = 0;
    for (int i = 0; i < numeros.length; i = i + 1) {
        if (numeros[i] > 0) {  // ✅ Lógica positiva
            contador = contador + 1;
        }
    }
    return contador;
}
```

### Ejemplos comparativos

#### Ejemplo 1: Búsqueda con condición

```java
// ❌ Incorrecto con break
public String buscarPrimeraPalabraLarga(String[] palabras) {
    String resultado = null;
    for (int i = 0; i < palabras.length; i = i + 1) {
        if (palabras[i].length() > 5) {
            resultado = palabras[i];
            break;  // ❌
        }
    }
    return resultado;
}

// ✅ Correcto con bandera
public String buscarPrimeraPalabraLarga(String[] palabras) {
    String resultado = null;
    boolean encontrada = false;
    int i = 0;
    
    while (i < palabras.length && !encontrada) {
        if (palabras[i].length() > 5) {
            resultado = palabras[i];
            encontrada = true;  // ✅
        }
        i = i + 1;
    }
    
    return resultado;
}
```

#### Ejemplo 2: Procesar elementos válidos

```java
// ❌ Incorrecto con continue
public void procesarElementosValidos(int[] elementos) {
    for (int i = 0; i < elementos.length; i = i + 1) {
        if (elementos[i] < 0) {
            continue;  // ❌ Saltar elementos negativos
        }
        if (elementos[i] > 100) {
            continue;  // ❌ Saltar elementos grandes
        }
        procesarElemento(elementos[i]);
    }
}

// ✅ Correcto con condicionales
public void procesarElementosValidos(int[] elementos) {
    for (int i = 0; i < elementos.length; i = i + 1) {
        boolean esValido = elementos[i] >= 0 && elementos[i] <= 100;
        if (esValido) {  // ✅ Condición positiva
            procesarElemento(elementos[i]);
        }
    }
}
```

#### Ejemplo 3: Búsqueda con múltiples condiciones

```java
// ❌ Incorrecto con break
public boolean existeElementoEspecial(int[] arreglo) {
    for (int i = 0; i < arreglo.length; i = i + 1) {
        if (arreglo[i] % 2 == 0 && arreglo[i] > 10) {
            return true;  // ❌ Podría ser break
        }
    }
    return false;
}

// ✅ Correcto con bandera
public boolean existeElementoEspecial(int[] arreglo) {
    boolean existe = false;
    int i = 0;
    
    while (i < arreglo.length && !existe) {
        if (arreglo[i] % 2 == 0 && arreglo[i] > 10) {
            existe = true;  // ✅ Usar bandera
        }
        i = i + 1;
    }
    
    return existe;
}
```

### Excepción: `break` en `switch`

El único contexto donde `break` está permitido es en sentencias `switch`:

```java
// ✅ CORRECTO: break en switch está permitido
public String obtenerNombreDia(int dia) {
    String nombre;
    switch (dia) {
        case 1:
            nombre = "Lunes";
            break;  // ✅ Permitido en switch
        case 2:
            nombre = "Martes";
            break;  // ✅ Permitido en switch
        // ...
        default:
            nombre = "Día inválido";
            break;  // ✅ Permitido en switch
    }
    return nombre;
}
```

:::{important}
`break` está permitido **solo en `switch`**, no en lazos (`for`, `while`, `do-while`).
:::

### Por qué existe esta regla

Esta es una **regla pedagógica** para forzar el razonamiento sobre condiciones de lazo:

```java
// break oculta la condición de terminación
while (true) {
    if (condicion) break;  // Condición oculta en el cuerpo
}

// Bandera hace explícita la condición
boolean continuar = true;
while (continuar) {  // Condición visible
    if (condicion) {
        continuar = false;
    }
}
```

### Comparación: Curso vs. Industria

| Aspecto | `break`/`continue` | Banderas |
|---------|-------------------|----------|
| **Claridad de condición** | ⚠️ Oculta | ✅ Explícita |
| **Facilidad de lectura** | ⚠️ Requiere análisis | ✅ Visible arriba |
| **Código profesional** | ✅ Estándar | ⚠️ Verboso |
| **En este curso** | ❌ Prohibido | ✅ Requerido |

:::{note}
En código profesional, `break` y `continue` son herramientas estándar y aceptadas. Esta restricción es exclusivamente educativa.
:::

### Patrón común: Búsqueda con terminación anticipada

```java
// ✅ Patrón estándar para búsquedas
public int buscar(int[] arreglo, int elemento) {
    int resultado = -1;
    boolean encontrado = false;
    int i = 0;
    
    while (i < arreglo.length && !encontrado) {
        if (arreglo[i] == elemento) {
            resultado = i;
            encontrado = true;
        }
        i = i + 1;
    }
    
    return resultado;
}
```

### Patrón común: Saltar elementos inválidos

```java
// ✅ Patrón estándar para filtrado
public int contarValidos(String[] elementos) {
    int contador = 0;
    for (int i = 0; i < elementos.length; i = i + 1) {
        boolean esValido = elementos[i] != null && elementos[i].length() > 0;
        if (esValido) {
            contador = contador + 1;
        }
    }
    return contador;
}
```

:::{tip}
Si te encontrás escribiendo `break` o `continue`, preguntate: "¿Qué bandera booleana representa esta condición de salida?"
:::

(regla-0x5003)=
## `0x5003` - Usar parámetros como variables solo si no cambia su significado

### Explicación

Los parámetros de un método representan las entradas originales y no deben modificarse para almacenar valores intermedios o resultados. Si necesitás un valor derivado o transformado, creá una variable local nueva con un nombre descriptivo.

### Justificación

1. **Claridad semántica**: El parámetro siempre mantiene su significado original durante todo el método.
2. **Debugging**: Al inspeccionar el parámetro, siempre ves el valor original recibido.
3. **Razonamiento**: Facilita verificar precondiciones y razonar sobre el código.
4. **Legibilidad**: Las variables locales con nombres distintos indican transformaciones.
5. **Inmutabilidad conceptual**: Los parámetros actúan como valores de entrada inmutables.

### Anti-patrón: Modificar parámetro ❌

```java
// ❌ INCORRECTO: Modificar el parámetro
public int calcularDobleConDescuento(int precio) {
    precio = precio * 2;        // ❌ 'precio' ya no es el precio original
    precio = precio - 10;       // ❌ Sigue modificando 'precio'
    return precio;
}
```

**Problemas:**
- En línea 2, `precio` ya no significa "precio recibido"
- Dificulta debugging (el valor original se pierde)
- No es claro qué representa `precio` en cada punto

### Solución correcta: Variable local ✅

```java
// ✅ CORRECTO: Usar variable local para transformaciones
public int calcularDobleConDescuento(int precio) {
    int precioDoble = precio * 2;
    int precioFinal = precioDoble - 10;
    return precioFinal;
}

// ✅ O más compacto si no necesitás intermedios
public int calcularDobleConDescuento(int precio) {
    int precioFinal = (precio * 2) - 10;
    return precioFinal;
}
```

### Ejemplos comparativos

#### Ejemplo 1: Transformación de número

```java
// ❌ Incorrecto: Reutilizar parámetro
public int procesarNumero(int numero) {
    numero = numero + 10;      // ❌ Perdimos el valor original
    numero = numero * 2;       // ❌ 'numero' tiene otro significado
    numero = numero - 5;       // ❌ Y otro más
    return numero;
}

// ✅ Correcto: Variables locales descriptivas
public int procesarNumero(int numero) {
    int numeroIncrementado = numero + 10;
    int numeroDoble = numeroIncrementado * 2;
    int resultado = numeroDoble - 5;
    return resultado;
}

// ✅ O con una sola variable resultado
public int procesarNumero(int numero) {
    int resultado = numero + 10;
    resultado = resultado * 2;
    resultado = resultado - 5;
    return resultado;
}
```

#### Ejemplo 2: Cálculo con múltiples pasos

```java
// ❌ Incorrecto: Modificar parámetros
public double calcularPrecioFinal(double precioBase, double descuento) {
    precioBase = precioBase - descuento;  // ❌ Ya no es el precio base
    precioBase = precioBase * 1.21;       // ❌ Ahora tiene IVA incluido
    return precioBase;
}

// ✅ Correcto: Variables intermedias
public double calcularPrecioFinal(double precioBase, double descuento) {
    double precioConDescuento = precioBase - descuento;
    double precioConIva = precioConDescuento * 1.21;
    return precioConIva;
}
```

#### Ejemplo 3: Procesamiento de texto

```java
// ❌ Incorrecto: Modificar parámetro String
public String normalizarTexto(String texto) {
    texto = texto.trim();           // ❌ Perdimos el texto original
    texto = texto.toLowerCase();    // ❌ 'texto' cambió de significado
    texto = texto.replace(" ", "_"); // ❌ Y sigue cambiando
    return texto;
}

// ✅ Correcto: Variable para resultado
public String normalizarTexto(String texto) {
    String textoSinEspacios = texto.trim();
    String textoMinusculas = textoSinEspacios.toLowerCase();
    String textoNormalizado = textoMinusculas.replace(" ", "_");
    return textoNormalizado;
}

// ✅ O con una variable resultado
public String normalizarTexto(String texto) {
    String resultado = texto.trim();
    resultado = resultado.toLowerCase();
    resultado = resultado.replace(" ", "_");
    return resultado;
}
```

### Caso especial: Parámetros en lazos

Los parámetros tampoco deben usarse como contadores o índices:

```java
// ❌ Incorrecto: Usar parámetro como contador
public void imprimirN Veces(String mensaje, int veces) {
    while (veces > 0) {
        System.out.println(mensaje);
        veces = veces - 1;  // ❌ 'veces' ya no es la cantidad original
    }
}

// ✅ Correcto: Variable local para contador
public void imprimirNVeces(String mensaje, int veces) {
    int contador = 0;
    while (contador < veces) {
        System.out.println(mensaje);
        contador = contador + 1;
    }
}

// ✅ O con variable para restantes
public void imprimirNVeces(String mensaje, int veces) {
    int restantes = veces;  // Variable local copia el valor
    while (restantes > 0) {
        System.out.println(mensaje);
        restantes = restantes - 1;
    }
}
```

### Cuando NO aplicar la regla

La regla dice "**si no cambia su significado**". Hay situaciones donde usar el parámetro directamente es aceptable:

#### Validación y normalización de entrada

```java
// ✅ Aceptable: normalizar entrada al inicio
public String procesarTexto(String texto) {
    // Normalizar entrada: texto sigue siendo "el texto de entrada"
    if (texto == null) {
        texto = "";  // ✅ Aceptable: establece valor por defecto
    }
    
    // Ahora trabajar con texto
    int longitud = texto.length();
    return analizarTexto(texto);
}
```

#### Parámetros que no se vuelven a usar

```java
// ✅ Aceptable si el parámetro no se usa después
public int calcular(int valor) {
    if (valor < 0) {
        valor = 0;  // ✅ Solo se usa para retornar inmediatamente
        return valor;
    }
    return valor * 2;
}

// ⚠️ Pero mejor usar variable local para claridad
public int calcular(int valor) {
    int valorNormalizado = (valor < 0) ? 0 : valor;
    return valorNormalizado * 2;
}
```

### Beneficio: Depuración más fácil

```java
// ❌ Difícil de depurar
public int calcular(int numero) {
    numero = numero + 5;     // Breakpoint aquí: numero = valor_original + 5
    numero = numero * 2;     // Breakpoint aquí: numero = (valor_original + 5) * 2
    numero = numero - 3;     // Breakpoint aquí: ¿cuál es el valor original?
    return numero;
}

// ✅ Fácil de depurar
public int calcular(int numero) {
    int conIncremento = numero + 5;    // Breakpoint: numero = valor_original
    int duplicado = conIncremento * 2;  // Breakpoint: ambos visibles
    int resultado = duplicado - 3;      // Breakpoint: todos visibles
    return resultado;
}
```

### Parámetros de objetos mutables

La regla también aplica a objetos:

```java
// ❌ Incorrecto: Modificar parámetro objeto
public void procesarPedido(Pedido pedido) {
    pedido = new Pedido();  // ❌ Ahora perdimos la referencia original
    pedido.setTotal(100.0);
}

// ✅ Correcto: Crear nuevo objeto con variable local
public Pedido crearCopia(Pedido pedidoOriginal) {
    Pedido pedidoCopia = new Pedido();
    pedidoCopia.setId(pedidoOriginal.getId());
    pedidoCopia.setTotal(pedidoOriginal.getTotal());
    return pedidoCopia;
}
```

:::{warning}
Modificar el **contenido** del objeto (mediante setters) es diferente de reasignar el parámetro. Esta regla se refiere a **reasignación de la referencia**, no a modificación del estado del objeto.
:::

### Resumen

```java
// ❌ Incorrecto: Parámetro cambia de significado
public int metodo(int valor) {
    valor = valor + 1;    // 'valor' ya no es el valor recibido
    valor = valor * 2;    // 'valor' tiene otro significado
    return valor;
}

// ✅ Correcto: Parámetro mantiene su significado
public int metodo(int valor) {
    int resultado = valor + 1;  // 'valor' sigue siendo el valor recibido
    resultado = resultado * 2;   // 'resultado' es claro
    return resultado;
}
```

:::{important}
**Regla de oro**: Si mirás un parámetro en cualquier punto del método, debería tener el mismo valor y significado que cuando entró al método.
:::

(regla-0x5004)=
## `0x5004` - Los métodos no deben usar `printf` o `Scanner` a no ser que sea explícitamente su propósito

### Explicación

Los métodos de lógica de negocio no deben mezclar cálculos o procesamiento con operaciones de entrada/salida (I/O). El uso de `System.out.println()`, `printf()`, `Scanner`, o cualquier otra forma de I/O debe estar restringido a métodos cuyo propósito explícito sea interactuar con el usuario o manejar I/O.

### Justificación

1. **Separación de responsabilidades**: Lógica de negocio separada de presentación.
2. **Testabilidad**: Métodos sin I/O son más fáciles de testear automáticamente.
3. **Reutilización**: Métodos sin I/O pueden usarse en diferentes contextos (GUI, web, consola).
4. **Mantenimiento**: Cambiar la forma de presentación no requiere modificar la lógica.
5. **Arquitectura limpia**: Base para arquitecturas en capas bien diseñadas.

### Principio: Separación de Capas

```
ARQUITECTURA CORRECTA:
┌────────────────────┐
│  Capa de           │  ← System.out, Scanner
│  Presentación/UI   │  ← printf, readLine
└────────────────────┘
         ↓
┌────────────────────┐
│  Capa de           │  ← NO debe tener I/O
│  Lógica de Negocio │  ← Solo cálculos y procesamiento
└────────────────────┘
         ↓
┌────────────────────┐
│  Capa de Datos     │  ← Archivos, BD
└────────────────────┘
```

### Anti-patrón: I/O mezclado con lógica ❌

```java
// ❌ INCORRECTO: Mezclar cálculo con impresión
public double calcularPromedio(double[] valores) {
    double suma = 0.0;
    
    System.out.println("Calculando promedio...");  // ❌ I/O en lógica
    
    for (int i = 0; i < valores.length; i = i + 1) {
        suma = suma + valores[i];
        System.out.println("Suma parcial: " + suma);  // ❌ I/O en lógica
    }
    
    double promedio = suma / valores.length;
    System.out.println("Promedio: " + promedio);  // ❌ I/O en lógica
    
    return promedio;
}
```

**Problemas:**
- No podés testear el método sin producir output en consola
- No podés usar el método en GUI o web sin modificarlo
- El método hace dos cosas: calcular Y mostrar

### Solución correcta: Separar responsabilidades ✅

```java
// ✅ CORRECTO: Método de lógica pura
public double calcularPromedio(double[] valores) {
    double suma = 0.0;
    
    for (int i = 0; i < valores.length; i = i + 1) {
        suma = suma + valores[i];
    }
    
    return suma / valores.length;  // Solo retorna, no imprime
}

// ✅ CORRECTO: Método separado para presentación
public void mostrarCalculoPromedio(double[] valores) {
    System.out.println("Calculando promedio...");
    
    double promedio = calcularPromedio(valores);  // Usa lógica
    
    System.out.println("Promedio: " + promedio);
}
```

### Ejemplo completo: Separación correcta

#### Capa de lógica (sin I/O)

```java
public class CalculadoraDescuentos {
    /**
     * Calcula el precio final aplicando descuento según tipo de cliente.
     *
     * @param precioBase Precio original del producto
     * @param tipoCliente Tipo de cliente (REGULAR, PREMIUM, VIP)
     * @return Precio final con descuento aplicado
     */
    public double calcularPrecioConDescuento(double precioBase, 
                                              TipoCliente tipoCliente) {
        double porcentajeDescuento = obtenerPorcentajeDescuento(tipoCliente);
        double descuento = precioBase * porcentajeDescuento;
        double precioFinal = precioBase - descuento;
        return precioFinal;  // ✅ Solo retorna, no imprime
    }
    
    private double obtenerPorcentajeDescuento(TipoCliente tipo) {
        double porcentaje;
        switch (tipo) {
            case REGULAR:
                porcentaje = 0.0;
                break;
            case PREMIUM:
                porcentaje = 0.10;
                break;
            case VIP:
                porcentaje = 0.20;
                break;
            default:
                porcentaje = 0.0;
                break;
        }
        return porcentaje;  // ✅ Solo retorna
    }
}
```

#### Capa de presentación (con I/O)

```java
public class InterfazUsuario {
    private CalculadoraDescuentos calculadora;
    private Scanner scanner;
    
    public InterfazUsuario() {
        this.calculadora = new CalculadoraDescuentos();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Interactúa con el usuario para calcular precio con descuento.
     * Este método SÍ puede usar Scanner y printf porque su propósito
     * explícito es la interacción con el usuario.
     */
    public void ejecutarCalculoInteractivo() {
        // ✅ I/O permitido aquí - es el propósito del método
        System.out.println("=== Calculadora de Descuentos ===");
        
        System.out.print("Ingrese precio base: ");
        double precio = scanner.nextDouble();
        
        System.out.print("Tipo de cliente (1=Regular, 2=Premium, 3=VIP): ");
        int opcion = scanner.nextInt();
        TipoCliente tipo = convertirOpcionATipo(opcion);
        
        // Delegar cálculo a capa de lógica
        double precioFinal = calculadora.calcularPrecioConDescuento(precio, tipo);
        
        // ✅ Mostrar resultado
        System.out.printf("Precio original: $%.2f%n", precio);
        System.out.printf("Precio con descuento: $%.2f%n", precioFinal);
        System.out.printf("Ahorro: $%.2f%n", precio - precioFinal);
    }
    
    private TipoCliente convertirOpcionATipo(int opcion) {
        TipoCliente tipo;
        switch (opcion) {
            case 2:
                tipo = TipoCliente.PREMIUM;
                break;
            case 3:
                tipo = TipoCliente.VIP;
                break;
            default:
                tipo = TipoCliente.REGULAR;
                break;
        }
        return tipo;
    }
}
```

### Consecuencias de la separación

#### Testabilidad mejorada

```java
// ✅ Método sin I/O es fácil de testear
@Test
void testCalcularPrecioConDescuento_ClientePremium_Aplica10Porciento() {
    CalculadoraDescuentos calc = new CalculadoraDescuentos();
    
    double precioFinal = calc.calcularPrecioConDescuento(100.0, 
                                                          TipoCliente.PREMIUM);
    
    assertEquals(90.0, precioFinal, 0.01);  // Sin output en consola
}
```

#### Reutilización en diferentes contextos

```java
// ✅ Misma lógica, diferente UI
public class InterfazGrafica extends JFrame {
    private CalculadoraDescuentos calculadora;
    
    private void onCalcularButtonClick() {
        double precio = Double.parseDouble(precioTextField.getText());
        TipoCliente tipo = (TipoCliente) tipoComboBox.getSelectedItem();
        
        // Reutilizamos la lógica
        double precioFinal = calculadora.calcularPrecioConDescuento(precio, tipo);
        
        resultadoLabel.setText(String.format("Precio final: $%.2f", precioFinal));
    }
}
```

### Métodos donde I/O SÍ está permitido

#### Métodos cuyo propósito es I/O

```java
// ✅ CORRECTO: El propósito del método ES la interacción
public class MenuPrincipal {
    /**
     * Muestra el menú y captura la opción del usuario.
     * @return Opción seleccionada por el usuario
     */
    public int mostrarMenuYCapturarOpcion() {
        // ✅ I/O permitido - es el propósito del método
        System.out.println("=== Menú Principal ===");
        System.out.println("1. Agregar producto");
        System.out.println("2. Eliminar producto");
        System.out.println("3. Listar productos");
        System.out.println("0. Salir");
        System.out.print("Seleccione opción: ");
        
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
}
```

#### Método main

```java
// ✅ CORRECTO: main puede tener I/O
public class Aplicacion {
    public static void main(String[] args) {
        // ✅ main puede interactuar con usuario
        System.out.println("Bienvenido al sistema");
        
        InterfazUsuario interfaz = new InterfazUsuario();
        interfaz.iniciar();  // Delegar a clase responsable de I/O
        
        System.out.println("Gracias por usar el sistema");
    }
}
```

### Violaciones comunes en el curso

#### Violación 1: Debug prints en lógica ❌

```java
// ❌ INCORRECTO
public int buscarMaximo(int[] numeros) {
    int maximo = numeros[0];
    System.out.println("Buscando máximo...");  // ❌ Debug print
    
    for (int i = 1; i < numeros.length; i = i + 1) {
        if (numeros[i] > maximo) {
            maximo = numeros[i];
            System.out.println("Nuevo máximo: " + maximo);  // ❌
        }
    }
    
    return maximo;
}

// ✅ CORRECTO: Sin I/O
public int buscarMaximo(int[] numeros) {
    int maximo = numeros[0];
    
    for (int i = 1; i < numeros.length; i = i + 1) {
        if (numeros[i] > maximo) {
            maximo = numeros[i];
        }
    }
    
    return maximo;
}
```

:::{tip}
Para debugging, usá el debugger del IDE en lugar de `System.out.println()`. Los prints de debug deben eliminarse antes de entregar el código.
:::

#### Violación 2: Pedir datos dentro de método de cálculo ❌

```java
// ❌ INCORRECTO: Pedir input en método de cálculo
public double calcularAreaRectangulo() {
    Scanner scanner = new Scanner(System.in);
    
    System.out.print("Ingrese base: ");  // ❌ I/O en lógica
    double base = scanner.nextDouble();
    
    System.out.print("Ingrese altura: ");  // ❌ I/O en lógica
    double altura = scanner.nextDouble();
    
    return base * altura;
}

// ✅ CORRECTO: Parámetros en lugar de input
public double calcularAreaRectangulo(double base, double altura) {
    return base * altura;  // Lógica pura
}

// ✅ Método separado para interacción
public void calcularAreaInteractivo() {
    Scanner scanner = new Scanner(System.in);
    
    System.out.print("Ingrese base: ");
    double base = scanner.nextDouble();
    
    System.out.print("Ingrese altura: ");
    double altura = scanner.nextDouble();
    
    double area = calcularAreaRectangulo(base, altura);  // Usa lógica
    
    System.out.printf("El área es: %.2f%n", area);
}
```

### Patrón recomendado: Controlador

```java
// ✅ Patrón: Separar en tres responsabilidades
public class SistemaFacturacion {
    // 1. Lógica de negocio (sin I/O)
    public class CalculadorFactura {
        public double calcularTotal(List<Producto> productos) {
            double total = 0.0;
            for (Producto p : productos) {
                total = total + p.getPrecio();
            }
            return total;
        }
    }
    
    // 2. Entrada de datos (con I/O)
    public class LectorDatos {
        private Scanner scanner = new Scanner(System.in);
        
        public List<Producto> leerProductos() {
            System.out.print("¿Cuántos productos? ");
            int cantidad = scanner.nextInt();
            
            List<Producto> productos = new ArrayList<>();
            for (int i = 0; i < cantidad; i = i + 1) {
                productos.add(leerProducto());
            }
            return productos;
        }
        
        private Producto leerProducto() {
            System.out.print("Nombre: ");
            String nombre = scanner.next();
            System.out.print("Precio: ");
            double precio = scanner.nextDouble();
            return new Producto(nombre, precio);
        }
    }
    
    // 3. Presentación de resultados (con I/O)
    public class MostradorResultados {
        public void mostrarFactura(List<Producto> productos, double total) {
            System.out.println("\n=== FACTURA ===");
            for (Producto p : productos) {
                System.out.printf("%s: $%.2f%n", p.getNombre(), p.getPrecio());
            }
            System.out.printf("TOTAL: $%.2f%n", total);
        }
    }
    
    // 4. Coordinador (orquesta las tres capas)
    public static void main(String[] args) {
        LectorDatos lector = new LectorDatos();
        CalculadorFactura calculador = new CalculadorFactura();
        MostradorResultados mostrador = new MostradorResultados();
        
        List<Producto> productos = lector.leerProductos();
        double total = calculador.calcularTotal(productos);
        mostrador.mostrarFactura(productos, total);
    }
}
```

### Métodos donde I/O SÍ es permitido

#### Clase de interfaz de usuario

```java
// ✅ CORRECTO: Clase dedicada a I/O
public class InterfazConsola {
    private Scanner scanner;
    
    public InterfazConsola() {
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Lee un número entero del usuario.
     * El propósito explícito del método ES leer input.
     */
    public int leerEntero(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextInt();
    }
    
    /**
     * Muestra un mensaje al usuario.
     * El propósito explícito del método ES mostrar output.
     */
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
    
    /**
     * Muestra un resultado formateado.
     */
    public void mostrarResultado(String etiqueta, double valor) {
        System.out.printf("%s: %.2f%n", etiqueta, valor);
    }
}
```

### Testing: Por qué importa

```java
// ❌ Método con I/O - difícil de testear
public int calcularYMostrar(int a, int b) {
    int resultado = a + b;
    System.out.println("Resultado: " + resultado);  // ❌
    return resultado;
}

// Test problemático
@Test
void testCalcular() {
    // Problema: genera output en consola durante test
    int resultado = calcularYMostrar(2, 3);
    assertEquals(5, resultado);
    // Output: "Resultado: 5" aparece en el log de tests
}
```

```java
// ✅ Método sin I/O - fácil de testear
public int calcular(int a, int b) {
    return a + b;  // ✅ Lógica pura
}

// Test limpio
@Test
void testCalcular_ConDosNumeros_RetornaSuma() {
    int resultado = calcular(2, 3);
    assertEquals(5, resultado);  // Sin output espurio
}
```

### Inyección de dependencias para I/O avanzado

Para casos más avanzados, inyectar la fuente de I/O:

```java
// ✅ Diseño avanzado: I/O inyectable
public class ProcesadorDatos {
    private Scanner scanner;
    private PrintStream output;
    
    // Constructor con dependencias inyectadas
    public ProcesadorDatos(Scanner input, PrintStream output) {
        this.scanner = input;
        this.output = output;
    }
    
    // Método que necesita I/O pero es testeable
    public void procesarDatosInteractivo() {
        output.print("Ingrese dato: ");
        int dato = scanner.nextInt();
        
        int resultado = procesarDato(dato);  // Lógica separada
        
        output.printf("Resultado: %d%n", resultado);
    }
    
    // Lógica pura, sin I/O
    private int procesarDato(int dato) {
        return dato * 2;  // ✅ Testeable fácilmente
    }
}

// Test con mocks
@Test
void testProcesarDatosInteractivo() {
    String input = "5\n";
    ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    
    ProcesadorDatos proc = new ProcesadorDatos(
        new Scanner(in),
        new PrintStream(out)
    );
    
    proc.procesarDatosInteractivo();
    
    assertTrue(out.toString().contains("Resultado: 10"));
}
```

### Resumen visual

```java
// ❌ Mal diseño: Todo mezclado
public class Calculadora {
    public double calcular() {
        System.out.print("Ingrese número: ");  // ❌ Input
        double num = new Scanner(System.in).nextDouble();
        double resultado = num * 2;
        System.out.println("Resultado: " + resultado);  // ❌ Output
        return resultado;
    }
}

// ✅ Buen diseño: Responsabilidades separadas
public class Calculadora {
    // Solo lógica, sin I/O
    public double calcular(double numero) {
        return numero * 2;
    }
}

public class UI {
    private Calculadora calc = new Calculadora();
    private Scanner scanner = new Scanner(System.in);
    
    // Solo I/O y coordinación
    public void ejecutar() {
        System.out.print("Ingrese número: ");
        double num = scanner.nextDouble();
        
        double resultado = calc.calcular(num);  // Usa lógica
        
        System.out.println("Resultado: " + resultado);
    }
}
```

:::{important}
**Principio de Separación de Responsabilidades (SRP)**: Cada método debe hacer una cosa. Si un método calcula Y muestra, está haciendo dos cosas.
:::

(regla-0x5005)=
## `0x5005` - Evitar anidamiento profundo de condicionales (máximo 3 niveles)

### Explicación

El anidamiento excesivo de estructuras condicionales (`if`, `else`, `switch`) dificulta significativamente la lectura y comprensión del código. Esta regla establece un **máximo de 3 niveles de profundidad** de anidación. Cuando se necesita más, debe refactorizarse usando retornos tempranos (guard clauses) o extracción de métodos.

### Justificación

1. **Complejidad ciclomática**: Reduce la complejidad cognitiva del código.
2. **Legibilidad**: Código profundamente anidado es difícil de seguir.
3. **Mantenibilidad**: Más fácil agregar o modificar condiciones.
4. **Prevención de errores**: Menos niveles reducen errores de lógica.
5. **Principio de un solo nivel de abstracción**: Cada método debe operar a un nivel conceptual.

### Límites de anidamiento

```
NIVEL 1: if (condicion1) {           ✅ Permitido
NIVEL 2:     if (condicion2) {       ✅ Permitido
NIVEL 3:         if (condicion3) {   ✅ Permitido (máximo)
NIVEL 4:             if (condicion4) { ❌ PROHIBIDO
                         // ...
                     }
                 }
             }
         }
```

### Anti-patrón: Anidamiento profundo ❌

```java
// ❌ INCORRECTO: 5 niveles de anidamiento
public void procesarPedido(Pedido pedido) {
    if (pedido != null) {                          // Nivel 1
        if (pedido.esValido()) {                   // Nivel 2
            if (pedido.getCliente() != null) {     // Nivel 3
                if (pedido.tieneSaldo()) {         // Nivel 4 ❌
                    if (pedido.tieneStock()) {     // Nivel 5 ❌
                        // Lógica profundamente anidada
                        procesarPagoYEnvio(pedido);
                    }
                }
            }
        }
    }
}
```

**Problemas:**
- Muy difícil de leer
- La lógica principal está profundamente enterrada
- Dificulta agregar nuevas condiciones
- Complejidad ciclomática alta

### Solución 1: Guard Clauses (Retorno temprano) ✅

```java
// ✅ CORRECTO: Guard clauses - sin anidamiento
public void procesarPedido(Pedido pedido) {
    // Validaciones tempranas con retorno
    if (pedido == null) {
        return;
    }
    if (!pedido.esValido()) {
        return;
    }
    if (pedido.getCliente() == null) {
        return;
    }
    if (!pedido.tieneSaldo()) {
        return;
    }
    if (!pedido.tieneStock()) {
        return;
    }
    
    // Lógica principal a nivel superior - fácil de leer
    procesarPagoYEnvio(pedido);
}
```

**Ventajas:**
- La lógica principal está al nivel superior
- Fácil agregar nuevas validaciones
- Cada condición es independiente y clara
- Cero niveles de anidamiento

:::{tip}
Las **guard clauses** (cláusulas de guarda) son chequeos al inicio del método que retornan tempranamente si las precondiciones no se cumplen. Mantienen el "happy path" al nivel superior.
:::

### Solución 2: Extracción de métodos ✅

```java
// ✅ CORRECTO: Extraer validaciones a métodos
public void procesarPedido(Pedido pedido) {
    if (!esProcesamientoValido(pedido)) {  // Método auxiliar
        return;
    }
    
    // Lógica principal
    procesarPagoYEnvio(pedido);
}

private boolean esProcesamientoValido(Pedido pedido) {
    if (pedido == null) {
        return false;
    }
    if (!pedido.esValido()) {
        return false;
    }
    if (pedido.getCliente() == null) {
        return false;
    }
    if (!pedido.tieneSaldo()) {
        return false;
    }
    if (!pedido.tieneStock()) {
        return false;
    }
    return true;
}
```

### Solución 3: Combinar condiciones ✅

```java
// ✅ CORRECTO: Condiciones compuestas cuando tiene sentido
public void procesarPedido(Pedido pedido) {
    boolean pedidoEsValido = pedido != null 
                             && pedido.esValido() 
                             && pedido.getCliente() != null;
    
    boolean tieneTodo = pedido.tieneSaldo() && pedido.tieneStock();
    
    if (!pedidoEsValido || !tieneTodo) {
        return;
    }
    
    // Lógica principal
    procesarPagoYEnvio(pedido);
}
```

### Ejemplos de diferentes contextos

#### Ejemplo 1: Validación de datos

```java
// ❌ Incorrecto: Anidamiento profundo
public boolean validarUsuario(Usuario usuario) {
    if (usuario != null) {
        if (usuario.getNombre() != null) {
            if (usuario.getNombre().length() > 0) {
                if (usuario.getEmail() != null) {
                    if (usuario.getEmail().contains("@")) {
                        return true;
                    }
                }
            }
        }
    }
    return false;
}

// ✅ Correcto: Guard clauses
public boolean validarUsuario(Usuario usuario) {
    if (usuario == null) {
        return false;
    }
    if (usuario.getNombre() == null || usuario.getNombre().length() == 0) {
        return false;
    }
    if (usuario.getEmail() == null || !usuario.getEmail().contains("@")) {
        return false;
    }
    return true;
}
```

#### Ejemplo 2: Procesamiento con múltiples condiciones

```java
// ❌ Incorrecto: Anidamiento excesivo
public double calcularDescuento(Cliente cliente, double monto) {
    if (cliente != null) {
        if (cliente.esPremium()) {
            if (monto > 1000) {
                if (cliente.getMesesAnti guedad() > 12) {
                    return monto * 0.25;  // 25% descuento
                } else {
                    return monto * 0.15;  // 15% descuento
                }
            } else {
                return monto * 0.10;  // 10% descuento
            }
        } else {
            if (monto > 500) {
                return monto * 0.05;
            }
        }
    }
    return 0.0;
}

// ✅ Correcto: Guard clauses y método auxiliar
public double calcularDescuento(Cliente cliente, double monto) {
    if (cliente == null) {
        return 0.0;
    }
    
    if (cliente.esPremium()) {
        return calcularDescuentoPremium(monto, cliente.getMesesAntiguedad());
    } else {
        return calcularDescuentoRegular(monto);
    }
}

private double calcularDescuentoPremium(double monto, int mesesAntiguedad) {
    if (monto > 1000 && mesesAntiguedad > 12) {
        return monto * 0.25;
    } else if (monto > 1000) {
        return monto * 0.15;
    } else {
        return monto * 0.10;
    }
}

private double calcularDescuentoRegular(double monto) {
    if (monto > 500) {
        return monto * 0.05;
    }
    return 0.0;
}
```

### Medición de niveles de anidamiento

```java
public void metodo() {
    // Nivel 0 (método)
    
    if (condicion1) {           // → Nivel 1
        // código
        
        if (condicion2) {       // → Nivel 2
            // código
            
            if (condicion3) {   // → Nivel 3 (máximo permitido)
                // código
            }
        }
    }
}
```

:::{note}
El cuerpo del método está en nivel 0. El primer `if` crea nivel 1, y así sucesivamente.
:::

### Anidamiento con diferentes estructuras

La regla aplica a **cualquier** estructura de control:

```java
// ❌ Mezcla de if, for, while anidados
public void procesar(List<Pedido> pedidos) {
    for (Pedido p : pedidos) {              // Nivel 1
        if (p.esValido()) {                  // Nivel 2
            for (Item i : p.getItems()) {    // Nivel 3
                if (i.tieneStock()) {        // Nivel 4 ❌
                    // ...
                }
            }
        }
    }
}

// ✅ Extraer método interno
public void procesar(List<Pedido> pedidos) {
    for (Pedido p : pedidos) {          // Nivel 1
        if (p.esValido()) {              // Nivel 2
            procesarItemsPedido(p);      // Nivel 3 → método
        }
    }
}

private void procesarItemsPedido(Pedido pedido) {
    for (Item i : pedido.getItems()) {   // Nivel 1 (en nuevo método)
        if (i.tieneStock()) {            // Nivel 2
            procesarItem(i);
        }
    }
}
```

### Técnica: Inversión de condicionales

```java
// ❌ Incorrecto: Anidamiento innecesario
public void procesar(String dato) {
    if (dato != null) {
        if (dato.length() > 0) {
            if (dato.startsWith("A")) {
                // procesar
            }
        }
    }
}

// ✅ Correcto: Invertir y retornar temprano
public void procesar(String dato) {
    if (dato == null) return;
    if (dato.length() == 0) return;
    if (!dato.startsWith("A")) return;
    
    // procesar
}

// ✅ O combinar condiciones
public void procesar(String dato) {
    boolean esValido = dato != null 
                       && dato.length() > 0 
                       && dato.startsWith("A");
    
    if (!esValido) return;
    
    // procesar
}
```

### Patrón: Polimorfismo para eliminar anidamiento

```java
// ❌ Incorrecto: Switch anidado
public double calcularPrecio(Producto producto) {
    if (producto.getCategoria() == Categoria.ELECTRONICA) {
        if (producto.esNuevo()) {
            if (producto.tieneGarantia()) {
                return producto.getPrecioBase() * 1.3;
            } else {
                return producto.getPrecioBase() * 1.1;
            }
        }
    }
    // ... más casos
    return producto.getPrecioBase();
}

// ✅ Correcto: Polimorfismo
public interface EstrategiaPrecio {
    double calcular(Producto producto);
}

public class PrecioElectronicaNueva implements EstrategiaPrecio {
    public double calcular(Producto producto) {
        if (producto.tieneGarantia()) {
            return producto.getPrecioBase() * 1.3;
        } else {
            return producto.getPrecioBase() * 1.1;
        }
    }
}
```

:::{important}
Cuando te encuentres anidando más de 3 niveles, es señal de que el método está haciendo demasiado y debe refactorizarse.
:::

(regla-0x5006)=
## `0x5006` - Los bucles deben tener condiciones de terminación claras

### Explicación

Todo lazo debe tener una **condición de terminación clara, comprensible y garantizada**. La condición debe hacer obvio por qué y cuándo el lazo terminará. Evitar condiciones complejas que requieran análisis profundo para entender el comportamiento del lazo.

### Justificación

1. **Prevención de lazos infinitos**: Condiciones claras reducen el riesgo de lazos sin fin.
2. **Legibilidad**: Debe ser obvio cuándo termina el lazo sin analizar el cuerpo.
3. **Razonamiento**: Facilita demostrar que el lazo termina (terminación garantizada).
4. **Mantenimiento**: Condiciones simples son más fáciles de modificar correctamente.
5. **Debugging**: Más fácil identificar por qué un lazo no termina cuando debería.

### Características de una buena condición

```
CONDICIÓN CLARA debe ser:
- Evaluable sin leer el cuerpo del lazo
- Compuesta por máximo 2-3 subexpresiones
- Con nombres de variables descriptivos
- Sin lógica compleja o llamadas a métodos pesados
```

### Ejemplos de condiciones claras ✅

```java
// ✅ Condición simple: contador hasta límite
for (int i = 0; i < numeros.length; i = i + 1) {
    // Es obvio: termina cuando i alcanza length
}

// ✅ Condición con bandera: búsqueda
boolean encontrado = false;
int i = 0;
while (i < elementos.length && !encontrado) {
    // Es obvio: termina cuando encuentra o recorre todo
}

// ✅ Condición doble: ventana deslizante
int inicio = 0;
int fin = 0;
while (fin < datos.length && inicio <= fin) {
    // Relación clara entre inicio y fin
}

// ✅ Condición con sentinel
int indice = 0;
while (arreglo[indice] != VALOR_SENTINEL) {
    // Es obvio: termina al encontrar sentinel
}
```

### Anti-patrón: Condiciones complejas ❌

```java
// ❌ INCORRECTO: Demasiadas condiciones
boolean flag1 = false;
boolean flag2 = true;
boolean flag3 = false;
int contador = 0;
int limite = 100;

while (flag2 && !flag1 && contador < limite && !flag3 && otraCondicion()) {
    // ❌ No es claro cuándo termina
    // ❌ Demasiadas variables involucradas
    // ❌ Llamada a método en condición
}
```

**Problemas:**
- 5 subexpresiones en la condición
- Variables con nombres genéricos (`flag1`, `flag2`)
- Llamada a método en cada iteración
- No es obvio qué combinación causa la terminación

### Solución: Simplificar y nombrar bien ✅

```java
// ✅ CORRECTO: Condición simplificada
boolean elementoEncontrado = false;
int indice = 0;
int cantidadElementos = elementos.size();

while (!elementoEncontrado && indice < cantidadElementos) {
    if (elementos.get(indice).cumpleCondicion()) {
        elementoEncontrado = true;
    }
    indice = indice + 1;
}
```

### Patrón: Búsqueda lineal

```java
// ✅ Patrón estándar de búsqueda
public int buscar(int[] arreglo, int elemento) {
    boolean encontrado = false;
    int indice = 0;
    
    // Condición clara: busca mientras no encuentre y haya elementos
    while (indice < arreglo.length && !encontrado) {
        if (arreglo[indice] == elemento) {
            encontrado = true;
        } else {
            indice = indice + 1;
        }
    }
    
    if (encontrado) {
        return indice;
    } else {
        return -1;
    }
}
```

### Patrón: Procesamiento hasta condición

```java
// ✅ Patrón: procesar hasta cumplir criterio
public int procesarHastaLimite(int[] valores, int limite) {
    int suma = 0;
    int indice = 0;
    boolean limiteSuperado = false;
    
    // Condición clara: mientras haya elementos y no supere límite
    while (indice < valores.length && !limiteSuperado) {
        suma = suma + valores[indice];
        if (suma >= limite) {
            limiteSuperado = true;
        }
        indice = indice + 1;
    }
    
    return suma;
}
```

### Garantizar terminación

#### Lazos contados (siempre terminan)

```java
// ✅ Terminación garantizada: contador hasta límite fijo
for (int i = 0; i < 10; i = i + 1) {
    // Terminación garantizada: i eventualmente alcanza 10
}

// ✅ Terminación garantizada: recorrer arreglo
for (int i = 0; i < arreglo.length; i = i + 1) {
    // Terminación garantizada: i eventualmente alcanza length
}
```

#### Lazos con progreso garantizado

```java
// ✅ Progreso garantizado hacia terminación
int valor = 100;
while (valor > 0) {
    valor = valor - 10;  // Decrece en cada iteración → terminará
}

// ✅ Progreso garantizado: consumir lista
List<String> items = obtenerItems();
while (!items.isEmpty()) {
    String item = items.remove(0);  // Lista se reduce → terminará
    procesarItem(item);
}
```

### Anti-patrón: Terminación no garantizada ❌

```java
// ❌ PELIGRO: Posible lazo infinito
int valor = 10;
while (valor != 0) {
    valor = valor + 2;  // ❌ Nunca será 0, incrementa de a 2
}

// ❌ PELIGRO: Depende de factor externo
while (!archivo.estaListo()) {
    // ❌ ¿Qué pasa si el archivo nunca está listo?
    esperar(100);
}

// ❌ PELIGRO: Condición que podría no cambiar
boolean condicion = calcularCondicion();
while (condicion) {
    procesarDatos();
    // ❌ ¿condicion se actualiza dentro del lazo?
}
```

### Soluciones para casos problemáticos

#### Agregar timeout o límite de iteraciones

```java
// ✅ Seguro: Límite de intentos
int intentos = 0;
int MAX_INTENTOS = 100;
boolean condicion = calcularCondicion();

while (condicion && intentos < MAX_INTENTOS) {
    procesarDatos();
    condicion = calcularCondicion();
    intentos = intentos + 1;
}

if (intentos >= MAX_INTENTOS) {
    throw new RuntimeException("Máximo de intentos alcanzado");
}
```

#### Actualización explícita de condición

```java
// ✅ Actualización clara de la condición
boolean continuarProcesando = true;
int elementosProcesados = 0;

while (continuarProcesando && elementosProcesados < elementos.size()) {
    Elemento elem = elementos.get(elementosProcesados);
    boolean exitoso = procesarElemento(elem);
    
    if (!exitoso) {
        continuarProcesando = false;  // Actualización explícita
    }
    
    elementosProcesados = elementosProcesados + 1;
}
```

### Lazos while vs for

#### Usar `for` cuando el número de iteraciones es conocido

```java
// ✅ for cuando sabés cuántas iteraciones
for (int i = 0; i < 10; i = i + 1) {
    // Exactamente 10 iteraciones
}

for (int i = 0; i < arreglo.length; i = i + 1) {
    // Exactamente length iteraciones
}
```

#### Usar `while` cuando depende de condición

```java
// ✅ while cuando depende de condición dinámica
boolean encontrado = false;
int i = 0;
while (i < arreglo.length && !encontrado) {
    // Termina cuando encuentra O termina el arreglo
}

// ✅ while para entrada de usuario
Scanner scanner = new Scanner(System.in);
int opcion = -1;
while (opcion != 0) {
    opcion = scanner.nextInt();
    // Termina cuando usuario ingresa 0
}
```

### Documentar condiciones complejas

Si la condición es compleja pero necesaria, documentarla:

```java
// ✅ Condición compleja pero documentada
/**
 * Procesa elementos hasta que:
 * - Se procesan todos los elementos, O
 * - Se alcanza el límite de peso, O
 * - Se encuentra un elemento inválido
 */
public void procesarElementos(List<Elemento> elementos, double limiteP eso) {
    boolean limiteSuperado = false;
    boolean elementoInvalido = false;
    int indice = 0;
    double pesoAcumulado = 0.0;
    
    // Condición: tres formas de terminar claramente documentadas
    while (indice < elementos.size() 
           && !limiteSuperado 
           && !elementoInvalido) {
        
        Elemento elem = elementos.get(indice);
        
        if (!elem.esValido()) {
            elementoInvalido = true;
        } else {
            pesoAcumulado = pesoAcumulado + elem.getPeso();
            if (pesoAcumulado > limitePeso) {
                limiteSuperado = true;
            }
        }
        
        indice = indice + 1;
    }
}
```

### Resumen

```java
// ❌ Condición confusa
while (f1 && !f2 && c < L && m() && !f3) {
    // ¿Cuándo termina?
}

// ✅ Condición clara
boolean encontrado = false;
int indice = 0;
while (indice < elementos.length && !encontrado) {
    // Obvio: termina cuando encuentra o recorre todo
}
```

:::{warning}
Si necesitás más de 10 segundos para entender cuándo termina un lazo, la condición no es lo suficientemente clara.
:::
    indice = indice + 1;
}
