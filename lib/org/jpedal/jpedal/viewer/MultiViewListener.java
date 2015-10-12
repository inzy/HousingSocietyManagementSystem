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
 * MultiViewListener.java
 * ---------------
 */

package org.jpedal.examples.viewer;

import java.io.File;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.commands.OpenFile;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.gui.GUIFactory;

public class MultiViewListener implements InternalFrameListener {

	Object pageScaling, pageRotation;
	private final PdfDecoder decode_pdf;
	private final GUIFactory currentGUI;
	private final Values commonValues;
	private final Commands currentCommands;

	public MultiViewListener(final PdfDecoder decode_pdf, final GUIFactory currentGUI, final Values commonValues, final Commands currentCommands){
		this.decode_pdf = decode_pdf;
		this.currentGUI = currentGUI;
		this.commonValues = commonValues;
		this.currentCommands = currentCommands;

		// pageScaling = "Window";
		// pageRotation = currentGUI.getRotation();

		// System.out.println("constructor"+ " "+pageScaling+" " +pageRotation);
	}

	@Override
    public void internalFrameOpened(final InternalFrameEvent e) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
    public void internalFrameClosing(final InternalFrameEvent e) {
		currentGUI.getButtons().setBackNavigationButtonsEnabled(false);
		currentGUI.getButtons().setForwardNavigationButtonsEnabled(false);
		currentGUI.resetPageNav();
	}

	@Override
    public void internalFrameClosed(final InternalFrameEvent e) {

		decode_pdf.flushObjectValues(true);

		decode_pdf.closePdfFile();
	}

	@Override
    public void internalFrameIconified(final InternalFrameEvent e) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
    public void internalFrameDeiconified(final InternalFrameEvent e) {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	/**
	 * switch to active PDF
	 * @param e
	 */
	@Override
    public void internalFrameActivated(final InternalFrameEvent e) {
		
		//System.out.println("activated pdf = "+decode_pdf.getClass().getName() + "@" + Integer.toHexString(decode_pdf.hashCode()));
		// choose selected PDF
		((GUI)currentGUI).setPdfDecoder(decode_pdf);
		currentCommands.setPdfDecoder(decode_pdf);
		/**
		 * align details in Viewer and variables
		 */
		final int page=decode_pdf.getlastPageDecoded();

		commonValues.setPageCount(decode_pdf.getPageCount());
		commonValues.setCurrentPage(page);

		final String fileName = decode_pdf.getFileName();
		if(fileName!=null){
			commonValues.setSelectedFile(fileName);
			final File file = new File(fileName);
			commonValues.setInputDir(file.getParent());
			commonValues.setFileSize(file.length() >> 10);
		}
		
		// System.err.println("ACTIVATED "+pageScaling+" "+pageRotation+"
		// count="+decode_pdf.getPageCount()/*+"
		// "+localPdf.getDisplayRotation()+" "+localPdf.getDisplayScaling()*/);

		commonValues.setPDF( OpenFile.isPDf);
		commonValues.setMultiTiff(commonValues.isMultiTiff());
		
		//System.err.println("ACTIVATED "+pageScaling+" "+pageRotation+" count="+decode_pdf.getPageCount()/*+" "+localPdf.getDisplayRotation()+" "+localPdf.getDisplayScaling()*/);

		
		if (pageScaling != null) {
            ((GUI) currentGUI).setSelectedComboItem(Commands.SCALING, pageScaling.toString());
        }

		if (pageRotation != null) {
            ((GUI) currentGUI).setSelectedComboItem(Commands.ROTATION, pageRotation.toString());
        }

// currentGUI.setPage(page);
// //pageCounter2.setText(""+page);

// pageCounter3.setText(""+decode_pdf.getPageCount());

		currentGUI.setPageNumber();

		decode_pdf.updateUI();

		currentGUI.removeSearchWindow(false);
// searchFrame.removeSearchWindow(false);
		
		//Only show navigation buttons required for newly activated frame
		currentGUI.getButtons().hideRedundentNavButtons(currentGUI);
	}

	@Override
    public void internalFrameDeactivated(final InternalFrameEvent e) {

		// save current settings
		if (pageScaling != null) {
			pageScaling = ((GUI)currentGUI).getSelectedComboItem(Commands.SCALING);
		}
		if (pageRotation != null) {
			pageRotation = ((GUI)currentGUI).getSelectedComboItem(Commands.ROTATION);
		}
		// System.err.println("DEACTIVATED "+pageScaling+" "+pageRotation);
	}

	public void setPageProperties(final Object rotation, final Object scaling) {
		pageRotation = rotation;
		pageScaling = scaling;
	}
}
