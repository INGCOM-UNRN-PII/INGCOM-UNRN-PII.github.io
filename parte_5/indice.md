---
title: "Parte 5: Arquitectura y Principios SOLID"
subject: Arquitectura de Software
---

(parte-5)=
# Parte 5: Arquitectura y Principios SOLID

Transición de patrones de diseño a principios de arquitectura y buenas prácticas para sistemas escalables y mantenibles.

## Temas Planeados

### 1. Principios SOLID

(01-principios-solid)=
#### 1.1 Single Responsibility Principle (SRP)
- Una clase, una razón para cambiar
- Identificar responsabilidades
- Cohesión y separación
- Refactoring para SRP
- Ejemplos prácticos

(01-open-closed)=
#### 1.2 Open/Closed Principle (OCP)
- Abierto para extensión, cerrado para modificación
- Uso de abstracciones
- Polimorfismo aplicado
- Planificación anticipada
- Evitar cascada de cambios

(01-liskov)=
#### 1.3 Liskov Substitution Principle (LSP)
- Subtipos deben ser intercambiables
- Contratos de herencia
- Violaciones comunes
- Precondiciones y postcondiciones
- Testing de sustitutibilidad

(01-interface-segregation)=
#### 1.4 Interface Segregation Principle (ISP)
- Interfaces específicas por cliente
- Evitar interfaces obesas
- Interfaces como contratos precisos
- Role Interfaces vs Header Interfaces
- Splitting de interfaces

(01-dependency-inversion)=
#### 1.5 Dependency Inversion Principle (DIP)
- Depender de abstracciones
- Inversión del control (IoC)
- Inyección de dependencias
- Contenedores IoC
- Testing con DI

---

### 2. Arquitectura en Capas

(02-arquitectura-capas)=
- Modelo tradicional (Presentation, Business, Data)
- Responsabilidades de cada capa
- Patrones por capa:
  - Capa de Presentación: MVC, MVP, MVVM
  - Capa de Negocio: Service, Business Logic
  - Capa de Datos: DAO, Repository, Query Objects
- Comunicación entre capas
- Separación de concerns
- Ventajas y limitaciones

---

### 3. Clean Code Aplicado

(03-clean-code)=
- **Nomenclatura significativa**
  - Nombres de variables
  - Nombres de métodos
  - Nombres de clases
  - Evitar desinformación

- **Funciones limpias**
  - Pequeñas y enfocadas
  - Un nivel de abstracción
  - Nombres descriptivos
  - Pocos parámetros
  - Sin efectos secundarios

- **Comentarios efectivos**
  - Cuándo comentar
  - Comentarios malos
  - Documentación con JavaDoc
  - Código autodescriptivo

- **Manejo de errores**
  - Excepciones chequeadas vs no chequeadas
  - Use earlier, fail faster
  - Try-catch-finally
  - Try-with-resources

- **Complejidad cognitiva**
  - Medir complejidad
  - Reducir nesting
  - Métodos cortos
  - Guardia clauses

---

### 4. Testing Avanzado

(04-testing-avanzado)=
- **Tests unitarios (repaso)**
  - AAA pattern (Arrange-Act-Assert)
  - Aislamiento y mocks
  - Fixtures y builders

- **Tests de integración**
  - Testing con BD (H2, testcontainers)
  - Testing con frameworks
  - Transacciones de prueba

- **Tests end-to-end**
  - API testing
  - Pruebas de flujo completo
  - Data setup y cleanup

- **Mocks y stubs**
  - Mockito
  - Spy vs Mock
  - Stubbing de dependencias
  - Verificación de interacciones

- **Cobertura de tests**
  - Jacoco
  - Cálculo de cobertura
  - Limitaciones de cobertura

- **TDD (Test-Driven Development)**
  - Red-Green-Refactor
  - Ventajas y desafíos
  - Practicando TDD

---

### 5. Refactoring Sistemático

(05-refactoring)=
- **Code smells (Olores de código)**
  - Long methods
  - Duplicate code
  - Large classes
  - Long parameter lists
  - Divergent change
  - Shotgun surgery

- **Técnicas de refactoring**
  - Extract Method
  - Extract Class
  - Inline Method
  - Move Method
  - Rename
  - Simplify Conditional

- **Refactoring seguro**
  - Importancia de tests
  - Cambios pequeños
  - Verificación continua
  - Herramientas de refactoring (IDE)

- **Patrones de refactoring**
  - Hacia patrones conocidos
  - Simplificación
  - Preparación para cambios

---

### 6. Domain-Driven Design (DDD) Introducción

