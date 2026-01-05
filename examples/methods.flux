class Counter {
    value
    fun inc() {
        this.value = this.value + 1
        return this
    }
}

c = Counter()
c.inc().inc()
print c.value
