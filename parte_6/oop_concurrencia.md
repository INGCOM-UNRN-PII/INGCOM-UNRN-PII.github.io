---
title: "OOP 11: Concurrencia y Objetos"
subtitle: "Diseño Orientado a Objetos en Entornos Concurrentes"
subject: Programación Orientada a Objetos
---

(oop11-concurrencia)=
# OOP 11: Concurrencia y Objetos

Los programas modernos rara vez se ejecutan en un único hilo. Desde interfaces gráficas que responden mientras procesan datos en segundo plano, hasta servidores que atienden miles de solicitudes simultáneas, la **concurrencia** es parte fundamental del desarrollo de software. Pero cuando múltiples hilos acceden a los mismos objetos, surgen problemas sutiles y difíciles de detectar.

Este capítulo explora cómo diseñar objetos que funcionen correctamente en entornos concurrentes, aplicando los principios de OOP que ya conocemos.

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, serás capaz de:

1. Comprender los desafíos de la concurrencia en programación orientada a objetos
2. Identificar condiciones de carrera y problemas de visibilidad
3. Diseñar objetos thread-safe usando inmutabilidad
4. Aplicar técnicas de sincronización adecuadamente
5. Usar patrones de diseño específicos para concurrencia
6. Evitar errores comunes como deadlocks y race conditions
:::

---

(por-que-concurrencia)=
## ¿Por Qué Concurrencia?

(motivacion-concurrencia)=
### Motivación

La concurrencia permite que un programa realice múltiples tareas aparentemente al mismo tiempo:

```
┌─────────────────────────────────────────────────────────────────┐
│              ESCENARIOS DE CONCURRENCIA                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   INTERFAZ GRÁFICA                                              │
│   ─────────────────                                             │
│   • La UI debe responder mientras se descarga un archivo        │
│   • Sin concurrencia: la aplicación se "congela"                │
│                                                                 │
│   SERVIDOR WEB                                                  │
│   ────────────                                                  │
│   • Miles de usuarios simultáneos                               │
│   • Cada solicitud se procesa en paralelo                       │
│                                                                 │
│   PROCESAMIENTO DE DATOS                                        │
│   ───────────────────────                                       │
│   • Dividir trabajo entre múltiples núcleos                     │
│   • Reducir tiempo total de procesamiento                       │
│                                                                 │
│   SISTEMAS REACTIVOS                                            │
│   ───────────────────                                           │
│   • Responder a eventos asincrónicos                            │
│   • Sensores, mensajes, actualizaciones                         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

(hilos-y-objetos)=
### Hilos y Objetos

Un **hilo** (thread) es una secuencia de ejecución independiente dentro de un programa. Múltiples hilos comparten el mismo espacio de memoria, lo que significa que pueden acceder a los mismos objetos.

```java
// Un objeto compartido entre hilos
public class Contador {
    private int valor = 0;
    
    public void incrementar() {
        valor++;  // ¿Es esto seguro?
    }
    
    public int getValor() {
        return valor;
    }
}

// Dos hilos acceden al mismo contador
Contador contador = new Contador();

Thread hilo1 = new Thread(() -> {
    for (int i = 0; i < 10000; i++) {
        contador.incrementar();
    }
});

Thread hilo2 = new Thread(() -> {
    for (int i = 0; i < 10000; i++) {
        contador.incrementar();
    }
});

hilo1.start();
hilo2.start();
hilo1.join();
hilo2.join();

// ¿Cuánto vale contador.getValor()?
// Esperamos 20000, pero podría ser menos...
System.out.println(contador.getValor());
```

---

(problemas-concurrencia)=
## Problemas de Concurrencia

(condicion-carrera)=
### Condición de Carrera (Race Condition)

Una **condición de carrera** ocurre cuando el resultado de un programa depende del orden impredecible en que se ejecutan las operaciones de diferentes hilos.

```java
// ❌ PROBLEMA: Race condition
public class Contador {
    private int valor = 0;
    
