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
 * Printer.java
 * ---------------
 */
package org.jpedal.examples.viewer.utils;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.PageRanges;

import javax.print.attribute.standard.PrinterResolution;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;

import org.jpedal.*;

import org.jpedal.color.PdfPaint;
import org.jpedal.examples.viewer.gui.popups.PrintPanel;
import org.jpedal.exception.PdfException;
import org.jpedal.external.ColorHandler;
import org.jpedal.external.Options;
import org.jpedal.gui.GUIFactory;
import org.jpedal.objects.PrinterOptions;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

public class Printer implements PrinterInt{
    
    /**flag to stop mutliple prints*/
    private static int printingThreads;
    
    /**page range to print*/
    int rangeStart = 1, rangeEnd = 1;
    
    /**type of printing - all, odd, even*/
    int subset = PrinterOptions.ALL_PAGES;
    
    /**Check to see if Printing cancelled*/
    boolean wasCancelled;
    
    /**Allow Printing Cancelled to appear once*/
    boolean messageShown;
    
    boolean pagesReversed;
    
    /**provide user with visual clue to print progress*/
    Timer updatePrinterProgress;
    
    private ProgressMonitor status;
    
    /** needs to be global for the printer selection to work */
    private DocPrintJob printJob;
    
    // for use with fest testing
    public static boolean showOptionPane = true;
    
    // public static int count;
    
