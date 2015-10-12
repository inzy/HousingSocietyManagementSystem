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
 * ExtractFormCheckboxData.java
 * ---------------
 */

/**
 * Extract checkbox data example
 *
 * It can run from jar directly using the command
 *
 * java -cp libraries_needed org/jpedal/examples/acroform/ExtractFormCheckboxData inputValue
 *
 * where inputValues is 1 value
 *
 * First value:	The filename (including the path if needed) or a directory containing files. If it contains spaces it must be enclosed by double quotes (ie "C:/Path with spaces/").
 */
package org.jpedal.examples.acroform;

import java.io.File;

import org.jpedal.PdfDecoderServer;
import org.jpedal.PdfDecoderInt;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.ReturnValues;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PdfDictionary;
import org.jpedal.utils.LogWriter;

public class ExtractFormCheckboxData {

    /**output where we put files*/
    private String outputDir = System.getProperty("user.dir");

    /**number of files read*/
    private int fileCount;

    /**flag to show if we print messages*/
    static final boolean outputMessages=true;

    @SuppressWarnings("UnusedDeclaration")
    public int getFileCount(){
        return fileCount;
    }

    /**correct separator for OS */
    final String separator = System.getProperty("file.separator");

    /**the decoder object which decodes the pdf and returns a data object*/
    PdfDecoderInt decodePdf;

    /**sample file which can be setup - substitute your own.
     * If a directory is given, all the files in the directory will be processed*/
    //private static String test_file = "/mnt/shared/storypad/input/acadapp.pdf";
    private static final String test_file = "/PDFdata/files-jpedal/Testdokument PDF.pdf";

    /**example method to open a file and extract the form data*/
    public ExtractFormCheckboxData(String file_name) {

        //check output dir has separator
        if (!outputDir.endsWith(separator)) {
            outputDir = outputDir + separator + "forms" + separator;
        }

        //create a directory if it doesn't exist
        final File output_path = new File(outputDir);
        if (!output_path.exists()){
            output_path.mkdirs();
        }

        /**
         * if file name ends pdf, do the file otherwise
         * do every pdf file in the directory. We already know file or
         * directory exists so no need to check that, but we do need to
         * check its a directory
         */
        if (file_name.toLowerCase().endsWith(".pdf")) {
            decodePage("",file_name);
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

                }else {
                    files = inputFiles.list();
                }
            } catch (final Exception ee) {
                LogWriter.writeLog(
                        "Exception trying to access file " + ee.getMessage());
            }

