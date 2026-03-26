---
title: 0x1 - Documentación y Comentarios
---

# Serie 0x1 - Documentación y Comentarios

(regla-0x1000)=
## `0x1000` - La documentación debe seguir el formato Javadoc estándar

### Explicación

Javadoc es el sistema de documentación estándar de Java. Permite generar documentación HTML a partir de comentarios especiales en el código. Toda la documentación pública de la API de Java está generada con Javadoc, y es el formato que se espera en proyectos profesionales.

### Justificación

1. **Estándar de la industria**: Javadoc es la convención universal en el ecosistema Java.
2. **Generación automática**: Permite crear documentación HTML profesional.
3. **Integración con IDEs**: Los IDEs muestran Javadoc en tooltips y autocompletado.
4. **Consistencia**: Formato uniforme facilita lectura y mantenimiento.
5. **Contratos documentados**: Formaliza precondiciones, postcondiciones y efectos secundarios.

### Sintaxis básica de Javadoc

```java
/**
 * Descripción breve del elemento (primera oración).
 * 
 * Descripción extendida opcional con más detalles.
 * Puede ocupar múltiples líneas y párrafos.
 * 
 * @param nombreParametro descripción del parámetro
 * @return descripción del valor de retorno
 * @throws TipoExcepcion cuándo se lanza esta excepción
 */
```

### Estructura de un bloque Javadoc

```
/**                          ← Inicio obligatorio (dos asteriscos)
 * Descripción principal     ← Texto descriptivo
 * @tag valor                ← Tags de documentación
 */                          ← Fin obligatorio
```

:::{important}
- Debe comenzar con `/**` (dos asteriscos), no `/*`
- Cada línea interna comienza con ` * ` (espacio, asterisco, espacio)
- Termina con `*/`
:::

### Ejemplos básicos

#### Documentar un método

```java
/**
 * Devuelve el valor absoluto de un número.
 * 
 * @param numero el número al que eliminar su signo
 * @return el número sin signo
 */
private static long valorAbsoluto(long numero) {
    if (numero < 0) {
        return -numero;
    }
    return numero;
}
```

#### Documentar con postcondiciones

Para mayor rigor, podés incluir postcondiciones:

```java
/**
 * Devuelve el valor absoluto de un número.
 * 
 * @param numero el número al que eliminar su signo
 * @return el número sin signo
 *         POST: se devuelve un valor >= 0 con la misma magnitud que numero
 */
private static long valorAbsoluto(long numero) {
    if (numero < 0) {
        return -numero;
    }
    return numero;
}
```

#### Documentar una clase

```java
/**
 * Representa una cuenta bancaria con operaciones básicas de
 * depósito y retiro.
 * 
 * Esta clase mantiene el saldo actual y el historial de
 * transacciones realizadas.
 */
public class CuentaBancaria {
    // ...
}
```

#### Documentar un atributo

```java
/**
 * Saldo actual de la cuenta en pesos argentinos.
 * INV: saldo >= 0 (no se permiten saldos negativos)
 */
private double saldo;

/**
 * Número único de identificación de la cuenta.
 * INV: numeroCuenta != null && numeroCuenta.matches("[0-9]{10}")
 */
private String numeroCuenta;
```

### Tags principales de Javadoc

| Tag | Uso | Requerido para |
|-----|-----|----------------|
| `@param` | Describe un parámetro | Todo método con parámetros |
| `@return` | Describe el valor de retorno | Todo método que retorna valor (no void) |
| `@throws` | Documenta excepciones | Todo método que lanza excepciones |
| `@see` | Referencia a otro elemento | Opcional |
| `@since` | Versión de introducción | Opcional |
| `@deprecated` | Marca elemento obsoleto | Cuando sea necesario |
| `@author` | Autor del código | Clases principales |
| `@version` | Versión del código | Clases principales |

### Uso de tags

#### @param - Documentar parámetros

```java
/**
 * Calcula el interés compuesto.
 * 
 * @param capitalInicial monto inicial de la inversión
 * @param tasaAnual tasa de interés anual (expresada como decimal, ej: 0.05 para 5%)
 * @param periodoAnios cantidad de años de la inversión
 * @return el monto final incluyendo el interés compuesto
 */
public double calcularInteresCompuesto(double capitalInicial, 
                                        double tasaAnual, 
                                        int periodoAnios) {
    return capitalInicial * Math.pow(1 + tasaAnual, periodoAnios);
}
```

:::{warning}
**Error común**: Usar `@returns` (con 's'). El tag correcto es `@return` (sin 's').

```java
// ❌ Incorrecto
@returns el resultado

// ✅ Correcto
@return el resultado
```
:::

#### @return - Documentar valor de retorno

```java
/**
 * Busca un usuario por su identificador.
 * 
 * @param id identificador único del usuario
 * @return el usuario encontrado, o null si no existe
 */
public Usuario buscarPorId(int id) {
    // ...
}

/**
 * Calcula el promedio de una lista de números.
 * 
 * @param numeros lista de números a promediar
 * @return el promedio aritmético
 *         POST: resultado >= min(numeros) && resultado <= max(numeros)
 */
public double calcularPromedio(List<Double> numeros) {
    // ...
}
```

#### @throws - Documentar excepciones

Ver también regla {ref}`regla-0x1002`:

```java
/**
 * Lee un archivo de configuración.
 * 
 * @param ruta ruta al archivo
 * @return configuración leída
 * @throws ArchivoNoEncontradoException si el archivo no existe
 * @throws ConfiguracionInvalidaException si el formato es incorrecto
 * @throws IOException si hay errores de lectura del sistema de archivos
 */
public Configuracion leerConfiguracion(String ruta) 
        throws ArchivoNoEncontradoException, 
               ConfiguracionInvalidaException, 
               IOException {
    // ...
}
```

### Formato y estilo del texto

#### Primera oración: Resumen

La primera oración (hasta el primer punto) es el resumen que aparece en el índice de Javadoc:

```java
/**
 * Calcula el área de un círculo. Esta implementación usa la
 * fórmula estándar πr² con alta precisión.
 * 
 * @param radio el radio del círculo en metros
 * @return el área en metros cuadrados
 */
```

#### Usar HTML cuando sea necesario

Javadoc soporta HTML para formateo avanzado:

```java
/**
 * Procesa una lista de elementos.
 * <p>
 * El procesamiento incluye:
 * <ul>
 *   <li>Validación de cada elemento</li>
 *   <li>Transformación según reglas</li>
 *   <li>Ordenamiento final</li>
 * </ul>
 * <p>
 * <strong>Nota:</strong> Este método modifica la lista original.
 * 
 * @param elementos lista a procesar
 */
public void procesar(List<String> elementos) {
    // ...
}
```

#### Código en la documentación

Usar `{@code}` para código inline y bloques:

```java
/**
 * Busca un elemento en la lista.
 * <p>
 * Ejemplo de uso:
 * <pre>{@code
 * Lista<String> nombres = new Lista<>();
 * nombres.agregar("Juan");
 * String encontrado = nombres.buscar("Juan");
 * }</pre>
 * 
 * @param elemento elemento a buscar
 * @return el elemento si se encuentra, {@code null} si no existe
 */
public String buscar(String elemento) {
    // ...
}
```

### Referencias cruzadas

#### @see para vincular documentación

```java
/**
 * Calcula el área de un rectángulo.
 * 
 * @param ancho ancho del rectángulo
 * @param alto alto del rectángulo
 * @return el área calculada
 * @see #calcularPerimetro(double, double)
 * @see java.lang.Math#abs(double)
 */
public double calcularArea(double ancho, double alto) {
    return ancho * alto;
}
```

### Precondiciones, Postcondiciones e Invariantes

Para documentación rigurosa, incluir contratos:

```java
/**
 * Retira dinero de la cuenta.
 * 
 * PRE: monto > 0 && monto <= saldo
 * POST: saldo = saldoAnterior - monto
 * 
 * @param monto cantidad a retirar
 * @return el nuevo saldo después del retiro
 * @throws SaldoInsuficienteException si monto > saldo
 * @throws MontoInvalidoException si monto <= 0
 */
public double retirar(double monto) 
        throws SaldoInsuficienteException, MontoInvalidoException {
    // ...
}
```

### Documentación de clases

