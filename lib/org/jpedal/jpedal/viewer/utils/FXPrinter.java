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
 * FXPrinter.java
 * ---------------
 */
package org.jpedal.examples.viewer.utils;

import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.collections.ObservableSet;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

import org.jpedal.PdfDecoderFX;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.gui.popups.PrintPanelFX;
import org.jpedal.examples.viewer.gui.popups.PrintPanelInt;
import org.jpedal.exception.PdfException;
import org.jpedal.external.Options;
import org.jpedal.gui.GUIFactory;
import org.jpedal.objects.PrinterOptions;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

@SuppressWarnings("ALL")
public class FXPrinter implements PrinterInt {

    /**
     * flag to stop mutliple prints
     */
    private static int printingThreads;

    /**
     * page range to print
     */
    private int rangeStart = 1, rangeEnd = 1;

    /**
     * type of printing - all, odd, even
     */
    private int subset = PrinterOptions.ALL_PAGES;

    /**
     * Check to see if Printing cancelled
     */
    private boolean wasCancelled;

    private boolean pagesReversed;

//    /**provide user with visual clue to print progress*/
//    Timer updatePrinterProgress = null;
//    
//    private ProgressMonitor status = null;
    /**
     * needs to be global for the printer selection to work
     */
    private javafx.print.PrinterJob printJob;

    // for use with fest testing
    public static boolean showOptionPane = true;

    // public static int count;
    private PrintPanelInt printPanel;

    /**
     * current print page or -1 if finished
     */
    public int currentPrintPage = 1;

    /**
     * return page currently being printed or -1 if finished
     */
    public int getCurrentPrintPage() {
        return currentPrintPage;
    }

