---
title: 0x6 - Restricciones sobre Programación Funcional
---

# Serie 0x6 - Restricciones sobre Programación Funcional

:::{note}
**Nota pedagógica**: Estas reglas están diseñadas para el aprendizaje de POO imperativa clásica. La programación funcional es valiosa, pero se enseñará en contextos apropiados más adelante.
:::

(regla-0x6000)=
## `0x6000` - No usar expresiones lambda

### Explicación

Las expresiones lambda (`->`) no están permitidas en este curso.

**Incorrecto** ❌:
```java
lista.forEach(elemento -> System.out.println(elemento));
numeros.stream().filter(n -> n > 10).collect(Collectors.toList());
```

**Correcto** ✅:
```java
for (String elemento : lista) {
    System.out.println(elemento);
}

List<Integer> mayoresADiez = new ArrayList<>();
for (Integer n : numeros) {
    if (n > 10) {
        mayoresADiez.add(n);
    }
}
```

(regla-0x6001)=
## `0x6001` - No usar referencias a métodos (method references)

### Explicación

Las referencias a métodos (`::`) no están permitidas.

**Incorrecto** ❌:
```java
lista.forEach(System.out::println);
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

(regla-0x6002)=
## `0x6002` - No usar la API de Streams

### Explicación

La API `java.util.stream` no está permitida en este curso. Usar bucles explícitos.

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

(regla-0x6004)=
## `0x6004` - No usar métodos funcionales de colecciones

### Explicación

Métodos como `forEach()`, `removeIf()`, `replaceAll()` no están permitidos. Usar iteradores o bucles.

**Correcto** ✅:
```java
Iterator<String> it = nombres.iterator();
while (it.hasNext()) {
    if (it.next().startsWith("A")) {
        it.remove();
    }
}
```

(regla-0x6005)=
## `0x6005` - No usar `Collectors` ni operaciones de reducción

### Explicación

Usar bucles explícitos para agregaciones y transformaciones.

(regla-0x6006)=
## `0x6006` - Preferir bucles `for` tradicionales o enhanced sobre operaciones funcionales

### Explicación

Los bucles son más explícitos y pedagógicos para entender el flujo de control.

(regla-0x6007)=
## `0x6007` - No usar el patrón de composición funcional

### Explicación

Evitar encadenar operaciones funcionales. Usar métodos y asignaciones explícitas.
