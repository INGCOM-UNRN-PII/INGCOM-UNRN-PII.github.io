---
title: "Ejercicios integradores creacionales"
subtitle: "Práctica para decidir estrategias de creación"
subject: Patrones de Diseño Creacionales
---

# Ejercicios integradores de patrones creacionales

Estos ejercicios obligan a comparar patrones de la misma familia. La meta no es elegir por memoria, sino por restricciones del problema.

```{exercise}
:label: ex-parte4-creacionales-integrador-1

Una plataforma de comercio electrónico vende en varios países. Para cada mercado necesita crear **pantallas**, **formatos de moneda** y **servicios de envío** coherentes entre sí. Además, algunas pantallas tienen decenas de opciones opcionales de configuración visual.

Indicá:

1. Qué patrón usarías para garantizar coherencia entre componentes del mismo mercado
2. Dónde aparece un caso razonable para **Builder**
3. Qué parte del diseño no resolvería bien **Factory Method** por sí solo
```

```{exercise}
:label: ex-parte4-creacionales-integrador-2

Un sistema de simulación carga desde disco una configuración costosa de robots base. Después necesita crear cientos de variantes pequeñas para pruebas, cambiando solo sensores y velocidad máxima.

Compará **Prototype**, **Builder** y **Singleton**. Indicá cuál usarías como mecanismo principal de creación, cuál descartarías y por qué.
```
