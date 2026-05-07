@echo off
echo Starting MXT EAV Backend...
echo ===========================
call mvn spring-boot:run -DskipTests
pause
