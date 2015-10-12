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
 * JavaFXScaling.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands.javafx;

import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.gui.GUIFactory;

/**
 * This class controls how the viewer content is scaled, It can be scaled by
 * either a set percentage or by width/height/page, The main scaling is
 * performed depending on the index variable value in the scaleAndRotate method
 * in either JavaFXGUI/SwingGUI.
 */
public class JavaFXScaling {

    public static void execute(final Object[] args, final Values commonValues, final PdfDecoderInt decode_pdf, final GUIFactory currentGUI) {

        if (args == null) {
            if (!Values.isProcessing() && commonValues.getSelectedFile() != null) {

                currentGUI.scaleAndRotate();
            }
        } else {
            currentGUI.setScalingFromExternal((String) args[0]);
            currentGUI.scaleAndRotate();
            while (Values.isProcessing()) {
                // wait while we scale your document
                try {
                    Thread.sleep(100);
                } catch (final InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

}
