package com.meix.doublerecord.net;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * @user steven
 * @createDate 2020/5/11 13:42
 * @description 自定义
 */
public class TrustAllCerts implements X509TrustManager {
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {}

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
}
