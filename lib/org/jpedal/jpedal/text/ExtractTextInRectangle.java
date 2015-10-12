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
 * ExtractTextInRectangle.java
 * ---------------
 */

package org.jpedal.examples.text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.fonts.FontMappings;
import org.jpedal.grouping.PdfGroupingAlgorithms;

import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;

/**
 * <h2><b>Sample Code Showing How JPedal Library can be used with
 * PDF to Extract Text from a Specified Rectangle.</b></h2>
 * 
 * <p><b>Debugging tip: Set verbose=true in LogWriter to see what is going on.</b></p>
 *
 * <p>It can run from jar directly using the command:
 *
 * <br><b>java -cp libraries_needed org/jpedal/examples/text/ExtractTextInRectangle inputValues</b></p>
 *
 * <p>Where inputValues is between one and five space delimited input values.</p>
 * <ul>
 * <li>First value:	The PDF filename (including the path if needed) or a directory containing PDF files. If it contains spaces it must be enclosed by double quotes (ie "C:/Path with spaces/").</li>
 * <li>Remaining values:	If you wish to extract PDF text from a specified area of the screen, enter the PDF co-ordinates of the bounding rectangle in the order x1 y1 x2 y2.</li>
 * </ul>
 * <p>By default, the PDF text will be extracted as plain text. You can include XML tagging by running the example with the command line paramter -Dxml=true
 * and the are additional options in the source code which can be altered.</p>
 * 
 * <p><a href="http://www.idrsolutions.com/how-to-extract-text-from-pdf-files/">See our Support Pages for more information on Text Extraction.</a></p>
 *
 */
public class ExtractTextInRectangle {

    /**used as part of IDRsolutions tests to limit pages to first 10*/
    public static boolean isTest;

    /**text extracted by call*/
    protected String text;

    /**use to control if XML extraqction*/
    private static boolean useXMLExtraction;

    /**use to control if we save to file*/
    protected static final boolean writeToFile=true;

    /**output where we put files*/
    protected String user_dir = System.getProperty("user.dir");

    /**flag to show if we display messages*/
    public static boolean showMessages=true;

    /**correct separator for OS */
    protected final String separator = System.getProperty("file.separator");

    /**the decoder object which decodes the pdf and returns a data object*/
    protected PdfDecoderInt decodePdf;

    /**location output files written to*/
    protected String outputDir = "";

    /**user-supplied co-ords*/
    protected static int defX1=-1,defX2,defY1,defY2;

    /**sample file which can be setup - substitute your own.
     * If a directory is given, all the files in the directory will be processed*/
    private static final String testFile = "/mnt/shared/sample.pdf";

