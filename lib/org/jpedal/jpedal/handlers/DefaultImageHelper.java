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
 * DefaultImageHelper.java
 * ---------------
 */
package org.jpedal.examples.handlers;


import org.jpedal.exception.PdfException;
import org.jpedal.external.ImageHelper;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;

/**
 * abstract image saving and reading so we can move between different libraries if ImageIO not sufficient
 **/
public class DefaultImageHelper{

    static ImageHelper customHelper=new StandardImageIO();

    public static void setUserImplementation(final ImageHelper userHelper){
        customHelper=userHelper;
    }

    public static void write(final BufferedImage image, final String type, final String file_name) throws IOException {

        customHelper.write(image, type, file_name);
    }

    public static BufferedImage read(final String file_name) {

        return customHelper.read(file_name);
    }

    public static Raster readRasterFromJPeg(final byte[] data) throws IOException {

        return customHelper.readRasterFromJPeg(data);
    }

    public static BufferedImage read(final byte[] data) throws IOException{

        return customHelper.read(data);

    }

    public static BufferedImage JPEG2000ToRGBImage(final byte[] data, final int w, final int h, final float[] decodeArray, final int pX, final int pY) throws PdfException {


        return customHelper.JPEG2000ToRGBImage(data, w, h, decodeArray, pX, pY);
    }
}
