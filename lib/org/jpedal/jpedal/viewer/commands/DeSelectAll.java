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
 * DeSelectAll.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.PdfDecoderInt;
import org.jpedal.display.Display;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.Messages;

/**
 * Deselects the text/image a user has highlighted in the Viewer
 */
public class DeSelectAll {

    public static void execute(final GUIFactory currentGUI, final PdfDecoderInt decode_pdf) {
        if (decode_pdf.getDisplayView() == Display.SINGLE_PAGE) {
        	
            /**
             * remove any outline and reset variables used to track change
             */
            decode_pdf.getTextLines().clearHighlights(); //remove highlighted text
            decode_pdf.repaintPane(0);
            decode_pdf.getPages().setHighlightedImage(null);// remove image highlight
            decode_pdf.getPages().refreshDisplay();
        } else {
            currentGUI.showMessageDialog(Messages.getMessage("PageLayoutMessage.SinglePageOnly"));
        }
    }
}
