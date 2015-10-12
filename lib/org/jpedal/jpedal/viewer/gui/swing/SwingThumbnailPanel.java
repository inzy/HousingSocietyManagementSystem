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
 * SwingThumbnailPanel.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.swing;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;

import org.jpedal.*;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.generic.GUIThumbnailPanel;

import org.jpedal.objects.PdfPageData;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.SwingWorker;

/**
 * Used in GUI example code.
 * <br>adds thumbnail capabilities to viewer,
 * <br>shows pages as thumbnails within this panel,
 * <br>So this panel can be added to the viewer
 *
 */
public class SwingThumbnailPanel extends JScrollPane implements GUIThumbnailPanel {

    static final boolean debugThumbnails=false;

    /**Swing thread to decode in background - we have one thread we use for various tasks*/
	SwingWorker worker;

    JPanel panel=new JPanel();

    /**handles drawing of thumbnails if needed*/
	private ThumbPainter painter=new ThumbPainter();
	
    /**can switch on or off thumbnails*/
	private boolean showThumbnailsdefault=true;
    
    private boolean showThumbnails=showThumbnailsdefault;

	/**flag to allow interruption in orderly manner*/
	public boolean interrupt;

	/**flag to show drawig taking place*/
	public boolean drawing,generateOtherVisibleThumbnails;

    /**custom decoder to create Thumbnails*/
    public ThumbnailDecoder thumbDecoder;

    /**
	 * thumbnails settings below
	 */
	/**buttons to display thumbnails*/
	private JButton[] pageButton;

    private BufferedImage[] images;

	private boolean[] buttonDrawn;

	private boolean[] isLandscape;

	private int[] pageHeight;

	/**weight and height for thumbnails*/
    private static final int thumbH=100,thumbW=70;

    final PdfDecoderInt decode_pdf;

    boolean isExtractor;
	private int lastPage=-1; //flag to ensure only changes result in processing


