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
 * Preferences.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.examples.viewer.gui.popups.SwingProperties;
import org.jpedal.gui.GUIFactory;

/**
 * Shows the Preferences Dialog Box
 */
public class Preferences {

    public static void execute(final Object[] args, final GUIFactory currentGUI) {
        if (args == null) {
            new SwingProperties(currentGUI);
        } else {

        }
    }
}
