#!/bin/bash

# Set the folder and output file
FOLDER_PATH="src/"  # O'zgartirish mumkin
OUTPUT_FILE="merged_output.txt"

# Clear the output file if it exists
> "$OUTPUT_FILE"

# Find only .java and .xml files and process them
find "$FOLDER_PATH" -type f \( -name "*.java" -o -name "*.xml" \) -print0 | while IFS= read -r -d '' file; do
    echo "Processing $file..."

    # Append the filename with full path
    printf "===== %s =====\n" "$file" >> "$OUTPUT_FILE"

    # Append the file content
    cat "$file" >> "$OUTPUT_FILE"

    # Add a separator for readability
    printf "\n\n" >> "$OUTPUT_FILE"
done

echo "All .java and .xml files have been merged into $OUTPUT_FILE"
