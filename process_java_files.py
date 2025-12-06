import os
import re

def process_java_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    # Remove import lines
    lines = [line for line in lines if not line.strip().startswith('import ')]

    # Remove leading whitespace from each line
    processed_lines = [re.sub(r'^\s+', '', line) for line in lines]

    return ''.join(processed_lines)

def main():
    base_dir = 'src/main/java/chsu/example/sports_medicine'
    output_file = 'all_source_code.txt'

    all_code = []

    for root, dirs, files in os.walk(base_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                relative_path = os.path.relpath(file_path, base_dir)
                processed_code = process_java_file(file_path)
                all_code.append(f'// --- File: {relative_path} ---\n{processed_code}\n')

    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(''.join(all_code))

    print(f'Processed code written to {output_file}')

if __name__ == '__main__':
    main()
