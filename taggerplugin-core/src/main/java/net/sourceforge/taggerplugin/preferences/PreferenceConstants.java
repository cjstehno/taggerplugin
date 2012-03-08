/*   ********************************************************************** **
**   Copyright (c) 2006-2007 Christopher J. Stehno (chris@stehno.com)       **
**   http://www.stehno.com                                                  **
**                                                                          **
**   All rights reserved                                                    **
**                                                                          **
**   This program and the accompanying materials are made available under   **
**   the terms of the Eclipse Public License v1.0 which accompanies this    **
**   distribution, and is available at:                                     **
**   http://www.stehno.com/legal/epl-1_0.html                               **
**                                                                          **
**   A copy is found in the file license.txt.                               **
**                                                                          **
**   This copyright notice MUST APPEAR in all copies of the file!           **
**  **********************************************************************  */
package net.sourceforge.taggerplugin.preferences;

public enum PreferenceConstants {

	CONFIRM_CLEAR_ASSOCIATIONS("confirm.clearassociations"),
	CONFIRM_DELETE_TAG("confirm.deletetag"),
	POSITION_LABEL_DECORATION("position.labeldecoration"),
	TAGSET_FILE_LOCATION("tagset.filelocation");

	private String key;

	private PreferenceConstants(String key){
		this.key = key;
	}

	public String getKey(){return(key);}
}
