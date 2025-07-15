# Testing Exploratorio - Sprint 4
## Digital Money - Funcionalidad de Transferencias

### Información del Documento
- **Proyecto**: Digital Money
- **Sprint**: 4
- **Fecha**: Julio 2025
- **Tester**: QA Team
- **Objetivo**: Validar la funcionalidad de transferencias de dinero a CBU/CVU/alias desde saldo disponible

---

## 1. Planificación del Testing Exploratorio

### 1.1 Alcance Sprint 4
- **API GET /accounts/ID/transferences** - Últimos destinatarios
- **API POST /accounts/ID/transferences** - Realizar transferencia
- **Transferencias a CBU** (22 dígitos)
- **Transferencias a CVU** (22 dígitos)
- **Transferencias a Alias** (texto)
- **Validaciones de saldo** y permisos
- **Manejo de errores** específicos del sprint

### 1.2 Tiempo Estimado
- **Total**: 8 horas
- **Sesión 1**: 2 horas - APIs de transferencias
- **Sesión 2**: 2 horas - Funcionalidad de transferencias en UI
- **Sesión 3**: 2 horas - Casos edge y validaciones
- **Sesión 4**: 2 horas - Últimos destinatarios y UX

---

## 2. Charter de Testing

### 2.1 Misión
> **Explorar** la nueva funcionalidad de transferencias en Digital Money  
> **Con el enfoque** de validar transferencias a CBU/CVU/alias y manejo de últimos destinatarios  
> **Para descubrir** issues en validaciones, manejo de errores y experiencia de usuario  
> **Durante** 8 horas distribuidas en 4 sesiones

### 2.2 Estrategia de Tours

#### Tour 1: "Money Transfer Tour" - APIs de Transferencias
- **Objetivo**: Explorar las APIs de transferencias implementadas
- **Endpoints**: 
  - GET /accounts/ID/transferences (Status 200, 403)
  - POST /accounts/ID/transferences (Status 200, 400, 410)
- **Notas**: Verificar códigos de respuesta y estructura de datos

#### Tour 2: "Destination Tour" - Tipos de Destinatario
- **Objetivo**: Validar transferencias a diferentes tipos de destino
- **Escenarios**: 
  - Transferencias a CBU válido (22 dígitos)
  - Transferencias a CVU válido (22 dígitos)
  - Transferencias a Alias existente
  - Validación de formatos incorrectos

#### Tour 3: "Error Handling Tour" - Casos Negativos
- **Objetivo**: Probar validaciones y manejo de errores
- **Escenarios**:
  - Fondos insuficientes (Status 410)
  - Cuenta inexistente (Status 400)
  - Sin autenticación (Status 403)
  - Formatos inválidos de CBU/CVU/Alias

#### Tour 4: "User Experience Tour" - Últimos Destinatarios y UX
- **Objetivo**: Evaluar experiencia de usuario en transferencias
- **Aspectos**:
  - Funcionalidad de últimos destinatarios
  - Flujo completo de transferencia en UI
  - Feedback visual y mensajes de error
  - Usabilidad en proceso de transferir dinero

---

## 3. Sesiones de Testing

### Sesión 1: APIs de Transferencias (2 horas)
**Fecha**: [Fecha de ejecución]  
**Charter**: Money Transfer Tour - Exploración de APIs

#### Endpoints a Probar:
1. **GET /accounts/{ID}/transferences**
   - Con token válido → Status 200
   - Sin token → Status 403
   - Token inválido → Status 403
   - Estructura de respuesta JSON

2. **POST /accounts/{ID}/transferences**
   - Transferencia exitosa → Status 200
   - Cuenta inexistente → Status 400
   - Fondos insuficientes → Status 410
   - Sin permisos → Status 403

#### Hallazgos API:
- [ ] GET retorna lista de últimos destinatarios correctamente
- [ ] POST procesa transferencias exitosamente
- [ ] Códigos de estado HTTP son correctos
- [ ] Validaciones de autenticación funcionan

### Sesión 2: Funcionalidad de Transferencias UI (2 horas)
**Fecha**: [Fecha de ejecución]  
**Charter**: Destination Tour - Transferencias por tipo

#### Escenarios de Transferencia:
1. **Transferencia a CBU**
   - CBU válido de 22 dígitos
   - Monto dentro del saldo disponible
   - Descripción opcional

2. **Transferencia a CVU**
   - CVU válido de 22 dígitos
   - Validación de formato
   - Confirmación de transferencia

3. **Transferencia a Alias**
   - Alias existente en el sistema
   - Validación de alias
   - Feedback de transferencia exitosa

#### Hallazgos UI:
- [ ] Interfaz de transferencias es intuitiva
- [ ] Validaciones en tiempo real funcionan
- [ ] Confirmaciones son claras
- [ ] Saldo se actualiza correctamente

### Sesión 3: Casos Edge y Validaciones (2 horas)
**Fecha**: [Fecha de ejecución]  
**Charter**: Error Handling Tour - Casos negativos

