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
 * FrameCloser.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.utils.*;
import org.jpedal.gui.GUIFactory;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

/**cleanly shutdown if user closes window*/
public class FrameCloser extends WindowAdapter {

    private final Commands currentCommands;
    private final GUIFactory currentGUI;
    private final PdfDecoderInt decode_pdf;
    private final GUIThumbnailPanel thumbnails;
    private final Values commonValues;
    private final PropertiesFile properties;

    public FrameCloser(final Commands currentCommands, final GUIFactory currentGUI, final PdfDecoderInt decode_pdf, final GUIThumbnailPanel thumbnails, final Values commonValues, final PropertiesFile properties) {
        this.currentCommands=currentCommands;
        this.currentGUI=currentGUI;
        this.decode_pdf=decode_pdf;
        this.thumbnails=thumbnails;
        this.commonValues=commonValues;
        this.properties = properties;
    }

    @Override //Refactor? Nathan ~ Can we cast to GUI and move deletePropertiesOnExit up?
    public void windowClosing(final WindowEvent e) {

            try {
                properties.setValue("lastDocumentPage", String.valueOf(commonValues.getCurrentPage()));
                //properties.writeDoc();
            } catch (final Exception e1) {
                if (LogWriter.isOutput()) {
                    LogWriter.writeLog("Attempting to set propeties values " + e1);
                }
            }

        //<start-fx>
        if(Printer.isPrinting()) {
            currentGUI.showMessageDialog(Messages.getMessage("PdfViewerBusyPrinting.message"));
        }
        //<end-fx>

        if(!Values.isProcessing()){

            //tell our code to exit cleanly asap
            thumbnails.terminateDrawing();

            decode_pdf.closePdfFile();

            currentCommands.executeCommand(Commands.EXIT, null);

        }else{

            currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
        }
    }
}
