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
 * JPanelDemo.java
 * ---------------
 */
package org.jpedal.examples.jpaneldemo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.examples.viewer.utils.FileFilterer;
import org.jpedal.fonts.FontMappings;

/**<h2><b>JPanel Demo</b></h2>
 * <p>A very simple example of a viewer.</p>
 *
 * <p><a href="http://files.idrsolutions.com/samplecode/org/jpedal/examples/viewer/Viewer.java.html">We recommend you look at our fully-featured, totally configurable version</a></p>
 */
public class JPanelDemo extends JFrame {
	
	private static final String viewerTitle="Jpanel Demo";
	
	/**the actual JPanel/decoder object*/
	private final PdfDecoder pdfDecoder;
	
	/**name of current PDF file*/
	private String currentFile;
	
	/**current page number (first page is 1)*/
	private int currentPage=1;
	
	private final JLabel pageCounter1=new JLabel("Page ");
	private final JTextField pageCounter2=new JTextField(4);//000 used to set prefered size
	private final JLabel pageCounter3=new JLabel("of");//000 used to set prefered size
	
	/**
	 * construct a pdf viewer, passing in the full file name
	 */
	public JPanelDemo(final String name){
		
		pdfDecoder = new PdfDecoder(true);


        //ensure non-embedded font map to sensible replacements
        FontMappings.setFontReplacements();

		
		currentFile = name;//store file name for use in page changer
		
		try{
			//this opens the PDF and reads its internal details
			pdfDecoder.openPdfFile(currentFile);
			
			//these 2 lines opens page 1 at 100% scaling
			pdfDecoder.decodePage(currentPage);
			pdfDecoder.setPageParameters(1,1); //values scaling (1=100%). page number
		}catch(final Exception e){
			e.printStackTrace();
		}
		
		//setup our GUI display
		initializeViewer();
		
		//set page number display
		pageCounter2.setText(String.valueOf(currentPage));
		pageCounter3.setText("of "+pdfDecoder.getPageCount());
	}
	
	/**
	 * construct an empty pdf viewer and pop up the open window
	 */
	public JPanelDemo(){
		
		setTitle(viewerTitle);
		
		pdfDecoder = new PdfDecoder(true);

        //ensure non-embedded font map to sensible replacements
        FontMappings.setFontReplacements();

		initializeViewer();
		
//		selectFile();
	}

	/**
	 * opens a chooser and allows user to select a pdf file and opens it
	 */
	private void selectFile() {

        final JFileChooser open = new JFileChooser(".");
		open.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		final String[] pdf = { "pdf" };
		open.addChoosableFileFilter(new FileFilterer(pdf,"Pdf (*.pdf)"));
		
		int resultOfFileSelect = JFileChooser.ERROR_OPTION;
		while(resultOfFileSelect==JFileChooser.ERROR_OPTION){
			
			resultOfFileSelect = open.showOpenDialog(this);
			
			if(resultOfFileSelect==JFileChooser.ERROR_OPTION) {
                System.err.println("JFileChooser error");
            }
			
			if(resultOfFileSelect==JFileChooser.APPROVE_OPTION){
				currentFile = open.getSelectedFile().getAbsolutePath();
				
				currentPage = 1;
				try{
					//close the current pdf before opening another
					pdfDecoder.closePdfFile();
					
//					this opens the PDF and reads its internal details
					pdfDecoder.openPdfFile(currentFile);
					
					//check for password encription and acertain
					if(!checkEncryption()){
						//if file content is not accessable make user select a different file
						resultOfFileSelect = JFileChooser.CANCEL_OPTION;
					}
					
//					these 2 lines opens page 1 at 100% scaling
					pdfDecoder.decodePage(currentPage);
					pdfDecoder.setPageParameters(1,1); //values scaling (1=100%). page number
					pdfDecoder.invalidate();
					
				}catch(final Exception e){
					e.printStackTrace();
				}
				
				//set page number display
				pageCounter2.setText(String.valueOf(currentPage));
				pageCounter3.setText("of "+pdfDecoder.getPageCount());
				
				setTitle(viewerTitle+" - "+currentFile);
				
				repaint();
			}
		}
	}

