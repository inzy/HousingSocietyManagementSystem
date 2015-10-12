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
 * ExtractClippedImages.java
 * ---------------
 */


package org.jpedal.examples.images;

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import java.util.Arrays;
import org.jpedal.PdfDecoderServer;
import org.jpedal.PdfDecoderInt;
import org.jpedal.io.JAIHelper;
import org.jpedal.objects.PdfImageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;
import org.w3c.dom.Element;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import org.jpedal.color.ColorSpaces;

/**
 * <h2><b>Sample Code providing a Workflow Which Extracts Clipped Images and Places Versions
 * Scaled to Specific Heights.</b></h2>
 *
 * <p>It can be run straight from jar using:
 *
 * <br><b>java -cp libraries_needed org/jpedal/examples/ ExtractClippedImages $inputDir $processedDir $logFile h1 dir1 h2 dir2 ... hn dirn</b></p>
 *
 * <p>Values with SPACES but be surrounded by "" as in "This is one value".
 * <br>The values passed are:</p>
 *
 * <ul>
 * <li>$inputDir - directory containing files.</li>
 * <li>$processedDIr - directory to put files in.</li>
 * <li>$log - path and name of logfile.</li>
 * </ul>
 *
 * <p>Any number of h - height required in pixels as an integer for output (-1 means keep current size) dir1 - directory to write out images.</p>
 *
 * <p>So to create 3 versions of the image (one at original size, one at 100 and one at 50 pixels high), you would use:
 *
 * <br><b>java -cp libraries_needed org/jpedal/examples/images/ExtractClippedImages /export/files/ /export/processedFiles/ /logs/image.log -1 /output/raw/ 100 /output/medium/ 50 /output/thumbnail/</b></p>
 *
 * <p>Note image quality depends on the raw image in the original.</p>
 *
 * <p>This can be VERY memory intensive.</p>
 *
 * <p>Option boolean mergeImages=false; to merge overlapping images disabled by default.</p>
 *
 * <p><a href="http://www.idrsolutions.com/how-to-extract-images-from-pdf-files/">See our support pages for more information on extracting images.</a></p>
 *
 */
public class ExtractClippedImages
{
    
    /**flag to show if we print messages*/
    public static boolean outputMessages = true;
    
    /**directory to place files once decoded*/
    private static String processed_dir="processed";
    
    /**used for regression tests by IDR solutions*/
    public static boolean testing;
    
    /**rootDir containing files*/
    private static String inputDir="";
    
    /**number of output directories*/
    private static int outputCount;
    
    /**sizes to output at -1 means unchanged*/
    private static float[] outputSizes;
    
    /**target directories for files*/
    private static String[] outputDirectories;
    
    /**the decoder object which decodes the pdf and returns a data object*/
    PdfDecoderInt decode_pdf;
    
    /**correct separator for OS */
    private static final String separator = System.getProperty( "file.separator" );
    
    /**location output files written to*/
    private static final String output_dir="clippedImages";
    
    /**type of image to save*/
    private String imageType = "tiff";
    
    /**background colour to add to JPEG*/
    private static final Color backgroundColor=Color.WHITE;
    
    /**test method to extract the images from a directory*/
    public ExtractClippedImages( final String rootDir, final String outDir )
    {
        /**
         * setup messages
         */
        try{
            Messages.setBundle(ResourceBundle.getBundle("org.jpedal.international.messages"));
        }catch(final Exception e){
            e.printStackTrace();
            System.out.println("Exception loading resource bundle");
        }
        
        /**
         * setup tests
         */
        /**read output values*/
        final String[] args={"500","high"};
        outputCount=(args.length)/2;
        
        /**read and create output directories*/
        outputSizes=new float[outputCount];
        outputDirectories=new String[outputCount];
        for(int i=0;i<outputCount;i++){
            
            try{
                outputSizes[i]=Float.parseFloat(args[(i*2)]);
            }catch(final Exception e){
                exit("Exception "+e+" reading integer "+args[(i*2)]);
            }
            
            try{
                outputDirectories[i]=outDir;//args[1+(i*2)];
                
                /**make sure has separator*/
                if((!outputDirectories[i].endsWith("\\"))&&(!outputDirectories[i].endsWith("/"))) {
                    outputDirectories[i] += separator;
                }
                
                final File dir=new File(outputDirectories[i]);
                if(!dir.exists()) {
                    dir.mkdirs();
                }
            }catch(final Exception e){
                exit("Exception "+e+" with directory "+args[4+(i*2)]);
            }
            
        }
        
        /**create output*/
        processFiles( rootDir );
        
    }
    
