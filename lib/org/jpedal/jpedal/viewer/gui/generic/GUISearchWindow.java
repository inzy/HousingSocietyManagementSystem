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
 * GUISearchWindow.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.generic;

import java.util.Map;
import org.jpedal.PdfDecoderInt;
import org.jpedal.examples.viewer.Values;

/**abstract level of search window*/ 
public interface GUISearchWindow {
	
    public static final int SEARCH_EXTERNAL_WINDOW = 0;
    public static final int SEARCH_TABBED_PANE = 1;
    public static final int SEARCH_MENU_BAR = 2;
    
	//Varible added to allow multiple search style to be implemented
	//int style = 0;

	void find(PdfDecoderInt decode_pdf, Values values);
	
	void findWithoutWindow(PdfDecoderInt decode_pdf, Values values, int searchType, boolean listOfTerms, boolean singlePageOnly, String searchValue);

	void grabFocusInInput();

	boolean isSearchVisible();
	
	void init(PdfDecoderInt dec, Values values);

	void removeSearchWindow(boolean justHide);
	
	void resetSearchWindow();
	
	GUISearchList getResults();

	GUISearchList getResults(int page);

	Map getTextRectangles();

	int getViewStyle();

	void setViewStyle(int i);

	//boolean isSearching();
	
	public int getFirstPageWithResults();
	
	public void setWholeWords(boolean wholeWords);
	
	public void setCaseSensitive(boolean caseSensitive);
	
	public void setMultiLine(boolean multiLine);
    
    //    public void setSearchHighlightsOnly(boolean highlightOnly);
        
    public void setSearchText(String s);
	
    public void setUpdateListDuringSearch(boolean updateListDuringSearch);
}
