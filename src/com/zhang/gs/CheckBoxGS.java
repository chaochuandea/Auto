package com.zhang.gs;

import android.widget.CheckBox;



public class CheckBoxGS extends AGS{
	@Override
	public Object getData() {
		return ((CheckBox)this.getView()).getText().toString();
	}

	@Override
	public void setData(Object obj) {
		((CheckBox)this.getView()).setText(obj.toString());
	}
}
