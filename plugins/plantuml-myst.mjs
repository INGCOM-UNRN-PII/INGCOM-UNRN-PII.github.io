// plantuml-myst: plugin de MyST (mystmd) que dibuja diagramas de PlantUML al construir el
// sitio o las exportaciones. Necesita PlantUML (Java) en la máquina que construye: localmente
// con scripts/instalar-plantuml.sh y en GitHub Actions con la action de este repositorio.
//
//   # myst.yml
//   project:
//     plugins:
//       - plugins/plantuml-myst.mjs
//
// Dibuja:
//   - los bloques de código ```plantuml y ```puml (con o sin @startuml/@enduml); los de
//     ```{code} plantuml se dejan como código, para mostrar la sintaxis;
//   - la directiva {plantuml} (alias {uml}), con :theme:, :width:, :align:, :alt:, :class:,
//     :caption: y :label:, y opcionalmente un archivo .puml como argumento.
//
// Los diagramas de una página se dibujan con una sola ejecución de PlantUML y quedan en una
// caché (por omisión _build/plantuml), indexada por el contenido: sólo se vuelve a dibujar lo
// que cambió. scripts/prerenderizar.mjs llena la caché de todo un proyecto de una vez.
//
// Variables de entorno:
//   PLANTUML_JAR           ruta a plantuml.jar; se ejecuta con `java -jar`.
//   PLANTUML               comando de PlantUML si no hay PLANTUML_JAR (por omisión, `plantuml`).
//   PLANTUML_MYST_CACHE    directorio de la caché (por omisión _build/plantuml).
//   PLANTUML_MYST_SALIDA   "embebido" (por omisión): cada SVG va en la página como data URI.
//                          "diagramas": escribe diagramas/plantuml-<hash>.svg al lado de la página.
//   PLANTUML_MYST_TEMA     theme de PlantUML por omisión (!theme), p. ej. "plain" o "mars".
//   PLANTUML_MYST_CONFIG   archivo .puml con skinparams comunes, que se antepone a cada diagrama.

import fs from "node:fs";
import path from "node:path";
import crypto from "node:crypto";
import { spawnSync } from "node:child_process";

export const VERSION = "0.1.0";
export const LENGUAJES = ["plantuml", "puml"];

// ── Configuración ────────────────────────────────────────────────────────────────────────
/** El comando de PlantUML, como [ejecutable, ...argumentos]. */
export function comando() {
  if (process.env.PLANTUML_JAR) return ["java", "-jar", process.env.PLANTUML_JAR];
  const c = (process.env.PLANTUML || "plantuml").trim();
  return c.split(/\s+/);
}

export function dirCache() {
  return path.resolve(process.env.PLANTUML_MYST_CACHE || path.join("_build", "plantuml"));
}

function salida() {
  const s = (process.env.PLANTUML_MYST_SALIDA || "embebido").trim();
  if (s !== "embebido" && s !== "diagramas") {
    throw new Error(`plantuml-myst: PLANTUML_MYST_SALIDA debe ser "embebido" o "diagramas", no "${s}"`);
  }
  return s;
}

function config() {
  const ruta = process.env.PLANTUML_MYST_CONFIG;
  if (!ruta) return "";
  try {
    return fs.readFileSync(path.resolve(ruta), "utf8").replace(/\r/g, "").trimEnd();
  } catch (e) {
    throw new Error(`plantuml-myst: no se pudo leer PLANTUML_MYST_CONFIG (${ruta}): ${e.message}`);
  }
}

// ── Fuente ───────────────────────────────────────────────────────────────────────────────
const INICIO = /^\s*@start(\w+)/m;

/**
 * La fuente completa de un diagrama: agrega @startuml/@enduml si faltan e inserta el theme y la
 * configuración común después de la línea @start. `theme` vacío o null usa PLANTUML_MYST_TEMA.
 */
export function prepararFuente(texto, opciones = {}) {
  return preparar(texto, opciones).fuente;
}

