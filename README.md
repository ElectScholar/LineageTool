# LineageTool

## Overview
LineageTool is a Java-based genealogy visualization application that helps users explore and understand family trees through an interactive graphical interface.

## Features
- **Interactive Graph Visualization**: Drag and arrange family nodes freely
- **Collapsible Nodes**: Double-click to collapse/expand family branches
- **Path Highlighting**: Click nodes to highlight ancestral paths
- **Information Panel**: View detailed information about selected family members
- **Multi-Root Support**: Display multiple family trees simultaneously
- **Smooth Navigation**: Enhanced scrolling and zooming capabilities
- **Search Functionality**: Search function allows collapsing of irrelevant information and view lineage of specific person in tree.
- **Mass Collapsing/Expanding** can collapse entire tree and slowly expand by each generation

## Technical Stack
- Java Swing for GUI components
- JGraphX for graph visualization
- Maven for dependency management

## Installation

### Prerequisites
- Java 8 or higher
- Maven 3.x

### Build Steps
```sh
# Clone the repository
git clone https://github.com/your-username/LineageTool.git
cd LineageTool

# Build with Maven also ensure correct sub directory folder
cd ./lineagetool
mvn clean install

# Run the application
mvn exec:java -Dexec.mainClass="com.lineagetool.App"
```

## Uploading Tree
- Tree currently does not allow during runtime manipulation of trees and only compiles tree at start with text file (see lineage.txt)

## Usage Guide 

### Navigation
- **Scroll**: Use mouse wheel to navigate vertically
- **Pan**: Click and drag empty space to move around
- **Zoom**: Hold Ctrl + mouse wheel to zoom in/out

### Node Interactions
- **Single Click**: View person's details and highlight path to root
- **Double Click**: Collapse/expand node's children
- **Drag**: Click and drag nodes to rearrange the tree

### Adding Family Members
Lineages are all controlled by single text file with example tree pre filled out.
(Run time data modification on the way)

## Development

### Project Structure
```
lineagetool/
├── src/main/java/com/lineagetool/
│   ├── App.java           # Application entry point
│__lineage.txt             # Modify tree data through here. 
└── pom.xml                # Maven configuration
```

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
