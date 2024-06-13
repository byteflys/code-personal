import java.io.*
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLServerSocket
import javax.net.ssl.SSLSocket
import javax.net.ssl.TrustManagerFactory

const val serverKeyStore = "resources/private.keystore"
const val clientKeyStore = "resources/public.truststore"
const val passphrase = "123456"

const val serverPort = 18001

fun main() {
    Thread(::launchServerSocket).start()
    Thread.sleep(500)
    Thread(::launchClientSocket).start()
}

fun launchServerSocket() {
    // load key manager from key store
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    val keyStore = KeyStore.getInstance("JKS")
    keyStore.load(FileInputStream(serverKeyStore), passphrase.toCharArray())
    keyManagerFactory.init(keyStore, passphrase.toCharArray())
    val keyManagers = keyManagerFactory.keyManagers
    // init ssl context
    val context = SSLContext.getInstance("TLS")
    context.init(keyManagers, null, null)
    // create server socket
    val serverSocketFactory = context.serverSocketFactory
    val serverSocket = serverSocketFactory.createServerSocket(serverPort) as SSLServerSocket
    serverSocket.needClientAuth = false
    // accept client session
    val socket = serverSocket.accept() as SSLSocket
    // communication with client
    while (true) {
        Thread.sleep(500)
        socket.getOutputStream().write("Hello World".encodeToByteArray())
        socket.getOutputStream().flush()
    }
}

fun launchClientSocket() {
    // load trust manager from trust store
    val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    val trustStore = KeyStore.getInstance("JKS")
    trustStore.load(FileInputStream(clientKeyStore), passphrase.toCharArray())
    trustManagerFactory.init(trustStore)
    val trustManagers = trustManagerFactory.trustManagers
    // init ssl context
    val context = SSLContext.getInstance("TLS")
    context.init(null, trustManagers, null)
    // create client socket and auto connect
    val sslSocketFactory = context.socketFactory
    val socket = sslSocketFactory.createSocket("localhost", serverPort) as SSLSocket
    // communication with server
    val buffer = ByteArray(1024)
    while (true) {
        val len = socket.getInputStream().read(buffer)
        if (len > 0) {
            val message = String(buffer, 0, len)
            println("Client Received: $message")
        }
    }
}