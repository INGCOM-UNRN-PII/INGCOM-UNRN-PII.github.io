---
title: "11: Testing en Programación Orientada a Objetos"
subtitle: "Verificación y Validación de Sistemas Orientados a Objetos"
subject: Programación Orientada a Objetos
---

(oop-testing)=
# OOP 8: Testing en Programación Orientada a Objetos

En los capítulos anteriores diseñamos sistemas orientados a objetos aplicando contratos ({ref}`oop-contratos`), patrones de diseño ({ref}`oop5-patrones-diseno`), principios SOLID ({ref}`oop-solid`) y técnicas de refactoring ({ref}`oop-refactoring`). Pero, ¿cómo sabemos que nuestro código **funciona correctamente**? ¿Cómo nos aseguramos de que los cambios futuros **no rompan funcionalidad existente**?

El **testing** es la disciplina que permite verificar el comportamiento del software de manera sistemática y repetible. En el contexto de la programación orientada a objetos, el testing presenta desafíos y oportunidades particulares debido a las características del paradigma: encapsulamiento, herencia, polimorfismo y la interacción entre objetos.

:::{admonition} Objetivos de Aprendizaje
:class: tip

Al finalizar este capítulo, serás capaz de:

1. Comprender los fundamentos del testing de software
2. Escribir tests unitarios efectivos para clases y objetos
3. Aplicar Test-Driven Development (TDD) en el diseño de objetos
4. Utilizar dobles de prueba (mocks, stubs, spies) para aislar componentes
5. Diseñar código orientado a objetos que sea fácilmente testeable
6. Relacionar testing con los principios SOLID y patrones de diseño
:::

---

(fundamentos-testing)=
## Fundamentos del Testing

(por-que-testing)=
### ¿Por qué Testing?

El software sin tests es como un puente sin inspección estructural: puede funcionar hoy, pero no tenemos garantías sobre mañana.

```
┌─────────────────────────────────────────────────────────────────┐
│                    CÓDIGO SIN TESTS                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   "Funciona en mi máquina"                                      │
│           │                                                     │
│           ▼                                                     │
│   ┌───────────────┐                                             │
│   │   Cambio en   │                                             │
│   │   el código   │                                             │
│   └───────┬───────┘                                             │
│           │                                                     │
│           ▼                                                     │
│   ┌───────────────┐     ┌───────────────┐                       │
│   │   ¿Sigue      │ ──▶ │     ???       │                       │
│   │   funcionando?│     │  Nadie sabe   │                       │
│   └───────────────┘     └───────────────┘                       │
│                                                                 │
│   Resultado: Miedo a cambiar, código congelado, bugs ocultos   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

```
┌─────────────────────────────────────────────────────────────────┐
│                    CÓDIGO CON TESTS                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   Tests automatizados verifican comportamiento                  │
│           │                                                     │
│           ▼                                                     │
│   ┌───────────────┐                                             │
│   │   Cambio en   │                                             │
│   │   el código   │                                             │
│   └───────┬───────┘                                             │
│           │                                                     │
│           ▼                                                     │
│   ┌───────────────┐     ┌───────────────┐                       │
│   │   Ejecutar    │ ──▶ │   VERDE ✓     │  Confianza            │
│   │   tests       │     │   o ROJO ✗    │  para cambiar         │
│   └───────────────┘     └───────────────┘                       │
│                                                                 │
│   Resultado: Confianza, refactoring seguro, documentación viva │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

**Beneficios del testing**:

1. **Verificación**: Confirmar que el código hace lo esperado
2. **Prevención de regresiones**: Detectar cuando un cambio rompe algo
3. **Documentación**: Los tests muestran cómo usar el código
4. **Diseño**: Escribir tests primero mejora el diseño
5. **Confianza**: Permite refactorizar sin miedo

(niveles-testing)=
### Niveles de Testing

```
┌─────────────────────────────────────────────────────────────────┐
│                   PIRÁMIDE DE TESTING                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│                         /\                                      │
│                        /  \       E2E / UI Tests               │
│                       /    \      (pocos, lentos, frágiles)    │
│                      /──────\                                   │
│                     /        \                                  │
│                    /  Tests   \   Integration Tests            │
│                   /   de       \  (algunos, moderados)         │
│                  / Integración  \                               │
│                 /────────────────\                              │
│                /                  \                             │
│               /   Tests Unitarios  \  Unit Tests               │
│              /                      \ (muchos, rápidos,        │
│             /________________________\  estables)               │
│                                                                 │
│   Ejecutar frecuentemente ◀──────────────────▶ Ejecutar menos  │
│   Rápidos, aislados                            Lentos, reales  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

| Nivel | Qué prueba | Características |
|-------|------------|-----------------|
| **Unitarios** | Una clase o método aislado | Rápidos, muchos, sin dependencias externas |
| **Integración** | Interacción entre componentes | Más lentos, verifican que partes trabajen juntas |
| **E2E (End-to-End)** | Sistema completo | Lentos, frágiles, simulan usuario real |

En este capítulo nos enfocamos en **tests unitarios**, fundamentales para el desarrollo orientado a objetos.

(anatomia-test)=
### Anatomía de un Test

Un test unitario sigue el patrón **AAA** (Arrange-Act-Assert):

```java
@Test
public void testSumaPositivos() {
    // ARRANGE (Preparar)
    Calculadora calc = new Calculadora();
    
    // ACT (Actuar)
    int resultado = calc.sumar(2, 3);
    
    // ASSERT (Verificar)
    assertEquals(5, resultado);
}
```

**Arrange**: Configurar el escenario inicial
**Act**: Ejecutar la acción a probar
**Assert**: Verificar el resultado esperado

:::{tip}
Otra nomenclatura común es **Given-When-Then** (Dado-Cuando-Entonces), más legible para tests de comportamiento:

- **Given** (Dado): Estado inicial
- **When** (Cuando): Acción ejecutada
- **Then** (Entonces): Resultado esperado
:::

---

(testing-clases-objetos)=
## Testing de Clases y Objetos

(que-testear)=
### ¿Qué Testear en una Clase?

Cuando testeamos una clase, nos enfocamos en su **comportamiento observable**, no en sus detalles de implementación.

```
┌─────────────────────────────────────────────────────────────────┐
│                    ¿QUÉ TESTEAR?                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ✓ TESTEAR                        ✗ NO TESTEAR                │
│   ──────────────────────           ─────────────────────       │
│   • Métodos públicos               • Métodos privados          │
│   • Comportamiento observable      • Detalles internos         │
│   • Contratos (pre/post)           • Implementación            │
│   • Casos límite                   • Getters/setters triviales │
│   • Manejo de errores              • Código de terceros        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

:::{admonition} Principio Fundamental
:class: important

**Testear comportamiento, no implementación.**

Si cambiás la implementación interna pero el comportamiento observable sigue igual, los tests no deberían romperse.
:::

(ejemplo-cuenta-bancaria)=
### Ejemplo: Testing de una Cuenta Bancaria

Comencemos con una clase simple:

```java
public class CuentaBancaria {
    private String titular;
    private double saldo;
    
    public CuentaBancaria(String titular, double saldoInicial) {
        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException("Titular requerido");
        }
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("Saldo inicial no puede ser negativo");
        }
        this.titular = titular;
        this.saldo = saldoInicial;
    }
    
    public void depositar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("Monto debe ser positivo");
        }
        this.saldo += monto;
    }
    
    public void extraer(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("Monto debe ser positivo");
        }
        if (monto > saldo) {
            throw new SaldoInsuficienteException(saldo, monto);
        }
        this.saldo -= monto;
    }
    
    public double getSaldo() {
        return saldo;
    }
    
    public String getTitular() {
        return titular;
    }
}
```

**Tests para la clase CuentaBancaria**:

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

class CuentaBancariaTest {
    
    @Nested
    @DisplayName("Creación de cuenta")
    class CreacionCuenta {
        
