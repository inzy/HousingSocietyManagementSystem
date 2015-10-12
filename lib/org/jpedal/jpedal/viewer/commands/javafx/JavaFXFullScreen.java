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
 * JavaFXFullScreen.java
 * ---------------
 */

package org.jpedal.examples.viewer.commands.javafx;

import javafx.stage.Stage;
import org.jpedal.gui.GUIFactory;

/**
 * Toggles between FullScreen.
 */
public class JavaFXFullScreen {

    private static boolean toggler = true;
    
    public static void execute(final Object[] args, final GUIFactory currentGUI) {
        if (args == null) {
            ((Stage)currentGUI.getFrame()).setFullScreen(toggler);
            toggler = !toggler;
        }
    }
}