	public SwingThumbnailPanel(final PdfDecoderInt decode_pdf){

		if(debugThumbnails) {
            System.out.println("SwingThumbnailPanel");
        }

		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.decode_pdf=decode_pdf;

		thumbDecoder=new ThumbnailDecoder(decode_pdf);

		this.addComponentListener(new ComponentListener(){
			@Override
            public void componentResized(final ComponentEvent componentEvent) {

				if(!isExtractor){
					/** draw thumbnails in background, having checked not already drawing */
					if(drawing) {
                        terminateDrawing();
                    }

                    decode_pdf.waitForDecodingToFinish();

                    if(decode_pdf.isOpen()) {
                        drawThumbnails();
                    }
				}
			}

			@Override
            public void componentMoved(final ComponentEvent componentEvent) {
				//To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
            public void componentShown(final ComponentEvent componentEvent) {
				//To change body of implemented methods use File | Settings | File Templates.
			}

			@Override
            public void componentHidden(final ComponentEvent componentEvent) {
				//To change body of implemented methods use File | Settings | File Templates.
			}
		});
	}

//
	/** class to paint thumbnails */
	private class ThumbPainter extends ComponentAdapter {

        boolean requestMade;
        /** used to track user stopping movement */
        final Timer trapMultipleMoves = new Timer(250,
				new ActionListener() {

			@Override
            public void actionPerformed(final ActionEvent event) {

                if(!requestMade){

                    requestMade=true;

                    if(debugThumbnails) {
                        System.out.println("actionPerformed");
                    }

                    if(Values.isProcessing()){
                    	if(debugThumbnails) {
                            System.out.println("Still processing page");
                        }
                    }else{

                        if(debugThumbnails) {
                            System.out.println("actionPerformed2");
                        }

                        /**create any new thumbnails revaled by scroll*/
                        /** draw thumbnails in background, having checked not already drawing */
                        if(drawing) {
                            terminateDrawing();
                        }

                        if(debugThumbnails) {
                            System.out.println("actionPerformed3");
                        }

                        requestMade=false;

                        drawThumbnails();
                    }
                }
            }
		});

		/*
		 * (non-Javadoc)
		 *
		 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
		 */
		@Override
        public void componentMoved(final ComponentEvent e) {

            //allow us to disable on scroll
			if (trapMultipleMoves.isRunning()) {
                trapMultipleMoves.stop();
            }

			trapMultipleMoves.setRepeats(false);
			trapMultipleMoves.start();

		}
	}

	/**
	 * setup thumbnails if needed
	 */
	@Override
    public synchronized void generateOtherVisibleThumbnails(final int currentPage){

        try{

            //flag to show drawing which terminate can reset
            generateOtherVisibleThumbnails=true;

            //stop multiple calls
            if(currentPage==-1 || currentPage==lastPage || pageButton==null) {
                return;
            }

            lastPage=currentPage;

            if(debugThumbnails) {
                System.out.println("generateOtherVisibleThumbnails------->" + currentPage);
            }

            final int count = decode_pdf.getPageCount();

            for (int i1 = 0; i1 < count; i1++) {

                if(!generateOtherVisibleThumbnails) {
                    return;
                }

                if (i1 != currentPage - 1 && i1<pageButton.length) {
                    pageButton[i1].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                }
            }

            //Ensure that the value being used is within array length
            if(currentPage-1 < pageButton.length){
            	
            	//set button and scroll to
            	if ((count > 1) && (currentPage > 0)) {
                    pageButton[currentPage - 1].setBorder(BorderFactory.createLineBorder(Color.red));
                }

            	//update thumbnail pane if needed
            	final Rectangle rect = panel.getVisibleRect();


            	if (!rect.contains(pageButton[currentPage - 1].getLocation())) {

            		if (SwingUtilities.isEventDispatchThread()){
            			final Rectangle vis=new Rectangle(pageButton[currentPage - 1].getLocation().x,
            					pageButton[currentPage - 1].getLocation().y,
            					pageButton[currentPage-1].getBounds().width,
            					pageButton[currentPage-1].getBounds().height);
            			panel.scrollRectToVisible(vis);
            		}else{
            			SwingUtilities.invokeAndWait(new Runnable() {

            				@Override
                            public void run() {

            					if(!generateOtherVisibleThumbnails) {
                                    return;
                                }

            					final Rectangle vis=new Rectangle(pageButton[currentPage - 1].getLocation().x,
            							pageButton[currentPage - 1].getLocation().y,
            							pageButton[currentPage-1].getBounds().width,
            							pageButton[currentPage-1].getBounds().height);

            					if(!generateOtherVisibleThumbnails) {
                                    return;
                                }

            					panel.scrollRectToVisible(vis);
            				}
            			});
            		}

            	}
            }
            if(!generateOtherVisibleThumbnails) {
                return;
            }

            //commonValues.setProcessing(false);

            /** draw thumbnails in background, having checked not already drawing */
            if(drawing) {
                terminateDrawing();
            }

            if(!generateOtherVisibleThumbnails) {
                return;
            }

            /** draw thumbnails in background */
            drawThumbnails();

        } catch (final InterruptedException e) {
        } catch (final InvocationTargetException e) {
            if (LogWriter.isOutput()) {
                LogWriter.writeLog("Caught an Exception " + e);
            }
        }
    }
	
	/**
	 * redraw thumbnails if scrolled
	 */
    @Override
	public void drawThumbnails(){

        if(!isEnabled()) {
            return;
        }

        //do not generate if still loading Linearized
        if(decode_pdf.isLoadingLinearizedPDF()) {
            return;
        }

        if(debugThumbnails) {
            System.out.println("start drawThumbnails------->");
        }

        //allow for re-entry
        if(drawing) {
            this.terminateDrawing();
        }
        
        //create the thread to just do the thumbnails
		worker = new SwingWorker() {

			@Override
            public Object construct() {
				
				drawing=true;
				
				try {
					final Rectangle rect = panel.getVisibleRect();
					final int pages = decode_pdf.getPageCount();
					
					for (int i = 0; i < pages; i++) {

						//wait if still drawing
                    	decode_pdf.waitForDecodingToFinish();

                        if (interrupt) {
                            i = pages;
                        } else if ((buttonDrawn!=null)&&(pageButton!=null)&&(rect!=null)&&(!buttonDrawn[i])&& (pageButton[i] != null)
								&& (rect.intersects(pageButton[i].getBounds()))) {

                            int h = thumbH;
							if (isLandscape[i]) {
                                h = thumbW;
                            }

							final BufferedImage page = thumbDecoder.getPageAsThumbnail(i + 1, h);
                            if (!interrupt) {
                                createThumbnail(page, i + 1);
                            }

                        }
					}
					
				} catch (final Exception e) {
					//stopped thumbnails
					e.printStackTrace();
				}

				//always reset flag so we can interupt
				interrupt=false;
				
				drawing=false;

                if(debugThumbnails) {
                    System.out.println("end drawThumbnails-------<");
                }

                return null;
			}
		};
		
		worker.start();
	}
		
	/**
	 * create a blank tile with a cross to use as a thumbnail for unloaded page
	 */
	private static BufferedImage createBlankThumbnail(final int w, final int h) {
		final BufferedImage blank=new BufferedImage(w+1,h+1,BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2=(Graphics2D) blank.getGraphics();
		g2.setColor(Color.white);
		g2.fill(new Rectangle(0,0,w,h));
		g2.setColor(Color.black);
		g2.draw(new Rectangle(0,0,w,h));
		g2.drawLine(0,0,w,h);
		g2.drawLine(0,h,w,0);
		return blank;
	}

    /**
     * return BufferedImage for page
     * @param page
     * @return
     */
    @Override
    public synchronized BufferedImage getImage(int page){

        //actually stored starting 0 not 1 so adjust
        page--;

        if(images==null || images[page]==null){
            if(page>-1){
                int h = thumbH;
                if (isLandscape[page]) {
                    h = thumbW;
                }

                final BufferedImage image = thumbDecoder.getPageAsThumbnail(page + 1, h);
                images[page]=image;

                return image;
            }else {
                return null;
            }
        }else {
            return images[page];
        }
    }



    /**
	 *setup a thumbnail button in outlines
	 */
	private void createThumbnail(final BufferedImage page, int i) {
		
		i--; //convert from page to array
		
		if(page!=null){
			/**add a border*/
			final Graphics2D g2=(Graphics2D) page.getGraphics();
			g2.setColor(Color.black);
			g2.draw(new Rectangle(0,0,page.getWidth()-1,page.getHeight()-1));

            /**scale and refresh button*/
			final ImageIcon pageIcon=new ImageIcon(page);

			if (SwingUtilities.isEventDispatchThread()) {
				 //images[i]=page;
				pageButton[i].setIcon(pageIcon);
				
				buttonDrawn[i] = true;
	        } else {
	        	
	        	final int ii=i;
				final Runnable doPaintComponent = new Runnable() {
					@Override
                    public void run() {
						 //images[i]=page;
						pageButton[ii].setIcon(pageIcon);
						
						buttonDrawn[ii] = true;
					}
				};
				SwingUtilities.invokeLater(doPaintComponent);
			} 			
		}
	}

	/**
	 * setup thumbnails at start - use when adding pages
	 */
	@Override
    public void setupThumbnails(final int pages, final Font textFont, final String message, final PdfPageData pageData) {


        if(debugThumbnails) {
            System.out.println("setupThumbnails");
        }

		lastPage=-1;

		getViewport().removeAll();
        panel.removeAll();
        //create dispaly for thumbnails
		getViewport().add(panel);
		panel.setLayout(new GridLayout(pages,1,0,10));
		panel.scrollRectToVisible(new Rectangle(0,0,1,1));

		getVerticalScrollBar().setUnitIncrement(80);

		//create empty thumbnails and add to display
		
		//empty thumbnails for unloaded pages
		final BufferedImage blankPortrait = createBlankThumbnail(thumbW, thumbH);
		final BufferedImage blankLandscape = createBlankThumbnail(thumbH,thumbW);
		final ImageIcon landscape=new ImageIcon(blankLandscape.getScaledInstance(-1,
				70,BufferedImage.SCALE_SMOOTH));
		final ImageIcon portrait=new ImageIcon(blankPortrait.getScaledInstance(-1,
				100,BufferedImage.SCALE_SMOOTH));
		
		isLandscape=new boolean[pages];
		pageHeight=new int[pages];
		pageButton=new JButton[pages];
		images=new BufferedImage[pages];
		buttonDrawn=new boolean[pages];
		
		for(int i=0;i<pages;i++){
			
			final int page=i+1;
			
			//create blank image with correct orientation
			final int ph;//pw
			final int cropWidth=pageData.getCropBoxWidth(page);
			final int cropHeight=pageData.getCropBoxHeight(page);
			final int rotation=pageData.getRotation(page);
			final ImageIcon usedLandscape;
            final ImageIcon usedPortrait;

            if((rotation==0)|(rotation==180)){
				ph=(pageData.getMediaBoxHeight(page));
				//pw=(pageData.getMediaBoxWidth(page));//%%
				usedLandscape = landscape;
				usedPortrait = portrait;
			}else{
				ph=(pageData.getMediaBoxWidth(page));
				//pw=(pageData.getMediaBoxHeight(page));//%%
				usedLandscape = portrait;
				usedPortrait = landscape;
			}
			
			if(cropWidth>cropHeight){
				pageButton[i]=new JButton(message+ ' ' +page,usedLandscape); //$NON-NLS-2$
				isLandscape[i]=true;
				pageHeight[i]=ph;//w;%%
			}else{
				pageButton[i]=new JButton(message+ ' ' +page,usedPortrait); //$NON-NLS-2$
				isLandscape[i]=false;
				pageHeight[i]=ph;
			}
			
			pageButton[i].setVerticalTextPosition(AbstractButton.BOTTOM);
			pageButton[i].setHorizontalTextPosition(AbstractButton.CENTER);
			if((i==0)&&(pages>1)) {
                pageButton[0].setBorder(BorderFactory.createLineBorder(Color.red));
            } else {
                pageButton[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            }
			
			pageButton[i].setFont(textFont);
			panel.add(pageButton[i],BorderLayout.CENTER);
	
        }

    }
	
	/**
	 *return a button holding the image,so we can add listener
	 */
	@Override
    public Object[] getButtons() {
		return pageButton;
	}
	
	@Override
    public void setThumbnailsEnabled(final boolean newValue) {
		showThumbnailsdefault=newValue;
		showThumbnails=newValue;

	}
	
	@Override
    public boolean isShownOnscreen() {

		return showThumbnails;

	}
	
	@Override
    public void resetToDefault() {
		showThumbnails=showThumbnailsdefault;
		
		
	}
	
	@Override
    public void setIsDisplayedOnscreen(final boolean b) {
		showThumbnails=b;
		
	}
	

	@Override
    public void addComponentListener() {
		panel.addComponentListener(painter);
		
	}
	
	@Override
    public void removeAllListeners() {
		panel.removeComponentListener(painter);

        //remove all listeners
        final Object[] buttons=getButtons();
        if(buttons!=null){
            for (final Object button : buttons) {
                final ActionListener[] l = ((JButton) button).getActionListeners();
                for (final ActionListener aL : l) {
                    ((JButton) button).removeActionListener(aL);
                }
            }
        }
    }

    /**stop any drawing*/
	@Override
    public void terminateDrawing() {

        //disable
        generateOtherVisibleThumbnails=false;
        
        //tell our code to exit cleanly asap
		if(drawing){

            interrupt=true;
			while(drawing){
				
				try {
					Thread.sleep(20);
                } catch (final InterruptedException e) {
					// should never be called
					e.printStackTrace();
				}
                
			}
			
			interrupt=false; //ensure synched
		}

    }

	@Override
    public void dispose() {
	
		this.removeAll();
		
		worker=null;

		if(panel!=null) {
            panel.removeAll();
        }
	    panel=null;

	    painter=null;
		
	    thumbDecoder=null;

	    pageButton=null;

		buttonDrawn=null;

		isLandscape=null;

		pageHeight=null;

        images=null;
		
	}
	
}
