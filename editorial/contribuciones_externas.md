---
title: Contribuciones externas
subtitle: Guía para recibir, revisar e integrar aportes de terceros.
---

# Contribuciones externas

El apunte admite aportes de terceros, pero no como edición directa sin curaduría.

Toda contribución debería entrar por un canal explícito, quedar trazable y revisarse antes de incorporarse al material publicado.

## Canales admitidos

| Canal | Cuándo conviene usarlo | Qué debería incluir |
| :--- | :--- | :--- |
| [Issue tracker del repositorio](https://github.com/INGCOM-UNRN-PII/INGCOM-UNRN-PII.github.io/issues) | Errores, referencias rotas, sugerencias editoriales, capítulos confusos, pedidos de mejora | archivo afectado, problema observado, propuesta concreta y contexto |
| [Pull request del repositorio](https://github.com/INGCOM-UNRN-PII/INGCOM-UNRN-PII.github.io/pulls) | Cambios ya implementados sobre archivos del sitio | alcance del cambio, archivos tocados, motivo y referencia al issue si existe |
| Correo institucional `mrvilugron@unrn.edu.ar` | Comentarios sensibles, observaciones no públicas, consultas institucionales o aportes que todavía no están listos para abrirse | tema, motivación, material involucrado y forma de contacto |

## Canal recomendado según el tipo de aporte

1. si el aporte describe un problema o una mejora, conviene abrir primero un issue,
2. si el aporte ya trae una solución concreta, conviene abrir un pull request,
3. si el aporte no debería discutirse en público, conviene usar email.

## Qué tipos de aporte son útiles

Se consideran aportes valiosos, entre otros:

- corrección de errores conceptuales o tipográficos,
- mejora de navegación, TOC o referencias cruzadas,
- clarificación pedagógica de una explicación,
- propuesta de ejercicios o ejemplos,
- mejora de una infografía SVG,
- detección de material desactualizado o inconsistente.

## Qué debería traer una buena contribución

Para reducir idas y vueltas, conviene que el aporte indique:

1. archivo o sección afectada,
2. problema concreto detectado,
3. por qué ese problema importa,
4. propuesta de corrección o mejora,
5. si corresponde, referencia a reglas de `reglas/` o de `editorial/`.

## Criterios de revisión

Un aporte externo no debería incorporarse solo porque está bien intencionado.

Antes de integrarlo, conviene revisar:

| Criterio | Pregunta de control |
| :--- | :--- |
| Coherencia editorial | ¿respeta la estructura y el tono del sitio? |
| Coherencia pedagógica | ¿encaja con el recorrido de la materia y el enfoque late objects? |
| Consistencia técnica | ¿no contradice capítulos o reglas ya publicadas? |
| Trazabilidad | ¿queda claro por qué se hizo el cambio? |
| Publicabilidad | ¿está listo para figurar en el contenido vigente? |

## Flujo recomendado para issues

1. describir el problema con enlace o ruta de archivo,
2. clasificar si es error, mejora, duda o propuesta,
3. acordar el alcance de la corrección,
4. resolverlo con commit o PR asociado,
5. cerrar el issue cuando el cambio ya quedó integrado.

## Flujo recomendado para pull requests

1. mantener el cambio acotado a un problema claro,
2. explicar qué se modificó y por qué,
3. enlazar el issue relacionado si existe,
4. evitar mezclar correcciones editoriales independientes en un mismo PR,
5. dejar el contenido alineado con `editorial/` y con `myst.yml` si corresponde.

## Uso del email

El email no debería reemplazar al issue tracker para correcciones rutinarias.

Conviene usarlo cuando:

- hay datos o contexto que no corresponde publicar,
- el aporte es preliminar y todavía no tiene forma de issue o PR,
- la consulta requiere coordinación institucional,
- o el remitente necesita un canal más directo antes de abrir un cambio público.

Si un aporte recibido por email termina generando trabajo editorial real, conviene trasladarlo después a un issue o a un PR para que quede trazabilidad.

## Regla editorial de integración

La existencia de un issue, un PR o un email no convierte automáticamente el contenido en parte del apunte.

El material recién pasa a ser contenido vigente cuando:

1. fue revisado,
2. quedó alineado con las reglas editoriales,
3. se integró al repositorio,
4. y forma parte de la versión publicada del sitio.

## Próximo paso

Si el aporte implica modificar páginas existentes o sumar material nuevo, conviene revisar también [estado_y_mantenimiento](estado_y_mantenimiento.md) antes de publicarlo.
