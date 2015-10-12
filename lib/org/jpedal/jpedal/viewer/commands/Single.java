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
 * Single.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.*;
import org.jpedal.display.Display;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.gui.GUIFactory;

/**
 *
 */
public class Single {

    public static void execute(final Object[] args, final PdfDecoderInt decode_pdf, final GUIFactory currentGUI) {
        if (!decode_pdf.isOpen() || decode_pdf.getDisplayView()==Display.SINGLE_PAGE) {
            return;
        }

        if (args == null) {

            currentGUI.getCombo(Commands.SCALING).setEnabled(true);
            currentGUI.getCombo(Commands.ROTATION).setEnabled(true);

            currentGUI.getButtons().getButton(Commands.MOUSEMODE).setEnabled(true);
            currentGUI.getButtons().getButton(Commands.SNAPSHOT).setEnabled(true);

            currentGUI.getButtons().alignLayoutMenuOption(Display.SINGLE_PAGE);

            if(Viewer.isFX()){
                ModeChange.changeModeInJavaFX(Display.SINGLE_PAGE,decode_pdf,currentGUI,null,null,null);
            }else{
                ModeChange.changeModeInSwing(Display.SINGLE_PAGE,decode_pdf,currentGUI,null,null,null);
            }
            
            currentGUI.resetRotationBox();
            currentGUI.scaleAndRotate();

            //Change the page number for single page mode
            //This ensures facing mode number is replaced with a single number
            currentGUI.setPageNumber();

        }
    }
}
