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
 * SwingMouseFunctionality.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.swing;

import java.awt.event.MouseEvent;

@SuppressWarnings("UnusedParameters")
public interface SwingMouseFunctionality {

	public void mouseClicked(MouseEvent e);

	public void mouseEntered(MouseEvent e);

	public void mouseExited(MouseEvent e);

	public void mousePressed(MouseEvent e);

	public void mouseReleased(MouseEvent e);

	public void mouseDragged(MouseEvent e);

	public void mouseMoved(MouseEvent e);
	
}
