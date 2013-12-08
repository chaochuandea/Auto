package com.zhang.gs;

import android.webkit.WebView;



public class WebViewGS extends AGS{
	@Override
	public Object getData() {
		return ((WebView)this.getView()).getOriginalUrl();
	}

	@Override
	public void setData(Object obj) {
		((WebView)this.getView()).loadUrl(obj.toString());
	}
}
