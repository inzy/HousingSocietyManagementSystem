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
 * GUISearchList.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.generic;

import java.util.Map;

public interface GUISearchList {

    public static final int NO_RESULTS_FOUND = 1;
    public static final int SEARCH_COMPLETE_SUCCESSFULLY = 2;
    public static final int SEARCH_INCOMPLETE = 4;
    public static final int SEARCH_PRODUCED_ERROR = 8;
    
	public Map getTextPages();

	public Map textAreas();
	
	/**
	 * Find out the current amount of results found
	 * @return the amount of search results found
	 */
	public int getResultCount();
	
	public void setSearchTerm(String term);
	
	public String getSearchTerm();
	
	public int getSelectedIndex();
	
	public void setSelectedIndex(int index);
	
	//public int getStatus();

	//public void setStatus(int status);
}
