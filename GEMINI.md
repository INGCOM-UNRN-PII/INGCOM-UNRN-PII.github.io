# Directivas para Gemini en Programación II

## Fuente de verdad editorial

Para toda decisión editorial, usar como referencia principal:

- `editorial/plantilla_capitulos.md`
- `editorial/estilo_y_formato.md`
- `editorial/estado_y_mantenimiento.md`

Si este archivo o `.github/copilot-instructions.md` entra en conflicto con `editorial/`, **prevalece `editorial/`**.

## Contexto específico de la cátedra

- La estrategia pedagógica es **late objects**.
- Se aprovecha el conocimiento previo de C siempre que ayude a construir el nuevo concepto.
- Los bucles se llaman **lazos**.
- El tono esperado es español rioplatense con **voseo**, nivel universitario y sin emojis salvo pedido explícito.
- Cuando un contenido ejemplifica una convención importante, conviene enlazar reglas de `reglas/` usando `{ref}`.

## Recordatorios operativos

### Al crear o modificar capítulos

1. Aplicar la plantilla editorial correspondiente.
2. Mantener alineados el archivo, el índice de su parte y `myst.yml`.
3. Si el material no está listo para publicación, no tratarlo como contenido publicado.

### Al crear diagramas SVG

1. Seguir `editorial/estilo_y_formato.md`.
2. Usar el subdirectorio numerado correspondiente al capítulo.
3. Reutilizar `resources/svg.css` y sus clases semánticas cuando sea posible.

### Al reestructurar partes o mover contenido

1. Decidir primero el estado editorial real del material.
2. Corregir `myst.yml`.
3. Corregir después el índice de la parte.
4. Evitar que queden placeholders o recorridos históricos fingiendo ser contenido vigente.

## Referencias útiles

- `editorial/indice.md`
- `reglas/indice.md`
- `myst.yml`
