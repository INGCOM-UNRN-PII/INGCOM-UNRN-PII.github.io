import os
import re
import unicodedata

def slugify(text):
    # Quitar acentos y normalizar a ASCII
    text = unicodedata.normalize('NFKD', text).encode('ascii', 'ignore').decode('ascii')
    # Pasar a minúsculas
    text = text.lower()
    # Reemplazar caracteres no alfanuméricos por guiones
    text = re.sub(r'[^a-z0-9]+', '-', text)
    # Limpiar guiones duplicados y extremos
    return text.strip('-')

def process_file(path):
    with open(path, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    new_lines = []
    changed = False

    for i, line in enumerate(lines):
        # Match headers #, ##, ###
        header_match = re.match(r'^(#{1,3})\s+(.*)', line)
        if header_match:
            title = header_match.group(2).strip()
            # Si el título contiene otros elementos de MyST (como roles), los limpiamos para el slug
            # Ejemplo: {ref}`regla-xxx` -> regla-xxx
            clean_title = re.sub(r'｛[a-z]+｝`([^`]+)`', r'\1', title)
            slug = slugify(clean_title)
            label = f"({slug})=\n"
            
            # Verificar si la línea anterior ya es una etiqueta
            has_label = False
            if i > 0:
                prev_line = lines[i-1].strip()
                if prev_line.startswith('(') and prev_line.endswith(')='):
                    has_label = True
                    # Si la etiqueta existente es diferente a la que generaríamos, la actualizamos
                    if prev_line != f"({slug})=":
                        if new_lines:
                            new_lines.pop()
                        new_lines.append(label)
                        changed = True
                else:
                    # No hay etiqueta, la añadimos
                    new_lines.append(label)
                    changed = True
            else:
                # Es la primera línea y es un encabezado, añadimos etiqueta
                new_lines.append(label)
                changed = True
        
        new_lines.append(line)

    if changed:
        with open(path, 'w', encoding='utf-8') as f:
            f.writelines(new_lines)
        return True
    return False

def main():
    apunte_dir = './apunte'
    for filename in os.listdir(apunte_dir):
        if filename.endswith('.md') and filename != 'indice.md':
            path = os.path.join(apunte_dir, filename)
            if process_file(path):
                print(f"Actualizado: {filename}")

if __name__ == "__main__":
    main()
