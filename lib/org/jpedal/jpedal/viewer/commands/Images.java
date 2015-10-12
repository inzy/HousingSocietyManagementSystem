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
 * Images.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import java.awt.Container;
import javax.swing.JOptionPane;
import org.jpedal.*;
import org.jpedal.constants.PDFImageProcessing;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.gui.popups.SaveImage;
import org.jpedal.examples.viewer.utils.Exporter;
import org.jpedal.gui.GUIFactory;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

/**
 * Extract/Save Images
 */
public class Images {

    public static void execute(final Object[] args, final GUIFactory currentGUI, final Values commonValues, final PdfDecoderInt decode_pdf) {
        if (args == null) {
            if (commonValues.getSelectedFile() == null) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NoFile"));
            } else {
                // get values from user

                final SaveImage current_selection = new SaveImage(commonValues.getInputDir(), commonValues.getPageCount(), commonValues.getCurrentPage());
                final int userChoice = current_selection.display((Container)currentGUI.getFrame(), Messages.getMessage("PdfViewerTitle.SaveImagesFromPageRange"));

                // get parameters and call if YES
                if (commonValues.getFileIsURL()) {
                    currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.CannotExportFromURL"));
                } else if (userChoice == JOptionPane.OK_OPTION) {
                    final Exporter exporter = new Exporter(currentGUI, commonValues.getSelectedFile(), decode_pdf);
                    exporter.extractImagesOnPages(current_selection);
                }
            }
        } else {

        }
    }

    /**
     * used to display non-PDF files
     */
    public static void addImage(final PdfDecoderInt decode_pdf, final Values commonValues) {
        final GraphicsState gs = new GraphicsState();
        gs.CTM[0][0] = commonValues.getBufferedImg().getWidth();
        gs.CTM[1][1] = commonValues.getBufferedImg().getHeight();

        decode_pdf.getDynamicRenderer().drawImage(1, commonValues.getBufferedImg(), gs, false, "image", PDFImageProcessing.NOTHING, -1);

        //Set size for the given page as each page can be a different size
        if (commonValues.isMultiTiff()) {

            if (commonValues.getBufferedImg() != null) {
                decode_pdf.getPdfPageData().setMediaBox(new float[]{0, 0, commonValues.getBufferedImg().getWidth(), commonValues.getBufferedImg().getHeight()});
            }

            decode_pdf.getPdfPageData().checkSizeSet(1);
        }
    }

    /**
     * called by nav functions to decode next page
     */
    public static void decodeImage(final boolean resizePanel, final PdfDecoderInt decode_pdf, final GUIFactory currentGUI, final GUIThumbnailPanel thumbnails, final Values commonValues) {

        //remove any search highlight
        decode_pdf.getTextLines().clearHighlights();
        //decode_pdf.addHighlights(null, false);
        //decode_pdf.setMouseHighlightAreas(null); now a duplicate of above

        //stop user changing scaling while decode in progress
        currentGUI.resetComboBoxes(false);

        currentGUI.getButtons().setPageLayoutButtonsEnabled(false);

        /**
         * flush any previous pages
         */
        decode_pdf.getDynamicRenderer().flush();
        decode_pdf.getPages().refreshDisplay();

        /**
         * if running terminate first
         */
        thumbnails.terminateDrawing();

        Values.setProcessing(true);

        final org.jpedal.utils.SwingWorker worker = new org.jpedal.utils.SwingWorker() {
            @Override
            public Object construct() {

                try {

                    currentGUI.updateStatusMessage(Messages.getMessage("PdfViewerDecoding.page"));

                    if (commonValues.getBufferedImg() != null) {
                        addImage(decode_pdf, commonValues);
                    }

                    final PdfPageData page_data = decode_pdf.getPdfPageData();

                    if (commonValues.getBufferedImg() != null) {
                        page_data.setMediaBox(new float[]{0, 0, commonValues.getBufferedImg().getWidth(), commonValues.getBufferedImg().getHeight()});
                    }

                    page_data.checkSizeSet(1);
                    currentGUI.resetRotationBox();

                    /**
                     * make sure screen fits display nicely
                     */
                    if ((resizePanel) && (thumbnails.isShownOnscreen())) {
                        currentGUI.scaleAndRotate();
                    }

                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }

                    currentGUI.setPageNumber();

                    currentGUI.setViewerTitle(null); // restore title

                    currentGUI.getButtons().hideRedundentNavButtons(currentGUI);
                } catch (final Exception e) {

                    if(LogWriter.isOutput()) {
                        LogWriter.writeLog("Exception in handling title "+e);
                    }
                    currentGUI.setViewerTitle(null); //restore title

                }

                currentGUI.setStatusProgress(100);

                //reanable user changing scaling
                currentGUI.resetComboBoxes(true);

                //ensure drawn
                //<start-fx>
                ((PdfDecoder)decode_pdf).repaint();
                //<end-fx>

                Values.setOpeningTransferedFile(false);
                
                return null;
            }
        };

        worker.start();
    }
}
