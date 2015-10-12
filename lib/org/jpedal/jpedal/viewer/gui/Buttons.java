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
 * Buttons.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui;

import org.jpedal.display.GUIDisplay;
import org.jpedal.examples.viewer.gui.generic.GUIMenuItems;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import org.jpedal.display.Display;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.generic.GUIButton;
import org.jpedal.examples.viewer.gui.generic.GUIButtons;
import org.jpedal.examples.viewer.gui.swing.SwingButton;
import org.jpedal.gui.GUIFactory;
import static org.jpedal.gui.GUIFactory.BUTTONBAR;
import static org.jpedal.gui.GUIFactory.NAVBAR;
import static org.jpedal.gui.GUIFactory.PAGES;

/**
 * This class controls everything todo with GUIButtons, 
 * it holds the objects and their corresponding methods.
 * 
 * To initialise the object/class call init()
 */
public class Buttons implements GUIButtons{

    /**holds OPEN, INFO,etc*/
    private JToolBar topButtons = new JToolBar();
    
    ButtonGroup layoutGroup = new ButtonGroup();

    private GUIButton mouseMode;

    //Optional Buttons for menu Search
    private GUIButton nextSearch, previousSearch;
    private GUIButton searchButton;

    //Buttons on the function bar
    private GUIButton openButton;
    private GUIButton printButton;

    private GUIButton docPropButton;
    private GUIButton infoButton;
    private GUIButton snapshotButton; //allows user to toggle on/off text/image snapshot

    //Buttons to control the view mode
    private GUIButton singleButton, continuousButton, continuousFacingButton, facingButton, pageFlowButton;

    //Buttons to navigate pages in the document
    private GUIButton first, fback, back, forward, fforward, last;


    private boolean isSingle;

    /**
     * Initialises the buttons
     *
     * @param isSingle is of type boolean
     */
    public void init(final boolean isSingle) {

        this.isSingle = isSingle;

        previousSearch = new SwingButton();
        nextSearch = new SwingButton();
        searchButton = new SwingButton();

        first = new SwingButton();
        fback = new SwingButton();
        back = new SwingButton();
        forward = new SwingButton();
        fforward = new SwingButton();
        last = new SwingButton();

        snapshotButton = new SwingButton();


        singleButton = new SwingButton();
        continuousButton = new SwingButton();
        continuousFacingButton = new SwingButton();
        facingButton = new SwingButton();

        pageFlowButton = new SwingButton();

        openButton = new SwingButton();
        printButton = new SwingButton();
        docPropButton = new SwingButton();
        infoButton = new SwingButton();
        mouseMode = new SwingButton();
        
        setupButtonStyle();

    }

    /**
     * Returns the button associated with the ID.
     *
     * @param ID is of type Int
     * @return GUIButton object
     */
    @Override
    public GUIButton getButton(final int ID) {

        switch (ID) {
            case Commands.SNAPSHOT:
                return snapshotButton;
            case Commands.ABOUT:
                return infoButton;
            case Commands.DOCINFO:
                return docPropButton;
            case Commands.PRINT:
                return printButton;
            case Commands.OPENFILE:
                return openButton;
            case Commands.CONTINUOUS_FACING:
                return continuousFacingButton;
            case Commands.CONTINUOUS:
                return continuousButton;
            case Commands.PAGEFLOW:
                return pageFlowButton;
            case Commands.FACING:
                return facingButton;
            case Commands.SINGLE:
                return singleButton;
            case Commands.MOUSEMODE:
                return mouseMode;
            case Commands.BACKPAGE:
                return back;
            case Commands.FIRSTPAGE:
                return first;
            case Commands.FBACKPAGE:
                return fback;
            case Commands.FORWARDPAGE:
                return forward;
            case Commands.FFORWARDPAGE:
                return fforward;
            case Commands.LASTPAGE:
                return last;
            case Commands.FIND:
                return searchButton;
            case Commands.PREVIOUSRESULT:
                return previousSearch;
            case Commands.NEXTRESULT:
                return nextSearch;
        }
        return null;
    }

