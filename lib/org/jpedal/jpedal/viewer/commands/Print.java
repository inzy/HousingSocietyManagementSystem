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
 * Print.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import javax.print.PrintServiceLookup;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.utils.*;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.Messages;

/**
 * Print the current document
 */
public class Print {

    public static void execute(final Object[] args, final GUIFactory currentGUI, final Values commonValues,
            final PropertiesFile properties, final PrinterInt currentPrinter, final PdfDecoderInt decode_pdf) {
        if (args == null) {
            if (commonValues.getSelectedFile() != null) {
                //<start-fx>
                if (!Printer.isPrinting()) {

                    if (!commonValues.isPDF()) {
                        currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.ImagePrinting"));
                    } else {
                        String defaultPrinter = properties.getValue("defaultPrinter");
                        if ((defaultPrinter == null || defaultPrinter.isEmpty()) &&
                             (PrintServiceLookup.lookupDefaultPrintService() != null) ){
                                defaultPrinter = PrintServiceLookup.lookupDefaultPrintService().getName();
                        }

                        currentPrinter.printPDF(decode_pdf, currentGUI, properties.getValue("printerBlacklist"), defaultPrinter);
                    }
                } else  //<end-fx>
                {
                    currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintFinish.message"));
                }
            } else {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerNoFile.message"));
            }
        } else {

        }
    }
}