        @Test
        @DisplayName("Se crea con titular y saldo inicial")
        void crearCuentaConDatosValidos() {
            CuentaBancaria cuenta = new CuentaBancaria("Juan Pérez", 1000);
            
            assertEquals("Juan Pérez", cuenta.getTitular());
            assertEquals(1000, cuenta.getSaldo());
        }
        
        @Test
        @DisplayName("Se puede crear con saldo inicial cero")
        void crearCuentaConSaldoCero() {
            CuentaBancaria cuenta = new CuentaBancaria("María García", 0);
            
            assertEquals(0, cuenta.getSaldo());
        }
        
        @Test
        @DisplayName("Rechaza titular nulo")
        void rechazaTitularNulo() {
            assertThrows(IllegalArgumentException.class, () -> 
                new CuentaBancaria(null, 100)
            );
        }
        
        @Test
        @DisplayName("Rechaza titular vacío")
        void rechazaTitularVacio() {
            assertThrows(IllegalArgumentException.class, () -> 
                new CuentaBancaria("   ", 100)
            );
        }
        
        @Test
        @DisplayName("Rechaza saldo inicial negativo")
        void rechazaSaldoNegativo() {
            assertThrows(IllegalArgumentException.class, () -> 
                new CuentaBancaria("Juan", -100)
            );
        }
    }
    
    @Nested
    @DisplayName("Depósitos")
    class Depositos {
        
        private CuentaBancaria cuenta;
        
        @BeforeEach
        void setUp() {
            cuenta = new CuentaBancaria("Test User", 500);
        }
        
        @Test
        @DisplayName("Depósito aumenta el saldo")
        void depositoAumentaSaldo() {
            cuenta.depositar(200);
            
            assertEquals(700, cuenta.getSaldo());
        }
        
        @Test
        @DisplayName("Múltiples depósitos se acumulan")
        void multiplesDepositos() {
            cuenta.depositar(100);
            cuenta.depositar(50);
            cuenta.depositar(25);
            
            assertEquals(675, cuenta.getSaldo());
        }
        
        @Test
        @DisplayName("Rechaza depósito de monto cero")
        void rechazaDepositoCero() {
            assertThrows(IllegalArgumentException.class, () -> 
                cuenta.depositar(0)
            );
        }
        
        @Test
        @DisplayName("Rechaza depósito de monto negativo")
        void rechazaDepositoNegativo() {
            assertThrows(IllegalArgumentException.class, () -> 
                cuenta.depositar(-50)
            );
        }
    }
    
    @Nested
    @DisplayName("Extracciones")
    class Extracciones {
        
        private CuentaBancaria cuenta;
        
        @BeforeEach
        void setUp() {
            cuenta = new CuentaBancaria("Test User", 1000);
        }
        
        @Test
        @DisplayName("Extracción disminuye el saldo")
        void extraccionDisminuyeSaldo() {
            cuenta.extraer(300);
            
            assertEquals(700, cuenta.getSaldo());
        }
        
        @Test
        @DisplayName("Puede extraer todo el saldo")
        void extraerTodoElSaldo() {
            cuenta.extraer(1000);
            
            assertEquals(0, cuenta.getSaldo());
        }
        
        @Test
        @DisplayName("Rechaza extracción mayor al saldo")
        void rechazaExtraccionExcesiva() {
            SaldoInsuficienteException ex = assertThrows(
                SaldoInsuficienteException.class, 
                () -> cuenta.extraer(1500)
            );
            
            assertEquals(1000, ex.getSaldoActual());
            assertEquals(1500, ex.getMontoSolicitado());
        }
        
        @Test
        @DisplayName("Rechaza extracción de monto cero")
        void rechazaExtraccionCero() {
            assertThrows(IllegalArgumentException.class, () -> 
                cuenta.extraer(0)
            );
        }
        
        @Test
        @DisplayName("El saldo no cambia si falla la extracción")
        void saldoNoCambiaSiFalla() {
            try {
                cuenta.extraer(2000);
            } catch (SaldoInsuficienteException e) {
                // Esperado
            }
            
            assertEquals(1000, cuenta.getSaldo());
        }
    }
}
```

(organizacion-tests)=
### Organización de Tests

Los tests anteriores muestran varias buenas prácticas:

1. **Clases anidadas (`@Nested`)**: Agrupan tests relacionados
2. **Nombres descriptivos (`@DisplayName`)**: Documentan el comportamiento
3. **Setup compartido (`@BeforeEach`)**: Evita duplicación
4. **Un assert por test** (idealmente): Cada test verifica una cosa

```
┌─────────────────────────────────────────────────────────────────┐
│                    ESTRUCTURA DE TESTS                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   CuentaBancariaTest                                            │
│   ├── CreacionCuenta                                            │
│   │   ├── crearCuentaConDatosValidos()                         │
│   │   ├── crearCuentaConSaldoCero()                            │
│   │   ├── rechazaTitularNulo()                                 │
│   │   ├── rechazaTitularVacio()                                │
│   │   └── rechazaSaldoNegativo()                               │
│   ├── Depositos                                                 │
│   │   ├── depositoAumentaSaldo()                               │
│   │   ├── multiplesDepositos()                                 │
│   │   ├── rechazaDepositoCero()                                │
│   │   └── rechazaDepositoNegativo()                            │
│   └── Extracciones                                              │
│       ├── extraccionDisminuyeSaldo()                           │
│       ├── extraerTodoElSaldo()                                 │
│       ├── rechazaExtraccionExcesiva()                          │
│       ├── rechazaExtraccionCero()                              │
│       └── saldoNoCambiaSiFalla()                               │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

(test-driven-development)=
## Test-Driven Development (TDD)

(que-es-tdd)=
### ¿Qué es TDD?

**Test-Driven Development** es una técnica donde los tests se escriben **antes** que el código de producción. No es solo una técnica de testing, sino una **técnica de diseño**.

```
┌─────────────────────────────────────────────────────────────────┐
│                    CICLO TDD: RED-GREEN-REFACTOR                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│              ┌─────────────┐                                    │
│              │   🔴 RED    │                                    │
│              │  Escribir   │                                    │
│              │  test que   │                                    │
│              │  falle      │                                    │
│              └──────┬──────┘                                    │
│                     │                                           │
│                     ▼                                           │
│   ┌─────────────────────────────────────┐                       │
│   │                                     │                       │
│   ▼                                     │                       │
│  ┌─────────────┐                 ┌──────┴──────┐                │
│  │ 🔵 REFACTOR │                 │  🟢 GREEN   │                │
│  │  Mejorar    │ ◀───────────── │  Escribir   │                │
│  │  diseño     │                 │  código     │                │
│  │  sin romper │                 │  mínimo     │                │
│  │  tests      │                 │  para pasar │                │
│  └─────────────┘                 └─────────────┘                │
│         │                                                       │
│         └───────────────────────────────────────────────────▶   │
│                            Repetir                              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

**Las tres reglas de TDD (Uncle Bob)**:

1. No escribir código de producción excepto para hacer pasar un test que falla
2. No escribir más de un test unitario que sea suficiente para fallar
3. No escribir más código de producción del necesario para pasar el test

(tdd-ejemplo-pila)=
### Ejemplo TDD: Implementando una Pila

Implementemos una `Pila` usando TDD paso a paso.

**Iteración 1: RED - Test para pila vacía**

```java
@Test
void pilaRecienCreadaEstaVacia() {
    Pila<Integer> pila = new Pila<>();
    
    assertTrue(pila.estaVacia());
}
```

Este test no compila porque `Pila` no existe. ¡Eso es RED!

**Iteración 1: GREEN - Código mínimo**

```java
public class Pila<T> {
    public boolean estaVacia() {
        return true;  // ¡Código mínimo para pasar!
    }
}
```

Test pasa. ¿Parece trampa? Es intencional. Fuerza a escribir más tests.

**Iteración 2: RED - Test para push**

```java
@Test
void pilaDespuesDePushNoEstaVacia() {
    Pila<Integer> pila = new Pila<>();
    
    pila.push(1);
    
    assertFalse(pila.estaVacia());
}
```

Falla porque `push` no existe y `estaVacia()` siempre retorna `true`.

**Iteración 2: GREEN**

```java
public class Pila<T> {
    private boolean tieneElementos = false;
    
