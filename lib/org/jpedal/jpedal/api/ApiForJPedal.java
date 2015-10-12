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
 * ApiForJPedal.java
 * ---------------
 */
package org.jpedal.examples.api;

import org.jpedal.PdfDecoder;
import org.jpedal.PdfDecoderInt;
import org.jpedal.exception.PdfException;
import org.jpedal.exception.PdfFontException;
import org.jpedal.exception.PdfSecurityException;
import org.jpedal.external.JPedalHelper;
import org.jpedal.fonts.FontMappings;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.io.ColorSpaceConvertor;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.StatusBar;
import org.jpedal.objects.PdfData;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.objects.PdfImageData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.acroforms.SwingData;
import org.jpedal.objects.javascript.defaultactions.DisplayJavascriptActions;
import org.jpedal.objects.javascript.defaultactions.JpedalDefaultJavascript;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.raw.FormObject;
import org.jpedal.parser.image.ImageCommands;
import org.jpedal.render.BaseDisplay;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Strip;
import org.jpedal.utils.repositories.Vector_Int;
import org.jpedal.utils.repositories.Vector_Object;
import org.jpedal.utils.repositories.Vector_String;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.Document;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;
import java.util.Map;
import org.jpedal.io.PdfFileReader;

/**
 * List of all methods for Public usage.
 *
 * We will document any changes here as well
 */
@SuppressWarnings("ALL")
public class ApiForJPedal {
    
    /**
         * PdfDecoderServer class
         *
         * org.jpedal.PdfDecoderServer
         *
         * Provides a class to open PDF files and extract Data and rasterize
         * 
         * No viewing or printing support. Does not create GUI form components
         */
    public static void Example_PdfDecoderServer(){
        
        final Example_PdfDecoderServer pdfDecoderServer = new Example_PdfDecoderServer();
        
    }
    
    /**
         * PdfDecoder class
         *
         * org.jpedal.PdfDecoder
         *
         * Provides an object to decode PDF files and provides a rasterizer if required.
         * Normal usage is to create an instance of PdfDecoder and access via public methods.
         * We recommend you access JPedal using only public methods listed in API.
         */
    public static void Example_PdfDecoder(){
        
        final Example_PdfDecoder pdfDecoder = new Example_PdfDecoder();
        
    }
    
    /**
     * PdfData class
     *
     * org.jpedal.objects.PdfData
     * 
     * Holds text data for extraction and manipulation.
     * 
     * Pdf routines create 'raw' text data.
     * Grouping routines will attempt to intelligently stitch together the 'raw' data and leave it as
     * 'processed data' in this class.
     * 
     * We recommend you access JPedal using only public methods listed in API.
     */
    public static void Example_PdfData(){
        
        final Example_PdfData pdfData = new Example_PdfData();
        
    }
    
    /**
     * PdfPageData class
     *
     * org.jpedal.objects.PdfPageData
     * 
     * We recommend you access JPedal using only public methods listed in API.
     */
    public static void Example_PdfPageData(){
        
        final Example_PdfPageData pdfPageData = new Example_PdfPageData();
        
    }
    
    /**
     * old version
     */
    
    
    //test USE structures
    final byte[] byteArrayTest=new byte[8];
    int intTest;
    long longTest;
    final boolean booleanTest=true;
    final BufferedImage bufferedImageTest=new BufferedImage(10,10,1);
    final String stringTest="test";
    
    final String[] stringArrayTest=new String[8];
    final float floatTest=3.6f;
    final int[] rectangleTest=new int[4];
    final Graphics graphicsTest=bufferedImageTest.getGraphics();
    final PageFormat pageFormatTest=new PageFormat();
    final Color colorTest=Color.red;
    final MouseEvent mouseEventTest=new MouseEvent(null,intTest,longTest,intTest,intTest,intTest,intTest,booleanTest);
    final Point pointTest=new Point(0,0);
    Rectangle[] rectangleArrayTest=new Rectangle[8];
    final int[] intArrayTest=new int[8];
    Shape[] shapeArrayTest=new Shape[8];
    final Object[] objectArrayTest=new Object[8];
    boolean[] booleanArrayTest=new boolean[8];
    final Border borderTest=BorderFactory.createEmptyBorder();
    final StringBuilder stringBufferTest = new StringBuilder();
    final Object objectTest = new Object();
    
