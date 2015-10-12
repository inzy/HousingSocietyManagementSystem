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
 * JavaFXMenuItems.java
 * ---------------
 */
package org.jpedal.examples.viewer.gui.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import org.jpedal.display.Display;
import org.jpedal.display.GUIDisplay;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.examples.viewer.gui.GUI;
import org.jpedal.examples.viewer.gui.generic.GUIButtons;
import org.jpedal.examples.viewer.gui.generic.GUIMenuItems;
import org.jpedal.examples.viewer.gui.CommandListener;
import org.jpedal.examples.viewer.utils.PropertiesFile;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.Messages;

/**
 * This class controls everything todo with Menu Items, it holds the objects and
 * their corresponding methods.
 *
 * To initialise the object/class call init()
 */
public class JavaFXMenuItems extends GUIMenuItems {

    
    /**holds all menu entries (File, View, Help)*/
	private MenuBar currentMenu =new MenuBar();
    
    //Menu items for gui
    private Menu fileMenu;
    private Menu openMenu;
    private MenuItem open;
    private MenuItem openUrl;
    private MenuItem save;
    private MenuItem reSaveAsForms;
    private MenuItem find;
    private MenuItem documentProperties;
    private MenuItem signPDF;
    private MenuItem print;
    //private MenuItem recentDocuments;
    private MenuItem exit;
    private Menu editMenu;
    private MenuItem copy;
    private MenuItem selectAll;
    private MenuItem deselectAll;
    private MenuItem preferences;
    private Menu viewMenu;
    private Menu goToMenu;
    private MenuItem firstPage;
    private MenuItem backPage;
    private MenuItem forwardPage;
    private MenuItem lastPage;
    private MenuItem goTo;
    private MenuItem previousDocument;
    private MenuItem nextDocument;
    private Menu pageLayoutMenu;
    private MenuItem single;
    private MenuItem continuous;
    private MenuItem facing;
    private MenuItem continuousFacing;
    private MenuItem pageFlow;
    private CheckMenuItem textSelect;
    private CheckMenuItem separateCover;
    private CheckMenuItem panMode;
    private MenuItem fullscreen;
    private Menu windowMenu;
    private MenuItem cascade;
    private MenuItem tile;
    
    private Menu helpMenu;
    private MenuItem visitWebsite;
    private MenuItem tipOfTheDay;
   // private MenuItem checkUpdates;
    private MenuItem about;
    
    private Menu exportMenu;
    private Menu pdfMenu;
    private Menu contentMenu;
    private Menu pageToolsMenu;
    private MenuItem onePerPage;
    private MenuItem nup;
    private MenuItem handouts;
    private MenuItem images;
    private MenuItem text;
    private MenuItem bitmap;
    private MenuItem rotatePages;
    private MenuItem deletePages;
    private MenuItem addPage;
    private MenuItem addHeaderFooter;
    private MenuItem stampText;
    private MenuItem stampImage;
    private MenuItem crop;

    public JavaFXMenuItems(final PropertiesFile properties) {
        super(properties);
    }
    
