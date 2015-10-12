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
 * SwingMenuItems.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import org.jpedal.display.Display;
import org.jpedal.display.swing.SingleDisplay;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.Buttons;
import org.jpedal.examples.viewer.gui.CommandListener;
import org.jpedal.examples.viewer.gui.generic.GUIButtons;
import org.jpedal.examples.viewer.gui.generic.GUIMenuItems;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.JavaFXHelper;
import org.jpedal.utils.Messages;

/**
 * This class controls everything todo with Menu Items, it holds the objects and
 * their corresponding methods.
 *
 * To initialise the object/class call init()
 */
public class SwingMenuItems extends GUIMenuItems {

    
    /**holds all menu entries (File, View, Help)*/
	private JMenuBar currentMenu =new JMenuBar();
    
    //Menu items for gui
    private JMenu fileMenu;
    private JMenu openMenu;
    private JMenuItem open;
    private JMenuItem openUrl;
    private JMenuItem save;
    private JMenuItem reSaveAsForms;
    private JMenuItem find;
    private JMenuItem documentProperties;
    private JMenuItem signPDF;
    private JMenuItem print;
    //private JMenuItem recentDocuments;
    private JMenuItem exit;
    private JMenu editMenu;
    private JMenuItem copy;
    private JMenuItem selectAll;
    private JMenuItem deselectAll;
    private JMenuItem preferences;
    private JMenu viewMenu;
    private JMenu goToMenu;
    private JMenuItem firstPage;
    private JMenuItem backPage;
    private JMenuItem forwardPage;
    private JMenuItem lastPage;
    private JMenuItem goTo;
    private JMenuItem previousDocument;
    private JMenuItem nextDocument;
    private JMenu pageLayoutMenu;
    private JMenuItem single;
    private JMenuItem continuous;
    private JMenuItem facing;
    private JMenuItem continuousFacing;
    private JMenuItem pageFlow;
    private JMenuItem textSelect;
    private JCheckBoxMenuItem separateCover;
    private JMenuItem panMode;
    private JMenuItem fullscreen;
    private JMenu windowMenu;
    private JMenuItem cascade;
    private JMenuItem tile;
    private JMenu exportMenu;
    private JMenu pdfMenu;
    private JMenuItem onePerPage;
    private JMenuItem nup;
    private JMenuItem handouts;
    private JMenu contentMenu;
    private JMenuItem images;
    private JMenuItem text;
    private JMenuItem bitmap;
    private JMenu pageToolsMenu;
    private JMenuItem rotatePages;
    private JMenuItem deletePages;
    private JMenuItem addPage;
    private JMenuItem addHeaderFooter;
    private JMenuItem stampText;
    private JMenuItem stampImage;
    private JMenuItem crop;
    private JMenu helpMenu;
    private JMenuItem visitWebsite;
    private JMenuItem tipOfTheDay;
    //private JMenuItem checkUpdates;
    private JMenuItem about;


    public SwingMenuItems(final PropertiesFile properties) {
        super(properties);
    }
    
    @Override
    public boolean isMenuItemExist(final int ID){
        return getMenuItem(ID) != null;
    }
    
    @Override
    public void setMenuItem(final int ID, final boolean enabled, final boolean visible) {
        if(ID == Commands.CURRENTMENU){
            currentMenu.setEnabled(enabled);
            currentMenu.setVisible(visible);
        }else{
            getMenuItem(ID).setEnabled(enabled);
            getMenuItem(ID).setVisible(visible);
        }
    }
    
    private JMenu getMenu(final int ID) {
        switch (ID) {
            case Commands.FILEMENU:
                return fileMenu;
            case Commands.EDITMENU:
                return editMenu;
            case Commands.OPENMENU:
                return openMenu;
            case Commands.VIEWMENU:
                return viewMenu;
            case Commands.GOTOMENU:
                return goToMenu;
            case Commands.PAGELAYOUTMENU:
                return pageLayoutMenu;
            case Commands.HELP:
                return helpMenu;
            case Commands.WINDOWMENU:
                return windowMenu;
        }
        return null;
    }
    