    /**
     * our classes that are not in API
     */
    
    //only really used if we want to disable XFA support
    AcroRenderer acroRendererTest=new AcroRenderer(true,false);
    
    final PdfData pdfDataTest=new PdfData();
    
    
    //test ASSIGNMENT structure
    boolean assignBoolean;
    String assignString;
    int assignInt;
    float assignFloat;
    //double assignDouble;
    Map assignMap;
    BufferedImage assignBufferedImage;
    Component assignComponent;
    //Color assignColor;
    //Stroke assignStroke;
    //PdfAnnots assignPdfAnnots;
    PdfGroupingAlgorithms assignPdfGroupingAlgorithms;
    PdfFileInformation assignPdfFileInformation;
    ObjectStore assignObjectStore;
    Document assignDocument;
    PageFormat assignPageFormat;
    //PageLines assignPageLines;
    PdfData assignPdfData;
    PdfImageData assignPdfImageData;
    PdfPageData assignPdfPageData;
    //byte[] assignByteArray;
    //PdfFormData assignPdfFormData;
    Printable assignPrintable;
    AffineTransform assignAffineTransform;
    String[]assignStringArray;
    //List assignList;
    List assignVector;
    //float[] assignFloatArray;
    //Rectangle assignRectangle;
    Dimension assignDimension;
    //Rectangle[] assignRectangleArray;
    int[] assignIntArray;
    Object assignObject;
    Object[] assignObjectArray;
    StringBuilder assignStringBuffer;
    
    /*
     * constructor variables
     */
    ColorSpaceConvertor testColorSpaceConvertorMethods;
    ObjectStore testObjectStore;
    StatusBar testStatusBar;
    LogWriter testLogWriter;
    //PdfAnnots testPdfAnnots;
    PdfDecoder testPdfDecoderMethods;
    PdfException testPdfException;
    PdfFileInformation testPdfFileInformation;
    //PdfFormData testPdfFormData;
    PdfGroupingAlgorithms testPdfGroupingAlgorithms;
    PdfImageData testPdfImageData;
    PdfPageData testPdfPageData;
    PdfDecoder testPdfPanel;
    PdfSecurityException testPdfSecurityException;
    Strip testStrip;
    Vector_Int testVector_Int;
    Vector_Object testVector_Object;
    Vector_String testVector_String;
    
    /**
     * Constructors
     */
    public void constructors(){
        testColorSpaceConvertorMethods=
                new ColorSpaceConvertor();
        
        testObjectStore=new ObjectStore();
        
        testStatusBar=new StatusBar();
        testStatusBar=new StatusBar(Color.blue);
        
        testLogWriter=new LogWriter();

        testPdfDecoderMethods=new PdfDecoder();
        testPdfDecoderMethods=new PdfDecoder(booleanTest);
        //testPdfDecoderMethods=new PdfDecoder(intTest,booleanTest);
        
        testPdfException=new PdfException();
        testPdfException=new PdfException(stringTest);
        
        testPdfFileInformation=new PdfFileInformation();
        
        //testPdfFormData=new PdfFormData(booleanTest);
        
        //testPdfGroupingAlgorithms=new PdfGroupingAlgorithms();
        testPdfGroupingAlgorithms=new PdfGroupingAlgorithms(pdfDataTest,testPdfPageData,true);
        
        testPdfImageData=new PdfImageData();
        
        testPdfPageData=new PdfPageData();
        
        testPdfPanel=new PdfDecoder();
        
        
        testPdfSecurityException=new PdfSecurityException("test");
        
        testStrip = new Strip();
        
        testVector_Int = new Vector_Int();
        testVector_Int = new Vector_Int(intTest);
        
        testVector_Object = new Vector_Object();
        testVector_Object = new Vector_Object(intTest);
        
        testVector_String = new Vector_String();
        testVector_String = new Vector_String(intTest);
    }
    
