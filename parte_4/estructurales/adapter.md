---
title: "Patrón Adapter"
subtitle: "Compatibilidad entre interfaces incompatibles"
subject: Patrones de Diseño Estructurales
---

(patron-adapter)=
# Adapter: Puente Incompatible

El patrón **Adapter** permite que clases con interfaces incompatibles trabajen juntas.

:::{admonition} Propósito
:class: note

Permitir que objetos con interfaces incompatibles colaboren, convirtiendo la interfaz de una clase en otra que el cliente espera.
:::

## Ejercicio

```{exercise}
:label: ej-adapter-json-xml

Crea un adapter que convierte un analizador JSON a interfaz XML.
```

```{solution} ej-adapter-json-xml
:class: dropdown

```java
interface AnalizadorXML {
    void parsear(String xml);
}

class AnalizadorJSON {
    public void analizar(String json) {
        System.out.println("Analizando JSON...");
    }
}

public class AdaptadorJSONaXML implements AnalizadorXML {
    private AnalizadorJSON json;
    
    public AdaptadorJSONaXML(AnalizadorJSON json) {
        this.json = json;
    }
    
    @Override
    public void parsear(String xml) {
        json.analizar(convertirXMLaJSON(xml));
    }
    
    private String convertirXMLaJSON(String xml) {
        return "{}";
    }
}
```
```
