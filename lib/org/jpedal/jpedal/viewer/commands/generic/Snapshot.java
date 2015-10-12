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
 * Snapshot.java
 * ---------------
 */

package org.jpedal.examples.viewer.commands.generic;

import org.jpedal.*;
import org.jpedal.display.Display;
import org.jpedal.gui.GUIFactory;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.Messages;

/**
 * Takes an Image Snapshot of the Selected Area
 */
public class Snapshot {
    public static boolean execute(final Object[] args, final GUIFactory currentGUI, final PdfDecoderInt decode_pdf, boolean extractingAsImage) {
        if (args == null) {

            if (!decode_pdf.isOpen()) {
                currentGUI.showMessageDialog("File must be open before you can snapshot.");
            } else {

                if (decode_pdf.getDisplayView() != Display.SINGLE_PAGE) {
                    currentGUI.showMessageDialog(Messages.getMessage("PageLayoutMessage.SinglePageOnly"));
                } else {
                    extractingAsImage = true;
                    DecoderOptions.showMouseBox = true;
                }
            }
        }

        return extractingAsImage;
    }
}
