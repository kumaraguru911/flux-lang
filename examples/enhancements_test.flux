# Test modulo operator
print 10 % 3

# Test array methods
arr = [1, 2, 3]
arr.push(4)
print arr
print arr.len()

# Test array pop
val = arr.pop()
print "Popped:", val
print arr

# Test string methods
str = "Hello World"
print str.upper()
print str.lower()
print str.split(" ")

# Test string trim
padded = "  test  "
print padded.trim()

# Test math functions
print floor(3.7)
print ceil(3.2)
print round(3.5)
print sqrt(16)
print abs(-5)
print min(10, 5)
print max(10, 5)

# Test type conversion
print toNumber("42")
print toString(123)

# Test null
x = null
print x
