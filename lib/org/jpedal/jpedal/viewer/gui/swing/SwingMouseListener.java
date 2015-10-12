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
 * SwingMouseListener.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.swing;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import org.jpedal.display.Display;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.MouseMode;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.MouseSelector;
import org.jpedal.examples.viewer.gui.MultiViewTransferHandler;
import org.jpedal.examples.viewer.gui.SingleViewTransferHandler;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.examples.viewer.gui.generic.GUIMouseHandler;
import org.jpedal.gui.GUIFactory;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.utils.LogWriter;

/**
 * Class to handle Swing Mouse Events that take place on the Pane
 * which holds the PDF content (PdfDecoder).
 */
public class SwingMouseListener extends MouseSelector implements GUIMouseHandler, MouseListener, MouseMotionListener, MouseWheelListener {

	private final PdfDecoder decode_pdf;
	private final GUIFactory currentGUI;
	private final Values commonValues;
	private final Commands currentCommands;
	
	final SwingMouseSelector selectionFunctions;
	final SwingMousePanMode panningFunctions;
	final SwingMousePageTurn pageTurnFunctions;
	
	//Custom mouse function
	private static SwingMouseFunctionality customMouseFunctions;

    private boolean scrollPageChanging;
	
	/**current cursor position*/
	private int cx,cy;
	
	/**tells user if we enter a link*/
	private static final String message="";

    /**
     * tracks mouse operation mode currently selected
     */
    private MouseMode mouseMode=new MouseMode();
	
	public SwingMouseListener(final PdfDecoder decode_pdf, final GUIFactory currentGUI,
			final Values commonValues, final Commands currentCommands) {

		this.decode_pdf=decode_pdf;
		this.currentGUI=currentGUI;
		this.commonValues=commonValues;
		this.currentCommands=currentCommands;
        this.mouseMode=currentCommands.getMouseMode();
		
		selectionFunctions = new SwingMouseSelector(decode_pdf, currentGUI, commonValues, currentCommands);
		panningFunctions = new SwingMousePanMode(decode_pdf);
		pageTurnFunctions = new SwingMousePageTurn(decode_pdf, currentGUI, commonValues, currentCommands);
		
//		decode_pdf.addExternalHandler(this, Options.SwingMouseHandler);
	}

	@Override
    public void setupMouse() {
		/**
		 * track and display screen co-ordinates and support links
		 */
		decode_pdf.addMouseMotionListener(this);
		decode_pdf.addMouseListener(this);
		decode_pdf.addMouseWheelListener(this);

		//set cursor
		decode_pdf.setDefaultCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        
        //Allow dragging and dropping to open PDF files.
        if(decode_pdf.getDisplayView() == Display.SINGLE_PAGE){
            final TransferHandler singleViewTransferHandler = new SingleViewTransferHandler(commonValues, currentGUI, currentCommands);
            decode_pdf.setTransferHandler(singleViewTransferHandler);
        } else {
            final TransferHandler multiViewTransferHandler = new MultiViewTransferHandler(commonValues, currentGUI, currentCommands);
            decode_pdf.setTransferHandler(multiViewTransferHandler);
        }

	}

	@Override
    public void mouseClicked(final MouseEvent e) {
		switch(mouseMode.getMouseMode()){

		case MouseMode.MOUSE_MODE_TEXT_SELECT :
			selectionFunctions.mouseClicked(e);
			break;

		case MouseMode.MOUSE_MODE_PANNING :
			//Does Nothing so ignore
			break;

		}
		
		if (decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON) && decode_pdf.getDisplayView()==Display.FACING){
			pageTurnFunctions.mouseClicked(e);
		}
		
