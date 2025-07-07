#!/bin/bash

echo "=== Digital Money Test Environment Validation ==="
echo ""

# Check if services are running
echo "1. Checking if services are running..."

# Check Eureka Server
echo -n "Eureka Server (8761): "
if curl -s http://localhost:8761/actuator/health > /dev/null 2>&1; then
    echo "✓ UP"
else
    echo "✗ DOWN"
fi

# Check Gateway
echo -n "Gateway (3500): "
if curl -s http://localhost:3500/actuator/health > /dev/null 2>&1; then
    echo "✓ UP"
else
    echo "✗ DOWN"
fi

echo ""
echo "2. Testing authentication..."

# Test login
echo -n "Login test (user@test.com): "
response=$(curl -s -w "%{http_code}" -o /tmp/login_response.json \
    -X POST http://localhost:3500/auth/login \
    -H "Content-Type: application/json" \
    -d '{"email":"user@test.com","password":"password123"}')

if [ "$response" = "200" ]; then
    echo "✓ SUCCESS"
    token=$(cat /tmp/login_response.json | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
    echo "  Token received: ${token:0:50}..."
else
    echo "✗ FAILED (HTTP $response)"
    echo "  Response: $(cat /tmp/login_response.json)"
fi

echo ""
echo "3. Recommendation:"
echo "  - If services are DOWN: Run 'docker-compose up' from project root"
echo "  - If login FAILED: Ensure database has test user data"

# Cleanup
rm -f /tmp/login_response.json