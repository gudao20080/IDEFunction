package com.greatmap.idefunction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *WebView
 */
public class WebActivity extends AppCompatActivity {

    @Bind(R.id.wv)
    WebView mWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        initWebViewParams();
        initData();
    }

    private void initWebViewParams() {
        WebSettings settings = mWv.getSettings();
        settings.setSupportZoom(true);
        settings.setJavaScriptEnabled(true);

        mWv.setWebViewClient(new WebViewClient());
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(Constant.KEY_TITLE, "");
            String url = extras.getString(Constant.KEY_URL, "");
            getSupportActionBar().setTitle(title);
            mWv.loadUrl(url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWv.canGoBack()) {
            mWv.goBack();
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
