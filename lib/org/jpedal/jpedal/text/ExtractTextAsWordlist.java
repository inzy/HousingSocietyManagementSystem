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
 * ExtractTextAsWordlist.java
 * ---------------
 */

package org.jpedal.examples.text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.jpedal.PdfDecoderServer;
import org.jpedal.PdfDecoderInt;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.fonts.FontMappings;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;

/**
 *
 * <h2><b>Sample Code Showing how JPedal Library can be used with
 * PDF to Extract Text from a Specified Rectangle as a Set of Words.</b></h2>
 *
 * <p>This example is based on extractTextInRectangle.java</p>
 *
 * <p>It can run from jar directly using the command:
 *
 * <br><b>java -cp libraries_needed org/jpedal/examples/text/ExtractTextAsWordlist inputValue</b></p>
 *
 * <p>Where inputValue is The PDF filename (including the path if needed) or a directory containing PDF files.
 * <br>If it contains spaces it must be enclosed by double quotes (ie "C:/Path with spaces/").</p>
 *
 * <p>These can then be entered into an index engine such as Lucene or used internally.</p>
 * 
 * <p><a href="http://www.idrsolutions.com/how-to-extract-text-from-pdf-files/">See our Support Pages for more information on Text Extraction.</a></p>
 */
public class ExtractTextAsWordlist {

    /**flag to show if we print messages*/
    public static boolean outputMessages=true;

    /**word count - used for testing*/
    private int wordsExtracted;

    /**output where we put files*/
    private String user_dir = System.getProperty("user.dir");

    /**correct separator for OS */
    final String separator = System.getProperty("file.separator");

    /**the decoder object which decodes the pdf and returns a data object*/
    PdfDecoderInt decodePdf;

    /**flag to show if file or byte array*/
    private boolean isFile=true;

    /**byte array*/
    private byte[] byteArray;

    /**used in our regression tests to limit to first 10 pages*/
    public static boolean isTest;

    /**
     * Example method to open a file and extract the raw text.
     * 
     * @param file_name is of type String
     */
    public ExtractTextAsWordlist(String file_name) {

        if(outputMessages) {
            System.out.println("processing " + file_name);
        }

        //check output dir has separator
        if (!user_dir.endsWith(separator)) {
            user_dir += separator;
        }

        /**
         * if file name ends pdf, do the file otherwise
         * do every pdf file in the directory. We already know file or
         * directory exists so no need to check that, but we do need to
         * check its a directory
         */
        if (file_name.toLowerCase().endsWith(".pdf")) {
            decodeFile(file_name);
        } else {

            /**
             * get list of files and check directory
             */

            String[] files = null;
            final File inputFiles;

            /**make sure name ends with a deliminator for correct path later*/
            if (!file_name.endsWith(separator)) {
                file_name += separator;
            }

            try {
                inputFiles = new File(file_name);

                if (!inputFiles.isDirectory()) {
                    System.err.println(
                            file_name + " is not a directory. Exiting program");
                }
                files = inputFiles.list();
            } catch (final Exception ee) {
                LogWriter.writeLog(
                        "Exception trying to access file " + ee.getMessage());
            }

            /**now work through all pdf files*/
            for (final String file : files) {

                if (file.toLowerCase().endsWith(".pdf")) {
                    if (outputMessages) {
                        System.out.println(file_name + file);
                    }

                    decodeFile(file_name + file);
                }
            }
        }
    }

    
    
     /**
      * Example method to open a file and extract the raw text.
      * 
      * @param array is of type byte[]
      */
    public ExtractTextAsWordlist(final byte[] array) {

        if(outputMessages) {
            System.out.println("processing byte array");
        }

        //check output dir has separator
        if (!user_dir.endsWith(separator)) {
            user_dir += separator;
        }


        //set values
        this.byteArray=array;
        isFile=false;

        //routine will open from array (is otherwise identical)
        decodeFile("byteArray");

    }

