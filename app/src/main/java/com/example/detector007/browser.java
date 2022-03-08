package com.example.detector007;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class browser extends AppCompatActivity {

    WebView webview;
    public static final String NAME = "NAME";
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        Intent i=getIntent();
        name=i.getStringExtra(NAME);

        webview=findViewById(R.id.webView);
        webview.loadUrl(name);
        WebSettings webSettings=webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());

    }
}