    private Menu getMenu(final int ID) {
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
    
    private MenuItem getMenuItem(final int ID) {
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
            case Commands.CONTENTMENU:
                return contentMenu;
            case Commands.IMAGES:
                return images;
            case Commands.TEXT:
                return text;
            case Commands.BITMAP:
                return bitmap;
            case Commands.HANDOUTS:
                return handouts;
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
    
    public MenuBar getCurrentMenuFX(){
        return currentMenu;
    }
    
    @Override
    public void dispose(){
        if(currentMenu!=null) {
            currentMenu.getMenus().removeAll(currentMenu.getMenus());
        }
		currentMenu =null;
    }
    
    @Override
    public void setCheckMenuItemSelected(final int ID, final boolean b){
        
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
                if(GUI.debugFX) {
                    System.out.println("Only TEXTSELECT, PANMODE and SEPARATECOVER are Accepted IDs");
                }
                break;
        }
        
    }
    
    @Override
    public void setBackNavigationItemsEnabled(final boolean enabled){
       
    	backPage.setDisable(!enabled);
		firstPage.setDisable(!enabled);
    }
    
    @Override
    public void setForwardNavigationItemsEnabled(final boolean enabled){
    	forwardPage.setDisable(!enabled);
		lastPage.setDisable(!enabled);
    }
    
    @Override
    public void setGoToNavigationItemEnabled(final boolean enabled){
    	goTo.setDisable(!enabled);
    }
    
	protected void addMenuItem(final Menu parentMenu, final String text, final String toolTip, final int ID) {

        boolean isCheckBox = false; //default value
        if (ID == Commands.SEPARATECOVER || ID == Commands.PANMODE || ID == Commands.TEXTSELECT) {
            isCheckBox = true;
        }

        final JavaFXID menuItem;
        if (isCheckBox) {
            menuItem = new JavaFXCheckBoxMenuItem(text);
        } else {
            menuItem = new JavaFXMenuItem(text);
        }

        if (!toolTip.isEmpty()) {
            menuItem.setToolTipText(toolTip);
        }
        menuItem.setID(ID);
        setKeyAccelerators(ID, (MenuItem) menuItem);

		//add listener
		menuItem.setOnAction(currentCommandListener.getJavaFXCommandListener());

		switch(ID){
		case Commands.OPENFILE :
			open = (MenuItem)menuItem;
			parentMenu.getItems().add(open);
			break;
		case Commands.OPENURL :
			openUrl = (MenuItem)menuItem;
			parentMenu.getItems().add(openUrl);
			break;
		case Commands.SAVE :
			save = (MenuItem)menuItem;
			parentMenu.getItems().add(save);
			break;
		case Commands.SAVEFORM :
			reSaveAsForms = (MenuItem)menuItem;
			//add name to resave option so fest can get to it.
            reSaveAsForms.setId("resaveForms");
			parentMenu.getItems().add(reSaveAsForms);
			break;
		case Commands.FIND :
			find = (MenuItem)menuItem;
			parentMenu.getItems().add(find);
			break;
		case Commands.DOCINFO :
			documentProperties = (MenuItem)menuItem;
			parentMenu.getItems().add(documentProperties);
			break;
        case Commands.SIGN :
			signPDF = (MenuItem)menuItem;
			parentMenu.getItems().add(signPDF);
			break;
		case Commands.PRINT :
			print = (MenuItem)menuItem;
			parentMenu.getItems().add(print);
			break;
		case Commands.EXIT :
			exit = (MenuItem)menuItem;
            //set name to exit so fest can find it
            exit.setId("exit");
			parentMenu.getItems().add(exit);
			break;
		case Commands.COPY :
			copy = (MenuItem)menuItem;
			parentMenu.getItems().add(copy);
			break;
		case Commands.SELECTALL :
			selectAll = (MenuItem)menuItem;
			parentMenu.getItems().add(selectAll);
			break;
		case Commands.DESELECTALL :
			deselectAll = (MenuItem)menuItem;
			parentMenu.getItems().add(deselectAll);
			break;
		case Commands.PREFERENCES :
			preferences = (MenuItem)menuItem;
			parentMenu.getItems().add(preferences);
			break;
		case Commands.FIRSTPAGE :
			firstPage = (MenuItem)menuItem;
			parentMenu.getItems().add(firstPage);
			break;
		case Commands.BACKPAGE :
			backPage = (MenuItem)menuItem;
			parentMenu.getItems().add(backPage);
			break;
		case Commands.FORWARDPAGE :
			forwardPage = (MenuItem)menuItem;
			parentMenu.getItems().add(forwardPage);
			break;
		case Commands.LASTPAGE :
			lastPage = (MenuItem)menuItem;
			parentMenu.getItems().add(lastPage);
			break;
		case Commands.GOTO :
			goTo = (MenuItem)menuItem;
			parentMenu.getItems().add(goTo);
			break;
		case Commands.PREVIOUSDOCUMENT :
			previousDocument = (MenuItem)menuItem;
			parentMenu.getItems().add(previousDocument);
			break;
		case Commands.NEXTDOCUMENT :
			nextDocument = (MenuItem)menuItem;
			parentMenu.getItems().add(nextDocument);
			break;
		case Commands.FULLSCREEN :
			fullscreen = (MenuItem)menuItem;
			parentMenu.getItems().add(fullscreen);
			break;
		case Commands.MOUSEMODE :
			fullscreen = (MenuItem)menuItem;
			parentMenu.getItems().add(fullscreen);
			break;
        case Commands.PANMODE:
            panMode = (CheckMenuItem) menuItem;
            panMode.setSelected(false);
            parentMenu.getItems().add(panMode);
            break;
		case Commands.TEXTSELECT :
			textSelect = (CheckMenuItem)menuItem;
			textSelect.setSelected(true);
            parentMenu.getItems().add(textSelect);
            break;
        case Commands.SEPARATECOVER :
            separateCover = (CheckMenuItem)menuItem;
            final boolean separateCoverOn = properties.getValue("separateCoverOn").toLowerCase().equals("true");
            separateCover.setSelected(true);
            GUIDisplay.default_separateCover = separateCoverOn;
            parentMenu.getItems().add(separateCover);
            break;
		case Commands.CASCADE :
			cascade = (MenuItem)menuItem;
			parentMenu.getItems().add(cascade);
			break;
		case Commands.TILE :
			tile = (MenuItem)menuItem;
			parentMenu.getItems().add(tile);
			break;
		case Commands.PDF :
			onePerPage = (MenuItem)menuItem;
			parentMenu.getItems().add(onePerPage);
			break;
		case Commands.NUP :
			nup = (MenuItem)menuItem;
			parentMenu.getItems().add(nup);
			break;
		case Commands.HANDOUTS :
			handouts = (MenuItem)menuItem;
			parentMenu.getItems().add(handouts);
			break;
		case Commands.IMAGES :
			images = (MenuItem)menuItem;
			parentMenu.getItems().add(images);
			break;
		case Commands.TEXT :
			this.text = (MenuItem)menuItem;
			parentMenu.getItems().add(this.text);
			break;
		case Commands.BITMAP :
			bitmap = (MenuItem)menuItem;
			parentMenu.getItems().add(bitmap); 
            break;
        case Commands.ROTATE :
			rotatePages = (MenuItem)menuItem;
			parentMenu.getItems().add(rotatePages); break;
		case Commands.DELETE :
			deletePages = (MenuItem)menuItem;
			parentMenu.getItems().add(deletePages);
			break;
		case Commands.ADD :
			addPage = (MenuItem)menuItem;
			parentMenu.getItems().add(addPage);
			break;
		case Commands.ADDHEADERFOOTER :
			addHeaderFooter = (MenuItem)menuItem;
			parentMenu.getItems().add(addHeaderFooter);
			break;
		case Commands.STAMPTEXT :
			stampText = (MenuItem)menuItem;
			parentMenu.getItems().add(stampText);
			break;
		case Commands.STAMPIMAGE :
			stampImage = (MenuItem)menuItem;
			parentMenu.getItems().add(stampImage);
			break;
		case Commands.SETCROP :
			crop = (MenuItem)menuItem;
			parentMenu.getItems().add(crop);
			break;
		case Commands.VISITWEBSITE :
			visitWebsite = (MenuItem)menuItem;
			parentMenu.getItems().add(visitWebsite);
			break;
		case Commands.TIP :
			tipOfTheDay = (MenuItem)menuItem;
			parentMenu.getItems().add(tipOfTheDay);
			break;
//		case Commands.UPDATE :
//			checkUpdates = (MenuItem)menuItem;
//			parentMenu.getItems().add(checkUpdates);
//			break;
		case Commands.ABOUT :
			about = (MenuItem)menuItem;
			parentMenu.getItems().add(about);
			break;


		default :
            if (menuItem instanceof MenuItem) {
                parentMenu.getItems().add((MenuItem) menuItem);
            } else if (menuItem instanceof CheckMenuItem) {
                parentMenu.getItems().add((CheckMenuItem) menuItem);
            }
		}
        
        disableUnimplementedItems(ID, false);
     
	}

    /**
     * sets up layout menu (controls page views - Multiple, facing,etc)
     */
    protected void initLayoutMenus(final Menu pageLayout, final String[] descriptions, final int[] value, final GUIButtons buttons, final Commands currentCommands, final boolean isSingle) {

        final int count=value.length;
        for(int i=0;i<count;i++){
            final CheckMenuItem pageView=new CheckMenuItem(descriptions[i]);
            if(i==0) {
                pageView.setSelected(true);
            }

            if(pageLayout!=null){

            	switch(value[i]){
            	case Display.SINGLE_PAGE :
            		single = pageView;
                    single.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(final ActionEvent t) {
                            currentCommands.executeCommand(Commands.SINGLE, null);
                        }
                    });
            		pageLayout.getItems().add(single);
            		break;
            	case Display.CONTINUOUS :
            		continuous = pageView;
                    continuous.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(final ActionEvent t) {
                            currentCommands.executeCommand(Commands.CONTINUOUS, null);
                        }
                    });
            		pageLayout.getItems().add(continuous);
            		break;
            	case Display.FACING :
            		facing = pageView;
                    facing.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(final ActionEvent t) {
                            currentCommands.executeCommand(Commands.FACING, null);
                        }
                    });
            		pageLayout.getItems().add(facing);
            		break;
            	case Display.CONTINUOUS_FACING :
            		continuousFacing = pageView;
                    continuousFacing.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(final ActionEvent t) {
                            currentCommands.executeCommand(Commands.CONTINUOUS_FACING, null);
                        }
                    });
            		pageLayout.getItems().add(continuousFacing);
            		break;
            	case Display.PAGEFLOW :
            		pageFlow = pageView;
                    pageFlow.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(final ActionEvent t) {
                            currentCommands.executeCommand(Commands.PAGEFLOW, null);
                        }
                    });
            		pageLayout.getItems().add(pageFlow);
            		break;
            	} 
                
            }
            disableUnimplementedItems(value[i], true);
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
                textSelect.setDisable(true);
                panMode.setDisable(true);
                textSelect.setSelected(true);
                panMode.setSelected(false);
                break;
            
            case Commands.PAGEFLOW:
                textSelect.setDisable(false);
					panMode.setDisable(false);
					textSelect.setSelected(false);
					panMode.setSelected(true);
                break;
                
            case Commands.CONTINUOUS:
                textSelect.setDisable(false);
                panMode.setDisable(true);
                textSelect.setSelected(false);
                panMode.setSelected(true);
                break;
                
            case Commands.CONTINUOUS_FACING:
                textSelect.setDisable(false);
                panMode.setDisable(true);
                textSelect.setSelected(false);
                panMode.setSelected(true);
                break;
                
            case Commands.FACING:
                textSelect.setDisable(false);
                panMode.setDisable(true);
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
    protected void addToMainMenu(final Menu fileMenuList) {
        //fileMenuList.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
        currentMenu.getMenus().add(fileMenuList);
        currentMenu.setUseSystemMenuBar(true);     
	}
    
    /**
     * create items on drop down menus
     */
	@Override
    public void createMainMenu(final boolean includeAll, final CommandListener currentCommandListener, final boolean isSingle,
            final Values commonValues, final Commands currentCommands, final GUIButtons buttons){

        this.currentCommandListener=currentCommandListener;

        String addSeparator;

		fileMenu = new Menu(Messages.getMessage("PdfViewerFileMenu.text"));

		addToMainMenu(fileMenu);

		/**
		 * add open options
		 **/

		openMenu = new Menu(Messages.getMessage("PdfViewerFileMenuOpen.text"));
        
        //openMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
        
		fileMenu.getItems().add(openMenu);

		addMenuItem(openMenu,Messages.getMessage("PdfViewerFileMenuOpen.text"),Messages.getMessage("PdfViewerFileMenuTooltip.open"),Commands.OPENFILE);

		addMenuItem(openMenu,Messages.getMessage("PdfViewerFileMenuOpenurl.text"),Messages.getMessage("PdfViewerFileMenuTooltip.openurl"),Commands.OPENURL);


		addSeparator = properties.getValue("Save")
			+ properties.getValue("Resaveasforms")
			+ properties.getValue("Find");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().contains("true")){
			fileMenu.getItems().add(new SeparatorMenuItem());
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
			fileMenu.getItems().add(new SeparatorMenuItem());
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
			fileMenu.getItems().add(new SeparatorMenuItem());
		}
		addMenuItem(fileMenu,Messages.getMessage("PdfViewerFileMenuPrint.text"),
				Messages.getMessage("PdfViewerFileMenuTooltip.print"),Commands.PRINT);

		addSeparator = properties.getValue("Recentdocuments");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().equals("true")){
			fileMenu.getItems().add(new SeparatorMenuItem());
			currentCommands.recentDocumentsOption();
		}

		addSeparator = properties.getValue("Exit");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().equals("true")){
			fileMenu.getItems().add(new SeparatorMenuItem());
		}
		addMenuItem(fileMenu,Messages.getMessage("PdfViewerFileMenuExit.text"),
				Messages.getMessage("PdfViewerFileMenuTooltip.exit"),Commands.EXIT);

		//EDIT MENU
		editMenu = new Menu(Messages.getMessage("PdfViewerEditMenu.text"));
		addToMainMenu(editMenu);

		addMenuItem(editMenu,Messages.getMessage("PdfViewerEditMenuCopy.text"),
				Messages.getMessage("PdfViewerEditMenuTooltip.Copy"),Commands.COPY);

		addMenuItem(editMenu,Messages.getMessage("PdfViewerEditMenuSelectall.text"),
				Messages.getMessage("PdfViewerEditMenuTooltip.Selectall"),Commands.SELECTALL);

		addMenuItem(editMenu,Messages.getMessage("PdfViewerEditMenuDeselectall.text"),
				Messages.getMessage("PdfViewerEditMenuTooltip.Deselectall"),Commands.DESELECTALL);

		addSeparator = properties.getValue("Preferences");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().equals("true")){
			editMenu.getItems().add(new SeparatorMenuItem());
		}
		addMenuItem(editMenu, Messages.getMessage("PdfViewerEditMenuPreferences.text"),
				Messages.getMessage("PdfViewerEditMenuTooltip.Preferences"), Commands.PREFERENCES);


		viewMenu = new Menu(Messages.getMessage("PdfViewerViewMenu.text"));
		addToMainMenu(viewMenu);

		goToMenu = new Menu(Messages.getMessage("GoToViewMenuGoto.text"));

        //goToMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());

		viewMenu.getItems().add(goToMenu);

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.FirstPage"),"",Commands.FIRSTPAGE);

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.BackPage"),"",Commands.BACKPAGE);

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.ForwardPage"),"",Commands.FORWARDPAGE);

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.LastPage"),"",Commands.LASTPAGE);

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.GoTo"),"",Commands.GOTO);

		addSeparator = properties.getValue("Previousdocument")
			+properties.getValue("Nextdocument");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().contains("true")){
			goToMenu.getItems().add(new SeparatorMenuItem());
		}

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.PreviousDoucment"),"",Commands.PREVIOUSDOCUMENT);

		addMenuItem(goToMenu,Messages.getMessage("GoToViewMenuGoto.NextDoucment"),"",Commands.NEXTDOCUMENT);

		/**
		 * add page layout
		 **/
		if(isSingle){
			pageLayoutMenu = new Menu(Messages.getMessage("PageLayoutViewMenu.PageLayout"));
			//pageLayoutMenu.getPopupMenu().setLightWeightPopupEnabled(!JavaFXHelper.isJavaFXAvailable());
			viewMenu.getItems().add(pageLayoutMenu);
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
        	viewMenu.getItems().add(new SeparatorMenuItem());
        	if(properties.getValue("panMode").equals("true")) {
                addMenuItem(viewMenu, Messages.getMessage("PdfViewerViewMenuPanMode.text"), Messages.getMessage("PdfViewerViewMenuTooltip.panMode"), Commands.PANMODE);
            }

        	if(properties.getValue("textSelect").equals("true")) {
                addMenuItem(viewMenu, Messages.getMessage("PdfViewerViewMenuTextSelectMode.text"), Messages.getMessage("PdfViewerViewMenuTooltip.textSelect"), Commands.TEXTSELECT);
            }
        	viewMenu.getItems().add(new SeparatorMenuItem());
        }
			
		addSeparator = properties.getValue("Fullscreen");
		if(!addSeparator.isEmpty() && addSeparator.toLowerCase().contains("true")){
			goToMenu.getItems().add(new SeparatorMenuItem());
		}

		//full page mode
		addMenuItem(viewMenu,Messages.getMessage("PdfViewerViewMenuFullScreenMode.text"),Messages.getMessage("PdfViewerViewMenuTooltip.fullScreenMode"),Commands.FULLSCREEN);

		if (!isSingle) {
			windowMenu = new Menu(Messages.getMessage("PdfViewerWindowMenu.text"));
			addToMainMenu(windowMenu);

			addMenuItem(windowMenu, Messages.getMessage("PdfViewerWindowMenuCascade.text"), "",	Commands.CASCADE);

			addMenuItem(windowMenu, Messages.getMessage("PdfViewerWindowMenuTile.text"), "", Commands.TILE);

		}

		/**
		 * add export menus
		 **/