    /**
     * Prepares all objects for the trash collector
     */
    public void dispose() {
        searchButton = null;
        nextSearch = null;
        previousSearch = null;
        first = null;
        fback = null;
        back = null;
        forward = null;
        fforward = null;
        last = null;
        singleButton = null;
        continuousButton = null;
        continuousFacingButton = null;
        facingButton = null;
        pageFlowButton = null;
        snapshotButton = null;

        layoutGroup = null;

        if(topButtons!=null) {
            topButtons.removeAll();
        }
        topButtons =null;
    }

    /**
     * Enables all the back navigation buttons.
     *
     * @param flag is of type boolean
     */
    @Override
    public void setBackNavigationButtonsEnabled(final boolean flag) {

        back.setEnabled(flag);
        first.setEnabled(flag);
        fback.setEnabled(flag);

    }

    /**
     * Enables all the forward navigation buttons.
     *
     * @param flag is of type boolean
     */
    @Override
    public void setForwardNavigationButtonsEnabled(final boolean flag) {

        forward.setEnabled(flag);
        last.setEnabled(flag);
        fforward.setEnabled(flag);

    }

    /**
     * Enables all the page layout buttons.
     *
     * @param flag is type boolean
     */
    @Override
    public void setPageLayoutButtonsEnabled(final boolean flag) {

        if (!isSingle) {
            return;
        }

        continuousButton.setEnabled(flag);
        continuousFacingButton.setEnabled(flag);
        facingButton.setEnabled(flag);

        pageFlowButton.setEnabled(flag);

        final Enumeration menuOptions = layoutGroup.getElements();

		if (menuOptions.hasMoreElements()) {

            //first one is always ON
            ((JMenuItem) menuOptions.nextElement()).setEnabled(true);

            //set other menu items
            while (menuOptions.hasMoreElements()) {
                ((JMenuItem) menuOptions.nextElement()).setEnabled(flag);
            }
        }

    }

    /**
     * Aligns the layout menu option to the current view mode
     * @param mode is of type int
     */
    @Override
    public void alignLayoutMenuOption(final int mode) {

        int i = 1;

        final Enumeration menuOptions = layoutGroup.getElements();

        //cycle to correct value
        while (menuOptions.hasMoreElements() && i != mode) {
            menuOptions.nextElement();
            i++;
        }

        //choose item
        ((JMenuItem) menuOptions.nextElement()).setSelected(true);
    }

    /**
     * Getter for layoutGroup.
     *
     * @return ButtonGroup object layoutGroup
     */
    public ButtonGroup getLayoutGroup() {
        return layoutGroup;
    }
    
    @Override
    public void checkButtonSeparators() {
        /**
         * Ensure the buttonBar doesn't start or end with a separator
         */
        boolean before=false, after=false;
        JSeparator currentSep=null;
        for(int k=0; k!=topButtons.getComponentCount(); k++) {
            if (topButtons.getComponent(k) instanceof JSeparator){
                if (currentSep == null) {
                    currentSep = (JSeparator) topButtons.getComponent(k);
                } else {
                    if (!before || !after) {
                        currentSep.setVisible(false);
                    } else {
                        currentSep.setVisible(true);
                    }
                    before = before || after;
                    after = false;
                    currentSep = (JSeparator)topButtons.getComponent(k);
                }
            } else {
                if (topButtons.getComponent(k).isVisible()) {
                    if (currentSep == null) {
                        before = true;
                    } else {
                        after = true;
                    }
                }
            }
        }
        if (currentSep != null) {
            if (!before || !after) {
                currentSep.setVisible(false);
            } else {
                currentSep.setVisible(true);
            }
        }
    }
    
    public JToolBar getTopButtons(){
        return topButtons;
    }
    
