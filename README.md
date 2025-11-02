SchoolApp es una aplicación web full-stack desarrollada por Mateo Beccan para la gestión de una institución educativa.
El sistema permite administrar estudiantes, profesores, materias y carreras, implementando un backend robusto con Spring Boot + MySQL y un frontend dinámico con Vue.js.
Está diseñado bajo una arquitectura modular y escalable, ideal para demostrar habilidades de desarrollo backend y frontend moderno en un entorno realista.

Principales funcionalidades

CRUD de Estudiantes, Profesores, Materias y Carreras

Listado y búsqueda dinámica desde el frontend

Enrutamiento interno con Vue Router

Conexión al backend vía Axios REST API

Arquitectura escalable lista para agregar:

Autenticación con Spring Security (ADMIN / PROFESOR / ALUMNO)

Validaciones de datos

Manejo de errores global

Dashboard administrativo
# Clonar el repositorio
git clone https://github.com/MateoBeccan/schoolapp.git
cd schoolapp

# Configurar base de datos en src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/schoolapp
spring.datasource.username=usuario
spring.datasource.password=contraseña

# Ejecutar
./mvnw spring-boot:run

cd frontend
npm install
npm run serve

