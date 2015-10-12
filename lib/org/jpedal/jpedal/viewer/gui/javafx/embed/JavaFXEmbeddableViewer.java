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
 * JavaFXEmbeddableViewer.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.javafx.embed;

import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.jpedal.examples.viewer.ViewerFX;

/**
 * A wrapper class for ViewerFX which allows the Viewer to be 
 * recognised as an embeddable element in JavaFX Scene Builder (2.0)
 * 
 * @author Simon
 */
public class JavaFXEmbeddableViewer extends Pane {
    
    private ViewerFX viewer;
    
    JavaFXEmbeddableViewer(){
        viewer = null;
        
        final Pane thisPane = this;
        
        // Running later as ViewerFX was creating some issue (Class load related, I believe)
        // Which caused this class to not be picked up by SceneBuilder
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try{
                    viewer = new ViewerFX(thisPane, null);
                    viewer.setupViewer();
                    
                    final BorderPane root = viewer.getRoot();
                    
                    // Bind the viewer to match the dimensions on this class
                    root.prefWidthProperty().bind(thisPane.prefWidthProperty());
                    root.prefHeightProperty().bind(thisPane.prefHeightProperty());
                }catch (final Exception e){
                    System.err.println(e.getMessage());
                }
            }
        });
        
    }
    
    public ViewerFX getViewerFX(){
        if(viewer==null){
            System.err.println("ViewerFX has not been initialised yet - Please run in Platform.runLater()");
        }
        return viewer;
    }
}
