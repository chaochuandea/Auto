package com.zhang.gs;

import android.widget.RadioButton;



public class RadioButtonGS extends AGS{
	@Override
	public Object getData() {
		return ((RadioButton)this.getView()).getText().toString();
	}

	@Override
	public void setData(Object obj) {
		((RadioButton)this.getView()).setText(obj.toString());
	}
}
