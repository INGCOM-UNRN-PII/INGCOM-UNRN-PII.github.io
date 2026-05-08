---
title: Instalación paso a paso IntelliJ IDEA
description: Guía de instalación y configuración de IntelliJ IDEA para desarrollo en Java.
---

# Instalación paso a paso IntelliJ IDEA

![IntelliJ IDEA](../images/guias/intellij_logo.png)

Complementario al JDK (que **debe estar instalado antes** que el IDE), utilizaremos IntelliJ IDEA. Una herramienta de este tipo es necesaria para cualquier proyecto más complejo que el TP1 debido a cómo se organiza la plataforma Java, además de incluir herramientas de inspección, estilo y testing fundamentales.

## 1. Descarga

:::{tip} Versión Recomendada
Descargá IntelliJ desde [este enlace](https://download.jetbrains.com/idea/ideaIC-2024). No recomendamos usar versiones de pago (Ultimate) aunque estén disponibles gratis para estudiantes: consumen muchos más recursos en funciones que no utilizaremos.
:::

---

## 2. Instalación Básica

El proceso de instalación es estándar. A continuación, los pasos clave para dejar el IDE listo en tu sistema operativo:

### Paso 1: Bienvenida
Iniciá el instalador y continuá con *Next*.

![image](../images/guias/intellij_paso1.png)

### Paso 2: Ubicación
Recomendamos dejar la ruta de instalación por defecto.

![image](../images/guias/intellij_paso2.png)

### Paso 3: Opciones de instalación
**Importante:** En esta pantalla no hace falta integrar el entorno al explorador (Add "Open Folder as Project"). Para nuestra materia, siempre abriremos proyectos completos (clonados) y abrir archivos individuales desde el explorador es innecesariamente pesado.

![image](../images/guias/intellij_paso3.png)

### Paso 4: Menú de inicio
Dejá la opción por defecto y procedé con *Install*.

![image](../images/guias/intellij_paso4.png)

### Paso 5: Finalización
Al terminar, marcá la opción para ejecutar IntelliJ y presioná *Finish*.

![image](../images/guias/intellij_paso5.png)

### Paso 6: Importar configuraciones (Opcional)
Si es la primera vez que lo instalás, elegí "Do not import settings".

![Paso 6 del instalador](../images/guias/intellij_paso6.png)

---

## 3. Primera ejecución y configuración del IDE

Antes de escribir código, conviene ajustar algunas opciones generales que te facilitarán la cursada.

### Términos y Condiciones / Telemetría
1. Aceptá el acuerdo de usuario (User Agreement).

![image](../images/guias/intellij_config1.png)
2. Seleccioná si deseás enviar estadísticas anónimas a JetBrains (opcional).
![image](../images/guias/intellij_config2.png)

### Instalación de corrector ortográfico (Recomendado)
IntelliJ viene con un corrector en inglés. Como escribiremos comentarios, nombres de variables y documentación en español, agregar el diccionario evitará que todo el código aparezca marcado como "error de ortografía".

1. En la pantalla de bienvenida, ve a la pestaña **Plugins**.

![image](../images/guias/intellij_config3.png)


![image](../images/guias/intellij_config4.png)

2. En la barra de búsqueda, escribí `Natural Languages`. Seleccioná el plugin y hacé clic en *Install*.

![image](../images/guias/intellij_config5.png)

3. Presioná *OK* o reiniciá el IDE si lo solicita para aplicar los cambios.

![image](../images/guias/intellij_config6.png)

### Ubicación de trabajo por defecto (Opcional)
Si querés mantener todos los TPs en una sola carpeta, podés configurar el directorio base en **Customize** -> **All settings...** -> **Appearance & Behavior** -> **System Settings** -> **Project directory**. Así no tendrás que buscar la carpeta cada vez que clones un proyecto nuevo.

![image](../images/guias/intellij_config7.png)

---

## 4. Integración con GitHub (Opcional pero muy recomendado)

:::{note} SSH vs Integración del IDE
Si ya configuraste una llave SSH para Git (como se explica en la guía de Git), podés clonar proyectos usando la terminal sin problemas. Sin embargo, conectar IntelliJ directamente con tu cuenta de GitHub te permitirá gestionar _Pull Requests_ y revisar correcciones directamente dentro del editor, simplificando enormemente tu trabajo.
:::

Podés vincular tu cuenta de GitHub al entorno siguiendo estos pasos:

1. En la pantalla de inicio, hacé clic en **Get from VCS**.

![image](../images/guias/intellij_proyecto1.png)

2. Seleccioná la pestaña **GitHub** en el menú lateral.

![image](../images/guias/intellij_proyecto2.png)

3. Hacé clic en **Log In via GitHub...**.

![image](../images/guias/intellij_proyecto3.png)


![image](../images/guias/intellij_proyecto4.png)

4. Se abrirá tu navegador. Presioná **Authorize in GitHub** e iniciá sesión.

![image](../images/guias/intellij_proyecto5.png)


![image](../images/guias/intellij_proyecto6.png)

5. Al volver al IDE, verás un listado de todos los repositorios a los que tenés acceso (incluidos los de la cátedra). Desde aquí podés seleccionar uno y clonarlo directamente.
   *(Alternativamente, podés pegar la URL `git clone` del repositorio en la barra superior).*

![image](../images/guias/intellij_proyecto7.png)

---

## Solución de problemas (Windows)

:::{warning} Antivirus y rendimiento
En Windows 10 y 11, Microsoft Defender analiza constantemente los archivos nuevos. Esto interfiere masivamente con el compilador de Java y ralentiza el IDE.
:::

Cuando abras un proyecto, es muy probable que IntelliJ muestre un aviso sobre el rendimiento debido al antivirus. Hacé clic en **Automatically** para que el IDE agregue las carpetas del proyecto y del JDK a las exclusiones del antivirus. Esto hará que la compilación sea sustancialmente más rápida.

![Configuración de antivirus](../images/guias/intellij_antivirus.png)