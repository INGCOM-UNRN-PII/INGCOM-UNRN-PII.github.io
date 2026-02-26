---
title: "TP 1 - Verificación y primeros pasos"
description: Trabajo Práctico para verificar la instalación de las herramientas y que todo esté funcionando.
---

# TP 1 - Verificación y primeros pasos

Este Trabajo Práctico es simple, está para verificar la instalación
de las herramientas y que todo esté funcionando.

Aunque a esta altura `git` ya debiera de estar instalado, configurado y andando,
les dejé los apuntes en el repositorio de la [cursada](https://dub.sh/p2/cursada).

:::{important}
Es muy importante seguir los pasos que están en el Wiki, primero para el JDK
y luego IntelliJ.
:::

Tengan en cuenta que gran parte de la verificación en este TP es automática y esto
requiere que los archivos tengan exactamente el nombre y ubicación indicado. Ya que
de otra forma, tendrán que corregirlo.

## Botones SOS

:::{tip} Cómo hacer preguntas
Pueden usar los atajos [SOS](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP1%20-%20)
para crear preguntas sobre los enunciados individuales, si es posible, no fusionen las preguntas, así es más fácil que
sus compañeros encuentren las respuestas, y pueden otorgarles más puntos a quienes responden.

Estos enlaces precargan el número del práctico y encabezado del enunciado, para que agreguen a continuación 
una síntesis de su pregunta y luego desarrollen la pregunta en el cuerpo de la publicación.

Estos enlaces estarán presentes en todos los trabajos prácticos, de forma que faciliten el uso
del espacio Discussions y se saquen todas las dudas.

Al hacer preguntas, entre más información agreguen mejor, no siempre estamos cerca de una computadora y si es posible
contestar desde el celular, lo haremos. O les pediremos que amplíen y agreguen información. No tenemos las consignas
de memoria, por lo que agregarla al preguntar es una buena idea.
:::

## La estructura de directorios

:::{warning}
Es muy importante que la estructura que contiene el repositorio no cambie.
Veremos las razones en clases, y está relacionado a cómo funciona gradle.
:::

La ubicación para agregar nuevos archivos de código Java, es
`/src/main/java/ar/unrn/`. Cualquier otra ubicación no funcionará correctamente.

Asimismo, los archivos que contengan un `main` deben terminar en `App` para
distinguirlos de futuros archivos.

Y parecido a esto, se encuentra la ubicación `/src/test/java/ar/unrn/`, en
donde se deben de alojar los archivos de pruebas.

Asimismo, todos los archivos `.md`, deben estar alojados en la carpeta `informes`
que se encuentra en la raíz.

(tp1-ejercicio1)=
```{exercise} Ejercicio 1
:label: tp1-ej1

Agregar un archivo markdown llamado [`informes/java.md`](informes/java.md) a la
raíz del repositorio conteniendo la salida de `java --version` que obtengan en
la terminal.

Una forma simple es emplear una redirección:

```bash
java --version > java.md
```

:::{note}
Sé que es probable que no estén muy familiarizados con la terminal, por lo
que espero que abran hilo si se encuentran con dificultades en este paso cosa
de explicarlo, en el repositorio de la cursada hay material al respecto.
:::
```

[SOS Ejercicio 1](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP1%20-%20Ejercicio%201)

(tp1-ejercicio2)=
```{exercise} Ejercicio 2 - HolaApp
:label: tp1-ej2

1. Ejecuten `HolaApp`.
2. En un archivo llamado [`informes/HolaApp.md`](informes/HolaApp.md), guarden la salida del programa cuando:
   1. Pasan palabras.
   2. Pasan números.
   3. Pasan emojis.

Utilicen encabezados para indicar qué le indicaron al programa.

En la terminal, verán `Ingresa un número, y probá otros valores: `.

Estructura sugerida del informe:

```markdown
# HolaApp

Que pasa cuando le pasamos...

### Texto

Esta es la salida cuando le pasamos texto al programa

### Números

Esta es la salida cuando le pasamos números al programa

### Emojis

Esta es la salida cuando le pasamos emojis al programa
```

Tengan presente qué forma parte del programa en sí, y qué del gradle (que se encarga
de ejecutar el programa).
```

[SOS Ejercicio 2](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP1%20-%20Ejercicio%202)

(tp1-ejercicio3)=
```{exercise} Ejercicio 3
:label: tp1-ej3

1. Ejecuten el archivo
2. Utilicen el auto-formato con el archivo `Desordenado.java`.
3. Vuelvan a ejecutar el archivo

Vean cómo antes y después, el código que contiene, es entendido tanto por el entorno
como por Java.
```

[SOS Ejercicio 3](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP1%20-%20Ejercicio%203)

(tp1-ejercicio4)=
```{exercise} Ejercicio 4
:label: tp1-ej4

Ejecuten los tests con los chequeos y guarden solo los informes html en el repositorio.
Los mismos se encuentran en el directorio `build/reports/*`.

:::{note}
Esto no es necesario en futuras prácticas, ya que las herramientas de verificación los
reemplazarán. Es para que sepan dónde buscar los reportes que deben de tener en cuenta.
:::
```

[SOS Ejercicio 4](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP1%20-%20Ejercicio%204)

(tp1-ejercicio5)=
```{exercise} Ejercicio 5
:label: tp1-ej5

Redacten un informe simple indicando qué similitudes ven con respecto a C
sobre el archivo `Desordenado.java`, incluyan en el informe qué otras estructuras
de C funcionan en Java y cuáles que probaron que no funcionaron.

Este archivo se debe llamar [`informes/similitudes.md`](informes/similitudes.md) y
estar ubicado el directorio indicado del repositorio.
```

[SOS Ejercicio 5](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP1%20-%20Ejercicio%205)

### Aspectos a tener en cuenta

#### Sintaxis general

Comparen la estructura de declaraciones, expresiones, operadores y tipos de datos básicos
en ambos lenguajes. ¿Qué elementos son prácticamente idénticos?

¿Funciona el preprocesador? ¿Hay punteros?

#### Estructuras de control

Analicen cómo se implementan lazos (`for`, `while`, `do-while`), condicionales (`if`, `else`, `switch`)
y otras estructuras de control en Java. ¿Hay diferencias significativas en su uso?

#### Funciones

Investiguen cómo se definen y llaman funciones en C y métodos en Java. ¿Cuál es la sintaxis y
cómo se manejan los parámetros y valores de retorno?

#### Bibliotecas estándar

Exploren las bibliotecas estándar de ambos lenguajes. ¿Qué funciones comunes encuentran
(por ejemplo, para entrada/salida, manipulación de cadenas, matemáticas)?
¿Hay diferencias en sus nombres o formas de uso?

:::{seealso}
- [Librería C](https://cplusplus.com/reference/clibrary/)
- [Librería Java](https://docs.oracle.com/en/java/javase/17/docs/api/index.html)
:::
