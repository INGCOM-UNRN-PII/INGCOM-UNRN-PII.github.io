---
title: 0x0 - Nomenclatura y Formato
---

# Serie 0x0 - Nomenclatura y Formato

Muchas de las reglas de nomenclatura que están aquí descritas, salen del estándar de
código oficial de Java, la idea de seguirlas es hacer que nuestro código no solo sea,
indistinguible del código de la plataforma. Sino que también nos ayude al leerlo,
estableciendo expectativas claras.

(regla-generales-0x0000)=
## `0x0000` - Sin errores de ortografía y apliquen formato markdown donde sea posible

### Explicación

La ortografía correcta en el código no es un lujo, es una necesidad profesional.
Los identificadores, comentarios y documentación forman parte de la comunicación
técnica del proyecto. Un error ortográfico no solo es poco profesional, sino que
puede generar confusión y dificultar el mantenimiento del código.

### Justificación

1. **Profesionalismo**: El código es un producto técnico que refleja la calidad
   del desarrollador y del equipo.
2. **Mantenibilidad**: Errores ortográficos dificultan la búsqueda de símbolos y
   pueden llevar a inconsistencias.
3. **Colaboración**: Otros desarrolladores (o vos mismo en el futuro) deben
   poder leer y entender el código sin ambigüedades.
4. **Herramientas disponibles**: Los IDEs modernos incluyen correctores ortográficos
   integrados.
5. **Estandar de la industria**: Markdown se usa por todos lados para dar formato.

### Herramientas

Consultá el [Apunte Markdown](../guias/markdown.md) para las cuestiones de formato
en documentación.

:::{important} No hay excusas.

Los IDEs modernos tienen corrector ortográfico integrado, por lo que los errores
tipográficos (_typos_) en identificadores y documentación **no serán aceptados**
en las entregas.

:::


### Casos Especiales

- **Acrónimos conocidos**: Siguen siendo válidos (HTTP, URL, JSON), pero deben
  usarse de forma consistente.
- **Términos técnicos en inglés**: Son aceptables cuando son estándar de la
  industria (`callback`, `buffer`, `thread`).
- **Nombres propios**: Si usás nombres de dominio específicos, asegurate de que
  estén bien escritos.

### Verificación en el IDE

La mayoría de los IDEs modernos incluyen corrector ortográfico, en particular 
**IntelliJ IDEA** que pueden configurar en `Settings > Editor > Spelling`
:::{tip}

Configurá tu entorno para que marque errores ortográficos en comentarios
e identificadores. Esto te ayudará a detectar problemas antes de hacer commit.

:::


(regla-generales-0x0001)=
## `0x0001` - Los nombres de las clases van en `CamelloCase`

### Explicación

`CamelloCase` (también conocido como `PascalCase` o `UpperCamelCase`) es la
convención de nomenclatura para clases e interfaces en Java. Consiste en
escribir cada palabra con la primera letra en mayúscula, sin espacios ni guiones
bajos.

### Sintaxis

```
NombreDeClase = PalabraInicial + Palabra2 + Palabra3 + ...
donde cada palabra comienza con Mayúscula
```

### Ejemplos

#### Correcto ✅

```java
public class CalculadoraAvanzada {
    // ...
}

public class GestorDeUsuarios {
    // ...
}

public class GeneradorDeReportes {
    // ...
}

public class ConexionBaseDeDatos {
    // ...
}
```

#### Incorrecto ❌

```java
public class calculadoraAvanzada {  // Primera letra debe ser mayúscula
    // ...
}

public class gestor_de_usuarios {  // No usar guiones bajos
    // ...
}

public class GENERADOR_DE_REPORTES {  // No todo en mayúsculas
    // ...
}

public class conexionBDD {  // Evitar acrónimos no estándar
    // ...
}
```

### Casos Especiales

#### Acrónimos

Cuando un nombre incluye acrónimos, los dos enfoques son aceptables:

**Opción 1 (Recomendada)**: Solo la primera letra en mayúscula

```java
public class ConexionHttp {  // Mejor legibilidad
public class AnalizadorXml {
public class ClienteApi {
```

**Opción 2**: Todas las letras del acrónimo en mayúscula (solo si es corto)

```java
public class ConexionHTTP {  // Aceptable
public class AnalizadorXML {
public class ClienteAPI {
```
:::{important} **Consistencia**

Elegí un enfoque y mantenelo en todo el proyecto. La primera opción es preferible
porque evita ambigüedades como `XMLHTTPRequest` vs `XmlHttpRequest` (un combo de dos
acrónimos)

:::


#### Nombres compuestos

Para nombres largos, considerá si realmente necesitás todas las palabras:

```java
// Aceptable pero largo
public class SistemaDeGestionDeInventarioDeAlmacen {
    // ...
}

// Mejor: más conciso sin perder claridad
public class GestorInventarioAlmacen {
    // ...
}
```


:::{note} Origen del nombre

El nombre "CamelloCase" viene de que las mayúsculas parecen jorobas de
camello: **C**alculadora**A**vanzada. Mientras que `dromedarioCase` tiene solo
una "joroba" al principio: calcular**P**romedio. 

:::

(regla-generales-0x0002)=
## `0x0002` - Los identificadores válidos son solo con alfabéticos `[a-zA-Z]`

### Explicación

Los identificadores en Java (nombres de clases, métodos, variables, etc.) deben
estar compuestos únicamente por letras del alfabeto inglés, sin números, guiones
bajos, acentos, eñes ni caracteres especiales.

### Justificación

1. **Portabilidad**: Código que usa caracteres ASCII básicos funciona en
   cualquier sistema operativo y configuración regional.
2. **Compatibilidad con herramientas**: Muchas herramientas de análisis,
   generadores de código y sistemas de build esperan identificadores ASCII.
3. **Convención internacional**: El desarrollo de software se realiza en inglés
   o con nomenclatura latinizada.
4. **Evitar problemas de encoding**: Los caracteres especiales pueden causar
   problemas con diferentes codificaciones de archivos.
5. **Legibilidad universal**: Cualquier desarrollador, independientemente de su
   idioma, puede leer el código.

### Restricciones

```
Identificador válido: [a-zA-Z]+
- Solo letras mayúsculas y minúsculas del alfabeto inglés
- Sin números (ni siquiera al final)
- Sin guiones bajos (_)
- Sin caracteres acentuados (á, é, í, ó, ú)
- Sin ñ ni otros caracteres especiales
```

### Ejemplos

#### Incorrecto ❌

```java
// Números en identificadores
public class Cliente2 {  // ❌ Contiene número
    private String nombre1;  // ❌ Contiene número
    private String apellido2;  // ❌ Contiene número
}

// Guiones bajos
public class Gestor_Usuarios {  // ❌ Contiene guión bajo
    private int cantidad_items;  // ❌ Contiene guión bajo
}

// Caracteres especiales del español
public class Año {  // ❌ Contiene ñ
    private String descripción;  // ❌ Contiene acento
    private boolean esEspañol;  // ❌ Contiene ñ
}

// Símbolos especiales
public class Calculadora$ {  // ❌ Contiene $
    private double precio€;  // ❌ Contiene símbolo de moneda
}
```

#### Correcto ✅

```java
public class ClientePremium {  // ✅ Solo alfabéticos
    private String nombrePrimario;  // ✅ Sin números
    private String apellidoSecundario;  // ✅ Descriptivo sin números
}

public class GestorUsuarios {  // ✅ Sin guiones bajos
    private int cantidadItems;  // ✅ CamelCase correcto
}

public class Anio {  // ✅ Sin ñ
    private String descripcion;  // ✅ Sin acentos
    private boolean esEspaniol;  // ✅ Sin ñ (o mejor: isSpanish)
}

public class Calculadora {  // ✅ Sin símbolos especiales
    private double precioEnEuros;  // ✅ Nombre descriptivo
}
```

### Casos Especiales

#### ¿Qué hacer con palabras que tienen ñ o acentos?

Tenés tres opciones:

1. **Latinizar** (Recomendado para español)

   ```java
   año → anio
   niño → ninio
   español → espaniol
   ```

2. **Traducir al inglés** (Recomendado para proyectos internacionales)

   ```java
   año → year
   niño → child
   español → spanish
   ```

3. **Abreviar o parafrasear**
   ```java
   descripción → descripcion o desc
   configuración → configuracion o config
   ```
:::{tip}

Para proyectos que se distribuirán internacionalmente, preferí nombres
en inglés. Para proyectos locales o educativos, la latinización es aceptable.

:::


#### Constantes y enumeraciones

Aunque las constantes usan `SNAKE_CASE` (regla {ref}`regla-0x0005`), siguen
aplicando las mismas restricciones:

```java
// Incorrecto
private static final int AÑO_ACTUAL = 2024;  // ❌ Contiene ñ
private static final String CONFIGURACIÓN_INICIAL = "...";  // ❌ Contiene acento

// Correcto
private static final int ANIO_ACTUAL = 2024;  // ✅
private static final String CONFIGURACION_INICIAL = "...";  // ✅
// O mejor aún:
private static final int CURRENT_YEAR = 2024;  // ✅
private static final String INITIAL_CONFIG = "...";  // ✅
```

### ¿Por qué Java permite pero nosotros prohibimos?

Técnicamente, desde Java 1.1, los identificadores pueden contener cualquier
carácter Unicode que sea una "letra" o "dígito" según el estándar. Sin embargo:

```java
// Técnicamente válido en Java, pero NO permitido en esta cátedra
public class Año2024 {  // Compila, pero ❌ para nosotros
    private int número;  // Compila, pero ❌ para nosotros
}
```

La diferencia visual entre algo con tilde o no, es mínima, esto puede traer
dolores de cabeza a la hora de debuggear.
:::{warning}

Que el compilador lo acepte no significa que sea una buena
práctica. Esta regla existe para garantizar código portable, mantenible y
profesional. 

:::


### Ventajas de esta restricción

| Aspecto          | Beneficio                                                      |
| ---------------- | -------------------------------------------------------------- |
| **Portabilidad** | Funciona en cualquier sistema sin problemas de encoding        |
| **Colaboración** | Cualquier desarrollador puede leer el código                   |
| **Herramientas** | Compatible con todas las herramientas de análisis y generación |
| **Búsqueda**     | Facilita buscar en el código sin preocuparse por acentos       |
| **Consistencia** | Fuerza un estándar uniforme en todo el proyecto                |

(regla-generales-0x0003)=
## `0x0003` - Variables, parámetros y variables locales van en `dromedarioCase`

### Explicación

`dromedarioCase` (también conocido como `camelCase` o `lowerCamelCase`) es la
convención de nomenclatura para variables de instancia, variables locales,
parámetros de métodos y atributos en Java. Consiste en comenzar con minúscula y
capitalizar la primera letra de cada palabra subsiguiente, sin espacios ni
separadores.

### Justificación

1. **Estándar de Java**: Convención universal en el ecosistema Java.
2. **Distinción visual**: Permite diferenciar variables de clases (que usan
   `CamelloCase`).
3. **Legibilidad**: Las mayúsculas internas actúan como separadores naturales.
4. **Consistencia**: Todas las APIs de Java siguen esta convención
   (`ArrayList.size()`, `String.length()`).

### Sintaxis

```
nombreDeVariable = palabraInicial + Palabra2 + Palabra3 + ...
donde la primera palabra va en minúscula
y cada palabra subsiguiente comienza con Mayúscula
```

### Ejemplos

#### Variables de instancia (atributos)

```java
public class CuentaBancaria {
    // Correcto ✅
    private String numeroDeCuenta;
    private double saldoActual;
    private String nombreTitular;
    private boolean estaActiva;

    // Incorrecto ❌
    private String NumeroDeCuenta;  // Primera letra debe ser minúscula
    private double Saldo_Actual;    // No usar guiones bajos
    private String nombre_titular;  // No usar snake_case
    private boolean ESTA_ACTIVA;    // No todo en mayúsculas
}
```

#### Variables locales

```java
public void procesarTransaccion() {
    // Correcto ✅
    double montoTotal = 0.0;
    int numeroDeItems = 10;
    String codigoTransaccion = generarCodigo();
    boolean esValida = validar(montoTotal);

    // Incorrecto ❌
    double MontoTotal = 0.0;        // Primera letra mayúscula
    int numero_de_items = 10;       // Guiones bajos
    String COD_TRANSACCION = "";    // Todo mayúsculas
    boolean Valida = false;         // Primera letra mayúscula
}
```

#### Parámetros de métodos

```java
public class Calculadora {
    // Correcto ✅
    public double calcularInteres(double capitalInicial,
                                   double tasaAnual,
                                   int periodoEnMeses) {
        // ...
    }

    public void registrarUsuario(String nombreCompleto,
                                  String correoElectronico,
                                  int edadEnAnios) {
        // ...
    }

    // Incorrecto ❌
    public double calcularInteres(double CapitalInicial,  // Mayúscula inicial
                                   double tasa_anual,      // Guión bajo
                                   int PERIODO) {          // Todo mayúsculas
        // ...
    }
}
```

### Casos Especiales

#### Variables de una sola letra

En contextos matemáticos o bucles simples, variables de una letra son
aceptables:

```java
// Aceptable en contextos apropiados
for (int i = 0; i < n; i++) {  // i, n: índices y límites
    for (int j = 0; j < m; j++) {  // j, m: índices anidados
        matriz[i][j] = 0;
    }
}

// Coordenadas
double x = punto.getX();
double y = punto.getY();
double z = punto.getZ();

// Matemática
double a = 2.0;
double b = 3.5;
double c = Math.sqrt(a * a + b * b);
```
:::{warning}

Las variables de una letra solo son apropiadas en contextos
matemáticos estándar o bucles simples. En cualquier otro caso, usá nombres
descriptivos. 

:::


#### Acrónimos en variables

Cuando una variable contiene un acrónimo, hay dos enfoques:

**Opción 1 (Recomendada)**: Tratar el acrónimo como una palabra normal

```java
// Recomendado
private String urlServidor;
private int idUsuario;
private String codigoHtml;
private Object valorJson;
```

**Opción 2**: Mantener el acrónimo en minúsculas si es corto

```java
// Aceptable
private String url;
private int id;
private String html;
private Object json;
```
:::{important}