    public void incrementar() {
        valor++;  // Parece atómico, pero NO lo es
    }
}
```

La operación `valor++` en realidad son tres pasos:

```
┌─────────────────────────────────────────────────────────────────┐
│        ANATOMÍA DE valor++ (tres operaciones)                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   1. LEER:   Obtener el valor actual de memoria → registro     │
│   2. SUMAR:  Incrementar el valor en el registro               │
│   3. ESCRIBIR: Guardar el nuevo valor en memoria               │
│                                                                 │
│   PROBLEMA: Otro hilo puede intervenir entre pasos             │
│                                                                 │
│   Hilo A                    Hilo B                              │
│   ──────                    ──────                              │
│   LEER valor (= 5)                                              │
│                             LEER valor (= 5)                    │
│   SUMAR (5 + 1 = 6)                                             │
│                             SUMAR (5 + 1 = 6)                   │
│   ESCRIBIR (valor = 6)                                          │
│                             ESCRIBIR (valor = 6)                │
│                                                                 │
│   RESULTADO: valor = 6 (¡debería ser 7!)                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

(problema-visibilidad)=
### Problema de Visibilidad

Los cambios realizados por un hilo pueden no ser visibles inmediatamente para otros hilos debido a cachés del procesador y optimizaciones.

```java
// ❌ PROBLEMA: Visibilidad
public class Bandera {
    private boolean detener = false;
    
    public void solicitar Parada() {
        detener = true;  // Hilo principal
    }
    
    public void ejecutar() {
        while (!detener) {  // Otro hilo podría no ver el cambio
            // hacer trabajo
        }
    }
}
```

El hilo de trabajo podría nunca ver que `detener` cambió a `true` porque lee de su caché local.

(atomicidad)=
### Falta de Atomicidad

Operaciones que parecen únicas en realidad son múltiples pasos que pueden ser interrumpidos.

```java
// ❌ PROBLEMA: Operación no atómica
public class CuentaBancaria {
    private double saldo;
    
    public void transferir(CuentaBancaria destino, double monto) {
        if (this.saldo >= monto) {     // Paso 1: verificar
            this.saldo -= monto;        // Paso 2: restar
            destino.saldo += monto;     // Paso 3: sumar
        }
        // Otro hilo podría modificar los saldos entre pasos
    }
}
```

---

(inmutabilidad-solucion)=
## Inmutabilidad: La Mejor Solución

(objetos-inmutables-concurrencia)=
### Objetos Inmutables

La forma más segura de manejar concurrencia es **eliminar el estado mutable compartido**. Los objetos inmutables son inherentemente thread-safe porque nunca cambian.

```java
// ✓ BIEN: Objeto inmutable - thread-safe por diseño
public final class Dinero {
    private final int centavos;
    private final String moneda;
    
    public Dinero(int centavos, String moneda) {
        this.centavos = centavos;
        this.moneda = moneda;
    }
    
    public Dinero sumar(Dinero otro) {
        if (!this.moneda.equals(otro.moneda)) {
            throw new IllegalArgumentException("Monedas diferentes");
        }
        return new Dinero(this.centavos + otro.centavos, this.moneda);
    }
    
    public Dinero multiplicar(int factor) {
        return new Dinero(this.centavos * factor, this.moneda);
    }
    
    public int getCentavos() { return centavos; }
    public String getMoneda() { return moneda; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dinero)) return false;
        Dinero otro = (Dinero) o;
        return centavos == otro.centavos && moneda.equals(otro.moneda);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(centavos, moneda);
    }
}
```

:::{important} Reglas para Inmutabilidad

1. Declarar la clase como `final` (no se puede extender)
2. Todos los campos `private final`
3. No proporcionar setters
4. Si hay campos de tipos mutables, hacer copias defensivas
5. No exponer referencias a objetos mutables internos
:::

(copias-defensivas)=
### Copias Defensivas

Cuando un objeto inmutable contiene referencias a objetos mutables, se deben hacer copias:

```java
public final class Evento {
    private final String nombre;
    private final Date fecha;  // Date es mutable!
    private final List<String> participantes;  // List es mutable!
    
    public Evento(String nombre, Date fecha, List<String> participantes) {
        this.nombre = nombre;
        // Copias defensivas en el constructor
        this.fecha = new Date(fecha.getTime());
        this.participantes = new ArrayList<>(participantes);
    }
    
    public Date getFecha() {
        // Copia defensiva en el getter
        return new Date(fecha.getTime());
    }
    
    public List<String> getParticipantes() {
        // Retornar copia inmutable
        return Collections.unmodifiableList(new ArrayList<>(participantes));
    }
}
```

:::{tip}
En Java moderno, usá `LocalDate` y `LocalDateTime` en lugar de `Date` — son inmutables por diseño.
:::

---

(sincronizacion)=
## Sincronización

Cuando la inmutabilidad no es posible, se necesita **sincronización** para coordinar el acceso de múltiples hilos.

