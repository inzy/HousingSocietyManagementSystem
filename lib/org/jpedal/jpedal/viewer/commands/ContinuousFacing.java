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
 * ContinuousFacing.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.*;
import org.jpedal.display.Display;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.gui.GUIFactory;

/**
 *
 */
public class ContinuousFacing {

    public static void execute(final Object[] args, final PdfDecoderInt decode_pdf, final GUIFactory currentGUI, final Values commonValues) {
        if (!decode_pdf.isOpen() || decode_pdf.getDisplayView()==Display.CONTINUOUS_FACING) {
            return;
        }

        if (args == null) {

            currentGUI.getCombo(Commands.SCALING).setEnabled(true);
            currentGUI.getCombo(Commands.ROTATION).setEnabled(true);

            currentGUI.getButtons().getButton(Commands.MOUSEMODE).setEnabled(true);
            currentGUI.getButtons().getButton(Commands.SNAPSHOT).setEnabled(true);

            //Fit to page
            //Set combo ID to -1 to avoid the actionListener being called which would change the position
            currentGUI.getCombo(Commands.SCALING).setID(-1);
            ((GUI)currentGUI).setSelectedComboIndex(Commands.SCALING, 0);
            currentGUI.getCombo(Commands.SCALING).setID(Commands.SCALING);
            currentGUI.scaleAndRotate();

            currentGUI.getButtons().alignLayoutMenuOption(Display.CONTINUOUS_FACING);
          
             if(Viewer.isFX()){
                ModeChange.changeModeInJavaFX(Display.CONTINUOUS_FACING,decode_pdf,currentGUI,null,null,null);
            }else{
                ModeChange.changeModeInSwing(Display.CONTINUOUS_FACING,decode_pdf,currentGUI,commonValues, null, null);
            }
            currentGUI.updateTextBoxSize();

        } else {

        }

    }
}