#### Casos de Error Probados:
1. **Validaciones de Formato**
   - CBU con menos/más de 22 dígitos
   - CVU con caracteres no numéricos
   - Alias con formato inválido

2. **Validaciones de Negocio**
   - Monto mayor al saldo disponible
   - Transferencia a cuenta inexistente
   - Transferencia sin autenticación

3. **Casos Límite**
   - Monto mínimo de transferencia
   - Monto máximo permitido
   - Caracteres especiales en descripción

#### Hallazgos de Validación:
- [ ] Errores 400, 403, 410 manejados correctamente
- [ ] Mensajes de error son descriptivos
- [ ] UI no se rompe con datos inválidos
- [ ] Validaciones previenen operaciones incorrectas

### Sesión 4: Últimos Destinatarios y UX (2 horas)
**Fecha**: [Fecha de ejecución]  
**Charter**: User Experience Tour - Flujo completo

#### Aspectos de UX Evaluados:
1. **Últimos Destinatarios**
   - Lista se muestra correctamente
   - Selección rápida de destinatarios
   - Información mostrada es útil

2. **Flujo Completo**
   - Login → Ir a transferir → Seleccionar destinatario → Confirmar → Ver resultado
   - Navegación es fluida
   - Feedback visual en cada paso

3. **Usabilidad**
   - Claridad de instrucciones
   - Tiempos de respuesta aceptables
   - Manejo de errores user-friendly

#### Hallazgos de UX:
- [ ] Flujo end-to-end funciona sin interrupciones
- [ ] Últimos destinatarios facilitan transferencias
- [ ] Experiencia de usuario es satisfactoria
- [ ] Accessibility básico implementado

---

## 4. Workflows de Testing Sprint 4

### Workflow 1: Transferencia Exitosa a CBU
```
1. Login con usuario que tiene saldo
2. Navegar a "Enviar dinero"
3. Ingresar CBU válido (22 dígitos)
4. Ingresar monto dentro del saldo disponible
5. Agregar descripción opcional
6. Confirmar transferencia
7. Verificar respuesta Status 200
8. Confirmar saldo actualizado
9. Verificar transferencia en historial
```

### Workflow 2: Transferencia a Últimos Destinatarios
```
1. Login con usuario con transferencias previas
2. GET /accounts/{ID}/transferences → Obtener últimos destinatarios
3. Acceder a "Enviar dinero"
4. Seleccionar destinatario de la lista de últimos
5. Ingresar monto
6. Confirmar transferencia
7. Verificar transferencia exitosa
8. Confirmar destinatario se mantiene en lista
```

### Workflow 3: Manejo de Errores de Transferencia
```
1. Login con usuario autenticado
2. Intentar transferencia con fondos insuficientes
3. Verificar Status 410 - Fondos insuficientes
4. Intentar transferencia a cuenta inexistente
5. Verificar Status 400 - Cuenta inexistente
6. Intentar acceso sin token
7. Verificar Status 403 - Permisos insuficientes
8. Confirmar mensajes de error apropiados en UI
```

### Workflow 4: Validación de Formatos CBU/CVU/Alias
```
1. Acceder a transferencias
2. Probar CBU con menos de 22 dígitos → Error
3. Probar CBU con más de 22 dígitos → Error
4. Probar CVU con caracteres alfabéticos → Error
5. Probar Alias inexistente → Status 400
6. Confirmar validaciones en frontend
7. Verificar validaciones en backend
8. Probar formatos válidos → Transferencia exitosa
```

---

## 5. Criterios de Aceptación

### Criterios Funcionales
- [ ] Todas las funcionalidades principales operativas
- [ ] Integración frontend-backend sin errores críticos
- [ ] Datos persisten correctamente
- [ ] Autenticación y autorización funcionan

### Criterios de Performance
- [ ] Tiempo de carga inicial < 3 segundos
- [ ] Respuesta de APIs < 2 segundos
- [ ] Sin memory leaks evidentes
- [ ] Responsive en dispositivos móviles

### Criterios de UX
- [ ] Navegación intuitiva
- [ ] Mensajes de error claros
- [ ] Feedback visual apropiado
- [ ] Consistencia visual

---

## 6. Herramientas Utilizadas

### Testing Manual
- **Navegadores**: Chrome, Firefox, Safari
- **Dispositivos**: Desktop, Tablet, Mobile (simulado)
- **Herramientas Dev**: Chrome DevTools, Network Tab

### Monitoreo
- **Docker**: `docker-compose ps`, `docker logs`
- **APIs**: Postman, REST Client
- **Performance**: Lighthouse, PageSpeed

---

## 7. Riesgos Identificados

### Riesgos Técnicos
1. **Dependencias de servicios**: Fallos en cadena si un microservicio falla
2. **Performance**: Tiempo de arranque de containers
3. **Memoria**: Consumo elevado de recursos

### Riesgos de Usuario
1. **Usabilidad**: Complejidad para nuevos usuarios
2. **Responsividad**: Experiencia en dispositivos móviles
3. **Feedback**: Falta de indicadores de progreso

---