    /**
     * Example method to open a file and extract the raw text.
     * 
     * @param file_name is of type String
     */
    public ExtractTextInRectangle(String file_name) {

//		get any user set dpi
        final String xmlFlag=System.getProperty("xml");
        if(xmlFlag!=null){
            ExtractTextInRectangle.useXMLExtraction=true;
        }

        //check output dir has separator
        if (!user_dir.endsWith(separator)) {
            user_dir += separator;
        }

        //create a directory if it doesn't exist
//		File output_path = new File(outputDir);
//		if (output_path.exists() == false)
//			output_path.mkdirs();

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
            final long fileCount = files.length;

            for (int i = 0; i < fileCount; i++) {
                if(showMessages) {
                    System.out.println(i + "/ " + fileCount + ' ' + files[i]);
                }

                if (files[i].toLowerCase().endsWith(".pdf")) {
                    if(showMessages) {
                        System.out.println(file_name + files[i]);
                    }

                    decodeFile(file_name + files[i]);
                }
            }
        }
    }

    /**
     * Routine to decode a file.
     *
     * @param file_name is of type String
     */
    protected void decodeFile(final String file_name) {

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
        outputDir = user_dir + "text" + separator + name + separator;


        /**debugging code to create a log
         LogWriter.setupLogFile(true,0,"","v",false);
         LogWriter.log_name =  "/mnt/shared/log.txt";
         */

        //PdfDecoder returns a PdfException if there is a problem
        try {
            decodePdf = new PdfDecoderServer(false);

            FontMappings.setFontReplacements();

            //this uses faster extraction and gives text, not XML


            if(isTest || useXMLExtraction){
                //;
            }else {
                decodePdf.useTextExtraction();
            }

            decodePdf.setExtractionMode(PdfDecoderServer.TEXT); //extract just text
            //decodePdf.setExtractionMode(PdfDecoder.TEXT+PdfDecoder.TEXTCOLOR); //extract just text including color infomation

            PdfDecoderServer.init(true);
            //make sure widths in data CRITICAL if we want to split lines correctly!!

            /**
             * open the file (and read metadata including pages in  file)
             */
            if(showMessages) {
                System.out.println("Opening file :" + file_name);
            }
            decodePdf.openPdfFile(file_name);

        } catch (final PdfSecurityException se) {
            System.err.println("Security Exception " + se + " in pdf code for text extraction on file "+decodePdf.getObjectStore().getCurrentFilename());
            //e.printStackTrace();
        } catch (final PdfException se) {
            System.err.println("Pdf Exception " + se + " in pdf code for text extraction on file "+decodePdf.getObjectStore().getCurrentFilename());
            //e.printStackTrace();
        } catch (final Exception e) {
            System.err.println("Exception " + e + " in pdf code for text extraction on file "+decodePdf.getObjectStore().getCurrentFilename());
            e.printStackTrace();
        }

        /**
         * extract data from pdf (if allowed).
         */
        if(!decodePdf.isExtractionAllowed()){
            if(showMessages) {
                System.out.println("Text extraction not allowed");
            }
        }else if (decodePdf.isEncrypted() && !decodePdf.isPasswordSupplied()) {
            if(showMessages){
                System.out.println("Encrypted settings");
                System.out.println(
                        "Please look at Viewer for code sample to handle such files");
                System.out.println("Or get support/consultancy");
            }
        } else {
            //page range
            final int start = 1;
            int end = decodePdf.getPageCount();

            //limit to 1st ten pages in testing
            if(end>10 && isTest) {
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

                    final int x1;
                    final int y1;
                    final int x2;
                    final int y2;

                    if(defX1==-1){
                        x1 = currentPageData.getMediaBoxX(page);
                        x2 = currentPageData.getMediaBoxWidth(page)+x1;

                        y2 = currentPageData.getMediaBoxY(page);
                        y1 = currentPageData.getMediaBoxHeight(page)+y2;
                    }else{
                        x1=defX1;
                        y1=defY1;
                        x2=defX2;
                        y2=defY2;
                    }

                    //tell user
                    if(showMessages) {
                        System.out.println(
                                "Extracting text from rectangle ("
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

                    /**The call to extract the text*/
                    text =null;

                    //add to restore old functionality
                    //currentGrouping.removeInvalidXMLValues(false);

                    try{
                        text =currentGrouping.extractTextInRectangle(
                                x1,
                                y1,
                                x2,
                                y2,
                                page,
                                false,
                                true);

                        if(isTest && text!=null){ //ensure we test estimateParagrpahs flag in our code
                            text += currentGrouping.extractTextInRectangle(
                                    x1,
                                    y1,
                                    x2,
                                    y2,
                                    page,
                                    true,
                                    true);
                        }

                    } catch (final PdfException e) {
                        decodePdf.closePdfFile();
                        System.err.println("Exception " + e.getMessage()+" in file "+decodePdf.getObjectStore().fullFileName);
                        e.printStackTrace();
                    }

                    if (text == null) {
                        if(showMessages) {
                            System.out.println("No text found");
                        }
                    } else if(writeToFile){

                        //ensure a directory for data
                        final File page_path = new File(outputDir + separator);
                        if (!page_path.exists()) {
                            page_path.mkdirs();
                        }

                        /**
                         * choose correct prefix
                         */
                        String prefix=".txt";
                        String encoding=System.getProperty("file.encoding");

                        if(useXMLExtraction){
                            prefix=".xml";
                            encoding="UTF-8";
                        }

                        if(ExtractTextInRectangle.isTest) {
                            prefix = ".xml";
                        }

                        /**
                         * output the data
                         */
                        if(showMessages) {
                            System.out.println(
                                    "Writing to " + outputDir + page + prefix);
                        }

                        final OutputStreamWriter output_stream =
                                new OutputStreamWriter(
                                        new FileOutputStream(outputDir + page + prefix),
                                        encoding);

                        if((useXMLExtraction)|(isTest)){
                            output_stream.write(
                                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
                            output_stream.write(
                                    "<!-- Pixel Location of text x1,y1,x2,y2\n");
                            output_stream.write("(x1,y1 is top left corner)\n");
                            output_stream.write("(x1,y1 is bottom right corner)\n");
                            output_stream.write(
                                    "(origin is bottom left corner)  -->\n");
                            output_stream.write("\n\n<ARTICLE>\n");
                            output_stream.write(
                                    "<LOCATION x1=\""
                                            + x1
                                            + "\" "
                                            + "y1=\""
                                            + y1
                                            + "\" "
                                            + "x2=\""
                                            + x2
                                            + "\" "
                                            + "y2=\""
                                            + y2
                                            + "\" />\n");
                            output_stream.write("\n\n<TEXT>\n");
                            //NOTE DATA IS TECHNICALLY UNICODE
                            output_stream.write(text); //write actual data
                            output_stream.write("\n\n</TEXT>\n");
                            output_stream.write("\n\n</ARTICLE>\n");
                        }else {
                            output_stream.write(text); //write actual data
                        }

                        output_stream.close();
                    }

                    //remove data once written out
                    decodePdf.flushObjectValues(false);
                }
            } catch (final Exception e) {
                decodePdf.closePdfFile();
                System.err.println("Exception " + e.getMessage());
                e.printStackTrace();
                System.out.println(decodePdf.getObjectStore().getCurrentFilename());
            }

            /**
             * flush data structures - not strictly required but included
             * as example
             */
            decodePdf.flushObjectValues(true); //flush any text data read

            /**tell user*/
            if(showMessages) {
                System.out.println("Text read");
            }

        }

        /**close the pdf file*/
        decodePdf.closePdfFile();

    }
    //////////////////////////////////////////////////////////////////////////
    /**
     * Main routine which checks for any files passed and runs the demo.
     *
     * @param args is of type String[]
     */
    public static void main(final String[] args) {

        if(showMessages) {
            System.out.println("Simple demo to extract text objects");
        }

        //set to default
        String file_name = testFile;

        //check user has passed us a filename
        if(args.length==1){
            file_name = args[0];
            System.out.println("File :" + file_name);
        }else  if(args.length==5){

            file_name = args[0];
            System.out.println("File :" + file_name);

            System.out.println("User coordinates supplied");
            defX1=Integer.parseInt(args[1]);
            defY1=Integer.parseInt(args[2]);
            defX2=Integer.parseInt(args[3]);
            defY2=Integer.parseInt(args[4]);
        }else{
            System.out.println("Please call with either ");
            System.out.println("FileName");
            System.out.println("or");
            System.out.println("FileName x1 y1 x2 y2");
        }

        //check file exists
        final File pdf_file = new File(file_name);

        //if file exists, open and get number of pages
        if (!pdf_file.exists()) {
            System.out.println("File " + file_name + " not found");
        }
        final long now=System.currentTimeMillis();
        new ExtractTextInRectangle(file_name);
        final long finished=System.currentTimeMillis();

        if(!isTest) {
            System.out.println("Time taken=" + ((finished - now) / 1000));
        }

    }

    /**
     * Return text extracted with last extraction.
     * 
     * @return String
     */
    public String getExtractedText() {

        return text;
    }
}
