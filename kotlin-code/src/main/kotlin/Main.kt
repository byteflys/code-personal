import java.io.FileInputStream
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    // create test data
    val file = "resources/data.txt"
    val origin = FileInputStream(file).readAllBytes()
    // generate encrypt key
    val generator = KeyGenerator.getInstance("DES")
    val secretKey1 = generator.generateKey()
    // encrypt
    val cipher1 = Cipher.getInstance("DES")
    cipher1.init(Cipher.ENCRYPT_MODE, secretKey1)
    val encrypted = cipher1.doFinal(origin)
    // generate decrypt key
    val keySpec = DESKeySpec(secretKey1.encoded)
    val keyFactory = SecretKeyFactory.getInstance("DES")
    val secretKey2 = keyFactory.generateSecret(keySpec)
    println(secretKey2 == secretKey1)
    // decrypt
    val cipher2 = Cipher.getInstance("DES")
    cipher2.init(Cipher.DECRYPT_MODE, secretKey2)
    val decrypted = cipher2.doFinal(encrypted)
    println(decrypted.toHexString() == origin.toHexString())
}