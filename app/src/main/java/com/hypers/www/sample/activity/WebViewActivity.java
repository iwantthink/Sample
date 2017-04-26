package com.hypers.www.sample.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hypers.www.sample.R;

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
    }

    @JavascriptInterface
    public void show(String s) {
        Toast.makeText(this, "hello this is from android", Toast.LENGTH_SHORT).show();
        //111
        //3222
    }

    @JavascriptInterface
    public void show() {
        Toast.makeText(this, "hello this is from android without param", Toast.LENGTH_SHORT).show();
    }
}
