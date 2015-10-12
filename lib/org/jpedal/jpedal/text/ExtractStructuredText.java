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
 * ExtractStructuredText.java
 * ---------------
 */

package org.jpedal.examples.text;

import java.io.File;
import java.io.InputStream;

import org.jpedal.*;

import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfSecurityException;

import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * <h2><b>Sample Code demonstrating JPedal library being used with
 * PDF to Extract Structed Text from a PDF.</b></h2>
 *
 * <p><b>Debugging tip: Set verbose=true in LogWriter to see what is going on.</b></p>
 *
 * <p>It can run from jar directly using the command:
 *
 * <br><b>java -cp libraries_needed org/jpedal/examples/text/ExtractTextInRectangle inputValues</b></p>
 *
 * <p>Where inputValues is two space delimited input values</p>
 * <ul>
 *  <li>First value:	The PDF filename (including the path if needed) or a directory containing PDF files. If it contains spaces it must be enclosed by double quotes (ie "C:/Path with spaces/").</li>
 *  <li>Second value (optional):	Target directory for ouput data.</li>
 * </ul>
 *
 * <p>For non-structured files, consider:</p>
 * <ul>
 * <li>http://files.idrsolutions.com/samplecode/org/jpedal/examples/text/ExtractTextAsWordlist.java.html</li>
 * <li>http://files.idrsolutions.com/samplecode/org/jpedal/examples/text/ExtractTextInRectangle.java.html</li>
 * <li>http://files.idrsolutions.com/samplecode/org/jpedal/examples/text/ExtractTextInRectangleAsTable.java.html</li>
 * </ul>
 * 
 * <p><a href="http://www.idrsolutions.com/how-to-extract-text-from-pdf-files/">See our Support Pages for more information on Text Extraction</a></p>
 */
public class ExtractStructuredText {

    /**used as part of test to limit pages to first 10*/
    public static boolean isTest;

    /**output where we put files*/
    protected static String output = System.getProperty("user.dir") + "xml";

    /**flag to show if we display messages*/
    public static boolean showMessages = true;

    /**correct separator for OS */
    protected final String separator = System.getProperty("file.separator");

    /**the decoder object which decodes the pdf and returns a data object*/
    protected PdfDecoderInt decodePdf;

    /**location output files written to*/
    protected String outputFile = "";

    /**
     * Example method to open a file or dir and extract the Structured Content
     * to outputDir.
     *
     * @param root is of type String
     * @param outputDir is of type String
     */
    public ExtractStructuredText(String root, final String outputDir) {

        output = outputDir;

        //check output dir has separator
        if (!output.endsWith(separator)) {
            output += separator;
        }

        //create a directory if it doesn't exist
        final File output_path = new File(output);
        if (!output_path.exists()) {
            output_path.mkdirs();
        }

        /**
         * if file name ends pdf, do the file otherwise
         * do every pdf file in the directory. We already know file or
         * directory exists so no need to check that, but we do need to
         * check its a directory
         */
        if (root.toLowerCase().endsWith(".pdf")) {
            decodeFile(root);
        } else {

            /**
             * get list of files and check directory
             */

            String[] files = null;
            final File inputFiles;

            /**make sure name ends with a deliminator for correct path later*/
            if (!root.endsWith(separator)) {
                root += separator;
            }

            try {
                inputFiles = new File(root);

                if (!inputFiles.isDirectory()) {
                    System.err.println(root
                            + " is not a directory. Exiting program");
                }
                files = inputFiles.list();
            } catch (final Exception ee) {
                LogWriter.writeLog("Exception trying to access file "
                        + ee.getMessage());
            }

            /**now work through all pdf files*/
            final long fileCount = files.length;

            for (int i = 0; i < fileCount; i++) {
                if (showMessages) {
                    System.out.println(i + "/ " + fileCount + ' ' + files[i]);
                }

                if (files[i].toLowerCase().endsWith(".pdf")) {
                    if (showMessages) {
                        System.out.println(root + files[i]);
                    }

                    decodeFile(root + files[i]);
                }
            }
        }
    }

