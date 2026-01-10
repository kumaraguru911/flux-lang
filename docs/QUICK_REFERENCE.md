# Flux Language Enhancement Quick Reference

## New Features Summary

### 1. **Modulo Operator** `%`
```flux
10 % 3      # Returns 1.0 (remainder)
```

### 2. **Array Methods** (8 new methods)
```flux
arr = [1, 2, 3]
arr.push(4)              # Add to end
arr.pop()                # Remove last
arr.shift()              # Remove first  
arr.unshift(0)           # Add to start
arr.len()                # Get length
arr.contains(2)          # Check if contains
arr.indexOf(2)           # Get index
arr.sort()               # Sort array
arr.reverse()            # Reverse array
```

### 3. **String Methods** (6 new methods)
```flux
str = "Hello World"
str.upper()              # Uppercase
str.lower()              # Lowercase
str.split(" ")           # Split by delimiter
str.trim()               # Remove whitespace
str.startsWith("Hello")  # Check prefix
str.endsWith("World")    # Check suffix
str.len()                # Length (existing)
str.substring(0, 5)      # Substring (existing)
```

### 4. **Math Functions** (7 new functions)
```flux
floor(3.7)               # 3.0
ceil(3.2)                # 4.0
round(3.5)               # 4.0
sqrt(16)                 # 4.0
abs(-5)                  # 5.0
min(3, 7)                # 3.0
max(3, 7)                # 7.0
```

### 5. **Type Conversion** (2 new functions)
```flux
toNumber("42")           # 42.0
toString(123)            # "123"
toNumber(true)           # 1.0
toString(false)          # "false"
```

### 6. **Null Support**
```flux
x = null
if x == null {
    print "x is null"
}
```

### 7. **Negative Numbers**
```flux
a = -5
b = -3.14
print a + b              # Works correctly
```

---

## Complete Function List

### Built-in Functions
- `len(array)` - Array length
- `type(value)` - Get value type
- `range(start, end)` - Create number range
- `floor(n)` - Round down
- `ceil(n)` - Round up
- `round(n)` - Round nearest
- `sqrt(n)` - Square root
- `abs(n)` - Absolute value
- `min(a, b)` - Minimum
- `max(a, b)` - Maximum
- `toNumber(v)` - To number
- `toString(v)` - To string

### Array Methods
- `.len()` - Length
- `.push(v)` - Add end
- `.pop()` - Remove end
- `.shift()` - Remove start
- `.unshift(v)` - Add start
- `.contains(v)` - Has value
- `.indexOf(v)` - Find index
- `.sort()` - Sort
- `.reverse()` - Reverse

### String Methods
- `.len()` - Length
- `.substring(s, e)` - Substring
- `.upper()` - Uppercase
- `.lower()` - Lowercase
- `.split(d)` - Split
- `.trim()` - Trim whitespace
- `.startsWith(p)` - Has prefix
- `.endsWith(s)` - Has suffix

---

## Example Usage

```flux
# Array manipulation
numbers = [5, 2, 8, 1, 9]
numbers.sort()
print numbers                    # [1, 2, 5, 8, 9]
print "Length:", numbers.len()   # Length: 5

# String processing  
text = "  Flux Language  "
clean = text.trim().upper()
words = clean.split(" ")
print words                      # ["FLUX", "LANGUAGE"]

# Math operations
result = sqrt(25) + abs(-3)
print result                     # 8.0
print result % 5                 # 3.0

# Type conversion
value = "42"
num = toNumber(value)
print num * 2                    # 84.0

# Null handling
x = null
print x == null                  # true
```

---

## Backward Compatibility

✅ All existing Flux programs continue to work unchanged.  
✅ All new features are additions, not breaking changes.  
✅ Existing examples in `examples/` directory all pass.

---

## Testing

Run the comprehensive demo:
```bash
java -cp bin Main run examples/full_enhancements_demo.flux
```

Test all features:
```bash
java -cp bin Main run examples/enhancements_test.flux
```

---
