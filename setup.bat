@echo off
echo =========================================
echo   ScholarScan Setup Script
echo =========================================
echo.

echo Checking prerequisites...

:: Check for Java
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo X Java not found. Please install Java 17 or higher.
    pause
    exit /b 1
)
echo √ Java found

:: Check for Maven
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo X Maven not found. Please install Maven 3.6 or higher.
    pause
    exit /b 1
)
echo √ Maven found

:: Check for Node.js
where node >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo X Node.js not found. Please install Node.js 18 or higher.
    pause
    exit /b 1
)
echo √ Node.js found

:: Check for npm
where npm >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo X npm not found. Please install npm.
    pause
    exit /b 1
)
echo √ npm found
echo.

:: Setup Backend
echo Setting up backend...
cd backend
if not exist "target" (
    echo Building backend (first time setup)...
    call mvn clean install -DskipTests
)
echo √ Backend ready
echo.

:: Setup Frontend
cd ..\frontend
echo Setting up frontend...
if not exist "node_modules" (
    echo Installing frontend dependencies (first time setup)...
    call npm install
)
echo √ Frontend ready
echo.

:: Instructions
echo =========================================
echo   Setup Complete!
echo =========================================
echo.
echo To run the application:
echo.
echo 1. Start the backend (in one terminal):
echo    cd backend ^&^& mvn spring-boot:run
echo.
echo 2. Start the frontend (in another terminal):
echo    cd frontend ^&^& npm run dev
echo.
echo 3. Open your browser and navigate to:
echo    http://localhost:3000
echo.
echo =========================================
echo.
echo Note: The system will use mock data if Serper API
echo credentials are not configured in:
echo backend\src\main\resources\application.properties
echo.
echo Happy analyzing academic papers! 📚
echo.
pause