Si el acrónimo está al inicio, debe ir completamente en
minúsculas: `htmlParser`, `xmlDocument`, `httpClient`. 

:::


#### Variables booleanas

Las variables booleanas tienen convenciones especiales (ver regla
{ref}`regla-0x0007`):

```java
// Correcto: prefijos interrogativos
private boolean esActivo;
private boolean tienePermisos;
private boolean puedeEditar;
private boolean estaListo;

// Incorrecto: sin prefijo interrogativo
private boolean activo;     // ❌
private boolean permisos;   // ❌
private boolean editable;   // ❌
private boolean listo;      // ❌
```

### Comparación con otras convenciones

| Tipo               | Convención       | Ejemplo              |
| ------------------ | ---------------- | -------------------- |
| Variable/Parámetro | `dromedarioCase` | `numeroDeCuenta`     |
| Clase/Interfaz     | `CamelloCase`    | `CuentaBancaria`     |
| Constante          | `SNAKE_CASE`     | `TASA_INTERES_ANUAL` |
| Paquete            | `lowercase`      | `com.empresa.banco`  |

### Nombres descriptivos

Recordá que además de seguir la convención de nomenclatura, los nombres deben
ser descriptivos (ver regla {ref}`regla-0x0006`):

```java
// Mal: sintaxis correcta pero no descriptivo
private int num;      // ❌ Muy genérico
private String str;   // ❌ No dice qué contiene
private double val;   // ❌ Ambiguo

// Bien: sintaxis y semántica correctas
private int numeroDeClientes;    // ✅ Claro y específico
private String nombreCompleto;   // ✅ Describe el contenido
private double saldoDisponible;  // ✅ Contexto claro
```
:::{tip}

Un buen nombre de variable debe responder la pregunta: "¿Qué
información almacena esto?" sin necesidad de leer el resto del código. 

:::


(regla-generales-0x0004)=
## `0x0004` - Los nombres de los métodos van en `dromedarioCase`

### Explicación

Los métodos en Java siguen la misma convención de nomenclatura que las
variables: `dromedarioCase`. El nombre debe comenzar con minúscula y cada
palabra subsiguiente con mayúscula, formando una frase verbal que describe la
acción que realiza el método.

### Justificación

1. **Estándar Java**: Todas las APIs estándar siguen esta convención
   (`toString()`, `equals()`, `hashCode()`, `compareTo()`).
2. **Distinción visual**: Permite diferenciar métodos de clases a simple vista.
3. **Legibilidad**: Los nombres verbales facilitan entender qué hace el método.
4. **Consistencia**: Mantiene coherencia con las variables, ya que ambos
   representan acciones o valores específicos.
:::{note} **Terminología**

En Java, técnicamente no existen "funciones" sino **métodos** (funciones asociadas
a una clase). Usamos ambos términos en la cátedra para facilitar la transición
desde C, donde sí existen funciones independientes.

:::


### Sintaxis

```
nombreDeMetodo = verbo + Complemento + Complemento2 + ...
donde el verbo inicial va en minúscula
y cada complemento comienza con Mayúscula
```

### Ejemplos

#### Métodos básicos

```java
public class Calculadora {
    // Correcto ✅
    public double calcularPromedio(double[] valores) { ... }
    public String obtenerNombre() { ... }
    public void validarDatos() { ... }
    public boolean esValido() { ... }
    public void establecerValor(int valor) { ... }

    // Incorrecto ❌
    public double CalcularPromedio() { ... }     // Mayúscula inicial
    public String ObtenerNombre() { ... }        // Mayúscula inicial
    public void Validar_Datos() { ... }          // Mayúscula + guión bajo
    public boolean EsValido() { ... }            // Mayúscula inicial
    public void ESTABLECER_VALOR(int valor) { ... }  // Todo mayúsculas
}
```

#### Patrones comunes de nomenclatura

**Getters y Setters** (JavaBean convention):

```java
public class Persona {
    private String nombre;
    private int edad;

    // Correcto ✅
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}
```

**Métodos booleanos** (ver también regla {ref}`regla-0x0007`):

```java
public class Usuario {
    // Correcto ✅ - usar prefijos interrogativos
    public boolean esAdministrador() { ... }
    public boolean tienePermisos() { ... }
    public boolean puedeEditar() { ... }
    public boolean estaActivo() { ... }

    // Incorrecto ❌ - sin prefijos interrogativos
    public boolean administrador() { ... }
    public boolean permisos() { ... }
    public boolean editable() { ... }
    public boolean activo() { ... }
}
```

**Métodos de conversión y transformación**:

```java
// Correcto ✅
public String convertirATexto() { ... }
public int obtenerComoEntero() { ... }
public List<String> transformarALista() { ... }

// Incorrecto ❌
public String ConvertirTexto() { ... }      // Mayúscula inicial
public int obtener_como_entero() { ... }    // Guiones bajos
public List<String> TransformarLista() { ... }  // Mayúscula inicial
```

### Casos Especiales

#### Constructores

Los constructores son una excepción: **siempre tienen el mismo nombre que la
clase** (en `CamelloCase`):

```java
public class CuentaBancaria {
    // Constructor: mismo nombre que la clase
    public CuentaBancaria(String numero, double saldoInicial) {
        // ...
    }
}
```

#### Métodos estáticos de utilidad

Los métodos estáticos siguen la misma convención:

```java
public class Utilidades {
    // Correcto ✅
    public static String formatearFecha(Date fecha) { ... }
    public static int calcularMaximo(int a, int b) { ... }
    public static boolean validarEmail(String email) { ... }

    // Incorrecto ❌
    public static String FormatearFecha(Date fecha) { ... }
    public static int Calcular_Maximo(int a, int b) { ... }
}
```

#### Métodos de callback o listeners

En interfaces funcionales o listeners, seguir la misma convención:

```java
public interface EventListener {
    void alRecibirEvento(Event evento);  // ✅ dromedarioCase
    void alCompletarTarea();             // ✅ dromedarioCase
}

// Implementación
button.onClick(evento -> {
    procesarClick(evento);  // ✅ dromedarioCase
});
```

#### Verbos recomendados

Usá verbos claros que indiquen la acción:

| Verbo                    | Uso                           | Ejemplo                                  |
| ------------------------ | ----------------------------- | ---------------------------------------- |
| `obtener` / `get`        | Devolver un valor             | `obtenerNombre()`, `getSaldo()`          |
| `establecer` / `set`     | Asignar un valor              | `establecerNombre()`, `setSaldo()`       |
| `es` / `is`              | Verificar estado (boolean)    | `esVacio()`, `isActive()`                |
| `tiene` / `has`          | Verificar posesión (boolean)  | `tienePermisos()`, `hasChildren()`       |
| `puede` / `can`          | Verificar capacidad (boolean) | `puedeEditar()`, `canExecute()`          |
| `calcular` / `calculate` | Realizar cálculo              | `calcularPromedio()`, `calculateTotal()` |
| `validar` / `validate`   | Verificar validez             | `validarDatos()`, `validateInput()`      |
| `procesar` / `process`   | Transformar o aplicar lógica  | `procesarPago()`, `processRequest()`     |
| `crear` / `create`       | Instanciar o construir        | `crearUsuario()`, `createReport()`       |
| `eliminar` / `delete`    | Borrar o destruir             | `eliminarCuenta()`, `deleteFile()`       |
| `agregar` / `add`        | Añadir elemento               | `agregarItem()`, `addElement()`          |
| `remover` / `remove`     | Quitar elemento               | `removerItem()`, `removeElement()`       |
| `buscar` / `find`        | Localizar elemento            | `buscarPorId()`, `findByName()`          |
:::{important}

El nombre del método debe describir **qué hace**, no **cómo lo
hace**. La implementación interna puede cambiar, pero el propósito del método
debe ser claro desde su nombre. 

:::


### Métodos largos vs. cortos

```java
// Demasiado largo - considera refactorizar
public void validarYProcesarYGuardarTransaccionEnBaseDeDatos() { ... }  // ❌

// Mejor: divide en métodos más específicos
public void procesarTransaccion(Transaccion t) {  // ✅
    validarTransaccion(t);
    guardarEnBaseDeDatos(t);
}

private void validarTransaccion(Transaccion t) { ... }
private void guardarEnBaseDeDatos(Transaccion t) { ... }
```
:::{tip}

Si el nombre del método es muy largo, probablemente esté haciendo
demasiadas cosas. Considerá dividirlo en métodos más pequeños y específicos
(Single Responsibility Principle). 

:::


(regla-generales-0x0005)=
## `0x0005` - Las constantes van en mayúsculas con `SNAKE_CASE`

### Explicación

Las constantes en Java deben nombrarse usando `SNAKE_CASE`: todas las letras en
mayúsculas, con palabras separadas por guiones bajos (`_`). Esta convención se
aplica exclusivamente a campos declarados como `static final`.

### Justificación

1. **Distinción visual inmediata**: Las mayúsculas hacen que las constantes se
   destaquen del resto del código.
2. **Estándar universal**: Prácticamente todos los lenguajes usan esta
   convención para constantes.
3. **Prevención de errores**: La distinción visual ayuda a evitar intentos de
   modificación accidental.
4. **Legibilidad**: El guión bajo es el único contexto donde se permite,
   facilitando la lectura de nombres largos.
5. **Compatibilidad**: Toda la API estándar de Java usa esta convención
   (`Integer.MAX_VALUE`, `Math.PI`).

### Sintaxis

```
NOMBRE_CONSTANTE = PALABRA1 + _ + PALABRA2 + _ + ...
donde todas las letras van en MAYÚSCULAS
y las palabras se separan con guión bajo (_)
```

### Declaración correcta

Las constantes deben declararse con `static final`:

```java
public class ConfiguracionSistema {
    // Correcto ✅
    public static final int MAX_INTENTOS = 3;
    public static final double PI = 3.14159265359;
    public static final String MENSAJE_ERROR = "Error al procesar";
    public static final int TIEMPO_ESPERA_MS = 5000;

    // Incorrecto ❌
    public static final int maxIntentos = 3;        // Debe ser mayúsculas
    public static final double pi = 3.14159;        // Debe ser mayúsculas
    public static final String MensajeError = "";   // Debe usar guiones bajos
    public static final int TIEMPOESPERARMS = 5000; // Falta separador
}
```

### Ejemplos

#### Constantes numéricas

```java
public class ConstantesMatematicas {
    // Correcto ✅
    public static final double PI = 3.14159265359;
    public static final double E = 2.71828182846;
    public static final double PHI = 1.61803398875;

    // Incorrecto ❌
    public static final double pi = 3.14159;    // Minúsculas
    public static final double e = 2.71828;     // Minúsculas
}

public class LimitesAplicacion {
    // Correcto ✅
    public static final int MAX_USUARIOS = 1000;
    public static final int MIN_EDAD = 18;
    public static final int TIMEOUT_SEGUNDOS = 30;

    // Incorrecto ❌
    public static final int maxUsuarios = 1000;      // dromedarioCase
    public static final int MinEdad = 18;            // CamelloCase
    public static final int TIMEOUTSEGUNDOS = 30;    // Sin separador
}
```

#### Constantes de texto

```java
public class MensajesAplicacion {
    // Correcto ✅
    public static final String MENSAJE_BIENVENIDA = "Bienvenido al sistema";
    public static final String ERROR_CONEXION = "No se pudo conectar";
    public static final String FORMATO_FECHA = "dd/MM/yyyy";

    // Incorrecto ❌
    public static final String mensajeBienvenida = "...";  // dromedarioCase
    public static final String ErrorConexion = "...";      // CamelloCase
    public static final String FORMATOFECHA = "...";       // Sin separador
}
```

#### Constantes de configuración

```java
public class Configuracion {
    // Correcto ✅
    public static final String BASE_URL = "https://api.ejemplo.com";
    public static final int PUERTO_SERVIDOR = 8080;
    public static final String NOMBRE_BASE_DATOS = "aplicacion_db";
    public static final boolean MODO_DEBUG = true;

    // Incorrecto ❌
    public static final String baseUrl = "...";           // dromedarioCase
    public static final int PuertoServidor = 8080;        // CamelloCase
    public static final String nombre_base_datos = "..."; // No mayúsculas
}
```

### Casos Especiales

#### Enumeraciones

Los valores de enumeraciones también usan `SNAKE_CASE`:

```java
// Correcto ✅
public enum EstadoPedido {
    PENDIENTE,
    EN_PROCESO,
    COMPLETADO,
    CANCELADO
}

public enum DiaSemana {
    LUNES,
    MARTES,
    MIERCOLES,
    JUEVES,
    VIERNES,
    SABADO,
    DOMINGO
}

// Incorrecto ❌
public enum EstadoPedido {
    Pendiente,      // CamelloCase
    enProceso,      // dromedarioCase
    completado,     // minúsculas
    CANCELADO       // ✅ Este sí está bien
}
```

#### Constantes vs. variables final

No toda variable `final` es una constante:

```java
public class Ejemplo {
    // Constante: static final, valor fijo
    public static final int MAX_VALOR = 100;  // ✅ SNAKE_CASE

    // No es constante: solo final, puede variar entre instancias
    private final String nombreUsuario;  // ✅ dromedarioCase

    public Ejemplo(String nombre) {
        this.nombreUsuario = nombre;  // Valor depende de la instancia
    }
}
```
:::{important} Solo usá `SNAKE_CASE` para constantes verdaderas
(`static final`). Las variables de instancia `final` siguen usando
`dromedarioCase` porque su valor puede variar entre objetos. 
:::

#### Constantes privadas vs. públicas

La convención se aplica independientemente de la visibilidad:

```java
public class Validador {
    // Constantes privadas
    private static final int LONGITUD_MINIMA = 8;
    private static final String PATRON_EMAIL = "^[a-z0-9]+@[a-z]+\\.[a-z]{2,}$";

    // Constantes públicas
    public static final int ERROR_VALIDACION = -1;
    public static final String VERSION = "1.0.0";
}
```

#### Acrónimos en constantes

En constantes, los acrónimos se integran naturalmente:

```java
// Correcto ✅
public static final String URL_API = "https://api.ejemplo.com";
public static final int ID_ADMINISTRADOR = 1;
public static final String FORMATO_JSON = "application/json";
public static final int PUERTO_HTTP = 80;
public static final int PUERTO_HTTPS = 443;

// Incorrecto ❌
public static final String URLApi = "...";      // No es SNAKE_CASE
public static final int IdAdministrador = 1;    // No es SNAKE_CASE
```

### Buenas prácticas adicionales

#### Agrupar constantes relacionadas

```java
public class ConfiguracionHttp {
    // Timeouts
    public static final int TIMEOUT_CONEXION_MS = 5000;
    public static final int TIMEOUT_LECTURA_MS = 10000;
    public static final int TIMEOUT_ESCRITURA_MS = 10000;

    // Códigos de respuesta
    public static final int CODIGO_OK = 200;
    public static final int CODIGO_ERROR = 500;
    public static final int CODIGO_NO_ENCONTRADO = 404;
}
```

#### Usar constantes en lugar de números mágicos

Ver también regla {ref}`regla-0x000C`:

```java
// Incorrecto ❌ - números mágicos
if (edad >= 18) {
    // ...
}
double area = radio * radio * 3.14159;

// Correcto ✅ - constantes nombradas
public static final int EDAD_MINIMA_LEGAL = 18;
public static final double PI = 3.14159;

if (edad >= EDAD_MINIMA_LEGAL) {
    // ...
}
double area = radio * radio * PI;
```
:::{tip}

Si usás un valor literal más de una vez en tu código, probablemente
debería ser una constante. Esto facilita el mantenimiento y reduce errores. 

:::


(regla-generales-0x0006)=
## `0x0006` - Los identificadores DEBEN ser descriptivos

### Explicación

Los identificadores (nombres de clases, métodos, variables) deben ser
autoexplicativos y revelar claramente su propósito o contenido. El código se lee
muchas más veces de las que se escribe, por lo que la claridad debe primar sobre
la brevedad.

### Justificación

1. **Legibilidad**: El código debe ser autoexplicativo sin necesidad de
   comentarios adicionales.
2. **Mantenibilidad**: Nombres claros facilitan modificaciones futuras por otros
   desarrolladores (o por vos mismo meses después).
3. **Reducción de errores**: Nombres ambiguos pueden llevar a
   malinterpretaciones y bugs.
4. **Documentación implícita**: Buenos nombres hacen innecesarios muchos
   comentarios.
5. **Profesionalismo**: Refleja atención al detalle y preocupación por la
   calidad del código.

### Criterios de descriptividad

Un identificador es descriptivo si cumple:

1. **Usa palabras completas**, no abreviaciones crípticas
2. **Revela intención**, no solo tipo de dato
3. **Aporta contexto** dentro de su ámbito
4. **Es pronunciable** y fácil de discutir con el equipo

### Ejemplos

#### Variables

```java
// Incorrecto ❌ - No descriptivo
int n;          // ¿Qué es n?
String s;       // ¿Qué contiene?
double val;     // ¿Valor de qué?
int cnt;        // ¿Contador de qué?
String tmp;     // ¿Temporal para qué?
List<String> d; // ¿Datos de qué tipo?

// Correcto ✅ - Descriptivo
int numeroDeEstudiantes;
String nombreCompleto;
double saldoCuentaBancaria;
int contadorIntentosFallidos;
String descripcionTemporalProducto;
List<String> nombresAprobados;
```

#### Métodos

```java
// Incorrecto ❌ - No descriptivo
public void proc() { ... }           // ¿Qué procesa?
public int get() { ... }             // ¿Obtiene qué?
public void upd(String s) { ... }    // ¿Actualiza qué?
public boolean chk() { ... }         // ¿Verifica qué?

// Correcto ✅ - Descriptivo
public void procesarPagoTarjeta() { ... }
public int obtenerEdadUsuario() { ... }
public void actualizarSaldoCuenta(String numeroCuenta) { ... }
public boolean verificarCredenciales() { ... }
```

#### Clases

```java
// Incorrecto ❌ - No descriptivo
public class Mgr { ... }        // ¿Manager de qué?
public class Data { ... }       // ¿Qué datos?
public class Utils { ... }      // ¿Utilidades de qué?
public class Info { ... }       // ¿Información de qué?

// Correcto ✅ - Descriptivo
public class GestorUsuarios { ... }
public class DatosTransaccion { ... }
public class UtilidadesFechas { ... }
public class InformacionSistema { ... }
```

### Casos Especiales

#### Variables de una letra: Contextos permitidos

Existen contextos específicos donde variables de una letra son **aceptables y
estándar**:

**Bucles e índices** (convención matemática):

```java
// Aceptable ✅ - Contexto de bucle simple
for (int i = 0; i < tamano; i++) {
    procesar(elementos[i]);
}

// Aceptable ✅ - Bucles anidados
for (int i = 0; i < filas; i++) {
    for (int j = 0; j < columnas; j++) {
        matriz[i][j] = calcular(i, j);
    }
}

// Límites numéricos comunes
int n = lista.size();
int m = matriz.length;
```

**Coordenadas y puntos** (convención geométrica):

```java
// Aceptable ✅ - Coordenadas cartesianas
double x = punto.getX();
double y = punto.getY();
double z = punto.getZ();

// Cálculos matemáticos
double distancia = Math.sqrt(x * x + y * y);
```

**Variables matemáticas estándar**:

```java
// Aceptable ✅ - Fórmulas conocidas
double a = coeficiente1;
double b = coeficiente2;
double c = terminoIndependiente;
double discriminante = b * b - 4 * a * c;
```
:::{warning}

Las variables de una letra **solo** son apropiadas en:

- Índices de bucles simples (`i`, `j`, `k`)
- Coordenadas espaciales (`x`, `y`, `z`)
- Fórmulas matemáticas estándar (`a`, `b`, `c`)
- Límites numéricos en contexto claro (`n`, `m`)

En **cualquier otro contexto**, usá nombres descriptivos completos.

:::


#### Abreviaciones comúnmente aceptadas

Algunas abreviaciones son tan estándar que son más claras que la forma completa:

```java
// Aceptables en contexto apropiado
int max = encontrarMaximo();      // vs maximoValor
int min = encontrarMinimo();      // vs minimoValor
String id = generarIdentificador(); // vs identificador
String url = construirUrl();      // vs localizadorRecursoUniforme

// Pero mejor aún: combinar con contexto
int maxIntentos = 3;
int minEdad = 18;
String idUsuario = "USR001";
String urlServidor = "https://...";
```

#### Bucles complejos requieren nombres descriptivos

Cuando los bucles son complejos o anidados en múltiples niveles, usá nombres
descriptivos:

```java
// Incorrecto ❌ - bucle complejo con variables de una letra
for (int i = 0; i < estudiantes.size(); i++) {
    for (int j = 0; j < materias.size(); j++) {
        for (int k = 0; k < examenes.size(); k++) {
            // ¿Qué representa cada índice? Difícil de seguir
            procesar(datos[i][j][k]);
        }
    }
}

// Correcto ✅ - bucle complejo con nombres descriptivos
for (int indiceEstudiante = 0; indiceEstudiante < estudiantes.size(); indiceEstudiante++) {
    for (int indiceMateria = 0; indiceMateria < materias.size(); indiceMateria++) {
        for (int indiceExamen = 0; indiceExamen < examenes.size(); indiceExamen++) {
            // Ahora es claro qué procesa cada nivel
            procesar(datos[indiceEstudiante][indiceMateria][indiceExamen]);
        }
    }
}

// Mejor aún ✅ - usar for-each cuando sea posible
for (Estudiante estudiante : estudiantes) {
    for (Materia materia : estudiante.getMaterias()) {
        for (Examen examen : materia.getExamenes()) {
            procesar(estudiante, materia, examen);
        }
    }
}
```

### Reglas de oro para nombres descriptivos
:::{important} **Pregunta clave**

Si alguien lee solo el nombre del identificador, ¿puede entender qué representa sin leer código adicional?

- **SÍ** → Nombre descriptivo ✅
- **NO** → Necesita mejorarse ❌ 

:::


#### Test de pronunciabilidad

Los identificadores deben poder pronunciarse y discutirse:

```java
// Difícil de pronunciar ❌
String usrNmFst;      // "user name first"?
int stdCnt;           // "standard count"?
double tmpVal;        // "temporary value"?

// Fácil de pronunciar ✅
String nombrePrimerUsuario;
int cantidadEstudiantes;
double valorTemporal;
```

#### Test de búsqueda

Los nombres genéricos son difíciles de buscar en el código:

```java
// Difícil de buscar ❌
String data;    // Buscar "data" devuelve demasiados resultados
int temp;       // "temp" aparece en muchos contextos
int count;      // "count" es muy genérico

// Fácil de buscar ✅
String datosTransaccion;
int temperaturaMaxima;
int contadorTransaccionesExitosas;
```

### Ejemplos del mundo real

#### Mal ejemplo de código real

```java
// ❌ Código difícil de entender
public void proc(String s, int n) {
    String tmp = s.substring(0, n);
    int val = tmp.length();
    if (val > 0) {
        List<String> l = new ArrayList<>();
        for (int i = 0; i < val; i++) {
            l.add(tmp.charAt(i) + "");
        }
    }
}
```

#### Buen ejemplo refactorizado

```java
// ✅ Código claro y autoexplicativo
public void procesarSubcadena(String textoOriginal, int longitudMaxima) {
    String subcadena = textoOriginal.substring(0, longitudMaxima);
    int cantidadCaracteres = subcadena.length();

    if (cantidadCaracteres > 0) {
        List<String> caracteresIndividuales = new ArrayList<>();
        for (int indice = 0; indice < cantidadCaracteres; indice++) {
            caracteresIndividuales.add(subcadena.charAt(indice) + "");
        }
    }
}
```
:::{tip}

Si necesitás comentarios para explicar qué hace una variable,
probablemente el nombre no es lo suficientemente descriptivo. Mejorá el nombre
en lugar de agregar comentarios. 

:::


(regla-0x0006A)=
## `0x0006A` - Nomenclatura de interfaces según su propósito

### Explicación

Las interfaces en Java deben nombrarse según el tipo de comportamiento que
describen. Esta regla establece una distinción semántica entre interfaces que
representan **capacidades de instancia** (sufijo `-able`) y las que representan
**operaciones funcionales** (sufijo `-or`/`-er`).

### Justificación

1. **Claridad de intención**: El sufijo revela inmediatamente si la interfaz
   representa una capacidad del objeto o una operación externa.
2. **Patrones de diseño**: Facilita identificar el patrón de diseño que se está
   aplicando (Strategy, Visitor, etc.).
3. **Uso correcto**: Ayuda a decidir cómo y dónde implementar la interfaz.
4. **Mantenibilidad**: Hace evidente si la implementación debe usar estado de
   instancia o ser stateless.
5. **Convención Java**: Alineado con interfaces estándar (`Comparable`,
   `Serializable`, `Comparator`, `Iterator`).

### Interfaces con sufijo `-able` (comportamiento exportado)

#### ¿Cuándo usar `-able`?

Usá sufijo `-able` para interfaces que describen **capacidades o
comportamientos** que:

- Las clases exponen públicamente
- Dependen del estado de la instancia (`this`)
- Representan una propiedad o habilidad del objeto
- Se implementan como métodos de instancia

#### Ejemplos correctos

```java
public interface Comparable<T> {
    int compararCon(T otro);  // Depende del estado de this
}

public interface Serializable {
    byte[] serializar();  // Serializa el estado actual
}

public interface Validable {
    boolean esValido();  // Valida el estado actual
}

public interface Cloneable {
    Object clonar();  // Clona el estado actual
}

public interface Dibujable {
    void dibujar(Graphics g);  // Dibuja el objeto usando su estado
}

public interface Ejecutable {
    void ejecutar();  // Ejecuta acción basada en configuración del objeto
}
```

#### Implementación típica

```java
// Comportamiento que depende del estado del objeto
public class Persona implements Comparable<Persona> {
    private String nombre;
    private int edad;

    @Override
    public int compararCon(Persona otra) {
        return this.edad - otra.edad;  // ✅ Usa this.edad (estado)
    }
}

public class Documento implements Serializable {
    private String titulo;
    private String contenido;

    @Override
    public byte[] serializar() {
        // ✅ Serializa this.titulo y this.contenido
        return (titulo + "|" + contenido).getBytes();
    }
}
```

### Interfaces con sufijo `-or`/`-er` (comportamiento funcional)

#### ¿Cuándo usar `-or`/`-er`?

Usá sufijo `-or` o `-er` para interfaces que describen **operaciones
funcionales** que:

- Se implementan de forma estática o con estado inmutable
- No dependen directamente del estado de `this`
- Representan estrategias, funciones o transformaciones
- Típicamente se implementan en clases internas o anónimas

#### Ejemplos correctos

```java
public interface Comparador<T> {
    int compare(T o1, T o2);  // No usa this, compara dos objetos externos
}

public interface Validador<T> {
    boolean validate(T objeto);  // Validación externa sin estado
}

public interface Calculador {
    double calculate(double x, double y);  // Operación sin estado
}

public interface Conversor<F, T> {
    T convert(F from);  // Transformación sin estado propio
}

public interface Procesador<T> {
    T process(T input);  // Procesamiento funcional
}

public interface Filtrador<T> {
    boolean filter(T elemento);  // Filtrado sin estado
}

public interface Mapeador<T, R> {
    R map(T entrada);  // Mapeo funcional
}
```

#### Implementación típica

```java
// Comportamiento funcional sin depender de this
public class OrdenamientoPersonas {
    private static class ComparadorPorNombre implements Comparador<Persona> {
        @Override
        public int compare(Persona p1, Persona p2) {
            // ✅ No usa this, opera sobre parámetros externos
            return p1.getNombre().compareTo(p2.getNombre());
        }
    }

    private static class ComparadorPorEdad implements Comparador<Persona> {
        @Override
        public int compare(Persona p1, Persona p2) {
            // ✅ Comparación stateless
            return p1.getEdad() - p2.getEdad();
        }
    }
}

```

### Comparación directa

#### Ejemplo con `-able` (usa `this`)

