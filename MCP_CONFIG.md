---
title: Claude MCP Configuration
description: MCP server setup for Python script execution using uv
---

# Claude MCP Configuration

## Python MCP Server (uv)

This project is set up to work with Claude's Python MCP server for running automation scripts via `uv`.

### Setup

The repository includes a Python project configured with `uv`:

- **`pyproject.toml`** - Project configuration (minimal, all scripts use stdlib only)
- **`uv.lock`** - Lock file for reproducible environments
- **`scripts/`** - Automation scripts for generating content indices and MyST anchors

### Running Scripts

Use `uv` to execute scripts from the scripts directory:

```bash
# Add MyST anchor labels to markdown headers
uv run python -m scripts.add_myst_anchors

# Generate index for apuntes
uv run python -m scripts.generate_apunte_index

# Generate index for guides
uv run python -m scripts.generate_guides_index

# Generate index for rules
uv run python -m scripts.generate_rules_index
```

Or run directly with Python:

```bash
cd scripts/
python add_myst_anchors.py
python generate_apunte_index.py
python generate_guides_index.py
python generate_rules_index.py
```

### Claude Configuration

To set up Claude as your MCP client, add this to your Claude configuration (typically `~/.claude/claude_desktop_config.json` or your Claude preferences):

```json
{
  "mcpServers": {
    "python-executor": {
      "command": "uv",
      "args": ["run", "python", "-c", "import sys; exec(sys.stdin.read())"],
      "env": {
        "PYTHONPATH": "/home/mrtin/dev/edu-sitios/INGCOM-UNRN-PII.github.io"
      }
    }
  }
}
```

This allows Claude to:
- Execute Python code snippets within the repository context
- Run scripts from the `scripts/` directory
- Test changes before committing
- Verify script functionality interactively

### Dependencies

All scripts use Python standard library only, so `uv sync` creates a minimal environment with just the Python interpreter.

Optional development tools can be installed with:

```bash
uv sync --all-extras  # Installs pytest, black, ruff for linting/testing
```

### Script Purposes

| Script | Purpose |
|--------|---------|
| `add_myst_anchors.py` | Adds MyST anchor labels to markdown headers for cross-referencing |
| `generate_apunte_index.py` | Creates index pages for apuntes (course notes) |
| `generate_guides_index.py` | Creates index pages for guides (tutorials) |
| `generate_rules_index.py` | Creates index pages for coding rules |

See `scripts/` directory for implementation details.
