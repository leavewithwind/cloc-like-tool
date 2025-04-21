#!/bin/bash
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
java -jar "$SCRIPT_DIR/cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar" "$@" 