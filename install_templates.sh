#!/bin/bash
set -e

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
TEMPLATE_SRC="$SCRIPT_DIR/templates/projects/AndroidHiltRetrofitRoom"

echo "Installing Android Studio Native Template (Phone and Tablet category)..."

# Copy to all Google Android Studio config directories
for dir in ~/.config/Google/AndroidStudio*; do
    if [ -d "$dir" ]; then
        TARGET_DIR="$dir/templates/projects"
        mkdir -p "$TARGET_DIR"
        cp -r "$TEMPLATE_SRC" "$TARGET_DIR/"
        echo "Installed in: $TARGET_DIR/AndroidHiltRetrofitRoom"
    fi
done

for dir in ~/.local/share/Google/AndroidStudio*; do
    if [ -d "$dir" ]; then
        TARGET_DIR="$dir/templates/projects"
        mkdir -p "$TARGET_DIR"
        cp -r "$TEMPLATE_SRC" "$TARGET_DIR/"
        echo "Installed in: $TARGET_DIR/AndroidHiltRetrofitRoom"
    fi
done

echo "Done! Restart Android Studio and check 'Phone and Tablet' in New Project."
