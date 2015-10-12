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
 * GUIThumbnailPanel.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.generic;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.jpedal.objects.PdfPageData;

/**generic version to show thumbnails in panel on side*/
public interface GUIThumbnailPanel {

	boolean isShownOnscreen();

	void terminateDrawing();

	void setIsDisplayedOnscreen(boolean b);

	Object[] getButtons();

	void addComponentListener();

	void generateOtherVisibleThumbnails(int currentPage);

	void setupThumbnails(int pages, Font textFont, String message, PdfPageData pdfPageData);

	//void removeAll();

	//void setupThumbnails(int i, int[] js, int pageCount);

	//void generateOtherThumbnails(String[] strings, Vector_Object thumbnailsStored);

	//void resetHighlightedThumbnail(int id);

	void resetToDefault();

	void removeAllListeners();

	void setThumbnailsEnabled(boolean value);

	//void refreshDisplay();

	void dispose();

    public BufferedImage getImage(int pNum);
    
    public void drawThumbnails();
}
