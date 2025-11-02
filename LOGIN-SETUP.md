# Sistema de Login - SchoolApp

## Configuración Inicial

### 1. Iniciar el Backend
```bash
mvn spring-boot:run
```

### 2. Crear Usuario Admin
Ejecutar una de estas opciones:

**Opción A: Script automático**
```bash
init-admin.bat
```

**Opción B: Curl manual**
```bash
curl -X POST http://localhost:8080/api/init
```

**Opción C: Desde el navegador**
```
POST http://localhost:8080/api/init
```

### 3. Iniciar Frontend
```bash
cd frontend
npm run dev
```

## Credenciales por Defecto
- **Usuario:** admin
- **Contraseña:** admin123

## Funcionalidades Implementadas

### Backend (Spring Boot)
- ✅ Autenticación JWT
- ✅ Modelo de Usuario con roles
- ✅ Configuración de seguridad optimizada
- ✅ Controladores con manejo de errores
- ✅ Pool de conexiones optimizado
- ✅ Endpoints protegidos

### Frontend (Vue.js)
- ✅ Página de login responsive
- ✅ Guards de autenticación en rutas
- ✅ Interceptores de axios para tokens
- ✅ Manejo automático de tokens expirados
- ✅ Navegación con logout
- ✅ Estados de loading y error

## Optimizaciones Implementadas

### Base de Datos
- Pool de conexiones HikariCP optimizado
- Configuración de timeouts mejorada

### Seguridad
- JWT con expiración de 24 horas
- CORS configurado correctamente
- Endpoints protegidos por roles

### Frontend
- Interceptores automáticos para autenticación
- Manejo de errores centralizado
- Estados de loading mejorados

## Estructura de Archivos Nuevos

```
backend/
├── model/
│   ├── User.java
│   ├── LoginRequest.java
│   └── LoginResponse.java
├── service/
│   ├── JwtService.java
│   └── UserService.java
├── security/
│   ├── SecurityConfig.java (actualizado)
│   └── JwtAuthenticationFilter.java
├── controller/
│   ├── AuthController.java
│   └── InitController.java
└── repository/
    └── UserRepository.java

frontend/
├── views/
│   └── LoginView.vue
├── auth.js
└── router/index.js (actualizado)
```

## Uso del Sistema

1. **Primera vez:** Ejecutar `init-admin.bat` para crear el usuario admin
2. **Login:** Ir a `http://localhost:5173/login`
3. **Navegación:** Una vez autenticado, acceso completo al sistema
4. **Logout:** Botón en la barra de navegación

## Notas Importantes

- El token JWT expira en 24 horas
- Las rutas están protegidas automáticamente
- El sistema redirige al login si el token expira
- Todas las peticiones incluyen el token automáticamente