		if(customMouseFunctions!=null){
			customMouseFunctions.mouseClicked(e);
		}
	}

	@Override
    public void mouseEntered(final MouseEvent e) {
		switch(mouseMode.getMouseMode()){

		case MouseMode.MOUSE_MODE_TEXT_SELECT :
			//Text selection does nothing here
			//selectionFunctions.mouseEntered(e);
			break;

		case MouseMode.MOUSE_MODE_PANNING :
			//Does Nothing so ignore
			break;

		}
		
		if (decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON) && decode_pdf.getDisplayView()==Display.FACING){
			pageTurnFunctions.mouseEntered(e);
		}
		

		if(customMouseFunctions!=null){
			customMouseFunctions.mouseEntered(e);
		}
	}

	@Override
    public void mouseExited(final MouseEvent e) {

        //Ensure mouse coords don't display when mouse not over PDF
        final int[] flag = {SwingGUI.CURSOR, 0};
        currentGUI.setMultibox(flag);

		switch(mouseMode.getMouseMode()){

		case MouseMode.MOUSE_MODE_TEXT_SELECT :
			selectionFunctions.mouseExited(e);
			break;

		case MouseMode.MOUSE_MODE_PANNING :
			//Does Nothing so ignore
			break;

		}
		
		if (decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON) && decode_pdf.getDisplayView()==Display.FACING){
			pageTurnFunctions.mouseExited(e);
		}
		

		if(customMouseFunctions!=null){
			customMouseFunctions.mouseExited(e);
		}
	}
	
	@Override
    public void mousePressed(final MouseEvent e) {
		//Start dragging
		if (SwingUtilities.isMiddleMouseButton(e)) {
			panningFunctions.mousePressed(e);
		}else{
			switch(mouseMode.getMouseMode()){

			case MouseMode.MOUSE_MODE_TEXT_SELECT :
				selectionFunctions.mousePressed(e);
				break;

			case MouseMode.MOUSE_MODE_PANNING :
				panningFunctions.mousePressed(e);
				break;

			}

			if (decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON) && decode_pdf.getDisplayView()==Display.FACING){
				pageTurnFunctions.mousePressed(e);
			}


			if(customMouseFunctions!=null){
				customMouseFunctions.mousePressed(e);
			}
		}
	}

	@Override
    public void mouseReleased(final MouseEvent e) {

		//stop middle click panning
		if (SwingUtilities.isMiddleMouseButton(e)) {
			panningFunctions.mouseReleased(e);
		}else{
			switch(mouseMode.getMouseMode()){

			case MouseMode.MOUSE_MODE_TEXT_SELECT :
				selectionFunctions.mouseReleased(e);
				break;

			case MouseMode.MOUSE_MODE_PANNING :
				panningFunctions.mouseReleased(e);
				break;

			}
			
			if (decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON) && decode_pdf.getDisplayView()==Display.FACING){
				pageTurnFunctions.mouseReleased(e);
			}

			if(customMouseFunctions!=null){
				customMouseFunctions.mouseReleased(e);
			}
		}
	}

	@Override
    public void mouseDragged(final MouseEvent e) {
		scrollAndUpdateCoords(e);
		
		if (SwingUtilities.isMiddleMouseButton(e)) {
			panningFunctions.mouseDragged(e);
		}else{
			switch(mouseMode.getMouseMode()){

			case MouseMode.MOUSE_MODE_TEXT_SELECT :
				selectionFunctions.mouseDragged(e);
				break;

			case MouseMode.MOUSE_MODE_PANNING :
				panningFunctions.mouseDragged(e);
				break;

			}
			
			if (decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON) && decode_pdf.getDisplayView()==Display.FACING){
				pageTurnFunctions.mouseDragged(e);
			}


			if(customMouseFunctions!=null){
				customMouseFunctions.mouseDragged(e);
			}
		}
	}

	public static void setCustomMouseFunctions(final SwingMouseFunctionality cmf) {
		customMouseFunctions = cmf;
	}
	
	boolean allowCoordUpdate = true;
	@Override
    public void mouseMoved(final MouseEvent e) {
		
		if(allowCoordUpdate){
			
			final int page = commonValues.getCurrentPage();

			final Point p = selectionFunctions.getCoordsOnPage(e.getX(), e.getY(), page);
			int x = (int)p.getX();
			int y = (int)p.getY();
			updateCoords(x, y);


			/*
			 * Mouse mode specific code
			 */
			switch(mouseMode.getMouseMode()){

			case MouseMode.MOUSE_MODE_TEXT_SELECT :
				final int[] values = updateXY(e.getX(), e.getY(), decode_pdf, commonValues);
				x =values[0];
				y =values[1];
				if(!currentCommands.extractingAsImage) {
                    getObjectUnderneath(x, y);
                }

				selectionFunctions.mouseMoved(e);
				break;

			case MouseMode.MOUSE_MODE_PANNING :
				//Does Nothing so ignore
				break;

			}

			if (decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON) && decode_pdf.getDisplayView()==Display.FACING){
				pageTurnFunctions.mouseMoved(e);
			}


			if(customMouseFunctions!=null){
				customMouseFunctions.mouseMoved(e);
			}
			
			allowCoordUpdate = false;
		}
	}

    private void getObjectUnderneath(final int x, final int y) {
        if(decode_pdf.getDisplayView()==Display.SINGLE_PAGE){
            final int type = decode_pdf.getDynamicRenderer().getObjectUnderneath(x, y);
            switch(type){
                case -1 :
                    decode_pdf.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    break;
                case DynamicVectorRenderer.TEXT :
                    decode_pdf.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    break;
                case DynamicVectorRenderer.IMAGE :
                    decode_pdf.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                    break;
                case DynamicVectorRenderer.TRUETYPE :
                    decode_pdf.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    break;
                case DynamicVectorRenderer.TYPE1C :
                    decode_pdf.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    break;
                case DynamicVectorRenderer.TYPE3 :
                    decode_pdf.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    break;
            }
        }
    }
	
	@Override
    public void mouseWheelMoved(final MouseWheelEvent event) {

		switch(decode_pdf.getDisplayView()){
		case Display.PAGEFLOW:
			break;

		case Display.FACING : 
			if(decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON)) {
                pageTurnFunctions.mouseWheelMoved(event);
            }
			break;
		case Display.SINGLE_PAGE : 

			if(currentGUI.getProperties().getValue("allowScrollwheelZoom").toLowerCase().equals("true") && (event.isMetaDown() || event.isControlDown())){
				//zoom
				int scaling = ((SwingGUI)currentGUI).getSelectedComboIndex(Commands.SCALING);
				if(scaling!=-1){
					scaling = (int)decode_pdf.getDPIFactory().removeScaling(decode_pdf.getScaling()*100);
				}else{
					String numberValue = (String)((SwingGUI)currentGUI).getSelectedComboItem(Commands.SCALING);
					try{
						scaling= (int)Float.parseFloat(numberValue);
					}catch(final Exception e){

						if(LogWriter.isOutput()) {
							LogWriter.writeLog("Exception in handling scaling "+e);
						}
						scaling=-1;
						//its got characters in it so get first valid number string
						final int length=numberValue.length();
						int ii=0;
						while(ii<length){
							final char c=numberValue.charAt(ii);
							if(((c>='0')&&(c<='9'))|(c=='.')) {
                                ii++;
                            } else {
                                break;
                            }
						}

						if(ii>0) {
                            numberValue = numberValue.substring(0, ii);
                        }

						//try again if we reset above
						if(scaling==-1){
							try{
								scaling = (int)Float.parseFloat(numberValue);
							}catch(final Exception e1){
								if(LogWriter.isOutput()) {
									LogWriter.writeLog("Exception in handling mouse "+e1);
								}
								scaling=-1;
							}
						}
					}
				}

				float value = event.getWheelRotation();

				if(scaling!=1 || value<0){
					if(value<0){
						value = 1.25f;
					}else{
						value = 0.8f;
					}
					if(!(scaling+value<0)){
						float currentScaling = (scaling*value);

						//kieran - is this one of yours?
						//
						if(((int)currentScaling)==(scaling)) {
                            currentScaling = scaling + 1;
                        } else {
                            currentScaling = ((int) currentScaling);
                        }

						if(currentScaling<1) {
                            currentScaling = 1;
                        }

						if(currentScaling>1000) {
                            currentScaling = 1000;
                        }

						//store mouse location
						final Rectangle r = decode_pdf.getVisibleRect();
						final double x = event.getX()/decode_pdf.getBounds().getWidth();
						final double y = event.getY()/decode_pdf.getBounds().getHeight();

						//update scaling
						currentGUI.snapScalingToDefaults(currentScaling);

						//center on mouse location
						final Thread t = new Thread() {
							@Override
                            public void run() {
								try {
									decode_pdf.scrollRectToVisible(new Rectangle(
											(int)((x*decode_pdf.getWidth())-(r.getWidth()/2)),
											(int)((y*decode_pdf.getHeight())-(r.getHeight()/2)),
											(int) decode_pdf.getVisibleRect().getWidth(),
											(int) decode_pdf.getVisibleRect().getHeight()));
									decode_pdf.repaint();
								} catch (final Exception e) {e.printStackTrace();}
							}
						};
						t.setDaemon(true);
						t.start();
						SwingUtilities.invokeLater(t);
					}
				}
			} else {

//				if(t2!=null)
//					t2.cancel();
//				
//				t2 = new java.util.Timer();
								

				final JScrollBar scroll = (JScrollBar)currentGUI.getVerticalScrollBar();
				
				// [AWI]: Get out if a vertical scrollbar is not displayed
				// (Prevents a NullPointerException from occurring later)
				if (scroll == null) {
					return;
				}
				
				//Ensure scrollToPage is set to the correct value as this can change
				//if scaling increases page size to larger than the display area
				scrollToPage = scroll.getValue();
				
				if (scroll.getValue()<=scroll.getMaximum() &&
						event.getUnitsToScroll() > 0 &&
						scrollToPage <= decode_pdf.getPageCount()) {

                    if (scrollPageChanging) {
                        return;
                    }

					scrollPageChanging = true;

                    //change page
                    //currentCommands.executeCommand(Commands.FORWARDPAGE, null);
					if(scrollToPage<decode_pdf.getPageCount()) {
                        scrollToPage++;
                    }
					
//                    TimerTask listener = new PageListener();
//                    t2.schedule(listener, 500);
                    
                    //update scrollbar so at top of page
                    if (SwingUtilities.isEventDispatchThread()) {
                        scroll.setValue(scrollToPage);
                    } else {
                        try {
                            SwingUtilities.invokeAndWait(new Runnable() {
                                @Override
                                public void run() {
                                    scroll.setValue(scrollToPage);
                                }
                            });
                        } catch(final Exception e) {

							if(LogWriter.isOutput()) {
								LogWriter.writeLog("Exception in handling mouse value "+e);
							}
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    scroll.setValue(scrollToPage);
                                }
                            });
                        }
                    }

                    scrollPageChanging = false;

				} else if (scroll.getValue()>=scroll.getMinimum() &&
						event.getUnitsToScroll() < 0 &&
						scrollToPage >= 1) {

                    if (scrollPageChanging) {
                        return;
                    }

					scrollPageChanging = true;

                    //change page
//                    currentCommands.executeCommand(Commands.BACKPAGE, null);
					if(scrollToPage>=1) {
                        scrollToPage--;
                    }
					
//					TimerTask listener = new PageListener();
//	                t2.schedule(listener, 500);
	                    
                    //update scrollbar so at bottom of page
                    if (SwingUtilities.isEventDispatchThread()) {
                        scroll.setValue(scrollToPage);
                    } else {
                        try {
                            SwingUtilities.invokeAndWait(new Runnable() {
                                @Override
                                public void run() {
                                    scroll.setValue(scrollToPage);
                                }
                            });
                        } catch(final Exception e) {
							if(LogWriter.isOutput()) {
								LogWriter.writeLog("Exception in handling mouse "+e);
							}
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    scroll.setValue(scrollToPage);
                                }
                            });
                        }
                    }

                    scrollPageChanging = false;

				}
			}
			
			//Don't break here so that continuous modes and single mode can use the following
		default :
			
			//scroll
			Area rect = new Area(decode_pdf.getVisibleRect());
			final AffineTransform transform = new AffineTransform();
			transform.translate(0, event.getUnitsToScroll() * decode_pdf.getScrollInterval());
			rect = rect.createTransformedArea(transform);
			decode_pdf.scrollRectToVisible(rect.getBounds());
			
			break;
		}
		//		//Not used in text selection or panning modes
		//		if (decode_pdf.turnoverOn && decode_pdf.getDisplayView()==Display.FACING){
		//			pageTurnFunctions.mouseWheelMoved(event);
		//		}
	}
	
