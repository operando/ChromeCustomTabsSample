package com.os.operando.chromeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    private static final String URL = "url";

    public static Intent createIntent(Context context, String url) {
        Intent i = new Intent(context, WebViewActivity.class);
        i.putExtra(URL, url);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(getIntent().getStringExtra(URL));
    }
}
