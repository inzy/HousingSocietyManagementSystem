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
 * ExtractImages.java
 * ---------------
 */

package org.jpedal.examples.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jpedal.PdfDecoderServer;

import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.handlers.DefaultImageHelper;
import org.jpedal.io.*;
import org.jpedal.objects.PdfImageData;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <h2><b>Simple Image Extraction with Images are Extracted to a Directory.</b></h2>
 *
 * <p>It can run from jar directly using the command:
 *
 * <br><b>java -cp libraries_needed org/jpedal/examples/images/ExtractImages inputValues</b></p>
 *
 * <p>Where inputValues is 1 or 2 values:</p>
 * <ul>
 * <li>First value:	The PDF filename (including the path if needed) or a directory containing PDF files. If it contains spaces it must be enclosed by double quotes (ie "C:/Path with spaces/").</li>
 * <li>Second value (optional):	This indicates the required output image type (default is png if nothing specified). Options are tiff, png, jpg.</li>
 * </ul>
 *
 * <p><a href="http://www.idrsolutions.com/how-to-extract-images-from-pdf-files/">See our Support Pages for more info on Image Extraction.</a></p>
 */
public class ExtractImages
{
    
    /**use dir for output*/
    private String user_dir = System.getProperty( "user.dir" );
    
    /**flag to show if we print messages*/
    public static boolean outputMessages = true;
    
    /**the decoder object which decodes the pdf and returns a data object*/
    PdfDecoderInt decode_pdf;
    
    /**correct separator for OS */
    final String separator = System.getProperty( "file.separator" );
    
    /**location output files written to*/
    private String output_dir="";
    
    public static String testOutputDir="current_images/";
    
    public static boolean isTest;
    
    //type of image to save
    private static String prefix = "png";
    
    /**sample file which can be setup - substitute your own.
     * If a directory is given, all the files in the directory will be processed*/
    private static final String test_file = "/mnt/shared/Poloznicel_nalozbene_test_1.pdf";
    
    
    /**example method to open a file and extract the images*/
    public ExtractImages( String file_name )
    {
        
        /**debugging code to create a log*/
        // LogWriter.setupLogFile(true,1,"","v",false);
        //	 LogWriter.log_name =  "/mnt/shared/log.txt";
        /***/
        
        //check root dir has separator
        if(!user_dir.endsWith(separator)) {
            user_dir += separator;
        }
        
        /**
         * if file name ends pdf, do the file otherwise
         * do every pdf file in the directory. We already know file or
         * directory exists so no need to check that, but we do need to
         * check its a directory
         */
        if (!file_name.startsWith(".") && file_name.toLowerCase().endsWith(".pdf")){
            decode(file_name);
        }else{
            
            /**
             * get list of files and check directory
             */
            
            String[] files = null;
            final File inputFiles;
            
            /**make sure name ends with a deliminator for correct path later*/
            if(!file_name.endsWith(separator)) {
                file_name += separator;
            }
            
            try
            {
                inputFiles = new File( file_name );
                
                if(!inputFiles.isDirectory()){
                    System.err.println(file_name+" is not a directory. Exiting program");
                    
                }else {
                    files = inputFiles.list();
                }
            }catch( final Exception ee )
            {
                LogWriter.writeLog( "Exception trying to access file " + ee.getMessage() );
            }
            
            /**now work through all pdf files*/
            final long fileCount=files.length;
            
            for(int i=0;i<fileCount;i++){
                System.out.println(i+"/ "+fileCount+ ' ' +files[i]);
                
                if(files[i].endsWith(".pdf")){
                    System.out.println(file_name+files[i]);
                    
                    decode(file_name+files[i]);
                }
            }
        }
    }
    