/** Como prepararFuente, y además `linea(n)`: la línea del texto original para la línea n de la fuente. */
export function preparar(texto, { theme } = {}) {
  const original = String(texto ?? "").replace(/\r/g, "");
  const iniciales = (original.match(/^(\s*\n)*/)[0].match(/\n/g) || []).length;
  let fuente = original.trim();
  const envuelto = !INICIO.test(fuente);
  if (envuelto) fuente = `@startuml\n${fuente}\n@enduml`;
  const tema = (theme ?? process.env.PLANTUML_MYST_TEMA ?? "").trim();
  const extra = [config(), tema ? `!theme ${tema}` : ""].filter(Boolean).join("\n");
  const n = extra ? extra.split("\n").length : 0;
  if (extra) fuente = fuente.replace(/^(\s*@start\w+[^\n]*)\n?/m, `$1\n${extra}\n`);
  const linea = (p) => {
    const l = envuelto ? p - 1 - n : p <= 1 ? p : p - n;
    return l >= 1 ? l + iniciales : null;
  };
  return { fuente: fuente + "\n", linea };
}

const INCLUSION = /^\s*!(?:include|includesub|include_many|include_once|import)\s+([^\s<][^\s!]*)/gm;

/** El contenido de los archivos incluidos con ruta relativa, recursivamente, para la clave. */
function inclusiones(fuente, dir, vistos = new Set()) {
  let total = "";
  for (const [, ref] of fuente.matchAll(INCLUSION)) {
    if (/^https?:/.test(ref)) continue;
    const ruta = path.resolve(dir, ref.replace(/!.*$/, ""));
    if (vistos.has(ruta)) continue;
    vistos.add(ruta);
    let contenido;
    try {
      contenido = fs.readFileSync(ruta, "utf8");
    } catch {
      continue; // PlantUML informará el error
    }
    total += `\0${ref}\0${contenido}` + inclusiones(contenido, path.dirname(ruta), vistos);
  }
  return total;
}

/**
 * La clave de caché: la fuente, el contenido de los archivos que incluye y el comando de
 * PlantUML (cambiar de versión de PlantUML con PLANTUML_JAR invalida la caché). No depende de
 * la ubicación del proyecto ni de la versión del plugin: la caché sirve en otra máquina, en CI
 * y entre el script de pre-renderizado de la action y el plugin del sitio.
 */
export function clave(fuente, dir) {
  return crypto
    .createHash("sha256")
    .update(`${comando().join(" ")}\0${fuente}${inclusiones(fuente, dir)}`)
    .digest("hex")
    .slice(0, 20);
}

// ── PlantUML ─────────────────────────────────────────────────────────────────────────────
export const estadisticas = { ejecuciones: 0, dibujados: 0, cache: 0 };

/** Lee el informe de -stdrpt:1: bloques status/lineNumber/label seguidos de "Error line N in file: X". */
function leerErrores(texto) {
  const errores = new Map();
  let actual = {};
  for (const linea of texto.split(/\r?\n/)) {
    let m;
    if ((m = linea.match(/^lineNumber=(\d+)/))) actual.linea = Number(m[1]);
    else if ((m = linea.match(/^label=(.*)$/))) actual.mensaje = m[1];
    else if ((m = linea.match(/^Error line (\d+) in file: (.+)$/))) {
      errores.set(path.resolve(m[2].trim()), {
        linea: actual.linea ?? Number(m[1]),
        mensaje: actual.mensaje || "error de sintaxis",
      });
      actual = {};
    }
  }
  return errores;
}

/**
 * Dibuja diagramas en lote. `items`: [{fuente, dir}] (dir: el directorio para los !include
 * relativos). Devuelve, en el mismo orden, [{clave, svg}] o [{clave, error, linea}]. Usa la
 * caché y ejecuta PlantUML una vez por directorio con diagramas nuevos.
 */
