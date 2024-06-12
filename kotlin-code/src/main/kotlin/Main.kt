import java.io.*
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLServerSocket
import javax.net.ssl.TrustManagerFactory

const val serverKeyStore = "server.jks"
const val clientKeyStore = "client.jks"
const val passphrase = "passphrase"

const val serverPort = 18001

fun main() {
    Thread(::launchServerSocket).start()
    Thread.sleep(500)
    Thread(::launchClientSocket).start()
}

fun launchServerSocket() {
    val context = SSLContext.getInstance("TLS")
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    val keyStore = KeyStore.getInstance("JKS")
    keyStore.load(FileInputStream(serverKeyStore), passphrase.toCharArray())
    keyManagerFactory.init(keyStore, passphrase.toCharArray())
    val keyManagers = keyManagerFactory.keyManagers
    context.init(keyManagers, null, null)
    val serverSocketFactory = context.serverSocketFactory
    val serverSocket = serverSocketFactory.createServerSocket(serverPort) as SSLServerSocket
    serverSocket.needClientAuth = false
    val socket = serverSocket.accept()
    while (true) {
        Thread.sleep(500)
        socket.getOutputStream().write("Hello World".encodeToByteArray())
    }
}

fun launchClientSocket() {
    val context = SSLContext.getInstance("TLS")
    val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    val keyStore = KeyStore.getInstance("JKS")
    keyStore.load(FileInputStream(clientKeyStore), passphrase.toCharArray())
    trustManagerFactory.init(keyStore)
    val trustManagers = trustManagerFactory.trustManagers
    context.init(null, trustManagers, null)
    val sslSocketFactory = context.socketFactory
    val socket = sslSocketFactory.createSocket("localhost", serverPort)
    while (true) {
        val bytes = socket.getInputStream().readAllBytes()
        if (bytes.isNotEmpty())
            println("Client Received: ${bytes.decodeToString()}")
    }
}