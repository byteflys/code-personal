import org.apache.commons.codec.digest.HmacUtils

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    val mac = HmacUtils.getInitializedMac("HMacMD5", byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 11))
    val encrypted = mac.doFinal()
    println(encrypted.toHexString())
}


