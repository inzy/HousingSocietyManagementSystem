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
 * SwingID.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.swing;

import java.awt.event.ActionListener;

public interface SwingID {

    /**
	 * @return the iD
	 */
	public abstract int getID();

	/**
	 * @param id the iD to set
	 */
	public abstract void setID(int id);

    public abstract void setToolTipText(String text);

     /**
     * Call this method instead of setOnAction for Swing
     * @param a 
     */
    public abstract void addActionListener(ActionListener a);
}
