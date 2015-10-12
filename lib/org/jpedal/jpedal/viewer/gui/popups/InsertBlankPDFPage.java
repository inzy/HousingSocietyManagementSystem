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
 * InsertBlankPDFPage.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.popups;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import javax.swing.JToggleButton;

import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

public class InsertBlankPDFPage extends Save
{
    final ButtonGroup buttonGroup1 = new ButtonGroup();

	final JToggleButton jToggleButton3 = new JToggleButton();
	
	final JToggleButton jToggleButton2 = new JToggleButton();
	
	final JRadioButton addToEnd=new JRadioButton();
	final JRadioButton addBeforePage=new JRadioButton();

	public InsertBlankPDFPage( final String root_dir, final int end_page, final int currentPage )
	{
		super(root_dir, end_page, currentPage);

		try
		{
			jbInit();
		}
		catch( final Exception e )
		{
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////
	/**
	 * get root dir
	 */
    @SuppressWarnings("UnusedDeclaration")
    public final int getInsertBefore()
	{
		
		int page = -1;
		
		if(addBeforePage.isSelected()){
			try{
				page = Integer.parseInt( startPage.getText() );
			}catch( final Exception e ){
				LogWriter.writeLog(Messages.getMessage("PdfViewerError.Exception")+ ' ' + e
				+ ' ' +Messages.getMessage("PdfViewerError.ExportError"));
                if(GUI.showMessages) {
                    JOptionPane.showMessageDialog(this, Messages.getMessage("PdfViewerError.InvalidSyntax"));
                }
			}
			
			if((page < 1) && (GUI.showMessages)) {
                    JOptionPane.showMessageDialog(this, Messages.getMessage("PdfViewerError.NegativePageValue"));
                }
			if(page > end_page){
                if(GUI.showMessages) {
                    JOptionPane.showMessageDialog(this, Messages.getMessage("PdfViewerText.Page") + ' ' +
                            page + ' ' + Messages.getMessage("PdfViewerError.OutOfBounds") + ' ' +
                            Messages.getMessage("PdfViewerText.PageCount") + ' ' + end_page);
                }
				
				page = -1;
			}
		}else {
            page = -2;
        }
		
		return page;

	}
	
	private void jbInit() throws Exception
	{
		
		pageRangeLabel.setText(Messages.getMessage("PdfViewerTitle.Location"));
		pageRangeLabel.setBounds( new Rectangle( 13, 13, 199, 26 ) );
		
		addToEnd.setText(Messages.getMessage("PdfViewerTitle.AddPageToEnd"));
		addToEnd.setBounds( new Rectangle( 23, 42, 400, 22 ) );
		addToEnd.setSelected(true);
		
		addBeforePage.setText(Messages.getMessage("PdfViewerTitle.InsertBeforePage"));
		addBeforePage.setBounds( new Rectangle( 23, 70, 150, 22 ) );
		
		startPage.setBounds( new Rectangle( 175, 70, 75, 22 ) );
		startPage.setText("");
		startPage.addKeyListener(new KeyListener(){
			@Override
            public void keyPressed(final KeyEvent arg0) {}

			@Override
            public void keyReleased(final KeyEvent arg0) {
				if(startPage.getText().isEmpty()) {
                    addToEnd.setSelected(true);
                } else {
                    addBeforePage.setSelected(true);
                }
				
			}

			@Override
            public void keyTyped(final KeyEvent arg0) {}
		});
		
		this.add( changeButton, null );
		this.add( pageRangeLabel, null );
		
		this.add( addToEnd, null );
		this.add( addBeforePage, null );
		
		this.add( startPage, null );
		
		this.add( jToggleButton2, null );
		this.add( jToggleButton3, null );
		
		
		buttonGroup1.add( addToEnd );
		buttonGroup1.add( addBeforePage );

		
	}
	
	@Override
    public final Dimension getPreferredSize()
	{
		return new Dimension( 350, 180 );
	}
	
}
