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
 * FXProgressBarWithText.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.javafx;

import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Convenience class which merges a progress bar and a text label to emulate a JProgressBar
 * e.g:
 * +------------+
 * |||| 10%     |
 * +------------+
 *    ^ progress value
 * 
 * @author Simon
 */
public class FXProgressBarWithText extends StackPane{
    private final Text message;
    private final ProgressBar progress;

    public FXProgressBarWithText() {
        message = new Text();
        progress = new ProgressBar();
        
        progress.prefWidthProperty().bind(this.widthProperty());
        progress.prefHeightProperty().bind(this.heightProperty());
        
        message.setFont(Font.font(null, FontWeight.SEMI_BOLD, 14));
        
        this.getChildren().addAll(progress, message);

        StackPane.setAlignment(message, Pos.CENTER);
        StackPane.setAlignment(progress, Pos.CENTER);
        
    }
    
    /**
     * Convenience method to set the progress bar value
     * @param val new value (between 0.0 and 1.0)
     */
    public void setProgress(final double val){
        progress.setProgress(val);
    }
    
    /**
     * Convenience method to set the text
     * @param text new String
     */
    public void setText(final String text){
        message.setText(text);
    }
    
    public Text getMessage() {
        return message;
    }

    public ProgressBar getProgress() {
        return progress;
    }
}