(06-ddd-introduccion)=
- **Ubiquitous Language (Lenguaje Ubicuo)**
  - Comunicación entre técnicos y expertos
  - Diccionario de términos
  - Nombrado en código

- **Building Blocks**
  - Value Objects
  - Entidades
  - Agregados
  - Raíces de agregado

- **Servicios de dominio**
  - Cuándo crear servicios
  - Stateless y puros
  - Separado de aplicación

- **Repositorios**
  - Abstracción de persistencia
  - Métodos de búsqueda
  - Simulación de colecciones

- **Factories**
  - Crear agregados complejos
  - Encapsular lógica de creación
  - Invariantes

- **Bounded Contexts**
  - Múltiples modelos
  - Límites explícitos
  - Context mapping

---

### 7. Métricas de Código y Deuda Técnica

(07-metricas-deuda-tecnica)=
- **Métricas cuantitativas**
  - Complejidad ciclomática
  - Líneas de código (LOC)
  - Acoplamiento
  - Cohesión

- **Herramientas de análisis**
  - SonarQube
  - Checkstyle
  - SpotBugs
  - JaCoCo

- **Deuda técnica**
  - Identificar deuda
  - Cuantificar deuda
  - Priorizar pago
  - Prevenir nueva deuda

- **Mejora continua**
  - Establecer baselines
  - Tracking de métricas
  - Objetivos de calidad

---

## Temas Sugeridos Adicionales

### Análisis de Código Avanzado
- Análisis de dependencias
- Detección de ciclos
- Aislamiento de módulos
- Visualización de arquitectura

### Mantenibilidad
- Versionado semántico
- Documentación técnica
- API contracts
- Backward compatibility
- Deprecación segura
- Release management

### Seguridad en Código
- Principios de seguridad
- Validación de entrada
- Sanitización de datos
- Inyección de código (SQL, XSS)
- Autenticación y autorización
- Cryptografía básica

### Performance y Optimización
- Profiling de aplicaciones
- Identificar cuellos de botella
- Optimización de loops
- Caching strategies
- Lazy loading y eager loading
- Garbage collection (GC tuning)

### Concurrencia Avanzada
- Threads y Thread pools
- Sincronización
- Deadlocks y livelocks
- Executor Framework
- CompletableFuture
- Reactive programming intro

### Gestión de Configuración
- Archivos de propiedades
- Variables de entorno
- Configuración por perfil
- Externalización
- Reloading dinámico

### Logging y Monitoreo
- Estrategias de logging
- Niveles de log
- Log aggregation
- Métricas y alertas
- Health checks
- Distributed tracing

---

## Estructura de Archivos Planeada

```
parte_5/
├── indice.md (este archivo)
├── 01_principios_solid.md
│   ├── single_responsibility.md
│   ├── open_closed.md
│   ├── liskov_substitution.md
│   ├── interface_segregation.md
│   └── dependency_inversion.md
├── 02_arquitectura_capas.md
├── 03_clean_code.md
├── 04_testing_avanzado.md
├── 05_refactoring.md
├── 06_ddd_introduccion.md
└── 07_metricas_deuda_tecnica.md (opcional)
```

---

## Objetivos de Aprendizaje

Al completar Parte 5, deberías:

- ✓ Aplicar principios SOLID en tus diseños
- ✓ Arquitectar aplicaciones en capas coherentes
- ✓ Escribir código limpio y mantenible
- ✓ Implementar tests significativos (unitarios, integración, e2e)
- ✓ Refactorizar código legado de forma segura
- ✓ Entender fundamentos de DDD
- ✓ Evaluar calidad con métricas
- ✓ Identificar y pagar deuda técnica

---

## Prerrequisitos

Dominar:
- Partes 1-4 (Java fundamental, OOP, patrones)
- Testing básico (JUnit 5)
- OOP avanzado (herencia, polimorfismo, interfaces)
- Patrones de diseño

---

## Próximo: Parte 6

Después de completar Parte 5, estarás listo para Parte 6:
- **Frameworks en producción** (Spring, Spring Boot)
- **Persistencia de datos** (Hibernate, JPA)
- **Aplicaciones web** (Spring MVC, REST APIs)
- **Sistemas distribuidos** (Microservicios, mensajería)
- **DevOps y deployment** (Docker, CI/CD)

---

## Conexiones con Otras Partes

- **Parte 1-4**: Foundation teórica y práctica
- **Parte 5**: Arquitectura y principios de diseño
- **Parte 6**: Aplicación en sistemas reales
- **Reglas de código**: Implementación de convenciones SOLID
- **Patrones (Parte 4)**: Aplicación de patrones respetando SOLID