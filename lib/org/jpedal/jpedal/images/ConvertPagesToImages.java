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
 * ConvertPagesToImages.java
 * ---------------
 */

package org.jpedal.examples.images;

import org.jpedal.*;
import org.jpedal.color.ColorSpaces;
import org.jpedal.constants.PageInfo;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;
import org.jpedal.io.JAIHelper;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Element;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 *
 * <h2><b>Example to convert PDF to Buffered images which can then be saved to Tiff, PNG or JPEG.</b></h2>
 *
 * <p>It can run from jar directly using the command:
 * <br><b>java -cp libraries_needed org/jpedal/examples/images/ConvertPagesToImages pdfFilepath inputValues</b></p>
 *
 * <p>There is another example (org.jpedal.examples.images.ConvertPagesToHiResImages)
 * for producing higher res images of pages (but likely to be slower).</p>
 *
 * <p>Where inputValues is 1-5 values. Value 1 is the file name or directory of PDF files to process (all doube quotes if it contains any spaces)</p>
 * <p>4 optional values of:-</p>
 * <ul>
 * <li>image type (jpeg,tiff,png)</li>
 * <li>scaling (100 = full size)</li>
 * <li>password for protected file (or null) can also be added</li>
 * <li>output path (must end with / or \\ character)</li>
 * </ul>
 * <p><a href="http://www.idrsolutions.com/how-to-convert-pdf-files-to-image">See here for a list of code examples to convert images</a></p>
 */
public class ConvertPagesToImages{
    
    /**
     * show if image transparent
     */
    boolean isTransparent;
    
    /**output where we put files*/
    private String user_dir = System.getProperty("user.dir");
    
    /**use 96 dpi as default so pages correct size (72 will be smaller)*/
    private float pageScaling =1.33f;
    
    /**flag to show if we print messages*/
    public static boolean outputMessages;
    
    String output_dir;
    
    /**correct separator for OS */
    final String separator = System.getProperty("file.separator");
    
    /**the decoder object which decodes the pdf and returns a data object*/
    PdfDecoderInt decode_pdf;
    
    //type of image to save thumbnails
    private String format = "png";
    
    /** holding all creators that produce OCR pdf's */
    private final String[] ocr = {"TeleForm"};
    
    /**flag to show if using images at highest quality -switch on with command line flag Dhires*/
    private boolean useHiresImage;
    
    /**sample file which can be setup - substitute your own.
     * If a directory is given, all the files in the directory will be processed*/
    private static final String test_file = "/mnt/shared/sample_pdfs/general/World Factbook.pdf";
    
    /**used as part of test to limit pages to first 10*/
    public static boolean isTest;
    
    
    //used for testing
    public static boolean orderReversed;
    
    /**scaling to use - default is 100 percent*/
    private int scaling=100;
    
    /**file password or null*/
    private String password;
    
    //only used if between 0 and 1
    private float JPEGcompression=-1f;
    
    public ConvertPagesToImages(final String[] args, final PdfDecoderInt pdf) {
        
        this.decode_pdf=pdf;
        
        init(args);
    }
    
    /**
     * constructor to provide same functionality as main method
     *
     */
    public ConvertPagesToImages(final String[] args) {
        init(args);
    }
    
    private void init(final String[] args){
        
        //read all values passed in by user and setup
        final String file_name = setParams(args);
        
        //check file exists
        final File pdf_file = new File(file_name);
        
        //if file exists, open and get number of pages
        if (!pdf_file.exists()) {
            System.out.println("File " + pdf_file + " not found");
            System.out.println("May need full path");
            
            return;
        }
        
        //System.out.println("testing file="+file_name);
        extraction(file_name, output_dir);
        
    }
    
