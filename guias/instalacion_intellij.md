---
title: Instalación paso a paso IntelliJ IDEA
description: Guía de instalación y configuración de IntelliJ IDEA para desarrollo en Java.
---

# Instalación paso a paso IntelliJ IDEA

![IntelliJ IDEA](../images/guias/intellij_logo.png)

Complementario al JDK, que debe de estar instalado antes que el IDE, una herramienta de este tipo es necesaria para cualquier cosa más compleja que el TP1. Esto es por la forma que tiene la Plataforma Java de organizarse.

## Descarga

:::{tip}
Pueden descargarlo de [este enlace](https://download.jetbrains.com/idea/ideaIC-2024.3.3.exe). No les recomiendo descargar las versiones de pago, que a pesar de estar disponibles a estudiantes, consumen más recursos en cuestiones que no les daremos uso. Además de que no les daremos soporte a quienes opten por utilizar otras herramientas (recuerden que somos muchos).
:::

## Instalador

### Paso 1
![image](../images/guias/intellij_paso1.png)

### Paso 2
![image](../images/guias/intellij_paso2.png)

### Paso 3
![image](../images/guias/intellij_paso3.png)

### Paso 4
![image](../images/guias/intellij_paso4.png)

### Paso 5
![image](../images/guias/intellij_paso5.png)

### Paso 6

:::{note}
Este es opcional, pero es necesario para terminar de configurar la herramienta.
:::

![Paso 6 del instalador](../images/guias/intellij_paso6.png)

## Primera ejecución y configuración base

### Paso 1 - Términos y Condiciones
![image](../images/guias/intellij_config1.png)

### Paso 2 - Telemetría opcional
![image](../images/guias/intellij_config2.png)

### Paso 3 - Pantalla de bienvenida
![image](../images/guias/intellij_config3.png)

### Paso 4 - Acceso a las opciones
![image](../images/guias/intellij_config4.png)

### Paso 5 - Instalación del corrector ortográfico en Español
La forma más simple es buscar `Natural Languages` para ir a esta pantalla.
![image](../images/guias/intellij_config5.png)

### Paso 6
Le damos OK para que se descargue.
![image](../images/guias/intellij_config6.png)

### Paso 7 Ubicación de trabajo por defecto
Este paso es completamente opcional, pero si quieren mantener todo ordenado, está bueno que no tengan que cambiar manualmente la ubicación con cada proyecto que inician.
![image](../images/guias/intellij_config7.png)

## Configuración de GitHub

:::{important}
Para simplificar _bastante_ el ciclo de trabajo, no solo acceden a los repositorios (esto lo van a hacer igual con la llave SSH), sino también al Pull Request de la corrección.
:::

### Paso 1
En la pantalla de Inicio
![image](../images/guias/intellij_proyecto1.png)

### Paso 2
![image](../images/guias/intellij_proyecto2.png)

### Paso 3
![image](../images/guias/intellij_proyecto3.png)

### Paso 4
![image](../images/guias/intellij_proyecto4.png)

### Paso 5
Es necesario iniciar sesión en GitHub para la autorización.
![image](../images/guias/intellij_proyecto5.png)

### Paso 6
Confirmación de la autorización
![image](../images/guias/intellij_proyecto6.png)

### Paso 7
Aquí verán todos los repositorios en los que tienen acceso, propios y los que son administrados por las cátedras.

### Paso 8
Alternativamente, pueden usar la dirección de `git clone` directamente
![image](../images/guias/intellij_proyecto7.png)

## Cuestiones de uso

:::{warning}
En Windows 10 y 11, el antivirus interfiere con la operación normal del IDE, el mismo se puede encargar de resolverlo. Aunque con el tamaño de los proyectos que manejaremos no será un problema.
:::

![Configuración de antivirus](../images/guias/intellij_antivirus.png)