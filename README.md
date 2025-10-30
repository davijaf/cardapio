
# UNIPDS - M02.A01 - Introduction to Java 25 with Gradle and Cardapio Project

## General Context

- Lesson about build tools in the Java ecosystem
- Focus on alternatives to Maven: Ant and Gradle
- Practical demonstration of project creation using Gradle in IntelliJ IDEA
- Example based on Dona Florinda's restaurant (from Chaves) to illustrate concepts
- The demonstration also served to introduce the new Java 25 version

## Main Features Demonstrated in `Main.java`

- **Menu Item Management:**
	- Load menu items from a database abstraction (`Database`, `DatabaseHashMap`).
	- Print total items, display item details, and remove items from the menu.
- **Serialization:**
	- Serialize menu items to JSON using Gson and print them.
- **Category Analysis:**
	- Count and display distinct categories using sets and Java streams.
	- Print categories in different orders (insertion, sorted by name).
	- Count items per category using loops and streams, with different map types to preserve order.
- **Filtering and Promotion:**
	- Filter items by promotional categories using `TreeSet`, `Set.of`, and `EnumSet`.
	- Print items on promotion and their categories.
- **Category Descriptions:**
	- Use `EnumMap` to associate category descriptions and print them for promotional categories.
- **Menu Viewing History:**
	- Track and print viewing history of menu items using the `HistoricoVisualizacao` class.
	- Show total views and list all view events.
- **Item Search and Modification:**
	- Search for items by ID and print results.
	- Remove items by ID and confirm removal.
	- Change item prices and audit price changes, displaying audit logs.
- **Memory Management:**
	- Demonstrate explicit garbage collection and its effect on menu items.

## Example Operations in the Demo

- Print all menu items and their JSON representations.
- Show and count all unique categories, using different collection types.
- Count and print the number of items per category, preserving order.
- Filter and print items on promotion by category.
- Show category descriptions for promotional categories.
- Track and print menu viewing history, including total views.
- Remove and update menu items, with confirmation and audit logs.

## Technologies Used

- Java 25 (demonstration purpose)
- Gradle (build tool)
- Gson (JSON serialization)
- HttpServer (com.sun.net.httpserver) - Simple HTTP server
- ServerSocket/Socket - TCP networking

## Network Programming Features

### HTTP Server (`ServidorItensCardapio`)
- Simple HTTP server using `com.sun.net.httpserver.HttpServer`
- Serves menu items as JSON via REST endpoint
- Runs on port 8000 at `/itens-cardapio`
- Reads and serves the `cardapio.json` file
- Returns proper `Content-Type: application/json` headers

### HTTP Server with ServerSocket (`ServidorItensCardapioComSocket`)
- Manual HTTP server implementation using low-level `ServerSocket`
- Demonstrates raw TCP socket programming
- Opens port 8000 and accepts client connections
- Reads bytes directly from `InputStream`
- Stores data in `StringBuilder` byte by byte
- Shows received data as numeric byte values
- Educational example showing the need for proper character encoding
- Illustrates the difference between bytes and characters in network communication

### HTTP Client (`ClientLocalhostCardapio`)
- Consumes the HTTP server endpoint
- Fetches menu items from `http://localhost:8000/itens-cardapio`
- Demonstrates HTTP GET requests in Java

### TCP Protocol Demonstration
Three classes to illustrate low-level TCP networking:

1. **`ServidorTCPManual`** - TCP Server Implementation
   - Uses `ServerSocket` to listen on port 9000
   - Accepts client connections with `accept()`
   - Demonstrates bidirectional communication:
     - OutputStream (server) → InputStream (client)
     - InputStream (server) ← OutputStream (client)
   - Echo server mode with interactive messaging
   - Reliable, ordered data delivery

2. **`ClienteTCPManual`** - TCP Client Implementation
   - Connects to server using `Socket`
   - Sends messages via OutputStream
   - Receives responses via InputStream
   - Interactive mode for testing bidirectional communication
   - Type "SAIR" to disconnect