    /**example method to extract the images from a directory*/
    public ExtractClippedImages( final String rootDir )
    {
        
        final String newImageType=System.getProperty("org.jpedal.imageType");
        if(newImageType!=null){
            imageType=newImageType.toLowerCase();
            System.out.println(imageType);
            
            if(imageType.equals("tif") || imageType.equals("tiff") ) {
                imageType="tiff";
            } else if(imageType.equals("jpg")|| imageType.equals("jpeg") ) {
                imageType="jpg";
            } else if(!imageType.equals("png")) {
                exit("Imagetype "+imageType+" not supported");
            }
        }
        else {
            imageType="png";
        }
        
        processFiles( rootDir );
        
    }
    
    /**example method to extract the images from a directory*/
    private void processFiles( String rootDir )
    {
        
        /**make sure name ends with a deliminator for correct path later*/
        if(!processed_dir.endsWith(separator)) {
            processed_dir += separator;
        }
        
        /**allow for single PDF*/
        if (rootDir.toLowerCase().endsWith(".pdf")) {
            
            if (outputMessages) {
                System.out.println(rootDir);
            }
            
            
            /**
             * decode the file
             */
            decode(rootDir);
            
        }else{
            
            /**make sure name ends with a deliminator for correct path later*/
            if((!rootDir.endsWith("\\"))&&(!rootDir.endsWith("/"))) {
                rootDir += separator;
            }
            
            /**check it is a directory*/
            final File testDir=new File(rootDir);
            if(!testDir.isDirectory()){
                exit("No root directory "+rootDir);
            }
            
            /**
             * get list of files
             */
            String[] files = null;
            
            try{
                final File inputFiles = new File( rootDir );
                //System.out.println(inputFiles.getAbsolutePath());
                if(!inputFiles.isDirectory()){
                    System.err.println(rootDir+" is not a directory. Exiting program");
                    
                }else {
                    files = inputFiles.list();
                }
            }catch( final Exception ee ){
                exit( "Exception trying to access file " + ee.getMessage() );
            }
            
            /**now work through all pdf files*/
            for (final String file : files) {
                
                if (file.toLowerCase().endsWith(".pdf")) {
                    
                    if (outputMessages) {
                        System.out.println(rootDir + file);
                    }
                    
                    
                    /**
                     * decode the file
                     */
                    decode(rootDir + file);
                    
                    /**archive the file*/
                    final File currentFile = new File(rootDir + file);
                    currentFile.renameTo(new File(processed_dir + file));
                }
            }
        }
    }
    
    /**
     * exit routine for code
     */
    private static void exit(final String string) {
        
        System.out.println("Exit message "+string);
        LogWriter.writeLog("Exit message "+string);
        
    }
    