    public void push(T elemento) {
        tieneElementos = true;
    }
    
    public boolean estaVacia() {
        return !tieneElementos;
    }
}
```

**Iteración 3: RED - Test para pop**

```java
@Test
void popRetornaUltimoElementoPusheado() {
    Pila<Integer> pila = new Pila<>();
    pila.push(42);
    
    int elemento = pila.pop();
    
    assertEquals(42, elemento);
}
```

**Iteración 3: GREEN**

```java
public class Pila<T> {
    private T elemento;
    private boolean tieneElementos = false;
    
    public void push(T elemento) {
        this.elemento = elemento;
        tieneElementos = true;
    }
    
    public T pop() {
        tieneElementos = false;
        return elemento;
    }
    
    public boolean estaVacia() {
        return !tieneElementos;
    }
}
```

**Iteración 4: RED - Test para múltiples elementos (LIFO)**

```java
@Test
void popRetornaElementosEnOrdenLIFO() {
    Pila<String> pila = new Pila<>();
    pila.push("primero");
    pila.push("segundo");
    pila.push("tercero");
    
    assertEquals("tercero", pila.pop());
    assertEquals("segundo", pila.pop());
    assertEquals("primero", pila.pop());
}
```

Este test falla porque solo guardamos un elemento.

**Iteración 4: GREEN**

```java
public class Pila<T> {
    private List<T> elementos = new ArrayList<>();
    
    public void push(T elemento) {
        elementos.add(elemento);
    }
    
    public T pop() {
        if (elementos.isEmpty()) {
            throw new EmptyStackException();
        }
        return elementos.remove(elementos.size() - 1);
    }
    
    public boolean estaVacia() {
        return elementos.isEmpty();
    }
}
```

**Iteración 5: REFACTOR**

El código está limpio, pero podemos agregar más tests para casos límite:

```java
@Test
void popEnPilaVaciaLanzaExcepcion() {
    Pila<Integer> pila = new Pila<>();
    
    assertThrows(EmptyStackException.class, () -> pila.pop());
}

@Test
void sizeRetornaCantidadDeElementos() {
    Pila<Integer> pila = new Pila<>();
    assertEquals(0, pila.size());
    
    pila.push(1);
    assertEquals(1, pila.size());
    
    pila.push(2);
    pila.push(3);
    assertEquals(3, pila.size());
}

@Test
void peekRetornaTopesinRemover() {
    Pila<Integer> pila = new Pila<>();
    pila.push(10);
    pila.push(20);
    
    assertEquals(20, pila.peek());
    assertEquals(20, pila.peek());  // No cambió
    assertEquals(2, pila.size());    // No se removió
}
```

(beneficios-tdd)=
### Beneficios de TDD

```
┌─────────────────────────────────────────────────────────────────┐
│                    BENEFICIOS DE TDD                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   1. DISEÑO EMERGENTE                                           │
│      El código se diseña desde la perspectiva del usuario       │
│      (el test), lo que produce APIs más usables.                │
│                                                                 │
│   2. CÓDIGO TESTEABLE                                           │
│      Si escribís tests primero, el código DEBE ser testeable.   │
│      Esto promueve bajo acoplamiento y alta cohesión.           │
│                                                                 │
│   3. DOCUMENTACIÓN VIVA                                         │
│      Los tests documentan cómo usar el código y qué esperar.    │
│      Esta documentación siempre está actualizada.               │
│                                                                 │
│   4. CONFIANZA                                                  │
│      Cada línea de código tiene al menos un test.               │
│      Refactorizar es seguro.                                    │
│                                                                 │
│   5. FOCO                                                       │
│      Trabajás en una cosa a la vez.                             │
│      Pequeños pasos manejables.                                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

(dobles-de-prueba)=
## Dobles de Prueba (Test Doubles)

(problema-dependencias)=
### El Problema de las Dependencias

¿Qué pasa cuando una clase depende de otra? ¿Cómo la testeamos en aislamiento?

```java
public class ServicioNotificaciones {
    private final EnviadorEmail enviador;
    private final RepositorioUsuarios repositorio;
    
    public ServicioNotificaciones(EnviadorEmail enviador, 
                                   RepositorioUsuarios repositorio) {
        this.enviador = enviador;
        this.repositorio = repositorio;
    }
    
    public void notificarPromocion(String promocion) {
        List<Usuario> usuarios = repositorio.obtenerActivos();
        for (Usuario u : usuarios) {
            enviador.enviar(u.getEmail(), "Nueva promoción", promocion);
        }
    }
}
```

Si queremos testear `ServicioNotificaciones`:

- No queremos enviar emails reales (lento, costoso, efectos secundarios)
- No queremos depender de una base de datos real

**Solución**: Reemplazar las dependencias con **dobles de prueba**.

(tipos-dobles)=
### Tipos de Dobles de Prueba

```
┌─────────────────────────────────────────────────────────────────┐
│                    TIPOS DE TEST DOUBLES                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   DUMMY                                                         │
│   ──────                                                        │
│   Objeto que se pasa pero nunca se usa realmente.               │
│   Solo para satisfacer parámetros.                              │
│                                                                 │
│   STUB                                                          │
│   ────                                                          │
│   Provee respuestas predefinidas a llamadas.                    │
│   No verifica cómo se lo llamó.                                 │
│                                                                 │
│   SPY                                                           │
│   ───                                                           │
│   Stub que además registra información sobre                    │
│   cómo fue llamado.                                             │
│                                                                 │
│   MOCK                                                          │
│   ────                                                          │
│   Objeto con expectativas preprogramadas.                       │
│   Falla si no se cumple el comportamiento esperado.             │
│                                                                 │
│   FAKE                                                          │
│   ────                                                          │
│   Implementación funcional simplificada.                        │
│   Ej: base de datos en memoria.                                 │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

(ejemplo-stubs)=
### Ejemplo: Usando Stubs

Creamos implementaciones simples para los tests:

```java
// Stub del repositorio que retorna usuarios predefinidos
class RepositorioUsuariosStub implements RepositorioUsuarios {
    private List<Usuario> usuarios;
    
    public RepositorioUsuariosStub(Usuario... usuarios) {
        this.usuarios = Arrays.asList(usuarios);
    }
    
    @Override
    public List<Usuario> obtenerActivos() {
        return usuarios;
    }
}

// Spy del enviador que registra los emails enviados
class EnviadorEmailSpy implements EnviadorEmail {
    private List<EmailEnviado> emailsEnviados = new ArrayList<>();
    
    @Override
    public void enviar(String destinatario, String asunto, String cuerpo) {
        emailsEnviados.add(new EmailEnviado(destinatario, asunto, cuerpo));
    }
    
    public int cantidadEnviados() {
        return emailsEnviados.size();
    }
    
    public boolean seEnvioA(String email) {
        return emailsEnviados.stream()
            .anyMatch(e -> e.destinatario().equals(email));
    }
    
    public EmailEnviado ultimoEnviado() {
        return emailsEnviados.get(emailsEnviados.size() - 1);
    }
}

record EmailEnviado(String destinatario, String asunto, String cuerpo) {}
```

**Tests usando los dobles**:

```java
class ServicioNotificacionesTest {
    
    @Test
    void notificaATodosLosUsuariosActivos() {
        // Arrange
        Usuario juan = new Usuario("juan@mail.com", "Juan");
        Usuario maria = new Usuario("maria@mail.com", "María");
        RepositorioUsuarios repo = new RepositorioUsuariosStub(juan, maria);
        EnviadorEmailSpy enviador = new EnviadorEmailSpy();
        
        ServicioNotificaciones servicio = new ServicioNotificaciones(enviador, repo);
        
        // Act
        servicio.notificarPromocion("¡50% de descuento!");
        
        // Assert
        assertEquals(2, enviador.cantidadEnviados());
        assertTrue(enviador.seEnvioA("juan@mail.com"));
        assertTrue(enviador.seEnvioA("maria@mail.com"));
    }
    
