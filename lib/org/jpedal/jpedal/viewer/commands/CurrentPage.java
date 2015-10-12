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
 * CurrentPage.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.PdfDecoderInt;
import org.jpedal.gui.GUIFactory;

/**
 *
 */
public class CurrentPage {

    public static Object execute(final PdfDecoderInt decode_pdf, final GUIFactory currentGUI) {

        Object status=-1;

        if (decode_pdf != null) {
            status=currentGUI.getValues().getCurrentPage();
        }

        return status;
    }
}
