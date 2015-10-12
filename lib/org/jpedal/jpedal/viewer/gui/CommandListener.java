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
 * CommandListener.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui;


import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.gui.javafx.JavaFXCommandListener;
//<start-fx>
import org.jpedal.examples.viewer.gui.swing.SwingCommandListener;
//<end-fx>

/**
 * This class is a wrapper for SwingCommandListener and JavaFXCommandListener.
 * single listener to execute all GUI commands and call Commands to execute
 */
public class CommandListener {
	
    JavaFXCommandListener commandListenerFX;
    //<start-fx>
    SwingCommandListener commandListenerSwing;
	//<end-fx>

	public CommandListener(final Commands currentCommands) {
        if(Viewer.isFX()){
            commandListenerFX = new JavaFXCommandListener(currentCommands);
        }else{
            //<start-fx>
            commandListenerSwing = new SwingCommandListener(currentCommands);
            //<end-fx>
        }
	}

    //<start-fx>
    public SwingCommandListener getSwingCommandListener(){
        return commandListenerSwing;
    }
    //<end-fx>

    public JavaFXCommandListener getJavaFXCommandListener(){
        return commandListenerFX;
    }
    
}
