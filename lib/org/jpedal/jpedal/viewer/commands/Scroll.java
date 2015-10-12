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
 * Scroll.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.awt.Rectangle;
import org.jpedal.*;
import org.jpedal.display.Display;
import org.jpedal.examples.viewer.Values;
import org.jpedal.objects.PdfPageData;

/**
 * Allows the user to scroll through the PDF document at a scroll interval
 * scroll interval is default of 10 unless changed.
 */
public class Scroll {

    public static void execute(final Object[] args, final Values commonValues, final PdfDecoderInt decode_pdf) {
        if (args == null) {

        } else {
            final int[] scrollTo = (int[]) args[0];
            int page = commonValues.getCurrentPage();
            if (args.length > 1 && args[1] != null) {
                page = (Integer) args[1];
            }

            if (scrollTo != null) {
                rectToHighlight(scrollTo, page, decode_pdf);
                //<start-fx>
                ((PdfDecoder)decode_pdf).invalidate();
                ((PdfDecoder)decode_pdf).repaint();
                //<end-fx>
            }
        }

    }

    public static void rectToHighlight(final int[] highlight, int page, final PdfDecoderInt decode_pdf) {
        int x = 0, y = 0, w = 0, h = 0;
        final int insetW = decode_pdf.getInsetW();
        final int insetH = decode_pdf.getInsetH();
        final float scaling = decode_pdf.getScaling();
        final int scrollInterval = decode_pdf.getScrollInterval();

        final int displayView = decode_pdf.getDisplayView();
        if (page < 1 || page > decode_pdf.getPageCount() || displayView == Display.SINGLE_PAGE) {
            page = decode_pdf.getPageNumber();
        }

        final PdfPageData pageData = decode_pdf.getPdfPageData();
        final int cropW = pageData.getCropBoxWidth(page);
        final int cropH = pageData.getCropBoxHeight(page);
        final int cropX = pageData.getCropBoxX(page);
        final int cropY = pageData.getCropBoxY(page);

        switch (decode_pdf.getDisplayRotation()) {
            case 0:
                x = (int) ((highlight[0] - cropX) * scaling) + insetW;
                y = (int) ((cropH - (highlight[1] - cropY)) * scaling) + insetH;
                w = (int) (highlight[2] * scaling);
                h = (int) (highlight[3] * scaling);

                break;
            case 90:
                x = (int) ((highlight[1] - cropY) * scaling) + insetH;
                y = (int) ((highlight[0] - cropX) * scaling) + insetW;
                w = (int) (highlight[3] * scaling);
                h = (int) (highlight[2] * scaling);

                break;
            case 180:
                x = (int) ((cropW - (highlight[0] - cropX)) * scaling) + insetW;
                y = (int) ((highlight[1] - cropY) * scaling) + insetH;
                w = (int) (highlight[2] * scaling);
                h = (int) (highlight[3] * scaling);

                break;
            case 270:
                x = (int) ((cropH - (highlight[1] - cropY)) * scaling) + insetH;
                y = (int) ((cropW - (highlight[0] - cropX)) * scaling) + insetW;
                w = (int) (highlight[3] * scaling);
                h = (int) (highlight[2] * scaling);

                break;
        }

        if (displayView != Display.SINGLE_PAGE && displayView != Display.PAGEFLOW) {
            x += decode_pdf.getPages().getXCordForPage(page);
            y += decode_pdf.getPages().getYCordForPage(page);
        }

        //<start-fx>
        final Rectangle visibleRect = ((PdfDecoder)decode_pdf).getVisibleRect();
        if (x > visibleRect.x + (visibleRect.width / 2)) {
            x += ((visibleRect.width / 2) - (highlight[2] / 2));
        } else {
            x -= ((visibleRect.width / 2) - (highlight[2] / 2));
        }

        if (y > visibleRect.y + (visibleRect.height / 2)) {
            y += ((visibleRect.height / 2) - (highlight[3] / 2));
        } else {
            y -= ((visibleRect.height / 2) - (highlight[3] / 2));
        }

        final Rectangle scrollto = new Rectangle(x - scrollInterval, y - scrollInterval, w + scrollInterval * 2, h + scrollInterval * 2);

        ((PdfDecoder)decode_pdf).scrollRectToVisible(scrollto);
        //<end-fx>
    }
}
