#!/bin/bash
set -e

echo "Building and starting Event-Driven Commerce platform..."

docker compose up --build -d --force-recreate

echo "Done. Services are starting in background."
echo "Run 'docker ps' to verify all containers are up."
