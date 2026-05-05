---
title: Análisis - Encapsulamiento en Herencia y Polimorfismo
description: Hallazgos y recomendaciones para integrar filosofía de encapsulamiento estricto en capítulos de OOP
---

# Análisis: Encapsulamiento Estricto en Herencia y Polimorfismo

## Resumen Ejecutivo

La cátedra **TIENE una política clara** de prohibir getters/setters, reemplazándolos por métodos de dominio. Sin embargo, los capítulos de herencia (`parte_2/04` y `parte_2/05`) **NO integran adecuadamente esta filosofía** con la sintaxis de herencia/polimorfismo, creando una **brecha conceptual peligrosa** para los estudiantes.

**Riesgo:** Estudiantes pueden interpretar que `protected` ≡ "válido usar getters en subclases", violando las reglas 0x200C y 0x2011.

---

## 1. Documentación Existente (Correcta y Clara)

### A. Prohibición Explícita
**Ubicación:** `reglas/apuntes_2025.md`, línea 234

```
### Los métodos de tipo `getter`/`setter` no están permitidos
```

- **Explícito:** Sin excepciones listadas
- **Directo:** Aplica a todo el código de la cátedra

### B. Regla 0x200C
**Ubicación:** `reglas/2_oop.md`, línea 1319

**Título:** "No usar métodos getter/setter si violan encapsulamiento"

**Contenido:**
- Crear getters/setters automáticamente viola encapsulamiento
- Métodos específicos del dominio son mejores alternativa
- Referencias cruzadas: 0x2001, 0x2011

**Implicación:** En lugar de `getNombre()/setNombre()`, usar `tieneNombre()`, `cambiarNombre()`

### C. Regla 0x2011
**Ubicación:** `reglas/2_oop.md`, línea 1439

**Título:** "No exponer detalles internos mediante getters (TP9 - Agenda)"

**Ejemplo Incorrecto:**
```java
// En clase Agenda
for (Contacto c : contactos) {
    if (c.getNombre().equals(nombre)) {  // ❌ Rompe encapsulamiento
        return c;
    }
}
```

**Ejemplo Correcto:**
```java
// En clase Contacto
public boolean tieneNombre(String nombre) {
    return this.nombre.equals(nombre);
}

// En clase Agenda
for (Contacto c : contactos) {
    if (c.tieneNombre(nombre)) {  // ✅ Respeta encapsulamiento
        return c;
    }
}
```

### D. Concepto de Encapsulamiento
**Ubicación:** `parte_2/02_oop_relaciones.md`, línea 35

- Define encapsulamiento: fusión datos+comportamiento + ocultamiento de detalles
- Analogía: control remoto de televisor
- Beneficios: control de invariantes, flexibilidad, cambios seguros

---

## 2. Gap 1: parte_2/04_oop_herencia_polimorfismo.md

### Descripción
- **Líneas:** 941
- **Título:** "4: Herencia y Polimorfismo Conceptual"
- **Enfoque:** Conceptos (no sintaxis Java específica)
- **Rol en currículo:** Bridge conceptual antes de parte_2/05 (sintaxis)

### Problemas Identificados

| Problema | Detalles | Severidad |
|----------|----------|-----------|
| No menciona encapsulamiento en jerarquías | El capítulo trata herencia sin conectar con encapsulamiento | ALTA |
| No aborda métodos de dominio vs getters | Ni una mención a cómo manejar acceso en subclases | ALTA |
| No referencia reglas 0x200C/0x2011 | Omisión crítica de la política de cátedra | ALTA |
| No cubre `protected` conceptualmente | ¿Qué significa que una subclase acceda a protected? | MEDIA |

### Lo que Falta

**Nueva Sección necesaria:** "Encapsulamiento en Jerarquías: Mantén tus Secretos"

Ubicación: Después de "Herencia: Especialización y Generalización"

Contenido requerido:
1. Herencia NO significa "exponer detalles internos"
2. Las subclases responden a través de métodos públicos de dominio
3. `protected` es para subclases, no para exposición a todo el mundo
4. Ejemplo: Cómo Gerente extends Empleado sin romper encapsulamiento
5. Referencias a 0x200C, 0x2011, encapsulamiento-concepto

---

## 3. Gap 2: parte_2/05_herencia_polimorfismo.md

### Descripción
- **Líneas:** 1457
- **Título:** "5: Herencia y Polimorfismo"
- **Enfoque:** Sintaxis Java concreta
- **Rol en currículo:** Implementación práctica de conceptos

### Problemas Identificados

#### 3.1 Protected SIN contexto de encapsulamiento
**Problema:** Muestra ejemplos de `protected` sin advertir sobre ruptura de encapsulamiento

Ejemplo en el archivo:
```java
public class Empleado {
    private String nombre;
    protected double salarioBase;  // ⚠️ Sin contexto
    
    public double calcularSalario() {
        return salarioBase;
    }
}
```

**Riesgo:** Estudiantes pueden pensar: "Protected = OK acceder directamente en subclases"

#### 3.2 Línea 494 es débil y ambigua
**Texto actual:**
```
"Como alternativa a getters cuando la herencia es el caso de uso principal"
```

**Problemas:**
- Sugiere que getters SON alternativa válida
- "Cuando herencia es caso" = ¿Siempre? ¿A veces?
- **Conflictúa** directamente con 0x200C y 0x2011

