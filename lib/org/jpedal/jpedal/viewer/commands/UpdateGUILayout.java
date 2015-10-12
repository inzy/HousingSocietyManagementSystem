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
 * UpdateGUILayout.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.gui.GUIFactory;

/**
 * Updates the layout of the page
 */
public class UpdateGUILayout {

    public static void execute(final Object[] args, final GUIFactory currentGUI) {
        if (args == null) {

        } else {
            String value = null;
            boolean show = false;

            if (args[0] instanceof String) {
                value = (String) args[0];
            }

            if (args[1] instanceof Boolean) {
                show = (Boolean) args[1];
            }

            if (value != null) {
                currentGUI.alterProperty(value, show);
            } else {
                throw new RuntimeException("String input was null");
            }
        }

    }
}