    private JMenuItem getMenuItem(final int ID) {
        switch (ID) {
            case Commands.FILEMENU:
                return fileMenu;
            case Commands.OPENMENU:
                return openMenu;
            case Commands.OPENFILE:
                return open;
            case Commands.OPENURL:
                return openUrl;
            case Commands.SAVE:
                return save;
            case Commands.RESAVEASFORM:
                return reSaveAsForms;
            case Commands.FIND:
                return find;
            case Commands.DOCINFO:
                return documentProperties;
            case Commands.SIGN:
                return signPDF;
            case Commands.PRINT:
                return print;
            case Commands.EXIT:
                return exit;
            case Commands.EDITMENU:
                return editMenu;
            case Commands.COPY:
                return copy;
            case Commands.SELECTALL:
                return selectAll;
            case Commands.DESELECTALL:
                return deselectAll;
            case Commands.PREFERENCES:
                return preferences;
            case Commands.VIEWMENU:
                return viewMenu;
            case Commands.GOTOMENU:
                return goToMenu;
            case Commands.FIRSTPAGE:
                return firstPage;
            case Commands.BACKPAGE:
                return backPage;
            case Commands.FORWARDPAGE:
                return forwardPage;
            case Commands.LASTPAGE:
                return lastPage;
            case Commands.GOTO:
                return goTo;
            case Commands.PREVIOUSDOCUMENT:
                return previousDocument;
            case Commands.NEXTDOCUMENT:
                return nextDocument;
            case Commands.PAGELAYOUTMENU:
                return pageLayoutMenu;
            case Commands.SINGLE:
                return single;
            case Commands.CONTINUOUS:
                return continuous;
            case Commands.FACING:
                return facing;
            case Commands.CONTINUOUS_FACING:
                return continuousFacing;
            case Commands.PAGEFLOW:
                return pageFlow;
            case Commands.TEXTSELECT:
                return textSelect;
            case Commands.SEPARATECOVER:
                return separateCover;
            case Commands.PANMODE:
                return panMode;
            case Commands.FULLSCREEN:
                return fullscreen;
            case Commands.WINDOWMENU:
                return windowMenu;
            case Commands.CASCADE:
                return cascade;
            case Commands.TILE:
                return tile;
            case Commands.EXPORTMENU:
                return exportMenu;
            case Commands.PDFMENU:
                return pdfMenu;
            case Commands.ONEPERPAGE:
                return onePerPage;
            case Commands.NUP:
                return nup;
            case Commands.HANDOUTS:
                return handouts;
            case Commands.CONTENTMENU:
                return contentMenu;
            case Commands.IMAGES:
                return images;
            case Commands.TEXT:
                return text;
            case Commands.BITMAP:
                return bitmap;
            case Commands.PAGETOOLSMENU:
                return pageToolsMenu;
            case Commands.ROTATE:
                return rotatePages;
            case Commands.DELETE:
                return deletePages;
            case Commands.ADD:
                return addPage;
            case Commands.ADDHEADERFOOTER:
                return addHeaderFooter;
            case Commands.STAMPTEXT:
                return stampText;
            case Commands.STAMPIMAGE:
                return stampImage;
            case Commands.CROP:
                return crop;
            case Commands.HELP:
                return helpMenu;
            case Commands.VISITWEBSITE:
                return visitWebsite;
            case Commands.TIP:
                return tipOfTheDay;
//            case Commands.UPDATE:
//                return checkUpdates;
            case Commands.ABOUT:
                return about;
        }

        return null;

    }
    
    public JMenuBar getCurrentMenu(){
        return currentMenu;
    }
    
    @Override
    public void dispose(){
        if(currentMenu!=null) {
            currentMenu.removeAll();
        }
		currentMenu =null;
    }
    
    @Override
    public void setBackNavigationItemsEnabled(final boolean enabled){
    	backPage.setEnabled(enabled);
		firstPage.setEnabled(enabled);
    }
    
    @Override
    public void setForwardNavigationItemsEnabled(final boolean enabled){
    	forwardPage.setEnabled(enabled);
		lastPage.setEnabled(enabled);
    }
    
    @Override
    public void setGoToNavigationItemEnabled(final boolean enabled){
    	goTo.setEnabled(enabled);
    }
    
