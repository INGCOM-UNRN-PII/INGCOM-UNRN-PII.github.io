(referencia-conceptos-poo)=
# Conceptos y Frases Clave de la POO

En el diseño orientado a objetos, existen frases, acrónimos y principios que actúan como "brújulas" para escribir código de calidad. Esta referencia resume los más importantes utilizados en la cátedra.

---

## 1. Tell, Don't Ask (Decile, no le preguntes)

Este es el principio fundamental del **Encapsulamiento**. Establece que no deberías pedirle datos a un objeto para tomar una decisión por él, sino que deberías ordenarle al objeto que realice la acción.

- **Mal (Ask):** Pedir el saldo, verificar si alcanza, y luego setear el nuevo saldo desde afuera.
- **Bien (Tell):** Decirle a la cuenta `extraer(monto)`. La cuenta conoce su saldo y decide si puede o no.

> *"No pidas datos para decidir, decile al objeto qué hacer con sus datos."*

---

## 2. Principio Hollywood

> *"No nos llames, nosotros te llamamos."*

Este principio se refiere a la **Inversión de Control**. En lugar de que tu código tenga el control total y llame a las librerías, vos proporcionás el comportamiento (clases, métodos) y el "marco" (framework) se encarga de llamarlo cuando sea necesario.

Es muy común en el uso de **Interfaces** y **Patrones de Diseño** como el *Template Method* o *Strategy*.

---

## 3. SOLID

Acrónimo de los cinco principios fundamentales de diseño orientado a objetos definidos por Robert C. Martin:

1. **S - Single Responsibility (Responsabilidad Única):** Una clase debe tener una sola razón para cambiar.
2. **O - Open/Closed (Abierto/Cerrado):** Las clases deben estar abiertas para su extensión pero cerradas para su modificación.
3. **L - Liskov Substitution (Sustitución de Liskov):** Las subclases deben ser sustituibles por sus clases base sin romper el programa.
4. **I - Interface Segregation (Segregación de Interfaces):** Es mejor tener muchas interfaces específicas que una sola interfaz general "gorda".
5. **D - Dependency Inversion (Inversión de Dependencias):** Depender de abstracciones (interfaces), no de implementaciones concretas.

Consultá [../parte_2/09_oop_solid](.\/../parte_2/09_oop_solid.md) para una explicación detallada.

---

## 4. DRY (Don't Repeat Yourself)

> *"No te repitas."*

Toda pieza de conocimiento o lógica debe tener una representación única y autoritativa dentro del sistema. Si tenés el mismo código en dos lugares, cualquier cambio futuro requiere actualizar ambos, lo que genera errores. 

**Solución:** Abstraer la lógica común en un método, clase o componente reutilizable.

---

## 5. KISS (Keep It Simple, Stupid)

> *"Mantenelo simple, tonto."*

La mayoría de los sistemas funcionan mejor si se mantienen simples en lugar de hacerlos complejos. La complejidad innecesaria (sobre-ingeniería) dificulta el mantenimiento, el testing y la comprensión del código.

---

## 6. YAGNI (You Ain't Gonna Need It)

> *"No lo vas a necesitar."*

No agregues funcionalidad hasta que sea realmente necesaria. Muchas veces los programadores intentamos "predecir" el futuro y agregamos abstracciones o características que terminan sin usarse, pero que agregan costo de mantenimiento.

---

## 7. Ley de Demeter (Principio de Menor Conocimiento)

> *"Solo hablá con tus amigos inmediatos."*

Un objeto no debería "navegar" a través de otros objetos para llegar a uno lejano. 
- **Mal:** `pedido.getCliente().getDireccion().getCiudad()` (Estás acoplando `Pedido` con la estructura interna de `Cliente` y `Direccion`).
- **Bien:** `pedido.obtenerCiudadDeEntrega()`.

---

## 8. Composición sobre Herencia

> *"Favor Composition Over Inheritance."*

Es preferible construir objetos complejos combinando objetos simples (**tiene-un**) en lugar de crear jerarquías de herencia profundas (**es-un**). La composición es más flexible, menos acoplada y más fácil de cambiar en tiempo de ejecución.

Consultá [../parte_2/04_oop_herencia_polimorfismo](.\/../parte_2/04_oop_herencia_polimorfismo.md) para la comparativa completa.

---

## 9. GRASP (General Responsibility Assignment Software Patterns)

Conjunto de patrones que ayudan a decidir **qué responsabilidad asignar a qué clase**. Los más comunes son:
- **Experto:** Asignar la responsabilidad a la clase que tiene la información necesaria.
- **Creador:** Quién debe ser el responsable de crear una instancia de un objeto.
- **Bajo Acoplamiento:** Mantener las dependencias al mínimo.
- **Alta Cohesión:** Que las clases hagan una sola cosa y la hagan bien.
