---
title: "Diseño por Contratos"
subtitle: "Especificaciones Formales para Software Confiable"
subject: Programación Orientada a Objetos
---

(oop-contratos)=
# Diseño por Contratos

En los capítulos anteriores construimos objetos con buena estructura ({ref}`fundamentos-de-la-programacion-orientada-a-objetos`), establecimos relaciones claras ({ref}`oop2-encapsulamiento-relaciones`), y aprendimos sobre herencia y polimorfismo ({ref}`oop3-herencia-polimorfismo` y {ref}`java-herencia-polimorfismo`). Pero, ¿cómo garantizamos que esos objetos se **comporten correctamente**? ¿Cómo especificamos qué espera un método y qué promete entregar?

El **Diseño por Contratos** (Design by Contract, DbC) es una metodología que responde estas preguntas estableciendo **obligaciones y garantías formales** entre los objetos que colaboran. Este enfoque se relaciona estrechamente con el {ref}`principio-sustitucion-liskov` y con el manejo de {ref}`java-excepciones`.

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, serás capaz de:

1. Entender la filosofía del Diseño por Contratos
2. Definir precondiciones, postcondiciones e invariantes
3. Distinguir entre errores del cliente y errores del proveedor
4. Aplicar contratos para diseñar interfaces robustas
5. Manejar violaciones de contrato apropiadamente
6. Integrar contratos con el principio de sustitución de Liskov
:::

---

(filosofia-contratos)=
## La Filosofía del Contrato

(metafora-contrato-legal)=
### La Metáfora del Contrato Legal

Imaginá un contrato entre un cliente y un proveedor de servicios:

```{mermaid}
graph TB
    subgraph "CONTRATO DE SERVICIO"
        A[CLIENTE se compromete a:]
        A1[• Proveer dirección válida<br/>• Pagar monto acordado<br/>• Estar disponible]
        
        B[PROVEEDOR se compromete a:]
        B1[• Entregar en 48 horas<br/>• Producto en óptimas condiciones<br/>• Reembolso si no cumple]
        
        C[DURANTE TODO EL CONTRATO:]
        C1[• Comunicación respetuosa<br/>• Confidencialidad de datos]
        
        A --> A1
        B --> B1
        C --> C1
    end
    
    style A fill:#aaf,stroke:#333
    style B fill:#afa,stroke:#333
    style C fill:#faa,stroke:#333
```

En software, los **métodos** establecen contratos similares:

- **Precondiciones**: Lo que el cliente (quien llama) debe garantizar
- **Postcondiciones**: Lo que el proveedor (el método) garantiza si se cumplen las precondiciones
- **Invariantes**: Condiciones que siempre deben ser verdaderas

(origen-dbc)=
### Origen: Bertrand Meyer y Eiffel

El Diseño por Contratos fue formalizado por **Bertrand Meyer** en los años 80, como parte del lenguaje de programación **Eiffel**. Meyer se inspiró en la lógica de Hoare y las especificaciones formales.

:::{note} Cita de Bertrand Meyer

"Un sistema de software confiable es uno que hace lo que se supone que debe hacer. Para saber qué se supone que debe hacer, necesitamos una especificación."

— Bertrand Meyer, "Object-Oriented Software Construction"
:::

(beneficios-dbc)=
### Beneficios del Diseño por Contratos

1. **Documentación ejecutable**: Los contratos son especificaciones que se pueden verificar
2. **Depuración más fácil**: Las violaciones indican exactamente dónde está el error
3. **Responsabilidades claras**: Se sabe quién falló (cliente o proveedor)
4. **Diseño más robusto**: Fuerza a pensar en casos límite
5. **Herencia segura**: Define reglas para especialización correcta

---

(precondiciones)=
## Precondiciones: Lo que el Cliente Debe Garantizar

(definicion-precondicion)=
### Definición

Una **precondición** es una condición que **debe ser verdadera antes** de que se ejecute un método. Es la **obligación del cliente** (quien llama al método) garantizar que se cumple.

```
┌─────────────────────────────────────────┐
│              MÉTODO                     │
│─────────────────────────────────────────│
│                                         │
│  PRECONDICIÓN (responsabilidad cliente) │
│  ════════════════════════════════════   │
│  "Si me llamás, asegurate de que..."    │
│                                         │
│  • Parámetros válidos                   │
│  • Estado del objeto apropiado          │
│  • Recursos disponibles                 │
│                                         │
└─────────────────────────────────────────┘
```

