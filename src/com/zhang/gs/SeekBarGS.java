package com.zhang.gs;

import android.widget.SeekBar;



public class SeekBarGS extends AGS{
	@Override
	public Object getData() {
		return ((SeekBar)this.getView()).getProgress();
	}

	@Override
	public void setData(Object obj) {
		((SeekBar)this.getView()).setProgress((Integer)obj);
	}
}
