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
 * FXInputDialog.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.javafx.dialog;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Dialog window which prompts the user for input.
 * 
 * @author Simon
 */
public class FXInputDialog extends FXMessageDialog {
    private final StringProperty input;
    
    public FXInputDialog(final Stage parent, final String message) {
        super(parent, Modality.APPLICATION_MODAL, message);
        
        input = new SimpleStringProperty();
        final TextField textField = new TextField();
        final BorderPane contentPane = getBorderPane();
        final Button cancelButton = new Button("Cancel");
        
        input.bind(textField.textProperty());
        textField.textProperty().set("");
        
        contentPane.getCenter().boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
            @Override public void changed(final ObservableValue<? extends Bounds> ov, final Bounds oldBounds, final Bounds newBounds) {
                textField.setPrefWidth(newBounds.getWidth());
            }
        });
        
        cancelButton.setPrefWidth(BUTTONWIDTH);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(final ActionEvent t) {
                cancel();
            }
        });
        
        getCenterGroup().getChildren().add(textField);
        getButtonGroup().getChildren().add(cancelButton);
        
    }
    
    /**
     * Use to get the value from the input box. Returns null if the user cancels the dialog.
     * @return 
     */
    public String showInputDialog(){
        showAndWait();
        
        if(isCancelled) {
            return null;
        } else {
            return input.get();
        }
    }
    
}