(ejemplos-precondiciones)=
### Ejemplos de Precondiciones

#### División

```java
/**
 * Divide dos números.
 * 
 * @precondition divisor != 0
 */
double dividir(double dividendo, double divisor) {
    // Si divisor es 0, el cliente violó el contrato
    return dividendo / divisor;
}
```

#### Acceso a Colección

```java
/**
 * Obtiene el elemento en la posición indicada.
 * 
 * @precondition indice >= 0
 * @precondition indice < tamaño()
 */
Elemento obtener(int indice) {
    return elementos[indice];
}
```

#### Transferencia Bancaria

```
/**java
 * Transfiere dinero a otra cuenta.
 * 
 * @precondition monto > 0
 * @precondition monto <= saldo
 * @precondition cuentaDestino != null
 * @precondition cuentaDestino.estaActiva()
 */
void transferir(double monto, Cuenta cuentaDestino) {
    this.saldo -= monto;
    cuentaDestino.saldo += monto;
}
```

(verificacion-precondiciones)=
### Verificación de Precondiciones

Las precondiciones pueden verificarse de varias formas:

#### Con Assertions (desarrollo)

```java
void transferir(double monto, Cuenta destino) {
    assert monto > 0 : "Monto debe ser positivo";
    assert monto <= saldo : "Fondos insuficientes";
    assert destino != null : "Cuenta destino requerida";
    
    // ... lógica
}
```

#### Con Validación Explícita

```java
void transferir(double monto, Cuenta destino) {
    if (monto <= 0) {
        throw new IllegalArgumentException("Monto debe ser positivo");
    }
    if (monto > saldo) {
        throw new IllegalArgumentException("Fondos insuficientes");
    }
    if (destino == null) {
        throw new NullPointerException("Cuenta destino requerida");
    }
    
    // ... lógica
}
```

#### Con Método de Validación

```java
void transferir(double monto, Cuenta destino) {
    validarTransferencia(monto, destino);
    
    // ... lógica
}

private void validarTransferencia(double monto, Cuenta destino) {
    // Centraliza las validaciones
}
```

(quien-verifica-precondiciones)=
### ¿Quién Verifica las Precondiciones?

Esta es una pregunta de diseño importante:

| Enfoque | Ventajas | Desventajas |
| :--- | :--- | :--- |
| **El cliente verifica** | Evita llamadas innecesarias | Código duplicado si hay múltiples clientes |
| **El método verifica** | Defensa en profundidad | Overhead en cada llamada |
| **Ambos verifican** | Máxima seguridad | Redundancia, más código |

:::{tip} Recomendación Práctica

En desarrollo, verificá siempre (fail-fast). En producción, el nivel de verificación depende del contexto:

- **APIs públicas**: Siempre verificar (no confiás en el cliente)
- **Métodos privados**: Opcional (controlás las llamadas)
- **Código crítico**: Siempre verificar
:::

---

(postcondiciones)=
## Postcondiciones: Lo que el Método Garantiza

(definicion-postcondicion)=
### Definición

Una **postcondición** es una condición que **debe ser verdadera después** de que se ejecute un método (asumiendo que las precondiciones se cumplieron). Es la **obligación del proveedor** (el método) garantizar que se cumple.

```{mermaid}
classDiagram
    class CuentaBancaria {
        -double saldo
        +transferir(double, Cuenta)
    }
    
    note for CuentaBancaria "CONTRATO transferir():\n\nPRECONDICIONES (cliente):\n• monto > 0\n• monto <= saldo\n• destino != null\n\nPOSTCONDICIONES (método):\n• this.saldo = old(saldo) - monto\n• destino.saldo = old(destino.saldo) + monto\n• conservación: suma total igual"
```

(ejemplos-postcondiciones)=
### Ejemplos de Postcondiciones

#### Raíz Cuadrada

```java
/**
 * Calcula la raíz cuadrada de un número.
 * 
 * @precondition numero >= 0
 * @postcondition resultado >= 0
 * @postcondition resultado * resultado ≈ numero (con tolerancia)
 */
double raizCuadrada(double numero) {
    // Implementación...
    return resultado;
}
```

#### Agregar a Lista

```java
/**
 * Agrega un elemento al final de la lista.
 * 
 * @precondition elemento != null
 * @postcondition tamaño() == old(tamaño()) + 1
 * @postcondition obtener(tamaño() - 1) == elemento
 * @postcondition contiene(elemento) == true
 */
void agregar(Elemento elemento) {
    // Implementación...
}
```

