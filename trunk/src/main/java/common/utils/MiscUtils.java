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
public class MiscUtils {
	private MiscUtils(){}

	/**
	 * check if testObj is null.
	 * appends message to sb and returns false if is null.
	 * @param testObj object to be tested on null
	 * @param varName name of property to be appended to sb
	 * @param sb will contain all messages
	 * @return true if not null
	 */
	public static boolean checkNotNull(Object testObj, String varName, StringBuilder sb){
		if (testObj==null){
			sb.append(varName);
			sb.append(" not specified; ");
			return false;
		} else {
			return true;
		}
	}
}
