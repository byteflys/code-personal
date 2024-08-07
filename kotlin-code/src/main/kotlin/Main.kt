fun main() {
    val text = "aaaa\${abc}aaaaa"
    val placeholder = "\${abc}"
    println(text.replace(placeholder, " hello "))
}