:::{note}
La notación `old(expresion)` representa el valor de la expresión **antes** de ejecutar el método. Es fundamental para especificar cómo cambia el estado.
:::

#### Ordenar Lista

```java
/**
 * Ordena la lista en orden ascendente.
 * 
 * @postcondition para todo i en [0, tamaño()-1): 
 *                obtener(i) <= obtener(i+1)
 * @postcondition tamaño() == old(tamaño())
 * @postcondition contiene exactamente los mismos elementos que antes
 */
void ordenar() {
    // Implementación...
}
```

#### Transferencia Bancaria (completo)

```java
/**
 * Transfiere dinero a otra cuenta.
 * 
 * @precondition monto > 0
 * @precondition monto <= saldo
 * @precondition destino != null
 * 
 * @postcondition this.saldo == old(this.saldo) - monto
 * @postcondition destino.saldo == old(destino.saldo) + monto
 * @postcondition old(this.saldo) + old(destino.saldo) == 
 *                this.saldo + destino.saldo  // Conservación del dinero
 */
void transferir(double monto, Cuenta destino) {
    this.saldo -= monto;
    destino.saldo += monto;
}
```

(postcondiciones-excepcionales)=
### Postcondiciones Excepcionales

¿Qué pasa cuando un método puede fallar legítimamente (no por violación de contrato)?

```java
/**
 * Lee el contenido de un archivo.
 * 
 * @precondition ruta != null
 * @precondition ruta no está vacía
 * 
 * @postcondition.normal resultado contiene el contenido del archivo
 * @postcondition.excepcional si archivo no existe: 
 *                            lanza ArchivoNoEncontradoException
 * @postcondition.excepcional si no hay permisos: 
 *                            lanza PermisoNegadoException
 */
String leerArchivo(String ruta) throws ArchivoNoEncontradoException, 
                                       PermisoNegadoException {
    // ...
}
```

---

(invariantes)=
## Invariantes de Clase: Lo que Siempre Debe Ser Verdad

(definicion-invariante)=
### Definición

Un **invariante de clase** es una condición que **siempre debe ser verdadera** para todas las instancias de una clase, en todo momento observable (entre llamadas a métodos públicos).

```
┌─────────────────────────────────────────────────────────┐
│                     CLASE                               │
│─────────────────────────────────────────────────────────│
│                                                         │
│  INVARIANTE DE CLASE                                    │
│  ═══════════════════                                    │
│  "En todo momento, estas condiciones son verdaderas..." │
│                                                         │
│  • Relaciones entre atributos                          │
│  • Restricciones de dominio                            │
│  • Consistencia interna                                │
│                                                         │
│  Se verifica:                                           │
│  ✓ Después del constructor                              │
│  ✓ Antes y después de cada método público               │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

(ejemplos-invariantes)=
### Ejemplos de Invariantes

#### Cuenta Bancaria

```java
/**
 * Representa una cuenta bancaria.
 * 
 * @invariant saldo >= 0
 * @invariant numero != null && numero.length() == 20
 * @invariant titular != null
 * @invariant fechaApertura <= fechaActual
 */
class CuentaBancaria {
    private double saldo;
    private String numero;
    private Cliente titular;
    private LocalDate fechaApertura;
    
    // Todos los métodos deben preservar estos invariantes
}
```

#### Fracción

```java
/**
 * Representa una fracción matemática.
 * 
 * @invariant denominador != 0
 * @invariant denominador > 0  // Normalizamos: signo en numerador
 * @invariant mcd(|numerador|, denominador) == 1  // Siempre simplificada
 */
class Fraccion {
    private int numerador;
    private int denominador;
    
    Fraccion(int num, int den) {
        // Debe establecer el invariante
        if (den == 0) throw new IllegalArgumentException("Denominador cero");
        
        // Normalizar signo
        if (den < 0) {
            num = -num;
            den = -den;
        }
        
        // Simplificar
        int divisor = mcd(Math.abs(num), den);
        this.numerador = num / divisor;
        this.denominador = den / divisor;
    }
    
    Fraccion sumar(Fraccion otra) {
        // Debe preservar el invariante en el resultado
        return new Fraccion(
            this.numerador * otra.denominador + otra.numerador * this.denominador,
            this.denominador * otra.denominador
        );
    }
}
```

#### Intervalo

```java
/**
 * Representa un intervalo cerrado [inicio, fin].
 * 
 * @invariant inicio <= fin
 */
class Intervalo {
    private double inicio;
    private double fin;
    
