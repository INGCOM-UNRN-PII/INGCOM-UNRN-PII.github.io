---
title: "Ejercicios integradores de comportamiento"
subtitle: "Práctica para decidir cómo distribuir responsabilidades"
subject: Patrones de Diseño de Comportamiento
---

# Ejercicios integradores de patrones de comportamiento

En esta familia conviene mirar con precisión qué cambia en el sistema: el algoritmo, el estado, la comunicación o la secuencia de procesamiento.

```{exercise}
:label: ex-parte4-comportamiento-integrador-1

Una plataforma de trámites universitarios debe:

1. Validar solicitudes en una secuencia de controles
2. Notificar a distintos subsistemas cuando cambia el estado
3. Permitir deshacer algunas acciones del operador

Compará **Chain of Responsibility**, **Observer** y **Command**. Indicá qué rol cumpliría cada patrón y qué error conceptual habría en intentar resolver todo con uno solo.
```

```{exercise}
:label: ex-parte4-comportamiento-integrador-2

Un simulador de transporte cambia su lógica según el modo del vehículo (`Detenido`, `En marcha`, `En mantenimiento`) y además permite elegir entre distintos algoritmos de cálculo de ruta. Parte del procesamiento de cada viaje, sin embargo, siempre sigue la misma secuencia general.

Compará **State**, **Strategy** y **Template Method**. Proponé una arquitectura donde cada patrón resuelva un problema distinto y complementario.
```
