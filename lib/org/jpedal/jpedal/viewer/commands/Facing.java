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
 * Facing.java
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
import org.jpedal.utils.Messages;

/**
 *
 */
public class Facing {

    /*Booleans for Commands*/
    private static boolean irregularSizesWarningShown;
    
    public static void execute(final Object[] args, final PdfDecoderInt decode_pdf, final GUIFactory currentGUI, final Values commonValues) {

        if (!decode_pdf.isOpen() || decode_pdf.getDisplayView()==Display.FACING) {
            return;
        }

        if (args == null) {
        	final String defaultValue = currentGUI.getProperties().getValue("enhancedFacingMode");
        	
        	if (Boolean.parseBoolean(defaultValue)) {
                if (decode_pdf.getPdfPageData().hasMultipleSizes()) {
                    if (!irregularSizesWarningShown) {
                        currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.PageDragIrregularSizes"));
                        irregularSizesWarningShown = true;
                    }
                    decode_pdf.getPages().setBoolean(Display.BoolValue.TURNOVER_ON, false);
                } else {
                    decode_pdf.getPages().setBoolean(Display.BoolValue.TURNOVER_ON, true);
                }
            }

            currentGUI.getCombo(Commands.SCALING).setEnabled(true);
            currentGUI.getCombo(Commands.ROTATION).setEnabled(false);

            currentGUI.getButtons().getButton(Commands.MOUSEMODE).setEnabled(true);
            currentGUI.getButtons().getButton(Commands.SNAPSHOT).setEnabled(true);

            //Fit to page
            ((GUI)currentGUI).setSelectedComboIndex(Commands.SCALING, 0);
            currentGUI.scaleAndRotate();

            currentGUI.getButtons().alignLayoutMenuOption(Display.FACING);
          
            if(Viewer.isFX()){
               // ModeChange.changeModeInJavaFX(Display.SINGLE_PAGE,decode_pdf,currentGUI);
            }else{
                ModeChange.changeModeInSwing(Display.FACING,decode_pdf,currentGUI,commonValues, null, null);
            }
            
        }
    }
}