            /**now work through all pdf files*/
            for (final String file : files) {
                if (file.toLowerCase().endsWith(".pdf")) {

                    if (outputMessages) {
                        System.out.println(">>_" + file_name + file);
                    }

                    decodePage(file_name, file);
                }
            }
        }
    }

    /**
     * routine to decode a page
     */
    private void decodePage(final String dir, final String name) {

        final String file_name = dir + name;

        //PdfDecoder returns a PdfException if there is a problem
        try {
            decodePdf = new PdfDecoderServer(false);

            /**
             * open the file (and read metadata including form in file) NO OTHER
             * ACTIVITY REQUIRED TO GET FORM DATA!!
             */
            if (outputMessages) {
                System.out.println("Opening file :" + file_name);
            }
            decodePdf.openPdfFile(file_name);

        } catch (final Exception e) {
            e.printStackTrace();

            System.err.println("Exception " + e + " in pdf code with " + file_name);

        }

        /**
         * extract data from pdf (if allowed).
         */
        if ((decodePdf.isEncrypted()) && (!decodePdf.isExtractionAllowed())) {
            if (outputMessages) {
                System.out.println("Encrypted settings");
                System.out.println("Please look at Viewer for code sample to handle such files");
            }
        } else {

            fileCount++;

            /**
             * get AcroRenderer object
             */
            final AcroRenderer acro = decodePdf.getFormRenderer();

            /**
             * accessing form Data
             *
             *  acro.getFormComponents(
             * NAME, REF or null for all values,
             * ReturnValues (Enum of possible values),
             * -1 for all pages or pageNumber)
             */
            
            //some examples which all return Object[]
           
            //all forms in document
            //acro.getFormComponents(null, ReturnValues.FORMOBJECTS_FROM_REF, -1);
            //acro.getFormComponents(null, ReturnValues.FORMOBJECTS_FROM_NAME, -1);
            
            //all forms on page 5
            //acro.getFormComponents(null, ReturnValues.FORMOBJECTS_FROM_REF, 5);
            //acro.getFormComponents(null, ReturnValues.FORMOBJECTS_FROM_NAME, 5);
            
            //all formNames
            //acro.getFormComponents(null, ReturnValues.FORM_NAMES, -1);
            
            //all FormNames of Forms on page 12
            //acro.getFormComponents(null, ReturnValues.FORMOBJECTS_FROM_REF, 12);
            
            //all forms in document called Mabel
            //acro.getFormComponents("Mabel", ReturnValues.FORMOBJECTS_FROM_NAME, -1);
            
            //all forms on page 5 called Mabel
            //acro.getFormComponents("Mabel", ReturnValues.FORMOBJECTS_FROM_NAME, 5);
         
            //form with PDF Reference
            //acro.getFormComponents("25 0 R", ReturnValues.FORMOBJECTS_FROM_REF, -1);
            
            //form with PDF Reference on page 5
            //acro.getFormComponents("25 0 R", ReturnValues.FORMOBJECTS_FROM_REF, 5);
            
            //if you just want the FormObject for PDF Object 25 0 R, you can also use
            //FormObject formObject=acro.getFormObject("25 0 R");
            
            //if you need direct access to the GUI components, you can use
            //(this will also trigger resolving these objects if Swing so we recommend you work
            //this FormObjects unless you explicitly need this)
            //acro.getFormComponents(null, ReturnValues.GUI_FORMS_FROM_NAME, -1);
            //acro.getFormComponents(null, ReturnValues.GUI_FORMS_FROM_NAME, 5);
            
            /**
             * Objects
             */
            //all FormNames in Document
            final Object[] components = acro.getFormComponents(null, ReturnValues.FORM_NAMES, -1);

            //iterate over all forms and get data for each on any page
            for (final Object nextCompName : components) {

                if (outputMessages) {
                    System.out.println(nextCompName);
                }

                final Object[] multiforms = acro.getFormComponents((String) nextCompName, ReturnValues.FORMOBJECTS_FROM_NAME, -1);
                for (final Object multiform : multiforms) {
                    if (outputMessages) {
                        System.out.println("name=" + nextCompName + " rawData=" + multiform + " value=" + ((FormObject) multiform).getSelectedItem());
                    }
                }
            }

            scanAnnotationsPerDoc(acro);  //Uncomment to scan document form objects
           
        }
        /**
         * close the pdf file
         */
        decodePdf.closePdfFile();
    }
 
    
     /**
     * You can scan through the Annotations on a per doc basis with 
     * this code
     */    
    private static void scanAnnotationsPerDoc(final AcroRenderer acro){
        
        //Create an array containing the document annotations
        final Object[] docObjects = acro.getFormComponents(null, ReturnValues.FORMOBJECTS_FROM_REF, -1);
 
        //Loop through and output the document annotations
        for (final Object docObject : docObjects) {
            if (docObject != null) {
                final FormObject annotObj = (FormObject) docObject;

                /**
                 * example code showing how you can read data from the FormObject
                 */
                final int subtype = annotObj.getParameterConstant(PdfDictionary.Subtype);


                if (subtype == PdfDictionary.Btn && annotObj.getPageNumber() != -1) { //if page -1, not displayed so ignore
                    System.out.println("\ntype=" + subtype + ' ' + PdfDictionary.showAsConstant(subtype) + " page=" + annotObj.getPageNumber());

                    System.out.println("name=" + annotObj.getTextStreamValue(PdfDictionary.T) + " isSelected=" + annotObj.isSelected() + " NormalState=" + annotObj.getNormalOnState() + ' ' + annotObj.getObjectRefAsString());
                }
            }
        }
    }
     
    //////////////////////////////////////////////////////////////////////////
    /**
     * main routine which checks for any files passed and runs the demo
     */
    public static void main(final String[] args) {

        if(outputMessages) {
            System.out.println("Simple demo to extract checkbox data");
        }

        //set to default
        String file_name = test_file;

        //check user has passed us a filename and use default if none
        if (args.length != 1){
            if(outputMessages) {
                System.out.println("Default test file used");
            }
        }else {
            file_name = args[0];
            if(outputMessages) {
                System.out.println("File :" + file_name);
            }
        }

        //check file exists
        final File pdf_file = new File(file_name);

        //if file exists, open and get number of pages
        if (!pdf_file.exists()) {
            if(outputMessages) {
                System.out.println("File " + file_name + " not found");
            }

        }else{
            new ExtractFormCheckboxData(file_name);
        }
    }

    //return location of files
    @SuppressWarnings("UnusedDeclaration")
    public String getOutputDir() {
        return outputDir;
    }
}
