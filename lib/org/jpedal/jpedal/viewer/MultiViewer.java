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
 * MultiViewer.java
 * ---------------
 */

package org.jpedal.examples.viewer;

import javax.swing.*;
import org.jpedal.gui.GUIFactory;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.LogWriter;

/** <h2><b>Multi View PDF Viewer</b></h2>
 * <p>Enhance the  PDF Viewer with the ability to include multiple windows for opening more than 1 PDF</p>
 *
 * <p><b>Run directly from jar with java -cp jpedal.jar org/jpedal/examples/viewer/MultiViewer</b></p>
 *
 * <p><a href="http://files.idrsolutions.com/samplecode/org/jpedal/examples/viewer/Viewer.java.html">See Our Swing Viewer Example Code</a></p>
 */

public class MultiViewer extends Viewer {

	/**
	 * setup and run client
	 */
	public MultiViewer() {

		//tell user we are in multipanel display
		currentGUI.setDisplayMode(GUIFactory.MULTIPAGE);

		//enable error messages which are OFF by default
		DecoderOptions.showErrorMessages=true;

//		//Search Frame style to Use
//		//0 = external window
//		//1 = search tab
//		//2 = Button Bar
//		searchFrame.setStyle(2);

	}

	/**
	 * setup and run client passing in paramter to show if
	 * running as applet, webstart or JSP (only applet has any effect
	 * at present)
	 */
	public MultiViewer(final int modeOfOperation) {

		//tell user we are in multipanel display
		currentGUI.setDisplayMode(GUIFactory.MULTIPAGE);

		//enable error messages which are OFF by default
		DecoderOptions.showErrorMessages=true;

		commonValues.setModeOfOperation(modeOfOperation);

		
	}

	/** main method to run the software as standalone application */
	public static void main(final String[] args) {

		/**
		 * set the look and feel for the GUI components to be the
		 * default for the system it is running on
		 */
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		}catch (final Exception e) {
			LogWriter.writeLog("Exception " + e + " setting look and feel");
		}

		final MultiViewer current = new MultiViewer();
        current.setupViewer();

		
	}

}
