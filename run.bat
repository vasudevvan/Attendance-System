@echo off
echo Compiling Java files...
javac -cp ".;sqlite-jdbc-3.50.3.0.jar" *.java

if %errorlevel% neq 0 (
    echo ❌ Compilation failed. Check your code.
    pause
    exit /b
)

echo Running Attendance System...
java -cp ".;sqlite-jdbc-3.50.3.0.jar" App
pause
