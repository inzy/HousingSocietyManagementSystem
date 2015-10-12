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
 * PanMode.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.net.URL;
import org.jpedal.*;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.MouseMode;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.gui.GUIFactory;

/**
 * Enables Pan Mode in the Viewer which enables the user to Pan the Document
 */
public class PanMode {

    public static void execute(final Object[] args, final GUIFactory currentGUI, final MouseMode mouseMode, final PdfDecoderInt decode_pdf) {
        if (args == null) {

            //Disable TextSelection
            currentGUI.getMenuItems().setCheckMenuItemSelected(Commands.TEXTSELECT, false);
            currentGUI.getButtons().getButton(Commands.SNAPSHOT).setEnabled(false);
            
            //Set Mouse Mode
            mouseMode.setMouseMode(MouseMode.MOUSE_MODE_PANNING);

            //Update buttons
            final URL url = currentGUI.getGUICursor().getURLForImage("mouse_pan.png");
            if (url != null) {
                currentGUI.getButtons().getButton(Commands.MOUSEMODE).setIcon(url);
            }

            //Update Cursor
            if(Viewer.isFX()){
                //not yet implemented
               // ((PdfDecoderFX)decode_pdf).setDefaultCursor(currentGUI.getGUICursor().getCursor(GUI.GRAB_CURSOR));
            }else{
                //<start-fx>
                ((PdfDecoder)decode_pdf).setDefaultCursor(currentGUI.getGUICursor().getCursor(GUI.GRAB_CURSOR));
                //<end-fx>
            }
            
        } else {

        }
    }
}