    @Test
    void noEnviaEmailsSiNoHayUsuarios() {
        RepositorioUsuarios repo = new RepositorioUsuariosStub();  // Sin usuarios
        EnviadorEmailSpy enviador = new EnviadorEmailSpy();
        ServicioNotificaciones servicio = new ServicioNotificaciones(enviador, repo);
        
        servicio.notificarPromocion("Promoción");
        
        assertEquals(0, enviador.cantidadEnviados());
    }
    
    @Test
    void emailContieneTextoDePromocion() {
        Usuario usuario = new Usuario("test@mail.com", "Test");
        RepositorioUsuarios repo = new RepositorioUsuariosStub(usuario);
        EnviadorEmailSpy enviador = new EnviadorEmailSpy();
        ServicioNotificaciones servicio = new ServicioNotificaciones(enviador, repo);
        
        servicio.notificarPromocion("¡Oferta especial!");
        
        EmailEnviado email = enviador.ultimoEnviado();
        assertEquals("Nueva promoción", email.asunto());
        assertEquals("¡Oferta especial!", email.cuerpo());
    }
}
```

(usando-mockito)=
### Usando Frameworks de Mocking: Mockito

En lugar de crear dobles manualmente, podemos usar frameworks como **Mockito**:

```java
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

class ServicioNotificacionesTestConMockito {
    
    @Mock
    private EnviadorEmail enviadorMock;
    
    @Mock
    private RepositorioUsuarios repositorioMock;
    
    @InjectMocks
    private ServicioNotificaciones servicio;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void notificaATodosLosUsuariosActivos() {
        // Arrange - configurar comportamiento del mock
        List<Usuario> usuarios = List.of(
            new Usuario("juan@mail.com", "Juan"),
            new Usuario("maria@mail.com", "María")
        );
        when(repositorioMock.obtenerActivos()).thenReturn(usuarios);
        
        // Act
        servicio.notificarPromocion("¡Descuento!");
        
        // Assert - verificar interacciones
        verify(enviadorMock, times(2)).enviar(anyString(), anyString(), anyString());
        verify(enviadorMock).enviar(eq("juan@mail.com"), anyString(), eq("¡Descuento!"));
        verify(enviadorMock).enviar(eq("maria@mail.com"), anyString(), eq("¡Descuento!"));
    }
    
    @Test
    void noEnviaEmailsSiNoHayUsuarios() {
        when(repositorioMock.obtenerActivos()).thenReturn(Collections.emptyList());
        
        servicio.notificarPromocion("Oferta");
        
        verify(enviadorMock, never()).enviar(anyString(), anyString(), anyString());
    }
}
```

**Funciones principales de Mockito**:

| Función | Propósito |
|---------|-----------|
| `mock(Clase.class)` | Crear un mock |
| `when(...).thenReturn(...)` | Configurar comportamiento |
| `when(...).thenThrow(...)` | Configurar que lance excepción |
| `verify(mock).metodo()` | Verificar que se llamó |
| `verify(mock, times(n))` | Verificar cantidad de llamadas |
| `verify(mock, never())` | Verificar que NO se llamó |
| `any()`, `eq()`, `anyString()` | Matchers para argumentos |

---

(diseno-testeable)=
## Diseño para Testeabilidad

(caracteristicas-codigo-testeable)=
### Características del Código Testeable

No todo código es fácil de testear. Un código **testeable** tiene ciertas características:

```
┌─────────────────────────────────────────────────────────────────┐
│                    CÓDIGO TESTEABLE                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ✓ DEPENDENCIAS INYECTADAS                                     │
│     Las dependencias se pasan por constructor o método,         │
│     no se crean internamente con "new".                         │
│                                                                 │
│   ✓ BAJO ACOPLAMIENTO                                           │
│     La clase depende de abstracciones (interfaces),             │
│     no de implementaciones concretas.                           │
│                                                                 │
│   ✓ RESPONSABILIDAD ÚNICA                                       │
│     Menos responsabilidades = menos casos a testear.            │
│                                                                 │
│   ✓ SIN ESTADO GLOBAL                                           │
│     No depende de singletons, variables estáticas mutables,     │
│     ni estado compartido.                                       │
│                                                                 │
│   ✓ DETERMINÍSTICO                                              │
│     Dado el mismo input, siempre produce el mismo output.       │
│     No depende de fecha/hora actual, aleatorios, etc.           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

(codigo-dificil-testear)=
### Código Difícil de Testear

**Problema 1: Dependencias creadas internamente**

```java
// ✗ DIFÍCIL DE TESTEAR
public class ReporteVentas {
    public String generar(LocalDate fecha) {
        // Crea la dependencia internamente - ¡no podemos reemplazarla!
        ConexionBaseDatos conexion = new ConexionBaseDatos("prod-server");
        List<Venta> ventas = conexion.obtenerVentas(fecha);
        // ... generar reporte
    }
}
```

**Solución: Inyección de dependencias**

```java
// ✓ FÁCIL DE TESTEAR
public class ReporteVentas {
    private final RepositorioVentas repositorio;
    
    public ReporteVentas(RepositorioVentas repositorio) {
        this.repositorio = repositorio;  // Dependencia inyectada
    }
    
    public String generar(LocalDate fecha) {
        List<Venta> ventas = repositorio.obtenerVentas(fecha);
        // ... generar reporte
    }
}

// En el test:
ReporteVentas reporte = new ReporteVentas(repositorioMock);
```

**Problema 2: Dependencia de fecha/hora actual**

```java
// ✗ DIFÍCIL DE TESTEAR
public class Pedido {
    private LocalDateTime fechaCreacion;
    
    public Pedido() {
        this.fechaCreacion = LocalDateTime.now();  // Indeterminístico
    }
    
    public boolean estaVencido() {
        return fechaCreacion.plusDays(7).isBefore(LocalDateTime.now());
    }
}
```

**Solución: Inyectar un Clock o la fecha**

```java
// ✓ FÁCIL DE TESTEAR
public class Pedido {
    private LocalDateTime fechaCreacion;
    private final Clock clock;
    
    public Pedido(Clock clock) {
        this.clock = clock;
        this.fechaCreacion = LocalDateTime.now(clock);
    }
    
    public boolean estaVencido() {
        return fechaCreacion.plusDays(7).isBefore(LocalDateTime.now(clock));
    }
}

// En el test:
Clock clockFijo = Clock.fixed(Instant.parse("2024-01-15T10:00:00Z"), ZoneId.systemDefault());
Pedido pedido = new Pedido(clockFijo);
// Ahora podés controlar el tiempo en los tests
```

**Problema 3: Singleton**

```java
// ✗ DIFÍCIL DE TESTEAR
public class Logger {
    private static final Logger INSTANCE = new Logger();
    private Logger() {}
    public static Logger getInstance() { return INSTANCE; }
}

public class Servicio {
    public void procesar() {
        Logger.getInstance().log("Procesando...");  // Acoplamiento al singleton
    }
}
```

**Solución: Inyectar la dependencia**

```java
// ✓ FÁCIL DE TESTEAR
public interface Logger {
    void log(String mensaje);
}

public class Servicio {
    private final Logger logger;
    
    public Servicio(Logger logger) {
        this.logger = logger;
    }
    
    public void procesar() {
        logger.log("Procesando...");
    }
}
```

(relacion-solid-testing)=
### Relación SOLID-Testing

Los principios SOLID (ver {ref}`oop-solid`) facilitan el testing:

