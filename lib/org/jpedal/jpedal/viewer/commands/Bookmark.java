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
 * Bookmark.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.*;
import org.jpedal.gui.GUIFactory;

/**
 * Bookmark the current page in the Viewer
 */
public class Bookmark {

    public static void execute(final Object[] args, final GUIFactory currentGUI, final PdfDecoderInt decode_pdf) {
        //Only works if a bookmark is specified and the currentGUI is not null
        if (args.length >= 1 && currentGUI != null) {
            final String bookmark = (String) args[0];

            currentGUI.setBookmarks(true);

            final String page = currentGUI.getBookmark(bookmark);

            if (page != null) {
                final int p = Integer.parseInt(page);

                try {
                    decode_pdf.decodePage(p);
                    //<start-fx>
                    ((PdfDecoder)decode_pdf).repaint();
                    //<end-fx>
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
