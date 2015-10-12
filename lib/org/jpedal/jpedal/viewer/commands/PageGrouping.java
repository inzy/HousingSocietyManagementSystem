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
 * PageGrouping.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.PdfDecoderInt;
import org.jpedal.exception.PdfException;

/**
 * Group Pages in Viewer
 */
public class PageGrouping {

    public static Object execute(final Object[] args, final PdfDecoderInt decode_pdf) {
        
        Object status = null;
        
        if (args != null) {

            final int i = (Integer) args[0];
            if (i == decode_pdf.getlastPageDecoded()) {
                try {
                    status = decode_pdf.getGroupingObject();
                } catch (final PdfException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    decode_pdf.decodePageInBackground(i);
                    status = decode_pdf.getBackgroundGroupingObject();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
            //ensure done
            decode_pdf.waitForDecodingToFinish();
        }
        
        return status;
        
    }
}
