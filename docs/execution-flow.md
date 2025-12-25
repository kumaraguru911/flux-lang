# Flux Execution Flow

This document explains **how a Flux program executes internally**, from the
moment a user runs a command to the point where the program terminates.
It complements the architecture document by focusing on **runtime behavior**.

---

## Overview

Flux executes programs using a linear, interpreter-based workflow.
Each stage processes the output of the previous stage, ensuring clarity
and debuggability at every step.

---

## Step-by-Step Execution Pipeline

Flux follows this execution sequence:

Command Invocation  
→ Source Loading  
→ Lexer  
→ Parser  
→ AST  
→ Interpreter  
→ Runtime Environment  
→ Program Termination  

---

## 1. Command Invocation

A Flux program is executed via the command-line interface (CLI):

```bash
flux run program.flux
# On Windows:
.\flux run program.flux
```

The CLI determines the execution mode:

- `run` → normal execution
- `ast` → AST visualization
- `trace` → execution tracing
- `env` → environment inspection
- `repl` → interactive session

---

## 2. Source Code Loading

Responsibility:

- Reads the .flux source file into memory as plain text

Key points:

- No preprocessing
- No compilation step
- The source code is passed directly to the lexer

This keeps Flux transparent and easy to reason about.

---

## 3. Lexical Analysis (Lexer)

Responsibility:

- Converts raw source code into a sequence of tokens

Example:

```flux
x = 10
```

Tokenized as:

```
IDENTIFIER(x) EQUAL NUMBER(10)
```

The lexer abstracts away characters and whitespace, producing a clean
stream of meaningful symbols for the parser.

---

## 4. Parsing (Syntax Analysis)

Responsibility:

- Validates token order against Flux grammar
- Builds the Abstract Syntax Tree (AST)

Important behavior:

- `for` loops are internally transformed into `while` loops
- This reduces interpreter complexity while preserving expressiveness

Syntax errors are detected at this stage.

---

## 5. Abstract Syntax Tree (AST)

Responsibility:

- Represents the program's structure independently of source formatting
- Acts as the central data structure for interpretation

The AST can be inspected using:

```bash
flux ast program.flux
```

This feature is especially useful for learning and debugging.

---

## 6. Interpretation (Execution)
Responsibility:

Walks the AST node by node

Executes statements sequentially

Evaluates expressions recursively

Key behaviors:

- Control flow (`if`, `while`, `for`) is resolved at runtime
- Expressions are evaluated dynamically
- Execution trace mode logs internal decisions

Trace mode:

```bash
flux trace program.flux
```

---

## 7. Runtime Environment Management
Responsibility:

Stores variable names and their current values

Maintains program state throughout execution

Capabilities:

- Supports numbers, strings, arrays, and indexing
- Detects undefined variables
- Provides environment inspection:

```bash
flux env program.flux
```

---

## 8. Program Termination

A Flux program terminates in one of two ways:

- Natural completion (end of file)
- Explicit termination using the `exit` statement

The `exit` statement stops execution cleanly without producing stack traces.

---

## Summary

Flux executes programs through a clear, staged interpreter pipeline.
Each step transforms the program into a more structured and meaningful
representation, making the execution model easy to understand,
debug, and extend.