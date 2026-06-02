#!/bin/bash
# MedBridge — compile and run script
# Run this from the medbridge directory

echo "Compiling MedBridge..."
mkdir -p out
javac -d out src/medbridge/*.java

if [ $? -eq 0 ]; then
  echo "Compilation successful!"
  echo "Starting MedBridge..."
  java -cp out medbridge.MedBridgeApp
else
  echo "Compilation failed. Check error messages above."
fi
