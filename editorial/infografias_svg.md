---
title: Infografías SVG
subtitle: Reglas editoriales para diagramas técnicos e infografías del apunte.
---

# Infografías SVG

Esta guía integra a la sección editorial las convenciones para diagramas SVG del proyecto.

Su objetivo es que los SVG no se resuelvan “a ojo”, sino con criterios consistentes de:

- ubicación,
- estilo visual,
- paleta,
- tipografía,
- clases CSS,
- legibilidad didáctica.

## Cuándo corresponde usar un SVG

Conviene crear una infografía SVG cuando:

1. una estructura o proceso se entiende mejor visualmente,
2. el diagrama aporta algo que el texto solo no resuelve,
3. la figura puede reutilizarse como referencia dentro del apunte.

No conviene agregar un SVG si solo repite lo mismo que el texto sin aportar claridad.

## Ubicación y nombres

La regla general es:

- el SVG va en un subdirectorio numerado correspondiente al capítulo,
- y el nombre del archivo debe ser descriptivo.

Ejemplo:

```text
parte_1/
├── 13_tad.md
└── 13/
    ├── pila_arreglo.svg
    ├── cola_circular.svg
    └── deque.svg
```

Nombres recomendados:

- `pila_arreglo.svg`
- `cola_circular.svg`
- `lista_enlazada_simple.svg`

Nombres a evitar:

- `diagrama1.svg`
- `figura_final.svg`
- `test.svg`

## Integración con MyST

La forma esperada de insertar una figura es:

````myst
```{figure} 13/pila_arreglo.svg
:label: fig-pila-arreglo
:align: center
:width: 90%

Pila implementada con arreglo. El índice `tope` indica la posición del último elemento.
```
````

La figura debería incluir:

- `:label:` cuando vaya a ser referenciada,
- ancho razonable,
- y epígrafe útil, no decorativo.

## Regla principal de estilo

Tomar `resources/svg.css` como fuente de verdad para paleta, tipografía y clases semánticas, pero **copiar dentro de cada SVG** el CSS que ese archivo necesita.

## CSS base de referencia

El archivo base es:

- `resources/svg.css`

No requiere instalación adicional, pero **no debería enlazarse desde producción** con `xml-stylesheet`.

### Regla de incrustación

Cada SVG publicado debería ser autocontenido. La forma esperada es:

```xml
<svg width="600" height="450" viewBox="0 0 600 450" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <style><![CDATA[
      /* Copiar desde resources/svg.css las variables y clases necesarias */
      :root {
        --unrn-red: #eb2141;
        --unrn-dark-blue: #192437;
        --gray-700: #404040;
      }

      .title {
        font-family: 'Fabrikat', Arial, sans-serif;
        font-size: 16px;
        font-weight: 700;
        fill: var(--unrn-dark-blue);
        text-anchor: middle;
      }
    ]]></style>
  </defs>
</svg>
```

Si el diagrama usa pocas clases, conviene incrustar solo las necesarias. Si usa muchas, conviene copiar y ajustar un bloque más grande tomado de `resources/svg.css`.

:::{warning}
No usar `<?xml-stylesheet ...?>` para publicar SVG del sitio. La ruta al CSS externo no se resuelve de forma consistente entre la versión local y el despliegue.
:::

## Estructura base recomendada

### Opción preferida

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<svg width="600" height="450" viewBox="0 0 600 450" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <style><![CDATA[
      /* Copiar desde resources/svg.css las reglas necesarias para este SVG */
    ]]></style>
    <marker id="arrowhead" markerWidth="10" markerHeight="10" refX="9" refY="3" orient="auto">
      <polygon points="0 0, 10 3, 0 6" fill="var(--unrn-red)"/>
    </marker>
  </defs>

  <text x="300" y="25" class="title">Título del Diagrama</text>
  <text x="300" y="45" class="subtitle">Descripción breve</text>
</svg>
```

### Marcadores de flechas

Cuando el diagrama use flechas, el marcador debería definirse en `<defs>` del propio SVG.

## Paleta de colores

### Colores institucionales UNRN

| Color | Variable CSS | Hex | Uso |
| :--- | :--- | :--- | :--- |
| Rojo UNRN | `--unrn-red` | `#eb2141` | Principal, acentos |
| Azul oscuro UNRN | `--unrn-dark-blue` | `#192437` | Secundario, texto |

### Colores por tipo de estructura

| Estructura | Colores primarios | Variables CSS |
| :--- | :--- | :--- |
| Pilas | Rojos / cálidos | `--stack-primary`, `--stack-data` |
| Colas | Verdes | `--queue-primary`, `--queue-data` |
| Deques | Morados | `--deque-primary`, `--deque-data` |
| Listas | Azules | `--list-primary`, `--list-data` |
| Árboles | Verde natural | `--tree-primary`, `--tree-data` |
| Grafos | Naranjas | `--graph-primary`, `--graph-data` |
| Hash / mapas | Índigos | `--hash-primary`, `--hash-data` |