    /**
     * routine to decode a PDF file
     */
    private void decode(final String file_name){
        
        /**setup the output direct
         * /**get just the name of the file without
         * the path to use as a sub-directory or .pdf
         */
        
        LogWriter.writeLog("==================");
        LogWriter.writeLog("File "+file_name);
        
        //PdfDecoder returns a PdfException if there is a problem
        try{
            
            decode_pdf = new PdfDecoderServer( false );
            decode_pdf.setExtractionMode(PdfDecoderServer.FINALIMAGES+PdfDecoderServer.CLIPPEDIMAGES,1);
            
            //as highres flag is now true by default and
            //clipped code assumes it is false we now need
            //to set it.
            decode_pdf.useHiResScreenDisplay(false);
            
            /** open the file (and read metadata including pages in  file)*/
            decode_pdf.openPdfFile( file_name );
            
        }catch( final Exception e ){
            exit(Messages.getMessage("PdfViewerError.Exception")+ ' ' +
                    e + ' ' +Messages.getMessage("PdfViewerError.OpeningPdfFiles"));
        }
        
        /**
         * extract data from pdf (if allowed).
         */
        if ((decode_pdf.isEncrypted()&&(!decode_pdf.isPasswordSupplied()))&&(!decode_pdf.isExtractionAllowed())) {
            exit(Messages.getMessage("PdfViewerError.EncryptedNotSupported"));
        }else{
            
            //page range
            final int start = 1;
            final int end =decode_pdf.getPageCount();

            try{
                for( int page = start;page < end + 1;page++ ){ //read pages
                    
                    LogWriter.writeLog(Messages.getMessage("PdfViewerDecoding.page")+ ' ' +page);
                    
                    //decode the page
                    decode_pdf.decodePage( page );
                    
                    //get the PdfImages object which now holds the images.
                    //binary data is stored in a temp directory and we hold the
                    //image name and other info in this object
                    final PdfImageData pdf_images = decode_pdf.getPdfImageData();
                    
                    //image count (note image 1 is item 0, so any loop runs 0 to count-1)
                    final int image_count = pdf_images.getImageCount();
                    
                    //tell user
                    if( image_count > 0 ) {
                        LogWriter.writeLog("page"+ ' ' +page+"contains "+image_count+ " images");
                    } else {
                        LogWriter.writeLog("No bitmapped images on page "+page);
                    }
                    
                    LogWriter.writeLog("Writing out images");
                    
                    //location of images
                    final float[] x1=new float[image_count];
                    final float[] y1=new float[image_count];
                    final float[] w=new float[image_count];
                    final float[] h=new float[image_count];

                    //used to merge images
                    final float[] rawX1=new float[image_count];
                    final float[] rawY1=new float[image_count];
                    final float[] rawH=new float[image_count];

                    final String[] image_name=new String[image_count];
                    final BufferedImage[] image=new BufferedImage[image_count];
                    
                    final boolean[] isMerged=new boolean[image_count];
                    
                    //work through and get each image details
                    for( int i = 0;i < image_count;i++ ){
                        
                        image_name[i] =pdf_images.getImageName( i );
                        
                        //we need some duplicates as we update some values on merge but still need originals at end
                        //so easiest just to store
                        x1[i]=pdf_images.getImageXCoord(i);
                        rawX1[i]=pdf_images.getImageXCoord(i);
                        y1[i]=pdf_images.getImageYCoord(i);
                        rawY1[i]=pdf_images.getImageYCoord(i);
                        w[i]=pdf_images.getImageWidth(i);
                        h[i]=pdf_images.getImageHeight(i);
                        rawH[i]=pdf_images.getImageHeight(i);
                        
                        image[i] =decode_pdf.getObjectStore().loadStoredImage(  "CLIP_"+image_name[i] );
                        
                    }
                    
                    //merge overlapping images
                    final boolean mergeImages=true;
                    if(mergeImages){
                        boolean imagesMerged=true;
                        final boolean[] isUsed=new boolean[image_count];
                        final ArrayList[] imagesUsed=new ArrayList[image_count];
                        
                        //get list of images to merge for each block
                        //we repeat from start each time as areas grow
                        while(imagesMerged){
                            
                            //if no overlaps found we will exit, otherwise repeat from start as sizes changed
                            imagesMerged=false;
                            
                            //for each image
                            for( int i = 0;i < image_count;i++ ){
                                
                                //compare against all others
                                for( int i2 = 0;i2 < image_count;i2++ ){
                                    
                                    if(i==i2) {
                                        continue;
                                    }
                                    
                                    /**
                                     * look for overlap
                                     */
                                    if(!isUsed[i] && !isUsed[i2] && image[i]!=null && image[i2]!=null && x1[i]>=x1[i2]&& x1[i]<=(x1[i2]+w[i2]) && y1[i]>=y1[i2]&& y1[i]<=(y1[i2]+h[i2])){
                                        
                                        //System.out.println("\n------Merging------");
                                        //System.out.println(i2+" "+x1[i2]+" "+y1[i2]+" "+(x1[i2]+w[i2])+" "+(y1[i2]+h[i2])+" "+image[i2]);
                                        //System.out.println(i+" "+x1[i]+" "+y1[i]+" "+(x1[i]+w[i])+" "+(y1[i]+h[i]+" "+image[i]));
                                        //work out the new combined size
                                        final float newX=x1[i2];
                                        final float newY=y1[i2];
                                        float newX2=x1[i]+w[i];
                                        final float altNewX2=x1[i2]+w[i2];
                                        if(newX2<altNewX2) {
                                            newX2=altNewX2;
                                        }
                                        float newY2=y1[i]+h[i];
                                        final float altNewY2=y1[i2]+h[i2];
                                        if(newY2<altNewY2) {
                                            newY2=altNewY2;
                                        }
                                        final float newW=newX2-newX;
                                        final float newH=newY2-newY;
                                        
                                        //System.out.println("new size ="+newX+" "+newY+" "+newW+" "+newH);
                                        x1[i2]=newX;
                                        y1[i2]=newY;
                                        w[i2]=newW;
                                        h[i2]=newH;
                                        isMerged[i2]=true;
                                        
                                        if(imagesUsed[i2]==null){
                                            imagesUsed[i2]=new ArrayList<Integer>(image_count);
                                            
                                            imagesUsed[i2].add(i2);
                                            
                                        }
                                        
                                        imagesUsed[i2].add(i);
                                        
                                        isUsed[i]=true;
                                        
                                        //merge any items attached to this image
                                        if(imagesUsed[i]!=null){
                                            imagesUsed[i2].addAll(Arrays.asList(imagesUsed[i].toArray()));
                                            
                                            isMerged[i]=false;
                                            imagesUsed[i]=null;
                                            
                                        }
                                        
                                        //restart
                                        imagesMerged=true;
                                        i=image_count;
                                        i2=image_count;
                                    }
                                }
                            }
                        }
                        
                        //now put together in correct order
                        for(int i=0;i<image_count;i++){
                            
                            if(imagesUsed[i]!=null){
                                
                                final float newX=x1[i];
                                final float newY=y1[i];
                                //float newH=h[i];
                                /**
                                 * put both images together
                                 */
                                final BufferedImage combinedImage=new BufferedImage((int)w[i],(int)h[i],BufferedImage.TYPE_INT_ARGB);
                                
                                final Graphics2D g2=combinedImage.createGraphics();
                                
                                //improve image quality
                                g2.setRenderingHints(ColorSpaces.hints);
                                
                                Collections.sort(imagesUsed[i]);
                                
                                int lastImage=-1;
                                for(final Object currentImage: imagesUsed[i].toArray()){
                                    
                                    final int i2=(Integer)currentImage;
                                    
                                    if(lastImage!=i2){ //avoid duplicates
                                        
                                        final int finalX=(int) (x1[i2] - newX);
                                        final int finalY=combinedImage.getHeight() +(int) (-y1[i2] - newY-h[i2]);
                                        
                                        g2.drawImage(image[i2], finalX, finalY,(int)w[i2],(int)h[i2], null);
                                        
                                        lastImage=i2;
                                        image[i2]=null;
                                    }
                                }
                                
                                image[i]=combinedImage;
                            }
                        }
                    }
                    
                    //save each image
                    for( int i = 0;i < image_count;i++ ){
                        
                        if(image[i]!=null){
                            //if(isMerged[i]){  //uncomment if you want just merged images
                            generateVersions(file_name, page, "<PAGELOCATION x1=\"" + x1[i] + "\" "
                                    + "y1=\"" + (y1[i] + h[i]) + "\" "
                                    + "x2=\"" + (x1[i] + w[i]) + "\" "
                                    + "y2=\"" + (y1[i]) + "\" />\n", image_name[i], image[i], i);
                            //}
                        }
                    }
                    
                    //flush images in case we do more than 1 page so only contains
                    //images from current page
                    decode_pdf.flushObjectValues(true);
                    
                }
            }catch( final Exception e ){
                decode_pdf.closePdfFile();
                LogWriter.writeLog( "Exception " + e.getMessage() );
                
                e.printStackTrace();
            }
        }
        
        /**close the pdf file*/
        decode_pdf.closePdfFile();
        
    }
    