```java
/**
 * Gestiona operaciones de una cuenta bancaria.
 * <p>
 * Una cuenta bancaria mantiene:
 * <ul>
 *   <li>Saldo actual (no negativo)</li>
 *   <li>Historial de transacciones</li>
 *   <li>Información del titular</li>
 * </ul>
 * <p>
 * INV: saldo >= 0
 * INV: numeroCuenta != null
 * 
 * @author Cátedra Programación II
 * @version 1.0
 * @since 2025
 */
public class CuentaBancaria {
    // ...
}
```

### Generación de documentación

Para generar HTML desde Javadoc:

```bash
# Generar documentación de todo el proyecto
javadoc -d docs -sourcepath src -subpackages com.proyecto

# Generar para un paquete específico
javadoc -d docs com.proyecto.modelo
```

:::{tip}
Los IDEs pueden generar la estructura básica de Javadoc automáticamente:
- **IntelliJ IDEA**: Escribir `/**` antes del método y presionar Enter
- **Eclipse**: Escribir `/**` antes del método y presionar Enter
- **VS Code**: Extensión "Document This"
:::

### Errores comunes

```java
// ❌ Usar comentario regular en lugar de Javadoc
// Este método calcula el promedio
public double calcular() { ... }

// ❌ Usar @returns en lugar de @return
/**
 * @returns el promedio
 */

// ❌ No documentar todos los parámetros
/**
 * @param a primer número
 * // Falta documentar b!
 */
public int sumar(int a, int b) { ... }

// ❌ Documentación vacía o no informativa
/**
 * Método calcular.
 * @param valor un valor
 * @return retorna un valor
 */

// ✅ Correcto
/**
 * Calcula el cuadrado de un número entero.
 * 
 * @param valor el número a elevar al cuadrado
 * @return el resultado de valor²
 */
public int calcular(int valor) { ... }
```

(regla-0x1001)=
## `0x1001` - Las clases, atributos y métodos llevan documentación Javadoc

### Explicación

Todo elemento del código (clases, interfaces, métodos, atributos) debe estar documentado con Javadoc, incluyendo miembros privados. La documentación no es opcional ni está limitada a APIs públicas.

### Justificación

1. **Comprensión futura**: Documentar privados ayuda a entender el razonamiento detrás de decisiones de diseño.
2. **Mantenimiento**: Facilita modificaciones por otros (o por vos mismo meses después).
3. **Revisión de código**: Permite validar que la implementación coincide con la intención.
4. **Transferencia de conocimiento**: Documenta el "por qué", no solo el "qué".
5. **Disciplina profesional**: Refleja rigor y atención al detalle.

### Elementos que requieren documentación

| Elemento | Requiere Javadoc | Excepción |
|----------|------------------|-----------|
| Clases públicas | ✅ Siempre | Ninguna |
| Clases privadas/internas | ✅ Siempre | Ninguna |
| Interfaces | ✅ Siempre | Ninguna |
| Métodos públicos | ✅ Siempre | Ninguna |
| Métodos privados | ✅ Siempre | Ninguna |
| Atributos públicos | ✅ Siempre | Ninguna |
| Atributos privados | ✅ Siempre | Solo si triviales (ej: `i` en bucle) |
| Constructores | ✅ Siempre | Ninguna |
| Constantes | ✅ Siempre | Ninguna |

### Ejemplos

#### Documentar una clase

```java
/**
 * Representa una cuenta bancaria con operaciones de depósito y retiro.
 * <p>
 * Mantiene el saldo actual y un historial de todas las transacciones
 * realizadas. El saldo nunca puede ser negativo.
 * <p>
 * INV: saldo >= 0
 * INV: numeroCuenta != null && !numeroCuenta.isEmpty()
 * INV: historial != null
 * 
 * @author Cátedra Programación II
 * @version 1.0
 */
public class CuentaBancaria {
    // ...
}
```

#### Documentar atributos

```java
public class Estudiante {
    /**
     * Número de legajo único del estudiante.
     * INV: legajo > 0
     */
    private int legajo;
    
    /**
     * Promedio actual de calificaciones del estudiante.
     * INV: 0.0 <= promedio <= 10.0
     */
    private double promedio;
    
    /**
     * Lista de materias en las que el estudiante está inscripto.
     * INV: materias != null
     */
    private List<Materia> materias;
    
    /**
     * Indica si el estudiante está actualmente cursando.
     * true si tiene al menos una materia activa, false en caso contrario.
     */
    private boolean estaActivo;
}
```

#### Documentar métodos públicos

```java
/**
 * Deposita dinero en la cuenta.
 * <p>
 * Agrega el monto especificado al saldo actual y registra
 * la transacción en el historial.
 * <p>
 * PRE: monto > 0
 * POST: saldo = saldoAnterior + monto
 * POST: historial contiene nueva transacción de tipo DEPOSITO
 * 
 * @param monto cantidad a depositar (debe ser positiva)
 * @throws MontoInvalidoException si monto <= 0
 */
public void depositar(double monto) throws MontoInvalidoException {
    // ...
}
```

#### Documentar métodos privados

```java
/**
 * Valida que un número de cuenta tenga el formato correcto.
 * <p>
 * El formato válido es: 10 dígitos numéricos.
 * Ejemplo: "1234567890"
 * 
 * @param numero el número de cuenta a validar
 * @return true si el formato es válido, false en caso contrario
 */
private boolean esNumeroValido(String numero) {
    return numero != null && numero.matches("[0-9]{10}");
}

/**
 * Registra una transacción en el historial.
 * <p>
 * Este método es interno y asume que la transacción ya fue validada.
 * No realiza verificaciones adicionales.
 * 
 * PRE: transaccion != null
 * POST: historial.size() = historial.size()@pre + 1
 * 
 * @param transaccion la transacción a registrar (no null)
 */
private void registrarTransaccion(Transaccion transaccion) {
    historial.add(transaccion);
}
```

### Clases abstractas: Documentación crítica

:::{important}
**Para clases abstractas y métodos abstractos**, la documentación es **crítica**. Debe establecer:

1. **Qué debe hacer** el método en las subclases
2. **Qué NO debe hacer** (restricciones)
3. **Precondiciones y postcondiciones** esperadas
4. **Invariantes** que deben mantenerse
:::

#### Ejemplo: Clase abstracta bien documentada

```java
/**
 * Clase base para calculadoras con operaciones aritméticas.
 * <p>
 * Las subclases deben implementar las operaciones básicas respetando
 * los siguientes contratos:
 * <ul>
 *   <li>Todas las operaciones deben manejar overflow/underflow</li>
 *   <li>La división debe lanzar excepción si divisor es cero</li>
 *   <li>Los resultados deben redondearse a 2 decimales</li>
 * </ul>
 * <p>
 * INV: precision == 2 (definido en la implementación)
 */
public abstract class CalculadoraBase {
    
    /**
     * Suma dos números.
     * <p>
     * Las implementaciones DEBEN:
     * - Manejar overflow detectando si el resultado excede los límites
     * - Retornar el resultado redondeado a 2 decimales
     * <p>
     * Las implementaciones NO DEBEN:
     * - Modificar los parámetros recibidos
     * - Lanzar excepciones (excepto overflow)
     * <p>
     * POST: |resultado - (a + b)| < 0.01
     * 
     * @param a primer sumando
     * @param b segundo sumando
     * @return la suma de a y b, redondeada a 2 decimales
     * @throws OverflowException si el resultado excede los límites numéricos
     */
    public abstract double sumar(double a, double b) throws OverflowException;
    
    /**
     * Divide dos números.
     * <p>
     * Las implementaciones DEBEN:
     * - Validar que el divisor no sea cero ANTES de dividir
     * - Lanzar excepción específica para división por cero
     * - Retornar resultado redondeado a 2 decimales
     * <p>
     * PRE: divisor != 0
     * POST: |resultado - (dividendo / divisor)| < 0.01
     * 
     * @param dividendo número a dividir
     * @param divisor número por el cual dividir
     * @return el resultado de dividendo / divisor
     * @throws DivisionPorCeroException si divisor == 0
     */
    public abstract double dividir(double dividendo, double divisor) 
            throws DivisionPorCeroException;
}
```

### Documentación de constructores

