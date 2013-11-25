package com.keju.baby.activity.base;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.keju.baby.Constants;
import com.keju.baby.R;

/**
 * html页面
 * @author Zhoujun
 * @version 创建时间：2013-11-21 下午2:42:53
 */
public class BaseWebViewActivity extends BaseActivity {
	protected WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_webview);
		init();
	}
	protected void init(){
		webView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = webView.getSettings();
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.requestFocus();// 使WebView内的输入框等获得焦点
		webView.setWebViewClient(new WebViewClient() {
			// 点击网页里面的链接还是在当前的webView内部跳转，不跳转外部浏览器
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			// 可以让webView处理https请求
			@Override
			public void onReceivedSslError(WebView view, android.webkit.SslErrorHandler handler,
					android.net.http.SslError error) {
				handler.proceed();
			};

			public void onLoadResource(WebView view, String url) {

			};

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

		});
	}
	protected void loadUrl(String url) {
		webView.loadUrl(Constants.URL_BASE_HTML + url);
	}
}