	protected void addMenuItem(final JMenu parentMenu, final String text, final String toolTip, final int ID) {

        boolean isCheckBox = false; //default value
        if (ID == Commands.SEPARATECOVER || ID == Commands.PANMODE || ID == Commands.TEXTSELECT) {
            isCheckBox = true;
        }
        
        final SwingID menuItem;
        if (isCheckBox) {
            menuItem = new SwingCheckBoxMenuItem(text);
        } else {
            menuItem = new SwingMenuItem(text);
        }

        if(!toolTip.isEmpty()) {
            menuItem.setToolTipText(toolTip);
        }
		menuItem.setID(ID);
		setKeyAccelerators(ID,(JMenuItem)menuItem);

		//add listener
		menuItem.addActionListener(currentCommandListener.getSwingCommandListener());

		switch(ID){
		case Commands.OPENFILE :
			open = (JMenuItem)menuItem;
			parentMenu.add(open);
			break;
		case Commands.OPENURL :
			openUrl = (JMenuItem)menuItem;
			parentMenu.add(openUrl);
			break;
		case Commands.SAVE :
			save = (JMenuItem)menuItem;
			parentMenu.add(save);
			break;
		case Commands.SAVEFORM :
			reSaveAsForms = (JMenuItem)menuItem;
			//add name to resave option so fest can get to it.
			reSaveAsForms.setName("resaveForms");
			parentMenu.add(reSaveAsForms);
			break;
		case Commands.FIND :
			find = (JMenuItem)menuItem;
			parentMenu.add(find);
			break;
		case Commands.DOCINFO :
			documentProperties = (JMenuItem)menuItem;
			parentMenu.add(documentProperties);
			break;
        case Commands.SIGN :
			signPDF = (JMenuItem)menuItem;
			parentMenu.add(signPDF);
			break;
		case Commands.PRINT :
			print = (JMenuItem)menuItem;
			parentMenu.add(print);
			break;
		case Commands.EXIT :
			exit = (JMenuItem)menuItem;
            //set name to exit so fest can find it
            exit.setName("exit");
			parentMenu.add(exit);
			break;
		case Commands.COPY :
			copy = (JMenuItem)menuItem;
			parentMenu.add(copy);
			break;
		case Commands.SELECTALL :
			selectAll = (JMenuItem)menuItem;
			parentMenu.add(selectAll);
			break;
		case Commands.DESELECTALL :
			deselectAll = (JMenuItem)menuItem;
			parentMenu.add(deselectAll);
			break;
		case Commands.PREFERENCES :
			preferences = (JMenuItem)menuItem;
			parentMenu.add(preferences);
			break;
		case Commands.FIRSTPAGE :
			firstPage = (JMenuItem)menuItem;
			parentMenu.add(firstPage);
			break;
		case Commands.BACKPAGE :
			backPage = (JMenuItem)menuItem;
			parentMenu.add(backPage);
			break;
		case Commands.FORWARDPAGE :
			forwardPage = (JMenuItem)menuItem;
			parentMenu.add(forwardPage);
			break;
		case Commands.LASTPAGE :
			lastPage = (JMenuItem)menuItem;
			parentMenu.add(lastPage);
			break;
		case Commands.GOTO :
			goTo = (JMenuItem)menuItem;
			parentMenu.add(goTo);
			break;
		case Commands.PREVIOUSDOCUMENT :
			previousDocument = (JMenuItem)menuItem;
			parentMenu.add(previousDocument);
			break;
		case Commands.NEXTDOCUMENT :
			nextDocument = (JMenuItem)menuItem;
			parentMenu.add(nextDocument);
			break;
		case Commands.FULLSCREEN :
			fullscreen = (JMenuItem)menuItem;
			parentMenu.add(fullscreen);
			break;
		case Commands.MOUSEMODE :
			fullscreen = (JMenuItem)menuItem;
			parentMenu.add(fullscreen);
			break;
		case Commands.PANMODE :
			panMode = (JMenuItem)menuItem;
			parentMenu.add(panMode);
			break;
		case Commands.TEXTSELECT :
			textSelect = (JMenuItem)menuItem;
			textSelect.setSelected(true);
			parentMenu.add(textSelect);
			break;
        case Commands.SEPARATECOVER :
            separateCover = (JCheckBoxMenuItem)menuItem;
            final boolean separateCoverOn = properties.getValue("separateCoverOn").toLowerCase().equals("true");
            separateCover.setState(separateCoverOn);
            SingleDisplay.default_separateCover = separateCoverOn;
            parentMenu.add(separateCover);
            break;
		case Commands.CASCADE :
			cascade = (JMenuItem)menuItem;
			parentMenu.add(cascade);
			break;
		case Commands.TILE :
			tile = (JMenuItem)menuItem;
			parentMenu.add(tile);
			break;
		case Commands.PDF :
			onePerPage = (JMenuItem)menuItem;
			parentMenu.add(onePerPage);
			break;
		case Commands.NUP :
			nup = (JMenuItem)menuItem;
			parentMenu.add(nup);
			break;
		case Commands.HANDOUTS :
			handouts = (JMenuItem)menuItem;
			parentMenu.add(handouts);
			break;
		case Commands.IMAGES :
			images = (JMenuItem)menuItem;
			parentMenu.add(images);
			break;
		case Commands.TEXT :
			this.text = (JMenuItem)menuItem;
			parentMenu.add(this.text);
			break;
		case Commands.BITMAP :
			bitmap = (JMenuItem)menuItem;
			parentMenu.add(bitmap); break;
		case Commands.ROTATE :
			rotatePages = (JMenuItem)menuItem;
			parentMenu.add(rotatePages); break;
		case Commands.DELETE :
			deletePages = (JMenuItem)menuItem;
			parentMenu.add(deletePages);
			break;
		case Commands.ADD :
			addPage = (JMenuItem)menuItem;
			parentMenu.add(addPage);
			break;
		case Commands.ADDHEADERFOOTER :
			addHeaderFooter = (JMenuItem)menuItem;
			parentMenu.add(addHeaderFooter);
			break;
		case Commands.STAMPTEXT :
			stampText = (JMenuItem)menuItem;
			parentMenu.add(stampText);
			break;
		case Commands.STAMPIMAGE :
			stampImage = (JMenuItem)menuItem;
			parentMenu.add(stampImage);
			break;
		case Commands.SETCROP :
			crop = (JMenuItem)menuItem;
			parentMenu.add(crop);
			break;
		case Commands.VISITWEBSITE :
			visitWebsite = (JMenuItem)menuItem;
			parentMenu.add(visitWebsite);
			break;
		case Commands.TIP :
			tipOfTheDay = (JMenuItem)menuItem;
			parentMenu.add(tipOfTheDay);
			break;
//		case Commands.UPDATE :
//			checkUpdates = (JMenuItem)menuItem;
//			parentMenu.add(checkUpdates);
//			break;
		case Commands.ABOUT :
			about = (JMenuItem)menuItem;
			parentMenu.add(about);
			break;


		default :
            if (menuItem instanceof JMenuItem) {
                parentMenu.add((JMenuItem) menuItem);
            } else if (menuItem instanceof JCheckBoxMenuItem) {
                parentMenu.add((JCheckBoxMenuItem) menuItem);
            }
		}
	}