    Intervalo(double inicio, double fin) {
        if (inicio > fin) {
            throw new IllegalArgumentException("Inicio debe ser <= fin");
        }
        this.inicio = inicio;
        this.fin = fin;
    }
    
    double longitud() {
        return fin - inicio;  // Siempre >= 0 por invariante
    }
    
    boolean contiene(double valor) {
        return valor >= inicio && valor <= fin;
    }
}
```

#### Lista Ordenada

```java
/**
 * Lista que mantiene sus elementos ordenados.
 * 
 * @invariant para todo i en [0, tamaño()-1): 
 *            obtener(i) <= obtener(i+1)
 */
class ListaOrdenada<T extends Comparable<T>> {
    private List<T> elementos;
    
    void agregar(T elemento) {
        // No puede simplemente agregar al final
        // Debe insertar en la posición correcta para mantener el invariante
        int pos = encontrarPosicion(elemento);
        elementos.add(pos, elemento);
    }
}
```

(invariantes-temporales)=
### Invariantes y Momentos de Verificación

El invariante no necesita ser verdadero **en todo instante**, sino en **momentos observables**:

```java
void transferir(double monto, Cuenta destino) {
    // INVARIANTE: saldo >= 0 ✓ (verdadero al entrar)
    
    this.saldo -= monto;
    // MOMENTO INTERMEDIO: ¡invariante podría ser falso temporalmente!
    // Esto es aceptable porque es inobservable
    
    destino.saldo += monto;
    
    // INVARIANTE: saldo >= 0 ✓ (debe ser verdadero al salir)
}
```

:::{warning} **Problema de Concurrencia**

En programas multihilo, los "momentos intermedios" pueden ser observados por otros hilos. Esto requiere sincronización adicional para mantener los invariantes.
:::

---

(contrato-completo)=
## El Contrato Completo

(estructura-contrato)=
### Estructura de un Contrato

Un contrato completo tiene tres partes:

```{mermaid}
graph TB
    subgraph "CONTRATO COMPLETO"
        A[PRECONDICIONES]
        A1[Obligación del CLIENTE<br/>─────────────────<br/>• Parámetros válidos<br/>• Estado apropiado<br/>• Recursos disponibles]
        
        B[POSTCONDICIONES]
        B1[Obligación del MÉTODO<br/>─────────────────<br/>• Valor de retorno correcto<br/>• Estado modificado<br/>• Efectos secundarios]
        
        C[INVARIANTES]
        C1[Siempre verdadero<br/>─────────────────<br/>• Condiciones sobre estado<br/>• Antes y después del método<br/>• En toda la vida del objeto]
        
        A --> A1
        B --> B1
        C --> C1
    end
    
    style A fill:#aaf,stroke:#333
    style B fill:#afa,stroke:#333
    style C fill:#faa,stroke:#333
```


(ejemplo-pila-contrato)=
### Ejemplo Completo: Pila con Contratos

```java
/**
 * Pila LIFO (Last In, First Out) con capacidad limitada.
 * 
 * @invariant tamaño() >= 0
 * @invariant tamaño() <= capacidad()
 * @invariant capacidad() > 0
 * @invariant estaVacia() == (tamaño() == 0)
 * @invariant estaLlena() == (tamaño() == capacidad())
 */
class Pila<T> {
    private T[] elementos;
    private int tope;
    private int capacidad;
    
