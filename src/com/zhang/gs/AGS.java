package com.zhang.gs;


public abstract class AGS {
	/**
	 * ����View
	 * @param view
	 */
	Object view;
	public void setView(Object view){
		this.view = view;
	};
	public Object getView(){
		return view;
	};
	public abstract Object getData();
	public abstract void setData(Object obj);
}