    private void extraction(String file_name, String output_dir) {
        
        //<start-server>
        //get any user set dpi
        final String hiresFlag = System.getProperty("org.jpedal.hires");
        if(org.jpedal.examples.viewer.Commands.hires || hiresFlag != null){
            useHiresImage=true;
        }
        //<end-server>
        
        this.output_dir=output_dir;
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
            
            if(!ConvertPagesToImages.isTest && output_dir==null) {
                output_dir=user_dir + "thumbnails" + separator;
            }
            
            decodeFile(file_name,output_dir);
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
            
            if(files!=null){
                /**now work through all pdf files*/
                for (final String file : files) {
                    
                    if (file.toLowerCase().endsWith(".pdf")) {
                        if (outputMessages) {
                            System.out.println(file_name + file);
                        }
                        
                        decodeFile(file_name + file, output_dir);
                    }
                }
            }
        }
        
        /**tell user*/
        if(outputMessages) {
            System.out.println("Thumbnails created");
        }
    }
    
    /**
     * routine to decode a file
     */
    private void decodeFile(final String file_name,String output_dir) {
        
        /**get just the name of the file without
         * the path to use as a sub-directory
         */
        
        String name = "demo"; //set a default just in case
        
        int pointer = file_name.lastIndexOf(separator);
        
        if(pointer==-1) {
            pointer = file_name.lastIndexOf('/');
        }
        
        if (pointer != -1){
            name = file_name.substring(pointer + 1, file_name.length() - 4);
        }else if((!ConvertPagesToImages.isTest)&&(file_name.toLowerCase().endsWith(".pdf"))){
            name=file_name.substring(0,file_name.length()-4);
        }
        
        //fix for odd files on Linux created when you view pages
        if(name.startsWith(".")) {
            return;
        }
        
        //create output dir for images
        if(output_dir==null) {
            output_dir = user_dir + "thumbnails" + separator ;
        }
        
        //PdfDecoder returns a PdfException if there is a problem
        try {
            if(decode_pdf==null) {
                decode_pdf = new PdfDecoderServer(true);
            }
            
            
            /**optional JAI code for faster rendering*/
            //org.jpedal.external.ImageHandler myExampleImageHandler=new org.jpedal.examples.handlers.ExampleImageDrawOnScreenHandler();
            //decode_pdf.addExternalHandler(myExampleImageHandler, Options.ImageHandler);
            
            
            /**/
            
            /**
             * font mappings
             */
            if(!isTest){
                
                //mappings for non-embedded fonts to use
                FontMappings.setFontReplacements();
                
            }
            
            if(useHiresImage) {
                decode_pdf.useHiResScreenDisplay(true);
            }
            
            //avoid breaking all my tests with code change!
            if(isTest) {
                pageScaling=1f;
            }
            
            //true as we are rendering page
            decode_pdf.setExtractionMode(0, pageScaling);
            //don't bother to extract text and images
            
            /**
             * open the file (and read metadata including pages in  file)
             */
            if (outputMessages) {
                System.out.println("Opening file :" + file_name);
            }
            
            
            if(password!=null) {
                decode_pdf.openPdfFile(file_name,password);
            } else {
                decode_pdf.openPdfFile(file_name);
                
                //<link><a name="shapeTracker" />
                /**
                 * code to track shapes
                 *
                 * //to run you will need to uncomment this block of code and TestShapeTracker() at bottom of class
                 * org.jpedal.external.ShapeTracker myShapeTracker=new TestShapeTracker();
                 * decode_pdf.addExternalHandler(myShapeTracker, org.jpedal.external.Options.ShapeTracker);
                 * /**/
            }
            
        } catch (final Exception e) {
            
            System.err.println("8.Exception " + e + " in pdf code in "+file_name);
        }
        
        /**
         * extract data from pdf (if allowed).
         */
        if(decode_pdf.isEncrypted() && !decode_pdf.isFileViewable()){
            //exit with error if not test
            if(!isTest) {
                throw new RuntimeException("Wrong password password used=>"+password+ '<');
            }
        }else if ((decode_pdf.isEncrypted()&&(!decode_pdf.isPasswordSupplied()))
                && (!decode_pdf.isExtractionAllowed())) {
            throw new RuntimeException("Extraction not allowed");
        } else {
            
            //<link><a name="separation" />
            /**
             * allow output to multiple images with different values on each
             *
             * Note we REMOVE shapes as it is a new feature and we do not want to break existing functions
             */
            final String separation=System.getProperty("org.jpedal.separation");
            if(separation!=null){
                
                Object[] sepValues= {7,"",Boolean.FALSE}; //default of normal
                if(separation.equals("all")){
                    sepValues=new Object[]{PdfDecoderServer.RENDERIMAGES,"image_and_shapes",Boolean.FALSE,
                        PdfDecoderServer.RENDERIMAGES + PdfDecoderServer.REMOVE_RENDERSHAPES,"image_without_shapes",Boolean.FALSE,
                        PdfDecoderServer.RENDERTEXT,"text_and_shapes",Boolean.TRUE,
                        7,"all",Boolean.FALSE,
                        PdfDecoderServer.RENDERTEXT + PdfDecoderServer.REMOVE_RENDERSHAPES,"text_without_shapes",Boolean.TRUE
                    };
                }
                
                final int sepCount =sepValues.length;
                for(int seps=0;seps<sepCount;seps += 3){
                    
                    decode_pdf.setRenderMode((Integer) sepValues[seps]);
                    extractPageAsImage(file_name, output_dir, name+ '_' +sepValues[seps+1], (Boolean) sepValues[seps + 2]); //boolean makes last transparent so we can see white text
                    
                }
                
            }else {
                //just get the page
                extractPageAsImage(file_name, output_dir, name, isTransparent);
            }
        }
        
        /**close the pdf file*/
        decode_pdf.closePdfFile();
        
    }
    
    private void extractPageAsImage(final String file_name, final String output_dir, final String name, final boolean isTransparent) {
        
        //create a directory if it doesn't exist
        final File output_path = new File(output_dir);
        if (!output_path.exists()) {
            output_path.mkdirs();
        }
        
        final String multiPageFlag=System.getProperty("org.jpedal.multipage_tiff");
        final boolean isSingleOutputFile=multiPageFlag!=null && multiPageFlag.toLowerCase().equals("true");
        
        //allow user to specify value
        final String rawJPEGComp=System.getProperty("org.jpedal.compression_jpeg");
        if(rawJPEGComp!=null){
            try{
                JPEGcompression=Float.parseFloat(rawJPEGComp);
            }catch(final Exception e){
                e.printStackTrace();
            }
            if(JPEGcompression<0 || JPEGcompression>1) {
                throw new RuntimeException("Invalid value for JPEG compression - must be between 0 and 1");
            }
            
        }
        
        final String tiffFlag=System.getProperty("org.jpedal.compress_tiff");
        final String jpgFlag=System.getProperty("org.jpedal.jpeg_dpi");
        final boolean compressTiffs = tiffFlag!=null && tiffFlag.toLowerCase().equals("true");
        
        if(JAIHelper.isJAIused()) {
            JAIHelper.confirmJAIOnClasspath();
        }
        
        //page range
        final int start = 1;
        int end = decode_pdf.getPageCount();

        //limit to 1st ten pages in testing
        if((end>10)&&(isTest)) {
            end=10;
        }
        
        /**
         * extract data from pdf and then write out the pages as images
         */
        
        if (outputMessages) {
            System.out.println("Thumbnails will be in  " + output_dir);
        }
        
        try {
            
            final BufferedImage[] multiPages = new BufferedImage[1 + (end-start)];
            
            if(orderReversed){
                for (int page = end; page >=start; page--) {
                    getPage(output_dir, name, isTransparent, isSingleOutputFile,rawJPEGComp, jpgFlag, compressTiffs, start, end,multiPages, page);
                }
            }else{
                for (int page = start; page < end + 1; page++) {
                    getPage(output_dir, name, isTransparent, isSingleOutputFile,rawJPEGComp, jpgFlag, compressTiffs, start, end,multiPages, page);
                }
            }
        } catch (final Exception e) {
            
            decode_pdf.closePdfFile();
            throw new RuntimeException("Exception " + e.getMessage()+" with thumbnails on File="+file_name);
        }
    }
    
    private void getPage(final String output_dir, final String name, final boolean isTransparent,
            final boolean isSingleOutputFile, final String rawJPEGComp, final String jpgFlag,
            final boolean compressTiffs, final int start, final int end,
            final BufferedImage[] multiPages, final int page) throws PdfException,
            IOException {
        { //read pages
            
            if (outputMessages  ) {
                System.out.println("Page " + page);
            }
            
            /**
             * create a name with zeros for if more than 9 pages appears in correct order
             */
            String pageAsString=String.valueOf(page);
            final String maxPageSize=String.valueOf(end);
            final int padding=maxPageSize.length()-pageAsString.length();
            for(int ii=0;ii<padding;ii++) {
                pageAsString='0'+pageAsString;
            }
            
            final String image_name;
            if(isSingleOutputFile) {
                image_name =name;
            } else {
                image_name =name+"_page_" + pageAsString;
            }
            
            /**
             * get PRODUCER and if OCR disable text printing
             */
            final PdfFileInformation currentFileInformation=decode_pdf.getFileInformationData();
            
            final String[] values=currentFileInformation.getFieldValues();
            final String[] fields=PdfFileInformation.getFieldNames();
            
            for(int i=0;i<fields.length;i++){
                
                if(fields[i].equals("Creator")){
                    
                    for (final String anOcr : ocr) {
                        
                        if (values[i].equals(anOcr)) {
                            
                            decode_pdf.setRenderMode(PdfDecoderServer.RENDERIMAGES);
                            
                        }
                    }
                }
            }
            
            /**
             * get the current page as a BufferedImage
             */
            BufferedImage image_to_save;
            if(!isTransparent) {
                image_to_save=decode_pdf.getPageAsImage(page);
            } else{ //use this if you want a transparent image
                image_to_save =decode_pdf.getPageAsTransparentImage(page);
                
                //<link><a name="jpegBlogArticle" />
                //java adds odd tint if you save this as JPEG which does not have transparency
                // so put as RGB on white background
                // (or save as PNG or TIFF which has transparency)
                // or just call decode_pdf.getPageAsImage(page)
                if(image_to_save!=null && format.toLowerCase().startsWith("jp")){
                    
                    final BufferedImage rawVersion=image_to_save;
                    
                    final int w=rawVersion.getWidth();
                    final int h=rawVersion.getHeight();
                    //blank canvas
                    image_to_save = new BufferedImage(w,h , BufferedImage.TYPE_INT_RGB);
                    
                    //
                    final Graphics2D g2 = image_to_save.createGraphics();
                    //white background
                    g2.setPaint(Color.WHITE);
                    g2.fillRect(0,0,w,h);
                    //paint on image
                    g2.drawImage(rawVersion, 0, 0,null);
                }
            }
            
            //<link><a name="grayscale" />
            //if just gray we can reduce memory usage by converting image to Grayscale
            
            /**
             * see what Colorspaces used and reduce image if appropriate
             * (only does Gray at present)
             *
             * null if JPedal unsure
             */
            final Iterator colorspacesUsed=decode_pdf.getPageInfo(PageInfo.COLORSPACES);
            
            int nextID;
            boolean isGrayOnly=colorspacesUsed!=null; //assume true and disprove
            while(colorspacesUsed!=null && colorspacesUsed.hasNext()){
                nextID= (Integer) (colorspacesUsed.next());
                
                if(nextID!= ColorSpaces.DeviceGray && nextID!=ColorSpaces.CalGray) {
                    isGrayOnly=false;
                }
            }
            
            //draw onto GRAY image to reduce colour depth
            //(converts ARGB to gray)
            if(isGrayOnly){
                final BufferedImage image_to_save2=new BufferedImage(image_to_save.getWidth(),image_to_save.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                image_to_save2.getGraphics().drawImage(image_to_save,0,0,null);
                image_to_save = image_to_save2;
            }
            
            //put image in array if multi-images
            if(isSingleOutputFile) {
                multiPages[page-start] = image_to_save;
            }
            
            
            if (image_to_save != null) {
                
                /**BufferedImage does not support any dpi concept. A higher dpi can be created
                 * using JAI to convert to a higher dpi image*/
                
                //shrink the page to 50% with graphics2D transformation
                //- add your own parameters as needed
                //you may want to replace null with a hints object if you
                //want to fine tune quality.
                
                /** example 1 biliniear scaling
                 * AffineTransform scale = new AffineTransform();
                 * scale.scale(.5, .5); //50% as a decimal
                 * AffineTransformOp scalingOp =new AffineTransformOp(scale, null);
                 * image_to_save =scalingOp.filter(image_to_save, null);
                 * 
                 */
                
                /** example 2 bicubic scaling - better quality but slower
                 * to preserve aspect ratio set newWidth or newHeight to -1*/
                
                /**allow user to specify maximum dimension for thumbnail*/
                final String maxDimensionAsString = System.getProperty("maxDimension");
                int maxDimension = -1;
                
                if(maxDimensionAsString != null) {
                    maxDimension = Integer.parseInt(maxDimensionAsString);
                }
                
                if(scaling!=100 || maxDimension != -1){
                    int newWidth=image_to_save.getWidth()*scaling/100;
                    int newHeight=image_to_save.getHeight()*scaling/100;
                    
                    final Image scaledImage;
                    if(maxDimension != -1 && (newWidth > maxDimension || newHeight > maxDimension)){
                        if(newWidth > newHeight){
                            newWidth = maxDimension;
                            scaledImage= image_to_save.getScaledInstance(newWidth,-1,BufferedImage.SCALE_SMOOTH);
                        } else {
                            newHeight = maxDimension;
                            scaledImage= image_to_save.getScaledInstance(-1,newHeight,BufferedImage.SCALE_SMOOTH);
                        }
                    } else {
                        scaledImage= image_to_save.getScaledInstance(newWidth,-1,BufferedImage.SCALE_SMOOTH);
                    }
                    
                    if(format.toLowerCase().startsWith("jp")) {
                        image_to_save = new BufferedImage(scaledImage.getWidth(null),scaledImage.getHeight(null) , BufferedImage.TYPE_INT_RGB);
                    } else {
                        image_to_save = new BufferedImage(scaledImage.getWidth(null),scaledImage.getHeight(null) , BufferedImage.TYPE_INT_ARGB);
                    }
                    
                    final Graphics2D g2 = image_to_save.createGraphics();
                    
                    g2.drawImage(scaledImage, 0, 0,null);
                }
                
                final String imageFormat = System.getProperty("org.jpedal.imageType");
                if(imageFormat!=null){
                    if(isNumber(imageFormat)){
                        final int iFormat = Integer.parseInt(imageFormat);
                        if(iFormat>-1 && iFormat<14){
                            final BufferedImage tempImage = new BufferedImage(image_to_save.getWidth(), image_to_save.getHeight(), iFormat);
                            final Graphics2D g = tempImage.createGraphics();
                            g.drawImage(image_to_save, null, null);
                            
                            image_to_save = tempImage;
                        }else{
                            System.err.println("Image Type is not valid. Value should be a digit between 0 - 13 based on the BufferedImage TYPE variables.");
                        }
                    }else{
                        System.err.println("Image Type provided is not an Integer. Value should be a digit between 0 - 13 based on the BufferedImage TYPE variables.");
                    }
                }
                
                if(JAIHelper.isJAIused() && format.startsWith("tif")){
                    
                    String outputFileName=null;
                    final boolean isLastPage=page == end;
                    
                    if(!isSingleOutputFile){
                        outputFileName=output_dir + pageAsString + image_name+".tif";
                    }else if(isLastPage){
                        outputFileName=output_dir + image_name+".tif";
                    }
                    
                    JAIHelper.saveAsTiff(compressTiffs, isSingleOutputFile, image_to_save, outputFileName, isLastPage, multiPages);
                    
                } else if ((jpgFlag != null || rawJPEGComp!=null) && format.startsWith("jp") && JAIHelper.isJAIused()) {
                    
                    saveAsJPEG(jpgFlag, image_to_save, JPEGcompression, new BufferedOutputStream(new FileOutputStream(output_dir + pageAsString + image_name + '.' + format)));
                    
                } else {
                    
                    //save image
                    decode_pdf.getObjectStore().saveStoredImage(
                            output_dir + pageAsString + image_name,
                            image_to_save,
                            true,
                            false,
                            format);
                }
            }
            
            //flush images in case we do more than 1 page so only contains
            //images from current page
            decode_pdf.flushObjectValues(true);
            //flush any text data read
            
        }
    }
    //////////////////////////////////////////////////////////////////////////
    /**
     * main routine which checks for any files passed and runs the demo
     *
     * Full details at http://files.idrsolutions.com/samplecode/org/jpedal/examples/images/ConvertPagesToImages.java.html
     */
    public static void main(final String[] args) {
        
        //long start=System.currentTimeMillis();
        
        System.out.println("Simple demo to extract images from a page");
        
        //check values first and exit with info if too many
        final int count=args.length;
        final boolean failed = count>4 || count==0;
        if(failed){
            
            if(count>0){
                System.out.println("too many arguments entered - run with no values to see defaults");
                
                StringBuilder arguments=new StringBuilder();
                for (final String arg : args) {
                    arguments.append(arg).append('\n');
                }
                System.out.println("you entered:\n"+ arguments +"as the arguments");
            }
            
            showCommandLineValues();
        }
        
        new ConvertPagesToImages(args);
        
        //System.out.println("Took="+(System.currentTimeMillis()-start)/1000);
    }
    
    private String setParams(final String[] args) {
        //set to default
        String file_name = test_file;
        
        //check user has passed us a filename and use default if none
        final int len=args.length;
        if (len == 0){
            showCommandLineValues();
        }else if(len == 1){
            file_name = args[0];
        }else if(len <6){
            
            //input
            file_name = args[0];
            
            for(int j=1;j<args.length;j++){
                final String value=args[j];
                final boolean isNumber=isNumber(value);
                
                if(isNumber){
                    try{
                        scaling=Integer.parseInt(value);
                    }catch(final Exception e){
                        throw new RuntimeException(value+" is not an integer "+e);
                    }
                }else{
                    final String in = value.toLowerCase();
                    if((in.equals("jpg"))||(in.equals("jpeg"))) {
                        format="jpg";
                    } else if(in.equals("tif")||in.equals("tiff")) {
                        format="tif";
                    } else if(in.equals("png")) {
                        format="png";
                    } else{
                        
                        //assume password if no / or \
                        if(value.endsWith("/") || value.endsWith("\\")) {
                            output_dir=value;
                        } else {
                            password=value;
                            
                            
                            //						failed=true;
                            //						System.out.println("value args not recognised as valid parameter.");
                            //						System.out.println("please enter \"jpg\", \"jpeg\", \"tif\", \"tiff\" or \"png\".");
                        }
                    }
                }
            }
        }
        return file_name;
    }
    
    static void showCommandLineValues() {
        System.out.println("Example can take 1-5 parameters");
        System.out.println("Value 1 is the file name or directory of PDF files to process");
        System.out.println("4 optional values of:-\nimage type (jpeg,tiff,png), \nscaling (100 = full size), \npassword for protected file (or null) can also be added ,\noutput path (must end with / or \\ character)");
        System.exit(0);
    }
    
    /**test to see if string or number*/
    private static boolean isNumber(final String value) {
        
        //assume true and see if proved wrong
        boolean isNumber=true;
        
        final int charCount=value.length();
        for(int i=0;i<charCount;i++){
            final char c=value.charAt(i);
            if((c<'0')|(c>'9')){
                isNumber=false;
                i=charCount;
            }
        }
        
        return isNumber;
    }
    
    /**
     * @return Returns the output_dir.
     */
    @SuppressWarnings("UnusedDeclaration")
    public String getOutputDir() {
        return output_dir;
    }
    
    //<link><a name="savejpeg" />
    private static void saveAsJPEG(final String jpgFlag, final BufferedImage image_to_save, final float JPEGcompression, final BufferedOutputStream fos) throws IOException {
        
        //useful documentation at http://docs.oracle.com/javase/7/docs/api/javax/imageio/metadata/doc-files/jpeg_metadata.html
        //useful example program at http://johnbokma.com/java/obtaining-image-metadata.html to output JPEG data
        
        //old jpeg class
        //com.sun.image.codec.jpeg.JPEGImageEncoder jpegEncoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(fos);
        //com.sun.image.codec.jpeg.JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(image_to_save);
        
        // Image writer
        final com.sun.imageio.plugins.jpeg.JPEGImageWriter imageWriter = (com.sun.imageio.plugins.jpeg.JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpeg").next();
        final ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
        imageWriter.setOutput(ios);
        
        //and metadata
        final IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image_to_save), null);
        
        if (jpgFlag != null){
            
            int dpi = 96;
            
            try {
                dpi = Integer.parseInt(jpgFlag);
            } catch (final Exception e) {
                e.printStackTrace();
            }
            
            //old metadata
            //jpegEncodeParam.setDensityUnit(com.sun.image.codec.jpeg.JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);
            //jpegEncodeParam.setXDensity(dpi);
            //jpegEncodeParam.setYDensity(dpi);
            
            //new metadata
            final Element tree = (Element) imageMetaData.getAsTree("javax_imageio_jpeg_image_1.0");
            final Element jfif = (Element)tree.getElementsByTagName("app0JFIF").item(0);
            jfif.setAttribute("Xdensity", Integer.toString(dpi));
            jfif.setAttribute("Ydensity", Integer.toString(dpi));
            
        }
        
        final JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
        if(JPEGcompression>=0 && JPEGcompression<=1f){
            
            //old compression
            //jpegEncodeParam.setQuality(JPEGcompression,false);
            
            // new Compression
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(JPEGcompression);
            
        }
        
        //old write and clean
        //jpegEncoder.encode(image_to_save, jpegEncodeParam);
        
        //new Write and clean up
        imageWriter.write(imageMetaData, new IIOImage(image_to_save, null, null), jpegParams);
        ios.close();
        imageWriter.dispose();
        
    }
    
}

