---
title: "Patrón Adapter"
subtitle: "Compatibilidad entre interfaces incompatibles"
subject: Patrones de Diseño Estructurales
---

(patron-adapter)=
# Adapter: Puente Incompatible

El patrón **Adapter** (también conocido como **Wrapper**) permite que clases con interfaces incompatibles trabajen juntas, convirtiendo la interfaz de una clase en otra que el cliente espera.

:::{admonition} Propósito
:class: note

Permitir que objetos con interfaces incompatibles colaboren, adaptando una interfaz existente a otra que el cliente requiere.
:::

---

## Concepto

El adapter actúa como un "traductor" entre dos interfaces:

- **Cliente**: Espera una interfaz específica
- **Adaptada**: Proporciona funcionalidad útil pero interfaz diferente
- **Adapter**: Traduce llamadas del cliente a llamadas de la adaptada

---

## Problema

```java
// Cliente espera esta interfaz
public interface TargetaConector {
    void conectar();
    void desconectar();
}

// Dispositivo antiguo con interfaz diferente
public class DispositivoAntiguoEuropeo {
    public void enchufar() {
        System.out.println("Enchufado con conector europeo (2 pines)");
    }
    
    public void desenchufar() {
        System.out.println("Desenchufado desde conector europeo");
    }
}

// ❌ Problema: No puedo usar directamente
TargetaConector conector = new DispositivoAntiguoEuropeo();  // Error de compilación!
```

---

## Solución: Adapter

```java
/**
 * Adapter que convierte DispositivoAntiguoEuropeo 
 * a la interfaz TargetaConector.
 */
public class AdaptadorEuropaAmerica implements TargetaConector {
    private DispositivoAntiguoEuropeo dispositivo;
    
    public AdaptadorEuropaAmerica(DispositivoAntiguoEuropeo dispositivo) {
        this.dispositivo = dispositivo;
    }
    
    @Override
    public void conectar() {
        // Traduce llamada del cliente a interfaz del adaptado
        dispositivo.enchufar();
    }
    
    @Override
    public void desconectar() {
        dispositivo.desenchufar();
    }
}

// ✅ Ahora funciona
DispositivoAntiguoEuropeo tv = new DispositivoAntiguoEuropeo();
TargetaConector conector = new AdaptadorEuropaAmerica(tv);
conector.conectar();      // Funciona como se esperaba
conector.desconectar();
```

---

## Diagrama UML

```
┌─────────────────┐
│    Cliente      │
│      main()     │
└────────┬────────┘
         │ usa
         │
         ▼
┌──────────────────────────┐
│  TargetaConector         │────┐
├──────────────────────────┤    │
│ + conectar()             │    │ implementa
│ + desconectar()          │    │
└──────────────────────────┘    │
         ▲                       │
         │                       │
         │ adapta a      ┌───────┴─────────────────────┐
         │              │                              │
         │              │                  ┌──────────────────┐
         │              │                  │    Adapter       │
         │              │     ┌────────────┤AdaptadorEuroAmer│
         │              │     │            │─────────────────┤
         │              │     │            │+ conectar()      │
         │              └─────┤            │+ desconectar()   │
    ┌────┴──────────────┐     │            │─────────────────┤
    │ Interfaz esperada │     │            │- dispositivo    │
    │ (TargetaConector) │     │            └──────────────────┘
    └───────────────────┘     │                      │
                              │ contiene            │
                              │                      ▼
                              │            ┌───────────────────────┐
                              │            │ DispositivoAntiguo    │
                              └───────────▶│ EuropeoLinea         │
                                           ├───────────────────────┤
                                           │ + enchufar()          │
                                           │ + desenchufar()       │
                                           └───────────────────────┘
                                           Clase existente (no modificable)
```

---

## Variantes

### Adapter de Clase (Herencia)

```java
/**
 * En lugar de composición, usa herencia.
 * (Menos flexible que composición)
 */
public class AdaptadorPorHerencia extends DispositivoAntiguoEuropeo 
    implements TargetaConector {
    
    @Override
    public void conectar() {
        this.enchufar();
    }
    
    @Override
    public void desconectar() {
        this.desenchufar();
    }
}
```

### Adapter Bidi (Bidireccional)

```java
/**
 * Permite adaptar en ambas direcciones.
 */
public class AdaptadorBidireccional implements TargetaConector, 
    InterfazAntigua {
    private DispositivoAntiguoEuropeo antiguo;
    private DispositivoNuevoAmericano nuevo;
    
    public AdaptadorBidireccional(DispositivoAntiguoEuropeo antiguo) {
        this.antiguo = antiguo;
    }
    
    @Override
    public void conectar() {
        antiguo.enchufar();
    }
    
    // ... otros métodos
}
```

---

## Ejemplo Completo: Integración de Sistemas

