# Copilot Instructions for Programación II

This is an educational website for the Programación II (Programming II) course at UNRN Andina, built with [MyST Markdown](https://mystmd.org/).

## Git usage

Create semantic commits for each change introduced.

## Build & Deploy

**Build the site locally:**
```bash
npm install -g mystmd  # One-time global install
myst build --html
```

Output is generated in `_build/html/`. The site deploys automatically to GitHub Pages on pushes to `main` via `.github/workflows/deploy.yml`.

Use `myst build --html` as the default validation step after editorial, navigation, TOC, reference, or SVG changes. Pay special attention to MyST warnings already seen in this repo:

- `Cross reference target was not found:`
- `No target for internal reference`
- parsing collisions between MyST citations and Java/Javadoc annotations such as ``@Test``, ``@Override``, ``@param``, ``@return`` and ``@throws``

When those annotations appear in prose, write them as inline code or escape them so MyST does not parse them as citations.

## Python Automation & MCP

This repository includes Python automation scripts (in `scripts/`) managed with **uv**. See `MCP_CONFIG.md` for Claude MCP server configuration to execute scripts directly.

**Run scripts:**
```bash
uv run python -m scripts.add_myst_anchors        # Add MyST anchors to headers
uv run python -m scripts.generate_apunte_index   # Generate apuntes index
uv run python -m scripts.generate_guides_index   # Generate guides index
uv run python -m scripts.generate_rules_index    # Generate rules index
```

All scripts use Python stdlib only (Python 3.9+). Initialize environment with: `uv sync`

## Project Structure

- **`parte_1/` to `parte_4/`** - Published course notes currently integrated in the main TOC
- **`parte_5/` and `parte_6/`** - Material not currently published in the main TOC; treat according to `editorial/estado_y_mantenimiento.md`
- **`guias/`** - Installation and tool tutorials (JDK, IntelliJ, Git, Bash, PlantUML)
- **`reglas/`** - Coding style rules and conventions (Java code standards, testing, documentation, exceptions)
- **`editorial/`** - Editorial source of truth for chapter structure, style, format, and publication criteria
- **`catedra/`** - Course policies and agreements
- **`glosario.md`** - Technical glossary for the course
- **`resources/`** - Shared CSS for SVG diagrams (`svg.css`) and images
- **`scripts/`** - Python automation scripts for generating indices and adding MyST anchors
- **`myst.yml`** - MyST project configuration with table of contents and site settings

## Editorial Source of Truth

For editorial decisions, use these files as the authoritative source:

- `editorial/plantilla_capitulos.md`
- `editorial/estilo_y_formato.md`
- `editorial/estado_y_mantenimiento.md`

If this file or `GEMINI.md` conflicts with `editorial/`, **`editorial/` wins**.

Critical reminders that remain repository-specific:

- Keep the course’s **late objects** approach.
- Write in Spanish rioplatense with **voseo** and use **lazos** for loops.
- When writing content, link to relevant rules in `reglas/` using `{ref}` where it helps students connect explanation with the official criterion.
- Keep indices, `myst.yml`, and page status aligned.

After editing published teaching content, do a quick pass for visible tuteo drift such as `necesitas`, `quieres`, `puedes`, `tienes`, `trabajas` or `serás capaz`, and normalize it to voseo or an impersonal formulation.

### Important References

- **`editorial/`** - Source of truth for chapter structure, style, format, and maintenance
- **GEMINI.md** - Companion instruction file that should defer to `editorial/` for editorial rules
- **reglas/** directory - Authoritative style and coding rules (students are held to these standards)
- **myst.yml** - Table of contents and build configuration; update when adding new pages

## Automation Scripts

Python scripts in `scripts/`:
- `generate_apunte_index.py` - Creates index pages for apuntes
- `generate_guides_index.py` - Generates guides directory index
- `generate_rules_index.py` - Generates rules directory index
- `add_myst_anchors.py` - Adds MyST anchor labels to markdown headers

These maintain consistency in index structure; review before modifying TOC structure in `myst.yml`.

## Typical Tasks

**Updating a part index (`parte_X/indice.md`):**
1. Treat it as an editorial **mapa de aprendizaje**, not just a file list
2. Include the part purpose, `## Orden sugerido de lectura`, `## Capítulos nucleares`, `## Repaso y ampliación`, and an explicit exhaustive index
3. Prefer visible pedagogical titles in links instead of raw filenames
4. Keep the part index aligned with `myst.yml` and the real publication state

**Updating a family index (`parte_4/*/indice.md`):**
1. Keep navigation to the family’s pattern pages explicit
2. Include `Cuándo usar` plus either a comparison table or a quick decision aid
3. Link the family’s `ejercicios_integradores.md` as the closing practical step
4. Keep the three family indices comparable in structure and depth

**Processing shared inventories with a lock file:**
1. Re-read both the shared inventory and its `.lock` file before claiming new work
2. Recalculate current `[~]`/`[x]` state from the files, not from stale assumptions
3. Update claimed markers and the lock file in the same edit so both stay consistent
4. If an agent becomes stale because items were already completed elsewhere, stop it instead of letting it keep writing

**Adding a new lesson:**
1. Create markdown file in appropriate directory (e.g., `parte_1/14_nuevotema.md`)
2. Apply the structure defined in `editorial/plantilla_capitulos.md`
3. Update the corresponding part index and `myst.yml` if the lesson is published
4. If adding diagrams, create `parte_1/14/` directory with SVG files
5. Reference style rules where applicable using `{ref}`
6. Run build locally to test
7. Commit and push to `main` (auto-deploys)

**Creating SVG diagrams:**
1. Follow `editorial/estilo_y_formato.md`
2. Create in appropriate numbered subdirectory (e.g., `parte_1/13/pila_dinamica.svg`)
3. Use `resources/svg.css` as the style source of truth, but keep published SVGs **autocontenidos**
4. Inline the needed classes/styles in each SVG; do **not** rely on `xml-stylesheet` or external CSS references for published pages
5. Reference in markdown with `{figure}` directive including `:label:` and `:width:`

**Updating rules or guidelines:**
1. Edit relevant file in `reglas/`
2. Add clear anchor labels (e.g., `(rule-0x1234)=`) for cross-referencing
3. Ensure consistency with existing rule numbering (hex codes like 0x0000, 0x0001)
4. Update `reglas/indice.md` if adding new rule files

## Technology Stack

- **MyST Markdown** - Content authoring with rich formatting
- **Node.js** - Runtime for MyST CLI (v18.x in CI)
- **GitHub Pages** - Hosting (configured via Actions)
- **SVG + CSS** - Technical diagrams with institutional branding
- **Python** - Automation and index generation

## Repository Info

- **GitHub**: https://github.com/INGCOM-UNRN-PII/INGCOM-UNRN-PII.github.io
- **Live Site**: Generated from `main` branch
- **Audience**: Students in Programación II; developers maintaining course materials
- **Languages**: Markdown (content), SVG (diagrams), Python (automation), small amount of CSS/YAML (configuration)
