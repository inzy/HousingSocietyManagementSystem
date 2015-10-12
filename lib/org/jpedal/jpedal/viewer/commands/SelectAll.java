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
 * SelectAll.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.*;
import org.jpedal.display.Display;
import org.jpedal.examples.viewer.Values;
import org.jpedal.gui.GUIFactory;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.Messages;

/**
 * Selects and highlights everything in the document
 */
public class SelectAll {

    public static void execute(final GUIFactory currentGUI, final PdfDecoderInt decode_pdf, final Values commonValues) {
        if (decode_pdf.getDisplayView() == Display.SINGLE_PAGE) {

            final int[][] allHighlights = decode_pdf.getTextLines().getLineAreasAs2DArray(commonValues.getCurrentPage());
            final int coordX;
            final int coordY;
            final int width;
            final int height;
            if (allHighlights != null) { //breaks in legacy text mode so trap

                int top = 0;
                int bottom = 0;
                for (int r = 0; r != allHighlights.length; r++) {
                    if (allHighlights[r][1] > allHighlights[top][1]) {
                        top = r;
                    }
                    if (allHighlights[r][1] < allHighlights[bottom][1]) {
                        bottom = r;
                    }
                }

                coordX = allHighlights[top][0];
                coordY = allHighlights[top][1] + (allHighlights[top][3] / 2);
                height = (allHighlights[bottom][1] + (allHighlights[bottom][3] / 2)) - coordY;
                width = (allHighlights[bottom][0] + allHighlights[bottom][2]) - coordX;
                decode_pdf.getTextLines().clearHighlights();
                decode_pdf.getTextLines().addHighlights(allHighlights, true, commonValues.getCurrentPage());
            } else {
                //breaks in legacy text mode so trap
                height = decode_pdf.getPdfPageData().getCropBoxHeight(commonValues.getCurrentPage());
                width = decode_pdf.getPdfPageData().getCropBoxWidth(commonValues.getCurrentPage());
                coordX = decode_pdf.getPdfPageData().getCropBoxX(commonValues.getCurrentPage());
                coordY = decode_pdf.getPdfPageData().getCropBoxY(commonValues.getCurrentPage());
            }

            // values require to manipulate selected text
            commonValues.m_x1 = coordX;
            commonValues.m_x2 = coordX + width;
            commonValues.m_y1 = coordY;
            commonValues.m_y2 = coordY + height;

            // add an outline rectangle to the display
            final int[] currentRectangle = {coordX, coordY, width, height};

            // Remove all previous highlight areas
            decode_pdf.updateCursorBoxOnScreen(null, 0); // remove box

            decode_pdf.getPages().setHighlightedImage(null);// remove image highlight

            decode_pdf.updateCursorBoxOnScreen(currentRectangle, DecoderOptions.highlightColor.getRGB());

            //<start-fx>
            decode_pdf.repaintPane(commonValues.getCurrentPage());
            //<end-fx>
        } else {
            currentGUI.showMessageDialog(Messages.getMessage("PageLayoutMessage.SinglePageOnly"));
        }
    }

}
