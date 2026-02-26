Diseña e implementa en Java una clase llamada `Hora` que represente y manipule unidades de tiempo en formato
sexagesimal (horas, minutos, segundos).

### Representación Interna:

Utiliza tres atributos privados de tipo `short` para almacenar los minutos y los segundos, con las horas como tipo `int`.
Asegúrate de que los valores de minutos y segundos se mantengan dentro del rango válido (0-59).

Cuando se reciban segundos exclusivamente, utiliza tipo `long`.

La clase debe ser construida para funcionar de manera inmutable.

### Excepciones
Creen como mínimo una excepción (`HoraExcepcion`) para agrupar los potenciales problemas de los métodos de esta clase.

### Constructores:

- Implementa los siguientes constructores:
    - Un constructor predeterminado que inicialice la hora a `00:00:00`.
    - Un constructor que reciba las horas, los minutos y los segundos como argumentos enteros. Realiza la validación
      necesaria para asegurar que los minutos y segundos estén dentro del rango correcto. Si se proporcionan valores
      inválidos, lanza una excepción.
    - Un constructor que reciba la hora total en segundos como argumento.

### Método `toString()`

Implementa el método `toString()` para que devuelva una representación legible de la hora en formato "HH:MM:SS" 
(por ejemplo, "09:30:45").

### Método de Comparación (`compareTo()`):

- Implementa un método `compareTo(Hora otraHora)` que compare la instancia actual de `Hora` con otra instancia (
  `otraHora`). El método debe retornar:
    - Un valor negativo si la hora actual es menor que `otraHora`.
    - Cero si las dos horas son iguales.
    - Un valor positivo si la hora actual es mayor que `otraHora`.

### Métodos de Operaciones Aritméticas:

- Implementa los siguientes métodos:
    - `sumar(Hora otraHora)`: Retorna una nueva instancia de `Hora` que representa la suma de la hora actual y
      `otraHora`. Asegúrate de manejar correctamente el acarreo de segundos a minutos y de minutos a horas.
    - `restar(Hora otraHora)`: Retorna una nueva instancia de `Hora` que representa la resta de `otraHora` a la hora
      actual. Considera cómo manejar el caso en que la hora actual es menor que `otraHora` (podrías retornar una nueva
      hora con valores negativos o lanzar una excepción, documentando claramente la elección).

### Métodos de Conversión:

- `aSegundos()`: Retorna un valor entero que representa la hora actual convertida a la cantidad total de segundos.
- `desdeSegundos(int totalSegundos)`: Un método estático que recibe una cantidad total de segundos como argumento y
  retorna una nueva instancia de `Hora` correspondiente. Asegúrate de manejar correctamente la conversión a horas,
  minutos y segundos.

### Métodos `equals()` y `hashCode()`:

- Implementa correctamente los métodos `equals(Object otroObjeto)` y `hashCode()` para permitir la comparación
  significativa de objetos `Hora` y su uso en colecciones basadas en hash (como `HashSet` o `HashMap`). Dos objetos
  `Hora` se consideran iguales si tienen los mismos valores de horas, minutos y segundos.

### Guía de tests

    - La correcta inicialización de los objetos `Hora` utilizando todos los constructores.
    - La validación de los rangos de minutos y segundos en el constructor.
    - La correcta representación de la hora utilizando el método `toString()` (incluyendo posibles sobrecargas).
    - Todas las combinaciones posibles de comparación en el método `compareTo()` (menor, mayor, igual).
    - Casos de prueba para la suma, incluyendo acarreos de segundos a minutos y de minutos a horas.
    - Casos de prueba para la resta, incluyendo escenarios donde se necesita "prestar" de unidades superiores.
    - La correcta conversión de horas a segundos y viceversa, incluyendo casos con valores cero y valores grandes.
    - La correcta implementación de `equals()` y `hashCode()`, verificando la igualdad de objetos con los mismos valores
      y la desigualdad con valores diferentes. También prueba la consistencia entre `equals()` y `hashCode()`.

### Opcionales

- Podrías considerar la implementación de métodos para sumar o restar una cantidad específica de segundos, minutos u
  horas directamente a un objeto `Hora`.
- Implementa una sobrecarga del método `toString()` para ofrecer formatos alternativos si se considera útil.
  (por ejemplo, incluyendo solo horas y minutos).