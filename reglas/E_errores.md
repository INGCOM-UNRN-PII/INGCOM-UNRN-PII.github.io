---
title: 0xE - Errores Comunes
---

# Serie 0xE - Errores Comunes (Error-Prone)

(regla-0xE000)=
## `0xE000` - No es correcto concatenar `String` en un bucle

### Explicación

Concatenar cadenas dentro de un lazo usando el operador `+` es extremadamente ineficiente porque cada concatenación crea un nuevo objeto `String` inmutable. En un lazo de $n$ iteraciones, esto genera $n$ objetos temporales innecesarios. Debe usarse `StringBuilder` para construcción eficiente de strings.

### Justificación

1. **Performance**: `StringBuilder` es $O(n)$, concatenación es $O(n²)$.
2. **Memoria**: Evita crear objetos temporales que saturan el garbage collector.
3. **Escalabilidad**: La diferencia es dramática con grandes volúmenes de datos.
4. **Buena práctica**: Estándar en toda aplicación Java profesional.
5. **Inmutabilidad de String**: Cada `+` crea un nuevo objeto String.

### El problema: Inmutabilidad de String

```java
// Cada operación crea un NUEVO objeto String
String s1 = "Hola";
String s2 = s1 + " Mundo";  // Crea nuevo String, s1 no cambia
// s1 sigue siendo "Hola"
// s2 es "Hola Mundo"
```

### Anti-patrón: Concatenación en lazo ❌

```java
// ❌ INCORRECTO: Concatenación en lazo
public String construirListaNumeros(int n) {
    String resultado = "";
    
    for (int i = 0; i < n; i = i + 1) {
        resultado = resultado + i + ", ";  // ❌ Crea nuevo String cada vez
    }
    
    return resultado;
}

// Con n=1000:
// Iteración 0: crea String "0, "
// Iteración 1: crea String "0, 1, "
// Iteración 2: crea String "0, 1, 2, "
// ...
// Iteración 999: crea String de ~4000 caracteres
// TOTAL: 1000 objetos String creados (999 se descartan inmediatamente)
```

### Solución: StringBuilder ✅

```java
// ✅ CORRECTO: Usar StringBuilder
public String construirListaNumeros(int n) {
    StringBuilder sb = new StringBuilder();
    
    for (int i = 0; i < n; i = i + 1) {
        sb.append(i);
        sb.append(", ");
    }
    
    return sb.toString();
}

// Con n=1000:
// StringBuilder crece dinámicamente
// TOTAL: 1 StringBuilder, 1 String final
// Mucho más eficiente
```

### Comparación de performance

```java
// Benchmark simple
public class ComparacionPerformance {
    public static void main(String[] args) {
        int n = 10000;
        
        // Método 1: Concatenación (LENTO)
        long inicio1 = System.currentTimeMillis();
        String resultado1 = "";
        for (int i = 0; i < n; i = i + 1) {
            resultado1 = resultado1 + i;
        }
        long tiempo1 = System.currentTimeMillis() - inicio1;
        System.out.println("Concatenación: " + tiempo1 + "ms");
        // Resultado típico: 500-2000ms
        
        // Método 2: StringBuilder (RÁPIDO)
        long inicio2 = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i = i + 1) {
            sb.append(i);
        }
        String resultado2 = sb.toString();
        long tiempo2 = System.currentTimeMillis() - inicio2;
        System.out.println("StringBuilder: " + tiempo2 + "ms");
        // Resultado típico: 1-5ms
    }
}
```

### Casos comunes en el curso

#### Construir listado de elementos

```java
// ❌ Incorrecto
public String listarProductos(List<Producto> productos) {
    String lista = "";
    for (Producto p : productos) {
        lista = lista + p.getNombre() + ": $" + p.getPrecio() + "\n";  // ❌
    }
    return lista;
}

// ✅ Correcto
public String listarProductos(List<Producto> productos) {
    StringBuilder sb = new StringBuilder();
    for (Producto p : productos) {
        sb.append(p.getNombre());
        sb.append(": $");
        sb.append(p.getPrecio());
        sb.append("\n");
    }
    return sb.toString();
}
```

#### Construir formato CSV

```java
// ❌ Incorrecto
public String generarCSV(int[][] matriz) {
    String csv = "";
    for (int i = 0; i < matriz.length; i = i + 1) {
        for (int j = 0; j < matriz[i].length; j = j + 1) {
            csv = csv + matriz[i][j] + ",";  // ❌ Doble lazo = aún peor
        }
        csv = csv + "\n";
    }
    return csv;
}

// ✅ Correcto
public String generarCSV(int[][] matriz) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < matriz.length; i = i + 1) {
        for (int j = 0; j < matriz[i].length; j = j + 1) {
            sb.append(matriz[i][j]);
            sb.append(",");
        }
        sb.append("\n");
    }
    return sb.toString();
}
```

#### Unir palabras con separador

```java
// ❌ Incorrecto
public String unir(String[] palabras, String separador) {
    String resultado = "";
    for (int i = 0; i < palabras.length; i = i + 1) {
        resultado = resultado + palabras[i];  // ❌
        if (i < palabras.length - 1) {
            resultado = resultado + separador;  // ❌
        }
    }
    return resultado;
}

// ✅ Correcto
public String unir(String[] palabras, String separador) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < palabras.length; i = i + 1) {
        sb.append(palabras[i]);
        if (i < palabras.length - 1) {
            sb.append(separador);
        }
    }
    return sb.toString();
}

// ✅ Aún mejor: usar String.join() (Java 8+)
public String unir(String[] palabras, String separador) {
    return String.join(separador, palabras);
}
```

### Métodos útiles de StringBuilder

```java
StringBuilder sb = new StringBuilder();

// Agregar contenido
sb.append("texto");
sb.append(123);
sb.append(true);
sb.append('c');

// Insertar en posición
sb.insert(0, "inicio: ");

// Reemplazar
sb.replace(0, 5, "nuevo");

// Eliminar
sb.delete(0, 5);

// Obtener String final
String resultado = sb.toString();
```

### Cuándo NO usar StringBuilder

#### Concatenaciones simples fuera de lazos ✅

```java
// ✅ Correcto: concatenación simple sin lazo
String mensaje = "El resultado es: " + resultado;

// ✅ Correcto: pocas concatenaciones
String nombre = titulo + " " + nombre + " " + apellido;

// ✅ Correcto: construcción en una línea
String url = protocolo + "://" + servidor + ":" + puerto + "/" + ruta;
```