```java
public interface Comparable<T> {
    int compararCon(T otro);
}

public class Producto implements Comparable<Producto> {
    private double precio;

    @Override
    public int compararCon(Producto otro) {
        // ✅ Compara THIS.precio con otro.precio
        return Double.compare(this.precio, otro.precio);
    }
}

// Uso: el objeto se compara a sí mismo con otro
Producto p1 = new Producto(100.0);
Producto p2 = new Producto(200.0);
int resultado = p1.compararCon(p2);  // p1 usa su propio estado
```

#### Ejemplo con `-or` (sin usar `this`)

```java
public interface Comparador<T> {
    int compare(T o1, T o2);
}

public class ComparadorProductoPorPrecio implements Comparador<Producto> {
    @Override
    public int compare(Producto p1, Producto p2) {
        // ✅ No usa this, opera sobre dos parámetros externos
        return Double.compare(p1.getPrecio(), p2.getPrecio());
    }
}

// Uso: un objeto externo compara dos productos
Comparador<Producto> comparador = new ComparadorProductoPorPrecio();
Producto p1 = new Producto(100.0);
Producto p2 = new Producto(200.0);
int resultado = comparador.compare(p1, p2);  // Comparación externa
```

### Tabla resumen

| Sufijo      | Tipo                   | Depende de `this` | Métodos            | Uso típico                | Ejemplo                                   |
| ----------- | ---------------------- | ----------------- | ------------------ | ------------------------- | ----------------------------------------- |
| `-able`     | Capacidad de instancia | ✅ Sí             | Instancia          | Comportamiento del objeto | `Comparable`, `Serializable`, `Dibujable` |
| `-or`/`-er` | Operación funcional    | ❌ No             | Estático/Stateless | Estrategia externa        | `Comparator`, `Validator`, `Processor`    |

### Patrones de diseño relacionados
:::{note} **Strategy Pattern**

Las interfaces con sufijo `-or` frecuentemente
implementan el patrón Strategy, donde el comportamiento se inyecta desde afuera.

**Capability Pattern**: Las interfaces con sufijo `-able` representan
capacidades que el objeto posee inherentemente. 

:::


### Casos especiales y excepciones

#### Interfaces de Java estándar

Algunas interfaces del JDK no siguen estrictamente esta convención por razones
históricas:

```java
// Excepciones históricas en Java estándar
Runnable      // Podría ser Ejecutable
Callable      // Similar a Ejecutable
Iterable      // ✅ Correcto: el objeto ES iterable
Iterator      // Operador de iteración (exception al patrón)
```

#### Interfaces funcionales (Java 8+)

Las interfaces funcionales modernas tienden a usar sufijo `-or`:

```java
// Interfaces funcionales estándar
Function<T, R>     // Transformador
Predicate<T>       // Evaluador/Filtrador
Consumer<T>        // Consumidor
Supplier<T>        // Proveedor
BiFunction<T, U, R> // Transformador binario
```

Podés crear tus propias interfaces funcionales siguiendo el patrón:

```java
@FunctionalInterface
public interface Transformador<T, R> {
    R transformar(T entrada);
}

@FunctionalInterface
public interface Evaluador<T> {
    boolean evaluar(T objeto);
}
```

:::{note} Restricción sobre Programación Funcional

Recuerden que no es objetivo de la cátedra el uso de las características de
programación funcional.

:::

#### ¿Qué pasa con nombres sin sufijos?

Algunas interfaces representan contratos o tipos abstractos sin énfasis en el
comportamiento:

```java
// Correcto ✅ - Representan tipos/contratos, no comportamientos
public interface Lista<T> {
    void agregar(T elemento);
    T obtener(int indice);
}

public interface Figura {
    double calcularArea();
    double calcularPerimetro();
}

public interface Repositorio<T> {
    void guardar(T entidad);
    T buscarPorId(int id);
}
```

### Ejemplos prácticos del mundo real

#### Sistema de ordenamiento

```java
// Enfoque -able: Los objetos se comparan a sí mismos
public class Estudiante implements Comparable<Estudiante> {
    private String nombre;
    private double promedio;

    @Override
    public int compararCon(Estudiante otro) {
        return Double.compare(this.promedio, otro.promedio);
    }
}

// Enfoque -or: Comparador externo con diferentes estrategias
public class ComparadorEstudiantes {
    public static class PorNombre implements Comparador<Estudiante> {
        @Override
        public int compare(Estudiante e1, Estudiante e2) {
            return e1.getNombre().compareTo(e2.getNombre());
        }
    }

    public static class PorPromedio implements Comparador<Estudiante> {
        @Override
        public int compare(Estudiante e1, Estudiante e2) {
            return Double.compare(e1.getPromedio(), e2.getPromedio());
        }
    }
}
```

#### Sistema de validación

```java
// Enfoque -able: El objeto se valida a sí mismo
public class FormularioRegistro implements Validable {
    private String email;
    private String contrasena;

    @Override
    public boolean esValido() {
        return this.email.contains("@") &&
               this.contrasena.length() >= 8;
    }
}

// Enfoque -or: Validador externo reutilizable
public class ValidadorEmail implements Validador<String> {
    @Override
    public boolean validate(String email) {
        return email != null &&
               email.contains("@") &&
               email.contains(".");
    }
}

public class ValidadorContrasena implements Validador<String> {
    private final int longitudMinima;

    public ValidadorContrasena(int longitudMinima) {
        this.longitudMinima = longitudMinima;
    }

    @Override
    public boolean validate(String contrasena) {
        return contrasena != null &&
               contrasena.length() >= longitudMinima;
    }
}
```
:::{tip} **¿Cuál elegir?**

- Si el comportamiento es **inherente al objeto** y usa su estado → `-able`
- Si el comportamiento es una **estrategia externa** reutilizable → `-or`/`-er`
- Si no está claro, preguntate: "¿Este comportamiento podría aplicarse a objetos
  de diferentes clases?" → Si es sí, probablemente sea `-or` 
  
:::


### Anti-patrones a evitar

```java
// ❌ Mal: Sufijo -able pero no usa this
public interface Calculadorable {  // Debería ser Calculador
    double calculate(double x, double y);  // No usa estado
}

// ❌ Mal: Sufijo -or pero depende de this
public interface Comparator<T> {
    int compare(T otro);  // Solo un parámetro, usa this implícitamente
    // Debería ser Comparable con compararCon(T otro)
}

// ✅ Correcto: Coincide semántica y sintaxis
public interface Calculador {
    double calculate(double x, double y);
}

public interface Comparable<T> {
    int compararCon(T otro);
}
```

**Nota**: Esta distinción ayuda a entender la intención del diseño y cómo se
usará la interfaz.

(regla-generales-0x0007)=
## `0x0007` - Los identificadores booleanos deben usar prefijos interrogativos

### Explicación

Los identificadores de tipo `boolean` (variables, parámetros, métodos que
retornan booleano) deben nombrarse como preguntas, usando prefijos
interrogativos. Esto hace que el código sea más legible y natural, como si se
estuviera haciendo una pregunta al objeto.

### Justificación

1. **Legibilidad natural**: El código se lee como una pregunta-respuesta
   natural.
2. **Claridad de intención**: Es inmediatamente obvio que el valor es booleano.
3. **Expresividad en condicionales**: Las condiciones se leen como oraciones
   naturales.
4. **Estándar de la industria**: Prácticamente todas las APIs de Java siguen
   esta convención.
5. **Prevención de errores**: Reduce confusión sobre qué representa el valor.

### Prefijos estándar

| Prefijo español | Prefijo inglés | Uso                     | Ejemplo                                 |
| --------------- | -------------- | ----------------------- | --------------------------------------- |
| `es`            | `is`           | Estado o característica | `esActivo`, `isValid`                   |
| `tiene`         | `has`          | Posesión o presencia    | `tienePermisos`, `hasChildren`          |
| `puede`         | `can`          | Capacidad o permiso     | `puedeEditar`, `canExecute`             |
| `esta`          | `should`       | Recomendación           | `estaListo`, `shouldRetry`              |
| `necesita`      | `needs`        | Requerimiento           | `necesitaActualizacion`, `needsRefresh` |
| `fue`           | `was`          | Estado pasado           | `fueModificado`, `wasProcessed`         |
| `sera`          | `will`         | Estado futuro           | `seraEliminado`, `willExpire`           |

### Ejemplos

#### Variables booleanas

```java
public class Usuario {
    // Correcto ✅
    private boolean esAdministrador;
    private boolean tienePermisos;
    private boolean puedeEditar;
    private boolean estaActivo;
    private boolean fueValidado;

    // Incorrecto ❌
    private boolean administrador;    // No es interrogativo
    private boolean permisos;         // No es interrogativo
    private boolean editable;         // Ambiguo (¿adjetivo o verbo?)
    private boolean activo;           // No es interrogativo
    private boolean validado;         // Participio, no pregunta
}
```

#### Métodos que retornan boolean

```java
public class Documento {
    // Correcto ✅
    public boolean esVacio() {
        return contenido.isEmpty();
    }

    public boolean tienePermisoLectura() {
        return permisos.contains(Permiso.LECTURA);
    }

    public boolean puedeGuardarse() {
        return !esVacio() && tieneNombreValido();
    }

    public boolean estaModificado() {
        return estadoModificacion;
    }

    // Incorrecto ❌
    public boolean vacio() {           // Sin prefijo
        return contenido.isEmpty();
    }

    public boolean permisoLectura() {  // Sin prefijo
        return permisos.contains(Permiso.LECTURA);
    }

    public boolean guardable() {       // Sufijo -able sin prefijo
        return !esVacio();
    }
}
```

#### Variables locales

```java
public void procesarPedido(Pedido pedido) {
    // Correcto ✅
    boolean esValido = validar(pedido);
    boolean tieneStock = verificarStock(pedido);
    boolean puedeEnviarse = esValido && tieneStock;

    if (puedeEnviarse) {
        enviar(pedido);
    }

    // Incorrecto ❌
    boolean valido = validar(pedido);
    boolean stock = verificarStock(pedido);
    boolean enviable = valido && stock;

    if (enviable) {
        enviar(pedido);
    }
}
```

### Casos especiales

#### Condicionales: Legibilidad natural

Con prefijos interrogativos, las condiciones se leen naturalmente:

```java
// Con prefijos ✅ - Se lee como pregunta natural
if (usuario.esAdministrador()) {
    // "¿El usuario es administrador? Sí → ejecutar"
}

if (archivo.puedeLeerse() && archivo.tieneContenido()) {
    // "¿El archivo puede leerse Y tiene contenido? Sí → procesar"
}

if (!conexion.estaActiva()) {
    // "¿La conexión NO está activa? Sí → reconectar"
}

// Sin prefijos ❌ - Se lee forzado o ambiguo
if (usuario.administrador()) {  // ¿Qué devuelve? ¿Un objeto? ¿Un boolean?
    // ...
}

if (archivo.legible() && archivo.contenido()) {  // Poco natural
    // ...
}
```

#### Negaciones: Evitar doble negativo

```java
// Problemático ⚠️ - Doble negación confusa
private boolean noEstaInactivo;

if (!noEstaInactivo) {  // ❌ Difícil de leer mentalmente
    // ...
}

// Mejor ✅ - Afirmativo
private boolean estaActivo;

if (estaActivo) {  // ✅ Claro y directo
    // ...
}

// Si necesitás el negativo
if (!estaActivo) {  // ✅ Una sola negación
    // ...
}
```
:::{warning} Evitá nombres booleanos negativos (`noEsValido`, `noTienePermisos`,
`noPuedeEditar`). Preferí la forma afirmativa y negá en la condición cuando sea
necesario. 
:::

#### Getters para booleanos

En JavaBeans, los getters de booleanos usan el prefijo `is` en lugar de `get`:

```java
public class Configuracion {
    private boolean modoDebug;
    private boolean autoguardado;

    // Correcto ✅ - Usar is para booleanos
    public boolean isModoDebug() {
        return modoDebug;
    }

    public boolean isAutoguardado() {
        return autoguardado;
    }

    // También aceptable ✅ - Seguir la convención de español
    public boolean esModoDebug() {
        return modoDebug;
    }

    public boolean estaAutoguardadoActivo() {
        return autoguardado;
    }

    // Incorrecto ❌
    public boolean getModoDebug() {  // Usar is, no get
        return modoDebug;
    }

    public boolean autoguardado() {  // Falta prefijo
        return autoguardado;
    }
}
```

#### Constantes booleanas

Las constantes booleanas también se benefician de nombres interrogativos:

```java
// Correcto ✅
public static final boolean ES_MODO_PRODUCCION = true;
public static final boolean PUEDE_USAR_CACHE = true;
public static final boolean TIENE_LICENCIA_VALIDA = false;

// Menos claro
public static final boolean MODO_PRODUCCION = true;  // ⚠️ Ambiguo
public static final boolean CACHE = true;            // ⚠️ ¿Cache qué?
public static final boolean LICENCIA = false;        // ⚠️ No es claro
```

### Prefijos según el contexto

#### Estado o propiedad: `es` / `is`

```java
boolean esVacio = lista.isEmpty();
boolean esValido = formulario.esValido();
boolean esPar = numero % 2 == 0;
boolean esNulo = objeto == null;
boolean esPositivo = valor > 0;
```

#### Posesión o contenido: `tiene` / `has`

```java
boolean tieneElementos = !lista.isEmpty();
boolean tienePermisos = usuario.tieneRol(ROL_ADMIN);
boolean tieneHijos = nodo.tieneHijos();
boolean tieneErrores = validacion.tieneErrores();
```

#### Capacidad o permiso: `puede` / `can`

```java
boolean puedeEditar = archivo.tienePermiso(ESCRITURA);
boolean puedeEjecutar = proceso.tieneRecursos();
boolean puedeConectarse = red.estaDisponible();
boolean puedeRetirarse = saldo >= monto;
```

#### Estado temporal: `esta` / `should`

```java
boolean estaListo = tarea.getProgreso() == 100;
boolean estaProcesando = hilo.isAlive();
boolean estaConectado = socket.isConnected();
boolean estaBloqueado = cuenta.getIntentosFallidos() > 3;
```

#### Verificación o requerimiento: `necesita` / `needs`

```java
boolean necesitaActualizacion = version < VERSION_ACTUAL;
boolean necesitaValidacion = !fueValidado;
boolean necesitaRecarga = cache.isStale();
```