```
┌─────────────────────────────────────────────────────────────────┐
│                    SOLID Y TESTING                              │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   SRP (Responsabilidad Única)                                   │
│   ────────────────────────────                                  │
│   → Menos responsabilidades = menos tests necesarios            │
│   → Tests más enfocados y claros                                │
│                                                                 │
│   OCP (Abierto/Cerrado)                                         │
│   ─────────────────────                                         │
│   → Nuevas funcionalidades = nuevos tests, no modificar viejos  │
│   → Los tests existentes siguen pasando                         │
│                                                                 │
│   LSP (Sustitución de Liskov)                                   │
│   ──────────────────────────                                    │
│   → Podemos usar mocks que implementen la interfaz              │
│   → Tests parametrizados para todas las implementaciones        │
│                                                                 │
│   ISP (Segregación de Interfaces)                               │
│   ────────────────────────────────                              │
│   → Interfaces pequeñas = mocks más simples                     │
│   → Menos métodos que mockear                                   │
│                                                                 │
│   DIP (Inversión de Dependencias)                               │
│   ────────────────────────────────                              │
│   → Dependencias inyectables = reemplazables por dobles         │
│   → Tests aislados del mundo exterior                           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

(testing-herencia-polimorfismo)=
## Testing de Herencia y Polimorfismo

(testing-jerarquias)=
### Testing de Jerarquías de Clases

Cuando tenemos una jerarquía de herencia, ¿cómo organizamos los tests?

```java
public abstract class Figura {
    public abstract double area();
    public abstract double perimetro();
}

public class Rectangulo extends Figura {
    private final double ancho;
    private final double alto;
    
    public Rectangulo(double ancho, double alto) {
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("Dimensiones deben ser positivas");
        }
        this.ancho = ancho;
        this.alto = alto;
    }
    
    @Override
    public double area() {
        return ancho * alto;
    }
    
    @Override
    public double perimetro() {
        return 2 * (ancho + alto);
    }
}

public class Cuadrado extends Rectangulo {
    public Cuadrado(double lado) {
        super(lado, lado);
    }
}

public class Circulo extends Figura {
    private final double radio;
    
    public Circulo(double radio) {
        if (radio <= 0) {
            throw new IllegalArgumentException("Radio debe ser positivo");
        }
        this.radio = radio;
    }
    
    @Override
    public double area() {
        return Math.PI * radio * radio;
    }
    
    @Override
    public double perimetro() {
        return 2 * Math.PI * radio;
    }
}
```

**Estrategia 1: Tests específicos por clase**

```java
class RectanguloTest {
    
    @Test
    void calculaAreaCorrectamente() {
        Rectangulo r = new Rectangulo(4, 5);
        assertEquals(20, r.area(), 0.001);
    }
    
    @Test
    void calculaPerimetroCorrectamente() {
        Rectangulo r = new Rectangulo(4, 5);
        assertEquals(18, r.perimetro(), 0.001);
    }
    
    @Test
    void rechazaDimensionesNegativas() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Rectangulo(-1, 5)
        );
    }
}

class CirculoTest {
    
    @Test
    void calculaAreaCorrectamente() {
        Circulo c = new Circulo(3);
        assertEquals(Math.PI * 9, c.area(), 0.001);
    }
    
    @Test
    void calculaPerimetroCorrectamente() {
        Circulo c = new Circulo(3);
        assertEquals(2 * Math.PI * 3, c.perimetro(), 0.001);
    }
}
```

**Estrategia 2: Tests parametrizados para comportamiento común**

```java
class FiguraContractTest {
    
    static Stream<Arguments> figuras() {
        return Stream.of(
            Arguments.of(new Rectangulo(4, 5), 20.0, 18.0),
            Arguments.of(new Cuadrado(4), 16.0, 16.0),
            Arguments.of(new Circulo(1), Math.PI, 2 * Math.PI)
        );
    }
    
    @ParameterizedTest
    @MethodSource("figuras")
    void areaEsPositiva(Figura figura, double areaEsperada, double perimetroEsperado) {
        assertTrue(figura.area() > 0, "Área debe ser positiva");
    }
    
    @ParameterizedTest
    @MethodSource("figuras")
    void perimetroEsPositivo(Figura figura, double areaEsperada, double perimetroEsperado) {
        assertTrue(figura.perimetro() > 0, "Perímetro debe ser positivo");
    }
    
    @ParameterizedTest
    @MethodSource("figuras")
    void calculaAreaCorrectamente(Figura figura, double areaEsperada, double perimetroEsperado) {
        assertEquals(areaEsperada, figura.area(), 0.001);
    }
    
    @ParameterizedTest
    @MethodSource("figuras")
    void calculaPerimetroCorrectamente(Figura figura, double areaEsperada, double perimetroEsperado) {
        assertEquals(perimetroEsperado, figura.perimetro(), 0.001);
    }
}
```

(testing-polimorfismo)=
### Testing de Comportamiento Polimórfico

Cuando el código usa polimorfismo, testear se vuelve más interesante:

```java
public class CalculadoraGeometrica {
    
    public double areaTotal(List<Figura> figuras) {
        return figuras.stream()
            .mapToDouble(Figura::area)
            .sum();
    }
    
    public Figura mayorArea(List<Figura> figuras) {
        return figuras.stream()
            .max(Comparator.comparing(Figura::area))
            .orElseThrow(() -> new IllegalArgumentException("Lista vacía"));
    }
}
```

```java
class CalculadoraGeometricaTest {
    
    private CalculadoraGeometrica calculadora;
    
    @BeforeEach
    void setUp() {
        calculadora = new CalculadoraGeometrica();
    }
    
    @Test
    void calculaAreaTotalDeFigurasMixtas() {
        List<Figura> figuras = List.of(
            new Rectangulo(2, 3),    // área = 6
            new Cuadrado(4),          // área = 16
            new Circulo(1)            // área = π ≈ 3.14
        );
        
        double areaTotal = calculadora.areaTotal(figuras);
        
        assertEquals(6 + 16 + Math.PI, areaTotal, 0.01);
    }
    
    @Test
    void encuentraFiguraConMayorArea() {
        Cuadrado grande = new Cuadrado(10);  // área = 100
        List<Figura> figuras = List.of(
            new Rectangulo(2, 3),
            grande,
            new Circulo(3)
        );
        
        Figura mayor = calculadora.mayorArea(figuras);
        
        assertSame(grande, mayor);
    }
    
    @Test
    void listaVaciaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> 
            calculadora.mayorArea(Collections.emptyList())
        );
    }
}
```

---

(buenas-practicas-testing)=
## Buenas Prácticas de Testing

(principios-first)=
### Principios FIRST

Los buenos tests siguen el acrónimo **FIRST**:

```
┌─────────────────────────────────────────────────────────────────┐
│                    PRINCIPIOS FIRST                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   F - FAST (Rápidos)                                            │
│       Los tests deben ejecutarse en milisegundos.               │
│       Si son lentos, no los vas a correr frecuentemente.        │
│                                                                 │
│   I - INDEPENDENT (Independientes)                              │
│       Cada test debe poder ejecutarse solo.                     │
│       No depender del orden de ejecución.                       │
│       No compartir estado entre tests.                          │
│                                                                 │
│   R - REPEATABLE (Repetibles)                                   │
│       El mismo test siempre da el mismo resultado.              │
│       No depender del entorno, hora, red, etc.                  │
│                                                                 │
│   S - SELF-VALIDATING (Auto-validantes)                         │
│       El test determina si pasó o falló automáticamente.        │
│       No requerir inspección manual del output.                 │
│                                                                 │
│   T - TIMELY (Oportunos)                                        │
│       Escritos junto con (o antes de) el código de producción.  │
│       No postergar los tests.                                   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

(nombres-descriptivos)=
### Nombres Descriptivos

Un test bien nombrado actúa como documentación:

```java
// ✗ Nombres pobres
@Test
void test1() { ... }

@Test
void testDepositar() { ... }

// ✓ Nombres descriptivos
@Test
void depositarMontoPositivoAumentaElSaldo() { ... }

@Test
void depositarMontoNegativoLanzaExcepcion() { ... }

@Test
void extraerMasDelSaldoDisponibleFalla() { ... }
```

