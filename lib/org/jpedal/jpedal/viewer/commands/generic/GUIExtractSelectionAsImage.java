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
 * GUIExtractSelectionAsImage.java
 * ---------------
 */


package org.jpedal.examples.viewer.commands.generic;

import java.awt.image.BufferedImage;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.gui.GUIFactory;
import org.jpedal.parser.DecoderOptions;

/**
 * This is a generic class which holds generic code for
 * Extracting the drawn CursorBox as an Image.
 */
public class GUIExtractSelectionAsImage {
    
    protected static BufferedImage snapShot;
    
    
    /**
     * Generic method to extract selected area 
     * as a rectangle and show onscreen.
     * 
     * Swing needs extracting into ExtractSelectionAsImage.
     */
    protected static void extractSelectedScreenAsImage(final Values commonValues, final GUIFactory currentGUI, final PdfDecoderInt decode_pdf) {
        
        /**ensure co-ords in right order*/
        int t_x1=commonValues.m_x1;
        int t_x2=commonValues.m_x2;
        int t_y1=commonValues.m_y1;
        int t_y2=commonValues.m_y2;
        
        if(commonValues.m_y1<commonValues.m_y2){
            t_y2=commonValues.m_y1;
            t_y1=commonValues.m_y2;
        }
        
        if(commonValues.m_x1>commonValues.m_x2){
            t_x2=commonValues.m_x1;
            t_x1=commonValues.m_x2;
        }
        float scaling = 100;
        
        if(DecoderOptions.isRunningOnWindows) {
            scaling = 100 * currentGUI.getScaling();
        }
        
        
        snapShot=decode_pdf.getSelectedRectangleOnscreen(t_x1,t_y1,t_x2,t_y2,scaling);
        
    }
}
