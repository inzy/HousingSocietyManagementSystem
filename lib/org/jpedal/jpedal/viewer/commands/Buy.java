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
 * Buy.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.BrowserLauncher;

/**
 * Code to handle Buy button we display in demo versions of Viewer
 */
public class Buy {

    public static void execute(final Object[] args, final GUIFactory currentGUI) {
        if (args == null) {
            try {
                BrowserLauncher.openURL("http://www.idrsolutions.com/jpedal-pricing/");
            } catch (final Exception e1) {
                currentGUI.showMessageDialog("Please visit http://www.idrsolutions.com/jpedal-pricing/");
            }
        }
    }
}
