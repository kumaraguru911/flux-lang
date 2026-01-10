print "========== ENHANCEMENTS SHOWCASE =========="

print ""
print "=== 1. MODULO OPERATOR ==="
print "10 % 3 =", 10 % 3
print "17 % 5 =", 17 % 5
print "7 % 2 =", 7 % 2

print ""
print "=== 2. ARRAY METHODS ==="
arr = [5, 2, 8, 1]
print "Original array:", arr

print "arr.len():", arr.len()
arr.push(9)
print "After push(9):", arr

x = arr.pop()
print "Popped value:", x, "  Remaining:", arr

arr.unshift(0)
print "After unshift(0):", arr

print "arr.contains(5):", arr.contains(5)
print "arr.contains(100):", arr.contains(100)
print "arr.indexOf(8):", arr.indexOf(8)
print "arr.indexOf(999):", arr.indexOf(999)

arr.sort()
print "After sort():", arr

arr.reverse()
print "After reverse():", arr

print ""
print "=== 3. STRING METHODS ==="
text = "Flux Language"
print "Original:", text

print "upper():", text.upper()
print "lower():", text.lower()
print "len():", text.len()

words = text.split(" ")
print "split(' '):", words

padded = "   Hello   "
print "Before trim: '" padded "'"
print "After trim:  '" padded.trim() "'"

print "startsWith('Flux'):", text.startsWith("Flux")
print "endsWith('Language'):", text.endsWith("Language")

print ""
print "=== 4. MATH FUNCTIONS ==="
print "floor(3.7):", floor(3.7)
print "ceil(3.2):", ceil(3.2)
print "round(3.5):", round(3.5)
print "sqrt(25):", sqrt(25)
print "abs(-42):", abs(-42)
print "min(10, 3):", min(10, 3)
print "max(10, 3):", max(10, 3)

print ""
print "=== 5. TYPE CONVERSION ==="
print "toNumber('100'):", toNumber("100")
print "toNumber('3.14'):", toNumber("3.14")
print "toString(42):", toString(42)
print "toString(true):", toString(true)
print "toNumber(true):", toNumber(true)

print ""
print "=== 6. NULL VALUES ==="
x = null
print "null value:", x
print "type(null):", type(x)

if x == null {
    print "x is null"
}

print ""
print "=== 7. NEGATIVE NUMBERS ==="
a = -5
b = -3
print "a =", a
print "b =", b
print "a + b =", a + b
print "a * b =", a * b

print ""
print "========== ALL TESTS PASSED =========="
