---
title: "Patrón Flyweight"
subtitle: "Compartir datos entre múltiples objetos"
subject: Patrones de Diseño Estructurales
---

(patron-flyweight)=
# Flyweight: Compartir Datos

El patrón **Flyweight** usa compartición para soportar grandes cantidades de objetos granulares eficientemente, separando estado intrínseco (compartido) del extrínseco (particular).

:::{note} Propósito

Reducir uso de memoria compartiendo datos entre múltiples objetos.
:::

---

## Origen e Historia

Gang of Four 1994. Surge de optimización de memoria en juegos/gráficos. Inspirado en el concepto de "compartir" datos comunes.

## Motivación

Necesario cuando:
- Millones de objetos similares consumirían demasiada memoria
- Muchos objetos tienen estado común reutilizable
- Crear nuevas instancias es costoso
- Trade-off: CPU + acceso extra por memoria

## Contexto

**Patrón:** Estado intrínseco (compartido) vs. extrínseco (particular)

**Anatomía:**
- **Flyweight**: Objeto inmutable compartible
- **Factory**: Crea/cachea flyweights
- **Cliente**: Referencia a flyweight + estado extrínseco
- Millones de referencias vs. una copia de flyweight

**Ejemplo:** 1M árboles = 1 flyweight de "Pino" + 1M referencias

---

## Problema

**Estado intrínseco**: Datos que se pueden compartir (ej: forma, color)
**Estado extrínseco**: Datos únicos por instancia (ej: posición, ID)

```
Sin Flyweight:   N objetos × tamaño completo = mucha memoria
Con Flyweight:   1 flyweight × tamaño completo + N referencias extrínsecas = poca memoria
```

---

## Problema

```java
// ❌ Millones de árboles en un bosque virtual: cada uno copia su forma y textura
class Árbol {
    private String especie;        // "Pino", "Roble" - repetido!
    private String textura;        // Datos de imagen - repetido!
    private double x, y, z;        // Posición única
    private double altura;         // Altura única
}

// 1 millón de árboles × 2MB cada uno = 2GB de memoria!
List<Árbol> bosque = new ArrayList<>();
for (int i = 0; i < 1_000_000; i++) {
    bosque.add(new Árbol("Pino", "textura.png", x, y, z, altura));
}
```

---

## Solución: Flyweight

```java
/**
 * Datos compartidos (Flyweight): estado intrínseco.
 */
public class TipoÁrbol {
    private final String especie;
    private final byte[] texturaComprimida;
    
    public TipoÁrbol(String especie, byte[] textura) {
        this.especie = especie;
        this.texturaComprimida = textura;
    }
    
    public String getEspecie() { return especie; }
    public byte[] getTextura() { return texturaComprimida; }
}

/**
 * Factory para compartir Flyweights.
 */
public class FábricaTipoÁrbol {
    private static final Map<String, TipoÁrbol> tipos = new HashMap<>();
    
    public static TipoÁrbol obtenerTipo(String especie, byte[] textura) {
        if (!tipos.containsKey(especie)) {
            tipos.put(especie, new TipoÁrbol(especie, textura));
        }
        return tipos.get(especie);
    }
}

/**
 * Árbol con estado extrínseco (posición, altura).
 */
public class Árbol {
    private TipoÁrbol tipo;     // Compartido (Flyweight)
    private double x, y, z;     // Extrínseco: posición
    private double altura;      // Extrínseco: altura
    
    public Árbol(TipoÁrbol tipo, double x, double y, double z, double altura) {
        this.tipo = tipo;
        this.x = x;
        this.y = y;
        this.z = z;
        this.altura = altura;
    }
    
    public void dibujar() {
        System.out.println("Dibujando " + tipo.getEspecie() + 
                         " en (" + x + "," + y + "," + z + "), altura: " + altura);
    }
}

/**
 * Bosque que reutiliza Flyweights.
 */
public class Bosque {
    private List<Árbol> árboles = new ArrayList<>();
    
    public void plantarÁrbol(String especie, double x, double y, double z, double altura) {
        // Obtener tipo compartido
        TipoÁrbol tipo = FábricaTipoÁrbol.obtenerTipo(especie, obtenerTextura(especie));
        Árbol árbol = new Árbol(tipo, x, y, z, altura);
        árboles.add(árbol);
    }
    
    public void dibujarBosque() {
        for (Árbol árbol : árboles) {
            árbol.dibujar();
        }
    }
    
    private byte[] obtenerTextura(String especie) {
        // Simulación: cargar textura real desde disco
        return new byte[1024 * 10]; // 10 KB por tipo (compartido)
    }
}

// ✅ Uso eficiente
Bosque bosque = new Bosque();
for (int i = 0; i < 1_000_000; i++) {
    // Solo la referencia al tipo compartido (8 bytes + posiciones)
    bosque.plantarÁrbol("Pino", Math.random() * 1000, Math.random() * 1000, 0, 20);
}
bosque.dibujarBosque();
```

---

## Diagrama UML

```
     ┌─────────────────────────┐
     │   FábricaTipoÁrbol      │
     ├─────────────────────────┤
     │- tipos: Map             │
     │+ obtenerTipo()          │
     └────────────┬────────────┘
                  │
                  ▼
        ┌────────────────────┐
        │   TipoÁrbol        │
        │  (Flyweight)       │
        ├────────────────────┤
        │- especie: String   │  <- Compartido
        │- textura: byte[]   │  <- Compartido
        └────────────────────┘
                  ▲
                  │ usa
                  │
        ┌─────────┴──────────┐
        │      Árbol         │
        ├────────────────────┤
        │- tipo: TipoÁrbol   │
        │- x, y, z: double  │  <- Extrínseco
        │- altura: double    │  <- Extrínseco
        └────────────────────┘
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Economía de memoria**: Reducción dramática de memoria
- **Performance**: Menos asignaciones y garbage collection
- **Escalabilidad**: Soporta millones de objetos
- **Simplicidad**: Cliente no ve complejidad

### ❌ Desventajas

- **Complejidad**: Separar estado es difícil
- **CPU vs. Memoria**: CPU extra en lookup de factory
- **Thread safety**: Necesita sincronización en factory
- **Debugging**: Difícil rastrear estado compartido

---

## Casos de Uso

✅ **Ideal para:**
- Juegos (partículas, sprites)
- Editores de texto (caracteres)
- Navegadores web (bloques de caché)
- Sistemas de bases de datos (conexiones compartidas)

❌ **Evita cuando:**
- Pocos objetos (overhead no compensa)
- Estado muta frecuentemente
- Sincronización es prohibitiva