	/**
	 * check if encryption present and acertain password, return true if content accessable
	 */
	private boolean checkEncryption() {
		
//		check if file is encrypted
		if(pdfDecoder.isEncrypted()){
			
			//if file has a null password it will have been decoded and isFileViewable will return true
			while(!pdfDecoder.isFileViewable()) {
	
				/** popup window if password needed */
				final String password = JOptionPane.showInputDialog(this,"Please enter password");
	
				/** try and reopen with new password */
				if (password != null) {
                    try {
                        pdfDecoder.setEncryptionPassword(password);
                    } catch (final PdfException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    //pdfDecoder.verifyAccess();
	
				}
			}
			return true;
		}
		//if not encrypted return true
		return true;
	}

	/**
	 * setup the viewer and its components
	 */
	private void initializeViewer() {
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		final Container cPane = getContentPane();
		cPane.setLayout(new BorderLayout());
		
		final JButton open = initOpenBut();//setup open button
		final Component[] itemsToAdd = initChangerPanel();//setup page display and changer
		
		final JPanel topBar = new JPanel();
		topBar.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		topBar.add(open);
//		topBar.add(pageChanger);
        for (final Component anItemsToAdd : itemsToAdd) {
            topBar.add(anItemsToAdd);
        }
		
		cPane.add(topBar,BorderLayout.NORTH);
		
		final JScrollPane display = initPDFDisplay();//setup scrollpane with pdf display inside
		cPane.add(display,BorderLayout.CENTER);
		
		pack();
        
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screen.width/2,screen.height/2);

        setLocationRelativeTo(null);//centre on screen

		setVisible(true);
	}
    
	/**
	 * returns the open button with listener
	 */
	private JButton initOpenBut() {
		
		final JButton open = new JButton();
		open.setIcon(new ImageIcon(getClass().getResource("/org/jpedal/examples/viewer/res/open.gif"))); //$NON-NLS-1$
		open.setText("Open");
		open.setToolTipText("Open a file"); 
		open.setBorderPainted(false);
		open.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(final ActionEvent e) {
				    selectFile();
			}
		});
		
		return open;
	}

	/**
	 * returns the scrollpane with pdfDecoder set as the viewport
	 */
	private JScrollPane initPDFDisplay() {
		
		final JScrollPane currentScroll = new JScrollPane();
		currentScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		currentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		currentScroll.setViewportView(pdfDecoder);
        
		return currentScroll;
	}

	/**
	 * setup the page display and changer panel and return it 
	 */
	private Component[] initChangerPanel(){
		
        final Component[] list = new Component[11];
        
		/**back to page 1*/
		final JButton start = new JButton();
		start.setBorderPainted(false);
		final URL startImage =getClass().getResource("/org/jpedal/examples/viewer/res/start.gif");
		start.setIcon(new ImageIcon(startImage));
		start.setToolTipText("Rewind to page 1");
//		currentBar1.add(start);
        list[0] = start;
		start.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(final ActionEvent e) {
			    if(currentFile!=null && currentPage!=1){
			    	currentPage = 1;
			    	try {
						pdfDecoder.decodePage(currentPage);
						pdfDecoder.invalidate();
						repaint();
					} catch (final Exception e1) {
						System.err.println("back to page 1");
						e1.printStackTrace();
					}
			    	
			    	//set page number display
					pageCounter2.setText(String.valueOf(currentPage));
			    }
			}
		});
		
		/**back 10 icon*/
		final JButton fback = new JButton();
		fback.setBorderPainted(false);
		final URL fbackImage =getClass().getResource("/org/jpedal/examples/viewer/res/fback.gif");
		fback.setIcon(new ImageIcon(fbackImage));
		fback.setToolTipText("Rewind 10 pages");
//		currentBar1.add(fback);
        list[1] = fback;
		fback.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(final ActionEvent e) {
				if(currentFile!=null && currentPage>10){
					currentPage -= 10;
			    	try {
						pdfDecoder.decodePage(currentPage);
						pdfDecoder.invalidate();
						repaint();
					} catch (final Exception e1) {
						System.err.println("back 10 pages");
						e1.printStackTrace();
					}
			    	
//			    	set page number display
					pageCounter2.setText(String.valueOf(currentPage));
				}
			}
		});
		
		/**back icon*/
		final JButton back = new JButton();
		back.setBorderPainted(false);
		final URL backImage =getClass().getResource("/org/jpedal/examples/viewer/res/back.gif");
		back.setIcon(new ImageIcon(backImage));
		back.setToolTipText("Rewind one page"); 
