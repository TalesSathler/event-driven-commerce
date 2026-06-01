#!/bin/bash
set -e

yarn install

echo 'Starting frontend dev server...'
npx nx serve --host 0.0.0.0