    /**
     * non-static fields
     */
    public void nonstaticfields(){
        //StatusBar
        assignFloat=testStatusBar.percentageDone;
        //ObjectStore
        assignString=testObjectStore.fullFileName;
        
    }
    
    /**
     * static fields
     */
    public void staticfields(){

        //BaseDisplay
        BaseDisplay.userHints=new  RenderingHints(null);

        //	  LogWriter
        assignBoolean=LogWriter.debug;
        assignString=LogWriter.log_name;
        assignBoolean=LogWriter.testing;
        
        //PdfDecoder
        assignInt=PdfDecoderInt.CLIPPEDIMAGES;
        assignInt=PdfDecoderInt.CMYKIMAGES;
        assignBoolean=FontMappings.enforceFontSubstitution;
        assignInt=PdfDecoderInt.FINALIMAGES;
        assignMap=FontMappings.fontSubstitutionAliasTable;
        assignMap=FontMappings.fontSubstitutionLocation;
        assignMap=FontMappings.fontSubstitutionTable;
        assignMap=FontMappings.fontPropertiesTable;
        assignInt=PdfDecoderInt.RAWCOMMANDS;
        assignInt=PdfDecoderInt.RAWIMAGES;
        assignInt=PdfDecoderInt.RENDERIMAGES;
        assignInt=PdfDecoderInt.RENDERTEXT;
        assignInt=PdfDecoderInt.TEXT;
        assignInt=PdfDecoderInt.TEXTCOLOR;
        // assignBoolean=PdfDecoder.use13jPEGConversion;
        assignString=PdfDecoder.version;
        
        //PdfGroupingAlgorithms
        assignBoolean=PdfGroupingAlgorithms.useUnrotatedCoords;

        //check image scaling
        SwingData.readOnlyScaling=-1;

        //font paths
        FontMappings.defaultFontDirs=new String[]{"a","b"};

        //Helper
        JPedalHelper Helper=new org.jpedal.examples.ExampleHelper();
        Helper=null;

        ImageCommands.trackImages=true;
        ImageCommands.trackImages=false;

        String errorMessage=FontMappings.setFontDirs(new String[]{"patha","pathb","pathc"});
        System.out.println(errorMessage);
        
        //emsure flag never set to final
        int a=PdfFileReader.alwaysCacheInMemory;
        PdfFileReader.alwaysCacheInMemory=-1;
        PdfFileReader.alwaysCacheInMemory=a;

    }
    