:::{note}
El compilador de Java optimiza automáticamente concatenaciones simples usando `StringBuilder` internamente. La regla aplica específicamente a **lazos**.
:::

#### Concatenación una sola vez

```java
// ✅ Aceptable: una sola concatenación por ejecución
public String formatear(String nombre, int edad) {
    return nombre + " tiene " + edad + " años";  // ✅ OK
}
```

### StringBuffer vs StringBuilder

```java
// StringBuilder: Rápido, no thread-safe (usar en este curso)
StringBuilder sb = new StringBuilder();

// StringBuffer: Más lento, thread-safe (para programación concurrente)
StringBuffer sbf = new StringBuffer();
```

:::{important}
En este curso, siempre usar `StringBuilder`. `StringBuffer` es para contextos multi-thread que no cubrimos.
:::

### Inicialización con capacidad

Para mejor performance cuando sabés el tamaño aproximado:

```java
// ✅ Óptimo: especificar capacidad inicial
public String construir Lista(int n) {
    // Evita redimensionamientos internos
    StringBuilder sb = new StringBuilder(n * 10);  // Estima capacidad
    
    for (int i = 0; i < n; i = i + 1) {
        sb.append(i);
    }
    
    return sb.toString();
}
```

### Impacto visual de la diferencia

```
Concatenación con +:
"a" + "b"        → crea String("ab")
"ab" + "c"       → crea String("abc")  
"abc" + "d"      → crea String("abcd")
...
Objetos creados: n-1 temporales + 1 final = n objetos

StringBuilder:
StringBuilder sb = new StringBuilder();
sb.append("a")   → modifica buffer interno
sb.append("b")   → modifica buffer interno
sb.append("c")   → modifica buffer interno
sb.toString()    → crea String final
Objetos creados: 1 StringBuilder + 1 String final = 2 objetos
```

:::{warning}
En lazos de miles de iteraciones, la diferencia puede ser de milisegundos vs. segundos. Siempre usá `StringBuilder` en lazos.
:::

(regla-0xE001)=
## `0xE001` - Comparar objetos con `==` en lugar de `equals()`

### Explicación

Uno de los errores más comunes y peligrosos en Java es usar el operador `==` para comparar objetos cuando en realidad se necesita comparar su contenido. El operador `==` compara **referencias de memoria** (identidad), mientras que el método `equals()` compara el **contenido lógico** de los objetos (igualdad).

### Justificación

1. **Semántica incorrecta**: `==` responde "¿son el mismo objeto en memoria?", mientras que `equals()` responde "¿tienen el mismo valor?"
2. **Comportamiento inesperado**: Dos objetos con el mismo contenido pueden tener referencias diferentes
3. **String pooling confunde**: Los literales String se optimizan, pero no los objetos creados dinámicamente
4. **Contrato de Object**: El método `equals()` está diseñado específicamente para comparación de valores

### Ejemplos

#### Incorrecto ❌

```java
String nombre1 = new String("Juan");
String nombre2 = new String("Juan");

if (nombre1 == nombre2) {  // ❌ false: referencias diferentes
    System.out.println("Son iguales");  // Nunca se ejecuta
}

// Con objetos personalizados
Persona p1 = new Persona("Ana", 25);
Persona p2 = new Persona("Ana", 25);

if (p1 == p2) {  // ❌ false: objetos diferentes en memoria
    System.out.println("Misma persona");  // Nunca se ejecuta
}

// En búsquedas
List<String> nombres = Arrays.asList("María", "Pedro", "Juan");
String buscado = new String("Juan");

if (nombres.contains(buscado)) {  // ✅ contains usa equals()
    // Se ejecuta correctamente
}

// Pero si hacemos búsqueda manual incorrecta:
boolean encontrado = false;
for (String nombre : nombres) {
    if (nombre == buscado) {  // ❌ Compara referencias
        encontrado = true;
        break;
    }
}
// encontrado será false incorrectamente
```

#### Correcto ✅

```java
String nombre1 = new String("Juan");
String nombre2 = new String("Juan");

if (nombre1.equals(nombre2)) {  // ✅ true: mismo contenido
    System.out.println("Son iguales");  // Se ejecuta
}

// Con objetos personalizados (requiere implementar equals)
Persona p1 = new Persona("Ana", 25);
Persona p2 = new Persona("Ana", 25);

if (p1.equals(p2)) {  // ✅ true si equals está bien implementado
    System.out.println("Misma persona");
}

// Búsqueda manual correcta
boolean encontrado = false;
for (String nombre : nombres) {
    if (nombre.equals(buscado)) {  // ✅ Compara contenido
        encontrado = true;
        break;
    }
}
```

### Casos Especiales: Cuándo SÍ usar `==`

#### 1. Comparación con `null`

```java
if (objeto == null) {  // ✅ Correcto y necesario
    throw new IllegalArgumentException("El objeto no puede ser null");
}

// ❌ objeto.equals(null) lanzaría NullPointerException si objeto es null
```

#### 2. Tipos primitivos

```java
int a = 5;
int b = 5;
if (a == b) {  // ✅ Correcto: primitivos no tienen equals()
    System.out.println("Iguales");
}

boolean activo = true;
if (activo == true) {  // ✅ Aunque preferible: if (activo)
    // ...
}
```

#### 3. Comparación de identidad intencional

```java
// Verificar que es exactamente la misma instancia (Singleton)
if (conexion == ConexionDB.getInstancia()) {  // ✅ Verificar identidad
    System.out.println("Es la instancia singleton");
}

// Verificar si dos variables apuntan al mismo objeto
Nodo nodo1 = lista.getPrimero();
Nodo nodo2 = lista.getUltimo();
if (nodo1 == nodo2) {  // ✅ Verificar si es el mismo nodo
    System.out.println("Lista con un solo elemento");
}
```

### El Truco del String Pool

```java
// Literales van al pool
String s1 = "Hola";      // Del pool de literales
String s2 = "Hola";      // Reutiliza del pool
String s3 = new String("Hola");  // Nuevo objeto en heap
String s4 = s3.intern(); // Obtiene referencia del pool

System.out.println(s1 == s2);      // true (mismo objeto del pool)
System.out.println(s1 == s3);      // false (diferentes objetos)
System.out.println(s1 == s4);      // true (intern() devuelve del pool)
System.out.println(s1.equals(s3)); // true (mismo contenido)
```

:::{warning}
**Nunca confíes en que `==` funcione con `String`** solo porque a veces da resultados correctos. El string pooling es una **optimización del compilador**, no una garantía semántica. La optimización puede no aplicarse en muchos casos (concatenación, lectura de archivos, entrada de usuario, etc.).

