#!/bin/bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
java -jar "$ROOT_DIR/target/cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" "$@" 