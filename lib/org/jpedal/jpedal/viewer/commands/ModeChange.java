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
* ModeChange.java
* ---------------
*/

package org.jpedal.examples.viewer.commands;

import java.awt.Container;
import java.awt.Dimension;
import javafx.application.Platform;
import javafx.stage.Stage;
import javax.swing.SwingUtilities;
import org.jpedal.PdfDecoderInt;
import org.jpedal.display.Display;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.Viewer;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.examples.viewer.gui.generic.GUISearchWindow;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.gui.GUIFactory;

/**
 *
 * @author markee
 */
class ModeChange {
    
    static void changeModeInSwing(final int mode, final PdfDecoderInt decode_pdf, final GUIFactory currentGUI, final Values commonValues, final PropertiesFile properties, final GUISearchWindow searchFrame) {
                          
        if (SwingUtilities.isEventDispatchThread()) {
            
            decode_pdf.setDisplayView(mode, Display.DISPLAY_CENTERED);
            
            chooseMode(decode_pdf, commonValues, currentGUI);
            
            currentGUI.getButtons().hideRedundentNavButtons(currentGUI);
            
            if (decode_pdf.getDisplayView() == Display.PAGEFLOW) {
                selectCurrentGui(currentGUI);
            } else {
                ((GUI) currentGUI).setSelectedComboIndex(Commands.ROTATION, 0);
                ((Container) currentGUI.getFrame()).setMinimumSize(new Dimension(0, 0));
            }
            
        } else {
            currentGUI.setCommandInThread(true);
            final Runnable doPaintComponent = new Runnable() {
                
                @Override
                public void run() {
                    decode_pdf.setDisplayView(mode, Display.DISPLAY_CENTERED);
                    
                    chooseMode(decode_pdf, commonValues, currentGUI);
                    
                    currentGUI.getButtons().hideRedundentNavButtons(currentGUI);
                    
                    if(decode_pdf.getDisplayView()==Display.PAGEFLOW){
                        selectCurrentGui(currentGUI);
                    }else{                    
                    ((GUI)currentGUI).setSelectedComboIndex(Commands.ROTATION, 0);
                    ((Container)currentGUI.getFrame()).setMinimumSize(new Dimension(0, 0));                  
                    }
                    currentGUI.setExecutingCommand(false);
                }
            };
            SwingUtilities.invokeLater(doPaintComponent);
        }
    }
    
    static void changeModeInJavaFX(final int mode, final PdfDecoderInt decode_pdf, final GUIFactory currentGUI,final Values commonValues, final PropertiesFile properties, final GUISearchWindow searchFrame) {
                                    
        if (Platform.isFxApplicationThread()) {
            
            decode_pdf.setDisplayView(mode, Display.DISPLAY_CENTERED);
            
            currentGUI.getButtons().hideRedundentNavButtons(currentGUI);
            ((GUI)currentGUI).setSelectedComboIndex(Commands.ROTATION, 0);
            
            ((Stage)currentGUI.getFrame()).setMinWidth(0);
            ((Stage)currentGUI.getFrame()).setMinHeight(0);
            
        } else {
            currentGUI.setCommandInThread(true);
            final Runnable doPaintComponent = new Runnable() {
                
                @Override
                public void run() {
                    decode_pdf.setDisplayView(mode, Display.DISPLAY_CENTERED);
                    
                    currentGUI.getButtons().hideRedundentNavButtons(currentGUI);
                    
                    if(decode_pdf.getDisplayView()==Display.PAGEFLOW){
                        selectCurrentGui(currentGUI);
                    }else{ 
                    
                    ((GUI)currentGUI).setSelectedComboIndex(Commands.ROTATION, 0);
                    
                    ((Stage)currentGUI.getFrame()).setMinWidth(0);
                    ((Stage)currentGUI.getFrame()).setMinHeight(0);
                    } 
                }
            };
            Platform.runLater(doPaintComponent);
        }
    }
 
    static void chooseMode(final PdfDecoderInt decode_pdf, final Values commonValues, final GUIFactory currentGUI){
        
        if(decode_pdf.getDisplayView()==Display.FACING){
                
                int p = commonValues.getCurrentPage();
                if (decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER) && ((p & 1) == 1 && p != 1)) {
                    p--;
                } else if (!decode_pdf.getPages().getBoolean(Display.BoolValue.SEPARATE_COVER) && ((p & 1) == 0)) {
                    p--;
                }
                commonValues.setCurrentPage(p);
                currentGUI.setPage(p);
                
                currentGUI.decodePage();//ensure all pages appear
            } else if (decode_pdf.getDisplayView()==Display.CONTINUOUS_FACING){
                
                int p = commonValues.getCurrentPage();
                if ((p & 1) == 1 && p != 1) {
                    p--;
                }
                commonValues.setCurrentPage(p);
                currentGUI.setPage(p);
            }
    }
    
    static void selectCurrentGui(final GUIFactory currentGUI){
        currentGUI.decodePage();//ensure all pages appear
        if (((GUI) currentGUI).getSelectedComboIndex(Commands.SCALING) != 0) {
            ((GUI) currentGUI).setSelectedComboIndex(Commands.SCALING, 0);
            ((GUI) currentGUI).getSelectedComboItem(Commands.SCALING);
        }
        if (((GUI) currentGUI).getSelectedComboIndex(Commands.SCALING) == 0) {
            int w = currentGUI.getPdfDecoder().getPdfPageData().getCropBoxWidth(currentGUI.getValues().getCurrentPage()) / 2;
            int h = currentGUI.getPdfDecoder().getPdfPageData().getCropBoxHeight(currentGUI.getValues().getCurrentPage()) / 2;

            //Don't allow screen to set minimum to be too large
            if (w > 800) {
                w = 800;
            }
            if (h > 600) {
                h = 600;
            }

            if (Viewer.isFX()) {
                ((Stage) currentGUI.getFrame()).setMinWidth(w);
                ((Stage) currentGUI.getFrame()).setMinHeight(h);
            } else {
                ((Container) currentGUI.getFrame()).setMinimumSize(new Dimension(w, (h)));
            }
        }
    }
    
}
