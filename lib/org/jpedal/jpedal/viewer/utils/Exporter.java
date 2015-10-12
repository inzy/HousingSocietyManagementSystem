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
 * Exporter.java
 * ---------------
 */
package org.jpedal.examples.viewer.utils;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ProgressMonitor;

import org.jpedal.*;

import org.jpedal.examples.viewer.gui.popups.SaveBitmap;
import org.jpedal.examples.viewer.gui.popups.SaveImage;
import org.jpedal.examples.viewer.gui.popups.SaveText;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.gui.GUIFactory;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.JAIHelper;
import org.jpedal.objects.PdfImageData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;
import org.jpedal.utils.Strip;
import org.jpedal.utils.SwingWorker;

/**provide save functions for Viewer to write out text, images, etc*/
public class Exporter {
    
    public static final int RECTANGLE=1;
    public static final int WORDLIST=2;
    public static final int TABLE=3;
    
    /**file separator used*/
    private final String separator=System.getProperty( "file.separator" );
    
    private String fileName="";
    
    private final GUIFactory currentGUI;
    
    private final PdfDecoderInt dPDF;
    
    private final String selectedFile;
    
    public Exporter(final GUIFactory currentGUI, final String selectedFile, final PdfDecoderInt decode_pdf){
        String fileName = new File(selectedFile).getName();
        if(fileName.lastIndexOf('.') != -1) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        
        final StringBuilder fileNameBuffer = new StringBuilder(fileName);
        int index;
        while((index = fileNameBuffer.toString().indexOf("%20")) != -1){
            fileNameBuffer.replace(index,index+3," ");
        }
        
        this.fileName = fileNameBuffer.toString();
        this.currentGUI=currentGUI;
        this.selectedFile=selectedFile;
        this.dPDF=decode_pdf;
        
    }
    
