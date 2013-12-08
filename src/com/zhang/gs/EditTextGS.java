package com.zhang.gs;

import android.widget.EditText;

public class EditTextGS extends AGS{
	@Override
	public Object getData() {
		return ((EditText)this.getView()).getText().toString();
	}

	@Override
	public void setData(Object obj) {
		((EditText)this.getView()).setText(obj.toString());
	}
}