    /**
     * non-static methods
     * @throws PdfException - traps calls
     */
    public void nonstaticmethods() throws PdfException{
        //ObjectStore
        ObjectStore.copy(stringTest,stringTest);
        ObjectStore.copyCMYKimages(stringTest);
        testObjectStore.flush();
        assignString = testObjectStore.getCurrentFilename();
        assignString = testObjectStore.getImageType(stringTest);
        testObjectStore.init(stringTest);
        assignBufferedImage = testObjectStore.loadStoredImage(stringTest);
        testObjectStore.saveAsCopy(stringTest,stringTest);
        assignBoolean = testObjectStore.saveRawCMYKImage(byteArrayTest,stringTest);
        assignBoolean = testObjectStore.saveStoredImage(stringTest,bufferedImageTest,booleanTest,
                booleanTest, stringTest);
        testObjectStore.storeFileName(stringTest);
        
        //StatusBar
        assignComponent = testStatusBar.getStatusObject();
        testStatusBar.initialiseStatus(stringTest);
        testStatusBar.resetStatus(stringTest);
        testStatusBar.setClientDisplay();
        testStatusBar.setProgress(intTest);
        testStatusBar.updateStatus(stringTest,intTest);
        
        //PdfAnnots
        //        assignColor = testPdfAnnots.getAnnotColor(intTest);
        //        assignInt = testPdfAnnots.getAnnotCount();
        //        assignString = testPdfAnnots.getAnnotObjectArea(intTest);
        //        assignMap = testPdfAnnots.getAnnotRawData(intTest);
        //        assignString = testPdfAnnots.getAnnotSubType(intTest);
        //        assignColor = testPdfAnnots.getBorderColor(intTest);
        //        assignStroke =  testPdfAnnots.getBorderStroke(intTest);
        //        assignString = testPdfAnnots.getField(intTest,stringTest);
        //        testPdfAnnots.readAnnots(byteArray);
        
        //PdfDecoder
        assignBoolean = FontMappings.addSubstituteFonts(stringTest, booleanTest);
        //testPdfDecoderMethods.addUserIconsForAnnotations(intTest, stringTest, imageArrayTest);
        //testPdfDecoderMethods.currentDisplay.flush();
        //testPdfDecoderMethods.pages.refreshDisplay();
        testPdfDecoderMethods.closePdfFile();
        //testPdfDecoderMethods.createPageHostspots(stringArrayTest, stringTest);
        try {
            testPdfDecoderMethods.decodePage(intTest);
        } catch (final Exception e1) {
            e1.printStackTrace();
        }
        // assignPdfAnnots = testPdfDecoderMethods.decodePageForAnnotations(intTest);
        try {
            testPdfDecoderMethods.decodePageInBackground(intTest);
        } catch (final Exception e2) {
            e2.printStackTrace();
        }
        testPdfDecoderMethods.flushObjectValues(booleanTest);
        
        assignPdfGroupingAlgorithms = testPdfDecoderMethods.getBackgroundGroupingObject();
        
        assignPdfFileInformation = testPdfDecoderMethods.getFileInformationData();

        try {
            assignPdfGroupingAlgorithms = testPdfDecoderMethods.getGroupingObject();

            List value = assignPdfGroupingAlgorithms.findMultipleTermsInRectangle(0, 0, 0, 0, 0, 0, new String[]{}, false, 0, null);
        } catch (final PdfException e4) {
            e4.printStackTrace();
        }
        assignInt =  testPdfDecoderMethods.getNumberOfPages();
        assignObjectStore = testPdfDecoderMethods.getObjectStore();
        assignDocument = testPdfDecoderMethods.getOutlineAsXML();
        try {
            assignBufferedImage = testPdfDecoderMethods.getPageAsImage(intTest);
        } catch (final PdfException e5) {
            e5.printStackTrace();
        }
        //assignBufferedImage = testPdfDecoderMethods.getPageAsThumbnail(intTest, intTest);
        assignInt = testPdfDecoderMethods.getPageCount();
        assignString = testPdfDecoderMethods.getPageFailureMessage();
        assignPageFormat = testPdfDecoderMethods.getPageFormat(intTest);
        //assignPageLines = testPdfDecoderMethods.getPageLines();
        //assignPdfAnnots = testPdfDecoderMethods.getPdfAnnotsData(null);
        assignPdfData = testPdfDecoderMethods.getPdfBackgroundData();
        assignPdfImageData = testPdfDecoderMethods.getPdfBackgroundImageData();
        //assignPdfPageData = testPdfDecoderMethods.getPdfBackgroundPageData();
        //assignByteArray = testPdfDecoderMethods.getPdfBuffer();
        try {
            assignPdfData = testPdfDecoderMethods.getPdfData();
        } catch (final PdfException e6) {
            e6.printStackTrace();
        }
        //assignPdfFormData = testPdfDecoderMethods.getPdfFormData();
        assignPdfImageData = testPdfDecoderMethods.getPdfImageData();
        assignPdfPageData = testPdfDecoderMethods.getPdfPageData();
        assignPrintable = testPdfDecoderMethods.getPrintable(intTest);
        assignBufferedImage = testPdfDecoderMethods.getSelectedRectangleOnscreen(intTest, intTest, intTest, intTest, intTest);
        assignBoolean = testPdfDecoderMethods.hasEmbeddedFonts();
        assignBoolean = testPdfDecoderMethods.hasOutline();
        //testPdfDecoderMethods.includeImagesInStream();
        PdfDecoder.init(booleanTest);
        assignBoolean = testPdfDecoderMethods.isEncrypted();
        assignBoolean = testPdfDecoderMethods.isExtractionAllowed();
        assignBoolean = testPdfDecoderMethods.isFileViewable();
        //assignBoolean = testPdfDecoderMethods.isForm();
        assignBoolean = testPdfDecoderMethods.isPageSuccessful();
        assignBoolean = testPdfDecoderMethods.isPasswordSupplied();
        //testPdfDecoderMethods.markAllPagesAsUnread();
        try {
            testPdfDecoderMethods.openPdfArray(byteArrayTest);
        } catch (final PdfException e7) {
            e7.printStackTrace();
        }
        try {
            testPdfDecoderMethods.openPdfFile(stringTest);
        } catch (final PdfException e8) {
            e8.printStackTrace();
        }
        //        try {
        //            testPdfDecoderMethods.openPdfFileFromURL(stringTest,false);
        //        } catch (PdfException e9) {
        //            e9.printStackTrace();
        //        }
        try {
            assignInt = testPdfDecoderMethods.print(graphicsTest, pageFormatTest, intTest);
        } catch (final PrinterException e10) {
            e10.printStackTrace();
        }
        testPdfDecoderMethods.resetViewableArea();
        //testPdfDecoderMethods.setCurrentFormRenderer(acroRendererTest);
        try {
            FontMappings.setDefaultDisplayFont(stringTest);
        } catch (final PdfFontException e11) {
            e11.printStackTrace();
        }
        //testPdfDecoderMethods.setEnableLegacyJPEGConversion(booleanTest);
        testPdfDecoderMethods.setEncryptionPassword(stringTest);
        testPdfDecoderMethods.setExtractionMode(intTest);
        
        testPdfDecoderMethods.setExtractionMode(intTest, floatTest);
        
        testPdfDecoderMethods.setPageFormat(intTest, pageFormatTest);
        testPdfDecoderMethods.setPagePrintRange(intTest, intTest);
        testPdfDecoderMethods.setRenderMode(intTest);
        testPdfDecoderMethods.setStatusBarObject(testStatusBar);
        
        
        testPdfDecoderMethods.waitForDecodingToFinish();
        FontMappings.addFontFile("","");
        
        //FontMappings.setFontDirs(stringArrayTest);
        try {
            assignAffineTransform = testPdfDecoderMethods.getPages().setViewableArea(rectangleTest);
        } catch (final PdfException e12) {
            e12.printStackTrace();
        }
        //testPdfDecoderMethods.showImageableArea();
        //testPdfDecoderMethods.toggleViewportBorder();
        testPdfDecoderMethods.useHiResScreenDisplay(booleanTest);
        assignInt = testPdfDecoderMethods.getScrollInterval();
        testPdfDecoderMethods.setScrollInterval(intTest);
        
        //PdfException
        assignString = testPdfException.getMessage();
        
        //PdfFileInformation
        assignStringArray = PdfFileInformation.getFieldNames();
        assignStringArray = testPdfFileInformation.getFieldValues();
        assignString = testPdfFileInformation.getFileXMLMetaData();
        testPdfFileInformation.setFieldValue(intTest,stringTest);
        testPdfFileInformation.setFileXMLMetaData(new byte[10]);
        
        //PdfGroupingAlgorithms
        //testPdfGroupingAlgorithms.cleanupText(pdfDataTest);
        try {
            assignMap = testPdfGroupingAlgorithms.extractTextAsTable(intTest,intTest,intTest,intTest,intTest,
                    booleanTest,booleanTest,booleanTest, booleanTest,intTest);
        } catch (final PdfException e13) {
            e13.printStackTrace();
        }
        try {
            assignVector = testPdfGroupingAlgorithms.extractTextAsWordlist(intTest,intTest,intTest,intTest,intTest,booleanTest,stringTest);
        } catch (final PdfException e14) {
            e14.printStackTrace();
        }
        try {
            assignString = testPdfGroupingAlgorithms.extractTextInRectangle(intTest,intTest,intTest,intTest,intTest,booleanTest,booleanTest);
        } catch (final PdfException e15) {
            e15.printStackTrace();
        }
        //        try {
        //            assignFloatArray = testPdfGroupingAlgorithms.findTextInRectangle(intTest,intTest,intTest,intTest,intTest,stringTest);
        //        } catch (PdfException e16) {
        //            e16.printStackTrace();
        //        }
        //        try {
        //            assignFloatArray = testPdfGroupingAlgorithms.findTextInRectangle(intTest,intTest,intTest,intTest,intTest,stringTest,intTest);
        //        } catch (PdfException e17) {
        //            e17.printStackTrace();
        //        }
        
        //PdfImageData
        testPdfImageData.clearImageData();
        assignInt = testPdfImageData.getImageCount();
        assignFloat = testPdfImageData.getImageHeight(intTest);
        assignString = testPdfImageData.getImageName(intTest);
        assignInt = testPdfImageData.getImagePageID(intTest);
        assignFloat = testPdfImageData.getImageWidth(intTest);
        assignFloat = testPdfImageData.getImageXCoord(intTest);
        assignFloat = testPdfImageData.getImageYCoord(intTest);
        testPdfImageData.setImageInfo(stringTest,intTest,floatTest,floatTest,floatTest,floatTest);
        
        //PdfPageData
        testPdfPageData.checkSizeSet(intTest);
        assignInt = testPdfPageData.getCropBoxHeight(intTest);
        assignInt = testPdfPageData.getCropBoxWidth(intTest);
        assignInt = testPdfPageData.getCropBoxX(intTest);
        assignInt = testPdfPageData.getCropBoxY(intTest);
        assignInt = testPdfPageData.getScaledCropBoxHeight(intTest);
        assignInt = testPdfPageData.getScaledCropBoxWidth(intTest);
        assignInt = testPdfPageData.getScaledCropBoxX(intTest);
        assignInt = testPdfPageData.getScaledCropBoxY(intTest);
        assignString = testPdfPageData.getCropValue(intTest);
        assignInt = testPdfPageData.getMediaBoxHeight(intTest);
        assignInt = testPdfPageData.getMediaBoxWidth(intTest);
        assignInt = testPdfPageData.getMediaBoxX(intTest);
        assignInt = testPdfPageData.getMediaBoxY(intTest);
        assignInt = testPdfPageData.getScaledMediaBoxHeight(intTest);
        assignInt = testPdfPageData.getScaledMediaBoxWidth(intTest);
        assignInt = testPdfPageData.getScaledMediaBoxX(intTest);
        assignInt = testPdfPageData.getScaledMediaBoxY(intTest);
        assignString = testPdfPageData.getMediaValue(intTest);
        assignInt = testPdfPageData.getRotation(intTest);
        testPdfPageData.setCropBox(new float[]{0,0,0,0});
        testPdfPageData.setMediaBox(new float[]{0,0,0,0});
        testPdfPageData.setPageRotation(intTest,intTest);
        
        //PdfPanel
        //testPdfPanel.addHiglightedObject(rectangleTest,colorTest);
        //testPdfPanel.addMergingDisplayForDebugging(vectorIntTest,vectorShapeTest,intTest,colorArrayTest);
        //testPdfPanel.disableBorderForPrinting();
        testPdfPanel.ensurePointIsVisible(pointTest);
        //assignRectangle = testPdfPanel.getCombinedAreas(rectangleTest,booleanTest);
        assignDimension = testPdfPanel.getMaximumSize();
        assignDimension = testPdfPanel.getMinimumSize();
        //assignBufferedImage = testPdfPanel.getPageAsThumbnail(intTest,null);
        //assignRectangleArray = testPdfPanel.getPageHotspots();
        assignInt = testPdfPanel.getPDFHeight();
        assignInt = testPdfPanel.getPDFWidth();
        assignDimension = testPdfPanel.getPreferredSize();
        //assignInt = testPdfPanel.getRawPDFHeight();
        //assignInt = testPdfPanel.getRawPDFWidth();
        assignString = testPdfPanel.getToolTipText(mouseEventTest);
        testPdfPanel.paint(graphicsTest);
        testPdfPanel.paintComponent(graphicsTest);
        //testPdfPanel.removeHiglightedObject();
        //testPdfPanel.repaintArea(rectangleTest,intTest);
        //testPdfPanel.setDebugDisplay(booleanTest);
        //testPdfPanel.setDebugView(intTest,booleanTest);
        //testPdfPanel.setDrawCrossHairs(booleanTest,intTest,colorTest);
        testPdfPanel.setHardwareAccelerationforScreen(booleanTest);
        testPdfPanel.setInset(intTest,intTest);
        testPdfDecoderMethods.setPageParameters(floatTest,intTest);
        testPdfDecoderMethods.setPageParameters(floatTest,intTest,intTest);
        //testPdfPanel.setPageRotation(intTest);
        testPdfPanel.setPDFBorder(borderTest);
        testPdfPanel.updateCursorBoxOnScreen(rectangleTest,colorTest.getRGB());
        
        //PdfSecurityException
        assignString = testPdfSecurityException.getMessage();
        
        //Vector_Int
        testVector_Int.add_together(intTest,intTest);
        testVector_Int.addElement(intTest);
        testVector_Int.clear();
        assignBoolean = testVector_Int.contains(intTest);
        testVector_Int.deleteElementWithValue(intTest);
        assignInt = testVector_Int.elementAt(intTest);
        
        assignIntArray = testVector_Int.get();
        assignInt = testVector_Int.getCapacity();
        testVector_Int.keep_larger(intTest,intTest);
        testVector_Int.keep_smaller(intTest,intTest);
        assignInt = testVector_Int.pull();
        testVector_Int.push(intTest);
        testVector_Int.removeElementAt(intTest);
        testVector_Int.reuse();
        testVector_Int.set(intArrayTest);
        testVector_Int.setElementAt(intTest,intTest);
        assignInt = testVector_Int.size();
        assignString = testVector_Int.toString();
        
        //Vector_Object
        testVector_Object.addElement(objectTest);
        testVector_Object.clear();
        assignBoolean = testVector_Object.contains(objectTest);
        assignObject = testVector_Object.elementAt(intTest);
        
        assignObjectArray = testVector_Object.get();
        assignObject = testVector_Object.pull();
        testVector_Object.push(objectTest);
        testVector_Object.removeElementAt(intTest);
        testVector_Object.set(objectArrayTest);
        testVector_Object.setElementAt(objectTest,intTest);
        assignInt = testVector_Object.size();
        
        //Vector_String
        testVector_String.addElement(stringTest);
        testVector_String.clear();
        assignBoolean = testVector_String.contains(stringTest);
        assignString = testVector_String.elementAt(intTest);
        
        assignStringArray = testVector_String.get();
        testVector_String.merge(intTest,intTest,stringTest);
        testVector_String.removeElementAt(intTest);
        testVector_String.set(stringArrayTest);
        testVector_String.setElementAt(stringTest,intTest);
        assignInt = testVector_String.size();
    }
    
