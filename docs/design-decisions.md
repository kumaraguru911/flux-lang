
# Flux Design Decisions

This document explains the **key design decisions** behind Flux.
Rather than focusing only on *what* Flux does, this file explains *why*
certain choices were made. This demonstrates engineering judgment,
tradeoff analysis, and intentional design.

---

## Why Build Flux From Scratch?

Flux was built entirely from scratch to deeply understand how
programming languages work internally.

Goals:
- Learn interpreter design fundamentals
- Avoid framework abstractions
- Expose every stage of execution
- Keep the system inspectable and educational

---

## Interpreter Instead of Compiler

Flux is implemented as an **interpreter**, not a compiler.

### Reasoning:
- Interpreters are simpler to implement and reason about
- They allow immediate execution and REPL support
- They align well with educational goals

This choice makes Flux ideal for learning how languages evaluate code
step by step.

---

## Dynamic Typing

Flux uses **dynamic typing**.

Example:
```flux
x = 10
x = "hello"
```

Reasoning:
- Reduces parser and type system complexity
- Allows focus on execution logic
- Improves beginner experience

---

## Explicit AST Representation

Flux builds and operates on an explicit Abstract Syntax Tree (AST).

Why this matters:
- Separates syntax from execution
- Makes interpretation clean and recursive
- Enables AST visualization (`flux ast`)
- Simplifies debugging and extension

The AST acts as the core contract between parsing and execution.

---

## For-Loop Desugaring

Flux supports for loops, but they are desugared internally into
while loops.

```flux
for i = 0 to 3 {
    print i
}
```

Internally behaves like:

```flux
i = 0
while i <= 3 {
    print i
    i = i + 1
}
```

Reasoning:
- Keeps interpreter small and focused
- Avoids duplicating loop execution logic
- Demonstrates real-world compiler techniques

---

## Nested Scopes and Environments

Flux supports nested environments for functions, classes, and blocks.

Reasoning:
- Enables proper function closures
- Supports object-oriented method binding
- Maintains lexical scoping
- Allows for more expressive programming

This extends the single environment model while keeping scope management clear.

---

## Functions and Closures

Flux includes first-class functions with closure support.

Reasoning:
- Demonstrates advanced interpreter concepts
- Enables functional programming patterns
- Supports lambda expressions
- Teaches about environment capture

Functions are implemented with proper parameter passing and return values.

---

## Object-Oriented Features

Flux supports classes, objects, and methods without inheritance.

Reasoning:
- Shows how object-oriented programming works internally
- Demonstrates method binding and `this` context
- Keeps the model simple (no inheritance complexity)
- Educates about instance creation and field access

---

## Graceful Exit Handling

Flux implements exit using a controlled runtime signal.

Benefits:
- No Java stack traces exposed to users
- Clean program termination
- Works consistently across CLI and REPL

This improves user experience and error clarity.

---

## Tooling-Focused Features

Flux includes tooling-oriented commands:

- `ast` → inspect program structure
- `trace` → observe execution
- `env` → inspect runtime state
- `repl` → interactive execution

Reasoning:
- Makes internal behavior visible
- Reinforces learning objectives
- Encourages experimentation

These tools do not change the language itself, only how it is observed.

---

## Built-in Functions

Flux includes built-in functions (`len`, `type`, `range`) implemented in Java.

Reasoning:
- Demonstrates native function integration
- Provides useful utilities without language complexity
- Shows how host language features can extend the interpreted language

---

## Logical Operators with Short-Circuiting

Flux implements `and`, `or`, and `not` with short-circuit evaluation.

Reasoning:
- Matches common programming language behavior
- Improves performance by avoiding unnecessary evaluations
- Demonstrates control flow in expression evaluation

---

## Summary

Every design decision in Flux is intentional.
The language favors clarity, inspectability, and educational value over
complexity. This makes Flux a strong foundation for learning interpreter
design and a solid base for future extensions.