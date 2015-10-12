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
 * JavaFXOpenFile.java
 * ---------------
 */

package org.jpedal.examples.viewer.commands.javafx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.jpedal.*;
import org.jpedal.display.Display;
import org.jpedal.examples.viewer.Commands;
import static org.jpedal.examples.viewer.Commands.hires;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.commands.*;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.examples.viewer.gui.generic.GUISearchWindow;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.gui.popups.DownloadProgress;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.exception.PdfException;
import org.jpedal.gui.GUIFactory;
import org.jpedal.io.JAIHelper;
import org.jpedal.io.JAITiffHelper;
import org.jpedal.linear.LinearThread;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.objects.acroforms.actions.ActionHandler;
import org.jpedal.objects.raw.OutlineObject;
import org.jpedal.objects.raw.PdfDictionary;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

/**
 * This class Opens up a new file on page one whilst still using the users
 * chosen settings, it first handles unsaved forms and resets the viewable area,
 * it decodes the PDF and finally opens the new document.
 */
public class JavaFXOpenFile {

    private static File file;
    
    private static InputStream inputStream;
    private static boolean isPDf;
    
    
    public static void executeOpenFile(final Object[] args, final GUIFactory currentGUI, final GUISearchWindow searchFrame, final PropertiesFile properties, final GUIThumbnailPanel thumbnails, final PdfDecoderInt decode_pdf, final Values commonValues) {
        
        //reset value to null
        inputStream = null;

        if (args == null) {

            /**
             * warn user on forms
             */
            SaveForm.handleUnsaveForms(currentGUI, commonValues, decode_pdf);

            //<start-fx>
            if (org.jpedal.examples.viewer.utils.Printer.isPrinting()) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintWait.message"));
            } else //<end-fx>

            if (Values.isProcessing()) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerDecodeWait.message"));
            } else {

                selectFile(commonValues, searchFrame, currentGUI, decode_pdf, properties, thumbnails);

                commonValues.setFileIsURL(false);
            }
        }else {
            if (args.length == 2 && args[0] instanceof byte[] && args[1] instanceof String) {

                final byte[] data = (byte[]) args[0];
                final String filename = (String) args[1];

                commonValues.setFileSize(data.length);

                commonValues.setSelectedFile(filename);
                currentGUI.setViewerTitle(null);

                if ((commonValues.getSelectedFile() != null) && !Values.isProcessing()) {
                    // reset the viewableArea before opening a new file
                    decode_pdf.resetViewableArea();
                    /**/

                    try {
                        isPDf = true;
                        commonValues.setMultiTiff(false);

                        // get any user set dpi
                        final String hiresFlag = System.getProperty("org.jpedal.hires");
                        if (Commands.hires || hiresFlag != null) {
                            commonValues.setUseHiresImage(true);
                        }
                        // get any user set dpi
                        final String memFlag = System.getProperty("org.jpedal.memory");
                        if (memFlag != null) {
                            commonValues.setUseHiresImage(false);
                        }

                        // reset flag
                        thumbnails.resetToDefault();

                        // flush forms list
                        currentGUI.setNoPagesDecoded();

                        // remove search frame if visible
                        if (searchFrame != null) {
                            searchFrame.removeSearchWindow(false);
                        }

                        commonValues.maxViewY = 0;// rensure reset for any  viewport

                        currentGUI.setQualityBoxVisible(isPDf);

                        commonValues.setCurrentPage(1);

                        if (currentGUI.isSingle()) {
                            decode_pdf.closePdfFile();
                        }

                        decode_pdf.openPdfArray(data);

                        currentGUI.updateStatusMessage("opening file");

                        boolean fileCanBeOpened = true;

                        if ((decode_pdf.isEncrypted()) && (!decode_pdf.isFileViewable())) {
                            fileCanBeOpened = false;

                            String password = System.getProperty("org.jpedal.password");

                            if (password == null) {
                                password = currentGUI.showInputDialog(Messages.getMessage("PdfViewerPassword.message")); //$NON-NLS-1$
                            }
                            /**
                             * try and reopen with new password
                             */
                            if (password != null) {
                                decode_pdf.setEncryptionPassword(password);
                                // decode_pdf.verifyAccess();

                                if (decode_pdf.isFileViewable()) {
                                    fileCanBeOpened = true;
                                }

                            }

                            if (!fileCanBeOpened) {
                                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPasswordRequired.message"));
                            }

                        }
                        if (fileCanBeOpened) {

                            if (properties.getValue("Recentdocuments").equals("true")) {
                                properties.addRecentDocument(commonValues.getSelectedFile());
                                currentGUI.getRecentDocument().updateRecentDocuments(properties.getRecentDocuments());
                            }

                            currentGUI.getRecentDocument().addToFileList(commonValues.getSelectedFile());

                            /**
                             * reset values
                             */
                            commonValues.setCurrentPage(1);
                        }

                        processPage(commonValues, decode_pdf, currentGUI, thumbnails);
                    } catch (final PdfException e) {
                       
                         if(LogWriter.isOutput()) { 
                             LogWriter.writeLog("Exception attempting to open file: " + e);
                         }
                    }
                }

            } else if (args.length >= 1) {

                if (args[0] instanceof InputStream) {

                    inputStream = (InputStream) args[0];

//                    currentGUI.resetNavBar();
                    final String newFile = "InputStream-" + System.currentTimeMillis() + ".pdf";

                    commonValues.setSelectedFile(newFile);
                    commonValues.setFileIsURL(true);

                    /**
                     * decode pdf
                     */
                    if (inputStream != null) {
                        try {
                            commonValues.setFileSize(0);
                            currentGUI.setViewerTitle(null);
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }

                        /**
                         * open the file
                         */
                        if (!Values.isProcessing()) {

                            /**
                             * if running terminate first
                             */
                            thumbnails.terminateDrawing();

                            decode_pdf.flushObjectValues(true);

                            // reset the viewableArea before opening a new file
                            decode_pdf.resetViewableArea();

                            currentGUI.stopThumbnails();

                            //if (!currentGUI.isSingle())
                            //  openNewMultiplePage(commonValues.getSelectedFile());
                            try {
                                //Set to true to show our default download window
                                openFile(commonValues.getSelectedFile(), commonValues, searchFrame, currentGUI, decode_pdf, properties, thumbnails);
                                while (Values.isProcessing()) {
                                    Thread.sleep(1000);

                                }
                            } catch (final InterruptedException e) {
                                
                                if(LogWriter.isOutput()) {
                                    LogWriter.writeLog("Exception attempting to open file: " + e);
                                }
                            }
                        }

                    }

                } else {
                    File file;
                    if (args[0] instanceof File) {
                        file = (File) args[0];
                    } else if (args[0] instanceof String) {
                        final String filename = (String) args[0];
                        final char[] str = filename.toCharArray();
                        if (str[1] == ':' || str[0] == '\\' || str[0] == '/')//root
                        {
                            file = new File(filename);
                        } else {
                            final String parent = new File(commonValues.getSelectedFile()).getParent();
                            file = new File(parent, filename);
                            try {
                                file = file.getCanonicalFile();
                            } catch (final Exception e) {

                                if(LogWriter.isOutput()) {
                                    LogWriter.writeLog("Exception in opening file "+e);
                                }
                                file = new File(parent, filename);
                            }
                        }
                    } else {
                        file = null;
                    }

                    /**
                     * decode
                     */
                    if (file != null) {
                        /**
                         * save path so we reopen her for later selections
                         */
                        try {
                            commonValues.setInputDir(file.getParentFile().getCanonicalPath());
                            
                            open(file.getAbsolutePath(), commonValues, searchFrame, currentGUI, decode_pdf, properties, thumbnails);

                            /**
                             * see if second value as Named Dest and store
                             * object ref if set
                             */
                            String bookmarkPage = null;
                            if (args.length > 1 && args[1] instanceof String) { //it may be a named destination ( ie bookmark=Test1)

                                final String bookmark = (String) args[1];
                                bookmarkPage = decode_pdf.getIO().convertNameToRef(bookmark);

                            }

                            if (bookmarkPage != null) { //and goto named Dest if present

                                //read the object
                                final PdfObject namedDest = new OutlineObject(bookmarkPage);
                                decode_pdf.getIO().readObject(namedDest);

                                //and generic open Dest code
                                decode_pdf.getFormRenderer().getActionHandler().gotoDest(namedDest, ActionHandler.MOUSECLICKED, PdfDictionary.Dest);
                            }

                            while (Values.isProcessing()) {
                                // Do nothing until pdf loaded
                                try {
                                    Thread.sleep(100);
                                } catch (final InterruptedException e) {
                                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                    
                                    
                                }
                            }
                        } catch (final IOException e1) {
                            
                            if(LogWriter.isOutput()) {
                                LogWriter.writeLog("Exception attempting to open file: " + e1);
                            }
                            
                        }
                    } else { // no file selected so redisplay old
                        currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.NoSelection"));
                    }
                }
            }
        }
    }
    
    /**
     * checks file can be opened (permission)
     *
     * @throws PdfException
     */
    public static void openFile(final String selectedFile, final Values commonValues, final GUISearchWindow searchFrame,
            final GUIFactory currentGUI, final PdfDecoderInt decode_pdf, final PropertiesFile properties,
            final GUIThumbnailPanel thumbnails) {

        isPDf = false;
        commonValues.setMultiTiff(false);

        //get any user set dpi
        final String hiresFlag = System.getProperty("org.jpedal.hires");
        if (Commands.hires || hiresFlag != null) {
            commonValues.setUseHiresImage(true);
        }

        //get any user set dpi
        final String memFlag = System.getProperty("org.jpedal.memory");
        if (memFlag != null) {
            commonValues.setUseHiresImage(false);
        }

        //reset flag
        thumbnails.resetToDefault();

        //flush forms list
        currentGUI.setNoPagesDecoded();

        //remove search frame if visible
        if (searchFrame != null) {
            searchFrame.removeSearchWindow(false);
        }

        commonValues.maxViewY = 0;// rensure reset for any viewport
        final String ending = selectedFile.toLowerCase().trim();
        commonValues.setPDF(ending.endsWith(".pdf") || ending.endsWith(".fdf"));
        if(ending.endsWith(".pdf") || ending.endsWith(".fdf")){
            isPDf = true;
        }

        //switch off continous mode for images
        if (!commonValues.isPDF()) {

            if (Platform.isFxApplicationThread()) {

                decode_pdf.setDisplayView(Display.SINGLE_PAGE, Display.DISPLAY_CENTERED);

            } else {
                //<start-fx>
                final Runnable doPaintComponent = new Runnable() {

                    @Override
                    public void run() {
                        decode_pdf.setDisplayView(Display.SINGLE_PAGE, Display.DISPLAY_CENTERED);
                    }
                };
                Platform.runLater(doPaintComponent);
                //<end-fx>
            }

        }
        
        currentGUI.setQualityBoxVisible(commonValues.isPDF());

        commonValues.setCurrentPage(1);
        
        try {
            final boolean fileCanBeOpened = openUpFile(commonValues.getSelectedFile(), commonValues, searchFrame, currentGUI, decode_pdf, properties, thumbnails);

            if (fileCanBeOpened) {
                processPage(commonValues, decode_pdf, currentGUI, thumbnails);
            } else {
                currentGUI.setViewerTitle(Messages.getMessage("PdfViewer.NoFile"));
                decode_pdf.getDynamicRenderer().flush();
                decode_pdf.getPages().refreshDisplay();
                currentGUI.scaleAndRotate();
                commonValues.setPageCount(1);
                commonValues.setCurrentPage(1);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            System.err.println(Messages.getMessage("PdfViewerError.Exception") + ' ' + e + ' ' + Messages.getMessage("PdfViewerError.DecodeFile"));

        }
        //}

        //});
        //t.start();
        //commonValues.setProcessing(false);
    }
    
    
    public static void open(final String file, final Values commonValues, final GUISearchWindow searchFrame,
            final GUIFactory currentGUI, PdfDecoderInt decode_pdf, final PropertiesFile properties,
            final GUIThumbnailPanel thumbnails) {

//        currentGUI.resetNavBar();

        final boolean isURL = file.startsWith("http:") || file.startsWith("file:");
        try {

            if (!isURL) {
                commonValues.setFileIsURL(false);
                commonValues.setFileSize(new File(file).length() >> 10);
            } else {
                commonValues.setFileIsURL(true);
            }

            commonValues.setSelectedFile(file);
            currentGUI.setViewerTitle(null);

        } catch (final Exception e) {
            LogWriter.writeLog("Exception " + e + " getting paths");
        }

        /**
         * check file exists
         */
        final File testFile = new File(commonValues.getSelectedFile());
        if (!isURL && !testFile.exists()) {
            currentGUI.showMessageDialog(Messages.getMessage("PdfViewerFile.text") + commonValues.getSelectedFile() + Messages.getMessage("PdfViewerNotExist"));

            /**
             * open the file
             */
        } else if (commonValues.getSelectedFile() != null && !Values.isProcessing()) {

            if (currentGUI.isSingle()) {
                decode_pdf.flushObjectValues(true);
            } else {
                //<start-fx>
                System.out.println("openNewMultiplePage() is not yet implented for JavaFX in JavaFXOpenFile.java");
                decode_pdf = openNewMultiplePage(commonValues.getSelectedFile(), currentGUI, commonValues);
                //<end-fx>
            }

            //reset the viewableArea before opening a new file
            //decode_pdf.resetViewableArea();
            /**/

            openFile(commonValues.getSelectedFile(), commonValues, searchFrame, currentGUI, decode_pdf, properties, thumbnails);

        }
    }

    //<start-fx>
    /**
     * this is the code which does all the work
     *
     * @return
     */
    public static PdfDecoderInt openNewMultiplePage(final String fileName, final GUIFactory currentGUI,final Values commonValues) {

        System.out.println("Opening New Multiple Page in JavaFXOpenFile needs JavaFX Implementation");
        
        //JDesktopPane desktopPane = currentGUI.getMultiViewerFrames();

        /**
         * setup PDF object
         */
        //		System.out.println("new pdf = "+localPdf.getClass().getName() + "@" + Integer.toHexString(localPdf.hashCode()));

        PdfDecoderInt decode_pdF = new PdfDecoder(true);
        final PdfDecoderFX decode_pdf = ((PdfDecoderFX)decode_pdF);

        ((GUI)currentGUI).setPdfDecoder(decode_pdf);

        if (Platform.isFxApplicationThread()) {

            decode_pdf.setDisplayView(Display.SINGLE_PAGE, Display.DISPLAY_CENTERED);

        } else {
                final Runnable doPaintComponent = new Runnable() {

                    @Override
                    public void run() {
                        decode_pdf.setDisplayView(Display.SINGLE_PAGE, Display.DISPLAY_CENTERED);
                    }
                };
                Platform.runLater(doPaintComponent);
        }
        

        //decode_pdf.addExternalHandler(this, Options.MultiPageUpdate);
        PdfDecoderFX.init(true);
        decode_pdf.setExtractionMode(0, 1); //values extraction mode,dpi of images, dpi of page as a factor of 72

        final int inset = GUI.getPDFDisplayInset();
        decode_pdf.setInset(inset, inset);

        decode_pdf.setPageParameters(currentGUI.getScaling(), commonValues.getCurrentPage(), currentGUI.getRotation());

        /**
         * setup Internal frame to hold PDF
         */
//        JInternalFrame pdf = new JInternalFrame(fileName, true, true, true, true);
//        String s = String.valueOf(startX);
//        pdf.setName(s);
//        pdf.setSize(250, 250);
//        pdf.setVisible(true);
//        pdf.setLocation(startX, startY);
//
//        startX = startX + 25;
//        startY = startY + 25;
//
//        /**
//         * listener to switch to this object if window selected
//         */
//        multiViewListener = new MultiViewListener(decode_pdf, currentGUI, commonValues, currentGUI.getCommand());
//        pdf.addInternalFrameListener(multiViewListener);
//
//        pdf.addComponentListener(new ComponentListener() {
//            @Override
//            public void componentHidden(ComponentEvent e) {
//            }
//
//            Prevent window from becoming unselectable
//            @Override
//            public void componentMoved(ComponentEvent e) {
//                Component c = e.getComponent();
//                Component parent = c.getParent();
//
//                if (c.getLocation().y < 0) {
//                    Prevent internal window from leaving off the top of the panel
//                    c.setLocation(c.getLocation().x, 0);
//
//                } else if ((c.getLocation().y + c.getSize().height) > (parent.getSize().height + (c.getSize().height / 2))) {
//                    Don't go too far off the bottom of the screen (half internal window size)
//                    c.setLocation(c.getLocation().x, (parent.getSize().height - (c.getSize().height / 2)));
//                }
//
//                if (c.getLocation().x < -(c.getSize().width / 2)) {
//                    Don't go too far off the left of the screen (half internal window size)
//                    c.setLocation(-(c.getSize().width / 2), c.getLocation().y);
//
//                } else if ((c.getLocation().x + c.getSize().width) > (parent.getSize().width + (c.getSize().width / 2))) {
//                    Don't go too far off the right of the screen (half internal window size)
//                    c.setLocation((parent.getSize().width - (c.getSize().width / 2)), c.getLocation().y);
//                }
//            }
//
//            @Override
//            public void componentResized(ComponentEvent e) {
//                if (decode_pdf.getParent() != null && ((GUI)currentGUI).getSelectedComboIndex(Commands.SCALING) < 3) {
//                    currentGUI.zoom();
//                }
//            }
//
//            @Override
//            public void componentShown(ComponentEvent e) {
//            }
//        });
//
//        /**
//         * add the pdf display to show page
//         *
//         */
//        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.getViewport().add(localPdf);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.getVerticalScrollBar().setUnitIncrement(80);
//        scrollPane.getHorizontalScrollBar().setUnitIncrement(80);
//
//        pdf.getContentPane().add(scrollPane);
//
//        desktopPane.add(fileName, pdf);
//
//        /**
//         * ensure at front and active
//         */
//        try {
//            pdf.setSelected(true);
//        } catch (PropertyVetoException e1) {
//            e1.printStackTrace();
//        }
//        pdf.toFront();
//        pdf.requestFocusInWindow();

        return decode_pdf;

    }
    //<end-fx>
    
    /**
     * decode and display selected page
     */
    public static void processPage(final Values commonValues, final PdfDecoderInt decode_pdf, final GUIFactory currentGUI, final GUIThumbnailPanel thumbnails) {

        if (commonValues.isPDF() && ((decode_pdf.isOpen() || !commonValues.isPDF() || decode_pdf.getJPedalObject(PdfDictionary.Linearized) != null))) {

            /**
             * get PRODUCER and if OCR disable text printing
             */
            final PdfFileInformation currentFileInformation = decode_pdf.getFileInformationData();

            /**
             * switch all on by default
             */
            decode_pdf.setRenderMode(PdfDecoderInt.RENDERIMAGES + PdfDecoderInt.RENDERTEXT);

            final String[] values = currentFileInformation.getFieldValues();
            final String[] fields = PdfFileInformation.getFieldNames();

            /**
             * holding all creators that produce OCR pdf's
             */
            final String[] ocr = {"TeleForm", "dgn2pdf"};

            for (int i = 0; i < fields.length; i++) {

                if ((fields[i].equals("Creator")) || (fields[i].equals("Producer"))) {

                    for (final String anOcr : ocr) {

                        if (values[i].equals(anOcr)) {

                            decode_pdf.setRenderMode(PdfDecoderInt.RENDERIMAGES);

                        }
                    }

                    //track Abbyy and tell JPedal to redraw highlights
                    if (values[i].equals("ABBYY FineReader 8.0 Professional Edition")) {
                        decode_pdf.setRenderMode(PdfDecoderInt.RENDERIMAGES + PdfDecoderInt.RENDERTEXT + PdfDecoderInt.OCR_PDF);
                    }
                }
            }

            final boolean currentProcessingStatus = Values.isProcessing();
            Values.setProcessing(true);	//stops listeners processing spurious event

            if (commonValues.isUseHiresImage()) {
                decode_pdf.useHiResScreenDisplay(true);
                ((GUI) currentGUI).setSelectedComboIndex(Commands.QUALITY, 1);
            } else {
                decode_pdf.useHiResScreenDisplay(false);
                ((GUI) currentGUI).setSelectedComboIndex(Commands.QUALITY, 0);
            }

            Values.setProcessing(currentProcessingStatus);

        }

        /**
         * special customisations for images
         */
        if (commonValues.isPDF()) {
            commonValues.setPageCount(decode_pdf.getPageCount());
        } else if (!commonValues.isMultiTiff()) {
            commonValues.setPageCount(1);
            decode_pdf.useHiResScreenDisplay(true);
        }

        if (commonValues.getPageCount() < commonValues.getCurrentPage()) {
            commonValues.setCurrentPage(commonValues.getPageCount());
            System.err.println(commonValues.getCurrentPage() + " out of range. Opening on last page");
            LogWriter.writeLog(commonValues.getCurrentPage() + " out of range. Opening on last page");
        }

        //values extraction mode,dpi of images, dpi of page as a factor of 72
        decode_pdf.setExtractionMode(PdfDecoderInt.TEXT, currentGUI.getScaling());

        /**
         * update the display, including any rotation
         */
        currentGUI.setPageNumber();

        currentGUI.resetRotationBox();

        if (commonValues.isPDF()) {
            currentGUI.decodePage();
        } else {
            //<start-fx>
            //resize (ensure at least certain size)
            currentGUI.scaleAndRotate();

            //add a border

            Images.decodeImage(hires, decode_pdf, currentGUI, thumbnails, commonValues);

            Values.setProcessing(false);
            //<end-fx>
        }
    }
    
    /**
     * initial method called to open a new PDF
     *
     * @throws PdfException
     */
    public static boolean openUpFile(final String selectedFile, final Values commonValues, final GUISearchWindow searchFrame,
            final GUIFactory currentGUI, final PdfDecoderInt decode_pdf, final PropertiesFile properties,
            final GUIThumbnailPanel thumbnails) throws PdfException {

        commonValues.maxViewY = 0;//ensure reset for any viewport

        searchFrame.resetSearchWindow();

        //Turn MultiPageTiff flag off to ensure no mistakes
        commonValues.setMultiTiff(false);

        boolean fileCanBeOpened = true;

        if (currentGUI.isSingle()) {
            decode_pdf.closePdfFile();
        }

        /**
         * ensure all data flushed from PdfDecoder before we decode the file
         */
        //decode_pdf.flushObjectValues(true);
        try {
            //System.out.println("commonValues.isPDF() = "+commonValues.isPDF()+" <<<");
            /**
             * opens the pdf and reads metadata
             */
            if (commonValues.isPDF()) {
                if (inputStream != null || selectedFile.startsWith("http") || selectedFile.startsWith("file:") || selectedFile.startsWith("jar:")) {
                    try {

                        //<link><a name="linearized" />
                        /**
                         * code below checks if file linearized and loads rest
                         * in background if it is
                         */
                        boolean isLinearized = false;

                        //use for all inputStream as we can't easily test
                        if (inputStream != null) {
                            isLinearized = true;
                        } else if (commonValues.getModeOfOperation() != Values.RUNNING_APPLET) {
                            isLinearized = isPDFLinearized(commonValues.getSelectedFile());
                        }

                        if (!isLinearized) {

                            if (commonValues.getSelectedFile().startsWith("jar:")) {
                                final InputStream is = Commands.class.getClass().getResourceAsStream(commonValues.getSelectedFile().substring(4));

                                decode_pdf.openPdfFileFromInputStream(is, false);
                            } else {
                                final DownloadProgress dlp = new DownloadProgress(commonValues.getSelectedFile());
                                final Thread t = new Thread() {
                                    @Override
                                    public void run() {
                                        while (dlp.isDownloading()) {
                                            currentGUI.setDownloadProgress("download", dlp.getProgress());
                                            try {
                                                Thread.sleep(500);
                                            } catch (final Exception e) {
                                                
                                                if(LogWriter.isOutput()) {
                                                    LogWriter.writeLog("Exception attempting to open file: " + e);
                                                }
                                            }
                                        }
                                    }
                                };
                                t.setDaemon(true);
                                t.start();
                                dlp.startDownload();

                                //currentGUI.showMessageDialog("cached 4");
                                final File tempFile = dlp.getFile();

                                //currentGUI.showMessageDialog("about to open "+tempFile.getCanonicalPath());
                                decode_pdf.openPdfFile(tempFile.getCanonicalPath());
                                //currentGUI.showMessageDialog("opened");
                            }

                        } else {

                            //currentGUI.showMessageDialog("loading linearized");
                            //update viewer to show this
                            currentGUI.setViewerTitle("Loading linearized PDF " + commonValues.getSelectedFile());

                            //now load linearized  part
                            if (inputStream != null) {
                                decode_pdf.openPdfFileFromInputStream(inputStream, true);
                            } else {
                                decode_pdf.openPdfFileFromURL(commonValues.getSelectedFile(), true);
                            }

                            final PdfObject linearObj = (PdfObject) decode_pdf.getJPedalObject(PdfDictionary.Linearized);
                            int linearfileLength = linearObj.getInt(PdfDictionary.L);

                            String message = "Downloading ";
                            linearfileLength /= 1024;
                            if (linearfileLength < 1024) {
                                message = message + linearfileLength + " kB";
                            } else {
                                linearfileLength /= 1024;
                                message = message + linearfileLength + " M";
                            }

                            final String fMessage = message;

                            final Thread fullReaderer = new Thread() {
                                @Override
                                public void run() {

                                    final LinearThread linearizedBackgroundReaderer = (LinearThread) decode_pdf.getJPedalObject(PdfDictionary.LinearizedReader);

                                    while (linearizedBackgroundReaderer != null && linearizedBackgroundReaderer.isAlive()) {

                                        try {
                                            Thread.sleep(1000);
                                            //System.out.println(".");
                                        } catch (final InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        currentGUI.setDownloadProgress(fMessage, linearizedBackgroundReaderer.getPercentageLoaded());

                                    }

                                    currentGUI.setDownloadProgress(fMessage, 100);

                                    processPage(commonValues, decode_pdf, currentGUI, thumbnails);

                                }
                            };

                            fullReaderer.setDaemon(true);
                            fullReaderer.start();

                        }
                    } catch (final Exception e) {
                        currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.UrlError") + " file=" + selectedFile + '\n' + e.getMessage());
                        decode_pdf.closePdfFile();
                        fileCanBeOpened = false;
                    }
                } else {

                    //alternate code to open via array or test byteArray
                    //not recommended for large files
                    final boolean test = false;
                    if (test) {
                        final File fileSize = new File(commonValues.getSelectedFile());
                        final byte[] bytes = new byte[(int) fileSize.length()];
                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(commonValues.getSelectedFile());
                            fis.read(bytes);
                            decode_pdf.openPdfArray(bytes);
                            //System.out.println("open via decode_pdf.openPdfArray(bytes)");

                        } catch (final Exception e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    } else {
                        try {
                            decode_pdf.openPdfFile(commonValues.getSelectedFile());
                        } catch (final RuntimeException e) {


                            //customise message for missing bouncycastle error
                            final String message;
                            if (e.getMessage()!=null && e.getMessage().contains("bouncycastle")) {
                                message = e.getMessage();
                            } else {
                                message = "Exception in code " + e.getMessage() + " please send to IDRsolutions";
                            }

                            currentGUI.showMessageDialog(message);
                            LogWriter.writeLog("Exception " + e.getMessage());
                        }
                    }
                    
                    if (decode_pdf.getPageCount() <= 1) {
                        currentGUI.getButtons().setPageLayoutButtonsEnabled(false);
                    }

                }

                //reset thumbnails
                currentGUI.reinitThumbnails();

            } else {

                //set values for page display
                decode_pdf.resetForNonPDFPage(1);

                JavaFXPageNavigator.setLastPageDecoded(1);

                final boolean isTiff = selectedFile.toLowerCase().contains(".tif");

                //decode image
                if (JAIHelper.isJAIused()) {
                    JAIHelper.confirmJAIOnClasspath();
                }

                final boolean isURL = selectedFile.startsWith("http:") || selectedFile.startsWith("file:");

                if (isTiff && JAIHelper.isJAIused()) {
                    try {

                        JavaFXPageNavigator.setTiffHelper(new JAITiffHelper(commonValues.getSelectedFile()));
                        final int pageCount = JavaFXPageNavigator.getTiffHelper().getTiffPageCount();

                        //Default to first page
                        commonValues.setTiffImageToLoad(0);
                        
                        //Multiple pages held within Tiff
                        if (pageCount > 1) {
                            //Set page count
                            decode_pdf.resetForNonPDFPage(pageCount);
                            commonValues.setPageCount(pageCount);
                            JavaFXPageNavigator.setLastPageDecoded(1);
                            //Flag to show this is a Tiff with multiple pages
                            commonValues.setMultiTiff(true);
                            commonValues.setMultiTiff(true);

                        }

                        JavaFXPageNavigator.drawMultiPageTiff(commonValues, decode_pdf);

                    } catch (final Exception e) {
                        e.printStackTrace();
                        LogWriter.writeLog("Exception " + e + Messages.getMessage("PdfViewerError.Loading") + commonValues.getSelectedFile());
                    }
                } else {
                    try {
                        // Load the source image from a file.
                        if (isURL) {
                            commonValues.setBufferedImg(ImageIO.read(new URL(selectedFile)));
                        } else {
                            commonValues.setBufferedImg(ImageIO.read(new File(selectedFile)));
                        }
                    } catch (final Exception e) {
                        LogWriter.writeLog("Exception " + e + "loading " + commonValues.getSelectedFile());
                    }

                }
            }
            //<<>>
            currentGUI.updateStatusMessage("opening file");

            /**
             * popup window if needed
             */
            if ((fileCanBeOpened) && (decode_pdf.isEncrypted()) && (!decode_pdf.isFileViewable())) {
                fileCanBeOpened = false;

                String password = System.getProperty("org.jpedal.password");
                if (password == null) {
                    password = currentGUI.showInputDialog(Messages.getMessage("PdfViewerPassword.message")); //$NON-NLS-1$
                }
                /**
                 * try and reopen with new password
                 */
                if (password != null) {
                    decode_pdf.setEncryptionPassword(password);
                    //decode_pdf.verifyAccess();

                    if (decode_pdf.isFileViewable()) {
                        fileCanBeOpened = true;
                    }

                }

                if (!fileCanBeOpened) {
                    currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPasswordRequired.message"));
                }

            }
            
            //Ensure bookmarks are loaded on file open
            currentGUI.setBookmarks(true);
            
            if (fileCanBeOpened) {

                if (properties.getValue("Recentdocuments").equals("true")) {
                    properties.addRecentDocument(commonValues.getSelectedFile());
                    currentGUI.getRecentDocument().updateRecentDocuments(properties.getRecentDocuments());
                }

                currentGUI.getRecentDocument().addToFileList(commonValues.getSelectedFile());

                /**
                 * reset values
                 */
                commonValues.setCurrentPage(1);
            }

        } catch (final PdfException e) {
            System.err.println(("Exception " + e + " opening file"));

            if (currentGUI.isSingle()) {

                if (GUI.showMessages) {
                    System.out.println("ErrorDialog in JavaFXOpenFile.java needs JavaFX Rewrite of Error Dialog class");
                    //ErrorDialog.showError(e, Messages.getMessage("PdfViewerOpenerror"), currentGUI.getFrame(), commonValues.getSelectedFile());
                }

                JavaFXExit.exit(thumbnails, currentGUI, commonValues, decode_pdf, properties);
            }

            throw e;
        }

        if (!decode_pdf.isOpen() && commonValues.isPDF() && decode_pdf.getJPedalObject(PdfDictionary.Linearized) == null) {
            return false;
        } else {
            return fileCanBeOpened;
        }
    }
    
    /**
     * opens a pdf file and calls the display/decode routines
     */
    public static void selectFile(final Values commonValues, final GUISearchWindow searchFrame,
            final GUIFactory currentGUI, final PdfDecoderInt decode_pdf, final PropertiesFile properties,
            final GUIThumbnailPanel thumbnails) {

        //remove search frame if visible
        if (searchFrame != null) {
            searchFrame.removeSearchWindow(false);
        }

        
        final FileChooser chooser = new FileChooser();
        chooser.setTitle("Open PDF file");
        
        //Open directory from existing directory
        if (file != null) {
            final File existDirectory = file.getParentFile();
            if (existDirectory.exists()) {
                chooser.setInitialDirectory(existDirectory);
            }
        }

        //Set extension filter
        final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        chooser.getExtensionFilters().add(extFilter);

        file = chooser.showOpenDialog((Stage)currentGUI.getFrame());

        /**
         * decode
         */
        if (file != null) {

            final String ext = file.getName().toLowerCase();
            final boolean isValid = ((ext.endsWith(".pdf")) || (ext.endsWith(".fdf"))
                    || (ext.endsWith(".tif")) || (ext.endsWith(".tiff"))
                    || (ext.endsWith(".png"))
                    || (ext.endsWith(".jpg")) || (ext.endsWith(".jpeg")));

            if (isValid) {
                /**
                 * save path so we reopen her for later selections
                 */
                try {
                    commonValues.setInputDir(file.getCanonicalPath());
                    open(file.getAbsolutePath(), commonValues, searchFrame, currentGUI, decode_pdf, properties, thumbnails);

                } catch (final IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewer.NotValidPdfWarning"));
            }

        } else { //no file selected so redisplay old
            currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.NoSelection"));
        }
    }
    
    /**
     * examine first few bytes to see if linearized and return true linearized file
     * @param pdfUrl
     * @return
     * @throws PdfException
     */
    public static final boolean isPDFLinearized(final String pdfUrl) {
        
        if (pdfUrl.startsWith("jar")) {
            return false;
        }
        
        boolean isLinear=false;
        //read first few bytes
        final URL url;
        final InputStream is;
        
        try {
            url = new URL(pdfUrl);
            is = url.openStream();
            //final String filename = url.getPath().substring(url.getPath().lastIndexOf('/')+1);
            
            // Download buffer
            final byte[] buffer = new byte[128];
            is.read(buffer);
            is.close();
            
            //test if linearized
            
            //scan for Linearized in text
            final int len=buffer.length;
            for(int i=0;i<len;i++ ){
                
                if(buffer[i]=='/' && buffer[i+1]=='L' && buffer[i+2]=='i' && buffer[i+3]=='n' && buffer[i+4]=='e' && buffer[i+5]=='a' && buffer[i+6]=='r'){
                    isLinear=true;
                    i=len;
                }
            }
            
        } catch (final IOException e) {
            if(LogWriter.isOutput()) {
                LogWriter.writeLog("[PDF] Exception " + e + " scanning URL " + pdfUrl);
            }
            e.printStackTrace();
        }
        
        return isLinear;
        
    }
    
    //////////////////////////////TEMP JAVAFX////////////////////////////////////////
    /**
     * Temporary JavaFX method to open PDFFiles until this class has finished JavaFXImplementation.
     * @param decode_pdf
     * @param commonValues 
     */
//    private static void openPDFFile(final PdfDecoderInt decode_pdf, final Values commonValues){
//        ///////////////////TEMPORARY OPENFILE CODE//////////////////
//        FileChooser chooser = new FileChooser();
//        chooser.setTitle("Open PDF file");
//        
//        //Open directory from existing directory
//        if (file != null) {
//            File existDirectory = file.getParentFile();
//            if (existDirectory.exists()) {
//                chooser.setInitialDirectory(existDirectory);
//            }
//        }
//
//        //Set extension filter
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
//        chooser.getExtensionFilters().add(extFilter);
//
//        file = chooser.showOpenDialog(null);
//
//        if (file != null) {
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    
//                    loadPDFFile(decode_pdf, commonValues);
//                }
//            });
//        }
//    }
//    
//    /**
//     * Temporary JavaFX method to open PDFFiles until this class has finished JavaFXImplementation.
//     * @param decode_pdf
//     * @param commonValues 
//     */
//    private static void loadPDFFile(PdfDecoderInt decode_pdf, Values commonValues) {
//        if (file == null) {
//            return;
//        }
//
//        String PDFfile = file.getAbsolutePath();
//        
//        Text fileLoc = new Text("No PDF Selected");
//        fileLoc.setText(PDFfile);
//
//        try {
//
//            //Open the pdf file so we can check for encryption
//            decode_pdf.openPdfFile(file.getAbsolutePath());
//
//            /**
//             * This code block deals with user input and JVM passwords in
//             * Encrypted PDF documents.
//             */
//            if (decode_pdf.isEncrypted()) {
//
//                int passwordCount = 0;        //Monitors how many attempts there have been to the password
//                closePasswordPrompt = false;  //Do not close the prompt box
//
//                //While the PDF content is not viewable, repeat until the correct password is found
//                while (!decode_pdf.isFileViewable() && !closePasswordPrompt) {
//
//                    /**
//                     * See if there's a JVM flag for the password & Use it if
//                     * there is Otherwise prompt the user to enter a password
//                     */
//                    if (System.getProperty("org.jpedal.password") != null) {
//                        password = System.getProperty("org.jpedal.password");
//                    } else if (!closePasswordPrompt) {
//                        showPasswordPrompt(passwordCount);
//                    }
//
//                    //If we have a password, try and open the PdfFile again with the password
//                    if (password != null) {
//                        //pdf.setEncryptionPassword(password);
//                        decode_pdf.openPdfFile(file.getAbsolutePath(), password);
//                    }
//                    passwordCount = passwordCount + 1; //Increment he password attempt
//
//                }
//
//            }
//            
//            try {
//                decode_pdf.decodePage(commonValues.getCurrentPage());
//            } catch (Exception ex) {
//                Logger.getLogger(JavaFXOpenFile.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        } catch (PdfException ex) {
//            ex.printStackTrace();
//            // If the pdf failed to open, don't decode it.
//            return;
//        }
//    }
//    
//    /**
//     * Temporary JavaFX method to open PDFFiles until this class has finished JavaFXImplementation.
//     * 
//     * This method will show a popup box and request for a password.
//     * 
//     * If the user does not enter the correct password it will ask them to try again.
//     * If the user presses the Cross button, the password prompt will close.
//     * 
//     * @param passwordCount is an int which represents the current input attempt
//     */
//    private static void showPasswordPrompt(final int passwordCount){
//        
//        //Setup password prompt content
//        final Stage enterPasswordStage = new Stage();
//        Button okButton = new Button("Ok");
//        Text titleText = new Text("Password Request");
//        final TextField inputPasswordField = new TextField("Please Enter Password");
//        
//        //If the user has attempted to enter the password more than once, change the text
//        if(passwordCount >= 1){
//            titleText.setText("Incorrect Password");
//            inputPasswordField.setText("Please Try Again");
//        }
//        
//        //Setup the password prompt & add children
//        enterPasswordStage.initModality(Modality.WINDOW_MODAL);
//        enterPasswordStage.setScene(new Scene(VBoxBuilder.create().children(titleText, inputPasswordField, okButton).
//        alignment(Pos.CENTER).padding(new Insets(10)).build()));
//              
//        //If the Ok button is pressed, store the user input as the password
//        okButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override public void handle(ActionEvent e) {
//                password = inputPasswordField.getText();
//                enterPasswordStage.hide();
//            }
//        });
//        
//        //Check whether stage is closed with the cross button, if it is stop requesting password.
//        enterPasswordStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//          public void handle(WindowEvent we) {
//              closePasswordPrompt = true;
//          }
//        });   
//        
//        //Halt everything until the Ok button / Cross button is pressed
//        enterPasswordStage.showAndWait();
//        
//    }
}
