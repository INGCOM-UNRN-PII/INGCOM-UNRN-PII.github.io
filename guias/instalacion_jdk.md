# Instalación paso a paso JDK

## Descarga
Durante la cursada, utilizaremos la versión del [JDK 21](https://download.oracle.com/java/21/latest/jdk-21_windows-x64_bin.exe),
la misma es una versión de [soporte extendido (LTS)](https://www.oracle.com/java/technologies/java-se-support-roadmap.html) por lo que es más conservadora en cambios.

Java es un lenguaje en constante cambio, esta forma de liberar al público las mejoras facilita que aquellos
que no puedan tomar el riesgo de probar algo experimental puedan utilizarlo de manera más segura.

Dejando la posibilidad a que los desarrolladores experimenten con las mejoras

## Instalador

### Paso 1
![image](https://github.com/INGCOM-UNRN-PII/cursada-2024/assets/56625/eebe9d30-4a15-46bc-b099-316005a32d44)

### Paso 2
![image](https://github.com/INGCOM-UNRN-PII/cursada-2024/assets/56625/f52f1466-27d0-41dd-b7cc-8a997f11cf83)

### Paso 3
![image](https://github.com/INGCOM-UNRN-PII/cursada-2024/assets/56625/b8858f7c-7f8b-4c3c-b936-a91482280cc4)

### Paso 4
![image](https://github.com/INGCOM-UNRN-PII/cursada-2024/assets/56625/0ffa7726-e57b-4b10-a365-73d679191069)

### Paso 5

En Windows, reiniciar.

En Linux, abrir una nueva terminal.

## Verificación

Ejecuten en una terminal, en donde debiera de leer algo parecido a:

```sh
$> java --version
openjdk 21.0.6 2025-01-21 LTS
OpenJDK Runtime Environment Temurin-21.0.6+7 (build 21.0.6+7-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.6+7 (build 21.0.6+7-LTS, mixed mode, sharing)
```

Si ven "`Command not found`" prueben reiniciar o [abran hilo](https://github.com/orgs/INGCOM-UNRN-PII/discussions/new?category=preguntas-y-respuestas).

De no funcionar, puede que sea necesario agregar manualmente información al `$PATH`. Lo vemos puntualmente de ser necesario.

## Verificación II

Con el archivo `HolaApp.java` a mano

```java
public class HolaApp {
   public static void main(String[] args) {
       System.out.printf("Hola %s!\n", "mundo");
   }
}
```

(Ya vamos a ver que es todo esto)

```sh
$> javac HolaApp.java
$> java HolaApp
```

Esto último **_tiene_** que funcionar, ya que es la base de todo el resto de la materia.
