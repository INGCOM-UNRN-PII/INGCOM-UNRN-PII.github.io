---
title: "Patrón Bridge"
subtitle: "Desacoplar abstracción de implementación"
subject: Patrones de Diseño Estructurales
---

(patron-bridge)=
# Bridge: Separar Abstracción e Implementación

Desacopla una abstracción de su implementación para que puedan variar independientemente.

:::{admonition} Propósito
:class: note

Permitir que abstracción e implementación varíen de manera independiente.
:::

## Ejemplo

```java
interface Renderer {
    void renderizar();
}

abstract class Forma {
    protected Renderer renderer;
    
    public Forma(Renderer renderer) {
        this.renderer = renderer;
    }
    
    abstract void dibujar();
}

class Circulo extends Forma {
    @Override
    public void dibujar() {
        System.out.println("Dibujando círculo");
        renderer.renderizar();
    }
}
```
