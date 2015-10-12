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
 * Cascade.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.LogWriter;

/**
 * Cascades all the window frames
 */
public class Cascade {

    public static void execute(final Object[] args, final GUIFactory currentGUI) {
        if (args == null) {
            cascade(currentGUI);
        } else {

        }

    }
    
    private static void cascade(final GUIFactory currentGUI) {
        final JDesktopPane desktopPane = (JDesktopPane)currentGUI.getMultiViewerFrames();

        final JInternalFrame[] frames = desktopPane.getAllFrames();

        /**
         * reverse the order of these frames, so when they are cascaded they
         * will maintain the order they were to start with
         */
        for (int left = 0, right = frames.length - 1; left < right; left++, right--) {
            // exchange the first and last
            final JInternalFrame temp = frames[left];
            frames[left] = frames[right];
            frames[right] = temp;
        }

        int x = 0;
        int y = 0;
        final int width = desktopPane.getWidth() / 2;
        final int height = desktopPane.getHeight() / 2;

        for (final JInternalFrame frame : frames) {
            if (!frame.isIcon()) { // if its minimized leave it there
                try {
                    frame.setMaximum(false);
                    frame.reshape(x, y, width, height);
                    frame.setSelected(true);

                    x += 25;
                    y += 25;
                    // wrap around at the desktop edge
                    if (x + width > desktopPane.getWidth()) {
                        x = 0;
                    }
                    if (y + height > desktopPane.getHeight()) {
                        y = 0;
                    }
                } catch (final PropertyVetoException e) {
                    
                     if(LogWriter.isOutput()) { 
                         LogWriter.writeLog("Exception attempting to set size" + e); 
                     } 
                    
               }
            }
    }
}

}
