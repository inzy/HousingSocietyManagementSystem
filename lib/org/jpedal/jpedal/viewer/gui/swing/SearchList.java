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
 * SearchList.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.swing;

import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Map;

import javax.swing.DefaultListModel;

import org.jpedal.examples.viewer.gui.generic.GUISearchList;
import org.jpedal.utils.Messages;


/**used by search function ro provide page number as tooltip*/
public class SearchList extends javax.swing.JList implements GUISearchList {

	private final Map textPages;
	private final Map textAreas;
	private String pageStr="Page";
	//private int Length = 0;
    
    //int status = NO_RESULTS_FOUND;
    
	private String searchTerm = "";

	/**
	 * Constructor that will set up the search list and store highlight areas
	 * internally so the search highlights can be manipulated externally.
	 * @param listModel :: List of teasers
	 * @param textPages :: Map of key to page of result
	 * @param textAreas :: Map of key to highlight area
	 */
	public SearchList(final DefaultListModel listModel, final Map textPages, final Map textAreas) {
		super(listModel);
		
		//Length = listModel.capacity();
		
		this.textPages=textPages;
		this.textAreas=textAreas;
		pageStr=Messages.getMessage("PdfViewerSearch.Page")+ ' ';
	}

	@Override
    public String getToolTipText(final MouseEvent event){
	
		final int index=this.locationToIndex(event.getPoint());
		
		final Object page=textPages.get(index);
		
		if(page!=null) {
            return pageStr + page;
        } else {
            return null;
        }
	}

	@Override
    public Map getTextPages() {
		return Collections.unmodifiableMap(textPages);
	}

	@Override
    public Map textAreas() {
		return Collections.unmodifiableMap(textAreas);
	}
	
	/**
	 * Find out the current amount of results found
	 * @return the amount of search results found
	 */
	@Override
    public int getResultCount(){
		return textAreas.size();
	}

	//public void setLength(int length) {
	//	Length = length;
	//}
	
	@Override
    public void setSearchTerm(final String term){
		this.searchTerm = term;
	}
	
	@Override
    public String getSearchTerm(){
		return searchTerm;
	}
	
	//public int getStatus() {
	//	return status;
	//}

	//public void setStatus(int status) {
	//	this.status = status;
	//}
}
