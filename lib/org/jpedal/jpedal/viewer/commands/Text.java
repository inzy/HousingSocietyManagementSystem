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
 * Text.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.awt.Container;
import javax.swing.JOptionPane;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.popups.SaveText;
import org.jpedal.examples.viewer.utils.Exporter;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.Messages;

/**
 * Saves the text from a page rage / current selection, cannot export from a URL.
 */
public class Text {

    public static void execute(final Object[] args, final GUIFactory currentGUI, final Values commonValues, final PdfDecoderInt decode_pdf) {
        if (args == null) {
            if (commonValues.getSelectedFile() == null) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
            } else if (!decode_pdf.isExtractionAllowed()) {
                currentGUI.showMessageDialog("Not allowed");
            } else {
                // get values from user
                final SaveText current_selection = new SaveText(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
                final int userChoice = current_selection.display((Container)currentGUI.getFrame(), Messages.getMessage("PdfViewerTitle.SaveTextFromPageRange"));

                // get parameters and call if YES
                if (commonValues.getFileIsURL()) {
                    currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));
                } else if (userChoice == JOptionPane.OK_OPTION) {
                    final Exporter exporter = new Exporter(currentGUI, commonValues.getSelectedFile(), decode_pdf);
                    exporter.extractTextOnPages(current_selection);
                }
            }
        } else {

        }
    }
}
