---
title: "Strategy"
subtitle: "Encapsular familia de algoritmos intercambiables"
subject: Patrones de Diseño de Comportamiento
---

(patron-strategy)=
# Strategy: Algoritmos Intercambiables

El patrón **Strategy** define una familia de algoritmos, encapsula cada uno, y los hace intercambiables, permitiendo que el algoritmo varíe independientemente de los clientes que lo usan.

:::{note} Propósito

Encapsular algoritmos intercambiables en objetos.
:::

---

## Origen e Historia

Gang of Four 1994. Surge de necesidad de intercambiar algoritmos dinámicamente sin condicionales.

## Motivación

Necesario cuando:
- Múltiples algoritmos para resolver un problema
- Cambiar algoritmo en runtime
- Evitar condicionales complejos
- Cada algoritmo es variante de algo

## Contexto

**Patrón:** Contexto + Strategy intercambiable

**Anatomía:**
- **Strategy**: Interfaz (ejecutar)
- **ConcreteStrategy**: Implementación específica
- **Context**: Usa strategy sin conocer detalles
- Cliente elige strategy en runtime

**Distinción de State vs Strategy:**
- **State**: Contexto cambia estado internamente
- **Strategy**: Cliente elige strategy

---

## Problema

```
Sin Strategy: Condicionales para elegir algoritmo
if (tipo == BURBUJA) ordenarBurbuja();
else if (tipo == QUICKSORT) ordenarQuicksort();

Con Strategy: Objeto que encapsula algoritmo
ordenador.setEstrategia(new Quicksort());
ordenador.ordenar();
```

---

## Problema

```java
// ❌ Condicionales por cada algoritmo
class OrdenadorListas {
    void ordenar(int[] array, String tipo) {
        if ("burbuja".equals(tipo)) {
            // Código de burbuja
        } else if ("quicksort".equals(tipo)) {
            // Código de quicksort
        } else if ("mergesort".equals(tipo)) {
            // Código de mergesort
        }
        // No es escalable
    }
}
```

---

## Solución: Strategy

```java
/**
 * Strategy: interfaz para algoritmos.
 */
public interface EstrategiaOrdenamiento {
    void ordenar(int[] array);
}

/**
 * Strategy concreto: Ordenamiento de burbuja.
 */
public class OrdenamientoBurbuja implements EstrategiaOrdenamiento {
    @Override
    public void ordenar(int[] array) {
        System.out.println("🫧 Ordenando con burbuja...");
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }
}

/**
 * Strategy concreto: Ordenamiento rápido.
 */
public class OrdenamientoQuicksort implements EstrategiaOrdenamiento {
    @Override
    public void ordenar(int[] array) {
        System.out.println("⚡ Ordenando con quicksort...");
        quicksort(array, 0, array.length - 1);
    }
    
    private void quicksort(int[] arr, int inicio, int fin) {
        if (inicio < fin) {
            int pivote = particionar(arr, inicio, fin);
            quicksort(arr, inicio, pivote - 1);
            quicksort(arr, pivote + 1, fin);
        }
    }
    
    private int particionar(int[] arr, int inicio, int fin) {
        int pivote = arr[fin];
        int i = inicio - 1;
        for (int j = inicio; j < fin; j++) {
            if (arr[j] < pivote) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[fin];
        arr[fin] = temp;
        return i + 1;
    }
}

/**
 * Contexto: usa la strategy.
 */
public class OrdenadorListas {
    private EstrategiaOrdenamiento estrategia;
    
    public OrdenadorListas(EstrategiaOrdenamiento estrategia) {
        this.estrategia = estrategia;
    }
    
    public void setEstrategia(EstrategiaOrdenamiento nuevaEstrategia) {
        this.estrategia = nuevaEstrategia;
    }
    
    public void ordenar(int[] array) {
        estrategia.ordenar(array);
    }
}

// ✅ Uso: Intercambiar algoritmos fácilmente
int[] datos = {5, 2, 8, 1, 9};

OrdenadorListas ordenador = new OrdenadorListas(new OrdenamientoBurbuja());
ordenador.ordenar(datos);

// Cambiar algoritmo sin modificar OrdenadorListas
ordenador.setEstrategia(new OrdenamientoQuicksort());
ordenador.ordenar(datos);
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Flexibilidad**: Cambiar algoritmo en runtime
- **Testabilidad**: Testear cada algoritmo independientemente
- **Mantenibilidad**: Agregar algoritmos sin modificar existentes
- **Separación**: Algoritmo separado de contexto

### ❌ Desventajas

- **Clases**: Muchas clases Strategy
- **Overhead**: Llamadas adicionales
- **Complejidad**: Overkill para pocos algoritmos
- **Comuniacción**: Cliente debe entender qué estrategia usar

---

## Cuándo Usarlo

✅ **Usa cuando:**
- Múltiples algoritmos para un problema
- Cambiar algoritmo en runtime
- Evitar condicionales complejos
- Ejemplos: Ordenamiento, búsqueda, compresión, pago

---

## Ejercicio

```{exercise}
:label: ej-strategy-pago

Crea sistema de pagos con Strategy:
1. Métodos: Tarjeta, Efectivo, Crypto
2. Procesar pago según método elegido
```

```{solution} ej-strategy-pago
:class: dropdown

```java
public interface EstrategiaPago {
    boolean procesar(double monto);
}

public class PagoTarjeta implements EstrategiaPago {
    private String número;
    
    public PagoTarjeta(String número) {
        this.número = número;
    }
    
    @Override
    public boolean procesar(double monto) {
        System.out.println("Procesando tarjeta: " + número);
        System.out.println("Pagando $" + monto);
        return true;
    }
}

public class PagoEfectivo implements EstrategiaPago {
    @Override
    public boolean procesar(double monto) {
        System.out.println("💵 Pagando en efectivo: $" + monto);
        return true;
    }
}

public class ProcesadorPagos {
    private EstrategiaPago estrategia;
    
    public void setEstrategia(EstrategiaPago e) {
        estrategia = e;
    }
    
    public boolean pagar(double monto) {
        return estrategia.procesar(monto);
    }
}

// Uso
ProcesadorPagos procesador = new ProcesadorPagos();
procesador.setEstrategia(new PagoTarjeta("1234-5678"));
procesador.pagar(100);

procesador.setEstrategia(new PagoEfectivo());
procesador.pagar(50);
```
```
