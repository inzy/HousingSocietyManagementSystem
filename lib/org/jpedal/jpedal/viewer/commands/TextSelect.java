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
 * TextSelect.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.net.URL;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.MouseMode;
import org.jpedal.gui.GUIFactory;

/**
 * Enable Text Selection in Viewer
 */
public class TextSelect {

    public static void execute(final Object[] args, final GUIFactory currentGUI, final MouseMode mouseMode) {
        if (args == null) {

            //Disable Pan Mode
            currentGUI.getMenuItems().setCheckMenuItemSelected(Commands.PANMODE, false);
            currentGUI.getButtons().getButton(Commands.SNAPSHOT).setEnabled(true);
            
            //Set mouse mode
            mouseMode.setMouseMode(MouseMode.MOUSE_MODE_TEXT_SELECT);
            
            //Update buttons
            final URL url = currentGUI.getGUICursor().getURLForImage("mouse_select.png");
            if (url != null) {
                currentGUI.getButtons().getButton(Commands.MOUSEMODE).setIcon(url);
            }
        } else {

        }
    }
}
