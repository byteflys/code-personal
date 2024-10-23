import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.X509TrustManager

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