    /**
     * routine to open and decode a pdf pages
     */
    private void decode(final String file_name){
        
        /**get just the name of the file without
         * the path to use as a sub-directory or .pdf
         */
        
        String name="demo"; //set a default just in case
        
        final int pointer=file_name.lastIndexOf(separator);
        
        if(pointer!=-1) {
            name=file_name.substring(pointer+1,file_name.length()-4);
        }
        
        //PdfDecoder returns a PdfException if there is a problem
        try
        {
            decode_pdf = new PdfDecoderServer( false );
            
            /**
             * use this version of setExtraction in code below to alter dpi from 72
             */
            //decode_pdf.setExtractionMode(DecoderOptions.FINALIMAGES,dpi,1);
            
            //tell JPedal what we want it to extract - flag added so user can run and extract OPI
            final String opiFlag=System.getProperty("org.jpedal.opi");
            if(opiFlag==null) {
                decode_pdf.setExtractionMode(PdfDecoderServer.RAWIMAGES+PdfDecoderServer.FINALIMAGES+PdfDecoderInt.RASTERIZE_FORMS);
            } else {
                decode_pdf.setExtractionMode(PdfDecoderServer.RAWIMAGES+PdfDecoderServer.FINALIMAGES+PdfDecoderServer.XFORMMETADATA+PdfDecoderInt.RASTERIZE_FORMS);
            }
            
            /**
             * open the file (and read metadata including pages in  file)
             */
            if(outputMessages) {
                System.out.println( "Opening file :" + file_name );
            }
            
            decode_pdf.openPdfFile( file_name );
            
            //byte version to open file
            /**
             * FileInputStream file = new FileInputStream(file_name);
             * byte[] arr = toByteArray(file);
             * decode_pdf.openPdfArray(arr);*/
        }
        catch( final Exception e )
        {
            System.err.println( "9.Exception " + e + " in pdf code" );
        }
        
        /**
         * extract data from pdf (if allowed).
         */
        if ((decode_pdf.isEncrypted()&&(!decode_pdf.isPasswordSupplied()))&&(!decode_pdf.isExtractionAllowed())) {
            if(outputMessages)	{
                System.out.println("Encrypted settings");
                System.out.println("Please look at Viewer for code sample to handle such files");
                System.out.println("Or get support/consultancy");
            }
        }else{
            
            //page range
            final int start = 1;
            final int end =decode_pdf.getPageCount();

            /**
             * create output dir for images
             */
            output_dir = user_dir + "images" + separator+name+separator;
            
            
            //create a directory if it doesn't exist
            final File output_path = new File( output_dir );
            if(!output_path.exists()) {
                output_path.mkdirs();
            }
            
            /**
             * extract data from pdf and then write out the images
             */
            if(outputMessages) {
                System.out.println( "Images will be in directory " + output_dir );
            }
            try
            {
                for( int page = start;page < end + 1;page++ )
                { //read pages
                    
                    //decode the page
                    decode_pdf.decodePage( page );
                    
                    //if you have JVM option -Dorg.jpedal.trackImages="true"
                    //this will give you details on images
                    //String imageInfo=decode_pdf.getInfo(PdfDictionary.Image);
                    //System.out.println("info is ="+imageInfo);
                    
                    //get the PdfImages object which now holds the images.
                    //binary data is stored in a temp directory and we hold the
                    //image name and other info in this object
                    final PdfImageData pdf_images = decode_pdf.getPdfImageData();
                    
                    //image count (note image 1 is item 0, so any loop runs 0 to count-1)
                    final int image_count = pdf_images.getImageCount();
                    
                    //tell user
                    if( image_count > 0 ){
                        if(outputMessages) {
                            System.out.println( "Page "+page+" contains " + image_count + " images" );
                        }
                        
                        //create a directory for page
                        
                        String target=output_dir+separator+page;
                        
                        
                        final File page_path = new File( target );
                        if(!page_path.exists()) {
                            page_path.mkdirs();
                        }
                        
                    }
                    
                    //work through and save each image
                    for( int i = 0;i < image_count;i++ )
                    {
                        final String image_name = pdf_images.getImageName( i );
                        BufferedImage image_to_save;
                        
                        try{
                            
                            //get raw version of image (R prefix for raw image)
                            image_to_save = decode_pdf.getObjectStore().loadStoredImage('R' + image_name );
                            
                            String outputDir=output_dir + page +separator;
                            
                            
                            saveImage(image_to_save,outputDir+ 'R' + image_name+ '.' +prefix,prefix);
                            
                            //load processed version of image (converted to rgb)
                            image_to_save = decode_pdf.getObjectStore().loadStoredImage( image_name );
                            
                            //save image
                            if(image_to_save==null){
                                if(outputMessages){
                                    System.out.println("No image data for "+image_name);
                                }
                            }else{
                                saveImage(image_to_save,outputDir+ image_name+ '.' +prefix,prefix);
                            }
                            
                            /**save metadata as XML file in 1.4/1.5 java - not in 1.3*/
                            outputMetaDataToXML(file_name, page, pdf_images, i, image_name);
                            
                        }
                        catch( final Exception ee )
                        {
                            System.err.println( "Exception " + ee + " in extracting images" );
                        }
                    }
                    
                    //flush images in case we do more than 1 page so only contains
                    //images from current page
                    decode_pdf.flushObjectValues(true);
                }
            }
            catch( final Exception e )
            {
                decode_pdf.closePdfFile();
                System.err.println( "Exception " + e.getMessage() );
                
            }
            
            /**tell user*/
            if(outputMessages) {
                System.out.println( "Images read" );
            }
            
        }
        
        /**close the pdf file*/
        decode_pdf.closePdfFile();
        
    }
    