```java
/**
 * Crea una nueva cuenta bancaria con saldo inicial.
 * <p>
 * La cuenta se crea en estado activo y con historial vacío.
 * <p>
 * PRE: numeroCuenta != null && !numeroCuenta.isEmpty()
 * PRE: saldoInicial >= 0
 * POST: this.saldo == saldoInicial
 * POST: this.estaActiva() == true
 * 
 * @param numeroCuenta número único de la cuenta (no null, no vacío)
 * @param nombreTitular nombre del titular de la cuenta
 * @param saldoInicial saldo con el que se abre la cuenta (debe ser >= 0)
 * @throws IllegalArgumentException si numeroCuenta es null o vacío
 * @throws SaldoInvalidoException si saldoInicial < 0
 */
public CuentaBancaria(String numeroCuenta, 
                       String nombreTitular, 
                       double saldoInicial) 
        throws SaldoInvalidoException {
    // ...
}
```

### Documentación de interfaces

```java
/**
 * Define el contrato para objetos que pueden compararse entre sí.
 * <p>
 * Las clases que implementen esta interfaz deben poder determinar
 * un orden natural entre sus instancias.
 * <p>
 * La implementación debe ser consistente con equals():
 * si a.equals(b) == true, entonces a.compararCon(b) == 0
 * 
 * @param <T> tipo de objetos que pueden compararse
 */
public interface Comparable<T> {
    
    /**
     * Compara este objeto con otro del mismo tipo.
     * <p>
     * El método debe retornar:
     * - Valor negativo si this < otro
     * - Cero si this == otro
     * - Valor positivo si this > otro
     * <p>
     * PRE: otro != null
     * POST: sgn(x.compararCon(y)) == -sgn(y.compararCon(x))
     * 
     * @param otro objeto con el cual comparar
     * @return negativo, cero, o positivo según el orden relativo
     * @throws NullPointerException si otro es null
     */
    int compararCon(T otro);
}
```

### Nivel de detalle apropiado

#### Métodos triviales vs. complejos

```java
// Método trivial - Documentación concisa
/**
 * Obtiene el nombre del titular.
 * 
 * @return el nombre del titular
 */
public String getNombre() {
    return nombre;
}

// Método complejo - Documentación detallada
/**
 * Procesa un pago con tarjeta de crédito.
 * <p>
 * El procesamiento incluye:
 * 1. Validación de datos de la tarjeta
 * 2. Verificación de fondos disponibles
 * 3. Aplicación de comisiones según tipo de tarjeta
 * 4. Registro de transacción en sistema contable
 * 5. Envío de notificación al cliente
 * <p>
 * Si algún paso falla, se revierte la transacción completa.
 * <p>
 * PRE: tarjeta != null && tarjeta.esValida()
 * PRE: monto > 0
 * POST: si retorna true, el saldo de la cuenta disminuyó en (monto + comision)
 * 
 * @param tarjeta datos de la tarjeta a procesar
 * @param monto monto a cobrar (sin incluir comisiones)
 * @return true si el pago fue exitoso, false si fue rechazado
 * @throws TarjetaInvalidaException si la tarjeta está vencida o bloqueada
 * @throws SaldoInsuficienteException si no hay fondos suficientes
 * @throws ErrorConexionException si no se puede conectar con el banco
 */
public boolean procesarPago(Tarjeta tarjeta, double monto)
        throws TarjetaInvalidaException, 
               SaldoInsuficienteException, 
               ErrorConexionException {
    // ...
}
```

### Documentar decisiones de diseño

Los atributos privados deben documentar **por qué** existen:

```java
/**
 * Cache de usuarios consultados recientemente.
 * <p>
 * Optimización: evita consultas repetidas a la base de datos
 * para usuarios accedidos frecuentemente. El cache se invalida
 * cada 5 minutos.
 * <p>
 * INV: cache != null
 * INV: cache.size() <= MAX_ELEMENTOS_CACHE
 */
private Map<Integer, Usuario> cacheUsuarios;

/**
 * Lock para sincronización de acceso concurrente al saldo.
 * <p>
 * Necesario porque múltiples hilos pueden intentar modificar
 * el saldo simultáneamente (depósitos y retiros concurrentes).
 */
private final Object lockSaldo = new Object();
```

### Documentación incremental

No es necesario documentar todo de una vez. Priorizá:

1. **APIs públicas** → Documentación completa inmediata
2. **Métodos complejos** → Documentar antes de implementar (ayuda a clarificar)
3. **Miembros privados** → Documentar cuando agregue valor (no lo obvio)
4. **Constantes** → Documentar valor y unidades

```java
// ✅ Atributo privado obvio - Documentación mínima aceptable
/**
 * Nombre completo del estudiante.
 */
private String nombre;

// ✅ Atributo privado con lógica - Documentación detallada
/**
 * Contador de intentos fallidos de login.
 * <p>
 * Se incrementa en cada intento fallido y se resetea a 0
 * cuando el login es exitoso. Si alcanza MAX_INTENTOS,
 * la cuenta se bloquea automáticamente.
 * <p>
 * INV: 0 <= intentosFallidos <= MAX_INTENTOS
 */
private int intentosFallidos;
```

### Clases abstractas: Documentación crítica

Las clases abstractas requieren especial atención porque definen **contratos** para subclases:

```java
/**
 * Clase base abstracta para gestores de archivos.
 * <p>
 * Las subclases DEBEN implementar los métodos de lectura y escritura
 * respetando los siguientes contratos:
 * <p>
 * <strong>Responsabilidades de las subclases:</strong>
 * <ul>
 *   <li>Validar que los archivos existen antes de leer</li>
 *   <li>Crear directorios necesarios antes de escribir</li>
 *   <li>Cerrar recursos (streams) en bloques finally</li>
 *   <li>Lanzar excepciones específicas, no genéricas</li>
 * </ul>
 * <p>
 * <strong>Restricciones de las subclases:</strong>
 * <ul>
 *   <li>NO modificar archivos durante lectura</li>
 *   <li>NO mantener streams abiertos después de operación</li>
 *   <li>NO silenciar excepciones sin logging</li>
 * </ul>
 */
public abstract class GestorArchivosBase {
    
    /**
     * Lee el contenido completo de un archivo.
     * <p>
     * Las implementaciones DEBEN:
     * - Verificar que el archivo existe antes de intentar leer
     * - Cerrar el stream en bloque finally
     * - Retornar contenido en codificación UTF-8
     * <p>
     * Las implementaciones NO DEBEN:
     * - Retornar null (lanzar excepción si hay error)
     * - Modificar el archivo durante lectura
     * - Cachear el contenido sin avisar
     * <p>
     * PRE: ruta != null && !ruta.isEmpty()
     * POST: resultado != null
     * 
     * @param ruta ruta absoluta o relativa al archivo
     * @return contenido del archivo como String
     * @throws ArchivoNoEncontradoException si el archivo no existe
     * @throws ErrorLecturaException si hay error al leer
     */
    public abstract String leer(String ruta) 
            throws ArchivoNoEncontradoException, ErrorLecturaException;
}
```

:::{warning}
En clases abstractas, no solo describas lo obvio del código. Documentá el **contrato** que las subclases deben respetar, incluyendo qué deben hacer y qué **no** deben hacer.
:::

### Documentar overrides

Cuando sobreescribís un método, podés referenciar la documentación padre:

```java
/**
 * {@inheritDoc}
 * <p>
 * Esta implementación ordena por edad de menor a mayor.
 */
@Override
public int compararCon(Persona otra) {
    return this.edad - otra.edad;
}
```

O documentar completamente si el comportamiento es significativamente diferente:

```java
/**
 * Compara esta persona con otra por edad.
 * <p>
 * A diferencia de otras implementaciones, esta versión
 * considera iguales a personas con diferencia de edad menor a 1 año.
 * 
 * @param otra persona con la cual comparar
 * @return negativo si this es menor, 0 si son similares, positivo si this es mayor
 */
@Override
public int compararCon(Persona otra) {
    int diferencia = this.edad - otra.edad;
    return Math.abs(diferencia) < 1 ? 0 : diferencia;
}
```

### Documentación de enums

