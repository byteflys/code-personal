import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.FileInputStream
import java.security.KeyPairGenerator
import java.security.Security
import java.security.Signature

fun main() {
    val provider = "BC"
    val keyAlgorithm = "ECIES"
    val signAlgorithm = "SHA256withECDSA"
    // register bouncy castle provider
    Security.addProvider(BouncyCastleProvider())
    // create test data
    val file = "resources/data.txt"
    val origin = FileInputStream(file).readAllBytes()
    // generate key pair
    val keyGenerator = KeyPairGenerator.getInstance(keyAlgorithm, provider)
    val keyPair = keyGenerator.genKeyPair()
    // sign
    val sign = Signature.getInstance(signAlgorithm, provider)
    sign.initSign(keyPair.private)
    sign.update(origin)
    val signature = sign.sign()
    // verify
    val verify = Signature.getInstance(signAlgorithm, provider)
    verify.initVerify(keyPair.public)
    verify.update(origin)
    val result = verify.verify(signature)
    println(result)
}