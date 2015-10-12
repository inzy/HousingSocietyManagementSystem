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
 * Highlight.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.*;

/**
 * This class allows you to Highlight a selected text/image 
 * using Rectangles by getting the text lines.
 */
public class Highlight {

    public static void execute(final Object[] args, final PdfDecoderInt decode_pdf) {
        
        decode_pdf.getTextLines().clearHighlights();

        if (args != null) {

            final int[][] highlights = (int[][]) args[0];
            final int page = (Integer) args[1];
            boolean areaSelect = true;
            if (args.length > 2) {
                areaSelect = (Boolean) args[2];
            }
            //decode_pdf.getTextLines().clearHighlights();

            //add text highlight
            decode_pdf.getTextLines().addHighlights(highlights, areaSelect, page);

            //highlights[0].x=1;
            //                decode_pdf.scrollRectToHighlight(highlights[0]);
            //<start-fx>
            ((PdfDecoder)decode_pdf).invalidate();
            ((PdfDecoder)decode_pdf).repaint();
            //<end-fx>
        }
    }
}
