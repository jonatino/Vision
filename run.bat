@echo off
cd /d "%~dp0"
title Vision

set bat="./build/install/Vision/bin/Vision.bat"

:loop
if exist %bat% (
    call %bat%
    pause
) else (
    call build.bat
    cls
    title Vision
    goto loop
)