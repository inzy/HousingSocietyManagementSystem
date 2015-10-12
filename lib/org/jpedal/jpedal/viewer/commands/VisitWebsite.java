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
 * VisitWebsite.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.BrowserLauncher;
import org.jpedal.utils.Messages;

/**
 * This class is generic for both Swing and JavaFX.
 */
public class VisitWebsite {

    public static void execute(final Object[] args, final GUIFactory currentGUI) {
        if (args == null) {
            try {
                BrowserLauncher.openURL(Messages.getMessage("PdfViewer.VisitWebsite"));
            } catch (final Exception e1) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.ErrorWebsite"));
            }

        }
    }
}