export function dibujarLote(items) {
  const cache = dirCache();
  const fuentes = path.join(cache, "fuentes");
  const resultados = items.map(({ fuente, dir }) => ({ clave: clave(fuente, dir), fuente, dir }));
  const pendientes = new Map(); // dir → resultados sin SVG en caché
  for (const r of resultados) {
    const svg = path.join(cache, `${r.clave}.svg`);
    if (fs.existsSync(svg)) {
      r.svg = fs.readFileSync(svg, "utf8");
      estadisticas.cache++;
    } else {
      if (!pendientes.has(r.dir)) pendientes.set(r.dir, []);
      pendientes.get(r.dir).push(r);
    }
  }
  for (const [dir, lote] of pendientes) {
    fs.mkdirSync(fuentes, { recursive: true });
    const archivos = [...new Set(lote.map((r) => r.clave))].map((c) => {
      const archivo = path.join(fuentes, `${c}.puml`);
      fs.writeFileSync(archivo, lote.find((r) => r.clave === c).fuente);
      return archivo;
    });
    const temporal = fs.mkdtempSync(path.join(cache, "tmp-"));
    const [exe, ...args] = comando();
    const opcionesJava = [process.env.JAVA_TOOL_OPTIONS, "-Djava.awt.headless=true", `-Dplantuml.include.path=${dir}`]
      .filter(Boolean)
      .join(" ");
    const p = spawnSync(exe, [...args, "-tsvg", "-charset", "UTF-8", "-stdrpt:1", "-o", temporal, ...archivos], {
      cwd: dir,
      encoding: "utf8",
      env: { ...process.env, JAVA_TOOL_OPTIONS: opcionesJava },
      maxBuffer: 64 * 1024 * 1024,
    });
    estadisticas.ejecuciones++;
    if (p.error) {
      fs.rmSync(temporal, { recursive: true, force: true });
      const mensaje =
        p.error.code === "ENOENT"
          ? `no se encontró PlantUML (${exe}). Instalalo con scripts/instalar-plantuml.sh o definí PLANTUML_JAR`
          : `no se pudo ejecutar PlantUML: ${p.error.message}`;
      for (const r of lote) r.error = mensaje;
      continue;
    }
    const errores = leerErrores(`${p.stdout}\n${p.stderr}`);
    for (const r of lote) {
      const archivo = path.join(fuentes, `${r.clave}.puml`);
      const generado = path.join(temporal, `${r.clave}.svg`);
      const error = errores.get(path.resolve(archivo));
      if (error) {
        r.error = error.mensaje;
        r.linea = error.linea; // en la fuente preparada, desde 1
      } else if (fs.existsSync(generado)) {
        r.svg = fs.readFileSync(generado, "utf8");
        fs.renameSync(generado, path.join(cache, `${r.clave}.svg`));
        estadisticas.dibujados++;
      } else if (fs.existsSync(path.join(cache, `${r.clave}.svg`))) {
        // El mismo diagrama, repetido en el lote: lo dibujó una ejecución anterior de este lote.
        r.svg = fs.readFileSync(path.join(cache, `${r.clave}.svg`), "utf8");
      } else {
        const detalle = (p.stderr || p.stdout || "").split("\n").filter((l) => l && !l.startsWith("Picked up")).slice(0, 3);
        r.error = `PlantUML no generó el diagrama (código ${p.status})${detalle.length ? `: ${detalle.join(" / ")}` : ""}`;
      }
    }
    fs.rmSync(temporal, { recursive: true, force: true });
  }
  return resultados.map(({ clave, svg, error, linea }) => (error ? { clave, error, linea } : { clave, svg }));
}

// ── Nodos ────────────────────────────────────────────────────────────────────────────────
/** Texto alternativo: el título del diagrama, o su tipo. */
export function textoAlternativo(fuente) {
  const titulo = fuente.match(/^\s*title\s+(.+)$/im);
  const tipo = (fuente.match(INICIO) || [])[1] || "uml";
  const nombres = { uml: "UML", mindmap: "mapa mental", wbs: "WBS", gantt: "Gantt", json: "JSON", yaml: "YAML", salt: "interfaz (Salt)", ebnf: "EBNF", regex: "expresión regular", chen: "entidad-relación" };
  const base = `Diagrama ${nombres[tipo] || tipo}`;
  return titulo ? `${base}: ${titulo[1].trim()}` : base;
}