3. **`DemonstracaoProtocoloTCP`** - Educational TCP Concepts
   - Visual ASCII diagrams showing ServerSocket ↔ Socket architecture
   - Explains bidirectional TCP communication flow
   - Compares TCP vs UDP (DatagramSocket)
   - Shows code examples and best practices
   - Highlights TCP features: reliability, ordering, flow control

### JSON Generation (`GeradorItensCardapioJSON`)
- Generates `cardapio.json` file from database
- Uses Gson with pretty printing
- Exports all menu items to JSON format

## How to Run

1. Ensure you have Java 25 and Gradle installed (or use the provided Gradle Wrapper).
2. Build the project:
	 ```sh
	 ./gradlew build
	 ```
3. Run the main class:
	 ```sh
	 ./gradlew run
	 ```

### Running the HTTP Server
Start the HTTP server to serve menu items:
```sh
./gradlew runServidor
```
Access the menu at: `http://localhost:8000/itens-cardapio`

### Running the HTTP Server with ServerSocket (Manual Implementation)
Start the low-level socket server to see raw byte communication:
```sh
./gradlew runServidorSocket
```
Then access `http://localhost:8000` in your browser to see how bytes are read from the InputStream.

### Running TCP Demonstrations

**View TCP concepts and diagrams:**
```sh
./gradlew runDemonstracaoTCP
```

**Start TCP server (Terminal 1):**
```sh
./gradlew runServidorTCP
```

**Connect TCP client (Terminal 2):**
```sh
./gradlew runClienteTCP
```

### Generate Menu JSON
```sh
./gradlew runGerador
```

## Notes

- The project demonstrates modern Java features (streams, records, collections, text blocks).
- The code is organized for educational purposes, showing both imperative and functional programming styles.

## Ant and Maven

- Ant released in 2000, uses XML, programmatic format based on tasks
- Ant does not manage dependencies natively (requires Ivy)
- Maven released in 2004, also uses XML, but is declarative
- Maven provides integrated dependency management
- Real project example: need for custom logic (e.g., test database names)
- Limitation of Maven/Ant: hard to add complex logic, requires creating Java plugins (Mojo)
- Ant's creator recognized XML was not the best choice, would have preferred a scripting language

## Gradle: Overview

- Gradle released in 2007, more modern
- Scripts can be written in Groovy or Kotlin
- Combines declarative approach (predefined phases) and programmatic (easy customization)
- Reuses Maven Central repository for dependencies
- Build described in the build.gradle file
- More flexible for custom logic within the script itself

## Installing and Using Gradle

- Two main installation methods: directly on the machine or via Gradle Wrapper (gradlew)
- Gradle Wrapper recommended, provides scripts for Windows (.bat) and Mac/Linux (shell script)
- Example of project creation in IntelliJ using Gradle
- Example project: menu for Dona Florinda's restaurant
- Java 20 chosen, Groovy as the build script language

## Gradle Project Structure

- Main files: build.gradle (main configuration), settings.gradle (project name)
- Gradle creates a folder structure similar to Maven: src/main/java, src/test/java
- Plugins add tasks (compile, test, generate JAR, JavaDoc)
- group and version defined, equivalent to Maven's groupId and version

## Dependencies and Tasks

- Add dependencies via implementation in build.gradle
- Example dependency: JUnit for testing
- Libraries searched in Maven Repository, example with Gson (Google)
- implementation for production dependencies, testImplementation for tests
- gradlew dependencies command shows all project dependencies

## Practical Demonstration: Creating Classes

- Creation of ItemCardapio class with attributes: id, name, description, category, price, discount
- Use of enum for categories (Appetizers, Main Courses, Drinks, Dessert)
- Discussion about immutability, use of final and constructor
- Suggestion to convert to record (modern Java)
- Organization in packages (e.g., mx.florinda.cardapio)
- Build automatically generates classes and JAR

## Serialization with Gson

- Adding Gson dependency to the project
- Creating Main class to instantiate ItemCardapio and serialize to JSON
- Use of Java text blocks for description
- Running the project via Gradle in the IDE
- Printing the object in JSON format

## Final Considerations

- Gradle will be used throughout the extra module of the course
- Tool highlighted for its flexibility and integration with the Java ecosystem
- End of lesson and invitation to upcoming videos