    /**
     * routine to decode a file
     */
    protected void decodeFile(final String file_name) {

        System.out.println("Processing " + file_name);

        /**get just the name of the file without
         * the path to use as a sub-directory or .pdf
         */
        final String name; //set a default just in case

        //allow for both separators
        int pointer = file_name.lastIndexOf('/');
        final int pointer2 = file_name.lastIndexOf('\\');
        if(pointer2>pointer) {
            pointer = pointer2;
        }

        name = file_name.substring(pointer + 1, file_name.length() - 4);

        /**
         * create output dir for text
         */
        outputFile = output + separator + name + ".xml";

        /**debugging code to create a log
         LogWriter.setupLogFile(true,0,"","v",false);
         LogWriter.log_name =  "/mnt/shared/log.txt";
         */

        //PdfDecoder returns a PdfException if there is a problem
        try {
            decodePdf = new PdfDecoderServer(false);

            if (showMessages) {
                System.out.println("\n----------------------------");
            }

            /**
             * open the file (and read metadata including pages in  file)
             */
            if (showMessages) {
                System.out.println("Opening file :" + file_name);
            }

            decodePdf.openPdfFile(file_name);

        } catch (final PdfSecurityException se) {
            System.err.println("Security Exception " + se
                    + " in pdf code for text extraction on file "
                    + decodePdf.getObjectStore().getCurrentFilename());
            //e.printStackTrace();
        } catch (final PdfException se) {
            System.err.println("Pdf Exception " + se
                    + " in pdf code for text extraction on file "
                    + decodePdf.getObjectStore().getCurrentFilename());
            //e.printStackTrace();
        } catch (final Exception e) {
            System.err.println("Exception " + e
                    + " in pdf code for text extraction on file "
                    + decodePdf.getObjectStore().getCurrentFilename());
            //e.printStackTrace();
        }

        /**
         * extract data from pdf (if allowed).
         */
        if ((decodePdf.isEncrypted() && (!decodePdf.isPasswordSupplied()))
                && (!decodePdf.isExtractionAllowed())) {
            if (showMessages) {
                System.out.println("Encrypted settings");
                System.out
                        .println("Please look at Viewer for code sample to handle such files");
                System.out.println("Or get support/consultancy");
            }
        } else {

            /**
             * extract data from pdf
             */
            try {

                //read pages -if you already have code this is probably
                //all you need!
                final Document tree = decodePdf.getMarkedContent();

                if (tree == null) {
                    //if (showMessages)
                    System.out.println("No text found");
                } else {

                    /**
                     * format tree
                     */
                    final InputStream stylesheet = this.getClass()
                            .getResourceAsStream(
                                    "/org/jpedal/examples/text/xmlstyle.xslt");

                    final TransformerFactory transformerFactory = TransformerFactory
                            .newInstance();

                    /**output tree*/
                    try {
                        final Transformer transformer = transformerFactory
                                .newTransformer(new StreamSource(stylesheet));

                        //useful for debugging
                        //transformer.transform(new DOMSource(tree), new StreamResult(System.out));

                        if(tree==null || !tree.hasChildNodes()){

                            //if(debug)
                            System.out.println("No tree data "+tree);
                            return;
                        }

                        //warn user if no content present
                        if(!tree.getDocumentElement().hasChildNodes()){
                            tree.appendChild(tree.createComment("There is NO Structured text in the file to extract!!"));
                            tree.appendChild(tree.createComment("JPedal can only extract it if it has been added when PDF created"));
                            tree.appendChild(tree.createComment("Please read our blog post at http://www.jpedal.org/PDFblog/2010/09/the-easy-way-to-discover-if-a-pdf-file-contains-structured-content/ "));
                        }

                        //System.out.println("outputFile="+outputFile);
                        transformer.transform(new DOMSource(tree), new StreamResult(outputFile));

                    } catch (final Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    } catch (final Error ee) {
                        ee.printStackTrace();
                        System.exit(1);
                    }


                    /**
                     * output the data
                     */
                    if (showMessages) {
                        System.out.println("Writing to " + outputFile);
                    }

                }

                if (showMessages) {
                    System.out.println("\n----------done--------------");
                }

                //remove data once written out
                decodePdf.flushObjectValues(false);

            } catch (final Exception e) {
                decodePdf.closePdfFile();
                System.err.println("Exception " + e.getMessage());
                e.printStackTrace();
                System.out.println(decodePdf.getObjectStore()
                        .getCurrentFilename());
            }

            /**
             * flush data structures - not strictly required but included
             * as example
             */
            decodePdf.flushObjectValues(true); //flush any text data read

            /**tell user*/
            if (showMessages) {
                System.out.println("Text read");
            }

            /**close the pdf file*/
            decodePdf.closePdfFile();

        }
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * Main routine which checks for any files passed and runs the demo.
     *
     * @param args is of type String[]
     */
    public static void main(final String[] args) {

        if (showMessages) {
            System.out.println("Simple demo to extract text objects");
        }

        //set to default
        final String file_name;

        //check user has passed us a filename
        if (args.length == 2) {
            file_name = args[0];
            output = args[1];
            System.out.println("File :" + file_name);

            //check file exists
            final File pdf_file = new File(file_name);

            //if file exists, open and get number of pages
            if (!pdf_file.exists()) {
                System.out.println("File " + file_name + " not found");
            }
            final long now = System.currentTimeMillis();
            new ExtractStructuredText(file_name,output);
            final long finished = System.currentTimeMillis();

            if (!isTest) {
                System.out.println("Time taken=" + ((finished - now) / 1000));
            }

        } else {
            System.out.println("Please call with parameters :-");
            System.out.println("FileName");
            System.out.println("outputDir");
        }
    }
}