:::{tip}
Elegir el esquema de color semántico correcto ayuda a que diagramas de una misma familia se reconozcan rápido.
:::

## Tipografías

Las tipografías esperadas son:

| Fuente | Uso | Clases frecuentes |
| :--- | :--- | :--- |
| Fabrikat | Títulos y subtítulos | `.title`, `.subtitle` |
| Lato | Etiquetas y texto general | `.label`, `.label-bold`, `.label-small` |
| Share Tech Mono | Código y elementos técnicos | `.code`, `.code-comment`, `.code-keyword` |

## Clases CSS más usadas

### Texto y etiquetas

```xml
<text class="title">Título Principal</text>
<text class="subtitle">Subtítulo</text>
<text class="label">Etiqueta normal</text>
<text class="label-bold">Etiqueta en negrita</text>
<text class="label-small">Etiqueta pequeña</text>
<text class="index">Índice numérico</text>
```

### Código

```xml
<text class="code">int x = 42;</text>
<text class="code-comment">// comentario</text>
<text class="code-keyword">return</text>
```

### Nodos genéricos

```xml
<rect class="node"/>
<rect class="node-data"/>
<rect class="node-pointer"/>
<rect class="node-null"/>
<rect class="node-highlight"/>
```

### Nodos por estructura

#### Pilas

```xml
<rect class="stack-node"/>
<rect class="stack-data"/>
<rect class="stack-pointer"/>
<path class="stack-arrow" marker-end="url(#arrowhead)"/>
<text class="stack-label">TOPE</text>
```

#### Colas

```xml
<rect class="queue-node"/>
<rect class="queue-data"/>
<rect class="queue-pointer"/>
<path class="queue-arrow" marker-end="url(#arrowhead)"/>
<text class="queue-label">FRENTE</text>
```

#### Deques

```xml
<rect class="deque-node"/>
<rect class="deque-data"/>
<path class="deque-arrow" marker-end="url(#arrowhead)"/>
<path class="deque-arrow-secondary"/>
```

#### Listas

```xml
<rect class="list-node"/>
<rect class="list-data"/>
<rect class="list-pointer"/>
<path class="list-arrow" marker-end="url(#arrowhead)"/>
```

#### Árboles

```xml
<rect class="tree-node"/>
<rect class="tree-data"/>
<path class="tree-arrow" marker-end="url(#arrowhead)"/>
```

### Flechas

```xml
<path class="arrow"/>
<path class="arrow-primary"/>
<path class="arrow-secondary"/>
<path class="arrow-dashed"/>
<path class="arrow-bidirectional"/>
```

### Contenedores

```xml
<rect class="code-block"/>
<rect class="highlight-box"/>
<rect class="info-box"/>
<rect class="memory-cell"/>
```

### Estados

```xml
<rect class="valid"/>
<rect class="invalid"/>
<rect class="warning"/>
```

### Líneas

```xml
<line class="divider"/>
<line class="divider-bold"/>
```

## Reglas didácticas para infografías

Un buen SVG del apunte debería cumplir estas funciones:

1. mostrar estructura, no solo decorar,
2. tener títulos y etiquetas claras,
3. mantener jerarquía visual evidente,
4. usar ejemplos de código solo cuando ayudan a conectar representación y programa,
5. evitar superposición de elementos o ruido visual.

## Buenas prácticas

1. usar clases CSS semánticas en lugar de estilos inline,
2. incrustar el CSS necesario dentro del propio SVG,
3. respetar la jerarquía tipográfica (`title` → `subtitle` → `label`),
4. mantener dimensiones proporcionadas,
5. incluir títulos descriptivos y etiquetas legibles,
6. sostener consistencia con diagramas similares ya existentes.

## Checklist antes de dar por listo un SVG

| Pregunta | Sí / No |
| :--- | :--- |
| ¿El CSS necesario está incrustado dentro del SVG? | |
| ¿El `viewBox` está bien definido? | |
| ¿Las dimensiones son razonables? | |
| ¿El color corresponde a la familia de estructura? | |
| ¿Las etiquetas se leen sin superposición? | |
| ¿Los ejemplos de código son correctos y aportan valor? | |
| ¿La figura se inserta en MyST con `figure` y epígrafe útil? | |

## Relación con otros documentos editoriales

- Esta guía define reglas específicas para infografías.
- [estilo_y_formato](estilo_y_formato.md) sigue definiendo la convención editorial general del sitio.
- [estado_y_mantenimiento](estado_y_mantenimiento.md) define cuándo una página y su material asociado están listos para publicarse.

## Próximo paso

Si el SVG acompaña a un capítulo nuevo, conviene volver a la [plantilla mínima de capítulos](plantilla_capitulos.md) para verificar que la página completa cierre bien.
