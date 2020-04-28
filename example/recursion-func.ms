func recursion(int n) int {
    if(n <= 0) {
        return 1
    }
    return recursion(n - 1) * n
}