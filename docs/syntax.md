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

Booleans are produced by comparisons and logical operations.

```flux
x > 5
x > 0 and x < 10
```

### Arrays

```flux
nums = [10, 20, 30]
```

### Functions

Functions are first-class objects.

```flux
fun greet(name) {
    print "Hello", name
}
```

### Objects

Objects are instances of classes.

```flux
class Point {
    x
    y
}

p = Point()
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

The `+` operator also concatenates strings:

```flux
msg = "Hello" + " " + "World"
print msg
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

## Logical Operators

Flux supports logical operators with short-circuit evaluation:

```flux
and    or    not
```

Example:

```flux
x = 7

if x > 0 and x < 10 {
    print "In range"
}

if not (x == 3) {
    print "Not three"
}
```

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

## Break and Continue

Use `break` to exit a loop early, and `continue` to skip to the next iteration.

```flux
for i = 0 to 10 {
    if i == 5 {
        break
    }
    if i % 2 == 0 {
        continue
    }
    print i
}
```

---

## Functions

Functions are defined using the `fun` keyword.

```flux
fun greet(name) {
    print "Hello", name
}

fun add(a, b) {
    return a + b
}
```

- Functions can have parameters
- Use `return` to return a value (optional)
- Functions are first-class objects and can be assigned to variables

### Calling Functions

```flux
greet("Flux")
result = add(10, 20)
```

---

## Lambda Functions

Anonymous functions (lambdas) can be defined inline.

```flux
double = fun(x) { return x * 2 }
print double(5)
```

---

## Classes and Objects

Classes define blueprints for objects.

```flux
class Point {
    x
    y

    fun init() {
        this.x = 0
        this.y = 0
    }

    fun move(dx, dy) {
        this.x = this.x + dx
        this.y = this.y + dy
    }
}
```

### Creating Objects

```flux
p = Point()
p.move(10, 5)
print p.x, p.y
```

- Fields are declared without types
- Methods are functions defined inside classes
- Use `this` to access the current instance
- The `init` method is called automatically when creating an instance

### Field Access

```flux
p.x = 10
print p.y
```

---

## Built-in Functions

Flux provides several built-in functions:

### len(array)

Returns the length of an array.

```flux
nums = [1, 2, 3]
print len(nums)  # 3
```

### type(value)

Returns the type of a value as a string.

```flux
print type(10)      # "number"
print type("hello") # "string"
print type([1,2])   # "array"
```

### range(start, end)

Creates an array of numbers from start to end-1.

```flux
nums = range(1, 5)  # [1, 2, 3, 4]
```

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
```

---

## Summary

Flux syntax is intentionally minimal and expressive.
It focuses on core programming constructs while remaining easy to read
and ideal for learning how interpreters work internally.