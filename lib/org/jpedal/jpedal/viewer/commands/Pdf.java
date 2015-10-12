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
 * Pdf.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.awt.Container;
import javax.swing.JOptionPane;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.popups.SavePDF;
import org.jpedal.examples.viewer.utils.ItextFunctions;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.Messages;

/**
 *
 */
@SuppressWarnings({"UnusedAssignment","PMD"})
public class Pdf {

    public static void execute(final Object[] args, final Values commonValues, final GUIFactory currentGUI, final PdfDecoderInt decode_pdf) {
        if (args == null) {
            if (commonValues.getSelectedFile() == null) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
            } else {
                //get values from user
                final SavePDF current_selection = new SavePDF(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
                final int userChoice = current_selection.display((Container)currentGUI.getFrame(), Messages.getMessage("PdfViewerTitle.SavePagesAsPdf"));

                //get parameters and call if YES
                if (userChoice == JOptionPane.OK_OPTION) {
                    final ItextFunctions itextFunctions = new ItextFunctions(currentGUI, commonValues.getSelectedFile(), decode_pdf);
                    ItextFunctions.extractPagesToNewPDF(current_selection);
                }
            }
        } else {

        }
    }
}
