---
title: Cuestiones de Estilo
description: Reglas de estilo y correcciones generales para código Java en la cátedra de Programación II.
---

# Cuestiones de estilo y correcciones generales

# Categorización de Reglas (Numeración Hexadecimal)

- **`0x0xxx`**: Nomenclatura y formato general
- **`0x1xxx`**: Documentación y comentarios
- **`0x2xxx`**: Diseño de clases y POO
- **`0x3xxx`**: Manejo de excepciones
- **`0x4xxx`**: Testing
- **`0x5xxx`**: Estructuras de control
- **`0x6xxx`**: Restricciones de programación funcional
- **`0xExxx`**: Errores comunes (Error-Prone)
- **`0xFxxx`**: Excepciones avanzadas

## Resumen de Prioridades

### 🔴 Críticas (deben cumplirse siempre)
- 0x0001-0x0004: Nomenclatura básica (clases, variables, métodos)
- 0x2001: Atributos privados (encapsulamiento)
- 0x2004, 0x2005: Contrato equals + hashCode
- 0x1000: Documentación Javadoc
- 0x5000: Un solo return por método
- 0x6000-0x6002: Sin lambda, method references, ni streams

### 🟠 Importantes (muy recomendadas)
- 0x3005: Atajar excepciones específicas
- 0x3006-0x3007: Excepciones diferentes para situaciones diferentes
- 0x2007: Sin código duplicado (DRY)
- 0x5001: Sin operadores compuestos
- 0x5002: Sin break/continue
- 0x6003-0x6005: Sin interfaces funcionales ni métodos funcionales
- 0xE000: StringBuilder en bucles

### 🟡 Recomendadas (buenas prácticas)
- 0x0008: Espacios en operadores
- 0x0009: No apilar líneas
- 0x000A: Imports específicos
- 0x200A: Evitar retornos null (usar excepciones o null documentado)
- 0x0007: No indicar tipo en nombres
- 0x6004: Evitar Optional como retorno

