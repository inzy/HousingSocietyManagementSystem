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
 * SwingCommandListener.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.gui.generic.GUIButton;

/**
 *
 */
public class SwingCommandListener implements ActionListener {
    
	long lastCall;
	static final int timeBetweenCalls = 1000;
    
    final Commands currentCommands;
    
    public SwingCommandListener(final Commands currentCommands){
        this.currentCommands = currentCommands;
    }
    
    
	@Override
	public void actionPerformed(final ActionEvent e) {
		
		final long currentTime = e.getWhen();
		
		final Object source=e.getSource();
		final int ID;
		if(source instanceof GUIButton) {
            ID = ((GUIButton) source).getID();
        } else if(source instanceof SwingMenuItem) {
            ID = ((SwingMenuItem) source).getID();
        } else if(source instanceof SwingCombo) {
            ID = ((SwingCombo) source).getID();
        } else {
            ID = ((SwingID) source).getID();
        }

		//If not a page navigation command, execute immediately, otherwise
		//only execute if time since last command is greater than the the delay
		if(ID<Commands.FIRSTPAGE || ID>Commands.GOTO || lastCall+timeBetweenCalls<currentTime){

			lastCall = currentTime;
			currentCommands.executeCommand(ID, null);
		}
	}
}
