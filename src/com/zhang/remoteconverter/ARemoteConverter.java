package com.zhang.remoteconverter;


/**
 * ����ת�����
 * @author chaochuande
 *
 */
public interface ARemoteConverter {
	/**
	 * ת��Ϊģ��
	 * @param str
	 * @return
	 */
	Object getModel(String str,Class<?> clas);
	String toJson(Object obj);
}
