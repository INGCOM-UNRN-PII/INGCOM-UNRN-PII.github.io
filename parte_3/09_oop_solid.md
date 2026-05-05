---
title: "9: Principios SOLID"
subtitle: "Fundamentos del Diseño Orientado a Objetos de Calidad"
subject: Programación Orientada a Objetos
---

(oop-solid)=
# OOP 6: Principios SOLID

En los capítulos anteriores construimos objetos, establecimos relaciones, exploramos herencia y polimorfismo, definimos contratos ({ref}`oop-contratos`), y estudiamos patrones de diseño ({ref}`oop5-patrones-diseno`). Pero, ¿cómo sabemos si nuestro diseño es **bueno**? ¿Qué características debe tener un sistema orientado a objetos para ser **mantenible**, **extensible** y **robusto**?

Los **principios SOLID** son cinco directrices fundamentales que guían el diseño de software orientado a objetos hacia sistemas de alta calidad. Fueron recopilados y popularizados por **Robert C. Martin** (Uncle Bob) a principios de los 2000, aunque cada principio tiene raíces más antiguas. Estos principios también ayudan a identificar y corregir {ref}`code-smells` en el código.

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, serás capaz de:

1. Comprender cada uno de los cinco principios SOLID
2. Identificar violaciones de estos principios en código existente
3. Aplicar refactorizaciones para corregir violaciones
4. Evaluar trade-offs entre principios
5. Relacionar SOLID con patrones de diseño conocidos
:::

---

(introduccion-solid)=
## Introducción: ¿Por qué SOLID?

(costo-mal-diseno)=
### El Costo del Mal Diseño

Considerá un sistema que crece sin principios claros de diseño:

```
Año 1: "¡Funciona! El código es simple."
Año 2: "Agregar features toma más tiempo..."
Año 3: "Cada cambio rompe algo en otro lado."
Año 4: "Nadie entiende este código."
Año 5: "Necesitamos reescribir todo desde cero."
```

Este patrón, conocido como **degradación del diseño**, ocurre cuando el código acumula **deuda técnica** sin control. Los síntomas incluyen:

- **Rigidez**: Cambios pequeños requieren modificar muchos archivos
- **Fragilidad**: Arreglar un bug introduce nuevos bugs
- **Inmovilidad**: Es imposible reutilizar código en otros contextos
- **Viscosidad**: Es más fácil hacer las cosas mal que bien

(solid-como-antidoto)=
### SOLID como Antídoto

Los principios SOLID atacan directamente estos síntomas:

| Principio | Combate |
|-----------|---------|
| **S**ingle Responsibility | Rigidez |
| **O**pen/Closed | Fragilidad |
| **L**iskov Substitution | Fragilidad, Inmovilidad |
| **I**nterface Segregation | Rigidez, Inmovilidad |
| **D**ependency Inversion | Rigidez, Inmovilidad |

:::{important}
SOLID no es un conjunto de reglas rígidas sino **heurísticas de diseño**. Aplicarlas ciegamente puede llevar a sobre-ingeniería. El objetivo es entender el **espíritu** de cada principio y aplicarlo con criterio.
:::

---

(srp-single-responsibility)=
## S: Principio de Responsabilidad Única

(srp-definicion)=
### Definición

:::{important} Single Responsibility Principle (SRP)

**Una clase debe tener una, y solo una, razón para cambiar.**

— Robert C. Martin
:::

La formulación original habla de "razón para cambiar" en lugar de "responsabilidad" porque es más precisa. Una **razón para cambiar** representa a un **actor** o **stakeholder** que podría solicitar modificaciones.

(srp-ejemplo-violacion)=
### Ejemplo: Violación del SRP

Considerá una clase `Empleado` típica:

```java
public class Empleado {
    private String nombre;
    private double salarioBase;
    private int horasTrabajadas;
    
    // Calcula el salario (usado por Contabilidad)
    public double calcularSalario() {
        return salarioBase + (horasTrabajadas * 50);
    }
    
    // Genera reporte para RRHH
    public String generarReporteRRHH() {
        return "Empleado: " + nombre + 
               "\nHoras: " + horasTrabajadas;
    }
    
    // Guarda en base de datos (usado por IT)
    public void guardarEnBaseDeDatos() {
        // Conexión a BD, SQL, etc.
    }
}
```

Esta clase tiene **tres razones para cambiar**:

1. **Contabilidad** podría cambiar la fórmula de cálculo salarial
2. **RRHH** podría modificar el formato del reporte
3. **IT** podría cambiar el esquema de base de datos

```{mermaid}
classDiagram
    class Empleado {
        -String nombre
        -double salarioBase
        -int horasTrabajadas
        +calcularSalario() double
        +generarReporteRRHH() String
        +guardarEnBaseDeDatos()
    }
    
    class Contabilidad {
        <<actor>>
    }
    
    class RRHH {
        <<actor>>
    }
    
    class IT {
        <<actor>>
    }
    
    Contabilidad ..> Empleado : usa calcularSalario()
    RRHH ..> Empleado : usa generarReporteRRHH()
    IT ..> Empleado : usa guardarEnBaseDeDatos()
    
    note for Empleado "❌ VIOLACIÓN SRP:<br>TRES razones para cambiar<br>TRES actores diferentes"
```

(srp-problemas)=
### Problemas de Esta Violación

1. **Acoplamiento no deseado**: Un cambio pedido por Contabilidad podría afectar a RRHH o IT
2. **Compilación innecesaria**: Modificar el reporte obliga a recompilar y redesplegar todo
3. **Conflictos de merge**: Múltiples equipos editando el mismo archivo

(srp-refactorizacion)=
### Refactorización: Separar Responsabilidades

```java
// Datos del empleado (estructura de datos pura)
public class Empleado {
    private String nombre;
    private double salarioBase;
    private int horasTrabajadas;
    
    // Solo getters/setters
    public String getNombre() { return nombre; }
    public double getSalarioBase() { return salarioBase; }
    public int getHorasTrabajadas() { return horasTrabajadas; }
}

// Responsabilidad: Contabilidad
public class CalculadorSalario {
    public double calcular(Empleado empleado) {
        return empleado.getSalarioBase() + 
               (empleado.getHorasTrabajadas() * 50);
    }
}

// Responsabilidad: RRHH
public class GeneradorReporteRRHH {
    public String generar(Empleado empleado) {
        return "Empleado: " + empleado.getNombre() + 
               "\nHoras: " + empleado.getHorasTrabajadas();
    }
}

// Responsabilidad: Persistencia
public class EmpleadoRepository {
    public void guardar(Empleado empleado) {
        // Lógica de persistencia
    }
}
```

