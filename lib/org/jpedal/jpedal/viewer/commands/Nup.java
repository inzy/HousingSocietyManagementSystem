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
 * Nup.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.awt.Container;
import javax.swing.JOptionPane;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.popups.ExtractPDFPagesNup;
import org.jpedal.examples.viewer.utils.ItextFunctions;
import org.jpedal.gui.GUIFactory;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.Messages;

/**
 *
 */
@SuppressWarnings({"UnusedAssignment","PMD"})
public class Nup {

    public static void execute(final Object[] args, final Values commonValues, final GUIFactory currentGUI, final PdfDecoderInt decode_pdf) {
        if (args == null) {
            if (commonValues.getSelectedFile() == null) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
            } else {
                //					get values from user
                final ExtractPDFPagesNup nup = new ExtractPDFPagesNup(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
                final int nupChoice = nup.display((Container)currentGUI.getFrame(), Messages.getMessage("PdfViewerNUP.titlebar"));

                //get parameters and call if YES
                if (nupChoice == JOptionPane.OK_OPTION) {

                    final PdfPageData currentPageData = decode_pdf.getPdfPageData();

                    //decode_pdf.closePdfFile();
                    final ItextFunctions itextFunctions = new ItextFunctions(currentGUI, commonValues.getSelectedFile(), decode_pdf);
                    ItextFunctions.nup(commonValues.getPageCount(), currentPageData, nup);
                    //open(commonValues.getSelectedFile());
                }
            }
        } else {

        }
    }
}
