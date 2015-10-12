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
 * ShowPageSize.java
 * ---------------
 */

package org.jpedal.examples;

import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.objects.PdfPageData;

/**
 * <h2><b>Example to Show Page Size of all Pages on System</b></h2>
 * 
 * <p><a href="http://www.idrsolutions.com/java-pdf-library-support/">See our Support Pages for more information on JPedal features.</a></p>
 */
public class ShowPageSize {

    public ShowPageSize(final String file_name){

        final PdfDecoderInt decode_pdf = new PdfDecoderServer( false ); //false as no display

        try{
        decode_pdf.openPdfFile( file_name );

            /**get page count*/
			final int pageCount= decode_pdf.getPageCount();
			System.out.println( "Page count=" + pageCount );


            //get PageData object
            final PdfPageData pageData = decode_pdf.getPdfPageData();
            //show all page sizes
            for(int ii=0;ii<pageCount;ii++){

                if(pageData.getRotation(ii)!=0){
                    System.out.println("Has rotation "+pageData.getRotation(ii)+" degrees");
                }

                //pixels
                System.out.print("page (size in pixels) "+ii+
                        " mediaBox="+pageData.getMediaBoxX(ii)+ ' ' +pageData.getMediaBoxY(ii)+ ' ' +pageData.getMediaBoxWidth(ii)+ ' ' +pageData.getMediaBoxHeight(ii)+
                        " CropBox="+pageData.getCropBoxX(ii)+ ' ' +pageData.getCropBoxY(ii)+ ' ' +pageData.getCropBoxWidth(ii)+ ' ' +pageData.getCropBoxHeight(ii));

                //inches
                float factor=72f; //72 is the usual screen dpi
                System.out.print(" (size in inches) "+ii+
                        " mediaBox="+pageData.getMediaBoxX(ii)/factor+ ' ' +pageData.getMediaBoxY(ii)/factor+ ' ' +pageData.getMediaBoxWidth(ii)/factor+ ' ' +pageData.getMediaBoxHeight(ii)/factor+
                        " CropBox="+pageData.getCropBoxX(ii)/factor+ ' ' +pageData.getCropBoxY(ii)/factor+pageData.getCropBoxWidth(ii)/factor+ ' ' +pageData.getCropBoxHeight(ii)/factor);

                //cm
                factor=72f/2.54f;
                System.out.print(" (size in cm) "+ii+
                        " mediaBox="+pageData.getMediaBoxX(ii)/factor+ ' ' +pageData.getMediaBoxY(ii)/factor+ ' ' +pageData.getMediaBoxWidth(ii)/factor+ ' ' +pageData.getMediaBoxHeight(ii)/factor+
                        " CropBox="+pageData.getCropBoxX(ii)/factor+ ' ' +pageData.getCropBoxY(ii)/factor+pageData.getCropBoxWidth(ii)/factor+ ' ' +pageData.getCropBoxHeight(ii)/factor+ '\n');

            }

			/**close the pdf file*/
			decode_pdf.closePdfFile();
        }catch(final Exception e){
            e.printStackTrace();
        }
    }

    /** main method to run the software as standalone application */
	public static void main(final String[] args) {
        if(args.length!=1){
            System.out.println("Please pass in file name (including path");
        }else{
            new ShowPageSize(args[0]);
        }
    }
}
