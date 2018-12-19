package com.example.djd.fingertest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by djd on 18-12-18.
 */

public class WebViewActivitiy extends AppCompatActivity {
    private WebView mWb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);
        mWb = findViewById(R.id.wb_test);
        final WebSettings webSettings = mWb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWb.loadUrl("file:///android_asset/test.html");

        findViewById(R.id.btn_calljs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWb.post(new Runnable() {
                    @Override
                    public void run() {
                    if (Build.VERSION.SDK_INT < 18) {
                        mWb.loadUrl("javascript:callJS()");
                    } else {
                        mWb.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                               /* AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivitiy.this);
                                builder.setTitle("Alert");
                                builder.setMessage(value+"> 18");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //result.confirm();
                                    }
                                });
                                builder.setCancelable(false);
                                builder.create().show();*/
                            }
                        });
                    }
                    }
                });
            }
        });


        mWb.setWebChromeClient(webChromeClient);
        mWb.addJavascriptInterface(new CallAndroid(), "test");
        mWb.setWebViewClient(webViewClient);
    }


    public class CallAndroid extends Object{

        @JavascriptInterface
       public void hello(String mgs) {
            Log.i("webviewActivity", "hello"+mgs);
        }

    }


    private WebViewClient webViewClient = new WebViewClient(){

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            Log.i("webviewActivity", "loading"+request.getUrl());
            if (url.contains("js")) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, request);

        }

    };

    private WebChromeClient webChromeClient = new WebChromeClient(){
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            //return super.onJsAlert(view, url, message, result);
            AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivitiy.this);
            builder.setTitle("Alert");
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }


    };


}
