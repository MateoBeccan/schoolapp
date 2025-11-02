@echo off
echo Creando datos de prueba...

echo Creando profesores primero...
curl -X POST http://localhost:8080/api/teachers -H "Content-Type: application/json" -d "{\"firstName\":\"Juan\",\"lastName\":\"Perez\",\"email\":\"juan@school.com\",\"department\":\"Matematicas\"}"
curl -X POST http://localhost:8080/api/teachers -H "Content-Type: application/json" -d "{\"firstName\":\"Maria\",\"lastName\":\"Garcia\",\"email\":\"maria@school.com\",\"department\":\"Historia\"}"
curl -X POST http://localhost:8080/api/teachers -H "Content-Type: application/json" -d "{\"firstName\":\"Carlos\",\"lastName\":\"Lopez\",\"email\":\"carlos@school.com\",\"department\":\"Ciencias\"}"

echo.
echo Creando materias...
curl -X POST http://localhost:8080/api/subjects -H "Content-Type: application/json" -d "{\"name\":\"Matematicas\",\"description\":\"Matematicas basicas\",\"credits\":4}"
curl -X POST http://localhost:8080/api/subjects -H "Content-Type: application/json" -d "{\"name\":\"Historia\",\"description\":\"Historia universal\",\"credits\":3}"
curl -X POST http://localhost:8080/api/subjects -H "Content-Type: application/json" -d "{\"name\":\"Ciencias\",\"description\":\"Ciencias naturales\",\"credits\":4}"

echo.
echo Creando estudiantes...
curl -X POST http://localhost:8080/api/students -H "Content-Type: application/json" -d "{\"enrollment\":\"B-6380/1\",\"firstName\":\"Ana\",\"lastName\":\"Lopez\",\"email\":\"ana@student.com\",\"career\":\"Ingenieria\",\"subjects\":[]}"
curl -X POST http://localhost:8080/api/students -H "Content-Type: application/json" -d "{\"enrollment\":\"C-7421/2\",\"firstName\":\"Carlos\",\"lastName\":\"Rodriguez\",\"email\":\"carlos@student.com\",\"career\":\"Medicina\",\"subjects\":[]}"

echo.
echo Creando examenes...
curl -X POST http://localhost:8080/api/exams -H "Content-Type: application/json" -d "{\"student\":{\"id\":1},\"subject\":{\"id\":1},\"examDate\":\"2024-01-15\",\"grade\":8.5,\"observations\":\"Excelente desempeño\"}"
curl -X POST http://localhost:8080/api/exams -H "Content-Type: application/json" -d "{\"student\":{\"id\":2},\"subject\":{\"id\":2},\"examDate\":\"2024-01-16\",\"grade\":7.0,\"observations\":\"Buen trabajo\"}"
curl -X POST http://localhost:8080/api/exams -H "Content-Type: application/json" -d "{\"student\":{\"id\":1},\"subject\":{\"id\":3},\"examDate\":\"2024-01-17\",\"grade\":9.2,\"observations\":\"Sobresaliente\"}"

echo.
echo Datos de prueba creados exitosamente!
pause