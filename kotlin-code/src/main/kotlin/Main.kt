import java.io.FileInputStream
import java.security.KeyPairGenerator
import javax.crypto.Cipher

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    val keyAlgorithm = "DSA"
    val cipherAlgorithm = "DSA"
    // create test data
    val file = "resources/data.txt"
    val origin = FileInputStream(file).readAllBytes().copyOf(245)
    // generate key pair
    val keyGenerator = KeyPairGenerator.getInstance(keyAlgorithm)
    val keyPair = keyGenerator.genKeyPair()
    // encrypt
    val cipher1 = Cipher.getInstance(cipherAlgorithm)
    cipher1.init(Cipher.ENCRYPT_MODE, keyPair.private)
    val encrypted = cipher1.doFinal(origin)
    // decrypt
    val cipher2 = Cipher.getInstance(cipherAlgorithm)
    cipher2.init(Cipher.DECRYPT_MODE, keyPair.public)
    val decrypted = cipher2.doFinal(encrypted)
    println(decrypted.toHexString() == origin.toHexString())
}