    /**save image - different versions have different bugs for file formats so we use best for
     * each image type
     * @param image_to_save
     */
    private static void saveImage(final BufferedImage image_to_save, final String fileName, final String prefix) {
        
        if(JAIHelper.isJAIused()) {
            JAIHelper.confirmJAIOnClasspath();
        }
        
        //we recommend JAI for tifs
        if(prefix.contains("tif") && JAIHelper.isJAIused()){
            
            try {
                
                final FileOutputStream os = new FileOutputStream(fileName);
                
                //get tiff compression
                final String tiffFlag=System.getProperty("org.jpedal.compress_tiff");
                final boolean compressTiffs = tiffFlag!=null;
                
                com.sun.media.jai.codec.TIFFEncodeParam params = null;
                
                if(compressTiffs){
                    params = new com.sun.media.jai.codec.TIFFEncodeParam();
                    params.setCompression(com.sun.media.jai.codec.TIFFEncodeParam.COMPRESSION_DEFLATE);
                }
                
                javax.media.jai.JAI.create("encode", image_to_save, os, "TIFF", params);
                
                
                os.flush();
                os.close();
                
            } catch (final Exception e) {
                e.printStackTrace();
            }
            
        }else{ //default
            try {
                
                DefaultImageHelper.write(image_to_save, prefix, fileName);
                
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    /**
     * write out details of image to XML file
     */
    private void outputMetaDataToXML(final String file_name, final int page, final PdfImageData pdf_images, final int i, final String image_name) {
        /**
         * save xml file with info
         */
        final float x1=pdf_images.getImageXCoord(i);
        final float y1=pdf_images.getImageYCoord(i);
        final float w=pdf_images.getImageWidth(i);
        final float h=pdf_images.getImageHeight(i);
        
        try{
            //create doc and set root
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final Document doc = db.newDocument();
            
            final Node root=doc.createElement("meta");
            doc.appendChild(root);
            
            //add comments
            final Node creation=doc.createComment("Created "+org.jpedal.utils.TimeNow.getShortTimeNow());
            doc.appendChild(creation);
            final Node info=doc.createComment("Pixel Location of image x1,y1,x2,y2");
            doc.appendChild(info);
            final Node moreInfo=doc.createComment("x1,y1 is top left corner origin is bottom left corner");
            doc.appendChild(moreInfo);
            
            //add location
            final Element location=doc.createElement("PAGELOCATION");
            location.setAttribute("x1", String.valueOf(x1));
            location.setAttribute("y1", String.valueOf((y1 + h)));
            location.setAttribute("x2", String.valueOf((x1 + w)));
            location.setAttribute("y2", String.valueOf(y1));
            root.appendChild(location);
            
            //add pdf file extracted from
            final Element fileName=doc.createElement("FILE");
            fileName.setAttribute("value",file_name);
            root.appendChild(fileName);
            
            //write out
            //use System.out for FileOutputStream to see on screen
            if(!isTest){
                final InputStream stylesheet = this.getClass().getResourceAsStream("/org/jpedal/examples/viewer/res/xmlstyle.xslt");
                
                final TransformerFactory transformerFactory = TransformerFactory.newInstance();
                final Transformer transformer = transformerFactory.newTransformer(new StreamSource(stylesheet));
                transformer.transform(new DOMSource(doc), new StreamResult(output_dir + page +separator+ image_name + ".xml"));
                
            }
            
        }catch(final Exception e){
            e.printStackTrace();
        }
    }
    
    //////////////////////////////////////////////////////////////////////////
    /**
     * main routine which checks for any files passed and runs the demo
     */
    public static void main( final String[] args )
    {
        if(outputMessages) {
            System.out.println( "Simple demo to extract images from a page" );
        }
        
        //set to default
        String file_name = test_file;
        boolean failed = false;
        
        //check user has passed us a filename and use default if none
        final int len=args.length;
        if (len == 0){
            System.out.println("Example can take 1 or 2 parameters");
            System.out.println("Value 1 is the file name or directory of PDF files to process");
            System.out.println("Value 2 is optional values of image type (jpeg,tiff,png). Default is png");
            System.exit(0);
        }else if(len == 1){
            file_name = args[0];
            System.out.println("file name="+file_name);
        }else if(len <3){
            
            //input
            file_name = args[0];
            
            if(outputMessages) {
                System.out.println("File :" + file_name);
            }
            
            for(int j=1;j<args.length;j++){
                final String value=args[j];
                
                {
                    final String in = value.toLowerCase();
                    if(in.equals("tif") || in.equals("tiff")) {
                        prefix="tif";
                    } else if(in.equals("png")) {
                        prefix="png";
                    } else{
                        failed=true;
                        System.out.println("value args not recognised as valid parameter.");
                        System.out.println("please enter \"tif\", \"tiff\" or \"png\".");
                    }
                }
            }
            
        }else {
            failed=true;
            System.out.println("too many arguments entered - run with no values to see defaults");
        }
        
        if(failed){
            StringBuilder arguments=new StringBuilder();
            for (final String arg : args) {
                arguments.append(arg).append('\n');
            }
            System.out.println("you entered:\n"+ arguments +"as the arguments");
        }
        
        //check file exists
        final File pdf_file = new File( file_name );
        
        //if file exists, open and get number of pages
        if(!pdf_file.exists())
        {
            System.out.println( "File " + file_name + " not found" );
        }
        new ExtractImages( file_name );
    }
    
    /**
     * @return Returns the output_dir.
     */
    @SuppressWarnings("UnusedDeclaration")
    public String getOutputDir() {
        return output_dir;
    }
    
    
}
