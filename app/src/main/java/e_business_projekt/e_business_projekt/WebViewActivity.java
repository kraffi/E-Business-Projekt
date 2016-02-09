package e_business_projekt.e_business_projekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by RaulVinhKhoa on 01.02.2016.
 */
public class WebViewActivity extends AppCompatActivity{

    //TODO: Check behavior with https
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        mWebView = new WebView(this);
        mWebView.loadUrl(url);
        setContentView(mWebView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    

}
