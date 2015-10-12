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
 * JavaFXExit.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands.javafx;

import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.commands.SaveForm;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;

import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.gui.GUIFactory;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

/**
 * Clean up and exit program
 */
public class JavaFXExit {

    public static void execute(final Object[] args, final GUIThumbnailPanel thumbnails, final GUIFactory currentGUI, final Values commonValues, final PdfDecoderInt decode_pdf, final PropertiesFile properties) {
        if (args == null) {
            //<start-fx>
            if (org.jpedal.examples.viewer.utils.Printer.isPrinting()) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerStillPrinting.text"));
            } else
                //<end-fx>
            {
                exit(thumbnails, currentGUI, commonValues, decode_pdf, properties);
            }

        } else {

        }
    }

    public static void exit(final GUIThumbnailPanel thumbnails, final GUIFactory currentGUI, final Values commonValues, final PdfDecoderInt decode_pdf, final PropertiesFile properties) {

        thumbnails.terminateDrawing();

        /**
         * warn user on forms
         */
        SaveForm.handleUnsaveForms(currentGUI, commonValues, decode_pdf);

        /**
         * cleanup
         */
        decode_pdf.closePdfFile();

        //needed to save recent files
        try {
            properties.setValue("lastDocumentPage", String.valueOf(commonValues.getCurrentPage()));
            if (properties.getValue("trackViewerSize").toLowerCase().equals("true")) {
                properties.setValue("startViewerWidth", String.valueOf(((Stage)currentGUI.getFrame()).getWidth()));
                properties.setValue("startViewerHeight", String.valueOf(((Stage)currentGUI.getFrame()).getHeight()));
            }

            if (properties.getValue("trackScaling").toLowerCase().equals("true")) {
                properties.setValue("startScaling", String.valueOf(((GUI) currentGUI).getSelectedComboItem(Commands.SCALING)));
            }

            if (properties.getValue("trackView").toLowerCase().equals("true")) {
                properties.setValue("startView", String.valueOf(decode_pdf.getDisplayView()));
            }

            if (properties.getValue("startSideTabOpen").toLowerCase().equals("true")) {
                properties.setValue("startSideTabOpen", "true");
            }

            if (properties.getValue("trackSelectedSideTab").toLowerCase().equals("true")) {
                final TabPane tabs = (TabPane)currentGUI.getSideTabBar();
                if (DecoderOptions.isRunningOnMac) {
                    properties.setValue("startSelectedSideTab", tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getText());
                } else {
                    properties.setValue("startSelectedSideTab", tabs.getTabs().get(tabs.getSelectionModel().getSelectedIndex()).getText());
                }
            }

            if (properties.getValue("trackSideTabExpandedSize").toLowerCase().equals("true")) {
                properties.setValue("sideTabBarExpandLength", String.valueOf(currentGUI.getSplitDividerLocation()));
            }

            properties.writeDoc();
        } catch (final Exception e1) {
            if(LogWriter.isOutput()) {
                LogWriter.writeLog("Exception attempting to Write proterties: " + e1);
            } 
        }

        //formClickTest needs this so that it does not exit after first test.
             if(!Viewer.exitOnClose){/**/
            currentGUI.dispose();
          
        } else {
            
            //Added this one for now to remove a delay being experienced.
            if(!Viewer.exitOnClose){
                decode_pdf.dispose();
                currentGUI.dispose();

                System.exit(0);
            }else{
                ((Stage)currentGUI.getFrame()).close();
                
                decode_pdf.dispose();
                currentGUI.dispose();

            }
        }
    }
}
