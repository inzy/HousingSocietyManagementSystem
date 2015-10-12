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
 * SwingButton.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.swing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.*;

import org.jpedal.examples.viewer.gui.generic.GUIButton;

/**Swing specific implementation of GUIButton interface*/
public class SwingButton extends JButton implements GUIButton{
	
	private int ID;

	public SwingButton(){
    }

    @Override
    public void init(final URL path, final int ID, final String toolTip){

        this.ID=ID;

        /**bookmarks icon*/
        setToolTipText(toolTip);


        setBorderPainted(false);

        if (path!=null)  {

                final ImageIcon icon = new ImageIcon(path);
                setIcon(icon);
                createPressedLook(this,icon);
        }

        /**
        if(path!=null ){ // actually its the text
            setText(path.getFile().split("\\.")[0]);

            setFont(new Font("Lucida",Font.ITALIC,14));

            if(path.equals("Buy"))
                setForeground(Color.BLUE);
            else
                setForeground(Color.RED);
        }   /**/
        
        //Prevent the dotted focus border
        setFocusable(false);
    }

    /**
	 * create a pressed look of the <b>icon</b> and added it to the pressed Icon of <b>button</b>
	 */
	private static void createPressedLook(final AbstractButton button, final ImageIcon icon) {
		final BufferedImage image = new BufferedImage(icon.getIconWidth()+2,icon.getIconHeight()+2,BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = (Graphics2D)image.getGraphics();
		g.drawImage(icon.getImage(), 1, 1, null);
		g.dispose();
		final ImageIcon iconPressed = new ImageIcon(image);
		button.setPressedIcon(iconPressed);
	}
	
	/**
	 * Sets the current icon for the image and creates the pressed look of the icon
	 * 
	 * @param url : ImageIcon for the image to be used for the button
	 */
    @Override
    public void setIcon(final URL url) {
        final ImageIcon icon = new ImageIcon(url);
		super.setIcon(icon);
        createPressedLook(this,icon);
    }
	
	/**command ID of button*/
	@Override
    public int getID() {
		
		return ID;
	}
	
}
