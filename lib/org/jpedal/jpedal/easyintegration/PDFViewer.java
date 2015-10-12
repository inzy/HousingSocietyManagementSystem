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
 * PDFViewer.java
 * ---------------
 */

package org.jpedal.examples.easyintegration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Viewer;

public class PDFViewer {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		
		//<link><a name="containers" />
		//Create display frame
		final JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new BorderLayout());
		
		/**
		 * possible options to add
		 */
		
		//All the main components most commonly used work, and others work to.
		////////////////////////////////////////////////////////
		final JInternalFrame rootContainer = new JInternalFrame("INTERNAL FRAME 1");
//		JPanel rootContainer = new JPanel();	
//		JTabbedPane rootContainer = new JTabbedPane();
//		JScrollPane rootContainer = new JScrollPane();
//		JLayeredPane rootContainer = new JLayeredPane();
//		JRootPane rootContainer = new JRootPane(); //not recommended for general usage
//		JSplitPane rootContainer = new JSplitPane();
		////////////////////////////////////////////////////////
		
		
		//Additional Label to show this is another program
		final JLabel label = new JLabel("This is a very simple program.");
		label.setFont(new Font("Lucida", Font.BOLD, 20));
		label.setForeground(Color.RED);
		frame.add(label, BorderLayout.NORTH);
		
		
		
		//The only two lines required to setup viewer for your software
		////////////////////////////////////////////////////////
		final Viewer viewer = new Viewer(rootContainer, null);
		viewer.setupViewer();
		////////////////////////////////////////////////////////
		//You can remove our GUI by using the options within viewer in the menu View->Preferences->Menu

		//Add the viewer to your application
		frame.add(rootContainer, BorderLayout.CENTER);
		
		
		//Require for internalFrame to be displayed
		rootContainer.setVisible(true);
		
		//Set up JFrame
		frame.setTitle("Viewer in External Viewer");
		frame.setSize(800, 600);
		frame.addWindowListener(new WindowListener(){
			@Override
            public void windowActivated(final WindowEvent e) {}
			@Override
            public void windowClosed(final WindowEvent e) {}
			@Override
            public void windowClosing(final WindowEvent e) {System.exit(1);}
			@Override
            public void windowDeactivated(final WindowEvent e) {}
			@Override
            public void windowDeiconified(final WindowEvent e) {}
			@Override
            public void windowIconified(final WindowEvent e) {}
			@Override
            public void windowOpened(final WindowEvent e) {}
		});
		
		//Display Frame
		frame.setVisible(true);
		
		final Object[] input;
		
		//Specify file you wish to open (JPedal handles getting the byte data)
		input = new Object[]{"/PDFData/Hand_Test/crbtrader.pdf"};
		viewer.executeCommand(Commands.OPENFILE, input);

		/**
		 * Below is some example code to execute commands from viewer.
		 * The commands can be executed using the executeCommand method using
		 * static varibles from Commands.java and in some cases an object array
		 * to pass in input values.
		 */
//		//Create buffer for testing purposes
//		String filename = "/PDFData/Hand_Test/crbtrader.pdf";
//		File fileSize=new File(filename);
//		byte[] data=new byte[(int) fileSize.length()];
//
//		FileInputStream fis= null;
//		try {
//			fis = new FileInputStream(filename);
//			fis.read(data);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		Open file by passing in the filename and a byte array
//		input = new Object[]{data, filename};
//		viewer.executeCommand(Commands.OPENFILE, input);
//
//		Specify how many page you wish to move forward
//		input[0] = new Object[]{"2"};
//		viewer.executeCommand(Commands.FORWARDPAGE, input);
//		
//		Specify how many pages you wish to move back
//		input[0] = new Object[]{"1"};
//		viewer.executeCommand(Commands.BACKPAGE, input);
//		
//		What rotation you wish to view the page at (0,90,180,270).
//		input[0] = new Object[]{"90"};
//		viewer.executeCommand(Commands.ROTATION, input);
//		
//		Specify a url to open
//		input[0] = new Object[]{"http://www.cs.bham.ac.uk/~axj/pub/papers/handy1.pdf"};
//		viewer.executeCommand(Commands.OPENURL, input);
//		
//		Specify the scaling to view the current pdf at
//		input[0] = new Object[]{"300"};
//		viewer.executeCommand(Commands.SCALING, input);
	}

}
