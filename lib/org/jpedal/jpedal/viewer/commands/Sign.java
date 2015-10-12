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
 * Sign.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.awt.Container;
import java.awt.Frame;
import javax.swing.JOptionPane;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.popups.SignWizardModel;
import org.jpedal.examples.viewer.gui.popups.Wizard;
import org.jpedal.examples.viewer.objects.SignData;
import org.jpedal.examples.viewer.utils.ItextFunctions;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.Messages;

/**
 *
 */
@SuppressWarnings({"UnusedAssignment","PMD"})
public class Sign {

    public static void execute(final Object[] args, final GUIFactory currentGUI, final Values commonValues, final PdfDecoderInt decode_pdf) {

        if (args == null) {
            if (commonValues.getFileIsURL()) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));
            } else if (commonValues.getSelectedFile() == null) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
            } else if (!decode_pdf.isExtractionAllowed()) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.ExtractionNotAllowed"));
            } else {
                final SignData signData = new SignData();
                final SignWizardModel signer = new SignWizardModel(signData, commonValues.getSelectedFile(), commonValues.getInputDir());
                final Wizard signWizard = new Wizard((Frame) currentGUI.getFrame(), signer);

                if (signWizard.showModalDialog() != JOptionPane.OK_OPTION) {
                    return;
                }

                if (!signData.validate()) {
                    currentGUI.showMessageDialog(signData.toString());
                    return;
                }

                final int response = JOptionPane.showConfirmDialog((Container)currentGUI.getFrame(),
                        signData.toString(),
                        Messages.getMessage("PdfViewerGeneral.IsThisCorrect"),
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.OK_OPTION) {
                    final ItextFunctions itextFunctions = new ItextFunctions(currentGUI, commonValues.getSelectedFile(), decode_pdf);
                    ItextFunctions.Sign(signData);
                } else {
                    JOptionPane.showMessageDialog((Container)currentGUI.getFrame(),
                            Messages.getMessage("PdfViewerMessage.SigningOperationCancelled"),
                            Messages.getMessage("PdfViewerGeneral.Warning"),
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

}
