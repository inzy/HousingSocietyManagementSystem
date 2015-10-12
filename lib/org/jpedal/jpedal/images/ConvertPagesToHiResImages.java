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
 * ConvertPagesToHiResImages.java
 * ---------------
 */

package org.jpedal.examples.images;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import org.jpedal.PdfDecoderServer;
import org.jpedal.PdfDecoderInt;
import org.jpedal.color.ColorSpaces;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.JAIHelper;
import org.jpedal.constants.JPedalSettings;
import org.jpedal.constants.PageInfo;
import org.w3c.dom.Element;

/**
 * <h2>Example to Convert PDF to Highest Possible Quality Images</h2>
 *
 * <p>Useful blog article at http://blog.idrsolutions.com/2009/07/pdf-to-image-quality/</p>
 *
 * <p>It can run from jar directly using the command :
 *
 * <br><b>java -cp libraries_needed org/jpedal/examples/images/ConvertPagesToHiResImages pdfFilepath inputValues</b></p>
 * <p>Also consider org.jpedal.examples.images.ConvertPagesToImages for faster conversion if quality isn't important</p>
 *
 * <p>Where inputValues is 2 values :-</p>
 *<ul>
 * <li>First Parameter:	The full path including the name and extension of the target PDF file.</li>
 * <li>Second Parameter:	The output image file type. Choices are tif, jpg and png.</li>
 *</ul>
 * <p><a href="http://javadoc.idrsolutions.com/org/jpedal/constants/JPedalSettings.html"> Also see here for settings to customise the image conversion.</a></p>
 *
 * <p><a href="http://www.idrsolutions.com/how-to-convert-pdf-files-to-image">See here for a list of code examples to convert images.</a></p>
 */
public class ConvertPagesToHiResImages {
    
    private static boolean debug;
    
    /**correct separator for OS */
    static final String separator = System.getProperty( "file.separator" );
    
    //only used if between 0 and 1
    private float JPEGcompression=-1f;
    
    public static void main(final String[] args) throws Exception {
        
        //<link><a name="hiResThumbnailExtr" />
        /*
        * Change these variables
        */
        final String fileType;
        String pdfFile;

        if(args!=null && args.length>1){
            
            pdfFile = args[0];
            fileType= args[1];
            
            //open the file
            if (!pdfFile.startsWith(".") && pdfFile.toLowerCase().endsWith(".pdf") && (fileType.equals("jpg") || fileType.equals("jpeg") || fileType.equals("png") || fileType.equals("tiff") || fileType.equals("tif"))){
                new ConvertPagesToHiResImages(fileType, pdfFile);
            }else{ //open the directory
                
                final File testDir=new File(pdfFile);
                
                if(testDir.isDirectory()){
                    
                    /**
                     * get list of files and check directory
                     */
                    
                    String[] files = null;
                    final File inputFiles;
                    
                    /**make sure name ends with a deliminator for correct path later*/
                    if (!pdfFile.endsWith(separator)) {
                        pdfFile += separator;
                    }
                    
                    try {
                        inputFiles = new File(pdfFile);
                        
                        files = inputFiles.list();
                    } catch (final Exception ee) {
                        LogWriter.writeLog("Exception trying to access file " + ee.getMessage());
                    }
                    
                    /**now work through all pdf files*/
                    for (final String file : files) {
                        
                        if (file.toLowerCase().endsWith(".pdf")) {
                            new ConvertPagesToHiResImages(fileType, pdfFile + file);
                        }
                    }
                }else {
                    System.out.println("The file to be processed has to be a pdf and the output filetype can only be jpg,png or tiff");
                }
            }
        }else{
            System.out.println("Not enough arguments passed in! Usage: \"C:\\examples\\1.pdf\" \"jpg\"");
        }
    }
    