### Ejemplos en contexto real

#### Sistema de autenticación

```java
public class SistemaAutenticacion {
    public boolean autenticar(String usuario, String contrasena) {
        Usuario usr = buscarUsuario(usuario);

        // ✅ Legibilidad natural
        boolean esUsuarioValido = usr != null;
        boolean esContrasenaCorrecta = verificarContrasena(usr, contrasena);
        boolean estaCuentaActiva = usr != null && usr.estaActiva();
        boolean tienePermisosNecesarios = verificarPermisos(usr);

        return esUsuarioValido &&
               esContrasenaCorrecta &&
               estaCuentaActiva &&
               tienePermisosNecesarios;
    }
}
```

#### Validación de formulario

```java
public class FormularioRegistro {
    private String email;
    private String contrasena;
    private String confirmacionContrasena;

    public boolean esValido() {
        // ✅ Cada condición es una pregunta clara
        boolean esEmailValido = email != null && email.contains("@");
        boolean esContrasenaSegura = contrasena != null && contrasena.length() >= 8;
        boolean esConfirmacionCorrecta = contrasena.equals(confirmacionContrasena);

        return esEmailValido && esContrasenaSegura && esConfirmacionCorrecta;
    }
}
```

#### Sistema de permisos

```java
public class GestorPermisos {
    public boolean puedeRealizarAccion(Usuario usuario, Accion accion) {
        // ✅ Flujo lógico claro mediante preguntas
        boolean esAdministrador = usuario.tieneRol(ROL_ADMIN);
        boolean tieneLicenciaActiva = usuario.getLicencia().estaVigente();
        boolean tienePermisoEspecifico = usuario.tienePermiso(accion);
        boolean estaEnHorarioPermitido = verificarHorario();

        return esAdministrador ||
               (tieneLicenciaActiva && tienePermisoEspecifico && estaEnHorarioPermitido);
    }
}
```

### Anti-patrones comunes

#### No usar sufijos participiales sin prefijo

```java
// ❌ Incorrecto - Participio sin prefijo
boolean validado;
boolean procesado;
boolean encontrado;
boolean habilitado;

// ✅ Correcto - Con prefijo interrogativo
boolean fueValidado;
boolean estaProcesado;
boolean fueEncontrado;
boolean estaHabilitado;
```

#### No usar adjetivos directos

```java
// ❌ Incorrecto - Adjetivo sin prefijo
boolean activo;
boolean disponible;
boolean completo;
boolean valido;

// ✅ Correcto - Con prefijo interrogativo
boolean estaActivo;
boolean estaDisponible;
boolean estaCompleto;
boolean esValido;
```

#### Evitar verbos en infinitivo

```java
// ❌ Incorrecto - Verbo en infinitivo
boolean validar;   // ¿Es una acción o un estado?
boolean procesar;  // Confuso
boolean ejecutar;  // Ambiguo

// ✅ Correcto - Pregunta sobre capacidad
boolean puedeValidarse;
boolean estaProcesando;
boolean puedeEjecutarse;
```

### Integración con expresiones complejas

Los prefijos interrogativos hacen que las expresiones booleanas complejas sean
legibles:

```java
public boolean puedeAprobarCredito(Cliente cliente, double monto) {
    boolean esClienteActivo = cliente.estaActivo();
    boolean tieneBuenHistorial = cliente.getScore() > 700;
    boolean esMontoRazonable = monto <= cliente.getLimiteCredito();
    boolean noTieneDeudasVencidas = !cliente.tieneDeudasVencidas();
    boolean cumpleEdadMinima = cliente.getEdad() >= EDAD_MINIMA_CREDITO;

    // ✅ La expresión final es muy legible
    return esClienteActivo &&
           tieneBuenHistorial &&
           esMontoRazonable &&
           noTieneDeudasVencidas &&
           cumpleEdadMinima;
}
```
:::{tip} Si tu expresión booleana es compleja, extraé sub-condiciones en
variables con nombres interrogativos. Esto documenta la lógica y mejora
drásticamente la legibilidad. 
:::

### Comparación: Con y sin prefijos

```java
// ❌ Sin prefijos - Difícil de leer
if (usuario.activo() && documento.disponible() && sistema.funcionando()) {
    // ¿Qué retornan estos métodos? ¿Booleanos? ¿Objetos?
}

// ✅ Con prefijos - Claro y directo
if (usuario.estaActivo() && documento.estaDisponible() && sistema.estaFuncionando()) {
    // Obviamente retornan booleanos y se leen como preguntas
}
```
:::{important} **Regla de oro**: Si leés en voz alta el código y suena como una
pregunta natural, el nombre es correcto.

Ejemplos:

- `if (usuario.esAdministrador())` → "¿El usuario es administrador?"
- `if (archivo.puedeLeerse())` → "¿El archivo puede leerse?"
- `if (lista.tieneElementos())` → "¿La lista tiene elementos?" 
:::

(regla-generales-0x0008)=
## `0x0008` - Los identificadores no deben llevar el tipo de lo que procesan

### Explicación

Los identificadores no deben incluir información sobre el tipo de dato que
representan. Esto se conoce como evitar la "notación húngara" o prefijos de
tipo. Java es un lenguaje fuertemente tipado, por lo que el tipo ya está
explícito en la declaración.

### Justificación

1. **Redundancia**: El sistema de tipos de Java ya proporciona esta información.
2. **Mantenibilidad**: Si cambia el tipo, no hay que cambiar el nombre en todo
   el código.
3. **Legibilidad**: Los nombres enfocados en el propósito son más claros que los
   enfocados en el tipo.
4. **Convención moderna**: La notación húngara es obsoleta y no se usa en código
   Java moderno.
5. **IDEs modernos**: Los editores muestran información de tipo automáticamente
   (hover, autocompletado).

### ¿Qué es la notación húngara?

Convención obsoleta de los años 80-90 que prefijaba variables con su tipo:

```
strNombre   → string nombre
intEdad     → integer edad
arrValores  → array valores
lstEstudiantes → list estudiantes
```

Era útil en lenguajes sin tipado estático o IDEs primitivos, pero en Java
moderno es **contraproducente**.

### Ejemplos

#### Variables

```java
// Incorrecto ❌ - Incluye el tipo en el nombre
String stringNombre = "Juan";
int intContador = 0;
double doubleTotal = 0.0;
boolean boolValido = true;
List<String> listaNombres = new ArrayList<>();
Map<String, Integer> mapEdades = new HashMap<>();
String[] arrayApellidos = new String[10];

// Correcto ✅ - Nombre describe el propósito, no el tipo
String nombre = "Juan";
int contador = 0;
double total = 0.0;
boolean esValido = true;
List<String> nombres = new ArrayList<>();
Map<String, Integer> edades = new HashMap<>();
String[] apellidos = new String[10];
```

#### Métodos

```java
public class Procesador {
    // Incorrecto ❌ - Tipo en el nombre
    public int calcularIntSuma(int[] numeros) { ... }
    public String obtenerStringNombre() { ... }
    public List<Integer> buscarListaIds() { ... }
    public boolean verificarBoolEstado() { ... }

    // Correcto ✅ - Nombre describe la acción/propósito
    public int calcularSuma(int[] numeros) { ... }
    public String obtenerNombre() { ... }
    public List<Integer> buscarIds() { ... }
    public boolean verificarEstado() { ... }
}
```

#### Parámetros

```java
// Incorrecto ❌
public void procesar(String strTexto, int intLongitud, List<String> listaPalabras) {
    // ...
}

// Correcto ✅
public void procesar(String texto, int longitud, List<String> palabras) {
    // El tipo ya está declarado
}
```

### Casos especiales

#### Cuando el tipo ES el propósito

En algunos casos, el tipo es realmente lo más descriptivo:

```java
// Aceptable ✅ - El tipo ES el propósito en este contexto
public class Configuracion {
    private Properties propiedades;  // Es un objeto Properties
    private Map<String, String> mapa;  // El contexto requiere saber que es un mapa
}

// Pero mejor aún ✅ - Agregar contexto de uso
public class Configuracion {
    private Properties propiedadesSistema;
    private Map<String, String> mapaConfiguraciones;
}
```

#### Colecciones: ¿Plural o tipo?

Para colecciones, usar plural es más claro que incluir el tipo:

```java
// ❌ Incorrecto - Tipo en el nombre
List<String> listaNombres;
ArrayList<Integer> arrayListIds;
Set<Persona> setPersonas;
Map<String, Usuario> mapUsuarios;

// ⚠️ Aceptable - Pero puede mejorar
List<String> nombres;
ArrayList<Integer> ids;
Set<Persona> personas;
Map<String, Usuario> usuarios;

// ✅ Mejor - Plural con contexto
List<String> nombresEstudiantes;
List<Integer> idsActivos;
Set<Persona> personasAutorizadas;
Map<String, Usuario> usuariosPorEmail;
```
:::{tip} Para colecciones, el plural ya sugiere que es una colección múltiple.
Agregá contexto adicional si mejora la claridad, pero evitá redundancia con el
tipo. 
:::

#### Variables temporales

Incluso variables temporales deben describir su propósito, no su tipo:

```java
// ❌ Incorrecto
String tempString = calcular();
int tmpInt = 0;
List<Object> listTemp = new ArrayList<>();

// ✅ Correcto
String resultadoTemporal = calcular();
int contadorTemporal = 0;
List<Object> elementosAuxiliares = new ArrayList<>();

// ✅ Mejor aún - sin "temporal" si el contexto es obvio
String resultado = calcular();
int contador = 0;
List<Object> elementos = new ArrayList<>();
```

### Por qué evitar la notación húngara

#### Problema 1: Redundancia visual

```java
// ❌ Redundancia obvia
String strNombre = "Juan";  // Ya veo que es String
int intEdad = 25;           // Ya veo que es int
boolean boolActivo = true;  // Ya veo que es boolean
```

#### Problema 2: Mantenimiento

```java
// Si cambio el tipo, ¿también cambio el nombre?
List<String> listNombres = new ArrayList<>();

// Después quiero usar un Set
Set<String> listNombres = new HashSet<>();  // ❌ El nombre miente!

// Mejor sin el tipo en el nombre
List<String> nombres = new ArrayList<>();
// Cambio fácil sin inconsistencia
Set<String> nombres = new HashSet<>();  // ✅ El nombre sigue siendo correcto
```

#### Problema 3: Legibilidad reducida

```java
// ❌ Difícil de leer - enfoque en tipos
if (intEdad >= intEdadMinima && strNombre != null) {
    listEstudiantes.add(new Estudiante(strNombre, intEdad));
}

// ✅ Fácil de leer - enfoque en propósito
if (edad >= edadMinima && nombre != null) {
    estudiantes.add(new Estudiante(nombre, edad));
}
```

### Excepciones históricas en APIs

Algunas APIs antiguas de Java incluyen tipos en nombres por razones históricas,
pero **no debés imitarlas**:

```java
// Excepciones históricas de Java (NO imitar)
StringBuilder sb = new StringBuilder();  // Tipo en el nombre de clase
StringBuffer buffer = new StringBuffer();

// En tu código, evitá esto
// ❌ No hagas:
String strBuilder = "...";
int intBuffer = 0;

// ✅ Hacé:
String construccionMensaje = "...";
int tamanioBuffer = 0;
```
:::{warning} Solo porque una API de Java o biblioteca externa use notación con
tipos, no significa que debas hacerlo en tu código. Mantené la consistencia con
las convenciones modernas de Java. 
:::

### Herramientas del IDE

Los IDEs modernos hacen innecesaria la notación húngara:

- **Hover sobre variable**: Muestra tipo instantáneamente
- **Autocompletado**: Sugiere métodos según el tipo
- **Navegación**: Salta a la declaración con un clic
- **Inlay hints**: Muestra tipos inferidos inline
:::{tip} Si sentís que necesitás el tipo en el nombre para entender el código,
probablemente el problema es que el IDE no está configurado adecuadamente o el
nombre no es suficientemente descriptivo. 
:::

(regla-generales-0x0009)=
## `0x0009` - Un espacio antes y después de los operadores

### Explicación

Todos los operadores binarios (aritméticos, lógicos, de comparación, de
asignación) deben estar rodeados por exactamente un espacio en cada lado. Esta
regla mejora la legibilidad y es fundamental para la consistencia visual del
código.

### Justificación

1. **Legibilidad**: Los espacios actúan como separadores visuales que facilitan
   la comprensión.
2. **Precedencia de operadores**: Hace más evidente el orden de evaluación.
3. **Consistencia**: Código uniforme es más fácil de revisar y mantener.
4. **Autoformateador**: Esta regla incentiva el uso de herramientas de formateo
   automático del IDE.
5. **Estándar de la industria**: Prácticamente todas las guías de estilo de Java
   lo requieren.

### Operadores afectados

#### Operadores aritméticos

```java
// Incorrecto ❌
int resultado = a+b*c-d/e;
double promedio = suma/cantidad;
int incremento = x+1;
int decremento = y-1;

// Correcto ✅
int resultado = a + b * c - d / e;
double promedio = suma / cantidad;
int incremento = x + 1;
int decremento = y - 1;
```

#### Operadores de comparación

```java
// Incorrecto ❌
if (x>0 && y<10) { }
if (nombre==null || edad>=18) { }
if (valor!=0) { }

// Correcto ✅
if (x > 0 && y < 10) { }
if (nombre == null || edad >= 18) { }
if (valor != 0) { }
```

#### Operadores lógicos

```java
// Incorrecto ❌
boolean resultado = a&&b||c;
boolean negacion = !a&&b;
if (esValido&&!estaVacio) { }

// Correcto ✅
boolean resultado = a && b || c;
boolean negacion = !a && b;  // ! es unario, no lleva espacio antes
if (esValido && !estaVacio) { }
```

#### Operadores de asignación

```java
// Incorrecto ❌
int x=10;
double y=x*2;
contador+=5;
suma*=factor;

// Correcto ✅
int x = 10;
double y = x * 2;
contador += 5;
suma *= factor;
```

#### Operadores ternarios

