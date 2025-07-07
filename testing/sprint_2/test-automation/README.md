# Framework de Automatización de Pruebas de Digital Money

## Visión General

Este es un framework de automatización de pruebas basado en Java para las funcionalidades del Sprint 2 de Digital Money, utilizando TestNG y REST Assured.

## Estructura del Framework

```
test-automation/
├── pom.xml # Dependencias y configuración de Maven
├── README.md # Este archivo
└── src/test/
├── java/com/digitalmoney/tests/
│ ├── BaseTest.java # Clase base con utilidades comunes
│ ├── AuthenticationSmokeTests.java # Pruebas smoke de autenticación
│ ├── CardManagementSmokeTests.java # Pruebas smoke de gestión de tarjetas
│ ├── UserManagementSmokeTests.java # Pruebas smoke de gestión de usuarios
│ └── InfrastructureSmokeTests.java # Pruebas smoke de infraestructura
└── resources/
└── testng-smoke.xml # Configuración de la suite TestNG
```

## Requisitos Previos

1. Java 17 o superior
2. Maven 3.6 o superior
3. Aplicación Digital Money corriendo en localhost
4. Todos los servicios (Eureka, Gateway, Auth, User, Account) deben estar en funcionamiento

## Servicios y Puertos Requeridos

- Servidor Eureka: http://localhost:8761
- Gateway: http://localhost:3500
- Servicio de Autenticación: http://localhost:8081
- Servicio de Usuario: http://localhost:8082
- Servicio de Cuenta: http://localhost:8083

## Cobertura de Pruebas

### Pruebas Smoke Implementadas

- **TC_S2_001**: Crear tarjeta - Datos válidos
- **TC_S2_002**: Obtener tarjetas por ID de cuenta
- **TC_S2_003**: Inicio de sesión - Credenciales válidas
- **TC_S2_004**: Validación de token - Token válido
- **TC_S2_005**: Actualizar perfil de usuario - Datos válidos
- **TC_S2_021**: Registro de usuario - Datos válidos
- **TC_S2_025**: Verificación de estado (health check) - Todos los servicios

### Casos de Prueba Adicionales

- Escenarios negativos de autenticación
- Escenarios negativos de gestión de tarjetas
- Escenarios negativos de gestión de usuarios
- Validación de enrutamiento en el Gateway
- Validación de encabezados CORS
- Verificación del descubrimiento de servicios

## Ejecución de Pruebas

### Ejecutar Todas las Pruebas Smoke

```bash
mvn clean test
```

### Ejecutar una Suite de Prueba Específica

```bash
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/testng-smoke.xml
```

### Ejecutar una Clase de Prueba Específica

```bash
mvn clean test -Dtest=AuthenticationSmokeTests
```

### Ejecutar con URL Base Personalizada

```bash
mvn clean test -DbaseUrl=http://your-server:3500
```

## Configuración de Pruebas

### Configuración Base

- URL Base: http://localhost:3500 (configurable)
- Credenciales de usuario por defecto para pruebas:
  - Email: user@test.com
  - Password: password123
- ID de cuenta de prueba por defecto: 1
- ID de usuario de prueba por defecto: 1

### Autenticación

Las pruebas se autentican automáticamente antes de cada método de prueba utilizando la configuración `@BeforeMethod` en `BaseTest.java`.

## Gestión de Datos de Prueba

- Los números de tarjeta se generan dinámicamente usando timestamps para asegurar unicidad
- Las direcciones de email para pruebas de registro incluyen timestamps para unicidad
- Los datos de prueba están parametrizados y pueden modificarse fácilmente

## Reportes

TestNG genera reportes HTML en el directorio `target/surefire-reports` después de la ejecución de pruebas.

## Dependencias

- **TestNG 7.8.0**: Framework de pruebas
- **REST Assured 5.3.2**: Librería para pruebas de API
- **Jackson 2.15.2**: Procesamiento JSON
- **Maven Surefire Plugin 3.0.0**: Ejecución de pruebas

## Solución de Problemas

### Problemas Comunes

1. **Conexión Rechazada**: Asegúrate de que todos los servicios estén ejecutándose en los puertos correctos
2. **Fallos de Autenticación**: Verifica que el usuario de prueba existe en la base de datos
3. **Errores 404**: Revisa si el descubrimiento de servicios de Eureka está funcionando correctamente

### Orden de Inicio de Servicios

1. Iniciar primero el Servidor Eureka
2. Iniciar los servicios de base de datos
3. Iniciar el Gateway
4. Iniciar los microservicios (Auth, User, Account)
5. Esperar 1-2 minutos para el registro de servicios
6. Ejecutar las pruebas

## Extender el Framework

Para agregar nuevas pruebas:

1. Crear nueva clase de prueba extendiendo `BaseTest`
2. Agregar métodos de prueba con anotaciones TestNG apropiadas
3. Actualizar `testng-smoke.xml` si es necesario
4. Seguir las convenciones de nomenclatura y patrones existentes