Ahora cada clase tiene **una única razón para cambiar**:

```{mermaid}
classDiagram
    class Empleado {
        -String nombre
        -double salarioBase
        -int horasTrabajadas
        +getNombre() String
        +getSalarioBase() double
        +getHorasTrabajadas() int
    }
    
    class CalculadorSalario {
        +calcular(Empleado) double
    }
    
    class GeneradorReporteRRHH {
        +generar(Empleado) String
    }
    
    class EmpleadoRepository {
        +guardar(Empleado)
        +buscar(int id) Empleado
    }
    
    class Contabilidad {
        <<actor>>
    }
    
    class RRHH {
        <<actor>>
    }
    
    class IT {
        <<actor>>
    }
    
    CalculadorSalario ..> Empleado : usa
    GeneradorReporteRRHH ..> Empleado : usa
    EmpleadoRepository ..> Empleado : usa
    
    Contabilidad ..> CalculadorSalario : solicita cambios
    RRHH ..> GeneradorReporteRRHH : solicita cambios
    IT ..> EmpleadoRepository : solicita cambios
    
    note for Empleado "✓ CUMPLE SRP:<br>Datos puros sin lógica de negocio"
    note for CalculadorSalario "✓ UNA razón para cambiar:<br>Cambios en reglas salariales"
    note for GeneradorReporteRRHH "✓ UNA razón para cambiar:<br>Cambios en formato de reporte"
```

(srp-granularidad)=
### ¿Cuánta Separación es Suficiente?

El SRP no dice que cada clase debe tener un solo método. El criterio es **razones para cambiar**, no cantidad de código.

:::{tip} Heurística Práctica

Preguntate: "Si [actor X] pide un cambio, ¿qué clases necesito modificar?"

Si la respuesta incluye clases que otros actores también usan, probablemente hay una violación del SRP.
:::

**Sobre-aplicación** (exceso de clases):
```java
// ¡Demasiado granular!
public class NombreEmpleado { ... }
public class SalarioBaseEmpleado { ... }
public class HorasTrabajadasEmpleado { ... }
```

**Sub-aplicación** (clase monolítica):
```java
// ¡Demasiadas responsabilidades!
public class SistemaEmpresarial {
    // Empleados, clientes, productos, ventas,
    // reportes, emails, notificaciones...
}
```

---

(ocp-open-closed)=
## O: Principio Abierto/Cerrado

(ocp-definicion)=
### Definición

:::{important} Open/Closed Principle (OCP)

**Las entidades de software deben estar abiertas para extensión, pero cerradas para modificación.**

— Bertrand Meyer
:::

Esto significa que debemos poder **agregar nuevo comportamiento** sin **modificar código existente**. La técnica principal para lograrlo es la **abstracción** mediante interfaces o clases abstractas.

(ocp-ejemplo-violacion)=
### Ejemplo: Violación del OCP

Considerá un sistema de cálculo de áreas:

```java
public class CalculadorArea {
    
    public double calcularAreaTotal(List<Object> figuras) {
        double total = 0;
        
        for (Object figura : figuras) {
            if (figura instanceof Rectangulo) {
                Rectangulo r = (Rectangulo) figura;
                total += r.getAncho() * r.getAlto();
                
            } else if (figura instanceof Circulo) {
                Circulo c = (Circulo) figura;
                total += Math.PI * c.getRadio() * c.getRadio();
                
            } else if (figura instanceof Triangulo) {
                Triangulo t = (Triangulo) figura;
                total += (t.getBase() * t.getAltura()) / 2;
            }
            // ¿Qué pasa si agregamos Hexágono, Trapecio, etc.?
        }
        
        return total;
    }
}
```

Cada vez que se agrega una nueva figura, hay que **modificar** `CalculadorArea`. La clase no está cerrada para modificación.

```{mermaid}
classDiagram
    class CalculadorArea {
        +calcularAreaTotal(List~Object~) double
    }
    
    class Rectangulo {
        -double ancho
        -double alto
        +getAncho() double
        +getAlto() double
    }
    
    class Circulo {
        -double radio
        +getRadio() double
    }
    
    class Triangulo {
        -double base
        -double altura
        +getBase() double
        +getAltura() double
    }
    
    CalculadorArea ..> Rectangulo : instanceof
    CalculadorArea ..> Circulo : instanceof
    CalculadorArea ..> Triangulo : instanceof
    
    note for CalculadorArea "❌ VIOLACIÓN OCP:<br>Cada nueva figura requiere<br>MODIFICAR este código<br>(agregar nuevo if/else)"
```

**Problema:** Agregar una nueva figura (Hexágono, Trapecio) requiere modificar `CalculadorArea`.

(ocp-refactorizacion)=
### Refactorización: Usar Abstracción

```java
// Abstracción: contrato que todas las figuras deben cumplir
public interface Figura {
    double calcularArea();
}

// Cada figura implementa su propio cálculo
public class Rectangulo implements Figura {
    private double ancho;
    private double alto;
    
    @Override
    public double calcularArea() {
        return ancho * alto;
    }
}

public class Circulo implements Figura {
    private double radio;
    
    @Override
    public double calcularArea() {
        return Math.PI * radio * radio;
    }
}

public class Triangulo implements Figura {
    private double base;
    private double altura;
    
    @Override
    public double calcularArea() {
        return (base * altura) / 2;
    }
}

// Calculador cerrado para modificación, abierto para extensión
public class CalculadorArea {
    
    public double calcularAreaTotal(List<Figura> figuras) {
        double total = 0;
        for (Figura figura : figuras) {
            total += figura.calcularArea();
        }
        return total;
    }
}
```

Ahora agregar un `Hexagono` no requiere modificar `CalculadorArea`:

