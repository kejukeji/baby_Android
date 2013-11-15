package com.keju.baby.activity.login;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.keju.baby.CommonApplication;
import com.keju.baby.Constants;
import com.keju.baby.R;
import com.keju.baby.activity.base.BaseActivity;

/**
 * 医生登录界面
 * @author Zhoujun
 * @version 创建时间：2013-10-25 下午2:35:21
 */
public class DoctorLoginActivity extends BaseActivity {
	private Button btnLeft, btnRight;
	private TextView tvTitle;
	
	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_login);
		findView();
		fillData();
		((CommonApplication) getApplication()).addActivity(this);
	}
	
	private void findView() {
		btnLeft = (Button) this.findViewById(R.id.btnLeft);
		btnLeft.setVisibility(View.INVISIBLE);
		btnRight = (Button) this.findViewById(R.id.btnRight);
		btnRight.setVisibility(View.INVISIBLE);
		tvTitle = (TextView) this.findViewById(R.id.tvTitle);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText("登陆");
		webView = (WebView) findViewById(R.id.webview);
	}

	private void fillData() {
		
		WebSettings webSettings = webView.getSettings();
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);
		webView.requestFocus();//使WebView内的输入框等获得焦点
		webView.loadUrl(Constants.BASE_HTML_URL + "login.html");
		webView.setWebViewClient(new WebViewClient() {
			// 点击网页里面的链接还是在当前的webView内部跳转，不跳转外部浏览器
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) { 
				view.loadUrl(url);
				return true;
			}
			//可以让webView处理https请求
			@Override
			public void onReceivedSslError(WebView view,
					android.webkit.SslErrorHandler handler,
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

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if (webView.canGoBack()) {
				webView.goBack(); // goBack()表示返回webView的上一页面，而不直接关闭WebView
				return true;
			}else {
				finish();
				return true;
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}
}
