@echo off

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