function escribirSiCambio(ruta, svg) {
  if (fs.existsSync(ruta) && fs.readFileSync(ruta, "utf8") === svg) return;
  fs.mkdirSync(path.dirname(ruta), { recursive: true });
  fs.writeFileSync(ruta, svg);
}

function urlDe(r, vfile) {
  if (salida() === "embebido" || !vfile?.path) {
    return `data:image/svg+xml;base64,${Buffer.from(r.svg).toString("base64")}`;
  }
  const nombre = `plantuml-${r.clave}.svg`;
  escribirSiCambio(path.join(path.dirname(vfile.path), "diagramas", nombre), r.svg);
  return `diagramas/${nombre}`;
}

function reportar(vfile, mensaje, posicion) {
  if (!vfile?.message) return;
  const m = vfile.message(mensaje, posicion, "plantuml-myst:render");
  m.fatal = true;
  m.url = "https://plantuml.com/";
}

function nodoError(fuente, mensaje) {
  return {
    type: "admonition",
    kind: "danger",
    children: [
      { type: "admonitionTitle", children: [{ type: "text", value: "Error de PlantUML" }] },
      { type: "paragraph", children: [{ type: "text", value: mensaje }] },
      { type: "code", lang: "text", value: fuente },
    ],
  };
}

const DIRECTIVA_DE_CODIGO = /\{(code|code-block|code-cell|sourcecode|literalinclude)\}/;

/**
 * Si un bloque de código plantuml/puml es para dibujar. Los de la directiva {plantuml} y los
 * bloques con valla (```plantuml) sí; los de {code} / {code-block} no: muestran el código
 * (por ejemplo, en una guía de PlantUML). mystmd no deja marca de la directiva en el nodo,
 * pero sus salidas no tienen posición, y si la tienen, la línea de la fuente lo dice.
 */
export function esParaDibujar(nodo, lineas) {
  if (nodo.plantuml) return true;
  const n = nodo.position?.start?.line;
  if (!n) return false;
  const linea = lineas?.[n - 1];
  return linea === undefined || !DIRECTIVA_DE_CODIGO.test(linea);
}

function lineasDe(vfile) {
  try {
    return vfile?.path ? fs.readFileSync(vfile.path, "utf8").split(/\r?\n/) : null;
  } catch {
    return null;
  }
}

/** Los bloques a dibujar del árbol: [{padre, indice, nodo}]. */
function buscar(arbol, vfile) {
  const encontrados = [];
  let lineas;
  (function recorrer(nodo) {
    const hijos = nodo.children;
    if (!Array.isArray(hijos)) return;
    hijos.forEach((h, i) => {
      if (h.type === "code" && LENGUAJES.includes(h.lang)) {
        if (lineas === undefined) lineas = lineasDe(vfile);
        if (esParaDibujar(h, lineas)) encontrados.push({ padre: hijos, indice: i, nodo: h });
      } else recorrer(h);
    });
  })(arbol);
  return encontrados;
}

/**
 * Reemplaza, en el árbol, los bloques ```plantuml/puml (y los que generó la directiva) por
 * sus diagramas, dibujados en un solo lote. Devuelve la cantidad de diagramas.
 */
export function transformarArbol(arbol, vfile) {
  const bloques = buscar(arbol, vfile);
  if (!bloques.length) return 0;
  const dir = vfile?.path ? path.dirname(path.resolve(vfile.path)) : process.cwd();
  let resultados;
  try {
    const preparados = bloques.map(({ nodo }) => preparar(nodo.value, { theme: nodo.plantuml?.theme }));
    // Los !include relativos se resuelven desde la página, o desde el archivo de la directiva.
    const items = preparados.map((p, i) => ({ fuente: p.fuente, dir: bloques[i].nodo.plantuml?.dir || dir }));
    resultados = dibujarLote(items).map((r, i) => ({
      ...r,
      fuente: preparados[i].fuente,
      linea: r.linea ? preparados[i].linea(r.linea) : undefined,
    }));
  } catch (e) {
    resultados = bloques.map(() => ({ error: e.message }));
  }
  bloques.forEach(({ padre, indice, nodo }, i) => {
    const r = resultados[i];
    const o = nodo.plantuml || {};
    if (r.error) {
      const donde = r.linea ? ` (línea ${r.linea} del diagrama)` : "";
      const fuenteVisible = nodo.value;
      const mensaje = `PlantUML: ${r.error}${donde}`;
      reportar(vfile, mensaje, nodo.position);
      padre[indice] = nodoError(fuenteVisible, mensaje);
      return;
    }
    const imagen = { type: "image", url: urlDe(r, vfile), alt: o.alt || textoAlternativo(r.fuente) };
    if (o.width) imagen.width = o.width;
    imagen.align = o.align || "center";
    if (o.class) imagen.class = o.class;
    if (nodo.position) imagen.position = nodo.position;
    padre[indice] = imagen;
  });
  return bloques.length;
}

