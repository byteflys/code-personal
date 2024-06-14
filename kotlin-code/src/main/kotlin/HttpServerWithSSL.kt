import com.sun.net.httpserver.HttpsConfigurator
import com.sun.net.httpserver.HttpsServer
import java.io.*
import java.net.InetSocketAddress
import java.security.KeyStore
import java.util.*
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession
import javax.net.ssl.X509TrustManager
import okhttp3.OkHttpClient
import okhttp3.Request

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
    val url = "https://localhost:18001/home"
    // create okhttp client
    val builder = OkHttpClient.Builder()
    setOkHttpSSL(builder)
    val client = builder.build()
    // create request
    val request = Request.Builder()
        .url(url)
        .get()
        .build()
    // execute call
    val call = client.newCall(request)
    val response = call.execute()
    // print response
    val responseBody = response.body.string()
    println(responseBody)
}

fun setOkHttpSSL(builder: OkHttpClient.Builder) {
    val trustManager = object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>, authType: String) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>, authType: String) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
    }
    val trustManagers = arrayOf(trustManager)
    val hostnameVerifier = HostnameVerifier { hostname: String, session: SSLSession -> true }
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustManagers, null)
    val socketFactory = sslContext.socketFactory
    builder.sslSocketFactory(socketFactory, trustManager)
    builder.hostnameVerifier(hostnameVerifier)
}