```java
/**
 * Estados posibles de un pedido en el sistema.
 * <p>
 * El ciclo de vida normal es: PENDIENTE → EN_PROCESO → COMPLETADO.
 * Un pedido puede pasar a CANCELADO desde cualquier estado excepto COMPLETADO.
 */
public enum EstadoPedido {
    
    /**
     * Pedido recién creado, esperando procesamiento.
     */
    PENDIENTE,
    
    /**
     * Pedido siendo preparado o empaquetado.
     */
    EN_PROCESO,
    
    /**
     * Pedido entregado exitosamente al cliente.
     * Estado final, no puede cambiar.
     */
    COMPLETADO,
    
    /**
     * Pedido cancelado por el cliente o por el sistema.
     * Estado final, no puede cambiar.
     */
    CANCELADO
}
```

### Herramientas del IDE para generar Javadoc

Todos los IDEs pueden generar la estructura básica:

```java
// 1. Escribir la firma del método
public void depositar(double monto) throws MontoInvalidoException {
    // ...
}

// 2. Colocar cursor antes del método
// 3. Escribir /** y presionar Enter
// 4. El IDE genera automáticamente:

/**
 * 
 * @param monto
 * @throws MontoInvalidoException
 */
public void depositar(double monto) throws MontoInvalidoException {
    // ...
}

// 5. Completar con descripciones apropiadas
```

:::{tip}
**Atajos de IDE:**
- **IntelliJ IDEA**: `/**` + Enter
- **Eclipse**: `/**` + Enter
- **VS Code**: `/**` + Enter (con extensión Java)

Esto genera la estructura con todos los tags necesarios. Solo tenés que llenar las descripciones.
:::

### Errores comunes

```java
// ❌ Sin documentación
public class Ejemplo {
    private int valor;
    public int getValor() { return valor; }
}

// ❌ Documentación vacía (placeholder)
/**
 * 
 */
public class Ejemplo { ... }

// ❌ Documentación no informativa
/**
 * Clase Ejemplo.
 */
public class Ejemplo { ... }

/**
 * Método getValor.
 * @return retorna valor
 */
public int getValor() { ... }

// ✅ Documentación informativa
/**
 * Representa un ejemplo con valor numérico configurable.
 * <p>
 * El valor puede modificarse en tiempo de ejecución y
 * afecta el comportamiento del método calcular().
 */
public class Ejemplo {
    /**
     * Valor numérico usado en cálculos internos.
     * INV: valor >= 0
     */
    private int valor;
    
    /**
     * Obtiene el valor actual usado en los cálculos.
     * 
     * @return el valor numérico actual (siempre >= 0)
     */
    public int getValor() {
        return valor;
    }
}
```

:::{important}
La documentación debe agregar **valor semántico**, no simplemente parafrasear el código. Explicá el **propósito**, las **restricciones** y el **contexto** de uso.
:::

(regla-0x1002)=
## `0x1002` - Todas las excepciones que lancemos deben estar documentadas con `@throws`

### Explicación

Todo método que pueda lanzar excepciones (tanto checked como unchecked) debe documentarlas con el tag `@throws`, especificando en qué circunstancias se lanza cada una. Si la misma excepción se lanza en múltiples contextos, cada uno debe estar explicado por separado.

### Justificación

1. **Contrato explícito**: Los usuarios del método saben qué excepciones esperar y cuándo.
2. **Manejo correcto**: Facilita escribir bloques try-catch apropiados.
3. **Prevención de errores**: Ayuda a identificar casos límite que deben manejarse.
4. **Documentación completa**: Captura tanto excepciones propias como de bibliotecas subyacentes.
5. **Debugging**: Facilita rastrear el origen de excepciones.

### Sintaxis

```java
/**
 * Descripción del método.
 * 
 * @param parametro descripción
 * @return descripción del retorno
 * @throws TipoExcepcion1 cuándo y por qué se lanza
 * @throws TipoExcepcion2 cuándo y por qué se lanza
 */
```

### Excepciones checked (controladas)

Deben documentarse **obligatoriamente** porque forman parte de la firma del método:

```java
/**
 * Lee un archivo de configuración y lo parsea.
 * 
 * @param ruta ruta al archivo de configuración
 * @return objeto de configuración parseado
 * @throws ArchivoNoEncontradoException si el archivo no existe en la ruta especificada
 * @throws ConfiguracionInvalidaException si el formato del archivo es incorrecto
 * @throws IOException si hay errores de lectura del sistema de archivos
 */
public Configuracion leerConfiguracion(String ruta) 
        throws ArchivoNoEncontradoException, 
               ConfiguracionInvalidaException, 
               IOException {
    // ...
}
```

### Excepciones unchecked (no controladas)

También deben documentarse, aunque no estén en la firma:

```java
/**
 * Divide dos números enteros.
 * 
 * @param dividendo número a dividir
 * @param divisor número por el cual dividir
 * @return resultado de la división entera
 * @throws ArithmeticException si divisor es 0 (división por cero)
 * @throws IllegalArgumentException si divisor es negativo
 */
public int dividir(int dividendo, int divisor) {
    if (divisor == 0) {
        throw new ArithmeticException("División por cero no permitida");
    }
    if (divisor < 0) {
        throw new IllegalArgumentException("Divisor no puede ser negativo");
    }
    return dividendo / divisor;
}
```

:::{important}
Documentá **todas** las excepciones que lances intencionalmente, sean checked o unchecked. Esto incluye `IllegalArgumentException`, `NullPointerException`, `IllegalStateException`, etc.
:::

### Múltiples contextos para la misma excepción

Si la misma excepción se lanza en diferentes situaciones, explicá cada una:

```java
/**
 * Procesa una transacción bancaria.
 * 
 * @param transaccion transacción a procesar
 * @throws TransaccionInvalidaException si el monto es negativo o cero
 * @throws TransaccionInvalidaException si la cuenta origen no existe
 * @throws TransaccionInvalidaException si la cuenta destino no existe
 * @throws SaldoInsuficienteException si la cuenta origen no tiene fondos suficientes
 */
public void procesar(Transaccion transaccion) 
        throws TransaccionInvalidaException, SaldoInsuficienteException {
    // ...
}
```

O mejor aún, combinar contextos relacionados:

```java
/**
 * Procesa una transacción bancaria.
 * 
 * @param transaccion transacción a procesar
 * @throws TransaccionInvalidaException en los siguientes casos:
 *         - El monto es negativo o cero
 *         - La cuenta origen no existe
 *         - La cuenta destino no existe
 * @throws SaldoInsuficienteException si la cuenta origen no tiene fondos suficientes
 */
```

### Excepciones de bibliotecas externas

Incluir excepciones que provengan de código que llamamos:

```java
/**
 * Guarda datos en formato JSON en un archivo.
 * 
 * @param datos objeto a serializar
 * @param archivo ruta del archivo destino
 * @throws IOException si hay error al escribir en el archivo (puede provenir de FileWriter)
 * @throws JsonProcessingException si los datos no pueden serializarse a JSON (de Jackson)
 * @throws IllegalArgumentException si datos es null
 */
public void guardarJson(Object datos, String archivo) 
        throws IOException, JsonProcessingException {
    if (datos == null) {
        throw new IllegalArgumentException("Los datos no pueden ser null");
    }
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(new File(archivo), datos);
}
```

### Documentar excepciones indirectas

Ver también regla {ref}`regla-0x1004`:

```java
/**
 * Procesa un pedido completo.
 * <p>
 * Este método realiza validación interna antes de procesar.
 * 
 * @param pedido el pedido a procesar
 * @throws PedidoInvalidoException si el pedido no pasa validación 
 *         (lanzada por validarPedido())
 * @throws SaldoInsuficienteException si no hay fondos 
 *         (lanzada por cobrarPago())
 */
public void procesar(Pedido pedido) 
        throws PedidoInvalidoException, SaldoInsuficienteException {
    validarPedido(pedido);  // Puede lanzar PedidoInvalidoException
    cobrarPago(pedido);     // Puede lanzar SaldoInsuficienteException
    enviarProducto(pedido);
}

/**
 * Valida que un pedido tenga todos los datos requeridos.
 * 
 * @param pedido pedido a validar
 * @throws PedidoInvalidoException si falta información obligatoria
 */
private void validarPedido(Pedido pedido) throws PedidoInvalidoException {
    // ...
}
```

### Orden de los tags @throws

Listar en orden de probabilidad o importancia:

```java
/**
 * Ejecuta una consulta SQL y retorna resultados.
 * 
 * @param query consulta SQL a ejecutar
 * @return resultados de la consulta
 * @throws IllegalArgumentException si query es null o vacío (más probable)
 * @throws SQLException si hay error en la sintaxis SQL
 * @throws ConexionCerradaException si la conexión está cerrada
 * @throws TimeoutException si la consulta excede el tiempo límite (menos probable)
 */
```

### Formato de descripción de excepciones

```java
// ✅ Descripción clara con condición específica
@throws SaldoInsuficienteException si el monto a retirar es mayor que el saldo disponible

// ✅ Con contexto adicional
@throws IOException si hay error al leer el archivo o si no se tienen permisos de lectura

// ❌ Descripción vaga
@throws Exception si algo sale mal

// ❌ Solo repetir el nombre
@throws SaldoInsuficienteException cuando el saldo es insuficiente
```

### Excepciones anidadas y causa raíz

Documentar cuando envolvés excepciones:

```java
/**
 * Carga configuración desde archivo remoto.
 * 
 * @param url URL del archivo de configuración
 * @return configuración cargada
 * @throws ConfiguracionException si no se puede cargar la configuración.
 *         Puede ser causada por:
 *         - IOException (problemas de red o lectura)
 *         - ParseException (formato inválido)
 *         - TimeoutException (servidor no responde)
 */
public Configuracion cargarRemota(String url) throws ConfiguracionException {
    try {
        // lógica de carga
    } catch (IOException | ParseException | TimeoutException e) {
        throw new ConfiguracionException("Error al cargar configuración", e);
    }
}
```

:::{tip}
Cuando envuelvas múltiples excepciones en una propia, documentá las posibles causas raíz. Esto ayuda a los usuarios del método a entender qué puede fallar.
:::

### Completitud vs. Pragmatismo

```java
// ✅ Documentación pragmática - Excepciones relevantes
/**
 * Parsea un número desde texto.
 * 
 * @param texto representación textual del número
 * @return el número parseado
 * @throws NumberFormatException si texto no es un número válido
 */
public int parsear(String texto) {
    return Integer.parseInt(texto);  // Puede lanzar NumberFormatException
}

// ⚠️ Sobre-documentación - Excepciones muy improbables
/**
 * Parsea un número desde texto.
 * 
 * @param texto representación textual del número
 * @return el número parseado
 * @throws NumberFormatException si texto no es un número válido
 * @throws OutOfMemoryError si no hay memoria para crear el Integer (extremadamente raro)
 * @throws StackOverflowError si la pila se agota (casi imposible aquí)
 */
```

:::{note}
Usá criterio: documentá excepciones **razonables** y **relevantes**. No documentes todos los errores teóricamente posibles del JVM (`OutOfMemoryError`, `StackOverflowError`, etc.) a menos que sean realmente probables en tu contexto.
:::

(regla-0x1003)=
## `0x1003` - Las excepciones de tiempo de ejecución deben documentar cómo evitar su lanzamiento

### Explicación

Las excepciones no controladas (*unchecked exceptions*) - subclases de `RuntimeException` - indican errores de programación que pueden evitarse. La documentación debe explicar **cómo prevenir** que se lancen, no solo cuándo ocurren.

### Justificación

1. **Prevención proactiva**: Enseña a los usuarios del método cómo usarlo correctamente.
2. **Debugging eficiente**: Facilita identificar la causa raíz del problema.
3. **Código defensivo**: Incentiva validaciones antes de llamar al método.
4. **Documentación educativa**: Explica las precondiciones implícitas.
5. **Mejores prácticas**: Alineado con el principio "fail-fast".

### Sintaxis recomendada

```java
/**
 * ...
 * @throws TipoExcepcion si [condición que causa la excepción].
 *         Para evitar esta excepción, [acción preventiva].
 */
```

### Ejemplos

#### División por cero

```java
/**
 * Divide dos números.
 * 
 * @param dividendo número a dividir
 * @param divisor número por el cual dividir
 * @return resultado de dividendo / divisor
 * @throws IllegalArgumentException si divisor es 0.
 *         Para evitar esta excepción, verificar que divisor != 0 antes de llamar.
 */
public double dividir(double dividendo, double divisor) {
    if (divisor == 0) {
        throw new IllegalArgumentException("El divisor no puede ser cero");
    }
    return dividendo / divisor;
}
```

#### Validación de parámetros null

```java
/**
 * Registra un usuario en el sistema.
 * 
 * @param usuario usuario a registrar
 * @throws NullPointerException si usuario es null.
 *         Para evitar esta excepción, asegurar que el objeto usuario
 *         esté inicializado antes de llamar a este método.
 * @throws IllegalArgumentException si el email del usuario es inválido.
 *         Para evitar esta excepción, validar el email con un Validador
 *         antes de crear el objeto Usuario.
 */
public void registrar(Usuario usuario) {
    if (usuario == null) {
        throw new NullPointerException("El usuario no puede ser null");
    }
    if (!usuario.getEmail().contains("@")) {
        throw new IllegalArgumentException("Email inválido");
    }
    // registrar
}
```

#### Índices fuera de rango

```java
/**
 * Obtiene un elemento en la posición especificada.
 * 
 * @param indice posición del elemento (0-based)
 * @return el elemento en la posición indicada
 * @throws IndexOutOfBoundsException si indice < 0 o indice >= size().
 *         Para evitar esta excepción, verificar que 0 <= indice < size()
 *         antes de llamar al método.
 */
public Elemento obtener(int indice) {
    if (indice < 0 || indice >= elementos.size()) {
        throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
    }
    return elementos.get(indice);
}
```

#### Estado inválido

```java
/**
 * Inicia el procesamiento de datos.
 * 
 * @throws IllegalStateException si el procesador ya fue iniciado.
 *         Para evitar esta excepción, verificar con estaIniciado()
 *         antes de llamar a iniciar().
 * @throws IllegalStateException si no se cargaron los datos requeridos.
 *         Para evitar esta excepción, llamar a cargarDatos() antes de iniciar().
 */
public void iniciar() {
    if (estaIniciado) {
        throw new IllegalStateException("Procesador ya fue iniciado");
    }
    if (datos == null) {
        throw new IllegalStateException("Datos no cargados");
    }
    // inicialización
}
```

### Patrón: Validación + Consejo

El formato recomendado es: **condición de error** + **cómo prevenirlo**

```java
// ✅ Patrón completo
@throws IllegalArgumentException si monto <= 0.
        Para evitar esta excepción, validar que monto > 0 antes de llamar.

// ✅ Con ejemplo de código
@throws NullPointerException si lista es null.
        Para evitar esta excepción, inicializar la lista:
        List<String> lista = new ArrayList<>();

// ✅ Con método auxiliar
@throws IllegalStateException si la conexión está cerrada.
        Para evitar esta excepción, verificar con estaConectado()
        antes de realizar operaciones.
```

### Diferencia con excepciones checked

```java
// Excepción CHECKED - Describe el error, no cómo prevenirlo
/**
 * Lee un archivo.
 * @throws IOException si el archivo no puede leerse
 */
public String leer(String ruta) throws IOException {
    // El usuario DEBE manejar esto con try-catch
}

// Excepción UNCHECKED - Describe el error Y cómo prevenirlo
/**
 * Obtiene el primer elemento.
 * @throws IllegalStateException si la lista está vacía.
 *         Para evitar esta excepción, verificar con esVacia()
 *         antes de llamar a obtenerPrimero().
 */
public Elemento obtenerPrimero() {
    // El usuario PUEDE prevenir esto validando antes
}
```

### Casos complejos

#### Múltiples formas de prevenir

```java
/**
 * Envía un email al usuario.
 * 
 * @param usuario usuario destinatario
 * @throws IllegalArgumentException si el email del usuario es null o inválido.
 *         Para evitar esta excepción:
 *         1. Validar que usuario.getEmail() != null, O
 *         2. Usar el método enviarSiTieneEmail() que maneja este caso
 */
public void enviarEmail(Usuario usuario) {
    String email = usuario.getEmail();
    if (email == null || !email.contains("@")) {
        throw new IllegalArgumentException("Email inválido: " + email);
    }
    // enviar
}

/**
 * Versión segura que no lanza excepción si no hay email.
 * 
 * @param usuario usuario destinatario
 * @return true si se envió el email, false si el usuario no tiene email válido
 */
public boolean enviarSiTieneEmail(Usuario usuario) {
    try {
        enviarEmail(usuario);
        return true;
    } catch (IllegalArgumentException e) {
        return false;
    }
}
```