    public static void javascriptMethods(){
//        AcroRenderer acro = new AcroRenderer(true);
        //acro.updateChangedForms();
        
        final JpedalDefaultJavascript defscript = new JpedalDefaultJavascript((Scriptable)new Object(),(new Context()));
        final Object d=defscript.printd("",(Scriptable)new Object());
        final Object d2=defscript.printd(0,(Scriptable)new Object());
        final Map m= JpedalDefaultJavascript.crackURL("");
        defscript.runtimeHighlight = false;
        final double d3= JpedalDefaultJavascript.z("", (double) 10);
        JpedalDefaultJavascript.beep(1);
        
        JpedalDefaultJavascript.calculate = 0;
        final String testString = JpedalDefaultJavascript.viewerType;
        final int testInt = JpedalDefaultJavascript.viewerVersion;
        
        //    	DisplayJavascriptActions display = new DisplayJavascriptActions();
        int testVal = DisplayJavascriptActions.visible;
        testVal = DisplayJavascriptActions.hidden;
        testVal = DisplayJavascriptActions.noPrint;
        testVal = DisplayJavascriptActions.noView;
        float[] testcol = DisplayJavascriptActions.transparent;//[ "T" ]
        testcol = DisplayJavascriptActions.black;//[ "G", 0 ]
        testcol = DisplayJavascriptActions.white;//[ "G", 1 ]
        testcol = DisplayJavascriptActions.red;//[ "RGB", 1,0,0 ]
        testcol = DisplayJavascriptActions.green;//[ "RGB", 0,1,0 ]
        testcol = DisplayJavascriptActions.blue;//[ "RGB", 0, 0, 1 ]
        testcol = DisplayJavascriptActions.cyan;//[ "CMYK", 1,0,0,0 ]
        testcol = DisplayJavascriptActions.magenta;//[ "CMYK", 0,1 0,0 ]
        testcol = DisplayJavascriptActions.yellow;//[ "CMYK", 0,0,1,0 ]
        testcol = DisplayJavascriptActions.dkGray;//[ "G", 0.25 ]
        testcol = DisplayJavascriptActions.gray;//[ "G", 0.5 ]
        testcol = DisplayJavascriptActions.ltGray;
        final float[] color = DisplayJavascriptActions.convertToColorFloatArray("");
        
        final FormObject form = new FormObject();

        // @oldJS
        Object val = form.getValue();//need to kept as java strings
        form.setValue("");
        form.setLineWidth(1);
        form.setBorderWidth(1);

        val=form.buttonGetCaption();
        form.buttonGetCaption(1);
        form.buttonSetCaption("");
        form.buttonSetCaption("",1);
        form.setfillColor(new float[]{});
        form.setfillColor(new Object());
        val=form.getfillColor();

        final Object[] forms = {new FormObject(),new FormObject()};
        
        final PdfLayerList layerlist = new PdfLayerList();
        final Object[] objs=layerlist.getOCGs();

        //currently no javascript methods associated, but is added to the javascript engine.
    }
    
