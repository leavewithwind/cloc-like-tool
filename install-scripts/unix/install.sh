#!/bin/bash
# Simple installation script for cloc tool
echo "Installing cloc tool..."

# Set directories
BIN_DIR="$HOME/bin"
TOOL_DIR="$BIN_DIR/cloc-like-tool"
JAR_NAME="cloc-like-tool-1.0-SNAPSHOT-jar-with-dependencies.jar"

# Create directories
echo "Creating directories..."
mkdir -p "$TOOL_DIR"

# Get script directory and project root
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd 2>/dev/null || echo "")"

# Find JAR file
echo "Looking for JAR file..."
JAR_PATH=""

# Check script directory for JAR
if [ -f "$SCRIPT_DIR/$JAR_NAME" ]; then
    JAR_PATH="$SCRIPT_DIR/$JAR_NAME"
    echo "Found JAR in script directory"
elif [ -f "$JAR_NAME" ]; then
    # Check current directory for JAR
    JAR_PATH="$PWD/$JAR_NAME"
    echo "Found JAR in current directory"
elif [ -n "$PROJECT_ROOT" ] && [ -f "$PROJECT_ROOT/$JAR_NAME" ]; then
    # Check project root for JAR
    JAR_PATH="$PROJECT_ROOT/$JAR_NAME"
    echo "Found JAR in project root directory"
elif [ -n "$PROJECT_ROOT" ] && [ -f "$PROJECT_ROOT/target/$JAR_NAME" ]; then
    # Check project target directory for JAR
    JAR_PATH="$PROJECT_ROOT/target/$JAR_NAME"
    echo "Found JAR in project target directory"
fi

# If JAR not found, exit
if [ -z "$JAR_PATH" ]; then
    echo "ERROR: JAR file not found"
    echo "Searched locations:"
    echo "- $SCRIPT_DIR/$JAR_NAME"
    echo "- $PWD/$JAR_NAME"
    if [ -n "$PROJECT_ROOT" ]; then
        echo "- $PROJECT_ROOT/$JAR_NAME"
        echo "- $PROJECT_ROOT/target/$JAR_NAME"
    fi
    echo
    echo "Please ensure the JAR file exists or build the project first."
    exit 1
fi

# Copy JAR to installation directory
echo "Copying JAR from: $JAR_PATH"
cp "$JAR_PATH" "$TOOL_DIR/"
if [ $? -ne 0 ]; then
    echo "ERROR: Failed to copy JAR file"
    exit 1
fi

# Create launcher script
echo "Creating launcher script..."
cat > "$BIN_DIR/cloc" << EOF
#!/bin/bash
java -jar "$TOOL_DIR/$JAR_NAME" "\$@"
EOF
chmod +x "$BIN_DIR/cloc"

# Add to PATH if needed
if [[ ":$PATH:" != *":$BIN_DIR:"* ]]; then
    echo "Adding to PATH..."
    for rcfile in "$HOME/.bashrc" "$HOME/.zshrc" "$HOME/.bash_profile"; do
        if [ -f "$rcfile" ]; then
            if ! grep -q "export PATH=\"\$HOME/bin:\$PATH\"" "$rcfile"; then
                echo 'export PATH="$HOME/bin:$PATH"' >> "$rcfile"
                echo "Updated $rcfile"
            fi
        fi
    done
else
    echo "$BIN_DIR is already in PATH"
fi

echo
echo "Installation complete!"
echo "You may need to restart your terminal or run: source ~/.bashrc"
echo "After that, you can run: cloc -l c++ your_source_directory"