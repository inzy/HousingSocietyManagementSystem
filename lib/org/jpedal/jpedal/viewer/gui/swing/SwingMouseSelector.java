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
 * SwingMouseSelector.java
 * ---------------
 */

package org.jpedal.examples.viewer.gui.swing;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Date;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.jpedal.display.Display;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.commands.generic.GUICopy;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.examples.viewer.gui.MouseSelector;
import org.jpedal.examples.viewer.gui.SwingGUI;
import org.jpedal.gui.GUIFactory;
import org.jpedal.io.Speech;
import org.jpedal.objects.PdfPageData;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.render.DynamicVectorRenderer;
import org.jpedal.text.TextLines;
import org.jpedal.utils.Messages;

public class SwingMouseSelector extends MouseSelector implements SwingMouseFunctionality{
    
    private final PdfDecoder decode_pdf;
    private final GUIFactory currentGUI;
    private final Values commonValues;
    private final Commands currentCommands;
    private PdfPageData page_data;
    
    //Experimental multi page highlight flag
    public static final boolean activateMultipageHighlight = true;
    
    //Variables to keep track of multiple clicks
    private int clickCount;
    private long lastTime = -1;
    
    //Page currently under the mouse
    private int pageMouseIsOver = -1;
    
    //Page currently being highlighted
    private int pageOfHighlight = -1;
    
    //Find current highlighted page
    private boolean startHighlighting;
    
    /*
     * ID of objects found during selection
     */
    public int id = -1;
    public int lastId =-1;
    
    //used to track changes when dragging rectangle around
    private int old_m_x2=-1,old_m_y2=-1;
    
    //Use alt to extract only within exact area
    boolean altIsDown;
    
    private final JPopupMenu rightClick = new JPopupMenu();
    private boolean menuCreated;
    
    //Right click options
    JMenuItem copy;
    //======================================
    JMenuItem selectAll, deselectall;
    //======================================
    JMenu extract;
    JMenuItem extractText, extractImage;
    ImageIcon snapshotIcon;
    JMenuItem snapShot;
    //======================================
    JMenuItem speakHighlighted;
    
    public SwingMouseSelector(final PdfDecoder decode_pdf, final GUIFactory currentGUI,
            final Values commonValues, final Commands currentCommands) {
        
        this.decode_pdf=decode_pdf;
        this.currentGUI=currentGUI;
        this.commonValues=commonValues;
        this.currentCommands=currentCommands;
        this.page_data = decode_pdf.getPdfPageData();
        
    }
    
    /**
     * Mouse Button Listener
     */
    @Override
    public void mouseClicked(final MouseEvent event) {
        
        if(decode_pdf.getDisplayView()==Display.SINGLE_PAGE || (activateMultipageHighlight && decode_pdf.getDisplayView()==Display.CONTINUOUS && decode_pdf.getDisplayView()==Display.CONTINUOUS_FACING)){
            final long currentTime = new Date().getTime();
            
            if(lastTime+500 < currentTime) {
                clickCount = 0;
            }
            
            lastTime = currentTime;
            
            if(isOtherKey(event)){
                //Single mode actions
                if(clickCount!=4) {
                    clickCount++;
                }
                
                final Point mousePoint = getCoordsOnPage(event.getX(), event.getY(), commonValues.getCurrentPage());
                final int mouseX = (int)mousePoint.getX();
                final int mouseY = (int)mousePoint.getY();
                
                if(decode_pdf.getDisplayView()==Display.SINGLE_PAGE) {
                    id = decode_pdf.getDynamicRenderer().isInsideImage(mouseX, mouseY);
                } else {
                    id = -1;
                }
                
                if(lastId!=id && id!=-1){
                    final int[] rectParams = decode_pdf.getDynamicRenderer().getAreaAsArray(id);
                                        
                    if(rectParams!=null){
                        int h= rectParams[3];
                        int w= rectParams[2];
                        
                        int x= rectParams[0];
                        int y= rectParams[1];
                        decode_pdf.getDynamicRenderer().setneedsHorizontalInvert(false);
                        decode_pdf.getDynamicRenderer().setneedsVerticalInvert(false);
                        //						Check for negative values
                        if(w<0){
                            decode_pdf.getDynamicRenderer().setneedsHorizontalInvert(true);
                            w =-w;
                            x -= w;
                        }
                        if(h<0){
                            decode_pdf.getDynamicRenderer().setneedsVerticalInvert(true);
                            h =-h;
                            y -= h;
                        }
                        
                        decode_pdf.getPages().setHighlightedImage(new int[]{x,y,w,h});
                        decode_pdf.repaint(x,y,w,h);
                    }
                    lastId = id;
                }else{
                    
                    decode_pdf.getPages().setHighlightedImage(null);
                    decode_pdf.repaint();
                    
                    lastId = -1;
                }
                
                if((id==-1) && (clickCount>1)){
                        switch(clickCount){
                            case 1 : //single click adds caret to page
                                /**
                                 * Does nothing yet. IF above prevents this case from ever happening
                                 * Add Caret code here and add shift click code for selection.
                                 * Also remember to comment out "if(clickCount>1)" from around this switch to activate
                                 */
                                break;
                            case 2 : //double click selects line
                                
                                final int[][] lineAreas = decode_pdf.getTextLines().getLineAreasAs2DArray(pageMouseIsOver);

                                if (lineAreas != null) { //Null is page has no lines
                                    final int[] point = {mouseX, mouseY, 1, 1};
                                    for (int i = 0; i != lineAreas.length; i++) {
                                        if (TextLines.intersects(point, lineAreas[i])) {
                                            decode_pdf.updateCursorBoxOnScreen(lineAreas[i], DecoderOptions.highlightColor.getRGB());
                                            decode_pdf.getTextLines().addHighlights(new int[][]{lineAreas[i]}, false, pageMouseIsOver);
                                        }
                                    }
                                }
                                break;
                            case 3 : //triple click selects paragraph
                                final int[] para = decode_pdf.getTextLines().setFoundParagraphAsArray(mouseX,mouseY, pageMouseIsOver);
                                if(para!=null){
                                    decode_pdf.updateCursorBoxOnScreen(para,DecoderOptions.highlightColor.getRGB());
                                    //decode_pdf.repaint();
                                    //decode_pdf.setMouseHighlightArea(para);
                                }
                                break;
                            case 4 : //quad click selects page
                                currentCommands.executeCommand(Commands.SELECTALL, null);
                                break;
                        }
                    }
                }
            }
        }
    
