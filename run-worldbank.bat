@echo off
setlocal

REM Determine project root (directory of this script)
set "PROJECT_DIR=%~dp0"
cd /d "%PROJECT_DIR%"

REM Run via Maven wrapper to ensure JavaFX + dependencies are placed on the module path
call mvnw.cmd clean javafx:run %*

endlocal

