import com.sun.net.httpserver.HttpsConfigurator
import com.sun.net.httpserver.HttpsServer
import java.io.*
import java.net.InetSocketAddress
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.KeyStore
import java.util.*
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

const val serverKeyStore = "resources/private.keystore"
const val clientKeyStore = "resources/public.truststore"
const val passphrase = "123456"

const val serverPort = 18001

fun main() {
    Thread(::launchHttpServer).start()
    Thread.sleep(1500)
    Thread(::launchHttpClient).start()
}

fun launchHttpServer() {
    // load key manager from key store
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    val keyStore = KeyStore.getInstance("JKS")
    keyStore.load(FileInputStream(serverKeyStore), passphrase.toCharArray())
    keyManagerFactory.init(keyStore, passphrase.toCharArray())
    val keyManagers = keyManagerFactory.keyManagers
    // configure ssl context
    val context = SSLContext.getInstance("TLS")
    context.init(keyManagers, null, null)
    // create https server
    val server = HttpsServer.create(InetSocketAddress(serverPort), 10)
    // configure https
    val configurator = HttpsConfigurator(context)
    server.httpsConfigurator = configurator
    // create service
    server.createContext("/home") { exchange ->
        val response = Date().toString().encodeToByteArray()
        exchange.sendResponseHeaders(200, response.size.toLong())
        exchange.responseBody.write(response)
        exchange.responseBody.close()
    }
    // start https server
    server.start()
}

fun launchHttpClient() {
    // load trust manager from trust store
    val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    val trustStore = KeyStore.getInstance("JKS")
    trustStore.load(FileInputStream(clientKeyStore), passphrase.toCharArray())
    trustManagerFactory.init(trustStore)
    val trustManagers = trustManagerFactory.trustManagers
    // init ssl context
    val context = SSLContext.getInstance("TLS")
    context.init(null, trustManagers, null)
    // create http client
    val uri = URI.create("https://localhost:18001/home")
    val client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .sslContext(context)
        .build()
    // send request
    val request = HttpRequest.newBuilder()
        .GET()
        .uri(uri)
        .build()
    // get response
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    println(response.body())
}