### Excepciones de bibliotecas

Si relanzás excepciones de bibliotecas sin envolver:

```java
/**
 * Parsea JSON desde String.
 * 
 * @param json String con contenido JSON
 * @return objeto deserializado
 * @throws JsonProcessingException si el JSON es inválido (de Jackson library).
 *         Para evitar esta excepción, validar el formato JSON antes de parsear,
 *         por ejemplo usando validarJson(String) de esta misma clase.
 */
public Objeto parsearJson(String json) throws JsonProcessingException {
    return objectMapper.readValue(json, Objeto.class);
}
```

### Patrones de prevención comunes

| Excepción | Prevención típica |
|-----------|-------------------|
| `NullPointerException` | Validar `!= null` antes de usar |
| `IllegalArgumentException` | Validar rango/formato de argumentos |
| `IllegalStateException` | Verificar estado con método `isXxx()` |
| `IndexOutOfBoundsException` | Validar `0 <= indice < size()` |
| `UnsupportedOperationException` | Consultar documentación de capacidades |

### Ejemplo completo

```java
/**
 * Repositorio de datos con operaciones CRUD.
 */
public class Repositorio<T> {
    private List<T> elementos = new ArrayList<>();
    private boolean estaInicializado = false;
    
    /**
     * Obtiene un elemento por su índice.
     * 
     * @param indice posición del elemento (0-based)
     * @return elemento en la posición indicada
     * @throws IllegalStateException si el repositorio no fue inicializado.
     *         Para evitar esta excepción, llamar a inicializar() antes de usar.
     * @throws IndexOutOfBoundsException si indice < 0 o indice >= size().
     *         Para evitar esta excepción, verificar que el índice esté en rango:
     *         0 <= indice < repositorio.size()
     */
    public T obtener(int indice) {
        if (!estaInicializado) {
            throw new IllegalStateException("Repositorio no inicializado");
        }
        if (indice < 0 || indice >= elementos.size()) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        return elementos.get(indice);
    }
    
    /**
     * Agrega un elemento al repositorio.
     * 
     * @param elemento elemento a agregar
     * @throws NullPointerException si elemento es null.
     *         Para evitar esta excepción, validar que elemento != null
     *         antes de agregarlo.
     * @throws IllegalStateException si el repositorio está en modo solo lectura.
     *         Para evitar esta excepción, verificar con esSoloLectura()
     *         antes de intentar agregar.
     */
    public void agregar(T elemento) {
        if (elemento == null) {
            throw new NullPointerException("Elemento no puede ser null");
        }
        elementos.add(elemento);
    }
}
```

:::{tip}
La documentación preventiva transforma excepciones runtime en **precondiciones documentadas**, haciendo el código más robusto y autoexplicativo.
:::

(regla-0x1004)=
## `0x1004` - Documenten el lanzamiento indirecto de excepciones propias

### Explicación

Cuando un método llama internamente a otros métodos que lanzan excepciones, y esas excepciones se propagan al llamador, deben documentarse indicando el origen. Esto es especialmente importante para métodos de validación internos.

### Justificación

1. **Trazabilidad**: El usuario sabe de dónde viene realmente la excepción.
2. **Debugging**: Facilita entender el flujo de excepciones.
3. **Transparencia**: Documenta dependencias internas que afectan el contrato.
4. **Completitud**: El contrato del método incluye todas las excepciones posibles, directas e indirectas.

### Ejemplos

#### Excepción lanzada por método interno

```java
/**
 * Procesa un pedido completo.
 * <p>
 * Realiza validación interna antes del procesamiento.
 * 
 * @param pedido el pedido a procesar
 * @throws PedidoInvalidoException si el pedido no pasa validación 
 *         (lanzada por validarPedido())
 * @throws SaldoInsuficienteException si no hay fondos para procesar el pago
 *         (lanzada por procesarPago())
 */
public void procesar(Pedido pedido) 
        throws PedidoInvalidoException, SaldoInsuficienteException {
    validarPedido(pedido);  // Puede lanzar PedidoInvalidoException
    procesarPago(pedido);   // Puede lanzar SaldoInsuficienteException
    enviarNotificacion(pedido);
}

/**
 * Valida que un pedido tenga datos completos.
 * 
 * @param pedido pedido a validar
 * @throws PedidoInvalidoException si falta información obligatoria
 */
private void validarPedido(Pedido pedido) throws PedidoInvalidoException {
    // validación
}
```

#### Múltiples métodos internos

```java
/**
 * Registra un nuevo usuario en el sistema.
 * <p>
 * Realiza validaciones múltiples y persiste en base de datos.
 * 
 * @param usuario usuario a registrar
 * @throws UsuarioInvalidoException si los datos son inválidos
 *         (lanzada por validarDatosUsuario())
 * @throws EmailDuplicadoException si el email ya está registrado
 *         (lanzada por verificarEmailUnico())
 * @throws ErrorBaseDatosException si falla la persistencia
 *         (lanzada por guardarEnBaseDatos())
 */
public void registrar(Usuario usuario) 
        throws UsuarioInvalidoException, 
               EmailDuplicadoException, 
               ErrorBaseDatosException {
    validarDatosUsuario(usuario);
    verificarEmailUnico(usuario.getEmail());
    guardarEnBaseDatos(usuario);
}
```

:::{tip}
Cuando documentes excepciones indirectas, incluí el nombre del método que las lanza. Esto facilita el debugging y la comprensión del flujo de control.
:::

(regla-0x1005)=
## `0x1005` - Al documentar, no se indica el tipo de los parámetros o retorno

### Explicación

En la documentación Javadoc, no se debe incluir el tipo de dato en las descripciones de `@param` y `@return`. El tipo ya está presente en la firma del método, por lo que incluirlo en la documentación es redundante.

### Justificación

1. **Evitar redundancia**: El tipo ya está explícito en la firma.
2. **Enfoque en semántica**: La documentación debe explicar el **propósito**, no repetir información sintáctica.
3. **Mantenibilidad**: Si cambia el tipo, no hay que actualizar la documentación.
4. **Legibilidad**: Documentación más concisa y enfocada.
5. **Estándar Javadoc**: Las herramientas de generación ya incluyen el tipo automáticamente.

### Ejemplos

#### Incorrecto ❌

```java
/**
 * Busca una persona por nombre.
 * 
 * @param nombre String - el nombre de la persona a buscar
 * @param edad int - la edad de la persona
 * @return Persona - la persona encontrada
 */
public Persona buscar(String nombre, int edad) {
    // ...
}

/**
 * Calcula el área de un rectángulo.
 * 
 * @param ancho double: ancho del rectángulo en metros
 * @param alto double: alto del rectángulo en metros
 * @return double: área calculada en metros cuadrados
 */
public double calcularArea(double ancho, double alto) {
    // ...
}
```

#### Correcto ✅

```java
/**
 * Busca una persona por nombre y edad.
 * 
 * @param nombre el nombre de la persona a buscar
 * @param edad la edad de la persona
 * @return la persona encontrada, o null si no existe
 */
public Persona buscar(String nombre, int edad) {
    // ...
}

/**
 * Calcula el área de un rectángulo.
 * 
 * @param ancho ancho del rectángulo en metros
 * @param alto alto del rectángulo en metros
 * @return área calculada en metros cuadrados
 */
public double calcularArea(double ancho, double alto) {
    // ...
}
```

### Enfoque en el propósito

La documentación debe responder:
- **¿Para qué sirve** este parámetro?
- **¿Qué representa** este valor de retorno?
- **¿Qué restricciones o características** tiene?

```java
// ❌ Repite información del tipo
/**
 * @param id Integer que identifica al usuario
 * @return un String con el nombre
 */

// ✅ Explica el propósito y restricciones
/**
 * @param id identificador único del usuario (debe ser > 0)
 * @return el nombre completo del usuario, o null si no se encuentra
 */
```

### Unidades y formato

En lugar del tipo, especificá **unidades** o **formato** cuando sea relevante:

```java
/**
 * Calcula el tiempo de viaje entre dos puntos.
 * 
 * @param distancia distancia entre puntos en kilómetros
 * @param velocidad velocidad promedio en km/h
 * @return tiempo de viaje en horas
 */
public double calcularTiempo(double distancia, double velocidad) {
    return distancia / velocidad;
}

/**
 * Parsea una fecha desde texto.
 * 
 * @param texto fecha en formato "dd/MM/yyyy"
 * @return objeto Date con la fecha parseada
 * @throws ParseException si el formato es incorrecto
 */
public Date parsearFecha(String texto) throws ParseException {
    // ...
}
```

### Restricciones y validaciones

En lugar del tipo, documentá **restricciones**:

```java
/**
 * Establece la edad de la persona.
 * 
 * @param edad la edad a establecer (debe estar entre 0 y 150)
 * @throws IllegalArgumentException si edad < 0 o edad > 150
 */
public void setEdad(int edad) {
    // No escribir: @param edad int - la edad
    // En su lugar, documentar el rango válido
}

/**
 * Busca usuarios por email.
 * 
 * @param email dirección de email (debe contener '@' y '.')
 * @return lista de usuarios con ese email (nunca null, puede estar vacía)
 */
public List<Usuario> buscarPorEmail(String email) {
    // No escribir: @param email String - el email
    // En su lugar, documentar el formato esperado
}
```

### Contexto de uso

```java
/**
 * Aplica un descuento al precio.
 * 
 * @param descuento porcentaje de descuento a aplicar, expresado como decimal
 *                  (ej: 0.15 para 15%, debe estar entre 0.0 y 1.0)
 * @return precio después de aplicar el descuento
 */
public double aplicarDescuento(double descuento) {
    // La documentación aclara cómo interpretar el valor
    // mucho más útil que decir "@param descuento double - el descuento"
}
```

### Colecciones: Contenido vs. Tipo

```java
// ❌ Documenta el tipo de contenedor
/**
 * @param lista ArrayList de strings con nombres
 * @return HashMap de edades
 */

// ✅ Documenta el contenido y propósito
/**
 * @param nombres lista de nombres de estudiantes a buscar (no null, puede estar vacía)
 * @return mapa de nombre a edad para los estudiantes encontrados (nunca null)
 */
public Map<String, Integer> buscarEdades(List<String> nombres) {
    // ...
}
```

### Identificadores bien elegidos

Si necesitás repetir el tipo en la documentación, probablemente el identificador es muy genérico:

```java
// ❌ Identificador genérico requiere aclarar tipo
/**
 * @param datos String - los datos en formato JSON
 */
public void procesar(String datos) { }

// ✅ Identificador específico, documentación enfocada
/**
 * @param json datos del usuario en formato JSON válido
 */
public void procesar(String json) { }

// ✅ Aún mejor
/**
 * @param jsonUsuario datos del usuario en formato JSON válido
 */
public void procesarUsuario(String jsonUsuario) { }
```

:::{important}
La necesidad de incluir el tipo en la documentación es señal de que el **nombre del parámetro** no es suficientemente descriptivo. Mejorá el nombre en lugar de agregar el tipo a la documentación.
:::

### Ejemplos del mundo real

```java
public class GestorTransacciones {
    /**
     * Registra una nueva transacción en el sistema.
     * <p>
     * La transacción se valida, se registra en la base de datos
     * y se envía notificación al usuario.
     * 
     * @param monto cantidad de dinero a transferir (debe ser > 0)
     * @param cuentaOrigen cuenta desde donde se debita (debe tener saldo suficiente)
     * @param cuentaDestino cuenta donde se acredita (debe estar activa)
     * @return identificador único de la transacción generada
     * @throws SaldoInsuficienteException si cuentaOrigen no tiene fondos
     * @throws CuentaInactivaException si cuentaDestino está bloqueada
     */
    public String registrarTransaccion(double monto, 
                                        Cuenta cuentaOrigen, 
                                        Cuenta cuentaDestino)
            throws SaldoInsuficienteException, CuentaInactivaException {
        // Nótese: no dice "@param monto double - el monto"
        // Dice qué es el monto y qué restricción tiene
    }
}
```

(regla-0x1006)=
## `0x1006` - Las precondiciones deben documentarse con `@param` o comentarios

### Explicación

Las precondiciones (condiciones que deben cumplirse **antes** de llamar al método) deben documentarse explícitamente. Esto se hace en la descripción de `@param` o con comentarios `PRE:` en el Javadoc.

### Justificación

1. **Contrato claro**: Define responsabilidades del llamador.
2. **Prevención de errores**: Evita uso incorrecto del método.
3. **Documentación de validaciones**: Explicita qué se valida y qué se asume.
4. **Diseño por contrato**: Formaliza precondiciones, postcondiciones e invariantes.

### Formas de documentar precondiciones

#### Opción 1: En la descripción del @param

```java
/**
 * Calcula el promedio de un arreglo.
 * 
 * @param numeros el arreglo de números a promediar (no puede ser null ni vacío)
 * @return el promedio de los valores
 * @throws IllegalArgumentException si el arreglo está vacío
 * @throws NullPointerException si el arreglo es null
 */
public double calcularPromedio(int[] numeros) {
    // implementación
}
```

#### Opción 2: Con notación PRE: explícita

```java
/**
 * Retira dinero de una cuenta.
 * <p>
 * PRE: monto > 0
 * PRE: monto <= saldo
 * PRE: cuenta.estaActiva()
 * POST: saldo = saldoAnterior - monto
 * 
 * @param monto cantidad a retirar
 * @throws MontoInvalidoException si monto <= 0
 * @throws SaldoInsuficienteException si monto > saldo
 */
public void retirar(double monto) 
        throws MontoInvalidoException, SaldoInsuficienteException {
    // implementación
}
```

#### Opción 3: Combinación (recomendada)

```java
/**
 * Divide dos números con precisión decimal.
 * <p>
 * PRE: divisor != 0
 * POST: |resultado - (dividendo / divisor)| < 0.0001
 * 
 * @param dividendo número a dividir
 * @param divisor número por el cual dividir (debe ser diferente de cero)
 * @return resultado de la división
 * @throws ArithmeticException si divisor == 0
 */
public double dividir(double dividendo, double divisor) {
    // implementación
}
```

### Tipos de precondiciones comunes

#### Validación de null

```java
/**
 * Registra un evento en el log.
 * 
 * @param evento evento a registrar (no puede ser null)
 * @param timestamp momento del evento (no puede ser null)
 * @throws NullPointerException si evento o timestamp son null
 */
public void registrar(Evento evento, LocalDateTime timestamp) {
    // ...
}
```

#### Rangos numéricos

```java
/**
 * Obtiene un elemento de la lista.
 * 
 * @param indice posición del elemento (debe estar entre 0 y size()-1)
 * @return elemento en la posición indicada
 * @throws IndexOutOfBoundsException si indice < 0 o indice >= size()
 */
public T obtener(int indice) {
    // ...
}

/**
 * Establece el porcentaje de descuento.
 * 
 * @param porcentaje porcentaje a aplicar (debe estar entre 0.0 y 100.0)
 * @throws IllegalArgumentException si porcentaje < 0 o porcentaje > 100
 */
public void setDescuento(double porcentaje) {
    // ...
}
```

#### Formato y patrones

```java
/**
 * Valida un número de teléfono argentino.
 * 
 * @param telefono número en formato "+54-9-XXX-XXX-XXXX" o similar
 * @return true si el formato es válido
 */
public boolean validarTelefono(String telefono) {
    // ...
}

/**
 * Parsea una fecha.
 * 
 * @param fecha fecha en formato ISO-8601 (yyyy-MM-dd)
 * @return objeto Date con la fecha parseada
 * @throws ParseException si el formato no coincide
 */
public Date parsear(String fecha) throws ParseException {
    // ...
}
```

#### Estado del objeto

```java
/**
 * Inicia el servidor HTTP.
 * <p>
 * PRE: !estaIniciado() - el servidor no debe estar ejecutándose
 * PRE: puerto > 0 && puerto < 65536
 * POST: estaIniciado() == true
 * 
 * @param puerto puerto en el cual escuchar (1-65535)
 * @throws IllegalStateException si el servidor ya está iniciado
 * @throws IllegalArgumentException si puerto está fuera de rango
 */
public void iniciar(int puerto) {
    // ...
}
```

