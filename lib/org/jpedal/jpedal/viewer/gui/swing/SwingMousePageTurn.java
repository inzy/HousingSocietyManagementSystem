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
 * SwingMousePageTurn.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.swing;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.Map;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.jpedal.display.Display;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.commands.PageNavigator;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.examples.viewer.gui.MouseSelector;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.external.Options;
import org.jpedal.external.AnnotationHandler;
import org.jpedal.gui.GUIFactory;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.utils.LogWriter;

public class SwingMousePageTurn extends MouseSelector implements SwingMouseFunctionality{
    
    private final PdfDecoder decode_pdf;
    private final GUIFactory currentGUI;
    private final Values commonValues;
    private final Commands currentCommands;
    
    private long lastPress;
    
    /** allow turning page to be drawn */
    private boolean drawingTurnover;
    
    /** show turning page when hovering over corner */
    private boolean previewTurnover;

    /**middle drag panning values*/
    private double middleDragStartX,middleDragStartY,xVelocity,yVelocity;
    private Timer middleDragTimer;
    
    long timeOfLastPageChange;

    public SwingMousePageTurn(final PdfDecoder decode_pdf, final GUIFactory currentGUI,
            final Values commonValues, final Commands currentCommands) {
        
        this.decode_pdf=decode_pdf;
        this.currentGUI=currentGUI;
        this.commonValues=commonValues;
        this.currentCommands=currentCommands;
        
        //        SwingMouseSelection sms = new SwingMouseSelection(decode_pdf, commonValues, this);
        //        sms.setupMouse();
        //        decode_pdf.setMouseMode(PdfDecoder.MOUSE_MODE_TEXT_SELECT);
        //
        //		decode_pdf.addExternalHandler(this, Options.SwingMouseHandler);
        
    }

    @Override
    public void mouseClicked(final MouseEvent event) {
        
        if (decode_pdf.getDisplayView()==Display.SINGLE_PAGE &&
                event.getButton()==MouseEvent.BUTTON1 &&
                decode_pdf.getExternalHandler(Options.UniqueAnnotationHandler)!=null){
            final int[] pos = updateXY(event.getX(), event.getY(), decode_pdf, commonValues);
            checkLinks(true,decode_pdf.getIO(),pos[0], pos[1]);
        }
    }
    
    @Override
    public void mouseEntered(final MouseEvent event) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void mouseExited(final MouseEvent event) {
        //Do nothing
    }
    
