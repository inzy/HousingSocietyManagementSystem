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
 * MouseMode.java
 * ---------------
 */
package org.jpedal.examples.viewer;

/**
 * tracks option for our Viewer
 */
public class MouseMode {

    public static final int MOUSE_MODE_TEXT_SELECT = 0;

    public static final int MOUSE_MODE_PANNING = 1;

    int mouseMode;


    public int getMouseMode() {
        return mouseMode;
    }

    public void setMouseMode(final int mouseMode) {
        this.mouseMode = mouseMode;
    }


}
