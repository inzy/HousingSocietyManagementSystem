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
 * ExampleActionHandler.java
 * ---------------
 */

package org.jpedal.examples.handlers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.external.JPedalActionHandler;
import org.jpedal.external.Options;
import org.jpedal.gui.GUIFactory;

public class ExampleActionHandler extends JFrame {
	public static void main(final String[] args) {
		new ExampleActionHandler();
	}

	public ExampleActionHandler() {
		/** add the Viewer component */
		final Viewer viewer = new Viewer();
		viewer.setRootContainer(getContentPane());
		
		/** Initiate viewer */
		viewer.setupViewer();
		
		/** create a new JPedalActionHandler implementation */
		final JPedalActionHandler helpAction = new JPedalActionHandler() {
			@Override
            public void actionPerformed(final GUIFactory currentGUI, final Commands commands) {
                currentGUI.showMessageDialog("Custom help dialog", "JPedal Help", JOptionPane.INFORMATION_MESSAGE);
			}
		};
		
		/** add the implementation to a Map, with its corresponding command, in this case Commands.HELP */
		final Map actions = new HashMap();
		actions.put(Commands.HELP, helpAction);
		
		/** pass the map into the external handler */
		viewer.addExternalHandler(actions, Options.JPedalActionHandler);
		
		/** display the Viewer */
		displayViewer();
	}

	private void displayViewer() {
		final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int width = d.width / 2;
        final int height = d.height / 2;
        if (width < 700) {
            width = 700;
        }
		
		setSize(width, height);
		setLocationRelativeTo(null); //centre on screen
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
