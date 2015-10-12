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
 * Scaling.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.SwingUtilities;
import org.jpedal.*;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.*;
import org.jpedal.gui.GUIFactory;

/**
 *
 */
public class Scaling {

    public static void execute(final Object[] args, final Values commonValues, final PdfDecoderInt decode_pdf, final GUIFactory currentGUI, final ViewStack viewStack) {

        //<start-fx>
        if (args == null) {
            if ((!Values.isProcessing()) && (commonValues.getSelectedFile() != null)) {
                	//store centre location
                	final Rectangle r = ((PdfDecoder)decode_pdf).getVisibleRect();
                    final Point px = ((SwingGUI)currentGUI).convertComponentCoordsToPageCoords((int)(r.getX() + (r.getWidth() / 2)), (int)(r.getY() + (r.getHeight() / 2)), decode_pdf.getPageNumber());

                    //scale
                    currentGUI.scaleAndRotate();

                    final float scaling = currentGUI.getScaling();
                    //center on stored location
                    final Thread t = new Thread() {
                        @Override
                        public void run() {
                            try {
                            	
                            	final Rectangle area = new Rectangle(
                            			(int)(decode_pdf.getPages().getXCordForPage(decode_pdf.getPageNumber())+(px.x*scaling)- (r.getWidth() / 2)),
                            			(int)(decode_pdf.getPages().getYCordForPage(decode_pdf.getPageNumber())+((decode_pdf.getPdfPageData().getCropBoxHeight(decode_pdf.getPageNumber())-px.y)*scaling)- (r.getHeight() / 2)),
                            			(int) ((PdfDecoder)decode_pdf).getVisibleRect().getWidth(),
                            			(int) ((PdfDecoder)decode_pdf).getVisibleRect().getHeight());
                            	viewStack.add(-1, area, null);
                                ((PdfDecoder)decode_pdf).scrollRectToVisible(area);
                                ((PdfDecoder)decode_pdf).repaint();

                            } catch (final Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    t.setDaemon(true);
                    SwingUtilities.invokeLater(t);
                }
 
        } else {

        	currentGUI.setScalingFromExternal((String) args[0]);

        }
        //<end-fx>
    }
}
