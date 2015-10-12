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
 * JavaFXScrollListener.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.javafx;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollBar;
import javafx.util.Duration;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.gui.GUI;

/**
 *
 * @author Simon
 */
public class JavaFXScrollListener implements ChangeListener<Number> {
    private final Timeline trapMultipleMovements;
    private boolean requestMade;
    // Avoid StackOverflowExceptions
    private boolean setValueLocally;
    private final GUI gui;
    private int nextPage=-1;
    private boolean decodeLock;
    private final ScrollBar scroll;
    
    JavaFXScrollListener(final GUI gui, final ScrollBar callback) {
        requestMade = false;
        setValueLocally = false;
        this.gui = gui;
        scroll = callback;
        this.trapMultipleMovements = new Timeline(new KeyFrame(Duration.millis(250), new EventHandler<ActionEvent>() {
            @Override public void handle(final ActionEvent event) {
                if(!requestMade){
                    requestMade = true;
                    if(nextPage > 0){
                        decodeLock = true;
                        gotoPage(nextPage);
                        gui.getPdfDecoder().waitForDecodingToFinish();
                        decodeLock = false;
                        
                    }
                    requestMade = false;
                }
            }
        }));
    }
    
    private void gotoPage(final int page){
        gui.getCommand().executeCommand(Commands.GOTO, new Object[]{Integer.toString(page)});
    }
    
    @Override
    public void changed(final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue) {
        if(setValueLocally || newValue == null){
            setValueLocally = false;
            return;
        }
        
        final int newPage = newValue.intValue()+1;
        
        if(decodeLock){
            setValueLocally = true;
            scroll.setValue(oldValue.intValue());
            return;
        }
        
        if(newValue.intValue() >= 0 && nextPage != newPage){
            nextPage = newPage <= gui.getPdfDecoder().getPageCount() ? newPage : gui.getPdfDecoder().getPageCount();
            
            if(trapMultipleMovements.getStatus() == Animation.Status.RUNNING) {
                trapMultipleMovements.stop();
            }
            
            trapMultipleMovements.setCycleCount(1);
            trapMultipleMovements.playFromStart();
            
        }
    }
    
}