    /**
     * sets up layout menu (controls page views - Multiple, facing,etc)
     */
    protected void initLayoutMenus(final JMenu pageLayout, final String[] descriptions, final int[] value, final GUIButtons buttons, final Commands currentCommands, final boolean isSingle) {

        final int count=value.length;
        for(int i=0;i<count;i++){
            final JCheckBoxMenuItem pageView=new JCheckBoxMenuItem(descriptions[i]);
            pageView.setBorder(BorderFactory.createEmptyBorder());
            ((Buttons)buttons).getLayoutGroup().add(pageView);
            if(i==0) {
                pageView.setSelected(true);
            }

            if(pageLayout!=null){

            	switch(value[i]){
            	case Display.SINGLE_PAGE :
            		single = pageView;
            		single.addActionListener(new ActionListener() {
            			@Override
                        public void actionPerformed(final ActionEvent e) {
            				currentCommands.executeCommand(Commands.SINGLE, null);
            			}
            		});
            		pageLayout.add(single);
            		break;
            	case Display.CONTINUOUS :
            		continuous = pageView;
            		continuous.addActionListener(new ActionListener() {
            			@Override
                        public void actionPerformed(final ActionEvent e) {
            				currentCommands.executeCommand(Commands.CONTINUOUS, null);
            			}
            		});
            		pageLayout.add(continuous);
            		break;
            	case Display.FACING :
            		facing = pageView;
            		facing.addActionListener(new ActionListener() {
            			@Override
                        public void actionPerformed(final ActionEvent e) {
            				currentCommands.executeCommand(Commands.FACING, null);
            			}
            		});
            		pageLayout.add(facing);
            		break;
            	case Display.CONTINUOUS_FACING :
            		continuousFacing = pageView;
            		continuousFacing.addActionListener(new ActionListener() {
            			@Override
                        public void actionPerformed(final ActionEvent e) {
            				currentCommands.executeCommand(Commands.CONTINUOUS_FACING, null);
            			}
            		});
            		pageLayout.add(continuousFacing);
            		break;
            	case Display.PAGEFLOW :
            		pageFlow = pageView;
            		pageFlow.addActionListener(new ActionListener() {
            			@Override
                        public void actionPerformed(final ActionEvent e) {
            				currentCommands.executeCommand(Commands.PAGEFLOW, null);
            			}
            		});
            		pageLayout.add(pageFlow);
            		break;
            	}            	
            }
        }

        if(!isSingle) {
            return;
        }

        //default is off
        buttons.setPageLayoutButtonsEnabled(false);


    }
    
