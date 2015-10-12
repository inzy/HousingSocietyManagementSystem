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
 * ShowLinks.java
 * ---------------
 */

/**
 * This example opens a pdf file and gets the document links
 */
package org.jpedal.examples;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.examples.handlers.DefaultImageHelper;
import org.jpedal.objects.raw.*;

public class ShowLinks {

    /**flag to add links to image of page as well if set*/
    private static final boolean includeImages = true;

    /**example method to open a file and return the links*/
    public ShowLinks(final String file_name) {

        BufferedImage img = null;

        final PdfDecoderInt decodePdf;

        if (includeImages) {
            decodePdf = new PdfDecoderServer(true);
        } else {
            decodePdf = new PdfDecoderServer(false);
        }


        try {
            decodePdf.openPdfFile(file_name);


            /**
             * form code here
             */
            //new list we can parse
            for (int ii = 1; ii < decodePdf.getPageCount() + 1; ii++) {

                //the list of Annots from the file
                final PdfArrayIterator annotListForPage = decodePdf.getFormRenderer().getAnnotsOnPage(ii);


                if (annotListForPage != null && annotListForPage.getTokenCount() > 0) { //can have empty lists


                    //get image if needed and save
                    if (includeImages) {
                        img = decodePdf.getPageAsImage(ii);

                    }

                    while (annotListForPage.hasMoreTokens()) {

                        //get ID of annot which has already been decoded and get actual object
                        final String annotKey = annotListForPage.getNextValueAsString(true);

                        //each PDF annot object - extract data from it
                        final FormObject annotObj = decodePdf.getFormRenderer().getFormObject(annotKey);

                        final int subtype = annotObj.getParameterConstant(PdfDictionary.Subtype);

                        if (subtype == PdfDictionary.Link) {

                            //PDF co-ords
                            System.out.println("link object");
                            final float[] coords = annotObj.getFloatArray(PdfDictionary.Rect);
                            System.out.println("PDF Rect= " + coords[0] + ' ' + coords[1] + ' ' + coords[2] + ' ' + coords[3]);

                            //convert to Javaspace rectangle by subtracting page Crop Height
                            final int pageH = decodePdf.getPdfPageData().getCropBoxHeight(ii);
                            final float x = coords[0];

                            final float w = coords[2] - coords[0];
                            final float h = coords[3] - coords[1];
                            final float y = pageH - coords[1] - h; //note we remove h from y
                            System.out.println("Javaspace Rect x=" + x + " y=" + y + " w=" + w + " h=" + h);

                            //draw on image as example
                            //get image if needed and save
                            if (includeImages) {

                                //as an example draw onto page
                                final Graphics2D g2 = (Graphics2D) img.getGraphics();
                                g2.setPaint(Color.RED);
                                g2.drawRect((int) x, (int) y, (int) w, (int) h);

                            }

                            //text in A subobject
                            final PdfObject aData = annotObj.getDictionary(PdfDictionary.A);
                            if (aData != null && aData.getNameAsConstant(PdfDictionary.S) == PdfDictionary.URI) {
                                final String text = aData.getTextStreamValue(PdfDictionary.URI); //+"ZZ"; deliberately broken first to test checking
                                System.out.println("text=" + text);
                            }
                        }
                    }  
                }

                //get image if needed and save
                if (includeImages) {

                    DefaultImageHelper.write(img, "PNG", "image-" + ii + ".png");
                }
            }

            /**close the pdf file*/
            decodePdf.closePdfFile();

        } catch (final Exception e) {
            e.printStackTrace();

        }
    }

    //////////////////////////////////////////////////////////////////////////
    /**
     * main routine which checks for any files passed and runs the sample code
     */
    public static void main(final String[] args) {
        System.out.println("Simple demo to extract pdf file links if any");

        //set to default
        final String file_name;

        //check user has passed us a filename
        if (args.length != 1) {
            System.out.println("No filename given or  wrong number of values");
        } else {
            file_name = args[0];
            System.out.println("File :" + file_name);

            //check file exists
            final File pdf_file = new File(file_name);

            //if file exists, open and show links
            if (!pdf_file.exists()) {
                System.out.println("File " + file_name + " not found");
            } else {
                new ShowLinks(file_name);
            }
        }

        System.exit(1);


    }
}
