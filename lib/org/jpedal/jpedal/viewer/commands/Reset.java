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
 * Reset.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;

/**
 *
 */
public class Reset {

    public static void execute(final Object[] args, final Values commonValues, final PdfDecoderInt decode_pdf) {

        if (args == null) {
            decode_pdf.resetViewableArea();
            commonValues.viewportScale = 1;
            commonValues.dx = 0;
            commonValues.dy = 0;
            commonValues.maxViewY = 0;
        } else {

        }
    }
}
