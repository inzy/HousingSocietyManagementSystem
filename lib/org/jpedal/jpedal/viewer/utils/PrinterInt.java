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
 * PrinterInt.java
 * ---------------
 */
package org.jpedal.examples.viewer.utils;

import org.jpedal.PdfDecoderInt;
import org.jpedal.gui.GUIFactory;

public interface PrinterInt {
    
    public void printPDF(PdfDecoderInt decodePdf, GUIFactory currentGUI, String blacklist, String defaultPrinter);
    
}
