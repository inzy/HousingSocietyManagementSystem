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
 * ApiParams.java
 * ---------------
 */
package org.jpedal.examples.api;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Iterator;
import java.util.Map;
import javax.print.attribute.SetOfIntegerSyntax;
import javax.swing.border.Border;
import org.jpedal.display.Display;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.io.StatusBar;
import org.jpedal.objects.Javascript;
import org.jpedal.objects.PageOrigins;
import org.jpedal.objects.PdfData;
import org.jpedal.objects.PdfFileInformation;
import org.jpedal.objects.PdfImageData;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.acroforms.AcroRenderer;
import org.jpedal.objects.outlines.OutlineData;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.text.TextLines;
import org.jpedal.utils.DPIFactory;
import org.w3c.dom.Document;

/**
 *
 * @author George
 */
public class ApiParams {
    
    boolean booleanValue;
    
    Document documentValue;
    
    int intValue;
    
    final int[] intArrayValue = {0};
    
    Display displayValue;
    
    Iterator iteratorValue;
    
    OutlineData outlineDataValue;
    
    PdfData pdfDataValue;
    
    PdfPageData pdfPageDataValue;
    
    BufferedImage bufferedImageValue;
    
    Map mapValue;
    
    float floatValue;
    
    final float[] floatArrayValue = {0};
    
    PdfImageData pdfImageDataValue;
    
    PdfFileInformation pdfFileInformationValue;
    
    DPIFactory dPIFactoryValue;
    
    DynamicVectorRenderer dynamicVectorRendererValue;
    
    PdfObjectReader pdfObjectReaderValue;
    
    Color colorValue;
    
    Color[] colorArrayValue;
    
    Object objectValue;
    
    Object[] objectArrayValue;
    
    String stringValue;
    
    //byte byteValue;
    
    byte[] byteArrayValue;
    
    Certificate certificateValue;
    
    PrivateKey privateKeyValue;
    
    InputStream inputStreamValue;
    
    AcroRenderer acroRendererValue;
    
    Javascript javascriptValue;
    
    ObjectStore objectStoreValue;
    
    DecoderOptions decoderOptionsValue;
    
    PdfGroupingAlgorithms pdfGroupingAlgorithmsValue;
    
    TextLines textLinesValue;
    
    //UIViewerInt uIViewerIntValue;
    
    //ViewerInt viewerIntValue;
    
    //SwingPrinter swingPrinterValue;
    
    AffineTransform affineTransformValue;
    
    int[] rectangleValue;
    
    Graphics graphicsValue;
    
    PageFormat pageFormatValue;
    
    Graphics2D graphics2DValue;
    
    StatusBar statusBarValue;
    
    Cursor cursorValue;
    
    Printable printableValue;
    
    Point pointValue;
    
    Dimension dimensionValue;
    
    Border borderValue;
    
    SetOfIntegerSyntax setOfIntegerSyntaxValue;
    
    StringBuffer stringBufferValue;
    
    PageOrigins pageOriginsValue;
    
    public ApiParams() {
        
    }
    
}
