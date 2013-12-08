package com.zhang.gs;

import android.widget.Button;

public class ButtonGS extends AGS{
	@Override
	public Object getData() {
		return ((Button)this.getView()).getText().toString();
	}

	@Override
	public void setData(Object obj) {
		((Button)this.getView()).setText(obj.toString());
	}
}
