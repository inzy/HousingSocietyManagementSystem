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
 * GUICombo.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.generic;

/**abstract version of ComboBox*/

public interface GUICombo {

	public void setSelectedIndex(int defaultSelection);

	public void setEditable(boolean b);

	public void setID(int id);

	public void setToolTipText(String tooltip);

	public void setEnabled(boolean value);

	public int getSelectedIndex();

	public void setSelectedItem(Object index);

	public Object getSelectedItem();
    
    public int getID();
    
    public void setVisibility(boolean set);
    
    public void setName(String name);

}
