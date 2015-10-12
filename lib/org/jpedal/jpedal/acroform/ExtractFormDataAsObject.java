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
 * ExtractFormDataAsObject.java
 * ---------------
 */

package org.jpedal.examples.acroform;

import java.io.File;

import org.jpedal.PdfDecoderServer;
import org.jpedal.PdfDecoderInt;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.ReturnValues;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.objects.raw.PdfArrayIterator;
import org.jpedal.objects.raw.PdfDictionary;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.utils.LogWriter;

/**
 * <h2><b>Simple Form Data Extraction With Images that are Extracted to a Directory.</b></h2>
 *
 * <p>It can run from jar directly using the command:
 *
 * <br><b>java -cp libraries_needed org/jpedal/examples/acroform/ExtractFormDataAsObject inputValue</b></p>
 *
 * <p>Where inputValues is 1 value:-</p>
 * <ul>
 * <li>First value:	The filename (including the path if needed) or a directory containing files. <br>If it contains spaces it must be enclosed by double quotes (ie "C:/Path with spaces/").</li>
 * </ul>
 * 
 * <p><a href="http://www.idrsolutions.com/pdf_forms/">See our Support Pages for more information on form element extraction.</a></p>
 */
@SuppressWarnings({"UnusedDeclaration", "PMD"})
public class ExtractFormDataAsObject {

    /**output where we put files*/
    private String outputDir = System.getProperty("user.dir");

    /**number of files read*/
    private int fileCount;

    /**flag to show if we print messages*/
    static boolean outputMessages;

    
    public int getFileCount(){
        return fileCount;
    }

    /**correct separator for OS */
    String separator = System.getProperty("file.separator");

    /**the decoder object which decodes the pdf and returns a data object*/
    PdfDecoderInt decodePdf;

    /**sample file which can be setup - substitute your own.
     * If a directory is given, all the files in the directory will be processed*/
    //private static String test_file = "/mnt/shared/storypad/input/acadapp.pdf";
    private static final String test_file = "/PDFdata/files-jpedal/Testdokument PDF.pdf";

    /**example method to open a file and extract the form data*/
    public ExtractFormDataAsObject(String file_name) {

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

                    //some tools want a form name to be set out slightly differently in XFA
                    //if (decodePdf.getFormRenderer().isXFA()) {
                    //    System.out.println("For IText mappings use " + com.idrsolutions.pdf.acroforms.xfa.XFAUtils.convertXFANameToZeroArrayFormat((String) nextCompName));
                    //}
                    
//                    Object[] multiforms=acro.getFormComponents((String)nextCompName, ReturnValues.FORMOBJECTS_FROM_NAME, -1);
//                    for(Object multi : multiforms){
//                    	System.out.println("Name="+nextCompName+" XFAtype="+((FormObject)multi).getXFARawDataType());
//                    }
                }
                
                final Object[] multiforms = acro.getFormComponents((String) nextCompName, ReturnValues.FORMOBJECTS_FROM_NAME, -1);
                for (final Object multiform : multiforms) {
                    if (outputMessages) {
                        System.out.println("name=" + nextCompName + " rawData=" + multiform + " value=" + ((FormObject) multiform).getSelectedItem());
                    }
                }
            }

            /**
             * gui components
             */
            //all FormNames in Document