    @Override
    public void printPDF(final PdfDecoderInt decodePdf, final GUIFactory currentGUI, final String blacklist, final String defaultPrinter) {

        //provides atomic flag on printing so we don't exit until all done
        printingThreads++;
        
        /**
         * printing in thread to improve background printing -
         * comment out if not required
         */
        final Thread worker = new Thread() {
            
            @Override
            @SuppressWarnings("UnusedAssignment")
            public void run() {
                
                boolean printFile = false;
                try {
                    
                    final PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
                    
                    //<link><a name="printerpagesize" />
                    /**
                     * default page size
                     */
                    final Paper paper = new Paper();
                    paper.setSize(595, 842);
                    paper.setImageableArea(43, 43, 509, 756);
                    
                    pf.setPaper(paper);
                    
                    /**
                     * workaround to improve performance on PCL printing
                     * by printing using drawString or Java's glyph if font
                     * available in Java
                     */
                    //decode_pdf.setTextPrint(PdfDecoder.NOTEXTPRINT); //normal mode - only needed to reset
                    //decode_pdf.setTextPrint(PdfDecoder.TEXTGLYPHPRINT); //intermediate mode - let Java create Glyphs if font matches
                    //decode_pdf.setTextPrint(PdfDecoder.TEXTSTRINGPRINT); //try and get Java to do all the work
                    
                    //wrap in Doc as we can then add a listeners
                    final Doc doc = new SimpleDoc(decodePdf, DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
                    
                    //setup default values to padd into JPS
                    final PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                    aset.add(new PageRanges(1, decodePdf.getPageCount()));
                    
                    // useful debugging code to show supported values and values returned by printer
                    //Attribute[] settings = aset.toArray();
                    
                    //Class[] attribs=printJob.getPrintService().getSupportedAttributeCategories();
                    //for(int i=0;i<attribs.length;i++)
                    //System.out.println(i+" "+attribs[i]);
                    
                    //for(int i=0;i<settings.length;i++) //show values set by printer
                    //	System.out.println(i+" "+settings[i].toString()+" "+settings[i].getName());
                    
                    /**
                     * custom dialog so we can copy Acrobat PDF settings
                     * (removed from OS versions)
                     */
                    final PrintPanel printPanel = (PrintPanel) currentGUI.printDialog(getAvailablePrinters(blacklist), defaultPrinter);
                    
                    printFile = printPanel.okClicked();
                    
                    //ensure PDF display reappears
                    ((PdfDecoder) decodePdf).repaint();
                    
                    // set values in JPS
                    
                    // choose the printer, testing if printer in list
                    setPrinter(printPanel.getPrinter());
                    
                    //<link><a name="printerrange" />
                    //range of pages
                    int printMode = 0;
                    subset = PrinterOptions.ALL_PAGES;
                    if (printPanel.isOddPagesOnly()) {
                        printMode = PrinterOptions.ODD_PAGES_ONLY;
                        subset = PrinterOptions.ODD_PAGES_ONLY;
                    } else if (printPanel.isEvenPagesOnly()) {
                        printMode = PrinterOptions.EVEN_PAGES_ONLY;
                        subset = PrinterOptions.EVEN_PAGES_ONLY;
                    }
                    
                    //flag to show reversed
                    pagesReversed = printPanel.isPagesReversed();
                    if (pagesReversed) {
                        printMode += PrinterOptions.PRINT_PAGES_REVERSED;
                    }
                    
                    ((PdfDecoder) decodePdf).setPrintPageMode(printMode);
                    
                    //can also take values such as  new PageRanges("3,5,7-9,15");
                    final SetOfIntegerSyntax range = printPanel.getPrintRange();
                    
                    //store color handler in case it needs to be replaced for grayscale printing
                    final Object storedColorHandler = decodePdf.getExternalHandler(Options.ColorHandler);
                    
                    if (range == null) {
                        currentGUI.showMessageDialog("No pages to print");
                    } else {
                        ((PdfDecoder) decodePdf).setPagePrintRange(range);
                        
                        // workout values for progress monitor
                        rangeStart = range.next(0); // find first
                        
                        // find last
                        int i = rangeStart;
                        rangeEnd = rangeStart;
                        if (range.contains(2147483647)) //allow for all returning largest int
                        {
                            rangeEnd = decodePdf.getPageCount();
                        } else {
                            while (range.next(i) != -1) {
                                i++;
                            }
                            rangeEnd = i;
                        }
                        
                        //pass through number of copies
                        aset.add(new Copies(printPanel.getCopies()));
                        
                        //<link><a name="printerautorotateandcenter" />
                        //Auto-rotate and scale flag
                        ((PdfDecoder) decodePdf).setPrintAutoRotateAndCenter(printPanel.isAutoRotateAndCenter());
                        
                        //<link><a name="printercurrentview" />
                        // Are we printing the current area only
                        ((PdfDecoder) decodePdf).setPrintCurrentView(printPanel.isPrintingCurrentView());
                        
                        //<link><a name="printerscaling" />
                        //set mode - see org.jpedal.objects.contstants.PrinterOptions for all values
                        ((PdfDecoder) decodePdf).setPrintPageScalingMode(printPanel.getPageScaling());
                        
                        //<link><a name="printermonochrome" />
                        //Set whether to print in monochrome or full color
                        if (printPanel.isMonochrome()) {
                            aset.remove(Chromaticity.COLOR);
                            aset.add(Chromaticity.MONOCHROME);
                            decodePdf.addExternalHandler(new ColorHandler() {
                                @Override
                                public void setPaint(final Graphics2D g2, final PdfPaint textFillCol, final int pageNumber, final boolean isPrinting) {
                                    //converts to grayscale for printing
                                    if (isPrinting && textFillCol != null) { //only on printout

                                        final int rgb = textFillCol.getRGB();

                                        //get the value
                                        final float[] val = new float[3];
                                        val[0] = ((rgb >> 16) & 255) / 255f;
                                        val[1] = ((rgb >> 8) & 255) / 255f;
                                        val[2] = (rgb & 255) / 255f;

                                        //to gray
                                        final ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
                                        final float[] grayVal = cs.fromRGB(val);

                                        final Color colGray = new Color(cs, grayVal, 1f);
                                        g2.setPaint(colGray);

                                    } else {
                                        g2.setPaint(textFillCol);
                                    }
                                }

                                @Override
                                public BufferedImage processImage(final BufferedImage image, final int pageNumber, final boolean isPrinting) {

                                    if (isPrinting && image != null) { //only on printout

                                        //grayscale conversion
                                        final BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                                        final Graphics2D newG2 = newImage.createGraphics();
                                        newG2.setPaint(Color.WHITE);
                                        newG2.fillRect(0, 0, image.getWidth(), image.getHeight());
                                        newG2.drawImage(image, 0, 0, null);

                                        return newImage;
                                    }
                                    return image;
                                }
                            }, Options.ColorHandler);
                        } else {
                            aset.remove(Chromaticity.MONOCHROME);
                            aset.add(Chromaticity.COLOR);
                        }
                        
                        //set paper size
                        if (printPanel.getSelectedPaper() != null) {
                            pf.setPaper(printPanel.getSelectedPaper());
                        }
                        //Set paper orientation
                        pf.setOrientation(printPanel.getSelectedPrinterOrientation());
                        
                        ((PdfDecoder) decodePdf).setPageFormat(pf);
                        
                        //<link><a name="printerusepdfsize" />
                        // flag if we use paper size or PDF size
                        ((PdfDecoder) decodePdf).setUsePDFPaperSize(printPanel.isPaperSourceByPDFSize());
                        
                        //Set print resolution
                        final PrinterResolution res = printPanel.getResolution();
                        if (res != null) {
                            aset.add(res);
                        }
                        
                    }
                    
                    /**/
                    
                    /**
                     * popup to show user progress
                     */
                    if (showOptionPane) {
                        status = new ProgressMonitor((Container)currentGUI.getFrame(), "", "", 1, 100);
                        
                        /** used to track user stopping movement and call refresh every 2 seconds*/
                        updatePrinterProgress = new Timer(1000, new ActionListener() {
                            
                            @Override
                            public void actionPerformed(final ActionEvent event) {
                                
                                final int currentPage = ((PdfDecoder) decodePdf).getCurrentPrintPage();
                                
                                if (currentPage > 0) {
                                    updatePrinterProgess(decodePdf, currentPage);
                                }
                                
                                //make sure turned off
                                if (currentPage == -1) {
                                    updatePrinterProgress.stop();
                                    status.close();
                                }
                            }
                        });
                        updatePrinterProgress.setRepeats(true);
                        updatePrinterProgress.start();
                    }
                    
                    //Name the print job the same as the Pdf file.
                    String name= decodePdf.getFileName();
                    if(name==null){ //can be null if we pass in PDF as byte[] array
                        name="JPedal printing";
                    }else{
                        final String[] jobString = decodePdf.getFileName().split("/");
                        final JobName jobName = new JobName(jobString[jobString.length-1], null);
                        if(printJob.getPrintService().isAttributeValueSupported(jobName, DocFlavor.SERVICE_FORMATTED.PAGEABLE, aset)) {
                            aset.add(jobName);
                        }
                    }
                    
                    /**
                     * actual print call
                     */
                    if (printFile) {
                        //used to track print activity
                        printJob.addPrintJobListener(new PDFPrintJobListener());
                        
                        //Print PDF document
                        printJob.print(doc, aset);
                    }
                    
                    //Restore color handler in case grayscale printing was used
                    decodePdf.addExternalHandler(storedColorHandler, Options.ColorHandler);
                    
                } catch (final Exception e) {
                    LogWriter.writeLog("Exception " + e + " printing");
                    e.printStackTrace();
                    currentGUI.showMessageDialog("Exception " + e);
                } catch (final Error err) {
                    err.printStackTrace();
                    LogWriter.writeLog("Error " + err + " printing");
                    currentGUI.showMessageDialog("Error " + err);
                }
                
                /**
                 * visual print update progress box
                 */
                if (updatePrinterProgress != null) {
                    updatePrinterProgress.stop();
                    status.close();
                }
                /**report any or our errors
                 * (we do it this way rather than via PrinterException as MAC OS X has a nasty bug in PrinterException)
                 */
                if (!printFile && !((PdfDecoder) decodePdf).isPageSuccessful()) {
                    String errorMessage = Messages.getMessage("PdfViewerError.ProblemsEncountered") + ((PdfDecoder) decodePdf).getPageFailureMessage() + '\n';
                    
                    if (((PdfDecoder) decodePdf).getPageFailureMessage().toLowerCase().contains("memory")) {
                        errorMessage += Messages.getMessage("PdfViewerError.RerunJava")
                                + Messages.getMessage("PdfViewerError.RerunJava1")
                                + Messages.getMessage("PdfViewerError.RerunJava2");
                    }
                    
                    currentGUI.showMessageDialog(errorMessage);
                }
                
                printingThreads--;
                
                //redraw to clean up
                ((PdfDecoder) decodePdf).resetCurrentPrintPage();
                ((PdfDecoder) decodePdf).invalidate();
                ((PdfDecoder) decodePdf).updateUI();
                ((PdfDecoder) decodePdf).repaint();
                
                
                
                if ((printFile && !wasCancelled)) {
                    
                    if (showOptionPane) {
                        currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintingFinished"));
                    }
                    
                    
                }
            }
        };
        worker.setDaemon(true);
        //start printing in background (comment out if not required)
        worker.start();
        
    }
    
    //<link><a name="printerlist" />
    
    public static String[] getAvailablePrinters(final String blacklist) {
        final PrintService[] service=PrinterJob.lookupPrintServices();
        
        final int noOfPrinters = service.length;
        String[] serviceNames = new String[noOfPrinters];
        
        //check blacklist
        if (blacklist != null) {
            final String[] bl = blacklist.split(",");
            
            int count = 0;
            //loop through printservices
            for (final PrintService aService : service) {
                boolean pass = true;
                final String name = aService.getName();
                
                //loop through blacklist items
                for (final String aBl : bl) {
                    
                    //check for wildcard
                    if (aBl.contains("*")) {
                        final String term = aBl.replace("*", "").trim();
                        if (name.contains(term)) {
                            pass = false;
                        }
                        
                    } else if (name.toLowerCase().equals(aBl.toLowerCase())) {
                        pass = false;
                    }
                }
                
                //Add to array
                if (pass) {
                    serviceNames[count] = name;
                    count++;
                }
            }
            
            //Trim array
            final String[] temp = serviceNames;
            serviceNames = new String[count];
            System.arraycopy(temp,0,serviceNames,0,count);
        } else {
            for(int i=0;i<noOfPrinters;i++) {
                serviceNames[i] = service[i].getName();
            }
        }
        
        return serviceNames;
    }
    
    
    /**visual print indicator*/
    private String dots=".";
    
    private void updatePrinterProgess(final PdfDecoderInt decode_pdf, final int currentPage) {
        
        
        //Calculate no of pages printing
        int noOfPagesPrinting=(rangeEnd-rangeStart+1);
        
        //Calculate which page we are currently printing
        final int currentPrintingPage=(currentPage-rangeStart);
        
        int actualCount=noOfPagesPrinting;
        int actualPage=currentPrintingPage;
        int actualPercentage= (int) (((float)actualPage/(float)actualCount)*100);
        
        
        if(status.isCanceled()){
            ((PdfDecoder)decode_pdf).stopPrinting();
            updatePrinterProgress.stop();
            status.close();
            wasCancelled=true;
            printingThreads--;
            
            if(!messageShown){
                JOptionPane.showMessageDialog(null,Messages.getMessage("PdfViewerPrint.PrintingCanceled"));
                messageShown=true;
            }
            return;
            
        }
        
        
        //update visual clue
        dots += '.';
        if(dots.length()>8) {
            dots = ".";
        }
        
        //allow for backwards
        boolean isBackwards=((currentPrintingPage<=0));
        
        if(rangeStart==rangeEnd) {
            isBackwards = false;
        }
        
        if((isBackwards)) {
            noOfPagesPrinting = (rangeStart - rangeEnd + 1);
        }
        
        int percentage = (int) (((float)currentPrintingPage / (float)noOfPagesPrinting) * 100);
        
        if((!isBackwards)&&(percentage<1)) {
            percentage = 1;
        }
        
        
        //invert percentage so percentage works correctly
        if(isBackwards){
            percentage=-percentage;
            //currentPrintingPage=-currentPrintingPage;
        }
        
        if(pagesReversed) {
            percentage = 100 - percentage;
        }
        
        status.setProgress(percentage);
        String message;
        
        if(subset==PrinterOptions.ODD_PAGES_ONLY){
            actualCount=((actualCount/2)+1);
            actualPage /= 2;
            
        }else if(subset==PrinterOptions.EVEN_PAGES_ONLY){
            actualCount=((actualCount/2)+1);
            actualPage /= 2;
            
        }
        
        /*
         * allow for printing 1 page
         * Set to page 1 of 1 like Adobe
         */
        if (actualCount==1){
            actualPercentage=50;
            actualPage=1;
            status.setProgress(actualPercentage);
        }
        
        message=actualPage + " "+Messages.getMessage("PdfViewerPrint.Of")+ ' ' +
                actualCount + ": " + actualPercentage + '%' + ' ' +dots;
        
        if(pagesReversed){
            message=(actualCount-actualPage) + " "+Messages.getMessage("PdfViewerPrint.Of")+ ' ' +
                    actualCount + ": " + percentage + '%' + ' ' +dots;
            
            status.setNote(Messages.getMessage("PdfViewerPrint.ReversedPrinting")+ ' ' + message);
            
        }else if(isBackwards) {
            status.setNote(Messages.getMessage("PdfViewerPrint.ReversedPrinting") + ' ' + message);
        } else {
            status.setNote(Messages.getMessage("PdfViewerPrint.Printing") + ' ' + message);
        }
        
    }
    
    
    
    //}
    
    public static boolean isPrinting() {
        
        return printingThreads>0;
    }
    
    //<link><a name="printerset" />
    
    private void setPrinter(final String chosenPrinter) throws PdfException {
        
        final PrintService[] service=PrinterJob.lookupPrintServices(); //list of printers
        
        final int count=service.length;
        boolean matchFound=false;
        
        for(int i=0;i<count;i++){
            if(service[i].getName().contains(chosenPrinter)){
                printJob= service[i].createPrintJob();
                i=count;
                matchFound=true;
            }
        }
        if(!matchFound) {
            throw new PdfException("Unknown printer " + chosenPrinter);
        }
    }
    
    /**
     * listener code - just an example
     */
    private static class PDFPrintJobListener implements PrintJobListener {
        
        private static final boolean showMessages=false;
        
        @Override
        public void printDataTransferCompleted(final PrintJobEvent printJobEvent) {
            if(showMessages) {
                System.out.println("printDataTransferCompleted=" + printJobEvent);
            }
        }
        
        @Override
        public void printJobCompleted(final PrintJobEvent printJobEvent) {
            if(showMessages) {
                System.out.println("printJobCompleted=" + printJobEvent);
            }
        }
        
        @Override
        public void printJobFailed(final PrintJobEvent printJobEvent) {
            if(showMessages) {
                System.out.println("printJobEvent=" + printJobEvent);
            }
        }
        
        @Override
        public void printJobCanceled(final PrintJobEvent printJobEvent) {
            if(showMessages) {
                System.out.println("printJobFailed=" + printJobEvent);
            }
        }
        
        @Override
        public void printJobNoMoreEvents(final PrintJobEvent printJobEvent) {
            if(showMessages) {
                System.out.println("printJobNoMoreEvents=" + printJobEvent);
            }
        }
        
        @Override
        public void printJobRequiresAttention(final PrintJobEvent printJobEvent) {
            if(showMessages) {
                System.out.println("printJobRequiresAttention=" + printJobEvent);
            }
        }
    }
}
