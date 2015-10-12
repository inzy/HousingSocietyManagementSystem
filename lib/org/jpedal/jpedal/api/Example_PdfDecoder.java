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
 * Example_PdfDecoder.java
 * ---------------
 */
package org.jpedal.examples.api;

import java.awt.print.PrinterException;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

@SuppressWarnings("ALL")
public class Example_PdfDecoder {
    
    ApiParams params = new ApiParams();
    
    public void Example_PdfDecoder(){
        
        ////////////////////////////////////////////////////////////////////////
        //constructors
        ////////////////////////////////////////////////////////////////////////
        
        /**
         * Recommend way to create a PdfDecoder for page rendering only.
         * Use PdfDecoder(params.booleanValue) if server extraction is required.
         */
        @SuppressWarnings("UnusedAssignment")
        PdfDecoder decoder = new PdfDecoder();
        
        /**
         * Recommended way to create a PdfDecoder if no page rendering is required.
         * Otherwise use PdfDecoder().
         *
         * Indicate if pages are being rendered (true) or if only extraction is taking place (false).
         */
        decoder = new PdfDecoder(params.booleanValue);
        
        ////////////////////////////////////////////////////////////////////////
        //public methods you can use
        ////////////////////////////////////////////////////////////////////////
        
        /**
         * Get the current logical page number.
         */
        params.intValue = decoder.getPageNumber();
        
        /**
         * Set the page's rotation.
         * A value of 90 will set the rotation to 90º.
         */
        decoder.setDisplayRotation(params.intValue);
        
        /**
         * return page number for last page decoded.
         * Only use in SingleDisplay mode.
         */
        params.intValue = decoder.getlastPageDecoded();
        
        /**
         * Get the details on page for type (defined in org.jpedal.constants.PageInfo) or null if there are no values.
         * Unrecognised key will throw a RunTime exception.
         * Null returned if JPedal is not clear on the result.
         */
        params.iteratorValue = decoder.getPageInfo(params.intValue);
        
        /**
         * Get an object containing the data for the outlines.
         */
        params.outlineDataValue = decoder.getOutlineData();
        
        /**
         * Track if the file is still loaded in the background.
         */
        params.booleanValue = decoder.isLoadingLinearizedPDF();
        
        /**
         * Get the type of alignment for the pages if they're smaller than the panel.
         * See options in Display class.
         */
        params.intValue = decoder.getPageAlignment();
        
        //<start-adobe>
        
        /**
         * Set whether width data is embedded.
         */
        PdfDecoder.init(params.booleanValue);
        
        //<end-adobe>

        /**
         * remove all static elements.
         * Use only once completely finished with JPedal as will not be reinitialised.
         */
        PdfDecoder.disposeAllStatic();
        
        /**
         * Remove all items from memory.
         * Call disposeAllStatic() if you wish to clear all static objects as well.
         */
        decoder.dispose();
        
        /**
         * Close the current PDF file and release all resources/delete any temporary files.
         */
        decoder.closePdfFile();
        
        /**
         * Show if PDF document contains an outline.
         */
        params.booleanValue = decoder.hasOutline();
        
        /**
         * Get a Document containing all the information on the PDF outline in an XML format.
         */
        params.documentValue = decoder.getOutlineAsXML();
        
        /**
         * Get an object containing the information on the page for calculating grouping.
         * Please note: Structure of PdfPageData is not guaranteed to remain constant.
         * Please contact IDRsolutions for advice.
         */
        params.pdfPageDataValue = decoder.getPdfPageData();
        
        /**
         * Set page range for printing (inclusive).
         * If end (2nd Parameter) is less than start (2nd Parameter) it will print them backwards.
         * (an invalid page range will throw a PdfException)
         */
        try {
            decoder.setPagePrintRange(params.intValue, params.intValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Tells program to try and use Java's font printing if possible as a work
         * around for an issue with PCL printing.
         * Values are:
         * PdfDecoder.TEXTGLYPHPRINT (use Java to rasterize font if available)
         * PdfDecoder.TEXTSTRINGPRINT (print as text and not raster - fastest option)
         * PdfDecoder.NOTEXTPRINT (default - highest quality)
         */
        decoder.setTextPrint(params.intValue);
        
        /**
         * If you are printing PDFs using JPedal in your custom code, you may
         * find pages missing because JPedal does not know about these
         * additional pages - This method allows you to tell JPedal you have
         * already printed certain pages (1st Parameter).
         */
        decoder.useLogicalPrintOffset(params.intValue);
        
        /**
         * Use the standard Java printing functionality.
         * Set the context into which the page is drawn (1st Parameter).
         * Set the size and orientation of the page being drawn (2nd Parameter).
         * Set the index (starting at 0) of the page to be drawn (3rd Parameter).
         * Will return either Printable.PAGE_EXISTS or Printable.NO_SUCH_PAGE.
         */
        try {
            params.intValue = decoder.print(params.graphicsValue, params.pageFormatValue, params.intValue);
        } catch (final PrinterException ex) {
              ex.printStackTrace();
        }
        
        /**
         * Get a high resolution image of the page at page number intValue.
         */
        try {
            params.bufferedImageValue = decoder.getPageAsHiRes(params.intValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Get a high resolution image of the page at page number intValue (1st Parameter).
         * Override the static settings in modifyJPedalParameters for this instance only using mapValue (2nd Parameter).
         */
        try {
            params.bufferedImageValue = decoder.getPageAsHiRes(params.intValue, params.mapValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Get a high resolution image of the page at page number intValue (1st Parameter).
         * Override the static settings in modifyJPedalParameters for this instance only using mapValue (2nd Parameter).
         * Set if image is transparent or not using booleanValue (3rd Parameter).
         */
        try {
            params.bufferedImageValue = decoder.getPageAsHiRes(params.intValue, params.mapValue, params.booleanValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Get a high resolution image of the page at page number intValue (1st Parameter).
         * Set if image is transparent or not using booleanValue (2nd Parameter).
         */
        try {
            params.bufferedImageValue = decoder.getPageAsHiRes(params.intValue, params.booleanValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Get an image of the page at page number intValue.
         */
        try {
            params.bufferedImageValue = decoder.getPageAsImage(params.intValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Get an image of the page at intValue with transparency set.
         */
        try {
            params.bufferedImageValue = decoder.getPageAsTransparentImage(params.intValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Put page onto Graphics2D object.
         * We do not recommend using this method unless you explicitly need access to direct Graphics2D objects.
         * Please use getPageAsImage/HiRes methods.
         * Boolean Parameter should be true unless you are using it on a server (4th Parameter).
         */
        try {
            decoder.renderPageOntoGraphics2D(params.floatValue, params.intValue, params.graphics2DValue, params.booleanValue);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        
        /**
         * Get the upscale factor applied to the last generated High Resolution image.
         * If no upscaling applied a negative value will be returned and should be ignored.
         */
        params.floatValue = decoder.getHiResUpscaleFactor();
        
        /**
         * Clear objects to reclaim memory once written out.
         * Set wether image data should be cleared as well with boolenValue.
         */
        decoder.flushObjectValues(params.booleanValue);
        
        /**
         * Get the data for the images.
         */
        params.pdfImageDataValue = decoder.getPdfImageData();
        
        //<start-adobe>
        
        /**
         * Get the data for the background images.
         */
        params.pdfImageDataValue = decoder.getPdfBackgroundImageData();
        
        //<end-adobe>
        
        /**
         * Set render mode for onscreen display (ie RENDERTEXT,RENDERIMAGES - add together to combine).
         * Only required if you do not wish to show all objects on screen (default is all).
         */
        decoder.setRenderMode(params.intValue);
        
        /**
         * Set extraction mode telling JPedal what to extract (ie TEXT,RAWIMAGES,FINALIMAGES - add together to combine).
         * See org.jpedal.examples for specific extraction examples.
         */
        decoder.setExtractionMode(params.intValue);
        
        /**
         * Alter certain values in JPdeal such as Color.
         * All Color and text highlighting values are static and common across the JVM.
         */
        try {
            decoder.modifyNonstaticJPedalParameters(params.mapValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Alter certain values in JPedal such as Color.
         *
         * If you are using getPageAsHiRes() AFTER passing additional parameters into JPedal using the static method
         * PdfDecoder.modifyJPedalParameters(), getPageAsHiRes() wont necessarily be thread safe.
         * If you want to use getPageAsHiRes() and pass in additional parameters in a thread safe manner, please use the method
         * getPageAsHiRes(int pageIndex, Map params) or getPageAsHiRes(int pageIndex, Map params, boolean isTransparent) and
         * pass the additional parameters in directly to the getPageAsHiRes() method without calling PdfDecoder.modifyJPedalParameters() first.
         *
         * Please see http://files.idrsolutions.com/samplecode/org/jpedal/examples/images/ConvertPagesToHiResImages.java.html for example usage.
         * All Color and text highlighting values except page colour are static and common across the JVM.
         */
        try {
            PdfDecoder.modifyJPedalParameters(params.mapValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Get file metadata.
         */
        params.pdfFileInformationValue = decoder.getFileInformationData();
        
        /**
         * Get handle on PDFFactory which adjusts display size so it matches the size in Acrobat.
         */
        params.dPIFactoryValue = decoder.getDPIFactory();
        
        /**
         * Initialise panel and set size to fit PDF page with rotation set to the default.
         * To keep existing scaling setting set scaling value to -1.
         */
        decoder.setPageParameters(params.floatValue, params.intValue);
        
        /**
         * Initialise panel and set size to fit PDF page with rotation set to the default.
         * To keep existing scaling setting set scaling value to -1.
         * Set rotation to draw page with intValue (3rd Parameter).
         */
        decoder.setPageParameters(params.floatValue, params.intValue, params.intValue);
        
        /**
         * Decode a page (1st Parameter).
         * Page must be between 1 and PdfDecoder.getPageCount().
         * Will kill off if already running.
         * returns a negative page if trying to open a linearized page that is not yet available.
         */
        try {
            decoder.decodePage(params.intValue);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        
        /**
         * See if page is available in Linearized mode.
         * All pages are available in non-linearized mode.
         */
        params.booleanValue = decoder.isPageAvailable(params.intValue);
        
        /**
         * Store additional content to be displayed over a page when printing.
         * Set the page that will be stored (1st Parameter).
         * Set the overlay type that will be stored for printing (2nd Parameter).
         * Set the colors that will be stored for printing (3rd Parameter).
         * Set any objects that will be stored for printing (4th Parameter).
         */
        try {
            decoder.printAdditionalObjectsOverPage(params.intValue, params.intArrayValue, params.colorArrayValue, params.objectArrayValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Store additional content to be displayed over all pages when printing.
         * Set the overlay type that will be stored for printing (1st Parameter).
         * Set the colors that will be stored for printing (2nd Parameter).
         * Set any objects that will be stored for printing (3rd Parameter).
         */
        try {
            decoder.printAdditionalObjectsOverAllPages(params.intArrayValue, params.colorArrayValue, params.objectArrayValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Store additional content to be displayed over a page.
         * Set the page on which to draw the graphical content (1st Parameter).
         * Set the overlay type that will be used (2nd Parameter).
         * Set the colors that will be used (3rd Parameter).
         * Set any objects that will be drawn (4th Parameter).
         * Additional calls will overwrite current settings.
         * ONLY works in SingleDisplay mode.
         */
        try {
            decoder.drawAdditionalObjectsOverPage(params.intValue, params.intArrayValue, params.colorArrayValue, params.objectArrayValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Remove all additional display only, graphical content from the page.
         * ONLY works in SingleDisplay mode.
         */
        try {
            decoder.flushAdditionalObjectsOnPage(params.intValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Check if High Resolutions images will be used for the display.
         */
        params.booleanValue = decoder.isHiResScreenDisplay();
        
        /**
         * Use High Resolution images for a higher quality display.
         * Downside is it is slower and uses more memory (default is false).
         *
         * Not implemented in OS version.
         */
        decoder.useHiResScreenDisplay(params.booleanValue);
        
        /**
         * Get the page count of the current PDF file.
         */
        params.intValue = decoder.getPageCount();
        
        /**
         * Check if the current PDF file is encrypted (true) or not (false).
         * If file is encrypted and not viewable (isFileViewable() is false) - a user specified password is needed.
         */
        params.booleanValue = decoder.isEncrypted();
        
        /**
         * Check if an encryption password has been supplied or if a certificate has been set (true) or not (false).
         */
        params.booleanValue = decoder.isPasswordSupplied();
        
        /**
         * Checks if the file can be viewed (true).
         * If not (false), a password needs to be provided.
         */
        params.booleanValue = decoder.isFileViewable();
        
        /**
         * Checks if content can be extracted (true) or not (false).
         */
        params.booleanValue = decoder.isExtractionAllowed();
        
        /**
         * Set a password for encryption.
         * No seperate call is needed for user or owner as it will resolve if
         * either calls verifyAccess() from 2.74 onwards.
         */
        try {
            decoder.setEncryptionPassword(params.stringValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open a byte stream containing the PDF file and extract key info
         * from the PDF file so we can decode any pages.
         * Does not actually decode the pages themselves.
         *
         * By default files over 16384 bytes are cached to disk.
         * This can be altered by setting PdfFileReader.alwaysCacheInMemory to a maximum size or -1 to always keep in memory.
         */
        try {
            decoder.openPdfArray(params.byteArrayValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open file (1st Parameter) using Certificate (2nd Parameter) and key (3rd Parameter).
         */
        try {
            decoder.openPdfFile(params.stringValue, params.certificateValue, params.privateKeyValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file (1st Parameter) and extract key info from the PDF
         * file so we can decode any pages and sets a password also (2nd Parameter).
         * Does not actually decode the pages themselves.
         * Also reads the form data.
         * You must explicitly close your stream!!
         */
        try {
            decoder.openPdfFileFromStream(params.objectValue, params.stringValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file (1st Parameter) and extract key info from the PDF
         * file so we can decode any pages.
         * Does not actually decode the pages themselves.
         * Also reads the form data.
         * You must explicitly close any open files with closePdfFile() or Java will not release all the memory.
         */
        try {
            decoder.openPdfFile(params.stringValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file (1st Parameter) and extract key info from the PDF
         * file so we can decode any pages and sets a password also (2nd Parameter).
         * Does not actually decode the pages themselves.
         * Also reads the form data.
         * You must explicitly close any open files with closePdfFile() or Java will not release all the memory.
         */
        try {
            decoder.openPdfFile(params.stringValue, params.stringValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file via URL (1st Parameter) and extract key info from
         * the PDF file so we can decode any pages.
         * Does not actually decode the pages themselves.
         * Also reads the form data - Based on an idea by Peter Jacobsen.
         *
         * You must explicitly close any open files with closePdfFile() or Java will not release all the memory.
         *
         * If booleanValue is true (2nd Parameter), method will return a true value once Linearized part is read.
         */
        try {
            params.booleanValue = decoder.openPdfFileFromURL(params.stringValue, params.booleanValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file via URL (1st Parameter) and extract key info from
         * the PDF file so we can decode any pages.
         * Does not actually decode the pages themselves.
         * Also reads the form data - Based on an idea by Peter Jacobsen.
         *
         * You must explicitly close any open files with closePdfFile() or Java will not release all the memory.
         *
         * If booleanValue is true (2nd Parameter), method will return a true value once Linearized part is read.
         *
         * A password is passed in as parameter stringValue (3rd Parameter).
         */
        try {
            params.booleanValue = decoder.openPdfFileFromURL(params.stringValue, params.booleanValue, params.stringValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file via InputStream (1st Parameter) and extract key
         * info from the PDF file so we can decode any pages.
         * Does not actually decode the pages themselves.
         * You must explicitly close any open files with closePdfFile() or Java will not release all the memory.
         *
         * If boolean supportLinearized is true (2nd Parameter), method will return a true value once the Linearized part is read.
         * (we recommend use you false unless you know exactly what you are doing)
         * IMPORTANT NOTE: If the stream does not contain enough bytes, test for Linearization may fail.
         */
        try {
            params.booleanValue = decoder.openPdfFileFromInputStream(params.inputStreamValue, params.booleanValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file via InputStream (1st Parameter) using a provided
         * password (2nd Parameter) and extract key info from the PDF file so we
         * can decode any pages.
         * Does not actually decode the pages themselves.
         * You must explicitly close any open files with closePdfFile() or Java will not release all the memory.
         *
         * If boolean supportLinearized is true (3rd Parameter), method will return a true value once the Linearized part is read.
         * (we recommend use you false unless you know exactly what you are doing)
         * IMPORTANT NOTE: If the stream does not contain enough bytes, test for Linearization may fail.
         */
        try {
            params.booleanValue = decoder.openPdfFileFromInputStream(params.inputStreamValue, params.booleanValue, params.stringValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Checks if text extraction is set to XML or pure text.
         */
        params.booleanValue = decoder.isXMLExtraction();
        
        /**
         * XML extraction is the default - pure text extraction is much faster.
         */
        decoder.useTextExtraction();
        
        /**
         * XML extraction is the default - pure text extraction is much faster.
         */
        decoder.useXMLExtraction();
        
        /**
         * Remove all displayed objects for JPanel display (wipes current page).
         */
        decoder.clearScreen();
        
        /**
         * Allows user to cache large objects to disk to avoid memory issues.
         *
         * Setting minimum size in bytes (of uncompressed stream) above certain objects
         * will ensure that they will be stored on disk if possible.
         * (default is -1 bytes which is all objects stored in memory)
         * Must be set before file opened.
         */
        decoder.setStreamCacheSize(params.intValue);
        
        /**
         * Checks if embedded fonts are present on the page just decoded.
         */
        params.booleanValue = decoder.hasEmbeddedFonts();
        
        /**
         * Checks if embedded fonts are contained and used within the whole PDF document.
         */
//        try {
//            params.booleanValue = decoder.PDFContainsEmbeddedFonts();
//        } catch (Exception ex) {
//            
//        }
        
        /**
         * Find out the page number of a page by providing a string reference for PDF the object.
         */
        params.intValue = decoder.getPageFromObjectRef(params.stringValue);
        
        /**
         * Get a list of the fonts used on the current page that are decoded or null.
         * Can be of type PdfDictionary.Font or PdfDictionary.Image.
         */
        params.stringValue = decoder.getInfo(params.intValue);
        
        /**
         * Get access to the Forms renderer object if needed.
         */
        params.acroRendererValue = decoder.getFormRenderer();
        
        /**
         * Check if page reported any errors while printing (true) or not (false).
         * Log can be found with getPageFailureMessage().
         */
        params.booleanValue = decoder.isPageSuccessful();
        
        /**
         * Get any errors or other messages while calling decodePage().
         * If the string length is zero there were no problems.
         */
        params.stringValue = decoder.getPageDecodeReport();
        
        /**
         * Return String with all error messages from the most different printing.
         */
        params.stringValue = decoder.getPageFailureMessage();
        
        /**
         * If running in GUI mode it will extract a section of rendered page as a
         * BufferedImage with the PDF coordinates given.
         * Set useHiResScreenDisplay(param.booleanValue) to true if you wish to use a High Resolutions image.
         */
        params.bufferedImageValue = decoder.getSelectedRectangleOnscreen(params.floatValue, params.floatValue, params.floatValue, params.floatValue, params.floatValue);
        
        /**
         * Get object which provides access to file images and name.
         */
        params.objectStoreValue = decoder.getObjectStore();
        
        /**
         * Get decoder options as an object for the cases where a value is needed externally and can't be static.
         */
        params.decoderOptionsValue = decoder.getDecoderOptions();
        
        //<start-adobe>
        
        /**
         * Get an object containing the grouped text of the last decoded page.
         * If no page has been decoded, a Runtime exception is thrown to warn the user.
         *
         * Please see org.jpedal.examples.text for example code.
         */
        try {
            params.pdfGroupingAlgorithmsValue = decoder.getGroupingObject();
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Get an object containing grouped text from background groupings.
         *
         * Please see org.jpedal.examples.text for example code.
         */
        params.pdfGroupingAlgorithmsValue = decoder.getBackgroundGroupingObject();
        
        //<end-adobe>
        
        /**
         * Get PDF version of the file.
         */
        params.stringValue = decoder.getPDFVersion();
        
        //<start-adobe>
        
        /**
         * Reset the page for non-PDF files.
         */
        decoder.resetForNonPDFPage(params.intValue);
        
        /**
         * Set view mode used for the panel and redraw in the new mode.
         * The different view modes are SINGLE_PAGE,CONTINUOUS,FACING andCONTINUOUS_FACING.
         * There is a delay so that scrolling can stop before drawing the background page can start.
         * Multipage views are not implemented in the OS releases.
         */
        decoder.setDisplayView(params.intValue, params.intValue);
        
        //<end-adobe>
        
        /**
         * Check to see if all images have been processed.
         * Used to check if a problem is suspected with some images.
         */
        params.booleanValue = decoder.hasAllImages();
        
        /**
         * Check the state of flags in class org.jpedal.parser.DecoderStatus.
         */
        params.booleanValue = decoder.getPageDecodeStatus(params.intValue);
        
        /**
         * Get page statuses.
         * (flags in class org.jpedal.parser.DecoderStatus)
         */
        params.stringValue = decoder.getPageDecodeStatusReport(params.intValue);
        
        /**
         * Set print mode to autorotate and center (true) or not (false).
         * (Matches Abodes Auto Print and rotate output.
         */
        decoder.setPrintAutoRotateAndCenter(params.booleanValue);
        
        /**
         * Set printout to print only the visible area in the viewer (true) or not (false).
         */
        decoder.setPrintCurrentView(params.booleanValue);
        
        /**
         * Get access to the PDF file.
         */
        params.pdfObjectReaderValue = decoder.getIO();
        
        /**
         * Get the name of the currently open PDF file.
         */
        params.stringValue = decoder.getFileName();
        
        /**
         * Checks if the currently open PDF file is a PDF form.
         */
        params.booleanValue = decoder.isForm();
        
        /**
         * Allow printing of different page sizes (true) or not (false).
         */
        decoder.setAllowDifferentPrintPageSizes(params.booleanValue);
        
        /**
         * Set an inset using a width (1st Parameter) and height (2nd Parameter)
         * for the display so that it will not touch the edge of the panel.
         */
        decoder.setInset(params.intValue, params.intValue);
        
        /**
         * Scroll the screen to ensure a point is visible.
         */
        decoder.ensurePointIsVisible(params.pointValue);
        
        /**
         * Set a left margin for printing odd pages (1st Parameter) and a margin
         * for even pages (2nd Parameter) (ie for duplex printing).
         */
        decoder.setPrintIndent(params.intValue, params.intValue);
        
        /**
         * Get the maximum size of the panel (not of the page).
         */
        params.dimensionValue = decoder.getMaximumSize();
        
        /**
         * Get the minimum size of the panel (not of the page).
         */
        params.dimensionValue = decoder.getMinimumSize();
        
        /**
         * Get the preferred size of the panel (not of the page).
         */
        params.dimensionValue = decoder.getPreferredSize();
        
        //<start-adobe>
        
        /**
         * Update the rectangle we draw to highlight an area -
         * See Viewer example for example code showing current usage.
         */
        decoder.updateCursorBoxOnScreen(params.rectangleValue, params.colorValue.getRGB());
        
        //<end-adobe>
        
        /**
         * Standard Java swing method to draw the page with any highlights onto the panel.
         */
        decoder.paint(params.graphicsValue);
        
        /**
         * Standard Java swing method to draw the page with any highlights onto the panel.
         */
        decoder.paintComponent(params.graphicsValue);
        
        /**
         * Get the width of the PDF page.
         *
         * It now includes any scaling factor you have set.
         */
        params.intValue = decoder.getPDFWidth();
        
        /**
         * Get the height of the PDF page.
         *
         * It now includes any scaling factor you have set.
         */
        params.intValue = decoder.getPDFHeight();
        
        /**
         * Set border which will be displayed on screen and when printing.
         * Setting a new value will enable screen and border painting.
         * Disable with disableBorderForPrinting().
         */
        decoder.setPDFBorder(params.borderValue);
        
        /**
         * Enables (true) or disables (false) hardware acceleration of screen rendering (default is on).
         */
        decoder.setHardwareAccelerationforScreen(params.booleanValue);
        
        //<start-adobe>
        
        /**
         * Get the amount the window scrolls by when scrolling (default is 10).
         */
        params.intValue = decoder.getScrollInterval();
        
        /**
         * Set the amount the window scrolls by when scrolling.
         */
        decoder.setScrollInterval(params.intValue);
        
        //<end-adobe>
        
        /**
         * Get the view mode used - ie SINGLE_PAGE,CONTINUOUS,FACING,CONTINUOUS_FACING.
         * Not implemented in OS versions.
         */
        params.intValue = decoder.getDisplayView();
        
        /**
         * Set page scaling mode to use when printing.
         * Default setting is  PAGE_SCALING_REDUCE_TO_PRINTER_MARGINS.
         * All values start "PAGE_SCALING"...
         */
        decoder.setPrintPageScalingMode(params.intValue);
        
        /**
         * Set to print using the size of the PDF page.
         */
        decoder.setUsePDFPaperSize(params.booleanValue);
        
        /**
         * Get the height value for the currently set inset.
         * Set inset using setInset(width, height).
         */
        params.intValue = decoder.getInsetH();
        
        /**
         * Get the width value for the currently set inset.
         * Set inset using setInset(width, height).
         */
        params.intValue = decoder.getInsetW();
        
        /**
         * Specify if the PDF page should be rotated to best fit the printed page imagable area (true) or not (false).
         */
        decoder.setPrintAutoRotate(params.booleanValue);
        
        /**
         * Set printing page range (inclusive).
         * See SilentPrint.java and Viewer.java for sample print code.
         * An invalid range will throw a PdfException.
         * Can take values such as 'new PageRanges("3,5,7-9,15")';
         */
        try {
            decoder.setPagePrintRange(params.setOfIntegerSyntaxValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Select whether to print only odd or even pages.
         * Printing modes are defined in org.jpedal.objects.PrinterOptions.java.
         * Values can be added together to combine them.
         */
        decoder.setPrintPageMode(params.intValue);
        
        /**
         * Ask JPedal to stop printing a page.
         */
        decoder.stopPrinting();
        
        /**
         * Check which page is currently being printed or -1 if it has finished printing.
         */
        params.intValue = decoder.getCurrentPrintPage();
        
        /**
         * Reset the page that is currently being printed to print page 0.
         */
        decoder.resetCurrentPrintPage();
        
        ////////////////////////////////////////////////////////////////////////
        /*
         *
         * AVOID USING THESE METHODS AS THEY ARE NOT PART OF THE API!!
         *
         */
        ////////////////////////////////////////////////////////////////////////
        
        //<start-adobe>
        
        params.documentValue = decoder.getMarkedContent();
        
        //<end-adobe>
        
        params.displayValue = decoder.getPages();
        
        //<start-adobe>
        
        decoder.resetViewableArea();
        
        try {
            params.affineTransformValue = decoder.getPages().setViewableArea(params.rectangleValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        params.pdfDataValue = decoder.getPdfBackgroundData();
        
        //<end-adobe>
        
        try {
            params.pdfDataValue = decoder.getPdfData();
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        decoder.setExtractionMode(params.intValue, params.floatValue);
        
        //<start-adobe>
        
        decoder.setStatusBarObject(params.statusBarValue);
        
        //<end-adobe>
        
        decoder.waitForDecodingToFinish();
        
        decoder.updatePageNumberDisplayed(params.intValue);
        
        params.dynamicVectorRendererValue = decoder.getDynamicRenderer();
        
        params.dynamicVectorRendererValue = decoder.getDynamicRenderer(params.booleanValue);
        
        decoder.setPDFCursor(params.cursorValue);
        
        decoder.setDefaultCursor(params.cursorValue);
        
        decoder.setCursor(params.cursorValue);
        
        try {
            decoder.decodePageInBackground(params.intValue);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        
        params.objectValue = decoder.getJPedalObject(params.intValue);
        
        decoder.setPageMode(params.intValue);
        
        params.javascriptValue = decoder.getJavaScript();
        
        decoder.setObjectStore(params.objectStoreValue);
        
        decoder.addExternalHandler(params.objectValue, params.intValue);
        
        params.objectValue = decoder.getExternalHandler(params.intValue);
        
        params.printableValue = decoder.getPrintable(params.intValue);
        
        params.textLinesValue = decoder.getTextLines();
        
        decoder.setUserOffsets(params.intValue, params.intValue, params.intValue);
        
        params.pointValue = decoder.getUserOffsets(params.intValue);
        
        params.floatValue = decoder.getScaling();
        
        params.rectangleValue = decoder.getPages().getCursorBoxOnScreenAsArray();
        
        params.intValue = decoder.getNumberOfPages();
        
        params.pageFormatValue = decoder.getPageFormat(params.intValue);
        
        params.pageFormatValue = decoder.getUserSetPageFormat(params.intValue);
        
        decoder.setCenterOnScaling(params.booleanValue);
        
        decoder.setPageFormat(params.intValue, params.pageFormatValue);
        
        decoder.setPageFormat(params.pageFormatValue);
        
    }
    
}