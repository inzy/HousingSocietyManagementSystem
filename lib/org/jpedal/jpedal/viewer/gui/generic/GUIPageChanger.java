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
 * GUIPageChanger.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.generic;

import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.GUI;

/**
 * Used 
 * 
 * @author Simon
 */
public abstract class GUIPageChanger {
    
    private final GUI gui;
    private final Values commonValues;
    private final int page;
    
    public GUIPageChanger(final GUI gui, final Values values, final int page){
        this.gui = gui;
        this.commonValues = values;
        this.page = page+1;
    }
    
    protected void handlePageChange(){
		if((!Values.isProcessing())&&(commonValues.getCurrentPage()!=page)){

            //if loading on linearized thread, see if we can actually display
            if(!gui.getPdfDecoder().isPageAvailable(page)){
                gui.showMessageDialog("Page "+page+" is not yet loaded");
                return;
            }
            gui.resetStatusMessage("");
            gui.getCommand().executeCommand(Commands.GOTO, new Object[]{Integer.toString(page)});
		}
        
    }
}
