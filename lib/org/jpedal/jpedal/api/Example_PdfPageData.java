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
 * Example_PdfPageData.java
 * ---------------
 */
package org.jpedal.examples.api;

import org.jpedal.objects.PdfPageData;

public class Example_PdfPageData {
    
    final ApiParams params = new ApiParams();
    
    public void Example_PdfPageData(){
        
        ////////////////////////////////////////////////////////////////////////
        //constructors
        ////////////////////////////////////////////////////////////////////////
        
        /** 
         * Create empty object to hold data relating to the PDF pages for extraction.
         */
        final PdfPageData pdfPageData = new PdfPageData();
        
        ////////////////////////////////////////////////////////////////////////
        //public methods you can use
        ////////////////////////////////////////////////////////////////////////
        
        /**
         * Get the values of the media box as a String.
         */
        params.stringValue = pdfPageData.getMediaValue(params.intValue);
        
        /**
         * Get the x coordinate of the media box.
         */
        params.intValue = pdfPageData.getMediaBoxX(params.intValue);
        
        /**
         * Get the rounded x coordinate value of the media box.
         */
        params.intValue = pdfPageData.getScaledMediaBoxX(params.intValue);
        
        /**
         * Get the y coordinate of the media box.
         */
        params.intValue = pdfPageData.getMediaBoxY(params.intValue);
        
        /**
         * Get the rounded y coordinate value of the media box.
         */
        params.intValue = pdfPageData.getScaledMediaBoxY(params.intValue);
        
        /**
         * Get the height of the media box.
         */
        params.intValue = pdfPageData.getMediaBoxHeight(params.intValue);
        
        /**
         * Get the rounded height value of the media box.
         */
        params.intValue = pdfPageData.getScaledMediaBoxHeight(params.intValue);
        
        /**
         * Get the width of the media box.
         */
        params.intValue = pdfPageData.getMediaBoxWidth(params.intValue);
        
        /**
         * Get the rounded x coordinate value of the media box.
         */
        params.intValue = pdfPageData.getScaledMediaBoxWidth(params.intValue);
        
        /**
         * Get the values of the crop box as a String.
         */
        params.stringValue = pdfPageData.getCropValue(params.intValue);
        
        /**
         * Get the rounded x coordinate value of the crop box.
         */
        params.intValue = pdfPageData.getCropBoxX(params.intValue);
        
        /**
         * Get the x coordinate of the crop box.
         */
        params.floatValue = pdfPageData.getCropBoxX2D(params.intValue);
        
        /**
         * Get the x coordinate of the crop box with the scaling applied.
         */
        params.intValue = pdfPageData.getScaledCropBoxX(params.intValue);
        
        /**
         * Get the rounded y coordinate value of the crop box.
         */
        params.intValue = pdfPageData.getCropBoxY(params.intValue);
        
        /**
         * Get the y coordinate of the crop box.
         */
        params.floatValue = pdfPageData.getCropBoxY2D(params.intValue);
        
        /**
         * Get the y coordinate of the crop box with the scaling applied.
         */
        params.intValue = pdfPageData.getScaledCropBoxY(params.intValue);
        
        /**
         * Get the rounded height value of the crop box.
         */
        params.intValue = pdfPageData.getCropBoxHeight(params.intValue);
        
        /**
         * Get the height of the crop box.
         */
        params.floatValue = pdfPageData.getCropBoxHeight2D(params.intValue);
        
        /**
         * Get the height of the crop box with the scaling applied.
         */
        params.intValue = pdfPageData.getScaledCropBoxHeight(params.intValue);
        
        /**
         * Get the rounded width value of the crop box.
         */
        params.intValue = pdfPageData.getCropBoxWidth(params.intValue);
        
        /**
         * Get the width of the crop box.
         */
        params.floatValue = pdfPageData.getCropBoxWidth2D(params.intValue);
        
        /**
         * Get the width of the crop box with the scaling applied.
         */
        params.intValue = pdfPageData.getScaledCropBoxWidth(params.intValue);
        
        /**
         * Get the scaling value that is currently being used.
         */
        params.floatValue = pdfPageData.getScalingValue();
        
        /**
         * Set the scaling value to apply to all values.
         */
        pdfPageData.setScalingValue(params.floatValue);
        
        /**
         * Get the page count.
         */
        params.intValue = pdfPageData.getPageCount();
        
        /** 
         * Get the rotation value.
         */
        params.intValue = pdfPageData.getRotation(params.intValue);
        
        /**
         * Set the page rotation.
         * Can accept negative values.
         * Acceptable values are 0, 90 , 180, 270.
         * Default value is 0.
         */
        pdfPageData.setPageRotation(params.intValue, params.intValue);
        
        /**
         * Check if the PDF document has varying page sizes and rotations.
         */
        params.booleanValue = pdfPageData.hasMultipleSizes();
        
        /**
         * Set to allow the page to start at different locations.
         * Bottom left is the default value.
         */
        pdfPageData.setOrigin(params.pageOriginsValue);
        
        /**
         * Check the current location that the page is set to start at.
         */
        params.pageOriginsValue = pdfPageData.getOrigin();
        
        ////////////////////////////////////////////////////////////////////////
        /*
         *
         * AVOID USING THESE METHODS AS THEY ARE NOT PART OF THE API!!
         *
         */
        ////////////////////////////////////////////////////////////////////////
        
        pdfPageData.setCropBox(params.floatArrayValue);
        
        pdfPageData.setMediaBox(params.floatArrayValue);
        
        pdfPageData.checkSizeSet(params.intValue);
        
    }
    
}