    @Override
    public void mousePressed(final MouseEvent event) {
        
        if((decode_pdf.getDisplayView()== Display.SINGLE_PAGE || (activateMultipageHighlight && decode_pdf.getDisplayView()==Display.CONTINUOUS && decode_pdf.getDisplayView()==Display.CONTINUOUS_FACING))
            && (isOtherKey(event))){
            	
            	//Get highlights here as screen display should happen is we have hightlight and removed them.
            	final int[][] rectParams = decode_pdf.getTextLines().getHighlightedAreasAs2DArray(commonValues.getCurrentPage());
            	
                /** remove any outline and reset variables used to track change */
                decode_pdf.updateCursorBoxOnScreen(null, 0); //remove box
                decode_pdf.getPages().setHighlightedImage(null);// remove image highlight
                decode_pdf.getTextLines().clearHighlights();
                
                //Remove focus from form is if anywhere on pdf panel is clicked / mouse dragged
                decode_pdf.grabFocus();
                
                final Point values = getCoordsOnPage(event.getX(), event.getY(), commonValues.getCurrentPage());
                commonValues.m_x1=(int)values.getX();
                commonValues.m_y1=(int)values.getY();

                if(rectParams!=null && rectParams.length>0){
                	decode_pdf.getPages().refreshDisplay();
                }
            }
        }

    @Override
    public void mouseReleased(final MouseEvent event) {
        if(decode_pdf.getDisplayView()==Display.SINGLE_PAGE || (activateMultipageHighlight && decode_pdf.getDisplayView()==Display.CONTINUOUS && decode_pdf.getDisplayView()==Display.CONTINUOUS_FACING)){
            if(isOtherKey(event)){
                
                //If we have been highlighting, stop now and reset all flags
                if(startHighlighting){
                    startHighlighting = false;
                    //pageOfHighlight = -1;
                }
                
                this.page_data = decode_pdf.getPdfPageData();
                final int cropX = page_data.getCropBoxX(commonValues.getCurrentPage());
                final int cropY = page_data.getCropBoxY(commonValues.getCurrentPage());
                final int mediaH = page_data.getMediaBoxHeight(commonValues.getCurrentPage());
                
                
                repaintArea(new Rectangle(commonValues.m_x1 - cropX, commonValues.m_y2 + cropY, commonValues.m_x2 - commonValues.m_x1 + cropX,
                        (commonValues.m_y1 - commonValues.m_y2) + cropY), mediaH);//redraw
                decode_pdf.repaint();
                
                if(currentCommands.extractingAsImage){
                    
                    /** remove any outline and reset variables used to track change */
                    decode_pdf.updateCursorBoxOnScreen(null, 0); //remove box
                    decode_pdf.getTextLines().clearHighlights(); //remove highlighted text
                    decode_pdf.getPages().setHighlightedImage(null);// remove image highlight
                    
                    decode_pdf.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    
                    currentCommands.executeCommand(Commands.EXTRACTASIMAGE, null);
                    currentCommands.extractingAsImage=false;
                    DecoderOptions.showMouseBox = false;
                    
                }
                
                //Ensure this is reset to -1 regardless
                pageOfHighlight = -1;
                
            } else if((event.getButton()==MouseEvent.BUTTON3) && 
                (currentGUI.getProperties().getValue("allowRightClick").toLowerCase().equals("true"))){
                    if (!menuCreated) {
                        createRightClickMenu();
                    }
                    
                    if(decode_pdf.getPages().getHighlightedImage()==null) {
                        extractImage.setEnabled(false);
                    } else {
                        extractImage.setEnabled(true);
                    }
                    
                    if(decode_pdf.getTextLines().getHighlightedAreasAs2DArray(commonValues.getCurrentPage())==null){
                        extractText.setEnabled(false);
                        speakHighlighted.setEnabled(false);
                        copy.setEnabled(false);
                    }else{
                        extractText.setEnabled(true);
                        speakHighlighted.setEnabled(true);
                        copy.setEnabled(true);
                    }
                    
                    if(decode_pdf!=null && decode_pdf.isOpen()) {
                        rightClick.show(decode_pdf, event.getX(), event.getY());
                    }
                }
            }
        }
    
    
    /**
     * Mouse Motion Listener
     */
    @Override
    public void mouseEntered(final MouseEvent arg0) {
        
    }
    