const transformacion = {
  name: "plantuml-bloques",
  doc: "Dibuja los bloques de código plantuml y puml, y la directiva {plantuml}, con PlantUML.",
  stage: "document",
  plugin: () => (arbol, vfile) => {
    transformarArbol(arbol, vfile);
  },
};

// ── Directiva {plantuml} ─────────────────────────────────────────────────────────────────
/** Las opciones de la directiva, también las usa scripts/prerenderizar.mjs. */
export const OPCIONES = ["theme", "width", "align", "alt", "caption", "label", "class"];

const directiva = {
  name: "plantuml",
  alias: ["uml"],
  doc: "Un diagrama de PlantUML: el cuerpo, o el archivo .puml del argumento.",
  arg: { type: String, doc: "Archivo .puml, relativo a la página (opcional)." },
  options: {
    theme: { type: String, doc: "Theme de PlantUML (!theme), p. ej. plain, mars, cerulean." },
    width: { type: String, doc: "Ancho de la imagen (por ejemplo 80% o 400px)." },
    align: { type: String, doc: "Alineación: left, center o right." },
    alt: { type: String, doc: "Texto alternativo; por omisión, el title del diagrama o su tipo." },
    caption: { type: String, doc: "Epígrafe; convierte el diagrama en una figura numerada." },
    label: { type: String, doc: "Etiqueta para referenciar la figura." },
    class: { type: String, doc: "Clases CSS de la imagen." },
  },
  body: { type: String, doc: "El diagrama en PlantUML." },
  run(data, vfile, ctx) {
    const o = data.options || {};
    let fuente = data.body || "";
    let dir;
    if (data.arg) {
      const base = vfile?.path ? path.dirname(path.resolve(vfile.path)) : process.cwd();
      const archivo = path.resolve(base, String(data.arg).trim());
      dir = path.dirname(archivo);
      try {
        fuente = fs.readFileSync(archivo, "utf8");
      } catch (e) {
        const mensaje = `PlantUML: no se pudo leer ${data.arg}: ${e.message}`;
        reportar(vfile, mensaje, data.node?.position);
        return [nodoError("", mensaje)];
      }
    }
    // Un bloque de código con las opciones: lo dibuja la transformación, junto con los demás.
    const bloque = {
      type: "code",
      lang: "plantuml",
      value: fuente,
      plantuml: { theme: o.theme, width: o.width, align: o.align, alt: o.alt, class: o.class, dir },
    };
    if (data.node?.position) bloque.position = data.node.position;
    if (!o.caption && !o.label) return [bloque];
    const figura = { type: "container", kind: "figure", children: [bloque] };
    if (o.label) {
      figura.label = o.label;
      figura.identifier = o.label.trim().toLowerCase().replace(/\s+/g, "-");
    }
    if (o.caption) {
      const contenido = ctx?.parseMyst ? ctx.parseMyst(o.caption).children : [{ type: "paragraph", children: [{ type: "text", value: o.caption }] }];
      figura.children.push({ type: "caption", children: contenido });
    }
    return [figura];
  },
};

const plugin = {
  name: `plantuml ${VERSION}`,
  directives: [directiva],
  transforms: [transformacion],
};

export default plugin;