**Regla simple**: Para objetos, **siempre usá `equals()`** para comparar contenido.
:::

### Protección contra NullPointerException

#### Patrón Yoda Conditions

```java
// Incorrecto: vulnerable a NPE
if (valorRecibido.equals("esperado")) {  // ❌ NPE si valorRecibido es null
    // ...
}

// Correcto: Yoda condition
if ("esperado".equals(valorRecibido)) {  // ✅ Devuelve false si valorRecibido es null
    // ...
}
```

#### Usando Objects.equals() (Java 7+)

```java
import java.util.Objects;

// Maneja nulls de forma segura
if (Objects.equals(valor1, valor2)) {  // ✅ true solo si ambos son null o equals()
    // ...
}

// Equivalente a:
// (valor1 == valor2) || (valor1 != null && valor1.equals(valor2))
```

:::{tip}
Consultá las reglas {ref}`regla-0x2004` y {ref}`regla-0x2005` sobre cómo implementar correctamente `equals()` y `hashCode()` en tus clases.
:::

### Herramientas de Detección

Los IDEs modernos pueden detectar este error:

- **IntelliJ IDEA**: Inspección "String comparison using '==', instead of 'equals()'"
- **Eclipse**: Warning "Comparing identical expressions"
- **SpotBugs**: Bug pattern "RC_REF_COMPARISON"

(regla-0xE002)=
## `0xE002` - No cerrar recursos (archivos, conexiones, etc.)

### Explicación

Los recursos del sistema (archivos, conexiones de red, sockets, streams) deben cerrarse explícitamente cuando ya no se necesitan. Si no se cierran, pueden causar **fugas de recursos** (*resource leaks*) que eventualmente agotan los recursos del sistema operativo.

Java proporciona la construcción **try-with-resources** (desde Java 7) que garantiza el cierre automático de recursos, incluso si ocurren excepciones.

### Justificación

1. **Fugas de recursos**: Un archivo no cerrado mantiene un *file descriptor* abierto indefinidamente
2. **Límites del sistema**: Los sistemas operativos tienen límites en la cantidad de archivos/conexiones abiertas simultáneamente
3. **Bloqueos**: Archivos no cerrados pueden quedar bloqueados, impidiendo su modificación o eliminación
4. **Garantía de limpieza**: try-with-resources ejecuta el cierre incluso si hay excepciones

### Ejemplos

#### Incorrecto ❌

```java
public String leerArchivo(String ruta) throws IOException {
    FileReader reader = new FileReader(ruta);
    BufferedReader buffer = new BufferedReader(reader);
    
    String linea = buffer.readLine();
    // ❌ Si ocurre excepción aquí, nunca se cierra
    
    reader.close();  // ❌ No se ejecuta si hay excepción antes
    return linea;
}
```

#### Correcto ✅

```java
public String leerArchivo(String ruta) throws IOException {
    try (FileReader reader = new FileReader(ruta);
         BufferedReader buffer = new BufferedReader(reader)) {
        
        return buffer.readLine();
        // ✅ Se cierran automáticamente reader y buffer
        // incluso si readLine() lanza excepción
    }
}
```

### Try-with-resources: Características

#### Múltiples recursos

```java
try (FileInputStream input = new FileInputStream("entrada.txt");
     FileOutputStream output = new FileOutputStream("salida.txt")) {
    
    int dato;
    while ((dato = input.read()) != -1) {
        output.write(dato);
    }
    // ✅ Se cierran en orden inverso: output, luego input
}
```

#### Requisito: Implementar AutoCloseable

```java
public class MiRecurso implements AutoCloseable {
    @Override
    public void close() {
        // Liberar recursos aquí
        System.out.println("Recurso cerrado");
    }
}

// Uso
try (MiRecurso recurso = new MiRecurso()) {
    // usar recurso
}  // close() se llama automáticamente
```

### Patrón Clásico (Pre-Java 7) - NO RECOMENDADO

```java
// Patrón antiguo, solo para referencia histórica
FileReader reader = null;
try {
    reader = new FileReader("archivo.txt");
    // usar reader
} catch (IOException e) {
    // manejar error
} finally {
    if (reader != null) {
        try {
            reader.close();
        } catch (IOException e) {
            // ¿Qué hacer aquí?
        }
    }
}
```

:::{important}
**Siempre usá try-with-resources** para cualquier objeto que implemente `AutoCloseable` o `Closeable`. Es más seguro, más limpio y menos propenso a errores que el manejo manual en bloques `finally`.
:::

:::{note}
Recursos comunes que requieren cierre:
- Archivos: `FileReader`, `FileWriter`, `FileInputStream`, `FileOutputStream`
- Buffers: `BufferedReader`, `BufferedWriter`
- Conexiones: `Connection`, `Statement`, `ResultSet` (JDBC)
- Sockets: `Socket`, `ServerSocket`
- Scanners: `Scanner`
:::

(regla-0xE003)=
## `0xE003` - Modificar una colección mientras se itera sobre ella

### Explicación

Modificar una colección (agregar, eliminar o reemplazar elementos) mientras se itera sobre ella con un *enhanced for loop* causa `ConcurrentModificationException`. Este error de diseño es fácil de cometer pero tiene soluciones claras: usar un `Iterator` explícito para modificaciones seguras o iterar sobre una copia de la colección.

### Justificación

1. **Fail-fast iterator**: Las colecciones de Java detectan modificaciones estructurales durante la iteración y lanzan excepción inmediatamente
2. **Comportamiento indefinido**: Sin esta protección, los resultados serían impredecibles (elementos saltados, duplicados, etc.)
3. **Seguridad del diseño**: La excepción previene bugs sutiles que serían muy difíciles de debuggear
4. **Iterator es la solución**: La interfaz `Iterator` proporciona el método `remove()` diseñado específicamente para esta situación

### Ejemplos

#### Incorrecto ❌

```java
List<String> nombres = new ArrayList<>(Arrays.asList("Ana", "Alberto", "Carlos", "Andrea"));

// ❌ ConcurrentModificationException
for (String nombre : nombres) {
    if (nombre.startsWith("A")) {
        nombres.remove(nombre);  // ❌ Modifica durante iteración
    }
}

// ❌ También incorrecto con índices (comportamiento incorrecto)
for (int i = 0; i < nombres.size(); i++) {
    if (nombres.get(i).startsWith("A")) {
        nombres.remove(i);  // ❌ Salta elementos después de remover
        // Después de remover índice i, el elemento i+1 pasa a i
        // pero el loop incrementa i, saltándose ese elemento
    }
}
```

