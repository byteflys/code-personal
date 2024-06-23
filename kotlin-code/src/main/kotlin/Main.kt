import java.io.FileInputStream
import java.util.Base64

fun main() {
    val file = "resources/data.txt"
    val origin = FileInputStream(file).readAllBytes()
    // encode bytes to base64 string
    val encoder = Base64.getEncoder()
    val encoded = encoder.encodeToString(origin)
    println(encoded)
    // decode base64 string to bytes
    val decoder = Base64.getDecoder()
    val decoded = decoder.decode(encoded)
    println(String(decoded))
}