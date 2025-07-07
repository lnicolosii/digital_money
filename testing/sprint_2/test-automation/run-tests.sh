#!/bin/bash
# Script para ejecutar las pruebas automatizadas de Sprint 2
# Digital Money - Test Automation Framework

echo "=================================="
echo "Digital Money Sprint 2 Test Suite"
echo "=================================="
echo ""

# Verificar prerequisitos
echo "1. Verificando prerequisitos..."

# Verificar Java
if ! command -v java &> /dev/null; then
    echo "❌ Java no encontrado. Por favor instalar Java 17+"
    exit 1
fi

# Verificar Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven no encontrado. Por favor instalar Maven 3.6+"
    exit 1
fi

echo "✅ Java y Maven encontrados"

# Verificar servicios
echo ""
echo "2. Verificando servicios..."

services=("8761:Eureka" "3500:Gateway" "8081:Auth" "8082:User" "8083:Account")
all_services_up=true

for service in "${services[@]}"; do
    port=$(echo $service | cut -d: -f1)
    name=$(echo $service | cut -d: -f2)
    
    if curl -s "http://localhost:$port" > /dev/null 2>&1; then
        echo "✅ $name Service (puerto $port): CORRIENDO"
    else
        echo "❌ $name Service (puerto $port): NO DISPONIBLE"
        all_services_up=false
    fi
done

if [ "$all_services_up" = false ]; then
    echo ""
    echo "⚠️  Algunos servicios no están disponibles."
    echo "   Por favor iniciar todos los servicios antes de ejecutar las pruebas."
    echo ""
    echo "   Orden de inicio recomendado:"
    echo "   1. docker-compose up db"
    echo "   2. mvn spring-boot:run en eureka-server"
    echo "   3. mvn spring-boot:run en gateway"
    echo "   4. mvn spring-boot:run en auth-service"
    echo "   5. mvn spring-boot:run en user-service"
    echo "   6. mvn spring-boot:run en account-service"
    echo ""
    read -p "¿Continuar con las pruebas de todos modos? (y/N): " continue_tests
    if [[ ! $continue_tests =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo ""
echo "3. Ejecutando pruebas automatizadas..."
echo ""

# Compilar proyecto
echo "Compilando framework de pruebas..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "❌ Error al compilar el proyecto"
    exit 1
fi

# Ejecutar suite de smoke tests
echo ""
echo "Ejecutando Smoke Tests..."
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-smoke.xml

# Capturar resultado
test_result=$?

echo ""
echo "=================================="
echo "RESULTADOS DE EJECUCIÓN"
echo "=================================="

if [ $test_result -eq 0 ]; then
    echo "✅ TODAS LAS PRUEBAS PASARON"
    echo ""
    echo "Suite ejecutada exitosamente:"
    echo "• Pruebas de infraestructura"
    echo "• Pruebas de autenticación"
    echo "• Pruebas de gestión de usuarios"
    echo "• Pruebas de gestión de tarjetas"
else
    echo "❌ ALGUNAS PRUEBAS FALLARON"
    echo ""
    echo "Revisar reportes en: target/surefire-reports/"
fi

echo ""
echo "Reportes generados:"
echo "• HTML Report: target/surefire-reports/index.html"
echo "• TestNG Report: test-output/index.html"
echo ""

echo "Para ver resultados detallados:"
echo "  open target/surefire-reports/index.html"
echo ""
echo "=================================="