---
title: Estado y mantenimiento
subtitle: Política editorial para publicar, revisar y mantener el sitio.
---

# Estado y mantenimiento

El sitio necesita distinguir con claridad entre lo que ya forma parte del apunte y lo que todavía es planificación, borrador o placeholder.

## Estados editoriales

| Estado | Qué significa | Cómo debería verse |
| :--- | :--- | :--- |
| Publicado | Material vigente del curso | En `myst.yml`, con índice y navegación coherente |
| En revisión | Material real pero todavía no consolidado | Fuera del TOC principal o marcado explícitamente |
| Planificado | Idea o roadmap futuro | Documentado como plan, no como parte ya publicada |
| Placeholder | Archivo vacío o casi vacío | No debería figurar como contenido vigente |

## Regla principal

El TOC publicado y los archivos reales del repositorio deberían contar la misma historia.

Eso implica:

1. no publicar partes incompletas como si ya estuvieran consolidadas,
2. no dejar índices describiendo recorridos que el TOC ya no publica,
3. no sostener placeholders vacíos dentro de un bloque que parece activo.

## Cuándo un capítulo está listo

Un capítulo se considera listo para publicación cuando cumple estas condiciones:

| Criterio | Debe cumplirse |
| :--- | :--- |
| Plantilla mínima | Sí |
| Estilo y tono del sitio | Sí |
| Índice de su parte actualizado | Sí |
| `myst.yml` alineado si corresponde | Sí |
| Navegación de cierre (`Próximo paso`) | Sí |
| Sin placeholders ni enlaces rotos asociados | Sí |

## Flujo recomendado para agregar un capítulo

1. crear el archivo en la parte correcta,
2. aplicar la plantilla mínima,
3. decidir si es capítulo, índice o ejercicio integrador,
4. actualizar el índice de la parte,
5. actualizar `myst.yml` si el contenido ya es publicado,
6. revisar referencias, etiquetas y navegación,
7. reconstruir el sitio.

## Flujo recomendado para reestructurar una parte

1. decidir el alcance real de la parte,
2. corregir primero `myst.yml`,
3. alinear después el índice de la parte,
4. revisar archivos que quedaron fuera, duplicados o históricos,
5. eliminar o marcar placeholders.

## Placeholders y material planificado

### Qué no conviene hacer

- dejar archivos vacíos dentro de una parte que parece activa,
- mantener índices ambiciosos sin contenido real,
- usar el mismo tono para un roadmap y para un apunte consolidado.

### Qué conviene hacer

- sacar del TOC principal lo que todavía no es publicable,
- marcar explícitamente si una sección está en desarrollo,
- concentrar la planificación en un lugar visible pero diferenciado.

## Checklist editorial previo a publicar

| Pregunta | Sí / No |
| :--- | :--- |
| ¿La página tiene estructura mínima? | |
| ¿El índice de su parte la incluye correctamente? | |
| ¿`myst.yml` refleja el estado real del contenido? | |
| ¿El tono coincide con el resto del sitio? | |
| ¿Las referencias internas y externas están alineadas? | |
| ¿El estado del contenido es explícito? | |

## Qué hacer cuando aparece una desalineación

Si un índice, el TOC y los archivos reales no coinciden, el orden de corrección recomendado es:

1. definir primero cuál es la estructura correcta,
2. corregir el TOC publicado,
3. corregir el índice de la parte,
4. decidir qué hacer con el material sobrante.

## Mantenimiento futuro

La sección editorial no sirve si solo se crea una vez. Debería revisarse cada vez que:

- se agrega una nueva parte,
- se reordena una secuencia de capítulos,
- se mueve contenido entre partes,
- o se cambia el criterio de publicación.

## Aportes de terceros

Los aportes externos conviene recibirlos por canales trazables y revisarlos con el mismo criterio que cualquier cambio interno.

Los canales admitidos y el flujo recomendado están documentados en [contribuciones_externas](contribuciones_externas.md).

## Sincronización con archivos de instrucciones

Los archivos auxiliares de instrucciones del repositorio no deberían duplicar reglas editoriales extensas.

En particular:

- `.github/copilot-instructions.md`
- `GEMINI.md`

deberían **apuntar a `editorial/` como fuente de verdad** para:

- estructura mínima de páginas,
- estilo y formato,
- convenciones MyST,
- criterios de publicación y mantenimiento.

Si cambia una regla editorial de fondo, conviene actualizar:

1. la documentación en `editorial/`,
2. y solo después los archivos de instrucciones que la referencian.

## Próximo paso

Si hay que escribir una página nueva, conviene volver a la [sección editorial](indice.md) y elegir el tipo de página correspondiente antes de empezar.