    @Override
    public void setMenusForDisplayMode(final int commandIDForDislayMode, final int mouseMode) {
        
        switch(commandIDForDislayMode){
            
            case Commands.SINGLE:
                textSelect.setEnabled(true);
                panMode.setEnabled(true);
                textSelect.setSelected(true);
                panMode.setSelected(false);
                break;
            
            case Commands.PAGEFLOW:
                textSelect.setEnabled(false);
					panMode.setEnabled(false);
					textSelect.setSelected(false);
					panMode.setSelected(true);
                break;
                
            case Commands.CONTINUOUS:
                textSelect.setEnabled(false);
                panMode.setEnabled(true);
                textSelect.setSelected(false);
                panMode.setSelected(true);
                break;
                
            case Commands.CONTINUOUS_FACING:
                textSelect.setEnabled(false);
                panMode.setEnabled(true);
                textSelect.setSelected(false);
                panMode.setSelected(true);
                break;
                
            case Commands.FACING:
                textSelect.setEnabled(false);
                panMode.setEnabled(true);
                textSelect.setSelected(false);
                panMode.setSelected(true);
                break;
                
            case Commands.MOUSEMODE:
                switch(mouseMode){
                    case 0 : //Text Select
                        textSelect.setSelected(true);
                        panMode.setSelected(false);
                        break;
                    case 1 : //Pan Mode
                        textSelect.setSelected(false);
                        panMode.setSelected(true);
                        break;
                }
        }
    }
    
    /**
     * add MenuItem to main menu
     */
    protected void addToMainMenu(final JMenu fileMenuList) {
        fileMenuList.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
        currentMenu.add(fileMenuList);
	}
    
    /**
     * create items on drop down menus
     */
	@Override
    public void createMainMenu(final boolean includeAll, final CommandListener currentCommandListener, final boolean isSingle,
            final Values commonValues, final Commands currentCommands, final GUIButtons buttons){

        this.currentCommandListener=currentCommandListener;

        String addSeparator;

		fileMenu = new JMenu(Messages.getMessage("PdfViewerFileMenu.text"));

		addToMainMenu(fileMenu);

		/**
		 * add open options
		 **/

		openMenu = new JMenu(Messages.getMessage("PdfViewerFileMenuOpen.text"));
        
        openMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
        
		fileMenu.add(openMenu);

		addMenuItem(openMenu,Messages.getMessage("PdfViewerFileMenuOpen.text"),Messages.getMessage("PdfViewerFileMenuTooltip.open"),Commands.OPENFILE);

		addMenuItem(openMenu,Messages.getMessage("PdfViewerFileMenuOpenurl.text"),Messages.getMessage("PdfViewerFileMenuTooltip.openurl"),Commands.OPENURL);


		addSeparator = properties.getValue("Save")
			+ properties.getValue("Resaveasforms")
			+ properties.getValue("Find");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().contains("true")){
			fileMenu.addSeparator();
		}
		
		
		addMenuItem(fileMenu,Messages.getMessage("PdfViewerFileMenuSave.text"),
				Messages.getMessage("PdfViewerFileMenuTooltip.save"),Commands.SAVE);

        //not set if I just run from jar as no IText....
		if(includeAll) {
            addMenuItem(fileMenu,
                    Messages.getMessage("PdfViewerFileMenuResaveForms.text"),
                    Messages.getMessage("PdfViewerFileMenuTooltip.saveForms"),
                    Commands.SAVEFORM);
        }


		// Remember to finish this off
		addMenuItem(fileMenu, Messages.getMessage("PdfViewerFileMenuFind.text"), Messages.getMessage("PdfViewerFileMenuTooltip.find"), Commands.FIND);

		// =====================



