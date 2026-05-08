---
title: Borradores y Observaciones 2025
description: Registro de observaciones de entregas y reglas en discusión.
---

# Banco de Observaciones y Borradores (2025)

:::{warning} Documento en borrador
Este documento **NO** es parte del reglamento activo. Funciona como un cuaderno de trabajo de la cátedra donde se registran observaciones frecuentes de los TPs (especialmente TP3, TP7 y TP9) y se debaten posibles nuevas reglas antes de formalizarlas.
:::

## Observaciones de Trabajos Prácticos

### Sobre el TP3 (Funciones y Archivos)
- **Separar responsabilidades:** Lógicas de verificación repetidas (ej. validar rangos) deben extraerse a funciones estáticas privadas (`public static void verificar(...)`).
- **Sobrecargas:** Usá la sobrecarga para simplificar las llamadas, proveyendo límites por defecto (ej. `Integer.MIN_VALUE`).
- **Excepciones específicas:** Si un arreglo viene `null`, no hay nada que procesar; eso amerita lanzar una excepción específica, no dejar que falle internamente con NullPointerException genéricos.

### Sobre el TP7 (Calculadora)
- **El método `toString`:** NO debe realizar cálculos. Su única responsabilidad es mostrar la "estructura" de la operación (ej: `5 + 3 = 8`). Concatenar internamente llamando a `calcular()` puede ocultar el costo real de una operación pesada.
- **Herencia:** Subí la lógica común de `toString()` a la clase base `Operacion` e implementá un método protegido `obtenerSimbolo()` en las subclases.
- **Principio OCP:** Usar `operador.equals("-")` dentro de la clase base es equivalente a un `instanceof` encubierto y viola el principio Abierto/Cerrado. La clase base no debe conocer los tipos específicos de operadores.

### Sobre el TP9 (Agenda)
- **Romper Encapsulamiento en Búsquedas:** Extraer el nombre de un contacto con `getNombre()` en un bucle dentro de la Agenda rompe el encapsulamiento. El contacto debe saber responder si coincide con un nombre: `contacto.tieneNombre(nombre)`.
- **Uso de `hashCode()` para igualdades:** Nunca compares objetos usando sus valores de `hashCode`. El hash no garantiza unicidad (pueden haber colisiones). Usá siempre `equals`.

## Reglas en Discusión (Telegráficas)
*Estas reglas están bajo análisis para ser integradas a las series 0x0 a 0x5.*

1. **Evitar retornos null:** Usar `Optional` o devolver colecciones vacías (ej: `Collections.emptyList()`).
2. **Import explícito:** No usar `import paquete.*`. Importar únicamente las clases necesarias.
3. **No apilar llamadas:** Una línea no debe encadenar más de 2 o 3 métodos (ej: `a.getB().getC().hacer()`). Fomenta la violación de la Ley de Demeter.
4. **Mensajes de Assert:** Si un `assertThrows` o `assertEquals` falla, incluir siempre el parámetro opcional del mensaje para facilitar la lectura en la consola del CI.