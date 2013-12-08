package com.zhang.gs;

import android.widget.ProgressBar;



public class ProgressBarGS extends AGS{
	@Override
	public Object getData() {
		return ((ProgressBar)this.getView()).getProgress();
	}

	@Override
	public void setData(Object obj) {
		((ProgressBar)this.getView()).setProgress((Integer)obj);
	}
}