    /**
     * routine to decode a file
     */
    private void decodeFile(final String file_name) {

        /**get just the name of the file without
         * the path to use as a sub-directory or .pdf
         */
        String name = "demo"; //set a default just in case

        final int pointer = file_name.lastIndexOf(separator);

        if (pointer != -1) {
            name = file_name.substring(pointer + 1, file_name.length() - 4);
        }

        /**
         * create output dir for text
         */
        final String outputDir=user_dir + "text" + separator + name + separator;

        /**debugging code to create a log*
         LogWriter.setupLogFile(true,0,"","",false);
         LogWriter.log_name =  "log.txt";
         /***/

        //PdfDecoder returns a PdfException if there is a problem
        try {
            decodePdf = new PdfDecoderServer(true);

            //incase fonts not embedded
            FontMappings.setFontReplacements();

            decodePdf.setExtractionMode(PdfDecoderServer.TEXT); //extract just text
            PdfDecoderServer.init(true);
            //make sure widths in data CRITICAL if we want to split lines correctly!!

            /**if you do not require XML content, pure text extraction
             * is much faster.
             */
             decodePdf.useTextExtraction();
             /**/

            //always reset to use unaltered co-ords - allow use of rotated or unrotated
            // co-ordinates on pages with rotation (used to be in PdfDecoder)
            PdfGroupingAlgorithms.useUnrotatedCoords=false;

            /**
             * open the file (and read metadata including pages in  file)
             */
            if(outputMessages) {
                System.out.println("Opening file :" + file_name);
            }

            if(isFile) {
                decodePdf.openPdfFile(file_name);
            } else {
                decodePdf.openPdfArray(byteArray);
            }
        } catch (final PdfSecurityException e) {
            System.err.println("Exception " + e+" in pdf code for wordlist"+file_name);
        } catch (final PdfException e) {
            System.err.println("Exception " + e+" in pdf code for wordlist"+file_name);

        } catch (final Exception e) {
            System.err.println("Exception " + e+" in pdf code for wordlist"+file_name);
            e.printStackTrace();
        }

        /**
         * extract data from pdf (if allowed).
         */
        if(!decodePdf.isExtractionAllowed()){
            if(outputMessages) {
                System.out.println("Text extraction not allowed");
            }
        }else if (decodePdf.isEncrypted() && !decodePdf.isPasswordSupplied()) {
            if(outputMessages){
                System.out.println("Encrypted settings");
                System.out.println("Please look at Viewer for code sample to handle such files");
            }
        } else{
            //page range
            final int start = 1;
            int end = decodePdf.getPageCount();

            //limit to 1st ten pages in testing
            if((end>10)&&(isTest)) {
                end = 10;
            }

            /**
             * extract data from pdf
             */
            try {
                for (int page = start; page < end + 1; page++) { //read pages

                    //decode the page
                    decodePdf.decodePage(page);

                    /** create a grouping object to apply grouping to data*/
                    final PdfGroupingAlgorithms currentGrouping =decodePdf.getGroupingObject();

                    /**use whole page size for  demo - get data from PageData object*/
                    final PdfPageData currentPageData = decodePdf.getPdfPageData();

                    final int x1 = currentPageData.getMediaBoxX(page);
                    final int x2 = currentPageData.getMediaBoxWidth(page)+x1;

                    final int y2 = currentPageData.getMediaBoxX(page);
                    final int y1 = currentPageData.getMediaBoxHeight(page)-y2;

                    //tell user
                    if(outputMessages) {
                        System.out.println(
                                "Page " + page + " Extracting text from rectangle ("
                                        + x1
                                        + ','
                                        + y1
                                        + ' '
                                        + x2
                                        + ','
                                        + y2
                                        + ')');
                    }

                    /**Co-ordinates are x1,y1 (top left hand corner), x2,y2(bottom right) */

                    /**The call to extract the list*/
                    List words =null;

                    /**new 7th October 2003 - define punctuation*/
                    try{
                        words =currentGrouping.extractTextAsWordlist(
                                x1,
                                y1,
                                x2,
                                y2,
                                page,
                                true,"&:=()!;.,\\/\"\"\'\'");
                    } catch (final PdfException e) {
                        decodePdf.closePdfFile();
                        System.err.println("Exception= "+ e+" in "+file_name);
                    }

                    if (words == null) {
                        if(outputMessages) {
                            System.out.println("No text found");
                        }

                    } else {

                        //create a directory if it doesn't exist
                        final File output_path = new File(outputDir);
                        if (!output_path.exists()) {
                            output_path.mkdirs();
                        }

                        /**each word is stored as 5 consecutive values (word,x1,y1,x2,y2)*/
                        final int wordCount=words.size()/5;

                        //update our count
                        wordsExtracted += wordCount;

                        /**just a simple message in this example*/
                        if(outputMessages) {
                            System.out.println("Page contains " + wordCount + " words.");
                        }

                        /**
                         * output the data
                         */
                        if(outputMessages) {
                            System.out.println("Writing to " + outputDir + "words-" + page + ".txt");
                        }

                        final OutputStreamWriter output_stream =
                                new OutputStreamWriter(
                                        new FileOutputStream(outputDir + "words-"+page + ".txt"),
                                        "UTF-8");

                        final Iterator wordIterator=words.iterator();
                        while(wordIterator.hasNext()){

                            String currentWord=(String) wordIterator.next();

                            /**remove the XML formatting if present - not needed for pure text*/
                            currentWord=Strip.convertToText(currentWord, decodePdf.isXMLExtraction());

                            /**if(currentWord.indexOf(" ")!=-1){
                             System.out.println("word="+currentWord);
                             System.exit(1);
                             }*/

                            /**
                             * these co-ordinates are absolute from the bottom of the page (MediaBox)
                             * If you are extracting image (which may use crop, use need to modify as below
                             */
                            final int wx1 = (int)Float.parseFloat((String) wordIterator.next());
                            final int wy1 = (int)Float.parseFloat((String) wordIterator.next());
                            final int wx2 = (int)Float.parseFloat((String) wordIterator.next());
                            final int wy2 = (int)Float.parseFloat((String) wordIterator.next());

                            /**
                             * version so co-ordinates would match cropped page (need to subtract x,y of crop)
                             */
                            /**
                             int wx1=(int)Float.parseFloat((String) wordIterator.next())-currentPageData.getCropBoxX(page);
                             int wy1=(int)Float.parseFloat((String) wordIterator.next())-currentPageData.getCropBoxY(page);
                             int wx2=(int)Float.parseFloat((String) wordIterator.next())-currentPageData.getCropBoxX(page);
                             int wy2=(int)Float.parseFloat((String) wordIterator.next())-currentPageData.getCropBoxY(page);
                             /**/

                            /**this could be inserting into a database instead*/
                            output_stream.write(currentWord+ ',' +wx1+ ',' +wy1+ ',' +wx2+ ',' +wy2+ '\n');

                        }
                        output_stream.close();

                    }

                    //remove data once written out
                    decodePdf.flushObjectValues(false);

                }
            } catch (final Exception e) {
                decodePdf.closePdfFile();
                System.err.println("Exception "+ e+" in "+file_name);
                e.printStackTrace();
            }

            /**
             * flush data structures - not strictly required but included
             * as example
             */
            decodePdf.flushObjectValues(true); //flush any text data read

            /**tell user*/
            if(outputMessages) {
                System.out.println("Text read");
            }

        }

        /**close the pdf file*/
        decodePdf.closePdfFile();

        decodePdf=null;

    }
    //////////////////////////////////////////////////////////////////////////
    /**
     * Main routine which checks for any files passed and runs the demo.
     *
     * @param args is of type String[]
     */
    public static void main(final String[] args) {
        if(outputMessages) {
            System.out.println("Simple demo to extract text objects");
        }

        //set to default
        String file_name="";

        //check user has passed us a filename and use default if none
        if (args.length==1){

            file_name = args[0];
            if(outputMessages) {
                System.out.println("File :" + file_name);
            }
        }else{
            System.out.println("You must pass ONE parameter - a filename or directory in as a parameter");
            System.out.println("Make sure you put double quotes around the value if it has spaces");
            System.exit(1);
        }

        //check file exists
        final File pdf_file = new File(file_name);

        //if file exists, open and get number of pages
        if (!pdf_file.exists()) {
            System.out.println("File " + file_name + " not found");
        }
        new ExtractTextAsWordlist(file_name);
    }

    /**
     * Return words extracted. We use this in some tests.
     * 
     * @return int
     */
    public int getWordsExtractedCount() {
        return wordsExtracted;
    }

}
