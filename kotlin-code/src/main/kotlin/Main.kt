import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.FileInputStream
import java.security.KeyPairGenerator
import java.security.Security
import javax.crypto.Cipher

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    val provider = "BC"
    val keyAlgorithm = "ECIES"
    val cipherAlgorithm = "ECIES"
    // register bouncy castle provider
    Security.addProvider(BouncyCastleProvider())
    // create test data
    val file = "resources/data.txt"
    val origin = FileInputStream(file).readAllBytes()
    // generate key pair
    val keyGenerator = KeyPairGenerator.getInstance(keyAlgorithm, provider)
    val keyPair = keyGenerator.genKeyPair()
    // encrypt
    val cipher1 = Cipher.getInstance(cipherAlgorithm, provider)
    cipher1.init(Cipher.ENCRYPT_MODE, keyPair.public)
    val encrypted = cipher1.doFinal(origin)
    // decrypt
    val cipher2 = Cipher.getInstance(cipherAlgorithm, provider)
    cipher2.init(Cipher.DECRYPT_MODE, keyPair.private)
    val decrypted = cipher2.doFinal(encrypted)
    println(decrypted.toHexString() == origin.toHexString())
}