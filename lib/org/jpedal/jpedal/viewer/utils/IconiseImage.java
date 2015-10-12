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
 * IconiseImage.java
 * ---------------
 */
package org.jpedal.examples.viewer.utils;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

/**
 *Used in GUI example code.
 * Wrapper for putting image on display
 */
public class IconiseImage implements Icon{
	
	private final BufferedImage current_image;
	private final int w, h;
	
	public IconiseImage( final BufferedImage current_image ) {
		this.current_image = current_image;
		w = current_image.getWidth();
		h = current_image.getHeight();
	}
	
	@Override
    public final void paintIcon( final Component c, final Graphics g, final int x, final int y ){
		
		final Graphics2D g2 = (Graphics2D)g;
		g2.drawImage( current_image, null, 0, 0 );
	}
	
	@Override
    public final int getIconWidth(){
		return w;
	}
	
	@Override
    public final int getIconHeight(){
		return h;
	}
}

