@echo off
echo Inicializando usuario administrador...
curl -X POST http://localhost:8080/api/init
echo.
echo Usuario admin creado exitosamente!
echo Username: admin
echo Password: admin123
pause