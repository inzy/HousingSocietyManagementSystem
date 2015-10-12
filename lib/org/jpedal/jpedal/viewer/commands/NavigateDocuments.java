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
 * NavigateDocuments.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.generic.GUISearchWindow;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.Messages;

/**
 * This class Opens up a previously opened document, you can open either a
 * previous document or the next document in the Viewer.
 */
public class NavigateDocuments {
    
    public static void executePrevDoc(final Object[] args, final GUIFactory currentGUI, final Values commonValues,
            final GUISearchWindow searchFrame, final PdfDecoderInt decode_pdf,
            final PropertiesFile properties, final GUIThumbnailPanel thumbnails) {
        if (args == null) {
            //<start-fx>
            if (org.jpedal.examples.viewer.utils.Printer.isPrinting()) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintWait.message"));
            } else //<end-fx>
            if (Values.isProcessing()) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
            } else {
                final String fileToOpen = currentGUI.getRecentDocument().getPreviousDocument();
                currentGUI.openFile(fileToOpen);                   
            }
        }
    }

    public static void executeNextDoc(final Object[] args, final GUIFactory currentGUI, final Values commonValues,
            final GUISearchWindow searchFrame, final PdfDecoderInt decode_pdf,
            final PropertiesFile properties, final GUIThumbnailPanel thumbnails) {
        if (args == null) {

            //<start-fx>
            if (org.jpedal.examples.viewer.utils.Printer.isPrinting()) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintWait.message"));
            } else   //<end-fx>
            if (Values.isProcessing()) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
            } else {
                final String fileToOpen = currentGUI.getRecentDocument().getNextDocument();
                currentGUI.openFile(fileToOpen);                 
            }
        }
    }  
}