//		if(commonValues.isItextOnClasspath()){
			exportMenu = new Menu(Messages.getMessage("PdfViewerExportMenu.text"));
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


			pdfMenu = new Menu(Messages.getMessage("PdfViewerExportMenuPDF.text"));
			exportMenu.getItems().add(pdfMenu);

			addMenuItem(pdfMenu,Messages.getMessage("PdfViewerExportMenuOnePerPage.text"),"",Commands.PDF);

			addMenuItem(pdfMenu,Messages.getMessage("PdfViewerExportMenuNUp.text"),"",Commands.NUP);

			addMenuItem(pdfMenu,Messages.getMessage("PdfViewerExportMenuHandouts.text"),"",Commands.HANDOUTS);



			contentMenu=new Menu(Messages.getMessage("PdfViewerExportMenuContent.text"));
			exportMenu.getItems().add(contentMenu);

			addMenuItem(contentMenu,Messages.getMessage("PdfViewerExportMenuImages.text"),"",Commands.IMAGES);

			addMenuItem(contentMenu,Messages.getMessage("PdfViewerExportMenuText.text"),"",Commands.TEXT);


			addMenuItem(exportMenu,Messages.getMessage("PdfViewerExportMenuBitmap.text"),"",Commands.BITMAP);
//		}
        
        /**
         * items options if IText available
         */
