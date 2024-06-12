package com.easing.commons.android.websocket;

import com.easing.commons.android.manager.Resources;

import org.java_websocket.server.DefaultSSLWebSocketServerFactory;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import lombok.SneakyThrows;

public class WebSocketServerSSL {

    @SneakyThrows
    public static SSLContext sslConextFromKeystore() {
        String storetype = "PKCS12";
        String keystore = "ssl/keystore.p12";
        String keypass = "123456";
        String storepass = "123456";

        KeyStore keyStore = KeyStore.getInstance(storetype);
        keyStore.load(Resources.readAssetStream(keystore), storepass.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(keyStore, keypass.toCharArray());
        TrustManagerFactory factory = TrustManagerFactory.getInstance("X509");
        factory.init(keyStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), factory.getTrustManagers(), null);
        return sslContext;
    }

    @SneakyThrows
    public static DefaultSSLWebSocketServerFactory sslFactory() {
        SSLContext sslContext = WebSocketServerSSL.sslConextFromKeystore();
        DefaultSSLWebSocketServerFactory websocketServerFactory = new DefaultSSLWebSocketServerFactory(sslContext);
        return websocketServerFactory;
    }
}
