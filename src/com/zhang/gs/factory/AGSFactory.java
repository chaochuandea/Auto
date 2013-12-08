package com.zhang.gs.factory;

import java.util.HashMap;
import java.util.Map;

import com.zhang.gs.AGS;
import com.zhang.gs.ButtonGS;
import com.zhang.gs.CheckBoxGS;
import com.zhang.gs.EditTextGS;
import com.zhang.gs.ImageButtonGS;
import com.zhang.gs.ImageViewGS;
import com.zhang.gs.ProgressBarGS;
import com.zhang.gs.RadioButtonGS;
import com.zhang.gs.SeekBarGS;
import com.zhang.gs.TextViewGS;
import com.zhang.gs.ToggleButtonGS;
import com.zhang.gs.WebViewGS;

public class AGSFactory {
	private static Map<String, AGS> agsMap ;
	public static AGS create(String viewSimpleName){
		if(agsMap==null){
			loadAGS();
		}
		return agsMap.get(viewSimpleName);
	}
	private static void loadAGS(){
		agsMap = new HashMap<String, AGS>();
		agsMap.put("TextView", new TextViewGS());
		agsMap.put("EditText", new EditTextGS());
		agsMap.put("Button", new ButtonGS());
		agsMap.put("CheckBox", new CheckBoxGS());
		agsMap.put("ImageView", new ImageViewGS());
		agsMap.put("ImageButton", new ImageButtonGS());
		agsMap.put("ProgressBar", new ProgressBarGS());
		agsMap.put("RadiaoButton", new RadioButtonGS());
		agsMap.put("SeekBar", new SeekBarGS());
		agsMap.put("ToggleButton", new ToggleButtonGS());
		agsMap.put("WebView", new WebViewGS());
	}
}
