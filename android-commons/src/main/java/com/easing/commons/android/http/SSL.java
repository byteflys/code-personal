package com.easing.commons.android.http;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.*;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class SSL {

    @SneakyThrows
    public static SSLContext sslContext() {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        return sslContext;
    }

    @SneakyThrows
    public static SSLSocketFactory sslSocketFactory() {
        SSLContext sslContext = SSL.sslContext();
        sslContext.init(null, trustManagers(), new SecureRandom());
        return sslContext.getSocketFactory();
    }

    public static X509TrustManager trustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String message) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String message) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        };
    }

    public static TrustManager[] trustManagers() {
        return new TrustManager[]{trustManager()};
    }

    public static HostnameVerifier hostnameVerifier() {
        return (hostname, session) -> true;
    }

}