    /**
     * Crea una pila con la capacidad especificada.
     * 
     * @precondition capacidad > 0
     * @postcondition tamaño() == 0
     * @postcondition capacidad() == capacidad
     * @postcondition estaVacia() == true
     */
    Pila(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("Capacidad debe ser positiva");
        }
        this.elementos = (T[]) new Object[capacidad];
        this.tope = -1;
        this.capacidad = capacidad;
    }
    
    /**
     * Apila un elemento.
     * 
     * @precondition elemento != null
     * @precondition !estaLlena()
     * @postcondition tamaño() == old(tamaño()) + 1
     * @postcondition tope() == elemento
     * @postcondition !estaVacia()
     */
    void apilar(T elemento) {
        if (elemento == null) {
            throw new NullPointerException("Elemento no puede ser null");
        }
        if (estaLlena()) {
            throw new IllegalStateException("Pila llena");
        }
        elementos[++tope] = elemento;
    }
    
    /**
     * Desapila y retorna el elemento del tope.
     * 
     * @precondition !estaVacia()
     * @postcondition tamaño() == old(tamaño()) - 1
     * @postcondition resultado == old(tope())
     * @postcondition !estaLlena()
     */
    T desapilar() {
        if (estaVacia()) {
            throw new IllegalStateException("Pila vacía");
        }
        T elemento = elementos[tope];
        elementos[tope--] = null;  // Ayuda al GC
        return elemento;
    }
    
    /**
     * Consulta el elemento del tope sin removerlo.
     * 
     * @precondition !estaVacia()
     * @postcondition resultado == el último elemento apilado
     * @postcondition tamaño() == old(tamaño())  // No modifica
     */
    T tope() {
        if (estaVacia()) {
            throw new IllegalStateException("Pila vacía");
        }
        return elementos[tope];
    }
    
    /**
     * @postcondition resultado == cantidad de elementos en la pila
     */
    int tamaño() {
        return tope + 1;
    }
    
    /**
     * @postcondition resultado == (tamaño() == 0)
     */
    boolean estaVacia() {
        return tope < 0;
    }
    
    /**
     * @postcondition resultado == (tamaño() == capacidad())
     */
    boolean estaLlena() {
        return tope >= capacidad - 1;
    }
}
```

---

(responsabilidades-cliente-proveedor)=
## Responsabilidades: Cliente vs Proveedor

(modelo-cliente-proveedor)=
### El Modelo Cliente-Proveedor

```
┌─────────────┐                    ┌─────────────┐
│   CLIENTE   │                    │  PROVEEDOR  │
│  (quien     │─────llamada───────▶│  (método    │
│   llama)    │                    │   llamado)  │
└─────────────┘                    └─────────────┘
      │                                   │
      │ Responsabilidad:                  │ Responsabilidad:
      │ PRECONDICIONES                    │ POSTCONDICIONES
      │                                   │
      ▼                                   ▼
┌─────────────────┐              ┌─────────────────┐
│ "Debo asegurar  │              │ "Si el cliente  │
│ que las         │              │ cumplió, yo     │
│ precondiciones  │              │ garantizo las   │
│ se cumplan"     │              │ postcondiciones"│
└─────────────────┘              └─────────────────┘
```

(violacion-contrato)=
### ¿Qué Pasa Cuando se Viola el Contrato?

| Violación | Responsable | Acción |
| :--- | :--- | :--- |
| Precondición no cumplida | **Cliente** | Bug en el código que llama |
| Postcondición no cumplida | **Proveedor** | Bug en el método |
| Invariante violado | **Proveedor** | Bug en la clase |

(ejemplo-violacion)=
### Ejemplo: Identificando Responsables

```java
class Calculadora {
    /**
     * @precondition b != 0
     * @postcondition resultado * b == a (aproximadamente)
     */
    double dividir(double a, double b) {
        return a / b;
    }
}

// Escenario 1: Cliente viola precondición
Calculadora calc = new Calculadora();
double resultado = calc.dividir(10, 0);  // ❌ ERROR DEL CLIENTE
// El cliente debería haber verificado que b != 0

// Escenario 2: Proveedor viola postcondición (hipotético bug)
double dividir(double a, double b) {
    return a + b;  // ❌ ERROR DEL PROVEEDOR - retorna suma, no división
}
```

(programacion-defensiva-vs-dbc)=
### Programación Defensiva vs Diseño por Contratos

Hay dos filosofías diferentes:

**Programación Defensiva**: "No confío en nadie"
```
void procesar(String dato) {
    if (dato == null) {
        dato = "";  // "Corrijo" silenciosamente
    }
    if (dato.isEmpty()) {
        return;  // No hago nada
    }
    // Proceso...
}
```

**Diseño por Contratos**: "Cumplí tu parte"
```
/**
 * @precondition dato != null
 * @precondition !dato.isEmpty()
 */
