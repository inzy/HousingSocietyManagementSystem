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
 * ShowFormNames.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;

import org.jpedal.PdfDecoderInt;
import org.jpedal.objects.acroforms.ReturnValues;

/**
 * Shows the names of all the forms of the current document
 */
public class ShowFormNames {

    public static void execute(final Object[] args, final PdfDecoderInt decode_pdf) {
        if (args == null) {

            final Object[] fieldNames = decode_pdf.getFormRenderer().getFormComponents(null, ReturnValues.FORM_NAMES, -1);
            final StringBuilder buf = new StringBuilder();
            buf.append("forms - \n");
            for (final Object fieldName : fieldNames) {
                buf.append(fieldName);
                buf.append('\n');
            }
            buf.append("END OF LIST");
            System.out.println(buf);

        }
    }
}
