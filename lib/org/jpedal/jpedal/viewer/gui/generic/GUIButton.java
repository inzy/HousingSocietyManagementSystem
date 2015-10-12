/*
 * ===========================================
 * Java Pdf Extraction Decoding Access Library
 * ===========================================
 *
 * Project Info:  http://www.idrsolutions.com
 * Help section for developers at http://www.idrsolutions.com/support/
 *
 * (C) Copyright 1997-2015 IDRsolutions and Contributors.
 *
 * This file is part of JPedal/JPDF2HTML5
 *
 
 *
 * ---------------
 * GUIButton.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.generic;

import java.net.URL;

/**abstract button object into interface*/
public interface GUIButton {
	
	void init(URL path, int i, String message);
	
	void setVisible(boolean b);
	
	void setEnabled(boolean b);

    void setIcon(URL url);

    int getID();

	void setName(String string);
	
}
