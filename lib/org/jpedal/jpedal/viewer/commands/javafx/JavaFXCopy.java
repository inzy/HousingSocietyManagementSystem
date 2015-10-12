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
 * JavaFXCopy.java
 * ---------------
 */

package org.jpedal.examples.viewer.commands.javafx;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.commands.generic.GUICopy;
import org.jpedal.gui.GUIFactory;

/**
 * Copies the text a user has selected/highlighted.
 */
public class JavaFXCopy extends GUICopy {
    
    public static void execute(final GUIFactory currentGUI, final PdfDecoderInt decode_pdf, final Values commonValues) {
        final String copyText = copySelectedText(decode_pdf, currentGUI, commonValues);
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(copyText);
        clipboard.setContent(content);
    }
    
}