#### Correcto ✅

**Opción 1: Iterator explícito (recomendado para remover)**

```java
List<String> nombres = new ArrayList<>(Arrays.asList("Ana", "Alberto", "Carlos", "Andrea"));

Iterator<String> it = nombres.iterator();
while (it.hasNext()) {
    String nombre = it.next();
    if (nombre.startsWith("A")) {
        it.remove();  // ✅ Iterator.remove() es seguro
    }
}
// Resultado: ["Carlos"]
```

**Opción 2: Iterar sobre copia (para modificaciones complejas)**

```java
List<String> nombres = new ArrayList<>(Arrays.asList("Ana", "Alberto", "Carlos", "Andrea"));
List<String> copia = new ArrayList<>(nombres);

for (String nombre : copia) {
    if (nombre.startsWith("A")) {
        nombres.remove(nombre);  // ✅ Modifica la lista original, itera sobre copia
    }
}
```

**Opción 3: Recolectar y modificar después**

```java
List<String> nombres = new ArrayList<>(Arrays.asList("Ana", "Alberto", "Carlos", "Andrea"));
List<String> aRemover = new ArrayList<>();

for (String nombre : nombres) {
    if (nombre.startsWith("A")) {
        aRemover.add(nombre);  // ✅ Recolectar elementos a remover
    }
}

nombres.removeAll(aRemover);  // ✅ Remover en lote después de iterar
```

**Opción 4: Crear nueva colección**

```java
List<String> nombres = new ArrayList<>(Arrays.asList("Ana", "Alberto", "Carlos", "Andrea"));
List<String> filtrados = new ArrayList<>();

for (String nombre : nombres) {
    if (!nombre.startsWith("A")) {
        filtrados.add(nombre);  // ✅ Agregar solo los que queremos mantener
    }
}
// Ahora filtrados contiene ["Carlos"]
```

### Casos Especiales

#### Iterar en reversa con índices

```java
List<String> nombres = new ArrayList<>(Arrays.asList("Ana", "Alberto", "Carlos", "Andrea"));

// Iterar en reversa para evitar saltar elementos
for (int i = nombres.size() - 1; i >= 0; i--) {
    if (nombres.get(i).startsWith("A")) {
        nombres.remove(i);  // ✅ Funciona pero es menos elegante
    }
}
```

:::{note}
Aunque iterar en reversa funciona, **preferí usar `Iterator`** porque:
1. Es más idiomático en Java
2. Funciona con cualquier `Collection`, no solo `List`
3. El código expresa claramente la intención
:::

#### Agregar elementos durante iteración

```java
List<Integer> numeros = new ArrayList<>(Arrays.asList(1, 2, 3));

// ❌ Incorrecto: agregar durante iteración
for (Integer num : numeros) {
    if (num < 3) {
        numeros.add(num * 10);  // ❌ ConcurrentModificationException
    }
}

// ✅ Correcto: recolectar primero, agregar después
List<Integer> nuevos = new ArrayList<>();
for (Integer num : numeros) {
    if (num < 3) {
        nuevos.add(num * 10);
    }
}
numeros.addAll(nuevos);  // ✅ Agregar después de terminar la iteración
```

### Explicación Técnica

Las colecciones de Java mantienen un contador interno llamado `modCount` que se incrementa cada vez que la estructura cambia. El iterator guarda este valor y verifica en cada operación que no haya cambiado.

```java
// Pseudocódigo del comportamiento interno
class ArrayList {
    private int modCount = 0;
    
    public void remove(int index) {
        modCount++;  // Se incrementa en cada modificación
        // ...
    }
}

class ArrayListIterator {
    private int expectedModCount;
    
    public E next() {
        if (list.modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
        // ...
    }
}
```

:::{warning}
El enhanced for loop (`for (Tipo elemento : coleccion)`) usa un `Iterator` internamente, por lo que también está sujeto a esta restricción. **No podés modificar la colección** que estás iterando.
:::

:::{tip}
**Regla general**: Si necesitás modificar una colección durante la iteración:
1. **Primera opción**: Usá `Iterator.remove()` para remociones simples
2. **Segunda opción**: Recolectá elementos a modificar y hacé los cambios después
3. **Tercera opción**: Creá una nueva colección con los elementos deseados
:::

(regla-0xE004)=
## `0xE004` - Ignorar el valor de retorno de métodos inmutables

### Explicación

Muchas clases en Java son **inmutables** por diseño, lo que significa que sus métodos no modifican el objeto original, sino que **retornan un nuevo objeto** con el cambio aplicado. Si ignorás el valor de retorno, el método no tiene ningún efecto. Este es un error común con `String`, `BigDecimal`, `LocalDate` y otras clases inmutables.

### Justificación

1. **Diseño inmutable**: Las clases inmutables son thread-safe y más fáciles de razonar
2. **No hay efectos secundarios**: Los métodos de instancia no modifican el estado interno
3. **Valor de retorno obligatorio**: El resultado del método es el nuevo objeto con el cambio aplicado
4. **Contrato de la clase**: La documentación de clases inmutables siempre aclara que retornan un nuevo objeto

### Ejemplos

#### Incorrecto ❌

```java
// String es inmutable
String texto = "Hola Mundo";
texto.replace("Mundo", "Java");  // ❌ No hace nada, se descarta el resultado
System.out.println(texto);  // "Hola Mundo" - sin cambios

texto.toUpperCase();  // ❌ Se descarta el resultado
System.out.println(texto);  // "Hola Mundo" - sigue igual

texto.trim();  // ❌ Se descarta
System.out.println(texto);  // "Hola Mundo" - sin cambios

// BigDecimal también es inmutable
BigDecimal precio = new BigDecimal("100.00");
precio.multiply(new BigDecimal("1.21"));  // ❌ Resultado descartado
System.out.println(precio);  // "100.00" - sin cambios

// LocalDate es inmutable
LocalDate fecha = LocalDate.of(2024, 1, 15);
fecha.plusDays(10);  // ❌ Resultado descartado
System.out.println(fecha);  // "2024-01-15" - sin cambios
```

#### Correcto ✅

