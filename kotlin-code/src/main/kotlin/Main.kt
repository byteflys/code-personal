import java.io.FileInputStream
import java.security.KeyPairGenerator
import java.security.Signature

fun main() {
    val keyAlgorithm = "DSA"
    val signAlgorithm = "SHA256withDSA"
    // create test data
    val file = "resources/data.txt"
    val origin = FileInputStream(file).readAllBytes()
    // generate key pair
    val keyGenerator = KeyPairGenerator.getInstance(keyAlgorithm)
    val keyPair = keyGenerator.genKeyPair()
    // sign
    val sign = Signature.getInstance(signAlgorithm)
    sign.initSign(keyPair.private)
    sign.update(origin)
    val signature = sign.sign()
    // verify
    val verify = Signature.getInstance(signAlgorithm)
    verify.initVerify(keyPair.public)
    verify.update(origin)
    val result = verify.verify(signature)
    println(result)
}