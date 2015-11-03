package com.huzefagadi.rashida.webview;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


public class MainActivity extends Activity {
    private ProgressBar progress;
    private WebView mainWebView;
    private Utility utility;
    private Context mContext;
    private WebView mWebviewPop;
    private FrameLayout mContainer;
    private CookieManager cookieManager;
    private Button refreshButton;
    private RelativeLayout noInternetMessage;


    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mContext = this;
        utility = new Utility();
        mainWebView = (WebView) findViewById(R.id.webview);
        mContainer = (FrameLayout) findViewById(R.id.webview_frame);
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        noInternetMessage = (RelativeLayout) findViewById(R.id.no_internet_message);
        refreshButton = (Button) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                onRefresh();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(mainWebView, true);
        }

        if (utility.checkInternetConnectivity(mContext)) {
            mContainer.setVisibility(View.VISIBLE);
            noInternetMessage.setVisibility(View.GONE);
            mainWebView.loadUrl(Constants.URL);
        } else {
            mContainer.setVisibility(View.GONE);
            noInternetMessage.setVisibility(View.VISIBLE);
        }
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(100);
        WebSettings webSettings = mainWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        mainWebView.setWebViewClient(new MyCustomWebViewClient());
        mainWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        mainWebView.setWebChromeClient(new MyCustomChromeClient());
        mainWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        Bundle extras = getIntent().getExtras();
        String link = null;
        if (extras != null) {
            link = extras.getString(Constants.LINK);
        }
        onNewIntent(getIntent());
        if (link != null && !link.isEmpty()) {
            mainWebView.loadUrl(link);
        } else {
            mainWebView.loadUrl(Constants.URL);
        }

    }

    private class MyCustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String host = Uri.parse(url).getHost();
            if (host.equals(Constants.TARGET_URL_PREFIX) || host.equals(Constants.TARGET_URL_PREFIX2)) {
                // This is my web site, so do not override; let my WebView load
                // the page
                if (mWebviewPop != null) {
                    mWebviewPop.setVisibility(View.GONE);
                    mContainer.removeView(mWebviewPop);
                    mWebviewPop = null;
                }
                return false;
            }
            if (host.equals("m.facebook.com") || host.equals("www.facebook.com") || host.equals("accounts.google.com") || host.equals("api.twitter.com")) {
                // return false;
            }
            //Launch in chrome or other application
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            Log.d("onReceivedSslError", "onReceivedSslError");
            //super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub

            progress.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            progress.setVisibility(View.VISIBLE);
            //showDialog();
            super.onPageStarted(view, url, favicon);
        }

    }

    private class MyCustomChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            MainActivity.this.setValue(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            mWebviewPop = new WebView(mContext);
            mWebviewPop.setVerticalScrollBarEnabled(false);
            mWebviewPop.setHorizontalScrollBarEnabled(false);
            mWebviewPop.setWebViewClient(new MyCustomWebViewClient());
            mWebviewPop.getSettings().setJavaScriptEnabled(true);
            mWebviewPop.getSettings().setSavePassword(false);
            mWebviewPop.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            mContainer.addView(mWebviewPop);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPop);
            resultMsg.sendToTarget();

            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
            Log.d("onCloseWindow", "called");
        }
    }

    public void setValue(int progress) {
        this.progress.setProgress(progress);
    }

    public void onRefresh() {
        // TODO Auto-generated method stub
        /*
		new Handler().postDelayed(new Runnable() {
			@Override public void run() {
				swipeLayout.setRefreshing(false);
			}
		}, 5000);*/
        if (utility.checkInternetConnectivity(mContext)) {
            mContainer.setVisibility(View.VISIBLE);
            noInternetMessage.setVisibility(View.GONE);
            mainWebView.loadUrl(Constants.URL);

        } else {

            mContainer.setVisibility(View.GONE);
            noInternetMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        //	System.out.println("BACK PRESSED-->"+mainWebView.getUrl());

        if (mWebviewPop != null) {
            mWebviewPop.setVisibility(View.GONE);
            mContainer.removeView(mWebviewPop);
            mWebviewPop = null;
        } else {
            if (mainWebView.canGoBack()) {
                mainWebView.goBack();
            } else {
                super.onBackPressed();
                finish();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mainWebView.loadUrl(extras.getString(Constants.LINK));
        }
    }

}