(synchronized-keyword)=
### La Palabra Clave `synchronized`

`synchronized` garantiza que solo un hilo puede ejecutar un bloque de código a la vez.

```java
// ✓ BIEN: Contador thread-safe con synchronized
public class ContadorSeguro {
    private int valor = 0;
    
    public synchronized void incrementar() {
        valor++;  // Solo un hilo a la vez puede ejecutar esto
    }
    
    public synchronized int getValor() {
        return valor;
    }
}
```

(monitor-lock)=
### El Monitor (Lock)

Cada objeto en Java tiene un **monitor** asociado. `synchronized` adquiere el monitor del objeto:

```java
public class CuentaBancaria {
    private double saldo;
    
    // synchronized en método de instancia usa 'this' como monitor
    public synchronized void depositar(double monto) {
        saldo += monto;
    }
    
    // Equivalente a:
    public void depositar2(double monto) {
        synchronized (this) {
            saldo += monto;
        }
    }
    
    // Se puede usar otro objeto como monitor
    private final Object lock = new Object();
    
    public void depositar3(double monto) {
        synchronized (lock) {
            saldo += monto;
        }
    }
}
```

(sincronizacion-granular)=
### Sincronización Granular

Sincronizar métodos completos puede ser demasiado restrictivo. Es mejor sincronizar solo lo necesario:

```java
public class Inventario {
    private final Map<String, Integer> stock = new HashMap<>();
    private final Object lockStock = new Object();
    
    private int operacionesTotales = 0;
    private final Object lockOperaciones = new Object();
    
    public void agregarStock(String producto, int cantidad) {
        synchronized (lockStock) {
            stock.merge(producto, cantidad, Integer::sum);
        }
        // Estadísticas separadas, no bloquean el stock
        synchronized (lockOperaciones) {
            operacionesTotales++;
        }
    }
    
    public int consultarStock(String producto) {
        synchronized (lockStock) {
            return stock.getOrDefault(producto, 0);
        }
    }
}
```

---

(volatile-keyword)=
## La Palabra Clave `volatile`

`volatile` garantiza visibilidad: los cambios son visibles inmediatamente para todos los hilos.

```java
public class TareaEnSegundoPlano {
    private volatile boolean cancelada = false;
    
    public void cancelar() {
        cancelada = true;  // Visible inmediatamente para otros hilos
    }
    
    public void ejecutar() {
        while (!cancelada) {
            // procesar
        }
        System.out.println("Tarea cancelada correctamente");
    }
}
```

:::{warning}
`volatile` NO garantiza atomicidad. `contador++` sigue siendo una race condition aunque `contador` sea volatile.
:::

```
┌─────────────────────────────────────────────────────────────────┐
│            volatile vs synchronized                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   volatile                      synchronized                    │
│   ────────                      ────────────                    │
│   • Garantiza visibilidad       • Garantiza visibilidad         │
│   • NO garantiza atomicidad     • Garantiza atomicidad          │
│   • Bajo costo                  • Mayor costo                   │
│   • Solo para variables         • Para bloques de código        │
│   • No bloquea otros hilos      • Bloquea otros hilos           │
│                                                                 │
│   Usar volatile:                Usar synchronized:              │
│   • Flags (boolean)             • Operaciones compuestas        │
│   • Publicar referencias        • Múltiples variables           │
│   • Estados simples             • Invariantes complejos         │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

(clases-atomicas)=
## Clases Atómicas

Java proporciona clases en `java.util.concurrent.atomic` que ofrecen operaciones atómicas sin sincronización explícita:

```java
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicLong;

public class ContadorAtomic {
    private final AtomicInteger valor = new AtomicInteger(0);
    
    public void incrementar() {
        valor.incrementAndGet();  // Atómico, thread-safe
    }
    
    public void sumar(int delta) {
        valor.addAndGet(delta);  // También atómico
    }
    
    public boolean incrementarSiMenorQue(int limite) {
        // Compare-and-set atómico
        int actual;
        do {
            actual = valor.get();
            if (actual >= limite) {
                return false;
            }
        } while (!valor.compareAndSet(actual, actual + 1));
        return true;
    }
    
    public int getValor() {
        return valor.get();
    }
}
```

(atomic-reference)=
### AtomicReference para Objetos

```java
public class CacheSimple<T> {
    private final AtomicReference<T> valor = new AtomicReference<>();
    
    public void actualizar(T nuevoValor) {
        valor.set(nuevoValor);
    }
    