    @Override
    public void mouseExited(final MouseEvent arg0) {
        
    }
    
    @Override
    public void mouseDragged(final MouseEvent event) {
        
        if(isOtherKey(event)){
            
            altIsDown = event.isAltDown();
            if(!startHighlighting) {
                startHighlighting = true;
            }
            
            final Point values = getCoordsOnPage(event.getX(), event.getY(), commonValues.getCurrentPage());
            
            if(pageMouseIsOver==pageOfHighlight){
                commonValues.m_x2=(int)values.getX();
                commonValues.m_y2=(int)values.getY();
            }
            
            if(commonValues.isPDF()) {
                generateNewCursorBox();
            }
            
        }
        
    }
    
    @Override
    public void mouseMoved(final MouseEvent event) {
    	//Prevent double, triple... click if mouse has moved
    	clickCount=0;
    }
    
    /**
     * Create right click menu if does not exist
     */
    private void createRightClickMenu(){
        
        copy = new JMenuItem(Messages.getMessage("PdfRightClick.copy"));
        selectAll = new JMenuItem(Messages.getMessage("PdfRightClick.selectAll"));
        deselectall = new JMenuItem(Messages.getMessage("PdfRightClick.deselectAll"));
        extract = new JMenu(Messages.getMessage("PdfRightClick.extract"));
        extractText = new JMenuItem(Messages.getMessage("PdfRightClick.extractText"));
        extractImage = new JMenuItem(Messages.getMessage("PdfRightClick.extractImage"));
        snapshotIcon = new ImageIcon(getClass().getResource("/org/jpedal/examples/viewer/res/snapshot_menu.gif"));
        snapShot = new JMenuItem(Messages.getMessage("PdfRightClick.snapshot"), snapshotIcon);
        speakHighlighted = new JMenuItem("Speak Highlighted text");
        
        rightClick.add(copy);
        copy.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (decode_pdf.getDisplayView() == Display.SINGLE_PAGE) {
                    currentCommands.executeCommand(Commands.COPY, null);
                } else {
                    if (GUI.showMessages) {
                        currentGUI.showMessageDialog("Copy is only avalible in single page display mode");
                    }
                }
            }
        });
        
        rightClick.addSeparator();
        
        
        rightClick.add(selectAll);
        selectAll.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent e) {
                currentCommands.executeCommand(Commands.SELECTALL, null);
            }
        });
        
        rightClick.add(deselectall);
        deselectall.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent e) {
                currentCommands.executeCommand(Commands.DESELECTALL, null);
            }
        });
        
        rightClick.addSeparator();
        
        rightClick.add(extract);
        
        extract.add(extractText);
        extractText.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (decode_pdf.getDisplayView() == Display.SINGLE_PAGE) {
                    currentCommands.executeCommand(Commands.EXTRACTTEXT, null);
                } else {
                    if (GUI.showMessages) {
                        currentGUI.showMessageDialog("Text Extraction is only avalible in single page display mode");
                    }
                }
            }
        });
        
        extract.add(extractImage);
        extractImage.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent e) {
                if(decode_pdf.getPages().getHighlightedImage()==null){
                    if(GUI.showMessages) {
                        JOptionPane.showMessageDialog(decode_pdf, "No image has been selected for extraction.", "No image selected", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    if(decode_pdf.getDisplayView()==1){
                        final JFileChooser jf = new JFileChooser();
                        final FileFilter ff1 = new FileFilter(){
                            @Override
                            public boolean accept(final File f){
                                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".jpeg");
                            }
                            @Override
                            public String getDescription(){
                                return "JPG (*.jpg)" ;
                            }
                        };
                        final FileFilter ff2 = new FileFilter(){
                            @Override
                            public boolean accept(final File f){
                                return f.isDirectory() || f.getName().toLowerCase().endsWith(".png");
                            }
                            @Override
                            public String getDescription(){
                                return "PNG (*.png)" ;
                            }
                        };
                        final FileFilter ff3 = new FileFilter(){
                            @Override
                            public boolean accept(final File f){
                                return f.isDirectory() || f.getName().toLowerCase().endsWith(".tif") || f.getName().toLowerCase().endsWith(".tiff");
                            }
                            @Override
                            public String getDescription(){
                                return "TIF (*.tiff)" ;
                            }
                        };
                        jf.addChoosableFileFilter(ff3);
                        jf.addChoosableFileFilter(ff2);
                        jf.addChoosableFileFilter(ff1);
                        jf.showSaveDialog(null);
                        
                        final File f = jf.getSelectedFile();
                        boolean failed = false;
                        if(f!=null){
                            String filename = f.getAbsolutePath();
                            String type = jf.getFileFilter().getDescription().substring(0,3).toLowerCase();
                            
                            //Check to see if user has entered extension if so ignore filter
                            if(filename.indexOf('.')!=-1){
                                final String testExt = filename.substring(filename.indexOf('.')+1).toLowerCase();
                                if(testExt.equals("jpg") || testExt.equals("jpeg")) {
                                    type = "jpg";
                                } else
                                    if(testExt.equals("png")) {
                                        type = "png";
                                    } else //*.tiff files using JAI require *.TIFF
                                        if(testExt.equals("tif") || testExt.equals("tiff")) {
                                            type = "tiff";
                                        } else{
                                            //Unsupported file format
                                            if(GUI.showMessages) {
                                                JOptionPane.showMessageDialog(null, "Sorry, we can not currently save images to ." + testExt + " files.");
                                            }
                                            failed = true;
                                        }
                            }
                            
                            //JAI requires *.tiff instead of *.tif
                            if(type.equals("tif")) {
                                type = "tiff";
                            }
                            
                            //Image saved in All files filter, default to .png
                            if(type.equals("all")) {
                                type = "png";
                            }
                            
                            //If no extension at end of name, added one
                            if(!filename.toLowerCase().endsWith('.' +type)) {
                                filename = filename + '.' + (type);
                            }
                            
                            //If valid extension was choosen
                            if(!failed) {
                                decode_pdf.getDynamicRenderer().saveImage(id, filename, type);
                            }
                        }
                    }
                }
            }
        });
        
        extract.add(snapShot);
        snapShot.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent e) {
                currentCommands.executeCommand(Commands.SNAPSHOT, null);
            }
        });
        
        final boolean useSpeech = Speech.speechAvailible();
        if (useSpeech) {
            String option = currentGUI.getProperties().getValue("voice");
            if (option != null && option.contains("(")) {
                option = option.substring(0, option.indexOf('('));
            }
            Speech.selectedVoice = option;
            rightClick.addSeparator();
        }
        
        speakHighlighted.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(final ActionEvent arg0) {
                if(useSpeech){
                    if (decode_pdf.getDisplayView() == Display.SINGLE_PAGE) {
                        final Thread speak = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                final String text = GUICopy.copySelectedText(decode_pdf, currentGUI, commonValues);
                                Speech.speakText(text);
                            }
                        });
                        speak.setDaemon(true);
                        speak.start();
                    } else {
                        if (GUI.showMessages) {
                            currentGUI.showMessageDialog("Speak text is only avalible in single page display mode");
                        }
                    }
                }
            }
        });
        
        if(useSpeech) {
            rightClick.add(speakHighlighted);
        }
        
        menuCreated = true;
        decode_pdf.add(rightClick);
    }
    
    /**
     * generate new  cursorBox and highlight extractable text,
     * if hardware acceleration off and extraction on<br>
     * and update current cursor box displayed on screen
     */
    protected void generateNewCursorBox() {
        
        //redraw rectangle of dragged box onscreen if it has changed significantly
        if (old_m_x2!=-1 || old_m_y2!=-1 || Math.abs(commonValues.m_x2-old_m_x2)>5 || Math.abs(commonValues.m_y2-old_m_y2)>5) {
            
            //allow for user to go up
            int top_x = commonValues.m_x1;
            if (commonValues.m_x1 > commonValues.m_x2) {
                top_x = commonValues.m_x2;
            }
            int top_y = commonValues.m_y1;
            if (commonValues.m_y1 > commonValues.m_y2) {
                top_y = commonValues.m_y2;
            }
            final int w = Math.abs(commonValues.m_x2 - commonValues.m_x1);
            final int h = Math.abs(commonValues.m_y2 - commonValues.m_y1);
            
            //add an outline rectangle  to the display
            final int[] currentRectangle={top_x,top_y,w,h};
            
            //tell JPedal to highlight text in this area (you can add other areas to array)
            decode_pdf.updateCursorBoxOnScreen(currentRectangle,DecoderOptions.highlightColor.getRGB());
            if(!currentCommands.extractingAsImage){
                final int type = decode_pdf.getDynamicRenderer().getObjectUnderneath(commonValues.m_x1, commonValues.m_y1);
                
                if((altIsDown &&
                        (type!=DynamicVectorRenderer.TEXT && type!=DynamicVectorRenderer.TRUETYPE &&
                        type!=DynamicVectorRenderer.TYPE1C && type!=DynamicVectorRenderer.TYPE3))){
                    
                    //Clear current highlight and replace with rectangle for the new size
                	decode_pdf.getTextLines().clearHighlights();
                    decode_pdf.getTextLines().addHighlights(new int[][]{currentRectangle}, true, pageOfHighlight);
                }else{ //Find start and end locations and highlight all object in order in between
                    final int[] r = {commonValues.m_x1, commonValues.m_y1,commonValues.m_x2 - commonValues.m_x1, commonValues.m_y2-commonValues.m_y1};
                    
                    decode_pdf.getTextLines().addHighlights(new int[][]{r}, false, pageOfHighlight);
                    
                }
            }
            //reset tracking
            old_m_x2=commonValues.m_x2;
            old_m_y2=commonValues.m_y2;
            
        }
        
        decode_pdf.getPages().refreshDisplay();
        decode_pdf.repaint();
    }
    
    private Point getPageCoordsInSingleDisplayMode(int x, int y, final int page){
        //<start-adobe>
            final int[] flag = new int[2];
            
            flag[0] = SwingGUI.CURSOR;
            flag[1]=0;
            
            final int pageWidth;
        final int pageHeight;
        if (currentGUI.getRotation()%180==90) {
                pageWidth = decode_pdf.getPdfPageData().getScaledCropBoxHeight(page);
                pageHeight = decode_pdf.getPdfPageData().getScaledCropBoxWidth(page);
            } else {
                pageWidth = decode_pdf.getPdfPageData().getScaledCropBoxWidth(page);
                pageHeight = decode_pdf.getPdfPageData().getScaledCropBoxHeight(page);
            }
            
            final Rectangle pageArea = new Rectangle(
                    (decode_pdf.getWidth()/2) - (pageWidth/2),
                    decode_pdf.getInsetH(),
                    pageWidth,
                    pageHeight);
            
            if (pageArea.contains(x,y))
                //set displayed
            {
                flag[1] = 1;
            } else
                //set not displayed
            {
                flag[1] = 0;
            }
            
            //Set highlighting page
            if(pageOfHighlight==-1 && startHighlighting){
                pageOfHighlight = page;
            }
            
            //Keep track of page the mouse is over at all times
            pageMouseIsOver = page;
            
            currentGUI.setMultibox(flag);
        
        //<end-adobe>
        
        final float scaling=currentGUI.getScaling();
        final int inset= GUI.getPDFDisplayInset();
        final int rotation=currentGUI.getRotation();
        
        
        //Apply inset to values
        int ex=adjustForAlignment(x,decode_pdf)-inset;
        int ey=y-inset;
        
        this.page_data = decode_pdf.getPdfPageData();
        final int mediaH = page_data.getMediaBoxHeight(commonValues.getCurrentPage());
        
        //undo any viewport scaling
        if(commonValues.maxViewY!=0){ // will not be zero if viewport in play
            ex=(int)(((ex-(commonValues.dx*scaling))/commonValues.viewportScale));
            ey=(int)((mediaH-((mediaH-(ey/scaling)-commonValues.dy)/commonValues.viewportScale))*scaling);
        }
        
        //Apply page scale to value
        x=(int)((ex)/scaling);
        y=(int)((ey/scaling));
        
        final int cropX = page_data.getCropBoxX(commonValues.getCurrentPage());
        final int cropY = page_data.getCropBoxY(commonValues.getCurrentPage());
        final int cropW = page_data.getCropBoxWidth(commonValues.getCurrentPage());
        final int cropH = page_data.getCropBoxHeight(commonValues.getCurrentPage());
        
        //Apply rotation to values
        if(rotation==90){
            final int tmp=(x+cropY);
            x = (y+cropX);
            y =tmp;
        }else if((rotation==180)){
            x =(cropW+cropX)-x;
            y =(y+cropY);
        }else if((rotation==270)){
            final int tmp=(cropH+cropY)-x;
            x =(cropW+cropX)-y;
            y =tmp;
        }else{
            x = (x+cropX);
            if(decode_pdf.getDisplayView()==Display.SINGLE_PAGE) {
                y = (cropH + cropY) - y;
            } else {
                y = (cropY) + y;
            }
        }
        
        return new Point(x, y);
    }
    
    private Point getPageCoordsInContinuousDisplayMode(int x, int y, int page){
        
        final Display pages= decode_pdf.getPages();
        
        //<start-adobe
            final int[] flag = new int[2];
            
            flag[0] = SwingGUI.CURSOR;
            flag[1]=0;
            
            //In continuous pages are centred so we need make
            int xAdjustment = (decode_pdf.getWidth()/2) - (decode_pdf.getPdfPageData().getScaledCropBoxWidth(page)/2);
            if(xAdjustment<0) {
                xAdjustment = 0;
            } else{
                //This adjustment is the correct position.
                //Offset removed to that when used later we get either offset unaltered or correct position
                xAdjustment -= pages.getXCordForPage(page);
            }
            Rectangle pageArea = new Rectangle(pages.getXCordForPage(page)+xAdjustment,
                    pages.getYCordForPage(page),
                    decode_pdf.getPdfPageData().getScaledCropBoxWidth(page),
                    decode_pdf.getPdfPageData().getScaledCropBoxHeight(page));
            if(pageArea.contains(x,y)){
                //set displayed
                flag[1] = 1;
            }
            
            
            
            if(flag[1]==0){
                if(y<pageArea.y && page>1){
                    while(flag[1]==0 && page>1){
                        page--;
                        pageArea = new Rectangle(pages.getXCordForPage(page)+xAdjustment,
                                pages.getYCordForPage(page),
                                decode_pdf.getPdfPageData().getScaledCropBoxWidth(page),
                                decode_pdf.getPdfPageData().getScaledCropBoxHeight(page));
                        if(pageArea.contains(x,y)){
                            //set displayed
                            flag[1] = 1;
                        }
                    }
                }else{
                    if(y>pageArea.getMaxY() && page<commonValues.getPageCount()){
                        while(flag[1]==0 && page<commonValues.getPageCount()){
                            page++;
                            pageArea = new Rectangle(pages.getXCordForPage(page)+xAdjustment,
                                    pages.getYCordForPage(page),
                                    decode_pdf.getPdfPageData().getScaledCropBoxWidth(page),
                                    decode_pdf.getPdfPageData().getScaledCropBoxHeight(page));
                            if(pageArea.contains(x,y)){
                                //set displayed
                                flag[1] = 1;
                            }
                        }
                    }
                }
            
            
            //Set highlighting page
            if(pageOfHighlight==-1 && startHighlighting){
                pageOfHighlight = page;
            }
            
            //Keep track of page mouse is over at all times
            pageMouseIsOver = page;
            
        }
            
        //Tidy coords for multipage views
        y= ((pages.getYCordForPage(page)+decode_pdf.getPdfPageData().getScaledCropBoxHeight(page))+decode_pdf.getInsetH())-y;
            
        currentGUI.setMultibox(flag);

        //<end-adobe>
        
        final float scaling=currentGUI.getScaling();
        final int inset= GUI.getPDFDisplayInset();
        final int rotation=currentGUI.getRotation();
        
        //Apply inset to values
        int ex=adjustForAlignment(x, decode_pdf)-inset;
        int ey=y-inset;
        
        this.page_data = decode_pdf.getPdfPageData();
        final int mediaH = page_data.getMediaBoxHeight(commonValues.getCurrentPage());
        
        //undo any viewport scaling
        if(commonValues.maxViewY!=0){ // will not be zero if viewport in play
            ex=(int)(((ex-(commonValues.dx*scaling))/commonValues.viewportScale));
            ey=(int)((mediaH-((mediaH-(ey/scaling)-commonValues.dy)/commonValues.viewportScale))*scaling);
        }
        
        //Apply page scale to value
        x=(int)((ex)/scaling);
        y=(int)((ey/scaling));
        
        
        final int cropX = page_data.getCropBoxX(commonValues.getCurrentPage());
        final int cropY = page_data.getCropBoxY(commonValues.getCurrentPage());
        final int cropW = page_data.getCropBoxWidth(commonValues.getCurrentPage());
        final int cropH = page_data.getCropBoxHeight(commonValues.getCurrentPage());
        //Apply rotation to values
        if(rotation==90){
            final int tmp=(x+cropY);
            x = (y+cropX);
            y =tmp;
        }else if((rotation==180)){
            x =(cropW+cropX)-x;
            y =(y+cropY);
        }else if((rotation==270)){
            final int tmp=(cropH+cropY)-x;
            x =(cropW+cropX)-y;
            y =tmp;
        }else{
            x = (x+cropX);
            if(decode_pdf.getDisplayView()==Display.SINGLE_PAGE) {
                y = (cropH + cropY) - y;
            } else {
                y = (cropY) + y;
            }
        }
        
        return new Point(x, y);
    }
    
    private Point getPageCoordsInContinuousFacingDisplayMode(int x, int y, int page){
        //<start-adobe>
        
        final Display pages= decode_pdf.getPages();
        

            final int[] flag = new int[2];
            
            flag[0] = SwingGUI.CURSOR;
            flag[1]=0;
            
            //Check if we are in the region of the left or right pages
            if(page != 1 && x>(decode_pdf.getWidth()/2) && page<commonValues.getPageCount()){// && x>pageArea.x){
                page++;
            }
            
            //Set the adjustment for page position
            int xAdjustment = (decode_pdf.getWidth()/2) - (decode_pdf.getPdfPageData().getScaledCropBoxWidth(page))-(decode_pdf.getInsetW());
            
            //Unsure if this is needed. Still checking
            if(xAdjustment<0){
                System.err.println("x adjustment is less than 0");
                xAdjustment = 0;
            }
            
            //Check to see if pagearea contains the mouse
            Rectangle pageArea = new Rectangle(pages.getXCordForPage(page)+xAdjustment,
                    pages.getYCordForPage(page),
                    decode_pdf.getPdfPageData().getScaledCropBoxWidth(page),
                    decode_pdf.getPdfPageData().getScaledCropBoxHeight(page));
            if(pageArea.contains(x,y)){
                //set displayed
                flag[1] = 1;
            }
            
            
            //If neither of the two current pages contain the mouse start checking the other pages
            //Could be improved to minimise on the loops and calls to decode_pdf.getPageOffsets(page)
            if(flag[1]==0){
                if(y<pageArea.y && page>1){
                    while(flag[1]==0 && page>1){
                        page--;
                        xAdjustment = (decode_pdf.getWidth()/2) - (decode_pdf.getPdfPageData().getScaledCropBoxWidth(page))-(decode_pdf.getInsetW());
                        if(xAdjustment<0) {
                            xAdjustment = 0;
                        }
                        pageArea = new Rectangle(pages.getXCordForPage(page)+xAdjustment,
                                pages.getYCordForPage(page),
                                decode_pdf.getPdfPageData().getScaledCropBoxWidth(page),
                                decode_pdf.getPdfPageData().getScaledCropBoxHeight(page));
                        if(pageArea.contains(x,y)){
                            //set displayed
                            flag[1] = 1;
                        }
                        
                    }
                }else{
                    if(y>pageArea.getMaxY() && page<commonValues.getPageCount()){
                        while(flag[1]==0 && page<commonValues.getPageCount()){
                            page++;
                            xAdjustment = (decode_pdf.getWidth()/2) - (decode_pdf.getPdfPageData().getScaledCropBoxWidth(page))-(decode_pdf.getInsetW());
                            if(xAdjustment<0) {
                                xAdjustment = 0;
                            }
                            pageArea = new Rectangle(pages.getXCordForPage(page)+xAdjustment,
                                    pages.getYCordForPage(page),
                                    decode_pdf.getPdfPageData().getScaledCropBoxWidth(page),
                                    decode_pdf.getPdfPageData().getScaledCropBoxHeight(page));
                            if(pageArea.contains(x,y)){
                                //set displayed
                                flag[1] = 1;
                            }
                            
                        }
                    }
                }
            }
            
            //Set highlighting page
            if(pageOfHighlight==-1 && startHighlighting){
                pageOfHighlight = page;
            }
            
            //Keep track of page mouse is over at all times
            pageMouseIsOver = page;
            
            //Tidy coords for multipage views
            y= (((pages.getYCordForPage(page)+decode_pdf.getPdfPageData().getScaledCropBoxHeight(page))+decode_pdf.getInsetH()))-y;
            
            x -= ((pages.getXCordForPage(page))-decode_pdf.getInsetW());
            
            currentGUI.setMultibox(flag);
            
        
        //<end-adobe>
        
        
        final float scaling=currentGUI.getScaling();
        final int inset= GUI.getPDFDisplayInset();
        final int rotation=currentGUI.getRotation();
        
        
        //Apply inset to values
        int ex=adjustForAlignment(x,decode_pdf)-inset;
        int ey=y-inset;
        this.page_data = decode_pdf.getPdfPageData();
        final int cropW = page_data.getCropBoxWidth(commonValues.getCurrentPage());
        final int cropH = page_data.getCropBoxHeight(commonValues.getCurrentPage());
        
        //undo any viewport scaling
        if(commonValues.maxViewY!=0){ // will not be zero if viewport in play
            ex=(int)(((ex-(commonValues.dx*scaling))/commonValues.viewportScale));
            ey=(int)((cropH-((cropH-(ey/scaling)-commonValues.dy)/commonValues.viewportScale))*scaling);
        }
        
        //Apply page scale to value
        x=(int)((ex)/scaling);
        y=(int)((ey/scaling));
        
        final int cropX = page_data.getCropBoxX(commonValues.getCurrentPage());
        final int cropY = page_data.getCropBoxY(commonValues.getCurrentPage());
        
        //Apply rotation to values
        if(rotation==90){
            final int tmp=(x+cropY);
            x = (y+cropX);
            y =tmp;
        }else if((rotation==180)){
            x =(cropW+cropX)-x;
            y =(y+cropY);
        }else if((rotation==270)){
            final int tmp=(cropH+cropY)-x;
            x =(cropW+cropX)-y;
            y =tmp;
        }else{
            x = (x+cropX);
            if(decode_pdf.getDisplayView()==Display.SINGLE_PAGE) {
                y = (cropH + cropY) - y;
            } else {
                y = (cropY) + y;
            }
        }
        
        return new Point(x, y);
    }
    
    private Point getPageCoordsInFacingDisplayMode(int x, int y){
        //<start-adobe>

        final int[] flag = new int[2];
        flag[0] = SwingGUI.CURSOR;
        
            //get raw w and h
            final int rawW;
        final int rawH;
        if (currentGUI.getRotation()%180==90) {
                rawW = decode_pdf.getPdfPageData().getCropBoxHeight(1);
                rawH = decode_pdf.getPdfPageData().getCropBoxWidth(1);
            } else {
                rawW = decode_pdf.getPdfPageData().getCropBoxWidth(1);
                rawH = decode_pdf.getPdfPageData().getCropBoxHeight(1);
            }
            
            float scaling = decode_pdf.getScaling();
            
            final double pageHeight = scaling*rawH;
            final double pageWidth = scaling*rawW;
            final int yStart = decode_pdf.getInsetH();
            
            //move so relative to center
            double left = (decode_pdf.getWidth()/2) - (pageWidth/2);
            double right = (decode_pdf.getWidth()/2) + (pageWidth/2);
            
            if(decode_pdf.getDisplayView()==Display.FACING){
            	 left = (decode_pdf.getWidth()/2);
            	 if(decode_pdf.getPageNumber()!=1 || decode_pdf.getPageCount()==2) {
                     left -= (pageWidth);
                 }
            	 
                 right = (decode_pdf.getWidth()/2) + (pageWidth);
            }
            
            if (x >= left && x <= right &&
                    y >= yStart && y <= yStart + pageHeight)
                //set displayed
            {
                flag[1] = 1;
            } else
                //set not displayed
            {
                flag[1] = 0;
            }
        
        currentGUI.setMultibox(flag);
    
        //<end-adobe>
        
        scaling=currentGUI.getScaling();
        final int inset= GUI.getPDFDisplayInset();
        final int rotation=currentGUI.getRotation();
        
        
        //Apply inset to values
        int ex=adjustForAlignment(x,decode_pdf)-inset;
        int ey=y-inset;
        
        this.page_data = decode_pdf.getPdfPageData();
        final int mediaH = page_data.getMediaBoxHeight(commonValues.getCurrentPage());
        
        //undo any viewport scaling
        if(commonValues.maxViewY!=0){ // will not be zero if viewport in play
            ex=(int)(((ex-(commonValues.dx*scaling))/commonValues.viewportScale));
            ey=(int)((mediaH-((mediaH-(ey/scaling)-commonValues.dy)/commonValues.viewportScale))*scaling);
        }
        
        //Apply page scale to value
        x=(int)((ex)/scaling);
        y=(int)((ey/scaling));
        
        final int cropX = page_data.getCropBoxX(commonValues.getCurrentPage());
        final int cropY = page_data.getCropBoxY(commonValues.getCurrentPage());
        final int cropW = page_data.getCropBoxWidth(commonValues.getCurrentPage());
        final int cropH = page_data.getCropBoxHeight(commonValues.getCurrentPage());
        
        //Apply rotation to values
        if(rotation==90){
            final int tmp=(x+cropY);
            x = (y+cropX);
            y =tmp;
        }else if((rotation==180)){
            x =(cropW+cropX)-x;
            y =(y+cropY);
        }else if((rotation==270)){
            final int tmp=(cropH+cropY)-x;
            x =(cropW+cropX)-y;
            y =tmp;
        }else{
            x = (x+cropX);
            if(decode_pdf.getDisplayView()==Display.SINGLE_PAGE) {
                y = (cropH + cropY) - y;
            } else {
                y = (cropY) + y;
            }
        }
        
        return new Point(x, y);
    }
    
    /**
     * Find and updates coords for the current page
     * @param x :: The x coordinate of the cursors location in display area coordinates
     * @param y :: The y coordinate of the cursors location in display area coordinates
     * @param page :: The page we are currently on
     * @return Point object of the cursor location in page coordinates
     */
    public Point getCoordsOnPage(int x, int y, final int page){
        
        //Update cursor position if over page
        
        final Point pagePosition;
        switch(decode_pdf.getDisplayView()){
            case Display.SINGLE_PAGE:
                pagePosition = getPageCoordsInSingleDisplayMode(x, y, page);
                x = pagePosition.x;
                y = pagePosition.y;
                break;
            case Display.CONTINUOUS:
                pagePosition = getPageCoordsInContinuousDisplayMode(x, y, page);
                x = pagePosition.x;
                y = pagePosition.y;
                break;
                
            case Display.FACING:
                pagePosition = getPageCoordsInFacingDisplayMode(x, y);
                x = pagePosition.x;
                y = pagePosition.y;
                
                break;
                
            case Display.CONTINUOUS_FACING:
                pagePosition = getPageCoordsInContinuousFacingDisplayMode(x, y, page);
                x = pagePosition.x;
                y = pagePosition.y;
                
                break;
            default : break;
        }
        
        return new Point(x, y);
    }
    
    /**requests repaint of an area*/
    public void repaintArea(final Rectangle screenBox, final int maxY){
        
        final int strip=10;
        
        final float scaling=decode_pdf.getScaling();
        
        final int x = (int)(screenBox.x*scaling)-strip;
        final int y = (int)((maxY-screenBox.y-screenBox.height)*scaling)-strip;
        final int width = (int)((screenBox.x+screenBox.width)*scaling)+strip+strip;
        final int height = (int)((screenBox.y+screenBox.height)*scaling)+strip+strip;
        
        /**repaint manager*/
        final RepaintManager currentManager=RepaintManager.currentManager(decode_pdf);
        
        currentManager.addDirtyRegion(decode_pdf,x,y,width,height);
        
    }
    
    /**
     * Checks to see whether the primary mouse button or any other key that
     * is not the secondary mouse button or the middle mouse button is pressed,
     * if it is then return true, otherwise return false.
     * @param e
     * @return 
     */
    private static boolean isOtherKey(final MouseEvent event){

        return event.getButton() == MouseEvent.BUTTON1 || event.getButton() == MouseEvent.NOBUTTON;
    }
}
