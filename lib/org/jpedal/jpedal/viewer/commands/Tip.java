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
 * Tip.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.awt.Container;
import org.jpedal.examples.viewer.gui.popups.TipOfTheDay;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.gui.GUIFactory;

/**
 * Code to handle the Tip of the day feature we display in theViewer
 */
public class Tip {

    public static void execute(final Object[] args, final GUIFactory currentGUI, final PropertiesFile properties) {
        if(args==null){
                    final TipOfTheDay tipOfTheDay = new TipOfTheDay((Container)currentGUI.getFrame(), "/org/jpedal/examples/viewer/res/tips", properties);
                    tipOfTheDay.setVisible(true);
                }else{
                    
                }
    }
}