void procesar(String dato) {
    assert dato != null && !dato.isEmpty();
    // Proceso... si falla, es culpa del cliente
}
```

:::{note} ¿Cuál es Mejor?

Depende del contexto:

- **APIs públicas/librerías**: Defensiva (no controlás quién llama)
- **Código interno**: DbC (equipos coordinados, contratos claros)
- **Código crítico**: Ambas (cinturón y tiradores)
:::

---

(contratos-herencia)=
## Contratos y Herencia: El Principio de Liskov Revisitado

(reglas-herencia-contratos)=
### Reglas para Subtipos

Cuando una subclase sobrescribe un método, debe respetar ciertas reglas para mantener la sustituibilidad:

```
┌─────────────────────────────────────────────────────────────┐
│              REGLAS DE CONTRATOS EN HERENCIA                │
│═════════════════════════════════════════════════════════════│
│                                                             │
│  PRECONDICIONES: Pueden ser IGUALES o MÁS DÉBILES           │
│  ─────────────────────────────────────────────────          │
│  La subclase puede aceptar MÁS casos que la superclase      │
│  (pero nunca rechazar casos que la superclase acepta)       │
│                                                             │
│  POSTCONDICIONES: Pueden ser IGUALES o MÁS FUERTES          │
│  ──────────────────────────────────────────────────         │
│  La subclase puede garantizar MÁS que la superclase         │
│  (pero nunca menos de lo que la superclase garantiza)       │
│                                                             │
│  INVARIANTES: Deben PRESERVARSE y pueden AGREGARSE          │
│  ────────────────────────────────────────────────           │
│  La subclase hereda los invariantes de la superclase        │
│  y puede agregar los suyos propios                          │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

(ejemplo-precondiciones-debiles)=
### Ejemplo: Precondiciones Más Débiles (Correcto)

```
class Calculadora {
    /**
     * @precondition numero >= 0
     */
    double raiz(double numero) {
        return Math.sqrt(numero);
    }
}

class CalculadoraCientifica extends Calculadora {
    /**
     * @precondition numero puede ser cualquier valor (incluidos negativos)
     * 
     * Precondición MÁS DÉBIL: acepta más casos ✓
     */
    @Override
    double raiz(double numero) {
        if (numero >= 0) {
            return Math.sqrt(numero);
        } else {
            // Retorna parte imaginaria (números complejos)
            return Math.sqrt(-numero);  // Simplificado
        }
    }
}

// Código cliente que usa Calculadora
void procesar(Calculadora calc) {
    double r = calc.raiz(4);  // Funciona con ambas
}
// Si recibe CalculadoraCientifica, sigue funcionando ✓
```

(ejemplo-precondiciones-fuertes-mal)=
### Ejemplo: Precondiciones Más Fuertes (Incorrecto)

```java
class Coleccion {
    /**
     * @precondition elemento != null
     */
    void agregar(Object elemento) {
        // ...
    }
}

class ColeccionEstricta extends Coleccion {
    /**
     * @precondition elemento != null
     * @precondition elemento instanceof String  // ❌ MÁS FUERTE
     */
    @Override
    void agregar(Object elemento) {
        if (!(elemento instanceof String)) {
            throw new IllegalArgumentException("Solo Strings");
        }
        // ...
    }
}

// Código cliente
void llenar(Coleccion col) {
    col.agregar(42);  // Válido según contrato de Coleccion
}

// Si recibe ColeccionEstricta, ¡falla! ❌
// Viola el principio de sustitución
```

(ejemplo-postcondiciones-fuertes)=
### Ejemplo: Postcondiciones Más Fuertes (Correcto)

```java
class Buscador {
    /**
     * @postcondition resultado contiene elementos que matchean
     */
    List<Resultado> buscar(String query) {
        // Búsqueda básica
        return resultados;
    }
}

class BuscadorOrdenado extends Buscador {
    /**
     * @postcondition resultado contiene elementos que matchean
     * @postcondition resultado está ordenado por relevancia  // MÁS FUERTE ✓
     */
    @Override
    List<Resultado> buscar(String query) {
        List<Resultado> resultados = super.buscar(query);
        Collections.sort(resultados, porRelevancia);
        return resultados;
    }
}

// El código cliente espera resultados, y recibe resultados ORDENADOS
// Más de lo esperado: ✓ compatible
```

(covarianza-contravarianza)=
### Relación con Covarianza y Contravarianza

Las reglas de contratos en herencia se relacionan con:

- **Contravarianza de precondiciones**: Los parámetros pueden ser más generales
- **Covarianza de postcondiciones**: Los resultados pueden ser más específicos

```java
class Animal {
    /**
     * @precondition comida instanceof Alimento
     * @postcondition estado es mejor o igual
     */
    void comer(Alimento comida) { }
}

class Gato extends Animal {
    /**
     * @precondition comida instanceof Alimento (o más general)
     * @postcondition estado es mejor (más específico) ✓
     */
    @Override
    void comer(Alimento comida) {
        // Puede aceptar cualquier Alimento
        // Pero garantiza mejora específica
    }
}
```

---

(contratos-practica)=
## Contratos en la Práctica

(documentacion-contratos)=
### Documentación de Contratos