    /**
     * main constructor to convert PDF to img
     * @param fileType
     * @param pdfFile
     * @throws Exception
     */
    public ConvertPagesToHiResImages(final String fileType, final String pdfFile) throws Exception {
        
        final long startTime=System.currentTimeMillis();
        
        final String outputPath = pdfFile.substring(0, pdfFile.toLowerCase().indexOf(".pdf")) + separator;
        final File outputPathFile = new File(outputPath);
        if ((!outputPathFile.exists() || !outputPathFile.isDirectory()) && ((!outputPathFile.mkdirs()) && debug)  ) {
                    System.err.println("Can't create directory " + outputPath);  
        }
        
        //PdfDecoder object provides the conversion
        final PdfDecoderInt decoder = new PdfDecoderServer(true);
        
        //mappings for non-embedded fonts to use
        FontMappings.setFontReplacements();
        
        decoder.openPdfFile(pdfFile);
        
        /**
         * this process is very flaxible to we create a Map and pass in values to select what sort
         * of results we want. There is a choice between methods used and image size. Larger images use more
         * memory and are slower but look better
         */
        final Map mapValues = new HashMap();
        
        /** USEFUL OPTIONS*/
        //do not scale above this figure
        mapValues.put(JPedalSettings.EXTRACT_AT_BEST_QUALITY_MAXSCALING, 2);
        
        //alternatively secify a page size (aspect ratio preserved so will do best fit)
        //set a page size (JPedal will put best fit to this)
        mapValues.put(JPedalSettings.EXTRACT_AT_PAGE_SIZE, new String[]{"2000","1600"});
        
        //which takes priority (default is false)
        mapValues.put(JPedalSettings.PAGE_SIZE_OVERRIDES_IMAGE, Boolean.TRUE);
        
        PdfDecoderServer.modifyJPedalParameters(mapValues);
        
        if(debug) {
            System.out.println("pdf : " + pdfFile);
        }
        
        try{
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
                    
                    decoder.setRenderMode((Integer) sepValues[seps]);
                    extractPageAsImage(fileType, outputPath, decoder,"_"+sepValues[seps+1], (Boolean) sepValues[seps + 2]); //boolean makes last transparent so we can see white text
                }
                
            }else {
                //just get the page
                extractPageAsImage(fileType, outputPath, decoder,"",false);
            }
            
        } finally {
            
            decoder.closePdfFile();
        }
        System.out.println("time="+(System.currentTimeMillis()-startTime)/1000);
        
        
    }
    
    /**
     * convenience method to get a page as a BufferedImage quickly
     * - for bulk conversion, use the other methods
     */
    @SuppressWarnings("UnusedDeclaration")
    public static BufferedImage getHiresPage(final int pageNo, final int scaling, final String pdfFile){
        
        BufferedImage imageToSave = null;
        
        final PdfDecoderInt decoder = new PdfDecoderServer(true);
        
        try{
            //mappings for non-embedded fonts to use
            FontMappings.setFontReplacements();
            
            decoder.openPdfFile(pdfFile);
            
            final PdfPageData pageData = decoder.getPdfPageData();
            final int width=scaling*pageData.getCropBoxWidth(pageNo);
            final int height=scaling*pageData.getCropBoxHeight(pageNo);
            
            final Map mapValues = new HashMap();
            
            /** USEFUL OPTIONS*/
            //do not scale above this figure
            mapValues.put(JPedalSettings.EXTRACT_AT_BEST_QUALITY_MAXSCALING, 2);
            
            //alternatively secify a page size (aspect ratio preserved so will do best fit)
            //set a page size (JPedal will put best fit to this)
            mapValues.put(JPedalSettings.EXTRACT_AT_PAGE_SIZE, new String[]{String.valueOf(width),String.valueOf(height)});
            
            //which takes priority (default is false)
            mapValues.put(JPedalSettings.PAGE_SIZE_OVERRIDES_IMAGE, Boolean.TRUE);
            
            PdfDecoderServer.modifyJPedalParameters(mapValues);
            
            imageToSave = decoder.getPageAsHiRes(pageNo,null,false);
            
        } catch (final PdfException e) {
            e.printStackTrace();
        } finally {
            
            decoder.closePdfFile();
        }
        return imageToSave;
    }
    
    /**
     * actual conversion of a PDF page into an image
     * @param fileType
     * @param outputPath
     * @param decoder
     * @param prefix
     * @param isTransparent
     * @throws PdfException
     * @throws IOException
     */
    private void extractPageAsImage(final String fileType, final String outputPath, final PdfDecoderInt decoder, final String prefix, final boolean isTransparent) throws PdfException, IOException {
        
        
        //page range
        final int start=1;
        final int end=decoder.getPageCount();

        //container if the user is creating a multi-image tiff
        final BufferedImage[] multiPages = new BufferedImage[1 + (end-start)];
        
        /**
         * set of JVM flags which allow user control on process
         */
        
        
        //////////////////TIFF OPTIONS/////////////////////////////////////////
        
        final String multiPageFlag=System.getProperty("org.jpedal.multipage_tiff");
        final boolean isSingleOutputFile=multiPageFlag!=null && multiPageFlag.toLowerCase().equals("true");
        
        final String tiffFlag=System.getProperty("org.jpedal.compress_tiff");
        final boolean compressTiffs = tiffFlag!=null && tiffFlag.toLowerCase().equals("true");
        
        //////////////////JPEG OPTIONS/////////////////////////////////////////
        
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
        
        final String jpgFlag=System.getProperty("org.jpedal.jpeg_dpi");
        
        ///////////////////////////////////////////////////////////////////////
        
        for (int pageNo = start; pageNo < end+1; pageNo++) {
            
            if(debug) {
                System.out.println("page : " + pageNo);
            }
            
            /**
             * If you are using decoder.getPageAsHiRes() after passing additional parameters into JPedal using the static method
             * PdfDecoder.modifyJPedalParameters(), then getPageAsHiRes() wont necessarily be thread safe.  If you want to use
             * getPageAsHiRes() and pass in additional parameters, in a thread safe mannor, please use the method
             * getPageAsHiRes(int pageIndex, Map params) or getPageAsHiRes(int pageIndex, Map params, boolean isTransparent) and
             * pass the additional parameters in directly to the getPageAsHiRes() method without calling PdfDecoder.modifyJPedalParameters()
             * first.
             *
             * Please see org/jpedal/examples/images/ConvertPagesToImages.java.html for more details on how to use HiRes image conversion
             */
            BufferedImage imageToSave = decoder.getPageAsHiRes(pageNo,null,isTransparent);
            
            final String imageFormat = System.getProperty("org.jpedal.imageType");
            if(imageFormat!=null){
                if(isNumber(imageFormat)){
                    final int iFormat = Integer.parseInt(imageFormat);
                    if(iFormat>-1 && iFormat<14){
                        final BufferedImage tempImage = new BufferedImage(imageToSave.getWidth(), imageToSave.getHeight(), iFormat);
                        final Graphics2D g = tempImage.createGraphics();
                        g.drawImage(imageToSave, null, null);
                        
                        imageToSave = tempImage;
                    }else{
                        System.err.println("Image Type is not valid. Value should be a digit between 0 - 13 based on the BufferedImage TYPE variables.");
                    }
                }else{
                    System.err.println("Image Type provided is not an Integer. Value should be a digit between 0 - 13 based on the BufferedImage TYPE variables.");
                }
            }
            
            //show status flag
            /**
             * if(decoder.getPageDecodeStatus(DecodeStatus.Timeout))
             * System.out.println("Timeout on decoding");
             * else
             * System.out.println("Done");
             * /**/
            
            decoder.flushObjectValues(true);
            
            //System.out.println("w="+imageToSave.getWidth()+" h="+imageToSave.getHeight());
            //image needs to be sRGB for JPEG
            if(fileType.equals("jpg")) {
                imageToSave = ColorSpaceConvertor.convertToRGB(imageToSave);
            }
            
            
            final String outputFileName;
            if(isSingleOutputFile) {
                outputFileName = outputPath+ "allPages"+prefix+ '.' + fileType;
            } else{
                /**
                 * create a name with zeros for if more than 9 pages appears in correct order
                 */
                String pageAsString=String.valueOf(pageNo);
                final String maxPageSize=String.valueOf(end);
                final int padding=maxPageSize.length()-pageAsString.length();
                for(int ii=0;ii<padding;ii++) {
                    pageAsString='0'+pageAsString;
                }
                
                outputFileName = outputPath + "page" + pageAsString +prefix + '.' + fileType;
            }
            
            //<link><a name="grayscale" />
            //if just gray we can reduce memory usage by converting image to Grayscale
            
            /**
             * see what Colorspaces used and reduce image if appropriate
             * (only does Gray at present)
             *
             * Can return null value if not sure
             */
            final Iterator colorspacesUsed=decoder.getPageInfo(PageInfo.COLORSPACES);
            
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
                final BufferedImage image_to_save2=new BufferedImage(imageToSave.getWidth(),imageToSave.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                image_to_save2.getGraphics().drawImage(imageToSave,0,0,null);
                imageToSave = image_to_save2;
            }
            
            //put image in array if multi-images  (we save on last page in code below)
            if(isSingleOutputFile) {
                multiPages[pageNo-start] = imageToSave;
            }
            
            //we save the image out here
            if (imageToSave != null) {
                
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
                
                if(JAIHelper.isJAIused()) {
                    JAIHelper.confirmJAIOnClasspath();
                }
                
                if(JAIHelper.isJAIused() && fileType.startsWith("tif")){
                    
                    JAIHelper.saveAsTiff(compressTiffs, isSingleOutputFile, imageToSave, outputFileName, pageNo == end, multiPages);
                }else if(isSingleOutputFile){
                    //non-JAI
                } else if ((jpgFlag != null || rawJPEGComp!=null) && fileType.startsWith("jp") && JAIHelper.isJAIused()) {
                    
                    saveAsJPEG(jpgFlag, imageToSave, JPEGcompression, new BufferedOutputStream(new FileOutputStream(outputFileName)));
                } else {
                    
                    final BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(new File(outputFileName)));
                    ImageIO.write(imageToSave, fileType, bos);
                    bos.flush();
                    bos.close();
                }
                
                imageToSave.flush();
            }
            
            if(debug){
                System.out.println("Created : " + outputFileName);
                
            }
        }
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
    
    private static void saveAsJPEG(final String jpgFlag, final BufferedImage image_to_save, final float JPEGcompression, final BufferedOutputStream fos) throws IOException {
        
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