//	    Object[] guiComps = acro.getFormComponents(null, ReturnValues.FORM_NAMES, -1);
//
//	    //iterate over all forms and get data for each on any page
//	    for(Object nextCompName :components){
//
//		if(outputMessages)
//		    System.out.println(nextCompName);
//
//		Object[] multiforms=acro.getFormComponents((String)nextCompName, ReturnValues.GUI_FORMS_FROM_NAME, -1);
//		for (Object multiform : multiforms) 
//		    System.out.println("name="+nextCompName + " comp=" + multiform);
//	    }
            
           // scanAnnotationsPerDoc(acro);  //Uncomment to scan document form objects
           // scanAnnotationsPerPage(acro); //Uncomment to scan page form objects

        }
        
        /**
         * XFA specific items (only works in XFA releases)
         */
        
        //will return false if not XFA or is XFA in legacy mode
        //only treu is XFA and using XFA data
        final boolean isXFA=decodePdf.getFormRenderer().isXFA();
        
        if(isXFA){
            /**
            System.out.println("XFA data detected and used");
            System.out.println("------------------------");
            
            //current supoorted values - returns node to scan
            System.out.println("config="+decodePdf.getFormRenderer().getXMLContentAsNode(PdfDictionary.XFA_CONFIG));
            System.out.println("config="+decodePdf.getFormRenderer().getXMLContentAsBytes(PdfDictionary.XFA_CONFIG));
            
            System.out.println("dataset="+decodePdf.getFormRenderer().getXMLContentAsNode(PdfDictionary.XFA_DATASET));
            System.out.println("dataset="+decodePdf.getFormRenderer().getXMLContentAsBytes(PdfDictionary.XFA_DATASET));
            
            System.out.println("template="+decodePdf.getFormRenderer().getXMLContentAsNode(PdfDictionary.XFA_TEMPLATE));
            System.out.println("template="+decodePdf.getFormRenderer().getXMLContentAsBytes(PdfDictionary.XFA_TEMPLATE));
            /**/
        }
        
        /**
         * close the pdf file
         */
        decodePdf.closePdfFile();
    }
 
     /**
     * You can scan through the Annotations on a per page basis with 
     * this code
     */   
    @SuppressWarnings("unused")
    private void scanAnnotationsPerPage(final AcroRenderer  acro){
        final int pageCount=decodePdf.getPageCount(); //page count
        for(int ii=1;ii<pageCount+1;ii++){

            //get the list of Annotations for each page
            final PdfArrayIterator annotListForPage = acro.getAnnotsOnPage(ii);

            System.out.println("-----page "+ii+ '/' +pageCount+ ' ' +annotListForPage);
            
            //if values present scan through
            if(annotListForPage!=null){
                while (annotListForPage.hasMoreTokens()) {

                    //get PDF ref of annot which has already been decoded and get actual FormObject
                    final String annotKey = annotListForPage.getNextValueAsString(true);

                    //each PDF annot object - extract data from it
                    final FormObject annotObj = decodePdf.getFormRenderer().getFormObject(annotKey);

                    printAnnotations(ii, annotObj);                                              
                }
            }
        }
    }    
    
     /**
     * You can scan through the Annotations on a per doc basis with 
     * this code
     */    
    @SuppressWarnings("unused")
    private void scanAnnotationsPerDoc(final AcroRenderer  acro){
        
        //Create an array containing the document annotations
        final Object[] docObjects = acro.getFormComponents(null, ReturnValues.FORMOBJECTS_FROM_REF, -1);
 
        //Loop through and output the document annotations
        for(int i = 0; i < docObjects.length; ++i){
            if(docObjects[i]!=null){
                printAnnotations(i, (FormObject)docObjects[i]);
            }
        }
    }
    
    /**
    * Function prints the annotations of given annotKey
    * used in a loop where count is the iteration
    */      
    private void printAnnotations(final int count, final FormObject annotObj){
        //get PDF ref of annot which has already been decoded and get actual FormObject

        System.out.println("---------------------------------------");
        /**
         * example code showing how you can read data from the FormObject
         */
        final int subtype = annotObj.getParameterConstant(PdfDictionary.Subtype);

        System.out.println("type="+subtype+ ' ' +PdfDictionary.showAsConstant(subtype));

        if (subtype == PdfDictionary.Sig || subtype == PdfDictionary.Link) {

            if (subtype == PdfDictionary.Link) {
                System.out.println("link object");
            } else {
                System.out.println("Sig object");
            }
            //PDF co-ords

            final float[] coords = annotObj.getFloatArray(PdfDictionary.Rect);
            System.out.println("PDF Rect= " + coords[0] + ' ' + coords[1] + ' ' + coords[2] + ' ' + coords[3]);

            //convert to Javaspace rectangle by subtracting page Crop Height
            final int pageH = decodePdf.getPdfPageData().getCropBoxHeight(count);
            final float x = coords[0];
            final float w = coords[2] - coords[0];
            final float h = coords[3] - coords[1];
            final float y = pageH - coords[1] - h; //note we remove h from y
            System.out.println("Javaspace Rect x=" + x + " y=" + y + " w=" + w + " h=" + h);

            //text in A subobject
            final PdfObject aData = annotObj.getDictionary(PdfDictionary.A);
            if (aData != null && aData.getNameAsConstant(PdfDictionary.S) == PdfDictionary.URI) {
                final String text = aData.getTextStreamValue(PdfDictionary.URI); //+"ZZ"; deliberately broken first to test checking
                System.out.println("text=" + text);
            }  
        }
    }
     
    //////////////////////////////////////////////////////////////////////////
    /**
     * main routine which checks for any files passed and runs the demo
     */
    public static void main(final String[] args) {

        if(outputMessages) {
            System.out.println("Simple demo to extract form data");
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
            new ExtractFormDataAsObject(file_name);
        }
    }

    //return location of files
    @SuppressWarnings("UnusedDeclaration")
    public String getOutputDir() {
        return outputDir;
    }
}
