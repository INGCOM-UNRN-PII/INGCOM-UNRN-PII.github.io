---
title: Guía de Markdown (GitHub Flavored)
short_title: Guía Markdown (GFM)
description:
  Guía completa de sintaxis Markdown para documentación técnica y académica
---

# Guía de Markdown

Markdown es un lenguaje de marcado ligero diseñado para ser **fácil de leer y
escribir**. Los archivos `.md` son puro texto, funcionan en cualquier editor y
son el estándar absoluto en GitHub para la documentación.

Esta guía se divide en dos partes: lo **imprescindible** que vas a usar todos los días, y los **extras avanzados** que dependen del renderizador de GitHub (GFM).

---

## Parte 1: Lo Mínimo para Empezar

Con estos seis elementos podés resolver el 90% de tus necesidades de documentación. Esta sintaxis es universal y funcionará en cualquier visualizador de Markdown.

### 1. Encabezados

Los encabezados estructuran el documento y crean jerarquía. Usá el símbolo `#`.
**Regla de oro:** Solo un `#` por archivo para el título principal.

```markdown
# Título del documento (H1)
## Sección principal (H2)
### Subsección (H3)
#### Detalle (H4)
```

### 2. Énfasis de Texto

Usá negrita para destacar conceptos importantes y cursiva para énfasis suave o términos técnicos.

```markdown
**Texto en negrita**
_Texto en cursiva_
~~Texto tachado~~
```

### 3. Listas

Ideales para enumerar pasos o características.

```markdown
**Lista con viñetas:**
- Primer elemento
- Segundo elemento
  - Sub-elemento (con indentación)

**Lista numerada:**
1. Primer paso
2. Segundo paso
```

### 4. Enlaces

Conectan tu documento con recursos externos o archivos locales.

```markdown
[Documentación de Java](https://docs.oracle.com/en/java/)
[Ir a la guía de Git](./git.md)
```

### 5. Imágenes

Sintaxis idéntica a los enlaces, pero precedida por un signo de exclamación `!`. El texto entre corchetes es el texto alternativo (importante si la imagen no carga).

```markdown
![Logo de la cátedra](../images/logo.png)
```

### 6. Código

Esencial para la documentación técnica. Usá comillas simples invertidas para código integrado en el párrafo, y triple comilla invertida para bloques multilínea.

**Código en línea:**
El método `printf()` imprime en consola.

```markdown
El método `printf()` imprime en consola.
```

**Bloques de código:**
Añadí el nombre del lenguaje después de las comillas triples para activar los colores de sintaxis.

````markdown
```java
public static void main(String[] args) {
    System.out.println("Hola");
}
```
````

---

## Parte 2: GitHub Flavored Markdown (Avanzado)

Estas características son extensiones que no están en el estándar original de Markdown, pero que **GitHub (GFM)** y nuestro motor de documentación (MyST) soportan perfectamente.

### Tablas

Útiles para comparar datos. Usá barras verticales `|` y guiones `-`. Podés alinear con dos puntos `:`.

```markdown
| Izquierda | Centro | Derecha |
| :-------- | :----: | ------: |
| Texto     | Texto  |   Texto |
| Más texto |  Más   |     Más |
```

### Listas de tareas

Permiten hacer checklists interactivos en los Issues o PRs de GitHub.

```markdown
- [x] Tarea completada
- [ ] Tarea pendiente
- [ ] Otra tarea pendiente
```

### Citas (Blockquotes)

Se usan para destacar advertencias, notas importantes o referencias textuales.

```markdown
> Esta es una cita importante.
> Múltiples líneas requieren un `>` al inicio de cada una.
```

### HTML embebido

Si Markdown no te alcanza (ej. para forzar el tamaño de una imagen o hacer una sección desplegable), GitHub permite HTML básico.

```markdown
<details>
<summary>Haz clic para expandir la respuesta</summary>
Acá va el texto oculto.
</details>
```

### Emojis

Podés usar códigos de emoji directamente en GitHub.

```markdown
:smile: :heart: :rocket:
```

### Comentarios invisibles

Si querés dejar una nota que no se vea en el documento final, usá sintaxis de comentario HTML.

```markdown
<!-- TODO: Actualizar esta sección la semana que viene -->
```

---

## Consejos Prácticos

1. **Legibilidad:** Dejá siempre una línea en blanco entre diferentes bloques (ej. entre un párrafo y una lista).
2. **Consistencia:** Elegí un estilo (por ejemplo, siempre usar `-` en lugar de `*` para listas) y mantenelo en todo el documento.
3. **Escapar caracteres:** Si necesitás mostrar un símbolo literal como `*` o `#`, usá una barra invertida antes: `\*`.