```{mermaid}
classDiagram
    class Figura {
        <<interface>>
        +calcularArea() double
    }
    
    class CalculadorArea {
        +calcularAreaTotal(List~Figura~) double
    }
    
    class Rectangulo {
        -double ancho
        -double alto
        +calcularArea() double
    }
    
    class Circulo {
        -double radio
        +calcularArea() double
    }
    
    class Triangulo {
        -double base
        -double altura
        +calcularArea() double
    }
    
    class Hexagono {
        -double lado
        +calcularArea() double
    }
    
    Figura <|.. Rectangulo
    Figura <|.. Circulo
    Figura <|.. Triangulo
    Figura <|.. Hexagono
    
    CalculadorArea ..> Figura : usa
    
    note for CalculadorArea "✓ CUMPLE OCP:<br>CERRADO para modificación<br>ABIERTO para extensión"
    note for Hexagono "Nueva figura agregada<br>sin modificar CalculadorArea"
```

**Beneficio:** Agregar `Hexagono` solo requiere crear la nueva clase, sin tocar `CalculadorArea`.

(ocp-mecanismos)=
### Mecanismos para Lograr OCP

#### 1. Polimorfismo (el más común)

```java
interface Exportador {
    void exportar(Documento doc);
}

class ExportadorPDF implements Exportador { ... }
class ExportadorWord implements Exportador { ... }
class ExportadorHTML implements Exportador { ... }  // Nuevo, sin modificar nada
```

#### 2. Patrón Strategy

```java
interface EstrategiaDescuento {
    double aplicar(double precio);
}

class SinDescuento implements EstrategiaDescuento { ... }
class DescuentoPorcentual implements EstrategiaDescuento { ... }
class DescuentoBlackFriday implements EstrategiaDescuento { ... }  // Nuevo
```

#### 3. Patrón Template Method

```java
abstract class ProcesadorArchivo {
    public final void procesar() {
        abrir();
        leerContenido();      // Hook para extensión
        procesarContenido();  // Hook para extensión
        cerrar();
    }
    
    protected abstract void leerContenido();
    protected abstract void procesarContenido();
}
```

(ocp-anticipacion)=
### El Problema de la Anticipación

OCP requiere **anticipar** qué partes del sistema cambiarán. Pero predecir el futuro es difícil.

:::{tip} Estrategia Práctica

**Regla de los tres strikes:**

1. Primera vez: Implementá la solución simple
2. Segunda vez que necesitás algo similar: Notá el patrón, pero no generalices aún
3. Tercera vez: Ahora sí, refactorizá para OCP

No generalices prematuramente. Esperá hasta tener evidencia de qué variaciones son reales.
:::

---

(lsp-liskov-substitution)=
## L: Principio de Sustitución de Liskov

(lsp-definicion)=
### Definición

:::{important} Liskov Substitution Principle (LSP)

**Los objetos de un programa deberían ser reemplazables por instancias de sus subtipos sin alterar la corrección del programa.**

— Barbara Liskov, 1987
:::

En términos más simples: si `S` es subtipo de `T`, entonces objetos de tipo `T` pueden ser sustituidos por objetos de tipo `S` sin que el programa se rompa.

(lsp-ejemplo-clasico)=
### El Ejemplo Clásico: Rectángulo y Cuadrado

Matemáticamente, un cuadrado **es un** rectángulo. Entonces, ¿`Cuadrado` debería heredar de `Rectangulo`?

```java
public class Rectangulo {
    protected int ancho;
    protected int alto;
    
    public void setAncho(int ancho) {
        this.ancho = ancho;
    }
    
    public void setAlto(int alto) {
        this.alto = alto;
    }
    
    public int getArea() {
        return ancho * alto;
    }
}

public class Cuadrado extends Rectangulo {
    
    @Override
    public void setAncho(int ancho) {
        this.ancho = ancho;
        this.alto = ancho;  // ¡Mantener cuadrado!
    }
    
    @Override
    public void setAlto(int alto) {
        this.alto = alto;
        this.ancho = alto;  // ¡Mantener cuadrado!
    }
}
```

Parece razonable, pero viola LSP. Considerá este código cliente:

```java
public void agrandarRectangulo(Rectangulo r) {
    int anchoOriginal = r.getAncho();
    r.setAlto(r.getAlto() + 10);
    
    // Postcondición esperada: el ancho no cambió
    assert r.getAncho() == anchoOriginal;  // ¡FALLA con Cuadrado!
}
```

```{mermaid}
classDiagram
    class Rectangulo {
        #int ancho
        #int alto
        +setAncho(int)
        +setAlto(int)
        +getArea() int
    }
    
    class Cuadrado {
        +setAncho(int)
        +setAlto(int)
    }
    
    Rectangulo <|-- Cuadrado
    
    note for Rectangulo "Contrato esperado:<br>setAlto() NO modifica ancho<br>setAncho() NO modifica alto"
    note for Cuadrado "❌ VIOLACIÓN LSP:<br>setAlto() MODIFICA ancho<br>setAncho() MODIFICA alto<br><br>No sustituible por Rectangulo"
```

**Problema:** `Cuadrado` viola las expectativas del cliente que usa `Rectangulo`.

(lsp-reglas)=
### Reglas de LSP

Para que un subtipo sea sustituible, debe cumplir:

#### 1. Precondiciones no pueden fortalecerse

El subtipo no puede exigir **más** que el supertipo.

```java
// Supertipo
class Procesador {
    // Precondición: valor >= 0
    void procesar(int valor) { ... }
}

// INCORRECTO: fortalece precondición
class ProcesadorEstricto extends Procesador {
    // Precondición: valor >= 10  ← ¡Más restrictiva!
    @Override
    void procesar(int valor) {
        if (valor < 10) throw new IllegalArgumentException();
    }
}

// CORRECTO: debilita o mantiene precondición
class ProcesadorFlexible extends Procesador {
    // Precondición: valor >= -100  ← Acepta más valores
    @Override
    void procesar(int valor) { ... }
}
```

#### 2. Postcondiciones no pueden debilitarse

El subtipo debe garantizar **al menos lo mismo** que el supertipo.

```java
// Supertipo
class Buscador {
    // Postcondición: retorna lista ordenada
    List<String> buscar(String query) { ... }
}

// INCORRECTO: debilita postcondición
class BuscadorRapido extends Buscador {
    // Postcondición: retorna lista (¿ordenada? a veces...)
    @Override
    List<String> buscar(String query) {
        // Retorna sin ordenar para ser más rápido
    }
}

// CORRECTO: fortalece postcondición
class BuscadorCompleto extends Buscador {
    // Postcondición: retorna lista ordenada + sin duplicados
    @Override
    List<String> buscar(String query) { ... }
}
```

#### 3. Invariantes deben preservarse

