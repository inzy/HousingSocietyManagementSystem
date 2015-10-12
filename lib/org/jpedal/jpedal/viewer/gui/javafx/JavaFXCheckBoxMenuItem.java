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
 * JavaFXCheckBoxMenuItem.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.javafx;

import javafx.scene.control.CheckMenuItem;

/**
 *
 */
public class JavaFXCheckBoxMenuItem extends CheckMenuItem implements JavaFXID {
    
	private int ID;

	public JavaFXCheckBoxMenuItem(final String text) {
		super(text);
	}

	/**
	 * @return the iD
	 */
	@Override
    public int getID() {
		return ID;
	}

	/**
	 * @param id the iD to set
	 */
	@Override
    public void setID(final int id) {
		ID = id;
	}

    @Override
    public void setToolTipText(final String text) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