    public T obtener() {
        return valor.get();
    }
    
    public boolean actualizarSi(T esperado, T nuevo) {
        return valor.compareAndSet(esperado, nuevo);
    }
}
```

---

(colecciones-concurrentes)=
## Colecciones Concurrentes

Las colecciones estándar (`ArrayList`, `HashMap`, etc.) no son thread-safe. Java ofrece alternativas concurrentes:

```java
import java.util.concurrent.*;

public class EjemplosColecciones {
    
    // ConcurrentHashMap: mejor que Collections.synchronizedMap
    private final ConcurrentMap<String, Usuario> usuarios = 
        new ConcurrentHashMap<>();
    
    public void registrar(String id, Usuario usuario) {
        // putIfAbsent es atómico
        usuarios.putIfAbsent(id, usuario);
    }
    
    public void actualizar(String id, String nuevoNombre) {
        // computeIfPresent es atómico
        usuarios.computeIfPresent(id, (k, u) -> {
            u.setNombre(nuevoNombre);
            return u;
        });
    }
    
    // CopyOnWriteArrayList: ideal para lecturas frecuentes
    private final List<Observador> observadores = 
        new CopyOnWriteArrayList<>();
    
    public void agregarObservador(Observador obs) {
        observadores.add(obs);
    }
    
    public void notificar(Evento evento) {
        // Iterar es seguro aunque otro hilo modifique la lista
        for (Observador obs : observadores) {
            obs.actualizar(evento);
        }
    }
    
    // BlockingQueue: para productor-consumidor
    private final BlockingQueue<Tarea> colaTareas = 
        new LinkedBlockingQueue<>(100);
    
    public void encolar(Tarea tarea) throws InterruptedException {
        colaTareas.put(tarea);  // Bloquea si la cola está llena
    }
    
    public Tarea obtener() throws InterruptedException {
        return colaTareas.take();  // Bloquea si la cola está vacía
    }
}
```

```
┌─────────────────────────────────────────────────────────────────┐
│          COLECCIONES CONCURRENTES                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   Colección                 Uso Principal                       │
│   ──────────────────────────────────────────────────────        │
│   ConcurrentHashMap         Mapa general, alta concurrencia     │
│   ConcurrentLinkedQueue     Cola no bloqueante                  │
│   CopyOnWriteArrayList      Lecturas muy frecuentes, pocas      │
│                             escrituras                          │
│   CopyOnWriteArraySet       Set con pocas modificaciones        │
│   BlockingQueue             Productor-consumidor                │
│   ConcurrentSkipListMap     Mapa ordenado concurrente           │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

(patrones-concurrencia)=
## Patrones de Diseño para Concurrencia

(patron-inmutable)=
### Patrón Inmutable

Ya lo vimos: crear objetos que no pueden cambiar después de su construcción.

(patron-confinamiento)=
### Patrón de Confinamiento (Thread Confinement)

Limitar el acceso a un objeto a un único hilo elimina la necesidad de sincronización.

```java
public class ProcesamientoConfinado {
    
    public void procesarArchivos(List<Path> archivos) {
        // ThreadLocal confina datos a cada hilo
        ThreadLocal<StringBuilder> buffer = 
            ThreadLocal.withInitial(StringBuilder::new);
        
        archivos.parallelStream().forEach(archivo -> {
            // Cada hilo tiene su propio StringBuilder
            StringBuilder sb = buffer.get();
            sb.setLength(0);  // Limpiar
            
            // Procesar archivo usando el buffer local
            procesarArchivo(archivo, sb);
        });
    }
    
    private void procesarArchivo(Path archivo, StringBuilder buffer) {
        // buffer es privado de este hilo, no necesita sincronización
    }
}
```

(patron-publicacion-segura)=
### Publicación Segura

Asegurar que un objeto es visible correctamente cuando se comparte entre hilos.

```java
public class PublicacionSegura {
    
    // ❌ MAL: Publicación insegura
    public Recurso recurso;  // Otro hilo podría ver un objeto parcialmente construido
    
    // ✓ BIEN: Publicación con volatile
    private volatile Recurso recursoSeguro;
    
    // ✓ BIEN: Publicación con sincronización
    private Recurso recursoSincronizado;
    
    public synchronized void setRecurso(Recurso r) {
        this.recursoSincronizado = r;
    }
    
    public synchronized Recurso getRecurso() {
        return recursoSincronizado;
    }
    
    // ✓ BIEN: Inicialización estática (thread-safe por JVM)
    private static final Recurso RECURSO_ESTATICO = new Recurso();
    
    // ✓ BIEN: Campo final en constructor
    private final Recurso recursoFinal;
    
    public PublicacionSegura() {
        this.recursoFinal = new Recurso();
    }
}
```

