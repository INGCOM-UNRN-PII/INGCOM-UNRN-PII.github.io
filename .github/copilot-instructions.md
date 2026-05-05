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

- **`parte_1/` and `parte_2/`** - Main course notes (Notes 1 and 2). Organized by topic with corresponding SVG diagrams in numbered subdirectories (e.g., `parte_1/13/` for `parte_1/13_tad.md`)
- **`guias/`** - Installation and tool tutorials (JDK, IntelliJ, Git, Bash, PlantUML)
- **`reglas/`** - Coding style rules and conventions (Java code standards, testing, documentation, exceptions)
- **`catedra/`** - Course policies and agreements
- **`glosario.md`** - Technical glossary for the course
- **`resources/`** - Shared CSS for SVG diagrams (`svg.css`) and images
- **`scripts/`** - Python automation scripts for generating indices and adding MyST anchors
- **`myst.yml`** - MyST project configuration with table of contents and site settings

## Key Conventions

### MyST Markdown Features

The site uses MyST MD syntax. Key elements:

- **Admonitions**: `:::{note}` / `:::{important}` / `:::{warning}` / `:::{tip}`
- **Exercises & Solutions**:
  ```myst
  ```{exercise}
  :label: ex-label
  Problem statement here
  ```

  :::{solution} ex-label
  :class: dropdown
  Solution details here
  ```
  ```
  :::
  ```
- **Math**: Inline `$...$` or display `$$...$$` (LaTeX)
- **Code blocks**: Use triple backticks with language syntax (java, python, bash)
- **Cross-references**: Use `{ref}` role: `{ref}`rule-0x0000`` points to anchors like `(rule-0x0000)=`
- **Figures**: `{figure} path/to/image.svg` with `:label:`, `:align: center`, `:width: %`

### SVG Diagram Conventions

SVG diagrams are **stored in numbered subdirectories** matching the markdown file:
- For `parte_1/13_tad.md`, diagrams go in `parte_1/13/`
- Use descriptive names: `pila_arreglo.svg`, `cola_circular.svg`

**SVG guidelines:**
- Import shared CSS: `<?xml-stylesheet href="../../resources/svg.css" type="text/css"?>`
- Use semantic SVG classes from `resources/svg.css`:
  - Stacks: `.stack-node`, `.stack-data`, `.stack-arrow`
  - Queues: `.queue-node`, `.queue-data`, `.queue-arrow`
  - Trees: `.tree-node`, `.tree-data`
  - Lists: `.list-node`, `.list-data`
  - Typical dimensions: 600-800px width, appropriate viewBox
- UNRN colors: `#eb2141` (red, primary), `#192437` (dark blue, secondary)
- Use institutional fonts: Fabrikat (titles), Lato (text), Share Tech Mono (code)

### Content & Language

**Tone & Style:**
- Spanish (Argentine Spanish with "voseo")
- Academic but approachable; university-level rigor without over-simplification
- No emoji unless explicitly requested
- Use "lazos" for loops (specific course terminology)

**Pedagogy:**
- "Late Objects" approach: build on C knowledge from the prerequisite course (Programación I)
- Gradually introduce Java concepts, starting with procedural aspects before deep OOP

**Code Style Rules:**
The course maintains extensive style guides in `reglas/`:
- `0_generales.md` - Nomenclature and format (no spelling errors, markdown where applicable)
- `1_documentacion.md` - Documentation standards
- `2_oop.md` - OOP-specific conventions
- `3_excepciones.md` - Exception handling
- `4_testing.md` - Testing practices (JUnit focus)
- `5_control.md` - Control flow
- `convenciones_codigo_java.md` - Java naming conventions (UpperCamelCase for classes, lowerCamelCase for variables/methods, UPPER_SNAKE_CASE for constants)

**When writing content**, link to relevant rules using the `{ref}` role to guide students to official guidelines.

### Important References

- **GEMINI.md** - Detailed directives for AI tools (MyST syntax, SVG creation, code style references)
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

**Adding a new lesson:**
1. Create markdown file in appropriate directory (e.g., `parte_1/14_nuevotema.md`)
2. Add entry to `myst.yml` table of contents
3. If adding diagrams, create `parte_1/14/` directory with SVG files
4. Reference style rules where applicable using `{ref}`
5. Run build locally to test
6. Commit and push to `main` (auto-deploys)

**Creating SVG diagrams:**
1. Create in appropriate numbered subdirectory (e.g., `parte_1/13/pila_dinamica.svg`)
2. Include CSS stylesheet reference with correct relative path
3. Use shared classes from `resources/svg.css` for consistency
4. Reference in markdown with `{figure}` directive including `:label:` and `:width:`

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
