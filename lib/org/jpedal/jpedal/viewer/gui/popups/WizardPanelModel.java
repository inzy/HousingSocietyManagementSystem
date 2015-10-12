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
 * WizardPanelModel.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.popups;

import java.awt.event.KeyListener;
import java.util.Map;
import javax.swing.event.ChangeListener;

/**
 * In order to use the Wizard class you need to implement this interface.
 * The implemented class will contain the contents of the user defined panels
 * and add listeners to components that effect the ability to go to the
 * next panel.  See SignWizardModel.java for an example.
 */

public interface WizardPanelModel 
{
	/**
	 * @return true if the currently displayed panel requires a Finish box instead of a next box.
	 */
    public abstract boolean isFinishPanel();
	
	/**
	 * @return The unique ID String of the first JPanel to be shown
	 */
    public abstract String getStartPanelID();
	
	/**
	 * @return The ID of the JPanel that should be displayed if next is clicked.
	 */
    public abstract String next();
	
	/**
	 * @return The ID of the JPanel that should be displayed if previous is clicked.
	 */
    public abstract String previous();
	
	/**
	 * This method can contain any housekeeping you require when the Wizard
	 * is exited.
	 */
    public abstract void close();
	
	/**
	 * @return true if the current panel has a previous panel
	 */
    public abstract boolean hasPrevious();
	
	/**
	 * @return true if the current panel is able to advance.
	 */
    public abstract boolean canAdvance();
	
	/**
	 * In order to use the Card Layout in the wizard class
	 * each JPanel must have a unique String identifier.
	 * ie HashMap (String, JPanel)
	 * 
	 * @return A mapping of ID Strings to JPanels. 
	 */
    public abstract Map getJPanels();
	
	/**
	 * A component that is registered will alert the Wizard
	 * class that it should check whether the advance button should be enabled.
	 * 
	 * @param e Alerts Wizard that the registered component effects the Next button enable/disable status
	 */
    public abstract void registerNextChangeListeners(ChangeListener e);
	
	/**
	 * The same effect as registerNextChangeListeners(ChangeListener e) except
	 * applied to keys.
	 * 
	 * @param e Alerts Wizard that the registered component effects the Next button enable/disable status
	 */
    public abstract void registerNextKeyListeners(KeyListener e);
}