Las condiciones que siempre son verdaderas en el supertipo también deben serlo en el subtipo.

```java
// Invariante: saldo >= 0
class CuentaBancaria {
    protected double saldo;
    
    void retirar(double monto) {
        if (monto > saldo) throw new SaldoInsuficienteException();
        saldo -= monto;
    }
}

// INCORRECTO: viola invariante
class CuentaConDescubierto extends CuentaBancaria {
    @Override
    void retirar(double monto) {
        saldo -= monto;  // ¡Permite saldo negativo!
    }
}
```

(lsp-solucion-cuadrado)=
### Solución al Problema Rectángulo-Cuadrado

Hay varias formas de resolverlo:

#### Opción 1: Inmutabilidad

```java
public final class Rectangulo {
    private final int ancho;
    private final int alto;
    
    public Rectangulo(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
    }
    
    public Rectangulo conAncho(int nuevoAncho) {
        return new Rectangulo(nuevoAncho, this.alto);
    }
    
    public Rectangulo conAlto(int nuevoAlto) {
        return new Rectangulo(this.ancho, nuevoAlto);
    }
}

public final class Cuadrado {
    private final int lado;
    
    public Cuadrado(int lado) {
        this.lado = lado;
    }
    
    public Cuadrado conLado(int nuevoLado) {
        return new Cuadrado(nuevoLado);
    }
}
```

Sin herencia, sin problema. Cada figura tiene su propio comportamiento coherente.

#### Opción 2: Interfaz común sin setters

```{mermaid}
classDiagram
    class Figura {
        <<interface>>
        +getArea() double
        +getPerimetro() double
    }
    
    class Rectangulo {
        -int ancho
        -int alto
        +setAncho(int)
        +setAlto(int)
        +getArea() double
        +getPerimetro() double
    }
    
    class Cuadrado {
        -int lado
        +setLado(int)
        +getArea() double
        +getPerimetro() double
    }
    
    Figura <|.. Rectangulo
    Figura <|.. Cuadrado
    
    note for Figura "✓ CUMPLE LSP:<br>Interfaz común solo lectura<br>Cada clase con sus propios setters"
    note for Rectangulo "setAncho() y setAlto()<br>independientes"
    note for Cuadrado "Solo setLado()<br>mantiene invariante"
```

(lsp-relacion-contratos)=
### Relación con Diseño por Contratos

LSP está íntimamente relacionado con el **Diseño por Contratos** (ver {ref}`oop-contratos`):

| Concepto | LSP | DbC |
|----------|-----|-----|
| Entrada | Precondiciones no fortalecidas | Precondiciones del contrato (ver {ref}`precondiciones-lo-que-el-cliente-debe-garantizar`) |
| Salida | Postcondiciones no debilitadas | Postcondiciones del contrato (ver {ref}`postcondiciones-lo-que-el-metodo-garantiza`) |
| Estado | Invariantes preservados | Invariantes de clase (ver {ref}`invariantes-de-clase-lo-que-siempre-debe-ser-verdad`) |

---

(isp-interface-segregation)=
## I: Principio de Segregación de Interfaces

(isp-definicion)=
### Definición

:::{important} Interface Segregation Principle (ISP)

**Los clientes no deberían verse forzados a depender de interfaces que no usan.**

— Robert C. Martin
:::

Es preferible tener muchas interfaces pequeñas y específicas que una interfaz grande y general.

(isp-ejemplo-violacion)=
### Ejemplo: Interfaz Gorda

Considerá una interfaz para dispositivos multifunción:

```java
public interface DispositivoMultifuncion {
    void imprimir(Documento doc);
    void escanear(Documento doc);
    void enviarFax(Documento doc);
    void fotocopiar(Documento doc);
}
```

Ahora, ¿qué pasa con una impresora simple que solo imprime?

```java
public class ImpresoraBasica implements DispositivoMultifuncion {
    
    @Override
    public void imprimir(Documento doc) {
        // Implementación real
    }
    
    @Override
    public void escanear(Documento doc) {
        throw new UnsupportedOperationException("No puedo escanear");
    }
    
    @Override
    public void enviarFax(Documento doc) {
        throw new UnsupportedOperationException("No puedo enviar fax");
    }
    
    @Override
    public void fotocopiar(Documento doc) {
        throw new UnsupportedOperationException("No puedo fotocopiar");
    }
}
```

```{mermaid}
classDiagram
    class DispositivoMultifuncion {
        <<interface>>
        +imprimir(Documento)
        +escanear(Documento)
        +enviarFax(Documento)
        +fotocopiar(Documento)
    }
    
    class ImpresoraBasica {
        +imprimir(Documento)
        +escanear(Documento)
        +enviarFax(Documento)
        +fotocopiar(Documento)
    }
    
    DispositivoMultifuncion <|.. ImpresoraBasica
    
    note for DispositivoMultifuncion "❌ VIOLACIÓN ISP:<br>Interfaz demasiado grande"
    note for ImpresoraBasica "Forzada a implementar<br>métodos que no usa<br>(lanza excepciones)"
```

(isp-refactorizacion)=
### Refactorización: Interfaces Segregadas

```java
public interface Impresora {
    void imprimir(Documento doc);
}

public interface Escaner {
    void escanear(Documento doc);
}

public interface Fax {
    void enviarFax(Documento doc);
}

public interface Fotocopiadora {
    void fotocopiar(Documento doc);
}

// Impresora simple: solo implementa lo que necesita
public class ImpresoraBasica implements Impresora {
    @Override
    public void imprimir(Documento doc) {
        // Implementación real
    }
}

// Dispositivo multifunción: implementa varias interfaces
public class CanonMultifuncion implements Impresora, Escaner, Fax, Fotocopiadora {
    @Override
    public void imprimir(Documento doc) { ... }
    
    @Override
    public void escanear(Documento doc) { ... }
    
    @Override
    public void enviarFax(Documento doc) { ... }
    
    @Override
    public void fotocopiar(Documento doc) { ... }
}

// Scanner de escritorio
public class EscanerEpson implements Escaner {
    @Override
    public void escanear(Documento doc) { ... }
}
```

