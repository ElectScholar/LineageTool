# LineageTool

## Overview
LineageTool is a Java-based application that models and manages family lineages using a **doubly linked list**. It allows adding and retrieving individuals while maintaining their **parent-child relationships**.

## Features
- **Doubly Linked List Implementation**: Uses a custom linked list to track lineage.
- **Add Individuals**: Add new family members dynamically.
- **Parent-Child Relationships**: Link individuals with parents and children.
- **Retrieve and Print Lineage**: Fetch and display lineage information.

## Installation
### Prerequisites
- Java 8 or higher
- Maven (for dependency management)

### Build and Run
1. **Clone the repository:**
   ```sh
   git clone https://github.com/your-repo/LineageTool.git
   cd LineageTool
   ```
2. **Compile the project:**
   ```sh
   mvn compile
   ```
3. **Run the application:**
   ```sh
   mvn exec:java -Dexec.mainClass="com.lineagetool.App"
   ```

## Usage
### Adding a Family Member
```java
LineageService lineage = new LineageService();
Person jacob = new Person("Jacob | Israel", new String[]{"Patriarch of Israel"});
lineage.addFirst(new Node<>(jacob));
```

### Adding a Child
```java
Person joseph = new Person("Joseph", new String[]{"Son of Jacob"});
lineage.addChild(new Node<>(joseph), "Jacob | Israel");
```

### Retrieving and Printing Lineage
```java
Node<Person> jacobNode = lineage.getNode("Jacob | Israel");
lineage.printLineage(jacobNode);
```

## Running Tests
To run unit tests, use:
```sh
mvn test
```

## Contributing
Feel free to open issues and pull requests!

## License
[MIT License](LICENSE)