(patron-productor-consumidor)=
### Patrón Productor-Consumidor

Desacoplar la producción de datos de su consumo usando una cola.

```java
public class SistemaMensajes {
    private final BlockingQueue<Mensaje> cola;
    private final ExecutorService consumidores;
    private volatile boolean activo = true;
    
    public SistemaMensajes(int capacidad, int numConsumidores) {
        this.cola = new ArrayBlockingQueue<>(capacidad);
        this.consumidores = Executors.newFixedThreadPool(numConsumidores);
        
        // Iniciar consumidores
        for (int i = 0; i < numConsumidores; i++) {
            consumidores.submit(this::consumir);
        }
    }
    
    // Productores llaman a este método
    public void enviar(Mensaje mensaje) throws InterruptedException {
        if (activo) {
            cola.put(mensaje);  // Bloquea si la cola está llena
        }
    }
    
    // Ejecutado por cada consumidor
    private void consumir() {
        while (activo || !cola.isEmpty()) {
            try {
                Mensaje mensaje = cola.poll(100, TimeUnit.MILLISECONDS);
                if (mensaje != null) {
                    procesar(mensaje);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private void procesar(Mensaje mensaje) {
        // Lógica de procesamiento
    }
    
    public void detener() {
        activo = false;
        consumidores.shutdown();
    }
}
```

---

(deadlock)=
## Deadlock: El Abrazo Mortal

Un **deadlock** ocurre cuando dos o más hilos se bloquean mutuamente esperando recursos que el otro tiene.

```java
// ❌ MAL: Posible deadlock
public class TransferenciaPeligrosa {
    
    public void transferir(CuentaBancaria origen, CuentaBancaria destino, 
                          double monto) {
        synchronized (origen) {          // Hilo 1 toma lock de A
            synchronized (destino) {     // Hilo 1 espera lock de B
                origen.restar(monto);
                destino.sumar(monto);
            }
        }
    }
}

// Si Hilo 1 hace transferir(A, B) y Hilo 2 hace transferir(B, A)
// simultáneamente: DEADLOCK
```

```
┌─────────────────────────────────────────────────────────────────┐
│                    DEADLOCK                                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│       Hilo 1                          Hilo 2                    │
│       ──────                          ──────                    │
│         │                               │                       │
│    synchronized(A)                 synchronized(B)              │
│    ┌─────────────┐                 ┌─────────────┐              │
│    │  tiene A    │                 │  tiene B    │              │
│    └─────────────┘                 └─────────────┘              │
│         │                               │                       │
│         │ quiere B                      │ quiere A              │
│         ▼                               ▼                       │
│    ╔═════════════╗                 ╔═════════════╗              │
│    ║  BLOQUEADO  ║◄───────────────►║  BLOQUEADO  ║              │
│    ║  esperando  ║                 ║  esperando  ║              │
│    ║     B       ║                 ║     A       ║              │
│    ╚═════════════╝                 ╚═════════════╝              │
│                                                                 │
│                    ¡DEADLOCK!                                   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

(evitar-deadlock)=
### Cómo Evitar Deadlocks

**1. Orden consistente de locks:**

```java
// ✓ BIEN: Siempre adquirir locks en el mismo orden
public class TransferenciaSegura {
    
