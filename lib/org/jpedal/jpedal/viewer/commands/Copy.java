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
 * Copy.java
 * ---------------
 */
package org.jpedal.examples.viewer.commands;


import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.commands.generic.GUICopy;
import org.jpedal.gui.GUIFactory;

/**
 * Copies the text a user has selected/highlighted.
 */
public class Copy extends GUICopy{

    public static void execute(final GUIFactory currentGUI, final PdfDecoderInt decode_pdf, final Values commonValues) {
        final String copyText = copySelectedText(decode_pdf, currentGUI, commonValues);
        final StringSelection ss = new StringSelection(copyText);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }

}
