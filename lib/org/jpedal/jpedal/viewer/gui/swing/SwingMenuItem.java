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
 * SwingMenuItem.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.swing;

import javax.swing.JMenuItem;

/**extended Menu with id stored internally so listener can identify commands*/
public class SwingMenuItem extends JMenuItem implements SwingID {
	
	private int ID;
	
	public SwingMenuItem(final String text) {
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
	
}