**Patrones para nombrar tests**:

| Patrón | Ejemplo |
|--------|---------|
| `metodo_escenario_resultado` | `depositar_montoPositivo_aumentaSaldo` |
| `dado_cuando_entonces` | `dadoCuentaConSaldo_cuandoExtraeTodo_entoncesQuedaEnCero` |
| `should_when` | `shouldThrowException_whenAmountIsNegative` |
| Oración descriptiva | `extraerMasDelSaldoLanzaSaldoInsuficienteException` |

(un-concepto-por-test)=
### Un Concepto por Test

Cada test debe verificar **una sola cosa**:

```java
// ✗ Test que verifica múltiples cosas
@Test
void testOperacionesBancarias() {
    CuentaBancaria cuenta = new CuentaBancaria("Juan", 1000);
    
    cuenta.depositar(500);
    assertEquals(1500, cuenta.getSaldo());
    
    cuenta.extraer(200);
    assertEquals(1300, cuenta.getSaldo());
    
    assertThrows(SaldoInsuficienteException.class, () -> 
        cuenta.extraer(2000)
    );
}

// ✓ Tests separados por concepto
@Test
void depositarAumentaElSaldo() {
    CuentaBancaria cuenta = new CuentaBancaria("Juan", 1000);
    cuenta.depositar(500);
    assertEquals(1500, cuenta.getSaldo());
}

@Test
void extraerDisminuyeElSaldo() {
    CuentaBancaria cuenta = new CuentaBancaria("Juan", 1000);
    cuenta.extraer(200);
    assertEquals(800, cuenta.getSaldo());
}

@Test
void extraerMasDelSaldoFalla() {
    CuentaBancaria cuenta = new CuentaBancaria("Juan", 1000);
    assertThrows(SaldoInsuficienteException.class, () -> 
        cuenta.extraer(2000)
    );
}
```

(arrange-act-assert-separados)=
### Secciones Claramente Separadas

Mantené las tres secciones del test visualmente separadas:

```java
@Test
void transferenciaMueveMontoEntreCuentas() {
    // Arrange
    CuentaBancaria origen = new CuentaBancaria("A", 1000);
    CuentaBancaria destino = new CuentaBancaria("B", 500);
    ServicioTransferencias servicio = new ServicioTransferencias();
    
    // Act
    servicio.transferir(origen, destino, 300);
    
    // Assert
    assertEquals(700, origen.getSaldo());
    assertEquals(800, destino.getSaldo());
}
```

---

(patrones-testing-avanzado)=
## Patrones de Testing Avanzados

(object-mother)=
### Object Mother

Un patrón para crear objetos de test de manera consistente:

```java
public class CuentaMother {
    
    public static CuentaBancaria cuentaVacia() {
        return new CuentaBancaria("Test User", 0);
    }
    
    public static CuentaBancaria cuentaConSaldo(double saldo) {
        return new CuentaBancaria("Test User", saldo);
    }
    
    public static CuentaBancaria cuentaPremium() {
        CuentaBancaria cuenta = new CuentaBancaria("Premium User", 10000);
        cuenta.activarBeneficiosPremium();
        return cuenta;
    }
    
    public static CuentaBancaria cuentaBloqueada() {
        CuentaBancaria cuenta = new CuentaBancaria("Blocked User", 500);
        cuenta.bloquear();
        return cuenta;
    }
}

// Uso en tests
@Test
void cuentaBloqueadaNoPuedeExtraer() {
    CuentaBancaria cuenta = CuentaMother.cuentaBloqueada();
    
    assertThrows(CuentaBloqueadaException.class, () -> 
        cuenta.extraer(100)
    );
}
```

(builder-para-tests)=
### Builder para Tests

Para objetos complejos con muchos atributos:

```java
public class PedidoBuilder {
    private String cliente = "Cliente Default";
    private LocalDate fecha = LocalDate.now();
    private List<ItemPedido> items = new ArrayList<>();
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    private Direccion direccionEntrega = DireccionMother.direccionDefault();
    
    public static PedidoBuilder unPedido() {
        return new PedidoBuilder();
    }
    
    public PedidoBuilder deCliente(String cliente) {
        this.cliente = cliente;
        return this;
    }
    
    public PedidoBuilder conFecha(LocalDate fecha) {
        this.fecha = fecha;
        return this;
    }
    
    public PedidoBuilder conItem(String producto, int cantidad, double precio) {
        this.items.add(new ItemPedido(producto, cantidad, precio));
        return this;
    }
    
    public PedidoBuilder conEstado(EstadoPedido estado) {
        this.estado = estado;
        return this;
    }
    
    public PedidoBuilder entregadoEn(Direccion direccion) {
        this.direccionEntrega = direccion;
        return this;
    }
    
    public Pedido build() {
        Pedido pedido = new Pedido(cliente, fecha);
        items.forEach(pedido::agregarItem);
        pedido.setEstado(estado);
        pedido.setDireccionEntrega(direccionEntrega);
        return pedido;
    }
}

// Uso en tests
@Test
void pedidoConMultiplesItemsCalculaTotalCorrectamente() {
    Pedido pedido = PedidoBuilder.unPedido()
        .deCliente("Juan")
        .conItem("Producto A", 2, 100.0)
        .conItem("Producto B", 1, 50.0)
        .build();
    
    assertEquals(250.0, pedido.getTotal(), 0.01);
}
```

(test-data-builder-vs-object-mother)=
### Cuándo usar cada patrón

```
┌─────────────────────────────────────────────────────────────────┐
│              OBJECT MOTHER vs TEST DATA BUILDER                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   OBJECT MOTHER                                                 │
│   ──────────────                                                │
│   • Objetos simples con pocas variaciones                       │
│   • Escenarios nombrados: cuentaVacia(), cuentaPremium()       │
│   • Menos código de setup                                       │
│                                                                 │
│   TEST DATA BUILDER                                             │
│   ─────────────────                                             │
│   • Objetos complejos con muchos atributos                     │
│   • Necesidad de variar atributos específicos                  │
│   • Fluent API para claridad                                   │
│                                                                 │
│   COMBINACIÓN                                                   │
│   ───────────                                                   │
│   • Builder como implementación interna de Object Mother        │
│   • Lo mejor de ambos mundos                                   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

(resumen-testing)=
## Resumen

```
┌─────────────────────────────────────────────────────────────────┐
│                TESTING EN POO - RESUMEN                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  FUNDAMENTOS                                                    │
│    • Tests como red de seguridad y documentación               │
│    • Pirámide de testing: unitarios > integración > E2E        │
│    • Patrón AAA: Arrange, Act, Assert                          │
│                                                                 │
│  TEST-DRIVEN DEVELOPMENT                                        │
│    • Ciclo RED-GREEN-REFACTOR                                  │
│    • Tests guían el diseño                                     │
│    • Código testeable por construcción                         │
│                                                                 │
│  DOBLES DE PRUEBA                                               │
│    • Stubs: respuestas predefinidas                            │
│    • Spies: registran llamadas                                 │
│    • Mocks: verifican expectativas                             │
│    • Frameworks: Mockito                                       │
│                                                                 │
│  DISEÑO TESTEABLE                                               │
│    • Inyección de dependencias                                 │
│    • Dependencias como interfaces                              │
│    • Evitar estado global y singletons                         │
│    • SOLID facilita testing                                    │
│                                                                 │
│  BUENAS PRÁCTICAS                                               │
│    • Principios FIRST                                          │
│    • Nombres descriptivos                                      │
│    • Un concepto por test                                      │
│    • Object Mother y Builder para crear datos                  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

(ejercicios-testing)=
## Ejercicios

```{exercise}
:label: testing-ex-carrito

**Testing de un Carrito de Compras**

Implementá tests para la siguiente clase `CarritoCompras`:

```java
public class CarritoCompras {
    private List<ItemCarrito> items = new ArrayList<>();
    private double descuentoPorcentaje = 0;
    
