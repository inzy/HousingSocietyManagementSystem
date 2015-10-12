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
 * JavaFXHelp.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands.javafx;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import org.jpedal.examples.viewer.gui.javafx.dialog.FXDialog;
import org.jpedal.utils.BrowserLauncher;

/**
 * This class will load a Pop-Up which points the user to our Support and
 * Documentation section on our website.
 */
public class JavaFXHelp {

    public static void execute(final Object[] args) {
        if (args == null) {
            getHelpBox();
        }

    }

    /**
     * Shows a popup window which displays information for support.
     *
     * @param currentGUI
     */
    private static void getHelpBox() {

        final Text info = new Text("Please click the link below for lots of tutorials and documentation");
        info.setTextAlignment(TextAlignment.CENTER);
        info.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        final Hyperlink link = new Hyperlink("Take Me To Support");

        //Seperates text from hyperlink button
        final Separator sep = new Separator();
        sep.setPrefHeight(50);
        sep.setOrientation(Orientation.HORIZONTAL);

        final VBox vBox = new VBox();
        vBox.getChildren().addAll(info, sep, link); //add items to vBox container
        vBox.setAlignment(Pos.CENTER);

        final FXDialog newDialog = new FXDialog(null, Modality.APPLICATION_MODAL, vBox, 400, 200);
        
        //Open default-browser window to support & docs page when link is clicked.
        link.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                try {
                    BrowserLauncher.openURL("http://idrsolutions.com/java-pdf-library-support/");
                    newDialog.close();
                } catch (final Exception ex) {
                    Logger.getLogger(JavaFXRSSyndication.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        newDialog.show();

    }

}
