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
 * BitMap.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.awt.Container;
import javax.swing.JOptionPane;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.popups.SaveBitmap;
//<start-fx>
import org.jpedal.examples.viewer.utils.Exporter;
//<end-fx>
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.Messages;

/**
 * Save the PDF document as a BitMap
 */
public class BitMap {

    public static void execute(final Object[] args, final GUIFactory currentGUI, final Values commonValues, final PdfDecoderInt decode_pdf) {
        if (args == null) {
            if (commonValues.getSelectedFile() == null) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.OpenFile"));
            } else {
                // get values from user
                final SaveBitmap current_selection = new SaveBitmap(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
                final int userChoice = current_selection.display((Container)currentGUI.getFrame(), Messages.getMessage("PdfViewer.SaveAsBitmap"));

                // get parameters and call if YES
                if (commonValues.getFileIsURL()) {
                    currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));
                } else if (userChoice == JOptionPane.OK_OPTION) {
                    //<start-fx>
                    final Exporter exporter = new Exporter(currentGUI, commonValues.getSelectedFile(), decode_pdf);
                    exporter.extractPagesAsImages(current_selection);
                    //<end-fx>
                }
            }
        } else {

        }
    }
}