```java
// Incorrecto ❌
String resultado = x>0?"positivo":"negativo";
int valor = a==b?1:0;

// Correcto ✅
String resultado = x > 0 ? "positivo" : "negativo";
int valor = a == b ? 1 : 0;

// Para expresiones complejas, considerar multilínea
String resultado = condicionLarga && otraCondicion
    ? "valor verdadero"
    : "valor falso";
```

### Excepciones: Operadores unarios

Los operadores unarios (que actúan sobre un solo operando) **no** llevan
espacio:

```java
// Correcto ✅ - Operadores unarios pegados al operando
int negativo = -x;       // Sin espacio después de -
boolean negacion = !estaActivo;  // Sin espacio después de !
contador++;              // Sin espacios
--indice;                // Sin espacios

// Incorrecto ❌
int negativo = - x;      // Espacio innecesario
boolean negacion = ! estaActivo;  // Espacio innecesario
contador ++;             // Espacio innecesario
-- indice;               // Espacio innecesario
```

### Casos especiales

#### Expresiones matemáticas complejas

Los espacios ayudan a visualizar la precedencia:

```java
// Sin espacios ❌ - Difícil de leer
double resultado = a*b+c*d-e/f;

// Con espacios ✅ - Clara precedencia
double resultado = a * b + c * d - e / f;

// Para expresiones muy complejas, considerar paréntesis y variables intermedias
double terminoA = a * b;
double terminoB = c * d;
double terminoC = e / f;
double resultado = terminoA + terminoB - terminoC;
```

#### Strings y concatenación

```java
// Incorrecto ❌
String mensaje = "Hola "+nombre+" bienvenido";
String ruta = directorio+"/"+archivo;

// Correcto ✅
String mensaje = "Hola " + nombre + " bienvenido";
String ruta = directorio + "/" + archivo;

// Mejor aún ✅ - Para concatenaciones complejas
String mensaje = String.format("Hola %s bienvenido", nombre);
String ruta = String.join("/", directorio, archivo);
```

#### Llamadas a métodos y acceso a campos

```java
// Correcto ✅ - Sin espacios en acceso
objeto.metodo()
array[indice]
lista.get(i)

// Incorrecto ❌
objeto . metodo()  // Sin espacios alrededor de .
array [indice]     // Sin espacio antes de [
lista . get(i)     // Sin espacios alrededor de .
```

### Autoformateador del IDE

Esta regla existe principalmente para incentivar el uso del autoformateador:
:::{important} **Herramientas de autoformato:**

- **IntelliJ IDEA**: `Ctrl + Alt + L` (Windows/Linux) o `Cmd + Option + L` (Mac)
- **Eclipse**: `Ctrl + Shift + F`
- **VS Code**: `Shift + Alt + F`

Configurá estos atajos y usá el autoformato antes de cada commit. Esto garantiza
espaciado consistente sin esfuerzo manual. 
:::

### Configuración recomendada

```java
// Antes del autoformato ❌
public class Ejemplo{
    private int x=0;
    public int calcular(int a,int b){
        return a+b*x-5;
    }
}

// Después del autoformato ✅
public class Ejemplo {
    private int x = 0;

    public int calcular(int a, int b) {
        return a + b * x - 5;
    }
}
```

### Impacto en expresiones complejas

```java
// ❌ Sin espacios - Difícil de analizar visualmente
if(usuario.esActivo()&&!usuario.estaBloqueado()&&usuario.tienePermiso(EDITAR)||usuario.esAdministrador()){
    double resultado=(a+b)*(c-d)/e+f*g;
}

// ✅ Con espacios - Fácil de leer y entender
if (usuario.esActivo() && !usuario.estaBloqueado() &&
    usuario.tienePermiso(EDITAR) || usuario.esAdministrador()) {

    double resultado = (a + b) * (c - d) / e + f * g;
}
```
:::{tip} Aunque el cambio parezca mínimo, los espacios consistentes reducen
significativamente la carga cognitiva al leer código complejo. En expresiones
largas, la diferencia es dramática. 
:::

(regla-0x000A)=
## `0x000A` - No apilen líneas

### Explicación

Esta regla prohíbe dos prácticas comunes pero problemáticas: (1) colocar bloques
de código en una sola línea sin llaves, y (2) encadenar demasiadas llamadas a
métodos en una sola expresión. Ambas dificultan la lectura y el debugging del
código.

### Justificación

1. **Legibilidad**: Código vertical es más fácil de escanear que código
   horizontal comprimido.
2. **Debugging**: Es más difícil colocar breakpoints en código apilado.
3. **Control de versiones**: Los diffs son más claros con una instrucción por
   línea.
4. **Prevención de errores**: Los bloques sin llaves son propensos a errores al
   agregar líneas.
5. **Mantenibilidad**: Facilita agregar logging o modificar la lógica.

### Problema 1: Bloques sin llaves

#### Incorrecto ❌

```java
// Una línea sin llaves
if (x > 0) return x;

// Múltiples sentencias en una línea
if (encontrado) procesarDatos(); else mostrarError();

// For de una línea
for (int i = 0; i < 10; i++) suma += valores[i];

// While de una línea
while (hayMas()) procesar();
```

#### Correcto ✅

```java
// Siempre usar llaves, incluso para una sola instrucción
if (x > 0) {
    return x;
}

// Bloques separados y claros
if (encontrado) {
    procesarDatos();
} else {
    mostrarError();
}

// For con llaves
for (int i = 0; i < 10; i++) {
    suma += valores[i];
}

// While con llaves
while (hayMas()) {
    procesar();
}
```

#### ¿Por qué siempre llaves?

El problema clásico de los bloques sin llaves:

```java
// Código original ❌
if (condicion)
    instruccion1();

// Alguien agrega una línea sin darse cuenta
if (condicion)
    instruccion1();
    instruccion2();  // ❌ SIEMPRE se ejecuta, no está en el if!

// Con llaves ✅ - Error imposible
if (condicion) {
    instruccion1();
    instruccion2();  // ✅ Claramente dentro del bloque
}
```
:::{warning} **Apple's goto fail bug**: Un bug de seguridad crítico en iOS/OSS
fue causado por no usar llaves en un `if`. Una línea adicional quedó fuera del
bloque condicional por error, comprometiendo la validación SSL.

```c
if (error)
    goto fail;
    goto fail;  // ❌ Esta línea SIEMPRE se ejecuta!
```

Siempre usá llaves, incluso para una sola instrucción. 
:::

### Problema 2: Encadenamiento excesivo (Method Chaining)

#### Incorrecto ❌

```java
// Cadena demasiado larga - Difícil de leer y debuggear
String resultado = objeto.getParte1().getParte2().getParte3().getParte4().getValor();

// Imposible colocar breakpoint en parte específica
Usuario usuario = sistema.getModulo().getGestor().buscarUsuario(id).validar().normalizar();

// Cadena larga con operaciones
double total = pedido.getItems().stream().filter(i -> i.esActivo())
    .map(Item::getPrecio).reduce(0.0, Double::sum);
```

#### Correcto ✅

```java
// Descomponer en pasos intermedios - Fácil de debuggear
String parte2 = objeto.getParte1().getParte2();
String parte3 = parte2.getParte3();
String parte4 = parte3.getParte4();
String resultado = parte4.getValor();

// O si la cadena es corta (máximo 2-3 llamadas)
String parte3 = objeto.getParte1().getParte2().getParte3();
String resultado = parte3.getParte4().getValor();

// Para cadenas inevitables, usar líneas múltiples
Usuario usuario = sistema.getModulo()
    .getGestor()
    .buscarUsuario(id)
    .validar()
    .normalizar();

// Streams: una operación por línea
double total = pedido.getItems()
    .stream()
    .filter(Item::esActivo)
    .map(Item::getPrecio)
    .reduce(0.0, Double::sum);
```

### Regla de oro: Máximo 2-3 llamadas encadenadas

```java
// ✅ Aceptable - 2 llamadas
String nombre = usuario.getPerfil().getNombre();

// ✅ Aceptable - 3 llamadas en contexto claro
int longitud = texto.trim().toLowerCase().length();

// ❌ Demasiadas - 4+ llamadas
String dato = sistema.getConfig().getSeccion("db").getParametro("url").toString();

// ✅ Refactorizado
Configuracion config = sistema.getConfig();
SeccionConfig seccionDb = config.getSeccion("db");
Parametro parametroUrl = seccionDb.getParametro("url");
String dato = parametroUrl.toString();
```

### Beneficios del debugging

```java
// ❌ Difícil de debuggear
String valor = obj.getA().getB().getC().getD();
// Si esto lanza NullPointerException, ¿cuál método retornó null?

// ✅ Fácil de debuggear
A parteA = obj.getA();           // Breakpoint aquí
B parteB = parteA.getB();        // Breakpoint aquí
C parteC = parteB.getC();        // Breakpoint aquí
String valor = parteC.getD();    // Breakpoint aquí
// Ahora es trivial encontrar dónde está el null
```

### Fluent APIs y Builder Pattern

Excepción aceptable: APIs fluidas diseñadas específicamente para encadenamiento,
pero siempre en múltiples líneas:

```java
// ✅ Aceptable - Builder pattern con una llamada por línea
Persona persona = new PersonaBuilder()
    .nombre("Juan")
    .apellido("Pérez")
    .edad(30)
    .email("juan@example.com")
    .build();

// ✅ Aceptable - Stream API bien formateado
List<String> nombres = estudiantes.stream()
    .filter(e -> e.estaActivo())
    .map(Estudiante::getNombre)
    .sorted()
    .collect(Collectors.toList());

// ❌ Incorrecto - Todo en una línea
Persona p = new PersonaBuilder().nombre("Juan").apellido("Pérez").edad(30).build();
```
:::{note} Las Fluent APIs están diseñadas para encadenamiento, pero deben
formatearse con una llamada por línea para mantener legibilidad. 
:::

### Múltiples sentencias por línea

```java
// ❌ Incorrecto - Múltiples sentencias
int x = 0; int y = 0; procesarAmbos(x, y);

// ❌ Incorrecto - Declaraciones múltiples
String nombre = "Juan", apellido = "Pérez", email = "juan@mail.com";

// ✅ Correcto - Una sentencia por línea
int x = 0;
int y = 0;
procesarAmbos(x, y);

// ✅ Correcto - Una declaración por línea
String nombre = "Juan";
String apellido = "Pérez";
String email = "juan@mail.com";
```
:::{tip} **Regla práctica**: Si necesitás scroll horizontal para leer una línea,
probablemente está apiilada. El código debe caber cómodamente en la pantalla sin
desplazamiento horizontal. 
:::

(regla-0x000B)=
## `0x000B` - No hacer `import paquete.*`, solo traer lo que se necesita

### Explicación

Los imports deben ser explícitos, importando cada clase individualmente en lugar
de usar el wildcard `*` para importar todo un paquete. Esto hace que las
dependencias del código sean claras y previene conflictos.

### Justificación

1. **Claridad de dependencias**: Es inmediatamente obvio qué clases se están
   usando.
2. **Prevención de conflictos**: Evita ambigüedades cuando dos paquetes tienen
   clases con el mismo nombre.
3. **Rendimiento de compilación**: El compilador no necesita escanear todo el
   paquete.
4. **Detección de código muerto**: Facilita identificar imports no utilizados.
5. **Control de versiones**: Los diffs muestran exactamente qué dependencias se
   agregaron o removieron.
6. **Documentación implícita**: Los imports actúan como índice de las clases
   usadas.

### Ejemplos

#### Incorrecto ❌

```java
import java.util.*;
import java.io.*;
import java.awt.*;

public class Ejemplo {
    private List<String> nombres = new ArrayList<>();
    private Map<Integer, String> datos = new HashMap<>();
    // ¿Qué otras clases de java.util estoy importando innecesariamente?
}
```

#### Correcto ✅

```java
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Ejemplo {
    private List<String> nombres = new ArrayList<>();
    private Map<Integer, String> datos = new HashMap<>();
    // Claro: solo uso List, ArrayList, Map, HashMap
}
```

### Problema: Conflictos de nombres

#### Escenario de conflicto

```java
// ❌ Imports con wildcard - Ambigüedad
import java.util.*;
import java.awt.*;

public class Ventana {
    // ¿Cuál List? ¿java.util.List o java.awt.List?
    private List elementos;  // ERROR: Ambiguo!
}
```

```java
// ✅ Imports explícitos - Sin ambigüedad
import java.util.List;
import java.util.ArrayList;
import java.awt.Window;
import java.awt.Frame;

public class Ventana {
    private List<String> elementos;  // ✅ Claro: java.util.List
    private Frame marco;              // ✅ Claro: java.awt.Frame
}
```

#### Cuando hay conflicto inevitable

Si realmente necesitás dos clases con el mismo nombre:

```java
// ✅ Importar una, usar fully qualified name para la otra
import java.util.Date;

public class Ejemplo {
    private Date fechaUtil;                    // java.util.Date
    private java.sql.Date fechaSql;            // java.sql.Date (fully qualified)

    public void procesar() {
        fechaUtil = new Date();
        fechaSql = new java.sql.Date(System.currentTimeMillis());
    }
}
```

### Organización de imports

Los IDEs organizan automáticamente los imports según estas reglas:

```java
// ✅ Orden estándar de imports
// 1. Imports estáticos
import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

// 2. java.*
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 3. javax.*
import javax.swing.JFrame;

// 4. org.*
import org.junit.Test;

// 5. com.*
import com.empresa.proyecto.Utilidades;

// Línea en blanco antes de la clase
public class MiClase {
    // ...
}
```

### Herramientas del IDE

#### Optimizar imports automáticamente

Todos los IDEs pueden optimizar imports:

- **IntelliJ IDEA**: `Ctrl + Alt + O` (Optimize Imports)
- **Eclipse**: `Ctrl + Shift + O` (Organize Imports)
- **VS Code**: Comando "Organize Imports"
:::{important} Configurá tu IDE para:

1. **Optimizar imports al guardar** (elimina no usados, ordena, no usa
   wildcards)
2. **Nunca usar wildcards** en la configuración de formato
3. **Importar automáticamente** al escribir código nuevo

**IntelliJ**: `Settings > Editor > General > Auto Import`

- ☑ Optimize imports on the fly
- ☑ Add unambiguous imports on the fly
- Imports threshold: `999` (para que nunca use `*`) 
:::

### Detección de código no usado