    private void generateVersions(final String file_name, final int page, final String s, final String image_name, final BufferedImage bufferedImage, final int i) {
        
        for(int versions=0;versions<outputCount;versions++){
            try{
                //find out format image was saved in
                //String type = decode_pdf.getObjectStore().getImageType( image_name );
                
                //load image (converted to rgb)
                BufferedImage image_to_save = bufferedImage;
                if(image_to_save==null) {
                    continue;
                }
                
                int index = file_name.lastIndexOf('\\');
                if(index==-1) {
                    index = file_name.lastIndexOf('/');
                }
                if(index==-1) {
                    index=0;
                }
                final String nameToUse = file_name.substring(index,file_name.length()-4);
                final String outputName=outputDirectories[versions]+nameToUse + '_' + page + '_' +i;
                
                float scaling=1;
                
                final int newHeight=image_to_save.getHeight();
                
                //scale
                if(outputSizes[versions]>0){
                    
                    scaling=outputSizes[versions]/newHeight;
                    
                    if(scaling>1){
                        scaling=1;
                    }else{
                        
                        final Image scaledImage= image_to_save.getScaledInstance(-1,(int)outputSizes[versions],BufferedImage.SCALE_SMOOTH);
                        
                        image_to_save = new BufferedImage(scaledImage.getWidth(null),scaledImage.getHeight(null) , BufferedImage.TYPE_INT_ARGB);
                        
                        final Graphics2D g2 =image_to_save.createGraphics();
                        
                        g2.drawImage(scaledImage, 0, 0,null);
                        //ImageIO.write((RenderedImage) scaledImage,"PNG",new File(outputName));
                        
                        
                    }
                }
                
                final String tiffFlag=System.getProperty("org.jpedal.compress_tiff");
                final String jpgFlag=System.getProperty("org.jpedal.jpeg_dpi");
                final boolean compressTiffs = tiffFlag!=null;
                
                //if(compressTiffs)
                JAIHelper.confirmJAIOnClasspath();
                
                //no transparency on JPEG so give background and draw on
                if(imageType.startsWith("jp")){
                    
                    final int iw=image_to_save.getWidth();
                    final int ih=image_to_save.getHeight();
                    final BufferedImage background=new BufferedImage(iw,ih, BufferedImage.TYPE_INT_RGB);
                    
                    final Graphics2D g2=(Graphics2D)background.getGraphics();
                    g2.setPaint(backgroundColor);
                    g2.fillRect(0,0,iw,ih);
                    
                    g2.drawImage(image_to_save,0,0,null);
                    image_to_save= background;
                    
                }
                
                if(testing){ //used in regression tests
                    decode_pdf.getObjectStore().saveStoredImage( outputName, image_to_save, true, false, imageType );
                }else if(JAIHelper.isJAIused() && imageType.startsWith("tif")){
                    
                    LogWriter.writeLog("Saving image with JAI " + outputName + '.' + imageType);
                    
                    final FileOutputStream os = new FileOutputStream(outputName+".tif");
                    
                    com.sun.media.jai.codec.TIFFEncodeParam params = null;
                    
                    if(compressTiffs){
                        params = new com.sun.media.jai.codec.TIFFEncodeParam();
                        params.setCompression(com.sun.media.jai.codec.TIFFEncodeParam.COMPRESSION_DEFLATE);
                    }
                    
                    javax.media.jai.JAI.create("encode", image_to_save, os, "TIFF", params);
                    
                    os.flush();
                    os.close();
                    
                } else if (jpgFlag != null && imageType.startsWith("jp") && JAIHelper.isJAIused()) {
                    
                    saveAsJPEG(jpgFlag, image_to_save, 1, new FileOutputStream(output_dir + page + image_name + '.' + imageType));
                    
                } else{
                    
                    //save image
                    LogWriter.writeLog("Saving image "+outputName+ '.'+imageType);
                    final BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(new File(outputName+ '.'+imageType)));
                    ImageIO.write(image_to_save, imageType, bos);
                    bos.flush();
                    bos.close();
                    //decode_pdf.getObjectStore().saveStoredImage( outputName, image_to_save, true, false, imageType );
                }
                //save an xml file with details
                /**
                 * output the data
                 */
                //LogWriter.writeLog( "Writing out "+(outputName + ".xml"));
                final OutputStreamWriter output_stream = new OutputStreamWriter(new FileOutputStream(outputName + ".xml"),"UTF-8");
                
                output_stream.write(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                output_stream.write(
                        "<!-- Pixel Location of image x1,y1,x2,y2\n");
                output_stream.write("(x1,y1 is top left corner)\n");
                output_stream.write(
                        "(origin is bottom left corner)  -->\n");
                output_stream.write("\n\n<META>\n");
                output_stream.write(s);
                output_stream.write("<FILE>"+file_name+"</FILE>\n");
                output_stream.write("<ORIGINALHEIGHT>"+newHeight+"</ORIGINALHEIGHT>\n");
                output_stream.write("<SCALEDHEIGHT>"+image_to_save.getHeight()+"</SCALEDHEIGHT>\n");
                output_stream.write("<SCALING>"+scaling+"</SCALING>\n");
                output_stream.write("</META>\n");
                output_stream.close();
            }catch( final Exception ee ){
                LogWriter.writeLog( "Exception " + ee + " in extracting images" );
            }
        }
    }
    
