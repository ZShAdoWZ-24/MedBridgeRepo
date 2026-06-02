@echo off
:: MedBridge — compile and run script for Windows
:: Double-click this file or run from Command Prompt

echo Compiling MedBridge...
if not exist out mkdir out
javac -d out src\medbridge\*.java

if %errorlevel% == 0 (
  echo Compilation successful!
  echo Starting MedBridge...
  java -cp out medbridge.MedBridgeApp
) else (
  echo Compilation failed. Check error messages above.
  pause
)
