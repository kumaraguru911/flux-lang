# Flux Syntax Guide

This document explains **how to write programs in Flux**.
Flux is designed to be simple, readable, and beginner-friendly, while
still demonstrating core programming language concepts.

---

## General Rules

- Flux is **statement-based**
- Statements are executed **top to bottom**
- Blocks are enclosed using `{ }`
- Whitespace and newlines are not significant
- Flux is **dynamically typed**

---

## Variables and Assignment

Variables are created using assignment.

```flux
x = 10
name = "Flux"
```

- Variables do not need type declarations
- Values are assigned dynamically at runtime

---

## Data Types

Flux supports the following basic data types:

### Numbers

```flux
a = 10
b = 3.5
```

### Strings

```flux
msg = "Hello, Flux"
```

### Booleans

Booleans are produced by comparisons.

```flux
x > 5
```

---

## Printing Output

Use the print statement to display output.

```flux
print x
print "Value of x:", x
```

Multiple expressions can be printed in one statement.

---

## Arrays

Arrays are ordered collections of values.

```flux
nums = [10, 20, 30]
```

### Accessing Array Elements

```flux
print nums[0]
print nums[2]
```

Array indices start at 0.

---

## Arithmetic Expressions

Flux supports standard arithmetic operators:

```flux
+   -   *   /
```

Example:

```flux
result = (10 + 5) * 2
print result
```

---

## Comparison Operators

Flux supports the following comparison operators:

```flux
>    >=    <    <=    ==    !=
```

Example:

```flux
x = 10
x > 5
```

Comparison expressions evaluate to boolean values.

---

## If / Else Statements

Conditional execution is handled using `if` and optional `else`.

```flux
x = 10

if x > 5 {
    print "Big number"
} else {
    print "Small number"
}
```

- Conditions must evaluate to a boolean
- Braces `{ }` are mandatory for blocks

---

## While Loop

The `while` loop executes as long as a condition is true.

```flux
i = 0

while i < 3 {
    print i
    i = i + 1
}
```

---

## For Loop

Flux provides a simple `for` loop for counting.

```flux
for i = 0 to 4 {
    print i
}
```

Internally, `for` loops are transformed into `while` loops.

---

## Exit Statement

The `exit` statement immediately stops program execution.

```flux
exit
```

- Terminates execution cleanly
- No stack trace is printed

---

## Comments

Single-line comments are supported.

```flux
# This is a comment
x = 10  # Inline comment
```

Comments are ignored by the lexer.

---

## Example Program

```flux
nums = [10, 20, 30]
sum = 0

for i = 0 to 2 {
    sum = sum + nums[i]
}

print "Sum =", sum

if sum > 50 {
    print "Big number"
}
```

---

## Summary

Flux syntax is intentionally minimal and expressive.
It focuses on core programming constructs while remaining easy to read
and ideal for learning how interpreters work internally.