```java
// String: asignar el resultado
String texto = "Hola Mundo";
texto = texto.replace("Mundo", "Java");  // ✅ Asignar resultado
System.out.println(texto);  // "Hola Java"

texto = texto.toUpperCase();  // ✅ Asignar
System.out.println(texto);  // "HOLA JAVA"

texto = texto.trim();  // ✅ Asignar
System.out.println(texto);  // "HOLA JAVA"

// BigDecimal: usar el resultado
BigDecimal precio = new BigDecimal("100.00");
BigDecimal precioConIva = precio.multiply(new BigDecimal("1.21"));  // ✅
System.out.println(precioConIva);  // "121.00"

// LocalDate: guardar el resultado
LocalDate fecha = LocalDate.of(2024, 1, 15);
LocalDate fechaFutura = fecha.plusDays(10);  // ✅
System.out.println(fechaFutura);  // "2024-01-25"
```

### Clases Inmutables Comunes

#### En java.lang
- `String` - todos sus métodos retornan nuevos Strings
- `Integer`, `Double`, `Boolean` - wrappers inmutables de primitivos

#### En java.math
- `BigDecimal` - operaciones aritméticas retornan nuevas instancias
- `BigInteger` - lo mismo para enteros grandes

#### En java.time (Java 8+)
- `LocalDate`, `LocalTime`, `LocalDateTime`
- `ZonedDateTime`, `Instant`
- `Duration`, `Period`

#### En java.util
- `Collections.unmodifiableList()` y similares retornan vistas inmutables

### Encadenamiento de Métodos (Method Chaining)

Las clases inmutables facilitan el encadenamiento:

```java
String resultado = "  hola mundo  "
    .trim()                    // Retorna nuevo String sin espacios
    .toUpperCase()            // Retorna nuevo String en mayúsculas
    .replace("MUNDO", "JAVA"); // Retorna nuevo String con reemplazo
// resultado: "HOLA JAVA"

LocalDate fechaCompleja = LocalDate.now()
    .plusMonths(3)
    .minusDays(5)
    .withDayOfMonth(1);
```

:::{tip}
Si un método de una clase inmutable retorna el mismo tipo que la clase, **es casi seguro que debés usar el valor de retorno**. Los métodos `void` en clases inmutables son extremadamente raros.
:::

### Comparación: Mutable vs Inmutable

```java
// StringBuilder es MUTABLE
StringBuilder sb = new StringBuilder("Hola");
sb.append(" Mundo");  // ✅ Modifica el objeto, no necesita asignación
System.out.println(sb);  // "Hola Mundo"

// String es INMUTABLE
String s = "Hola";
s.concat(" Mundo");  // ❌ Se descarta el resultado
System.out.println(s);  // "Hola" - sin cambios

s = s.concat(" Mundo");  // ✅ Asignar resultado
System.out.println(s);  // "Hola Mundo"
```

:::{warning}
**Error sutil**: Muchos desarrolladores principiantes asumen que todos los métodos modifican el objeto. Las clases inmutables van en contra de esta intuición. **Siempre verificá** si una clase es inmutable consultando su documentación.
:::

### Detección en el IDE

Los IDEs modernos detectan este problema:

- **IntelliJ IDEA**: "Result of method call ignored"
- **Eclipse**: "The value of the local variable is not used"
- **SpotBugs**: Bug pattern "RV_RETURN_VALUE_IGNORED"

(regla-0xE005)=
## `0xE005` - Usar `float` o `double` para cálculos monetarios o de precisión

### Explicación

Los tipos `float` y `double` utilizan representación de **punto flotante binario** (IEEE 754), que no puede representar exactamente muchos valores decimales. Esto causa errores de redondeo acumulativos que son **inaceptables** en cálculos monetarios, financieros o científicos que requieren precisión decimal exacta.

Para valores monetarios o cálculos que requieren precisión decimal, **siempre usá `BigDecimal`** o representá valores en la unidad más pequeña como enteros (centavos).

### Justificación

1. **Errores de redondeo**: `0.1 + 0.2` no es exactamente `0.3` en punto flotante binario
2. **Acumulación de errores**: En múltiples operaciones, los errores se amplifican
3. **Implicaciones legales**: Errores en cálculos financieros pueden tener consecuencias legales
4. **Estándar de la industria**: Los sistemas financieros profesionales usan aritmética decimal

### Ejemplos

#### El Problema: Errores de Punto Flotante

```java
// ❌ Incorrecto: cálculos monetarios con double
double precio1 = 0.1;
double precio2 = 0.2;
double total = precio1 + precio2;

System.out.println(total);  // 0.30000000000000004 ❌
System.out.println(total == 0.3);  // false ❌

// ❌ Más problemas con float
float saldo = 1.0f;
for (int i = 0; i < 10; i++) {
    saldo -= 0.1f;
}
System.out.println(saldo);  // 0.09999999 en lugar de 0.0 ❌

// ❌ Problema en comparaciones
double descuento = 0.1 * 3;  // Debería ser 0.3
if (descuento == 0.3) {  // false debido a error de redondeo ❌
    aplicarDescuento();
}
```

#### Correcto ✅: Usando BigDecimal

```java
import java.math.BigDecimal;
import java.math.RoundingMode;

// ✅ Correcto: usar BigDecimal para dinero
BigDecimal precio1 = new BigDecimal("0.1");
BigDecimal precio2 = new BigDecimal("0.2");
BigDecimal total = precio1.add(precio2);

System.out.println(total);  // 0.3 ✅

// ✅ Operaciones complejas
BigDecimal subtotal = new BigDecimal("100.00");
BigDecimal iva = new BigDecimal("0.21");
BigDecimal recargo = new BigDecimal("5.50");

BigDecimal montoIva = subtotal.multiply(iva);
BigDecimal totalFinal = subtotal.add(montoIva).add(recargo);

// ✅ Control de precisión y redondeo
BigDecimal resultado = subtotal.divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
// Divide por 3, mantiene 2 decimales, redondea hacia arriba en .5
```

#### Correcto ✅: Usar Enteros (Centavos)

```java
// ✅ Alternativa: usar enteros para representar centavos
int precioEnCentavos1 = 10;  // $0.10
int precioEnCentavos2 = 20;  // $0.20
int totalEnCentavos = precioEnCentavos1 + precioEnCentavos2;  // 30 centavos

// Convertir a pesos para display
double pesosMostrar = totalEnCentavos / 100.0;  // 0.3
System.out.printf("Total: $%.2f%n", pesosMostrar);  // "Total: $0.30"
```

### BigDecimal: Buenas Prácticas

#### Constructor con String (NO con double)

```java
// ❌ Incorrecto: crear BigDecimal desde double
BigDecimal malo = new BigDecimal(0.1);
System.out.println(malo);  // 0.1000000000000000055511151231257827021181583404541015625

// ✅ Correcto: usar constructor con String
BigDecimal bueno = new BigDecimal("0.1");
System.out.println(bueno);  // 0.1

// ✅ Alternativa: usar valueOf para enteros o long
BigDecimal desde Entero = BigDecimal.valueOf(100);  // 100
BigDecimal desdeDouble = BigDecimal.valueOf(0.1);  // OK con valueOf
```