    @Override
    public void mousePressed(final MouseEvent event) {
        
        //Activate turnover if pressed while preview on
        if (previewTurnover && decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON) && decode_pdf.getDisplayView()==Display.FACING &&
                event.getButton()==MouseEvent.BUTTON1) {
            drawingTurnover = true;
            //set cursor
            decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.GRABBING_CURSOR));
            lastPress = System.currentTimeMillis();
        }
        
        //Start dragging
        if (event.getButton()==MouseEvent.BUTTON2) {
            middleDragStartX = event.getX() - decode_pdf.getVisibleRect().getX();
            middleDragStartY = event.getY() - decode_pdf.getVisibleRect().getY();
            decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.PAN_CURSOR));
            
            //set up timer to refresh display
            if (middleDragTimer == null) {
                middleDragTimer = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        final Rectangle r = decode_pdf.getVisibleRect();
                        r.translate((int) xVelocity, (int) yVelocity);
                        if (xVelocity < -2) {
                            if (yVelocity < -2) {
                                decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.PAN_CURSORTL));
                            } else if (yVelocity > 2) {
                                decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.PAN_CURSORBL));
                            } else {
                                decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.PAN_CURSORL));
                            }
                        } else if (xVelocity > 2) {
                            if (yVelocity < -2) {
                                decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.PAN_CURSORTR));
                            } else if (yVelocity > 2) {
                                decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.PAN_CURSORBR));
                            } else {
                                decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.PAN_CURSORR));
                            }
                        } else {
                            if (yVelocity < -2) {
                                decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.PAN_CURSORT));
                            } else if (yVelocity > 2) {
                                decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.PAN_CURSORB));
                            } else {
                                decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.PAN_CURSOR));
                            }
                        }
                        decode_pdf.scrollRectToVisible(r);

                        //update displayed range
                        final Object display = decode_pdf.getPages();
                        if (display instanceof org.jpedal.display.swing.MultiDisplay) {
                            ((org.jpedal.display.swing.MultiDisplay) display).decodeOtherPages(currentGUI.getValues().getCurrentPage(), decode_pdf.getPageCount());
                        }

                    }
                });
            }
            middleDragTimer.start();
        }
    }
    
    @Override
    public void mouseReleased(final MouseEvent event) {
        
        //Stop drawing turnover
        if (decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON) && decode_pdf.getDisplayView()==Display.FACING) {
            drawingTurnover = false;
            
            final boolean dragLeft = currentGUI.getDragLeft();
            final boolean dragTop = currentGUI.getDragTop();
            
            if (lastPress+200 > System.currentTimeMillis()) {
                if (dragLeft) {
                    currentCommands.executeCommand(Commands.BACKPAGE, null);
                } else {
                    currentCommands.executeCommand(Commands.FORWARDPAGE, null);
                }
                previewTurnover = false;
                decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.DEFAULT_CURSOR));
            } else {
                //Trigger fall
                final Point corner = new Point();
                corner.y = decode_pdf.getInsetH();
                if (!dragTop) {
                    corner.y += ((decode_pdf.getPdfPageData().getCropBoxHeight(1) * decode_pdf.getScaling()));
                }
                
                if (dragLeft) {
                    corner.x = (int) ((decode_pdf.getVisibleRect().getWidth() / 2) - (decode_pdf.getPdfPageData().getCropBoxWidth(1) * decode_pdf.getScaling()));
                } else {
                    corner.x = (int) ((decode_pdf.getVisibleRect().getWidth() / 2) + (decode_pdf.getPdfPageData().getCropBoxWidth(1) * decode_pdf.getScaling()));
                }
                
                //				MouseMotionListener[] listeners = decode_pdf.getMouseMotionListeners();
                //				if (mover==null) {
                //					for (int i=0; i< listeners.length; i++) {
                //						if (listeners[i] instanceof mouse_mover) {
                //							mover = ((mouse_mover)listeners[i]);
                //						}
                //					}
                //				}
                //				mover.testFall(corner,event.getPoint(),dragLeft);
                
                testFall(corner,event.getPoint(),dragLeft);
            }
        }
        
        //stop middle click panning
        if (event.getButton() == MouseEvent.BUTTON2) {
            xVelocity = 0;
            yVelocity = 0;
            decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.DEFAULT_CURSOR));
            middleDragTimer.stop();
            decode_pdf.repaint();
        }
        
    }
    
    @Override
    public void mouseDragged(final MouseEvent event) {
        if(SwingUtilities.isLeftMouseButton(event)){
            //altIsDown = event.isAltDown();
            //dragged = true;
            //			int[] values = updateXY(event);
            //			commonValues.m_x2=values[0];
            //			commonValues.m_y2=values[1];
            
            //			if(commonValues.isPDF())
            //				generateNewCursorBox();
            
            if(decode_pdf.getExternalHandler(Options.UniqueAnnotationHandler)!=null){
                final int[] pos = updateXY(event.getX(), event.getY(), decode_pdf, commonValues);
                checkLinks(true,decode_pdf.getIO(),pos[0], pos[1]);
            }
            
            //update mouse coords for turnover
            if (decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON) && (drawingTurnover || previewTurnover) && decode_pdf.getDisplayView()==Display.FACING) {
                decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(SwingGUI.GRABBING_CURSOR));
                
                //update coords
                if (currentGUI.getDragLeft()) {
                    if (currentGUI.getDragTop()) {
                        decode_pdf.setUserOffsets(event.getX(), event.getY(), org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_TOP_LEFT);
                    } else {
                        decode_pdf.setUserOffsets(event.getX(), event.getY(), org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_BOTTOM_LEFT);
                    }
                } else {
                    if (currentGUI.getDragTop()) {
                        decode_pdf.setUserOffsets(event.getX(), event.getY(), org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_TOP_RIGHT);
                    } else {
                        decode_pdf.setUserOffsets(event.getX(), event.getY(), org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_BOTTOM_RIGHT);
                    }
                }
            }
            
            
            
        } else if (SwingUtilities.isMiddleMouseButton(event)) {
            //middle drag - update velocity
            xVelocity = ((event.getX() - decode_pdf.getVisibleRect().getX()) - middleDragStartX)/4;
            yVelocity = ((event.getY() - decode_pdf.getVisibleRect().getY()) - middleDragStartY)/4;
        }
        
    }
    
    @Override
    public void mouseMoved(final MouseEvent event) {
        
        if (decode_pdf.getDisplayView() == Display.FACING &&
                decode_pdf.getPages().getBoolean(Display.BoolValue.TURNOVER_ON) &&
                ((SwingGUI)decode_pdf.getExternalHandler(Options.GUIContainer)).getPageTurnScalingAppropriate() &&
                !decode_pdf.getPdfPageData().hasMultipleSizes() &&
                !PageNavigator.getPageTurnAnimating()) {
            //show preview turnover
            
            //get width and height of page
            float pageH = (decode_pdf.getPdfPageData().getCropBoxHeight(1)*decode_pdf.getScaling())-1;
            float pageW = (decode_pdf.getPdfPageData().getCropBoxWidth(1)*decode_pdf.getScaling())-1;
            
            if ((decode_pdf.getPdfPageData().getRotation(1)+currentGUI.getRotation())%180==90) {
                final float temp = pageH;
                pageH = pageW+1;
                pageW = temp;
            }
            
            final Point corner = new Point();
            
            //right turnover
            if (commonValues.getCurrentPage()+1 < commonValues.getPageCount()) {
                corner.x = (int)((decode_pdf.getVisibleRect().getWidth()/2)+pageW);
                corner.y = (int) (decode_pdf.getInsetH()+ pageH);
                
                final Point cursor = event.getPoint();
                
                if (cursor.x > corner.x-30 && cursor.x <= corner.x &&
                        ((cursor.y > corner.y-30 && cursor.y <= corner.y) ||
                        (cursor.y >= corner.y-pageH && cursor.y <corner.y-pageH+30))) {
                    //if close enough display preview turnover
                    
                    //set cursor
                    decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(GUI.GRAB_CURSOR));
                    
                    previewTurnover = true;
                    if (cursor.y >= corner.y-pageH && cursor.y <corner.y-pageH+30) {
                        corner.y = (int) (corner.y - pageH);
                        decode_pdf.setUserOffsets((int)cursor.getX(), (int)cursor.getY(), org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_TOP_RIGHT);
                    } else {
                        decode_pdf.setUserOffsets((int) cursor.getX(), (int) cursor.getY(), org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_BOTTOM_RIGHT);
                    }
                    
                } else {
                    if (currentGUI.getDragTop()) {
                        corner.y = (int) (corner.y - pageH);
                    }
                    testFall(corner, cursor, false);
                }
            }
            
            //left turnover
            if (commonValues.getCurrentPage() != 1) {
                corner.x = (int)((decode_pdf.getVisibleRect().getWidth()/2)-pageW);
                corner.y = (int) (decode_pdf.getInsetH()+pageH);
                
                final Point cursor = event.getPoint();
                
                if (cursor.x < corner.x+30 && cursor.x >= corner.x &&
                        ((cursor.y > corner.y-30 && cursor.y <= corner.y) ||
                        (cursor.y >= corner.y-pageH && cursor.y < corner.y-pageH+30))) {
                    //if close enough display preview turnover
                    //                    System.out.println("drawing left live "+decode_pdf.drawLeft);
                    //set cursor
                    decode_pdf.setCursor(currentGUI.getGUICursor().getCursor(GUI.GRAB_CURSOR));
                    
                    previewTurnover = true;
                    if (cursor.y >= corner.y-pageH && cursor.y < corner.y-pageH+30) {
                        corner.y = (int) (corner.y - pageH);
                        decode_pdf.setUserOffsets((int)cursor.getX(), (int)cursor.getY(), org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_TOP_LEFT);
                    } else {
                        decode_pdf.setUserOffsets((int) cursor.getX(), (int) cursor.getY(), org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_BOTTOM_LEFT);
                    }
                    
                } else {
                    if (currentGUI.getDragTop()) {
                        corner.y = (int) (corner.y - pageH);
                    }
                    testFall(corner,cursor, true);
                }
            }
            
        }
        
        //<start-adobe>
        //Update cursor position if over page in single mode
