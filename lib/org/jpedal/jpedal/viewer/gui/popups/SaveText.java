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
 * SaveText.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.popups;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import org.jpedal.examples.viewer.utils.Exporter;
import org.jpedal.utils.Messages;
public class SaveText extends Save{
	
	final JLabel outputFileTypeLabel = new JLabel();
	final JLabel outputFormat=new JLabel();
	
	final ButtonGroup buttonGroup1 = new ButtonGroup();
	final ButtonGroup buttonGroup2 = new ButtonGroup();
	
	final JToggleButton jToggleButton3 = new JToggleButton();
	
	final JToggleButton jToggleButton2 = new JToggleButton();
	
	final JRadioButton isPlainText = new JRadioButton();
	final JRadioButton isXML = new JRadioButton();
	
	final JRadioButton isWordlist = new JRadioButton();
	final JRadioButton isTable = new JRadioButton();
	final JRadioButton isRectangle = new JRadioButton();
	
	public SaveText( final String root_dir, final int end_page, final int currentPage ) {
		
		super(root_dir, end_page,currentPage);
		
		try{
			jbInit();
		}catch( final Exception e ){
			e.printStackTrace();
		}
	}
	
	public boolean isXMLExtaction() {
		return isXML.isSelected();
	}
	
	public int getTextType() {
		int prefix = Exporter.RECTANGLE;
		
		if( isWordlist.isSelected() ) {
            prefix = Exporter.WORDLIST;
        }
		if( isTable.isSelected() ) {
            prefix = Exporter.TABLE;
        }
		
		return prefix;
	}
	
	@Override
    public final Dimension getPreferredSize()
	{
		return new Dimension( 490, 280 );
	}
	
	private void jbInit() throws Exception{
		
		rootFilesLabel.setBounds( new Rectangle( 13, 13, 400, 26 ) );
		
		rootDir.setBounds( new Rectangle( 23, 40, 232, 23 ) );
		changeButton.setBounds( new Rectangle( 272, 39, 101, 23 ) );
		
		pageRangeLabel.setBounds( new Rectangle( 13, 71, 400, 26 ) );
		
		startLabel.setBounds( new Rectangle( 23, 100, 150, 22 ) );
		startPage.setBounds( new Rectangle( 150, 100, 75, 22 ) );
		
		endLabel.setBounds( new Rectangle( 260, 100, 75, 22 ) );
		endPage.setBounds( new Rectangle( 320, 100, 75, 22 ) );
		
		optionsForFilesLabel.setBounds( new Rectangle( 13, 134, 400, 26 ) );
		
		outputFileTypeLabel.setText(Messages.getMessage("PdfViewerMessage.OutputType"));
		outputFileTypeLabel.setBounds( new Rectangle( 23, 174, 164, 19 ) );
		
		isPlainText.setText(Messages.getMessage("PdfViewerOption.PlainText"));
		isPlainText.setBounds( new Rectangle( 180, 174, 100, 19 ) );
		
		isXML.setBounds( new Rectangle( 280, 174, 95, 19 ) );
		isXML.setSelected( true );
		isXML.setText(Messages.getMessage("PdfViewerOption.XML"));
		
		outputFormat.setText(Messages.getMessage("PdfViewerMessage.OutputFormat"));
		outputFormat.setBounds( new Rectangle( 23, 214, 164, 19 ) );
		
		isRectangle.setText(Messages.getMessage("PdfViewerOption.Rectangle"));
		isRectangle.setBounds( new Rectangle( 180, 214, 75, 19 ) );
		isRectangle.setSelected( true );
		isRectangle.addActionListener( new ActionListener() {
			@Override
            public void actionPerformed( final ActionEvent e ){
				isPlainText.setText(Messages.getMessage("PdfViewerOption.PlainText"));
			}
		} );
		
		isWordlist.setText(Messages.getMessage("PdfViewerOption.Wordlist"));
		isWordlist.setBounds( new Rectangle( 280, 214, 100, 19 ) );
		//isWordlist.setBounds( new Rectangle(300, 214, 95, 19  ) );
		isWordlist.addActionListener( new ActionListener() {
			@Override
            public void actionPerformed( final ActionEvent e ){
				isPlainText.setText(Messages.getMessage("PdfViewerOption.PlainText"));
			}
		} );
		
		isTable.setText(Messages.getMessage("PdfViewerOption.Table"));
		isTable.setBounds( new Rectangle( 225, 214, 75, 19 ) );
		isTable.addActionListener( new ActionListener() {
			@Override
            public void actionPerformed( final ActionEvent e ){
				isPlainText.setText(Messages.getMessage("PdfViewerOption.CSV"));
			}
		} );
		
		
		//common
		this.add( startPage, null );
		this.add( endPage, null );
		this.add( rootDir, null );
		this.add( rootFilesLabel, null );
		this.add( changeButton, null );
		this.add( endLabel, null );
		this.add( startLabel, null );
		this.add( pageRangeLabel, null );
		
		this.add( optionsForFilesLabel, null );
		this.add( outputFileTypeLabel, null );
		this.add( jToggleButton2, null );
		this.add( jToggleButton3, null );
		this.add( isPlainText, null );
		this.add( isXML, null );
		this.add( outputFormat, null );
		this.add( isRectangle, null );
		this.add( isWordlist, null );
		
		buttonGroup1.add( isXML );
		buttonGroup1.add( isPlainText );
		
		buttonGroup2.add( isRectangle );
		buttonGroup2.add( isTable );
		buttonGroup2.add( isWordlist );
		
	}
}
