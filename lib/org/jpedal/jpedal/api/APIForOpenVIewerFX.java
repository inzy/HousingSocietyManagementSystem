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
 * APIForJPDF2HTML5.java
 * ---------------
 */

package org.jpedal.examples.api;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jpedal.examples.baseviewer.BaseViewerFX;
import org.jpedal.examples.viewer.Commands;

/**
 *
 * This is where we log all public methods in BaseViewer, Viewer and plugin
 * @author markee
 */
public class APIForOpenVIewerFX {
    
    /**
     * this contains links to all variables and methods which need to be public for FX plugin
     */
    static void plugin(){
        
        final int checkIntIsPublic=Commands.GETPDFNAME;
        
        BaseViewerFX baseViewerTest=new BaseViewerFX();
        
        baseViewerTest.setupViewer(0,0);
        baseViewerTest.addListeners();
        baseViewerTest.loadPDF(new java.io.File("test.pdf"));
        
    }
    
    static void viewerFX(){
        
        BaseViewerFX instance = new BaseViewerFX();
        
        instance.start(new Stage());
       
        Scene scene = instance.setupViewer(10, 10);
   
        instance.addListeners();
       
        instance.loadPDF(null);
       
        String result = instance.getPDFfilename();
      

    }

}
