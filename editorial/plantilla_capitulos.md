---
title: Plantilla mínima de capítulos
subtitle: Estructura obligatoria para mantener consistencia entre partes.
---

# Plantilla mínima de capítulos

La plantilla mínima no define el contenido conceptual de un capítulo. Define su **contrato editorial**.

Además, fija una exigencia de base: **todo lo que se incluye en la página debe quedar explicado**.

No alcanza con nombrar un concepto, pegar un bloque de código, mostrar una tabla o insertar un diagrama si el texto no aclara qué se tiene que mirar, por qué importa y cómo se conecta con el punto que se está enseñando.

La regla es simple: un capítulo no debería quedar publicado si obliga al lector a adivinar:

- qué va a aprender,
- qué necesita saber antes,
- dónde empieza el desarrollo,
- cómo se cierra,
- qué actividad lo consolida,
- y cómo sigue el recorrido.

## Tipos de página y estructura esperada

| Tipo de página | Estructura mínima |
| :--- | :--- |
| Capítulo regular | objetivo, prerrequisitos, desarrollo, resumen, ejercicios, próximo paso |
| Índice de parte | propósito de la parte, orden sugerido, capítulos nucleares, repaso/ampliación, índice exhaustivo |
| Índice de familia | navegación temática, criterios de uso, comparación o decisión rápida, cierre integrador |
| Ejercicios integradores | breve introducción, consignas, relación con la familia o parte |

## Capítulo regular: estructura obligatoria

### 1. Apertura

Todo capítulo regular debería abrir con:

1. front matter mínimo (`title` y `description` cuando corresponda),
2. etiqueta MyST si ese capítulo será referenciado,
3. encabezado principal `#`,
4. uno o dos párrafos de contexto.

### 2. Capa de orientación

El lector necesita una capa breve que explicite:

- **objetivo**,
- **prerrequisitos**,
- **cómo recorrer el desarrollo**.

Eso puede aparecer de dos formas válidas:

1. un bloque explícito de **Objetivos de Aprendizaje** más una **Hoja de ruta del capítulo**, o
2. una **Hoja de ruta del capítulo** que incluya objetivo, prerrequisitos y desarrollo.

:::{note}
No hace falta forzar una sola forma visual si el capítulo ya tiene una variante estable. Lo obligatorio es la **función editorial**, no una única presentación.
:::

### 3. Desarrollo principal

El desarrollo debe aparecer en secciones y subsecciones legibles, con progresión clara.

No es obligatorio que exista un encabezado literal `## Desarrollo`, pero sí que el capítulo tenga un cuerpo identificable y ordenado.

Ese cuerpo también tiene que **explicar** lo que introduce:

- si aparece un concepto nuevo, hay que desarrollarlo;
- si aparece un ejemplo, hay que decir qué muestra;
- si aparece una tabla o figura, hay que interpretar qué debería leer el estudiante;
- si aparece código, hay que vincularlo con la decisión conceptual del capítulo.

### 4. Resumen

Todo capítulo debe cerrar con un `## Resumen` o equivalente claramente rotulado como resumen.

Su función no es repetir todo, sino cerrar el contrato pedagógico:

- qué ideas quedan,
- qué distinciones importan,
- qué errores conviene no arrastrar.

### 5. Ejercicios

Todo capítulo debe incluir `## Ejercicios`.

Aceptan dos escalas válidas:

- ejercicios desarrollados,
- un mini ejercicio de cierre, si el capítulo es breve o muy focalizado.

### 6. Próximo paso

Todo capítulo debe cerrar con `## Próximo paso`.

La finalidad es explícita: conectar el capítulo con el recorrido inmediato y evitar páginas que “terminan en seco”.

## Plantilla base copiable

````myst
---
title: "Título del capítulo"
description: Breve descripción del tema.
---

(identificador-del-capitulo)=
# Título del capítulo

Párrafo de apertura con contexto, alcance y sentido del tema dentro de la parte.

:::{tip} Objetivos de Aprendizaje

Al finalizar este capítulo, se espera que el estudiante pueda:

1. ...
2. ...
3. ...
:::

:::{note} Hoja de ruta del capítulo

**Prerrequisitos.** ...

**Desarrollo.** ...
:::

## Primer bloque del desarrollo

...

## Segundo bloque del desarrollo

...

## Resumen

...

## Ejercicios

```{exercise}
:label: ex-identificador

Consigna.
```

## Próximo paso

Para seguir, conviene pasar a ...
````

## Variantes aceptadas

### Capítulo breve

Si el capítulo es corto, el objetivo puede quedar dentro de la hoja de ruta y el cierre puede usar un único ejercicio breve.

### Capítulo extenso

Si el capítulo es largo, puede incluir:

- objetivos explícitos,
- hoja de ruta,
- ejercicios principales,
- ejercicios avanzados,
- lecturas recomendadas.

Eso no reemplaza la plantilla mínima; la expande, sin embargo, si es _demasiado_ extenso, ver como dividirlo.

## Cosas que no deberían faltar

| Elemento | Estado esperado |
| :--- | :--- |
| Apertura contextual | Obligatorio |
| Objetivo | Obligatorio |
| Prerrequisitos | Obligatorio |
| Desarrollo | Obligatorio |
| Explicación de conceptos, ejemplos y recursos | Obligatorio |
| Resumen | Obligatorio |
| Ejercicios | Obligatorio |
| Próximo paso | Obligatorio |

## Errores editoriales frecuentes

1. capítulo que desarrolla bien pero no dice para qué sirve,
2. capítulo con ejercicios pero sin cierre conceptual,
3. capítulo correcto pero sin conexión con el siguiente,
4. capítulo breve agregado “al pasar” y sin plantilla,
5. índice o TOC que no se actualiza junto con la nueva página,
6. página que enumera, muestra o incrusta recursos sin explicarlos.

## Próximo paso

Después de definir la estructura de la página, conviene revisar [estilo y formato](estilo_y_formato.md).
