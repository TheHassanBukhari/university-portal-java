# University Portal (Java)

A Java-based Student Result Management System with **Console** and **GUI** versions. Detailed documentation is available in the `Documentation/` folder.

## Project Structure

* `Console Based Version/` – Console implementation (includes `run.txt` with run commands)
* `GUI Based Version/` – GUI (Swing) implementation (includes `run.txt` with run commands)
* `Documentation/` – Project documentation PDF
* `UML/` – UML diagrams

## How to Run

Each version has a `run.txt` file in its folder. You can follow the steps there, or manually:

### Console Version

1. Open `Console Based Version` folder
2. Open terminal in this folder
3. Run the commands in `run.txt` or execute:

```bash
mkdir -p bin
javac -d bin src/models/*.java src/util/*.java src/interfaces/*.java src/main/*.java
java -cp bin main.Main
```

### GUI Version

1. Open `GUI Based Version` folder
2. Open terminal in this folder
3. Run the commands in `run.txt` or execute:

```bash
mkdir -p bin
javac -d bin src/models/*.java src/util/*.java src/interfaces/*.java src/main/*.java
java -cp bin main.MainGUI
```

### What These Commands Do

* `mkdir -p bin` → Creates folder for compiled class files (ignores if exists)
* `javac -d bin ...` → Compiles all Java code
* `java -cp bin main.Main` → Runs the Console version
* `java -cp bin main.MainGUI` → Runs the GUI version

## License

MIT License
