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
 * AddHeaderFooter.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.awt.Container;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.generic.GUISearchWindow;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.gui.popups.AddHeaderFooterToPDFPages;
import org.jpedal.examples.viewer.utils.ItextFunctions;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.gui.GUIFactory;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.Messages;

import javax.swing.*;



/**
 * This class allows you to add a header and a footer (usually the page number)
 * to the top and bottom of each page in the opened document. It uses itext
 * function addHeaderFooter()
 */
@SuppressWarnings({"UnusedAssignment","PMD"})
public class AddHeaderFooter {

    public static void execute(final Object[] args, final Values commonValues, final GUISearchWindow searchFrame,
                               final GUIFactory currentGUI, final PdfDecoderInt decode_pdf, final PropertiesFile properties,
                               final GUIThumbnailPanel thumbnails) {
        if (args == null) {
            if (commonValues.getSelectedFile() == null) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
            } else {

                //get values from user
                final AddHeaderFooterToPDFPages addHeaderFooter = new AddHeaderFooterToPDFPages(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
                final int headerFooterPagesChoice = addHeaderFooter.display((Container)currentGUI.getFrame(), Messages.getMessage("PdfViewerTitle.AddHeaderAndFooters"));

                //get parameters and call if YES
                if (headerFooterPagesChoice == JOptionPane.OK_OPTION) {

                    final PdfPageData currentPageData = decode_pdf.getPdfPageData();

                    decode_pdf.closePdfFile();
                    final ItextFunctions itextFunctions = new ItextFunctions(currentGUI, commonValues.getSelectedFile(), decode_pdf);
                    ItextFunctions.addHeaderFooter(commonValues.getPageCount(), currentPageData, addHeaderFooter);
                    OpenFile.open(commonValues.getSelectedFile(), commonValues, searchFrame, currentGUI, decode_pdf, properties, thumbnails);
                }
            }
        } else {

        }
    }
}
