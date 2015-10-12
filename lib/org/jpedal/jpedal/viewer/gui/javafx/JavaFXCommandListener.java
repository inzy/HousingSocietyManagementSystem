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
 * JavaFXCommandListener.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.gui.generic.GUIButton;

/**
 *
 */
public class JavaFXCommandListener implements EventHandler<ActionEvent> {

    
    final Commands currentCommands;
    
    public JavaFXCommandListener(final Commands currentCommands){
        this.currentCommands = currentCommands;
    }
    
    @Override
    public void handle(final ActionEvent t) {

        final Object source = t.getSource();
        final int ID;
        if (source instanceof GUIButton) {
            ID = ((GUIButton) source).getID();
        } else if (source instanceof JavaFXMenuItem) {
            ID = ((JavaFXMenuItem) source).getID();
        } else if (source instanceof JavaFXCombo) {
            ID = ((JavaFXCombo) source).getID();
        } else {
            ID = ((JavaFXID) source).getID();
        }

        currentCommands.executeCommand(ID, null);
    }
    
}
