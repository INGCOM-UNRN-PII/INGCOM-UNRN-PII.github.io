---
title: "TP9 - Agenda"
description: Trabajo práctico sobre gestión de contactos con orientación a objetos.
---

# TP9 - Agenda

## Forma de entrega

:::{important}
- No olviden completar la plantilla con sus datos y agregar la descripción de cada función.
  Aunque `main` puede no tener este comentario, no está de más registrar qué es lo que el
  `Scanner` recibe.
- Siempre que sea posible, los mensajes de commit deben ser descriptivos.
- Implementen un main que haga un uso de las funciones implementadas, pueden utilizar un `Scanner`.
- La entrada y salida debe estar separada de la función que cumple la consigna, salvo que la consigna lo pida.
- No olviden la utilización de auto-formato, las herramientas de corrección le prestan atención
  a este tema.
- Puede ser necesario completar, cambiar y ajustar la documentación de las funciones pedidas.
- Es posible hacer cambios en la forma de las funciones a implementar, pero para esto, indíquenlo explícitamente
  en el comentario de documentación.
- No olviden crear Tests.
:::

:::{tip}
Pueden usar los atajos [SOS](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas&title=TP9%20-%20)
para crear preguntas sobre los enunciados individuales, si es posible, no fusionen las preguntas, así es más fácil que
sus compañeros encuentren las respuestas, y pueden otorgarles más puntos a quienes responden.
:::

## Consideraciones generales

Utilicen el paquete base `ar.unrn.agenda`.

:::{warning}
No está permitido el uso de métodos de tipo `getter`/`setter` para ninguna de las clases.

Recuerden no utilizar lo referido a programación funcional, la sintaxis no la vimos y consiste en usar cosas como
`->` y `::`.
:::

No olviden crear un `main` que permita el uso de la `Agenda`.

### `Contacto`

Representa un contacto inmutable e individual en la agenda, representado por los siguientes atributos:
Nombre, Apellido, dirección de email, fecha de nacimiento y fecha de último contacto.

:::{tip}
Para la fecha de nacimiento pueden usar `java.util.LocalDate`.
:::

Comportamiento a implementar:

- Constructor parametrizado.
- Igualdad y `hashCode`
- Override de `toString` y una sobrecarga con opciones de formato.
- `Comparable` con un criterio base de ordenamiento.
- Un `Comparator` por cada atributo, llamado por ejemplo `porApellido`.
- Un método estático para crear un `Contacto` aleatorio.

### Clase `Agenda`

Gestiona la colección de objetos `Contacto`, opcionalmente utilizando los `Arreglo` creados en el TP anterior.

- Atributos:
    - El conjunto de `Contacto`, en la lista de comportamiento se indica `java.util.List`, pero puede ser algún
      `Arreglo`
    - El propietario (como `Contacto`)

- Comportamiento:
    * `public Agenda()`: Constructor que inicializa la lista interna de contactos (e.g.,
      `this.contactos = new java.util.ArrayList<>();`).
    * `public void agregarContacto(Contacto contacto)`: Añade un `Contacto` a la lista. Debería validar que el
      `Contacto` no sea nulo y que no tenga duplicados.
    * `public boolean eliminarContacto(String email)`: Elimina el `Contacto` cuyo email coincide con el dado. Devuelve
      `true` si se encontró y eliminó, `false` en caso contrario.
    * `public java.util.List<Contacto> buscarContactoPorNombre(String nombre)`: Busca y devuelve una lista de `Contacto`
      s que coinciden con el nombre dado. Puede haber múltiples contactos con el mismo nombre.
    * `public Contacto buscarContactoPorEmail(String email)`: Busca y devuelve el `Contacto` cuyo email coincide con
      el dado.
    * Sin alterar el orden interno de la agenda:
        * `public java.util.List<Contacto> listarContactosOrdenadosPorNombreApellido()`
        * `public java.util.List<Contacto> listarContactosOrdenadosPorApellidoNombre()`
        * `public java.util.List<Contacto> listarContactosOrdenadosPorFechaDeNacimiento()`
    * `public int cantidadTotalDeContactos()`: Retorna el número de contactos en la agenda (`contactos.size()`).
    * `public boolean verificarExistenciaDeContacto(String email)`: Verifica si existe un contacto con el email dado.
    * `public void limpiarAgenda()`: Elimina todos los contactos de la lista (`contactos.clear()`).

### Opcionales

:::{note}
En ambos, sin duplicar los `Contacto`.
:::

#### Implementar una clasificación de contactos

Esta representa una categoría o tipo para un contacto (ej: "Personal", "Trabajo", "Familia").

La `Agenda` debe de poder crear conjuntos agrupados por estas categorías.

#### Implementar `GrupoContactos`

Que representa una colección nombrada de contactos dentro de la agenda (ej: "Amigos de la infancia", "Equipo de
proyecto").

Implementar `agregar` / `quitar` / `contiene`

#### Almacenamiento persistente

:::{warning}
Sin utilizar `getter`s/`setter`s o Persistencia de Java.
:::

Guarden a disco la `Agenda` de forma que la información pueda ser accedida luego de que el programa se cierra.