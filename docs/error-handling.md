# Flux Error Handling Guide

Flux is designed to provide **clear, beginner-friendly error messages**
while maintaining safe and predictable execution behavior. This document
describes how Flux detects, reports, and handles different classes of errors.

---

## Error Categories

Flux errors are divided into two main categories:

1. **Syntax Errors** — detected during parsing
2. **Runtime Errors** — detected during execution

This separation helps users understand whether an error comes from
*how the program is written* or *how it behaves at runtime*.

---

## Syntax Errors

Syntax errors occur when Flux source code violates grammar rules.

### Examples of Syntax Errors

```flux
x =
if x > 5
    print x
```

### Behavior

- Detected during parsing
- Execution does not start
- Error messages include line numbers and context

Example message:

```
[Flux Syntax Error]
Expected expression after '=' at line 3.
```

---

## Runtime Errors
Runtime errors occur while executing a syntactically valid program.

Flux stops execution immediately when a runtime error is encountered.

---

### Undefined Variable Errors

Occurs when accessing a variable that has not been defined.

```flux
print x
```

Error message:

```
[Flux Runtime Error]
Undefined variable 'x'.
```

---

### Type Errors

Occurs when an operation is applied to incompatible types.

```flux
x = "hello"
y = x + 10
```

Error message:

```
[Flux Runtime Error]
Operands must be numbers.
```

---

### Array Index Errors

Occurs when accessing an array with an invalid index.

```flux
nums = [1, 2, 3]
print nums[5]
```

Error message:

```
[Flux Runtime Error]
Array index out of bounds (index = 5, size = 3).
```

---

### Invalid Array Access

Occurs when attempting to index a non-array value.

```flux
x = 10
print x[0]
```

Error message:

```
[Flux Runtime Error]
Not an array.
```

---

### Control Flow Errors

#### Exit Statement

The exit statement terminates execution immediately.

```flux
exit
```

Behavior:

- Execution stops cleanly
- No stack trace is printed
- Used intentionally for program termination

---

### REPL Error Handling

In REPL mode:

- Errors are printed
- The REPL continues running
- Program state is preserved unless exit is called

Example:

```
> print y
[Flux Runtime Error]
Undefined variable 'y'.
> x = 10
> print x
10
```

---

## Design Philosophy

Flux error handling follows these principles:

- Fail fast — stop execution on errors
- Clear messages — no cryptic stack traces
- Beginner-friendly — explain what went wrong
- Safe termination — no undefined behavior

---

## Summary

Flux provides structured, predictable, and readable error handling.
By separating syntax and runtime errors and offering clear messages,
Flux helps users understand and correct mistakes efficiently.