    public void transferir(CuentaBancaria origen, CuentaBancaria destino, 
                          double monto) {
        // Ordenar por ID para garantizar orden consistente
        CuentaBancaria primero = origen.getId() < destino.getId() ? origen : destino;
        CuentaBancaria segundo = origen.getId() < destino.getId() ? destino : origen;
        
        synchronized (primero) {
            synchronized (segundo) {
                origen.restar(monto);
                destino.sumar(monto);
            }
        }
    }
}
```

**2. Usar timeouts:**

```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class TransferenciaConTimeout {
    private final ReentrantLock lock = new ReentrantLock();
    
    public boolean transferir(CuentaBancaria origen, CuentaBancaria destino,
                             double monto) {
        try {
            if (origen.getLock().tryLock(1, TimeUnit.SECONDS)) {
                try {
                    if (destino.getLock().tryLock(1, TimeUnit.SECONDS)) {
                        try {
                            origen.restar(monto);
                            destino.sumar(monto);
                            return true;
                        } finally {
                            destino.getLock().unlock();
                        }
                    }
                } finally {
                    origen.getLock().unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return false;  // No se pudo completar, reintentar después
    }
}
```

**3. Reducir el alcance de la sincronización:**

```java
// ✓ BIEN: Minimizar tiempo con locks
public void procesar(Datos datos) {
    // Preparación sin lock
    Resultado parcial = calcularSinEstadoCompartido(datos);
    
    // Solo sincronizar la actualización del estado
    synchronized (this) {
        estado.actualizar(parcial);
    }
    
    // Post-procesamiento sin lock
    notificarCompletado(parcial);
}
```

---

(executor-service)=
## Ejecutores y Pool de Hilos

Crear hilos manualmente es costoso y propenso a errores. Java proporciona `ExecutorService` para gestionar pools de hilos.

```java
import java.util.concurrent.*;

public class ProcesadorTareas {
    private final ExecutorService executor;
    
    public ProcesadorTareas(int numHilos) {
        // Pool fijo de hilos
        this.executor = Executors.newFixedThreadPool(numHilos);
    }
    
    public void ejecutarTarea(Runnable tarea) {
        executor.submit(tarea);
    }
    
    public <T> Future<T> ejecutarConResultado(Callable<T> tarea) {
        return executor.submit(tarea);
    }
    
    public void procesarEnParalelo(List<Trabajo> trabajos) 
            throws InterruptedException, ExecutionException {
        
        // Convertir trabajos a Callables
        List<Callable<Resultado>> tareas = trabajos.stream()
            .map(t -> (Callable<Resultado>) t::procesar)
            .toList();
        
        // Ejecutar todas y esperar resultados
        List<Future<Resultado>> futuros = executor.invokeAll(tareas);
        
        // Recolectar resultados
        for (Future<Resultado> futuro : futuros) {
            Resultado r = futuro.get();  // Bloquea hasta que esté listo
            // usar resultado
        }
    }
    
    public void detener() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

(tipos-executors)=
### Tipos de Ejecutores

```java
// Pool fijo: número constante de hilos
ExecutorService fijo = Executors.newFixedThreadPool(4);

// Pool cached: crea hilos según demanda, los reutiliza
ExecutorService cached = Executors.newCachedThreadPool();

// Un solo hilo: ejecuta tareas secuencialmente
ExecutorService single = Executors.newSingleThreadExecutor();

// Programado: ejecuta tareas con delay o periódicamente
ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2);

scheduled.schedule(() -> System.out.println("Después de 5 seg"), 
                   5, TimeUnit.SECONDS);

scheduled.scheduleAtFixedRate(() -> System.out.println("Cada minuto"),
                               0, 1, TimeUnit.MINUTES);
```

---

(completable-future)=
## CompletableFuture: Programación Asincrónica

`CompletableFuture` permite componer operaciones asincrónicas de forma más elegante que con `Future` tradicional.

```java
import java.util.concurrent.CompletableFuture;

public class ServicioAsync {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    
    public CompletableFuture<Usuario> buscarUsuario(String id) {
        return CompletableFuture.supplyAsync(() -> {
            // Operación costosa (ej: llamada a BD)
            return repositorio.buscar(id);
        }, executor);
    }
    
    public CompletableFuture<Perfil> obtenerPerfilCompleto(String usuarioId) {
        return buscarUsuario(usuarioId)
            .thenCompose(usuario -> {
                // Encadenar otra operación async
                return buscarPreferencias(usuario);
            })
            .thenCombine(buscarHistorial(usuarioId), (prefs, historial) -> {
                // Combinar resultados de operaciones paralelas
                return new Perfil(prefs, historial);
            })
            .exceptionally(error -> {
                // Manejar errores
                log.error("Error obteniendo perfil", error);
                return Perfil.porDefecto();
            });
    }
    
    public void procesarMultiples(List<String> ids) {
        // Ejecutar todas las búsquedas en paralelo
        List<CompletableFuture<Usuario>> futuros = ids.stream()
            .map(this::buscarUsuario)
            .toList();
        
        // Esperar que todas terminen
        CompletableFuture<Void> todas = CompletableFuture.allOf(
            futuros.toArray(new CompletableFuture[0])
        );
        
        todas.thenRun(() -> {
            List<Usuario> usuarios = futuros.stream()
                .map(CompletableFuture::join)
                .toList();
            procesarUsuarios(usuarios);
        });
    }
}
```

---

(principios-diseno-concurrente)=
## Principios de Diseño Concurrente

(recomendaciones)=
### Recomendaciones

```
┌─────────────────────────────────────────────────────────────────┐
│         PRINCIPIOS PARA CÓDIGO CONCURRENTE                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   1. PREFERIR INMUTABILIDAD                                     │
│      Los objetos inmutables son thread-safe por definición      │
│                                                                 │
│   2. MINIMIZAR ESTADO COMPARTIDO                                │
│      Menos estado compartido = menos sincronización necesaria   │
│                                                                 │
│   3. CONFINAR ESTADO A HILOS                                    │
│      Si solo un hilo accede a un dato, no hay problema          │
│                                                                 │
│   4. USAR ABSTRACCIONES DE ALTO NIVEL                           │
│      ExecutorService, CompletableFuture, colecciones            │
│      concurrentes en lugar de synchronized manual               │
│                                                                 │
│   5. SINCRONIZAR EL MENOR TIEMPO POSIBLE                        │
│      Entrar y salir de zonas sincronizadas rápidamente          │
│                                                                 │
│   6. DOCUMENTAR DECISIONES DE CONCURRENCIA                      │
│      ¿Es thread-safe? ¿Bajo qué condiciones?                    │
│                                                                 │
│   7. TESTEAR EXHAUSTIVAMENTE                                    │
│      Los bugs de concurrencia son difíciles de reproducir       │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

(documentar-thread-safety)=
### Documentar Thread Safety

```java
/**
 * Registro de usuarios thread-safe.
 * 
 * <p>Esta clase es segura para uso concurrente. Todos los métodos
 * públicos pueden ser invocados simultáneamente desde múltiples hilos.
 * 
 * <p>Implementación: usa ConcurrentHashMap internamente.
 */
@ThreadSafe
public class RegistroUsuarios {
    
    private final ConcurrentMap<String, Usuario> usuarios = 
        new ConcurrentHashMap<>();
    
    /**
     * Registra un nuevo usuario.
     * Esta operación es atómica: el usuario se registra completamente
     * o no se registra en absoluto.
     */
    public boolean registrar(Usuario usuario) {
        return usuarios.putIfAbsent(usuario.getId(), usuario) == null;
    }
}
```

---

(resumen-concurrencia)=
## Resumen

:::{tip} Conceptos Clave

1. **Concurrencia** permite ejecutar múltiples tareas simultáneamente
2. **Race conditions** ocurren cuando hilos acceden datos compartidos sin coordinación
3. **Inmutabilidad** es la mejor solución: objetos que no cambian son thread-safe
4. **synchronized** garantiza acceso exclusivo pero tiene costo
5. **volatile** garantiza visibilidad pero no atomicidad
6. **Clases atómicas** (`AtomicInteger`, etc.) ofrecen operaciones seguras sin locks
7. **Colecciones concurrentes** están diseñadas para uso multi-hilo
8. **Deadlocks** ocurren cuando hilos se esperan mutuamente
9. **ExecutorService** gestiona pools de hilos eficientemente
10. **CompletableFuture** permite composición de operaciones asincrónicas
:::

```
┌─────────────────────────────────────────────────────────────────┐
│           DECISIONES DE DISEÑO PARA CONCURRENCIA                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ¿El objeto puede ser inmutable?                               │
│         │                                                       │
│         ├─── SÍ ───► Hacerlo inmutable (mejor opción)           │
│         │                                                       │
│         └─── NO ───► ¿Puede confinarse a un hilo?               │
│                            │                                    │
│                            ├─── SÍ ───► ThreadLocal o diseño    │
│                            │            que evite compartir      │
│                            │                                    │
│                            └─── NO ───► ¿Operaciones simples?   │
│                                              │                  │
│                                              ├─── SÍ ───► Atomic│
│                                              │                  │
│                                              └─── NO ───► sync  │
│                                                          o Lock │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

```{exercise}
:label: ej-contador-seguro

Implementá una clase `ContadorLimitado` que:
- Permita incrementar un contador hasta un máximo
- Sea thread-safe
- Retorne `true` si el incremento fue exitoso, `false` si se alcanzó el límite

```java
public class ContadorLimitado {
    // Implementar
    
    public ContadorLimitado(int limite) { }
    
    public boolean incrementar() { }
    
    public int getValor() { }
}
```
```

```{solution} ej-contador-seguro
:class: dropdown

```java
import java.util.concurrent.atomic.AtomicInteger;

public class ContadorLimitado {
    private final AtomicInteger valor = new AtomicInteger(0);
    private final int limite;
    
    public ContadorLimitado(int limite) {
        if (limite < 0) {
            throw new IllegalArgumentException("Límite debe ser >= 0");
        }
        this.limite = limite;
    }
    
    public boolean incrementar() {
        int actual;
        int nuevo;
        do {
            actual = valor.get();
            if (actual >= limite) {
                return false;
            }
            nuevo = actual + 1;
        } while (!valor.compareAndSet(actual, nuevo));
        return true;
    }
    
    public int getValor() {
        return valor.get();
    }
    
    public int getLimite() {
        return limite;
    }
}
```
```

```{exercise}
:label: ej-cache-thread-safe

Diseñá una clase `CacheSimple<K, V>` thread-safe que:
- Permita guardar y obtener valores por clave
- Tenga un método `obtenerOCalcular(K clave, Supplier<V> calculador)` que retorne el valor si existe, o lo calcule (una sola vez) si no existe
- Use la colección concurrente apropiada

```

```{solution} ej-cache-thread-safe
:class: dropdown

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class CacheSimple<K, V> {
    private final ConcurrentMap<K, V> cache = new ConcurrentHashMap<>();
    
    public void guardar(K clave, V valor) {
        cache.put(clave, valor);
    }
    
    public V obtener(K clave) {
        return cache.get(clave);
    }
    
    public V obtenerOCalcular(K clave, Supplier<V> calculador) {
        // computeIfAbsent garantiza que el calculador se ejecuta
        // solo una vez, incluso con múltiples hilos
        return cache.computeIfAbsent(clave, k -> calculador.get());
    }
    
    public void eliminar(K clave) {
        cache.remove(clave);
    }
    
    public void limpiar() {
        cache.clear();
    }
    
    public int tamaño() {
        return cache.size();
    }
}
```
```

```{exercise}
:label: ej-productor-consumidor

Implementá un sistema simple de productor-consumidor donde:
- Múltiples productores generan números enteros
- Un consumidor suma todos los números
- El sistema se detiene cuando se reciben N números

```

```{solution} ej-productor-consumidor
:class: dropdown

```java
import java.util.concurrent.*;

public class SumadorConcurrente {
    private final BlockingQueue<Integer> cola = new LinkedBlockingQueue<>(100);
    private final AtomicInteger suma = new AtomicInteger(0);
    private final AtomicInteger contadorRecibidos = new AtomicInteger(0);
    private final int objetivo;
    private final CountDownLatch completado;
    
    public SumadorConcurrente(int objetivo) {
        this.objetivo = objetivo;
        this.completado = new CountDownLatch(1);
    }
    
    // Método para productores
    public void producir(int valor) throws InterruptedException {
        if (contadorRecibidos.get() < objetivo) {
            cola.put(valor);
        }
    }
    
    // Tarea del consumidor
    public void consumir() {
        while (contadorRecibidos.get() < objetivo) {
            try {
                Integer valor = cola.poll(100, TimeUnit.MILLISECONDS);
                if (valor != null) {
                    suma.addAndGet(valor);
                    if (contadorRecibidos.incrementAndGet() >= objetivo) {
                        completado.countDown();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    public int esperarResultado() throws InterruptedException {
        completado.await();
        return suma.get();
    }
    
    public static void main(String[] args) throws Exception {
        SumadorConcurrente sumador = new SumadorConcurrente(1000);
        
        ExecutorService productores = Executors.newFixedThreadPool(4);
        ExecutorService consumidor = Executors.newSingleThreadExecutor();
        
        // Iniciar consumidor
        consumidor.submit(sumador::consumir);
        
        // Iniciar productores
        for (int i = 0; i < 4; i++) {
            final int producerId = i;
            productores.submit(() -> {
                for (int j = 0; j < 250; j++) {
                    try {
                        sumador.producir(1);  // Cada productor envía 250 unos
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
        
        int resultado = sumador.esperarResultado();
        System.out.println("Suma total: " + resultado);  // Debería ser 1000
        
        productores.shutdown();
        consumidor.shutdown();
    }
}
```
```

---

## Lecturas Recomendadas

- Goetz, Brian et al. "Java Concurrency in Practice"
- Lea, Doug. "Concurrent Programming in Java"
- Oracle. "The Java Tutorials: Concurrency"