    /**
     * static methods
     */
    public void staticMethods(){
        //ColorSaceConvertor
        assignBufferedImage = ColorSpaceConvertor.algorithmicConvertCMYKImageToRGB(byteArrayTest,intTest,intTest);
        assignBufferedImage = ColorSpaceConvertor.convertColorspace(bufferedImageTest,intTest);
        assignBufferedImage = ColorSpaceConvertor.convertToARGB(bufferedImageTest);
        assignBufferedImage = ColorSpaceConvertor.convertToRGB(bufferedImageTest);
        
        //LogWriter
        //LogWriter.debugFile(stringTest);
        //LogWriter.noLogging();
        LogWriter.resetLogFile();
        LogWriter.setupLogFile(stringTest);
        //assignBoolean = LogWriter.testLogFileWriteable();
        //LogWriter.write(mapTest,intTest);
        LogWriter.writeLog(stringTest);
        //LogWriter.writeLogWithoutCR(stringTest);
        
        //PdfGroupingAlgorithms
        assignString = PdfGroupingAlgorithms.removeHiddenMarkers(stringTest);
        
        //Strip
        assignString = Strip.convertToText(stringTest,true);
        assignString = Strip.removeMultipleSpacesAndReturns(stringTest);
        assignString = Strip.stripAllSpaces(stringTest);
        assignStringBuffer = Strip.stripAllSpaces(stringBufferTest);
        assignString = Strip.stripComment(stringTest);
        assignString = Strip.stripSpaces(stringTest);
        assignStringBuffer = Strip.stripXML(stringTest,true);
        assignStringBuffer = Strip.trim(stringBufferTest);
    }
}