#### Especificar Escala y Redondeo

```java
BigDecimal precio = new BigDecimal("19.99");
BigDecimal cantidad = new BigDecimal("3");
BigDecimal subtotal = precio.multiply(cantidad);  // 59.97

// División requiere escala y modo de redondeo
BigDecimal descuento = new BigDecimal("0.15");
BigDecimal montoDescuento = subtotal.multiply(descuento)
    .setScale(2, RoundingMode.HALF_UP);  // ✅ 2 decimales, redondeo estándar

BigDecimal total = subtotal.subtract(montoDescuento);
```

#### Comparación de BigDecimal

```java
BigDecimal precio1 = new BigDecimal("10.00");
BigDecimal precio2 = new BigDecimal("10.0");

// ❌ equals() considera la escala
System.out.println(precio1.equals(precio2));  // false (diferente escala)

// ✅ compareTo() ignora la escala
System.out.println(precio1.compareTo(precio2) == 0);  // true
```

### Casos de Uso Apropiados

#### Cuándo SÍ usar float/double

```java
// ✅ Cálculos científicos aproximados
double radio = 5.0;
double area = Math.PI * radio * radio;

// ✅ Gráficos y coordenadas
float x = 10.5f;
float y = 20.3f;

// ✅ Mediciones físicas con margen de error aceptable
double temperatura = 36.5;
double velocidad = 299792458.0;  // velocidad de la luz en m/s
```

#### Cuándo usar BigDecimal

```java
// ✅ Cálculos monetarios
BigDecimal precio = new BigDecimal("19.99");
BigDecimal iva = precio.multiply(new BigDecimal("0.21"));

// ✅ Porcentajes en finanzas
BigDecimal tasaInteres = new BigDecimal("0.0525");  // 5.25%

// ✅ Cualquier cálculo donde la precisión decimal es crítica
BigDecimal dosisM edicamento = new BigDecimal("0.025");  // 25 miligramos
```

#### Cuándo usar enteros (centavos)

```java
// ✅ Sistemas de puntos, monedas virtuales
int puntosJugador = 1500;

// ✅ Centavos para cálculos simples
int precioEnCentavos = 1999;  // $19.99
int cantidadComprada = 3;
int totalCentavos = precioEnCentavos * cantidadComprada;
```

### Ejemplo del Mundo Real

```java
public class FacturaItem {
    private BigDecimal precioUnitario;
    private int cantidad;
    private BigDecimal tasaImpuesto;
    
    public BigDecimal calcularSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad))
            .setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal calcularImpuesto() {
        return calcularSubtotal().multiply(tasaImpuesto)
            .setScale(2, RoundingMode.HALF_UP);
    }
    
    public BigDecimal calcularTotal() {
        return calcularSubtotal().add(calcularImpuesto());
    }
}

// Uso
FacturaItem item = new FacturaItem(
    new BigDecimal("19.99"),  // precio unitario
    3,                         // cantidad
    new BigDecimal("0.21")    // 21% IVA
);

System.out.println("Subtotal: $" + item.calcularSubtotal());  // $59.97
System.out.println("IVA: $" + item.calcularImpuesto());       // $12.59
System.out.println("Total: $" + item.calcularTotal());        // $72.56
```

:::{important}
**Regla de oro**: Si el valor representa **dinero**, **porcentajes financieros** o cualquier cantidad donde los **errores de redondeo son inaceptables**, usá `BigDecimal`. No hay excusas.
:::

:::{warning}
Los errores de punto flotante pueden ser imperceptibles en operaciones individuales pero se **acumulan** en cálculos complejos. En un sistema de facturación con miles de operaciones diarias, estos errores pueden resultar en diferencias significativas entre los registros contables.
:::

:::{note}
**Performance**: `BigDecimal` es más lento que `double`, pero en aplicaciones de negocio esto es irrelevante. La **corrección** es infinitamente más importante que la velocidad cuando se trata de dinero.
:::

### Recursos Adicionales

- Java API: `java.math.BigDecimal`
- IEEE 754: Estándar de punto flotante binario
- Effective Java (Bloch): Item 60 - "Avoid float and double if exact answers are required"

(regla-0xE006)=
## `0xE006` - Usar comparaciones de strings para determinar comportamiento

### Explicación

Usar comparaciones de strings (como `operador.equals("-")` o `tipo.equals("suma")`) para determinar el comportamiento de un programa es equivalente a usar verificaciones de tipo con `instanceof`, y **viola el principio Open/Closed** del diseño orientado a objetos. Este anti-patrón aparece frecuentemente cuando los estudiantes intentan implementar polimorfismo manualmente en lugar de usar las capacidades del lenguaje.

El caso más común es en calculadoras o sistemas de operaciones donde se almacena el tipo de operación como `String` y luego se usa un gran `if-else` o `switch` para decidir qué hacer.

### Justificación

1. **Violación de Open/Closed**: Agregar una nueva operación requiere modificar el código existente
2. **Falta de polimorfismo**: No se aprovecha la capacidad del lenguaje para dispatch dinámico
3. **Código frágil**: Los strings son propensos a typos y no tienen validación en tiempo de compilación
4. **Mantenibilidad**: La lógica de decisión crece linealmente con cada nuevo caso
5. **Testing complejo**: Cada rama condicional debe testearse por separado

### Contexto: TP7 - Calculadora

Este problema surge típicamente en el TP7 (Calculadora) cuando se intenta:

```java
// ❌ Anti-patrón común
public class Operacion {
    private double operando1;
    private double operando2;
    private String tipoOperacion;  // ❌ String para determinar comportamiento
    
    public double calcular() {
        if (tipoOperacion.equals("suma")) {
            return operando1 + operando2;
        } else if (tipoOperacion.equals("resta")) {
            return operando1 - operando2;
        } else if (tipoOperacion.equals("multiplicacion")) {
            return operando1 * operando2;
        } else if (tipoOperacion.equals("division")) {
            if (operando2 == 0) {
                throw new ArithmeticException("División por cero");
            }
            return operando1 / operando2;
        }
        throw new IllegalArgumentException("Operación desconocida: " + tipoOperacion);
    }
    
    // ❌ Cada método debe repetir la lógica de decisión
    public String obtenerSimbolo() {
        if (tipoOperacion.equals("suma")) {
            return "+";
        } else if (tipoOperacion.equals("resta")) {
            return "-";
        } else if (tipoOperacion.equals("multiplicacion")) {
            return "*";
        } else if (tipoOperacion.equals("division")) {
            return "/";
        }
        return "?";
    }
}
```

