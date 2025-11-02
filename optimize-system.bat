@echo off
echo ========================================
echo    OPTIMIZANDO SISTEMA SCHOOLAPP
echo ========================================

echo.
echo 1. Instalando fuentes profesionales...
cd frontend
call npm install @fontsource/inter

echo.
echo 2. Compilando backend...
cd ..
call mvn clean compile -q

echo.
echo 3. Iniciando servicios...
echo Backend: mvn spring-boot:run
echo Frontend: cd frontend && npm run dev

echo.
echo ========================================
echo    SISTEMA OPTIMIZADO EXITOSAMENTE
echo ========================================
echo.
echo Accede a: http://localhost:5173/login
echo Usuario: admin
echo Password: admin123
echo.
pause