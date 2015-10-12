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
 * SwingCombo.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.swing;

import javax.swing.JComboBox;

import org.jpedal.examples.viewer.gui.generic.GUICombo;

public class SwingCombo extends JComboBox implements GUICombo{
	
	private int ID;
	
	public SwingCombo(final String[] qualityValues) {
		super(qualityValues);
        setLightWeightPopupEnabled(false);
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
    public void setVisibility(final boolean set) {
        setVisible(set);
    }
	
}
