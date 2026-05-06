---
title: Estilo y formato
subtitle: Voz, terminología, MyST y convenciones de escritura del sitio.
---

# Estilo y formato

Esta guía reúne las convenciones editoriales que afectan la forma visible del apunte: tono, lenguaje, sintaxis MyST, referencias, ejemplos y decisiones de formato.

## Voz y tono

El sitio debería sostener estas reglas:

- español rioplatense con **voseo**,
- tono académico pero cercano,
- rigor universitario sin simplificar en exceso,
- sin emojis salvo pedido explícito,
- explicación docente antes que definición enciclopédica.

## Pedagogía base

La estrategia de la cátedra es **late objects**:

1. aprovechar el conocimiento previo de C,
2. entrar por lo procedural cuando ayuda,
3. introducir OOP cuando el terreno conceptual ya está armado.

Eso implica dos decisiones editoriales frecuentes:

- comparar con C cuando la comparación aclara,
- no adelantar complejidad OOP si el capítulo todavía no la necesita.

## Terminología

Estas convenciones conviene sostenerlas de forma estable:

- usar **lazos** para loops,
- distinguir con claridad entre lenguaje, biblioteca, herramienta y framework,
- evitar alternar sin necesidad entre muchos nombres para el mismo concepto,
- preferir nombres de secciones que digan la función pedagógica del bloque.

## Encabezados y jerarquía visual

### Regla de base

La jerarquía de encabezados debe ser consistente:

- `#` para la página,
- `##` para bloques principales,
- `###` para subtemas.

No conviene saltar de `#` a `###` sin necesidad.

### Títulos de secciones

Los títulos deberían cumplir una de estas funciones:

- presentar un concepto,
- marcar una decisión,
- cerrar una etapa,
- introducir una actividad.

Ejemplos buenos:

- `## Resumen`
- `## Ejercicios`
- `## Próximo paso`
- `### Cuando aplica`
- `### Error común`

## Convenciones de MyST

### Admonitions

Usar directivas con función clara:

- `note` para contexto o aclaración,
- `important` para una restricción fuerte,
- `warning` para errores frecuentes o malos usos,
- `tip` para estrategia, heurística o lectura recomendada.

### Bloques de código

Todo bloque de código debería indicar lenguaje cuando corresponda:

````myst
```java
...
```
````

Esto evita parsing ambiguo y mejora legibilidad.

### Ejercicios y soluciones

La forma preferida es:

````myst
```{exercise}
:label: ex-identificador

Consigna
```

:::{solution} ex-identificador
:class: dropdown

Resolución
```java
...//codigo de solucion si corresponde
```
:::
````

### Figuras y SVG

Cuando una página necesita figura:

- usar `figure`,
- ubicar SVG en el subdirectorio correspondiente al número de apunte,
- mantener consistencia con `resources/svg.css`,
- evitar diagramas aislados sin texto que los introduzca o cierre.

## Referencias y enlaces

### Referencias internas

Si una sección se va a citar más de una vez, conviene darle etiqueta explícita.

Usar `{ref}` cuando la relación sea conceptual y estable. Usar enlace relativo cuando la navegación sea simplemente de página a página.

### Enlaces de recorrido

Los enlaces de `Próximo paso` deberían ser concretos y locales al recorrido, no genéricos.

## Casos de parsing que ya dieron problemas

### Anotaciones o tags con `@`

En prosa, las anotaciones Java y tags de Javadoc pueden chocar con el parser de MyST. Para evitarlo:

- escribirlas como código inline,
- o escaparlas cuando haga falta.

Ejemplos seguros:

- ``\@Test``
- ``\@Override``
- ``\@param``

### Bloques de ejemplo

Si un ejemplo contiene Java con anotaciones, Javadoc o firmas complejas, conviene tiparlo explícitamente como `java`.

## Estilo de ejemplos

Los ejemplos deberían:

1. responder a una decisión conceptual del capítulo,
2. tener nombres comprensibles,
3. no introducir ruido innecesario,
4. poder reutilizarse en ejercicios, comparación o resumen.

## Qué evitar

1. títulos excesivamente vagos,
2. alternar tono formal e informal sin criterio,
3. meter contenido planificado como si ya estuviera consolidado,
4. usar bloques de código sin lenguaje cuando eso afecta el parsing,
5. dejar referencias, imágenes o etiquetas “para después”.

## Próximo paso

Una vez definido el estilo de la página, conviene revisar [estado y mantenimiento](estado_y_mantenimiento.md) para decidir si ese material ya está listo para publicarse.