```{mermaid}
classDiagram
    class Impresora {
        <<interface>>
        +imprimir(Documento)
    }
    
    class Escaner {
        <<interface>>
        +escanear(Documento)
    }
    
    class Fax {
        <<interface>>
        +enviarFax(Documento)
    }
    
    class Fotocopiadora {
        <<interface>>
        +fotocopiar(Documento)
    }
    
    class ImpresoraBasica {
        +imprimir(Documento)
    }
    
    class EscanerEpson {
        +escanear(Documento)
    }
    
    class CanonMultifuncion {
        +imprimir(Documento)
        +escanear(Documento)
        +enviarFax(Documento)
        +fotocopiar(Documento)
    }
    
    Impresora <|.. ImpresoraBasica
    Escaner <|.. EscanerEpson
    Impresora <|.. CanonMultifuncion
    Escaner <|.. CanonMultifuncion
    Fax <|.. CanonMultifuncion
    Fotocopiadora <|.. CanonMultifuncion
    
    note for Impresora "✓ CUMPLE ISP:<br>Interfaces pequeñas y específicas"
    note for ImpresoraBasica "Solo implementa lo que necesita"
    note for CanonMultifuncion "Combina múltiples interfaces"
```

(isp-beneficios)=
### Beneficios de ISP

1. **Bajo acoplamiento**: Los clientes solo dependen de lo que usan
2. **Compilación selectiva**: Cambios en `Fax` no afectan a `ImpresoraBasica`
3. **Diseño más claro**: Interfaces pequeñas son más fáciles de entender
4. **Composición flexible**: Se pueden combinar interfaces según necesidad

(isp-cohesion)=
### ISP y Cohesión de Interfaces

Una buena heurística es que cada interfaz debe representar un **rol** coherente:

```java
// MAL: Mezcla roles diferentes
interface Usuario {
    void login();
    void logout();
    void cambiarPassword();
    void generarReporte();      // ¿Todos los usuarios generan reportes?
    void administrarUsuarios(); // ¿Todos los usuarios administran?
}

// BIEN: Roles separados
interface Autenticable {
    void login();
    void logout();
    void cambiarPassword();
}

interface GeneradorReportes {
    void generarReporte();
}

interface AdministradorUsuarios {
    void administrarUsuarios();
}
```

---

(dip-dependency-inversion)=
## D: Principio de Inversión de Dependencias

(dip-definicion)=
### Definición

:::{important} Dependency Inversion Principle (DIP)

**A. Los módulos de alto nivel no deben depender de módulos de bajo nivel. Ambos deben depender de abstracciones.**

**B. Las abstracciones no deben depender de detalles. Los detalles deben depender de abstracciones.**

— Robert C. Martin
:::

Este principio habla de la **dirección de las dependencias** en la arquitectura del software.

(dip-ejemplo-violacion)=
### Ejemplo: Violación de DIP

Considerá un sistema de notificaciones:

```java
// Módulo de bajo nivel (detalle de implementación)
public class EnviadorEmail {
    public void enviar(String destinatario, String mensaje) {
        // Lógica SMTP...
    }
}

// Módulo de alto nivel (lógica de negocio)
public class ServicioNotificaciones {
    private EnviadorEmail enviador = new EnviadorEmail();  // ¡Dependencia directa!
    
    public void notificarUsuario(Usuario usuario, String mensaje) {
        enviador.enviar(usuario.getEmail(), mensaje);
    }
}
```

```{mermaid}
classDiagram
    class ServicioNotificaciones {
        -EnviadorEmail enviador
        +notificarUsuario(Usuario, String)
    }
    
    class EnviadorEmail {
        +enviar(String, String)
    }
    
    ServicioNotificaciones --> EnviadorEmail
    
    note for ServicioNotificaciones "❌ VIOLACIÓN DIP:<br>Módulo ALTO nivel depende<br>directamente de módulo BAJO nivel"
    note for EnviadorEmail "Detalle de implementación<br>concreto (SMTP)"
```

**Problemas:** No se puede cambiar a SMS, difícil de testear, acoplamiento alto.

(dip-refactorizacion)=
### Refactorización: Invertir la Dependencia

```java
// Abstracción (definida en el nivel de la lógica de negocio)
public interface Notificador {
    void enviar(String destinatario, String mensaje);
}

// Módulo de alto nivel depende de la abstracción
public class ServicioNotificaciones {
    private Notificador notificador;  // Dependencia a abstracción
    
    public ServicioNotificaciones(Notificador notificador) {
        this.notificador = notificador;
    }
    
    public void notificarUsuario(Usuario usuario, String mensaje) {
        notificador.enviar(usuario.getEmail(), mensaje);
    }
}

// Módulos de bajo nivel implementan la abstracción
public class NotificadorEmail implements Notificador {
    @Override
    public void enviar(String destinatario, String mensaje) {
        // Lógica SMTP...
    }
}

public class NotificadorSMS implements Notificador {
    @Override
    public void enviar(String destinatario, String mensaje) {
        // Lógica SMS...
    }
}

public class NotificadorMock implements Notificador {
    @Override
    public void enviar(String destinatario, String mensaje) {
        // Para tests
    }
}
```

```{mermaid}
classDiagram
    class ServicioNotificaciones {
        -Notificador notificador
        +ServicioNotificaciones(Notificador)
        +notificarUsuario(Usuario, String)
    }
    
    class Notificador {
        <<interface>>
        +enviar(String, String)
    }
    
    class NotificadorEmail {
        +enviar(String, String)
    }
    
    class NotificadorSMS {
        +enviar(String, String)
    }
    
    class NotificadorPush {
        +enviar(String, String)
    }
    
    class NotificadorMock {
        +enviar(String, String)
    }
    
    ServicioNotificaciones --> Notificador
    Notificador <|.. NotificadorEmail
    Notificador <|.. NotificadorSMS
    Notificador <|.. NotificadorPush
    Notificador <|.. NotificadorMock
    
    note for Notificador "✓ CUMPLE DIP:<br>Abstracción propiedad<br>del módulo de alto nivel"
    note for ServicioNotificaciones "Alto nivel depende<br>de ABSTRACCIÓN"
    note for NotificadorEmail "Bajo nivel depende<br>de ABSTRACCIÓN"
```

(dip-inversion-control)=
### Inversión de Control (IoC)

DIP está relacionado con el concepto de **Inversión de Control**: en lugar de que el código de alto nivel controle la creación de dependencias, ese control se "invierte" hacia afuera.

#### Inyección de Dependencias

Es el mecanismo más común para implementar DIP:

