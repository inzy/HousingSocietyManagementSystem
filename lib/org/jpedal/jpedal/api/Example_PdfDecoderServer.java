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
 * Example_PdfDecoderServer.java
 * ---------------
 */
package org.jpedal.examples.api;

import org.jpedal.PdfDecoderServer;
import org.jpedal.exception.PdfException;


@SuppressWarnings("UnusedAssignment")
public class Example_PdfDecoderServer {
    
    final ApiParams params = new ApiParams();
    
    public void Example_PdfDecoderServer(){
        
        ////////////////////////////////////////////////////////////////////////
        //constructors
        ////////////////////////////////////////////////////////////////////////
        
        /*
         * recommended usage - use this to create an object to use in most cases
         */
        PdfDecoderServer serverDecoder = new PdfDecoderServer();
        
        /*
         * Allows user to set true/false. Set to false if only using for extraction and not rendering
         */
        serverDecoder = new PdfDecoderServer(params.booleanValue);
        
        ////////////////////////////////////////////////////////////////////////
        //public methods you can use
        ////////////////////////////////////////////////////////////////////////
        
        /*
         * See if file open - may not be open if user interrupted open or problem was encountered
         */
        params.booleanValue = serverDecoder.isOpen();
        
        /*
         * Get an XML file containing information about the marked content of a pdf document
         */
        params.documentValue = serverDecoder.getMarkedContent();
        
        /*
         * Get the current page rotation (in addition to rotation in file) in degrees
         * So if user turns page by 90º, value will be 90
         */
        params.intValue = serverDecoder.getDisplayRotation();
        
        /*
         * Get the current page number
         */
        params.intValue = serverDecoder.getPageNumber();
        
        /*
         * Set the page's rotation
         * A value of 90 will set the rotation to 90º
         */
        serverDecoder.setDisplayRotation(params.intValue);
        
        /*
         * Get the page number for the last page decoded
         * Only use in SingleDisplay mode
         */
        params.intValue = serverDecoder.getlastPageDecoded();
        
        
        /*
         * Get the details on page for type (defined in org.jpedal.constants.PageInfo) or null if there are no values
         * Unrecognised key will throw a RunTime exception
         * Null will be returned if JPedal is not clear on result
         */
        params.iteratorValue = serverDecoder.getPageInfo(params.intValue);
        
        /*
         * Get an object containing the data for the outlines.
         */
        params.outlineDataValue = serverDecoder.getOutlineData();
        
        /*
         * Track if file is still loaded in background
         */
        params.booleanValue = serverDecoder.isLoadingLinearizedPDF();
        
        /*
         * Get type of alignment for the pages if they're smaller than the panel
         */
        params.intValue = serverDecoder.getPageAlignment();
        
        /*
         * Set wether width data is embedded.
         */
        PdfDecoderServer.init(params.booleanValue);
        
        /*
         * Remove all static elements (should not generally need using)
         * Only use if you know what you are doing with it
         *
         * Only call when completely finished with JPedal as they will not be reinitialised
         */
        PdfDecoderServer.disposeAllStatic();
        
        /*
         * Remove all items from memory
         * Call disposeAllStatic() if you wish to clear all static objects as well
         */
        serverDecoder.dispose();
        
        /*
         * Close the current PDF file and release all resources/delete any temporary files
         */
        serverDecoder.closePdfFile();
        
        /*
         * Show if PDF document contains an outline
         */
        params.booleanValue = serverDecoder.hasOutline();
        
        /**
         * Get a Document containing all the information on the PDF outline in an XML format.
         */
        params.documentValue = serverDecoder.getOutlineAsXML();
        
        /*
         * Get an object containing the information on the page for calculating grouping.
         * Please note: Structure of PdfPageData is not guaranteed to remain constant.
         * Please contact IDRsolutions for advice.
         */
        params.pdfPageDataValue = serverDecoder.getPdfPageData();
        
        /*
         * Get a high resolution image of the page at page number intValue
         */
        try {
            params.bufferedImageValue = serverDecoder.getPageAsHiRes(params.intValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /*
         * Get a high resolution image of the page at page number intValue (1st Parameter)
         * Override the static settings in modifyJPedalParameters for this instance only using mapValue (2nd Parameter)
         */
        try {
            params.bufferedImageValue = serverDecoder.getPageAsHiRes(params.intValue, params.mapValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /*
         * Get a high resolution image of the page at page number intValue (1st Parameter)
         * Override the static settings in modifyJPedalParameters for this instance only using mapValue (2nd Parameter)
         * Set if image is transparent or not using booleanValue (3rd Parameter)
         */
        try {
            params.bufferedImageValue = serverDecoder.getPageAsHiRes(params.intValue, params.mapValue, params.booleanValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /*
         * Get a high resolution image of the page at page number intValue (1st Parameter)
         * Set if image is transparent or not using booleanValue (2nd Parameter)
         */
        try {
            params.bufferedImageValue = serverDecoder.getPageAsHiRes(params.intValue, params.booleanValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /*
         * Get an image of the page at page number intValue
         */
        try {
            params.bufferedImageValue = serverDecoder.getPageAsImage(params.intValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /*
         * Get an image of the page at intValue with transparency set
         */
        try {
            params.bufferedImageValue = serverDecoder.getPageAsTransparentImage(params.intValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /*
         * Get the upscale factor applied to the last generated High Resolution image
         * If no upscaling applied a negative value will be returned and should be ignored
         */
        params.floatValue = serverDecoder.getHiResUpscaleFactor();
        
        /*
         * Clear objects to reclaim memory once written out
         * Set wether image data should be cleared as well with boolenValue
         */
        serverDecoder.flushObjectValues(params.booleanValue);
        
        /*
         * get data for the images
         */
        params.pdfImageDataValue = serverDecoder.getPdfImageData();
        
        /*
         * get data for the background images
         */
        params.pdfImageDataValue = serverDecoder.getPdfBackgroundImageData();
        
        /*
         * Set render mode for onscreen display (ie RENDERTEXT,RENDERIMAGES - add together to combine)
         * Only required if you do not wish to show all objects on screen (default is all)
         */
        serverDecoder.setRenderMode(params.intValue);
        
        /*
         * Set extraction mode telling JPedal what to extract (ie TEXT,RAWIMAGES,FINALIMAGES - add together to combine)
         * See org.jpedal.examples for specific extraction examples
         */
        serverDecoder.setExtractionMode(params.intValue);
        
        /*
         * Alter certain values in JPdeal such as Color
         * All Color and text highlighting values are static and common across the JVM
         */
        try {
            serverDecoder.modifyNonstaticJPedalParameters(params.mapValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Alter certain values in JPedal such as Color
         *
         * If you are using getPageAsHiRes() AFTER passing additional parameters into JPedal using the static method
         * PdfDecoder.modifyJPedalParameters(), getPageAsHiRes() wont necessarily be thread safe.
         * If you want to use getPageAsHiRes() and pass in additional parameters in a thread safe manner, please use the method
         * getPageAsHiRes(int pageIndex, Map params) or getPageAsHiRes(int pageIndex, Map params, boolean isTransparent) and
         * pass the additional parameters in directly to the getPageAsHiRes() method without calling PdfDecoder.modifyJPedalParameters() first.
         *
         * Please see http://files.idrsolutions.com/samplecode/org/jpedal/examples/images/ConvertPagesToHiResImages.java.html for example usage
         * All Color and text highlighting values except page colour are static and common across the JVM
         */
        try {
            PdfDecoderServer.modifyJPedalParameters(params.mapValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /*
         * Get file metadata
         */
        params.pdfFileInformationValue = serverDecoder.getFileInformationData();
        
        /*
         * Get handle on PDFFactory which adjusts display size so it matches the size in Acrobat
         */
        params.dPIFactoryValue = serverDecoder.getDPIFactory();
        
        /*
         * Initialise panel and set size to fit PDF page with rotation set to the default
         * To keep existing scaling setting set scaling value to -1
         */
        serverDecoder.setPageParameters(params.floatValue, params.intValue);
        
        /*
         * Initialise panel and set size to fit PDF page with rotation set to the default
         * To keep existing scaling setting set scaling value to -1
         * Set rotation to draw page with intValue (3rd Parameter)
         */
        serverDecoder.setPageParameters(params.floatValue, params.intValue, params.intValue);
        
        /*
         * Get access to the PDF file
         */
        params.pdfObjectReaderValue = serverDecoder.getIO();
        
        /**
         * Decode a page - intValue
         * Page must be between 1 and PdfDecoder.getPageCount()
         * Will kill off if already running
         * returns a negative page if trying to open a linearized page that is not yet available
         */
        try {
            serverDecoder.decodePage(params.intValue);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        
        /*
         * See if page is available in Linearized mode
         * All pages are available in non-linearized mode
         */
        params.booleanValue = serverDecoder.isPageAvailable(params.intValue);
        
        /*
         * allow user to add graphical content on top of page - for display ONLY
         * Additional calls will overwrite current settings
         *
         * ONLY works in SingleDisplay mode
         */
        try {
            serverDecoder.drawAdditionalObjectsOverPage(params.intValue, params.intArrayValue, params.colorArrayValue, params.objectArrayValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /*
         * Remove all additional graphical content from the page that are only for display
         *
         * ONLY works in SingleDisplay mode
         */
        try {
            serverDecoder.flushAdditionalObjectsOnPage(params.intValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /*
         * Check if High Resolutions images will be used for the display
         */
        params.booleanValue = serverDecoder.isHiResScreenDisplay();
        
        /*
         * Use High Resolution images for a higher quality display
         * Downside is it is slower and uses more memory (default is false)
         *
         * Not implemented in OS version
         */
        serverDecoder.useHiResScreenDisplay(params.booleanValue);
        
        /*
         * Get the page count of the current PDF file
         */
        params.intValue = serverDecoder.getPageCount();
        
        /*
         * Check if the current PDF file is encrypted
         * If file is encrypted and not viewable (isFileViewable() is false) - a user specified password is needed
         */
        params.booleanValue = serverDecoder.isEncrypted();
        
        /*
         * Show if encryption password has been supplied or if a certificate has been set
         */
        params.booleanValue = serverDecoder.isPasswordSupplied();
        
        /*
         * Checks if the file can be viewed
         * If not, a password needs to be provided
         */
        params.booleanValue = serverDecoder.isFileViewable();
        
        /*
         * Checks if content can be extracted
         */
        params.booleanValue = serverDecoder.isExtractionAllowed();
        
        /*
         * Set a password for encryption.
         */
        try {
            serverDecoder.setEncryptionPassword(params.stringValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open a byte stream containing the PDF file and extract key info
         * from the PDF file so we can decode any pages.
         * Does not actually decode the pages themselves.
         *
         * By default files over 16384 bytes are cached to disk
         * This can be altered by setting PdfFileReader.alwaysCacheInMemory to a maximum size or -1 to always keep in memory
         */
        try {
            serverDecoder.openPdfArray(params.byteArrayValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /*
         * Open file using Certificate and key.
         */
        try {
            serverDecoder.openPdfFile(params.stringValue, params.certificateValue, params.privateKeyValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file (1st Parameter) and extract key info from the PDF file so we can
         * decode any pages and sets a password also (2nd Parameter).
         * Does not actually decode the pages themselves.
         * Also reads the form data.
         * You must explicitly close your stream!!
         */
        try {
            serverDecoder.openPdfFileFromStream(params.objectValue, params.stringValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file (1st Parameter) and extract key info from the PDF file so we can
         * decode any pages.
         * Does not actually decode the pages themselves.
         * Also reads the form data.
         * You must explicitly close any open files with closePdfFile() or Java will not release all the memory
         */
        try {
            serverDecoder.openPdfFile(params.stringValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file (1st Parameter) and extract key info from the PDF file so we can
         * decode any pages and sets password also (2nd Parameter).
         * Does not actually decode the pages themselves.
         * Also reads the form data.
         * You must explicitly close any open files with closePdfFile() or Java will not release all the memory
         */
        try {
            serverDecoder.openPdfFile(params.stringValue, params.stringValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file via URL (1st Parameter) and extract key info from the PDF file so we
         * can decode any pages.
         * Does not actually decode the pages themselves
         * Also reads the form data - Based on an idea by Peter Jacobsen
         *
         * You must explicitly close any open files with closePdfFile() or Java will not release all the memory
         *
         * If booleanValue is true (2nd Parameter), method will return a true value once Linearized part is read
         */
        try {
            params.booleanValue = serverDecoder.openPdfFileFromURL(params.stringValue, params.booleanValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file via URL (1st Parameter) and extract key info from the PDF file so we
         * can decode any pages.
         * Does not actually decode the pages themselves.
         * Also reads the form data - Based on an idea by Peter Jacobsen.
         *
         * You must explicitly close any open files with closePdfFile() or Java will not release all the memory.
         *
         * If booleanValue is true (2nd Parameter), method will return a true value once Linearized part is read.
         *
         * A password is passed in as parameter stringValue (3rd Parameter)
         */
        try {
            params.booleanValue = serverDecoder.openPdfFileFromURL(params.stringValue, params.booleanValue, params.stringValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file via InputStream (1st Parameter) and extract key info from the PDF file so we
         * can decode any pages.
         * Does not actually decode the pages themselves.
         * You must explicitly close any open files with closePdfFile() or Java will not release all the memory.
         *
         * If boolean supportLinearized is true, method will return a true value once Linearized part is read.
         * (we recommend use you false unless you know exactly what you are doing)
         * IMPORTANT NOTE: If the stream does not contain enough bytes, test for Linearization may fail.
         */
        try {
            params.booleanValue = serverDecoder.openPdfFileFromInputStream(params.inputStreamValue, params.booleanValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Open the PDF file via URL (1st Parameter) and extract key info from the PDF file so we
         * can decode any pages.
         * Does not actually decode the pages themselves.
         * Also reads the form data - Based on an idea by Peter Jacobsen.
         * You must explicitly close any open files with closePdfFile() or Java will not release all the memory.
         *
         * If boolean supportLinearized is true, method will return a true value once Linearized part is read
         */
        try {
            params.booleanValue = serverDecoder.openPdfFileFromURL(params.stringValue, params.booleanValue, params.stringValue);
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Checks if text extraction is set to XML or pure text.
         */
        params.booleanValue = serverDecoder.isXMLExtraction();
        
        /**
         * XML extraction is the default - pure text extraction is much faster.
         */
        serverDecoder.useTextExtraction();
        
        /**
         * XML extraction is the default - pure text extraction is much faster.
         */
        serverDecoder.useXMLExtraction();
        
        /**
         * Remove all displayed objects for JPanel display (wipes current page).
         */
        serverDecoder.clearScreen();
        
        /**
         * Allows user to cache large objects to disk to avoid memory issues.
         *
         * Setting minimum size in bytes (of uncompressed stream) above certain objects
         * will ensure that they will be stored on disk if possible.
         * (default is -1 bytes which is all objects stored in memory)
         * Must be set before file opened.
         */
        serverDecoder.setStreamCacheSize(params.intValue);
        
        /**
         * Checks if embedded fonts are present on the page just decoded.
         */
        params.booleanValue = serverDecoder.hasEmbeddedFonts();
        
        /**
         * Checks if embedded fonts are contained and used within the whole PDF document.
         */
//        try {
//            params.booleanValue = serverDecoder.PDFContainsEmbeddedFonts();
//        } catch (Exception ex) {
//            
//        }
        
        /**
         * Find out the page number of a page by providing a reference for PDF object.
         */
        params.intValue = serverDecoder.getPageFromObjectRef(params.stringValue);
        
        /**
         * Get a list of the fonts used on the current page that are decoded or null.
         * Can be of type PdfDictionary.Font or PdfDictionary.Image.
         */
        params.stringValue = serverDecoder.getInfo(params.intValue);
        
        /**
         * Get access to the Forms renderer object if needed.
         */
        params.acroRendererValue = serverDecoder.getFormRenderer();
        
        /**
         * Get access to Javascript object if needed.
         */
        params.javascriptValue = serverDecoder.getJavaScript();
        
        /**
         * Get any errors or other messages while calling decodePage().
         * If the string length is zero there were no problems.
         */
        params.stringValue = serverDecoder.getPageDecodeReport();
        
        /**
         * Get object which provides access to file images and name.
         */
        params.objectStoreValue = serverDecoder.getObjectStore();
        
        /**
         * Get decoder options as an object for the cases where a value is needed externally and can't be static.
         */
        params.decoderOptionsValue = serverDecoder.getDecoderOptions();
        
        /**
         * Get an object containing the grouped text of the last decoded page.
         * If no page has been decoded, a Runtime exception is thrown to warn the user.
         *
         * Please see org.jpedal.examples.text for example code.
         */
        try {
            params.pdfGroupingAlgorithmsValue = serverDecoder.getGroupingObject();
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        /**
         * Get an object containing grouped text from background groupings.
         *
         * Please see org.jpedal.examples.text for example code.
         */
        params.pdfGroupingAlgorithmsValue = serverDecoder.getBackgroundGroupingObject();
        
        /**
         * Get PDF version of the file.
         */
        params.stringValue = serverDecoder.getPDFVersion();
        
        /**
         * Check to see if all images have been processed.
         * Used to check if a problem is suspected with some images.
         */
        params.booleanValue = serverDecoder.hasAllImages();
        
        /**
         * Check the state of flags in class org.jpedal.parser.DecoderStatus.
         */
        params.booleanValue = serverDecoder.getPageDecodeStatus(params.intValue);
        
        /**
         * Get page statuses.
         * (flags in class org.jpedal.parser.DecoderStatus)
         */
        params.stringValue = serverDecoder.getPageDecodeStatusReport(params.intValue);
        
        /**
         * Get the name of the currently open PDF file.
         */
        params.stringValue = serverDecoder.getFileName();
        
        /**
         * Checks if the currently open PDF file is a PDF form.
         */
        params.booleanValue = serverDecoder.isForm();
        
        /**
         * Get TextLines object that contains information on the lines of text for highlighting.
         */
        params.textLinesValue = serverDecoder.getTextLines();
        
        /**
         * Get the width of the PDF page.
         *
         * It now includes any scaling factor you have set
         */
        params.intValue = serverDecoder.getPDFWidth();
        
        /**
         * Get the height of the PDF page.
         *
         * It now includes any scaling factor you have set
         */
        params.intValue = serverDecoder.getPDFHeight();
        
        /**
         * Get view mode used for display.
         * (SINGLE_PAGE,CONTINUOUS,FACING,CONTINUOUS_FACING)
         * Not implemented in OS versions.
         */
        params.intValue = serverDecoder.getDisplayView();
        
        /**
         * Get the current scaling value used internally.
         */
        params.floatValue = serverDecoder.getScaling();
        
        ////////////////////////////////////////////////////////////////////////
        /*
         *
         * AVOID USING THESE METHODS AS THEY ARE NOT PART OF THE API!!
         *
         */
        ////////////////////////////////////////////////////////////////////////
        
        params.displayValue = serverDecoder.getPages();
        
        serverDecoder.resetViewableArea();
        
        params.pdfDataValue = serverDecoder.getPdfBackgroundData();
        
        try {
            params.pdfDataValue = serverDecoder.getPdfData();
        } catch (final PdfException ex) {
            ex.printStackTrace();
        }
        
        serverDecoder.setExtractionMode(params.intValue, params.floatValue);
        
        serverDecoder.waitForDecodingToFinish();
        
        params.dynamicVectorRendererValue = serverDecoder.getDynamicRenderer();
        
        try {
            serverDecoder.decodePageInBackground(params.intValue);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        
        params.objectValue = serverDecoder.getJPedalObject(params.intValue);
        
        serverDecoder.setPageMode(params.intValue);
        
        serverDecoder.setObjectStore(params.objectStoreValue);
        
        serverDecoder.addExternalHandler(params.objectValue, params.intValue);
        
        params.objectValue = serverDecoder.getExternalHandler(params.intValue);
        
    }
    
}