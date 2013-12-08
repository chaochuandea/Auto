package com.zhang.gs;

import android.widget.TextView;

public class TextViewGS extends AGS{
	@Override
	public Object getData() {
		return ((TextView)this.getView()).getText().toString();
	}

	@Override
	public void setData(Object obj) {
		((TextView)this.getView()).setText(obj.toString());
	}
}
