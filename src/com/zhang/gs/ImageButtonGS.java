package com.zhang.gs;

import com.zhang.bitmap.BitmapCommonUtils;
import com.zhang.bitmap.BitmapDisplayConfig;
import com.zhang.bitmap.callback.BitmapLoadCallBack;
import com.zhang.bitmap.callback.SimpleBitmapLoadCallBack;
import com.zhang.utils.BitmapUtils;
import com.zhang.bitmap.callback.BitmapLoadFrom;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageButton;

public class ImageButtonGS extends AGS{
	private BitmapDisplayConfig bigPicDisplayConfig;
	@Override
	public Object getData() {
		Bitmap bitmap = BitmapUtils.drawableToBitmap(((ImageButton)this.getView()).getDrawable());
		return BitmapUtils.Bitmap2InputStream(bitmap);
	}

	@Override
	public void setData(Object obj) {
		final Context context = ((ImageButton)this.getView()).getContext();
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		 bigPicDisplayConfig = new BitmapDisplayConfig();
		 bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
	     bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(context));
	     BitmapLoadCallBack<ImageButton> callback = new SimpleBitmapLoadCallBack<ImageButton>() {
	            @Override
	            public void onLoadStarted(ImageButton container, String uri, BitmapDisplayConfig config) {
	                super.onLoadStarted(container, uri, config);
	            }

	            @Override
	            public void onLoadCompleted(ImageButton container, String url, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
	                super.onLoadCompleted(container, url, bitmap, config, from);
	            }
	        };
	        bitmapUtils.display((ImageButton)this.getView(), obj.toString(), bigPicDisplayConfig, callback);
	}
}
