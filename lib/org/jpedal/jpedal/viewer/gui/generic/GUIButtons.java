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
 * GUIButtons.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.generic;

import org.jpedal.gui.GUIFactory;

/**
 * Abstract class so we can use getButtons and instantiate JavaFXButtons.java and
 * Buttons.java objects in either SwingGUI.java or JavaFXGUI.java which ensures
 * Swing code(Buttons.java) stays in SwingGUI.java and JavaFX code(JavaFXButtons.java)
 * stays in JavaFXGUI, keeping GUI.java generic.
 */
public interface GUIButtons {
    
    public GUIButton getButton(int ID);
    
    public void setBackNavigationButtonsEnabled(boolean flag);
    
    public void setForwardNavigationButtonsEnabled(boolean flag);
    
    public void checkButtonSeparators();
    
    public void setVisible(boolean set);
    
    public void setEnabled(boolean set);
    
    public void hideRedundentNavButtons(GUIFactory currentGUI);
    
    public void alignLayoutMenuOption(int mode);
    
    public void setPageLayoutButtonsEnabled(boolean flag);
    
}
