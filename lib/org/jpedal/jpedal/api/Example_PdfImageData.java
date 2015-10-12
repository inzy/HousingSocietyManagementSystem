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
 * Example_PdfImageData.java
 * ---------------
 */
package org.jpedal.examples.api;

import org.jpedal.objects.PdfImageData;

public class Example_PdfImageData {
    
    final ApiParams params = new ApiParams();
    
    public void Example_PdfData(){
        
        ////////////////////////////////////////////////////////////////////////
        //constructors
        ////////////////////////////////////////////////////////////////////////
        
        /**
         * Create an instance of PdfImageData to hold metadata on extracted images.
         */
        final PdfImageData pdfImageData = new PdfImageData();
        
        ////////////////////////////////////////////////////////////////////////
        //public methods you can use
        ////////////////////////////////////////////////////////////////////////
        
        /**
         * Get the page ID of the image.
         */
        params.intValue = pdfImageData.getImagePageID(params.intValue);
        
        /**
         * Get the name of the image.
         */
        params.stringValue = pdfImageData.getImageName(params.intValue);
        
        /**
         * Get the X coordinates in pixel for an image.
         */
        params.floatValue = pdfImageData.getImageXCoord(params.intValue);
        
        /**
         * Get the Y coordinates in pixels for an image.
         */
        params.floatValue = pdfImageData.getImageYCoord(params.intValue);
        
        /**
         * Get the width in pixels for an image.
         */
        params.floatValue = pdfImageData.getImageWidth(params.intValue);
        
        /**
         * get height for image in pixels
         * 
         * Get the height in pixels for an image.
         */
        params.floatValue = pdfImageData.getImageHeight(params.intValue);
        
        /**
         * Get the current number of images.
         * Note that the first image is item 0.
         */
        params.intValue = pdfImageData.getImageCount();
        
        /**
         * Clear PdfImageData object of any image data and reset.
         */
        pdfImageData.clearImageData();
        
        ////////////////////////////////////////////////////////////////////////
        /*
         *
         * AVOID USING THESE METHODS AS THEY ARE NOT PART OF THE API!!
         *
         */
        ////////////////////////////////////////////////////////////////////////
        
        pdfImageData.setImageInfo(params.stringValue, params.intValue, params.floatValue, params.floatValue, params.floatValue, params.floatValue);
        
    }
    
}