### Precondiciones múltiples

```java
/**
 * Transfiere dinero entre dos cuentas.
 * <p>
 * Precondiciones:
 * - origen y destino no pueden ser null
 * - monto debe ser > 0
 * - origen debe tener saldo >= monto
 * - ambas cuentas deben estar activas
 * <p>
 * POST: origen.getSaldo() = saldoOriginal - monto
 * POST: destino.getSaldo() = saldoOriginal + monto
 * 
 * @param origen cuenta desde donde se debita (no null, activa, con saldo suficiente)
 * @param destino cuenta donde se acredita (no null, activa)
 * @param monto cantidad a transferir (debe ser > 0)
 * @throws NullPointerException si origen o destino son null
 * @throws IllegalArgumentException si monto <= 0
 * @throws SaldoInsuficienteException si origen no tiene fondos
 * @throws CuentaInactivaException si alguna cuenta está inactiva
 */
public void transferir(Cuenta origen, Cuenta destino, double monto) 
        throws SaldoInsuficienteException, CuentaInactivaException {
    // ...
}
```

:::{note}
Las precondiciones pueden documentarse de forma distribuida (en cada `@param`) o de forma centralizada (con bloque `PRE:`). Elegí el enfoque que sea más claro para el caso específico.
:::

(regla-0x1007)=
## `0x1007` - Los comentarios TODO deben incluir contexto

### Explicación

Los comentarios `TODO` no deben ser simples recordatorios vagos. Deben incluir información contextual: quién debe hacerlo, qué exactamente falta, por qué está pendiente, y opcionalmente una fecha o referencia.

### Justificación

1. **Responsabilidad clara**: Se sabe quién debe completar la tarea.
2. **Contexto preservado**: Se entiende por qué quedó pendiente.
3. **Priorización**: Facilita decidir qué TODOs son más urgentes.
4. **Rastreabilidad**: Permite buscar TODOs por autor o fecha.
5. **Previene abandono**: TODOs sin contexto tienden a quedar para siempre.

### Formato recomendado

```
// TODO (Autor, Fecha): Descripción específica de qué falta
// Contexto opcional: Por qué está pendiente o información adicional
```

### Ejemplos

#### Incorrecto ❌

```java
// TODO: arreglar esto
// TODO: mejorar
// TODO: revisar
// TODO: terminar
// TODO: fix bug
// TODO: implementar
```

Estos TODOs son **inútiles** porque:
- No dicen qué hay que hacer exactamente
- No dicen quién debe hacerlo
- No explican por qué quedó pendiente
- Probablemente nunca se completen

#### Correcto ✅

```java
// TODO (Martin, 2025-02-18): Implementar validación de RUT chileno
// Pendiente porque necesitamos confirmar el algoritmo con el cliente

// TODO (Laura): Optimizar consulta SQL, actualmente toma 3seg en producción
// Ver issue #234 en JIRA

// TODO (Equipo, 2025-03): Migrar a nueva API de pagos v2
// La API v1 se depreca en junio 2025, tenemos 3 meses

// TODO (Pendiente): Agregar soporte para timezone diferente a UTC
// Requiere refactorizar cómo se almacenan las fechas en BD

// TODO (Carlos): Extraer esta lógica a clase separada cuando tengamos más casos
// Por ahora solo hay 2 usos, pero se espera que crezca
```

### Elementos de un buen TODO

| Elemento | Ejemplo | Obligatorio |
|----------|---------|-------------|
| **Autor** | `(Martin)` | ✅ Sí |
| **Fecha** | `2025-02-18` | Recomendado |
| **Descripción específica** | "Implementar caché de consultas" | ✅ Sí |
| **Contexto/Razón** | "Pendiente por aprobación del líder" | ✅ Sí |
| **Referencia** | "Ver issue #234" | Opcional |

### Diferentes niveles de urgencia

```java
// TODO URGENTE (Martin, 2025-03-05): Fix memory leak en procesamiento de imágenes
// Causando OutOfMemoryError en producción. Prioridad ALTA.

// TODO (Laura): Refactorizar este método cuando tengamos tiempo
// Funciona pero es difícil de mantener. Prioridad BAJA.

// FIXME (Carlos, 2025-02-28): El cálculo da resultados incorrectos para valores negativos
// Workaround temporal: validar que valores sean positivos antes de llamar.

// HACK (Equipo): Esta lógica es un parche temporal
// Reemplazar con solución apropiada en próxima versión.
```

### Tipos de comentarios de acción

| Tag | Significado | Uso |
|-----|-------------|-----|
| `TODO` | Funcionalidad pendiente | Nueva característica o mejora |
| `FIXME` | Bug conocido | Algo que está roto y debe arreglarse |
| `HACK` | Solución temporal | Código que funciona pero debe reescribirse |
| `XXX` | Atención requerida | Código problemático o peligroso |
| `NOTE` | Información importante | Aclaración sobre decisión de diseño |

### Ejemplos por tipo

```java
public class ProcesadorPagos {
    
    // TODO (Martin, 2025-03-10): Agregar soporte para pagos en cuotas
    // Cliente lo solicitó para próxima release. Ver especificación en docs/pagos-cuotas.md
    
    // FIXME (Laura, 2025-03-04): Transacciones duplicadas en alta concurrencia
    // Reproducible con 100+ usuarios simultáneos. Agregar lock o transacción optimista.
    
    // HACK (Carlos): Retry infinito para reconexión
    // Solución temporal hasta implementar política de reintentos configurable.
    private void reconectar() {
        while (true) {
            try {
                conectar();
                break;
            } catch (IOException e) {
                // reintentar
            }
        }
    }
    
    // XXX (Equipo): Este método modifica estado global
    // Puede causar race conditions. Considerar refactorizar.
    
    // NOTE: Usamos BigDecimal en lugar de double para evitar errores de redondeo
    // Ver análisis en docs/precision-monetaria.md
}
```

### Búsqueda y tracking de TODOs

Los IDEs permiten buscar y filtrar TODOs:

```java
// IntelliJ IDEA: View > Tool Windows > TODO
// Eclipse: Window > Show View > Tasks
// VS Code: Buscar "TODO" en todos los archivos
```

:::{tip}
**Integración con sistemas de tracking:**

Para proyectos grandes, vinculá TODOs con issues:

```java
// TODO (Issue #234): Implementar paginación de resultados
// https://github.com/proyecto/repo/issues/234
```

Esto permite tracking formal en el sistema de issues.
:::

### Anti-patrones

```java
// ❌ TODO vago sin información
// TODO: revisar esto

// ❌ TODO sin autor
// TODO: implementar validación

// ❌ TODO sin razón
// TODO (Martin): agregar método

// ✅ TODO completo
// TODO (Martin, 2025-03-05): Implementar validación de CUIT/CUIL argentino
// Pendiente porque requiere tabla de verificación que debe obtenerse de AFIP
// Estimado: 2 horas de desarrollo + testing
```

### Ciclo de vida de un TODO

```java
// Día 1: Se identifica necesidad
// TODO (Martin, 2025-03-01): Agregar logging de errores
// Necesario para debugging en producción

// Día 10: Se actualiza con más info
// TODO (Martin, 2025-03-01): Agregar logging de errores
// UPDATE (2025-03-10): Cliente confirmó que necesita nivel DEBUG
// Usar SLF4J con configuración por ambiente

// Día 15: Se completa y se elimina el comentario
// (comentario eliminado, funcionalidad implementada)
```

:::{important}
Los TODOs no deben ser permanentes. Deben completarse o convertirse en issues formales en el sistema de tracking. Un TODO de hace 6 meses es señal de mala gestión de proyecto.
:::

### TODOs en código de estudiantes

Para entregas de la cátedra:

```java
// TODO (Estudiante: Juan Pérez): Implementar ordenamiento por edad
// Pendiente para la próxima entrega (TP5)
// Requiere implementar Comparable en clase Persona

// NOTA: Este TODO será evaluado en la corrección
// Si está presente sin justificación, puede afectar la nota
```

:::{warning}
En entregas finales, **no deben quedar TODOs** sin resolver a menos que estén explícitamente permitidos o sean parte del alcance de una entrega futura.
:::
