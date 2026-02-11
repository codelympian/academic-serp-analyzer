#!/bin/bash

echo "========================================="
echo "  ScholarScan Setup Script"
echo "========================================="
echo ""

# Check for Java
echo "Checking prerequisites..."
if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Please install Java 17 or higher."
    exit 1
fi

echo "✅ Java found: $(java -version 2>&1 | head -n 1)"

# Check for Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven not found. Please install Maven 3.6 or higher."
    exit 1
fi

echo "✅ Maven found: $(mvn -version 2>&1 | head -n 1)"

# Check for Node.js
if ! command -v node &> /dev/null; then
    echo "❌ Node.js not found. Please install Node.js 18 or higher."
    exit 1
fi

echo "✅ Node.js found: $(node -v)"

# Check for npm
if ! command -v npm &> /dev/null; then
    echo "❌ npm not found. Please install npm."
    exit 1
fi

echo "✅ npm found: $(npm -v)"
echo ""

# Setup Backend
echo "Setting up backend..."
cd backend || exit 1

if [ ! -d "target" ]; then
    echo "Building backend (first time setup)..."
    mvn clean install -DskipTests
fi

echo "✅ Backend ready"
echo ""

# Setup Frontend
echo "Setting up frontend..."
cd ../frontend || exit 1

if [ ! -d "node_modules" ]; then
    echo "Installing frontend dependencies (first time setup)..."
    npm install
fi

echo "✅ Frontend ready"
echo ""

# Instructions
echo "========================================="
echo "  Setup Complete!"
echo "========================================="
echo ""
echo "To run the application:"
echo ""
echo "1. Start the backend (in one terminal):"
echo "   cd backend && mvn spring-boot:run"
echo ""
echo "2. Start the frontend (in another terminal):"
echo "   cd frontend && npm run dev"
echo ""
echo "3. Open your browser and navigate to:"
echo "   http://localhost:3000"
echo ""
echo "========================================="
echo ""
echo "Note: The system will use mock data if Serper API"
echo "credentials are not configured in:"
echo "backend/src/main/resources/application.properties"
echo ""
echo "Happy analyzing academic papers! 📚"
echo ""
