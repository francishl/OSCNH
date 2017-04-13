package com.huliang.oschn.api;

import android.annotation.SuppressLint;
import android.app.Application;

import com.huliang.oschn.util.TLog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by huliang on 17/3/20.
 */
public class ApiHttpClient {

    public final static String HOST = "www.oschina.net";
    public static String API_URL = "https://www.oschina.net/%s";

    private static AsyncHttpClient CLIENT;

    /**
     * 初始化网络请求，包括Cookie的初始化
     *
     * @param context
     */
    public static void init(Application context) {
        AsyncHttpClient client = new AsyncHttpClient();
        ApiHttpClient.setHttpClient(client, context);
    }

    public static void get(String partUrl, RequestParams params,
                           AsyncHttpResponseHandler handler) {
        CLIENT.get(getAbsoluteApiUrl(partUrl), params, handler);
        log("GET " + partUrl + "?" + params);
    }

    public static void post(String partUrl, RequestParams params,
                            AsyncHttpResponseHandler handler) {
        CLIENT.post(getAbsoluteApiUrl(partUrl), params, handler);
        log("GET " + partUrl + "?" + params);
    }

    public static void setHttpClient(AsyncHttpClient c, Application application) {
        CLIENT = c;
        initSSL(CLIENT);
    }

    public static String getAbsoluteApiUrl(String partUrl) {
        String url = partUrl;
        if (!partUrl.startsWith("http:") && !partUrl.startsWith("https:")) {
            url = String.format(API_URL, partUrl);
        }
        return url;
    }

    public static void log(String log) {
        TLog.log("ApiHttpClient", log);
    }

    /**
     * Android 环境下处理 HTTPS 请求
     *
     * @param client
     */
    private static void initSSL(AsyncHttpClient client) {
        try {
            // initialize a default Keystore
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            // load the KeyStore
            trustStore.load(null, null);
            // initialize a new SSLSocketFacrory
            MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
            // set that all host names are allowed in the socket factory
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            // set the SSL Factory
            client.setSSLSocketFactory(socketFactory);
            // initialize a GET http request
            // ...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义 SSLSocketFactory
     */
    @SuppressWarnings("deprecation")
    private static class MySSLSocketFactory extends cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory {
        // TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, UnrecoverableEntryException, KeyStoreException {
            super(truststore);

            // 创建TrustManager
            TrustManager tm = new X509TrustManager() {
                @SuppressLint("TrustAllX509TrustManager")
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @SuppressLint("TrustAllX509TrustManager")
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            // 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
