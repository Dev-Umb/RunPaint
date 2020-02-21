package com.example.applicationtest.Frament;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.applicationtest.R;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class Frament2 extends Fragment {
    private com.tencent.smtt.sdk.WebView mwebView;
    private com.tencent.smtt.sdk.WebSettings settings;
    private Button back;
    @Override
    public void onStart() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mwebView.goBack();
            }
        });
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.activity_frament2,container,false);
        mwebView = contentView.findViewById(R.id.webView);
        init();
        mwebView.loadUrl("https://www.runners.cn/");
       // mwebView.loadUrl("http://debugtbs.qq.com/");
        final SwipeRefreshLayout swipeRefreshLayout = contentView.findViewById(R.id.webView_Refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mwebView.reload();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        mwebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
            }
        });
        final ProgressBar progressBar = contentView.findViewById(R.id.webView_loading);
        mwebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView webView, int i) {
                if (i==100)
                {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        back = contentView.findViewById(R.id.webView_back);

        return contentView;
    }
    private void init()
    {
        settings = mwebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(false);
        settings.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NORMAL);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }

}
