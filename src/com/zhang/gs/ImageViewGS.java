package com.zhang.gs;

import com.zhang.bitmap.BitmapCommonUtils;
import com.zhang.bitmap.BitmapDisplayConfig;
import com.zhang.bitmap.callback.BitmapLoadCallBack;
import com.zhang.bitmap.callback.SimpleBitmapLoadCallBack;
import com.zhang.utils.BitmapUtils;
import com.zhang.bitmap.callback.BitmapLoadFrom;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageViewGS extends AGS{
	private BitmapDisplayConfig bigPicDisplayConfig;
	@Override
	public Object getData() {
		Bitmap bitmap = BitmapUtils.drawableToBitmap(((ImageView)this.getView()).getDrawable());
		return BitmapUtils.Bitmap2InputStream(bitmap);
	}

	@Override
	public void setData(Object obj) {
		final Context context = ((ImageView)this.getView()).getContext();
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		 bigPicDisplayConfig = new BitmapDisplayConfig();
		 bigPicDisplayConfig.setBitmapConfig(Bitmap.Config.RGB_565);
	     bigPicDisplayConfig.setBitmapMaxSize(BitmapCommonUtils.getScreenSize(context));
	     BitmapLoadCallBack<ImageView> callback = new SimpleBitmapLoadCallBack<ImageView>() {
	            @Override
	            public void onLoadStarted(ImageView container, String uri, BitmapDisplayConfig config) {
	                super.onLoadStarted(container, uri, config);
	            }

	            @Override
	            public void onLoadCompleted(ImageView container, String url, Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
	                super.onLoadCompleted(container, url, bitmap, config, from);
	            }
	        };
	        bitmapUtils.display((ImageView)this.getView(), obj.toString(), bigPicDisplayConfig, callback);
	}
}
