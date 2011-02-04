/*******************************************************************************
 * Copyright (c) 2011 demchuck.dima@gmail.com.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     demchuck.dima@gmail.com - initial API and implementation
 ******************************************************************************/

package common.utils;

/**
 *
 * @author demchuck.dima@gmail.com
 */
public class StringUtils {
    
    private StringUtils() {}
    
    public static String firstLetterUpperCase(String s){
       char[] str=new char[s.length()];
       s.getChars(0,s.length(),str,0);
       str[0]=Character.toUpperCase(str[0]);
       return String.copyValueOf(str);
    }

	/**
	 * return name of getter like get+PropertyName
	 * @param propertyName name of property
	 * @return getter name
	 */
    public static String getterNameForProperty(String propertyName){
       char[] str=new char[propertyName.length()];
       propertyName.getChars(0,propertyName.length(),str,0);
       str[0]=Character.toUpperCase(str[0]);
       return "get"+String.copyValueOf(str);
    }

	/**
	 * return name of setter like set+PropertyName
	 * @param propertyName name of property
	 * @return getter name
	 */
    public static String setterNameForProperty(String propertyName){
       char[] str=new char[propertyName.length()];
       propertyName.getChars(0,propertyName.length(),str,0);
       str[0]=Character.toUpperCase(str[0]);
       return "set"+String.copyValueOf(str);
    }

	/**
	 * converts String to long and returns null if any fail
	 * @param value
	 * @return
	 */
	public static Long getLong(String value){
		try{
			return Long.parseLong(value, 10);
		}catch(Exception e){
			return null;
		}
	}
    
}