    public void agregar(Producto producto, int cantidad) { ... }
    public void remover(Producto producto) { ... }
    public void vaciar() { ... }
    public int cantidadItems() { ... }
    public double subtotal() { ... }
    public void aplicarDescuento(double porcentaje) { ... }
    public double total() { ... }
    public boolean estaVacio() { ... }
}
```

Escribí tests que cubran:
1. Agregar productos al carrito
2. Remover productos
3. Calcular subtotal
4. Aplicar descuentos
5. Casos límite (carrito vacío, descuento 100%, etc.)
```

```{solution} testing-ex-carrito
:class: dropdown

```java
class CarritoComprasTest {
    
    private CarritoCompras carrito;
    private Producto laptop;
    private Producto mouse;
    
    @BeforeEach
    void setUp() {
        carrito = new CarritoCompras();
        laptop = new Producto("Laptop", 1000.0);
        mouse = new Producto("Mouse", 50.0);
    }
    
    @Nested
    @DisplayName("Carrito vacío")
    class CarritoVacio {
        
        @Test
        void carritoNuevoEstaVacio() {
            assertTrue(carrito.estaVacio());
            assertEquals(0, carrito.cantidadItems());
        }
        
        @Test
        void carritoVacioTieneTotalCero() {
            assertEquals(0, carrito.total(), 0.001);
        }
    }
    
    @Nested
    @DisplayName("Agregar productos")
    class AgregarProductos {
        
        @Test
        void agregarProductoLoIncluye() {
            carrito.agregar(laptop, 1);
            
            assertFalse(carrito.estaVacio());
            assertEquals(1, carrito.cantidadItems());
        }
        
        @Test
        void agregarMultiplesUnidadesDelMismoProducto() {
            carrito.agregar(laptop, 3);
            
            assertEquals(3, carrito.cantidadItems());
            assertEquals(3000.0, carrito.subtotal(), 0.001);
        }
        
        @Test
        void agregarDiferentesProductos() {
            carrito.agregar(laptop, 1);
            carrito.agregar(mouse, 2);
            
            assertEquals(3, carrito.cantidadItems());
            assertEquals(1100.0, carrito.subtotal(), 0.001);
        }
    }
    
    @Nested
    @DisplayName("Remover productos")
    class RemoverProductos {
        
        @Test
        void removerProductoLoElimina() {
            carrito.agregar(laptop, 1);
            carrito.agregar(mouse, 1);
            
            carrito.remover(laptop);
            
            assertEquals(1, carrito.cantidadItems());
            assertEquals(50.0, carrito.subtotal(), 0.001);
        }
        
        @Test
        void removerTodosLosProductosDejaCarritoVacio() {
            carrito.agregar(laptop, 1);
            carrito.remover(laptop);
            
            assertTrue(carrito.estaVacio());
        }
        
        @Test
        void vaciarCarritoRemoverTodo() {
            carrito.agregar(laptop, 2);
            carrito.agregar(mouse, 5);
            
            carrito.vaciar();
            
            assertTrue(carrito.estaVacio());
            assertEquals(0, carrito.total(), 0.001);
        }
    }
    
    @Nested
    @DisplayName("Descuentos")
    class Descuentos {
        
        @Test
        void aplicarDescuentoReduceTotal() {
            carrito.agregar(laptop, 1);  // $1000
            
            carrito.aplicarDescuento(10);  // 10%
            
            assertEquals(900.0, carrito.total(), 0.001);
        }
        
        @Test
        void descuentoCienPorcientoResultaEnCero() {
            carrito.agregar(laptop, 1);
            
            carrito.aplicarDescuento(100);
            
            assertEquals(0, carrito.total(), 0.001);
        }
        
        @Test
        void subtotalNoCambiaConDescuento() {
            carrito.agregar(laptop, 1);
            
            carrito.aplicarDescuento(20);
            
            assertEquals(1000.0, carrito.subtotal(), 0.001);
            assertEquals(800.0, carrito.total(), 0.001);
        }
        
        @Test
        void rechazaDescuentoNegativo() {
            assertThrows(IllegalArgumentException.class, () ->
                carrito.aplicarDescuento(-10)
            );
        }
        
        @Test
        void rechazaDescuentoMayorACien() {
            assertThrows(IllegalArgumentException.class, () ->
                carrito.aplicarDescuento(110)
            );
        }
    }
}
```
```

```{exercise}
:label: testing-ex-tdd-calculadora

**TDD: Calculadora de Expresiones**

Usando TDD (Red-Green-Refactor), implementá una `Calculadora` que pueda evaluar expresiones simples.

Requisitos:
1. Sumar dos números: `"2 + 3"` → `5`
2. Restar: `"10 - 4"` → `6`
3. Multiplicar: `"3 * 4"` → `12`
4. Dividir: `"15 / 3"` → `5`
5. División por cero lanza excepción
6. Expresiones con espacios variables: `"2+3"`, `"2 + 3"`, `"2  +  3"` → `5`

Mostrá el ciclo TDD escribiendo primero los tests.
```

```{solution} testing-ex-tdd-calculadora
:class: dropdown

**Ciclo TDD paso a paso:**

```java
// ITERACIÓN 1: RED - Test básico de suma
@Test
void sumaBasica() {
    Calculadora calc = new Calculadora();
    assertEquals(5, calc.evaluar("2 + 3"));
}

// ITERACIÓN 1: GREEN - Implementación mínima
public class Calculadora {
    public int evaluar(String expresion) {
        return 5;  // ¡Hardcodeado!
    }
}

// ITERACIÓN 2: RED - Otra suma para forzar generalización
@Test
void otraSuma() {
    Calculadora calc = new Calculadora();
    assertEquals(15, calc.evaluar("10 + 5"));
}

// ITERACIÓN 2: GREEN - Parsear la expresión
public class Calculadora {
    public int evaluar(String expresion) {
        String[] partes = expresion.split("\\+");
        int a = Integer.parseInt(partes[0].trim());
        int b = Integer.parseInt(partes[1].trim());
        return a + b;
    }
}

// ITERACIÓN 3: RED - Resta
@Test
void restaBasica() {
    Calculadora calc = new Calculadora();
    assertEquals(6, calc.evaluar("10 - 4"));
}

// ITERACIÓN 3: GREEN - Detectar operador
public class Calculadora {
    public int evaluar(String expresion) {
        if (expresion.contains("+")) {
            String[] partes = expresion.split("\\+");
            return Integer.parseInt(partes[0].trim()) + 
                   Integer.parseInt(partes[1].trim());
        } else if (expresion.contains("-")) {
            String[] partes = expresion.split("-");
            return Integer.parseInt(partes[0].trim()) - 
                   Integer.parseInt(partes[1].trim());
        }
        throw new IllegalArgumentException("Expresión inválida");
    }
}

// ITERACIÓN 4: REFACTOR - Extraer lógica común
public class Calculadora {
    
    public int evaluar(String expresion) {
        char operador = encontrarOperador(expresion);
        String[] operandos = expresion.split("\\" + operador);
        int a = Integer.parseInt(operandos[0].trim());
        int b = Integer.parseInt(operandos[1].trim());
        
        return switch (operador) {
            case '+' -> a + b;
            case '-' -> a - b;
            default -> throw new IllegalArgumentException("Operador desconocido");
        };
    }
    
    private char encontrarOperador(String expresion) {
        for (char c : new char[]{'+', '-', '*', '/'}) {
            if (expresion.contains(String.valueOf(c))) {
                return c;
            }
        }
        throw new IllegalArgumentException("No se encontró operador");
    }
}

// ITERACIÓN 5: RED - Multiplicación y división
@Test
void multiplicacion() {
    assertEquals(12, new Calculadora().evaluar("3 * 4"));
}

@Test
void division() {
    assertEquals(5, new Calculadora().evaluar("15 / 3"));
}

