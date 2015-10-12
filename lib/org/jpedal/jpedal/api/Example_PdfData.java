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
 * Example_PdfData.java
 * ---------------
 */
package org.jpedal.examples.api;

import org.jpedal.objects.PdfData;

public class Example_PdfData {
    
    final ApiParams params = new ApiParams();
    
    public void Example_PdfData(){
        
        ////////////////////////////////////////////////////////////////////////
        //constructors
        ////////////////////////////////////////////////////////////////////////
        
        /** 
         * Create empty object to hold data for extraction.
         */
        final PdfData pdfData = new PdfData();
        
        ////////////////////////////////////////////////////////////////////////
        //public methods you can use
        ////////////////////////////////////////////////////////////////////////
        
        /**
         * get number of raw objects on page.
         */
        params.intValue = pdfData.getRawTextElementCount();
        
        /**
         * Store line of raw text for later processing.
         */
        pdfData.addRawTextElement(params.floatValue, params.intValue, params.stringValue,
                params.floatValue, params.intValue, params.floatValue,
                params.floatValue, params.floatValue, params.floatValue,
                params.stringBufferValue, params.intValue,
                params.stringValue, params.booleanValue);
        
        /**
         * Set flag to show that the width has been embedded in the text.
         */
        pdfData.widthIsEmbedded();
        
        /**
         * Check if the width has been embedded in the text.
         */
        params.booleanValue = pdfData.IsEmbedded();
        
        /**
         * Set to extract color as XML as well (true) or not (false).
         */
        pdfData.enableTextColorDataExtraction();
        
        /**
         * Check if color is being extracted as XML as well.
         */
        params.booleanValue = pdfData.isColorExtracted(); 
        
        /**
         * Clear the store of objects.
         */
        pdfData.dispose();
        
        ////////////////////////////////////////////////////////////////////////
        /*
         *
         * AVOID USING THESE METHODS AS THEY ARE NOT PART OF THE API!!
         *
         */
        ////////////////////////////////////////////////////////////////////////
        
        pdfData.flushTextList();
        
    }
    
}