//
//            int[] flag = new int[2];
//            flag[0] = SwingGUI.CURSOR;
//            
//            if (decode_pdf.getDisplayView() == Display.SINGLE_PAGE || (SwingMouseSelector.activateMultipageHighlight && decode_pdf.getDisplayView()==Display.CONTINUOUS && decode_pdf.getDisplayView()==Display.CONTINUOUS_FACING)) {
//                //get raw w and h
//                int rawW,rawH;
//                if (currentGUI.getRotation()%180==90) {
//                    rawW = decode_pdf.getPdfPageData().getCropBoxHeight(1);
//                    rawH = decode_pdf.getPdfPageData().getCropBoxWidth(1);
//                } else {
//                    rawW = decode_pdf.getPdfPageData().getCropBoxWidth(1);
//                    rawH = decode_pdf.getPdfPageData().getCropBoxHeight(1);
//                }
//                
//                Point p = event.getPoint();
//                int x = (int)p.getX();
//                int y = (int)p.getY();
//                
//                float scaling = decode_pdf.getScaling();
//                
//                double pageHeight = scaling*rawH;
//                double pageWidth = scaling*rawW;
//                int yStart = decode_pdf.getInsetH();
//                
//                //move so relative to center
//                double left = (decode_pdf.getWidth()/2) - (pageWidth/2);
//                double right = (decode_pdf.getWidth()/2) + (pageWidth/2);
//                
//                if(decode_pdf.getDisplayView()==Display.FACING){
//                	 left = (decode_pdf.getWidth()/2);
//                	 if(decode_pdf.getPageNumber()!=1 || decode_pdf.getPageCount()==2)
//                		 left -= (pageWidth);
//                	 
//                     right = (decode_pdf.getWidth()/2) + (pageWidth);
//                }
//                
//                if (x >= left && x <= right &&
//                        y >= yStart && y <= yStart + pageHeight)
//                    //set displayed
//                    flag[1] = 1;
//                else
//                    //set not displayed
//                    flag[1] = 0;
//                
//                
//            } else {
//                //set not displayed
//                flag[1] = 0;
//            }
//            currentGUI.setMultibox(flag);
        
        //<end-adobe>
        
        if(decode_pdf.getExternalHandler(Options.UniqueAnnotationHandler)!=null){
            final int[] pos = updateXY(event.getX(), event.getY(), decode_pdf, commonValues);
            checkLinks(false,decode_pdf.getIO(),pos[0], pos[1]);
        }
        
        
    }
    
    public void mouseWheelMoved(final MouseWheelEvent event) {
        if(decode_pdf.getDisplayView() == Display.PAGEFLOW) {
            return;
        }
        
        if(currentGUI.getProperties().getValue("allowScrollwheelZoom").toLowerCase().equals("true") && event.isControlDown()){
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
                                LogWriter.writeLog("Exception in handling scaling "+e1);
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
            
            final JScrollBar scroll = ((JScrollPane)decode_pdf.getParent().getParent()).getVerticalScrollBar();
            if ((scroll.getValue()>=scroll.getMaximum()-scroll.getHeight() || scroll.getHeight()==0) &&
                    event.getUnitsToScroll() > 0 &&
                    timeOfLastPageChange+700 < System.currentTimeMillis() &&
                    currentGUI.getValues().getCurrentPage() < decode_pdf.getPageCount()) {
                
                //change page
                timeOfLastPageChange = System.currentTimeMillis();
                currentCommands.executeCommand(Commands.FORWARDPAGE, null);
                
                //update scrollbar so at top of page
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        scroll.setValue(scroll.getMinimum());
                    }
                });
                
            } else if (scroll.getValue()==scroll.getMinimum() &&
                    event.getUnitsToScroll() < 0 &&
                    timeOfLastPageChange+700 < System.currentTimeMillis() &&
                    currentGUI.getValues().getCurrentPage() > 1) {
                
                //change page
                timeOfLastPageChange = System.currentTimeMillis();
                currentCommands.executeCommand(Commands.BACKPAGE, null);
                
                //update scrollbar so at bottom of page
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        scroll.setValue(scroll.getMaximum());
                    }
                });
                
            } else {
                //scroll
                Area rect = new Area(decode_pdf.getVisibleRect());
                final AffineTransform transform = new AffineTransform();
                transform.translate(0, event.getUnitsToScroll() * decode_pdf.getScrollInterval());
                rect = rect.createTransformedArea(transform);
                decode_pdf.scrollRectToVisible(rect.getBounds());
            }
        }
    }
    
    /**
     * checks the link areas on the page and allow user to save file
     **/
    public void checkLinks(final boolean mouseClicked, final PdfObjectReader pdfObjectReader, final int x, final int y){
        
        
        //get 'hotspots' for the page
        final Map objs=currentGUI.getHotspots();
        
        //look for a match and call code
        if(objs!=null){
            ((AnnotationHandler)decode_pdf.getExternalHandler(Options.UniqueAnnotationHandler)).checkLinks(objs, mouseClicked, pdfObjectReader, x, y, currentGUI, commonValues);
        }
    }
    
    
    public void testFall(final Point corner, final Point cursor, final boolean testLeft) {
        if (!previewTurnover) {
            return;
        }
        
        float width = (decode_pdf.getPdfPageData().getCropBoxWidth(1)*decode_pdf.getScaling())-1;
        
        if ((decode_pdf.getPdfPageData().getRotation(1)+currentGUI.getRotation())%180==90) {
            width = decode_pdf.getPdfPageData().getCropBoxHeight(1)*decode_pdf.getScaling();
        }
        
        final float pageW = width;
        
        if (!testLeft) {
            if (!currentGUI.getDragLeft()) {
                //reset cursor
                decode_pdf.setCursor(Cursor.getDefaultCursor());
                
                //If previously displaying turnover, animate to corner
                final Thread animation = new Thread() {
                    @Override
                    public void run() {
                        
                        corner.x = (int)((decode_pdf.getVisibleRect().getWidth()/2)+pageW);
                        //work out if page change needed
                        boolean fallBack = true;
                        if (cursor.x < corner.x- pageW) {
                            corner.x = (int)(corner.x - (2* pageW));
                            fallBack = false;
                        }
                        
                        // Fall animation
                        int velocity = 1;
                        
                        //ensure cursor is not outside expected range
                        if (fallBack && cursor.x >= corner.x) {
                            cursor.x = corner.x - 1;
                        }
                        if (!fallBack && cursor.x <= corner.x) {
                            cursor.x = corner.x + 1;
                        }
                        if (!currentGUI.getDragTop() && cursor.y >= corner.y) {
                            cursor.y = corner.y - 1;
                        }
                        if (currentGUI.getDragTop() && cursor.y <= corner.y) {
                            cursor.y = corner.y + 1;
                        }
                        
                        //Calculate distance required
                        final double distX = (corner.x-cursor.x);
                        final double distY = (corner.y-cursor.y);
                        
                        //Loop through animation
                        while ((fallBack && cursor.getX() <= corner.getX()) ||
                                (!fallBack && cursor.getX() >= corner.getX()) ||
                                (!currentGUI.getDragTop() && cursor.getY() <= corner.getY()) ||
                                (currentGUI.getDragTop() && cursor.getY() >= corner.getY())) {
                            
                            //amount to move this time
                            double xMove = velocity*distX*0.002;
                            double yMove = velocity*distY*0.002;
                            
                            //make sure always moves at least 1 pixel in each direction
                            if (Math.abs(xMove) < 1) {
                                xMove /= Math.abs(xMove);
                            }
                            if (Math.abs(yMove) < 1) {
                                yMove /= Math.abs(yMove);
                            }
                            
                            cursor.setLocation(cursor.getX() + xMove, cursor.getY() + yMove);
                            if (currentGUI.getDragTop()) {
                                decode_pdf.setUserOffsets((int) cursor.getX(), (int) cursor.getY(), org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_TOP_RIGHT);
                            } else {
                                decode_pdf.setUserOffsets((int) cursor.getX(), (int) cursor.getY(), org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_BOTTOM_RIGHT);
                            }
                            
                            //Double speed til moving 32/frame
                            if (velocity < 32) {
                                velocity *= 2;
                            }
                            
                            //sleep til next frame
                            try {
                                Thread.sleep(50);
                            } catch (final Exception e) {
                                e.printStackTrace();
                            }
                            
                        }
                        
                        if (!fallBack) {
                            //calculate page to turn to
                            int forwardPage = commonValues.getCurrentPage()+1;
                            if (decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER) && forwardPage%2==1) {
                                forwardPage++;
                            } else if (!decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER) && forwardPage%2==0) {
                                forwardPage++;
                            }
                            
                            //change page
                            commonValues.setCurrentPage(forwardPage);
                            currentGUI.setPageNumber();
                            decode_pdf.setPageParameters(currentGUI.getScaling(), commonValues.getCurrentPage());
                            currentGUI.decodePage();
                        }
                        
                        //hide turnover
                        decode_pdf.setUserOffsets(0,0,org.jpedal.external.OffsetOptions.INTERNAL_DRAG_BLANK);
                        PageNavigator.setPageTurnAnimating(false, currentGUI);
                    }
                };
                animation.setDaemon(true);
                PageNavigator.setPageTurnAnimating(true, currentGUI);
                animation.start();
                previewTurnover = false;
            }
        } else {
            if (previewTurnover && currentGUI.getDragLeft()) {
                //reset cursor
                decode_pdf.setCursor(Cursor.getDefaultCursor());
                
                //If previously displaying turnover, animate to corner
                final Thread animation = new Thread() {
                    @Override
                    public void run() {
                        
                        corner.x = (int)((decode_pdf.getVisibleRect().getWidth()/2)-pageW);
                        //work out if page change needed
                        boolean fallBack = true;
                        if (cursor.x > corner.x+pageW) {
                            corner.x = (int)(corner.x + (2*pageW));
                            fallBack = false;
                        }
                        
                        // Fall animation
                        int velocity = 1;
                        
                        //ensure cursor is not outside expected range
                        if (!fallBack && cursor.x >= corner.x) {
                            cursor.x = corner.x - 1;
                        }
                        if (fallBack && cursor.x <= corner.x) {
                            cursor.x = corner.x + 1;
                        }
                        if (!currentGUI.getDragTop() && cursor.y >= corner.y) {
                            cursor.y = corner.y - 1;
                        }
                        if (currentGUI.getDragTop() && cursor.y <= corner.y) {
                            cursor.y = corner.y + 1;
                        }
                        
                        //Calculate distance required
                        final double distX = (corner.x-cursor.x);
                        final double distY = (corner.y-cursor.y);
                        
                        //Loop through animation
                        while ((!fallBack && cursor.getX() <= corner.getX()) ||
                                (fallBack && cursor.getX() >= corner.getX()) ||
                                (!currentGUI.getDragTop() && cursor.getY() <= corner.getY()) ||
                                (currentGUI.getDragTop() && cursor.getY() >= corner.getY())) {
                            
                            //amount to move this time
                            double xMove = velocity*distX*0.002;
                            double yMove = velocity*distY*0.002;
                            
                            //make sure always moves at least 1 pixel in each direction
                            if (Math.abs(xMove) < 1) {
                                xMove /= Math.abs(xMove);
                            }
                            if (Math.abs(yMove) < 1) {
                                yMove /= Math.abs(yMove);
                            }
                            
                            cursor.setLocation(cursor.getX() + xMove, cursor.getY() + yMove);
                            if (currentGUI.getDragTop()) {
                                decode_pdf.setUserOffsets((int) cursor.getX(), (int) cursor.getY(), org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_TOP_LEFT);
                            } else {
                                decode_pdf.setUserOffsets((int) cursor.getX(), (int) cursor.getY(), org.jpedal.external.OffsetOptions.INTERNAL_DRAG_CURSOR_BOTTOM_LEFT);
                            }
                            
                            //Double speed til moving 32/frame
                            if (velocity < 32) {
                                velocity *= 2;
                            }
                            
                            //sleep til next frame
                            try {
                                Thread.sleep(50);
                            } catch (final Exception e) {
                                e.printStackTrace();
                            }
                            
                        }
                        
                        if (!fallBack) {
                            //calculate page to turn to
                            int backPage = commonValues.getCurrentPage()-2;
                            if (backPage == 0) {
                                backPage = 1;
                            }
                            
                            //change page
                            commonValues.setCurrentPage(backPage);
                            currentGUI.setPageNumber();
                            decode_pdf.setPageParameters(currentGUI.getScaling(), commonValues.getCurrentPage());
                            currentGUI.decodePage();
                        }
                        
                        //hide turnover
                        decode_pdf.setUserOffsets(0, 0,org.jpedal.external.OffsetOptions.INTERNAL_DRAG_BLANK);
                        PageNavigator.setPageTurnAnimating(false, currentGUI);
                    }
                };
                animation.setDaemon(true);
                PageNavigator.setPageTurnAnimating(true, currentGUI);
                animation.start();
                previewTurnover = false;
            }
        }
    }
}


