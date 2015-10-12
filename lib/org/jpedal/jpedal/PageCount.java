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
 * PageCount.java
 * ---------------
 */

package org.jpedal.examples;
import java.io.File;

import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;

/**
 * <h2><b>This Example Opens a PDF and Gets the Number of Pages it Contains.</b></h2>
 * 
 * <p><a href="http://www.idrsolutions.com/java-pdf-library-support/">See our Support Pages for more information on JPedal features.</a></p>
 */
public class PageCount
{

    /**user dir in which program can write*/
    private String user_dir = System.getProperty( "user.dir" );

    /**sample file which can be setup - substitute your own. */
    private static final String test_file = "/mnt/win_d/sample.pdf";

    /**example method to open a file and return the number of pages*/
    public PageCount( final String file_name )
    {
        final String separator = System.getProperty( "file.separator" );

        //check output dir has separator
        if(!user_dir.endsWith(separator)) {
            user_dir += separator;
        }

        /**
         * set up PdfDecoder object telling
         * it whether to display messages
         * and where to find its lookup tables
         */
        final PdfDecoderInt decode_pdf;

        //PdfDecoder returns a PdfException if there is a problem
        try
        {
            decode_pdf = new PdfDecoderServer( false ); //false as no GUI display needed

            /**
             * open the file (and read metadata including pages in  file)
             */
            System.out.println( "Opening file :" + file_name );
            decode_pdf.openPdfFile( file_name );

            /**get page number*/
            System.out.println( "Page count=" + decode_pdf.getPageCount() );

            /**close the pdf file*/
            decode_pdf.closePdfFile();

        }
        catch( final Exception e )
        {
            System.err.println( "5.Exception " + e + " in pdf code" );

        }


    }
    //////////////////////////////////////////////////////////////////////////
    /**
     * main routine which checks for any files passed and runs the demo
     */
    public static void main( final String[] args )
    {
        System.out.println( "Simple demo to extract number of pages" );

        //set to default
        String file_name = test_file;

        //check user has passed us a filename and use default if none
        if( args.length != 1 ) {
            System.out.println("Default test file used");
        } else{
            file_name = args[0];
            System.out.println( "File :" + file_name );
        }

        //check file exists
        final File pdf_file = new File( file_name );

        //if file exists, open and get number of pages
        if(!pdf_file.exists()) {
            System.out.println("File " + file_name + " not found");
        } else{
            new PageCount( file_name );
        }
    }
}