```java
// Inyección por constructor (preferido)
public class ServicioNotificaciones {
    private final Notificador notificador;
    
    public ServicioNotificaciones(Notificador notificador) {
        this.notificador = notificador;
    }
}

// Uso
Notificador emailReal = new NotificadorEmail();
ServicioNotificaciones servicio = new ServicioNotificaciones(emailReal);

// Para tests
Notificador mock = new NotificadorMock();
ServicioNotificaciones servicioTest = new ServicioNotificaciones(mock);
```

(dip-ownership)=
### ¿Quién "Posee" la Abstracción?

Un punto sutil pero importante: la **abstracción debe pertenecer al módulo de alto nivel**, no al de bajo nivel.

```
INCORRECTO:
┌─────────────────────────────────────────────┐
│ paquete: servicios.email                    │
│                                             │
│   interface Notificador  ← definida aquí    │
│   class NotificadorEmail                    │
└─────────────────────────────────────────────┘
             ▲
             │ depende
┌────────────┴────────────────────────────────┐
│ paquete: negocio                            │
│                                             │
│   class ServicioNotificaciones              │
└─────────────────────────────────────────────┘

CORRECTO:
┌─────────────────────────────────────────────┐
│ paquete: negocio                            │
│                                             │
│   interface Notificador  ← definida aquí    │
│   class ServicioNotificaciones              │
└─────────────────────────────────────────────┘
             ▲
             │ implementa
┌────────────┴────────────────────────────────┐
│ paquete: servicios.email                    │
│                                             │
│   class NotificadorEmail implements Notif.  │
└─────────────────────────────────────────────┘
```

---

(solid-interrelaciones)=
## Interrelaciones entre Principios SOLID

Los cinco principios no son independientes; se refuerzan mutuamente:

```
┌─────────────────────────────────────────────────────────────────┐
│                    INTERRELACIONES SOLID                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│              ┌─────┐                                            │
│              │ SRP │ ← Base de todo: responsabilidades claras  │
│              └──┬──┘                                            │
│                 │                                               │
│         ┌───────┴───────┐                                       │
│         ▼               ▼                                       │
│     ┌─────┐         ┌─────┐                                     │
│     │ OCP │ ←──────→│ LSP │ ← Extensión segura                 │
│     └──┬──┘         └──┬──┘                                     │
│        │               │                                        │
│        └───────┬───────┘                                        │
│                │                                                │
│         ┌──────┴──────┐                                         │
│         ▼             ▼                                         │
│     ┌─────┐       ┌─────┐                                       │
│     │ ISP │       │ DIP │ ← Interfaces limpias + dependencias  │
│     └─────┘       └─────┘   bien direccionadas                 │
│                                                                 │
│  SRP → Clases pequeñas → más fácil cumplir OCP                 │
│  OCP → Usa polimorfismo → LSP define reglas de polimorfismo    │
│  LSP → Subtipos correctos → requiere interfaces cohesivas (ISP)│
│  ISP → Interfaces pequeñas → facilitan DIP                     │
│  DIP → Abstracciones → habilitan OCP                           │
└─────────────────────────────────────────────────────────────────┘
```

(solid-ejemplo-integrado)=
### Ejemplo Integrado: Sistema de Pagos

Veamos cómo todos los principios trabajan juntos:

```java
// ISP: Interfaces segregadas para diferentes capacidades
interface ProcesadorPago {
    ResultadoPago procesar(OrdenPago orden);
}

interface Reembolsable {
    ResultadoReembolso reembolsar(String transaccionId, double monto);
}

interface ConSuscripcion {
    void crearSuscripcion(Usuario usuario, Plan plan);
    void cancelarSuscripcion(String suscripcionId);
}

// LSP: Implementaciones que cumplen contratos
class ProcesadorTarjeta implements ProcesadorPago, Reembolsable {
    @Override
    public ResultadoPago procesar(OrdenPago orden) {
        // Precondición: orden.getMonto() > 0
        // Postcondición: retorna resultado válido (éxito o error con razón)
        // ...
    }
    
    @Override
    public ResultadoReembolso reembolsar(String transaccionId, double monto) {
        // ...
    }
}

class ProcesadorPayPal implements ProcesadorPago, Reembolsable, ConSuscripcion {
    // Implementa todas las interfaces que soporta
}

class ProcesadorTransferencia implements ProcesadorPago {
    // Solo soporta pagos, no reembolsos ni suscripciones
}

// SRP: Cada clase tiene una responsabilidad clara
class ValidadorOrden {
    public void validar(OrdenPago orden) { /* solo validación */ }
}

class RegistradorTransacciones {
    public void registrar(ResultadoPago resultado) { /* solo logging */ }
}

class NotificadorPagos {
    private final Notificador notificador;  // DIP
    
    public void notificar(Usuario usuario, ResultadoPago resultado) {
        // solo notificaciones
    }
}

// OCP + DIP: ServicioPagos cerrado para modificación, abierto para extensión
class ServicioPagos {
    private final ProcesadorPago procesador;  // DIP: depende de abstracción
    private final ValidadorOrden validador;
    private final RegistradorTransacciones registrador;
    private final NotificadorPagos notificador;
    
    // Inyección de dependencias
    public ServicioPagos(
            ProcesadorPago procesador,
            ValidadorOrden validador,
            RegistradorTransacciones registrador,
            NotificadorPagos notificador) {
        this.procesador = procesador;
        this.validador = validador;
        this.registrador = registrador;
        this.notificador = notificador;
    }
    
    public ResultadoPago procesarPago(Usuario usuario, OrdenPago orden) {
        validador.validar(orden);
        ResultadoPago resultado = procesador.procesar(orden);
        registrador.registrar(resultado);
        notificador.notificar(usuario, resultado);
        return resultado;
    }
}
```

Para agregar un nuevo procesador (ej: criptomonedas):

1. Crear `ProcesadorCripto implements ProcesadorPago` ✓
2. Inyectarlo en `ServicioPagos` ✓
3. ¡Sin modificar código existente! ✓

---

(solid-antipatrones)=
## Antipatrones y Errores Comunes

(solid-sobre-ingenieria)=
### Sobre-Ingeniería por SOLID

Aplicar SOLID ciegamente puede llevar a código innecesariamente complejo:

