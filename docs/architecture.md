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
- Keywords (`if`, `while`, `for`, `print`, `exit`, `fun`, `class`, `return`, `this`, `and`, `or`, `not`, `break`, `continue`)
- Identifiers (`x`, `sum`, `nums`)
- Literals (`10`, `"hello"`, `true`, `false`)
- Operators (`+`, `-`, `>`, `==`, `=`, `.`, `[`, `]`)
- Symbols (`(`, `)`, `{`, `}`, `,`, `#`)

**Why this matters:**
Separating lexing from parsing simplifies syntax analysis and error handling.

---

### 2. Parser (Syntax Analysis)

**Responsibility:**
- Consumes tokens produced by the lexer
- Builds an Abstract Syntax Tree (AST)
- Enforces language grammar rules

**Key design choices:**
- `for` loops are desugared internally into `while` loops
- Functions and classes are parsed with proper scoping
- Logical operators support short-circuit evaluation
- Method calls are handled via dot notation

---

### 3. Abstract Syntax Tree (AST)

**Responsibility:**
- Represents the program structure as a tree
- Decouples syntax from execution

**Supported constructs:**
- Statements: Print, Expression, Assignment, If/Else, While, For, Function, Class, Return, Break, Continue, Exit
- Expressions: Binary, Unary, Literal, Variable, Call, Get, Set, Array, Index, Lambda, Logical

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
- Dynamic typing with runtime type checking
- Function calls with parameter passing and return values
- Class instantiation and method binding
- Array operations and indexing
- Logical operations with short-circuiting
- Loop control with break/continue
- Execution trace mode for educational insight
- Graceful program termination via `exit`

---

### 5. Runtime Environment

**Responsibility:**
- Stores variable names and their values
- Maintains program state during execution
- Supports nested scopes for functions and blocks

**Additional capabilities:**
- Environment dump (`flux env`) for runtime inspection
- Supports arrays, objects, and functions as first-class values
- Handles undefined variable errors safely
- Built-in functions (`len`, `type`, `range`)

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
