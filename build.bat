@echo off
cd /d "%~dp0"
title Vision Builder
call gradlew installDist
echo.
pause