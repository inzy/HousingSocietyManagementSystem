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
 * Handouts.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.io.File;
import javax.swing.JFileChooser;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.utils.ItextFunctions;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.Messages;

/**
 *
 */
@SuppressWarnings({"UnusedAssignment","PMD"})
public class Handouts {

    public static void execute(final Object[] args, final GUIFactory currentGUI, final Values commonValues, final PdfDecoderInt decode_pdf) {
        if (args == null) {
            if (commonValues.getFileIsURL()) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));
            }

            if (commonValues.getSelectedFile() == null) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
            } else {
                if (!commonValues.getFileIsURL()) {//ensure file choose not displayed if opened from URL
                    final JFileChooser chooser1 = new JFileChooser();
                    chooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);

                    final int approved1 = chooser1.showSaveDialog(null);
                    if (approved1 == JFileChooser.APPROVE_OPTION) {

                        final File file = chooser1.getSelectedFile();

                        final ItextFunctions itextFunctions = new ItextFunctions(currentGUI, commonValues.getSelectedFile(), decode_pdf);
                        ItextFunctions.handouts(file.getAbsolutePath());
                    }
                }
            }
        } else {

        }
    }
}