```java
// ¿Realmente necesitamos todo esto para sumar dos números?

interface Sumador {
    int sumar(int a, int b);
}

interface SumadorFactory {
    Sumador crear();
}

class SumadorImpl implements Sumador {
    @Override
    public int sumar(int a, int b) {
        return a + b;
    }
}

class SumadorFactoryImpl implements SumadorFactory {
    @Override
    public Sumador crear() {
        return new SumadorImpl();
    }
}

// vs

class Matematica {
    static int sumar(int a, int b) {
        return a + b;
    }
}
```

:::{warning}
**YAGNI**: You Ain't Gonna Need It (No lo vas a necesitar)

No agregues abstracciones "por si acaso". Agregá complejidad cuando la necesités, no antes.
:::

(solid-mal-aplicados)=
### Principios Mal Aplicados

| Principio | Mal Aplicado | Consecuencia |
|-----------|--------------|--------------|
| SRP | Una clase por método | Explosión de clases |
| OCP | Interfaces para todo | Abstracción prematura |
| LSP | Evitar toda herencia | Duplicación de código |
| ISP | Interfaces de un método | Fragmentación excesiva |
| DIP | Inyectar todo | Configuración compleja |

(solid-balance)=
### Encontrar el Balance

:::{tip} Heurísticas de Balance

1. **Empezá simple**, refactorizá cuando duela
2. **Tres strikes** antes de generalizar
3. **Tests son la mejor documentación** de tus decisiones
4. **El código más fácil de cambiar** es el que no existe
5. **Preguntate**: ¿Esto hace el código más fácil de entender?
:::

---

(solid-resumen)=
## Resumen: Los Cinco Principios

```
┌─────────────────────────────────────────────────────────────────┐
│                         SOLID                                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  S - Single Responsibility                                      │
│      Una clase, una razón para cambiar                         │
│      → Evita clases "todoterreno"                              │
│                                                                 │
│  O - Open/Closed                                                │
│      Abierto para extensión, cerrado para modificación         │
│      → Usá polimorfismo para agregar comportamiento            │
│                                                                 │
│  L - Liskov Substitution                                        │
│      Subtipos deben ser sustituibles por sus supertipos        │
│      → Respetá los contratos al heredar                        │
│                                                                 │
│  I - Interface Segregation                                      │
│      Interfaces pequeñas y específicas                         │
│      → No fuerces a implementar lo que no se usa               │
│                                                                 │
│  D - Dependency Inversion                                       │
│      Dependé de abstracciones, no de implementaciones          │
│      → Inyectá dependencias, no las creés internamente         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

(solid-ejercicios)=
## Ejercicios

```{exercise}
:label: solid-ex-srp

**Identificar Violaciones de SRP**

Analizá la siguiente clase e identificá cuántas responsabilidades tiene. Luego, proponé una refactorización.

```java
public class Usuario {
    private String nombre;
    private String email;
    private String passwordHash;
    
    public void guardarEnBaseDeDatos() { ... }
    public void enviarEmailBienvenida() { ... }
    public boolean validarPassword(String password) { ... }
    public String generarReporteActividad() { ... }
    public void exportarAJSON() { ... }
    public void importarDesdeJSON(String json) { ... }
}
```
```

```{solution} solid-ex-srp
:class: dropdown

La clase tiene al menos **5 responsabilidades**:

1. **Datos del usuario** (nombre, email, password)
2. **Persistencia** (guardar en BD)
3. **Comunicación** (enviar email)
4. **Autenticación** (validar password)
5. **Serialización** (JSON import/export)

**Refactorización:**

```java
// Solo datos
public class Usuario {
    private String nombre;
    private String email;
    private String passwordHash;
    // getters/setters
}

// Persistencia
public class UsuarioRepository {
    public void guardar(Usuario usuario) { ... }
    public Usuario buscar(String email) { ... }
}

// Comunicación
public class NotificadorUsuario {
    public void enviarBienvenida(Usuario usuario) { ... }
}

// Autenticación
public class ServicioAutenticacion {
    public boolean validarCredenciales(Usuario u, String password) { ... }
}

// Serialización
public class UsuarioSerializer {
    public String toJSON(Usuario usuario) { ... }
    public Usuario fromJSON(String json) { ... }
}
```
```

```{exercise}
:label: solid-ex-ocp

**Aplicar OCP**

El siguiente código viola OCP. Refactorizalo para que agregar nuevos tipos de descuento no requiera modificar `CalculadorPrecio`.

```java
public class CalculadorPrecio {
    public double calcular(Producto producto, String tipoDescuento) {
        double precio = producto.getPrecioBase();
        
        if (tipoDescuento.equals("NINGUNO")) {
            return precio;
        } else if (tipoDescuento.equals("PORCENTAJE_10")) {
            return precio * 0.9;
        } else if (tipoDescuento.equals("PORCENTAJE_20")) {
            return precio * 0.8;
        } else if (tipoDescuento.equals("MONTO_FIJO")) {
            return precio - 50;
        }
        
        return precio;
    }
}
```
```

```{solution} solid-ex-ocp
:class: dropdown

```java
// Abstracción para descuentos
public interface Descuento {
    double aplicar(double precioBase);
}

// Implementaciones
public class SinDescuento implements Descuento {
    @Override
    public double aplicar(double precioBase) {
        return precioBase;
    }
}

public class DescuentoPorcentual implements Descuento {
    private final double porcentaje;
    
    public DescuentoPorcentual(double porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    @Override
    public double aplicar(double precioBase) {
        return precioBase * (1 - porcentaje / 100);
    }
}

public class DescuentoMontoFijo implements Descuento {
    private final double monto;
    
    public DescuentoMontoFijo(double monto) {
        this.monto = monto;
    }
    
    @Override
    public double aplicar(double precioBase) {
        return Math.max(0, precioBase - monto);
    }
}

// Calculador cerrado para modificación
public class CalculadorPrecio {
    public double calcular(Producto producto, Descuento descuento) {
        return descuento.aplicar(producto.getPrecioBase());
    }
}

// Agregar nuevo descuento: solo crear nueva clase
public class DescuentoBlackFriday implements Descuento {
    @Override
    public double aplicar(double precioBase) {
        return precioBase * 0.5;  // 50% off
    }
}
```
```

```{exercise}
:label: solid-ex-lsp

**Detectar Violación de LSP**

¿Por qué el siguiente diseño viola LSP? ¿Cómo lo corregirías?

