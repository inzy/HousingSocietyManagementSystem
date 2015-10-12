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
 * SetTextColor.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.util.HashMap;
import java.util.Map;
import org.jpedal.PdfDecoderInt;
import org.jpedal.constants.JPedalSettings;
import org.jpedal.exception.PdfException;

/**
 * Sets the documents text color in the Viewer
 */
public class SetTextColor {

    public static void execute(final Object[] args, final PdfDecoderInt decode_pdf) {

        try {
            final Map map = new HashMap();
            map.put(JPedalSettings.TEXT_COLOR, args[0]);
            decode_pdf.modifyNonstaticJPedalParameters(map);
        } catch (final PdfException e2) {

            e2.printStackTrace();
        }

    }
}
