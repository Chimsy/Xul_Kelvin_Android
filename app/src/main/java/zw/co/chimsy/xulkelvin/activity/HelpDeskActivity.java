package zw.co.chimsy.xulkelvin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import zw.co.chimsy.xulkelvin.R;

public class HelpDeskActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_desk);


        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());

        // Loading local FAQ html file into web view
        webView.loadUrl("file:///android_asset/helpdesk/index.html");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