```java
// Con wildcard ❌ - Imposible saber qué se usa realmente
import java.util.*;

public class Ejemplo {
    private List<String> nombres = new ArrayList<>();
    // ¿Uso algo más de java.util? No es evidente
}

// Con imports explícitos ✅ - Claro qué se usa
import java.util.List;
import java.util.ArrayList;
import java.util.Map;  // ← IDE marcará esto como "no usado"

public class Ejemplo {
    private List<String> nombres = new ArrayList<>();
    // Claro: Map no se usa, puede eliminarse
}
```

### Excepciones muy específicas

#### Imports estáticos de constantes

Para imports estáticos de muchas constantes de la misma clase, el wildcard puede
ser aceptable:

```java
// Aceptable en casos específicos ⚠️
import static java.lang.Math.*;

public class CalculosGeometricos {
    public double calcularDistancia(double x, double y) {
        return sqrt(pow(x, 2) + pow(y, 2));  // Usa múltiples métodos de Math
    }

    public double calcularArea(double radio) {
        return PI * pow(radio, 2);
    }
}

// Pero aún mejor ✅ - Explícito
import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class CalculosGeometricos {
    // Mismo código, pero claro qué métodos de Math se usan
}
```
:::{warning} Incluso para imports estáticos, preferí ser explícito. Solo
considerá wildcards si importás 5+ elementos del mismo paquete y todos son
realmente necesarios. 
:::

### Impacto en diffs de Git

```diff
# ❌ Con wildcard - No se ve qué cambió
  import java.util.*;

# ✅ Con imports explícitos - Cambio claro
  import java.util.List;
  import java.util.ArrayList;
+ import java.util.Set;
+ import java.util.HashSet;
```
:::{tip} Los imports explícitos hacen que los code reviews sean más
informativos, porque es inmediatamente visible qué nuevas dependencias se
agregaron. 
:::

(regla-0x000C)=
## `0x000C` - Los números mágicos deben convertirse en constantes nombradas

### Explicación

Un "número mágico" es un valor literal numérico que aparece directamente en el
código sin explicación de su significado. Estos números deben extraerse como
constantes con nombres descriptivos, excepto valores obvios como 0, 1, o -1 en
contextos claros.

### Justificación

1. **Autodocumentación**: El nombre de la constante explica qué representa el
   número.
2. **Mantenibilidad**: Cambiar el valor en un solo lugar en vez de buscar y
   reemplazar.
3. **Prevención de errores**: Evita typos al escribir el mismo número múltiples
   veces.
4. **Legibilidad**: `edad >= EDAD_MINIMA_LEGAL` es más claro que `edad >= 18`.
5. **Contexto**: El nombre proporciona contexto que el número solo no tiene.

### ¿Qué es un número mágico?

```java
// ❌ Números mágicos - ¿Qué significan?
if (edad >= 18) { ... }              // ¿Por qué 18?
double area = radio * radio * 3.14159;  // ¿Por qué este valor específico?
for (int i = 0; i < 100; i++) { ... }   // ¿Por qué 100?
Thread.sleep(5000);                  // ¿Por qué 5000?
if (intentos > 3) { ... }            // ¿Por qué 3?
```

### Ejemplos

#### Incorrecto ❌

```java
public class SistemaAutenticacion {
    public boolean autenticar(String usuario, String contrasena) {
        int intentos = contarIntentos(usuario);

        if (intentos > 3) {  // ❌ ¿Por qué 3?
            bloquearCuenta(usuario, 86400);  // ❌ ¿Qué es 86400?
            return false;
        }

        if (contrasena.length() < 8) {  // ❌ ¿Por qué 8?
            return false;
        }

        return verificarHash(usuario, contrasena);
    }

    public double calcularDescuento(double monto) {
        if (monto > 1000) {  // ❌ ¿Por qué 1000?
            return monto * 0.15;  // ❌ ¿Qué es 0.15?
        }
        return monto * 0.05;  // ❌ ¿Qué es 0.05?
    }
}
```

#### Correcto ✅

```java
public class SistemaAutenticacion {
    private static final int MAX_INTENTOS_FALLIDOS = 3;
    private static final int DURACION_BLOQUEO_SEGUNDOS = 86400;  // 24 horas
    private static final int LONGITUD_MINIMA_CONTRASENA = 8;

    private static final double MONTO_DESCUENTO_PREMIUM = 1000.0;
    private static final double DESCUENTO_PREMIUM = 0.15;  // 15%
    private static final double DESCUENTO_ESTANDAR = 0.05;  // 5%

    public boolean autenticar(String usuario, String contrasena) {
        int intentos = contarIntentos(usuario);

        if (intentos > MAX_INTENTOS_FALLIDOS) {
            bloquearCuenta(usuario, DURACION_BLOQUEO_SEGUNDOS);
            return false;
        }

        if (contrasena.length() < LONGITUD_MINIMA_CONTRASENA) {
            return false;
        }

        return verificarHash(usuario, contrasena);
    }

    public double calcularDescuento(double monto) {
        if (monto > MONTO_DESCUENTO_PREMIUM) {
            return monto * DESCUENTO_PREMIUM;
        }
        return monto * DESCUENTO_ESTANDAR;
    }
}
```

### Excepciones: Números NO mágicos

#### Contextos obvios donde 0, 1, -1 son aceptables

```java
// ✅ Aceptable - Contexto matemático/lógico obvio
int suma = 0;           // Inicialización neutral para suma
int producto = 1;       // Inicialización neutral para producto
int resultado = -1;     // Valor de "no encontrado" estándar

// ✅ Aceptable - Índices estándar
array[0]                // Primer elemento
lista.get(1)            // Segundo elemento
return -1;              // Convención de "no encontrado"

// ✅ Aceptable - Comparaciones estándar
if (valor == 0) { ... }     // Comparación con cero
if (contador > 0) { ... }   // Positivo
if (indice < 0) { ... }     // Negativo (inválido)

// ✅ Aceptable - Incrementos/Decrementos unitarios
contador++;
indice += 1;
valor -= 1;

// ✅ Aceptable - Multiplicadores triviales
doble = valor * 2;
mitad = valor / 2;
```

#### Números pequeños en contexto obvio

```java
// ✅ Aceptable - Números pequeños con contexto claro
int diasPorSemana = 7;
int mesesPorAnio = 12;
int horasPorDia = 24;

// Pero si se usa múltiples veces, mejor como constante
private static final int DIAS_POR_SEMANA = 7;
```

### Casos especiales

#### Números con significado de dominio

```java
public class ValidadorTarjetaCredito {
    // ❌ Número mágico
    if (digitosVerificadores % 10 == 0) { ... }

    // ✅ Con constante y comentario explicativo
    private static final int MODULO_LUHN = 10;

    // Algoritmo de Luhn para validación de tarjetas
    if (digitosVerificadores % MODULO_LUHN == 0) { ... }
}
```

#### Dimensiones y tamaños

```java
// ❌ Números mágicos en dimensiones
int[] buffer = new int[1024];
if (archivo.size() > 5242880) { ... }  // ¿Qué es este número?

// ✅ Con constantes descriptivas
private static final int TAMANIO_BUFFER = 1024;
private static final int MAX_TAMANIO_ARCHIVO = 5 * 1024 * 1024;  // 5 MB

int[] buffer = new int[TAMANIO_BUFFER];
if (archivo.size() > MAX_TAMANIO_ARCHIVO) { ... }
```

#### Tiempos y duraciones

```java
// ❌ Números mágicos en tiempos
Thread.sleep(5000);
cache.setTiempoVida(3600);
if (tiempoTranscurrido > 86400) { ... }

// ✅ Con constantes que documentan la unidad
private static final int ESPERA_INICIAL_MS = 5000;     // 5 segundos
private static final int TIEMPO_VIDA_CACHE_SEG = 3600; // 1 hora
private static final int SEGUNDOS_POR_DIA = 86400;     // 24 horas

Thread.sleep(ESPERA_INICIAL_MS);
cache.setTiempoVida(TIEMPO_VIDA_CACHE_SEG);
if (tiempoTranscurrido > SEGUNDOS_POR_DIA) { ... }

// ✅ Mejor aún - usar TimeUnit para claridad
private static final long ESPERA_INICIAL = TimeUnit.SECONDS.toMillis(5);
private static final long TIEMPO_VIDA_CACHE = TimeUnit.HOURS.toSeconds(1);
private static final long UN_DIA = TimeUnit.DAYS.toSeconds(1);
```

#### Porcentajes y tasas

```java
// ❌ Números mágicos
double descuento = precio * 0.15;
double iva = subtotal * 0.21;
double comision = monto * 0.03;

// ✅ Con constantes
private static final double TASA_DESCUENTO = 0.15;      // 15%
private static final double TASA_IVA = 0.21;            // 21%
private static final double TASA_COMISION = 0.03;       // 3%

double descuento = precio * TASA_DESCUENTO;
double iva = subtotal * TASA_IVA;
double comision = monto * TASA_COMISION;

// ✅ Alternativa - expresar como porcentaje entero
private static final double PORCENTAJE_DESCUENTO = 15.0;
private static final double DESCUENTO = PORCENTAJE_DESCUENTO / 100.0;
```

### Constantes matemáticas

```java
public class CalculosGeometricos {
    // ✅ Constantes matemáticas conocidas
    private static final double PI = 3.14159265359;
    private static final double E = 2.71828182846;
    private static final double PHI = 1.61803398875;  // Razón áurea

    // O mejor aún, usar las del JDK
    private static final double PI = Math.PI;
    private static final double E = Math.E;

    public double calcularAreaCirculo(double radio) {
        return PI * radio * radio;  // Claro qué es PI
    }
}
```

### Organización de constantes

#### Agrupación lógica

```java
public class ConfiguracionServidor {
    // Timeouts (en milisegundos)
    private static final int TIMEOUT_CONEXION_MS = 5000;
    private static final int TIMEOUT_LECTURA_MS = 10000;
    private static final int TIMEOUT_ESCRITURA_MS = 10000;

    // Límites de recursos
    private static final int MAX_CONEXIONES_SIMULTANEAS = 100;
    private static final int MAX_TAMANIO_COLA = 1000;
    private static final int MAX_HILOS_POOL = 50;

    // Reintentos
    private static final int MAX_REINTENTOS = 3;
    private static final int ESPERA_ENTRE_REINTENTOS_MS = 2000;
}
```

#### Clase de constantes dedicada

Para proyectos grandes, considerá una clase de constantes:

```java
public final class Constantes {
    // Constructor privado para prevenir instanciación
    private Constantes() {
        throw new AssertionError("Clase de constantes no instanciable");
    }

    // Constantes de la aplicación
    public static final int EDAD_MINIMA_REGISTRO = 18;
    public static final int LONGITUD_MAXIMA_NOMBRE = 100;
    public static final String FORMATO_FECHA_ESTANDAR = "dd/MM/yyyy";

    // Constantes de configuración
    public static final int TIMEOUT_DEFAULT_MS = 30000;
    public static final int MAX_INTENTOS_CONEXION = 5;
}
```
:::{note} Para proyectos muy grandes, es mejor tener clases de constantes por
módulo o dominio, en lugar de una única clase con todas las constantes. 
:::

### Expresiones calculadas vs. literales

```java
// ✅ Aceptable - Expresión calculada auto-documentada
private static final int BYTES_POR_MEGABYTE = 1024 * 1024;
private static final int SEGUNDOS_POR_DIA = 24 * 60 * 60;
private static final long MILISEGUNDOS_POR_HORA = 60 * 60 * 1000;

// Más claro que el literal directo
private static final int BYTES_POR_MEGABYTE = 1048576;  // ¿Qué es esto?
```

### Anti-patrón: Números mágicos disfrazados

```java
// ❌ Constante con nombre no descriptivo - Sigue siendo "mágica"
private static final int VALOR = 18;
private static final double NUMERO = 3.14159;
private static final int LIMITE = 100;

// ✅ Constante con nombre descriptivo
private static final int EDAD_MINIMA_LEGAL = 18;
private static final double PI = 3.14159;
private static final int MAX_ELEMENTOS_PAGINA = 100;
```
:::{warning} Declarar una constante no es suficiente. El **nombre** debe
explicar qué representa el número y en qué contexto se usa. 
:::

### Cuándo NO usar constantes

#### Valores triviales en contexto obvio

```java
// ✅ No requiere constante - Contexto obvio
String[] partes = texto.split(" ");
if (lista.size() == 0) { ... }      // Aunque isEmpty() es mejor
array[0] = valor;                   // Primer elemento
return lista.size() - 1;            // Último índice

// ❌ Constante innecesaria - Sobrecomplica
private static final int PRIMER_ELEMENTO = 0;
private static final int LISTA_VACIA = 0;

array[PRIMER_ELEMENTO] = valor;  // Overkill
if (lista.size() == LISTA_VACIA) { ... }  // Usar isEmpty()
```

#### Operaciones matemáticas estándar

```java
// ✅ Aceptable - Operaciones matemáticas obvias
int doble = valor * 2;
int mitad = valor / 2;
boolean esPar = numero % 2 == 0;
int cuadrado = lado * lado;

// ❌ Constante innecesaria
private static final int MULTIPLICADOR_DOBLE = 2;
int doble = valor * MULTIPLICADOR_DOBLE;  // Sobrecomplica
```

### Detección de números mágicos

#### Preguntas para identificarlos

Hacete estas preguntas sobre cada número en tu código:

1. **¿Qué representa este número?** Si no es inmediatamente obvio → Es mágico
2. **¿Se usa en múltiples lugares?** Si sí → Definitivamente debe ser constante
3. **¿Cambiaría en diferentes contextos?** Si sí → Debe ser constante o
   configurable
4. **¿Alguien sin contexto entendería su significado?** Si no → Es mágico

#### Ejemplos de evaluación

```java
// ¿Es mágico?
if (temperatura > 100) { ... }
// Pregunta: ¿Por qué 100? ¿Celsius? ¿Fahrenheit? ¿Kelvin?
// Respuesta: ❌ SÍ es mágico

// ✅ Solución
private static final double TEMP_EBULLICION_CELSIUS = 100.0;
if (temperatura > TEMP_EBULLICION_CELSIUS) { ... }
```