		addSeparator = properties.getValue("Documentproperties");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().equals("true")){
			fileMenu.addSeparator();
		}
		addMenuItem(fileMenu,Messages.getMessage("PdfViewerFileMenuDocProperties.text"),
				Messages.getMessage("PdfViewerFileMenuTooltip.props"),Commands.DOCINFO);

        if(commonValues.isEncrypOnClasspath()) {
            addMenuItem(fileMenu,Messages.getMessage("PdfViewerFileMenuSignPDF.text"),
                    Messages.getMessage("PdfViewerFileMenuTooltip.sign"),Commands.SIGN);
        }
        else {
            addMenuItem(fileMenu,Messages.getMessage("PdfViewerFileMenuSignPDF.text"),
                    Messages.getMessage("PdfViewerFileMenuSignPDF.NotPath"),Commands.SIGN);
        }


		addSeparator = properties.getValue("Print");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().equals("true")){
			fileMenu.addSeparator();
		}
		addMenuItem(fileMenu,Messages.getMessage("PdfViewerFileMenuPrint.text"),
				Messages.getMessage("PdfViewerFileMenuTooltip.print"),Commands.PRINT);

		addSeparator = properties.getValue("Recentdocuments");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().equals("true")){
			fileMenu.addSeparator();
			currentCommands.recentDocumentsOption();
		}

		addSeparator = properties.getValue("Exit");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().equals("true")){
			fileMenu.addSeparator();
		}
		addMenuItem(fileMenu,Messages.getMessage("PdfViewerFileMenuExit.text"),
				Messages.getMessage("PdfViewerFileMenuTooltip.exit"),Commands.EXIT);


		//EDIT MENU
		editMenu = new JMenu(Messages.getMessage("PdfViewerEditMenu.text"));
		addToMainMenu(editMenu);

		addMenuItem(editMenu,Messages.getMessage("PdfViewerEditMenuCopy.text"),
				Messages.getMessage("PdfViewerEditMenuTooltip.Copy"),Commands.COPY);

		addMenuItem(editMenu,Messages.getMessage("PdfViewerEditMenuSelectall.text"),
				Messages.getMessage("PdfViewerEditMenuTooltip.Selectall"),Commands.SELECTALL);

		addMenuItem(editMenu,Messages.getMessage("PdfViewerEditMenuDeselectall.text"),
				Messages.getMessage("PdfViewerEditMenuTooltip.Deselectall"),Commands.DESELECTALL);

		addSeparator = properties.getValue("Preferences");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().equals("true")){
			editMenu.addSeparator();
		}
		addMenuItem(editMenu, Messages.getMessage("PdfViewerEditMenuPreferences.text"),
				Messages.getMessage("PdfViewerEditMenuTooltip.Preferences"), Commands.PREFERENCES);


		viewMenu = new JMenu(Messages.getMessage("PdfViewerViewMenu.text"));
		addToMainMenu(viewMenu);

		goToMenu = new JMenu(Messages.getMessage("GoToViewMenuGoto.text"));

        goToMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());

		viewMenu.add(goToMenu);

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.FirstPage"),"",Commands.FIRSTPAGE);

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.BackPage"),"",Commands.BACKPAGE);

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.ForwardPage"),"",Commands.FORWARDPAGE);

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.LastPage"),"",Commands.LASTPAGE);

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.GoTo"),"",Commands.GOTO);

		addSeparator = properties.getValue("Previousdocument")
			+properties.getValue("Nextdocument");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().contains("true")){
			goToMenu.addSeparator();
		}

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.PreviousDoucment"),"",Commands.PREVIOUSDOCUMENT);

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.NextDoucment"),"",Commands.NEXTDOCUMENT);

		/**
		 * add page layout
		 **/
		if(isSingle){
			pageLayoutMenu = new JMenu(Messages.getMessage("PageLayoutViewMenu.PageLayout"));
			pageLayoutMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
			viewMenu.add(pageLayoutMenu);
		}
		
		final String[] descriptions={Messages.getMessage("PageLayoutViewMenu.SinglePage"),Messages.getMessage("PageLayoutViewMenu.Continuous"),Messages.getMessage("PageLayoutViewMenu.ContinousFacing"),Messages.getMessage("PageLayoutViewMenu.Facing"),Messages.getMessage("PageLayoutViewMenu.PageFlow")};
        final int[] value={Display.SINGLE_PAGE, Display.CONTINUOUS,Display.CONTINUOUS_FACING,Display.FACING,Display.PAGEFLOW};

		if(isSingle) {
            initLayoutMenus(pageLayoutMenu, descriptions, value, buttons, currentCommands, isSingle);
        }

        if(properties.getValue("separateCover").equals("true")) {
            addMenuItem(viewMenu, Messages.getMessage("PdfViewerViewMenuSeparateCover.text"), Messages.getMessage("PdfViewerViewMenuTooltip.separateCover"), Commands.SEPARATECOVER);
        }

		// addMenuItem(view,Messages.getMessage("PdfViewerViewMenuAutoscroll.text"),Messages.getMessage("PdfViewerViewMenuTooltip.autoscroll"),Commands.AUTOSCROLL);
        if(properties.getValue("panMode").equals("true") || properties.getValue("textSelect").equals("true")){
        	viewMenu.addSeparator();
        	if(properties.getValue("panMode").equals("true")) {
                addMenuItem(viewMenu, Messages.getMessage("PdfViewerViewMenuPanMode.text"), Messages.getMessage("PdfViewerViewMenuTooltip.panMode"), Commands.PANMODE);
            }

        	if(properties.getValue("textSelect").equals("true")) {
                addMenuItem(viewMenu, Messages.getMessage("PdfViewerViewMenuTextSelectMode.text"), Messages.getMessage("PdfViewerViewMenuTooltip.textSelect"), Commands.TEXTSELECT);
            }
        	viewMenu.addSeparator();
        }
			
		addSeparator = properties.getValue("Fullscreen");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().contains("true")){
			goToMenu.addSeparator();
		}

		//full page mode
		addMenuItem(viewMenu,Messages.getMessage("PdfViewerViewMenuFullScreenMode.text"),Messages.getMessage("PdfViewerViewMenuTooltip.fullScreenMode"),Commands.FULLSCREEN);

		if (!isSingle) {
			windowMenu = new JMenu(Messages.getMessage("PdfViewerWindowMenu.text"));
			addToMainMenu(windowMenu);

			addMenuItem(windowMenu, Messages.getMessage("PdfViewerWindowMenuCascade.text"), "",	Commands.CASCADE);

			addMenuItem(windowMenu, Messages.getMessage("PdfViewerWindowMenuTile.text"), "", Commands.TILE);

		}

		/**
		 * add export menus
		 **/