#### 3.3 NO hay sección de testing
**Línea 1136** menciona `public void test(Rectangulo r)` pero:
- Sin explicación
- Sin sección "Cómo Testear Herencia"
- Sin ejemplos de testing SIN getters

#### 3.4 NO vincula con reglas de cátedra
Cuando muestra ejemplo Gerente/Empleado:
- No menciona que Gerente NO debería tener `getBonificacion()`
- No referencia Regla 0x200C
- No muestra métodos de dominio como alternativa

### Lo que Falta

**A) Advertencia sobre `protected`**
- Ubicación: Sección de modificadores de acceso
- Contenido: Por qué `protected` en atributos puede romper encapsulamiento

**B) Nueva Sección: "Testing de Herencia sin Getters"**
- Ubicación: Después de ejemplos de override y polimorfismo
- Contenido:
  - Cómo verificar comportamiento sin getters
  - Testing de invariantes a través de métodos públicos
  - Ejemplo completo: Gerente extends Empleado
  - Referencias a parte_2/11_oop_testing.md

**C) Ejemplo Gerente/Empleado "Correcto"**
- Versión actual: NO enfatiza encapsulamiento
- Versión requerida: Con métodos de dominio, sin getters
- Tests correspondientes

**D) Reemplazar línea 494**
De:
```
"Como alternativa a getters cuando la herencia es el caso de uso principal"
```

A:
```
"Las subclases interactúan con la superclase mediante métodos de dominio,
no getters/setters. Ver {ref}`regla-0x200C` para más detalles."
```

---

## 4. Gap 3: parte_2/11_oop_testing.md

### Descripción
- **Líneas:** ~2200
- **Título:** "11: Testing en Programación Orientada a Objetos"
- **Enfoque:** Estrategias de testing para OOP

### Verificación Necesaria

**Preguntas clave:**
- ¿Cubre testing CON restricción de no getters?
- ¿Muestra ejemplos de testing comportamental?
- ¿Explica cómo verificar invariantes sin getter access?

**Sospecha de gaps:**
- Probablemente muestra ejemplos con getters (patrón común en testing)
- Probablemente no enfatiza "testing without accessor methods"
- Probablemente no muestra jerarquía Gerente/Empleado

**Acción:** Verificar y actualizar según hallazgos

---

## 5. Riesgo: Interpretación Incorrecta por Estudiantes

### Escenario
Estudiante lee:
1. parte_2/04: Conceptos de herencia ✓
2. parte_2/05: Sintaxis de herencia ✓
3. Reglas: Pero sin leerlas con cuidado ❌

### Conclusión Incorrecta Probable
> "Para hacer `Gerente extends Empleado`, uso `protected` para `salarioBase`
> y luego `getters/setters`. Es cómodo y es herencia después de todo."

### Resultado
- ❌ Viola Regla 0x200C
- ❌ Viola Regla 0x2011
- ❌ Rompe encapsulamiento
- ❌ Falla en evaluación

---

## 6. Plan de Corrección

### Prioridad 1 (Crítico): parte_2/04_oop_herencia_polimorfismo.md

**Tarea:** Agregar sección sobre encapsulamiento en jerarquías

Ubicación: Después de "Herencia: Especialización y Generalización"

Contenido:
- Herencia ≠ exposición de detalles
- Métodos de dominio preservan encapsulamiento
- Ejemplo: Gerente extends Empleado (forma correcta)
- Referencias a 0x200C, 0x2011

**Estimado:** 300-400 palabras + ejemplos

---

### Prioridad 2 (Crítico): parte_2/05_herencia_polimorfismo.md

**Tareas:**
1. Advertencia sobre `protected` (100 palabras)
2. Reemplazar línea 494 (50 palabras)
3. Nueva sección "Testing de Herencia sin Getters" (600+ palabras)
4. Ejemplo mejorado Gerente/Empleado (200 palabras)

**Estimado:** 950+ palabras + ejemplos + tests

---

### Prioridad 3 (Medio): parte_2/11_oop_testing.md

**Tareas:**
1. Verificar cobertura de testing sin getters
2. Agregar ejemplos si faltan
3. Sección sobre testing de invariantes
4. Referencias cruzadas con parte_2/05

**Estimado:** 400+ palabras según hallazgos

---

## 7. Referencias Cruzadas

| Archivo | Línea | Concepto |
|---------|-------|----------|
| parte_2/02_oop_relaciones.md | 35 | (encapsulamiento-concepto) |
| reglas/2_oop.md | 1319 | (regla-0x200C) |
| reglas/2_oop.md | 1439 | (regla-0x2011) |
| reglas/apuntes_2025.md | 234 | Prohibición de getters/setters |
| parte_2/11_oop_testing.md | - | Estrategias de testing OOP |

---

## 8. Conclusión

**La cátedra TIENE reglas claras. Los apuntes NO las conectan bien.**

El resultado es una brecha peligrosa donde estudiantes pueden escribir código que:
- Parece "natural" (usa syntaxis válida de Java)
- Es "cómodo" (getters/setters automáticos)
- **Viola completamente** la política de encapsulamiento estricto

**Solución:** Conectar explícitamente 04/05 con:
- Concepto de encapsulamiento (parte_2/02)
- Reglas de dominio (reglas/2_oop.md)
- Estrategias de testing (parte_2/11)

---

*Documento generado: 2026-04-24*
*Estado: Hallazgos documentados, listo para implementación*
