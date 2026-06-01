---
title: "Parte 6: Tipos de Datos Abstractos"
description: "Especificación formal y algebraica de estructuras de datos."
---

(parte6-indice)=
# Parte 6: Tipos de Datos Abstractos

Esta parte introduce el enfoque algebraico para especificar y razonar sobre tipos de datos abstractos. A diferencia de las implementaciones concretas (pilas con arreglos, colas con listas enlazadas), un TDA formaliza *qué promete* una estructura de datos, independientemente de *cómo se implementa*.

## Propósito de la parte

Aprender a escribir especificaciones rigurosas que capturen el comportamiento esencial de estructuras de datos mediante sorts, operaciones y axiomas. Esto permite:

- Razonar correctamente sobre el comportamiento de una estructura.
- Validar que diferentes implementaciones cumplen el mismo contrato.
- Construir código modular y reemplazable.

## Orden sugerido de lectura

1. **Introducción a Tipos de Datos Abstractos** (`1_introtda.md`) — Comienza aquí para entender la diferencia entre especificación e implementación, y aprende la notación algebraica (sorts, operaciones, axiomas).

2. El contenido futuro de esta parte expandirá con TDAs específicos (pilas, colas, conjuntos, diccionarios) escritos en notación algebraica, y la traducción de esas especificaciones a código Java.

## Índice completo

- `1_introtda.md` — Introducción a Tipos de Datos Abstractos (sorts, operaciones, axiomas, demostraciones algebraicas)

## Próximo paso

Después de dominar los conceptos de esta parte, podrás escribir especificaciones formales para cualquier estructura de datos y verificar que tus implementaciones las cumplen correctamente.