//		if(commonValues.isItextOnClasspath()){
			exportMenu = new JMenu(Messages.getMessage("PdfViewerExportMenu.text"));
			addToMainMenu(exportMenu);

			//<link><a name="newmenu" />
			/**
			 * external/itext menu option example adding new option to Export menu
			 */
			// addMenuItem(export,"NEW",tooltip,Commands.NEWFUNCTION);
			/**
			 * external/itext menu option example adding new option to Export menu
			 * Tooltip text can be externalised in Messages.getMessage("PdfViewerTooltip.NEWFUNCTION")
			 * and text added into files in res package
			 */


			pdfMenu = new JMenu(Messages.getMessage("PdfViewerExportMenuPDF.text"));
            pdfMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
			exportMenu.add(pdfMenu);

			addMenuItem(pdfMenu,Messages.getMessage("PdfViewerExportMenuOnePerPage.text"),"",Commands.PDF);

			addMenuItem(pdfMenu,Messages.getMessage("PdfViewerExportMenuNUp.text"),"",Commands.NUP);

			addMenuItem(pdfMenu,Messages.getMessage("PdfViewerExportMenuHandouts.text"),"",Commands.HANDOUTS);



			contentMenu=new JMenu(Messages.getMessage("PdfViewerExportMenuContent.text"));
            contentMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
			exportMenu.add(contentMenu);

			addMenuItem(contentMenu,Messages.getMessage("PdfViewerExportMenuImages.text"),"",Commands.IMAGES);

			addMenuItem(contentMenu,Messages.getMessage("PdfViewerExportMenuText.text"),"",Commands.TEXT);


			addMenuItem(exportMenu,Messages.getMessage("PdfViewerExportMenuBitmap.text"),"",Commands.BITMAP);
//		}
        
        /**
         * items options if IText available
         */
//		if(commonValues.isItextOnClasspath()){
			pageToolsMenu = new JMenu(Messages.getMessage("PdfViewerPageToolsMenu.text"));
			addToMainMenu(pageToolsMenu);

			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuRotate.text"),"",Commands.ROTATE);
			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuDelete.text"),"",Commands.DELETE);
			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuAddPage.text"),"",Commands.ADD);
			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuAddHeaderFooter.text"),"",Commands.ADDHEADERFOOTER);
			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuStampText.text"),"",Commands.STAMPTEXT);
			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuStampImage.text"),"",Commands.STAMPIMAGE);
			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuSetCrop.text"),"",Commands.SETCROP);

