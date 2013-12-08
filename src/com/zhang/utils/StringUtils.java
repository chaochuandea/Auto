package com.zhang.utils;

public class StringUtils {
	//����ĸתСд
    public static String toLowerCaseFirstOne(String s)
    {
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
    //����ĸת��д
    public static String toUpperCaseFirstOne(String s)
    {
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
    public static String replace(String strSource, String strFrom, String strTo) {    
        if (strSource == null) {        
          return null;    
        }  
        int i = 0;
        if ((i = strSource.indexOf(strFrom, i)) >= 0) {
          char[] cSrc = strSource.toCharArray();
          char[] cTo = strTo.toCharArray();
          int len = strFrom.length();  
          StringBuffer buf = new StringBuffer(cSrc.length);  
          buf.append(cSrc, 0, i).append(cTo);
          i += len;    
          int j = i;       
          while ((i = strSource.indexOf(strFrom, i)) > 0) {  
            buf.append(cSrc, j, i - j).append(cTo);   
            i += len;  
            j = i;        
          }        
          buf.append(cSrc, j, cSrc.length - j); 
          return buf.toString(); 
        } 
        return strSource;
      }
}