/**
 * example code to get the Shapes as we parse the PDF file
 *
 * class TestShapeTracker implements org.jpedal.external.ShapeTracker {
 * @Override
 * public void addShape(int tokenNumber, int type, Shape currentShape, org.jpedal.color.PdfPaint nonstrokecolor, org.jpedal.color.PdfPaint strokecolor) {
 * 
 * //use this to see type
 * //Cmd.getCommandAsString(type);
 * 
 * //print out details
 * if(type==org.jpedal.parser.Cmd.S){ //use stroke color to draw line
 * System.out.println("-------Stroke-------PDF cmd");
 * System.out.println("tokenNumber="+tokenNumber+" "+currentShape.getBounds()+" stroke color="+strokecolor);
 * 
 * }else if(type==org.jpedal.parser.Cmd.F ){ //uses fill color to fill shape
 * System.out.println("-------Fill-------PDF cmd");
 * System.out.println("tokenNumber="+tokenNumber+" "+currentShape.getBounds()+" fill color="+nonstrokecolor);
 * 
 * }else{ //not yet implemented (probably B which is S and F combo)
 * System.out.println("Not yet added "+type);
 * System.out.println("tokenNumber="+tokenNumber+" "+currentShape.getBounds()+" type="+type);
 * 
 * }
 * }
 * }
 * /**/
