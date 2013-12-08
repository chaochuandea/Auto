package com.zhang.gs;

import android.widget.ToggleButton;


public class ToggleButtonGS extends AGS{
	@Override
	public Object getData() {
		return ((ToggleButton)this.getView()).getText().toString();
	}

	@Override
	public void setData(Object obj) {
		((ToggleButton)this.getView()).setText(obj.toString());
	}
}