//		}


		helpMenu = new JMenu(Messages.getMessage("PdfViewerHelpMenu.text"));
		addToMainMenu(helpMenu);

		addMenuItem(helpMenu,Messages.getMessage("PdfViewerHelpMenu.VisitWebsite"),"",Commands.VISITWEBSITE);
		addMenuItem(helpMenu,Messages.getMessage("PdfViewerHelpMenuTip.text"),"",Commands.TIP);
		//addMenuItem(helpMenu,Messages.getMessage("PdfViewerHelpMenuUpdates.text"),"",Commands.UPDATE);
		addMenuItem(helpMenu,Messages.getMessage("PdfViewerHelpMenuabout.text"),Messages.getMessage("PdfViewerHelpMenuTooltip.about"),Commands.ABOUT);


	}
    
    
	/**setup keyboard shortcuts*/
	static void setKeyAccelerators(final int ID, final JMenuItem menuItem){

		int systemMask = java.awt.Event.CTRL_MASK;
		if(DecoderOptions.isRunningOnMac){
			systemMask = java.awt.Event.META_MASK;
		}

		switch(ID){

		case Commands.FIND:
			menuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F,systemMask));
			break;
		case Commands.SAVE:
			menuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,systemMask));
			break;
		case Commands.PRINT:
			menuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P,systemMask));
			break;
		case Commands.EXIT:
			menuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q,systemMask));
			break;
		case Commands.DOCINFO:
			menuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D,systemMask));
			break;
		case Commands.OPENFILE:
			menuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,systemMask));
			break;
		case Commands.OPENURL:
			menuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U,systemMask));
			break;
		case Commands.PREVIOUSDOCUMENT:
			menuItem.setAccelerator( KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LEFT,java.awt.event.KeyEvent.ALT_MASK | java.awt.event.KeyEvent.SHIFT_MASK));
			break;
		case Commands.NEXTDOCUMENT:
			menuItem.setAccelerator( KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_RIGHT,java.awt.event.KeyEvent.ALT_MASK | java.awt.event.KeyEvent.SHIFT_MASK));
			break;
		case Commands.FIRSTPAGE:
			menuItem.setAccelerator( KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_HOME,systemMask));
			break;
		case Commands.BACKPAGE:
			menuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_UP, systemMask));
			break;
		case Commands.FORWARDPAGE:
			menuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DOWN, systemMask));
			break;
		case Commands.LASTPAGE:
			menuItem.setAccelerator( KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_END,systemMask));
			break;
		case Commands.GOTO:
			menuItem.setAccelerator( KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,systemMask | java.awt.event.KeyEvent.SHIFT_MASK));
			break;
		case Commands.BITMAP:
			menuItem.setAccelerator( KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B,java.awt.event.KeyEvent.ALT_MASK));
			break;
		case Commands.COPY:
			menuItem.setAccelerator( KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,systemMask));
			break;
		case Commands.SELECTALL:
			menuItem.setAccelerator( KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A,systemMask));
			break;
		case Commands.DESELECTALL:
			menuItem.setAccelerator( KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A,systemMask+java.awt.event.KeyEvent.SHIFT_DOWN_MASK));
			break;
		case Commands.PREFERENCES:
			menuItem.setAccelerator( KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K,systemMask));
			break;

		}
	}

    @Override
    public void setCheckMenuItemSelected(final int ID, final boolean b) {
        switch(ID){
            case Commands.TEXTSELECT:
                textSelect.setSelected(b);
                break;
            case Commands.PANMODE:
                panMode.setSelected(b);
                break;
            case Commands.SEPARATECOVER:
                separateCover.setSelected(b);
                break;
            default:
                System.out.println("Only TEXTSELECT, PANMODE and SEPARATECOVER are Accepted IDs");
                break;
        }
    }
    
    @Override
    public void ensureNoSeperators() {
        ensureNoSeperators(Commands.FILEMENU);
        ensureNoSeperators(Commands.EDITMENU);
        ensureNoSeperators(Commands.VIEWMENU);
        ensureNoSeperators(Commands.GOTOMENU);
    }

    @Override
    public void ensureNoSeperators(int type) {
        for (int k = 0; k != ((JMenu) getMenuItem(type)).getMenuComponentCount(); k++) {
            if (((JMenu) getMenuItem(type)).getMenuComponent(k).isVisible()) {
                if (((JMenu) getMenuItem(type)).getMenuComponent(k) instanceof JSeparator) {
                    getMenuItem(type).remove(((JMenu) getMenuItem(type)).getMenuComponent(k));
                }
                return;
            }
        }
    }
    
    @Override
    public void addToMenu(final Object menuItem, final int parentMenuID) {
        getMenu(parentMenuID).add((JMenuItem)menuItem);
    }

}
