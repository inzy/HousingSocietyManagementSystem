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
 * Toggle.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.*;

/**
 *
 */
public class Toggle {

    public static void execute(final Object[] args, final PdfDecoderInt decode_pdf) {

        if (args == null) {
            //<start-fx>
            ((PdfDecoder)decode_pdf).repaint();
            //<end-fx>
        } else {

        }
    }
}
