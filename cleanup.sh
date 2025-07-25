#!/bin/bash
set -e

# 파일 삭제 명령어
echo "Deleting .java files in backend..."
rm -rf ./backend/generated/src/main/java/com/mickey/dinggading/api/*.java
rm -rf ./backend/generated/src/main/java/com/mickey/dinggading/model/*.java

echo "Deleting .ts files in frontend..."
rm -rf ./frontend/generated/*.ts
rm -rf ./frontend/generated/api/*.ts
rm -rf ./frontend/generated/model/*.ts

echo "Cleanup completed."
