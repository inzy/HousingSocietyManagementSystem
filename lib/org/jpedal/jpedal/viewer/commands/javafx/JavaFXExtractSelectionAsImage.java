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
 * JavaFXExtractSelectionAsImage.java
 * ---------------
 */

package org.jpedal.examples.viewer.commands.javafx;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.commands.generic.GUIExtractSelectionAsImage;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.examples.viewer.gui.javafx.dialog.FXDialog;
import org.jpedal.gui.GUIFactory;

/**
 * This class is a JavaFX specific class to hold the JavaFX code for
 * Extracting the drawn CursorBox as an Image.
 */
public class JavaFXExtractSelectionAsImage extends GUIExtractSelectionAsImage {
    public static void execute(final Values commonValues, final GUIFactory currentGUI, final PdfDecoderInt decode_pdf) {
        extractSelectedScreenAsImage(commonValues,currentGUI,decode_pdf); //Calls the generic code.

        
        final VBox pane = new VBox();
        final FXDialog dialog = new FXDialog((Stage)currentGUI.getFrame(), Modality.APPLICATION_MODAL, pane);
        dialog.setResizeable(false);
        //wrap image so we can display
        if( snapShot != null ){
            //IconiseImage icon_image = new IconiseImage( snapShot );
            final ImageView imv1 = new ImageView();
            imv1.setImage(SwingFXUtils.toFXImage(snapShot, null));
            dialog.setWidth(imv1.getImage().getWidth());
            dialog.setHeight(imv1.getImage().getHeight()+50);
            //add image to pane if there is one
            pane.getChildren().add(imv1);
        }else{
            return;
        }
        
        final HBox btnBox = new HBox();
        final Button saveBtn = new Button("Save");
        final Button cancelBtn = new Button("Cancel");
        btnBox.getChildren().addAll(saveBtn, cancelBtn);
        btnBox.setAlignment(Pos.BOTTOM_RIGHT);
        pane.getChildren().add(btnBox);
        
        dialog.show();
        
        if(GUI.debugFX){
            System.out.println("Save Dialog required for JavaFXExtractSelectionAsImage.java");
        }
    }
}
