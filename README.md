# Flux Programming Language

Flux is a **beginner-friendly, interpreter-based programming language**
built completely **from scratch** using core Java.  
It is designed to demonstrate how programming languages work internally,
from lexical analysis to runtime execution.

Flux focuses on **clarity, inspectability, and learning**, making it ideal
for students, beginners, and anyone curious about language design.

![Flux Workflow](images/workflow.png)

---

## ✨ Features

### Core Language
- Built entirely from scratch (no external frameworks)
- Lexer → Parser → AST → Interpreter pipeline
- Dynamic typing with null/nil support
- Variables and expressions with negative number support
- Comments (`#`)
- Beginner-friendly error messages

### Data Types & Collections
- Arrays with indexed access and **8 methods** (push, pop, sort, reverse, etc.)
- Dictionaries/Maps with key-value access
- Strings with **8 methods** (upper, lower, split, trim, startsWith, endsWith, etc.)
- Numbers with **modulo operator** (`%`)

### Control Flow
- Conditional statements (`if / else`)
- Loops (`while`, `for`) with `break` and `continue`
- Logical operators (`and`, `or`, `not`) with short-circuit evaluation

### Functions & OOP
- Named and anonymous/lambda functions
- Functions with closures and lexical scoping
- Classes and object-oriented programming
- Methods and `this` keyword

### Built-in Functions (12 total)
- **Type/Array**: `len()`, `type()`, `range()`
- **Math**: `floor()`, `ceil()`, `round()`, `sqrt()`, `abs()`, `min()`, `max()`
- **Conversion**: `toNumber()`, `toString()`

### Other Features
- Graceful program termination (`exit`)

---

## 🔍 Internal Power (WOW Features)

Flux exposes its internals through powerful tooling:

- **AST Printer** – visualize program structure  
- **Execution Trace Mode** – observe interpreter decisions step by step  
- **Environment Dump** – inspect runtime variables  
- **REPL** – interactive Read–Eval–Print Loop  

These features make Flux an excellent **educational language**.

---

## 🚀 Recent Enhancements (January 2026)

The runtime has been significantly enhanced with:

- ✅ **Modulo Operator** – `%` for remainder operations
- ✅ **Array Methods** – `push()`, `pop()`, `shift()`, `unshift()`, `sort()`, `reverse()`, `contains()`, `indexOf()`
- ✅ **String Methods** – `upper()`, `lower()`, `split()`, `trim()`, `startsWith()`, `endsWith()`
- ✅ **Math Functions** – `floor()`, `ceil()`, `round()`, `sqrt()`, `abs()`, `min()`, `max()`
- ✅ **Type Conversion** – `toNumber()`, `toString()`
- ✅ **Null Support** – Proper null/nil value handling
- ✅ **Negative Numbers** – Unary minus operator support

See [ENHANCEMENTS.md](ENHANCEMENTS.md) and [QUICK_REFERENCE.md](QUICK_REFERENCE.md) for complete details.

---

## 📁 Project Structure

```
Flux/
├── src/
│ ├── lexer/
│ ├── parser/
│ ├── ast/
│ ├── interpreter/
│ ├── runtime/
│ └── Main.java
├── examples/
│ └── basics.flux
├── docs/
│ ├── architecture.md
│ ├── execution-flow.md
│ ├── syntax.md
│ ├── cli.md
│ ├── error-handling.md
│ └── design-decisions.md
├── flux.bat / flux.cmd
└── README.md
```

---

## 🚀 Getting Started

### Compile Flux

From the `src` directory:

```bash
javac Main.java lexer/*.java parser/*.java ast/*.java interpreter/*.java runtime/*.java
```

### ▶️ Running Flux Programs

From the project root:

```bash
.\flux run examples/basics.flux
```

---

## 🧰 Command-Line Interface (CLI)

Flux provides several CLI commands:

### Run a program

```bash
.\flux run program.flux
```

### View Abstract Syntax Tree

```bash
.\flux ast program.flux
```

### Execution Trace Mode

```bash
.\flux trace program.flux
```

### Runtime Environment Dump

```bash
.\flux env program.flux
```

### Start Interactive REPL

```bash
.\flux repl
```

---

## 🧪 Example Flux Program

```flux
class Counter {
    fun init() {
        this.value = 0
    }

    fun inc() {
        this.value = this.value + 1
        return this
    }
}

fun add(a, b) {
    return a + b
}

c = Counter()
c.inc().inc()
print "Count:", c.value

nums = range(1, 5)
sum = 0

for i = 0 to len(nums) - 1 {
    sum = add(sum, nums[i])
}

print "Sum:", sum

if sum > 10 and not (sum == 0) {
    print "Big sum"
}

# Maps and string methods
person = {"name": "Alice", "age": 30}
print person["name"]

greeting = "Hello, World!"
print greeting.len()
print greeting.substring(0, 5)
```

## 🎯 Modern Features Example

```flux
# Enhanced array operations
arr = [5, 2, 8, 1, 9]
arr.sort()
print "Sorted:", arr                          # [1, 2, 5, 8, 9]
print "Contains 5:", arr.contains(5)          # true

# Enhanced string operations
text = "  Flux Language  "
clean = text.trim().upper()
words = clean.split(" ")
print "Words:", words                         # ["FLUX", "LANGUAGE"]

# Math operations with modulo
for i = 1 to 10 {
    if i % 2 == 0 {
        print i, "is even"
    }
}

# Type conversion
value = "42"
num = toNumber(value)
print "Double:", num * 2                     # 84.0

# Null handling
x = null
if x == null {
    print "x is null"
}
```

---

## 📘 Documentation

Detailed documentation is available:

**Core Documentation** (in `docs/` folder):
- [Architecture Overview](docs/architecture.md)
- [Execution Flow](docs/execution-flow.md)
- [Language Syntax](docs/syntax.md)
- [CLI Reference](docs/cli.md)
- [Error Handling](docs/error-handling.md)
- [Design Decisions](docs/design-decisions.md)

**Enhancement Documentation**:
- [ENHANCEMENTS.md](ENHANCEMENTS.md) – Detailed enhancement guide
- [QUICK_REFERENCE.md](QUICK_REFERENCE.md) – Quick feature reference
- [IMPLEMENTATION_COMPLETE.md](IMPLEMENTATION_COMPLETE.md) – Implementation summary

---

## 🎯 Design Goals

- Teach how interpreters work internally
- Keep syntax simple and readable
- Expose internal execution details
- Avoid unnecessary complexity
- Build everything from first principles

---

## 🧠 Learning Outcomes

Building Flux involved learning:

- Lexical analysis and tokenization
- Recursive descent parsing
- AST construction and traversal
- Interpreter design
- Runtime environment management
- Object-oriented features (classes, methods, inheritance-free)
- CLI tooling and REPL design

---

## 📚 Test Your Knowledge

Run the comprehensive enhancement demo:

```bash
.\flux run examples/full_enhancements_demo.flux
```

This will showcase all new features in action!

---

## 🏁 Conclusion

Flux is not just a toy language — it is a **complete, inspectable interpreter**
designed to demonstrate how programming languages work under the hood.
With recent enhancements, it now includes:

- ✅ Comprehensive built-in functions
- ✅ Rich array and string methods
- ✅ Modern language features (null support, modulo, negative numbers)
- ✅ Full educational value through inspectable internals

It serves as both a **learning tool** and a **strong portfolio project**.

Built with ❤️ to learn how languages really work.