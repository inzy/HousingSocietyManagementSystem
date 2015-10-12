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
 * Delete.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.awt.Container;
import javax.swing.JOptionPane;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.generic.GUISearchWindow;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.gui.popups.DeletePDFPages;
import org.jpedal.examples.viewer.utils.ItextFunctions;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.gui.GUIFactory;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.Messages;

/**
 * Deleted selected pages from the Document, uses itext functions delete()
 * It then re-open the document excluding the deleted pages.
 */
@SuppressWarnings({"UnusedAssignment","PMD"})
public class Delete {

    public static void execute(final Object[] args, final Values commonValues, final GUISearchWindow searchFrame,
            final GUIFactory currentGUI, final PdfDecoderInt decode_pdf, final PropertiesFile properties,
            final GUIThumbnailPanel thumbnails) {
        if (args == null) {
            if (commonValues.getSelectedFile() == null) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
            } else {
                //get values from user
                final DeletePDFPages deletedPages = new DeletePDFPages(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
                final int deletedPagesChoice = deletedPages.display((Container)currentGUI.getFrame(), Messages.getMessage("PdfViewerDelete.text"));

                //get parameters and call if YES
                if (deletedPagesChoice == JOptionPane.OK_OPTION) {

                    final PdfPageData currentPageData = decode_pdf.getPdfPageData();

                    decode_pdf.closePdfFile();
                    final ItextFunctions itextFunctions = new ItextFunctions(currentGUI, commonValues.getSelectedFile(), decode_pdf);
                    ItextFunctions.delete(commonValues.getPageCount(), currentPageData, deletedPages);
                    OpenFile.open(commonValues.getSelectedFile(), commonValues, searchFrame, currentGUI, decode_pdf, properties, thumbnails);
                }
            }
        } else {

        }
    }
}
