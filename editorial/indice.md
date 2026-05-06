---
title: Editorial
subtitle: Criterios de estructura, estilo y mantenimiento del apunte.
---

# Sección editorial

Esta sección documenta las reglas editoriales del apunte para que el criterio de publicación no dependa de memoria, costumbre o arrastre histórico.

Su objetivo es evitar tres problemas:

1. que cada capítulo se escriba con una estructura distinta,
2. que el tono y el formato se desalineen entre partes,
3. que aparezcan materiales publicados, planificados y vacíos mezclados sin criterio visible.

## Alcance

Estas reglas aplican a:

- capítulos de `parte_1` a `parte_4`,
- futuras partes que se publiquen en el TOC principal,
- índices, portadas y archivos de ejercicios integradores,
- documentación editorial o de mantenimiento del propio sitio.

:::{important}
Esta sección no reemplaza a las [reglas de estilo](../reglas/indice.md) para el código de estudiantes. Cumple otro rol: definir **cómo se mantiene y publica el apunte**.
:::

## Documentos de esta sección

| Documento | Qué define |
| :--- | :--- |
| [plantilla_capitulos](plantilla_capitulos.md) | Estructura mínima obligatoria por tipo de página |
| [estilo_y_formato](estilo_y_formato.md) | Voz, terminología, MyST, referencias, ejemplos y convenciones de escritura |
| [estado_y_mantenimiento](estado_y_mantenimiento.md) | Estados del contenido, criterios de publicación y checklist de mantenimiento |

## Regla corta

Antes de considerar “listo” un cambio editorial, debería cumplirse esto:

1. la página tiene la estructura mínima que le corresponde,
2. el índice de su parte y `myst.yml` cuentan la misma historia,
3. el estado del contenido es explícito,
4. el tono, formato y referencias siguen las convenciones del sitio.

## Qué se considera una página del apunte

No todas las páginas cumplen la misma función. A nivel editorial, el sitio hoy usa al menos estos tipos:

| Tipo de página | Ejemplos | Expectativa editorial |
| :--- | :--- | :--- |
| Capítulo regular | `parte_1/04_metodos.md` | Plantilla mínima completa |
| Índice o portada de parte | `parte_2/indice.md` | Mapa de aprendizaje + índice exhaustivo |
| Índice de familia o subtema | `parte_4/creacionales/indice.md` | Navegación, comparación y cierre integrador |
| Ejercicios integradores | `parte_4/*/ejercicios_integradores.md` | Consignas, no desarrollo teórico completo |
| Documentación editorial | `editorial/*.md` | Reglas de mantenimiento y publicación |

## Uso esperado

La sección editorial sirve para dos momentos:

- **antes de escribir**: elegir plantilla y alcance de la página,
- **antes de publicar**: revisar que estructura, navegación y estado estén alineados.

## Próximo paso

Si se va a crear o rehacer un capítulo, conviene seguir primero la [plantilla mínima de capítulos](plantilla_capitulos.md).
