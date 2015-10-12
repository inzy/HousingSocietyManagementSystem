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
 * JavaFXMouseFunctionality.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.javafx;

import javafx.scene.input.MouseEvent;

@SuppressWarnings("UnusedParameters")
public interface JavaFXMouseFunctionality {

    public void mouseClicked(MouseEvent e);

    public void mouseEntered(MouseEvent e);

    public void mouseExited(MouseEvent e);

    public void mousePressed(MouseEvent e);

    public void mouseReleased(MouseEvent e);

    public void mouseDragged(MouseEvent e);

    public void mouseMoved(MouseEvent e);

}