// ITERACIÓN 5: GREEN - Agregar operadores
public int evaluar(String expresion) {
    char operador = encontrarOperador(expresion);
    String[] operandos = expresion.split(
        operador == '*' || operador == '+' ? "\\" + operador : String.valueOf(operador)
    );
    int a = Integer.parseInt(operandos[0].trim());
    int b = Integer.parseInt(operandos[1].trim());
    
    return switch (operador) {
        case '+' -> a + b;
        case '-' -> a - b;
        case '*' -> a * b;
        case '/' -> a / b;
        default -> throw new IllegalArgumentException("Operador desconocido");
    };
}

// ITERACIÓN 6: RED - División por cero
@Test
void divisionPorCeroLanzaExcepcion() {
    assertThrows(ArithmeticException.class, () ->
        new Calculadora().evaluar("10 / 0")
    );
}

// ITERACIÓN 6: GREEN - Ya funciona con Java! (ArithmeticException nativa)

// ITERACIÓN 7: RED - Espacios variables
@Test
void expresionSinEspacios() {
    assertEquals(5, new Calculadora().evaluar("2+3"));
}

@Test
void expresionConMuchosEspacios() {
    assertEquals(5, new Calculadora().evaluar("2  +  3"));
}

// ITERACIÓN 7: GREEN - trim() ya maneja esto
// Los tests pasan sin cambios (¡buena señal de diseño robusto!)
```

**Versión final refactorizada:**

```java
public class Calculadora {
    
    private static final Set<Character> OPERADORES = Set.of('+', '-', '*', '/');
    
    public int evaluar(String expresion) {
        char operador = encontrarOperador(expresion);
        int[] operandos = parsearOperandos(expresion, operador);
        return calcular(operandos[0], operandos[1], operador);
    }
    
    private char encontrarOperador(String expresion) {
        for (char c : OPERADORES) {
            // Evitar confundir signo negativo con resta
            int index = expresion.indexOf(c, 1);
            if (index > 0) {
                return c;
            }
        }
        throw new IllegalArgumentException("Operador no encontrado: " + expresion);
    }
    
    private int[] parsearOperandos(String expresion, char operador) {
        String regex = operador == '*' || operador == '+' 
            ? "\\" + operador 
            : String.valueOf(operador);
        String[] partes = expresion.split(regex, 2);
        
        return new int[] {
            Integer.parseInt(partes[0].trim()),
            Integer.parseInt(partes[1].trim())
        };
    }
    
    private int calcular(int a, int b, char operador) {
        return switch (operador) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;  // ArithmeticException si b == 0
            default -> throw new IllegalArgumentException("Operador inválido: " + operador);
        };
    }
}
```
```

```{exercise}
:label: testing-ex-mocks

**Usando Mocks: Sistema de Notificaciones**

Dado el siguiente sistema de notificaciones:

```java
public interface Notificador {
    void enviar(String destinatario, String mensaje);
    boolean estaDisponible();
}

public class SistemaAlertas {
    private final List<Notificador> notificadores;
    private final RegistroAlertas registro;
    
    public SistemaAlertas(List<Notificador> notificadores, RegistroAlertas registro) {
        this.notificadores = notificadores;
        this.registro = registro;
    }
    
    public int enviarAlerta(String destinatario, String mensaje) {
        int enviados = 0;
        for (Notificador n : notificadores) {
            if (n.estaDisponible()) {
                n.enviar(destinatario, mensaje);
                enviados++;
            }
        }
        registro.registrar(destinatario, mensaje, enviados);
        return enviados;
    }
}
```

Escribí tests usando Mockito que verifiquen:
1. Envía por todos los notificadores disponibles
2. No envía por notificadores no disponibles
3. Siempre registra la alerta aunque ningún notificador esté disponible
4. Retorna la cantidad correcta de envíos
```

```{solution} testing-ex-mocks
:class: dropdown

```java
@ExtendWith(MockitoExtension.class)
class SistemaAlertasTest {
    
    @Mock
    private Notificador emailNotificador;
    
    @Mock
    private Notificador smsNotificador;
    
    @Mock
    private Notificador pushNotificador;
    
    @Mock
    private RegistroAlertas registro;
    
    private SistemaAlertas sistema;
    
    @BeforeEach
    void setUp() {
        sistema = new SistemaAlertas(
            List.of(emailNotificador, smsNotificador, pushNotificador),
            registro
        );
    }
    
    @Test
    void enviaAlertaPorTodosLosNotificadoresDisponibles() {
        // Arrange
        when(emailNotificador.estaDisponible()).thenReturn(true);
        when(smsNotificador.estaDisponible()).thenReturn(true);
        when(pushNotificador.estaDisponible()).thenReturn(true);
        
        // Act
        sistema.enviarAlerta("usuario@mail.com", "Alerta crítica");
        
        // Assert
        verify(emailNotificador).enviar("usuario@mail.com", "Alerta crítica");
        verify(smsNotificador).enviar("usuario@mail.com", "Alerta crítica");
        verify(pushNotificador).enviar("usuario@mail.com", "Alerta crítica");
    }
    
    @Test
    void noEnviaPorNotificadoresNoDisponibles() {
        // Arrange
        when(emailNotificador.estaDisponible()).thenReturn(true);
        when(smsNotificador.estaDisponible()).thenReturn(false);  // No disponible
        when(pushNotificador.estaDisponible()).thenReturn(true);
        
        // Act
        sistema.enviarAlerta("user", "Mensaje");
        
        // Assert
        verify(emailNotificador).enviar(anyString(), anyString());
        verify(smsNotificador, never()).enviar(anyString(), anyString());
        verify(pushNotificador).enviar(anyString(), anyString());
    }
    
    @Test
    void siempreRegistraLaAlertaAunqueFallenTodosLosNotificadores() {
        // Arrange
        when(emailNotificador.estaDisponible()).thenReturn(false);
        when(smsNotificador.estaDisponible()).thenReturn(false);
        when(pushNotificador.estaDisponible()).thenReturn(false);
        
        // Act
        sistema.enviarAlerta("destino", "mensaje importante");
        
        // Assert
        verify(registro).registrar("destino", "mensaje importante", 0);
    }
    
    @Test
    void retornaCantidadCorrectaDeEnvios() {
        // Arrange
        when(emailNotificador.estaDisponible()).thenReturn(true);
        when(smsNotificador.estaDisponible()).thenReturn(false);
        when(pushNotificador.estaDisponible()).thenReturn(true);
        
        // Act
        int enviados = sistema.enviarAlerta("dest", "msg");
        
        // Assert
        assertEquals(2, enviados);
    }
    
    @Test
    void registraConCantidadCorrectaDeEnvios() {
        // Arrange
        when(emailNotificador.estaDisponible()).thenReturn(true);
        when(smsNotificador.estaDisponible()).thenReturn(true);
        when(pushNotificador.estaDisponible()).thenReturn(false);
        
        // Act
        sistema.enviarAlerta("usuario", "alerta");
        
        // Assert
        verify(registro).registrar(eq("usuario"), eq("alerta"), eq(2));
    }
    
    @Test
    void manejaListaVaciaDeNotificadores() {
        // Arrange
        SistemaAlertas sistemaSinNotificadores = new SistemaAlertas(
            Collections.emptyList(),
            registro
        );
        
        // Act
        int enviados = sistemaSinNotificadores.enviarAlerta("dest", "msg");
        
        // Assert
        assertEquals(0, enviados);
        verify(registro).registrar("dest", "msg", 0);
    }
}
```
```

---

## Lecturas Recomendadas

- Beck, K. (2002). *Test-Driven Development: By Example*
- Freeman, S. & Pryce, N. (2009). *Growing Object-Oriented Software, Guided by Tests*
- Meszaros, G. (2007). *xUnit Test Patterns: Refactoring Test Code*
- Martin, R. C. (2008). *Clean Code*, Capítulo 9: Unit Tests

