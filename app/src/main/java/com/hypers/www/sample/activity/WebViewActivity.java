package com.hypers.www.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hypers.www.sample.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/x.html");

        mWebView.addJavascriptInterface(this, "android");

//        Object object = HMT.getInterface(WebViewActivity.this);
//
//        mWebView.addJavascriptInterface(object, "hmt");
    }


    @JavascriptInterface
    public void show(String str, String json) {
        Log.d("WebViewActivity", json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Log.d("WebViewActivity", key);
                String value = (String) jsonObject.get(key);
                Log.d("WebViewActivity", value);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Toast.makeText(this, " msg from js: " + str, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void show(String str) {
        Toast.makeText(this, "param 1 msg = " + str, Toast.LENGTH_SHORT).show();
    }

    public static class HMT {
        public static Object getInterface(final Activity activity) {

            return new Object() {

                @JavascriptInterface
                public void show(String str) {
                    Toast.makeText(activity, "this is from .....HMT" + str, Toast.LENGTH_SHORT).show();
                }
            };

        }
    }
}

