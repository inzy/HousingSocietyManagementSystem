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
 * SwingPageChanger.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.examples.viewer.gui.generic.GUIPageChanger;

public class SwingPageChanger extends GUIPageChanger implements ActionListener{

    public SwingPageChanger(final GUI gui, final Values values, final int page) {
        super(gui, values, page);
    }

	@Override
    public void actionPerformed(final ActionEvent e) {
		super.handlePageChange();
	}

}
