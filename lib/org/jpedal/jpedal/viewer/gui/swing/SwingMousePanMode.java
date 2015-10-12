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
 * SwingMousePanMode.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.swing;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.external.Options;
import org.jpedal.gui.GUIFactory;

public class SwingMousePanMode implements SwingMouseFunctionality{

	private Point currentPoint;
    private Rectangle currentView;
    private final PdfDecoder decode_pdf;
	
	public SwingMousePanMode(final PdfDecoder decode_pdf) {
		this.decode_pdf=decode_pdf;
	}
	
	@Override
    public void mouseClicked(final MouseEvent arg0) {
		
	}

	@Override
    public void mouseEntered(final MouseEvent arg0) {

	}
	
	@Override
    public void mouseExited(final MouseEvent arg0) {

	}

	@Override
    public void mousePressed(final MouseEvent arg0) {
        if (SwingUtilities.isLeftMouseButton(arg0) || 
        		SwingUtilities.isMiddleMouseButton(arg0)) {
            currentPoint = arg0.getPoint();
            currentView = decode_pdf.getVisibleRect();

            //set cursor
            final GUIFactory gui = ((SwingGUI)decode_pdf.getExternalHandler(Options.GUIContainer));
            decode_pdf.setCursor(gui.getGUICursor().getCursor(SwingGUI.GRABBING_CURSOR));
        }
	}

	@Override
    public void mouseReleased(final MouseEvent arg0) {
        //reset cursor
        final GUIFactory gui = ((SwingGUI)decode_pdf.getExternalHandler(Options.GUIContainer));
        decode_pdf.setCursor(gui.getGUICursor().getCursor(GUI.GRAB_CURSOR));
	}

	@Override
    public void mouseDragged(final MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) || 
        		SwingUtilities.isMiddleMouseButton(e)) {
            final Point newPoint = e.getPoint();

            final int diffX = currentPoint.x-newPoint.x;
            final int diffY = currentPoint.y-newPoint.y;


            final Rectangle view = currentView;

            view.x +=diffX;

            view.y +=diffY;



            if(!view.contains(decode_pdf.getVisibleRect())) {
                decode_pdf.scrollRectToVisible(view);
            }
        }
    }

	@Override
    public void mouseMoved(final MouseEvent e) {
		
	}

}