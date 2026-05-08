---
title: 0x6 - Restricciones sobre Programación Funcional
---

# Serie 0x6 - Restricciones sobre Programación Funcional

:::{note}
**Nota pedagógica**: Estas reglas están diseñadas para asegurar el dominio de la POO imperativa clásica, los algoritmos fundamentales y el control de flujo manual. No son una condena a la programación funcional, que es extremadamente valiosa y el estándar de la industria. Estas restricciones aplican exclusivamente a la etapa inicial de la carrera: una vez consolidadas las bases de la orientación a objetos, estos recursos funcionales se reintroducirán en materias posteriores para el diseño avanzado, procesamiento de datos masivos y programación concurrente.
:::

(regla-0x6000)=
## `0x6000` - No usar expresiones lambda ni referencias a métodos

### Explicación

Las expresiones lambda (`->`) y las referencias a métodos (`::`) no están permitidas en esta materia. Esta restricción te obliga a internalizar la sintaxis y el control de flujo explícito de Java.

**Incorrecto** ❌:
```java
lista.forEach(elemento -> System.out.println(elemento));
nombres.stream().map(String::toUpperCase).collect(Collectors.toList());
```

**Correcto** ✅:
```java
for (String elemento : lista) {
    System.out.println(elemento);
}

List<String> nombresMayusculas = new ArrayList<>();
for (String nombre : nombres) {
    nombresMayusculas.add(nombre.toUpperCase());
}
```

:::{importan} En tests
Esta regla no aplica a los tests, ya que `assertThrows` es sumamente mas compacta y expresiva.
:::

(regla-0x6001)=
## `0x6001` - No usar la API de Streams ni `Collectors`

### Explicación

La API `java.util.stream` y sus operaciones de reducción (`Collectors`, `.sum()`, etc.) abstraen los bucles. En esta etapa, es necesario usar bucles explícitos (`for`, `while`, `for-each`) para manejar el estado de las variables y comprender algorítmicamente las transformaciones de datos.

**Incorrecto** ❌:
```java
int suma = numeros.stream()
    .filter(n -> n % 2 == 0)
    .mapToInt(Integer::intValue)
    .sum();
```

**Correcto** ✅:
```java
int suma = 0;
for (Integer numero : numeros) {
    if (numero % 2 == 0) {
        suma = suma + numero;
    }
}
```

(regla-0x6002)=
## `0x6002` - No usar métodos funcionales de colecciones ni encadenamiento

### Explicación

Se agrupan aquí restricciones sobre métodos funcionales preexistentes en las colecciones de Java, tales como `forEach()`, `removeIf()`, `replaceAll()`, y el patrón de composición funcional o encadenamiento. Se deben usar iteradores explícitos o bucles tradicionales. Esto asegura la comprensión de cómo y en qué momento muta el estado.

**Incorrecto** ❌:
```java
nombres.removeIf(n -> n.startsWith("A"));
```

**Correcto** ✅:
```java
Iterator<String> it = nombres.iterator();
while (it.hasNext()) {
    if (it.next().startsWith("A")) {
        it.remove();
    }
}
```