Los contratos se documentan típicamente en los comentarios:

```java
/**
 * Calcula el factorial de un número.
 * 
 * <p>El factorial de n (n!) es el producto de todos los
 * enteros positivos menores o iguales a n.</p>
 * 
 * @param n el número del cual calcular el factorial
 * @return el factorial de n
 * 
 * @precondition n >= 0
 * @precondition n <= 20 (para evitar overflow en long)
 * 
 * @postcondition resultado >= 1
 * @postcondition resultado == n * (n-1) * ... * 1 para n > 0
 * @postcondition resultado == 1 para n == 0
 * 
 * @throws IllegalArgumentException si n < 0 o n > 20
 */
long factorial(int n) {
    if (n < 0 || n > 20) {
        throw new IllegalArgumentException("n debe estar en [0, 20]");
    }
    
    long resultado = 1;
    for (int i = 2; i <= n; i++) {
        resultado *= i;
    }
    return resultado;
}
```

(assertions-java)=
### Uso de Assertions en Java

Java provee `assert` para verificar condiciones:

```
void metodo(int valor) {
    // Precondición
    assert valor > 0 : "valor debe ser positivo";
    
    // ... lógica ...
    
    int resultado = calcular();
    
    // Postcondición
    assert resultado >= 0 : "resultado no puede ser negativo";
}
```

:::{warning}
Las assertions están **deshabilitadas por defecto** en Java. Para habilitarlas:

```bash
java -ea MiPrograma        # Enable assertions
java -ea:mi.paquete... MiPrograma  # Solo en un paquete
```

No uses assertions para validar entrada de usuario o APIs públicas.
:::

(bibliotecas-contratos)=
### Bibliotecas para Contratos

Existen bibliotecas que facilitan la verificación de contratos:

**Google Guava (Preconditions)**
```
import static com.google.common.base.Preconditions.*;

void transferir(double monto, Cuenta destino) {
    checkArgument(monto > 0, "Monto debe ser positivo: %s", monto);
    checkArgument(monto <= saldo, "Fondos insuficientes");
    checkNotNull(destino, "Cuenta destino requerida");
    
    // ...
}
```

**Apache Commons (Validate)**
```
import org.apache.commons.lang3.Validate;

void transferir(double monto, Cuenta destino) {
    Validate.isTrue(monto > 0, "Monto debe ser positivo");
    Validate.isTrue(monto <= saldo, "Fondos insuficientes");
    Validate.notNull(destino, "Cuenta destino requerida");
    
    // ...
}
```

(testing-contratos)=
### Testing y Contratos

Los contratos guían la escritura de tests:

```
// Test de precondiciones (casos límite inválidos)
@Test(expected = IllegalArgumentException.class)
void dividir_divisorCero_lanzaExcepcion() {
    calculadora.dividir(10, 0);
}

// Test de postcondiciones (verificar garantías)
@Test
void dividir_valoresValidos_resultadoCorrecto() {
    double resultado = calculadora.dividir(10, 2);
    assertEquals(5.0, resultado, 0.001);
    assertEquals(10.0, resultado * 2, 0.001);  // Verifica postcondición
}

// Test de invariantes (estado consistente)
@Test
void pila_operacionesVarias_invariantesPreservados() {
    Pila<Integer> pila = new Pila<>(10);
    
    pila.apilar(1);
    pila.apilar(2);
    assertTrue(pila.tamaño() >= 0);
    assertTrue(pila.tamaño() <= pila.capacidad());
    
    pila.desapilar();
    assertTrue(pila.tamaño() >= 0);
    assertTrue(pila.tamaño() <= pila.capacidad());
}
```

---

(null-contratos)=
## El Problema del Null y los Contratos

(null-como-violacion)=
### Null como Fuente de Violaciones

`null` es una fuente constante de violaciones de contrato:

```
// ¿Qué significa retornar null?
Usuario buscar(String id) {
    // ¿null significa "no encontrado"?
    // ¿O significa "error"?
    // ¿O el id era inválido?
}

// ¿Qué pasa si el parámetro es null?
void procesar(Usuario usuario) {
    usuario.getNombre();  // NullPointerException
}
```

(estrategias-null)=
### Estrategias para Manejar Null

#### 1. Prohibir null explícitamente

```
/**
 * @precondition usuario != null
 * @postcondition resultado != null
 */
String formatear(Usuario usuario) {
    Objects.requireNonNull(usuario, "Usuario no puede ser null");
    return usuario.getNombre() + " - " + usuario.getEmail();
}
```

