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
 * ExtractOutline.java
 * ---------------
 */

package org.jpedal.examples.text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jpedal.PdfDecoderServer;
import org.jpedal.PdfDecoderInt;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;

/**
 * <h2><b>Sample Code Showing how JPedal Library can be used with
 * PDF to Extract Outline Data from PDF.</b></h2>
 *
 * <p><b>Debugging tip: Set verbose=true in LogWriter to see what is going on.</b></p>
 * 
 * <p><a href="http://www.idrsolutions.com/how-to-extract-text-from-pdf-files/">See our Support Pages for more information on Text Extraction.</a></p>
 */
public class ExtractOutline {

    /**flag to show if we print messages*/
    public static boolean showMessages=true;

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
    @SuppressWarnings("UnusedDeclaration")
    public static boolean isTest;
    
    /**
     * Example method to open a file and extract the raw text.
     * 
     * @param file_name is of type String 
     */
    public ExtractOutline(final String file_name) {

        if(showMessages) {
            System.out.println("processing " + file_name);
        }

        //check output dir has separator
        if (!user_dir.endsWith(separator)) {
            user_dir += separator;
        }

        sortFiles(file_name);

    }

    private void sortFiles(String file_name){
        /**
         * if file name ends pdf, do the file otherwise
         * do every pdf file in the directory. We already know file or
         * directory exists so no need to check that, but we do need to
         * check its a directory
         */
        String[] files = null;
        File inputFiles = new File(file_name);

        if (file_name.toLowerCase().endsWith(".pdf")) {
            decodeFile(file_name);
        }else if(inputFiles.isDirectory()){

            /**
             * get list of files and check directory
             */

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
                    if (showMessages) {
                        System.out.println(file_name + file);
                    }

                    decodeFile(file_name + file);
                } else {
                    sortFiles(file_name + file);
                }
            }
        }
    }

    /**
     * Example method to open a file and extract the raw text.
     * 
     * @param array is of type byte[]
     */
    @SuppressWarnings("UnusedDeclaration")
    public ExtractOutline(final byte[] array) {

        if(showMessages) {
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
            decodePdf = new PdfDecoderServer(false);
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
            if(showMessages) {
                System.out.println("Opening file :" + file_name);
            }

            if(isFile) {
                decodePdf.openPdfFile(file_name);
            } else {
                decodePdf.openPdfArray(byteArray);
            }
        } catch (final PdfSecurityException e) {
            System.err.println("Exception " + e+" in pdf code "+file_name);
        } catch (final PdfException e) {
            System.err.println("Exception " + e+" in pdf code "+file_name);

        } catch (final Exception e) {
            System.err.println("Exception " + e+" in pdf code "+file_name);
            e.printStackTrace();
        }

        /**Get the outline from the PDF*/
        final Document outline =decodePdf.getOutlineAsXML();
        try {
            /**/
            final InputStream stylesheet = this.getClass().getResourceAsStream("/org/jpedal/examples/viewer/res/xmlstyle.xslt");

            final TransformerFactory tFactory = TransformerFactory.newInstance();
            final Transformer transformer = tFactory.newTransformer(new StreamSource(stylesheet));

            final DOMSource source = new DOMSource(outline);
            if(source.getNode()!=null){
                final File output = new File(outputDir+"outline.txt");

                if(!output.exists()){
                    final File createDir = new File(outputDir);
                    createDir.mkdirs();
                    output.createNewFile();
                }
                final FileOutputStream fos = new FileOutputStream(output);
                final StreamResult result = new StreamResult(fos);
                transformer.transform(source, result);
            }
        } catch (final TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (final TransformerException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        if(showMessages){
            System.out.println("Outline read to "+outputDir+"outline.txt");
            System.out.println("source="+outline);
        }
        /**
         * flush data structures - not strictly required but included
         * as example
         */
        decodePdf.flushObjectValues(true); //flush any text data read

        /**tell user*/
        if(showMessages) {
            System.out.println("Outline read");
        }

        /**close the pdf file*/
        decodePdf.closePdfFile();

        decodePdf=null;

    }

    /**
     * Main routine which checks for any files passed and runs the demo.
     * 
     * @param args is of type String[]
     */
    public static void main(final String[] args) {
        if(showMessages) {
            System.out.println("Simple demo to extract the outLine of a pdf");
        }

        //set to default
        String file_name="";

        //check user has passed us a filename and use default if none
        if (args.length==1){

            file_name = args[0];
            if(showMessages) {
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
        new ExtractOutline(file_name);
    }
}
