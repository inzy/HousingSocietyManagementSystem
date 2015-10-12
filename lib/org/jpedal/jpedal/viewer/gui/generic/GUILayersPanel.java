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
 * GUILayersPanel.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.generic;

import org.jpedal.PdfDecoderInt;
import org.jpedal.objects.layers.PdfLayerList;

/**
 *
 * @author Simon
 */
public interface GUILayersPanel {
    public void reinitialise(PdfLayerList layersObject, PdfDecoderInt decode_pdf,
                             Object scrollPane, int currentPage);
    
    public void rescanPdfLayers();
    
    public void resetLayers();
}
