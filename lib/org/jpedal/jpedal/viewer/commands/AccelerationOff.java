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
 * AccelerationOff.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.PdfDecoder;
import org.jpedal.PdfDecoderInt;

/**
 * Toggles Acceleration Off in the Viewer
 */
public class AccelerationOff {

    public static void execute(final Object[] args, final PdfDecoderInt decode_pdf) {

        if (args == null) {
            decode_pdf.setHardwareAccelerationforScreen(false);
            ((PdfDecoder)decode_pdf).invalidate();
            ((PdfDecoder)decode_pdf).repaint();
        } else {

        }
    }
}