    /**
     * @return Returns the output_dir.
     */
    public static String getOutputDir() {
        return output_dir;
    }
    
    /**
     * main routine which checks for any files passed and runs the demo
     */
    public static void main( final String[] args )
    {
        
        final long start=System.currentTimeMillis();
        
        Messages.setBundle(ResourceBundle.getBundle("org.jpedal.international.messages"));
        
        if(outputMessages) {
            System.out.println( "Simple demo to extract images from a page at various heights" );
        }
        
        /**exit and report if wrong number of values*/
        if((args.length >= 5) && ((args.length % 2) == 1)) {
            
            LogWriter.writeLog("Values read");
            LogWriter.writeLog("inputDir="+args[0]);
            LogWriter.writeLog("processedDir="+args[1]);
            LogWriter.writeLog("logFile="+args[2]);
            LogWriter.writeLog("Directory and height pair values"+args[3]+" <> "+args[4]+ '<');
            
            /**count output values*/
            outputCount = (args.length-3) / 2;
            
            for(int i=0; i<outputCount; i++) {
                LogWriter.writeLog(args[i + 3]);
                if(((i % 2) == 0) && (!args[i + 3].matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+"))){
                        exit("Invalid value: " + args[i+3]);
                }
            }
            
        }
        else if(((args.length - 3) % 2) == 1) {
            exit("Value/Directory pairs invalid");
        }
        else {
            System.out.println("Requires");
            System.out.println("inputDir processedDir logFile");
            System.out.println("height Directory (as many pairs as you like)");
            exit( "Not enough parameters passed to software" );
        }
        
        /**read path values*/
        inputDir =args[0];
        processed_dir=args[1];
        
        final String logging=System.getProperty("org.jpedal.logging");
        if(logging!=null && logging.toLowerCase().equals("true")){
            LogWriter.log_name=args[2];
            LogWriter.setupLogFile("");
        }
        
        /**check input directory exists*/
        final File pdf_file = new File( inputDir );
        
        /**check processed exists and create if not*/
        final File processedDir=new File(processed_dir);
        if(!processedDir.exists()) {
            processedDir.mkdirs();
        }
        
        /**if dir exists, open and get number of pages*/
        if(!pdf_file.exists()) {
            exit( "Directory " + inputDir + " not found" );
        }
        
        /**read and create output directories*/
        outputSizes=new float[outputCount];
        outputDirectories=new String[outputCount];
        for(int i=0;i<outputCount;i++){
            
            try{
                outputSizes[i]=Float.parseFloat(args[3+(i*2)]);
            }catch(final Exception e){
                exit("Exception "+e+" reading integer "+args[3+(i*2)]);
            }
            
            try{
                outputDirectories[i]=args[4+(i*2)];
                
                /**make sure has separator*/
                if((!outputDirectories[i].endsWith("\\"))&&(!outputDirectories[i].endsWith("/"))) {
                    outputDirectories[i] += separator;
                }
                
                final File dir=new File(outputDirectories[i]);
                if(!dir.exists()) {
                    dir.mkdirs();
                }
            }catch(final Exception e){
                exit("Exception "+e+" with directory "+args[4+(i*2)]);
            }
            
        }
        
        new ExtractClippedImages( inputDir );
        
        LogWriter.writeLog("Process completed");
        
        final long end=System.currentTimeMillis();
        
        System.out.println("Took "+(end-start)/1000+" seconds");
    }
    
    private static void saveAsJPEG(final String jpgFlag, final BufferedImage image_to_save, final float JPEGcompression, final FileOutputStream fos) throws IOException {
        
        //useful documentation at http://docs.oracle.com/javase/7/docs/api/javax/imageio/metadata/doc-files/jpeg_metadata.html
        //useful example program at http://johnbokma.com/java/obtaining-image-metadata.html to output JPEG data
        
        //old jpeg class
        //com.sun.image.codec.jpeg.JPEGImageEncoder jpegEncoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(fos);
        //com.sun.image.codec.jpeg.JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(image_to_save);
        
        // Image writer
        final JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO.getImageWritersBySuffix("jpeg").next();
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