### Solución: Polimorfismo

#### Diseño Correcto ✅

```java
// ✅ Interfaz o clase abstracta
public interface Operacion {
    double calcular();
    String obtenerSimbolo();
}

// ✅ Cada operación es una clase
public class Suma implements Operacion {
    private double operando1;
    private double operando2;
    
    public Suma(double operando1, double operando2) {
        this.operando1 = operando1;
        this.operando2 = operando2;
    }
    
    @Override
    public double calcular() {
        return operando1 + operando2;  // ✅ Lógica específica
    }
    
    @Override
    public String obtenerSimbolo() {
        return "+";  // ✅ Cada clase conoce su símbolo
    }
}

public class Division implements Operacion {
    private double operando1;
    private double operando2;
    
    public Division(double operando1, double operando2) {
        if (operando2 == 0) {
            throw new IllegalArgumentException("El divisor no puede ser cero");
        }
        this.operando1 = operando1;
        this.operando2 = operando2;
    }
    
    @Override
    public double calcular() {
        return operando1 / operando2;  // ✅ Validación en constructor
    }
    
    @Override
    public String obtenerSimbolo() {
        return "/";
    }
}
```

#### Uso

```java
// ✅ El cliente no necesita saber qué tipo específico es
Operacion op1 = new Suma(10, 5);
Operacion op2 = new Division(20, 4);

System.out.println(op1.calcular());  // 15.0
System.out.println(op2.calcular());  // 5.0

// ✅ Agregar nueva operación no requiere modificar código existente
public class Potencia implements Operacion {
    private double base;
    private double exponente;
    
    @Override
    public double calcular() {
        return Math.pow(base, exponente);
    }
    
    @Override
    public String obtenerSimbolo() {
        return "^";
    }
}
```

### Ventajas del Diseño Polimórfico

#### 1. Open/Closed Principle

```java
// Agregar nueva operación: crear nueva clase (Open)
// No modificar clases existentes (Closed)
public class Modulo implements Operacion {
    // Nueva funcionalidad sin tocar código existente
}
```

#### 2. Type Safety

```java
// ❌ Con Strings: error en runtime
Operacion op = crearOperacion("suma");  // Typo no detectado

// ✅ Con tipos: error en compile time
Operacion op = new Suma(10, 5);  // El compilador verifica
```

#### 3. Mantenibilidad

```java
// ✅ Cada clase tiene una única responsabilidad
// ✅ Cambios en División no afectan a Suma
// ✅ Fácil de testear cada operación independientemente
```

### Factory Pattern para Creación

Si necesitás crear operaciones desde strings (por ejemplo, al parsear entrada del usuario):

```java
public class OperacionFactory {
    
    public static Operacion crear(String simbolo, double op1, double op2) {
        // ✅ Lógica de decisión centralizada en UN solo lugar
        return switch (simbolo) {
            case "+" -> new Suma(op1, op2);
            case "-" -> new Resta(op1, op2);
            case "*" -> new Multiplicacion(op1, op2);
            case "/" -> new Division(op1, op2);
            default -> throw new IllegalArgumentException("Operación no soportada: " + simbolo);
        };
    }
}

// Uso
Operacion op = OperacionFactory.crear("+", 10, 5);
double resultado = op.calcular();  // ✅ Polimórfico desde aquí
```

:::{important}
La única lógica de decisión basada en strings debería estar **en la factory** al momento de **crear** el objeto. Una vez creado, todo el comportamiento debe ser **polimórfico**.
:::

### Casos Donde No Aplica

Este anti-patrón NO se refiere a:

```java
// ✅ Validación de entrada
if (entrada.equals("salir")) {
    terminarPrograma();
}

// ✅ Configuración y opciones
if (modo.equals("debug")) {
    activarDebug();
}

// ✅ Comandos de UI/CLI
if (comando.equals("guardar")) {
    guardarArchivo();
}
```

La regla se aplica cuando el string **determina el comportamiento polimórfico** de una jerarquía de clases.

:::{warning}
**Indicadores de este anti-patrón**:
- Múltiples métodos con `if-else` o `switch` sobre el mismo string
- String que representa "tipo" o "categoría" del objeto
- Necesidad de agregar nuevos casos en múltiples lugares
- Lógica de negocio duplicada en diferentes métodos

**Solución**: Convertir a jerarquía de clases con polimorfismo.
:::

:::{tip}
**Principio general**: Si te encontrás escribiendo el mismo `if-else` o `switch` sobre un campo de tipo/categoría en múltiples métodos, **es hora de aplicar polimorfismo**.
:::

### Referencias Relacionadas

- Ver {ref}`regla-0x200D` sobre Single Responsibility Principle
- Ver principios SOLID en el contexto de diseño orientado a objetos
- Patrón Strategy: Otra forma de evitar condicionales basados en tipo

(regla-0xE007)=
## `0xE007` - Usar separadores de línea específicos de plataforma

### Explicación

Los diferentes sistemas operativos utilizan distintos caracteres para representar un salto de línea: Windows usa `\r\n` (CRLF), Unix/Linux/macOS usan `\n` (LF), y Mac OS clásico usaba `\r` (CR). Hardcodear `\n` en el código Java hace que el programa funcione incorrectamente en otras plataformas.

Java proporciona `System.lineSeparator()` que retorna el separador de línea apropiado para el sistema operativo actual, garantizando **compatibilidad multiplataforma**.

### Justificación

1. **Portabilidad**: El código debe funcionar en cualquier sistema operativo sin modificaciones
2. **Corrección**: Archivos de texto deben usar las convenciones de la plataforma
3. **Interoperabilidad**: Otros programas esperan el formato nativo del sistema
4. **Profesionalismo**: Es un estándar de la industria para código portable

### Ejemplos

#### Incorrecto ❌

```java
// ❌ Hardcodear \n: solo funciona bien en Unix/Linux/macOS
public String generarReporte() {
    StringBuilder reporte = new StringBuilder();
    reporte.append("Título del Reporte\n");  // ❌ Hardcoded
    reporte.append("==================\n");  // ❌
    reporte.append("Datos aquí\n");          // ❌
    return reporte.toString();
}

// ❌ Leer archivo esperando \n específicamente
public void procesarArchivo(String contenido) {
    String[] lineas = contenido.split("\n");  // ❌ Falla en Windows
    for (String linea : lineas) {
        procesarLinea(linea);
    }
}

// ❌ Detectar fin de línea manualmente
for (int i = 0; i < contenido.length(); i++) {
    if (contenido.charAt(i) == '\n') {  // ❌ Solo detecta LF
        // Procesar línea
    }
}
```