//		if(commonValues.isItextOnClasspath()){
			pageToolsMenu = new Menu(Messages.getMessage("PdfViewerPageToolsMenu.text"));
			addToMainMenu(pageToolsMenu);

			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuRotate.text"),"",Commands.ROTATE);
			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuDelete.text"),"",Commands.DELETE);
			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuAddPage.text"),"",Commands.ADD);
			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuAddHeaderFooter.text"),"",Commands.ADDHEADERFOOTER);
			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuStampText.text"),"",Commands.STAMPTEXT);
			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuStampImage.text"),"",Commands.STAMPIMAGE);
			addMenuItem(pageToolsMenu,Messages.getMessage("PdfViewerPageToolsMenuSetCrop.text"),"",Commands.SETCROP);

//		}


		helpMenu = new Menu(Messages.getMessage("PdfViewerHelpMenu.text"));
		addToMainMenu(helpMenu);

		addMenuItem(helpMenu,Messages.getMessage("PdfViewerHelpMenu.VisitWebsite"),"",Commands.VISITWEBSITE);
		addMenuItem(helpMenu,Messages.getMessage("PdfViewerHelpMenuTip.text"),"",Commands.TIP);
		//addMenuItem(helpMenu,Messages.getMessage("PdfViewerHelpMenuUpdates.text"),"",Commands.UPDATE);
		addMenuItem(helpMenu,Messages.getMessage("PdfViewerHelpMenuabout.text"),Messages.getMessage("PdfViewerHelpMenuTooltip.about"),Commands.ABOUT);


	}
    
    
	/**setup keyboard shortcuts*/
	static void setKeyAccelerators(final int ID, final MenuItem menuItem){
		      switch (ID) {

            case Commands.FIND:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+F"));
                break;
            case Commands.SAVE:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+S"));
                break;
            case Commands.PRINT:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+P"));
                break;
            case Commands.EXIT:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+Q"));
                break;
            case Commands.DOCINFO:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+D"));
                break;
            case Commands.OPENFILE:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+O"));
                break;
            case Commands.OPENURL:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+U"));
                break;
            case Commands.PREVIOUSDOCUMENT:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+LEFT+SHIFT"));
                break;
            case Commands.NEXTDOCUMENT:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+RIGHT+SHIFT"));
                break;
            case Commands.FIRSTPAGE:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+HOME"));
                break;
            case Commands.BACKPAGE:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+UP"));
                break;
            case Commands.FORWARDPAGE:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+DOWN"));
                break;
            case Commands.LASTPAGE:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+END"));
                break;
            case Commands.GOTO:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+N+SHIFT"));
                break;
            case Commands.BITMAP:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+B"));
                break;
            case Commands.COPY:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+C"));
                break;
            case Commands.SELECTALL:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+A"));
                break;
            case Commands.DESELECTALL:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+A+SHIFT"));
                break;
            case Commands.PREFERENCES:
                menuItem.setAccelerator(KeyCombination.keyCombination("Shortcut+K"));
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
        for (int k = 0; k != ((Menu) getMenuItem(type)).getItems().size(); k++) {
            if (((Menu) getMenuItem(type)).getItems().get(k).isVisible()) {
                if (((Menu) getMenuItem(type)).getItems().get(k) instanceof SeparatorMenuItem) {
                    ((Menu) getMenuItem(type)).getItems().remove(k);
                }
                break;
            }
        }
    }
    
    @Override
    public boolean isMenuItemExist(final int ID){
        return getMenuItem(ID) != null;
    }
    
    @Override
    public void setMenuItem(final int ID, final boolean enabled, final boolean visible) {
        if(ID == Commands.CURRENTMENU){
            currentMenu.setDisable(!enabled);
            currentMenu.setVisible(visible);
        }else{
            getMenuItem(ID).setDisable(!enabled);
            getMenuItem(ID).setVisible(visible);
        }
        if(ID == Commands.FULLSCREEN  && DecoderOptions.isRunningOnMac){
              fullscreen.setDisable(true);
        }
    }
    
    @Override
    public void addToMenu(final Object menuItem, final int parentMenuID){
        getMenu(parentMenuID).getItems().add((MenuItem)menuItem);
    }

    /**
     * Temporary Method to Disable unimplemented Viewer Items.
     * Edit and Remove the items from this method as we implement features.
     * 
     */
    public void disableUnimplementedItems(final int ID,  final boolean disableViewModes){
        
        @SuppressWarnings("PointlessBooleanExpression") final boolean debug = !GUI.debugFX;

        final int ALL = -10;
        
        if(ID != ALL){
            if(!disableViewModes){
                switch(ID){
                    /**
                     * View Menu.
                     */
                    case Commands.SEPARATECOVER:
                        separateCover.setDisable(debug);
                        break;
                    /**
                     * File Menu.
                     */
                    case Commands.OPENURL:
                        openUrl.setDisable(debug);
                        break;
                    case Commands.FIND:
                        find.setDisable(debug);
                        break;
                    case Commands.SAVEFORM:
                        reSaveAsForms.setDisable(debug);
                        break;
                    case Commands.SIGN:
                        signPDF.setDisable(debug);
                        break;
                    case Commands.PRINT:
                        print.setDisable(debug);
                        break;

                }
            }
            else{
                /**
                 * Disable View Modes.
                 */
                switch(ID){
                    case Display.CONTINUOUS:
                        continuous.setDisable(debug);
                        break;
                    case Display.FACING:
                        facing.setDisable(debug);
                        break;
                    case Display.PAGEFLOW:
                        pageFlow.setDisable(debug);
                        break;
                    case Display.CONTINUOUS_FACING:
                        continuousFacing.setDisable(debug);
                        break;

                }
            }
        }else{
            separateCover.setDisable(debug);
            openUrl.setDisable(debug);
            reSaveAsForms.setDisable(debug);
            signPDF.setDisable(debug);
            print.setDisable(debug);
            continuous.setDisable(debug);
            facing.setDisable(debug);
            pageFlow.setDisable(debug);
            continuousFacing.setDisable(debug); 
        }
    }   
    
}
