package com.hmt.analytics.jcenter_upload;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NetworkUitlity {

    private static final String TAG = NetworkUitlity.class.getSimpleName();
    public static final int READ_TIME_OUT = 15000;
    public static final int CONNECT_TIME_OUT = 15000;

    public static MyMessage get(String url) {
        MyMessage message = new MyMessage();
        String returnContent = "";
        int status = 0;
        printLog(TAG, "get : url = " + url);
        try {
            if (url.startsWith("https")) {
                HttpsURLConnection conn = null;
                try {
                    conn = NetworkUitlity.ignoreSSL(url);
                    conn.setReadTimeout(READ_TIME_OUT);
                    conn.setConnectTimeout(CONNECT_TIME_OUT);
                    conn.connect();
                    status = conn.getResponseCode();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    String lines = "";
                    while ((lines = in.readLine()) != null) {
                        returnContent += lines;
                    }
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }

            } else {
                HttpURLConnection conn = null;
                try {
                    URL requestUrl = new URL(url);
                    conn = (HttpURLConnection) requestUrl.openConnection();
                    conn.setReadTimeout(READ_TIME_OUT);
                    conn.setConnectTimeout(CONNECT_TIME_OUT);
                    conn.connect();
                    status = conn.getResponseCode();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    String lines = "";
                    while ((lines = in.readLine()) != null) {
                        returnContent += lines;
                    }
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }

            printLog(TAG, "get : status = " + status + " , " +
                    "returnContent = " + returnContent);
            switch (status) {
                case 200:
                    message.setFlag(true);
                    message.setMsg(returnContent);
                    break;
                default:
                    message.setFlag(false);
                    message.setMsg(returnContent);
                    break;
            }
            message.setMsg(returnContent);
        } catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("err", e.toString());
                returnContent = jsonObject.toString();
                message.setFlag(false);
                message.setMsg(returnContent);
                printLog(TAG, "get : " + returnContent);
            } catch (JSONException e1) {
                printLog(TAG, "get : " + e1.getMessage());
            }
        }
        return message;
    }

    public static MyMessage post(String url, String data) {
        printLog(TAG, "post : 2 param url = " + url);
        String returnContent = "";
        int status = 0;
        BufferedReader in = null;
        MyMessage message = new MyMessage();

        DataOutputStream out = null;
        try {

            if (url.startsWith("https")) {
                HttpsURLConnection conn = null;
                try {
                    conn = NetworkUitlity.ignoreSSL(url);

                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setReadTimeout(READ_TIME_OUT);
                    conn.setConnectTimeout(CONNECT_TIME_OUT);
                    out = new DataOutputStream(conn.getOutputStream());
                    if (data != null) {
                        data = NetworkUitlity.getEncodeData(data);
                        out.writeBytes(data);
                    }
                    out.flush();
                    status = conn.getResponseCode();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            } else {
                HttpURLConnection conn = null;
                try {
                    URL requestUrl = new URL(url);
                    conn = (HttpURLConnection) requestUrl.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setReadTimeout(READ_TIME_OUT);
                    conn.setConnectTimeout(CONNECT_TIME_OUT);
                    conn.setDoOutput(true);
                    out = new DataOutputStream(conn.getOutputStream());
                    if (data != null) {
                        data = NetworkUitlity.getEncodeData(data);
                        out.writeBytes(data);
                    }
                    out.flush();
                    status = conn.getResponseCode();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }

            printLog(TAG, "post : status = " + status);

            if (HttpsURLConnection.HTTP_OK == status) {
                String temp = in.readLine();
                while (temp != null) {
                    if (returnContent != null)
                        returnContent += temp;
                    else
                        returnContent = temp;
                    temp = in.readLine();
                }
                returnContent = URLDecoder.decode(returnContent);
                message.setFlag(true);
                message.setMsg(returnContent);
            } else {
                message.setFlag(false);
                message.setMsg(returnContent);
            }

        } catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("err", e.toString());
                returnContent = jsonObject.toString();
                message.setFlag(false);
                message.setMsg(returnContent);
                printLog(TAG, "post : returnContent  = " + returnContent);
            } catch (JSONException e1) {
                printLog(TAG, "post : " + e1.getMessage());
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    printLog(TAG, "post : " + e.getMessage());
                }
            }
        }

        return message;
    }

    public static boolean post(String url, String data, String apiName) {
        int status = 0;
        BufferedReader in = null;
        printLog(TAG, "post : 3 param url = " + url);
        DataOutputStream out = null;
        try {
            if (url.startsWith("https")) {
                HttpsURLConnection conn = null;
                try {
                    conn = NetworkUitlity.ignoreSSL(url);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setReadTimeout(READ_TIME_OUT);
                    conn.setConnectTimeout(CONNECT_TIME_OUT);
                    conn.connect();
                    out = new DataOutputStream(conn.getOutputStream());
                    if (data != null) {
                        data = NetworkUitlity.getEncodeData(data);
                        out.writeBytes(data);
                    }
                    out.flush();
                    status = conn.getResponseCode();
                } catch (IOException e) {
                    printLog(TAG, "============" + e.getMessage());
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            } else {
                URL requestUrl = new URL(url);
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) requestUrl.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setReadTimeout(READ_TIME_OUT);
                    conn.setConnectTimeout(CONNECT_TIME_OUT);
                    out = new DataOutputStream(conn.getOutputStream());
                    if (data != null) {
                        data = NetworkUitlity.getEncodeData(data);
                        out.writeBytes(data);
                    }
                    out.flush();
                    status = conn.getResponseCode();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
            printLog(TAG, "post : status = " + status);
            if (HttpsURLConnection.HTTP_OK == status) {
                return true;
            } else {
            }
        } catch (Exception e) {
            printLog(TAG, "post : " + e.toString());
        } catch (Error err) {
            printLog(TAG, "post : " + err.toString());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    printLog(TAG, "post : " + e.getMessage());
                }
            }
        }
        return false;
    }

    public static final String SAULT = "88f702a9ef191b4a22e84ffe4a24e1add60a35b9f2394c560ff";

    private static String getEncodeData(String data) throws IOException {
        printLog(TAG, "getEncodeData : data = " + data);
        data = "contents=" + data;
        return data;
    }

    public static HttpsURLConnection ignoreSSL(String url)
            throws NoSuchAlgorithmException, KeyManagementException,
            IOException {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                // return null;
                return new X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] certs,
                                           String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs,
                                           String authType) {
                if (null == certs) {
                    throw new IllegalArgumentException(
                            "Check Server X509Certificate is null");
                }
                if (certs.length < 0) {
                    throw new IllegalArgumentException(
                            "Check Server X509Certificate is empty");
                }
                for (X509Certificate cert : certs) {
                    try {
                        cert.checkValidity();
                    } catch (CertificateExpiredException e) {
                        printLog(TAG, "ignoreSSL : " + e.toString());
                    } catch (CertificateNotYetValidException e) {
                        printLog(TAG, "ignoreSSL : " + e.toString());
                    }
                    // Verify the certificate's public key chain.
                    try {
                        cert.verify(((X509Certificate) cert).getPublicKey());
                    } catch (NoSuchAlgorithmException e) {
                        printLog(TAG, "ignoreSSL : " + e.toString());
                    } catch (InvalidKeyException e) {
                        printLog(TAG, "ignoreSSL : " + e.toString());
                    } catch (NoSuchProviderException e) {
                        printLog(TAG, "ignoreSSL : " + e.toString());
                    } catch (SignatureException e) {
                        printLog(TAG, "ignoreSSL : " + e.toString());
                    } catch (CertificateException e) {
                        printLog(TAG, "ignoreSSL : " + e.toString());
                    }
                }
            }
        }};
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        URL requestUrl = new URL(url);
        HttpsURLConnection httpsConn = (HttpsURLConnection) requestUrl
                .openConnection();
//        httpsConn.setRequestProperty("Connection", "Close");
//        httpsConn.setSSLSocketFactory(sc.getSocketFactory());
        httpsConn.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                Log.d(TAG, "hostname = " + hostname);
                if (hostname.equals("irs01")) {
                    return true;
                } else {
                    HostnameVerifier hv = HttpsURLConnection
                            .getDefaultHostnameVerifier();
                    return hv.verify(hostname, session);
                }
            }
        });
        return httpsConn;
    }


    public static void printLog(String tag, String log) {
        if (!TextUtils.isEmpty(log)) {
            Log.e(tag, log);
        }
    }

}