    /* (non-Javadoc)
     * @see org.jpedal.examples.viewer.gui.swing.GUIFactory#addButton(int, java.lang.String, java.lang.String, int)
     */
    public void addButton(final int line, final String toolTip, final String path, final int ID,
                            final GUIMenuItems swMenuItems, final GUIFactory currentGUI,
                            final CommandListener currentCommandListener, final JToolBar pagesToolBar, final JToolBar navToolBar) {

        GUIButton newButton = new SwingButton();

        /**
         * specific buttons
         */
        switch (ID) {




            case Commands.FIRSTPAGE:
                newButton = getButton(Commands.FIRSTPAGE);
                break;
            case Commands.FBACKPAGE:
                newButton = getButton(Commands.FBACKPAGE);
                break;
            case Commands.BACKPAGE:
                newButton = getButton(Commands.BACKPAGE);
                break;
            case Commands.FORWARDPAGE:
                newButton = getButton(Commands.FORWARDPAGE);
                break;
            case Commands.FFORWARDPAGE:
                newButton = getButton(Commands.FFORWARDPAGE);
                break;
            case Commands.LASTPAGE:
                newButton = getButton(Commands.LASTPAGE);
                break;
            case Commands.SNAPSHOT:
                newButton = getButton(Commands.SNAPSHOT);
                break;
            case Commands.SINGLE:
                newButton = getButton(Commands.SINGLE);
                newButton.setName("SINGLE");
                ((SwingButton) newButton).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        swMenuItems.setMenusForDisplayMode(Commands.SINGLE, -1);
                    }
                });
                break;
            case Commands.CONTINUOUS:
                newButton = getButton(Commands.CONTINUOUS);
                newButton.setName("CONTINUOUS");
                ((SwingButton) newButton).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        swMenuItems.setMenusForDisplayMode(Commands.CONTINUOUS, -1);
                    }
                });
                break;
            case Commands.CONTINUOUS_FACING:
                newButton = getButton(Commands.CONTINUOUS_FACING);
                newButton.setName("CONTINUOUS_FACING");
                ((SwingButton) newButton).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        swMenuItems.setMenusForDisplayMode(Commands.CONTINUOUS_FACING, -1);
                    }
                });
                break;
            case Commands.FACING:
                newButton = getButton(Commands.FACING);
                newButton.setName("FACING");
                ((SwingButton) newButton).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        swMenuItems.setMenusForDisplayMode(Commands.FACING, -1);
                    }
                });
                break;
            case Commands.PAGEFLOW:
                newButton = getButton(Commands.PAGEFLOW);
                newButton.setName("PAGEFLOW");
                ((SwingButton) newButton).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        swMenuItems.setMenusForDisplayMode(Commands.PAGEFLOW, -1);
                    }
                });
                break;
            case Commands.PREVIOUSRESULT:
                newButton = getButton(Commands.PREVIOUSRESULT);
                newButton.setEnabled(false);
                newButton.setName("PREVIOUSRESULT");
                break;
            case Commands.NEXTRESULT:
                newButton = getButton(Commands.NEXTRESULT);
                newButton.setEnabled(false);
                newButton.setName("NEXTRESULT");
                break;
            case Commands.OPENFILE:
                newButton = getButton(Commands.OPENFILE);
                newButton.setName("open");
                break;
            case Commands.PRINT:
                newButton = getButton(Commands.PRINT);
                newButton.setName("print");
                break;
            case Commands.FIND:
                newButton = getButton(Commands.FIND);
                newButton.setName("search");
                break;
            case Commands.DOCINFO:
                newButton = getButton(Commands.DOCINFO);
                break;
            case Commands.ABOUT:
                newButton = getButton(Commands.ABOUT);
                break;
            case Commands.MOUSEMODE:
                newButton = getButton(Commands.MOUSEMODE);
                ((SwingButton) newButton).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        if (currentGUI.getPdfDecoder().getDisplayView() == Display.SINGLE_PAGE) {
                            swMenuItems.setMenusForDisplayMode(Commands.MOUSEMODE, currentGUI.getCommand().getMouseMode().getMouseMode());
                        }
                    }
                });
                newButton.setName("mousemode");
                break;
        }

        ((SwingButton) newButton).addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {
            }

            @Override
            public void mousePressed(final MouseEvent e) {
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                if (GUIDisplay.allowChangeCursor) {
                    ((SwingButton) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                if (GUIDisplay.allowChangeCursor) {
                    ((SwingButton) e.getSource()).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        newButton.init(currentGUI.getGUICursor().getURLForImage(path), ID, toolTip);
        

        //add listener
        ((AbstractButton) newButton).addActionListener(currentCommandListener.getSwingCommandListener());

        final int mode = currentGUI.getValues().getModeOfOperation();

        //remove background for the applet as default L&F has a shaded toolbar
        if (mode == Values.RUNNING_APPLET) {
            ((AbstractButton) newButton).setContentAreaFilled(false);
        }

        //add to toolbar
        if (line == BUTTONBAR || mode == Values.RUNNING_PLUGIN) {
            topButtons.add((AbstractButton) newButton);

            //add spaces for plugin
            if (mode == Values.RUNNING_PLUGIN && (mode == Commands.LASTPAGE || mode == Commands.PAGEFLOW)) {
                topButtons.add(Box.createHorizontalGlue());
            }

        } else if (line == NAVBAR) {
            navToolBar.add((AbstractButton) newButton);
        } else if (line == PAGES) {
            pagesToolBar.add((AbstractButton) newButton, BorderLayout.CENTER);
        }
    }
    
	//When page changes make sure only relevant navigation buttons are displayed
	@Override
    public void hideRedundentNavButtons(final GUIFactory currentGUI){

		int maxPages = currentGUI.getPdfDecoder().getPageCount();
		if(currentGUI.getValues().isMultiTiff()){
			maxPages = currentGUI.getValues().getPageCount();
		}

        if (((currentGUI.getPdfDecoder().getDisplayView() == Display.FACING && currentGUI.getPdfDecoder().getPages().getBoolean(Display.BoolValue.SEPARATE_COVER)) ||
                currentGUI.getPdfDecoder().getDisplayView() == Display.CONTINUOUS_FACING) 
                && (maxPages & 1)==1) {
            maxPages--;
        }
        
		if(currentGUI.getValues().getCurrentPage()==1){
			setBackNavigationButtonsEnabled(false);
			currentGUI.getMenuItems().setBackNavigationItemsEnabled(false);
		}else{
			setBackNavigationButtonsEnabled(true);
			currentGUI.getMenuItems().setBackNavigationItemsEnabled(true);
		}
		
		if(currentGUI.getValues().getCurrentPage()==maxPages){
			setForwardNavigationButtonsEnabled(false);
			currentGUI.getMenuItems().setForwardNavigationItemsEnabled(false);
		}else{
			setForwardNavigationButtonsEnabled(true);
			currentGUI.getMenuItems().setForwardNavigationItemsEnabled(true);
		}
		
		currentGUI.getMenuItems().setGoToNavigationItemEnabled(maxPages!=1);
		
        //update single mode toolbar to be visible in only SINGLE if set
        if(currentGUI.getThumbnailScrollBar()!=null){
            if(currentGUI.getPdfDecoder().getDisplayView() == Display.SINGLE_PAGE){

                currentGUI.setScrollBarPolicy(GUI.ScrollPolicy.VERTICAL_NEVER);
                currentGUI.setScrollBarPolicy(GUI.ScrollPolicy.HORIZONTAL_NEVER);

                currentGUI.setThumbnailScrollBarVisibility(true);
            }else if (currentGUI.getPdfDecoder().getDisplayView() == Display.PAGEFLOW){

                currentGUI.setScrollBarPolicy(GUI.ScrollPolicy.VERTICAL_NEVER);
                currentGUI.setScrollBarPolicy(GUI.ScrollPolicy.HORIZONTAL_NEVER);

                currentGUI.setThumbnailScrollBarVisibility(false);
            }else{

                currentGUI.setScrollBarPolicy(GUI.ScrollPolicy.VERTICAL_AS_NEEDED);
                currentGUI.setScrollBarPolicy(GUI.ScrollPolicy.HORIZONTAL_AS_NEEDED);

                currentGUI.setThumbnailScrollBarVisibility(false);
            }
        }
	}
    
    /**
    * This is where we setup the Button Styles
    */
    public void setupButtonStyle(){
        topButtons.setBorder(BorderFactory.createEmptyBorder());
        topButtons.setLayout(new FlowLayout(FlowLayout.LEADING));
        topButtons.setFloatable(false);
        topButtons.setFont(new Font("SansSerif", Font.PLAIN, 8));
    }
    
    @Override
    public void setVisible(final boolean set){
        
        topButtons.setVisible(set);
        
    }
    
    @Override
    public void setEnabled(final boolean set){
        
        topButtons.setEnabled(set);
        
    }

}