#### 2. Usar Optional

```
/**
 * @postcondition resultado.isPresent() si el usuario existe
 * @postcondition resultado.isEmpty() si no existe
 */
Optional<Usuario> buscar(String id) {
    Usuario u = baseDatos.buscar(id);
    return Optional.ofNullable(u);
}

// Uso
Optional<Usuario> usuario = buscar("123");
usuario.ifPresent(u -> System.out.println(u.getNombre()));
String nombre = usuario.map(Usuario::getNombre).orElse("Anónimo");
```

#### 3. Patrón Null Object

```
interface Usuario {
    String getNombre();
    boolean esReal();
}

class UsuarioReal implements Usuario {
    String getNombre() { return this.nombre; }
    boolean esReal() { return true; }
}

class UsuarioNulo implements Usuario {
    String getNombre() { return "Anónimo"; }
    boolean esReal() { return false; }
}

// Nunca retorna null
Usuario buscar(String id) {
    Usuario u = baseDatos.buscar(id);
    return u != null ? u : new UsuarioNulo();
}
```

---

(resumen-oop5)=
## Resumen

### Elementos del Contrato

| Elemento | Responsable | Cuándo se Verifica |
| :--- | :--- | :--- |
| **Precondición** | Cliente | Antes del método |
| **Postcondición** | Proveedor | Después del método |
| **Invariante** | Clase | Antes y después de métodos públicos |

### Reglas en Herencia

| Elemento | Regla en Subclases |
| :--- | :--- |
| Precondiciones | Iguales o más débiles |
| Postcondiciones | Iguales o más fuertes |
| Invariantes | Se heredan y pueden agregarse |

### Beneficios Clave

1. **Claridad**: Responsabilidades explícitas
2. **Robustez**: Errores detectados temprano
3. **Documentación**: Especificación ejecutable
4. **Debugging**: Localización precisa de fallos
5. **Diseño**: Fuerza a pensar en casos límite

---

(ejercicios-oop5)=
## Ejercicios

```{exercise}
:label: ej-contrato-cola
Especificá el contrato completo (precondiciones, postcondiciones e invariantes) para una clase `Cola<T>` con operaciones `encolar`, `desencolar`, `frente`, `estaVacia` y `tamaño`.
```

```{exercise}
:label: ej-contrato-rectangulo
Diseñá una clase `Rectangulo` con contratos apropiados. Considerá:
- Invariantes sobre ancho y alto
- Precondiciones del constructor
- Postcondiciones de métodos como `area()`, `perimetro()`, `escalar(factor)`
- ¿Qué pasa si querés agregar una subclase `Cuadrado`? Analizá las implicaciones de LSP.
```

```{exercise}
:label: ej-violacion-contrato
Dado el siguiente código, identificá qué tipo de violación de contrato ocurre (precondición, postcondición o invariante) y quién es el responsable:

1. `lista.obtener(-1)` donde obtener requiere índice >= 0
2. Un método `ordenar()` que deja la lista desordenada
3. Una cuenta bancaria que después de un depósito tiene saldo negativo
4. Llamar a `pila.desapilar()` en una pila vacía
5. Un constructor de `Fraccion(1, 0)`
```

```{exercise}
:label: ej-herencia-contrato
Analizá si las siguientes redefiniciones de métodos respetan las reglas de contratos en herencia:

1. Clase base: `buscar(String query)` requiere query.length() >= 3
   Subclase: `buscar(String query)` requiere query.length() >= 1
   
2. Clase base: `calcular(int n)` garantiza resultado > 0
   Subclase: `calcular(int n)` garantiza resultado >= 0
   
3. Clase base: `procesar(Object o)` acepta cualquier Object
   Subclase: `procesar(Object o)` solo acepta String
```

````{exercise}
:label: ej-refactoring-contratos
Refactorizá el siguiente código para usar Diseño por Contratos apropiadamente:

```text
String procesarPedido(Pedido pedido, Cliente cliente, String direccion) {
    if (pedido == null) return "Error";
    if (cliente == null) return "Error";
    if (direccion == null || direccion.isEmpty()) return "Error";
    if (pedido.getItems().isEmpty()) return "Error";
    if (!cliente.estaActivo()) return "Error";
    if (pedido.getTotal() <= 0) return "Error";
    
    // ... procesar ...
    return "OK";
}
```

¿Qué condiciones son precondiciones? ¿Cuáles deberían ser invariantes de clase? ¿Cómo documentarías el contrato?
````