```java
// ¿Es mágico?
for (int i = 0; i < array.length; i++) { ... }
// Pregunta: ¿Son 0 y 1 mágicos aquí?
// Respuesta: ❌ NO son mágicos, contexto obvio (iteración completa)
```

### Constantes con comentarios explicativos

Para números complejos, agregar comentarios:

```java
// ✅ Constante + comentario explica el origen
private static final int MAX_CONEXIONES_TCP = 65535;  // Límite de puertos TCP/IP

private static final double GRAVEDAD_TIERRA = 9.80665;  // m/s² (estándar CGPM)

// Días hasta advertencia de expiración
private static final int DIAS_ADVERTENCIA_LICENCIA = 30;

// Factor de conversión de Fahrenheit a Celsius
private static final double FACTOR_F_TO_C = 5.0 / 9.0;
```

### Números mágicos en tests

Los tests también deben evitar números mágicos:

```java
// ❌ Test con números mágicos
@Test
public void testDescuento() {
    double resultado = calcularDescuento(1500);
    assertEquals(225.0, resultado, 0.01);  // ¿De dónde sale 225?
}

// ✅ Test con constantes descriptivas
@Test
public void testDescuentoPremium() {
    double montoCompra = 1500.0;
    double descuentoEsperado = montoCompra * 0.15;  // 15% para monto > 1000

    double resultado = calcularDescuento(montoCompra);

    assertEquals(descuentoEsperado, resultado, 0.01);
}
```

### Configuración vs. Constantes

Para valores que pueden cambiar entre entornos, considerá configuración externa:

```java
// ❌ Hard-coded - Requiere recompilar para cambiar
private static final int MAX_CONEXIONES = 100;
private static final String URL_BASE = "http://localhost:8080";

// ✅ Configurable - Puede cambiar sin recompilar
public class Configuracion {
    private static final int MAX_CONEXIONES_DEFAULT = 100;
    private static final String URL_BASE_DEFAULT = "http://localhost:8080";

    public int getMaxConexiones() {
        return Integer.parseInt(
            System.getProperty("max.conexiones",
                              String.valueOf(MAX_CONEXIONES_DEFAULT))
        );
    }

    public String getUrlBase() {
        return System.getProperty("url.base", URL_BASE_DEFAULT);
    }
}
```
:::{tip} Si un "número mágico" puede variar entre entornos (desarrollo, testing,
producción), debería ser un parámetro de configuración en lugar de una constante
hard-coded. 
:::

### Refactoring: De mágicos a constantes

#### Proceso de refactorización

1. **Identificar** el número mágico
2. **Extraer** a constante con nombre descriptivo
3. **Reemplazar** todas las ocurrencias
4. **Verificar** que el código sigue funcionando

```java
// Antes ❌
public class Validador {
    public boolean esContrasenaSegura(String contrasena) {
        return contrasena.length() >= 8 &&
               contiene(contrasena, "[0-9]") &&
               contiene(contrasena, "[A-Z]");
    }

    public boolean esCodigoValido(String codigo) {
        return codigo.matches("[A-Z]{2}[0-9]{8}");
    }
}

// Después ✅
public class Validador {
    private static final int LONGITUD_MINIMA_CONTRASENA = 8;
    private static final String PATRON_TIENE_DIGITO = "[0-9]";
    private static final String PATRON_TIENE_MAYUSCULA = "[A-Z]";

    private static final int LONGITUD_CODIGO_LETRAS = 2;
    private static final int LONGITUD_CODIGO_DIGITOS = 8;
    private static final String PATRON_CODIGO =
        String.format("[A-Z]{%d}[0-9]{%d}",
                     LONGITUD_CODIGO_LETRAS,
                     LONGITUD_CODIGO_DIGITOS);

    public boolean esContrasenaSegura(String contrasena) {
        return contrasena.length() >= LONGITUD_MINIMA_CONTRASENA &&
               contiene(contrasena, PATRON_TIENE_DIGITO) &&
               contiene(contrasena, PATRON_TIENE_MAYUSCULA);
    }

    public boolean esCodigoValido(String codigo) {
        return codigo.matches(PATRON_CODIGO);
    }
}
```
:::{important} Cuando extraigas constantes, agrupalas lógicamente y considerá si
el significado es realmente claro. Una constante mal nombrada es tan
problemática como un número mágico. 
:::

(regla-0x000D)=
## `0x000D` - Los comentarios deben explicar el "por qué", no el "qué"

### Explicación

Los comentarios son una herramienta poderosa, pero mal utilizados pueden
**empeorar** la calidad del código en lugar de mejorarla. Un buen comentario
explica la **intención**, **razones** o **contexto** de una decisión que no es
evidente en el código mismo. Un mal comentario simplemente **repite** lo que el
código ya dice claramente.

El código debe ser **autoexplicativo** a través de nombres descriptivos y
estructura clara. Los comentarios solo son necesarios cuando la intención, el
razonamiento o las restricciones no pueden expresarse adecuadamente en el
código.

### Justificación

1. **Código autoexplicativo**: Los identificadores descriptivos eliminan la
   necesidad de comentarios básicos
2. **Mantenimiento**: Los comentarios que explican el "qué" se desactualizan
   cuando cambia el código
3. **Ruido visual**: Comentarios obvios distraen de la lógica real
4. **Valor agregado**: Los comentarios deben aportar información que el código
   no puede expresar
5. **Intención del programador**: El "por qué" preserva el conocimiento del
   diseñador original

### Ejemplos

#### Incorrecto ❌: Comentarios que describen "qué"

```java
// ❌ Comentarios obvios que no agregan valor
public class Cliente {
    // Incrementa i en 1
    i = i + 1;

    // Verifica si el nombre es nulo
    if (nombre == null) {
        // Lanza excepción
        throw new IllegalArgumentException();
    }

    // Crea un nuevo objeto ArrayList
    List<Producto> productos = new ArrayList<>();

    // Loop que itera sobre los productos
    for (Producto p : productos) {
        // Imprime el producto
        System.out.println(p);
    }

    // Retorna verdadero
    return true;
}
```

#### Correcto ✅: Comentarios que explican "por qué"

```java
public class Cliente {
    // No necesita comentario, el código es claro
    i = i + 1;

    // Validación temprana para evitar NullPointerException en comparaciones posteriores
    if (nombre == null) {
        throw new IllegalArgumentException("El nombre no puede ser nulo");
    }

    // No necesita comentario
    List<Producto> productos = new ArrayList<>();

    // Usamos búsqueda binaria porque la lista está ordenada (O(log n) vs O(n))
    int posicion = busquedaBinaria(lista, elemento);

    // El algoritmo de Luhn requiere sumar dígitos en posiciones pares
    // por separado de los impares antes de la verificación final
    int sumaPares = calcularSumaPosicionesPares(digitos);
    int sumaImpares = calcularSumaPosicionesImpares(digitos);

    // Retornamos true porque la especificación del cliente requiere
    // que clientes sin compras previas sean considerados activos por defecto
    return true;
}
```

### Cuándo Comentar: Guía Práctica

#### ✅ Comentá cuando:

1. **Algoritmos no obvios**:

   ```java
   // Floyd's cycle detection (tortoise and hare)
   // Detecta ciclos en O(n) tiempo y O(1) espacio
   Nodo lento = cabeza;
   Nodo rapido = cabeza;
   ```

2. **Decisiones de diseño**:

   ```java
   // Usamos TreeMap en lugar de HashMap porque necesitamos
   // mantener las claves ordenadas para reportes
   Map<String, Cliente> clientes = new TreeMap<>();
   ```

3. **Restricciones o requisitos del negocio**:

   ```java
   // Según normativa AFIP, el CUIT debe tener exactamente 11 dígitos
   if (cuit.length() != 11) {
       throw new IllegalArgumentException("CUIT inválido");
   }
   ```

4. **Optimizaciones no obvias**:

   ```java
   // Cache de resultados costosos. Se limpia cada hora
   // para evitar uso excesivo de memoria (requisito no funcional)
   private Map<String, Resultado> cache = new HashMap<>();
   ```

5. **Workarounds o hacks temporales**:

   ```java
   // TODO: Workaround temporal para bug en librería externa (issue #1234)
   // Remover cuando se actualice a versión 2.5.0
   Thread.sleep(100);  // Pausa necesaria para sincronización
   ```

6. **Referencias a especificaciones**:
   ```java
   // Implementación según RFC 2616, sección 3.6
   // https://tools.ietf.org/html/rfc2616#section-3.6
   String encodedValue = encodeChunked(data);
   ```

#### ❌ NO comentés cuando:

1. **El código es evidente**:

   ```java
   // ❌ NO: Suma 1 a contador
   contador = contador + 1;

   // ✅ SÍ: El código habla por sí mismo
   contador = contador + 1;
   ```

2. **El nombre lo explica**:

   ```java
   // ❌ NO: Verifica si el cliente está activo
   if (clienteEstaActivo()) {

   // ✅ SÍ: El nombre del método es suficiente
   if (clienteEstaActivo()) {
   ```

3. **Comentarios de "ruido"**:

   ```java
   // ❌ NO: Constructor
   public Cliente() {

   // ❌ NO: Getters y setters
   public String getNombre() {
       return nombre;
   }
   ```

### Alternativa: Refactorización en lugar de Comentarios

Muchas veces, un comentario indica que el código necesita refactorización:

#### Antes ❌: Comentario explica lógica compleja

```java
// Calcula el precio final aplicando descuento por volumen
// Si compra más de 100 unidades: 15% descuento
// Si compra más de 50 unidades: 10% descuento
// Si compra más de 20 unidades: 5% descuento
double precioFinal;
if (cantidad > 100) {
    precioFinal = precioBase * 0.85;
} else if (cantidad > 50) {
    precioFinal = precioBase * 0.90;
} else if (cantidad > 20) {
    precioFinal = precioBase * 0.95;
} else {
    precioFinal = precioBase;
}
```

#### Después ✅: Código autoexplicativo

```java
// ✅ El método y las constantes eliminan la necesidad de comentarios
private static final int UMBRAL_DESCUENTO_MAXIMO = 100;
private static final int UMBRAL_DESCUENTO_MEDIO = 50;
private static final int UMBRAL_DESCUENTO_MINIMO = 20;

private static final double DESCUENTO_MAXIMO = 0.15;
private static final double DESCUENTO_MEDIO = 0.10;
private static final double DESCUENTO_MINIMO = 0.05;

public double calcularPrecioConDescuentoPorVolumen(double precioBase, int cantidad) {
    double porcentajeDescuento = obtenerDescuentoPorVolumen(cantidad);
    return precioBase * (1 - porcentajeDescuento);
}

private double obtenerDescuentoPorVolumen(int cantidad) {
    if (cantidad > UMBRAL_DESCUENTO_MAXIMO) {
        return DESCUENTO_MAXIMO;
    }
    if (cantidad > UMBRAL_DESCUENTO_MEDIO) {
        return DESCUENTO_MEDIO;
    }
    if (cantidad > UMBRAL_DESCUENTO_MINIMO) {
        return DESCUENTO_MINIMO;
    }
    return 0.0;
}
```

### Comentarios vs Javadoc

**Los comentarios** (`//` o `/* */`) son para explicar **implementación
interna**:

```java
public double calcularDescuento(double monto) {
    // Aplicamos la fórmula de descuento exponencial porque
    // da mejores resultados en el análisis de fidelización (ver informe Q1-2024)
    return monto * Math.exp(-0.05 * diasDesdePrimeraCompra);
}
```

**Javadoc** (`/** */`) es para documentar **API pública** (qué hace, no cómo):

```java
/**
 * Calcula el descuento aplicable basado en el monto y la antigüedad del cliente.
 *
 * @param monto el monto base sobre el cual calcular el descuento
 * @return el descuento aplicable en la misma unidad que el monto
 * @throws IllegalArgumentException si el monto es negativo
 */
public double calcularDescuento(double monto) {
    // ...
}
```
:::{important} **Regla de oro**: Si necesitás un comentario para explicar
**qué** hace el código, probablemente necesitás **refactorizar** (extraer
método, renombrar variables, etc.). Los comentarios deben explicar **por qué**
se hace de esa forma. 
:::

### Tipos de Comentarios Útiles

#### 1. Contexto de negocio

```java
// La regla de negocio establece que usuarios premium
// tienen acceso sin límite de intentos durante el período de prueba
if (usuario.esPremium() && usuario.enPeriodoDePrueba()) {
    return Integer.MAX_VALUE;
}
```

#### 2. Limitaciones conocidas

```java
// LIMITACIÓN: Este algoritmo asume que los datos están ordenados
// Si se modifica para aceptar datos no ordenados, cambiar a búsqueda lineal
return busquedaBinaria(datos, objetivo);
```

#### 3. Explicación de magic numbers justificados

```java
// Timeout de 30 segundos basado en SLA del servicio externo (28s + 2s buffer)
private static final int TIMEOUT_MILISEGUNDOS = 30000;
```

#### 4. Explicación de código contra-intuitivo

```java
// Incrementamos primero porque el índice inicial es -1
// (representa "antes del primer elemento")
indice++;
Elemento actual = array[indice];
```
:::{warning} **Señales de advertencia** de comentarios malos:

- Comentario más largo que el código que describe
- Comentario que repite exactamente lo que dice el código
- Comentario que describe nombres de variables o métodos
- Comentario que se vuelve obsoleto apenas cambia el código

Si encontrás estos patrones, **refactorizá el código** en lugar de comentar. 
:::
:::{tip} **Test de calidad para comentarios**: Preguntate:

1. ¿Este comentario aporta información que no está en el código?
2. ¿Un programador competente lo necesitaría para entender la intención?
3. ¿Seguirá siendo válido si refactorizo el código?

Si la respuesta a alguna es "no", eliminá o mejorá el comentario. 
:::

### Referencias Relacionadas

- Ver {ref}`regla-0x0006` sobre identificadores descriptivos que eliminan
  necesidad de comentarios
- Ver {ref}`regla-0x1000` sobre formato Javadoc para documentación de API
- Ver {ref}`regla-0x1007` sobre comentarios TODO con contexto
- Ver {ref}`regla-0x200A` sobre métodos cortos que son más fáciles de entender
  sin comentarios