//		currentBar1.add(back);
        list[2] = back;
		back.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(final ActionEvent e) {
			if(currentFile!=null && currentPage>1){
				currentPage -= 1;
		    	try {
					pdfDecoder.decodePage(currentPage);
					pdfDecoder.invalidate();
					repaint();
				} catch (final Exception e1) {
					System.err.println("back 1 page");
					e1.printStackTrace();
				}
		    	
//		    	set page number display
				pageCounter2.setText(String.valueOf(currentPage));
			}
			}
		});
		
		pageCounter2.setEditable(true);
		pageCounter2.addActionListener(new ActionListener(){
		    
		    @Override
            public void actionPerformed(final ActionEvent a) {
		        
		        final String value=pageCounter2.getText().trim();
		        final int newPage;
		        
		        //allow for bum values
		        try{
		            newPage=Integer.parseInt(value);
		            
		            if((newPage>pdfDecoder.getPageCount())|(newPage<1)){
		            	return;
		            }
		            
		            currentPage=newPage;
		            try{
		            	pdfDecoder.decodePage(currentPage);
		            	pdfDecoder.invalidate();
						repaint();
		            }catch(final Exception e){
		            	System.err.println("page number entered");
		            	e.printStackTrace();
		            }
		            
		        }catch(final Exception e){
		            JOptionPane.showMessageDialog(null, '>' +value+ "< is Not a valid Value.\nPlease enter a number between 1 and "+pdfDecoder.getPageCount()+ ' ' +e);
				}
		        
		    }
		    
		});
		
		/**put page count in middle of forward and back*/
//		currentBar1.add(pageCounter1);
//		currentBar1.add(new JPanel());//add gap
//		currentBar1.add(pageCounter2);
//		currentBar1.add(new JPanel());//add gap
//		currentBar1.add(pageCounter3);
        list[3] = pageCounter1;
        list[4] = new JPanel();
        list[5] = pageCounter2;
        list[6] = new JPanel();
        list[7] = pageCounter3;

		/**forward icon*/
		final JButton forward = new JButton();
		forward.setBorderPainted(false);
		final URL fowardImage =getClass().getResource("/org/jpedal/examples/viewer/res/forward.gif");
		forward.setIcon(new ImageIcon(fowardImage));
		forward.setToolTipText("forward 1 page"); 
//		currentBar1.add(forward);
        list[8] = forward;
		forward.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(final ActionEvent e) {
			if(currentFile!=null && currentPage<pdfDecoder.getPageCount()){
				currentPage += 1;
				try {
					pdfDecoder.decodePage(currentPage);
					pdfDecoder.invalidate();
					repaint();
				} catch (final Exception e1) {
					System.err.println("forward 1 page");
					e1.printStackTrace();
				}
				
//				set page number display
				pageCounter2.setText(String.valueOf(currentPage));
			}
			}
		});
		
		/**fast forward icon*/
		final JButton fforward = new JButton();
		fforward.setBorderPainted(false);
		final URL ffowardImage =getClass().getResource("/org/jpedal/examples/viewer/res/fforward.gif");
		fforward.setIcon(new ImageIcon(ffowardImage));
		fforward.setToolTipText("Fast forward 10 pages"); 
//		currentBar1.add(fforward);
        list[9] = fforward;
		fforward.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(final ActionEvent e) {
			if(currentFile!=null && currentPage<pdfDecoder.getPageCount()-9){
				currentPage += 10;
				try {
					pdfDecoder.decodePage(currentPage);
					pdfDecoder.invalidate();
					repaint();
				} catch (final Exception e1) {
					System.err.println("forward 10 pages");
					e1.printStackTrace();
				}
				
//				set page number display
				pageCounter2.setText(String.valueOf(currentPage));
			}
			}
		});

		/**goto last page*/
		final JButton end = new JButton();
		end.setBorderPainted(false);
		final URL endImage =getClass().getResource("/org/jpedal/examples/viewer/res/end.gif");
		end.setIcon(new ImageIcon(endImage));
		end.setToolTipText("Fast forward to last page");
//		currentBar1.add(end);
        list[10] = end;
		end.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(final ActionEvent e) {
			if(currentFile!=null && currentPage<pdfDecoder.getPageCount()){
				currentPage = pdfDecoder.getPageCount();
				try {
					pdfDecoder.decodePage(currentPage);
					pdfDecoder.invalidate();
					repaint();
				} catch (final Exception e1) {
					System.err.println("forward to last page");
					e1.printStackTrace();
				}
				
//				set page number display
				pageCounter2.setText(String.valueOf(currentPage));
			}
			}
		});
		
		return list;
	}

	/**create a standalone program. User may pass in name of file as option*/
	public static void main(final String[] args) {
		
		
        /** Run the software */
        if (args.length > 0) {
            new JPanelDemo(args[0]);
        } else {
            new JPanelDemo();
        }
	}
}