#### Correcto ✅

```java
// ✅ Usar System.lineSeparator()
public String generarReporte() {
    StringBuilder reporte = new StringBuilder();
    String nl = System.lineSeparator();
    
    reporte.append("Título del Reporte").append(nl);  // ✅
    reporte.append("==================").append(nl);  // ✅
    reporte.append("Datos aquí").append(nl);          // ✅
    return reporte.toString();
}

// ✅ Usar BufferedReader.readLine() que maneja todos los formatos
public void procesarArchivo(String ruta) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
        String linea;
        while ((linea = reader.readLine()) != null) {  // ✅ Maneja todos los formatos
            procesarLinea(linea);
        }
    }
}

// ✅ Pattern.split() también es multiplataforma
public void procesarContenido(String contenido) {
    String[] lineas = contenido.split("\\R");  // ✅ \R es regex para cualquier line break
    for (String linea : lineas) {
        procesarLinea(linea);
    }
}
```

### Alternativas Según el Caso

#### 1. Para generación de texto

```java
// Opción A: System.lineSeparator() (más explícito)
String texto = "Línea 1" + System.lineSeparator() + "Línea 2";

// Opción B: String.format con %n (más conciso)
String texto = String.format("Línea 1%nLínea 2");

// Opción C: PrintWriter que maneja automáticamente
try (PrintWriter writer = new PrintWriter("archivo.txt")) {
    writer.println("Línea 1");  // ✅ println usa el separador correcto
    writer.println("Línea 2");
}
```

#### 2. Para lectura de archivos línea por línea

```java
// ✅ BufferedReader.readLine() es la solución estándar
try (BufferedReader reader = new BufferedReader(new FileReader("archivo.txt"))) {
    String linea;
    while ((linea = reader.readLine()) != null) {
        // readLine() elimina automáticamente el terminador de línea
        // y funciona con \n, \r\n, y \r
        System.out.println(linea);
    }
}

// ✅ Java 8+: Files.lines()
try (Stream<String> lineas = Files.lines(Paths.get("archivo.txt"))) {
    lineas.forEach(System.out::println);
}
```

:::{note}
En el contexto del curso (serie 0x6), no podés usar Streams. Usá `BufferedReader` con un loop tradicional.
:::

#### 3. Para dividir texto en líneas

```java
// ✅ Regex \R coincide con cualquier secuencia de salto de línea
String contenido = "Línea 1\nLínea 2\r\nLínea 3\rLínea 4";
String[] lineas = contenido.split("\\R");  // ✅ Funciona con todos los formatos

// Resultado: ["Línea 1", "Línea 2", "Línea 3", "Línea 4"]
```

### Caracteres de Salto de Línea

| Sistema Operativo | Secuencia | Representación | Nombre |
|:------------------|:---------:|:---------------|:-------|
| Unix/Linux/macOS  | `\n`      | LF (Line Feed) | 0x0A   |
| Windows           | `\r\n`    | CRLF (Carriage Return + Line Feed) | 0x0D 0x0A |
| Mac OS clásico    | `\r`      | CR (Carriage Return) | 0x0D |

### Caso Real: TP7 - Calculadora

El contexto específico menciona procesar contenido de archivos carácter por carácter:

```java
// ❌ Incorrecto: asumir solo \n
public void parsearCalculadora(String contenidoArchivo) {
    for (int i = 0; i < contenidoArchivo.length(); i++) {
        char c = contenidoArchivo.charAt(i);
        if (c == '\n') {  // ❌ Solo detecta LF, falla con CRLF
            procesarFinDeLinea();
        }
    }
}

// ✅ Correcto: usar String.split con \R
public void parsearCalculadora(String contenidoArchivo) {
    String[] lineas = contenidoArchivo.split("\\R");
    for (String linea : lineas) {
        procesarLinea(linea);
    }
}

// ✅ Mejor aún: usar BufferedReader si lees de archivo
public void parsearCalculadora(Path ruta) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(ruta)) {
        String linea;
        while ((linea = reader.readLine()) != null) {
            procesarLinea(linea);
        }
    }
}
```

### Detección Manual Robusta

Si realmente necesitás detectar fin de línea carácter por carácter (poco común):

```java
public boolean esFinDeLinea(char c) {
    return c == '\n' || c == '\r';
}

public void procesar(String contenido) {
    for (int i = 0; i < contenido.length(); i++) {
        char c = contenido.charAt(i);
        
        if (esFinDeLinea(c)) {
            // Manejo de CRLF: si es \r y el siguiente es \n, saltar ambos
            if (c == '\r' && i + 1 < contenido.length() && contenido.charAt(i + 1) == '\n') {
                i++;  // Saltar el \n que sigue al \r
            }
            procesarFinDeLinea();
        } else {
            procesarCaracter(c);
        }
    }
}
```

:::{warning}
El enfoque de procesar carácter por carácter es **raramente necesario** en Java moderno. Las APIs de alto nivel (`BufferedReader.readLine()`, `Files.lines()`, `String.split()`) manejan automáticamente las diferencias de plataforma y son más robustas.
:::

### Testing Multiplataforma

```java
@Test
public void testGenerarReporteFuncionaEnCualquierPlataforma() {
    String reporte = generador.generarReporte();
    
    // ✅ Verificar que usa el separador correcto del sistema
    assertTrue(reporte.contains(System.lineSeparator()));
    
    // ✅ O dividir y verificar líneas
    String[] lineas = reporte.split("\\R");
    assertEquals(3, lineas.length);
    assertEquals("Título del Reporte", lineas[0]);
}
```

:::{tip}
**Regla práctica**:
- **Generación de texto**: Usá `System.lineSeparator()` o `%n` en `printf`/`format`
- **Lectura de texto**: Usá `BufferedReader.readLine()` o `split("\\R")`
- **Escritura de texto**: Usá `PrintWriter.println()` que maneja automáticamente

**Nunca hardcodees `\n`, `\r\n` o `\r`** directamente en el código de lógica de negocio.
:::

### Referencias Relacionadas

- Ver {ref}`regla-0xE002` sobre cierre correcto de recursos al leer archivos
- Java API: `System.lineSeparator()`, `BufferedReader.readLine()`
