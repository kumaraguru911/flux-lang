# Flux Architecture Overview

Flux is implemented as a classic, educational interpreter pipeline.
The architecture is intentionally kept simple and modular to clearly
demonstrate how programming languages work internally.

The system is divided into well-defined stages, each with a single
responsibility.

---

## High-Level Pipeline

Flux follows this execution flow:

Source Code  
→ Lexer (Tokenization)  
→ Parser (Syntax Analysis)  
→ AST (Program Structure)  
→ Interpreter (Execution)  
→ Runtime Environment  

Each stage transforms data into a more structured representation.

---

## Component Breakdown

### 1. Lexer (Lexical Analysis)

**Responsibility:**
- Reads raw Flux source code
- Converts characters into meaningful tokens

**Examples of tokens:**
- Keywords (`if`, `while`, `for`, `print`, `exit`)
- Identifiers (`x`, `sum`, `nums`)
- Literals (`10`, `"hello"`)
- Operators (`+`, `-`, `>`, `==`)

**Why this matters:**
Separating lexing from parsing simplifies syntax analysis and error handling.

---

### 2. Parser (Syntax Analysis)

**Responsibility:**
- Consumes tokens produced by the lexer
- Builds an Abstract Syntax Tree (AST)
- Enforces language grammar rules

**Key design choice:**
- `for` loops are desugared internally into `while` loops
- This keeps the interpreter simpler while supporting higher-level syntax

---

### 3. Abstract Syntax Tree (AST)

**Responsibility:**
- Represents the program structure as a tree
- Decouples syntax from execution

**Why AST is important:**
- Enables AST visualization (`flux ast`)
- Makes interpretation logic clean and extensible
- Separates language structure from runtime behavior

---

### 4. Interpreter

**Responsibility:**
- Walks the AST node by node
- Executes statements sequentially
- Evaluates expressions recursively

**Key features:**
- Clear separation of statements and expressions
- Execution trace mode for educational insight
- Graceful program termination via `exit`

---

### 5. Runtime Environment

**Responsibility:**
- Stores variable names and their values
- Maintains program state during execution

**Additional capabilities:**
- Environment dump (`flux env`) for runtime inspection
- Supports arrays and indexed access
- Handles undefined variable errors safely

---

## Design Principles

Flux is designed with the following principles:

- **Separation of concerns**  
  Each component has a single responsibility.

- **No external frameworks**  
  Everything is built from scratch using core Java.

- **Beginner-first clarity**  
  Code prioritizes readability over cleverness.

- **Extensibility**  
  New language features can be added without major refactoring.

---

## Summary

The Flux architecture mirrors real-world interpreter designs while
remaining approachable for learning purposes. By cleanly separating
lexing, parsing, AST construction, and execution, Flux demonstrates
how programming languages operate under the hood.
