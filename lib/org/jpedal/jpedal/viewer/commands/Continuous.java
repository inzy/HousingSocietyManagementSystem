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
 * Continuous.java
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
public class Continuous {

    public static void execute(final PdfDecoderInt decode_pdf, final GUIFactory currentGUI, final Object[] args) {
        if (!decode_pdf.isOpen() || decode_pdf.getDisplayView()==Display.CONTINUOUS) {
            return;
        }

        if (args == null) {

            currentGUI.getCombo(Commands.SCALING).setEnabled(true);
            currentGUI.getCombo(Commands.ROTATION).setEnabled(true);

            currentGUI.getButtons().getButton(Commands.MOUSEMODE).setEnabled(true);
            currentGUI.getButtons().getButton(Commands.SNAPSHOT).setEnabled(true);

            currentGUI.getButtons().alignLayoutMenuOption(Display.CONTINUOUS);
            
            if(Viewer.isFX()){
                ModeChange.changeModeInJavaFX(Display.CONTINUOUS,decode_pdf,currentGUI,null,null,null);
            }else{
                ModeChange.changeModeInSwing(Display.CONTINUOUS,decode_pdf,currentGUI,null, null, null);
            }
            
        } else {

        }

    }

    
}
