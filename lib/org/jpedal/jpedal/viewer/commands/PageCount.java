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
 * PageCount.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.PdfDecoderInt;

/**
 * Count the number of pages the current document in the Viewer has
 */
public class PageCount {

    public static Object execute(final PdfDecoderInt decode_pdf) {

        final Object status;
        
        if (decode_pdf == null) {
            status = -1;
        } else {
            status = decode_pdf.getPageCount();
        }
        
        return status;
    }
}
