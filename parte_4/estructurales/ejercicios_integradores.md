---
title: "Ejercicios integradores estructurales"
subtitle: "Práctica para justificar composición y desacoplamiento"
subject: Patrones de Diseño Estructurales
---

# Ejercicios integradores de patrones estructurales

Estos ejercicios mezclan problemas donde más de un patrón parece razonable. La resolución debe justificar qué acoplamiento se reduce y qué complejidad nueva se acepta.

```{exercise}
:label: ex-parte4-estructurales-integrador-1

Una universidad quiere publicar un portal que unifique trámites de inscripción, pagos y certificados. Cada subsistema ya existe, usa APIs distintas y algunos servicios son lentos de cargar. Además, el equipo quiere registrar auditoría y métricas sin modificar el código de negocio.

Indicá qué combinación de **Facade**, **Adapter**, **Proxy** y **Decorator** usarías, y para qué rol concreto en la arquitectura.
```

```{exercise}
:label: ex-parte4-estructurales-integrador-2

Un editor gráfico maneja capas, grupos de capas y figuras simples. También debe exportar el dibujo en SVG y en PNG, y el equipo quiere evitar duplicar jerarquías enteras por cada formato.

Compará **Composite** y **Bridge**. Indicá dónde usarías cada uno y qué problema aparece si intentás resolver todo con un único patrón.
```