    @Override
    public void printPDF(final PdfDecoderInt decodePdf, final GUIFactory currentGUI, final String blacklist, final String defaultPrinter) {

//        final PdfDecoderInt decode_pdf = decodePdf;
        final String fileName = decodePdf.getFileName();
        //provides atomic flag on printing so we don't exit until all done
        printingThreads++;
        
        /**
         * printing in thread to improve background printing - comment out if
         * not required
         */
        printPanel = (PrintPanelFX) currentGUI.printDialog(getAvailablePrinters(blacklist), defaultPrinter);

        final Thread worker = new Thread() {

            @Override
            @SuppressWarnings("UnusedAssignment")
            public void run() {
                
                while (printPanel == null || printPanel.isVisible()) {
                    try {
                        Thread.sleep(200);
                    } catch (final InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                final boolean printFile = printPanel.okClicked();

                final PdfDecoderFX decode_pdf = new PdfDecoderFX();

                try {

                    final PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
                    
                    /**
                     * custom dialog so we can copy Acrobat PDF settings
                     * (removed from OS versions)
                     */
                    //ensure PDF display reappears
//                    ((PdfDecoderFX)decode_pdf).repaint();
                    decode_pdf.openPdfFile(fileName);
                    
                    //Ensure no border on output
                    decode_pdf.setBorderPresent(false);
                    
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

                    decode_pdf.setPrintPageMode(printMode);

                    //can also take values such as  new PageRanges("3,5,7-9,15");
                    final SetOfIntegerSyntax range = printPanel.getPrintRange();

                    //store color handler in case it needs to be replaced for grayscale printing
                    final Object storedColorHandler = decode_pdf.getExternalHandler(Options.ColorHandler);

                    if (range == null) {
                        currentGUI.showMessageDialog("No pages to print");
                    } else {
                        PdfDecoderFX.setPagePrintRange(range);

                        // workout values for progress monitor
                        rangeStart = range.next(0); // find first
                        currentPrintPage = rangeStart;

                        // find last
                        int i = rangeStart;
                        rangeEnd = rangeStart;
                        if (range.contains(2147483647)) //allow for all returning largest int
                        {
                            rangeEnd = decode_pdf.getPageCount();
                        } else {
                            while (range.next(i) != -1) {
                                i++;
                            }
                            rangeEnd = i;
                        }
                        
                        //<link><a name="printerautorotateandcenter" />
                        //Auto-rotate and scale flag
                        decode_pdf.setPrintAutoRotateAndCenter(printPanel.isAutoRotateAndCenter());

                        //<link><a name="printercurrentview" />
                        // Are we printing the current area only
                        decode_pdf.setPrintCurrentView(printPanel.isPrintingCurrentView());

                        //<link><a name="printerscaling" />
                        //set mode - see org.jpedal.objects.contstants.PrinterOptions for all values
                        decode_pdf.setPrintPageScalingMode(printPanel.getPageScaling());
                        
                        //set paper size
                        if (printPanel.getSelectedPaper() != null) {
                            pf.setPaper(printPanel.getSelectedPaper());
                        }
                        //Set paper orientation
                        pf.setOrientation(printPanel.getSelectedPrinterOrientation());

                        decode_pdf.setPageFormat(pf);

                        //<link><a name="printerusepdfsize" />
                        // flag if we use paper size or PDF size
                        decode_pdf.setUsePDFPaperSize(printPanel.isPaperSourceByPDFSize());
                        
                    }
                    
                    /**
                     * actual print call
                     */
                    if (printFile) {

                        if (!Platform.isFxApplicationThread()) {
                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {

                                    if (printJob != null) {
                                        try {
                                            print(decode_pdf, currentGUI, currentPrintPage);
                                        } catch (final Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            });
                        } else {

                            if (printJob != null) {
                                try {
                                    print(decode_pdf, currentGUI, currentPrintPage);
                                } catch (final Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                    }

                    //Restore color handler in case grayscale printing was used
                    decode_pdf.addExternalHandler(storedColorHandler, Options.ColorHandler);

                } catch (final Exception e) {
                    LogWriter.writeLog("Exception " + e + " printing");
                    e.printStackTrace();
                    currentGUI.showMessageDialog("Exception " + e);
                } catch (final Error err) {
                    err.printStackTrace();
                    LogWriter.writeLog("Error " + err + " printing");
                    currentGUI.showMessageDialog("Error " + err);
                }

                printingThreads--;

                //redraw to clean up
                decode_pdf.resetCurrentPrintPage();

            }
        };
        worker.setDaemon(true);
        //start printing in background (comment out if not required)
        worker.start();

    }

    private void print(final PdfDecoderFX decode_pdf, final GUIFactory currentGUI, int currentPrintPage) throws Exception {
        decode_pdf.decodePage(currentPrintPage);

        decode_pdf.waitForDecodingToFinish();
        decode_pdf.repaintPane(currentPrintPage);

        //Take print margins and set the closest paper size
        final javafx.print.Printer printer = printJob.getPrinter();
        final Iterator<javafx.print.Paper> i = printer.getPrinterAttributes().getSupportedPapers().iterator();
        javafx.print.Paper paper = printer.getDefaultPageLayout().getPaper();
        double selectedPaperWidth = printPanel.getSelectedPaper().getWidth();
        double selectedPaperHeight = printPanel.getSelectedPaper().getHeight();
        
        //Chose the closest size to the pdf page size
        if(printPanel.isPaperSourceByPDFSize()){
            selectedPaperWidth = decode_pdf.getPdfPageData().getCropBoxWidth(currentPrintPage);
            selectedPaperHeight = decode_pdf.getPdfPageData().getCropBoxHeight(currentPrintPage);
        }
        
        while (i.hasNext()) {
            final javafx.print.Paper p = i.next();
            if ((Math.abs(selectedPaperWidth - p.getWidth()) < Math.abs(selectedPaperWidth - paper.getWidth())) 
                && (Math.abs(selectedPaperHeight - p.getHeight()) < Math.abs(selectedPaperHeight - paper.getHeight()))) {
                    paper = p;
                }
            }
        
        final float[] margins = ((PrintPanelFX)printPanel).getMargins();
        final PageLayout pageLayout = printer.createPageLayout(paper, PageOrientation.PORTRAIT, (double)margins[0], (double)margins[1], (double)margins[2], (double)margins[3]);
        
        //Apply all print options here
        boolean shouldRotate = false;
        if (printPanel.isAutoRotateAndCenter()) {
            final boolean landscapePage = decode_pdf.getBoundsInParent().getWidth() > decode_pdf.getBoundsInParent().getHeight();
            final boolean landscapePaper = paper.getWidth()>paper.getHeight();
            shouldRotate = (landscapePage!=landscapePaper);
        }
        
        double originX = 0;
        double originY = 0;
        if(printPanel.isAutoRotateAndCenter()){
            originX = decode_pdf.getBoundsInParent().getWidth()/2;
            originY = decode_pdf.getBoundsInParent().getHeight()/2;
        }
        
        if(printPanel.isAutoRotateAndCenter()){
            decode_pdf.getTransforms().add(new Translate((pageLayout.getPrintableWidth()-decode_pdf.getBoundsInParent().getWidth())/2, (pageLayout.getPrintableHeight()-decode_pdf.getBoundsInParent().getHeight())/2));
            
            if(shouldRotate){
                decode_pdf.getTransforms().add(new Rotate(90, originX, originY));
            }
        }
        
        //Print scaling
        final double scaleX = pageLayout.getPrintableWidth() / decode_pdf.getBoundsInParent().getWidth();
        final double scaleY = pageLayout.getPrintableHeight() / decode_pdf.getBoundsInParent().getHeight();
       
        final int scaleFactor = printPanel.getPageScaling();
        double scale = 1.0d;
        switch(scaleFactor){
            case PrinterOptions.PAGE_SCALING_FIT_TO_PRINTER_MARGINS:
                scale = scaleX;
                if (scaleY < scaleX) {
                    scale = scaleY;
                }
                
                break;
            case PrinterOptions.PAGE_SCALING_REDUCE_TO_PRINTER_MARGINS:
                scale = scaleX;
                if (scaleY < scaleX) {
                    scale = scaleY;
                }
                if(scale>1){//Only scale if larger than print size
                    scale = 1.0d;
                }
                break;
            case PrinterOptions.PAGE_SCALING_NONE:
                //Do Nothing as already set to 1
                break;
        }
        
        //Scale page
        decode_pdf.getTransforms().add(new Scale(scale, -scale, originX, originY));
        
        //Print Page Size
        printJob.getJobSettings().setPageLayout(pageLayout);
        
        if(printPanel.isPagesReversed()){
            final int temp = rangeEnd;
            rangeEnd = rangeStart;
            rangeStart = temp;
            //currentPrintPage = temp;
        }
                
        if (printJob != null) {
           // final boolean success = false;
            int copies = printPanel.getCopies();
            while (copies > 0) {
                currentPrintPage = rangeStart;
                while(currentPrintPage != rangeEnd){
                    if((subset == PrinterOptions.ALL_PAGES) ||
                            (subset == PrinterOptions.ODD_PAGES_ONLY && currentPrintPage%2==1) ||
                            (subset == PrinterOptions.EVEN_PAGES_ONLY && currentPrintPage%2==0)){

                        decode_pdf.decodePage(currentPrintPage);
                        decode_pdf.waitForDecodingToFinish();
                        decode_pdf.repaintPane(currentPrintPage);
                        printJob.printPage(decode_pdf);
                    }

                    if(printPanel.isPagesReversed()){
                        currentPrintPage--;
                    }else{
                        currentPrintPage++;
                    }

                }

                //Final print for the range
                if((subset == PrinterOptions.ALL_PAGES) ||
                        (subset == PrinterOptions.ODD_PAGES_ONLY && currentPrintPage%2==1) ||
                        (subset == PrinterOptions.EVEN_PAGES_ONLY && currentPrintPage%2==0)){

                    decode_pdf.decodePage(currentPrintPage);
                    decode_pdf.waitForDecodingToFinish();
                    decode_pdf.repaintPane(currentPrintPage);
                    printJob.printPage(decode_pdf);
                }
                
                copies--;
            }
//            if (success) {
                printJob.endJob();
//            }
        }

        if ((!wasCancelled)) {
                                                //System.out.println("FINISHED");
            if (showOptionPane) {
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerPrintingFinished"));
            }

        }

        //currentPrintPage = -1;
    }
    //<link><a name="printerlist" />
    public static String[] getAvailablePrinters(final String blacklist) {
        final PrintService[] service = PrinterJob.lookupPrintServices();

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
            System.arraycopy(temp, 0, serviceNames, 0, count);
        } else {
            for (int i = 0; i < noOfPrinters; i++) {
                serviceNames[i] = service[i].getName();
            }
        }

        return serviceNames;
    }

    /**
     * visual print indicator
     */
    //private final String dots = ".";
    
    /**
    private void updatePrinterProgess(final PdfDecoderInt decode_pdf, final int currentPage) {

    	//System.out.println("UPDATE METHOD");
        //Calculate no of pages printing
        int noOfPagesPrinting = (rangeEnd - rangeStart + 1);

        //Calculate which page we are currently printing
        final int currentPrintingPage = (currentPage - rangeStart);

        final int actualCount = noOfPagesPrinting;
        final int actualPage = currentPrintingPage;
        //int actualPercentage= (int) (((float)actualPage/(float)actualCount)*100);

//        if(status.isCanceled()){
//            ((PdfDecoderFX)decode_pdf).stopPrinting();
//            updatePrinterProgress.stop();
//            status.close();
//            wasCancelled=true;
//            printingThreads--;
//            
//            if(!messageShown){
//                JOptionPane.showMessageDialog(null,Messages.getMessage("PdfViewerPrint.PrintingCanceled"));
//                messageShown=true;
//            }
//            return;
//            
//        }
        //update visual clue
        dots += '.';
        if (dots.length() > 8) {
            dots = ".";
        }

        //allow for backwards
        boolean isBackwards = ((currentPrintingPage <= 0));

        if (rangeStart == rangeEnd) {
            isBackwards = false;
        }

        if ((isBackwards)) {
            noOfPagesPrinting = (rangeStart - rangeEnd + 1);
        }

        int percentage = (int) (((float) currentPrintingPage / (float) noOfPagesPrinting) * 100);

        if ((!isBackwards) && (percentage < 1)) {
            percentage = 1;
        }

        //invert percentage so percentage works correctly
        if (isBackwards) {
            percentage = -percentage;
            //currentPrintingPage=-currentPrintingPage;
        }

//        if (pagesReversed) {
//            percentage = 100 - percentage;
//        }

//        status.setProgress(percentage);
        //String message;
//        if(subset==PrinterOptions.ODD_PAGES_ONLY){
//            actualCount=((actualCount/2)+1);
//            actualPage=actualPage/2;
//
//        }else if(subset==PrinterOptions.EVEN_PAGES_ONLY){
//            actualCount=((actualCount/2)+1);
//            actualPage=actualPage/2;
//
//        }
        // allow for printing 1 page
        // Set to page 1 of 1 like Adobe
        
//        if (actualCount==1){
//            actualPercentage=50;
//            actualPage=1;
//            status.setProgress(actualPercentage);
//        }
//        message=actualPage + " "+Messages.getMessage("PdfViewerPrint.Of")+ ' ' +
//                actualCount + ": " + actualPercentage + '%' + ' ' +dots;
//        if(pagesReversed){
//            message=(actualCount-actualPage) + " "+Messages.getMessage("PdfViewerPrint.Of")+ ' ' +
//                    actualCount + ": " + percentage + '%' + ' ' +dots;
//            
//            status.setNote(Messages.getMessage("PdfViewerPrint.ReversedPrinting")+ ' ' + message);
//            
//        }else if(isBackwards)
//            status.setNote(Messages.getMessage("PdfViewerPrint.ReversedPrinting")+ ' ' + message);
//        else
//            status.setNote(Messages.getMessage("PdfViewerPrint.Printing")+ ' ' + message);
    }
/**/

    //}
    public static boolean isPrinting() {

        return printingThreads > 0;
    }

    //<link><a name="printerset" />
    private void setPrinter(final String chosenPrinter) throws PdfException {

        final ObservableSet<javafx.print.Printer> printers = javafx.print.Printer.getAllPrinters();

        boolean matchFound = false;

        final Iterator<javafx.print.Printer> it = printers.iterator();
        javafx.print.Printer p;
        while (it.hasNext() && !matchFound) {
            p = it.next();
            if (p != null && p.getName().contains(chosenPrinter)) {
                printJob = javafx.print.PrinterJob.createPrinterJob(p);
                matchFound = true;
            }
        }

        if (!matchFound) {
            throw new PdfException("Unknown printer " + chosenPrinter);
        }
    }

    /**
     * listener code - just an example
     */
    private static class PDFPrintJobListener implements PrintJobListener {

        private static final boolean showMessages = false;

        @Override
        public void printDataTransferCompleted(final PrintJobEvent printJobEvent) {
            if (showMessages) {
                System.out.println("printDataTransferCompleted=" + printJobEvent.toString());
            }
        }

        @Override
        public void printJobCompleted(final PrintJobEvent printJobEvent) {
            if (showMessages) {
                System.out.println("printJobCompleted=" + printJobEvent.toString());
            }
        }

        @Override
        public void printJobFailed(final PrintJobEvent printJobEvent) {
            if (showMessages) {
                System.out.println("printJobEvent=" + printJobEvent.toString());
            }
        }

        @Override
        public void printJobCanceled(final PrintJobEvent printJobEvent) {
            if (showMessages) {
                System.out.println("printJobFailed=" + printJobEvent.toString());
            }
        }

        @Override
        public void printJobNoMoreEvents(final PrintJobEvent printJobEvent) {
            if (showMessages) {
                System.out.println("printJobNoMoreEvents=" + printJobEvent.toString());
            }
        }

        @Override
        public void printJobRequiresAttention(final PrintJobEvent printJobEvent) {
            if (showMessages) {
                System.out.println("printJobRequiresAttention=" + printJobEvent.toString());
            }
        }
    }
//    private int duplexGapOdd, duplexGapEven;
//    private boolean centerPrintedPage;
//    private boolean rotatePrintedPage;
//    
//    public void setPrintIndent(final int oddPages, final int evenPages) {
//        this.duplexGapOdd = oddPages;
//        this.duplexGapEven = evenPages;
//    }
//
//    public void setCenterOnScaling(final boolean center){
//
//        centerPrintedPage = center;
//    }
//    
//    public void setAutoRotate(final boolean rotate){
//
//        rotatePrintedPage = rotate;
//    }
}
