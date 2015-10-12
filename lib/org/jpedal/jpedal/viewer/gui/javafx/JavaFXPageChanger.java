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
 * JavaFXPageChanger.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.examples.viewer.gui.generic.GUIPageChanger;

/**
 *
 * @author Simon
 */
public class JavaFXPageChanger extends GUIPageChanger implements EventHandler<ActionEvent> {

    public JavaFXPageChanger(final GUI gui, final Values values, final int page) {
        super(gui, values, page);
    }

    @Override
    public void handle(final ActionEvent event) {
        super.handlePageChange();
    }
    
}
