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
 * JavaFXInfo.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.gui.javafx.dialog.FXDialog;
import org.jpedal.utils.BrowserLauncher;
import org.jpedal.utils.Messages;

/**
 * This class displays a popup window which gives user information about JPedal.
 * It can be called by going to Help then About on the main menu-bar.
 */
public class JavaFXInfo {
    
    public static void execute(final Object[] args) {
        if (args == null) {
            getInfoBox();
        } else {

        }
    }
    
    private static void getInfoBox(){
        
        /**
         * Build Main Body Title.
         */
        final Text title = new Text ("Simple Viewer Information");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 14));
         
        /**
         * Build Main Body Text.
         */
        final Text info = new Text(Messages.getMessage("PdfViewerInfo1"));
        info.setWrappingWidth(350);
        info.setTextAlignment(TextAlignment.JUSTIFY);
        info.setText(info.getText()+"\n\nVersions:\n JPedal: " + PdfDecoderInt.version + "          " + "Java: " + System.getProperty("java.version"));
        info.setFont(Font.font("SansSerif", FontWeight.NORMAL,12));
        
        /**
         * Build Main Body Logo.
         */
        final ImageView imageView = new ImageView(new Image("/org/jpedal/examples/viewer/res/logo.gif"));
        
        /**
         * Build Main Body HyperLink.
         */
        final Hyperlink link = new Hyperlink("Take Me To JPedal Library");

        /**
         * Build Text Seperators.
         */
        final Separator sepBottom = new Separator();
        sepBottom.setPrefHeight(50);
        sepBottom.setOrientation(Orientation.HORIZONTAL);
        final Separator sepTop = new Separator();
        sepTop.setPrefHeight(50);
        sepTop.setOrientation(Orientation.HORIZONTAL);

        /**
         * Add Items to a VBox Container.
         */
        final VBox vBox = new VBox();
        vBox.getChildren().addAll(title,sepTop,info,sepBottom,imageView,link);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));
        
        final FXDialog newDialog = new FXDialog(null, Modality.APPLICATION_MODAL, vBox, 400,300);
        
        //Open default-browser window to support & docs page when link is clicked.
        link.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                try {
                    BrowserLauncher.openURL("http://www.idrsolutions.com/java-pdf-library/");
                    newDialog.close();
                } catch (final Exception ex) {
                   ex.printStackTrace();
                }
            }
        });
        
        newDialog.show();
    }
    
}
