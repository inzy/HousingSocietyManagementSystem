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
 * RecentDocumentsFactory.java
 * ---------------
 */

package org.jpedal.examples.viewer;

import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.gui.generic.GUISearchWindow;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.gui.GUIFactory;

public interface RecentDocumentsFactory {
    
    public String getPreviousDocument();

    public String getNextDocument();
    
    public void updateRecentDocuments(String[] recentDocs);
    
    public void enableRecentDocuments(boolean enable);
    
    public void clearRecentDocuments(PropertiesFile properties);
    
    public void addToFileList(String selectedFile);
    
    public void createMenuItems(String fileNameToAdd, int position, GUIFactory currentGUI,
                                Values commonValues, PdfDecoderInt decode_pdf, PropertiesFile properties,
                                GUIThumbnailPanel thumbnails, GUISearchWindow searchFrame);
    
}
