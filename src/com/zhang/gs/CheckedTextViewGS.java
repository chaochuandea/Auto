package com.zhang.gs;

import android.widget.CheckedTextView;



public class CheckedTextViewGS extends AGS{
	@Override
	public Object getData() {
		return ((CheckedTextView)this.getView()).getText().toString();
	}

	@Override
	public void setData(Object obj) {
		((CheckedTextView)this.getView()).setText(obj.toString());
	}
}
