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
 * SeperateCover.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.PdfDecoderInt;
import org.jpedal.display.Display;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.gui.GUIFactory;

/**
 *
 */
public class SeperateCover {

    public static void execute(final Object[] args, final PdfDecoderInt decode_pdf, final PropertiesFile properties, final GUIFactory currentGUI, final Values commonValues) {
        if (args == null) {
            //swap option
            decode_pdf.getPages().setBoolean(Display.BoolValue.SEPARATE_COVER, !decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER));

            //update view
            if (decode_pdf.getDisplayView() == Display.FACING) {
                Facing.execute(args, decode_pdf, currentGUI, commonValues);
            }

            //set properties file
            if (decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) {
                properties.setValue("separateCoverOn", "true");
            } else {
                properties.setValue("separateCoverOn", "false");
            }
        } else {

        }
    }
}
