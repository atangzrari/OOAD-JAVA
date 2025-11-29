@echo off
setlocal

REM Set JAVA_HOME to JDK 23
set "JAVA_HOME=C:\Program Files\Java\jdk-23"

REM Determine project root (directory of this script)
set "PROJECT_DIR=%~dp0"
cd /d "%PROJECT_DIR%"

REM Run via Maven wrapper
echo Starting WorldBank application...
call mvnw.cmd clean javafx:run

endlocal
