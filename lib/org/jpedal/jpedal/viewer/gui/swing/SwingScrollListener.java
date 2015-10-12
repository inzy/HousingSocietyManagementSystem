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
 * SwingScrollListener.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.TimerTask;
import org.jpedal.display.Display;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;
import org.jpedal.utils.LogWriter;

public class SwingScrollListener extends MouseAdapter implements AdjustmentListener {

	static final boolean debugThumbnail = false;
    java.util.Timer t;
    int pNum=-1,lastPageSent=-1;
    boolean usingMouseClick;
    boolean mousePressed;
    boolean showLast;
    public BufferedImage lastImage;
    
    final SwingGUI swingGui;

    public SwingScrollListener(final SwingGUI gui) {
		swingGui = gui;
	}
    
    private void startTimer() {
        //turn if off if running
        if (t != null) {
            t.cancel();
        }

        //Removed this code as changes the functionality beyond the original.
        //Kept here as a comment incase wanted at some point in the future
        /**
        //If first thumnail to display, set no delay, this allows thumnails to appear from the very begining
        //Only used when mouse pressed and no thumbnail so far,
        //otherwise scroll wheel will decode each page from start page and scroll destination
        if(mousePressed && !showLast && lastImage==null){
        	TimerTask listener = new PageListener();
        	t = new java.util.Timer();
        	t.schedule(listener, 0);
        }else //This will delay thumbnail update so we don't spam viewer with update requests
        /**/
        
        if(((GUIThumbnailPanel)swingGui.getThumbnailPanel()).isShownOnscreen()){
        	long delay = 175;
        	
        	//Increase delay when using mouse wheel, otherwise scroll is impractical
        	if(!usingMouseClick){
        		delay = 500;
        	}
        	//restart - if its not stopped it will trigger page update
        	final TimerTask listener = new PageListener();
        	t = new java.util.Timer();
        	t.schedule(listener, delay);
        }
    }

    /**
     * get page and start timer to creatye thumbnail
     * If not moved, will draw this page
     */
    @Override
    public void adjustmentValueChanged(final AdjustmentEvent e) {

        pNum=e.getAdjustable().getValue()+1;

        
        //If mouse has not been pressed, do not create preview thumbnail
        //This prevents thumbnails on scroll which causes some issue.
        if(!mousePressed){
        	showLast = false;
        	lastImage = null;
        }
        
        //Show loading image
        if (showLast) {
            //Use stored image
        	swingGui.getPdfDecoder().setPreviewThumbnail(lastImage, "Page "+pNum+" of "+swingGui.getPdfDecoder().getPageCount());
        	swingGui.getPdfDecoder().repaint();
        } else if (lastImage != null) {
            //Create loading image
            final BufferedImage img = new BufferedImage(lastImage.getWidth(), lastImage.getHeight(), lastImage.getType());
            final Graphics2D g2 = (Graphics2D)img.getGraphics();

            //Draw last image
            g2.drawImage(lastImage,0,0,null);

            //Gray out
            g2.setPaint(new Color(0,0,0,130));
            g2.fillRect(0,0,lastImage.getWidth(),lastImage.getHeight());

            //Draw loading string
            final String l = "Loading...";
            final int textW = g2.getFontMetrics().stringWidth(l);
            final int textH = g2.getFontMetrics().getHeight();
            g2.setPaint(Color.WHITE);
            g2.drawString(l,(lastImage.getWidth()/2)-(textW/2),(lastImage.getHeight()/2)+(textH/2));

            //Store and set to reuse
            lastImage = img;
            showLast = true;

            //Update
            swingGui.getPdfDecoder().setPreviewThumbnail(img,"Page "+pNum+" of "+swingGui.getPdfDecoder().getPageCount());
            swingGui.getPdfDecoder().repaint();
        }

        startTimer();
        
    }

    public synchronized void setThumbnail(){

        //if(mousePressed){

            if(lastPageSent!=pNum){

                lastPageSent=pNum;

                try{
                	
                	//decode_pdf.waitForDecodingToFinish();
                	
                    final BufferedImage image=((GUIThumbnailPanel)swingGui.getThumbnailPanel()).getImage(pNum);

                    if(debugThumbnail) {
                        System.out.println(pNum + " " + image);
                    }

                    //Store and turn off using stored image
                    lastImage = image;
                    showLast = false;
                    swingGui.getPdfDecoder().setPreviewThumbnail(image,"Page "+pNum+" of "+swingGui.getPdfDecoder().getPageCount());

                    swingGui.getPdfDecoder().repaint();

                }catch(final Exception ee){
                    if (LogWriter.isOutput()) {
                        LogWriter.writeLog("Caught an Exception " + ee);
                    }
                }
            //}
        }
    }

    public void releaseAndUpdate() {

    	if(usingMouseClick) {
            usingMouseClick = false;
        }
    	
    	//turn if off if running
        if (t != null) {
            t.cancel();
        }

        //System.out.println("releaseAndUpdate");


        if (swingGui.getPdfDecoder().getDisplayView() != Display.PAGEFLOW) {
            swingGui.getPdfDecoder().setPreviewThumbnail(null, "Page " + pNum + " of " + swingGui.getPdfDecoder().getPageCount());
        }

        //if(pNum>0 && lastPage!=pNum){
        swingGui.getCommand().executeCommand(Commands.GOTO, new Object[]{Integer.toString(pNum)});
        //}else
          //  decode_pdf.clearScreen();

        //Update page number for new position
        swingGui.setPageNumber();
            
        swingGui.getPdfDecoder().repaint();
    }

    @Override
    public void mousePressed(final MouseEvent e) {

        if(debugThumbnail) {
            System.out.println("pressed");
        }

        mousePressed=true;

        usingMouseClick = true;
        //if(mousePressed)
        	startTimer();

    }

    @Override
    public void mouseReleased(final MouseEvent e) {

        if(debugThumbnail) {
            System.out.println("release");
        }

        if(mousePressed) {
            releaseAndUpdate();
        }

        mousePressed=false;


    }
    
    /**
     * used to update preview thumbnail to next page
     */
    class PageListener extends TimerTask {
    	@Override
        public void run() {
        	
        	if(mousePressed) {
                setThumbnail();
            } else{
        		usingMouseClick=false;
        		releaseAndUpdate();
        	}
        	
        	if (t != null) {
                t.cancel();
            }
        }
    }

}
