package com.meix.doublerecord.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meix.doublerecord.R;
import com.meix.doublerecord.base.AppManager;
import com.meix.doublerecord.base.BaseActivity;
import com.meix.doublerecord.constant.KeyConstant;
import com.meix.doublerecord.entity.WebFromEntity;
import com.meix.doublerecord.util.GsonUtil;

public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    private final static int JS_APP_GO_VIDEO_OPTIONS = 1001;
    public final static int REQUEST_DOUBLE_VIDEO_CODE = 101;
    private WebView webView;
    private ProgressBar mProgressBar;
    private TextView tv_web_title;
    private ImageView iv_back;
    private String url;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                String obj = "";
                if (msg.obj != null) {
                    obj = String.valueOf(msg.obj);
                }
                switch (msg.what) {
                    case JS_APP_GO_VIDEO_OPTIONS:
                        goToVideoMain(obj);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    /**
     * 启动双录界面
     *
     * @param jsonStr
     */
    private void goToVideoMain(String jsonStr) {
        WebFromEntity webFromEntity = GsonUtil.GsonToBean(jsonStr, WebFromEntity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KeyConstant.URL_WEB_ENTITY_KEY, webFromEntity);
        DoubleVideoMainActivity.startActivityWithOject(this, bundle);
    }

    public static void startActivity(Activity context, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, WebViewActivity.class);
        intent.putExtra(KeyConstant.URL_BUNDLE_KEY, bundle);
        context.startActivityForResult(intent, REQUEST_DOUBLE_VIDEO_CODE, bundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initPresenter() {

    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void initView(Bundle savedInstanceState) {
        AppManager.getAppManager().addActivity(this);
        webView = (WebView) findViewById(R.id.webView);
        mProgressBar = (ProgressBar) findViewById(R.id.mProgressBar);
        tv_web_title = (TextView) findViewById(R.id.tv_web_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        url = getIntent().getBundleExtra(KeyConstant.URL_BUNDLE_KEY).getString(KeyConstant.URL_KEY);
        setCookie();
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDisplayZoomControls(false);
        settings.setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);
//        webView.requestFocus();
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                return true;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                tv_web_title.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    mProgressBar.setProgress(progress);
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setProgress(progress);
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        webView.addJavascriptInterface(new CommonJsInterface(), "smAndroid");

        webView.loadUrl(url);
    }

    @Override
    public void initListener() {
        iv_back.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                webView.goForward();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if (v == iv_back) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                webView.goForward();
            } else {
                finish();
            }
        }
    }

    /**
     * 客户传过来的参数
     */
    private int client_id = 1234;
    /**
     * 设置cookie
     */
    private void setCookie() {
        CookieManager cookieManager = CookieManager.getInstance();
        String CookieStr = "client_id=" + client_id;
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, CookieStr);
        Log.i("webview", cookieManager.getCookie(url));
    }

    private class CommonJsInterface {
        @JavascriptInterface
        public void goToVideoMain(String response) {
            Message message = new Message();
            message.what = JS_APP_GO_VIDEO_OPTIONS;
            message.obj = response;
            mHandler.sendMessage(message);
        }
    }

    public void refreshH5() {
        webView.reload();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if ("video_success".equals(data.getStringExtra(KeyConstant.URL_VIDEO_SUCCESS_KEY))) {
                    refreshH5();
                }
            }
        }
    }
}