```java
public class Ave {
    public void volar() {
        System.out.println("Volando...");
    }
}

public class Pinguino extends Ave {
    @Override
    public void volar() {
        throw new UnsupportedOperationException("Los pingüinos no vuelan");
    }
}
```
```

```{solution} solid-ex-lsp
:class: dropdown

**Violación de LSP:**

El código cliente que trabaja con `Ave` espera poder llamar a `volar()` sin excepciones. `Pinguino` rompe esta expectativa lanzando una excepción.

```java
void hacerVolarAves(List<Ave> aves) {
    for (Ave ave : aves) {
        ave.volar();  // ¡Explota con Pinguino!
    }
}
```

**Solución: Segregar la capacidad de volar**

```java
public interface Ave {
    void comer();
    void moverse();
}

public interface Volador {
    void volar();
}

public class Paloma implements Ave, Volador {
    @Override
    public void comer() { ... }
    
    @Override
    public void moverse() {
        volar();  // Las palomas se mueven volando
    }
    
    @Override
    public void volar() {
        System.out.println("Volando...");
    }
}

public class Pinguino implements Ave {
    @Override
    public void comer() { ... }
    
    @Override
    public void moverse() {
        System.out.println("Caminando/nadando...");
    }
    // No implementa Volador → no tiene volar()
}

// Código cliente correcto
void hacerVolarVoladores(List<Volador> voladores) {
    for (Volador v : voladores) {
        v.volar();  // Solo acepta cosas que vuelan
    }
}
```
```

```{exercise}
:label: solid-ex-isp

**Aplicar ISP**

La siguiente interfaz es "gorda". Segregala en interfaces más pequeñas y coherentes.

```java
public interface Trabajador {
    void trabajar();
    void comer();
    void dormir();
    void reportarHoras();
    void solicitarVacaciones();
    void calcularSalario();
    void administrarEquipo();
    void contratarPersonal();
    void despedirPersonal();
}
```
```

```{solution} solid-ex-isp
:class: dropdown

```java
// Actividades básicas de cualquier trabajador
public interface Trabajador {
    void trabajar();
}

// Necesidades humanas (no aplica a robots)
public interface SerHumano {
    void comer();
    void dormir();
}

// Gestión de tiempo
public interface EmpleadoConHorario {
    void reportarHoras();
    void solicitarVacaciones();
}

// Aspectos salariales
public interface Asalariado {
    void calcularSalario();
}

// Capacidades de liderazgo
public interface Gerente {
    void administrarEquipo();
}

// Capacidades de RRHH
public interface RecursosHumanos {
    void contratarPersonal();
    void despedirPersonal();
}

// Implementaciones
public class EmpleadoComun implements Trabajador, SerHumano, 
                                      EmpleadoConHorario, Asalariado {
    // Implementa lo que necesita
}

public class GerenteArea implements Trabajador, SerHumano, 
                                    EmpleadoConHorario, Asalariado, 
                                    Gerente {
    // Puede administrar equipo
}

public class DirectorRRHH implements Trabajador, SerHumano,
                                     EmpleadoConHorario, Asalariado,
                                     RecursosHumanos {
    // Puede contratar/despedir
}

public class RobotTrabajador implements Trabajador {
    // Solo trabaja, no come ni duerme
}
```
```

```{exercise}
:label: solid-ex-dip

**Refactorizar con DIP**

El siguiente código tiene dependencias invertidas incorrectamente. Refactorizalo aplicando DIP.

```java
public class GeneradorReportes {
    private MySQLDatabase database = new MySQLDatabase();
    private PDFWriter writer = new PDFWriter();
    private SMTPMailer mailer = new SMTPMailer();
    
    public void generarYEnviarReporte(String email) {
        String datos = database.query("SELECT * FROM ventas");
        byte[] pdf = writer.createPDF(datos);
        mailer.send(email, "Reporte", pdf);
    }
}
```
```

```{solution} solid-ex-dip
:class: dropdown

```java
// Abstracciones (definidas en el módulo de negocio)
public interface FuenteDatos {
    String obtenerDatos(String consulta);
}

public interface GeneradorDocumento {
    byte[] generar(String contenido);
}

public interface EnviadorMensajes {
    void enviar(String destinatario, String asunto, byte[] adjunto);
}

// Implementaciones (módulos de bajo nivel)
public class MySQLFuenteDatos implements FuenteDatos {
    @Override
    public String obtenerDatos(String consulta) {
        // Lógica MySQL
    }
}

public class GeneradorPDF implements GeneradorDocumento {
    @Override
    public byte[] generar(String contenido) {
        // Lógica PDF
    }
}

public class EnviadorSMTP implements EnviadorMensajes {
    @Override
    public void enviar(String destinatario, String asunto, byte[] adjunto) {
        // Lógica SMTP
    }
}

// Módulo de alto nivel con dependencias inyectadas
public class GeneradorReportes {
    private final FuenteDatos fuenteDatos;
    private final GeneradorDocumento generadorDoc;
    private final EnviadorMensajes enviador;
    
    public GeneradorReportes(
            FuenteDatos fuenteDatos,
            GeneradorDocumento generadorDoc,
            EnviadorMensajes enviador) {
        this.fuenteDatos = fuenteDatos;
        this.generadorDoc = generadorDoc;
        this.enviador = enviador;
    }
    
    public void generarYEnviarReporte(String email, String consulta) {
        String datos = fuenteDatos.obtenerDatos(consulta);
        byte[] documento = generadorDoc.generar(datos);
        enviador.enviar(email, "Reporte", documento);
    }
}

// Configuración (composición root)
FuenteDatos db = new MySQLFuenteDatos();
GeneradorDocumento gen = new GeneradorPDF();
EnviadorMensajes mail = new EnviadorSMTP();

GeneradorReportes reportes = new GeneradorReportes(db, gen, mail);
reportes.generarYEnviarReporte("usuario@email.com", "SELECT * FROM ventas");

// Para tests
GeneradorReportes reportesTest = new GeneradorReportes(
    mockFuenteDatos,
    mockGenerador,
    mockEnviador
);
```
```

---

## Lecturas Recomendadas

- Martin, R. C. (2003). *Agile Software Development: Principles, Patterns, and Practices*
- Martin, R. C. (2017). *Clean Architecture*
- Meyer, B. (1997). *Object-Oriented Software Construction* (2nd ed.)
- Fowler, M. (2018). *Refactoring: Improving the Design of Existing Code* (2nd ed.)