//	java.util.Timer t2 = null;
	int scrollToPage = -1;
	
//    class PageListener extends TimerTask {
//
//        public void run() {
//        	if(scrollToPage!=-1){
//        		currentCommands.executeCommand(Commands.GOTO, new Object[]{(""+scrollToPage)});
//        		scrollToPage = -1;
//        	}
//        }
//    }
	
	/**
	 * scroll to visible Rectangle and update Coords box on screen
	 */
	protected void scrollAndUpdateCoords(final MouseEvent event) {
        //scroll if user hits side
        final int interval=decode_pdf.getScrollInterval();
		final Rectangle visible_test=new Rectangle(adjustForAlignment(event.getX(),decode_pdf),event.getY(),interval,interval);
		if((currentGUI.allowScrolling())&&(!decode_pdf.getVisibleRect().contains(visible_test))) {
            decode_pdf.scrollRectToVisible(visible_test);
        }

		
		final int page = commonValues.getCurrentPage();
		
		final Point p = selectionFunctions.getCoordsOnPage(event.getX(), event.getY(), page);
		final int x = (int)p.getX();
		final int y = (int)p.getY();
		updateCoords(x, y);
    }
	
	/**update current page co-ordinates on screen
	 */
	public void updateCoords(/*MouseEvent event*/final int x, final int y){

		cx=x;
		cy=y;

		if(decode_pdf.getDisplayView()!=Display.SINGLE_PAGE){
			
			if(SwingMouseSelector.activateMultipageHighlight  && (decode_pdf.getDisplayView()==Display.FACING || decode_pdf.getDisplayView()==Display.CONTINUOUS || decode_pdf.getDisplayView()==Display.CONTINUOUS_FACING)){
				
				if((decode_pdf.getDisplayView()==Display.FACING) && 
					(decode_pdf.getPageNumber()<decode_pdf.getPageCount())){
						//Width of page on left so we can show the correct coords on right page
						final int xOffset = decode_pdf.getPdfPageData().getCropBoxWidth(decode_pdf.getPageNumber());
						if(cx >= xOffset) {
                            cx -= xOffset;
                        }
//						else{
//							if(decode_pdf.getPageNumber()==1){
//								cy = 0;
//								cx = 0;
//							}
//						}
					}
			}else{
				cx=0;
				cy=0;
			}
		}


		if((Values.isProcessing())||(commonValues.getSelectedFile()==null)) {
            currentGUI.setCoordText("  X: " + " Y: " + ' ' + ' '); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        } else {
            currentGUI.setCoordText("  X: " + cx + " Y: " + cy + ' ' + ' ' + message); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }

	}
	
	 public int[] getCursorLocation() {
	        return new int[]{cx,cy};
	    }
	
	 public void checkLinks(final boolean mouseClicked, final PdfObjectReader pdfObjectReader){
		 //int[] pos = updateXY(event.getX(), event.getY());
		 pageTurnFunctions.checkLinks(mouseClicked, pdfObjectReader, cx, cy);
	 }

	 public void updateCordsFromFormComponent(final MouseEvent e) {
		 final JComponent component = (JComponent) e.getSource();

		 int x = component.getX() + e.getX();
		 int y = component.getY() + e.getY();
		 final Point p = selectionFunctions.getCoordsOnPage(x, y, commonValues.getCurrentPage());
		 x = (int)p.getX();
		 y = (int)p.getY();
			
		 updateCoords(x, y);
	 }
	 
	 /**
	  * Mouse move is called from AWTEventListener
	  * This method is called from there to only allow a single call
	  * to mouseMoved each time the mouseEvent appears
	  */
	 public void allowMouseCoordsUpdate(){
		 allowCoordUpdate = true;
     }
}