```java
// Interfaz que espera el cliente (nuevo sistema)
public interface SistemaNotificacionModerno {
    void enviarNotificacion(String mensaje);
}

// Sistema antiguo que no queremos modificar
public class SistemaNotificacionAntiguoFax {
    public void enviarFax(String numero, String contenido) {
        System.out.println("Fax enviado a " + numero + ": " + contenido);
    }
}

// Adapter que traduce notificación moderna a fax antiguo
public class AdaptadorNotificacionAFax implements SistemaNotificacionModerno {
    private SistemaNotificacionAntiguoFax sistemaFax;
    private String numeroFaxPorDefecto;
    
    public AdaptadorNotificacionAFax(SistemaNotificacionAntiguoFax sistemaFax, 
                                      String numeroFax) {
        this.sistemaFax = sistemaFax;
        this.numeroFaxPorDefecto = numeroFax;
    }
    
    @Override
    public void enviarNotificacion(String mensaje) {
        // Traduce la interfaz moderna a la antigua
        sistemaFax.enviarFax(numeroFaxPorDefecto, mensaje);
    }
}

// Cliente que usa la interfaz moderna
public class CentroNotificaciones {
    private SistemaNotificacionModerno sistema;
    
    public CentroNotificaciones(SistemaNotificacionModerno sistema) {
        this.sistema = sistema;
    }
    
    public void notificar(String mensaje) {
        sistema.enviarNotificacion(mensaje);
    }
}

// Uso
SistemaNotificacionAntiguoFax fax = new SistemaNotificacionAntiguoFax();
SistemaNotificacionModerno adaptado = new AdaptadorNotificacionAFax(fax, "+549111234567");
CentroNotificaciones centro = new CentroNotificaciones(adaptado);

centro.notificar("Alerta de seguridad");  // Internamente usa fax
// Salida: Fax enviado a +549111234567: Alerta de seguridad
```

---

## Ventajas y Desventajas

### ✅ Ventajas

- **Reutilización**: Usa código existente sin modificarlo
- **Separación**: Desvincula cliente del código adaptado
- **Flexibilidad**: Agregar nuevos adapters sin cambiar código existente
- **Single Responsibility**: Adapter solo hace traducción

### ❌ Desventajas

- **Complejidad**: Introduce clases adicionales
- **Indirección**: Capa extra de llamadas
- **Performance**: Overhead de traducción
- **Confusión**: Muchos adapters pueden ser confusos

---

## Cuándo Usarlo

✅ **Usa Adapter cuando:**
- Tienes código legacy que no puedes modificar
- Necesitas integrar bibliotecas de terceros
- Dos interfaces incompatibles necesitan trabajar juntas
- Quieres mantener separación de concerns

❌ **Evita cuando:**
- Puedes refactorizar la interfaz original
- La adaptación es trivial
- Hay múltiples niveles de adaptación

---

## Comparación con Patrones Similares

| Patrón | Propósito | Cuando |
| :--- | :--- | :--- |
| **Adapter** | Hacer compatibles interfaces incompatibles | Integrar existentes |
| **Bridge** | Desacoplar abstracción de implementación | Múltiples dimensiones |
| **Decorator** | Agregar responsabilidades dinámicamente | Extender comportamiento |
| **Facade** | Simplificar interfaz compleja | Sistemas complejos |

---

## Ejercicio

```{exercise}
:label: ej-adapter-formatos

Crea un adapter que permita usar un analizador JSON (existente) donde se espera un analizador XML:

1. Interfaz `ParserXML` con `parse(String): Document`
2. Clase `ParserJSON` existente (no modificar) con `analizar(String): JSONObject`
3. Adapter que traduce entre ellos
4. Prueba con diferentes datos
```

```{solution} ej-adapter-formatos
:class: dropdown

```java
// Interfaz que el cliente espera
public interface ParserXML {
    Document parse(String xmlString) throws Exception;
}

// Interfaz de conversión auxiliar
public interface Document {
    String getRootElement();
}

// Clase existente que NO queremos modificar
public class ParserJSON {
    public JSONObject analizar(String json) {
        // Simula análisis de JSON
        return new JSONObject(json);
    }
}

// Clase auxiliar para almacenar resultado
public class JSONObject {
    private String contenido;
    
    public JSONObject(String contenido) {
        this.contenido = contenido;
    }
    
    public String getContenido() {
        return contenido;
    }
}

// Documento resultante
public class XMLDocument implements Document {
    private String root;
    
    public XMLDocument(String root) {
        this.root = root;
    }
    
    @Override
    public String getRootElement() {
        return root;
    }
}

// ADAPTER: Traduce JSON a XML
public class AdaptadorJSONaXML implements ParserXML {
    private ParserJSON parserJSON;
    
    public AdaptadorJSONaXML(ParserJSON parserJSON) {
        this.parserJSON = parserJSON;
    }
    
    @Override
    public Document parse(String xmlString) throws Exception {
        // Convertir XML a JSON
        String json = convertirXMLaJSON(xmlString);
        
        // Usar el parser JSON
        JSONObject objeto = parserJSON.analizar(json);
        
        // Convertir resultado de vuelta a Document
        return new XMLDocument(objeto.getContenido());
    }
    
    private String convertirXMLaJSON(String xml) {
        // Conversión simplificada
        if (xml.contains("<usuario>")) {
            return "{\"usuario\": \"datos\"}";
        }
        return "{}";
    }
}

// Cliente que usa la interfaz XML
public class ClienteXML {
    private ParserXML parser;
    
    public ClienteXML(ParserXML parser) {
        this.parser = parser;
    }
    
    public void procesar(String xml) throws Exception {
        Document doc = parser.parse(xml);
        System.out.println("Procesado: " + doc.getRootElement());
    }
}

// Pruebas
public class PruebaAdapter {
    public static void main(String[] args) throws Exception {
        ParserJSON parserJSON = new ParserJSON();
        ParserXML parserXML = new AdaptadorJSONaXML(parserJSON);
        
        ClienteXML cliente = new ClienteXML(parserXML);
        cliente.procesar("<usuario><nombre>Juan</nombre></usuario>");
        
        // Salida: Procesado: {"usuario": "datos"}
    }
}
```
```

