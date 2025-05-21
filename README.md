# Taxonomy Maker

Its just a node graph, dude. Click on a node in the tree, edit the data. Deleted nodes delete all children. Originally created out of frustration while working on a project involving speculative xenobiology.

---

## 📦 Build Instructions

### Prerequisites

- Java JDK 8 or higher
- Unix-like shell (Linux, macOS, WSL)
- Optional `.jar` dependencies in `lib/`

### Directory Structure
project-root/<br>
├── build.sh<br>
├── run_tests.sh<br>
├── update_libs.sh<br>
├── lib.config<br>
├── src/<br>
│ ├── main/<br>
│ │ ├── java/<br>
│ │ └── resources/ # optional<br>
│ └── test/<br>
│ └── Test.java # or any other test runner class<br>
├── lib/ # auto-managed<br>
├── dist/ # generated output JARs<br>
├── bin/ # intermediate class files<br>
└── test/ # compiled test classes<br>

---

## 🔧 `build.sh` – Building the Project

This script compiles your Java source files, resolves the main class, and produces a self-contained JAR.

### Usage

```bash
chmod +x build.sh
./build.sh
```
Outputs a JAR file to dist/ named like: YourProject_May212025.jar

```bash
java -jar "$(./build.sh)"
```
Will build and run the application in one line.

Automatically:
- Extracts non-JUnit dependencies into bin/
- Compiles Java source from src/main/java
- Copies resources from src/main/resources (if any)
- Resolves and sets the main class in the JAR manifest
## 📚 `update_libs.sh` – Managing Dependencies

Use this script to populate the `lib/` folder by fetching `.jar` files from Maven Central or GitHub Releases.

### Usage

```bash
chmod +x update_libs.sh
./update_libs.sh
```
### Configuration File: lib.config

Specify dependencies per line.
#### Maven Format:
```bash
group.id:artifact-id
group.id:artifact-id:classifier
group.id:artifact-id:classifier:version
```
Examples:
```bash
org.slf4j:slf4j-api
com.google.code.gson:gson::2.8.9
```

#### Github Format:
```bash
github:owner/repo:asset-name-substring
```
Examples:
```bash
github:SomeUser/SomeRepo:natives-linux.jar
```
### Behavior
- Clears the lib/ and extracted_jars/ directories before downloading
- Automatically creates lib/ if it doesn't exist
- Resolves latest versions from Maven if not explicitly provided
- Adds optional classifier support (e.g., native libraries)

## ✅ `run_tests.sh` – Compiling & Running Tests

This script builds the project, compiles test classes, and executes a test runner.

### Usage

```bash
chmod +x run_tests.sh
./run_tests.sh
```
### Behavior
- Invokes build.sh and captures the path to the generated JAR
- Compiles test sources from src/test/ into the test/ directory
- Uses the built JAR and any libraries in lib/ as the classpath
- Executes the test.Test class, passing the JAR path as a command-line argument

### Output
- Compilation errors (if any) are printed to the terminal
- The test runner (test.Test) is executed and its output is shown

### 🧰 Notes

- Assumes a `public static void main(String[] args)` method exists in `test.Test`
- The script expects `junit` or other test dependencies to be available in `lib/`
- The test JAR path is passed to the test runner as its first argument
- `test/` is used only for compiled test class output and is cleared on each run
