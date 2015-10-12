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
 * Rotation.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.examples.viewer.Values;
import org.jpedal.gui.GUIFactory;

/**
 * Rotates the current document in the Viewer This class is Generic
 * and does not need a specific Swing/JavaFX implementation.
 */
public class Rotation {

    public static void execute(final Object[] args, final GUIFactory currentGUI, final Values commonValues) {
        if (args == null) {
            if (commonValues.getSelectedFile() != null) {
                currentGUI.rotate();
            }
        } else {
            final int rotation = Integer.parseInt((String) args[0]);
            while (Values.isProcessing()) {
                // wait while we rotate your document
                try {
                    Thread.sleep(100);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            currentGUI.setRotationFromExternal(rotation);
            currentGUI.scaleAndRotate();
        }
    }

}
