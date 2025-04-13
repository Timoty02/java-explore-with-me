@echo off
echo [Windows] Project build start...
call mvn clean install
if %errorlevel% neq 0 (
    echo Ошибка при сборке Maven!
    exit /b %errorlevel%
)

echo [Windows] Dicker-images build...
call docker-compose build
if %errorlevel% neq 0 (
    echo Ошибка при сборке Docker!
    exit /b %errorlevel%
)

echo [Windows] Container start...
call docker-compose up -d
echo Start successful.