    public void extractPagesAsImages(final SaveBitmap current_selection) {
        //get user choice
        final int startPage = current_selection.getStartPage();
        final int endPage = current_selection.getEndPage();
        
        if(startPage < 1 || endPage < 1) {
            return;
        }
        
        final String format = current_selection.getPrefix();
        final int scaling=current_selection.getScaling();
        final String output_dir = current_selection.getRootDir()+separator+fileName+separator+"thumbnails"+separator;
        
        final File testDirExists=new File(output_dir);
        if(!testDirExists.exists()) {
            testDirExists.mkdirs();
        }
        
        final ProgressMonitor status = new ProgressMonitor((Container)currentGUI.getFrame(),
                Messages.getMessage("PdfViewerMessage.GeneratingBitmaps"),"",startPage,endPage);
        
        final SwingWorker worker = new SwingWorker() {
            
            @Override
            public Object construct() {
                //do the save
                int count=0;
                boolean yesToAll = false;
                for(int page=startPage;page<endPage+1;page++){
                    if(status.isCanceled()){
                        currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") +
                                count+ ' ' +Messages.getMessage("PdfViewerError.ReportNumberOfImagesExported"));
                        return null;
                    }
                    
                    BufferedImage image_to_save=null;
                    
                    try {
                        image_to_save = dPDF.getPageAsImage(page);
                    } catch (final PdfException e1) {
                        e1.printStackTrace();
                    }
                    
                    if(image_to_save!=null){ //
                        
                        /** scale image with bicubic scaling*/
                        if(scaling!=100){
                            final int newWidth=image_to_save.getWidth()*scaling/100;
                            
                            //only 1 new co-ord needed so use -1 for other as aspect ration does not change
                            final Image scaledImage= image_to_save.getScaledInstance(newWidth,-1,BufferedImage.SCALE_SMOOTH);
                            image_to_save = new BufferedImage(scaledImage.getWidth(null),scaledImage.getHeight(null) , BufferedImage.TYPE_INT_RGB);
                            final Graphics2D g2 = image_to_save.createGraphics();
                            g2.drawImage(scaledImage, 0, 0,null);
                            
                        }
                        
                        
                        final File fileToSave = new File(output_dir + page+ '.' +format);
                        if(fileToSave.exists() && !yesToAll){
                            if((endPage - startPage) > 1){
                                final int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(),true);
                                
                                if(n==0){
                                    // clicked yes so just carry on for this once
                                }else if(n==1){
                                    // clicked yes to all, so set flag
                                    yesToAll = true;
                                }else if(n==2){
                                    // clicked no, so loop round again
                                    status.setProgress(page);
                                    continue;
                                }else{
                                    
                                    currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") +
                                            count+ ' ' +Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported"));
                                    
                                    status.close();
                                    return null;
                                }
                            }else{
                                final int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(),false);
                                
                                if(n==0){
                                    // clicked yes so just carry on
                                }else{
                                    // clicked no, so exit
                                    return null;
                                }
                            }
                        }
                        
                        //save image
                        dPDF.getObjectStore().saveStoredImage(output_dir + page,image_to_save,true,false,format);
                    }
                    
                    count++;
                    status.setProgress(page+1);
                    
                }
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerTitle.PagesSavedAsImages")+ ' ' +output_dir);
                return null;
            }
        };
        
        worker.start();
    }
    
    /**save image - different versions have different bugs for file formats so we use best for
     * each image type
     * @param image_to_save
     */
    private static void saveImage(final BufferedImage image_to_save, final String fileName, final String prefix) {
        
        if(JAIHelper.isJAIused()) {
            JAIHelper.confirmJAIOnClasspath();
        }
        
        if(prefix.contains("tif") && JAIHelper.isJAIused()){
            
            try {
                final FileOutputStream os = new FileOutputStream(fileName);
                javax.media.jai.JAI.create("encode", image_to_save, os,"TIFF", null);
            } catch (final FileNotFoundException e) {
                e.printStackTrace();
            }
            
        }else{ //default
            try {
                
                ImageIO.write(image_to_save,prefix,new File(fileName));
                
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * routine to write out clipped PDFs
     */
    private void decodeHires(final int start, final int end, final String imageType, final String output_dir){
        
        PdfDecoderInt decode_pdf=null;
        
        String target="";
        
        //PdfDecoder returns a PdfException if there is a problem
        try{
            
            decode_pdf = new PdfDecoder( false );
            decode_pdf.setExtractionMode(PdfDecoderInt.FINALIMAGES+PdfDecoderInt.CLIPPEDIMAGES,1);
            
            /** open the file (and read metadata including pages in  file)*/
            decode_pdf.openPdfFile( selectedFile );
            
        }catch( final Exception e ){
            e.printStackTrace();
        }
        
        /**
         * extract data from pdf (if allowed).
         */
        if ((decode_pdf.isEncrypted()&&(!decode_pdf.isPasswordSupplied()))&&(!decode_pdf.isExtractionAllowed())) {
            return;
        }
        
        final ProgressMonitor status = new ProgressMonitor((Container)currentGUI.getFrame(),
                Messages.getMessage("PdfViewerMessage.ExtractImages"),"",start,end);
        
        try{
            int count=0;
            boolean yesToAll = false;
            for( int page = start;page < end + 1;page++ ){ //read pages
                if(status.isCanceled()){
                    currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") +
                            count+ ' ' +Messages.getMessage("PdfViewerError.ReportNumberOfImagesExported"));
                    return;
                }
                //decode the page
                decode_pdf.decodePage( page );
                
                //get the PdfImages object which now holds the images.
                //binary data is stored in a temp directory and we hold the
                //image name and other info in this object
                final PdfImageData pdf_images = decode_pdf.getPdfImageData();
                
                //image count (note image 1 is item 0, so any loop runs 0 to count-1)
                final int image_count = pdf_images.getImageCount();
                
                if(image_count>0){
                    target=output_dir+page+separator;
                    final File targetExists=new File(target);
                    if(!targetExists.exists()) {
                        targetExists.mkdir();
                    }
                }
                
                //work through and save each image
                for( int i = 0;i < image_count;i++ ){
                    
                    final String image_name =pdf_images.getImageName( i );
                    BufferedImage image_to_save;
                    
                    final float x1=pdf_images.getImageXCoord(i);
                    final float y1=pdf_images.getImageYCoord(i);
                    final float w=pdf_images.getImageWidth(i);
                    final float h=pdf_images.getImageHeight(i);
                    
                    try{
                        
                        image_to_save =decode_pdf.getObjectStore().loadStoredImage(  "CLIP_"+image_name );
                        
                        //save image
                        
                        if(image_to_save!=null){
                            
                            //remove transparency on jpeg
                            if(imageType.toLowerCase().startsWith("jp")) {
                                image_to_save = ColorSpaceConvertor.convertToRGB(image_to_save);
                            }
                            
                            final File fileToSave = new File(target+image_name+ '.' +imageType);
                            if(fileToSave.exists() && !yesToAll){
                                final int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(),true);
                                
                                if(n==0){
                                    // clicked yes so just carry on for this once
                                }else if(n==1){
                                    // clicked yes to all, so set flag
                                    yesToAll = true;
                                }else if(n==2){
                                    // clicked no, so loop round again
                                    status.setProgress(page);
                                    continue;
                                }else{
                                    
                                    currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") +
                                            count+ ' ' +Messages.getMessage("PdfViewerError.ReportNumberOfImagesExported"));
                                    
                                    status.close();
                                    return;
                                }
                            }
                            
                            saveImage(image_to_save,target+image_name+ '.' +imageType,imageType);
                            count++;
                        }
                        
                        //save an xml file with details
                        /**
                         * output the data
                         */
                        //LogWriter.writeLog( "Writing out "+(outputName + ".xml"));
                        final OutputStreamWriter output_stream =
                                new OutputStreamWriter(
                                new FileOutputStream(target+image_name + ".xml"),
                                "UTF-8");
                        
                        output_stream.write(
                                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                        output_stream.write(
                                "<!-- Pixel Location of image x1,y1,x2,y2\n");
                        output_stream.write("(x1,y1 is top left corner)\n");
                        output_stream.write(
                                "(origin is bottom left corner)  -->\n");
                        output_stream.write("\n\n<META>\n");
                        output_stream.write(
                                "<PAGELOCATION x1=\""+ x1+ "\" "
                                + "y1=\""+ (y1+h)+ "\" "
                                + "x2=\""+ (x1+w)+ "\" "
                                + "y2=\""+ (y1)+ "\" />\n");
                        output_stream.write("<FILE>"+this.fileName+"</FILE>\n");
                        output_stream.write("</META>\n");
                        output_stream.close();
                    }catch( final Exception ee ){
                        ee.printStackTrace();
                        LogWriter.writeLog( "Exception " + ee + " in extracting images" );
                    }
                }
                
                
                //flush images in case we do more than 1 page so only contains
                //images from current page
                decode_pdf.flushObjectValues(true);
                
                status.setProgress(page+1);
                
            }
            status.close();
            
            currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.ImagesSavedTo")+ ' ' +output_dir);
            
            
        }catch( final Exception e ){
            decode_pdf.closePdfFile();
            LogWriter.writeLog( "Exception " + e.getMessage() );
        }
        
        
        
        /**close the pdf file*/
        decode_pdf.closePdfFile();
        
    }
    
    
    public void extractImagesOnPages(final SaveImage current_selection) {
        final int startPage = current_selection.getStartPage();
        final int endPage = current_selection.getEndPage();
        
        if(startPage < 1 || endPage < 1) {
            return;
        }
        
        final int type=current_selection.getImageType();
        //get user choice
        final String format = current_selection.getPrefix();
        final String output_dir = current_selection.getRootDir()+separator+fileName+separator+"images"+separator;
        
        final File testDirExists=new File(output_dir);
        if(!testDirExists.exists()) {
            testDirExists.mkdirs();
        }
        
        final SwingWorker worker = new SwingWorker() {
            @Override
            public Object construct() {
                //do the save
                
                switch(type){
                    case PdfDecoderInt.CLIPPEDIMAGES:
                        decodeHires(startPage,endPage,format,output_dir);
                        break;
                    case PdfDecoderInt.RAWIMAGES:
                        decodeImages(startPage,endPage,format,output_dir,false);
                        break;
                    case PdfDecoderInt.FINALIMAGES:
                        decodeImages(startPage,endPage,format,output_dir,true);
                        break;
                    default:
                        System.out.println("Unknown setting");
                        break;
                }
                
                return null;
            }
        };
        
        worker.start();
    }
    
    
    /**
     * routine to write out images in PDFs
     */
    private void decodeImages(final int start, final int end, final String prefix, final String output_dir, final boolean downsampled){
        
        PdfDecoderInt decode_pdf=null;
        
        //PdfDecoder returns a PdfException if there is a problem
        try{
            
            decode_pdf = new PdfDecoder( false );
            
            decode_pdf.setExtractionMode(PdfDecoderInt.RAWIMAGES+PdfDecoderInt.FINALIMAGES,1);
            /** open the file (and read metadata including pages in  file)*/
            decode_pdf.openPdfFile( selectedFile );
            
        }catch( final Exception e ){
            e.printStackTrace();
        }
        
        /**
         * extract data from pdf (if allowed).
         */
        if ((decode_pdf.isEncrypted()&&(!decode_pdf.isPasswordSupplied()))&&(!decode_pdf.isExtractionAllowed())) {
            return;
        }
        
        final ProgressMonitor status = new ProgressMonitor((Container)currentGUI.getFrame(),
                Messages.getMessage("PdfViewerMessage.ExtractImages"),"",start,end);
        
        try{
            int count=0;
            boolean yesToAll = false;
            for( int page = start;page < end + 1;page++ ){ //read pages
                if(status.isCanceled()){
                    currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") +
                            count + Messages.getMessage("PdfViewerError.ReportNumberOfImagesExported"));
                    return;
                }
                //decode the page
                decode_pdf.decodePage( page );
                
                //get the PdfImages object which now holds the images.
                //binary data is stored in a temp directory and we hold the
                //image name and other info in this object
                final PdfImageData pdf_images = decode_pdf.getPdfImageData();
                
                //image count (note image 1 is item 0, so any loop runs 0 to count-1)
                final int image_count = pdf_images.getImageCount();
                
                String target=output_dir+separator;
                if(downsampled) {
                    target = target + "downsampled" + separator + page + separator;
                } else {
                    target = target + "normal" + separator + page + separator;
                }
                
                //tell user
                if( image_count > 0 ){
                    
                    
                    //create a directory for page
                    File page_path = new File( target );
                    if(!page_path.exists()) {
                        page_path.mkdirs();
                    }
                    
                    
                    //do it again as some OS struggle with creating nested dirs
                    page_path = new File( target );
                    if(!page_path.exists()) {
                        page_path.mkdirs();
                    }
                    
                }
                
                //work through and save each image
                for( int i = 0;i < image_count;i++ )
                {
                    final String image_name = pdf_images.getImageName( i );
                    BufferedImage image_to_save;
                    
                    try
                    {
                        if(downsampled){
                            //load processed version of image (converted to rgb)
                            image_to_save = decode_pdf.getObjectStore().loadStoredImage( image_name );
                            if(prefix.toLowerCase().startsWith("jp")){
                                image_to_save=ColorSpaceConvertor.convertToRGB(image_to_save);
                                
                            }
                        }else{
                            //get raw version of image (R prefix for raw image)
                            image_to_save = decode_pdf.getObjectStore().loadStoredImage( image_name );
                            if(prefix.toLowerCase().startsWith("jp")){
                                image_to_save=ColorSpaceConvertor.convertToRGB(image_to_save);
                            }
                        }
                        
                        final File fileToSave = new File(target+ image_name+ '.' +prefix);
                        if(fileToSave.exists() && !yesToAll){
                            final int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(),true);
                            
                            if(n==0){
                                // clicked yes so just carry on for this once
                            }else if(n==1){
                                // clicked yes to all, so set flag
                                yesToAll = true;
                            }else if(n==2){
                                // clicked no, so loop round again
                                status.setProgress(page);
                                continue;
                            }else{
                                
                                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") +
                                        count+ ' ' +Messages.getMessage("PdfViewerError.ReportNumberOfImagesExported"));
                                
                                status.close();
                                return;
                            }
                        }
                        
                        //save image
                        saveImage(image_to_save,target+ image_name+ '.' +prefix,prefix);
                        count++;
                    }
                    
                    
                    catch( final Exception ee )
                    {
                        System.err.println( "Exception " + ee + " in extracting images" );
                    }
                }
                
                //flush images in case we do more than 1 page so only contains
                //images from current page
                decode_pdf.flushObjectValues(true);
                
                
                status.setProgress(page+1);
            }
            
            currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.ImagesSavedTo")+ ' ' +output_dir);
            
            status.close();
        }catch( final Exception e ){
            decode_pdf.closePdfFile();
            LogWriter.writeLog( "Exception " + e.getMessage() );
        }
        
        /**close the pdf file*/
        decode_pdf.closePdfFile();
        
    }
    
    
    public void extractTextOnPages(final SaveText current_selection) {
        //get user choice
        final int startPage = current_selection.getStartPage();
        final int endPage = current_selection.getEndPage();
        
        if(startPage < 1 || endPage < 1) {
            return;
        }
        
        final int type=current_selection.getTextType();
        final boolean useXMLExtraction=current_selection.isXMLExtaction();
        
        final String output_dir = current_selection.getRootDir()+separator+fileName+separator+"text"+separator;
        
        final File testDirExists=new File(output_dir);
        if(!testDirExists.exists()) {
            testDirExists.mkdirs();
        }
        
        final SwingWorker worker = new SwingWorker() {
            @Override
            public Object construct() {
                //do the save
                
                switch(type){
                    case Exporter.RECTANGLE:
                        decodeTextRectangle(startPage,endPage,output_dir,useXMLExtraction);
                        break;
                    case Exporter.WORDLIST:
                        decodeTextWordlist(startPage,endPage,output_dir,useXMLExtraction);
                        break;
                    case Exporter.TABLE:
                        decodeTextTable(startPage,endPage,output_dir,useXMLExtraction);
                        
                        break;
                    default:
                        System.out.println("Unknown setting");
                        break;
                }
                
                return null;
            }
        };
        
        worker.start();
        
    }
    
    
    
    
    private void decodeTextTable(final int startPage, final int endPage, final String output_dir, final boolean useXMLExtraction) {
        
        PdfDecoderInt decode_pdf=null;
        
        try {
            decode_pdf = new PdfDecoder(false);
            decode_pdf.setExtractionMode(PdfDecoderInt.TEXT); //extract just text
            
            PdfDecoder.init(true);
            
            /**
             * open the file (and read metadata including pages in  file)
             */
            
            decode_pdf.openPdfFile(selectedFile);
            
        } catch (final Exception e) {
            System.err.println("Exception " + e + " in pdf code");
        }
        
        /**
         * extract data from pdf (if allowed).
         */
        if ((decode_pdf.isEncrypted()&&(!decode_pdf.isPasswordSupplied()))&& (!decode_pdf.isExtractionAllowed())) {
            System.out.println("Encrypted settings");
            System.out.println("Please look at Viewer for code sample to handle such files");
        } else {
            
            final ProgressMonitor status = new ProgressMonitor((Container)currentGUI.getFrame(),
                    Messages.getMessage("PdfViewerMessage.ExtractText"),"",startPage,endPage);
            /**
             * extract data from pdf
             */
            try {
                int count=0;
                boolean yesToAll = false;
                for (int page = startPage; page < endPage + 1; page++) { //read pages
                    if(status.isCanceled()){
                        currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") +count
                                + ' ' +Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported"));
                        return;
                    }
                    //decode the page
                    decode_pdf.decodePage(page);
                    
                    /** create a grouping object to apply grouping to data*/
                    final PdfGroupingAlgorithms currentGrouping =decode_pdf.getGroupingObject();
                    
                    /**use whole page size for  demo - get data from PageData object*/
                    final PdfPageData currentPageData = decode_pdf.getPdfPageData();
                    
                    final int x1;
                    final int y1;
                    final int x2;
                    final int y2;

                    x1 = currentPageData.getMediaBoxX(page);
                    x2 = currentPageData.getMediaBoxWidth(page)+x1;
                    
                    y2 = currentPageData.getMediaBoxY(page);
                    y1 = currentPageData.getMediaBoxHeight(page)+y2;
                    
                    //default for xml
                    String ending="_text.csv";
                    
                    if(useXMLExtraction) {
                        ending = "_xml.txt";
                    }
                    
                    /**Co-ordinates are x1,y1 (top left hand corner), x2,y2(bottom right) */
                    
                    /**The call to extract the table*/
                    final Map tableContent;
                    String tableText=null;
                    
                    try{
                        //the source code for this grouping is in the customer area
                        //in class pdfGroupingAlgorithms
                        //all these settings are defined in the Java
                        
                        tableContent =currentGrouping.extractTextAsTable(
                                x1,
                                y1,
                                x2,
                                y2,
                                page,
                                !useXMLExtraction,
                                false,
                                false,false,0);
                        
                        //get the text from the Map object
                        tableText=(String)tableContent.get("content");
                        
                    } catch (final PdfException e) {
                        decode_pdf.closePdfFile();
                        System.err.println("Exception " + e.getMessage()+" with table extraction");
                    }catch (final Error e) {
                        e.printStackTrace();
                    }
                    
                    if (tableText == null) {
                        System.out.println("No text found");
                    } else {
                        
                        
                        final String target=output_dir+separator+"table"+separator;
                        
                        //create a directory if it doesn't exist
                        final File output_path = new File(target);
                        if (!output_path.exists()) {
                            output_path.mkdirs();
                        }
                        
                        final File fileToSave = new File(target + fileName+ '_' +page+ ending);
                        if(fileToSave.exists() && !yesToAll){
                            if((endPage - startPage) > 1){
                                final int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(),true);
                                
                                if(n==0){
                                    // clicked yes so just carry on for this once
                                }else if(n==1){
                                    // clicked yes to all, so set flag
                                    yesToAll = true;
                                }else if(n==2){
                                    // clicked no, so loop round again
                                    status.setProgress(page);
                                    continue;
                                }else{
                                    
                                    currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") +
                                            count+ ' ' +Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported"));
                                    
                                    status.close();
                                    return;
                                }
                            }else{
                                final int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(),false);
                                
                                if(n==0){
                                    // clicked yes so just carry on
                                }else{
                                    // clicked no, so exit
                                    return;
                                }
                            }
                        }
                        
                        /**
                         * output the data - you may wish to alter the encoding to suit
                         */
                        final OutputStreamWriter output_stream =
                                new OutputStreamWriter(
                                new FileOutputStream(target + fileName+ '_' +page+ ending),
                                "UTF-8");
                        
                        //						xml header
                        if(useXMLExtraction) {
                            output_stream.write("<xml><BODY>\n\n");
                        }
                        
                        output_stream.write(tableText); //write actual data
                        
                        //						xml footer
                        if(useXMLExtraction) {
                            output_stream.write("\n</body></xml>");
                        }
                        
                        output_stream.close();
                        
                    }
                    count++;
                    status.setProgress(page+1);
                    //remove data once written out
                    decode_pdf.flushObjectValues(false);
                }
                status.close();
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.TextSavedTo")+ ' ' +output_dir);
            } catch (final Exception e) {
                decode_pdf.closePdfFile();
                System.err.println("Exception " + e.getMessage());
                e.printStackTrace();
            }catch(final Error e){
                System.out.println("h34343");
                e.printStackTrace();
            }
            
            decode_pdf.flushObjectValues(true); //flush any text data read
            
        }
        
        /**close the pdf file*/
        decode_pdf.closePdfFile();
        
    }
    
    private void decodeTextWordlist(final int startPage, final int endPage, final String output_dir, final boolean useXMLExtraction) {
        
        PdfDecoderInt decode_pdf=null;
        
        //PdfDecoder returns a PdfException if there is a problem
        try {
            decode_pdf = new PdfDecoder(false);
            
            decode_pdf.setExtractionMode(PdfDecoderInt.TEXT); //extract just text
            PdfDecoder.init(true);
            
            
            //always reset to use unaltered co-ords - allow use of rotated or unrotated
            // co-ordinates on pages with rotation (used to be in PdfDecoder)
            PdfGroupingAlgorithms.useUnrotatedCoords=false;
            
            /**
             * open the file (and read metadata including pages in  file)
             */
            decode_pdf.openPdfFile(selectedFile);
            
        } catch (final PdfSecurityException e) {
            System.err.println("Exception " + e+" in pdf code for wordlist"+selectedFile);
        } catch (final PdfException e) {
            System.err.println("Exception " + e+" in pdf code for wordlist"+selectedFile);
            
        } catch (final Exception e) {
            System.err.println("Exception " + e+" in pdf code for wordlist"+selectedFile);
            e.printStackTrace();
        }
        
        /**
         * extract data from pdf (if allowed).
         */
        if ((decode_pdf.isEncrypted()&&(!decode_pdf.isPasswordSupplied()))&& (!decode_pdf.isExtractionAllowed())) {
            System.out.println("Encrypted settings");
            System.out.println("Please look at Viewer for code sample to handle such files");
            
        } else{
            //page range
            int wordsExtracted=0;
            
            final ProgressMonitor status = new ProgressMonitor((Container)currentGUI.getFrame(),
                    Messages.getMessage("PdfViewerMessage.ExtractText"),"",startPage,endPage);
            
            /**
             * extract data from pdf
             */
            try {
                int count=0;
                boolean yesToAll = false;
                for (int page = startPage; page < endPage + 1; page++) { //read pages
                    if(status.isCanceled()){
                        currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") +
                                count+ ' ' +Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported"));
                        return;
                    }
                    //decode the page
                    decode_pdf.decodePage(page);
                    
                    /** create a grouping object to apply grouping to data*/
                    final PdfGroupingAlgorithms currentGrouping =decode_pdf.getGroupingObject();
                    
                    /**use whole page size for  demo - get data from PageData object*/
                    final PdfPageData currentPageData = decode_pdf.getPdfPageData();
                    
                    final int x1 = currentPageData.getMediaBoxX(page);
                    final int x2 = currentPageData.getMediaBoxWidth(page)+x1;
                    
                    final int y2 = currentPageData.getMediaBoxX(page);
                    final int y1 = currentPageData.getMediaBoxHeight(page)-y2;
                    
                    /**Co-ordinates are x1,y1 (top left hand corner), x2,y2(bottom right) */
                    
                    /**The call to extract the list*/
                    List words =null;
                    
                    try{
                        words =currentGrouping.extractTextAsWordlist(
                                x1,
                                y1,
                                x2,
                                y2,
                                page,
                                true,"&:=()!;.,\\/\"\"\'\'");
                    } catch (final PdfException e) {
                        decode_pdf.closePdfFile();
                        System.err.println("Exception= "+ e+" in "+selectedFile);
                        e.printStackTrace();
                    }catch(final Error e){
                        e.printStackTrace();
                    }
                    
                    if (words == null) {
                        
                        System.out.println("No text found");
                        
                    } else {
                        
                        final String target=output_dir+separator+"wordlist"+separator;
                        
                        //create a directory if it doesn't exist
                        final File output_path = new File(target);
                        if (!output_path.exists()) {
                            output_path.mkdirs();
                        }
                        
                        /**
                         * choose correct prefix
                         */
                        String prefix="_text.txt";
                        String encoding=System.getProperty("file.encoding");
                        
                        if(useXMLExtraction){
                            prefix="_xml.txt";
                            encoding="UTF-8";
                        }
                        
                        /**each word is stored as 5 consecutive values (word,x1,y1,x2,y2)*/
                        final int wordCount=words.size()/5;
                        
                        //update our count
                        wordsExtracted += wordCount;
                        
                        
                        final File fileToSave = new File(target + fileName+ '_' +page + prefix);
                        if(fileToSave.exists() && !yesToAll){
                            if((endPage - startPage) > 1){
                                final int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(),true);
                                
                                if(n==0){
                                    // clicked yes so just carry on for this once
                                }else if(n==1){
                                    // clicked yes to all, so set flag
                                    yesToAll = true;
                                }else if(n==2){
                                    // clicked no, so loop round again
                                    status.setProgress(page);
                                    continue;
                                }else{
                                    
                                    currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") +
                                            count+ ' ' +Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported"));
                                    
                                    status.close();
                                    return;
                                }
                            }else{
                                final int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(),false);
                                
                                if(n==0){
                                    // clicked yes so just carry on
                                }else{
                                    // clicked no, so exit
                                    return;
                                }
                            }
                        }
                        
                        
                        /**
                         * output the data
                         */
                        final OutputStreamWriter output_stream =
                                new OutputStreamWriter(
                                new FileOutputStream(target + fileName+ '_' +page + prefix),
                                encoding);
                        
                        final Iterator wordIterator=words.iterator();
                        while(wordIterator.hasNext()){
                            
                            String currentWord=(String) wordIterator.next();
                            
                            /**remove the XML formatting if present - not needed for pure text*/
                            if(!useXMLExtraction) {
                                currentWord = Strip.convertToText(currentWord, true);
                            }
                            
                            final int wx1=(int)Float.parseFloat((String) wordIterator.next());
                            final int wy1=(int)Float.parseFloat((String) wordIterator.next());
                            final int wx2=(int)Float.parseFloat((String) wordIterator.next());
                            final int wy2=(int)Float.parseFloat((String) wordIterator.next());
                            
                            /**this could be inserting into a database instead*/
                            output_stream.write(currentWord+ ',' +wx1+ ',' +wy1+ ',' +wx2+ ',' +wy2+ '\n');
                            
                        }
                        output_stream.close();
                        
                    }
                    
                    count++;
                    status.setProgress(page+1);
                    
                    //remove data once written out
                    decode_pdf.flushObjectValues(false);
                    
                }
                status.close();
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.TextSavedTo")+ ' ' +output_dir);
            } catch (final Exception e) {
                decode_pdf.closePdfFile();
                System.err.println("Exception "+ e+" in "+selectedFile);
                e.printStackTrace();
            }catch(final Error e){
                e.printStackTrace();
            }
        }
        
        /**close the pdf file*/
        decode_pdf.closePdfFile();
        
    }
    
    private void decodeTextRectangle(final int startPage, final int endPage, final String output_dir, final boolean useXMLExtraction) {
        
        PdfDecoderInt decode_pdf=null;
        
        //PdfDecoder returns a PdfException if there is a problem
        try {
            decode_pdf = new PdfDecoder( false );
            
            if(!useXMLExtraction) {
                decode_pdf.useTextExtraction();
            }
            
            decode_pdf.setExtractionMode(PdfDecoderInt.TEXT); //extract just text
            PdfDecoder.init(true);
            
            /**
             * open the file (and read metadata including pages in  file)
             */
            decode_pdf.openPdfFile(selectedFile);
            
        } catch (final PdfSecurityException se) {
            System.err.println("Security Exception " + se + " in pdf code for text extraction on file ");
            //e.printStackTrace();
        } catch (final PdfException se) {
            System.err.println("Pdf Exception " + se + " in pdf code for text extraction on file ");
            //e.printStackTrace();
        } catch (final Exception e) {
            System.err.println("Exception " + e + " in pdf code for text extraction on file ");
            e.printStackTrace();
        }
        
        /**
         * extract data from pdf (if allowed).
         */
        if ((decode_pdf.isEncrypted()&&(!decode_pdf.isPasswordSupplied()))&& (!decode_pdf.isExtractionAllowed())) {
            System.out.println("Encrypted settings");
            System.out.println("Please look at Viewer for code sample to handle such files");
            
        } else {
            
            final ProgressMonitor status = new ProgressMonitor((Container)currentGUI.getFrame(),
                    Messages.getMessage("PdfViewerMessage.ExtractText"),"",startPage,endPage);
            
            /**
             * extract data from pdf
             */
            try {
                int count=0;
                boolean yesToAll = false;
                for (int page = startPage; page < endPage + 1; page++) { //read pages
                    if(status.isCanceled()){
                        currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport")
                                +count+ ' ' +Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported"));
                        return;
                    }
                    //decode the page
                    decode_pdf.decodePage(page);
                    
                    /** create a grouping object to apply grouping to data*/
                    final PdfGroupingAlgorithms currentGrouping =decode_pdf.getGroupingObject();
                    
                    /**use whole page size for  demo - get data from PageData object*/
                    final PdfPageData currentPageData = decode_pdf.getPdfPageData();
                    
                    final int x1 = currentPageData.getMediaBoxX(page);
                    final int x2 = currentPageData.getMediaBoxWidth(page)+x1;
                    
                    final int y2 = currentPageData.getMediaBoxY(page);
                    final int y1 = currentPageData.getMediaBoxHeight(page)+y2;
                    
                    /**Co-ordinates are x1,y1 (top left hand corner), x2,y2(bottom right) */
                    
                    /**The call to extract the text*/
                    String text =null;
                    
                    try{
                        text =currentGrouping.extractTextInRectangle(
                                x1,
                                y1,
                                x2,
                                y2,
                                page,
                                false,
                                true);
                    } catch (final PdfException e) {
                        decode_pdf.closePdfFile();
                        System.err.println("Exception " + e.getMessage()+" in file "+decode_pdf.getObjectStore().fullFileName);
                        e.printStackTrace();
                    }
                    
                    //allow for no text
                    if(text==null) {
                        continue;
                    }
                    
                    final String target=output_dir+separator+"rectangle"+separator;
                    
                    //ensure a directory for data
                    final File page_path = new File(target);
                    if (!page_path.exists()) {
                        page_path.mkdirs();
                    }
                    
                    /**
                     * choose correct prefix
                     */
                    String prefix="_text.txt";
                    String encoding=System.getProperty("file.encoding");
                    
                    if(useXMLExtraction){
                        prefix="_xml.txt";
                        encoding="UTF-8";
                    }
                    
                    final File fileToSave = new File(target + fileName+ '_' +page + prefix);
                    if(fileToSave.exists() && !yesToAll){
                        if((endPage - startPage) > 1){
                            final int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(),true);
                            
                            if(n==0){
                                // clicked yes so just carry on for this once
                            }else if(n==1){
                                // clicked yes to all, so set flag
                                yesToAll = true;
                            }else if(n==2){
                                // clicked no, so loop round again
                                status.setProgress(page);
                                continue;
                            }else{
                                
                                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerError.UserStoppedExport") +
                                        count+ ' ' +Messages.getMessage("PdfViewerError.ReportNumberOfPagesExported"));
                                
                                status.close();
                                return;
                            }
                        }else{
                            final int n = currentGUI.showOverwriteDialog(fileToSave.getAbsolutePath(),false);
                            
                            if(n==0){
                                // clicked yes so just carry on
                            }else{
                                // clicked no, so exit
                                return;
                            }
                        }
                    }
                    
                    /**
                     * output the data
                     */
                    final OutputStreamWriter output_stream =
                            new OutputStreamWriter(
                            new FileOutputStream(target + fileName+ '_' +page + prefix),
                            encoding);
                    
                    if((useXMLExtraction)){
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
                    
                    count++;
                    output_stream.close();
                    
                    status.setProgress(page+1);
                    
                    //remove data once written out
                    decode_pdf.flushObjectValues(true);
                }
                status.close();
                currentGUI.showMessageDialog(Messages.getMessage("PdfViewerMessage.TextSavedTo")+ ' ' +output_dir);
                
            } catch (final Exception e) {
                decode_pdf.closePdfFile();
                System.err.println("Exception " + e.getMessage());
                e.printStackTrace();
                System.out.println(decode_pdf.getObjectStore().getCurrentFilename());
            }
        }
        
        /**close the pdf file*/
        decode_pdf.closePdfFile();
    }
}
